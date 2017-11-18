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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF45;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link URLSearchParams}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class URLSearchParamsTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "foo=1&bar=2", ""},
            FF45 = { "%3Ffoo=1&bar=2", ""},
            IE = {})
    @NotYetImplemented(FF45)
    public void ctor() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        alert(new URLSearchParams('?foo=1&bar=2'));\n"
            + "        alert(new URLSearchParams());\n"
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
    @Alerts(DEFAULT = { "key=value", "key=value&empty-key=undefined",
                        "key=value&empty-key=undefined&key=overwrite"},
            IE = {})
    public void append() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams();\n"
            + "        param.append('key', 'value');\n"
            + "        alert(param);\n"
            + "        param.append('empty-key', undefined);\n"
            + "        alert(param);\n"
            + "        param.append('key', 'overwrite');\n"
            + "        alert(param);\n"
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
    @Alerts(DEFAULT = { "key2=val2", "key2=val2", "key2=val2"},
            IE = {})
    public void delete() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2');\n"
            + "        param.delete('key1');\n"
            + "        alert(param);\n"
            + "        param.delete('key3');\n"
            + "        alert(param);\n"
            + "        param.delete('key1');\n"
            + "        alert(param);\n"
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
    @Alerts(DEFAULT = { "val1", "val2", "null"},
            IE = {})
    public void get() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2&key1=val3');\n"
            + "        alert(param.get('key1'));\n"
            + "        alert(param.get('key2'));\n"
            + "        alert(param.get('key3'));\n"
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
    @Alerts(DEFAULT = { "true", "true", "false"},
            IE = {})
    public void has() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2&key1=val3');\n"
            + "        alert(param.has('key1'));\n"
            + "        alert(param.has('key2'));\n"
            + "        alert(param.has('key3'));\n"
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
    @Alerts(DEFAULT = { "val1,val3", "val2", ""},
            IE = {})
    public void getAll() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2&key1=val3');\n"
            + "        alert(param.getAll('key1'));\n"
            + "        alert(param.getAll('key2'));\n"
            + "        alert(param.getAll('key3'));\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
