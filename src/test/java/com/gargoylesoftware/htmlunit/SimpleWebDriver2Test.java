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
package com.gargoylesoftware.htmlunit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Same as {@link SimpleWebDriverTest}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SimpleWebDriver2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void arrayProperties() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function log(text) {\n"
            + "      var textarea = document.getElementById('myTextarea');\n"
            + "      textarea.value += text + ',';\n"
            + "    }\n"
            + "    function test() {\n"
            + "      var properties = ['concat', 'every', 'filter', 'forEach', 'indexOf',\n"
            + "        'join', 'lastIndexOf', 'map', 'pop', 'push',\n"
            + "        'reverse', 'shift', 'slice', 'some', 'sort',\n"
            + "        'splice', 'toLocaleString', 'toSource', 'toString', 'unshift'];\n"
            + "      for (var i = 0; i < properties.length; i++) {\n"
            + "        var p = properties[i];\n"
            + "        var v = [][p];\n"
            + "        log(p + ': ' + typeof(v));\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <textarea id='myTextarea' cols='80' rows='10'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        final String expected;
        if (getBrowserVersion().isFirefox()) {
            expected = "concat: function,every: function,filter: function,forEach: function,indexOf: function,"
                + "join: function,lastIndexOf: function,map: function,pop: function,push: function,reverse: function,"
                + "shift: function,slice: function,some: function,sort: function,splice: function,"
                + "toLocaleString: function,toSource: function,toString: function,unshift: function,";
        }
        else {
            expected = "concat: function,every: undefined,filter: undefined,forEach: undefined,indexOf: undefined,"
                + "join: function,lastIndexOf: undefined,map: undefined,pop: function,push: function,"
                + "reverse: function,shift: function,slice: function,some: undefined,sort: function,splice: function,"
                + "toLocaleString: function,toSource: undefined,toString: function,unshift: function,";
        }

        assertEquals(expected, driver.findElement(By.id("myTextarea")).getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void objectProperties() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function log(text) {\n"
            + "      var textarea = document.getElementById('myTextarea');\n"
            + "      textarea.value += text + ',';\n"
            + "    }\n"
            + "    function test() {\n"
            + "      var properties = ['__defineGetter__', '__defineSetter__', '__lookupGetter__',\n"
            + "        '__lookupSetter__', 'hasOwnProperty', 'isPrototypeOf', 'propertyIsEnumerable',\n"
            + "        'toLocaleString', 'toSource', 'toString', 'valueOf'];\n"
            + "      for (var i = 0; i < properties.length; i++) {\n"
            + "        var p = properties[i];\n"
            + "        var v = [][p];\n"
            + "        log(p + ': ' + typeof(v));\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <textarea id='myTextarea' cols='80' rows='10'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        final String expected;
        if (getBrowserVersion().isFirefox()) {
            expected = "__defineGetter__: function,__defineSetter__: function,__lookupGetter__: function,"
                + "__lookupSetter__: function,hasOwnProperty: function,isPrototypeOf: function,"
                + "propertyIsEnumerable: function,toLocaleString: function,toSource: function,toString: function,"
                + "valueOf: function,";
        }
        else {
            expected = "__defineGetter__: undefined,__defineSetter__: undefined,__lookupGetter__: undefined,"
                + "__lookupSetter__: undefined,hasOwnProperty: function,isPrototypeOf: function,"
                + "propertyIsEnumerable: function,toLocaleString: function,toSource: undefined,toString: function,"
                + "valueOf: function,";
        }

        assertEquals(expected, driver.findElement(By.id("myTextarea")).getValue());
    }
}
