/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import java.util.ArrayList;

/**
 * Simple 2D shape polygon.
 *
 * @author Ronald Brill
 */
public class Polygon2D implements Shape2D {
    private final ArrayList<Point2D> points_;
    private final Rectangle2D boundingBox_;

    /**
     * Ctor.
     * @see #lineTo(double, double)
     * @param startX the x value of the first point.
     * @param startY the Y value of the first point.
     */
    public Polygon2D(final double startX, final double startY) {
        points_ = new ArrayList<>();
        points_.add(new Point2D(startX, startY));
        boundingBox_ = new Rectangle2D(startX, startY, startX, startY);
    }

    /**
     * Add another corner Point to the polygon.
     * @param x the x value of the corner to be added
     * @param y the y value of the corner to be added
     * @return this to support fluent style construction
     */
    public Polygon2D lineTo(final double x, final double y) {
        points_.add(new Point2D(x, y));
        boundingBox_.extend(x, y);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(final double x, final double y) {
        if (!boundingBox_.contains(x, y)) {
            return false;
        }

        final double outsideX = boundingBox_.getLeft() - 0.0000001;
        final double outsideY = boundingBox_.getBottom();

        final Line2D testLine = new Line2D(outsideX, outsideY, x, y);
        int intersectionCount = 0;

        int i = 0;
        while (i < points_.size() - 1) {
            final Point2D start = points_.get(i);
            final Point2D end = points_.get(++i);
            final Line2D border = new Line2D(start, end);

            if (border.contains(x, y)) {
                return true;
            }

            final Point2D intersectionPoint = border.intersect(testLine);
            if (intersectionPoint != null
                    && border.contains(intersectionPoint.getX(), intersectionPoint.getY())
                    && testLine.contains(intersectionPoint.getX(), intersectionPoint.getY())) {
                intersectionCount++;
            }
        }

        final Point2D start = points_.get(0);
        final Point2D end = points_.get(i);
        final Line2D border = new Line2D(start, end);

        if (border.contains(x, y)) {
            return true;
        }

        final Point2D intersectionPoint = border.intersect(testLine);
        if (intersectionPoint != null
                && border.contains(intersectionPoint.getX(), intersectionPoint.getY())
                && testLine.contains(intersectionPoint.getX(), intersectionPoint.getY())) {
            intersectionCount++;
        }

        return intersectionCount % 2 != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return points_.size() < 2;
    }

    @Override
    public String toString() {
        return "Polygon2D []";
    }
}
