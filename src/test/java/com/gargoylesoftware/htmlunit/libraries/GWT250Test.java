/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;

/**
 * Tests for version 2.5.0 of <a href="https://developers.google.com/web-toolkit/">Google Web Toolkit</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class GWT250Test extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void hello() throws Exception {
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadGWTPage("Hello", collectedAlerts, "//button");
        final HtmlButton button = page.getFirstByXPath("//button");
        final DomText buttonLabel = (DomText) button.getChildren().iterator().next();
        assertEquals("Click me", buttonLabel.getData());
        button.click();
        final String[] expectedAlerts = {"Hello, AJAX"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test value inside {@link HtmlDivision}, {@link HtmlInput} or {@link DomText}
     *
     * @param element the element to search in
     * @param expectedValue expected value of the value inside the cell
     */
    private void assertElementValue(final HtmlElement element, final String expectedValue) {
        DomNode child = element.getFirstChild();
        while (child != null && !(child instanceof DomElement)
                && (!(child instanceof DomText) || !((DomText) child).getData().equals(expectedValue))) {
            child = child.getNextSibling();
        }
        if (child instanceof HtmlDivision) {
            final HtmlDivision div = (HtmlDivision) child;
            DomNode firstChild = div.getFirstChild();
            while (firstChild != null && !(firstChild instanceof DomElement)) {
                firstChild = firstChild.getNextSibling();
            }
            if (firstChild instanceof HtmlBold || firstChild instanceof HtmlItalic) {
                firstChild = firstChild.getFirstChild();
            }
            if (firstChild instanceof DomText) {
                final DomText text = (DomText) firstChild;
                assertEquals(expectedValue, text.getData());
            }
            else {
                fail("Could not find '" + expectedValue + "'");
            }
        }
        else if (child instanceof HtmlInput) {
            final HtmlInput input = (HtmlInput) child;
            assertEquals(expectedValue, input.getValueAttribute());
        }
        else if (child instanceof DomText) {
            final DomText text = (DomText) child;
            assertEquals(expectedValue, text.getData());
        }
        else {
            fail("Could not find '" + expectedValue + "'");
        }
    }

    /**
     * Returns the GWT directory being tested.
     * @return the GWT directory being tested
     */
    protected String getDirectory() {
        return "2.5.0";
    }

    /**
     * Loads the GWT unit test index page using the specified test name.
     *
     * @param testName the test name
     * @param collectedAlerts the List to collect alerts into
     * @param elementXPath the XPath for an element that is dynamically added to the page.
     * if not null than this method waits until this element is there (but max. 30s)
     * @throws Exception if an error occurs
     * @return the loaded page
     */
    protected HtmlPage loadGWTPage(final String testName, final List<String> collectedAlerts,
            final String elementXPath) throws Exception {
        final String resource = "libraries/GWT/" + getDirectory() + "/" + testName + "/" + testName + ".html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        final WebClient client = getWebClient();
        if (collectedAlerts != null) {
            client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        }

        final HtmlPage page = client.getPage(url);

        // wait because the integration build machine is sometimes busy
        if (null == elementXPath) {
            client.waitForBackgroundJavaScriptStartingBefore(2000);
        }
        else {
            final long endTime = System.currentTimeMillis() + 30000L;
            while (null == page.getFirstByXPath(elementXPath) && System.currentTimeMillis() < endTime) {
                client.waitForBackgroundJavaScriptStartingBefore(1000);
            }
        }
        return page;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dynaTable() throws Exception {
        startWebServer("src/test/resources/libraries/GWT/" + getDirectory() + "/DynaTable",
                new String[] {"src/test/resources/libraries/GWT/" + getDirectory() + "/gwt-servlet.jar"});

        final WebClient client = getWebClient();

        final String url = "http://localhost:" + PORT + "/DynaTable.html";
        final HtmlPage page = client.getPage(url);
        client.waitForBackgroundJavaScriptStartingBefore(2000);

        final String[] firstRow = {"Inman Mendez",
            "Majoring in Phrenology", "Mon 9:45-10:35, Tues 2:15-3:05, Fri 8:45-9:35, Fri 9:45-10:35"};

        final List<?> detailsCells = page.getByXPath("//table[@class='table']//tr[2]/td");
        assertEquals(firstRow.length, detailsCells.size());
        for (int i = 0; i < firstRow.length; i++) {
            final HtmlTableDataCell cell = (HtmlTableDataCell) detailsCells.get(i);
            assertElementValue(cell, firstRow[i]);
        }
    }

}
