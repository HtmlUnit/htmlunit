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
package com.gargoylesoftware.htmlunit.javascript.regexp;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for the RegEx support.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class RegExpTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"g", "true", "false", "false", "false"},
            IE = {"undefined", "true", "false", "false", "undefined"})
    public void globalCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'g');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"g", "true", "false", "false", "false"},
            IE = {"undefined", "true", "false", "false", "undefined"})
    public void global() throws Exception {
        testEvaluateProperties("/foo/g;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"i", "false", "true", "false", "false"},
            IE = {"undefined", "false", "true", "false", "undefined"})
    public void ignoreCaseCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'i');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"i", "false", "true", "false", "false"},
            IE = {"undefined", "false", "true", "false", "undefined"})
    public void ignoreCase() throws Exception {
        testEvaluateProperties("/foo/i;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"m", "false", "false", "true", "false"},
            IE = {"undefined", "false", "false", "true", "undefined"})
    public void multilineCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'm');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"m", "false", "false", "true", "false"},
            IE = {"undefined", "false", "false", "true", "undefined"})
    public void multiline() throws Exception {
        testEvaluateProperties("/foo/m;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"y", "false", "false", "false", "true"},
            IE = {})
    public void stickyCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'y');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"y", "false", "false", "false", "true"},
            IE = {})
    public void sticky() throws Exception {
        testEvaluateProperties("/foo/y;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"gm", "true", "false", "true", "false"},
            IE = {"undefined", "true", "false", "true", "undefined"})
    public void globalMultilineCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'gm');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"gm", "true", "false", "true", "false"},
            IE = {"undefined", "true", "false", "true", "undefined"})
    public void globalMultiline() throws Exception {
        testEvaluateProperties("/foo/gm;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"gi", "true", "true", "false", "false"},
            IE = {"undefined", "true", "true", "false", "undefined"})
    public void globalIgnoreCaseCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'ig');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"gi", "true", "true", "false", "false"},
            IE = {"undefined", "true", "true", "false", "undefined"})
    public void globalIgnoreCase() throws Exception {
        testEvaluateProperties("/foo/ig;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"gy", "true", "false", "false", "true"},
            IE = {})
    public void globalStickyCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'gy');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"gy", "true", "false", "false", "true"},
            IE = {})
    public void globalSticky() throws Exception {
        testEvaluateProperties("/foo/gy;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"gim", "true", "true", "true", "false"},
            IE = {"undefined", "true", "true", "true", "undefined"})
    public void globalMultilineIgnoreCaseCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'mig');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"gim", "true", "true", "true", "false"},
            IE = {"undefined", "true", "true", "true", "undefined"})
    public void globalMultilineIgnoreCase() throws Exception {
        testEvaluateProperties("/foo/gmi;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"giy", "true", "true", "false", "true"},
            IE = {})
    public void globalIgnoreCaseStickyCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'yig');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"giy", "true", "true", "false", "true"},
            IE = {})
    public void globalIgnoreCaseSticky() throws Exception {
        testEvaluateProperties("/foo/ygi;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"gmy", "true", "false", "true", "true"},
            IE = {})
    public void globalMultilineStickyCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'gmy');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"gmy", "true", "false", "true", "true"},
            IE = {})
    public void globalMultilineSticky() throws Exception {
        testEvaluateProperties("/foo/gmy;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"im", "false", "true", "true", "false"},
            IE = {"undefined", "false", "true", "true", "undefined"})
    public void ignoreCaseMultilineCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'im');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"im", "false", "true", "true", "false"},
            IE = {"undefined", "false", "true", "true", "undefined"})
    public void ignoreCaseMultiline() throws Exception {
        testEvaluateProperties("/foo/mi;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"iy", "false", "true", "false", "true"},
            IE = {})
    public void ignoreCaseStickyCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'yi');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"iy", "false", "true", "false", "true"},
            IE = {})
    public void ignoreCaseSticky() throws Exception {
        testEvaluateProperties("/foo/iy;");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"my", "false", "false", "true", "true"},
            IE = {})
    public void multilineStickyCtor() throws Exception {
        testEvaluateProperties("new RegExp('foo', 'my');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"my", "false", "false", "true", "true"},
            IE = {})
    public void multilineSticky() throws Exception {
        testEvaluateProperties("/foo/my;");
    }

    private void testEvaluateProperties(final String script) throws Exception {
        final String html =
                "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "function test() {\n"
                + "  var regex = " + script + "\n"
                + "  log(regex.flags);\n"
                + "  log(regex.global);\n"
                + "  log(regex.ignoreCase);\n"
                + "  log(regex.multiline);\n"
                + "  log(regex.sticky);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + LOG_TEXTAREA
                + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = {})
    public void stickyStartOfLine() throws Exception {
        final String script =
                "var regex = /^foo/y;\n"
                + "regex.lastIndex = 2;\n"
                + "log(regex.test('..foo'));";
        testEvaluate(script);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false", "true"},
            IE = {})
    public void stickyStartOfLineMultiline() throws Exception {
        final String script =
                "var regex = /^foo/my;\n"
                + "regex.lastIndex = 2;\n"
                + "log(regex.test('..foo'))\n"
                + "regex.lastIndex = 2;\n"
                + "log(regex.test('.\\nfoo'));";
        testEvaluate(script);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "a", "a"},
            IE = {})
    public void stickyAndGlobal() throws Exception {
        final String script =
                "var result = 'aaba'.match(/a/yg);\n"
                + "log(result.length);\n"
                + "log(result[0]);\n"
                + "log(result[1]);\n";
        testEvaluate(script);
    }

    /**
     * @throws Exception if an error occurs
     */
    // @Test
    @Alerts(DEFAULT = {"0", "undefined", "true", "false", "undefined"},
            IE = {})
    public void flagsPropery() throws Exception {
        final String script =
                "var get = Object.getOwnPropertyDescriptor(RegExp.prototype, 'flags');\n"
                + "log(get.get.length);\n"
                + "log(get.value);\n"
                + "log(get.configurable);\n"
                + "log(get.enumerable);\n"
                + "log(get.writable);\n";
        testEvaluate(script);
    }

    private void testEvaluate(final String script) throws Exception {
        final String html =
                "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "function test() {\n"
                + script
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + LOG_TEXTAREA
                + "</body></html>";

        loadPageVerifyTextArea2(html);
    }
}
