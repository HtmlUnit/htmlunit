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

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for {@link URLSearchParams}.
 *
 * @author Ronald Brill
 * @author cd alexndr
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        log(new URLSearchParams('?foo=1&bar=2'));\n"
            + "        log(new URLSearchParams());\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        log(new URLSearchParams());\n"
            + "        log(new URLSearchParams('?foo=1&bar=2'));\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('q', 'Html Unit');\n"
            + "        searchParams.append('unml', '\u00E4\u00DC');\n"
            + "        log(searchParams);\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('q', 'HtmlUnit');\n"
            + "        searchParams.append('u', '\u043B\u0189');\n"
            + "        log(searchParams);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('', 'emptyKey');\n"
            + "        log(searchParams);\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append(null, 'nullKey');\n"
            + "        log(searchParams);\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append(undefined, 'undefinedKey');\n"
            + "        log(searchParams);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('emptyValue', '');\n"
            + "        log(searchParams);\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('nullValue', null);\n"
            + "        log(searchParams);\n"

            + "        searchParams = new URLSearchParams();\n"
            + "        searchParams.append('undefinedValue', undefined);\n"
            + "        log(searchParams);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            FF_ESR = "noValue=undefined")
    public void stringMissingParam() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        try {\n"
            + "          searchParams = new URLSearchParams();\n"
            + "          searchParams.append('noValue');\n"
            + "          log(searchParams);\n"
            + "        } catch(e) { log('exception param'); }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams();\n"
            + "        param.append('key', 'value');\n"
            + "        log(param);\n"
            + "        param.append('empty-key', undefined);\n"
            + "        log(param);\n"
            + "        param.append('key', 'overwrite');\n"
            + "        log(param);\n"
            + "        param.append('key-null', null);\n"
            + "        log(param);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"key=value", "key=value&empty-key=undefined",
                       "key=value&empty-key=undefined&key=overwrite",
                       "key=value&empty-key=undefined&key=overwrite&key-null=null",
                       "http://test.com/p?key=value&empty-key=undefined&key=overwrite&key-null=null"},
            IE = {})
    public void appendFromUrl() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var url = new URL('http://test.com/p');\n"
            + "        var param = url.searchParams;\n"
            + "        param.append('key', 'value');\n"
            + "        log(param);\n"
            + "        param.append('empty-key', undefined);\n"
            + "        log(param);\n"
            + "        param.append('key', 'overwrite');\n"
            + "        log(param);\n"
            + "        param.append('key-null', null);\n"
            + "        log(param);\n"

            + "        log(url);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2&key2=val3');\n"
            + "        param.delete('key1');\n"
            + "        log(param);\n"
            + "        param.delete('key2');\n"
            + "        log(param);\n"
            + "        param.delete('key3');\n"
            + "        log(param);\n"
            + "        param.delete(undefined);\n"
            + "        log(param);\n"
            + "        param.delete(null);\n"
            + "        log(param);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"key2=val2&key2=val3", "", "", "", "", "http://test.com/p"},
            IE = {})
    public void deleteFromUrl() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var url = new URL('http://test.com/p?key1=val1&key2=val2&key2=val3');\n"
            + "        var param = url.searchParams;\n"
            + "        param.delete('key1');\n"
            + "        log(param);\n"
            + "        param.delete('key2');\n"
            + "        log(param);\n"
            + "        param.delete('key3');\n"
            + "        log(param);\n"
            + "        param.delete(undefined);\n"
            + "        log(param);\n"
            + "        param.delete(null);\n"
            + "        log(param);\n"

            + "        log(url);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"key+1=val1&key2=val2", "http://test.com/p?key%201=val1&key2=val2",
                       "key2=val2", "http://test.com/p?key2=val2"},
            IE = {})
    @HtmlUnitNYI(CHROME = {"key 1=val1&key2=val2", "http://test.com/p?key 1=val1&key2=val2",
                           "key2=val2", "http://test.com/p?key2=val2"},
                 EDGE = {"key 1=val1&key2=val2", "http://test.com/p?key 1=val1&key2=val2",
                         "key2=val2", "http://test.com/p?key2=val2"},
                 FF = {"key 1=val1&key2=val2", "http://test.com/p?key 1=val1&key2=val2",
                       "key2=val2", "http://test.com/p?key2=val2"},
                 FF_ESR = {"key 1=val1&key2=val2", "http://test.com/p?key 1=val1&key2=val2",
                           "key2=val2", "http://test.com/p?key2=val2"})
    public void deleteFromUrlSpecialChars() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var url = new URL('http://test.com/p?key 1=val1&key2=val2');\n"
            + "        var param = url.searchParams;\n"
            + "        log(param);\n"
            + "        log(url);\n"

            + "        param.delete('key 1');\n"
            + "        log(param);\n"
            + "        log(url);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2&key1=val3');\n"
            + "        log(param.get('key1'));\n"
            + "        log(param.get('key2'));\n"
            + "        log(param.get('key3'));\n"
            + "        log(param.get(undefined));\n"
            + "        log(param.get(null));\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2&key1=val3');\n"
            + "        log(param.has('key1'));\n"
            + "        log(param.has('key2'));\n"
            + "        log(param.has('key3'));\n"
            + "        log(param.has(undefined));\n"
            + "        log(param.has(null));\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2&key1=val3');\n"
            + "        log(param.getAll('key1'));\n"
            + "        log(param.getAll('key2'));\n"
            + "        log(param.getAll('key3'));\n"
            + "        log(param.getAll(undefined));\n"
            + "        log(param.getAll(null));\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=val2&key2=val3');\n"
            + "        param.set('key4', 'val4');\n"
            + "        log(param);\n"
            + "        param.set('key1', 'new1');\n"
            + "        log(param);\n"
            + "        param.set('key2', 'new2');\n"
            + "        log(param);\n"
            + "        param.set('key3', undefined);\n"
            + "        log(param);\n"
            + "        param.set('key4', null);\n"
            + "        log(param);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"key1=val1&key2=val2&key2=val3&key4=val4",
                       "key1=new1&key2=val2&key2=val3&key4=val4",
                       "key1=new1&key2=new2&key4=val4",
                       "key1=new1&key2=new2&key4=val4&key3=undefined",
                       "key1=new1&key2=new2&key4=null&key3=undefined",
                       "http://test.com/p?key1=new1&key2=new2&key4=null&key3=undefined"},
            IE = {})
    public void setFromUrl() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var url = new URL('http://test.com/p?key1=val1&key2=val2&key2=val3');\n"
            + "        var param = url.searchParams;\n"
            + "        param.set('key4', 'val4');\n"
            + "        log(param);\n"
            + "        param.set('key1', 'new1');\n"
            + "        log(param);\n"
            + "        param.set('key2', 'new2');\n"
            + "        log(param);\n"
            + "        param.set('key3', undefined);\n"
            + "        log(param);\n"
            + "        param.set('key4', null);\n"
            + "        log(param);\n"

            + "        log(url);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"function keys() { [native code] }", "[object Iterator]",
                       "key1", "key2", "key1", "", "true"},
            FF = {"function keys() { [native code] }", "[object URLSearchParams Iterator]",
                  "key1", "key2", "key1", "", "true"},
            FF_ESR = {"function keys() { [native code] }", "[object URLSearchParams Iterator]",
                      "key1", "key2", "key1", "", "true"},
            IE = {})
    public void keys() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=&key1=val3&=val4');\n"

            + "        log(param.keys);\n"
            + "        var iter = param.keys();\n"
            + "        log(iter);\n"

            + "        var entry = iter.next().value;\n"
            + "        log(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        log(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        log(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        log(entry);\n"

            + "        log(iter.next().done);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"function values() { [native code] }", "[object Iterator]",
                       "val1", "", "val3", "val4", "true"},
            FF = {"function values() { [native code] }", "[object URLSearchParams Iterator]",
                  "val1", "", "val3", "val4", "true"},
            FF_ESR = {"function values() { [native code] }", "[object URLSearchParams Iterator]",
                      "val1", "", "val3", "val4", "true"},
            IE = {})
    public void values() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=&key1=val3&=val4');\n"

            + "        log(param.values);\n"
            + "        var iter = param.values();\n"
            + "        log(iter);\n"

            + "        var entry = iter.next().value;\n"
            + "        log(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        log(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        log(entry);\n"
            + "        entry = iter.next().value;\n"
            + "        log(entry);\n"

            + "        log(iter.next().done);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=&key1=val3&=val4');\n"

            + "        for (var i of param.values()) {\n"
            + "          log(i);\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"function entries() { [native code] }", "[object Iterator]",
                       "key1-val1", "key2-", "key1-val3", "-val4", "true"},
            FF = {"function entries() { [native code] }", "[object URLSearchParams Iterator]",
                  "key1-val1", "key2-", "key1-val3", "-val4", "true"},
            FF_ESR = {"function entries() { [native code] }", "[object URLSearchParams Iterator]",
                      "key1-val1", "key2-", "key1-val3", "-val4", "true"},
            IE = {})
    public void entries() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=&key1=val3&=val4');\n"

            + "        log(param.entries);\n"
            + "        var iter = param.entries();\n"
            + "        log(iter);\n"

            + "        var entry = iter.next().value;\n"
            + "        log(entry[0] + '-' + entry[1]);\n"
            + "        entry = iter.next().value;\n"
            + "        log(entry[0] + '-' + entry[1]);\n"
            + "        entry = iter.next().value;\n"
            + "        log(entry[0] + '-' + entry[1]);\n"
            + "        entry = iter.next().value;\n"
            + "        log(entry[0] + '-' + entry[1]);\n"

            + "        log(iter.next().done);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (self.URLSearchParams) {\n"
            + "        var param = new URLSearchParams('key1=val1&key2=&key1=val3&=val4');\n"

            + "        for (var i of param.entries()) {\n"
            + "          log(i);\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function URLSearchParams() { [native code] }",
                       "function URLSearchParams() { [native code] }",
                       "function URLSearchParams() { [native code] }",
                       "key1=val1", "key1=val1", "key1=val1"},
            IE = {})
    public void testToString() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (self.URLSearchParams) {\n"
            + "    log(URLSearchParams);\n"
            + "    log('' + URLSearchParams);\n"
            + "    log(URLSearchParams.toString());\n"

            + "    var p = new URLSearchParams('key1=val1');\n"
            + "    log(p);\n"
            + "    log('' + p);\n"
            + "    log(p.toString());\n"
            + "  }"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
