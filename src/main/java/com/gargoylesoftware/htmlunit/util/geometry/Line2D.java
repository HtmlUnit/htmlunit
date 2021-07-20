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
package com.gargoylesoftware.htmlunit.util.geometry;

/**
 * Simple 2D shape line.
 *
 * @author Ronald Brill
 */
public class Line2D implements Shape2D {

    private static final double epsilon = 0.0000001;

    private final double startX_;
    private final double startY_;
    private final double endX_;
    private final double endY_;
    private final boolean isVertical_;

    private final double slope_;
    private final double yIntercept_;

    public Line2D(final Point2D start, final Point2D end) {
        this(start.getX(), start.getY(), end.getX(), end.getY());
    }

    public Line2D(final double x1, final double y1, final double x2, final double y2) {
        startX_ = x1;
        startY_ = y1;
        endX_ = x2;
        endY_ = y2;

        isVertical_ = Math.abs(startX_ - endX_) < epsilon;
        if (isVertical_) {
            slope_ = Double.NaN;
            yIntercept_ = Double.NaN;
        }
        else {
            // y = slope * x + b
            slope_ = (endY_ - startY_) / (endX_ - startX_);
            yIntercept_ = startY_ - slope_ * startX_;
        }
    }

    public Point2D intersect(final Line2D line) {
        if (isVertical_ && line.isVertical_) {
            return null;
        }

        if (isVertical_ && !line.isVertical_) {
            final double intersectY = line.slope_ * startX_ + line.yIntercept_;
            return new Point2D(startX_, intersectY);
        }

        if (!isVertical_ && line.isVertical_) {
            final double intersectY = slope_ * line.startX_ + yIntercept_;
            return new Point2D(line.startX_, intersectY);
        }

        // parallel?
        if (Math.abs(slope_ - line.slope_) < epsilon) {
            return null;
        }

        // x = (n2-n1)/(m1-m2)
        final double intersectX = (line.yIntercept_ - yIntercept_) / (slope_ - line.slope_);
        // y = m2*x+n2
        final double intersectY = slope_ * intersectX + yIntercept_;
        return new Point2D(intersectX, intersectY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(final double x, final double y) {
        if (isVertical_) {
            if (Math.abs(startX_ - x) > epsilon) {
                return false;
            }
        }
        else {
            final double testY = slope_ * x + yIntercept_;
            if (Math.abs(y - testY) > epsilon) {
                return false;
            }

            if (x < startX_ && x < endX_
                    || (x > startX_ && x > endX_)) {
                return false;
            }
        }

        if (y < startY_ && y < endY_
                || (y > startY_ && y > endY_)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Line2D [ (" + startX_ + ", " + startY_ + "), (" + endX_ + ", " + endY_
                + "), " + (isVertical_ ? "isVertical" : "y = " + slope_ + "*x + " + yIntercept_ + "]");
    }
}
