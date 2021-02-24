/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.general;

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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.runners.BrowserVersionClassRunner;

/**
 * Tests own properties of an object.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ElementOwnPropertiesTest extends WebDriverTestCase {

    private static BrowserVersion BROWSER_VERSION_;

    private void test(final String tagName) throws Exception {
        testString("", "document.createElement('" + tagName + "')");
    }

    private void testString(final String preparation, final String string) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><script>\n"
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
                + "      alert('exception');\n"
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
                + "    var props = Object.getOwnPropertyNames(object.constructor.prototype);\n"
                + "    for (i = 0; i < props.length; i++) {\n"
                + "      var property = props[i];\n"

                // TODO remove if fixed in Rhino
                + "      if ('__proto__' == property) continue;\n"
                + "      if (typeof object[property] == 'function')\n"
                + "        all.push(property + '()');\n"
                + "      else\n"
                + "        all.push(property);\n"
                + "    }\n"

                + "    all.sort(sortFunction);\n"
                + "    if (all.length == 0) { all = '-' };\n"
                + "    if (all.length > 150) {\n"
                 + "      alert(all.slice(0, 150));\n"
                + "      all = all.slice(150);\n"
                + "    }\n"
                + "    alert(all);\n"
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
                + "</body></html>";

        if (BROWSER_VERSION_ == null) {
            BROWSER_VERSION_ = getBrowserVersion();
        }

        loadPageWithAlerts2(html);
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
                + "/ownproperties-" + BROWSER_VERSION_.getNickname() + ".html"),
                htmlHeader()
                    .append(overview(counts))
                    .append(htmlDetailsHeader())
                    .append(html)
                    .append(htmlDetailsFooter())
                    .append(htmlFooter()).toString(), ISO_8859_1);
    }

    private static void collectStatistics(final BrowserVersion browserVersion, final DefaultCategoryDataset dataset,
            final StringBuilder html, final int[] counts) {
        final Method[] methods = ElementOwnPropertiesTest.class.getMethods();
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
                else if (browserVersion == BrowserVersion.FIREFOX_78) {
                    expectedAlerts = BrowserVersionClassRunner
                            .firstDefinedOrGiven(expectedAlerts, alerts.FF78(), alerts.DEFAULT());
                }
                else if (browserVersion == BrowserVersion.FIREFOX) {
                    expectedAlerts = BrowserVersionClassRunner
                            .firstDefinedOrGiven(expectedAlerts, alerts.FF(), alerts.DEFAULT());
                }
                else if (browserVersion == BrowserVersion.CHROME) {
                    expectedAlerts = BrowserVersionClassRunner
                            .firstDefinedOrGiven(expectedAlerts, alerts.CHROME(), alerts.DEFAULT());
                }

                final HtmlUnitNYI htmlUnitNYI = method.getAnnotation(HtmlUnitNYI.class);
                String[] nyiAlerts = {};
                if (htmlUnitNYI != null) {
                    if (browserVersion == BrowserVersion.INTERNET_EXPLORER) {
                        nyiAlerts = BrowserVersionClassRunner.firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.IE());
                    }
                    else if (browserVersion == BrowserVersion.EDGE) {
                        nyiAlerts = BrowserVersionClassRunner.firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.EDGE());
                    }
                    else if (browserVersion == BrowserVersion.FIREFOX_78) {
                        nyiAlerts = BrowserVersionClassRunner.firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.FF78());
                    }
                    else if (browserVersion == BrowserVersion.FIREFOX) {
                        nyiAlerts = BrowserVersionClassRunner.firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.FF());
                    }
                    else if (browserVersion == BrowserVersion.CHROME) {
                        nyiAlerts = BrowserVersionClassRunner.firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.CHROME());
                    }
                }

                final List<String> realProperties = stringAsArray(String.join(",", expectedAlerts));
                final List<String> simulatedProperties = stringAsArray(String.join(",", nyiAlerts));

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
            new File(getTargetDirectory() + "/ownproperties-" + BROWSER_VERSION_.getNickname() + ".png"));
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
        .append("HtmlUnit implemented properties and methods for " + BROWSER_VERSION_.getNickname())
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
        html.append("<th>Class</th><th>Methods/Properties</th><th>Counts</th>\n");
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
    @Alerts(CHROME = "assignedSlot,constructor(),splitText(),wholeText",
            EDGE = "assignedSlot,constructor(),splitText(),wholeText",
            FF = "assignedSlot,constructor(),splitText(),wholeText",
            FF78 = "assignedSlot,constructor(),splitText(),wholeText",
            IE = "constructor,removeNode(),replaceNode(),replaceWholeText(),splitText(),swapNode(),wholeText")
    @HtmlUnitNYI(CHROME = "constructor(),splitText(),wholeText",
            EDGE = "constructor(),splitText(),wholeText",
            FF78 = "constructor(),splitText(),wholeText",
            FF = "constructor(),splitText(),wholeText",
            IE = "constructor,splitText(),wholeText")
    public void text() throws Exception {
        testString("", "document.createTextNode('some text')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),localName,name,namespaceURI,ownerElement,prefix,specified,value",
            EDGE = "constructor(),localName,name,namespaceURI,ownerElement,prefix,specified,value",
            FF = "constructor(),localName,name,namespaceURI,ownerElement,prefix,specified,value",
            FF78 = "constructor(),localName,name,namespaceURI,ownerElement,prefix,specified,value",
            IE = "constructor,expando,name,ownerElement,specified,value")
    @HtmlUnitNYI(IE = "constructor,expando,localName,name,namespaceURI,ownerElement,prefix,"
                + "specified,value")
    public void attr() throws Exception {
        testString("", "document.createAttribute('some_attrib')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,text")
    @HtmlUnitNYI(IE = "constructor,getAttribute(),getAttributeNode(),text")
    public void comment() throws Exception {
        testString("", "document.createComment('come_comment')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void unknown() throws Exception {
        testString("", "unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void htmlElement() throws Exception {
        testString("", "unknown");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Element}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "after(),animate(),append(),"
                + "ariaAtomic,ariaAutoComplete,ariaBusy,ariaChecked,ariaColCount,ariaColIndex,ariaColSpan,"
                + "ariaCurrent,ariaDescription,ariaDisabled,ariaExpanded,ariaHasPopup,ariaHidden,ariaKeyShortcuts,"
                + "ariaLabel,ariaLevel,ariaLive,ariaModal,ariaMultiLine,ariaMultiSelectable,ariaOrientation,"
                + "ariaPlaceholder,ariaPosInSet,ariaPressed,ariaReadOnly,ariaRelevant,ariaRequired,"
                + "ariaRoleDescription,ariaRowCount,ariaRowIndex,ariaRowSpan,ariaSelected,ariaSetSize,ariaSort,"
                + "ariaValueMax,ariaValueMin,ariaValueNow,ariaValueText,"
                + "assignedSlot,attachShadow(),attributes,attributeStyleMap,before(),"
                + "childElementCount,children,classList,className,clientHeight,clientLeft,clientTop,clientWidth,"
                + "closest(),computedStyleMap(),constructor(),elementTiming,firstElementChild,"
                + "getAnimations(),getAttribute(),getAttributeNames(),getAttributeNode(),getAttributeNodeNS(),"
                + "getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),"
                + "hasAttributeNS(),hasAttributes(),hasPointerCapture(),id,innerHTML,insertAdjacentElement(),"
                + "insertAdjacentHTML(),insertAdjacentText(),lastElementChild,localName,matches(),namespaceURI,"
                + "nextElementSibling,onbeforecopy,onbeforecut,onbeforepaste,onbeforexrselect,onfullscreenchange,"
                + "onfullscreenerror,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,part,prefix,prepend(),"
                + "previousElementSibling,querySelector(),querySelectorAll(),releasePointerCapture(),remove(),"
                + "removeAttribute(),removeAttributeNode(),removeAttributeNS(),replaceChildren(),"
                + "replaceWith(),requestFullscreen(),"
                + "requestPointerLock(),scroll(),scrollBy(),scrollHeight,scrollIntoView(),scrollIntoViewIfNeeded(),"
                + "scrollLeft,scrollTo(),scrollTop,scrollWidth,setAttribute(),setAttributeNode(),"
                + "setAttributeNodeNS(),setAttributeNS(),setPointerCapture(),shadowRoot,slot,tagName,"
                + "toggleAttribute(),webkitMatchesSelector(),webkitRequestFullScreen(),webkitRequestFullscreen()",
            EDGE = "after(),animate(),append(),ariaAtomic,ariaAutoComplete,ariaBusy,ariaChecked,ariaColCount,"
                + "ariaColIndex,ariaColSpan,ariaCurrent,ariaDescription,ariaDisabled,ariaExpanded,ariaHasPopup,"
                + "ariaHidden,ariaKeyShortcuts,ariaLabel,ariaLevel,ariaLive,ariaModal,ariaMultiLine,"
                + "ariaMultiSelectable,ariaOrientation,ariaPlaceholder,ariaPosInSet,ariaPressed,ariaReadOnly,"
                + "ariaRelevant,ariaRequired,ariaRoleDescription,ariaRowCount,ariaRowIndex,ariaRowSpan,ariaSelected,"
                + "ariaSetSize,ariaSort,ariaValueMax,ariaValueMin,ariaValueNow,ariaValueText,assignedSlot,"
                + "attachShadow(),attributes,attributeStyleMap,before(),childElementCount,children,classList,"
                + "className,clientHeight,clientLeft,clientTop,clientWidth,closest(),computedStyleMap(),"
                + "constructor(),elementTiming,firstElementChild,getAnimations(),getAttribute(),"
                + "getAttributeNames(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),"
                + "getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),"
                + "hasAttributeNS(),hasAttributes(),hasPointerCapture(),id,innerHTML,insertAdjacentElement(),"
                + "insertAdjacentHTML(),insertAdjacentText(),lastElementChild,localName,matches(),namespaceURI,"
                + "nextElementSibling,onbeforecopy,onbeforecut,onbeforepaste,onbeforexrselect,onfullscreenchange,"
                + "onfullscreenerror,onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,part,"
                + "prefix,prepend(),previousElementSibling,querySelector(),querySelectorAll(),"
                + "releasePointerCapture(),remove(),removeAttribute(),removeAttributeNode(),removeAttributeNS(),"
                + "replaceChildren(),replaceWith(),requestFullscreen(),"
                + "requestPointerLock(),scroll(),scrollBy(),scrollHeight,"
                + "scrollIntoView(),scrollIntoViewIfNeeded(),scrollLeft,scrollTo(),scrollTop,scrollWidth,"
                + "setAttribute(),setAttributeNode(),setAttributeNodeNS(),setAttributeNS(),setPointerCapture(),"
                + "shadowRoot,slot,tagName,toggleAttribute(),webkitMatchesSelector(),"
                + "webkitRequestFullScreen(),webkitRequestFullscreen()",
            FF = "after(),animate(),append(),assignedSlot,attachShadow(),attributes,before(),childElementCount,"
                + "children,classList,className,clientHeight,clientLeft,clientTop,clientWidth,closest(),"
                + "constructor(),firstElementChild,getAnimations(),getAttribute(),getAttributeNames(),"
                + "getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),"
                + "hasAttributeNS(),hasAttributes(),hasPointerCapture(),id,innerHTML,insertAdjacentElement(),"
                + "insertAdjacentHTML(),insertAdjacentText(),lastElementChild,localName,matches(),"
                + "mozMatchesSelector(),mozRequestFullScreen(),namespaceURI,nextElementSibling,onfullscreenchange,"
                + "onfullscreenerror,outerHTML,part,prefix,prepend(),previousElementSibling,querySelector(),"
                + "querySelectorAll(),releaseCapture(),releasePointerCapture(),remove(),removeAttribute(),"
                + "removeAttributeNode(),removeAttributeNS(),replaceChildren(),replaceWith(),"
                + "requestFullscreen(),requestPointerLock(),"
                + "scroll(),scrollBy(),scrollHeight,scrollIntoView(),scrollLeft,scrollLeftMax,scrollTo(),scrollTop,"
                + "scrollTopMax,scrollWidth,setAttribute(),setAttributeNode(),setAttributeNodeNS(),setAttributeNS(),"
                + "setCapture(),setPointerCapture(),shadowRoot,slot,tagName,toggleAttribute(),"
                + "webkitMatchesSelector()",
            FF78 = "after(),animate(),append(),assignedSlot,attachShadow(),attributes,before(),childElementCount,"
                + "children,classList,className,clientHeight,clientLeft,clientTop,clientWidth,closest(),constructor(),"
                + "firstElementChild,getAnimations(),getAttribute(),getAttributeNames(),getAttributeNode(),"
                + "getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),"
                + "hasAttributeNS(),hasAttributes(),hasPointerCapture(),id,innerHTML,insertAdjacentElement(),"
                + "insertAdjacentHTML(),insertAdjacentText(),lastElementChild,localName,matches(),"
                + "mozMatchesSelector(),mozRequestFullScreen(),namespaceURI,nextElementSibling,onfullscreenchange,"
                + "onfullscreenerror,outerHTML,part,prefix,prepend(),previousElementSibling,querySelector(),"
                + "querySelectorAll(),releaseCapture(),releasePointerCapture(),remove(),removeAttribute(),"
                + "removeAttributeNode(),removeAttributeNS(),replaceChildren(),replaceWith(),requestFullscreen(),"
                + "requestPointerLock(),scroll(),scrollBy(),scrollHeight,scrollIntoView(),scrollLeft,"
                + "scrollLeftMax,scrollTo(),scrollTop,scrollTopMax,scrollWidth,setAttribute(),setAttributeNode(),"
                + "setAttributeNodeNS(),setAttributeNS(),setCapture(),setPointerCapture(),shadowRoot,slot,"
                + "tagName,toggleAttribute(),webkitMatchesSelector()",
            IE = "childElementCount,clientHeight,clientLeft,clientTop,clientWidth,constructor,firstElementChild,"
                + "getAttribute(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),"
                + "getClientRects(),getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),"
                + "lastElementChild,msContentZoomFactor,msGetRegionContent(),msGetUntransformedBounds(),"
                + "msMatchesSelector(),msRegionOverflow,msReleasePointerCapture(),msRequestFullscreen(),"
                + "msSetPointerCapture(),msZoomTo(),nextElementSibling,ongotpointercapture,onlostpointercapture,"
                + "onmsgesturechange,onmsgesturedoubletap,onmsgestureend,onmsgesturehold,onmsgesturestart,"
                + "onmsgesturetap,onmsgotpointercapture,onmsinertiastart,onmslostpointercapture,onmspointercancel,"
                + "onmspointerdown,onmspointerenter,onmspointerleave,onmspointermove,onmspointerout,onmspointerover,"
                + "onmspointerup,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,previousElementSibling,querySelector(),querySelectorAll(),"
                + "releasePointerCapture(),removeAttribute(),removeAttributeNode(),removeAttributeNS(),scrollHeight,"
                + "scrollLeft,scrollTop,scrollWidth,setAttribute(),setAttributeNode(),setAttributeNodeNS(),"
                + "setAttributeNS(),setPointerCapture(),"
                + "tagName")
    @HtmlUnitNYI(CHROME = "after(),attributes,before(),childElementCount,children,classList,className,clientHeight,"
                + "clientLeft,clientTop,clientWidth,constructor(),firstElementChild,getAttribute(),getAttributeNode(),"
                + "getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),hasAttributes(),id,"
                + "innerHTML,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),lastElementChild,"
                + "localName,"
                + "matches(),namespaceURI,nextElementSibling,onbeforecopy,onbeforecut,onbeforepaste,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,prefix,"
                + "previousElementSibling,querySelector(),querySelectorAll(),remove(),removeAttribute(),"
                + "removeAttributeNode(),removeAttributeNS(),replaceWith(),scrollHeight,scrollIntoView(),"
                + "scrollIntoViewIfNeeded(),scrollLeft,scrollTop,scrollWidth,setAttribute(),setAttributeNode(),"
                + "setAttributeNS(),tagName,webkitMatchesSelector()",
            EDGE = "after(),attributes,before(),childElementCount,children,classList,className,clientHeight,"
                + "clientLeft,clientTop,clientWidth,constructor(),firstElementChild,getAttribute(),getAttributeNode(),"
                + "getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),hasAttributes(),id,"
                + "innerHTML,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),lastElementChild,"
                + "localName,"
                + "matches(),namespaceURI,nextElementSibling,onbeforecopy,onbeforecut,onbeforepaste,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,prefix,"
                + "previousElementSibling,querySelector(),querySelectorAll(),remove(),removeAttribute(),"
                + "removeAttributeNode(),removeAttributeNS(),replaceWith(),scrollHeight,scrollIntoView(),"
                + "scrollIntoViewIfNeeded(),scrollLeft,scrollTop,scrollWidth,setAttribute(),setAttributeNode(),"
                + "setAttributeNS(),tagName,webkitMatchesSelector()",
            FF = "after(),attributes,before(),childElementCount,children,classList,className,clientHeight,clientLeft,"
                + "clientTop,clientWidth,constructor(),firstElementChild,getAttribute(),getAttributeNode(),"
                + "getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),"
                + "hasAttributeNS(),hasAttributes(),id,innerHTML,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),lastElementChild,localName,matches(),mozMatchesSelector(),namespaceURI,"
                + "nextElementSibling,outerHTML,prefix,previousElementSibling,querySelector(),querySelectorAll(),"
                + "releaseCapture(),remove(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),replaceWith(),"
                + "scrollHeight,scrollIntoView(),scrollLeft,scrollTop,scrollWidth,setAttribute(),setAttributeNode(),"
                + "setAttributeNS(),setCapture(),tagName,webkitMatchesSelector()",
            FF78 = "after(),attributes,before(),childElementCount,children,classList,className,clientHeight,clientLeft,"
                + "clientTop,clientWidth,constructor(),firstElementChild,getAttribute(),getAttributeNode(),"
                + "getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),"
                + "hasAttributeNS(),hasAttributes(),id,innerHTML,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),lastElementChild,localName,matches(),mozMatchesSelector(),namespaceURI,"
                + "nextElementSibling,outerHTML,prefix,previousElementSibling,querySelector(),querySelectorAll(),"
                + "releaseCapture(),remove(),removeAttribute(),removeAttributeNode(),removeAttributeNS(),replaceWith(),"
                + "scrollHeight,scrollIntoView(),scrollLeft,scrollTop,scrollWidth,setAttribute(),setAttributeNode(),"
                + "setAttributeNS(),setCapture(),tagName,webkitMatchesSelector()",
            IE = "attributes,childElementCount,clientHeight,clientLeft,clientTop,clientWidth,constructor,"
                + "firstElementChild,getAttribute(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),"
                + "getBoundingClientRect(),getClientRects(),getElementsByTagName(),getElementsByTagNameNS(),"
                + "hasAttribute(),hasAttributeNS(),lastElementChild,msMatchesSelector(),nextElementSibling,"
                + "ongotpointercapture,onlostpointercapture,onmsgesturechange,onmsgesturedoubletap,onmsgestureend,"
                + "onmsgesturehold,onmsgesturestart,onmsgesturetap,onmsgotpointercapture,onmsinertiastart,"
                + "onmslostpointercapture,onmspointercancel,onmspointerdown,onmspointerenter,onmspointerleave,"
                + "onmspointermove,onmspointerout,onmspointerover,onmspointerup,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "previousElementSibling,querySelector(),querySelectorAll(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),scrollHeight,scrollLeft,scrollTop,scrollWidth,setAttribute(),"
                + "setAttributeNode(),setAttributeNS(),tagName")
    public void element() throws Exception {
        testString("", "element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "exception",
            EDGE = "exception",
            FF = "exception",
            FF78 = "exception",
            IE = "blockDirection,clipBottom,clipLeft,clipRight,clipTop,constructor,hasLayout")
    @HtmlUnitNYI(IE = "accelerator,backgroundAttachment,backgroundColor,backgroundImage,backgroundPosition,"
            + "backgroundRepeat,borderBottomColor,borderBottomStyle,borderBottomWidth,borderLeftColor,"
            + "borderLeftStyle,borderLeftWidth,borderRightColor,borderRightStyle,borderRightWidth,"
            + "borderTopColor,borderTopStyle,borderTopWidth,bottom,color,constructor,cssFloat,cssText,"
            + "display,font,fontFamily,fontSize,getAttribute(),getPropertyPriority(),getPropertyValue(),"
            + "height,left,length,letterSpacing,lineHeight,margin,marginBottom,marginLeft,marginRight,marginTop,"
            + "maxHeight,maxWidth,minHeight,minWidth,opacity,orphans,outline,outlineWidth,padding,"
            + "paddingBottom,paddingLeft,paddingRight,paddingTop,pixelBottom,pixelHeight,pixelLeft,"
            + "pixelRight,pixelTop,pixelWidth,posBottom,posHeight,position,posLeft,posRight,posTop,"
            + "posWidth,removeAttribute(),removeProperty(),right,rubyAlign,setAttribute(),setProperty(),"
            + "textDecorationBlink,textDecorationLineThrough,textDecorationNone,textDecorationOverline,"
            + "textDecorationUnderline,textIndent,top,verticalAlign,widows,width,wordSpacing,zIndex")
    public void currentStyle() throws Exception {
        testString("", "document.body.currentStyle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,composedPath(),"
                + "constructor(),currentTarget,defaultPrevented,eventPhase,initEvent(),NONE,path,preventDefault(),"
                + "returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                + "type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,composedPath(),"
                + "constructor(),currentTarget,defaultPrevented,eventPhase,initEvent(),NONE,path,preventDefault(),"
                + "returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                + "type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "composedPath(),constructor(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),META_MASK,NONE,originalTarget,preventDefault(),returnValue,"
                + "SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                + "type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "composedPath(),constructor(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),META_MASK,NONE,originalTarget,preventDefault(),returnValue,"
                + "SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                + "type",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,constructor,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),isTrusted,preventDefault(),srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                + "type")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "constructor(),"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,constructor(),"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,constructor(),"
                + "CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),target,"
                + "timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,constructor(),"
                + "CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),target,"
                + "timeStamp,type",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,constructor,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),preventDefault(),srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type")
    public void event() throws Exception {
        testString("", "event ? event : window.event");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),PERSISTENT,TEMPORARY",
            EDGE = "constructor(),PERSISTENT,TEMPORARY",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = {"addEventListener(),alert(),animationStartTime,applicationCache,atob(),blur(),btoa(),"
                + "cancelAnimationFrame(),captureEvents(),clearImmediate(),clearInterval(),clearTimeout(),"
                + "clientInformation,clipboardData,close(),closed,confirm(),console,constructor,defaultStatus,"
                + "devicePixelRatio,dispatchEvent(),document,doNotTrack,event,external,focus(),frameElement,"
                + "frames,getComputedStyle(),getSelection(),history,indexedDB,innerHeight,innerWidth,item(),"
                + "length,localStorage,location,matchMedia(),maxConnectionsPerServer,moveBy(),moveTo(),"
                + "msAnimationStartTime,msCancelRequestAnimationFrame(),msClearImmediate(),msCrypto,"
                + "msIndexedDB,msIsStaticHTML(),msMatchMedia(),msRequestAnimationFrame(),msSetImmediate(),"
                + "msWriteProfilerMark(),name,navigate(),navigator,offscreenBuffering,onabort,onafterprint,"
                + "onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncompassneedscalibration,oncontextmenu,ondblclick,ondevicemotion,ondeviceorientation,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhashchange,onhelp,oninput,"
                + "onkeydown,onkeypress,onkeyup,onload(),onloadeddata,onloadedmetadata,onloadstart,"
                + "onmessage,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onmsgesturechange,onmsgesturedoubletap,onmsgestureend,"
                + "onmsgesturehold,onmsgesturestart,onmsgesturetap,onmsinertiastart,onmspointercancel,"
                + "onmspointerdown,onmspointerenter,onmspointerleave,onmspointermove,onmspointerout,"
                + "onmspointerover,onmspointerup,onoffline,ononline,onpagehide,onpageshow,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onpopstate,onprogress,onratechange,"
                + "onreadystatechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onstalled,"
                + "onstorage,onsubmit,onsuspend,ontimeupdate,onunload",
                "onvolumechange,onwaiting,open(),opener,outerHeight,outerWidth,pageXOffset,pageYOffset,"
                    + "parent,performance,postMessage(),print(),prompt(),releaseEvents(),removeEventListener(),"
                    + "requestAnimationFrame(),resizeBy(),resizeTo(),screen,screenLeft,screenTop,screenX,screenY,"
                    + "scroll(),scrollBy(),scrollTo(),self,sessionStorage,setImmediate(),setInterval(),"
                    + "setTimeout(),showHelp(),showModalDialog(),showModelessDialog(),status,styleMedia,top,"
                    + "toStaticHTML(),toString(),window"})
    @HtmlUnitNYI(CHROME = {"alert(),atob(),blur(),btoa(),cancelAnimationFrame(),captureEvents(),"
                + "clearInterval(),clearTimeout(),clientInformation,close(),closed,confirm(),console,constructor(),"
                + "crypto,devicePixelRatio,document,event,external,find(),focus(),frameElement,frames,"
                + "getComputedStyle(),"
                + "getSelection(),history,innerHeight,innerWidth,length,localStorage,location,matchMedia(),"
                + "moveBy(),moveTo(),"
                + "name,navigator,offscreenBuffering,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onbeforeunload,onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncuechange,ondblclick,ondevicemotion,ondeviceorientation,ondeviceorientationabsolute,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,"
                + "onended,onerror,onfocus,ongotpointercapture,onhashchange,oninput,oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onlanguagechange,onload(),onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmessage,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onoffline,ononline,onpagehide,onpageshow,onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onpopstate,onprogress,onratechange,onrejectionhandled,onreset,onresize,onscroll,"
                + "onsearch,onseeked,onseeking,onselect,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitionend,onunhandledrejection,onunload,onvolumechange,onwaiting,"
                + "onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,"
                + "onwheel,open(),opener,outerHeight,outerWidth,pageXOffset,pageYOffset,parent,performance,"
                + "PERSISTENT,postMessage(),print(),prompt(),releaseEvents(),requestAnimationFrame(),resizeBy(),"
                + "resizeTo(),screen,scroll()",
                "scrollBy(),scrollTo(),scrollX,scrollY,self,sessionStorage,setInterval(),"
                        + "setTimeout(),speechSynthesis,status,stop(),styleMedia,TEMPORARY,top,window"},
            EDGE = {"alert(),atob(),blur(),btoa(),cancelAnimationFrame(),captureEvents(),"
                + "clearInterval(),clearTimeout(),clientInformation,close(),closed,confirm(),console,constructor(),"
                + "crypto,devicePixelRatio,document,event,external,find(),focus(),frameElement,frames,"
                + "getComputedStyle(),"
                + "getSelection(),history,innerHeight,innerWidth,length,localStorage,location,matchMedia(),"
                + "moveBy(),moveTo(),"
                + "name,navigator,offscreenBuffering,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onbeforeunload,onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncuechange,ondblclick,ondevicemotion,ondeviceorientation,ondeviceorientationabsolute,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,"
                + "onended,onerror,onfocus,ongotpointercapture,onhashchange,oninput,oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onlanguagechange,onload(),onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmessage,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onoffline,ononline,onpagehide,onpageshow,onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onpopstate,onprogress,onratechange,onrejectionhandled,onreset,onresize,onscroll,"
                + "onsearch,onseeked,onseeking,onselect,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitionend,onunhandledrejection,onunload,onvolumechange,onwaiting,"
                + "onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,"
                + "onwheel,open(),opener,outerHeight,outerWidth,pageXOffset,pageYOffset,parent,performance,"
                + "PERSISTENT,postMessage(),print(),prompt(),releaseEvents(),requestAnimationFrame(),resizeBy(),"
                + "resizeTo(),screen,scroll()",
                "scrollBy(),scrollTo(),scrollX,scrollY,self,sessionStorage,setInterval(),"
                        + "setTimeout(),speechSynthesis,status,stop(),styleMedia,TEMPORARY,top,window"},
            FF = "alert(),applicationCache,atob(),blur(),btoa(),cancelAnimationFrame(),captureEvents(),"
                + "clearInterval(),clearTimeout(),close(),closed,confirm(),console,constructor(),controllers,"
                + "crypto,devicePixelRatio,document,dump(),event,external,find(),focus(),frameElement,frames,"
                + "getComputedStyle(),getSelection(),history,innerHeight,innerWidth,length,localStorage,location,"
                + "matchMedia(),moveBy(),moveTo(),mozInnerScreenX,mozInnerScreenY,name,navigator,"
                + "netscape,onabort,onafterprint,onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,"
                + "onchange,onclick,oncontextmenu,ondblclick,ondevicelight,ondevicemotion,ondeviceorientation,"
                + "ondeviceproximity,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,onhashchange,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onlanguagechange,onload(),onloadeddata,onloadedmetadata,onloadstart,"
                + "onmessage,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmozfullscreenchange,onmozfullscreenerror,onoffline,ononline,onpagehide,onpageshow,onpause,"
                + "onplay,onplaying,onpopstate,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,onunload,onuserproximity,"
                + "onvolumechange,onwaiting,onwheel,open(),opener,outerHeight,outerWidth,pageXOffset,pageYOffset,"
                + "parent,performance,postMessage(),print(),prompt(),releaseEvents(),requestAnimationFrame(),"
                + "resizeBy(),resizeTo(),screen,scroll(),scrollBy(),scrollByLines(),scrollByPages(),scrollTo(),"
                + "scrollX,scrollY,self,sessionStorage,setInterval(),setTimeout(),status,stop(),top,window",
            FF78 = "alert(),applicationCache,atob(),blur(),btoa(),cancelAnimationFrame(),captureEvents(),"
                + "clearInterval(),clearTimeout(),close(),closed,confirm(),console,constructor(),controllers,"
                + "crypto,devicePixelRatio,document,dump(),event,external,find(),focus(),frameElement,frames,"
                + "getComputedStyle(),getSelection(),history,innerHeight,innerWidth,length,localStorage,location,"
                + "matchMedia(),moveBy(),moveTo(),mozInnerScreenX,mozInnerScreenY,name,navigator,"
                + "netscape,onabort,onafterprint,onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,"
                + "onchange,onclick,oncontextmenu,ondblclick,ondevicelight,ondevicemotion,ondeviceorientation,"
                + "ondeviceproximity,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,onhashchange,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onlanguagechange,onload(),onloadeddata,onloadedmetadata,onloadstart,"
                + "onmessage,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmozfullscreenchange,onmozfullscreenerror,onoffline,ononline,onpagehide,onpageshow,onpause,"
                + "onplay,onplaying,onpopstate,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,onunload,onuserproximity,"
                + "onvolumechange,onwaiting,onwheel,open(),opener,outerHeight,outerWidth,pageXOffset,pageYOffset,"
                + "parent,performance,postMessage(),print(),prompt(),releaseEvents(),requestAnimationFrame(),"
                + "resizeBy(),resizeTo(),screen,scroll(),scrollBy(),scrollByLines(),scrollByPages(),scrollTo(),"
                + "scrollX,scrollY,self,sessionStorage,setInterval(),setTimeout(),status,stop(),top,window",
            IE = {"ActiveXObject,alert(),ANGLE_instanced_arrays,AnimationEvent,ApplicationCache,applicationCache,"
                + "Array(),ArrayBuffer(),atob(),Attr,Audio(),BeforeUnloadEvent,Blob(),blur(),Boolean(),"
                + "btoa(),Call(),CallSite(),cancelAnimationFrame(),CanvasGradient,CanvasPattern,"
                + "CanvasRenderingContext2D,captureEvents(),CDATASection,CharacterData,clearInterval(),"
                + "clearTimeout(),clientInformation,ClientRect,ClientRectList,clipboardData,close(),"
                + "closed,CloseEvent,CollectGarbage(),Comment,CompositionEvent,confirm(),Console,console,"
                + "constructor,Coordinates,Crypto,CSSFontFaceRule,CSSImportRule,CSSKeyframeRule,"
                + "CSSKeyframesRule,CSSMediaRule,CSSNamespaceRule,CSSPageRule,CSSRule,CSSRuleList,"
                + "CSSStyleDeclaration,CSSStyleRule,CSSStyleSheet,CustomEvent,DataTransfer,DataView(),"
                + "Date(),decodeURI(),decodeURIComponent(),DeviceMotionEvent,DeviceOrientationEvent,"
                + "devicePixelRatio,Document,document,DocumentFragment,"
                + "DocumentType,DOMError,DOMException,DOMImplementation,DOMParser(),DOMSettableTokenList,"
                + "DOMStringList,DOMStringMap,DOMTokenList,doNotTrack,DragEvent,Element,encodeURI(),"
                + "encodeURIComponent(),Enumerator(),Error(),ErrorEvent,escape(),eval(),EvalError(),Event,"
                + "event,EXT_texture_filter_anisotropic,external,File,FileList,FileReader(),Float32Array(),"
                + "Float64Array(),focus(),FocusEvent,FormData(),frameElement,frames,Function(),Geolocation,"
                + "getComputedStyle(),getSelection(),History,history,HTMLAllCollection,HTMLAnchorElement,"
                + "HTMLAppletElement,HTMLAreaElement,HTMLAudioElement,HTMLBaseElement,HTMLBaseFontElement,"
                + "HTMLBGSoundElement,HTMLBlockElement,HTMLBodyElement,HTMLBRElement,HTMLButtonElement,"
                + "HTMLCanvasElement,HTMLCollection,HTMLDataListElement,HTMLDDElement,HTMLDirectoryElement,"
                + "HTMLDivElement,HTMLDListElement,HTMLDocument,HTMLDTElement,HTMLElement,HTMLEmbedElement,"
                + "HTMLFieldSetElement,HTMLFontElement,HTMLFormElement,HTMLFrameElement,HTMLFrameSetElement,"
                + "HTMLHeadElement,HTMLHeadingElement,HTMLHRElement,HTMLHtmlElement,HTMLIFrameElement,"
                + "HTMLImageElement,HTMLInputElement,HTMLIsIndexElement,HTMLLabelElement,HTMLLegendElement,"
                + "HTMLLIElement,HTMLLinkElement,HTMLMapElement,HTMLMarqueeElement,HTMLMediaElement",
                "HTMLMenuElement,HTMLMetaElement,HTMLModElement,HTMLNextIdElement,HTMLObjectElement,"
                    + "HTMLOListElement,HTMLOptGroupElement,HTMLOptionElement,HTMLParagraphElement,HTMLParamElement,"
                    + "HTMLPhraseElement,HTMLPreElement,HTMLProgressElement,HTMLQuoteElement,HTMLScriptElement,"
                    + "HTMLSelectElement,HTMLSourceElement,HTMLSpanElement,HTMLStyleElement,HTMLTableCaptionElement,"
                    + "HTMLTableCellElement,HTMLTableColElement,HTMLTableDataCellElement,HTMLTableElement,"
                    + "HTMLTableHeaderCellElement,HTMLTableRowElement,HTMLTableSectionElement,HTMLTextAreaElement,"
                    + "HTMLTitleElement,HTMLTrackElement,HTMLUListElement,HTMLUnknownElement,HTMLVideoElement,"
                    + "IDBCursor,IDBCursorWithValue,IDBDatabase,IDBFactory,IDBIndex,IDBKeyRange,IDBObjectStore,"
                    + "IDBOpenDBRequest,IDBRequest,IDBTransaction,IDBVersionChangeEvent,Image(),ImageData,Infinity,"
                    + "innerHeight,innerWidth,Int16Array(),Int32Array(),Int8Array(),InternalError(),Intl,isFinite(),"
                    + "isNaN(),JavaException(),JSON,KeyboardEvent,length,localStorage,Location,location,Map(),"
                    + "matchMedia(),Math,MediaError,MediaList,MediaQueryList,MediaSource(),"
                    + "MessageChannel(),MessageEvent,MessagePort,"
                    + "MimeType,MimeTypeArray,MouseEvent,MouseWheelEvent,moveBy(),moveTo(),MSGestureEvent,"
                    + "MutationEvent,"
                    + "MutationObserver(),MutationRecord,name,NamedNodeMap,NaN,navigate(),Navigator,navigator,Node,"
                    + "NodeFilter,NodeIterator,NodeList,Number(),Object(),OES_element_index_uint,"
                    + "OES_standard_derivatives,"
                    + "OES_texture_float,OES_texture_float_linear,offscreenBuffering,onabort,onafterprint,"
                    + "onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                    + "oncontextmenu,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                    + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,"
                    + "onhashchange,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload(),onloadeddata,"
                    + "onloadedmetadata,onloadstart,onmessage,onmousedown,onmouseenter,onmouseleave,"
                    + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onmsgesturechange,"
                    + "onmsgesturedoubletap,onmsgestureend,onmsgesturehold,onmsgesturestart,onmsgesturetap,"
                    + "onmsinertiastart,onmspointercancel,onmspointerdown,onmspointerenter,onmspointerleave,"
                    + "onmspointermove,onmspointerout,onmspointerover,onmspointerup,onoffline,ononline,onpagehide,"
                    + "onpageshow,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                    + "onpointermove,onpointerout,onpointerover,onpointerup,onpopstate,onprogress,onratechange,"
                    + "onreadystatechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onstalled,onstorage,"
                    + "onsubmit,onsuspend,ontimeupdate,onunload,onvolumechange,onwaiting,open(),opener,Option(),"
                    + "outerHeight,outerWidth,PageTransitionEvent,pageXOffset,pageYOffset,parent,parseFloat(),"
                    + "parseInt(),Performance,performance,PerformanceEntry,PerformanceMark,PerformanceMeasure,"
                    + "PerformanceNavigation,PerformanceNavigationTiming,PerformanceResourceTiming,PerformanceTiming,"
                    + "Plugin,PluginArray,PointerEvent,PopStateEvent,Position,PositionError,postMessage(),print(),"
                    + "process(),ProcessingInstruction,ProgressEvent,prompt(),Range,RangeError(),ReferenceError(),"
                    + "RegExp(),releaseEvents(),requestAnimationFrame(),resizeBy(),resizeTo(),Screen,screen,Script(),"
                    + "ScriptEngine(),ScriptEngineBuildVersion(),ScriptEngineMajorVersion(),ScriptEngineMinorVersion(),"
                    + "scroll(),scrollBy(),scrollTo(),Selection,self,sessionStorage,Set(),setInterval(),setTimeout(),"
                    + "showModalDialog(),showModelessDialog(),sortFunction(),SourceBuffer(),SourceBufferList(),"
                    + "status,Storage,StorageEvent,String(),"
                    + "StyleMedia,styleMedia,StyleSheet,StyleSheetList,SubtleCrypto,SVGAElement,"
                    + "SVGAngle,SVGAnimatedAngle,"
                    + "SVGAnimatedBoolean,SVGAnimatedEnumeration,SVGAnimatedInteger,SVGAnimatedLength,"
                    + "SVGAnimatedLengthList,"
                    + "SVGAnimatedNumber,SVGAnimatedNumberList,SVGAnimatedPreserveAspectRatio,SVGAnimatedRect,"
                    + "SVGAnimatedString,SVGAnimatedTransformList,SVGCircleElement,SVGClipPathElement,"
                    + "SVGComponentTransferFunctionElement,SVGDefsElement,SVGDescElement,SVGElement,SVGEllipseElement,"
                    + "SVGFEBlendElement,SVGFEColorMatrixElement,SVGFEComponentTransferElement,SVGFECompositeElement,"
                    + "SVGFEConvolveMatrixElement,SVGFEDiffuseLightingElement,SVGFEDisplacementMapElement,"
                    + "SVGFEDistantLightElement,SVGFEFloodElement,SVGFEFuncAElement,"
                    + "SVGFEFuncBElement,SVGFEFuncGElement,"
                    + "SVGFEFuncRElement,SVGFEGaussianBlurElement,SVGFEImageElement,SVGFEMergeElement,"
                    + "SVGFEMergeNodeElement,"
                    + "SVGFEMorphologyElement,SVGFEOffsetElement,SVGFEPointLightElement,SVGFESpecularLightingElement,"
                    + "SVGFESpotLightElement,SVGFETileElement,SVGFETurbulenceElement,SVGFilterElement,SVGGElement,"
                    + "SVGGradientElement,SVGImageElement,SVGLength,SVGLengthList,"
                    + "SVGLinearGradientElement,SVGLineElement,"
                    + "SVGMarkerElement,SVGMaskElement,SVGMatrix,SVGMetadataElement,SVGNumber,"
                    + "SVGNumberList,SVGPathElement,"
                    + "SVGPathSeg,SVGPathSegArcAbs,SVGPathSegArcRel,SVGPathSegClosePath,SVGPathSegCurvetoCubicAbs,"
                    + "SVGPathSegCurvetoCubicRel,SVGPathSegCurvetoCubicSmoothAbs,SVGPathSegCurvetoCubicSmoothRel,"
                    + "SVGPathSegCurvetoQuadraticAbs,SVGPathSegCurvetoQuadraticRel,SVGPathSegCurvetoQuadraticSmoothAbs,"
                    + "SVGPathSegCurvetoQuadraticSmoothRel,SVGPathSegLinetoAbs,SVGPathSegLinetoHorizontalAbs,"
                    + "SVGPathSegLinetoHorizontalRel,SVGPathSegLinetoRel,SVGPathSegLinetoVerticalAbs,"
                    + "SVGPathSegLinetoVerticalRel,SVGPathSegList,SVGPathSegMovetoAbs,SVGPathSegMovetoRel,"
                    + "SVGPatternElement,SVGPoint,SVGPointList,SVGPolygonElement,SVGPolylineElement,"
                    + "SVGPreserveAspectRatio,SVGRadialGradientElement,SVGRect,SVGRectElement,SVGScriptElement,"
                    + "SVGStopElement,SVGStringList,SVGStyleElement,SVGSVGElement,SVGSwitchElement,SVGSymbolElement,"
                    + "SVGTextContentElement,SVGTextElement,SVGTextPathElement,SVGTextPositioningElement,"
                    + "SVGTitleElement,SVGTransform,SVGTransformList,SVGTSpanElement,SVGUnitTypes,SVGUseElement,"
                    + "SVGViewElement,SVGZoomEvent,SyntaxError(),test(),Text,TextEvent,TextMetrics,TextRange,TextTrack,"
                    + "TextTrackCue(),TextTrackCueList,TextTrackList,TimeRanges,top,TrackEvent,TransitionEvent,"
                    + "TreeWalker,TypeError(),UIEvent,Uint16Array(),Uint32Array(),Uint8Array(),Uint8ClampedArray(),"
                    + "undefined,unescape(),URIError(),URL,ValidityState,VideoPlaybackQuality,"
                    + "WeakMap(),WEBGL_compressed_texture_s3tc,"
                    + "WEBGL_debug_renderer_info,WebGLActiveInfo,WebGLBuffer,WebGLContextEvent(),WebGLFramebuffer,"
                    + "WebGLProgram,WebGLRenderbuffer,WebGLRenderingContext,WebGLShader,WebGLShaderPrecisionFormat,"
                    + "WebGLTexture,WebGLUniformLocation,WebSocket(),WheelEvent,Window,window,With(),Worker(),"
                    + "XMLDocument,XMLHttpRequest(),XMLHttpRequestEventTarget,XMLSerializer()"})
    public void window() throws Exception {
        testString("", "window");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAbbreviated}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void abbr() throws Exception {
        test("abbr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAcronym}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void acronym() throws Exception {
        test("acronym");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAnchor}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "charset,constructor(),coords,download,hash,host,hostname,href,hreflang,name,origin,password,"
                + "pathname,ping,port,protocol,referrerPolicy,rel,relList,rev,search,shape,target,text,toString(),"
                + "type,username",
            EDGE = "charset,constructor(),coords,download,hash,host,hostname,href,hreflang,name,origin,password,"
                + "pathname,ping,port,protocol,referrerPolicy,rel,relList,rev,search,shape,target,text,toString(),"
                + "type,"
                + "username",
            FF = "charset,constructor(),coords,download,hash,host,hostname,href,hreflang,name,origin,password,"
                + "pathname,ping,port,protocol,referrerPolicy,rel,relList,rev,search,shape,target,text,toString(),"
                + "type,username",
            FF78 = "charset,constructor(),coords,download,hash,host,hostname,href,hreflang,name,origin,password,"
                + "pathname,ping,port,protocol,referrerPolicy,rel,relList,rev,search,shape,target,text,toString(),"
                + "type,username",
            IE = "charset,constructor,coords,hash,host,hostname,href,hreflang,Methods,mimeType,name,nameProp,"
                + "pathname,port,protocol,protocolLong,rel,rev,search,shape,target,text,toString(),type,"
                + "urn")
    @HtmlUnitNYI(CHROME = "charset,constructor(),coords,download,hash,host,hostname,href,hreflang,name,origin,password,"
                + "pathname,ping,port,protocol,referrerPolicy,rel,relList,rev,search,shape,target,text,"
                + "type,username",
            EDGE = "charset,constructor(),coords,download,hash,host,hostname,href,hreflang,name,origin,password,"
                + "pathname,ping,port,protocol,referrerPolicy,rel,relList,rev,search,shape,target,text,"
                + "type,username",
            FF = "charset,constructor(),coords,download,hash,host,hostname,href,hreflang,name,origin,password,"
                + "pathname,ping,port,protocol,referrerPolicy,rel,relList,rev,search,shape,target,text,"
                + "type,username",
            FF78 = "charset,constructor(),coords,download,hash,host,hostname,href,hreflang,name,origin,password,"
                + "pathname,ping,port,protocol,referrerPolicy,rel,relList,rev,search,shape,target,text,"
                + "type,username",
            IE = "exception")
    public void a() throws Exception {
        test("a");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAddress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,clear,constructor,width")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,"
                + "onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,"
                + "onselectstart,onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,style,"
                + "tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,"
                + "onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,"
                + "onselectstart,onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,style,"
                + "tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "clear,constructor,width")
    public void address() throws Exception {
        test("address");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlApplet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "constructor()",
            IE = "align,alt,altHtml,archive,BaseHref,border,classid,code,codeBase,codeType,constructor,"
                + "contentDocument,data,declare,form,height,hspace,name,namedRecordset(),object,recordset,standby,"
                + "type,useMap,vspace,"
                + "width")
    @HtmlUnitNYI(IE = "align,alt,border,classid,constructor,height,width")
    public void applet() throws Exception {
        test("applet");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "alt,constructor(),coords,download,hash,host,hostname,href,noHref,origin,password,pathname,ping,"
                + "port,protocol,referrerPolicy,rel,relList,search,shape,target,toString(),"
                + "username",
            EDGE = "alt,constructor(),coords,download,hash,host,hostname,href,noHref,origin,password,pathname,ping,"
                + "port,protocol,referrerPolicy,rel,relList,search,shape,target,toString(),"
                + "username",
            FF = "alt,constructor(),coords,download,hash,host,hostname,href,noHref,origin,password,pathname,ping,"
                + "port,protocol,referrerPolicy,rel,relList,search,shape,target,toString(),"
                + "username",
            FF78 = "alt,constructor(),coords,download,hash,host,hostname,href,noHref,origin,password,pathname,ping,"
                + "port,protocol,referrerPolicy,rel,relList,search,shape,target,toString(),"
                + "username",
            IE = "alt,constructor,coords,hash,host,hostname,href,noHref,pathname,port,protocol,rel,search,shape,"
                + "target,"
                + "toString()")
    @HtmlUnitNYI(CHROME = "alt,constructor(),coords,rel,relList",
            EDGE = "alt,constructor(),coords,rel,relList",
            FF78 = "alt,constructor(),coords,rel,relList",
            FF = "alt,constructor(),coords,rel,relList",
            IE = "alt,constructor,coords,rel")
    public void area() throws Exception {
        test("area");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlArticle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,"
                + "onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhelp,"
                + "oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onmscontentzoom,"
                + "onmsmanipulationstatechanged,onpaste,onpause,onplay,onplaying,onprogress,onratechange,onreset,"
                + "onscroll,onseeked,onseeking,onselect,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "onvolumechange,onwaiting,outerHTML,parentElement,releaseCapture(),removeNode(),runtimeStyle,"
                + "scrollIntoView(),setActive(),setCapture(),style,tabIndex,title,uniqueID")
    public void article() throws Exception {
        test("article");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAside}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,"
                + "ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,"
                + "ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,"
                + "onclick,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,"
                + "ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,"
                + "onerror,onfocus,onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,"
                + "onpaste,onpause,onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,"
                + "onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,"
                + "outerHTML,parentElement,releaseCapture(),removeNode(),runtimeStyle,scrollIntoView(),setActive(),"
                + "setCapture(),style,tabIndex,title,uniqueID")
    public void aside() throws Exception {
        test("aside");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAudio}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor")
    @HtmlUnitNYI(CHROME = "constructor(),nodeName,nodeType",
            EDGE = "constructor(),nodeName,nodeType",
            FF = "constructor(),nodeName,nodeType",
            FF78 = "constructor(),nodeName,nodeType",
            IE = "constructor,nodeName,nodeType")
    public void audio() throws Exception {
        test("audio");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBackgroundSound}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "balance,constructor,loop,src,volume")
    @HtmlUnitNYI(FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            IE = "constructor")
    public void bgsound() throws Exception {
        test("bgsound");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBase}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),href,target",
            EDGE = "constructor(),href,target",
            FF = "constructor(),href,target",
            FF78 = "constructor(),href,target",
            IE = "constructor,href,target")
    public void base() throws Exception {
        test("base");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBaseFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,"
                + "dir,draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onshow,onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,"
                + "ontransitionend,ontransitionrun,ontransitionstart,onvolumechange,onwaiting,"
                + "onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,"
                + "onwheel,spellcheck,style,tabIndex,title",
            IE = "color,constructor,face,size")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title")
    public void basefont() throws Exception {
        test("basefont");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBidirectionalIsolation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,"
                + "onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,"
                + "onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void bdi() throws Exception {
        test("bdi");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void bdo() throws Exception {
        test("bdo");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBig}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void big() throws Exception {
        test("big");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBlink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(IE = "constructor")
    public void blink() throws Exception {
        test("blink");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBlockQuote}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "cite,constructor()",
            EDGE = "cite,constructor()",
            FF = "cite,constructor()",
            FF78 = "cite,constructor()",
            IE = "cite,clear,constructor,width")
    @HtmlUnitNYI(IE = "clear,constructor,width")
    public void blockquote() throws Exception {
        test("blockquote");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "aLink,background,bgColor,constructor(),link,onafterprint,onbeforeprint,onbeforeunload,onblur,"
                + "onerror,onfocus,onhashchange,onlanguagechange,onload(),onmessage,onmessageerror,onoffline,"
                + "ononline,onpagehide,onpageshow,onpopstate,onrejectionhandled,onresize,onscroll,onstorage,"
                + "onunhandledrejection,onunload,text,"
                + "vLink",
            EDGE = "aLink,background,bgColor,constructor(),link,onafterprint,onbeforeprint,onbeforeunload,onblur,"
                + "onerror,onfocus,onhashchange,onlanguagechange,onload(),onmessage,onmessageerror,onoffline,"
                + "ononline,onpagehide,onpageshow,onpopstate,onrejectionhandled,onresize,onscroll,onstorage,"
                + "onunhandledrejection,onunload,text,"
                + "vLink",
            FF = "aLink,background,bgColor,constructor(),link,onafterprint,onbeforeprint,onbeforeunload,"
                + "onhashchange,onlanguagechange,onmessage,onmessageerror,onoffline,ononline,onpagehide,onpageshow,"
                + "onpopstate,onrejectionhandled,onstorage,onunhandledrejection,onunload,text,"
                + "vLink",
            FF78 = "aLink,background,bgColor,constructor(),link,onafterprint,onbeforeprint,onbeforeunload,"
                + "onhashchange,onlanguagechange,onmessage,onmessageerror,onoffline,ononline,onpagehide,onpageshow,"
                + "onpopstate,onrejectionhandled,onstorage,onunhandledrejection,onunload,text,"
                + "vLink",
            IE = "aLink,background,bgColor,bgProperties,bottomMargin,constructor,createTextRange(),leftMargin,link,"
                + "noWrap,onafterprint,onbeforeprint,onbeforeunload,onblur,onerror,onfocus,onhashchange,onload,"
                + "onmessage,onoffline,ononline,onpagehide,onpageshow,onpopstate,onresize,onstorage,onunload,"
                + "rightMargin,scroll,text,topMargin,"
                + "vLink")
    @HtmlUnitNYI(IE = "aLink,background,bgColor,constructor,createTextRange(),link,onafterprint,onbeforeprint,"
                + "onbeforeunload,onhashchange,onmessage,onoffline,ononline,onpagehide,onpageshow,onpopstate,"
                + "onresize,onstorage,onunload,text,vLink")
    public void body() throws Exception {
        test("body");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBold}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void b() throws Exception {
        test("b");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "clear,constructor()",
            IE = "clear,constructor")
    public void br() throws Exception {
        test("br");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "checkValidity(),constructor(),disabled,form,formAction,formEnctype,formMethod,formNoValidate,"
                + "formTarget,labels,name,reportValidity(),setCustomValidity(),type,validationMessage,validity,"
                + "value,willValidate",
            EDGE = "checkValidity(),constructor(),disabled,form,formAction,formEnctype,formMethod,formNoValidate,"
                + "formTarget,labels,name,reportValidity(),setCustomValidity(),type,validationMessage,validity,"
                + "value,"
                + "willValidate",
            FF = "autofocus,checkValidity(),constructor(),disabled,form,formAction,formEnctype,formMethod,"
                + "formNoValidate,formTarget,labels,name,reportValidity(),setCustomValidity(),type,"
                + "validationMessage,validity,value,willValidate",
            FF78 = "autofocus,checkValidity(),constructor(),disabled,form,formAction,formEnctype,formMethod,"
                + "formNoValidate,formTarget,labels,name,reportValidity(),setCustomValidity(),type,"
                + "validationMessage,validity,value,willValidate",
            IE = "autofocus,checkValidity(),constructor,createTextRange(),form,formAction,formEnctype,formMethod,"
                + "formNoValidate,formTarget,name,setCustomValidity(),status,type,validationMessage,validity,value,"
                + "willValidate")
    @HtmlUnitNYI(CHROME = "checkValidity(),constructor(),disabled,form,labels,name,type,value",
            EDGE = "checkValidity(),constructor(),disabled,form,labels,name,type,value",
            FF78 = "checkValidity(),constructor(),disabled,form,labels,name,type,value",
            FF = "checkValidity(),constructor(),disabled,form,labels,name,type,value",
            IE = "checkValidity(),constructor,createTextRange(),form,name,type,value")
    public void button() throws Exception {
        test("button");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCanvas}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "captureStream(),constructor(),getContext(),height,toBlob(),"
                    + "toDataURL(),transferControlToOffscreen(),width",
            EDGE = "captureStream(),constructor(),getContext(),height,toBlob(),toDataURL(),"
                + "transferControlToOffscreen(),"
                + "width",
            FF = "captureStream(),constructor(),getContext(),height,"
                    + "mozOpaque,mozPrintCallback,toBlob(),toDataURL(),width",
            FF78 = "captureStream(),constructor(),getContext(),height,"
                    + "mozOpaque,mozPrintCallback,toBlob(),toDataURL(),width",
            IE = "constructor,getContext(),height,msToBlob(),toDataURL(),width")
    @HtmlUnitNYI(CHROME = "constructor(),getContext(),height,toDataURL(),width",
            EDGE = "constructor(),getContext(),height,toDataURL(),width",
            FF78 = "constructor(),getContext(),height,toDataURL(),width",
            FF = "constructor(),getContext(),height,toDataURL(),width",
            IE = "constructor,getContext(),height,toDataURL(),width")
    public void canvas() throws Exception {
        test("canvas");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,constructor()",
            EDGE = "align,constructor()",
            FF = "align,constructor()",
            FF78 = "align,constructor()",
            IE = "align,constructor,vAlign")
    public void caption() throws Exception {
        test("caption");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCenter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,clear,constructor,width")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "clear,constructor,width")
    public void center() throws Exception {
        test("center");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCitation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,"
                + "tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,"
                + "tabIndex,title",
            IE = "constructor")
    public void cite() throws Exception {
        test("cite");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void code() throws Exception {
        test("code");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCommand}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void command() throws Exception {
        test("command");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDataList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),options",
            EDGE = "constructor(),options",
            FF = "constructor(),options",
            FF78 = "constructor(),options",
            IE = "constructor,options")
    public void datalist() throws Exception {
        test("datalist");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinition}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,"
                + "onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,"
                + "onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,"
                + "tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,"
                + "tabIndex,title",
            IE = "constructor")
    public void dfn() throws Exception {
        test("dfn");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "constructor,noWrap")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title")
    public void dd() throws Exception {
        test("dd");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDeletedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "cite,constructor(),dateTime",
            EDGE = "cite,constructor(),dateTime",
            FF = "cite,constructor(),dateTime",
            FF78 = "cite,constructor(),dateTime",
            IE = "cite,constructor,dateTime")
    public void del() throws Exception {
        test("del");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDetails}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),open",
            EDGE = "constructor(),open",
            FF = "constructor(),open",
            FF78 = "constructor(),open",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void details() throws Exception {
        test("details");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDialog}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "close(),constructor(),open,returnValue,show(),showModal()",
            EDGE = "close(),constructor(),open,returnValue,show(),showModal()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            IE = "constructor")
    public void dialog() throws Exception {
        test("dialog");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDirectory}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "compact,constructor()",
            EDGE = "compact,constructor()",
            FF = "compact,constructor()",
            FF78 = "compact,constructor()",
            IE = "compact,constructor,type")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor()",
            FF = "constructor()",
            IE = "constructor")
    public void dir() throws Exception {
        test("dir");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDivision}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,constructor()",
            EDGE = "align,constructor()",
            FF = "align,constructor()",
            FF78 = "align,constructor()",
            IE = "align,constructor,noWrap")
    public void div() throws Exception {
        test("div");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "compact,constructor()",
            IE = "compact,constructor")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor()",
            FF = "constructor()",
            IE = "constructor")
    public void dl() throws Exception {
        test("dl");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "constructor,noWrap")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title")
    public void dt() throws Exception {
        test("dt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,constructor(),getSVGDocument(),height,name,src,type,width",
            EDGE = "align,constructor(),getSVGDocument(),height,name,src,type,width",
            FF = "align,constructor(),getSVGDocument(),height,name,src,type,width",
            FF78 = "align,constructor(),getSVGDocument(),height,name,src,type,width",
            IE = "constructor,getSVGDocument(),height,hidden,msPlayToDisabled,msPlayToPreferredSourceUri,"
                + "msPlayToPrimary,name,palette,pluginspage,readyState,src,units,"
                + "width")
    @HtmlUnitNYI(CHROME = "align,constructor(),height,name,width",
            EDGE = "align,constructor(),height,name,width",
            FF78 = "align,constructor(),height,name,width",
            FF = "align,constructor(),height,name,width",
            IE = "constructor,height,name,width")
    public void embed() throws Exception {
        test("embed");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlEmphasis}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void em() throws Exception {
        test("em");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFieldSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "checkValidity(),constructor(),disabled,elements,form,name,reportValidity(),setCustomValidity(),"
                + "type,validationMessage,validity,"
                + "willValidate",
            EDGE = "checkValidity(),constructor(),disabled,elements,form,name,reportValidity(),setCustomValidity(),"
                + "type,validationMessage,validity,"
                + "willValidate",
            FF = "checkValidity(),constructor(),disabled,elements,form,name,reportValidity(),setCustomValidity(),"
                + "type,validationMessage,validity,"
                + "willValidate",
            FF78 = "checkValidity(),constructor(),disabled,elements,form,name,reportValidity(),setCustomValidity(),"
                + "type,validationMessage,validity,"
                + "willValidate",
            IE = "align,checkValidity(),constructor,form,setCustomValidity(),validationMessage,validity,"
                + "willValidate")
    @HtmlUnitNYI(CHROME = "checkValidity(),constructor(),disabled,form,name",
            EDGE = "checkValidity(),constructor(),disabled,form,name",
            FF78 = "checkValidity(),constructor(),disabled,form,name",
            FF = "checkValidity(),constructor(),disabled,form,name",
            IE = "align,checkValidity(),constructor,disabled,form")
    public void fieldset() throws Exception {
        test("fieldset");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFigureCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,"
                + "onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,"
                + "onpause,onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "parentElement,releaseCapture(),removeNode(),runtimeStyle,scrollIntoView(),setActive(),"
                + "setCapture(),style,tabIndex,title,uniqueID")
    public void figcaption() throws Exception {
        test("figcaption");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFigure}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,"
                + "onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,"
                + "onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),"
                + "isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,offsetParent,offsetTop,"
                + "offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,onbeforedeactivate,"
                + "onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncuechange,"
                + "oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhelp,oninput,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onmscontentzoom,"
                + "onmsmanipulationstatechanged,onpaste,onpause,onplay,onplaying,onprogress,onratechange,onreset,"
                + "onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "onvolumechange,onwaiting,outerHTML,parentElement,releaseCapture(),removeNode(),runtimeStyle,"
                + "scrollIntoView(),setActive(),setCapture(),style,tabIndex,title,uniqueID")
    public void figure() throws Exception {
        test("figure");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "color,constructor(),face,size",
            EDGE = "color,constructor(),face,size",
            FF = "color,constructor(),face,size",
            FF78 = "color,constructor(),face,size",
            IE = "color,constructor,face,size")
    public void font() throws Exception {
        test("font");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlForm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "acceptCharset,action,autocomplete,checkValidity(),constructor(),elements,encoding,enctype,length,"
                + "method,name,noValidate,reportValidity(),requestSubmit(),reset(),submit(),"
                + "target",
            EDGE = "acceptCharset,action,autocomplete,checkValidity(),constructor(),elements,encoding,enctype,length,"
                + "method,name,noValidate,reportValidity(),requestSubmit(),reset(),submit(),"
                + "target",
            FF = "acceptCharset,action,autocomplete,checkValidity(),constructor(),elements,encoding,enctype,length,"
                + "method,name,noValidate,reportValidity(),requestSubmit(),reset(),submit(),"
                + "target",
            FF78 = "acceptCharset,action,autocomplete,checkValidity(),constructor(),elements,encoding,enctype,length,"
                + "method,name,noValidate,reportValidity(),requestSubmit(),reset(),submit(),"
                + "target",
            IE = "acceptCharset,action,autocomplete,checkValidity(),constructor,elements,encoding,enctype,item(),"
                + "length,method,name,namedItem(),noValidate,reset(),submit(),tags(),target,"
                + "urns()")
    @HtmlUnitNYI(CHROME = "action,checkValidity(),constructor(),elements,encoding,enctype,length,method,"
                + "name,requestSubmit(),reset(),submit(),target",
            EDGE = "action,checkValidity(),constructor(),elements,encoding,enctype,length,method,"
                + "name,requestSubmit(),reset(),submit(),target",
            FF78 = "action,checkValidity(),constructor(),elements,encoding,enctype,length,method,name,"
                + "requestSubmit(),reset(),submit(),target",
            FF = "action,checkValidity(),constructor(),elements,encoding,enctype,length,method,name,"
                + "requestSubmit(),reset(),submit(),target",
            IE = "action,checkValidity(),constructor,elements,encoding,enctype,item(),length,method,name,"
                + "reset(),submit(),target")
    public void form() throws Exception {
        test("form");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,"
                + "isDisabled,isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),"
                + "offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,"
                + "onbeforeactivate,onbeforecopy,onbeforecut,onbeforedeactivate,onbeforepaste,onblur,"
                + "oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhelp,"
                + "oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,onplay,"
                + "onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,"
                + "outerHTML,outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),"
                + "removeNode(),replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),"
                + "setActive(),setCapture(),sourceIndex,spellcheck,style,swapNode(),tabIndex,title,"
                + "uniqueID,uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,"
                + "onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhelp,"
                + "oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onmscontentzoom,"
                + "onmsmanipulationstatechanged,onpaste,onpause,onplay,onplaying,onprogress,onratechange,onreset,"
                + "onscroll,onseeked,onseeking,onselect,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,"
                + "onvolumechange,onwaiting,outerHTML,parentElement,releaseCapture(),removeNode(),runtimeStyle,"
                + "scrollIntoView(),setActive(),setCapture(),style,tabIndex,title,uniqueID")
    public void footer() throws Exception {
        test("footer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),contentDocument,contentWindow,frameBorder,longDesc,marginHeight,marginWidth,name,"
                + "noResize,scrolling,"
                + "src",
            EDGE = "constructor(),contentDocument,contentWindow,frameBorder,longDesc,marginHeight,marginWidth,name,"
                + "noResize,scrolling,"
                + "src",
            FF = "constructor(),contentDocument,contentWindow,frameBorder,longDesc,marginHeight,marginWidth,name,"
                + "noResize,scrolling,"
                + "src",
            FF78 = "constructor(),contentDocument,contentWindow,frameBorder,longDesc,marginHeight,marginWidth,name,"
                + "noResize,scrolling,"
                + "src",
            IE = "border,borderColor,constructor,contentDocument,contentWindow,frameBorder,frameSpacing,"
                + "getSVGDocument(),height,longDesc,marginHeight,marginWidth,name,noResize,onload,scrolling,"
                + "security,src,"
                + "width")
    @HtmlUnitNYI(CHROME = "constructor(),contentDocument,contentWindow,name,src",
            EDGE = "constructor(),contentDocument,contentWindow,name,src",
            FF78 = "constructor(),contentDocument,contentWindow,name,src",
            FF = "constructor(),contentDocument,contentWindow,name,src",
            IE = "border,constructor,contentDocument,contentWindow,name,src")
    public void frame() throws Exception {
        test("frame");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFrameSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "cols,constructor(),onafterprint,onbeforeprint,onbeforeunload,onblur,onerror,onfocus,onhashchange,"
                + "onlanguagechange,onload(),onmessage,onmessageerror,onoffline,ononline,onpagehide,onpageshow,"
                + "onpopstate,onrejectionhandled,onresize,onscroll,onstorage,onunhandledrejection,onunload,"
                + "rows",
            EDGE = "cols,constructor(),onafterprint,onbeforeprint,onbeforeunload,onblur,onerror,onfocus,onhashchange,"
                + "onlanguagechange,onload(),onmessage,onmessageerror,onoffline,ononline,onpagehide,onpageshow,"
                + "onpopstate,onrejectionhandled,onresize,onscroll,onstorage,onunhandledrejection,onunload,"
                + "rows",
            FF = "cols,constructor(),onafterprint,onbeforeprint,onbeforeunload,onhashchange,onlanguagechange,"
                + "onmessage,onmessageerror,onoffline,ononline,onpagehide,onpageshow,onpopstate,onrejectionhandled,"
                + "onstorage,onunhandledrejection,onunload,"
                + "rows",
            FF78 = "cols,constructor(),onafterprint,onbeforeprint,onbeforeunload,onhashchange,onlanguagechange,"
                + "onmessage,onmessageerror,onoffline,ononline,onpagehide,onpageshow,onpopstate,onrejectionhandled,"
                + "onstorage,onunhandledrejection,onunload,"
                + "rows",
            IE = "border,borderColor,cols,constructor,frameBorder,frameSpacing,name,onafterprint,onbeforeprint,"
                + "onbeforeunload,onblur,onerror,onfocus,onhashchange,onload,onmessage,onoffline,ononline,"
                + "onpagehide,onpageshow,onresize,onstorage,onunload,"
                + "rows")
    @HtmlUnitNYI(CHROME = "cols,constructor(),onafterprint,onbeforeprint,onbeforeunload,onhashchange,"
                + "onlanguagechange,onmessage,onmessageerror,onoffline,ononline,onpagehide,onpageshow,onpopstate,"
                + "onrejectionhandled,onstorage,onunhandledrejection,onunload,rows",
            EDGE = "cols,constructor(),onafterprint,onbeforeprint,onbeforeunload,onhashchange,"
                + "onlanguagechange,onmessage,onmessageerror,onoffline,ononline,onpagehide,onpageshow,onpopstate,"
                + "onrejectionhandled,onstorage,onunhandledrejection,onunload,rows",
            IE = "border,cols,constructor,onafterprint,onbeforeprint,onbeforeunload,onhashchange,onmessage,"
                + "onoffline,ononline,onpagehide,onpageshow,onresize,onstorage,onunload,rows")
    public void frameset() throws Exception {
        test("frameset");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHead}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,profile")
    @HtmlUnitNYI(IE = "constructor")
    public void head() throws Exception {
        test("head");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,"
                + "onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,"
                + "onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,"
                + "onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,onplay,onplaying,"
                + "onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,parentElement,releaseCapture(),"
                + "removeNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),style,tabIndex,title,uniqueID")
    public void header() throws Exception {
        test("header");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading1}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,constructor()",
            IE = "align,clear,constructor")
    public void h1() throws Exception {
        test("h1");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading2}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,constructor()",
            IE = "align,clear,constructor")
    public void h2() throws Exception {
        test("h2");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading3}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,constructor()",
            IE = "align,clear,constructor")
    public void h3() throws Exception {
        test("h3");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading4}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,constructor()",
            IE = "align,clear,constructor")
    public void h4() throws Exception {
        test("h4");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading5}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,constructor()",
            IE = "align,clear,constructor")
    public void h5() throws Exception {
        test("h5");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading6}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,constructor()",
            IE = "align,clear,constructor")
    public void h6() throws Exception {
        test("h6");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHorizontalRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,color,constructor(),noShade,size,width",
            IE = "align,color,constructor,noShade,size,width")
    @HtmlUnitNYI(CHROME = "align,color,constructor(),width",
            EDGE = "align,color,constructor(),width",
            FF78 = "align,color,constructor(),width",
            FF = "align,color,constructor(),width",
            IE = "align,color,constructor,width")
    public void hr() throws Exception {
        test("hr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHtml}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),version",
            EDGE = "constructor(),version",
            FF = "constructor(),version",
            FF78 = "constructor(),version",
            IE = "constructor,version")
    public void html() throws Exception {
        test("html");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInlineFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,allow,allowFullscreen,allowPaymentRequest,constructor(),contentDocument,contentWindow,"
                + "csp,featurePolicy,frameBorder,getSVGDocument(),height,"
                + "loading,longDesc,marginHeight,marginWidth,name,"
                + "referrerPolicy,sandbox,scrolling,src,srcdoc,"
                + "width",
            EDGE = "align,allow,allowFullscreen,allowPaymentRequest,constructor(),contentDocument,contentWindow,csp,"
                + "featurePolicy,frameBorder,getSVGDocument(),height,loading,longDesc,marginHeight,marginWidth,name,"
                + "referrerPolicy,sandbox,scrolling,src,srcdoc,"
                + "width",
            FF = "align,allow,allowFullscreen,constructor(),contentDocument,contentWindow,"
                + "frameBorder,getSVGDocument(),height,longDesc,marginHeight,marginWidth,name,referrerPolicy,"
                + "sandbox,scrolling,src,srcdoc,"
                + "width",
            FF78 = "align,allow,allowFullscreen,allowPaymentRequest,constructor(),contentDocument,contentWindow,"
                + "frameBorder,getSVGDocument(),height,longDesc,marginHeight,marginWidth,name,referrerPolicy,"
                + "sandbox,scrolling,src,srcdoc,"
                + "width",
            IE = "align,border,constructor,contentDocument,contentWindow,frameBorder,frameSpacing,getSVGDocument(),"
                + "height,hspace,longDesc,marginHeight,marginWidth,name,noResize,onload,sandbox,scrolling,security,"
                + "src,vspace,"
                + "width")
    @HtmlUnitNYI(CHROME = "align,constructor(),contentDocument,contentWindow,height,name,src,width",
            EDGE = "align,constructor(),contentDocument,contentWindow,height,name,src,width",
            FF78 = "align,constructor(),contentDocument,contentWindow,height,name,src,width",
            FF = "align,constructor(),contentDocument,contentWindow,height,name,src,width",
            IE = "align,border,constructor,contentDocument,contentWindow,height,name,src,width")
    public void iframe() throws Exception {
        test("iframe");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInlineQuotation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "cite,constructor()",
            IE = "cite,constructor,dateTime")
    public void q() throws Exception {
        test("q");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,alt,border,complete,constructor(),crossOrigin,currentSrc,decode(),decoding,height,hspace,"
                + "isMap,loading,longDesc,lowsrc,name,naturalHeight,naturalWidth,referrerPolicy,sizes,src,srcset,"
                + "useMap,vspace,width,x,y",
            EDGE = "align,alt,border,complete,constructor(),crossOrigin,currentSrc,decode(),decoding,height,hspace,"
                + "isMap,loading,longDesc,lowsrc,name,naturalHeight,naturalWidth,referrerPolicy,sizes,src,srcset,"
                + "useMap,vspace,width,x,"
                + "y",
            FF = "align,alt,border,complete,constructor(),crossOrigin,currentSrc,decode(),decoding,height,hspace,"
                + "isMap,loading,longDesc,lowsrc,name,naturalHeight,naturalWidth,referrerPolicy,sizes,src,srcset,"
                + "useMap,vspace,width,x,y",
            FF78 = "align,alt,border,complete,constructor(),crossOrigin,currentSrc,decode(),decoding,height,hspace,"
                + "isMap,longDesc,lowsrc,name,naturalHeight,naturalWidth,referrerPolicy,sizes,src,srcset,useMap,"
                + "vspace,width,x,y",
            IE = "align,alt,border,complete,constructor,crossOrigin,dynsrc,fileCreatedDate,fileModifiedDate,"
                + "fileUpdatedDate,height,href,hspace,isMap,longDesc,loop,lowsrc,mimeType,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,name,nameProp,naturalHeight,naturalWidth,"
                + "protocol,src,start,useMap,vrml,vspace,width")
    @HtmlUnitNYI(CHROME = "align,alt,border,complete,constructor(),height,name,naturalHeight,naturalWidth,src,width",
            EDGE = "align,alt,border,complete,constructor(),height,name,naturalHeight,naturalWidth,src,width",
            FF78 = "align,alt,border,complete,constructor(),height,name,naturalHeight,naturalWidth,src,width",
            FF = "align,alt,border,complete,constructor(),height,name,naturalHeight,naturalWidth,src,width",
            IE = "align,alt,border,complete,constructor(),height,name,naturalHeight,naturalWidth,src,width")
    public void img() throws Exception {
        test("img");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "align,alt,border,complete,constructor,crossOrigin,dynsrc,fileCreatedDate,fileModifiedDate,"
                + "fileUpdatedDate,height,href,hspace,isMap,longDesc,loop,lowsrc,mimeType,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,name,nameProp,naturalHeight,naturalWidth,protocol,src,"
                + "start,useMap,vrml,vspace,"
                + "width")
    @HtmlUnitNYI(FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "align,alt,border,complete,constructor(),height,name,naturalHeight,naturalWidth,src,width")
    public void image() throws Exception {
        test("image");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "cite,constructor(),dateTime",
            EDGE = "cite,constructor(),dateTime",
            FF = "cite,constructor(),dateTime",
            FF78 = "cite,constructor(),dateTime",
            IE = "cite,constructor,dateTime")
    public void ins() throws Exception {
        test("ins");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlIsIndex}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "action,constructor,form,prompt")
    @HtmlUnitNYI(IE = "constructor")
    public void isindex() throws Exception {
        test("isindex");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlItalic}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void i() throws Exception {
        test("i");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlKeyboard}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void kbd() throws Exception {
        test("kbd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "constructor()",
            IE = "cite,clear,constructor,width")
    @HtmlUnitNYI(IE = "clear,constructor,width")
    public void keygen() throws Exception {
        test("keygen");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLabel}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),control,form,htmlFor",
            EDGE = "constructor(),control,form,htmlFor",
            FF = "constructor(),control,form,htmlFor",
            FF78 = "constructor(),control,form,htmlFor",
            IE = "constructor,form,htmlFor")
    public void label() throws Exception {
        test("label");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,"
                + "onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,"
                + "onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,"
                + "onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,"
                + "onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            IE = "constructor")
    public void layer() throws Exception {
        test("layer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLegend}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,constructor(),form",
            IE = "align,constructor,form")
    public void legend() throws Exception {
        test("legend");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlListing}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),width",
            EDGE = "constructor(),width",
            FF = "constructor(),width",
            FF78 = "constructor(),width",
            IE = "cite,clear,constructor,width")
    @HtmlUnitNYI(IE = "clear,constructor,width")
    public void listing() throws Exception {
        test("listing");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlListItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),type,value",
            EDGE = "constructor(),type,value",
            FF = "constructor(),type,value",
            FF78 = "constructor(),type,value",
            IE = "constructor,type,value")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor()",
            FF = "constructor()",
            IE = "constructor")
    public void li() throws Exception {
        test("li");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "as,charset,constructor(),crossOrigin,disabled,href,hreflang,imageSizes,imageSrcset,"
                + "integrity,media,referrerPolicy,rel,relList,rev,sheet,sizes,target,type",
            EDGE = "as,charset,constructor(),crossOrigin,disabled,href,hreflang,imageSizes,imageSrcset,integrity,"
                + "media,referrerPolicy,rel,relList,rev,sheet,sizes,target,"
                + "type",
            FF = "as,charset,constructor(),crossOrigin,disabled,href,hreflang,imageSizes,imageSrcset,"
                + "integrity,media,referrerPolicy,rel,relList,rev,sheet,sizes,target,"
                + "type",
            FF78 = "as,charset,constructor(),crossOrigin,disabled,href,hreflang,imageSizes,imageSrcset,integrity,"
                + "media,referrerPolicy,rel,relList,rev,sheet,sizes,target,"
                + "type",
            IE = "charset,constructor,href,hreflang,media,rel,rev,sheet,target,type")
    @HtmlUnitNYI(CHROME = "constructor(),disabled,href,rel,relList,rev,type",
            EDGE = "constructor(),disabled,href,rel,relList,rev,type",
            FF78 = "constructor(),disabled,href,rel,relList,rev,type",
            FF = "constructor(),disabled,href,rel,relList,rev,type",
            IE = "constructor,disabled,href,rel,rev,type")
    public void link() throws Exception {
        test("link");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMain}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,"
                + "tabIndex,title",
           FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,"
                + "tabIndex,title",
           IE = "constructor")
    public void main() throws Exception {
        test("main");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMap}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "areas,constructor(),name",
            EDGE = "areas,constructor(),name",
            FF = "areas,constructor(),name",
            FF78 = "areas,constructor(),name",
            IE = "areas,constructor,name")
    public void map() throws Exception {
        test("map");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMark}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,"
                + "onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,"
                + "onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,"
                + "onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,onplay,onplaying,"
                + "onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,parentElement,releaseCapture(),"
                + "removeNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),style,tabIndex,title,uniqueID")
    public void mark() throws Exception {
        test("mark");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMarquee}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "behavior,bgColor,constructor(),direction,height,hspace,loop,scrollAmount,scrollDelay,start(),"
                + "stop(),trueSpeed,vspace,"
                + "width",
            EDGE = "behavior,bgColor,constructor(),direction,height,hspace,loop,scrollAmount,scrollDelay,start(),"
                + "stop(),trueSpeed,vspace,"
                + "width",
            FF = "behavior,bgColor,constructor(),direction,height,hspace,loop,onbounce,onfinish,onstart,"
                + "scrollAmount,scrollDelay,start(),stop(),trueSpeed,vspace,"
                + "width",
            FF78 = "behavior,bgColor,constructor(),direction,height,hspace,loop,onbounce,onfinish,onstart,"
                + "scrollAmount,scrollDelay,start(),stop(),trueSpeed,vspace,"
                + "width",
            IE = "behavior,bgColor,constructor,direction,height,hspace,loop,onbounce,onfinish,onstart,scrollAmount,"
                + "scrollDelay,start(),stop(),trueSpeed,vspace,"
                + "width")
    @HtmlUnitNYI(CHROME = "bgColor,constructor(),height,width",
            EDGE = "bgColor,constructor(),height,width",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "bgColor,constructor,height,width")
    public void marquee() throws Exception {
        test("marquee");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMenu}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "compact,constructor()",
            FF78 = "compact,constructor(),label,type",
            IE = "compact,constructor,type")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor(),label,type",
            FF = "constructor()",
            IE = "constructor,type")
    public void menu() throws Exception {
        test("menu");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMenuItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "constructor()",
            FF78 = "checked,constructor(),defaultChecked,disabled,icon,label,radiogroup,type",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(FF78 = "constructor()",
            IE = "constructor")
    public void menuitem() throws Exception {
        test("menuitem");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMeta}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "constructor(),content,httpEquiv,name,scheme",
            IE = "charset,constructor,content,httpEquiv,name,scheme,url")
    public void meta() throws Exception {
        test("meta");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMeter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),high,labels,low,max,min,optimum,value",
            EDGE = "constructor(),high,labels,low,max,min,optimum,value",
            FF = "constructor(),high,labels,low,max,min,optimum,value",
            FF78 = "constructor(),high,labels,low,max,min,optimum,value",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void meter() throws Exception {
        test("meter");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMultiColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void multicol() throws Exception {
        test("multicol");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNav}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,offsetTop,"
                + "offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncuechange,"
                + "oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,offsetTop,"
                + "offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncuechange,"
                + "oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),"
                + "isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,offsetParent,offsetTop,"
                + "offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,onbeforedeactivate,"
                + "onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncuechange,"
                + "oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhelp,oninput,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,onplay,onplaying,onprogress,"
                + "onratechange,onreset,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,parentElement,releaseCapture(),"
                + "removeNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),style,tabIndex,title,uniqueID")
    public void nav() throws Exception {
        test("nav");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNextId}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "constructor()",
            IE = "constructor,n")
    @HtmlUnitNYI(IE = "constructor")
    public void nextid() throws Exception {
        test("nextid");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,"
                + "onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,"
                + "onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,"
                + "title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,"
                + "title",
            IE = "constructor")
    public void nobr() throws Exception {
        test("nobr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,"
                + "onfocus,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,"
                + "onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,"
                + "onfocus,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,"
                + "onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,"
                + "onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,"
                + "onfocus,onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,"
                + "onpause,onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,"
                + "onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,"
                + "outerHTML,parentElement,releaseCapture(),removeNode(),runtimeStyle,scrollIntoView(),"
                + "setActive(),setCapture(),style,tabIndex,title,uniqueID")
    public void noembed() throws Exception {
        test("noembed");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoFrames}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,"
                + "tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,"
                + "tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + ""
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),"
                + "isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,offsetParent,offsetTop,"
                + "offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,onbeforedeactivate,"
                + "onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncuechange,"
                + "oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhelp,oninput,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onmscontentzoom,"
                + "onmsmanipulationstatechanged,onpaste,onpause,onplay,onplaying,onprogress,onratechange,onreset,"
                + "onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "onvolumechange,onwaiting,outerHTML,parentElement,releaseCapture(),removeNode(),runtimeStyle,"
                + "scrollIntoView(),setActive(),setCapture(),style,tabIndex,title,uniqueID")
    public void noframes() throws Exception {
        test("noframes");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            IE = "constructor")
    public void nolayer() throws Exception {
        test("nolayer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,"
                + "onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,"
                + "onpause,onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "parentElement,releaseCapture(),removeNode(),runtimeStyle,scrollIntoView(),setActive(),"
                + "setCapture(),style,tabIndex,title,uniqueID")
    public void noscript() throws Exception {
        test("noscript");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlObject}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,archive,border,checkValidity(),code,codeBase,codeType,constructor(),contentDocument,"
                + "contentWindow,data,declare,form,getSVGDocument(),height,hspace,name,reportValidity(),"
                + "setCustomValidity(),standby,type,useMap,validationMessage,validity,vspace,width,"
                + "willValidate",
            EDGE = "align,archive,border,checkValidity(),code,codeBase,codeType,constructor(),contentDocument,"
                + "contentWindow,data,declare,form,getSVGDocument(),height,hspace,name,reportValidity(),"
                + "setCustomValidity(),standby,type,useMap,validationMessage,validity,vspace,width,"
                + "willValidate",
            FF = "align,archive,border,checkValidity(),code,codeBase,codeType,constructor(),contentDocument,"
                + "contentWindow,data,declare,form,getSVGDocument(),height,hspace,name,reportValidity(),"
                + "setCustomValidity(),standby,type,useMap,validationMessage,validity,vspace,width,"
                + "willValidate",
            FF78 = "align,archive,border,checkValidity(),code,codeBase,codeType,constructor(),contentDocument,"
                + "contentWindow,data,declare,form,getSVGDocument(),height,hspace,name,reportValidity(),"
                + "setCustomValidity(),standby,type,useMap,validationMessage,validity,vspace,width,"
                + "willValidate",
            IE = "align,alt,altHtml,archive,BaseHref,border,checkValidity(),classid,code,codeBase,codeType,"
                + "constructor,contentDocument,data,declare,form,getSVGDocument(),height,hspace,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,name,namedRecordset(),object,readyState,recordset,"
                + "setCustomValidity(),standby,type,useMap,validationMessage,validity,vspace,width,"
                + "willValidate")
    @HtmlUnitNYI(CHROME = "align,border,checkValidity(),constructor(),form,height,name,width",
            EDGE = "align,border,checkValidity(),constructor(),form,height,name,width",
            FF78 = "align,border,checkValidity(),constructor(),form,height,name,width",
            FF = "align,border,checkValidity(),constructor(),form,height,name,width",
            IE = "align,alt,border,checkValidity(),classid,constructor,form,height,name,width")
    public void object() throws Exception {
        test("object");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOrderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "compact,constructor(),reversed,start,type",
            IE = "compact,constructor,start,type")
    @HtmlUnitNYI(CHROME = "constructor(),type",
            EDGE = "constructor(),type",
            FF78 = "constructor(),type",
            FF = "constructor(),type",
            IE = "constructor,type")
    public void ol() throws Exception {
        test("ol");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOptionGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),disabled,label",
            EDGE = "constructor(),disabled,label",
            FF = "constructor(),disabled,label",
            FF78 = "constructor(),disabled,label",
            IE = "constructor,defaultSelected,form,index,label,selected,text,value")
    @HtmlUnitNYI(IE = "constructor,disabled,label")
    public void optgroup() throws Exception {
        test("optgroup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "constructor(),defaultSelected,disabled,form,index,label,selected,text,value",
            IE = "constructor,defaultSelected,form,index,label,selected,text,value")
    @HtmlUnitNYI(IE = "constructor,defaultSelected,disabled,form,index,label,selected,text,value")
    public void option() throws Exception {
        test("option");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOutput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "checkValidity(),constructor(),defaultValue,form,htmlFor,labels,name,reportValidity(),"
                + "setCustomValidity(),type,validationMessage,validity,value,"
                + "willValidate",
            EDGE = "checkValidity(),constructor(),defaultValue,form,htmlFor,labels,name,reportValidity(),"
                + "setCustomValidity(),type,validationMessage,validity,value,"
                + "willValidate",
            FF = "checkValidity(),constructor(),defaultValue,form,htmlFor,labels,name,reportValidity(),"
                + "setCustomValidity(),type,validationMessage,validity,value,"
                + "willValidate",
            FF78 = "checkValidity(),constructor(),defaultValue,form,htmlFor,labels,name,reportValidity(),"
                + "setCustomValidity(),type,validationMessage,validity,value,"
                + "willValidate",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(CHROME = "checkValidity(),constructor(),labels,name",
            EDGE = "checkValidity(),constructor(),labels,name",
            FF78 = "checkValidity(),constructor(),labels,name",
            FF = "checkValidity(),constructor(),labels,name",
            IE = "constructor")
    public void output() throws Exception {
        test("output");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlParagraph}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,constructor()",
            IE = "align,clear,constructor")
    public void p() throws Exception {
        test("p");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlParameter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),name,type,value,valueType",
            EDGE = "constructor(),name,type,value,valueType",
            FF = "constructor(),name,type,value,valueType",
            FF78 = "constructor(),name,type,value,valueType",
            IE = "constructor,name,type,value,valueType")
    public void param() throws Exception {
        test("param");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPlainText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,clear,constructor,width")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            IE = "clear,constructor,width")
    public void plaintext() throws Exception {
        test("plaintext");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPreformattedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),width",
            EDGE = "constructor(),width",
            FF = "constructor(),width",
            FF78 = "constructor(),width",
            IE = "cite,clear,constructor,width")
    public void pre() throws Exception {
        test("pre");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlProgress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "constructor(),labels,max,position,value",
            IE = "constructor,form,max,position,value")
    @HtmlUnitNYI(CHROME = "constructor(),labels,max,value",
            EDGE = "constructor(),labels,max,value",
            FF78 = "constructor(),labels,max,value",
            FF = "constructor(),labels,max,value",
            IE = "constructor,max,value")
    public void progress() throws Exception {
        test("progress");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRp}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "constructor()",
            FF = "constructor()",
            IE = "constructor")
    public void rp() throws Exception {
        test("rp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRt}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "constructor()",
            FF = "constructor()",
            IE = "constructor")
    public void rt() throws Exception {
        test("rt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRuby}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            FF78 = "constructor()",
            FF = "constructor()",
            IE = "constructor")
    public void ruby() throws Exception {
        test("ruby");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlS}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void s() throws Exception {
        test("s");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void samp() throws Exception {
        test("samp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "async,charset,constructor(),crossOrigin,defer,event,htmlFor,"
                        + "integrity,noModule,referrerPolicy,src,text,type",
            EDGE = "async,charset,constructor(),crossOrigin,defer,event,htmlFor,integrity,noModule,referrerPolicy,"
                + "src,text,"
                + "type",
            FF = "async,charset,constructor(),crossOrigin,defer,event,htmlFor,integrity,noModule,referrerPolicy,"
                + "src,text,"
                + "type",
            FF78 = "async,charset,constructor(),crossOrigin,defer,event,htmlFor,"
                        + "integrity,noModule,referrerPolicy,src,text,type",
            IE = "async,charset,constructor,crossOrigin,defer,event,htmlFor,src,text,type")
    @HtmlUnitNYI(CHROME = "async,constructor(),src,text,type",
            EDGE = "async,constructor(),src,text,type",
            FF78 = "async,constructor(),src,text,type",
            FF = "async,constructor(),src,text,type",
            IE = "async,constructor,src,text,type")
    public void script() throws Exception {
        test("script");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,"
                + "ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,"
                + "ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwheel,"
                + "style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,"
                + "onclick,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onshow,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,"
                + "onclick,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,"
                + "style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),isContentEditable,lang,language,mergeAttributes(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,"
                + "onbeforecopy,onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,"
                + "oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondeactivate,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhelp,oninput,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,onplay,onplaying,onprogress,"
                + "onratechange,onreset,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,parentElement,releaseCapture(),"
                + "removeNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),style,tabIndex,title,"
                + "uniqueID")
    public void section() throws Exception {
        test("section");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "add(),autocomplete,checkValidity(),constructor(),disabled,form,item(),labels,length,multiple,"
                + "name,namedItem(),options,remove(),reportValidity(),required,selectedIndex,selectedOptions,"
                + "setCustomValidity(),size,type,validationMessage,validity,value,willValidate",
            EDGE = "add(),autocomplete,checkValidity(),constructor(),disabled,form,item(),labels,length,multiple,"
                + "name,namedItem(),options,remove(),reportValidity(),required,selectedIndex,selectedOptions,"
                + "setCustomValidity(),size,type,validationMessage,validity,value,"
                + "willValidate",
            FF = "add(),autocomplete,autofocus,checkValidity(),constructor(),disabled,form,item(),labels,length,"
                + "multiple,name,namedItem(),options,remove(),reportValidity(),required,selectedIndex,"
                + "selectedOptions,setCustomValidity(),size,type,validationMessage,validity,value,"
                + "willValidate",
            FF78 = "add(),autocomplete,autofocus,checkValidity(),constructor(),disabled,form,item(),labels,length,"
                + "multiple,name,namedItem(),options,remove(),reportValidity(),required,selectedIndex,"
                + "selectedOptions,setCustomValidity(),size,type,validationMessage,validity,value,"
                + "willValidate",
            IE = "add(),autofocus,checkValidity(),constructor,form,item(),length,multiple,name,namedItem(),options,"
                + "remove(),required,selectedIndex,setCustomValidity(),size,tags(),type,urns(),validationMessage,"
                + "validity,value,willValidate")
    @HtmlUnitNYI(CHROME = "add(),checkValidity(),constructor(),disabled,form,item(),labels,length,multiple,"
                + "name,options,remove(),required,selectedIndex,size,type,value",
            EDGE = "add(),checkValidity(),constructor(),disabled,form,item(),labels,length,multiple,"
                + "name,options,remove(),required,selectedIndex,size,type,value",
            FF78 = "add(),checkValidity(),constructor(),disabled,form,item(),labels,length,multiple,"
                + "name,options,remove(),required,selectedIndex,size,type,value",
            FF = "add(),checkValidity(),constructor(),disabled,form,item(),labels,length,multiple,"
                + "name,options,remove(),required,selectedIndex,size,type,value",
            IE = "add(),checkValidity(),constructor,form,item(),length,multiple,"
                + "name,options,remove(),required,selectedIndex,size,type,value")
    public void select() throws Exception {
        test("select");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSmall}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void small() throws Exception {
        test("small");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSource}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),media,sizes,src,srcset,type",
            EDGE = "constructor(),media,sizes,src,srcset,type",
            FF = "constructor(),media,sizes,src,srcset,type",
            FF78 = "constructor(),media,sizes,src,srcset,type",
            IE = "constructor,media,msKeySystem,src,type")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor()",
            FF = "constructor()",
            IE = "constructor")
    public void source() throws Exception {
        test("source");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSpan}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "constructor()",
            IE = "constructor")
    public void span() throws Exception {
        test("span");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStrike}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void strike() throws Exception {
        test("strike");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStrong}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void strong() throws Exception {
        test("strong");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStyle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),disabled,media,sheet,type",
            EDGE = "constructor(),disabled,media,sheet,type",
            FF = "constructor(),disabled,media,sheet,type",
            FF78 = "constructor(),disabled,media,sheet,type",
            IE = "constructor,media,sheet,type")
    @HtmlUnitNYI(IE = "constructor,disabled,media,sheet,type")
    public void style() throws Exception {
        test("style");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSubscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void sub() throws Exception {
        test("sub");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSummary}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,"
                + "dir,draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,"
                + "onanimationiteration,onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,"
                + "onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,"
                + "ondragend,ondragenter,ondragexit,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,"
                + "onloadend,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onshow,onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,"
                + "ontransitionend,ontransitionrun,ontransitionstart,onvolumechange,onwaiting,"
                + "onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                + "onwebkittransitionend,onwheel,spellcheck,style,tabIndex,title",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void summary() throws Exception {
        test("summary");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void sup() throws Exception {
        test("sup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSvg}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void svg() throws Exception {
        test("svg");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,bgColor,border,caption,cellPadding,cellSpacing,constructor(),createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),frame,"
                + "insertRow(),rows,rules,summary,tBodies,tFoot,tHead,"
                + "width",
            EDGE = "align,bgColor,border,caption,cellPadding,cellSpacing,constructor(),createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),frame,"
                + "insertRow(),rows,rules,summary,tBodies,tFoot,tHead,"
                + "width",
            FF = "align,bgColor,border,caption,cellPadding,cellSpacing,constructor(),createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),frame,"
                + "insertRow(),rows,rules,summary,tBodies,tFoot,tHead,"
                + "width",
            FF78 = "align,bgColor,border,caption,cellPadding,cellSpacing,constructor(),createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),frame,"
                + "insertRow(),rows,rules,summary,tBodies,tFoot,tHead,"
                + "width",
            IE = "align,background,bgColor,border,borderColor,borderColorDark,borderColorLight,caption,cellPadding,"
                + "cells,cellSpacing,cols,constructor,createCaption(),createTBody(),createTFoot(),createTHead(),"
                + "deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),frame,height,insertRow(),moveRow(),rows,"
                + "rules,summary,tBodies,tFoot,tHead,"
                + "width")
    @HtmlUnitNYI(CHROME = "bgColor,border,caption,cellPadding,cellSpacing,constructor(),createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteTFoot(),deleteTHead(),rules,summary,tBodies,"
                + "tFoot,tHead,width",
            EDGE = "bgColor,border,caption,cellPadding,cellSpacing,constructor(),createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteTFoot(),deleteTHead(),rules,summary,tBodies,"
                + "tFoot,tHead,width",
            FF78 = "bgColor,border,caption,cellPadding,cellSpacing,constructor(),createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteTFoot(),deleteTHead(),rules,summary,"
                + "tBodies,tFoot,tHead,width",
            FF = "bgColor,border,caption,cellPadding,cellSpacing,constructor(),createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteTFoot(),deleteTHead(),rules,summary,"
                + "tBodies,tFoot,tHead,width",
            IE = "bgColor,border,borderColor,borderColorDark,borderColorLight,caption,cellPadding,cellSpacing,"
                + "constructor,createCaption(),createTBody(),createTFoot(),createTHead(),deleteCaption(),"
                + "deleteTFoot(),deleteTHead(),rules,summary,tBodies,tFoot,tHead,width")
    public void table() throws Exception {
        test("table");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,ch,chOff,constructor(),span,vAlign,width",
            EDGE = "align,ch,chOff,constructor(),span,vAlign,width",
            FF = "align,ch,chOff,constructor(),span,vAlign,width",
            FF78 = "align,ch,chOff,constructor(),span,vAlign,width",
            IE = "align,ch,chOff,constructor,span,vAlign,width")
    @HtmlUnitNYI(CHROME = "constructor(),span,width",
            EDGE = "constructor(),span,width",
            FF78 = "constructor(),span,width",
            FF = "constructor(),span,width",
            IE = "constructor,span,width")
    public void col() throws Exception {
        test("col");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableColumnGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,ch,chOff,constructor(),span,vAlign,width",
            EDGE = "align,ch,chOff,constructor(),span,vAlign,width",
            FF = "align,ch,chOff,constructor(),span,vAlign,width",
            FF78 = "align,ch,chOff,constructor(),span,vAlign,width",
            IE = "align,ch,chOff,constructor,span,vAlign,width")
    @HtmlUnitNYI(CHROME = "constructor(),span,width",
            EDGE = "constructor(),span,width",
            FF78 = "constructor(),span,width",
            FF = "constructor(),span,width",
            IE = "constructor,span,width")
    public void colgroup() throws Exception {
        test("colgroup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            EDGE = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            FF = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            FF78 = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            IE = "align,bgColor,ch,chOff,constructor,deleteRow(),insertRow(),moveRow(),rows,vAlign")
    @HtmlUnitNYI(CHROME = "ch,chOff,constructor(),vAlign",
            EDGE = "ch,chOff,constructor(),vAlign",
            FF78 = "ch,chOff,constructor(),vAlign",
            FF = "ch,chOff,constructor(),vAlign",
            IE = "bgColor,ch,chOff,constructor,vAlign")
    public void tbody() throws Exception {
        test("tbody");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableDataCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "abbr,align,axis,bgColor,cellIndex,ch,chOff,colSpan,constructor(),headers,height,noWrap,rowSpan,"
                + "scope,vAlign,"
                + "width",
            EDGE = "abbr,align,axis,bgColor,cellIndex,ch,chOff,colSpan,constructor(),headers,height,noWrap,rowSpan,"
                + "scope,vAlign,"
                + "width",
            FF = "abbr,align,axis,bgColor,cellIndex,ch,chOff,colSpan,constructor(),headers,height,noWrap,rowSpan,"
                + "scope,vAlign,"
                + "width",
            FF78 = "abbr,align,axis,bgColor,cellIndex,ch,chOff,colSpan,constructor(),headers,height,noWrap,rowSpan,"
                + "scope,vAlign,"
                + "width",
            IE = "constructor")
    @HtmlUnitNYI(CHROME = "abbr,axis,bgColor,cellIndex,colSpan,constructor(),headers,height,noWrap,rowSpan,"
                + "scope,width",
            EDGE = "abbr,axis,bgColor,cellIndex,colSpan,constructor(),headers,height,noWrap,rowSpan,"
                + "scope,width",
            FF78 = "abbr,axis,bgColor,cellIndex,colSpan,constructor(),headers,height,noWrap,rowSpan,scope,width",
            FF = "abbr,axis,bgColor,cellIndex,colSpan,constructor(),headers,height,noWrap,rowSpan,scope,width")
    public void td() throws Exception {
        test("td");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "abbr,align,axis,bgColor,cellIndex,ch,chOff,colSpan,constructor(),headers,height,noWrap,rowSpan,"
                + "scope,vAlign,"
                + "width",
            EDGE = "abbr,align,axis,bgColor,cellIndex,ch,chOff,colSpan,constructor(),headers,height,noWrap,rowSpan,"
                + "scope,vAlign,"
                + "width",
            FF = "abbr,align,axis,bgColor,cellIndex,ch,chOff,colSpan,constructor(),headers,height,noWrap,rowSpan,"
                + "scope,vAlign,"
                + "width",
            FF78 = "abbr,align,axis,bgColor,cellIndex,ch,chOff,colSpan,constructor(),headers,height,noWrap,rowSpan,"
                + "scope,vAlign,"
                + "width",
            IE = "constructor,scope")
    @HtmlUnitNYI(CHROME = "abbr,axis,bgColor,cellIndex,colSpan,constructor(),headers,height,noWrap,rowSpan,scope,width",
            EDGE = "abbr,axis,bgColor,cellIndex,colSpan,constructor(),headers,height,noWrap,rowSpan,scope,width",
            FF78 = "abbr,axis,bgColor,cellIndex,colSpan,constructor(),headers,height,noWrap,rowSpan,scope,width",
            FF = "abbr,axis,bgColor,cellIndex,colSpan,constructor(),headers,height,noWrap,rowSpan,scope,width",
            IE = "constructor")
    public void th() throws Exception {
        test("th");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableRow}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,bgColor,cells,ch,chOff,constructor(),deleteCell(),insertCell(),rowIndex,sectionRowIndex,"
                + "vAlign",
            EDGE = "align,bgColor,cells,ch,chOff,constructor(),deleteCell(),insertCell(),rowIndex,sectionRowIndex,"
                + "vAlign",
            FF = "align,bgColor,cells,ch,chOff,constructor(),deleteCell(),insertCell(),rowIndex,sectionRowIndex,"
                + "vAlign",
            FF78 = "align,bgColor,cells,ch,chOff,constructor(),deleteCell(),insertCell(),rowIndex,sectionRowIndex,"
                + "vAlign",
            IE = "align,bgColor,borderColor,borderColorDark,borderColorLight,cells,ch,chOff,constructor,"
                + "deleteCell(),height,insertCell(),rowIndex,sectionRowIndex,"
                + "vAlign")
    @HtmlUnitNYI(CHROME = "bgColor,cells,constructor(),deleteCell(),insertCell(),rowIndex,sectionRowIndex",
            EDGE = "bgColor,cells,constructor(),deleteCell(),insertCell(),rowIndex,sectionRowIndex",
            FF78 = "bgColor,cells,constructor(),deleteCell(),insertCell(),rowIndex,sectionRowIndex",
            FF = "bgColor,cells,constructor(),deleteCell(),insertCell(),rowIndex,sectionRowIndex",
            IE = "bgColor,borderColor,borderColorDark,borderColorLight,cells,constructor,"
                + "deleteCell(),insertCell(),rowIndex,sectionRowIndex")
    public void tr() throws Exception {
        test("tr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTextArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "autocomplete,checkValidity(),cols,constructor(),defaultValue,dirName,disabled,form,labels,"
                + "maxLength,minLength,name,placeholder,readOnly,reportValidity(),required,rows,select(),"
                + "selectionDirection,selectionEnd,selectionStart,setCustomValidity(),setRangeText(),"
                + "setSelectionRange(),textLength,type,validationMessage,validity,value,willValidate,"
                + "wrap",
            EDGE = "autocomplete,checkValidity(),cols,constructor(),defaultValue,dirName,disabled,form,labels,"
                + "maxLength,minLength,name,placeholder,readOnly,reportValidity(),required,rows,select(),"
                + "selectionDirection,selectionEnd,selectionStart,setCustomValidity(),setRangeText(),"
                + "setSelectionRange(),textLength,type,validationMessage,validity,value,willValidate,"
                + "wrap",
            FF = "autocomplete,autofocus,checkValidity(),cols,constructor(),defaultValue,disabled,form,labels,"
                + "maxLength,minLength,name,placeholder,readOnly,reportValidity(),required,rows,select(),"
                + "selectionDirection,selectionEnd,selectionStart,setCustomValidity(),setRangeText(),"
                + "setSelectionRange(),textLength,type,validationMessage,validity,value,willValidate,"
                + "wrap",
            FF78 = "autocomplete,autofocus,checkValidity(),cols,constructor(),defaultValue,disabled,form,"
                + "labels,maxLength,minLength,name,placeholder,"
                + "readOnly,reportValidity(),required,rows,select(),selectionDirection,selectionEnd,"
                + "selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),"
                + "textLength,type,validationMessage,validity,value,willValidate,wrap",
            IE = "autofocus,checkValidity(),cols,constructor,createTextRange(),defaultValue,form,maxLength,name,"
                + "placeholder,readOnly,required,rows,select(),selectionEnd,selectionStart,setCustomValidity(),"
                + "setSelectionRange(),status,type,validationMessage,validity,value,willValidate,"
                + "wrap")
    @HtmlUnitNYI(CHROME = "checkValidity(),cols,constructor(),defaultValue,disabled,form,labels,"
                + "maxLength,minLength,name,placeholder,readOnly,required,rows,select(),selectionEnd,"
                + "selectionStart,setSelectionRange(),textLength,type,value",
            EDGE = "checkValidity(),cols,constructor(),defaultValue,disabled,form,labels,"
                + "maxLength,minLength,name,placeholder,readOnly,required,rows,select(),selectionEnd,"
                + "selectionStart,setSelectionRange(),textLength,type,value",
            FF78 = "checkValidity(),cols,constructor(),defaultValue,disabled,form,labels,maxLength,minLength,name,"
                + "placeholder,readOnly,required,rows,select(),selectionEnd,selectionStart,setSelectionRange(),"
                + "textLength,type,value",
            FF = "checkValidity(),cols,constructor(),defaultValue,disabled,form,labels,maxLength,minLength,name,"
                + "placeholder,readOnly,required,rows,select(),selectionEnd,selectionStart,setSelectionRange(),"
                + "textLength,type,value",
            IE = "checkValidity(),cols,constructor,createTextRange(),defaultValue,form,maxLength,name,placeholder,"
                + "readOnly,required,rows,select(),selectionEnd,selectionStart,setSelectionRange(),type,value")
    public void textarea() throws Exception {
        test("textarea");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            EDGE = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            FF = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            FF78 = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            IE = "align,bgColor,ch,chOff,constructor,deleteRow(),insertRow(),moveRow(),rows,vAlign")
    @HtmlUnitNYI(CHROME = "ch,chOff,constructor(),vAlign",
            EDGE = "ch,chOff,constructor(),vAlign",
            FF78 = "ch,chOff,constructor(),vAlign",
            FF = "ch,chOff,constructor(),vAlign",
            IE = "bgColor,ch,chOff,constructor,vAlign")
    public void tfoot() throws Exception {
        test("tfoot");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            EDGE = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            FF = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            FF78 = "align,ch,chOff,constructor(),deleteRow(),insertRow(),rows,vAlign",
            IE = "align,bgColor,ch,chOff,constructor,deleteRow(),insertRow(),moveRow(),rows,vAlign")
    @HtmlUnitNYI(CHROME = "ch,chOff,constructor(),vAlign",
            EDGE = "ch,chOff,constructor(),vAlign",
            FF78 = "ch,chOff,constructor(),vAlign",
            FF = "ch,chOff,constructor(),vAlign",
            IE = "bgColor,ch,chOff,constructor,vAlign")
    public void thead() throws Exception {
        test("thead");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTeletype}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void tt() throws Exception {
        test("tt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTime}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "constructor(),dateTime",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void time() throws Exception {
        test("time");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTitle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),text",
            EDGE = "constructor(),text",
            FF = "constructor(),text",
            FF78 = "constructor(),text",
            IE = "constructor,text")
    public void title() throws Exception {
        test("title");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTrack}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),default,ERROR,kind,label,LOADED,LOADING,NONE,readyState,src,srclang,track",
            EDGE = "constructor(),default,ERROR,kind,label,LOADED,LOADING,NONE,readyState,src,srclang,track",
            FF = "constructor(),default,ERROR,kind,label,LOADED,LOADING,NONE,readyState,src,srclang,track",
            FF78 = "constructor(),default,ERROR,kind,label,LOADED,LOADING,NONE,readyState,src,srclang,track",
            IE = "constructor,default,ERROR,kind,label,LOADED,LOADING,NONE,readyState,src,srclang,track")
    @HtmlUnitNYI(CHROME = "constructor(),ERROR,LOADED,LOADING,NONE",
            EDGE = "constructor(),ERROR,LOADED,LOADING,NONE",
            FF78 = "constructor(),ERROR,LOADED,LOADING,NONE",
            FF = "constructor(),ERROR,LOADED,LOADING,NONE",
            IE = "constructor,ERROR,LOADED,LOADING,NONE")
    public void track() throws Exception {
        test("track");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlUnderlined}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void u() throws Exception {
        test("u");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlUnorderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "compact,constructor(),type",
            EDGE = "compact,constructor(),type",
            FF = "compact,constructor(),type",
            FF78 = "compact,constructor(),type",
            IE = "compact,constructor,type")
    @HtmlUnitNYI(CHROME = "constructor(),type",
            EDGE = "constructor(),type",
            FF78 = "constructor(),type",
            FF = "constructor(),type",
            IE = "constructor,type")
    public void ul() throws Exception {
        test("ul");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlVariable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "cite,constructor,dateTime")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,"
                + "onseeking,onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "constructor")
    public void var() throws Exception {
        test("var");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlVideo}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "cancelVideoFrameCallback(),constructor(),disablePictureInPicture,getVideoPlaybackQuality(),"
                + "height,onenterpictureinpicture,onleavepictureinpicture,"
                + "playsInline,poster,requestPictureInPicture(),requestVideoFrameCallback(),"
                + "videoHeight,videoWidth,webkitDecodedFrameCount,"
                + "webkitDisplayingFullscreen,webkitDroppedFrameCount,webkitEnterFullScreen(),"
                + "webkitEnterFullscreen(),webkitExitFullScreen(),webkitExitFullscreen(),webkitSupportsFullscreen,"
                + "width",
            EDGE = "cancelVideoFrameCallback(),constructor(),disablePictureInPicture,getVideoPlaybackQuality(),"
                + "height,onenterpictureinpicture,onleavepictureinpicture,playsInline,poster,"
                + "requestPictureInPicture(),requestVideoFrameCallback(),videoHeight,videoWidth,"
                + "webkitDecodedFrameCount,webkitDisplayingFullscreen,webkitDroppedFrameCount,"
                + "webkitEnterFullScreen(),webkitEnterFullscreen(),webkitExitFullScreen(),webkitExitFullscreen(),"
                + "webkitSupportsFullscreen,"
                + "width",
            FF = "constructor(),getVideoPlaybackQuality(),height,mozDecodedFrames,mozFrameDelay,mozHasAudio,"
                + "mozPaintedFrames,mozParsedFrames,mozPresentedFrames,poster,videoHeight,videoWidth,"
                + "width",
            FF78 = "constructor(),getVideoPlaybackQuality(),height,mozDecodedFrames,mozFrameDelay,mozHasAudio,"
                + "mozPaintedFrames,mozParsedFrames,mozPresentedFrames,poster,videoHeight,videoWidth,"
                + "width",
            IE = "constructor,getVideoPlaybackQuality(),height,msZoom,poster,videoHeight,videoWidth,width")
    @HtmlUnitNYI(CHROME = "constructor(),height,width",
            EDGE = "constructor(),height,width",
            FF78 = "constructor(),height,width",
            FF = "constructor(),height,width",
            IE = "constructor,height,width")
    public void video() throws Exception {
        test("video");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlWordBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),constructor(),"
                + "contentEditable,dataset,dir,draggable,enterKeyHint,focus(),hidden,innerText,inputMode,"
                + "isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                + "onwebkitanimationstart,onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),constructor(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,spellcheck,style,"
                + "tabIndex,"
                + "title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),constructor,contains(),contentEditable,"
                + "createControlRange(),currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),"
                + "getAdjacentText(),getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,"
                + "isMultiLine,isTextEdit,lang,language,mergeAttributes(),msGetInputContext(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,"
                + "onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            FF = "accessKey,blur(),click(),constructor(),contentEditable,dataset,dir,focus(),"
                + "hidden,innerText,isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,"
                + "offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,"
                + "onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,"
                + "spellcheck,style,tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),constructor,contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),"
                + "isContentEditable,lang,language,mergeAttributes(),offsetHeight,offsetLeft,offsetParent,offsetTop,"
                + "offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,onbeforedeactivate,"
                + "onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,"
                + "oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,"
                + "onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,onplay,onplaying,onprogress,"
                + "onratechange,onreset,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,parentElement,releaseCapture(),"
                + "removeNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),style,tabIndex,title,uniqueID")
    public void wbr() throws Exception {
        test("wbr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlExample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),width",
            EDGE = "constructor(),width",
            FF = "constructor(),width",
            FF78 = "constructor(),width",
            IE = "cite,clear,constructor,width")
    @HtmlUnitNYI(IE = "clear,constructor,width")
    public void xmp() throws Exception {
        test("xmp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accept,align,alt,autocomplete,checked,checkValidity(),constructor(),defaultChecked,defaultValue,"
                + "dirName,disabled,files,form,formAction,formEnctype,formMethod,formNoValidate,formTarget,height,"
                + "incremental,indeterminate,labels,list,max,maxLength,min,minLength,multiple,name,pattern,"
                + "placeholder,readOnly,reportValidity(),required,select(),selectionDirection,selectionEnd,"
                + "selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),size,src,step,stepDown(),"
                + "stepUp(),type,useMap,validationMessage,validity,value,valueAsDate,valueAsNumber,webkitdirectory,"
                + "webkitEntries,width,"
                + "willValidate",
            EDGE = "accept,align,alt,autocomplete,checked,checkValidity(),constructor(),defaultChecked,defaultValue,"
                + "dirName,disabled,files,form,formAction,formEnctype,formMethod,formNoValidate,formTarget,height,"
                + "incremental,indeterminate,labels,list,max,maxLength,min,minLength,multiple,name,pattern,"
                + "placeholder,readOnly,reportValidity(),required,select(),selectionDirection,selectionEnd,"
                + "selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),size,src,step,stepDown(),"
                + "stepUp(),type,useMap,validationMessage,validity,value,valueAsDate,valueAsNumber,webkitdirectory,"
                + "webkitEntries,width,"
                + "willValidate",
            FF = "accept,align,alt,autocomplete,autofocus,checked,checkValidity(),constructor(),defaultChecked,"
                + "defaultValue,disabled,files,form,formAction,formEnctype,formMethod,formNoValidate,formTarget,"
                + "height,indeterminate,labels,list,max,maxLength,min,minLength,mozIsTextField(),multiple,name,"
                + "pattern,placeholder,readOnly,reportValidity(),required,select(),selectionDirection,selectionEnd,"
                + "selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),size,src,step,stepDown(),"
                + "stepUp(),textLength,type,useMap,validationMessage,validity,value,valueAsDate,valueAsNumber,"
                + "webkitdirectory,webkitEntries,width,"
                + "willValidate",
            FF78 = "accept,align,alt,autocomplete,autofocus,checked,checkValidity(),constructor(),defaultChecked,"
                + "defaultValue,disabled,files,form,formAction,formEnctype,formMethod,formNoValidate,formTarget,"
                + "height,indeterminate,labels,list,max,maxLength,min,minLength,mozIsTextField(),multiple,name,"
                + "pattern,placeholder,readOnly,reportValidity(),required,select(),selectionDirection,selectionEnd,"
                + "selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),size,src,step,stepDown(),"
                + "stepUp(),textLength,type,useMap,validationMessage,validity,value,valueAsDate,valueAsNumber,"
                + "webkitdirectory,webkitEntries,width,"
                + "willValidate",
            IE = "accept,align,alt,autocomplete,autofocus,border,checked,checkValidity(),complete,constructor,"
                + "createTextRange(),defaultChecked,defaultValue,dynsrc,files,form,formAction,formEnctype,"
                + "formMethod,formNoValidate,formTarget,height,hspace,indeterminate,list,loop,lowsrc,max,maxLength,"
                + "min,multiple,name,pattern,placeholder,readOnly,required,select(),selectionEnd,selectionStart,"
                + "setCustomValidity(),setSelectionRange(),size,src,start,status,step,stepDown(),stepUp(),type,"
                + "useMap,validationMessage,validity,value,valueAsNumber,vrml,vspace,width,"
                + "willValidate")
    @HtmlUnitNYI(CHROME = "accept,align,alt,autocomplete,checked,checkValidity(),constructor(),defaultChecked,"
                + "defaultValue,disabled,files,form,height,labels,max,maxLength,min,minLength,name,placeholder,"
                + "readOnly,required,"
                + "select(),selectionEnd,selectionStart,setSelectionRange(),size,src,step,type,value,width",
            EDGE = "accept,align,alt,autocomplete,checked,checkValidity(),constructor(),defaultChecked,"
                + "defaultValue,disabled,files,form,height,labels,max,maxLength,min,minLength,name,placeholder,"
                + "readOnly,required,"
                + "select(),selectionEnd,selectionStart,setSelectionRange(),size,src,step,type,value,width",
            FF78 = "accept,align,alt,autocomplete,checked,checkValidity(),constructor(),defaultChecked,"
                + "defaultValue,disabled,files,form,height,labels,max,maxLength,min,minLength,name,placeholder,"
                + "readOnly,required,"
                + "select(),selectionEnd,selectionStart,setSelectionRange(),size,src,step,textLength,type,value,width",
            FF = "accept,align,alt,autocomplete,checked,checkValidity(),constructor(),defaultChecked,"
                + "defaultValue,disabled,files,form,height,labels,max,maxLength,min,minLength,name,placeholder,"
                + "readOnly,required,"
                + "select(),selectionEnd,selectionStart,setSelectionRange(),size,src,step,textLength,type,value,width",
            IE = "accept,align,alt,autocomplete,border,checked,checkValidity(),constructor,createTextRange(),"
                + "defaultChecked,defaultValue,files,form,height,max,maxLength,min,name,placeholder,readOnly,required,"
                + "select(),selectionEnd,selectionStart,setSelectionRange(),size,src,step,type,value,width")
    public void input() throws Exception {
        test("input");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),value",
            EDGE = "constructor(),value",
            FF = "constructor(),value",
            FF78 = "constructor(),value",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void data() throws Exception {
        test("data");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlContent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),getDistributedNodes(),select",
            EDGE = "constructor(),getDistributedNodes(),select",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            IE = "constructor")
    public void content() throws Exception {
        test("content");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPicture}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void picutre() throws Exception {
        test("picture");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTemplate}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "constructor(),content",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "constructor")
    public void template() throws Exception {
        test("template");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "altKey,charCode,code,constructor(),ctrlKey,DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,"
                + "DOM_KEY_LOCATION_RIGHT,"
                + "DOM_KEY_LOCATION_STANDARD,getModifierState(),initKeyboardEvent(),isComposing,"
                + "key,keyCode,location,metaKey,repeat,shiftKey",
            EDGE = "altKey,charCode,code,constructor(),ctrlKey,DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,"
                + "DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,getModifierState(),initKeyboardEvent(),"
                + "isComposing,key,keyCode,location,metaKey,repeat,shiftKey",
            FF = {"altKey,charCode,code,constructor(),ctrlKey,DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,"
                + "DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,DOM_VK_0,DOM_VK_1,DOM_VK_2,DOM_VK_3,DOM_VK_4,"
                + "DOM_VK_5,DOM_VK_6,DOM_VK_7,DOM_VK_8,DOM_VK_9,DOM_VK_A,DOM_VK_ACCEPT,DOM_VK_ADD,DOM_VK_ALT,"
                + "DOM_VK_ALTGR,DOM_VK_AMPERSAND,DOM_VK_ASTERISK,DOM_VK_AT,DOM_VK_ATTN,DOM_VK_B,DOM_VK_BACK_QUOTE,"
                + "DOM_VK_BACK_SLASH,DOM_VK_BACK_SPACE,DOM_VK_C,DOM_VK_CANCEL,DOM_VK_CAPS_LOCK,DOM_VK_CIRCUMFLEX,"
                + "DOM_VK_CLEAR,DOM_VK_CLOSE_BRACKET,DOM_VK_CLOSE_CURLY_BRACKET,DOM_VK_CLOSE_PAREN,DOM_VK_COLON,"
                + "DOM_VK_COMMA,DOM_VK_CONTEXT_MENU,DOM_VK_CONTROL,DOM_VK_CONVERT,DOM_VK_CRSEL,DOM_VK_D,"
                + "DOM_VK_DECIMAL,DOM_VK_DELETE,DOM_VK_DIVIDE,DOM_VK_DOLLAR,DOM_VK_DOUBLE_QUOTE,DOM_VK_DOWN,"
                + "DOM_VK_E,DOM_VK_EISU,DOM_VK_END,DOM_VK_EQUALS,DOM_VK_EREOF,DOM_VK_ESCAPE,DOM_VK_EXCLAMATION,"
                + "DOM_VK_EXECUTE,DOM_VK_EXSEL,DOM_VK_F,DOM_VK_F1,DOM_VK_F10,DOM_VK_F11,DOM_VK_F12,DOM_VK_F13,"
                + "DOM_VK_F14,DOM_VK_F15,DOM_VK_F16,DOM_VK_F17,DOM_VK_F18,DOM_VK_F19,DOM_VK_F2,DOM_VK_F20,DOM_VK_F21,"
                + "DOM_VK_F22,DOM_VK_F23,DOM_VK_F24,DOM_VK_F3,DOM_VK_F4,DOM_VK_F5,DOM_VK_F6,DOM_VK_F7,DOM_VK_F8,"
                + "DOM_VK_F9,DOM_VK_FINAL,DOM_VK_G,DOM_VK_GREATER_THAN,DOM_VK_H,DOM_VK_HANGUL,DOM_VK_HANJA,"
                + "DOM_VK_HASH,DOM_VK_HELP,DOM_VK_HOME,DOM_VK_HYPHEN_MINUS,DOM_VK_I,DOM_VK_INSERT,DOM_VK_J,"
                + "DOM_VK_JUNJA,DOM_VK_K,DOM_VK_KANA,DOM_VK_KANJI,DOM_VK_L,DOM_VK_LEFT,DOM_VK_LESS_THAN,DOM_VK_M,"
                + "DOM_VK_META,DOM_VK_MODECHANGE,DOM_VK_MULTIPLY,DOM_VK_N,DOM_VK_NONCONVERT,DOM_VK_NUM_LOCK,"
                + "DOM_VK_NUMPAD0,DOM_VK_NUMPAD1,DOM_VK_NUMPAD2,DOM_VK_NUMPAD3,DOM_VK_NUMPAD4,DOM_VK_NUMPAD5,"
                + "DOM_VK_NUMPAD6,DOM_VK_NUMPAD7,DOM_VK_NUMPAD8,DOM_VK_NUMPAD9,DOM_VK_O,DOM_VK_OPEN_BRACKET,"
                + "DOM_VK_OPEN_CURLY_BRACKET,DOM_VK_OPEN_PAREN,DOM_VK_P,DOM_VK_PA1,DOM_VK_PAGE_DOWN,DOM_VK_PAGE_UP,"
                + "DOM_VK_PAUSE,DOM_VK_PERCENT,DOM_VK_PERIOD,DOM_VK_PIPE,DOM_VK_PLAY,DOM_VK_PLUS,DOM_VK_PRINT,"
                + "DOM_VK_PRINTSCREEN,DOM_VK_PROCESSKEY,DOM_VK_Q,DOM_VK_QUESTION_MARK,DOM_VK_QUOTE,DOM_VK_R,"
                + "DOM_VK_RETURN,DOM_VK_RIGHT,DOM_VK_S,DOM_VK_SCROLL_LOCK,DOM_VK_SELECT",
                "DOM_VK_SEMICOLON,"
                    + "DOM_VK_SEPARATOR,DOM_VK_SHIFT,DOM_VK_SLASH,DOM_VK_SLEEP,DOM_VK_SPACE,DOM_VK_SUBTRACT,DOM_VK_T,"
                    + "DOM_VK_TAB,DOM_VK_TILDE,DOM_VK_U,DOM_VK_UNDERSCORE,DOM_VK_UP,DOM_VK_V,DOM_VK_VOLUME_DOWN,"
                    + "DOM_VK_VOLUME_MUTE,DOM_VK_VOLUME_UP,DOM_VK_W,DOM_VK_WIN,DOM_VK_WIN_ICO_00,DOM_VK_WIN_ICO_CLEAR,"
                    + "DOM_VK_WIN_ICO_HELP,DOM_VK_WIN_OEM_ATTN,DOM_VK_WIN_OEM_AUTO,DOM_VK_WIN_OEM_BACKTAB,"
                    + "DOM_VK_WIN_OEM_CLEAR,DOM_VK_WIN_OEM_COPY,DOM_VK_WIN_OEM_CUSEL,DOM_VK_WIN_OEM_ENLW,"
                    + "DOM_VK_WIN_OEM_FINISH,DOM_VK_WIN_OEM_FJ_JISHO,DOM_VK_WIN_OEM_FJ_LOYA,DOM_VK_WIN_OEM_FJ_MASSHOU,"
                    + "DOM_VK_WIN_OEM_FJ_ROYA,DOM_VK_WIN_OEM_FJ_TOUROKU,DOM_VK_WIN_OEM_JUMP,DOM_VK_WIN_OEM_PA1,"
                    + "DOM_VK_WIN_OEM_PA2,DOM_VK_WIN_OEM_PA3,DOM_VK_WIN_OEM_RESET,DOM_VK_WIN_OEM_WSCTRL,DOM_VK_X,"
                    + "DOM_VK_Y,DOM_VK_Z,DOM_VK_ZOOM,"
                    + "getModifierState(),initKeyboardEvent(),initKeyEvent(),isComposing,"
                    + "key,keyCode,location,metaKey,repeat,shiftKey"},
            FF78 = {"altKey,charCode,code,constructor(),ctrlKey,DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,"
                + "DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,DOM_VK_0,DOM_VK_1,DOM_VK_2,DOM_VK_3,DOM_VK_4,"
                + "DOM_VK_5,DOM_VK_6,DOM_VK_7,DOM_VK_8,DOM_VK_9,DOM_VK_A,DOM_VK_ACCEPT,DOM_VK_ADD,DOM_VK_ALT,"
                + "DOM_VK_ALTGR,DOM_VK_AMPERSAND,DOM_VK_ASTERISK,DOM_VK_AT,DOM_VK_ATTN,DOM_VK_B,DOM_VK_BACK_QUOTE,"
                + "DOM_VK_BACK_SLASH,DOM_VK_BACK_SPACE,DOM_VK_C,DOM_VK_CANCEL,DOM_VK_CAPS_LOCK,DOM_VK_CIRCUMFLEX,"
                + "DOM_VK_CLEAR,DOM_VK_CLOSE_BRACKET,DOM_VK_CLOSE_CURLY_BRACKET,DOM_VK_CLOSE_PAREN,DOM_VK_COLON,"
                + "DOM_VK_COMMA,DOM_VK_CONTEXT_MENU,DOM_VK_CONTROL,DOM_VK_CONVERT,DOM_VK_CRSEL,DOM_VK_D,"
                + "DOM_VK_DECIMAL,DOM_VK_DELETE,DOM_VK_DIVIDE,DOM_VK_DOLLAR,DOM_VK_DOUBLE_QUOTE,DOM_VK_DOWN,"
                + "DOM_VK_E,DOM_VK_EISU,DOM_VK_END,DOM_VK_EQUALS,DOM_VK_EREOF,DOM_VK_ESCAPE,DOM_VK_EXCLAMATION,"
                + "DOM_VK_EXECUTE,DOM_VK_EXSEL,DOM_VK_F,DOM_VK_F1,DOM_VK_F10,DOM_VK_F11,DOM_VK_F12,DOM_VK_F13,"
                + "DOM_VK_F14,DOM_VK_F15,DOM_VK_F16,DOM_VK_F17,DOM_VK_F18,DOM_VK_F19,DOM_VK_F2,DOM_VK_F20,DOM_VK_F21,"
                + "DOM_VK_F22,DOM_VK_F23,DOM_VK_F24,DOM_VK_F3,DOM_VK_F4,DOM_VK_F5,DOM_VK_F6,DOM_VK_F7,DOM_VK_F8,"
                + "DOM_VK_F9,DOM_VK_FINAL,DOM_VK_G,DOM_VK_GREATER_THAN,DOM_VK_H,DOM_VK_HANGUL,DOM_VK_HANJA,"
                + "DOM_VK_HASH,DOM_VK_HELP,DOM_VK_HOME,DOM_VK_HYPHEN_MINUS,DOM_VK_I,DOM_VK_INSERT,DOM_VK_J,"
                + "DOM_VK_JUNJA,DOM_VK_K,DOM_VK_KANA,DOM_VK_KANJI,DOM_VK_L,DOM_VK_LEFT,DOM_VK_LESS_THAN,DOM_VK_M,"
                + "DOM_VK_META,DOM_VK_MODECHANGE,DOM_VK_MULTIPLY,DOM_VK_N,DOM_VK_NONCONVERT,DOM_VK_NUM_LOCK,"
                + "DOM_VK_NUMPAD0,DOM_VK_NUMPAD1,DOM_VK_NUMPAD2,DOM_VK_NUMPAD3,DOM_VK_NUMPAD4,DOM_VK_NUMPAD5,"
                + "DOM_VK_NUMPAD6,DOM_VK_NUMPAD7,DOM_VK_NUMPAD8,DOM_VK_NUMPAD9,DOM_VK_O,DOM_VK_OPEN_BRACKET,"
                + "DOM_VK_OPEN_CURLY_BRACKET,DOM_VK_OPEN_PAREN,DOM_VK_P,DOM_VK_PA1,DOM_VK_PAGE_DOWN,DOM_VK_PAGE_UP,"
                + "DOM_VK_PAUSE,DOM_VK_PERCENT,DOM_VK_PERIOD,DOM_VK_PIPE,DOM_VK_PLAY,DOM_VK_PLUS,DOM_VK_PRINT,"
                + "DOM_VK_PRINTSCREEN,DOM_VK_PROCESSKEY,DOM_VK_Q,DOM_VK_QUESTION_MARK,DOM_VK_QUOTE,DOM_VK_R,"
                + "DOM_VK_RETURN,DOM_VK_RIGHT,DOM_VK_S,DOM_VK_SCROLL_LOCK,DOM_VK_SELECT",
                "DOM_VK_SEMICOLON,"
                    + "DOM_VK_SEPARATOR,DOM_VK_SHIFT,DOM_VK_SLASH,DOM_VK_SLEEP,DOM_VK_SPACE,DOM_VK_SUBTRACT,DOM_VK_T,"
                    + "DOM_VK_TAB,DOM_VK_TILDE,DOM_VK_U,DOM_VK_UNDERSCORE,DOM_VK_UP,DOM_VK_V,DOM_VK_VOLUME_DOWN,"
                    + "DOM_VK_VOLUME_MUTE,DOM_VK_VOLUME_UP,DOM_VK_W,DOM_VK_WIN,DOM_VK_WIN_ICO_00,DOM_VK_WIN_ICO_CLEAR,"
                    + "DOM_VK_WIN_ICO_HELP,DOM_VK_WIN_OEM_ATTN,DOM_VK_WIN_OEM_AUTO,DOM_VK_WIN_OEM_BACKTAB,"
                    + "DOM_VK_WIN_OEM_CLEAR,DOM_VK_WIN_OEM_COPY,DOM_VK_WIN_OEM_CUSEL,DOM_VK_WIN_OEM_ENLW,"
                    + "DOM_VK_WIN_OEM_FINISH,DOM_VK_WIN_OEM_FJ_JISHO,DOM_VK_WIN_OEM_FJ_LOYA,DOM_VK_WIN_OEM_FJ_MASSHOU,"
                    + "DOM_VK_WIN_OEM_FJ_ROYA,DOM_VK_WIN_OEM_FJ_TOUROKU,DOM_VK_WIN_OEM_JUMP,DOM_VK_WIN_OEM_PA1,"
                    + "DOM_VK_WIN_OEM_PA2,DOM_VK_WIN_OEM_PA3,DOM_VK_WIN_OEM_RESET,DOM_VK_WIN_OEM_WSCTRL,DOM_VK_X,"
                    + "DOM_VK_Y,DOM_VK_Z,DOM_VK_ZOOM,"
                    + "getModifierState(),initKeyboardEvent(),initKeyEvent(),isComposing,"
                    + "key,keyCode,location,metaKey,repeat,shiftKey"},
            IE = "altKey,char,charCode,constructor,ctrlKey,DOM_KEY_LOCATION_JOYSTICK,DOM_KEY_LOCATION_LEFT,"
                + "DOM_KEY_LOCATION_MOBILE,DOM_KEY_LOCATION_NUMPAD,DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,"
                + "getModifierState(),initKeyboardEvent(),key,keyCode,locale,location,metaKey,repeat,shiftKey,which")
    @HtmlUnitNYI(CHROME = "altKey,charCode,code,constructor(),ctrlKey,DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,"
                        + "DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,"
                        + "isComposing,key,keyCode,location,metaKey,repeat,shiftKey,which",
                EDGE = "altKey,charCode,code,constructor(),ctrlKey,DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,"
                        + "DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,"
                        + "isComposing,key,keyCode,location,metaKey,repeat,shiftKey,which",
                FF78 = {"altKey,charCode,code,constructor(),ctrlKey,DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,"
                    + "DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,DOM_VK_0,DOM_VK_1,DOM_VK_2,DOM_VK_3,DOM_VK_4,"
                    + "DOM_VK_5,DOM_VK_6,DOM_VK_7,DOM_VK_8,DOM_VK_9,DOM_VK_A,DOM_VK_ACCEPT,DOM_VK_ADD,DOM_VK_ALT,"
                    + "DOM_VK_ALTGR,DOM_VK_AMPERSAND,DOM_VK_ASTERISK,DOM_VK_AT,DOM_VK_ATTN,DOM_VK_B,DOM_VK_BACK_QUOTE,"
                    + "DOM_VK_BACK_SLASH,DOM_VK_BACK_SPACE,DOM_VK_C,DOM_VK_CANCEL,DOM_VK_CAPS_LOCK,DOM_VK_CIRCUMFLEX,"
                    + "DOM_VK_CLEAR,DOM_VK_CLOSE_BRACKET,DOM_VK_CLOSE_CURLY_BRACKET,DOM_VK_CLOSE_PAREN,DOM_VK_COLON,"
                    + "DOM_VK_COMMA,DOM_VK_CONTEXT_MENU,DOM_VK_CONTROL,DOM_VK_CONVERT,DOM_VK_CRSEL,DOM_VK_D,"
                    + "DOM_VK_DECIMAL,DOM_VK_DELETE,DOM_VK_DIVIDE,DOM_VK_DOLLAR,DOM_VK_DOUBLE_QUOTE,DOM_VK_DOWN,"
                    + "DOM_VK_E,DOM_VK_EISU,DOM_VK_END,DOM_VK_EQUALS,DOM_VK_EREOF,DOM_VK_ESCAPE,DOM_VK_EXCLAMATION,"
                    + "DOM_VK_EXECUTE,DOM_VK_EXSEL,DOM_VK_F,DOM_VK_F1,DOM_VK_F10,DOM_VK_F11,DOM_VK_F12,DOM_VK_F13,"
                    + "DOM_VK_F14,DOM_VK_F15,DOM_VK_F16,DOM_VK_F17,DOM_VK_F18,DOM_VK_F19,DOM_VK_F2,"
                    + "DOM_VK_F20,DOM_VK_F21,"
                    + "DOM_VK_F22,DOM_VK_F23,DOM_VK_F24,DOM_VK_F3,DOM_VK_F4,DOM_VK_F5,DOM_VK_F6,DOM_VK_F7,DOM_VK_F8,"
                    + "DOM_VK_F9,DOM_VK_FINAL,DOM_VK_G,DOM_VK_GREATER_THAN,DOM_VK_H,DOM_VK_HANGUL,DOM_VK_HANJA,"
                    + "DOM_VK_HASH,DOM_VK_HELP,DOM_VK_HOME,DOM_VK_HYPHEN_MINUS,DOM_VK_I,DOM_VK_INSERT,DOM_VK_J,"
                    + "DOM_VK_JUNJA,DOM_VK_K,DOM_VK_KANA,DOM_VK_KANJI,DOM_VK_L,DOM_VK_LEFT,DOM_VK_LESS_THAN,DOM_VK_M,"
                    + "DOM_VK_META,DOM_VK_MODECHANGE,DOM_VK_MULTIPLY,DOM_VK_N,DOM_VK_NONCONVERT,DOM_VK_NUM_LOCK,"
                    + "DOM_VK_NUMPAD0,DOM_VK_NUMPAD1,DOM_VK_NUMPAD2,DOM_VK_NUMPAD3,DOM_VK_NUMPAD4,DOM_VK_NUMPAD5,"
                    + "DOM_VK_NUMPAD6,DOM_VK_NUMPAD7,DOM_VK_NUMPAD8,DOM_VK_NUMPAD9,DOM_VK_O,DOM_VK_OPEN_BRACKET,"
                    + "DOM_VK_OPEN_CURLY_BRACKET,DOM_VK_OPEN_PAREN,DOM_VK_P,DOM_VK_PA1,DOM_VK_PAGE_DOWN,DOM_VK_PAGE_UP,"
                    + "DOM_VK_PAUSE,DOM_VK_PERCENT,DOM_VK_PERIOD,DOM_VK_PIPE,DOM_VK_PLAY,DOM_VK_PLUS,DOM_VK_PRINT,"
                    + "DOM_VK_PRINTSCREEN,DOM_VK_PROCESSKEY,DOM_VK_Q,DOM_VK_QUESTION_MARK,DOM_VK_QUOTE,DOM_VK_R,"
                    + "DOM_VK_RETURN,DOM_VK_RIGHT,DOM_VK_S,DOM_VK_SCROLL_LOCK,DOM_VK_SELECT",
                    "DOM_VK_SEMICOLON,"
                        + "DOM_VK_SEPARATOR,DOM_VK_SHIFT,DOM_VK_SLASH,DOM_VK_SLEEP,DOM_VK_SPACE,DOM_VK_SUBTRACT,"
                        + "DOM_VK_T,DOM_VK_TAB,DOM_VK_TILDE,DOM_VK_U,DOM_VK_UNDERSCORE,DOM_VK_UP,DOM_VK_V,"
                        + "DOM_VK_VOLUME_DOWN,"
                        + "DOM_VK_VOLUME_MUTE,DOM_VK_VOLUME_UP,DOM_VK_W,DOM_VK_WIN,DOM_VK_WIN_ICO_00,"
                        + "DOM_VK_WIN_ICO_CLEAR,"
                        + "DOM_VK_WIN_ICO_HELP,DOM_VK_WIN_OEM_ATTN,DOM_VK_WIN_OEM_AUTO,DOM_VK_WIN_OEM_BACKTAB,"
                        + "DOM_VK_WIN_OEM_CLEAR,DOM_VK_WIN_OEM_COPY,DOM_VK_WIN_OEM_CUSEL,DOM_VK_WIN_OEM_ENLW,"
                        + "DOM_VK_WIN_OEM_FINISH,DOM_VK_WIN_OEM_FJ_JISHO,DOM_VK_WIN_OEM_FJ_LOYA,"
                        + "DOM_VK_WIN_OEM_FJ_MASSHOU,"
                        + "DOM_VK_WIN_OEM_FJ_ROYA,DOM_VK_WIN_OEM_FJ_TOUROKU,DOM_VK_WIN_OEM_JUMP,DOM_VK_WIN_OEM_PA1,"
                        + "DOM_VK_WIN_OEM_PA2,DOM_VK_WIN_OEM_PA3,DOM_VK_WIN_OEM_RESET,DOM_VK_WIN_OEM_WSCTRL,DOM_VK_X,"
                        + "DOM_VK_Y,DOM_VK_Z,DOM_VK_ZOOM,"
                        + "initKeyEvent(),isComposing,key,keyCode,location,metaKey,repeat,shiftKey,which"},
                FF = {"altKey,charCode,code,constructor(),ctrlKey,DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,"
                    + "DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,DOM_VK_0,DOM_VK_1,DOM_VK_2,DOM_VK_3,DOM_VK_4,"
                    + "DOM_VK_5,DOM_VK_6,DOM_VK_7,DOM_VK_8,DOM_VK_9,DOM_VK_A,DOM_VK_ACCEPT,DOM_VK_ADD,DOM_VK_ALT,"
                    + "DOM_VK_ALTGR,DOM_VK_AMPERSAND,DOM_VK_ASTERISK,DOM_VK_AT,DOM_VK_ATTN,DOM_VK_B,DOM_VK_BACK_QUOTE,"
                    + "DOM_VK_BACK_SLASH,DOM_VK_BACK_SPACE,DOM_VK_C,DOM_VK_CANCEL,DOM_VK_CAPS_LOCK,DOM_VK_CIRCUMFLEX,"
                    + "DOM_VK_CLEAR,DOM_VK_CLOSE_BRACKET,DOM_VK_CLOSE_CURLY_BRACKET,DOM_VK_CLOSE_PAREN,DOM_VK_COLON,"
                    + "DOM_VK_COMMA,DOM_VK_CONTEXT_MENU,DOM_VK_CONTROL,DOM_VK_CONVERT,DOM_VK_CRSEL,DOM_VK_D,"
                    + "DOM_VK_DECIMAL,DOM_VK_DELETE,DOM_VK_DIVIDE,DOM_VK_DOLLAR,DOM_VK_DOUBLE_QUOTE,DOM_VK_DOWN,"
                    + "DOM_VK_E,DOM_VK_EISU,DOM_VK_END,DOM_VK_EQUALS,DOM_VK_EREOF,DOM_VK_ESCAPE,DOM_VK_EXCLAMATION,"
                    + "DOM_VK_EXECUTE,DOM_VK_EXSEL,DOM_VK_F,DOM_VK_F1,DOM_VK_F10,DOM_VK_F11,DOM_VK_F12,DOM_VK_F13,"
                    + "DOM_VK_F14,DOM_VK_F15,DOM_VK_F16,DOM_VK_F17,DOM_VK_F18,DOM_VK_F19,DOM_VK_F2,"
                    + "DOM_VK_F20,DOM_VK_F21,"
                    + "DOM_VK_F22,DOM_VK_F23,DOM_VK_F24,DOM_VK_F3,DOM_VK_F4,DOM_VK_F5,DOM_VK_F6,DOM_VK_F7,DOM_VK_F8,"
                    + "DOM_VK_F9,DOM_VK_FINAL,DOM_VK_G,DOM_VK_GREATER_THAN,DOM_VK_H,DOM_VK_HANGUL,DOM_VK_HANJA,"
                    + "DOM_VK_HASH,DOM_VK_HELP,DOM_VK_HOME,DOM_VK_HYPHEN_MINUS,DOM_VK_I,DOM_VK_INSERT,DOM_VK_J,"
                    + "DOM_VK_JUNJA,DOM_VK_K,DOM_VK_KANA,DOM_VK_KANJI,DOM_VK_L,DOM_VK_LEFT,DOM_VK_LESS_THAN,DOM_VK_M,"
                    + "DOM_VK_META,DOM_VK_MODECHANGE,DOM_VK_MULTIPLY,DOM_VK_N,DOM_VK_NONCONVERT,DOM_VK_NUM_LOCK,"
                    + "DOM_VK_NUMPAD0,DOM_VK_NUMPAD1,DOM_VK_NUMPAD2,DOM_VK_NUMPAD3,DOM_VK_NUMPAD4,DOM_VK_NUMPAD5,"
                    + "DOM_VK_NUMPAD6,DOM_VK_NUMPAD7,DOM_VK_NUMPAD8,DOM_VK_NUMPAD9,DOM_VK_O,DOM_VK_OPEN_BRACKET,"
                    + "DOM_VK_OPEN_CURLY_BRACKET,DOM_VK_OPEN_PAREN,DOM_VK_P,DOM_VK_PA1,DOM_VK_PAGE_DOWN,DOM_VK_PAGE_UP,"
                    + "DOM_VK_PAUSE,DOM_VK_PERCENT,DOM_VK_PERIOD,DOM_VK_PIPE,DOM_VK_PLAY,DOM_VK_PLUS,DOM_VK_PRINT,"
                    + "DOM_VK_PRINTSCREEN,DOM_VK_PROCESSKEY,DOM_VK_Q,DOM_VK_QUESTION_MARK,DOM_VK_QUOTE,DOM_VK_R,"
                    + "DOM_VK_RETURN,DOM_VK_RIGHT,DOM_VK_S,DOM_VK_SCROLL_LOCK,DOM_VK_SELECT",
                    "DOM_VK_SEMICOLON,"
                        + "DOM_VK_SEPARATOR,DOM_VK_SHIFT,DOM_VK_SLASH,DOM_VK_SLEEP,DOM_VK_SPACE,DOM_VK_SUBTRACT,"
                        + "DOM_VK_T,DOM_VK_TAB,DOM_VK_TILDE,DOM_VK_U,DOM_VK_UNDERSCORE,DOM_VK_UP,DOM_VK_V,"
                        + "DOM_VK_VOLUME_DOWN,"
                        + "DOM_VK_VOLUME_MUTE,DOM_VK_VOLUME_UP,DOM_VK_W,DOM_VK_WIN,DOM_VK_WIN_ICO_00,"
                        + "DOM_VK_WIN_ICO_CLEAR,"
                        + "DOM_VK_WIN_ICO_HELP,DOM_VK_WIN_OEM_ATTN,DOM_VK_WIN_OEM_AUTO,DOM_VK_WIN_OEM_BACKTAB,"
                        + "DOM_VK_WIN_OEM_CLEAR,DOM_VK_WIN_OEM_COPY,DOM_VK_WIN_OEM_CUSEL,DOM_VK_WIN_OEM_ENLW,"
                        + "DOM_VK_WIN_OEM_FINISH,DOM_VK_WIN_OEM_FJ_JISHO,DOM_VK_WIN_OEM_FJ_LOYA,"
                        + "DOM_VK_WIN_OEM_FJ_MASSHOU,"
                        + "DOM_VK_WIN_OEM_FJ_ROYA,DOM_VK_WIN_OEM_FJ_TOUROKU,DOM_VK_WIN_OEM_JUMP,DOM_VK_WIN_OEM_PA1,"
                        + "DOM_VK_WIN_OEM_PA2,DOM_VK_WIN_OEM_PA3,DOM_VK_WIN_OEM_RESET,DOM_VK_WIN_OEM_WSCTRL,DOM_VK_X,"
                        + "DOM_VK_Y,DOM_VK_Z,DOM_VK_ZOOM,"
                        + "initKeyEvent(),isComposing,key,keyCode,location,metaKey,repeat,shiftKey,which"},
                IE = "altKey,char,charCode,constructor,ctrlKey,DOM_KEY_LOCATION_JOYSTICK,DOM_KEY_LOCATION_LEFT,"
                    + "DOM_KEY_LOCATION_MOBILE,DOM_KEY_LOCATION_NUMPAD,DOM_KEY_LOCATION_RIGHT,"
                    + "DOM_KEY_LOCATION_STANDARD,key,keyCode,location,metaKey,repeat,shiftKey,which")
    public void keyboardEvent() throws Exception {
        testString("", "document.createEvent('KeyboardEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),detail,initUIEvent(),sourceCapabilities,view,which",
            EDGE = "constructor(),detail,initUIEvent(),sourceCapabilities,view,which",
            FF = "constructor(),detail,initUIEvent(),layerX,layerY,rangeOffset,rangeParent,SCROLL_PAGE_DOWN,"
                + "SCROLL_PAGE_UP,view,"
                + "which",
            FF78 = "constructor(),detail,initUIEvent(),layerX,layerY,rangeOffset,rangeParent,SCROLL_PAGE_DOWN,"
                + "SCROLL_PAGE_UP,view,"
                + "which",
            IE = "constructor,detail,deviceSessionId,initUIEvent(),view")
    @HtmlUnitNYI(CHROME = "constructor(),detail,initUIEvent(),view",
            EDGE = "constructor(),detail,initUIEvent(),view",
            FF78 = "constructor(),detail,initUIEvent(),SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,view",
            FF = "constructor(),detail,initUIEvent(),SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,view",
            IE = "constructor,detail,initUIEvent(),view")
    public void uiEvent() throws Exception {
        testString("", "document.createEvent('UIEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.DragEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),dataTransfer",
            EDGE = "constructor(),dataTransfer",
            FF = "constructor(),dataTransfer,initDragEvent()",
            FF78 = "constructor(),dataTransfer,initDragEvent()",
            IE = "constructor,dataTransfer,initDragEvent(),msConvertURL()")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor()",
            FF = "constructor()",
            IE = "constructor")
    public void dragEvent() throws Exception {
        testString("", "document.createEvent('DragEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "altitudeAngle,azimuthAngle,constructor(),getCoalescedEvents(),getPredictedEvents(),height,"
                + "isPrimary,pointerId,pointerType,pressure,"
                + "tangentialPressure,tiltX,tiltY,twist,width",
            EDGE = "altitudeAngle,azimuthAngle,constructor(),getCoalescedEvents(),"
                + "getPredictedEvents(),height,isPrimary,pointerId,pointerType,"
                + "pressure,tangentialPressure,tiltX,tiltY,twist,"
                + "width",
            FF = "constructor(),getCoalescedEvents(),height,isPrimary,pointerId,pointerType,pressure,"
                + "tangentialPressure,tiltX,tiltY,twist,width",
            FF78 = "constructor(),getCoalescedEvents(),height,isPrimary,pointerId,pointerType,pressure,"
                + "tangentialPressure,tiltX,tiltY,twist,width",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "altitudeAngle,azimuthAngle,constructor(),height,isPrimary,pointerId,pointerType,"
                + "pressure,tiltX,tiltY,width",
            EDGE = "altitudeAngle,azimuthAngle,constructor(),height,isPrimary,pointerId,pointerType,"
                + "pressure,tiltX,tiltY,width",
            FF78 = "constructor(),height,isPrimary,pointerId,pointerType,pressure,"
                + "tiltX,tiltY,width",
            FF = "constructor(),height,isPrimary,pointerId,pointerType,pressure,"
                + "tiltX,tiltY,width")
    public void pointerEvent() throws Exception {
        testString("", "new PointerEvent('click')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "exception",
            EDGE = "exception",
            FF = "exception",
            FF78 = "exception",
            IE = "constructor,height,hwTimestamp,initPointerEvent(),isPrimary,pointerId,pointerType,pressure,"
                + "rotation,tiltX,tiltY,"
                + "width")
    @HtmlUnitNYI(IE = "constructor,height,initPointerEvent(),isPrimary,pointerId,pointerType,pressure,"
                + "tiltX,tiltY,width")
    public void pointerEvent2() throws Exception {
        testString("", " document.createEvent('PointerEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.WheelEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),deltaMode,deltaX,deltaY,deltaZ,DOM_DELTA_LINE,DOM_DELTA_PAGE,DOM_DELTA_PIXEL,"
                + "wheelDelta,wheelDeltaX,"
                + "wheelDeltaY",
            EDGE = "constructor(),deltaMode,deltaX,deltaY,deltaZ,DOM_DELTA_LINE,DOM_DELTA_PAGE,DOM_DELTA_PIXEL,"
                + "wheelDelta,wheelDeltaX,"
                + "wheelDeltaY",
            FF = "exception",
            FF78 = "exception",
            IE = "constructor,deltaMode,deltaX,deltaY,deltaZ,DOM_DELTA_LINE,DOM_DELTA_PAGE,DOM_DELTA_PIXEL,"
                + "initWheelEvent()")
    @HtmlUnitNYI(CHROME = "constructor(),DOM_DELTA_LINE,DOM_DELTA_PAGE,DOM_DELTA_PIXEL",
            EDGE = "constructor(),DOM_DELTA_LINE,DOM_DELTA_PAGE,DOM_DELTA_PIXEL",
            IE = "constructor,DOM_DELTA_LINE,DOM_DELTA_PAGE,DOM_DELTA_PIXEL")
    public void wheelEvent() throws Exception {
        testString("", "document.createEvent('WheelEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "altKey,button,buttons,clientX,clientY,constructor(),ctrlKey,fromElement,getModifierState(),"
                + "initMouseEvent(),layerX,layerY,metaKey,movementX,movementY,offsetX,offsetY,pageX,pageY,"
                + "relatedTarget,screenX,screenY,shiftKey,toElement,x,y",
            EDGE = "altKey,button,buttons,clientX,clientY,constructor(),ctrlKey,fromElement,getModifierState(),"
                + "initMouseEvent(),layerX,layerY,metaKey,movementX,movementY,offsetX,offsetY,pageX,pageY,"
                + "relatedTarget,screenX,screenY,shiftKey,toElement,x,"
                + "y",
            FF = "altKey,button,buttons,clientX,clientY,constructor(),ctrlKey,getModifierState(),initMouseEvent(),"
                + "initNSMouseEvent(),metaKey,movementX,movementY,MOZ_SOURCE_CURSOR,MOZ_SOURCE_ERASER,"
                + "MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,MOZ_SOURCE_PEN,MOZ_SOURCE_TOUCH,MOZ_SOURCE_UNKNOWN,"
                + "mozInputSource,mozPressure,offsetX,offsetY,pageX,pageY,region,relatedTarget,screenX,screenY,"
                + "shiftKey,x,y",
            FF78 = "altKey,button,buttons,clientX,clientY,constructor(),ctrlKey,getModifierState(),initMouseEvent(),"
                + "initNSMouseEvent(),metaKey,movementX,movementY,MOZ_SOURCE_CURSOR,MOZ_SOURCE_ERASER,"
                + "MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,MOZ_SOURCE_PEN,MOZ_SOURCE_TOUCH,MOZ_SOURCE_UNKNOWN,"
                + "mozInputSource,mozPressure,offsetX,offsetY,pageX,pageY,region,relatedTarget,screenX,screenY,"
                + "shiftKey,x,"
                + "y",
            IE = "altKey,button,buttons,clientX,clientY,constructor,ctrlKey,fromElement,getModifierState(),"
                + "initMouseEvent(),layerX,layerY,metaKey,offsetX,offsetY,pageX,pageY,relatedTarget,screenX,screenY,"
                + "shiftKey,toElement,which,x,y")
    @HtmlUnitNYI(CHROME = "altKey,button,buttons,clientX,clientY,constructor(),ctrlKey,initMouseEvent(),metaKey,"
                + "pageX,pageY,screenX,screenY,shiftKey,which",
            EDGE = "altKey,button,buttons,clientX,clientY,constructor(),ctrlKey,initMouseEvent(),metaKey,"
                + "pageX,pageY,screenX,screenY,shiftKey,which",
            FF78 = "altKey,button,buttons,clientX,clientY,constructor(),ctrlKey,initMouseEvent(),"
                + "metaKey,MOZ_SOURCE_CURSOR,"
                + "MOZ_SOURCE_ERASER,MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,MOZ_SOURCE_PEN,MOZ_SOURCE_TOUCH,"
                + "MOZ_SOURCE_UNKNOWN,pageX,pageY,screenX,screenY,shiftKey,which",
            FF = "altKey,button,buttons,clientX,clientY,constructor(),ctrlKey,initMouseEvent(),"
                + "metaKey,MOZ_SOURCE_CURSOR,"
                + "MOZ_SOURCE_ERASER,MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,MOZ_SOURCE_PEN,MOZ_SOURCE_TOUCH,"
                + "MOZ_SOURCE_UNKNOWN,pageX,pageY,screenX,screenY,shiftKey,which",
            IE = "altKey,button,buttons,clientX,clientY,constructor,ctrlKey,initMouseEvent(),metaKey,pageX,pageY,"
                + "screenX,screenY,shiftKey,which")
    public void mouseEvent() throws Exception {
        testString("", "document.createEvent('MouseEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.CompositionEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),data,initCompositionEvent()",
            EDGE = "constructor(),data,initCompositionEvent()",
            FF = "constructor(),data,initCompositionEvent(),locale",
            FF78 = "constructor(),data,initCompositionEvent(),locale",
            IE = "constructor,data,initCompositionEvent(),locale")
    @HtmlUnitNYI(CHROME = "constructor(),data",
            EDGE = "constructor(),data",
            FF78 = "constructor(),data",
            FF = "constructor(),data",
            IE = "constructor,data")
    public void compositionEvent() throws Exception {
        testString("", "document.createEvent('CompositionEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.FocusEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),relatedTarget",
            EDGE = "constructor(),relatedTarget",
            FF = "constructor(),relatedTarget",
            FF78 = "constructor(),relatedTarget",
            IE = "constructor,initFocusEvent(),relatedTarget")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor()",
            FF = "constructor()",
            IE = "constructor")
    public void focusEvent() throws Exception {
        testString("", "document.createEvent('FocusEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.InputEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),data,dataTransfer,getTargetRanges(),inputType,isComposing",
            EDGE = "constructor(),data,dataTransfer,getTargetRanges(),inputType,isComposing",
            FF = "constructor(),data,dataTransfer,inputType,isComposing",
            FF78 = "constructor(),data,dataTransfer,inputType,isComposing",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "constructor(),data,inputType,isComposing",
            EDGE = "constructor(),data,inputType,isComposing",
            FF78 = "constructor(),data,inputType,isComposing",
            FF = "constructor(),data,inputType,isComposing")
    public void inputEvent() throws Exception {
        testString("", "new InputEvent('input')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.MouseWheelEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "exception",
            EDGE = "exception",
            FF = "exception",
            FF78 = "exception",
            IE = "constructor,initMouseWheelEvent(),wheelDelta")
    @HtmlUnitNYI(IE = "constructor")
    public void mouseWheelEvent() throws Exception {
        testString("", "document.createEvent('MouseWheelEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.SVGZoomEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgZoomEvent() throws Exception {
        testString("", "document.createEvent('SVGZoomEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.TextEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),data,initTextEvent()",
            EDGE = "constructor(),data,initTextEvent()",
            FF = "constructor(),data,initCompositionEvent(),locale",
            FF78 = "constructor(),data,initCompositionEvent(),locale",
            IE = "constructor,data,DOM_INPUT_METHOD_DROP,DOM_INPUT_METHOD_HANDWRITING,DOM_INPUT_METHOD_IME,"
                + "DOM_INPUT_METHOD_KEYBOARD,DOM_INPUT_METHOD_MULTIMODAL,DOM_INPUT_METHOD_OPTION,"
                + "DOM_INPUT_METHOD_PASTE,DOM_INPUT_METHOD_SCRIPT,DOM_INPUT_METHOD_UNKNOWN,DOM_INPUT_METHOD_VOICE,"
                + "initTextEvent(),inputMethod,"
                + "locale")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor(),detail,initUIEvent(),SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,view",
            FF = "constructor(),detail,initUIEvent(),SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,view",
            IE = "constructor,DOM_INPUT_METHOD_DROP,DOM_INPUT_METHOD_HANDWRITING,DOM_INPUT_METHOD_IME,"
                + "DOM_INPUT_METHOD_KEYBOARD,DOM_INPUT_METHOD_MULTIMODAL,DOM_INPUT_METHOD_OPTION,"
                + "DOM_INPUT_METHOD_PASTE,DOM_INPUT_METHOD_SCRIPT,DOM_INPUT_METHOD_UNKNOWN,DOM_INPUT_METHOD_VOICE")
    public void textEvent() throws Exception {
        testString("", "document.createEvent('TextEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.TouchEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "altKey,changedTouches,constructor(),ctrlKey,metaKey,shiftKey,targetTouches,touches",
            EDGE = "altKey,changedTouches,constructor(),ctrlKey,metaKey,shiftKey,targetTouches,touches",
            FF = "exception",
            FF78 = "exception",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()")
    public void touchEvent2() throws Exception {
        testString("", "new TouchEvent('touch')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSlot}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "assign(),assignedElements(),assignedNodes(),constructor(),name",
            EDGE = "assign(),assignedElements(),assignedNodes(),constructor(),name",
            FF = "assignedElements(),assignedNodes(),constructor(),name",
            FF78 = "assignedElements(),assignedNodes(),constructor(),name",
            IE = "constructor,namedRecordset(),recordset")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor()",
            FF = "constructor()",
            IE = "constructor")
    public void slot() throws Exception {
        test("slot");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor")
    @HtmlUnitNYI(CHROME = "constructor(),cookie,dispatchEvent(),documentElement,getElementById(),"
                + "getSelection(),head,open(),write(),writeln()",
            EDGE = "constructor(),cookie,dispatchEvent(),documentElement,getElementById(),"
                + "getSelection(),head,open(),write(),writeln()",
            FF78 = "close(),constructor(),cookie,dispatchEvent(),documentElement,getElementById(),"
                + "getElementsByName(),getSelection(),head,open(),write(),writeln()",
            FF = "close(),constructor(),cookie,dispatchEvent(),documentElement,getElementById(),"
                + "getElementsByName(),getSelection(),head,open(),write(),writeln()",
            IE = "constructor,cookie,dispatchEvent(),documentElement,getElementById(),getSelection(),"
                + "head,open(),write(),writeln()")
    public void htmlDocument() throws Exception {
        testString("", "document");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Document}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()",
            IE = "constructor")
    @HtmlUnitNYI(CHROME = "constructor(),getElementsByTagName()",
            EDGE = "constructor(),getElementsByTagName()",
            FF78 = "constructor(),getElementsByTagName()",
            FF = "constructor(),getElementsByTagName()",
            IE = "constructor,getElementsByTagName()")
    public void document() throws Exception {
        testString("", "xmlDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "autofocus,blur(),className,constructor(),dataset,focus(),nonce,onabort,onanimationend,"
                + "onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,"
                + "onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,"
                + "ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,"
                + "onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,"
                + "onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                + "onwebkittransitionend,onwheel,ownerSVGElement,style,tabIndex,"
                + "viewportElement",
            EDGE = "autofocus,blur(),className,constructor(),dataset,focus(),nonce,onabort,onanimationend,"
                + "onanimationiteration,onanimationstart,onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,"
                + "onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,"
                + "ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,"
                + "onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,"
                + "onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                + "onwebkittransitionend,onwheel,ownerSVGElement,style,tabIndex,"
                + "viewportElement",
            FF = "blur(),className,constructor(),dataset,focus(),id,nonce,onabort,onanimationcancel,onanimationend,"
                + "onanimationiteration,onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,"
                + "ondragexit,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,"
                + "onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,ownerSVGElement,"
                + "style,tabIndex,"
                + "viewportElement",
            FF78 = "blur(),className,constructor(),dataset,focus(),id,nonce,onabort,onanimationcancel,onanimationend,"
                + "onanimationiteration,onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,"
                + "ondragexit,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,"
                + "onfocus,onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectstart,onshow,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,ownerSVGElement,"
                + "style,tabIndex,"
                + "viewportElement",
            IE = "childElementCount,clientHeight,clientLeft,clientTop,clientWidth,constructor,firstElementChild,"
                + "getAttribute(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),"
                + "getClientRects(),getElementsByTagName(),getElementsByTagNameNS(),hasAttributeNS(),"
                + "lastElementChild,msContentZoomFactor,msGetRegionContent(),msGetUntransformedBounds(),"
                + "msMatchesSelector(),msRegionOverflow,msReleasePointerCapture(),msRequestFullscreen(),"
                + "msSetPointerCapture(),msZoomTo(),nextElementSibling,ongotpointercapture,onlostpointercapture,"
                + "onmsgesturechange,onmsgesturedoubletap,onmsgestureend,onmsgesturehold,onmsgesturestart,"
                + "onmsgesturetap,onmsgotpointercapture,onmsinertiastart,onmslostpointercapture,onmspointercancel,"
                + "onmspointerdown,onmspointerenter,onmspointerleave,onmspointermove,onmspointerout,onmspointerover,"
                + "onmspointerup,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,previousElementSibling,querySelector(),querySelectorAll(),"
                + "releasePointerCapture(),removeAttribute(),removeAttributeNode(),removeAttributeNS(),scrollHeight,"
                + "scrollLeft,scrollTop,scrollWidth,setAttribute(),setAttributeNode(),setAttributeNodeNS(),"
                + "setAttributeNS(),setPointerCapture(),"
                + "tagName")
    @HtmlUnitNYI(CHROME = "constructor(),onabort,onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,onvolumechange,"
                + "onwaiting,onwheel,style",
            EDGE = "constructor(),onabort,onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,"
                + "onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,onvolumechange,"
                + "onwaiting,onwheel,style",
            FF78 = "constructor(),onabort,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,"
                + "oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,oninput,oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,style",
            FF = "constructor(),onabort,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,"
                + "oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,oninput,oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,style",
            IE = "attributes,childElementCount,clientHeight,clientLeft,clientTop,clientWidth,constructor,"
                + "firstElementChild,getAttribute(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),"
                + "getBoundingClientRect(),getClientRects(),getElementsByTagName(),getElementsByTagNameNS(),"
                + "hasAttribute(),hasAttributeNS(),lastElementChild,msMatchesSelector(),nextElementSibling,"
                + "ongotpointercapture,onlostpointercapture,onmsgesturechange,onmsgesturedoubletap,onmsgestureend,"
                + "onmsgesturehold,onmsgesturestart,onmsgesturetap,onmsgotpointercapture,onmsinertiastart,"
                + "onmslostpointercapture,onmspointercancel,onmspointerdown,onmspointerenter,onmspointerleave,"
                + "onmspointermove,onmspointerout,onmspointerover,onmspointerup,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "previousElementSibling,querySelector(),querySelectorAll(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),scrollHeight,scrollLeft,scrollTop,scrollWidth,setAttribute(),"
                + "setAttributeNode(),setAttributeNS(),tagName")
    public void svgElement() throws Exception {
        testString("", "svg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),localName,name,namespaceURI,ownerElement,prefix,specified,value",
            EDGE = "constructor(),localName,name,namespaceURI,ownerElement,prefix,specified,value",
            FF = "constructor(),localName,name,namespaceURI,ownerElement,prefix,specified,value",
            FF78 = "constructor(),localName,name,namespaceURI,ownerElement,prefix,specified,value",
            IE = "constructor,expando,name,ownerElement,specified,value")
    @HtmlUnitNYI(IE = "constructor,expando,localName,name,namespaceURI,ownerElement,prefix,specified,value")
    public void nodeAndAttr() throws Exception {
        testString("", "document.createAttribute('some_attrib')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),comparePoint(),constructor(),createContextualFragment(),deleteContents(),"
                + "detach(),END_TO_END,END_TO_START,endContainer,endOffset,expand(),extractContents(),"
                + "getBoundingClientRect(),getClientRects(),insertNode(),intersectsNode(),isPointInRange(),"
                + "selectNode(),selectNodeContents(),setEnd(),setEndAfter(),setEndBefore(),setStart(),"
                + "setStartAfter(),setStartBefore(),START_TO_END,START_TO_START,startContainer,startOffset,"
                + "surroundContents(),"
                + "toString()",
            EDGE = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),comparePoint(),constructor(),createContextualFragment(),deleteContents(),"
                + "detach(),END_TO_END,END_TO_START,endContainer,endOffset,expand(),extractContents(),"
                + "getBoundingClientRect(),getClientRects(),insertNode(),intersectsNode(),isPointInRange(),"
                + "selectNode(),selectNodeContents(),setEnd(),setEndAfter(),setEndBefore(),setStart(),"
                + "setStartAfter(),setStartBefore(),START_TO_END,START_TO_START,startContainer,startOffset,"
                + "surroundContents(),"
                + "toString()",
            FF = "cloneContents(),cloneRange(),collapse(),commonAncestorContainer,compareBoundaryPoints(),"
                + "comparePoint(),constructor(),createContextualFragment(),deleteContents(),detach(),END_TO_END,"
                + "END_TO_START,extractContents(),getBoundingClientRect(),getClientRects(),insertNode(),"
                + "intersectsNode(),isPointInRange(),selectNode(),selectNodeContents(),setEnd(),setEndAfter(),"
                + "setEndBefore(),setStart(),setStartAfter(),setStartBefore(),START_TO_END,START_TO_START,"
                + "surroundContents(),"
                + "toString()",
            FF78 = "cloneContents(),cloneRange(),collapse(),commonAncestorContainer,compareBoundaryPoints(),"
                + "comparePoint(),constructor(),createContextualFragment(),deleteContents(),detach(),END_TO_END,"
                + "END_TO_START,extractContents(),getBoundingClientRect(),getClientRects(),insertNode(),"
                + "intersectsNode(),isPointInRange(),selectNode(),selectNodeContents(),setEnd(),setEndAfter(),"
                + "setEndBefore(),setStart(),setStartAfter(),setStartBefore(),START_TO_END,START_TO_START,"
                + "surroundContents(),"
                + "toString()",
            IE = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),constructor,createContextualFragment(),deleteContents(),detach(),"
                + "END_TO_END,END_TO_START,endContainer,endOffset,extractContents(),getBoundingClientRect(),"
                + "getClientRects(),insertNode(),selectNode(),selectNodeContents(),setEnd(),setEndAfter(),"
                + "setEndBefore(),setStart(),setStartAfter(),setStartBefore(),START_TO_END,START_TO_START,"
                + "startContainer,startOffset,surroundContents(),"
                + "toString()")
    @HtmlUnitNYI(CHROME = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),constructor(),createContextualFragment(),deleteContents(),detach(),"
                + "END_TO_END,END_TO_START,endContainer,endOffset,extractContents(),getBoundingClientRect(),"
                + "getClientRects(),insertNode(),selectNode(),selectNodeContents(),setEnd(),setEndAfter(),"
                + "setEndBefore(),setStart(),setStartAfter(),setStartBefore(),START_TO_END,START_TO_START,"
                + "startContainer,startOffset,surroundContents(),toString()",
            EDGE = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),constructor(),createContextualFragment(),deleteContents(),detach(),"
                + "END_TO_END,END_TO_START,endContainer,endOffset,extractContents(),getBoundingClientRect(),"
                + "getClientRects(),insertNode(),selectNode(),selectNodeContents(),setEnd(),setEndAfter(),"
                + "setEndBefore(),setStart(),setStartAfter(),setStartBefore(),START_TO_END,START_TO_START,"
                + "startContainer,startOffset,surroundContents(),toString()",
            FF78 = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),constructor(),createContextualFragment(),deleteContents(),"
                + "detach(),END_TO_END,END_TO_START,endContainer,endOffset,extractContents(),"
                + "getBoundingClientRect(),getClientRects(),insertNode(),selectNode(),selectNodeContents(),"
                + "setEnd(),setEndAfter(),setEndBefore(),setStart(),setStartAfter(),setStartBefore(),"
                + "START_TO_END,START_TO_START,startContainer,startOffset,surroundContents(),toString()",
            FF = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),constructor(),createContextualFragment(),deleteContents(),"
                + "detach(),END_TO_END,END_TO_START,endContainer,endOffset,extractContents(),"
                + "getBoundingClientRect(),getClientRects(),insertNode(),selectNode(),selectNodeContents(),"
                + "setEnd(),setEndAfter(),setEndBefore(),setStart(),setStartAfter(),setStartBefore(),"
                + "START_TO_END,START_TO_START,startContainer,startOffset,surroundContents(),toString()")
    public void range() throws Exception {
        testString("", "document.createRange()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "append(),childElementCount,children,constructor(),firstElementChild,getElementById(),"
                + "lastElementChild,prepend(),querySelector(),querySelectorAll(),replaceChildren()",
            EDGE = "append(),childElementCount,children,constructor(),firstElementChild,getElementById(),"
                + "lastElementChild,prepend(),querySelector(),querySelectorAll(),replaceChildren()",
            FF = "append(),childElementCount,children,constructor(),firstElementChild,getElementById(),"
                + "lastElementChild,prepend(),querySelector(),"
                + "querySelectorAll(),replaceChildren()",
            FF78 = "append(),childElementCount,children,constructor(),firstElementChild,getElementById(),"
                + "lastElementChild,prepend(),querySelector(),querySelectorAll(),"
                + "replaceChildren()",
            IE = "constructor,querySelector(),querySelectorAll(),removeNode(),replaceNode(),swapNode()")
    @HtmlUnitNYI(CHROME = "childElementCount,children,constructor(),firstElementChild,getElementById(),"
                + "lastElementChild,querySelector(),querySelectorAll()",
            EDGE = "childElementCount,children,constructor(),firstElementChild,getElementById(),"
                + "lastElementChild,querySelector(),querySelectorAll()",
            FF78 = "childElementCount,children,constructor(),firstElementChild,getElementById()"
                + ",lastElementChild,querySelector(),querySelectorAll()",
            FF = "childElementCount,children,constructor(),firstElementChild,getElementById()"
                + ",lastElementChild,querySelector(),querySelectorAll()",
            IE = "constructor,querySelector(),querySelectorAll()")
    public void documentFragment() throws Exception {
        testString("", "document.createDocumentFragment()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "baseLatency,close(),constructor(),createMediaElementSource(),createMediaStreamDestination(),"
                + "createMediaStreamSource(),getOutputTimestamp(),resume(),suspend()",
            EDGE = "baseLatency,close(),constructor(),createMediaElementSource(),createMediaStreamDestination(),"
                + "createMediaStreamSource(),getOutputTimestamp(),resume(),suspend()",
            FF = "baseLatency,close(),constructor(),createMediaElementSource(),createMediaStreamDestination(),"
                + "createMediaStreamSource(),createMediaStreamTrackSource(),getOutputTimestamp(),"
                + "outputLatency,suspend()",
            FF78 = "baseLatency,close(),constructor(),createMediaElementSource(),createMediaStreamDestination(),"
                + "createMediaStreamSource(),createMediaStreamTrackSource(),getOutputTimestamp(),"
                + "outputLatency,suspend()",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "constructor(),createGain(),decodeAudioData()",
            EDGE = "constructor(),createGain(),decodeAudioData()",
            FF78 = "constructor(),createGain(),decodeAudioData()",
            FF = "constructor(),createGain(),decodeAudioData()")
    public void audioContext() throws Exception {
        testString("", "new AudioContext()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "automationRate,cancelAndHoldAtTime(),cancelScheduledValues(),constructor(),"
                + "defaultValue,exponentialRampToValueAtTime(),linearRampToValueAtTime(),maxValue,minValue,"
                + "setTargetAtTime(),setValueAtTime(),setValueCurveAtTime(),value",
            EDGE = "automationRate,cancelAndHoldAtTime(),cancelScheduledValues(),constructor(),"
                + "defaultValue,exponentialRampToValueAtTime(),linearRampToValueAtTime(),maxValue,minValue,"
                + "setTargetAtTime(),setValueAtTime(),setValueCurveAtTime(),value",
            FF = "cancelScheduledValues(),constructor(),defaultValue,exponentialRampToValueAtTime(),"
                + "linearRampToValueAtTime(),maxValue,minValue,setTargetAtTime(),setValueAtTime(),"
                + "setValueCurveAtTime(),value",
            FF78 = "cancelScheduledValues(),constructor(),defaultValue,exponentialRampToValueAtTime(),"
                + "linearRampToValueAtTime(),maxValue,minValue,setTargetAtTime(),setValueAtTime(),"
                + "setValueCurveAtTime(),value",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "constructor(),defaultValue,maxValue,minValue,value",
            EDGE = "constructor(),defaultValue,maxValue,minValue,value",
            FF78 = "constructor(),defaultValue,maxValue,minValue,value",
            FF = "constructor(),defaultValue,maxValue,minValue,value")
    public void audioParam() throws Exception {
        testString("var audioCtx = new AudioContext(); var gainNode = new GainNode(audioCtx);", "gainNode.gain");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),gain",
            EDGE = "constructor(),gain",
            FF = "constructor(),gain",
            FF78 = "constructor(),gain",
            IE = "exception")
    public void gainNode() throws Exception {
        testString("var audioCtx = new AudioContext();", "new GainNode(audioCtx)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),returnValue",
            EDGE = "constructor(),returnValue",
            FF = "constructor(),returnValue",
            FF78 = "constructor(),returnValue",
            IE = "exception")
    public void beforeUnloadEvent() throws Exception {
        testString("", "document.createEvent('BeforeUnloadEvent')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "code,constructor(),reason,wasClean",
            EDGE = "code,constructor(),reason,wasClean",
            FF = "code,constructor(),reason,wasClean",
            FF78 = "code,constructor(),reason,wasClean",
            IE = "exception")
    public void closeEvent() throws Exception {
        testString("", "new CloseEvent('type-close')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),data,timecode",
            EDGE = "constructor(),data,timecode",
            FF = "constructor(),data",
            FF78 = "constructor(),data",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "constructor(),data",
            EDGE = "constructor(),data")
    public void blobEvent() throws Exception {
        testString("var debug = {hello: 'world'};"
                    + "var blob = new Blob([JSON.stringify(debug, null, 2)], {type : 'application/json'});",
                    "new BlobEvent('blob', { 'data': blob })");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.TouchEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "altKey,changedTouches,constructor(),ctrlKey,metaKey,shiftKey,targetTouches,touches",
            EDGE = "altKey,changedTouches,constructor(),ctrlKey,metaKey,shiftKey,targetTouches,touches",
            FF = "exception",
            FF78 = "exception",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()")
    public void touchEvent() throws Exception {
        testString("", "new TouchEvent('touch')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.DeviceMotionEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "acceleration,accelerationIncludingGravity,constructor(),interval,rotationRate",
            EDGE = "acceleration,accelerationIncludingGravity,constructor(),interval,rotationRate",
            FF = "acceleration,accelerationIncludingGravity,constructor(),"
                + "initDeviceMotionEvent(),interval,rotationRate",
            FF78 = "acceleration,accelerationIncludingGravity,constructor(),"
                + "initDeviceMotionEvent(),interval,rotationRate",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF = "constructor()",
            FF78 = "constructor()")
    public void deviceMotionEvent() throws Exception {
        testString("", "new DeviceMotionEvent('motion')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.ErrorEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "colno,constructor(),error,filename,lineno,message",
            EDGE = "colno,constructor(),error,filename,lineno,message",
            FF = "colno,constructor(),error,filename,lineno,message",
            FF78 = "colno,constructor(),error,filename,lineno,message",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor()",
            FF = "constructor()")
    public void errorEvent() throws Exception {
        testString("", "new ErrorEvent('error')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.GamepadEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),gamepad",
            EDGE = "constructor(),gamepad",
            FF = "constructor(),gamepad",
            FF78 = "constructor(),gamepad",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "constructor()",
            EDGE = "constructor()",
            FF78 = "constructor()",
            FF = "constructor()")
    public void gamepadEvent() throws Exception {
        testString("", "new GamepadEvent('gamepad')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.MutationEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "ADDITION,attrChange,attrName,constructor(),initMutationEvent(),MODIFICATION,"
                + "newValue,prevValue,relatedNode,REMOVAL",
            EDGE = "ADDITION,attrChange,attrName,constructor(),initMutationEvent(),MODIFICATION,"
                + "newValue,prevValue,relatedNode,REMOVAL",
            FF = "ADDITION,attrChange,attrName,constructor(),initMutationEvent(),MODIFICATION,"
                + "newValue,prevValue,relatedNode,REMOVAL",
            FF78 = "ADDITION,attrChange,attrName,constructor(),initMutationEvent(),MODIFICATION,"
                + "newValue,prevValue,relatedNode,REMOVAL",
            IE = "ADDITION,attrChange,attrName,constructor,initMutationEvent(),MODIFICATION,"
                + "newValue,prevValue,relatedNode,REMOVAL")
    @HtmlUnitNYI(CHROME = "ADDITION,constructor(),MODIFICATION,REMOVAL",
            EDGE = "ADDITION,constructor(),MODIFICATION,REMOVAL",
            FF78 = "ADDITION,constructor(),MODIFICATION,REMOVAL",
            FF = "ADDITION,constructor(),MODIFICATION,REMOVAL",
            IE = "ADDITION,constructor,MODIFICATION,REMOVAL")
    public void mutationEvent() throws Exception {
        testString("", "document.createEvent('MutationEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.OfflineAudioCompletionEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void offlineAudioCompletionEvent() throws Exception {
        testString("", "document.createEvent('OfflineAudioCompletionEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.media.SourceBufferList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "constructor(),length,onaddsourcebuffer,onremovesourcebuffer",
            EDGE = "constructor(),length,onaddsourcebuffer,onremovesourcebuffer",
            FF = "constructor(),length,onaddsourcebuffer,onremovesourcebuffer",
            FF78 = "constructor(),length,onaddsourcebuffer,onremovesourcebuffer",
            IE = "addEventListener(),constructor,dispatchEvent(),item(),length,removeEventListener()")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception",
            FF78 = "exception",
            IE = "exception")
    public void sourceBufferList() throws Exception {
        testString("var mediaSource = new MediaSource;", "mediaSource.sourceBuffers");
    }
}
