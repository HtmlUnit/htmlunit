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

import com.gargoylesoftware.htmlunit.html.HtmlArea;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Simple 2D shape circle.
 *
 * @author Ronald Brill
 */
public class Circle2D implements Shape2D {
    private final double centerX_;
    private final double centerY_;
    private final double radius_;

    /**
     * Ctor.
     * @param centerX x value of the second center
     * @param centerY y value of the second center
     * @param radius the radius
     */
    public Circle2D(final double centerX, final double centerY, final double radius) {
        centerX_ = centerX;
        centerY_ = centerY;
        radius_ = radius;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(final double x, final double y) {
        final double offsetX = centerX_ - x;
        final double offsetY = centerY_ - y;

        return offsetX * offsetX + offsetY * offsetY <= radius_ * radius_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return radius_ < epsilon;
    }

    public static Shape2D parse(HtmlArea htmlArea) {
        // browsers seem to support comma and blank
        final String[] coords = StringUtils.split(htmlArea.getCoordsAttribute(), ", ");

        double centerX = 0;
        double centerY = 0;
        double radius = 0;

        try {
            if (coords.length > 0) {
                centerX = Double.parseDouble(coords[0].trim());
            }
            if (coords.length > 1) {
                centerY = Double.parseDouble(coords[1].trim());
            }
            if (coords.length > 2) {
                radius = Double.parseDouble(coords[2].trim());
            }

        }
        catch (final NumberFormatException e) {
            htmlArea.getLOG().warn("Invalid circle coords '" + Arrays.toString(coords) + "'", e);
        }

        return new Circle2D(centerX, centerY, radius);
    }

    @Override
    public String toString() {
        return "Circle2D [ (" + centerX_ + ", " + centerY_ + ") radius = " + radius_ + "]";
    }
}
