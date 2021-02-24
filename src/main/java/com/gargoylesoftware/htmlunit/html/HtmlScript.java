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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.AbstractJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;

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

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "script";

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
     * {@inheritDoc}
     */
    @Override
    public boolean isDeferred() {
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

        // namespaceURI is always null here - we can call getAttribute directly
        final String oldValue = getAttribute(qualifiedName);
        super.setAttributeNS(null, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);

        if (isAttachedToPage() && oldValue.isEmpty() && getFirstChild() == null) {
            final PostponedAction action = new PostponedAction(getPage()) {
                @Override
                public void execute() {
                    ScriptElementSupport.executeScriptIfNeeded(HtmlScript.this);
                }
            };
            final AbstractJavaScriptEngine<?> engine = getPage().getWebClient().getJavaScriptEngine();
            engine.addPostponedAction(action);
        }
    }

    /**
     * Executes the <tt>onreadystatechange</tt> handler when simulating IE, as well as executing
     * the script itself, if necessary.
     * {@inheritDoc}
     */
    @Override
    public void onAllChildrenAddedToPage(final boolean postponed) {
        ScriptElementSupport.onAllChildrenAddedToPage(this, postponed);
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
     * @see com.gargoylesoftware.htmlunit.html.HtmlInput#asText()
     *
     * @return an empty string as the content of script is not visible by itself
     *
     * @deprecated as of version 2.48.0; use asNormalizedText() instead
     */
    @Deprecated
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
