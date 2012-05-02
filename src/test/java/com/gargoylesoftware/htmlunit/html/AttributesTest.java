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
package com.gargoylesoftware.htmlunit.html;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * <p>Tests for all the generated attribute accessors. This test case will
 * dynamically generate tests for all the various attributes. The code
 * is fairly complicated but doing it this way is much easier than writing
 * individual tests for all the attributes.</p>
 *
 * <p>With the new custom DOM, this test has somewhat lost its significance.
 * We simply set and get the attributes and compare the results.</p>
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Christian Sell
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class AttributesTest extends TestCase {

    private final Class<?> classUnderTest_;
    private final Method method_;
    private final HtmlPage page_;
    private final String attributeName_;

    private static final List<String> EXCLUDED_METHODS = new ArrayList<String>();
    static {
        EXCLUDED_METHODS.add("getHtmlElementsByAttribute");
        EXCLUDED_METHODS.add("getOneHtmlElementByAttribute");
        EXCLUDED_METHODS.add("getAttribute");
        EXCLUDED_METHODS.add("getElementsByAttribute");
    }

    /**
     * Returns a test suite containing a separate test for each attribute on each element.
     *
     * @return the test suite
     * @throws Exception if the tests cannot be created
     */
    public static Test suite() throws Exception {
        final WebClient webClient = new WebClient();
        final MockWebConnection connection = new MockWebConnection();
        connection.setDefaultResponse("<html><head><title>foo</title></head><body></body></html>");
        webClient.setWebConnection(connection);
        final HtmlPage page = webClient.getPage(WebTestCase.URL_FIRST);

        final TestSuite suite = new TestSuite();
        final String[] classesToTest = {
            "HtmlAbbreviated", "HtmlAcronym",
            "HtmlAddress", "HtmlAnchor", "HtmlApplet", "HtmlArea",
            "HtmlAudio", "HtmlBackgroundSound",
            "HtmlBase", "HtmlBaseFont", "HtmlBidirectionalOverride",
            "HtmlBig", "HtmlBlink",
            "HtmlBlockQuote", "HtmlBody", "HtmlBold", "HtmlBreak", "HtmlButton",
            "HtmlButtonInput", "HtmlCanvas", "HtmlCaption", "HtmlCenter",
            "HtmlCheckBoxInput", "HtmlCitation", "HtmlCode",
            "HtmlDefinition", "HtmlDefinitionDescription",
            "HtmlDefinitionList", "HtmlDefinitionTerm",
            "HtmlDeletedText", "HtmlDirectory", "HtmlDivision", /*"HtmlElement", */
            "HtmlEmbed", "HtmlEmphasis", "HtmlExample",
            "HtmlFieldSet", "HtmlFileInput", "HtmlFont", "HtmlForm",
            "HtmlFrame", "HtmlFrameSet", "HtmlHead", "HtmlHeading1",
            "HtmlHeading2", "HtmlHeading3", "HtmlHeading4", "HtmlHeading5",
            "HtmlHeading6", "HtmlHiddenInput", "HtmlHorizontalRule",
            "HtmlHtml", "HtmlImage", "HtmlImageInput", "HtmlInlineFrame",
            "HtmlInlineQuotation",
            "HtmlInsertedText", "HtmlIsIndex", "HtmlItalic",
            "HtmlKeyboard", "HtmlLabel",
            "HtmlLegend", "HtmlLink", "HtmlListing", "HtmlListItem", "HtmlMap",
            "HtmlMarquee",
            "HtmlMenu", "HtmlMeta", "HtmlMultiColumn",
            "HtmlNoBreak", "HtmlNoEmbed", "HtmlNoFrames", "HtmlNoScript",
            "HtmlObject", "HtmlOption", "HtmlOptionGroup", "HtmlOrderedList",
            /*"HtmlPage",*/ "HtmlParagraph", "HtmlParameter", "HtmlPasswordInput",
            "HtmlPlainText",
            "HtmlPreformattedText", "HtmlRadioButtonInput", "HtmlResetInput",
            "HtmlS", "HtmlSample", "HtmlScript", "HtmlSelect", "HtmlSmall",
            "HtmlSpacer", "HtmlSpan", "HtmlSource", "HtmlStrike",
            "HtmlStrong", "HtmlStyle", "HtmlSubmitInput",
            "HtmlSubscript", "HtmlSuperscript",
            "HtmlTable", "HtmlTableBody", /*"HtmlTableCell",*/ "HtmlTableColumn",
            "HtmlTableColumnGroup", "HtmlTableDataCell",
            "HtmlTableFooter", "HtmlTableHeader", "HtmlTableHeaderCell",
            "HtmlTableRow", "HtmlTeletype", "HtmlTextArea", "HtmlTextInput",
            "HtmlTitle", "HtmlUnderlined", "HtmlUnorderedList",
            "HtmlVariable", "HtmlVideo", "HtmlWordBreak"
        };

        for (final String testClass : classesToTest) {
            final Class<?> clazz = Class.forName("com.gargoylesoftware.htmlunit.html." + testClass);
            addTestsForClass(clazz, page, suite);
        }
        return suite;
    }

    /**
     * Adds all the tests for a given class.
     *
     * @param clazz the class to create tests for
     * @param page the page that will be passed into the constructor of the objects to be tested
     * @param suite the suite that all the tests will be placed inside
     * @throws Exception if the tests cannot be created
     */
    private static void addTestsForClass(
            final Class<?> clazz,
            final HtmlPage page,
            final TestSuite suite)
        throws
            Exception {

        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            final String methodName = method.getName();
            if (methodName.startsWith("get")
                && methodName.endsWith("Attribute")
                && !EXCLUDED_METHODS.contains(methodName)) {

                String attributeName = methodName.substring(3, methodName.length() - 9).toLowerCase();
                if ("xmllang".equals(attributeName)) {
                    attributeName = "xml:lang";
                }
                else if ("columns".equals(attributeName)) {
                    attributeName = "cols";
                }
                else if ("columnspan".equals(attributeName)) {
                    attributeName = "colspan";
                }
                else if ("textdirection".equals(attributeName)) {
                    attributeName = "dir";
                }
                else if ("httpequiv".equals(attributeName)) {
                    attributeName = "http-equiv";
                }
                else if ("acceptcharset".equals(attributeName)) {
                    attributeName = "accept-charset";
                }
                else if ("htmlfor".equals(attributeName)) {
                    attributeName = "for";
                }
                suite.addTest(new AttributesTest(attributeName, clazz, method, page));
            }
        }
    }

    /**
     * Creates an instance of the test. This will test one specific attribute
     * on one specific class.
     * @param attributeName the name of the attribute to test
     * @param classUnderTest the class containing the attribute
     * @param method the "getter" method for the specified attribute
     * @param page the page that will be passed into the constructor of the object to be tested
     */
    public AttributesTest(
            final String attributeName,
            final Class<?> classUnderTest,
            final Method method,
            final HtmlPage page) {

        super(createTestName(classUnderTest, method));
        classUnderTest_ = classUnderTest;
        method_ = method;
        page_ = page;
        attributeName_ = attributeName;
    }

    /**
     * Creates a name for this particular test that reflects the attribute being tested.
     * @param clazz the class containing the attribute
     * @param method the getter method for the attribute
     * @return the test name
     */
    private static String createTestName(final Class<?> clazz, final Method method) {
        String className = clazz.getName();
        final int index = className.lastIndexOf('.');
        className = className.substring(index + 1);

        return "testAttributes_" + className + '_' + method.getName();
    }

    /**
     * Runs the actual test.
     * @throws Exception if the test fails
     */
    @Override
    protected void runTest() throws Exception {
        final String value = "value";

        final HtmlElement objectToTest = getNewInstanceForClassUnderTest();
        objectToTest.setAttribute(attributeName_, value);

        final Object noObjects[] = new Object[0];
        final Object result = method_.invoke(objectToTest, noObjects);
        assertSame(value, result);
    }

    /**
     * Creates a new instance of the class being tested.
     * @return the new instance
     * @throws Exception if the new object cannot be created
     */
    private HtmlElement getNewInstanceForClassUnderTest() throws Exception {
        final HtmlElement newInstance;
        if (classUnderTest_ == HtmlTableRow.class) {
            newInstance = HTMLParser.getFactory(HtmlTableRow.TAG_NAME).createElement(
                    page_, HtmlTableRow.TAG_NAME, null);
        }
        else if (classUnderTest_ == HtmlTableHeaderCell.class) {
            newInstance = HTMLParser.getFactory(HtmlTableHeaderCell.TAG_NAME).createElement(
                    page_, HtmlTableHeaderCell.TAG_NAME, null);
        }
        else if (classUnderTest_ == HtmlTableDataCell.class) {
            newInstance = HTMLParser.getFactory(HtmlTableDataCell.TAG_NAME).createElement(
                    page_, HtmlTableDataCell.TAG_NAME, null);
        }
        else {
            final String tagName = (String) classUnderTest_.getField("TAG_NAME").get(null);
            newInstance = HTMLParser.getFactory(tagName).createElement(page_, tagName, null);
        }

        return newInstance;
    }
}
