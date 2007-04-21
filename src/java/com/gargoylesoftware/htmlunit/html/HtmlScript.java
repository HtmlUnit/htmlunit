/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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


/**
 * Wrapper for the html element "script".<br>
 * When a script tag references an externat script (with attribute src) it gets executed when the node
 * is added to the DOM tree. When the script code is nested, it gets executed when the text node 
 * containing the script is added to the HtmlScript.<br>
 * The ScriptFilter feature of NekoHtml can't be used because it doesn't allow immediate access to the DOM 
 * (ie <code>document.write("&lt;span id='mySpan'/>"); document.getElementById("mySpan").tagName;</code>
 * can't work with a filter).
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-81598695">DOM Level 1</a>
 * @see <a href="http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109/html.html#ID-81598695">DOM Level 2</a>
 */
public class HtmlScript extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "script";

    /** Invalid source attribute which should be ignored (used by JS libraries like jQuery). */
    private static final String SLASH_SLASH_COLON = "//:";

    /** Not really used? */
    private static int EventHandlerId_;

    /**
     * Create an instance of HtmlScript
     *
     * @param page The HtmlPage that contains this element.
     * @param attributes the initial attributes
     */
    public HtmlScript( final HtmlPage page, final Map attributes ) {
        super(page, attributes);
    }

    /**
     * @return the HTML tag name
     */
    public String getTagName() {
        return TAG_NAME;
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
     * Executes the content as a script if it is a text node.
     * {@inheritDoc}
     */
    public DomNode appendChild(final DomNode node) {
        final DomNode response = super.appendChild(node);
        if (canExecute()) {
            executeInlineScriptIfNeeded();
        }
        return response;
    }

    /**
     * Indicates if the script can be executed.
     * @return <code>false</code> if the script is nested in a node preventing its execution
     */
    boolean canExecute() {
        for (DomNode o = this; o != null; o = o.getParentNode()) {
            if (o instanceof HtmlInlineFrame || o instanceof HtmlNoFrames) {
                return false;
            }
        }
        return true;
    }

    /**
     * For internal use only.
     */
    void executeInlineScriptIfNeeded() {
        if (getSrcAttribute() == HtmlElement.ATTRIBUTE_NOT_DEFINED && isExecutionNeeded()) {
            final DomCharacterData textNode = (DomCharacterData) getFirstChild();
            final String scriptCode;
            if (getEventAttribute() != ATTRIBUTE_NOT_DEFINED
                    && getHtmlForAttribute() != ATTRIBUTE_NOT_DEFINED) {
                // event name can be like "onload" or "onload()"
                String eventName = getEventAttribute();
                if (eventName.endsWith("()")) {
                    eventName = eventName.substring(0, eventName.length()-2);
                }
                final String scriptEventHandler = getHtmlForAttribute() + "." + eventName;
                final String evhName = "htmlunit_evh_JJLL" + EventHandlerId_;
                scriptCode = "function " + evhName + "()\n{"
                    + textNode.getData() + "}\n"
                    + scriptEventHandler + "=" + evhName + ";";
            }
            else {
                scriptCode = textNode.getData();
            }
            getPage().executeJavaScriptIfPossible(scriptCode,
                    "Embedded script in " +
                    getPage().getWebResponse().getUrl().toExternalForm() +
                    " from (" + getStartLineNumber() + ", " +
                    getStartColumnNumber() + ") to (" + getEndLineNumber() +
                    ", " + getEndColumnNumber() + ")", false, null);
        }
    }

    /**
     * Indicates if script execution is needed (possible).
     * @return <code>true</code> if script should be execute
     */
    private boolean isExecutionNeeded() {
        final HtmlPage page = getPage();

        if (!page.getWebClient().isJavaScriptEnabled()) {
            return false;
        }
        if (!HtmlPage.isJavaScript(getTypeAttribute(), getLanguageAttribute())) {
            getLog().warn("Script is not javascript (type: " + getTypeAttribute() 
                    + ", language: " + getLanguageAttribute() + "). Skipping execution.");
            return false;
        }
        
        return true;
    }

    /**
     * For internal use only.
     */
    void executeScriptIfNeeded() {
        if (!isExecutionNeeded()) {
            return;
        }
        final String src = getSrcAttribute();
        if(src.equals(SLASH_SLASH_COLON)){
            return;
        }
        if (src != HtmlElement.ATTRIBUTE_NOT_DEFINED) {
            getLog().debug("Loading external javascript: " + src);
            getPage().loadExternalJavaScriptFile(src, getCharsetAttribute());
        }
        else if (getFirstChild() != null) {
            executeInlineScriptIfNeeded();
        }
    }

    /**
     * @see com.gargoylesoftware.htmlunit.html.HtmlInput#asText()
     * @return an empty string as the content of script is not visible by itself
     */
    public String asText() {
        return "";
    }

}
