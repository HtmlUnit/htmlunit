/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlPage}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlPage3Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void emptyJavaScript() throws Exception {
        final String html = "<body>\n"
            + "<a id='myAnchor' href='javascript:'>Hello</a>\n"
            + "</body>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myAnchor")).click();
    }

    /**
     * Test for 3306491.
     * @throws Exception if an error occurs
     */
    @Test
    public void formElementCreatedFromJavascript() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script type='text/javascript'>\n"
            + "  function modifyForm() {\n"
            + "  var myForm = document.forms['test_form'];\n"
            + "  var el = document.createElement('input');\n"
            + "  el.setAttribute('addedBy','js');\n"
            + "  el.name = 'myHiddenField';\n"
            + "  el.value = 'myValue';\n"
            + "  el.type = 'hidden';\n"
            + "  myForm.appendChild(el);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onLoad='modifyForm()'>\n"
            + "  <form id='test_form' action='http://www.sourceforge.com/' method='post'>\n"
            + "    <input type='submit' value='click'/>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final List<WebElement> elements = driver.findElements(By.xpath("//*"));
        Assert.assertEquals(7, elements.size());

        Assert.assertEquals("html", elements.get(0).getTagName());
        Assert.assertEquals("head", elements.get(1).getTagName());
        Assert.assertEquals("script", elements.get(2).getTagName());
        Assert.assertEquals("body", elements.get(3).getTagName());
        Assert.assertEquals("form", elements.get(4).getTagName());
        Assert.assertEquals("input", elements.get(5).getTagName());

        final WebElement input = elements.get(6);
        Assert.assertEquals("input", input.getTagName());
        Assert.assertEquals("myHiddenField", input.getAttribute("name"));
        Assert.assertEquals("js", input.getAttribute("addedBy"));
        Assert.assertEquals("js", input.getAttribute("addedby"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = { "ISO-8859-1", "ISO-8859-1", "ISO-8859-1", "ISO-8859-1" },
            FF = { "ISO-8859-1", "ISO-8859-1", "undefined", "undefined" },
            IE = { "undefined", "undefined", "iso-8859-1", "windows-1252" },
            IE10 = { "ISO-8859-1", "iso-8859-1", "iso-8859-1", "windows-1252" })
    public void getPageEncoding() throws Exception {
        final String htmlContent = "<html><head>\n"
            + "  <title>foo</title>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=Shift_JIS'>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.inputEncoding);\n"
            + "      alert(document.characterSet);\n"
            + "      alert(document.charset);\n"
            + "      alert(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "<table><tr><td>\n"
            + "<meta name=vs_targetSchema content=\"http://schemas.microsoft.com/intellisense/ie5\">\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</td></tr></table>\n"
            + "</body></html>";
        loadPageWithAlerts2(htmlContent);
    }

}
