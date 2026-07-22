/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static org.htmlunit.BrowserVersionFeatures.ANCHOR_SEND_PING_REQUEST;
import static org.htmlunit.BrowserVersionFeatures.FORM_SUBMISSION_HEADER_CACHE_CONTROL_MAX_AGE;
import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.BrowserVersion;
import org.htmlunit.FormEncodingType;
import org.htmlunit.HttpHeader;
import org.htmlunit.HttpMethod;
import org.htmlunit.Page;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.WebWindow;
import org.htmlunit.protocol.javascript.JavaScriptURLConnection;
import org.htmlunit.util.ArrayUtils;
import org.htmlunit.util.StringUtils;
import org.htmlunit.util.UrlUtils;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * A helper class to be used by elements which support {@link HyperlinkElement} -
 * currently {@link HtmlAnchor} ({@code a}) and {@link HtmlArea} ({@code area}), the two
 * elements the HTML spec defines as "hyperlink elements".
 *
 * @author Ronald Brill
 */
public final class HyperlinkElementSupport {

    private static final Log LOG = LogFactory.getLog(HyperlinkElementSupport.class);

    private HyperlinkElementSupport() {
        // util class
    }

    /**
     * Support method that is called from {@link HtmlAnchor#doClickStateUpdate(boolean, boolean, String)}
     * and {@link HtmlArea#doClickStateUpdate(boolean, boolean)}: resolves the target URL, sends the
     * hyperlink-auditing ("ping") request if applicable, builds the navigation {@link WebRequest}
     * (including Sec-Fetch-* setup), and hands it off for download/navigation.
     *
     * @param element the hyperlink element being clicked
     * @param shiftKey {@code true} if SHIFT is pressed
     * @param ctrlKey {@code true} if CTRL is pressed
     * @param hrefSuffix the suffix to add to the element's {@code href} attribute
     *        (for instance coordinates from an image map)
     * @throws IOException if an IO error occurs
     */
    public static void doClickStateUpdate(final HyperlinkElement element, final boolean shiftKey,
            final boolean ctrlKey, final String hrefSuffix) throws IOException {
        final HtmlElement htmlElement = (HtmlElement) element;

        final String href = (element.getHrefAttribute() + hrefSuffix).trim();
        if (LOG.isDebugEnabled()) {
            final String w = htmlElement.getPage().getEnclosingWindow().getName();
            LOG.debug("do click action in window '" + w + "', using href '" + href + "'");
        }
        if (ATTRIBUTE_NOT_DEFINED == element.getHrefAttribute()) {
            return;
        }
        final String downloadAttribute = element.getDownloadAttribute();
        HtmlPage page = (HtmlPage) htmlElement.getPage();
        if (StringUtils.startsWithIgnoreCase(href, JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
            final StringBuilder builder = new StringBuilder(href.length());
            builder.append(JavaScriptURLConnection.JAVASCRIPT_PREFIX);
            for (int i = JavaScriptURLConnection.JAVASCRIPT_PREFIX.length(); i < href.length(); i++) {
                final char ch = href.charAt(i);
                if (ch == '%' && i + 2 < href.length()) {
                    final char ch1 = Character.toUpperCase(href.charAt(i + 1));
                    final char ch2 = Character.toUpperCase(href.charAt(i + 2));
                    if ((Character.isDigit(ch1) || ch1 >= 'A' && ch1 <= 'F')
                            && (Character.isDigit(ch2) || ch2 >= 'A' && ch2 <= 'F')) {
                        builder.append((char) Integer.parseInt(href.substring(i + 1, i + 3), 16));
                        i += 2;
                        continue;
                    }
                }
                builder.append(ch);
            }

            final String target;
            if (shiftKey || ctrlKey || ATTRIBUTE_NOT_DEFINED != downloadAttribute) {
                target = WebClient.TARGET_BLANK;
            }
            else {
                target = page.getResolvedTarget(element.getTargetAttribute());
            }
            final WebWindow win = page.getWebClient().openTargetWindow(page.getEnclosingWindow(),
                    target, WebClient.TARGET_SELF);
            Page enclosedPage = win.getEnclosedPage();
            if (enclosedPage == null) {
                win.getWebClient().getPage(win, WebRequest.newAboutBlankRequest());
                enclosedPage = win.getEnclosedPage();
            }
            if (enclosedPage != null && enclosedPage.isHtmlPage()) {
                page = (HtmlPage) enclosedPage;
                page.executeJavaScript(builder.toString(), "javascript url", htmlElement.getStartLineNumber());
            }
            return;
        }

        final URL url = getTargetUrl(href, page);

        final URL pageUrl = page.getUrl();

        final WebClient webClient = page.getWebClient();
        final BrowserVersion browser = webClient.getBrowserVersion();
        if (ATTRIBUTE_NOT_DEFINED != element.getPingAttribute() && browser.hasFeature(ANCHOR_SEND_PING_REQUEST)) {
            final URL pingUrl = getTargetUrl(element.getPingAttribute(), page);
            final WebRequest pingRequest = new WebRequest(pingUrl, HttpMethod.POST);
            pingRequest.setAdditionalHeader(HttpHeader.ACCEPT_ENCODING, browser.getAcceptEncodingHeader());
            pingRequest.setAdditionalHeader(HttpHeader.PING_FROM, pageUrl.toExternalForm());
            pingRequest.setAdditionalHeader(HttpHeader.PING_TO, url.toExternalForm());
            pingRequest.setEncodingType(FormEncodingType.TEXT_PLAIN); // text/ping
            try {
                pingRequest.setAdditionalHeader(HttpHeader.ORIGIN,
                        UrlUtils.getUrlWithProtocolAndAuthority(pageUrl).toExternalForm());
            }
            catch (final MalformedURLException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Invalid origin url '" + pageUrl + "'");
                }
            }
            if (browser.hasFeature(FORM_SUBMISSION_HEADER_CACHE_CONTROL_MAX_AGE)) {
                pingRequest.setAdditionalHeader(HttpHeader.CACHE_CONTROL, "max-age=0");
            }
            pingRequest.setRequestBody("PING");

            // Sec-Fetch-* support (https://www.w3.org/TR/fetch-metadata/):
            // hyperlink-auditing (ping) requests have no destination and are always no-cors
            pingRequest.setFetchDestination(WebRequest.FetchDestination.EMPTY);
            pingRequest.setFetchModeOverride(WebRequest.FetchMode.NO_CORS);
            pingRequest.setRequestingUrl(pageUrl);

            try {
                webClient.loadWebResponse(pingRequest);
            }
            catch (final Exception e) {
                // ignore
            }
        }

        final WebRequest webRequest = new WebRequest(url, browser.getHtmlAcceptHeader(),
                                                            browser.getAcceptEncodingHeader());
        // use the page encoding even if this is a GET requests
        webRequest.setCharset(page.getCharset());

        // Sec-Fetch-* support (https://www.w3.org/TR/fetch-metadata/):
        // this is a top-level navigation, initiated by the page containing the link.
        // The initiator must be set regardless of "noreferrer", since Sec-Fetch-Site
        // reflects the true relationship between initiator and target even when the
        // Referer header itself is suppressed.
        // TODO: userActivation is hardcoded to true here because doClickStateUpdate()
        // currently has no way to tell a real (WebDriver-simulated) click apart from a
        // script-triggered one (e.g. anchor.click()/area.click()); real browsers only
        // send Sec-Fetch-User for the former. Once the click/event dispatch path exposes
        // a trusted/user-gesture flag, thread it through instead of hardcoding this.
        webRequest.markAsNavigation(page.getUrl(), true);

        if (!relContainsNoreferrer(element)) {
            webRequest.setRefererHeader(page.getUrl());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "Getting page for " + url.toExternalForm()
                    + ", derived from href '" + href
                    + "', using the originating URL "
                    + page.getUrl());
        }

        final String target;
        if (shiftKey || ctrlKey
                || (webClient.getAttachmentHandler() == null
                        && ATTRIBUTE_NOT_DEFINED != downloadAttribute)) {
            target = WebClient.TARGET_BLANK;
        }
        else {
            target = page.getResolvedTarget(element.getTargetAttribute());
        }
        page.getWebClient().download(page.getEnclosingWindow(), target, webRequest,
                true, (ATTRIBUTE_NOT_DEFINED == downloadAttribute) ? null : downloadAttribute,
                htmlElement.getClass().getSimpleName() + " click");
    }

    /**
     * Returns {@code true} if the element's {@code rel} attribute contains {@code noreferrer}.
     *
     * @param element the hyperlink element
     * @return {@code true} if the element's {@code rel} attribute contains {@code noreferrer}
     */
    public static boolean relContainsNoreferrer(final HyperlinkElement element) {
        String rel = element.getRelAttribute();
        if (rel != null) {
            rel = rel.toLowerCase(Locale.ROOT);
            return ArrayUtils.contains(StringUtils.splitAtBlank(rel), "noreferrer");
        }
        return false;
    }

    /**
     * Returns the calculated target url.
     *
     * @param href the href
     * @param page the HtmlPage
     * @return the calculated target url.
     * @throws MalformedURLException if an IO error occurs
     */
    public static URL getTargetUrl(final String href, final HtmlPage page) throws MalformedURLException {
        URL url = page.getFullyQualifiedUrl(href);
        // fix for empty url
        if (StringUtils.isEmptyOrNull(href)) {
            url = UrlUtils.getUrlWithNewRef(url, null);
        }
        return url;
    }
}
