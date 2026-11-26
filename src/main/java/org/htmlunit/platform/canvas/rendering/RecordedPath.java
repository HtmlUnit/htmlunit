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
package org.htmlunit.platform.canvas.rendering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A path recorded independent of a canvas (e.g. the path of a {@code Path2D} object).
 * The segments are stored in the coordinates they were given in; {@link #replay(RenderingBackend)}
 * adds them to the current path of a backend, where the current transformation of the backend is applied
 * like for the path methods of the canvas.
 * <p>This class does not depend on a graphics library; the geometry is done by the backend.</p>
 *
 * @author Ronald Brill
 */
public final class RecordedPath implements Serializable {

    private final List<Segment> segments_ = new ArrayList<>();

    /**
     * One segment of the path.
     */
    @FunctionalInterface
    private interface Segment extends Serializable {
        /**
         * Adds this segment to the current path of the backend.
         * @param backend the backend
         */
        void replay(RenderingBackend backend);
    }

    /**
     * Creates an empty path.
     */
    public RecordedPath() {
        super();
    }

    /**
     * Creates a copy of the given path.
     * @param path the path to copy
     */
    public RecordedPath(final RecordedPath path) {
        super();
        segments_.addAll(path.segments_);
    }

    /**
     * Adds the segments of this path to the current path of the backend.
     * @param backend the backend
     */
    public void replay(final RenderingBackend backend) {
        for (final Segment segment : segments_) {
            segment.replay(backend);
        }
    }

    /**
     * Returns whether this path has no segments.
     * @return whether this path has no segments
     */
    public boolean isEmpty() {
        return segments_.isEmpty();
    }

    /**
     * Adds the segments of the given path, transformed by the given matrix.
     * @param path the path to add
     * @param a the a (m11) value of the matrix
     * @param b the b (m12) value of the matrix
     * @param c the c (m21) value of the matrix
     * @param d the d (m22) value of the matrix
     * @param e the e (dx) value of the matrix
     * @param f the f (dy) value of the matrix
     */
    public void addPath(final RecordedPath path,
            final double a, final double b, final double c, final double d, final double e, final double f) {
        final RecordedPath copy = new RecordedPath(path);
        if (a == 1 && b == 0 && c == 0 && d == 1 && e == 0 && f == 0) {
            segments_.addAll(copy.segments_);
            return;
        }

        segments_.add(backend -> {
            // the save/restore only changes the transformation; the current path is not part of the state
            backend.save();
            try {
                backend.transform(a, b, c, d, e, f);
                copy.replay(backend);
            }
            finally {
                backend.restore();
            }
        });
    }

    /**
     * Records a moveTo.
     * @param x the x
     * @param y the y
     */
    public void moveTo(final double x, final double y) {
        segments_.add(backend -> backend.moveTo(x, y));
    }

    /**
     * Records a lineTo.
     * @param x the x
     * @param y the y
     */
    public void lineTo(final double x, final double y) {
        segments_.add(backend -> backend.lineTo(x, y));
    }

    /**
     * Records a bezierCurveTo.
     * @param cp1x the cp1x
     * @param cp1y the cp1y
     * @param cp2x the cp2x
     * @param cp2y the cp2y
     * @param x the x
     * @param y the y
     */
    public void bezierCurveTo(final double cp1x, final double cp1y, final double cp2x, final double cp2y,
            final double x, final double y) {
        segments_.add(backend -> backend.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y));
    }

    /**
     * Records a quadraticCurveTo.
     * @param cpx the cpx
     * @param cpy the cpy
     * @param x the x
     * @param y the y
     */
    public void quadraticCurveTo(final double cpx, final double cpy, final double x, final double y) {
        segments_.add(backend -> backend.quadraticCurveTo(cpx, cpy, x, y));
    }

    /**
     * Records an arcTo.
     * @param x1 the x1
     * @param y1 the y1
     * @param x2 the x2
     * @param y2 the y2
     * @param radius the radius
     */
    public void arcTo(final double x1, final double y1, final double x2, final double y2, final double radius) {
        segments_.add(backend -> backend.arcTo(x1, y1, x2, y2, radius));
    }

    /**
     * Records an ellipse (an arc is an ellipse with the same radii and without rotation).
     * @param x the x
     * @param y the y
     * @param radiusX the radiusX
     * @param radiusY the radiusY
     * @param rotation the rotation
     * @param startAngle the startAngle
     * @param endAngle the endAngle
     * @param anticlockwise is anti-clockwise
     */
    public void ellipse(final double x, final double y, final double radiusX, final double radiusY,
            final double rotation, final double startAngle, final double endAngle, final boolean anticlockwise) {
        segments_.add(backend -> backend.ellipse(x, y, radiusX, radiusY, rotation, startAngle, endAngle,
                anticlockwise));
    }

    /**
     * Records a rect.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    public void rect(final double x, final double y, final double w, final double h) {
        segments_.add(backend -> backend.rect(x, y, w, h));
    }

    /**
     * Records a closePath.
     */
    public void closePath() {
        segments_.add(RenderingBackend::closePath);
    }

    /**
     * Records a rectangle with rounded corners (see {@code roundRect()} of the canvas). The radii
     * have to be valid and normalized already (see {@link #roundRect(double, double, double, double, double[])}).
     *
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     * @param radii the eight radii (x and y of the upper left, upper right, lower right, lower left corner)
     */
    private void addRoundRect(final double x, final double y, final double w, final double h, final double[] radii) {
        final double ulx = radii[0];
        final double uly = radii[1];
        final double urx = radii[2];
        final double ury = radii[3];
        final double lrx = radii[4];
        final double lry = radii[5];
        final double llx = radii[6];
        final double lly = radii[7];

        moveTo(x + ulx, y);
        lineTo(x + w - urx, y);
        ellipse(x + w - urx, y + ury, urx, ury, 0, -Math.PI / 2, 0, false);
        lineTo(x + w, y + h - lry);
        ellipse(x + w - lrx, y + h - lry, lrx, lry, 0, 0, Math.PI / 2, false);
        lineTo(x + llx, y + h);
        ellipse(x + llx, y + h - lly, llx, lly, 0, Math.PI / 2, Math.PI, false);
        lineTo(x, y + uly);
        ellipse(x + ulx, y + uly, ulx, uly, 0, Math.PI, Math.PI * 3 / 2, false);
        closePath();
        moveTo(x, y);
    }

    /**
     * Records a rectangle with rounded corners following the {@code roundRect()} algorithm
     * of the html standard (the radii are scaled down if they do not fit).
     *
     * @param x the x
     * @param y the y
     * @param w the width (may be negative)
     * @param h the height (may be negative)
     * @param cornerRadii the radii as pairs of x and y for the upper left, upper right,
     *        lower right and lower left corner (before flipping for negative width or height)
     */
    public void roundRect(final double x, final double y, final double w, final double h,
            final double[] cornerRadii) {
        final double[] radii = cornerRadii.clone();

        double left = x;
        double top = y;
        double width = w;
        double height = h;

        // a negative width or height flips the rectangle; the corners are swapped
        if (width < 0) {
            left += width;
            width = -width;
            swap(radii, 0, 2);
            swap(radii, 6, 4);
        }
        if (height < 0) {
            top += height;
            height = -height;
            swap(radii, 0, 6);
            swap(radii, 2, 4);
        }

        // scale down the radii if they overlap
        final double top2 = radii[0] + radii[2];
        final double right2 = radii[3] + radii[5];
        final double bottom2 = radii[6] + radii[4];
        final double left2 = radii[1] + radii[7];
        double scale = 1;
        if (top2 > 0) {
            scale = Math.min(scale, width / top2);
        }
        if (bottom2 > 0) {
            scale = Math.min(scale, width / bottom2);
        }
        if (left2 > 0) {
            scale = Math.min(scale, height / left2);
        }
        if (right2 > 0) {
            scale = Math.min(scale, height / right2);
        }
        if (scale < 1) {
            for (int i = 0; i < radii.length; i++) {
                radii[i] *= scale;
            }
        }

        addRoundRect(left, top, width, height, radii);
    }

    private static void swap(final double[] radii, final int corner1, final int corner2) {
        // swaps the x and y radii of the two corners
        for (int i = 0; i < 2; i++) {
            final double tmp = radii[corner1 + i];
            radii[corner1 + i] = radii[corner2 + i];
            radii[corner2 + i] = tmp;
        }
    }
}
