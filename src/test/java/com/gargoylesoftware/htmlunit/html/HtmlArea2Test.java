/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlArea}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlArea2Test extends WebDriverTestCase {

    private WebDriver createWebClient(final String onClick) throws Exception {
        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<body>\n"
            + "  <img src='/images/planets.gif' width='145' height='126' usemap='#planetmap'>\n"
            + "  <map id='planetmap' name='planetmap'>\n"
            + "    <area shape='rect' onClick=\"" + onClick + "\" coords='0,0,82,126' id='second' "
                        + "href='" + URL_SECOND + "'>\n"
            + "    <area shape='circle' coords='90,58,3' id='third' href='" + URL_THIRD + "'>\n"
            + "  </map>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>third</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent);

        return loadPage2(firstContent);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void referer() throws Exception {
        final WebDriver driver = createWebClient("");

        driver.get(URL_FIRST.toExternalForm());
        driver.findElement(By.id("third")).click();

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertEquals(URL_FIRST.toString(), lastAdditionalHeaders.get("Referer"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html = "<html><head><title>Page A</title></head>\n"
                + "<body>"
                + "  <img id='myImg' usemap='#imgmap'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea' shape='rect' coords='0,0,1,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);

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
                + "<body>"
                + "  <img id='myImg' usemap='#imgmap' style='display: none'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap'>\n"
                + "    <area id='myArea' shape='rect' coords='0,0,1,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);

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
        final String html = "<html><head><title>Page A</title></head>\n"
                + "<body>"
                + "  <img id='myImg' usemap='#imgmap'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap' style='display: none'>\n"
                + "    <area id='myArea' shape='rect' coords='0,0,1,1'>\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);

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
    public void isDisplayedEmptyArea() throws Exception {
        final String html = "<html><head><title>Page A</title></head>\n"
                + "<body>"
                + "  <img id='myImg' usemap='#imgmap'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
                + "  <map id='myMap' name='imgmap' style='display: none'>\n"
                + "    <area id='myArea1' shape='rect' coords='0,0,0,1'>\n"
                + "    <area id='myArea2' shape='rect' coords='0,0,1,0'>\n"
                + "    <area id='myArea3' shape='rect' coords='0,0,0,0'>\n"
                + "    <area id='myArea4' shape='rect' >\n"
                + "    <area id='myArea5' >\n"
                + "  </map>\n"
                + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);

        boolean displayed = driver.findElement(By.id("myArea1")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myArea2")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myArea3")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myArea4")).isDisplayed();
        assertTrue(displayed);

        displayed = driver.findElement(By.id("myArea5")).isDisplayed();
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

        final WebDriver driver = loadPageWithAlerts2(html);

        boolean displayed = driver.findElement(By.id("myMap")).isDisplayed();
        assertFalse(displayed);

        displayed = driver.findElement(By.id("myArea")).isDisplayed();
        assertFalse(displayed);
    }
}
