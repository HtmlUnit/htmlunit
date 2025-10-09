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
package org.htmlunit.javascript.host.dom;

import static org.htmlunit.BrowserVersionFeatures.EVENT_ONANIMATION_DOCUMENT_CREATE_NOT_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.EVENT_ONCLOSE_DOCUMENT_CREATE_NOT_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.EVENT_ONPOPSTATE_DOCUMENT_CREATE_NOT_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.EVENT_TYPE_MUTATIONEVENT;
import static org.htmlunit.BrowserVersionFeatures.EVENT_TYPE_TEXTEVENT;
import static org.htmlunit.BrowserVersionFeatures.EVENT_TYPE_WHEELEVENT;
import static org.htmlunit.BrowserVersionFeatures.JS_DOCUMENT_EVALUATE_RECREATES_RESULT;
import static org.htmlunit.BrowserVersionFeatures.JS_DOCUMENT_SELECTION_RANGE_COUNT;
import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.HttpHeader;
import org.htmlunit.Page;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebResponse;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Callable;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.NativeFunction;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.cssparser.parser.CSSException;
import org.htmlunit.html.DomComment;
import org.htmlunit.html.DomDocumentFragment;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomText;
import org.htmlunit.html.FrameWindow;
import org.htmlunit.html.Html;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlArea;
import org.htmlunit.html.HtmlAttributeChangeEvent;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlEmbed;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlFrameSet;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlRb;
import org.htmlunit.html.HtmlRp;
import org.htmlunit.html.HtmlRt;
import org.htmlunit.html.HtmlRtc;
import org.htmlunit.html.HtmlScript;
import org.htmlunit.html.HtmlSvg;
import org.htmlunit.html.HtmlUnknownElement;
import org.htmlunit.html.UnknownElementFactory;
import org.htmlunit.html.impl.SimpleRange;
import org.htmlunit.http.HttpUtils;
import org.htmlunit.httpclient.HtmlUnitBrowserCompatCookieSpec;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.Element;
import org.htmlunit.javascript.host.FontFaceSet;
import org.htmlunit.javascript.host.Location;
import org.htmlunit.javascript.host.NativeFunctionPrefixResolver;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.animations.AnimationEvent;
import org.htmlunit.javascript.host.css.StyleSheetList;
import org.htmlunit.javascript.host.dom.AbstractList.EffectOnCache;
import org.htmlunit.javascript.host.event.BeforeUnloadEvent;
import org.htmlunit.javascript.host.event.CloseEvent;
import org.htmlunit.javascript.host.event.CompositionEvent;
import org.htmlunit.javascript.host.event.CustomEvent;
import org.htmlunit.javascript.host.event.DragEvent;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.FocusEvent;
import org.htmlunit.javascript.host.event.HashChangeEvent;
import org.htmlunit.javascript.host.event.KeyboardEvent;
import org.htmlunit.javascript.host.event.MessageEvent;
import org.htmlunit.javascript.host.event.MouseEvent;
import org.htmlunit.javascript.host.event.MutationEvent;
import org.htmlunit.javascript.host.event.PointerEvent;
import org.htmlunit.javascript.host.event.PopStateEvent;
import org.htmlunit.javascript.host.event.ProgressEvent;
import org.htmlunit.javascript.host.event.TextEvent;
import org.htmlunit.javascript.host.event.UIEvent;
import org.htmlunit.javascript.host.event.WheelEvent;
import org.htmlunit.javascript.host.file.Blob;
import org.htmlunit.javascript.host.html.HTMLAllCollection;
import org.htmlunit.javascript.host.html.HTMLBodyElement;
import org.htmlunit.javascript.host.html.HTMLCollection;
import org.htmlunit.javascript.host.html.HTMLElement;
import org.htmlunit.javascript.host.html.HTMLFrameSetElement;
import org.htmlunit.util.Cookie;
import org.htmlunit.util.StringUtils;
import org.htmlunit.util.UrlUtils;
import org.htmlunit.xpath.xml.utils.PrefixResolver;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ProcessingInstruction;

/**
 * A JavaScript object for {@code Document}.
 *
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Chen Jun
 * @author Christian Sell
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Michael Ottati
 * @author George Murnock
 * @author Ahmed Ashour
 * @author Rob Di Marco
 * @author Ronald Brill
 * @author Chuck Dumont
 * @author Frank Danek
 * @author Madis Pärn
 * @author Lai Quang Duong
 * @author Sven Strickroth
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms531073.aspx">MSDN documentation</a>
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-7068919">W3C Dom Level 1</a>
 */
@JsxClass
public class Document extends Node {

    private static final Log LOG = LogFactory.getLog(Document.class);

    /**
     * See <a href="https://developer.mozilla.org/en/Rich-Text_Editing_in_Mozilla#Executing_Commands">
     *     Executing Commands</a>
     */
    private static final Set<String> EXECUTE_CMDS_FF = new HashSet<>();
    private static final Set<String> EXECUTE_CMDS_CHROME = new HashSet<>();
    /** The formatter to use for the <code>lastModified</code> attribute. */
    private static final DateTimeFormatter LAST_MODIFIED_DATE_FORMATTER
                                            = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

    /** Contains all supported DOM level 2 events. */
    private static final Map<String, Class<? extends Event>> SUPPORTED_DOM2_EVENT_TYPE_MAP;
    /** Contains all supported DOM level 3 events. DOM level 2 events are not included. */
    private static final Map<String, Class<? extends Event>> SUPPORTED_DOM3_EVENT_TYPE_MAP;
    /** Contains all supported vendor specific events. */
    private static final Map<String, Class<? extends Event>> SUPPORTED_VENDOR_EVENT_TYPE_MAP;

    /*
      Initializes the supported event type map.
      Map<String, Class> which maps strings a caller may use when calling into
      {@link #createEvent(String)} to the associated event class. To support a new
      event creation type, the event type and associated class need to be added into this map in
      the static initializer. The map is unmodifiable. Any class that is a value in this map MUST
      have a no-arg constructor.
     */
    static {
        final Map<String, Class<? extends Event>> dom2EventMap = new HashMap<>();
        dom2EventMap.put("HTMLEvents", Event.class);
        dom2EventMap.put("MouseEvents", MouseEvent.class);
        dom2EventMap.put("MutationEvents", MutationEvent.class);
        dom2EventMap.put("UIEvents", UIEvent.class);
        SUPPORTED_DOM2_EVENT_TYPE_MAP = Collections.unmodifiableMap(dom2EventMap);

        final Map<String, Class<? extends Event>> dom3EventMap = new HashMap<>();
        dom3EventMap.put("Event", Event.class);
        dom3EventMap.put("KeyboardEvent", KeyboardEvent.class);
        dom3EventMap.put("MouseEvent", MouseEvent.class);
        dom3EventMap.put("MessageEvent", MessageEvent.class);
        dom3EventMap.put("MutationEvent", MutationEvent.class);
        dom3EventMap.put("UIEvent", UIEvent.class);
        dom3EventMap.put("CustomEvent", CustomEvent.class);
        dom3EventMap.put("CloseEvent", CloseEvent.class);
        dom3EventMap.put("CompositionEvent", CompositionEvent.class);
        dom3EventMap.put("DragEvent", DragEvent.class);
        dom3EventMap.put("TextEvent", TextEvent.class);
        SUPPORTED_DOM3_EVENT_TYPE_MAP = Collections.unmodifiableMap(dom3EventMap);

        final Map<String, Class<? extends Event>> additionalEventMap = new HashMap<>();
        additionalEventMap.put("BeforeUnloadEvent", BeforeUnloadEvent.class);
        additionalEventMap.put("Events", Event.class);
        additionalEventMap.put("HashChangeEvent", HashChangeEvent.class);
        additionalEventMap.put("KeyEvents", KeyboardEvent.class);
        additionalEventMap.put("PointerEvent", PointerEvent.class);
        additionalEventMap.put("PopStateEvent", PopStateEvent.class);
        additionalEventMap.put("ProgressEvent", ProgressEvent.class);
        additionalEventMap.put("FocusEvent", FocusEvent.class);
        additionalEventMap.put("WheelEvent", WheelEvent.class);
        additionalEventMap.put("AnimationEvent", AnimationEvent.class);
        SUPPORTED_VENDOR_EVENT_TYPE_MAP = Collections.unmodifiableMap(additionalEventMap);
    }

    private Window window_;
    private DOMImplementation implementation_;
    private String designMode_;
    private String compatMode_;
    private int documentMode_ = -1;
    private String domain_;
    private String lastModified_;
    private ScriptableObject currentScript_;
    private transient FontFaceSet fonts_;
    private transient StyleSheetList styleSheetList_;

    private final Map<String, Blob> blobUrl2Blobs_ = new HashMap<>();

    static {
        // commands
        String[] cmds = {
            "BackColor", "BackgroundImageCache" /* Undocumented */,
            "Bold",
            "CreateLink", "Delete",
            "FontName", "FontSize", "ForeColor", "FormatBlock",
            "Indent", "InsertHorizontalRule", "InsertImage",
            "InsertOrderedList", "InsertParagraph", "InsertUnorderedList",
            "Italic", "JustifyCenter", "JustifyFull", "JustifyLeft", "JustifyNone",
            "JustifyRight",
            "Outdent",
            "Print",
            "Redo", "RemoveFormat",
            "SelectAll", "StrikeThrough", "Subscript", "Superscript",
            "Underline", "Undo", "Unlink", "Unselect"
        };
        for (final String cmd : cmds) {
            if (!"Bold".equals(cmd)) {
                EXECUTE_CMDS_CHROME.add(cmd.toLowerCase(Locale.ROOT));
            }
        }

        cmds = new String[] {
            "backColor", "bold", "contentReadOnly", "copy", "createLink", "cut", "decreaseFontSize", "delete",
            "fontName", "fontSize", "foreColor", "formatBlock", "heading", "hiliteColor", "increaseFontSize",
            "indent", "insertHorizontalRule", "insertHTML", "insertImage", "insertOrderedList", "insertUnorderedList",
            "insertParagraph", "italic",
            "justifyCenter", "JustifyFull", "justifyLeft", "justifyRight", "outdent", "paste", "redo",
            "removeFormat", "selectAll", "strikeThrough", "subscript", "superscript", "underline", "undo", "unlink",
            "useCSS", "styleWithCSS"
        };
        for (final String cmd : cmds) {
            EXECUTE_CMDS_FF.add(cmd.toLowerCase(Locale.ROOT));
            if (!"bold".equals(cmd)) {
                EXECUTE_CMDS_CHROME.add(cmd.toLowerCase(Locale.ROOT));
            }
        }
    }

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * Sets the Window JavaScript object that encloses this document.
     * @param window the Window JavaScript object that encloses this document
     */
    public void setWindow(final Window window) {
        window_ = window;
    }

    /**
     * Returns the value of the {@code location} property.
     * @return the value of the {@code location} property
     */
    @JsxGetter
    public Location getLocation() {
        if (window_ == null) {
            return null;
        }
        return window_.getLocation();
    }

    /**
     * Sets the value of the {@code location} property. The location's default property is "href",
     * so setting "document.location='http://www.sf.net'" is equivalent to setting
     * "document.location.href='http://www.sf.net'".
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms535866.aspx">MSDN documentation</a>
     * @param location the location to navigate to
     * @throws IOException when location loading fails
     */
    @JsxSetter
    public void setLocation(final String location) throws IOException {
        window_.setLocation(location);
    }

    /**
     * Returns the value of the {@code referrer} property.
     * @return the value of the {@code referrer} property
     */
    @JsxGetter
    public String getReferrer() {
        String referrer = "";
        final WebResponse webResponse = getPage().getWebResponse();
        if (webResponse != null) {
            referrer = webResponse.getWebRequest().getAdditionalHeaders().get(HttpHeader.REFERER);
            if (referrer == null) {
                referrer = "";
            }
        }
        return referrer;
    }

    /**
     * Gets the JavaScript property {@code documentElement} for the document.
     * @return the root node for the document
     */
    @JsxGetter
    public Element getDocumentElement() {
        final Object documentElement = getPage().getDocumentElement();
        if (documentElement == null) {
            // for instance with an XML document with parsing error
            return null;
        }
        return (Element) getScriptableFor(documentElement);
    }

    /**
     * Gets the JavaScript property {@code rootElement}.
     * @return the root node for the document
     */
    @JsxGetter
    public Element getRootElement() {
        return null;
    }

    /**
     * Gets the JavaScript property {@code doctype} for the document.
     * @return the DocumentType of the document
     */
    @JsxGetter
    public HtmlUnitScriptable getDoctype() {
        final Object documentType = getPage().getDoctype();
        if (documentType == null) {
            return null;
        }
        return getScriptableFor(documentType);
    }

    /**
     * Returns a value which indicates whether or not the document can be edited.
     * @return a value which indicates whether or not the document can be edited
     */
    @JsxGetter
    public String getDesignMode() {
        if (designMode_ == null) {
            designMode_ = "off";
        }
        return designMode_;
    }

    /**
     * Sets a value which indicates whether or not the document can be edited.
     * @param mode a value which indicates whether or not the document can be edited
     */
    @JsxSetter
    public void setDesignMode(final String mode) {
        if ("on".equalsIgnoreCase(mode)) {
            designMode_ = "on";
            final SgmlPage page = getPage();
            if (page != null && page.isHtmlPage()
                    && getBrowserVersion().hasFeature(JS_DOCUMENT_SELECTION_RANGE_COUNT)) {
                final HtmlPage htmlPage = (HtmlPage) page;
                final DomNode child = htmlPage.getBody().getFirstChild();
                final DomNode rangeNode = child == null ? htmlPage.getBody() : child;
                htmlPage.setSelectionRange(new SimpleRange(rangeNode, 0));
            }
        }
        else if ("off".equalsIgnoreCase(mode)) {
            designMode_ = "off";
        }
    }

    /**
     * Returns the page that this document is modeling.
     * @return the page that this document is modeling
     */
    public SgmlPage getPage() {
        return (SgmlPage) getDomNodeOrDie();
    }

    /**
     * Gets the window in which this document is contained.
     * @return the window
     */
    @JsxGetter
    public Object getDefaultView() {
        return getWindow();
    }

    /**
     * Creates a new document fragment.
     * @return a newly created document fragment
     */
    @JsxFunction
    public HtmlUnitScriptable createDocumentFragment() {
        final DomDocumentFragment fragment = getDomNodeOrDie().getPage().createDocumentFragment();
        final DocumentFragment node = new DocumentFragment();
        node.setParentScope(getParentScope());
        node.setPrototype(getPrototype(node.getClass()));
        node.setDomNode(fragment);
        return getScriptableFor(fragment);
    }

    /**
     * Creates a new HTML attribute with the specified name.
     *
     * @param attributeName the name of the attribute to create
     * @return an attribute with the specified name
     */
    @JsxFunction
    public Attr createAttribute(final String attributeName) {
        return getPage().createAttribute(attributeName).getScriptableObject();
    }

    /**
     * Imports a node from another document to this document.
     * The source node is not altered or removed from the original document;
     * this method creates a new copy of the source node.
     *
     * @param importedNode the node to import
     * @param deep Whether to recursively import the subtree under the specified node; or not
     * @return the imported node that belongs to this Document
     */
    @JsxFunction
    public HtmlUnitScriptable importNode(final Node importedNode, final boolean deep) {
        DomNode domNode = importedNode.getDomNodeOrDie();
        domNode = domNode.cloneNode(deep);
        domNode.processImportNode(this);
        for (final DomNode childNode : domNode.getDescendants()) {
            childNode.processImportNode(this);
        }
        return domNode.getScriptableObject();
    }

    /**
     * Adopts a node from an external document.
     * The node and its subtree are removed from the document it's in (if any),
     * and its ownerDocument is changed to the current document.
     * The node can then be inserted into the current document.
     *
     * @param externalNode the node from another document to be adopted
     * @return the adopted node that can be used in the current document
     */
    @JsxFunction
    public HtmlUnitScriptable adoptNode(final Node externalNode) {
        externalNode.remove();
        return importNode(externalNode, true);
    }

    /**
     * Returns the implementation object of the current document.
     * @return implementation-specific object
     */
    @JsxGetter
    public DOMImplementation getImplementation() {
        if (implementation_ == null) {
            implementation_ = new DOMImplementation();
            implementation_.setParentScope(getWindow());
            implementation_.setPrototype(getPrototype(implementation_.getClass()));
        }
        return implementation_;
    }

    /**
     * Adapts any DOM node to resolve namespaces so that an XPath expression can be easily
     * evaluated relative to the context of the node where it appeared within the document.
     * @param nodeResolver the node to be used as a context for namespace resolution
     * @return an XPathNSResolver which resolves namespaces with respect to the definitions
     *         in scope for a specified node
     */
    @JsxFunction
    public XPathNSResolver createNSResolver(final Node nodeResolver) {
        final XPathNSResolver resolver = new XPathNSResolver();
        resolver.setElement(nodeResolver);
        resolver.setParentScope(getWindow());
        resolver.setPrototype(getPrototype(resolver.getClass()));
        return resolver;
    }

    /**
     * Create a new DOM text node with the given data.
     *
     * @param newData the string value for the text node
     * @return the new text node or NOT_FOUND if there is an error
     */
    @JsxFunction
    public HtmlUnitScriptable createTextNode(final String newData) {
        final DomNode domNode = new DomText(getDomNodeOrDie().getPage(), newData);
        return makeScriptableFor(domNode);
    }

    /**
     * Creates a new Comment.
     * @param comment the comment text
     * @return the new Comment
     */
    @JsxFunction
    public HtmlUnitScriptable createComment(final String comment) {
        final DomNode domNode = new DomComment(getDomNodeOrDie().getPage(), comment);
        return getScriptableFor(domNode);
    }

    /**
     * Evaluates an XPath expression string and returns a result of the specified type if possible.
     * @param expression the XPath expression string to be parsed and evaluated
     * @param contextNode the context node for the evaluation of this XPath expression
     * @param resolver the resolver permits translation of all prefixes, including the XML namespace prefix,
     *        within the XPath expression into appropriate namespace URIs.
     * @param type If a specific type is specified, then the result will be returned as the corresponding type
     * @param result the result object which may be reused and returned by this method
     * @return the result of the evaluation of the XPath expression
     */
    @JsxFunction
    public XPathResult evaluate(final String expression, final Node contextNode,
            final Object resolver, final int type, final Object result) {
        XPathResult xPathResult = null;
        if (result instanceof XPathResult) {
            xPathResult = (XPathResult) result;

            if (getBrowserVersion().hasFeature(JS_DOCUMENT_EVALUATE_RECREATES_RESULT)) {
                xPathResult = new XPathResult();
                xPathResult.setParentScope(getParentScope());
                xPathResult.setPrototype(getPrototype(xPathResult.getClass()));
            }
        }
        else if (result == null
                || JavaScriptEngine.isUndefined(result)
                || result instanceof ScriptableObject) {
            xPathResult = new XPathResult();
            xPathResult.setParentScope(getParentScope());
            xPathResult.setPrototype(getPrototype(xPathResult.getClass()));
        }
        else {
            throw JavaScriptEngine.typeError("Argument 5 of Document.evaluate has to be an XPathResult or null.");
        }

        try {
            PrefixResolver prefixResolver = null;
            if (resolver instanceof NativeFunction) {
                prefixResolver = new NativeFunctionPrefixResolver(
                                            (NativeFunction) resolver, contextNode.getParentScope());
            }
            else if (resolver instanceof PrefixResolver) {
                prefixResolver = (PrefixResolver) resolver;
            }
            xPathResult.init(contextNode.getDomNodeOrDie().getByXPath(expression, prefixResolver), type);
            return xPathResult;
        }
        catch (final Exception e) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "Failed to execute 'evaluate': " + e.getMessage(),
                    org.htmlunit.javascript.host.dom.DOMException.SYNTAX_ERR);
        }
    }

    /**
     * Creates a new element with the given tag name.
     *
     * @param tagName the tag name
     * @return the new HTML element, or NOT_FOUND if the tag is not supported
     */
    @JsxFunction
    public HtmlUnitScriptable createElement(final Object tagName) {
        if (tagName == null || JavaScriptEngine.isUndefined(tagName)) {
            final org.w3c.dom.Node element = getPage().createElement("unknown");
            ((HtmlUnknownElement) element).markAsCreatedByJavascript();
            return getScriptableFor(element);
        }

        // https://dom.spec.whatwg.org/#dom-document-createelement
        // NameStartChar ::= ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D]
        //                       | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF]
        //                       | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
        // NameChar      ::= NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
        // Name          ::= NameStartChar (NameChar)*

        // but I have no idea what the browsers are doing
        // the following code is a wild guess that might be good enough for the moment
        final String tagNameString = JavaScriptEngine.toString(tagName);
        if (tagNameString.length() > 0) {
            final int firstChar = tagNameString.charAt(0);
            if (!(isLetter(firstChar)
                    || ':' == firstChar
                    || '_' == firstChar)) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("createElement: Provided string '" + tagNameString + "' contains an invalid character");
                }
                throw JavaScriptEngine.asJavaScriptException(
                        getWindow(),
                        "createElement: Provided string '" + tagNameString + "' contains an invalid character",
                        org.htmlunit.javascript.host.dom.DOMException.INVALID_CHARACTER_ERR);
            }
            final int length = tagNameString.length();
            for (int i = 1; i < length; i++) {
                final int c = tagNameString.charAt(i);
                if (!(Character.isLetterOrDigit(c)
                        || ':' == c
                        || '_' == c
                        || '-' == c
                        || '.' == c)) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("createElement: Provided string '"
                                    + tagNameString + "' contains an invalid character");
                    }
                    throw JavaScriptEngine.asJavaScriptException(
                            getWindow(),
                            "createElement: Provided string '" + tagNameString
                                + "' contains an invalid character",
                            org.htmlunit.javascript.host.dom.DOMException.INVALID_CHARACTER_ERR);
                }
            }
        }

        org.w3c.dom.Node element = getPage().createElement(tagNameString);

        if (element instanceof HtmlImage) {
            ((HtmlImage) element).markAsCreatedByJavascript();
        }
        else if (element instanceof HtmlRb) {
            ((HtmlRb) element).markAsCreatedByJavascript();
        }
        else if (element instanceof HtmlRp) {
            ((HtmlRp) element).markAsCreatedByJavascript();
        }
        else if (element instanceof HtmlRt) {
            ((HtmlRt) element).markAsCreatedByJavascript();
        }
        else if (element instanceof HtmlRtc) {
            ((HtmlRtc) element).markAsCreatedByJavascript();
        }
        else if (element instanceof HtmlUnknownElement) {
            ((HtmlUnknownElement) element).markAsCreatedByJavascript();
        }
        else if (element instanceof HtmlSvg) {
            element = UnknownElementFactory.INSTANCE.createElementNS(getPage(), "", "svg", null);
            ((HtmlUnknownElement) element).markAsCreatedByJavascript();
        }
        final HtmlUnitScriptable jsElement = getScriptableFor(element);

        if (jsElement == NOT_FOUND) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("createElement(" + tagName
                    + ") cannot return a result as there isn't a JavaScript object for the element "
                    + element.getClass().getName());
            }
        }
        return jsElement;
    }

    // our version of the Character.isLetter() without MODIFIER_LETTER
    private static boolean isLetter(final int codePoint) {
        return ((((1 << Character.UPPERCASE_LETTER)
                    | (1 << Character.LOWERCASE_LETTER)
                    | (1 << Character.TITLECASE_LETTER)
                    | (1 << Character.OTHER_LETTER)
                ) >> Character.getType(codePoint)) & 1) != 0;
    }

    /**
     * Creates a new HTML element with the given tag name, and name.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @return the new HTML element, or NOT_FOUND if the tag is not supported
     */
    @JsxFunction
    public HtmlUnitScriptable createElementNS(final String namespaceURI, final String qualifiedName) {
        if ("http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul".equals(namespaceURI)) {
            throw JavaScriptEngine.typeError("XUL not available");
        }

        final org.w3c.dom.Element element;
        if (Html.XHTML_NAMESPACE.equals(namespaceURI)
                || Html.SVG_NAMESPACE.equals(namespaceURI)) {
            element = getPage().createElementNS(namespaceURI, qualifiedName);
        }
        else {
            element = new DomElement(namespaceURI, qualifiedName, getPage(), null);
        }
        return getScriptableFor(element);
    }

    /**
     * Returns all the descendant elements with the specified tag name.
     * @param tagName the name to search for
     * @return all the descendant elements with the specified tag name
     */
    @JsxFunction
    public HTMLCollection getElementsByTagName(final String tagName) {
        final HTMLCollection collection = new HTMLCollection(getDomNodeOrDie(), false);

        if (StringUtils.equalsChar('*', tagName)) {
            collection.setIsMatchingPredicate((Predicate<DomNode> & Serializable) node -> true);
        }
        else {
            collection.setIsMatchingPredicate(
                    (Predicate<DomNode> & Serializable)
                    node -> tagName.equalsIgnoreCase(node.getNodeName()));
        }

        return collection;
    }

    /**
     * Returns a list of elements with the given tag name belonging to the given namespace.
     * @param namespaceURI the namespace URI of elements to look for
     * @param localName is either the local name of elements to look for or the special value "*",
     *                  which matches all elements.
     * @return a live NodeList of found elements in the order they appear in the tree
     */
    @JsxFunction
    public HTMLCollection getElementsByTagNameNS(final Object namespaceURI, final String localName) {
        final HTMLCollection elements = new HTMLCollection(getDomNodeOrDie(), false);
        elements.setIsMatchingPredicate(
                (Predicate<DomNode> & Serializable)
                node -> localName.equals(node.getLocalName()));
        return elements;
    }

    /**
     * Returns the value of the {@code activeElement} property.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533065.aspx">MSDN documentation</a>
     * @return the value of the {@code activeElement} property
     */
    @JsxGetter
    public Object getActiveElement() {
        return null;
    }

    /**
     * Returns the character encoding of the current document.
     * @return the character encoding of the current document
     */
    @JsxGetter
    public String getCharacterSet() {
        if (!(getPage() instanceof HtmlPage)) {
            // TODO: implement XmlPage.getCharset
            return "";
        }
        return getPage().getCharset().name();
    }

    /**
     * Retrieves the character set used to encode the document.
     * @return the character set used to encode the document
     */
    @JsxGetter
    public String getCharset() {
        if (!(getPage() instanceof HtmlPage)) {
            // TODO: implement XmlPage.getCharset
            return "";
        }
        return getPage().getCharset().name();
    }

    /**
     * Returns the value of the JavaScript property {@code anchors}.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms537435.aspx">MSDN documentation</a>
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_doc_ref4.html#1024543">
     *     Gecko DOM reference</a>
     * @return the value of this property
     */
    @JsxGetter
    public HTMLCollection getAnchors() {
        final HTMLCollection anchors = new HTMLCollection(getDomNodeOrDie(), true);

        anchors.setIsMatchingPredicate(
                (Predicate<DomNode> & Serializable)
                node -> {
                    if (!(node instanceof HtmlAnchor)) {
                        return false;
                    }
                    final HtmlAnchor anchor = (HtmlAnchor) node;
                    return anchor.hasAttribute(DomElement.NAME_ATTRIBUTE);
                });

        anchors.setEffectOnCacheFunction(
                (java.util.function.Function<HtmlAttributeChangeEvent, EffectOnCache> & Serializable)
                    event -> {
                        if (DomElement.NAME_ATTRIBUTE.equals(event.getName())
                                || DomElement.ID_ATTRIBUTE.equals(event.getName())) {
                            return EffectOnCache.RESET;
                        }
                        return EffectOnCache.NONE;
                    });

        return anchors;
    }

    /**
     * Returns the value of the JavaScript property {@code applets}.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms537436.aspx">
     *     MSDN documentation</a>
     * @see <a href="https://developer.mozilla.org/En/DOM:document.applets">
     *     Gecko DOM reference</a>
     * @return the value of this property
     */
    @JsxGetter
    public HTMLCollection getApplets() {
        return new HTMLCollection(getDomNodeOrDie(), false);
    }

    /**
     * Returns this document's {@code body} element.
     * @return this document's {@code body} element
     */
    @JsxGetter
    public HTMLElement getBody() {
        final Page page = getPage();
        if (page instanceof HtmlPage) {
            final HtmlPage htmlPage = (HtmlPage) page;
            final HtmlElement body = htmlPage.getBody();
            if (body != null) {
                return body.getScriptableObject();
            }

            // strange but this returns the frameset element
            final DomElement doc = htmlPage.getDocumentElement();
            if (doc != null) {
                for (final DomNode node : doc.getChildren()) {
                    if (node instanceof HtmlFrameSet) {
                        return (HTMLElement) node.getScriptableObject();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Sets the {@code body} element of the document.
     * @param htmlElement the new html element
     */
    @JsxSetter
    public void setBody(final HTMLElement htmlElement) {
        if (htmlElement instanceof HTMLBodyElement || htmlElement instanceof HTMLFrameSetElement) {
            final Page page = getPage();
            if (page instanceof HtmlPage) {
                final HtmlElement body = ((HtmlPage) page).getBody();
                if (body != null) {
                    body.replace(htmlElement.getDomNodeOrDie());
                }
            }
            return;
        }
        throw JavaScriptEngine.asJavaScriptException(
                getWindow(),
                "Failed to set the 'body' property on 'Document': "
                        + "The new body element is of type '" +  htmlElement.getTagName() + "'. "
                        + "It must be either a 'BODY' or 'FRAMESET' element.",
                org.htmlunit.javascript.host.dom.DOMException.HIERARCHY_REQUEST_ERR);
    }

    /**
     * JavaScript function {@code close}.
     * <p>See <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/section-dynamic.html">
     * http://www.whatwg.org/specs/web-apps/current-work/multipage/section-dynamic.html</a> for
     * a good description of the semantics of open(), write(), writeln() and close().</p>
     *
     * @throws IOException if an IO problem occurs
     */
    @JsxFunction({CHROME, EDGE})
    public void close() throws IOException {
        // nothing to do
    }

    /**
     * Returns the {@code compatMode} property.
     * @return the {@code compatMode} property
     */
    @JsxGetter
    public String getCompatMode() {
        // initialize the modes
        getDocumentMode();
        return compatMode_;
    }

    /**
     * Returns the {@code documentMode} property.
     * @return the {@code documentMode} property
     */
    public int getDocumentMode() {
        if (documentMode_ != -1) {
            return documentMode_;
        }

        compatMode_ = "CSS1Compat";

        if (isQuirksDocType()) {
            compatMode_ = "BackCompat";
        }

        final float version = getBrowserVersion().getBrowserVersionNumeric();
        documentMode_ = (int) Math.floor(version);
        return documentMode_;
    }

    private boolean isQuirksDocType() {
        final DocumentType docType = getPage().getDoctype();
        if (docType != null) {
            final String systemId = docType.getSystemId();
            if (systemId != null) {
                if ("http://www.w3.org/TR/html4/strict.dtd".equals(systemId)) {
                    return false;
                }

                if ("http://www.w3.org/TR/html4/loose.dtd".equals(systemId)) {
                    final String publicId = docType.getPublicId();
                    if ("-//W3C//DTD HTML 4.01 Transitional//EN".equals(publicId)) {
                        return false;
                    }
                }

                if ("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd".equals(systemId)
                    || "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd".equals(systemId)) {
                    return false;
                }
            }
            else if (docType.getPublicId() == null) {
                return docType.getName() == null;
            }
        }
        return true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Called from the HTMLParser if an 'X-UA-Compatible' meta tag found.
     * @param documentMode the mode forced by the meta tag
     */
    public void forceDocumentMode(final int documentMode) {
        documentMode_ = documentMode;
        compatMode_ = documentMode == 5 ? "BackCompat" : "CSS1Compat";
    }

    /**
     * Returns the first element within the document that matches the specified group of selectors.
     * @param selectors the selectors
     * @return null if no matches are found; otherwise, it returns the first matching element
     */
    @JsxFunction
    public Node querySelector(final String selectors) {
        try {
            final DomNode node = getDomNodeOrDie().querySelector(selectors);
            if (node != null) {
                return node.getScriptableObject();
            }
            return null;
        }
        catch (final CSSException e) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "An invalid or illegal selector was specified (selector: '"
                            + selectors + "' error: " + e.getMessage() + ").",
                    org.htmlunit.javascript.host.dom.DOMException.SYNTAX_ERR);
        }
    }

    /**
     * Retrieves all element nodes from descendants of the starting element node that match any selector
     * within the supplied selector strings.
     * The NodeList object returned by the querySelectorAll() method must be static, not live.
     * @param selectors the selectors
     * @return the static node list
     */
    @JsxFunction
    public NodeList querySelectorAll(final String selectors) {
        try {
            return NodeList.staticNodeList(this, getDomNodeOrDie().querySelectorAll(selectors));
        }
        catch (final CSSException e) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "An invalid or illegal selector was specified (selector: '"
                            + selectors + "' error: " + e.getMessage() + ").",
                    org.htmlunit.javascript.host.dom.DOMException.SYNTAX_ERR);
        }
    }

    /**
     * Indicates if the command is supported.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536681.aspx">MSDN documentation</a>
     * @param cmd the command identifier
     * @return {@code true} if the command is supported
     */
    @JsxFunction
    public boolean queryCommandSupported(final String cmd) {
        return hasCommand(cmd, true);
    }

    private boolean hasCommand(final String cmd, final boolean includeBold) {
        if (null == cmd) {
            return false;
        }

        final String cmdLC = cmd.toLowerCase(Locale.ROOT);
        if (getBrowserVersion().isChrome() || getBrowserVersion().isEdge()) {
            return EXECUTE_CMDS_CHROME.contains(cmdLC) || (includeBold && "bold".equalsIgnoreCase(cmd));
        }
        return EXECUTE_CMDS_FF.contains(cmdLC);
    }

    /**
     * Indicates if the command can be successfully executed using <code>execCommand</code>, given
     * the current state of the document.
     * @param cmd the command identifier
     * @return {@code true} if the command can be successfully executed
     */
    @JsxFunction
    public boolean queryCommandEnabled(final String cmd) {
        return hasCommand(cmd, true);
    }

    /**
     * Executes a command.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536419.aspx">MSDN documentation</a>
     * @param cmd the command identifier
     * @param userInterface display a user interface if the command supports one
     * @param value the string, number, or other value to assign (possible values depend on the command)
     * @return {@code true} if the command was successful, {@code false} otherwise
     */
    @JsxFunction
    public boolean execCommand(final String cmd, final boolean userInterface, final Object value) {
        if (!hasCommand(cmd, false)) {
            return false;
        }
        if (LOG.isWarnEnabled()) {
            LOG.warn("Nothing done for execCommand(" + cmd + ", ...) (feature not implemented)");
        }
        return true;
    }

    /**
     * Returns the value of the {@code URL} property.
     * @return the value of the {@code URL} property
     */
    @JsxGetter(propertyName = "URL")
    public String getURL_js() {
        return getPage().getUrl().toExternalForm();
    }

    /**
     * Returns the value of the {@code documentURI} property.
     * @return the value of the {@code documentURI} property
     */
    @JsxGetter
    public String getDocumentURI() {
        return getURL_js();
    }

    /**
     * Returns the {@code cookie} property.
     * @return the {@code cookie} property
     */
    @JsxGetter
    public String getCookie() {
        final SgmlPage sgmlPage = getPage();

        final StringBuilder builder = new StringBuilder();
        final Set<Cookie> cookies = sgmlPage.getWebClient().getCookies(sgmlPage.getUrl());
        for (final Cookie cookie : cookies) {
            if (cookie.isHttpOnly()) {
                continue;
            }
            if (builder.length() != 0) {
                builder.append("; ");
            }
            if (!HtmlUnitBrowserCompatCookieSpec.EMPTY_COOKIE_NAME.equals(cookie.getName())) {
                builder.append(cookie.getName()).append('=');
            }
            builder.append(cookie.getValue());
        }

        return builder.toString();
    }

    /**
     * Adds a cookie, as long as cookies are enabled.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533693.aspx">MSDN documentation</a>
     * @param newCookie in the format "name=value[;expires=date][;domain=domainname][;path=path][;secure]"
     */
    @JsxSetter
    public void setCookie(final String newCookie) {
        if (StringUtils.isBlank(newCookie)) {
            return;
        }

        final SgmlPage sgmlPage = getPage();
        sgmlPage.getWebClient().addCookie(newCookie, sgmlPage.getUrl(), this);
    }

    /**
     * Implementation of the {@link org.w3c.dom.events.DocumentEvent} interface's
     * {@link org.w3c.dom.events.DocumentEvent#createEvent(String)} method. The method creates an
     * uninitialized event of the specified type.
     *
     * @see <a href="http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-DocumentEvent">DocumentEvent</a>
     * @param eventType the event type to create
     * @return an event object for the specified type
     * @throws DOMException if the event type is not supported (will have a type of
     *         DOMException.NOT_SUPPORTED_ERR)
     */
    @JsxFunction
    public Event createEvent(final String eventType) throws DOMException {
        Class<? extends Event> clazz = SUPPORTED_DOM2_EVENT_TYPE_MAP.get(eventType);
        if (clazz == null) {
            clazz = SUPPORTED_DOM3_EVENT_TYPE_MAP.get(eventType);
            if (CloseEvent.class == clazz
                    && getBrowserVersion().hasFeature(EVENT_ONCLOSE_DOCUMENT_CREATE_NOT_SUPPORTED)) {
                clazz = null;
            }
            else if (TextEvent.class == clazz
                    && !getBrowserVersion().hasFeature(EVENT_TYPE_TEXTEVENT)) {
                clazz = CompositionEvent.class;
            }
        }

        if (MutationEvent.class == clazz
                && !getBrowserVersion().hasFeature(EVENT_TYPE_MUTATIONEVENT)) {
            clazz = null;
        }
        else if (clazz == null
                && ("Events".equals(eventType)
                    || "HashChangeEvent".equals(eventType)
                    || "BeforeUnloadEvent".equals(eventType)
                    || "PopStateEvent".equals(eventType)
                    || "FocusEvent".equals(eventType)
                    || "WheelEvent".equals(eventType)
                            && getBrowserVersion().hasFeature(EVENT_TYPE_WHEELEVENT)
                    || "AnimationEvent".equals(eventType))) {
            clazz = SUPPORTED_VENDOR_EVENT_TYPE_MAP.get(eventType);

            if (PopStateEvent.class == clazz
                    && getBrowserVersion().hasFeature(EVENT_ONPOPSTATE_DOCUMENT_CREATE_NOT_SUPPORTED)) {
                clazz = null;
            }
            if (AnimationEvent.class == clazz
                    && getBrowserVersion().hasFeature(EVENT_ONANIMATION_DOCUMENT_CREATE_NOT_SUPPORTED)) {
                clazz = null;
            }
        }

        if (clazz == null) {
            throw JavaScriptEngine.asJavaScriptException(
                    this,
                    "Event Type '" + eventType + "' is not supported.",
                    org.htmlunit.javascript.host.dom.DOMException.NOT_SUPPORTED_ERR);
        }

        try {
            final Event event = clazz.getDeclaredConstructor().newInstance();
            event.setParentScope(getWindow());
            event.setPrototype(getPrototype(clazz));
            event.eventCreated();
            return event;
        }
        catch (final InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw JavaScriptEngine.reportRuntimeError("Failed to instantiate event: class ='" + clazz.getName()
                            + "' for event type of '" + eventType + "': " + e.getMessage());
        }
    }

    /**
     * Returns a new NodeIterator object.
     *
     * @param root The root node at which to begin the NodeIterator's traversal.
     * @param whatToShow an optional long representing a bitmask created by combining
     *        the constant properties of {@link NodeFilter}
     * @param filter an object implementing the {@link NodeFilter} interface
     * @return a new NodeIterator object
     */
    @JsxFunction
    public NodeIterator createNodeIterator(final Node root, final int whatToShow, final Scriptable filter) {
        final org.w3c.dom.traversal.NodeFilter filterWrapper = createFilterWrapper(filter, false);
        final NodeIterator iterator = new NodeIterator(root, whatToShow, filterWrapper);
        iterator.setParentScope(getParentScope());
        iterator.setPrototype(getPrototype(iterator.getClass()));
        return iterator;
    }

    private static org.w3c.dom.traversal.NodeFilter createFilterWrapper(final Scriptable filter,
            final boolean filterFunctionOnly) {
        org.w3c.dom.traversal.NodeFilter filterWrapper = null;
        if (filter != null) {
            filterWrapper = n -> {
                final Object[] args = {((DomNode) n).getScriptableObject()};
                final Object response;
                if (filter instanceof Callable) {
                    response = ((Callable) filter).call(Context.getCurrentContext(), filter, filter, args);
                }
                else {
                    if (filterFunctionOnly) {
                        throw JavaScriptEngine.reportRuntimeError("only a function is allowed as filter");
                    }
                    response = ScriptableObject.callMethod(filter, "acceptNode", args);
                }
                return (short) JavaScriptEngine.toNumber(response);
            };
        }
        return filterWrapper;
    }

    /**
     * Creates and returns a new TreeWalker. The following JavaScript parameters are passed into this method:
     * <ul>
     *   <li>JavaScript param 1: The root node of the TreeWalker. Must not be {@code null}.</li>
     *   <li>JavaScript param 2: Flag specifying which types of nodes appear in the logical view of the TreeWalker.
     *       See {@link NodeFilter} for the set of possible Show_ values.</li>
     *   <li>JavaScript param 3: The {@link NodeFilter} to be used with this TreeWalker, or {@code null}
     *       to indicate no filter.</li>
     *   <li>JavaScript param 4: If {@code false}, the contents of EntityReference nodes are not present
     *       in the logical view.</li>
     * </ul>
     *
     * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/traversal.html">DOM-Level-2-Traversal-Range</a>
     * @param root the node which will serve as the root for the TreeWalker
     * @param whatToShow specifies which node types may appear in the logical view of the tree presented
     * @param filter the NodeFilter to be used with this TreeWalker, or null to indicate no filter
     * @param expandEntityReferences If false,
     *        the contents of EntityReference nodes are not presented in the logical view
     * @throws DOMException on attempt to create a TreeWalker with a root that is {@code null}
     * @return a new TreeWalker
     */
    @JsxFunction
    public TreeWalker createTreeWalker(final Node root, final double whatToShow, final Scriptable filter,
            final boolean expandEntityReferences) throws DOMException {

        // seems that Rhino doesn't like long as parameter type
        // this strange conversation preserves NodeFilter.SHOW_ALL
        final int whatToShowI = (int) Double.valueOf(whatToShow).longValue();

        final org.w3c.dom.traversal.NodeFilter filterWrapper = createFilterWrapper(filter, false);
        final TreeWalker t = new TreeWalker(root, whatToShowI, filterWrapper, false);
        t.setParentScope(getWindow(this));
        t.setPrototype(staticGetPrototype(getWindow(this), TreeWalker.class));
        return t;
    }

    @SuppressWarnings("unchecked")
    private static Scriptable staticGetPrototype(final Window window,
            final Class<? extends HtmlUnitScriptable> javaScriptClass) {
        final Scriptable prototype = window.getPrototype(javaScriptClass);
        if (prototype == null && javaScriptClass != HtmlUnitScriptable.class) {
            return staticGetPrototype(window, (Class<? extends HtmlUnitScriptable>) javaScriptClass.getSuperclass());
        }
        return prototype;
    }

    /**
     * Creates and returns a new range.
     * @return a new range
     * @see <a href="http://www.xulplanet.com/references/objref/HTMLDocument.html#method_createRange">XUL Planet</a>
     */
    @JsxFunction
    public Range createRange() {
        final Range range = new Range(this);
        range.setParentScope(getWindow());
        range.setPrototype(getPrototype(Range.class));
        return range;
    }

    /**
     * Returns the domain name of the server that served the document, or {@code null} if the server
     * cannot be identified by a domain name.
     * @return the domain name of the server that served the document
     * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-2250147">
     *     W3C documentation</a>
     */
    @JsxGetter
    public String getDomain() {
        if (domain_ == null && getPage().getWebResponse() != null) {
            URL url = getPage().getUrl();
            if (url == UrlUtils.URL_ABOUT_BLANK) {
                final WebWindow w = getWindow().getWebWindow();
                if (w instanceof FrameWindow) {
                    url = ((FrameWindow) w).getEnclosingPage().getUrl();
                }
                else {
                    return null;
                }
            }
            domain_ = url.getHost().toLowerCase(Locale.ROOT);
        }

        return domain_;
    }

    /**
     * Sets the domain of this document.
     *
     * <p>Domains can only be set to suffixes of the existing domain
     * with the except of setting the domain to itself.</p>
     * <p>
     * The domain will be set according to the following rules:
     * <ol>
     * <li>If the newDomain.equalsIgnoreCase(currentDomain) the method returns with no error.</li>
     * <li>If the browser version is netscape, the newDomain is downshifted.</li>
     * <li>The change will take place if and only if the suffixes of the
     *       current domain and the new domain match AND there are at least
     *       two domain qualifiers e.g. the following transformations are legal
     *       d1.d2.d3.gargoylesoftware.com may be transformed to itself or:
     *          d2.d3.gargoylesoftware.com
     *             d3.gargoylesoftware.com
     *                gargoylesoftware.com
     * <p>
     *        transformation to:        com
     *        will fail
     * </li>
     * </ol>
     * <p>
     * TODO This code could be modified to understand country domain suffixes.
     * The domain www.bbc.co.uk should be trimmable only down to bbc.co.uk
     * trimming to co.uk should not be possible.
     * @param newDomain the new domain to set
     */
    @JsxSetter
    public void setDomain(String newDomain) {
        newDomain = newDomain.toLowerCase(Locale.ROOT);

        final String currentDomain = getDomain();
        if (currentDomain.equalsIgnoreCase(newDomain)) {
            return;
        }

        if (newDomain.indexOf('.') == -1) {
            throw JavaScriptEngine.reportRuntimeError("Illegal domain value, cannot set domain from: \""
                    + currentDomain + "\" to: \"" + newDomain + "\" (new domain has to contain a dot).");
        }

        if (currentDomain.indexOf('.') > -1
                && !currentDomain.toLowerCase(Locale.ROOT).endsWith("." + newDomain.toLowerCase(Locale.ROOT))) {
            throw JavaScriptEngine.reportRuntimeError("Illegal domain value, cannot set domain from: \""
                    + currentDomain + "\" to: \"" + newDomain + "\"");
        }

        domain_ = newDomain;
    }

    /**
     * Sets the {@code onclick} event handler for this element.
     * @param handler the {@code onclick} event handler for this element
     */
    @JsxSetter
    public void setOnclick(final Object handler) {
        setEventHandler(MouseEvent.TYPE_CLICK, handler);
    }

    /**
     * Returns the {@code onclick} event handler for this element.
     * @return the {@code onclick} event handler for this element
     */
    @JsxGetter
    public Function getOnclick() {
        return getEventHandler(MouseEvent.TYPE_CLICK);
    }

    /**
     * Sets the {@code ondblclick} event handler for this element.
     * @param handler the {@code ondblclick} event handler for this element
     */
    @JsxSetter
    public void setOndblclick(final Object handler) {
        setEventHandler(MouseEvent.TYPE_DBL_CLICK, handler);
    }

    /**
     * Returns the {@code ondblclick} event handler for this element.
     * @return the {@code ondblclick} event handler for this element
     */
    @JsxGetter
    public Function getOndblclick() {
        return getEventHandler(MouseEvent.TYPE_DBL_CLICK);
    }

    /**
     * Sets the {@code onblur} event handler for this element.
     * @param handler the {@code onblur} event handler for this element
     */
    @JsxSetter
    public void setOnblur(final Object handler) {
        setEventHandler(Event.TYPE_BLUR, handler);
    }

    /**
     * Returns the {@code onblur} event handler for this element.
     * @return the {@code onblur} event handler for this element
     */
    @JsxGetter
    public Function getOnblur() {
        return getEventHandler(Event.TYPE_BLUR);
    }

    /**
     * Sets the {@code onfocus} event handler for this element.
     * @param handler the {@code onfocus} event handler for this element
     */
    @JsxSetter
    public void setOnfocus(final Object handler) {
        setEventHandler(Event.TYPE_FOCUS, handler);
    }

    /**
     * Returns the {@code onfocus} event handler for this element.
     * @return the {@code onfocus} event handler for this element
     */
    @JsxGetter
    public Function getOnfocus() {
        return getEventHandler(Event.TYPE_FOCUS);
    }

    /**
     * Sets the {@code onkeydown} event handler for this element.
     * @param handler the {@code onkeydown} event handler for this element
     */
    @JsxSetter
    public void setOnkeydown(final Object handler) {
        setEventHandler(Event.TYPE_KEY_DOWN, handler);
    }

    /**
     * Returns the {@code onkeydown} event handler for this element.
     * @return the {@code onkeydown} event handler for this element
     */
    @JsxGetter
    public Function getOnkeydown() {
        return getEventHandler(Event.TYPE_KEY_DOWN);
    }

    /**
     * Sets the {@code onkeypress} event handler for this element.
     * @param handler the {@code onkeypress} event handler for this element
     */
    @JsxSetter
    public void setOnkeypress(final Object handler) {
        setEventHandler(Event.TYPE_KEY_PRESS, handler);
    }

    /**
     * Returns the {@code onkeypress} event handler for this element.
     * @return the {@code onkeypress} event handler for this element
     */
    @JsxGetter
    public Function getOnkeypress() {
        return getEventHandler(Event.TYPE_KEY_PRESS);
    }

    /**
     * Sets the {@code onkeyup} event handler for this element.
     * @param handler the {@code onkeyup} event handler for this element
     */
    @JsxSetter
    public void setOnkeyup(final Object handler) {
        setEventHandler(Event.TYPE_KEY_UP, handler);
    }

    /**
     * Returns the {@code onkeyup} event handler for this element.
     * @return the {@code onkeyup} event handler for this element
     */
    @JsxGetter
    public Function getOnkeyup() {
        return getEventHandler(Event.TYPE_KEY_UP);
    }

    /**
     * Sets the {@code onmousedown} event handler for this element.
     * @param handler the {@code onmousedown} event handler for this element
     */
    @JsxSetter
    public void setOnmousedown(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_DOWN, handler);
    }

    /**
     * Returns the {@code onmousedown} event handler for this element.
     * @return the {@code onmousedown} event handler for this element
     */
    @JsxGetter
    public Function getOnmousedown() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_DOWN);
    }

    /**
     * Sets the {@code onmousemove} event handler for this element.
     * @param handler the {@code onmousemove} event handler for this element
     */
    @JsxSetter
    public void setOnmousemove(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_MOVE, handler);
    }

    /**
     * Returns the {@code onmousemove} event handler for this element.
     * @return the {@code onmousemove} event handler for this element
     */
    @JsxGetter
    public Function getOnmousemove() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_MOVE);
    }

    /**
     * Sets the {@code onmouseout} event handler for this element.
     * @param handler the {@code onmouseout} event handler for this element
     */
    @JsxSetter
    public void setOnmouseout(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_OUT, handler);
    }

    /**
     * Returns the {@code onmouseout} event handler for this element.
     * @return the {@code onmouseout} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseout() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OUT);
    }

    /**
     * Sets the {@code onmouseover} event handler for this element.
     * @param handler the {@code onmouseover} event handler for this element
     */
    @JsxSetter
    public void setOnmouseover(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_OVER, handler);
    }

    /**
     * Returns the {@code onmouseover} event handler for this element.
     * @return the {@code onmouseover} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseover() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OVER);
    }

    /**
     * Sets the {@code onmouseup} event handler for this element.
     * @param handler the {@code onmouseup} event handler for this element
     */
    @JsxSetter
    public void setOnmouseup(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_UP, handler);
    }

    /**
     * Returns the {@code onmouseup} event handler for this element.
     * @return the {@code onmouseup} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseup() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_UP);
    }

    /**
     * Sets the {@code oncontextmenu} event handler for this element.
     * @param handler the {@code oncontextmenu} event handler for this element
     */
    @JsxSetter
    public void setOncontextmenu(final Object handler) {
        setEventHandler(MouseEvent.TYPE_CONTEXT_MENU, handler);
    }

    /**
     * Returns the {@code oncontextmenu} event handler for this element.
     * @return the {@code oncontextmenu} event handler for this element
     */
    @JsxGetter
    public Function getOncontextmenu() {
        return getEventHandler(MouseEvent.TYPE_CONTEXT_MENU);
    }

    /**
     * Sets the {@code onresize} event handler for this element.
     * @param handler the {@code onresize} event handler for this element
     */
    @JsxSetter
    public void setOnresize(final Object handler) {
        setEventHandler(Event.TYPE_RESIZE, handler);
    }

    /**
     * Returns the {@code onresize} event handler for this element.
     * @return the {@code onresize} event handler for this element
     */
    @JsxGetter
    public Function getOnresize() {
        return getEventHandler(Event.TYPE_RESIZE);
    }

    /**
     * Sets the {@code onerror} event handler for this element.
     * @param handler the {@code onerror} event handler for this element
     */
    @JsxSetter
    public void setOnerror(final Object handler) {
        setEventHandler(Event.TYPE_ERROR, handler);
    }

    /**
     * Returns the {@code onerror} event handler for this element.
     * @return the {@code onerror} event handler for this element
     */
    @JsxGetter
    public Function getOnerror() {
        return getEventHandler(Event.TYPE_ERROR);
    }

    /**
     * Returns the {@code oninput} event handler for this element.
     * @return the {@code oninput} event handler for this element
     */
    @JsxGetter
    public Function getOninput() {
        return getEventHandler(Event.TYPE_INPUT);
    }

    /**
     * Sets the {@code oninput} event handler for this element.
     * @param oninput the {@code oninput} event handler for this element
     */
    @JsxSetter
    public void setOninput(final Object oninput) {
        setEventHandler(Event.TYPE_INPUT, oninput);
    }

    /**
     * Returns the {@code hidden} property.
     * @return the {@code hidden} property
     */
    @JsxGetter
    public boolean isHidden() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public int getChildElementCount() {
        int counter = 0;
        if (getPage().getDocumentElement() != null) {
            counter++;
        }
        return counter;
    }

    /**
     * Returns the element for the specified x coordinate and the specified y coordinate.
     * The current implementation always returns null element.
     *
     * @param x the x offset, in pixels
     * @param y the y offset, in pixels
     * @return the element for the specified x coordinate and the specified y coordinate
     */
    @JsxFunction
    public HtmlUnitScriptable elementFromPoint(final int x, final int y) {
        return null;
    }

    /**
     * Returns the value of the {@code forms} property.
     * @return the value of the {@code forms} property
     */
    @JsxGetter
    public HTMLCollection getForms() {
        final HTMLCollection forms = new HTMLCollection(getDomNodeOrDie(), false);

        forms.setIsMatchingPredicate(
                (Predicate<DomNode> & Serializable)
                node -> node instanceof HtmlForm && node.getPrefix() == null);
        return forms;
    }

    /**
     * Returns the value of the {@code embeds} property.
     * @return the value of the {@code embeds} property
     */
    @JsxGetter
    public HTMLCollection getEmbeds() {
        final HTMLCollection embeds = new HTMLCollection(getDomNodeOrDie(), false);

        embeds.setIsMatchingPredicate((Predicate<DomNode> & Serializable) node -> node instanceof HtmlEmbed);
        return embeds;
    }

    /**
     * Returns the value of the {@code embeds} property.
     * @return the value of the {@code embeds} property
     */
    @JsxGetter
    public HTMLCollection getImages() {
        final HTMLCollection images = new HTMLCollection(getDomNodeOrDie(), false);

        images.setIsMatchingPredicate((Predicate<DomNode> & Serializable) node -> node instanceof HtmlImage);
        return images;
    }

    /**
     * Returns the value of the {@code scripts} property.
     * @return the value of the {@code scripts} property
     */
    @JsxGetter
    public HTMLCollection getScripts() {
        final HTMLCollection scripts = new HTMLCollection(getDomNodeOrDie(), false);

        scripts.setIsMatchingPredicate((Predicate<DomNode> & Serializable) node -> node instanceof HtmlScript);
        return scripts;
    }

    /**
     * Retrieves a collection of stylesheet objects representing the style sheets that correspond
     * to each instance of a Link or
     * {@link org.htmlunit.javascript.host.css.CSSStyleDeclaration} object in the document.
     *
     * @return styleSheet collection
     */
    @JsxGetter
    public StyleSheetList getStyleSheets() {
        if (styleSheetList_ == null) {
            styleSheetList_ = new StyleSheetList(this);
        }
        return styleSheetList_;
    }

    /**
     * Returns the value of the {@code plugins} property.
     * @return the value of the {@code plugins} property
     */
    @JsxGetter
    public HTMLCollection getPlugins() {
        return getEmbeds();
    }

    /**
     * Returns the value of the JavaScript property {@code links}. Refer also to the
     * <a href="http://msdn.microsoft.com/en-us/library/ms537465.aspx">MSDN documentation</a>.
     * @return the value of this property
     */
    @JsxGetter
    public HTMLCollection getLinks() {
        final HTMLCollection links = new HTMLCollection(getDomNodeOrDie(), true);

        links.setEffectOnCacheFunction(
                (java.util.function.Function<HtmlAttributeChangeEvent, EffectOnCache> & Serializable)
                event -> {
                    final HtmlElement node = event.getHtmlElement();
                    if ((node instanceof HtmlAnchor || node instanceof HtmlArea) && "href".equals(event.getName())) {
                        return EffectOnCache.RESET;
                    }
                    return EffectOnCache.NONE;
                });

        links.setIsMatchingPredicate(
                (Predicate<DomNode> & Serializable)
                node ->
                    (node instanceof HtmlAnchor || node instanceof HtmlArea)
                    && ((HtmlElement) node).hasAttribute("href"));

        return links;
    }

    /**
     * Returns all the descendant elements with the specified class name.
     * @param className the name to search for
     * @return all the descendant elements with the specified class name
     * @see <a href="https://developer.mozilla.org/en/DOM/document.getElementsByClassName">Mozilla doc</a>
     */
    @JsxFunction
    public HTMLCollection getElementsByClassName(final String className) {
        return null;
    }

    /**
     * Returns all HTML elements that have a {@code name} attribute with the specified value.
     * <p>
     * Refer to <a href="http://www.w3.org/TR/DOM-Level-2-HTML/html.html#ID-71555259">
     * The DOM spec</a> for details.
     *
     * @param elementName - value of the {@code name} attribute to look for
     * @return all HTML elements that have a {@code name} attribute with the specified value
     */
    @JsxFunction
    public NodeList getElementsByName(final String elementName) {
        return null;
    }

    /**
     * Returns {@code false} if the active element in the document has no focus;
     * {@code true} if the active element in the document has focus.
     * @return whether the active element in the document has focus or not
     */
    @JsxFunction
    public boolean hasFocus() {
        return false;
    }

    /**
     * Returns this document's title.
     * @return this document's title
     */
    @JsxGetter
    public String getTitle() {
        return "";
    }

    /**
     * Sets this document's title.
     * @param title the new title
     */
    @JsxSetter
    public void setTitle(final String title) {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public HTMLCollection getChildren() {
        return super.getChildren();
    }

    /**
     * Returns the {@code contentType} property.
     * @return the {@code contentType} property
     */
    @JsxGetter
    public String getContentType() {
        return getPage().getContentType();
    }

    /**
     * Returns the current selection.
     * @return the current selection
     */
    @JsxFunction
    public Selection getSelection() {
        return null;
    }

    /**
     * Returns this document's {@code head} element.
     * @return this document's {@code head} element
     */
    @JsxGetter
    public Object getHead() {
        return null;
    }

    /**
     * Returns a string representing the encoding under which the document was parsed.
     * @return a string representing the encoding under which the document was parsed
     */
    @JsxGetter
    public String getInputEncoding() {
        return getPage().getCharset().name();
    }

    /**
     * Returns the last modification date of the document.
     * @see <a href="https://developer.mozilla.org/en/DOM/document.lastModified">Mozilla documentation</a>
     * @return the date as string
     */
    @JsxGetter
    public String getLastModified() {
        if (lastModified_ == null) {
            final WebResponse webResponse = getPage().getWebResponse();
            final Date lastModified;
            if (webResponse != null) {
                String stringDate = webResponse.getResponseHeaderValue("Last-Modified");
                if (stringDate == null) {
                    stringDate = webResponse.getResponseHeaderValue("Date");
                }
                lastModified = parseDateOrNow(stringDate);
            }
            else {
                lastModified = new Date();
            }

            final ZoneId zoneid = Context.getCurrentContext().getTimeZone().toZoneId();
            lastModified_ = LAST_MODIFIED_DATE_FORMATTER.format(lastModified.toInstant().atZone(zoneid));
        }
        return lastModified_;
    }

    private static Date parseDateOrNow(final String stringDate) {
        final Date date = HttpUtils.parseDate(stringDate);
        if (date == null) {
            return new Date();
        }
        return date;
    }

    /**
     * Mock for the moment.
     */
    @JsxFunction({FF, FF_ESR})
    public void releaseCapture() {
        // nothing to do
    }

    /**
     * Returns the ready state of the document.
     * @return the ready state of the document
     *
     * @see DomNode#READY_STATE_UNINITIALIZED
     * @see DomNode#READY_STATE_LOADING
     * @see DomNode#READY_STATE_LOADED
     * @see DomNode#READY_STATE_INTERACTIVE
     * @see DomNode#READY_STATE_COMPLETE
     */
    @JsxGetter
    public String getReadyState() {
        return getDomNodeOrDie().getReadyState();
    }

    /**
     * Does nothing special anymore.
     *
     * @param type the type of events to capture
     * @see Window#captureEvents(String)
     */
    @JsxFunction
    public void captureEvents(final String type) {
        // Empty.
    }

    /**
     * Does nothing special anymore.
     *
     * @param type the type of events to capture
     * @see Window#releaseEvents(String)
     */
    @JsxFunction
    public void releaseEvents(final String type) {
        // Empty.
    }

    /**
     * Returns the value of the {@code alinkColor} property.
     * @return the value of the {@code alinkColor} property
     */
    @JsxGetter
    public String getAlinkColor() {
        final HTMLElement body = getBody();
        if (body instanceof HTMLBodyElement) {
            return ((HTMLBodyElement) body).getALink();
        }
        return null;
    }

    /**
     * Sets the value of the {@code alinkColor} property.
     * @param color the value of the {@code alinkColor} property
     */
    @JsxSetter
    public void setAlinkColor(final String color) {
        final HTMLElement body = getBody();
        if (body instanceof HTMLBodyElement) {
            ((HTMLBodyElement) body).setALink(color);
        }
    }

    /**
     * Returns the value of the {@code bgColor} property.
     * @return the value of the {@code bgColor} property
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getBgColor() {
        final HTMLElement body = getBody();
        if (body instanceof HTMLBodyElement) {
            return ((HTMLBodyElement) body).getBgColor();
        }
        return null;
    }

    /**
     * Sets the value of the {@code bgColor} property.
     * @param color the value of the {@code bgColor} property
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setBgColor(final String color) {
        final HTMLElement body = getBody();
        if (body instanceof HTMLBodyElement) {
            ((HTMLBodyElement) body).setBgColor(color);
        }
    }

    /**
     * Returns the value of the {@code fgColor} property.
     * @return the value of the {@code fgColor} property
     */
    @JsxGetter
    public String getFgColor() {
        final HTMLElement body = getBody();
        if (body instanceof HTMLBodyElement) {
            return ((HTMLBodyElement) body).getText();
        }
        return null;
    }

    /**
     * Sets the value of the {@code fgColor} property.
     * @param color the value of the {@code fgColor} property
     */
    @JsxSetter
    public void setFgColor(final String color) {
        final HTMLElement body = getBody();
        if (body instanceof HTMLBodyElement) {
            ((HTMLBodyElement) body).setText(color);
        }
    }

    /**
     * Returns the value of the {@code linkColor} property.
     * @return the value of the {@code linkColor} property
     */
    @JsxGetter
    public String getLinkColor() {
        final HTMLElement body = getBody();
        if (body instanceof HTMLBodyElement) {
            return ((HTMLBodyElement) body).getLink();
        }
        return null;
    }

    /**
     * Sets the value of the {@code linkColor} property.
     * @param color the value of the {@code linkColor} property
     */
    @JsxSetter
    public void setLinkColor(final String color) {
        final HTMLElement body = getBody();
        if (body instanceof HTMLBodyElement) {
            ((HTMLBodyElement) body).setLink(color);
        }
    }

    /**
     * Returns the value of the {@code vlinkColor} property.
     * @return the value of the {@code vlinkColor} property
     */
    @JsxGetter
    public String getVlinkColor() {
        final HTMLElement body = getBody();
        if (body instanceof HTMLBodyElement) {
            return ((HTMLBodyElement) body).getVLink();
        }
        return null;
    }

    /**
     * Sets the value of the {@code vlinkColor} property.
     * @param color the value of the {@code vlinkColor} property
     */
    @JsxSetter
    public void setVlinkColor(final String color) {
        final HTMLElement body = getBody();
        if (body instanceof HTMLBodyElement) {
            ((HTMLBodyElement) body).setVLink(color);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public Element getLastElementChild() {
        return super.getLastElementChild();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public Element getFirstElementChild() {
        return super.getFirstElementChild();
    }

    /**
     * Returns the {@code xmlEncoding} property.
     * @return the {@code xmlEncoding} property
     */
    @JsxGetter({CHROME, EDGE})
    public String getXmlEncoding() {
        return getPage().getXmlEncoding();
    }

    /**
     * Returns the {@code xmlStandalone} property.
     * @return the {@code xmlStandalone} property
     */
    @JsxGetter({CHROME, EDGE})
    public boolean isXmlStandalone() {
        return getPage().getXmlStandalone();
    }

    /**
     * Returns the {@code xmlVersion} property.
     * @return the {@code xmlVersion} property
     */
    @JsxGetter({CHROME, EDGE})
    public String getXmlVersion() {
        return getPage().getXmlVersion();
    }

    /**
     * Returns the {@code onabort} event handler for this element.
     * @return the {@code onabort} event handler for this element
     */
    @JsxGetter
    public Function getOnabort() {
        return getEventHandler(Event.TYPE_ABORT);
    }

    /**
     * Sets the {@code onabort} event handler for this element.
     * @param onabort the {@code onabort} event handler for this element
     */
    @JsxSetter
    public void setOnabort(final Object onabort) {
        setEventHandler(Event.TYPE_ABORT, onabort);
    }

    /**
     * Returns the {@code onauxclick} event handler for this element.
     * @return the {@code onauxclick} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnauxclick() {
        return getEventHandler(Event.TYPE_AUXCLICK);
    }

    /**
     * Sets the {@code onauxclick} event handler for this element.
     * @param onauxclick the {@code onauxclick} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnauxclick(final Object onauxclick) {
        setEventHandler(Event.TYPE_AUXCLICK, onauxclick);
    }

    /**
     * Returns the {@code onbeforecopy} event handler for this element.
     * @return the {@code onbeforecopy} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnbeforecopy() {
        return getEventHandler(Event.TYPE_BEFORECOPY);
    }

    /**
     * Sets the {@code onbeforecopy} event handler for this element.
     * @param onbeforecopy the {@code onbeforecopy} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnbeforecopy(final Object onbeforecopy) {
        setEventHandler(Event.TYPE_BEFORECOPY, onbeforecopy);
    }

    /**
     * Returns the {@code onbeforecut} event handler for this element.
     * @return the {@code onbeforecut} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnbeforecut() {
        return getEventHandler(Event.TYPE_BEFORECUT);
    }

    /**
     * Sets the {@code onbeforecut} event handler for this element.
     * @param onbeforecut the {@code onbeforecut} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnbeforecut(final Object onbeforecut) {
        setEventHandler(Event.TYPE_BEFORECUT, onbeforecut);
    }

    /**
     * Returns the {@code onbeforepaste} event handler for this element.
     * @return the {@code onbeforepaste} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnbeforepaste() {
        return getEventHandler(Event.TYPE_BEFOREPASTE);
    }

    /**
     * Sets the {@code onbeforepaste} event handler for this element.
     * @param onbeforepaste the {@code onbeforepaste} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnbeforepaste(final Object onbeforepaste) {
        setEventHandler(Event.TYPE_BEFOREPASTE, onbeforepaste);
    }

    /**
     * Returns the {@code oncancel} event handler for this element.
     * @return the {@code oncancel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOncancel() {
        return getEventHandler(Event.TYPE_CANCEL);
    }

    /**
     * Sets the {@code oncancel} event handler for this element.
     * @param oncancel the {@code oncancel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOncancel(final Object oncancel) {
        setEventHandler(Event.TYPE_CANCEL, oncancel);
    }

    /**
     * Returns the {@code oncanplay} event handler for this element.
     * @return the {@code oncanplay} event handler for this element
     */
    @JsxGetter
    public Function getOncanplay() {
        return getEventHandler(Event.TYPE_CANPLAY);
    }

    /**
     * Sets the {@code oncanplay} event handler for this element.
     * @param oncanplay the {@code oncanplay} event handler for this element
     */
    @JsxSetter
    public void setOncanplay(final Object oncanplay) {
        setEventHandler(Event.TYPE_CANPLAY, oncanplay);
    }

    /**
     * Returns the {@code oncanplaythrough} event handler for this element.
     * @return the {@code oncanplaythrough} event handler for this element
     */
    @JsxGetter
    public Function getOncanplaythrough() {
        return getEventHandler(Event.TYPE_CANPLAYTHROUGH);
    }

    /**
     * Sets the {@code oncanplaythrough} event handler for this element.
     * @param oncanplaythrough the {@code oncanplaythrough} event handler for this element
     */
    @JsxSetter
    public void setOncanplaythrough(final Object oncanplaythrough) {
        setEventHandler(Event.TYPE_CANPLAYTHROUGH, oncanplaythrough);
    }

    /**
     * Returns the {@code onchange} event handler for this element.
     * @return the {@code onchange} event handler for this element
     */
    @JsxGetter
    public Function getOnchange() {
        return getEventHandler(Event.TYPE_CHANGE);
    }

    /**
     * Sets the {@code onchange} event handler for this element.
     * @param onchange the {@code onchange} event handler for this element
     */
    @JsxSetter
    public void setOnchange(final Object onchange) {
        setEventHandler(Event.TYPE_CHANGE, onchange);
    }

    /**
     * Returns the {@code onclose} event handler for this element.
     * @return the {@code onclose} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnclose() {
        return getEventHandler(Event.TYPE_CLOSE);
    }

    /**
     * Sets the {@code onclose} event handler for this element.
     * @param onclose the {@code onclose} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnclose(final Object onclose) {
        setEventHandler(Event.TYPE_CLOSE, onclose);
    }

    /**
     * Returns the {@code oncopy} event handler for this element.
     * @return the {@code oncopy} event handler for this element
     */
    @JsxGetter
    public Function getOncopy() {
        return getEventHandler(Event.TYPE_COPY);
    }

    /**
     * Sets the {@code oncopy} event handler for this element.
     * @param oncopy the {@code oncopy} event handler for this element
     */
    @JsxSetter
    public void setOncopy(final Object oncopy) {
        setEventHandler(Event.TYPE_COPY, oncopy);
    }

    /**
     * Returns the {@code oncuechange} event handler for this element.
     * @return the {@code oncuechange} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOncuechange() {
        return getEventHandler(Event.TYPE_CUECHANGE);
    }

    /**
     * Sets the {@code oncuechange} event handler for this element.
     * @param oncuechange the {@code oncuechange} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOncuechange(final Object oncuechange) {
        setEventHandler(Event.TYPE_CUECHANGE, oncuechange);
    }

    /**
     * Returns the {@code oncut} event handler for this element.
     * @return the {@code oncut} event handler for this element
     */
    @JsxGetter
    public Function getOncut() {
        return getEventHandler(Event.TYPE_CUT);
    }

    /**
     * Sets the {@code oncut} event handler for this element.
     * @param oncut the {@code oncut} event handler for this element
     */
    @JsxSetter
    public void setOncut(final Object oncut) {
        setEventHandler(Event.TYPE_CUT, oncut);
    }

    /**
     * Returns the {@code ondrag} event handler for this element.
     * @return the {@code ondrag} event handler for this element
     */
    @JsxGetter
    public Function getOndrag() {
        return getEventHandler(Event.TYPE_DRAG);
    }

    /**
     * Sets the {@code ondrag} event handler for this element.
     * @param ondrag the {@code ondrag} event handler for this element
     */
    @JsxSetter
    public void setOndrag(final Object ondrag) {
        setEventHandler(Event.TYPE_DRAG, ondrag);
    }

    /**
     * Returns the {@code ondragend} event handler for this element.
     * @return the {@code ondragend} event handler for this element
     */
    @JsxGetter
    public Function getOndragend() {
        return getEventHandler(Event.TYPE_DRAGEND);
    }

    /**
     * Sets the {@code ondragend} event handler for this element.
     * @param ondragend the {@code ondragend} event handler for this element
     */
    @JsxSetter
    public void setOndragend(final Object ondragend) {
        setEventHandler(Event.TYPE_DRAGEND, ondragend);
    }

    /**
     * Returns the {@code ondragenter} event handler for this element.
     * @return the {@code ondragenter} event handler for this element
     */
    @JsxGetter
    public Function getOndragenter() {
        return getEventHandler(Event.TYPE_DRAGENTER);
    }

    /**
     * Sets the {@code ondragenter} event handler for this element.
     * @param ondragenter the {@code ondragenter} event handler for this element
     */
    @JsxSetter
    public void setOndragenter(final Object ondragenter) {
        setEventHandler(Event.TYPE_DRAGENTER, ondragenter);
    }

    /**
     * Returns the {@code ondragleave} event handler for this element.
     * @return the {@code ondragleave} event handler for this element
     */
    @JsxGetter
    public Function getOndragleave() {
        return getEventHandler(Event.TYPE_DRAGLEAVE);
    }

    /**
     * Sets the {@code ondragleave} event handler for this element.
     * @param ondragleave the {@code ondragleave} event handler for this element
     */
    @JsxSetter
    public void setOndragleave(final Object ondragleave) {
        setEventHandler(Event.TYPE_DRAGLEAVE, ondragleave);
    }

    /**
     * Returns the {@code ondragover} event handler for this element.
     * @return the {@code ondragover} event handler for this element
     */
    @JsxGetter
    public Function getOndragover() {
        return getEventHandler(Event.TYPE_DRAGOVER);
    }

    /**
     * Sets the {@code ondragover} event handler for this element.
     * @param ondragover the {@code ondragover} event handler for this element
     */
    @JsxSetter
    public void setOndragover(final Object ondragover) {
        setEventHandler(Event.TYPE_DRAGOVER, ondragover);
    }

    /**
     * Returns the {@code ondragstart} event handler for this element.
     * @return the {@code ondragstart} event handler for this element
     */
    @JsxGetter
    public Function getOndragstart() {
        return getEventHandler(Event.TYPE_DRAGSTART);
    }

    /**
     * Sets the {@code ondragstart} event handler for this element.
     * @param ondragstart the {@code ondragstart} event handler for this element
     */
    @JsxSetter
    public void setOndragstart(final Object ondragstart) {
        setEventHandler(Event.TYPE_DRAGSTART, ondragstart);
    }

    /**
     * Returns the {@code ondrop} event handler for this element.
     * @return the {@code ondrop} event handler for this element
     */
    @JsxGetter
    public Function getOndrop() {
        return getEventHandler(Event.TYPE_DROP);
    }

    /**
     * Sets the {@code ondrop} event handler for this element.
     * @param ondrop the {@code ondrop} event handler for this element
     */
    @JsxSetter
    public void setOndrop(final Object ondrop) {
        setEventHandler(Event.TYPE_DROP, ondrop);
    }

    /**
     * Returns the {@code ondurationchange} event handler for this element.
     * @return the {@code ondurationchange} event handler for this element
     */
    @JsxGetter
    public Function getOndurationchange() {
        return getEventHandler(Event.TYPE_DURATIONCHANGE);
    }

    /**
     * Sets the {@code ondurationchange} event handler for this element.
     * @param ondurationchange the {@code ondurationchange} event handler for this element
     */
    @JsxSetter
    public void setOndurationchange(final Object ondurationchange) {
        setEventHandler(Event.TYPE_DURATIONCHANGE, ondurationchange);
    }

    /**
     * Returns the {@code onemptied} event handler for this element.
     * @return the {@code onemptied} event handler for this element
     */
    @JsxGetter
    public Function getOnemptied() {
        return getEventHandler(Event.TYPE_EMPTIED);
    }

    /**
     * Sets the {@code onemptied} event handler for this element.
     * @param onemptied the {@code onemptied} event handler for this element
     */
    @JsxSetter
    public void setOnemptied(final Object onemptied) {
        setEventHandler(Event.TYPE_EMPTIED, onemptied);
    }

    /**
     * Returns the {@code onended} event handler for this element.
     * @return the {@code onended} event handler for this element
     */
    @JsxGetter
    public Function getOnended() {
        return getEventHandler(Event.TYPE_ENDED);
    }

    /**
     * Sets the {@code onended} event handler for this element.
     * @param onended the {@code onended} event handler for this element
     */
    @JsxSetter
    public void setOnended(final Object onended) {
        setEventHandler(Event.TYPE_ENDED, onended);
    }

    /**
     * Returns the {@code ongotpointercapture} event handler for this element.
     * @return the {@code ongotpointercapture} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOngotpointercapture() {
        return getEventHandler(Event.TYPE_GOTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code ongotpointercapture} event handler for this element.
     * @param ongotpointercapture the {@code ongotpointercapture} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOngotpointercapture(final Object ongotpointercapture) {
        setEventHandler(Event.TYPE_GOTPOINTERCAPTURE, ongotpointercapture);
    }

    /**
     * Returns the {@code oninvalid} event handler for this element.
     * @return the {@code oninvalid} event handler for this element
     */
    @JsxGetter
    public Function getOninvalid() {
        return getEventHandler(Event.TYPE_INVALID);
    }

    /**
     * Sets the {@code oninvalid} event handler for this element.
     * @param oninvalid the {@code oninvalid} event handler for this element
     */
    @JsxSetter
    public void setOninvalid(final Object oninvalid) {
        setEventHandler(Event.TYPE_INVALID, oninvalid);
    }

    /**
     * Returns the {@code onload} event handler for this element.
     * @return the {@code onload} event handler for this element
     */
    @JsxGetter
    public Function getOnload() {
        return getEventHandler(Event.TYPE_LOAD);
    }

    /**
     * Sets the {@code onload} event handler for this element.
     * @param onload the {@code onload} event handler for this element
     */
    @JsxSetter
    public void setOnload(final Object onload) {
        setEventHandler(Event.TYPE_LOAD, onload);
    }

    /**
     * Returns the {@code onloadeddata} event handler for this element.
     * @return the {@code onloadeddata} event handler for this element
     */
    @JsxGetter
    public Function getOnloadeddata() {
        return getEventHandler(Event.TYPE_LOADEDDATA);
    }

    /**
     * Sets the {@code onloadeddata} event handler for this element.
     * @param onloadeddata the {@code onloadeddata} event handler for this element
     */
    @JsxSetter
    public void setOnloadeddata(final Object onloadeddata) {
        setEventHandler(Event.TYPE_LOADEDDATA, onloadeddata);
    }

    /**
     * Returns the {@code onloadedmetadata} event handler for this element.
     * @return the {@code onloadedmetadata} event handler for this element
     */
    @JsxGetter
    public Function getOnloadedmetadata() {
        return getEventHandler(Event.TYPE_LOADEDMETADATA);
    }

    /**
     * Sets the {@code onloadedmetadata} event handler for this element.
     * @param onloadedmetadata the {@code onloadedmetadata} event handler for this element
     */
    @JsxSetter
    public void setOnloadedmetadata(final Object onloadedmetadata) {
        setEventHandler(Event.TYPE_LOADEDMETADATA, onloadedmetadata);
    }

    /**
     * Returns the {@code onloadstart} event handler for this element.
     * @return the {@code onloadstart} event handler for this element
     */
    @JsxGetter
    public Function getOnloadstart() {
        return getEventHandler(Event.TYPE_LOAD_START);
    }

    /**
     * Sets the {@code onloadstart} event handler for this element.
     * @param onloadstart the {@code onloadstart} event handler for this element
     */
    @JsxSetter
    public void setOnloadstart(final Object onloadstart) {
        setEventHandler(Event.TYPE_LOAD_START, onloadstart);
    }

    /**
     * Returns the {@code onlostpointercapture} event handler for this element.
     * @return the {@code onlostpointercapture} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnlostpointercapture() {
        return getEventHandler(Event.TYPE_LOSTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code onlostpointercapture} event handler for this element.
     * @param onlostpointercapture the {@code onlostpointercapture} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnlostpointercapture(final Object onlostpointercapture) {
        setEventHandler(Event.TYPE_LOSTPOINTERCAPTURE, onlostpointercapture);
    }

    /**
     * Returns the {@code onmouseenter} event handler for this element.
     * @return the {@code onmouseenter} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseenter() {
        return getEventHandler(Event.TYPE_MOUDEENTER);
    }

    /**
     * Sets the {@code onmouseenter} event handler for this element.
     * @param onmouseenter the {@code onmouseenter} event handler for this element
     */
    @JsxSetter
    public void setOnmouseenter(final Object onmouseenter) {
        setEventHandler(Event.TYPE_MOUDEENTER, onmouseenter);
    }

    /**
     * Returns the {@code onmouseleave} event handler for this element.
     * @return the {@code onmouseleave} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseleave() {
        return getEventHandler(Event.TYPE_MOUSELEAVE);
    }

    /**
     * Sets the {@code onmouseleave} event handler for this element.
     * @param onmouseleave the {@code onmouseleave} event handler for this element
     */
    @JsxSetter
    public void setOnmouseleave(final Object onmouseleave) {
        setEventHandler(Event.TYPE_MOUSELEAVE, onmouseleave);
    }

    /**
     * Returns the {@code onmousewheel} event handler for this element.
     * @return the {@code onmousewheel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnmousewheel() {
        return getEventHandler(Event.TYPE_MOUSEWHEEL);
    }

    /**
     * Sets the {@code onmousewheel} event handler for this element.
     * @param onmousewheel the {@code onmousewheel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnmousewheel(final Object onmousewheel) {
        setEventHandler(Event.TYPE_MOUSEWHEEL, onmousewheel);
    }

    /**
     * Returns the {@code onpaste} event handler for this element.
     * @return the {@code onpaste} event handler for this element
     */
    @JsxGetter
    public Function getOnpaste() {
        return getEventHandler(Event.TYPE_PASTE);
    }

    /**
     * Sets the {@code onpaste} event handler for this element.
     * @param onpaste the {@code onpaste} event handler for this element
     */
    @JsxSetter
    public void setOnpaste(final Object onpaste) {
        setEventHandler(Event.TYPE_PASTE, onpaste);
    }

    /**
     * Returns the {@code onpause} event handler for this element.
     * @return the {@code onpause} event handler for this element
     */
    @JsxGetter
    public Function getOnpause() {
        return getEventHandler(Event.TYPE_PAUSE);
    }

    /**
     * Sets the {@code onpause} event handler for this element.
     * @param onpause the {@code onpause} event handler for this element
     */
    @JsxSetter
    public void setOnpause(final Object onpause) {
        setEventHandler(Event.TYPE_PAUSE, onpause);
    }

    /**
     * Returns the {@code onplay} event handler for this element.
     * @return the {@code onplay} event handler for this element
     */
    @JsxGetter
    public Function getOnplay() {
        return getEventHandler(Event.TYPE_PLAY);
    }

    /**
     * Sets the {@code onplay} event handler for this element.
     * @param onplay the {@code onplay} event handler for this element
     */
    @JsxSetter
    public void setOnplay(final Object onplay) {
        setEventHandler(Event.TYPE_PLAY, onplay);
    }

    /**
     * Returns the {@code onplaying} event handler for this element.
     * @return the {@code onplaying} event handler for this element
     */
    @JsxGetter
    public Function getOnplaying() {
        return getEventHandler(Event.TYPE_PLAYING);
    }

    /**
     * Sets the {@code onplaying} event handler for this element.
     * @param onplaying the {@code onplaying} event handler for this element
     */
    @JsxSetter
    public void setOnplaying(final Object onplaying) {
        setEventHandler(Event.TYPE_PLAYING, onplaying);
    }

    /**
     * Returns the {@code onpointercancel} event handler for this element.
     * @return the {@code onpointercancel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointercancel() {
        return getEventHandler(Event.TYPE_POINTERCANCEL);
    }

    /**
     * Sets the {@code onpointercancel} event handler for this element.
     * @param onpointercancel the {@code onpointercancel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointercancel(final Object onpointercancel) {
        setEventHandler(Event.TYPE_POINTERCANCEL, onpointercancel);
    }

    /**
     * Returns the {@code onpointerdown} event handler for this element.
     * @return the {@code onpointerdown} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerdown() {
        return getEventHandler(Event.TYPE_POINTERDOWN);
    }

    /**
     * Sets the {@code onpointerdown} event handler for this element.
     * @param onpointerdown the {@code onpointerdown} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerdown(final Object onpointerdown) {
        setEventHandler(Event.TYPE_POINTERDOWN, onpointerdown);
    }

    /**
     * Returns the {@code onpointerenter} event handler for this element.
     * @return the {@code onpointerenter} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerenter() {
        return getEventHandler(Event.TYPE_POINTERENTER);
    }

    /**
     * Sets the {@code onpointerenter} event handler for this element.
     * @param onpointerenter the {@code onpointerenter} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerenter(final Object onpointerenter) {
        setEventHandler(Event.TYPE_POINTERENTER, onpointerenter);
    }

    /**
     * Returns the {@code onpointerleave} event handler for this element.
     * @return the {@code onpointerleave} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerleave() {
        return getEventHandler(Event.TYPE_POINTERLEAVE);
    }

    /**
     * Sets the {@code onpointerleave} event handler for this element.
     * @param onpointerleave the {@code onpointerleave} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerleave(final Object onpointerleave) {
        setEventHandler(Event.TYPE_POINTERLEAVE, onpointerleave);
    }

    /**
     * Returns the {@code onpointerlockchange} event handler for this element.
     * @return the {@code onpointerlockchange} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerlockchange() {
        return getEventHandler(Event.TYPE_POINTERLOCKCHANGE);
    }

    /**
     * Sets the {@code onpointerlockchange} event handler for this element.
     * @param onpointerlockchange the {@code onpointerlockchange} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerlockchange(final Object onpointerlockchange) {
        setEventHandler(Event.TYPE_POINTERLOCKCHANGE, onpointerlockchange);
    }

    /**
     * Returns the {@code onpointerlockerror} event handler for this element.
     * @return the {@code onpointerlockerror} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerlockerror() {
        return getEventHandler(Event.TYPE_POINTERLOCKERROR);
    }

    /**
     * Sets the {@code onpointerlockerror} event handler for this element.
     * @param onpointerlockerror the {@code onpointerlockerror} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerlockerror(final Object onpointerlockerror) {
        setEventHandler(Event.TYPE_POINTERLOCKERROR, onpointerlockerror);
    }

    /**
     * Returns the {@code onpointermove} event handler for this element.
     * @return the {@code onpointermove} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointermove() {
        return getEventHandler(Event.TYPE_POINTERMOVE);
    }

    /**
     * Sets the {@code onpointermove} event handler for this element.
     * @param onpointermove the {@code onpointermove} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointermove(final Object onpointermove) {
        setEventHandler(Event.TYPE_POINTERMOVE, onpointermove);
    }

    /**
     * Returns the {@code onpointerout} event handler for this element.
     * @return the {@code onpointerout} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerout() {
        return getEventHandler(Event.TYPE_POINTEROUT);
    }

    /**
     * Sets the {@code onpointerout} event handler for this element.
     * @param onpointerout the {@code onpointerout} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerout(final Object onpointerout) {
        setEventHandler(Event.TYPE_POINTEROUT, onpointerout);
    }

    /**
     * Returns the {@code onpointerover} event handler for this element.
     * @return the {@code onpointerover} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerover() {
        return getEventHandler(Event.TYPE_POINTEROVER);
    }

    /**
     * Sets the {@code onpointerover} event handler for this element.
     * @param onpointerover the {@code onpointerover} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerover(final Object onpointerover) {
        setEventHandler(Event.TYPE_POINTEROVER, onpointerover);
    }

    /**
     * Returns the {@code onpointerup} event handler for this element.
     * @return the {@code onpointerup} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerup() {
        return getEventHandler(Event.TYPE_POINTERUP);
    }

    /**
     * Sets the {@code onpointerup} event handler for this element.
     * @param onpointerup the {@code onpointerup} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerup(final Object onpointerup) {
        setEventHandler(Event.TYPE_POINTERUP, onpointerup);
    }

    /**
     * Returns the {@code onprogress} event handler for this element.
     * @return the {@code onprogress} event handler for this element
     */
    @JsxGetter
    public Function getOnprogress() {
        return getEventHandler(Event.TYPE_PROGRESS);
    }

    /**
     * Sets the {@code onprogress} event handler for this element.
     * @param onprogress the {@code onprogress} event handler for this element
     */
    @JsxSetter
    public void setOnprogress(final Object onprogress) {
        setEventHandler(Event.TYPE_PROGRESS, onprogress);
    }

    /**
     * Returns the {@code onratechange} event handler for this element.
     * @return the {@code onratechange} event handler for this element
     */
    @JsxGetter
    public Function getOnratechange() {
        return getEventHandler(Event.TYPE_RATECHANGE);
    }

    /**
     * Sets the {@code onratechange} event handler for this element.
     * @param onratechange the {@code onratechange} event handler for this element
     */
    @JsxSetter
    public void setOnratechange(final Object onratechange) {
        setEventHandler(Event.TYPE_RATECHANGE, onratechange);
    }

    /**
     * Returns the {@code onreadystatechange} event handler for this element.
     * @return the {@code onreadystatechange} event handler for this element
     */
    @JsxGetter
    public Function getOnreadystatechange() {
        return getEventHandler(Event.TYPE_READY_STATE_CHANGE);
    }

    /**
     * Sets the {@code onreadystatechange} event handler for this element.
     * @param onreadystatechange the {@code onreadystatechange} event handler for this element
     */
    @JsxSetter
    public void setOnreadystatechange(final Object onreadystatechange) {
        setEventHandler(Event.TYPE_READY_STATE_CHANGE, onreadystatechange);
    }

    /**
     * Returns the {@code onreset} event handler for this element.
     * @return the {@code onreset} event handler for this element
     */
    @JsxGetter
    public Function getOnreset() {
        return getEventHandler(Event.TYPE_RESET);
    }

    /**
     * Sets the {@code onreset} event handler for this element.
     * @param onreset the {@code onreset} event handler for this element
     */
    @JsxSetter
    public void setOnreset(final Object onreset) {
        setEventHandler(Event.TYPE_RESET, onreset);
    }

    /**
     * Returns the {@code onscroll} event handler for this element.
     * @return the {@code onscroll} event handler for this element
     */
    @JsxGetter
    public Function getOnscroll() {
        return getEventHandler(Event.TYPE_SCROLL);
    }

    /**
     * Sets the {@code onscroll} event handler for this element.
     * @param onscroll the {@code onscroll} event handler for this element
     */
    @JsxSetter
    public void setOnscroll(final Object onscroll) {
        setEventHandler(Event.TYPE_SCROLL, onscroll);
    }

    /**
     * Returns the {@code onsearch} event handler for this element.
     * @return the {@code onsearch} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnsearch() {
        return getEventHandler(Event.TYPE_SEARCH);
    }

    /**
     * Sets the {@code onsearch} event handler for this element.
     * @param onsearch the {@code onsearch} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnsearch(final Object onsearch) {
        setEventHandler(Event.TYPE_SEARCH, onsearch);
    }

    /**
     * Returns the {@code onseeked} event handler for this element.
     * @return the {@code onseeked} event handler for this element
     */
    @JsxGetter
    public Function getOnseeked() {
        return getEventHandler(Event.TYPE_SEEKED);
    }

    /**
     * Sets the {@code onseeked} event handler for this element.
     * @param onseeked the {@code onseeked} event handler for this element
     */
    @JsxSetter
    public void setOnseeked(final Object onseeked) {
        setEventHandler(Event.TYPE_SEEKED, onseeked);
    }

    /**
     * Returns the {@code onseeking} event handler for this element.
     * @return the {@code onseeking} event handler for this element
     */
    @JsxGetter
    public Function getOnseeking() {
        return getEventHandler(Event.TYPE_SEEKING);
    }

    /**
     * Sets the {@code onseeking} event handler for this element.
     * @param onseeking the {@code onseeking} event handler for this element
     */
    @JsxSetter
    public void setOnseeking(final Object onseeking) {
        setEventHandler(Event.TYPE_SEEKING, onseeking);
    }

    /**
     * Returns the {@code onselect} event handler for this element.
     * @return the {@code onselect} event handler for this element
     */
    @JsxGetter
    public Function getOnselect() {
        return getEventHandler(Event.TYPE_SELECT);
    }

    /**
     * Sets the {@code onselect} event handler for this element.
     * @param onselect the {@code onselect} event handler for this element
     */
    @JsxSetter
    public void setOnselect(final Object onselect) {
        setEventHandler(Event.TYPE_SELECT, onselect);
    }

    /**
     * Returns the {@code onselectionchange} event handler for this element.
     * @return the {@code onselectionchange} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnselectionchange() {
        return getEventHandler(Event.TYPE_SELECTIONCHANGE);
    }

    /**
     * Sets the {@code onselectionchange} event handler for this element.
     * @param onselectionchange the {@code onselectionchange} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnselectionchange(final Object onselectionchange) {
        setEventHandler(Event.TYPE_SELECTIONCHANGE, onselectionchange);
    }

    /**
     * Returns the {@code onselectstart} event handler for this element.
     * @return the {@code onselectstart} event handler for this element
     */
    @JsxGetter
    public Function getOnselectstart() {
        return getEventHandler(Event.TYPE_SELECTSTART);
    }

    /**
     * Sets the {@code onselectstart} event handler for this element.
     * @param onselectstart the {@code onselectstart} event handler for this element
     */
    @JsxSetter
    public void setOnselectstart(final Object onselectstart) {
        setEventHandler(Event.TYPE_SELECTSTART, onselectstart);
    }

    /**
     * Returns the {@code onstalled} event handler for this element.
     * @return the {@code onstalled} event handler for this element
     */
    @JsxGetter
    public Function getOnstalled() {
        return getEventHandler(Event.TYPE_STALLED);
    }

    /**
     * Sets the {@code onstalled} event handler for this element.
     * @param onstalled the {@code onstalled} event handler for this element
     */
    @JsxSetter
    public void setOnstalled(final Object onstalled) {
        setEventHandler(Event.TYPE_STALLED, onstalled);
    }

    /**
     * Returns the {@code onsubmit} event handler for this element.
     * @return the {@code onsubmit} event handler for this element
     */
    @JsxGetter
    public Function getOnsubmit() {
        return getEventHandler(Event.TYPE_SUBMIT);
    }

    /**
     * Sets the {@code onsubmit} event handler for this element.
     * @param onsubmit the {@code onsubmit} event handler for this element
     */
    @JsxSetter
    public void setOnsubmit(final Object onsubmit) {
        setEventHandler(Event.TYPE_SUBMIT, onsubmit);
    }

    /**
     * Returns the {@code onsuspend} event handler for this element.
     * @return the {@code onsuspend} event handler for this element
     */
    @JsxGetter
    public Function getOnsuspend() {
        return getEventHandler(Event.TYPE_SUSPEND);
    }

    /**
     * Sets the {@code onsuspend} event handler for this element.
     * @param onsuspend the {@code onsuspend} event handler for this element
     */
    @JsxSetter
    public void setOnsuspend(final Object onsuspend) {
        setEventHandler(Event.TYPE_SUSPEND, onsuspend);
    }

    /**
     * Returns the {@code ontimeupdate} event handler for this element.
     * @return the {@code ontimeupdate} event handler for this element
     */
    @JsxGetter
    public Function getOntimeupdate() {
        return getEventHandler(Event.TYPE_TIMEUPDATE);
    }

    /**
     * Sets the {@code ontimeupdate} event handler for this element.
     * @param ontimeupdate the {@code ontimeupdate} event handler for this element
     */
    @JsxSetter
    public void setOntimeupdate(final Object ontimeupdate) {
        setEventHandler(Event.TYPE_TIMEUPDATE, ontimeupdate);
    }

    /**
     * Returns the {@code ontoggle} event handler for this element.
     * @return the {@code ontoggle} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOntoggle() {
        return getEventHandler(Event.TYPE_TOGGLE);
    }

    /**
     * Sets the {@code ontoggle} event handler for this element.
     * @param ontoggle the {@code ontoggle} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOntoggle(final Object ontoggle) {
        setEventHandler(Event.TYPE_TOGGLE, ontoggle);
    }

    /**
     * Returns the {@code onvolumechange} event handler for this element.
     * @return the {@code onvolumechange} event handler for this element
     */
    @JsxGetter
    public Function getOnvolumechange() {
        return getEventHandler(Event.TYPE_VOLUMECHANGE);
    }

    /**
     * Sets the {@code onvolumechange} event handler for this element.
     * @param onvolumechange the {@code onvolumechange} event handler for this element
     */
    @JsxSetter
    public void setOnvolumechange(final Object onvolumechange) {
        setEventHandler(Event.TYPE_VOLUMECHANGE, onvolumechange);
    }

    /**
     * Returns the {@code onwaiting} event handler for this element.
     * @return the {@code onwaiting} event handler for this element
     */
    @JsxGetter
    public Function getOnwaiting() {
        return getEventHandler(Event.TYPE_WAITING);
    }

    /**
     * Sets the {@code onwaiting} event handler for this element.
     * @param onwaiting the {@code onwaiting} event handler for this element
     */
    @JsxSetter
    public void setOnwaiting(final Object onwaiting) {
        setEventHandler(Event.TYPE_WAITING, onwaiting);
    }

    /**
     * Returns the {@code onwebkitfullscreenchange} event handler for this element.
     * @return the {@code onwebkitfullscreenchange} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnwebkitfullscreenchange() {
        return getEventHandler(Event.TYPE_WEBKITFULLSCREENCHANGE);
    }

    /**
     * Sets the {@code onwebkitfullscreenchange} event handler for this element.
     * @param onwebkitfullscreenchange the {@code onwebkitfullscreenchange} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnwebkitfullscreenchange(final Object onwebkitfullscreenchange) {
        setEventHandler(Event.TYPE_WEBKITFULLSCREENCHANGE, onwebkitfullscreenchange);
    }

    /**
     * Returns the {@code onwebkitfullscreenerror} event handler for this element.
     * @return the {@code onwebkitfullscreenerror} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnwebkitfullscreenerror() {
        return getEventHandler(Event.TYPE_WEBKITFULLSCREENERROR);
    }

    /**
     * Sets the {@code onwebkitfullscreenerror} event handler for this element.
     * @param onwebkitfullscreenerror the {@code onwebkitfullscreenerror} event handler for this element
     */
    @JsxSetter
    public void setOnwebkitfullscreenerror(final Object onwebkitfullscreenerror) {
        setEventHandler(Event.TYPE_WEBKITFULLSCREENERROR, onwebkitfullscreenerror);
    }

    /**
     * Returns the {@code onwheel} event handler for this element.
     * @return the {@code onwheel} event handler for this element
     */
    @JsxGetter
    public Function getOnwheel() {
        return getEventHandler(Event.TYPE_WHEEL);
    }

    /**
     * Sets the {@code onwheel} event handler for this element.
     * @param onwheel the {@code onwheel} event handler for this element
     */
    @JsxSetter
    public void setOnwheel(final Object onwheel) {
        setEventHandler(Event.TYPE_WHEEL, onwheel);
    }

    /**
     * Returns the {@code onafterscriptexecute} event handler for this element.
     * @return the {@code onafterscriptexecute} event handler for this element
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnafterscriptexecute() {
        return getEventHandler(Event.TYPE_AFTERSCRIPTEXECUTE);
    }

    /**
     * Sets the {@code onafterscriptexecute} event handler for this element.
     * @param onafterscriptexecute the {@code onafterscriptexecute} event handler for this element
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnafterscriptexecute(final Object onafterscriptexecute) {
        setEventHandler(Event.TYPE_AFTERSCRIPTEXECUTE, onafterscriptexecute);
    }

    /**
     * Returns the {@code onbeforescriptexecute} event handler for this element.
     * @return the {@code onbeforescriptexecute} event handler for this element
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnbeforescriptexecute() {
        return getEventHandler(Event.TYPE_BEFORESCRIPTEXECUTE);
    }

    /**
     * Sets the {@code onbeforescriptexecute} event handler for this element.
     * @param onbeforescriptexecute the {@code onbeforescriptexecute} event handler for this element
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnbeforescriptexecute(final Object onbeforescriptexecute) {
        setEventHandler(Event.TYPE_BEFORESCRIPTEXECUTE, onbeforescriptexecute);
    }

    /**
     * Returns the {@code onmozfullscreenchange} event handler for this element.
     * @return the {@code onmozfullscreenchange} event handler for this element
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnmozfullscreenchange() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENCHANGE);
    }

    /**
     * Sets the {@code onmozfullscreenchange} event handler for this element.
     * @param onmozfullscreenchange the {@code onmozfullscreenchange} event handler for this element
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnmozfullscreenchange(final Object onmozfullscreenchange) {
        setEventHandler(Event.TYPE_MOZFULLSCREENCHANGE, onmozfullscreenchange);
    }

    /**
     * Returns the {@code onmozfullscreenerror} event handler for this element.
     * @return the {@code onmozfullscreenerror} event handler for this element
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnmozfullscreenerror() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENERROR);
    }

    /**
     * Sets the {@code onmozfullscreenerror} event handler for this element.
     * @param onmozfullscreenerror the {@code onmozfullscreenerror} event handler for this element
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnmozfullscreenerror(final Object onmozfullscreenerror) {
        setEventHandler(Event.TYPE_MOZFULLSCREENERROR, onmozfullscreenerror);
    }

    /**
     * @return the {@code currentScript}
     */
    @JsxGetter
    public ScriptableObject getCurrentScript() {
        return currentScript_;
    }

    /**
     * @param script the {@code currentScript}
     */
    public void setCurrentScript(final ScriptableObject script) {
        currentScript_ = script;
    }

    /**
     * @return the {@code FontFaceSet}
     */
    @JsxGetter
    public ScriptableObject getFonts() {
        if (fonts_ == null) {
            final FontFaceSet fonts = new FontFaceSet();
            fonts.setParentScope(getWindow());
            fonts.setPrototype(getPrototype(fonts.getClass()));
            fonts_ = fonts;
        }
        return fonts_;
    }

    /**
     * Returns the value of the {@code all} property.
     * @return the value of the {@code all} property
     */
    @JsxGetter
    public HTMLAllCollection getAll() {
        final HTMLAllCollection all = new HTMLAllCollection(getDomNodeOrDie());
        all.setAvoidObjectDetection(true);
        all.setIsMatchingPredicate((Predicate<DomNode> & Serializable) node -> true);
        return all;
    }

    /**
     * Returns the element with the specified ID, as long as it is an HTML element; {@code null} otherwise.
     * @param id the ID to search for
     * @return the element with the specified ID, as long as it is an HTML element; {@code null} otherwise
     */
    @JsxFunction
    public HtmlUnitScriptable getElementById(final String id) {
        final DomNode domNode = getDomNodeOrDie();
        for (final DomElement descendant : domNode.getDomElementDescendants()) {
            if (id.equals(descendant.getId())) {
                return descendant.getScriptableObject();
            }
        }
        return null;
    }

    /**
     * Creates a new ProcessingInstruction.
     * @param target the target
     * @param data the data
     * @return the new ProcessingInstruction
     */
    @JsxFunction
    public HtmlUnitScriptable createProcessingInstruction(final String target, final String data) {
        final ProcessingInstruction node = getPage().createProcessingInstruction(target, data);
        return getScriptableFor(node);
    }

    /**
     * Creates a new createCDATASection.
     * @param data the data
     * @return the new CDATASection
     */
    @JsxFunction
    public HtmlUnitScriptable createCDATASection(final String data) {
        final CDATASection node = getPage().createCDATASection(data);
        return getScriptableFor(node);
    }

    /**
     * Does... nothing.
     * @see <a href="https://developer.mozilla.org/en/DOM/document.clear">Mozilla doc</a>
     */
    @JsxFunction
    public void clear() {
        // nothing
    }

    /**
     * {@inheritDoc}
     */
    @JsxFunction
    @Override
    public boolean contains(final Object element) {
        return getDocumentElement().contains(element);
    }

    /**
     * Generate and return the URL for the given blob.
     * @param blob the Blob containing the data
     * @return the URL {@link org.htmlunit.javascript.host.URL#createObjectURL(Object)}
     */
    public String generateBlobUrl(final Blob blob) {
        final URL url = getPage().getUrl();

        String origin = "null";
        if (!UrlUtils.URL_ABOUT_BLANK.equals(url)) {
            origin = url.getProtocol() + "://" + url.getAuthority();
        }

        final String blobUrl = "blob:" + origin + "/" + UUID.randomUUID();
        blobUrl2Blobs_.put(blobUrl, blob);
        return blobUrl;
    }

    /**
     * @param url the url to resolve
     * @return the Blob for the given URL or {@code null} if not found.
     */
    public Blob resolveBlobUrl(final String url) {
        return blobUrl2Blobs_.get(url);
    }

    /**
     * Revokes the URL for the given blob.
     * @param url the url to revoke {@link org.htmlunit.javascript.host.URL#revokeObjectURL(Scriptable)}
     */
    public void revokeBlobUrl(final String url) {
        blobUrl2Blobs_.remove(url);
    }
}
