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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HTMLOptionsCollection}.
 *
 * @author Ronald Brill
 */
public class HTMLOptionsCollectionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo*"})
    public void addNoPosEmpty() throws Exception {
        add("", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo"})
    public void addNoPosEmptyMulti() throws Exception {
        add("", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three", "foo"})
    public void addNoPos() throws Exception {
        add("", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three*", "foo"})
    public void addNoPosMulti() throws Exception {
        add("", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo*"})
    public void addPosMinusOneEmpty() throws Exception {
        add(", -1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo"})
    public void addPosMinusOneEmptyMulti() throws Exception {
        add(", -1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three", "foo"})
    public void addPosMinusOne() throws Exception {
        add(", -1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three*", "foo"})
    public void addPosMinusOneMulti() throws Exception {
        add(", -1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo*"})
    public void addPosZeroEmpty() throws Exception {
        add(", 0", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo"})
    public void addPosZeroEmptyMulti() throws Exception {
        add(", 0", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "foo", "One", "Two*", "Three"})
    public void addPosZero() throws Exception {
        add(", 0", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "foo", "One", "Two*", "Three*"})
    public void addPosZeroMulti() throws Exception {
        add(", 0", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo*"})
    public void addPosOneEmpty() throws Exception {
        add(", 1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo"})
    public void addPosOneEmptyMulti() throws Exception {
        add(", 1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "foo", "Two*", "Three"})
    public void addPosOne() throws Exception {
        add(", 1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "foo", "Two*", "Three*"})
    public void addPosOneMulti() throws Exception {
        add(", 1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo*"})
    public void addPosThreeEmpty() throws Exception {
        add(", 3", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo"})
    public void addPosThreeEmptyMulti() throws Exception {
        add(", 3", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three", "foo"})
    public void addPosThree() throws Exception {
        add(", 3", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three*", "foo"})
    public void addPosThreeMulti() throws Exception {
        add(", 3", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo*"})
    public void addPosTenEmpty() throws Exception {
        add(", 10", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo"})
    public void addPosTenEmptyMulti() throws Exception {
        add(", 10", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three", "foo"})
    public void addPosTen() throws Exception {
        add(", 10", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three*", "foo"})
    public void addPosTenMulti() throws Exception {
        add(", 10", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo*"})
    public void addBeforeNullEmpty() throws Exception {
        add(", null", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo"})
    public void addBeforeNullEmptyMulti() throws Exception {
        add(", null", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three", "foo"})
    public void addBeforeNull() throws Exception {
        add(", null", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three*", "foo"})
    public void addBeforeNullMulti() throws Exception {
        add(", null", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "NotFoundError/DOMException"})
    public void addBeforeUnknownEmpty() throws Exception {
        add(", new Option('foo', '123')", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "NotFoundError/DOMException"})
    public void addBeforeUnknownEmptyMulti() throws Exception {
        add(", new Option('foo', '123')", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "NotFoundError/DOMException"})
    public void addBeforeUnknown() throws Exception {
        add(", new Option('foo', '123')", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "NotFoundError/DOMException"})
    public void addBeforeUnknownMulti() throws Exception {
        add(", new Option('foo', '123')", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "foo", "One", "Two*", "Three"})
    public void addBeforeFirst() throws Exception {
        add(", oSelect.options[0]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "foo", "One", "Two*", "Three*"})
    public void addBeforeFirstMulti() throws Exception {
        add(", oSelect.options[0]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "foo", "Two*", "Three"})
    public void addBeforeSecond() throws Exception {
        add(", oSelect.options[1]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "foo", "Two*", "Three*"})
    public void addBeforeSecondMulti() throws Exception {
        add(", oSelect.options[1]", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "foo", "Three"})
    public void addBeforeLast() throws Exception {
        add(", oSelect.options[2]", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "foo", "Three*"})
    public void addBeforeLastMulti() throws Exception {
        add(", oSelect.options[2]", false, true);
    }

    private void add(final String param, final boolean empty, final boolean multi) throws Exception {
        String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var oSelect = document.forms.testForm.select1;\n"
            + "        log(oSelect.length);\n"
            + "        var opt = new Option('foo', '123');\n"
            + "        oSelect.options.add(opt" + param + ");\n"

            + "        log(oSelect.options.length);\n"
            + "        for (var i = 0; i < oSelect.options.length; i++) {\n"
            + "          log(oSelect.options[i].text + (oSelect.options[i].selected ? '*' : ''));\n"
            + "        }\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='testForm'>\n"
            + "    <select name='select1' " + (multi ? "multiple" : "") + ">\n";
        if (!empty) {
            html = html
                    + "      <option name='option1' value='value1'>One</option>\n"
                    + "      <option name='option2' value='value2' selected>Two</option>\n"
                    + "      <option name='option3' value='value3'" + (multi ? "selected" : "") + ">Three</option>\n";
        }
        html = html
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void getMinusOneEmpty() throws Exception {
        get("-1", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void getMinusOne() throws Exception {
        get("-1", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("One")
    public void getZero() throws Exception {
        get("0", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Two*")
    public void getOne() throws Exception {
        get("1", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Three")
    public void getTwo() throws Exception {
        get("2", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void getThree() throws Exception {
        get("3", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void getTenEmpty() throws Exception {
        get("10", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void getTen() throws Exception {
        get("10", false);
    }

    private void get(final String pos, final boolean empty) throws Exception {
        String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var oSelect = document.forms.testForm.select1;\n"
            + "        var opt = oSelect.options[" + pos + "];\n"
            + "        log(opt ? opt.text + (opt.selected ? '*' : '') : opt);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='testForm'>\n"
            + "    <select name='select1' >\n";
        if (!empty) {
            html = html
                    + "      <option name='option1' value='value1'>One</option>\n"
                    + "      <option name='option2' value='value2' selected>Two</option>\n"
                    + "      <option name='option3' value='value3'>Three</option>\n";
        }
        html = html
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void putMinusOneNullEmpty() throws Exception {
        put("-1", "null", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void putMinusOneNullEmptyMulti() throws Exception {
        put("-1", "null", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "One", "Two*", "Three"})
    public void putMinusOneNull() throws Exception {
        put("-1", "null", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "One", "Two*", "Three*"})
    public void putMinusOneNullMulti() throws Exception {
        put("-1", "null", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "One", "Two*", "Three"})
    public void putMinusOneEmpty() throws Exception {
        put("-1", "opt", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "One", "Two*", "Three*"})
    public void putMinusOneEmptyMulti() throws Exception {
        put("-1", "opt", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "One", "Two*", "Three"})
    public void putMinusOne() throws Exception {
        put("-1", "opt", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "One", "Two*", "Three*"})
    public void putMinusOneMulti() throws Exception {
        put("-1", "opt", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo*"})
    public void putZeroEmpty() throws Exception {
        put("0", "opt", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "foo"})
    public void putZeroEmptyMulti() throws Exception {
        put("0", "opt", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "foo", "Two*", "Three"})
    public void putZero() throws Exception {
        put("0", "opt", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "foo", "Two*", "Three*"})
    public void putZeroMulti() throws Exception {
        put("0", "opt", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "One*", "foo", "Three"})
    public void putOne() throws Exception {
        put("1", "opt", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "One", "foo", "Three*"})
    public void putOneMulit() throws Exception {
        put("1", "opt", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "One", "Two*", "foo"})
    public void putTwo() throws Exception {
        put("2", "opt", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "3", "One", "Two*", "foo"})
    public void putTwoMulti() throws Exception {
        put("2", "opt", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three", "foo"})
    public void putThree() throws Exception {
        put("3", "opt", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "4", "One", "Two*", "Three*", "foo"})
    public void putThreeMulti() throws Exception {
        put("3", "opt", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "11", "One", "Two*", "Three", "", "", "", "", "", "", "", "foo"})
    public void putTen() throws Exception {
        put("10", "opt", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "11", "One", "Two*", "Three*", "", "", "", "", "", "", "", "foo"})
    public void putTenMulti() throws Exception {
        put("10", "opt", false, true);
    }

    private void put(final String pos, final String param, final boolean empty, final boolean multi) throws Exception {
        String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var oSelect = document.forms.testForm.select1;\n"
            + "        log(oSelect.length);\n"
            + "        var opt = new Option('foo', '123');\n"
            + "        oSelect.options[" + pos + "] = " + param + ";\n"

            + "        log(oSelect.options.length);\n"
            + "        for (var i = 0; i < oSelect.options.length; i++) {\n"
            + "          log(oSelect.options[i].text + (oSelect.options[i].selected ? '*' : ''));\n"
            + "        }\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='testForm'>\n"
            + "    <select name='select1' " + (multi ? "multiple" : "") + ">\n";
        if (!empty) {
            html = html
                    + "      <option name='option1' value='value1'>One</option>\n"
                    + "      <option name='option2' value='value2' selected>Two</option>\n"
                    + "      <option name='option3' value='value3'" + (multi ? "selected" : "") + ">Three</option>\n";
        }
        html = html
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void removeMinusOneEmpty() throws Exception {
        remove("-1", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void removeMinusOneEmptyMulti() throws Exception {
        remove("-1", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "One", "Two*", "Three"})
    public void removeMinusOne() throws Exception {
        remove("-1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "One", "Two*", "Three*"})
    public void removeMinusOneMulti() throws Exception {
        remove("-1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "Two*", "Three"})
    public void removeZero() throws Exception {
        remove("0", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "Two*", "Three*"})
    public void removeZeroMulti() throws Exception {
        remove("0", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "One*", "Three"})
    public void removeOne() throws Exception {
        remove("1", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "One", "Three*"})
    public void removeOneMulti() throws Exception {
        remove("1", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "One", "Two*"})
    public void removeTwo() throws Exception {
        remove("2", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "One", "Two*"})
    public void removeTwoMulti() throws Exception {
        remove("2", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "One", "Two*", "Three"})
    public void removeThree() throws Exception {
        remove("3", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "One", "Two*", "Three*"})
    public void removeThreeMulti() throws Exception {
        remove("3", false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void removeTenEmpty() throws Exception {
        remove("10", true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void removeTenEmptyMulti() throws Exception {
        remove("10", true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "One", "Two*", "Three"})
    public void removeTen() throws Exception {
        remove("10", false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "One", "Two*", "Three*"})
    public void removeTenMuti() throws Exception {
        remove("10", false, true);
    }

    private void remove(final String pos, final boolean empty, final boolean multi) throws Exception {
        String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var oSelect = document.forms.testForm.select1;\n"
            + "        oSelect.options.remove(" + pos + ");\n"

            + "        log(oSelect.options.length);\n"
            + "        for (var i = 0; i < oSelect.options.length; i++) {\n"
            + "          log(oSelect.options[i].text + (oSelect.options[i].selected ? '*' : ''));\n"
            + "        }\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='testForm'>\n"
            + "    <select name='select1' " + (multi ? "multiple" : "") + ">\n";
        if (!empty) {
            html = html
                    + "      <option name='option1' value='value1'>One</option>\n"
                    + "      <option name='option2' value='value2' selected>Two</option>\n"
                    + "      <option name='option3' value='value3'" + (multi ? "selected" : "") + ">Three</option>\n";
        }
        html = html
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "3"})
    public void length() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var sel = document.form1.select0;\n"
            + "  log(sel.options.length);\n"
            + "  sel = document.form1.select1;\n"
            + "  log(sel.options.length);\n"
            + "  sel = document.form1.select3;\n"
            + "  log(sel.options.length);\n"
            + "}</script></head>\n"

            + "<body onload='test()'>\n"
            + "<form name='form1'>\n"
            + "  <select name='select0'></select>\n"

            + "  <select name='select1'>\n"
            + "    <option>One</option>\n"
            + "  </select>\n"

            + "  <select name='select3'>\n"
            + "    <option>One</option>\n"
            + "    <option>Two</option>\n"
            + "    <option>Three</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "One", "3", "One", "Two", "Three"})
    public void setLengthMinusOne() throws Exception {
        setLength("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "0"})
    public void setLengthZero() throws Exception {
        setLength("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "", "1", "One", "1", "One"})
    public void setLengthOne() throws Exception {
        setLength("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "", "", "2", "One", "", "2", "One", "Two"})
    public void setLengthTwo() throws Exception {
        setLength("2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "", "", "", "3", "One", "", "", "3", "One", "Two", "Three"})
    public void setLengthThree() throws Exception {
        setLength("3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"10", "", "", "", "", "", "", "", "", "", "",
             "10", "One", "", "", "", "", "", "", "", "", "",
             "10", "One", "Two", "Three", "", "", "", "", "", "", ""})
    public void setLengthTen() throws Exception {
        setLength("10");
    }

    private void setLength(final String lenght) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var sel = document.form1.select0;\n"
            + "  try {\n"
            + "    sel.options.length = " + lenght + ";\n"
            + "    log(sel.options.length);\n"
            + "    for (var i = 0; i < sel.options.length; i++) {\n"
            + "      log(sel.options[i].text);\n"
            + "    }\n"
            + "  } catch(e) { logEx(e); }\n"

            + "  var sel = document.form1.select1;\n"
            + "  try {\n"
            + "    sel.options.length = " + lenght + ";\n"
            + "    log(sel.options.length);\n"
            + "    for (var i = 0; i < sel.options.length; i++) {\n"
            + "      log(sel.options[i].text);\n"
            + "    }\n"
            + "  } catch(e) { logEx(e); }\n"

            + "  var sel = document.form1.select3;\n"
            + "  try {\n"
            + "    sel.options.length = " + lenght + ";\n"
            + "    log(sel.options.length);\n"
            + "    for (var i = 0; i < sel.options.length; i++) {\n"
            + "      log(sel.options[i].text);\n"
            + "    }\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}</script></head>\n"

            + "<body onload='test()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select0'></select>\n"

            + "    <select name='select1'>\n"
            + "      <option>One</option>\n"
            + "    </select>\n"

            + "    <select name='select3'>\n"
            + "      <option>One</option>\n"
            + "      <option>Two</option>\n"
            + "      <option>Three</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "", "4", "One", "1", "", "0"})
    public void setLength_increase() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var sel = document.form1.select0;\n"
            + "  try {\n"
            + "    sel.options.length = 1;\n"
            + "    log(sel.options.length);\n"
            + "    log(sel.options[0].text);\n"
            + "  } catch(e) { log(e); }\n"

            + "  sel = document.form1.select1;\n"
            + "  try {\n"
            + "    sel.options.length = 4;\n"
            + "    log(sel.options.length);\n"
            + "    log(sel.options[0].text);\n"
            + "    log(sel.options[0].childNodes.length);\n"
            + "    log(sel.options[1].text);\n"
            + "    log(sel.options[1].childNodes.length);\n"
            + "  } catch(e) { log(e); }\n"
            + "}</script></head>\n"

            + "<body onload='test()'>\n"
            + "<form name='form1'>\n"
            + "  <select name='select0'></select>\n"

            + "  <select name='select1'>\n"
            + "    <option>One</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "false", "true", "false", "false"})
    public void in() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var opts = document.form1.select.options;\n"
            + "  log(opts.length);\n"
            + "  log(-1 in opts);\n"
            + "  log(0 in opts);\n"
            + "  log(1 in opts);\n"
            + "  log(42 in opts);\n"
            + "}</script></head>\n"

            + "<body onload='test()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select'>\n"
            + "      <option>One</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "undefined", "[object HTMLOptionElement]", "undefined", "undefined"})
    public void index() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var opts = document.form1.select.options;\n"
            + "  log(opts.length);\n"
            + "  log(opts[-1]);\n"
            + "  log(opts[0]);\n"
            + "  log(opts[1]);\n"
            + "  log(opts[42]);\n"
            + "}</script></head>\n"

            + "<body onload='test()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select'>\n"
            + "      <option>One</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "null", "[object HTMLOptionElement]", "null", "null"})
    public void item() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var opts = document.form1.select.options;\n"
            + "  log(opts.length);\n"
            + "  log(opts.item(-1));\n"
            + "  log(opts.item(0));\n"
            + "  log(opts.item(1));\n"
            + "  log(opts.item(42));\n"
            + "}</script></head>\n"

            + "<body onload='test()'>\n"
            + "  <form name='form1'>\n"
            + "    <select name='select'>\n"
            + "      <option>One</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
