/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.Map;

import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.javascript.host.HTMLScriptElement;

/**
 * Wrapper for the html element "script".<br>
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
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-81598695">DOM Level 1</a>
 * @see <a href="http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109/html.html#ID-81598695">DOM Level 2</a>
 */
public class HtmlScript extends HtmlElement {

    private static final long serialVersionUID = 5736570536821513938L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "script";

    /** Invalid source attribute which should be ignored (used by JS libraries like jQuery). */
    private static final String SLASH_SLASH_COLON = "//:";

    /** Not really used? */
    private static int EventHandlerId_;

    /**
     * Create an instance of HtmlScript
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param page The HtmlPage that contains this element.
     * @param attributes the initial attributes
     */
    HtmlScript(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Return the value of the attribute "charset".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "charset"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharsetAttribute() {
        return getAttributeValue("charset");
    }

    /**
     * Return the value of the attribute "type".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "type"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttributeValue("type");
    }

    /**
     * Return the value of the attribute "language".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "language"
     * or an empty string if that attribute isn't defined.
     */
    public final String getLanguageAttribute() {
        return getAttributeValue("language");
    }

    /**
     * Return the value of the attribute "src".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "src"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSrcAttribute() {
        return getAttributeValue("src");
    }

    /**
     * Return the value of the attribute "event".
     * @return The value of the attribute "event"
     */
    public final String getEventAttribute() {
        return getAttributeValue("event");
    }

    /**
     * Return the value of the attribute "for".
     * @return The value of the attribute "for"
     */
    public final String getHtmlForAttribute() {
        return getAttributeValue("for");
    }

    /**
     * Return the value of the attribute "defer".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "defer"
     * or an empty string if that attribute isn't defined.
     */
    public final String getDeferAttribute() {
        return getAttributeValue("defer");
    }

    /**
     * Executes the content as a script if said content is a text node.
     * {@inheritDoc}
     */
    @Override
    public DomNode appendDomChild(final DomNode node) {
        final DomNode response = super.appendDomChild(node);
        executeInlineScriptIfNeeded(false);
        return response;
    }

    /**
     * If setting the <tt>src</tt> attribute, this method executes the new JavaScript if necessary
     * (behavior varies by browser version). {@inheritDoc}
     */
    @Override
    public void setAttributeValue(final String namespaceURI, final String qualifiedName, final String attributeValue) {

        boolean execute = false;
        if (namespaceURI == null && "src".equals(qualifiedName)) {
            final boolean ie = getPage().getWebClient().getBrowserVersion().isIE();
            if (ie || (getAttribute("src").length() == 0 && getFirstDomChild() == null)) {
                // Always execute if IE; if FF, only execute if the "src" attribute
                // was undefined and there was no inline code.
                execute = true;
            }
        }

        super.setAttributeValue(namespaceURI, qualifiedName, attributeValue);

        if (execute) {
            executeScriptIfNeeded(true);
        }
    }

    /**
     * Executes the <tt>onreadystatechange</tt> handler when simulating IE, as well as executing
     * the script itself, if necessary. {@inheritDoc}
     */
    @Override
    protected void onAddedToPage() {
        getLog().debug("Script node added: " + asXml());
        executeOnReadyStateChangeHandlerIfNecessary();
        executeScriptIfNeeded(true);
        super.onAddedToPage();
    }

    /**
     * Executes this script node as inline script if necessary and/or possible.
     *
     * @param executeIfDeferred if <tt>false</tt>, and we are emulating IE, and the <tt>defer</tt>
     * attribute is defined, the script is not executed
     */
    private void executeInlineScriptIfNeeded(final boolean executeIfDeferred) {
        if (!isExecutionNeeded()) {
            return;
        }

        final String defer = getDeferAttribute();
        final boolean ie = getPage().getWebClient().getBrowserVersion().isIE();
        if (!executeIfDeferred && defer != ATTRIBUTE_NOT_DEFINED && ie) {
            return;
        }

        final String src = getSrcAttribute();
        if (src != HtmlElement.ATTRIBUTE_NOT_DEFINED) {
            return;
        }

        final DomCharacterData textNode = (DomCharacterData) getFirstDomChild();
        final String forr = getHtmlForAttribute();
        String event = getEventAttribute();

        final String scriptCode;
        if (event != ATTRIBUTE_NOT_DEFINED && forr != ATTRIBUTE_NOT_DEFINED) {
            // The event name can be like "onload" or "onload()".
            if (event.endsWith("()")) {
                event = event.substring(0, event.length() - 2);
            }
            final String handler = forr + "." + event;
            final String functionName = "htmlunit_event_handler_JJLL" + EventHandlerId_;
            scriptCode = "function " + functionName + "()\n"
                + "{" + textNode.getData() + "}\n"
                + handler + "=" + functionName + ";";
        }
        else {
            scriptCode = textNode.getData();
        }

        final String url = getPage().getWebResponse().getUrl().toExternalForm();
        final int line1 = getStartLineNumber();
        final int line2 = getEndLineNumber();
        final int col1 = getStartColumnNumber();
        final int col2 = getEndColumnNumber();
        final String desc = "script in " + url + " from (" + line1 + ", " + col1 + ") to (" + line2 + ", " + col2 + ")";
        getPage().executeJavaScriptIfPossible(scriptCode, desc, line1);
    }

    /**
     * Executes this script node if necessary and/or possible.
     *
     * @param executeIfDeferred if <tt>false</tt>, and we are emulating IE, and the <tt>defer</tt>
     * attribute is defined, the script is not executed
     */
    void executeScriptIfNeeded(final boolean executeIfDeferred) {
        if (!isExecutionNeeded()) {
            return;
        }

        final String defer = getDeferAttribute();
        final boolean ie = getPage().getWebClient().getBrowserVersion().isIE();
        if (!executeIfDeferred && defer != ATTRIBUTE_NOT_DEFINED && ie) {
            return;
        }

        final String src = getSrcAttribute();
        if (src.equals(SLASH_SLASH_COLON)) {
            return;
        }

        if (src != ATTRIBUTE_NOT_DEFINED) {
            getLog().debug("Loading external javascript: " + src);
            getPage().loadExternalJavaScriptFile(src, getCharsetAttribute());
        }
        else if (getFirstDomChild() != null) {
            executeInlineScriptIfNeeded(executeIfDeferred);
        }
    }

    /**
     * Indicates if script execution is necessary and/or possible.
     *
     * @return <code>true</code> if the script should be executed.
     */
    private boolean isExecutionNeeded() {
        final HtmlPage page = getPage();

        // If JavaScript is disabled, we don't need to execute.
        if (!page.getWebClient().isJavaScriptEnabled()) {
            return false;
        }

        // If the script node is nested in an iframe, a noframes, or a noscript node, we don't need to execute.
        for (DomNode o = this; o != null; o = o.getParentDomNode()) {
            if (o instanceof HtmlInlineFrame || o instanceof HtmlNoFrames || o instanceof HtmlNoScript) {
                return false;
            }
        }

        // If the underlying page no longer owns its window, the client has moved on (possibly
        // because another script set window.location.href), and we don't need to execute.
        if (page.getEnclosingWindow() != null && page.getEnclosingWindow().getEnclosedPage() != page) {
            return false;
        }

        // If the script language is not JavaScript, we can't execute.
        if (!HtmlPage.isJavaScript(getTypeAttribute(), getLanguageAttribute())) {
            final String t = getTypeAttribute();
            final String l = getLanguageAttribute();
            getLog().warn("Script is not javascript (type: " + t + ", language: " + l + "). Skipping execution.");
            return false;
        }

        // If the script's root ancestor node is not the page, the the script is not a part of the page.
        // If it isn't yet part of the page, don't execute the script; it's probably just being cloned.
        DomNode root = this;
        while (root.getParentDomNode() != null) {
            root = root.getParentDomNode();
        }
        if (root != getPage()) {
            return false;
        }

        return true;
    }

    /**
     * Sets the <tt>readyState</tt> to {@link DomNode#READY_STATE_COMPLETE} and executes the
     * <tt>onreadystatechange</tt> handler when simulating IE. Note that script nodes go
     * straight to the {@link DomNode#READY_STATE_COMPLETE} state, skipping all previous states.
     */
    private void executeOnReadyStateChangeHandlerIfNecessary() {
        if (getPage().getWebClient().getBrowserVersion().isIE()) {
            setReadyState(READY_STATE_COMPLETE);
            final HTMLScriptElement script = (HTMLScriptElement) getScriptObject();
            final Function handler = script.getOnReadyStateChangeHandler();
            if (handler != null) {
                getPage().executeJavaScriptFunctionIfPossible(handler, script, new Object[0], this);
            }
        }
    }

    /**
     * @see com.gargoylesoftware.htmlunit.html.HtmlInput#asText()
     * @return an empty string as the content of script is not visible by itself
     */
    @Override
    public String asText() {
        return "";
    }

    /**
     * Indicates if a node without children should be written in expanded form as xml
     * (i.e. with closing tag rather than with "/&gt;")
     * @return <code>true</code> to make generated xml readable as html
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }
}
