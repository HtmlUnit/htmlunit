/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * Proof of concept for using WebDriver to run (some) HtmlUnit tests and have the possibility
 * to check in "real" browsers if our expectations are correct.
 * <p>
 * This test runs with HtmlUnit unless the system property "htmlunit.webdriver" is set to "firefox"
 * in which case the test will run in the "real" firefox browser.
 * </p>
 * <p>
 * Examples:
 * mvn test -Dtest=SimpleWebDriverTest (runs the test with HtmlUnit)<br/>
 * mvn test -Dtest=SimpleWebDriverTest -Dhtmlunit.webdriver=firefox (runs the test with Firefox from the path)<br/>
 * mvn test -Dtest=SimpleWebDriverTest -Dhtmlunit.webdriver=firefox
 * -Dwebdriver.firefox.bin=/home/user/firefox-3.0.1
 * (runs the test with the specified Firefox version)<br/>
 * </p>
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SimpleWebDriverTest extends WebTestCase {
    private static Map<BrowserVersion, WebDriver> WEB_DRIVERS_ = new HashMap<BrowserVersion, WebDriver>();

    /**
     * Configure the driver only once.
     * @return the driver
     */
    protected WebDriver getWebDriver() {
        WebDriver webDriver = WEB_DRIVERS_.get(getBrowserVersion());
        if (webDriver == null) {
            webDriver = buildWebDriver();
            WEB_DRIVERS_.put(getBrowserVersion(), webDriver);
        }
        return webDriver;
    }

    /**
     * Closes the drivers.
     */
    @AfterClass
    public static void shutDownAll() {
        for (final WebDriver webDriver : WEB_DRIVERS_.values()) {
            webDriver.close();
        }
    }

    /**
     * Test event order.
     * @throws Exception if the test fails
     */
    @Test
    public void eventOrder() throws Exception {
        final File testsDir = new File("src/test/resources/testfiles");
        final File testFile = new File(testsDir, "testEventOrder.html");

        final WebDriver webDriver = getWebDriver();

        webDriver.get(testFile.toURI().toURL().toExternalForm());
        final WebElement textField = webDriver.findElement(By.id("foo"));
        textField.click(); // to give focus
        textField.sendKeys("a");
        webDriver.findElement(By.id("other")).click();

        // verifications
        assertEquals(getExpectedEntries(), getEntries("log"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickEvents() throws Exception {
        final File testsDir = new File("src/test/resources/testfiles");
        final File testFile = new File(testsDir, "testClickEvents.html");

        final WebDriver webDriver = getWebDriver();

        webDriver.get(testFile.toURI().toURL().toExternalForm());

        webDriver.findElement(By.id("testSpan")).click();
        webDriver.findElement(By.id("testInput")).click();
        webDriver.findElement(By.id("testImage")).click();

        // verifications
        assertEquals(getExpectedEntries(), getEntries("log"));
    }

    /**
     * Test handling of &lt;script event=".." for=".."&gt;.
     * @throws Exception if the test fails
     */
    @Test
    public void scriptEventFor() throws Exception {
        final File testsDir = new File("src/test/resources/testfiles");
        final File testFile = new File(testsDir, "testScriptEventFor.html");

        final WebDriver webDriver = getWebDriver();

        webDriver.get(testFile.toURI().toURL().toExternalForm());
        webDriver.findElement(By.id("div1")).click();
        webDriver.findElement(By.id("div2")).click();

        // verifications
        assertEquals(getExpectedEntries(), getEntries("log"));
    }

    private List<String> getExpectedEntries() {
        final WebDriver webDriver = getWebDriver();
        final BrowserVersion browserVersion = getBrowserVersion();

        String expectationNodeId = "expected";
        final List<WebElement> nodes = webDriver.findElements(By.xpath("//*[starts-with(@id, 'expected')]"));
        if (nodes.isEmpty()) {
            throw new RuntimeException("No expectations found in html code");
        }
        final String specificName = "expected_" + browserVersion.getNickName();
        for (final WebElement node : nodes) {
            final String nodeId = node.getAttribute("id");
            if (specificName.contains(nodeId) && nodeId.length() > expectationNodeId.length()) {
                expectationNodeId = nodeId;
            }
        }
        return getEntries(expectationNodeId);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void innerHTMLwithQuotes() throws Exception {
        doTest("testInnerHTML_quotesInAttribute.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void document_xxx_formAccess() throws Exception {
        doTest("testDocument.xxx_accessToForm.html");
    }

    private void doTest(final String fileName) throws Exception {
        final File testsDir = new File("src/test/resources/testfiles");
        final File testFile = new File(testsDir, fileName);

        getWebDriver().get(testFile.toURI().toURL().toExternalForm());

        // verifications
        assertEquals(getExpectedEntries(), getEntries("log"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Browsers({ Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7 })
    @Test
    public void fireEventCopyTemplateProperties() throws Exception {
        doTest("testFireEvent_initFromTemplate.html");
    }

    /**
     * Get the log entries for the node with the given id.
     * @param id the node id
     * @return the log entries
     */
    protected List<String> getEntries(final String id) {
        final List<WebElement> log = getWebDriver().findElements(By.xpath("id('" + id + "')/li"));
        final List<String> entries = new ArrayList<String>();
        for (final WebElement elt : log) {
            entries.add(elt.getText());
        }

        return entries;
    }

    private WebDriver buildWebDriver() {
        if ("firefox".equalsIgnoreCase(System.getProperty("htmlunit.webdriver"))) {
            return new FirefoxDriver();
        }
        // TODO: IEDriver
        final WebClient webClient = getWebClient();
        final HtmlUnitDriver driver = new HtmlUnitDriver(true) {
            @Override
            protected WebClient newWebClient() {
                return webClient;
            }

            @Override
            protected WebElement newHtmlUnitWebElement(final HtmlElement element) {
                return new FixedWebDriverHtmlUnitWebElement(this, element);
            }
        };
        return driver;
    }
}

/**
 * As HtmlUnit didn't generate the right events, WebDriver did it for us, but now that we do it correctly,
 * WebDriver shouldn't do it anymore
 * http://code.google.com/p/webdriver/issues/detail?id=93
 */
class FixedWebDriverHtmlUnitWebElement extends HtmlUnitWebElement {

    public FixedWebDriverHtmlUnitWebElement(final HtmlUnitDriver parent, final HtmlElement element) {
        super(parent, element);
    }

    @Override
    public void click() {
        if (!(getElement() instanceof ClickableElement)) {
            return;
        }

        final ClickableElement clickableElement = ((ClickableElement) getElement());
        try {
            clickableElement.click();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
