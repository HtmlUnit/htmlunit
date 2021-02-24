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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
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
    @Alerts(DEFAULT = {"foo=1&bar=2", ""},
            IE = {})
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
    @Alerts(DEFAULT = {"", "foo=1&bar=2", "q=Html+Unit&unml=%C3%A4%C3%9C", "q=HtmlUnit&u=%3F%3F"},
            IE = {})
    public void string() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        alert(new URLSearchParams());\n"
            + "        alert(new URLSearchParams('?foo=1&bar=2'));\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('q', 'Html Unit');\n"
            + "        searchParams.append('unml', '\u00E4\u00DC');\n"
            + "        alert(searchParams);\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('q', 'HtmlUnit');\n"
            + "        searchParams.append('u', '\u043B\u0189');\n"
            + "        alert(searchParams);\n"
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
    @Alerts(DEFAULT = {"=emptyKey", "null=nullKey", "undefined=undefinedKey"},
            IE = {})
    public void stringKeys() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('', 'emptyKey');\n"
            + "        alert(searchParams);\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append(null, 'nullKey');\n"
            + "        alert(searchParams);\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append(undefined, 'undefinedKey');\n"
            + "        alert(searchParams);\n"
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
    @Alerts(DEFAULT = {"emptyValue=", "nullValue=null", "undefinedValue=undefined"},
            IE = {})
    public void stringValues() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('emptyValue', '');\n"
            + "        alert(searchParams);\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('nullValue', null);\n"
            + "        alert(searchParams);\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('undefinedValue', undefined);\n"
            + "        alert(searchParams);\n"
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
    @Alerts(DEFAULT = "exception param",
            IE = {})
    @HtmlUnitNYI(CHROME = "noValue=undefined",
            EDGE = "noValue=undefined",
            FF = "noValue=undefined",
            FF78 = "noValue=undefined")
    public void stringMissingParam() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        try {\n"
            + "          searchParams = new URLSearchParams();\n"
            + "          searchParams.append('noValue');\n"
            + "          alert(searchParams);\n"
            + "        } catch(e) { alert('exception param'); }\n"
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
    @Alerts(DEFAULT = {"key=value", "key=value&empty-key=undefined",
                        "key=value&empty-key=undefined&key=overwrite",
                        "key=value&empty-key=undefined&key=overwrite&key-null=null"},
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
            + "        param.append('key-null', null);\n"
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
    @Alerts(DEFAULT = {"key2=val2&key2=val3", "", "", "", ""},
            IE = {})
    public void delete() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2&key2=val3');\n"
            + "        param.delete('key1');\n"
            + "        alert(param);\n"
            + "        param.delete('key2');\n"
            + "        alert(param);\n"
            + "        param.delete('key3');\n"
            + "        alert(param);\n"
            + "        param.delete(undefined);\n"
            + "        alert(param);\n"
            + "        param.delete(null);\n"
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
    @Alerts(DEFAULT = {"val1", "val2", "null", "null", "null"},
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
            + "        alert(param.get(undefined));\n"
            + "        alert(param.get(null));\n"
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
    @Alerts(DEFAULT = {"true", "true", "false", "false", "false"},
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
            + "        alert(param.has(undefined));\n"
            + "        alert(param.has(null));\n"
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
    @Alerts(DEFAULT = {"val1,val3", "val2", "", "", ""},
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
            + "        alert(param.getAll(undefined));\n"
            + "        alert(param.getAll(null));\n"
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
    @Alerts(DEFAULT = {"key1=val1&key2=val2&key2=val3&key4=val4",
                        "key1=new1&key2=val2&key2=val3&key4=val4",
                        "key1=new1&key2=new2&key4=val4",
                        "key1=new1&key2=new2&key4=val4&key3=undefined",
                        "key1=new1&key2=new2&key4=null&key3=undefined"},
            IE = {})
    public void set() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2&key2=val3');\n"
            + "        param.set('key4', 'val4');\n"
            + "        alert(param);\n"
            + "        param.set('key1', 'new1');\n"
            + "        alert(param);\n"
            + "        param.set('key2', 'new2');\n"
            + "        alert(param);\n"
            + "        param.set('key3', undefined);\n"
            + "        alert(param);\n"
            + "        param.set('key4', null);\n"
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
    @Alerts(DEFAULT = {"function keys() { [native code] }", "[object Iterator]",
                    "key1", "key2", "key1", "", "true"},
            FF = {"function keys() {\n    [native code]\n}", "[object URLSearchParams Iterator]",
                    "key1", "key2", "key1", "", "true"},
            FF78 = {"function keys() {\n    [native code]\n}", "[object URLSearchParams Iterator]",
                    "key1", "key2", "key1", "", "true"},
            IE = {})
    public void keys() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=&key1=val3&=val4');\n"

            + "        alert(param.keys);\n"
            + "        var iter = param.keys();\n"
            + "        alert(iter);\n"

            + "        var entry = iter.next().value;\n"
            + "        alert(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        alert(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        alert(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        alert(entry);\n"

            + "        alert(iter.next().done);\n"
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
    @Alerts(DEFAULT = {"function values() { [native code] }", "[object Iterator]",
                    "val1", "", "val3", "val4", "true"},
            FF = {"function values() {\n    [native code]\n}", "[object URLSearchParams Iterator]",
                    "val1", "", "val3", "val4", "true"},
            FF78 = {"function values() {\n    [native code]\n}", "[object URLSearchParams Iterator]",
                    "val1", "", "val3", "val4", "true"},
            IE = {})
    public void values() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=&key1=val3&=val4');\n"

            + "        alert(param.values);\n"
            + "        var iter = param.values();\n"
            + "        alert(iter);\n"

            + "        var entry = iter.next().value;\n"
            + "        alert(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        alert(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        alert(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        alert(entry);\n"

            + "        alert(iter.next().done);\n"
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
    @Alerts(DEFAULT = {"val1", "", "val3", "val4"},
            IE = {})
    public void valuesForOf() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=&key1=val3&=val4');\n"

            + "        for (var i of param.values()) {\n"
            + "          alert(i);\n"
            + "        }\n"
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
    @Alerts(DEFAULT = {"function entries() { [native code] }", "[object Iterator]",
                    "key1-val1", "key2-", "key1-val3", "-val4", "true"},
            FF = {"function entries() {\n    [native code]\n}", "[object URLSearchParams Iterator]",
                    "key1-val1", "key2-", "key1-val3", "-val4", "true"},
            FF78 = {"function entries() {\n    [native code]\n}", "[object URLSearchParams Iterator]",
                    "key1-val1", "key2-", "key1-val3", "-val4", "true"},
            IE = {})
    public void entries() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=&key1=val3&=val4');\n"

            + "        alert(param.entries);\n"
            + "        var iter = param.entries();\n"
            + "        alert(iter);\n"

            + "        var entry = iter.next().value;\n"
            + "        alert(entry[0] + '-' + entry[1]);\n"
            + "        entry = iter.next().value;\n"
            + "        alert(entry[0] + '-' + entry[1]);\n"
            + "        entry = iter.next().value;\n"
            + "        alert(entry[0] + '-' + entry[1]);\n"
            + "        entry = iter.next().value;\n"
            + "        alert(entry[0] + '-' + entry[1]);\n"

            + "        alert(iter.next().done);\n"
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
    @Alerts(DEFAULT = {"key1,val1", "key2,", "key1,val3", ",val4"},
            IE = {})
    public void entriesForOf() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=&key1=val3&=val4');\n"

            + "        for (var i of param.entries()) {\n"
            + "          alert(i);\n"
            + "        }\n"
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
