/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link CharacterDataImpl}.
 *
 * @version $Revision$
 * @author David K. Taylor
 */
@RunWith(BrowserRunner.class)
public class CharacterDataImplTest extends WebDriverTestCase {

    /**
     * Regression test for inline text nodes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some Text", "9", "3", "Some Text", "#text" })
    public void characterDataImpl_textNode() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    alert(text1.data);\n"
            + "    alert(text1.length);\n"
            + "    alert(text1.nodeType);\n"
            + "    alert(text1.nodeValue);\n"
            + "    alert(text1.nodeName);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for setting the data property of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some New Text", "Some New Text" })
    public void characterDataImpl_setData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.data='Some New Text';\n"
            + "    alert(text1.data);\n"
            + "    alert(text1.nodeValue);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for setting the nodeValue property of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some New Text", "Some New Text" })
    public void characterDataImpl_setNodeValue() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.nodeValue='Some New Text';\n"
            + "    alert(text1.data);\n"
            + "    alert(text1.nodeValue);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for appendData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some Text Appended")
    public void characterDataImpl_appendData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.appendData(' Appended');\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for deleteData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some Text")
    public void characterDataImpl_deleteData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.deleteData(5, 11);\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Not So New Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for insertData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some New Text")
    public void characterDataImpl_insertData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.insertData(5, 'New ');\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for replaceData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some New Text")
    public void characterDataImpl_replaceData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    text1.replaceData(5, 3, 'New');\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Old Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for substringData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"New", "Some New Text" })
    public void characterDataImpl_substringData() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    alert(text1.substringData(5, 3));\n"
            + "    alert(text1.data);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some New Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Regression test for substringData of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some ", "Text", "true" })
    public void textImpl_splitText() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var div1=document.getElementById('div1');\n"
            + "    var text1=div1.firstChild;\n"
            + "    var text2=text1.splitText(5);\n"
            + "    alert(text1.data);\n"
            + "    alert(text2.data);\n"
            + "    alert(text1.nextSibling==text2);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='div1'>Some Text</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("First", driver.getTitle());
    }
}
