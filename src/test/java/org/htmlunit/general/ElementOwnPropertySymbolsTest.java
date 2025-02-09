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
package org.htmlunit.general;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.javascript.host.Location;
import org.htmlunit.javascript.host.Screen;
import org.htmlunit.javascript.host.crypto.Crypto;
import org.htmlunit.javascript.host.crypto.SubtleCrypto;
import org.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import org.htmlunit.javascript.host.dom.CDATASection;
import org.htmlunit.javascript.host.dom.NodeList;
import org.htmlunit.javascript.host.dom.XPathEvaluator;
import org.htmlunit.javascript.host.dom.XPathResult;
import org.htmlunit.javascript.host.html.HTMLCollection;
import org.htmlunit.javascript.host.performance.Performance;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.junit.BrowserVersionClassRunner;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.util.SortOrder;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests own properties of an object.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ElementOwnPropertySymbolsTest extends WebDriverTestCase {

    private static BrowserVersion BROWSER_VERSION_;

    private void test(final String tagName) throws Exception {
        testString("", "document.createElement('" + tagName + "')");
    }

    private void testString(final String preparation, final String string) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "  function test(event) {\n"
                + "    var xmlDocument = document.implementation.createDocument('', '', null);\n"
                + "    var element = xmlDocument.createElement('wakwak');\n"
                + "    var unknown = document.createElement('harhar');\n"
                + "    var div = document.createElement('div');\n"
                + "    var svg = document.getElementById('mySvg');\n"
                + "    try{\n"
                + "      " + preparation + "\n"
                + "      process(" + string + ");\n"
                + "    } catch(e) {logEx(e);return;}\n"
                + "  }\n"
                + "\n"
                + "  /*\n"
                + "   * Alerts all properties (including functions) of the specified object.\n"
                + "   *\n"
                + "   * @param object the object to write the property of\n"
                + "   */\n"
                + "  function process(object) {\n"
                + "    var all = [];\n"
                + "    var props = Object.getOwnPropertySymbols(object.constructor.prototype);\n"
                + "    for (i = 0; i < props.length; i++) {\n"
                + "      var str = props[i].toString();\n"

                + "      let desc = Object.getOwnPropertyDescriptor(object.constructor.prototype, props[i]);\n"
                + "      str += ' [';\n"
                + "      if (desc.get != undefined) str += 'G';\n"
                + "      if (desc.set != undefined) str += 'S';\n"
                + "      if (desc.writable) str += 'W';\n"
                + "      if (desc.configurable) str += 'C';\n"
                + "      if (desc.enumerable) str += 'E';\n"
                + "      str += ']'\n"

                + "      var val = object[props[i]];\n"
                + "      if (typeof val === 'function') {\n"
                + "        str = str + ' [function]';\n"
                + "      } else if (typeof val === 'string') {\n"
                + "        str = str + ' [' + val + ']';\n"
                + "      } else {\n"
                + "        str = str + ' [' + JSON.stringify(val) + ']';\n"
                + "      }\n"
                + "      all.push(str);\n"
                + "    }\n"

                + "    all.sort(sortFunction);\n"
                + "    if (all.length == 0) { all = '-' };\n"
                + "    log(all);\n"
                + "  }\n"
                + "  function sortFunction(s1, s2) {\n"
                + "    var s1lc = s1.toLowerCase();\n"
                + "    var s2lc =  s2.toLowerCase();\n"
                + "    if (s1lc > s2lc) { return 1; }\n"
                + "    if (s1lc < s2lc) { return -1; }\n"
                + "    return s1 > s2 ? 1 : -1;\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test(event)'>\n"
                + "  <svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
                + "    <invalid id='mySvg'/>\n"
                + "  </svg>\n"

                + "  <style>\n"
                + "    @page { margin: 1cm; }\n"
                + "  </style>\n"
                + "  <style>\n"
                + "    @media screen { p { background-color:#FFFFFF; }};\n"
                + "  </style>\n"
                + "  <style>\n"
                + "    @font-face { font-family: Delicious; src: url('Delicious-Bold.otf'); };\n"
                + "  </style>\n"
                + "  <style>\n"
                + "    @import 'imp.css';\n"
                + "  </style>\n"
                + "  <style>\n"
                + "    h3 { color: blue;  }\n"
                + "  </style>\n"

                + "  <form name='myForm', id='myFormId'>"
                + "    <input type='radio' name='first'/><input type='radio' name='first'/>"
                + "    <input id='fileItem' type='file' />"
                + "  </form>"

                + LOG_TEXTAREA
                + "</body></html>";

        if (BROWSER_VERSION_ == null) {
            BROWSER_VERSION_ = getBrowserVersion();
        }

        loadPageVerifyTextArea2(html);
    }

    private static List<String> stringAsArray(final String string) {
        if (string.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(string.split(","));
    }

    /**
     * Resets browser-specific values.
     */
    @BeforeClass
    public static void beforeClass() {
        BROWSER_VERSION_ = null;
    }

    /**
     * Saves HTML and PNG files.
     *
     * @throws IOException if an error occurs
     */
    @AfterClass
    public static void saveAll() throws IOException {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        final int[] counts = {0, 0};
        final StringBuilder html = new StringBuilder();
        html.setLength(0);

        collectStatistics(BROWSER_VERSION_, dataset, html, counts);
        saveChart(dataset);

        FileUtils.writeStringToFile(new File(getTargetDirectory()
                + "/ownsymbols-" + BROWSER_VERSION_.getNickname() + ".html"),
                htmlHeader()
                    .append(overview(counts))
                    .append(htmlDetailsHeader())
                    .append(html)
                    .append(htmlDetailsFooter())
                    .append(htmlFooter()).toString(), ISO_8859_1);
    }

    private static void collectStatistics(final BrowserVersion browserVersion, final DefaultCategoryDataset dataset,
            final StringBuilder html, final int[] counts) {
        final Method[] methods = ElementOwnPropertySymbolsTest.class.getMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));
        for (final Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {

                final Alerts alerts = method.getAnnotation(Alerts.class);
                String[] expectedAlerts = {};
                if (BrowserVersionClassRunner.isDefined(alerts.value())) {
                    expectedAlerts = alerts.value();
                }
                else if (browserVersion == BrowserVersion.EDGE) {
                    expectedAlerts = BrowserVersionClassRunner
                            .firstDefinedOrGiven(expectedAlerts, alerts.EDGE(), alerts.DEFAULT());
                }
                else if (browserVersion == BrowserVersion.FIREFOX_ESR) {
                    expectedAlerts = BrowserVersionClassRunner
                            .firstDefinedOrGiven(expectedAlerts, alerts.FF_ESR(), alerts.DEFAULT());
                }
                else if (browserVersion == BrowserVersion.FIREFOX) {
                    expectedAlerts = BrowserVersionClassRunner
                            .firstDefinedOrGiven(expectedAlerts, alerts.FF(), alerts.DEFAULT());
                }
                else if (browserVersion == BrowserVersion.CHROME) {
                    expectedAlerts = BrowserVersionClassRunner
                            .firstDefinedOrGiven(expectedAlerts, alerts.CHROME(), alerts.DEFAULT());
                }

                final List<String> realProperties = stringAsArray(String.join(",", expectedAlerts));
                List<String> simulatedProperties = stringAsArray(String.join(",", expectedAlerts));

                final HtmlUnitNYI htmlUnitNYI = method.getAnnotation(HtmlUnitNYI.class);
                String[] nyiAlerts = {};
                if (htmlUnitNYI != null) {
                    if (browserVersion == BrowserVersion.EDGE) {
                        nyiAlerts = BrowserVersionClassRunner.firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.EDGE());
                    }
                    else if (browserVersion == BrowserVersion.FIREFOX_ESR) {
                        nyiAlerts = BrowserVersionClassRunner.firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.FF_ESR());
                    }
                    else if (browserVersion == BrowserVersion.FIREFOX) {
                        nyiAlerts = BrowserVersionClassRunner.firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.FF());
                    }
                    else if (browserVersion == BrowserVersion.CHROME) {
                        nyiAlerts = BrowserVersionClassRunner.firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.CHROME());
                    }

                    simulatedProperties = stringAsArray(String.join(",", nyiAlerts));
                }

                final List<String> erroredProperties = new ArrayList<>(simulatedProperties);
                erroredProperties.removeAll(realProperties);

                final List<String> implementedProperties = new ArrayList<>(simulatedProperties);
                implementedProperties.retainAll(realProperties);

                counts[1] += implementedProperties.size();
                counts[0] += realProperties.size();

                htmlDetails(method.getName(), html, realProperties, implementedProperties, erroredProperties);

                dataset.addValue(implementedProperties.size(), "Implemented", method.getName());
                dataset.addValue(realProperties.size(),
                        browserVersion.getNickname().replace("FF", "Firefox "),
                       method.getName());
                dataset.addValue(erroredProperties.size(), "Should not be implemented", method.getName());
            }
        }
    }

    private static void saveChart(final DefaultCategoryDataset dataset) throws IOException {
        final JFreeChart chart = ChartFactory.createBarChart(
            "HtmlUnit implemented properties and methods for " + BROWSER_VERSION_.getNickname(), "Objects",
            "Count", dataset, PlotOrientation.HORIZONTAL, true, true, false);
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        final NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        final LayeredBarRenderer renderer = new LayeredBarRenderer();
        plot.setRenderer(renderer);
        plot.setRowRenderingOrder(SortOrder.DESCENDING);
        renderer.setSeriesPaint(0, new GradientPaint(0, 0, Color.green, 0, 0, new Color(0, 64, 0)));
        renderer.setSeriesPaint(1, new GradientPaint(0, 0, Color.blue, 0, 0, new Color(0, 0, 64)));
        renderer.setSeriesPaint(2, new GradientPaint(0, 0, Color.red, 0, 0, new Color(64, 0, 0)));
        ImageIO.write(chart.createBufferedImage(1200, 2400), "png",
            new File(getTargetDirectory() + "/ownsymbols-" + BROWSER_VERSION_.getNickname() + ".png"));
    }

    /**
     * Returns the 'target' directory.
     * @return the 'target' directory
     */
    public static String getTargetDirectory() {
        final String dirName = "./target";
        final File dir = new File(dirName);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new RuntimeException("Could not create artifacts directory");
            }
        }
        return dirName;
    }

    private static StringBuilder htmlHeader() {
        final StringBuilder html = new StringBuilder();
        html.append("<html><head>\n");
        html.append("<style type=\"text/css\">\n");
        html.append("table.bottomBorder { border-collapse:collapse; }\n");
        html.append("table.bottomBorder td, table.bottomBorder th { "
                            + "border-bottom:1px dotted black;padding:5px; }\n");
        html.append("table.bottomBorder td.numeric { text-align:right; }\n");
        html.append("</style>\n");
        html.append("</head><body>\n");

        html.append("<div align='center'>").append("<h2>")
        .append("HtmlUnit implemented symbols for " + BROWSER_VERSION_.getNickname())
        .append("</h2>").append("</div>\n");
        return html;
    }

    private static StringBuilder overview(final int[] counts) {
        final StringBuilder html = new StringBuilder();
        html.append("<table class='bottomBorder'>");
        html.append("<tr>\n");

        html.append("<th>Total Implemented:</th>\n");
        html.append("<td>" + counts[1])
            .append(" (" + Math.round(((double) counts[1]) / counts[0] * 100))
            .append("%)</td>\n");

        html.append("</tr>\n");
        html.append("</table>\n");

        html.append("<p><br></p>\n");

        return html;
    }

    private static StringBuilder htmlFooter() {
        final StringBuilder html = new StringBuilder();

        html.append("<br>").append("Legend:").append("<br>")
        .append("<span style='color: blue'>").append("To be implemented").append("</span>").append("<br>")
        .append("<span style='color: green'>").append("Implemented").append("</span>").append("<br>")
        .append("<span style='color: red'>").append("Should not be implemented").append("</span>");
        html.append("\n");

        html.append("</body>\n");
        html.append("</html>\n");
        return html;
    }

    private static StringBuilder htmlDetailsHeader() {
        final StringBuilder html = new StringBuilder();

        html.append("<table class='bottomBorder' width='100%'>");
        html.append("<tr>\n");
        html.append("<th>Class</th><th>Symbols</th><th>Counts</th>\n");
        html.append("</tr>");
        return html;
    }

    private static StringBuilder htmlDetails(final String name, final StringBuilder html,
            final List<String> realProperties,
            final List<String> implementedProperties, final List<String> erroredProperties) {
        html.append("<tr>").append('\n').append("<td rowspan='2'>").append("<a name='" + name + "'>").append(name)
            .append("</a>").append("</td>").append('\n').append("<td>");
        int implementedCount = 0;

        if (realProperties.isEmpty()) {
            html.append("&nbsp;");
        }
        else if (realProperties.size() == 1
                && realProperties.contains("exception")
                && implementedProperties.size() == 1
                && implementedProperties.contains("exception")
                && erroredProperties.size() == 0) {
            html.append("&nbsp;");
        }
        else {
            for (int i = 0; i < realProperties.size(); i++) {
                final String color;
                if (implementedProperties.contains(realProperties.get(i))) {
                    color = "green";
                    implementedCount++;
                }
                else {
                    color = "blue";
                }
                html.append("<span style='color: " + color + "'>").append(realProperties.get(i)).append("</span>");
                if (i < realProperties.size() - 1) {
                    html.append(',').append(' ');
                }
            }
        }

        html.append("</td>").append("<td>").append(implementedCount).append('/')
            .append(realProperties.size()).append("</td>").append("</tr>").append('\n');
        html.append("<tr>").append("<td>");
        for (int i = 0; i < erroredProperties.size(); i++) {
            html.append("<span style='color: red'>").append(erroredProperties.get(i)).append("</span>");
            if (i < erroredProperties.size() - 1) {
                html.append(',').append(' ');
            }
        }
        if (erroredProperties.isEmpty()) {
            html.append("&nbsp;");
        }
        html.append("</td>")
            .append("<td>").append(erroredProperties.size()).append("</td>").append("</tr>\n");

        return html;
    }

    private static StringBuilder htmlDetailsFooter() {
        final StringBuilder html = new StringBuilder();
        html.append("</table>");
        return html;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Text]")
    public void text() throws Exception {
        testString("", "document.createTextNode('some text')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Attr]")
    public void attr() throws Exception {
        testString("", "document.createAttribute('some_attrib')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Comment]")
    public void comment() throws Exception {
        testString("", "document.createComment('come_comment')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void unknown() throws Exception {
        testString("", "unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void htmlElement() throws Exception {
        testString("", "unknown");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Element}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Element],"
                + "Symbol(Symbol.unscopables) [C] [{\"after\":true,\"append\":true,\"before\":true,"
                    + "\"prepend\":true,\"remove\":true,\"replaceChildren\":true,"
                    + "\"replaceWith\":true,\"slot\":true}]",
            FF = "Symbol(Symbol.toStringTag) [C] [Element],"
                + "Symbol(Symbol.unscopables) [C] [{\"slot\":true,\"before\":true,\"after\":true,"
                    + "\"replaceWith\":true,\"remove\":true,\"prepend\":true,"
                    + "\"append\":true,\"replaceChildren\":true}]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [Element],"
                + "Symbol(Symbol.unscopables) [C] [{\"slot\":true,\"before\":true,\"after\":true,"
                    + "\"replaceWith\":true,\"remove\":true,\"prepend\":true,"
                    + "\"append\":true,\"replaceChildren\":true}]")
    @HtmlUnitNYI(CHROME = "Symbol(Symbol.toStringTag) [C] [Element]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [Element]",
            FF = "Symbol(Symbol.toStringTag) [C] [Element]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [Element]")
    public void element() throws Exception {
        testString("", "element");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Element}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT =
                "Symbol(Symbol.toStringTag) [C] [Element],"
                + "Symbol(Symbol.unscopables) [C] [{\"after\":true,\"append\":true,"
                    + "\"before\":true,\"prepend\":true,\"remove\":true,\"replaceChildren\":true,\"replaceWith\":true,"
                    + "\"slot\":true}]",
            FF = "Symbol(Symbol.toStringTag) [C] [Element],"
                + "Symbol(Symbol.unscopables) [C] [{\"slot\":true,\"before\":true,\"after\":true,\"replaceWith\":true,"
                    + "\"remove\":true,\"prepend\":true,\"append\":true,\"replaceChildren\":true}]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [Element],"
                + "Symbol(Symbol.unscopables) [C] [{\"slot\":true,\"before\":true,\"after\":true,\"replaceWith\":true,"
                    + "\"remove\":true,\"prepend\":true,\"append\":true,\"replaceChildren\":true}]")
    @HtmlUnitNYI(CHROME = "Symbol(Symbol.toStringTag) [C] [Element]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [Element]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [Element]",
            FF = "Symbol(Symbol.toStringTag) [C] [Element]")
    public void element2() throws Exception {
        testString("", "element, document.createDocumentFragment()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void currentStyle() throws Exception {
        testString("", "document.body.currentStyle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Event]")
    public void event() throws Exception {
        testString("", "event");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Window]")
    public void window() throws Exception {
        testString("", "window");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAbbreviated}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void abbr() throws Exception {
        test("abbr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAcronym}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void acronym() throws Exception {
        test("acronym");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAnchor}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLAnchorElement]")
    public void a() throws Exception {
        test("a");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAddress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void address() throws Exception {
        test("address");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlApplet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void applet() throws Exception {
        test("applet");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLAreaElement]")
    public void area() throws Exception {
        test("area");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlArticle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void article() throws Exception {
        test("article");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAside}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void aside() throws Exception {
        test("aside");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAudio}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLAudioElement]")
    public void audio() throws Exception {
        test("audio");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBackgroundSound}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void bgsound() throws Exception {
        test("bgsound");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBase}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLBaseElement]")
    public void base() throws Exception {
        test("base");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBaseFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void basefont() throws Exception {
        test("basefont");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBidirectionalIsolation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void bdi() throws Exception {
        test("bdi");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBidirectionalOverride}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void bdo() throws Exception {
        test("bdo");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBig}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void big() throws Exception {
        test("big");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBlink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void blink() throws Exception {
        test("blink");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBlockQuote}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLQuoteElement]")
    public void blockquote() throws Exception {
        test("blockquote");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLBodyElement]")
    public void body() throws Exception {
        test("body");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBold}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void b() throws Exception {
        test("b");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLBRElement]")
    public void br() throws Exception {
        test("br");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLButtonElement]")
    public void button() throws Exception {
        test("button");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCanvas}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLCanvasElement]")
    public void canvas() throws Exception {
        test("canvas");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTableCaptionElement]")
    public void caption() throws Exception {
        test("caption");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCenter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void center() throws Exception {
        test("center");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCitation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void cite() throws Exception {
        test("cite");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void code() throws Exception {
        test("code");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCommand}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void command() throws Exception {
        test("command");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDataList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLDataListElement]")
    public void datalist() throws Exception {
        test("datalist");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinition}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void dfn() throws Exception {
        test("dfn");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionDescription}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void dd() throws Exception {
        test("dd");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDeletedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLModElement]")
    public void del() throws Exception {
        test("del");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDetails}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLDetailsElement]")
    public void details() throws Exception {
        test("details");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDialog}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLDialogElement]")
    public void dialog() throws Exception {
        test("dialog");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDirectory}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLDirectoryElement]")
    public void dir() throws Exception {
        test("dir");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDivision}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLDivElement]")
    public void div() throws Exception {
        test("div");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLDListElement]")
    public void dl() throws Exception {
        test("dl");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionTerm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void dt() throws Exception {
        test("dt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLEmbedElement]")
    public void embed() throws Exception {
        test("embed");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlEmphasis}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void em() throws Exception {
        test("em");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFieldSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLFieldSetElement]")
    public void fieldset() throws Exception {
        test("fieldset");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFigureCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void figcaption() throws Exception {
        test("figcaption");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFigure}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void figure() throws Exception {
        test("figure");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLFontElement]")
    public void font() throws Exception {
        test("font");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlForm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLFormElement]")
    public void form() throws Exception {
        test("form");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.xml.FormData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [FormData]")
    public void formData() throws Exception {
        testString("", "new FormData()");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void footer() throws Exception {
        test("footer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLFrameElement]")
    public void frame() throws Exception {
        test("frame");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFrameSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLFrameSetElement]")
    public void frameset() throws Exception {
        test("frameset");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHead}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLHeadElement]")
    public void head() throws Exception {
        test("head");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void header() throws Exception {
        test("header");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading1}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]")
    public void h1() throws Exception {
        test("h1");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading2}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]")
    public void h2() throws Exception {
        test("h2");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading3}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]")
    public void h3() throws Exception {
        test("h3");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading4}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]")
    public void h4() throws Exception {
        test("h4");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading5}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]")
    public void h5() throws Exception {
        test("h5");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading6}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]")
    public void h6() throws Exception {
        test("h6");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHorizontalRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLHRElement]")
    public void hr() throws Exception {
        test("hr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHtml}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLHtmlElement]")
    public void html() throws Exception {
        test("html");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInlineFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLIFrameElement]")
    public void iframe() throws Exception {
        test("iframe");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInlineQuotation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLQuoteElement]")
    public void q() throws Exception {
        test("q");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLImageElement]")
    public void img() throws Exception {
        test("img");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            FF = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void image() throws Exception {
        test("image");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLModElement]")
    public void ins() throws Exception {
        test("ins");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlIsIndex}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void isindex() throws Exception {
        test("isindex");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlItalic}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void i() throws Exception {
        test("i");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlKeyboard}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void kbd() throws Exception {
        test("kbd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void keygen() throws Exception {
        test("keygen");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLabel}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLLabelElement]")
    public void label() throws Exception {
        test("label");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            FF = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void layer() throws Exception {
        test("layer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLegend}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLLegendElement]")
    public void legend() throws Exception {
        test("legend");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlListing}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLPreElement]")
    public void listing() throws Exception {
        test("listing");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlListItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLLIElement]")
    public void li() throws Exception {
        test("li");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLLinkElement]")
    public void link() throws Exception {
        test("link");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMain}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void main() throws Exception {
        test("main");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMap}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLMapElement]")
    public void map() throws Exception {
        test("map");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMark}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void mark() throws Exception {
        test("mark");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMarquee}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLMarqueeElement]")
    public void marquee() throws Exception {
        test("marquee");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMenu}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLMenuElement]")
    public void menu() throws Exception {
        test("menu");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMenuItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void menuitem() throws Exception {
        test("menuitem");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMeta}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLMetaElement]")
    public void meta() throws Exception {
        test("meta");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMeter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLMeterElement]")
    public void meter() throws Exception {
        test("meter");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMultiColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void multicol() throws Exception {
        test("multicol");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNav}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void nav() throws Exception {
        test("nav");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNextId}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void nextid() throws Exception {
        test("nextid");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void nobr() throws Exception {
        test("nobr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void noembed() throws Exception {
        test("noembed");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoFrames}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void noframes() throws Exception {
        test("noframes");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            FF = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void nolayer() throws Exception {
        test("nolayer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void noscript() throws Exception {
        test("noscript");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlObject}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLObjectElement]")
    public void object() throws Exception {
        test("object");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOrderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLOListElement]")
    public void ol() throws Exception {
        test("ol");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOptionGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLOptGroupElement]")
    public void optgroup() throws Exception {
        test("optgroup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLOptionElement]")
    public void option() throws Exception {
        test("option");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOutput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLOutputElement]")
    public void output() throws Exception {
        test("output");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlParagraph}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLParagraphElement]")
    public void p() throws Exception {
        test("p");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlParameter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLParamElement]")
    public void param() throws Exception {
        test("param");
    }

    /**
     * Test {@link Performance}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Performance]")
    public void performance() throws Exception {
        testString("", "performance");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPlainText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void plaintext() throws Exception {
        test("plaintext");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPreformattedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLPreElement]")
    public void pre() throws Exception {
        test("pre");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlProgress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLProgressElement]")
    public void progress() throws Exception {
        test("progress");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRb}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void rb() throws Exception {
        test("rb");
    }

    /**
     * Test HtmlRbc.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void rbc() throws Exception {
        test("rbc");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRp}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void rp() throws Exception {
        test("rp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRt}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void rt() throws Exception {
        test("rt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRtc}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void rtc() throws Exception {
        test("rtc");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRuby}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void ruby() throws Exception {
        test("ruby");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlS}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void s() throws Exception {
        test("s");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void samp() throws Exception {
        test("samp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLScriptElement]")
    public void script() throws Exception {
        test("script");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void section() throws Exception {
        test("section");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLSelectElement]")
    public void select() throws Exception {
        test("select");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLOptionsCollection]")
    public void optionsCollection() throws Exception {
        testString("var sel = document.createElement('select')", "sel.options");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSmall}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void small() throws Exception {
        test("small");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSource}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLSourceElement]")
    public void source() throws Exception {
        test("source");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSpan}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLSpanElement]")
    public void span() throws Exception {
        test("span");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStrike}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void strike() throws Exception {
        test("strike");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStrong}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void strong() throws Exception {
        test("strong");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStyle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLStyleElement]")
    public void style() throws Exception {
        test("style");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSubscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void sub() throws Exception {
        test("sub");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSummary}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void summary() throws Exception {
        test("summary");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void sup() throws Exception {
        test("sup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSvg}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void svg() throws Exception {
        test("svg");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTableElement]")
    public void table() throws Exception {
        test("table");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTableColElement]")
    public void col() throws Exception {
        test("col");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableColumnGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTableColElement]")
    public void colgroup() throws Exception {
        test("colgroup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTableSectionElement]")
    public void tbody() throws Exception {
        test("tbody");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableDataCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTableCellElement]")
    public void td() throws Exception {
        test("td");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableHeaderCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTableCellElement]")
    public void th() throws Exception {
        test("th");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableRow}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTableRowElement]")
    public void tr() throws Exception {
        test("tr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTextArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTextAreaElement]")
    public void textarea() throws Exception {
        test("textarea");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTableSectionElement]")
    public void tfoot() throws Exception {
        test("tfoot");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTableSectionElement]")
    public void thead() throws Exception {
        test("thead");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTeletype}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void tt() throws Exception {
        test("tt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTime}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTimeElement]")
    public void time() throws Exception {
        test("time");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTitle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTitleElement]")
    public void title() throws Exception {
        test("title");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTrack}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTrackElement]")
    public void track() throws Exception {
        test("track");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlUnderlined}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void u() throws Exception {
        test("u");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlUnorderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUListElement]")
    public void ul() throws Exception {
        test("ul");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlVariable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void var() throws Exception {
        test("var");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlVideo}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLVideoElement]")
    public void video() throws Exception {
        test("video");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlWordBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLElement]")
    public void wbr() throws Exception {
        test("wbr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlExample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLPreElement]")
    public void xmp() throws Exception {
        test("xmp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLInputElement]")
    public void input() throws Exception {
        test("input");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLDataElement]")
    public void data() throws Exception {
        test("data");
    }

    /**
     * Test HtmlContent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void content() throws Exception {
        test("content");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPicture}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLPictureElement]")
    public void picutre() throws Exception {
        test("picture");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTemplate}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLTemplateElement]")
    public void template() throws Exception {
        test("template");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.KeyboardEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [KeyboardEvent]")
    public void keyboardEvent() throws Exception {
        testString("", "document.createEvent('KeyboardEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.UIEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Event]")
    public void event2() throws Exception {
        testString("", "document.createEvent('Event')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.UIEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [UIEvent]")
    public void uiEvent() throws Exception {
        testString("", "document.createEvent('UIEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.URL}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [URL]")
    public void url() throws Exception {
        testString("", "new URL('http://developer.mozilla.org')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.URL}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [URL]")
    public void webkitURL() throws Exception {
        testString("", "new webkitURL('http://developer.mozilla.org')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.DragEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [DragEvent]")
    public void dragEvent() throws Exception {
        testString("", "document.createEvent('DragEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.PointerEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [PointerEvent]")
    public void pointerEvent() throws Exception {
        testString("", "new PointerEvent('click')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.PointerEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NotSupportedError/DOMException")
    public void pointerEvent2() throws Exception {
        testString("", " document.createEvent('PointerEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.WheelEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [WheelEvent]",
            FF = "NotSupportedError/DOMException",
            FF_ESR = "NotSupportedError/DOMException")
    public void wheelEvent() throws Exception {
        testString("", "document.createEvent('WheelEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MouseEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [MouseEvent]")
    public void mouseEvent() throws Exception {
        testString("", "document.createEvent('MouseEvent')");
    }



    /**
     * Test {@link org.htmlunit.javascript.host.event.CompositionEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [CompositionEvent]")
    public void compositionEvent() throws Exception {
        testString("", "document.createEvent('CompositionEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.FocusEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [FocusEvent]")
    public void focusEvent() throws Exception {
        testString("", "document.createEvent('FocusEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.InputEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [InputEvent]")
    public void inputEvent() throws Exception {
        testString("", "new InputEvent('input')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MouseWheelEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NotSupportedError/DOMException")
    public void mouseWheelEvent() throws Exception {
        testString("", "document.createEvent('MouseWheelEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.SVGZoomEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NotSupportedError/DOMException")
    public void svgZoomEvent() throws Exception {
        testString("", "document.createEvent('SVGZoomEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.TextEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [TextEvent]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [CompositionEvent]")
    public void textEvent() throws Exception {
        testString("", "document.createEvent('TextEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.TouchEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [TouchEvent]",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    public void touchEvent2() throws Exception {
        testString("", "new TouchEvent('touch')");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSlot}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLSlotElement]")
    public void slot() throws Exception {
        test("slot");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDocument}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Document],"
                + "Symbol(Symbol.unscopables) [C] [{\"append\":true,\"fullscreen\":true,"
                    + "\"prepend\":true,\"replaceChildren\":true}]",
            FF = "Symbol(Symbol.toStringTag) [C] [Document],"
                    + "Symbol(Symbol.unscopables) [C] [{\"fullscreen\":true,\"prepend\":true,"
                    + "\"append\":true,\"replaceChildren\":true}]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [Document],"
                    + "Symbol(Symbol.unscopables) [C] [{\"fullscreen\":true,\"prepend\":true,"
                    + "\"append\":true,\"replaceChildren\":true}]")
    @HtmlUnitNYI(CHROME = "TypeError",
            EDGE = "TypeError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    public void document() throws Exception {
        testString("", "new Document()");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDocument}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [HTMLDocument]")
    public void htmlDocument() throws Exception {
        testString("", "document");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Document}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [XMLDocument]")
    public void xmlDocument() throws Exception {
        testString("", "xmlDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [SVGElement]")
    public void svgElement() throws Exception {
        testString("", "svg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Attr]")
    public void nodeAndAttr() throws Exception {
        testString("", "document.createAttribute('some_attrib')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Range]")
    public void range() throws Exception {
        testString("", "document.createRange()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [DocumentFragment],"
                + "Symbol(Symbol.unscopables) [C] [{\"append\":true,\"prepend\":true,\"replaceChildren\":true}]",
            FF = "Symbol(Symbol.toStringTag) [C] [DocumentFragment],"
                + "Symbol(Symbol.unscopables) [C] [{\"prepend\":true,\"append\":true,\"replaceChildren\":true}]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [DocumentFragment],"
                + "Symbol(Symbol.unscopables) [C] [{\"prepend\":true,\"append\":true,\"replaceChildren\":true}]")
    @HtmlUnitNYI(CHROME = "Symbol(Symbol.toStringTag) [C] [DocumentFragment]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [DocumentFragment]",
            FF = "Symbol(Symbol.toStringTag) [C] [DocumentFragment]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [DocumentFragment]")
    public void documentFragment() throws Exception {
        testString("", "document.createDocumentFragment()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [AudioContext]")
    public void audioContext() throws Exception {
        testString("", "new AudioContext()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [OfflineAudioContext]")
    public void offlineAudioContext() throws Exception {
        testString("", "new OfflineAudioContext({length: 44100 * 1, sampleRate: 44100})");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [AudioParam]")
    public void audioParam() throws Exception {
        testString("var audioCtx = new AudioContext(); var gainNode = new GainNode(audioCtx);", "gainNode.gain");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [GainNode]")
    public void gainNode() throws Exception {
        testString("var audioCtx = new AudioContext();", "new GainNode(audioCtx)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [BeforeUnloadEvent]")
    public void beforeUnloadEvent() throws Exception {
        testString("", "document.createEvent('BeforeUnloadEvent')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [CloseEvent]")
    public void closeEvent() throws Exception {
        testString("", "new CloseEvent('type-close')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [BlobEvent]")
    public void blobEvent() throws Exception {
        testString("var debug = {hello: 'world'};"
                    + "var blob = new Blob([JSON.stringify(debug, null, 2)], {type : 'application/json'});",
                    "new BlobEvent('blob', { 'data': blob })");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.TouchEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [TouchEvent]",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    public void touchEvent() throws Exception {
        testString("", "new TouchEvent('touch')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.DeviceMotionEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [DeviceMotionEvent]")
    public void deviceMotionEvent() throws Exception {
        testString("", "new DeviceMotionEvent('motion')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.ErrorEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [ErrorEvent]")
    public void errorEvent() throws Exception {
        testString("", "new ErrorEvent('error')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.GamepadEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [GamepadEvent]")
    public void gamepadEvent() throws Exception {
        testString("", "new GamepadEvent('gamepad')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MutationEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "NotSupportedError/DOMException",
            FF = "Symbol(Symbol.toStringTag) [C] [MutationEvent]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [MutationEvent]")
    public void mutationEvent() throws Exception {
        testString("", "document.createEvent('MutationEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.OfflineAudioCompletionEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NotSupportedError/DOMException")
    public void offlineAudioCompletionEvent() throws Exception {
        testString("", "document.createEvent('OfflineAudioCompletionEvent')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [PageTransitionEvent]")
    public void pageTransitionEvent() throws Exception {
        testString("", "new PageTransitionEvent('transition')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.media.SourceBufferList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [SourceBufferList]")
    @HtmlUnitNYI(CHROME = "TypeError",
            EDGE = "TypeError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    public void sourceBufferList() throws Exception {
        testString("var mediaSource = new MediaSource;", "mediaSource.sourceBuffers");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]")
    public void htmlCollection() throws Exception {
        testString("", "document.getElementsByTagName('div')");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]")
    public void htmlCollectionDocumentAnchors() throws Exception {
        testString("", "document.anchors");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]")
    public void htmlCollectionDocumentApplets() throws Exception {
        testString("", "document.applets");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]")
    public void htmlCollectionDocumentEmbeds() throws Exception {
        testString("", "document.embeds");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]")
    public void htmlCollectionDocumentForms() throws Exception {
        testString("", "document.forms");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]")
    public void htmlCollectionDocumentImages() throws Exception {
        testString("", "document.images");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]")
    public void htmlCollectionDocumentLinks() throws Exception {
        testString("", "document.links");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]")
    public void htmlCollectionDocumentScripts() throws Exception {
        testString("", "document.scripts");
    }

    /**
     * Test {@link NodeList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [NodeList]")
    public void nodeListElementById() throws Exception {
        testString("", "document.getElementById('myLog').childNodes");
    }

    /**
     * Test {@link NodeList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [NodeList]")
    public void nodeListElementsByName() throws Exception {
        testString("", "document.getElementsByName('myLog')");
    }

    /**
     * Test {@link NodeList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [NodeList]")
    public void nodeListButtonLabels() throws Exception {
        testString("var button = document.createElement('button');", "button.labels");
    }

    /**
     * Test {@link ComputedCSSStyleDeclaration}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [CSSStyleDeclaration]",
            FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [CSS2Properties]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [CSS2Properties]")
    public void computedStyle() throws Exception {
        testString("", "window.getComputedStyle(document.body)");
    }

    /**
     * Test {@link ComputedCSSStyleDeclaration}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [CSSStyleDeclaration]",
            FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [CSS2Properties]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [CSS2Properties]")
    @HtmlUnitNYI(FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [CSSStyleDeclaration]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [CSSStyleDeclaration]")
    public void cssStyleDeclaration() throws Exception {
        testString("", "document.body.style");
    }

    /**
     * Test {@link Location}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Location]")
    public void location() throws Exception {
        testString("", "window.location");
        testString("", "document.location");
    }

    /**
     * Test {@link Screen}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Screen]")
    public void screen() throws Exception {
        testString("", "window.screen");
    }

    /**
     * Test {@link Screen}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [ScreenOrientation]")
    public void screenOrientation() throws Exception {
        testString("", "window.screen.orientation");
    }

    /**
     * Test {@link Crypto}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Crypto]")
    public void crypto() throws Exception {
        testString("", "window.crypto");
    }

    /**
     * Test {@link SubtleCrypto}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [SubtleCrypto]")
    public void cryptoSubtle() throws Exception {
        testString("", "window.crypto.subtle");
    }

    /**
     * Test {@link XPathEvaluator}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [XPathEvaluator]")
    public void xPathEvaluator() throws Exception {
        testString("", "new XPathEvaluator()");
    }

    /**
     * Test {@link XPathExpression}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [XPathExpression]")
    public void xPathExpression() throws Exception {
        testString("var res = new XPathEvaluator().createExpression('//span')", "res");
    }

    /**
     * Test {@link XPathResult}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [XPathResult]")
    public void xPathResult() throws Exception {
        testString("var res = document.evaluate('/html/body', document, null, XPathResult.ANY_TYPE, null);", "res");
    }

    /**
     * Test {@link CDATASection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [CDATASection]")
    public void cDATASection() throws Exception {
        final String setup = " var doc = document.implementation.createDocument('', '', null);\n"
                + "var root = doc.appendChild(doc.createElement('root'));\n"
                + "var cdata = root.appendChild(doc.createCDATASection('abcdef'));\n";

        testString(setup, "cdata");
    }

    /**
     * Test {@link CDATASection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [DocumentType],"
                + "Symbol(Symbol.unscopables) [C] "
                    + "[{\"after\":true,\"before\":true,\"remove\":true,\"replaceWith\":true}]",
            FF = "Symbol(Symbol.toStringTag) [C] [DocumentType],"
                + "Symbol(Symbol.unscopables) [C] "
                    + "[{\"before\":true,\"after\":true,\"replaceWith\":true,\"remove\":true}]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [DocumentType],"
                + "Symbol(Symbol.unscopables) [C] "
                    + "[{\"before\":true,\"after\":true,\"replaceWith\":true,\"remove\":true}]")
    @HtmlUnitNYI(CHROME = "Symbol(Symbol.toStringTag) [C] [DocumentType]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [DocumentType]",
            FF = "Symbol(Symbol.toStringTag) [C] [DocumentType]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [DocumentType]")
    public void documentType() throws Exception {
        testString("", "document.firstChild");
    }

    /**
     * Test Blob.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [Blob]")
    public void blob() throws Exception {
        testString("", "new Blob([1, 2], { type: \"text/html\" })");
    }

    /**
     * Test URLSearchParams.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [URLSearchParams]")
    public void urlSearchParams() throws Exception {
        testString("", "new URLSearchParams('q=URLUtils.searchParams&topic=api')");
    }

    /**
     * Test NamedNodeMap.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [NamedNodeMap]")
    public void namedNodeMap() throws Exception {
        testString("", "element.attributes");
    }

    /**
     * Test MutationObserver.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [MutationObserver]")
    public void mutationObserver() throws Exception {
        testString("", "new MutationObserver(function(m) {})");
    }

    /**
     * Test WebKitMutationObserver.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [MutationObserver]",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    public void webKitMutationObserver() throws Exception {
        testString("", "new WebKitMutationObserver(function(m) {})");
    }

    /**
     * Test StyleSheet.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [CSSStyleSheet]")
    public void cssStyleSheet() throws Exception {
        testString("", "document.styleSheets[0]");
    }

    /**
     * Test CSSPageRule.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [CSSPageRule]")
    public void cssPageRule() throws Exception {
        testString("", "document.styleSheets[0].cssRules[0]");
    }

    /**
     * Test CSSMediaRule.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [CSSMediaRule]")
    public void cssMediaRule() throws Exception {
        testString("", "document.styleSheets[1].cssRules[0]");
    }

    /**
     * Test CSSFontFaceRule.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [CSSFontFaceRule]")
    public void cssFontFaceRule() throws Exception {
        testString("", "document.styleSheets[2].cssRules[0]");
    }

    /**
     * Test CSSImportRule.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [CSSImportRule]")
    public void cssImportRule() throws Exception {
        testString("", "document.styleSheets[3].cssRules[0]");
    }

    /**
     * Test CSSRule.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Symbol(Symbol.toStringTag) [C] [CSSStyleRule]")
    public void cssStyleRule() throws Exception {
        testString("", "document.styleSheets[4].cssRules[0]");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.geo.Geolocation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.toStringTag) [C] [Geolocation]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [Geolocation]",
            FF = "Symbol(Symbol.toStringTag) [C] [Geolocation]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [Geolocation]")
    public void geolocation() throws Exception {
        testString("", " navigator.geolocation");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.geo.Geolocation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.toStringTag) [C] [XMLHttpRequest]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [XMLHttpRequest]",
            FF = "Symbol(Symbol.toStringTag) [C] [XMLHttpRequest]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [XMLHttpRequest]")
    public void xmlHttpRequest() throws Exception {
        testString("", "new XMLHttpRequest()");
    }

    /**
     * Test Request.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.toStringTag) [C] [Request]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [Request]",
            FF = "Symbol(Symbol.toStringTag) [C] [Request]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [Request]")
    public void request() throws Exception {
        testString("", "new Request('https://www.htmlunit.org')");
    }

    /**
     * Test Response.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.toStringTag) [C] [Response]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [Response]",
            FF = "Symbol(Symbol.toStringTag) [C] [Response]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [Response]")
    public void response() throws Exception {
        testString("", "new Response()");
    }

    /**
     * Test RadioNodeList.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [RadioNodeList]",
            EDGE = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [RadioNodeList]",
            FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [RadioNodeList]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [RadioNodeList]")
    public void radioNodeList() throws Exception {
        testString("", "document.myForm.first");
    }

    /**
     * Test HTMLFormControlsCollection.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.iterator) [WC] [function],"
                    + "Symbol(Symbol.toStringTag) [C] [HTMLFormControlsCollection]",
            EDGE = "Symbol(Symbol.iterator) [WC] [function],"
                    + "Symbol(Symbol.toStringTag) [C] [HTMLFormControlsCollection]",
            FF = "Symbol(Symbol.iterator) [WC] [function],"
                    + "Symbol(Symbol.toStringTag) [C] [HTMLFormControlsCollection]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],"
                    + "Symbol(Symbol.toStringTag) [C] [HTMLFormControlsCollection]")
    public void htmlFormControlsCollection() throws Exception {
        testString("", "document.myForm.elements");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.abort.AbortController}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.toStringTag) [C] [AbortController]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [AbortController]",
            FF = "Symbol(Symbol.toStringTag) [C] [AbortController]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [AbortController]")
    public void abortController() throws Exception {
        testString("", "new AbortController()");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.abort.AbortSignal}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.toStringTag) [C] [AbortSignal]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [AbortSignal]",
            FF = "Symbol(Symbol.toStringTag) [C] [AbortSignal]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [AbortSignal]")
    public void abortSignal() throws Exception {
        testString("", "new AbortController().signal");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DOMTokenList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [DOMTokenList]",
            EDGE = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [DOMTokenList]",
            FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [DOMTokenList]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [DOMTokenList]")
    public void domTokenList() throws Exception {
        testString("", "document.body.classList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.draganddrop.DataTransfer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.toStringTag) [C] [DataTransfer]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [DataTransfer]",
            FF = "Symbol(Symbol.toStringTag) [C] [DataTransfer]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [DataTransfer]")
    public void dataTransfer() throws Exception {
        testString("", "new DataTransfer()");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.draganddrop.DataTransferItemList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [DataTransferItemList]",
            EDGE = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [DataTransferItemList]",
            FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [DataTransferItemList]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [DataTransferItemList]")
    public void dataTransferItemList() throws Exception {
        testString("", "new DataTransfer().items");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.file.FileList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [FileList]",
            EDGE = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [FileList]",
            FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [FileList]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [FileList]")
    public void fileList() throws Exception {
        testString("", "new DataTransfer().files");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.file.FileList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [FileList]",
            EDGE = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [FileList]",
            FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [FileList]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [FileList]")
    public void fileList2() throws Exception {
        testString("", "document.getElementById('fileItem').files");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.PluginArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [PluginArray]",
            EDGE = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [PluginArray]",
            FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [PluginArray]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [PluginArray]")
    public void pluginArray() throws Exception {
        testString("", "navigator.plugins");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Plugin}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [Plugin]",
            EDGE = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [Plugin]",
            FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [Plugin]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [Plugin]")
    public void plugin() throws Exception {
        testString("", "navigator.plugins[0]");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.MimeTypeArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [MimeTypeArray]",
            EDGE = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [MimeTypeArray]",
            FF = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [MimeTypeArray]",
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [MimeTypeArray]")
    public void mimeTypeArray() throws Exception {
        testString("", "navigator.mimeTypes");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.MimeType}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.toStringTag) [C] [MimeType]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [MimeType]",
            FF = "Symbol(Symbol.toStringTag) [C] [MimeType]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [MimeType]")
    public void mimeType() throws Exception {
        testString("", "navigator.mimeTypes[0]");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Navigator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.toStringTag) [C] [Navigator]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [Navigator]",
            FF = "Symbol(Symbol.toStringTag) [C] [Navigator]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [Navigator]")
    public void navigator() throws Exception {
        testString("", "navigator");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DOMException}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "Symbol(Symbol.toStringTag) [C] [DOMException]",
            EDGE = "Symbol(Symbol.toStringTag) [C] [DOMException]",
            FF = "Symbol(Symbol.toStringTag) [C] [DOMException]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [DOMException]")
    public void domException() throws Exception {
        testString("", "new DOMException('message', 'name')");
    }
}
