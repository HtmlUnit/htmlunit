/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests of the source repository of <a href="http://code.google.com/webtoolkit">Google Web Toolkit</a>,
 * which are marked to fail with HtmlUnit.
 *
 * To generate the JavaScript, copy the test case to "Hello" GWT sample, compile it with "-style PRETTY"
 * by modifying "gwtc" target, and run "ant".  In generated ".nocache.js", search for "ie6" or "gecko1_8"
 * to know which JavaScript file corresponds to IE or FF respectively.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class GWTSourceTest extends WebDriverTestCase {

    /**
     * Original test resides in
     * <a href="http://code.google.com/p/google-web-toolkit/source/browse/trunk/user/test/com/google/gwt/emultest/java/lang/StringTest.java">StringTest</a>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    @Alerts({ "\\*\\[", "\\\\", "+1", "abcdef", "1\\1abc123\\123de1234\\1234f", "\n  \n", "x  x", "x\"\\", "$$x$" })
    public void testReplaceAll() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var regex, replacement, x1, x2, x3, x4, x5;\n"
            + "      regex = $replaceAll('*[', "
            + "'([/\\\\\\\\\\\\.\\\\*\\\\+\\\\?\\\\|\\\\(\\\\)\\\\[\\\\]\\\\{\\\\}])', '\\\\\\\\$1');\n"
            + "      alert(regex);\n"
            + "      replacement = "
            + "$replaceAll($replaceAll('\\\\', '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '\\\\$', '\\\\\\\\$');\n"
            + "      alert(replacement);\n"
            + "      alert($replaceAll('*[1', regex, '+'));\n"
            + "      x1 = 'xxxabcxxdexf';\n"
            + "      alert($replaceAll(x1, 'x*', ''));\n"
            + "      x2 = '1abc123de1234f';\n"
            + "      alert($replaceAll(x2, '([1234]+)', '$1\\\\\\\\$1'));\n"
            + "      x3 = 'x  x';\n"
            + "      alert($replaceAll(x3, 'x', '\\n'));\n"
            + "      x4 = 'x  \\n';\n"
            + "      alert($replaceAll(x4, '\\\\\\n', 'x'));\n"
            + "      x5 = 'x';\n"
            + "      alert($replaceAll(x5, 'x', '\\\\x\\\\\"\\\\\\\\'));\n"
            + "      alert($replaceAll(x5, '(x)', '\\\\$\\\\$$1\\\\$'));\n"
            + "    }\n"
            + "    function $replaceAll(this$static, regex, replace){\n"
            + "      replace = __translateReplaceString(replace);\n"
            + "      return this$static.replace(RegExp(regex, 'g'), replace);\n"
            + "    }\n"
            + "    function __translateReplaceString(replaceStr){\n"
            + "      var pos = 0;\n"
            + "      while (0 <= (pos = replaceStr.indexOf('\\\\', pos))) {\n"
            + "        if (replaceStr.charCodeAt(pos + 1) == 36) {\n"
            + "          replaceStr = replaceStr.substr(0, pos - 0) + '$' + $substring(replaceStr, ++pos);\n"
            + "        }\n"
            + "        else {\n"
            + "          replaceStr = replaceStr.substr(0, pos - 0) + $substring(replaceStr, ++pos);\n"
            + "        }\n"
            + "      }\n"
            + "      return replaceStr;\n"
            + "    }\n"
            + "    function $substring(this$static, beginIndex){\n"
            + "      return this$static.substr(beginIndex, this$static.length - beginIndex);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Original test resides in
     * <a href="http://code.google.com/p/google-web-toolkit/source/browse/trunk/user/test/com/google/gwt/emultest/java/lang/StringTest.java">StringTest</a>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    @Alerts({ "foobar", "$0bar", "$1bar", "\\$1bar", "\\1", "cb", "cb", "a$$b", "a$1b", "a$`b", "a$'b" })
    public void testReplaceString() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert($replace('bazbar', 'baz', 'foo'));\n"
            + "      alert($replace('foobar', 'foo', '$0'));\n"
            + "      alert($replace('foobar', 'foo', '$1'));\n"
            + "      alert($replace('foobar', 'foo', '\\\\$1'));\n"
            + "      alert($replace('*[)1', '*[)', '\\\\'));\n"
            + "      alert($replace('$ab', '$a', 'c'));\n"
            + "      alert($replace('^ab', '^a', 'c'));\n"
            + "      alert($replace('a[x]b', '[x]', '$$'));\n"
            + "      alert($replace('a[x]b', '[x]', '$1'));\n"
            + "      alert($replace('a[x]b', '[x]', '$`'));\n"
            + "      alert($replace('a[x]b', '[x]', \"$'\"));\n"
            + "    }\n"
            + "    function $replace(this$static, from, to){\n"
            + "      var regex, replacement;\n"
            + "      regex = $replaceAll(from, "
            + "'([/\\\\\\\\\\\\.\\\\*\\\\+\\\\?\\\\|\\\\(\\\\)\\\\[\\\\]\\\\{\\\\}$^])', '\\\\\\\\$1');\n"
            + "      replacement = $replaceAll("
            + "$replaceAll(to, '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '\\\\$', '\\\\\\\\$');\n"
            + "      return $replaceAll(this$static, regex, replacement);\n"
            + "    }\n"
            + "    function $replaceAll(this$static, regex, replace){\n"
            + "      replace = __translateReplaceString(replace);\n"
            + "      return this$static.replace(RegExp(regex, 'g'), replace);\n"
            + "    }\n"
            + "    function __translateReplaceString(replaceStr){\n"
            + "      var pos = 0;\n"
            + "      while (0 <= (pos = replaceStr.indexOf('\\\\', pos))) {\n"
            + "        if (replaceStr.charCodeAt(pos + 1) == 36) {\n"
            + "          replaceStr = replaceStr.substr(0, pos - 0) + '$' + $substring(replaceStr, ++pos);\n"
            + "        }\n"
            + "        else {\n"
            + "          replaceStr = replaceStr.substr(0, pos - 0) + $substring(replaceStr, ++pos);\n"
            + "        }\n"
            + "      }\n"
            + "      return replaceStr;\n"
            + "    }\n"
            + "    function $substring(this$static, beginIndex){\n"
            + "      return this$static.substr(beginIndex, this$static.length - beginIndex);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Original test resides in
     * <a href="http://code.google.com/p/google-web-toolkit/source/browse/trunk/user/test/com/google/gwt/dom/client/MapTests.java">MapTests</a>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "true", "true", "true" })
    public void testGetArea() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var map = document.createElement('map');\n"
            + "      var area0 = document.createElement('area');\n"
            + "      var area1 = document.createElement('area');\n"
            + "      var area2 = document.createElement('area');\n"
            + "      map.appendChild(area0);\n"
            + "      map.appendChild(area1);\n"
            + "      map.appendChild(area2);\n"
            + "      var areaElems = map.areas;\n"
            + "      alert(areaElems.length);\n"
            + "      alert(area0 === areaElems[0]);\n"
            + "      alert(area1 === areaElems[1]);\n"
            + "      alert(area2 === areaElems[2]);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Original test resides in
     * <a href="http://code.google.com/p/google-web-toolkit/source/browse/trunk/user/test/com/google/gwt/user/client/ui/SimpleRadioButtonTest.java">SimpleRadioButtonTest</a>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    @Alerts(IE = { "true", "false", "false", "false", "true", "true" },
            FF = { "true", "true", "true", "true", "true", "true" })
    public void testProperties() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var input = document.createElement('input');\n"
            + "      input.type = 'radio';\n"
            + "      input.checked = true;\n"
            + "      alert(input.checked);\n"
            + "      document.body.appendChild(input);\n"
            + "      alert(input.checked);\n"
            + "      document.body.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "\n"
            + "      input.defaultChecked = true;\n"
            + "      alert(input.checked);\n"
            + "      document.body.appendChild(input);\n"
            + "      alert(input.checked);\n"
            + "      document.body.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
