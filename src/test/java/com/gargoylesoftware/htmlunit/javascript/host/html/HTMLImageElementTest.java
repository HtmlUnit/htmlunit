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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF17;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HTMLImageElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLImageElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLImageElement]",
            IE8 = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myId'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLImageElement]",
            IE8 = "[object]")
    public void simpleScriptable2() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <image id='myId'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * This test verifies that JavaScript can be used to get the <tt>src</tt> attribute of an <tt>&lt;img&gt;</tt> tag.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§foo.gif")
    public void getSrc() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    alert(document.getElementById('anImage').src);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<img src='foo.gif' id='anImage'/>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(""); // to have a dummy response for the image
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getSrc_newImage_srcNotSet() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    var oImg = new Image();\n"
            + "    alert(oImg.src);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * This test verifies that when JavaScript is used to modify the <tt>src</tt> attribute, the value is
     * persisted to the corresponding <tt>&lt;img&gt;</tt> node in the DOM tree.
     * @throws Exception if the test fails
     */
    @Test
    public void setSrc() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    document.getElementById('anImage').src = 'bar.gif';\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<img src='foo.gif' id='anImage'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement image = driver.findElement(By.id("anImage"));
        assertEquals(URL_FIRST + "bar.gif", image.getAttribute("src"));
    }

    /**
     * JavaScript can be used to preload images, as follows:
     * <code>var newImage = new Image(); newImage.src = 'foo.gif';</code>.
     * When <code>new Image()</code> is called, HtmlUnit creates a new JavaScript
     * Image object. However, no corresponding DOM node is created, which is
     * just as well, since browers don't create one either.
     * This test verifies that the above JavaScript code can be invoked without
     * throwing an "IllegalStateException: DomNode has not been set for this
     * SimpleScriptable."
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§bar.gif")
    public void setSrc_newImage() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    var preloadImage = new Image();\n"
            + "    preloadImage.src = 'bar.gif';\n"
            + "    alert(preloadImage.src);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(""); // to have a dummy response for the image
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void attributeName() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var oImg = document.getElementById('myImage');\n"
            + "  oImg.name = 'foo';\n"
            + "  alert(oImg.name);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<img src='foo.png' id='myImage'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(""); // to have a dummy response for the image
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void onLoad_BadUrl() throws Exception {
        final String html = "<html><body><img src='http:// [/url]http://x.com/a/b' onload='alert(1)'/></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "relative", "", "" })
    public void newImage() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    var i = new Image();\n"
            + "    alert(i.style != null);\n"
            + "    i.style.position = 'relative';\n"
            + "    alert(i.style.position);\n"
            + "    alert(i.border);\n"
            + "    alert(i.alt);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "left", "right", "middle", "justify", "bottom", "middle",
                    "top", "absbottom", "absmiddle", "bottom", "texttop", "wrong", "" },
            IE = { "left", "right", "center", "", "bottom", "middle",
                "top", "absBottom", "absMiddle", "baseline", "textTop", "", "" })
    @NotYetImplemented({ FF17, FF24 })
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <img id='i1' align='left' />\n"
            + "  <img id='i2' align='right' />\n"
            + "  <img id='i3' align='center' />\n"
            + "  <img id='i4' align='justify' />\n"
            + "  <img id='i5' align='bottom' />\n"
            + "  <img id='i6' align='middle' />\n"
            + "  <img id='i7' align='top' />\n"
            + "  <img id='i8' align='absbottom' />\n"
            + "  <img id='i9' align='absmiddle' />\n"
            + "  <img id='i10' align='baseline' />\n"
            + "  <img id='i11' align='texttop' />\n"
            + "  <img id='i12' align='wrong' />\n"
            + "  <img id='i13' />\n"

            + "<script>\n"
            + "  for (i=1; i<=13; i++) {\n"
            + "    alert(document.getElementById('i'+i).align);\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "CenTer", "8", "foo", "left", "right", "middle", "justify",
                "bottom", "middle", "top", "absbottom", "absmiddle", "bottom", "texttop" },
            IE = { "center", "error", "center", "error", "center", "left", "right",
                "center", "error", "center", "bottom", "middle", "top", "absBottom",
                "absMiddle", "baseline", "textTop" })
    @NotYetImplemented({ FF17, FF24 })
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <img id='i1' align='left' />\n"

            + "<script>\n"
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) { alert('error'); }\n"
            + "    alert(elem.align);\n"
            + "  }\n"

            + "  var elem = document.getElementById('i1');\n"
            + "  setAlign(elem, 'CenTer');\n"

            + "  setAlign(elem, '8');\n"
            + "  setAlign(elem, 'foo');\n"

            + "  setAlign(elem, 'left');\n"
            + "  setAlign(elem, 'right');\n"
            + "  setAlign(elem, 'center');\n"
            + "  setAlign(elem, 'justify');\n"
            + "  setAlign(elem, 'bottom');\n"
            + "  setAlign(elem, 'middle');\n"
            + "  setAlign(elem, 'top');\n"
            + "  setAlign(elem, 'absbottom');\n"
            + "  setAlign(elem, 'absmiddle');\n"
            + "  setAlign(elem, 'baseline');\n"
            + "  setAlign(elem, 'texttop');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test image's width and height.
     * Regression test for bug
     * http://sourceforge.net/tracker/?func=detail&atid=448266&aid=2861064&group_id=47038
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "number: 300", "number: 200", "number: 24", "number: 24", "number: 24", "number: 24" },
            CHROME = { "number: 300", "number: 200", "number: 0", "number: 0", "number: 0", "number: 0" },
            IE = { "number: 300", "number: 200", "number: 28", "number: 30", "number: 1", "number: 1" },
            IE11 = { "number: 300", "number: 200", "number: 28", "number: 30", "number: 28", "number: 30" })
    public void testWidthHeightWithoutSource() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfos(imageId) {\n"
            + "    var img = document.getElementById(imageId);\n"
            + "    alert(typeof(img.width) + ': ' + img.width);\n"
            + "    alert(typeof(img.height) + ': ' + img.height);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    showInfos('myImage1');\n"
            + "    showInfos('myImage2');\n"
            + "    showInfos('myImage3');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage1' width='300' height='200'>\n"
            + "  <img id='myImage2'>\n"
            + "  <img id='myImage3' width='hello' height='hello'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

   /**
     * Test that image's width and height are numbers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "number: 300", "number: 200", "number: 1", "number: 1", "number: 1", "number: 1" })
    public void testWidthHeightWithSource() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfos(imageId) {\n"
            + "    var img = document.getElementById(imageId);\n"
            + "    alert(typeof(img.width) + ': ' + img.width);\n"
            + "    alert(typeof(img.height) + ': ' + img.height);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    showInfos('myImage1');\n"
            + "    showInfos('myImage2');\n"
            + "    showInfos('myImage3');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage1' src='" + URL_SECOND + "' width='300' height='200'>\n"
            + "  <img id='myImage2' src='" + URL_SECOND + "' >\n"
            + "  <img id='myImage3' src='" + URL_SECOND + "' width='hello' height='hello'>\n"
            + "</body></html>";

        final URL url = getClass().getClassLoader().getResource("testfiles/tiny-jpg.img");
        final FileInputStream fis = new FileInputStream(new File(url.toURI()));
        final byte[] directBytes = IOUtils.toByteArray(fis);
        fis.close();

        final MockWebConnection webConnection = getMockWebConnection();
        final List<NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_SECOND, directBytes, 200, "ok", "image/jpg", emptyList);

        loadPageWithAlerts2(html);
    }

    /**
     * Test that image's width and height are numbers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "number: 300", "number: 200", "number: 24", "number: 24", "number: 24", "number: 24" },
            CHROME = { "number: 300", "number: 200", "number: 18", "number: 20", "number: 18", "number: 20" },
            IE = { "number: 300", "number: 200", "number: 1", "number: 1", "number: 1", "number: 1" })
    public void testWidthHeightInvalidSource() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfos(imageId) {\n"
            + "    var img = document.getElementById(imageId);\n"
            + "    alert(typeof(img.width) + ': ' + img.width);\n"
            + "    alert(typeof(img.height) + ': ' + img.height);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    showInfos('myImage1');\n"
            + "    showInfos('myImage2');\n"
            + "    showInfos('myImage3');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage1' src='" + URL_SECOND + "' width='300' height='200'>\n"
            + "  <img id='myImage2' src='" + URL_SECOND + "' >\n"
            + "  <img id='myImage3' src='" + URL_SECOND + "' width='hello' height='hello'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
