/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLAllCollection}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLAllCollectionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "null",
            IE8 = "null")
    public void namedItem_Unknown() throws Exception {
        namedItem("'foo'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("button1-")
    public void namedItem_ById() throws Exception {
        namedItem("'button1'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-button2",
            CHROME = "null")
    public void namedItem_ByName_formWithoutId() throws Exception {
        namedItem("'button2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b3-button3",
            CHROME = "null")
    public void namedItem_ByName() throws Exception {
        namedItem("'button3'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "coll 2", "b4-button4_1", "b4-button4_2" },
            IE = "b4-button4_1")
    public void namedItem_DuplicateId() throws Exception {
        namedItem("'b4'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "coll 2", "b5_1-button5", "b5_2-button5" },
            IE = "b5_1-button5",
            CHROME = "null")
    public void namedItem_DuplicateName() throws Exception {
        namedItem("'button5'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "coll 2", "b6-button6", "button6-button6_2" },
            CHROME = { "button6-button6_2" },
            IE = "b6-button6")
    public void namedItem_DuplicateIdName() throws Exception {
        namedItem("'button6'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myHtml-undefined",
            IE8 = "[object HTMLCommentElement]")
    public void namedItem_ZeroIndex() throws Exception {
        item("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myHead-undefined",
            IE8 = "myHtml-undefined")
    public void namedItem_ValidIndex() throws Exception {
        item("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "myHead-undefined",
            IE8 = "myHtml-undefined")
    public void namedItem_DoubleIndex() throws Exception {
        item("1.1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = "null",
            CHROME = "null")
    public void namedItem_InvalidIndex() throws Exception {
        item("200");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myHead-undefined",
            IE11 = "null",
            IE8 = "myHtml-undefined")
    public void namedItem_IndexAsString() throws Exception {
        item("'1'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "null",
            IE8 = "myHtml-undefined")
    public void namedItem_IndexDoubleAsString() throws Exception {
        item("'1.1'");
    }

    private void namedItem(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html id='myHtml'><head id='myHead'><title id='myTitle'>First</title><script>\n"
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      alert(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      alert('coll ' + result.length);\n"
            + "      for(i=0; i < result.length; i++) {\n"
            + "        alert(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      alert(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      alert(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var item = document.all.namedItem(" + name + ");\n"
            + "      report(item);\n"
            + "    } catch(e) { alert(e); }"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='button1'></button>\n"
            + "  <button name='button2'></button>\n"
            + "  <button id='b3' name='button3'></button>\n"
            + "  <button id='b4' name='button4_1'></button>\n"
            + "  <button id='b4' name='button4_2'></button>\n"
            + "  <button id='b5_1' name='button5'></button>\n"
            + "  <button id='b5_2' name='button5'></button>\n"
            + "  <button id='b6' name='button6'></button>\n"
            + "  <button id='button6' name='button6_2'></button>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "null",
            IE8 = "[object HTMLCommentElement]")
    @NotYetImplemented(IE8)
    public void item_Unknown() throws Exception {
        item("'foo'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b2-button2",
            IE8 = "[object HTMLCommentElement]")
    @NotYetImplemented(IE8)
    public void item_ById() throws Exception {
        item("'b2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b2-button2",
            CHROME = "undefined",
            IE8 = "[object HTMLCommentElement]")
    @NotYetImplemented(IE8)
    public void item_ByName() throws Exception {
        item("'button2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "null",
            IE8 = "exception")
    public void item_NegativIndex() throws Exception {
        item("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myHtml-undefined",
            IE8 = "[object HTMLCommentElement]")
    public void item_ZeroIndex() throws Exception {
        item("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myHead-undefined",
            IE8 = "myHtml-undefined")
    public void item_ValidIndex() throws Exception {
        item("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "myHead-undefined",
            IE8 = "myHtml-undefined")
    public void item_DoubleIndex() throws Exception {
        item("1.1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = "null",
            CHROME = "null")
    public void item_InvalidIndex() throws Exception {
        item("200");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myHead-undefined",
            IE11 = "null",
            IE8 = "myHtml-undefined")
    public void item_IndexAsString() throws Exception {
        item("'1'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "null",
            IE8 = "myHtml-undefined")
    public void item_IndexDoubleAsString() throws Exception {
        item("'1.1'");
    }

    private void item(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html id='myHtml'><head id='myHead'><title id='myTitle'>First</title><script>\n"
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      alert(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      alert('coll ' + result.length);\n"
            + "      for(i=0; i < result.length; i++) {\n"
            + "        alert(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      alert(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      alert(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var item = document.all.item(" + name + ");\n"
            + "      report(item);\n"
            + "    } catch(e) { alert('exception'); }"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='b1' name='button1'></button>\n"
            + "  <button id='b2' name='button2'></button>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void arrayIndex_Unknown() throws Exception {
        arrayIndex("'foo'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b2-button2")
    public void arrayIndex_ById() throws Exception {
        arrayIndex("'b2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b2-button2",
            CHROME = "undefined")
    public void arrayIndex_ByName() throws Exception {
        arrayIndex("'button2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void arrayIndex_NegativIndex() throws Exception {
        arrayIndex("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myHtml-undefined",
            IE8 = "[object HTMLCommentElement]")
    public void arrayIndex_ZeroIndex() throws Exception {
        arrayIndex("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myHead-undefined",
            IE8 = "myHtml-undefined")
    public void arrayIndex_ValidIndex() throws Exception {
        arrayIndex("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "myHead-undefined",
            IE8 = "myHtml-undefined")
    public void arrayIndex_DoubleIndex() throws Exception {
        arrayIndex("1.1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void arrayIndex_InvalidIndex() throws Exception {
        arrayIndex("200");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myTitle-undefined",
            IE8 = "myHead-undefined")
    public void arrayIndex_IndexAsString() throws Exception {
        arrayIndex("'2'");
    }

    private void arrayIndex(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html id='myHtml'><head id='myHead'><title id='myTitle'>First</title><script>\n"
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      alert(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      alert('coll ' + result.length);\n"
            + "      for(i=0; i < result.length; i++) {\n"
            + "        alert(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      alert(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      alert(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var item = document.all[" + name + "];\n"
            + "      report(item);\n"
            + "    } catch(e) { alert('exception'); }"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='b1' name='button1'></button>\n"
            + "  <button id='b2' name='button2'></button>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
