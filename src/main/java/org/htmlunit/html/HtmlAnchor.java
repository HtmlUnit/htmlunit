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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.htmlunit.Page;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebWindow;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.html.HTMLElement;

/**
 * Wrapper for the HTML element "a".
 *
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Christian Sell
 * @author Ahmed Ashour
 * @author Dmitri Zoubkov
 * @author Ronald Brill
 * @author Frank Danek
 * @author Lai Quang Duong
 */
public class HtmlAnchor extends HtmlElement implements HyperlinkElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "a";

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlAnchor(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <P extends Page> P click(final Event event,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey,
            final boolean ignoreVisibility) throws IOException {
        WebWindow oldWebWindow = null;
        if (ctrlKey) {
            oldWebWindow = ((HTMLElement) event.getSrcElement()).getDomNodeOrDie()
                    .getPage().getWebClient().getCurrentWindow();
        }

        P page = super.click(event, shiftKey, ctrlKey, altKey, ignoreVisibility);

        if (ctrlKey) {
            page.getEnclosingWindow().getWebClient().setCurrentWindow(oldWebWindow);
            page = (P) oldWebWindow.getEnclosedPage();
        }

        return page;
    }

    /**
     * Same as {@link #doClickStateUpdate(boolean, boolean)}, except that it accepts an {@code href} suffix,
     * needed when a click is performed on an image map to pass information on the click position.
     *
     * @param shiftKey {@code true} if SHIFT is pressed
     * @param ctrlKey {@code true} if CTRL is pressed
     * @param hrefSuffix the suffix to add to the anchor's {@code href} attribute
     *        (for instance coordinates from an image map)
     * @throws IOException if an IO error occurs
     */
    protected void doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey, final String hrefSuffix)
            throws IOException {
        HyperlinkElementSupport.doClickStateUpdate(this, shiftKey, ctrlKey, hrefSuffix);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param href the href
     * @param page the HtmlPage
     * @return the calculated target url.
     * @throws MalformedURLException if an IO error occurs
     */
    public static URL getTargetUrl(final String href, final HtmlPage page) throws MalformedURLException {
        return HyperlinkElementSupport.getTargetUrl(href, page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey) throws IOException {
        doClickStateUpdate(shiftKey, ctrlKey, "");
        return false;
    }

    /**
     * Returns the value of the attribute {@code charset}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code charset} or an empty string if that attribute isn't defined
     */
    public final String getCharsetAttribute() {
        return getAttributeDirect("charset");
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type} or an empty string if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        return getAttributeDirect(TYPE_ATTRIBUTE);
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name} or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttributeDirect(NAME_ATTRIBUTE);
    }

    /**
     * Returns the value of the attribute {@code href}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code href} or an empty string if that attribute isn't defined
     */
    @Override
    public final String getHrefAttribute() {
        return getAttributeDirect("href").trim();
    }

    /**
     * Returns the value of the attribute {@code hreflang}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code hreflang} or an empty string if that attribute isn't defined
     */
    public final String getHrefLangAttribute() {
        return getAttributeDirect("hreflang");
    }

    /**
     * Returns the value of the attribute {@code rel}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code rel} or an empty string if that attribute isn't defined
     */
    @Override
    public final String getRelAttribute() {
        return getAttributeDirect("rel");
    }

    /**
     * Returns the value of the attribute {@code rev}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code rev} or an empty string if that attribute isn't defined
     */
    public final String getRevAttribute() {
        return getAttributeDirect("rev");
    }

    /**
     * Returns the value of the attribute {@code accesskey}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code accesskey} or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttributeDirect("accesskey");
    }

    /**
     * Returns the value of the attribute {@code shape}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code shape} or an empty string if that attribute isn't defined
     */
    public final String getShapeAttribute() {
        return getAttributeDirect("shape");
    }

    /**
     * Returns the value of the attribute {@code coords}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code coords} or an empty string if that attribute isn't defined
     */
    public final String getCoordsAttribute() {
        return getAttributeDirect("coords");
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex} or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttributeDirect("tabindex");
    }

    /**
     * Returns the value of the attribute {@code onfocus}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onfocus} or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttributeDirect("onfocus");
    }

    /**
     * Returns the value of the attribute {@code onblur}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onblur} or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttributeDirect("onblur");
    }

    /**
     * Returns the value of the attribute {@code target}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code target} or an empty string if that attribute isn't defined
     */
    @Override
    public final String getTargetAttribute() {
        return getAttributeDirect("target");
    }

    /**
     * Open this link in a new window, much as web browsers do when you shift-click a link or use the context
     * menu to open in a new window.
     * <p>
     * It should be noted that even web browsers will sometimes not give the expected result when using this
     * method of following links. Links that have no real href and rely on JavaScript to do their work will
     * fail.
     * </p>
     *
     * @return the page opened by this link, nested in a new {@link org.htmlunit.TopLevelWindow}
     * @throws MalformedURLException if the href could not be converted to a valid URL
     */
    public final Page openLinkInNewWindow() throws MalformedURLException {
        final URL target = ((HtmlPage) getPage()).getFullyQualifiedUrl(getHrefAttribute());
        final String windowName = "HtmlAnchor.openLinkInNewWindow() target";
        final WebWindow newWindow = getPage().getWebClient().openWindow(target, windowName);
        return newWindow.getEnclosedPage();
    }

    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE;
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
     * Returns the value of the attribute {@code ping}.
     *
     * @return the value of the attribute {@code ping}
     */
    @Override
    public final String getPingAttribute() {
        return getAttributeDirect("ping");
    }

    /**
     * Returns the value of the attribute {@code download}.
     *
     * @return the value of the attribute {@code download}
     */
    @Override
    public final String getDownloadAttribute() {
        return getAttributeDirect("download");
    }
}
