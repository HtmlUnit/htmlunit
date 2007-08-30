/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.libraries;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 * Tests for 1.4 version of <a href="http://code.google.com/webtoolkit">Google Web Toolkit</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class GWT14Test extends WebTestCase {

    /**
     * Creates an instance.
     *
     * @param name The name of the test.
     */
    public GWT14Test(final String name) {
        super(name);
    }

    /**
     * @throws Exception If an error occurs.
     */
    public void testHello() throws Exception {
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(BrowserVersion.getDefault(), collectedAlerts, "Hello");
        final HtmlButton button = (HtmlButton) page.getByXPath("//button").get(0);
        final DomText buttonLabel = (DomText) button.getChildIterator().next();
        assertEquals("Click me", buttonLabel.getData());
        button.click();
        final String[] expectedAlerts = {"Hello, AJAX"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test I18N default language
     *
     * @throws Exception If an error occurs.
     */
    public void testI18N() throws Exception {
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(BrowserVersion.getDefault(), collectedAlerts, "I18N");
        testI18N(page, "numberFormatOutputText", "31,415,926,535.898");
        String timeZone = new SimpleDateFormat("Z").format(Calendar.getInstance().getTime());
        timeZone = timeZone.substring(0, 3) + ':' + timeZone.substring(3);
        testI18N(page, "dateTimeFormatOutputText", "Monday, September 13, 1999 12:00:00 AM GMT" + timeZone);
        testI18N(page, "messagesFormattedOutputText",
                "User 'amelie' has security clearance 'guest' and cannot access '/secure/blueprints.xml'");
        testI18N(page, "constantsFirstNameText", "Amelie");
        testI18N(page, "constantsLastNameText", "Crutcher");
        testI18N(page, "constantsFavoriteColorList",
                new String[] {"Red", "White", "Yellow", "Black", "Blue", "Green", "Grey", "Light Grey"});
        testI18N(page, "constantsWithLookupResultsText", "Red");
        final Map map = new HashMap();
        map.put("name", "Amelie Crutcher");
        map.put("timeZone", "EST");
        map.put("userID", "123");
        map.put("lastLogOn", "2/2/2006");
        testI18NDictionary(page, map);
    }

    /**
     * Test value inside {@link HtmlDivision} or {@link HtmlInput}
     * @param page The page to load.
     * @param id id of the element to search for.
     * @param expectedValue Expected value of the value inside the element
     * @throws Exception If the test fails.
     */
    private void testI18N(final HtmlPage page, final String id, final String expectedValue) throws Exception {
        final HtmlTableDataCell cell = (HtmlTableDataCell) page.getHtmlElementById(id);
        final Object child = cell.getFirstDomChild();
        if (child instanceof HtmlDivision) {
            final HtmlDivision div = (HtmlDivision) child;
            final DomText text = (DomText) div.getFirstDomChild();
            assertEquals(expectedValue, text.getData());
        }
        else if (child instanceof HtmlInput) {
            final HtmlInput input = (HtmlInput) child;
            assertEquals(expectedValue, input.getValueAttribute());
        }
    }

    /**
     * Test value of {@link HtmlSelect}
     *
     * @param page The page to load.
     * @param id id of the element to search for.
     * @param expectedValues Expected value of the value inside the select.
     * @throws Exception If the test fails.
     */
    private void testI18N(final HtmlPage page, final String id, final String[] expectedValues) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) page.getHtmlElementById(id);
        final Object child = cell.getFirstDomChild();
        if (child instanceof HtmlSelect) {
            final HtmlSelect select = (HtmlSelect) child;
            assertEquals(expectedValues.length, select.getOptionSize());
            for (int i = 0; i < expectedValues.length; i++) {
                assertEquals(expectedValues[i], select.getOption(i).getFirstDomChild().getNodeValue());
            }
        }
    }

    private void testI18NDictionary(final HtmlPage page, final Map expectedMap) throws Exception {
        final HtmlTableRow headerRow =
            (HtmlTableRow) page.getByXPath("//*[@class='i18n-dictionary-header-row']").get(0);
        final HtmlTableRow valueRow = (HtmlTableRow) headerRow.getNextDomSibling();
        DomNode headerNode = headerRow.getFirstDomChild();
        DomNode valueNode = valueRow.getFirstDomChild();
        final Set foundHeaders = new HashSet();
        for (int i = 0; i < expectedMap.size(); i++) {
            final String header = headerNode.getFirstDomChild().getNodeValue();
            final String value = valueNode.getFirstDomChild().getNodeValue();
            
            assertNotNull(expectedMap.get(header));
            assertEquals(expectedMap.get(header), value);
            foundHeaders.add(header);
            
            valueNode = valueNode.getNextDomSibling();
            headerNode = headerNode.getNextDomSibling();
        }
        assertEquals(expectedMap.size(), foundHeaders.size());
    }

    /**
     * Returns the GWT directory being tested.
     * @return the GWT directory being tested.
     */
    protected String getDirectory() {
        return "1.4.60";
    }

    /**
     * Loads the GWT unit test index page using the specified browser version, and test name.
     *
     * @param version The browser version to use.
     * @param collectedAlerts The List to collect alerts into.
     * @param testName The name of the sample GWT test.
     * @throws Exception if an error occurs.
     * @return The loaded page.
     */
    protected HtmlPage loadPage(final BrowserVersion version, final List collectedAlerts, final String testName)
        throws Exception {
        final String resource = "gwt/" + getDirectory() + "/" + testName + "/" + testName + ".html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        final WebClient client = new WebClient(version);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) client.getPage(url);
        page.getEnclosingWindow().getThreadManager().joinAll(10000);
        return page;
    }

}
