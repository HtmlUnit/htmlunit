/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.apache.commons.codec.binary.Base64;

import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement;

/**
 * A JavaScript object for a CanvasRenderingContext2D.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
public class CanvasRenderingContext2D extends SimpleScriptable {

    private final HTMLCanvasElement canvas_;
    private final BufferedImage image_;
    private final Graphics2D graphics2D_;

    /**
     * Default constructor.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public CanvasRenderingContext2D() {
        canvas_ = null;
        image_ = null;
        graphics2D_ = null;
    }

    /**
     * Constructs in association with {@link HTMLCanvasElement}.
     * @param canvas the {@link HTMLCanvasElement}
     */
    public CanvasRenderingContext2D(final HTMLCanvasElement canvas) {
        canvas_ = canvas;
        image_ = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        graphics2D_ = image_.createGraphics();
    }

    /**
     * Returns the "fillStyle" property.
     * @return the "fillStyle" property
     */
    @JsxGetter
    public Object getFillStyle() {
        return null;
    }

    /**
     * Sets the "fillStyle" property.
     * @param fillStyle the "fillStyle" property
     */
    @JsxSetter
    public void setFillStyle(final Object fillStyle) {
        //empty
    }

    /**
     * Returns the "strokeStyle" property.
     * @return the "strokeStyle" property
     */
    @JsxGetter
    public Object getStrokeStyle() {
        return null;
    }

    /**
     * Sets the "strokeStyle" property.
     * @param strokeStyle the "strokeStyle" property
     */
    @JsxSetter
    public void setStrokeStyle(final Object strokeStyle) {
        //empty
    }

    /**
     * Returns the "lineWidth" property.
     * @return the "lineWidth" property
     */
    @JsxGetter
    public double getLineWidth() {
        return 0;
    }

    /**
     * Sets the "lineWidth" property.
     * @param lineWidth the "lineWidth" property
     */
    @JsxSetter
    public void setLineWidth(final Object lineWidth) {
        //empty
    }

    /**
     * Returns the "globalAlpha" property.
     * @return the "globalAlpha" property
     */
    @JsxGetter
    public double getGlobalAlpha() {
        return 0;
    }

    /**
     * Sets the "globalAlpha" property.
     * @param globalAlpha the "globalAlpha" property
     */
    @JsxSetter
    public void setGlobalAlpha(final Object globalAlpha) {
        //empty
    }

    /**
     * Draws an arc.
     * @param x the x
     * @param y the y
     * @param radius the radius
     * @param startAngle the start angle
     * @param endAngle the end angle
     * @param anticlockwise is anti-clockwise
     */
    @JsxFunction
    public void arc(final double x, final double y, final double radius, final double startAngle,
                final double endAngle, final boolean anticlockwise) {
        //empty
    }

    /**
     * Draws an arc.
     * @param x1 the x1
     * @param y1 the y1
     * @param x2 the x2
     * @param y2 the y2
     * @param radius the radius
     */
    @JsxFunction
    public void arcTo(final double x1, final double y1, final double x2, final double y2,
                final double radius) {
        //empty
    }

    /**
     * Begins the subpaths.
     */
    @JsxFunction
    public void beginPath() {
        //empty
    }

    /**
     * Draws a cubic Bézier curve.
     * @param cp1x the cp1x
     * @param cp1y the cp1y
     * @param cp2x the cp2x
     * @param cp2y the cp2y
     * @param x the x
     * @param y the y
     */
    @JsxFunction
    public void bezierCurveTo(final double cp1x, final double cp1y, final double cp2x, final double cp2y,
            final double x, final double y) {
        //empty
    }

    /**
     * Clears the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    @JsxFunction
    public void clearRect(final double x, final double y, final double w, final double h) {
        //empty
    }

    /**
     * Creates a new clipping region.
     */
    @JsxFunction
    public void clip() {
        //empty
    }

    /**
     * Closes the subpaths.
     */
    @JsxFunction
    public void closePath() {
        //empty
    }

    /**
     * Creates a new, blank ImageData object with the specified dimensions.
     */
    @JsxFunction
    public void createImageData() {
        //empty
    }

    /**
     * Creates linear gradient.
     * @param x0 the x0
     * @param y0 the y0
     * @param r0 the r0
     * @param x1 the x1
     * @param y1 the y1
     * @param r1 the r1
     */
    @JsxFunction
    public void createLinearGradient(final double x0, final double y0, final double r0, final double x1,
            final Object y1, final Object r1) {
        //empty
    }

    /**
     * Creates a pattern.
     */
    @JsxFunction
    public void createPattern() {
        //empty
    }

    /**
     * Creates a gradient.
     */
    @JsxFunction
    public void createRadialGradient() {
        //empty
    }

    /**
     * Draws images onto the canvas.
     *
     * @param image an element to draw into the context
     * @param sx the X coordinate of the top left corner of the sub-rectangle of the source image
     *        to draw into the destination context
     * @param sy the Y coordinate of the top left corner of the sub-rectangle of the source image
     *        to draw into the destination context
     * @param sWidth the width of the sub-rectangle of the source image to draw into the destination context
     * @param sHeight the height of the sub-rectangle of the source image to draw into the destination context
     * @param dx the X coordinate in the destination canvas at which to place the top-left corner of the source image
     * @param dy the Y coordinate in the destination canvas at which to place the top-left corner of the source image
     * @param dWidth the width to draw the image in the destination canvas. This allows scaling of the drawn image
     * @param dHeight the height to draw the image in the destination canvas. This allows scaling of the drawn image
     */
    @JsxFunction
    @SuppressWarnings("unused")
    public void drawImage(final Object image, final int sx, final int sy, final Object sWidth, final Object sHeight,
            final Object dx, final Object dy, final Object dWidth, final Object dHeight) {
        Integer dxI;
        Integer dyI;
        Integer dWidthI = null;
        Integer dHeightI = null;
        Integer sWidthI = null;
        Integer sHeightI = null;
        if (dx != Undefined.instance) {
            dxI = ((Number) dx).intValue();
            dyI = ((Number) dy).intValue();
            dWidthI = ((Number) dWidth).intValue();
            dHeightI = ((Number) dHeight).intValue();
        }
        else {
            dxI = sx;
            dyI = sy;
        }
        if (sWidth != Undefined.instance) {
            sWidthI = ((Number) sWidth).intValue();
            sHeightI = ((Number) sHeight).intValue();
        }

        try {
            if (image instanceof HTMLImageElement) {
                final ImageReader imageReader =
                        ((HtmlImage) ((HTMLImageElement) image).getDomNodeOrDie()).getImageReader();
                if (imageReader.getNumImages(true) != 0) {
                    final BufferedImage img = imageReader.read(0);
                    graphics2D_.drawImage(img, dxI, dyI, null);
                }
            }
        }
        catch (final IOException ioe) {
            throw Context.throwAsScriptRuntimeEx(ioe);
        }
    }

    /**
     * Returns the Data URL.
     *
     * @param type an optional type
     * @return the dataURL
     */
    public String toDataURL(String type) {
        try {
            if (type == null) {
                type = "png";
            }
            return "data:" + type + ";base64," + encodeToString(image_, type);
        }
        catch (final IOException ioe) {
            throw Context.throwAsScriptRuntimeEx(ioe);
        }
    }

    private static String encodeToString(final BufferedImage image, final String type) throws IOException {
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(image, type, bos);

            final byte[] imageBytes = bos.toByteArray();

            return new String(new Base64().encode(imageBytes));
        }
    }

    /**
     * Paints the specified ellipse.
     * @param x the x
     * @param y the y
     * @param radiusX the radiusX
     * @param radiusY the radiusY
     * @param rotation the rotation
     * @param startAngle the startAngle
     * @param endAngle the endAngle
     * @param anticlockwise the anticlockwise
     */
    @JsxFunction(@WebBrowser(CHROME))
    public void ellipse(final double x, final double y,
                    final double radiusX, final double radiusY,
                    final double rotation, final double startAngle, final double endAngle,
                    final boolean anticlockwise) {
        //empty
    }

    /**
     * Fills the shape.
     */
    @JsxFunction
    public void fill() {
        //empty
    }

    /**
     * Paints the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    @JsxFunction
    public void fillRect(final double x, final double y, final double w, final double h) {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void fillText() {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void getImageData() {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction({ @WebBrowser(IE), @WebBrowser(value = FF, minVersion = 31), @WebBrowser(CHROME) })
    public void getLineDash() {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void getLineData() {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void isPointInPath() {
        //empty
    }

    /**
     * Connect the last point to the given point.
     * @param x the x
     * @param y the y
     */
    @JsxFunction
    public void lineTo(final double x, final double y) {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void measureText() {
        //empty
    }

    /**
     * Creates a new subpath.
     * @param x the x
     * @param y the y
     */
    @JsxFunction
    public void moveTo(final double x, final double y) {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void putImageData() {
        //empty
    }

    /**
     * Draws a quadratic Bézier curve.
     * @param controlPointX the x-coordinate of the control point
     * @param controlPointY the y-coordinate of the control point
     * @param endPointX the x-coordinate of the end point
     * @param endPointY the y-coordinate of the end point
     */
    @JsxFunction
    public void quadraticCurveTo(final double controlPointX, final double controlPointY,
            final double endPointX, final double endPointY) {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void rect() {
        //empty
    }

    /**
     * Pops state stack and restore state.
     */
    @JsxFunction
    public void restore() {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void rotate() {
        //empty
    }

    /**
     * Pushes state on state stack.
     */
    @JsxFunction
    public void save() {
        //empty
    }

    /**
     * Changes the transformation matrix to apply a scaling transformation with the given characteristics.
     * @param x the scale factor in the horizontal direction
     * @param y the scale factor in the vertical direction
     */
    @JsxFunction
    public void scale(final Object x, final Object y) {
      //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction({ @WebBrowser(IE), @WebBrowser(value = FF, minVersion = 31), @WebBrowser(CHROME) })
    public void setLineDash() {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void setTransform() {
        //empty
    }

    /**
     * Calculates the strokes of all the subpaths of the current path.
     */
    @JsxFunction
    public void stroke() {
        //empty
    }

    /**
     * Strokes the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    @JsxFunction
    public void strokeRect(final double x, final double y, final double w, final double h) {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void strokeText() {
        //empty
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void transform() {
        //empty
    }

    /**
     * Changes the transformation matrix to apply a translation transformation with the given characteristics.
     * @param x the translation distance in the horizontal direction
     * @param y the translation distance in the vertical direction
     */
    @JsxFunction
    public void translate(final Object x, final Object y) {
      // empty
    }

    /**
     * Returns the associated {@link HTMLCanvasElement}.
     * @return the associated {@link HTMLCanvasElement}
     */
    @JsxGetter
    public HTMLCanvasElement getCanvas() {
        return canvas_;
    }
}
