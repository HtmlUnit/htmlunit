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
package com.gargoylesoftware.htmlunit.html.impl;

/**
 * Our own implementation of color to be
 * independent of awt (for this).
 *
 * @author Ronald Brill
 */
public class Color {
    private final int red_;
    private final int green_;
    private final int blue_;
    private final int alpha_;

    public Color(final int red, final int green, final int blue) {
        this(red, green, blue, 255);
    }

    public Color(final int red, final int green, final int blue, final int alpha) {
        if (red < 0 || red > 255) {
            throw new IllegalArgumentException("Color red value '" + red + " outside of expected range");
        }
        if (green < 0 || green > 255) {
            throw new IllegalArgumentException("Color red value '" + green + " outside of expected range");
        }
        if (blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Color red value '" + blue + " outside of expected range");
        }
        if (alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException("Color red value '" + alpha + " outside of expected range");
        }

        red_ = red;
        green_ = green;
        blue_ = blue;
        alpha_ = alpha;
    }

    public int getRed() {
        return red_;
    }

    public int getGreen() {
        return green_;
    }

    public int getBlue() {
        return blue_;
    }

    public int getAlpha() {
        return alpha_;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + alpha_;
        result = prime * result + blue_;
        result = prime * result + green_;
        result = prime * result + red_;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final Color other = (Color) obj;
        if (alpha_ != other.alpha_) {
            return false;
        }
        if (blue_ != other.blue_) {
            return false;
        }
        if (green_ != other.green_) {
            return false;
        }
        if (red_ != other.red_) {
            return false;
        }
        return true;
    }
}
