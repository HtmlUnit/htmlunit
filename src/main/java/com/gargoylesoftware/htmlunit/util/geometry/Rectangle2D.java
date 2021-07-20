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
 * Simple 2D shape rectangle.
 *
 * @author Ronald Brill
 */
public class Rectangle2D implements Shape2D {
    private double left_;
    private double top_;
    private double right_;
    private double bottom_;

    public Rectangle2D(final double x1, final double y1, final double x2, final double y2) {
        if (x1 < x2) {
            left_ = x1;
            right_ = x2;
        }
        else {
            left_ = x2;
            right_ = x1;
        }

        if (y1 > y2) {
            top_ = y1;
            bottom_ = y2;
        }
        else {
            top_ = y2;
            bottom_ = y1;
        }
    }

    public double getLeft() {
        return left_;
    }

    public double getBottom() {
        return bottom_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(final double x, final double y) {
        return x >= left_
                && x <= right_
                && y <= top_
                && y >= bottom_;
    }

    public void extend(final double x, final double y) {
        if (x > right_) {
            right_ = x;
        }
        else {
            if (x < left_) {
                left_ = x;
            }
        }

        if (y > top_) {
            top_ = y;
        }
        else {
            if (y < bottom_) {
                bottom_ = y;
            }
        }
    }

    @Override
    public String toString() {
        return "Rectangle2D [left_=" + left_ + ", top_=" + top_ + ", right_=" + right_ + ", bottom_=" + bottom_ + "]";
    }
}
