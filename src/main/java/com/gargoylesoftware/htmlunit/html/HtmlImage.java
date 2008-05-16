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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Wrapper for the HTML element "img".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author <a href="mailto:knut.johannes.dahle@gmail.com">Knut Johannes Dahle</a>
 */
public class HtmlImage extends ClickableElement {

    private static final long serialVersionUID = -2304247017681577696L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "img";
    private int lastClickX_;
    private int lastClickY_;
    private WebResponse imageWebResponse_;
    private ImageReader imageReader_;
    private boolean downloaded_;

    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlImage(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute "src". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "src" or an empty string if that attribute isn't defined
     */
    public final String getSrcAttribute() {
        return getAttributeValue("src");
    }

    /**
     * Returns the value of the attribute "alt". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "alt" or an empty string if that attribute isn't defined
     */
    public final String getAltAttribute() {
        return getAttributeValue("alt");
    }

    /**
     * Returns the value of the attribute "name". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "name" or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttributeValue("name");
    }

    /**
     * Returns the value of the attribute "longdesc". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "longdesc" or an empty string if that attribute isn't defined
     */
    public final String getLongDescAttribute() {
        return getAttributeValue("longdesc");
    }

    /**
     * Returns the value of the attribute "height". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "height" or an empty string if that attribute isn't defined
     */
    public final String getHeightAttribute() {
        return getAttributeValue("height");
    }

    /**
     * Returns the value of the attribute "width". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "width" or an empty string if that attribute isn't defined
     */
    public final String getWidthAttribute() {
        return getAttributeValue("width");
    }

    /**
     * Returns the value of the attribute "usemap". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "usemap" or an empty string if that attribute isn't defined
     */
    public final String getUseMapAttribute() {
        return getAttributeValue("usemap");
    }

    /**
     * Returns the value of the attribute "ismap". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "ismap" or an empty string if that attribute isn't defined
     */
    public final String getIsmapAttribute() {
        return getAttributeValue("ismap");
    }

    /**
     * Returns the value of the attribute "align". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "align" or an empty string if that attribute isn't defined
     */
    public final String getAlignAttribute() {
        return getAttributeValue("align");
    }

    /**
     * Returns the value of the attribute "border". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "border" or an empty string if that attribute isn't defined
     */
    public final String getBorderAttribute() {
        return getAttributeValue("border");
    }

    /**
     * Returns the value of the attribute "hspace". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "hspace" or an empty string if that attribute isn't defined
     */
    public final String getHspaceAttribute() {
        return getAttributeValue("hspace");
    }

    /**
     * Returns the value of the attribute "vspace". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "vspace" or an empty string if that attribute isn't defined
     */
    public final String getVspaceAttribute() {
        return getAttributeValue("vspace");
    }

    /**
     * <span style="color:red">POTENIAL PERFORMANCE KILLER - DOWNLOADS THE IMAGE - USE AT YOUR OWN RISK.</span><br/>
     * If the image is not already downloaded it triggers a download. Then it stores the image in the HtmlImage
     * object for later use.<br/>
     *
     * @return returns the real height of the image.
     * @throws IOException if an error occurs while downloading the image or reading it.
     */
    public int getHeight() throws IOException {
        readImageIfNeeded();
        return imageReader_.getHeight(0);
    }

    /**
     * <span style="color:red">POTENIAL PERFORMANCE KILLER - DOWNLOADS THE IMAGE - USE AT YOUR OWN RISK.</span><br/>
     * If the image is not already downloaded it triggers a download. Then it stores the image in the HtmlImage
     * object for later use.<br/>
     *
     * @return returns the real width of the image.
     * @throws IOException if an error occurs while downloading the image or reading it.
     */
    public int getWidth() throws IOException {
        readImageIfNeeded();
        return imageReader_.getWidth(0);
    }

    /**
     * <span style="color:red">POTENIAL PERFORMANCE KILLER - DOWNLOADS THE IMAGE - USE AT YOUR OWN RISK.</span><br/>
     * If the image is not already downloaded it triggers a download. Then it stores the image in the HtmlImage
     * object for later use.<br/>
     *
     * @return the ImageReader representing the image from the download stream.
     * @throws IOException if an error occurs while downloading the image and if its of a
     * unsupported content-type.
     */
    public ImageReader getImageReader() throws IOException {
        readImageIfNeeded();
        return imageReader_;
    }

    /**
     * <span style="color:red">POTENIAL PERFORMANCE KILLER - DOWNLOADS THE IMAGE - USE AT YOUR OWN RISK.</span><br/>
     * If the image is not already downloaded it triggers a download. Then it stores the image in the HtmlImage
     * object for later use.<br/>
     *
     * @param downloadIfNeeded indicates if a request should be performed this hasn't been done previously
     * @return <code>null</code> if no download should be performed and when this wasn't already done. The response
     * received when performing a request for the image referenced by this tag otherwise.
     * @throws IOException if an error occurs while downloading the image.
     */
    public WebResponse getWebResponse(final boolean downloadIfNeeded) throws IOException {
        if (downloadIfNeeded) {
            downloadImageIfNeeded();
        }
        return imageWebResponse_;
    }

    /**
     * <span style="color:red">POTENIAL PERFORMANCE KILLER - DOWNLOADS THE IMAGE - USE AT YOUR OWN RISK.</span><br/>
     * If the image is not already downloaded it triggers a download. Then it stores the image in the HtmlImage
     * object for later use.<br/>
     *
     * Download the image specified in the src attribute.
     *
     * @throws IOException if an error occurs while downloading the image or if the stream is of a
     * unsupported content-type
     */
    private void downloadImageIfNeeded() throws IOException {
        if (!downloaded_) {
            final HtmlPage page = getPage();
            final WebClient webclient = page.getWebClient();

            final URL url = page.getFullyQualifiedUrl(getSrcAttribute());
            imageWebResponse_ = webclient.loadWebResponse(new WebRequestSettings(url));
            downloaded_ = true;
        }
    }

    private void readImageIfNeeded() throws IOException {
        downloadImageIfNeeded();
        if (imageReader_ == null) {
            final ImageInputStream iis = ImageIO.createImageInputStream(imageWebResponse_.getContentAsStream());
            final Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                throw new IOException("No image detected in response");
            }
            imageReader_ = iter.next();
            imageReader_.setInput(iis);
        }
    }

    /**
     * Simulates clicking this element at the specified position. This only makes sense for
     * an image map (currently only server side), where the position matters. This method
     * returns the page contained by this image's window after the click, which may or may not
     * be the same as the original page, depending on JavaScript event handlers, etc.
     *
     * @param x the x position of the click
     * @param y the y position of the click
     * @return the page contained by this image's window after the click
     * @exception IOException if an IO error occurs
     */
    public Page click(final int x, final int y) throws IOException {
        lastClickX_ = x;
        lastClickY_ = y;
        return super.click();
    }

    /**
     * Simulates clicking this element at the position <tt>(0, 0)</tt>. This method returns
     * the page contained by this image's window after the click, which may or may not be the
     * same as the original page, depending on JavaScript event handlers, etc.
     *
     * @return the page contained by this image's window after the click
     * @exception IOException if an IO error occurs
     */
    @Override
    public Page click() throws IOException {
        return click(0, 0);
    }

    /**
     * Performs the click action on the enclosing A tag (if any).
     * @param defaultPage the default page to return if the action does not load a new page
     * @return the page that is currently loaded after execution of this method
     * @throws IOException if an IO error occurred
     */
    @Override
    protected Page doClickAction(final Page defaultPage) throws IOException {
        if (getUseMapAttribute() != ATTRIBUTE_NOT_DEFINED) {
            // remove initial '#'
            final String mapName = getUseMapAttribute().substring(1);
            final HtmlElement doc = getPage().getDocumentElement();
            final HtmlMap map = (HtmlMap) doc.getOneHtmlElementByAttribute("map", "name", mapName);
            for (final HtmlElement element : map.getChildElements()) {
                if (element instanceof HtmlArea) {
                    final HtmlArea area = (HtmlArea) element;
                    if (area.containsPoint(lastClickX_, lastClickY_)) {
                        return area.doClickAction(defaultPage);
                    }
                }
            }
            return super.doClickAction(defaultPage);
        }
        final HtmlAnchor anchor = (HtmlAnchor) getEnclosingElement("a");
        if (anchor == null) {
            return super.doClickAction(defaultPage);
        }
        if (getIsmapAttribute() != ATTRIBUTE_NOT_DEFINED) {
            final String suffix = "?" + lastClickX_ + "," + lastClickY_;
            return anchor.doClickAction(defaultPage, suffix);
        }
        return anchor.doClickAction(defaultPage);
    }

}
