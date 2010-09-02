/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a CanvasRenderingContext2D.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class CanvasRenderingContext2D extends SimpleScriptable {

    /**
     * Returns the "fillStyle" property.
     * @return the "fillStyle" property
     */
    public Object jsxGet_fillStyle() {
        return null;
    }

    /**
     * Sets the "fillStyle" property.
     * @param fillStyle the "fillStyle" property
     */
    public void jsxSet_fillStyle(final Object fillStyle) {
        //empty
    }

    /**
     * Returns the "strokeStyle" property.
     * @return the "strokeStyle" property
     */
    public Object jsxGet_strokeStyle() {
        return null;
    }

    /**
     * Sets the "strokeStyle" property.
     * @param strokeStyle the "strokeStyle" property
     */
    public void jsxSet_strokeStyle(final Object strokeStyle) {
        //empty
    }

    /**
     * Returns the "lineWidth" property.
     * @return the "lineWidth" property
     */
    public double jsxGet_lineWidth() {
        return 0;
    }

    /**
     * Sets the "lineWidth" property.
     * @param lineWidth the "lineWidth" property
     */
    public void jsxSet_lineWidth(final Object lineWidth) {
        //empty
    }

    /**
     * Returns the "globalAlpha" property.
     * @return the "globalAlpha" property
     */
    public double jsxGet_globalAlpha() {
        return 0;
    }

    /**
     * Sets the "globalAlpha" property.
     * @param globalAlpha the "globalAlpha" property
     */
    public void jsxSet_globalAlpha(final Object globalAlpha) {
        //empty
    }

    /**
     * Clears the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    public void jsxFunction_clearRect(final double x, final double y, final double w, final double h) {
        //empty
    }

    /**
     * Paints the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    public void jsxFunction_fillRect(final double x, final double y, final double w, final double h) {
        //empty
    }

    /**
     * Strokes the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    public void jsxFunction_strokeRect(final double x, final double y, final double w, final double h) {
        //empty
    }

    /**
     * Begins the subpaths.
     */
    public void jsxFunction_beginPath() {
        //empty
    }

    /**
     * Closes the subpaths.
     */
    public void jsxFunction_closePath() {
        //empty
    }

    /**
     * Creates a new subpath.
     * @param x the x
     * @param y the y
     */
    public void jsxFunction_moveTo(final double x, final double y) {
        //empty
    }

    /**
     * Connect the last point to the given point.
     * @param x the x
     * @param y the y
     */
    public void jsxFunction_lineTo(final double x, final double y) {
        //empty
    }

    /**
     * Pushes state on state stack.
     */
    public void jsxFunction_save() {
        //empty
    }

    /**
     * Pops state stack and restore state.
     */
    public void jsxFunction_restore() {
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
    public void jsxFunction_createLinearGradient(final double x0, final double y0, final double r0, final double x1,
            final Object y1, final Object r1) {
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
    public void jsxFunction_arc(final double x, final double y, final double radius, final double startAngle,
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
    public void jsxFunction_arcTo(final double x1, final double y1, final double x2, final double y2,
                final double radius) {
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
    public void jsxFunction_bezierCurveTo(final double cp1x, final double cp1y, final double cp2x, final double cp2y,
            final double x, final double y) {
        //empty
    }

    /**
     * Fills the shape.
     */
    public void jsxFunction_fill() {
        //empty
    }

    /**
     * Calculates the strokes of all the subpaths of the current path.
     */
    public void jsxFunction_stroke() {
        //empty
    }

    /**
     * Creates a new clipping region.
     */
    public void jsxFunction_clip() {
        //empty
    }

    /**
     * Draws images onto the canvas.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     */
    public static void jsxFunction_drawImage(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        //empty
    }
}
