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
package org.htmlunit.platform.font;

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.css.CssPixelValueConverter;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * @author Ronald Brill
 */
public class AwtFontUtil implements FontUtil {

    @Override
    public int countLines(final String content, final int pixelWidth, final String fontSize) {
        final String[] lines = StringUtils.split(content, '\n');
        int lineCount = 0;
        final int fontSizeInt = CssPixelValueConverter.pixelValue(fontSize);
        final FontRenderContext fontRenderCtx = new FontRenderContext(null, false, true);
        for (final String line : lines) {
            if (org.htmlunit.util.StringUtils.isBlank(line)) {
                lineCount++;
            }
            else {
                // width is specified, we have to do some line breaking
                final AttributedString attributedString = new AttributedString(line);
                attributedString.addAttribute(TextAttribute.SIZE, fontSizeInt / 1.1);
                final LineBreakMeasurer lineBreakMeasurer =
                        new LineBreakMeasurer(attributedString.getIterator(), fontRenderCtx);
                lineBreakMeasurer.nextLayout(pixelWidth);
                lineCount++;
                while (lineBreakMeasurer.getPosition() < line.length() && lineCount < 1000) {
                    lineBreakMeasurer.nextLayout(pixelWidth);
                    lineCount++;
                }
            }
        }
        return lineCount;
    }
}
