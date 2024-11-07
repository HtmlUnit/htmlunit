/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HTMLAllCollection}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLAllCollectionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
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
    @Alerts("-button2")
    public void namedItem_ByName_formWithoutId() throws Exception {
        namedItem("'button2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b3-button3")
    public void namedItem_ByName() throws Exception {
        namedItem("'button3'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"coll 2", "b4-button4_1", "b4-button4_2"})
    public void namedItem_DuplicateId() throws Exception {
        namedItem("'b4'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"coll 2", "b5_1-button5", "b5_2-button5"})
    public void namedItem_DuplicateName() throws Exception {
        namedItem("'button5'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"coll 2", "b6-button6", "button6-button6_2"})
    public void namedItem_DuplicateIdName() throws Exception {
        namedItem("'button6'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void namedItem_ZeroIndex() throws Exception {
        namedItem("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void namedItem_ValidIndex() throws Exception {
        namedItem("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void namedItem_DoubleIndex() throws Exception {
        namedItem("1.1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void namedItem_InvalidIndex() throws Exception {
        namedItem("200");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void namedItem_IndexAsString() throws Exception {
        namedItem("'1'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void namedItem_IndexDoubleAsString() throws Exception {
        namedItem("'1.1'");
    }

    private void namedItem(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html id='myHtml'><head id='myHead'><title id='myTitle'>First</title><script>\n"
            + "  alerts = ''\n"
            + "  function log(msg) { alerts += msg + '§';}\n"
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      log(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      log('coll ' + result.length);\n"
            + "      for(var i = 0; i < result.length; i++) {\n"
            + "        log(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      log(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      log(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var item = document.all.namedItem(" + name + ");\n"
            + "      report(item);\n"
            + "    } catch(e) { log(e); }\n"
            + "    document.title = alerts;"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void item_Unknown() throws Exception {
        item("'foo'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void item_ById() throws Exception {
        item("'b2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void item_ByName() throws Exception {
        item("'button2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void item_NegativIndex() throws Exception {
        item("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myHtml-undefined")
    public void item_ZeroIndex() throws Exception {
        item("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myHead-undefined")
    public void item_ValidIndex() throws Exception {
        item("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void item_DoubleIndex() throws Exception {
        item("1.1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void item_InvalidIndex() throws Exception {
        item("200");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myHead-undefined")
    public void item_IndexAsString() throws Exception {
        item("'1'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void item_IndexDoubleAsString() throws Exception {
        item("'1.1'");
    }

    private void item(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html id='myHtml'><head id='myHead'><title id='myTitle'>First</title><script>\n"
            + "  alerts = ''\n"
            + "  function log(msg) { alerts += msg + '§';}\n"
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      log(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      log('coll ' + result.length);\n"
            + "      for(var i = 0; i < result.length; i++) {\n"
            + "        log(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      log(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      log(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var item = document.all.item(" + name + ");\n"
            + "      report(item);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "    document.title = alerts;"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='b1' name='button1'></button>\n"
            + "  <button id='b2' name='button2'></button>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
    @Alerts("b2-button2")
    public void arrayIndex_ById() throws Exception {
        arrayIndex("'b2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
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
    @Alerts("myHtml-undefined")
    public void arrayIndex_ZeroIndex() throws Exception {
        arrayIndex("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myHead-undefined")
    public void arrayIndex_ValidIndex() throws Exception {
        arrayIndex("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
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
    @Alerts("myTitle-undefined")
    public void arrayIndex_IndexAsString() throws Exception {
        arrayIndex("'2'");
    }

    private void arrayIndex(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html id='myHtml'><head id='myHead'><title id='myTitle'>First</title><script>\n"
            + "  alerts = ''\n"
            + "  function log(msg) { alerts += msg + '§';}\n"
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      log(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      log('coll ' + result.length);\n"
            + "      for(var i = 0; i < result.length; i++) {\n"
            + "        log(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      log(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      log(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var item = document.all[" + name + "];\n"
            + "      report(item);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "    document.title = alerts;"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='b1' name='button1'></button>\n"
            + "  <button id='b2' name='button2'></button>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void functionIndex_Unknown() throws Exception {
        functionIndex("'foo'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void functionIndex_ById() throws Exception {
        functionIndex("'b2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b2-button2")
    public void functionIndex_ByName() throws Exception {
        functionIndex("'button2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void functionIndex_NegativIndex() throws Exception {
        functionIndex("-1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myHtml-undefined")
    public void functionIndex_ZeroIndex() throws Exception {
        functionIndex("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myHead-undefined")
    public void functionIndex_ValidIndex() throws Exception {
        functionIndex("1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void functionIndex_DoubleIndex() throws Exception {
        functionIndex("1.1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void functionIndex_InvalidIndex() throws Exception {
        functionIndex("200");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myTitle-undefined")
    public void functionIndex_IndexAsString() throws Exception {
        functionIndex("'2'");
    }

    private void functionIndex(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html id='myHtml'><head id='myHead'><title id='myTitle'>First</title><script>\n"
            + "  alerts = ''\n"
            + "  function log(msg) { alerts += msg + '§';}\n"
            + "  function report(result) {\n"
            + "    if (result == null || result == undefined) {\n"
            + "      log(result);\n"
            + "    } else if (('length' in result) && ('item' in result)) {\n"
            + "      log('coll ' + result.length);\n"
            + "      for(var i = 0; i < result.length; i++) {\n"
            + "        log(result.item(i).id + '-' + result.item(i).name);\n"
            + "      }\n"
            + "    } else if (result.id || result.name) {\n"
            + "      log(result.id + '-' + result.name);\n"
            + "    } else {\n"
            + "      log(result);\n"
            + "    }\n"
            + "  }\n"

            + "  function doTest() {\n"
            + "    try {\n"
            + "      var item = document.all(" + name + ");\n"
            + "      report(item);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "    document.title = alerts;"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <button id='b1' name='button1'></button>\n"
            + "  <button id='b2' name='button2'></button>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLAllCollection]", "function HTMLAllCollection() { [native code] }"})
    public void type() throws Exception {
        final String html = ""
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(document.all);\n"
            + "      log(HTMLAllCollection);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function () { [native code] }")
    public void proto() throws Exception {
        final String html = ""
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(HTMLAllCollection.__proto__);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * See https://developer.mozilla.org/en-US/docs/Web/API/HTMLAllCollection#special_type_conversion_behavior.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void looselyEqualToUndefined() throws Exception {
        final String html = ""
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(undefined === document.all);\n"
            + "  log(undefined == document.all);\n"
            + "  log(document.all === undefined);\n"
            + "  log(document.all == undefined);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * See https://developer.mozilla.org/en-US/docs/Web/API/HTMLAllCollection#special_type_conversion_behavior.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void looselyEqualToNull() throws Exception {
        final String html = ""
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(null === document.all);\n"
            + "  log(null == document.all);\n"
            + "  log(document.all === null);\n"
            + "  log(document.all == null);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * See https://developer.mozilla.org/en-US/docs/Web/API/HTMLAllCollection#special_type_conversion_behavior.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7", "1", "3", "[object HTMLAllCollection]", "5"})
    public void falsyInBooleanContexts() throws Exception {
        final String html = ""
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  x = 11;\n"
            + "  if(document.all) { x = 1 } else { x = 7 }"
            + "  log(x);\n"

            + "  if(!document.all) { x = 1 } else { x = 7 }"
            + "  log(x);\n"

            + "  log(document.all ? 4 : 3);\n"

            + "  log(document.all ?? 'htmlunit');\n"
            + "  log(document.all?.length);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * See https://developer.mozilla.org/en-US/docs/Web/API/HTMLAllCollection#special_type_conversion_behavior.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void typeof() throws Exception {
        final String html = ""
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof document.all);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
