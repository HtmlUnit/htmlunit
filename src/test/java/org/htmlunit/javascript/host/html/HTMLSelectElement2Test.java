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
package org.htmlunit.javascript.host.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.htmlunit.MockWebConnection;
import org.htmlunit.Page;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlOption;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSelect;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HTMLSelectElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class HTMLSelectElement2Test extends SimpleWebTestCase {

    /**
     * Changes made through JS should not trigger an onchange event.
     * @throws Exception if the test fails
     */
    @Test
    public void noOnchangeFromJS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>Test infinite loop on js onchange</title></head>\n"
            + "<body><form name='myForm'>\n"
            + "<select name='a' onchange='this.form.b.selectedIndex=0'>\n"
            + "<option value='1'>one</option>\n"
            + "<option value='2'>two</option>\n"
            + "</select>\n"
            + "<select name='b' onchange='alert(\"b changed\")'>\n"
            + "<option value='G'>green</option>\n"
            + "<option value='R' selected>red</option>\n"
            + "</select>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        final HtmlSelect selectA = page.getFormByName("myForm").getSelectByName("a");
        final HtmlOption optionA2 = selectA.getOption(1);

        assertEquals("two", optionA2.asNormalizedText());

        final HtmlSelect selectB = page.getFormByName("myForm").getSelectByName("b");
        assertEquals(1, selectB.getSelectedOptions().size());
        assertEquals("red", selectB.getSelectedOptions().get(0).asNormalizedText());

         // changed selection in first select
        optionA2.setSelected(true);
        assertTrue(optionA2.isSelected());
        assertEquals(1, selectB.getSelectedOptions().size());
        assertEquals("green", selectB.getSelectedOptions().get(0).asNormalizedText());

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * Test for bug 1159709.
     * @throws Exception if the test fails
     */
    @Test
    public void rightPageAfterOnchange() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<iframe src='fooIFrame.html'></iframe>\n"
            + "<form name='form1' action='http://first' method='post'>\n"
            + "  <select name='select1' onchange='this.form.submit()'>\n"
            + "    <option value='option1' selected='true' name='option1'>One</option>\n"
            + "    <option value='option2' name='option2'>Two</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse("<html><body></body></html>");
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlForm form = page.getFormByName("form1");
        final HtmlSelect select = form.getSelectByName("select1");
        final Page page2 = select.setSelectedAttribute("option2", true);
        assertEquals("http://first/", page2.getUrl());
    }

    /**
     * Tests that select delegates submit to the containing form.
     * @throws Exception if the test fails
     */
    @Test
    public void onChangeCallsFormSubmit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form name='test' action='foo'>\n"
            + "<select name='select1' onchange='submit()'>\n"
            + "<option>a</option>\n"
            + "<option selected='selected'>b</option>\n"
            + "</select></form>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse(DOCTYPE_HTML + "<html><title>page 2</title><body></body></html>");
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlPage page2 = page.getFormByName("test").getSelectByName("select1").getOption(0).click();
        assertEquals("page 2", page2.getTitleText());
    }

    /**
     * Test for bug 1684652.
     * @throws Exception if the test fails
     */
    @Test
    public void selectedIndexReset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>first</title></head>\n"
            + "<body onload='document.forms[0].testSelect.selectedIndex = -1; "
            + "  document.forms[0].testSelect.options[0].selected = true;'>\n"
            + "<form>\n"
            + "<select name='testSelect'>\n"
            + "<option value='testValue'>value</option>\n"
            + "</select>\n"
            + "<input id='testButton' type='submit'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final Page page2 = page.getHtmlElementById("testButton").click();
        final URL url2 = page2.getUrl();
        assertTrue("Select in URL " + url2, url2.toExternalForm().contains("testSelect=testValue"));
    }

}
