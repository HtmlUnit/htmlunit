/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.htmlunit.HttpHeader;
import org.htmlunit.Page;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * Tests for {@link HtmlArea}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlAreaTest extends WebDriverTestCase {

    private static final String IMG_SRC = " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
            + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='";

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
                + "  <img id='myImg' usemap='#imgmap' " + IMG_SRC + ">\n"
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
                + "  <img id='myImg' usemap='#imgmap' " + IMG_SRC + ">\n"
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
                + "  <img id='myImg' usemap='#imgmap' " + IMG_SRC + ">\n"
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
                + "  <img id='myImg' usemap='#imgmap' style='display: none' " + IMG_SRC + ">\n"
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
                + "  <img id='myImg' usemap='#imgmap' " + IMG_SRC + ">\n"
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
                + "  <img id='myImg' usemap='#imgmap' " + IMG_SRC + ">\n"
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

        setExpectedAlerts(ArrayUtils.EMPTY_STRING_ARRAY);
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
                + "  <img id='myImg' usemap='#imgmap' " + IMG_SRC + ">\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea1' shape='circle' coords='0,0,0'>\n"
                + "    <area id='myArea2' shape='circle' >\n"
                + "    <area id='myArea3' shape='circle' coords='0,0,0.8'>\n"
                + "  </map>\n"
                + "</body></html>";

        final String[] expected = getExpectedAlerts();

        setExpectedAlerts(ArrayUtils.EMPTY_STRING_ARRAY);
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
                + "  <img id='myImg' usemap='#imgmap' " + IMG_SRC + ">\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea1' shape='poly' coords='0,0'>\n"
                + "    <area id='myArea2' shape='poly' coords='0,0,1,1'>\n"
                + "    <area id='myArea3' shape='poly' >\n"
                + "    <area id='myArea4' shape='poly' coords='0,0,1,0,0,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final String[] expected = getExpectedAlerts();

        setExpectedAlerts(ArrayUtils.EMPTY_STRING_ARRAY);
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
            + "  <area href='javascript:log(\"clicked\")' id='a2' shape='rect' coords='0,0,145,126'/>\n"
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

        if (useRealBrowser() && getBrowserVersion().isFirefox()) {
            final WebElement img = driver.findElement(By.tagName("img"));
            new Actions(driver).moveToElement(img, 10, 10).click().perform();
        }
        else {
            driver.findElement(By.id("a2")).click();
        }

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
            + "  <area href='javasCRIpT:log(\"clicked\")' id='a2' shape='rect' coords='0,0,145,126'/>\n"
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

        if (useRealBrowser() && getBrowserVersion().isFirefox()) {
            final WebElement img = driver.findElement(By.tagName("img"));
            new Actions(driver).moveToElement(img, 10, 10).click().perform();
        }
        else {
            driver.findElement(By.id("a2")).click();
        }

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
            + "  <area href='    javascript:log(\"clicked\")' id='a2' shape='rect' coords='0,0,145,126'/>\n"
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

        if (useRealBrowser() && getBrowserVersion().isFirefox()) {
            final WebElement img = driver.findElement(By.tagName("img"));
            new Actions(driver).moveToElement(img, 10, 10).click().perform();
        }
        else {
            driver.findElement(By.id("a2")).click();
        }

        verifyTitle2(driver, getExpectedAlerts());
        if (driver instanceof HtmlUnitDriver) {
            final Page secondPage = getEnclosedPage();
            assertSame(page, secondPage);
        }
    }

    /**
     * In action "this" should be the window and not the area.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
            + "  <area href='javascript:log(this == window)' id='a2' shape='rect' coords='0,0,145,126'/>\n"
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

        if (useRealBrowser() && getBrowserVersion().isFirefox()) {
            final WebElement img = driver.findElement(By.tagName("img"));
            new Actions(driver).moveToElement(img, 10, 10).click().perform();
        }
        else {
            driver.findElement(By.id("a2")).click();
        }

        verifyTitle2(driver, getExpectedAlerts());
        if (driver instanceof HtmlUnitDriver) {
            final Page secondPage = getEnclosedPage();
            assertSame(page, secondPage);
        }
    }

    private void areaIsDisplayed(final String body, final String... ids) throws Exception {
        final String html = DOCTYPE_HTML + "<html><body>\n" + body + "\n</body></html>";
        final WebDriver driver = loadPage2(html);

        final List<String> actual = new ArrayList<>();
        for (final String id : ids) {
            actual.add(String.valueOf(driver.findElement(By.id(id)).isDisplayed()));
        }
        assertEquals(Arrays.asList(getExpectedAlerts()), actual);
    }

    /** Non-empty shapes on a visible image are displayed. */
    @Test
    @Alerts({ "true", "true", "true", "true" })
    public void areaShapes() throws Exception {
        areaIsDisplayed("<img usemap='#m' width='20' height='20'" + IMG_SRC + ">\n"
                + "<map name='m'>\n"
                + "  <area id='rect' shape='rect' coords='0,0,10,10'>\n"
                + "  <area id='circle' shape='circle' coords='10,10,5'>\n"
                + "  <area id='poly' shape='poly' coords='0,0,10,0,10,10'>\n"
                + "  <area id='default' shape='default'>\n"
                + "</map>", "rect", "circle", "poly", "default");
    }

    /** Shapes with zero width or height are not displayed. */
    @Test
    @Alerts({"false", "false", "false", "false"})
    @HtmlUnitNYI(CHROME = {"false", "false", "false", "true"},
            EDGE = {"false", "false", "false", "true"},
            FF = {"false", "false", "false", "true"},
            FF_ESR = {"false", "false", "false", "true"})
    public void areaEmptyShapes() throws Exception {
        areaIsDisplayed("<img usemap='#m' width='20' height='20'" + IMG_SRC + ">\n"
                + "<map name='m'>\n"
                + "  <area id='rectZero' shape='rect' coords='0,0,0,0'>\n"
                + "  <area id='rectNoWidth' shape='rect' coords='5,0,5,10'>\n"
                + "  <area id='circleZero' shape='circle' coords='10,10,0'>\n"
                + "  <area id='polyLine' shape='poly' coords='0,0,10,0,20,0'>\n"
                + "</map>",
                "rectZero", "rectNoWidth", "circleZero", "polyLine");
    }

    /** The area's own display/visibility is ignored, only image and shape count. */
    @Test
    @Alerts({ "true", "true", "true" })
    public void areaOwnStyleIgnored() throws Exception {
        areaIsDisplayed("<img usemap='#m' width='20' height='20'" + IMG_SRC + ">\n"
                + "<map name='m'>\n"
                + "  <area id='none' shape='rect' coords='0,0,10,10' style='display: none'>\n"
                + "  <area id='block' shape='rect' coords='0,0,10,10' style='display: block'>\n"
                + "  <area id='hidden' shape='rect' coords='0,0,10,10' style='visibility: hidden'>\n"
                + "</map>",
                "none", "block", "hidden");
    }

    /** Hidden image: area is not displayed. HtmlUnit mismatch: currently true. */
    @Test
    @Alerts({ "false", "false" })
    public void areaImageHidden() throws Exception {
        areaIsDisplayed("<img usemap='#m1' width='20' height='20' style='display: none'" + IMG_SRC + ">\n"
                + "<map name='m1'><area id='a1' shape='rect' coords='0,0,10,10'></map>\n"
                + "<img usemap='#m2' width='20' height='20' style='visibility: hidden'" + IMG_SRC + ">\n"
                + "<map name='m2'><area id='a2' shape='rect' coords='0,0,10,10'></map>", "a1", "a2");
    }

    /** No image uses the map. HtmlUnit mismatch: currently true. */
    @Test
    @Alerts("false")
    public void areaWithoutImage() throws Exception {
        areaIsDisplayed("<map name='m'><area id='a1' shape='rect' coords='0,0,10,10'></map>", "a1");
    }

    /**
     * The image points to a different map name. HtmlUnit mismatch: currently true.
     */
    @Test
    @Alerts("false")
    public void areaImageUsesOtherMap() throws Exception {
        areaIsDisplayed("<img usemap='#other' width='20' height='20'" + IMG_SRC + ">\n"
                + "<map name='m'><area id='a1' shape='rect' coords='0,0,10,10'></map>", "a1");
    }

    /**
     * The map's own display is not consulted. HtmlUnit mismatch: currently false.
     */
    @Test
    @Alerts("true")
    public void areaMapHidden() throws Exception {
        areaIsDisplayed("<img usemap='#m' width='20' height='20'" + IMG_SRC + ">\n"
                + "<map name='m' style='display: none'>" + "<area id='a1' shape='rect' coords='0,0,10,10'></map>",
                "a1");
    }

    /** An area outside a map falls back to its default display:none. */
    @Test
    @Alerts("false")
    public void areaOutsideMap() throws Exception {
        areaIsDisplayed("<area id='a1' shape='rect' coords='0,0,10,10'>", "a1");
    }

    /** Fewer than three points is not an area. */
    @Test
    @Alerts("true")
    public void areaPolyTwoPoints() throws Exception {
        areaShape("poly", "0,0,10,10");
    }

    /** Reversed corners: the spec swaps them, so this should be a real rect. */
    @Test
    @Alerts("false")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void areaRectReversed() throws Exception {
        areaShape("rect", "10,10,0,0");
    }

    /** Negative radius. */
    @Test
    @Alerts("false")
    public void areaCircleNegativeRadius() throws Exception {
        areaShape("circle", "10,10,-5");
    }

    /** Blank and comma mix, which parseRect claims browsers accept. */
    @Test
    @Alerts("false")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void areaCoordsSeparators() throws Exception {
        areaShape("rect", "0 0, 10  10");
    }

    /** Shape value is case-insensitive. */
    @Test
    @Alerts("true")
    public void areaShapeUpperCase() throws Exception {
        areaShape("RECT", "0,0,10,10");
    }

    /**
     * An invalid shape value falls back to rect. HtmlUnit has no branch for this.
     */
    @Test
    @Alerts("false")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void areaShapeInvalid() throws Exception {
        areaShape("foo", "0,0,10,10");
    }

    /** Invalid shape with empty coords. */
    @Test
    @Alerts("false")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void areaShapeInvalidEmptyCoords() throws Exception {
        areaShape("foo", "0,0,0,0");
    }

    /** Missing coords on each shape. */
    @Test
    @Alerts({ "false", "false", "false" })
    public void areaNoCoords() throws Exception {
        areaIsDisplayed("<img usemap='#m' width='20' height='20'" + IMG_SRC + ">\n" + "<map name='m'>\n"
                + "  <area id='rect' shape='rect'>\n" + "  <area id='circle' shape='circle'>\n"
                + "  <area id='poly' shape='poly'>\n" + "</map>", "rect", "circle", "poly");
    }

    /** No shape and no coords at all. */
    @Test
    @Alerts("false")
    public void areaNoShapeNoCoords() throws Exception {
        areaIsDisplayed(
                "<img usemap='#m' width='20' height='20'" + IMG_SRC + ">\n" + "<map name='m'><area id='a'></map>", "a");
    }

    /** An area nested in a div inside the map is valid HTML. */
    @Test
    @Alerts("false")
    public void areaNestedInMap() throws Exception {
        areaIsDisplayed("<img usemap='#m' width='20' height='20'" + IMG_SRC + ">\n"
                + "<map name='m'><div><area id='a' shape='rect' coords='0,0,10,10'></div></map>", "a");
    }

    /** The whole structure sits in a hidden container. */
    @Test
    @Alerts("false")
    public void areaAncestorHidden() throws Exception {
        areaIsDisplayed("<div style='display: none'>" + "<img usemap='#m' width='20' height='20'" + IMG_SRC + ">\n"
                + "<map name='m'><area id='a' shape='rect' coords='0,0,10,10'></map></div>", "a");
    }

    /** Map hidden through an ancestor while the image stays visible. */
    @Test
    @Alerts("true")
    public void areaMapAncestorHidden() throws Exception {
        areaIsDisplayed("<img usemap='#m' width='20' height='20'" + IMG_SRC + ">\n" + "<div style='display: none'>"
                + "<map name='m'><area id='a' shape='rect' coords='0,0,10,10'></map></div>", "a");
    }

    /** Everything changes after load. This catches any caching of the result. */
    @Test
    @Alerts({ "false", "true", "false", "true" })
    public void areaDynamicChanges() throws Exception {
        final WebDriver driver = loadPage2(
                DOCTYPE_HTML + "<html><body>\n" + "<img id='img' usemap='#m' width='20' height='20'" + IMG_SRC + ">\n"
                        + "<map name='m'><area id='a' shape='rect' coords='0,0,0,0'></map>\n" + "</body></html>");
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final WebElement area = driver.findElement(By.id("a"));
        final List<String> actual = new ArrayList<>();

        actual.add(String.valueOf(area.isDisplayed()));
        js.executeScript("document.getElementById('a').coords = '0,0,10,10'");
        actual.add(String.valueOf(area.isDisplayed()));
        js.executeScript("document.getElementById('img').style.display = 'none'");
        actual.add(String.valueOf(area.isDisplayed()));
        js.executeScript("document.getElementById('img').style.display = ''");
        actual.add(String.valueOf(area.isDisplayed()));

        assertEquals(Arrays.asList(getExpectedAlerts()), actual);
    }

    @Test
    @Alerts("false")
    public void areaRectZero() throws Exception {
        areaShape("rect", "0,0,0,0");
    }

    @Test
    @Alerts("false")
    public void areaRectNoWidth() throws Exception {
        areaShape("rect", "5,0,5,10");
    }

    @Test
    @Alerts("false")
    public void areaCircleZeroRadius() throws Exception {
        areaShape("circle", "10,10,0");
    }

    @Test
    @Alerts("false")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void areaPolyCollinear() throws Exception {
        areaShape("poly", "0,0,10,0,20,0");
    }

    private void areaShape(final String shape, final String coords) throws Exception {
        areaIsDisplayed("<img usemap='#m' width='20' height='20'" + IMG_SRC + ">\n"
                + "<map name='m'><area id='a' shape='" + shape + "' coords='" + coords + "'></map>", "a");
    }
}
