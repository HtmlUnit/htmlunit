/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPage.JavaScriptLoadResult;
import com.gargoylesoftware.htmlunit.javascript.AbstractJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;

/**
 * Wrapper for the HTML element "script".<br>
 * When a script tag references an external script (with attribute src) it gets executed when the node
 * is added to the DOM tree. When the script code is nested, it gets executed when the text node
 * containing the script is added to the HtmlScript.<br>
 * The ScriptFilter feature of NekoHtml can't be used because it doesn't allow immediate access to the DOM
 * (i.e. <code>document.write("&lt;span id='mySpan'/&gt;"); document.getElementById("mySpan").tagName;</code>
 * can't work with a filter).
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Dmitri Zoubkov
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Daniel Wagner-Hall
 * @author Frank Danek
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-81598695">DOM Level 1</a>
 * @see <a href="http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109/html.html#ID-81598695">DOM Level 2</a>
 */
public class HtmlScript extends HtmlElement implements ScriptElement {

    private static final Log LOG = LogFactory.getLog(HtmlScript.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "script";

    /** Invalid source attribute which should be ignored (used by JS libraries like jQuery). */
    private static final String SLASH_SLASH_COLON = "//:";

    private boolean executed_;
    private boolean createdByJavascript_;

    /**
     * Creates an instance of HtmlScript
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlScript(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getCharsetAttribute() {
        return getAttributeDirect("charset");
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type}
     * or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttributeDirect("type");
    }

    /**
     * Returns the value of the attribute {@code language}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code language}
     * or an empty string if that attribute isn't defined.
     */
    public final String getLanguageAttribute() {
        return getAttributeDirect("language");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSrcAttribute() {
        return getSrcAttributeNormalized();
    }

    /**
     * Returns the value of the attribute {@code event}.
     * @return the value of the attribute {@code event}
     */
    public final String getEventAttribute() {
        return getAttributeDirect("event");
    }

    /**
     * Returns the value of the attribute {@code for}.
     * @return the value of the attribute {@code for}
     */
    public final String getHtmlForAttribute() {
        return getAttributeDirect("for");
    }

    /**
     * Returns the value of the attribute {@code defer}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code defer}
     * or an empty string if that attribute isn't defined.
     */
    public final String getDeferAttribute() {
        return getAttributeDirect("defer");
    }

    /**
     * Returns {@code true} if this script is deferred.
     * @return {@code true} if this script is deferred
     */
    protected boolean isDeferred() {
        return getDeferAttribute() != ATTRIBUTE_NOT_DEFINED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayBeDisplayed() {
        return false;
    }

    /**
     * If setting the <tt>src</tt> attribute, this method executes the new JavaScript if necessary
     * (behavior varies by browser version). {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        // special additional processing for the 'src'
        if (namespaceURI != null || !SRC_ATTRIBUTE.equals(qualifiedName)) {
            super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                    notifyMutationObservers);
            return;
        }

        final String oldValue = getAttributeNS(namespaceURI, qualifiedName);
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);

        if (isAttachedToPage() && oldValue.isEmpty() && getFirstChild() == null) {
            final PostponedAction action = new PostponedAction(getPage()) {
                @Override
                public void execute() {
                    executeScriptIfNeeded();
                }
            };
            final AbstractJavaScriptEngine<?> engine = getPage().getWebClient().getJavaScriptEngine();
            engine.addPostponedAction(action);
        }
    }

    /**
     * Executes the <tt>onreadystatechange</tt> handler when simulating IE, as well as executing
     * the script itself, if necessary. {@inheritDoc}
     */
    @Override
    public void onAllChildrenAddedToPage(final boolean postponed) {
        if (getOwnerDocument() instanceof XmlPage) {
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Script node added: " + asXml());
        }

        final PostponedAction action = new PostponedAction(getPage(), "Execution of script " + this) {
            @Override
            public void execute() {
                Object jsDoc = null;
                final Object window = getPage().getEnclosingWindow().getScriptableObject();
                if (window instanceof Window) {
                    jsDoc = ((Window) window).getDocument();
                    ((HTMLDocument) jsDoc).setExecutingDynamicExternalPosponed(getStartLineNumber() == -1
                            && getSrcAttribute() != ATTRIBUTE_NOT_DEFINED);
                }
                try {
                    executeScriptIfNeeded();
                }
                finally {
                    if (jsDoc instanceof HTMLDocument) {
                        ((HTMLDocument) jsDoc).setExecutingDynamicExternalPosponed(false);
                    }
                }
            }
        };

        final AbstractJavaScriptEngine<?> engine = getPage().getWebClient().getJavaScriptEngine();
        if (hasAttribute("async") && !engine.isScriptRunning()) {
            final HtmlPage owningPage = getHtmlPageOrNull();
            owningPage.addAfterLoadAction(action);
        }
        else if (hasAttribute("async")
                || postponed && StringUtils.isBlank(getTextContent())) {
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

    /**
     * Executes this script node as inline script if necessary and/or possible.
     */
    private void executeInlineScriptIfNeeded() {
        if (!isExecutionNeeded()) {
            return;
        }

        final String src = getSrcAttribute();
        if (src != ATTRIBUTE_NOT_DEFINED) {
            return;
        }

        final String forr = getHtmlForAttribute();
        String event = getEventAttribute();
        // The event name can be like "onload" or "onload()".
        if (event.endsWith("()")) {
            event = event.substring(0, event.length() - 2);
        }

        final String scriptCode = getScriptCode();
        if (event != ATTRIBUTE_NOT_DEFINED && forr != ATTRIBUTE_NOT_DEFINED
                && hasFeature(JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_WINDOW) && "window".equals(forr)) {
            final Window window = getPage().getEnclosingWindow().getScriptableObject();
            final BaseFunction function = new EventHandler(this, event, scriptCode);
            window.getEventListenersContainer().addEventListener(StringUtils.substring(event, 2), function, false);
            return;
        }
        if (forr == ATTRIBUTE_NOT_DEFINED || "onload".equals(event)) {
            final String url = getPage().getUrl().toExternalForm();
            final int line1 = getStartLineNumber();
            final int line2 = getEndLineNumber();
            final int col1 = getStartColumnNumber();
            final int col2 = getEndColumnNumber();
            final String desc = "script in " + url + " from (" + line1 + ", " + col1
                + ") to (" + line2 + ", " + col2 + ")";

            executed_ = true;
            ((HtmlPage) getPage()).executeJavaScript(scriptCode, desc, line1);
        }
    }

    /**
     * Gets the script held within the script tag.
     */
    private String getScriptCode() {
        final Iterable<DomNode> textNodes = getChildren();
        final StringBuilder scriptCode = new StringBuilder();
        for (final DomNode node : textNodes) {
            if (node instanceof DomText) {
                final DomText domText = (DomText) node;
                scriptCode.append(domText.getData());
            }
        }
        return scriptCode.toString();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Executes this script node if necessary and/or possible.
     */
    public void executeScriptIfNeeded() {
        if (!isExecutionNeeded()) {
            return;
        }

        final HtmlPage page = (HtmlPage) getPage();

        final String src = getSrcAttribute();

        if (src != ATTRIBUTE_NOT_DEFINED) {
            if (src.equals(SLASH_SLASH_COLON)) {
                executeEvent(Event.TYPE_ERROR);
                return;
            }

            if (!src.startsWith(JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
                // <script src="[url]"></script>
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loading external JavaScript: " + src);
                }
                try {
                    executed_ = true;
                    Charset charset = EncodingSniffer.toCharset(getCharsetAttribute());
                    if (charset == null) {
                        charset = page.getCharset();
                    }

                    JavaScriptLoadResult result = null;
                    final Window win = page.getEnclosingWindow().getScriptableObject();
                    final Document doc = win.getDocument();
                    try {
                        doc.setCurrentScript(getScriptableObject());
                        result = page.loadExternalJavaScriptFile(src, charset);
                    }
                    finally {
                        doc.setCurrentScript(null);
                    }

                    if (result == JavaScriptLoadResult.SUCCESS) {
                        executeEvent(Event.TYPE_LOAD);
                    }
                    else if (result == JavaScriptLoadResult.DOWNLOAD_ERROR) {
                        executeEvent(Event.TYPE_ERROR);
                    }
                    else if (result == JavaScriptLoadResult.NO_CONTENT) {
                        final BrowserVersion browserVersion = getPage().getWebClient().getBrowserVersion();
                        if (browserVersion.hasFeature(JS_SCRIPT_HANDLE_204_AS_ERROR)) {
                            executeEvent(Event.TYPE_ERROR);
                        }
                        else {
                            executeEvent(Event.TYPE_LOAD);
                        }
                    }
                }
                catch (final FailingHttpStatusCodeException e) {
                    executeEvent(Event.TYPE_ERROR);
                    throw e;
                }
            }
        }
        else if (getFirstChild() != null) {
            // <script>[code]</script>
            final Window win = page.getEnclosingWindow().getScriptableObject();
            final Document doc = win.getDocument();
            try {
                doc.setCurrentScript(getScriptableObject());
                executeInlineScriptIfNeeded();
            }
            finally {
                doc.setCurrentScript(null);
            }

            if (hasFeature(EVENT_ONLOAD_INTERNAL_JAVASCRIPT)) {
                executeEvent(Event.TYPE_LOAD);
            }
        }
    }

    private void executeEvent(final String type) {
        final Object scriptable = getScriptableObject();
        final HTMLScriptElement script = (HTMLScriptElement) scriptable;
        final Event event = new Event(this, type);
        script.executeEventLocally(event);
    }

    /**
     * Indicates if script execution is necessary and/or possible.
     *
     * @return {@code true} if the script should be executed
     */
    private boolean isExecutionNeeded() {
        if (executed_) {
            return false;
        }

        if (!isAttachedToPage()) {
            return false;
        }

        // If JavaScript is disabled, we don't need to execute.
        final SgmlPage page = getPage();
        if (!page.getWebClient().getOptions().isJavaScriptEnabled()) {
            return false;
        }

        // If innerHTML or outerHTML is being parsed
        final HtmlPage htmlPage = getHtmlPageOrNull();
        if (htmlPage != null && htmlPage.isParsingHtmlSnippet()) {
            return false;
        }

        // If the script node is nested in an iframe, a noframes, or a noscript node, we don't need to execute.
        for (DomNode o = this; o != null; o = o.getParentNode()) {
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
        final String t = getTypeAttribute();
        final String l = getLanguageAttribute();
        if (!ScriptElementSupport.isJavaScript(this, t, l)) {
            LOG.warn("Script is not JavaScript (type: '" + t + "', language: '" + l + "'). Skipping execution.");
            return false;
        }

        // If the script's root ancestor node is not the page, then the script is not a part of the page.
        // If it isn't yet part of the page, don't execute the script; it's probably just being cloned.

        return getPage().isAncestorOf(this);
    }

    /**
     * Sets the <tt>readyState</tt> to the specified state and executes the
     * <tt>onreadystatechange</tt> handler when simulating IE.
     * @param state this script ready state
     */
    protected void setAndExecuteReadyState(final String state) {
    }

    /**
     * @see com.gargoylesoftware.htmlunit.html.HtmlInput#asText()
     * @return an empty string as the content of script is not visible by itself
     */
    // we need to preserve this method as it is there since many versions with the above documentation.
    @Override
    public String asText() {
        return "";
    }

    /**
     * Indicates if a node without children should be written in expanded form as XML
     * (i.e. with closing tag rather than with "/&gt;")
     * @return {@code true} to make generated XML readable as HTML
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void printChildrenAsXml(final String indent, final PrintWriter printWriter) {
        final DomCharacterData textNode = (DomCharacterData) getFirstChild();
        if (textNode == null) {
            return;
        }

        final String data = textNode.getData();
        if (data.contains("//<![CDATA[")) {
            printWriter.print(data);
            printWriter.print("\r\n");
        }
        else {
            printWriter.print("//<![CDATA[");
            printWriter.print("\r\n");
            printWriter.print(data);
            printWriter.print("\r\n");
            printWriter.print("//]]>");
            printWriter.print("\r\n");
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Resets the executed flag.
     * @see HtmlScript#processImportNode(Document)
     */
    public void resetExecuted() {
        executed_ = false;
    }

    @Override
    public void processImportNode(final Document doc) {
        super.processImportNode(doc);

        executed_ = true;
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);

        printWriter.print(getClass().getSimpleName());
        printWriter.print("[<");
        printOpeningTagContentAsXml(printWriter);
        printWriter.print(">");
        printWriter.print(getScriptCode());
        printWriter.print("]");
        printWriter.flush();
        return writer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.NONE;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Marks this frame as created by javascript. This is needed to handle
     * some special IE behavior.
     */
    public void markAsCreatedByJavascript() {
        createdByJavascript_ = true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns true if this frame was created by javascript. This is needed to handle
     * some special IE behavior.
     * @return true or false
     */
    public boolean wasCreatedByJavascript() {
        return createdByJavascript_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecuted() {
        return executed_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExecuted(final boolean executed) {
        executed_ = executed;
    }
}
