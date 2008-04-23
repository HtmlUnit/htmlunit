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

import static com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection.JAVASCRIPT_PREFIX;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Wrapper for the HTML element "area".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlArea extends ClickableElement {

    private static final long serialVersionUID = 8933911141016200386L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "area";

    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlArea(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Page doClickAction(final Page defaultPage) throws IOException {
        final HtmlPage enclosingPage = getPage();
        final WebClient webClient = enclosingPage.getWebClient();

        final String href = getHrefAttribute();
        if (href != null && href.length() > 0) {
            final HtmlPage page = getPage();
            if (TextUtil.startsWithIgnoreCase(href, JAVASCRIPT_PREFIX)) {
                return page.executeJavaScriptIfPossible(
                    href, "javascript url", getStartLineNumber()).getNewPage();
            }
            final URL url;
            try {
                url = enclosingPage.getFullyQualifiedUrl(getHrefAttribute());
            }
            catch (final MalformedURLException e) {
                throw new IllegalStateException(
                        "Not a valid url: " + getHrefAttribute());
            }
            final WebRequestSettings settings = new WebRequestSettings(url);
            final WebWindow webWindow = enclosingPage.getEnclosingWindow();
            return webClient.getPage(
                    webWindow,
                    enclosingPage.getResolvedTarget(getTargetAttribute()),
                    settings);
        }
        return defaultPage;
    }

    /**
     * Returns the value of the attribute "shape". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "shape" or an empty string if that attribute isn't defined
     */
    public final String getShapeAttribute() {
        return getAttributeValue("shape");
    }

    /**
     * Returns the value of the attribute "coords". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "coords" or an empty string if that attribute isn't defined
     */
    public final String getCoordsAttribute() {
        return getAttributeValue("coords");
    }

    /**
     * Returns the value of the attribute "href". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "href" or an empty string if that attribute isn't defined
     */
    public final String getHrefAttribute() {
        return getAttributeValue("href");
    }

    /**
     * Returns the value of the attribute "nohref". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "nohref" or an empty string if that attribute isn't defined
     */
    public final String getNoHrefAttribute() {
        return getAttributeValue("nohref");
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
     * Returns the value of the attribute "tabindex". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "tabindex" or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue("tabindex");
    }

    /**
     * Returns the value of the attribute "accesskey". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accesskey" or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttributeValue("accesskey");
    }

    /**
     * Returns the value of the attribute "onfocus". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onfocus" or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttributeValue("onfocus");
    }

    /**
     * Returns the value of the attribute "onblur". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onblur" or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttributeValue("onblur");
    }

    /**
     * Returns the value of the attribute "target". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "target" or an empty string if that attribute isn't defined
     */
    public final String getTargetAttribute() {
        return getAttributeValue("target");
    }

    /**
     * Indicates if this area contains the specified point.
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return <code>true</code> if the point is contained in this area
     */
    boolean containsPoint(final int x, final int y) {
        final String shape = StringUtils.defaultIfEmpty(getShapeAttribute(), "rect").toLowerCase();

        if ("default".equals(shape) && getCoordsAttribute() != null) {
            return true;
        }
        else if ("rect".equals(shape) && getCoordsAttribute() != null) {
            final String[] coords = getCoordsAttribute().split(",");
            final double leftX = Double.parseDouble(coords[0].trim());
            final double topY = Double.parseDouble(coords[1].trim());
            final double rightX = Double.parseDouble(coords[2].trim());
            final double bottomY = Double.parseDouble(coords[3].trim());
            final Rectangle2D rectangle = new Rectangle2D.Double(leftX, topY,
                    rightX - leftX + 1, bottomY - topY + 1);
            if (rectangle.contains(x, y)) {
                return true;
            }
        }
        else if ("circle".equals(shape) && getCoordsAttribute() != null) {
            final String[] coords = getCoordsAttribute().split(",");
            final double centerX = Double.parseDouble(coords[0].trim());
            final double centerY = Double.parseDouble(coords[1].trim());
            final String radiusString = coords[2].trim();
            
            final int radius;
            try {
                radius = Integer.parseInt(radiusString);
            }
            catch (final NumberFormatException nfe) {
                throw new NumberFormatException("Circle radius of " + radiusString + " is not yet implemented.");
            }
            final Ellipse2D ellipse = new Ellipse2D.Double(centerX - radius / 2, centerY - radius / 2,
                    radius, radius);
            if (ellipse.contains(x, y)) {
                return true;
            }
        }
        else if ("poly".equals(shape) && getCoordsAttribute() != null) {
            final String[] coords = getCoordsAttribute().split(",");
            final GeneralPath path = new GeneralPath();
            for (int i = 0; i + 1 < coords.length; i += 2) {
                if (i == 0) {
                    path.moveTo(Float.parseFloat(coords[i]), Float.parseFloat(coords[i + 1]));
                }
                else {
                    path.lineTo(Float.parseFloat(coords[i]), Float.parseFloat(coords[i + 1]));
                }
            }
            path.closePath();
            if (path.contains(x, y)) {
                return true;
            }
        }
        
        return false;
    }
}
