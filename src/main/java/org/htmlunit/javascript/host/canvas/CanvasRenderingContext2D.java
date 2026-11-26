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
package org.htmlunit.javascript.host.canvas;

import java.io.IOException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.html.HTMLCanvasElement;
import org.htmlunit.javascript.host.html.HTMLImageElement;
import org.htmlunit.platform.Platform;
import org.htmlunit.platform.canvas.rendering.RecordedPath;
import org.htmlunit.platform.canvas.rendering.RenderingBackend;
import org.htmlunit.platform.canvas.rendering.RenderingBackend.LineCap;
import org.htmlunit.platform.canvas.rendering.RenderingBackend.LineJoin;
import org.htmlunit.platform.canvas.rendering.RenderingBackend.TextAlign;
import org.htmlunit.platform.canvas.rendering.RenderingBackend.TextBaseline;
import org.htmlunit.platform.canvas.rendering.RenderingBackend.WindingRule;
import org.htmlunit.protocol.data.DataURLConnection;
import org.htmlunit.util.MimeType;

/**
 * A JavaScript object for {@code CanvasRenderingContext2D}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D">MDN Documentation</a>
 */
@JsxClass
public class CanvasRenderingContext2D extends HtmlUnitScriptable {

    private static final Log LOG = LogFactory.getLog(CanvasRenderingContext2D.class);
    private static final String TYPE = "CanvasRenderingContext2D";

    private HTMLCanvasElement canvas_;
    private RenderingBackend renderingBackend_;

    /**
     * Default constructor.
     */
    public CanvasRenderingContext2D() {
        super();
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Constructs in association with {@link HTMLCanvasElement}.
     * @param canvas the {@link HTMLCanvasElement}
     */
    public CanvasRenderingContext2D(final HTMLCanvasElement canvas) {
        super();
        canvas_ = canvas;
        renderingBackend_ = null;
    }

    /**
     * Discards the current bitmap (and the drawing state); the next drawing operation
     * creates a new, empty bitmap using the current size of the canvas.
     * Called if the size of the canvas changes.
     */
    public void resetBitmap() {
        renderingBackend_ = null;
    }

    private RenderingBackend getRenderingBackend() {
        if (renderingBackend_ == null) {
            final int imageWidth = Math.max(1, canvas_.getWidth());
            final int imageHeight = Math.max(1, canvas_.getHeight());

            renderingBackend_ = Platform.getRenderingBackend(imageWidth, imageHeight);
        }
        return renderingBackend_;
    }

    /**
     * Specifies the alpha (transparency) value that is applied to shapes and images
     * before they are drawn onto the canvas.
     * @return the {@code globalAlpha} property
     */
    @JsxGetter
    public double getGlobalAlpha() {
        return getRenderingBackend().getGlobalAlpha();
    }

    /**
     * Sets the {@code globalAlpha} property.
     * @param globalAlpha the {@code globalAlpha} property value
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
    public HtmlUnitScriptable getFillStyle() {
        LOG.info("CanvasRenderingContext2D.getFillStyle() not yet implemented");
        return null;
    }

    /**
     * Sets the {@code fillStyle} property.
     * @param fillStyle the {@code fillStyle} property value
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
    public HtmlUnitScriptable getStrokeStyle() {
        LOG.info("CanvasRenderingContext2D.getStrokeStyle() not yet implemented");
        return null;
    }

    /**
     * Sets the {@code strokeStyle} property.
     * @param strokeStyle the {@code strokeStyle} property value
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
     * @param lineWidth the {@code lineWidth} property value
     */
    @JsxSetter
    public void setLineWidth(final Object lineWidth) {
        if (!JavaScriptEngine.isUndefined(lineWidth)) {
            final double width = JavaScriptEngine.toNumber(lineWidth);
            if (!Double.isNaN(width)) {
                getRenderingBackend().setLineWidth((float) width);
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
        if (!Path2D.areFinite(x, y, radius, startAngle, endAngle)) {
            return;
        }
        Path2D.checkRadius(this, "arc", TYPE, radius);
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
        if (!Path2D.areFinite(x1, y1, x2, y2, radius)) {
            return;
        }
        Path2D.checkRadius(this, "arcTo", TYPE, radius);
        getRenderingBackend().arcTo(x1, y1, x2, y2, radius);
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
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     */
    @JsxFunction
    public static void clip(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof CanvasRenderingContext2D canvas)) {
            throw JavaScriptEngine.reportRuntimeError(
                    "CanvasRenderingContext2D.clip() failed - this is not a CanvasRenderingContext2D");
        }

        // clip(optional CanvasFillRule fillRule) or clip(Path2D path, optional CanvasFillRule fillRule)
        final Path2D path = pathArgument(args, 2, "clip");
        final WindingRule windingRule = windingRule(args, path == null ? 0 : 1, "clip");
        canvas.getRenderingBackend().clip(windingRule, path == null ? null : path.getRecordedPath());
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
     * This may accept a variable number of arguments.
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the {@code ImageData} object
     */
    @JsxFunction
    public static ImageData createImageData(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof CanvasRenderingContext2D canvas)) {
            throw JavaScriptEngine.reportRuntimeError(
                    "CanvasRenderingContext2D.createImageData() failed - this is not a CanvasRenderingContext2D");
        }

        if (args.length > 0 && args[0] instanceof ImageData imageDataParameter) {
            final ImageData imageData = new ImageData(null,
                    0, 0, imageDataParameter.getWidth(), imageDataParameter.getHeight());
            imageData.setParentScope(scope);
            imageData.setPrototype(canvas.getPrototype(imageData.getClass()));
            return imageData;
        }

        if (args.length > 1) {
            final int width = Math.abs((int) JavaScriptEngine.toInteger(args, 0));
            final int height = Math.abs((int) JavaScriptEngine.toInteger(args, 1));
            final ImageData imageData = new ImageData(null, 0, 0, width, height);
            imageData.setParentScope(canvas.getParentScope());
            imageData.setPrototype(canvas.getPrototype(imageData.getClass()));
            return imageData;
        }

        throw JavaScriptEngine.reportRuntimeError(
                "CanvasRenderingContext2D.createImageData() failed - "
                + "wrong parameters given (" + StringUtils.join(args, ", ") + ")");
    }

    /**
     * Creates linear gradient.
     * @param x0 the x0
     * @param y0 the y0
     * @param x1 the x1
     * @param y1 the y1
     * @return the new CanvasGradient
     */
    @JsxFunction
    public CanvasGradient createLinearGradient(final double x0, final double y0, final double x1,
            final Object y1) {
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

        if (image instanceof HTMLCanvasElement canvasElem && !canvasElem.isOriginClean()) {
            // drawing a tainted canvas taints this canvas
            canvas_.setOriginCleanFalse();
        }

        if (image instanceof HTMLImageElement imageElem) {
            try {
                final HtmlImage htmlImage = (HtmlImage) imageElem.getDomNodeOrDie();
                if (htmlImage.isBroken()) {
                    throw JavaScriptEngine.asJavaScriptException(this,
                            "Failed to execute 'drawImage' on 'CanvasRenderingContext2D': "
                                    + "The HTMLImageElement provided is in the 'broken' state.",
                            DOMException.INVALID_STATE_ERR);
                }
                final org.htmlunit.platform.image.ImageData imageData = htmlImage.getImageData();

                // image data of another origin taints the canvas
                if (htmlImage.isCorsCrossOrigin()) {
                    canvas_.setOriginCleanFalse();
                }

                // 3 arguments
                //   void ctx.drawImage(image, dx, dy);
                if (JavaScriptEngine.isUndefined(sWidth)) {
                    getRenderingBackend().drawImage(imageData, 0, 0, null, null, sx, sy, null, null);
                }

                // 5 arguments
                //   void ctx.drawImage(image, dx, dy, dWidth, dHeight);
                else if (JavaScriptEngine.isUndefined(dx)) {
                    final int dWidthI = JavaScriptEngine.toInt32(sWidth);
                    final int dHeightI = JavaScriptEngine.toInt32(sHeight);

                    getRenderingBackend().drawImage(imageData, 0, 0, null, null, sx, sy, dWidthI, dHeightI);
                }

                // all 9 arguments
                //   void ctx.drawImage(image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);
                else {
                    final int sWidthI = JavaScriptEngine.toInt32(sWidth);
                    final int sHeightI = JavaScriptEngine.toInt32(sHeight);

                    final int dxI = JavaScriptEngine.toInt32(dx);
                    final int dyI = JavaScriptEngine.toInt32(dy);
                    final int dWidthI = JavaScriptEngine.toInt32(dWidth);
                    final int dHeightI = JavaScriptEngine.toInt32(dHeight);

                    getRenderingBackend().drawImage(imageData,
                            sx, sy, sWidthI, sHeightI, dxI, dyI, dWidthI, dHeightI);
                }
            }
            catch (final IOException ex) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("There is no ImageReader available for your image with src '" + imageElem.getSrc() + "'. "
                            + "Please have a look at https://www.htmlunit.org/images-howto.html "
                            + "for a possible solution.");
                }
            }
        }
    }

    /**
     * Returns the Data URL.
     *
     * @param type an optional type
     * @return the dataURL
     */
    public String toDataURL(final String type) {
        try {
            final String mimeType = type == null ? MimeType.IMAGE_PNG : type;
            return DataURLConnection.DATA_PREFIX + mimeType + ";base64,"
                    + getRenderingBackend().encodeToString(mimeType);
        }
        catch (final IOException ex) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(ex);
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
    @JsxFunction
    public void ellipse(final double x, final double y,
                    final double radiusX, final double radiusY,
                    final double rotation, final double startAngle, final double endAngle,
                    final boolean anticlockwise) {
        if (!Path2D.areFinite(x, y, radiusX, radiusY, rotation, startAngle, endAngle)) {
            return;
        }
        Path2D.checkRadius(this, "ellipse", TYPE, radiusX, "major-axis");
        Path2D.checkRadius(this, "ellipse", TYPE, radiusY, "minor-axis");
        getRenderingBackend().ellipse(x, y, radiusX, radiusY, rotation, startAngle, endAngle, anticlockwise);
    }

    /**
     * Fills the shape.
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction
    public static void fill(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof CanvasRenderingContext2D renderingCtx)) {
            throw JavaScriptEngine.reportRuntimeError(
                    "CanvasRenderingContext2D.fill() failed - this is not a CanvasRenderingContext2D");
        }

        // fill(optional CanvasFillRule fillRule) or fill(Path2D path, optional CanvasFillRule fillRule)
        final Path2D path = pathArgument(args, 2, "fill");
        final int ruleIndex = path == null ? 0 : 1;
        final WindingRule windingRule = windingRule(args, ruleIndex, "fill");

        if (path == null) {
            renderingCtx.getRenderingBackend().fill(windingRule);
        }
        else {
            renderingCtx.getRenderingBackend().fill(windingRule, path.getRecordedPath());
        }
    }

    /**
     * Returns the Path2D argument of a method with an optional Path2D as first parameter.
     * @param args the arguments
     * @param minArgsForPath if there are at least this number of arguments, the first one has to be a path
     * @param method the method name for the error message
     * @return the path or {@code null} if the first argument is not a path
     */
    private static Path2D pathArgument(final Object[] args, final int minArgsForPath, final String method) {
        if (args.length > 0 && args[0] instanceof Path2D path) {
            return path;
        }
        if (args.length >= minArgsForPath) {
            throw JavaScriptEngine.typeError("Failed to execute '" + method
                    + "' on 'CanvasRenderingContext2D': parameter 1 is not of type 'Path2D'.");
        }
        return null;
    }

    /**
     * Returns the CanvasFillRule argument.
     * @param args the arguments
     * @param index the index of the fill rule argument
     * @param method the method name for the error message
     * @return the winding rule (default is non zero)
     */
    private static WindingRule windingRule(final Object[] args, final int index, final String method) {
        if (args.length <= index || JavaScriptEngine.isUndefined(args[index])) {
            return WindingRule.NON_ZERO;
        }

        final String rule = JavaScriptEngine.toString(args[index]);
        if ("nonzero".equals(rule)) {
            return WindingRule.NON_ZERO;
        }
        if ("evenodd".equals(rule)) {
            return WindingRule.EVEN_ODD;
        }
        throw JavaScriptEngine.typeError("Failed to execute '" + method + "' on 'CanvasRenderingContext2D': "
                + "The provided value '" + rule + "' is not a valid enum value of type CanvasFillRule.");
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
        if (!canvas_.isOriginClean()) {
            throw JavaScriptEngine.asJavaScriptException(this,
                    "Failed to execute 'getImageData' on 'CanvasRenderingContext2D': "
                            + "The canvas has been tainted by cross-origin data.",
                    DOMException.SECURITY_ERR);
        }

        final ImageData imageData = new ImageData(getRenderingBackend(), sx, sy, sw, sh);
        imageData.setParentScope(getParentScope());
        imageData.setPrototype(getPrototype(imageData.getClass()));
        return imageData;
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction(functionName = "getLineDash")
    public void lineDash() {
        LOG.info("CanvasRenderingContext2D.getLineDash() not yet implemented");
    }

    /**
     * Dummy placeholder.
     */
    @JsxFunction(functionName = "getLineData")
    public void lineData() {
        LOG.info("CanvasRenderingContext2D.getLineData() not yet implemented");
    }

    /**
     * Checks whether the given point is inside the current or the given path.
     * Supported signatures are {@code isPointInPath(x, y, optional fillRule)}
     * and {@code isPointInPath(path, x, y, optional fillRule)}.
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return whether the point is inside the path
     */
    @JsxFunction
    public static boolean isPointInPath(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof CanvasRenderingContext2D canvas)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }

        final Path2D path = pathArgument(args, 4, "isPointInPath");
        final int xIndex = path == null ? 0 : 1;
        if (args.length < xIndex + 2) {
            throw JavaScriptEngine.typeError("Failed to execute 'isPointInPath' on 'CanvasRenderingContext2D': "
                    + (xIndex + 2) + " arguments required, but only " + args.length + " present.");
        }

        final double x = JavaScriptEngine.toNumber(args[xIndex]);
        final double y = JavaScriptEngine.toNumber(args[xIndex + 1]);
        final WindingRule windingRule = windingRule(args, xIndex + 2, "isPointInPath");
        return canvas.getRenderingBackend().isPointInPath(
                path == null ? null : path.getRecordedPath(), x, y, windingRule);
    }

    /**
     * Checks whether the given point is inside the area covered by the stroke of the current or the given path.
     * Supported signatures are {@code isPointInStroke(x, y)} and {@code isPointInStroke(path, x, y)}.
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return whether the point is inside the stroke
     */
    @JsxFunction
    public static boolean isPointInStroke(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof CanvasRenderingContext2D canvas)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }

        final Path2D path = pathArgument(args, 3, "isPointInStroke");
        final int xIndex = path == null ? 0 : 1;
        if (args.length < xIndex + 2) {
            throw JavaScriptEngine.typeError("Failed to execute 'isPointInStroke' on 'CanvasRenderingContext2D': "
                    + (xIndex + 2) + " arguments required, but only " + args.length + " present.");
        }

        final double x = JavaScriptEngine.toNumber(args[xIndex]);
        final double y = JavaScriptEngine.toNumber(args[xIndex + 1]);
        return canvas.getRenderingBackend().isPointInStroke(path == null ? null : path.getRecordedPath(), x, y);
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
        if (text == null || JavaScriptEngine.isUndefined(text)) {
            throw JavaScriptEngine.typeError("Missing argument for CanvasRenderingContext2D.measureText().");
        }

        final String textValue = JavaScriptEngine.toString(text);

        // TODO take font into account
        final int width = textValue.length() * getBrowserVersion().getPixelsPerChar();

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
     *        from which the image data will be extracted. Defaults to 0.
     * @param dirtyY vertical position (y coordinate) of the top-left corner
     *        from which the image data will be extracted. Defaults to 0.
     * @param dirtyWidth width of the rectangle to be painted.
     *        Defaults to the width of the image data.
     * @param dirtyHeight height of the rectangle to be painted.
     *        Defaults to the height of the image data.
     */
    @JsxFunction
    public void putImageData(final ImageData imageData,
                final int dx, final int dy, final Object dirtyX, final Object dirtyY,
                final Object dirtyWidth, final Object dirtyHeight) {
        int dirtyXArg = 0;
        int dirtyYArg = 0;
        int dirtyWidthArg = imageData.getWidth();
        int dirtyHeightArg = imageData.getHeight();

        if (!JavaScriptEngine.isUndefined(dirtyX)) {
            dirtyXArg = (int) JavaScriptEngine.toInteger(dirtyX);

            if (JavaScriptEngine.isUndefined(dirtyY)
                    || JavaScriptEngine.isUndefined(dirtyWidth)
                    || JavaScriptEngine.isUndefined(dirtyHeight)) {
                throw JavaScriptEngine.reportRuntimeError(
                        "CanvasRenderingContext2D.putImageData() failed - seven parameters expected");
            }
            dirtyYArg = (int) JavaScriptEngine.toInteger(dirtyY);
            dirtyWidthArg = (int) JavaScriptEngine.toInteger(dirtyWidth);
            dirtyHeightArg = (int) JavaScriptEngine.toInteger(dirtyHeight);
        }

        getRenderingBackend().putImageData(
                imageData.getData().getBuffer().getByteBuffer(), imageData.getWidth(), imageData.getHeight(),
                dx, dy, dirtyXArg, dirtyYArg, dirtyWidthArg, dirtyHeightArg);
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
     * Adds a rectangle with rounded corners to the current path.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     * @param radii a radius, a DOMPointInit or an array of up to four of them
     */
    @JsxFunction
    public void roundRect(final double x, final double y, final double w, final double h, final Object radii) {
        final RecordedPath path = new RecordedPath();
        Path2D.roundRect(path, TYPE, x, y, w, h, radii);
        path.replay(getRenderingBackend());
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
    public void scale(final double x, final double y) {
        if (!Path2D.areFinite(x, y)) {
            return;
        }
        getRenderingBackend().transform(x, 0, 0, y, 0, 0);
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
     * Calculates the strokes of all the subpaths of the current path or of the given path.
     * @param path the path to stroke or undefined to stroke the current path
     */
    @JsxFunction
    public void stroke(final Object path) {
        if (JavaScriptEngine.isUndefined(path)) {
            getRenderingBackend().stroke();
            return;
        }

        if (!(path instanceof Path2D path2d)) {
            throw JavaScriptEngine.typeError("Failed to execute 'stroke' on 'CanvasRenderingContext2D': "
                    + "parameter 1 is not of type 'Path2D'.");
        }
        getRenderingBackend().stroke(path2d.getRecordedPath());
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
    public void translate(final double x, final double y) {
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

    /**
     * Returns the the shape used to join two line segments where they meet.
     * There are three possible values for this property: "round", "bevel",
     * and "miter". The default is "miter".
     *
     * @return the the shape used to join two line segments
     */
    @JsxGetter
    public String getLineJoin() {
        switch (getRenderingBackend().getLineJoin()) {
            case ROUND:
                return "round";
            case BEVEL:
                return "bevel";
            default:
                return "miter";
        }
    }

    /**
     * Sets the {@code lineJoin} property.
     * @param lineJoin the {@code lineJoin} property value
     */
    @JsxSetter
    public void setLineJoin(final String lineJoin) {
        switch (lineJoin) {
            case "round":
                getRenderingBackend().setLineJoin(LineJoin.ROUND);
                break;
            case "bevel":
                getRenderingBackend().setLineJoin(LineJoin.BEVEL);
                break;
            case "miter":
                getRenderingBackend().setLineJoin(LineJoin.MITER);
                break;
            default:
                // ignore invalid values per spec
        }
    }

    /**
     * Returns the the shape used to join two line segments where they meet.
     * There are three possible values for this property: "round", "bevel",
     * and "miter". The default is "miter".
     *
     * @return the the shape used to join two line segments
     */
    @JsxGetter
    public String getLineCap() {
        switch (getRenderingBackend().getLineCap()) {
            case BUTT:
                return "butt";
            case ROUND:
                return "round";
            default:
                return "square";
        }
    }

    /**
     * Sets the {@code lineCap} property.
     * @param lineCap the {@code lineCap} property value
     */
    @JsxSetter
    public void setLineCap(final String lineCap) {
        switch (lineCap) {
            case "butt":
                getRenderingBackend().setLineCap(LineCap.BUTT);
                break;
            case "round":
                getRenderingBackend().setLineCap(LineCap.ROUND);
                break;
            case "square":
                getRenderingBackend().setLineCap(LineCap.SQUARE);
                break;
            default:
                // ignore invalid values per spec
        }
    }

    /**
     * Returns the text alignment used when drawing text. The possible values are
     * "start", "end", "left", "right" and "center". The default is "start".
     *
     * @return the text alignment
     */
    @JsxGetter
    public String getTextAlign() {
        return getRenderingBackend().getTextAlign().name().toLowerCase(Locale.ROOT);
    }

    /**
     * Sets the {@code textAlign} property; invalid values are ignored.
     * @param textAlign the {@code textAlign} property value
     */
    @JsxSetter
    public void setTextAlign(final String textAlign) {
        switch (textAlign) {
            case "start":
                getRenderingBackend().setTextAlign(TextAlign.START);
                break;
            case "end":
                getRenderingBackend().setTextAlign(TextAlign.END);
                break;
            case "left":
                getRenderingBackend().setTextAlign(TextAlign.LEFT);
                break;
            case "right":
                getRenderingBackend().setTextAlign(TextAlign.RIGHT);
                break;
            case "center":
                getRenderingBackend().setTextAlign(TextAlign.CENTER);
                break;
            default:
                // ignore invalid values per spec
        }
    }

    /**
     * Returns the text baseline used when drawing text. The possible values are
     * "top", "hanging", "middle", "alphabetic", "ideographic" and "bottom".
     * The default is "alphabetic".
     *
     * @return the text baseline
     */
    @JsxGetter
    public String getTextBaseline() {
        return getRenderingBackend().getTextBaseline().name().toLowerCase(Locale.ROOT);
    }

    /**
     * Sets the {@code textBaseline} property; invalid values are ignored.
     * @param textBaseline the {@code textBaseline} property value
     */
    @JsxSetter
    public void setTextBaseline(final String textBaseline) {
        switch (textBaseline) {
            case "top":
                getRenderingBackend().setTextBaseline(TextBaseline.TOP);
                break;
            case "hanging":
                getRenderingBackend().setTextBaseline(TextBaseline.HANGING);
                break;
            case "middle":
                getRenderingBackend().setTextBaseline(TextBaseline.MIDDLE);
                break;
            case "alphabetic":
                getRenderingBackend().setTextBaseline(TextBaseline.ALPHABETIC);
                break;
            case "ideographic":
                getRenderingBackend().setTextBaseline(TextBaseline.IDEOGRAPHIC);
                break;
            case "bottom":
                getRenderingBackend().setTextBaseline(TextBaseline.BOTTOM);
                break;
            default:
                // ignore invalid values per spec
        }
    }
}
