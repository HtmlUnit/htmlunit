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
        // see https://developer.mozilla.org/en-US/docs/Web/CSS/color_value

        // CSS Level 1
        CSS_COLORS.put("black", "rgb(0, 0, 0)");
        CSS_COLORS.put("silver", "rgb(192, 192, 192)");
        CSS_COLORS.put("gray", "rgb(128, 128, 128)");
        CSS_COLORS.put("white", "rgb(255, 255, 255)");
        CSS_COLORS.put("maroon", "rgb(128, 0, 0)");
        CSS_COLORS.put("red", "rgb(255, 0, 0)");
        CSS_COLORS.put("purple", "rgb(128, 0, 128)");
        CSS_COLORS.put("fuchsia", "rgb(255, 0, 255)");
        CSS_COLORS.put("green", "rgb(0, 128, 0)");
        CSS_COLORS.put("lime", "rgb(0, 255, 0)");
        CSS_COLORS.put("olive", "rgb(128, 128, 0)");
        CSS_COLORS.put("yellow", "rgb(255, 255, 0)");
        CSS_COLORS.put("navy", "rgb(0, 0, 128)");
        CSS_COLORS.put("blue", "rgb(0, 0, 255)");
        CSS_COLORS.put("teal", "rgb(0, 128, 128)");
        CSS_COLORS.put("aqua", "rgb(0, 255, 255)");

        // CSS Level 2 (Revision 1)
        CSS_COLORS.put("orange", "rgb(255, 165, 0)");

        // CSS Color Module Level 3
        CSS_COLORS.put("aliceblue", "rgb(240, 248, 255)");
        CSS_COLORS.put("antiquewhite", "rgb(250, 235, 215)");
        CSS_COLORS.put("aquamarine", "rgb(127, 255, 212)");
        CSS_COLORS.put("azure", "rgb(240, 255, 255)");
        CSS_COLORS.put("beige", "rgb(245, 245, 220)");
        CSS_COLORS.put("bisque", "rgb(255, 228, 196)");
        CSS_COLORS.put("blanchedalmond", "rgb(255, 235, 205)");
        CSS_COLORS.put("blueviolet", "rgb(138, 43, 226)");
        CSS_COLORS.put("brown", "rgb(165, 42, 42)");
        CSS_COLORS.put("burlywood", "rgb(222, 184, 135)");
        CSS_COLORS.put("cadetblue", "rgb(95, 158, 160)");
        CSS_COLORS.put("chartreuse", "rgb(127, 255, 0)");
        CSS_COLORS.put("chocolate", "rgb(210, 105, 30)");
        CSS_COLORS.put("coral", "rgb(255, 127, 80)");
        CSS_COLORS.put("cornflowerblue", "rgb(100, 149, 237)");
        CSS_COLORS.put("cornsilk", "rgb(255, 248, 220)");
        CSS_COLORS.put("crimson", "rgb(220, 20, 60)");
        CSS_COLORS.put("cyan", "rgb(0, 255, 255)"); // synonym of aqua
        CSS_COLORS.put("darkblue", "rgb(0, 0, 139)");
        CSS_COLORS.put("darkcyan", "rgb(0, 139, 139)");
        CSS_COLORS.put("darkgoldenrod", "rgb(184, 134, 11)");
        CSS_COLORS.put("darkgray", "rgb(169, 169, 169)");
        CSS_COLORS.put("darkgreen", "rgb(0, 100, 0)");
        CSS_COLORS.put("darkgrey", "rgb(169, 169, 169)");
        CSS_COLORS.put("darkkhaki", "rgb(189, 183, 107)");
        CSS_COLORS.put("darkmagenta", "rgb(139, 0, 139)");
        CSS_COLORS.put("darkolivegreen", "rgb(85, 107, 47)");
        CSS_COLORS.put("darkorange", "rgb(255, 140, 0)");
        CSS_COLORS.put("darkorchid", "rgb(153, 50, 204)");
        CSS_COLORS.put("darkred", "rgb(139, 0, 0)");
        CSS_COLORS.put("darksalmon", "rgb(233, 150, 122)");
        CSS_COLORS.put("darkseagreen", "rgb(143, 188, 143)");
        CSS_COLORS.put("darkslateblue", "rgb(72, 61, 139)");
        CSS_COLORS.put("darkslategray", "rgb(47, 79, 79)");
        CSS_COLORS.put("darkslategrey", "rgb(47, 79, 79)");
        CSS_COLORS.put("darkturquoise", "rgb(0, 206, 209)");
        CSS_COLORS.put("darkviolet", "rgb(148, 0, 211)");
        CSS_COLORS.put("deeppink", "rgb(255, 20, 147)");
        CSS_COLORS.put("deepskyblue", "rgb(0, 191, 255)");
        CSS_COLORS.put("dimgray", "rgb(105, 105, 105)");
        CSS_COLORS.put("dimgrey", "rgb(105, 105, 105)");
        CSS_COLORS.put("dodgerblue", "rgb(30, 144, 255)");
        CSS_COLORS.put("firebrick", "rgb(178, 34, 34)");
        CSS_COLORS.put("floralwhite", "rgb(255, 250, 240)");
        CSS_COLORS.put("forestgreen", "rgb(34, 139, 34)");
        CSS_COLORS.put("gainsboro", "rgb(220, 220, 220)");
        CSS_COLORS.put("ghostwhite", "rgb(248, 248, 255)");
        CSS_COLORS.put("gold", "rgb(255, 215, 0)");
        CSS_COLORS.put("goldenrod", "rgb(218, 165, 32)");
        CSS_COLORS.put("greenyellow", "rgb(173, 255, 47)");
        CSS_COLORS.put("grey", "rgb(128, 128, 128)");
        CSS_COLORS.put("honeydew", "rgb(240, 255, 240)");
        CSS_COLORS.put("hotpink", "rgb(255, 105, 180)");
        CSS_COLORS.put("indianred", "rgb(205, 92, 92)");
        CSS_COLORS.put("indigo", "rgb(75, 0, 130)");
        CSS_COLORS.put("ivory", "rgb(255, 255, 240)");
        CSS_COLORS.put("khaki", "rgb(240, 230, 140)");
        CSS_COLORS.put("lavender", "rgb(230, 230, 250)");
        CSS_COLORS.put("lavenderblush", "rgb(255, 240, 245)");
        CSS_COLORS.put("lawngreen", "rgb(124, 252, 0)");
        CSS_COLORS.put("lemonchiffon", "rgb(255, 250, 205)");
        CSS_COLORS.put("lightblue", "rgb(173, 216, 230)");
        CSS_COLORS.put("lightcoral", "rgb(240, 128, 128)");
        CSS_COLORS.put("lightcyan", "rgb(224, 255, 255)");
        CSS_COLORS.put("lightgoldenrodyellow", "rgb(250, 250, 210)");
        CSS_COLORS.put("lightgray", "rgb(211, 211, 211)");
        CSS_COLORS.put("lightgreen", "rgb(144, 238, 144)");
        CSS_COLORS.put("lightgrey", "rgb(211, 211, 211)");
        CSS_COLORS.put("lightpink", "rgb(255, 182, 193)");
        CSS_COLORS.put("lightsalmon", "rgb(255, 160, 122)");
        CSS_COLORS.put("lightseagreen", "rgb(32, 178, 170)");
        CSS_COLORS.put("lightskyblue", "rgb(135, 206, 250)");
        CSS_COLORS.put("lightslategray", "rgb(119, 136, 153)");
        CSS_COLORS.put("lightslategrey", "rgb(119, 136, 153)");
        CSS_COLORS.put("lightsteelblue", "rgb(176, 196, 222)");
        CSS_COLORS.put("lightyellow", "rgb(255, 255, 224)");
        CSS_COLORS.put("limegreen", "rgb(50, 205, 50)");
        CSS_COLORS.put("linen", "rgb(250, 240, 230)");
        CSS_COLORS.put("magenta", "rgb(255, 0, 255)"); // synonym of fuchsia
        CSS_COLORS.put("mediumaquamarine", "rgb(102, 205, 170)");
        CSS_COLORS.put("mediumblue", "rgb(0, 0, 205)");
        CSS_COLORS.put("mediumorchid", "rgb(186, 85, 211)");
        CSS_COLORS.put("mediumpurple", "rgb(147, 112, 219)");
        CSS_COLORS.put("mediumseagreen", "rgb(60, 179, 113)");
        CSS_COLORS.put("mediumslateblue", "rgb(123, 104, 238)");
        CSS_COLORS.put("mediumspringgreen", "rgb(0, 250, 154)");
        CSS_COLORS.put("mediumturquoise", "rgb(72, 209, 204)");
        CSS_COLORS.put("mediumvioletred", "rgb(199, 21, 133)");
        CSS_COLORS.put("midnightblue", "rgb(25, 25, 112)");
        CSS_COLORS.put("mintcream", "rgb(245, 255, 250)");
        CSS_COLORS.put("mistyrose", "rgb(255, 228, 225)");
        CSS_COLORS.put("moccasin", "rgb(255, 228, 181)");
        CSS_COLORS.put("navajowhite", "rgb(255, 222, 173)");
        CSS_COLORS.put("oldlace", "rgb(253, 245, 230)");
        CSS_COLORS.put("olivedrab", "rgb(107, 142, 35)");
        CSS_COLORS.put("orangered", "rgb(255, 69, 0)");
        CSS_COLORS.put("orchid", "rgb(218, 112, 214)");
        CSS_COLORS.put("palegoldenrod", "rgb(238, 232, 170)");
        CSS_COLORS.put("palegreen", "rgb(152, 251, 152)");
        CSS_COLORS.put("paleturquoise", "rgb(175, 238, 238)");
        CSS_COLORS.put("palevioletred", "rgb(219, 112, 147)");
        CSS_COLORS.put("papayawhip", "rgb(255, 239, 213)");
        CSS_COLORS.put("peachpuff", "rgb(255, 218, 185)");
        CSS_COLORS.put("peru", "rgb(205, 133, 63)");
        CSS_COLORS.put("pink", "rgb(255, 192, 203)");
        CSS_COLORS.put("plum", "rgb(221, 160, 221)");
        CSS_COLORS.put("powderblue", "rgb(176, 224, 230)");
        CSS_COLORS.put("rosybrown", "rgb(188, 143, 143)");
        CSS_COLORS.put("royalblue", "rgb(65, 105, 225)");
        CSS_COLORS.put("saddlebrown", "rgb(139, 69, 19)");
        CSS_COLORS.put("salmon", "rgb(250, 128, 114)");
        CSS_COLORS.put("sandybrown", "rgb(244, 164, 96)");
        CSS_COLORS.put("seagreen", "rgb(46, 139, 87)");
        CSS_COLORS.put("seashell", "rgb(255, 245, 238)");
        CSS_COLORS.put("sienna", "rgb(160, 82, 45)");
        CSS_COLORS.put("skyblue", "rgb(135, 206, 235)");
        CSS_COLORS.put("slateblue", "rgb(106, 90, 205)");
        CSS_COLORS.put("slategray", "rgb(112, 128, 144)");
        CSS_COLORS.put("slategrey", "rgb(112, 128, 144)");
        CSS_COLORS.put("snow", "rgb(255, 250, 250)");
        CSS_COLORS.put("springgreen", "rgb(0, 255, 127)");
        CSS_COLORS.put("steelblue", "rgb(70, 130, 180)");
        CSS_COLORS.put("tan", "rgb(210, 180, 140)");
        CSS_COLORS.put("thistle", "rgb(216, 191, 216)");
        CSS_COLORS.put("tomato", "rgb(255, 99, 71)");
        CSS_COLORS.put("turquoise", "rgb(64, 224, 208)");
        CSS_COLORS.put("violet", "rgb(238, 130, 238)");
        CSS_COLORS.put("wheat", "rgb(245, 222, 179)");
        CSS_COLORS.put("whitesmoke", "rgb(245, 245, 245)");
        CSS_COLORS.put("yellowgreen", "rgb(154, 205, 50)");

        // CSS Color Module Level 4
        CSS_COLORS.put("rebeccapurple", "rgb(102, 51, 153)");
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
