/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.css;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Helper to work with colors.
 *
 * @author Ronald Brill
 */
public final class CssColors {

    private static final Map<String, String> CSS_COLORS = new HashMap<>();

    static {
        CSS_COLORS.put("aqua", "rgb(0, 255, 255)");
        CSS_COLORS.put("black", "rgb(0, 0, 0)");
        CSS_COLORS.put("blue", "rgb(0, 0, 255)");
        CSS_COLORS.put("fuchsia", "rgb(255, 0, 255)");
        CSS_COLORS.put("gray", "rgb(128, 128, 128)");
        CSS_COLORS.put("green", "rgb(0, 128, 0)");
        CSS_COLORS.put("lime", "rgb(0, 255, 0)");
        CSS_COLORS.put("maroon", "rgb(128, 0, 0)");
        CSS_COLORS.put("navy", "rgb(0, 0, 128)");
        CSS_COLORS.put("olive", "rgb(128, 128, 0)");
        CSS_COLORS.put("purple", "rgb(128, 0, 128)");
        CSS_COLORS.put("red", "rgb(255, 0, 0)");
        CSS_COLORS.put("silver", "rgb(192, 192, 192)");
        CSS_COLORS.put("teal", "rgb(0, 128, 128)");
        CSS_COLORS.put("white", "rgb(255, 255, 255)");
        CSS_COLORS.put("yellow", "rgb(255, 255, 0)");
    }

    private CssColors() {
        // util class
    }

    /**
     * Returns if the specified token is a reserved color keyword.
     * @param token the token to check
     * @return whether the token is a reserved color keyword or not
     */
    public static boolean isColorKeyword(final String token) {
        return CSS_COLORS.containsKey(token.toLowerCase(Locale.ROOT));
    }

    /**
     * Gets the RGB equivalent of a CSS color if the provided color is recognized.
     * @param color the color
     * @return the provided color if this is not a recognized color keyword, the RGB value
     *         in the form "rgb(x, y, z)" otherwise
     */
    public static String toRGBColor(final String color) {
        final String rgbValue = CSS_COLORS.get(color.toLowerCase(Locale.ROOT));
        if (rgbValue != null) {
            return rgbValue;
        }
        return color;
    }
}
