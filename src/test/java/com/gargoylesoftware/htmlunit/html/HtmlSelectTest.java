/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlSelect}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Mike Williams
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlSelectTest extends SimpleWebTestCase {
    /** JUnit rule must be public fields :-(. */
    @Rule
    public TemporaryFolder tmpFolderProvider_ = new TemporaryFolder();

    /**
     * Test the good path of submitting a select.
     * @exception Exception If the test fails
     */
    @Test
    public void testSelect() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1'>\n"
            + "<option value='option1'>Option1</option>\n"
            + "<option value='option2' selected='selected'>Option2</option>\n"
            + "<option value='option3'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select1").get(0);
        final HtmlSubmitInput button = form.getInputByName("button");

        // Test that the select is being correctly identified as a submittable element
        assertEquals(Arrays.asList(new Object[] {select, button}), form.getSubmittableElements(button));

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = button.click();

        assertEquals("url", getDefaultUrl() + "?select1=option2&button=foo", secondPage.getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
        assertNotNull(secondPage);
    }

    /**
     * Tests submitting the select with no options selected.
     * @exception Exception If the test fails
     */
    @Test
    public void testSelect_MultipleSelectNoneSelected() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' multiple>\n"
            + "<option value='option1'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<option value='option3'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select1").get(0);
        assertNotNull(select);

        final HtmlSubmitInput button = form.getInputByName("button");

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = button.click();

        assertEquals("url", getDefaultUrl() + "?button=foo", secondPage.getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
        assertNotNull(secondPage);
    }

    /**
     * Tests changing the selected option.
     * @exception Exception If the test fails
     */
    @Test
    public void testSelect_ChangeSelectedOption_SingleSelect() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1'>\n"
            + "<option value='option1' selected='selected'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<option value='option3'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select1").get(0);
        final HtmlSubmitInput button = form.getInputByName("button");

        // Change the value
        select.setSelectedAttribute("option3", true);

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = (HtmlPage) button.click();

        assertEquals("url", getDefaultUrl() + "?select1=option3&button=foo", secondPage.getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
        assertNotNull(secondPage);
    }

    /**
     * Tests changing the selected option.
     * @exception Exception If the test fails
     */
    @Test
    public void testSelect_ChangeSelectedOption_MultipleSelect() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' multiple='multiple'>\n"
            + "<option value='option1' selected='selected'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<option value='option3'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select1").get(0);
        final HtmlSubmitInput button = form.getInputByName("button");

        // Change the value
        select.setSelectedAttribute("option3", true);
        select.setSelectedAttribute("option2", true);

        // Test that the correct value is being passed back up to the server
        final HtmlPage secondPage = button.click();

        assertEquals("url", getDefaultUrl() + "?select1=option1&select1=option2&select1=option3&button=foo",
                secondPage.getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
        assertNotNull(secondPage);
    }

    /**
     * Tests multiple selected options on multiple select lists.
     * @exception Exception If the test fails
     */
    @Test
    public void testSelect_MultipleSelectMultipleSelected() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' multiple>\n"
            + "<option value='option1' selected='selected'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<option value='option3' selected='selected'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select1").get(0);
        final List<HtmlOption> expected = new ArrayList<HtmlOption>();
        expected.add(select.getOptionByValue("option1"));
        expected.add(select.getOptionByValue("option3"));

        assertEquals(expected, select.getSelectedOptions());
    }

    /**
     * Test multiple selected options on single select lists. This is erroneous HTML, but
     * browsers simply use the last option.
     *
     * @exception Exception If the test fails
     */
    @Test
    public void testSelect_SingleSelectMultipleSelected() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1'>\n"
            + "<option value='option1' selected='selected'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<option value='option3' selected='selected'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select1").get(0);
        final List<HtmlOption> expected = new ArrayList<HtmlOption>();
        expected.add(select.getOptionByValue("option3"));

        assertEquals(expected, select.getSelectedOptions());
    }

    /**
     * Test no selected options on single select lists. This is erroneous HTML, but
     * browsers simply assume the first one to be selected
     *
     * @exception Exception If the test fails
     */
    @Test
    public void testSelect_SingleSelectNoneSelected() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1'>\n"
            + "<option value='option1'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<option value='option3'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select1").get(0);
        final List<HtmlOption> expected = new ArrayList<HtmlOption>();
        expected.add(select.getOptionByValue("option1"));

        assertEquals(expected, select.getSelectedOptions());
    }

    /**
     * Tests no selected options on single select lists with a size > 1.
     * @exception Exception If the test fails
     */
    @Test
    public void testSelect_SingleSelectNoneSelectedButSizeGreaterThanOne() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form>\n"
            + "<select name='select1' size='2' id='mySelect'>\n"
            + "<option value='option1'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<option value='option3'>Option3</option>\n"
            + "</select>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlSelect select = page.getHtmlElementById("mySelect");

        assertEquals(Collections.EMPTY_LIST, select.getSelectedOptions());
    }

    /**
     * Tests changing the selected option.
     * @exception Exception If the test fails
     */
    @Test
    public void testSetSelected_IllegalValue() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1'>\n"
            + "<option value='option1' selected='selected'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<option value='option3'>Option3</option>\n"
            + "</select>\n"
            + "<select name='select2'>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlSelect select = form.getSelectByName("select1");

        select.setSelectedAttribute("missingOption", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetOptions() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1'>\n"
            + "<option value='option1' selected='selected'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<optgroup label='group1'>\n"
            + "    <option value='option3'>Option3</option>\n"
            + "</optgroup>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select1").get(0);

        final List<HtmlOption> expectedOptions = new ArrayList<HtmlOption>();
        expectedOptions.add(select.getOptionByValue("option1"));
        expectedOptions.add(select.getOptionByValue("option2"));
        expectedOptions.add(select.getOptionByValue("option3"));

        assertEquals(expectedOptions, select.getOptions());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSelect_OptionMultiple_NoValueOnAttribute() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' id='select1' multiple>\n"
            + "<option value='option1'>Option1</option>\n"
            + "<option value='option2' >Option2</option>\n"
            + "<option value='option3'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlSelect select = page.getHtmlElementById("select1");
        assertTrue(select.isMultipleSelectEnabled());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetOptionByValue() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body><form id='form1'>\n"
            + "<select name='select1'>\n"
            + "    <option value='option1'>s1o1</option>\n"
            + "    <option value='option2'>s1o2</option>\n"
            + "</select>\n"
            + "<select name='select2'>\n"
            + "    <option value='option1'>s2o1</option>\n"
            + "    <option value='option2'>s2o2</option>\n"
            + "    <option>s2o3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select2").get(0);
        assertEquals("s2o2", select.getOptionByValue("option2").asText());

        assertEquals(select.getOption(2), select.getOptionByValue("s2o3"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSelect_SetSelected_OnChangeHandler() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' onChange='alert(\"changing\")'>\n"
            + "<option value='option1' selected='selected'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<option value='option3'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select1").get(0);

        // Change the value
        select.setSelectedAttribute("option3", true);

        final String[] expectedAlerts = {"changing"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetSelectionOnOptionWithNoName() throws Exception {
        final String htmlContent = "<html><body><form name='form' method='GET' action='action.html'>\n"
            + "<select name='select' multiple size='5'>\n"
            + "<option value='1'>111</option>\n"
            + "<option id='option2'>222</option>\n"
            + "</select>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlOption option = page.getHtmlElementById("option2");
        option.setSelected(true);
    }

    private void checkOptions(final HtmlSelect select) {
        final List<HtmlOption> options = select.getOptions();
        if (options.isEmpty()) {
            assertNull(select.getFirstChild());
            assertNull(select.getLastChild());
        }
        else {
            assertEquals(options.get(0), select.getFirstChild());
            assertEquals(options.get(options.size() - 1), select.getLastChild());
        }
    }

    /** @throws Exception if the test fails */
    @Test
    public void testRemoveOptionsFromSelect() throws Exception {
        final String htmlContent = "<html><body><form name='form' method='GET' action='action.html'>\n"
            + "<select name='select' id='theSelect'>"
            + "<option value='a'>111</option>"
            + "<option value='b'>222</option>"
            + "<option value='c'>333</option>"
            + "<option value='d'>444</option>"
            + "</select>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlSelect theSelect = page.getHtmlElementById("theSelect");
        assertNotNull(theSelect);

        assertEquals(4, theSelect.getOptions().size());
        assertEquals("a", theSelect.getOption(0).getValueAttribute());
        assertEquals("b", theSelect.getOption(1).getValueAttribute());
        assertEquals("c", theSelect.getOption(2).getValueAttribute());
        assertEquals("d", theSelect.getOption(3).getValueAttribute());

        // remove from the middle
        theSelect.getOption(1).remove();
        checkOptions(theSelect);
        assertEquals(3, theSelect.getOptions().size());
        assertEquals("a", theSelect.getOption(0).getValueAttribute());
        assertEquals("c", theSelect.getOption(1).getValueAttribute());
        assertEquals("d", theSelect.getOption(2).getValueAttribute());

        // remove from the end
        theSelect.getOption(2).remove();
        checkOptions(theSelect);
        assertEquals(2, theSelect.getOptions().size());
        assertEquals("a", theSelect.getOption(0).getValueAttribute());
        assertEquals("c", theSelect.getOption(1).getValueAttribute());

        // remove from the front
        theSelect.getOption(0).remove();
        checkOptions(theSelect);
        assertEquals(1, theSelect.getOptions().size());
        assertEquals("c", theSelect.getOption(0).getValueAttribute());

        // remove from the last one
        theSelect.getOption(0).remove();
        checkOptions(theSelect);
        assertEquals(0, theSelect.getOptions().size());
    }

    /** @throws Exception if the test fails */
    @Test
    public void testEditOptions() throws Exception {
        final String htmlContent = "<html><body><form name='form' method='GET' action='action.html'>\n"
            + "<select name='select' id='theSelect'>\n"
            + "<option value='a'>111</option>\n"
            + "<option value='b'>222</option>\n"
            + "<option value='c'>333</option>\n"
            + "</select>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlSelect theSelect = page.getHtmlElementById("theSelect");

        assertNotNull(theSelect);
        assertEquals(3, theSelect.getOptions().size());

        appendOption(theSelect, "d");
        assertEquals(4, theSelect.getOptions().size());
        assertEquals("d", theSelect.getOption(3).getValueAttribute());

        theSelect.setOptionSize(1);
        assertEquals(1, theSelect.getOptions().size());
        assertEquals("a", theSelect.getOption(0).getValueAttribute());

        appendOption(theSelect, "x");
        assertEquals(2, theSelect.getOptions().size());
        assertEquals("x", theSelect.getOption(1).getValueAttribute());
    }

    void appendOption(final HtmlSelect select, final String value) {
        final HtmlOption option = (HtmlOption) HTMLParser.getFactory(HtmlOption.TAG_NAME).createElement(
                select.getPage(), HtmlOption.TAG_NAME, null);
        option.setValueAttribute(value);
        option.setLabelAttribute(value);
        select.appendOption(option);
    }

    /**
     * Test that asText() returns a blank string if nothing is selected.
     *
     * @exception Exception If the test fails
     */
    @Test
    public void asTextWhenNothingSelected() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form>\n"
            + "<select name='select1' size='1' id='mySelect'>\n"
            + "</select>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlSelect select = page.getHtmlElementById("mySelect");

        assertEquals("", select.asText());
    }

    /**
     * Verifies that asText() returns all options when multiple options are selectable, instead of just
     * the selected ones.
     * @throws Exception if an error occurs
     */
    @Test
    public void asTextWithMultipleSelect() throws Exception {
        final String html = "<html><body><form>\n"
            + "<select name='a' multiple>\n"
            + "<option value='1'>foo</option>\n"
            + "<option value='2'>bar</option>\n"
            + "<option value='3'>baz</option>\n"
            + "</select>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlSelect select = (HtmlSelect) page.getDocumentElement().getHtmlElementsByTagName("select").get(0);
        assertEquals("foo\nbar\nbaz", select.asText());
    }

    /**
     * Test that setSelectedAttribute returns the right page.
     *
     * @exception Exception If the test fails
     */
    @Test
    public void testSetSelectedAttributeReturnedPage() throws Exception {
        final String content = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  document.getElementById('iframe').src = 'about:blank';\n"
            + "}\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<select name='select1' size='1' id='mySelect' onchange='test()'>\n"
            + "<option value='option1'>option 1</option>\n"
            + "<option value='option2'>option 2</option>\n"
            + "</select>\n"
            + "</form>\n"
            + "<iframe id='iframe' src='about:blank'></iframe>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);

        final HtmlSelect select = page.getHtmlElementById("mySelect");
        final HtmlOption option = select.getOptionByValue("option2");
        final Page page2 = select.setSelectedAttribute(option, true);
        assertEquals(page, page2);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOnChangeResultPage() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<select name='select1' id='select1' onchange='location=\"about:blank\"'>\n"
            + "     <option id='option1'>Option1</option>\n"
            + "     <option id='option2' selected>Number Two</option>\n"
            + "</select>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        final HtmlPage page2 = option1.click();
        assertEquals("about:blank", page2.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onChange_resultPage_newCurrentWindow() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<select name='select1' id='select1' onchange='window.open(\"about:blank\", \"_blank\")'>\n"
            + "     <option id='option1'>Option1</option>\n"
            + "     <option id='option2' selected>Number Two</option>\n"
            + "</select>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlSelect select = page.getHtmlElementById("select1");
        final HtmlOption option1 = page.getHtmlElementById("option1");
        final HtmlPage page2 = select.setSelectedAttribute(option1, true);
        assertEquals("about:blank", page2.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_size() throws Exception {
        final String content = "<html><head><title>foo</title></head>\n"
            + "<body>\n"
            + "<select/>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        assertEquals(-1, page.asXml().indexOf("size"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void select_focus() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<select name='select1' id='select1' multiple onfocus='alert(\"focus\")'>\n"
            + "<option value='option1'>Option1</option>\n"
            + "<option value='option2'>Option2</option>\n"
            + "<option value='option3' selected>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals(Collections.emptyList(), collectedAlerts);

        final HtmlSelect select = page.getHtmlElementById("select1");
        assertNotSame(select, page.getFocusedElement());
        select.getOption(0).setSelected(true);
        assertSame(select, page.getFocusedElement());

        final String[] expectedAlerts = {"focus"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetOptionByText() throws Exception {
        final String html = "<html><head><title>foo</title></head><body><form id='form1'>\n"
            + "<select name='select1'>\n"
            + "    <option value='option1'>s1o1</option>\n"
            + "    <option value='option2'>s1o2</option>\n"
            + "</select>\n"
            + "<select name='select2'>\n"
            + "    <option value='option1'>s2o1</option>\n"
            + "    <option value='option2'>s2o2</option>\n"
            + "    <option>s2o3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSelect select = form.getSelectsByName("select2").get(0);
        assertEquals("s2o2", select.getOptionByText("s2o2").asText());

        assertEquals(select.getOption(2), select.getOptionByText("s2o3"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void savePageSavesSelectedOption() throws Exception {
        final String content = "<html><body>\n"
            + "<form action=''>\n"
            + "  <select id='main'>\n"
            + "    <option value='1'>option 1</option>\n"
            + "    <option value='2'>option 2</option>\n"
            + "    <option value='3' selected>option 3</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "<script>\n"
            + "var oSelect = document.getElementById('main');\n"
            + "oSelect.options[1].selected = true;\n"
            + "alert(oSelect.options[1].getAttribute('selected'));\n"
            + "</script>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlSelect select = (HtmlSelect) page.getElementById("main");
        assertEquals("option 2", select.getSelectedOptions().get(0).getText());

        // save the file and reload it
        final File file = new File(tmpFolderProvider_.newFolder("tmp"), "test.html");
        page.save(file);
        final String html2 = FileUtils.readFileToString(file, "UTF-8");
        final HtmlPage page2 = loadPage(html2);
        final HtmlSelect select2 = (HtmlSelect) page2.getElementById("main");
        assertEquals("option 2", select2.getSelectedOptions().get(0).getText());
    }
}
