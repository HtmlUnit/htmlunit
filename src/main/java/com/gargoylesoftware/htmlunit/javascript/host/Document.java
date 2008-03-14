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
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.UniqueTag;
import org.w3c.dom.DOMException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

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
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_document.asp">
 * MSDN documentation</a>
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-7068919">
 * W3C Dom Level 1</a>
 */
public class Document extends EventNode {

    private static final long serialVersionUID = -7646789903352066465L;

    /**
     * Map<String, Class> which maps strings a caller may use when calling into
     * {@link #jsxFunction_createEvent(String)} to the associated event class. To support a new
     * event creation type, the event type and associated class need to be added into this map in
     * the static initializer. The map is unmodifiable. Any class that is a value in this map MUST
     * have a no-arg constructor.
     */
    private static final Map<String, Class< ? extends Event>> SUPPORTED_EVENT_TYPE_MAP;
    
    private static final String[] EXECUTE_CMDS_IE_ARR = {
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
    };
    private static final List<String> EXECUTE_CMDS_IE = Arrays.asList(EXECUTE_CMDS_IE_ARR);

    private HTMLCollection all_; // has to be a member to have equality (==) working
    private HTMLCollection forms_; // has to be a member to have equality (==) working
    private HTMLCollection links_; // has to be a member to have equality (==) working
    private HTMLCollection images_; // has to be a member to have equality (==) working
    private HTMLCollection scripts_; // has to be a member to have equality (==) working
    private HTMLCollection anchors_; // has to be a member to have equality (==) working
    private StyleSheetList styleSheets_; // has to be a member to have equality (==) working

    /** The buffer that will be used for calls to document.write() */
    private final StringBuilder writeBuffer_ = new StringBuilder();
    private boolean writeInCurrentDocument_ = true;
    private String domain_;
    private Window window_;

    private DOMImplementation implementation_;

    /** Initializes the supported event type map. */
    static {
        final Map<String, Class< ? extends Event>> eventMap = new HashMap<String, Class< ? extends Event>>();
        eventMap.put("Event", Event.class);
        eventMap.put("Events", Event.class);
        eventMap.put("HTMLEvents", Event.class);
        eventMap.put("UIEvent", UIEvent.class);
        eventMap.put("UIEvents", UIEvent.class);
        eventMap.put("MouseEvent", MouseEvent.class);
        eventMap.put("MouseEvents", MouseEvent.class);
        SUPPORTED_EVENT_TYPE_MAP = Collections.unmodifiableMap(eventMap);
    }

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public Document() {
    }

    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Define the Window JavaScript object that encloses this Document object.
     *
     * @param window The Window JavaScript object that encloses this document.
     */
    void setWindow(final Window window) {
        window_ = window;
    }

    /**
     * Return the HTML page that this document is modeling..
     * @return The page.
     */
    public HtmlPage getHtmlPage() {
        return (HtmlPage) getDomNodeOrDie();
    }

    /**
     * Return the HTML page that this document is modeling or null if the
     * page is empty.
     * @return The page.
     */
    public HtmlPage getHtmlPageOrNull() {
        return (HtmlPage) getDomNodeOrNull();
    }

    /**
     * Return the value of the javascript attribute "forms".
     * @return The value of this attribute.
     */
    public Object jsxGet_forms() {
        if (forms_ == null) {
            forms_ = new HTMLCollection(this);
            forms_.init(getDomNodeOrDie(), ".//form");
        }
        return forms_;
    }

    /**
     * Return the value of the javascript attribute "links".  Refer also to the
     * <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/collections/links.asp"
     * MSDN documentation</a>
     * @return The value of this attribute.
     */
    public Object jsxGet_links() {
        if (links_ == null) {
            links_ = new HTMLCollection(this);
            links_.init(getDomNodeOrDie(), ".//a[@href] | .//area[@href]");
        }
        return links_;
    }

    /**
     * Return the value of the javascript attribute "anchors".
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/collections/anchors.asp">
     * MSDN documentation</a>
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_doc_ref4.html#1024543">
     * Gecko DOM reference</a>
     * @return The value of this attribute.
     */
    public Object jsxGet_anchors() {
        if (anchors_ == null) {
            anchors_ = new HTMLCollection(this);
            final String xpath;
            if (getBrowserVersion().isIE()) {
                xpath = ".//a[@name or @id]";
            }
            else {
                xpath = ".//a[@name]";
            }

            anchors_.init(getDomNodeOrDie(), xpath);
        }
        return anchors_;
    }

    /**
     * javascript function "write" may accept a variable number of args.
     * It's not documented by W3C, Mozilla or MSDN but works with Mozilla and IE.
     * @param context The javascript context
     * @param thisObj The scriptable
     * @param args The arguments passed into the method.
     * @param function The function.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/write.asp">
     * MSDN documentation</a>
     */
    public static void jsxFunction_write(
        final Context context, final Scriptable thisObj, final Object[] args,  final Function function) {

        final Document thisAsDocument = getDocument(thisObj);
        thisAsDocument.write(concatArgsAsString(args));
    }

    /**
     * Converts the arguments to strings and concatenate them.
     * @param args the javascript arguments
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
     * javascript function "writeln" may accept a variable number of args.
     * It's not documented by W3C, Mozilla or MSDN but works with Mozilla and IE.
     * @param context The javascript context
     * @param thisObj The scriptable
     * @param args The arguments passed into the method.
     * @param function The function.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/writeln.asp">
     * MSDN documentation</a>
     */
    public static void jsxFunction_writeln(
        final Context context, final Scriptable thisObj, final Object[] args,  final Function function) {

        final Document thisAsDocument = getDocument(thisObj);
        thisAsDocument.write(concatArgsAsString(args) + "\n");
    }

    /**
     * Gets the document instance.
     * @param document maybe the prototype when function is used without "this"
     * @return the current document
     */
    private static Document getDocument(final Scriptable thisObj) {
        // if function is used "detached", then thisObj is the top scope (ie Window), not the real object
        // cf unit test DocumentTest#testDocumentWrite_AssignedToVar
        if (thisObj instanceof Document) {
            return (Document) thisObj;
        }
        else {
            final Window window = getWindow(thisObj);
            final BrowserVersion browser = window.getWebWindow().getWebClient().getBrowserVersion();
            if (browser.isIE()) {
                return window.jsxGet_document();
            }
            else {
                throw Context.reportRuntimeError("Function can't be used detached from document");
            }
        }
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
        getLog().debug("write: " + content);

        // If the page isn't currently being parsed (i.e. this call to write() or writeln()
        // was triggered by an event, setTimeout(), etc), then the new content will replace
        // the entire page. Basically, we make an implicit open() call.
        final HtmlPage page = (HtmlPage) getDomNodeOrDie();
        if (!page.isBeingParsed()) {
            writeInCurrentDocument_ = false;
        }

        // Add content to the content buffer.
        writeBuffer_.append(content);

        // If open() was called; don't write to doc yet -- wait for call to close().
        if (!writeInCurrentDocument_) {
            getLog().debug("wrote content to buffer");
            return;
        }

        // If the buffered content isn't complete and wellformed; don't write to doc yet.
        final String bufferedContent = writeBuffer_.toString();
        if (!canAlreadyBeParsed(bufferedContent)) {
            getLog().debug("write: not enough content to parsed it now");
            return;
        }

        // Let the user know that we can (and will) go ahead and write to the document.
        getLog().debug("parsing buffered content: " + bufferedContent);

        // Clear the buffer.
        writeBuffer_.setLength(0);

        // Get the node at which the parsed content should be added.
        HtmlElement current;
        final HtmlElement doc = page.getDocumentHtmlElement();
        HtmlElement body = page.getBody();
        if (body == null) {
            // The body doesn't exist yet! Add it.
            body = new HtmlBody(null, "body", page, null, true);
            doc.appendChild(body);
            current = body;
        }
        else {
            if (body instanceof HtmlBody && ((HtmlBody) body).isTemporary()) {
                // Add inside the (temporary) body.
                current = body;
            }
            else {
                // Add inside the current / last element.
                current = getLastHtmlElement(doc);
            }
        }

        // Quick and dirty workaround for target (IFRAME JS object aren't an HTMLElement).
        if (current instanceof HtmlInlineFrame) {
            current = (HtmlElement) current.getParentDomNode();
        }

        // Append the new content.
        ((HTMLElement) getJavaScriptNode(current)).jsxFunction_insertAdjacentHTML(HTMLElement.POSITION_BEFORE_END,
                        bufferedContent);
    }

    /**
     * Indicates if the content is a well formed HTML snippet that can already be parsed to be added
     * to the DOM.
     *
     * @param content the HTML snippet
     * @return <code>false</code> if it not well formed
     */
    private boolean canAlreadyBeParsed(final String content) {
        // all <script> must have their </script> because the parser doesn't close automatically this tag
        // All tags must be complete, that is from '<' to '>'.
        final int tagOutside = 0;
        final int tagSart = 1;
        final int tagInName = 2;
        final int tagInside = 3;
        int tagState = tagOutside;
        int tagNameBeginIndex = 0;
        int scriptTagCount = 0;
        boolean tagIsOpen = true;
        for (int index = 0; index < content.length(); index++) {
            final char currentChar = content.charAt(index);
            switch (tagState) {
                case tagOutside:
                    if (currentChar == '<') {
                        tagState = tagSart;
                        tagIsOpen = true;
                    }
                    break;
                case tagSart:
                    if (currentChar == '/') {
                        tagIsOpen = false;
                        tagNameBeginIndex = index + 1;
                    }
                    else {
                        tagNameBeginIndex = index;
                    }
                    tagState = tagInName;
                    break;
                case tagInName:
                    if (!Character.isLetter(currentChar)) {
                        final String tagName = content.substring(tagNameBeginIndex, index);
                        if (tagName.equalsIgnoreCase("script")) {
                            if (tagIsOpen) {
                                scriptTagCount++;
                            }
                            else if (scriptTagCount > 0) {
                                // Ignore extra close tags for now.  Let the
                                // parser deal with them.
                                scriptTagCount--;
                            }
                        }
                        if (currentChar == '>') {
                            tagState = tagOutside;
                        }
                        else {
                            tagState = tagInside;
                        }
                    }
                    break;
                case tagInside:
                    if (currentChar == '>') {
                        tagState = tagOutside;
                    }
                    break;
                default:
                    // nothing
            }
        }
        if (scriptTagCount > 0 || tagState != tagOutside) {
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
        final DomNode lastChild = node.getLastDomChild();
        if (lastChild == null
                || !(lastChild instanceof HtmlElement)
                || lastChild instanceof HtmlScript) {
            return node;
        }

        return getLastHtmlElement((HtmlElement) lastChild);
    }

    /**
     * Returns the cookie attribute.
     * @return The cookie attribute
     */
    public String jsxGet_cookie() {
        final HtmlPage page = getHtmlPage();
        final HttpState state = page.getWebClient().getWebConnection().getState();
        final URL url = page.getWebResponse().getUrl();
        final boolean secure = "https".equals(url.getProtocol());
        final int port;
        if (url.getPort() != -1) {
            port = url.getPort();
        }
        else {
            port = url.getDefaultPort();
        }

        final CookieSpec spec = CookiePolicy.getCookieSpec(WebClient.HTMLUNIT_COOKIE_POLICY);
        final Cookie[] cookies = spec.match(url.getHost(), port, url.getPath(), secure, state.getCookies());
        if (cookies == null) {
            return "";
        }

        final StringBuilder buffer = new StringBuilder();
        for (final Cookie cookie : cookies) {
            if (buffer.length() != 0) {
                buffer.append("; ");
            }
            buffer.append(cookie.getName());
            buffer.append("=");
            buffer.append(cookie.getValue());
        }

        return buffer.toString();
    }

    /**
     * Adds a cookie.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/cookie.asp">
     * MSDN documentation</a>
     * @param newCookie in the format "name=value[;expires=date][;domain=domainname][;path=path][;secure]
     */
    public void jsxSet_cookie(final String newCookie) {
        final HttpState state = getHtmlPage().getWebClient().getWebConnection().getState();

        final Cookie cookie = buildCookie(newCookie, getHtmlPage().getWebResponse().getUrl());
        state.addCookie(cookie);
        getLog().info("Added cookie: " + cookie);
    }

    /**
     * Builds a cookie object from the string representation allowed in JS
     * @param newCookie in the format "name=value[;expires=date][;domain=domainname][;path=path][;secure]
     * @param currentURL the url of the current page
     * @return the cookie
     */
    static Cookie buildCookie(final String newCookie, final URL currentURL) {
        final StringTokenizer st = new StringTokenizer(newCookie, ";");
        final String nameValue = st.nextToken();

        final String name = StringUtils.substringBefore(nameValue, "=").trim();
        final String value = StringUtils.substringAfter(nameValue, "=").trim();

        final Map<String, Object> attributes = new HashMap<String, Object>();
        // default values
        attributes.put("domain", currentURL.getHost());
        // default value "" as it seems that org.apache.commons.httpclient.cookie.CookieSpec
        // doesn't like null as path
        attributes.put("path", "");

        while (st.hasMoreTokens()) {
            final String token = st.nextToken();
            final int indexEqual = token.indexOf("=");
            if (indexEqual > -1) {
                attributes.put(token.substring(0, indexEqual).toLowerCase().trim(),
                        token.substring(indexEqual + 1).trim());
            }
            else {
                attributes.put(token.toLowerCase().trim(), Boolean.TRUE);
            }
        }

        // Try to parse the <expires> value as a date if specified
        Date expires = null;
        final String date = (String) attributes.get("expires");
        if (date != null) {
            try {
                expires = DateUtil.parseDate(date);
            }
            catch (final DateParseException e) {
                // nothing
            }
        }

        final String domain = (String) attributes.get("domain");
        final String path = (String) attributes.get("path");
        final boolean secure = (attributes.get("secure") != null);
        final Cookie cookie = new Cookie(domain, name, value, path, expires, secure);

        return cookie;
    }

    /**
     * Return the value of the "location" property.
     * @return The value of the "location" property
     */
    public Location jsxGet_location() {
        return window_.jsxGet_location();
    }

    /**
     * Sets the value of the "location" property. The location's default property is "href",
     * so setting "document.location='http://www.sf.net'" is equivalent to setting
     * "document.location.href='http://www.sf.net'".
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_location.asp">
     * MSDN documentation</a>
     * @param location the location to navigate to
     * @throws IOException when location loading fails
     */
    public void jsxSet_location(final String location) throws IOException {
        window_.jsxSet_location(location);
    }

    /**
     * Return the value of the "images" property.
     * @return The value of the "images" property
     */
    public Object jsxGet_images() {
        if (images_ == null) {
            images_ = new HTMLCollection(this);
            images_.init(getDomNodeOrDie(), ".//img");
        }
        return images_;
    }

    /**
     * Return the value of the "referrer" property.
     * @return The value of the "referrer" property
     */
    public String jsxGet_referrer() {
        final String referrer = getHtmlPage().getWebResponse().getResponseHeaderValue("referrer");
        if (referrer == null) {
            return "";
        }
        else {
            return referrer;
        }
    }

    /**
     * Return the value of the "URL" property.
     * @return The value of the "URL" property
     */
    public String jsxGet_URL() {
        return getHtmlPage().getWebResponse().getUrl().toExternalForm();
    }

    /**
     * Return the value of the "all" property.
     * @return The value of the "all" property
     */
    public HTMLCollection jsxGet_all() {
        if (all_ == null) {
            all_ = new HTMLCollectionTags(this);
            all_.setAvoidObjectDetection(!getBrowserVersion().isIE());
            all_.init(getDomNodeOrDie(), ".//*");
        }
        return all_;
    }

    /**
     * JavaScript function "open".
     *
     * See http://www.whatwg.org/specs/web-apps/current-work/multipage/section-dynamic.html for
     * a good description of the semantics of open(), write(), writeln() and close().
     *
     * @param context the JavaScript context
     * @param scriptable the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return nothing
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/open_1.asp">
     * MSDN documentation</a>
     */
    public static Object jsxFunction_open(
        final Context context, final Scriptable scriptable, final Object[] args, final Function function) {

        // Any open() invocations are ignored during the parsing stage, because write() and
        // writeln() invocations will directly append content to the current insertion point.
        final Document document = (Document) scriptable;
        final HtmlPage page = (HtmlPage) document.getDomNodeOrDie();
        if (page.isBeingParsed()) {
            document.getLog().warn("Ignoring call to open() during the parsing stage.");
            return null;
        }

        // We're not in the parsing stage; OK to continue.
        if (!document.writeInCurrentDocument_) {
            document.getLog().warn("Function open() called when document is already open.");
        }
        document.writeInCurrentDocument_ = false;

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
    public void jsxFunction_close() throws IOException {
        if (writeInCurrentDocument_) {
            getLog().warn("close() called when document is not open.");
        }
        else {
            final WebResponse webResponse = new StringWebResponse(writeBuffer_.toString());
            final HtmlPage page = (HtmlPage) getDomNodeOrDie().getPage();
            final WebClient webClient = page.getWebClient();
            final WebWindow window = page.getEnclosingWindow();

            webClient.loadWebResponseInto(webResponse, window);

            writeInCurrentDocument_ = true;
            writeBuffer_.setLength(0);
        }
    }

    /**
     * Get the JavaScript property "documentElement" for the document.
     * @return The root node for the document.
     */
    public SimpleScriptable jsxGet_documentElement() {
        return getScriptableFor(((HtmlPage) getDomNodeOrDie()).getDocumentHtmlElement());
    }

    /**
     * Get the window in which this document is contained.
     * @return the window
     */
    public Object jsxGet_defaultView() {
        return getWindow();
    }

    /**
     * Get the window in which this document is contained.
     * @return the window
     */
    public Object jsxGet_parentWindow() {
        return getWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object jsxFunction_appendChild(final Object childObject) {
        if (limitAppendChildToIE() && !getBrowserVersion().isIE()) {
            // Firefox does not allow insertion at the document level.
            throw new RuntimeException("Node cannot be inserted at the specified point in the hierarchy.");
        }
        else {
            // We're emulating IE; we can allow insertion.
            return super.jsxFunction_appendChild(childObject);
        }
    }

    /**
     * Returns <tt>true</tt> if this document only allows <tt>appendChild</tt> to be called on
     * it when emulating IE.
     *
     * @return <tt>true</tt> if this document only allows <tt>appendChild</tt> to be called on
     *         it when emulating IE
     *
     * @see Document#limitAppendChildToIE()
     * @see XMLDocument#limitAppendChildToIE()
     */
    protected boolean limitAppendChildToIE() {
        return true;
    }

    /**
     * Create a new HTML element with the given tag name.
     *
     * @param tagName The tag name
     * @return the new HTML element, or NOT_FOUND if the tag is not supported.
     */
    public Object jsxFunction_createElement(String tagName) {
        Object result = NOT_FOUND;
        try {
            final BrowserVersion browserVersion = getBrowserVersion();

            //IE can handle HTML
            if (tagName.startsWith("<") && browserVersion.isIE()) {
                try {
                    final HtmlElement proxyNode =
                        new HtmlDivision(null, HtmlDivision.TAG_NAME, (HtmlPage) getDomNodeOrDie().getPage(), null);
                    HTMLParser.parseFragment(proxyNode, tagName);
                    final DomNode resultNode = proxyNode.getFirstDomChild();
                    resultNode.removeAllChildren();
                    result = resultNode.getScriptObject();
                }
                catch (final Exception e) {
                    getLog().error("Unexpected exception occurred while parsing html snippet", e);
                    throw Context.reportRuntimeError("Unexpected exception occurred while parsing html snippet: "
                            + e.getMessage());
                }
            }
            else {
                //Firefox handles only simple '<tagName>'
                if (tagName.startsWith("<") && tagName.endsWith(">") && browserVersion.isNetscape()) {
                    tagName = tagName.substring(1, tagName.length() - 1);
                    if (!tagName.matches("\\w+")) {
                        getLog().error("Unexpected exception occurred while parsing html snippet");
                        throw Context.reportRuntimeError("Unexpected exception occurred while parsing html snippet: "
                                + tagName);
                    }
                }

                final Page page = getDomNodeOrDie().getPage();
                final DomNode element;
                if (page instanceof HtmlPage) {
                    element = ((HtmlPage) page).createHtmlElement(tagName);
                }
                else {
                    element = ((XmlPage) page).createXmlElement(tagName);
                }
                final Object jsElement = getScriptableFor(element);

                if (jsElement == NOT_FOUND) {
                    getLog().debug("createElement(" + tagName
                            + ") cannot return a result as there isn't a javascript object for the element "
                            + element.getClass().getName());
                }
                else {
                    result = jsElement;
                }
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to NOT_FOUND
        }
        return result;
    }

    /**
     * Create a new HTML element with the given tag name, and name
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @return the new HTML element, or NOT_FOUND if the tag is not supported.
     */
    public Object jsxFunction_createElementNS(final String namespaceURI, final String qualifiedName) {
        Object result = NOT_FOUND;
        try {
            final HtmlElement htmlElement =
                ((HtmlPage) getDomNodeOrDie().getPage()).createHtmlElementNS(namespaceURI, qualifiedName);
            final Object jsElement = getScriptableFor(htmlElement);

            if (jsElement == NOT_FOUND) {
                getLog().debug("createElementNS(" + namespaceURI + ',' + qualifiedName
                    + ") cannot return a result as there isn't a javascript object for the HTML element "
                    + htmlElement.getClass().getName());
            }
            else {
                result = jsElement;
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to NOT_FOUND
        }
        return result;
    }

    /**
     * Create a new DocumentFragment
     * @return a newly created DocumentFragment.
     */
    public Object jsxFunction_createDocumentFragment() {
        final DomDocumentFragment fragment = ((SgmlPage) getDomNodeOrDie().getPage()).createDomDocumentFragment();
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
     * @return an attribute with the specified name.
     */
    public Attribute jsxFunction_createAttribute(final String attributeName) {
        final Attribute att = new Attribute();
        att.setPrototype(getPrototype(Attribute.class));
        att.setParentScope(getWindow());
        att.init(attributeName, null);
        return att;
    }

    /**
     * Creates a new Stylesheet.
     * Current implementation just creates an empty {@link Stylesheet} object.
     * @param url the stylesheet url
     * @param index where to insert the sheet in the collection
     * @return the newly created stylesheet
     */
    public Stylesheet jsxFunction_createStyleSheet(final String url, final int index) {
        // minimal implementation
        final Stylesheet stylesheet = new Stylesheet();
        stylesheet.setPrototype(getPrototype(Stylesheet.class));
        stylesheet.setParentScope(getWindow());

        return stylesheet;
    }

    /**
     * Create a new DOM text node with the given data.
     *
     * @param newData The string value for the text node.
     * @return the new text node or NOT_FOUND if there is an error.
     */
    public Object jsxFunction_createTextNode(final String newData) {
        Object result = NOT_FOUND;
        try {
            final DomNode domNode = new DomText(getDomNodeOrDie().getPage(), newData);
            final Object jsElement = getScriptableFor(domNode);

            if (jsElement == NOT_FOUND) {
                getLog().debug("createTextNode(" + newData
                    + ") cannot return a result as there isn't a javascript object for the DOM node "
                    + domNode.getClass().getName());
            }
            else {
                result = jsElement;
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to NOT_FOUND
        }
        return result;
    }

    /**
     * Returns the {@link BoxObject} for the specific element.
     *
     * @param element target for BoxObject.
     * @return the BoxObject
     */
    public BoxObject jsxFunction_getBoxObjectFor(final HTMLElement element) {
        return element.getBoxObject();
    }
    
    /**
     * Return the element with the specified id or null if that element could
     * not be found
     * @param id The ID to search for
     * @return the element or null
     */
    public Object jsxFunction_getElementById(final String id) {
        Object result = null;
        try {
            final HtmlElement htmlElement =
                ((HtmlPage) getDomNodeOrDie()).getDocumentHtmlElement().getHtmlElementById(id);
            final Object jsElement = getScriptableFor(htmlElement);

            if (jsElement == NOT_FOUND) {
                getLog().debug("getElementById(" + id
                    + ") cannot return a result as there isn't a javascript object for the HTML element "
                    + htmlElement.getClass().getName());
            }
            else {
                result = jsElement;
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to null

            final BrowserVersion browser = getBrowserVersion();
            if (browser.isIE()) {
                final HTMLCollection elements = (HTMLCollection) jsxFunction_getElementsByName(id);
                result = elements.get(0, elements);
                if (result instanceof UniqueTag) {
                    return null;
                }
                getLog().warn("getElementById(" + id + ") did a getElementByName for Internet Explorer");
                return result;
            }
            getLog().debug("getElementById(" + id + "): no DOM node found with this id");
        }
        return result;
    }

    /**
     * Returns all the descendant elements with the specified tag name.
     * @param tagName the name to search for
     * @return all the descendant elements with the specified tag name
     */
    public Object jsxFunction_getElementsByTagName(final String tagName) {
        return getHtmlPage().getElementsByTagName(tagName);
    }

    /**
     * Returns all HTML elements that have a "name" attribute with the given value.
     *
     * Refer to <a href="http://www.w3.org/TR/DOM-Level-2-HTML/html.html#ID-71555259">
     * The DOM spec</a> for details.
     *
     * @param elementName - value of the "name" attribute to look for
     * @return NodeList of elements
     */
    public Object jsxFunction_getElementsByName(final String elementName) {
        final HTMLCollection collection = new HTMLCollection(this);
        final String exp = ".//*[@name='" + elementName + "']";
        collection.init(getDomNodeOrDie(), exp);
        return collection;
    }

    /**
     * Imports a node from another document to this document.
     * The source node is not altered or removed from the original document;
     * this method creates a new copy of the source node.
     *
     * @param importedNode The node to import.
     * @param deep Whether to recursively import the subtree under the specified node; or not.
     * @return The imported node that belongs to this Document.
     */
    public Object jsxFunction_importNode(final Node importedNode, final boolean deep) {
        return ((DomNode) importedNode.getDomNodeOrDie().cloneNode(deep)).getScriptObject();
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
        if (page == null) {
            return NOT_FOUND;
        }
        // Try to satisfy this request using a map-backed operation before punting and using XPath.
        // XPath operations are very expensive, and this method gets invoked quite a bit.
        // This little shortcut shaves ~35% off the build time (3 min -> 2 min, as of 8/10/2007).
        final List<HtmlElement> elements = page.getHtmlElementsByName(name);
        if (elements.isEmpty()) {
            return NOT_FOUND;
        }
        if (elements.size() == 1) {
            final HtmlElement element = (HtmlElement) elements.get(0);
            final String tagName = element.getTagName();
            if (HtmlImage.TAG_NAME.equals(tagName) || HtmlForm.TAG_NAME.equals(tagName)) {
                return getScriptableFor(element);
            }
            else {
                return NOT_FOUND;
            }
        }
        // The shortcut wasn't enough, which means we probably need to perform the XPath operation anyway.
        // Note that the XPath expression below HAS TO MATCH the tag name checks performed in the shortcut above.
        // TODO: Behavior for iframe seems to differ between IE and Moz.
        final HTMLCollection collection = new HTMLCollection(this);
        final String xpath = ".//*[(@name = '" + name + "' and (name() = 'img' or name() = 'form'))]";
        collection.init(page, xpath);
        final int size = collection.jsxGet_length();
        if (size == 1) {
            return collection.get(0, collection);
        }
        else if (size > 1) {
            return collection;
        }
        return NOT_FOUND;
    }

    /**
     * Returns this document's <tt>body</tt> element.
     * @return this document's <tt>body</tt> element
     */
    public Object jsxGet_body() {
        final HtmlElement body = getHtmlPage().getBody();
        if (body != null) {
            return body.getScriptObject();
        }
        else {
            return null;
        }
    }

    /**
     * Returns this document's title.
     * @return this document's title
     */
    public String jsxGet_title() {
        return getHtmlPage().getTitleText();
    }

    /**
     * Sets this document's title.
     * @param title the new title
     */
    public void jsxSet_title(final String title) {
        getHtmlPage().setTitleText(title);
    }

    /**
     * Returns the ready state of the document. This is an IE-only property.
     * @return The ready state of the document.
     * @see DomNode#READY_STATE_UNINITIALIZED
     * @see DomNode#READY_STATE_LOADING
     * @see DomNode#READY_STATE_LOADED
     * @see DomNode#READY_STATE_INTERACTIVE
     * @see DomNode#READY_STATE_COMPLETE
     */
    public String jsxGet_readyState() {
        final DomNode node = getDomNodeOrDie();
        if (node instanceof HtmlPage) {
            return ((HtmlPage) node).getDocumentHtmlElement().getReadyState();
        }
        else {
            return node.getReadyState();
        }
    }

    /**
     * The domain name of the server that served the document,
     * or null if the server cannot be identified by a domain name.
     * @return domain name
     * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-2250147">
     * W3C documentation</a>
     */
    public String jsxGet_domain() {
        if (domain_ == null) {
            domain_ = getHtmlPage().getWebResponse().getUrl().getHost();
            final BrowserVersion browser = getBrowserVersion();
            if (browser.isNetscape()) {
                domain_ = domain_.toLowerCase();
            }
        }

        return domain_;
    }

    /**
     * Set the the domain of this document.
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
    public void jsxSet_domain(final String newDomain) {
        final String currentDomain = jsxGet_domain();
        if (currentDomain.equalsIgnoreCase(newDomain)) {
            return;
        }

        if (newDomain.indexOf(".") == -1
                || !currentDomain.toLowerCase().endsWith("." + newDomain.toLowerCase())) {
            throw Context.reportRuntimeError("Illegal domain value, can not set domain from: \""
                    + currentDomain + "\" to: \"" + newDomain + "\"");
        }

        // Netscape down shifts the case of the domain
        if (getBrowserVersion().isNetscape()) {
            domain_ = newDomain.toLowerCase();
        }
        else {
            domain_ = newDomain;
        }
    }

    /**
     * Return the value of the javascript attribute "scripts".
     * @return The value of this attribute.
     */
    public Object jsxGet_scripts() {
        if (scripts_ == null) {
            scripts_ = new HTMLCollection(this);
            scripts_.init(getDomNodeOrDie(), ".//script");
        }
        return scripts_;
    }

    /**
     * Return the value of the javascript attribute "selection".
     * @return The value of this attribute.
     */
    public Selection jsxGet_selection() {
        final Selection selection = new Selection();
        selection.setParentScope(getParentScope());
        selection.setPrototype(getPrototype(selection.getClass()));
        return selection;
    }

    /**
     * Return the value of the frames property.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/collections/frames.asp">
     * MSDN documentation</a>
     * @return The live collection of frames
     */
    public Object jsxGet_frames() {
        return getWindow().jsxGet_frames();
    }
    
    /**
     * Returns the implementation object of the current document.
     * @return implementation-specific object.
     */
    public DOMImplementation jsxGet_implementation() {
        if (implementation_ == null) {
            implementation_ = new DOMImplementation();
            implementation_.setParentScope(getWindow());
            implementation_.setPrototype(getPrototype(implementation_.getClass()));
        }
        return implementation_;
    }

    /**
     * Retrieves a collection of styleSheet objects representing the style sheets that correspond
     * to each instance of a Link or {@link CSSStyleDeclaration} object in the document.
     *
     * @return styleSheet collection.
     */
    public Object jsxGet_styleSheets() {
        if (styleSheets_ == null) {
            styleSheets_ = new StyleSheetList(this);
        }
        return styleSheets_;
    }

    /**
     * Implementation of the {@link DocumentEvent} interface's
     * {@link org.w3c.dom.events.DocumentEvent#createEvent(String)} method. The method creates an
     * event of the specified type.
     *
     * @link http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-DocumentEvent
     * @param eventType The event type to create.
     * @return The associated event object for that type. The event object will NOT have had its
     *         initialization method called. It is up to the caller of the method to initialize the
     *         event.
     * @throws DOMException Thrown if the event type is not supported. The DOMException will have a
     *         type of DOMException.NOT_SUPPORTED_ERR
     */
    public Event jsxFunction_createEvent(final String eventType) throws DOMException {
        final Class< ? extends Event> clazz = (Class< ? extends Event>) SUPPORTED_EVENT_TYPE_MAP.get(eventType);
        if (clazz == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Event Type is not supported: " + eventType);
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
     * Implementation of the <tt>createEventObject</tt> method supported by Internet Explorer.
     *
     * @link http://msdn2.microsoft.com/en-us/library/ms536390.aspx
     * @return An instance of the event object.  The event object will NOT have its
     *         member variables initialized.  It is up to the caller of the method to initialize
     *         the properties of the event.
     */
    public Event jsxFunction_createEventObject() {
        final Event event = new Event();
        event.setParentScope(getWindow());
        event.setPrototype(getPrototype(event.getClass()));
        return event;
    }

    /**
     * Returns the element for the specified x coordinate and the specified y coordinate.
     * The current implementation returns the &lt;body&gt; element.
     *
     * @param x Specifies the X-offset, in pixels.
     * @param y Specifies the Y-offset, in pixels.
     *
     * @return the element for the specified x coordinate and the specified y coordinate.
     */
    public Object jsxFunction_elementFromPoint(final int x, final int y) {
        return jsxGet_body();
    }

    /**
     * Create a new range.
     * @return the range.
     * @see <a href="http://www.xulplanet.com/references/objref/HTMLDocument.html#method_createRange">
     * XUL Planet</a>
     */
    public Object jsxFunction_createRange() {
        final Range r = new Range();
        r.setParentScope(getWindow());
        r.setPrototype(getPrototype(Range.class));
        return r;
    }

    /**
     * Adapts any DOM node to resolve namespaces so that an XPath expression
     * can be easily evaluated relative to the context of the node where it appeared within the document.
     * @param nodeResolver The node to be used as a context for namespace resolution.
     * @return XPathNSResolver which resolves namespaces with respect to the definitions in scope for a specified node.
     */
    public XPathNSResolver jsxFunction_createNSResolver(final Node nodeResolver) {
        final XPathNSResolver resolver = new XPathNSResolver();
        resolver.setElement(nodeResolver);
        resolver.setParentScope(getWindow());
        resolver.setPrototype(getPrototype(resolver.getClass()));
        return resolver;
    }
    
    /**
     * Evaluates an XPath expression string and returns a result of the specified type if possible.
     * @param expression The XPath expression string to be parsed and evaluated.
     * @param contextNode The context node for the evaluation of this XPath expression.
     * @param resolver The resolver permits translation of all prefixes, including the xml namespace prefix,
     *        within the XPath expression into appropriate namespace URIs.
     * @param type If a specific type is specified, then the result will be returned as the corresponding type.
     * @param result The result object which may be reused and returned by this method.
     * @return The result of the evaluation of the XPath expression.
     */
    public XPathResult jsxFunction_evaluate(final String expression, final Node contextNode,
            final Object resolver, final int type, final Object result) {
        XPathResult xPathResult = (XPathResult) result;
        if (xPathResult == null) {
            xPathResult = new XPathResult();
            xPathResult.setParentScope(getParentScope());
            xPathResult.setPrototype(getPrototype(xPathResult.getClass()));
        }
        xPathResult.init(contextNode.getDomNodeOrDie().getByXPath(expression), type);
        return xPathResult;
    }

    /**
     * Indicates if the command is supported.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536681.aspx">MSDN documentation</a>
     * @param cmd the command identifier
     * @return <code>true></code> if the command is supported
     */
    public boolean jsxFunction_queryCommandSupported(final String cmd) {
        if (!getBrowserVersion().isIE()) {
            // strangely the function exists in my FF 2.0.0.11 but always throws an exception
            throw Context.reportRuntimeError("queryCommandSupported not really supported by FF");
        }
        return EXECUTE_CMDS_IE.contains(cmd);
    }

    /**
     * Executes a command.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536419.aspx">MSDN documentation</a>
     * @param cmd the command identifier
     * @param userInterface display a user interface if the command supports one
     * @param value the string, number, or other value to assign. Possible values depend on the command
     * @return <code>true></code> if the command is successful
     */
    public boolean jsxFunction_execCommand(final String cmd, final boolean userInterface, final Object value) {
        if (!EXECUTE_CMDS_IE.contains(cmd)) {
            throw Context.reportRuntimeError("execCommand: invalid command >" + cmd + "<");
        }
        
        getLog().warn("Nothing done for execCommand(" + cmd + ", ...) (feature not implemented)");
        return true;
    }
}
