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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLButtonElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLButtonElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "A", "a", "A", "a8", "8Afoo", "8", "@" })
    public void readWriteAccessKey() throws Exception {
        final String html
            = "<html><body><button id='a1'>a1</button><button id='a2' accesskey='A'>a2</button><script>\n"
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = 'a';\n"
            + "a2.accessKey = 'A';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = 'a8';\n"
            + "a2.accessKey = '8Afoo';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = '8';\n"
            + "a2.accessKey = '@';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests setting the <tt>type</tt> property.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "submit", "button", "submit" },
            CHROME = { "submit", "submit", "submit" },
            IE8 = { "button", "exception", "button", "button" })
    public void type() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var b = document.createElement('button');\n"
            + "    alert(b.type);\n"
            + "    try {\n"
            + "      b.type = 'button';\n"
            + "    } catch(e) {alert('exception')}\n"
            + "    alert(b.type);\n"
            + "    b.removeAttribute('type');\n"
            + "    alert(b.type);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "submit", "submit", "submit", "submit", "reset", "button", "submit" },
            IE8 = { "button", "button", "submit", "button", "reset", "button", "button" })
    public void getType() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myNone').type);\n"
            + "    alert(document.getElementById('myEmpty').type);\n"
            + "    alert(document.getElementById('mySubmit').type);\n"
            + "    alert(document.getElementById('mySubmitTrim').type);\n"
            + "    alert(document.getElementById('myReset').type);\n"
            + "    alert(document.getElementById('myButton').type);\n"
            + "    alert(document.getElementById('myUnknown').type);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <button id='myNone'></button>\n"
            + "    <button type='' id='myEmpty'></button>\n"
            + "    <button type='submit' id='mySubmit'></button>\n"
            + "    <button type=' Submit\t' id='mySubmitTrim'></button>\n"
            + "    <button type='reSet' id='myReset'></button>\n"
            + "    <button type='button' id='myButton'></button>\n"
            + "    <button type='unknown' id='myUnknown'></button>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "myFormId", "null", "null", "null", "null", "myFormId", "null", "myForm2Id", "myForm2Id" },
            IE11 = { "myFormId", "myFormId", "null", "myFormId", "myFormId", "null", "myFormId", "myFormId", "null" },
            IE8 = { "myFormId", "", "", "undefined", "undefined", "undefined", "undefined", "undefined", "undefined" })
    @NotYetImplemented({ FF, IE8 })
    public void getForm() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function show(id) {\n"
            + "    elem = document.getElementById(id);\n"
            + "    if (elem.form) {\n"
            + "      alert(elem.form.id);\n"
            + "    } else {\n"
            + "      alert(elem.form);\n"
            + "    }\n"
            + "  }\n"
            + "  function test() {\n"
            + "    show('myNone');\n"
            + "    show('myEmptyInside');\n"
            + "    show('myEmptyOutside');\n"
            + "    show('myFormTrim');\n"
            + "    show('myFormCase');\n"
            + "    show('myOutside');\n"
            + "    show('myUnknown');\n"
            + "    show('myFormOther');\n"
            + "    show('myFormOtherOutside');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myFormId' action='" + URL_SECOND + "'>\n"
            + "    <button id='myNone'></button>\n"
            + "    <button form='' id='myEmptyInside'></button>\n"
            + "    <button form='myFormId' id='myForm'></button>\n"
            + "    <button form=' myFormId\t' id='myFormTrim'></button>\n"
            + "    <button form='myformid' id='myFormCase'></button>\n"
            + "    <button form='unknown' id='myUnknown'></button>\n"
            + "    <button form='myForm2Id' id='myFormOther'></button>\n"
            + "  </form>\n"
            + "  <form id='myForm2Id' action='" + URL_SECOND + "'>\n"
            + "  </form>\n"
            + "  <button form='myFormId' id='myOutside'></button>\n"
            + "  <button form='' id='myEmptyOutside'></button>\n"
            + "  <button form='myForm2Id' id='myFormOtherOutside'></button>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "test", "4", "42", "2", "[object HTMLButtonElement]", "26" },
            IE8 = { "test", "4", "42", "2", "[object]", "8" })
    public void getAttributeAndSetValue() throws Exception {
        final String html =
            "<html>\n"
            + "  <head><title>foo</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 'test';\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"

            + "        t.value = 42;\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"

            + "        t.value = document.getElementById('t');\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <button id='t'>abc</button>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "null", "4", "null", "4" })
    public void getAttributeAndSetValueNull() throws Exception {
        final String html =
            "<html>\n"
            + "  <head><title>foo</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 'null';\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"

            + "        t.value = null;\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <button id='t'>abc</button>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
