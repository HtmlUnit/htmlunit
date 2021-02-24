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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.io.IOException;

import javax.imageio.ImageReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.AwtRenderingBackend;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.RenderingBackend;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.RenderingBackend.WindingRule;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code CanvasRenderingContext2D}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass
public class CanvasRenderingContext2D extends SimpleScriptable {

    private static final Log LOG = LogFactory.getLog(CanvasRenderingContext2D.class);

    private final HTMLCanvasElement canvas_;
    private RenderingBackend renderingBackend_;

    /**
     * Default constructor.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public CanvasRenderingContext2D() {
        canvas_ = null;
        renderingBackend_ = null;
    }

    /**
     * Constructs in association with {@link HTMLCanvasElement}.
     * @param canvas the {@link HTMLCanvasElement}
     */
    public CanvasRenderingContext2D(final HTMLCanvasElement canvas) {
        canvas_ = canvas;
        renderingBackend_ = null;
    }

    private RenderingBackend getRenderingBackend() {
        if (renderingBackend_ == null) {
            final int imageWidth = Math.max(1, canvas_.getWidth());
            final int imageHeight = Math.max(1, canvas_.getHeight());
            renderingBackend_ = new AwtRenderingBackend(imageWidth, imageHeight);
        }
        return renderingBackend_;
    }

    /**
     * Specifies the alpha (transparency) value that is applied to shapes and images
     * before they are drawn onto the canvas..
     * @return the {@code globalAlpha} property
     */
    @JsxGetter
    public double getGlobalAlpha() {
        return getRenderingBackend().getGlobalAlpha();
    }

    /**
     * Sets the {@code globalAlpha} property.
     * @param globalAlpha the {@code globalAlpha} property
     */
    @JsxSetter
    public void setGlobalAlpha(final double globalAlpha) {
        getRenderingBackend().setGlobalAlpha(globalAlpha);
    }

    /**
     * Returns the {@code fillStyle} property.
     * @return the {@code fillStyle} property
     */
    @JsxGetter
    public Object getFillStyle() {
        LOG.info("CanvasRenderingContext2D.getFillStyle() not yet implemented");
        return null;
    }

    /**
     * Sets the {@code fillStyle} property.
     * @param fillStyle the {@code fillStyle} property
     */
    @JsxSetter
    public void setFillStyle(final String fillStyle) {
        getRenderingBackend().setFillStyle(fillStyle);
    }

    /**
     * Returns the {@code strokeStyle} property.
     * @return the {@code strokeStyle} property
     */
    @JsxGetter
    public Object getStrokeStyle() {
        LOG.info("CanvasRenderingContext2D.getStrokeStyle() not yet implemented");
        return null;
    }

    /**
     * Sets the {@code strokeStyle} property.
     * @param strokeStyle the {@code strokeStyle} property
     */
    @JsxSetter
    public void setStrokeStyle(final String strokeStyle) {
        getRenderingBackend().setStrokeStyle(strokeStyle);
    }

    /**
     * Returns the {@code lineWidth} property.
     * @return the {@code lineWidth} property
     */
    @JsxGetter
    public double getLineWidth() {
        return getRenderingBackend().getLineWidth();
    }

    /**
     * Sets the {@code lineWidth} property.
     * @param lineWidth the {@code lineWidth} property
     */
    @JsxSetter
    public void setLineWidth(final Object lineWidth) {
        if (!Undefined.isUndefined(lineWidth)) {
            final double width = Context.toNumber(lineWidth);
            if (!Double.isNaN(width)) {
                getRenderingBackend().setLineWidth((int) width);
            }
        }
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
        getRenderingBackend().arc(x, y, radius, startAngle, endAngle, anticlockwise);
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
        LOG.info("CanvasRenderingContext2D.arcTo() not yet implemented");
    }

    /**
     * Begins the subpaths.
     */
    @JsxFunction
    public void beginPath() {
        getRenderingBackend().beginPath();
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
        getRenderingBackend().bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
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
        getRenderingBackend().clearRect(x, y, w, h);
    }

    /**
     * Creates a new clipping region.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     */
    @JsxFunction
    public static void clip(final Context context, final Scriptable thisObj, final Object[] args,
        final Function function) {
        if (!(thisObj instanceof CanvasRenderingContext2D)) {
            throw Context.reportRuntimeError(
                    "CanvasRenderingContext2D.getImageData() failed - this is not a CanvasRenderingContext2D");
        }
        final CanvasRenderingContext2D canvas = (CanvasRenderingContext2D) thisObj;

        RenderingBackend.WindingRule windingRule = WindingRule.NON_ZERO;
        if (args.length == 1) {
            final String windingRuleParam = ScriptRuntime.toString(args[0]);
            if ("evenodd".contentEquals(windingRuleParam)) {
                windingRule = WindingRule.EVEN_ODD;
            }
            canvas.getRenderingBackend().clip(windingRule, null);
        }

        if (args.length > 1) {
            if (!(args[0] instanceof Path2D)) {
                throw Context.reportRuntimeError(
                        "CanvasRenderingContext2D.clip() failed - the first parameter has to be a Path2D");
            }

            final String windingRuleParam = ScriptRuntime.toString(args[1]);
            if ("evenodd".contentEquals(windingRuleParam)) {
                windingRule = WindingRule.EVEN_ODD;
            }

            LOG.info("CanvasRenderingContext2D.clip(path, fillRule) not yet implemented");
            // canvas.getRenderingBackend().clip(windingRule, (Path2D) args[0]);
        }

        canvas.getRenderingBackend().clip(WindingRule.NON_ZERO, null);
    }

    /**
     * Closes the current subpath.
     */
    @JsxFunction
    public void closePath() {
        getRenderingBackend().closePath();
    }

    /**
     * Returns the {@code ImageData} object.
     * this may accept a variable number of arguments.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the {@code ImageData} object
     */
    @JsxFunction
    public static ImageData createImageData(final Context context, final Scriptable thisObj, final Object[] args,
        final Function function) {
        if (!(thisObj instanceof CanvasRenderingContext2D)) {
            throw Context.reportRuntimeError(
                    "CanvasRenderingContext2D.getImageData() failed - this is not a CanvasRenderingContext2D");
        }
        final CanvasRenderingContext2D canvas = (CanvasRenderingContext2D) thisObj;

        if (args.length > 0 && args[0] instanceof ImageData) {
            final ImageData imageDataParameter = (ImageData) args[0];
            final ImageData imageData = new ImageData(null,
                    0, 0, imageDataParameter.getWidth(), imageDataParameter.getHeight());
            imageData.setParentScope(canvas.getParentScope());
            imageData.setPrototype(canvas.getPrototype(imageData.getClass()));
            return imageData;
        }

        if (args.length > 1) {
            final int width = Math.abs((int) ScriptRuntime.toInteger(args, 0));
            final int height = Math.abs((int) ScriptRuntime.toInteger(args, 1));
            final ImageData imageData = new ImageData(null, 0, 0, width, height);
            imageData.setParentScope(canvas.getParentScope());
            imageData.setPrototype(canvas.getPrototype(imageData.getClass()));
            return imageData;
        }

        throw Context.reportRuntimeError(
                "CanvasRenderingContext2D.getImageData() failed - "
                + "wrong parameters given (" + StringUtils.join(args, ", ") + ")");
    }

    /**
     * Creates linear gradient.
     * @param x0 the x0
     * @param y0 the y0
     * @param r0 the r0
     * @param x1 the x1
     * @param y1 the y1
     * @param r1 the r1
     * @return the new CanvasGradient
     */
    @JsxFunction
    public CanvasGradient createLinearGradient(final double x0, final double y0, final double r0, final double x1,
            final Object y1, final Object r1) {
        final CanvasGradient canvasGradient = new CanvasGradient();
        canvasGradient.setParentScope(getParentScope());
        canvasGradient.setPrototype(getPrototype(canvasGradient.getClass()));
        return canvasGradient;
    }

    /**
     * Creates a pattern.
     */
    @JsxFunction
    public void createPattern() {
        LOG.info("CanvasRenderingContext2D.createPattern() not yet implemented");
    }

    /**
     * Creates a gradient.
     * @param x0 the x axis of the coordinate of the start circle
     * @param y0 the y axis of the coordinate of the start circle
     * @param r0 the radius of the start circle
     * @param x1 the x axis of the coordinate of the end circle
     * @param y1 the y axis of the coordinate of the end circle
     * @param r1 the radius of the end circle
     * @return the new CanvasGradient
     */
    @JsxFunction
    public CanvasGradient createRadialGradient(final double x0, final double y0,
                            final double r0, final double x1, final double y1, final double r1) {
        final CanvasGradient canvasGradient = new CanvasGradient();
        canvasGradient.setParentScope(getParentScope());
        canvasGradient.setPrototype(getPrototype(canvasGradient.getClass()));
        return canvasGradient;
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

        if (image instanceof HTMLImageElement) {
            final HTMLImageElement imageElem = (HTMLImageElement) image;
            try {
                final ImageReader imageReader = ((HtmlImage) imageElem.getDomNodeOrDie()).getImageReader();

                // 3 arguments
                //   void ctx.drawImage(image, dx, dy);
                if (Undefined.isUndefined(sWidth)) {
                    getRenderingBackend().drawImage(imageReader, 0, 0, null, null, sx, sy, null, null);
                }

                // 5 arguments
                //   void ctx.drawImage(image, dx, dy, dWidth, dHeight);
                else if (Undefined.isUndefined(dx)) {
                    final int dWidthI = ScriptRuntime.toInt32(sWidth);
                    final int dHeightI = ScriptRuntime.toInt32(sHeight);

                    getRenderingBackend().drawImage(imageReader, 0, 0, null, null, sx, sy, dWidthI, dHeightI);
                }

                // all 9 arguments
                //   void ctx.drawImage(image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);
                else {
                    final int sWidthI = ScriptRuntime.toInt32(sWidth);
                    final int sHeightI = ScriptRuntime.toInt32(sHeight);

                    final int dxI = ScriptRuntime.toInt32(dx);
                    final int dyI = ScriptRuntime.toInt32(dy);
                    final int dWidthI = ScriptRuntime.toInt32(dWidth);
                    final int dHeightI = ScriptRuntime.toInt32(dHeight);

                    getRenderingBackend().drawImage(imageReader,
                            sx, sy, sWidthI, sHeightI, dxI, dyI, dWidthI, dHeightI);
                }
            }
            catch (final IOException ioe) {
                LOG.info("There is no ImageReader available for you imgage with src '" + imageElem.getSrc() + "'"
                        + "Please have a look at ... for a possible solution.");
            }
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
                type = "image/png";
            }
            return "data:" + type + ";base64," + getRenderingBackend().encodeToString(type);
        }
        catch (final IOException ioe) {
            throw Context.throwAsScriptRuntimeEx(ioe);
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
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public void ellipse(final double x, final double y,
                    final double radiusX, final double radiusY,
                    final double rotation, final double startAngle, final double endAngle,
                    final boolean anticlockwise) {
        getRenderingBackend().ellipse(x, y, radiusX, radiusY, rotation, startAngle, endAngle, anticlockwise);
    }

    /**
     * Fills the shape.
     */
    @JsxFunction
    public void fill() {
        getRenderingBackend().fill();
    }

    /**
     * Paints the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    @JsxFunction
    public void fillRect(final int x, final int y, final int w, final int h) {
        getRenderingBackend().fillRect(x, y, w, h);
    }

    /**
     * Fills a given text at the given (x, y) position.
     * @param text the text
     * @param x the x
     * @param y the y
     */
    @JsxFunction
    public void fillText(final String text, final double x, final double y) {
        getRenderingBackend().fillText(text, x, y);
    }

    /**
     * Returns the {@code ImageData} object.
     * @param sx x
     * @param sy y
     * @param sw width
     * @param sh height
     * @return the {@code ImageData} object
     */
    @JsxFunction
    public ImageData getImageData(final int sx, final int sy, final int sw, final int sh) {
        final ImageData imageData = new ImageData(getRenderingBackend(), sx, sy, sw, sh);
        imageData.setParentScope(getParentScope());
        imageData.setPrototype(getPrototype(imageData.getClass()));
        return imageData;
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void getLineDash() {
        LOG.info("CanvasRenderingContext2D.getLineDash() not yet implemented");
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void getLineData() {
        LOG.info("CanvasRenderingContext2D.getLineData() not yet implemented");
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void isPointInPath() {
        LOG.info("CanvasRenderingContext2D.isPointInPath() not yet implemented");
    }

    /**
     * Connect the last point to the given point.
     * @param x the x
     * @param y the y
     */
    @JsxFunction
    public void lineTo(final double x, final double y) {
        getRenderingBackend().lineTo(x, y);
    }

    /**
     * Calculate TextMetrics for the given text.
     * @param text the text to measure
     * @return the text metrics
     */
    @JsxFunction
    public TextMetrics measureText(final Object text) {
        if (text == null || Undefined.isUndefined(text)) {
            throw Context.throwAsScriptRuntimeEx(
                    new RuntimeException("Missing argument for CanvasRenderingContext2D.measureText()."));
        }

        final String textValue = Context.toString(text);

        // TODO take font into account
        final int width = textValue.length() * getBrowserVersion().getPixesPerChar();

        final TextMetrics metrics = new TextMetrics(width);
        metrics.setParentScope(getParentScope());
        metrics.setPrototype(getPrototype(metrics.getClass()));
        return metrics;
    }

    /**
     * Creates a new subpath.
     * @param x the x
     * @param y the y
     */
    @JsxFunction
    public void moveTo(final double x, final double y) {
        getRenderingBackend().moveTo(x, y);
    }

    /**
     * Paints data from the given ImageData object onto the canvas.
     * @param imageData an ImageData object containing the array of pixel values
     * @param dx horizontal position (x coordinate) at which to place the image data in the destination canvas
     * @param dy vertical position (y coordinate) at which to place the image data in the destination canvas
     * @param dirtyX horizontal position (x coordinate) of the top-left corner
     *  from which the image data will be extracted. Defaults to 0.
     * @param dirtyY vertical position (y coordinate) of the top-left corner
     *  from which the image data will be extracted. Defaults to 0.
     * @param dirtyWidth width of the rectangle to be painted.
     *  Defaults to the width of the image data.
     * @param dirtyHeight height of the rectangle to be painted.
     *  Defaults to the height of the image data.
     */
    @JsxFunction
    public void putImageData(final ImageData imageData,
                final int dx, final int dy, final Object dirtyX, final Object dirtyY,
                final Object dirtyWidth, final Object dirtyHeight) {
        int dirtyXArg = 0;
        int dirtyYArg = 0;
        int dirtyWidthArg = imageData.getWidth();
        int dirtyHeightArg = imageData.getHeight();

        if (!Undefined.isUndefined(dirtyX)) {
            dirtyXArg = (int) ScriptRuntime.toInteger(dirtyX);

            if (Undefined.isUndefined(dirtyY)
                    || Undefined.isUndefined(dirtyWidth)
                    || Undefined.isUndefined(dirtyHeight)) {
                throw Context.reportRuntimeError(
                        "CanvasRenderingContext2D.putImageData() failed - seven parameters expected");
            }
            dirtyYArg = (int) ScriptRuntime.toInteger(dirtyY);
            dirtyWidthArg = (int) ScriptRuntime.toInteger(dirtyWidth);
            dirtyHeightArg = (int) ScriptRuntime.toInteger(dirtyHeight);
        }

        getRenderingBackend().putImageData(imageData, dx, dy, dirtyXArg, dirtyYArg, dirtyWidthArg, dirtyHeightArg);
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
        getRenderingBackend().quadraticCurveTo(controlPointX, controlPointY, endPointX, endPointY);
    }

    /**
     * Renders a rectangle.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    @JsxFunction
    public void rect(final double x, final double y, final double w, final double h) {
        getRenderingBackend().rect(x, y, w, h);
    }

    /**
     * Pops state stack and restore state.
     */
    @JsxFunction
    public void restore() {
        getRenderingBackend().restore();
    }

    /**
     * Adds a rotation to the transformation matrix.
     * @param angle the angle
     */
    @JsxFunction
    public void rotate(final double angle) {
        getRenderingBackend().rotate(angle);
    }

    /**
     * Pushes state on state stack.
     */
    @JsxFunction
    public void save() {
        getRenderingBackend().save();
    }

    /**
     * Changes the transformation matrix to apply a scaling transformation with the given characteristics.
     * @param x the scale factor in the horizontal direction
     * @param y the scale factor in the vertical direction
     */
    @JsxFunction
    public void scale(final Object x, final Object y) {
        LOG.info("CanvasRenderingContext2D.scale() not yet implemented");
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void setLineDash() {
        LOG.info("CanvasRenderingContext2D.setLineDash() not yet implemented");
    }

    /**
     * Resets (overrides) the current transformation to the identity matrix,
     * and then invokes a transformation described by the arguments of this method.
     * This lets you scale, rotate, translate (move), and skew the context.
     * @param m11 Horizontal scaling. A value of 1 results in no scaling
     * @param m12 Vertical skewing
     * @param m21 Horizontal skewing
     * @param m22 Vertical scaling. A value of 1 results in no scaling
     * @param dx Horizontal translation (moving)
     * @param dy Vertical translation (moving).
     */
    @JsxFunction
    public void setTransform(final double m11, final double m12,
                    final double m21, final double m22, final double dx, final double dy) {
        getRenderingBackend().setTransform(m11, m12, m21, m22, dx, dy);
    }

    /**
     * Calculates the strokes of all the subpaths of the current path.
     */
    @JsxFunction
    public void stroke() {
        getRenderingBackend().stroke();
    }

    /**
     * Strokes the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    @JsxFunction
    public void strokeRect(final int x, final int y, final int w, final int h) {
        getRenderingBackend().strokeRect(x, y, w, h);
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction
    public void strokeText() {
        LOG.info("CanvasRenderingContext2D.strokeText() not yet implemented");
    }

    /**
     * Multiplies the current transformation with the matrix described by the
     * arguments of this method. This lets you scale, rotate, translate (move),
     * and skew the context.
     * @param m11 Horizontal scaling. A value of 1 results in no scaling
     * @param m12 Vertical skewing
     * @param m21 Horizontal skewing
     * @param m22 Vertical scaling. A value of 1 results in no scaling
     * @param dx Horizontal translation (moving)
     * @param dy Vertical translation (moving).
     */
    @JsxFunction
    public void transform(final double m11, final double m12,
                    final double m21, final double m22, final double dx, final double dy) {
        getRenderingBackend().transform(m11, m12, m21, m22, dx, dy);
    }

    /**
     * Changes the transformation matrix to apply a translation transformation with the given characteristics.
     * @param x the translation distance in the horizontal direction
     * @param y the translation distance in the vertical direction
     */
    @JsxFunction
    public void translate(final int x, final int y) {
        getRenderingBackend().translate(x, y);
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
