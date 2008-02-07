/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase2;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for {@link CSSStyleDeclaration}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class CSSStyleDeclarationTest extends WebTestCase2 {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testStyle_OneCssAttribute() throws Exception {
        final String firstContent
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

        final String[] expectedAlerts = {"black", "pink", "color: pink;"};
        createTestPageForRealBrowserIfNeeded(firstContent, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(firstContent, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);

        assertEquals("color: pink;", page.getHtmlElementById("div1").getAttributeValue("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testStyle_MultipleCssAttributes() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var style = document.getElementById('div1').style;\n"
            + "    alert(style.color);\n"
            + "    style.color = 'pink';\n"
            + "    alert(style.color);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: black;background:blue;foo:bar'>foo</div></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(firstContent, collectedAlerts);

        final String[] expectedAlerts = {"black", "pink"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertEquals(
            "background: blue; color: pink; foo: bar;",
            page.getHtmlElementById("div1").getAttributeValue("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testStyle_OneUndefinedCssAttribute() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var style = document.getElementById('div1').style;\n"
            + "    alert(document.getElementById('nonexistingid'));\n"
            + "    alert(style.color);\n"
            + "    style.color = 'pink';\n"
            + "    alert(style.color);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'><div id='div1'>foo</div></body></html>";

        final String[] expectedAlerts = {"null", "", "pink"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);

        assertEquals("color: pink;", page.getHtmlElementById("div1").getAttributeValue("style"));
    }

    /**
     * Even if javascript isn't really executed according to the browser version used,
     * it may have some side effects if configuration is incorrect.
     * @throws Exception if the test fails
     */
    @Test
    public void testMozillaStyle() throws Exception {
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

        final String[] expectedAlerts = {"", "hidden", "undefined"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testIEStyle() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv = document.getElementById('div1');\n"
            + "    alert(oDiv.style.behavior);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1'>foo</div></body></html>";

        final String[] expectedAlerts = {""};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Checks that the scopes are correctly set on the style element (wasn't working in CVS snapshot 2005.01.23)
     * @throws Exception if the test fails
     */
    @Test
    public void testOnclickAccessStyle() throws Exception {
        final String content = "<html><head><title>Color Change Page</title>\n"
             + "<script>\n"
             + "function test(obj)\n"
             + "{\n"
             + "   obj.style.backgroundColor = 'yellow';\n"
             + "}\n"
             + "</script>\n"
             + "</head>\n"
             + "<body>\n"
             + "<span id='red' onclick='test(this)'>foo</span>\n"
             + "</body></html>";

        final HtmlPage page = loadPage(content);
        ((ClickableElement) page.getHtmlElementById("red")).click();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testAccessProperties() throws Exception {
        final String content = "<html><head><title>First</title><script>\n"
                + "function doTest() {\n"
                + "    var oDiv = document.getElementById('div1');\n"
                + "    alert(typeof oDiv.style.visibility);\n"
                + "    alert(typeof oDiv.style.color);\n"
                + "    alert(typeof oDiv.style.backgroundImage);\n"
                + "    alert(typeof oDiv.style.foo);\n"
                + "}\n</script></head>\n"
                + "<body onload='doTest()'>\n"
                + "<div id='div1'>foo</div></body></html>";

        final String[] expectedAlerts = {"string", "string", "string", "undefined"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 1592299
     * @throws Exception if the test fails
     */
    @Test
    public void testSetStylePropertyNonString() throws Exception {
        final String content = "<html><head><title>First</title><script>\n"
                + "function doTest() {\n"
                + "    var oDiv1 = document.getElementById('div1');\n"
                + "    oDiv1.style.pixelLeft = 123;\n"
                + "    alert(oDiv1.style.pixelLeft);\n"
                + "}\n</script></head>\n"
                + "<body onload='doTest()'>\n"
                + "<div id='div1'>foo</div></body></html>";

        final String[] expectedAlerts = {"123"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetPropertyValue() throws Exception {
        testGetPropertyValue(BrowserVersion.FIREFOX_2);
        try {
            testGetPropertyValue(BrowserVersion.INTERNET_EXPLORER_7_0);
            fail("getPropertyValue is not supported in IE");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void testGetPropertyValue(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv1 = document.getElementById('div1');\n"
            + "    alert(oDiv1.style.getPropertyValue('background'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='background: blue'>foo</div></body></html>";

        final String[] expectedAlerts = {"blue"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetPropertyValue_WithDash() throws Exception {
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
        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, actual);
        final String[] expected = {"30px", "", "30px", "arial", "", "arial"};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testStyleFilter_IE() throws Exception {
        testStyleFilter(BrowserVersion.INTERNET_EXPLORER_6_0, new String[] {"", "alpha(opacity=50)"});
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testStyleFilter_FF() throws Exception {
        testStyleFilter(BrowserVersion.FIREFOX_2, new String[] {"undefined", "undefined"});
    }

    private void testStyleFilter(final BrowserVersion browserVersion, final String[] expected) throws Exception {
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
        final List<String> actual = new ArrayList<String>();
        loadPage(browserVersion, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testSetExpression() throws Exception {
        testSetExpression(BrowserVersion.INTERNET_EXPLORER_7_0);
        try {
            testSetExpression(BrowserVersion.FIREFOX_2);
            fail("setExpression is not defined for Firefox");
        }
        catch (final Exception e) {
            //expected
        }
    }
    
    private void testSetExpression(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     div1.style.setExpression('title','id');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";

        loadPage(browserVersion, content, null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testRemoveExpression() throws Exception {
        testRemoveExpression(BrowserVersion.INTERNET_EXPLORER_7_0);
        try {
            testRemoveExpression(BrowserVersion.FIREFOX_2);
            fail("removeExpression is not defined for Firefox");
        }
        catch (final Exception e) {
            //expected
        }
    }
    
    private void testRemoveExpression(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     div1.style.setExpression('title','id');\n"
            + "     div1.style.removeExpression('title');"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";

        loadPage(browserVersion, content, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testBorderStyles() throws Exception {
        final String content
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

        final String[] expectedAlerts = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testProperties() throws Exception {
        testProperties(BrowserVersion.INTERNET_EXPLORER_7_0, "clear posRight backgroundRepeat borderTopStyle "
            + "marginTop fontVariant listStylePosition backgroundPositionX lineHeight scrollbarHighlightColor "
            + "overflowX paddingLeft maxWidth borderLeftWidth padding listStyleType borderLeftColor display "
            + "textDecorationLineThrough marginBottom textKashidaSpace borderCollapse textDecorationBlink "
            + "scrollbarFaceColor backgroundAttachment borderRightStyle fontStyle textUnderlinePosition textIndent "
            + "textDecorationOverline msInterpolationMode layoutGridMode right pageBreakAfter background filter "
            + "borderColor posWidth left minHeight rubyOverhang layoutGrid visibility verticalAlign borderBottomWidth "
            + "scrollbarShadowColor textTransform lineBreak scrollbarArrowColor margin borderBottomColor "
            + "borderTopWidth behavior letterSpacing layoutFlow font borderTopColor paddingBottom whiteSpace overflow "
            + "borderBottomStyle cssText width clip cursor fontSize imeMode backgroundPosition color paddingRight "
            + "textAutospace pageBreakBefore direction bottom fontFamily unicodeBidi posHeight posBottom "
            + "borderRightColor styleFloat textJustify backgroundColor posTop zIndex borderLeftStyle zoom "
            + "listStyleImage wordSpacing textDecoration borderBottom layoutGridChar tableLayout border textAlign "
            + "backgroundPositionY backgroundImage borderWidth borderTop textJustifyTrim minWidth "
            + "scrollbar3dLightColor fontWeight scrollbarDarkShadowColor textAlignLast posLeft maxHeight "
            + "borderRightWidth paddingTop wordBreak textOverflow rubyPosition borderStyle wordWrap position "
            + "overflowY textDecorationUnderline layoutGridLine top textDecorationNone writingMode height "
            + "scrollbarTrackColor listStyle borderRight scrollbarBaseColor marginRight marginLeft layoutGridType "
            + "textKashida rubyAlign borderLeft ");
        testProperties(BrowserVersion.FIREFOX_2, "length cssText azimuth background backgroundAttachment "
            + "backgroundColor backgroundImage backgroundPosition backgroundRepeat border borderCollapse borderColor "
            + "borderSpacing borderStyle borderTop borderRight borderBottom borderLeft borderTopColor "
            + "borderRightColor borderBottomColor borderLeftColor borderTopStyle borderRightStyle borderBottomStyle "
            + "borderLeftStyle borderTopWidth borderRightWidth borderBottomWidth borderLeftWidth borderWidth bottom "
            + "captionSide clear clip color content counterIncrement counterReset cue cueAfter cueBefore cursor "
            + "direction display elevation emptyCells cssFloat font fontFamily fontSize fontSizeAdjust fontStretch "
            + "fontStyle fontVariant fontWeight height left letterSpacing lineHeight listStyle listStyleImage "
            + "listStylePosition listStyleType margin marginTop marginRight marginBottom marginLeft markerOffset marks "
            + "maxHeight maxWidth minHeight minWidth orphans outline outlineColor outlineStyle outlineWidth overflow "
            + "padding paddingTop paddingRight paddingBottom paddingLeft page pageBreakAfter pageBreakBefore "
            + "pageBreakInside pause pauseAfter pauseBefore pitch pitchRange position quotes richness right size speak "
            + "speakHeader speakNumeral speakPunctuation speechRate stress tableLayout textAlign textDecoration "
            + "textIndent textShadow textTransform top unicodeBidi verticalAlign visibility voiceFamily volume "
            + "whiteSpace widows width wordSpacing zIndex MozAppearance MozBackgroundClip MozBackgroundInlinePolicy "
            + "MozBackgroundOrigin MozBinding MozBorderBottomColors MozBorderLeftColors MozBorderRightColors "
            + "MozBorderTopColors MozBorderRadius MozBorderRadiusTopleft MozBorderRadiusTopright "
            + "MozBorderRadiusBottomleft MozBorderRadiusBottomright MozBoxAlign MozBoxDirection MozBoxFlex "
            + "MozBoxOrient MozBoxOrdinalGroup MozBoxPack MozBoxSizing MozColumnCount MozColumnWidth MozColumnGap "
            + "MozFloatEdge MozForceBrokenImageIcon MozImageRegion MozMarginEnd MozMarginStart MozOpacity MozOutline "
            + "MozOutlineColor MozOutlineRadius MozOutlineRadiusTopleft MozOutlineRadiusTopright "
            + "MozOutlineRadiusBottomleft MozOutlineRadiusBottomright MozOutlineStyle MozOutlineWidth "
            + "MozOutlineOffset MozPaddingEnd MozPaddingStart MozUserFocus MozUserInput MozUserModify MozUserSelect "
            + "opacity outlineOffset overflowX overflowY ");
    }

    private void testProperties(final BrowserVersion browserVersion, final String expectedText) throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  var s = '';\n"
            + "  for (var i in style) {\n"
            + "    if (eval('style.' + i) == '')\n"
            + "      s += i + ' ';\n"
            + "  }\n"
            + "  document.getElementById('myTextarea').value = s;\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'><br>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(browserVersion, html, null);
        final List<String> expectedStyles = Arrays.asList(expectedText.split(" "));
        Collections.sort(expectedStyles);
        final List<String> collectedStyles =
            Arrays.asList(((HtmlTextArea) page.getHtmlElementById("myTextarea")).getText().split(" "));
        Collections.sort(collectedStyles);
        assertEquals(expectedStyles, collectedStyles);
    }

    /**
     * Test types of properties.
     * @throws Exception if the test fails
     */
    @Test
    public void testProperties2() throws Exception {
        testProperties2(BrowserVersion.INTERNET_EXPLORER_7_0, "clear backgroundRepeat borderTopStyle marginTop "
            + "fontVariant listStylePosition backgroundPositionX lineHeight scrollbarHighlightColor overflowX "
            + "paddingLeft maxWidth borderLeftWidth padding listStyleType borderLeftColor display marginBottom "
            + "textKashidaSpace borderCollapse scrollbarFaceColor backgroundAttachment borderRightStyle fontStyle "
            + "textUnderlinePosition textIndent msInterpolationMode layoutGridMode right pageBreakAfter background "
            + "filter borderColor left minHeight rubyOverhang layoutGrid visibility verticalAlign borderBottomWidth "
            + "scrollbarShadowColor textTransform lineBreak scrollbarArrowColor margin borderBottomColor "
            + "borderTopWidth behavior letterSpacing layoutFlow font borderTopColor paddingBottom whiteSpace overflow "
            + "borderBottomStyle cssText width clip cursor fontSize imeMode backgroundPosition color paddingRight "
            + "textAutospace pageBreakBefore direction bottom fontFamily unicodeBidi borderRightColor styleFloat "
            + "textJustify backgroundColor borderLeftStyle zoom listStyleImage wordSpacing textDecoration "
            + "borderBottom layoutGridChar tableLayout border textAlign backgroundPositionY backgroundImage "
            + "borderWidth borderTop textJustifyTrim minWidth scrollbar3dLightColor fontWeight "
            + "scrollbarDarkShadowColor textAlignLast maxHeight borderRightWidth paddingTop wordBreak textOverflow "
            + "rubyPosition borderStyle wordWrap position overflowY layoutGridLine top writingMode height "
            + "scrollbarTrackColor listStyle borderRight scrollbarBaseColor marginRight marginLeft layoutGridType "
            + "textKashida rubyAlign borderLeft ");
        testProperties2(BrowserVersion.FIREFOX_2, "cssText azimuth background backgroundAttachment backgroundColor "
            + "backgroundImage backgroundPosition backgroundRepeat border borderCollapse borderColor borderSpacing "
            + "borderStyle borderTop borderRight borderBottom borderLeft borderTopColor borderRightColor "
            + "borderBottomColor borderLeftColor borderTopStyle borderRightStyle borderBottomStyle borderLeftStyle "
            + "borderTopWidth borderRightWidth borderBottomWidth borderLeftWidth borderWidth bottom captionSide clear "
            + "clip color content counterIncrement counterReset cue cueAfter cueBefore cursor direction display "
            + "elevation emptyCells cssFloat font fontFamily fontSize fontSizeAdjust fontStretch fontStyle "
            + "fontVariant fontWeight height left letterSpacing lineHeight listStyle listStyleImage listStylePosition "
            + "listStyleType margin marginTop marginRight marginBottom marginLeft markerOffset marks maxHeight "
            + "maxWidth minHeight minWidth orphans outline outlineColor outlineStyle outlineWidth overflow padding "
            + "paddingTop paddingRight paddingBottom paddingLeft page pageBreakAfter pageBreakBefore pageBreakInside "
            + "pause pauseAfter pauseBefore pitch pitchRange position quotes richness right size speak speakHeader "
            + "speakNumeral speakPunctuation speechRate stress tableLayout textAlign textDecoration textIndent "
            + "textShadow textTransform top unicodeBidi verticalAlign visibility voiceFamily volume whiteSpace widows "
            + "width wordSpacing zIndex MozAppearance MozBackgroundClip MozBackgroundInlinePolicy MozBackgroundOrigin "
            + "MozBinding MozBorderBottomColors MozBorderLeftColors MozBorderRightColors MozBorderTopColors "
            + "MozBorderRadius MozBorderRadiusTopleft MozBorderRadiusTopright MozBorderRadiusBottomleft "
            + "MozBorderRadiusBottomright MozBoxAlign MozBoxDirection MozBoxFlex MozBoxOrient MozBoxOrdinalGroup "
            + "MozBoxPack MozBoxSizing MozColumnCount MozColumnWidth MozColumnGap MozFloatEdge "
            + "MozForceBrokenImageIcon MozImageRegion MozMarginEnd MozMarginStart MozOpacity MozOutline "
            + "MozOutlineColor MozOutlineRadius MozOutlineRadiusTopleft MozOutlineRadiusTopright "
            + "MozOutlineRadiusBottomleft MozOutlineRadiusBottomright MozOutlineStyle MozOutlineWidth "
            + "MozOutlineOffset MozPaddingEnd MozPaddingStart MozUserFocus MozUserInput MozUserModify MozUserSelect "
            + "opacity outlineOffset overflowX overflowY ");
    }

    private void testProperties2(final BrowserVersion browserVersion, final String expectedText) throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var style = document.getElementById('myDiv').style;\n"
            + "  var s = '';\n"
            + "  for (var i in style) {\n"
            + "    if (eval('style.' + i) === '')\n"
            + "      s += i + ' ';\n"
            + "  }\n"
            + "  document.getElementById('myTextarea').value = s;\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'><br>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(browserVersion, html, null);
        final List<String> expectedStyles = Arrays.asList(expectedText.split(" "));
        Collections.sort(expectedStyles);
        final List<String> collectedStyles =
            Arrays.asList(((HtmlTextArea) page.getHtmlElementById("myTextarea")).getText().split(" "));
        Collections.sort(collectedStyles);
        assertEquals(expectedStyles, collectedStyles);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testCSSText() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
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

        final String[] expectedAlerts = {"", "", "15px", "italic", "", "italic"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testBorder() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var style = document.getElementById('myDiv').style;\n"
            + "     alert(style.getPropertyValue('border-top-width'));\n"
            + "     alert(style.getPropertyValue('border-top-style'));\n"
            + "     alert(style.getPropertyValue('border-top-color'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv' style='border: red 1px solid'/>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"1px", "solid", "red"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testComputedWidthOfHiddenElements() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('myDiv1');\n"
            + "     var div2 = document.getElementById('myDiv2');\n"
            + "     alert(window.getComputedStyle(div1, null).width);\n"
            + "     alert(window.getComputedStyle(div2, null).width);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv1'/>\n"
            + "  <div id='myDiv2' style='display:none'/>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"1256px", "auto"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
