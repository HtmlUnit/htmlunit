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
            + "    var found = false;\n"
            + "    for (var i in e.style) {\n"
            + "      if (i == '" + propertyName + "') {\n"
            + "        found = true;\n"
            + "      }\n"
            + "    }\n"
            + "    alert(found);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _TEXT_DECORATION_BLINK() throws Exception {
        test("textDecorationBlink");
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
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _TEXT_DECORATION_UNDERLINE() throws Exception {
        test("textDecorationUnderline");
    }

}
