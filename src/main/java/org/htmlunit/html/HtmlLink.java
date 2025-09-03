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
package org.htmlunit.html;

import static org.htmlunit.BrowserVersionFeatures.HTMLLINK_CHECK_RESPONSE_TYPE_FOR_STYLESHEET;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.BrowserVersion;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.htmlunit.css.CssStyleSheet;
import org.htmlunit.cssparser.dom.MediaListImpl;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.html.HTMLLinkElement;
import org.htmlunit.util.ArrayUtils;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.StringUtils;
import org.htmlunit.xml.XmlPage;

/**
 * Wrapper for the HTML element "link". <b>Note:</b> This is not a clickable link,
 * that one is an HtmlAnchor
 *
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Christian Sell
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class HtmlLink extends HtmlElement {
    private static final Log LOG = LogFactory.getLog(HtmlLink.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "link";

    /**
     * The associated style sheet (only valid for links of type
     * <code>&lt;link rel="stylesheet" type="text/css" href="..." /&gt;</code>).
     */
    private CssStyleSheet sheet_;

    /**
     * Creates an instance of HtmlLink
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlLink(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute {@code charset}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code charset}
     *         or an empty string if that attribute isn't defined.
     */
    public final String getCharsetAttribute() {
        return getAttributeDirect("charset");
    }

    /**
     * Returns the value of the attribute {@code href}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code href}
     *         or an empty string if that attribute isn't defined.
     */
    public final String getHrefAttribute() {
        return getAttributeDirect("href");
    }

    /**
     * Returns the value of the attribute {@code hreflang}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code hreflang}
     *         or an empty string if that attribute isn't defined.
     */
    public final String getHrefLangAttribute() {
        return getAttributeDirect("hreflang");
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type}
     *         or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttributeDirect(TYPE_ATTRIBUTE);
    }

    /**
     * Returns the value of the attribute {@code rel}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code rel}
     *         or an empty string if that attribute isn't defined.
     */
    public final String getRelAttribute() {
        return getAttributeDirect("rel");
    }

    /**
     * Returns the value of the attribute {@code rev}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code rev}
     *         or an empty string if that attribute isn't defined.
     */
    public final String getRevAttribute() {
        return getAttributeDirect("rev");
    }

    /**
     * Returns the value of the attribute {@code media}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code media}
     *         or an empty string if that attribute isn't defined.
     */
    public final String getMediaAttribute() {
        return getAttributeDirect("media");
    }

    /**
     * Returns the value of the attribute {@code target}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code target}
     *         or an empty string if that attribute isn't defined.
     */
    public final String getTargetAttribute() {
        return getAttributeDirect("target");
    }

    /**
     * <span style="color:red">POTENIAL PERFORMANCE KILLER - DOWNLOADS THE RESOURCE - USE AT YOUR OWN RISK.</span><br>
     * If the linked content is not already downloaded it triggers a download. Then it stores the response
     * for later use.<br>
     *
     * @param downloadIfNeeded indicates if a request should be performed this hasn't been done previously
     * @return {@code null} if no download should be performed and when this wasn't already done; the response
     *         received when performing a request for the content referenced by this tag otherwise
     * @throws IOException if an error occurs while downloading the content
     */
    public WebResponse getWebResponse(final boolean downloadIfNeeded) throws IOException {
        return getWebResponse(downloadIfNeeded, null, false, null);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * If the linked content is not already downloaded it triggers a download. Then it stores the response
     * for later use.<br>
     *
     * @param downloadIfNeeded indicates if a request should be performed this hasn't been done previously
     * @param request the request; if null getWebRequest() is called to create one
     * @param isStylesheetRequest true if this should return a stylesheet
     * @param type the type definined for the stylesheet link
     * @return {@code null} if no download should be performed and when this wasn't already done; the response
     *         received when performing a request for the content referenced by this tag otherwise
     * @throws IOException if an error occurs while downloading the content
     */
    public WebResponse getWebResponse(final boolean downloadIfNeeded, WebRequest request,
            final boolean isStylesheetRequest, final String type) throws IOException {
        final WebClient webClient = getPage().getWebClient();
        if (null == request) {
            request = getWebRequest();
        }

        if (downloadIfNeeded) {
            try {
                final WebResponse response = webClient.loadWebResponse(request);
                if (response.isSuccess()) {
                    if (isStylesheetRequest
                            && webClient.getBrowserVersion()
                                 .hasFeature(HTMLLINK_CHECK_RESPONSE_TYPE_FOR_STYLESHEET)) {

                        if (StringUtils.isNotBlank(type)
                                && !MimeType.TEXT_CSS.equals(type)) {
                            return null;
                        }

                        final String respType = response.getContentType();
                        if (StringUtils.isNotBlank(respType)
                                && !MimeType.TEXT_CSS.equals(respType)) {
                            executeEvent(webClient, Event.TYPE_ERROR);
                            return response;
                        }
                    }
                    executeEvent(webClient, Event.TYPE_LOAD);
                    return response;
                }
                executeEvent(webClient, Event.TYPE_ERROR);
                return response;
            }
            catch (final IOException e) {
                executeEvent(webClient, Event.TYPE_ERROR);
                throw e;
            }
        }

        // retrieve the response, from the cache if available
        return webClient.getCache().getCachedResponse(request);
    }

    /**
     * Returns the request which will allow us to retrieve the content referenced by the {@code href} attribute.
     * @return the request which will allow us to retrieve the content referenced by the {@code href} attribute
     * @throws MalformedURLException in case of problem resolving the URL
     */
    public WebRequest getWebRequest() throws MalformedURLException {
        final HtmlPage page = (HtmlPage) getPage();
        final URL url = page.getFullyQualifiedUrl(getHrefAttribute());

        final BrowserVersion browser = page.getWebClient().getBrowserVersion();
        final WebRequest request = new WebRequest(url, browser.getCssAcceptHeader(), browser.getAcceptEncodingHeader());
        // use the page encoding even if this is a GET requests
        request.setCharset(page.getCharset());
        request.setRefererHeader(page.getUrl());

        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.NONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayBeDisplayed() {
        return false;
    }

    private void executeEvent(final WebClient webClient, final String type) {
        if (!webClient.isJavaScriptEngineEnabled()) {
            return;
        }

        final HTMLLinkElement link = getScriptableObject();
        final Event event = new Event(this, type);
        link.executeEventLocally(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAllChildrenAddedToPage(final boolean postponed) {
        if (getOwnerDocument() instanceof XmlPage) {
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Link node added: " + asXml());
        }

        if (isStyleSheetLink()) {
            final WebClient webClient = getPage().getWebClient();
            if (!webClient.getOptions().isCssEnabled()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Stylesheet Link found but ignored because css support is disabled ("
                                + asXml().replaceAll("[\\r\\n]", "") + ").");
                }
                return;
            }

            if (!webClient.isJavaScriptEngineEnabled()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Stylesheet Link found but ignored because javascript engine is disabled ("
                                + asXml().replaceAll("[\\r\\n]", "") + ").");
                }
                return;
            }

            final PostponedAction action = new PostponedAction(getPage(), "Loading of link " + this) {
                @Override
                public void execute() {
                    final HTMLLinkElement linkElem = HtmlLink.this.getScriptableObject();
                    // force loading, caching inside the link
                    linkElem.getSheet();
                }
            };

            final AbstractJavaScriptEngine<?> engine = webClient.getJavaScriptEngine();
            if (postponed) {
                engine.addPostponedAction(action);
            }
            else {
                try {
                    action.execute();
                }
                catch (final RuntimeException e) {
                    throw e;
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }

            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Link type '" + getRelAttribute() + "' not supported ("
                        + asXml().replaceAll("[\\r\\n]", "") + ").");
        }
    }

    /**
     * Returns the associated style sheet (only valid for links of type
     * <code>&lt;link rel="stylesheet" type="text/css" href="..." /&gt;</code>).
     * @return the associated style sheet
     */
    public CssStyleSheet getSheet() {
        if (sheet_ == null) {
            sheet_ = CssStyleSheet.loadStylesheet(this, this, null);
        }
        return sheet_;
    }

    /**
     * @return true if the rel attribute is 'stylesheet'
     */
    public boolean isStyleSheetLink() {
        final String rel = getRelAttribute();
        if (rel != null) {
            return ArrayUtils.containsIgnoreCase(StringUtils.splitAtBlank(rel), "stylesheet");
        }
        return false;
    }

    /**
     * @return true if the rel attribute is 'modulepreload'
     */
    public boolean isModulePreloadLink() {
        final String rel = getRelAttribute();
        if (rel != null) {
            return ArrayUtils.containsIgnoreCase(StringUtils.splitAtBlank(rel), "modulepreload");
        }
        return false;
    }

    /**
     * <p><span style="color:red">Experimental API: May be changed in next release
     * and may not yet work perfectly!</span></p>
     *
     * Verifies if the provided node is a link node pointing to an active stylesheet.
     *
     * @return true if the provided node is a stylesheet link
     */
    public boolean isActiveStyleSheetLink() {
        if (isStyleSheetLink()) {
            final String media = getMediaAttribute();
            if (StringUtils.isBlank(media)) {
                return true;
            }

            final MediaListImpl mediaList =
                    CssStyleSheet.parseMedia(media, getPage().getWebClient());
            return CssStyleSheet.isActive(mediaList, getPage().getEnclosingWindow());
        }
        return false;
    }
}
