/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner.Default;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.annotations.ToRunWithRealBrowsers;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;

/**
 * Tests for iterability of CSS style attributes defined in {@link StyleAttributes}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
@ToRunWithRealBrowsers
public class StyleAttributesIterableTest extends WebDriverTestCase {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final List<Object[]> list = new ArrayList<>();
        for (final Definition definition : StyleAttributes.Definition.values()) {
            list.add(new Object[] {definition});
        }
        return list;
    }

    /**
     * The {@link Definition} to test.
     */
    @Parameter
    public Definition definition_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Default
    public void test() throws Exception {
        test(definition_.getPropertyName());
    }

    private void test(final String propertyName) throws Exception {
        final String html =
            "<html><head><script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    for (var i in e.style) {\n"
            + "      if (i == '" + propertyName + "') {\n"
            + "        alert('true');\n"
            + "        return;\n"
            + "      }\n"
            + "    }\n"
            + "    alert('false');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _TOUCH_ACTION() throws Exception {
        test("touchAction");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF52 = "true")
    public void _WEBKIT_BACKGROUND_CLIP() throws Exception {
        test("webkitBackgroundClip");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF52 = "true")
    public void _WEBKIT_BACKGROUND_ORIGIN() throws Exception {
        test("webkitBackgroundOrigin");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_BORDER_IMAGE() throws Exception {
        test("webkitBorderImage");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_BOX_ALIGN() throws Exception {
        test("webkitBoxAlign");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_BOX_DIRECTION() throws Exception {
        test("webkitBoxDirection");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_BOX_FLEX() throws Exception {
        test("webkitBoxFlex");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false")
    public void _WEBKIT_BOX_FLEX_GROUP() throws Exception {
        test("webkitBoxFlexGroup");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false")
    public void _WEBKIT_BOX_LINES() throws Exception {
        test("webkitBoxLines");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_BOX_ORDINAL_GROUP() throws Exception {
        test("webkitBoxOrdinalGroup");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_BOX_ORIENT() throws Exception {
        test("webkitBoxOrient");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_BOX_PACK() throws Exception {
        test("webkitBoxPack");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_TEXT_FILL_COLOR() throws Exception {
        test("webkitTextFillColor");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_TEXT_STROKE() throws Exception {
        test("webkitTextStroke");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_TEXT_STROKE_COLOR() throws Exception {
        test("webkitTextStrokeColor");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF60 = "false")
    public void _WEBKIT_TEXT_STROKE_WIDTH() throws Exception {
        test("webkitTextStrokeWidth");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getExpectedAlerts() {
        if (definition_ == null) {
            return super.getExpectedAlerts();
        }
        final BrowserVersion browserVersion = getBrowserVersion();
        return new String[] {Boolean.toString(definition_.isAvailable(browserVersion, true))};
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _FONT_VARIANT_CAPS() throws Exception {
        test("fontVariantCaps");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _FONT_VARIANT_EAST_ASIAN() throws Exception {
        test("fontVariantEastAsian");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _FONT_VARIANT_NUMERIC() throws Exception {
        test("fontVariantNumeric");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _BACKGROUND_POSITION_X() throws Exception {
        test("backgroundPositionX");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _BACKGROUND_POSITION_X_() throws Exception {
        test("background-position-x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _BACKGROUND_POSITION_Y() throws Exception {
        test("backgroundPositionY");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _BACKGROUND_POSITION_Y_() throws Exception {
        test("background-position-y");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _BLOCK_SIZE() throws Exception {
        test("blockSize");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _COLUMNS() throws Exception {
        test("columns");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _CARET_COLOR() throws Exception {
        test("caretColor");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _COLOR_ADJUST() throws Exception {
        test("colorAdjust");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _COLOR_ADJUST_() throws Exception {
        test("color-adjust");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _COLUMN_COUNT() throws Exception {
        test("columnCount");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _COLUMN_COUNT_() throws Exception {
        test("column-count");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _COLUMN_FILL() throws Exception {
        test("columnFill");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _COLUMN_FILL_() throws Exception {
        test("column-fill");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _COLUMN_GAP() throws Exception {
        test("columnGap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _COLUMN_GAP_() throws Exception {
        test("column-gap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _COLUMN_RULE() throws Exception {
        test("columnRule");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _COLUMN_RULE_() throws Exception {
        test("column-rule");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _COLUMN_RULE_COLOR() throws Exception {
        test("columnRuleColor");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _COLUMN_RULE_COLOR_() throws Exception {
        test("column-rule-color");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _COLUMN_RULE_STYLE() throws Exception {
        test("columnRuleStyle");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _COLUMN_RULE_STYLE_() throws Exception {
        test("column-rule-style");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _COLUMN_RULE_WIDTH() throws Exception {
        test("columnRuleWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _COLUMN_WIDTH() throws Exception {
        test("columnWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID() throws Exception {
        test("grid");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_AREA() throws Exception {
        test("gridArea");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_AUTO_COLUMNS() throws Exception {
        test("gridAutoColumns");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_AUTO_FLOW() throws Exception {
        test("gridAutoFlow");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_AUTO_ROWS() throws Exception {
        test("gridAutoRows");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_COLUMN() throws Exception {
        test("gridColumn");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_COLUMN_END() throws Exception {
        test("gridColumnEnd");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_COLUMN_GAP() throws Exception {
        test("gridColumnGap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_COLUMN_START() throws Exception {
        test("gridColumnStart");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_GAP() throws Exception {
        test("gridGap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_ROW() throws Exception {
        test("gridRow");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_ROW_END() throws Exception {
        test("gridRowEnd");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_ROW_GAP() throws Exception {
        test("gridRowGap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_ROW_START() throws Exception {
        test("gridRowStart");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_TEMPLATE() throws Exception {
        test("gridTemplate");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_TEMPLATE_AREAS() throws Exception {
        test("gridTemplateAreas");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_TEMPLATE_COLUMNS() throws Exception {
        test("gridTemplateColumns");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GRID_TEMPLATE_ROWS() throws Exception {
        test("gridTemplateRows");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _INLINE_SIZE() throws Exception {
        test("inlineSize");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _JUSTIFY_ITEMS() throws Exception {
        test("justifyItems");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _JUSTIFY_SELF() throws Exception {
        test("justifySelf");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _LINE_BREAK() throws Exception {
        test("lineBreak");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    public void _MARKER_OFFSET() throws Exception {
        test("markerOffset");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF60 = "true")
    public void _MARKER_OFFSET_() throws Exception {
        test("marker-offset");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MAX_BLOCK_SIZE() throws Exception {
        test("maxBlockSize");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MAX_INLINE_SIZE() throws Exception {
        test("maxInlineSize");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MIN_BLOCK_SIZE() throws Exception {
        test("minBlockSize");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MIN_INLINE_SIZE() throws Exception {
        test("minInlineSize");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    public void _MOTION() throws Exception {
        test("motion");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    public void _OFFSET_ROTATION() throws Exception {
        test("offsetRotation");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _OVERFLOW_WRAP() throws Exception {
        test("overflowWrap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _PIXEL_BOTTOM() throws Exception {
        test("pixelBottom");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _PIXEL_HEIGHT() throws Exception {
        test("pixelHeight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _PIXEL_LEFT() throws Exception {
        test("pixelLeft");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _PIXEL_RIGHT() throws Exception {
        test("pixelRight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _PIXEL_TOP() throws Exception {
        test("pixelTop");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _PLACE_CONTENT() throws Exception {
        test("placeContent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _PLACE_ITEMS() throws Exception {
        test("placeItems");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _PLACE_SELF() throws Exception {
        test("placeSelf");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _PIXEL_WIDTH() throws Exception {
        test("pixelWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _POS_BOTTOM() throws Exception {
        test("posBottom");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _POS_HEIGHT() throws Exception {
        test("posHeight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _POS_LEFT() throws Exception {
        test("posLeft");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _POS_RIGHT() throws Exception {
        test("posRight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _POS_TOP() throws Exception {
        test("posTop");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _POS_WIDTH() throws Exception {
        test("posWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SCROLL_BEHAVIOR() throws Exception {
        test("scrollBehavior");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void _TEXT_ALIGN_LAST() throws Exception {
        test("textAlignLast");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _TEXT_COMBINE_UPRIGHT() throws Exception {
        test("textCombineUpright");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _TEXT_DECORATION_BLINK() throws Exception {
        test("textDecorationBlink");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _TEXT_DECORATION_COLOR() throws Exception {
        test("textDecorationColor");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _TEXT_DECORATION_LINE() throws Exception {
        test("textDecorationLine");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _TEXT_DECORATION_LINE_THROUGH() throws Exception {
        test("textDecorationLineThrough");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _TEXT_DECORATION_NONE() throws Exception {
        test("textDecorationNone");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _TEXT_DECORATION_OVERLINE() throws Exception {
        test("textDecorationOverline");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _TEXT_DECORATION_STYLE() throws Exception {
        test("textDecorationStyle");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _TEXT_DECORATION_UNDERLINE() throws Exception {
        test("textDecorationUnderline");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _TEXT_UNDERLINE_POSITION() throws Exception {
        test("textUnderlinePosition");
    }
}
