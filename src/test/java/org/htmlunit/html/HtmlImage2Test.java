/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import static org.htmlunit.junit.annotation.TestedBrowser.CHROME;
import static org.htmlunit.junit.annotation.TestedBrowser.EDGE;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.http.HttpStatus;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.NotYetImplemented;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * Tests for {@link HtmlImage}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlImage2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageWithoutSource() throws Exception {
        loadImage("");
        loadImageInnerHtml("");
        loadImageImportNodeHtml("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageEmptySource() throws Exception {
        loadImage("src=''");
        loadImageInnerHtml("src=''");
        loadImageImportNodeHtml("src=''");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF = "2",
            FF_ESR = "2")
    public void loadImageBlankSource() throws Exception {
        loadImage("src=' '");
        loadImageInnerHtml("src=' '");
        loadImageImportNodeHtml("src=' '");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void loadImage() throws Exception {
        loadImage("src='img.jpg'");
        loadImageInnerHtml("src='img.jpg'");
        loadImageImportNodeHtml("src='img.jpg'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void loadImageUnknown() throws Exception {
        loadImage("src='unknown'");
        loadImageInnerHtml("src='unknown'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            CHROME = "1",
            EDGE = "1")
    @NotYetImplemented({CHROME, EDGE})
    public void loadImageUnknown2() throws Exception {
        loadImageImportNodeHtml("src='unknown'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageBrokenUrl() throws Exception {
        loadImage("src='rbri://nowhere'");
        loadImageInnerHtml("src='rbri://nowhere'");
        loadImageImportNodeHtml("src='rbri://nowhere'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageAboutBlank() throws Exception {
        loadImage("src='about:blank'");
        loadImageInnerHtml("src='about:blank'");
        loadImageImportNodeHtml("src='about:blank'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageAboutX() throws Exception {
        loadImage("src='about:x'");
        loadImageInnerHtml("src='about:x'");
        loadImageImportNodeHtml("src='about:x'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void loadImageWrongType() throws Exception {
        loadImage("src='" + URL_FIRST + "'");
        loadImageInnerHtml("src='" + URL_FIRST + "'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            CHROME = "1",
            EDGE = "1")
    @NotYetImplemented({CHROME, EDGE})
    public void loadImageWrongType2() throws Exception {
        loadImageImportNodeHtml("src='" + URL_FIRST + "'");
    }

    private void loadImage(final String src) throws Exception {
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var img = document.getElementById('myImage');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage' " + src + " >\n"
            + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);
        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount() - count);
    }

    private void loadImageInnerHtml(final String src) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var tester = document.getElementById('tester');\n"
            + "    tester.innerHTML = \"<img id='myImage' " + src + " >\";\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <button id='test' onclick='test()'>Test</button>\n"
            + "  <div id='tester'></div>\n"
            + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        driver.findElement(By.id("test")).click();
        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount() - count);
    }

    private void loadImageImportNodeHtml(final String src) throws Exception {
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var tester = document.getElementById('tester');\n"

            + "    var doc = document.implementation.createHTMLDocument('test');\n"
            + "    doc.body.innerHTML = \"<img id='myImage' " + src + " >\";\n"

            + "    var srcNode = doc.getElementById('myImage');\n"
            + "    var newNode = document.importNode(srcNode, true);\n"
            + "    document.body.replaceChild(newNode, tester);\n"
            + "    alert('before');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <button id='test' onclick='test()'>Test</button>\n"
            + "  <div id='tester'></div>\n"
            + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        driver.findElement(By.id("test")).click();
        verifyAlerts(driver, "before");

        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount() - count);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void isDisplayed() throws Exception {
        isDisplayed("src='img.jpg'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    public void isDisplayedNoSource() throws Exception {
        isDisplayed("");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    public void isDisplayedEmptySource() throws Exception {
        isDisplayed("src=''");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false",
            EDGE = "false")
    public void isDisplayedBlankSource() throws Exception {
        isDisplayed("src=' '");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void isDisplayedInvalidSource() throws Exception {
        isDisplayed("src='unknown.gif'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void isDisplayedWrongType() throws Exception {
        isDisplayed("src='" + URL_FIRST + "'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    public void isDisplayedDisplayNone() throws Exception {
        isDisplayed("src='img.jpg' style='display: none'");
    }

    private void isDisplayed(final String src) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", Collections.emptyList());

            getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        }

        final String html = DOCTYPE_HTML
                + "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <img id='myImg' " + src + " >\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final boolean displayed = driver.findElement(By.id("myImg")).isDisplayed();
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[0]), displayed);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§img.gif")
    public void src() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var img = document.getElementById('myImg');\n"
            + "    log(img.src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <img id='myImg' src='img.gif'>\n"
            + "</body>\n"
            + "</html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "§§URL§§abcd/img.gif"})
    public void lineBreaksInUrl() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-gif.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_SECOND, "abcd/img.gif");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok",
                                                MimeType.IMAGE_GIF, Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var img = document.getElementById('myImg');\n"
            + "    img.width;\n" // this forces image loading in htmlunit
            + "    log(img.width);\n"
            + "    log(img.src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <img id='myImg' src='" + URL_SECOND + "a\rb\nc\r\nd/img.gif' onError='log(\"broken\");'>\n"
            + "</body>\n"
            + "</html>";

        expandExpectedAlertsVariables(URL_SECOND);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"58", "29", "58", "29"},
            FF = {"58", "29", "70", "118"},
            FF_ESR = {"58", "29", "68", "118"})
    @NotYetImplemented
    public void clickWithCoordinates() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-gif.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_SECOND, "img.gif");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok",
                                        MimeType.IMAGE_GIF, Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function clickImage(event) {\n"
            + "    log(event.clientX);\n"
            + "    log(event.clientY);\n"
            + "    log(event.screenX);\n"
            + "    log(event.screenY);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <img id='myImg' src='" + URL_SECOND + "img.gif' "
                    + "width='100px' height='42px' onclick='clickImage(event)'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final Actions actions = new Actions(driver);
        // this requires a webdriver change
        actions.moveToElement(driver.findElement(By.id("myImg")), 0, 0).click().build().perform();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_NoContent() throws Exception {
        addEventListener(HttpStatus.NO_CONTENT_204);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_BadRequest() throws Exception {
        addEventListener(HttpStatus.BAD_REQUEST_400);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_Forbidden() throws Exception {
        addEventListener(HttpStatus.FORBIDDEN_403);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_NotFound() throws Exception {
        addEventListener(HttpStatus.NOT_FOUND_404);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_MethodNotAllowed() throws Exception {
        addEventListener(HttpStatus.METHOD_NOT_ALLOWED_405);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_NotAcceptable() throws Exception {
        addEventListener(HttpStatus.NOT_ACCEPTABLE_406);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_ProxyAuthRequired() throws Exception {
        addEventListener(HttpStatus.PROXY_AUTHENTICATION_REQUIRED_407);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_RequestTimeout() throws Exception {
        addEventListener(HttpStatus.REQUEST_TIMEOUT_408);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_Conflict() throws Exception {
        addEventListener(HttpStatus.CONFLICT_409);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_Gone() throws Exception {
        addEventListener(HttpStatus.GONE_410);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_LengthRequired() throws Exception {
        addEventListener(HttpStatus.LENGTH_REQUIRED_411);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_PreconditionFailed() throws Exception {
        addEventListener(HttpStatus.PRECONDITION_FAILED_412);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_PayloadTooLarge() throws Exception {
        addEventListener(HttpStatus.PAYLOAD_TOO_LARGE_413);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_UriTooLong() throws Exception {
        addEventListener(HttpStatus.URI_TOO_LONG_414);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_UnsupportedMediaType() throws Exception {
        addEventListener(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_RangeNotSatisfiable() throws Exception {
        addEventListener(HttpStatus.RANGE_NOT_SATISFIABLE_416);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_ExpectationFailed() throws Exception {
        addEventListener(HttpStatus.EXPECTATION_FAILED_417);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_ImaTeapot() throws Exception {
        addEventListener(HttpStatus.IM_A_TEAPOT_418);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_EnhanceYourCalm() throws Exception {
        addEventListener(HttpStatus.ENHANCE_YOUR_CALM_420);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_MisdirectedRequest() throws Exception {
        addEventListener(HttpStatus.MISDIRECTED_REQUEST_421);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_UnprocessableEntity() throws Exception {
        addEventListener(HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_Locked() throws Exception {
        addEventListener(HttpStatus.LOCKED_423);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_FailedDependency() throws Exception {
        addEventListener(HttpStatus.FAILED_DEPENDENCY_424);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_UpgradeRequired() throws Exception {
        addEventListener(HttpStatus.UPGRADE_REQUIRED_426);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_PreconditionRequired() throws Exception {
        addEventListener(HttpStatus.PRECONDITION_REQUIRED_428);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_TooManyRedirects() throws Exception {
        addEventListener(HttpStatus.TOO_MANY_REQUESTS_429);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_RequestHeaderFieldsTooLarge() throws Exception {
        addEventListener(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE_431);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_UnavailableForLegalReasons() throws Exception {
        addEventListener(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS_451);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_InternalServerError() throws Exception {
        addEventListener(HttpStatus.INTERNAL_SERVER_ERROR_500);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_NotImplemented() throws Exception {
        addEventListener(HttpStatus.NOT_IMPLEMENTED_501);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_BadGateway() throws Exception {
        addEventListener(HttpStatus.BAD_GATEWAY_502);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_ServiceUnavailable() throws Exception {
        addEventListener(HttpStatus.SERVICE_UNAVAILABLE_503);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_GatewayTimeout() throws Exception {
        addEventListener(HttpStatus.GATEWAY_TIMEOUT_504);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_HttpVersionNotSupported() throws Exception {
        addEventListener(HttpStatus.HTTP_VERSION_NOT_SUPPORTED_505);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_InsufficientStrorage() throws Exception {
        addEventListener(HttpStatus.INSUFFICIENT_STORAGE_507);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_LoopDetected() throws Exception {
        addEventListener(HttpStatus.LOOP_DETECTED_508);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_NotExtended() throws Exception {
        addEventListener(HttpStatus.NOT_EXTENDED_510);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("error [object HTMLImageElement]")
    public void addEventListener_NetworkAuthenticationRequired() throws Exception {
        addEventListener(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED_511);
    }

    private void addEventListener(final int statusCode) throws Exception {
        // use always a different url to avoid caching effects
        final URL scriptUrl = new URL(URL_SECOND, "" + System.currentTimeMillis() + ".png");

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var s1 = document.createElement('img');\n"
            + "    s1.src = '" + scriptUrl + "';\n"
            + "    s1.addEventListener('load', function() { log('load'); }, false);\n"
            + "    s1.addEventListener('error', function(event) { log(event.type + ' ' + event.target); }, false);\n"
            + "    document.body.insertBefore(s1, document.body.firstChild);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(scriptUrl, (String) null,
                statusCode, "test", MimeType.TEXT_JAVASCRIPT, null);
        loadPageVerifyTitle2(html);
    }
}
