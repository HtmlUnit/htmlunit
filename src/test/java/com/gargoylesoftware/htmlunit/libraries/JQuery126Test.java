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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with version 1.2.6 of the <a href="http://jquery.com/">jQuery JavaScript library</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class JQuery126Test extends WebTestCase {

    private static WebClient CLLIENT_;
    private static Iterator<HtmlElement> ITERATOR_;
    private static BrowserVersion BROWSER_VERSION_;
    private HtmlListItem listItem_;
    private int itemIndex_;

    /**
     * Before.
     */
    @Before
    public void init() {
        if (getBrowserVersion() != BROWSER_VERSION_) {
            try {
                ITERATOR_ = loadPage();
                BROWSER_VERSION_ = getBrowserVersion();
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        listItem_ = (HtmlListItem) ITERATOR_.next();
        itemIndex_ = 0;
    }

    @SuppressWarnings("unchecked")
    private void assertResult(final String expectedTestResult) {
        final String actual = ((HtmlElement) ((List) listItem_.getByXPath("./strong")).get(0)).asText();
        assertEquals(expectedTestResult, actual);
    }

    @SuppressWarnings("unchecked")
    private void assertAssertion(final String expectedAssertion) {
        final String actual = ((HtmlListItem) ((List) listItem_.getByXPath("./ol/li")).get(itemIndex_++)).asText();
        assertEquals(expectedAssertion, actual);
    }

    /**
     * Loads the jQuery unit test index page using the specified browser version, allows its
     * JavaScript to run to completion, and returns a list item iterator containing the test
     * results.
     *
     * @return a list item iterator containing the test results
     * @throws Exception if an error occurs
     */
    protected Iterator<HtmlElement> loadPage() throws Exception {
        final String resource = "jquery/" + getVersion() + "/test/index.html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        if (CLLIENT_ != null) {
            CLLIENT_.closeAllWindows();
            System.gc();
        }
        CLLIENT_ = getWebClient();

        final HtmlPage page = CLLIENT_.getPage(url);
        page.getEnclosingWindow().getThreadManager().joinAll(2 * 60 * 1000);

        // dump the result page if not OK
        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null) {
            final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            final File f = new File(tmpDir,
                "jquery" + getVersion() + '_' + getBrowserVersion().getNickname() + "_result.html");
            FileUtils.writeStringToFile(f, page.asXml(), "UTF-8");
            getLog().info("Test result for "
                + getVersion() + '_' + getBrowserVersion().getNickname() + " written to: " + f.getAbsolutePath());
        }

        final HtmlElement doc = page.getDocumentElement();
        final HtmlOrderedList tests = (HtmlOrderedList) doc.getHtmlElementById("tests");
        final Iterable<HtmlElement> i = tests.getChildElements();
        return i.iterator();
    }

    /**
     * {@inheritDoc}
     */
    protected String getVersion() {
        return "1.2.6";
    }

    /**
     * Test.
     */
    @Test
    public void core_module__Basic_requirements() {
        assertResult("core module: Basic requirements (0, 7, 7)");
        assertAssertion("Array.push()");
        assertAssertion("Function.apply()");
        assertAssertion("getElementById");
        assertAssertion("getElementsByTagName");
        assertAssertion("RegExp");
        assertAssertion("jQuery");
        assertAssertion("$()");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__$__() {
        assertResult("core module: $() (0, 8, 8)");
        assertAssertion("Basic selector with jQuery object as context");
        assertAssertion("Correct number of elements generated for code: 1");
        assertAssertion("Correct number of elements generated for img: 1");
        assertAssertion("Correct number of elements generated for div hr code b: 4");
        assertAssertion("Correct number of elements generated for window: 1");
        assertAssertion("Correct number of elements generated for document: 1");
        assertAssertion("Test passing an array to the factory: 2");
        assertAssertion("Test passing an html node to the factory: [object]");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__browser() {
        assertResult("core module: browser (0, 13, 13)");
        assertAssertion("Checking UA string: 6.0");
        assertAssertion("Checking UA string: 7.0");
        assertAssertion("Checking UA string: 1.7.12");
        assertAssertion("Checking UA string: 1.8.1.3");
        assertAssertion("Checking UA string: 1.7.5");
        assertAssertion("Checking UA string: 1.8.0.11");
        assertAssertion("Checking UA string: 9.20");
        assertAssertion("Checking UA string: 9.20");
        assertAssertion("Checking UA string: 9.20");
        assertAssertion("Checking UA string: 418.9");
        assertAssertion("Checking UA string: 418.8");
        assertAssertion("Checking UA string: 312.8");
        assertAssertion("Checking UA string: null");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__noConflict() {
        assertResult("core module: noConflict (0, 6, 6)");
        assertAssertion(
            "noConflict returned the jQuery object: function( selector, context ) { "
            + "// The jQuery object is actually just the init constructor 'enhanced' "
            + "return new jQuery.fn.init( selector, context ); }");
        assertAssertion("Make sure jQuery wasn't touched.: function( selector, context ) { // The jQuery object is "
            + "actually just the init constructor 'enhanced' return new jQuery.fn.init( selector, context ); }");
        assertAssertion("Make sure $ was reverted.: $");
        assertAssertion("noConflict returned the jQuery object: function( selector, context ) { // The jQuery object "
            + "is actually just the init constructor 'enhanced' return new jQuery.fn.init( selector, context ); }");
        assertAssertion("Make sure jQuery was reverted.: jQuery");
        assertAssertion("Make sure $ was reverted.: $");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__isFunction() {
        assertResult("core module: isFunction (0, 21, 21)");
        assertAssertion("No Value");
        assertAssertion("null Value");
        assertAssertion("undefined Value");
        assertAssertion("Empty String Value");
        assertAssertion("0 Value");
        assertAssertion("String Function( function String() { [native code] } )");
        assertAssertion("Array Function( function Array() { [native code] } )");
        assertAssertion("Object Function( function Object() { [native code] } )");
        assertAssertion("Function Function( function Function() { [native code] } )");
        assertAssertion("Function String");
        assertAssertion("Function Array");
        assertAssertion("Function Object");
        assertAssertion("Normal Function");
        assertAssertion("Object Element");
        assertAssertion("getAttribute Function");
        assertAssertion("childNodes Property");
        assertAssertion("A normal DOM Element");
        assertAssertion("A default function property");
        assertAssertion("Anchor Element");
        assertAssertion("Recursive Function Call");
        assertAssertion("Recursive Function Call");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__$__html__() {
        assertResult("core module: $('html') (0, 6, 6)");
        assertAssertion("Creating a script");
        assertAssertion("Make sure the script wasn't executed prematurely");
        assertAssertion("Executing a scripts contents in the right context");
        assertAssertion("Creating a link");
        assertAssertion("Check node,textnode,comment creation (some browsers delete comments)");
        assertAssertion("Make sure that options are auto-selected #2050");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__$__html___context_() {
        assertResult("core module: $('html', context) (0, 1, 1)");
        assertAssertion("Verify a span created with a div context works, #1763: 1");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__length() {
        assertResult("core module: length (0, 1, 1)");
        assertAssertion("Get Number of Elements Found: 6");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__size__() {
        assertResult("core module: size() (0, 1, 1)");
        assertAssertion("Get Number of Elements Found: 6");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__get__() {
        assertResult("core module: get() (0, 1, 1)");
        assertAssertion("Get All Elements");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__get_Number_() {
        assertResult("core module: get(Number) (0, 1, 1)");
        assertAssertion("Get A Single Element: [object]");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__add_String_Element_Array_undefined_() {
        assertResult("core module: add(String|Element|Array|undefined) (0, 12, 12)");
        assertAssertion("Check elements from document");
        assertAssertion("Check elements from document");
        assertAssertion("Check elements from array");
        assertAssertion("Check on-the-fly element1: x1");
        assertAssertion("Check on-the-fly element2: x2");
        assertAssertion("Check on-the-fly element1: x1");
        assertAssertion("Check on-the-fly element2: x2");
        assertAssertion("Check that undefined adds nothing: 0");
        assertAssertion("Pass an array: 3");
        assertAssertion("Check duplicated elements: 1");
        assertAssertion("Check duplicated elements using the window: 1");
        assertAssertion("Add a form (adds the elements)");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__each_Function_() {
        assertResult("core module: each(Function) (0, 1, 1)");
        assertAssertion("Execute a function, Relative");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__index_Object_() {
        assertResult("core module: index(Object) (0, 10, 10)");
        assertAssertion("Check for index of elements: 0");
        assertAssertion("Check for index of elements: 1");
        assertAssertion("Check for index of elements: 0");
        assertAssertion("Check for index of elements: 1");
        assertAssertion("Check for index of elements: 2");
        assertAssertion("Check for index of elements: 3");
        assertAssertion("Check for not found index: -1");
        assertAssertion("Check for not found index: -1");
        assertAssertion("Pass in a jQuery object: 0");
        assertAssertion("Pass in a jQuery object: 1");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__attr_String_() {
        assertResult("core module: attr(String) (0, 26, 26)");
        assertAssertion("Check for value attribute: Test");
        assertAssertion("Check for defaultValue attribute: Test");
        assertAssertion("Check for type attribute: text");
        assertAssertion("Check for type attribute: radio");
        assertAssertion("Check for type attribute: checkbox");
        assertAssertion("Check for rel attribute: bookmark");
        assertAssertion("Check for title attribute: Google!");
        assertAssertion("Check for hreflang attribute: en");
        assertAssertion("Check for lang attribute: en");
        assertAssertion("Check for class attribute: blog link");
        assertAssertion("Check for name attribute: name");
        assertAssertion("Check for name attribute: action");
        assertAssertion("Check for action attribute");
        assertAssertion("Check for maxlength attribute: 30");
        assertAssertion("Check for maxLength attribute: 30");
        assertAssertion("Check for maxLength attribute: 30");
        assertAssertion("Check for selectedIndex attribute: 3");
        assertAssertion("Check for nodeName attribute: DIV");
        assertAssertion("Check for tagName attribute: DIV");
        assertAssertion("Check for non-absolute href (an anchor): #5");
        assertAssertion("Make sure that a non existent attribute returns undefined");
        assertAssertion("Make sure a null expando returns null");
        assertAssertion("Make sure the dom attribute is retrieved when no expando is found: baz");
        assertAssertion("Make sure the expando is preferred over the dom attribute: bar");
        assertAssertion("Make sure that setting works well when both expando and dom attribute are available: cool");
        assertAssertion("Make sure the expando is preferred over the dom attribute, even if undefined");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__attr_String__Function_() {
        assertResult("core module: attr(String, Function) (0, 2, 2)");
        assertAssertion("Set value from id: text1");
        assertAssertion("Set value with an index: 0");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__attr_Hash_() {
        assertResult("core module: attr(Hash) (0, 1, 1)");
        assertAssertion("Set Multiple Attributes");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__attr_String__Object_() {
        assertResult("core module: attr(String, Object) (0, 17, 17)");
        assertAssertion("Set Attribute, the #false element didn't get the attribute 'foo': false");
        assertAssertion("Try to set an attribute to nothing");
        assertAssertion("Set name attribute: something");
        assertAssertion("Set checked attribute: true");
        assertAssertion("Set checked attribute: false");
        assertAssertion("Set readonly attribute: true");
        assertAssertion("Set readonly attribute: false");
        assertAssertion("Set maxlength attribute: 5");
        assertAssertion("Set maxlength attribute: 10");
        assertAssertion("Set attribute to a string of \"0\": 0");
        assertAssertion("Set attribute to the number 0: 0");
        assertAssertion("Set attribute to the number 1: 1");
        assertAssertion("Check node,textnode,comment for attr: attrvalue");
        assertAssertion("Exception thrown when trying to change type property");
        assertAssertion("Verify that you can't change the type of an input element: checkbox");
        assertAssertion("Exception thrown when trying to change type property");
        assertAssertion("Verify that you can change the type of an input element that isn't in the DOM: checkbox");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__css_String_Hash_() {
        assertResult("core module: css(String|Hash) (0, 19, 19)");
        assertAssertion("Check for css property \"display\": none");
        assertAssertion("Modifying CSS display: Assert element is visible");
        assertAssertion("Modified CSS display: Assert element is hidden");
        assertAssertion("Modified CSS display: Assert element is visible");
        assertAssertion("Modified CSS float using \"styleFloat\": Assert float is right: right");
        assertAssertion("Modified CSS float using \"cssFloat\": Assert float is left: left");
        assertAssertion("Modified CSS float using \"float\": Assert float is right: right");
        assertAssertion("Modified CSS font-size: Assert font-size is 30px: 30px");
        assertAssertion("Assert opacity is 0 as a String: 0");
        assertAssertion("Assert opacity is 0 as a Number: 0");
        assertAssertion("Assert opacity is 0.25 as a String: 0.25");
        assertAssertion("Assert opacity is 0.25 as a Number: 0.25");
        assertAssertion("Assert opacity is 0.5 as a String: 0.5");
        assertAssertion("Assert opacity is 0.5 as a Number: 0.5");
        assertAssertion("Assert opacity is 0.75 as a String: 0.75");
        assertAssertion("Assert opacity is 0.75 as a Number: 0.75");
        assertAssertion("Assert opacity is 1 as a String: 1");
        assertAssertion("Assert opacity is 1 as a Number: 1");
        assertAssertion("Assert opacity is 1 when set to an empty String: 1");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__css_String__Object_() {
        assertResult("core module: css(String, Object) (0, 21, 21)");
        assertAssertion("Modifying CSS display: Assert element is visible");
        assertAssertion("Modified CSS display: Assert element is hidden");
        assertAssertion("Modified CSS display: Assert element is visible");
        assertAssertion("Modified CSS float using \"styleFloat\": Assert float is left: left");
        assertAssertion("Modified CSS float using \"cssFloat\": Assert float is right: right");
        assertAssertion("Modified CSS float using \"float\": Assert float is left: left");
        assertAssertion("Modified CSS font-size: Assert font-size is 20px: 20px");
        assertAssertion("Assert opacity is 0 as a String: 0");
        assertAssertion("Assert opacity is 0 as a Number: 0");
        assertAssertion("Assert opacity is 0.25 as a String: 0.25");
        assertAssertion("Assert opacity is 0.25 as a Number: 0.25");
        assertAssertion("Assert opacity is 0.5 as a String: 0.5");
        assertAssertion("Assert opacity is 0.5 as a Number: 0.5");
        assertAssertion("Assert opacity is 0.75 as a String: 0.75");
        assertAssertion("Assert opacity is 0.75 as a Number: 0.75");
        assertAssertion("Assert opacity is 1 as a String: 1");
        assertAssertion("Assert opacity is 1 as a Number: 1");
        assertAssertion("Assert opacity is 1 when set to an empty String: 1");
        assertAssertion("Assert opacity is 1 when a different filter is set in IE, #1438: 1");
        assertAssertion("Check node,textnode,comment css works: 1px");
        assertAssertion("Make sure browser thinks it is hidden: none");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__jQuery_css_elem___height___doesn_t_clear_radio_buttons__bug__1095_() {
        assertResult("core module: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095) (0, 4, 4)");
        assertAssertion("Check first radio still checked.");
        assertAssertion("Check last radio still NOT checked.");
        assertAssertion("Check first checkbox still checked.");
        assertAssertion("Check last checkbox still NOT checked.");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__width__() {
        assertResult("core module: width() (0, 9, 9)");
        assertAssertion("Test set to 30 correctly: 30");
        assertAssertion("Test negative width ignored: 30");
        assertAssertion("Test padding specified with pixels: 30");
        assertAssertion("Test border specified with pixels: 30");
        assertAssertion("Test padding specified with ems: 30");
        assertAssertion("Test border specified with ems: 30");
        assertAssertion("Test padding specified with percent: 30");
        assertAssertion("Test hidden div: 30");
        assertAssertion("Test child width with border and padding: 20");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__height__() {
        assertResult("core module: height() (0, 8, 8)");
        assertAssertion("Test set to 30 correctly: 30");
        assertAssertion("Test negative height ignored: 30");
        assertAssertion("Test padding specified with pixels: 30");
        assertAssertion("Test border specified with pixels: 30");
        assertAssertion("Test padding specified with ems: 30");
        assertAssertion("Test border specified with ems: 30");
        assertAssertion("Test padding specified with percent: 30");
        assertAssertion("Test hidden div: 30");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__text__() {
        assertResult("core module: text() (0, 1, 1)");
        assertAssertion("Check for merged text of more then one element.: This link has class=\"blog\": "
            + "Simon Willison's Weblog");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__wrap_String_Element_() {
        assertResult("core module: wrap(String|Element) (0, 8, 8)");
        assertAssertion("Check for wrapping of on-the-fly html: Try them out:");
        assertAssertion("Check if wrapper has class \"red\"");
        assertAssertion("Check for element wrapping");
        assertAssertion("Check for element wrapping: Try them out:");
        assertAssertion("Checkbox's state is erased after wrap() action, see #769");
        assertAssertion("Checkbox's state is erased after wrap() action, see #769");
        assertAssertion("Check node,textnode,comment wraps ok: 3");
        assertAssertion("Check node,textnode,comment wraps doesn't hurt text: hi there ");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__wrapAll_String_Element_() {
        assertResult("core module: wrapAll(String|Element) (0, 8, 8)");
        assertAssertion("Check for wrapping of on-the-fly html: 1");
        assertAssertion("Check if wrapper has class \"red\"");
        assertAssertion("Check if wrapper has class \"red\"");
        assertAssertion("Correct Previous Sibling: [object]");
        assertAssertion("Correct Parent: [object]");
        assertAssertion("Same Parent: [object]");
        assertAssertion("Correct Previous Sibling: [object]");
        assertAssertion("Correct Parent: [object]");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__wrapInner_String_Element_() {
        assertResult("core module: wrapInner(String|Element) (0, 6, 6)");
        assertAssertion("Only one child: 1");
        assertAssertion("Verify Right Element");
        assertAssertion("Verify Elements Intact: 0");
        assertAssertion("Only one child: 1");
        assertAssertion("Verify Right Element");
        assertAssertion("Verify Elements Intact: 0");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__append_String_Element_Array_Element__jQuery_() {
        assertResult("core module: append(String|Element|Array<Element>|jQuery) (0, 21, 21)");
        assertAssertion("Check if text appending works: Try them out:buga");
        assertAssertion("Appending html options to select element: appendTest");
        assertAssertion(
            "Check for appending of element: This link has class=\"blog\": Simon Willison's WeblogTry them out:");
        assertAssertion("Check for appending of array of elements: This link has class=\"blog\": "
            + "Simon Willison's WeblogTry them out:Yahoo");
        assertAssertion("Check for appending of jQuery object: This link has class=\"blog\": "
            + "Simon Willison's WeblogTry them out:Yahoo");
        assertAssertion("Check for appending a number");
        assertAssertion("Check for appending text with spaces");
        assertAssertion("Check for appending an empty array.");
        assertAssertion("Check for appending an empty string.");
        assertAssertion("Check for appending an empty nodelist.");
        assertAssertion("Check for appending a form: 1");
        assertAssertion("Test for appending a DOM node to the contents of an IFrame");
        assertAssertion("Append legend (#legend)");
        assertAssertion("Appending <OPTION> (all caps): Test");
        assertAssertion("Append colgroup");
        assertAssertion("Append col");
        assertAssertion("Append caption");
        assertAssertion("Append Select (#appendSelect1, #appendSelect2)");
        assertAssertion("Check node,textnode,comment append moved leaving just the div: 1");
        assertAssertion("Check node,textnode,comment append works");
        assertAssertion("Check node,textnode,comment append cleanup worked");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__appendTo_String_Element_Array_Element__jQuery_() {
        assertResult("core module: appendTo(String|Element|Array<Element>|jQuery) (0, 6, 6)");
        assertAssertion("Check if text appending works: Try them out:buga");
        assertAssertion("Appending html options to select element: appendTest");
        assertAssertion("Check for appending of element: This link has class=\"blog\": "
            + "Simon Willison's WeblogTry them out:");
        assertAssertion("Check for appending of array of elements: This link has class=\"blog\": "
            + "Simon Willison's WeblogTry them out:Yahoo");
        assertAssertion("Check for appending of jQuery object: This link has class=\"blog\": "
            + "Simon Willison's WeblogTry them out:Yahoo");
        assertAssertion("Append select (#foo select)");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__prepend_String_Element_Array_Element__jQuery_() {
        assertResult("core module: prepend(String|Element|Array<Element>|jQuery) (0, 5, 5)");
        assertAssertion("Check if text prepending works: bugaTry them out:");
        assertAssertion("Prepending html options to select element: prependTest");
        assertAssertion("Check for prepending of element: Try them out:This link has class=\"blog\": "
            + "Simon Willison's Weblog");
        assertAssertion("Check for prepending of array of elements: Try them out:YahooThis link has class=\"blog\": "
            + "Simon Willison's Weblog");
        assertAssertion("Check for prepending of jQuery object: Try them out:YahooThis link has class=\"blog\": "
            + "Simon Willison's Weblog");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__prependTo_String_Element_Array_Element__jQuery_() {
        assertResult("core module: prependTo(String|Element|Array<Element>|jQuery) (0, 6, 6)");
        assertAssertion("Check if text prepending works: bugaTry them out:");
        assertAssertion("Prepending html options to select element: prependTest");
        assertAssertion("Check for prepending of element: Try them out:This link has class=\"blog\": "
            + "Simon Willison's Weblog");
        assertAssertion("Check for prepending of array of elements: Try them out:YahooThis link has class=\"blog\": "
            + "Simon Willison's Weblog");
        assertAssertion("Check for prepending of jQuery object: Try them out:YahooThis link has class=\"blog\": "
            + "Simon Willison's Weblog");
        assertAssertion("Prepend Select (#prependSelect1, #prependSelect2)");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__before_String_Element_Array_Element__jQuery_() {
        assertResult("core module: before(String|Element|Array<Element>|jQuery) (0, 4, 4)");
        assertAssertion("Insert String before: This is a normal link: bugaYahoo");
        assertAssertion("Insert element before: This is a normal link: Try them out:Yahoo");
        assertAssertion("Insert array of elements before: This is a normal link: Try them out:diveintomarkYahoo");
        assertAssertion("Insert jQuery before: This is a normal link: Try them out:diveintomarkYahoo");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__insertBefore_String_Element_Array_Element__jQuery_() {
        assertResult("core module: insertBefore(String|Element|Array<Element>|jQuery) (0, 4, 4)");
        assertAssertion("Insert String before: This is a normal link: bugaYahoo");
        assertAssertion("Insert element before: This is a normal link: Try them out:Yahoo");
        assertAssertion("Insert array of elements before: This is a normal link: Try them out:diveintomarkYahoo");
        assertAssertion("Insert jQuery before: This is a normal link: Try them out:diveintomarkYahoo");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__after_String_Element_Array_Element__jQuery_() {
        assertResult("core module: after(String|Element|Array<Element>|jQuery) (0, 4, 4)");
        assertAssertion("Insert String after: This is a normal link: Yahoobuga");
        assertAssertion("Insert element after: This is a normal link: YahooTry them out:");
        assertAssertion("Insert array of elements after: This is a normal link: YahooTry them out:diveintomark");
        assertAssertion("Insert jQuery after: This is a normal link: YahooTry them out:diveintomark");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__insertAfter_String_Element_Array_Element__jQuery_() {
        assertResult("core module: insertAfter(String|Element|Array<Element>|jQuery) (0, 4, 4)");
        assertAssertion("Insert String after: This is a normal link: Yahoobuga");
        assertAssertion("Insert element after: This is a normal link: YahooTry them out:");
        assertAssertion("Insert array of elements after: This is a normal link: YahooTry them out:diveintomark");
        assertAssertion("Insert jQuery after: This is a normal link: YahooTry them out:diveintomark");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__replaceWith_String_Element_Array_Element__jQuery_() {
        assertResult("core module: replaceWith(String|Element|Array<Element>|jQuery) (0, 10, 10)");
        assertAssertion("Replace element with string");
        assertAssertion("Verify that original element is gone, after string");
        assertAssertion("Replace element with element");
        assertAssertion("Verify that original element is gone, after element");
        assertAssertion("Replace element with array of elements");
        assertAssertion("Replace element with array of elements");
        assertAssertion("Verify that original element is gone, after array of elements");
        assertAssertion("Replace element with set of elements");
        assertAssertion("Replace element with set of elements");
        assertAssertion("Verify that original element is gone, after set of elements");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__replaceAll_String_Element_Array_Element__jQuery_() {
        assertResult("core module: replaceAll(String|Element|Array<Element>|jQuery) (0, 10, 10)");
        assertAssertion("Replace element with string");
        assertAssertion("Verify that original element is gone, after string");
        assertAssertion("Replace element with element");
        assertAssertion("Verify that original element is gone, after element");
        assertAssertion("Replace element with array of elements");
        assertAssertion("Replace element with array of elements");
        assertAssertion("Verify that original element is gone, after array of elements");
        assertAssertion("Replace element with set of elements");
        assertAssertion("Replace element with set of elements");
        assertAssertion("Verify that original element is gone, after set of elements");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__end__() {
        assertResult("core module: end() (0, 3, 3)");
        assertAssertion("Check for end: Yahoo");
        assertAssertion("Check for end with nothing to end");
        assertAssertion("Check for non-destructive behaviour: Yahoo");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__find_String_() {
        assertResult("core module: find(String) (0, 2, 2)");
        assertAssertion("Check for find: Yahoo");
        assertAssertion("Check node,textnode,comment to find zero divs: 0");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__clone__() {
        assertResult("core module: clone() (0, 20, 20)");
        assertAssertion("Assert text for #en: This is a normal link: Yahoo");
        assertAssertion("Check for clone: Try them out:Yahoo");
        assertAssertion("Reassert text for #en: This is a normal link: Yahoo");
        assertAssertion("Clone a <table/>: TABLE");
        assertAssertion("Clone a <tr/>: TR");
        assertAssertion("Clone a <td/>: TD");
        assertAssertion("Clone a <div/>: DIV");
        assertAssertion("Clone a <button/>: BUTTON");
        assertAssertion("Clone a <ul/>: UL");
        assertAssertion("Clone a <ol/>: OL");
        assertAssertion("Clone a <li/>: LI");
        assertAssertion("Clone a <input type='checkbox' />: INPUT");
        assertAssertion("Clone a <select/>: SELECT");
        assertAssertion("Clone a <option/>: OPTION");
        assertAssertion("Clone a <textarea/>: TEXTAREA");
        assertAssertion("Clone a <tbody/>: TBODY");
        assertAssertion("Clone a <thead/>: THEAD");
        assertAssertion("Clone a <tfoot/>: TFOOT");
        assertAssertion("Clone a <iframe/>: IFRAME");
        assertAssertion("Check node,textnode,comment clone works (some browsers delete comments on clone)");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__is_String_() {
        assertResult("core module: is(String) (0, 26, 26)");
        assertAssertion("Check for element: A form must be a form");
        assertAssertion("Check for element: A form is not a div");
        assertAssertion("Check for class: Expected class \"blog\"");
        assertAssertion("Check for class: Did not expect class \"link\"");
        assertAssertion("Check for multiple classes: Expected classes \"blog\" and \"link\"");
        assertAssertion("Check for multiple classes: Expected classes \"blog\" and \"link\", but not \"blogTest\"");
        assertAssertion("Check for attribute: Expected attribute lang to be \"en\"");
        assertAssertion("Check for attribute: Expected attribute lang to be \"en\", not \"de\"");
        assertAssertion("Check for attribute: Expected attribute type to be \"text\"");
        assertAssertion("Check for attribute: Expected attribute type to be \"text\", not \"radio\"");
        assertAssertion("Check for pseudoclass: Expected to be disabled");
        assertAssertion("Check for pseudoclass: Expected not disabled");
        assertAssertion("Check for pseudoclass: Expected to be checked");
        assertAssertion("Check for pseudoclass: Expected not checked");
        assertAssertion("Check for child: Expected a child \"p\" element");
        assertAssertion("Check for child: Did not expect \"ul\" element");
        assertAssertion("Check for childs: Expected \"p\", \"a\" and \"code\" child elements");
        assertAssertion("Check for childs: Expected \"p\", \"a\" and \"code\" child elements, but no \"ol\"");
        assertAssertion("Expected false for an invalid expression - 0");
        assertAssertion("Expected false for an invalid expression - null");
        assertAssertion("Expected false for an invalid expression - \"\"");
        assertAssertion("Expected false for an invalid expression - undefined");
        assertAssertion("Comma-seperated; Check for lang attribute: Expect en or de");
        assertAssertion("Comma-seperated; Check for lang attribute: Expect en or de");
        assertAssertion("Comma-seperated; Check for lang attribute: Expect en or de");
        assertAssertion("Comma-seperated; Check for lang attribute: Expect en or de");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__$_extend_Object__Object_() {
        assertResult("core module: $.extend(Object, Object) (0, 20, 20)");
        assertAssertion("Check if extended: settings must be extended");
        assertAssertion("Check if not modified: options must not be modified");
        assertAssertion("Check if extended: settings must be extended");
        assertAssertion("Check if not modified: options must not be modified");
        assertAssertion("Check if foo: settings must be extended");
        assertAssertion("Check if not deep2: options must not be modified");
        assertAssertion("Make sure that a deep clone was not attempted on the document: [object]");
        assertAssertion("Check to make sure null values are copied");
        assertAssertion("Check to make sure undefined values are not copied");
        assertAssertion("Check to make sure null values are inserted");
        assertAssertion("Check to make sure a recursive obj doesn't go never-ending loop by not copying it over");
        assertAssertion("Check to make sure a value with coersion 'false' copies over when necessary to fix #1907: 1");
        assertAssertion("Check to make sure values equal with coersion (but not actually equal) overwrite correctly");
        assertAssertion("Make sure a null value doesn't crash with deep extend, for #1908");
        assertAssertion("Make sure a null value can be overwritten: notnull");
        assertAssertion("Verify a function can be extended: value");
        assertAssertion("Check if extended: settings must be extended");
        assertAssertion("Check if not modified: options1 must not be modified");
        assertAssertion("Check if not modified: options1 must not be modified");
        assertAssertion("Check if not modified: options2 must not be modified");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__val__() {
        assertResult("core module: val() (0, 4, 4)");
        assertAssertion("Check for value of input element: Test");
        assertAssertion("Check for value of input element: ");
        assertAssertion("Check a paragraph element to see if it has a value: ");
        assertAssertion("Check an empty jQuery object will return undefined from val");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__val_String_() {
        assertResult("core module: val(String) (0, 4, 4)");
        assertAssertion("Check for modified value of input element: bla");
        assertAssertion("Check for modified (via val(String)) value of input element");
        assertAssertion("Check for modified (via val(String)) value of select element: 3");
        assertAssertion("Check node,textnode,comment with val(): asdf");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__html_String_() {
        assertResult("core module: html(String) (0, 11, 11)");
        assertAssertion("Set HTML");
        assertAssertion("Check node,textnode,comment with html(): bold");
        assertAssertion("Selected option correct: O2");
        assertAssertion("$().html().evalScripts() Evals Scripts Twice in Firefox, see #975");
        assertAssertion("$().html().evalScripts() Evals Scripts Twice in Firefox, see #975");
        assertAssertion("Script is executed in order: 0");
        assertAssertion("Execute after html (even though appears before): 1");
        assertAssertion("Script (nested) is executed in order: 1");
        assertAssertion("Execute after html: 1");
        assertAssertion("Script (unnested) is executed in order: 2");
        assertAssertion("Execute after html: 1");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__filter__() {
        assertResult("core module: filter() (0, 6, 6)");
        assertAssertion("filter(String)");
        assertAssertion("filter('String, String')");
        assertAssertion("filter('String,String')");
        assertAssertion("filter(Function)");
        assertAssertion("Check node,textnode,comment to filter the one span: 1");
        assertAssertion("Check node,textnode,comment to filter the one span: 0");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__not__() {
        assertResult("core module: not() (0, 8, 8)");
        assertAssertion("not('selector'): 2");
        assertAssertion("not(DOMElement): 2");
        assertAssertion("not('.class')");
        assertAssertion("not('selector, selector')");
        assertAssertion("not(jQuery)");
        assertAssertion("not(Array-like DOM collection): 0");
        assertAssertion("not('complex selector')");
        assertAssertion("filter out DOM element");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__andSelf__() {
        assertResult("core module: andSelf() (0, 4, 4)");
        assertAssertion("Check for siblings and self");
        assertAssertion("Check for children and self");
        assertAssertion("Check for parent and self");
        assertAssertion("Check for parents and self");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__siblings__String__() {
        assertResult("core module: siblings([String]) (0, 5, 5)");
        assertAssertion("Check for siblings");
        assertAssertion("Check for filtered siblings (has code child element)");
        assertAssertion("Check for filtered siblings (has anchor child element)");
        assertAssertion("Check for multiple filters");
        assertAssertion("Check for unique results from siblings");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__children__String__() {
        assertResult("core module: children([String]) (0, 3, 3)");
        assertAssertion("Check for children");
        assertAssertion("Check for filtered children");
        assertAssertion("Check for multiple filters");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__parent__String__() {
        assertResult("core module: parent([String]) (0, 5, 5)");
        assertAssertion("Simple parent check: ap");
        assertAssertion("Filtered parent check: ap");
        assertAssertion("Filtered parent check, no match: 0");
        assertAssertion("Check for multiple filters: ap");
        assertAssertion("Check for unique results from parent");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__parents__String__() {
        assertResult("core module: parents([String]) (0, 5, 5)");
        assertAssertion("Simple parents check: ap");
        assertAssertion("Filtered parents check: ap");
        assertAssertion("Filtered parents check2: main");
        assertAssertion("Check for multiple filters");
        assertAssertion("Check for unique results from parents");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__next__String__() {
        assertResult("core module: next([String]) (0, 4, 4)");
        assertAssertion("Simple next check: foo");
        assertAssertion("Filtered next check: foo");
        assertAssertion("Filtered next check, no match: 0");
        assertAssertion("Multiple filters: foo");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__prev__String__() {
        assertResult("core module: prev([String]) (0, 4, 4)");
        assertAssertion("Simple prev check: ap");
        assertAssertion("Filtered prev check: ap");
        assertAssertion("Filtered prev check, no match: 0");
        assertAssertion("Multiple filters: ap");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__show__() {
        assertResult("core module: show() (0, 15, 15)");
        assertAssertion("Show");
        assertAssertion("Show using correct display type for div: block");
        assertAssertion("Show using correct display type for p: block");
        assertAssertion("Show using correct display type for a: inline");
        assertAssertion("Show using correct display type for code: inline");
        assertAssertion("Show using correct display type for pre: block");
        assertAssertion("Show using correct display type for span: inline");
        assertAssertion("Show using correct display type for table: block");
        assertAssertion("Show using correct display type for thead: block");
        assertAssertion("Show using correct display type for tbody: block");
        assertAssertion("Show using correct display type for tr: block");
        assertAssertion("Show using correct display type for th: block");
        assertAssertion("Show using correct display type for td: block");
        assertAssertion("Show using correct display type for ul: block");
        assertAssertion("Show using correct display type for li: block");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__addClass_String_() {
        assertResult("core module: addClass(String) (0, 2, 2)");
        assertAssertion("Add Class");
        assertAssertion("Check node,textnode,comment for addClass");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__removeClass_String____simple() {
        assertResult("core module: removeClass(String) - simple (0, 4, 4)");
        assertAssertion("Remove Class");
        assertAssertion("Remove multiple classes");
        assertAssertion("Empty string passed to removeClass");
        assertAssertion("Check node,textnode,comment for removeClass");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__toggleClass_String_() {
        assertResult("core module: toggleClass(String) (0, 3, 3)");
        assertAssertion("Assert class not present");
        assertAssertion("Assert class present");
        assertAssertion("Assert class not present");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__removeAttr_String() {
        assertResult("core module: removeAttr(String (0, 1, 1)");
        assertAssertion("remove class: ");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__text_String_() {
        assertResult("core module: text(String) (0, 4, 4)");
        assertAssertion("Check escaped text: <div><b>Hello</b> cruel world!</div>");
        assertAssertion("Check node,textnode,comment with text(): hi!");
        assertAssertion("Check node,textnode,comment with text(): there ");
        assertAssertion("Check node,textnode,comment with text(): 8");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__$_each_Object_Function_() {
        assertResult("core module: $.each(Object,Function) (0, 12, 12)");
        assertAssertion("Check array iteration: 0");
        assertAssertion("Check array iteration: 1");
        assertAssertion("Check array iteration: 2");
        assertAssertion("Check array iteration: 0");
        assertAssertion("Check array iteration: 1");
        assertAssertion("Check array iteration: 2");
        assertAssertion("Check object iteration: name");
        assertAssertion("Check object iteration: lang");
        assertAssertion("Looping over an array: 6");
        assertAssertion("Looping over an array, with break: 3");
        assertAssertion("Looping over an object: 6");
        assertAssertion("Looping over an object, with break: 3");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__$_prop() {
        assertResult("core module: $.prop (0, 2, 2)");
        assertAssertion("Check with Function argument: ap");
        assertAssertion("Check with value argument: value");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__$_className() {
        assertResult("core module: $.className (0, 6, 6)");
        assertAssertion("Check single added class: hi");
        assertAssertion("Check more added classes: hi foo bar");
        assertAssertion("Remove all classes: ");
        assertAssertion("Check removal of one class: hi bar");
        assertAssertion("Check has1");
        assertAssertion("Check has2");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__$_data() {
        assertResult("core module: $.data (0, 5, 5)");
        assertAssertion("Check for no data exists: undefined");
        assertAssertion("Check for added data: success");
        assertAssertion("Check for overwritten data: overwritten");
        assertAssertion("Check that data wasn't removed: overwritten");
        assertAssertion("Check for null data");
    }

    /**
     * Test.
     */
    @Test
    public void core_module___data__() {
        assertResult("core module: .data() (0, 18, 18)");
        assertAssertion("Check for no data exists: undefined");
        assertAssertion("Check for added data: success");
        assertAssertion("Check for overwritten data: overwritten");
        assertAssertion("Check that data wasn't removed: overwritten");
        assertAssertion("Check for null data");
        assertAssertion("Check for original data: overwritten");
        assertAssertion("Check for namespaced data: 2");
        assertAssertion("Check for unmatched namespace: overwritten");
        assertAssertion("Check triggered setter functions: 2");
        assertAssertion("Check triggered getter functions: 5");
        assertAssertion("Check for original data: 1");
        assertAssertion("Check for namespaced data: 2");
        assertAssertion("Check for unmatched namespace: 1");
        assertAssertion("Check triggered setter functions: 1");
        assertAssertion("Check triggered getter functions: 5");
        assertAssertion("Check for original data: testroot");
        assertAssertion("Check for namespaced data: testfoo");
        assertAssertion("Check for unmatched namespace: testroot");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__$_removeData() {
        assertResult("core module: $.removeData (0, 1, 1)");
        assertAssertion("Check removal of data: undefined");
    }

    /**
     * Test.
     */
    @Test
    public void core_module___removeData__() {
        assertResult("core module: .removeData() (0, 6, 6)");
        assertAssertion("Check removal of data: undefined");
        assertAssertion("Make sure data is intact: testing2");
        assertAssertion("Make sure data is intact: testing");
        assertAssertion("Make sure data is intact: testing2");
        assertAssertion("Make sure data is intact: undefined");
        assertAssertion("Make sure data is intact: undefined");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__remove__() {
        assertResult("core module: remove() (0, 6, 6)");
        assertAssertion("Check text is not removed");
        assertAssertion("Check remove: 0");
        assertAssertion("Check text is not removed");
        assertAssertion("Check filtered remove: 1");
        assertAssertion("Check node,textnode,comment remove works: 3");
        assertAssertion("Check node,textnode,comment remove works: 0");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__empty__() {
        assertResult("core module: empty() (0, 3, 3)");
        assertAssertion("Check text is removed: 0");
        assertAssertion("Check elements are not removed: 4");
        assertAssertion("Check node,textnode,comment empty works: ");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__slice__() {
        assertResult("core module: slice() (0, 5, 5)");
        assertAssertion("slice(1,2)");
        assertAssertion("slice(1)");
        assertAssertion("slice(0,3)");
        assertAssertion("slice(-1)");
        assertAssertion("eq(1)");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__map__() {
        assertResult("core module: map() (0, 2, 2)");
        assertAssertion("Array Map");
        assertAssertion("Single Map");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__contents__() {
        assertResult("core module: contents() (0, 12, 12)");
        assertAssertion("Check element contents: 9");
        assertAssertion("Check existance of IFrame document");
        assertAssertion("Check existance of IFrame body");
        assertAssertion("Find span in IFrame and check its text: span text");
        assertAssertion("Check the original div and the new div are in IFrame: 2");
        assertAssertion("Add text to div in IFrame: init text");
        assertAssertion("Add text to div in IFrame: div text");
        assertAssertion("Delete the div and check only one div left in IFrame: 1");
        assertAssertion("Make sure the correct div is still left after deletion in IFrame: span text");
        assertAssertion("Check for JS error on add and delete of a table in IFrame: 1");
        assertAssertion("Check node,textnode,comment contents is just one: 1");
        assertAssertion("Check node,textnode,comment contents is just the one from span: hi");
    }

    /**
     * Test.
     */
    @Test
    public void core_module__$_makeArray() {
        assertResult("core module: $.makeArray (0, 15, 15)");
        assertAssertion("Pass makeArray a jQuery object: HEAD");
        assertAssertion("Pass makeArray a nodelist: PWD");
        assertAssertion("Pass makeArray an arguments array: 12");
        assertAssertion("Pass makeArray a real array: 123");
        assertAssertion("Pass nothing to makeArray and expect an empty array: 0");
        assertAssertion("Pass makeArray a number: 0");
        assertAssertion("Pass makeArray a string: foo");
        assertAssertion("Pass makeArray a boolean: function Boolean() { [native code] } ");
        assertAssertion("Pass makeArray a single node: DIV");
        assertAssertion("Pass makeArray an array like map (with length): ab");
        assertAssertion("Pass makeArray a childNodes array: HEAD");
        assertAssertion("Pass makeArray a function: 1");
        assertAssertion("Pass makeArray the window: [object]");
        assertAssertion("Pass makeArray a regex: function RegExp() { [native code] } ");
        assertAssertion("Pass makeArray a form (treat as elements)");
    }

    /**
     * Test.
     */
    @Test
    public void dimensions_module__innerWidth__() {
        assertResult("dimensions module: innerWidth() (0, 3, 3)");
        assertAssertion("Test with margin and border: 30");
        assertAssertion("Test with margin, border and padding: 70");
        assertAssertion("Test hidden div: 70");
    }

    /**
     * Test.
     */
    @Test
    public void dimensions_module__innerHeight__() {
        assertResult("dimensions module: innerHeight() (0, 3, 3)");
        assertAssertion("Test with margin and border: 30");
        assertAssertion("Test with margin, border and padding: 70");
        assertAssertion("Test hidden div: 70");
    }

    /**
     * Test.
     */
    @Test
    public void dimensions_module__outerWidth__() {
        assertResult("dimensions module: outerWidth() (0, 6, 6)");
        assertAssertion("Test with only width set: 30");
        assertAssertion("Test with padding: 70");
        assertAssertion("Test with padding and border: 74");
        assertAssertion("Test with padding, border and margin without margin option: 74");
        assertAssertion("Test with padding, border and margin with margin option: 94");
        assertAssertion("Test hidden div with padding, border and margin with margin option: 94");
    }

    /**
     * Test.
     */
    @Test
    public void dimensions_module__outerHeight__() {
        assertResult("dimensions module: outerHeight() (0, 6, 6)");
        assertAssertion("Test with only width set: 30");
        assertAssertion("Test with padding: 70");
        assertAssertion("Test with padding and border: 74");
        assertAssertion("Test with padding, border and margin without margin option: 74");
        assertAssertion("Test with padding, border and margin with margin option: 94");
        assertAssertion("Test hidden div with padding, border and margin with margin option: 94");
    }

    /**
     * Test.
     */
    @Test
    public void selector_module__element() {
        assertResult("selector module: element (0, 9, 9)");
        assertAssertion("Select all");
        assertAssertion("Select all elements, no comment nodes");
        assertAssertion("Element Selector (p)");
        assertAssertion("Element Selector (body)");
        assertAssertion("Element Selector (html)");
        assertAssertion("Parent Element (div p)");
        assertAssertion("Object/param as context: 2");
        assertAssertion("<input name=\"length\"> cannot be found under IE, see #945");
        assertAssertion("<input name=\"length\"> cannot be found under IE, see #945");
    }

    /**
     * Test.
     */
    @Test
    public void selector_module__broken() {
        assertResult("selector module: broken (0, 7, 7)");
        assertAssertion("Broken Selector ([)");
        assertAssertion("Broken Selector (()");
        assertAssertion("Broken Selector ({)");
        assertAssertion("Broken Selector (<)");
        assertAssertion("Broken Selector (())");
        assertAssertion("Broken Selector (<>)");
        assertAssertion("Broken Selector ({})");
    }

    /**
     * Test.
     */
    @Test
    public void selector_module__id() {
        assertResult("selector module: id (0, 25, 25)");
        assertAssertion("ID Selector (#body)");
        assertAssertion("ID Selector w/ Element (body#body)");
        assertAssertion("ID Selector w/ Element (ul#first)");
        assertAssertion("ID selector with existing ID descendant (#firstp #simon1)");
        assertAssertion("ID selector with non-existant descendant (#firstp #foobar)");
        assertAssertion("ID selector using UTF8 (#??Ta?ibe?i)");
        assertAssertion("Multiple ID selectors using UTF8 (#??Ta?ibe?i, #??)");
        assertAssertion("Descendant ID selector using UTF8 (div #??)");
        assertAssertion("Child ID selector using UTF8 (form > #??)");
        assertAssertion("Escaped ID (#foo\\:bar)");
        assertAssertion("Escaped ID (#test\\.foo\\[5\\]bar)");
        assertAssertion("Descendant escaped ID (div #foo\\:bar)");
        assertAssertion("Descendant escaped ID (div #test\\.foo\\[5\\]bar)");
        assertAssertion("Child escaped ID (form > #foo\\:bar)");
        assertAssertion("Child escaped ID (form > #test\\.foo\\[5\\]bar)");
        assertAssertion("ID Selector, child ID present (#form > #radio1)");
        assertAssertion("ID Selector, not an ancestor ID (#form #first)");
        assertAssertion("ID Selector, not a child ID (#form > #option1a)");
        assertAssertion("All Children of ID (#foo > *)");
        assertAssertion("All Children of ID with no children (#firstUL/*)");
        assertAssertion("ID selector with same value for a name attribute: tName1");
        assertAssertion("ID selector non-existing but name attribute on an A tag: 0");
        assertAssertion("ID Selector on Form with an input that has a name of 'id' (#lengthtest)");
        assertAssertion("ID selector with non-existant ancestor (#asdfasdf #foobar)");
        assertAssertion("ID selector within the context of another element");
    }

    /**
     * Test.
     */
    @Test
    public void selector_module__class() {
        assertResult("selector module: class (0, 16, 16)");
        assertAssertion("Class Selector (.blog)");
        assertAssertion("Class Selector (.blog.link)");
        assertAssertion("Class Selector w/ Element (a.blog)");
        assertAssertion("Parent Class Selector (p .blog)");
        assertAssertion("Class selector using UTF8 (.??Ta?ibe?i)");
        assertAssertion("Class selector using UTF8 (.??)");
        assertAssertion("Class selector using UTF8 (.??Ta?ibe?i.??)");
        assertAssertion("Class selector using UTF8 (.??Ta?ibe?i, .??)");
        assertAssertion("Descendant class selector using UTF8 (div .??Ta?ibe?i)");
        assertAssertion("Child class selector using UTF8 (form > .??Ta?ibe?i)");
        assertAssertion("Escaped Class (.foo\\:bar)");
        assertAssertion("Escaped Class (.test\\.foo\\[5\\]bar)");
        assertAssertion("Descendant scaped Class (div .foo\\:bar)");
        assertAssertion("Descendant scaped Class (div .test\\.foo\\[5\\]bar)");
        assertAssertion("Child escaped Class (form > .foo\\:bar)");
        assertAssertion("Child escaped Class (form > .test\\.foo\\[5\\]bar)");
    }

    /**
     * Test.
     */
    @Test
    public void selector_module__multiple() {
        assertResult("selector module: multiple (0, 4, 4)");
        assertAssertion("Comma Support (a.blog, p)");
        assertAssertion("Comma Support (a.blog , p)");
        assertAssertion("Comma Support (a.blog ,p)");
        assertAssertion("Comma Support (a.blog,p)");
    }

    /**
     * Test.
     */
    @Test
    public void selector_module__child_and_adjacent() {
        assertResult("selector module: child and adjacent (0, 37, 37)");
        assertAssertion("Child (p > a)");
        assertAssertion("Child (p> a)");
        assertAssertion("Child (p >a)");
        assertAssertion("Child (p>a)");
        assertAssertion("Child w/ Class (p > a.blog)");
        assertAssertion("All Children (code > *)");
        assertAssertion("All Grandchildren (p > * > *)");
        assertAssertion("Adjacent (a + a)");
        assertAssertion("Adjacent (a +a)");
        assertAssertion("Adjacent (a+ a)");
        assertAssertion("Adjacent (a+a)");
        assertAssertion("Adjacent (p + p)");
        assertAssertion("Comma, Child, and Adjacent (a + a, code > a)");
        assertAssertion("First Child (p:first-child)");
        assertAssertion("Nth Child (p:nth-child(1))");
        assertAssertion("Last Child (p:last-child)");
        assertAssertion("Last Child (a:last-child)");
        assertAssertion("Nth-child (#main form#form > *:nth-child(2))");
        assertAssertion("Nth-child (#main form#form > :nth-child(2))");
        assertAssertion("Nth-child (#form select:first option:nth-child(3))");
        assertAssertion("Nth-child (#form select:first option:nth-child(0n+3))");
        assertAssertion("Nth-child (#form select:first option:nth-child(1n+0))");
        assertAssertion("Nth-child (#form select:first option:nth-child(1n))");
        assertAssertion("Nth-child (#form select:first option:nth-child(n))");
        assertAssertion("Nth-child (#form select:first option:nth-child(even))");
        assertAssertion("Nth-child (#form select:first option:nth-child(odd))");
        assertAssertion("Nth-child (#form select:first option:nth-child(2n))");
        assertAssertion("Nth-child (#form select:first option:nth-child(2n+1))");
        assertAssertion("Nth-child (#form select:first option:nth-child(3n))");
        assertAssertion("Nth-child (#form select:first option:nth-child(3n+1))");
        assertAssertion("Nth-child (#form select:first option:nth-child(3n+2))");
        assertAssertion("Nth-child (#form select:first option:nth-child(3n+3))");
        assertAssertion("Nth-child (#form select:first option:nth-child(3n-1))");
        assertAssertion("Nth-child (#form select:first option:nth-child(3n-2))");
        assertAssertion("Nth-child (#form select:first option:nth-child(3n-3))");
        assertAssertion("Nth-child (#form select:first option:nth-child(3n+0))");
        assertAssertion("Nth-child (#form select:first option:nth-child(-n+3))");
    }

    /**
     * Test.
     */
    @Test
    public void selector_module__attributes() {
        assertResult("selector module: attributes (0, 20, 20)");
        assertAssertion("Attribute Exists (a[title])");
        assertAssertion("Attribute Exists (*[title])");
        assertAssertion("Attribute Exists ([title])");
        assertAssertion("Attribute Equals (a[rel='bookmark'])");
        assertAssertion("Attribute Equals (a[rel=\"bookmark\"])");
        assertAssertion("Attribute Equals (a[rel=bookmark])");
        assertAssertion("Multiple Attribute Equals (#form input[type='hidden'],#form input[type='radio'])");
        assertAssertion("Multiple Attribute Equals (#form input[type=\"hidden\"],#form input[type='radio'])");
        assertAssertion("Multiple Attribute Equals (#form input[type=hidden],#form input[type=radio])");
        assertAssertion("Attribute selector using UTF8 (span[lang=??])");
        assertAssertion("Attribute Begins With (a[href ^= 'http://www'])");
        assertAssertion("Attribute Ends With (a[href $= 'org/'])");
        assertAssertion("Attribute Contains (a[href *= 'google'])");
        assertAssertion("Select options via [selected] (#select1 option[selected])");
        assertAssertion("Select options via [selected] (#select2 option[selected])");
        assertAssertion("Select options via [selected] (#select3 option[selected])");
        assertAssertion("Grouped Form Elements (input[name='foo[bar]'])");
        assertAssertion(":not() Existing attribute (#form select:not([multiple]))");
        assertAssertion(":not() Equals attribute (#form select:not([name=select1]))");
        assertAssertion(":not() Equals quoted attribute (#form select:not([name='select1']))");
    }

    /**
     * Test.
     */
    @Test
    public void selector_module__pseudo_____selectors() {
        assertResult("selector module: pseudo (:) selectors (0, 35, 35)");
        assertAssertion("First Child (p:first-child)");
        assertAssertion("Last Child (p:last-child)");
        assertAssertion("Only Child (a:only-child)");
        assertAssertion("Empty (ul:empty)");
        assertAssertion("Enabled UI Element (#form input:enabled)");
        assertAssertion("Disabled UI Element (#form input:disabled)");
        assertAssertion("Checked UI Element (#form input:checked)");
        assertAssertion("Selected Option Element (#form option:selected)");
        assertAssertion("Text Contains (a:contains('Google'))");
        assertAssertion("Text Contains (a:contains('Google Groups'))");
        assertAssertion("Element Preceded By (p ~ div)");
        assertAssertion("Not (a.blog:not(.link))");
        assertAssertion("Not - multiple (#form option:not(:contains('Nothing'),#option1b,:selected))");
        assertAssertion("Not - complex (#form option:not([id^='opt']:gt(0):nth-child(-n+3)))");
        assertAssertion("Not - recursive (#form option:not(:not(:selected))[id^='option3'])");
        assertAssertion("nth Element (p:nth(1))");
        assertAssertion("First Element (p:first)");
        assertAssertion("Last Element (p:last)");
        assertAssertion("Even Elements (p:even)");
        assertAssertion("Odd Elements (p:odd)");
        assertAssertion("Position Equals (p:eq(1))");
        assertAssertion("Position Greater Than (p:gt(0))");
        assertAssertion("Position Less Than (p:lt(3))");
        assertAssertion("Is A Parent (p:parent)");
        assertAssertion("Is Visible (#form input:visible)");
        assertAssertion("Is Hidden (#form input:hidden)");
        assertAssertion("Form element :input (#form :input)");
        assertAssertion("Form element :radio (#form :radio)");
        assertAssertion("Form element :checkbox (#form :checkbox)");
        assertAssertion("Form element :text (#form :text)");
        assertAssertion("Form element :radio:checked (#form :radio:checked)");
        assertAssertion("Form element :checkbox:checked (#form :checkbox:checked)");
        assertAssertion(
            "Form element :checkbox:checked, :radio:checked (#form :checkbox:checked, #form :radio:checked)");
        assertAssertion("Headers (:header)");
        assertAssertion("Has Children - :has() (p:has(a))");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__bind____with_data() {
        assertResult("event module: bind(), with data (0, 3, 3)");
        assertAssertion("bind() with data, check passed data exists");
        assertAssertion("bind() with data, Check value of passed data: bar");
        assertAssertion("Event handler unbound when using data.");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__bind____with_data__trigger_with_data() {
        assertResult("event module: bind(), with data, trigger with data (0, 4, 4)");
        assertAssertion("check passed data exists");
        assertAssertion("Check value of passed data: bar");
        assertAssertion("Check trigger data");
        assertAssertion("Check value of trigger data: foo");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__bind____multiple_events_at_once() {
        assertResult("event module: bind(), multiple events at once (0, 2, 2)");
        assertAssertion("bind() with multiple events at once: 1");
        assertAssertion("bind() with multiple events at once: 1");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__bind____no_data() {
        assertResult("event module: bind(), no data (0, 1, 1)");
        assertAssertion("Check that no data is added to the event object");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__bind____iframes() {
        assertResult("event module: bind(), iframes (0, 0, 0)");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__bind____trigger_change_on_select() {
        assertResult("event module: bind(), trigger change on select (0, 3, 3)");
        assertAssertion("Event.data is not a global event object: 0");
        assertAssertion("Event.data is not a global event object: 1");
        assertAssertion("Event.data is not a global event object: 2");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__bind____namespaced_events__cloned_events() {
        assertResult("event module: bind(), namespaced events, cloned events (0, 6, 6)");
        assertAssertion("Normal click triggered");
        assertAssertion("Namespaced click triggered");
        assertAssertion("Namespaced click triggered");
        assertAssertion("Normal click triggered");
        assertAssertion("Check node,textnode,comment bind just does real nodes: 1");
        assertAssertion("Handler is bound to appendTo'd elements");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__trigger___shortcuts() {
        assertResult("event module: trigger() shortcuts (0, 6, 6)");
        assertAssertion("Context element does not exist, length must be zero: 0");
        assertAssertion("Context element does not exist, direct access to element must return undefined");
        assertAssertion("click event handler for checkbox gets fired twice, see #815");
        assertAssertion("Check that click, triggers onclick event handler also: 1");
        assertAssertion("Check that click, triggers onclick event handler on an a tag also: 1");
        assertAssertion("Trigger the load event, using the shortcut .load() (#2819)");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__unbind_event_() {
        assertResult("event module: unbind(event) (0, 8, 8)");
        assertAssertion("Fake normal bind");
        assertAssertion("Fake onebind");
        assertAssertion("Fake normal bind");
        assertAssertion("Handler is removed");
        assertAssertion("Extra handlers weren't accidentally removed.");
        assertAssertion("Removed the events expando after all handlers are unbound.");
        assertAssertion("unbind() with multiple events at once: 0");
        assertAssertion("unbind() with multiple events at once: 0");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__trigger_event___data____fn__() {
        assertResult("event module: trigger(event, [data], [fn]) (0, 67, 67)");
        assertAssertion("check passed data: click");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("Native call was triggered");
        assertAssertion("check passed data: click");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("check current value: test");
        assertAssertion("Native call was triggered");
        assertAssertion("check passed data: click");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("check passed data: click");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("Verify handler response: test");
        assertAssertion("check passed data: click");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("Verify handler response: false");
        assertAssertion("check passed data: click");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("Verify handler response: test");
        assertAssertion("check passed data: click");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("check passed data: click");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("Verify handler response: test");
        assertAssertion("Trigger focus on hidden element");
        assertAssertion("check passed data: click");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("check current value: test");
        assertAssertion("Verify triggerHandler return is overwritten by extra function: newVal");
        assertAssertion("check passed data: click");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("check passed data: 1");
        assertAssertion("check passed data: 2");
        assertAssertion("check passed data: abc");
        assertAssertion("check current value: test");
        assertAssertion("Verify triggerHandler return is not overwritten by extra function: test");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__toggle_Function__Function______() {
        assertResult("event module: toggle(Function, Function, ...) (0, 11, 11)");
        assertAssertion("Check for toggle(fn, fn): 1");
        assertAssertion("toggle correctly passes through additional triggered arguments, see #1701: 4");
        assertAssertion("Execute event only once");
        assertAssertion("toggle(Function,Function) assigned from within one('xxx'), see #1054: 0");
        assertAssertion("toggle(Function,Function) assigned from within one('xxx'), see #1054: 1");
        assertAssertion("Trying toggle with 3 functions, attempt 1 yields 1: 1");
        assertAssertion("Trying toggle with 3 functions, attempt 2 yields 2: 2");
        assertAssertion("Trying toggle with 3 functions, attempt 3 yields 3: 3");
        assertAssertion("Trying toggle with 3 functions, attempt 4 yields 1: 1");
        assertAssertion("Trying toggle with 3 functions, attempt 5 yields 2: 2");
        assertAssertion("Unbinding one function from toggle unbinds them all");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__jQuery_function_$_____() {
        assertResult("event module: jQuery(function($) {}) (0, 1, 1)");
        assertAssertion("ready doesn't provide an event object, instead it provides a reference to the jQuery "
            + "function, see http://docs.jquery.com/Events/ready#fn: function( selector, context ) { // The jQuery "
            + "object is actually just the init constructor 'enhanced' return new jQuery.fn.init( selector, context "
            + "); }");
    }

    /**
     * Test.
     */
    @Test
    public void event_module__event_properties() {
        assertResult("event module: event properties (0, 1, 1)");
        assertAssertion("assert event.timeStamp is present");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__animate_Hash__Object__Function_() {
        assertResult("fx module: animate(Hash, Object, Function) (0, 1, 1)");
        assertAssertion("Check if animate changed the hash parameter: show");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__animate_option__queue_____false_() {
        assertResult("fx module: animate option (queue === false) (0, 1, 1)");
        assertAssertion("Animations finished in the correct order");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__queue___defaults_to__fx__type() {
        assertResult("fx module: queue() defaults to 'fx' type (0, 2, 2)");
        assertAssertion("queue() got an array set with type 'fx'");
        assertAssertion("queue('fx') got an array set with no type");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__stop__() {
        assertResult("fx module: stop() (0, 3, 3)");
        assertAssertion("An animation occurred 4px 0px");
        assertAssertion("Stop didn't reset the animation 4px 0px");
        assertAssertion("The animation didn't continue: 4");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__stop_____several_in_queue() {
        assertResult("fx module: stop() - several in queue (0, 4, 4)");
        assertAssertion("All 3 still in the queue: 3");
        assertAssertion("An animation occurred 4px 0px");
        assertAssertion("Stop didn't reset the animation 4px 0px");
        assertAssertion("The next animation continued: 2");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__stop_clearQueue_() {
        assertResult("fx module: stop(clearQueue) (0, 4, 4)");
        assertAssertion("An animation occurred 4px 0px");
        assertAssertion("Stop didn't reset the animation 4px 0px");
        assertAssertion("The animation queue was cleared: 0");
        assertAssertion("The animation didn't continue: 4");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__stop_clearQueue__gotoEnd_() {
        assertResult("fx module: stop(clearQueue, gotoEnd) (0, 3, 3)");
        assertAssertion("An animation occurred 4px 0px");
        assertAssertion("Stop() reset the animation: 200");
        assertAssertion("The next animation continued: 3");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__toggle__() {
        assertResult("fx module: toggle() (0, 3, 3)");
        assertAssertion("is visible");
        assertAssertion("is hidden");
        assertAssertion("is visible again");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_Overflow_and_Display() {
        assertResult("fx module: JS Overflow and Display (0, 2, 2)");
        assertAssertion("Overflow should be visible: visible: visible");
        assertAssertion("Display shouldn't be tampered with.: inline");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_Overflow_and_Display() {
        assertResult("fx module: CSS Overflow and Display (0, 2, 2)");
        assertAssertion("Overflow should be visible: visible: visible");
        assertAssertion("Display shouldn't be tampered with.: inline");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_Auto_to_show() {
        assertResult("fx module: CSS Auto to show (0, 5, 5)");
        assertAssertion("Showing, display should block: block: block");
        assertAssertion("Width must be reset to : : 0");
        assertAssertion("Height must be reset to : : 0");
        assertAssertion("Opacity must be reset to : : ");
        assertAssertion("Make sure height is auto.");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_Auto_to_hide() {
        assertResult("fx module: CSS Auto to hide (0, 4, 4)");
        assertAssertion("Width must be reset to : : 0");
        assertAssertion("Height must be reset to : : 0");
        assertAssertion("Opacity must be reset to : : ");
        assertAssertion("Hiding, display should be none: none: none");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_Auto_to_100() {
        assertResult("fx module: CSS Auto to 100 (0, 6, 6)");
        assertAssertion("Final opacity should be 1: 1: 1");
        assertAssertion("Opacity should be explicitly set to 1, is instead: 1");
        assertAssertion("Final width should be 100: 100px: 100px");
        assertAssertion("Width should be explicitly set to 100, is instead: 0");
        assertAssertion("Final height should be 100: 100px: 100px");
        assertAssertion("Height should be explicitly set to 100, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_Auto_to_50() {
        assertResult("fx module: CSS Auto to 50 (0, 6, 6)");
        assertAssertion("Final opacity should be 0.5: 0.5: 0.5");
        assertAssertion("Opacity should be explicitly set to 0.5, is instead: 0.5");
        assertAssertion("Final width should be 50: 50px: 50px");
        assertAssertion("Width should be explicitly set to 50, is instead: 0");
        assertAssertion("Final height should be 50: 50px: 50px");
        assertAssertion("Height should be explicitly set to 50, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_Auto_to_0() {
        assertResult("fx module: CSS Auto to 0 (0, 6, 6)");
        assertAssertion("Final opacity should be 0: 0: 0");
        assertAssertion("Opacity should be explicitly set to 0, is instead: 0");
        assertAssertion("Final width should be 0: 0px: 0px");
        assertAssertion("Width should be explicitly set to 0, is instead: 0");
        assertAssertion("Final height should be 0: 0px: 0px");
        assertAssertion("Height should be explicitly set to 0, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_Auto_to_show() {
        assertResult("fx module: JS Auto to show (0, 5, 5)");
        assertAssertion("Showing, display should block: block: block");
        assertAssertion("Width must be reset to : auto: 0");
        assertAssertion("Height must be reset to : auto: 0");
        assertAssertion("Opacity must be reset to : : ");
        assertAssertion("Make sure height is auto.");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_Auto_to_hide() {
        assertResult("fx module: JS Auto to hide (0, 4, 4)");
        assertAssertion("Width must be reset to : auto: 0");
        assertAssertion("Height must be reset to : auto: 0");
        assertAssertion("Opacity must be reset to : : ");
        assertAssertion("Hiding, display should be none: none: none");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_Auto_to_100() {
        assertResult("fx module: JS Auto to 100 (0, 6, 6)");
        assertAssertion("Final opacity should be 1: 1: 1");
        assertAssertion("Opacity should be explicitly set to 1, is instead: 1");
        assertAssertion("Final width should be 100: 100px: 100px");
        assertAssertion("Width should be explicitly set to 100, is instead: 0");
        assertAssertion("Final height should be 100: 100px: 100px");
        assertAssertion("Height should be explicitly set to 100, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_Auto_to_50() {
        assertResult("fx module: JS Auto to 50 (0, 6, 6)");
        assertAssertion("Final opacity should be 0.5: 0.5: 0.5");
        assertAssertion("Opacity should be explicitly set to 0.5, is instead: 0.5");
        assertAssertion("Final width should be 50: 50px: 50px");
        assertAssertion("Width should be explicitly set to 50, is instead: 0");
        assertAssertion("Final height should be 50: 50px: 50px");
        assertAssertion("Height should be explicitly set to 50, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_Auto_to_0() {
        assertResult("fx module: JS Auto to 0 (0, 6, 6)");
        assertAssertion("Final opacity should be 0: 0: 0");
        assertAssertion("Opacity should be explicitly set to 0, is instead: 0");
        assertAssertion("Final width should be 0: 0px: 0px");
        assertAssertion("Width should be explicitly set to 0, is instead: 0");
        assertAssertion("Final height should be 0: 0px: 0px");
        assertAssertion("Height should be explicitly set to 0, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_100_to_show() {
        assertResult("fx module: CSS 100 to show (0, 5, 5)");
        assertAssertion("Showing, display should block: block: block");
        assertAssertion("Width must be reset to : : 0");
        assertAssertion("Height must be reset to : : 0");
        assertAssertion("Opacity must be reset to : : ");
        assertAssertion("Make sure height is auto.");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_100_to_hide() {
        assertResult("fx module: CSS 100 to hide (0, 4, 4)");
        assertAssertion("Width must be reset to : : 0");
        assertAssertion("Height must be reset to : : 0");
        assertAssertion("Opacity must be reset to : : ");
        assertAssertion("Hiding, display should be none: none: none");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_100_to_100() {
        assertResult("fx module: CSS 100 to 100 (0, 6, 6)");
        assertAssertion("Final opacity should be 1: 1: 1");
        assertAssertion("Opacity should be explicitly set to 1, is instead: 1");
        assertAssertion("Final width should be 100: 100px: 100px");
        assertAssertion("Width should be explicitly set to 100, is instead: 0");
        assertAssertion("Final height should be 100: 100px: 100px");
        assertAssertion("Height should be explicitly set to 100, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_100_to_50() {
        assertResult("fx module: CSS 100 to 50 (0, 6, 6)");
        assertAssertion("Final opacity should be 0.5: 0.5: 0.5");
        assertAssertion("Opacity should be explicitly set to 0.5, is instead: 0.5");
        assertAssertion("Final width should be 50: 50px: 50px");
        assertAssertion("Width should be explicitly set to 50, is instead: 0");
        assertAssertion("Final height should be 50: 50px: 50px");
        assertAssertion("Height should be explicitly set to 50, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_100_to_0() {
        assertResult("fx module: CSS 100 to 0 (0, 6, 6)");
        assertAssertion("Final opacity should be 0: 0: 0");
        assertAssertion("Opacity should be explicitly set to 0, is instead: 0");
        assertAssertion("Final width should be 0: 0px: 0px");
        assertAssertion("Width should be explicitly set to 0, is instead: 0");
        assertAssertion("Final height should be 0: 0px: 0px");
        assertAssertion("Height should be explicitly set to 0, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_100_to_show() {
        assertResult("fx module: JS 100 to show (0, 5, 5)");
        assertAssertion("Showing, display should block: block: block");
        assertAssertion("Width must be reset to 100: 100px: 0");
        assertAssertion("Height must be reset to 100: 100px: 0");
        assertAssertion("Opacity must be reset to 1: 1: 1");
        assertAssertion("Make sure height is auto.");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_100_to_hide() {
        assertResult("fx module: JS 100 to hide (0, 4, 4)");
        assertAssertion("Width must be reset to 100: 100px: 0");
        assertAssertion("Height must be reset to 100: 100px: 0");
        assertAssertion("Opacity must be reset to 1: 1: 1");
        assertAssertion("Hiding, display should be none: none: none");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_100_to_100() {
        assertResult("fx module: JS 100 to 100 (0, 6, 6)");
        assertAssertion("Final opacity should be 1: 1: 1");
        assertAssertion("Opacity should be explicitly set to 1, is instead: 1");
        assertAssertion("Final width should be 100: 100px: 100px");
        assertAssertion("Width should be explicitly set to 100, is instead: 0");
        assertAssertion("Final height should be 100: 100px: 100px");
        assertAssertion("Height should be explicitly set to 100, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_100_to_50() {
        assertResult("fx module: JS 100 to 50 (0, 6, 6)");
        assertAssertion("Final opacity should be 0.5: 0.5: 0.5");
        assertAssertion("Opacity should be explicitly set to 0.5, is instead: 0.5");
        assertAssertion("Final width should be 50: 50px: 50px");
        assertAssertion("Width should be explicitly set to 50, is instead: 0");
        assertAssertion("Final height should be 50: 50px: 50px");
        assertAssertion("Height should be explicitly set to 50, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_100_to_0() {
        assertResult("fx module: JS 100 to 0 (0, 6, 6)");
        assertAssertion("Final opacity should be 0: 0: 0");
        assertAssertion("Opacity should be explicitly set to 0, is instead: 0");
        assertAssertion("Final width should be 0: 0px: 0px");
        assertAssertion("Width should be explicitly set to 0, is instead: 0");
        assertAssertion("Final height should be 0: 0px: 0px");
        assertAssertion("Height should be explicitly set to 0, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_50_to_show() {
        assertResult("fx module: CSS 50 to show (0, 5, 5)");
        assertAssertion("Showing, display should block: block: block");
        assertAssertion("Width must be reset to : : 0");
        assertAssertion("Height must be reset to : : 0");
        assertAssertion("Opacity must be reset to : : ");
        assertAssertion("Make sure height is auto.");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_50_to_hide() {
        assertResult("fx module: CSS 50 to hide (0, 4, 4)");
        assertAssertion("Width must be reset to : : 0");
        assertAssertion("Height must be reset to : : 0");
        assertAssertion("Opacity must be reset to : : ");
        assertAssertion("Hiding, display should be none: none: none");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_50_to_100() {
        assertResult("fx module: CSS 50 to 100 (0, 6, 6)");
        assertAssertion("Final opacity should be 1: 1: 1");
        assertAssertion("Opacity should be explicitly set to 1, is instead: 1");
        assertAssertion("Final width should be 100: 100px: 100px");
        assertAssertion("Width should be explicitly set to 100, is instead: 0");
        assertAssertion("Final height should be 100: 100px: 100px");
        assertAssertion("Height should be explicitly set to 100, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_50_to_50() {
        assertResult("fx module: CSS 50 to 50 (0, 6, 6)");
        assertAssertion("Final opacity should be 0.5: 0.5: 0.5");
        assertAssertion("Opacity should be explicitly set to 0.5, is instead: 0.5");
        assertAssertion("Final width should be 50: 50px: 50px");
        assertAssertion("Width should be explicitly set to 50, is instead: 0");
        assertAssertion("Final height should be 50: 50px: 50px");
        assertAssertion("Height should be explicitly set to 50, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_50_to_0() {
        assertResult("fx module: CSS 50 to 0 (0, 6, 6)");
        assertAssertion("Final opacity should be 0: 0: 0");
        assertAssertion("Opacity should be explicitly set to 0, is instead: 0");
        assertAssertion("Final width should be 0: 0px: 0px");
        assertAssertion("Width should be explicitly set to 0, is instead: 0");
        assertAssertion("Final height should be 0: 0px: 0px");
        assertAssertion("Height should be explicitly set to 0, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_50_to_show() {
        assertResult("fx module: JS 50 to show (0, 5, 5)");
        assertAssertion("Showing, display should block: block: block");
        assertAssertion("Width must be reset to 50: 50px: 0");
        assertAssertion("Height must be reset to 50: 50px: 0");
        assertAssertion("Opacity must be reset to 0.5: 0.5: 0.5");
        assertAssertion("Make sure height is auto.");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_50_to_hide() {
        assertResult("fx module: JS 50 to hide (0, 4, 4)");
        assertAssertion("Width must be reset to 50: 50px: 0");
        assertAssertion("Height must be reset to 50: 50px: 0");
        assertAssertion("Opacity must be reset to 0.5: 0.5: 0.5");
        assertAssertion("Hiding, display should be none: none: none");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_50_to_100() {
        assertResult("fx module: JS 50 to 100 (0, 6, 6)");
        assertAssertion("Final opacity should be 1: 1: 1");
        assertAssertion("Opacity should be explicitly set to 1, is instead: 1");
        assertAssertion("Final width should be 100: 100px: 100px");
        assertAssertion("Width should be explicitly set to 100, is instead: 0");
        assertAssertion("Final height should be 100: 100px: 100px");
        assertAssertion("Height should be explicitly set to 100, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_50_to_50() {
        assertResult("fx module: JS 50 to 50 (0, 6, 6)");
        assertAssertion("Final opacity should be 0.5: 0.5: 0.5");
        assertAssertion("Opacity should be explicitly set to 0.5, is instead: 0.5");
        assertAssertion("Final width should be 50: 50px: 50px");
        assertAssertion("Width should be explicitly set to 50, is instead: 0");
        assertAssertion("Final height should be 50: 50px: 50px");
        assertAssertion("Height should be explicitly set to 50, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_50_to_0() {
        assertResult("fx module: JS 50 to 0 (0, 6, 6)");
        assertAssertion("Final opacity should be 0: 0: 0");
        assertAssertion("Opacity should be explicitly set to 0, is instead: 0");
        assertAssertion("Final width should be 0: 0px: 0px");
        assertAssertion("Width should be explicitly set to 0, is instead: 0");
        assertAssertion("Final height should be 0: 0px: 0px");
        assertAssertion("Height should be explicitly set to 0, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_0_to_show() {
        assertResult("fx module: CSS 0 to show (0, 5, 5)");
        assertAssertion("Showing, display should block: block: block");
        assertAssertion("Width must be reset to : : 0");
        assertAssertion("Height must be reset to : : 0");
        assertAssertion("Opacity must be reset to : : ");
        assertAssertion("Make sure height is auto.");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_0_to_hide() {
        assertResult("fx module: CSS 0 to hide (0, 4, 4)");
        assertAssertion("Width must be reset to : : 0");
        assertAssertion("Height must be reset to : : 0");
        assertAssertion("Opacity must be reset to : : ");
        assertAssertion("Hiding, display should be none: none: none");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_0_to_100() {
        assertResult("fx module: CSS 0 to 100 (0, 6, 6)");
        assertAssertion("Final opacity should be 1: 1: 1");
        assertAssertion("Opacity should be explicitly set to 1, is instead: 1");
        assertAssertion("Final width should be 100: 100px: 100px");
        assertAssertion("Width should be explicitly set to 100, is instead: 0");
        assertAssertion("Final height should be 100: 100px: 100px");
        assertAssertion("Height should be explicitly set to 100, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_0_to_50() {
        assertResult("fx module: CSS 0 to 50 (0, 6, 6)");
        assertAssertion("Final opacity should be 0.5: 0.5: 0.5");
        assertAssertion("Opacity should be explicitly set to 0.5, is instead: 0.5");
        assertAssertion("Final width should be 50: 50px: 50px");
        assertAssertion("Width should be explicitly set to 50, is instead: 0");
        assertAssertion("Final height should be 50: 50px: 50px");
        assertAssertion("Height should be explicitly set to 50, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__CSS_0_to_0() {
        assertResult("fx module: CSS 0 to 0 (0, 6, 6)");
        assertAssertion("Final opacity should be 0: 0: 0");
        assertAssertion("Opacity should be explicitly set to 0, is instead: 0");
        assertAssertion("Final width should be 0: 0px: 0px");
        assertAssertion("Width should be explicitly set to 0, is instead: 0");
        assertAssertion("Final height should be 0: 0px: 0px");
        assertAssertion("Height should be explicitly set to 0, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_0_to_show() {
        assertResult("fx module: JS 0 to show (0, 5, 5)");
        assertAssertion("Showing, display should block: block: block");
        assertAssertion("Width must be reset to 0: 0px: 0");
        assertAssertion("Height must be reset to 0: 0px: 0");
        assertAssertion("Opacity must be reset to 0: 0: 0");
        assertAssertion("Make sure height is auto.");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_0_to_hide() {
        assertResult("fx module: JS 0 to hide (0, 4, 4)");
        assertAssertion("Width must be reset to 0: 0px: 0");
        assertAssertion("Height must be reset to 0: 0px: 0");
        assertAssertion("Opacity must be reset to 0: 0: 0");
        assertAssertion("Hiding, display should be none: none: none");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_0_to_100() {
        assertResult("fx module: JS 0 to 100 (0, 6, 6)");
        assertAssertion("Final opacity should be 1: 1: 1");
        assertAssertion("Opacity should be explicitly set to 1, is instead: 1");
        assertAssertion("Final width should be 100: 100px: 100px");
        assertAssertion("Width should be explicitly set to 100, is instead: 0");
        assertAssertion("Final height should be 100: 100px: 100px");
        assertAssertion("Height should be explicitly set to 100, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_0_to_50() {
        assertResult("fx module: JS 0 to 50 (0, 6, 6)");
        assertAssertion("Final opacity should be 0.5: 0.5: 0.5");
        assertAssertion("Opacity should be explicitly set to 0.5, is instead: 0.5");
        assertAssertion("Final width should be 50: 50px: 50px");
        assertAssertion("Width should be explicitly set to 50, is instead: 0");
        assertAssertion("Final height should be 50: 50px: 50px");
        assertAssertion("Height should be explicitly set to 50, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__JS_0_to_0() {
        assertResult("fx module: JS 0 to 0 (0, 6, 6)");
        assertAssertion("Final opacity should be 0: 0: 0");
        assertAssertion("Opacity should be explicitly set to 0, is instead: 0");
        assertAssertion("Final width should be 0: 0px: 0px");
        assertAssertion("Width should be explicitly set to 0, is instead: 0");
        assertAssertion("Final height should be 0: 0px: 0px");
        assertAssertion("Height should be explicitly set to 0, is instead: 0");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__Chain_fadeOut_fadeIn() {
        assertResult("fx module: Chain fadeOut fadeIn (0, 5, 5)");
        assertAssertion("Make sure that opacity is reset (Old: 1 Cur: 1): 1");
        assertAssertion("Make sure that height is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that width is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that display is reset (Old: block Cur: block): block");
        assertAssertion("Make sure that overflow is reset (Old: visible Cur: visible): visible");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__Chain_fadeIn_fadeOut() {
        assertResult("fx module: Chain fadeIn fadeOut (0, 5, 5)");
        assertAssertion("Make sure that opacity is reset (Old: 1 Cur: 1): 1");
        assertAssertion("Make sure that height is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that width is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that display is reset (Old: none Cur: none): none");
        assertAssertion("Make sure that overflow is reset (Old: visible Cur: visible): visible");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__Chain_hide_show() {
        assertResult("fx module: Chain hide show (0, 5, 5)");
        assertAssertion("Make sure that opacity is reset (Old: 1 Cur: 1): 1");
        assertAssertion("Make sure that height is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that width is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that display is reset (Old: block Cur: block): block");
        assertAssertion("Make sure that overflow is reset (Old: visible Cur: visible): visible");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__Chain_show_hide() {
        assertResult("fx module: Chain show hide (0, 5, 5)");
        assertAssertion("Make sure that opacity is reset (Old: 1 Cur: 1): 1");
        assertAssertion("Make sure that height is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that width is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that display is reset (Old: none Cur: none): none");
        assertAssertion("Make sure that overflow is reset (Old: visible Cur: visible): visible");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__Chain_toggle_in() {
        assertResult("fx module: Chain toggle in (0, 5, 5)");
        assertAssertion("Make sure that opacity is reset (Old: 1 Cur: 1): 1");
        assertAssertion("Make sure that height is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that width is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that display is reset (Old: block Cur: block): block");
        assertAssertion("Make sure that overflow is reset (Old: visible Cur: visible): visible");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__Chain_toggle_out() {
        assertResult("fx module: Chain toggle out (0, 5, 5)");
        assertAssertion("Make sure that opacity is reset (Old: 1 Cur: 1): 1");
        assertAssertion("Make sure that height is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that width is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that display is reset (Old: none Cur: none): none");
        assertAssertion("Make sure that overflow is reset (Old: visible Cur: visible): visible");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__Chain_slideDown_slideUp() {
        assertResult("fx module: Chain slideDown slideUp (0, 5, 5)");
        assertAssertion("Make sure that opacity is reset (Old: 1 Cur: 1): 1");
        assertAssertion("Make sure that height is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that width is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that display is reset (Old: none Cur: none): none");
        assertAssertion("Make sure that overflow is reset (Old: visible Cur: visible): visible");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__Chain_slideUp_slideDown() {
        assertResult("fx module: Chain slideUp slideDown (0, 5, 5)");
        assertAssertion("Make sure that opacity is reset (Old: 1 Cur: 1): 1");
        assertAssertion("Make sure that height is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that width is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that display is reset (Old: block Cur: block): block");
        assertAssertion("Make sure that overflow is reset (Old: visible Cur: visible): visible");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__Chain_slideToggle_in() {
        assertResult("fx module: Chain slideToggle in (0, 5, 5)");
        assertAssertion("Make sure that opacity is reset (Old: 1 Cur: 1): 1");
        assertAssertion("Make sure that height is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that width is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that display is reset (Old: block Cur: block): block");
        assertAssertion("Make sure that overflow is reset (Old: visible Cur: visible): visible");
    }

    /**
     * Test.
     */
    @Test
    public void fx_module__Chain_slideToggle_out() {
        assertResult("fx module: Chain slideToggle out (0, 5, 5)");
        assertAssertion("Make sure that opacity is reset (Old: 1 Cur: 1): 1");
        assertAssertion("Make sure that height is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that width is reset (Old: 0 Cur: 0): 0");
        assertAssertion("Make sure that display is reset (Old: none Cur: none): none");
        assertAssertion("Make sure that overflow is reset (Old: visible Cur: visible): visible");
    }
}
