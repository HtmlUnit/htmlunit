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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlAttr;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFrameSet;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;

/**
 * The javascript object "HTMLElement" which is the base class for all HTML
 * objects. This will typically wrap an instance of {@link HtmlElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Hans Donner
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 */
public class HTMLElement extends Element implements ScriptableWithFallbackGetter {

    private static final long serialVersionUID = -6864034414262085851L;
    private static final int BEHAVIOR_ID_UNKNOWN = -1;
    static final int BEHAVIOR_ID_CLIENT_CAPS = 0;
    static final int BEHAVIOR_ID_HOMEPAGE = 1;
    static final int BEHAVIOR_ID_DOWNLOAD = 2;
    private static final String BEHAVIOR_CLIENT_CAPS = "#default#clientCaps";
    private static final String BEHAVIOR_HOMEPAGE = "#default#homePage";
    private static final String BEHAVIOR_DOWNLOAD = "#default#download";
    static final String POSITION_BEFORE_BEGIN = "beforeBegin";
    static final String POSITION_AFTER_BEGIN = "afterBegin";
    static final String POSITION_BEFORE_END = "beforeEnd";
    static final String POSITION_AFTER_END = "afterEnd";

    /**
     * Static counter for {@link #uniqueID_}.
     */
    private static int UniqueID_Counter_ = 1;

    private final Set<String> behaviors_ = new HashSet<String>();
    private BoxObject boxObject_; // lazy init
    private HTMLCollection all_; // has to be a member to have equality (==) working
    private int scrollLeft_;
    private int scrollTop_;
    private String uniqueID_;

    /**
     * The tag names of the objects for which outerHTML is readonly
     */
    private static final List<String> OUTER_HTML_READONLY =
        Arrays.asList(new String[] {
            "caption", "col", "colgroup", "frameset", "html",
            "tbody", "td", "tfoot", "th", "thead", "tr"});

    private CSSStyleDeclaration style_;

    /**
     * Create an instance.
     */
    public HTMLElement() {
        // Empty.
    }

    /**
     * Return the value of the "all" property.
     * @return The value of the "all" property
     */
    public HTMLCollection jsxGet_all() {
        if (all_ == null) {
            all_ = new HTMLCollection(this);
            try {
                all_.init(getDomNodeOrDie(), new HtmlUnitXPath(".//*"));
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Failed to initialize collection all: " + e.getMessage());
            }
        }
        return all_;
    }

    /**
     * Return the style object for this element.
     *
     * @return The style object
     */
    public Object jsxGet_style() {
        return style_;
    }

    /**
     * Returns the current style object for this element.
     * @return The current style object
     */
    public Object jsxGet_currentStyle() {
        return style_;
    }

    /**
     * Returns the runtime style object for this element.
     * @return The runtime style object
     */
    public Object jsxGet_runtimeStyle() {
        return style_;
    }

    /**
     * Set the DOM node that corresponds to this javascript object
     * @param domNode The DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        style_ = new CSSStyleDeclaration(this);

        /**
         * Convert javascript snippets defined in the attribute map to executable event handlers.
         * Should be called only on construction.
         */
        final HtmlElement htmlElt = (HtmlElement) domNode;
        for (final HtmlAttr attr : htmlElt.getAttributesCollection()) {
            final String eventName = attr.getName();
            if (eventName.startsWith("on")) {
                // TODO: check that it is an "allowed" event for the browser, and take care to the case
                final BaseFunction eventHandler = new EventHandler(htmlElt, eventName, attr.getHtmlValue());
                setEventHandler(eventName, eventHandler);
                // forward onload, onclick, ondblclick, ... to window
                if ((domNode instanceof HtmlBody || domNode instanceof HtmlFrameSet)) {
                    getWindow().getEventListenersContainer()
                        .setEventHandlerProp(eventName.substring(2), eventHandler);
                }
            }
        }
    }

    /**
     * Return the element ID.
     * @return The ID of this element.
     */
    public String jsxGet_id() {
        return getHtmlElementOrDie().getId();
    }

    /**
     * Set the identifier this element.
     * @param newId The new identifier of this element.
     */
    public void jsxSet_id(final String newId) {
        getHtmlElementOrDie().setId(newId);
    }

    /**
     * Return the element title.
     * @return The ID of this element.
     */
    public String jsxGet_title() {
        return getHtmlElementOrDie().getAttributeValue("title");
    }

    /**
     * Set the title of this element.
     * @param newTitle The new identifier of this element.
     */
    public void jsxSet_title(final String newTitle) {
        getHtmlElementOrDie().setAttributeValue("title", newTitle);
    }

    /**
     * Return true if this element is disabled.
     * @return True if this element is disabled.
     */
    public boolean jsxGet_disabled() {
        return getHtmlElementOrDie().isAttributeDefined("disabled");
    }

    /**
     * Set whether or not to disable this element
     * @param disabled True if this is to be disabled.
     */
    public void jsxSet_disabled(final boolean disabled) {
        final HtmlElement element = getHtmlElementOrDie();
        if (disabled) {
            element.setAttributeValue("disabled", "disabled");
        }
        else {
            element.removeAttribute("disabled");
        }
    }

    /**
     * Return the tag name of this element.
     * @return The tag name in uppercase.
     */
    public String jsxGet_tagName() {
        String tagName = getHtmlElementOrDie().getTagName();
        if (jsxGet_namespaceURI() == null) {
            tagName = tagName.toUpperCase();
        }
        return tagName;
    }

    /**
     * Returns The URI that identifies an XML namespace.
     * @return The URI that identifies an XML namespace.
     */
    public String jsxGet_namespaceURI() {
        return getHtmlElementOrDie().getNamespaceURI();
    }

    /**
     * Returns The local name (without prefix).
     * @return The local name (without prefix).
     */
    public String jsxGet_localName() {
        String localName = getHtmlElementOrDie().getLocalName();
        if (jsxGet_namespaceURI() == null) {
            localName = localName.toUpperCase();
        }
        return localName;
    }

    /**
     * Returns The Namespace prefix
     * @return The Namespace prefix.
     */
    public String jsxGet_prefix() {
        return getHtmlElementOrDie().getPrefix();
    }

    /**
     * Return the owner document
     * @return the document
     */
    public Object jsxGet_ownerDocument() {
        return getWindow().jsxGet_document();
    }

    /**
     * Looks at attributes with the given name
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        final HtmlElement htmlElement = getHtmlElementOrNull();
        // can name be an attribute of current element?
        // first approximation: attribute are all lowercase
        // this should be improved because it's wrong. For instance: tabIndex, hideFocus, acceptCharset
        if (htmlElement != null && name.toLowerCase().equals(name)) {
            final String value = htmlElement.getAttributeValue(name);
            if (HtmlElement.ATTRIBUTE_NOT_DEFINED != value) {
                getLog().debug("Found attribute for evaluation of property \"" + name
                        + "\" for of " + this);
                return value;
            }
        }

        return NOT_FOUND;
    }

    /**
     * Returns a collection of the attributes of this element.
     * @return a collection of the attributes of this element
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.attributes">Gecko DOM Reference</a>
     */
    public NamedNodeMap jsxGet_attributes() {
        return new NamedNodeMap(getHtmlElementOrDie());
    }

    /**
     * Gets the specified attribute.
     * @param attributeName attribute name.
     * @return The value of the specified attribute, <code>null</code> if the attribute is not defined
     */
    public String jsxFunction_getAttribute(final String attributeName) {
        final String value = getHtmlElementOrDie().getAttributeValue(attributeName);
        if (value == HtmlElement.ATTRIBUTE_NOT_DEFINED) {
            return null;
        }
        else {
            return value;
        }
    }

    /**
     * Gets the specified attribute.
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to look for
     * @return The value of the specified attribute, <code>null</code> if the attribute is not defined
     */
    public String jsxFunction_getAttributeNS(final String namespaceURI, final String localName) {
        return getHtmlElementOrDie().getAttributeNS(namespaceURI, localName);
    }

    /**
     * Test for attribute.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-ElHasAttr">
     * the DOM reference</a>
     *
     * @param name Name of the attribute to test
     * @return <code>true</code> if the node has this attribute
     */
    public boolean jsxFunction_hasAttribute(final String name) {
        return getHtmlElementOrDie().getAttribute(name) != HtmlElement.ATTRIBUTE_NOT_DEFINED;
    }

    /**
     * Test for attribute.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-ElHasAttrNS">
     * the DOM reference</a>
     *
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to look for
     * @return <code>true</code> if the node has this attribute
     */
    public boolean jsxFunction_hasAttributeNS(final String namespaceURI, final String localName) {
        return getHtmlElementOrDie().getAttributeNS(namespaceURI, localName) != HtmlElement.ATTRIBUTE_NOT_DEFINED;
    }

    /**
     * Set an attribute.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-F68F082">
     * the DOM reference</a>
     *
     * @param name Name of the attribute to set
     * @param value Value to set the attribute to
     */
    public void jsxFunction_setAttribute(final String name, final String value) {
        getHtmlElementOrDie().setAttributeValue(name, value);

        //FF: call corresponding event handler jsxSet_onxxx if found
        if (getWindow().getWebWindow().getWebClient().getBrowserVersion().isNetscape()) {
            try {
                final Method method = getClass().getMethod("jsxSet_" + name, new Class[] {Object.class});
                final String source = "function(){" + value + "}";
                method.invoke(this, new Object[] {
                        Context.getCurrentContext().compileFunction(getWindow(), source, "", 0, null)});
            }
            catch (final NoSuchMethodException e) {
                //silently ignore
            }
            catch (final IllegalAccessException e) {
                //silently ignore
            }
            catch (final InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    /**
     * Sets the specified attribute.
     * @param namespaceURI the namespace URI
     * @param qualifiedName the local name of the attribute to look for
     * @param value the new attribute value
     */
    public void jsxFunction_setAttributeNS(final String namespaceURI, final String qualifiedName, final String value) {
        getHtmlElementOrDie().setAttributeValue(namespaceURI, qualifiedName, value);
    }

    /**
     * Remove an attribute.
     *
     * @param name Name of the attribute to remove
     */
    public void jsxFunction_removeAttribute(final String name) {
        getHtmlElementOrDie().removeAttribute(name);
    }

    /**
     * Gets the attribute node for the specified attribute.
     * @param attributeName the name of the attribute to retrieve
     * @return the attribute node for the specified attribute.
     */
    public Object jsxFunction_getAttributeNode(final String attributeName) {
        final Attribute att = new Attribute();
        att.setPrototype(getPrototype(Attribute.class));
        att.setParentScope(getWindow());
        att.init(attributeName, getHtmlElementOrDie());
        return att;
    }

    /**
     * Sets the attribute node for the specified attribute.
     * @param newAtt the attribute to set.
     * @return the replaced attribute node, if any.
     */
    public Attribute jsxFunction_setAttributeNode(final Attribute newAtt) {
        final String name = newAtt.jsxGet_name();
        final String value = newAtt.jsxGet_value();
        final Attribute replacedAtt = (Attribute) jsxFunction_getAttributeNode(name);
        replacedAtt.detachFromParent();
        getHtmlElementOrDie().setAttributeValue(name, value);
        return replacedAtt;
    }

    /**
     * Returns all the descendant elements with the specified tag name.
     * @param tagName the name to search for
     * @return all the descendant elements with the specified tag name
     */
    public Object jsxFunction_getElementsByTagName(final String tagName) {
        final DomNode node = getDomNodeOrDie();
        final HTMLCollection collection = new HTMLCollection(this);
        try {
            final String xpath;
            if ("*".equals(tagName)) {
                xpath = "//*";
            }
            else {
                xpath = "//node()[name() = '" + tagName.toLowerCase() + "']";
            }
            collection.init(node, new HtmlUnitXPath(xpath, HtmlUnitXPath.buildSubtreeNavigator(node)));
        }
        catch (final JaxenException e) {
            final String msg = "Error initializing collection getElementsByTagName(" + tagName + "): ";
            throw Context.reportRuntimeError(msg + e.getMessage());
        }
        return collection;
    }

    /**
     * Return the class defined for this element
     * @return the class name
     */
    public Object jsxGet_className() {
        return getHtmlElementOrDie().getAttributeValue("class");
    }

    /**
     * Return "clientHeight" attribute.
     * @return the clientHeight attribute.
     */
    public int jsxGet_clientHeight() {
        return 0;
    }

    /**
     * Return "clientWidth" attribute.
     * @return the clientWidth attribute.
     */
    public int jsxGet_clientWidth() {
        return 0;
    }

    /**
     * Set the class attribute for this element.
     * @param className - the new class name
     */
    public void jsxSet_className(final String className) {
        getHtmlElementOrDie().setAttributeValue("class", className);
    }

    /**
     * Get the innerHTML attribute
     * @return the contents of this node as html
     */
    public String jsxGet_innerHTML() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, getDomNodeOrDie(), !"SCRIPT".equals(jsxGet_tagName()));
        return buf.toString();
    }

    /**
     * Get the innerText attribute
     * @return the contents of this node as text
     */
    public String jsxGet_innerText() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, getDomNodeOrDie(), false);
        return buf.toString();
    }

    /**
     * Gets the outerHTML of the node.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/outerhtml.asp">
     * MSDN documentation</a>
     * @return the contents of this node as HTML
     */
    public String jsxGet_outerHTML() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printNode(buf, getDomNodeOrDie(), true);
        return buf.toString();
    }

    private void printChildren(final StringBuilder buffer, final DomNode node, final boolean html) {
        for (final DomNode child : node.getChildren()) {
            printNode(buffer, child, html);
        }
    }

    private void printNode(final StringBuilder buffer, final DomNode node, final boolean html) {
        if (node instanceof DomComment) {
            // Remove whitespace sequences.
            final String s = node.getNodeValue().replaceAll("  ", " ");
            buffer.append("<!--").append(s).append("-->");
        }
        else if (node instanceof DomCharacterData) {
            // Remove whitespace sequences, possibly escape XML characters.
            String s = node.getNodeValue().replaceAll("  ", " ");
            if (html) {
                s = com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlChars(s);
            }
            buffer.append(s);
        }
        else if (html) {
            // Start the tag name. IE does it in uppercase, FF in lowercase.
            final HtmlElement element = (HtmlElement) node;
            final boolean ie = getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE();
            String tag = element.getTagName();
            if (ie) {
                tag = tag.toUpperCase();
            }
            buffer.append("<").append(tag);
            // Add the attributes. IE does not use quotes, FF does.
            for (final HtmlAttr attr : element.getAttributesCollection()) {
                final String name = attr.getName();
                final String value = (String) attr.getHtmlValue();
                final boolean quote = !ie || com.gargoylesoftware.htmlunit.util.StringUtils.containsWhitespace(value);
                buffer.append(' ').append(name).append("=");
                if (quote) {
                    buffer.append("\"");
                }
                buffer.append(value);
                if (quote) {
                    buffer.append("\"");
                }
            }
            buffer.append(">");
            // Add the children.
            printChildren(buffer, node, html);
            // Close the tag. IE does it in uppercase, FF in lowercase.
            buffer.append("</").append(tag).append(">");
        }
        else {
            final HtmlElement element = (HtmlElement) node;
            if (element.getTagName().equals("p")) {
                buffer.append("\r\n"); // \r\n because it's to implement something IE specific
            }
            if (!element.getTagName().equals("script")) {
                printChildren(buffer, node, html);
            }
        }
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * @param value - the new value for the contents of this node
     */
    public void jsxSet_innerHTML(final Object value) {
        final DomNode domNode = getDomNodeOrDie();
        domNode.removeAllChildren();

        final BrowserVersion browserVersion = getWindow().getWebWindow().getWebClient().getBrowserVersion();

        // null && IE     -> add child
        // null && non-IE -> Don't add
        // ''             -> Don't add
        if ((value == null && browserVersion.isIE())
            || (value != null && !"".equals(value))) {

            final String valueAsString = Context.toString(value);
            parseHtmlSnippet(domNode, true, valueAsString);

            //if the parentNode has null parentNode in IE,
            //create a DocumentFragment to be the parentNode's parentNode.
            if (domNode.getParentDomNode() == null
                    && getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE()) {
                final DomDocumentFragment fragment = ((HtmlPage) domNode.getPage()).createDomDocumentFragment();
                fragment.appendDomChild(domNode);
            }
        }
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * @param value - the new value for the contents of this node
     */
    public void jsxSet_innerText(final String value) {
        final DomNode domNode = getDomNodeOrDie();
        domNode.removeAllChildren();

        final DomNode node = new DomText(getDomNodeOrDie().getPage(), value);
        domNode.appendDomChild(node);

        //if the parentNode has null parentNode in IE,
        //create a DocumentFragment to be the parentNode's parentNode.
        if (domNode.getParentDomNode() == null
                && getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE()) {
            final DomDocumentFragment fragment = ((HtmlPage) domNode.getPage()).createDomDocumentFragment();
            fragment.appendDomChild(domNode);
        }
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * Sets the outerHTML of the node.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/outerhtml.asp">
     * MSDN documentation</a>
     * @param value - the new value for replacing this node
     */
    public void jsxSet_outerHTML(final String value) {
        final DomNode domNode = getDomNodeOrDie();

        if (OUTER_HTML_READONLY.contains(domNode.getNodeName())) {
            throw Context.reportRuntimeError("outerHTML is read-only for tag " + domNode.getNodeName());
        }

        parseHtmlSnippet(domNode, false, value);
        domNode.remove();
    }

    /**
     * Parses the html code
     * @param htmlSnippet the html code extract to parse
     */
    static void parseHtmlSnippet(final DomNode target, final boolean append, final String source) {

        final DomNode proxyNode = new HtmlDivision(null, HtmlDivision.TAG_NAME, (HtmlPage) target.getPage(), null) {
            private static final long serialVersionUID = 2108037256628269797L;
            @Override
            public DomNode appendDomChild(final DomNode node) {
                if (append) {
                    return target.appendDomChild(node);
                }
                else {
                    target.insertBefore(node);
                    return node;
                }
            }
        };

        try {
            HTMLParser.parseFragment(proxyNode, source);
        }
        catch (final IOException e) {
            LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing html snippet", e);
            throw Context.reportRuntimeError("Unexpected exception occurred while parsing html snippet: "
                    + e.getMessage());
        }
        catch (final SAXException e) {
            LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing html snippet", e);
            throw Context.reportRuntimeError("Unexpected exception occurred while parsing html snippet: "
                    + e.getMessage());
        }
    }

    /**
     * Gets the attributes of the element in the form of a {@link org.xml.sax.Attributes}
     * @param element the element to read the attributes from
     * @return the attributes
     */
    protected AttributesImpl readAttributes(final HtmlElement element) {
        final AttributesImpl attributes = new AttributesImpl();
        for (final HtmlAttr entry : element.getAttributesCollection()) {
            final String name = entry.getName();
            final String value = entry.getHtmlValue();
            attributes.addAttribute(null, name, name, null, value);
        }

        return attributes;
    }

    /**
     * Inserts the given HTML text into the element at the location.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536452.aspx">
     * MSDN documentation</a>
     * @param where specifies where to insert the HTML text, using one of the following value:
     *        beforeBegin, afterBegin, beforeEnd, afterEnd
     * @param text the HTML text to insert
     */
    public void jsxFunction_insertAdjacentHTML(final String where, final String text) {
        final Object[] values = getInsertAdjacentLocation(where);
        final DomNode node = (DomNode) values[0];
        final boolean append = ((Boolean) values[1]).booleanValue();

        // add the new nodes
        parseHtmlSnippet(node, append, text);
    }

    /**
     * Inserts the given element into the element at the location.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536451.aspx">
     * MSDN documentation</a>
     * @param where specifies where to insert the element, using one of the following value:
     *        beforeBegin, afterBegin, beforeEnd, afterEnd
     * @param object the element to insert
     * @return an element object
     */
    public Object jsxFunction_insertAdjacentElement(final String where, final Object object) {
        if (object instanceof Node) {
            final DomNode childNode = ((Node) object).getDomNodeOrDie();
            final Object[] values = getInsertAdjacentLocation(where);
            final DomNode node = (DomNode) values[0];
            final boolean append = ((Boolean) values[1]).booleanValue();

            if (append) {
                node.appendDomChild(childNode);
            }
            else {
                node.insertBefore(childNode);
            }
            return object;
        }
        else {
            throw Context.reportRuntimeError("Passed object is not an element: " + object);
        }
    }

    /**
     * Returns where and how to add the new node.
     * Used by {@link #jsxFunction_insertAdjacentHTML(String, String)} and
     * {@link #jsxFunction_insertAdjacentElement(String, Object)}.
     *
     * @param where specifies where to insert the element, using one of the following value:
     *        beforeBegin, afterBegin, beforeEnd, afterEnd
     *
     * @return an array of 1-DomNode:parentNode and 2-Boolean:append
     */
    private Object[] getInsertAdjacentLocation(final String where) {
        final DomNode currentNode = getDomNodeOrDie();
        final DomNode node;
        final boolean append;
        
        // compute the where and how the new nodes should be added
        if (POSITION_AFTER_BEGIN.equalsIgnoreCase(where)) {
            if (currentNode.getFirstDomChild() == null) {
                // new nodes should appended to the children of current node
                node = currentNode;
                append = true;
            }
            else {
                // new nodes should be inserted before first child
                node = currentNode.getFirstDomChild();
                append = false;
            }
        }
        else if (POSITION_BEFORE_BEGIN.equalsIgnoreCase(where)) {
            // new nodes should be inserted before current node
            node = currentNode;
            append = false;
        }
        else if (POSITION_BEFORE_END.equalsIgnoreCase(where)) {
            // new nodes should appended to the children of current node
            node = currentNode;
            append = true;
        }
        else if (POSITION_AFTER_END.equalsIgnoreCase(where)) {
            if (currentNode.getNextDomSibling() == null) {
                // new nodes should appended to the children of parent node
                node = currentNode.getParentDomNode();
                append = true;
            }
            else {
                // new nodes should be inserted before current node's next sibling
                node = currentNode.getNextDomSibling();
                append = false;
            }
        }
        else {
            throw Context.reportRuntimeError("Illegal position value: \"" + where + "\"");
        }
        
        if (append) {
            return new Object[] {node, Boolean.TRUE};
        }
        else {
            return new Object[] {node, Boolean.FALSE};
        }
    }
    
    /**
     * Adds the specified behavior to this HTML element. Currently only supports
     * the following default IE behaviors:
     * <ul>
     *   <li>#default#clientCaps</li>
     *   <li>#default#homePage</li>
     *   <li>#default#download</li>
     * </ul>
     * @param behavior the URL of the behavior to add, or a default behavior name
     * @return an identifier that can be user later to detach the behavior from the element
     */
    public int jsxFunction_addBehavior(final String behavior) {
        // if behavior already defined, then nothing to do
        if (behaviors_.contains(behavior)) {
            return 0;
        }

        final Class< ? extends HTMLElement> c = getClass();
        if (BEHAVIOR_CLIENT_CAPS.equalsIgnoreCase(behavior)) {
            defineProperty("availHeight", c, 0);
            defineProperty("availWidth", c, 0);
            defineProperty("bufferDepth", c, 0);
            defineProperty("colorDepth", c, 0);
            defineProperty("connectionType", c, 0);
            defineProperty("cookieEnabled", c, 0);
            defineProperty("cpuClass", c, 0);
            defineProperty("height", c, 0);
            defineProperty("javaEnabled", c, 0);
            defineProperty("platform", c, 0);
            defineProperty("systemLanguage", c, 0);
            defineProperty("userLanguage", c, 0);
            defineProperty("width", c, 0);
            defineFunctionProperties(new String[] {"addComponentRequest"}, c, 0);
            defineFunctionProperties(new String[] {"clearComponentRequest"}, c, 0);
            defineFunctionProperties(new String[] {"compareVersions"}, c, 0);
            defineFunctionProperties(new String[] {"doComponentRequest"}, c, 0);
            defineFunctionProperties(new String[] {"getComponentVersion"}, c, 0);
            defineFunctionProperties(new String[] {"isComponentInstalled"}, c, 0);
            behaviors_.add(BEHAVIOR_CLIENT_CAPS);
            return BEHAVIOR_ID_CLIENT_CAPS;
        }
        else if (BEHAVIOR_HOMEPAGE.equalsIgnoreCase(behavior)) {
            defineFunctionProperties(new String[] {"isHomePage"}, c, 0);
            defineFunctionProperties(new String[] {"setHomePage"}, c, 0);
            defineFunctionProperties(new String[] {"navigateHomePage"}, c, 0);
            behaviors_.add(BEHAVIOR_CLIENT_CAPS);
            return BEHAVIOR_ID_HOMEPAGE;
        }
        else if (BEHAVIOR_DOWNLOAD.equalsIgnoreCase(behavior)) {
            defineFunctionProperties(new String[] {"startDownload"}, c, 0);
            behaviors_.add(BEHAVIOR_DOWNLOAD);
            return BEHAVIOR_ID_DOWNLOAD;
        }
        else {
            getLog().warn("Unimplemented behavior: " + behavior);
            return BEHAVIOR_ID_UNKNOWN;
        }
    }

    /**
     * Removes the behavior corresponding to the specified identifier from this element.
     * @param id the identifier for the behavior to remove
     */
    public void jsxFunction_removeBehavior(final int id) {
        switch (id) {
            case BEHAVIOR_ID_CLIENT_CAPS:
                delete("availHeight");
                delete("availWidth");
                delete("bufferDepth");
                delete("colorDepth");
                delete("connectionType");
                delete("cookieEnabled");
                delete("cpuClass");
                delete("height");
                delete("javaEnabled");
                delete("platform");
                delete("systemLanguage");
                delete("userLanguage");
                delete("width");
                delete("addComponentRequest");
                delete("clearComponentRequest");
                delete("compareVersions");
                delete("doComponentRequest");
                delete("getComponentVersion");
                delete("isComponentInstalled");
                behaviors_.remove(BEHAVIOR_CLIENT_CAPS);
                break;
            case BEHAVIOR_ID_HOMEPAGE:
                delete("isHomePage");
                delete("setHomePage");
                delete("navigateHomePage");
                behaviors_.remove(BEHAVIOR_HOMEPAGE);
                break;
            case BEHAVIOR_ID_DOWNLOAD:
                delete("startDownload");
                behaviors_.remove(BEHAVIOR_DOWNLOAD);
                break;
            default:
                getLog().warn("Unexpected behavior id: " + id + ". Ignoring.");
        }
    }

    //----------------------- START #default#clientCaps BEHAVIOR -----------------------

    /**
     * Returns the screen's available height. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's available height.
     */
    public int getAvailHeight() {
        return getWindow().jsxGet_screen().jsxGet_availHeight();
    }

    /**
     * Returns the screen's available width. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's available width.
     */
    public int getAvailWidth() {
        return getWindow().jsxGet_screen().jsxGet_availWidth();
    }

    /**
     * Returns the screen's buffer depth. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's buffer depth.
     */
    public int getBufferDepth() {
        return getWindow().jsxGet_screen().jsxGet_bufferDepth();
    }

    /**
     * Returns BoxObject for this element.
     * @return BoxObject for this element.
     */
    public BoxObject getBoxObject() {
        if (boxObject_ == null) {
            boxObject_ = new BoxObject();
            boxObject_.setParentScope(getWindow());
            boxObject_.setPrototype(getPrototype(boxObject_.getClass()));
        }
        return boxObject_;
    }
    
    /**
     * Returns the screen's color depth. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's color depth.
     */
    public int getColorDepth() {
        return getWindow().jsxGet_screen().jsxGet_colorDepth();
    }

    /**
     * Returns the connection type being used. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the connection type being used.
     * Current implementation always return "modem"
     */
    public String getConnectionType() {
        return "modem";
    }

    /**
     * Returns <tt>true</tt> if cookies are enabled. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return whether or not cookies are enabled.
     */
    public boolean getCookieEnabled() {
        return getWindow().jsxGet_navigator().jsxGet_cookieEnabled();
    }

    /**
     * Returns the type of CPU used. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the type of CPU used.
     */
    public String getCpuClass() {
        return getWindow().jsxGet_navigator().jsxGet_cpuClass();
    }

    /**
     * Returns the screen's height. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's height.
     */
    public int getHeight() {
        return getWindow().jsxGet_screen().jsxGet_height();
    }

    /**
     * Returns <tt>true</tt> if Java is enabled. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return whether or not Java is enabled.
     */
    public boolean getJavaEnabled() {
        return getWindow().jsxGet_navigator().jsxFunction_javaEnabled();
    }

    /**
     * Returns the platform used. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the platform used.
     */
    public String getPlatform() {
        return getWindow().jsxGet_navigator().jsxGet_platform();
    }

    /**
     * Returns the system language. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the system language.
     */
    public String getSystemLanguage() {
        return getWindow().jsxGet_navigator().jsxGet_systemLanguage();
    }

    /**
     * Returns the user language. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the user language.
     */
    public String getUserLanguage() {
        return getWindow().jsxGet_navigator().jsxGet_userLanguage();
    }

    /**
     * Returns the screen's width. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's width.
     */
    public int getWidth() {
        return getWindow().jsxGet_screen().jsxGet_width();
    }

    /**
     * Adds the specified component to the queue of components to be installed. Note
     * that no components ever get installed, and this call is always ignored. Part of
     * the <tt>#default#clientCaps</tt> default IE behavior implementation.
     * @param id the identifier for the component to install
     * @param idType the type of identifier specified
     * @param minVersion the minimum version of the component to install
     */
    public void addComponentRequest(final String id, final String idType, final String minVersion) {
        getLog().debug("Call to addComponentRequest(" + id + ", " + idType + ", " + minVersion + ") ignored.");
    }

    /**
     * Clears the component install queue of all component requests. Note that no components
     * ever get installed, and this call is always ignored. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     */
    public void clearComponentRequest() {
        getLog().debug("Call to clearComponentRequest() ignored.");
    }

    /**
     * Compares the two specified version numbers. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @param v1 the first of the two version numbers to compare
     * @param v2 the second of the two version numbers to compare
     * @return -1 if v1 < v2, 0 if v1 = v2, and 1 if v1 > v2
     */
    public int compareVersions(final String v1, final String v2) {
        final int i = v1.compareTo(v2);
        if (i == 0)     { return 0;  }
        else if (i < 0) { return -1; }
        else           { return 1;  }
    }

    /**
     * Downloads all the components queued via {@link #addComponentRequest(String, String, String)}.
     * @return <tt>true</tt> if the components are downloaded successfully.
     * Current implementation always return <code>false</code>
     */
    public boolean doComponentRequest() {
        return false;
    }

    /**
     * Returns the version of the specified component.
     * @param id the identifier for the component whose version is to be returned
     * @param idType the type of identifier specified
     * @return the version of the specified component.
     */
    public String getComponentVersion(final String id, final String idType) {
        if ("{E5D12C4E-7B4F-11D3-B5C9-0050045C3C96}".equals(id)) { //Yahoo Messenger
            return "";
        }
        return "1.0";
    }

    /**
     * Returns <tt>true</tt> if the specified component is installed.
     * @param id the identifier for the component to check for
     * @param idType the type of id specified
     * @param minVersion the minimum version to check for
     * @return <tt>true</tt> if the specified component is installed.
     */
    public boolean isComponentInstalled(final String id, final String idType, final String minVersion) {
        return false;
    }

    //----------------------- START #default#download BEHAVIOR -----------------------
    /**
     * Implementation of the IE behavior #default#download
     * @param uri The URI of the download source
     * @param callback the method which should be called when the download is finished
     * @see <a href="http://msdn.microsoft.com/workshop/author/behaviors/reference/methods/startdownload.asp">
     * MSDN documentation</a>
     * @throws MalformedURLException If the url cannot be created
     */
    public void startDownload(final String uri, final Function callback) throws MalformedURLException {
        final HtmlPage page = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
        final URL url = page.getFullyQualifiedUrl(uri);
        if (!page.getWebResponse().getUrl().getHost().equals(url.getHost())) {
            throw Context.reportRuntimeError("Not authorized url: " + url);
        }
        final Thread t = new DownloadBehaviorDownloader(url, callback);
        getLog().debug("Starting download thread for " + url);
        t.start();
    }

    /**
     * A helper class for the IE behavior #default#download
     * This represents a download action. The download is handled
     * asynchronously, when the download is finished, the method specified
     * by callback is called with one argument - the content of the response as string.
     * @see #startDownload(String, Function)
     * @author <a href="mailto:stefan@anzinger.net">Stefan Anzinger</a>
     */
    private class DownloadBehaviorDownloader extends Thread {
        private final  URL url_;
        private final Function callback_;

        /**
         * @param url The URL to download
         * @param callback The function to callback
         */
        public DownloadBehaviorDownloader(final URL url, final Function callback) {
            super("Downloader for behavior #default#download '" + url + "'");
            url_ = url;
            callback_ = callback;
        }

        /**
         * Does the download and calls the callback method
         */
        @Override
        public void run() {
            final WebClient wc = getWindow().getWebWindow().getWebClient();
            final Scriptable scope = callback_.getParentScope();
            final WebRequestSettings settings = new WebRequestSettings(url_);

            try {
                final WebResponse webResponse = wc.loadWebResponse(settings);
                final String content = webResponse.getContentAsString();
                getLog().debug("Downloaded content: " + StringUtils.abbreviate(content, 512));
                final Object[] args = new Object[] {content};
                final ContextAction action = new ContextAction() {
                    public Object run(final Context cx) {
                        callback_.call(cx, scope, scope, args);
                        return null;
                    }
                };
                Context.call(action);
            }
            catch (final Exception e) {
                getLog().error("Behavior #default#download: Cannot download " + url_, e);
            }
        }
    }
    //----------------------- END #default#download BEHAVIOR -----------------------

    //----------------------- START #default#homePage BEHAVIOR -----------------------

    /**
     * Returns <tt>true</tt> if the specified URL is the web client's current
     * homepage and the document calling the method is on the same domain as the
     * user's homepage. Part of the <tt>#default#homePage</tt> default IE behavior
     * implementation.
     * @param url the URL to check
     * @return <tt>true</tt> if the specified URL is the current homepage
     */
    public boolean isHomePage(final String url) {
        try {
            final URL newUrl = new URL(url);
            final URL currentUrl = getDomNodeOrDie().getPage().getWebResponse().getUrl();
            final String home = getDomNodeOrDie().getPage().getEnclosingWindow().getWebClient().getHomePage();
            final boolean sameDomains = newUrl.getHost().equalsIgnoreCase(currentUrl.getHost());
            final boolean isHomePage = (home != null && home.equals(url));
            return (sameDomains && isHomePage);
        }
        catch (final MalformedURLException e) {
            return false;
        }
    }

    /**
     * Sets the web client's current homepage. Part of the <tt>#default#homePage</tt>
     * default IE behavior implementation.
     * @param url the new homepage URL
     */
    public void setHomePage(final String url) {
        getDomNodeOrDie().getPage().getEnclosingWindow().getWebClient().setHomePage(url);
    }

    /**
     * Causes the web client to navigate to the current home page. Part of the
     * <tt>#default#homePage</tt> default IE behavior implementation.
     * @throws IOException if loading home page fails
     */
    public void navigateHomePage() throws IOException {
        final WebClient webClient = getDomNodeOrDie().getPage().getEnclosingWindow().getWebClient();
        webClient.getPage(webClient.getHomePage());
    }

    //----------------------- END #default#homePage BEHAVIOR -----------------------

    /**
     * Get the children of the current node.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/collections/children.asp">
     * MSDN documentation</a>
     * @return the child at the given position
     */
    public Object jsxGet_children() {
        final DomNode element = getDomNodeOrDie();
        final HTMLCollection children = new HTMLCollection(this);

        try {
            final XPath xpath = new HtmlUnitXPath("./*", HtmlUnitXPath.buildSubtreeNavigator(element));
            children.init(element, xpath);
        }
        catch (final JaxenException e) {
            // should never occur
            throw Context.reportRuntimeError("Failed initializing children: " + e.getMessage());
        }
        return children;
    }

    /**
     * Set the onclick event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onclick(final Object handler) {
        setEventHandlerProp("onclick", handler);
    }

    /**
     * Get the onclick event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onclick() {
        return getEventHandlerProp("onclick");
    }

    /**
     * Set the ondblclick event handler for this element.
     * @param handler the new handler
     **/
    public void jsxSet_ondblclick(final Object handler) {
        setEventHandlerProp("ondblclick", handler);
    }

    /**
     * Get the ondblclick event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_ondblclick() {
        return getEventHandlerProp("ondblclick");
    }

    /**
     * Set the onblur event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onblur(final Object handler) {
        setEventHandlerProp("onblur", handler);
    }

    /**
     * Get the onblur event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onblur() {
        return getEventHandlerProp("onblur");
    }

    /**
     * Set the onfocus event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onfocus(final Object handler) {
        setEventHandlerProp("onfocus", handler);
    }

    /**
     * Get the onfocus event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onfocus() {
        return getEventHandlerProp("onfocus");
    }

    /**
     * Set the onkeydown event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onkeydown(final Object handler) {
        setEventHandlerProp("onkeydown", handler);
    }

    /**
     * Get the onkeydown event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onkeydown() {
        return getEventHandlerProp("onkeydown");
    }

    /**
     * Set the onkeypress event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onkeypress(final Object handler) {
        setEventHandlerProp("onkeypress", handler);
    }

    /**
     * Get the onkeypress event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onkeypress() {
        return getEventHandlerProp("onkeypress");
    }

    /**
     * Set the onkeyup event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onkeyup(final Object handler) {
        setEventHandlerProp("onkeyup", handler);
    }

    /**
     * Get the onkeyup event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onkeyup() {
        return getEventHandlerProp("onkeyup");
    }

    /**
     * Set the onmousedown event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onmousedown(final Object handler) {
        setEventHandlerProp("onmousedown", handler);
    }

    /**
     * Get the onmousedown event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onmousedown() {
        return getEventHandlerProp("onmousedown");
    }

    /**
     * Set the onmousemove event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onmousemove(final Object handler) {
        setEventHandlerProp("onmousemove", handler);
    }

    /**
     * Get the onmousemove event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onmousemove() {
        return getEventHandlerProp("onmousemove");
    }

    /**
     * Set the onmouseout event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onmouseout(final Object handler) {
        setEventHandlerProp("onmouseout", handler);
    }

    /**
     * Get the onmouseout event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onmouseout() {
        return getEventHandlerProp("onmouseout");
    }

    /**
     * Set the onmouseover event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onmouseover(final Object handler) {
        setEventHandlerProp("onmouseover", handler);
    }

    /**
     * Get the onmouseover event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onmouseover() {
        return getEventHandlerProp("onmouseover");
    }

    /**
     * Set the onmouseup event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onmouseup(final Object handler) {
        setEventHandlerProp("onmouseup", handler);
    }

    /**
     * Get the onmouseup event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onmouseup() {
        return getEventHandlerProp("onmouseup");
    }

    /**
     * Set the oncontextmenu event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_oncontextmenu(final Object handler) {
        setEventHandlerProp("oncontextmenu", handler);
    }

    /**
     * Get the oncontextmenu event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_oncontextmenu() {
        return getEventHandlerProp("oncontextmenu");
    }

    /**
     * Set the onresize event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onresize(final Object handler) {
        setEventHandlerProp("onresize", handler);
    }

    /**
     * Get the onresize event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onresize() {
        return getEventHandlerProp("onresize");
    }

    /**
     * Returns this element's <tt>offsetHeight</tt>, which is the element height plus the element's padding
     * plus the element's border. This method returns a dummy value compatible with mouse event coordinates
     * during mouse events.
     * @return this element's <tt>offsetHeight</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534199.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     */
    public int jsxGet_offsetHeight() {
        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            // compute appropriate offsetHeight to make as if mouse event produced within this element
            return event.jsxGet_clientY() - getPosY() + 50;
        }
        else {
            return 1;
        }
    }

    /**
     * Returns this element's <tt>offsetWidth</tt>, which is the element width plus the element's padding
     * plus the element's border. This method returns a dummy value compatible with mouse event coordinates
     * during mouse events.
     * @return this element's <tt>offsetWidth</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534304.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     */
    public int jsxGet_offsetWidth() {
        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            // compute appropriate offsetwidth to make as if mouse event produced within this element
            return event.jsxGet_clientX() - getPosX() + 50;
        }
        else {
            return 1;
        }
    }

    /**
     * Returns <tt>true</tt> if this element's node is an ancestor of the specified event's target node.
     * @param event the event whose target node is to be checked
     * @return <tt>true</tt> if this element's node is an ancestor of the specified event's target node
     */
    private boolean isAncestorOfEventTarget(final MouseEvent event) {
        if (event == null) {
            return false;
        }
        final HTMLElement target = (HTMLElement) event.jsxGet_target();
        return getHtmlElementOrDie().isAncestorOf(target.getHtmlElementOrDie());
    }

    /**
     * Gets the x position of the element. The value returned doesn't need to be "correct" as it
     * just needs to be compatible with mouse event coordinates.
     *
     * @return the x position
     */
    int getPosX() {
        int cumulativeOffset = 0;
        HTMLElement element = this;
        while (element != null) {
            cumulativeOffset += element.jsxGet_offsetLeft();
            element = (HTMLElement) element.jsxGet_offsetParent();
        }
        return cumulativeOffset;
    }

    /**
     * Gets the y position of the element. The value returned doesn't need to be "correct" as it
     * just needs to be compatible with mouse event coordinates.
     * @return the y position
     */
    int getPosY() {
        int cumulativeOffset = 0;
        HTMLElement element = this;
        while (element != null) {
            cumulativeOffset += element.jsxGet_offsetTop();
            element = (HTMLElement) element.jsxGet_offsetParent();
        }
        return cumulativeOffset;
    }

    /**
     * Returns this element's <tt>offsetLeft</tt>, which is the calculated left position of this
     * element relative to the <tt>offsetParent</tt>.
     * @return this element's <tt>offsetLeft</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534200.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     */
    public int jsxGet_offsetLeft() {
        return 1;
    }

    /**
     * Returns this element's <tt>offsetTop</tt>, which is the calculated top position of this
     * element relative to the <tt>offsetParent</tt>.
     * @return this element's <tt>offsetTop</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534303.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     */
    public int jsxGet_offsetTop() {
        return 1;
    }

    /**
     * Returns this element's <tt>offsetParent</tt>. The <tt>offsetLeft</tt> and <tt>offsetTop</tt>
     * attributes are relative to the <tt>offsetParent</tt>.
     * @return this element's <tt>offsetParent</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534302.aspx">MSDN Documentation</a>
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_el_ref20.html">Gecko DOM Reference</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://www.w3.org/TR/REC-CSS2/box.html">Box Model</a>
     */
    public Object jsxGet_offsetParent() {
        DomNode currentElement = getHtmlElementOrDie();
        Object offsetParent = null;
        while (currentElement != null) {
            final DomNode parentNode = currentElement.getParentDomNode();
            // According to the Microsoft and Mozilla documentation, and from experimentation
            // in the IE and Firefox browsers, the offsetParent is the container
            // (<td>, <table>, <body>) nearest to the node
            if ((parentNode instanceof HtmlTableDataCell)
                || (parentNode instanceof HtmlTable)
                || (parentNode instanceof HtmlBody)) {
                offsetParent = parentNode.getScriptObject();
                break;
            }
            currentElement = currentElement.getParentDomNode();
        }
        return offsetParent;
    }

    /**
     * Just for debug purposes.
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "HTMLElement for " + getHtmlElementOrNull();
    }

    /**
     * Get the scrollTop for this element.
     * @return a dummy value (default is 0)
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/scrollTop.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_scrollTop() {
        return scrollTop_;
    }

    /**
     * Set the scrollTop for this element.
     * @param scroll the new value
     */
    public void jsxSet_scrollTop(final int scroll) {
        scrollTop_ = scroll;
    }

    /**
     * Get the scrollLeft for this element.
     * @return a dummy value (default is 0)
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/scrollLeft.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_scrollLeft() {
        return scrollLeft_;
    }

    /**
     * Set the scrollLeft for this element.
     * @param scroll the new value
     */
    public void jsxSet_scrollLeft(final int scroll) {
        scrollLeft_ = scroll;
    }

    /**
     * Get the scrollHeight for this element.
     * @return a dummy value of 10
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/scrollHeight.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_scrollHeight() {
        return 10;
    }

    /**
     * Get the scrollWidth for this element.
     * @return a dummy value of 10
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/scrollWidth.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_scrollWidth() {
        return 10;
    }

    /**
     * Get the JavaScript property "parentElement".
     * <p>It is identical to {@link #jsxGet_parentNode()}
     * with the exception of <tt>HTML</tt>, which has a <tt>null</tt> parent element.
     * @return The parent element
     * @see #jsxGet_parentNode()
     */
    public Object jsxGet_parentElement() {
        if ("html".equalsIgnoreCase(getDomNodeOrDie().getNodeName())) {
            return null;
        }
        else {
            return jsxGet_parentNode();
        }
    }

    /**
     * Implement the scrollIntoView() javascript function but don't actually do
     * anything. The requirement
     * is just to prevent scripts that call that method from failing
     */
    public void jsxFunction_scrollIntoView() { }
    
    /**
     * Retrieves an object that specifies the bounds of a collection of TextRectangle objects.
     * @return an object that specifies the bounds of a collection of TextRectangle objects.
     */
    public TextRectangle jsxFunction_getBoundingClientRect() {
        final TextRectangle textRectangle = new TextRectangle();
        textRectangle.setParentScope(getWindow());
        textRectangle.setPrototype(getPrototype(textRectangle.getClass()));
        return textRectangle;
    }

    /**
     * Retrieves a collection of rectangles that describes the layout of the contents of an object
     * or range within the client. Each rectangle describes a single line.
     * @return a collection of rectangles that describes the layout of the contents.
     */
    public Object jsxFunction_getClientRects() {
        return new NativeArray(0);
    }

    /**
     * Sets an expression for the specified HTMLElement.
     *
     * @param propertyName Specifies the name of the property to which expression is added.
     * @param expression specifies any valid script statement without quotations or semicolons.
     *        This string can include references to other properties on the current page.
     *        Array references are not allowed on object properties included in this script.
     * @param language specified the language used.
     */
    public void jsxFunction_setExpression(final String propertyName, final String expression, final String language) {
        //empty implementation
    }
    
    /**
     * Removes the expression from the specified property.
     *
     * @param propertyName Specifies the name of the property from which to remove an expression.
     * @return true if the expression was successfully removed.
     */
    public boolean jsxFunction_removeExpression(final String propertyName) {
        return true;
    }

    /**
     * Retrieves an auto-generated, unique identifier for the object.
     * <b>Note</b> The unique ID generated is not guaranteed to be the same every time the page is loaded.
     * @return an auto-generated, unique identifier for the object.
     */
    public String jsxGet_uniqueID() {
        if (uniqueID_ == null) {
            uniqueID_ = "ms__id" + UniqueID_Counter_++;
        }
        return uniqueID_;
    }

    /**
     * Dispatches an event into the event system (standards-conformant browsers only). See
     * <a href="http://developer.mozilla.org/en/docs/DOM:element.dispatchEvent">the Gecko
     * DOM reference</a> for more information.
     *
     * @param event the event to be dispatched
     * @return <tt>false</tt> if at least one of the event handlers which handled the event
     *         called <tt>preventDefault</tt>; <tt>true</tt> otherwise
     */
    public boolean jsxFunction_dispatchEvent(final Event event) {
        final HtmlElement element = getHtmlElementOrDie();
        if (event instanceof MouseEvent && element instanceof ClickableElement) {
            if (event.jsxGet_type().equals(MouseEvent.TYPE_CLICK)) {
                try {
                    ((ClickableElement) element).click(event);
                }
                catch (final IOException e) {
                    throw Context.reportRuntimeError("Error calling click(): " + e.getMessage());
                }
            }
            else if (event.jsxGet_type().equals(MouseEvent.TYPE_DBL_CLICK)) {
                try {
                    ((ClickableElement) element).dblClick(event.jsxGet_shiftKey(), event.jsxGet_ctrlKey(),
                            event.jsxGet_altKey());
                }
                catch (final IOException e) {
                    throw Context.reportRuntimeError("Error calling dblClick(): " + e.getMessage());
                }
            }
            else {
                fireEvent(event);
            }
        }
        else {
            fireEvent(event);
        }
        return !event.isPreventDefault();
    }

    /**
     * Fires a specified event on this element (IE only). See the
     * <a href="http://msdn2.microsoft.com/en-us/library/ms536423.aspx">MSDN documentation</a>
     * for more information.
     * @param cx the JavaScript context
     * @param thisObj the element instance on which this method was invoked
     * @param args contains the event type as a string, and an optional event template
     * @param f the function being invoked
     * @return <tt>true</tt> if the event fired successfully, <tt>false</tt> if it was cancelled
     */
    public static ScriptResult jsxFunction_fireEvent(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function f) {

        final HTMLElement me = (HTMLElement) thisObj;

        // Extract the function arguments.
        final String type = (String) args[0];
        final Event template;
        if (args.length > 1) {
            template = (Event) args[1];
        }
        else {
            template = null;
        }

        // Create the event, whose class will depend on the type specified.
        final Event event;
        final String cleanedType = StringUtils.removeStart(type.toLowerCase(), "on");
        if (MouseEvent.isMouseEvent(cleanedType)) {
            event = new MouseEvent();
            event.setPrototype(me.getPrototype(MouseEvent.class));
        }
        else {
            event = new Event();
            event.setPrototype(me.getPrototype(Event.class));
        }
        event.setParentScope(me.getWindow());

        // Initialize the event using the template, if provided.
        if (template != null) {
            event.copyPropertiesFrom(template);
        }

        // These four properties have predefined values, independent of the template.
        event.jsxSet_cancelBubble(false);
        event.jsxSet_returnValue(Boolean.TRUE);
        event.jsxSet_srcElement(me);
        event.setEventType(cleanedType);

        return me.fireEvent(event);
    }

    /**
     * Return the html element that corresponds to this javascript object or throw an exception
     * if one cannot be found.
     * @return The html element
     * @exception IllegalStateException If the html element could not be found.
     */
    public final HtmlElement getHtmlElementOrDie() throws IllegalStateException {
        return (HtmlElement) getDomNodeOrDie();
    }

    /**
     * Return the html element that corresponds to this javascript object
     * or null if an element hasn't been set.
     * @return The html element or null
     */
    public final HtmlElement getHtmlElementOrNull() {
        return (HtmlElement) getDomNodeOrNull();
    }
    
    /**
     * Remove focus from this element.
     */
    public void jsxFunction_blur() {
        final HtmlElement element = (HtmlElement) getDomNodeOrDie();
        element.blur();
    }

    /**
     * Set the focus to this element.
     */
    public void jsxFunction_focus() {
        final HtmlElement element = (HtmlElement) getDomNodeOrDie();
        element.focus();
    }
}
