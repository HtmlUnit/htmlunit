/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONLOAD_INTERNAL_JAVASCRIPT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLSCRIPT_TRIM_TYPE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SCRIPT_HANDLE_204_AS_ERROR;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_WINDOW;
import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage.JavaScriptLoadResult;
import com.gargoylesoftware.htmlunit.javascript.AbstractJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * A helper class to be used by elements which support {@link ScriptElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Ronny Shapiro
 */
public final class ScriptElementSupport {

    private static final Log LOG = LogFactory.getLog(ScriptElementSupport.class);

    /** Invalid source attribute which should be ignored (used by JS libraries like jQuery). */
    private static final String SLASH_SLASH_COLON = "//:";

    private ScriptElementSupport() {
    }

    /**
     * Lifecycle method invoked after a node and all its children have been added to a page, during
     * parsing of the HTML. Intended to be overridden by nodes which need to perform custom logic
     * after they and all their child nodes have been processed by the HTML parser. This method is
     * not recursive, and the default implementation is empty, so there is no need to call
     * <tt>super.onAllChildrenAddedToPage()</tt> if you implement this method.
     * @param element the element
     * @param postponed whether to use {@link com.gargoylesoftware.htmlunit.javascript.PostponedAction} or no
     */
    public static void onAllChildrenAddedToPage(final DomElement element, final boolean postponed) {
        if (element.getOwnerDocument() instanceof XmlPage) {
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Script node added: " + element.asXml());
        }

        if (!element.getPage().getWebClient().isJavaScriptEngineEnabled()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Script found but not executed because javascript engine is disabled");
            }
            return;
        }

        final ScriptElement script = (ScriptElement) element;
        final String srcAttrib = script.getSrcAttribute();
        if (ATTRIBUTE_NOT_DEFINED != srcAttrib
                && script.isDeferred()) {
            return;
        }

        final WebWindow webWindow = element.getPage().getEnclosingWindow();
        if (webWindow != null) {
            final StringBuilder description = new StringBuilder()
                    .append("Execution of ")
                    .append(srcAttrib == ATTRIBUTE_NOT_DEFINED ? "inline " : "external ")
                    .append(element.getClass().getSimpleName());
            if (srcAttrib != ATTRIBUTE_NOT_DEFINED) {
                description.append(" (").append(srcAttrib).append(")");
            }
            final PostponedAction action = new PostponedAction(element.getPage(), description.toString()) {
                @Override
                public void execute() {
                    // see HTMLDocument.setExecutingDynamicExternalPosponed(boolean)
                    HTMLDocument jsDoc = null;
                    final Window window = webWindow.getScriptableObject();
                    if (window != null) {
                        jsDoc = (HTMLDocument) window.getDocument();
                        jsDoc.setExecutingDynamicExternalPosponed(element.getStartLineNumber() == -1
                                && srcAttrib != ATTRIBUTE_NOT_DEFINED);
                    }
                    try {
                        executeScriptIfNeeded(element);
                    }
                    finally {
                        if (jsDoc != null) {
                            jsDoc.setExecutingDynamicExternalPosponed(false);
                        }
                    }
                }
            };

            final AbstractJavaScriptEngine<?> engine = element.getPage().getWebClient().getJavaScriptEngine();
            if (engine != null
                    && element.hasAttribute("async") && !engine.isScriptRunning()) {
                final HtmlPage owningPage = element.getHtmlPageOrNull();
                owningPage.addAfterLoadAction(action);
            }
            else if (engine != null
                    && (element.hasAttribute("async")
                            || postponed && StringUtils.isBlank(element.getTextContent()))) {
                engine.addPostponedAction(action);
            }
            else {
                try {
                    action.execute();
                }
                catch (final RuntimeException e) {
                    throw e;
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Executes this script node if necessary and/or possible.
     * @param element the element
     */
    public static void executeScriptIfNeeded(final DomElement element) {
        if (!isExecutionNeeded(element)) {
            return;
        }

        final HtmlPage page = (HtmlPage) element.getPage();
        final ScriptElement scriptElement = (ScriptElement) element;

        final String src = scriptElement.getSrcAttribute();
        if (src.equals(SLASH_SLASH_COLON)) {
            executeEvent(element, Event.TYPE_ERROR);
            return;
        }

        if (src != ATTRIBUTE_NOT_DEFINED) {
            if (!src.startsWith(JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
                // <script src="[url]"></script>
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loading external JavaScript: " + src);
                }
                try {
                    scriptElement.setExecuted(true);
                    Charset charset = EncodingSniffer.toCharset(scriptElement.getCharsetAttribute());
                    if (charset == null) {
                        charset = page.getCharset();
                    }

                    JavaScriptLoadResult result = null;
                    final Window win = page.getEnclosingWindow().getScriptableObject();
                    final Document doc = win.getDocument();
                    try {
                        doc.setCurrentScript(element.getScriptableObject());
                        result = page.loadExternalJavaScriptFile(src, charset);
                    }
                    finally {
                        doc.setCurrentScript(null);
                    }

                    if (result == JavaScriptLoadResult.SUCCESS) {
                        executeEvent(element, Event.TYPE_LOAD);
                    }
                    else if (result == JavaScriptLoadResult.DOWNLOAD_ERROR) {
                        executeEvent(element, Event.TYPE_ERROR);
                    }
                    else if (result == JavaScriptLoadResult.NO_CONTENT) {
                        final BrowserVersion browserVersion = page.getWebClient().getBrowserVersion();
                        if (browserVersion.hasFeature(JS_SCRIPT_HANDLE_204_AS_ERROR)) {
                            executeEvent(element, Event.TYPE_ERROR);
                        }
                        else {
                            executeEvent(element, Event.TYPE_LOAD);
                        }
                    }
                }
                catch (final FailingHttpStatusCodeException e) {
                    executeEvent(element, Event.TYPE_ERROR);
                    throw e;
                }
            }
        }
        else if (element.getFirstChild() != null) {
            // <script>[code]</script>
            final Window win = page.getEnclosingWindow().getScriptableObject();
            final Document doc = win.getDocument();
            try {
                doc.setCurrentScript(element.getScriptableObject());
                executeInlineScriptIfNeeded(element);
            }
            finally {
                doc.setCurrentScript(null);
            }

            if (element.hasFeature(EVENT_ONLOAD_INTERNAL_JAVASCRIPT)) {
                executeEvent(element, Event.TYPE_LOAD);
            }
        }
    }

    /**
     * Indicates if script execution is necessary and/or possible.
     *
     * @param element the element
     * @return {@code true} if the script should be executed
     */
    private static boolean isExecutionNeeded(final DomElement element) {
        if (((ScriptElement) element).isExecuted()) {
            return false;
        }

        if (!element.isAttachedToPage()) {
            return false;
        }

        // If JavaScript is disabled, we don't need to execute.
        final SgmlPage page = element.getPage();
        if (!page.getWebClient().isJavaScriptEnabled()) {
            return false;
        }

        // If innerHTML or outerHTML is being parsed
        final HtmlPage htmlPage = element.getHtmlPageOrNull();
        if (htmlPage != null && htmlPage.isParsingHtmlSnippet()) {
            return false;
        }

        // If the script node is nested in an iframe, a noframes, or a noscript node, we don't need to execute.
        for (DomNode o = element; o != null; o = o.getParentNode()) {
            if (o instanceof HtmlInlineFrame || o instanceof HtmlNoFrames) {
                return false;
            }
        }

        // If the underlying page no longer owns its window, the client has moved on (possibly
        // because another script set window.location.href), and we don't need to execute.
        if (page.getEnclosingWindow() != null && page.getEnclosingWindow().getEnclosedPage() != page) {
            return false;
        }

        // If the script language is not JavaScript, we can't execute.
        final String t = element.getAttributeDirect("type");
        final String l = element.getAttributeDirect("language");
        if (!isJavaScript(element, t, l)) {
            // Was at warn level before 2.46 but other types or tricky implementations with unsupported types
            // are common out there and too many peoples out there thinking the is the root of problems.
            // Browsers are also not warning about this.
            if (LOG.isDebugEnabled()) {
                LOG.debug("Script is not JavaScript (type: '" + t + "', language: '" + l + "'). Skipping execution.");
            }
            return false;
        }

        // If the script's root ancestor node is not the page, then the script is not a part of the page.
        // If it isn't yet part of the page, don't execute the script; it's probably just being cloned.
        return element.getPage().isAncestorOf(element);
    }

    /**
     * Returns true if a script with the specified type and language attributes is actually JavaScript.
     * According to <a href="http://www.w3.org/TR/REC-html40/types.html#h-6.7">W3C recommendation</a>
     * are content types case insensitive.<br>
     * IE supports only a limited number of values for the type attribute. For testing you can
     * use http://www.robinlionheart.com/stds/html4/scripts.
     * @param element the element
     * @param typeAttribute the type attribute specified in the script tag
     * @param languageAttribute the language attribute specified in the script tag
     * @return true if the script is JavaScript
     */
    public static boolean isJavaScript(final DomElement element, String typeAttribute, final String languageAttribute) {
        final BrowserVersion browserVersion = element.getPage().getWebClient().getBrowserVersion();

        if (browserVersion.hasFeature(HTMLSCRIPT_TRIM_TYPE)) {
            typeAttribute = typeAttribute.trim();
        }

        if (StringUtils.isNotEmpty(typeAttribute)) {
            return MimeType.isJavascriptMimeType(typeAttribute);
        }

        if (StringUtils.isNotEmpty(languageAttribute)) {
            return StringUtils.startsWithIgnoreCase(languageAttribute, "javascript");
        }
        return true;
    }

    private static void executeEvent(final DomElement element, final String type) {
        final EventTarget eventTarget = element.getScriptableObject();
        final Event event = new Event(element, type);
        eventTarget.executeEventLocally(event);
    }

    /**
     * Executes this script node as inline script if necessary and/or possible.
     */
    private static void executeInlineScriptIfNeeded(final DomElement element) {
        if (!isExecutionNeeded(element)) {
            return;
        }

        final ScriptElement scriptElement = (ScriptElement) element;
        final String src = scriptElement.getSrcAttribute();
        if (src != ATTRIBUTE_NOT_DEFINED) {
            return;
        }

        final String forr = element.getAttributeDirect("for");
        String event = element.getAttributeDirect("event");
        // The event name can be like "onload" or "onload()".
        if (event.endsWith("()")) {
            event = event.substring(0, event.length() - 2);
        }

        final String scriptCode = getScriptCode(element);
        if (event != ATTRIBUTE_NOT_DEFINED
                && forr != ATTRIBUTE_NOT_DEFINED
                && element.hasFeature(JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_WINDOW)
                && "window".equals(forr)) {
            final Window window = element.getPage().getEnclosingWindow().getScriptableObject();
            final BaseFunction function = new EventHandler(element, event, scriptCode);
            window.getEventListenersContainer().addEventListener(StringUtils.substring(event, 2), function, false);
            return;
        }
        if (forr == ATTRIBUTE_NOT_DEFINED || "onload".equals(event)) {
            final String url = element.getPage().getUrl().toExternalForm();
            final int line1 = element.getStartLineNumber();
            final int line2 = element.getEndLineNumber();
            final int col1 = element.getStartColumnNumber();
            final int col2 = element.getEndColumnNumber();
            final String desc = "script in " + url + " from (" + line1 + ", " + col1
                + ") to (" + line2 + ", " + col2 + ")";

            scriptElement.setExecuted(true);
            ((HtmlPage) element.getPage()).executeJavaScript(scriptCode, desc, line1);
        }
    }

    /**
     * Gets the script held within the script tag.
     */
    private static String getScriptCode(final DomElement element) {
        final Iterable<DomNode> textNodes = element.getChildren();
        final StringBuilder scriptCode = new StringBuilder();
        for (final DomNode node : textNodes) {
            if (node instanceof DomText) {
                final DomText domText = (DomText) node;
                scriptCode.append(domText.getData());
            }
        }
        return scriptCode.toString();
    }

}
