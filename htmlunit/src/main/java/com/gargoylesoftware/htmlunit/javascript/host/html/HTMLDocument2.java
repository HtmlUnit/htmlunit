/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONCLOSE_DOCUMENT_CREATE_NOT_SUPPORTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_TYPE_BEFOREUNLOADEVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_TYPE_HASHCHANGEEVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_TYPE_KEY_EVENTS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_TYPE_POINTEREVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_TYPE_PROGRESSEVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCUMENT_CREATE_ATTRUBUTE_LOWER_CASE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCUMENT_FORMS_FUNCTION_SUPPORTED;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.NashornJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnly;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnlyStatus;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document2;
import com.gargoylesoftware.htmlunit.javascript.host.event.BeforeUnloadEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.CloseEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.CustomEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event2;
import com.gargoylesoftware.htmlunit.javascript.host.event.HashChangeEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.MutationEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.PopStateEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.ProgressEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.ECMAErrors;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

public class HTMLDocument2 extends Document2 {

    private static final Log LOG = LogFactory.getLog(HTMLDocument2.class);

    /**
     * Map<String, Class> which maps strings a caller may use when calling into
     * {@link #createEvent(String)} to the associated event class. To support a new
     * event creation type, the event type and associated class need to be added into this map in
     * the static initializer. The map is unmodifiable. Any class that is a value in this map MUST
     * have a no-arg constructor.
     */
    /** Contains all supported DOM level 2 events. */
    private static final Map<String, Class<? extends Event2>> SUPPORTED_DOM2_EVENT_TYPE_MAP;
    /** Contains all supported DOM level 3 events. DOM level 2 events are not included. */
    private static final Map<String, Class<? extends Event2>> SUPPORTED_DOM3_EVENT_TYPE_MAP;
    /** Contains all supported vendor specific events. */
    private static final Map<String, Class<? extends Event2>> SUPPORTED_VENDOR_EVENT_TYPE_MAP;

    // all as lowercase for performance
    private static final Set<String> EXECUTE_CMDS_IE = new HashSet<>();
    /** https://developer.mozilla.org/en/Rich-Text_Editing_in_Mozilla#Executing_Commands */
    private static final Set<String> EXECUTE_CMDS_FF = new HashSet<>();
    private static final Set<String> EXECUTE_CMDS_CHROME = new HashSet<>();

    private HTMLCollection2 all_; // has to be a member to have equality (==) working
    private HTMLCollection2 forms_; // has to be a member to have equality (==) working
    private HTMLCollection2 links_; // has to be a member to have equality (==) working
    private HTMLCollection2 images_; // has to be a member to have equality (==) working
    private HTMLCollection2 scripts_; // has to be a member to have equality (==) working
    private HTMLCollection2 anchors_; // has to be a member to have equality (==) working
    private HTMLCollection2 applets_; // has to be a member to have equality (==) working
    private StyleSheetList2 styleSheets_; // has to be a member to have equality (==) working
    private HTMLElement2 activeElement_;

    /** The buffer that will be used for calls to document.write(). */
    private final StringBuilder writeBuffer_ = new StringBuilder();
    private boolean writeInCurrentDocument_ = true;
    private String domain_;
    private String uniqueID_;
    private String lastModified_;
    private String compatMode_;
    private int documentMode_ = -1;

    private boolean closePostponedAction_;

    /** Initializes the supported event type map. */
    static {
        final Map<String, Class<? extends Event2>> dom2EventMap = new HashMap<>();
        dom2EventMap.put("HTMLEvents", Event2.class);
        dom2EventMap.put("MouseEvents", MouseEvent2.class);
        dom2EventMap.put("MutationEvents", MutationEvent2.class);
        dom2EventMap.put("UIEvents", UIEvent2.class);
        SUPPORTED_DOM2_EVENT_TYPE_MAP = Collections.unmodifiableMap(dom2EventMap);

        final Map<String, Class<? extends Event2>> dom3EventMap = new HashMap<>();
        dom3EventMap.put("Event", Event2.class);
        dom3EventMap.put("KeyboardEvent", KeyboardEvent2.class);
        dom3EventMap.put("MouseEvent", MouseEvent2.class);
        dom3EventMap.put("MessageEvent", MessageEvent2.class);
        dom3EventMap.put("MutationEvent", MutationEvent2.class);
        dom3EventMap.put("UIEvent", UIEvent2.class);
        dom3EventMap.put("CustomEvent", CustomEvent2.class);
        dom3EventMap.put("CloseEvent", CloseEvent2.class);
        SUPPORTED_DOM3_EVENT_TYPE_MAP = Collections.unmodifiableMap(dom3EventMap);

        final Map<String, Class<? extends Event2>> additionalEventMap = new HashMap<>();
        additionalEventMap.put("BeforeUnloadEvent", BeforeUnloadEvent2.class);
        additionalEventMap.put("Events", Event2.class);
        additionalEventMap.put("HashChangeEvent", HashChangeEvent2.class);
        additionalEventMap.put("KeyEvents", KeyboardEvent2.class);
        additionalEventMap.put("PointerEvent", PointerEvent2.class);
        additionalEventMap.put("PopStateEvent", PopStateEvent2.class);
        additionalEventMap.put("ProgressEvent", ProgressEvent2.class);
        SUPPORTED_VENDOR_EVENT_TYPE_MAP = Collections.unmodifiableMap(additionalEventMap);

        // commands
        List<String> cmds = Arrays.asList(
            "2D-Position", "AbsolutePosition",
            "BlockDirLTR", "BlockDirRTL", "BrowseMode",
            "ClearAuthenticationCache", "CreateBookmark", "Copy", "Cut",
            "DirLTR", "DirRTL",
            "EditMode",
            "InlineDirLTR", "InlineDirRTL", "InsertButton", "InsertFieldset",
            "InsertIFrame", "InsertInputButton", "InsertInputCheckbox",
            "InsertInputFileUpload", "InsertInputHidden", "InsertInputImage", "InsertInputPassword", "InsertInputRadio",
            "InsertInputReset", "InsertInputSubmit", "InsertInputText", "InsertMarquee",
            "InsertSelectDropdown", "InsertSelectListbox", "InsertTextArea",
            "LiveResize", "MultipleSelection", "Open",
            "OverWrite", "PlayImage",
            "Refresh", "RemoveParaFormat", "SaveAs",
            "SizeToControl", "SizeToControlHeight", "SizeToControlWidth", "Stop", "StopImage",
            "UnBookmark",
            "Paste"
        );
        for (final String cmd : cmds) {
            EXECUTE_CMDS_IE.add(cmd.toLowerCase(Locale.ROOT));
        }

        cmds = Arrays.asList(
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
        );
        for (final String cmd : cmds) {
            EXECUTE_CMDS_IE.add(cmd.toLowerCase(Locale.ROOT));
            if (!"Bold".equals(cmd)) {
                EXECUTE_CMDS_CHROME.add(cmd.toLowerCase(Locale.ROOT));
            }
        }

        cmds = Arrays.asList(
            "backColor", "bold", "contentReadOnly", "copy", "createLink", "cut", "decreaseFontSize", "delete",
            "fontName", "fontSize", "foreColor", "formatBlock", "heading", "hiliteColor", "increaseFontSize",
            "indent", "insertHorizontalRule", "insertHTML", "insertImage", "insertOrderedList", "insertUnorderedList",
            "insertParagraph", "italic",
            "justifyCenter", "JustifyFull", "justifyLeft", "justifyRight", "outdent", "paste", "redo",
            "removeFormat", "selectAll", "strikeThrough", "subscript", "superscript", "underline", "undo", "unlink",
            "useCSS", "styleWithCSS"
        );
        for (final String cmd : cmds) {
            EXECUTE_CMDS_FF.add(cmd.toLowerCase(Locale.ROOT));
            if (!"bold".equals(cmd)) {
                EXECUTE_CMDS_CHROME.add(cmd.toLowerCase(Locale.ROOT));
            }
        }
    }

    public static HTMLDocument2 constructor(final boolean newObj, final Object self) {
        final HTMLDocument2 host = new HTMLDocument2();
        ScriptUtils.initialize(host);
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Returns the HTML page that this document is modeling.
     * @return the HTML page that this document is modeling
     */
    public HtmlPage getPage() {
        return (HtmlPage) getDomNodeOrDie();
    }

    /**
     * Returns the element with the specified ID, or {@code null} if that element could not be found.
     * @param id the ID to search for
     * @return the element, or {@code null} if it could not be found
     */
    @Function
    public Object getElementById(final String id) {
        implicitCloseIfNecessary();
        Object result = null;
        final DomElement domElement = getPage().getElementById(id);
        if (null == domElement) {
            // Just fall through - result is already set to null
            if (LOG.isDebugEnabled()) {
                LOG.debug("getElementById(" + id + "): no DOM node found with this id");
            }
        }
        else {
            final Object jsElement = getScriptableFor(domElement);
            if (jsElement == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getElementById(" + id
                            + ") cannot return a result as there isn't a JavaScript object for the HTML element "
                            + domElement.getClass().getName());
                }
            }
            else {
                result = jsElement;
            }
        }
        return result;
    }

    /**
     * Returns all HTML elements that have a "name" attribute with the specified value.
     *
     * Refer to <a href="http://www.w3.org/TR/DOM-Level-2-HTML/html.html#ID-71555259">
     * The DOM spec</a> for details.
     *
     * @param elementName - value of the {@code name} attribute to look for
     * @return all HTML elements that have a "name" attribute with the specified value
     */
    @Function
    public HTMLCollection2 getElementsByName(final String elementName) {
        implicitCloseIfNecessary();
        if ("null".equals(elementName)) {
            return HTMLCollection2.emptyCollection(getWindow());
        }
        // Null must me changed to '' for proper collection initialization.
        final String expElementName = "null".equals(elementName) ? "" : elementName;

        final HtmlPage page = getPage();
//        final HTMLCollection2 collection = new HTMLCollection2(page, true) {
//            @Override
//            protected List<Object> computeElements() {
//                return new ArrayList<Object>(page.getElementsByName(expElementName));
//            }
//
//            @Override
//            protected EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
//                if ("name".equals(event.getName())) {
//                    return EffectOnCache.RESET;
//                }
//                return EffectOnCache.NONE;
//            }
//        };
//
//        return collection;
        return null;
    }

    /**
     * Closes the document implicitly, i.e. flushes the <tt>document.write</tt> buffer (IE only).
     */
    private void implicitCloseIfNecessary() {
        if (!writeInCurrentDocument_) {
            try {
                close();
            }
            catch (final IOException e) {
                throw ECMAErrors.referenceError(e, "error");
            }
        }
    }

    /**
     * JavaScript function "close".
     *
     * See http://www.whatwg.org/specs/web-apps/current-work/multipage/section-dynamic.html for
     * a good description of the semantics of open(), write(), writeln() and close().
     *
     * @throws IOException if an IO problem occurs
     */
    @Function
    public void close() throws IOException {
        if (writeInCurrentDocument_) {
            LOG.warn("close() called when document is not open.");
        }
        else {
            final HtmlPage page = getPage();
            final URL url = page.getUrl();
            final StringWebResponse webResponse = new StringWebResponse(writeBuffer_.toString(), url);
            webResponse.setFromJavascript(true);
            writeInCurrentDocument_ = true;
            writeBuffer_.setLength(0);

            final WebClient webClient = page.getWebClient();
            final WebWindow window = page.getEnclosingWindow();
            webClient.loadWebResponseInto(webResponse, window);
        }
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
    @Function
    public Event2 createEvent(final String eventType) throws DOMException {
        Class<? extends Event2> clazz = null;
        clazz = SUPPORTED_DOM2_EVENT_TYPE_MAP.get(eventType);
        if (clazz == null) {
            clazz = SUPPORTED_DOM3_EVENT_TYPE_MAP.get(eventType);
            if (CloseEvent2.class == clazz
                    && getBrowserVersion().hasFeature(EVENT_ONCLOSE_DOCUMENT_CREATE_NOT_SUPPORTED)) {
                clazz = null;
            }
        }
        if (clazz == null) {
            if ("Events".equals(eventType)
                || "KeyEvents".equals(eventType) && getBrowserVersion().hasFeature(EVENT_TYPE_KEY_EVENTS)
                || "HashChangeEvent".equals(eventType)
                    && getBrowserVersion().hasFeature(EVENT_TYPE_HASHCHANGEEVENT)
                || "BeforeUnloadEvent".equals(eventType)
                    && getBrowserVersion().hasFeature(EVENT_TYPE_BEFOREUNLOADEVENT)
                || "PointerEvent".equals(eventType)
                    && getBrowserVersion().hasFeature(EVENT_TYPE_POINTEREVENT)
                || "PopStateEvent".equals(eventType)
                || "ProgressEvent".equals(eventType)
                    && getBrowserVersion().hasFeature(EVENT_TYPE_PROGRESSEVENT)) {
                clazz = SUPPORTED_VENDOR_EVENT_TYPE_MAP.get(eventType);
            }
        }
        if (clazz == null) {
            throw new RuntimeException(new DOMException(DOMException.NOT_SUPPORTED_ERR,
                "Event Type is not supported: " + eventType));
        }
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow().getScriptContext());
        final Event2 event = Event2.constructor(true, global);
        event.eventCreated();
        return event;
    }

    /**
     * Retrieves a collection of stylesheet objects representing the style sheets that correspond
     * to each instance of a Link or
     * {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration} object in the document.
     *
     * @return styleSheet collection
     */
    @Getter
    public StyleSheetList2 getStyleSheets() {
        if (styleSheets_ == null) {
            styleSheets_ = new StyleSheetList2(this);
        }
        return styleSheets_;
    }

    /**
     * Returns this document's {@code body} element.
     * @return this document's {@code body} element
     */
    @Getter
    @CanSetReadOnly(CanSetReadOnlyStatus.EXCEPTION)
    public HTMLElement2 getBody() {
        final HtmlPage page = getPage();
        final HtmlElement body = page.getBody();
        if (body != null) {
            return (HTMLElement2) body.getScriptObject2();
        }
        return null;
    }

    /**
     * Returns this document's {@code head} element.
     * @return this document's {@code head} element
     */
    @Getter
    public HTMLElement2 getHead() {
        final HtmlElement head = getPage().getHead();
        if (head != null) {
            return (HTMLElement2) head.getScriptObject2();
        }
        return null;
    }

    /**
     * Returns this document's title.
     * @return this document's title
     */
    @Getter
    public String getTitle() {
        return getPage().getTitleText();
    }

    /**
     * Sets this document's title.
     * @param title the new title
     */
    @Setter
    public void setTitle(final String title) {
        getPage().setTitleText(title);
    }

    /**
     * Sets the specified element as the document's active element.
     * @see HTMLElement#setActive()
     * @param element the new active element for this document
     */
    public void setActiveElement(final HTMLElement2 element) {
        // TODO update page focus element also

        activeElement_ = element;

        if (element != null) {
            // if this is part of an iFrame, make the iFrame tag the
            // active element of his doc
            final WebWindow window = element.getDomNodeOrDie().getPage().getEnclosingWindow();
            if (window instanceof FrameWindow) {
                final BaseFrameElement frame = ((FrameWindow) window).getFrameElement();
                if (frame instanceof HtmlInlineFrame) {
                    final Window2 winWithFrame = (Window2) frame.getPage().getEnclosingWindow().getScriptObject2();
                    ((HTMLDocument2) Window2.getDocument(winWithFrame)).setActiveElement(
                                (HTMLElement2) frame.getScriptObject2());
                }
            }
        }
    }

    /**
     * Returns the value of the {@code activeElement} property.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533065.aspx">MSDN documentation</a>
     * @return the value of the {@code activeElement} property
     */
    @Getter
    public HTMLElement2 getActiveElement() {
        if (activeElement_ == null) {
            final HtmlElement body = getPage().getBody();
            if (body != null) {
                activeElement_ = (HTMLElement2) getScriptableFor(body);
            }
        }

        return activeElement_;
    }

    /**
     * Creates a new HTML attribute with the specified name.
     *
     * @param attributeName the name of the attribute to create
     * @return an attribute with the specified name
     */
    @Override
    public Attr2 createAttribute(final String attributeName) {
        String name = attributeName;
        if (StringUtils.isNotEmpty(name)
                && getBrowserVersion().hasFeature(JS_DOCUMENT_CREATE_ATTRUBUTE_LOWER_CASE)) {
            name = name.toLowerCase();
        }

        return super.createAttribute(name);
    }

    /**
     * Returns the value of the JavaScript attribute {@code forms}.
     * @return the value of the JavaScript attribute {@code forms}
     */
    @Getter
    public Object getForms() {
        if (forms_ == null) {
            final boolean allowFunctionCall = getBrowserVersion().hasFeature(JS_DOCUMENT_FORMS_FUNCTION_SUPPORTED);

            forms_ = new HTMLCollection2(getDomNodeOrDie(), false) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return node instanceof HtmlForm && node.getPrefix() == null;
                }

//                @Override
//                public Object call(final Context cx, final Scriptable scope,
//                        final Scriptable thisObj, final Object[] args) {
//                    if (allowFunctionCall) {
//                        return super.call(cx, scope, thisObj, args);
//                    }
//                    throw Context.reportRuntimeError("TypeError: document.forms is not a function");
//                }
            };
        }
        return forms_;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLDocument2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @ClassConstructor({@WebBrowser(CHROME), @WebBrowser(FF)})
    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("HTMLDocument", 
                    staticHandle("constructor", HTMLDocument2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("HTMLDocument");
        }
    }

    @ClassConstructor(@WebBrowser(IE))
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        public ObjectConstructor() {
            super("HTMLDocument");
        }
    }
}
