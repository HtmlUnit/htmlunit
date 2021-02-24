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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.PropertyInfo;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;

/**
 * Tests for {@link CSSStyleDeclaration}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class CSSStyleDeclarationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"black", "pink", "color: pink;", "color: pink;"})
    public void style_OneCssAttribute() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest() {\n"
            + "  var node = document.getElementById('div1');\n"
            + "  var style = node.style;\n"
            + "  alert(style.color);\n"
            + "  style.color = 'pink';\n"
            + "  alert(style.color);\n"
            + "  alert(node.getAttribute('style'));\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'><div id='div1' style='color: black'>foo</div></body></html>";

        final String style = getExpectedAlerts()[getExpectedAlerts().length - 1];

        setExpectedAlerts(Arrays.copyOf(getExpectedAlerts(), getExpectedAlerts().length - 1));
        final WebDriver driver = loadPageWithAlerts2(html);

        assertEquals(style, driver.findElement(By.id("div1")).getAttribute("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"black", "pink", "color: pink; background: blue none repeat scroll 0% 0%;"},
            CHROME = {"black", "pink", "color: pink; background: blue;"},
            EDGE = {"black", "pink", "color: pink; background: blue;"},
            IE = {"black", "pink", "background: blue; color: pink; foo: bar;"})
    @HtmlUnitNYI(CHROME = {"black", "pink", "color: pink; background: blue; foo: bar;"},
            EDGE = {"black", "pink", "color: pink; background: blue; foo: bar;"},
            FF = {"black", "pink", "color: pink; background: blue; foo: bar;"},
            FF78 = {"black", "pink", "color: pink; background: blue; foo: bar;"},
            IE = {"black", "pink", "color: pink; background: blue; foo: bar;"})
    public void style_MultipleCssAttributes() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "  var style = document.getElementById('div1').style;\n"
            + "  alert(style.color);\n"
            + "  style.color = 'pink';\n"
            + "  alert(style.color);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: black;background:blue;foo:bar'>foo</div></body></html>";

        final String style = getExpectedAlerts()[getExpectedAlerts().length - 1];

        setExpectedAlerts(Arrays.copyOf(getExpectedAlerts(), getExpectedAlerts().length - 1));
        final WebDriver driver = loadPageWithAlerts2(html);

        assertEquals(style, driver.findElement(By.id("div1")).getAttribute("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "", "pink", "color: pink;"})
    public void style_OneUndefinedCssAttribute() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest() {\n"
            + "  var style = document.getElementById('div1').style;\n"
            + "  alert(document.getElementById('nonexistingid'));\n"
            + "  alert(style.color);\n"
            + "  style.color = 'pink';\n"
            + "  alert(style.color);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'><div id='div1'>foo</div></body></html>";

        final String style = getExpectedAlerts()[getExpectedAlerts().length - 1];

        setExpectedAlerts(Arrays.copyOf(getExpectedAlerts(), getExpectedAlerts().length - 1));
        final WebDriver driver = loadPageWithAlerts2(html);

        assertEquals(style, driver.findElement(By.id("div1")).getAttribute("style"));
    }

    /**
     * Even if JavaScript isn't really executed according to the browser version used,
     * it may have some side effects if configuration is incorrect.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "hidden", "undefined"})
    public void mozillaStyle() throws Exception {
        final String content
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var oDiv = document.getElementById('div1');\n"
            + "  log(oDiv.style.visibility);\n"
            + "  oDiv.style.visibility = 'hidden';\n"
            + "  log(oDiv.style.visibility);\n"
            + "  log(oDiv.style.behavior);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1'>foo</div></body></html>";
        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void behavior() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var oDiv = document.getElementById('div1');\n"
            + "  log(oDiv.style.behavior);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1'>foo</div></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Checks that the scopes are correctly set on the style element (wasn't working in CVS snapshot 2005.01.23).
     * @throws Exception if the test fails
     */
    @Test
    public void onclickAccessStyle() throws Exception {
        final String html = "<html><head><title>Color Change Page</title>\n"
             + "<script>\n"
             + "function test(obj) {\n"
             + "  obj.style.backgroundColor = 'yellow';\n"
             + "}\n"
             + "</script>\n"
             + "</head>\n"
             + "<body>\n"
             + "<span id='red' onclick='test(this)'>foo</span>\n"
             + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("red")).click();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "string", "string", "undefined"})
    public void accessProperties() throws Exception {
        final String html = "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  var oDiv = document.getElementById('div1');\n"
                + "  log(typeof oDiv.style.visibility);\n"
                + "  log(typeof oDiv.style.color);\n"
                + "  log(typeof oDiv.style.backgroundImage);\n"
                + "  log(typeof oDiv.style.foo);\n"
                + "}\n</script></head>\n"
                + "<body onload='doTest()'>\n"
                + "<div id='div1'>foo</div></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for Bug #398.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void setStylePropertyNonString() throws Exception {
        final String html = "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  var oDiv1 = document.getElementById('div1');\n"
                + "  oDiv1.style.pixelLeft = 123;\n"
                + "  log(oDiv1.style.pixelLeft);\n"
                + "}\n</script></head>\n"
                + "<body onload='doTest()'>\n"
                + "<div id='div1'>foo</div></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "blue",
            FF = "blue none repeat scroll 0% 0%",
            FF78 = "blue none repeat scroll 0% 0%")
    @HtmlUnitNYI(FF = "blue",
            FF78 = "blue")
    public void getPropertyValue() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    var oDiv1 = document.getElementById('div1');\n"
            + "    log(oDiv1.style.getPropertyValue('background'));\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='background: blue'>foo</div></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"*blue* string", ""})
    public void removeProperty() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var oDiv1 = document.getElementById('div1');\n"
            + "  if (oDiv1.style.removeProperty) {\n"
            + "    var value = oDiv1.style.removeProperty('color');\n"
            + "    log('*' + value + '* ' + typeof(value));\n"
            + "    log(oDiv1.style.cssText);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: blue'>foo</div></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"** string", "blue"})
    public void removePropertyUnknown() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var oDiv1 = document.getElementById('div1');\n"
            + "  if (oDiv1.style.removeProperty) {\n"
            + "    var value = oDiv1.style.removeProperty('font-size');\n"
            + "    log('*' + value + '* ' + typeof(value));\n"
            + "    log(oDiv1.style.getPropertyValue('color'));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: blue'>foo</div></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"** string", "blue"})
    public void removePropertyUndefined() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var oDiv1 = document.getElementById('div1');\n"
            + "  if (!oDiv1.style.removeProperty) {\n"
            + "    log('removeProperty not available');\n"
            + "    return;\n"
            + "  }\n"
            + "  var value = oDiv1.style.removeProperty(undefined);\n"
            + "  log('*' + value + '* ' + typeof(value));\n"
            + "  log(oDiv1.style.getPropertyValue('color'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: blue'>foo</div></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"30px", "", "30px", "arial", "", "arial"})
    public void getPropertyValue_WithDash() throws Exception {
        final String html =
            "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "    function prop(elem, prop) {\n"
            + "      try{\n"
            + "        var p = span.style.getPropertyValue(prop);\n"
            + "        log(p);\n"
            + "      } catch (e) { log('exception'); }\n"
            + "    }\n"

            + "    function test() {\n"
            + "      var span = document.getElementById('span');\n"
            + "      span.style['fontSize'] = '30px';\n"
            + "      log(span.style.fontSize);\n"
            + "      prop(span, 'fontSize');\n"
            + "      prop(span, 'font-size');\n"
            + "      span.style['fontFamily'] = 'arial';\n"
            + "      log(span.style.fontFamily);\n"
            + "      prop(span, 'fontFamily');\n"
            + "      prop(span, 'font-family');\n"
            + "    }\n"
            + "</script>\n"
            + "<span id='span'>x</span>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    @HtmlUnitNYI(CHROME = {"", "alpha(opacity=50)"},
            EDGE = {"", "alpha(opacity=50)"},
            FF = {"", "alpha(opacity=50)"},
            FF78 = {"", "alpha(opacity=50)"},
            IE = {"", "alpha(opacity=50)"})
    public void styleFilter() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "   function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     log(div1.style.filter);\n"
            + "     var div2 = document.getElementById('div2');\n"
            + "     log(div2.style.filter);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div1'>foo</div>\n"
            + "<div id='div2' style='filter:alpha(opacity=50)'>bar</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that initializing <tt>opacity</tt> attribute to various values behaves correctly.
     * The whitespace in the various expected alerts is VERY important.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "0.5", "0.4", "0.33333", "-3", "3", "", "", ""})
    public void initOpacity() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='o1' style='opacity: '>d</div>\n"
            + "<div id='o2' style='opacity:0.5'>d</div>\n"
            + "<div id='o3' style='opacity:.4'>d</div>\n"
            + "<div id='o4' style='opacity: 0.33333'>d</div>\n"
            + "<div id='o5' style='opacity: -3'>d</div>\n"
            + "<div id='o6' style='opacity: 3'>d</div>\n"
            + "<div id='o7' style='opacity: 10px'>d</div>\n"
            + "<div id='o8' style='opacity: foo'>d</div>\n"
            + "<div id='o9' style='opacity: auto'>d</div>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "for (var i = 1; i < 10; i++) {\n"
            + "  d = document.getElementById('o' + i);\n"
            + "  log(d.style.opacity);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that setting <tt>element.style.opacity</tt> to various values behaves correctly.
     * The whitespace in the various expected alerts is VERY important.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("- 0.5 0.4 0.33333 -3 3 8 7 7 7 7 7 ")
    public void setOpacity() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d'>d</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var d = document.getElementById('d');\n"
            + "var s = '-';\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = 0.5;\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = .4;\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = 0.33333;\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = -3;\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = 3;\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = '8';\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = ' 7 ';\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = NaN;\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = '10px';\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = 'foo';\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = 'auto';\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = '';\n"
            + "s += d.style.opacity;\n"
            + "log(s);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "exception"})
    public void setExpression() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    log(typeof div1.style.setExpression);\n"
            + "    div1.style.setExpression('title','id');\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "exception"})
    public void removeExpression() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    log(typeof div1.style.removeExpression);\n"
            + "    div1.style.setExpression('title','id');\n"
            + "    div1.style.removeExpression('title');\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""})
    public void borderStyles_noStyle() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var oDiv = document.getElementById('div1');\n"
            + "  log(oDiv.style.borderBottom);\n"
            + "  log(oDiv.style.borderBottomColor);\n"
            + "  log(oDiv.style.borderBottomStyle);\n"
            + "  log(oDiv.style.borderBottomWidth);\n"
            + "  log(oDiv.style.borderLeft);\n"
            + "  log(oDiv.style.borderLeftColor);\n"
            + "  log(oDiv.style.borderLeftStyle);\n"
            + "  log(oDiv.style.borderLeftWidth);\n"
            + "  log(oDiv.style.borderRight);\n"
            + "  log(oDiv.style.borderRightColor);\n"
            + "  log(oDiv.style.borderRightStyle);\n"
            + "  log(oDiv.style.borderRightWidth);\n"
            + "  log(oDiv.style.borderTop);\n"
            + "  log(oDiv.style.borderTopColor);\n"
            + "  log(oDiv.style.borderTopStyle);\n"
            + "  log(oDiv.style.borderTopWidth);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1'>foo</div></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3px", "4px", "2px", "1px"})
    public void borderXxxWidth() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var oDiv = document.getElementById('div1');\n"
            + "  log(oDiv.style.borderBottomWidth);\n"
            + "  log(oDiv.style.borderLeftWidth);\n"
            + "  log(oDiv.style.borderRightWidth);\n"
            + "  log(oDiv.style.borderTopWidth);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='border-width: 1px 2px 3px 4px'>foo</div></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"thin", "medium", "thick", "thick"})
    public void borderXxxWidthConstants() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var oDiv = document.getElementById('div1');\n"
            + "  log(oDiv.style.borderRightWidth);\n"
            + "  oDiv = document.getElementById('div2');\n"
            + "  log(oDiv.style.borderLeftWidth);\n"
            + "  oDiv = document.getElementById('div3');\n"
            + "  log(oDiv.style.borderBottomWidth);\n"
            + "  log(oDiv.style.borderTopWidth);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='border: thin'>foo</div>\n"
            + "<div id='div2' style='border: medium'>foo</div>\n"
            + "<div id='div3' style='border: thick'>foo</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = "foo")
    public void initUnsupportdProperty() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='my' style='htmlunit: foo'>d</div>\n"

            + "<script>\n"
            + "  d = document.getElementById('my');\n"
            + "  alert(d.style.htmlunit);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "foo"})
    public void setUnsupportdProperty() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='my' style=''>d</div>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  d = document.getElementById('my');\n"
            + "  log(d.style.htmlunit);\n"
            + "  d.style.htmlunit = 'foo';\n"
            + "  log(d.style.htmlunit);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "", "1", "2", "2", "2", "2", "5", "5", "5", "5"})
    public void zIndex() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  log(typeof style.zIndex);\n"
            + "  log(style.zIndex);\n"
            + "  style.zIndex = 1;\n"
            + "  log(style.zIndex);\n"
            + "  style.zIndex = 2.0;\n"
            + "  log(style.zIndex);\n"
            + "  style.zIndex = 3.1;\n"
            + "  log(style.zIndex);\n"
            + "  style.zIndex = 4.5;\n"
            + "  log(style.zIndex);\n"
            + "  style.zIndex = 4.6;\n"
            + "  log(style.zIndex);\n"
            + "  style.zIndex = '5';\n"
            + "  log(style.zIndex);\n"
            + "  style.zIndex = '6.0';\n"
            + "  log(style.zIndex);\n"
            + "  style.zIndex = '7.1';\n"
            + "  log(style.zIndex);\n"
            + "  style.zIndex = '8.6';\n"
            + "  log(style.zIndex);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"string", "", "string", "", "string", "4", "string", "", "string", "", "string", ""},
            IE = {"string", "", "string", "", "number", "4", "string", "", "string", "", "string", ""})
    public void zIndexDefault() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var style = document.getElementById('divUndefined').style;\n"
            + "  log(typeof style.zIndex);\n"
            + "  log(style.zIndex);\n"

            + "  style = document.getElementById('divBlank').style;\n"
            + "  log(typeof style.zIndex);\n"
            + "  log(style.zIndex);\n"

            + "  style = document.getElementById('divInteger').style;\n"
            + "  log(typeof style.zIndex);\n"
            + "  log(style.zIndex);\n"

            + "  style = document.getElementById('divFloat').style;\n"
            + "  log(typeof style.zIndex);\n"
            + "  log(style.zIndex);\n"

            + "  style = document.getElementById('divFloat2').style;\n"
            + "  log(typeof style.zIndex);\n"
            + "  log(style.zIndex);\n"

            + "  style = document.getElementById('invalidDiv').style;\n"
            + "  log(typeof style.zIndex);\n"
            + "  log(style.zIndex);\n"

            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='divUndefined'/>\n"
            + "  <div id='divBlank' style='z-index: '/>\n"
            + "  <div id='divInteger' style='z-index: 4'/>\n"
            + "  <div id='divFloat' style='z-index: 4.2'/>\n"
            + "  <div id='divFloat2' style='z-index: 4.7'/>\n"
            + "  <div id='invalidDiv' style='z-index: unfug'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "1", "1"})
    public void zIndexSetUndefined() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  var un_defined;\n"
            + "  log(style.zIndex);\n"

            + "  try {\n"
            + "    style.zIndex = un_defined;\n"
            + "  } catch (e) { log('error'); }\n"
            + "  log(style.zIndex);\n"

            + "  style.zIndex = 1;\n"
            + "  log(style.zIndex);\n"

            + "  try {\n"
            + "    style.zIndex = un_defined;\n"
            + "  } catch (e) { log('error'); }\n"
            + "  log(style.zIndex);\n"

            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "1", ""})
    public void zIndexSetNull() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  log(style.zIndex);\n"

            + "  try {\n"
            + "    style.zIndex = null;\n"
            + "  } catch (e) { log('error'); }\n"
            + "  log(style.zIndex);\n"

            + "  style.zIndex = 1;\n"
            + "  log(style.zIndex);\n"

            + "  try {\n"
            + "    style.zIndex = null;\n"
            + "  } catch (e) { log('error'); }\n"
            + "  log(style.zIndex);\n"

            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "7", "7", "", "4", "1"})
    public void zIndexSetString() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  var unknown;\n"
            + "  log(style.zIndex);\n"

            + "  style.zIndex = '7';\n"
            + "  log(style.zIndex);\n"

            + "  style.zIndex = '7.6';\n"
            + "  log(style.zIndex);\n"

            + "  style.zIndex = '';\n"
            + "  log(style.zIndex);\n"

            + "  style.zIndex = '4';\n"
            + "  try {\n"
            + "    style.zIndex = '   ';\n"
            + "  } catch (e) { log('error'); }\n"
            + "  log(style.zIndex);\n"

            + "  style.zIndex = '1';\n"
            + "  try {\n"
            + "    style.zIndex = 'NAN';\n"
            + "  } catch (e) { log('error'); }\n"
            + "  log(style.zIndex);\n"

            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "1", "1"})
    public void zIndexSetInvalid() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  log(style.zIndex);\n"
            + "  try {\n"
            + "    style.zIndex = 'hallo';\n"
            + "  } catch (e) { log('error'); }\n"
            + "  log(style.zIndex);\n"

            + "  style.zIndex = 1;\n"
            + "  log(style.zIndex);\n"
            + "  try {\n"
            + "    style.zIndex = 'hallo';\n"
            + "  } catch (e) { log('error'); }\n"
            + "  log(style.zIndex);\n"
            + "}\n"

            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "15px", "italic", "", "italic"})
    public void cssText() throws Exception {
        final String html = "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var style = document.getElementById('myDiv').style;\n"
            + "    log(style.fontSize);\n"
            + "    log(style.fontStyle);\n"
            + "    style.cssText = 'font-size: 15px; font-style: italic';\n"
            + "    log(style.fontSize);\n"
            + "    log(style.fontStyle);\n"
            + "    style.cssText = 'font-style: italic';\n"
            + "    log(style.fontSize);\n"
            + "    log(style.fontStyle);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1px", "solid", "red"})
    public void border() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var style = document.getElementById('myDiv').style;\n"
            + "    if (style.getPropertyValue) {\n"
            + "      log(style.getPropertyValue('border-top-width'));\n"
            + "      log(style.getPropertyValue('border-top-style'));\n"
            + "      log(style.getPropertyValue('border-top-color'));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv' style='border: red 1px solid'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void display() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    myDiv.style.display = 'none';\n"
            + "    log(myDiv.style.display == 'none');\n"
            + "    myDiv.style.display = '';\n"
            + "    log(myDiv.style.display == 'none');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "", "none", "rgb(0, 128, 0)"},
            IE = {"inline", "rgb(0, 0, 0)", "none", "rgb(0, 128, 0)"})
    @HtmlUnitNYI(IE = {"none", "rgb(0, 128, 0)", "none", "rgb(0, 128, 0)"})
    public void displayDefaultOverwritesNone() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <style>\n"
            + "    tt { display: none; color: green; }\n"
            + "  </style>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var e = document.createElement('tt');\n"
            + "      var style = window.getComputedStyle(e, null);\n"
            + "      log(style['display']);\n"
            + "      log(style['color']);\n"
            + "      document.body.appendChild(e);\n"
            + "      log(style['display']);\n"
            + "      log(style['color']);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "", "inline", "rgb(0, 0, 0)"},
            IE = {"inline", "rgb(0, 0, 0)", "inline", "rgb(0, 0, 0)"})
    public void displayDefault() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var e = document.createElement('tt');\n"
            + "      var style = window.getComputedStyle(e, null);\n"
            + "      log(style['display']);\n"
            + "      log(style['color']);\n"
            + "      document.body.appendChild(e);\n"
            + "      log(style['display']);\n"
            + "      log(style['color']);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1px", "2px"})
    public void resettingValue() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    myDiv.style.marginTop = '1px';\n"
            + "    log(myDiv.style.marginTop);\n"
            + "    myDiv.style.marginTop = '2px';\n"
            + "    log(myDiv.style.marginTop);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2px", "30px"})
    public void resettingValue2() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    myDiv.style.marginTop = '2px';\n"
            + "    log(myDiv.style.marginTop);\n"
            + "    myDiv.style.left = '-1px';\n"
            + "    myDiv.style.marginTop = '30px';\n"
            + "    log(myDiv.style.marginTop);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that setting margins all at once and setting margins individually all work, both in static
     * styles and in calculated styles.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({
            "L:3px,R:3px,T:3px,B:3px", "L:5px,R:5px,T:5px,B:5px", "L:7px,R:2px,T:2px,B:2px",
            "L:3px,R:3px,T:3px,B:3px", "L:5px,R:5px,T:5px,B:5px", "L:7px,R:2px,T:2px,B:2px"})
    public void marginAllvsMarginSingle() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <style>\n"
            + "      #m1 { margin: 3px; }\n"
            + "      #m2 { margin-left: 3px; margin: 5px; }\n"
            + "      #m3 { margin: 2px; margin-left: 7px; }\n"
            + "    </style>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        alertComputedMargins('m1');\n"
            + "        alertComputedMargins('m2');\n"
            + "        alertComputedMargins('m3');\n"
            + "        alertNonComputedMargins('m4');\n"
            + "        alertNonComputedMargins('m5');\n"
            + "        alertNonComputedMargins('m6');\n"
            + "      }\n"
            + "      function alertComputedMargins(id) {\n"
            + "        var e = document.getElementById(id);\n"
            + "        var s = getComputedStyle(e, null);\n"
            + "        log('L:' + s.marginLeft + ',R:' + s.marginRight +\n"
            + "          ',T:' + s.marginTop + ',B:' + s.marginBottom);\n"
            + "      }\n"
            + "      function alertNonComputedMargins(id) {\n"
            + "        var e = document.getElementById(id);\n"
            + "        var s = e.style;\n"
            + "        log('L:' + s.marginLeft + ',R:' + s.marginRight +\n"
            + "          ',T:' + s.marginTop + ',B:' + s.marginBottom);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <div id='m1'>m1</div>\n"
            + "    <div id='m2'>m2</div>\n"
            + "    <div id='m3'>m3</div>\n"
            + "    <div id='m4' style='margin: 3px;'>m4</div>\n"
            + "    <div id='m5' style='margin-left: 3px; margin: 5px;'>m5</div>\n"
            + "    <div id='m6' style='margin: 2px; margin-left: 7px;'>m6</div>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that setting paddings all at once and setting paddings individually all work, both in static
     * styles and in calculated styles.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({
        "L:3px,R:3px,T:3px,B:3px", "L:5px,R:5px,T:5px,B:5px", "L:7px,R:2px,T:2px,B:2px",
        "L:3px,R:3px,T:3px,B:3px", "L:5px,R:5px,T:5px,B:5px", "L:7px,R:2px,T:2px,B:2px"})
    public void paddingAllvsPaddingSingle() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <style>\n"
            + "      #m1 { padding: 3px; }\n"
            + "      #m2 { padding-left: 3px; padding: 5px; }\n"
            + "      #m3 { padding: 2px; padding-left: 7px; }\n"
            + "    </style>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        alertComputedPaddings('m1');\n"
            + "        alertComputedPaddings('m2');\n"
            + "        alertComputedPaddings('m3');\n"
            + "        alertNonComputedPaddings('m4');\n"
            + "        alertNonComputedPaddings('m5');\n"
            + "        alertNonComputedPaddings('m6');\n"
            + "      }\n"
            + "      function alertComputedPaddings(id) {\n"
            + "        var e = document.getElementById(id);\n"
            + "        var s = e.currentStyle ? e.currentStyle : getComputedStyle(e, null);\n"
            + "        log('L:' + s.paddingLeft + ',R:' + s.paddingRight +\n"
            + "          ',T:' + s.paddingTop + ',B:' + s.paddingBottom);\n"
            + "      }\n"
            + "      function alertNonComputedPaddings(id) {\n"
            + "        var e = document.getElementById(id);\n"
            + "        var s = e.style;\n"
            + "        log('L:' + s.paddingLeft + ',R:' + s.paddingRight +\n"
            + "          ',T:' + s.paddingTop + ',B:' + s.paddingBottom);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <div id='m1'>m1</div>\n"
            + "    <div id='m2'>m2</div>\n"
            + "    <div id='m3'>m3</div>\n"
            + "    <div id='m4' style='padding: 3px;'>m4</div>\n"
            + "    <div id='m5' style='padding-left: 3px; padding: 5px;'>m5</div>\n"
            + "    <div id='m6' style='padding: 2px; padding-left: 7px;'>m6</div>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Tests setting attributes (like padding and margin) using shorthand notation.
     * @throws Exception if an error occurs
     */
    @Test
    public void styleShorthand() throws Exception {
        styleShorthand("margin: 10px", "marginTop", "10px");
        styleShorthand("margin: 10px", "marginLeft", "10px");
        styleShorthand("margin: 10px", "marginRight", "10px");
        styleShorthand("margin: 10px", "marginBottom", "10px");

        styleShorthand("margin: 10px 20px", "marginTop", "10px");
        styleShorthand("margin: 10px 20px", "marginLeft", "20px");
        styleShorthand("margin: 10px 20px", "marginRight", "20px");
        styleShorthand("margin: 10px 20px", "marginBottom", "10px");

        styleShorthand("margin: 10px 20px 30px", "marginTop", "10px");
        styleShorthand("margin: 10px 20px 30px", "marginLeft", "20px");
        styleShorthand("margin: 10px 20px 30px", "marginRight", "20px");
        styleShorthand("margin: 10px 20px 30px", "marginBottom", "30px");

        styleShorthand("margin: 10px 20px 30px 40px", "marginTop", "10px");
        styleShorthand("margin: 10px 20px 30px 40px", "marginLeft", "40px");
        styleShorthand("margin: 10px 20px 30px 40px", "marginRight", "20px");
        styleShorthand("margin: 10px 20px 30px 40px", "marginBottom", "30px");

        styleShorthand("padding: 10px", "paddingTop", "10px");
        styleShorthand("padding: 10px", "paddingLeft", "10px");
        styleShorthand("padding: 10px", "paddingRight", "10px");
        styleShorthand("padding: 10px", "paddingBottom", "10px");

        styleShorthand("padding: 10px 20px", "paddingTop", "10px");
        styleShorthand("padding: 10px 20px", "paddingLeft", "20px");
        styleShorthand("padding: 10px 20px", "paddingRight", "20px");
        styleShorthand("padding: 10px 20px", "paddingBottom", "10px");

        styleShorthand("padding: 10px 20px 30px", "paddingTop", "10px");
        styleShorthand("padding: 10px 20px 30px", "paddingLeft", "20px");
        styleShorthand("padding: 10px 20px 30px", "paddingRight", "20px");
        styleShorthand("padding: 10px 20px 30px", "paddingBottom", "30px");

        styleShorthand("padding: 10px 20px 30px 40px", "paddingTop", "10px");
        styleShorthand("padding: 10px 20px 30px 40px", "paddingLeft", "40px");
        styleShorthand("padding: 10px 20px 30px 40px", "paddingRight", "20px");
        styleShorthand("padding: 10px 20px 30px 40px", "paddingBottom", "30px");
    }

    private void styleShorthand(final String style, final String attribute, final String expectedValue)
        throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) {\n"
            + "    var ta = document.getElementById('myTextArea');\n"
            + "    ta.value += msg + '; ';\n"
            + "  }\n"
            + "function test() {\n"
            + "  var style = document.getElementById('d').style;\n"
            + "  log(style." + attribute + ");\n"
            + "}\n</script>\n"
            + "</head>\n"
            + "<body onload='test()'><div id='d' style='" + style + "'>foo</div>\n"
            + "  <textarea id='myTextArea' cols='80' rows='30'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(expectedValue + "; ", textArea.getAttribute("value"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "")
    public void getAttribute() throws Exception {
        getAttribute("\"font\"");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "")
    public void getAttributeFont() throws Exception {
        getAttribute("'font'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "green")
    public void getAttributeColor() throws Exception {
        getAttribute("'color'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "green")
    public void getAttributeColorCase() throws Exception {
        getAttribute("'ColoR'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "")
    public void getAttributeFont0() throws Exception {
        getAttribute("'font', 0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "green")
    public void getAttributeColor0() throws Exception {
        getAttribute("'color', 0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "green")
    public void getAttributeColorCase0() throws Exception {
        getAttribute("'coLOr', 0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "")
    public void getAttributeFont1() throws Exception {
        getAttribute("'font', 1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "green")
    public void getAttributeColor1() throws Exception {
        getAttribute("'color', 1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "green")
    public void getAttributeColorCase1() throws Exception {
        getAttribute("'ColOR', 1");
    }

    private void getAttribute(final String params) throws Exception {
        final String html =
              "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (document.all['a'].style.getAttribute) {\n"
            + "    log(document.all['a'].style.getAttribute(" + params + "));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<a id='a' href='#' style='color:green'>go</a></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'font', 'blah'", "green", "green"})
    public void setAttributeFont() throws Exception {
        setAttribute("'font', 'blah'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'color', 'red'", "green", "red"})
    public void setAttributeColor() throws Exception {
        setAttribute("'color', 'red'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'ColoR', 'red'", "green", "red"})
    public void setAttributeColorCase() throws Exception {
        setAttribute("'ColoR', 'red'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'font', 'blah', 0", "green", "green"})
    public void setAttributeFont0() throws Exception {
        setAttribute("'font', 'blah', 0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'color', 'red', 0", "green", "red"})
    public void setAttributeColor0() throws Exception {
        setAttribute("'color', 'red', 0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'ColoR', 'red', 0", "green", "red"})
    public void setAttributeColorCase0() throws Exception {
        setAttribute("'ColoR', 'red', 0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'font', 'blah', 1", "green", "green"})
    public void setAttributeFont1() throws Exception {
        setAttribute("'font', 'blah', 1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'color', 'red', 1", "green", "red"})
    public void setAttributeColor1() throws Exception {
        setAttribute("'color', 'red', 1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'ColoR', 'red', 1", "green", "red"})
    public void setAttributeColorCase1() throws Exception {
        setAttribute("'ColoR', 'red', 1");
    }

    private void setAttribute(final String params) throws Exception {
        final String html =
              "<html><body onload='test()'>\n"
            + "<a id='a' href='#' style='color:green'>go</a>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (document.all['a'].style.getAttribute) {\n"
            + "      log(\"" + params + "\");\n"
            + "      log(document.all['a'].style.getAttribute('color'));\n"
            + "      document.all['a'].style.setAttribute(" + params + ");\n"
            + "      log(document.all['a'].style.getAttribute('color'));\n"
            + "    }\n"
            + "    else {\n"
            + "      log('not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'font'", "green", "false", "green"})
    public void removeAttributeFont() throws Exception {
        removeAttribute("'font'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'color'", "green", "true", ""})
    public void removeAttributeColor() throws Exception {
        removeAttribute("'color'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'ColoR'", "green", "true", ""})
    public void removeAttributeColorCase() throws Exception {
        removeAttribute("'ColoR'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'font', 0", "green", "false", "green"})
    public void removeAttributeFont0() throws Exception {
        removeAttribute("'font', 0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'color', 0", "green", "true", ""})
    public void removeAttributeColor0() throws Exception {
        removeAttribute("'color', 0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'ColoR', 0", "green", "true", ""})
    public void removeAttributeColorCase0() throws Exception {
        removeAttribute("'ColoR', 0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'font', 1", "green", "false", "green"})
    public void removeAttributeFont1() throws Exception {
        removeAttribute("'font', 1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'color', 1", "green", "true", ""})
    public void removeAttributeColor1() throws Exception {
        removeAttribute("'color', 1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "not supported",
            IE = {"'ColoR', 1", "green", "true", ""})
    public void removeAttributeColorCase1() throws Exception {
        removeAttribute("'ColoR', 1");
    }

    private void removeAttribute(final String params) throws Exception {
        final String html =
              "<html><body onload='test()'>\n"
            + "<a id='a' href='#' style='color:green'>go</a>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (document.all['a'].style.getAttribute) {\n"
            + "      log(\"" + params + "\");\n"
            + "      log(document.all['a'].style.getAttribute('color'));\n"
            + "      log(document.all['a'].style.removeAttribute(" + params + "));\n"
            + "      log(document.all['a'].style.getAttribute('color'));\n"
            + "    }\n"
            + "    else {\n"
            + "      log('not supported');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"red ", "black ", "blue important", "gray ", "green "})
    public void setProperty() throws Exception {
        final String[] expected = getExpectedAlerts();
        setPropertyBackgroundColor("'background-color', 'red', ''", expected[0]);
        setPropertyBackgroundColor("'background-ColoR', 'black', ''", expected[1]);
        setPropertyBackgroundColor("'background-color', 'blue', 'important'", expected[2]);
        setPropertyBackgroundColor("'background-color', 'gray', null", expected[3]);
        setPropertyBackgroundColor("'background-color', 'green', undefined", expected[4]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"green ", "black important", "green "})
    public void setPropertyImportant() throws Exception {
        final String[] expected = getExpectedAlerts();
        setPropertyBackgroundColor("'background-color', 'white', 'crucial'", expected[0]);
        setPropertyBackgroundColor("'background-color', 'black', 'imPORTant'", expected[1]);
        setPropertyBackgroundColor("'background-color', 'blue', 'important '", expected[2]);
    }

    private void setPropertyBackgroundColor(final String params, final String... expected) throws Exception {
        final String html =
              "<html><body onload='test()'>\n"
            + "<a id='a' href='#' style='background-color:green'>go</a>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var node = document.getElementById('a');\n"
            + "    try {\n"
            + "      node.style.setProperty(" + params + ");\n"
            + "      alert(node.style.backgroundColor + ' ' + node.style.getPropertyPriority('background-color'));\n"
            + "    } catch(e) { alert(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        setExpectedAlerts(expected);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setWidthProperty() throws Exception {
        setLengthProperty("width", "width");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setWidth() throws Exception {
        setLength("width", "width");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setHeightProperty() throws Exception {
        setLengthProperty("height", "height");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setHeight() throws Exception {
        setLength("height", "height");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setTopProperty() throws Exception {
        setLengthProperty("top", "top");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setTop() throws Exception {
        setLength("top", "top");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setLeftProperty() throws Exception {
        setLengthProperty("left", "left");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setLeft() throws Exception {
        setLength("left", "left");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setBottomProperty() throws Exception {
        setLengthProperty("bottom", "bottom");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setBottom() throws Exception {
        setLength("bottom", "bottom");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setRightProperty() throws Exception {
        setLengthProperty("right", "right");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setRight() throws Exception {
        setLength("right", "right");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                         "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMarginTopProperty() throws Exception {
        setLengthProperty("margin-top", "marginTop");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMarginTop() throws Exception {
        setLength("margin-top", "marginTop");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMarginLeftProperty() throws Exception {
        setLengthProperty("margin-left", "marginLeft");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMarginLeft() throws Exception {
        setLength("margin-left", "marginLeft");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMarginBottomProperty() throws Exception {
        setLengthProperty("margin-bottom", "marginBottom");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMarginBottom() throws Exception {
        setLength("margin-bottom", "marginBottom");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMarginRightProperty() throws Exception {
        setLengthProperty("margin-right", "marginRight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMarginRight() throws Exception {
        setLength("margin-right", "marginRight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setPaddingTopProperty() throws Exception {
        setLengthProperty("padding-top", "paddingTop");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setPaddingTop() throws Exception {
        setLength("padding-top", "paddingTop");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setPaddingLeftProperty() throws Exception {
        setLengthProperty("padding-left", "paddingLeft");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setPaddingLeft() throws Exception {
        setLength("padding-left", "paddingLeft");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setPaddingBottomProperty() throws Exception {
        setLengthProperty("padding-bottom", "paddingBottom");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setPaddingBottom() throws Exception {
        setLength("padding-bottom", "paddingBottom");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setPaddingRightProperty() throws Exception {
        setLengthProperty("padding-right", "paddingRight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setPaddingRight() throws Exception {
        setLength("padding-right", "paddingRight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setBorderTopWidthProperty() throws Exception {
        setLengthProperty("border-top-width", "borderTopWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setBorderTopWidth() throws Exception {
        setLength("border-top-width", "borderTopWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                            "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                            "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setBorderLeftWidthProperty() throws Exception {
        setLengthProperty("border-left-width", "borderLeftWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setBorderLeftWidth() throws Exception {
        setLength("border-left-width", "borderLeftWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                            "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                            "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setBorderBottomWidthProperty() throws Exception {
        setLengthProperty("border-bottom-width", "borderBottomWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setBorderBottomWidth() throws Exception {
        setLength("border-bottom-width", "borderBottomWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setBorderRightWidthProperty() throws Exception {
        setLengthProperty("border-right-width", "borderRightWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setBorderRightWidth() throws Exception {
        setLength("border-right-width", "borderRightWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMaxWidthProperty() throws Exception {
        setLengthProperty("max-width", "maxWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMaxWidth() throws Exception {
        setLength("max-width", "maxWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMinWidthProperty() throws Exception {
        setLengthProperty("min-width", "minWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMinWidth() throws Exception {
        setLength("min-width", "minWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMaxHeightProperty() throws Exception {
        setLengthProperty("max-height", "maxHeight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMaxHeight() throws Exception {
        setLength("max-height", "maxHeight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMinHeightProperty() throws Exception {
        setLengthProperty("min-height", "minHeight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "auto", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setMinHeight() throws Exception {
        setLength("min-height", "minHeight");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setTextIndentProperty() throws Exception {
        setLengthProperty("text-indent", "textIndent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setTextIndent() throws Exception {
        setLength("text-indent", "textIndent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setFontSizeProperty() throws Exception {
        setLengthProperty("font-size", "fontSize");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setFontSize() throws Exception {
        setLength("font-size", "fontSize");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            FF = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            FF78 = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setWordSpacingProperty() throws Exception {
        setLengthProperty("word-spacing", "wordSpacing");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px", "17px"},
            FF = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px", "17px"},
            FF78 = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                    "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px", "17px"})
    public void setWordSpacing() throws Exception {
        setLength("word-spacing", "wordSpacing");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setLetterSpacingProperty() throws Exception {
        setLengthProperty("letter-spacing", "letterSpacing");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setLetterSpacing() throws Exception {
        setLength("letter-spacing", "letterSpacing");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "7%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "7%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setVerticalAlignProperty() throws Exception {
        setLengthProperty("vertical-align", "verticalAlign");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"4px", "5px", "6em", "17px", "70%", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "auto", "70%", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setVerticalAlign() throws Exception {
        setLength("vertical-align", "verticalAlign");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"17px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setOutlineWidthProperty() throws Exception {
        setLengthProperty("outline-width", "outlineWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"thin", "medium", "thick"})
    public void setOutlineWidthProperty2() throws Exception {
        final String[] expected = getExpectedAlerts();
        setLengthProperty("outline-width", "outlineWidth", "'thin', ''", expected[0]);
        setLengthProperty("outline-width", "outlineWidth", "'medium', ''", expected[1]);
        setLengthProperty("outline-width", "outlineWidth", "'thick', ''", expected[2]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"17px", "5px", "6em", "17px", "17px", "initial", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"},
            IE = {"4px", "5px", "6em", "17px", "17px", "17px", "inherit",
                        "17px", "17px", "17px", "", "17px", "", "17px", "17px", "17px"})
    public void setOutlineWidth() throws Exception {
        setLength("outline-width", "outlineWidth");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"thin", "medium", "thick"})
    public void setOutlineWidth2() throws Exception {
        final String[] expected = getExpectedAlerts();
        setLength("outline-width", "outlineWidth", "'thin'", expected[0]);
        setLength("outline-width", "outlineWidth", "'medium'", expected[1]);
        setLength("outline-width", "outlineWidth", "'thick'", expected[2]);
    }

    private void setLengthProperty(final String cssProp, final String prop) throws Exception {
        final String[] expected = getExpectedAlerts();
        setLengthProperty(cssProp, prop, "'4', ''", expected[0]);
        setLengthProperty(cssProp, prop, "'5px', ''", expected[1]);
        setLengthProperty(cssProp, prop, "'6em', ''", expected[2]);
        setLengthProperty(cssProp, prop, "'auto', ''", expected[3]);
        setLengthProperty(cssProp, prop, "'7%', ''", expected[4]);
        setLengthProperty(cssProp, prop, "'initial', ''", expected[5]);
        setLengthProperty(cssProp, prop, "'inherit', ''", expected[6]);
        setLengthProperty(cssProp, prop, "'ellen', ''", expected[7]);
        setLengthProperty(cssProp, prop, "undefined, ''", expected[8]);
        setLengthProperty(cssProp, prop, "'undefined', ''", expected[9]);
        setLengthProperty(cssProp, prop, "'', null", expected[10]);
        setLengthProperty(cssProp, prop, "NaN, ''", expected[11]);
        setLengthProperty(cssProp, prop, "null, ''", expected[12]);
        setLengthProperty(cssProp, prop, "'NaNpx', ''", expected[13]);
        setLengthProperty(cssProp, prop, "true, ''", expected[14]);
        setLengthProperty(cssProp, prop, "Infinity, ''", expected[15]);
    }

    private void setLength(final String cssProp, final String prop) throws Exception {
        final String[] expected = getExpectedAlerts();
        setLength(cssProp, prop, "4", expected[0]);
        setLength(cssProp, prop, "'5px'", expected[1]);
        setLength(cssProp, prop, "'6em'", expected[2]);
        setLength(cssProp, prop, "'auto'", expected[3]);
        setLength(cssProp, prop, "'70%'", expected[4]);
        setLength(cssProp, prop, "'initial'", expected[5]);
        setLength(cssProp, prop, "'inherit'", expected[6]);
        setLength(cssProp, prop, "'ellen'", expected[7]);
        setLength(cssProp, prop, "undefined", expected[8]);
        setLength(cssProp, prop, "'undefined'", expected[9]);
        setLength(cssProp, prop, "''", expected[10]);
        setLength(cssProp, prop, "NaN", expected[11]);
        setLength(cssProp, prop, "null", expected[12]);
        setLength(cssProp, prop, "'NaNpx'", expected[13]);
        setLength(cssProp, prop, "true", expected[14]);
        setLength(cssProp, prop, "Infinity", expected[15]);
    }

    private void setLengthProperty(final String cssProp, final String prop,
                    final String params, final String... expected) throws Exception {
        final String html =
              "<html><body onload='test()'>\n"
            + "<a id='a' href='#' style='" + cssProp + ":17px'>go</a>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var node = document.getElementById('a');\n"
            + "    try {\n"
            + "      node.style.setProperty('" + cssProp + "', " + params + ");\n"
            + "      log(node.style." + prop + ");\n"
            + "    } catch(e) { log(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html, expected);
    }

    private void setLength(final String cssProp, final String prop,
                    final String params, final String... expected) throws Exception {
        final String html =
              "<html><body onload='test()'>\n"
            + "<a id='a' href='#' style='" + cssProp + ":17px'>go</a>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var node = document.getElementById('a');\n"
            + "    try {\n"
            + "      node.style." + prop + " = " + params + ";\n"
            + "      log(node.style." + prop + ");\n"
            + "    } catch(e) { log(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html, expected);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "important", "", "important"})
    public void getPropertyPriority() throws Exception {
        final String html =
                "<html><body onload='test()'>\n"
              + "<a id='a1' href='#' style='color:green'>go</a>\n"
              + "<a id='a2' href='#' style='color:blue !important'>go</a>\n"

              + "<a id='a3' href='#' style='background-color:green'>go</a>\n"
              + "<a id='a4' href='#' style='background-color:blue !important'>go</a>\n"

              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "  function test() {\n"
              + "    var node = document.getElementById('a1');\n"
              + "    if (node.style.getPropertyPriority) {\n"
              + "      log(node.style.getPropertyPriority('color'));\n"

              + "      node = document.getElementById('a2');\n"
              + "      log(node.style.getPropertyPriority('color'));\n"

              + "      node = document.getElementById('a3');\n"
              + "      log(node.style.getPropertyPriority('background-color'));\n"

              + "      node = document.getElementById('a4');\n"
              + "      log(node.style.getPropertyPriority('background-color'));\n"
              + "    } else {\n"
              + "      log('not supported');\n"
              + "    }\n"
              + "  }\n"
              + "</script>\n"
              + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"black", "pink", "color: pink;", "color: pink;"})
    public void caseInsensitive() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest() {\n"
            + "  var node = document.getElementById('div1');\n"
            + "  var style = node.style;\n"
            + "  alert(style.color);\n"
            + "  style.color = 'pink';\n"
            + "  alert(style.color);\n"
            + "  alert(node.getAttribute('style'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'><div id='div1' style='COLOR: BLACK'>foo</div></body></html>";

        final String style = getExpectedAlerts()[getExpectedAlerts().length - 1];

        setExpectedAlerts(Arrays.copyOf(getExpectedAlerts(), getExpectedAlerts().length - 1));
        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals(style, driver.findElement(By.id("div1")).getAttribute("style"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"5px", "undefined", "1em", "undefined"},
            IE = {"5px", "5", "1em", "16", "30px", "30"})
    public void pixelLeft() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='a' style='left: 5px; border: 1px solid black;'>a</div>\n"
            + "<div id='b' style='left: 1em; border: 1px solid black;'>b</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var a = document.getElementById('a');\n"
            + "  var b = document.getElementById('b');\n"
            + "  log(a.style.left);\n"
            + "  log(a.style.pixelLeft);\n"
            + "  log(b.style.left);\n"
            + "  log(b.style.pixelLeft);\n"
            + "  if(a.style.pixelLeft) {\n"
            + "    a.style.pixelLeft = 30;\n"
            + "    log(a.style.left);\n"
            + "    log(a.style.pixelLeft);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"5px", "undefined", "1em", "undefined"},
            IE = {"5px", "5", "1em", "16", "30px", "30"})
    public void pixelRight() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='a' style='right: 5px; border: 1px solid black;'>a</div>\n"
            + "<div id='b' style='right: 1em; border: 1px solid black;'>b</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var a = document.getElementById('a');\n"
            + "  var b = document.getElementById('b');\n"
            + "  log(a.style.right);\n"
            + "  log(a.style.pixelRight);\n"
            + "  log(b.style.right);\n"
            + "  log(b.style.pixelRight);\n"
            + "  if(a.style.pixelRight) {\n"
            + "    a.style.pixelRight = 30;\n"
            + "    log(a.style.right);\n"
            + "    log(a.style.pixelRight);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"5px", "undefined", "1em", "undefined"},
            IE = {"5px", "5", "1em", "16", "30px", "30"})
    public void pixelTop() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='a' style='top: 5px; border: 1px solid black;'>a</div>\n"
            + "<div id='b' style='top: 1em; border: 1px solid black;'>b</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var a = document.getElementById('a');\n"
            + "  var b = document.getElementById('b');\n"
            + "  log(a.style.top);\n"
            + "  log(a.style.pixelTop);\n"
            + "  log(b.style.top);\n"
            + "  log(b.style.pixelTop);\n"
            + "  if(a.style.pixelTop) {\n"
            + "    a.style.pixelTop = 30;\n"
            + "    log(a.style.top);\n"
            + "    log(a.style.pixelTop);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"5px", "undefined", "1em", "undefined"},
            IE = {"5px", "5", "1em", "16", "30px", "30"})
    public void pixelBottom() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='a' style='bottom: 5px; border: 1px solid black;'>a</div>\n"
            + "<div id='b' style='bottom: 1em; border: 1px solid black;'>b</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var a = document.getElementById('a');\n"
            + "  var b = document.getElementById('b');\n"
            + "  log(a.style.bottom);\n"
            + "  log(a.style.pixelBottom);\n"
            + "  log(b.style.bottom);\n"
            + "  log(b.style.pixelBottom);\n"
            + "  if(a.style.pixelBottom) {\n"
            + "    a.style.pixelBottom = 30;\n"
            + "    log(a.style.bottom);\n"
            + "    log(a.style.pixelBottom);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for a __lookupSetter__ bug affecting Hotmail when emulating Firefox.
     * @see <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=491433">Rhino bug 491433</a>
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "none"},
            CHROME = {"undefined", "before", "none", "exception"},
            EDGE = {"undefined", "before", "none", "exception"},
            IE = {"function", "before", "none", "after", "none"})
    @HtmlUnitNYI(CHROME = {"function", "before", "none", "after", "none"},
            EDGE = {"function", "before", "none", "after", "none"},
            FF = {"function", "before", "none", "after", "none"},
            FF78 = {"function", "before", "none", "after", "none"})
    public void interceptSetter() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<div id='d'>foo</div>\n"
            + "<script>\n"
            + "  try {\n"
            + "    var css = window.CSSStyleDeclaration;\n"
            + "    var oldDisplay = css.prototype.__lookupSetter__('display');\n"
            + "    alert(typeof oldDisplay);\n"

            + "    var newDisplay = function(x){\n"
            + "                       alert('before');\n"
            + "                       alert(x);\n"
            + "                       oldDisplay.call(this, x);\n"
            + "                       alert('after');\n"
            + "                     };\n"
            + "    css.prototype.__defineSetter__('display', newDisplay);\n"

            + "    var div = document.getElementById('d');\n"
            + "    div.style.display = 'none';\n"
            + "    alert(div.style.display);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", ""})
    public void setToNull() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  alert(div1.style.border);\n"
            + "  try {\n"
            + "    div1.style.border = null;\n"
            + "  } catch (e) {\n"
            + "    alert('error');\n"
            + "  }\n"
            + "  alert(div1.style.border);\n"
            + "  alert(div1.style.display);\n"
            + "  try {\n"
            + "    div1.style.display = null;\n"
            + "  } catch (e) {\n"
            + "    alert('error');\n"
            + "  }\n"
            + "  alert(div1.style.display);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<div id='div1'>foo</div></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1", "width", "undefined", "undefined"},
            IE = {"1", "width", "", "undefined"})
    public void length() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var a = document.createElement('div');\n"
            + "    a.style.cssText = 'width: 100%';\n"
            + "    log(a.style.length);\n"
            + "    log(a.style[0]);\n"
            + "    log(a.style[1]);\n"
            + "    log(a.style[-1]);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "border-box"})
    public void boxSizing() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var style = document.getElementById('test').style;\n"
            + "    log(style.boxSizing === '');\n"

            + "    style = document.createElement('div').style;\n"
            + "    log(style.boxSizing === '');\n"
            + "    style.boxSizing = 'border-box';\n"
            + "    log(style.boxSizing);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='test'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Another one from the jQuery browser support tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"auto", ""},
            IE = {"auto", "auto"})
    public void jQueryPixelPosition() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('test');\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    alert(style.top);\n"

            + "    div = document.createElement('div');\n"
            + "    style = window.getComputedStyle(div, null);\n"
            + "    alert(style.top);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='test'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests that all getters and setters {@link CSSStyleDeclaration} have correct browser support
     * as defined in {@link StyleAttributes}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void styleAttributes() throws Exception {
        final List<String> allProperties = new ArrayList<>();
        for (final BrowserVersion browserVersion : allBrowsers()) {
            final ClassConfiguration config
                = AbstractJavaScriptConfiguration.getClassConfiguration(CSSStyleDeclaration.class, browserVersion);
            for (final Definition definition : StyleAttributes.getDefinitions(browserVersion)) {
                if (!definition.name().endsWith("_")) {
                    final String propertyName = definition.getPropertyName();
                    final PropertyInfo info = config.getPropertyMap().get(propertyName);
                    if (info != null) {
                        allProperties.add(propertyName);
                    }
                }
            }
        }

        final BrowserVersion browserVersion = getBrowserVersion();
        final ClassConfiguration config
            = AbstractJavaScriptConfiguration.getClassConfiguration(CSSStyleDeclaration.class, browserVersion);
        for (final Definition definition : StyleAttributes.getDefinitions(browserVersion)) {
            if (!definition.name().endsWith("_")) {
                final String propertyName = definition.getPropertyName();
                final PropertyInfo info = config.getPropertyMap().get(propertyName);
                if (allProperties.contains(propertyName)
                        && (info == null || info.getReadMethod() == null || info.getWriteMethod() == null)) {
                    fail("CSSStyleDeclaration: " + propertyName + " must support " + browserVersion.getNickname());
                }
            }
        }

        for (final String propertyName : config.getPropertyMap().keySet()) {
            if (!"length".equals(propertyName) && StyleAttributes.getDefinition(propertyName, browserVersion) == null) {
                fail("CSSStyleDeclaration: incorrectly defines " + propertyName
                        + " for " + browserVersion.getNickname());
            }
        }
    }

    /**
     * Ensures no default implementation is being used.
     *
     * When no JavaScript method is defined, {@link StyleAttributes} values are used, this can be overridden only
     * when a different implementation is needed.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void defaultImplementation() throws Exception {
        final BrowserVersion browserVersion = getBrowserVersion();
        final ClassConfiguration config
            = AbstractJavaScriptConfiguration.getClassConfiguration(CSSStyleDeclaration.class, browserVersion);
        final Map<String, PropertyInfo> propertyMap = config.getPropertyMap();
        final File cssFolder = new File("src/main/java/com/gargoylesoftware/htmlunit/javascript/host/css/");
        final List<String> cssLines = FileUtils.readLines(new File(cssFolder, "CSSStyleDeclaration.java"), ISO_8859_1);
        final List<String> computedLines = FileUtils.readLines(new File(cssFolder, "ComputedCSSStyleDeclaration.java"),
                ISO_8859_1);
        for (final Map.Entry<String, PropertyInfo> entry : propertyMap.entrySet()) {
            final PropertyInfo info = entry.getValue();
            if (info.getReadMethod() == null) {
                fail(browserVersion.getNickname() + " CSSStyleDeclaration: no getter for " + entry.getKey());
            }
            if (info.getWriteMethod() == null && !"length".equals(entry.getKey())) {
                fail(browserVersion.getNickname() + " CSSStyleDeclaration: no setter for " + entry.getKey());
            }
            if (isDefaultGetter(cssLines, info) && isDefaultSetter(cssLines, info)
                    && isDefaultGetterComputed(computedLines, info)) {
                fail(browserVersion.getNickname()
                        + " CSSStyleDeclaration: default implementation for " + entry.getKey());
            }
        }
    }

    private static boolean isDefaultGetter(final List<String> lines, final PropertyInfo info) {
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            final String nextLine = i + 1 < lines.size() ? lines.get(i + 1) : null;
            if (line.startsWith("    public ")
                    && line.contains(" " + info.getReadMethod().getName() + "(")
                    && nextLine != null && nextLine.contains("  return getStyleAttribute(")
                    && lines.get(i + 2).equals("    }")) {
                final String styleName = nextLine.substring(nextLine.indexOf('(' + 1), nextLine.indexOf(')'));
                try {
                    final String attributeName = Definition.valueOf(styleName).getAttributeName();
                    final String methodName = "get"
                            + Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
                    if (info.getReadMethod().getName().equals(methodName)) {
                        return true;
                    }
                }
                catch (final Exception e) {
                    // ignore
                }
            }
        }
        return false;
    }

    private static boolean isDefaultSetter(final List<String> lines, final PropertyInfo info) {
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            if (line.startsWith("    public void ")
                    && line.contains(" " + info.getWriteMethod().getName() + "(")
                    && lines.get(i + 1).contains("  setStyleAttribute(")
                    && lines.get(i + 2).equals("    }")) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDefaultGetterComputed(final List<String> lines, final PropertyInfo info) {
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            if (line.startsWith("    public ") && line.contains(" " + info.getReadMethod().getName() + "(")) {
                final String nextLine = lines.get(i + 1);
                if (nextLine.contains("  return defaultIfEmpty(super." + info.getReadMethod().getName() + "(),")
                        && nextLine.indexOf(',', nextLine.indexOf(',') + 1) == -1
                        && lines.get(i + 2).equals("    }")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "2", "", "2", "5", "5", "5", "5"},
            IE = {"", "2", "0", "0", "5", "5", "0", "0"},
            FF = {"undefined", "undefined", "0", "undefined", "5", "undefined", "0", "undefined"},
            FF78 = {"undefined", "undefined", "0", "undefined", "5", "undefined", "0", "undefined"})
    public void widows() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    debug(div);\n"
            + "    div.style.widows = 0;\n"
            + "    debug(div);\n"
            + "    div.style.widows = 5;\n"
            + "    debug(div);\n"
            + "    div.style.widows = 0;\n"
            + "    debug(div);\n"
            + "  }\n"
            + "  function debug(div) {\n"
            + "    alert(div.style.widows);\n"
            + "    alert(window.getComputedStyle(div, null).widows);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "2", "", "2", "5", "5", "5", "5"},
            IE = {"", "2", "0", "0", "5", "5", "0", "0"},
            FF = {"undefined", "undefined", "0", "undefined", "5", "undefined", "0", "undefined"},
            FF78 = {"undefined", "undefined", "0", "undefined", "5", "undefined", "0", "undefined"})
    public void orphans() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    debug(div);\n"
            + "    div.style.orphans = 0;\n"
            + "    debug(div);\n"
            + "    div.style.orphans = 5;\n"
            + "    debug(div);\n"
            + "    div.style.orphans = 0;\n"
            + "    debug(div);\n"
            + "  }\n"
            + "  function debug(div) {\n"
            + "    log(div.style.orphans);\n"
            + "    log(window.getComputedStyle(div, null).orphans);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "static", "", "static", "", "static", "absolute", "absolute", "", "static"})
    public void position() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    debug(div);\n"
            + "    div.style.position = 'fake';\n"
            + "    debug(div);\n"
            + "    div.style.position = ' ';\n"
            + "    debug(div);\n"
            + "    div.style.position = 'AbSoLuTe';\n"
            + "    debug(div);\n"
            + "    div.style.position = '';\n"
            + "    debug(div);\n"
            + "  }\n"
            + "  function debug(div) {\n"
            + "    log(div.style.position);\n"
            + "    log(window.getComputedStyle(div, null).position);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]", "green", "abc"},
            FF = {"[object CSS2Properties]", "[object CSS2Properties]", "green", "abc"},
            FF78 = {"[object CSS2Properties]", "[object CSS2Properties]", "green", "abc"},
            IE = {"[object MSStyleCSSProperties]", "[object MSStyleCSSProperties]", "", ""})
    @HtmlUnitNYI(FF = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]", "green", "abc"},
            FF78 = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]", "green", "abc"},
            IE = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]", "", ""})
    public void setStyle() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    alert(div.style);\n"
            + "    div.style = 'color: green; font-family: abc';\n"
            + "    alert(div.style);\n"
            + "    alert(div.style.color);\n"
            + "    alert(div.style.fontFamily);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1", "false", "true", "false", "false"},
            IE = {"1", "false", "true", "true", "true"})
    public void in() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var node = document.getElementById('div1');\n"
            + "    var style = node.style;\n"
            + "    log(style.length);\n"
            + "    log(-1 in style);\n"
            + "    log(0 in style);\n"
            + "    log(1 in style);\n"
            + "    log(42 in style);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' style='color: black'>foo</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
