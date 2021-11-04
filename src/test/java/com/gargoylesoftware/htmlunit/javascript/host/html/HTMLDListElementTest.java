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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionList;

/**
 * Unit tests for {@link HTMLDListElement}.
 *
 * @author Daniel Gredler
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLDListElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLDListElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <dl id='myId'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final WebElement element = driver.findElement(By.id("myId"));
            assertTrue(toHtmlElement(element) instanceof HtmlDefinitionList);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "true", "true", "true", "null", "", "blah", "2",
             "true", "false", "true", "false", "", "null", "", "null"})
    public void compact() throws Exception {
        final String html =
                "<html>\n"
                + "  <head>\n"
                + "    <script>\n"
                + LOG_TITLE_FUNCTION
                + "      function test() {\n"
                + "        log(document.getElementById('dl1').compact);\n"
                + "        log(document.getElementById('dl2').compact);\n"
                + "        log(document.getElementById('dl3').compact);\n"
                + "        log(document.getElementById('dl4').compact);\n"
                + "        log(document.getElementById('dl1').getAttribute('compact'));\n"
                + "        log(document.getElementById('dl2').getAttribute('compact'));\n"
                + "        log(document.getElementById('dl3').getAttribute('compact'));\n"
                + "        log(document.getElementById('dl4').getAttribute('compact'));\n"

                + "        document.getElementById('dl1').compact = true;\n"
                + "        document.getElementById('dl2').compact = false;\n"
                + "        document.getElementById('dl3').compact = 'xyz';\n"
                + "        document.getElementById('dl4').compact = null;\n"
                + "        log(document.getElementById('dl1').compact);\n"
                + "        log(document.getElementById('dl2').compact);\n"
                + "        log(document.getElementById('dl3').compact);\n"
                + "        log(document.getElementById('dl4').compact);\n"
                + "        log(document.getElementById('dl1').getAttribute('compact'));\n"
                + "        log(document.getElementById('dl2').getAttribute('compact'));\n"
                + "        log(document.getElementById('dl3').getAttribute('compact'));\n"
                + "        log(document.getElementById('dl4').getAttribute('compact'));\n"
                + "      }\n"
                + "    </script>\n"
                + "  </head>\n"
                + "  <body onload='test()'>\n"
                + "    <dl id='dl1'><dt>a</dt><dd>b</dd></dl>\n"
                + "    <dl compact='' id='dl2'><dt>a</dt><dd>b</dd></dl>\n"
                + "    <dl compact='blah' id='dl3'><dt>a</dt><dd>b</dd></dl>\n"
                + "    <dl compact='2' id='dl4'><dt>a</dt><dd>b</dd></dl>\n"
                + "  </body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined", "undefined",
             "null", "", "blah", "A", "1", "a", "A", "i", "I", "u"})
    public void type() throws Exception {
        final String html =
                "<html>\n"
                + "  <head>\n"
                + "    <script>\n"
                + LOG_TITLE_FUNCTION
                + "      function test() {\n"
                + "        log(document.getElementById('dl1').type);\n"
                + "        log(document.getElementById('dl2').type);\n"
                + "        log(document.getElementById('dl3').type);\n"
                + "        log(document.getElementById('dl4').type);\n"
                + "        log(document.getElementById('dl1').getAttribute('type'));\n"
                + "        log(document.getElementById('dl2').getAttribute('type'));\n"
                + "        log(document.getElementById('dl3').getAttribute('type'));\n"
                + "        log(document.getElementById('dl4').getAttribute('type'));\n"

                + "        document.getElementById('dl1').type = '1';\n"
                + "        log(document.getElementById('dl1').type);\n"

                + "        document.getElementById('dl1').type = 'a';\n"
                + "        log(document.getElementById('dl1').type);\n"

                + "        document.getElementById('dl1').type = 'A';\n"
                + "        log(document.getElementById('dl1').type);\n"

                + "        document.getElementById('dl1').type = 'i';\n"
                + "        log(document.getElementById('dl1').type);\n"

                + "        document.getElementById('dl1').type = 'I';\n"
                + "        log(document.getElementById('dl1').type);\n"

                + "        try { document.getElementById('dl1').type = 'u' } catch(e) {log('exception');}\n"
                + "        log(document.getElementById('dl1').type);\n"
                + "      }\n"
                + "    </script>\n"
                + "  </head>\n"
                + "  <body onload='test()'>\n"
                + "    <dl id='dl1'><dt>a</dt><dd>b</dd></dl>\n"
                + "    <dl type='' id='dl2'><dt>a</dt><dd>b</dd></dl>\n"
                + "    <dl type='blah' id='dl3'><dt>a</dt><dd>b</dd></dl>\n"
                + "    <dl type='A' id='dl4'><dt>a</dt><dd>b</dd></dl>\n"
                + "  </body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }
}
