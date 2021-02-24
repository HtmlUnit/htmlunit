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
package com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering;

import java.io.IOException;

import javax.imageio.ImageReader;

import com.gargoylesoftware.htmlunit.javascript.host.canvas.ImageData;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.Path2D;

/**
 * Interface to the rendering context used by
 * {@link com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D}.
 *
 * @author Ronald Brill
 */
public interface RenderingBackend {

    enum WindingRule {
        NON_ZERO,
        EVEN_ODD
    }

    /**
     * Starts a new path by emptying the list of sub-paths.
     */
    void beginPath();

    /**
     * Adds a cubic Bézier curve to the current sub-path. It requires
     * three points: the first two are control points and the third one
     * is the end point. The starting point is the latest point in the
     * current path, which can be changed using moveTo() before
     * creating the Bézier curve.
     * @param cp1x the cp1x
     * @param cp1y the cp1y
     * @param cp2x the cp2x
     * @param cp2y the cp2y
     * @param x the x
     * @param y the y
     */
    void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y,
            double x, double y);

    /**
     * Adds a circular arc to the current sub-path.
     * @param x the x
     * @param y the y
     * @param radius the radius
     * @param startAngle the start angle
     * @param endAngle the end angle
     * @param anticlockwise is anti-clockwise
     */
    void arc(double x, double y, double radius, double startAngle,
                double endAngle, boolean anticlockwise);

    /**
     * Paints the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    void clearRect(double x, double y, double w, double h);

    /**
     * Draws images onto the context.
     *
     * @param imageReader the reader to read the image from 8the first one)
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
     * @throws IOException in case o problems
     */
    void drawImage(ImageReader imageReader,
            int sx, int sy, Integer sWidth, Integer sHeight,
            int dx, int dy, Integer dWidth, Integer dHeight) throws IOException;

    /**
     * Constructs a base64 encoded string out of the image data.
     *
     * @param type the name of the image format
     * @return the base64 encoded string
     * @throws IOException in case o problems
     */
    String encodeToString(String type) throws IOException;

    /**
     * Creates an elliptical arc centered at (x, y) with the radii radiusX and radiusY.
     * The path starts at startAngle and ends at endAngle, and travels in the direction
     * given by anticlockwise (defaulting to clockwise).
     * @param x the x
     * @param y the y
     * @param radiusX the radiusX
     * @param radiusY the radiusY
     * @param rotation the rotation
     * @param startAngle the start angle
     * @param endAngle the end angle
     * @param anticlockwise is anti-clockwise
     */
    void ellipse(double x, double y,
            double radiusX, double radiusY,
            double rotation, double startAngle, double endAngle,
            boolean anticlockwise);

    /**
     * Fills the current or given path with the current fillStyle.
     */
    void fill();

    /**
     * Paints the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    void fillRect(int x, int y, int w, int h);

    /**
     * Fills a given text at the given (x, y) position.
     * @param text the text
     * @param x the x
     * @param y the y
     */
    void fillText(String text, double x, double y);

    /**
     * Creates a byte array containing the (4) color values of all pixels.
     *
     * @param width the width
     * @param height the height
     * @param sx start point x
     * @param sy start point y
     * @return the bytes
     */
    byte[] getBytes(int width, int height, int sx, int sy);

    /**
     * Adds a straight line to the current sub-path by connecting the
     * sub-path's last point to the specified (x, y) coordinates.
     * @param x the x
     * @param y the y
     */
    void lineTo(double x, double y);

    /**
     * Begins a new sub-path at the point specified
     * by the given (x, y) coordinates.
     * @param x the x
     * @param y the y
     */
    void moveTo(double x, double y);

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
    void putImageData(ImageData imageData, int dx, int dy, int dirtyX, int dirtyY, int dirtyWidth, int dirtyHeight);

    /**
     * Adds a quadratic Bézier curve to the current sub-path. It requires
     * two points: the first one is a control point and the second one is
     * the end point. The starting point is the latest point in the
     * current path, which can be changed using moveTo() before
     * creating the quadratic Bézier curve.
     * @param cpx the cpx
     * @param cpy the cpy
     * @param x the x
     * @param y the y
     */
    void quadraticCurveTo(double cpx, double cpy, double x, double y);

    /**
     * Adds a rectangle to the current path.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    void rect(double x, double y, double w, double h);

    /**
     * Restores the most recently saved canvas state by popping the top
     * entry in the drawing state stack. If there is no saved state,
     * this method does nothing.
     */
    void restore();

    /**
     * Adds a rotation to the transformation matrix.
     * @param angle the angle
     */
    void rotate(double angle);

    /**
     * Saves the entire state of the canvas by pushing
     * the current state onto a stack.
     */
    void save();

    /**
     * Sets the {@code fillStyle} property.
     * @param fillStyle the {@code fillStyle} property
     */
    void setFillStyle(String fillStyle);

    /**
     * Sets the {@code strokeStyle} property.
     * @param strokeStyle the {@code strokeStyle} property
     */
    void setStrokeStyle(String strokeStyle);

    /**
     * Returns the {@code lineWidth} property.
     * @return the {@code lineWidth} property
     */
    int getLineWidth();

    /**
     * Sets the {@code lineWidth} property.
     * @param lineWidth the {@code lineWidth} property
     */
    void setLineWidth(int lineWidth);

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
    void setTransform(double m11, double m12, double m21, double m22, double dx, double dy);

    /**
     * Strokes (outlines) the current or given path with the current stroke style.
     */
    void stroke();

    /**
     * Paints the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    void strokeRect(int x, int y, int w, int h);

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
    void transform(double m11, double m12, double m21, double m22, double dx, double dy);

    /**
     * Adds a translation transformation to the current matrix.
     * @param x the x
     * @param y the y
     */
    void translate(int x, int y);

    /**
     * Turns the current or given path into the current clipping region.
     * It replaces any previous clipping region.
     * @param windingRule the RenderingBackend.WindingRule {@link WindingRule}
     * to be used
     * @param path the path or null if the current path should be used
     */
    void clip(RenderingBackend.WindingRule windingRule, Path2D path);

    /**
     * Attempts to add a straight line from the current point to the start of the current sub-path.
     * If the shape has already been closed or has only one point, this function does nothing.
     */
    void closePath();

    /**
     * @return the alpha (transparency) value that is applied to shapes and images
     * before they are drawn onto the canvas.
     */
    double getGlobalAlpha();

    /**
     * Specifies the alpha (transparency) value that is applied to shapes and images
     * before they are drawn onto the canvas.
     * @param globalAlpha the new alpha
     */
    void setGlobalAlpha(double globalAlpha);
}
