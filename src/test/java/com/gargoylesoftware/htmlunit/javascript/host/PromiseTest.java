/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Promise}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Madis Pärn
 * @author Ronald Brill
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "fulfilled!", "TypeError: Throwing", "Resolving"},
            IE = "")
    public void resolveThenables() throws Exception {
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
            + "\n"
            + "        var thenable = {\n"
            + "          then: function(resolve) {\n"
            + "            throw new TypeError('Throwing');\n"
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
            + "\n"
            + "        var thenable = {\n"
            + "          then: function(resolve) {\n"
            + "            resolve('Resolving');\n"
            + "            throw new TypeError('Throwing');\n"
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
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
                + "\n"
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Success",
            IE = "")
    public void thenInsideEventHandler() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      if(!window.Promise) {\n"
                + "        return;\n"
                + "      }\n"
                + "      document.getElementById('btn1').onclick = function() {\n"
                + "        new Promise(function(resolve, reject) {\n"
                + "           window.setTimeout( function() {\n"
                + "             resolve('Success');\n"
                + "           }, 0);\n"
                + "        }).then(function(value) {\n"
                + "          document.title += value;\n"
                + "        });\n"
                + "      };\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "<button id='btn1'>BTN1</button>\n"
                + "</body>\n"
                + "</html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("btn1")).click();

        Thread.sleep(200);
        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

    /**
     * Test for Bug #1780.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"object", "3", "3,1337,foo"},
            IE = {})
    @NotYetImplemented({CHROME, FF})
    public void all() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p1 = Promise.resolve(3);\n"
            + "        var p2 = 1337;\n"
            + "        var p3 = new Promise(function(resolve, reject) {\n"
            + "          setTimeout(resolve, 100, \"foo\");\n"
            + "        });\n"
            + "\n"
            + "        Promise.all([ p1, p2, p3 ]).then(function(values) {\n"
            + "          log(typeof values);\n"
            + "          log(values.length);\n"
            + "          log(values);\n"
            + "        });\n"
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
        Thread.sleep(200);
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = { "function () { [native code] }",
                        "function () { [native code] }",
                        "[object Window]",
                        "done", "resolved value"},
            FF = { "function () {\n    [native code]\n}",
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
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
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }
}
