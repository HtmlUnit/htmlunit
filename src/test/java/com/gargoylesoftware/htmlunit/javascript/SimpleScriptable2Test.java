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
package com.gargoylesoftware.htmlunit.javascript;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for {@link SimpleScriptable}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:BarnabyCourt@users.sourceforge.net">Barnaby Court</a>
 * @author David K. Taylor
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class SimpleScriptable2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"text/html", "text/html"},
            IE = {"undefined", "123456"})
    public void setNonWritablePropertyContentType() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.contentType);\n"
            + "      document.contentType = '123456';\n"
            + "      log(document.contentType);\n"
            + "    } catch (e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"CSS1Compat", "CSS1Compat"})
    public void setNonWritablePropertyCompatMode() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.compatMode);\n"
            + "      document.compatMode = '123456';\n"
            + "      log(document.compatMode);\n"
            + "    } catch (e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Arguments]")
    public void arguments_toString() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(arguments);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    public void stringWithExclamationMark() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var x = '<!>';\n"
            + "    log(x.length);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Blocked by Rhino bug 419090 (https://bugzilla.mozilla.org/show_bug.cgi?id=419090).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"x1", "x2", "x3", "x4", "x5"})
    public void arrayedMap() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var map = {};\n"
            + "    map['x1'] = 'y1';\n"
            + "    map['x2'] = 'y2';\n"
            + "    map['x3'] = 'y3';\n"
            + "    map['x4'] = 'y4';\n"
            + "    map['x5'] = 'y5';\n"
            + "    for (var i in map) {\n"
            + "      log(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * This is related to HtmlUnitContextFactory.hasFeature(Context.FEATURE_PARENT_PROTO_PROPERTIES).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void parentProtoFeature() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.createElement('div').__proto__ != undefined);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for http://sourceforge.net/p/htmlunit/bugs/587/.
     * See also http://groups.google.com/group/mozilla.dev.tech.js-engine.rhino/browse_thread/thread/1f1c24f58f662c58.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void passFunctionAsParameter() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function run(fun) {\n"
            + "    fun('log(1)');\n"
            + "  }\n"
            + "\n"
            + "  function test() {\n"
            + "    run(eval);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test JavaScript: 'new Date().getTimezoneOffset()' compared to java.text.SimpleDateFormat.format().
     *
     * @throws Exception if the test fails
     */
    @Test
    public void dateGetTimezoneOffset() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var offset = Math.abs(new Date().getTimezoneOffset());\n"
            + "    var timezone = '' + (offset/60);\n"
            + "    if (timezone.length == 1)\n"
            + "      timezone = '0' + timezone;\n"
            + "    log(timezone);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        final String timeZone = new SimpleDateFormat("Z", Locale.ROOT)
                .format(Calendar.getInstance(Locale.ROOT).getTime());
        final String hour = timeZone.substring(1, 3);
        String strMinutes = timeZone.substring(3, 5);
        final int minutes = Integer.parseInt(strMinutes);
        final StringBuilder sb = new StringBuilder();
        if (minutes != 0) {
            sb.append(hour, 1, hour.length());
            strMinutes = String.valueOf((double) minutes / 60);
            strMinutes = strMinutes.substring(1);
            sb.append(strMinutes);
        }
        else {
            sb.append(hour);
        }
        setExpectedAlerts(sb.toString());
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "function", "function"})
    public void callee() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var fun = arguments.callee.toString();\n"
            + "  log(fun.indexOf('test()') != -1);\n"
            + "  log(typeof arguments.callee);\n"
            + "  log(typeof arguments.callee.caller);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLDivElement]")
    public void getDefaultValue() throws Exception {
        getDefaultValue(false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLDivElement]")
    public void getDefaultValue_xhtml() throws Exception {
        getDefaultValue(true);
    }

    private void getDefaultValue(final boolean xhtml) throws Exception {
        final String header = xhtml ? "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" : "";
        final String html = header
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.createElement('div'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Setting is actually ignored in FF.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void set_ReadOnly_window_closed() throws Exception {
        set_ReadOnly("window.closed");
    }

    /**
     * Setting the property works in FF.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("window.length was set")
    @HtmlUnitNYI(CHROME = "0",
            EDGE = "0",
            FF = "0",
            FF_ESR = "0",
            IE = "0")
    public void set_ReadOnly_window_length() throws Exception {
        set_ReadOnly("window.length");
    }

    /**
     * Setting the property works in FF.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void set_ReadOnly_style_length() throws Exception {
        set_ReadOnly("document.body.style.length");
    }

    /**
     * All functions seem to be able to be set.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("document.getElementById was set")
    public void set_ReadOnly_getElementById() throws Exception {
        set_ReadOnly("document.getElementById");
    }

    private void set_ReadOnly(final String expression) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    " + expression + " = '" + expression + " was set" + "';\n"
            + "    log(" + expression + ");\n"
            + "  } catch(e) {log('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for the result of __lookupGetter__.
     * Until 20.06.2014 the result was wrongly a MemberBox. Converting it to a boolean was producing warning messages
     * on the error output.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "true", "function length() { [native code] }", "0", "0"},
            CHROME = {"function", "true", "function get length() { [native code] }", "0", "0"},
            EDGE = {"function", "true", "function get length() { [native code] }", "0", "0"},
            IE = {"function", "true", " function length() { [native code] } ", "0", "0"})
    @HtmlUnitNYI(CHROME = {"function", "true", "function length() { [native code] }", "0", "0"},
            EDGE = {"function", "true", "function length() { [native code] }", "0", "0"})
    public void lookupGetter() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var lengthGetter = window.__lookupGetter__('length');\n"
            + "    log(typeof lengthGetter);\n"
            + "    log(!!lengthGetter);\n"
            + "    log(lengthGetter);\n"
            + "    log(lengthGetter.call(window));\n"
            + "    log(lengthGetter.call());\n"
            + "  } catch(e) {log('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
