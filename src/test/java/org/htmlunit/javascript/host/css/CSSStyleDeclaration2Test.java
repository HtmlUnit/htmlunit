/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.css;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link CSSStyleDeclaration}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
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
      } catch(e) {
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
    @Alerts({"bottom: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "left: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "right: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "top: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]"})
    public void width_like_properties() throws Exception {
        width_like_properties("bottom", "left", "right", "top");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"borderBottomWidth: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42em]",
             "borderLeftWidth: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42em]",
             "borderRightWidth: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42em]",
             "borderTopWidth: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42em]"})
    public void width_like_properties_border() throws Exception {
        width_like_properties("borderBottomWidth", "borderLeftWidth", "borderRightWidth", "borderTopWidth");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"marginBottom: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "marginLeft: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "marginRight: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "marginTop: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]"})
    public void width_like_properties_margin() throws Exception {
        width_like_properties("marginBottom", "marginLeft", "marginRight", "marginTop");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"paddingBottom: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "paddingLeft: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "paddingRight: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "paddingTop: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]"})
    public void width_like_properties_padding() throws Exception {
        width_like_properties("paddingBottom", "paddingLeft", "paddingRight", "paddingTop");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"height: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "width: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "maxHeight: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "maxWidth: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "minHeight: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
             "minWidth: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]"})
    public void width_like_properties_heightWidth() throws Exception {
        width_like_properties("height", "width", "maxHeight", "maxWidth", "minHeight", "minWidth");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"fontSize: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                       "letterSpacing: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42em]",
                       "outlineWidth: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42em]",
                       "textIndent: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                       "verticalAlign: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                       "wordSpacing: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42em]"},
            FF = {"fontSize: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                  "letterSpacing: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                  "outlineWidth: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42em]",
                  "textIndent: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                  "verticalAlign: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                  "wordSpacing: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]"},
            FF_ESR = {"fontSize: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                      "letterSpacing: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                      "outlineWidth: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42em]",
                      "textIndent: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                      "verticalAlign: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]",
                      "wordSpacing: initial [] 42.0 [] 42.7 [] 42 [] 42px [42px] 42mm [42mm] 42em [42em] 42% [42%]"})
    public void width_like_properties_font() throws Exception {
        width_like_properties("fontSize", "letterSpacing", "outlineWidth", "textIndent",
                        "verticalAlign", "wordSpacing");
    }

    private void width_like_properties(final String... properties) throws Exception {
        final String props = "'" + String.join("', '", properties) + "'";
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var properties = [" + props + "];\n"
            + "\n"
            + "  for (var prop in properties) {\n"
            + "    prop = properties[prop];\n"
            + "    var result = prop + ':';\n"

            + "    var node = document.createElement('div');\n"
            + "    result += ' initial [' + node.style[prop] + ']';\n"

            + "    node.style[prop] = '42.0';\n"
            + "    result += ' 42.0 [' + node.style[prop] + ']';\n"

            + "    node.style[prop] = '42.7';\n"
            + "    result += ' 42.7 [' + node.style[prop] + ']';\n"

            + "    node.style[prop] = '42';\n"
            + "    result += ' 42 [' + node.style[prop] + ']';\n"

            + "    node.style[prop] = '42px';\n"
            + "    result += ' 42px [' + node.style[prop] + ']';\n"

            + "    node.style[prop] = '42mm';\n"
            + "    result += ' 42mm [' + node.style[prop] + ']';\n"

            + "    node.style[prop] = '42em';\n"
            + "    result += ' 42em [' + node.style[prop] + ']';\n"

            + "    node.style[prop] = '42%';\n"
            + "    result += ' 42% [' + node.style[prop] + ']';\n"

            + "    log(result);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "baseline", "sub", "super", "text-top",
             "text-bottom", "middle", "top", "bottom",
             "1.7em", "4px", "32%",
             "inherit", "initial", "revert", "unset",
             "unset", "unset", "unset"})
    public void verticalAlign() throws Exception {
        checkPropertyValues("vertical-align",
                "baseline", "sub", "super", "text-top", "text-bottom", "middle", "top", "bottom",
                "1.7em", "4px", "32%",
                "inherit", "initial", "revert", "unset",
                "1 px", "7mond", "not-supported");
        checkPropertyValuesDirect("verticalAlign",
                "baseline", "sub", "super", "text-top", "text-bottom", "middle", "top", "bottom",
                "1.7em", "4px", "32%",
                "inherit", "initial", "revert", "unset",
                "1 px", "7mond", "not-supported");
    }

    private void checkPropertyValuesDirect(final String property, final String... propertyValues) throws Exception {
        final String propValues = "'" + String.join("', '", propertyValues) + "'";
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var propValues = [" + propValues + "];\n"
            + "\n"
            + "  var node = document.createElement('div');\n"
            + "  var styleVal = node.style." + property + ";\n"
            + "  log(styleVal);\n"

            + "  propValues.forEach(propValue => {\n"
            + "    node.style." + property + " = propValue;\n"
            + "    styleVal = node.style." + property + ";\n"
            + "    log(styleVal);\n"
            + "  });\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    private void checkPropertyValues(final String property, final String... propertyValues) throws Exception {
        final String propValues = "'" + String.join("', '", propertyValues) + "'";
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var propValues = [" + propValues + "];\n"
            + "\n"
            + "  var node = document.createElement('div');\n"
            + "  var styleVal = node.style['" + property + "'];\n"
            + "  log(styleVal);\n"

            + "  propValues.forEach(propValue => {\n"
            + "    node.style['" + property + "'] = propValue;\n"
            + "    styleVal = node.style['" + property + "'];\n"
            + "    log(styleVal);\n"
            + "  });\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void properties() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
            + "  document.getElementById('myLog').value = array.join('\\n');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'><br></div>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final String expected = loadExpectation("CSSStyleDeclaration2Test.properties", ".txt");

        final WebDriver driver = loadPage2(html);
        verify(() -> driver.findElement(By.id("myLog")).getDomProperty("value"), expected);
    }

    /**
     * Test types of properties.
     * @throws Exception if the test fails
     */
    @Test
    public void properties2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
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
            + "  document.getElementById('myLog').value = array.join('\\n');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'><br></div>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final String expected = loadExpectation("CSSStyleDeclaration2Test.properties2", ".txt");

        final WebDriver driver = loadPage2(html);
        verify(() -> driver.findElement(By.id("myLog")).getDomProperty("value"), expected);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void setLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var style = document.body.style;\n"
            + "  try {\n"
            + "    log(style.length);\n"
            + "    style.length = 100;\n"
            + "    log(style.length);\n"
            + "  } catch(e) { log(e); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "Type error"})
    public void setLengthStrictMode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  'use strict';\n"
            + "  var style = document.body.style;\n"
            + "  try {\n"
            + "    log(style.length);\n"
            + "    style.length = 100;\n"
            + "    log(style.length);\n"
            + "  } catch(e) { log('Type error'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"function values() { [native code] }", "no for..of", "display"})
    public void iterator() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "</head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var style = document.body.style;\n"

                + "    if (typeof Symbol != 'undefined') {\n"
                + "      log(style[Symbol.iterator]);\n"
                + "    }\n"

                + "    if (!style.forEach) {\n"
                + "      log('no for..of');\n"
                + "    }\n"

                + "    if (typeof Symbol === 'undefined') {\n"
                + "      return;\n"
                + "    }\n"

                + "    for (var i of style) {\n"
                + "      log(i);\n"
                + "    }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()' style='display: inline'>\n"
                + "  <div></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
