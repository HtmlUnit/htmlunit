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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;

import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * Base class for tests using WebDriver.
 * @version $Revision$
 * @author Marc Guillemot
 */
public abstract class WebDriverTestCase extends WebTestCase {
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
     * Reads the expected entries from the node "expected" or "expected_FF" or ... according to the browser used.
     * @return the expected entries
     */
    protected List<String> getExpectedEntries() {
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
