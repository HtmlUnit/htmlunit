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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.Html;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParser;
import com.gargoylesoftware.htmlunit.html.parser.neko.HtmlUnitNekoHtmlParser;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * The default implementation of {@link PageCreator}. Designed to be extended for easier handling of new content
 * types. Just check the content type in <tt>createPage()</tt> and call <tt>super(createPage())</tt> if your custom
 * type isn't found. There are also protected <tt>createXXXXPage()</tt> methods for creating the {@link Page} types
 * which HtmlUnit already knows about for your custom content types.
 *
 * <p>
 * The following table shows the type of {@link Page} created depending on the content type:<br>
 * <br>
 *  <table border="1" width="50%" summary="Page Types">
 *    <tr>
 *      <th>Content type</th>
 *      <th>Type of page</th>
 *    </tr>
 *    <tr>
 *      <td>text/html<br>
 *          text/javascript</td>
 *      <td>{@link HtmlPage}</td>
 *    </tr>
 *    <tr>
 *      <td>text/xml<br>
 *      application/xml<br>
 *      text/vnd.wap.wml<br>
 *      *+xml
 *      </td>
 *      <td>{@link XmlPage}, or an {@link XHtmlPage} if an XHTML namespace is used</td>
 *    </tr>
 *    <tr>
 *      <td>text/*</td>
 *      <td>{@link TextPage}</td>
 *    </tr>
 *    <tr>
 *      <td>Anything else</td>
 *      <td>{@link UnexpectedPage}</td>
 *    </tr>
 *  </table>
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:yourgod@users.sourceforge.net">Brad Clarke</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Antoni Reus
 */
public class DefaultPageCreator implements PageCreator, Serializable {

    private static final byte[] markerUTF8_ = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
    private static final byte[] markerUTF16BE_ = {(byte) 0xfe, (byte) 0xff};
    private static final byte[] markerUTF16LE_ = {(byte) 0xff, (byte) 0xfe};

    /**
     * See http://tools.ietf.org/html/draft-abarth-mime-sniff-05
     */
    private static final String[] htmlPatterns = {"!DOCTYPE HTML", "HTML", "HEAD", "SCRIPT",
        "IFRAME", "H1", "DIV", "FONT", "TABLE", "A", "STYLE", "TITLE", "B", "BODY", "BR", "P", "!--" };

    private static final HTMLParser htmlParser_ = new HtmlUnitNekoHtmlParser();

    /**
     * The different supported page types.
     */
    public enum PageType {
        /** html. */
        HTML,
        /** javascript. */
        JAVASCRIPT,
        /** xml. */
        XML,
        /** text. */
        TEXT,
        /** unknown. */
        UNKNOWN
    }

    /**
     * Determines the kind of page to create from the content type.
     * @param contentType the content type to evaluate
     * @return "xml", "html", "javascript", "text" or "unknown"
     */
    public static PageType determinePageType(final String contentType) {
        if (null == contentType) {
            return PageType.UNKNOWN;
        }

        final String contentTypeLC = contentType.toLowerCase(Locale.ROOT);
        switch (contentTypeLC) {
            case MimeType.TEXT_HTML:
            case "image/svg+xml":
                return PageType.HTML;

            case "text/javascript":
            case "application/x-javascript":
            case MimeType.APPLICATION_JAVASCRIPT:
                return PageType.JAVASCRIPT;

            case MimeType.TEXT_XML:
            case "application/xml":
            case "text/vnd.wap.wml":
                return PageType.XML;

            default:
                if (contentTypeLC.endsWith("+xml")) {
                    return PageType.XML;
                }

                if (contentTypeLC.startsWith("text/")) {
                    return PageType.TEXT;
                }

                return PageType.UNKNOWN;
        }
    }

    /**
     * Determines the kind of page to create from the content type.
     * @param webResponse the response to investigate
     * @exception IOException if an IO problem occurs
     * @return "xml", "html", "javascript", "text" or "unknown"
     */
    public static PageType determinePageType(final WebResponse webResponse) throws IOException {
        final String contentType = webResponse.getContentType();
        if (!StringUtils.isEmpty(contentType)) {
            return determinePageType(contentType);
        }

        // sniff - http://tools.ietf.org/html/draft-abarth-mime-sniff-05
        try (InputStream contentAsStream = webResponse.getContentAsStream()) {
            final byte[] bytes = read(contentAsStream, 512);
            if (bytes.length == 0) {
                return determinePageType(MimeType.TEXT_PLAIN);
            }

            if (startsWith(bytes, markerUTF8_) || startsWith(bytes, markerUTF16BE_)
                    || startsWith(bytes, markerUTF16LE_)) {
                return determinePageType(MimeType.TEXT_PLAIN);
            }

            if (isBinary(bytes)) {
                return determinePageType(MimeType.APPLICATION_OCTET_STREAM);
            }

            final String asAsciiString = new String(bytes, "ASCII").trim().toUpperCase(Locale.ROOT);
            for (final String htmlPattern : htmlPatterns) {
                try {
                    if ('<' == asAsciiString.charAt(0)) {
                        if (asAsciiString.startsWith(htmlPattern, 1)) {
                            final char spaceOrBracket = asAsciiString.charAt(htmlPattern.length() + 1);
                            if (' ' == spaceOrBracket || '>' == spaceOrBracket) {
                                return determinePageType(MimeType.TEXT_HTML);
                            }
                        }
                    }
                }
                catch (final ArrayIndexOutOfBoundsException e) {
                    // ignore and try next
                }
            }
        }
        return determinePageType(MimeType.TEXT_PLAIN);
    }

    /**
     * Creates an instance.
     */
    public DefaultPageCreator() {
        // Empty.
    }

    /**
     * Create a Page object for the specified web response.
     *
     * @param webResponse the response from the server
     * @param webWindow the window that this page will be loaded into
     * @exception IOException if an IO problem occurs
     * @return the new page object
     */
    @Override
    public Page createPage(final WebResponse webResponse, final WebWindow webWindow) throws IOException {
        final PageType pageType = determinePageType(webResponse);
        switch (pageType) {
            case HTML:
                return createHtmlPage(webResponse, webWindow);

            case JAVASCRIPT:
                return createHtmlPage(webResponse, webWindow);

            case XML:
                final SgmlPage sgmlPage = createXmlPage(webResponse, webWindow);
                final DomElement doc = sgmlPage.getDocumentElement();
                if (doc != null && Html.XHTML_NAMESPACE.equals(doc.getNamespaceURI())) {
                    return createXHtmlPage(webResponse, webWindow);
                }
                return sgmlPage;

            case TEXT:
                return createTextPage(webResponse, webWindow);

            default:
                return createUnexpectedPage(webResponse, webWindow);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLParser getHtmlParser() {
        return htmlParser_;
    }

    /**
     * See http://tools.ietf.org/html/draft-abarth-mime-sniff-05#section-4
     * @param bytes the bytes to check
     */
    private static boolean isBinary(final byte[] bytes) {
        for (final byte b : bytes) {
            if ((b >= 0x00 && b < 0x08)
                || b == 0x0B
                || (b >= 0x0E && b <= 0x1A)
                || (b >= 0x1C && b <= 0x1F)) {
                return true;
            }
        }
        return false;
    }

    private static boolean startsWith(final byte[] bytes, final byte[] lookFor) {
        if (bytes.length < lookFor.length) {
            return false;
        }

        for (int i = 0; i < lookFor.length; i++) {
            if (bytes[i] != lookFor[i]) {
                return false;
            }
        }

        return true;
    }

    private static byte[] read(final InputStream stream, final int maxNb) throws IOException {
        final byte[] buffer = new byte[maxNb];
        final int nbRead = stream.read(buffer);
        if (nbRead == buffer.length) {
            return buffer;
        }
        return ArrayUtils.subarray(buffer, 0, nbRead);
    }

    /**
     * Creates an HtmlPage for this WebResponse.
     *
     * @param webResponse the page's source
     * @param webWindow the WebWindow to place the HtmlPage in
     * @return the newly created HtmlPage
     * @throws IOException if the page could not be created
     */
    protected HtmlPage createHtmlPage(final WebResponse webResponse, final WebWindow webWindow) throws IOException {
        final HtmlPage page = new HtmlPage(webResponse, webWindow);
        webWindow.setEnclosedPage(page);

        htmlParser_.parse(webResponse, page, false);
        return page;
    }

    /**
     * Creates an XHtmlPage for this WebResponse.
     *
     * @param webResponse the page's source
     * @param webWindow the WebWindow to place the HtmlPage in
     * @return the newly created XHtmlPage
     * @throws IOException if the page could not be created
     */
    protected XHtmlPage createXHtmlPage(final WebResponse webResponse, final WebWindow webWindow) throws IOException {
        final XHtmlPage page = new XHtmlPage(webResponse, webWindow);
        webWindow.setEnclosedPage(page);

        htmlParser_.parse(webResponse, page, true);
        return page;
    }

    /**
     * Creates a TextPage for this WebResponse.
     *
     * @param webResponse the page's source
     * @param webWindow the WebWindow to place the TextPage in
     * @return the newly created TextPage
     */
    protected TextPage createTextPage(final WebResponse webResponse, final WebWindow webWindow) {
        final TextPage newPage = new TextPage(webResponse, webWindow);
        webWindow.setEnclosedPage(newPage);
        return newPage;
    }

    /**
     * Creates an UnexpectedPage for this WebResponse.
     *
     * @param webResponse the page's source
     * @param webWindow the WebWindow to place the UnexpectedPage in
     * @return the newly created UnexpectedPage
     */
    protected UnexpectedPage createUnexpectedPage(final WebResponse webResponse, final WebWindow webWindow) {
        final UnexpectedPage newPage = new UnexpectedPage(webResponse, webWindow);
        webWindow.setEnclosedPage(newPage);
        return newPage;
    }

    /**
     * Creates an SgmlPage for this WebResponse.
     *
     * @param webResponse the page's source
     * @param webWindow the WebWindow to place the TextPage in
     * @return the newly created TextPage
     * @throws IOException if the page could not be created
     */
    protected SgmlPage createXmlPage(final WebResponse webResponse, final WebWindow webWindow) throws IOException {
        final SgmlPage page = new XmlPage(webResponse, webWindow);
        webWindow.setEnclosedPage(page);
        return page;
    }

}
