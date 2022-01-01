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

    @Override
    public String toString() {
        return "Circle2D [ (" + centerX_ + ", " + centerY_ + ") radius = " + radius_ + "]";
    }
}
