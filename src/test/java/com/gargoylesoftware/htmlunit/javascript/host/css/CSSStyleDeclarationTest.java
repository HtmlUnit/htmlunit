/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
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
    @Alerts({ "black", "pink", "color: pink;", "color: pink;" })
    public void style_OneCssAttribute() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var node = document.getElementById('div1');\n"
            + "    var style = node.style;\n"
            + "    alert(style.color);\n"
            + "    style.color = 'pink';\n"
            + "    alert(style.color);\n"
            + "    alert(node.getAttribute('style'));\n"
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
    @Alerts(DEFAULT = { "black", "pink", "color: pink; background: blue none repeat scroll 0% 0%;" },
            CHROME = { "black", "pink", "color: pink; background: blue;" },
            IE = { "black", "pink", "background: blue; color: pink; foo: bar;" })
    @NotYetImplemented
    public void style_MultipleCssAttributes() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var style = document.getElementById('div1').style;\n"
            + "    alert(style.color);\n"
            + "    style.color = 'pink';\n"
            + "    alert(style.color);\n"
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
    @Alerts({ "null", "", "pink", "color: pink;" })
    public void style_OneUndefinedCssAttribute() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var style = document.getElementById('div1').style;\n"
            + "    alert(document.getElementById('nonexistingid'));\n"
            + "    alert(style.color);\n"
            + "    style.color = 'pink';\n"
            + "    alert(style.color);\n"
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
    @Alerts({"", "hidden", "undefined" })
    public void mozillaStyle() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv = document.getElementById('div1');\n"
            + "    alert(oDiv.style.visibility);\n"
            + "    oDiv.style.visibility = 'hidden';\n"
            + "    alert(oDiv.style.visibility);\n"
            + "    alert(oDiv.style.behavior);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1'>foo</div></body></html>";
        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void behavior() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv = document.getElementById('div1');\n"
            + "    alert(oDiv.style.behavior);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1'>foo</div></body></html>";

        loadPageWithAlerts2(html);
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
             + "   obj.style.backgroundColor = 'yellow';\n"
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
    @Alerts({ "string", "string", "string", "undefined" })
    public void accessProperties() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
                + "function doTest() {\n"
                + "    var oDiv = document.getElementById('div1');\n"
                + "    alert(typeof oDiv.style.visibility);\n"
                + "    alert(typeof oDiv.style.color);\n"
                + "    alert(typeof oDiv.style.backgroundImage);\n"
                + "    alert(typeof oDiv.style.foo);\n"
                + "}\n</script></head>\n"
                + "<body onload='doTest()'>\n"
                + "<div id='div1'>foo</div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 1592299.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void setStylePropertyNonString() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
                + "function doTest() {\n"
                + "    var oDiv1 = document.getElementById('div1');\n"
                + "    oDiv1.style.pixelLeft = 123;\n"
                + "    alert(oDiv1.style.pixelLeft);\n"
                + "}\n</script></head>\n"
                + "<body onload='doTest()'>\n"
                + "<div id='div1'>foo</div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "blue",
            FF = "blue none repeat scroll 0% 0%")
    @NotYetImplemented(FF)
    public void getPropertyValue() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var oDiv1 = document.getElementById('div1');\n"
            + "    alert(oDiv1.style.getPropertyValue('background'));\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='background: blue'>foo</div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "*blue* string", "" })
    public void removeProperty() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "  var oDiv1 = document.getElementById('div1');\n"
            + "  if (oDiv1.style.removeProperty) {\n"
            + "    var value = oDiv1.style.removeProperty('color');\n"
            + "    alert('*' + value + '* ' + typeof(value));\n"
            + "    alert(oDiv1.style.cssText);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: blue'>foo</div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "** string", "blue" })
    public void removePropertyUnknown() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "  var oDiv1 = document.getElementById('div1');\n"
            + "  if (oDiv1.style.removeProperty) {\n"
            + "    var value = oDiv1.style.removeProperty('font-size');\n"
            + "    alert('*' + value + '* ' + typeof(value));\n"
            + "    alert(oDiv1.style.getPropertyValue('color'));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: blue'>foo</div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "** string", "blue" })
    public void removePropertyUndefined() throws Exception {
        final String html = "<html>\n"
            + "<head><title>First</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var oDiv1 = document.getElementById('div1');\n"
            + "  if (!oDiv1.style.removeProperty) {\n"
            + "    alert('removeProperty not available');\n"
            + "    return;\n"
            + "  }\n"
            + "  var value = oDiv1.style.removeProperty(undefined);\n"
            + "  alert('*' + value + '* ' + typeof(value));\n"
            + "  alert(oDiv1.style.getPropertyValue('color'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: blue'>foo</div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "30px", "", "30px", "arial", "", "arial" })
    public void getPropertyValue_WithDash() throws Exception {
        final String html =
            "<html><body onload='test()'><script>\n"
            + "    function prop(elem, prop) {\n"
            + "      try{\n"
            + "        var p = span.style.getPropertyValue(prop);\n"
            + "        alert(p);\n"
            + "      } catch (e) { alert('exception'); }\n"
            + "    }\n"

            + "    function test() {\n"
            + "        var span = document.getElementById('span');\n"
            + "        span.style['fontSize'] = '30px';\n"
            + "        alert(span.style.fontSize);\n"
            + "        prop(span, 'fontSize');\n"
            + "        prop(span, 'font-size');\n"
            + "        span.style['fontFamily'] = 'arial';\n"
            + "        alert(span.style.fontFamily);\n"
            + "        prop(span, 'fontFamily');\n"
            + "        prop(span, 'font-family');\n"
            + "    }\n"
            + "</script>\n"
            + "<span id='span'>x</span>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "" })
    @NotYetImplemented
    public void styleFilter() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test(){\n"
            + "      var div1 = document.getElementById('div1');\n"
            + "      alert(div1.style.filter);\n"
            + "      var div2 = document.getElementById('div2');\n"
            + "      alert(div2.style.filter);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div1'>foo</div>\n"
            + "<div id='div2' style='filter:alpha(opacity=50)'>bar</div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that initializing <tt>opacity</tt> attribute to various values behaves correctly.
     * The whitespace in the various expected alerts is VERY important.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "0.5", "0.4", "0.33333", "-3", "3", "", "", "" })
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
            + "for (var i = 1; i < 10; i++) {\n"
            + "  d = document.getElementById('o' + i);\n"
            + "  alert(d.style.opacity);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that setting <tt>element.style.opacity</tt> to various values behaves correctly.
     * The whitespace in the various expected alerts is VERY important.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(" 0.5 0.4 0.33333 -3 3 8 7 7 7 7 ")
    public void setOpacity() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d'>d</div>\n"
            + "<script>\n"
            + "var d = document.getElementById('d');\n"
            + "var s = '';\n"
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
            + "d.style.opacity = '10px';\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = 'foo';\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = 'auto';\n"
            + "s += d.style.opacity + ' ';\n"
            + "d.style.opacity = '';\n"
            + "s += d.style.opacity;\n"
            + "alert(s);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "undefined", "exception" })
    public void setExpression() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     alert(typeof div1.style.setExpression);\n"
            + "     div1.style.setExpression('title','id');\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "undefined", "exception" })
    public void removeExpression() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     alert(typeof div1.style.removeExpression);\n"
            + "     div1.style.setExpression('title','id');\n"
            + "     div1.style.removeExpression('title');"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" })
    public void borderStyles_noStyle() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv = document.getElementById('div1');\n"
            + "    alert(oDiv.style.borderBottom);\n"
            + "    alert(oDiv.style.borderBottomColor);\n"
            + "    alert(oDiv.style.borderBottomStyle);\n"
            + "    alert(oDiv.style.borderBottomWidth);\n"
            + "    alert(oDiv.style.borderLeft);\n"
            + "    alert(oDiv.style.borderLeftColor);\n"
            + "    alert(oDiv.style.borderLeftStyle);\n"
            + "    alert(oDiv.style.borderLeftWidth);\n"
            + "    alert(oDiv.style.borderRight);\n"
            + "    alert(oDiv.style.borderRightColor);\n"
            + "    alert(oDiv.style.borderRightStyle);\n"
            + "    alert(oDiv.style.borderRightWidth);\n"
            + "    alert(oDiv.style.borderTop);\n"
            + "    alert(oDiv.style.borderTopColor);\n"
            + "    alert(oDiv.style.borderTopStyle);\n"
            + "    alert(oDiv.style.borderTopWidth);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1'>foo</div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3px", "4px", "2px", "1px" })
    public void borderXxxWidth() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv = document.getElementById('div1');\n"
            + "    alert(oDiv.style.borderBottomWidth);\n"
            + "    alert(oDiv.style.borderLeftWidth);\n"
            + "    alert(oDiv.style.borderRightWidth);\n"
            + "    alert(oDiv.style.borderTopWidth);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='border-width: 1px 2px 3px 4px'>foo</div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "thin", "medium", "thick", "thick" })
    public void borderXxxWidthConstants() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv = document.getElementById('div1');\n"
            + "    alert(oDiv.style.borderRightWidth);\n"
            + "    oDiv = document.getElementById('div2');\n"
            + "    alert(oDiv.style.borderLeftWidth);\n"
            + "    oDiv = document.getElementById('div3');\n"
            + "    alert(oDiv.style.borderBottomWidth);\n"
            + "    alert(oDiv.style.borderTopWidth);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='border: thin'>foo</div>"
            + "<div id='div2' style='border: medium'>foo</div>"
            + "<div id='div3' style='border: thick'>foo</div>"
            + "</body></html>";
        loadPageWithAlerts2(html);
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
    @Alerts({ "undefined", "foo" })
    public void setUnsupportdProperty() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='my' style=''>d</div>\n"

            + "<script>\n"
            + "  d = document.getElementById('my');\n"
            + "  alert(d.style.htmlunit);\n"
            + "  d.style.htmlunit='foo';\n"
            + "  alert(d.style.htmlunit);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "string", "", "1", "2", "2", "2", "2", "5", "5", "5", "5" })
    public void zIndex() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  alert(typeof style.zIndex);\n"
            + "  alert(style.zIndex);\n"
            + "  style.zIndex = 1;\n"
            + "  alert(style.zIndex);\n"
            + "  style.zIndex = 2.0;\n"
            + "  alert(style.zIndex);\n"
            + "  style.zIndex = 3.1;\n"
            + "  alert(style.zIndex);\n"
            + "  style.zIndex = 4.5;\n"
            + "  alert(style.zIndex);\n"
            + "  style.zIndex = 4.6;\n"
            + "  alert(style.zIndex);\n"
            + "  style.zIndex = '5';\n"
            + "  alert(style.zIndex);\n"
            + "  style.zIndex = '6.0';\n"
            + "  alert(style.zIndex);\n"
            + "  style.zIndex = '7.1';\n"
            + "  alert(style.zIndex);\n"
            + "  style.zIndex = '8.6';\n"
            + "  alert(style.zIndex);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "string", "", "string", "", "string", "4", "string", "", "string", "", "string", "" },
            IE = { "string", "", "string", "", "number", "4", "string", "", "string", "", "string", "" })
    public void zIndexDefault() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var style = document.getElementById('divUndefined').style;\n"
            + "  alert(typeof style.zIndex);\n"
            + "  alert(style.zIndex);\n"

            + "  style = document.getElementById('divBlank').style;\n"
            + "  alert(typeof style.zIndex);\n"
            + "  alert(style.zIndex);\n"

            + "  style = document.getElementById('divInteger').style;\n"
            + "  alert(typeof style.zIndex);\n"
            + "  alert(style.zIndex);\n"

            + "  style = document.getElementById('divFloat').style;\n"
            + "  alert(typeof style.zIndex);\n"
            + "  alert(style.zIndex);\n"

            + "  style = document.getElementById('divFloat2').style;\n"
            + "  alert(typeof style.zIndex);\n"
            + "  alert(style.zIndex);\n"

            + "  style = document.getElementById('invalidDiv').style;\n"
            + "  alert(typeof style.zIndex);\n"
            + "  alert(style.zIndex);\n"

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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "", "1", "1" })
    public void zIndexSetUndefined() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  var un_defined;\n"
            + "  alert(style.zIndex);\n"

            + "  try {\n"
            + "    style.zIndex = un_defined;\n"
            + "  } catch (e) { alert('error'); }\n"
            + "  alert(style.zIndex);\n"

            + "  style.zIndex = 1;\n"
            + "  alert(style.zIndex);\n"

            + "  try {\n"
            + "    style.zIndex = un_defined;\n"
            + "  } catch (e) { alert('error'); }\n"
            + "  alert(style.zIndex);\n"

            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "", "1", "" })
    public void zIndexSetNull() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  alert(style.zIndex);\n"

            + "  try {\n"
            + "    style.zIndex = null;\n"
            + "  } catch (e) { alert('error'); }\n"
            + "  alert(style.zIndex);\n"

            + "  style.zIndex = 1;\n"
            + "  alert(style.zIndex);\n"

            + "  try {\n"
            + "    style.zIndex = null;\n"
            + "  } catch (e) { alert('error'); }\n"
            + "  alert(style.zIndex);\n"

            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "7", "7", "", "4", "1" })
    public void zIndexSetString() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  var unknown;\n"
            + "  alert(style.zIndex);\n"

            + "  style.zIndex = '7';\n"
            + "  alert(style.zIndex);\n"

            + "  style.zIndex = '7.6';\n"
            + "  alert(style.zIndex);\n"

            + "  style.zIndex = '';\n"
            + "  alert(style.zIndex);\n"

            + "  style.zIndex = '4';\n"
            + "  try {\n"
            + "    style.zIndex = '   ';\n"
            + "  } catch (e) { alert('error'); }\n"
            + "  alert(style.zIndex);\n"

            + "  style.zIndex = '1';\n"
            + "  try {\n"
            + "    style.zIndex = 'NAN';\n"
            + "  } catch (e) { alert('error'); }\n"
            + "  alert(style.zIndex);\n"

            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "1", "1" })
    public void zIndexSetInvalid() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  alert(style.zIndex);\n"
            + "  try {\n"
            + "    style.zIndex = 'hallo';\n"
            + "  } catch (e) { alert('error'); }\n"
            + "  alert(style.zIndex);\n"

            + "  style.zIndex = 1;\n"
            + "  alert(style.zIndex);\n"
            + "  try {\n"
            + "    style.zIndex = 'hallo';\n"
            + "  } catch (e) { alert('error'); }\n"
            + "  alert(style.zIndex);\n"
            + "}\n"

            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "", "15px", "italic", "", "italic" })
    public void cssText() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var style = document.getElementById('myDiv').style;\n"
            + "     alert(style.fontSize);\n"
            + "     alert(style.fontStyle);\n"
            + "     style.cssText = 'font-size: 15px; font-style: italic';\n"
            + "     alert(style.fontSize);\n"
            + "     alert(style.fontStyle);\n"
            + "     style.cssText = 'font-style: italic';\n"
            + "     alert(style.fontSize);\n"
            + "     alert(style.fontStyle);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1px", "solid", "red" })
    public void border() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var style = document.getElementById('myDiv').style;\n"
            + "    if (style.getPropertyValue) {\n"
            + "      alert(style.getPropertyValue('border-top-width'));\n"
            + "      alert(style.getPropertyValue('border-top-style'));\n"
            + "      alert(style.getPropertyValue('border-top-color'));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv' style='border: red 1px solid'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "false" })
    public void display() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    myDiv.style.display='none';\n"
            + "    alert(myDiv.style.display=='none');\n"
            + "    myDiv.style.display='';\n"
            + "    alert(myDiv.style.display=='none');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "none", "rgb(0, 128, 0)", "none", "rgb(0, 128, 0)" },
            CHROME = { "", "", "none", "rgb(0, 128, 0)" },
            IE = { "inline", "rgb(0, 0, 0)", "none", "rgb(0, 128, 0)" })
    @NotYetImplemented(IE)
    public void displayDefaultOverwritesNone() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <style>\n"
            + "    tt { display: none; color: green; }\n"
            + "  </style>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var e = document.createElement('tt');\n"
            + "      var style = window.getComputedStyle(e, null);\n"
            + "      alert(style['display']);\n"
            + "      alert(style['color']);\n"
            + "      document.body.appendChild(e);\n"
            + "      alert(style['display']);\n"
            + "      alert(style['color']);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "block", "rgb(0, 0, 0)", "inline", "rgb(0, 0, 0)" },
            CHROME = { "", "", "inline", "rgb(0, 0, 0)" },
            IE = { "inline", "rgb(0, 0, 0)", "inline", "rgb(0, 0, 0)" })
    public void displayDefault() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var e = document.createElement('tt');\n"
            + "      var style = window.getComputedStyle(e, null);\n"
            + "      alert(style['display']);\n"
            + "      alert(style['color']);\n"
            + "      document.body.appendChild(e);\n"
            + "      alert(style['display']);\n"
            + "      alert(style['color']);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1px", "2px" })
    public void resettingValue() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    myDiv.style.marginTop='1px';\n"
            + "    alert(myDiv.style.marginTop);\n"
            + "    myDiv.style.marginTop='2px';\n"
            + "    alert(myDiv.style.marginTop);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2px", "30px" })
    public void resettingValue2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    myDiv.style.marginTop='2px';\n"
            + "    alert(myDiv.style.marginTop);\n"
            + "    myDiv.style.left='-1px';\n"
            + "    myDiv.style.marginTop='30px';\n"
            + "    alert(myDiv.style.marginTop);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that setting margins all at once and setting margins individually all work, both in static
     * styles and in calculated styles.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({
            "L:3px,R:3px,T:3px,B:3px", "L:5px,R:5px,T:5px,B:5px", "L:7px,R:2px,T:2px,B:2px",
            "L:3px,R:3px,T:3px,B:3px", "L:5px,R:5px,T:5px,B:5px", "L:7px,R:2px,T:2px,B:2px" })
    public void marginAllvsMarginSingle() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <style>\n"
            + "      #m1 { margin: 3px; }\n"
            + "      #m2 { margin-left: 3px; margin: 5px; }\n"
            + "      #m3 { margin: 2px; margin-left: 7px; }\n"
            + "    </style>\n"
            + "    <script>\n"
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
            + "        alert('L:' + s.marginLeft + ',R:' + s.marginRight +\n"
            + "          ',T:' + s.marginTop + ',B:' + s.marginBottom);\n"
            + "      }\n"
            + "      function alertNonComputedMargins(id) {\n"
            + "        var e = document.getElementById(id);\n"
            + "        var s = e.style;\n"
            + "        alert('L:' + s.marginLeft + ',R:' + s.marginRight +\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that setting paddings all at once and setting paddings individually all work, both in static
     * styles and in calculated styles.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({
        "L:3px,R:3px,T:3px,B:3px", "L:5px,R:5px,T:5px,B:5px", "L:7px,R:2px,T:2px,B:2px",
        "L:3px,R:3px,T:3px,B:3px", "L:5px,R:5px,T:5px,B:5px", "L:7px,R:2px,T:2px,B:2px" })
    public void paddingAllvsPaddingSingle() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <style>\n"
            + "      #m1 { padding: 3px; }\n"
            + "      #m2 { padding-left: 3px; padding: 5px; }\n"
            + "      #m3 { padding: 2px; padding-left: 7px; }\n"
            + "    </style>\n"
            + "    <script>\n"
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
            + "        alert('L:' + s.paddingLeft + ',R:' + s.paddingRight +\n"
            + "          ',T:' + s.paddingTop + ',B:' + s.paddingBottom);\n"
            + "      }\n"
            + "      function alertNonComputedPaddings(id) {\n"
            + "        var e = document.getElementById(id);\n"
            + "        var s = e.style;\n"
            + "        alert('L:' + s.paddingLeft + ',R:' + s.paddingRight +\n"
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
        loadPageWithAlerts2(html);
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
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "    var style = document.getElementById('d').style;\n"
            + "    alert(style." + attribute + ");\n"
            + "}\n</script></head>\n"
            + "<body onload='test()'><div id='d' style='" + style + "'>foo</div></body></html>";

        setExpectedAlerts(expectedValue);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { },
            IE = { "", "green", "green", "", "green", "green", "", "green", "green" })
    public void getAttribute() throws Exception {
        getAttribute("\"font\"", new String[0]);
        final String[] expected = getExpectedAlerts();
        if (expected.length != 0) {
            getAttribute("'font'", expected[0]);
            getAttribute("'color'", expected[1]);
            getAttribute("'ColoR'", expected[2]);
            getAttribute("'font', 0", expected[3]);
            getAttribute("'color', 0", expected[4]);
            getAttribute("'coLOr', 0", expected[5]);
            getAttribute("'font', 1", expected[6]);
            getAttribute("'color', 1", expected[7]);
            getAttribute("'ColOR', 1", expected[8]);
        }
    }

    private void getAttribute(final String params, final String... expected) throws Exception {
        final String html =
              "<html><head><script>\n"
            + "function test() {\n"
            + "  if (document.all['a'].style.getAttribute) {\n"
            + "    alert(document.all['a'].style.getAttribute(" + params + "));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<a id='a' href='#' style='color:green'>go</a></body></html>";

        setExpectedAlerts(expected);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "not supported", "not supported", "not supported", "not supported", "not supported",
                "not supported", "not supported", "not supported", "not supported" },
            IE = { "'font', 'blah', green, green",
                "'color', 'red', green, red",
                "'ColoR', 'red', green, red",
                "'font', 'blah', 0, green, green",
                "'color', 'red', 0, green, red",
                "'ColoR', 'red', 0, green, red",
                "'font', 'blah', 1, green, green",
                "'color', 'red', 1, green, red",
                "'ColoR', 'red', 1, green, red" })
    public void setAttribute() throws Exception {
        final String[] expected = getExpectedAlerts();
        setAttribute("'font', 'blah'", expected[0]);
        setAttribute("'color', 'red'", expected[1]);
        setAttribute("'ColoR', 'red'", expected[2]);
        setAttribute("'font', 'blah', 0", expected[3]);
        setAttribute("'color', 'red', 0", expected[4]);
        setAttribute("'ColoR', 'red', 0", expected[5]);
        setAttribute("'font', 'blah', 1", expected[6]);
        setAttribute("'color', 'red', 1", expected[7]);
        setAttribute("'ColoR', 'red', 1", expected[8]);
    }

    private void setAttribute(final String params, final String... expected) throws Exception {
        final String html =
              "<html><body onload='test()'>\n"
            + "<a id='a' href='#' style='color:green'>go</a>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (document.all['a'].style.getAttribute) {\n"
            + "      alert(\"" + params + "\");\n"
            + "      alert(document.all['a'].style.getAttribute('color'));\n"
            + "      document.all['a'].style.setAttribute(" + params + ");\n"
            + "      alert(document.all['a'].style.getAttribute('color'));\n"
            + "    }\n"
            + "    else {\n"
            + "      alert('not supported');\n"
            + "    }\n"
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
    @Alerts(DEFAULT = { "not supported", "not supported", "not supported", "not supported", "not supported",
                "not supported", "not supported", "not supported", "not supported" },
            IE = { "'font', green, false, green",
                "'color', green, true, ",
                "'ColoR', green, true, ",
                "'font', 0, green, false, green",
                "'color', 0, green, true, ",
                "'ColoR', 0, green, true, ",
                "'font', 1, green, false, green",
                "'color', 1, green, true, ",
                "'ColoR', 1, green, true, " })
    public void removeAttribute() throws Exception {
        final String[] expected = getExpectedAlerts();
        removeAttribute("'font'", expected[0]);
        removeAttribute("'color'", expected[1]);
        removeAttribute("'ColoR'", expected[2]);
        removeAttribute("'font', 0", expected[3]);
        removeAttribute("'color', 0", expected[4]);
        removeAttribute("'ColoR', 0", expected[5]);
        removeAttribute("'font', 1", expected[6]);
        removeAttribute("'color', 1", expected[7]);
        removeAttribute("'ColoR', 1", expected[8]);
    }

    private void removeAttribute(final String params, final String... expected) throws Exception {
        final String html =
              "<html><body onload='test()'>\n"
            + "<a id='a' href='#' style='color:green'>go</a>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (document.all['a'].style.getAttribute) {\n"
            + "      alert(\"" + params + "\");\n"
            + "      alert(document.all['a'].style.getAttribute('color'));\n"
            + "      alert(document.all['a'].style.removeAttribute(" + params + "));\n"
            + "      alert(document.all['a'].style.getAttribute('color'));\n"
            + "    }\n"
            + "    else {\n"
            + "      alert('not supported');\n"
            + "    }\n"
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
    @Alerts({ "red ", "black ", "blue important", "gray " })
    public void setProperty() throws Exception {
        final String[] expected = getExpectedAlerts();
        setProperty("'background-color', 'red', ''", expected[0]);
        setProperty("'background-ColoR', 'black', ''", expected[1]);
        setProperty("'background-color', 'blue', 'important'", expected[2]);
        setProperty("'background-color', 'gray', null", expected[3]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "green ", "black important", "green " },
            FF = { "green ", "green ", "green " })
    public void setPropertyImportant() throws Exception {
        final String[] expected = getExpectedAlerts();
        setProperty("'background-color', 'white', 'crucial'", expected[0]);
        setProperty("'background-color', 'black', 'imPORTant'", expected[1]);
        setProperty("'background-color', 'blue', 'important '", expected[2]);
    }

    private void setProperty(final String params, final String... expected) throws Exception {
        final String html =
              "<html><body onload='test()'>\n"
            + "<a id='a' href='#' style='background-color:green'>go</a>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var node = document.getElementById('a');\n"
            + "    if (node.style.setProperty) {\n"
            + "      try {\n"
            + "        node.style.setProperty(" + params + ");\n"
            + "        alert(node.style.backgroundColor + ' ' + node.style.getPropertyPriority('background-color'));\n"
            + "      } catch(e) { alert(e); }\n"
            + "    } else {\n"
            + "      alert('not supported');\n"
            + "    }\n"
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
    @Alerts({ "", "important", "", "important" })
    public void getPropertyPriority() throws Exception {
        final String html =
                "<html><body onload='test()'>\n"
              + "<a id='a1' href='#' style='color:green'>go</a>\n"
              + "<a id='a2' href='#' style='color:blue !important'>go</a>\n"

              + "<a id='a3' href='#' style='background-color:green'>go</a>\n"
              + "<a id='a4' href='#' style='background-color:blue !important'>go</a>\n"

              + "<script>\n"
              + "  function test() {\n"
              + "    var node = document.getElementById('a1');\n"
              + "    if (node.style.getPropertyPriority) {\n"
              + "      alert(node.style.getPropertyPriority('color'));\n"

              + "      node = document.getElementById('a2');\n"
              + "      alert(node.style.getPropertyPriority('color'));\n"

              + "      node = document.getElementById('a3');\n"
              + "      alert(node.style.getPropertyPriority('background-color'));\n"

              + "      node = document.getElementById('a4');\n"
              + "      alert(node.style.getPropertyPriority('background-color'));\n"
              + "    } else {\n"
              + "      alert('not supported');\n"
              + "    }\n"
              + "  }\n"
              + "</script>\n"
              + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BLACK", "pink", "color: pink;", "color: pink;" },
            CHROME = { "black", "pink", "color: pink;", "color: pink;" },
            IE = { "black", "pink", "color: pink;", "color: pink;" })
    public void caseInsensitive() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var node = document.getElementById('div1');\n"
            + "    var style = node.style;\n"
            + "    alert(style.color);\n"
            + "    style.color = 'pink';\n"
            + "    alert(style.color);\n"
            + "    alert(node.getAttribute('style'));\n"
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
    @Alerts(DEFAULT = { "5px", "undefined", "1em", "undefined" },
            IE = { "5px", "5", "1em", "16", "30px", "30" })
    public void pixelLeft() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='a' style='left: 5px; border: 1px solid black;'>a</div>\n"
            + "<div id='b' style='left: 1em; border: 1px solid black;'>b</div>\n"
            + "<script>\n"
            + "  var a = document.getElementById('a');\n"
            + "  var b = document.getElementById('b');\n"
            + "  alert(a.style.left);\n"
            + "  alert(a.style.pixelLeft);\n"
            + "  alert(b.style.left);\n"
            + "  alert(b.style.pixelLeft);\n"
            + "  if(a.style.pixelLeft) {\n"
            + "    a.style.pixelLeft = 30;\n"
            + "    alert(a.style.left);\n"
            + "    alert(a.style.pixelLeft);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "5px", "undefined", "1em", "undefined" },
            IE = { "5px", "5", "1em", "16", "30px", "30" })
    public void pixelRight() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='a' style='right: 5px; border: 1px solid black;'>a</div>\n"
            + "<div id='b' style='right: 1em; border: 1px solid black;'>b</div>\n"
            + "<script>\n"
            + "  var a = document.getElementById('a');\n"
            + "  var b = document.getElementById('b');\n"
            + "  alert(a.style.right);\n"
            + "  alert(a.style.pixelRight);\n"
            + "  alert(b.style.right);\n"
            + "  alert(b.style.pixelRight);\n"
            + "  if(a.style.pixelRight) {\n"
            + "    a.style.pixelRight = 30;\n"
            + "    alert(a.style.right);\n"
            + "    alert(a.style.pixelRight);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "5px", "undefined", "1em", "undefined" },
            IE = { "5px", "5", "1em", "16", "30px", "30" })
    public void pixelTop() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='a' style='top: 5px; border: 1px solid black;'>a</div>\n"
            + "<div id='b' style='top: 1em; border: 1px solid black;'>b</div>\n"
            + "<script>\n"
            + "  var a = document.getElementById('a');\n"
            + "  var b = document.getElementById('b');\n"
            + "  alert(a.style.top);\n"
            + "  alert(a.style.pixelTop);\n"
            + "  alert(b.style.top);\n"
            + "  alert(b.style.pixelTop);\n"
            + "  if(a.style.pixelTop) {\n"
            + "    a.style.pixelTop = 30;\n"
            + "    alert(a.style.top);\n"
            + "    alert(a.style.pixelTop);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "5px", "undefined", "1em", "undefined" },
            IE = { "5px", "5", "1em", "16", "30px", "30" })
    public void pixelBottom() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='a' style='bottom: 5px; border: 1px solid black;'>a</div>\n"
            + "<div id='b' style='bottom: 1em; border: 1px solid black;'>b</div>\n"
            + "<script>\n"
            + "  var a = document.getElementById('a');\n"
            + "  var b = document.getElementById('b');\n"
            + "  alert(a.style.bottom);\n"
            + "  alert(a.style.pixelBottom);\n"
            + "  alert(b.style.bottom);\n"
            + "  alert(b.style.pixelBottom);\n"
            + "  if(a.style.pixelBottom) {\n"
            + "    a.style.pixelBottom = 30;\n"
            + "    alert(a.style.bottom);\n"
            + "    alert(a.style.pixelBottom);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test for a __lookupSetter__ bug affecting Hotmail when emulating Firefox.
     * @see <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=491433">Rhino bug 491433</a>
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "none" },
            CHROME = { "undefined", "before", "none", "exception" },
            IE = { "function", "before", "none", "after", "none" })
    @NotYetImplemented
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
    @Alerts({ "", "", "", "" })
    public void setToNull() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    alert(div1.style.border);\n"
            + "    try {\n"
            + "      div1.style.border = null;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "    alert(div1.style.border);\n"
            + "    alert(div1.style.display);\n"
            + "    try {\n"
            + "      div1.style.display = null;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "    alert(div1.style.display);\n"
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
    @Alerts(DEFAULT = { "1", "width", "", "undefined" },
            FF = { "1", "width", "undefined", "undefined" })
    public void length() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = document.createElement('div');\n"
            + "    a.style.cssText='width: 100%';\n"
            + "    alert(a.style.length);\n"
            + "    alert(a.style[0]);\n"
            + "    alert(a.style[1]);\n"
            + "    alert(a.style[-1]);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "true", "border-box" })
    public void boxSizing() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var style = document.getElementById('test').style;\n"
            + "    alert(style.boxSizing === '');\n"

            + "    style = document.createElement('div').style;\n"
            + "    alert(style.boxSizing === '');\n"
            + "    style.boxSizing = 'border-box';\n"
            + "    alert(style.boxSizing);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='test'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Another one from the jQuery browser support tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "auto", "auto" },
            CHROME = { "auto", "" })
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
     * Tests that all values in {@link StyleAttributes} have a method in {@link CSSStyleDeclaration}
     * and is supported by the browser.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Ignore
    public void styleAttributes() throws Exception {
        final BrowserVersion browserVersion = getBrowserVersion();
        final ClassConfiguration config
            = AbstractJavaScriptConfiguration.getClassConfiguration(CSSStyleDeclaration.class, browserVersion);
        for (final Definition definition : StyleAttributes.getDefinitions(browserVersion)) {
            if (!definition.name().endsWith("_")) {
                final String propertyName = definition.getPropertyName();
                final PropertyInfo info = config.getPropertyMap().get(propertyName);
                if (info == null) {
                    fail(browserVersion.getNickname() + ": CSSStyleDeclaration: not defined " + propertyName);
                }
                else if (info.getReadMethod() == null) {
                    fail(browserVersion.getNickname() + ": CSSStyleDeclaration: property "
                            + propertyName + " does not have getter.");
                }
                else if (info.getWriteMethod() == null) {
                    fail(browserVersion.getNickname() + ": CSSStyleDeclaration: property "
                            + propertyName + " does not have setter.");
                }
            }
        }
        for (final String propertyName : config.getPropertyMap().keySet()) {
            if (StyleAttributes.getDefinition(propertyName, browserVersion) == null) {
                fail(browserVersion.getNickname() + " CSSStyleDeclaration: incorrectly defines " + propertyName);
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
        final List<String> cssLines = FileUtils.readLines(new File(cssFolder, "CSSStyleDeclaration.java"));
        final List<String> computedLines = FileUtils.readLines(new File(cssFolder, "ComputedCSSStyleDeclaration.java"));
        for (final String propertyName : propertyMap.keySet()) {
            final PropertyInfo info = propertyMap.get(propertyName);
            if (info.getReadMethod() == null) {
                fail(browserVersion.getNickname() + " CSSStyleDeclaration: no getter for " + propertyName);
            }
            if (info.getWriteMethod() == null && !"length".equals(propertyName)) {
                fail(browserVersion.getNickname() + " CSSStyleDeclaration: no setter for " + propertyName);
            }
            if (isDefaultGetter(cssLines, info) && isDefaultSetter(cssLines, info)
                    && isDefaultGetterComputed(computedLines, info)) {
                fail(browserVersion.getNickname() + " CSSStyleDeclaration: default implementation for " + propertyName);
            }
        }
    }

    private static boolean isDefaultGetter(final List<String> lines, final PropertyInfo info) {
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            final String nextLine = i + 1 < lines.size() ? lines.get(i + 1) : null;
            if (line.startsWith("    public ")
                    && line.contains(" " + info.getReadMethod().getName() + "(")
                    && nextLine.contains("  return getStyleAttribute(")
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

}
