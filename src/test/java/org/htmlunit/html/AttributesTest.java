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
package org.htmlunit.html;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.htmlunit.BrowserVersion;
import org.htmlunit.MockWebConnection;
import org.htmlunit.WebClient;
import org.htmlunit.WebTestCase;
import org.htmlunit.html.parser.HTMLParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * <p>Tests for all the generated attribute accessors. This test case will
 * dynamically generate tests for all the various attributes. The code
 * is fairly complicated but doing it this way is much easier than writing
 * individual tests for all the attributes.</p>
 *
 * <p>With the new custom DOM, this test has somewhat lost its significance.
 * We simply set and get the attributes and compare the results.</p>
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Christian Sell
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class AttributesTest {

    private static final List<String> EXCLUDED_METHODS = new ArrayList<>();
    static {
        EXCLUDED_METHODS.add("getHtmlElementsByAttribute");
        EXCLUDED_METHODS.add("getOneHtmlElementByAttribute");
        EXCLUDED_METHODS.add("getAttribute");
        EXCLUDED_METHODS.add("getElementsByAttribute");
    }

    /**
     * Creates dynamic tests for each attribute on each element.
     *
     * @return stream of dynamic tests
     * @throws Exception if the tests cannot be created
     */
    @TestFactory
    Stream<DynamicTest> attributeTests() throws Exception {
        final String[] classesToTest = {
            "HtmlAbbreviated", "HtmlAcronym",
            "HtmlAnchor", "HtmlAddress", "HtmlArea",
            "HtmlArticle", "HtmlAside", "HtmlAudio",
            "HtmlBackgroundSound", "HtmlBase", "HtmlBaseFont",
            "HtmlBidirectionalIsolation",
            "HtmlBidirectionalOverride", "HtmlBig",
            "HtmlBlockQuote", "HtmlBody", "HtmlBold",
            "HtmlBreak", "HtmlButton", "HtmlCanvas", "HtmlCaption",
            "HtmlCenter", "HtmlCitation", "HtmlCode", "DomComment",
            "HtmlData", "HtmlDataList",
            "HtmlDefinition", "HtmlDefinitionDescription",
            "HtmlDeletedText", "HtmlDetails", "HtmlDialog", "HtmlDirectory",
            "HtmlDivision", "HtmlDefinitionList",
            "HtmlDefinitionTerm", "HtmlEmbed",
            "HtmlEmphasis",
            "HtmlFieldSet", "HtmlFigureCaption", "HtmlFigure",
            "HtmlFont", "HtmlForm", "HtmlFooter",
            "HtmlFrame", "HtmlFrameSet",
            "HtmlHead", "HtmlHeader",
            "HtmlHeading1", "HtmlHeading2", "HtmlHeading3",
            "HtmlHeading4", "HtmlHeading5", "HtmlHeading6",
            "HtmlHorizontalRule", "HtmlHtml", "HtmlInlineFrame",
            "HtmlInlineQuotation",
            "HtmlImage", "HtmlImage", "HtmlInsertedText",
            "HtmlItalic", "HtmlKeyboard", "HtmlLabel", "HtmlLayer",
            "HtmlLegend", "HtmlListing", "HtmlListItem",
            "HtmlLink",
            "HtmlMap", "HtmlMain", "HtmlMark", "HtmlMarquee",
            "HtmlMenu", "HtmlMeta", "HtmlMeter",
            "HtmlNav",
            "HtmlNoBreak", "HtmlNoEmbed", "HtmlNoFrames", "HtmlNoLayer",
            "HtmlNoScript", "HtmlObject", "HtmlOrderedList",
            "HtmlOptionGroup", "HtmlOption", "HtmlOutput",
            "HtmlParagraph",
            "HtmlParameter", "HtmlPicture", "HtmlPlainText", "HtmlPreformattedText",
            "HtmlProgress",
            "HtmlRb", "HtmlRp", "HtmlRt", "HtmlRtc", "HtmlRuby",
            "HtmlS", "HtmlSample",
            "HtmlScript", "HtmlSection", "HtmlSelect", "HtmlSlot", "HtmlSmall",
            "HtmlSource", "HtmlSpan",
            "HtmlStrike", "HtmlStrong", "HtmlStyle",
            "HtmlSubscript", "HtmlSummary", "HtmlSuperscript",
            "HtmlSvg",
            "HtmlTable", "HtmlTableColumn", "HtmlTableColumnGroup",
            "HtmlTableBody", "HtmlTableDataCell", "HtmlTableHeaderCell",
            "HtmlTableRow", "HtmlTextArea", "HtmlTableFooter",
            "HtmlTableHeader", "HtmlTeletype", "HtmlTemplate", "HtmlTrack",
            "HtmlTime", "HtmlTitle",
            "HtmlUnderlined", "HtmlUnorderedList",
            "HtmlVariable", "HtmlVideo",
            "HtmlWordBreak", "HtmlExample"
        };

        final HashSet<String> supportedTags = new HashSet<>(DefaultElementFactory.SUPPORTED_TAGS_);
        final List<DynamicTest> tests = new ArrayList<>();

        for (final String testClass : classesToTest) {
            final Class<?> clazz = Class.forName("org.htmlunit.html." + testClass);
            tests.addAll(createTestsForClass(clazz));

            String tag;
            if (DomComment.class == clazz) {
                tag = "comment";
            }
            else {
                tag = (String) clazz.getField("TAG_NAME").get(null);
            }
            supportedTags.remove(tag);
            try {
                tag = (String) clazz.getField("TAG_NAME2").get(null);
                supportedTags.remove(tag);
            }
            catch (final NoSuchFieldException ignored) {
                // ignore
            }
        }

        supportedTags.remove("input");

        if (!supportedTags.isEmpty()) {
            throw new RuntimeException("Missing tag class(es) " + supportedTags);
        }
        return tests.stream();
    }

    /**
     * Creates all the tests for a given class.
     *
     * @param clazz the class to create tests for
     * @throws Exception if the tests cannot be created
     */
    private List<DynamicTest> createTestsForClass(final Class<?> clazz) throws Exception {
        final List<DynamicTest> tests = new ArrayList<>();
        final Method[] methods = clazz.getMethods();

        for (final Method method : methods) {
            final String methodName = method.getName();
            if (methodName.startsWith("get")
                && methodName.endsWith("Attribute")
                && !EXCLUDED_METHODS.contains(methodName)) {

                final String attributeName =
                        normalizeAttributeName(
                                methodName.substring(3, methodName.length() - 9).toLowerCase(Locale.ROOT));

                final String testName = createTestName(clazz, method);
                tests.add(DynamicTest.dynamicTest(testName, () -> {
                    executeAttributeTest(clazz, method, attributeName);
                }));
            }
        }
        return tests;
    }

    /**
     * Normalizes attribute names for special cases.
     */
    private static String normalizeAttributeName(final String attributeName) {
        switch (attributeName) {
            case "xmllang": return "xml:lang";
            case "columns": return "cols";
            case "columnspan": return "colspan";
            case "textdirection": return "dir";
            case "httpequiv": return "http-equiv";
            case "acceptcharset": return "accept-charset";
            case "htmlfor": return "for";
            default: return attributeName;
        }
    }

    /**
     * Executes the actual test for a specific attribute.
     * @param classUnderTest the class under test
     * @param method the getter method
     * @param attributeName the attribute name
     * @throws Exception if the test fails
     */
    private static void executeAttributeTest(final Class<?> classUnderTest,
            final Method method, final String attributeName) throws Exception {
        try (WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
            final MockWebConnection connection = new MockWebConnection();
            connection.setDefaultResponse("<html><head><title>foo</title></head><body></body></html>");
            webClient.setWebConnection(connection);
            final HtmlPage page = webClient.getPage(WebTestCase.URL_FIRST);
            final String value = "value";
            final DomElement objectToTest = getNewInstanceForClassUnderTest(classUnderTest, page);
            objectToTest.setAttribute(attributeName, value);
            final Object[] noObjects = new Object[0];
            final Object result = method.invoke(objectToTest, noObjects);
            Assertions.assertSame(value, result);
        }
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
     * Creates a new instance of the class being tested.
     * @return the new instance
     * @throws Exception if the new object cannot be created
     */
    private static DomElement getNewInstanceForClassUnderTest(
                        final Class<?> classUnderTest, final HtmlPage page) throws Exception {
        final HTMLParser htmlParser = page.getWebClient().getPageCreator().getHtmlParser();
        final DomElement newInstance;
        if (classUnderTest == HtmlTableRow.class) {
            newInstance = htmlParser.getFactory(HtmlTableRow.TAG_NAME)
                    .createElement(page, HtmlTableRow.TAG_NAME, null);
        }
        else if (classUnderTest == HtmlTableHeaderCell.class) {
            newInstance = htmlParser.getFactory(HtmlTableHeaderCell.TAG_NAME)
                    .createElement(page, HtmlTableHeaderCell.TAG_NAME, null);
        }
        else if (classUnderTest == HtmlTableDataCell.class) {
            newInstance = htmlParser.getFactory(HtmlTableDataCell.TAG_NAME)
                    .createElement(page, HtmlTableDataCell.TAG_NAME, null);
        }
        else {
            final String tagName = (String) classUnderTest.getField("TAG_NAME").get(null);
            newInstance = htmlParser.getFactory(tagName).createElement(page, tagName, null);
        }

        return newInstance;
    }
}
