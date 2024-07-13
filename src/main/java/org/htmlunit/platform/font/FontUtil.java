/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.platform.font;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * API for abstraction of font related stuff. This encapsulates awt font access to support android.
 *
 * @author Ronald Brill
 */
public interface FontUtil {

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Helper that layouts the text regarding the pixelWith and the fontSize and counts the resulting lines.
     *
     * @param content the text to be layouted
     * @param pixelWidth the max width of the resulting text block
     * @param fontSize the font size to be used
     *
     * @return the number of lines the layouted text will have
     */
    int countLines(String content, int pixelWidth, String fontSize);
}
