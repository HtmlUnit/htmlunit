/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebClient;
import org.htmlunit.WebWindow;
import org.htmlunit.html.HtmlPage.JavaScriptLoadResult;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.dom.Document;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.javascript.host.html.HTMLDocument;
import org.htmlunit.protocol.javascript.JavaScriptURLConnection;
import org.htmlunit.util.EncodingSniffer;
import org.htmlunit.util.MimeType;
import org.htmlunit.xml.XmlPage;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * A helper class to be used by elements which support {@link ScriptElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Ronny Shapiro
 * @author Sven Strickroth
 */
public final class ScriptElementSupport {

    private static final Log LOG = LogFactory.getLog(ScriptElementSupport.class);

    /** Invalid source attribute which should be ignored (used by JS libraries like jQuery). */
    private static final String SLASH_SLASH_COLON = "//:";

    private ScriptElementSupport() {
        // util class
    }

    /**
     * Support method that is called from the (html or svg) script and the link tag.
     *
     * @param script the ScriptElement to work for
     * @param postponed whether to use {@link org.htmlunit.javascript.PostponedAction} or not
     */
    public static void onAllChildrenAddedToPage(final ScriptElement script, final boolean postponed) {
        final DomElement element = (DomElement) script;
        if (element.getOwnerDocument() instanceof XmlPage) {
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Script node added: " + element.asXml());
        }

        final WebClient webClient = element.getPage().getWebClient();
        if (!webClient.isJavaScriptEngineEnabled()) {
            LOG.debug("Script found but not executed because javascript engine is disabled");
            return;
        }

        final String srcAttrib = script.getScriptSource();
        final boolean hasNoSrcAttrib = ATTRIBUTE_NOT_DEFINED == srcAttrib;
        if (!hasNoSrcAttrib && script.isDeferred()) {
            return;
        }

        final WebWindow webWindow = element.getPage().getEnclosingWindow();
        if (webWindow != null) {
            final StringBuilder description = new StringBuilder()
                    .append("Execution of ")
                    .append(hasNoSrcAttrib ? "inline " : "external ")
                    .append(element.getClass().getSimpleName());
            if (!hasNoSrcAttrib) {
                description.append(" (").append(srcAttrib).append(')');
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
                                && ATTRIBUTE_NOT_DEFINED != srcAttrib);
                    }
                    try {
                        executeScriptIfNeeded(script, false, false);
                    }
                    finally {
                        if (jsDoc != null) {
                            jsDoc.setExecutingDynamicExternalPosponed(false);
                        }
                    }
                }
            };

            final AbstractJavaScriptEngine<?> engine = webClient.getJavaScriptEngine();
            if (element.hasAttribute("async") && !engine.isScriptRunning()) {
                final HtmlPage owningPage = element.getHtmlPageOrNull();
                owningPage.addAfterLoadAction(action);
            }
            else if (element.hasAttribute("async")
                            || postponed && StringUtils.isBlank(element.getTextContent())) {
                engine.addPostponedAction(action);
            }
            else {
                try {
                    action.execute();
                    engine.processPostponedActions();
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
     *
     * @param script the ScriptElement to work for
     * @param ignoreAttachedToPage don't do the isAttachedToPage check
     * @param ignorePageIsAncestor don't do the element.getPage().isAncestorOf(element) check
     */
    public static void executeScriptIfNeeded(final ScriptElement script, final boolean ignoreAttachedToPage,
            final boolean ignorePageIsAncestor) {
        if (!isExecutionNeeded(script, ignoreAttachedToPage, ignorePageIsAncestor)) {
            return;
        }

        final String src = script.getScriptSource();
        final DomElement element = (DomElement) script;
        if (SLASH_SLASH_COLON.equals(src)) {
            executeEvent(element, Event.TYPE_ERROR);
            return;
        }

        final HtmlPage page = (HtmlPage) element.getPage();
        if (src != ATTRIBUTE_NOT_DEFINED) {
            if (!src.startsWith(JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
                // <script src="[url]"></script>
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loading external JavaScript: " + src);
                }
                try {
                    script.setExecuted(true);
                    Charset charset = EncodingSniffer.toCharset(script.getScriptCharset());
                    if (charset == null) {
                        charset = page.getCharset();
                    }

                    final JavaScriptLoadResult result;
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
                        executeEvent(element, Event.TYPE_LOAD);
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
                executeInlineScriptIfNeeded(script);
            }
            finally {
                doc.setCurrentScript(null);
            }
        }
    }

    /**
     * Indicates if script execution is necessary and/or possible.
     *
     * @param script the ScriptElement to work for
     * @param ignoreAttachedToPage don't do the isAttachedToPage check
     * @param ignorePageIsAncestor don't do the element.getPage().isAncestorOf(element) check
     * @return {@code true} if the script should be executed
     */
    private static boolean isExecutionNeeded(final ScriptElement script, final boolean ignoreAttachedToPage,
            final boolean ignorePageIsAncestor) {
        if (script.isExecuted() || script.wasCreatedByDomParser()) {
            return false;
        }

        final DomElement element = (DomElement) script;
        if (!ignoreAttachedToPage && !element.isAttachedToPage()) {
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
        if (!isJavaScript(t, l)) {
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
        return ignorePageIsAncestor || element.getPage().isAncestorOf(element);
    }

    /**
     * Returns true if a script with the specified type and language attributes is actually JavaScript.
     * According to <a href="http://www.w3.org/TR/REC-html40/types.html#h-6.7">W3C recommendation</a>
     * are content types case insensitive.<br>
     *
     * @param typeAttribute the type attribute specified in the script tag
     * @param languageAttribute the language attribute specified in the script tag
     * @return true if the script is JavaScript
     */
    public static boolean isJavaScript(String typeAttribute, final String languageAttribute) {
        typeAttribute = typeAttribute.trim();

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

        event.setParentScope(eventTarget);
        event.setPrototype(eventTarget.getPrototype(event.getClass()));

        eventTarget.executeEventLocally(event);
    }

    /**
     * Executes this script node as inline script if necessary and/or possible.
     */
    private static void executeInlineScriptIfNeeded(final ScriptElement script) {
        if (!isExecutionNeeded(script, false, false)) {
            return;
        }

        final String src = script.getScriptSource();
        if (src != ATTRIBUTE_NOT_DEFINED) {
            return;
        }

        final DomElement element = (DomElement) script;
        final String forr = element.getAttributeDirect("for");
        String event = element.getAttributeDirect("event");
        // The event name can be like "onload" or "onload()".
        if (event.endsWith("()")) {
            event = event.substring(0, event.length() - 2);
        }

        final String scriptCode = getScriptCode(element);
        if (forr == ATTRIBUTE_NOT_DEFINED || "onload".equals(event)) {
            final String url = element.getPage().getUrl().toExternalForm();
            final int line1 = element.getStartLineNumber();
            final int line2 = element.getEndLineNumber();
            final int col1 = element.getStartColumnNumber();
            final int col2 = element.getEndColumnNumber();
            final String desc = "script in " + url + " from (" + line1 + ", " + col1
                + ") to (" + line2 + ", " + col2 + ")";

            script.setExecuted(true);
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
