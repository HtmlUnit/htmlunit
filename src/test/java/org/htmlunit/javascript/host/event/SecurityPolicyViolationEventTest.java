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
package org.htmlunit.javascript.host.event;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link SecurityPolicyViolationEvent}.
 *
 * @author Ronald Brill
 */
public class SecurityPolicyViolationEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    log(event);\n"
            + "    log(event.type);\n"
            + "    log(event.bubbles);\n"
            + "    log(event.cancelable);\n"
            + "    log(event.composed);\n"
            + "  }\n";

    private static final String DUMP_PROPERTIES_FUNCTION = "  function dumpProperties(event) {\n"
            + "    log(event.documentURI);\n"
            + "    log(event.referrer);\n"
            + "    log(event.blockedURI);\n"
            + "    log(event.effectiveDirective);\n"
            + "    log(event.violatedDirective);\n"
            + "    log(event.originalPolicy);\n"
            + "    log(event.sourceFile);\n"
            + "    log(event.sample);\n"
            + "    log(event.disposition);\n"
            + "    log(event.statusCode);\n"
            + "    log(event.lineNumber);\n"
            + "    log(event.columnNumber);\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "true", "false", "true"},
            FF = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "false", "false", "false"},
            FF_ESR = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "false", "false", "false"})
    @HtmlUnitNYI(CHROME = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "false", "false", "false"},
            EDGE = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "false", "false", "false"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent('securitypolicyviolation');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    @HtmlUnitNYI(CHROME = {"[object SecurityPolicyViolationEvent]", "undefined", "false", "false", "false"},
            EDGE = {"[object SecurityPolicyViolationEvent]", "undefined", "false", "false", "false"},
            FF = {"[object SecurityPolicyViolationEvent]", "undefined", "false", "false", "false"},
            FF_ESR = {"[object SecurityPolicyViolationEvent]", "undefined", "false", "false", "false"})
    public void create_ctorWithoutType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent();\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object SecurityPolicyViolationEvent]", "42", "true", "false", "true"},
            FF = {"[object SecurityPolicyViolationEvent]", "42", "false", "false", "false"},
            FF_ESR = {"[object SecurityPolicyViolationEvent]", "42", "false", "false", "false"})
    @HtmlUnitNYI(CHROME = {"[object SecurityPolicyViolationEvent]", "42", "false", "false", "false"},
            EDGE = {"[object SecurityPolicyViolationEvent]", "42", "false", "false", "false"})
    public void create_ctorNumericType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent(42);\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object SecurityPolicyViolationEvent]", "null", "true", "false", "true"},
            FF = {"[object SecurityPolicyViolationEvent]", "null", "false", "false", "false"},
            FF_ESR = {"[object SecurityPolicyViolationEvent]", "null", "false", "false", "false"})
    @HtmlUnitNYI(CHROME = {"[object SecurityPolicyViolationEvent]", "null", "false", "false", "false"},
            EDGE = {"[object SecurityPolicyViolationEvent]", "null", "false", "false", "false"})
    public void create_ctorNullType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent(null);\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ReferenceError")
    public void create_ctorUnknownType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent(unknown);\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object SecurityPolicyViolationEvent]", "HtmlUnitEvent", "true", "false", "true"},
            FF = {"[object SecurityPolicyViolationEvent]", "HtmlUnitEvent", "false", "false", "false"},
            FF_ESR = {"[object SecurityPolicyViolationEvent]", "HtmlUnitEvent", "false", "false", "false"})
    @HtmlUnitNYI(CHROME = {"[object SecurityPolicyViolationEvent]", "HtmlUnitEvent", "false", "false", "false"},
            EDGE = {"[object SecurityPolicyViolationEvent]", "HtmlUnitEvent", "false", "false", "false"})
    public void create_ctorArbitraryType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent('HtmlUnitEvent');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "true", "false", "true"},
            FF = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "true", "true", "true"},
            FF_ESR = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "true", "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "true", "true", "true"},
            EDGE = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "true", "true", "true"})
    public void create_ctorAllDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent('securitypolicyviolation', {\n"
            + "        'bubbles': true,\n"
            + "        'cancelable': true,\n"
            + "        'composed': true\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "true", "false", "true"},
            FF = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "false", "false", "false"},
            FF_ESR = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "false", "false", "false"})
    @HtmlUnitNYI(CHROME = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "false", "false", "false"},
            EDGE = {"[object SecurityPolicyViolationEvent]", "securitypolicyviolation", "false", "false", "false"})
    public void create_ctorAllDetailsMissingData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent('securitypolicyviolation', {});\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NotSupportedError/DOMException")
    public void create_createEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('SecurityPolicyViolationEvent');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The violation properties of an event created without init object.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", "", "", "", "enforce", "0", "0", "0"})
    public void properties_ctorDefaults() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent('securitypolicyviolation');\n"
            + "      dumpProperties(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_PROPERTIES_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The violation properties of an event created with all init values.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://localhost/doc.html",
             "http://localhost/ref.html",
             "inline",
             "script-src-elem",
             "script-src",
             "script-src 'self'",
             "http://localhost/script.js",
             "alert(1)",
             "report",
             "404",
             "7",
             "13"})
    public void properties_ctorAllDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent('securitypolicyviolation', {\n"
            + "        'documentURI': 'http://localhost/doc.html',\n"
            + "        'referrer': 'http://localhost/ref.html',\n"
            + "        'blockedURI': 'inline',\n"
            + "        'effectiveDirective': 'script-src-elem',\n"
            + "        'violatedDirective': 'script-src',\n"
            + "        'originalPolicy': \"script-src 'self'\",\n"
            + "        'sourceFile': 'http://localhost/script.js',\n"
            + "        'sample': 'alert(1)',\n"
            + "        'disposition': 'report',\n"
            + "        'statusCode': 404,\n"
            + "        'lineNumber': 7,\n"
            + "        'columnNumber': 13\n"
            + "      });\n"
            + "      dumpProperties(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_PROPERTIES_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Conversion of the init values.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"42", "", "null", "", "", "", "", "", "enforce", "404", "-1", "3"},
            FF = {"42", "", "null", "", "", "", "", "", "enforce", "404", "4294967295", "3"},
            FF_ESR = {"42", "", "null", "", "", "", "", "", "enforce", "404", "4294967295", "3"})
    @HtmlUnitNYI(FF = {"42", "", "null", "", "", "", "", "", "enforce", "404", "-1", "3"},
            FF_ESR = {"42", "", "null", "", "", "", "", "", "enforce", "404", "-1", "3"})
    public void properties_ctorConversion() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent('securitypolicyviolation', {\n"
            + "        'documentURI': 42,\n"
            + "        'blockedURI': null,\n"
            + "        'sample': undefined,\n"
            + "        'disposition': 'enforce',\n"
            + "        'statusCode': '404',\n"
            + "        'lineNumber': -1,\n"
            + "        'columnNumber': 3.7\n"
            + "      });\n"
            + "      dumpProperties(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_PROPERTIES_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The disposition has to be 'enforce' or 'report'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void properties_ctorInvalidDisposition() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent('securitypolicyviolation', {\n"
            + "        'disposition': 'invalid'\n"
            + "      });\n"
            + "      log(event.disposition);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_PROPERTIES_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The violation properties are read only.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline", "200"})
    public void properties_readOnly() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new SecurityPolicyViolationEvent('securitypolicyviolation', {\n"
            + "        'blockedURI': 'inline',\n"
            + "        'statusCode': 200\n"
            + "      });\n"
            + "      event.blockedURI = 'eval';\n"
            + "      event.statusCode = 404;\n"
            + "      log(event.blockedURI);\n"
            + "      log(event.statusCode);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_PROPERTIES_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The violation properties are defined on the prototype.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"documentURI true",
             "referrer true",
             "blockedURI true",
             "effectiveDirective true",
             "violatedDirective true",
             "originalPolicy true",
             "sourceFile true",
             "sample true",
             "disposition true",
             "statusCode true",
             "lineNumber true",
             "columnNumber true"})
    public void properties_prototype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var props = ['documentURI', 'referrer', 'blockedURI', 'effectiveDirective',\n"
            + "                   'violatedDirective', 'originalPolicy', 'sourceFile', 'sample',\n"
            + "                   'disposition', 'statusCode', 'lineNumber', 'columnNumber'];\n"
            + "      for (var i = 0; i < props.length; i++) {\n"
            + "        log(props[i] + ' ' + SecurityPolicyViolationEvent.prototype.hasOwnProperty(props[i]));\n"
            + "      }\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_PROPERTIES_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The properties of the event fired for a blocked inline script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"securitypolicyviolation true false true",
             "SCRIPT",
             "http://localhost:22222/",
             "",
             "inline",
             "script-src-elem",
             "script-src-elem",
             "script-src 'nonce-test' 'report-sample'",
             "http://localhost:22222/",
             "log('inline');",
             "enforce",
             "200",
             "45",
             "9"})
    public void fired_inlineScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<meta http-equiv='Content-Security-Policy' content=\"script-src 'nonce-test' 'report-sample'\">\n"
            + "<script nonce='test'>\n"
            + LOG_TITLE_FUNCTION
            + DUMP_PROPERTIES_FUNCTION
            + "  document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "    log(e.type + ' ' + e.bubbles + ' ' + e.cancelable + ' ' + e.composed);\n"
            + "    log(e.target.nodeName);\n"
            + "    dumpProperties(e);\n"
            + "  });\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>log('inline');</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * The properties of the event fired for a blocked external script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"securitypolicyviolation true false true",
                    "#document",
                    "http://localhost:22222/",
                    "",
                    "http://localhost:22222/test.js",
                    "script-src-elem",
                    "script-src-elem",
                    "script-src 'nonce-test'",
                    "",
                    "",
                    "enforce",
                    "200",
                    "0",
                    "0"},
            FF = {"securitypolicyviolation true false true",
               "#document",
               "http://localhost:22222/",
               "",
               "http://localhost:22222/test.js",
               "script-src-elem",
               "script-src-elem",
               "script-src 'nonce-test'",
               "",
               "",
               "enforce",
               "200",
               "0",
               "1"},
            FF_ESR = {"securitypolicyviolation true false true",
                   "#document",
                   "http://localhost:22222/",
                   "",
                   "http://localhost:22222/test.js",
                   "script-src-elem",
                   "script-src-elem",
                   "script-src 'nonce-test'",
                   "",
                   "",
                   "enforce",
                   "200",
                   "0",
                   "1"})
    @HtmlUnitNYI(FF = {"securitypolicyviolation true false true",
                  "#document",
                  "http://localhost:22222/",
                  "",
                  "http://localhost:22222/test.js",
                  "script-src-elem",
                  "script-src-elem",
                  "script-src 'nonce-test'",
                  "",
                  "",
                  "enforce",
                  "200",
                  "0",
                  "0"},
            FF_ESR = {"securitypolicyviolation true false true",
                      "#document",
                      "http://localhost:22222/",
                      "",
                      "http://localhost:22222/test.js",
                      "script-src-elem",
                      "script-src-elem",
                      "script-src 'nonce-test'",
                      "",
                      "",
                      "enforce",
                      "200",
                      "0",
                      "0"})
    public void fired_externalScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<meta http-equiv='Content-Security-Policy' content=\"script-src 'nonce-test'\">\n"
            + "<script nonce='test'>\n"
            + LOG_TITLE_FUNCTION
            + DUMP_PROPERTIES_FUNCTION
            + "  document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "    log(e.type + ' ' + e.bubbles + ' ' + e.cancelable + ' ' + e.composed);\n"
            + "    log(e.target.nodeName);\n"
            + "    dumpProperties(e);\n"
            + "  });\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script src='test.js'></script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * The properties of the event fired for a blocked eval().
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"EvalError",
             "securitypolicyviolation true false true",
             "#document",
             "http://localhost:22222/",
             "",
             "eval",
             "script-src",
             "script-src",
             "script-src 'nonce-test' 'report-sample'",
             "http://localhost:22222/",
             "1 + 1",
             "enforce",
             "200",
             "42",
             "9"})
    @HtmlUnitNYI(CHROME = {"EvalError",
                          "securitypolicyviolation true false true",
                          "#document",
                          "http://localhost:22222/",
                          "",
                          "eval",
                          "script-src",
                          "script-src",
                          "script-src 'nonce-test' 'report-sample'",
                          "http://localhost:22222/",
                          "1 + 1",
                          "enforce",
                          "200",
                          "42",
                          "0"},
            EDGE = {"EvalError",
                    "securitypolicyviolation true false true",
                    "#document",
                    "http://localhost:22222/",
                    "",
                    "eval",
                    "script-src",
                    "script-src",
                    "script-src 'nonce-test' 'report-sample'",
                    "http://localhost:22222/",
                    "1 + 1",
                    "enforce",
                    "200",
                    "42",
                    "0"},
            FF = {"EvalError",
                  "securitypolicyviolation true false true",
                  "#document",
                  "http://localhost:22222/",
                  "",
                  "eval",
                  "script-src",
                  "script-src",
                  "script-src 'nonce-test' 'report-sample'",
                  "http://localhost:22222/",
                  "1 + 1",
                  "enforce",
                  "200",
                  "42",
                  "0"},
            FF_ESR = {"EvalError",
                      "securitypolicyviolation true false true",
                      "#document",
                      "http://localhost:22222/",
                      "",
                      "eval",
                      "script-src",
                      "script-src",
                      "script-src 'nonce-test' 'report-sample'",
                      "http://localhost:22222/",
                      "1 + 1",
                      "enforce",
                      "200",
                      "42",
                      "0"})
    public void fired_eval() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<meta http-equiv='Content-Security-Policy' content=\"script-src 'nonce-test' 'report-sample'\">\n"
            + "<script nonce='test'>\n"
            + LOG_TITLE_FUNCTION
            + DUMP_PROPERTIES_FUNCTION
            + "  document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "    log(e.type + ' ' + e.bubbles + ' ' + e.cancelable + ' ' + e.composed);\n"
            + "    log(e.target.nodeName);\n"
            + "    dumpProperties(e);\n"
            + "  });\n"
            + "  try { eval('1 + 1'); } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * The properties of the event fired for a blocked event handler attribute.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"securitypolicyviolation true false true",
                    "BUTTON",
                    "http://localhost:22222/",
                    "",
                    "inline",
                    "script-src-attr",
                    "script-src-attr",
                    "script-src 'nonce-test' 'report-sample'",
                    "",
                    "log('click')",
                    "enforce",
                    "200",
                    "0",
                    "0"},
            FF = {"securitypolicyviolation true false true",
               "BUTTON",
               "http://localhost:22222/",
               "",
               "inline",
               "script-src-attr",
               "script-src-attr",
               "script-src 'nonce-test' 'report-sample'",
               "http://localhost:22222/",
               "log('click')",
               "enforce",
               "200",
               "0",
               "1"},
            FF_ESR = {"securitypolicyviolation true false true",
                   "BUTTON",
                   "http://localhost:22222/",
                   "",
                   "inline",
                   "script-src-attr",
                   "script-src-attr",
                   "script-src 'nonce-test' 'report-sample'",
                   "http://localhost:22222/",
                   "log('click')",
                   "enforce",
                   "200",
                   "0",
                   "1"})
    @HtmlUnitNYI(FF = {"securitypolicyviolation true false true",
                  "BUTTON",
                  "http://localhost:22222/",
                  "",
                  "inline",
                  "script-src-attr",
                  "script-src-attr",
                  "script-src 'nonce-test' 'report-sample'",
                  "",
                  "log('click')",
                  "enforce",
                  "200",
                  "0",
                  "0"},
            FF_ESR = {"securitypolicyviolation true false true",
                      "BUTTON",
                      "http://localhost:22222/",
                      "",
                      "inline",
                      "script-src-attr",
                      "script-src-attr",
                      "script-src 'nonce-test' 'report-sample'",
                      "",
                      "log('click')",
                      "enforce",
                      "200",
                      "0",
                      "0"})
    public void fired_eventHandlerAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<meta http-equiv='Content-Security-Policy' content=\"script-src 'nonce-test' 'report-sample'\">\n"
            + "<script nonce='test'>\n"
            + LOG_TITLE_FUNCTION
            + DUMP_PROPERTIES_FUNCTION
            + "  document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "    log(e.type + ' ' + e.bubbles + ' ' + e.cancelable + ' ' + e.composed);\n"
            + "    log(e.target.nodeName);\n"
            + "    dumpProperties(e);\n"
            + "  });\n"
            + "  function test() { document.getElementById('btn').click(); }\n"
            + "  window.addEventListener('load', test);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<button id='btn' onclick=\"log('click')\">click</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void inWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('SecurityPolicyViolationEvent' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
