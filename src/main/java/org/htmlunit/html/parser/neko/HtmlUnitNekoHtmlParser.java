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
package org.htmlunit.html.parser.neko;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlunit.ObjectInstantiationException;
import org.htmlunit.Page;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebAssert;
import org.htmlunit.WebResponse;
import org.htmlunit.cyberneko.HTMLScanner;
import org.htmlunit.cyberneko.HTMLTagBalancer;
import org.htmlunit.cyberneko.xerces.util.DefaultErrorHandler;
import org.htmlunit.cyberneko.xerces.xni.QName;
import org.htmlunit.cyberneko.xerces.xni.XNIException;
import org.htmlunit.cyberneko.xerces.xni.parser.XMLErrorHandler;
import org.htmlunit.cyberneko.xerces.xni.parser.XMLInputSource;
import org.htmlunit.cyberneko.xerces.xni.parser.XMLParseException;
import org.htmlunit.html.DefaultElementFactory;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.ElementFactory;
import org.htmlunit.html.Html;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.UnknownElementFactory;
import org.htmlunit.html.parser.HTMLParser;
import org.htmlunit.html.parser.HTMLParserListener;
import org.htmlunit.svg.SvgElementFactory;
import org.htmlunit.util.StringUtils;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * <p>SAX parser implementation that uses the NekoHTML {@link org.htmlunit.cyberneko.HTMLConfiguration}
 * to parse HTML into a HtmlUnit-specific DOM (HU-DOM) tree.</p>
 *
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ethan Glasser-Camp
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 */
public final class HtmlUnitNekoHtmlParser implements HTMLParser {

    /**
     * The SVG factory.
     */
    public static final SvgElementFactory SVG_FACTORY = new SvgElementFactory();

    private static final Map<String, ElementFactory> ELEMENT_FACTORIES = new HashMap<>();

    static {
        final DefaultElementFactory defaultElementFactory = new DefaultElementFactory();
        for (final String tagName : DefaultElementFactory.SUPPORTED_TAGS_) {
            ELEMENT_FACTORIES.put(tagName, defaultElementFactory);
        }
    }

    /**
     * Ctor.
     */
    public HtmlUnitNekoHtmlParser() {
        // Empty.
    }

    /**
     * Parses the HTML content from the given string into an object tree representation.
     *
     * @param parent the parent for the new nodes
     * @param source the (X)HTML to be parsed
     * @throws SAXException if a SAX error occurs
     * @throws IOException if an IO error occurs
     */
    @Override
    public void parseFragment(final DomNode parent, final String source) throws SAXException, IOException {
        parseFragment(parent, parent, source, false);
    }

    /**
     * Parses the HTML content from the given string into an object tree representation.
     *
     * @param parent where the new parsed nodes will be added to
     * @param context the context to build the fragment context stack
     * @param source the (X)HTML to be parsed
     * @param createdByJavascript if true the (script) tag was created by javascript
     * @throws SAXException if a SAX error occurs
     * @throws IOException if an IO error occurs
     */
    @Override
    public void parseFragment(final DomNode parent, final DomNode context, final String source,
            final boolean createdByJavascript)
        throws SAXException, IOException {
        final Page page = parent.getPage();
        if (!(page instanceof HtmlPage)) {
            return;
        }
        final HtmlPage htmlPage = (HtmlPage) page;
        final URL url = htmlPage.getUrl();

        final HtmlUnitNekoDOMBuilder domBuilder =
                new HtmlUnitNekoDOMBuilder(this, parent, url, source, createdByJavascript);
        domBuilder.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
        // build fragment context stack
        DomNode node = context;
        final List<QName> ancestors = new ArrayList<>();
        while (node != null && node.getNodeType() != Node.DOCUMENT_NODE) {
            ancestors.add(0, new QName(null, node.getNodeName(), null, null));
            node = node.getParentNode();
        }
        if (ancestors.isEmpty() || !"html".equals(ancestors.get(0).getLocalpart())) {
            ancestors.add(0, new QName(null, "html", null, null));
        }
        if (ancestors.size() == 1 || !"body".equals(ancestors.get(1).getLocalpart())) {
            ancestors.add(1, new QName(null, "body", null, null));
        }

        domBuilder.setFeature(HTMLScanner.ALLOW_SELFCLOSING_TAGS, true);
        domBuilder.setProperty(HTMLTagBalancer.FRAGMENT_CONTEXT_STACK, ancestors.toArray(new QName[0]));

        final XMLInputSource in = new XMLInputSource(null, url.toString(), null, new StringReader(source), null);

        htmlPage.registerParsingStart();
        htmlPage.registerSnippetParsingStart();
        try {
            domBuilder.parse(in);
        }
        finally {
            htmlPage.registerParsingEnd();
            htmlPage.registerSnippetParsingEnd();
        }
    }

    /**
     * Parses the WebResponse into an object tree representation.
     *
     * @param webResponse the response data
     * @param page the HtmlPage to add the nodes
     * @param xhtml if true use the XHtml parser
     * @param createdByJavascript if true the (script) tag was created by javascript
     * @throws IOException if there is an IO error
     */
    @Override
    public void parse(final WebResponse webResponse, final HtmlPage page,
            final boolean xhtml, final boolean createdByJavascript) throws IOException {
        final URL url = webResponse.getWebRequest().getUrl();
        final HtmlUnitNekoDOMBuilder domBuilder =
                new HtmlUnitNekoDOMBuilder(this, page, url, null, createdByJavascript);

        Charset charset = webResponse.getContentCharsetOrNull();
        try {
            if (charset == null) {
                charset = StandardCharsets.ISO_8859_1;
            }
            else {
                domBuilder.setFeature(HTMLScanner.IGNORE_SPECIFIED_CHARSET, true);
            }

            // xml content is different
            if (xhtml) {
                domBuilder.setFeature(HTMLScanner.ALLOW_SELFCLOSING_TAGS, true);
                domBuilder.setFeature(HTMLScanner.SCRIPT_STRIP_CDATA_DELIMS, true);
                domBuilder.setFeature(HTMLScanner.STYLE_STRIP_CDATA_DELIMS, true);
            }
        }
        catch (final Exception e) {
            throw new ObjectInstantiationException("Error setting HTML parser feature", e);
        }

        try (InputStream content = webResponse.getContentAsStream()) {
            final String encoding = charset.name();
            final XMLInputSource in = new XMLInputSource(null, url.toString(), null, content, encoding);

            page.registerParsingStart();
            try {
                domBuilder.parse(in);
            }
            catch (final XNIException e) {
                // extract enclosed exception
                final Throwable origin = extractNestedException(e);
                throw new RuntimeException("Failed parsing content from " + url, origin);
            }
        }
        finally {
            page.registerParsingEnd();
        }
    }

    /**
     * Extract nested exception within an XNIException (Nekohtml uses reflection and generated
     * exceptions are wrapped many times within XNIException and InvocationTargetException)
     *
     * @param e the original XNIException
     * @return the cause exception
     */
    static Throwable extractNestedException(final Throwable e) {
        Throwable originalException = e;
        Throwable cause = ((XNIException) e).getException();
        while (cause != null) {
            originalException = cause;
            if (cause instanceof XNIException) {
                cause = ((XNIException) cause).getException();
            }
            else if (cause instanceof InvocationTargetException) {
                cause = cause.getCause();
            }
            else {
                cause = null;
            }
        }
        return originalException;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ElementFactory getSvgFactory() {
        return SVG_FACTORY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ElementFactory getFactory(final String tagName) {
        final ElementFactory result = ELEMENT_FACTORIES.get(tagName);

        if (result != null) {
            return result;
        }
        return UnknownElementFactory.instance;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns the pre-registered element factory corresponding to the specified tag, or an UnknownElementFactory.
     * @param page the page
     * @param namespaceURI the namespace URI
     * @param qualifiedName the qualified name
     * @param insideSvg is the node inside an SVG node or not
     * @param svgSupport true if called from javascript createElementNS
     * @return the pre-registered element factory corresponding to the specified tag, or an UnknownElementFactory
     */
    @Override
    public ElementFactory getElementFactory(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final boolean insideSvg, final boolean svgSupport) {
        if (insideSvg) {
            return SVG_FACTORY;
        }

        if (namespaceURI == null || namespaceURI.isEmpty()
            || Html.XHTML_NAMESPACE.equals(namespaceURI)
            || Html.SVG_NAMESPACE.equals(namespaceURI)
            || !qualifiedName.contains(":")) {

            String tagName = qualifiedName;
            final int index = tagName.indexOf(':');
            if (index == -1) {
                tagName = StringUtils.toRootLowerCase(tagName);
            }
            else {
                tagName = tagName.substring(index + 1);
            }
            final ElementFactory factory;
            if (svgSupport && !"svg".equals(tagName) && Html.SVG_NAMESPACE.equals(namespaceURI)) {
                factory = SVG_FACTORY;
            }
            else {
                factory = ELEMENT_FACTORIES.get(tagName);
            }

            if (factory != null) {
                return factory;
            }
        }
        return UnknownElementFactory.instance;
    }
}

/**
 * Utility to transmit parsing errors to a {@link HTMLParserListener}.
 */
class HtmlUnitNekoHTMLErrorHandler implements XMLErrorHandler {
    private final HTMLParserListener listener_;
    private final URL url_;
    private final String html_;

    HtmlUnitNekoHTMLErrorHandler(final HTMLParserListener listener, final URL url, final String htmlContent) {
        WebAssert.notNull("listener", listener);
        WebAssert.notNull("url", url);
        listener_ = listener;
        url_ = url;
        html_ = htmlContent;
    }

    /** @see DefaultErrorHandler#error(String,String,XMLParseException) */
    @Override
    public void error(final String domain, final String key,
            final XMLParseException exception) throws XNIException {
        listener_.error(exception.getMessage(),
                url_,
                html_,
                exception.getLineNumber(),
                exception.getColumnNumber(),
                key);
    }

    /** @see DefaultErrorHandler#warning(String,String,XMLParseException) */
    @Override
    public void warning(final String domain, final String key,
            final XMLParseException exception) throws XNIException {
        listener_.warning(exception.getMessage(),
                url_,
                html_,
                exception.getLineNumber(),
                exception.getColumnNumber(),
                key);
    }

    @Override
    public void fatalError(final String domain, final String key,
            final XMLParseException exception) throws XNIException {
        listener_.error(exception.getMessage(),
                url_,
                html_,
                exception.getLineNumber(),
                exception.getColumnNumber(),
                key);
    }
}
