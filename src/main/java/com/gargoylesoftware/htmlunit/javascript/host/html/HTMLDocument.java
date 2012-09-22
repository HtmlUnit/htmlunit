/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import static com.gargoylesoftware.htmlunit.util.StringUtils.containsCaseInsensitive;
import static com.gargoylesoftware.htmlunit.util.StringUtils.parseHttpDate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.UniqueTag;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.CSSException;
import org.w3c.dom.DOMException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentType;
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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnly;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnlyStatus;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Document;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.KeyboardEvent;
import com.gargoylesoftware.htmlunit.javascript.host.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.MutationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.NamespaceCollection;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.javascript.host.NodeFilter;
import com.gargoylesoftware.htmlunit.javascript.host.Range;
import com.gargoylesoftware.htmlunit.javascript.host.Selection;
import com.gargoylesoftware.htmlunit.javascript.host.StaticNodeList;
import com.gargoylesoftware.htmlunit.javascript.host.TreeWalker;
import com.gargoylesoftware.htmlunit.javascript.host.UIEvent;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * A JavaScript object for a Document.
 *
 * @version $Revision$
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
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535862.aspx">MSDN documentation</a>
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-7068919">
 * W3C DOM Level 1</a>
 */
@JsxClass
public class HTMLDocument extends Document implements ScriptableWithFallbackGetter {

    private static final Log LOG = LogFactory.getLog(HTMLDocument.class);

    /** The cookie name used for cookies with no name (HttpClient doesn't like empty names). */
    public static final String EMPTY_COOKIE_NAME = "HTMLUNIT_EMPTY_COOKIE";

    /** The format to use for the <tt>lastModified</tt> attribute. */
    private static final String LAST_MODIFIED_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

    private static final Pattern FIRST_TAG_PATTERN = Pattern.compile("<(\\w+)(\\s+[^>]*)?>");
    private static final Pattern ATTRIBUTES_PATTERN = Pattern.compile("(\\w+)\\s*=\\s*['\"]([^'\"]*)['\"]");

    /**
     * Map<String, Class> which maps strings a caller may use when calling into
     * {@link #createEvent(String)} to the associated event class. To support a new
     * event creation type, the event type and associated class need to be added into this map in
     * the static initializer. The map is unmodifiable. Any class that is a value in this map MUST
     * have a no-arg constructor.
     */
    private static final Map<String, Class<? extends Event>> SUPPORTED_EVENT_TYPE_MAP;

    private static final List<String> EXECUTE_CMDS_IE = Arrays.asList(new String[] {
        "2D-Position", "AbsolutePosition", "BackColor", "BackgroundImageCache" /* Undocumented */,
        "BlockDirLTR", "BlockDirRTL", "Bold", "BrowseMode", "ClearAuthenticationCache", "Copy", "CreateBookmark",
        "CreateLink", "Cut", "Delete", "DirLTR", "DirRTL",
        "EditMode", "FontName", "FontSize", "ForeColor", "FormatBlock",
        "Indent", "InlineDirLTR", "InlineDirRTL", "InsertButton", "InsertFieldset",
        "InsertHorizontalRule", "InsertIFrame", "InsertImage", "InsertInputButton", "InsertInputCheckbox",
        "InsertInputFileUpload", "InsertInputHidden", "InsertInputImage", "InsertInputPassword", "InsertInputRadio",
        "InsertInputReset", "InsertInputSubmit", "InsertInputText", "InsertMarquee", "InsertOrderedList",
        "InsertParagraph", "InsertSelectDropdown", "InsertSelectListbox", "InsertTextArea", "InsertUnorderedList",
        "Italic", "JustifyCenter", "JustifyFull", "JustifyLeft", "JustifyNone",
        "JustifyRight", "LiveResize", "MultipleSelection", "Open", "Outdent",
        "OverWrite", "Paste", "PlayImage", "Print", "Redo",
        "Refresh", "RemoveFormat", "RemoveParaFormat", "SaveAs", "SelectAll",
        "SizeToControl", "SizeToControlHeight", "SizeToControlWidth", "Stop", "StopImage",
        "StrikeThrough", "Subscript", "Superscript", "UnBookmark", "Underline",
        "Undo", "Unlink", "Unselect"
    });

    /** https://developer.mozilla.org/en/Rich-Text_Editing_in_Mozilla#Executing_Commands */
    private static final List<String> EXECUTE_CMDS_FF = Arrays.asList(new String[] {
        "backColor", "bold", "contentReadOnly", "copy", "createLink", "cut", "decreaseFontSize", "delete",
        "fontName", "fontSize", "foreColor", "formatBlock", "heading", "hiliteColor", "increaseFontSize",
        "indent", "insertHorizontalRule", "insertHTML", "insertImage", "insertOrderedList", "insertUnorderedList",
        "insertParagraph", "italic", "justifyCenter", "justifyLeft", "justifyRight", "outdent", "paste", "redo",
        "removeFormat", "selectAll", "strikeThrough", "subscript", "superscript", "underline", "undo", "unlink",
        "useCSS", "styleWithCSS"
    });

    /**
     * Static counter for {@link #uniqueID_}.
     */
    private static int UniqueID_Counter_ = 1;

    private HTMLCollection all_; // has to be a member to have equality (==) working
    private HTMLCollection forms_; // has to be a member to have equality (==) working
    private HTMLCollection links_; // has to be a member to have equality (==) working
    private HTMLCollection images_; // has to be a member to have equality (==) working
    private HTMLCollection scripts_; // has to be a member to have equality (==) working
    private HTMLCollection anchors_; // has to be a member to have equality (==) working
    private HTMLCollection applets_; // has to be a member to have equality (==) working
    private StyleSheetList styleSheets_; // has to be a member to have equality (==) working
    private NamespaceCollection namespaces_; // has to be a member to have equality (==) working
    private HTMLElement activeElement_;

    /** The buffer that will be used for calls to document.write(). */
    private final StringBuilder writeBuffer_ = new StringBuilder();
    private boolean writeInCurrentDocument_ = true;
    private String domain_;
    private String uniqueID_;
    private String lastModified_;

    private boolean closePostponedAction_;

    /** Initializes the supported event type map. */
    static {
        final Map<String, Class<? extends Event>> eventMap = new HashMap<String, Class<? extends Event>>();
        eventMap.put("Event", Event.class);
        eventMap.put("Events", Event.class);
        eventMap.put("KeyboardEvent", KeyboardEvent.class);
        eventMap.put("KeyEvents", KeyboardEvent.class);
        eventMap.put("HTMLEvents", Event.class);
        eventMap.put("MouseEvent", MouseEvent.class);
        eventMap.put("MouseEvents", MouseEvent.class);
        eventMap.put("MutationEvent", MutationEvent.class);
        eventMap.put("MutationEvents", MutationEvent.class);
        eventMap.put("UIEvent", UIEvent.class);
        eventMap.put("UIEvents", UIEvent.class);
        SUPPORTED_EVENT_TYPE_MAP = Collections.unmodifiableMap(eventMap);
    }

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    public HTMLDocument() {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <N extends DomNode> N getDomNodeOrDie() throws IllegalStateException {
        try {
            return (N) super.getDomNodeOrDie();
        }
        catch (final IllegalStateException e) {
            final DomNode node = getDomNodeOrNullFromRealDocument();
            if (node != null) {
                return (N) node;
            }
            throw Context.reportRuntimeError("No node attached to this object");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public DomNode getDomNodeOrNull() {
        DomNode node = super.getDomNodeOrNull();
        if (node == null) {
            node = getDomNodeOrNullFromRealDocument();
        }
        return node;
    }

    /**
     * Document functions invoked on the window end up executing on the document prototype -- and
     * this is supposed to work when we're emulating IE! So when {@link #getDomNodeOrDie()} or
     * {@link #getDomNodeOrNull()} are invoked on the document prototype (which would usually fail),
     * we need to actually return the real document's DOM node so that other functions which rely
     * on these two functions work. See {@link HTMLDocumentTest#documentMethodsWithoutDocument()}
     * for sample JavaScript code.
     *
     * @return the real document's DOM node, or <tt>null</tt> if we're not emulating IE
     */
    private DomNode getDomNodeOrNullFromRealDocument() {
        DomNode node = null;
        final boolean ie = getWindow().getWebWindow().getWebClient()
            .getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_51);
        if (ie) {
            final Scriptable scope = getParentScope();
            if (scope instanceof Window) {
                final Window w = (Window) scope;
                final Document realDocument = w.getDocument();
                if (realDocument != this) {
                    node = realDocument.getDomNodeOrDie();
                }
            }
        }
        return node;
    }

    /**
     * Returns the HTML page that this document is modeling.
     * @return the HTML page that this document is modeling
     */
    public HtmlPage getHtmlPage() {
        return (HtmlPage) getDomNodeOrDie();
    }

    /**
     * Returns the HTML page that this document is modeling, or <tt>null</tt> if the page is empty.
     * @return the HTML page that this document is modeling, or <tt>null</tt> if the page is empty
     */
    public HtmlPage getHtmlPageOrNull() {
        return (HtmlPage) getDomNodeOrNull();
    }

    /**
     * Returns the value of the JavaScript attribute "forms".
     * @return the value of the JavaScript attribute "forms"
     */
    @JsxGetter
    public Object get_forms() {
        if (forms_ == null) {
            final HtmlPage page = getHtmlPage();
            final boolean allowFunctionCall =
                    getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCUMENT_FORMS_FUNCTION_SUPPORTED);

            forms_ = new HTMLCollection(page, false, "HTMLDocument.forms") {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return node instanceof HtmlForm;
                }

                @Override
                public final Object call(final Context cx, final Scriptable scope,
                        final Scriptable thisObj, final Object[] args) {
                    if (allowFunctionCall) {
                        return super.call(cx, scope, thisObj, args);
                    }
                    throw Context.reportRuntimeError("TypeError: document.forms is not a function");
                }
            };
        }
        return forms_;
    }

    /**
     * Returns the value of the JavaScript attribute "links". Refer also to the
     * <a href="http://msdn.microsoft.com/en-us/library/ms537465.aspx">MSDN documentation</a>.
     * @return the value of this attribute
     */
    @JsxGetter
    public Object get_links() {
        if (links_ == null) {
            links_ = new HTMLCollection(getDomNodeOrDie(), true, "HTMLDocument.links") {
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
        return links_;
    }

    /**
     * Returns the last modification date of the document.
     * @see <a href="https://developer.mozilla.org/en/DOM/document.lastModified">Mozilla documentation</a>
     * @return the date as string
     */
    @JsxGetter
    public String get_lastModified() {
        if (lastModified_ == null) {
            final WebResponse webResponse = getPage().getWebResponse();
            String stringDate = webResponse.getResponseHeaderValue("Last-Modified");
            if (stringDate == null) {
                stringDate = webResponse.getResponseHeaderValue("Date");
            }
            final Date lastModified = parseDateOrNow(stringDate);
            lastModified_ = new SimpleDateFormat(LAST_MODIFIED_DATE_FORMAT).format(lastModified);
        }
        return lastModified_;
    }

    private static Date parseDateOrNow(final String stringDate) {
        final Date date = parseHttpDate(stringDate);
        if (date == null) {
            return new Date();
        }
        return date;
    }

    /**
     * Returns the value of the JavaScript attribute "namespaces".
     * @return the value of the JavaScript attribute "namespaces"
     */
    @JsxGetter(@WebBrowser(IE))
    public Object get_namespaces() {
        if (namespaces_ == null) {
            namespaces_ = new NamespaceCollection(this);
        }
        return namespaces_;
    }

    /**
     * Returns the value of the JavaScript attribute "anchors".
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms537435.aspx">MSDN documentation</a>
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_doc_ref4.html#1024543">
     * Gecko DOM reference</a>
     * @return the value of this attribute
     */
    @JsxGetter
    public Object get_anchors() {
        if (anchors_ == null) {
            final boolean checkId =
                getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ANCHORS_REQUIRES_NAME_OR_ID);

            anchors_ = new HTMLCollection(getDomNodeOrDie(), true, "HTMLDocument.anchors") {
                @Override
                protected boolean isMatching(final DomNode node) {
                    if (!(node instanceof HtmlAnchor)) {
                        return false;
                    }
                    final HtmlAnchor anchor = (HtmlAnchor) node;
                    if (checkId) {
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
        return anchors_;
    }

    /**
     * Returns the value of the JavaScript attribute "applets".
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms537436.aspx">
     * MSDN documentation</a>
     * @see <a href="https://developer.mozilla.org/En/DOM:document.applets">
     * Gecko DOM reference</a>
     * @return the value of this attribute
     */
    @JsxGetter
    public Object get_applets() {
        if (applets_ == null) {
            applets_ = new HTMLCollection(getDomNodeOrDie(), false, "HTMLDocument.applets") {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return node instanceof HtmlApplet;
                }
            };
        }
        return applets_;
    }

    /**
     * JavaScript function "write" may accept a variable number of arguments.
     * It's not documented by W3C, Mozilla or MSDN but works with Mozilla and IE.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536782.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public static void write(final Context context, final Scriptable thisObj, final Object[] args,
        final Function function) {
        final HTMLDocument thisAsDocument = getDocument(thisObj);
        thisAsDocument.write(concatArgsAsString(args));
    }

    /**
     * Converts the arguments to strings and concatenate them.
     * @param args the JavaScript arguments
     * @return the string concatenation
     */
    private static String concatArgsAsString(final Object[] args) {
        final StringBuilder buffer = new StringBuilder();
        for (final Object arg : args) {
            buffer.append(Context.toString(arg));
        }
        return buffer.toString();
    }

    /**
     * JavaScript function "writeln" may accept a variable number of arguments.
     * It's not documented by W3C, Mozilla or MSDN but works with Mozilla and IE.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536783.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public static void writeln(
        final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final HTMLDocument thisAsDocument = getDocument(thisObj);
        thisAsDocument.write(concatArgsAsString(args) + "\n");
    }

    /**
     * Returns the current document instance, using <tt>thisObj</tt> as a hint.
     * @param thisObj a hint as to the current document (may be the prototype when function is used without "this")
     * @return the current document instance
     */
    private static HTMLDocument getDocument(final Scriptable thisObj) {
        // if function is used "detached", then thisObj is the top scope (ie Window), not the real object
        // cf unit test DocumentTest#testDocumentWrite_AssignedToVar
        // may be the prototype too
        // cf DocumentTest#testDocumentWrite_AssignedToVar2
        if (thisObj instanceof HTMLDocument && thisObj.getPrototype() instanceof HTMLDocument) {
            return (HTMLDocument) thisObj;
        }
        else if (thisObj instanceof DocumentProxy && thisObj.getPrototype() instanceof HTMLDocument) {
            return (HTMLDocument) ((DocumentProxy) thisObj).getDelegee();
        }
        final Window window = getWindow(thisObj);
        final BrowserVersion browser = window.getWebWindow().getWebClient().getBrowserVersion();
        if (browser.hasFeature(BrowserVersionFeatures.GENERATED_53)) {
            return (HTMLDocument) window.getDocument();
        }
        throw Context.reportRuntimeError("Function can't be used detached from document");
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("write: " + content);
        }

        final HtmlPage page = getDomNodeOrDie();
        if (!page.isBeingParsed()) {
            writeInCurrentDocument_ = false;
        }

        // Add content to the content buffer.
        writeBuffer_.append(content);

        // If open() was called; don't write to doc yet -- wait for call to close().
        if (!writeInCurrentDocument_) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("wrote content to buffer");
            }
            scheduleImplicitClose();
            return;
        }
        final String bufferedContent = writeBuffer_.toString();
        if (!canAlreadyBeParsed(bufferedContent)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("write: not enough content to parse it now");
            }
            return;
        }

        writeBuffer_.setLength(0);
        page.writeInParsedStream(bufferedContent);
    }

    private void scheduleImplicitClose() {
        if (!closePostponedAction_) {
            closePostponedAction_ = true;
            final HtmlPage page = getDomNodeOrDie();
            page.getWebClient().getJavaScriptEngine().addPostponedAction(new PostponedAction(page) {
                @Override
                public void execute() throws Exception {
                    if (writeBuffer_.length() > 0) {
                        close();
                    }
                    closePostponedAction_ = false;
                }
            });
        }
    }

    private enum PARSING_STATUS { OUTSIDE, START, IN_NAME, INSIDE, IN_STRING }

    /**
     * Indicates if the content is a well formed HTML snippet that can already be parsed to be added to the DOM.
     *
     * @param content the HTML snippet
     * @return <code>false</code> if it not well formed
     */
    static boolean canAlreadyBeParsed(final String content) {
        // all <script> must have their </script> because the parser doesn't close automatically this tag
        // All tags must be complete, that is from '<' to '>'.
        PARSING_STATUS tagState = PARSING_STATUS.OUTSIDE;
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
                        tagState = PARSING_STATUS.START;
                        tagIsOpen = true;
                    }
                    else if (scriptTagCount > 0 && (currentChar == '\'' || currentChar == '"')) {
                        tagState = PARSING_STATUS.IN_STRING;
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
                    tagState = PARSING_STATUS.IN_NAME;
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
                            tagState = PARSING_STATUS.OUTSIDE;
                        }
                        else {
                            tagState = PARSING_STATUS.INSIDE;
                        }
                    }
                    else if (!Character.isLetter(currentChar)) {
                        tagState = PARSING_STATUS.OUTSIDE;
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
                            tagState = PARSING_STATUS.OUTSIDE;
                        }
                    }
                    break;
                case IN_STRING:
                    if (stringSkipNextChar) {
                        stringSkipNextChar = false;
                    }
                    else {
                        if (currentChar == stringBoundary) {
                            tagState = PARSING_STATUS.OUTSIDE;
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
        if (scriptTagCount > 0 || tagState != PARSING_STATUS.OUTSIDE) {
            return false;
        }

        return true;
    }

    /**
     * Gets the node that is the last one when exploring following nodes, depth-first.
     * @param node the node to search
     * @return the searched node
     */
    HtmlElement getLastHtmlElement(final HtmlElement node) {
        final DomNode lastChild = node.getLastChild();
        if (lastChild == null
                || !(lastChild instanceof HtmlElement)
                || lastChild instanceof HtmlScript) {
            return node;
        }

        return getLastHtmlElement((HtmlElement) lastChild);
    }

    /**
     * Returns the cookie attribute.
     * @return the cookie attribute
     */
    @JsxGetter
    public String get_cookie() {
        final HtmlPage page = getHtmlPage();

        URL url = page.getWebResponse().getWebRequest().getUrl();
        url = replaceForCookieIfNecessary(url);

        final StringBuilder buffer = new StringBuilder();
        final Set<Cookie> cookies = page.getWebClient().getCookieManager().getCookies(url);
        for (final Cookie cookie : cookies) {
            if (cookie.isHttpOnly()) {
                continue;
            }
            if (buffer.length() != 0) {
                buffer.append("; ");
            }
            if (!EMPTY_COOKIE_NAME.equals(cookie.getName())) {
                buffer.append(cookie.getName());
                buffer.append("=");
            }
            buffer.append(cookie.getValue());
        }

        return buffer.toString();
    }

    /**
     * Returns the "compatMode" attribute.
     * Note that it is deprecated in Internet Explorer 8 in favor of the documentMode.
     * @return the "compatMode" attribute
     */
    @JsxGetter
    public String get_compatMode() {
        return getHtmlPage().isQuirksMode() ? "BackCompat" : "CSS1Compat";
    }

    /**
     * Adds a cookie, as long as cookies are enabled.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533693.aspx">MSDN documentation</a>
     * @param newCookie in the format "name=value[;expires=date][;domain=domainname][;path=path][;secure]
     */
    @JsxSetter
    public void set_cookie(final String newCookie) {
        final CookieManager cookieManager = getHtmlPage().getWebClient().getCookieManager();
        if (cookieManager.isCookiesEnabled()) {
            URL url = getHtmlPage().getWebResponse().getWebRequest().getUrl();
            url = replaceForCookieIfNecessary(url);
            final Cookie cookie = buildCookie(newCookie, url);
            cookieManager.addCookie(cookie);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Added cookie: " + cookie);
            }
        }
        else if (LOG.isDebugEnabled()) {
            LOG.debug("Skipped adding cookie: " + newCookie);
        }
    }

    /**
     * {@link org.apache.commons.httpclient.cookie.CookieSpec#match(String, int, String, boolean, Cookie[])} doesn't
     * like empty hosts and negative ports, but these things happen if we're dealing with a local file. This method
     * allows us to work around this limitation in HttpClient by feeding it a bogus host and port.
     *
     * @param url the URL to replace if necessary
     * @return the replacement URL, or the original URL if no replacement was necessary
     */
    private static URL replaceForCookieIfNecessary(URL url) {
        final String protocol = url.getProtocol();
        final boolean file = "file".equals(protocol);
        if (file) {
            try {
                url = UrlUtils.getUrlWithNewPort(UrlUtils.getUrlWithNewHost(url, "LOCAL_FILESYSTEM"), 0);
            }
            catch (final MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return url;
    }

    /**
     * Builds a cookie object from the string representation allowed in JS.
     * @param newCookie in the format "name=value[;expires=date][;domain=domainname][;path=path][;secure]
     * @param currentURL the URL of the current page
     * @return the cookie
     */
    public static Cookie buildCookie(final String newCookie, final URL currentURL) {
        // Pull out the cookie name and value.
        String name, value;
        final StringTokenizer st = new StringTokenizer(newCookie, ";");
        if (newCookie.contains("=")) {
            final String nameAndValue = st.nextToken();
            name = StringUtils.substringBefore(nameAndValue, "=").trim();
            value = StringUtils.substringAfter(nameAndValue, "=").trim();
        }
        else {
            name = EMPTY_COOKIE_NAME;
            value = newCookie;
        }

        // Default attribute values (note: HttpClient doesn't like null paths).
        final Map<String, Object> atts = new HashMap<String, Object>();
        atts.put("domain", currentURL.getHost());
        atts.put("path", "");

        // Custom attribute values.
        while (st.hasMoreTokens()) {
            final String token = st.nextToken();
            final int indexEqual = token.indexOf('=');
            if (indexEqual > -1) {
                atts.put(token.substring(0, indexEqual).toLowerCase().trim(), token.substring(indexEqual + 1).trim());
            }
            else {
                atts.put(token.toLowerCase().trim(), Boolean.TRUE);
            }
        }

        // Try to parse the <expires> value as a date if specified.
        final String date = (String) atts.get("expires");
        final Date expires = parseHttpDate(date);

        // Build the cookie.
        final String domain = (String) atts.get("domain");
        final String path = (String) atts.get("path");
        final boolean secure = (atts.get("secure") != null);
        final Cookie cookie = new Cookie(domain, name, value, path, expires, secure);

        return cookie;
    }

    /**
     * Returns the value of the "images" property.
     * @return the value of the "images" property
     */
    @JsxGetter
    public Object get_images() {
        if (images_ == null) {
            images_ = new HTMLCollection(getDomNodeOrDie(), false, "HTMLDocument.images") {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return node instanceof HtmlImage;
                }
            };
        }
        return images_;
    }

    /**
     * Returns the value of the "URL" property.
     * @return the value of the "URL" property
     */
    @JsxGetter
    public String get_URL() {
        return getHtmlPage().getWebResponse().getWebRequest().getUrl().toExternalForm();
    }

    /**
     * Retrieves an auto-generated, unique identifier for the object.
     * <b>Note</b> The unique ID generated is not guaranteed to be the same every time the page is loaded.
     * @return an auto-generated, unique identifier for the object
     */
    @JsxGetter(@WebBrowser(IE))
    public String get_uniqueID() {
        if (uniqueID_ == null) {
            uniqueID_ = "ms__id" + UniqueID_Counter_++;
        }
        return uniqueID_;
    }

    /**
     * Returns the value of the "all" property.
     * @return the value of the "all" property
     */
    @JsxGetter
    public HTMLCollection get_all() {
        if (all_ == null) {
            all_ = new HTMLCollectionTags(getDomNodeOrDie(), "HTMLDocument.all") {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return true;
                }
            };
            all_.setAvoidObjectDetection(
                    !getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_OBJECT_DETECTION));
        }
        return all_;
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
     * @return a reference to the new document object or the window object.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536652.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public Object open(final String url, final Object name, final Object features,
            final Object replace) {
        // Any open() invocations are ignored during the parsing stage, because write() and
        // writeln() invocations will directly append content to the current insertion point.
        final HtmlPage page = getHtmlPage();
        if (page.isBeingParsed()) {
            LOG.warn("Ignoring call to open() during the parsing stage.");
            return null;
        }

        // We're not in the parsing stage; OK to continue.
        if (!writeInCurrentDocument_) {
            LOG.warn("Function open() called when document is already open.");
        }
        writeInCurrentDocument_ = false;
        return null;
    }

    /**
     * JavaScript function "close".
     *
     * See http://www.whatwg.org/specs/web-apps/current-work/multipage/section-dynamic.html for
     * a good description of the semantics of open(), write(), writeln() and close().
     *
     * @throws IOException if an IO problem occurs
     */
    @JsxFunction
    public void close() throws IOException {
        if (writeInCurrentDocument_) {
            LOG.warn("close() called when document is not open.");
        }
        else {
            final HtmlPage page = getHtmlPage();
            final URL url = page.getWebResponse().getWebRequest().getUrl();
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
     * Closes the document implicitly, i.e. flushes the <tt>document.write</tt> buffer (IE only).
     */
    private void implicitCloseIfNecessary() {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_55);
        if (!writeInCurrentDocument_ && ie) {
            try {
                close();
            }
            catch (final IOException e) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
        }
    }

    /**
     * Gets the window in which this document is contained.
     * @return the window
     */
    @JsxGetter(@WebBrowser(IE))
    public Object get_parentWindow() {
        return getWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object appendChild(final Object childObject) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCUMENT_APPEND_CHILD_SUPPORTED)) {
            // We're emulating IE; we can allow insertion.
            return super.appendChild(childObject);
        }

        // Firefox does not allow insertion at the document level.
        throw Context.reportRuntimeError("Node cannot be inserted at the specified point in the hierarchy.");
    }

    /**
     * Create a new HTML element with the given tag name.
     *
     * @param tagName the tag name
     * @return the new HTML element, or NOT_FOUND if the tag is not supported
     */
    @Override
    public Object createElement(String tagName) {
        Object result = NOT_FOUND;

        // IE can handle HTML, but it takes only the first tag found
        if (tagName.startsWith("<") && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_57)) {
            final Matcher m = FIRST_TAG_PATTERN.matcher(tagName);
            if (m.find()) {
                tagName = m.group(1);
                result = super.createElement(tagName);
                if (result == NOT_FOUND || m.group(2) == null) {
                    return result;
                }
                final HTMLElement elt = (HTMLElement) result;

                // handle attributes
                final String attributes = m.group(2);
                final Matcher mAttribute = ATTRIBUTES_PATTERN.matcher(attributes);
                while (mAttribute.find()) {
                    final String attrName = mAttribute.group(1);
                    final String attrValue = mAttribute.group(2);
                    elt.setAttribute(attrName, attrValue);
                }
            }
        }
        else {
            return super.createElement(tagName);
        }

        return result;
    }

    /**
     * Creates a new Stylesheet.
     * Current implementation just creates an empty {@link CSSStyleSheet} object.
     * @param url the stylesheet URL
     * @param index where to insert the sheet in the collection
     * @return the newly created stylesheet
     */
    @JsxFunction(@WebBrowser(IE))
    public CSSStyleSheet createStyleSheet(final String url, final int index) {
        // minimal implementation
        final CSSStyleSheet stylesheet = new CSSStyleSheet();
        stylesheet.setPrototype(getPrototype(CSSStyleSheet.class));
        stylesheet.setParentScope(getWindow());
        return stylesheet;
    }

    /**
     * Returns the element with the specified ID, or <tt>null</tt> if that element could not be found.
     * @param id the ID to search for
     * @return the element, or <tt>null</tt> if it could not be found
     */
    @JsxFunction
    public Object getElementById(final String id) {
        implicitCloseIfNecessary();
        Object result = null;
        try {
            final boolean caseSensitive =
                getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_GET_ELEMENT_BY_ID_CASE_SENSITIVE);
            final DomElement htmlElement = getHtmlPage().getElementById(id, caseSensitive);
            final Object jsElement = getScriptableFor(htmlElement);
            if (jsElement == NOT_FOUND) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getElementById(" + id
                            + ") cannot return a result as there isn't a JavaScript object for the HTML element "
                            + htmlElement.getClass().getName());
                }
            }
            else {
                result = jsElement;
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to null
            final BrowserVersion browser = getBrowserVersion();
            if (browser.hasFeature(BrowserVersionFeatures.JS_GET_ELEMENT_BY_ID_ALSO_BY_NAME_IN_QUICKS_MODE)
                    && getHtmlPage().isQuirksMode()) {
                final HTMLCollection elements = getElementsByName(id);
                result = elements.get(0, elements);
                if (result instanceof UniqueTag) {
                    return null;
                }
                LOG.warn("getElementById(" + id + ") did a getElementByName for Internet Explorer");
                return result;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("getElementById(" + id + "): no DOM node found with this id");
            }
        }
        return result;
    }

    /**
     * Returns all the descendant elements with the specified class name.
     * @param className the name to search for
     * @return all the descendant elements with the specified class name
     * @see <a href="https://developer.mozilla.org/en/DOM/document.getElementsByClassName">Mozilla doc</a>
     */
    @JsxFunction(@WebBrowser(FF))
    public HTMLCollection getElementsByClassName(final String className) {
        return ((HTMLElement) get_documentElement()).getElementsByClassName(className);
    }

    /**
     * Returns all HTML elements that have a "name" attribute with the specified value.
     *
     * Refer to <a href="http://www.w3.org/TR/DOM-Level-2-HTML/html.html#ID-71555259">
     * The DOM spec</a> for details.
     *
     * @param elementName - value of the "name" attribute to look for
     * @return all HTML elements that have a "name" attribute with the specified value
     */
    @JsxFunction
    public HTMLCollection getElementsByName(final String elementName) {
        implicitCloseIfNecessary();
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_59)
                && (StringUtils.isEmpty(elementName) || "null".equals(elementName))) {
            return HTMLCollection.emptyCollection(getWindow());
        }
        // Null must me changed to '' for proper collection initialization.
        final String expElementName = "null".equals(elementName) ? "" : elementName;

        final HtmlPage page = (HtmlPage) getPage();
        final String description = "HTMLDocument.getElementsByName('" + elementName + "')";
        final HTMLCollection collection = new HTMLCollection(page, true, description) {
            @Override
            protected List<Object> computeElements() {
                return new ArrayList<Object>(page.getElementsByName(expElementName));
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
     * Calls to <tt>document.XYZ</tt> should first look at elements named <tt>XYZ</tt> before
     * using standard functions.
     *
     * {@inheritDoc}
     */
    @Override
    protected Object getWithPreemption(final String name) {
        final HtmlPage page = (HtmlPage) getDomNodeOrNull();
        if (page == null || getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_160)) {
            return NOT_FOUND;
        }
        return getIt(name);
    }

    private Object getIt(final String name) {
        final HtmlPage page = (HtmlPage) getDomNodeOrNull();

        final boolean isIE = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_60);
        final HTMLCollection collection = new HTMLCollection(page, true, "HTMLDocument." + name) {
            @Override
            protected List<Object> computeElements() {
                final List<DomElement> elements;
                if (isIE) {
                    elements = page.getElementsByIdAndOrName(name);
                }
                else {
                    elements = page.getElementsByName(name);
                }
                final List<Object> matchingElements = new ArrayList<Object>();
                for (final DomElement elt : elements) {
                    if (elt instanceof HtmlForm || elt instanceof HtmlImage || elt instanceof HtmlApplet
                            || (isIE && elt instanceof BaseFrameElement)) {
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
                else if (isIE && "id".equals(attributeName)) {
                    return EffectOnCache.RESET;
                }

                return EffectOnCache.NONE;
            }

            @Override
            protected SimpleScriptable getScriptableFor(final Object object) {
                if (isIE && object instanceof BaseFrameElement) {
                    return (SimpleScriptable) ((BaseFrameElement) object).getEnclosedWindow().getScriptObject();
                }
                return super.getScriptableFor(object);
            }
        };

        final int length = collection.get_length();
        if (length == 0) {
            return NOT_FOUND;
        }
        else if (length == 1) {
            return collection.item(Integer.valueOf(0));
        }

        return collection;
    }

    /**
     * Looks at attributes with the specified name.
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_161)) {
            return getIt(name);
        }
        return NOT_FOUND;
    }

    /**
     * Returns this document's <tt>body</tt> element.
     * @return this document's <tt>body</tt> element
     */
    @CanSetReadOnly(CanSetReadOnlyStatus.EXCEPTION)
    @JsxGetter
    public HTMLElement get_body() {
        final HtmlPage page = getHtmlPage();
        // for IE, the body of a not yet loaded page is null whereas it already exists for FF
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_61)
                && (page.getEnclosingWindow() instanceof FrameWindow)) {
            final HtmlPage enclosingPage = (HtmlPage) page.getEnclosingWindow().getParentWindow().getEnclosedPage();
            if (WebClient.URL_ABOUT_BLANK.equals(page.getWebResponse().getWebRequest().getUrl())
                    && enclosingPage.getReadyState() != DomNode.READY_STATE_COMPLETE) {
                return null;
            }
        }
        final HtmlElement body = page.getBody();
        if (body != null) {
            return (HTMLElement) body.getScriptObject();
        }
        return null;
    }

    /**
     * Returns this document's <tt>head</tt> element.
     * @return this document's <tt>head</tt> element
     */
    @JsxGetter(@WebBrowser(value = FF, minVersion = 10))
    public HTMLElement get_head() {
        final HtmlElement head = getHtmlPage().getHead();
        if (head != null) {
            return (HTMLElement) head.getScriptObject();
        }
        return null;
    }

    /**
     * Returns this document's title.
     * @return this document's title
     */
    @JsxGetter
    public String get_title() {
        return getHtmlPage().getTitleText();
    }

    /**
     * Sets this document's title.
     * @param title the new title
     */
    @JsxSetter
    public void set_title(final String title) {
        getHtmlPage().setTitleText(title);
    }

    /**
     * Returns the value of the <tt>bgColor</tt> attribute.
     * @return the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String get_bgColor() {
        String color = getHtmlPage().getBody().getAttribute("bgColor");
        if (color == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_COLOR)) {
            color = "#ffffff";
        }
        return color;
    }

    /**
     * Sets the value of the <tt>bgColor</tt> attribute.
     * @param color the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void set_bgColor(final String color) {
        final HTMLBodyElement body = (HTMLBodyElement) getHtmlPage().getBody().getScriptObject();
        body.set_bgColor(color);
    }

    /**
     * Returns the value of the <tt>alinkColor</tt> attribute.
     * @return the value of the <tt>alinkColor</tt> attribute
     */
    @JsxGetter
    public String get_alinkColor() {
        String color = getHtmlPage().getBody().getAttribute("aLink");
        if (color == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_COLOR)) {
            color = "#0000ff";
        }
        return color;
    }

    /**
     * Sets the value of the <tt>alinkColor</tt> attribute.
     * @param color the value of the <tt>alinkColor</tt> attribute
     */
    @JsxSetter
    public void set_alinkColor(final String color) {
        final HTMLBodyElement body = (HTMLBodyElement) getHtmlPage().getBody().getScriptObject();
        body.set_aLink(color);
    }

    /**
     * Returns the value of the <tt>linkColor</tt> attribute.
     * @return the value of the <tt>linkColor</tt> attribute
     */
    @JsxGetter
    public String get_linkColor() {
        String color = getHtmlPage().getBody().getAttribute("link");
        if (color == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_COLOR)) {
            color = "#0000ff";
        }
        return color;
    }

    /**
     * Sets the value of the <tt>linkColor</tt> attribute.
     * @param color the value of the <tt>linkColor</tt> attribute
     */
    @JsxSetter
    public void set_linkColor(final String color) {
        final HTMLBodyElement body = (HTMLBodyElement) getHtmlPage().getBody().getScriptObject();
        body.set_link(color);
    }

    /**
     * Returns the value of the <tt>vlinkColor</tt> attribute.
     * @return the value of the <tt>vlinkColor</tt> attribute
     */
    @JsxGetter
    public String get_vlinkColor() {
        String color = getHtmlPage().getBody().getAttribute("vLink");
        if (color == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_COLOR)) {
            color = "#800080";
        }
        return color;
    }

    /**
     * Sets the value of the <tt>vlinkColor</tt> attribute.
     * @param color the value of the <tt>vlinkColor</tt> attribute
     */
    @JsxSetter
    public void set_vlinkColor(final String color) {
        final HTMLBodyElement body = (HTMLBodyElement) getHtmlPage().getBody().getScriptObject();
        body.set_vLink(color);
    }

    /**
     * Returns the value of the <tt>fgColor</tt> attribute.
     * @return the value of the <tt>fgColor</tt> attribute
     */
    @JsxGetter
    public String get_fgColor() {
        String color = getHtmlPage().getBody().getAttribute("text");
        if (color == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLDOCUMENT_COLOR)) {
            color = "#000000";
        }
        return color;
    }

    /**
     * Sets the value of the <tt>fgColor</tt> attribute.
     * @param color the value of the <tt>fgColor</tt> attribute
     */
    @JsxSetter
    public void set_fgColor(final String color) {
        final HTMLBodyElement body = (HTMLBodyElement) getHtmlPage().getBody().getScriptObject();
        body.set_text(color);
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
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String get_readyState() {
        final DomNode node = getDomNodeOrDie();
        return node.getReadyState();
    }

    /**
     * Returns the domain name of the server that served the document, or <tt>null</tt> if the server
     * cannot be identified by a domain name.
     * @return the domain name of the server that served the document
     * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-2250147">
     * W3C documentation</a>
     */
    @JsxGetter
    public String get_domain() {
        if (domain_ == null) {
            URL url = getHtmlPage().getWebResponse().getWebRequest().getUrl();
            if (url == WebClient.URL_ABOUT_BLANK) {
                final WebWindow w = getWindow().getWebWindow();
                if (w instanceof FrameWindow) {
                    url = ((FrameWindow) w).getEnclosingPage().getWebResponse().getWebRequest().getUrl();
                }
                else {
                    return null;
                }
            }
            domain_ = url.getHost();
            final BrowserVersion browser = getBrowserVersion();
            if (browser.hasFeature(BrowserVersionFeatures.JS_DOCUMENT_DOMAIN_IS_LOWERCASE)) {
                domain_ = domain_.toLowerCase();
            }
        }

        return domain_;
    }

    /**
     * Sets the the domain of this document.
     *
     * Domains can only be set to suffixes of the existing domain
     * with the exception of setting the domain to itself.
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
     *
     *        transformation to:        com
     *        will fail
     * </li>
     * </ol>
     * </p>
     * TODO This code could be modified to understand country domain suffixes.
     * The domain www.bbc.co.uk should be trimmable only down to bbc.co.uk
     * trimming to co.uk should not be possible.
     * @param newDomain the new domain to set
     */
    @JsxSetter
    public void set_domain(final String newDomain) {
        final BrowserVersion browserVersion = getBrowserVersion();

        // IE (at least 6) doesn't allow to set domain of about:blank
        if (WebClient.URL_ABOUT_BLANK == getPage().getWebResponse().getWebRequest().getUrl()
            && browserVersion.hasFeature(BrowserVersionFeatures.JS_DOCUMENT_SETTING_DOMAIN_THROWS_FOR_ABOUT_BLANK)) {
            throw Context.reportRuntimeError("Illegal domain value, cannot set domain from \"about:blank\" to: \""
                    + newDomain + "\"");
        }

        final String currentDomain = get_domain();
        if (currentDomain.equalsIgnoreCase(newDomain)) {
            return;
        }

        if (newDomain.indexOf('.') == -1
                || !currentDomain.toLowerCase().endsWith("." + newDomain.toLowerCase())) {
            throw Context.reportRuntimeError("Illegal domain value, cannot set domain from: \""
                    + currentDomain + "\" to: \"" + newDomain + "\"");
        }

        // Netscape down shifts the case of the domain
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCUMENT_DOMAIN_IS_LOWERCASE)) {
            domain_ = newDomain.toLowerCase();
        }
        else {
            domain_ = newDomain;
        }
    }

    /**
     * Returns the value of the JavaScript attribute <tt>scripts</tt>.
     * @return the value of the JavaScript attribute <tt>scripts</tt>
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(value = FF, minVersion = 10) })
    public Object get_scripts() {
        if (scripts_ == null) {
            scripts_ = new HTMLCollection(getDomNodeOrDie(), false, "HTMLDocument.scripts") {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return node instanceof HtmlScript;
                }
            };
        }
        return scripts_;
    }

    /**
     * Returns the value of the JavaScript attribute <tt>selection</tt>.
     * @return the value of the JavaScript attribute <tt>selection</tt>
     */
    @JsxGetter(@WebBrowser(IE))
    public Selection get_selection() {
        return getWindow().getSelectionImpl();
    }

    /**
     * Returns the value of the <tt>frames</tt> property.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms537459.aspx">MSDN documentation</a>
     * @return the live collection of frames contained by this document
     */
    @JsxGetter(@WebBrowser(IE))
    public Object get_frames() {
        return getWindow().get_frames();
    }

    /**
     * Retrieves a collection of stylesheet objects representing the style sheets that correspond
     * to each instance of a Link or
     * {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration} object in the document.
     *
     * @return styleSheet collection
     */
    @JsxGetter
    public StyleSheetList get_styleSheets() {
        if (styleSheets_ == null) {
            styleSheets_ = new StyleSheetList(this);
        }
        return styleSheets_;
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
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public Event createEvent(final String eventType) throws DOMException {
        final Class<? extends Event> clazz = SUPPORTED_EVENT_TYPE_MAP.get(eventType);
        if (clazz == null) {
            Context.throwAsScriptRuntimeEx(new DOMException(DOMException.NOT_SUPPORTED_ERR,
                "Event Type is not supported: " + eventType));
            return null; // to stop eclipse warning
        }
        try {
            final Event event = clazz.newInstance();
            event.setEventType(eventType);
            event.setParentScope(getWindow());
            event.setPrototype(getPrototype(clazz));
            return event;
        }
        catch (final InstantiationException e) {
            throw Context.reportRuntimeError("Failed to instantiate event: class ='" + clazz.getName()
                            + "' for event type of '" + eventType + "': " + e.getMessage());
        }
        catch (final IllegalAccessException e) {
            throw Context.reportRuntimeError("Failed to instantiate event: class ='" + clazz.getName()
                            + "' for event type of '" + eventType + "': " + e.getMessage());
        }
    }

    /**
     * Implementation of the <tt>createEventObject</tt> method supported by Internet Explorer. This
     * method returns an uninitialized event object. It is up to the caller of the method to initialize
     * the properties of the event.
     *
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536390.aspx">MSDN Documentation</a>
     * @return an uninitialized event object
     */
    @JsxFunction(@WebBrowser(IE))
    public Event createEventObject() {
        final Event event = new MouseEvent();
        event.setParentScope(getWindow());
        event.setPrototype(getPrototype(event.getClass()));
        return event;
    }

    /**
     * Returns the element for the specified x coordinate and the specified y coordinate.
     * The current implementation always returns the &lt;body&gt; element.
     *
     * @param x the x offset, in pixels
     * @param y the y offset, in pixels
     * @return the element for the specified x coordinate and the specified y coordinate
     */
    @JsxFunction
    public Object elementFromPoint(final int x, final int y) {
        // minimal implementation to make simple unit test happy for FF and IE
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_164) && (x <= 0 || y <= 0)) {
            return null;
        }
        return get_body();
    }

    /**
     * Creates and returns a new range.
     * @return a new range
     * @see <a href="http://www.xulplanet.com/references/objref/HTMLDocument.html#method_createRange">XUL Planet</a>
     */
    @JsxFunction(@WebBrowser(FF))
    public Range createRange() {
        final Range r = new Range(this);
        r.setParentScope(getWindow());
        r.setPrototype(getPrototype(Range.class));
        return r;
    }

    /**
     * Creates and returns a new TreeWalker. The following JavaScript parameters are passed into this method:
     * <ul>
     *   <li>JavaScript param 1: The root node of the TreeWalker. Must not be <tt>null</tt>.</li>
     *   <li>JavaScript param 2: Flag specifying which types of nodes appear in the logical view of the TreeWalker.
     *       See {@link NodeFilter} for the set of possible Show_ values.</li>
     *   <li>JavaScript param 3: The {@link NodeFilter} to be used with this TreeWalker, or <tt>null</tt>
     *       to indicate no filter.</li>
     *   <li>JavaScript param 4: If <tt>false</tt>, the contents of EntityReference nodes are not present
     *       in the logical view.</li>
     * </ul>
     *
     * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/traversal.html">DOM-Level-2-Traversal-Range</a>
     * @param root the node which will serve as the root for the TreeWalker
     * @param whatToShow specifies which node types may appear in the logical view of the tree presented
     * @param filter the NodeFilter to be used with this TreeWalker, or null to indicate no filter
     * @param expandEntityReferences If false,
     *        the contents of EntityReference nodes are not presented in the logical view
     * @throws DOMException on attempt to create a TreeWalker with a root that is <code>null</code>
     * @return a new TreeWalker
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public Object createTreeWalker(final Node root, final double whatToShow, final Scriptable filter,
            final boolean expandEntityReferences) throws DOMException {

        // seems that Rhino doesn't like long as parameter type
        final long whatToShowL = Double.valueOf(whatToShow).longValue();
        NodeFilter filterWrapper = null;
        if (filter != null) {
            filterWrapper = new NodeFilter() {
                @Override
                public short acceptNode(final Node n) {
                    final Object[] args = new Object[] {n};
                    final Object response;
                    if (filter instanceof Callable) {
                        response = ((Callable) filter).call(Context.getCurrentContext(), filter, filter, args);
                    }
                    else {
                        response = ScriptableObject.callMethod(filter, "acceptNode", args);
                    }
                    return (short) Context.toNumber(response);
                }
            };
        }

        final TreeWalker t = new TreeWalker(root, whatToShowL, filterWrapper, Boolean.valueOf(expandEntityReferences));
        t.setParentScope(getWindow(this));
        t.setPrototype(staticGetPrototype(getWindow(this), TreeWalker.class));
        return t;
    }

    @SuppressWarnings("unchecked")
    private static Scriptable staticGetPrototype(final Window window,
            final Class<? extends SimpleScriptable> javaScriptClass) {
        final Scriptable prototype = window.getPrototype(javaScriptClass);
        if (prototype == null && javaScriptClass != SimpleScriptable.class) {
            return staticGetPrototype(window, (Class<? extends SimpleScriptable>) javaScriptClass.getSuperclass());
        }
        return prototype;
    }

    /**
     * Indicates if the command is supported.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536681.aspx">MSDN documentation</a>
     * @param cmd the command identifier
     * @return <code>true></code> if the command is supported
     */
    @JsxFunction
    public boolean queryCommandSupported(final String cmd) {
        final boolean ff = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_165);
        final String mode = get_designMode();
        if (!ff) {
            return containsCaseInsensitive(EXECUTE_CMDS_IE, cmd);
        }
        if (!"on".equals(mode)) {
            final String msg = "queryCommandSupported() called while document.designMode='" + mode + "'.";
            throw Context.reportRuntimeError(msg);
        }
        return containsCaseInsensitive(EXECUTE_CMDS_FF, cmd);
    }

    /**
     * Indicates if the command can be successfully executed using <tt>execCommand</tt>, given
     * the current state of the document.
     * @param cmd the command identifier
     * @return <code>true</code> if the command can be successfully executed
     */
    @JsxFunction
    public boolean queryCommandEnabled(final String cmd) {
        final boolean ff = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_165);
        final String mode = get_designMode();
        if (!ff) {
            return containsCaseInsensitive(EXECUTE_CMDS_IE, cmd);
        }
        if (!"on".equals(mode)) {
            final String msg = "queryCommandEnabled() called while document.designMode='" + mode + "'.";
            throw Context.reportRuntimeError(msg);
        }
        return containsCaseInsensitive(EXECUTE_CMDS_FF, cmd);
    }

    /**
     * Executes a command.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536419.aspx">MSDN documentation</a>
     * @param cmd the command identifier
     * @param userInterface display a user interface if the command supports one
     * @param value the string, number, or other value to assign (possible values depend on the command)
     * @return <tt>true</tt> if the command was successful, <tt>false</tt> otherwise
     */
    @JsxFunction
    public boolean execCommand(final String cmd, final boolean userInterface, final Object value) {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_63);
        if ((ie && !containsCaseInsensitive(EXECUTE_CMDS_IE, cmd))
            || (!ie && !containsCaseInsensitive(EXECUTE_CMDS_FF, cmd))) {

            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.EXECCOMMAND_THROWS_ON_WRONG_COMMAND)) {
                throw Context.reportRuntimeError("document.execCommand(): invalid command '" + cmd + "'");
            }
            return false;
        }
        LOG.warn("Nothing done for execCommand(" + cmd + ", ...) (feature not implemented)");
        return true;
    }

    /**
     * Returns the value of the "activeElement" property.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533065.aspx">MSDN documentation</a>
     * @return the value of the "activeElement" property
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public Object get_activeElement() {
        if (activeElement_ == null) {
            final HtmlElement body = getHtmlPage().getBody();
            if (body != null) {
                activeElement_ = (HTMLElement) getScriptableFor(body);
            }
        }
        return activeElement_;
    }

    /**
     * Sets the specified element as the document's active element.
     * @see HTMLElement#setActive()
     * @param element the new active element for this document
     */
    public void setActiveElement(final HTMLElement element) {
        activeElement_ = element;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleScriptable get_doctype() {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCUMENT_DOCTYPE_NULL)) {
            return null;
        }
        return super.get_doctype();
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
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public boolean dispatchEvent(final Event event) {
        event.setTarget(this);
        final ScriptResult result = fireEvent(event);
        return !event.isAborted(result);
    }

    /**
     * Retrieves all element nodes from descendants of the starting element node that match any selector
     * within the supplied selector strings.
     * The NodeList object returned by the querySelectorAll() method must be static, not live.
     * @param selectors the selectors
     * @return the static node list
     */
    @JsxFunction({ @WebBrowser(value = IE, minVersion = 8), @WebBrowser(FF) })
    public StaticNodeList querySelectorAll(final String selectors) {
        try {
            final List<Node> nodes = new ArrayList<Node>();
            for (final DomNode domNode : getHtmlPage().querySelectorAll(selectors)) {
                nodes.add((Node) domNode.getScriptObject());
            }
            return new StaticNodeList(nodes, this);
        }
        catch (final CSSException e) {
            throw Context.reportRuntimeError("An invalid or illegal string was specified");
        }
    }

    /**
     * Returns the first element within the document that matches the specified group of selectors.
     * @param selectors the selectors
     * @return null if no matches are found; otherwise, it returns the first matching element
     */
    @JsxFunction({ @WebBrowser(value = IE, minVersion = 8), @WebBrowser(FF) })
    public Node querySelector(final String selectors) {
        try {
            final DomNode node = getHtmlPage().querySelector(selectors);
            if (node != null) {
                return (Node) node.getScriptObject();
            }
            return null;
        }
        catch (final CSSException e) {
            throw Context.reportRuntimeError("An invalid or illegal string was specified");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        final Object response = super.get(name, start);

        // IE8 support .querySelector(All) but not in quirks mode
        // => TODO: find a better way to handle this!
        if (response instanceof FunctionObject
            && ("querySelectorAll".equals(name) || "querySelector".equals(name))
            && getBrowserVersion().hasFeature(BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS)
            && getHtmlPage().isQuirksMode()) {
            return NOT_FOUND;
        }

        return response;
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
    @Override
    public SimpleScriptable makeScriptableFor(final DomNode domNode) {
        if (domNode instanceof DomDocumentType
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.DOCTYPE_IS_COMMENT)) {
            final DomDocumentType docType = (DomDocumentType) domNode;
            final DomComment comment = new DomComment(getHtmlPage(), "DOCTYPE " + docType.getName() + " PUBLIC \""
                    + docType.getPublicId() + "\"      \"" + docType.getSystemId() + '"');
            return super.makeScriptableFor(comment);
        }
        return super.makeScriptableFor(domNode);
    }

    /**
     * Sets the head.
     * @param head the head
     */
    @JsxSetter(@WebBrowser(value = FF, minVersion = 10))
    public void set_head(final ScriptableObject head) {
        //ignore
    }

    /**
     * Returns the current selection.
     * @return the current selection
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public Selection getSelection() {
        return getWindow().getSelectionImpl();
    }
}
