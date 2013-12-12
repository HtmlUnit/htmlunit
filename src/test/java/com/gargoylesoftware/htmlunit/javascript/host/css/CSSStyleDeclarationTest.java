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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF17;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link CSSStyleDeclaration}.
 *
 * @version $Revision$
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
    @Alerts({ "black", "pink", "color: pink;" })
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

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("color: pink;", driver.findElement(By.id("div1")).getAttribute("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "black", "pink" })
    @NotYetImplemented({ FF17, FF24 })
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

        final WebDriver driver = loadPageWithAlerts2(html);
        final String expected;
        if ("FF17".equals(getBrowserVersion().getNickname())
                || "FF24".equals(getBrowserVersion().getNickname())) {
            expected = "color: pink; background: none repeat scroll 0% 0% blue;";
        }
        else if ("IE11".equals(getBrowserVersion().getNickname())) {
            expected = "background: blue; color: pink; foo: bar;";
        }
        else {
            expected = "color: pink; background: blue; foo: bar;";
        }
        assertEquals(expected, driver.findElement(By.id("div1")).getAttribute("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "null", "", "pink" })
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

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("color: pink;", driver.findElement(By.id("div1")).getAttribute("style"));
    }

    /**
     * Even if JavaScript isn't really executed according to the browser version used,
     * it may have some side effects if configuration is incorrect.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "hidden", "undefined" },
            IE8 = {"", "hidden", "" })
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
    @Alerts(DEFAULT = "undefined",
            IE8 = "")
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
    @Alerts(FF = "none repeat scroll 0% 0% blue",
            IE = "exception",
            IE11 = "blue")
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
    @Browsers({ FF, IE11 })
    @Alerts({ "*blue* string", "" })
    public void removeProperty() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv1 = document.getElementById('div1');\n"
            + "    var value = oDiv1.style.removeProperty('color');\n"
            + "    alert('*' + value + '* ' + typeof(value));\n"
            + "    alert(oDiv1.style.cssText);\n"
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
    @Browsers({ FF, IE11 })
    @Alerts({ "** string", "blue" })
    public void removePropertyUnknown() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv1 = document.getElementById('div1');\n"
            + "    var value = oDiv1.style.removeProperty('font-size');\n"
            + "    alert('*' + value + '* ' + typeof(value));\n"
            + "    alert(oDiv1.style.getPropertyValue('color'));\n"
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
    @Browsers({ FF, IE11 })
    @Alerts({ "** string", "blue" })
    public void removePropertyUndefined() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv1 = document.getElementById('div1');\n"
            + "    var value = oDiv1.style.removeProperty(undefined);\n"
            + "    alert('*' + value + '* ' + typeof(value));\n"
            + "    alert(oDiv1.style.getPropertyValue('color'));\n"
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
    @Browsers({ FF, IE11 })
    @Alerts({ "30px", "", "30px", "arial", "", "arial" })
    public void getPropertyValue_WithDash() throws Exception {
        final String html =
              "<html><body onload='test()'><script>\n"
            + "    function test() {\n"
            + "        var span = document.getElementById('span');\n"
            + "        span.style['fontSize'] = '30px';\n"
            + "        alert(span.style.fontSize);\n"
            + "        alert(span.style.getPropertyValue('fontSize'));\n"
            + "        alert(span.style.getPropertyValue('font-size'));\n"
            + "        span.style['fontFamily'] = 'arial';\n"
            + "        alert(span.style.fontFamily);\n"
            + "        alert(span.style.getPropertyValue('fontFamily'));\n"
            + "        alert(span.style.getPropertyValue('font-family'));\n"
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
    @Alerts(DEFAULT = { "", "" },
            IE8 = { "", "alpha(opacity=50)" })
    @NotYetImplemented({ FF17, FF24 })
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
    @Alerts(DEFAULT = { "", "0.5", "0.4", "0.33333", "-3", "3", "", "", "" },
            IE8 = { "", "0.5", ".4", "0.33333", "-3", "3", "10px", "foo", "auto" })
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
            + "for (i=1; i<10; i++) {\n"
            + "  d = document.getElementById('o'+i);\n"
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
    @Alerts(DEFAULT = " 0.5 0.4 0.33333 -3 3 8 7 7 7 7 ",
            IE8 = "undefined 0.5 0.4 0.33333 -3 3 8  7  10px foo auto ")
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
    @Alerts(DEFAULT = { "undefined", "exception" },
            IE8 = "function")
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
    @Alerts(DEFAULT = { "undefined", "exception" },
            IE8 = "function")
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
    @Alerts(DEFAULT = { "string", "", "1", "2", "2", "2", "2", "5", "5", "5", "5" },
            IE8 = { "number", "0", "1", "2", "3", "4", "4", "5", "6", "7", "8" })
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
    @Alerts(FF = { "string", "", "string", "", "string", "4", "string", "", "string", "" , "string", "" },
            IE = { "number", "0", "number", "0", "number", "4", "number", "4", "number", "4", "number", "0" },
            IE11 = { "string", "", "string", "", "number", "4", "string", "", "string", "" , "string", "" })
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
    @Alerts(DEFAULT = { "", "", "1", "1" },
            IE8 = { "0", "error", "0", "1", "error", "1" })
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
    @Alerts(DEFAULT = { "", "", "1", "" },
            IE8 = { "0", "error", "0", "1", "error", "1" })
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
    @Alerts(DEFAULT = {"", "7", "7", "", "4", "1" },
            IE8 = {"0", "7", "7", "0", "error", "4", "error", "1" })
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
    @Alerts(DEFAULT = {"", "", "1", "1" },
            IE8 = {"0", "error", "0", "1", "error", "1" })
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
    @Browsers({ FF, IE11 })
    @Alerts({ "1px", "solid", "red" })
    public void border() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var style = document.getElementById('myDiv').style;\n"
            + "     alert(style.getPropertyValue('border-top-width'));\n"
            + "     alert(style.getPropertyValue('border-top-style'));\n"
            + "     alert(style.getPropertyValue('border-top-color'));\n"
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
            + "        var s = e.currentStyle ? e.currentStyle : getComputedStyle(e, null);\n"
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
    @Browsers(IE)
    @Alerts(IE = { "", "green", "green", "", "green", "green", "", "green", "" },
            IE11 = { "", "green", "green", "", "green", "green", "", "green", "green" })
    public void getAttribute() throws Exception {
        final String[] expected = getExpectedAlerts();
        getAttribute("\"font\"", expected[0]);
        getAttribute("\"color\"", expected[1]);
        getAttribute("\"ColoR\"", expected[2]);
        getAttribute("\"font\", 0", expected[3]);
        getAttribute("\"color\", 0", expected[4]);
        getAttribute("\"coLOr\", 0", expected[5]);
        getAttribute("\"font\", 1", expected[6]);
        getAttribute("\"color\", 1", expected[7]);
        getAttribute("\"ColOR\", 1", expected[8]);
    }

    private void getAttribute(final String params, final String... expected) throws Exception {
        final String html =
              "<html><head><script>\n"
            + "function test() {\n"
            + "  alert(document.all[\"a\"].style.getAttribute(" + params + "));\n"
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
    @Browsers(IE)
    @Alerts(IE = { "green, green", "green, red", "green, green", "green, green",
                "green, red", "green, red", "green, green", "green, red", "green, green" },
            IE11 = { "green, green", "green, red", "green, red", "green, green",
                "green, red", "green, red", "green, green", "green, red", "green, red" })
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
            + "    alert(document.all['a'].style.getAttribute('color'));\n"
            + "    document.all['a'].style.setAttribute(" + params + ");\n"
            + "    alert(document.all['a'].style.getAttribute('color'));\n"
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
    @Browsers(IE)
    @Alerts(IE = { "green, false, green", "green, true, ", "green, false, green", "green, false, green",
            "green, true, ", "green, true, ", "green, false, green", "green, true, ", "green, false, green" },
            IE11 = { "green, false, green", "green, true, ", "green, true, ", "green, false, green",
                "green, true, ", "green, true, ", "green, false, green", "green, true, ", "green, true, " })
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
            + "    alert(document.all['a'].style.getAttribute('color'));\n"
            + "    alert(document.all['a'].style.removeAttribute(" + params + "));\n"
            + "    alert(document.all['a'].style.getAttribute('color'));\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        setExpectedAlerts(expected);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BLACK", "pink", "color: pink;" },
            IE11 = { "black", "pink", "color: pink;" })
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
            + "}\n</script></head>\n"
            + "<body onload='doTest()'><div id='div1' style='COLOR: BLACK'>foo</div></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("color: pink;", driver.findElement(By.id("div1")).getAttribute("style"));
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
            IE = "exception",
            IE11 = { "function", "before", "none", "after", "none" })
    @NotYetImplemented(FF)
    public void interceptSetter() throws Exception {
        final String html = "<html><body><div id='d'>foo</div><script>\n"
            + "try {\n"
            + "var css = window.CSSStyleDeclaration;\n"
            + "var oldDisplay = css.prototype.__lookupSetter__('display');\n"
            + "alert(typeof oldDisplay);\n"
            + "\n"
            + "var newDisplay = function(x){ alert('before'); alert(x); oldDisplay.call(this, x); alert('after'); };\n"
            + "css.prototype.__defineSetter__('display', newDisplay);\n"
            + "\n"
            + "var div = document.getElementById('d');\n"
            + "div.style.display = 'none';\n"
            + "alert(div.style.display);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "", "", "" },
            IE8 = {"", "error", "", "", "error", "" })
    @NotYetImplemented(IE)
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
}
