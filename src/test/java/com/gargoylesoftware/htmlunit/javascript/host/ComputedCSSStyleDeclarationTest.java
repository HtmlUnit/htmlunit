/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for {@link ComputedCSSStyleDeclaration}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ComputedCSSStyleDeclarationTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @Alerts("none")
    public void cssFloat() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    alert(window.getComputedStyle(e,null).cssFloat);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Compares all style and getComputedStle.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
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
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final List<String> expectedValues = stringProperties(expectedText);
        final List<String> collectedValues =
            stringProperties(((HtmlTextArea) page.getHtmlElementById("myTextarea")).getText());
        assertEquals(expectedValues, collectedValues);
    }

    private List<String> stringProperties(final String string) throws Exception {
        final List<String> values = new ArrayList<String>();

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
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
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

        loadPageWithAlerts(html);
    }

    /**
     * Some style tests. There are two points in this case:
     * <ol>
     *  <li>https://sourceforge.net/tracker/index.php?func=detail&aid=1566274&group_id=82996&atid=567969</li>
     *  <li>No idea why Firefox alert "pointer" for the "myDiv2", the rule should only apply to "myDiv1"</li>
     * </ol>
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
    @NotYetImplemented({ Browser.FIREFOX_2, Browser.FIREFOX_3 })
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7 })
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

        loadPageWithAlerts(html);
    }
}
