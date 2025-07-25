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

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.htmlunit.HttpHeader;
import org.htmlunit.Page;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlArea}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlAreaTest extends WebDriverTestCase {

    private WebDriver createWebClient(final String onClick) throws Exception {
        final URL urlImage = new URL(URL_FIRST, "img.jpg");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", Collections.emptyList());
        }

        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>first</title></head>\n"
            + "<body>\n"
            + "  <img src='" + urlImage + "' width='145' height='126' usemap='#planetmap'>\n"
            + "  <map id='planetmap' name='planetmap'>\n"
            + "    <area shape='rect' onClick=\"" + onClick + "\" coords='0,0,82,126' id='second' "
                        + "href='" + URL_SECOND + "'>\n"
            + "    <area shape='circle' coords='90,58,3' id='third' href='" + URL_THIRD + "'>\n"
            + "  </map>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>second</title></head><body></body></html>";
        final String thirdContent = DOCTYPE_HTML + "<html><head><title>third</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent);

        return loadPage2(firstContent);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    @BuggyWebDriver(FF = "WebDriverException",
                    FF_ESR = "WebDriverException")
    public void referer() throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = createWebClient("");
        driver.get(URL_FIRST.toExternalForm());
        try {
            driver.findElement(By.id("third")).click();

            final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
            assertEquals(getExpectedAlerts()[0], lastAdditionalHeaders.get(HttpHeader.REFERER));
        }
        catch (final WebDriverException e) {
            e.printStackTrace();
            assertEquals(getExpectedAlerts()[0], "WebDriverException");
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayedRect() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <img id='myImg' usemap='#imgmap'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea' shape='rect' coords='0,0,1,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        boolean displayed = driver.findElement(By.id("myImg")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myMap")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myArea")).isDisplayed();
        assertTrue(displayed);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayedCircle() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <img id='myImg' usemap='#imgmap'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea' shape='circle' coords='0,0,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        boolean displayed = driver.findElement(By.id("myImg")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myMap")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myArea")).isDisplayed();
        assertTrue(displayed);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayedPolygon() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <img id='myImg' usemap='#imgmap'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea' shape='poly' coords='7,9,5,1,2,2'>\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        boolean displayed = driver.findElement(By.id("myImg")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myMap")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myArea")).isDisplayed();
        assertTrue(displayed);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayedHiddenImage() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <img id='myImg' usemap='#imgmap' style='display: none'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea' shape='rect' coords='0,0,1,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        boolean displayed = driver.findElement(By.id("myImg")).isDisplayed();
        assertFalse(displayed);

        displayed = driver.findElement(By.id("myMap")).isDisplayed();
        assertFalse(displayed);

        displayed = driver.findElement(By.id("myArea")).isDisplayed();
        assertFalse(displayed);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayedHiddenMap() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <img id='myImg' usemap='#imgmap'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap' style='display: none'>\n"
                + "    <area id='myArea' shape='rect' coords='0,0,1,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        boolean displayed = driver.findElement(By.id("myImg")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myMap")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myArea")).isDisplayed();
        assertTrue(displayed);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "false", "false", "false", "true"})
    public void isDisplayedEmptyRect() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <img id='myImg' usemap='#imgmap'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea1' shape='rect' coords='0,0,0,1'>\n"
                + "    <area id='myArea2' shape='rect' coords='0,0,1,0'>\n"
                + "    <area id='myArea3' shape='rect' coords='0,0,0,0'>\n"
                + "    <area id='myArea4' shape='rect' >\n"
                + "    <area id='myArea5' >\n"
                + "    <area id='myArea6' shape='rect' coords='0,0,1,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final String[] expected = getExpectedAlerts();

        setExpectedAlerts(new String[] {});
        final WebDriver driver = loadPage2(html);

        boolean displayed = driver.findElement(By.id("myArea1")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[0]), displayed);

        displayed = driver.findElement(By.id("myArea2")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[1]), displayed);

        displayed = driver.findElement(By.id("myArea3")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[2]), displayed);

        displayed = driver.findElement(By.id("myArea4")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[3]), displayed);

        displayed = driver.findElement(By.id("myArea5")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[4]), displayed);

        displayed = driver.findElement(By.id("myArea6")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[5]), displayed);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "true"})
    public void isDisplayedEmptyCircle() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <img id='myImg' usemap='#imgmap'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea1' shape='circle' coords='0,0,0'>\n"
                + "    <area id='myArea2' shape='circle' >\n"
                + "    <area id='myArea3' shape='circle' coords='0,0,0.8'>\n"
                + "  </map>\n"
                + "</body></html>";

        final String[] expected = getExpectedAlerts();

        setExpectedAlerts(new String[] {});
        final WebDriver driver = loadPage2(html);

        boolean displayed = driver.findElement(By.id("myArea1")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[0]), displayed);

        displayed = driver.findElement(By.id("myArea2")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[1]), displayed);

        displayed = driver.findElement(By.id("myArea3")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[2]), displayed);
    }


    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void isDisplayedEmptyPolygon() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <img id='myImg' usemap='#imgmap'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea1' shape='poly' coords='0,0'>\n"
                + "    <area id='myArea2' shape='poly' coords='0,0,1,1'>\n"
                + "    <area id='myArea3' shape='poly' >\n"
                + "    <area id='myArea4' shape='poly' coords='0,0,1,0,0,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final String[] expected = getExpectedAlerts();

        setExpectedAlerts(new String[] {});
        final WebDriver driver = loadPage2(html);

        boolean displayed = driver.findElement(By.id("myArea1")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[0]), displayed);

        displayed = driver.findElement(By.id("myArea2")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[1]), displayed);

        displayed = driver.findElement(By.id("myArea3")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[2]), displayed);

        displayed = driver.findElement(By.id("myArea4")).isDisplayed();
        assertEquals(Boolean.parseBoolean(expected[3]), displayed);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayedMissingImage() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <map id='myMap' name='imgmap' style='display: none'>\n"
                + "    <area id='myArea' shape='rect' coords='0,0,1,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        boolean displayed = driver.findElement(By.id("myMap")).isDisplayed();
        assertFalse(displayed);

        displayed = driver.findElement(By.id("myArea")).isDisplayed();
        assertFalse(displayed);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click_javascriptUrl() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head><script>" + LOG_TITLE_FUNCTION + "</script></head><body>\n"
            + "<img src='img.jpg' width='145' height='126' usemap='#somename'>\n"
            + "<map name='somename'>\n"
            + "  <area href='javascript:log(\"clicked\")' id='a2' shape='rect' coords='0,0,30,30'/>\n"
            + "</map></body></html>";

        final WebDriver driver = loadPage2(html);
        final Page page;
        if (driver instanceof HtmlUnitDriver) {
            page = getEnclosedPage();
        }
        else {
            page = null;
        }

        verifyTitle2(driver);

        driver.findElement(By.id("a2")).click();

        verifyTitle2(driver, "clicked");
        if (driver instanceof HtmlUnitDriver) {
            final Page secondPage = getEnclosedPage();
            assertSame(page, secondPage);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clicked")
    @BuggyWebDriver(FF = "Todo", FF_ESR = "Todo")
    public void click_javascriptUrlMixedCase() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head><script>" + LOG_TITLE_FUNCTION + "</script></head><body>\n"
            + "<img src='img.jpg' width='145' height='126' usemap='#somename'>\n"
            + "<map name='somename'>\n"
            + "  <area href='javasCRIpT:log(\"clicked\")' id='a2' shape='rect' coords='0,0,30,30'/>\n"
            + "</map>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final Page page;
        if (driver instanceof HtmlUnitDriver) {
            page = getEnclosedPage();
        }
        else {
            page = null;
        }

        verifyTitle2(driver);

        driver.findElement(By.id("a2")).click();

        verifyTitle2(driver, getExpectedAlerts());
        if (driver instanceof HtmlUnitDriver) {
            final Page secondPage = getEnclosedPage();
            assertSame(page, secondPage);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clicked")
    @BuggyWebDriver(FF = "Todo", FF_ESR = "Todo")
    public void click_javascriptUrlLeadingWhitespace() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head><script>" + LOG_TITLE_FUNCTION + "</script></head><body>\n"
            + "<img src='img.jpg' width='145' height='126' usemap='#somename'>\n"
            + "<map name='somename'>\n"
            + "  <area href='    javascript:log(\"clicked\")' id='a2' shape='rect' coords='0,0,30,30'/>\n"
            + "</map></body></html>";

        final WebDriver driver = loadPage2(html);
        final Page page;
        if (driver instanceof HtmlUnitDriver) {
            page = getEnclosedPage();
        }
        else {
            page = null;
        }

        verifyTitle2(driver);

        driver.findElement(By.id("a2")).click();

        verifyTitle2(driver, getExpectedAlerts());
        if (driver instanceof HtmlUnitDriver) {
            final Page secondPage = getEnclosedPage();
            assertSame(page, secondPage);
        }
    }

    /**
     * In action "this" should be the window and not the area.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @BuggyWebDriver(FF = "Todo", FF_ESR = "Todo")
    public void thisInJavascriptHref() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head><script>" + LOG_TITLE_FUNCTION + "</script></head><body>\n"
            + "<img src='img.jpg' width='145' height='126' usemap='#somename'>\n"
            + "<map name='somename'>\n"
            + "  <area href='javascript:log(this == window)' id='a2' shape='rect' coords='0,0,30,30'/>\n"
            + "</map></body></html>";

        final WebDriver driver = loadPage2(html);
        final Page page;
        if (driver instanceof HtmlUnitDriver) {
            page = getEnclosedPage();
        }
        else {
            page = null;
        }

        verifyTitle2(driver);

        driver.findElement(By.id("a2")).click();

        verifyTitle2(driver, getExpectedAlerts());
        if (driver instanceof HtmlUnitDriver) {
            final Page secondPage = getEnclosedPage();
            assertSame(page, secondPage);
        }
    }

}
