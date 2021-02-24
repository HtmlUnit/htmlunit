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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DISPLAY_BLOCK;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;

/**
 * Wrapper for the HTML element "area".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class HtmlArea extends HtmlElement {
    private static final Log LOG = LogFactory.getLog(HtmlArea.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "area";

    private static final String SHAPE_RECT = "rect";
    private static final String SHAPE_CIRCLE = "circle";
    private static final String SHAPE_POLY = "poly";

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlArea(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey) throws IOException {
        final HtmlPage enclosingPage = (HtmlPage) getPage();
        final WebClient webClient = enclosingPage.getWebClient();

        final String href = getHrefAttribute().trim();
        if (!href.isEmpty()) {
            final HtmlPage page = (HtmlPage) getPage();
            if (StringUtils.startsWithIgnoreCase(href, JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
                page.executeJavaScript(
                    href, "javascript url", getStartLineNumber());
                return false;
            }
            final URL url;
            try {
                url = enclosingPage.getFullyQualifiedUrl(getHrefAttribute());
            }
            catch (final MalformedURLException e) {
                throw new IllegalStateException(
                        "Not a valid url: " + getHrefAttribute(), e);
            }
            final WebRequest request = new WebRequest(url);
            request.setCharset(page.getCharset());
            request.setRefererlHeader(page.getUrl());
            final WebWindow webWindow = enclosingPage.getEnclosingWindow();
            webClient.getPage(
                    webWindow,
                    enclosingPage.getResolvedTarget(getTargetAttribute()),
                    request);
        }
        return false;
    }

    /**
     * Returns the value of the attribute {@code shape}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code shape} or an empty string if that attribute isn't defined
     */
    public final String getShapeAttribute() {
        return getAttributeDirect("shape");
    }

    /**
     * Returns the value of the attribute {@code coords}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code coords} or an empty string if that attribute isn't defined
     */
    public final String getCoordsAttribute() {
        return getAttributeDirect("coords");
    }

    /**
     * Returns the value of the attribute {@code href}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code href} or an empty string if that attribute isn't defined
     */
    public final String getHrefAttribute() {
        return getAttributeDirect("href");
    }

    /**
     * Returns the value of the attribute {@code nohref}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code nohref} or an empty string if that attribute isn't defined
     */
    public final String getNoHrefAttribute() {
        return getAttributeDirect("nohref");
    }

    /**
     * Returns the value of the attribute {@code alt}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code alt} or an empty string if that attribute isn't defined
     */
    public final String getAltAttribute() {
        return getAttributeDirect("alt");
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex} or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttributeDirect("tabindex");
    }

    /**
     * Returns the value of the attribute {@code accesskey}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code accesskey} or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttributeDirect("accesskey");
    }

    /**
     * Returns the value of the attribute {@code onfocus}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onfocus} or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttributeDirect("onfocus");
    }

    /**
     * Returns the value of the attribute {@code onblur}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onblur} or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttributeDirect("onblur");
    }

    /**
     * Returns the value of the attribute {@code target}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code target} or an empty string if that attribute isn't defined
     */
    public final String getTargetAttribute() {
        return getAttributeDirect("target");
    }

    /**
     * Indicates if this area contains the specified point.
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return {@code true} if the point is contained in this area
     */
    boolean containsPoint(final int x, final int y) {
        final String shape = StringUtils.defaultIfEmpty(getShapeAttribute(), SHAPE_RECT).toLowerCase(Locale.ROOT);

        if ("default".equals(shape) && getCoordsAttribute() != null) {
            return true;
        }

        if (SHAPE_RECT.equals(shape) && getCoordsAttribute() != null) {
            final Rectangle2D rectangle = parseRect();
            return rectangle.contains(x, y);
        }

        if (SHAPE_CIRCLE.equals(shape) && getCoordsAttribute() != null) {
            final Ellipse2D ellipse = parseCircle();
            return ellipse.contains(x, y);
        }

        if (SHAPE_POLY.equals(shape) && getCoordsAttribute() != null) {
            final GeneralPath path = parsePoly();
            return path.contains(x, y);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        if (hasFeature(CSS_DISPLAY_BLOCK)) {
            return DisplayStyle.NONE;
        }
        return DisplayStyle.INLINE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDisplayed() {
        final DomNode parent = getParentNode();
        if (null != parent && parent instanceof HtmlMap && parent.isDisplayed()) {
            return !isEmpty();
        }
        return false;
    }

    private Rectangle2D parseRect() {
        // browsers seem to support comma and blank
        final String[] coords = StringUtils.split(getCoordsAttribute(), ", ");

        double leftX = 0;
        double topY = 0;
        double rightX = 0;
        double bottomY = 0;

        try {
            if (coords.length > 0) {
                leftX = Double.parseDouble(coords[0].trim());
            }
            if (coords.length > 1) {
                topY = Double.parseDouble(coords[1].trim());
            }
            if (coords.length > 2) {
                rightX = Double.parseDouble(coords[2].trim());
            }
            if (coords.length > 3) {
                bottomY = Double.parseDouble(coords[3].trim());
            }
        }
        catch (final NumberFormatException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Invalid rect coords '" + Arrays.toString(coords) + "'", e);
            }
        }

        final Rectangle2D rectangle = new Rectangle2D.Double(leftX, topY,
                rightX - leftX, bottomY - topY);
        return rectangle;
    }

    private Ellipse2D parseCircle() {
        // browsers seem to support comma and blank
        final String[] coords = StringUtils.split(getCoordsAttribute(), ", ");
        final String radiusString = coords[2].trim();

        final int radius;
        try {
            radius = Integer.parseInt(radiusString);
        }
        catch (final NumberFormatException nfe) {
            throw new NumberFormatException("Circle radius of " + radiusString + " is not yet implemented.");
        }

        final double centerX = Double.parseDouble(coords[0].trim());
        final double centerY = Double.parseDouble(coords[1].trim());
        final Ellipse2D ellipse = new Ellipse2D.Double(centerX - (double) radius / 2, centerY - (double) radius / 2,
                radius, radius);
        return ellipse;
    }

    private GeneralPath parsePoly() {
        // browsers seem to support comma and blank
        final String[] coords = StringUtils.split(getCoordsAttribute(), ", ");
        final GeneralPath path = new GeneralPath();

        try {
            for (int i = 0; i + 1 < coords.length; i += 2) {
                if (i == 0) {
                    path.moveTo(Float.parseFloat(coords[i]), Float.parseFloat(coords[i + 1]));
                }
                else {
                    path.lineTo(Float.parseFloat(coords[i]), Float.parseFloat(coords[i + 1]));
                }
            }
        }
        catch (final NumberFormatException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Invalid poly coords '" + Arrays.toString(coords) + "'", e);
            }
        }

        path.closePath();
        return path;
    }

    private boolean isEmpty() {
        final String shape = StringUtils.defaultIfEmpty(getShapeAttribute(), SHAPE_RECT).toLowerCase(Locale.ROOT);

        if ("default".equals(shape) && getCoordsAttribute() != null) {
            return false;
        }

        if (SHAPE_RECT.equals(shape) && getCoordsAttribute() != null) {
            final Rectangle2D rectangle = parseRect();
            return rectangle.isEmpty();
        }

        if (SHAPE_CIRCLE.equals(shape) && getCoordsAttribute() != null) {
            final Ellipse2D ellipse = parseCircle();
            return ellipse.isEmpty();
        }

        if (SHAPE_POLY.equals(shape) && getCoordsAttribute() != null) {
            return false;
        }

        return false;
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
}
