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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Promise}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class PromiseTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function, undefined, undefined, function",
            IE = "")
    public void staticMethods() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        alert(typeof Promise.resolve);\n"
            + "        alert(typeof Promise.then);\n"
            + "        var p = Promise.resolve('something');\n"
            + "        alert(typeof p.resolve);\n"
            + "        alert(typeof p.then);\n"
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
    @Alerts(DEFAULT = "Success",
            IE = "")
    public void resolve() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        Promise.resolve('Success').then(function(value) {\n"
            + "            alert(value);\n"
            + "        }, function(value) {\n"
            + "            alert('failure');\n"
            + "        });\n"
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
    @Alerts(DEFAULT = "undefined",
            IE = "")
    public void resolveEmpty() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        Promise.resolve().then(function(value) {\n"
            + "            alert(value);\n"
            + "        }, function(value) {\n"
            + "            alert('failure');\n"
            + "        });\n"
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
            IE = "")
    public void resolveArray() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var p = Promise.resolve([1,2,3]);\n"
            + "        p.then(function(v) {\n"
            + "            alert(v[0]);\n"
            + "        });\n"
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
    @Alerts(DEFAULT = "true",
            IE = "")
    public void resolvePromise() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (window.Promise) {\n"
            + "        var original = Promise.resolve(true);\n"
            + "        var cast = Promise.resolve(original);\n"
            + "        cast.then(function(v) {\n"
            + "          alert(v);\n"
            + "        });\n"
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
    @Alerts(DEFAULT = "true, fulfilled!, TypeError: Throwing, Resolving",
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
            + "        alert(p1 instanceof Promise);\n"
            + "\n"
            + "        p1.then(function(v) {\n"
            + "            alert(v);\n"
            + "        }, function(e) {\n"
            + "            alert('failure');\n"
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
            + "          alert('failure');\n"
            + "        }, function(e) {\n"
            + "          alert(e);\n"
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
            + "          alert(v);\n"
            + "        }, function(e) {\n"
            + "          alert('failure');\n"
            + "        });\n"
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
    @Alerts(DEFAULT = "1, 2",
            IE = "")
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
            + "          alert(value);\n"
            + "          return value + 1;\n"
            + "        }).then(function(value) {\n"
            + "          alert(value);\n"
            + "        });\n"
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
    @Alerts(DEFAULT = { "end", "in then" },
            IE = "exception")
    public void then() throws Exception {
        final String html = "<html><body><script>"
            + "try {\n"
            + "  var p = Promise.resolve(void 0);\n"
            + "  p.then(function() { alert('in then'); });\n"
            + "  alert('end');\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
