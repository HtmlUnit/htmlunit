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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HtmlElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Denis N. Antonioli
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE8 = "value")
    public void onpropertychange() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    document.getElementById('input1').value = 'New Value';\n"
            + "  }\n"
            + "  function handler() {\n"
            + "    alert(event.propertyName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='input1' onpropertychange='handler()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true" })
    public void duplicateId() throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var value = document.getElementById('duplicateID').innerHTML;\n"
            + "    alert(value.length > 10);\n"
            + "    document.getElementById('duplicateID').style.display = 'block';\n"
            + "    alert(value === document.getElementById('duplicateID').innerHTML);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <fieldset id='duplicateID'><span id='duplicateID'></span></fieldset>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(IE8)
    @Alerts(IE8 = { "1", "1" })
    public void onpropertychange2() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    document.getElementById('input1').value = 'New Value';\n"
            + "  }\n"
            + "  function handler() {\n"
            + "    alert(1);\n"
            + "    document.getElementById('input1').dir='rtl';"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='input1' onpropertychange='handler()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that cloned node attributes have the same initial values, but changes can be made
     * to the clone without affecting the original node, and that the id attribute is treated the
     * same as all the other attributes. See bug 1707726.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "false", "true", "a", "a", "b", "b", "b", "c" })
    public void clonedNodeAttributes() throws Exception {
        final String html = "<html><body id='a' title='b'><script>\n"
            + "var x = document.body.cloneNode(true);\n"
            + "alert(document.body==x);\n"
            + "alert(document.getElementById('a')==document.body);\n"
            + "alert(document.body.id);\n"
            + "alert(x.id);\n"
            + "alert(document.body.title);\n"
            + "alert(x.title);\n"
            + "x.title='c';\n"
            + "alert(document.body.title);\n"
            + "alert(x.title);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test attribute.text and attribute.xml added for XmlElement attributes
     * are undefined for HtmlElement.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "undefined", "undefined" })
    public void textAndXmlUndefined() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "    <input type='text' id='textfield1' onfocus='alert(1)'>\n"
            + "    <script>\n"
            + "         var node = document.getElementById('textfield1');\n"
            + "         alert(node.attributes[0].nodeName.length > 0);\n"
            + "         alert(node.attributes[0].text);\n"
            + "         alert(node.attributes[0].xml);\n"
            + "    </script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
