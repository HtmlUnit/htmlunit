/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.html;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.htmlunit.BrowserVersionFeatures.EVENT_FOCUS_FOCUS_IN_BLUR_OUT;
import static org.htmlunit.BrowserVersionFeatures.EVENT_FOCUS_IN_FOCUS_OUT_BLUR;
import static org.htmlunit.BrowserVersionFeatures.EVENT_FOCUS_ON_LOAD;
import static org.htmlunit.BrowserVersionFeatures.FOCUS_BODY_ELEMENT_AT_START;
import static org.htmlunit.BrowserVersionFeatures.HTTP_HEADER_SEC_FETCH;
import static org.htmlunit.BrowserVersionFeatures.JS_EVENT_LOAD_SUPPRESSED_BY_CONTENT_SECURIRY_POLICY;
import static org.htmlunit.BrowserVersionFeatures.JS_IGNORES_UTF8_BOM_SOMETIMES;
import static org.htmlunit.BrowserVersionFeatures.PAGE_SELECTION_RANGE_FROM_SELECTABLE_TEXT_INPUT;
import static org.htmlunit.BrowserVersionFeatures.URL_MISSING_SLASHES;
import static org.htmlunit.html.DisabledElement.ATTRIBUTE_DISABLED;
import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.BrowserVersion;
import org.htmlunit.Cache;
import org.htmlunit.ElementNotFoundException;
import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.History;
import org.htmlunit.HttpHeader;
import org.htmlunit.OnbeforeunloadHandler;
import org.htmlunit.Page;
import org.htmlunit.ScriptResult;
import org.htmlunit.SgmlPage;
import org.htmlunit.TopLevelWindow;
import org.htmlunit.WebAssert;
import org.htmlunit.WebClient;
import org.htmlunit.WebClientOptions;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Script;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.Undefined;
import org.htmlunit.css.ComputedCssStyleDeclaration;
import org.htmlunit.css.CssStyleSheet;
import org.htmlunit.html.FrameWindow.PageDenied;
import org.htmlunit.html.impl.SelectableTextInput;
import org.htmlunit.html.impl.SimpleRange;
import org.htmlunit.html.parser.HTMLParserDOMBuilder;
import org.htmlunit.httpclient.HttpClientConverter;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.HtmlUnitContextFactory;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.event.BeforeUnloadEvent;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.javascript.host.html.HTMLDocument;
import org.htmlunit.protocol.javascript.JavaScriptURLConnection;
import org.htmlunit.util.EncodingSniffer;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.SerializableLock;
import org.htmlunit.util.UrlUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.ProcessingInstruction;

/**
 * A representation of an HTML page returned from a server.
 * <p>
 * This class provides different methods to access the page's content like
 * {@link #getForms()}, {@link #getAnchors()}, {@link #getElementById(String)}, ... as well as the
 * very powerful inherited methods {@link #getByXPath(String)} and {@link #getFirstByXPath(String)}
 * for fine grained user specific access to child nodes.
 * </p>
 * <p>
 * Child elements allowing user interaction provide methods for this purpose like {@link HtmlAnchor#click()},
 * {@link HtmlInput#type(String)}, {@link HtmlOption#setSelected(boolean)}, ...
 * </p>
 * <p>
 * HtmlPage instances should not be instantiated directly. They will be returned by {@link WebClient#getPage(String)}
 * when the content type of the server's response is <code>text/html</code> (or one of its variations).<br>
 * <br>
 * <b>Example:</b><br>
 * <br>
 * <code>
 * final HtmlPage page = webClient.{@link WebClient#getPage(String) getPage}("http://mywebsite/some/page.html");
 * </code>
 * </p>
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Alex Nikiforoff
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author Andreas Hangler
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Dmitri Zoubkov
 * @author Sudhan Moghe
 * @author Ethan Glasser-Camp
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 * @author Ronald Brill
 * @author Frank Danek
 * @author Joerg Werner
 * @author Atsushi Nakagawa
 * @author Rural Hunter
 */
public class HtmlPage extends SgmlPage {

    private static final Log LOG = LogFactory.getLog(HtmlPage.class);

    private static final Comparator<DomElement> documentPositionComparator = new DocumentPositionComparator();

    private HTMLParserDOMBuilder domBuilder_;
    private transient Charset originalCharset_;
    private final Object lock_ = new SerializableLock(); // used for synchronization

    private Map<String, SortedSet<DomElement>> idMap_ = new ConcurrentHashMap<>();
    private Map<String, SortedSet<DomElement>> nameMap_ = new ConcurrentHashMap<>();

    private SortedSet<BaseFrameElement> frameElements_ = new TreeSet<>(documentPositionComparator);
    private int parserCount_;
    private int snippetParserCount_;
    private int inlineSnippetParserCount_;
    private Collection<HtmlAttributeChangeListener> attributeListeners_;
    private List<PostponedAction> afterLoadActions_ = Collections.synchronizedList(new ArrayList<>());
    private boolean cleaning_;
    private HtmlBase base_;
    private URL baseUrl_;
    private List<AutoCloseable> autoCloseableList_;
    private ElementFromPointHandler elementFromPointHandler_;
    private DomElement elementWithFocus_;
    private List<SimpleRange> selectionRanges_ = new ArrayList<>(3);

    private transient ComputedStylesCache computedStylesCache_;

    private static final HashSet<String> TABBABLE_TAGS =
            new HashSet<>(Arrays.asList(HtmlAnchor.TAG_NAME, HtmlArea.TAG_NAME,
                    HtmlButton.TAG_NAME, HtmlInput.TAG_NAME, HtmlObject.TAG_NAME,
                    HtmlSelect.TAG_NAME, HtmlTextArea.TAG_NAME));
    private static final HashSet<String> ACCEPTABLE_TAG_NAMES =
            new HashSet<>(Arrays.asList(HtmlAnchor.TAG_NAME, HtmlArea.TAG_NAME,
                    HtmlButton.TAG_NAME, HtmlInput.TAG_NAME, HtmlLabel.TAG_NAME,
                    HtmlLegend.TAG_NAME, HtmlTextArea.TAG_NAME));

    /** Definition of special cases for the smart DomHtmlAttributeChangeListenerImpl */
    private static final Set<String> ATTRIBUTES_AFFECTING_PARENT = new HashSet<>(Arrays.asList(
            "style",
            "class",
            "height",
            "width"));

    static class DocumentPositionComparator implements Comparator<DomElement>, Serializable {
        @Override
        public int compare(final DomElement elt1, final DomElement elt2) {
            final short relation = elt1.compareDocumentPosition(elt2);
            if (relation == 0) {
                return 0; // same node
            }
            if ((relation & DOCUMENT_POSITION_CONTAINS) != 0 || (relation & DOCUMENT_POSITION_PRECEDING) != 0) {
                return 1;
            }

            return -1;
        }
    }

    /**
     * Creates an instance of HtmlPage.
     * An HtmlPage instance is normally retrieved with {@link WebClient#getPage(String)}.
     *
     * @param webResponse the web response that was used to create this page
     * @param webWindow the window that this page is being loaded into
     */
    public HtmlPage(final WebResponse webResponse, final WebWindow webWindow) {
        super(webResponse, webWindow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPage getPage() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasCaseSensitiveTagNames() {
        return false;
    }

    /**
     * Initialize this page.
     * @throws IOException if an IO problem occurs
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     * {@link org.htmlunit.WebClientOptions#setThrowExceptionOnFailingStatusCode(boolean)} is set
     * to true.
     */
    @Override
    public void initialize() throws IOException, FailingHttpStatusCodeException {
        final WebWindow enclosingWindow = getEnclosingWindow();
        final boolean isAboutBlank = getUrl() == UrlUtils.URL_ABOUT_BLANK;
        if (isAboutBlank) {
            // a frame contains first a faked "about:blank" before its real content specified by src gets loaded
            if (enclosingWindow instanceof FrameWindow
                    && !((FrameWindow) enclosingWindow).getFrameElement().isContentLoaded()) {
                return;
            }

            // save the URL that should be used to resolve relative URLs in this page
            if (enclosingWindow instanceof TopLevelWindow) {
                final TopLevelWindow topWindow = (TopLevelWindow) enclosingWindow;
                final WebWindow openerWindow = topWindow.getOpener();
                if (openerWindow != null && openerWindow.getEnclosedPage() != null) {
                    baseUrl_ = openerWindow.getEnclosedPage().getWebResponse().getWebRequest().getUrl();
                }
            }
        }

        if (!isAboutBlank) {
            setReadyState(READY_STATE_INTERACTIVE);
            getDocumentElement().setReadyState(READY_STATE_INTERACTIVE);
            executeEventHandlersIfNeeded(Event.TYPE_READY_STATE_CHANGE);
        }

        executeDeferredScriptsIfNeeded();

        executeEventHandlersIfNeeded(Event.TYPE_DOM_DOCUMENT_LOADED);

        loadFrames();

        // don't set the ready state if we really load the blank page into the window
        // see Node.initInlineFrameIfNeeded()
        if (!isAboutBlank) {
            if (hasFeature(FOCUS_BODY_ELEMENT_AT_START)) {
                setElementWithFocus(getBody());
            }
            setReadyState(READY_STATE_COMPLETE);
            getDocumentElement().setReadyState(READY_STATE_COMPLETE);
            executeEventHandlersIfNeeded(Event.TYPE_READY_STATE_CHANGE);
        }

        // frame initialization has a different order
        boolean isFrameWindow = enclosingWindow instanceof FrameWindow;
        boolean isFirstPageInFrameWindow = false;
        if (isFrameWindow) {
            isFrameWindow = ((FrameWindow) enclosingWindow).getFrameElement() instanceof HtmlFrame;

            final History hist = enclosingWindow.getHistory();
            if (hist.getLength() > 0 && UrlUtils.URL_ABOUT_BLANK == hist.getUrl(0)) {
                isFirstPageInFrameWindow = hist.getLength() <= 2;
            }
            else {
                isFirstPageInFrameWindow = enclosingWindow.getHistory().getLength() < 2;
            }
        }

        if (isFrameWindow && !isFirstPageInFrameWindow) {
            executeEventHandlersIfNeeded(Event.TYPE_LOAD);
        }

        for (final FrameWindow frameWindow : getFrames()) {
            if (frameWindow.getFrameElement() instanceof HtmlFrame) {
                final Page page = frameWindow.getEnclosedPage();
                if (page != null && page.isHtmlPage()) {
                    ((HtmlPage) page).executeEventHandlersIfNeeded(Event.TYPE_LOAD);
                }
            }
        }

        if (!isFrameWindow) {
            executeEventHandlersIfNeeded(Event.TYPE_LOAD);

            if (!isAboutBlank && enclosingWindow.getWebClient().isJavaScriptEnabled()
                    && hasFeature(EVENT_FOCUS_ON_LOAD)) {
                final HtmlElement body = getBody();
                if (body != null) {
                    final Event event = new Event((Window) enclosingWindow.getScriptableObject(), Event.TYPE_FOCUS);
                    body.fireEvent(event);
                }
            }
        }

        try {
            while (!afterLoadActions_.isEmpty()) {
                final PostponedAction action = afterLoadActions_.remove(0);
                action.execute();
            }
        }
        catch (final IOException e) {
            throw e;
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
        executeRefreshIfNeeded();
    }

    /**
     * Adds an action that should be executed once the page has been loaded.
     * @param action the action
     */
    void addAfterLoadAction(final PostponedAction action) {
        afterLoadActions_.add(action);
    }

    /**
     * Clean up this page.
     */
    @Override
    public void cleanUp() {
        //To avoid endless recursion caused by window.close() in onUnload
        if (cleaning_) {
            return;
        }

        cleaning_ = true;
        try {
            super.cleanUp();
            executeEventHandlersIfNeeded(Event.TYPE_UNLOAD);
            deregisterFramesIfNeeded();
        }
        finally {
            cleaning_ = false;

            if (autoCloseableList_ != null) {
                for (final AutoCloseable closeable : new ArrayList<>(autoCloseableList_)) {
                    try {
                        closeable.close();
                    }
                    catch (final Exception e) {
                        LOG.error("Closing the autoclosable " + closeable + " failed", e);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlElement getDocumentElement() {
        return (HtmlElement) super.getDocumentElement();
    }

    /**
     * Returns the <code>body</code> element (or <code>frameset</code> element),
     * or {@code null} if it does not yet exist.
     * @return the <code>body</code> element (or <code>frameset</code> element),
     * or {@code null} if it does not yet exist
     */
    public HtmlElement getBody() {
        final DomElement doc = getDocumentElement();
        if (doc != null) {
            for (final DomNode node : doc.getChildren()) {
                if (node instanceof HtmlBody || node instanceof HtmlFrameSet) {
                    return (HtmlElement) node;
                }
            }
        }
        return null;
    }

    /**
     * Returns the head element.
     * @return the head element
     */
    public HtmlElement getHead() {
        final DomElement doc = getDocumentElement();
        if (doc != null) {
            for (final DomNode node : doc.getChildren()) {
                if (node instanceof HtmlHead) {
                    return (HtmlElement) node;
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document getOwnerDocument() {
        return null;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public org.w3c.dom.Node importNode(final org.w3c.dom.Node importedNode, final boolean deep) {
        throw new UnsupportedOperationException("HtmlPage.importNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String getInputEncoding() {
        throw new UnsupportedOperationException("HtmlPage.getInputEncoding is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXmlEncoding() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getXmlStandalone() {
        return false;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setXmlStandalone(final boolean xmlStandalone) throws DOMException {
        throw new UnsupportedOperationException("HtmlPage.setXmlStandalone is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXmlVersion() {
        return null;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setXmlVersion(final String xmlVersion) throws DOMException {
        throw new UnsupportedOperationException("HtmlPage.setXmlVersion is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public boolean getStrictErrorChecking() {
        throw new UnsupportedOperationException("HtmlPage.getStrictErrorChecking is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setStrictErrorChecking(final boolean strictErrorChecking) {
        throw new UnsupportedOperationException("HtmlPage.setStrictErrorChecking is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String getDocumentURI() {
        throw new UnsupportedOperationException("HtmlPage.getDocumentURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setDocumentURI(final String documentURI) {
        throw new UnsupportedOperationException("HtmlPage.setDocumentURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public org.w3c.dom.Node adoptNode(final org.w3c.dom.Node source) throws DOMException {
        throw new UnsupportedOperationException("HtmlPage.adoptNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public DOMConfiguration getDomConfig() {
        throw new UnsupportedOperationException("HtmlPage.getDomConfig is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public org.w3c.dom.Node renameNode(final org.w3c.dom.Node newNode, final String namespaceURI,
        final String qualifiedName) throws DOMException {
        throw new UnsupportedOperationException("HtmlPage.renameNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Charset getCharset() {
        if (originalCharset_ == null) {
            originalCharset_ = getWebResponse().getContentCharset();
        }
        return originalCharset_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentType() {
        return getWebResponse().getContentType();
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public DOMImplementation getImplementation() {
        throw new UnsupportedOperationException("HtmlPage.getImplementation is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * @param tagName the tag name, preferably in lowercase
     */
    @Override
    public DomElement createElement(String tagName) {
        if (tagName.indexOf(':') == -1) {
            tagName = org.htmlunit.util.StringUtils.toRootLowerCaseWithCache(tagName);
        }
        return getWebClient().getPageCreator().getHtmlParser().getFactory(tagName)
                    .createElementNS(this, null, tagName, null, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement createElementNS(final String namespaceURI, final String qualifiedName) {
        return getWebClient().getPageCreator().getHtmlParser()
                .getElementFactory(this, namespaceURI, qualifiedName, false, true)
                .createElementNS(this, namespaceURI, qualifiedName, null, true);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Attr createAttributeNS(final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("HtmlPage.createAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public EntityReference createEntityReference(final String id) {
        throw new UnsupportedOperationException("HtmlPage.createEntityReference is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public ProcessingInstruction createProcessingInstruction(final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("HtmlPage.createProcessingInstruction is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getElementById(final String elementId) {
        final SortedSet<DomElement> elements = idMap_.get(elementId);
        if (elements != null) {
            return elements.first();
        }
        return null;
    }

    /**
     * Returns the {@link HtmlAnchor} with the specified name.
     *
     * @param name the name to search by
     * @return the {@link HtmlAnchor} with the specified name
     * @throws ElementNotFoundException if the anchor could not be found
     */
    public HtmlAnchor getAnchorByName(final String name) throws ElementNotFoundException {
        return getDocumentElement().getOneHtmlElementByAttribute("a", DomElement.NAME_ATTRIBUTE, name);
    }

    /**
     * Returns the {@link HtmlAnchor} with the specified href.
     *
     * @param href the string to search by
     * @return the HtmlAnchor
     * @throws ElementNotFoundException if the anchor could not be found
     */
    public HtmlAnchor getAnchorByHref(final String href) throws ElementNotFoundException {
        return getDocumentElement().getOneHtmlElementByAttribute("a", "href", href);
    }

    /**
     * Returns a list of all anchors contained in this page.
     * @return the list of {@link HtmlAnchor} in this page
     */
    public List<HtmlAnchor> getAnchors() {
        return getDocumentElement().getElementsByTagNameImpl("a");
    }

    /**
     * Returns the first anchor with the specified text.
     * @param text the text to search for
     * @return the first anchor that was found
     * @throws ElementNotFoundException if no anchors are found with the specified text
     */
    public HtmlAnchor getAnchorByText(final String text) throws ElementNotFoundException {
        WebAssert.notNull("text", text);

        for (final HtmlAnchor anchor : getAnchors()) {
            if (text.equals(anchor.asNormalizedText())) {
                return anchor;
            }
        }
        throw new ElementNotFoundException("a", "<text>", text);
    }

    /**
     * Returns the first form that matches the specified name.
     * @param name the name to search for
     * @return the first form
     * @exception ElementNotFoundException If no forms match the specified result.
     */
    public HtmlForm getFormByName(final String name) throws ElementNotFoundException {
        final List<HtmlForm> forms = getDocumentElement()
                .getElementsByAttribute("form", DomElement.NAME_ATTRIBUTE, name);
        if (forms.isEmpty()) {
            throw new ElementNotFoundException("form", DomElement.NAME_ATTRIBUTE, name);
        }
        return forms.get(0);
    }

    /**
     * Returns a list of all the forms in this page.
     * @return all the forms in this page
     */
    public List<HtmlForm> getForms() {
        return getDocumentElement().getElementsByTagNameImpl("form");
    }

    /**
     * Given a relative URL (ie <code>/foo</code>), returns a fully-qualified URL based on
     * the URL that was used to load this page.
     *
     * @param relativeUrl the relative URL
     * @return the fully-qualified URL for the specified relative URL
     * @exception MalformedURLException if an error occurred when creating a URL object
     */
    public URL getFullyQualifiedUrl(String relativeUrl) throws MalformedURLException {
        // to handle http: and http:/ in FF (Bug #474)
        if (hasFeature(URL_MISSING_SLASHES)) {
            boolean incorrectnessNotified = false;
            while (relativeUrl.startsWith("http:") && !relativeUrl.startsWith("http://")) {
                if (!incorrectnessNotified) {
                    notifyIncorrectness("Incorrect URL \"" + relativeUrl + "\" has been corrected");
                    incorrectnessNotified = true;
                }
                relativeUrl = "http:/" + relativeUrl.substring(5);
            }
        }

        return WebClient.expandUrl(getBaseURL(), relativeUrl);
    }

    /**
     * Given a target attribute value, resolve the target using a base target for the page.
     *
     * @param elementTarget the target specified as an attribute of the element
     * @return the resolved target to use for the element
     */
    public String getResolvedTarget(final String elementTarget) {
        final String resolvedTarget;
        if (base_ == null) {
            resolvedTarget = elementTarget;
        }
        else if (elementTarget != null && !elementTarget.isEmpty()) {
            resolvedTarget = elementTarget;
        }
        else {
            resolvedTarget = base_.getTargetAttribute();
        }
        return resolvedTarget;
    }

    /**
     * Returns a list of ids (strings) that correspond to the tabbable elements
     * in this page. Return them in the same order specified in {@link #getTabbableElements}
     *
     * @return the list of id's
     */
    public List<String> getTabbableElementIds() {
        final List<String> list = new ArrayList<>();

        for (final HtmlElement element : getTabbableElements()) {
            list.add(element.getId());
        }

        return Collections.unmodifiableList(list);
    }

    /**
     * Returns a list of all elements that are tabbable in the order that will
     * be used for tabbing.<p>
     *
     * The rules for determining tab order are as follows:
     * <ol>
     *   <li>Those elements that support the tabindex attribute and assign a
     *   positive value to it are navigated first. Navigation proceeds from the
     *   element with the lowest tabindex value to the element with the highest
     *   value. Values need not be sequential nor must they begin with any
     *   particular value. Elements that have identical tabindex values should
     *   be navigated in the order they appear in the character stream.
     *   <li>Those elements that do not support the tabindex attribute or
     *   support it and assign it a value of "0" are navigated next. These
     *   elements are navigated in the order they appear in the character
     *   stream.
     *   <li>Elements that are disabled do not participate in the tabbing
     *   order.
     * </ol>
     * Additionally, the value of tabindex must be within 0 and 32767. Any
     * values outside this range will be ignored.<p>
     *
     * The following elements support the <code>tabindex</code> attribute: A, AREA, BUTTON,
     * INPUT, OBJECT, SELECT, and TEXTAREA.<p>
     *
     * @return all the tabbable elements in proper tab order
     */
    public List<HtmlElement> getTabbableElements() {
        final List<HtmlElement> tabbableElements = new ArrayList<>();
        for (final HtmlElement element : getHtmlElementDescendants()) {
            final String tagName = element.getTagName();
            if (TABBABLE_TAGS.contains(tagName)) {
                final boolean disabled = element.hasAttribute(ATTRIBUTE_DISABLED);
                if (!disabled && !HtmlElement.TAB_INDEX_OUT_OF_BOUNDS.equals(element.getTabIndex())) {
                    tabbableElements.add(element);
                }
            }
        }
        tabbableElements.sort(createTabOrderComparator());
        return Collections.unmodifiableList(tabbableElements);
    }

    private static Comparator<HtmlElement> createTabOrderComparator() {
        return (element1, element2) -> {
            final Short i1 = element1.getTabIndex();
            final Short i2 = element2.getTabIndex();

            final short index1;
            if (i1 == null) {
                index1 = -1;
            }
            else {
                index1 = i1.shortValue();
            }

            final short index2;
            if (i2 == null) {
                index2 = -1;
            }
            else {
                index2 = i2.shortValue();
            }

            final int result;
            if (index1 > 0 && index2 > 0) {
                result = index1 - index2;
            }
            else if (index1 > 0) {
                result = -1;
            }
            else if (index2 > 0) {
                result = 1;
            }
            else if (index1 == index2) {
                result = 0;
            }
            else {
                result = index2 - index1;
            }

            return result;
        };
    }

    /**
     * Returns the HTML element that is assigned to the specified access key. An
     * access key (aka mnemonic key) is used for keyboard navigation of the
     * page.<p>
     *
     * Only the following HTML elements may have <code>accesskey</code>s defined: A, AREA,
     * BUTTON, INPUT, LABEL, LEGEND, and TEXTAREA.
     *
     * @param accessKey the key to look for
     * @return the HTML element that is assigned to the specified key or null
     *      if no elements can be found that match the specified key.
     */
    public HtmlElement getHtmlElementByAccessKey(final char accessKey) {
        final List<HtmlElement> elements = getHtmlElementsByAccessKey(accessKey);
        if (elements.isEmpty()) {
            return null;
        }
        return elements.get(0);
    }

    /**
     * Returns all the HTML elements that are assigned to the specified access key. An
     * access key (aka mnemonic key) is used for keyboard navigation of the
     * page.<p>
     *
     * The HTML specification seems to indicate that one accesskey cannot be used
     * for multiple elements however Internet Explorer does seem to support this.
     * It's worth noting that Firefox does not support multiple elements with one
     * access key so you are making your HTML browser specific if you rely on this
     * feature.<p>
     *
     * Only the following HTML elements may have <code>accesskey</code>s defined: A, AREA,
     * BUTTON, INPUT, LABEL, LEGEND, and TEXTAREA.
     *
     * @param accessKey the key to look for
     * @return the elements that are assigned to the specified accesskey
     */
    public List<HtmlElement> getHtmlElementsByAccessKey(final char accessKey) {
        final List<HtmlElement> elements = new ArrayList<>();

        final String searchString = Character.toString(accessKey).toLowerCase(Locale.ROOT);
        for (final HtmlElement element : getHtmlElementDescendants()) {
            if (ACCEPTABLE_TAG_NAMES.contains(element.getTagName())) {
                final String accessKeyAttribute = element.getAttributeDirect("accesskey");
                if (searchString.equalsIgnoreCase(accessKeyAttribute)) {
                    elements.add(element);
                }
            }
        }

        return elements;
    }

    /**
     * <p>Executes the specified JavaScript code within the page. The usage would be similar to what can
     * be achieved to execute JavaScript in the current page by entering "javascript:...some JS code..."
     * in the URL field of a native browser.</p>
     * <p><b>Note:</b> the provided code won't be executed if JavaScript has been disabled on the WebClient
     * (see {@link org.htmlunit.WebClient#isJavaScriptEnabled()}.</p>
     * @param sourceCode the JavaScript code to execute
     * @return a ScriptResult which will contain both the current page (which may be different than
     * the previous page) and a JavaScript result object
     */
    public ScriptResult executeJavaScript(final String sourceCode) {
        return executeJavaScript(sourceCode, "injected script", 1);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * <p>
     * Execute the specified JavaScript if a JavaScript engine was successfully
     * instantiated. If this JavaScript causes the current page to be reloaded
     * (through location="" or form.submit()) then return the new page. Otherwise
     * return the current page.
     * </p>
     * <p><b>Please note:</b> Although this method is public, it is not intended for
     * general execution of JavaScript. Users of HtmlUnit should interact with the pages
     * as a user would by clicking on buttons or links and having the JavaScript event
     * handlers execute as needed..
     * </p>
     *
     * @param sourceCode the JavaScript code to execute
     * @param sourceName the name for this chunk of code (will be displayed in error messages)
     * @param startLine the line at which the script source starts
     * @return a ScriptResult which will contain both the current page (which may be different than
     * the previous page and a JavaScript result object.
     */
    public ScriptResult executeJavaScript(String sourceCode, final String sourceName, final int startLine) {
        if (!getWebClient().isJavaScriptEnabled()) {
            return new ScriptResult(Undefined.instance);
        }

        if (StringUtils.startsWithIgnoreCase(sourceCode, JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
            sourceCode = sourceCode.substring(JavaScriptURLConnection.JAVASCRIPT_PREFIX.length()).trim();
            if (sourceCode.startsWith("return ")) {
                sourceCode = sourceCode.substring("return ".length());
            }
        }

        final Object result = getWebClient().getJavaScriptEngine().execute(this, sourceCode, sourceName, startLine);
        return new ScriptResult(result);
    }

    /** Various possible external JavaScript file loading results. */
    enum JavaScriptLoadResult {
        /** The load was aborted and nothing was done. */
        NOOP,
        /** The load was aborted and nothing was done. */
        NO_CONTENT,
        /** The external JavaScript file was downloaded and compiled successfully. */
        SUCCESS,
        /** The external JavaScript file was not downloaded successfully. */
        DOWNLOAD_ERROR,
        /** The external JavaScript file was downloaded but was not compiled successfully. */
        COMPILATION_ERROR
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param srcAttribute the source attribute from the script tag
     * @param scriptCharset the charset from the script tag
     * @return the result of loading the specified external JavaScript file
     * @throws FailingHttpStatusCodeException if the request's status code indicates a request
     *         failure and the {@link WebClient} was configured to throw exceptions on failing
     *         HTTP status codes
     */
    JavaScriptLoadResult loadExternalJavaScriptFile(final String srcAttribute, final Charset scriptCharset)
        throws FailingHttpStatusCodeException {

        final WebClient client = getWebClient();
        if (StringUtils.isBlank(srcAttribute) || !client.isJavaScriptEnabled()) {
            return JavaScriptLoadResult.NOOP;
        }

        final URL scriptURL;
        try {
            scriptURL = getFullyQualifiedUrl(srcAttribute);
            final String protocol = scriptURL.getProtocol();
            if ("javascript".equals(protocol)) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Ignoring script src [" + srcAttribute + "]");
                }
                return JavaScriptLoadResult.NOOP;
            }
            if (!"http".equals(protocol) && !"https".equals(protocol)
                    && !"data".equals(protocol) && !"file".equals(protocol)) {
                client.getJavaScriptErrorListener().malformedScriptURL(this, srcAttribute,
                        new MalformedURLException("unknown protocol: '" + protocol + "'"));
                return JavaScriptLoadResult.NOOP;
            }
        }
        catch (final MalformedURLException e) {
            client.getJavaScriptErrorListener().malformedScriptURL(this, srcAttribute, e);
            return JavaScriptLoadResult.NOOP;
        }

        final Object script;
        try {
            script = loadJavaScriptFromUrl(scriptURL, scriptCharset);
        }
        catch (final IOException e) {
            client.getJavaScriptErrorListener().loadScriptError(this, scriptURL, e);
            return JavaScriptLoadResult.DOWNLOAD_ERROR;
        }
        catch (final FailingHttpStatusCodeException e) {
            if (e.getStatusCode() == HttpClientConverter.NO_CONTENT) {
                return JavaScriptLoadResult.NO_CONTENT;
            }
            client.getJavaScriptErrorListener().loadScriptError(this, scriptURL, e);
            throw e;
        }

        if (script == null) {
            return JavaScriptLoadResult.COMPILATION_ERROR;
        }

        @SuppressWarnings("unchecked")
        final AbstractJavaScriptEngine<Object> engine = (AbstractJavaScriptEngine<Object>) client.getJavaScriptEngine();
        engine.execute(this, script);
        return JavaScriptLoadResult.SUCCESS;
    }

    /**
     * Loads JavaScript from the specified URL. This method may return {@code null} if
     * there is a problem loading the code from the specified URL.
     *
     * @param url the URL of the script
     * @param scriptCharset the charset from the script tag
     * @return the content of the file, or {@code null} if we ran into a compile error
     * @throws IOException if there is a problem downloading the JavaScript file
     * @throws FailingHttpStatusCodeException if the request's status code indicates a request
     *         failure and the {@link WebClient} was configured to throw exceptions on failing
     *         HTTP status codes
     */
    private Object loadJavaScriptFromUrl(final URL url, final Charset scriptCharset) throws IOException,
        FailingHttpStatusCodeException {

        final WebRequest referringRequest = getWebResponse().getWebRequest();

        final WebClient client = getWebClient();
        final WebRequest request = new WebRequest(url);
        // copy all headers from the referring request
        request.setAdditionalHeaders(new HashMap<>(referringRequest.getAdditionalHeaders()));

        // at least overwrite this headers
        final BrowserVersion browserVersion = client.getBrowserVersion();
        request.setAdditionalHeader(HttpHeader.ACCEPT, client.getBrowserVersion().getScriptAcceptHeader());
        if (browserVersion.hasFeature(HTTP_HEADER_SEC_FETCH)) {
            request.setAdditionalHeader(HttpHeader.SEC_FETCH_SITE, "same-origin");
            request.setAdditionalHeader(HttpHeader.SEC_FETCH_MODE, "no-cors");
            request.setAdditionalHeader(HttpHeader.SEC_FETCH_DEST, "script");
        }

        request.setRefererlHeader(referringRequest.getUrl());
        request.setCharset(scriptCharset);

        // our cache is a bit strange;
        // loadWebResponse check the cache for the web response
        // AND also fixes the request url for the following cache lookups
        final WebResponse response = client.loadWebResponse(request);

        // now we can look into the cache with the fixed request for
        // a cached script
        final Cache cache = client.getCache();
        final Object cachedScript = cache.getCachedObject(request);
        if (cachedScript instanceof Script) {
            return cachedScript;
        }

        client.printContentIfNecessary(response);
        client.throwFailingHttpStatusCodeExceptionIfNecessary(response);

        final int statusCode = response.getStatusCode();
        if (statusCode == HttpClientConverter.NO_CONTENT) {
            throw new FailingHttpStatusCodeException(response);
        }

        if (!response.isSuccess()) {
            throw new IOException("Unable to download JavaScript from '" + url + "' (status " + statusCode + ").");
        }

        final String contentType = response.getContentType();
        if (contentType != null) {
            if (MimeType.isObsoleteJavascriptMimeType(contentType)) {
                getWebClient().getIncorrectnessListener().notify(
                        "Obsolete content type encountered: '" + contentType + "' "
                                + "for remotely loaded JavaScript element at '" + url + "'.", this);
            }
            else if (!MimeType.isJavascriptMimeType(contentType)) {
                getWebClient().getIncorrectnessListener().notify(
                        "Expect content type of '" + MimeType.TEXT_JAVASCRIPT + "' "
                                + "for remotely loaded JavaScript element at '" + url + "', "
                                + "but got '" + contentType + "'.", this);
            }
        }

        Charset scriptEncoding = Charset.forName("windows-1252");
        final boolean ignoreBom;
        final Charset contentCharset = EncodingSniffer.sniffEncodingFromHttpHeaders(response.getResponseHeaders());
        if (contentCharset == null) {
            // use info from script tag or fall back to utf-8
            if (scriptCharset != null && ISO_8859_1 != scriptCharset) {
                ignoreBom = true;
                scriptEncoding = scriptCharset;
            }
            else {
                ignoreBom = ISO_8859_1 != scriptCharset;
            }
        }
        else if (ISO_8859_1 == contentCharset) {
            ignoreBom = true;
        }
        else {
            ignoreBom = true;
            scriptEncoding = contentCharset;
        }

        final String scriptCode = response.getContentAsString(scriptEncoding,
                                ignoreBom
                                && getWebClient().getBrowserVersion().hasFeature(JS_IGNORES_UTF8_BOM_SOMETIMES));
        if (null != scriptCode) {
            final AbstractJavaScriptEngine<?> javaScriptEngine = client.getJavaScriptEngine();
            final Object script = javaScriptEngine.compile(this, scriptCode, url.toExternalForm(), 1);
            if (script != null && cache.cacheIfPossible(request, response, script)) {
                // no cleanup if the response is stored inside the cache
                return script;
            }

            response.cleanUp();
            return script;
        }

        response.cleanUp();
        return null;
    }

    /**
     * Returns the title of this page or an empty string if the title wasn't specified.
     *
     * @return the title of this page or an empty string if the title wasn't specified
     */
    public String getTitleText() {
        final HtmlTitle titleElement = getTitleElement();
        if (titleElement != null) {
            return titleElement.asNormalizedText();
        }
        return "";
    }

    /**
     * Sets the text for the title of this page. If there is not a title element
     * on this page, then one has to be generated.
     * @param message the new text
     */
    public void setTitleText(final String message) {
        HtmlTitle titleElement = getTitleElement();
        if (titleElement == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No title element, creating one");
            }
            final HtmlHead head = (HtmlHead) getFirstChildElement(getDocumentElement(), HtmlHead.class);
            if (head == null) {
                // perhaps should we create head too?
                throw new IllegalStateException("Headelement was not defined for this page");
            }
            final Map<String, DomAttr> emptyMap = Collections.emptyMap();
            titleElement = new HtmlTitle(HtmlTitle.TAG_NAME, this, emptyMap);
            if (head.getFirstChild() != null) {
                head.getFirstChild().insertBefore(titleElement);
            }
            else {
                head.appendChild(titleElement);
            }
        }

        titleElement.setNodeValue(message);
    }

    /**
     * Gets the first child of startElement that is an instance of the given class.
     * @param startElement the parent element
     * @param clazz the class to search for
     * @return {@code null} if no child found
     */
    private static DomElement getFirstChildElement(final DomElement startElement, final Class<?> clazz) {
        if (startElement == null) {
            return null;
        }
        for (final DomElement element : startElement.getChildElements()) {
            if (clazz.isInstance(element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Gets the first child of startElement or it's children that is an instance of the given class.
     * @param startElement the parent element
     * @param clazz the class to search for
     * @return {@code null} if no child found
     */
    private DomElement getFirstChildElementRecursive(final DomElement startElement, final Class<?> clazz) {
        if (startElement == null) {
            return null;
        }
        for (final DomElement element : startElement.getChildElements()) {
            if (clazz.isInstance(element)) {
                return element;
            }
            final DomElement childFound = getFirstChildElementRecursive(element, clazz);
            if (childFound != null) {
                return childFound;
            }
        }

        return null;
    }

    /**
     * Gets the title element for this page. Returns null if one is not found.
     *
     * @return the title element for this page or null if this is not one
     */
    private HtmlTitle getTitleElement() {
        return (HtmlTitle) getFirstChildElementRecursive(getDocumentElement(), HtmlTitle.class);
    }

    /**
     * Looks for and executes any appropriate event handlers. Looks for body and frame tags.
     * @param eventType either {@link Event#TYPE_LOAD}, {@link Event#TYPE_UNLOAD}, or {@link Event#TYPE_BEFORE_UNLOAD}
     * @return {@code true} if user accepted <code>onbeforeunload</code> (not relevant to other events)
     */
    private boolean executeEventHandlersIfNeeded(final String eventType) {
        // If JavaScript isn't enabled, there's nothing for us to do.
        if (!getWebClient().isJavaScriptEnabled()) {
            return true;
        }

        // Execute the specified event on the document element.
        final WebWindow window = getEnclosingWindow();
        if (window.getScriptableObject() instanceof Window) {
            final Event event;
            if (eventType.equals(Event.TYPE_BEFORE_UNLOAD)) {
                event = new BeforeUnloadEvent(this, eventType);
            }
            else {
                event = new Event(this, eventType);
            }

            // This is the same as DomElement.fireEvent() and was copied
            // here so it could be used with HtmlPage.
            if (LOG.isDebugEnabled()) {
                LOG.debug("Firing " + event);
            }

            final EventTarget jsNode;
            if (Event.TYPE_DOM_DOCUMENT_LOADED.equals(eventType)) {
                jsNode = getScriptableObject();
            }
            else if (Event.TYPE_READY_STATE_CHANGE.equals(eventType)) {
                jsNode = getDocumentElement().getScriptableObject();
            }
            else {
                // The load/beforeunload/unload events target Document but paths Window only (tested in Chrome/FF)
                jsNode = window.getScriptableObject();
            }

            final HtmlUnitContextFactory cf = ((JavaScriptEngine) getWebClient().getJavaScriptEngine())
                                                    .getContextFactory();
            cf.callSecured(cx -> jsNode.fireEvent(event), this);

            if (!isOnbeforeunloadAccepted(this, event)) {
                return false;
            }
        }

        // If this page was loaded in a frame, execute the version of the event specified on the frame tag.
        if (window instanceof FrameWindow) {
            final FrameWindow fw = (FrameWindow) window;
            final BaseFrameElement frame = fw.getFrameElement();

            // if part of a document fragment, then the load event is not triggered
            if (Event.TYPE_LOAD.equals(eventType) && frame.getParentNode() instanceof DomDocumentFragment) {
                return true;
            }

            if (frame.hasEventHandlers("on" + eventType)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Executing on" + eventType + " handler for " + frame);
                }
                if (window.getScriptableObject() instanceof Window) {
                    final Event event;
                    if (Event.TYPE_BEFORE_UNLOAD.equals(eventType)) {
                        event = new BeforeUnloadEvent(frame, eventType);
                    }
                    else {
                        // ff does not trigger the onload event in this case
                        if (PageDenied.BY_CONTENT_SECURIRY_POLICY == fw.getPageDenied()
                                && hasFeature(JS_EVENT_LOAD_SUPPRESSED_BY_CONTENT_SECURIRY_POLICY)) {
                            return true;
                        }

                        event = new Event(frame, eventType);
                    }
                    // This fires the "load" event for the <frame> element which, like all non-window
                    // load events, propagates up to Document but not Window.  The "load" event for
                    // <frameset> on the other hand, like that of <body>, is handled above where it is
                    // fired against Document and directed to Window.
                    frame.fireEvent(event);

                    if (!isOnbeforeunloadAccepted((HtmlPage) frame.getPage(), event)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @return true if the OnbeforeunloadHandler has accepted to change the page
     */
    public boolean isOnbeforeunloadAccepted() {
        return executeEventHandlersIfNeeded(Event.TYPE_BEFORE_UNLOAD);
    }

    private boolean isOnbeforeunloadAccepted(final HtmlPage page, final Event event) {
        if (event instanceof BeforeUnloadEvent) {
            final BeforeUnloadEvent beforeUnloadEvent = (BeforeUnloadEvent) event;
            if (beforeUnloadEvent.isBeforeUnloadMessageSet()) {
                final OnbeforeunloadHandler handler = getWebClient().getOnbeforeunloadHandler();
                if (handler == null) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("document.onbeforeunload() returned a string in event.returnValue,"
                                + " but no onbeforeunload handler installed.");
                    }
                }
                else {
                    final String message = JavaScriptEngine.toString(beforeUnloadEvent.getReturnValue());
                    return handler.handleEvent(page, message);
                }
            }
        }
        return true;
    }

    /**
     * If a refresh has been specified either through a meta tag or an HTTP
     * response header, then perform that refresh.
     * @throws IOException if an IO problem occurs
     */
    private void executeRefreshIfNeeded() throws IOException {
        // If this page is not in a frame then a refresh has already happened,
        // most likely through the JavaScript onload handler, so we don't do a
        // second refresh.
        final WebWindow window = getEnclosingWindow();
        if (window == null) {
            return;
        }

        final String refreshString = getRefreshStringOrNull();
        if (refreshString == null || refreshString.isEmpty()) {
            return;
        }

        final double time;
        final URL url;

        int index = StringUtils.indexOfAnyBut(refreshString, "0123456789");
        final boolean timeOnly = index == -1;

        if (timeOnly) {
            // Format: <meta http-equiv='refresh' content='10'>
            try {
                time = Double.parseDouble(refreshString);
            }
            catch (final NumberFormatException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Malformed refresh string (no ';' but not a number): " + refreshString, e);
                }
                return;
            }
            url = getUrl();
        }
        else {
            // Format: <meta http-equiv='refresh' content='10;url=http://www.blah.com'>
            try {
                time = Double.parseDouble(refreshString.substring(0, index).trim());
            }
            catch (final NumberFormatException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Malformed refresh string (no valid number before ';') " + refreshString, e);
                }
                return;
            }
            index = refreshString.toLowerCase(Locale.ROOT).indexOf("url=", index);
            if (index == -1) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Malformed refresh string (found ';' but no 'url='): " + refreshString);
                }
                return;
            }
            final StringBuilder builder = new StringBuilder(refreshString.substring(index + 4));
            if (StringUtils.isBlank(builder.toString())) {
                //content='10; URL=' is treated as content='10'
                url = getUrl();
            }
            else {
                if (builder.charAt(0) == '"' || builder.charAt(0) == 0x27) {
                    builder.deleteCharAt(0);
                }
                if (builder.charAt(builder.length() - 1) == '"' || builder.charAt(builder.length() - 1) == 0x27) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                final String urlString = builder.toString();
                try {
                    url = getFullyQualifiedUrl(urlString);
                }
                catch (final MalformedURLException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Malformed URL in refresh string: " + refreshString, e);
                    }
                    throw e;
                }
            }
        }

        final int timeRounded = (int) time;
        checkRecursion();
        getWebClient().getRefreshHandler().handleRefresh(this, url, timeRounded);
    }

    private void checkRecursion() {
        final StackTraceElement[] elements = new Exception().getStackTrace();
        if (elements.length > 500) {
            for (int i = 0; i < 500; i++) {
                if (!elements[i].getClassName().startsWith("org.htmlunit.")) {
                    return;
                }
            }
            final WebResponse webResponse = getWebResponse();
            throw new FailingHttpStatusCodeException("Too much redirect for "
                    + webResponse.getWebRequest().getUrl(), webResponse);
        }
    }

    /**
     * Returns an auto-refresh string if specified. This will look in both the meta
     * tags and inside the HTTP response headers.
     * @return the auto-refresh string
     */
    private String getRefreshStringOrNull() {
        final List<HtmlMeta> metaTags = getMetaTags("refresh");
        if (!metaTags.isEmpty()) {
            return metaTags.get(0).getContentAttribute().trim();
        }
        return getWebResponse().getResponseHeaderValue("Refresh");
    }

    /**
     * Executes any deferred scripts, if necessary.
     */
    private void executeDeferredScriptsIfNeeded() {
        if (!getWebClient().isJavaScriptEnabled()) {
            return;
        }
        final DomElement doc = getDocumentElement();
        final List<HtmlElement> elements = new ArrayList<>(doc.getElementsByTagName("script"));
        for (final HtmlElement e : elements) {
            if (e instanceof HtmlScript) {
                final HtmlScript script = (HtmlScript) e;
                if (script.isDeferred() && ATTRIBUTE_NOT_DEFINED != script.getSrcAttribute()) {
                    ScriptElementSupport.executeScriptIfNeeded(script, true, true);
                }
            }
        }
    }

    /**
     * Deregister frames that are no longer in use.
     */
    public void deregisterFramesIfNeeded() {
        for (final WebWindow window : getFrames()) {
            getWebClient().deregisterWebWindow(window);
            final Page page = window.getEnclosedPage();
            if (page != null && page.isHtmlPage()) {
                // seems quite silly, but for instance if the src attribute of an iframe is not
                // set, the error only occurs when leaving the page
                ((HtmlPage) page).deregisterFramesIfNeeded();
            }
        }
    }

    /**
     * Returns a list containing all the frames (from frame and iframe tags) in this page.
     * @return a list of {@link FrameWindow}
     */
    public List<FrameWindow> getFrames() {
        final List<FrameWindow> list = new ArrayList<>(frameElements_.size());
        for (final BaseFrameElement frameElement : frameElements_) {
            list.add(frameElement.getEnclosedWindow());
        }
        return list;
    }

    /**
     * Returns the first frame contained in this page with the specified name.
     * @param name the name to search for
     * @return the first frame found
     * @exception ElementNotFoundException If no frame exist in this page with the specified name.
     */
    public FrameWindow getFrameByName(final String name) throws ElementNotFoundException {
        for (final FrameWindow frame : getFrames()) {
            if (frame.getName().equals(name)) {
                return frame;
            }
        }

        throw new ElementNotFoundException("frame or iframe", DomElement.NAME_ATTRIBUTE, name);
    }

    /**
     * Simulate pressing an access key. This may change the focus, may click buttons and may invoke
     * JavaScript.
     *
     * @param accessKey the key that will be pressed
     * @return the element that has the focus after pressing this access key or null if no element
     * has the focus.
     * @throws IOException if an IO error occurs during the processing of this access key (this
     *         would only happen if the access key triggered a button which in turn caused a page load)
     */
    public DomElement pressAccessKey(final char accessKey) throws IOException {
        final HtmlElement element = getHtmlElementByAccessKey(accessKey);
        if (element != null) {
            element.focus();
            if (element instanceof HtmlAnchor
                    || element instanceof HtmlArea
                    || element instanceof HtmlButton
                    || element instanceof HtmlInput
                    || element instanceof HtmlLabel
                    || element instanceof HtmlLegend
                    || element instanceof HtmlTextArea) {
                final Page newPage = element.click();

                if (newPage != this && getFocusedElement() == element) {
                    // The page was reloaded therefore no element on this page will have the focus.
                    getFocusedElement().blur();
                }
            }
        }

        return getFocusedElement();
    }

    /**
     * Move the focus to the next element in the tab order. To determine the specified tab
     * order, refer to {@link HtmlPage#getTabbableElements()}
     *
     * @return the element that has focus after calling this method
     */
    public HtmlElement tabToNextElement() {
        final List<HtmlElement> elements = getTabbableElements();
        if (elements.isEmpty()) {
            setFocusedElement(null);
            return null;
        }

        final HtmlElement elementToGiveFocus;
        final DomElement elementWithFocus = getFocusedElement();
        if (elementWithFocus == null) {
            elementToGiveFocus = elements.get(0);
        }
        else {
            final int index = elements.indexOf(elementWithFocus);
            if (index == -1) {
                // The element with focus isn't on this page
                elementToGiveFocus = elements.get(0);
            }
            else {
                if (index == elements.size() - 1) {
                    elementToGiveFocus = elements.get(0);
                }
                else {
                    elementToGiveFocus = elements.get(index + 1);
                }
            }
        }

        setFocusedElement(elementToGiveFocus);
        return elementToGiveFocus;
    }

    /**
     * Move the focus to the previous element in the tab order. To determine the specified tab
     * order, refer to {@link HtmlPage#getTabbableElements()}
     *
     * @return the element that has focus after calling this method
     */
    public HtmlElement tabToPreviousElement() {
        final List<HtmlElement> elements = getTabbableElements();
        if (elements.isEmpty()) {
            setFocusedElement(null);
            return null;
        }

        final HtmlElement elementToGiveFocus;
        final DomElement elementWithFocus = getFocusedElement();
        if (elementWithFocus == null) {
            elementToGiveFocus = elements.get(elements.size() - 1);
        }
        else {
            final int index = elements.indexOf(elementWithFocus);
            if (index == -1) {
                // The element with focus isn't on this page
                elementToGiveFocus = elements.get(elements.size() - 1);
            }
            else {
                if (index == 0) {
                    elementToGiveFocus = elements.get(elements.size() - 1);
                }
                else {
                    elementToGiveFocus = elements.get(index - 1);
                }
            }
        }

        setFocusedElement(elementToGiveFocus);
        return elementToGiveFocus;
    }

    /**
     * Returns the HTML element with the specified ID. If more than one element
     * has this ID (not allowed by the HTML spec), then this method returns the
     * first one.
     *
     * @param elementId the ID value to search for
     * @param <E> the element type
     * @return the HTML element with the specified ID
     * @throws ElementNotFoundException if no element was found matching the specified ID
     */
    @SuppressWarnings("unchecked")
    public <E extends HtmlElement> E getHtmlElementById(final String elementId) throws ElementNotFoundException {
        final DomElement element = getElementById(elementId);
        if (element == null) {
            throw new ElementNotFoundException("*", DomElement.ID_ATTRIBUTE, elementId);
        }
        return (E) element;
    }

    /**
     * Returns the elements with the specified ID. If there are no elements
     * with the specified ID, this method returns an empty list. Please note that
     * the lists returned by this method are immutable.
     *
     * @param elementId the ID value to search for
     * @return the elements with the specified name attribute
     */
    public List<DomElement> getElementsById(final String elementId) {
        final SortedSet<DomElement> elements = idMap_.get(elementId);
        if (elements != null) {
            return new ArrayList<>(elements);
        }
        return Collections.emptyList();
    }

    /**
     * Returns the element with the specified name. If more than one element
     * has this name, then this method returns the first one.
     *
     * @param name the name value to search for
     * @param <E> the element type
     * @return the element with the specified name
     * @throws ElementNotFoundException if no element was found matching the specified name
     */
    @SuppressWarnings("unchecked")
    public <E extends DomElement> E getElementByName(final String name) throws ElementNotFoundException {
        final SortedSet<DomElement> elements = nameMap_.get(name);
        if (elements != null) {
            return (E) elements.first();
        }
        throw new ElementNotFoundException("*", DomElement.NAME_ATTRIBUTE, name);
    }

    /**
     * Returns the elements with the specified name attribute. If there are no elements
     * with the specified name, this method returns an empty list. Please note that
     * the lists returned by this method are immutable.
     *
     * @param name the name value to search for
     * @return the elements with the specified name attribute
     */
    public List<DomElement> getElementsByName(final String name) {
        final SortedSet<DomElement> elements = nameMap_.get(name);
        if (elements != null) {
            return new ArrayList<>(elements);
        }
        return Collections.emptyList();
    }

    /**
     * Returns the elements with the specified string for their name or ID. If there are
     * no elements with the specified name or ID, this method returns an empty list.
     *
     * @param idAndOrName the value to search for
     * @return the elements with the specified string for their name or ID
     */
    public List<DomElement> getElementsByIdAndOrName(final String idAndOrName) {
        final Collection<DomElement> list1 = idMap_.get(idAndOrName);
        final Collection<DomElement> list2 = nameMap_.get(idAndOrName);
        final List<DomElement> list = new ArrayList<>();
        if (list1 != null) {
            list.addAll(list1);
        }
        if (list2 != null) {
            for (final DomElement elt : list2) {
                if (!list.contains(elt)) {
                    list.add(elt);
                }
            }
        }
        return list;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param node the node that has just been added to the document
     */
    void notifyNodeAdded(final DomNode node) {
        if (node instanceof DomElement) {
            addMappedElement((DomElement) node, true);

            if (node instanceof BaseFrameElement) {
                frameElements_.add((BaseFrameElement) node);
            }
            for (final HtmlElement child : node.getHtmlElementDescendants()) {
                if (child instanceof BaseFrameElement) {
                    frameElements_.add((BaseFrameElement) child);
                }
            }

            if ("base".equals(node.getNodeName())) {
                calculateBase();
            }
        }
        node.onAddedToPage();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param node the node that has just been removed from the tree
     */
    void notifyNodeRemoved(final DomNode node) {
        if (node instanceof HtmlElement) {
            removeMappedElement((HtmlElement) node, true, true);

            if (node instanceof BaseFrameElement) {
                frameElements_.remove(node);
            }
            for (final HtmlElement child : node.getHtmlElementDescendants()) {
                if (child instanceof BaseFrameElement) {
                    frameElements_.remove(child);
                }
            }

            if ("base".equals(node.getNodeName())) {
                calculateBase();
            }
        }
    }

    /**
     * Adds an element to the ID and name maps, if necessary.
     * @param element the element to be added to the ID and name maps
     */
    void addMappedElement(final DomElement element) {
        addMappedElement(element, false);
    }

    /**
     * Adds an element to the ID and name maps, if necessary.
     * @param element the element to be added to the ID and name maps
     * @param recurse indicates if children must be added too
     */
    void addMappedElement(final DomElement element, final boolean recurse) {
        if (isAncestorOf(element)) {
            addElement(idMap_, element, DomElement.ID_ATTRIBUTE, recurse);
            addElement(nameMap_, element, DomElement.NAME_ATTRIBUTE, recurse);
        }
    }

    private void addElement(final Map<String, SortedSet<DomElement>> map, final DomElement element,
            final String attribute, final boolean recurse) {
        final String value = element.getAttribute(attribute);

        if (ATTRIBUTE_NOT_DEFINED != value) {
            SortedSet<DomElement> elements = map.get(value);
            if (elements == null) {
                elements = new TreeSet<>(documentPositionComparator);
                elements.add(element);
                map.put(value, elements);
            }
            else {
                elements.add(element);
            }
        }
        if (recurse) {
            for (final DomElement child : element.getChildElements()) {
                addElement(map, child, attribute, true);
            }
        }
    }

    /**
     * Removes an element from the ID and name maps, if necessary.
     * @param element the element to be removed from the ID and name maps
     */
    void removeMappedElement(final HtmlElement element) {
        removeMappedElement(element, false, false);
    }

    /**
     * Removes an element and optionally its children from the ID and name maps, if necessary.
     * @param element the element to be removed from the ID and name maps
     * @param recurse indicates if children must be removed too
     * @param descendant indicates of the element was descendant of this HtmlPage, but now its parent might be null
     */
    void removeMappedElement(final DomElement element, final boolean recurse, final boolean descendant) {
        if (descendant || isAncestorOf(element)) {
            removeElement(idMap_, element, DomElement.ID_ATTRIBUTE, recurse);
            removeElement(nameMap_, element, DomElement.NAME_ATTRIBUTE, recurse);
        }
    }

    private void removeElement(final Map<String, SortedSet<DomElement>> map, final DomElement element,
            final String attribute, final boolean recurse) {
        final String value = element.getAttribute(attribute);

        if (ATTRIBUTE_NOT_DEFINED != value) {
            final SortedSet<DomElement> elements = map.remove(value);
            if (elements != null && (elements.size() != 1 || !elements.contains(element))) {
                elements.remove(element);
                map.put(value, elements);
            }
        }
        if (recurse) {
            for (final DomElement child : element.getChildElements()) {
                removeElement(map, child, attribute, true);
            }
        }
    }

    /**
     * Indicates if the attribute name indicates that the owning element is mapped.
     * @param document the owning document
     * @param attributeName the name of the attribute to consider
     * @return {@code true} if the owning element should be mapped in its owning page
     */
    static boolean isMappedElement(final Document document, final String attributeName) {
        return document instanceof HtmlPage
            && (DomElement.NAME_ATTRIBUTE.equals(attributeName) || DomElement.ID_ATTRIBUTE.equals(attributeName));
    }

    private void calculateBase() {
        final List<HtmlElement> baseElements = getDocumentElement().getElementsByTagName("base");

        base_ = null;
        for (final HtmlElement baseElement : baseElements) {
            if (baseElement instanceof HtmlBase) {
                if (base_ != null) {
                    notifyIncorrectness("Multiple 'base' detected, only the first is used.");
                    break;
                }
                base_ = (HtmlBase) baseElement;
            }
        }
    }

    /**
     * Loads the content of the contained frames. This is done after the page is completely loaded, to allow script
     * contained in the frames to reference elements from the page located after the closing &lt;/frame&gt; tag.
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link WebClientOptions#setThrowExceptionOnFailingStatusCode(boolean)} is set to {@code true}
     */
    void loadFrames() throws FailingHttpStatusCodeException {
        for (final FrameWindow w : getFrames()) {
            final BaseFrameElement frame = w.getFrameElement();
            // test if the frame should really be loaded:
            // if a script has already changed its content, it should be skipped
            // use == and not equals(...) to identify initial content (versus URL set to "about:blank")
            if (frame.getEnclosedWindow() != null
                    && UrlUtils.URL_ABOUT_BLANK == frame.getEnclosedPage().getUrl()
                    && !frame.isContentLoaded()) {
                frame.loadInnerPage();
            }
        }
    }

    /**
     * Gives a basic representation for debugging purposes.
     * @return a basic representation
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder()
            .append("HtmlPage(")
            .append(getUrl())
            .append(")@")
            .append(hashCode());
        return builder.toString();
    }

    /**
     * Gets the meta tag for a given {@code http-equiv} value.
     * @param httpEquiv the {@code http-equiv} value
     * @return a list of {@link HtmlMeta}
     */
    protected List<HtmlMeta> getMetaTags(final String httpEquiv) {
        if (getDocumentElement() == null) {
            return Collections.emptyList(); // weird case, for instance if document.documentElement has been removed
        }
        final String nameLC = httpEquiv.toLowerCase(Locale.ROOT);
        final List<HtmlMeta> tags = getDocumentElement().getElementsByTagNameImpl("meta");
        final List<HtmlMeta> foundTags = new ArrayList<>();
        for (final HtmlMeta htmlMeta : tags) {
            if (nameLC.equals(htmlMeta.getHttpEquivAttribute().toLowerCase(Locale.ROOT))) {
                foundTags.add(htmlMeta);
            }
        }
        return foundTags;
    }

    /**
     * Creates a clone of this instance, and clears cached state to be not shared with the original.
     *
     * @return a clone of this instance
     */
    @Override
    protected HtmlPage clone() {
        final HtmlPage result = (HtmlPage) super.clone();
        result.elementWithFocus_ = null;

        result.idMap_ = new ConcurrentHashMap<>();
        result.nameMap_ = new ConcurrentHashMap<>();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPage cloneNode(final boolean deep) {
        // we need the ScriptObject clone before cloning the kids.
        final HtmlPage result = (HtmlPage) super.cloneNode(false);
        if (getWebClient().isJavaScriptEnabled()) {
            final HtmlUnitScriptable jsObjClone = getScriptableObject().clone();
            jsObjClone.setDomNode(result);
        }

        // if deep, clone the kids too, and re initialize parts of the clone
        if (deep) {
            // this was previously synchronized but that makes not sense, why
            // lock the source against a copy only one has a reference too,
            // because result is a local reference
            result.attributeListeners_ = null;

            result.selectionRanges_ = new ArrayList<>(3);
            // the original one is synchronized so we should do that here too, shouldn't we?
            result.afterLoadActions_ = Collections.synchronizedList(new ArrayList<>());
            result.frameElements_ = new TreeSet<>(documentPositionComparator);
            for (DomNode child = getFirstChild(); child != null; child = child.getNextSibling()) {
                result.appendChild(child.cloneNode(true));
            }
        }
        return result;
    }

    /**
     * Adds an HtmlAttributeChangeListener to the listener list.
     * The listener is registered for all attributes of all HtmlElements contained in this page.
     *
     * @param listener the attribute change listener to be added
     * @see #removeHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    public void addHtmlAttributeChangeListener(final HtmlAttributeChangeListener listener) {
        WebAssert.notNull("listener", listener);
        synchronized (lock_) {
            if (attributeListeners_ == null) {
                attributeListeners_ = new LinkedHashSet<>();
            }
            attributeListeners_.add(listener);
        }
    }

    /**
     * Removes an HtmlAttributeChangeListener from the listener list.
     * This method should be used to remove HtmlAttributeChangeListener that were registered
     * for all attributes of all HtmlElements contained in this page.
     *
     * @param listener the attribute change listener to be removed
     * @see #addHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    public void removeHtmlAttributeChangeListener(final HtmlAttributeChangeListener listener) {
        WebAssert.notNull("listener", listener);
        synchronized (lock_) {
            if (attributeListeners_ != null) {
                attributeListeners_.remove(listener);
            }
        }
    }

    /**
     * Notifies all registered listeners for the given event to add an attribute.
     * @param event the event to fire
     */
    void fireHtmlAttributeAdded(final HtmlAttributeChangeEvent event) {
        final List<HtmlAttributeChangeListener> listeners = safeGetAttributeListeners();
        if (listeners != null) {
            for (final HtmlAttributeChangeListener listener : listeners) {
                listener.attributeAdded(event);
            }
        }
    }

    /**
     * Notifies all registered listeners for the given event to replace an attribute.
     * @param event the event to fire
     */
    void fireHtmlAttributeReplaced(final HtmlAttributeChangeEvent event) {
        final List<HtmlAttributeChangeListener> listeners = safeGetAttributeListeners();
        if (listeners != null) {
            for (final HtmlAttributeChangeListener listener : listeners) {
                listener.attributeReplaced(event);
            }
        }
    }

    /**
     * Notifies all registered listeners for the given event to remove an attribute.
     * @param event the event to fire
     */
    void fireHtmlAttributeRemoved(final HtmlAttributeChangeEvent event) {
        final List<HtmlAttributeChangeListener> listeners = safeGetAttributeListeners();
        if (listeners != null) {
            for (final HtmlAttributeChangeListener listener : listeners) {
                listener.attributeRemoved(event);
            }
        }
    }

    private List<HtmlAttributeChangeListener> safeGetAttributeListeners() {
        synchronized (lock_) {
            if (attributeListeners_ != null) {
                return new ArrayList<>(attributeListeners_);
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkChildHierarchy(final org.w3c.dom.Node newChild) throws DOMException {
        if (newChild instanceof Element) {
            if (getDocumentElement() != null) {
                throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                    "The Document may only have a single child Element.");
            }
        }
        else if (newChild instanceof DocumentType) {
            if (getDoctype() != null) {
                throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                    "The Document may only have a single child DocumentType.");
            }
        }
        else if (!(newChild instanceof Comment || newChild instanceof ProcessingInstruction)) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "The Document may not have a child of this type: " + newChild.getNodeType());
        }
        super.checkChildHierarchy(newChild);
    }

    /**
     * Returns {@code true} if an HTML parser is operating on this page, adding content to it.
     * @return {@code true} if an HTML parser is operating on this page, adding content to it
     */
    public boolean isBeingParsed() {
        return parserCount_ > 0;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Called by the HTML parser to let the page know that it has started parsing some content for this page.
     */
    public void registerParsingStart() {
        parserCount_++;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Called by the HTML parser to let the page know that it has finished parsing some content for this page.
     */
    public void registerParsingEnd() {
        parserCount_--;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns {@code true} if an HTML parser is parsing a non-inline HTML snippet to add content
     * to this page. Non-inline content is content that is parsed for the page, but not in the
     * same stream as the page itself -- basically anything other than <code>document.write()</code>
     * or <code>document.writeln()</code>: <code>innerHTML</code>, <code>outerHTML</code>,
     * <code>document.createElement()</code>, etc.
     *
     * @return {@code true} if an HTML parser is parsing a non-inline HTML snippet to add content
     *         to this page
     */
    public boolean isParsingHtmlSnippet() {
        return snippetParserCount_ > 0;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Called by the HTML parser to let the page know that it has started parsing a non-inline HTML snippet.
     */
    public void registerSnippetParsingStart() {
        snippetParserCount_++;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Called by the HTML parser to let the page know that it has finished parsing a non-inline HTML snippet.
     */
    public void registerSnippetParsingEnd() {
        snippetParserCount_--;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns {@code true} if an HTML parser is parsing an inline HTML snippet to add content
     * to this page. Inline content is content inserted into the parser stream dynamically
     * while the page is being parsed (i.e. <code>document.write()</code> or <code>document.writeln()</code>).
     *
     * @return {@code true} if an HTML parser is parsing an inline HTML snippet to add content
     *         to this page
     */
    public boolean isParsingInlineHtmlSnippet() {
        return inlineSnippetParserCount_ > 0;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Called by the HTML parser to let the page know that it has started parsing an inline HTML snippet.
     */
    public void registerInlineSnippetParsingStart() {
        inlineSnippetParserCount_++;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Called by the HTML parser to let the page know that it has finished parsing an inline HTML snippet.
     */
    public void registerInlineSnippetParsingEnd() {
        inlineSnippetParserCount_--;
    }

    /**
     * Refreshes the page by sending the same parameters as previously sent to get this page.
     * @return the newly loaded page.
     * @throws IOException if an IO problem occurs
     */
    public Page refresh() throws IOException {
        return getWebClient().getPage(getWebResponse().getWebRequest());
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * <p>
     * Parses the given string as would it belong to the content being parsed
     * at the current parsing position
     * </p>
     * @param string the HTML code to write in place
     */
    public void writeInParsedStream(final String string) {
        getDOMBuilder().pushInputString(string);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Sets the builder to allow page to send content from document.write(ln) calls.
     * @param htmlUnitDOMBuilder the builder
     */
    public void setDOMBuilder(final HTMLParserDOMBuilder htmlUnitDOMBuilder) {
        domBuilder_ = htmlUnitDOMBuilder;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns the current builder.
     * @return the current builder
     */
    public HTMLParserDOMBuilder getDOMBuilder() {
        return domBuilder_;
    }

    /**
     * <p>Returns all namespaces defined in the root element of this page.</p>
     * <p>The default namespace has a key of an empty string.</p>
     * @return all namespaces defined in the root element of this page
     */
    public Map<String, String> getNamespaces() {
        final org.w3c.dom.NamedNodeMap attributes = getDocumentElement().getAttributes();
        final Map<String, String> namespaces = new HashMap<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            final Attr attr = (Attr) attributes.item(i);
            String name = attr.getName();
            if (name.startsWith("xmlns")) {
                int startPos = 5;
                if (name.length() > 5 && name.charAt(5) == ':') {
                    startPos = 6;
                }
                name = name.substring(startPos);
                namespaces.put(name, attr.getValue());
            }
        }
        return namespaces;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDocumentType(final DocumentType type) {
        super.setDocumentType(type);
    }

    /**
     * Saves the current page, with all images, to the specified location.
     * The default behavior removes all script elements.
     *
     * @param file file to write this page into
     * @throws IOException If an error occurs
     */
    public void save(final File file) throws IOException {
        new XmlSerializer().save(this, file);
    }

    /**
     * Returns whether the current page mode is in {@code quirks mode} or in {@code standards mode}.
     * @return true for {@code quirks mode}, false for {@code standards mode}
     */
    public boolean isQuirksMode() {
        return "BackCompat".equals(((HTMLDocument) getScriptableObject()).getCompatMode());
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * {@inheritDoc}
     */
    @Override
    public boolean isAttachedToPage() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHtmlPage() {
        return true;
    }

    /**
     * The base URL used to resolve relative URLs.
     * @return the base URL
     */
    public URL getBaseURL() {
        URL baseUrl;
        if (base_ == null) {
            baseUrl = getUrl();
            final WebWindow window = getEnclosingWindow();
            final boolean frame = window != null && window != window.getTopWindow();
            if (frame) {
                final boolean frameSrcIsNotSet = baseUrl == UrlUtils.URL_ABOUT_BLANK;
                final boolean frameSrcIsJs = "javascript".equals(baseUrl.getProtocol());
                if (frameSrcIsNotSet || frameSrcIsJs) {
                    baseUrl = window.getTopWindow().getEnclosedPage().getWebResponse()
                        .getWebRequest().getUrl();
                }
            }
            else if (baseUrl_ != null) {
                baseUrl = baseUrl_;
            }
        }
        else {
            final String href = base_.getHrefAttribute().trim();
            if (StringUtils.isEmpty(href)) {
                baseUrl = getUrl();
            }
            else {
                final URL url = getUrl();
                try {
                    if (href.startsWith("http://") || href.startsWith("https://")) {
                        baseUrl = new URL(href);
                    }
                    else if (href.startsWith("//")) {
                        baseUrl = new URL(String.format("%s:%s", url.getProtocol(), href));
                    }
                    else if (href.length() > 0 && href.charAt(0) == '/') {
                        final int port = Window.getPort(url);
                        baseUrl = new URL(String.format("%s://%s:%d%s", url.getProtocol(), url.getHost(), port, href));
                    }
                    else if (url.toString().endsWith("/")) {
                        baseUrl = new URL(String.format("%s%s", url, href));
                    }
                    else {
                        baseUrl = new URL(UrlUtils.resolveUrl(url, href));
                    }
                }
                catch (final MalformedURLException e) {
                    notifyIncorrectness("Invalid base url: \"" + href + "\", ignoring it");
                    baseUrl = url;
                }
            }
        }

        return baseUrl;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Adds an {@link AutoCloseable}, which would be closed during the {@link #cleanUp()}.
     * @param autoCloseable the autoclosable
     */
    public void addAutoCloseable(final AutoCloseable autoCloseable) {
        if (autoCloseable == null) {
            return;
        }

        if (autoCloseableList_ == null) {
            autoCloseableList_ = new ArrayList<>();
        }
        autoCloseableList_.add(autoCloseable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(final Event event) {
        if (Event.TYPE_BLUR.equals(event.getType()) || Event.TYPE_FOCUS.equals(event.getType())) {
            return true;
        }
        return super.handles(event);
    }

    /**
     * Sets the {@link ElementFromPointHandler}.
     * @param elementFromPointHandler the handler
     */
    public void setElementFromPointHandler(final ElementFromPointHandler elementFromPointHandler) {
        elementFromPointHandler_ = elementFromPointHandler;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns the element for the specified x coordinate and the specified y coordinate.
     *
     * @param x the x offset, in pixels
     * @param y the y offset, in pixels
     * @return the element for the specified x coordinate and the specified y coordinate
     */
    public HtmlElement getElementFromPoint(final int x, final int y) {
        if (elementFromPointHandler_ == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("ElementFromPointHandler was not specicifed for " + this);
            }
            if (x <= 0 || y <= 0) {
                return null;
            }
            return getBody();
        }
        return elementFromPointHandler_.getElementFromPoint(this, x, y);
    }

    /**
     * Moves the focus to the specified element. This will trigger any relevant JavaScript
     * event handlers.
     *
     * @param newElement the element that will receive the focus, use {@code null} to remove focus from any element
     * @return true if the specified element now has the focus
     * @see #getFocusedElement()
     */
    public boolean setFocusedElement(final DomElement newElement) {
        return setFocusedElement(newElement, false);
    }

    /**
     * Moves the focus to the specified element. This will trigger any relevant JavaScript
     * event handlers.
     *
     * @param newElement the element that will receive the focus, use {@code null} to remove focus from any element
     * @param windowActivated - whether the enclosing window got focus resulting in specified element getting focus
     * @return true if the specified element now has the focus
     * @see #getFocusedElement()
     */
    public boolean setFocusedElement(final DomElement newElement, final boolean windowActivated) {
        if (elementWithFocus_ == newElement && !windowActivated) {
            // nothing to do
            return true;
        }

        final DomElement oldFocusedElement = elementWithFocus_;
        elementWithFocus_ = null;

        if (!windowActivated) {
            if (hasFeature(EVENT_FOCUS_IN_FOCUS_OUT_BLUR)) {
                if (oldFocusedElement != null) {
                    oldFocusedElement.fireEvent(Event.TYPE_FOCUS_OUT);
                }

                if (newElement != null) {
                    newElement.fireEvent(Event.TYPE_FOCUS_IN);
                }
            }

            if (oldFocusedElement != null) {
                oldFocusedElement.removeFocus();
                oldFocusedElement.fireEvent(Event.TYPE_BLUR);

                if (hasFeature(EVENT_FOCUS_FOCUS_IN_BLUR_OUT)) {
                    oldFocusedElement.fireEvent(Event.TYPE_FOCUS_OUT);
                }
            }
        }

        elementWithFocus_ = newElement;

        // use newElement in the code below because element elementWithFocus_
        // might be changed by another thread
        if (newElement instanceof SelectableTextInput
                && hasFeature(PAGE_SELECTION_RANGE_FROM_SELECTABLE_TEXT_INPUT)) {
            final SelectableTextInput sti = (SelectableTextInput) newElement;
            setSelectionRange(new SimpleRange(newElement, sti.getSelectionStart(), newElement, sti.getSelectionEnd()));
        }

        if (newElement != null) {
            newElement.focus();
            newElement.fireEvent(Event.TYPE_FOCUS);

            if (hasFeature(EVENT_FOCUS_FOCUS_IN_BLUR_OUT)) {
                newElement.fireEvent(Event.TYPE_FOCUS_IN);
            }
        }

        // If a page reload happened as a result of the focus change then obviously this
        // element will not have the focus because its page has gone away.
        return this == getEnclosingWindow().getEnclosedPage();
    }

    /**
     * Returns the element with the focus or null if no element has the focus.
     * @return the element with focus or null
     * @see #setFocusedElement(DomElement)
     */
    public DomElement getFocusedElement() {
        return elementWithFocus_;
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * Sets the element with focus.
     * @param elementWithFocus the element with focus
     */
    public void setElementWithFocus(final DomElement elementWithFocus) {
        elementWithFocus_ = elementWithFocus;
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * @return the element with focus or the body
     */
    public HtmlElement getActiveElement() {
        final DomElement activeElement = getFocusedElement();
        if (activeElement instanceof HtmlElement) {
            return (HtmlElement) activeElement;
        }

        final HtmlElement body = getBody();
        if (body != null) {
            return body;
        }
        return null;
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * <p>Returns the page's current selection ranges. Note that some browsers, like IE, only allow
     * a single selection at a time.</p>
     *
     * @return the page's current selection ranges
     */
    public List<SimpleRange> getSelectionRanges() {
        return selectionRanges_;
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * <p>Makes the specified selection range the *only* selection range on this page.</p>
     *
     * @param selectionRange the selection range
     */
    public void setSelectionRange(final SimpleRange selectionRange) {
        selectionRanges_.clear();
        selectionRanges_.add(selectionRange);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Execute a Function in the given context.
     *
     * @param function the JavaScript Function to call
     * @param thisObject the "this" object to be used during invocation
     * @param args the arguments to pass into the call
     * @param htmlElementScope the HTML element for which this script is being executed
     *        This element will be the context during the JavaScript execution. If null,
     *        the context will default to the page.
     * @return a ScriptResult which will contain both the current page (which may be different than
     *        the previous page and a JavaScript result object.
     */
    public ScriptResult executeJavaScriptFunction(final Object function, final Object thisObject,
            final Object[] args, final DomNode htmlElementScope) {
        if (!getWebClient().isJavaScriptEnabled()) {
            return new ScriptResult(null);
        }

        return executeJavaScriptFunction((Function) function, (Scriptable) thisObject, args, htmlElementScope);
    }

    private ScriptResult executeJavaScriptFunction(final Function function, final Scriptable thisObject,
            final Object[] args, final DomNode htmlElementScope) {

        final JavaScriptEngine engine = (JavaScriptEngine) getWebClient().getJavaScriptEngine();
        final Object result = engine.callFunction(this, function, thisObject, args, htmlElementScope);

        return new ScriptResult(result);
    }

    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(originalCharset_ == null ? null : originalCharset_.name());
    }

    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        final String charsetName = (String) ois.readObject();
        if (charsetName != null) {
            originalCharset_ = Charset.forName(charsetName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNodeValue(final String value) {
        // Default behavior is to do nothing, overridden in some subclasses
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrefix(final String prefix) {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearComputedStyles() {
        if (computedStylesCache_ != null) {
            computedStylesCache_.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearComputedStyles(final DomElement element) {
        if (computedStylesCache_ != null) {
            computedStylesCache_.remove(element);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearComputedStylesUpToRoot(final DomElement element) {
        if (computedStylesCache_ != null) {
            computedStylesCache_.remove(element);

            DomNode parent = element.getParentNode();
            while (parent != null) {
                computedStylesCache_.remove(parent);
                parent = parent.getParentNode();
            }
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param element the element to clear its cache
     * @param normalizedPseudo the pseudo attribute
     * @return the cached CSS2Properties object or null
     */
    public ComputedCssStyleDeclaration getStyleFromCache(final DomElement element,
            final String normalizedPseudo) {
        final ComputedCssStyleDeclaration styleFromCache = getCssPropertiesCache().get(element, normalizedPseudo);
        return styleFromCache;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Caches a CSS2Properties object.
     * @param element the element to clear its cache
     * @param normalizedPseudo the pseudo attribute
     * @param style the CSS2Properties to cache
     */
    public void putStyleIntoCache(final DomElement element, final String normalizedPseudo,
            final ComputedCssStyleDeclaration style) {
        getCssPropertiesCache().put(element, normalizedPseudo, style);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @return a list of all styles from this page (&lt;style&gt; and &lt;link rel=stylesheet&gt;).
     * This returns an empty list if css support is disabled in the web client options.
     */
    public List<CssStyleSheet> getStyleSheets() {
        final List<CssStyleSheet> styles = new ArrayList<>();
        if (getWebClient().getOptions().isCssEnabled()) {
            for (final HtmlElement htmlElement : getHtmlElementDescendants()) {
                if (htmlElement instanceof HtmlStyle) {
                    styles.add(((HtmlStyle) htmlElement).getSheet());
                    continue;
                }

                if (htmlElement instanceof HtmlLink) {
                    final HtmlLink link = (HtmlLink) htmlElement;
                    if (link.isStyleSheetLink()) {
                        styles.add(link.getSheet());
                    }
                }
            }
        }
        return styles;
    }

    /**
     * @return the CSSPropertiesCache for this page
     */
    private ComputedStylesCache getCssPropertiesCache() {
        if (computedStylesCache_ == null) {
            computedStylesCache_ = new ComputedStylesCache();

            // maintain the style cache
            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl();
            addDomChangeListener(listener);
            addHtmlAttributeChangeListener(listener);
        }
        return computedStylesCache_;
    }

    /**
     * <p>Listens for changes anywhere in the document and evicts cached computed styles whenever something relevant
     * changes. Note that the very lazy way of doing this (completely clearing the cache every time something happens)
     * results in very meager performance gains. In order to get good (but still correct) performance, we need to be
     * a little smarter.</p>
     *
     * <p>CSS 2.1 has the following <a href="http://www.w3.org/TR/CSS21/selector.html">selector types</a> (where "SN" is
     * shorthand for "the selected node"):</p>
     *
     * <ol>
     *   <li><em>Universal</em> (i.e. "*"): Affected by the removal of SN from the document.</li>
     *   <li><em>Type</em> (i.e. "div"): Affected by the removal of SN from the document.</li>
     *   <li><em>Descendant</em> (i.e. "div span"): Affected by changes to SN or to any of its ancestors.</li>
     *   <li><em>Child</em> (i.e. "div &gt; span"): Affected by changes to SN or to its parent.</li>
     *   <li><em>Adjacent Sibling</em> (i.e. "table + p"): Affected by changes to SN or its previous sibling.</li>
     *   <li><em>Attribute</em> (i.e. "div.up, div[class~=up]"): Affected by changes to an attribute of SN.</li>
     *   <li><em>ID</em> (i.e. "#header): Affected by changes to the <code>id</code> attribute of SN.</li>
     *   <li><em>Pseudo-Elements and Pseudo-Classes</em> (i.e. "p:first-child"): Affected by changes to parent.</li>
     * </ol>
     *
     * <p>Together, these rules dictate that the smart (but still lazy) way of removing elements from the computed style
     * cache is as follows -- whenever a node changes in any way, the cache needs to be cleared of styles for nodes
     * which:</p>
     *
     * <ul>
     *   <li>are actually the same node as the node that changed</li>
     *   <li>are siblings of the node that changed</li>
     *   <li>are descendants of the node that changed</li>
     * </ul>
     *
     * <p>Additionally, whenever a <code>style</code> node or a <code>link</code> node
     * with <code>rel=stylesheet</code> is added or
     * removed, all elements should be removed from the computed style cache.</p>
     */
    private class DomHtmlAttributeChangeListenerImpl implements DomChangeListener, HtmlAttributeChangeListener {

        DomHtmlAttributeChangeListenerImpl() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void nodeAdded(final DomChangeEvent event) {
            nodeChanged(event.getChangedNode(), null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void nodeDeleted(final DomChangeEvent event) {
            nodeChanged(event.getChangedNode(), null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            nodeChanged(event.getHtmlElement(), event.getName());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            nodeChanged(event.getHtmlElement(), event.getName());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            nodeChanged(event.getHtmlElement(), event.getName());
        }

        private void nodeChanged(final DomNode changedNode, final String attribName) {
            // If a stylesheet was changed, all of our calculations could be off; clear the cache.
            if (changedNode instanceof HtmlStyle) {
                clearComputedStyles();
                return;
            }
            if (changedNode instanceof HtmlLink) {
                if (((HtmlLink) changedNode).isStyleSheetLink()) {
                    clearComputedStyles();
                    return;
                }
            }

            // Apparently it wasn't a stylesheet that changed; be semi-smart about what we evict and when.
            final boolean clearParents = ATTRIBUTES_AFFECTING_PARENT.contains(attribName);
            if (computedStylesCache_ != null) {
                computedStylesCache_.nodeChanged(changedNode, clearParents);
            }
        }
    }

    /**
     * Cache computed styles when possible, because their calculation is very expensive.
     * We use a weak hash map because we don't want this cache to be the only reason
     * nodes are kept around in the JVM, if all other references to them are gone.
     */
    private static final class ComputedStylesCache implements Serializable {
        private transient WeakHashMap<DomElement, Map<String, ComputedCssStyleDeclaration>>
                    computedStyles_ = new WeakHashMap<>();

        ComputedStylesCache() {
        }

        public synchronized ComputedCssStyleDeclaration get(final DomElement element,
                final String normalizedPseudo) {
            final Map<String, ComputedCssStyleDeclaration> elementMap = computedStyles_.get(element);
            if (elementMap != null) {
                return elementMap.get(normalizedPseudo);
            }
            return null;
        }

        public synchronized void put(final DomElement element,
                final String normalizedPseudo, final ComputedCssStyleDeclaration style) {
            final Map<String, ComputedCssStyleDeclaration>
                    elementMap = computedStyles_.computeIfAbsent(element, k -> new WeakHashMap<>());
            elementMap.put(normalizedPseudo, style);
        }

        public synchronized void nodeChanged(final DomNode changed, final boolean clearParents) {
            final Iterator<Map.Entry<DomElement, Map<String, ComputedCssStyleDeclaration>>>
                    i = computedStyles_.entrySet().iterator();
            while (i.hasNext()) {
                final Map.Entry<DomElement, Map<String, ComputedCssStyleDeclaration>> entry = i.next();
                final DomElement node = entry.getKey();
                if (changed == node
                    || changed.getParentNode() == node.getParentNode()
                    || changed.isAncestorOf(node)
                    || clearParents && node.isAncestorOf(changed)) {
                    i.remove();
                }
            }

            // maybe this is a better solution but i have to think a bit more about this
            //
            //            if (computedStyles_.isEmpty()) {
            //                return;
            //            }
            //
            //            // remove all siblings
            //            DomNode parent = changed.getParentNode();
            //            if (parent != null) {
            //                for (DomNode sibling : parent.getChildNodes()) {
            //                    computedStyles_.remove(sibling.getScriptableObject());
            //                }
            //
            //                if (clearParents) {
            //                    // remove all parents
            //                    while (parent != null) {
            //                        computedStyles_.remove(parent.getScriptableObject());
            //                        parent = parent.getParentNode();
            //                    }
            //                }
            //            }
            //
            //            // remove changed itself and all descendants
            //            computedStyles_.remove(changed.getScriptableObject());
            //            for (DomNode descendant : changed.getDescendants()) {
            //                computedStyles_.remove(descendant.getScriptableObject());
            //            }
        }

        public synchronized void clear() {
            computedStyles_.clear();
        }

        public synchronized Map<String, ComputedCssStyleDeclaration> remove(
                final DomNode element) {
            return computedStyles_.remove(element);
        }

        private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            computedStyles_ = new WeakHashMap<>();
        }
    }
}
