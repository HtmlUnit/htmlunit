/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Promise}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Madis Pärn
 * @author Ronald Brill
 * @author Rural Hunter
 */
@RunWith(BrowserRunner.class)
public class PromiseTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"function", "function", "undefined", "undefined",
                "undefined", "undefined", "function", "function"},
            IE = {})
    public void staticMethods() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        alert(typeof Promise.resolve);\n"
            + "        alert(typeof Promise.reject);\n"
            + "        alert(typeof Promise.then);\n"
            + "        alert(typeof Promise.catch);\n"
            + "        var p = Promise.resolve('something');\n"
            + "        alert(typeof p.resolve);\n"
            + "        alert(typeof p.reject);\n"
            + "        alert(typeof p.then);\n"
            + "        alert(typeof p.catch);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = {})
    public void length() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        log(Promise.length);\n"
            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "function () { [native code] }",
                        "function () { [native code] }",
                        "[object Window]",
                        "done", "resolved value"},
            FF = { "function () {\n    [native code]\n}",
                       "function () {\n    [native code]\n}",
                       "[object Window]",
                       "done", "resolved value"},
            FF78 = { "function () {\n    [native code]\n}",
                        "function () {\n    [native code]\n}",
                        "[object Window]",
                        "done", "resolved value"},
            IE = {})
    public void constructor() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "          log(resolve);\n"
                + "          log(reject);\n"
                + "          log(this);\n"
                + "          resolve('resolved value');\n"
                + "        });\n"
                + "        p.then(function(value) {log(value);});\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true"},
            IE = {})
    public void constructorWithoutFunction() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        try{\n"
                + "          var p = new Promise();\n"
                + "          log('done');\n"
                + "        } catch(e) { log(e instanceof TypeError); }\n"

                + "        try{\n"
                + "          var p = new Promise([1, 2, 4]);\n"
                + "          log('done');\n"
                + "        } catch(e) { log(e instanceof TypeError); }\n"

                + "        try{\n"
                + "          var original = Promise.resolve(42);\n"
                + "          var p = new Promise(original);\n"
                + "          log('done');\n"
                + "        } catch(e) { log(e instanceof TypeError); }\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "Rejected"},
            IE = {})
    public void reject() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        Promise.reject('Rejected').then(function(value) {\n"
            + "            log('failure');\n"
            + "        }, function(value) {\n"
            + "            log(value);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "false", "[object Promise]"},
            IE = {})
    public void rejectPromise() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var original = Promise.reject(42);\n"
            + "        var cast = Promise.reject(original);\n"
            + "        cast.then(function(v) {\n"
            + "          log('failure');\n"
            + "        }, function(value) {\n"
            + "          log(value);\n"
            + "        });\n"
            + "        log('done');\n"
            + "        log(original === cast);\n"
            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "Resolved"},
            IE = {})
    public void resolve() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        Promise.resolve('Resolved').then(function(value) {\n"
            + "            log(value);\n"
            + "        }, function(value) {\n"
            + "            log('failure');\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "undefined"},
            IE = {})
    public void resolveEmpty() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        Promise.resolve().then(function(value) {\n"
            + "            log(value);\n"
            + "        }, function(value) {\n"
            + "            log('failure');\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "1"},
            IE = {})
    public void resolveArray() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p = Promise.resolve([1,2,3]);\n"
            + "        p.then(function(v) {\n"
            + "            log(v[0]);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "HtmlUnit"},
            IE = {})
    public void resolveString() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p = Promise.resolve('HtmlUnit');\n"
            + "        p.then(function(v) {\n"
            + "            log(v);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "true", "42"},
            IE = {})
    public void resolvePromise() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var original = Promise.resolve(42);\n"
            + "        var cast = Promise.resolve(original);\n"
            + "        cast.then(function(v) {\n"
            + "          log(v);\n"
            + "        });\n"
            + "        log('done');\n"
            + "        log(original === cast);\n"
            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "fulfilled!"},
            IE = "")
    public void resolveThenable() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"

            + "        var p1 = Promise.resolve({\n"
            + "          then: function(onFulfill, onReject) {\n"
            + "            onFulfill('fulfilled!');\n"
            + "          }\n"
            + "        });\n"
            + "        log(p1 instanceof Promise);\n"
            + "\n"
            + "        p1.then(function(v) {\n"
            + "            log(v);\n"
            + "        }, function(e) {\n"
            + "            log('failure');\n"
            + "        });\n"

            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "aaa"},
            IE = "")
    public void resolveThenablePrototype() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        function MyThenable() {\n"
            + "          this.value = 'aaa';\n"
            + "        };"
            + "        MyThenable.prototype = { then: function(onFulfill, onReject) { onFulfill(this.value); }};\n"
            + "        var thenable = new MyThenable();\n"
            + "        var p1 = Promise.resolve(1);\n"
            + "        log(p1 instanceof Promise);\n"
            + "\n"
            + "        p1=p1.then(function(v) {\n"
            + "            return thenable;\n"
            + "        }, function(e) {\n"
            + "            log('failure');\n"
            + "        });\n"
            + "\n"
            + "        p1=p1.then(function(v) {\n"
            + "            log(v);\n"
            + "        }, function(e) {\n"
            + "            log('failure');\n"
            + "        });\n"

            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "TypeError: Throwing 1",
            IE = "")
    public void resolveThenableThrows() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"

            + "        var thenable = {\n"
            + "          then: function(resolve) {\n"
            + "            throw new TypeError('Throwing 1');\n"
            + "            resolve(\"Resolving\");\n"
            + "          }\n"
            + "        };\n"
            + "\n"
            + "        var p2 = Promise.resolve(thenable);\n"
            + "        p2.then(function(v) {\n"
            + "          log('failure');\n"
            + "        }, function(e) {\n"
            + "          log(e);\n"
            + "        });\n"

            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Resolving",
            IE = "")
    public void resolveThenableThrowsAfterCallback() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"

            + "        var thenable = {\n"
            + "          then: function(resolve) {\n"
            + "            resolve('Resolving');\n"
            + "            throw new TypeError('Throwing 2');\n"
            + "          }\n"
            + "        };\n"
            + "\n"
            + "        var p3 = Promise.resolve(thenable);\n"
            + "        p3.then(function(v) {\n"
            + "          log(v);\n"
            + "        }, function(e) {\n"
            + "          log('failure');\n"
            + "        });\n"
            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "[object Object]"},
            IE = "")
    public void resolveThenablesWithoutThen() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = Promise.resolve({ id: 17 });\n"
            + "        log(p1 instanceof Promise);\n"
            + "\n"
            + "        p1.then(function(v) {\n"
            + "            log(v);\n"
            + "        }, function(e) {\n"
            + "            log('failure');\n"
            + "        });\n"
            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "1", "2"},
            IE = {})
    public void thenChanining() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p = new Promise(function(resolve, reject) {\n"
            + "          resolve(1);\n"
            + "        });\n"
            + "\n"
            + "        p.then(function(value) {\n"
            + "          log(value);\n"
            + "          return value + 1;\n"
            + "        }).then(function(value) {\n"
            + "          log(value);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "undefined"},
            IE = {})
    public void then() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = Promise.resolve(void 0);\n"
                + "        p.then(function(value) {\n"
                + "          log(value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "undefined"},
            IE = {})
    public void thenAsync() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve(void 0);\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        p.then(function(value) {\n"
                + "          log(value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "1 yes", "2 yes"},
            IE = {})
    public void thenTwice() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = Promise.resolve('yes');\n"
                + "        p.then(function(value) {\n"
                + "          log('1 ' + value);\n"
                + "        })\n"
                + "        p.then(function(value) {\n"
                + "          log('2 ' + value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "1 yes", "2 yes"},
            IE = {})
    public void thenTwiceAsync() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve('yes');\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        p.then(function(value) {\n"
                + "          log('1 ' + value);\n"
                + "        })\n"
                + "        p.then(function(value) {\n"
                + "          log('2 ' + value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "first", "second"},
            IE = {})
    public void thenAsyncPromiseResolved() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve('first');\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        var p2 = p.then(function(value) {\n"
                + "          log(value);\n"
                + "          return Promise.resolve('second');"
                + "        })\n"
                + "        p2.then(function(value) {\n"
                + "          log(value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "first", "second"},
            IE = {})
    public void thenAsyncPromiseRejected() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve('first');\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        var p2 = p.then(function(value) {\n"
                + "          log(value);\n"
                + "          return Promise.reject('second');"
                + "        })\n"
                + "        p2.then(function(value) {\n"
                + "          log('Failure');\n"
                + "        },"
                + "        function(value) {\n"
                + "          log(value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "first", "second"},
            IE = {})
    public void thenAsyncPromiseAsyncResolved() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve('first');\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        var p2 = p.then(function(value) {\n"
                + "          log(value);\n"
                + "          return new Promise(function(resolve, reject) {\n"
                + "             window.setTimeout( function() {\n"
                + "               resolve('second');\n"
                + "             }, 20);\n"
                + "          })\n"
                + "        })\n"
                + "        p2.then(function(value) {\n"
                + "          log(value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "first", "second"},
            IE = {})
    public void thenAsyncPromiseAsyncRejected() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve('first');\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        var p2 = p.then(function(value) {\n"
                + "          log(value);\n"
                + "          return new Promise(function(resolve, reject) {\n"
                + "             window.setTimeout( function() {\n"
                + "               reject('second');\n"
                + "             }, 20);\n"
                + "          })\n"
                + "        })\n"
                + "        p2.then(function(value) {\n"
                + "          log('Failure');\n"
                + "        },"
                + "        function(value) {\n"
                + "          log(value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "Success first"},
            IE = {})
    public void thenTestAsyncChainedResolve() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve('first');\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        var p2 = p.then(undefined, function(value) {\n"
                + "          log(value);\n"
                + "        })\n"
                + "        p2.then(function(value) {\n"
                + "          log('Success ' + value);\n"
                + "        },"
                + "        function(value) {\n"
                + "          log('Failure ' + value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "Failure first"},
            IE = {})
    public void thenTestAsyncChainedReject() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             reject('first');\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        var p2 = p.then(function(value) {\n"
                + "          log(value);\n"
                + "        }, undefined)\n"
                + "        p2.then(function(value) {\n"
                + "          log('Success ' + value);\n"
                + "        },"
                + "        function(value) {\n"
                + "          log('Failure ' + value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "Success first"},
            IE = {})
    public void thenTestAsyncChainedNotAFunction() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve('first');\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        var p2 = p.then(7);\n"
                + "        var p3 = p2.then('test');\n"
                + "        p3.then(function(value) {\n"
                + "          log('Success ' + value);\n"
                + "        },"
                + "        function(value) {\n"
                + "          log('Failure ' + value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"Success", "string", "oh, no!", "after catch"},
            IE = {})
    public void catchTest() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "          resolve('Success');\n"
                + "        });\n"
                + "\n"
                + "        p.then(function(value) {\n"
                + "          log(value);\n"
                + "          throw 'oh, no!';\n"
                + "        }).catch(function(e) {\n"
                + "          log(typeof e);\n"
                + "          log(e);\n"
                + "        }).then(function(e) {\n"
                + "          log('after catch');\n"
                + "        }, function() {\n"
                + "          log('failure');\n"
                + "        });\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"Success", "string", "oh, no!", "after catch"},
            IE = {})
    public void catchTestAsync() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve('Success');\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        p.then(function(value) {\n"
                + "          log(value);\n"
                + "          throw 'oh, no!';\n"
                + "        }).catch(function(e) {\n"
                + "          log(typeof e);\n"
                + "          log(e);\n"
                + "        }).then(function(e) {\n"
                + "          log('after catch');\n"
                + "        }, function() {\n"
                + "          log('failure');\n"
                + "        });\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "Success first"},
            IE = {})
    public void catchTestAsyncChained() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        var p = new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve('first');\n"
                + "           }, 20);\n"
                + "        })\n"
                + "        var p2 = p.catch(function(value) {\n"
                + "          log(value);\n"
                + "        })\n"
                + "        p2.then(function(value) {\n"
                + "          log('Success ' + value);\n"
                + "        },"
                + "        function(value) {\n"
                + "          log('Failure ' + value);\n"
                + "        })\n"
                + "        log('done');\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "Success"},
            IE = "")
    public void thenInsideEventHandler() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if (window.Promise) {\n"
                + "        document.getElementById('btn1').onclick = function() {\n"
                + "          new Promise(function(resolve, reject) {\n"
                + "            window.setTimeout( function() {\n"
                + "              resolve('Success');\n"
                + "            }, 20);\n"
                + "          }).then(function(value) {\n"
                + "            log(value);\n"
                + "          });\n"
                + "          log('done');\n"
                + "        };\n"
                + "      }\n"
                + "    }\n"
                + "\n"
                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <button id='btn1'>BTN1</button>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>\n";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("btn1")).click();

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"create thenable 4", "fulfilled"},
            IE = "")
    public void thenThenable() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"

            + "        var p1 = Promise.resolve(4);\n"
            + "        p2 = p1.then(function(v) {\n"
            + "          log('create thenable ' + v);\n"
            + "          return { then: function(onFulfill, onReject) { onFulfill('fulfilled'); }};\n"
            + "        });\n"
            + "\n"
            + "        p2.then(function(v) {\n"
            + "            log(v);\n"
            + "        }, function(e) {\n"
            + "            log('failure');\n"
            + "        });\n"

            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "failure1",
            IE = "")
    public void thenThenableThrows() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"

            + "        var thenable = {\n"
            + "          then: function(resolve) {\n"
            + "            throw new TypeError('Throwing 1');\n"
            + "            resolve(\"Resolving\");\n"
            + "          }\n"
            + "        };\n"
            + "\n"
            + "        var p1 = Promise.resolve(1);\n"
            + "        p2 = p1.then(thenable);\n"

            + "        p2.then(function(v) {\n"
            + "          log('failure' + v);\n"
            + "        }, function(e) {\n"
            + "          log(e);\n"
            + "        });\n"

            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "failure1"},
            IE = "")
    public void thenNotThenable() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"

            + "        var elseable = {\n"
            + "          else: function(resolve) {\n"
            + "            resolve(\"Resolving\");\n"
            + "          }\n"
            + "        };\n"
            + "\n"
            + "        var p1 = Promise.resolve(1);\n"
            + "        try{\n"
            + "          p2 = p1.then(elseable);\n"
            + "          log('done');\n"
            + "        } catch(e) { log(e instanceof TypeError); }\n"

            + "        p2.then(function(v) {\n"
            + "          log('failure' + v);\n"
            + "        }, function(e) {\n"
            + "          log(e);\n"
            + "        });\n"

            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "")
    public void thenThenableThrowsAfterCallback() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"

            + "        var thenable = {\n"
            + "          then: function(resolve) {\n"
            + "            resolve('Resolving');\n"
            + "            throw new TypeError('Throwing 2');\n"
            + "          }\n"
            + "        };\n"
            + "\n"
            + "        var p1 = Promise.resolve(1);\n"
            + "        p2 = p1.then(thenable);\n"

            + "        p2.then(function(v) {\n"
            + "          log(v);\n"
            + "        }, function(e) {\n"
            + "          log('failure');\n"
            + "        });\n"
            + "      }\n"
            + "    }\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "object", "3", "3,1337,Success"},
            IE = {})
    public void allAsyncArray() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = Promise.resolve(3);\n"
            + "        var p2 = 1337;\n"
            + "        var p3 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              resolve('Success');\n"
            + "            }, 20);\n"
            + "        });\n"
            + "\n"
            + "        Promise.all([p1, p2, p3]).then(function(values) {\n"
            + "          log(typeof values);\n"
            + "          log(values.length);\n"
            + "          log(values);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "first 3,1337,Success", "second 3,Success"},
            IE = {})
    public void allAsyncArray2() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = Promise.resolve(3);\n"
            + "        var p2 = 1337;\n"
            + "        var p3 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              resolve('Success');\n"
            + "            }, 20);\n"
            + "        });\n"
            + "\n"
            + "        Promise.all([p1, p2, p3]).then(function(values) {\n"
            + "          log('first ' + values);\n"
            + "        });\n"
            + "        Promise.all([p1, p3]).then(function(values) {\n"
            + "          log('second ' + values);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "object", "3", "3,1337,Success"},
            IE = {})
    public void allAsyncSet() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = Promise.resolve(3);\n"
            + "        var p2 = 1337;\n"
            + "        var p3 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              resolve('Success');\n"
            + "            }, 20);\n"
            + "        });\n"
            + "\n"
            + "        Promise.all(new Set([p1, p2, p3])).then(function(values) {\n"
            + "          log(typeof values);\n"
            + "          log(values.length);\n"
            + "          log(values);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "string", "Failed"},
            IE = {})
    public void allRejectAsync() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = Promise.resolve(3);\n"
            + "        var p2 = 1337;\n"
            + "        var p3 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              reject('Failed');\n"
            + "            }, 20);\n"
            + "        });\n"
            + "\n"
            + "        Promise.all([p1, p2, p3]).then(function(value) {\n"
            + "            log('failure');\n"
            + "        }, function(value) {\n"
            + "          log(typeof value);\n"
            + "          log(value);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "first Failed", "second Failed"},
            IE = {})
    public void allRejectAsync2() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = Promise.resolve(3);\n"
            + "        var p2 = 1337;\n"
            + "        var p3 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              reject('Failed');\n"
            + "            }, 20);\n"
            + "        });\n"
            + "\n"
            + "        Promise.all([p1, p2, p3]).then(function(value) {\n"
            + "            log('failure');\n"
            + "        }, function(value) {\n"
            + "          log('first ' + value);\n"
            + "        });\n"
            + "        Promise.all([p1, p3]).then(function(value) {\n"
            + "            log('failure');\n"
            + "        }, function(value) {\n"
            + "          log('second ' + value);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "Success 2"},
            IE = {})
    public void raceAsync() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              resolve('Success');\n"
            + "            }, 40);\n"
            + "        });\n"
            + "        var p2 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              resolve('Success 2');\n"
            + "            }, 20);\n"
            + "        });\n"
            + "\n"
            + "        Promise.race([p1, p2]).then(function(value) {\n"
            + "          log(value);\n"
            + "        }, function(value) {\n"
            + "          log('failure');\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "first Success 2", "second Success 2"},
            IE = {})
    public void raceAsync2() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              resolve('Success');\n"
            + "            }, 40);\n"
            + "        });\n"
            + "        var p2 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              resolve('Success 2');\n"
            + "            }, 20);\n"
            + "        });\n"
            + "\n"
            + "        Promise.race([p1, p2]).then(function(value) {\n"
            + "          log('first ' + value);\n"
            + "        }, function(value) {\n"
            + "          log('failure');\n"
            + "        });\n"
            + "        Promise.race([p1, p2]).then(function(value) {\n"
            + "          log('second ' + value);\n"
            + "        }, function(value) {\n"
            + "          log('failure');\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "Failed"},
            IE = {})
    public void raceRejectAsync() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              resolve('Success');\n"
            + "            }, 40);\n"
            + "        });\n"
            + "        var p2 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              reject('Failed');\n"
            + "            }, 20);\n"
            + "        });\n"
            + "\n"
            + "        Promise.race([p1, p2]).then(function(value) {\n"
            + "          log('failure');\n"
            + "        }, function(value) {\n"
            + "          log(value);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"done", "first Failed", "second Failed"},
            IE = {})
    public void raceRejectAsync2() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              resolve('Success');\n"
            + "            }, 40);\n"
            + "        });\n"
            + "        var p2 = new Promise(function(resolve, reject) {\n"
            + "            window.setTimeout( function() {\n"
            + "              reject('Failed');\n"
            + "            }, 20);\n"
            + "        });\n"
            + "\n"
            + "        Promise.race([p1, p2]).then(function(value) {\n"
            + "          log('failure');\n"
            + "        }, function(value) {\n"
            + "          log('first ' + value);\n"
            + "        });\n"
            + "        Promise.race([p1, p2]).then(function(value) {\n"
            + "          log('failure');\n"
            + "        }, function(value) {\n"
            + "          log('second ' + value);\n"
            + "        });\n"
            + "        log('done');\n"
            + "      }\n"
            + "    }\n"
            + "\n"
            + "    function log(x) {\n"
            + "      document.getElementById('log').value += x + '\\n';\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body>\n"
            + "</html>\n";

        final WebDriver driver = loadPage2(html);

        verifyAlerts(() -> driver.findElement(By.id("log"))
                .getAttribute("value").trim().replaceAll("\r", ""), String.join("\n", getExpectedAlerts()));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            IE = "test")
    public void changeLocationFromPromise() throws Exception {
        final String html =
                "<html>\n"
              + "<head>\n"
              + "  <title>test</title>\n"
              + "  <script>\n"
              + "    function test() {\n"
              + "      if (window.Promise) {\n"
              + "        Promise.resolve(1).then(function () {\n"
              + "          location.href = 'about:blank';\n"
              + "        });\n"
              + "      }\n"
              + "    }\n"
              + "  </script>\n"
              + "</head>\n"
              + "<body onload='test()'>\n"
              + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
              + "</body>\n"
              + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }
}
