/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * A helper class for handling {@code height} for {@link ComputedCSSStyleDeclaration}.
 *
 * @author Ahmed Ashour
 */
final class ComputedHeight {

    private static final Map<BrowserVersion, int[]> heightMap = new HashMap<>();

    static {
        final int[] chrome = {0, 1, 2, 4, 5, 5, 6, 8, 9, 10, 11, 12, 15, 16, 16, 17, 18, 20, 21, 22, 23, 25, 26, 26, 27,
                28, 30, 31, 32, 33, 34, 36, 37, 37, 38, 40, 42, 43, 44, 45, 47, 48, 48, 49, 51, 52, 53, 54, 55, 57, 58,
                58, 59, 60, 62, 63, 64, 65, 67, 69, 69, 70, 71, 73, 74, 75, 76, 77, 79, 79, 80, 81, 83, 84, 85, 86, 87,
                89, 90, 90, 91, 93, 94, 96, 97, 98, 100, 101, 101, 102, 103, 105, 106, 107, 108, 110, 111, 111, 112,
                113, 115, 116, 117, 118, 119, 121, 122, 123, 124, 126, 127, 128, 129, 130, 132, 132, 133, 134, 136, 137,
                138, 139, 140, 142, 142, 143, 144, 145, 147};
        heightMap.put(BrowserVersion.CHROME, chrome);

        final int[] ff38 = {0, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30,
                31, 32, 33, 34, 35, 36, 37, 38, 40, 41, 42, 43, 44, 45, 46, 47, 48, 50, 51, 52, 53, 53, 55, 57, 58, 59,
                61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 72, 73, 74, 75, 76, 77, 78, 79, 80, 82, 84, 85, 86, 87, 88, 89,
                90, 91, 93, 94, 95, 96, 96, 98, 99, 100, 101, 103, 104, 105, 106, 106, 108, 109, 111, 112, 113, 115,
                116, 117, 118, 119, 120, 121, 122, 123, 125, 126, 127, 128, 129, 130, 131, 132, 133, 135, 136, 138, 139,
                139, 141, 142, 143, 144, 146, 147, 148, 149};
        heightMap.put(BrowserVersion.FIREFOX_38, ff38);

        final int[] ff45 = {0, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30,
                31, 32, 33, 34, 35, 36, 37, 38, 40, 41, 42, 43, 44, 45, 46, 47, 48, 50, 51, 52, 53, 53, 55, 57, 58, 59,
                61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 72, 73, 74, 75, 76, 77, 78, 79, 80, 82, 84, 85, 86, 87, 88, 89,
                90, 91, 93, 94, 95, 96, 96, 98, 99, 100, 101, 103, 104, 105, 106, 106, 108, 109, 111, 112, 113, 115,
                116, 117, 118, 119, 120, 121, 122, 123, 125, 126, 127, 128, 129, 130, 131, 132, 133, 135, 136, 138, 139,
                139, 141, 142, 143, 144, 146, 147, 148, 149};
        heightMap.put(BrowserVersion.FIREFOX_45, ff45);

        final int[] ie = {0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 17, 18, 20, 21, 22, 23, 24, 25, 26, 28, 29,
                30, 31, 32, 33, 35, 36, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 49, 51, 52, 53, 54, 55, 56, 58, 59,
                60, 61, 62, 63, 64, 66, 67, 68, 69, 70, 71, 72, 74, 75, 76, 77, 78, 79, 80, 82, 83, 84, 85, 86, 87, 89,
                90, 91, 92, 93, 94, 95, 97, 98, 99, 100, 101, 102, 103, 105, 106, 107, 108, 109, 110, 112, 113, 114,
                115, 116, 117, 118, 120, 121, 122, 123, 124, 125, 126, 128, 129, 130, 131, 132, 133, 135, 136, 137, 138,
                139, 140, 141, 143, 144, 145, 146, 147};
        heightMap.put(BrowserVersion.INTERNET_EXPLORER, ie);
    }

    /**
     * Returns the corresponding height of the specified {@code fontSize}
     * @param browserVersion the browser version
     * @param fontSize the font size
     * @return the corresponding height
     */
    static int getHeight(final BrowserVersion browserVersion, final String fontSize) {
        final int[] array = heightMap.get(browserVersion);
        if (array == null) {
            return 18;
        }
        int fontSizeInt = Integer.parseInt(fontSize.substring(0, fontSize.length() - 2));
        if (fontSizeInt < array.length) {
            return array[fontSizeInt];
        }
        return (int) (fontSizeInt * 1.2);
    }

    private ComputedHeight() {
    }

}
