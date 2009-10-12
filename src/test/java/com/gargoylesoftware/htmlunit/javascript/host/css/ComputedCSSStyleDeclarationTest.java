/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link ComputedCSSStyleDeclaration}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class ComputedCSSStyleDeclarationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "none", IE = "undefined")
    public void cssFloat() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    var s = window.getComputedStyle ? window.getComputedStyle(e,null) : e.currentStyle;\n"
            + "    alert(s.cssFloat);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Compares all style and getComputedStle.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    public void stringProperties() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  var e = document.getElementById('myDiv');\n"
            + "  var str = '';\n"
            + "  for (var i in e.style) {\n"
            + "    var s1 = eval('e.style.' + i);\n"
            + "    var s2 = eval('window.getComputedStyle(e,null).' + i);\n"
            + "    if(typeof s1 == 'string')\n"
            + "      str += i + '=' + s1 + ':' + s2 + ',';\n"
            + "  }\n"
            + "  document.getElementById('myTextarea').value = str;\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'><br>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "</body></html>";

        final String expectedText = "cssText=:,azimuth=:,background=:,"
            + "backgroundAttachment=:scroll,backgroundColor=:transparent,backgroundImage=:none,"
            + "backgroundPosition=:,backgroundRepeat=:repeat,border=:,borderCollapse=:separate,borderColor=:,"
            + "borderSpacing=:0px 0px,borderStyle=:,borderTop=:,borderRight=:,borderBottom=:,borderLeft=:,"
            + "borderTopColor=:rgb(0, 0, 0),borderRightColor=:rgb(0, 0, 0),borderBottomColor=:rgb(0, 0, 0),"
            + "borderLeftColor=:rgb(0, 0, 0),borderTopStyle=:none,borderRightStyle=:none,borderBottomStyle=:none,"
            + "borderLeftStyle=:none,borderTopWidth=:0px,borderRightWidth=:0px,borderBottomWidth=:0px,"
            + "borderLeftWidth=:0px,borderWidth=:,bottom=:auto,captionSide=:top,clear=:none,clip=:auto,"
            + "color=:rgb(0, 0, 0),content=:,counterIncrement=:none,counterReset=:none,cue=:,cueAfter=:,cueBefore=:,"
            + "cursor=:auto,direction=:ltr,display=:block,elevation=:,emptyCells=:-moz-show-background,cssFloat=:none,"
            + "font=:,fontFamily=:serif,fontSize=:16px,fontSizeAdjust=:none,fontStretch=:,fontStyle=:normal,"
            + "fontVariant=:normal,fontWeight=:400,height=:363px,left=:auto,letterSpacing=:normal,lineHeight=:normal,"
            + "listStyle=:,listStyleImage=:none,listStylePosition=:outside,listStyleType=:disc,margin=:,"
            + "marginTop=:0px,marginRight=:0px,marginBottom=:0px,marginLeft=:0px,markerOffset=:none,marks=:,"
            + "maxHeight=:none,maxWidth=:none,minHeight=:0px,minWidth=:0px,orphans=:,outline=:,"
            + "outlineColor=:rgb(0, 0, 0),outlineStyle=:none,outlineWidth=:0px,overflow=:visible,padding=:,"
            + "paddingTop=:0px,paddingRight=:0px,paddingBottom=:0px,paddingLeft=:0px,page=:,pageBreakAfter=:,"
            + "pageBreakBefore=:,pageBreakInside=:,pause=:,pauseAfter=:,pauseBefore=:,pitch=:,pitchRange=:,"
            + "position=:static,quotes=:,richness=:,right=:auto,size=:,speak=:,speakHeader=:,speakNumeral=:,"
            + "speakPunctuation=:,speechRate=:,stress=:,tableLayout=:auto,textAlign=:start,textDecoration=:none,"
            + "textIndent=:0px,textShadow=:,textTransform=:none,top=:auto,unicodeBidi=:normal,verticalAlign=:baseline,"
            + "visibility=:visible,voiceFamily=:,volume=:,whiteSpace=:normal,widows=:,width=:1256px,"
            + "wordSpacing=:normal,zIndex=:auto,MozAppearance=:none,MozBackgroundClip=:border,"
            + "MozBackgroundInlinePolicy=:continuous,MozBackgroundOrigin=:padding,MozBinding=:none,"
            + "MozBorderBottomColors=:none,MozBorderLeftColors=:none,MozBorderRightColors=:none,"
            + "MozBorderTopColors=:none,MozBorderRadius=:,MozBorderRadiusTopleft=:0px,MozBorderRadiusTopright=:0px,"
            + "MozBorderRadiusBottomleft=:0px,MozBorderRadiusBottomright=:0px,MozBoxAlign=:stretch,"
            + "MozBoxDirection=:normal,MozBoxFlex=:0,MozBoxOrient=:horizontal,MozBoxOrdinalGroup=:1,MozBoxPack=:start,"
            + "MozBoxSizing=:content-box,MozColumnCount=:auto,MozColumnWidth=:auto,MozColumnGap=:0px,"
            + "MozFloatEdge=:content-box,MozForceBrokenImageIcon=:,MozImageRegion=:auto,MozMarginEnd=:,"
            + "MozMarginStart=:,MozOpacity=:1,MozOutline=:,MozOutlineColor=:rgb(0, 0, 0),MozOutlineRadius=:,"
            + "MozOutlineRadiusTopleft=:0px,MozOutlineRadiusTopright=:0px,MozOutlineRadiusBottomleft=:0px,"
            + "MozOutlineRadiusBottomright=:0px,MozOutlineStyle=:none,MozOutlineWidth=:0px,MozOutlineOffset=:0px,"
            + "MozPaddingEnd=:,MozPaddingStart=:,MozUserFocus=:none,MozUserInput=:auto,MozUserModify=:read-only,"
            + "MozUserSelect=:auto,opacity=:1,outlineOffset=:0px,overflowX=:visible,overflowY=:visible,";

        final WebDriver driver = loadPage2(html);
        final List<String> expectedValues = stringProperties(expectedText);
        final List<String> collectedValues = stringProperties(driver.findElement(By.id("myTextarea")).getValue());
        assertEquals(expectedValues.toString(), collectedValues.toString());
    }

    private List<String> stringProperties(final String string) throws Exception {
        final List<String> values = new ArrayList<String>();
        if (string.length() == 0) {
            return values;
        }

        //string.split(",") will not work because we have values of 'rgb(0, 0, 0)'
        int i = string.indexOf('=');
        i = string.indexOf('=', i + 1);
        int p0;
        for (p0 = 0; i != -1;) {
            final int p1 = string.lastIndexOf(',', i);
            values.add(string.substring(p0, p1));
            i = string.indexOf('=', i + 1);
            p0 = p1 + 1;
        }
        values.add(string.substring(p0, string.length() - 1));

        Collections.sort(values, new Comparator<String>() {

            public int compare(String o1, String o2) {
                o1 = o1.substring(0, o1.indexOf('='));
                o2 = o2.substring(0, o2.indexOf('='));
                return o1.compareToIgnoreCase(o2);
            }

        });
        return values;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({"", "", "auto", "pointer" })
    public void styleElement() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<style type='text/css'>\n"
            + "  /* <![CDATA[ */\n"
            + "  #myDiv2 {cursor: pointer}\n"
            + "  /* ]]> */\n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('myDiv1');\n"
            + "     var div2 = document.getElementById('myDiv2');\n"
            + "     alert(div1.style.cursor);\n"
            + "     alert(div2.style.cursor);\n"
            + "     alert(window.getComputedStyle(div1, null).cursor);\n"
            + "     alert(window.getComputedStyle(div2, null).cursor);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv1'/>\n"
            + "  <div id='myDiv2'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Some style tests. There are two points in this case:
     * <ol>
     *  <li>https://sourceforge.net/tracker/index.php?func=detail&aid=1566274&group_id=82996&atid=567969</li>
     *  <li>the "pointer" value gets inherited by "myDiv2", which is parsed as a child of "style_test_1"</li>
     * </ol>
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({"", "", "pointer", "pointer" })
    public void styleElement2() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<style type='text/css'>\n"
            + "  /* <![CDATA[ */\n"
            + "  #style_test_1 {cursor: pointer}\n"
            + "  /* ]]> */\n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('style_test_1');\n"
            + "     var div2 = document.getElementById('myDiv2');\n"
            + "     alert(div1.style.cursor);\n"
            + "     alert(div2.style.cursor);\n"
            + "     alert(window.getComputedStyle(div1, null).cursor);\n"
            + "     alert(window.getComputedStyle(div2, null).cursor);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='style_test_1'/>\n"
            + "  <div id='myDiv2'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "0", "number" })
    public void zIndex() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    alert(e.currentStyle['zIndex']);\n"
            + "    alert(typeof e.currentStyle['zIndex']);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("50px")
    public void styleAttributePreferredOverStylesheet() throws Exception {
        final String html = "<html>\n"
            + "<head><style>div { width: 30px; }</style></head>\n"
            + "<body>\n"
            + "<div id='d' style='width:50px'>foo</div>\n"
            + "<script>\n"
            + "var d = document.getElementById('d');\n"
            + "var style = d.currentStyle;\n"
            + "style = style ? style : window.getComputedStyle(d,'');\n"
            + "alert(style.width);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "1em 1em", "1em 1em", "1em 1em", "1em 1em", "1em 1em", "1em 1em", "1em 1em", "1em 1em", "1em 1em",
                   "1em 1em", "1em 1em", "1em 1em", "1em 1em", "1em 1em" },
            FF = { "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px",
                   "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px" })
    public void lengthsConvertedToPixels() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d' style='width:1em; height:1em; border:1em solid black; padding:1em; margin:1em;'>d</div>\n"
            + "<script>\n"
            + "var d = document.getElementById('d');\n"
            + "var cs = d.currentStyle;\n"
            + "if(!cs) cs = window.getComputedStyle(d, '');\n"
            + "alert(d.style.width + ' ' + cs.width);\n"
            + "alert(d.style.height + ' ' + cs.height);\n"
            + "alert(d.style.borderBottomWidth + ' ' + cs.borderBottomWidth);\n"
            + "alert(d.style.borderLeftWidth + ' ' + cs.borderLeftWidth);\n"
            + "alert(d.style.borderTopWidth + ' ' + cs.borderTopWidth);\n"
            + "alert(d.style.borderRightWidth + ' ' + cs.borderRightWidth);\n"
            + "alert(d.style.paddingBottom + ' ' + cs.paddingBottom);\n"
            + "alert(d.style.paddingLeft + ' ' + cs.paddingLeft);\n"
            + "alert(d.style.paddingTop + ' ' + cs.paddingTop);\n"
            + "alert(d.style.paddingRight + ' ' + cs.paddingRight);\n"
            + "alert(d.style.marginBottom + ' ' + cs.marginBottom);\n"
            + "alert(d.style.marginLeft + ' ' + cs.marginLeft);\n"
            + "alert(d.style.marginTop + ' ' + cs.marginTop);\n"
            + "alert(d.style.marginRight + ' ' + cs.marginRight);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "block block block block block block block block block",
            FF = "table table-header-group table-row-group table-cell table-row table-cell block list-item block")
    public void defaultDisplayValues() throws Exception {
        final String html = "<html><body>\n"
            + "  <table id='table'>\n"
            + "    <thead id='thead'><tr id='tr'><th id='th'>header</th></tr></thead>\n"
            + "    <tbody id='tbody'><tr><td id='td'>body</td></tr></tbody>\n"
            + "  </table>\n"
            + "  <ul id='ul'><li id='li'>blah</li></ul>\n"
            + "  <div id='div'></div>\n"
            + "  <script>\n"
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      return e.currentStyle ? e.currentStyle.display : window.getComputedStyle(e, '').display;\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    alert(x('table') + ' ' + x('thead') + ' ' + x('tbody') + ' ' + x('th') + ' ' + x('tr') +\n"
            + "      ' ' + x('td') + ' ' + x('ul') + ' ' + x('li') + ' ' + x('div'));</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "transparent", "red", "white" },
            FF = { "transparent", "rgb(255, 0, 0)", "rgb(255, 255, 255)" })
    public void backgroundColor() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d0'>div 0</div>\n"
            + "<div id='d1' style='background: red'>d</div>\n"
            + "<div id='d2' style='background: white url(http://htmlunit.sf.net/foo.png) repeat-x fixed top right'>"
            + "second div</div>\n"
            + "<script>\n"
            + "function getStyle(x) {\n"
            + "  var d = document.getElementById(x);\n"
            + "  var cs = d.currentStyle;\n"
            + "  if(!cs) cs = window.getComputedStyle(d, '');\n"
            + "  return cs;\n"
            + "}\n"
            + "var cs0 = getStyle('d0');\n"
            + "alert(cs0.backgroundColor);\n"
            + "var cs1 = getStyle('d1');\n"
            + "alert(cs1.backgroundColor);\n"
            + "var cs2 = getStyle('d2');\n"
            + "alert(cs2.backgroundColor);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10px")
    public void fontSize() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d0' style='font-size: 10px;'>\n"
            + "<div id='d1'>inside</div>\n"
            + "</div>\n"
            + "<script>\n"
            + "function getStyle(x) {\n"
            + "  var d = document.getElementById(x);\n"
            + "  var cs = d.currentStyle;\n"
            + "  if(!cs) cs = window.getComputedStyle(d, '');\n"
            + "  return cs;\n"
            + "}\n"
            + "var cs1 = getStyle('d1');\n"
            + "alert(cs1.fontSize);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "1256px", "auto" }, IE = { "auto", "auto" })
    public void computedWidthOfHiddenElements() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('myDiv1');\n"
            + "     var cs1 = window.getComputedStyle ? window.getComputedStyle(div1, null) : div1.currentStyle;\n"
            + "     alert(cs1.width);\n"
            + "     var div2 = document.getElementById('myDiv2');\n"
            + "     var cs2 = window.getComputedStyle ? window.getComputedStyle(div2, null) : div2.currentStyle;\n"
            + "     alert(cs2.width);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv1'></div>\n"
            + "  <div id='myDiv2' style='display:none'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(content);
    }

    /**
     * Verifies that at least one CSS attribute is correctly inherited by default.
     * Required by the MochiKit tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ ",", "separate,separate", "collapse,", "collapse,collapse" })
    public void inheritedImplicitly() throws Exception {
        final String html
            = "<html><body><table id='a'><tr id='b'><td>a</td></tr></table><script>\n"
            + "var a = document.getElementById('a');\n"
            + "var b = document.getElementById('b');\n"
            + "var as = a.style;\n"
            + "var bs = b.style;\n"
            + "var acs = window.getComputedStyle ? window.getComputedStyle(a,null) : a.currentStyle;\n"
            + "var bcs = window.getComputedStyle ? window.getComputedStyle(b,null) : b.currentStyle;\n"
            + "alert(as.borderCollapse + ',' + bs.borderCollapse);\n"
            + "alert(acs.borderCollapse + ',' + bcs.borderCollapse);\n"
            + "as.borderCollapse = 'collapse';\n"
            + "alert(as.borderCollapse + ',' + bs.borderCollapse);\n"
            + "alert(acs.borderCollapse + ',' + bcs.borderCollapse);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that when the class of an ancestor node matters for the effective style,
     * it is recomputed if the class of the ancestor node changes.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "underline", "none", "underline" },
            IE = { "underline", "underline", "underline" })
    public void changeInParentClassNodeReferencedByRule() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function readDecoration(id) {\n"
            + "  var e = document.getElementById(id);\n"
            + "  var s = window.getComputedStyle ? window.getComputedStyle(e,null) : e.currentStyle;\n"
            + "  alert(s.textDecoration)\n"
            + "}\n"
            + "function test() {\n"
            + "  var fooA = document.getElementById('fooA');\n"
            + "  readDecoration('fooB')\n"
            + "  fooA.setAttribute('class', '');\n"
            + "  readDecoration('fooB')\n"
            + "  fooA.setAttribute('class', 'A');\n"
            + "  readDecoration('fooB')\n"
            + "}\n"
            + "</script>\n"
            + "<style>\n"
            + ".A .B { text-decoration: underline }\n"
            + "</style>\n"
            + "</head><body onload='test()'>\n"
            + "<div class='A' id='fooA'>A\n"
            + "<div class='B' id='fooB'>B</div></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "200px,400px", "200,400", "200px,400px", "50%,25%", "100,100", "100px,100px" },
            IE = { "200px,400px", "200,400", "200px,400px", "50%,25%", "100,100", "50%,25%" })
    public void widthAndHeightPercentages() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:200px;height:400px'><div id='d2' style='width:50%;height:25%'></div></div>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var d1 = document.getElementById('d1');\n"
            + "    var s1 = window.getComputedStyle ? window.getComputedStyle(d1, null) : d1.currentStyle;\n"
            + "    var d2 = document.getElementById('d2');\n"
            + "    var s2 = window.getComputedStyle ? window.getComputedStyle(d2, null) : d2.currentStyle;\n"
            + "    alert(d1.style.width + ',' + d1.style.height);\n"
            + "    alert(d1.offsetWidth + ',' + d1.offsetHeight);\n"
            + "    alert(s1.width + ',' + s1.height);\n"
            + "    alert(d2.style.width + ',' + d2.style.height);\n"
            + "    alert(d2.offsetWidth + ',' + d2.offsetHeight);\n"
            + "    alert(s2.width + ',' + s2.height);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

}
