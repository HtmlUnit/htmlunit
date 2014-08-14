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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link CSSStyleDeclaration}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class CSSStyleDeclaration2Test extends WebDriverTestCase {

    /*
     Below is a page to see the different elements behavior
<html>
  <head>
    <script>
      function test() {
        //all properties of CSSStyleDeclaration in JavaScriptConfiguration.xml
        var properties = ['azimuth','background','backgroundAttachment','backgroundColor','backgroundImage',
        'backgroundPosition','backgroundPositionX','backgroundPositionY','backgroundRepeat','behavior','border',
        'borderBottom','borderBottomColor','borderBottomStyle','borderBottomWidth','borderCollapse','borderColor',
        'borderLeft','borderLeftColor','borderLeftStyle','borderLeftWidth','borderRight','borderRightColor',
        'borderRightStyle','borderRightWidth','borderSpacing','borderStyle','borderTop','borderTopColor',
        'borderTopStyle','borderTopWidth','borderWidth','bottom','captionSide','clear','clip','color','content',
        'counterIncrement','counterReset','cssFloat','cssText','cue','cueAfter','cueBefore','cursor','direction',
        'display','elevation','emptyCells','filter','font','fontFamily','fontSize','fontSizeAdjust','fontStretch',
        'fontStyle','fontVariant','fontWeight','height','imeMode','layoutFlow','layoutGrid','layoutGridChar',
        'layoutGridLine','layoutGridMode','layoutGridType','left','letterSpacing','lineBreak','lineHeight',
        'listStyle','listStyleImage','listStylePosition','listStyleType','margin','marginBottom','marginLeft',
        'marginRight','marginTop','markerOffset','marks','maxHeight','maxWidth','minHeight','minWidth',
        'MozAppearance','MozBackgroundClip','MozBackgroundInlinePolicy','MozBackgroundOrigin','MozBinding',
        'MozBorderBottomColors','MozBorderLeftColors','MozBorderRadius','MozBorderRadiusBottomleft',
        'MozBorderRadiusBottomright','MozBorderRadiusTopleft','MozBorderRadiusTopright','MozBorderRightColors',
        'MozBorderTopColors','MozBoxAlign','MozBoxDirection','MozBoxFlex','MozBoxOrdinalGroup','MozBoxOrient',
        'MozBoxPack','MozBoxSizing','MozColumnCount','MozColumnGap','MozColumnWidth','MozFloatEdge',
        'MozForceBrokenImageIcon','MozImageRegion','MozMarginEnd','MozMarginStart','MozOpacity','MozOutline',
        'MozOutlineColor','MozOutlineOffset','MozOutlineRadius','MozOutlineRadiusBottomleft',
        'MozOutlineRadiusBottomright','MozOutlineRadiusTopleft','MozOutlineRadiusTopright','MozOutlineStyle',
        'MozOutlineWidth','MozPaddingEnd','MozPaddingStart','MozUserFocus','MozUserInput','MozUserModify',
        'MozUserSelect','msInterpolationMode','opacity','orphans','outline','outlineColor','outlineOffset',
        'outlineStyle','outlineWidth','overflow','overflowX','overflowY','padding','paddingBottom','paddingLeft',
        'paddingRight','paddingTop','page','pageBreakAfter','pageBreakBefore','pageBreakInside','pause',
        'pauseAfter','pauseBefore','pitch','pitchRange','pixelBottom','pixelLeft','pixelRight','pixelTop',
        'posBottom','posHeight','position','posLeft','posRight','posTop','posWidth','quotes','richness',
        'right','rubyAlign','rubyOverhang','rubyPosition','scrollbar3dLightColor','scrollbarArrowColor',
        'scrollbarBaseColor','scrollbarDarkShadowColor','scrollbarFaceColor','scrollbarHighlightColor',
        'scrollbarShadowColor','scrollbarTrackColor','size','speak','speakHeader','speakNumeral',
        'speakPunctuation','speechRate','stress','styleFloat','tableLayout','textAlign','textAlignLast',
        'textAutospace','textDecoration','textDecorationBlink','textDecorationLineThrough','textDecorationNone',
        'textDecorationOverline','textDecorationUnderline','textIndent','textJustify','textJustifyTrim',
        'textKashida','textKashidaSpace','textOverflow','textShadow','textTransform','textUnderlinePosition',
        'top','unicodeBidi','verticalAlign','visibility','voiceFamily','volume','whiteSpace','widows','width',
        'wordBreak','wordSpacing','wordWrap','writingMode','zIndex','zoom'];

    var ta = document.getElementById('myTextarea');
    for (var prop in properties) {
      prop = properties[prop];
      var node = document.createElement('div');
      var buffer = prop + ':';
      try {
        buffer += node.style[prop];
        node.style[prop] = '42.0';
        buffer += ',' + node.style[prop];
        node.style[prop] = '42.7';
        buffer += ',' + node.style[prop];
        node.style[prop] = '42';
        buffer += ',' + node.style[prop];
      } catch (e) {
          buffer += ',' + 'error';
      }
      ta.value += buffer + '\n';
    }
}
</script></head>
<body onload='test()'>
  <textarea id='myTextarea' cols='120' rows='40'></textarea>
</body></html>
     */

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "success", "success", "success", "success" })
    public void width_like_properties() throws Exception {
        width_like_properties("bottom", "left", "right", "top");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "borderBottomWidth 42% - 42em", "borderLeftWidth 42% - 42em",
                "borderRightWidth 42% - 42em", "borderTopWidth 42% - 42em" })
    @NotYetImplemented
    public void width_like_properties_border() throws Exception {
        width_like_properties("borderBottomWidth", "borderLeftWidth", "borderRightWidth", "borderTopWidth");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "success", "success", "success", "success" })
    public void width_like_properties_margin() throws Exception {
        width_like_properties("marginBottom", "marginLeft", "marginRight", "marginTop");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "success", "success", "success", "success" })
    public void width_like_properties_padding() throws Exception {
        width_like_properties("paddingBottom", "paddingLeft", "paddingRight", "paddingTop");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "success", "success", "success", "success", "success", "success" },
            FF = { "success", "success", "maxHeight 42.0 - ; 42.7 - ; 42 - ",
                    "maxWidth 42.0 - ; 42.7 - ; 42 - ", "minHeight 42.0 - ; 42.7 - ; 42 - ",
                    "minWidth 42.0 - ; 42.7 - ; 42 - " })
    @NotYetImplemented(FF)
    public void width_like_properties_heightWidth() throws Exception {
        width_like_properties("height", "width", "maxHeight", "maxWidth", "minHeight", "minWidth");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "success", "letterSpacing 42% - 42em",
                        "outlineWidth 42% - 42em", "success", "success",
                        "wordSpacing 42% - 42em" },
            FF = { "success", "letterSpacing 42% - 42em",
                        "outlineWidth 42.0 - ; 42.7 - ; 42 - ; 42% - 42em",
                        "textIndent 42.0 - ; 42.7 - ; 42 - ",
                        "verticalAlign 42.0 - ; 42.7 - ; 42 - ",
                        "wordSpacing 42% - 42em" })
    @NotYetImplemented
    public void width_like_properties_font() throws Exception {
        width_like_properties("fontSize", "letterSpacing", "outlineWidth", "textIndent",
                        "verticalAlign", "wordSpacing");
    }

    private void width_like_properties(final String... properties) throws Exception {
        final String props = "'" + StringUtils.join(properties, "', '") + "'";
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var properties = [" + props + "];\n"
            + "\n"
            + "  for (var prop in properties) {\n"
            + "    prop = properties[prop];\n"
            + "    var result = '';\n"

            + "    var node = document.createElement('div');\n"
            + "    if (node.style[prop] != '') {\n"
            + "      if (result == '') { result += prop } else { result += '; ' }\n"
            + "      result += ' initial ' + node.style[prop];\n"
            + "    }\n"

            + "    node.style[prop] = '42.0';\n"
            + "    if (node.style[prop] != '42px') {\n"
            + "      if (result == '') { result += prop } else { result += ';' }\n"
            + "      result += ' 42.0 - ' + node.style[prop];\n"
            + "    }\n"

            + "    node.style[prop] = '42.7';\n"
            + "    var expected = document.all ? '42px' : '42.7px';\n"
            + "    if (node.style[prop] != expected) {\n"
            + "      if (result == '') { result += prop } else { result += ';' }\n"
            + "      result += ' 42.7 - ' + node.style[prop];\n"
            + "    }\n"

            + "    node.style[prop] = '42';\n"
            + "    if (node.style[prop] != '42px') {\n"
            + "      if (result == '') { result += prop } else { result += ';' }\n"
            + "      result += ' 42 - ' + node.style[prop];\n"
            + "    }\n"

            + "    node.style[prop] = '42px';\n"
            + "    if (node.style[prop] != '42px') {\n"
            + "      if (result == '') { result += prop } else { result += ';' }\n"
            + "      result += ' 42px - ' + node.style[prop];\n"
            + "    }\n"

            + "    node.style[prop] = '42mm';\n"
            + "    if (node.style[prop] != '42mm') {\n"
            + "      if (result == '') { result += prop } else { result += ';' }\n"
            + "      result += ' 42mm - ' + node.style[prop];\n"
            + "    }\n"

            + "    node.style[prop] = '42em';\n"
            + "    if (node.style[prop] != '42em') {\n"
            + "      if (result == '') { result += prop } else { result += ';' }\n"
            + "      result += ' 42em - ' + node.style[prop];\n"
            + "    }\n"

            + "    node.style[prop] = '42%';\n"
            + "    if (node.style[prop] != '42%') {\n"
            + "      if (result == '') { result += prop } else { result += ';' }\n"
            + "      result += ' 42% - ' + node.style[prop];\n"
            + "    }\n"

            + "    alert(result == '' ? 'success' : result);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void properties() throws Exception {
        final String expected = loadExpectation("CSSStyleDeclaration2Test.properties", ".txt");

        final String html
            = "<html>\n"
            + "<head><title>Tester</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  var array = [];\n"
            + "  for (var i in style) {\n"
            + "    try {\n"
            + "      if (eval('style.' + i) == '') { array.push(i); }\n"
            + "    } catch(e) {}\n" // ignore strange properties like '@@iterator'
            + "  }\n"
            + "  array.sort();\n"
            + "  document.getElementById('myTextarea').value = array.join('\\n');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'><br>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        String actual = driver.findElement(By.id("myTextarea")).getAttribute("value");
        actual = StringUtils.replace(actual, "\r\n", "\n");
        assertEquals(expected, actual);
    }

    /**
     * Test types of properties.
     * @throws Exception if the test fails
     */
    @Test
    public void properties2() throws Exception {
        final String expected = loadExpectation("CSSStyleDeclaration2Test.properties2", ".txt");

        final String html
            = "<html>\n"
            + "<head><title>Tester</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  var array = [];\n"
            + "  for (var i in style) {\n"
            + "    try {\n"
            + "      if (eval('style.' + i) === '') { array.push(i); }\n"
            + "    } catch(e) {}\n" // ignore strange properties like '@@iterator'
            + "  }\n"
            + "  array.sort();\n"
            + "  document.getElementById('myTextarea').value = array.join('\\n');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'><br>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        String actual = driver.findElement(By.id("myTextarea")).getAttribute("value");
        actual = StringUtils.replace(actual, "\r\n", "\n");
        assertEquals(expected, actual);
    }
}
