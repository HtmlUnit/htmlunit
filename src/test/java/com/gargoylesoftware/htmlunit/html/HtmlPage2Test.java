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
package com.gargoylesoftware.htmlunit.html;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlPage}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlPage2Test extends SimpleWebTestCase {

    /**
     * Utility for temporary folders.
     * Has to be public due to JUnit's constraints for @Rule.
     */
    @Rule
    public final TemporaryFolder tmpFolderProvider_ = new TemporaryFolder();

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getFullQualifiedUrl_topWindow() throws Exception {
        final String firstHtml = "<html><head><title>first</title>\n"
            + "<script>\n"
            + "function init() {\n"
            + "  var iframe = window.frames['f'];\n"
            + "  iframe.document.write(\"<form name='form' action='" + URL_SECOND + "'>"
            + "<input name='submit' type='submit'></form>\");\n"
            + "  iframe.document.close();\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "  <iframe name='f'></iframe>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>second</title></head>\n"
            + "<body><p>Form submitted successfully.</p></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setDefaultResponse(secondHtml);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);

        HtmlPage framePage = (HtmlPage) page.getFrameByName("f").getEnclosedPage();
        final HtmlForm form = framePage.getFormByName("form");
        final HtmlInput submit = form.getInputByName("submit");
        framePage = submit.click();
        assertEquals("Form submitted successfully.", framePage.getBody().asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello there")
    public void save() throws Exception {
        final String html = "<html><head><script src='" + URL_SECOND + "'>\n</script></head></html>";

        final String js = "alert('Hello there')";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, js);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);

        final HtmlScript sript = page.getFirstByXPath("//script");
        assertEquals(URL_SECOND.toString(), sript.getSrcAttribute());

        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_HtmlPageTest_save.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
        final String content = FileUtils.readFileToString(file, ISO_8859_1);
        assertFalse(content.contains("<script"));

        assertEquals(URL_SECOND.toString(), sript.getSrcAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void save_image() throws Exception {
        final String html = "<html><body><img src='" + URL_SECOND + "'></body></html>";

        final URL url = getClass().getClassLoader().getResource("testfiles/tiny-jpg.img");
        final WebClient webClient = getWebClientWithMockWebConnection();
        try (FileInputStream fis = new FileInputStream(new File(url.toURI()))) {
            final byte[] directBytes = IOUtils.toByteArray(fis);
            final MockWebConnection webConnection = getMockWebConnection();

            webConnection.setResponse(URL_FIRST, html);
            final List<NameValuePair> emptyList = Collections.emptyList();
            webConnection.setResponse(URL_SECOND, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlImage img = page.getFirstByXPath("//img");
        assertEquals(URL_SECOND.toString(), img.getSrcAttribute());
        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_HtmlPageTest_save2.html");
        final File imgFile = new File(tmpFolder, "hu_HtmlPageTest_save2/second.jpeg");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
        final byte[] loadedBytes = FileUtils.readFileToByteArray(imgFile);
        assertTrue(loadedBytes.length > 0);
        assertEquals(URL_SECOND.toString(), img.getSrcAttribute());
    }

    /**
     * As of 24.05.2011 an IOException was occurring when saving a page where
     * the response to the request for an image was not an image.
     * @throws Exception if the test fails
     */
    @Test
    public void save_imageNotImage() throws Exception {
        final String html = "<html><body><img src='foo.txt'></body></html>";

        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setDefaultResponse("hello", MimeType.TEXT_PLAIN);

        final HtmlPage page = loadPageWithAlerts(html);

        final File folder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(folder, "hu_save.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        final File imgFile = new File(folder, "hu_save/foo.txt");
        assertEquals("hello", FileUtils.readFileToString(imgFile, UTF_8));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void save_image_without_src() throws Exception {
        final String html = "<html><body><img></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_FIRST, html);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_HtmlPageTest_save3.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        final HtmlImage img = page.getFirstByXPath("//img");
        assertEquals(DomElement.ATTRIBUTE_NOT_DEFINED, img.getSrcAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void save_image_empty_src() throws Exception {
        final String html = "<html><body><img src=''></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_FIRST, html);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_HtmlPageTest_save3.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        final HtmlImage img = page.getFirstByXPath("//img");
        assertEquals(DomElement.ATTRIBUTE_NOT_DEFINED, img.getSrcAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void save_frames() throws Exception {
        final String mainContent
            = "<html><head><title>First</title></head>\n"
            + "<frameset cols='50%,*'>\n"
            + "  <frame name='left' src='" + URL_SECOND + "' frameborder='1' />\n"
            + "  <frame name='right' src='" + URL_THIRD + "' frameborder='1' />\n"
            + "  <frame name='withoutsrc' />\n"
            + "</frameset>\n"
            + "</html>";
        final String frameLeftContent = "<html><head><title>Second</title></head><body>\n"
            + "<iframe src='iframe.html'></iframe>\n"
            + "<img src='img.jpg'>\n"
            + "</body></html>";
        final String frameRightContent = "<html><head><title>Third</title></head><body>frame right</body></html>";
        final String iframeContent  = "<html><head><title>Iframe</title></head><body>iframe</body></html>";

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);

            final MockWebConnection webConnection = getMockWebConnection();
            webConnection.setResponse(URL_FIRST, mainContent);
            webConnection.setResponse(URL_SECOND, frameLeftContent);
            webConnection.setResponse(URL_THIRD, frameRightContent);
            final URL urlIframe = new URL(URL_SECOND, "iframe.html");
            webConnection.setResponse(urlIframe, iframeContent);

            final List<NameValuePair> emptyList = Collections.emptyList();
            final URL urlImage = new URL(URL_SECOND, "img.jpg");
            webConnection.setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final WebClient webClient = getWebClientWithMockWebConnection();
        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlFrame leftFrame = page.getElementByName("left");
        assertEquals(URL_SECOND.toString(), leftFrame.getSrcAttribute());
        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_HtmlPageTest_saveFrame.html");
        final File expectedLeftFrameFile = new File(tmpFolder, "hu_HtmlPageTest_saveFrame/second.html");
        final File expectedRightFrameFile = new File(tmpFolder, "hu_HtmlPageTest_saveFrame/third.html");
        final File expectedIFrameFile = new File(tmpFolder, "hu_HtmlPageTest_saveFrame/second/iframe.html");
        final File expectedImgFile = new File(tmpFolder, "hu_HtmlPageTest_saveFrame/second/img.jpg");
        final File[] allFiles = {file, expectedLeftFrameFile, expectedImgFile, expectedIFrameFile,
            expectedRightFrameFile};

        page.save(file);
        for (final File f : allFiles) {
            assertTrue(f.toString(), f.exists());
            assertTrue(f.toString(), f.isFile());
        }

        final byte[] loadedBytes = FileUtils.readFileToByteArray(expectedImgFile);
        assertTrue(loadedBytes.length > 0);

        // ensure that saving the page hasn't changed the DOM
        assertEquals(URL_SECOND.toString(), leftFrame.getSrcAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void save_css() throws Exception {
        final String html = "<html><head>\n"
            + "<link rel='stylesheet' type='text/css' href='" + URL_SECOND + "'/></head></html>";

        final String css = "body {color: blue}";

        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, css);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlLink cssLink = page.getFirstByXPath("//link");
        assertEquals(URL_SECOND.toString(), cssLink.getHrefAttribute());

        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_HtmlPageTest_save4.html");
        final File cssFile = new File(tmpFolder, "hu_HtmlPageTest_save4/second.css");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
        assertEquals(css, FileUtils.readFileToString(cssFile, ISO_8859_1));

        assertEquals(URL_SECOND.toString(), cssLink.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void save_css_without_href() throws Exception {
        final String html = "<html><head>\n"
            + "<link rel='stylesheet' type='text/css' /></head></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_FIRST, html);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_HtmlPageTest_save5.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        final HtmlLink cssLink = page.getFirstByXPath("//link");
        assertEquals(DomElement.ATTRIBUTE_NOT_DEFINED, cssLink.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void save_css_empty_href() throws Exception {
        final String html = "<html><head>\n"
            + "<link rel='stylesheet' type='text/css' href='' /></head></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        final MockWebConnection webConnection = getMockWebConnection();

        webConnection.setResponse(URL_FIRST, html);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_HtmlPageTest_save5.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        final HtmlLink cssLink = page.getFirstByXPath("//link");
        assertEquals(DomElement.ATTRIBUTE_NOT_DEFINED, cssLink.getHrefAttribute());
    }

    /**
     * This was producing java.io.IOException: File name too long as of HtmlUnit-2.9.
     * Many file systems have a limit 255 byte for file names.
     * @throws Exception if the test fails
     */
    @Test
    public void saveShouldStripLongFileNames() throws Exception {
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        final String longName = generator.generate(500) + ".html";
        final String html = "<html><body><iframe src='" + longName + "'></iframe></body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse("<html/>");
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);

        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_HtmlPageTest_save.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void serialization_attributeListenerLock() throws Exception {
        final String html = "<html><head><script>\n"
            + "function foo() {\n"
            + "  document.getElementById('aframe').src = '" + URL_FIRST + "';\n"
            + "  return false;\n"
            + "}</script>\n"
            + "<body><iframe src='about:blank' id='aframe'></iframe>\n"
            + "<a href='#' onclick='foo()' id='link'>load iframe</a></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final WebClient copy = clone(page.getWebClient());
        final HtmlPage copyPage = (HtmlPage) copy.getCurrentWindow().getTopWindow().getEnclosedPage();
        copyPage.getHtmlElementById("link").click();
        assertEquals(URL_FIRST.toExternalForm(), copyPage.getElementById("aframe").getAttribute("src"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void save_emptyTextArea() throws Exception {
        final String html = "<html>\n"
            + "<head/>\n"
            + "<body>\n"
            + "<textarea></textarea>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        final File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
        final File file = new File(tmpFolder, "hu_HtmlPage2Test_save_emptyTextArea.html");
        try {
            page.save(file);
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertTrue(page.asXml().contains("</textarea>"));
            assertTrue(FileUtils.readFileToString(file, ISO_8859_1).contains("</textarea>"));
        }
        finally {
            assertTrue(file.delete());
        }
    }
}
