/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.html;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.htmlunit.Page;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HtmlImage}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlImageTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isMapClick() throws Exception {
        isMapClick("img1", Boolean.FALSE, "?0,0", "?25,30");
        isMapClick("img2", Boolean.FALSE, "", "");
        isMapClick("img3", Boolean.TRUE, "", "");
        isMapClick("img3", Boolean.TRUE, "", "");
    }

    private void isMapClick(final String imgId, final Boolean samePage,
            final String urlSuffixClick, final String urlSuffixClickXY) throws Exception {

        final URL urlImage = new URL(URL_FIRST, "img.jpg");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String htmlContent
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <a href='http://server/foo'>\n"
            + "    <img id='img1' src='" + urlImage + "' ismap>\n"
            + "    <img id='img2' src='" + urlImage + "'>\n"
            + "  </a>\n"
            + "  <img id='img3' src='" + urlImage + "' ismap>\n"
            + "  <img id='img4' src='" + urlImage + "'>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlImage img = page.getHtmlElementById(imgId);

        final Page page2 = img.click();
        assertEquals("same page after click", samePage, Boolean.valueOf(page == page2));
        if (!samePage.booleanValue()) {
            assertEquals("http://server/foo" + urlSuffixClick, page2.getUrl());
        }

        final Page page3 = img.click(25, 30);
        assertEquals("same page after click(25, 30)", samePage, Boolean.valueOf(page == page3));
        if (!samePage.booleanValue()) {
            assertEquals("http://server/foo" + urlSuffixClickXY, page3.getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void useMapClick() throws Exception {
        useMapClick(0, 0, "/");
        useMapClick(10, 10, "a.html");
        useMapClick(19, 10, "a.html");
        useMapClick(29, 10, "b.html");
        useMapClick(50, 50, "/");
    }

    /**
     * @throws Exception if the test fails
     */
    private void useMapClick(final int x, final int y, final String urlSuffix) throws Exception {
        final URL urlImage = new URL(URL_FIRST, "img.jpg");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String htmlContent
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <img id='myImg' src='" + urlImage + "' usemap='#map1'>\n"
            + "  <map name='map1'>\n"
            + "    <area href='a.html' shape='rect' coords='5,5,20,20'>\n"
            + "    <area href='b.html' shape='circle' coords='25,10,10'>\n"
            + "  </map>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlImage img = page.getHtmlElementById("myImg");

        final Page page2 = img.click(x, y);
        final URL url = page2.getUrl();
        assertTrue(url.toExternalForm(), url.toExternalForm().endsWith(urlSuffix));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<img id='img1' src='foo.png'>\n"
            + "<img id='img2' name='testName' src='foo.png' alt='young'>\n"
            + "<img id='img3' src='foo.png' width='11' height='17px'>\n"
            + "<img id='img4' src='foo.png' width='11em' height='17%'>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(content);

        HtmlImage img = page.getHtmlElementById("img1");
        String expected = "<img id=\"img1\" src=\"foo.png\"/>";
        assertEquals(expected, img.asXml().trim());

        img = page.getHtmlElementById("img2");
        expected = "<img id=\"img2\" name=\"testName\" src=\"foo.png\" alt=\"young\"/>";
        assertEquals(expected, img.asXml().trim());

        img = page.getHtmlElementById("img3");
        expected = "<img id=\"img3\" src=\"foo.png\" width=\"11\" height=\"17px\"/>";
        assertEquals(expected, img.asXml().trim());

        img = page.getHtmlElementById("img4");
        expected = "<img id=\"img4\" src=\"foo.png\" width=\"11em\" height=\"17%\"/>";
        assertEquals(expected, img.asXml().trim());
    }

    /**
     * Test case for issue #1833.
     * Simply save the image without any parsing.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void saveAsNotSupportedImageType() throws Exception {
        try (InputStream is = getClass().getClassLoader().
                getResourceAsStream("testfiles/not_supported_type.jpg")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <img id='myImage' src='img.jpg' >\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final HtmlImage img = page.getHtmlElementById("myImage");
        final File tempFile = File.createTempFile("img", ".tmp");
        img.saveAs(tempFile);
        FileUtils.deleteQuietly(tempFile);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void determineWidthHeightFromImage() throws Exception {
        try (InputStream is = getClass().getClassLoader().
                getResourceAsStream("testfiles/4x7.jpg")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "4x7.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }
        try (InputStream is = getClass().getClassLoader().
                getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <img id='myImage' src='4x7.jpg' >\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final HtmlImage img = page.getHtmlElementById("myImage");
        assertEquals(4, img.getWidth());
        assertEquals(7, img.getHeight());

        // source change has to force new determination
        img.setAttribute("src", "img.jpg");
        assertEquals(1, img.getWidth());
        assertEquals(1, img.getHeight());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"16", "16", "2"},
            FF = {"24", "24", "2"},
            FF_ESR = {"24", "24", "2"})
    public void retrieveImagePerDefault() throws Exception {
        final String html =
                "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <img id='myImage' src='4x7.jpg' >\n"
                + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();

        final HtmlPage page = loadPage(html);
        final HtmlImage img = page.getHtmlElementById("myImage");
        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), img.getWidthOrDefault());
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), img.getHeightOrDefault());

        assertEquals(Integer.parseInt(getExpectedAlerts()[2]), getMockWebConnection().getRequestCount() - count);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"4x7.jpg", "§§URL§§4x7.jpg"})
    public void src() throws Exception {
        final String html =
                "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <img id='myImage' src='4x7.jpg' >\n"
                + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlImage img = page.getHtmlElementById("myImage");

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], img.getSrcAttribute());
        assertEquals(getExpectedAlerts()[1], img.getSrc());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"8-8-18-18-", "10-15-18-18-"})
    public void clickWithCoordinates() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-gif.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_SECOND, "img.gif");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", MimeType.IMAGE_GIF, emptyList);
        }

        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function log(msg) { window.document.title += msg + '-';}\n"
            + "  function clickImage(event) {\n"
            + "    log(event.clientX);\n"
            + "    log(event.clientY);\n"
            + "    log(event.screenX);\n"
            + "    log(event.screenY);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <img id='myImage' src='" + URL_SECOND + "img.gif' "
                    + "width='100px' height='42px' onclick='clickImage(event)'>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        final HtmlImage img = page.getHtmlElementById("myImage");

        img.click();
        assertEquals(getExpectedAlerts()[0], page.getTitleText());

        img.click(2, 7);
        assertEquals(getExpectedAlerts()[0] + getExpectedAlerts()[1], page.getTitleText());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"in-out-Image.onload(0)-", "mousedown-in-out-Image.onload(1)-mouseup-in-out-click-in-out-Image.onload(2)-Image.onload(3)-"})
    public void onload() throws Exception {
        final String html =
                "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function log(msg) { window.document.title += msg + '-';}\n"
                + "  function test(i) {\n"
                + "    log('in');\n"
                + "    var image = new Image();\n"
                + "    image.onload = function () { log(\"Image.onload(\" + i + \")\") };\n"
                + "    image.src = '4x7.jpg';\n"
                + "    log('out');\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload=\"test(0)\">\n"
                + "<button onmousedown=\"log('mousedown'); test(1)\""
                + "        onmouseup=\"log('mouseup'); test(2)\""
                + "        onclick=\"log('click'); test(3)\"></button>\n"
                + "</body>\n"
                + "</html>\n";

        final HtmlPage page = loadPage(html);
        assertEquals(getExpectedAlerts()[0], page.getTitleText());

        page.setTitleText("");
        page.<HtmlButton>getFirstByXPath("//button").click();
        assertEquals(getExpectedAlerts()[1], page.getTitleText());
    }
}
