/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.html;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.htmlunit.BrowserVersionFeatures.CSP_META_SINGLE_POLICY;
import static org.htmlunit.BrowserVersionFeatures.CSP_NONCE_HIDING_ENFORCED_ONLY;
import static org.htmlunit.BrowserVersionFeatures.CSP_REPORT_FIREFOX_FORMAT;
import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.ContentSecurityPolicyViolation;
import org.htmlunit.ContentSecurityPolicyViolationListener;
import org.htmlunit.HttpHeader;
import org.htmlunit.HttpMethod;
import org.htmlunit.TopLevelWindow;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.EvaluatorException;
import org.htmlunit.corejs.javascript.ScriptStackElement;
import org.htmlunit.csp.FetchDirectiveKind;
import org.htmlunit.csp.Policy;
import org.htmlunit.csp.directive.ReportUriDirective;
import org.htmlunit.csp.directive.SourceExpressionDirective;
import org.htmlunit.csp.url.GUID;
import org.htmlunit.csp.url.URI;
import org.htmlunit.csp.url.URLWithScheme;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.javascript.host.event.SecurityPolicyViolationEvent;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.StringUtils;
import org.htmlunit.util.UrlUtils;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * The Content-Security-Policy of a page. Contains the enforced policies (from the
 * Content-Security-Policy headers and meta elements) and the report only policies
 * (from the Content-Security-Policy-Report-Only headers).
 * <p>
 * All the check methods return {@code false} if one of the enforced policies does not allow
 * the action. For every policy (enforced or report only) that does not allow the action a
 * securitypolicyviolation event is fired.
 * </p>
 *
 * @author Ronald Brill
 */
public final class ContentSecurityPolicy implements Serializable {

    private static final Log LOG = LogFactory.getLog(ContentSecurityPolicy.class);

    /** The name of the report only header. */
    public static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    /** The maximum length of the sample reported for 'report-sample'. */
    private static final int SAMPLE_LENGTH = 40;

    private static final String BLOCKED_INLINE = "inline";
    private static final String BLOCKED_EVAL = "eval";
    private static final String FORM_ACTION = "form-action";
    private static final String BASE_URI = "base-uri";
    private static final String REPORT_CONTENT_TYPE = "application/csp-report";

    /** The violation events to be fired after the current script has finished. */
    private static final ThreadLocal<List<Runnable>> PENDING_VIOLATIONS = new ThreadLocal<>();

    private final List<String> enforced_ = new ArrayList<>();
    private final List<String> reportOnly_ = new ArrayList<>();
    private final List<String> meta_ = new ArrayList<>();
    private boolean metaSinglePolicy_;
    // for inherited policies the url of the creator document ('self')
    private URL selfUrl_;

    private transient List<Policy> enforcedPolicies_;
    private transient List<Policy> reportOnlyPolicies_;

    /**
     * Ctor.
     */
    private ContentSecurityPolicy() {
        super();
    }

    /**
     * Creates the policy for the given response.
     *
     * @param webResponse the response
     * @return the policy or {@code null} if the response has no policy header
     */
    public static ContentSecurityPolicy fromResponse(final WebResponse webResponse) {
        if (webResponse == null) {
            return null;
        }

        ContentSecurityPolicy result = null;
        for (final NameValuePair header : webResponse.getResponseHeaders()) {
            final String value = header.getValue();
            if (StringUtils.isBlank(value)) {
                continue;
            }

            if (HttpHeader.CONTENT_SECURIRY_POLICY.equalsIgnoreCase(header.getName())) {
                if (result == null) {
                    result = new ContentSecurityPolicy();
                }
                result.enforced_.add(value);
            }
            else if (HEADER_REPORT_ONLY.equalsIgnoreCase(header.getName())) {
                if (result == null) {
                    result = new ContentSecurityPolicy();
                }
                result.reportOnly_.add(value);
            }
        }
        return result;
    }

    /**
     * Returns a copy of the policy of the creator for documents with a local scheme url
     * (about:, data:, blob:). Like the browsers, these documents inherit the policy of the
     * document creating them; for frames this is the parent document, for windows opened
     * by window.open() the opener. The copy is independent from the policy of the creator
     * (policies added later by meta elements are not shared) and 'self' refers to the
     * origin of the creator.
     *
     * @param url the url of the new document
     * @param window the window the new document is loaded into
     * @return the inherited policy or {@code null}
     */
    public static ContentSecurityPolicy inherit(final URL url, final WebWindow window) {
        if (url == null || window == null) {
            return null;
        }
        final String protocol = url.getProtocol();
        if (!"about".equals(protocol) && !"data".equals(protocol) && !"blob".equals(protocol)) {
            return null;
        }

        WebWindow creator = null;
        if (window instanceof FrameWindow) {
            creator = window.getParentWindow();
        }
        else if (window instanceof TopLevelWindow topLevelWindow) {
            creator = topLevelWindow.getOpener();
        }
        if (creator == null || creator == window
                || !(creator.getEnclosedPage() instanceof HtmlPage creatorPage)) {
            return null;
        }

        final ContentSecurityPolicy creatorCsp = creatorPage.getContentSecurityPolicy();
        if (creatorCsp == null) {
            return null;
        }

        final ContentSecurityPolicy result = new ContentSecurityPolicy();
        result.enforced_.addAll(creatorCsp.enforced_);
        result.reportOnly_.addAll(creatorCsp.reportOnly_);
        result.meta_.addAll(creatorCsp.meta_);
        result.metaSinglePolicy_ = creatorCsp.metaSinglePolicy_;
        result.selfUrl_ = selfUrl(creatorPage);
        return result;
    }

    /**
     * Adds a policy defined by a meta element to the given page.
     * The policy is parsed with the meta restrictions (frame-ancestors, sandbox and
     * report-uri are ignored). Depending on the browser, commas split the content into
     * several policies (Chrome) or are part of the single policy (Firefox).
     *
     * @param page the page
     * @param serializedPolicy the policy
     */
    public static void addMetaPolicy(final HtmlPage page, final String serializedPolicy) {
        if (StringUtils.isBlank(serializedPolicy)) {
            return;
        }

        ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            csp = new ContentSecurityPolicy();
            page.setContentSecurityPolicy(csp);
        }
        csp.meta_.add(serializedPolicy);
        csp.metaSinglePolicy_ = page.getWebClient().getBrowserVersion().hasFeature(CSP_META_SINGLE_POLICY);
        csp.enforcedPolicies_ = null;
    }

    /**
     * Checks if the given inline script is allowed.
     *
     * @param page the page
     * @param element the script element
     * @param source the script source
     * @return {@code true} if the script is allowed
     */
    public static boolean allowsInlineScript(final HtmlPage page, final DomElement element, final String source) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Optional<String> nonce = nonce(element);
        final Optional<Boolean> parserInserted = Optional.of(isParserInserted(element));
        final Violation violation = new Violation(FetchDirectiveKind.ScriptSrcElem, BLOCKED_INLINE, source,
                element, documentUrl(page), element.getStartLineNumber(), element.getStartColumnNumber());
        return csp.check(page, violation,
                policy -> policy.allowsInlineScript(nonce, Optional.of(source), parserInserted));
    }

    /**
     * Checks if the given external script is allowed.
     *
     * @param page the page
     * @param element the script element
     * @param url the url of the script
     * @return {@code true} if the script is allowed
     */
    public static boolean allowsExternalScript(final HtmlPage page, final DomElement element, final URL url) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Optional<String> nonce = nonce(element);
        final String integrityAttrib = element.getAttributeDirect("integrity");
        final Optional<String> integrity = ATTRIBUTE_NOT_DEFINED == integrityAttrib
                ? Optional.empty() : Optional.of(integrityAttrib);
        final Optional<URI> scriptUrl = URI.parseURI(url.toExternalForm());
        final Optional<Boolean> parserInserted = Optional.of(isParserInserted(element));
        final Optional<URI> origin = origin(page);
        final Violation violation = new Violation(FetchDirectiveKind.ScriptSrcElem, withoutRef(url), null,
                null, "", 0, 0);
        return csp.check(page, violation,
                policy -> policy.allowsExternalScript(nonce, integrity, scriptUrl, parserInserted, origin));
    }

    /**
     * Checks if the given event handler attribute is allowed.
     *
     * @param page the page
     * @param element the element
     * @param source the source of the event handler
     * @return {@code true} if the event handler is allowed
     */
    public static boolean allowsScriptAsAttribute(final HtmlPage page, final DomElement element,
            final String source) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Violation violation = new Violation(FetchDirectiveKind.ScriptSrcAttr, BLOCKED_INLINE, source,
                element, "", 0, 0);
        final boolean allowed = csp.check(page, violation,
                policy -> policy.allowsScriptAsAttribute(Optional.of(source)));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if the given javascript: url is allowed.
     *
     * @param page the page
     * @param element the element (link, form) or {@code null}
     * @param source the source of the javascript url (including the 'javascript:' prefix)
     * @return {@code true} if the javascript url is allowed
     */
    public static boolean allowsJavaScriptUrl(final HtmlPage page, final DomElement element,
            final String source) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Optional<URI> origin = origin(page);
        final Violation violation = new Violation(FetchDirectiveKind.ScriptSrcElem, BLOCKED_INLINE, source,
                element, "", 0, 0);
        final boolean allowed = csp.check(page, violation,
                policy -> policy.allowsJavascriptUrlNavigation(Optional.of(source), origin));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if a connection (XMLHttpRequest, fetch(), ...) to the given url is allowed (connect-src).
     *
     * @param page the page
     * @param url the url to connect to
     * @param reportedUrl the url reported in case of a violation; for redirects this is the
     *        url of the initial request
     * @return {@code true} if the connection is allowed
     */
    public static boolean allowsConnection(final HtmlPage page, final URL url, final URL reportedUrl) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Optional<URLWithScheme> target = Optional.of(toUrlWithScheme(url));
        final Optional<URI> origin = origin(page);
        final Violation violation = new Violation(FetchDirectiveKind.ConnectSrc, blockedUri(reportedUrl), null,
                null, "", 0, 0);
        // like browsers, ws: and wss: urls are not mapped to http: and https: before matching
        final boolean allowed = csp.check(page, violation, policy -> policy.allowsConnection(target, origin, false));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if the inline style element (style-src-elem) is allowed.
     *
     * @param page the page
     * @param element the style element
     * @param source the content of the style element
     * @return {@code true} if the style is allowed
     */
    public static boolean allowsInlineStyle(final HtmlPage page, final DomElement element, final String source) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Optional<String> nonce = nonce(element);
        final Violation violation = inlineViolation(page, FetchDirectiveKind.StyleSrcElem, source, element);
        final boolean allowed = csp.check(page, violation,
                policy -> policy.allowsInlineStyle(nonce, Optional.of(source)));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if the style attribute (style-src-attr) is allowed.
     * Like the browsers, style changes done by the CSSOM (e.g. element.style.color = 'red')
     * are not checked.
     *
     * @param page the page
     * @param element the element with the style attribute
     * @param source the value of the style attribute
     * @return {@code true} if the style attribute is allowed
     */
    public static boolean allowsStyleAttribute(final HtmlPage page, final DomElement element,
            final String source) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Violation violation = inlineViolation(page, FetchDirectiveKind.StyleSrcAttr, source, element);
        final boolean allowed = csp.check(page, violation,
                policy -> policy.allowsStyleAsAttribute(Optional.of(source)));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if loading the external stylesheet (link element or @import) is allowed (style-src-elem).
     *
     * @param page the page
     * @param element the link element (the nonce is taken from this element) or {@code null} for @import
     * @param url the url of the stylesheet
     * @return {@code true} if the stylesheet is allowed
     */
    public static boolean allowsExternalStyle(final HtmlPage page, final DomElement element, final URL url) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Optional<String> nonce = element == null ? Optional.empty() : nonce(element);
        final Optional<URI> styleUrl = URI.parseURI(url.toExternalForm());
        final Optional<URI> origin = origin(page);
        final Violation violation = new Violation(FetchDirectiveKind.StyleSrcElem, withoutRef(url), null,
                null, "", 0, 0);
        final boolean allowed = csp.check(page, violation,
                policy -> policy.allowsExternalStyle(nonce, styleUrl, origin));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if loading the given url into a frame or iframe is allowed
     * (frame-src, with fallback to child-src and default-src).
     * Like the browsers, the violation is reported for the document and the blocked url
     * is reported without path if it is cross origin (navigations may leak information).
     *
     * @param page the page containing the frame
     * @param url the url to load into the frame (for redirects the target of the redirect)
     * @return {@code true} if loading is allowed
     */
    public static boolean allowsFrame(final HtmlPage page, final URL url) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Optional<URLWithScheme> target = Optional.of(toUrlWithScheme(url));
        final Optional<URI> origin = origin(page);
        final Violation violation = new Violation(FetchDirectiveKind.FrameSrc, navigationBlockedUri(page, url),
                null, null, "", 0, 0);
        final boolean allowed = csp.check(page, violation, policy -> policy.allowsFrame(target, origin));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if the url of an object or embed element is allowed (object-src, with fallback
     * to default-src). HtmlUnit does not load the content of these elements; like the browsers,
     * the url is checked anyway to report the violation.
     *
     * @param element the object or embed element
     * @param src the value of the data (object) or src (embed) attribute
     * @return {@code true} if the url is allowed (or the attribute is empty or invalid)
     */
    public static boolean allowsObject(final DomElement element, final String src) {
        final HtmlPage page = element.getHtmlPageOrNull();
        if (page == null || page.getContentSecurityPolicy() == null
                || ATTRIBUTE_NOT_DEFINED == src || StringUtils.isBlank(src)) {
            return true;
        }

        final URL url;
        try {
            url = page.getFullyQualifiedUrl(src);
        }
        catch (final MalformedURLException e) {
            return true;
        }

        final Optional<URLWithScheme> target = Optional.of(toUrlWithScheme(url));
        final Optional<URI> origin = origin(page);
        final Violation violation = new Violation(FetchDirectiveKind.ObjectSrc, objectBlockedUri(page, url), null,
                null, "", 0, 0);
        final boolean allowed = page.getContentSecurityPolicy()
                .check(page, violation, policy -> policy.allowsObject(target, origin));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if loading the given image url is allowed (img-src, with fallback to default-src).
     *
     * @param page the page containing the image
     * @param url the url of the image
     * @return {@code true} if the image is allowed
     */
    public static boolean allowsImage(final HtmlPage page, final URL url) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Optional<URLWithScheme> target = Optional.of(toUrlWithScheme(url));
        final Optional<URI> origin = origin(page);
        final Violation violation = new Violation(FetchDirectiveKind.ImgSrc, blockedUri(url), null,
                null, "", 0, 0);
        final boolean allowed = csp.check(page, violation, policy -> policy.allowsImage(target, origin));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if creating a worker with the given script url is allowed
     * (worker-src, with fallback to child-src, script-src and default-src).
     *
     * @param page the page creating the worker
     * @param url the url of the worker script
     * @return {@code true} if the worker is allowed
     */
    public static boolean allowsWorker(final HtmlPage page, final URL url) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Optional<URLWithScheme> target = Optional.of(toUrlWithScheme(url));
        final Optional<URI> origin = origin(page);
        final Violation violation = new Violation(FetchDirectiveKind.WorkerSrc, blockedUri(url), null,
                null, "", 0, 0);
        final boolean allowed = csp.check(page, violation, policy -> policy.allowsWorker(target, origin));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if the given url is allowed as base url of the document (base-uri).
     * There is no fallback to default-src for this directive.
     *
     * @param page the page containing the base element
     * @param url the resolved url of the base element
     * @return {@code true} if the base url is allowed
     */
    public static boolean allowsBaseUri(final HtmlPage page, final URL url) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final URLWithScheme target = toUrlWithScheme(url);
        final Optional<URI> origin = origin(page);
        final Violation violation = new Violation(BASE_URI, withoutRef(url), null, null, "", 0, 0);
        final boolean allowed = csp.check(page, violation,
                policy -> policy.baseUri()
                            .map(directive -> Policy.doesUrlMatchSourceListInOrigin(target, directive, origin))
                            .orElse(true));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if the submission of a form to the given url is allowed (form-action).
     * There is no fallback to default-src for this directive.
     *
     * @param page the page containing the form
     * @param url the url the form is submitted to (for javascript: urls the whole url)
     * @param reportedUrl the url reported in case of a violation; for redirects this is the
     *        url of the form submission
     * @return {@code true} if the submission is allowed
     */
    public static boolean allowsFormAction(final HtmlPage page, final URL url, final URL reportedUrl) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Optional<URLWithScheme> target = Optional.of(toUrlWithScheme(url));
        final Optional<URI> origin = origin(page);
        final Violation violation = new Violation(FORM_ACTION, blockedUri(reportedUrl), null, null, "", 0, 0);
        final boolean allowed = csp.check(page, violation,
                policy -> policy.allowsFormAction(target, Optional.of(false), Optional.empty(), origin));
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Checks if the evaluation of strings as code (eval(), new Function(), setTimeout(string)) is allowed.
     *
     * @param page the page
     * @param source the source to evaluate
     * @param lineNumber the line number of the code calling eval or {@code 0} if not known
     * @return {@code true} if the evaluation is allowed
     */
    public static boolean allowsEval(final HtmlPage page, final String source, final int lineNumber) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return true;
        }

        final Violation violation = new Violation(FetchDirectiveKind.ScriptSrc, BLOCKED_EVAL, source,
                null, documentUrl(page), lineNumber, 0);
        final boolean allowed = csp.check(page, violation, Policy::allowsEval);
        firePendingViolationsIfNoScriptRunning(page);
        return allowed;
    }

    /**
     * Fires the securitypolicyviolation events of the violations detected by
     * the current thread so far. Called after a script element was processed and
     * when the outermost script execution has finished.
     */
    public static void firePendingViolations() {
        final List<Runnable> pending = PENDING_VIOLATIONS.get();
        if (pending == null) {
            return;
        }

        // firing an event may add new violations
        while (!pending.isEmpty()) {
            pending.remove(0).run();
        }
        PENDING_VIOLATIONS.remove();
    }

    /**
     * Fires the pending violations if no script is running; otherwise this is done
     * at the end of the script execution.
     */
    private static void firePendingViolationsIfNoScriptRunning(final HtmlPage page) {
        final AbstractJavaScriptEngine<?> engine = page.getWebClient().getJavaScriptEngine();
        if (engine == null || !engine.isScriptRunning()) {
            firePendingViolations();
        }
    }

    private boolean check(final HtmlPage page, final Violation violation, final Predicate<Policy> check) {
        boolean allowed = true;
        for (final Policy policy : getEnforcedPolicies()) {
            if (!check.test(policy)) {
                allowed = false;
                report(page, policy, violation, SecurityPolicyViolationEvent.DISPOSITION_ENFORCE);
            }
        }
        for (final Policy policy : getReportOnlyPolicies()) {
            if (!check.test(policy)) {
                report(page, policy, violation, SecurityPolicyViolationEvent.DISPOSITION_REPORT);
            }
        }
        return allowed;
    }

    /**
     * Logs the violation and fires the securitypolicyviolation event (async)
     * at the target element (if connected) or the document.
     */
    private static void report(final HtmlPage page, final Policy policy, final Violation violation,
            final String disposition) {
        final FetchDirectiveKind directive = violation.directive_;
        final String effectiveDirective = violation.effectiveDirective_;
        final String blockedUri = violation.blockedUri_;
        final String source = violation.source_;
        final DomElement element = violation.target_;
        if (LOG.isWarnEnabled()) {
            LOG.warn((SecurityPolicyViolationEvent.DISPOSITION_REPORT.equals(disposition) ? "[Report Only] " : "")
                    + "Content-Security-Policy violation: '" + effectiveDirective
                    + "' blocked '" + blockedUri + "' in '" + page.getUrl() + "' (policy '" + policy + "').");
        }

        String sample = "";
        if (source != null && directive != null) {
            final SourceExpressionDirective governing =
                    policy.getGoverningDirectiveForEffectiveDirective(directive).orElse(null);
            if (governing != null && governing.reportSample()) {
                sample = source.length() > SAMPLE_LENGTH ? source.substring(0, SAMPLE_LENGTH) : source;
            }
        }

        final DomNode target = element != null && element.isAttachedToPage() ? element : page;
        final int lineNumber = Math.max(0, violation.lineNumber_);
        final int columnNumber = Math.max(0, violation.columnNumber_);
        final String documentUrl = documentUrl(page);
        final String sourceFile = violation.sourceFile_;
        final String originalPolicy = policy.toString();
        final int statusCode = page.getWebResponse() == null ? 0 : page.getWebResponse().getStatusCode();
        final String finalSample = sample;

        final ContentSecurityPolicyViolationListener listener =
                page.getWebClient().getContentSecurityPolicyViolationListener();
        if (listener != null) {
            listener.violation(page, new ContentSecurityPolicyViolation(documentUrl, blockedUri,
                    effectiveDirective, originalPolicy, sourceFile, finalSample, lineNumber, columnNumber,
                    statusCode, SecurityPolicyViolationEvent.DISPOSITION_REPORT.equals(disposition)));
        }

        // like the browsers, report-uri is ignored if the policy is delivered by a meta element
        // or if the policy contains a report-to directive
        final ReportUriDirective reportUri = policy.deliveredViaMeta() || policy.reportTo().isPresent()
                ? null : policy.reportUri().orElse(null);
        if (reportUri != null && !reportUri.getUris().isEmpty()) {
            final String report;
            if (page.getWebClient().getBrowserVersion().hasFeature(CSP_REPORT_FIREFOX_FORMAT)) {
                report = "{\"csp-report\":{"
                        + jsonMember("blocked-uri", blockedUri) + ","
                        + "\"column-number\":" + Math.max(1, columnNumber) + ","
                        + jsonMember("disposition", disposition) + ","
                        + jsonMember("document-uri", documentUrl) + ","
                        + jsonMember("effective-directive", effectiveDirective) + ","
                        + (lineNumber > 0 ? "\"line-number\":" + lineNumber + "," : "")
                        + jsonMember("original-policy", absoluteReportUris(page, originalPolicy, reportUri)) + ","
                        + jsonMember("referrer", "") + ","
                        + (StringUtils.isEmptyOrNull(finalSample) ? "" : jsonMember("script-sample", finalSample) + ",")
                        + (StringUtils.isEmptyOrNull(sourceFile) ? "" : jsonMember("source-file", sourceFile) + ",")
                        + "\"status-code\":" + statusCode + ","
                        + jsonMember("violated-directive", effectiveDirective)
                        + "}}";
            }
            else {
                report = "{\"csp-report\":{"
                        + jsonMember("document-uri", documentUrl) + ","
                        + jsonMember("referrer", "") + ","
                        + jsonMember("violated-directive", effectiveDirective) + ","
                        + jsonMember("effective-directive", effectiveDirective) + ","
                        + jsonMember("original-policy", originalPolicy) + ","
                        + jsonMember("disposition", disposition) + ","
                        + jsonMember("blocked-uri", blockedUri) + ","
                        + (lineNumber > 0 ? "\"line-number\":" + lineNumber + "," : "")
                        + (columnNumber > 0 ? "\"column-number\":" + columnNumber + "," : "")
                        + (StringUtils.isEmptyOrNull(sourceFile) ? "" : jsonMember("source-file", sourceFile) + ",")
                        + "\"status-code\":" + statusCode + ","
                        + jsonMember("script-sample", finalSample)
                        + "}}";
            }
            final List<String> uris = new ArrayList<>(reportUri.getUris());
            addPending(() -> sendReports(page, uris, report));
        }

        final WebWindow window = page.getEnclosingWindow();
        if (window == null || !page.getWebClient().isJavaScriptEnabled()
                || window.getScriptableObject() == null) {
            return;
        }

        final Runnable fireEvent = () -> {
            final SecurityPolicyViolationEvent event = new SecurityPolicyViolationEvent(target);
            event.setDocumentURI(documentUrl);
            event.setBlockedURI(blockedUri);
            event.setEffectiveDirective(effectiveDirective);
            event.setViolatedDirective(effectiveDirective);
            event.setOriginalPolicy(originalPolicy);
            event.setSourceFile(sourceFile);
            event.setSample(finalSample);
            event.setDisposition(disposition);
            event.setStatusCode(statusCode);
            event.setLineNumber(lineNumber);
            event.setColumnNumber(columnNumber);

            final EventTarget jsTarget = target.getScriptableObject();
            ((JavaScriptEngine) page.getWebClient().getJavaScriptEngine())
                    .callSecured(cx -> jsTarget.fireEvent(event), page);
        };

        // browsers queue a task to fire the event; to get a deterministic order
        // we fire the event after the current script has finished
        // (see firePendingViolations())
        addPending(fireEvent);
    }

    private static void addPending(final Runnable runnable) {
        List<Runnable> pending = PENDING_VIOLATIONS.get();
        if (pending == null) {
            pending = new ArrayList<>();
            PENDING_VIOLATIONS.set(pending);
        }
        pending.add(runnable);
    }

    /**
     * Sends the violation report (report-uri) as POST request with content type
     * application/csp-report to all the given uris (relative uris are resolved against the
     * url of the document). Errors are only logged.
     */
    private static void sendReports(final HtmlPage page, final List<String> uris, final String report) {
        final WebClient webClient = page.getWebClient();
        for (final String uri : uris) {
            try {
                final URL url = page.getFullyQualifiedUrl(uri);
                final WebRequest request = new WebRequest(url, HttpMethod.POST);
                request.setAdditionalHeader(HttpHeader.CONTENT_TYPE, REPORT_CONTENT_TYPE);
                request.setCharset(UTF_8);
                request.setRequestBody(report);
                request.setFetchDestination(WebRequest.FetchDestination.REPORT);
                request.setFetchModeOverride(WebRequest.FetchMode.NO_CORS);
                request.setRequestingUrl(page.getUrl());
                webClient.loadWebResponse(request);
            }
            catch (final IOException | RuntimeException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Sending the Content-Security-Policy violation report to '" + uri + "' failed", e);
                }
            }
        }
    }

    /**
     * Returns the policy with absolute report uris (like Firefox).
     */
    private static String absoluteReportUris(final HtmlPage page, final String policy,
            final ReportUriDirective reportUri) {
        final StringBuilder uris = new StringBuilder("report-uri");
        for (final String uri : reportUri.getUris()) {
            String absolute = uri;
            try {
                absolute = page.getFullyQualifiedUrl(uri).toExternalForm();
            }
            catch (final MalformedURLException e) {
                // keep the uri as it is
            }
            uris.append(' ').append(absolute);
        }

        final String[] parts = policy.split(";", -1);
        for (int i = 0; i < parts.length; i++) {
            final String trimmed = parts[i].trim();
            if (trimmed.toLowerCase(Locale.ROOT).startsWith("report-uri")) {
                final String leading = parts[i].substring(0, parts[i].indexOf(trimmed));
                parts[i] = leading + uris;
            }
        }
        return String.join(";", parts);
    }

    private static String jsonMember(final String name, final String value) {
        final StringBuilder json = new StringBuilder();
        json.append('"').append(name).append("\":\"");
        if (value != null) {
            for (int i = 0; i < value.length(); i++) {
                final char c = value.charAt(i);
                switch (c) {
                    case '"':
                        json.append("\\\"");
                        break;
                    case '\\':
                        json.append("\\\\");
                        break;
                    case '\n':
                        json.append("\\n");
                        break;
                    case '\r':
                        json.append("\\r");
                        break;
                    case '\t':
                        json.append("\\t");
                        break;
                    default:
                        if (c < 0x20) {
                            json.append(String.format("\\u%04x", (int) c));
                        }
                        else {
                            json.append(c);
                        }
                }
            }
        }
        return json.append('"').toString();
    }

    private List<Policy> getEnforcedPolicies() {
        if (enforcedPolicies_ == null) {
            final List<Policy> policies = parse(enforced_);
            final Policy.MetaCommaHandling commaHandling = metaSinglePolicy_
                    ? Policy.MetaCommaHandling.SINGLE_POLICY : Policy.MetaCommaHandling.SPLIT;
            for (final String serialized : meta_) {
                policies.addAll(Policy.parseSerializedCSPList(serialized,
                        Policy.PolicyListErrorConsumer.ignored, commaHandling).getPolicies());
            }
            enforcedPolicies_ = policies;
        }
        return enforcedPolicies_;
    }

    private List<Policy> getReportOnlyPolicies() {
        if (reportOnlyPolicies_ == null) {
            reportOnlyPolicies_ = parse(reportOnly_);
        }
        return reportOnlyPolicies_;
    }

    private static List<Policy> parse(final List<String> serializedPolicies) {
        final List<Policy> policies = new ArrayList<>();
        for (final String serialized : serializedPolicies) {
            policies.addAll(Policy.parseSerializedCSPList(serialized,
                    Policy.PolicyListErrorConsumer.ignored).getPolicies());
        }
        return policies;
    }

    /**
     * Creates the violation for inline content (style element or style attribute). Like the browsers,
     * the location is the location of the running script, if the element was created or changed
     * by a script; otherwise the location of the element in the document.
     */
    private static Violation inlineViolation(final HtmlPage page, final FetchDirectiveKind directive,
            final String source, final DomElement element) {
        if (Context.getCurrentContext() != null) {
            final ScriptStackElement[] stack = new EvaluatorException("csp").getScriptStack();
            if (stack.length > 0) {
                return new Violation(directive, BLOCKED_INLINE, source, element,
                        stack[0].fileName, Math.max(0, stack[0].lineNumber), 0);
            }
        }
        return new Violation(directive, BLOCKED_INLINE, source, element,
                documentUrl(page), element.getStartLineNumber(), element.getStartColumnNumber());
    }

    /**
     * Returns the nonce of the element (the nonce slot, not the maybe hidden attribute);
     * empty if the element has no nonce or is not nonceable.
     */
    private static Optional<String> nonce(final DomElement element) {
        final String nonce = element.getNonce();
        if (nonce.isEmpty() || !isNonceable(element)) {
            return Optional.empty();
        }
        return Optional.of(nonce);
    }

    /**
     * Spec 'is element nonceable': elements with an attribute name or value containing
     * '&lt;script' or '&lt;style' are not nonceable (protection against dangling markup).
     */
    private static boolean isNonceable(final DomElement element) {
        for (final DomAttr attr : element.getAttributesMap().values()) {
            if (containsScriptOrStyle(attr.getName()) || containsScriptOrStyle(attr.getValue())) {
                return false;
            }
        }
        return true;
    }

    private static boolean containsScriptOrStyle(final String value) {
        final String lower = value.toLowerCase(Locale.ROOT);
        return lower.contains("<script") || lower.contains("<style");
    }

    /**
     * Nonce hiding: if the page has a policy delivered by a header, the nonce attribute of
     * an element added to the page is replaced by an empty string (the nonce IDL attribute
     * still returns the nonce). Firefox does this only for enforced policies.
     *
     * @param element the element added to the page
     */
    public static void hideNonceIfNeeded(final DomElement element) {
        final HtmlPage page = element.getHtmlPageOrNull();
        if (page == null) {
            return;
        }
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp == null) {
            return;
        }
        if (csp.enforced_.isEmpty()
                && (csp.reportOnly_.isEmpty()
                        || page.getWebClient().getBrowserVersion().hasFeature(CSP_NONCE_HIDING_ENFORCED_ONLY))) {
            return;
        }

        final String nonce = element.getAttributeDirect(DomElement.NONCE_ATTRIBUTE);
        if (ATTRIBUTE_NOT_DEFINED == nonce || nonce.isEmpty()) {
            return;
        }
        element.hideNonce();
    }

    /**
     * Elements created by the parser have a line number; elements created by
     * JavaScript (e.g. document.createElement()) have no line number.
     */
    private static boolean isParserInserted(final DomElement element) {
        return element.getStartLineNumber() != -1;
    }

    private static String documentUrl(final HtmlPage page) {
        return withoutRef(page.getUrl());
    }

    private static String withoutRef(final URL url) {
        final String externalForm = url.toExternalForm();
        final int hashPos = externalForm.indexOf('#');
        if (hashPos == -1) {
            return externalForm;
        }
        return externalForm.substring(0, hashPos);
    }

    /**
     * Converts the given url into the url representation used by the csp library
     * (htmlunit-csp) to match the url against the source expressions of a policy.
     * <ul>
     *   <li>Urls with a host (e.g. http, https, ws, wss) are converted into a
     *       {@link URI} made of the scheme and the host (both lower case), the port
     *       (the default port of the scheme if the url has no explicit port) and the path; the query
     *       and the fragment are not part of the result. Host sources (e.g. {@code https://example.com/api/})
     *       and 'self' are matched against these parts.</li>
     *   <li>Urls without a host (e.g. data:, blob:, about:, file:///) are converted into a
     *       {@link GUID} made of the scheme (lower case) and the remaining part
     *       of the url; such urls are only matched by a scheme source (e.g. {@code data:}),
     *       never by 'self', a host source or {@code *}.</li>
     * </ul>
     * The result is the same as the one of {@link URI#parseURI(String)} or
     * {@link GUID#parseGUID(String)}, but without parsing the url again.
     *
     * @param url the url to convert
     * @return the converted url
     */
    static URLWithScheme toUrlWithScheme(final URL url) {
        final String scheme = url.getProtocol().toLowerCase(Locale.ROOT);
        final String host = url.getHost();
        if (host == null || host.isEmpty()) {
            final String externalForm = url.toExternalForm();
            return new GUID(scheme, externalForm.substring(externalForm.indexOf(':') + 1));
        }

        int port = url.getPort();
        if (port == -1) {
            port = URI.defaultPortForProtocol(scheme);
        }
        return new URI(scheme, host.toLowerCase(Locale.ROOT), port, url.getPath());
    }

    /**
     * Returns the blockedURI reported for the given url; like in browsers only the scheme
     * is reported for urls without host (e.g. data:).
     */
    private static String blockedUri(final URL url) {
        final String host = url.getHost();
        if (host == null || host.isEmpty()) {
            return url.getProtocol();
        }
        return withoutRef(url);
    }

    /**
     * The blocked uri reported for navigations (e.g. frames); like Chrome
     * <ul>
     *   <li>urls without host (e.g. data:) are reported as empty string</li>
     *   <li>cross origin urls are reported as origin (without path)</li>
     *   <li>same origin urls are reported without the fragment</li>
     * </ul>
     */
    private static String navigationBlockedUri(final HtmlPage page, final URL url) {
        final String host = url.getHost();
        if (host == null || host.isEmpty()) {
            return "";
        }
        if (UrlUtils.isSameOrigin(selfUrl(page), url)) {
            return withoutRef(url);
        }

        final String protocol = url.getProtocol();
        final int port = url.getPort();
        if (port == -1 || port == url.getDefaultPort()) {
            return protocol + "://" + host;
        }
        return protocol + "://" + host + ":" + port;
    }

    /**
     * The blocked uri reported for objects (object-src); like the browsers.
     * <ul>
     *   <li>urls without host (e.g. data:) are reported as scheme</li>
     *   <li>cross origin urls are reported as origin (without path)</li>
     *   <li>same origin urls are reported without the fragment</li>
     * </ul>
     */
    private static String objectBlockedUri(final HtmlPage page, final URL url) {
        final String host = url.getHost();
        if (host == null || host.isEmpty()) {
            return url.getProtocol();
        }
        return navigationBlockedUri(page, url);
    }

    private static Optional<URI> origin(final HtmlPage page) {
        return URI.parseURI(selfUrl(page).toExternalForm());
    }

    /**
     * Returns the url used for 'self'; for inherited policies the url of the creator.
     */
    private static URL selfUrl(final HtmlPage page) {
        final ContentSecurityPolicy csp = page.getContentSecurityPolicy();
        if (csp != null && csp.selfUrl_ != null) {
            return csp.selfUrl_;
        }
        return page.getUrl();
    }

    /**
     * The details of a violation.
     */
    private static final class Violation {
        private final FetchDirectiveKind directive_;
        private final String effectiveDirective_;
        private final String blockedUri_;
        private final String source_;
        private final DomElement target_;
        private final String sourceFile_;
        private final int lineNumber_;
        private final int columnNumber_;

        Violation(final FetchDirectiveKind directive, final String blockedUri, final String source,
                final DomElement target, final String sourceFile, final int lineNumber, final int columnNumber) {
            directive_ = directive;
            effectiveDirective_ = directive.getRepr();
            blockedUri_ = blockedUri;
            source_ = source;
            target_ = target;
            sourceFile_ = sourceFile;
            lineNumber_ = lineNumber;
            columnNumber_ = columnNumber;
        }

        /**
         * Ctor for violations of directives that are no fetch directives (e.g. form-action).
         */
        Violation(final String effectiveDirective, final String blockedUri, final String source,
                final DomElement target, final String sourceFile, final int lineNumber, final int columnNumber) {
            directive_ = null;
            effectiveDirective_ = effectiveDirective;
            blockedUri_ = blockedUri;
            source_ = source;
            target_ = target;
            sourceFile_ = sourceFile;
            lineNumber_ = lineNumber;
            columnNumber_ = columnNumber;
        }
    }
}
