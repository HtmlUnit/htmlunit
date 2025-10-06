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
package org.htmlunit.doc;

import java.util.Iterator;
import java.util.List;

import org.htmlunit.WebClient;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomNodeList;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlBody;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSubmitInput;
import org.htmlunit.html.HtmlTextInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for the sample code from the documentation to make sure
 * we adapt the docu or do not break the samples.
 *
 * @author Ronald Brill
 */
public class GettingStartedTest extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void homePage() throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://www.htmlunit.org/");
            Assertions.assertEquals("HtmlUnit – Welcome to HtmlUnit", page.getTitleText());

            final String pageAsXml = page.asXml();
            Assertions.assertTrue(pageAsXml.contains("<body class=\"topBarDisabled\">"));

            final String pageAsText = page.asNormalizedText();
            Assertions.assertTrue(pageAsText.contains("Support for the HTTP and HTTPS protocols"));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xpath() throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://www.htmlunit.org/");

            //get list of all divs
            final List<?> divs = page.getByXPath("//div");

            //get div which has a 'id' attribute of 'banner'
            final HtmlDivision div = (HtmlDivision) page.getByXPath("//div[@id='banner']").get(0);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void cssSelector() throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://www.htmlunit.org/");

            //get list of all divs
            final DomNodeList<DomNode> divs = page.querySelectorAll("div");
            for (final DomNode div : divs) {
                // ....
            }

            //get div which has the id 'breadcrumbs'
            final DomNode div = page.querySelector("div#breadcrumbs");
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    public void submittingForm() throws Exception {
        try (WebClient webClient = new WebClient()) {

            // Get the first page
            final HtmlPage page = webClient.getPage("http://some_url");

            // Get the form that we are dealing with and within that form,
            // find the submit button and the field that we want to change.
            final HtmlForm form = page.getFormByName("myform");

            final HtmlSubmitInput button = form.getInputByName("submitbutton");
            final HtmlTextInput textField = form.getInputByName("userid");

            // Change the value of the text field
            textField.type("root");

            // Now submit the form by clicking the button and get back the second page.
            final HtmlPage secondPage = button.click();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void extractTextToc() throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://www.htmlunit.org/");

            final DomNode sponsoringDiv = page.querySelector("#bodyColumn > section:nth-child(1) > div:nth-child(2)");

            // A normalized textual representation of this element that represents
            // what would be visible to the user if this page was shown in a web browser.
            // Whitespace is normalized like in the browser and block tags are separated by '\n'.
            final String content = sponsoringDiv.asNormalizedText();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void extractTextFromBody() throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://www.htmlunit.org/");

            final HtmlBody body = page.getBody();

            // A normalized textual representation of this element that represents
            // what would be visible to the user if this page was shown in a web browser.
            // Whitespace is normalized like in the browser and block tags are separated by '\n'.
            final String bodyContent = body.asNormalizedText();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    public void getElements() throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("http://some_url");

            final HtmlDivision div = page.getHtmlElementById("some_div_id");
            final HtmlAnchor anchor = page.getAnchorByName("anchor_name");
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    public void getElements2() throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("http://some_url");

            final DomNodeList<DomElement> inputs = page.getElementsByTagName("input");
            final Iterator<DomElement> nodesIterator = inputs.iterator();
            // now iterate
        }
    }
}
