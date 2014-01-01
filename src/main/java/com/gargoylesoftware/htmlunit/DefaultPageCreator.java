/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * The default implementation of {@link PageCreator}. Designed to be extended for easier handling of new content
 * types. Just check the content type in <tt>createPage()</tt> and call <tt>super(createPage())</tt> if your custom
 * type isn't found. There are also protected <tt>createXXXXPage()</tt> methods for creating the {@link Page} types
 * which HtmlUnit already knows about for your custom content types.
 *
 * <p />
 * The following table shows the type of {@link Page} created depending on the content type:<br>
 * <br>
 *  <table border="1" width="50%">
 *    <tr>
 *      <th>Content type</th>
 *      <th>Type of page</th>
 *    </tr>
 *    <tr>
 *      <td>text/html</td>
 *      <td>{@link HtmlPage}</td>
 *    </tr>
 *    <tr>
 *      <td>text/xml<br/>
 *      application/xml<br/>
 *      text/vnd.wap.wml<br/>
 *      *+xml
 *      </td>
 *      <td>{@link XmlPage}, or an {@link XHtmlPage} if an XHTML namespace is used</td>
 *    </tr>
 *    <tr>
 *      <td>text/javascript<br/>
 *      application/x-javascript
 *      </td>
 *      <td>{@link JavaScriptPage}</td>
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
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:yourgod@users.sourceforge.net">Brad Clarke</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public class DefaultPageCreator implements PageCreator, Serializable {

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

        if ("text/html".equals(contentType)) {
            return PageType.HTML;
        }

        if ("text/javascript".equals(contentType) || "application/x-javascript".equals(contentType)
                || "application/javascript".equals(contentType)) {
            return PageType.JAVASCRIPT;
        }

        if ("text/xml".equals(contentType)
            || "application/xml".equals(contentType)
            || "text/vnd.wap.wml".equals(contentType)
            || contentType.endsWith("+xml")) {
            return PageType.XML;
        }

        if (contentType.startsWith("text/")) {
            return PageType.TEXT;
        }

        return PageType.UNKNOWN;
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
    public Page createPage(final WebResponse webResponse, final WebWindow webWindow) throws IOException {
        final String contentType = determineContentType(webResponse.getContentType().toLowerCase(Locale.ENGLISH),
            webResponse.getContentAsStream());

        final PageType pageType = determinePageType(contentType);
        switch (pageType) {
            case HTML:
                return createHtmlPage(webResponse, webWindow);

            case JAVASCRIPT:
                return createJavaScriptPage(webResponse, webWindow);

            case XML:
                final XmlPage xml = createXmlPage(webResponse, webWindow);
                final DomElement doc = xml.getDocumentElement();
                if (doc != null && HTMLParser.XHTML_NAMESPACE.equals(doc.getNamespaceURI())) {
                    return createXHtmlPage(webResponse, webWindow);
                }
                return xml;

            case TEXT:
                return createTextPage(webResponse, webWindow);

            default:
                return createUnexpectedPage(webResponse, webWindow);
        }
    }

    /**
     * Tries to determine the content type.
     * TODO: implement a content type sniffer based on the
     * <a href="http://tools.ietf.org/html/draft-abarth-mime-sniff-05">Content-Type Processing Model</a>
     * @param contentType the contentType header if any
     * @param contentAsStream stream allowing to read the downloaded content
     * @return the sniffed mime type
     * @exception IOException if an IO problem occurs
     */
    protected String determineContentType(final String contentType, final InputStream contentAsStream)
        throws IOException {
        final byte[] markerUTF8 = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
        final byte[] markerUTF16BE = {(byte) 0xfe, (byte) 0xff};
        final byte[] markerUTF16LE = {(byte) 0xff, (byte) 0xfe};

        try {
            if (!StringUtils.isEmpty(contentType)) {
                return contentType;
            }

            final byte[] bytes = read(contentAsStream, 500);
            if (bytes.length == 0) {
                return "text/plain";
            }

            final String asAsciiString = new String(bytes, "ASCII").toUpperCase(Locale.ENGLISH);
            if (asAsciiString.contains("<HTML")) {
                return "text/html";
            }
            else if (startsWith(bytes, markerUTF8) || startsWith(bytes, markerUTF16BE)
                    || startsWith(bytes, markerUTF16LE)) {
                return "text/plain";
            }
            else if (isBinary(bytes)) {
                return "application/octet-stream";
            }
        }
        finally {
            IOUtils.closeQuietly(contentAsStream);
        }
        return "text/plain";
    }

    /**
     * See http://tools.ietf.org/html/draft-abarth-mime-sniff-05#section-4
     * @param bytes the bytes to check
     */
    private boolean isBinary(final byte[] bytes) {
        for (byte b : bytes) {
            if (b < 0x08
                || b == 0x0B
                || (b >= 0x0E && b <= 0x1A)
                || (b >= 0x1C && b <= 0x1F)) {
                return true;
            }
        }
        return false;
    }

    private boolean startsWith(final byte[] bytes, final byte[] lookFor) {
        if (bytes.length < lookFor.length) {
            return false;
        }

        for (int i = 0; i < lookFor.length; ++i) {
            if (bytes[i] != lookFor[i]) {
                return false;
            }
        }

        return true;
    }

    private byte[] read(final InputStream stream, final int maxNb) throws IOException {
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
        return HTMLParser.parseHtml(webResponse, webWindow);
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
        return HTMLParser.parseXHtml(webResponse, webWindow);
    }

    /**
     * Creates a JavaScriptPage for this WebResponse.
     *
     * @param webResponse the page's source
     * @param webWindow the WebWindow to place the JavaScriptPage in
     * @return the newly created JavaScriptPage
     */
    protected JavaScriptPage createJavaScriptPage(final WebResponse webResponse, final WebWindow webWindow) {
        final JavaScriptPage newPage = new JavaScriptPage(webResponse, webWindow);
        webWindow.setEnclosedPage(newPage);
        return newPage;
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
     * Creates an XmlPage for this WebResponse.
     *
     * @param webResponse the page's source
     * @param webWindow the WebWindow to place the TextPage in
     * @return the newly created TextPage
     * @throws IOException if the page could not be created
     */
    protected XmlPage createXmlPage(final WebResponse webResponse, final WebWindow webWindow) throws IOException {
        final XmlPage newPage = new XmlPage(webResponse, webWindow);
        webWindow.setEnclosedPage(newPage);
        return newPage;
    }
}
