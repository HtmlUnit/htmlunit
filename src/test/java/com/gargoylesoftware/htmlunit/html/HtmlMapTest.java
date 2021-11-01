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

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlMap}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlMapTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLMapElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <map id='myId'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlMap.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§a.html")
    @BuggyWebDriver(FF = "Element <area id=\"tester\" href=\"a.html\"> could not be scrolled into view",
                    FF78 = "Element <area id=\"tester\" href=\"a.html\"> could not be scrolled into view",
                    IE = "§§URL§§")
    public void mapClick() throws Exception {
        final URL urlImage = new URL(URL_FIRST, "img.jpg");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/4x7.jpg")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html
            = "<html>\n"
            + "<head></head>"
            + "<body>\n"
            + "  <img id='myImg' src='" + urlImage + "' usemap='#map1'>\n"
            + "  <map name='map1'>\n"
            + "    <area id='tester' href='a.html' shape='rect' coords='0,0,4,4'>\n"
            + "  </map>\n"
            + "</body></html>";

        final String secondContent
            = "<html><head><title>Second</title></head><body></body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setDefaultResponse(secondContent);


        final WebDriver driver = loadPage2(html);
        expandExpectedAlertsVariables(URL_FIRST);

        try {
            driver.findElement(By.id("tester")).click();

            assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
            assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
        }
        catch (final Exception e) {
            assertEquals(getExpectedAlerts()[0], e.getMessage().substring(0, getExpectedAlerts()[0].length()));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html = "<html><head><title>Page A</title></head>\n"
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
    public void isDisplayedHiddenImage() throws Exception {
        final String html = "<html><head><title>Page A</title></head>\n"
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
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayedHiddenMap() throws Exception {
        final String html = "<html><head><title>Page A</title></head>\n"
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
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayedMissingImage() throws Exception {
        final String html = "<html><head><title>Page A</title></head>\n"
                + "<body>\n"
                + "  <map id='myMap' name='imgmap' style='display: none'>\n"
                + "    <area id='myArea' shape='rect' coords='0,0,1,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final boolean displayed = driver.findElement(By.id("myMap")).isDisplayed();
        assertFalse(displayed);
    }
}
