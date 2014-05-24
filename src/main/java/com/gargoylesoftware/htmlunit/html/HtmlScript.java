/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_SCRIPT_DISPLAY_INLINE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONERROR_EXTERNAL_JAVASCRIPT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONLOAD_EXTERNAL_JAVASCRIPT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONLOAD_INTERNAL_JAVASCRIPT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONREADY_STATE_CHANGE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLSCRIPT_APPLICATION_JAVASCRIPT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLSCRIPT_TRIM_TYPE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SCRIPT_ALWAYS_REEXECUTE_ON_SRC_CHANGE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_ELEMENT_BY_ID;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_WINDOW;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SCRIPT_SUPPORTS_ONREADYSTATECHANGE;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPage.JavaScriptLoadResult;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.host.Document;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Wrapper for the HTML element "script".<br>
 * When a script tag references an external script (with attribute src) it gets executed when the node
 * is added to the DOM tree. When the script code is nested, it gets executed when the text node
 * containing the script is added to the HtmlScript.<br>
 * The ScriptFilter feature of NekoHtml can't be used because it doesn't allow immediate access to the DOM
 * (i.e. <code>document.write("&lt;span id='mySpan'/>"); document.getElementById("mySpan").tagName;</code>
 * can't work with a filter).
 *
 * @version $Revision$
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
public class HtmlScript extends HtmlElement {

    private static final Log LOG = LogFactory.getLog(HtmlScript.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "script";

    /** Invalid source attribute which should be ignored (used by JS libraries like jQuery). */
    private static final String SLASH_SLASH_COLON = "//:";

    private boolean executed_;

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
     * Returns the value of the attribute "charset". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "charset"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharsetAttribute() {
        return getAttribute("charset");
    }

    /**
     * Returns the value of the attribute "type". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "type"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttribute("type");
    }

    /**
     * Returns the value of the attribute "language". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "language"
     * or an empty string if that attribute isn't defined.
     */
    public final String getLanguageAttribute() {
        return getAttribute("language");
    }

    /**
     * Returns the value of the attribute "src". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "src"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSrcAttribute() {
        return getAttribute("src");
    }

    /**
     * Returns the value of the attribute "event".
     * @return the value of the attribute "event"
     */
    public final String getEventAttribute() {
        return getAttribute("event");
    }

    /**
     * Returns the value of the attribute "for".
     * @return the value of the attribute "for"
     */
    public final String getHtmlForAttribute() {
        return getAttribute("for");
    }

    /**
     * Returns the value of the attribute "defer". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "defer"
     * or an empty string if that attribute isn't defined.
     */
    public final String getDeferAttribute() {
        return getAttribute("defer");
    }

    /**
     * Returns <tt>true</tt> if this script is deferred.
     * @return <tt>true</tt> if this script is deferred
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
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue) {
        final String oldValue = getAttributeNS(namespaceURI, qualifiedName);
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue);

        // special additional processing for the 'src'
        if (namespaceURI == null && "src".equals(qualifiedName)) {
            if (isDirectlyAttachedToPage()) {
                final boolean alwaysReexecute = hasFeature(JS_SCRIPT_ALWAYS_REEXECUTE_ON_SRC_CHANGE);
                // always execute if IE;
                if (alwaysReexecute) {
                    resetExecuted();
                    final PostponedAction action = new PostponedAction(getPage()) {
                        @Override
                        public void execute() {
                            executeScriptIfNeeded();
                        }
                    };
                    final JavaScriptEngine engine = getPage().getWebClient().getJavaScriptEngine();
                    engine.addPostponedAction(action);
                }
                // if FF, only execute if the "src" attribute
                // was undefined and there was no inline code.
                else if (oldValue.isEmpty() && getFirstChild() == null) {
                    final PostponedAction action = new PostponedAction(getPage()) {
                        @Override
                        public void execute() {
                            executeScriptIfNeeded();
                        }
                    };
                    final JavaScriptEngine engine = getPage().getWebClient().getJavaScriptEngine();
                    engine.addPostponedAction(action);
                }
            }
        }
    }

    /**
     * Executes the <tt>onreadystatechange</tt> handler when simulating IE, as well as executing
     * the script itself, if necessary. {@inheritDoc}
     */
    @Override
    protected void onAllChildrenAddedToPage(final boolean postponed) {
        if (getOwnerDocument() instanceof XmlPage) {
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Script node added: " + asXml());
        }
        final PostponedAction action = new PostponedAction(getPage()) {
            @Override
            public void execute() {
                final HTMLDocument jsDoc = (HTMLDocument) ((Window) getPage().getEnclosingWindow().getScriptObject())
                        .getDocument();
                jsDoc.setExecutingDynamicExternalPosponed(getStartLineNumber() == -1
                        && getSrcAttribute() != ATTRIBUTE_NOT_DEFINED);

                try {
                    final boolean onReady = hasFeature(JS_SCRIPT_SUPPORTS_ONREADYSTATECHANGE);
                    if (onReady) {
                        if (!isDeferred()) {
                            if (SLASH_SLASH_COLON.equals(getSrcAttribute())) {
                                setAndExecuteReadyState(READY_STATE_COMPLETE);
                                executeScriptIfNeeded();
                            }
                            else {
                                setAndExecuteReadyState(READY_STATE_LOADING);
                                executeScriptIfNeeded();
                                setAndExecuteReadyState(READY_STATE_LOADED);
                            }
                        }
                    }
                    else {
                        executeScriptIfNeeded();
                    }
                }
                finally {
                    jsDoc.setExecutingDynamicExternalPosponed(false);
                }
            }
        };
        if (postponed && StringUtils.isBlank(getTextContent())) {
            final JavaScriptEngine engine = getPage().getWebClient().getJavaScriptEngine();
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
        if (event != ATTRIBUTE_NOT_DEFINED && forr != ATTRIBUTE_NOT_DEFINED) {
            if (hasFeature(JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_WINDOW) && "window".equals(forr)) {
                final Window window = (Window) getPage().getEnclosingWindow().getScriptObject();
                final BaseFunction function = new EventHandler(this, event, scriptCode);
                window.attachEvent(event, function);
                return;
            }
            if (hasFeature(JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_ELEMENT_BY_ID)) {
                try {
                    final HtmlElement elt = ((HtmlPage) getPage()).getHtmlElementById(forr);
                    elt.setEventHandler(event, scriptCode);
                }
                catch (final ElementNotFoundException e) {
                    LOG.warn("<script for='" + forr + "' ...>: no element found with id \""
                        + forr + "\". Ignoring.");
                }
                return;
            }
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
            ((HtmlPage) getPage()).executeJavaScriptIfPossible(scriptCode, desc, line1);
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
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Executes this script node if necessary and/or possible.
     */
    public void executeScriptIfNeeded() {
        if (!isExecutionNeeded()) {
            return;
        }

        final HtmlPage page = (HtmlPage) getPage();

        final String src = getSrcAttribute();
        if (src.equals(SLASH_SLASH_COLON)) {
            return;
        }

        if (src != ATTRIBUTE_NOT_DEFINED) {
            if (!src.startsWith(JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
                // <script src="[url]"></script>
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loading external JavaScript: " + src);
                }
                try {
                    executed_ = true;
                    final JavaScriptLoadResult result = page.loadExternalJavaScriptFile(src, getCharsetAttribute());
                    if (result == JavaScriptLoadResult.SUCCESS) {
                        executeEventIfBrowserHasFeature(Event.TYPE_LOAD, EVENT_ONLOAD_EXTERNAL_JAVASCRIPT);
                    }
                    else if (result == JavaScriptLoadResult.DOWNLOAD_ERROR) {
                        executeEventIfBrowserHasFeature(Event.TYPE_ERROR, EVENT_ONERROR_EXTERNAL_JAVASCRIPT);
                    }
                }
                catch (final FailingHttpStatusCodeException e) {
                    executeEventIfBrowserHasFeature(Event.TYPE_ERROR, EVENT_ONERROR_EXTERNAL_JAVASCRIPT);
                    throw e;
                }
            }
        }
        else if (getFirstChild() != null) {
            // <script>[code]</script>
            executeInlineScriptIfNeeded();

            executeEventIfBrowserHasFeature(Event.TYPE_LOAD, EVENT_ONLOAD_INTERNAL_JAVASCRIPT);
        }
    }

    private void executeEventIfBrowserHasFeature(final String type, final BrowserVersionFeatures feature) {
        if (hasFeature(feature)) {
            final HTMLScriptElement script = (HTMLScriptElement) getScriptObject();
            final Event event = new Event(HtmlScript.this, type);
            script.executeEvent(event);
        }
    }

    /**
     * Indicates if script execution is necessary and/or possible.
     *
     * @return <code>true</code> if the script should be executed
     */
    private boolean isExecutionNeeded() {
        if (executed_) {
            return false;
        }

        if (!isDirectlyAttachedToPage()) {
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
        if (!isJavaScript(getTypeAttribute(), getLanguageAttribute())) {
            final String t = getTypeAttribute();
            final String l = getLanguageAttribute();
            LOG.warn("Script is not JavaScript (type: " + t + ", language: " + l + "). Skipping execution.");
            return false;
        }

        // If the script's root ancestor node is not the page, then the script is not a part of the page.
        // If it isn't yet part of the page, don't execute the script; it's probably just being cloned.
        DomNode root = this;
        while (root.getParentNode() != null) {
            root = root.getParentNode();
        }
        if (root != getPage()) {
            return false;
        }

        return true;
    }

    /**
     * Returns true if a script with the specified type and language attributes is actually JavaScript.
     * According to <a href="http://www.w3.org/TR/REC-html40/types.html#h-6.7">W3C recommendation</a>
     * are content types case insensitive.<b>
     * IE supports only a limited number of values for the type attribute. For testing you can
     * use http://www.robinlionheart.com/stds/html4/scripts.
     * @param typeAttribute the type attribute specified in the script tag
     * @param languageAttribute the language attribute specified in the script tag
     * @return true if the script is JavaScript
     */
    boolean isJavaScript(String typeAttribute, final String languageAttribute) {
        final BrowserVersion browserVersion = getPage().getWebClient().getBrowserVersion();

        if (browserVersion.hasFeature(HTMLSCRIPT_TRIM_TYPE)) {
            typeAttribute = typeAttribute.trim();
        }

        if (StringUtils.isNotEmpty(typeAttribute)) {
            if ("text/javascript".equalsIgnoreCase(typeAttribute)
                    || "text/ecmascript".equalsIgnoreCase(typeAttribute)) {
                return true;
            }

            final boolean appJavascriptSupported = browserVersion.hasFeature(HTMLSCRIPT_APPLICATION_JAVASCRIPT);
            if (appJavascriptSupported
                    && ("application/javascript".equalsIgnoreCase(typeAttribute)
                            || "application/ecmascript".equalsIgnoreCase(typeAttribute)
                            || "application/x-javascript".equalsIgnoreCase(typeAttribute))) {
                return true;
            }
            return false;
        }

        if (StringUtils.isNotEmpty(languageAttribute)) {
            return StringUtils.startsWithIgnoreCase(languageAttribute, "javascript");
        }
        return true;
    }

    /**
     * Sets the <tt>readyState</tt> to the specified state and executes the
     * <tt>onreadystatechange</tt> handler when simulating IE.
     * @param state this script ready state
     */
    protected void setAndExecuteReadyState(final String state) {
        if (hasFeature(EVENT_ONREADY_STATE_CHANGE)) {
            setReadyState(state);
            final HTMLScriptElement script = (HTMLScriptElement) getScriptObject();
            final Event event = new Event(this, Event.TYPE_READY_STATE_CHANGE);
            script.executeEvent(event);
        }
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
     * @return <code>true</code> to make generated XML readable as HTML
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
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
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

        printWriter.print(ClassUtils.getShortClassName(getClass()));
        printWriter.print("[<");
        printOpeningTagContentAsXml(printWriter);
        printWriter.print(">");
        printWriter.print(getScriptCode());
        printWriter.print("]");
        printWriter.flush();
        return writer.toString();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the default display style.
     *
     * @return the default display style.
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        if (hasFeature(CSS_SCRIPT_DISPLAY_INLINE)) {
            return DisplayStyle.INLINE;
        }
        return DisplayStyle.NONE;
    }
}
