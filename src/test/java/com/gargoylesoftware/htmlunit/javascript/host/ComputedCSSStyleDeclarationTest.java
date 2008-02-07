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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for {@link ComputedCSSStyleDeclaration}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class ComputedCSSStyleDeclarationTest extends WebTestCase2 {

    /**
     * @throws Exception If the test fails
     */
    @Test
    public void testCSSFloat() throws Exception {
        final String content = "<html>\n"
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
        
        final String[] expectedAlerts = {"none"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Compares all style and getComputedStle.
     * @throws Exception If the test fails
     */
    @Test
    public void testStringProperties() throws Exception {
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
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, html, null);
        final List<String> expectedValues = testStringProperties(expectedText);
        final List<String> collectedValues =
            testStringProperties(((HtmlTextArea) page.getHtmlElementById("myTextarea")).getText());
        assertEquals(expectedValues, collectedValues);
    }

    private List<String> testStringProperties(final String string) throws Exception {
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
    public void testStyleElements() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String content = "<html><head><title>foo</title>\n"
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

        final String[] expectedAlerts = {"", "", "auto", "pointer"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
