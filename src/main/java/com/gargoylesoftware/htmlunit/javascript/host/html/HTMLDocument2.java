/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLDOCUMENT_GET_ALSO_FRAMES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLDOCUMENT_GET_FOR_ID_AND_OR_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLDOCUMENT_GET_PREFERS_STANDARD_FUNCTIONS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ANCHORS_REQUIRES_NAME_OR_ID;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCUMENT_CREATE_ATTRUBUTE_LOWER_CASE;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
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
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlApplet;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.javascript.NashornJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
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
import com.gargoylesoftware.js.internal.dynalink.CallSiteDescriptor;
import com.gargoylesoftware.js.internal.dynalink.linker.GuardedInvocation;
import com.gargoylesoftware.js.internal.dynalink.linker.LinkRequest;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.ECMAErrors;
import com.gargoylesoftware.js.nashorn.internal.runtime.FindProperty;
import com.gargoylesoftware.js.nashorn.internal.runtime.JSType;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;
import com.gargoylesoftware.js.nashorn.internal.runtime.linker.NashornGuards;

/**
 * A JavaScript object for {@code HTMLDocument}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Michael Ottati
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Ahmed Ashour
 * @author Rob Di Marco
 * @author Sudhan Moghe
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 * @author Ronald Brill
 * @author Frank Danek
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535862.aspx">MSDN documentation</a>
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-7068919">
 * W3C DOM Level 1</a>
 */
@ScriptClass
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

    /**
     * Static counter for {@link #uniqueID_}.
     */
    private static int UniqueID_Counter_ = 1;

    private enum ParsingStatus { OUTSIDE, START, IN_NAME, INSIDE, IN_STRING }

    private HTMLElement2 activeElement_;

    /** The buffer that will be used for calls to document.write(). */
    private final StringBuilder writeBuilder_ = new StringBuilder();
    private boolean writeInCurrentDocument_ = true;
    private String domain_;
    private String uniqueID_;
    private String lastModified_;
    private String compatMode_;
    private int documentMode_ = -1;

    private boolean closePostponedAction_;
    private boolean allAsFunction_;

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

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
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
    @Override
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
        final HTMLCollection2 collection = new HTMLCollection2(page, true) {
            @Override
            protected List<DomNode> computeElements() {
                return new ArrayList<>(page.getElementsByName(expElementName));
            }

            @Override
            protected EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
                if ("name".equals(event.getName())) {
                    return EffectOnCache.RESET;
                }
                return EffectOnCache.NONE;
            }
        };

        return collection;
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
            final StringWebResponse webResponse = new StringWebResponse(writeBuilder_.toString(), url);
            webResponse.setFromJavascript(true);
            writeInCurrentDocument_ = true;
            writeBuilder_.setLength(0);

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
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow());
        try {
            final Event2 event = (Event2) clazz.getMethod("constructor", boolean.class, Object.class)
                .invoke(null, true, global);
            event.eventCreated();
            return event;
        }
        catch (final InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
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
        return new StyleSheetList2(this);
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
    public void setTitle(final Object title) {
        getPage().setTitleText(JSType.toString(title));
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
                    final Window2 winWithFrame =
                            frame.getPage().getEnclosingWindow().<Global>getScriptableObject().getWindow();
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
            name = name.toLowerCase(Locale.ROOT);
        }

        return super.createAttribute(name);
    }

    /**
     * Returns the value of the JavaScript attribute {@code forms}.
     * @return the value of the JavaScript attribute {@code forms}
     */
    @Getter
    public Object getForms() {
        return new HTMLCollection2(getDomNodeOrDie(), false) {
            @Override
            protected boolean isMatching(final DomNode node) {
                return node instanceof HtmlForm && node.getPrefix() == null;
            }

//            @Override
//            public Object call(final Context cx, final Scriptable scope,
//                    final Scriptable thisObj, final Object[] args) {
//                if (allowFunctionCall) {
//                    return super.call(cx, scope, thisObj, args);
//                }
//                throw Context.reportRuntimeError("TypeError: document.forms is not a function");
//            }
        };
    }

    /**
     * JavaScript function "open".
     *
     * See http://www.whatwg.org/specs/web-apps/current-work/multipage/section-dynamic.html for
     * a good description of the semantics of open(), write(), writeln() and close().
     *
     * @param url when a new document is opened, <i>url</i> is a String that specifies a MIME type for the document.
     *        When a new window is opened, <i>url</i> is a String that specifies the URL to render in the new window
     * @param name the name
     * @param features the features
     * @param replace whether to replace in the history list or no
     * @return a reference to the new document object.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536652.aspx">MSDN documentation</a>
     */
    @Function
    public Object open(final Object url, final Object name, final Object features,
            final Object replace) {
        // Any open() invocations are ignored during the parsing stage, because write() and
        // writeln() invocations will directly append content to the current insertion point.
        final HtmlPage page = getPage();
        if (page.isBeingParsed()) {
            LOG.warn("Ignoring call to open() during the parsing stage.");
            return null;
        }

        // We're not in the parsing stage; OK to continue.
        if (!writeInCurrentDocument_) {
            LOG.warn("Function open() called when document is already open.");
        }
        writeInCurrentDocument_ = false;
        if (getWindow().getWebWindow() instanceof FrameWindow
                && WebClient.ABOUT_BLANK.equals(getPage().getUrl().toExternalForm())) {
            final URL enclosingUrl = ((FrameWindow) getWindow().getWebWindow()).getEnclosingPage().getUrl();
            getPage().getWebResponse().getWebRequest().setUrl(enclosingUrl);
        }
        return this;
    }

    /**
     * JavaScript function "write" may accept a variable number of arguments.
     * It's not documented by W3C, Mozilla or MSDN but works with Mozilla and IE.
     * @param args the arguments passed into the method
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536782.aspx">MSDN documentation</a>
     */
    @Function
    public void write(final Object... args) {
        write(concatArgsAsString(args));
    }

    private boolean executionExternalPostponed_;

    /**
     * This a hack!!! A cleaner way is welcome.
     * Handle a case where document.write is simply ignored.
     * See HTMLDocumentWrite2Test.write_fromScriptAddedWithAppendChild_external.
     * @param executing indicates if executing or not
     */
    public void setExecutingDynamicExternalPosponed(final boolean executing) {
        executionExternalPostponed_ = executing;
    }

    /**
     * JavaScript function "write".
     *
     * See http://www.whatwg.org/specs/web-apps/current-work/multipage/section-dynamic.html for
     * a good description of the semantics of open(), write(), writeln() and close().
     *
     * @param content the content to write
     */
    protected void write(final String content) {
        // really strange: if called from an external script loaded as postponed action, write is ignored!!!
        if (executionExternalPostponed_) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("skipping write for external posponed: " + content);
            }
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("write: " + content);
        }

        final HtmlPage page = (HtmlPage) getDomNodeOrDie();
        if (!page.isBeingParsed()) {
            writeInCurrentDocument_ = false;
        }

        // Add content to the content buffer.
        writeBuilder_.append(content);

        // If open() was called; don't write to doc yet -- wait for call to close().
        if (!writeInCurrentDocument_) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("wrote content to buffer");
            }
            scheduleImplicitClose();
            return;
        }
        final String bufferedContent = writeBuilder_.toString();
        if (!canAlreadyBeParsed(bufferedContent)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("write: not enough content to parse it now");
            }
            return;
        }

        writeBuilder_.setLength(0);
        page.writeInParsedStream(bufferedContent);
    }

    private void scheduleImplicitClose() {
        if (!closePostponedAction_) {
            closePostponedAction_ = true;
            final HtmlPage page = (HtmlPage) getDomNodeOrDie();
            final WebWindow enclosingWindow = page.getEnclosingWindow();
            page.getWebClient().getJavaScriptEngine().addPostponedAction(new PostponedAction(page) {
                @Override
                public void execute() throws Exception {
                    if (writeBuilder_.length() != 0) {
                        close();
                    }
                    closePostponedAction_ = false;
                }

                @Override
                public boolean isStillAlive() {
                    return !enclosingWindow.isClosed();
                }
            });
        }
    }

    /**
     * Indicates if the content is a well formed HTML snippet that can already be parsed to be added to the DOM.
     *
     * @param content the HTML snippet
     * @return {@code false} if it not well formed
     */
    static boolean canAlreadyBeParsed(final String content) {
        // all <script> must have their </script> because the parser doesn't close automatically this tag
        // All tags must be complete, that is from '<' to '>'.
        ParsingStatus tagState = ParsingStatus.OUTSIDE;
        int tagNameBeginIndex = 0;
        int scriptTagCount = 0;
        boolean tagIsOpen = true;
        char stringBoundary = 0;
        boolean stringSkipNextChar = false;
        int index = 0;
        char openingQuote = 0;
        for (final char currentChar : content.toCharArray()) {
            switch (tagState) {
                case OUTSIDE:
                    if (currentChar == '<') {
                        tagState = ParsingStatus.START;
                        tagIsOpen = true;
                    }
                    else if (scriptTagCount > 0 && (currentChar == '\'' || currentChar == '"')) {
                        tagState = ParsingStatus.IN_STRING;
                        stringBoundary = currentChar;
                        stringSkipNextChar = false;
                    }
                    break;
                case START:
                    if (currentChar == '/') {
                        tagIsOpen = false;
                        tagNameBeginIndex = index + 1;
                    }
                    else {
                        tagNameBeginIndex = index;
                    }
                    tagState = ParsingStatus.IN_NAME;
                    break;
                case IN_NAME:
                    if (Character.isWhitespace(currentChar) || currentChar == '>') {
                        final String tagName = content.substring(tagNameBeginIndex, index);
                        if ("script".equalsIgnoreCase(tagName)) {
                            if (tagIsOpen) {
                                scriptTagCount++;
                            }
                            else if (scriptTagCount > 0) {
                                // Ignore extra close tags for now. Let the parser deal with them.
                                scriptTagCount--;
                            }
                        }
                        if (currentChar == '>') {
                            tagState = ParsingStatus.OUTSIDE;
                        }
                        else {
                            tagState = ParsingStatus.INSIDE;
                        }
                    }
                    else if (!Character.isLetter(currentChar)) {
                        tagState = ParsingStatus.OUTSIDE;
                    }
                    break;
                case INSIDE:
                    if (currentChar == openingQuote) {
                        openingQuote = 0;
                    }
                    else if (openingQuote == 0) {
                        if (currentChar == '\'' || currentChar == '"') {
                            openingQuote = currentChar;
                        }
                        else if (currentChar == '>' && openingQuote == 0) {
                            tagState = ParsingStatus.OUTSIDE;
                        }
                    }
                    break;
                case IN_STRING:
                    if (stringSkipNextChar) {
                        stringSkipNextChar = false;
                    }
                    else {
                        if (currentChar == stringBoundary) {
                            tagState = ParsingStatus.OUTSIDE;
                        }
                        else if (currentChar == '\\') {
                            stringSkipNextChar = true;
                        }
                    }
                    break;
                default:
                    // nothing
            }
            index++;
        }
        if (scriptTagCount > 0 || tagState != ParsingStatus.OUTSIDE) {
            if (LOG.isDebugEnabled()) {
                final StringBuilder message = new StringBuilder();
                message.append("canAlreadyBeParsed() retruns false for content: '");
                message.append(StringUtils.abbreviateMiddle(content, ".", 100));
                message.append("' (scriptTagCount: " + scriptTagCount);
                message.append(" tagState: " + tagState);
                message.append(")");
                LOG.debug(message.toString());
            }
            return false;
        }

        return true;
    }

    /**
     * JavaScript function "writeln" may accept a variable number of arguments.
     * It's not documented by W3C, Mozilla or MSDN but works with Mozilla and IE.
     * @param args the arguments passed into the method
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536783.aspx">MSDN documentation</a>
     */
    @Function
    public void writeln(final Object... args) {
        write(concatArgsAsString(args) + "\n");
    }

    /**
     * Converts the arguments to strings and concatenate them.
     * @param args the JavaScript arguments
     * @return the string concatenation
     */
    private static String concatArgsAsString(final Object... args) {
        final StringBuilder builder = new StringBuilder();
        for (final Object arg : args) {
            builder.append(arg);
        }
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuardedInvocation noSuchProperty(final CallSiteDescriptor desc, final LinkRequest request) {
        final String name = desc.getNameToken(CallSiteDescriptor.NAME_OPERAND);

        final MethodHandle mh = MethodHandles.insertArguments(
                staticHandle("getArbitraryProperty", Object.class, HTMLDocument2.class, String.class),
                1, name);
        final boolean explicitInstanceOfCheck = NashornGuards.explicitInstanceOfCheck(desc, request);
        return new GuardedInvocation(mh,
                NashornGuards.getMapGuard(getMap(), explicitInstanceOfCheck),
                getProtoSwitchPoints(name, null),
                explicitInstanceOfCheck ? null : ClassCastException.class);
    }

    private static Object getArbitraryProperty(final HTMLDocument2 self, final String name) {
        final HtmlPage page = (HtmlPage) self.getDomNodeOrNull();

        final boolean forIDAndOrName = self.getBrowserVersion().hasFeature(HTMLDOCUMENT_GET_FOR_ID_AND_OR_NAME);
        final boolean alsoFrames = self.getBrowserVersion().hasFeature(HTMLDOCUMENT_GET_ALSO_FRAMES);
        final HTMLCollection2 collection = new HTMLCollection2(page, true) {
            @Override
            protected List<DomNode> computeElements() {
                final List<DomElement> elements;
                if (forIDAndOrName) {
                    elements = page.getElementsByIdAndOrName(name);
                }
                else {
                    elements = page.getElementsByName(name);
                }
                final List<DomNode> matchingElements = new ArrayList<>();
                for (final DomElement elt : elements) {
                    if (elt instanceof HtmlForm || elt instanceof HtmlImage || elt instanceof HtmlApplet
                            || (alsoFrames && elt instanceof BaseFrameElement)) {
                        matchingElements.add(elt);
                    }
                }
                return matchingElements;
            }

            @Override
            protected EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
                final String attributeName = event.getName();
                if ("name".equals(attributeName)) {
                    return EffectOnCache.RESET;
                }
                else if (forIDAndOrName && "id".equals(attributeName)) {
                    return EffectOnCache.RESET;
                }

                return EffectOnCache.NONE;
            }
        };

        final int length = (Integer) collection.getLength();
        if (length == 0) {
            return ScriptRuntime.UNDEFINED;
        }
        else if (length == 1) {
            return collection.item(Integer.valueOf(0));
        }

        return collection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GuardedInvocation findGetMethod(final CallSiteDescriptor desc, final LinkRequest request,
            final String operator) {
        final String name = desc.getNameToken(CallSiteDescriptor.NAME_OPERAND);
        if ("all".equals(name)) {
            allAsFunction_ = "getMethod".equals(operator);
        }
        if (!getBrowserVersion().hasFeature(HTMLDOCUMENT_GET_PREFERS_STANDARD_FUNCTIONS)
                && getArbitraryProperty(this, name) != ScriptRuntime.UNDEFINED) {
            return noSuchProperty(desc, request);
        }
        return super.findGetMethod(desc, request, operator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FindProperty findProperty(final String key, final boolean deep, final ScriptObject start) {
        if ("all".equals(key) && deep && allAsFunction_) {
            final ScriptObject myProto = getProto();
            final FindProperty find = myProto == null ? null : myProto.findProperty(key, true, start);
            if (find != null) {
                return find;
            }
        }
        return super.findProperty(key, deep, start);
    }

    /**
     * Function {@code all}.
     * @param index the index
     * @return the value
     */
    @Function
    public Object all(final Object index) {
        return getAll().call(index);
    }

    /**
     * Returns the value of the {@code all} property.
     * @return the value of the {@code all} property
     */
    @Getter
    public HTMLCollection2 getAll() {
        return new HTMLAllCollection2(getDomNodeOrDie()) {
            @Override
            protected boolean isMatching(final DomNode node) {
                return true;
            }
            @Override
            public boolean avoidObjectDetection() {
                return true;
            }
        };
    }

    /**
     * Returns the value of the JavaScript property {@code anchors}.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms537435.aspx">MSDN documentation</a>
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_doc_ref4.html#1024543">
     * Gecko DOM reference</a>
     * @return the value of this property
     */
    @Getter
    public Object getAnchors() {
        return new HTMLCollection2(getDomNodeOrDie(), true) {
            @Override
            protected boolean isMatching(final DomNode node) {
                if (!(node instanceof HtmlAnchor)) {
                    return false;
                }
                final HtmlAnchor anchor = (HtmlAnchor) node;
                if (getBrowserVersion().hasFeature(JS_ANCHORS_REQUIRES_NAME_OR_ID)) {
                    return anchor.hasAttribute("name") || anchor.hasAttribute("id");
                }
                return anchor.hasAttribute("name");
            }

            @Override
            protected EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
                final HtmlElement node = event.getHtmlElement();
                if (!(node instanceof HtmlAnchor)) {
                    return EffectOnCache.NONE;
                }
                if ("name".equals(event.getName()) || "id".equals(event.getName())) {
                    return EffectOnCache.RESET;
                }
                return EffectOnCache.NONE;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object appendChild(final Object childObject) {
        throw new RuntimeException("Node cannot be inserted at the specified point in the hierarchy.");
    }

    /**
     * Gets the default character set from the current regional language settings.
     * @return the default character set from the current regional language settings
     */
    @Getter(IE)
    public String getDefaultCharset() {
        return "windows-1252";
    }

    /**
     * Gets the window in which this document is contained.
     * @return the window
     */
    @Getter(IE)
    public Global getParentWindow() {
        return getWindow().getGlobal();
    }

    /**
     * Returns the element for the specified x coordinate and the specified y coordinate.
     * The current implementation always returns the &lt;body&gt; element.
     *
     * @param x the x offset, in pixels
     * @param y the y offset, in pixels
     * @return the element for the specified x coordinate and the specified y coordinate
     */
    @Function
    public Object elementFromPoint(final int x, final int y) {
        final HtmlElement element = getPage().getElementFromPoint(x, y);
        return element == null ? null : element.getScriptableObject();
    }

    /**
     * Executes a command.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536419.aspx">MSDN documentation</a>
     * @param cmd the command identifier
     * @param userInterface display a user interface if the command supports one
     * @param value the string, number, or other value to assign (possible values depend on the command)
     * @return {@code true} if the command was successful, {@code false} otherwise
     */
    @Function
    public boolean execCommand(final String cmd, final boolean userInterface, final Object value) {
        if (!hasCommand(cmd, false)) {
            return false;
        }
        LOG.warn("Nothing done for execCommand(" + cmd + ", ...) (feature not implemented)");
        return true;
    }

    private boolean hasCommand(final String cmd, final boolean includeBold) {
        if (null == cmd) {
            return false;
        }

        final String cmdLC = cmd.toLowerCase(Locale.ROOT);
        if (getBrowserVersion().isIE()) {
            return EXECUTE_CMDS_IE.contains(cmdLC);
        }
        if (getBrowserVersion().isChrome()) {
            return EXECUTE_CMDS_CHROME.contains(cmdLC) || (includeBold && "bold".equalsIgnoreCase(cmd));
        }
        return EXECUTE_CMDS_FF.contains(cmdLC);
    }

    /**
     * Returns the value of the JavaScript property {@code links}. Refer also to the
     * <a href="http://msdn.microsoft.com/en-us/library/ms537465.aspx">MSDN documentation</a>.
     * @return the value of this property
     */
    @Getter
    public Object getLinks() {
        return new HTMLCollection2(getDomNodeOrDie(), true) {
            @Override
            protected boolean isMatching(final DomNode node) {
                return (node instanceof HtmlAnchor || node instanceof HtmlArea)
                        && ((HtmlElement) node).hasAttribute("href");
            }

            @Override
            protected EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
                final HtmlElement node = event.getHtmlElement();
                if ((node instanceof HtmlAnchor || node instanceof HtmlArea) && "href".equals(event.getName())) {
                    return EffectOnCache.RESET;
                }
                return EffectOnCache.NONE;
            }
        };
    }

    /**
     * Returns the ready state of the document. This is an IE-only property.
     * @return the ready state of the document
     * @see DomNode#READY_STATE_UNINITIALIZED
     * @see DomNode#READY_STATE_LOADING
     * @see DomNode#READY_STATE_LOADED
     * @see DomNode#READY_STATE_INTERACTIVE
     * @see DomNode#READY_STATE_COMPLETE
     */
    @Getter
    public String getReadyState() {
        return getDomNodeOrDie().getReadyState();
    }

    /**
     * Returns the value of the {@code referrer} property.
     * @return the value of the {@code referrer} property
     */
    @Getter
    public String getReferrer() {
        final String referrer = getPage().getWebResponse().getWebRequest().getAdditionalHeaders().get("Referer");
        if (referrer == null) {
            return "";
        }
        return referrer;
    }

    /**
     * Returns the value of the {@code scripts} property.
     * @return the value of the {@code scripts} property
     */
    @Getter
    public Object getScripts() {
        return new HTMLCollection2(getDomNodeOrDie(), false) {
            @Override
            protected boolean isMatching(final DomNode node) {
                return node instanceof HtmlScript;
            }
        };
    }

    /**
     * Returns the value of the {@code URL} property.
     * @return the value of the {@code URL} property
     */
    @Getter(name = "URL")
    public String getURL() {
        return getPage().getUrl().toExternalForm();
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

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("HTMLDocument",
                    staticHandle("constructor", HTMLDocument2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("HTMLDocument");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("HTMLDocument");
        }
    }
}
