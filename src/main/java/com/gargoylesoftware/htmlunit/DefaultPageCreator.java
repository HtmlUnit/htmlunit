/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import java.io.Serializable;

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
 */
public class DefaultPageCreator implements PageCreator, Serializable  {

    private static final long serialVersionUID = -4420355214574495577L;

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
        if (WebResponseData.isBigContent(webResponse.getResponseHeaders())) {
            return createBigContentPage(webResponse, webWindow);
        }

        final String contentType = webResponse.getContentType().toLowerCase();
        final Page newPage;

        final String pageType = determinePageType(contentType);
        if (pageType.equals("html")) {
            newPage = createHtmlPage(webResponse, webWindow);
        }
        else if (pageType.equals("javascript")) {
            newPage = createJavaScriptPage(webResponse, webWindow);
        }
        else if (pageType.equals("xml")) {
            final XmlPage xml = createXmlPage(webResponse, webWindow);
            final DomElement doc = xml.getDocumentElement();
            if (doc != null && HTMLParser.XHTML_NAMESPACE.equals(doc.getNamespaceURI())) {
                newPage = createXHtmlPage(webResponse, webWindow);
            }
            else {
                newPage = xml;
            }
        }
        else if (pageType.equals("text")) {
            newPage = createTextPage(webResponse, webWindow);
        }
        else {
            newPage = createUnexpectedPage(webResponse, webWindow);
        }
        return newPage;
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
     * Creates a BigContentPage for this WebResponse.
     *
     * @param webResponse the page's source
     * @param webWindow the WebWindow to place the BigContentPage in
     * @return the newly created BigContentPage
     */
    protected BigContentPage createBigContentPage(final WebResponse webResponse, final WebWindow webWindow) {
        final BigContentPage newPage = new BigContentPage(webResponse, webWindow);
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

    /**
     * Determines the kind of page to create from the content type.
     * @param contentType the content type to evaluate
     * @return "xml", "html", "javascript", "text" or "unknown"
     */
    protected String determinePageType(final String contentType) {
        if (contentType.equals("text/html") || contentType.equals("")) {
            return "html";
        }
        else if (contentType.equals("text/javascript") || contentType.equals("application/x-javascript")) {
            return "javascript";
        }
        else if (contentType.equals("text/xml")
                || contentType.equals("application/xml")
                || contentType.equals("text/vnd.wap.wml")
                || contentType.matches(".*\\+xml")) {
            return "xml";
        }
        else if (contentType.startsWith("text/")) {
            return "text";
        }
        else {
            return "unknown";
        }
    }
}
