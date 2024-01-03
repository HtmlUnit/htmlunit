/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
import org.htmlunit.javascript.host.dom.XPathResult;
import org.htmlunit.javascript.host.html.HTMLCollection;
import org.htmlunit.javascript.host.performance.Performance;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
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
                + "    } catch (e) {\n"
                + "      log('exception');\n"
                + "      return;"
                + "    }\n"
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
                if (browserVersion == BrowserVersion.INTERNET_EXPLORER) {
                    expectedAlerts = BrowserVersionClassRunner
                            .firstDefinedOrGiven(expectedAlerts, alerts.IE(), alerts.DEFAULT());
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
                    if (browserVersion == BrowserVersion.INTERNET_EXPLORER) {
                        nyiAlerts = BrowserVersionClassRunner.firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.IE());
                    }
                    else if (browserVersion == BrowserVersion.EDGE) {
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
                        browserVersion.getNickname().replace("FF", "Firefox ").replace("IE", "Internet Explorer "),
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
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Text]",
            IE = "exception")
    public void text() throws Exception {
        testString("", "document.createTextNode('some text')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Attr]",
            IE = "exception")
    public void attr() throws Exception {
        testString("", "document.createAttribute('some_attrib')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Comment]",
            IE = "exception")
    public void comment() throws Exception {
        testString("", "document.createComment('come_comment')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void unknown() throws Exception {
        testString("", "unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
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
                    + "\"append\":true,\"replaceChildren\":true}]",
            IE = "exception")
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
                    + "\"remove\":true,\"prepend\":true,\"append\":true,\"replaceChildren\":true}]",
            IE = "exception")
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
    @Alerts("exception")
    public void currentStyle() throws Exception {
        testString("", "document.body.currentStyle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Event]",
            IE = "exception")
    public void event() throws Exception {
        testString("", "event");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Window]",
            IE = "exception")
    public void window() throws Exception {
        testString("", "window");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAbbreviated}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void abbr() throws Exception {
        test("abbr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAcronym}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void acronym() throws Exception {
        test("acronym");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAnchor}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLAnchorElement]",
            IE = "exception")
    public void a() throws Exception {
        test("a");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAddress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void address() throws Exception {
        test("address");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlApplet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void applet() throws Exception {
        test("applet");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLAreaElement]",
            IE = "exception")
    public void area() throws Exception {
        test("area");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlArticle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void article() throws Exception {
        test("article");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAside}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void aside() throws Exception {
        test("aside");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAudio}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLAudioElement]",
            IE = "exception")
    public void audio() throws Exception {
        test("audio");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBackgroundSound}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void bgsound() throws Exception {
        test("bgsound");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBase}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLBaseElement]",
            IE = "exception")
    public void base() throws Exception {
        test("base");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBaseFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void basefont() throws Exception {
        test("basefont");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBidirectionalIsolation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void bdi() throws Exception {
        test("bdi");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBidirectionalOverride}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void bdo() throws Exception {
        test("bdo");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBig}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void big() throws Exception {
        test("big");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBlink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void blink() throws Exception {
        test("blink");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBlockQuote}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLQuoteElement]",
            IE = "exception")
    public void blockquote() throws Exception {
        test("blockquote");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLBodyElement]",
            IE = "exception")
    public void body() throws Exception {
        test("body");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBold}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void b() throws Exception {
        test("b");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLBRElement]",
            IE = "exception")
    public void br() throws Exception {
        test("br");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLButtonElement]",
            IE = "exception")
    public void button() throws Exception {
        test("button");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCanvas}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLCanvasElement]",
            IE = "exception")
    public void canvas() throws Exception {
        test("canvas");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTableCaptionElement]",
            IE = "exception")
    public void caption() throws Exception {
        test("caption");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCenter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void center() throws Exception {
        test("center");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCitation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void cite() throws Exception {
        test("cite");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void code() throws Exception {
        test("code");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCommand}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void command() throws Exception {
        test("command");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDataList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLDataListElement]",
            IE = "exception")
    public void datalist() throws Exception {
        test("datalist");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinition}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void dfn() throws Exception {
        test("dfn");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionDescription}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void dd() throws Exception {
        test("dd");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDeletedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLModElement]",
            IE = "exception")
    public void del() throws Exception {
        test("del");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDetails}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLDetailsElement]",
            IE = "exception")
    public void details() throws Exception {
        test("details");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDialog}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLDialogElement]",
            IE = "exception")
    public void dialog() throws Exception {
        test("dialog");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDirectory}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLDirectoryElement]",
            IE = "exception")
    public void dir() throws Exception {
        test("dir");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDivision}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLDivElement]",
            IE = "exception")
    public void div() throws Exception {
        test("div");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLDListElement]",
            IE = "exception")
    public void dl() throws Exception {
        test("dl");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionTerm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void dt() throws Exception {
        test("dt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLEmbedElement]",
            IE = "exception")
    public void embed() throws Exception {
        test("embed");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlEmphasis}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void em() throws Exception {
        test("em");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFieldSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLFieldSetElement]",
            IE = "exception")
    public void fieldset() throws Exception {
        test("fieldset");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFigureCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void figcaption() throws Exception {
        test("figcaption");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFigure}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void figure() throws Exception {
        test("figure");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLFontElement]",
            IE = "exception")
    public void font() throws Exception {
        test("font");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlForm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLFormElement]",
            IE = "exception")
    public void form() throws Exception {
        test("form");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.xml.FormData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [FormData]",
            IE = "exception")
    public void formData() throws Exception {
        testString("", "new FormData()");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void footer() throws Exception {
        test("footer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLFrameElement]",
            IE = "exception")
    public void frame() throws Exception {
        test("frame");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFrameSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLFrameSetElement]",
            IE = "exception")
    public void frameset() throws Exception {
        test("frameset");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHead}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLHeadElement]",
            IE = "exception")
    public void head() throws Exception {
        test("head");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void header() throws Exception {
        test("header");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading1}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]",
            IE = "exception")
    public void h1() throws Exception {
        test("h1");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading2}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]",
            IE = "exception")
    public void h2() throws Exception {
        test("h2");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading3}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]",
            IE = "exception")
    public void h3() throws Exception {
        test("h3");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading4}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]",
            IE = "exception")
    public void h4() throws Exception {
        test("h4");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading5}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]",
            IE = "exception")
    public void h5() throws Exception {
        test("h5");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading6}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLHeadingElement]",
            IE = "exception")
    public void h6() throws Exception {
        test("h6");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHorizontalRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLHRElement]",
            IE = "exception")
    public void hr() throws Exception {
        test("hr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHtml}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLHtmlElement]",
            IE = "exception")
    public void html() throws Exception {
        test("html");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInlineFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLIFrameElement]",
            IE = "exception")
    public void iframe() throws Exception {
        test("iframe");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInlineQuotation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLQuoteElement]",
            IE = "exception")
    public void q() throws Exception {
        test("q");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLImageElement]",
            IE = "exception")
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
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void image() throws Exception {
        test("image");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLModElement]",
            IE = "exception")
    public void ins() throws Exception {
        test("ins");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlIsIndex}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void isindex() throws Exception {
        test("isindex");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlItalic}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void i() throws Exception {
        test("i");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlKeyboard}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void kbd() throws Exception {
        test("kbd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void keygen() throws Exception {
        test("keygen");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLabel}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLLabelElement]",
            IE = "exception")
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
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void layer() throws Exception {
        test("layer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLegend}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLLegendElement]",
            IE = "exception")
    public void legend() throws Exception {
        test("legend");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlListing}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLPreElement]",
            IE = "exception")
    public void listing() throws Exception {
        test("listing");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlListItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLLIElement]",
            IE = "exception")
    public void li() throws Exception {
        test("li");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLLinkElement]",
            IE = "exception")
    public void link() throws Exception {
        test("link");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMain}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void main() throws Exception {
        test("main");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMap}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLMapElement]",
            IE = "exception")
    public void map() throws Exception {
        test("map");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMark}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void mark() throws Exception {
        test("mark");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMarquee}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLMarqueeElement]",
            IE = "exception")
    public void marquee() throws Exception {
        test("marquee");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMenu}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLMenuElement]",
            IE = "exception")
    public void menu() throws Exception {
        test("menu");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMenuItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void menuitem() throws Exception {
        test("menuitem");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMeta}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLMetaElement]",
            IE = "exception")
    public void meta() throws Exception {
        test("meta");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMeter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLMeterElement]",
            IE = "exception")
    public void meter() throws Exception {
        test("meter");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMultiColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void multicol() throws Exception {
        test("multicol");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNav}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void nav() throws Exception {
        test("nav");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNextId}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void nextid() throws Exception {
        test("nextid");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void nobr() throws Exception {
        test("nobr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void noembed() throws Exception {
        test("noembed");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoFrames}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
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
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void nolayer() throws Exception {
        test("nolayer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void noscript() throws Exception {
        test("noscript");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlObject}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLObjectElement]",
            IE = "exception")
    public void object() throws Exception {
        test("object");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOrderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLOListElement]",
            IE = "exception")
    public void ol() throws Exception {
        test("ol");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOptionGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLOptGroupElement]",
            IE = "exception")
    public void optgroup() throws Exception {
        test("optgroup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLOptionElement]",
            IE = "exception")
    public void option() throws Exception {
        test("option");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOutput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLOutputElement]",
            IE = "exception")
    public void output() throws Exception {
        test("output");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlParagraph}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLParagraphElement]",
            IE = "exception")
    public void p() throws Exception {
        test("p");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlParameter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLParamElement]",
            IE = "exception")
    public void param() throws Exception {
        test("param");
    }

    /**
     * Test {@link Performance}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Performance]",
            IE = "exception")
    public void performance() throws Exception {
        testString("", "performance");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPlainText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void plaintext() throws Exception {
        test("plaintext");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPreformattedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLPreElement]",
            IE = "exception")
    public void pre() throws Exception {
        test("pre");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlProgress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLProgressElement]",
            IE = "exception")
    public void progress() throws Exception {
        test("progress");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRb}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    @HtmlUnitNYI(FF = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void rb() throws Exception {
        test("rb");
    }

    /**
     * Test HtmlRbc.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void rbc() throws Exception {
        test("rbc");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRp}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    @HtmlUnitNYI(FF = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void rp() throws Exception {
        test("rp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRt}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    @HtmlUnitNYI(FF = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void rt() throws Exception {
        test("rt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRtc}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    @HtmlUnitNYI(FF = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void rtc() throws Exception {
        test("rtc");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRuby}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    @HtmlUnitNYI(FF = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]")
    public void ruby() throws Exception {
        test("ruby");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlS}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void s() throws Exception {
        test("s");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void samp() throws Exception {
        test("samp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLScriptElement]",
            IE = "exception")
    public void script() throws Exception {
        test("script");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void section() throws Exception {
        test("section");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLSelectElement]",
            IE = "exception")
    public void select() throws Exception {
        test("select");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLOptionsCollection]",
            IE = "exception")
    public void optionsCollection() throws Exception {
        testString("var sel = document.createElement('select')", "sel.options");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSmall}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void small() throws Exception {
        test("small");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSource}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLSourceElement]",
            IE = "exception")
    public void source() throws Exception {
        test("source");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSpan}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLSpanElement]",
            IE = "exception")
    public void span() throws Exception {
        test("span");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStrike}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void strike() throws Exception {
        test("strike");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStrong}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void strong() throws Exception {
        test("strong");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStyle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLStyleElement]",
            IE = "exception")
    public void style() throws Exception {
        test("style");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSubscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void sub() throws Exception {
        test("sub");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSummary}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void summary() throws Exception {
        test("summary");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void sup() throws Exception {
        test("sup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSvg}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void svg() throws Exception {
        test("svg");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTableElement]",
            IE = "exception")
    public void table() throws Exception {
        test("table");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTableColElement]",
            IE = "exception")
    public void col() throws Exception {
        test("col");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableColumnGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTableColElement]",
            IE = "exception")
    public void colgroup() throws Exception {
        test("colgroup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTableSectionElement]",
            IE = "exception")
    public void tbody() throws Exception {
        test("tbody");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableDataCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTableCellElement]",
            IE = "exception")
    public void td() throws Exception {
        test("td");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableHeaderCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTableCellElement]",
            IE = "exception")
    public void th() throws Exception {
        test("th");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableRow}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTableRowElement]",
            IE = "exception")
    public void tr() throws Exception {
        test("tr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTextArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTextAreaElement]",
            IE = "exception")
    public void textarea() throws Exception {
        test("textarea");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTableSectionElement]",
            IE = "exception")
    public void tfoot() throws Exception {
        test("tfoot");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTableSectionElement]",
            IE = "exception")
    public void thead() throws Exception {
        test("thead");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTeletype}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void tt() throws Exception {
        test("tt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTime}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTimeElement]",
            IE = "exception")
    public void time() throws Exception {
        test("time");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTitle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTitleElement]",
            IE = "exception")
    public void title() throws Exception {
        test("title");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTrack}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTrackElement]",
            IE = "exception")
    public void track() throws Exception {
        test("track");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlUnderlined}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void u() throws Exception {
        test("u");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlUnorderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUListElement]",
            IE = "exception")
    public void ul() throws Exception {
        test("ul");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlVariable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void var() throws Exception {
        test("var");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlVideo}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLVideoElement]",
            IE = "exception")
    public void video() throws Exception {
        test("video");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlWordBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLElement]",
            IE = "exception")
    public void wbr() throws Exception {
        test("wbr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlExample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLPreElement]",
            IE = "exception")
    public void xmp() throws Exception {
        test("xmp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLInputElement]",
            IE = "exception")
    public void input() throws Exception {
        test("input");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLDataElement]",
            IE = "exception")
    public void data() throws Exception {
        test("data");
    }

    /**
     * Test HtmlContent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLUnknownElement]",
            IE = "exception")
    public void content() throws Exception {
        test("content");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPicture}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLPictureElement]",
            IE = "exception")
    public void picutre() throws Exception {
        test("picture");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTemplate}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLTemplateElement]",
            IE = "exception")
    public void template() throws Exception {
        test("template");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.KeyboardEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [KeyboardEvent]",
            IE = "exception")
    public void keyboardEvent() throws Exception {
        testString("", "document.createEvent('KeyboardEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.UIEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Event]",
            IE = "exception")
    public void event2() throws Exception {
        testString("", "document.createEvent('Event')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.UIEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [UIEvent]",
            IE = "exception")
    public void uiEvent() throws Exception {
        testString("", "document.createEvent('UIEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.URL}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [URL]",
            IE = "exception")
    public void url() throws Exception {
        testString("", "new URL('http://developer.mozilla.org')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.URL}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [URL]",
            IE = "exception")
    public void webkitURL() throws Exception {
        testString("", "new webkitURL('http://developer.mozilla.org')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.DragEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [DragEvent]",
            IE = "exception")
    public void dragEvent() throws Exception {
        testString("", "document.createEvent('DragEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.PointerEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [PointerEvent]",
            IE = "exception")
    public void pointerEvent() throws Exception {
        testString("", "new PointerEvent('click')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.PointerEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception")
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
            FF = "exception",
            FF_ESR = "exception",
            IE = "exception")
    public void wheelEvent() throws Exception {
        testString("", "document.createEvent('WheelEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MouseEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [MouseEvent]",
            IE = "exception")
    public void mouseEvent() throws Exception {
        testString("", "document.createEvent('MouseEvent')");
    }



    /**
     * Test {@link org.htmlunit.javascript.host.event.CompositionEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [CompositionEvent]",
            IE = "exception")
    public void compositionEvent() throws Exception {
        testString("", "document.createEvent('CompositionEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.FocusEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [FocusEvent]",
            IE = "exception")
    public void focusEvent() throws Exception {
        testString("", "document.createEvent('FocusEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.InputEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [InputEvent]",
            IE = "exception")
    public void inputEvent() throws Exception {
        testString("", "new InputEvent('input')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MouseWheelEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception")
    public void mouseWheelEvent() throws Exception {
        testString("", "document.createEvent('MouseWheelEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.SVGZoomEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
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
            FF = "Symbol(Symbol.toStringTag) [C] [CompositionEvent]",
            FF_ESR = "Symbol(Symbol.toStringTag) [C] [CompositionEvent]",
            IE = "exception")
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
            FF = "exception",
            FF_ESR = "exception",
            IE = "exception")
    public void touchEvent2() throws Exception {
        testString("", "new TouchEvent('touch')");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSlot}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLSlotElement]",
            IE = "exception")
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
                    + "\"append\":true,\"replaceChildren\":true}]",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception",
            FF_ESR = "exception")
    public void document() throws Exception {
        testString("", "new Document()");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDocument}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [HTMLDocument]",
            IE = "exception")
    public void htmlDocument() throws Exception {
        testString("", "document");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Document}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [XMLDocument]",
            IE = "exception")
    public void xmlDocument() throws Exception {
        testString("", "xmlDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [SVGElement]",
            IE = "exception")
    public void svgElement() throws Exception {
        testString("", "svg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Attr]",
            IE = "exception")
    public void nodeAndAttr() throws Exception {
        testString("", "document.createAttribute('some_attrib')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Range]",
            IE = "exception")
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
                + "Symbol(Symbol.unscopables) [C] [{\"prepend\":true,\"append\":true,\"replaceChildren\":true}]",
            IE = "exception")
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
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [AudioContext]",
            IE = "exception")
    public void audioContext() throws Exception {
        testString("", "new AudioContext()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [OfflineAudioContext]",
            IE = "exception")
    public void offlineAudioContext() throws Exception {
        testString("", "new OfflineAudioContext({length: 44100 * 1, sampleRate: 44100})");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [AudioParam]",
            IE = "exception")
    public void audioParam() throws Exception {
        testString("var audioCtx = new AudioContext(); var gainNode = new GainNode(audioCtx);", "gainNode.gain");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [GainNode]",
            IE = "exception")
    public void gainNode() throws Exception {
        testString("var audioCtx = new AudioContext();", "new GainNode(audioCtx)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [BeforeUnloadEvent]",
            IE = "exception")
    public void beforeUnloadEvent() throws Exception {
        testString("", "document.createEvent('BeforeUnloadEvent')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [CloseEvent]",
            IE = "exception")
    public void closeEvent() throws Exception {
        testString("", "new CloseEvent('type-close')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [BlobEvent]",
            IE = "exception")
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
            FF = "exception",
            FF_ESR = "exception",
            IE = "exception")
    public void touchEvent() throws Exception {
        testString("", "new TouchEvent('touch')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.DeviceMotionEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [DeviceMotionEvent]",
            IE = "exception")
    public void deviceMotionEvent() throws Exception {
        testString("", "new DeviceMotionEvent('motion')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.ErrorEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [ErrorEvent]",
            IE = "exception")
    public void errorEvent() throws Exception {
        testString("", "new ErrorEvent('error')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.GamepadEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [GamepadEvent]",
            IE = "exception")
    public void gamepadEvent() throws Exception {
        testString("", "new GamepadEvent('gamepad')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MutationEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [MutationEvent]",
            IE = "exception")
    public void mutationEvent() throws Exception {
        testString("", "document.createEvent('MutationEvent')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.OfflineAudioCompletionEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void offlineAudioCompletionEvent() throws Exception {
        testString("", "document.createEvent('OfflineAudioCompletionEvent')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [PageTransitionEvent]",
            IE = "exception")
    public void pageTransitionEvent() throws Exception {
        testString("", "new PageTransitionEvent('transition')");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.media.SourceBufferList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [SourceBufferList]",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception",
            FF_ESR = "exception")
    public void sourceBufferList() throws Exception {
        testString("var mediaSource = new MediaSource;", "mediaSource.sourceBuffers");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]",
            IE = "exception")
    public void htmlCollection() throws Exception {
        testString("", "document.getElementsByTagName('div')");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]",
            IE = "exception")
    public void htmlCollectionDocumentAnchors() throws Exception {
        testString("", "document.anchors");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]",
            IE = "exception")
    public void htmlCollectionDocumentApplets() throws Exception {
        testString("", "document.applets");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]",
            IE = "exception")
    public void htmlCollectionDocumentEmbeds() throws Exception {
        testString("", "document.embeds");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]",
            IE = "exception")
    public void htmlCollectionDocumentForms() throws Exception {
        testString("", "document.forms");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]",
            IE = "exception")
    public void htmlCollectionDocumentImages() throws Exception {
        testString("", "document.images");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]",
            IE = "exception")
    public void htmlCollectionDocumentLinks() throws Exception {
        testString("", "document.links");
    }

    /**
     * Test {@link HTMLCollection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [HTMLCollection]",
            IE = "exception")
    public void htmlCollectionDocumentScripts() throws Exception {
        testString("", "document.scripts");
    }

    /**
     * Test {@link NodeList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [NodeList]",
            IE = "exception")
    public void nodeListElementById() throws Exception {
        testString("", "document.getElementById('myLog').childNodes");
    }

    /**
     * Test {@link NodeList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [NodeList]",
            IE = "exception")
    public void nodeListElementsByName() throws Exception {
        testString("", "document.getElementsByName('myLog')");
    }

    /**
     * Test {@link NodeList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [NodeList]",
            IE = "exception")
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
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [CSS2Properties]",
            IE = "exception")
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
            FF_ESR = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [CSS2Properties]",
            IE = "exception")
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
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Location]",
            IE = "exception")
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
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Screen]",
            IE = "exception")
    public void screen() throws Exception {
        testString("", "window.screen");
    }

    /**
     * Test {@link Screen}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [ScreenOrientation]",
            IE = "exception")
    public void screenOrientation() throws Exception {
        testString("", "window.screen.orientation");
    }

    /**
     * Test {@link Crypto}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Crypto]",
            IE = "exception")
    public void crypto() throws Exception {
        testString("", "window.crypto");
    }

    /**
     * Test {@link SubtleCrypto}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [SubtleCrypto]",
            IE = "exception")
    public void cryptoSubtle() throws Exception {
        testString("", "window.crypto.subtle");
    }

    /**
     * Test {@link XPathResult}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [XPathResult]",
            IE = "exception")
    public void xPathResult() throws Exception {
        testString("var res = document.evaluate('/html/body', document, null, XPathResult.ANY_TYPE, null);", "res");
    }

    /**
     * Test {@link CDATASection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [CDATASection]",
            IE = "exception")
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
                    + "[{\"before\":true,\"after\":true,\"replaceWith\":true,\"remove\":true}]",
            IE = "exception")
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
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [Blob]",
            IE = "exception")
    public void blob() throws Exception {
        testString("", "new Blob([1, 2], { type: \"text/html\" })");
    }

    /**
     * Test URLSearchParams.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [URLSearchParams]",
            IE = "exception")
    public void urlSearchParams() throws Exception {
        testString("", "new URLSearchParams('q=URLUtils.searchParams&topic=api')");
    }

    /**
     * Test NamedNodeMap.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.iterator) [WC] [function],Symbol(Symbol.toStringTag) [C] [NamedNodeMap]",
            IE = "exception")
    public void namedNodeMap() throws Exception {
        testString("", "element.attributes");
    }

    /**
     * Test MutationObserver.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [MutationObserver]",
            IE = "exception")
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
            FF = "exception",
            FF_ESR = "exception",
            IE = "exception")
    public void webKitMutationObserver() throws Exception {
        testString("", "new WebKitMutationObserver(function(m) {})");
    }

    /**
     * Test StyleSheet.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [CSSStyleSheet]",
            IE = "exception")
    public void cssStyleSheet() throws Exception {
        testString("", "document.styleSheets[0]");
    }

    /**
     * Test CSSPageRule.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [CSSPageRule]",
            IE = "exception")
    public void cssPageRule() throws Exception {
        testString("", "document.styleSheets[0].cssRules[0]");
    }

    /**
     * Test CSSMediaRule.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [CSSMediaRule]",
            IE = "exception")
    public void cssMediaRule() throws Exception {
        testString("", "document.styleSheets[1].cssRules[0]");
    }

    /**
     * Test CSSFontFaceRule.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [CSSFontFaceRule]",
            IE = "exception")
    public void cssFontFaceRule() throws Exception {
        testString("", "document.styleSheets[2].cssRules[0]");
    }

    /**
     * Test CSSImportRule.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [CSSImportRule]",
            IE = "exception")
    public void cssImportRule() throws Exception {
        testString("", "document.styleSheets[3].cssRules[0]");
    }

    /**
     * Test CSSRule.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Symbol(Symbol.toStringTag) [C] [CSSStyleRule]",
            IE = "exception")
    public void cssStyleRule() throws Exception {
        testString("", "document.styleSheets[4].cssRules[0]");
    }
}
