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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.jetty.Server;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpWebConnectionTest;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;

/**
 * Tests for 1.5 release candidate 1 of <a href="http://code.google.com/webtoolkit">Google Web Toolkit</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class GWT15RC1Test extends WebTestCase {

    private Server server_;

    /**
     * Test value inside {@link HtmlDivision}, {@link HtmlInput} or {@link DomText}
     *
     * @param cell the cells to search in
     * @param expectedValue expected value of the value inside the cell
     * @throws Exception if the test fails
     */
    private void tableDataCell(final HtmlTableDataCell cell, final String expectedValue) {
        final Object child = cell.getFirstChild();
        if (child instanceof HtmlDivision) {
            final HtmlDivision div = (HtmlDivision) child;
            DomNode firstChild = div.getFirstChild();
            if (firstChild instanceof HtmlUnknownElement
                    && (firstChild.getNodeName().equals("b") || firstChild.getNodeName().equals("i"))) {
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
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(Browser.FIREFOX_2)
    public void dynaTable() throws Exception {
        server_ = HttpWebConnectionTest.startWebServer("src/test/resources/gwt/" + getDirectory() + "/DynaTable",
                new String[] {"src/test/resources/gwt/" + getDirectory() + "/gwt-servlet.jar"});
        
        final WebClient client = getWebClient();

        final String url = "http://localhost:" + HttpWebConnectionTest.PORT + "/DynaTable.html";
        final HtmlPage page = (HtmlPage) client.getPage(url);

        final String[] firstRow = {"Inman Mendez",
            "Majoring in Phrenology", "Mon 9:45-10:35, Tues 2:15-3:05, Fri 8:45-9:35, Fri 9:45-10:35"};

        //try 40 times to wait .5 second each for filling the page.
        for (int i = 0; i < 40; i++) {
            final List< ? > detailsCells = page.getByXPath("//table[@class='table']//tr[2]/td");
            if (detailsCells.size() == firstRow.length) {
                final HtmlTableDataCell firstCell = (HtmlTableDataCell) detailsCells.get(0);
                if (firstCell.getFirstChild().getNodeValue().equals(firstRow[0])) {
                    break;
                }
            }
            synchronized (page) {
                page.wait(500);
            }
        }

        final List< ? > detailsCells = page.getByXPath("//table[@class='table']//tr[2]/td");
        assertEquals(firstRow.length, detailsCells.size());
        for (int i = 0; i < firstRow.length; i++) {
            final HtmlTableDataCell cell = (HtmlTableDataCell) detailsCells.get(i);
            tableDataCell(cell, firstRow[i]);
        }
    }

    /**
     * Returns the GWT directory being tested.
     * @return the GWT directory being tested
     */
    protected String getDirectory() {
        return "1.5RC1";
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    public void after() throws Exception {
        HttpWebConnectionTest.stopWebServer(server_);
        server_ = null;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void loadingJavaScript() throws Exception {
        final String firstContent = " <html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    document.getElementById('debugDiv').innerHTML += 'before, ';\n"
            + "    var iframe2 = document.createElement('iframe');\n"
            + "    iframe2.src = '" + URL_SECOND + "';\n"
            + "    document.body.appendChild(iframe2);\n"
            + "    var iframe3 = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe3);\n"
            + "    iframe3.src = '" + URL_THIRD + "';\n"
            + "    document.getElementById('debugDiv').innerHTML += 'after, ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='debugDiv'/>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent
            = "<script>parent.document.getElementById('debugDiv').innerHTML += 'second.html, ';</script>";
        final String thirdContent
            = "<script>parent.document.getElementById('debugDiv').innerHTML += 'third.html, ';</script>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        conn.setResponse(URL_THIRD, thirdContent);
        client.setWebConnection(conn);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlDivision div = (HtmlDivision) page.getHtmlElementById("debugDiv");
        assertEquals("before, after, second.html, third.html, ", div.getFirstChild().getNodeValue());
    }
}
