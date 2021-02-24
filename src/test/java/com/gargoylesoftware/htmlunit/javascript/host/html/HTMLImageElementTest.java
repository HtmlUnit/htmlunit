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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HTMLImageElement}.
 *
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLImageElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLImageElement]", "[object HTMLImageElement]"})
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId1'));\n"
            + "    alert(document.getElementById('myId2'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myId1'>\n"
            + "  <image id='myId2'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"IMG", "IMG"})
    public void nodeName() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId1').nodeName);\n"
            + "    alert(document.getElementById('myId2').nodeName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myId1'>\n"
            + "  <image id='myId2'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"IMG", "IMG"})
    public void tagName() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId1').tagName);\n"
            + "    alert(document.getElementById('myId2').tagName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myId1'>\n"
            + "  <image id='myId2'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLImageElement]", "[object HTMLUnknownElement]", "IMG", "IMAGE",
                "[object HTMLImageElement]", "[object HTMLImageElement]", "IMG", "IMG"},
            FF = {"[object HTMLImageElement]", "[object HTMLElement]", "IMG", "IMAGE",
                "[object HTMLImageElement]", "[object HTMLImageElement]", "IMG", "IMG"},
            FF78 = {"[object HTMLImageElement]", "[object HTMLElement]", "IMG", "IMAGE",
                "[object HTMLImageElement]", "[object HTMLImageElement]", "IMG", "IMG"},
            IE = {"[object HTMLImageElement]", "[object HTMLImageElement]", "IMG", "IMG",
                "[object HTMLImageElement]", "[object HTMLImageElement]", "IMG", "IMG"})
    public void image() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.createElement('img'));\n"
            + "    alert(document.createElement('image'));\n"
            + "    alert(document.createElement('img').nodeName);\n"
            + "    alert(document.createElement('image').nodeName);\n"
            + "    alert(document.getElementById('myId1'));\n"
            + "    alert(document.getElementById('myId2'));\n"
            + "    alert(document.getElementById('myId1').nodeName);\n"
            + "    alert(document.getElementById('myId2').nodeName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myId1'>\n"
            + "  <image id='myId2'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "undefined", "", ""},
            IE = {"", "", "", ""})
    public void src() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.createElement('img').src);\n"
            + "    alert(document.createElement('image').src);\n"
            + "    alert(document.getElementById('myId1').src);\n"
            + "    alert(document.getElementById('myId2').src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myId1'>\n"
            + "  <image id='myId2'>\n"
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
            + "function doTest() {\n"
            + "  alert(document.getElementById('anImage').src);\n"
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
            + "function doTest() {\n"
            + "  var oImg = new Image();\n"
            + "  alert(oImg.src);\n"
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
            + "function doTest() {\n"
            + "  document.getElementById('anImage').src = 'bar.gif';\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<img src='foo.gif' id='anImage'/>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(html);
        final WebElement image = driver.findElement(By.id("anImage"));
        assertEquals(URL_FIRST + "bar.gif", image.getAttribute("src"));
    }

    /**
     * JavaScript can be used to preload images, as follows:
     * <code>var newImage = new Image(); newImage.src = 'foo.gif';</code>.
     * When <code>new Image()</code> is called, HtmlUnit creates a new JavaScript
     * Image object. However, no corresponding DOM node is created, which is
     * just as well, since browsers don't create one either.
     * This test verifies that the above JavaScript code can be invoked without
     * throwing an "IllegalStateException: DomNode has not been set for this
     * SimpleScriptable."
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onLoad", "§§URL§§bar.gif"})
    public void setSrc_newImage() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest() {\n"
            + "  var preloadImage = new Image();\n"
            + "  preloadImage.onload = alert('onLoad');\n"
            + "  preloadImage.src = 'bar.gif';\n"
            + "  alert(preloadImage.src);\n"
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
    @Alerts({"true", "relative", "", ""})
    public void newImage() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest() {\n"
            + "  var i = new Image();\n"
            + "  alert(i.style != null);\n"
            + "  i.style.position = 'relative';\n"
            + "  alert(i.style.position);\n"
            + "  alert(i.border);\n"
            + "  alert(i.alt);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"left", "right", "center", "justify", "bottom", "middle",
                "top", "absbottom", "absmiddle", "baseline", "texttop", "wrong", ""},
            FF = {"left", "right", "middle", "justify", "bottom", "middle",
                "top", "absbottom", "absmiddle", "bottom", "texttop", "wrong", ""},
            FF78 = {"left", "right", "middle", "justify", "bottom", "middle",
                "top", "absbottom", "absmiddle", "bottom", "texttop", "wrong", ""},
            IE = {"left", "right", "center", "", "bottom", "middle",
                "top", "absBottom", "absMiddle", "baseline", "textTop", "", ""})
    @NotYetImplemented({FF, FF78})
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
            + "  for (var i = 1; i <= 13; i++) {\n"
            + "    alert(document.getElementById('i' + i).align);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CenTer", "8", "foo", "left", "right", "center", "justify",
                "bottom", "middle", "top", "absbottom", "absmiddle", "baseline", "texttop"},
            FF = {"CenTer", "8", "foo", "left", "right", "middle", "justify",
                "bottom", "middle", "top", "absbottom", "absmiddle", "bottom", "texttop"},
            FF78 = {"CenTer", "8", "foo", "left", "right", "middle", "justify",
                "bottom", "middle", "top", "absbottom", "absmiddle", "bottom", "texttop"},
            IE = {"center", "error", "center", "error", "center", "left", "right",
                "center", "error", "center", "bottom", "middle", "top", "absBottom",
                "absMiddle", "baseline", "textTop"})
    @NotYetImplemented({FF, FF78})
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
     * <a href="http://sourceforge.net/tracker/?func=detail&atid=448266&aid=2861064&group_id=47038">issue 915</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"number: 300", "number: 200", "number: 0", "number: 0", "number: 0", "number: 0"},
            IE = {"number: 300", "number: 200", "number: 28", "number: 30", "number: 28", "number: 30"})
    public void widthHeightWithoutSource() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfo(imageId) {\n"
            + "    var img = document.getElementById(imageId);\n"
            + "    alert(typeof(img.width) + ': ' + img.width);\n"
            + "    alert(typeof(img.height) + ': ' + img.height);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    showInfo('myImage1');\n"
            + "    showInfo('myImage2');\n"
            + "    showInfo('myImage3');\n"
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
    @Alerts({"number: 300", "number: 200", "number: 1", "number: 1", "number: 1", "number: 1"})
    public void widthHeightWithSource() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfo(imageId) {\n"
            + "    var img = document.getElementById(imageId);\n"
            + "    alert(typeof(img.width) + ': ' + img.width);\n"
            + "    alert(typeof(img.height) + ': ' + img.height);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    showInfo('myImage1');\n"
            + "    showInfo('myImage2');\n"
            + "    showInfo('myImage3');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage1' src='" + URL_SECOND + "' width='300' height='200'>\n"
            + "  <img id='myImage2' src='" + URL_SECOND + "' >\n"
            + "  <img id='myImage3' src='" + URL_SECOND + "' width='hello' height='hello'>\n"
            + "</body></html>";

        final URL url = getClass().getClassLoader().getResource("testfiles/tiny-jpg.img");
        try (FileInputStream fis = new FileInputStream(new File(url.toURI()))) {
            final byte[] directBytes = IOUtils.toByteArray(fis);

            final MockWebConnection webConnection = getMockWebConnection();
            final List<NameValuePair> emptyList = Collections.emptyList();
            webConnection.setResponse(URL_SECOND, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        loadPageWithAlerts2(html);
    }

    /**
      * Test that image's width and height are numbers.
      * @throws Exception if the test fails
      */
    @Test
    @Alerts(DEFAULT = {"number: 300", "number: 200", "number: 0", "number: 0", "number: 0", "number: 0"},
            IE = {"number: 300", "number: 200", "number: 28", "number: 30", "number: 28", "number: 30"})
    public void widthHeightEmptySource() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfo(imageId) {\n"
            + "    var img = document.getElementById(imageId);\n"
            + "    alert(typeof(img.width) + ': ' + img.width);\n"
            + "    alert(typeof(img.height) + ': ' + img.height);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    showInfo('myImage1');\n"
            + "    showInfo('myImage2');\n"
            + "    showInfo('myImage3');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage1' src='' width='300' height='200'>\n"
            + "  <img id='myImage2' src='' >\n"
            + "  <img id='myImage3' src='' width='hello' height='hello'>\n"
            + "</body></html>";

        final URL url = getClass().getClassLoader().getResource("testfiles/tiny-jpg.img");
        try (FileInputStream fis = new FileInputStream(new File(url.toURI()))) {
            final byte[] directBytes = IOUtils.toByteArray(fis);
            final MockWebConnection webConnection = getMockWebConnection();
            final List<NameValuePair> emptyList = Collections.emptyList();
            webConnection.setResponse(URL_SECOND, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        loadPageWithAlerts2(html);
    }

    /**
      * Test that image's width and height are numbers.
      * @throws Exception if the test fails
      */
    @Test
    @Alerts(DEFAULT = {"number: 300", "number: 200", "number: 24", "number: 24", "number: 24", "number: 24"},
            CHROME = {"number: 300", "number: 200", "number: 0", "number: 0", "number: 0", "number: 0"},
            EDGE = {"number: 300", "number: 200", "number: 0", "number: 0", "number: 0", "number: 0"},
            IE = {"number: 300", "number: 200", "number: 28", "number: 30", "number: 28", "number: 30"})
    public void widthHeightBlankSource() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfo(imageId) {\n"
            + "    var img = document.getElementById(imageId);\n"
            + "    alert(typeof(img.width) + ': ' + img.width);\n"
            + "    alert(typeof(img.height) + ': ' + img.height);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    showInfo('myImage1');\n"
            + "    showInfo('myImage2');\n"
            + "    showInfo('myImage3');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage1' src=' ' width='300' height='200'>\n"
            + "  <img id='myImage2' src=' ' >\n"
            + "  <img id='myImage3' src=' ' width='hello' height='hello'>\n"
            + "</body></html>";

        final URL url = getClass().getClassLoader().getResource("testfiles/tiny-jpg.img");
        try (FileInputStream fis = new FileInputStream(new File(url.toURI()))) {
            final byte[] directBytes = IOUtils.toByteArray(fis);
            final MockWebConnection webConnection = getMockWebConnection();
            final List<NameValuePair> emptyList = Collections.emptyList();
            webConnection.setResponse(URL_SECOND, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        loadPageWithAlerts2(html);
    }

    /**
     * Test that image's width and height are numbers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"number: 300", "number: 200", "number: 24", "number: 24", "number: 24", "number: 24"},
            CHROME = {"number: 300", "number: 200", "number: 16", "number: 16", "number: 16", "number: 16"},
            EDGE = {"number: 300", "number: 200", "number: 16", "number: 16", "number: 16", "number: 16"},
            IE = {"number: 300", "number: 200", "number: 28", "number: 30", "number: 28", "number: 30"})
    public void widthHeightInvalidSource() throws Exception {
        getMockWebConnection().setDefaultResponse("");

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfo(imageId) {\n"
            + "    var img = document.getElementById(imageId);\n"
            + "    alert(typeof(img.width) + ': ' + img.width);\n"
            + "    alert(typeof(img.height) + ': ' + img.height);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    showInfo('myImage1');\n"
            + "    showInfo('myImage2');\n"
            + "    showInfo('myImage3');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage1' src='" + URL_SECOND + "' width='300' height='200'>\n"
            + "  <img id='myImage2' src='" + URL_SECOND + "' >\n"
            + "  <img id='myImage3' src='" + URL_SECOND + "' width='hello' height='hello'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);

        shutDownRealIE();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true", "true"},
            IE = {"false", "false", "false", "true"})
    public void complete() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);

            final URL urlImage = new URL(URL_SECOND, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
            getMockWebConnection().setDefaultResponse("Test");
        }

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfo(imageId) {\n"
            + "    var img = document.getElementById(imageId);\n"
            + "    alert(img.complete);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    showInfo('myImage1');\n"
            + "    showInfo('myImage2');\n"
            + "    showInfo('myImage3');\n"
            + "    showInfo('myImage4');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage1' >\n"
            + "  <img id='myImage2' src=''>\n"
            + "  <img id='myImage3' src='" + URL_SECOND + "'>\n"
            + "  <img id='myImage4' src='" + URL_SECOND + "img.jpg'>\n"
            + "</body></html>";
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error2;error3;error4;load5;", "3"},
            FF = {"error2;error3;error4;load5;", "4"},
            FF78 = {"error2;error3;error4;load5;", "4"})
    // at the moment we do not check the image content
    @HtmlUnitNYI(CHROME = {"error2;error3;load4;load5;", "3"},
            EDGE = {"error2;error3;load4;load5;", "3"},
            FF = {"error2;load3;load4;load5;", "4"},
            FF78 = {"error2;load3;load4;load5;", "4"},
            IE = {"error2;error3;load4;load5;", "3"})
    public void onload() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);

            final URL urlImage = new URL(URL_SECOND, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
            getMockWebConnection().setResponse(URL_SECOND, "Test", 200, "OK", MimeType.TEXT_HTML, emptyList);
            getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        }

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfo(text) {\n"
            + "    document.title += text + ';';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <img id='myImage1' onload='showInfo(\"load1\")' onerror='showInfo(\"error1\")'>\n"
            + "  <img id='myImage2' src='' onload='showInfo(\"load2\")' onerror='showInfo(\"error2\")'>\n"
            + "  <img id='myImage3' src='  ' onload='showInfo(\"load3\")' onerror='showInfo(\"error3\")'>\n"
            + "  <img id='myImage4' src='" + URL_SECOND + "' onload='showInfo(\"load4\")' "
                    + "onerror='showInfo(\"error4\")'>\n"
            + "  <img id='myImage5' src='" + URL_SECOND + "img.jpg' onload='showInfo(\"load5\")' "
                    + "onerror='showInfo(\"error5\")'>\n"
            + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load;", "2"})
    public void emptyMimeType() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);

            final URL urlImage = new URL(URL_SECOND, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "", emptyList);
            getMockWebConnection().setDefaultResponse("Test");
        }

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfo(text) {\n"
            + "    document.title += text + ';';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <img id='myImage5' src='" + URL_SECOND + "img.jpg' onload='showInfo(\"load\")' "
                    + "onerror='showInfo(\"error\")'>\n"
            + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load;", "2"})
    public void wrongMimeType() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);

            final URL urlImage = new URL(URL_SECOND, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "text/html", emptyList);
            getMockWebConnection().setDefaultResponse("Test");
        }

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfo(text) {\n"
            + "    document.title += text + ';';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <img id='myImage5' src='" + URL_SECOND + "img.jpg' onload='showInfo(\"load\")' "
                    + "onerror='showInfo(\"error\")'>\n"
            + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"test", "string", "hui", "", "null", "false", "true", ""})
    public void alt() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var testImg = document.getElementById('myImage');\n"
            + "        alert(testImg.alt);\n"
            + "        alert(typeof testImg.alt);\n"

            + "        testImg.alt = 'hui';\n"
            + "        alert(testImg.alt);\n"

            + "        testImg.alt = '';\n"
            + "        alert(testImg.alt);\n"

            + "        testImg.alt = null;\n"
            + "        alert(testImg.alt);\n"
            + "        alert(testImg.alt === null);\n"
            + "        alert(testImg.alt === 'null');\n"

            + "        var testImg = document.getElementById('myImageWithoutAlt');\n"
            + "        alert(testImg.alt);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <img id='myImage' src='" + URL_SECOND + "' alt='test'>\n"
            + "    <img id='myImageWithoutAlt' src='" + URL_SECOND + "'>\n"
            + "  </body>\n"
            + "</html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myImage clicked", "myImageNone clicked"})
    public void click() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    document.getElementById('myImage').click();\n"
            + "    document.getElementById('myImageNone').click();\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage' src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                                    + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='"
                            + " onclick='alert(\"myImage clicked\");'>\n"
            + "  <img id='myImageNone' src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                                    + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='"
                            + " style='display: none' onclick='alert(\"myImageNone clicked\");'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "myImageWithMap clicked",
            IE = "a0 clicked")
    @NotYetImplemented(IE)
    public void clickWithMap() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    document.getElementById('myImageWithMap').click();\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImageWithMap' usemap='#dot'"
                                + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                                + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='"
                            + " onclick='alert(\"myImageWithMap clicked\");'>\n"
                + "  <map name='dot'>\n"
                + "    <area id='a0' shape='rect' coords='0 0 7 7' onclick='alert(\"a0 clicked\");'/>\n"
                + "    <area id='a1' shape='rect' coords='0,0,1,1' onclick='alert(\"a1 clicked\");'/>\n"
                + "  <map>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"done;", "2"})
    public void img_download() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><body>\n"
            + "<script>\n"
            + "  var i = new Image();\n"
            + "  i.src = 'img.jpg';\n"
            + "  document.title += 'done;';\n"
            + "</script></body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);
        assertEquals(URL_FIRST + "img.jpg", getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"done;onload;", "2"})
    public void img_download_onloadBefore() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><body>\n"
            + "<script>\n"
            + "  var i = new Image();\n"
            + "  i.onload = function() { document.title += 'onload;'; };\n"
            + "  i.src = 'img.jpg';\n"
            + "  document.title += 'done;';\n"
            + "</script></body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);
        assertEquals(URL_FIRST + "img.jpg", getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"done;onload;", "2"})
    public void img_download_onloadAfter() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><body>\n"
            + "<script>\n"
            + "  var i = new Image();\n"
            + "  i.src = 'img.jpg';\n"
            + "  document.title += 'done;';\n"
            + "  i.onload = function() { document.title += 'onload;'; };\n"
            + "</script></body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);
        assertEquals(URL_FIRST + "img.jpg", getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute, it gets downloaded
     * and the <tt>onload</tt> handler gets invoked.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"done;onload;", "2"})
    public void img_onLoad_calledWhenImageDownloaded_dynamic() throws Exception {
        // this seems to need a fresh browser to pass
        shutDownAll();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);

            URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);

            urlImage = new URL(URL_FIRST, "img2.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><body>\n"
            + "<script>\n"
            + "  var i = new Image();\n"
            + "  i.src = 'img.jpg';\n"
            + "  i.src = 'img2.jpg';\n"
            + "  document.title += 'done;';\n"
            + "  i.onload = function() { document.title += 'onload;'; };\n"
            + "</script></body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);
        assertEquals(URL_FIRST + "img2.jpg", getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute, it gets downloaded
     * and the <tt>onload</tt> handler gets invoked.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"done;onload;", "2"})
    public void img_onLoad_calledWhenImageDownloaded_dynamic2() throws Exception {
        // this seems to need a fresh browser to pass
        shutDownAll();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);

            URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);

            urlImage = new URL(URL_FIRST, "img2.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><body>\n"
            + "<script>\n"
            + "  var i = new Image();\n"
            + "  i.onload = function() { document.title += 'onload;'; };\n"
            + "  i.src = 'img.jpg';\n"
            + "  i.src = 'img2.jpg';\n"
            + "  document.title += 'done;';\n"
            + "</script></body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);
        assertEquals(URL_FIRST + "img2.jpg", getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * Verifies that if an image is created if the page is already
     * finished, the onload handler is called.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"done;onload;onload2;", "3"})
    public void img_onLoad_calledWhenImageDownloaded_dynamic_twoSteps() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);

            URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);

            urlImage = new URL(URL_FIRST, "img2.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><body>\n"
                + "<script>\n"
                + "  var i = new Image();\n"
                + "  i.src = 'img.jpg';\n"
                + "  i.onload = function() {\n"
                + "    document.title += 'onload;';\n"
                + "    var i2 = document.createElement('img');\n"
                + "    i2.src = 'img2.jpg';\n"
                + "    i2.onload = function() {\n"
                + "      document.title += 'onload2;';\n"
                + "    };\n"
                + "  };\n"
                + "  document.title += 'done;';\n"
                + "</script></body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);
        assertEquals(URL_FIRST + "img2.jpg", getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute set from a script, it gets downloaded
     * and the <tt>onload</tt> handler gets invoked.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"image one;image two;", "3"})
    public void img_onLoad_calledWhenImageDownloaded_mixed() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);

            URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);

            urlImage = new URL(URL_FIRST, "img2.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html
                = "<html><body><img id='img' name='img'/><script>\n"
                + "  var i = new Image();\n"
                + "  i.onload = function() {\n"
                + "    document.title += 'image one;';\n"
                + "    i.onload = function() {\n"
                + "      document.title += 'image two;';\n"
                + "    };\n"
                + "    i.src = 'img2.jpg';\n"
                + "  };\n"
                + "  i.setAttribute('src','img.jpg');\n"
                + "  var t = setTimeout(function() {clearTimeout(t);}, 500);\n"
                + "</script></body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);

        final List<String> requestedUrls = getMockWebConnection().getRequestedUrls(URL_FIRST);
        assertEquals("", requestedUrls.get(0));
        assertEquals("img.jpg", requestedUrls.get(1));
        assertEquals("img2.jpg", requestedUrls.get(2));
    }
}
