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
package org.htmlunit.javascript.host.html;

import static org.htmlunit.BrowserVersionFeatures.HTMLDOCUMENT_ELEMENTS_BY_NAME_EMPTY;
import static org.htmlunit.BrowserVersionFeatures.HTMLDOCUMENT_GET_ALSO_FRAMES;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.ScriptResult;
import org.htmlunit.StringWebResponse;
import org.htmlunit.WebClient;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.BaseFrameElement;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.FrameWindow;
import org.htmlunit.html.HtmlAttributeChangeEvent;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlScript;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.Element;
import org.htmlunit.javascript.host.dom.AbstractList.EffectOnCache;
import org.htmlunit.javascript.host.dom.Attr;
import org.htmlunit.javascript.host.dom.Document;
import org.htmlunit.javascript.host.dom.Node;
import org.htmlunit.javascript.host.dom.NodeList;
import org.htmlunit.javascript.host.dom.Selection;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.util.UrlUtils;

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
 * @author Sven Strickroth
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535862.aspx">MSDN documentation</a>
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-7068919">
 * W3C DOM Level 1</a>
 */
@JsxClass
public class HTMLDocument extends Document {

    private static final Log LOG = LogFactory.getLog(HTMLDocument.class);

    private enum ParsingStatus { OUTSIDE, START, IN_NAME, INSIDE, IN_STRING }

    /** The buffer that will be used for calls to document.write(). */
    private final StringBuilder writeBuilder_ = new StringBuilder();
    private boolean writeInCurrentDocument_ = true;

    private boolean closePostponedAction_;
    private boolean executionExternalPostponed_;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode getDomNodeOrDie() {
        try {
            return super.getDomNodeOrDie();
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.reportRuntimeError("No node attached to this object");
        }
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
     * JavaScript function "write" may accept a variable number of arguments.
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536782.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public static void write(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        final HTMLDocument thisAsDocument = getDocument(thisObj);
        thisAsDocument.write(concatArgsAsString(args));
    }

    /**
     * Converts the arguments to strings and concatenate them.
     * @param args the JavaScript arguments
     * @return the string concatenation
     */
    private static String concatArgsAsString(final Object[] args) {
        final StringBuilder builder = new StringBuilder();
        for (final Object arg : args) {
            builder.append(JavaScriptEngine.toString(arg));
        }
        return builder.toString();
    }

    /**
     * JavaScript function "writeln" may accept a variable number of arguments.
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536783.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public static void writeln(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        final HTMLDocument thisAsDocument = getDocument(thisObj);
        thisAsDocument.write(concatArgsAsString(args) + "\n");
    }

    /**
     * Returns the current document instance, using <code>thisObj</code> as a hint.
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
        if (thisObj instanceof DocumentProxy && thisObj.getPrototype() instanceof HTMLDocument) {
            return (HTMLDocument) ((DocumentProxy) thisObj).getDelegee();
        }

        throw JavaScriptEngine.reportRuntimeError("Function can't be used detached from document");
    }

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
     * <p>
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
            LOG.debug("wrote content to buffer");
            scheduleImplicitClose();
            return;
        }
        final String bufferedContent = writeBuilder_.toString();
        if (!canAlreadyBeParsed(bufferedContent)) {
            LOG.debug("write: not enough content to parse it now");
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
            page.getWebClient().getJavaScriptEngine().addPostponedAction(
                    new PostponedAction(page, "HTMLDocument.scheduleImplicitClose") {
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
                final StringBuilder message = new StringBuilder()
                    .append("canAlreadyBeParsed() retruns false for content: '")
                    .append(StringUtils.abbreviateMiddle(content, ".", 100))
                    .append("' (scriptTagCount: ")
                        .append(scriptTagCount)
                    .append(" tagState: ")
                        .append(tagState)
                    .append(')');
                LOG.debug(message.toString());
            }
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
        if (!(lastChild instanceof HtmlElement)
                || lastChild instanceof HtmlScript) {
            return node;
        }

        return getLastHtmlElement((HtmlElement) lastChild);
    }

    /**
     * JavaScript function "open".
     * <p>
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
    @JsxFunction
    public HTMLDocument open(final Object url, final Object name, final Object features,
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
        final WebWindow ww = getWindow().getWebWindow();
        if (ww instanceof FrameWindow
                && UrlUtils.ABOUT_BLANK.equals(getPage().getUrl().toExternalForm())) {
            final URL enclosingUrl = ((FrameWindow) ww).getEnclosingPage().getUrl();
            getPage().getWebResponse().getWebRequest().setUrl(enclosingUrl);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction({FF, FF_ESR})
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
            // reset isAttachedToPageDuringOnload_ to trigger the onload event for chrome also
            if (window instanceof FrameWindow) {
                final BaseFrameElement frame = ((FrameWindow) window).getFrameElement();
                final HtmlUnitScriptable scriptable = frame.getScriptableObject();
                if (scriptable instanceof HTMLIFrameElement) {
                    ((HTMLIFrameElement) scriptable).onRefresh();
                }
            }
            webClient.loadWebResponseInto(webResponse, window);
        }
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public Element getDocumentElement() {
        implicitCloseIfNecessary();
        return super.getDocumentElement();
    }

    /**
     * Closes the document implicitly, i.e. flushes the <code>document.write</code> buffer (IE only).
     */
    private void implicitCloseIfNecessary() {
        if (!writeInCurrentDocument_) {
            try {
                close();
            }
            catch (final IOException e) {
                throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node appendChild(final Object childObject) {
        throw JavaScriptEngine.reportRuntimeError("Node cannot be inserted at the specified point in the hierarchy.");
    }

    /**
     * Returns the element with the specified ID, or {@code null} if that element could not be found.
     * @param id the ID to search for
     * @return the element, or {@code null} if it could not be found
     */
    @JsxFunction
    @Override
    public HtmlUnitScriptable getElementById(final String id) {
        implicitCloseIfNecessary();
        final DomElement domElement = getPage().getElementById(id);
        if (null == domElement) {
            // Just fall through - result is already set to null
            if (LOG.isDebugEnabled()) {
                LOG.debug("getElementById(" + id + "): no DOM node found with this id");
            }
            return null;
        }

        final HtmlUnitScriptable jsElement = getScriptableFor(domElement);
        if (jsElement == NOT_FOUND) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getElementById(" + id
                        + ") cannot return a result as there isn't a JavaScript object for the HTML element "
                        + domElement.getClass().getName());
            }
            return null;
        }
        return jsElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLCollection getElementsByClassName(final String className) {
        return getDocumentElement().getElementsByClassName(className);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeList getElementsByName(final String elementName) {
        implicitCloseIfNecessary();

        if ("null".equals(elementName)
                || (elementName.isEmpty()
                    && getBrowserVersion().hasFeature(HTMLDOCUMENT_ELEMENTS_BY_NAME_EMPTY))) {
            return NodeList.staticNodeList(getWindow(), new ArrayList<>());
        }

        final HtmlPage page = getPage();
        final NodeList elements = new NodeList(page, true);
        elements.setElementsSupplier(
                (Supplier<List<DomNode>> & Serializable)
                () -> new ArrayList<>(page.getElementsByName(elementName)));

        elements.setEffectOnCacheFunction(
                (java.util.function.Function<HtmlAttributeChangeEvent, EffectOnCache> & Serializable)
                event -> {
                    if ("name".equals(event.getName())) {
                        return EffectOnCache.RESET;
                    }
                    return EffectOnCache.NONE;
                });

        return elements;
    }

    /**
     * Calls to <code>document.XYZ</code> should first look at elements named <code>XYZ</code> before
     * using standard functions.
     * <p>
     * {@inheritDoc}
     */
    @Override
    protected Object getWithPreemption(final String name) {
        final HtmlPage page = (HtmlPage) getDomNodeOrNull();
        if (page == null) {
            final Object response = getPrototype().get(name, this);
            if (response != NOT_FOUND) {
                return response;
            }
        }
        return getIt(name);
    }

    private Object getIt(final String name) {
        final HtmlPage page = (HtmlPage) getDomNodeOrNull();
        if (page == null) {
            return NOT_FOUND;
        }

        final boolean alsoFrames = getBrowserVersion().hasFeature(HTMLDOCUMENT_GET_ALSO_FRAMES);

        // for performance
        // we will calculate the elements to decide if we really have
        // to really create a HTMLCollection or not
        final List<DomNode> matchingElements = getItComputeElements(page, name, alsoFrames);
        final int size = matchingElements.size();
        if (size == 0) {
            return NOT_FOUND;
        }
        if (size == 1) {
            final DomNode object = matchingElements.get(0);
            if (alsoFrames && object instanceof BaseFrameElement) {
                return ((BaseFrameElement) object).getEnclosedWindow().getScriptableObject();
            }
            return super.getScriptableFor(object);
        }

        final HTMLCollection coll = new HTMLCollection(page, matchingElements) {
            @Override
            protected HtmlUnitScriptable getScriptableFor(final Object object) {
                if (alsoFrames && object instanceof BaseFrameElement) {
                    return ((BaseFrameElement) object).getEnclosedWindow().getScriptableObject();
                }
                return super.getScriptableFor(object);
            }
        };

        coll.setElementsSupplier(
                (Supplier<List<DomNode>> & Serializable)
                () -> getItComputeElements(page, name, alsoFrames));

        coll.setEffectOnCacheFunction(
                (java.util.function.Function<HtmlAttributeChangeEvent, EffectOnCache> & Serializable)
                event -> {
                    final String attributeName = event.getName();
                    if (DomElement.NAME_ATTRIBUTE.equals(attributeName)) {
                        return EffectOnCache.RESET;
                    }

                    return EffectOnCache.NONE;
                });

        return coll;
    }

    static List<DomNode> getItComputeElements(final HtmlPage page, final String name,
            final boolean alsoFrames) {
        final List<DomElement> elements = page.getElementsByName(name);
        final List<DomNode> matchingElements = new ArrayList<>();
        for (final DomElement elt : elements) {
            if (elt instanceof HtmlForm || elt instanceof HtmlImage
                    || (alsoFrames && elt instanceof BaseFrameElement)) {
                matchingElements.add(elt);
            }
        }
        return matchingElements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLElement getHead() {
        final HtmlElement head = getPage().getHead();
        if (head == null) {
            return null;
        }
        return head.getScriptableObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return getPage().getTitleText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(final String title) {
        getPage().setTitleText(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLElement getActiveElement() {
        final HtmlElement activeElement = getPage().getActiveElement();
        if (activeElement != null) {
            return activeElement.getScriptableObject();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFocus() {
        return getPage().getFocusedElement() != null;
    }

    /**
     * Dispatches an event into the event system (standards-conformant browsers only). See
     * <a href="https://developer.mozilla.org/en-US/docs/DOM/element.dispatchEvent">the Gecko
     * DOM reference</a> for more information.
     *
     * @param event the event to be dispatched
     * @return {@code false} if at least one of the event handlers which handled the event
     *         called <code>preventDefault</code>; {@code true} otherwise
     */
    @Override
    @JsxFunction
    public boolean dispatchEvent(final Event event) {
        event.setTarget(this);
        final ScriptResult result = fireEvent(event);
        return !event.isAborted(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Selection getSelection() {
        return getWindow().getSelectionImpl();
    }

    /**
     * Creates a new HTML attribute with the specified name.
     *
     * @param attributeName the name of the attribute to create
     * @return an attribute with the specified name
     */
    @Override
    public Attr createAttribute(final String attributeName) {
        String name = attributeName;
        if (StringUtils.isNotEmpty(name)) {
            name = org.htmlunit.util.StringUtils.toRootLowerCase(name);
        }

        return super.createAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBaseURI() {
        return getPage().getBaseURL().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlUnitScriptable elementFromPoint(final int x, final int y) {
        final HtmlElement element = getPage().getElementFromPoint(x, y);
        return element == null ? null : element.getScriptableObject();
    }
}
