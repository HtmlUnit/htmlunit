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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.runners.BrowserVersionClassRunner;

/**
 * Tests all properties of an object.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ElementPropertiesTest extends WebDriverTestCase {

    private static BrowserVersion BROWSER_VERSION_;

    private void test(final String tagName) throws Exception {
        testString("", "document.createElement('" + tagName + "'), unknown");
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
                + "   * @param parent the direct parent of the object (or child of that parent), can be null.\n"
                + "   *        The parent is used to exclude any inherited properties.\n"
                + "   */\n"
                + "  function process(object, parent) {\n"
                + "    var all = [];\n"
                + "    for (var property in object) {\n"
                + "      try {\n"
                + "        if (parent == null || !(property in parent)) {\n"
                + "          if (typeof object[property] == 'function')\n"
                + "            all.push(property + '()');\n"
                + "          else\n"
                + "            all.push(property);\n"
                + "        }\n"
                + "      } catch(e) {\n"
                + "        try{\n"
                + "          if (typeof object[property] == 'function')\n"
                + "            all.push(property + '()');\n"
                + "          else\n"
                + "            all.push(property);\n"
                + "        } catch (e) {\n"
                + "          all.push(property.toString());\n"
                + "        }\n"
                + "      }\n"
                + "    }\n"
                + "    all.sort(sortFunction);\n"
                + "    if (all.length == 0) { all = '-' };\n"
                + "    if (all.length > 140) {\n"
                 + "      alert(all.slice(0, 140));\n"
                + "      all = all.slice(140);\n"
                + "    }\n"
                + "    alert(all);\n"
                + "  }\n"
                + "  function sortFunction(s1, s2) {\n"
                + "    return s1.toLowerCase() > s2.toLowerCase() ? 1 : -1;\n"
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
                + "/properties-" + BROWSER_VERSION_.getNickname() + ".html"),
                htmlHeader()
                    .append(overview(counts))
                    .append(htmlDetailsHeader())
                    .append(html)
                    .append(htmlDetailsFooter())
                    .append(htmlFooter()).toString(), ISO_8859_1);
    }

    private static void collectStatistics(final BrowserVersion browserVersion, final DefaultCategoryDataset dataset,
            final StringBuilder html, final int[] counts) {
        final Method[] methods = ElementPropertiesTest.class.getMethods();
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
            new File(getTargetDirectory() + "/properties-" + BROWSER_VERSION_.getNickname() + ".png"));
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
    @Alerts(DEFAULT = "appendData(),data,deleteData(),insertData(),length,replaceData(),splitText(),substringData(),"
                + "wholeText",
            IE = "appendData(),data,deleteData(),insertData(),length,replaceData(),replaceWholeText(),splitText(),"
                + "substringData(),"
                + "wholeText")
    @HtmlUnitNYI(IE = "appendData(),data,deleteData(),insertData(),length,replaceData(),splitText(),"
                + "substringData(),wholeText")
    public void text() throws Exception {
        testString("", "document.createTextNode('some text'), unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "name,ownerElement,specified,value",
            IE = "expando,name,ownerElement,specified,value")
    public void attr() throws Exception {
        testString("", "document.createAttribute('some_attrib'), unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "appendData(),data,deleteData(),insertData(),length,replaceData(),substringData()",
            IE = "appendData(),data,deleteData(),insertData(),length,replaceData(),substringData(),text")
    public void comment() throws Exception {
        testString("", "document.createComment('come_comment'), unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "namedRecordset(),recordset")
    @HtmlUnitNYI(IE = "-")
    public void unknown() throws Exception {
        testString("", "unknown, div");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),contentEditable,dataset,dir,"
                + "draggable,enterKeyHint,focus(),hidden,innerText,inputMode,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onanimationend,"
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
                + "onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            EDGE = "accessKey,attachInternals(),autocapitalize,autofocus,blur(),click(),contentEditable,dataset,dir,"
                + "draggable,enterKeyHint,focus(),hidden,innerText,inputMode,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onanimationend,"
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
                + "onwebkittransitionend,onwheel,outerText,spellcheck,style,tabIndex,title,"
                + "translate",
            FF = "accessKey,accessKeyLabel,blur(),click(),contentEditable,dataset,dir,draggable,focus(),"
                + "hidden,innerText,isContentEditable,lang,nonce,offsetHeight,offsetLeft,offsetParent,offsetTop,"
                + "offsetWidth,"
                + "onabort,onanimationcancel,onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,"
                + "oncanplay,oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragexit,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadend,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,"
                + "ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,onvolumechange,"
                + "onwaiting,onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                + "onwebkittransitionend,onwheel,spellcheck,style,tabIndex,title",
            FF78 = "accessKey,accessKeyLabel,blur(),click(),contentEditable,contextMenu,dataset,dir,draggable,"
                + "focus(),hidden,innerText,isContentEditable,lang,nonce,"
                + "offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,onauxclick,onblur,"
                + "oncanplay,oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragexit,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,"
                + "onloadend,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,"
                + "onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,"
                + "onscroll,onseeked,onseeking,onselect,onselectstart,onshow,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                + "onwebkittransitionend,onwheel,spellcheck,style,tabIndex,title",
            IE = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
                + "clearAttributes(),click(),componentFromPoint(),contains(),contentEditable,createControlRange(),"
                + "currentStyle,dataset,dir,disabled,dragDrop(),draggable,focus(),getAdjacentText(),"
                + "getElementsByClassName(),hidden,hideFocus,id,innerHTML,innerText,insertAdjacentElement(),"
                + "insertAdjacentHTML(),insertAdjacentText(),isContentEditable,isDisabled,isMultiLine,isTextEdit,"
                + "lang,language,mergeAttributes(),msGetInputContext(),namedRecordset(),offsetHeight,offsetLeft,"
                + "offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,onbeforecopy,onbeforecut,"
                + "onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onfocusin,onfocusout,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,"
                + "outerText,parentElement,parentTextEdit,recordNumber,recordset,releaseCapture(),removeNode(),"
                + "replaceAdjacentText(),replaceNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "sourceIndex,spellcheck,style,swapNode(),tabIndex,title,uniqueID,"
                + "uniqueNumber")
    @HtmlUnitNYI(CHROME = "accessKey,blur(),click(),contentEditable,dataset,dir,focus(),hidden,innerText,"
                + "isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,oncontextmenu,"
                + "oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,"
                + "onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            EDGE = "accessKey,blur(),click(),contentEditable,dataset,dir,focus(),hidden,innerText,"
                + "isContentEditable,lang,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,oncontextmenu,"
                + "oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwheel,style,tabIndex,title",
            FF78 = "accessKey,blur(),click(),contentEditable,dataset,dir,focus(),hidden,innerText,isContentEditable,"
                + "lang,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,onblur,oncanplay,"
                + "oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,"
                + "ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,"
                + "onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onshow,onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,spellcheck,style,"
                + "tabIndex,title",
            FF = "accessKey,blur(),click(),contentEditable,dataset,dir,focus(),hidden,innerText,isContentEditable,"
                + "lang,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,"
                + "onanimationcancel,onanimationend,onanimationiteration,onanimationstart,onblur,oncanplay,"
                + "oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,"
                + "ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,"
                + "onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,"
                + "onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,"
                + "ontransitionrun,ontransitionstart,onvolumechange,onwaiting,spellcheck,style,"
                + "tabIndex,title",
            IE = "accessKey,blur(),children,classList,className,clearAttributes(),click(),contains(),"
                + "contentEditable,currentStyle,dataset,dir,disabled,focus(),getElementsByClassName(),"
                + "hidden,id,innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),"
                + "insertAdjacentText(),isContentEditable,lang,language,mergeAttributes(),offsetHeight,"
                + "offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onactivate,onbeforeactivate,"
                + "onbeforecopy,onbeforecut,onbeforedeactivate,onbeforepaste,onblur,oncanplay,oncanplaythrough,"
                + "onchange,onclick,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondeactivate,ondrag,"
                + "ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhelp,oninput,onkeydown,"
                + "onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onmscontentzoom,onmsmanipulationstatechanged,onpaste,onpause,onplay,onplaying,"
                + "onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,onselectstart,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,outerHTML,parentElement,"
                + "releaseCapture(),removeNode(),runtimeStyle,scrollIntoView(),setActive(),setCapture(),"
                + "style,tabIndex,title,uniqueID")
    public void htmlElement() throws Exception {
        testString("", "unknown, element");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Element}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "animate(),append(),"
                + "ariaAtomic,ariaAutoComplete,ariaBusy,ariaChecked,ariaColCount,ariaColIndex,ariaColSpan,"
                + "ariaCurrent,ariaDescription,ariaDisabled,ariaExpanded,ariaHasPopup,ariaHidden,"
                + "ariaKeyShortcuts,ariaLabel,"
                + "ariaLevel,ariaLive,ariaModal,ariaMultiLine,ariaMultiSelectable,ariaOrientation,"
                + "ariaPlaceholder,ariaPosInSet,ariaPressed,ariaReadOnly,ariaRelevant,ariaRequired,"
                + "ariaRoleDescription,ariaRowCount,ariaRowIndex,ariaRowSpan,ariaSelected,ariaSetSize,"
                + "ariaSort,ariaValueMax,ariaValueMin,ariaValueNow,ariaValueText,"
                + "attachShadow(),attributes,attributeStyleMap,childElementCount,children,classList,className,"
                + "clientHeight,clientLeft,clientTop,clientWidth,closest(),computedStyleMap(),"
                + "elementTiming,firstElementChild,getAnimations(),getAttribute(),getAttributeNames(),"
                + "getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),"
                + "getClientRects(),getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),"
                + "hasAttribute(),hasAttributeNS(),hasAttributes(),hasPointerCapture(),id,innerHTML,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),lastElementChild,localName,"
                + "matches(),namespaceURI,"
                + "onbeforecopy,onbeforecut,onbeforepaste,onbeforexrselect,onfullscreenchange,onfullscreenerror,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,part,prefix,prepend(),"
                + "querySelector(),querySelectorAll(),"
                + "releasePointerCapture(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),replaceChildren(),requestFullscreen(),requestPointerLock(),"
                + "scroll(),scrollBy(),scrollHeight,scrollIntoView(),scrollIntoViewIfNeeded(),"
                + "scrollLeft,scrollTo(),scrollTop,scrollWidth,setAttribute(),setAttributeNode(),setAttributeNodeNS(),"
                + "setAttributeNS(),setPointerCapture(),shadowRoot,slot,tagName,toggleAttribute(),"
                + "webkitMatchesSelector(),webkitRequestFullscreen(),webkitRequestFullScreen()",
            EDGE = "animate(),append(),"
                + "ariaAtomic,ariaAutoComplete,ariaBusy,ariaChecked,ariaColCount,ariaColIndex,ariaColSpan,"
                + "ariaCurrent,ariaDescription,ariaDisabled,ariaExpanded,ariaHasPopup,ariaHidden,"
                + "ariaKeyShortcuts,ariaLabel,"
                + "ariaLevel,ariaLive,ariaModal,ariaMultiLine,ariaMultiSelectable,ariaOrientation,"
                + "ariaPlaceholder,ariaPosInSet,ariaPressed,ariaReadOnly,ariaRelevant,ariaRequired,"
                + "ariaRoleDescription,ariaRowCount,ariaRowIndex,ariaRowSpan,ariaSelected,ariaSetSize,"
                + "ariaSort,ariaValueMax,ariaValueMin,ariaValueNow,ariaValueText,"
                + "attachShadow(),attributes,attributeStyleMap,childElementCount,children,classList,className,"
                + "clientHeight,clientLeft,clientTop,clientWidth,closest(),computedStyleMap(),"
                + "elementTiming,firstElementChild,getAnimations(),getAttribute(),getAttributeNames(),"
                + "getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),"
                + "getClientRects(),getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),"
                + "hasAttribute(),hasAttributeNS(),hasAttributes(),hasPointerCapture(),id,innerHTML,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),lastElementChild,localName,"
                + "matches(),namespaceURI,"
                + "onbeforecopy,onbeforecut,onbeforepaste,onbeforexrselect,onfullscreenchange,onfullscreenerror,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,part,prefix,prepend(),"
                + "querySelector(),querySelectorAll(),"
                + "releasePointerCapture(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),replaceChildren(),requestFullscreen(),requestPointerLock(),"
                + "scroll(),scrollBy(),scrollHeight,scrollIntoView(),scrollIntoViewIfNeeded(),"
                + "scrollLeft,scrollTo(),scrollTop,scrollWidth,setAttribute(),setAttributeNode(),setAttributeNodeNS(),"
                + "setAttributeNS(),setPointerCapture(),shadowRoot,slot,tagName,toggleAttribute(),"
                + "webkitMatchesSelector(),webkitRequestFullscreen(),webkitRequestFullScreen()",
            FF = "animate(),append(),attachShadow(),attributes,childElementCount,children,classList,className,"
                + "clientHeight,clientLeft,clientTop,clientWidth,closest(),firstElementChild,getAnimations(),"
                + "getAttribute(),getAttributeNames(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),"
                + "getBoundingClientRect(),getClientRects(),getElementsByClassName(),getElementsByTagName(),"
                + "getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),hasAttributes(),hasPointerCapture(),"
                + "id,innerHTML,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),lastElementChild,"
                + "localName,matches(),mozMatchesSelector(),mozRequestFullScreen(),namespaceURI,onfullscreenchange,"
                + "onfullscreenerror,outerHTML,part,prefix,prepend(),querySelector(),querySelectorAll(),"
                + "releaseCapture(),releasePointerCapture(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),replaceChildren(),requestFullscreen(),requestPointerLock(),"
                + "scroll(),scrollBy(),scrollHeight,"
                + "scrollIntoView(),scrollLeft,scrollLeftMax,scrollTo(),scrollTop,scrollTopMax,scrollWidth,"
                + "setAttribute(),setAttributeNode(),setAttributeNodeNS(),setAttributeNS(),setCapture(),"
                + "setPointerCapture(),shadowRoot,slot,tagName,toggleAttribute(),webkitMatchesSelector()",
            FF78 = "animate(),append(),attachShadow(),attributes,childElementCount,children,classList,className,"
                + "clientHeight,clientLeft,clientTop,clientWidth,closest(),firstElementChild,"
                + "getAnimations(),getAttribute(),"
                + "getAttributeNames(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),"
                + "getBoundingClientRect(),getClientRects(),getElementsByClassName(),getElementsByTagName(),"
                + "getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),hasAttributes(),hasPointerCapture(),"
                + "id,innerHTML,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),lastElementChild,"
                + "localName,matches(),mozMatchesSelector(),mozRequestFullScreen(),namespaceURI,onfullscreenchange,"
                + "onfullscreenerror,outerHTML,part,prefix,prepend(),"
                + "querySelector(),querySelectorAll(),releaseCapture(),"
                + "releasePointerCapture(),removeAttribute(),removeAttributeNode(),removeAttributeNS(),"
                + "replaceChildren(),"
                + "requestFullscreen(),requestPointerLock(),scroll(),scrollBy(),scrollHeight,scrollIntoView(),"
                + "scrollLeft,scrollLeftMax,scrollTo(),scrollTop,scrollTopMax,scrollWidth,setAttribute(),"
                + "setAttributeNode(),setAttributeNodeNS(),setAttributeNS(),setCapture(),setPointerCapture(),"
                + "shadowRoot,slot,tagName,toggleAttribute(),webkitMatchesSelector()",
            IE = "childElementCount,clientHeight,clientLeft,clientTop,clientWidth,firstElementChild,getAttribute(),"
                + "getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),lastElementChild,"
                + "msContentZoomFactor,msGetRegionContent(),msGetUntransformedBounds(),msMatchesSelector(),"
                + "msRegionOverflow,msReleasePointerCapture(),msRequestFullscreen(),msSetPointerCapture(),msZoomTo(),"
                + "nextElementSibling,ongotpointercapture,onlostpointercapture,onmsgesturechange,onmsgesturedoubletap,"
                + "onmsgestureend,onmsgesturehold,onmsgesturestart,onmsgesturetap,onmsgotpointercapture,"
                + "onmsinertiastart,onmslostpointercapture,onmspointercancel,onmspointerdown,onmspointerenter,"
                + "onmspointerleave,onmspointermove,onmspointerout,onmspointerover,onmspointerup,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "previousElementSibling,querySelector(),querySelectorAll(),releasePointerCapture(),removeAttribute(),"
                + "removeAttributeNode(),removeAttributeNS(),scrollHeight,scrollLeft,scrollTop,scrollWidth,"
                + "setAttribute(),setAttributeNode(),setAttributeNodeNS(),setAttributeNS(),setPointerCapture(),tagName")
    @HtmlUnitNYI(CHROME = "attributes,childElementCount,children,classList,className,clientHeight,clientLeft,clientTop,"
                + "clientWidth,firstElementChild,getAttribute(),getAttributeNode(),getAttributeNodeNS(),"
                + "getAttributeNS(),getBoundingClientRect(),getClientRects(),getElementsByClassName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),hasAttributes(),"
                + "id,innerHTML,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),lastElementChild,"
                + "localName,matches(),namespaceURI,onbeforecopy,onbeforecut,onbeforepaste,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,prefix,"
                + "querySelector(),querySelectorAll(),removeAttribute(),removeAttributeNode(),removeAttributeNS(),"
                + "scrollHeight,scrollIntoView(),scrollIntoViewIfNeeded(),scrollLeft,scrollTop,scrollWidth,"
                + "setAttribute(),setAttributeNode(),setAttributeNS(),tagName,webkitMatchesSelector()",
            EDGE = "attributes,childElementCount,children,classList,className,clientHeight,clientLeft,clientTop,"
                + "clientWidth,firstElementChild,getAttribute(),getAttributeNode(),getAttributeNodeNS(),"
                + "getAttributeNS(),getBoundingClientRect(),getClientRects(),getElementsByClassName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),hasAttributes(),"
                + "id,innerHTML,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),lastElementChild,"
                + "localName,matches(),namespaceURI,onbeforecopy,onbeforecut,onbeforepaste,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,prefix,"
                + "querySelector(),querySelectorAll(),removeAttribute(),removeAttributeNode(),removeAttributeNS(),"
                + "scrollHeight,scrollIntoView(),scrollIntoViewIfNeeded(),scrollLeft,scrollTop,scrollWidth,"
                + "setAttribute(),setAttributeNode(),setAttributeNS(),tagName,webkitMatchesSelector()",
            FF78 = "attributes,childElementCount,children,classList,className,clientHeight,clientLeft,clientTop,"
                + "clientWidth,firstElementChild,getAttribute(),getAttributeNode(),getAttributeNodeNS(),"
                + "getAttributeNS(),getBoundingClientRect(),getClientRects(),getElementsByClassName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),"
                + "hasAttributes(),id,innerHTML,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),"
                + "lastElementChild,localName,matches(),mozMatchesSelector(),namespaceURI,outerHTML,prefix,"
                + "querySelector(),querySelectorAll(),releaseCapture(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),scrollHeight,scrollIntoView(),scrollLeft,scrollTop,scrollWidth,setAttribute(),"
                + "setAttributeNode(),setAttributeNS(),setCapture(),tagName,webkitMatchesSelector()",
            FF = "attributes,childElementCount,children,classList,className,clientHeight,clientLeft,clientTop,"
                + "clientWidth,firstElementChild,getAttribute(),getAttributeNode(),getAttributeNodeNS(),"
                + "getAttributeNS(),getBoundingClientRect(),getClientRects(),getElementsByClassName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),"
                + "hasAttributes(),id,innerHTML,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),"
                + "lastElementChild,localName,matches(),mozMatchesSelector(),namespaceURI,outerHTML,prefix,"
                + "querySelector(),querySelectorAll(),releaseCapture(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),scrollHeight,scrollIntoView(),scrollLeft,scrollTop,scrollWidth,setAttribute(),"
                + "setAttributeNode(),setAttributeNS(),setCapture(),tagName,webkitMatchesSelector()",
            IE = "childElementCount,clientHeight,clientLeft,clientTop,clientWidth,firstElementChild,getAttribute(),"
                + "getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),lastElementChild,"
                + "msMatchesSelector(),nextElementSibling,ongotpointercapture,onlostpointercapture,onmsgesturechange,"
                + "onmsgesturedoubletap,onmsgestureend,onmsgesturehold,onmsgesturestart,onmsgesturetap,"
                + "onmsgotpointercapture,onmsinertiastart,onmslostpointercapture,onmspointercancel,onmspointerdown,"
                + "onmspointerenter,onmspointerleave,onmspointermove,onmspointerout,onmspointerover,onmspointerup,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerup,previousElementSibling,querySelector(),querySelectorAll(),"
                + "removeAttribute(),removeAttributeNode(),removeAttributeNS(),scrollHeight,scrollLeft,scrollTop,"
                + "scrollWidth,setAttribute(),setAttributeNode(),setAttributeNS(),tagName")
    public void element() throws Exception {
        testString("", "element, xmlDocument.createTextNode('abc')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Element}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "after(),animate(),"
                + "ariaAtomic,ariaAutoComplete,ariaBusy,ariaChecked,ariaColCount,ariaColIndex,ariaColSpan,ariaCurrent,"
                + "ariaDescription,ariaDisabled,ariaExpanded,ariaHasPopup,ariaHidden,ariaKeyShortcuts,"
                + "ariaLabel,ariaLevel,ariaLive,"
                + "ariaModal,ariaMultiLine,ariaMultiSelectable,ariaOrientation,ariaPlaceholder,ariaPosInSet,"
                + "ariaPressed,ariaReadOnly,ariaRelevant,ariaRequired,ariaRoleDescription,ariaRowCount,ariaRowIndex,"
                + "ariaRowSpan,ariaSelected,ariaSetSize,ariaSort,ariaValueMax,ariaValueMin,ariaValueNow,"
                + "ariaValueText,assignedSlot,attachShadow(),attributes,attributeStyleMap,before(),classList,className,"
                + "clientHeight,clientLeft,clientTop,clientWidth,closest(),computedStyleMap(),"
                + "elementTiming,getAnimations(),getAttribute(),"
                + "getAttributeNames(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),"
                + "getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),getElementsByTagName(),"
                + "getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),hasAttributes(),hasPointerCapture(),id,"
                + "innerHTML,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),localName,matches(),"
                + "namespaceURI,nextElementSibling,onbeforecopy,onbeforecut,"
                + "onbeforepaste,onbeforexrselect,onfullscreenchange,onfullscreenerror,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,"
                + "part,prefix,"
                + "previousElementSibling,releasePointerCapture(),remove(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),replaceWith(),requestFullscreen(),requestPointerLock(),"
                + "scroll(),scrollBy(),scrollHeight,scrollIntoView(),"
                + "scrollIntoViewIfNeeded(),scrollLeft,scrollTo(),scrollTop,scrollWidth,setAttribute(),"
                + "setAttributeNode(),"
                + "setAttributeNodeNS(),setAttributeNS(),setPointerCapture(),shadowRoot,slot,"
                + "tagName,toggleAttribute(),"
                + "webkitMatchesSelector(),webkitRequestFullscreen(),webkitRequestFullScreen()",
            EDGE = "after(),animate(),"
                + "ariaAtomic,ariaAutoComplete,ariaBusy,ariaChecked,ariaColCount,ariaColIndex,ariaColSpan,ariaCurrent,"
                + "ariaDescription,ariaDisabled,ariaExpanded,ariaHasPopup,ariaHidden,ariaKeyShortcuts,"
                + "ariaLabel,ariaLevel,ariaLive,"
                + "ariaModal,ariaMultiLine,ariaMultiSelectable,ariaOrientation,ariaPlaceholder,ariaPosInSet,"
                + "ariaPressed,ariaReadOnly,ariaRelevant,ariaRequired,ariaRoleDescription,ariaRowCount,ariaRowIndex,"
                + "ariaRowSpan,ariaSelected,ariaSetSize,ariaSort,ariaValueMax,ariaValueMin,ariaValueNow,"
                + "ariaValueText,assignedSlot,attachShadow(),attributes,attributeStyleMap,before(),classList,className,"
                + "clientHeight,clientLeft,clientTop,clientWidth,closest(),computedStyleMap(),"
                + "elementTiming,getAnimations(),getAttribute(),"
                + "getAttributeNames(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),"
                + "getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),getElementsByTagName(),"
                + "getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),hasAttributes(),hasPointerCapture(),id,"
                + "innerHTML,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),localName,matches(),"
                + "namespaceURI,nextElementSibling,onbeforecopy,onbeforecut,"
                + "onbeforepaste,onbeforexrselect,onfullscreenchange,onfullscreenerror,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,"
                + "part,prefix,"
                + "previousElementSibling,releasePointerCapture(),remove(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),replaceWith(),requestFullscreen(),requestPointerLock(),"
                + "scroll(),scrollBy(),scrollHeight,scrollIntoView(),"
                + "scrollIntoViewIfNeeded(),scrollLeft,scrollTo(),scrollTop,scrollWidth,setAttribute(),"
                + "setAttributeNode(),"
                + "setAttributeNodeNS(),setAttributeNS(),setPointerCapture(),shadowRoot,slot,"
                + "tagName,toggleAttribute(),"
                + "webkitMatchesSelector(),webkitRequestFullscreen(),webkitRequestFullScreen()",
            FF = "after(),animate(),assignedSlot,attachShadow(),attributes,before(),classList,className,clientHeight,"
                + "clientLeft,clientTop,clientWidth,closest(),getAnimations(),getAttribute(),getAttributeNames(),"
                + "getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),"
                + "hasAttributeNS(),hasAttributes(),hasPointerCapture(),id,innerHTML,insertAdjacentElement(),"
                + "insertAdjacentHTML(),insertAdjacentText(),localName,matches(),mozMatchesSelector(),"
                + "mozRequestFullScreen(),namespaceURI,nextElementSibling,onfullscreenchange,onfullscreenerror,"
                + "outerHTML,part,prefix,previousElementSibling,releaseCapture(),releasePointerCapture(),remove(),"
                + "removeAttribute(),removeAttributeNode(),removeAttributeNS(),replaceWith(),requestFullscreen(),"
                + "requestPointerLock(),scroll(),scrollBy(),scrollHeight,scrollIntoView(),scrollLeft,scrollLeftMax,"
                + "scrollTo(),scrollTop,scrollTopMax,scrollWidth,setAttribute(),setAttributeNode(),"
                + "setAttributeNodeNS(),setAttributeNS(),setCapture(),setPointerCapture(),shadowRoot,slot,tagName,"
                + "toggleAttribute(),webkitMatchesSelector()",
            FF78 = "after(),animate(),assignedSlot,attachShadow(),attributes,before(),classList,className,clientHeight,"
                + "clientLeft,clientTop,clientWidth,closest(),getAnimations(),"
                + "getAttribute(),getAttributeNames(),getAttributeNode(),"
                + "getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),"
                + "hasAttributeNS(),hasAttributes(),hasPointerCapture(),id,innerHTML,insertAdjacentElement(),"
                + "insertAdjacentHTML(),insertAdjacentText(),localName,matches(),mozMatchesSelector(),"
                + "mozRequestFullScreen(),namespaceURI,nextElementSibling,onfullscreenchange,onfullscreenerror,"
                + "outerHTML,part,prefix,previousElementSibling,releaseCapture(),"
                + "releasePointerCapture(),remove(),removeAttribute(),removeAttributeNode(),removeAttributeNS(),"
                + "replaceWith(),requestFullscreen(),requestPointerLock(),scroll(),scrollBy(),scrollHeight,"
                + "scrollIntoView(),scrollLeft,scrollLeftMax,scrollTo(),scrollTop,scrollTopMax,scrollWidth,"
                + "setAttribute(),setAttributeNode(),setAttributeNodeNS(),setAttributeNS(),setCapture(),"
                + "setPointerCapture(),shadowRoot,slot,tagName,toggleAttribute(),webkitMatchesSelector()",
            IE = "childElementCount,clientHeight,clientLeft,clientTop,clientWidth,firstElementChild,getAttribute(),"
                + "getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),getClientRects(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),lastElementChild,"
                + "msContentZoomFactor,msGetRegionContent(),msGetUntransformedBounds(),msMatchesSelector(),"
                + "msRegionOverflow,msReleasePointerCapture(),msRequestFullscreen(),msSetPointerCapture(),msZoomTo(),"
                + "nextElementSibling,ongotpointercapture,onlostpointercapture,onmsgesturechange,onmsgesturedoubletap,"
                + "onmsgestureend,onmsgesturehold,onmsgesturestart,onmsgesturetap,onmsgotpointercapture,"
                + "onmsinertiastart,onmslostpointercapture,onmspointercancel,onmspointerdown,onmspointerenter,"
                + "onmspointerleave,onmspointermove,onmspointerout,onmspointerover,onmspointerup,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "previousElementSibling,releasePointerCapture(),removeAttribute(),removeAttributeNode(),"
                + "removeAttributeNS(),scrollHeight,scrollLeft,scrollTop,scrollWidth,setAttribute(),setAttributeNode(),"
                + "setAttributeNodeNS(),setAttributeNS(),setPointerCapture(),tagName")
    @HtmlUnitNYI(CHROME = "after(),attributes,before(),classList,className,clientHeight,clientLeft,clientTop,"
                + "clientWidth,getAttribute(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),"
                + "getBoundingClientRect(),getClientRects(),getElementsByClassName(),getElementsByTagName(),"
                + "getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),hasAttributes(),id,innerHTML,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),localName,matches(),"
                + "namespaceURI,nextElementSibling,onbeforecopy,onbeforecut,onbeforepaste,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,prefix,"
                + "previousElementSibling,remove(),removeAttribute(),removeAttributeNode(),removeAttributeNS(),"
                + "replaceWith(),scrollHeight,scrollIntoView(),scrollIntoViewIfNeeded(),scrollLeft,scrollTop,"
                + "scrollWidth,setAttribute(),setAttributeNode(),setAttributeNS(),tagName,webkitMatchesSelector()",
            EDGE = "after(),attributes,before(),classList,className,clientHeight,clientLeft,clientTop,"
                + "clientWidth,getAttribute(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),"
                + "getBoundingClientRect(),getClientRects(),getElementsByClassName(),getElementsByTagName(),"
                + "getElementsByTagNameNS(),hasAttribute(),hasAttributeNS(),hasAttributes(),id,innerHTML,"
                + "insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),localName,matches(),"
                + "namespaceURI,nextElementSibling,onbeforecopy,onbeforecut,onbeforepaste,"
                + "onsearch,onwebkitfullscreenchange,onwebkitfullscreenerror,outerHTML,prefix,"
                + "previousElementSibling,remove(),removeAttribute(),removeAttributeNode(),removeAttributeNS(),"
                + "replaceWith(),scrollHeight,scrollIntoView(),scrollIntoViewIfNeeded(),scrollLeft,scrollTop,"
                + "scrollWidth,setAttribute(),setAttributeNode(),setAttributeNS(),tagName,webkitMatchesSelector()",
            FF78 = "after(),attributes,before(),classList,className,clientHeight,clientLeft,clientTop,clientWidth,"
                + "getAttribute(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),"
                + "getClientRects(),getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),"
                + "hasAttribute(),hasAttributeNS(),hasAttributes(),id,innerHTML,insertAdjacentElement(),"
                + "insertAdjacentHTML(),insertAdjacentText(),localName,matches(),mozMatchesSelector(),namespaceURI,"
                + "nextElementSibling,outerHTML,prefix,previousElementSibling,releaseCapture(),remove(),"
                + "removeAttribute(),removeAttributeNode(),removeAttributeNS(),replaceWith(),scrollHeight,"
                + "scrollIntoView(),scrollLeft,scrollTop,scrollWidth,setAttribute(),setAttributeNode(),"
                + "setAttributeNS(),setCapture(),tagName,webkitMatchesSelector()",
            FF = "after(),attributes,before(),classList,className,clientHeight,clientLeft,clientTop,clientWidth,"
                + "getAttribute(),getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),"
                + "getClientRects(),getElementsByClassName(),getElementsByTagName(),getElementsByTagNameNS(),"
                + "hasAttribute(),hasAttributeNS(),hasAttributes(),id,innerHTML,insertAdjacentElement(),"
                + "insertAdjacentHTML(),insertAdjacentText(),localName,matches(),mozMatchesSelector(),namespaceURI,"
                + "nextElementSibling,outerHTML,prefix,previousElementSibling,releaseCapture(),remove(),"
                + "removeAttribute(),removeAttributeNode(),removeAttributeNS(),replaceWith(),scrollHeight,"
                + "scrollIntoView(),scrollLeft,scrollTop,scrollWidth,setAttribute(),setAttributeNode(),"
                + "setAttributeNS(),setCapture(),tagName,webkitMatchesSelector()",
            IE = "childElementCount,clientHeight,clientLeft,clientTop,clientWidth,firstElementChild,getAttribute(),"
                + "getAttributeNode(),getAttributeNodeNS(),getAttributeNS(),getBoundingClientRect(),"
                + "getClientRects(),getElementsByTagName(),getElementsByTagNameNS(),hasAttribute(),"
                + "hasAttributeNS(),lastElementChild,msMatchesSelector(),nextElementSibling,ongotpointercapture,"
                + "onlostpointercapture,onmsgesturechange,onmsgesturedoubletap,onmsgestureend,onmsgesturehold,"
                + "onmsgesturestart,onmsgesturetap,onmsgotpointercapture,onmsinertiastart,onmslostpointercapture,"
                + "onmspointercancel,onmspointerdown,onmspointerenter,onmspointerleave,onmspointermove,"
                + "onmspointerout,onmspointerover,onmspointerup,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,previousElementSibling,"
                + "removeAttribute(),removeAttributeNode(),removeAttributeNS(),scrollHeight,scrollLeft,"
                + "scrollTop,scrollWidth,setAttribute(),setAttributeNode(),setAttributeNS(),tagName")
    public void element2() throws Exception {
        testString("", "element, document.createDocumentFragment()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "blockDirection,clipBottom,clipLeft,clipRight,clipTop,hasLayout")
    @HtmlUnitNYI(IE = "-")
    public void currentStyle() throws Exception {
        testString("", "document.body.currentStyle, document.body.style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,defaultPrevented,eventPhase,initEvent(),isTrusted,"
                + "NONE,path,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,defaultPrevented,eventPhase,initEvent(),isTrusted,"
                + "NONE,path,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "composedPath(),CONTROL_MASK,currentTarget,"
                + "defaultPrevented,eventPhase,explicitOriginalTarget,initEvent(),isTrusted,"
                + "META_MASK,NONE,originalTarget,preventDefault(),returnValue,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,"
                + "type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "composedPath(),CONTROL_MASK,currentTarget,"
                + "defaultPrevented,eventPhase,explicitOriginalTarget,initEvent(),isTrusted,"
                + "META_MASK,NONE,originalTarget,preventDefault(),returnValue,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,"
                + "type",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),isTrusted,preventDefault(),srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                + "type")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,"
                + "CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,"
                + "CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),preventDefault(),"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type")
    public void event() throws Exception {
        testString("", "event ? event : window.event, null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"addEventListener(),alert(),atob(),blur(),btoa(),caches,cancelAnimationFrame(),"
                + "cancelIdleCallback(),captureEvents(),cdc_adoQpoasnfa76pfcZLmcfl_Array(),"
                + "cdc_adoQpoasnfa76pfcZLmcfl_Promise(),cdc_adoQpoasnfa76pfcZLmcfl_Symbol(),chrome,clearInterval(),"
                + "clearTimeout(),clientInformation,close(),closed,confirm(),cookieStore,createImageBitmap(),"
                + "crossOriginIsolated,crypto,customElements,defaultstatus,defaultStatus,devicePixelRatio,"
                + "dispatchEvent(),document,external,fetch(),find(),focus(),frameElement,frames,getComputedStyle(),"
                + "getSelection(),history,indexedDB,innerHeight,innerWidth,isSecureContext,length,localStorage,"
                + "location,locationbar,matchMedia(),menubar,moveBy(),moveTo(),name,navigator,onabort,onafterprint,"
                + "onanimationend,onanimationiteration,onanimationstart,onappinstalled,onauxclick,"
                + "onbeforeinstallprompt,onbeforeprint,onbeforeunload,onblur,oncancel,oncanplay,oncanplaythrough,"
                + "onchange,onclick,onclose,oncontextmenu,oncuechange,ondblclick,ondevicemotion,ondeviceorientation,"
                + "ondeviceorientationabsolute,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,"
                + "onhashchange,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onlanguagechange,onload(),"
                + "onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmessage,onmessageerror,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onoffline,ononline,onpagehide,onpageshow,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerrawupdate,"
                + "onpointerup,onpopstate,onprogress,onratechange,onrejectionhandled,onreset,onresize,onscroll,"
                + "onsearch,onseeked,onseeking,onselect,onselectionchange,onselectstart",
                "onstalled,onstorage,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onunhandledrejection,onunload,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,open(),"
                + "openDatabase(),opener,origin,outerHeight,outerWidth,pageXOffset,pageYOffset,parent,performance,"
                + "PERSISTENT,personalbar,postMessage(),print(),process(),prompt(),queueMicrotask(),releaseEvents(),"
                + "removeEventListener(),requestAnimationFrame(),requestIdleCallback(),resizeBy(),resizeTo(),screen,"
                + "screenLeft,screenTop,screenX,screenY,scroll(),scrollbars,scrollBy(),scrollTo(),scrollX,scrollY,"
                + "self,sessionStorage,setInterval(),setTimeout(),showDirectoryPicker(),showOpenFilePicker(),"
                + "showSaveFilePicker(),sortFunction(),speechSynthesis,status,statusbar,stop(),styleMedia,TEMPORARY,"
                + "test(),toolbar,top,trustedTypes,visualViewport,webkitCancelAnimationFrame(),"
                + "webkitRequestAnimationFrame(),webkitRequestFileSystem(),webkitResolveLocalFileSystemURL(),"
                + "webkitStorageInfo,"
                + "window"},
            EDGE = {"addEventListener(),alert(),atob(),blur(),btoa(),caches,cancelAnimationFrame(),"
                + "cancelIdleCallback(),captureEvents(),cdc_adoQpoasnfa76pfcZLmcfl_Array(),"
                + "cdc_adoQpoasnfa76pfcZLmcfl_Promise(),cdc_adoQpoasnfa76pfcZLmcfl_Symbol(),chrome,clearInterval(),"
                + "clearTimeout(),clientInformation,close(),closed,confirm(),cookieStore,createImageBitmap(),"
                + "crossOriginIsolated,crypto,customElements,defaultstatus,defaultStatus,devicePixelRatio,"
                + "dispatchEvent(),document,external,fetch(),find(),focus(),frameElement,frames,getComputedStyle(),"
                + "getSelection(),history,indexedDB,innerHeight,innerWidth,isSecureContext,length,localStorage,"
                + "location,locationbar,matchMedia(),menubar,moveBy(),moveTo(),name,navigator,onabort,onafterprint,"
                + "onanimationend,onanimationiteration,onanimationstart,onappinstalled,onauxclick,"
                + "onbeforeinstallprompt,onbeforeprint,onbeforeunload,onblur,oncancel,oncanplay,oncanplaythrough,"
                + "onchange,onclick,onclose,oncontextmenu,oncuechange,ondblclick,ondevicemotion,ondeviceorientation,"
                + "ondeviceorientationabsolute,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onformdata,ongotpointercapture,"
                + "onhashchange,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onlanguagechange,onload(),"
                + "onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmessage,onmessageerror,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,"
                + "onoffline,ononline,onpagehide,onpageshow,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerrawupdate,"
                + "onpointerup,onpopstate,onprogress,onratechange,onrejectionhandled,onreset,onresize,onscroll,"
                + "onsearch,onseeked,onseeking,onselect,onselectionchange,onselectstart",
                "onstalled,onstorage,"
                    + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                    + "ontransitionstart,onunhandledrejection,onunload,onvolumechange,onwaiting,onwebkitanimationend,"
                    + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,open(),"
                    + "openDatabase(),opener,origin,outerHeight,outerWidth,pageXOffset,pageYOffset,parent,performance,"
                    + "PERSISTENT,personalbar,postMessage(),print(),process(),prompt(),"
                    + "queueMicrotask(),releaseEvents(),"
                    + "removeEventListener(),requestAnimationFrame(),"
                    + "requestIdleCallback(),resizeBy(),resizeTo(),screen,"
                    + "screenLeft,screenTop,screenX,screenY,scroll(),scrollbars,scrollBy(),scrollTo(),scrollX,scrollY,"
                    + "self,sessionStorage,setInterval(),setTimeout(),showDirectoryPicker(),showOpenFilePicker(),"
                    + "showSaveFilePicker(),sortFunction(),speechSynthesis,"
                    + "status,statusbar,stop(),styleMedia,TEMPORARY,"
                    + "test(),toolbar,top,trustedTypes,visualViewport,webkitCancelAnimationFrame(),"
                    + "webkitRequestAnimationFrame(),webkitRequestFileSystem(),webkitResolveLocalFileSystemURL(),"
                    + "webkitStorageInfo,"
                    + "window"},
            FF = {"addEventListener(),alert(),applicationCache,atob(),blur(),btoa(),caches,cancelAnimationFrame(),"
                + "cancelIdleCallback(),captureEvents(),clearInterval(),clearTimeout(),close(),closed,confirm(),"
                + "createImageBitmap(),crossOriginIsolated,crypto,customElements,devicePixelRatio,dispatchEvent(),"
                + "document,dump(),event,external,fetch(),find(),focus(),frameElement,frames,fullScreen,"
                + "getComputedStyle(),getDefaultComputedStyle(),getSelection(),history,indexedDB,innerHeight,"
                + "innerWidth,InstallTrigger,isSecureContext,length,localStorage,location,locationbar,matchMedia(),"
                + "menubar,moveBy(),moveTo(),mozInnerScreenX,mozInnerScreenY,name,navigator,onabort,"
                + "onabsolutedeviceorientation,onafterprint,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,"
                + "onchange,onclick,onclose,oncontextmenu,oncuechange,ondblclick,ondevicelight,ondevicemotion,"
                + "ondeviceorientation,ondeviceproximity,ondrag,ondragend,ondragenter,ondragexit,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onformdata,"
                + "ongotpointercapture,onhashchange,oninput,oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onlanguagechange,onload(),onloadeddata,onloadedmetadata,onloadend,onloadstart,"
                + "onlostpointercapture,onmessage,onmessageerror,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,"
                + "onoffline,ononline,onpagehide,onpageshow,onpause,onplay,onplaying,onpointercancel,"
                + "onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,"
                + "onpointerup,onpopstate,onprogress,onratechange,onrejectionhandled,onreset,onresize,onscroll,"
                + "onseeked,onseeking,onselect,onselectstart",
                "onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,"
                    + "ontransitionend,ontransitionrun,ontransitionstart,onunhandledrejection,onunload,"
                    + "onuserproximity,onvolumechange,onvrdisplayactivate,onvrdisplayconnect,"
                    + "onvrdisplaydeactivate,onvrdisplaydisconnect,onvrdisplaypresentchange,"
                    + "onwaiting,onwebkitanimationend,"
                    + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,open(),opener,"
                    + "origin,outerHeight,outerWidth,pageXOffset,pageYOffset,parent,"
                    + "performance,personalbar,postMessage(),"
                    + "print(),process(),prompt(),queueMicrotask(),releaseEvents(),removeEventListener(),"
                    + "requestAnimationFrame(),requestIdleCallback(),resizeBy(),resizeTo(),screen,screenLeft,screenTop,"
                    + "screenX,screenY,scroll(),scrollbars,scrollBy(),scrollByLines(),scrollByPages(),scrollMaxX,"
                    + "scrollMaxY,scrollTo(),scrollX,scrollY,self,sessionStorage,"
                    + "setInterval(),setResizable(),setTimeout(),"
                    + "sidebar,sizeToContent(),sortFunction(),speechSynthesis,status,statusbar,stop(),test(),toolbar,"
                    + "top,u2f,updateCommands(),window"},
            FF78 = {"addEventListener(),alert(),applicationCache,atob(),blur(),btoa(),caches,cancelAnimationFrame(),"
                + "cancelIdleCallback(),captureEvents(),clearInterval(),clearTimeout(),close(),closed,confirm(),"
                + "createImageBitmap(),crossOriginIsolated,crypto,customElements,"
                + "devicePixelRatio,dispatchEvent(),document,dump(),"
                + "event,external,fetch(),find(),focus(),frameElement,frames,fullScreen,getComputedStyle(),"
                + "getDefaultComputedStyle(),getSelection(),history,indexedDB,innerHeight,innerWidth,InstallTrigger,"
                + "isSecureContext,length,localStorage,location,locationbar,matchMedia(),menubar,moveBy(),moveTo(),"
                + "mozInnerScreenX,mozInnerScreenY,name,navigator,onabort,onabsolutedeviceorientation,"
                + "onafterprint,onanimationcancel,onanimationend,onanimationiteration,onanimationstart,onauxclick,"
                + "onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncuechange,ondblclick,ondevicelight,ondevicemotion,ondeviceorientation,"
                + "ondeviceproximity,ondrag,ondragend,ondragenter,ondragexit,ondragleave,ondragover,ondragstart,"
                + "ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onformdata,"
                + "ongotpointercapture,onhashchange,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onlanguagechange,onload(),onloadeddata,"
                + "onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmessage,onmessageerror,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmozfullscreenchange,onmozfullscreenerror,onoffline,ononline,onpagehide,onpageshow,onpause,"
                + "onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onpopstate,onprogress,onratechange,onrejectionhandled,"
                + "onreset,onresize,onscroll,"
                + "onseeked,onseeking,onselect,onselectstart",
                "onshow,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,ontoggle,"
                    + "ontransitioncancel,ontransitionend,"
                    + "ontransitionrun,ontransitionstart,onunhandledrejection,onunload,"
                    + "onuserproximity,onvolumechange,onvrdisplayactivate,onvrdisplayconnect,onvrdisplaydeactivate,"
                    + "onvrdisplaydisconnect,onvrdisplaypresentchange,onwaiting,onwebkitanimationend,"
                    + "onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,onwheel,open(),opener,"
                    + "origin,outerHeight,outerWidth,pageXOffset,pageYOffset,parent,"
                    + "performance,personalbar,postMessage(),"
                    + "print(),process(),prompt(),queueMicrotask(),"
                    + "releaseEvents(),removeEventListener(),requestAnimationFrame(),"
                    + "requestIdleCallback(),resizeBy(),resizeTo(),"
                    + "screen,screenLeft,screenTop,screenX,screenY,scroll(),"
                    + "scrollbars,scrollBy(),scrollByLines(),scrollByPages(),scrollMaxX,scrollMaxY,scrollTo(),"
                    + "scrollX,scrollY,self,sessionStorage,setInterval(),setResizable(),setTimeout(),sidebar,"
                    + "sizeToContent(),sortFunction(),speechSynthesis,status,statusbar,stop(),test(),toolbar,top,u2f,"
                    + "updateCommands(),window"},
            IE = {"addEventListener(),alert(),animationStartTime,applicationCache,atob(),blur(),btoa(),"
                + "cancelAnimationFrame(),captureEvents(),clearImmediate(),clearInterval(),clearTimeout(),"
                + "clientInformation,clipboardData,close(),closed,confirm(),console,"
                + "defaultStatus,devicePixelRatio,dispatchEvent(),document,doNotTrack,event,external,focus(),"
                + "frameElement,frames,getComputedStyle(),getSelection(),history,indexedDB,innerHeight,"
                + "innerWidth,item(),length,localStorage,location,matchMedia(),maxConnectionsPerServer,moveBy(),"
                + "moveTo(),msAnimationStartTime,msCancelRequestAnimationFrame(),msClearImmediate(),msCrypto,"
                + "msIndexedDB,msIsStaticHTML(),msMatchMedia(),msRequestAnimationFrame(),msSetImmediate(),"
                + "msWriteProfilerMark(),name,navigate(),navigator,offscreenBuffering,onabort,onafterprint,"
                + "onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncompassneedscalibration,oncontextmenu,ondblclick,ondevicemotion,ondeviceorientation,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhashchange,onhelp,"
                + "oninput,onkeydown,onkeypress,onkeyup,onload(),onloadeddata,onloadedmetadata,onloadstart,"
                + "onmessage,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onmsgesturechange,onmsgesturedoubletap,onmsgestureend,onmsgesturehold,"
                + "onmsgesturestart,onmsgesturetap,onmsinertiastart,onmspointercancel,onmspointerdown,"
                + "onmspointerenter,onmspointerleave,onmspointermove,onmspointerout,onmspointerover,onmspointerup,"
                + "onoffline,ononline,onpagehide,onpageshow,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onpopstate,"
                + "onprogress,onratechange,onreadystatechange,onreset,onresize,onscroll",
                "onseeked,onseeking,onselect,"
                    + "onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,onunload,onvolumechange,"
                    + "onwaiting,open(),"
                    + "opener,outerHeight,outerWidth,pageXOffset,pageYOffset,parent,performance,"
                    + "postMessage(),print(),"
                    + "process(),prompt(),releaseEvents(),removeEventListener(),requestAnimationFrame(),resizeBy(),"
                    + "resizeTo(),screen,screenLeft,screenTop,screenX,screenY,scroll(),scrollBy(),scrollTo(),self,"
                    + "sessionStorage,setImmediate(),setInterval(),setTimeout(),showHelp(),showModalDialog(),"
                    + "showModelessDialog(),sortFunction(),status,styleMedia,test(),top,toStaticHTML(),toString(),"
                    + "window"})
    @HtmlUnitNYI(CHROME = {"addEventListener(),alert(),atob(),blur(),btoa(),cancelAnimationFrame(),"
                + "captureEvents(),clearInterval(),clearTimeout(),clientInformation,close(),closed,confirm(),"
                + "console,crypto,devicePixelRatio,dispatchEvent(),document,event,external,find(),focus(),"
                + "frameElement,frames,getComputedStyle(),getSelection(),history,innerHeight,innerWidth,length,"
                + "localStorage,location,matchMedia(),moveBy(),moveTo(),name,navigator,offscreenBuffering,"
                + "onabort,onanimationend,onanimationiteration,onanimationstart,onauxclick,onbeforeunload,"
                + "onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,oncontextmenu,"
                + "oncuechange,ondblclick,ondevicemotion,ondeviceorientation,ondeviceorientationabsolute,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,ongotpointercapture,onhashchange,oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onlanguagechange,onload(),onloadeddata,onloadedmetadata,"
                + "onloadstart,onlostpointercapture,onmessage,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onoffline,ononline,onpagehide,"
                + "onpageshow,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onpopstate,onprogress,"
                + "onratechange,onrejectionhandled,onreset,onresize,onscroll,onsearch,onseeked,onseeking,"
                + "onselect,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,ontoggle,"
                + "ontransitionend,onunhandledrejection,onunload,onvolumechange,onwaiting,"
                + "onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                + "onwebkittransitionend,onwheel,open(),opener,outerHeight,outerWidth,pageXOffset,"
                + "pageYOffset,parent",
                "performance,PERSISTENT,postMessage(),print(),process(),prompt(),"
                    + "releaseEvents(),removeEventListener(),requestAnimationFrame(),resizeBy(),resizeTo(),"
                    + "screen,scroll(),scrollBy(),scrollTo(),scrollX,scrollY,self,sessionStorage,"
                    + "setInterval(),setTimeout(),sortFunction(),speechSynthesis,status,stop(),styleMedia,"
                    + "TEMPORARY,test(),top,window"},
            EDGE = {"addEventListener(),alert(),atob(),blur(),btoa(),cancelAnimationFrame(),"
                + "captureEvents(),clearInterval(),clearTimeout(),clientInformation,close(),closed,confirm(),"
                + "console,crypto,devicePixelRatio,dispatchEvent(),document,event,external,find(),focus(),"
                + "frameElement,frames,getComputedStyle(),getSelection(),history,innerHeight,innerWidth,length,"
                + "localStorage,location,matchMedia(),moveBy(),moveTo(),name,navigator,offscreenBuffering,"
                + "onabort,onanimationend,onanimationiteration,onanimationstart,onauxclick,onbeforeunload,"
                + "onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,oncontextmenu,"
                + "oncuechange,ondblclick,ondevicemotion,ondeviceorientation,ondeviceorientationabsolute,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,ongotpointercapture,onhashchange,oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onlanguagechange,onload(),onloadeddata,onloadedmetadata,"
                + "onloadstart,onlostpointercapture,onmessage,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onoffline,ononline,onpagehide,"
                + "onpageshow,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onpopstate,onprogress,"
                + "onratechange,onrejectionhandled,onreset,onresize,onscroll,onsearch,onseeked,onseeking,"
                + "onselect,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,ontoggle,"
                + "ontransitionend,onunhandledrejection,onunload,onvolumechange,onwaiting,"
                + "onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                + "onwebkittransitionend,onwheel,open(),opener,outerHeight,outerWidth,pageXOffset,pageYOffset,"
                + "parent",
                "performance,PERSISTENT,postMessage(),print(),process(),prompt(),"
                    + "releaseEvents(),removeEventListener(),requestAnimationFrame(),resizeBy(),resizeTo(),"
                    + "screen,scroll(),scrollBy(),scrollTo(),scrollX,scrollY,self,sessionStorage,"
                    + "setInterval(),setTimeout(),sortFunction(),speechSynthesis,status,stop(),styleMedia,"
                    + "TEMPORARY,test(),top,window"},
            FF78 = {"addEventListener(),alert(),applicationCache,atob(),blur(),btoa(),cancelAnimationFrame(),"
                + "captureEvents(),clearInterval(),clearTimeout(),close(),closed,confirm(),console,controllers,"
                + "crypto,devicePixelRatio,dispatchEvent(),document,dump(),event,external,find(),focus(),"
                + "frameElement,frames,getComputedStyle(),getSelection(),history,innerHeight,innerWidth,"
                + "length,localStorage,location,matchMedia(),moveBy(),moveTo(),mozInnerScreenX,mozInnerScreenY,"
                + "name,navigator,netscape,onabort,onafterprint,onbeforeprint,onbeforeunload,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,ondblclick,"
                + "ondevicelight,ondevicemotion,ondeviceorientation,ondeviceproximity,ondrag,ondragend,"
                + "ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,"
                + "onerror,onfocus,onhashchange,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onlanguagechange,"
                + "onload(),onloadeddata,onloadedmetadata,onloadstart,onmessage,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onoffline,ononline,onpagehide,onpageshow,onpause,onplay,onplaying,"
                + "onpopstate,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onshow,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,onunload,onuserproximity,"
                + "onvolumechange,onwaiting,onwheel,open(),opener,outerHeight,outerWidth,pageXOffset,"
                + "pageYOffset,parent,performance,postMessage(),print(),process(),prompt(),releaseEvents(),"
                + "removeEventListener(),requestAnimationFrame(),resizeBy(),resizeTo(),screen,scroll(),"
                + "scrollBy(),scrollByLines(),scrollByPages(),scrollTo()",
                "scrollX,scrollY,self,sessionStorage,"
                    + "setInterval(),setTimeout(),sortFunction(),status,stop(),test(),top,window"},
            FF = {"addEventListener(),alert(),applicationCache,atob(),blur(),btoa(),cancelAnimationFrame(),"
                + "captureEvents(),clearInterval(),clearTimeout(),close(),closed,confirm(),console,controllers,"
                + "crypto,devicePixelRatio,dispatchEvent(),document,dump(),event,external,find(),focus(),"
                + "frameElement,frames,getComputedStyle(),getSelection(),history,innerHeight,innerWidth,"
                + "length,localStorage,location,matchMedia(),moveBy(),moveTo(),mozInnerScreenX,mozInnerScreenY,"
                + "name,navigator,netscape,onabort,onafterprint,onbeforeprint,onbeforeunload,"
                + "onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,ondblclick,"
                + "ondevicelight,ondevicemotion,ondeviceorientation,ondeviceproximity,ondrag,ondragend,"
                + "ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,"
                + "onerror,onfocus,onhashchange,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onlanguagechange,"
                + "onload(),onloadeddata,onloadedmetadata,onloadstart,onmessage,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onoffline,ononline,onpagehide,onpageshow,onpause,onplay,onplaying,"
                + "onpopstate,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,onunload,onuserproximity,"
                + "onvolumechange,onwaiting,onwheel,open(),opener,outerHeight,outerWidth,pageXOffset,"
                + "pageYOffset,parent,performance,postMessage(),print(),process(),prompt(),releaseEvents(),"
                + "removeEventListener(),requestAnimationFrame(),resizeBy(),resizeTo(),screen,scroll(),"
                + "scrollBy(),scrollByLines(),scrollByPages(),scrollTo(),scrollX",
                "scrollY,self,sessionStorage,"
                    + "setInterval(),setTimeout(),sortFunction(),status,stop(),test(),top,window"},
            IE = {"addEventListener(),alert(),applicationCache,atob(),blur(),btoa(),cancelAnimationFrame(),"
                + "captureEvents(),clearInterval(),clearTimeout(),clientInformation,clipboardData,close(),"
                + "closed,CollectGarbage(),confirm(),console,devicePixelRatio,dispatchEvent(),document,"
                + "doNotTrack,event,external,focus(),frameElement,frames,getComputedStyle(),getSelection(),"
                + "history,innerHeight,innerWidth,length,localStorage,location,matchMedia(),moveBy(),"
                + "moveTo(),name,navigate(),navigator,offscreenBuffering,onabort,onafterprint,"
                + "onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,"
                + "onfocusout,onhashchange,onhelp,oninput,onkeydown,onkeypress,onkeyup,onload(),"
                + "onloadeddata,onloadedmetadata,onloadstart,onmessage,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onmsgesturechange,"
                + "onmsgesturedoubletap,onmsgestureend,onmsgesturehold,onmsgesturestart,onmsgesturetap,"
                + "onmsinertiastart,onmspointercancel,onmspointerdown,onmspointerenter,onmspointerleave,"
                + "onmspointermove,onmspointerout,onmspointerover,onmspointerup,onoffline,ononline,"
                + "onpagehide,onpageshow,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "onpopstate,onprogress,onratechange,onreadystatechange,onreset,onresize,onscroll,"
                + "onseeked,onseeking,onselect,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,"
                + "onunload,onvolumechange,onwaiting,open(),opener,outerHeight,outerWidth,pageXOffset,"
                + "pageYOffset,parent",
                "performance,postMessage(),print(),process(),prompt(),"
                    + "releaseEvents(),removeEventListener(),requestAnimationFrame(),resizeBy(),"
                    + "resizeTo(),screen,ScriptEngine(),ScriptEngineBuildVersion(),"
                    + "ScriptEngineMajorVersion(),ScriptEngineMinorVersion(),scroll(),scrollBy(),"
                    + "scrollTo(),self,sessionStorage,setInterval(),setTimeout(),showModalDialog(),"
                    + "showModelessDialog(),sortFunction(),status,styleMedia,test(),top,window"})
    public void window() throws Exception {
        testString("", "window, null");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAbbreviated}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void abbr() throws Exception {
        test("abbr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAcronym}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void acronym() throws Exception {
        test("acronym");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAnchor}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "charset,coords,download,hash,host,hostname,href,hreflang,name,origin,password,pathname,ping,"
                + "port,protocol,referrerPolicy,rel,relList,rev,search,shape,target,text,type,"
                + "username",
            IE = "charset,coords,hash,host,hostname,href,hreflang,Methods,mimeType,name,nameProp,pathname,port,"
                + "protocol,protocolLong,rel,rev,search,shape,target,text,type,"
                + "urn")
    public void a() throws Exception {
        test("a");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAddress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,clear,width")
    public void address() throws Exception {
        test("address");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlApplet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "align,alt,altHtml,archive,BaseHref,border,classid,code,codeBase,codeType,contentDocument,data,"
                + "declare,form,height,hspace,name,object,standby,type,useMap,vspace,width")
    @HtmlUnitNYI(IE = "align,alt,border,classid,height,width")
    public void applet() throws Exception {
        test("applet");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "alt,coords,download,hash,host,hostname,href,noHref,origin,password,pathname,ping,port,"
                + "protocol,referrerPolicy,rel,relList,search,shape,target,username",
            IE = "alt,coords,hash,host,hostname,href,noHref,pathname,port,protocol,rel,search,shape,target")
    @HtmlUnitNYI(CHROME = "alt,coords,rel,relList",
            EDGE = "alt,coords,rel,relList",
            FF78 = "alt,coords,rel,relList",
            FF = "alt,coords,rel,relList",
            IE = "alt,coords,rel")
    public void area() throws Exception {
        test("area");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlArticle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void article() throws Exception {
        test("article");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAside}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void aside() throws Exception {
        test("aside");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAudio}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "addTextTrack(),autoplay,buffered,"
                + "canPlayType(),captureStream(),controls,controlsList,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,disableRemotePlayback,duration,"
                + "ended,error,HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,"
                + "HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,load(),loop,mediaKeys,muted,NETWORK_EMPTY,NETWORK_IDLE,"
                + "NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,onencrypted,"
                + "onwaitingforkey,pause(),paused,play(),playbackRate,played,preload,preservesPitch,readyState,remote,"
                + "seekable,seeking,setMediaKeys(),setSinkId(),sinkId,src,srcObject,textTracks,"
                + "volume,webkitAudioDecodedByteCount,"
                + "webkitVideoDecodedByteCount",
            EDGE = "addTextTrack(),autoplay,buffered,"
                + "canPlayType(),captureStream(),controls,controlsList,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,disableRemotePlayback,duration,"
                + "ended,error,HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,"
                + "HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,load(),loop,mediaKeys,muted,NETWORK_EMPTY,NETWORK_IDLE,"
                + "NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,onencrypted,"
                + "onwaitingforkey,pause(),paused,play(),playbackRate,played,preload,preservesPitch,readyState,remote,"
                + "seekable,seeking,setMediaKeys(),setSinkId(),sinkId,src,srcObject,textTracks,"
                + "volume,webkitAudioDecodedByteCount,"
                + "webkitVideoDecodedByteCount",
            FF = "addTextTrack(),autoplay,buffered,canPlayType(),controls,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,duration,ended,error,fastSeek(),HAVE_CURRENT_DATA,"
                + "HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,load(),loop,mediaKeys,"
                + "mozAudioCaptured,mozCaptureStream(),mozCaptureStreamUntilEnded(),mozFragmentEnd,mozGetMetadata(),"
                + "mozPreservesPitch,muted,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,"
                + "onencrypted,onwaitingforkey,pause(),paused,play(),playbackRate,played,preload,readyState,seekable,"
                + "seeking,seekToNextFrame(),setMediaKeys(),src,srcObject,textTracks,volume",
            FF78 = "addTextTrack(),autoplay,buffered,canPlayType(),controls,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,duration,ended,error,fastSeek(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,"
                + "HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,load(),loop,mediaKeys,mozAudioCaptured,"
                + "mozCaptureStream(),mozCaptureStreamUntilEnded(),mozFragmentEnd,mozGetMetadata(),mozPreservesPitch,"
                + "muted,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,onencrypted,"
                + "onwaitingforkey,pause(),paused,play(),playbackRate,played,preload,readyState,seekable,seeking,"
                + "seekToNextFrame(),setMediaKeys(),src,srcObject,textTracks,volume",
            IE = "addTextTrack(),audioTracks,autobuffer,autoplay,buffered,canPlayType(),controls,currentSrc,"
                + "currentTime,defaultPlaybackRate,duration,ended,error,HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,"
                + "HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,initialTime,load(),loop,"
                + "msGraphicsTrustStatus,msKeys,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,msSetMediaKeys(),muted,"
                + "NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,"
                + "NETWORK_NO_SOURCE,networkState,onmsneedkey,"
                + "pause(),paused,play(),playbackRate,played,preload,readyState,"
                + "seekable,seeking,src,textTracks,volume")
    @HtmlUnitNYI(CHROME = "canPlayType(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,"
                + "HAVE_NOTHING,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,pause(),play()",
            EDGE = "canPlayType(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,"
                + "HAVE_NOTHING,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,pause(),play()",
            FF78 = "canPlayType(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,"
                + "NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,pause(),play()",
            FF = "canPlayType(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,"
                + "NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,pause(),play()",
            IE = "canPlayType(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,"
                + "HAVE_NOTHING,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,pause(),play()")
    public void audio() throws Exception {
        test("audio");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBackgroundSound}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "balance,loop,src,volume")
    @HtmlUnitNYI(IE = "-")
    public void bgsound() throws Exception {
        test("bgsound");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBase}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("href,target")
    public void base() throws Exception {
        test("base");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBaseFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "color,face,size")
    public void basefont() throws Exception {
        test("basefont");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBidirectionalIsolation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void bdi() throws Exception {
        test("bdi");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void bdo() throws Exception {
        test("bdo");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBig}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void big() throws Exception {
        test("big");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBlink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void blink() throws Exception {
        test("blink");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBlockQuote}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "cite",
            IE = "cite,clear,width")
    public void blockquote() throws Exception {
        test("blockquote");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "aLink,background,bgColor,link,onafterprint,onbeforeprint,"
                + "onbeforeunload,onhashchange,onlanguagechange,onmessage,"
                + "onmessageerror,onoffline,ononline,onpagehide,onpageshow,onpopstate,"
                + "onrejectionhandled,onstorage,onunhandledrejection,onunload,"
                + "text,vLink",
            EDGE = "aLink,background,bgColor,link,onafterprint,onbeforeprint,"
                + "onbeforeunload,onhashchange,onlanguagechange,onmessage,"
                + "onmessageerror,onoffline,ononline,onpagehide,onpageshow,onpopstate,"
                + "onrejectionhandled,onstorage,onunhandledrejection,onunload,"
                + "text,vLink",
            FF = "aLink,background,bgColor,link,onafterprint,onbeforeprint,onbeforeunload,onhashchange,"
                + "onlanguagechange,onmessage,onmessageerror,"
                + "onoffline,ononline,onpagehide,onpageshow,onpopstate,onrejectionhandled,"
                + "onstorage,onunhandledrejection,onunload,text,vLink",
            FF78 = "aLink,background,bgColor,link,onafterprint,onbeforeprint,onbeforeunload,onhashchange,"
                + "onlanguagechange,onmessage,onmessageerror,"
                + "onoffline,ononline,onpagehide,onpageshow,onpopstate,onrejectionhandled,"
                + "onstorage,onunhandledrejection,onunload,text,vLink",
            IE = "aLink,background,bgColor,bgProperties,bottomMargin,createTextRange(),leftMargin,link,noWrap,"
                + "onafterprint,onbeforeprint,onbeforeunload,onhashchange,onmessage,onoffline,ononline,onpagehide,"
                + "onpageshow,onpopstate,onresize,onstorage,onunload,rightMargin,scroll,text,topMargin,"
                + "vLink")
    @HtmlUnitNYI(IE = "aLink,background,bgColor,createTextRange(),link,onafterprint,onbeforeprint,onbeforeunload,"
                + "onhashchange,onmessage,onoffline,ononline,onpagehide,onpageshow,onpopstate,onresize,"
                + "onstorage,onunload,text,vLink")
    public void body() throws Exception {
        test("body");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBold}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void b() throws Exception {
        test("b");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clear")
    public void br() throws Exception {
        test("br");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "checkValidity(),disabled,form,formAction,formEnctype,formMethod,formNoValidate,"
                + "formTarget,labels,name,reportValidity(),setCustomValidity(),type,validationMessage,validity,"
                + "value,willValidate",
            EDGE = "checkValidity(),disabled,form,formAction,formEnctype,formMethod,formNoValidate,"
                + "formTarget,labels,name,reportValidity(),setCustomValidity(),type,validationMessage,validity,"
                + "value,willValidate",
            FF = "autofocus,checkValidity(),disabled,form,formAction,formEnctype,formMethod,formNoValidate,"
                + "formTarget,labels,name,reportValidity(),setCustomValidity(),type,validationMessage,validity,"
                + "value,willValidate",
            FF78 = "autofocus,checkValidity(),disabled,form,formAction,formEnctype,formMethod,formNoValidate,"
                + "formTarget,labels,name,reportValidity(),setCustomValidity(),type,validationMessage,validity,"
                + "value,willValidate",
            IE = "autofocus,checkValidity(),createTextRange(),form,formAction,formEnctype,formMethod,"
                + "formNoValidate,formTarget,name,setCustomValidity(),status,type,validationMessage,validity,value,"
                + "willValidate")
    @HtmlUnitNYI(CHROME = "checkValidity(),disabled,form,labels,name,type,value",
            EDGE = "checkValidity(),disabled,form,labels,name,type,value",
            FF78 = "checkValidity(),disabled,form,labels,name,type,value",
            FF = "checkValidity(),disabled,form,labels,name,type,value",
            IE = "checkValidity(),createTextRange(),form,name,type,value")
    public void button() throws Exception {
        test("button");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCanvas}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "captureStream(),getContext(),height,toBlob(),"
                    + "toDataURL(),transferControlToOffscreen(),width",
            EDGE = "captureStream(),getContext(),height,toBlob(),"
                    + "toDataURL(),transferControlToOffscreen(),width",
            FF = "captureStream(),getContext(),height,"
                    + "mozOpaque,mozPrintCallback,toBlob(),toDataURL(),width",
            FF78 = "captureStream(),getContext(),height,"
                    + "mozOpaque,mozPrintCallback,toBlob(),toDataURL(),width",
            IE = "getContext(),height,msToBlob(),toDataURL(),width")
    @HtmlUnitNYI(CHROME = "getContext(),height,toDataURL(),width",
            EDGE = "getContext(),height,toDataURL(),width",
            FF78 = "getContext(),height,toDataURL(),width",
            FF = "getContext(),height,toDataURL(),width",
            IE = "getContext(),height,toDataURL(),width")
    public void canvas() throws Exception {
        test("canvas");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align",
            IE = "align,vAlign")
    public void caption() throws Exception {
        test("caption");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCenter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,clear,width")
    public void center() throws Exception {
        test("center");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCitation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void cite() throws Exception {
        test("cite");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void code() throws Exception {
        test("code");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCommand}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void command() throws Exception {
        test("command");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDataList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("options")
    public void datalist() throws Exception {
        test("datalist");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinition}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void dfn() throws Exception {
        test("dfn");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "noWrap")
    public void dd() throws Exception {
        test("dd");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDeletedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cite,dateTime")
    public void del() throws Exception {
        test("del");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDetails}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "open",
            IE = "-")
    public void details() throws Exception {
        test("details");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDialog}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            CHROME = "close(),open,returnValue,show(),showModal()",
            EDGE = "close(),open,returnValue,show(),showModal()")
    @HtmlUnitNYI(CHROME = "-", EDGE = "-")
    public void dialog() throws Exception {
        test("dialog");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDirectory}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "compact",
            IE = "compact,type")
    @HtmlUnitNYI(IE = "compact")
    public void dir() throws Exception {
        test("dir");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDivision}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align",
            IE = "align,noWrap")
    public void div() throws Exception {
        test("div");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("compact")
    public void dl() throws Exception {
        test("dl");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "noWrap")
    public void dt() throws Exception {
        test("dt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,getSVGDocument(),height,name,src,type,width",
            IE = "getSVGDocument(),height,msPlayToDisabled,msPlayToPreferredSourceUri,msPlayToPrimary,name,palette,"
                + "pluginspage,readyState,src,units,"
                + "width")
    @HtmlUnitNYI(CHROME = "align,height,name,width",
            EDGE = "align,height,name,width",
            FF78 = "align,height,name,width",
            FF = "align,height,name,width",
            IE = "height,name,width")
    public void embed() throws Exception {
        test("embed");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlEmphasis}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void em() throws Exception {
        test("em");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFieldSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "checkValidity(),disabled,elements,form,name,reportValidity(),setCustomValidity(),type,"
                + "validationMessage,validity,willValidate",
            IE = "align,checkValidity(),form,setCustomValidity(),validationMessage,validity,willValidate")
    @HtmlUnitNYI(CHROME = "checkValidity(),disabled,form,name",
            EDGE = "checkValidity(),disabled,form,name",
            FF78 = "checkValidity(),disabled,form,name",
            FF = "checkValidity(),disabled,form,name",
            IE = "align,checkValidity(),form")
    public void fieldset() throws Exception {
        test("fieldset");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFigureCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void figcaption() throws Exception {
        test("figcaption");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFigure}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void figure() throws Exception {
        test("figure");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("color,face,size")
    public void font() throws Exception {
        test("font");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlForm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "acceptCharset,action,autocomplete,checkValidity(),elements,encoding,enctype,length,method,name,"
                + "noValidate,reportValidity(),requestSubmit(),reset(),submit(),"
                + "target",
            EDGE = "acceptCharset,action,autocomplete,checkValidity(),elements,encoding,enctype,length,method,name,"
                + "noValidate,reportValidity(),requestSubmit(),reset(),submit(),"
                + "target",
            FF = "acceptCharset,action,autocomplete,checkValidity(),elements,encoding,enctype,length,method,name,"
                + "noValidate,reportValidity(),requestSubmit(),reset(),submit(),"
                + "target",
            FF78 = "acceptCharset,action,autocomplete,checkValidity(),elements,encoding,enctype,length,method,name,"
                + "noValidate,reportValidity(),requestSubmit(),reset(),submit(),"
                + "target",
            IE = "acceptCharset,action,autocomplete,checkValidity(),elements,encoding,enctype,item(),length,method,"
                + "name,namedItem(),noValidate,reset(),submit(),"
                + "target")
    @HtmlUnitNYI(CHROME = "action,checkValidity(),elements,encoding,enctype,length,method,name,"
                    + "requestSubmit(),reset(),submit(),target",
            EDGE = "action,checkValidity(),elements,encoding,enctype,length,method,name,"
                    + "requestSubmit(),reset(),submit(),target",
            FF78 = "action,checkValidity(),elements,encoding,enctype,length,method,name,"
                    + "requestSubmit(),reset(),submit(),target",
            FF = "action,checkValidity(),elements,encoding,enctype,length,method,name,"
                    + "requestSubmit(),reset(),submit(),target",
            IE = "action,checkValidity(),elements,encoding,enctype,item(),length,method,name,reset(),submit(),target")
    public void form() throws Exception {
        test("form");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void footer() throws Exception {
        test("footer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "contentDocument,contentWindow,frameBorder,longDesc,marginHeight,marginWidth,"
                + "name,noResize,scrolling,"
                + "src",
            IE = "border,borderColor,contentDocument,contentWindow,frameBorder,frameSpacing,getSVGDocument(),"
                + "height,longDesc,marginHeight,marginWidth,name,noResize,scrolling,security,src,"
                + "width")
    @HtmlUnitNYI(CHROME = "contentDocument,contentWindow,name,src",
            EDGE = "contentDocument,contentWindow,name,src",
            FF78 = "contentDocument,contentWindow,name,src",
            FF = "contentDocument,contentWindow,name,src",
            IE = "border,contentDocument,contentWindow,name,src")
    public void frame() throws Exception {
        test("frame");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFrameSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "cols,onafterprint,onbeforeprint,onbeforeunload,onhashchange,onlanguagechange,"
                + "onmessage,onmessageerror,onoffline,ononline,onpagehide,"
                + "onpageshow,onpopstate,onrejectionhandled,onstorage,onunhandledrejection,onunload,"
                + "rows",
            EDGE = "cols,onafterprint,onbeforeprint,onbeforeunload,onhashchange,onlanguagechange,"
                + "onmessage,onmessageerror,onoffline,ononline,onpagehide,"
                + "onpageshow,onpopstate,onrejectionhandled,onstorage,onunhandledrejection,onunload,"
                + "rows",
            FF = "cols,onafterprint,onbeforeprint,onbeforeunload,onhashchange,onlanguagechange,onmessage,"
                + "onmessageerror,onoffline,ononline,"
                + "onpagehide,onpageshow,onpopstate,onrejectionhandled,onstorage,onunhandledrejection,"
                + "onunload,rows",
            FF78 = "cols,onafterprint,onbeforeprint,onbeforeunload,onhashchange,onlanguagechange,onmessage,"
                + "onmessageerror,onoffline,ononline,"
                + "onpagehide,onpageshow,onpopstate,onrejectionhandled,onstorage,onunhandledrejection,onunload,"
                + "rows",
            IE = "border,borderColor,cols,frameBorder,frameSpacing,name,onafterprint,onbeforeprint,onbeforeunload,"
                + "onhashchange,onmessage,onoffline,ononline,onpagehide,onpageshow,onresize,onstorage,onunload,"
                + "rows")
    @HtmlUnitNYI(IE = "border,cols,onafterprint,onbeforeprint,onbeforeunload,onhashchange,onmessage,onoffline,"
                + "ononline,onpagehide,onpageshow,onresize,onstorage,onunload,rows")
    public void frameset() throws Exception {
        test("frameset");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHead}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "profile")
    @HtmlUnitNYI(IE = "-")
    public void head() throws Exception {
        test("head");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void header() throws Exception {
        test("header");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading1}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align",
            IE = "align,clear")
    public void h1() throws Exception {
        test("h1");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading2}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align",
            IE = "align,clear")
    public void h2() throws Exception {
        test("h2");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading3}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align",
            IE = "align,clear")
    public void h3() throws Exception {
        test("h3");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading4}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align",
            IE = "align,clear")
    public void h4() throws Exception {
        test("h4");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading5}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align",
            IE = "align,clear")
    public void h5() throws Exception {
        test("h5");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading6}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align",
            IE = "align,clear")
    public void h6() throws Exception {
        test("h6");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHorizontalRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("align,color,noShade,size,width")
    @HtmlUnitNYI(CHROME = "align,color,width",
            EDGE = "align,color,width",
            FF78 = "align,color,width",
            FF = "align,color,width",
            IE = "align,color,width")
    public void hr() throws Exception {
        test("hr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHtml}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("version")
    public void html() throws Exception {
        test("html");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInlineFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,allow,allowFullscreen,allowPaymentRequest,contentDocument,contentWindow,"
                + "csp,featurePolicy,frameBorder,getSVGDocument(),height,"
                + "loading,longDesc,marginHeight,marginWidth,name,"
                + "referrerPolicy,sandbox,scrolling,src,srcdoc,"
                + "width",
            EDGE = "align,allow,allowFullscreen,allowPaymentRequest,contentDocument,contentWindow,"
                + "csp,featurePolicy,frameBorder,getSVGDocument(),height,"
                + "loading,longDesc,marginHeight,marginWidth,name,"
                + "referrerPolicy,sandbox,scrolling,src,srcdoc,"
                + "width",
            FF = "align,allow,allowFullscreen,contentDocument,contentWindow,frameBorder,"
                + "getSVGDocument(),height,longDesc,marginHeight,marginWidth,name,referrerPolicy,"
                + "sandbox,scrolling,src,srcdoc,width",
            FF78 = "align,allow,allowFullscreen,allowPaymentRequest,contentDocument,contentWindow,frameBorder,"
                + "getSVGDocument(),height,longDesc,marginHeight,marginWidth,name,referrerPolicy,"
                + "sandbox,scrolling,src,srcdoc,width",
            IE = "align,border,contentDocument,contentWindow,frameBorder,frameSpacing,getSVGDocument(),height,"
                + "hspace,longDesc,marginHeight,marginWidth,name,noResize,sandbox,scrolling,security,src,vspace,"
                + "width")
    @HtmlUnitNYI(CHROME = "align,contentDocument,contentWindow,height,name,src,width",
            EDGE = "align,contentDocument,contentWindow,height,name,src,width",
            FF78 = "align,contentDocument,contentWindow,height,name,src,width",
            FF = "align,contentDocument,contentWindow,height,name,src,width",
            IE = "align,border,contentDocument,contentWindow,height,name,src,width")
    public void iframe() throws Exception {
        test("iframe");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInlineQuotation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "cite",
            IE = "cite,dateTime")
    public void q() throws Exception {
        test("q");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,alt,border,complete,crossOrigin,currentSrc,decode(),decoding,"
                + "height,hspace,isMap,loading,longDesc,lowsrc,name,"
                + "naturalHeight,naturalWidth,referrerPolicy,sizes,src,srcset,useMap,vspace,width,x,"
                + "y",
            EDGE = "align,alt,border,complete,crossOrigin,currentSrc,decode(),decoding,"
                + "height,hspace,isMap,loading,longDesc,lowsrc,name,"
                + "naturalHeight,naturalWidth,referrerPolicy,sizes,src,srcset,useMap,vspace,width,x,"
                + "y",
            FF = "align,alt,border,complete,crossOrigin,currentSrc,decode(),decoding,height,hspace,isMap,loading,"
                + "longDesc,lowsrc,name,naturalHeight,naturalWidth,referrerPolicy,sizes,src,srcset,"
                + "useMap,vspace,width,x,y",
            FF78 = "align,alt,border,complete,crossOrigin,currentSrc,decode(),decoding,height,hspace,isMap,"
                + "longDesc,lowsrc,name,naturalHeight,naturalWidth,referrerPolicy,sizes,src,srcset,"
                + "useMap,vspace,width,x,y",
            IE = "align,alt,border,complete,crossOrigin,dynsrc,fileCreatedDate,fileModifiedDate,fileUpdatedDate,"
                + "height,href,hspace,isMap,longDesc,loop,lowsrc,mimeType,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,name,nameProp,naturalHeight,naturalWidth,protocol,src,"
                + "start,useMap,vrml,vspace,"
                + "width")
    @HtmlUnitNYI(CHROME = "align,alt,border,complete,height,name,naturalHeight,naturalWidth,src,width",
            EDGE = "align,alt,border,complete,height,name,naturalHeight,naturalWidth,src,width",
            FF78 = "align,alt,border,complete,height,name,naturalHeight,naturalWidth,src,width",
            FF = "align,alt,border,complete,height,name,naturalHeight,naturalWidth,src,width",
            IE = "align,alt,border,complete,height,name,naturalHeight,naturalWidth,src,width")
    public void img() throws Exception {
        test("img");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "align,alt,border,complete,crossOrigin,dynsrc,fileCreatedDate,fileModifiedDate,fileUpdatedDate,"
                + "height,href,hspace,isMap,longDesc,loop,lowsrc,mimeType,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,name,nameProp,naturalHeight,naturalWidth,protocol,src,"
                + "start,useMap,vrml,vspace,"
                + "width")
    @HtmlUnitNYI(IE = "align,alt,border,complete,height,name,naturalHeight,naturalWidth,src,width")
    public void image() throws Exception {
        test("image");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cite,dateTime")
    public void ins() throws Exception {
        test("ins");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlIsIndex}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "action,form,prompt")
    @HtmlUnitNYI(IE = "-")
    public void isindex() throws Exception {
        test("isindex");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlItalic}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void i() throws Exception {
        test("i");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlKeyboard}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void kbd() throws Exception {
        test("kbd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,clear,width")
    public void keygen() throws Exception {
        test("keygen");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLabel}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "control,form,htmlFor",
            IE = "form,htmlFor")
    public void label() throws Exception {
        test("label");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void layer() throws Exception {
        test("layer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLegend}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("align,form")
    public void legend() throws Exception {
        test("legend");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlListing}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "width",
            IE = "cite,clear,width")
    public void listing() throws Exception {
        test("listing");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlListItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("type,value")
    @HtmlUnitNYI(CHROME = "-",
            EDGE = "-",
            FF78 = "-",
            FF = "-",
            IE = "-")
    public void li() throws Exception {
        test("li");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "as,charset,crossOrigin,disabled,href,hreflang,"
                + "imageSizes,imageSrcset,integrity,"
                + "media,referrerPolicy,rel,relList,rev,sheet,sizes,target,type",
            EDGE = "as,charset,crossOrigin,disabled,href,hreflang,"
                + "imageSizes,imageSrcset,integrity,"
                + "media,referrerPolicy,rel,relList,rev,sheet,sizes,target,type",
            FF = "as,charset,crossOrigin,disabled,href,hreflang,imageSizes,imageSrcset,integrity,"
                + "media,referrerPolicy,rel,relList,rev,sheet,sizes,target,type",
            FF78 = "as,charset,crossOrigin,disabled,href,hreflang,imageSizes,imageSrcset,integrity,"
                + "media,referrerPolicy,rel,relList,rev,sheet,sizes,target,type",
            IE = "charset,href,hreflang,media,rel,rev,sheet,target,type")
    @HtmlUnitNYI(CHROME = "disabled,href,rel,relList,rev,type",
            EDGE = "disabled,href,rel,relList,rev,type",
            FF78 = "disabled,href,rel,relList,rev,type",
            FF = "disabled,href,rel,relList,rev,type",
            IE = "href,rel,rev,type")
    public void link() throws Exception {
        test("link");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMain}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void main() throws Exception {
        test("main");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMap}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("areas,name")
    public void map() throws Exception {
        test("map");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMark}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void mark() throws Exception {
        test("mark");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMarquee}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "behavior,bgColor,direction,height,hspace,loop,scrollAmount,scrollDelay,start(),stop(),trueSpeed,"
                + "vspace,width",
            EDGE = "behavior,bgColor,direction,height,hspace,loop,scrollAmount,scrollDelay,start(),stop(),trueSpeed,"
                + "vspace,width",
            FF = "behavior,bgColor,direction,height,hspace,loop,onbounce,onfinish,onstart,scrollAmount,"
                + "scrollDelay,start(),stop(),trueSpeed,vspace,width",
            FF78 = "behavior,bgColor,direction,height,hspace,loop,onbounce,onfinish,onstart,scrollAmount,"
                + "scrollDelay,start(),stop(),trueSpeed,vspace,width",
            IE = "behavior,bgColor,direction,height,hspace,loop,onbounce,onfinish,onstart,scrollAmount,scrollDelay,"
                + "start(),stop(),trueSpeed,vspace,width")
    @HtmlUnitNYI(CHROME = "bgColor,height,width",
            EDGE = "bgColor,height,width",
            FF78 = "-",
            FF = "-",
            IE = "bgColor,height,width")
    public void marquee() throws Exception {
        test("marquee");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMenu}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "compact",
            FF78 = "compact,label,type",
            IE = "compact,type")
    public void menu() throws Exception {
        test("menu");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMenuItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            FF78 = "checked,defaultChecked,disabled,icon,label,radiogroup,type")
    @HtmlUnitNYI(FF78 = "-")
    public void menuitem() throws Exception {
        test("menuitem");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMeta}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "content,httpEquiv,name,scheme",
            IE = "charset,content,httpEquiv,name,scheme,url")
    public void meta() throws Exception {
        test("meta");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMeter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "high,labels,low,max,min,optimum,value",
            IE = "-")
    public void meter() throws Exception {
        test("meter");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMultiColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void multicol() throws Exception {
        test("multicol");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNav}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void nav() throws Exception {
        test("nav");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNextId}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "n")
    @HtmlUnitNYI(IE = "-")
    public void nextid() throws Exception {
        test("nextid");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void nobr() throws Exception {
        test("nobr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void noembed() throws Exception {
        test("noembed");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoFrames}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void noframes() throws Exception {
        test("noframes");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void nolayer() throws Exception {
        test("nolayer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void noscript() throws Exception {
        test("noscript");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlObject}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "align,archive,border,checkValidity(),code,codeBase,codeType,contentDocument,contentWindow,"
                + "data,declare,form,"
                + "getSVGDocument(),height,hspace,name,reportValidity(),setCustomValidity(),standby,type,useMap,"
                + "validationMessage,validity,vspace,width,willValidate",
            EDGE = "align,archive,border,checkValidity(),code,codeBase,codeType,contentDocument,contentWindow,"
                + "data,declare,form,"
                + "getSVGDocument(),height,hspace,name,reportValidity(),setCustomValidity(),standby,type,useMap,"
                + "validationMessage,validity,vspace,width,willValidate",
            FF = "align,archive,border,checkValidity(),code,codeBase,codeType,contentDocument,contentWindow,data,"
                + "declare,form,getSVGDocument(),height,hspace,name,reportValidity(),setCustomValidity(),standby,"
                + "type,useMap,validationMessage,validity,vspace,width,willValidate",
            FF78 = "align,archive,border,checkValidity(),code,codeBase,codeType,contentDocument,contentWindow,data,"
                + "declare,form,getSVGDocument(),height,hspace,name,reportValidity(),setCustomValidity(),standby,"
                + "type,useMap,validationMessage,validity,vspace,width,willValidate",
            IE = "align,alt,altHtml,archive,BaseHref,border,checkValidity(),classid,code,codeBase,codeType,"
                + "contentDocument,data,declare,form,getSVGDocument(),height,hspace,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,name,object,readyState,setCustomValidity(),standby,"
                + "type,useMap,validationMessage,validity,vspace,width,willValidate")
    @HtmlUnitNYI(CHROME = "align,border,checkValidity(),form,height,name,width",
            EDGE = "align,border,checkValidity(),form,height,name,width",
            FF78 = "align,border,checkValidity(),form,height,name,width",
            FF = "align,border,checkValidity(),form,height,name,width",
            IE = "align,alt,border,checkValidity(),classid,form,height,name,width")
    public void object() throws Exception {
        test("object");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOrderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "compact,reversed,start,type",
            IE = "compact,start,type")
    @HtmlUnitNYI(CHROME = "compact,type",
            EDGE = "compact,type",
            FF78 = "compact,type",
            FF = "compact,type",
            IE = "compact,type")
    public void ol() throws Exception {
        test("ol");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOptionGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "disabled,label",
            IE = "defaultSelected,form,index,label,selected,text,value")
    @HtmlUnitNYI(IE = "label")
    public void optgroup() throws Exception {
        test("optgroup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "defaultSelected,disabled,form,index,label,selected,text,value",
            IE = "defaultSelected,form,index,label,selected,text,value")
    public void option() throws Exception {
        test("option");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOutput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "checkValidity(),defaultValue,form,htmlFor,labels,name,reportValidity(),setCustomValidity(),type,"
                + "validationMessage,validity,value,"
                + "willValidate",
            IE = "-")
    @HtmlUnitNYI(CHROME = "checkValidity(),labels,name",
            EDGE = "checkValidity(),labels,name",
            FF78 = "checkValidity(),labels,name",
            FF = "checkValidity(),labels,name")
    public void output() throws Exception {
        test("output");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlParagraph}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align",
            IE = "align,clear")
    public void p() throws Exception {
        test("p");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlParameter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("name,type,value,valueType")
    public void param() throws Exception {
        test("param");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPlainText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,clear,width")
    public void plaintext() throws Exception {
        test("plaintext");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPreformattedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "width",
            IE = "cite,clear,width")
    public void pre() throws Exception {
        test("pre");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlProgress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "labels,max,position,value",
            IE = "form,max,position,value")
    @HtmlUnitNYI(CHROME = "labels,max,value",
            EDGE = "labels,max,value",
            FF78 = "labels,max,value",
            FF = "labels,max,value",
            IE = "max,value")
    public void progress() throws Exception {
        test("progress");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRp}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    @HtmlUnitNYI(IE = "-")
    public void rp() throws Exception {
        test("rp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRt}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    @HtmlUnitNYI(IE = "-")
    public void rt() throws Exception {
        test("rt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRuby}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    @HtmlUnitNYI(IE = "-")
    public void ruby() throws Exception {
        test("ruby");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlS}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void s() throws Exception {
        test("s");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void samp() throws Exception {
        test("samp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "async,charset,crossOrigin,defer,event,htmlFor,"
                + "integrity,noModule,referrerPolicy,src,text,type",
            EDGE = "async,charset,crossOrigin,defer,event,htmlFor,"
                + "integrity,noModule,referrerPolicy,src,text,type",
            FF = "async,charset,crossOrigin,defer,event,htmlFor,"
                + "integrity,noModule,referrerPolicy,src,text,type",
            FF78 = "async,charset,crossOrigin,defer,event,htmlFor,"
                + "integrity,noModule,referrerPolicy,src,text,type",
            IE = "async,charset,crossOrigin,defer,event,htmlFor,src,text,type")
    @HtmlUnitNYI(CHROME = "async,src,text,type",
            EDGE = "async,src,text,type",
            FF78 = "async,src,text,type",
            FF = "async,src,text,type",
            IE = "async,src,text,type")
    public void script() throws Exception {
        test("script");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void section() throws Exception {
        test("section");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "add(),autocomplete,checkValidity(),"
                + "disabled,form,item(),labels,length,multiple,name,namedItem(),"
                + "options,reportValidity(),required,selectedIndex,selectedOptions,setCustomValidity(),size,type,"
                + "validationMessage,validity,value,"
                + "willValidate",
            EDGE = "add(),autocomplete,checkValidity(),"
                + "disabled,form,item(),labels,length,multiple,name,namedItem(),"
                + "options,reportValidity(),required,selectedIndex,selectedOptions,setCustomValidity(),size,type,"
                + "validationMessage,validity,value,"
                + "willValidate",
            FF = "add(),autocomplete,autofocus,checkValidity(),disabled,form,item(),labels,length,multiple,name,"
                + "namedItem(),options,reportValidity(),required,selectedIndex,selectedOptions,setCustomValidity(),"
                + "size,type,validationMessage,validity,value,"
                + "willValidate",
            FF78 = "add(),autocomplete,autofocus,checkValidity(),disabled,form,item(),labels,length,multiple,name,"
                + "namedItem(),options,reportValidity(),required,selectedIndex,selectedOptions,setCustomValidity(),"
                + "size,type,validationMessage,validity,value,"
                + "willValidate",
            IE = "add(),autofocus,checkValidity(),form,item(),length,multiple,name,namedItem(),options,remove(),"
                + "required,selectedIndex,setCustomValidity(),size,type,validationMessage,validity,value,"
                + "willValidate")
    @HtmlUnitNYI(CHROME = "add(),checkValidity(),disabled,form,item(),labels,length,multiple,name,options,"
                + "required,selectedIndex,size,type,value",
            EDGE = "add(),checkValidity(),disabled,form,item(),labels,length,multiple,name,options,"
                + "required,selectedIndex,size,type,value",
            FF78 = "add(),checkValidity(),disabled,form,item(),labels,length,multiple,name,options,"
                + "required,selectedIndex,size,type,value",
            FF = "add(),checkValidity(),disabled,form,item(),labels,length,multiple,name,options,"
                + "required,selectedIndex,size,type,value",
            IE = "add(),checkValidity(),form,item(),length,multiple,name,options,remove(),"
                + "required,selectedIndex,size,type,value")
    public void select() throws Exception {
        test("select");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSmall}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void small() throws Exception {
        test("small");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSource}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "media,sizes,src,srcset,type",
            IE = "media,msKeySystem,src,type")
    @HtmlUnitNYI(CHROME = "-",
            EDGE = "-",
            FF78 = "-",
            FF = "-",
            IE = "-")
    public void source() throws Exception {
        test("source");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSpan}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void span() throws Exception {
        test("span");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStrike}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void strike() throws Exception {
        test("strike");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStrong}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void strong() throws Exception {
        test("strong");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStyle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "disabled,media,sheet,type",
            IE = "media,sheet,type")
    public void style() throws Exception {
        test("style");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSubscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void sub() throws Exception {
        test("sub");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSummary}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void summary() throws Exception {
        test("summary");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void sup() throws Exception {
        test("sup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSvg}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void svg() throws Exception {
        test("svg");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,bgColor,border,caption,cellPadding,cellSpacing,createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),frame,"
                + "insertRow(),rows,rules,summary,tBodies,tFoot,tHead,"
                + "width",
            IE = "align,background,bgColor,border,borderColor,borderColorDark,borderColorLight,caption,cellPadding,"
                + "cells,cellSpacing,cols,createCaption(),createTBody(),createTFoot(),createTHead(),deleteCaption(),"
                + "deleteRow(),deleteTFoot(),deleteTHead(),frame,height,insertRow(),moveRow(),rows,rules,summary,"
                + "tBodies,tFoot,tHead,"
                + "width")
    @HtmlUnitNYI(CHROME = "align,bgColor,border,caption,cellPadding,cellSpacing,createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),insertRow(),"
                + "rows,rules,summary,tBodies,tFoot,tHead,width",
            EDGE = "align,bgColor,border,caption,cellPadding,cellSpacing,createCaption(),createTBody(),"
                    + "createTFoot(),createTHead(),deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),insertRow(),"
                    + "rows,rules,summary,tBodies,tFoot,tHead,width",
            FF78 = "align,bgColor,border,caption,cellPadding,cellSpacing,createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),insertRow(),"
                + "rows,rules,summary,tBodies,tFoot,tHead,width",
            FF = "align,bgColor,border,caption,cellPadding,cellSpacing,createCaption(),createTBody(),"
                + "createTFoot(),createTHead(),deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),insertRow(),"
                + "rows,rules,summary,tBodies,tFoot,tHead,width",
            IE = "align,bgColor,border,borderColor,borderColorDark,borderColorLight,caption,cellPadding,"
                + "cellSpacing,createCaption(),createTBody(),createTFoot(),createTHead(),deleteCaption(),"
                + "deleteRow(),deleteTFoot(),deleteTHead(),insertRow(),moveRow(),rows,rules,summary,tBodies,"
                + "tFoot,tHead,width")
    public void table() throws Exception {
        test("table");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("align,ch,chOff,span,vAlign,width")
    public void col() throws Exception {
        test("col");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableColumnGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("align,ch,chOff,span,vAlign,width")
    public void colgroup() throws Exception {
        test("colgroup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,ch,chOff,deleteRow(),insertRow(),rows,vAlign",
            IE = "align,bgColor,ch,chOff,deleteRow(),insertRow(),moveRow(),rows,vAlign")
    public void tbody() throws Exception {
        test("tbody");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableDataCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "abbr,align,axis,bgColor,cellIndex,ch,chOff,colSpan,headers,height,noWrap,rowSpan,scope,vAlign,"
                + "width",
            IE = "abbr,align,axis,background,bgColor,borderColor,borderColorDark,borderColorLight,cellIndex,ch,"
                + "chOff,colSpan,headers,height,noWrap,rowSpan,scope,vAlign,"
                + "width")
    @HtmlUnitNYI(IE = "abbr,align,axis,bgColor,borderColor,borderColorDark,borderColorLight,cellIndex,ch,chOff,"
                + "colSpan,headers,height,noWrap,rowSpan,scope,vAlign,width")
    public void td() throws Exception {
        test("td");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "abbr,align,axis,bgColor,cellIndex,ch,chOff,colSpan,headers,height,noWrap,rowSpan,scope,vAlign,"
                + "width",
            IE = "abbr,align,axis,background,bgColor,borderColor,borderColorDark,borderColorLight,cellIndex,ch,"
                + "chOff,colSpan,headers,height,noWrap,rowSpan,scope,vAlign,"
                + "width")
    @HtmlUnitNYI(IE = "abbr,align,axis,bgColor,borderColor,borderColorDark,borderColorLight,cellIndex,ch,chOff,"
                + "colSpan,headers,height,noWrap,rowSpan,scope,vAlign,width")
    public void th() throws Exception {
        test("th");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableRow}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,bgColor,cells,ch,chOff,deleteCell(),insertCell(),rowIndex,sectionRowIndex,vAlign",
            IE = "align,bgColor,borderColor,borderColorDark,borderColorLight,cells,ch,chOff,deleteCell(),height,"
                + "insertCell(),rowIndex,sectionRowIndex,"
                + "vAlign")
    @HtmlUnitNYI(IE = "align,bgColor,borderColor,borderColorDark,borderColorLight,cells,ch,chOff,deleteCell(),"
                + "insertCell(),rowIndex,sectionRowIndex,vAlign")
    public void tr() throws Exception {
        test("tr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTextArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "autocomplete,checkValidity(),cols,defaultValue,dirName,disabled,form,labels,"
                + "maxLength,minLength,name,placeholder,readOnly,reportValidity(),required,rows,select(),"
                + "selectionDirection,selectionEnd,selectionStart,setCustomValidity(),setRangeText(),"
                + "setSelectionRange(),textLength,type,validationMessage,validity,value,willValidate,"
                + "wrap",
            EDGE = "autocomplete,checkValidity(),cols,defaultValue,dirName,disabled,form,labels,"
                + "maxLength,minLength,name,placeholder,readOnly,reportValidity(),required,rows,select(),"
                + "selectionDirection,selectionEnd,selectionStart,setCustomValidity(),setRangeText(),"
                + "setSelectionRange(),textLength,type,validationMessage,validity,value,willValidate,"
                + "wrap",
            FF = "autocomplete,autofocus,checkValidity(),cols,defaultValue,disabled,form,"
                + "labels,maxLength,minLength,name,placeholder,"
                + "readOnly,reportValidity(),required,rows,select(),selectionDirection,selectionEnd,"
                + "selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),"
                + "textLength,type,validationMessage,validity,value,willValidate,wrap",
            FF78 = "autocomplete,autofocus,checkValidity(),cols,defaultValue,disabled,form,"
                + "labels,maxLength,minLength,name,placeholder,"
                + "readOnly,reportValidity(),required,rows,select(),selectionDirection,selectionEnd,"
                + "selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),"
                + "textLength,type,validationMessage,validity,value,willValidate,wrap",
            IE = "autofocus,checkValidity(),cols,createTextRange(),defaultValue,form,maxLength,name,placeholder,"
                + "readOnly,required,rows,select(),selectionEnd,selectionStart,setCustomValidity(),"
                + "setSelectionRange(),status,type,validationMessage,validity,value,willValidate,"
                + "wrap")
    @HtmlUnitNYI(CHROME = "checkValidity(),cols,defaultValue,disabled,form,labels,maxLength,minLength,name,"
                + "placeholder,readOnly,required,rows,select(),selectionEnd,selectionStart,setSelectionRange(),"
                + "textLength,type,value",
            EDGE = "checkValidity(),cols,defaultValue,disabled,form,labels,maxLength,minLength,name,"
                + "placeholder,readOnly,required,rows,select(),selectionEnd,selectionStart,setSelectionRange(),"
                + "textLength,type,value",
            FF78 = "checkValidity(),cols,defaultValue,disabled,form,labels,maxLength,minLength,name,placeholder,"
                + "readOnly,required,rows,select(),selectionEnd,selectionStart,setSelectionRange(),"
                + "textLength,type,value",
            FF = "checkValidity(),cols,defaultValue,disabled,form,labels,maxLength,minLength,name,placeholder,"
                + "readOnly,required,rows,select(),selectionEnd,selectionStart,setSelectionRange(),"
                + "textLength,type,value",
            IE = "checkValidity(),cols,createTextRange(),defaultValue,form,maxLength,name,placeholder,readOnly,"
                + "required,rows,select(),selectionEnd,selectionStart,setSelectionRange(),type,value")
    public void textarea() throws Exception {
        test("textarea");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,ch,chOff,deleteRow(),insertRow(),rows,vAlign",
            IE = "align,bgColor,ch,chOff,deleteRow(),insertRow(),moveRow(),rows,vAlign")
    public void tfoot() throws Exception {
        test("tfoot");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,ch,chOff,deleteRow(),insertRow(),rows,vAlign",
            IE = "align,bgColor,ch,chOff,deleteRow(),insertRow(),moveRow(),rows,vAlign")
    public void thead() throws Exception {
        test("thead");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTeletype}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void tt() throws Exception {
        test("tt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTime}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "dateTime",
            IE = "-")
    public void time() throws Exception {
        test("time");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTitle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text")
    public void title() throws Exception {
        test("title");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTrack}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("default,ERROR,kind,label,LOADED,LOADING,NONE,readyState,src,srclang,track")
    @HtmlUnitNYI(CHROME = "ERROR,LOADED,LOADING,NONE",
            EDGE = "ERROR,LOADED,LOADING,NONE",
            FF78 = "ERROR,LOADED,LOADING,NONE",
            FF = "ERROR,LOADED,LOADING,NONE",
            IE = "ERROR,LOADED,LOADING,NONE")
    public void track() throws Exception {
        test("track");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlUnderlined}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void u() throws Exception {
        test("u");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlUnorderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("compact,type")
    public void ul() throws Exception {
        test("ul");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlVariable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            IE = "cite,dateTime")
    public void var() throws Exception {
        test("var");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlVideo}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "addTextTrack(),autoplay,buffered,cancelVideoFrameCallback(),"
                + "canPlayType(),captureStream(),controls,controlsList,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,disablePictureInPicture,disableRemotePlayback,duration,"
                + "ended,error,getVideoPlaybackQuality(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,"
                + "HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,height,load(),loop,mediaKeys,muted,NETWORK_EMPTY,"
                + "NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,onencrypted,"
                + "onenterpictureinpicture,onleavepictureinpicture,"
                + "onwaitingforkey,pause(),paused,play(),playbackRate,played,playsInline,"
                + "poster,preload,preservesPitch,"
                + "readyState,remote,requestPictureInPicture(),requestVideoFrameCallback(),"
                + "seekable,seeking,setMediaKeys(),setSinkId(),sinkId,src,srcObject,"
                + "textTracks,videoHeight,videoWidth,"
                + "volume,webkitAudioDecodedByteCount,webkitDecodedFrameCount,"
                + "webkitDisplayingFullscreen,webkitDroppedFrameCount,"
                + "webkitEnterFullscreen(),webkitEnterFullScreen(),"
                + "webkitExitFullscreen(),webkitExitFullScreen(),"
                + "webkitSupportsFullscreen,webkitVideoDecodedByteCount,width",
            EDGE = "addTextTrack(),autoplay,buffered,cancelVideoFrameCallback(),canPlayType(),captureStream(),"
                + "controls,controlsList,crossOrigin,currentSrc,currentTime,defaultMuted,defaultPlaybackRate,"
                + "disablePictureInPicture,disableRemotePlayback,duration,ended,error,getVideoPlaybackQuality(),"
                + "HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,height,load(),"
                + "loop,mediaKeys,muted,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,"
                + "onencrypted,onenterpictureinpicture,onleavepictureinpicture,onwaitingforkey,pause(),paused,"
                + "play(),playbackRate,played,playsInline,poster,preload,preservesPitch,readyState,remote,"
                + "requestPictureInPicture(),requestVideoFrameCallback(),seekable,seeking,setMediaKeys(),"
                + "setSinkId(),sinkId,src,srcObject,textTracks,videoHeight,videoWidth,volume,"
                + "webkitAudioDecodedByteCount,webkitDecodedFrameCount,webkitDisplayingFullscreen,"
                + "webkitDroppedFrameCount,webkitEnterFullscreen(),webkitEnterFullScreen(),webkitExitFullscreen(),"
                + "webkitExitFullScreen(),webkitSupportsFullscreen,webkitVideoDecodedByteCount,"
                + "width",
            FF = "addTextTrack(),autoplay,buffered,canPlayType(),controls,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,duration,ended,error,fastSeek(),getVideoPlaybackQuality(),"
                + "HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,height,load(),"
                + "loop,mediaKeys,mozAudioCaptured,mozCaptureStream(),mozCaptureStreamUntilEnded(),mozDecodedFrames,"
                + "mozFragmentEnd,mozFrameDelay,mozGetMetadata(),mozHasAudio,mozPaintedFrames,mozParsedFrames,"
                + "mozPresentedFrames,mozPreservesPitch,muted,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,"
                + "NETWORK_NO_SOURCE,networkState,onencrypted,onwaitingforkey,pause(),paused,play(),playbackRate,"
                + "played,poster,preload,readyState,seekable,seeking,seekToNextFrame(),setMediaKeys(),src,"
                + "srcObject,textTracks,videoHeight,videoWidth,volume,width",
            FF78 = "addTextTrack(),autoplay,buffered,canPlayType(),controls,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,duration,ended,error,fastSeek(),getVideoPlaybackQuality(),"
                + "HAVE_CURRENT_DATA,"
                + "HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,height,load(),loop,mediaKeys,"
                + "mozAudioCaptured,"
                + "mozCaptureStream(),mozCaptureStreamUntilEnded(),"
                + "mozDecodedFrames,mozFragmentEnd,mozFrameDelay,mozGetMetadata(),mozHasAudio,mozPaintedFrames,"
                + "mozParsedFrames,mozPresentedFrames,mozPreservesPitch,muted,NETWORK_EMPTY,"
                + "NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,onencrypted,"
                + "onwaitingforkey,pause(),paused,play(),playbackRate,"
                + "played,poster,preload,readyState,seekable,seeking,seekToNextFrame(),setMediaKeys(),"
                + "src,srcObject,textTracks,videoHeight,videoWidth,volume,"
                + "width",
            IE = "addTextTrack(),audioTracks,autobuffer,autoplay,buffered,canPlayType(),controls,currentSrc,"
                + "currentTime,defaultPlaybackRate,duration,ended,error,getVideoPlaybackQuality(),"
                + "HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,"
                + "HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,height,initialTime,load(),loop,"
                + "msGraphicsTrustStatus,msKeys,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,msSetMediaKeys(),msZoom,"
                + "muted,NETWORK_EMPTY,NETWORK_IDLE,"
                + "NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,"
                + "onmsneedkey,pause(),paused,play(),playbackRate,played,poster,"
                + "preload,readyState,seekable,seeking,src,textTracks,videoHeight,videoWidth,volume,"
                + "width")
    @HtmlUnitNYI(CHROME = "canPlayType(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,"
                + "HAVE_NOTHING,height,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,pause(),"
                + "play(),width",
            EDGE = "canPlayType(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,"
                + "HAVE_NOTHING,height,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,pause(),"
                + "play(),width",
            FF78 = "canPlayType(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,"
                + "HAVE_NOTHING,height,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,pause(),"
                + "play(),width",
            FF = "canPlayType(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,"
                + "HAVE_NOTHING,height,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,pause(),"
                + "play(),width",
            IE = "canPlayType(),HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,"
                + "HAVE_NOTHING,height,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,pause(),"
                + "play(),width")
    public void video() throws Exception {
        test("video");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlWordBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void wbr() throws Exception {
        test("wbr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlExample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "width",
            IE = "cite,clear,width")
    public void xmp() throws Exception {
        test("xmp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "accept,align,alt,autocomplete,checked,checkValidity(),"
                + "defaultChecked,defaultValue,"
                + "dirName,disabled,files,form,formAction,formEnctype,formMethod,formNoValidate,formTarget,height,"
                + "incremental,indeterminate,labels,list,max,maxLength,min,minLength,multiple,name,pattern,"
                + "placeholder,readOnly,reportValidity(),required,select(),selectionDirection,selectionEnd,"
                + "selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),size,src,step,stepDown(),"
                + "stepUp(),type,useMap,validationMessage,validity,value,valueAsDate,valueAsNumber,webkitdirectory,"
                + "webkitEntries,width,willValidate",
            EDGE = "accept,align,alt,autocomplete,checked,checkValidity(),"
                + "defaultChecked,defaultValue,"
                + "dirName,disabled,files,form,formAction,formEnctype,formMethod,formNoValidate,formTarget,height,"
                + "incremental,indeterminate,labels,list,max,maxLength,min,minLength,multiple,name,pattern,"
                + "placeholder,readOnly,reportValidity(),required,select(),selectionDirection,selectionEnd,"
                + "selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),size,src,step,stepDown(),"
                + "stepUp(),type,useMap,validationMessage,validity,value,valueAsDate,valueAsNumber,webkitdirectory,"
                + "webkitEntries,width,willValidate",
            FF = "accept,align,alt,autocomplete,autofocus,checked,checkValidity(),defaultChecked,defaultValue,"
                + "disabled,files,form,formAction,formEnctype,formMethod,formNoValidate,formTarget,height,"
                + "indeterminate,labels,list,max,maxLength,min,minLength,mozIsTextField(),multiple,name,"
                + "pattern,placeholder,readOnly,reportValidity(),required,select(),selectionDirection,"
                + "selectionEnd,selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),size,"
                + "src,step,stepDown(),stepUp(),textLength,type,useMap,validationMessage,validity,value,"
                + "valueAsDate,valueAsNumber,webkitdirectory,webkitEntries,width,willValidate",
            FF78 = "accept,align,alt,autocomplete,autofocus,checked,checkValidity(),defaultChecked,defaultValue,"
                + "disabled,files,form,formAction,formEnctype,formMethod,formNoValidate,formTarget,height,"
                + "indeterminate,labels,list,max,maxLength,min,minLength,mozIsTextField(),multiple,name,"
                + "pattern,placeholder,readOnly,reportValidity(),required,"
                + "select(),selectionDirection,selectionEnd,selectionStart,setCustomValidity(),"
                + "setRangeText(),setSelectionRange(),size,src,step,stepDown(),stepUp(),textLength,type,useMap,"
                + "validationMessage,validity,value,valueAsDate,valueAsNumber,webkitdirectory,webkitEntries,"
                + "width,willValidate",
            IE = "accept,align,alt,autocomplete,autofocus,border,checked,checkValidity(),complete,"
                + "createTextRange(),defaultChecked,defaultValue,dynsrc,files,form,formAction,formEnctype,"
                + "formMethod,formNoValidate,formTarget,height,hspace,indeterminate,list,loop,lowsrc,max,maxLength,"
                + "min,multiple,name,pattern,placeholder,readOnly,required,select(),selectionEnd,selectionStart,"
                + "setCustomValidity(),setSelectionRange(),size,src,start,status,step,stepDown(),stepUp(),type,"
                + "useMap,validationMessage,validity,value,valueAsNumber,vrml,vspace,width,willValidate")
    @HtmlUnitNYI(CHROME = "accept,align,alt,autocomplete,checked,checkValidity(),defaultChecked,defaultValue,"
                + "disabled,files,form,height,labels,max,maxLength,min,minLength,name,placeholder,readOnly,"
                + "required,select(),selectionEnd,selectionStart,setSelectionRange(),size,src,step,type,value,"
                + "width",
            EDGE = "accept,align,alt,autocomplete,checked,checkValidity(),defaultChecked,defaultValue,"
                + "disabled,files,form,height,labels,max,maxLength,min,minLength,name,placeholder,readOnly,"
                + "required,select(),selectionEnd,selectionStart,setSelectionRange(),size,src,step,type,value,"
                + "width",
            FF78 = "accept,align,alt,autocomplete,checked,checkValidity(),defaultChecked,defaultValue,disabled,"
                + "files,form,height,labels,max,maxLength,min,minLength,name,placeholder,readOnly,required,"
                + "select(),selectionEnd,selectionStart,setSelectionRange(),size,src,step,textLength,type,value,width",
            FF = "accept,align,alt,autocomplete,checked,checkValidity(),defaultChecked,defaultValue,disabled,"
                + "files,form,height,labels,max,maxLength,min,minLength,name,placeholder,readOnly,required,"
                + "select(),selectionEnd,selectionStart,setSelectionRange(),size,src,step,textLength,type,value,width",
            IE = "accept,align,alt,autocomplete,border,checked,checkValidity(),createTextRange(),"
                + "defaultChecked,defaultValue,files,form,height,max,maxLength,min,name,placeholder,readOnly,"
                + "required,select(),selectionEnd,selectionStart,setSelectionRange(),size,src,step,type,"
                + "value,width")
    public void input() throws Exception {
        test("input");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "value",
            IE = "-")
    public void data() throws Exception {
        test("data");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlContent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "-",
            CHROME = "getDistributedNodes(),select",
            EDGE = "getDistributedNodes(),select")
    @HtmlUnitNYI(CHROME = "-", EDGE = "-")
    public void content() throws Exception {
        test("content");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPicture}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    public void picutre() throws Exception {
        test("picture");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTemplate}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "content",
            IE = "-")
    public void template() throws Exception {
        test("template");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,charCode,code,"
                + "composed,composedPath(),ctrlKey,currentTarget,defaultPrevented,detail,DOM_KEY_LOCATION_LEFT,"
                + "DOM_KEY_LOCATION_NUMPAD,DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,eventPhase,"
                + "getModifierState(),"
                + "initEvent(),initKeyboardEvent(),initUIEvent(),isComposing,isTrusted,key,keyCode,location,metaKey,"
                + "NONE,path,preventDefault(),repeat,returnValue,shiftKey,sourceCapabilities,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which",
            EDGE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,charCode,code,"
                + "composed,composedPath(),ctrlKey,currentTarget,defaultPrevented,detail,DOM_KEY_LOCATION_LEFT,"
                + "DOM_KEY_LOCATION_NUMPAD,DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,eventPhase,"
                + "getModifierState(),"
                + "initEvent(),initKeyboardEvent(),initUIEvent(),isComposing,isTrusted,key,keyCode,location,metaKey,"
                + "NONE,path,preventDefault(),repeat,returnValue,shiftKey,sourceCapabilities,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which",
            FF = {"ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "charCode,code,composed,composedPath(),CONTROL_MASK,ctrlKey,currentTarget,defaultPrevented,detail,"
                + "DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,"
                + "DOM_VK_0,DOM_VK_1,DOM_VK_2,DOM_VK_3,DOM_VK_4,DOM_VK_5,DOM_VK_6,DOM_VK_7,DOM_VK_8,DOM_VK_9,DOM_VK_A,"
                + "DOM_VK_ACCEPT,DOM_VK_ADD,DOM_VK_ALT,DOM_VK_ALTGR,DOM_VK_AMPERSAND,DOM_VK_ASTERISK,DOM_VK_AT,"
                + "DOM_VK_ATTN,DOM_VK_B,DOM_VK_BACK_QUOTE,DOM_VK_BACK_SLASH,DOM_VK_BACK_SPACE,DOM_VK_C,"
                + "DOM_VK_CANCEL,DOM_VK_CAPS_LOCK,"
                + "DOM_VK_CIRCUMFLEX,DOM_VK_CLEAR,DOM_VK_CLOSE_BRACKET,DOM_VK_CLOSE_CURLY_BRACKET,DOM_VK_CLOSE_PAREN,"
                + "DOM_VK_COLON,DOM_VK_COMMA,DOM_VK_CONTEXT_MENU,DOM_VK_CONTROL,DOM_VK_CONVERT,DOM_VK_CRSEL,DOM_VK_D,"
                + "DOM_VK_DECIMAL,DOM_VK_DELETE,DOM_VK_DIVIDE,DOM_VK_DOLLAR,DOM_VK_DOUBLE_QUOTE,DOM_VK_DOWN,DOM_VK_E,"
                + "DOM_VK_EISU,DOM_VK_END,DOM_VK_EQUALS,DOM_VK_EREOF,DOM_VK_ESCAPE,DOM_VK_EXCLAMATION,DOM_VK_EXECUTE,"
                + "DOM_VK_EXSEL,DOM_VK_F,DOM_VK_F1,DOM_VK_F10,DOM_VK_F11,DOM_VK_F12,DOM_VK_F13,DOM_VK_F14,DOM_VK_F15,"
                + "DOM_VK_F16,DOM_VK_F17,DOM_VK_F18,DOM_VK_F19,DOM_VK_F2,DOM_VK_F20,DOM_VK_F21,DOM_VK_F22,DOM_VK_F23,"
                + "DOM_VK_F24,DOM_VK_F3,DOM_VK_F4,DOM_VK_F5,DOM_VK_F6,DOM_VK_F7,DOM_VK_F8,DOM_VK_F9,DOM_VK_FINAL,"
                + "DOM_VK_G,DOM_VK_GREATER_THAN,DOM_VK_H,DOM_VK_HANGUL,DOM_VK_HANJA,DOM_VK_HASH,DOM_VK_HELP,"
                + "DOM_VK_HOME,DOM_VK_HYPHEN_MINUS,DOM_VK_I,DOM_VK_INSERT,DOM_VK_J,DOM_VK_JUNJA,DOM_VK_K,"
                + "DOM_VK_KANA,DOM_VK_KANJI,DOM_VK_L,DOM_VK_LEFT,DOM_VK_LESS_THAN,DOM_VK_M,DOM_VK_META,"
                + "DOM_VK_MODECHANGE,DOM_VK_MULTIPLY,DOM_VK_N,DOM_VK_NONCONVERT,DOM_VK_NUM_LOCK,DOM_VK_NUMPAD0,"
                + "DOM_VK_NUMPAD1,DOM_VK_NUMPAD2,DOM_VK_NUMPAD3,DOM_VK_NUMPAD4,DOM_VK_NUMPAD5,DOM_VK_NUMPAD6,"
                + "DOM_VK_NUMPAD7,DOM_VK_NUMPAD8,DOM_VK_NUMPAD9,DOM_VK_O,DOM_VK_OPEN_BRACKET,DOM_VK_OPEN_CURLY_BRACKET,"
                + "DOM_VK_OPEN_PAREN",
                "DOM_VK_P,DOM_VK_PA1,DOM_VK_PAGE_DOWN,DOM_VK_PAGE_UP,DOM_VK_PAUSE,DOM_VK_PERCENT,"
                    + "DOM_VK_PERIOD,DOM_VK_PIPE,DOM_VK_PLAY,DOM_VK_PLUS,"
                    + "DOM_VK_PRINT,DOM_VK_PRINTSCREEN,DOM_VK_PROCESSKEY,"
                    + "DOM_VK_Q,DOM_VK_QUESTION_MARK,DOM_VK_QUOTE,DOM_VK_R,DOM_VK_RETURN,DOM_VK_RIGHT,DOM_VK_S,"
                    + "DOM_VK_SCROLL_LOCK,DOM_VK_SELECT,DOM_VK_SEMICOLON,DOM_VK_SEPARATOR,DOM_VK_SHIFT,DOM_VK_SLASH,"
                    + "DOM_VK_SLEEP,DOM_VK_SPACE,DOM_VK_SUBTRACT,DOM_VK_T,DOM_VK_TAB,DOM_VK_TILDE,DOM_VK_U,"
                    + "DOM_VK_UNDERSCORE,DOM_VK_UP,DOM_VK_V,DOM_VK_VOLUME_DOWN,DOM_VK_VOLUME_MUTE,DOM_VK_VOLUME_UP,"
                    + "DOM_VK_W,DOM_VK_WIN,DOM_VK_WIN_ICO_00,DOM_VK_WIN_ICO_CLEAR,"
                    + "DOM_VK_WIN_ICO_HELP,DOM_VK_WIN_OEM_ATTN,"
                    + "DOM_VK_WIN_OEM_AUTO,DOM_VK_WIN_OEM_BACKTAB,DOM_VK_WIN_OEM_CLEAR,DOM_VK_WIN_OEM_COPY,"
                    + "DOM_VK_WIN_OEM_CUSEL,DOM_VK_WIN_OEM_ENLW,DOM_VK_WIN_OEM_FINISH,DOM_VK_WIN_OEM_FJ_JISHO,"
                    + "DOM_VK_WIN_OEM_FJ_LOYA,DOM_VK_WIN_OEM_FJ_MASSHOU,"
                    + "DOM_VK_WIN_OEM_FJ_ROYA,DOM_VK_WIN_OEM_FJ_TOUROKU,"
                    + "DOM_VK_WIN_OEM_JUMP,DOM_VK_WIN_OEM_PA1,DOM_VK_WIN_OEM_PA2,"
                    + "DOM_VK_WIN_OEM_PA3,DOM_VK_WIN_OEM_RESET,"
                    + "DOM_VK_WIN_OEM_WSCTRL,DOM_VK_X,DOM_VK_Y,DOM_VK_Z,DOM_VK_ZOOM,eventPhase,explicitOriginalTarget,"
                    + "getModifierState(),initEvent(),initKeyboardEvent(),initKeyEvent(),initUIEvent(),isComposing,"
                    + "isTrusted,key,keyCode,layerX,layerY,location,META_MASK,metaKey,NONE,originalTarget,"
                    + "preventDefault(),rangeOffset,rangeParent,repeat,returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,"
                    + "SHIFT_MASK,shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                    + "type,view,which"},
            FF78 = {"ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "charCode,code,composed,composedPath(),CONTROL_MASK,ctrlKey,currentTarget,defaultPrevented,detail,"
                + "DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,"
                + "DOM_VK_0,DOM_VK_1,DOM_VK_2,DOM_VK_3,DOM_VK_4,DOM_VK_5,DOM_VK_6,DOM_VK_7,DOM_VK_8,DOM_VK_9,DOM_VK_A,"
                + "DOM_VK_ACCEPT,DOM_VK_ADD,DOM_VK_ALT,DOM_VK_ALTGR,DOM_VK_AMPERSAND,DOM_VK_ASTERISK,DOM_VK_AT,"
                + "DOM_VK_ATTN,DOM_VK_B,DOM_VK_BACK_QUOTE,DOM_VK_BACK_SLASH,DOM_VK_BACK_SPACE,DOM_VK_C,"
                + "DOM_VK_CANCEL,DOM_VK_CAPS_LOCK,"
                + "DOM_VK_CIRCUMFLEX,DOM_VK_CLEAR,DOM_VK_CLOSE_BRACKET,DOM_VK_CLOSE_CURLY_BRACKET,DOM_VK_CLOSE_PAREN,"
                + "DOM_VK_COLON,DOM_VK_COMMA,DOM_VK_CONTEXT_MENU,DOM_VK_CONTROL,DOM_VK_CONVERT,DOM_VK_CRSEL,DOM_VK_D,"
                + "DOM_VK_DECIMAL,DOM_VK_DELETE,DOM_VK_DIVIDE,DOM_VK_DOLLAR,DOM_VK_DOUBLE_QUOTE,DOM_VK_DOWN,DOM_VK_E,"
                + "DOM_VK_EISU,DOM_VK_END,DOM_VK_EQUALS,DOM_VK_EREOF,DOM_VK_ESCAPE,DOM_VK_EXCLAMATION,DOM_VK_EXECUTE,"
                + "DOM_VK_EXSEL,DOM_VK_F,DOM_VK_F1,DOM_VK_F10,DOM_VK_F11,DOM_VK_F12,DOM_VK_F13,DOM_VK_F14,DOM_VK_F15,"
                + "DOM_VK_F16,DOM_VK_F17,DOM_VK_F18,DOM_VK_F19,DOM_VK_F2,DOM_VK_F20,DOM_VK_F21,DOM_VK_F22,DOM_VK_F23,"
                + "DOM_VK_F24,DOM_VK_F3,DOM_VK_F4,DOM_VK_F5,DOM_VK_F6,DOM_VK_F7,DOM_VK_F8,DOM_VK_F9,DOM_VK_FINAL,"
                + "DOM_VK_G,DOM_VK_GREATER_THAN,DOM_VK_H,DOM_VK_HANGUL,DOM_VK_HANJA,DOM_VK_HASH,DOM_VK_HELP,"
                + "DOM_VK_HOME,DOM_VK_HYPHEN_MINUS,DOM_VK_I,DOM_VK_INSERT,DOM_VK_J,DOM_VK_JUNJA,DOM_VK_K,"
                + "DOM_VK_KANA,DOM_VK_KANJI,DOM_VK_L,DOM_VK_LEFT,DOM_VK_LESS_THAN,DOM_VK_M,DOM_VK_META,"
                + "DOM_VK_MODECHANGE,DOM_VK_MULTIPLY,DOM_VK_N,DOM_VK_NONCONVERT,DOM_VK_NUM_LOCK,DOM_VK_NUMPAD0,"
                + "DOM_VK_NUMPAD1,DOM_VK_NUMPAD2,DOM_VK_NUMPAD3,DOM_VK_NUMPAD4,DOM_VK_NUMPAD5,DOM_VK_NUMPAD6,"
                + "DOM_VK_NUMPAD7,DOM_VK_NUMPAD8,DOM_VK_NUMPAD9,DOM_VK_O,DOM_VK_OPEN_BRACKET,DOM_VK_OPEN_CURLY_BRACKET,"
                + "DOM_VK_OPEN_PAREN",
                "DOM_VK_P,DOM_VK_PA1,DOM_VK_PAGE_DOWN,DOM_VK_PAGE_UP,DOM_VK_PAUSE,DOM_VK_PERCENT,"
                    + "DOM_VK_PERIOD,DOM_VK_PIPE,DOM_VK_PLAY,DOM_VK_PLUS,"
                    + "DOM_VK_PRINT,DOM_VK_PRINTSCREEN,DOM_VK_PROCESSKEY,"
                    + "DOM_VK_Q,DOM_VK_QUESTION_MARK,DOM_VK_QUOTE,DOM_VK_R,DOM_VK_RETURN,DOM_VK_RIGHT,DOM_VK_S,"
                    + "DOM_VK_SCROLL_LOCK,DOM_VK_SELECT,DOM_VK_SEMICOLON,DOM_VK_SEPARATOR,DOM_VK_SHIFT,DOM_VK_SLASH,"
                    + "DOM_VK_SLEEP,DOM_VK_SPACE,DOM_VK_SUBTRACT,DOM_VK_T,DOM_VK_TAB,DOM_VK_TILDE,DOM_VK_U,"
                    + "DOM_VK_UNDERSCORE,DOM_VK_UP,DOM_VK_V,DOM_VK_VOLUME_DOWN,DOM_VK_VOLUME_MUTE,DOM_VK_VOLUME_UP,"
                    + "DOM_VK_W,DOM_VK_WIN,DOM_VK_WIN_ICO_00,DOM_VK_WIN_ICO_CLEAR,"
                    + "DOM_VK_WIN_ICO_HELP,DOM_VK_WIN_OEM_ATTN,"
                    + "DOM_VK_WIN_OEM_AUTO,DOM_VK_WIN_OEM_BACKTAB,DOM_VK_WIN_OEM_CLEAR,DOM_VK_WIN_OEM_COPY,"
                    + "DOM_VK_WIN_OEM_CUSEL,DOM_VK_WIN_OEM_ENLW,DOM_VK_WIN_OEM_FINISH,DOM_VK_WIN_OEM_FJ_JISHO,"
                    + "DOM_VK_WIN_OEM_FJ_LOYA,DOM_VK_WIN_OEM_FJ_MASSHOU,"
                    + "DOM_VK_WIN_OEM_FJ_ROYA,DOM_VK_WIN_OEM_FJ_TOUROKU,"
                    + "DOM_VK_WIN_OEM_JUMP,DOM_VK_WIN_OEM_PA1,DOM_VK_WIN_OEM_PA2,"
                    + "DOM_VK_WIN_OEM_PA3,DOM_VK_WIN_OEM_RESET,"
                    + "DOM_VK_WIN_OEM_WSCTRL,DOM_VK_X,DOM_VK_Y,DOM_VK_Z,DOM_VK_ZOOM,eventPhase,explicitOriginalTarget,"
                    + "getModifierState(),initEvent(),initKeyboardEvent(),initKeyEvent(),initUIEvent(),isComposing,"
                    + "isTrusted,key,keyCode,layerX,layerY,location,META_MASK,metaKey,NONE,originalTarget,"
                    + "preventDefault(),rangeOffset,rangeParent,repeat,returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,"
                    + "SHIFT_MASK,shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                    + "type,view,which"},
            IE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,char,charCode,"
                + "ctrlKey,currentTarget,defaultPrevented,detail,deviceSessionId,DOM_KEY_LOCATION_JOYSTICK,"
                + "DOM_KEY_LOCATION_LEFT,"
                + "DOM_KEY_LOCATION_MOBILE,DOM_KEY_LOCATION_NUMPAD,DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,"
                + "eventPhase,getModifierState(),initEvent(),initKeyboardEvent(),initUIEvent(),isTrusted,key,keyCode,"
                + "locale,location,metaKey,preventDefault(),repeat,shiftKey,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which")
    @HtmlUnitNYI(CHROME = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "charCode,"
                + "code,composed,ctrlKey,currentTarget,"
                + "defaultPrevented,detail,DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,"
                + "DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,"
                + "eventPhase,initEvent(),initUIEvent(),isComposing,key,keyCode,location,"
                + "metaKey,NONE,preventDefault(),repeat,returnValue,shiftKey,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which",
            EDGE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,charCode,"
                + "code,composed,ctrlKey,currentTarget,"
                + "defaultPrevented,detail,DOM_KEY_LOCATION_LEFT,DOM_KEY_LOCATION_NUMPAD,"
                + "DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,"
                + "eventPhase,initEvent(),initUIEvent(),isComposing,key,keyCode,location,"
                + "metaKey,NONE,preventDefault(),repeat,returnValue,shiftKey,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which",
            FF78 = {"ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,charCode,"
                + "code,composed,CONTROL_MASK,ctrlKey,currentTarget,defaultPrevented,detail,DOM_KEY_LOCATION_LEFT,"
                + "DOM_KEY_LOCATION_NUMPAD,DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,DOM_VK_0,DOM_VK_1,"
                + "DOM_VK_2,DOM_VK_3,DOM_VK_4,DOM_VK_5,DOM_VK_6,DOM_VK_7,DOM_VK_8,DOM_VK_9,DOM_VK_A,DOM_VK_ACCEPT,"
                + "DOM_VK_ADD,DOM_VK_ALT,DOM_VK_ALTGR,DOM_VK_AMPERSAND,DOM_VK_ASTERISK,DOM_VK_AT,DOM_VK_ATTN,"
                + "DOM_VK_B,DOM_VK_BACK_QUOTE,DOM_VK_BACK_SLASH,DOM_VK_BACK_SPACE,DOM_VK_C,DOM_VK_CANCEL,"
                + "DOM_VK_CAPS_LOCK,DOM_VK_CIRCUMFLEX,DOM_VK_CLEAR,DOM_VK_CLOSE_BRACKET,DOM_VK_CLOSE_CURLY_BRACKET,"
                + "DOM_VK_CLOSE_PAREN,DOM_VK_COLON,DOM_VK_COMMA,DOM_VK_CONTEXT_MENU,DOM_VK_CONTROL,DOM_VK_CONVERT,"
                + "DOM_VK_CRSEL,DOM_VK_D,DOM_VK_DECIMAL,DOM_VK_DELETE,DOM_VK_DIVIDE,DOM_VK_DOLLAR,"
                + "DOM_VK_DOUBLE_QUOTE,DOM_VK_DOWN,DOM_VK_E,DOM_VK_EISU,DOM_VK_END,DOM_VK_EQUALS,"
                + "DOM_VK_EREOF,DOM_VK_ESCAPE,DOM_VK_EXCLAMATION,DOM_VK_EXECUTE,DOM_VK_EXSEL,DOM_VK_F,"
                + "DOM_VK_F1,DOM_VK_F10,DOM_VK_F11,DOM_VK_F12,DOM_VK_F13,DOM_VK_F14,DOM_VK_F15,DOM_VK_F16,"
                + "DOM_VK_F17,DOM_VK_F18,DOM_VK_F19,DOM_VK_F2,DOM_VK_F20,DOM_VK_F21,DOM_VK_F22,DOM_VK_F23,"
                + "DOM_VK_F24,DOM_VK_F3,DOM_VK_F4,DOM_VK_F5,DOM_VK_F6,DOM_VK_F7,DOM_VK_F8,DOM_VK_F9,DOM_VK_FINAL,"
                + "DOM_VK_G,DOM_VK_GREATER_THAN,DOM_VK_H,DOM_VK_HANGUL,DOM_VK_HANJA,DOM_VK_HASH,DOM_VK_HELP,"
                + "DOM_VK_HOME,DOM_VK_HYPHEN_MINUS,DOM_VK_I,DOM_VK_INSERT,DOM_VK_J,DOM_VK_JUNJA,DOM_VK_K,"
                + "DOM_VK_KANA,DOM_VK_KANJI,DOM_VK_L,DOM_VK_LEFT,DOM_VK_LESS_THAN,DOM_VK_M,DOM_VK_META,"
                + "DOM_VK_MODECHANGE,DOM_VK_MULTIPLY,DOM_VK_N,DOM_VK_NONCONVERT,DOM_VK_NUM_LOCK,DOM_VK_NUMPAD0,"
                + "DOM_VK_NUMPAD1,DOM_VK_NUMPAD2,DOM_VK_NUMPAD3,DOM_VK_NUMPAD4,DOM_VK_NUMPAD5,DOM_VK_NUMPAD6,"
                + "DOM_VK_NUMPAD7,DOM_VK_NUMPAD8,DOM_VK_NUMPAD9,DOM_VK_O,DOM_VK_OPEN_BRACKET,"
                + "DOM_VK_OPEN_CURLY_BRACKET,DOM_VK_OPEN_PAREN,DOM_VK_P",
                "DOM_VK_PA1,DOM_VK_PAGE_DOWN,"
                    + "DOM_VK_PAGE_UP,DOM_VK_PAUSE,DOM_VK_PERCENT,DOM_VK_PERIOD,DOM_VK_PIPE,DOM_VK_PLAY,"
                    + "DOM_VK_PLUS,DOM_VK_PRINT,DOM_VK_PRINTSCREEN,DOM_VK_PROCESSKEY,DOM_VK_Q,DOM_VK_QUESTION_MARK,"
                    + "DOM_VK_QUOTE,DOM_VK_R,DOM_VK_RETURN,DOM_VK_RIGHT,DOM_VK_S,DOM_VK_SCROLL_LOCK,DOM_VK_SELECT,"
                    + "DOM_VK_SEMICOLON,DOM_VK_SEPARATOR,DOM_VK_SHIFT,DOM_VK_SLASH,DOM_VK_SLEEP,DOM_VK_SPACE,"
                    + "DOM_VK_SUBTRACT,DOM_VK_T,DOM_VK_TAB,DOM_VK_TILDE,DOM_VK_U,DOM_VK_UNDERSCORE,DOM_VK_UP,"
                    + "DOM_VK_V,DOM_VK_VOLUME_DOWN,DOM_VK_VOLUME_MUTE,DOM_VK_VOLUME_UP,DOM_VK_W,DOM_VK_WIN,"
                    + "DOM_VK_WIN_ICO_00,DOM_VK_WIN_ICO_CLEAR,DOM_VK_WIN_ICO_HELP,DOM_VK_WIN_OEM_ATTN,"
                    + "DOM_VK_WIN_OEM_AUTO,DOM_VK_WIN_OEM_BACKTAB,DOM_VK_WIN_OEM_CLEAR,DOM_VK_WIN_OEM_COPY,"
                    + "DOM_VK_WIN_OEM_CUSEL,DOM_VK_WIN_OEM_ENLW,DOM_VK_WIN_OEM_FINISH,DOM_VK_WIN_OEM_FJ_JISHO,"
                    + "DOM_VK_WIN_OEM_FJ_LOYA,DOM_VK_WIN_OEM_FJ_MASSHOU,DOM_VK_WIN_OEM_FJ_ROYA,"
                    + "DOM_VK_WIN_OEM_FJ_TOUROKU,DOM_VK_WIN_OEM_JUMP,DOM_VK_WIN_OEM_PA1,"
                    + "DOM_VK_WIN_OEM_PA2,DOM_VK_WIN_OEM_PA3,DOM_VK_WIN_OEM_RESET,DOM_VK_WIN_OEM_WSCTRL,"
                    + "DOM_VK_X,DOM_VK_Y,DOM_VK_Z,DOM_VK_ZOOM,"
                    + "eventPhase,initEvent(),initKeyEvent(),initUIEvent(),isComposing,"
                    + "key,keyCode,location,META_MASK,metaKey,NONE,preventDefault(),repeat,returnValue,"
                    + "SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,"
                    + "shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),"
                    + "target,timeStamp,type,view,which"},
            FF = {"ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,charCode,"
                + "code,composed,CONTROL_MASK,ctrlKey,currentTarget,defaultPrevented,detail,DOM_KEY_LOCATION_LEFT,"
                + "DOM_KEY_LOCATION_NUMPAD,DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,DOM_VK_0,DOM_VK_1,"
                + "DOM_VK_2,DOM_VK_3,DOM_VK_4,DOM_VK_5,DOM_VK_6,DOM_VK_7,DOM_VK_8,DOM_VK_9,DOM_VK_A,DOM_VK_ACCEPT,"
                + "DOM_VK_ADD,DOM_VK_ALT,DOM_VK_ALTGR,DOM_VK_AMPERSAND,DOM_VK_ASTERISK,DOM_VK_AT,DOM_VK_ATTN,"
                + "DOM_VK_B,DOM_VK_BACK_QUOTE,DOM_VK_BACK_SLASH,DOM_VK_BACK_SPACE,DOM_VK_C,DOM_VK_CANCEL,"
                + "DOM_VK_CAPS_LOCK,DOM_VK_CIRCUMFLEX,DOM_VK_CLEAR,DOM_VK_CLOSE_BRACKET,DOM_VK_CLOSE_CURLY_BRACKET,"
                + "DOM_VK_CLOSE_PAREN,DOM_VK_COLON,DOM_VK_COMMA,DOM_VK_CONTEXT_MENU,DOM_VK_CONTROL,DOM_VK_CONVERT,"
                + "DOM_VK_CRSEL,DOM_VK_D,DOM_VK_DECIMAL,DOM_VK_DELETE,DOM_VK_DIVIDE,DOM_VK_DOLLAR,"
                + "DOM_VK_DOUBLE_QUOTE,DOM_VK_DOWN,DOM_VK_E,DOM_VK_EISU,DOM_VK_END,DOM_VK_EQUALS,"
                + "DOM_VK_EREOF,DOM_VK_ESCAPE,DOM_VK_EXCLAMATION,DOM_VK_EXECUTE,DOM_VK_EXSEL,DOM_VK_F,"
                + "DOM_VK_F1,DOM_VK_F10,DOM_VK_F11,DOM_VK_F12,DOM_VK_F13,DOM_VK_F14,DOM_VK_F15,DOM_VK_F16,"
                + "DOM_VK_F17,DOM_VK_F18,DOM_VK_F19,DOM_VK_F2,DOM_VK_F20,DOM_VK_F21,DOM_VK_F22,DOM_VK_F23,"
                + "DOM_VK_F24,DOM_VK_F3,DOM_VK_F4,DOM_VK_F5,DOM_VK_F6,DOM_VK_F7,DOM_VK_F8,DOM_VK_F9,DOM_VK_FINAL,"
                + "DOM_VK_G,DOM_VK_GREATER_THAN,DOM_VK_H,DOM_VK_HANGUL,DOM_VK_HANJA,DOM_VK_HASH,DOM_VK_HELP,"
                + "DOM_VK_HOME,DOM_VK_HYPHEN_MINUS,DOM_VK_I,DOM_VK_INSERT,DOM_VK_J,DOM_VK_JUNJA,DOM_VK_K,"
                + "DOM_VK_KANA,DOM_VK_KANJI,DOM_VK_L,DOM_VK_LEFT,DOM_VK_LESS_THAN,DOM_VK_M,DOM_VK_META,"
                + "DOM_VK_MODECHANGE,DOM_VK_MULTIPLY,DOM_VK_N,DOM_VK_NONCONVERT,DOM_VK_NUM_LOCK,DOM_VK_NUMPAD0,"
                + "DOM_VK_NUMPAD1,DOM_VK_NUMPAD2,DOM_VK_NUMPAD3,DOM_VK_NUMPAD4,DOM_VK_NUMPAD5,DOM_VK_NUMPAD6,"
                + "DOM_VK_NUMPAD7,DOM_VK_NUMPAD8,DOM_VK_NUMPAD9,DOM_VK_O,DOM_VK_OPEN_BRACKET,"
                + "DOM_VK_OPEN_CURLY_BRACKET,DOM_VK_OPEN_PAREN,DOM_VK_P",
                "DOM_VK_PA1,DOM_VK_PAGE_DOWN,"
                    + "DOM_VK_PAGE_UP,DOM_VK_PAUSE,DOM_VK_PERCENT,DOM_VK_PERIOD,DOM_VK_PIPE,DOM_VK_PLAY,"
                    + "DOM_VK_PLUS,DOM_VK_PRINT,DOM_VK_PRINTSCREEN,DOM_VK_PROCESSKEY,DOM_VK_Q,DOM_VK_QUESTION_MARK,"
                    + "DOM_VK_QUOTE,DOM_VK_R,DOM_VK_RETURN,DOM_VK_RIGHT,DOM_VK_S,DOM_VK_SCROLL_LOCK,DOM_VK_SELECT,"
                    + "DOM_VK_SEMICOLON,DOM_VK_SEPARATOR,DOM_VK_SHIFT,DOM_VK_SLASH,DOM_VK_SLEEP,DOM_VK_SPACE,"
                    + "DOM_VK_SUBTRACT,DOM_VK_T,DOM_VK_TAB,DOM_VK_TILDE,DOM_VK_U,DOM_VK_UNDERSCORE,DOM_VK_UP,"
                    + "DOM_VK_V,DOM_VK_VOLUME_DOWN,DOM_VK_VOLUME_MUTE,DOM_VK_VOLUME_UP,DOM_VK_W,DOM_VK_WIN,"
                    + "DOM_VK_WIN_ICO_00,DOM_VK_WIN_ICO_CLEAR,DOM_VK_WIN_ICO_HELP,DOM_VK_WIN_OEM_ATTN,"
                    + "DOM_VK_WIN_OEM_AUTO,DOM_VK_WIN_OEM_BACKTAB,DOM_VK_WIN_OEM_CLEAR,DOM_VK_WIN_OEM_COPY,"
                    + "DOM_VK_WIN_OEM_CUSEL,DOM_VK_WIN_OEM_ENLW,DOM_VK_WIN_OEM_FINISH,DOM_VK_WIN_OEM_FJ_JISHO,"
                    + "DOM_VK_WIN_OEM_FJ_LOYA,DOM_VK_WIN_OEM_FJ_MASSHOU,DOM_VK_WIN_OEM_FJ_ROYA,"
                    + "DOM_VK_WIN_OEM_FJ_TOUROKU,DOM_VK_WIN_OEM_JUMP,DOM_VK_WIN_OEM_PA1,"
                    + "DOM_VK_WIN_OEM_PA2,DOM_VK_WIN_OEM_PA3,DOM_VK_WIN_OEM_RESET,DOM_VK_WIN_OEM_WSCTRL,"
                    + "DOM_VK_X,DOM_VK_Y,DOM_VK_Z,DOM_VK_ZOOM,"
                    + "eventPhase,initEvent(),initKeyEvent(),initUIEvent(),isComposing,"
                    + "key,keyCode,location,META_MASK,metaKey,NONE,preventDefault(),repeat,returnValue,"
                    + "SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,"
                    + "shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),"
                    + "target,timeStamp,type,view,which"},
            IE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,char,charCode,"
                + "ctrlKey,currentTarget,defaultPrevented,detail,DOM_KEY_LOCATION_JOYSTICK,DOM_KEY_LOCATION_LEFT,"
                + "DOM_KEY_LOCATION_MOBILE,DOM_KEY_LOCATION_NUMPAD,DOM_KEY_LOCATION_RIGHT,DOM_KEY_LOCATION_STANDARD,"
                + "eventPhase,initEvent(),initUIEvent(),key,keyCode,location,"
                + "metaKey,preventDefault(),repeat,shiftKey,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which")
    public void keyboardEvent() throws Exception {
        testString("", "document.createEvent('KeyboardEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "isTrusted,NONE,path,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "isTrusted,NONE,path,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),isTrusted,META_MASK,NONE,originalTarget,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),isTrusted,META_MASK,NONE,originalTarget,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),isTrusted,preventDefault(),srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),preventDefault(),srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type")
    public void event2() throws Exception {
        testString("", "document.createEvent('Event')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "composedPath(),currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),"
                + "isTrusted,NONE,path,preventDefault(),returnValue,sourceCapabilities,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "composedPath(),currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),"
                + "isTrusted,NONE,path,preventDefault(),returnValue,sourceCapabilities,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,detail,eventPhase,"
                + "explicitOriginalTarget,initEvent(),initUIEvent(),isTrusted,layerX,layerY,META_MASK,NONE,"
                + "originalTarget,preventDefault(),rangeOffset,rangeParent,returnValue,SCROLL_PAGE_DOWN,"
                + "SCROLL_PAGE_UP,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,detail,eventPhase,"
                + "explicitOriginalTarget,initEvent(),initUIEvent(),isTrusted,layerX,layerY,META_MASK,NONE,"
                + "originalTarget,preventDefault(),rangeOffset,rangeParent,returnValue,SCROLL_PAGE_DOWN,"
                + "SCROLL_PAGE_UP,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,"
                + "defaultPrevented,detail,deviceSessionId,eventPhase,initEvent(),initUIEvent(),isTrusted,"
                + "preventDefault(),srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),NONE,preventDefault(),"
                + "returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),NONE,preventDefault(),"
                + "returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,"
                + "defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),preventDefault(),srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view")
    public void uiEvent() throws Exception {
        testString("", "document.createEvent('UIEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.DragEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "clientX,clientY,composed,composedPath(),ctrlKey,currentTarget,dataTransfer,defaultPrevented,detail,"
                + "eventPhase,fromElement,getModifierState(),initEvent(),initMouseEvent(),initUIEvent(),isTrusted,"
                + "layerX,layerY,metaKey,movementX,movementY,NONE,offsetX,offsetY,pageX,pageY,path,preventDefault(),"
                + "relatedTarget,returnValue,screenX,screenY,shiftKey,sourceCapabilities,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,toElement,type,view,which,x,y",
            EDGE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "clientX,clientY,composed,composedPath(),ctrlKey,currentTarget,dataTransfer,defaultPrevented,detail,"
                + "eventPhase,fromElement,getModifierState(),initEvent(),initMouseEvent(),initUIEvent(),isTrusted,"
                + "layerX,layerY,metaKey,movementX,movementY,NONE,offsetX,offsetY,pageX,pageY,path,preventDefault(),"
                + "relatedTarget,returnValue,screenX,screenY,shiftKey,sourceCapabilities,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,toElement,type,view,which,x,y",
            FF = "ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,composedPath(),CONTROL_MASK,ctrlKey,currentTarget,"
                + "dataTransfer,defaultPrevented,detail,eventPhase,explicitOriginalTarget,getModifierState(),"
                + "initDragEvent(),initEvent(),initMouseEvent(),initNSMouseEvent(),initUIEvent(),isTrusted,"
                + "layerX,layerY,META_MASK,metaKey,movementX,movementY,MOZ_SOURCE_CURSOR,MOZ_SOURCE_ERASER,"
                + "MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,MOZ_SOURCE_PEN,MOZ_SOURCE_TOUCH,MOZ_SOURCE_UNKNOWN,"
                + "mozInputSource,mozPressure,NONE,offsetX,offsetY,originalTarget,pageX,pageY,preventDefault(),"
                + "rangeOffset,rangeParent,region,relatedTarget,returnValue,screenX,screenY,SCROLL_PAGE_DOWN,"
                + "SCROLL_PAGE_UP,SHIFT_MASK,shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which,x,y",
            FF78 = "ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,composedPath(),CONTROL_MASK,ctrlKey,currentTarget,"
                + "dataTransfer,defaultPrevented,detail,eventPhase,explicitOriginalTarget,getModifierState(),"
                + "initDragEvent(),initEvent(),initMouseEvent(),initNSMouseEvent(),initUIEvent(),isTrusted,"
                + "layerX,layerY,META_MASK,metaKey,movementX,movementY,MOZ_SOURCE_CURSOR,MOZ_SOURCE_ERASER,"
                + "MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,MOZ_SOURCE_PEN,MOZ_SOURCE_TOUCH,MOZ_SOURCE_UNKNOWN,"
                + "mozInputSource,mozPressure,NONE,offsetX,offsetY,originalTarget,pageX,pageY,preventDefault(),"
                + "rangeOffset,rangeParent,region,relatedTarget,returnValue,screenX,screenY,SCROLL_PAGE_DOWN,"
                + "SCROLL_PAGE_UP,SHIFT_MASK,shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which,x,y",
            IE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "clientX,clientY,ctrlKey,currentTarget,dataTransfer,defaultPrevented,detail,deviceSessionId,"
                + "eventPhase,fromElement,getModifierState(),initDragEvent(),initEvent(),initMouseEvent(),"
                + "initUIEvent(),isTrusted,layerX,layerY,metaKey,msConvertURL(),offsetX,offsetY,pageX,pageY,"
                + "preventDefault(),relatedTarget,screenX,screenY,shiftKey,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,toElement,type,view,which,x,y")
    @HtmlUnitNYI(CHROME = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,ctrlKey,currentTarget,defaultPrevented,detail,eventPhase,"
                + "initEvent(),initMouseEvent(),initUIEvent(),metaKey,NONE,pageX,pageY,preventDefault(),"
                + "returnValue,screenX,screenY,shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which",
            EDGE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,ctrlKey,currentTarget,defaultPrevented,detail,eventPhase,"
                + "initEvent(),initMouseEvent(),initUIEvent(),metaKey,NONE,pageX,pageY,preventDefault(),"
                + "returnValue,screenX,screenY,shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which",
            FF78 = "ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,CONTROL_MASK,ctrlKey,currentTarget,defaultPrevented,detail,"
                + "eventPhase,initEvent(),initMouseEvent(),initUIEvent(),META_MASK,metaKey,MOZ_SOURCE_CURSOR,"
                + "MOZ_SOURCE_ERASER,MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,MOZ_SOURCE_PEN,MOZ_SOURCE_TOUCH,"
                + "MOZ_SOURCE_UNKNOWN,NONE,pageX,pageY,preventDefault(),returnValue,screenX,screenY,"
                + "SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,shiftKey,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which",
            FF = "ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,CONTROL_MASK,ctrlKey,currentTarget,defaultPrevented,detail,"
                + "eventPhase,initEvent(),initMouseEvent(),initUIEvent(),META_MASK,metaKey,MOZ_SOURCE_CURSOR,"
                + "MOZ_SOURCE_ERASER,MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,MOZ_SOURCE_PEN,MOZ_SOURCE_TOUCH,"
                + "MOZ_SOURCE_UNKNOWN,NONE,pageX,pageY,preventDefault(),returnValue,screenX,screenY,"
                + "SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,shiftKey,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which",
            IE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "clientX,clientY,ctrlKey,currentTarget,defaultPrevented,detail,eventPhase,initEvent(),"
                + "initMouseEvent(),initUIEvent(),metaKey,pageX,pageY,preventDefault(),screenX,screenY,"
                + "shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                + "type,view,which")
    public void dragEvent() throws Exception {
        testString("", "document.createEvent('DragEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "altitudeAngle,azimuthAngle,getCoalescedEvents(),getPredictedEvents(),height,"
                + "isPrimary,pointerId,pointerType,pressure,"
                + "tangentialPressure,tiltX,tiltY,twist,width",
            EDGE = "altitudeAngle,azimuthAngle,getCoalescedEvents(),getPredictedEvents(),height,"
                + "isPrimary,pointerId,pointerType,pressure,"
                + "tangentialPressure,tiltX,tiltY,twist,width",
            FF = "getCoalescedEvents(),height,isPrimary,pointerId,pointerType,pressure,"
                + "tangentialPressure,tiltX,tiltY,twist,width",
            FF78 = "getCoalescedEvents(),height,isPrimary,pointerId,pointerType,pressure,"
                + "tangentialPressure,tiltX,tiltY,twist,width",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "altitudeAngle,azimuthAngle,height,"
                    + "isPrimary,pointerId,pointerType,pressure,tiltX,tiltY,width",
            EDGE = "altitudeAngle,azimuthAngle,height,isPrimary,pointerId,pointerType,pressure,tiltX,tiltY,width",
            FF78 = "height,isPrimary,pointerId,pointerType,pressure,tiltX,tiltY,width",
            FF = "height,isPrimary,pointerId,pointerType,pressure,tiltX,tiltY,width")
    public void pointerEvent() throws Exception {
        testString("", "new PointerEvent('click'), document.createEvent('MouseEvent')");
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
            IE = "height,hwTimestamp,initPointerEvent(),isPrimary,pointerId,"
                + "pointerType,pressure,rotation,tiltX,tiltY,width")
    @HtmlUnitNYI(IE = "height,initPointerEvent(),isPrimary,pointerId,pointerType,pressure,tiltX,tiltY,width")
    public void pointerEvent2() throws Exception {
        testString("", " document.createEvent('PointerEvent'), document.createEvent('MouseEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.WheelEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "deltaMode,deltaX,deltaY,deltaZ,DOM_DELTA_LINE,DOM_DELTA_PAGE,"
                + "DOM_DELTA_PIXEL,wheelDelta,wheelDeltaX,wheelDeltaY",
            EDGE = "deltaMode,deltaX,deltaY,deltaZ,DOM_DELTA_LINE,DOM_DELTA_PAGE,"
                + "DOM_DELTA_PIXEL,wheelDelta,wheelDeltaX,wheelDeltaY",
            FF = "exception",
            FF78 = "exception",
            IE = "deltaMode,deltaX,deltaY,deltaZ,DOM_DELTA_LINE,DOM_DELTA_PAGE,DOM_DELTA_PIXEL,initWheelEvent()")
    @HtmlUnitNYI(CHROME = "DOM_DELTA_LINE,DOM_DELTA_PAGE,DOM_DELTA_PIXEL",
            EDGE = "DOM_DELTA_LINE,DOM_DELTA_PAGE,DOM_DELTA_PIXEL",
            IE = "DOM_DELTA_LINE,DOM_DELTA_PAGE,DOM_DELTA_PIXEL")
    public void wheelEvent() throws Exception {
        testString("", "document.createEvent('WheelEvent'), document.createEvent('MouseEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,composedPath(),ctrlKey,currentTarget,"
                + "defaultPrevented,detail,eventPhase,fromElement,getModifierState(),initEvent(),initMouseEvent(),"
                + "initUIEvent(),isTrusted,layerX,layerY,metaKey,movementX,movementY,NONE,offsetX,offsetY,"
                + "pageX,pageY,path,preventDefault(),relatedTarget,returnValue,screenX,screenY,shiftKey,"
                + "sourceCapabilities,srcElement,stopImmediatePropagation(),stopPropagation(),target,"
                + "timeStamp,toElement,type,view,which,x,y",
            EDGE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,composedPath(),ctrlKey,currentTarget,"
                + "defaultPrevented,detail,eventPhase,fromElement,getModifierState(),initEvent(),initMouseEvent(),"
                + "initUIEvent(),isTrusted,layerX,layerY,metaKey,movementX,movementY,NONE,offsetX,offsetY,"
                + "pageX,pageY,path,preventDefault(),relatedTarget,returnValue,screenX,screenY,shiftKey,"
                + "sourceCapabilities,srcElement,stopImmediatePropagation(),stopPropagation(),target,"
                + "timeStamp,toElement,type,view,which,x,y",
            FF = "ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,composedPath(),CONTROL_MASK,ctrlKey,currentTarget,"
                + "defaultPrevented,detail,eventPhase,explicitOriginalTarget,getModifierState(),initEvent(),"
                + "initMouseEvent(),initNSMouseEvent(),initUIEvent(),isTrusted,layerX,layerY,META_MASK,metaKey,"
                + "movementX,movementY,MOZ_SOURCE_CURSOR,MOZ_SOURCE_ERASER,MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,"
                + "MOZ_SOURCE_PEN,MOZ_SOURCE_TOUCH,MOZ_SOURCE_UNKNOWN,mozInputSource,mozPressure,NONE,offsetX,"
                + "offsetY,originalTarget,pageX,pageY,preventDefault(),rangeOffset,rangeParent,region,"
                + "relatedTarget,returnValue,screenX,screenY,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,shiftKey,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which,x,y",
            FF78 = "ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,composedPath(),CONTROL_MASK,ctrlKey,currentTarget,"
                + "defaultPrevented,detail,eventPhase,explicitOriginalTarget,getModifierState(),initEvent(),"
                + "initMouseEvent(),initNSMouseEvent(),initUIEvent(),isTrusted,layerX,layerY,META_MASK,metaKey,"
                + "movementX,movementY,MOZ_SOURCE_CURSOR,MOZ_SOURCE_ERASER,MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,"
                + "MOZ_SOURCE_PEN,MOZ_SOURCE_TOUCH,MOZ_SOURCE_UNKNOWN,mozInputSource,mozPressure,NONE,offsetX,"
                + "offsetY,originalTarget,pageX,pageY,preventDefault(),rangeOffset,rangeParent,region,"
                + "relatedTarget,returnValue,screenX,screenY,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,shiftKey,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which,x,y",
            IE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "clientX,clientY,ctrlKey,currentTarget,defaultPrevented,detail,deviceSessionId,eventPhase,"
                + "fromElement,getModifierState(),initEvent(),initMouseEvent(),initUIEvent(),isTrusted,layerX,"
                + "layerY,metaKey,offsetX,offsetY,pageX,pageY,preventDefault(),relatedTarget,screenX,screenY,"
                + "shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,toElement,"
                + "type,view,which,x,y")
    @HtmlUnitNYI(CHROME = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,ctrlKey,currentTarget,defaultPrevented,detail,eventPhase,"
                + "initEvent(),initMouseEvent(),initUIEvent(),metaKey,NONE,pageX,pageY,preventDefault(),"
                + "returnValue,screenX,screenY,shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which",
            EDGE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,ctrlKey,currentTarget,defaultPrevented,detail,eventPhase,"
                + "initEvent(),initMouseEvent(),initUIEvent(),metaKey,NONE,pageX,pageY,preventDefault(),"
                + "returnValue,screenX,screenY,shiftKey,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which",
            FF78 = "ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,CONTROL_MASK,ctrlKey,currentTarget,defaultPrevented,"
                + "detail,eventPhase,initEvent(),initMouseEvent(),initUIEvent(),META_MASK,metaKey,"
                + "MOZ_SOURCE_CURSOR,MOZ_SOURCE_ERASER,MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,MOZ_SOURCE_PEN,"
                + "MOZ_SOURCE_TOUCH,MOZ_SOURCE_UNKNOWN,NONE,pageX,pageY,preventDefault(),returnValue,screenX,"
                + "screenY,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,shiftKey,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which",
            FF = "ALT_MASK,altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,composed,CONTROL_MASK,ctrlKey,currentTarget,defaultPrevented,"
                + "detail,eventPhase,initEvent(),initMouseEvent(),initUIEvent(),META_MASK,metaKey,"
                + "MOZ_SOURCE_CURSOR,MOZ_SOURCE_ERASER,MOZ_SOURCE_KEYBOARD,MOZ_SOURCE_MOUSE,MOZ_SOURCE_PEN,"
                + "MOZ_SOURCE_TOUCH,MOZ_SOURCE_UNKNOWN,NONE,pageX,pageY,preventDefault(),returnValue,screenX,"
                + "screenY,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,shiftKey,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which",
            IE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "clientX,clientY,ctrlKey,currentTarget,defaultPrevented,detail,eventPhase,initEvent(),"
                + "initMouseEvent(),initUIEvent(),metaKey,pageX,pageY,preventDefault(),screenX,screenY,shiftKey,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which")
    public void mouseEvent() throws Exception {
        testString("", "document.createEvent('MouseEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.CompositionEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,data,defaultPrevented,detail,eventPhase,"
                + "initCompositionEvent(),initEvent(),initUIEvent(),isTrusted,NONE,path,preventDefault(),"
                + "returnValue,sourceCapabilities,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,data,defaultPrevented,detail,eventPhase,"
                + "initCompositionEvent(),initEvent(),initUIEvent(),isTrusted,NONE,path,preventDefault(),"
                + "returnValue,sourceCapabilities,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,data,defaultPrevented,detail,eventPhase,"
                + "explicitOriginalTarget,initCompositionEvent(),initEvent(),initUIEvent(),isTrusted,"
                + "layerX,layerY,locale,META_MASK,NONE,originalTarget,preventDefault(),rangeOffset,rangeParent,"
                + "returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,data,defaultPrevented,detail,eventPhase,"
                + "explicitOriginalTarget,initCompositionEvent(),initEvent(),initUIEvent(),isTrusted,"
                + "layerX,layerY,locale,META_MASK,NONE,originalTarget,preventDefault(),rangeOffset,rangeParent,"
                + "returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,data,"
                + "defaultPrevented,detail,deviceSessionId,eventPhase,initCompositionEvent(),initEvent(),"
                + "initUIEvent(),isTrusted,locale,preventDefault(),srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,currentTarget,"
                + "data,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),NONE,preventDefault(),"
                + "returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,currentTarget,"
                + "data,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),NONE,preventDefault(),"
                + "returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,data,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,data,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,data,"
                + "defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),preventDefault(),srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view")
    public void compositionEvent() throws Exception {
        testString("", "document.createEvent('CompositionEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.FocusEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,defaultPrevented,detail,eventPhase,initEvent(),"
                + "initUIEvent(),isTrusted,NONE,path,preventDefault(),relatedTarget,returnValue,"
                + "sourceCapabilities,srcElement,stopImmediatePropagation(),stopPropagation(),target,"
                + "timeStamp,type,view,which",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,defaultPrevented,detail,eventPhase,initEvent(),"
                + "initUIEvent(),isTrusted,NONE,path,preventDefault(),relatedTarget,returnValue,"
                + "sourceCapabilities,srcElement,stopImmediatePropagation(),stopPropagation(),target,"
                + "timeStamp,type,view,which",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,detail,eventPhase,"
                + "explicitOriginalTarget,initEvent(),initUIEvent(),isTrusted,layerX,layerY,META_MASK,NONE,"
                + "originalTarget,preventDefault(),rangeOffset,rangeParent,relatedTarget,returnValue,"
                + "SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,detail,eventPhase,"
                + "explicitOriginalTarget,initEvent(),initUIEvent(),isTrusted,layerX,layerY,META_MASK,NONE,"
                + "originalTarget,preventDefault(),rangeOffset,rangeParent,relatedTarget,returnValue,"
                + "SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view,which",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "currentTarget,defaultPrevented,detail,deviceSessionId,eventPhase,initEvent(),"
                + "initFocusEvent(),initUIEvent(),isTrusted,preventDefault(),relatedTarget,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),NONE,"
                + "preventDefault(),returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),NONE,"
                + "preventDefault(),returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,"
                + "defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),preventDefault(),srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view")
    public void focusEvent() throws Exception {
        testString("", "document.createEvent('FocusEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.InputEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,data,dataTransfer,defaultPrevented,detail,"
                + "eventPhase,getTargetRanges(),initEvent(),initUIEvent(),inputType,isComposing,"
                + "isTrusted,NONE,path,preventDefault(),returnValue,sourceCapabilities,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,data,dataTransfer,defaultPrevented,detail,"
                + "eventPhase,getTargetRanges(),initEvent(),initUIEvent(),inputType,isComposing,"
                + "isTrusted,NONE,path,preventDefault(),returnValue,sourceCapabilities,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,data,dataTransfer,defaultPrevented,"
                + "detail,eventPhase,explicitOriginalTarget,initEvent(),initUIEvent(),inputType,isComposing,"
                + "isTrusted,layerX,layerY,META_MASK,NONE,originalTarget,preventDefault(),rangeOffset,"
                + "rangeParent,returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,data,dataTransfer,defaultPrevented,"
                + "detail,eventPhase,explicitOriginalTarget,initEvent(),initUIEvent(),inputType,isComposing,"
                + "isTrusted,layerX,layerY,META_MASK,NONE,originalTarget,preventDefault(),rangeOffset,"
                + "rangeParent,returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view,which",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "data,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),"
                + "inputType,isComposing,NONE,"
                + "preventDefault(),returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "data,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),"
                + "inputType,isComposing,NONE,"
                + "preventDefault(),returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,currentTarget,"
                + "data,defaultPrevented,detail,eventPhase,initEvent(),"
                + "initUIEvent(),inputType,isComposing,"
                + "META_MASK,NONE,preventDefault(),returnValue,SCROLL_PAGE_DOWN,"
                + "SCROLL_PAGE_UP,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,currentTarget,"
                + "data,defaultPrevented,detail,eventPhase,initEvent(),"
                + "initUIEvent(),inputType,isComposing,"
                + "META_MASK,NONE,preventDefault(),returnValue,SCROLL_PAGE_DOWN,"
                + "SCROLL_PAGE_UP,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,view")
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
            IE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,button,buttons,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,clientX,clientY,ctrlKey,currentTarget,defaultPrevented,detail,"
                + "deviceSessionId,eventPhase,fromElement,getModifierState(),initEvent(),initMouseEvent(),"
                + "initMouseWheelEvent(),initUIEvent(),isTrusted,layerX,layerY,metaKey,offsetX,offsetY,"
                + "pageX,pageY,preventDefault(),relatedTarget,screenX,screenY,shiftKey,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,toElement,type,"
                + "view,wheelDelta,which,x,y")
    @HtmlUnitNYI(IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),preventDefault(),"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view")
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
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,data,defaultPrevented,detail,eventPhase,initEvent(),"
                + "initTextEvent(),initUIEvent(),isTrusted,NONE,path,preventDefault(),returnValue,"
                + "sourceCapabilities,srcElement,stopImmediatePropagation(),stopPropagation(),target,"
                + "timeStamp,type,view,which",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,data,defaultPrevented,detail,eventPhase,initEvent(),"
                + "initTextEvent(),initUIEvent(),isTrusted,NONE,path,preventDefault(),returnValue,"
                + "sourceCapabilities,srcElement,stopImmediatePropagation(),stopPropagation(),target,"
                + "timeStamp,type,view,which",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,data,defaultPrevented,detail,eventPhase,"
                + "explicitOriginalTarget,initCompositionEvent(),initEvent(),initUIEvent(),isTrusted,layerX,layerY,"
                + "locale,META_MASK,NONE,originalTarget,preventDefault(),rangeOffset,rangeParent,returnValue,"
                + "SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,data,defaultPrevented,detail,eventPhase,"
                + "explicitOriginalTarget,initCompositionEvent(),initEvent(),initUIEvent(),isTrusted,layerX,layerY,"
                + "locale,META_MASK,NONE,originalTarget,preventDefault(),rangeOffset,rangeParent,returnValue,"
                + "SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type,view,which",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,data,"
                + "defaultPrevented,detail,deviceSessionId,DOM_INPUT_METHOD_DROP,DOM_INPUT_METHOD_HANDWRITING,"
                + "DOM_INPUT_METHOD_IME,DOM_INPUT_METHOD_KEYBOARD,DOM_INPUT_METHOD_MULTIMODAL,DOM_INPUT_METHOD_OPTION,"
                + "DOM_INPUT_METHOD_PASTE,DOM_INPUT_METHOD_SCRIPT,DOM_INPUT_METHOD_UNKNOWN,DOM_INPUT_METHOD_VOICE,"
                + "eventPhase,initEvent(),initTextEvent(),initUIEvent(),inputMethod,isTrusted,locale,preventDefault(),"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),NONE,preventDefault(),returnValue,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),NONE,preventDefault(),returnValue,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SCROLL_PAGE_DOWN,SCROLL_PAGE_UP,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            IE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,"
                + "defaultPrevented,"
                + "detail,DOM_INPUT_METHOD_DROP,DOM_INPUT_METHOD_HANDWRITING,DOM_INPUT_METHOD_IME,"
                + "DOM_INPUT_METHOD_KEYBOARD,DOM_INPUT_METHOD_MULTIMODAL,DOM_INPUT_METHOD_OPTION,"
                + "DOM_INPUT_METHOD_PASTE,DOM_INPUT_METHOD_SCRIPT,DOM_INPUT_METHOD_UNKNOWN,"
                + "DOM_INPUT_METHOD_VOICE,eventPhase,initEvent(),initUIEvent(),preventDefault(),srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view")
    public void textEvent() throws Exception {
        testString("", "document.createEvent('TextEvent')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.TouchEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "changedTouches,composed,composedPath(),ctrlKey,currentTarget,defaultPrevented,detail,eventPhase,"
                + "initEvent(),initUIEvent(),isTrusted,metaKey,NONE,path,preventDefault(),returnValue,shiftKey,"
                + "sourceCapabilities,srcElement,stopImmediatePropagation(),stopPropagation(),target,targetTouches,"
                + "timeStamp,touches,type,view,which",
            EDGE = "altKey,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "changedTouches,composed,composedPath(),ctrlKey,currentTarget,defaultPrevented,detail,eventPhase,"
                + "initEvent(),initUIEvent(),isTrusted,metaKey,NONE,path,preventDefault(),returnValue,shiftKey,"
                + "sourceCapabilities,srcElement,stopImmediatePropagation(),stopPropagation(),target,targetTouches,"
                + "timeStamp,touches,type,view,which",
            FF = "exception",
            FF78 = "exception",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),NONE,preventDefault(),"
                + "returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,detail,eventPhase,initEvent(),initUIEvent(),NONE,preventDefault(),"
                + "returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,view")
    public void touchEvent() throws Exception {
        testString("", "new TouchEvent('touch')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSlot}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "assign(),assignedElements(),assignedNodes(),name",
            EDGE = "assign(),assignedElements(),assignedNodes(),name",
            FF = "assignedElements(),assignedNodes(),name",
            FF78 = "assignedElements(),assignedNodes(),name",
            IE = "-")
    @HtmlUnitNYI(CHROME = "-",
            EDGE = "-",
            FF78 = "-",
            FF = "-")
    public void slot() throws Exception {
        test("slot");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-")
    @HtmlUnitNYI(CHROME = "open(),write(),writeln()",
            EDGE = "open(),write(),writeln()",
            FF78 = "close(),cookie,getElementsByName(),getSelection(),head,"
                + "open(),write(),writeln()",
            FF = "close(),cookie,getElementsByName(),getSelection(),head,"
                + "open(),write(),writeln()",
            IE = "getSelection(),open(),write(),writeln()")
    public void htmlDocument() throws Exception {
        testString("", "document, xmlDocument");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Document}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"activeElement,adoptedStyleSheets,adoptNode(),alinkColor,all,anchors,append(),applets,bgColor,"
                + "body,captureEvents(),caretRangeFromPoint(),characterSet,charset,childElementCount,children,"
                + "clear(),close(),compatMode,contentType,cookie,createAttribute(),createAttributeNS(),"
                + "createCDATASection(),createComment(),createDocumentFragment(),createElement(),createElementNS(),"
                + "createEvent(),createExpression(),createNodeIterator(),createNSResolver(),"
                + "createProcessingInstruction(),createRange(),createTextNode(),createTreeWalker(),currentScript,"
                + "defaultView,designMode,dir,doctype,documentElement,documentURI,domain,elementFromPoint(),"
                + "elementsFromPoint(),embeds,evaluate(),execCommand(),exitFullscreen(),exitPictureInPicture(),"
                + "exitPointerLock(),featurePolicy,fgColor,firstElementChild,fonts,forms,fragmentDirective,"
                + "fullscreen,fullscreenElement,fullscreenEnabled,getAnimations(),getElementById(),"
                + "getElementsByClassName(),getElementsByName(),getElementsByTagName(),getElementsByTagNameNS(),"
                + "getSelection(),hasFocus(),head,hidden,images,implementation,importNode(),inputEncoding,"
                + "lastElementChild,lastModified,linkColor,links,location,onabort,onanimationend,"
                + "onanimationiteration,onanimationstart,onauxclick,onbeforecopy,onbeforecut,onbeforepaste,onblur,"
                + "oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,"
                + "oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,onformdata,onfreeze,onfullscreenchange,"
                + "onfullscreenerror,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,"
                + "onplaying,onpointercancel",
                "onpointerdown,onpointerenter,onpointerleave,onpointerlockchange,"
                + "onpointerlockerror,onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,"
                + "onprogress,onratechange,onreadystatechange,onreset,onresize,onresume,onscroll,onsearch,"
                + "onsecuritypolicyviolation,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                + "ontransitionstart,onvisibilitychange,onvolumechange,onwaiting,onwebkitanimationend,"
                + "onwebkitanimationiteration,onwebkitanimationstart,onwebkitfullscreenchange,"
                + "onwebkitfullscreenerror,onwebkittransitionend,onwheel,open(),pictureInPictureElement,"
                + "pictureInPictureEnabled,plugins,pointerLockElement,prepend(),queryCommandEnabled(),"
                + "queryCommandIndeterm(),queryCommandState(),queryCommandSupported(),queryCommandValue(),"
                + "querySelector(),querySelectorAll(),readyState,referrer,releaseEvents(),replaceChildren(),"
                + "rootElement,scripts,scrollingElement,styleSheets,timeline,title,URL,visibilityState,vlinkColor,"
                + "wasDiscarded,webkitCancelFullScreen(),webkitCurrentFullScreenElement,webkitExitFullscreen(),"
                + "webkitFullscreenElement,webkitFullscreenEnabled,webkitHidden,webkitIsFullScreen,"
                + "webkitVisibilityState,write(),writeln(),xmlEncoding,xmlStandalone,"
                + "xmlVersion"},
            EDGE = {"activeElement,adoptedStyleSheets,adoptNode(),alinkColor,all,anchors,append(),applets,bgColor,"
                + "body,captureEvents(),caretRangeFromPoint(),characterSet,charset,childElementCount,children,"
                + "clear(),close(),compatMode,contentType,cookie,createAttribute(),createAttributeNS(),"
                + "createCDATASection(),createComment(),createDocumentFragment(),createElement(),createElementNS(),"
                + "createEvent(),createExpression(),createNodeIterator(),createNSResolver(),"
                + "createProcessingInstruction(),createRange(),createTextNode(),createTreeWalker(),currentScript,"
                + "defaultView,designMode,dir,doctype,documentElement,documentURI,domain,elementFromPoint(),"
                + "elementsFromPoint(),embeds,evaluate(),execCommand(),exitFullscreen(),exitPictureInPicture(),"
                + "exitPointerLock(),featurePolicy,fgColor,firstElementChild,fonts,forms,fragmentDirective,"
                + "fullscreen,fullscreenElement,fullscreenEnabled,getAnimations(),getElementById(),"
                + "getElementsByClassName(),getElementsByName(),getElementsByTagName(),getElementsByTagNameNS(),"
                + "getSelection(),hasFocus(),hasStorageAccess(),head,hidden,images,implementation,importNode(),"
                + "inputEncoding,lastElementChild,lastModified,linkColor,links,location,onabort,onanimationend,"
                + "onanimationiteration,onanimationstart,onauxclick,onbeforecopy,onbeforecut,onbeforepaste,onblur,"
                + "oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,"
                + "oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,onformdata,onfreeze,onfullscreenchange,"
                + "onfullscreenerror,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,"
                + "onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,"
                + "onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,"
                + "onplaying",
                "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointerlockchange,"
                    + "onpointerlockerror,onpointermove,onpointerout,onpointerover,onpointerrawupdate,onpointerup,"
                    + "onprogress,onratechange,onreadystatechange,onreset,onresize,onresume,onscroll,onsearch,"
                    + "onsecuritypolicyviolation,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,"
                    + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,"
                    + "ontransitionstart,onvisibilitychange,onvolumechange,onwaiting,onwebkitanimationend,"
                    + "onwebkitanimationiteration,onwebkitanimationstart,onwebkitfullscreenchange,"
                    + "onwebkitfullscreenerror,onwebkittransitionend,onwheel,open(),pictureInPictureElement,"
                    + "pictureInPictureEnabled,plugins,pointerLockElement,prepend(),queryCommandEnabled(),"
                    + "queryCommandIndeterm(),queryCommandState(),queryCommandSupported(),queryCommandValue(),"
                    + "querySelector(),querySelectorAll(),readyState,referrer,releaseEvents(),replaceChildren(),"
                    + "requestStorageAccess(),rootElement,scripts,scrollingElement,styleSheets,timeline,title,URL,"
                    + "visibilityState,vlinkColor,wasDiscarded,webkitCancelFullScreen(),webkitCurrentFullScreenElement,"
                    + "webkitExitFullscreen(),webkitFullscreenElement,webkitFullscreenEnabled,webkitHidden,"
                    + "webkitIsFullScreen,webkitVisibilityState,write(),writeln(),xmlEncoding,xmlStandalone,"
                    + "xmlVersion"},
            FF = {"activeElement,adoptNode(),alinkColor,all,anchors,append(),applets,bgColor,body,captureEvents(),"
                + "caretPositionFromPoint(),characterSet,charset,childElementCount,children,clear(),close(),"
                + "compatMode,contentType,cookie,createAttribute(),createAttributeNS(),createCDATASection(),"
                + "createComment(),createDocumentFragment(),createElement(),createElementNS(),createEvent(),"
                + "createExpression(),createNodeIterator(),createNSResolver(),createProcessingInstruction(),"
                + "createRange(),createTextNode(),createTreeWalker(),currentScript,defaultView,designMode,dir,"
                + "doctype,documentElement,documentURI,domain,elementFromPoint(),elementsFromPoint(),embeds,"
                + "enableStyleSheetsForSet(),evaluate(),execCommand(),exitFullscreen(),exitPointerLock(),fgColor,"
                + "firstElementChild,fonts,forms,fullscreen,fullscreenElement,fullscreenEnabled,getAnimations(),"
                + "getElementById(),"
                + "getElementsByClassName(),getElementsByName(),getElementsByTagName(),getElementsByTagNameNS(),"
                + "getSelection(),hasFocus(),hasStorageAccess(),head,hidden,images,implementation,importNode(),"
                + "inputEncoding,lastElementChild,lastModified,lastStyleSheetSet,linkColor,links,location,"
                + "mozCancelFullScreen(),mozFullScreen,mozFullScreenElement,mozFullScreenEnabled,"
                + "mozSetImageElement(),onabort,onafterscriptexecute,onanimationcancel,onanimationend,"
                + "onanimationiteration,onanimationstart,onauxclick,onbeforescriptexecute,onblur,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragexit,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,onformdata,onfullscreenchange,"
                + "onfullscreenerror,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onload,onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmozfullscreenchange,onmozfullscreenerror",
                "onpaste,onpause,onplay,onplaying,"
                    + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointerlockchange,"
                    + "onpointerlockerror,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,"
                    + "onratechange,onreadystatechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                    + "onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                    + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                    + "onvisibilitychange,onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,"
                    + "onwebkitanimationstart,onwebkittransitionend,onwheel,open(),plugins,pointerLockElement,"
                    + "preferredStyleSheetSet,prepend(),queryCommandEnabled(),queryCommandIndeterm(),"
                    + "queryCommandState(),queryCommandSupported(),queryCommandValue(),querySelector(),"
                    + "querySelectorAll(),readyState,referrer,releaseCapture(),releaseEvents(),replaceChildren(),"
                    + "requestStorageAccess(),"
                    + "rootElement,scripts,scrollingElement,selectedStyleSheetSet,styleSheets,styleSheetSets,"
                    + "timeline,title,URL,visibilityState,vlinkColor,write(),writeln()"},
            FF78 = {"activeElement,adoptNode(),alinkColor,all,anchors,append(),applets,bgColor,"
                + "body,captureEvents(),caretPositionFromPoint(),"
                + "characterSet,charset,childElementCount,children,clear(),close(),"
                + "compatMode,contentType,cookie,createAttribute(),"
                + "createAttributeNS(),createCDATASection(),createComment(),createDocumentFragment(),"
                + "createElement(),createElementNS(),createEvent(),createExpression(),createNodeIterator(),"
                + "createNSResolver(),createProcessingInstruction(),createRange(),createTextNode(),createTreeWalker(),"
                + "currentScript,defaultView,designMode,dir,doctype,documentElement,documentURI,domain,"
                + "elementFromPoint(),"
                + "elementsFromPoint(),embeds,enableStyleSheetsForSet(),evaluate(),execCommand(),"
                + "exitFullscreen(),exitPointerLock(),fgColor,"
                + "firstElementChild,fonts,forms,fullscreen,fullscreenElement,fullscreenEnabled,getAnimations(),"
                + "getElementById(),"
                + "getElementsByClassName(),getElementsByName(),getElementsByTagName(),getElementsByTagNameNS(),"
                + "getSelection(),hasFocus(),hasStorageAccess(),head,hidden,images,implementation,importNode(),"
                + "inputEncoding,lastElementChild,lastModified,lastStyleSheetSet,linkColor,links,location,"
                + "mozCancelFullScreen(),mozFullScreen,mozFullScreenElement,mozFullScreenEnabled,mozSetImageElement(),"
                + "onabort,onafterscriptexecute,onanimationcancel,onanimationend,onanimationiteration,onanimationstart,"
                + "onauxclick,onbeforescriptexecute,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,"
                + "onfullscreenchange,onfullscreenerror,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror",
                "onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                    + "onpointerleave,onpointerlockchange,onpointerlockerror,onpointermove,"
                    + "onpointerout,onpointerover,"
                    + "onpointerup,onprogress,onratechange,onreadystatechange,onreset,onresize,onscroll,onseeked,"
                    + "onseeking,"
                    + "onselect,onselectionchange,onselectstart,onshow,onstalled,"
                    + "onsubmit,onsuspend,ontimeupdate,ontoggle,"
                    + "ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,onvisibilitychange,"
                    + "onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                    + "onwebkittransitionend,onwheel,open(),plugins,pointerLockElement,"
                    + "preferredStyleSheetSet,prepend(),"
                    + "queryCommandEnabled(),queryCommandIndeterm(),queryCommandState(),"
                    + "queryCommandSupported(),queryCommandValue(),"
                    + "querySelector(),querySelectorAll(),readyState,referrer,releaseCapture(),"
                    + "releaseEvents(),replaceChildren(),requestStorageAccess(),"
                    + "rootElement,scripts,scrollingElement,"
                    + "selectedStyleSheetSet,styleSheets,styleSheetSets,timeline,title,URL,visibilityState,"
                    + "vlinkColor,write(),writeln()"},
            IE = {"activeElement,adoptNode(),alinkColor,all,anchors,applets,bgColor,body,captureEvents(),characterSet,"
                + "charset,clear(),close(),compatible,compatMode,cookie,createAttribute(),createAttributeNS(),"
                + "createCDATASection(),createComment(),createDocumentFragment(),createElement(),createElementNS(),"
                + "createEvent(),createNodeIterator(),createProcessingInstruction(),createRange(),createTextNode(),"
                + "createTreeWalker(),defaultCharset,defaultView,designMode,dir,doctype,documentElement,documentMode,"
                + "domain,elementFromPoint(),embeds,execCommand(),execCommandShowHelp(),fgColor,fileCreatedDate,"
                + "fileModifiedDate,fileUpdatedDate,focus(),forms,frames,getElementById(),getElementsByClassName(),"
                + "getElementsByName(),getElementsByTagName(),getElementsByTagNameNS(),getSelection(),hasFocus(),head,"
                + "hidden,images,implementation,importNode(),inputEncoding,lastModified,linkColor,links,location,media,"
                + "mimeType,msCapsLockWarningOff,msCSSOMElementFloatMetrics,msElementsFromPoint(),msElementsFromRect(),"
                + "msExitFullscreen(),msFullscreenElement,msFullscreenEnabled,msHidden,msVisibilityState,nameProp,"
                + "onabort,onactivate,onbeforeactivate,onbeforedeactivate,onblur,oncanplay,oncanplaythrough,onchange,"
                + "onclick,oncontextmenu,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhelp,"
                + "oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onmscontentzoom,onmsfullscreenchange,"
                + "onmsfullscreenerror,onmsgesturechange,onmsgesturedoubletap,onmsgestureend,onmsgesturehold,"
                + "onmsgesturestart,onmsgesturetap,onmsinertiastart,onmsmanipulationstatechanged,onmspointercancel,"
                + "onmspointerdown,onmspointerenter,onmspointerleave,onmspointermove,onmspointerout,onmspointerover,"
                + "onmspointerup,onmssitemodejumplistitemremoved,onmsthumbnailclick,onpause",
                "onplay,onplaying,"
                    + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                    + "onpointerout,"
                    + "onpointerover,onpointerup,onprogress,onratechange,onreadystatechange,onreset,onscroll,onseeked,"
                    + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onstop,onstoragecommit,onsubmit,"
                    + "onsuspend,ontimeupdate,onvolumechange,onwaiting,open(),parentWindow,plugins,protocol,"
                    + "queryCommandEnabled(),queryCommandIndeterm(),queryCommandState(),queryCommandSupported(),"
                    + "queryCommandText(),queryCommandValue(),querySelector(),querySelectorAll(),readyState,referrer,"
                    + "releaseCapture(),releaseEvents(),rootElement,scripts,security,styleSheets,title,uniqueID,"
                    + "updateSettings(),URL,URLUnencoded,visibilityState,vlinkColor,write(),writeln(),xmlEncoding,"
                    + "xmlStandalone,xmlVersion"})
    @HtmlUnitNYI(CHROME = {"activeElement,adoptNode(),alinkColor,all,anchors,applets,bgColor,body,"
                + "captureEvents(),characterSet,charset,childElementCount,"
                + "children,clear(),close(),compatMode,contentType,cookie,createAttribute(),createCDATASection(),"
                + "createComment(),createDocumentFragment(),createElement(),createElementNS(),createEvent(),"
                + "createNodeIterator(),createNSResolver(),createProcessingInstruction(),createRange(),"
                + "createTextNode(),createTreeWalker(),currentScript,defaultView,designMode,doctype,"
                + "documentElement,documentURI,domain,elementFromPoint(),embeds,evaluate(),execCommand(),"
                + "fgColor,firstElementChild,fonts,forms,getElementById(),getElementsByClassName(),getElementsByName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),getSelection(),hasFocus(),head,hidden,images,"
                + "implementation,importNode(),inputEncoding,lastElementChild,lastModified,linkColor,links,location,"
                + "onabort,onauxclick,onbeforecopy,onbeforecut,onbeforepaste,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointerlockchange,onpointerlockerror,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreadystatechange,onreset,onresize,onscroll,onsearch,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit",
                "onsuspend,ontimeupdate,ontoggle,onvolumechange,onwaiting,"
                    + "onwebkitfullscreenchange,onwebkitfullscreenerror,onwheel,"
                    + "plugins,queryCommandEnabled(),queryCommandSupported(),"
                    + "querySelector(),querySelectorAll(),readyState,referrer,releaseEvents(),rootElement,"
                    + "scripts,styleSheets,title,URL,vlinkColor,xmlEncoding,xmlStandalone,xmlVersion"},
            EDGE = {"activeElement,adoptNode(),alinkColor,all,anchors,applets,bgColor,body,"
                + "captureEvents(),characterSet,charset,childElementCount,"
                + "children,clear(),close(),compatMode,contentType,cookie,createAttribute(),createCDATASection(),"
                + "createComment(),createDocumentFragment(),createElement(),createElementNS(),createEvent(),"
                + "createNodeIterator(),createNSResolver(),createProcessingInstruction(),createRange(),"
                + "createTextNode(),createTreeWalker(),currentScript,defaultView,designMode,doctype,"
                + "documentElement,documentURI,domain,elementFromPoint(),embeds,evaluate(),execCommand(),"
                + "fgColor,firstElementChild,fonts,forms,getElementById(),getElementsByClassName(),getElementsByName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),getSelection(),hasFocus(),"
                + "head,hidden,images,"
                + "implementation,importNode(),inputEncoding,lastElementChild,lastModified,linkColor,links,location,"
                + "onabort,onauxclick,onbeforecopy,onbeforecut,onbeforepaste,onblur,oncancel,oncanplay,"
                + "oncanplaythrough,onchange,onclick,onclose,oncontextmenu,oncopy,oncuechange,oncut,ondblclick,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointerlockchange,onpointerlockerror,onpointermove,onpointerout,onpointerover,onpointerup,"
                + "onprogress,onratechange,onreadystatechange,onreset,onresize,onscroll,onsearch,onseeked,"
                + "onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit",
                "onsuspend,ontimeupdate,ontoggle,onvolumechange,onwaiting,onwebkitfullscreenchange,"
                    + "onwebkitfullscreenerror,onwheel,"
                    + "plugins,queryCommandEnabled(),queryCommandSupported(),"
                    + "querySelector(),querySelectorAll(),readyState,referrer,releaseEvents(),rootElement,"
                    + "scripts,styleSheets,title,URL,vlinkColor,xmlEncoding,xmlStandalone,xmlVersion"},
            FF78 = "activeElement,adoptNode(),alinkColor,all,anchors,applets,bgColor,body,"
                + "captureEvents(),characterSet,charset,childElementCount,children,clear(),compatMode,"
                + "contentType,createAttribute(),createCDATASection(),createComment(),createDocumentFragment(),"
                + "createElement(),createElementNS(),createEvent(),createNodeIterator(),createNSResolver(),"
                + "createProcessingInstruction(),createRange(),createTextNode(),createTreeWalker(),"
                + "currentScript,defaultView,designMode,doctype,documentElement,documentURI,domain,elementFromPoint(),"
                + "embeds,evaluate(),execCommand(),fgColor,firstElementChild,fonts,forms,"
                + "getElementById(),getElementsByClassName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasFocus(),hidden,images,implementation,"
                + "importNode(),inputEncoding,lastElementChild,lastModified,linkColor,links,location,onabort,"
                + "onafterscriptexecute,onbeforescriptexecute,onblur,oncanplay,oncanplaythrough,onchange,"
                + "onclick,oncontextmenu,oncopy,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onprogress,"
                + "onratechange,onreadystatechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onshow,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,onwheel,"
                + "plugins,queryCommandEnabled(),queryCommandSupported(),"
                + "querySelector(),querySelectorAll(),readyState,referrer,releaseCapture(),releaseEvents(),rootElement,"
                + "scripts,styleSheets,title,URL,vlinkColor",
            FF = "activeElement,adoptNode(),alinkColor,all,anchors,applets,bgColor,body,"
                + "captureEvents(),characterSet,charset,childElementCount,children,clear(),compatMode,"
                + "contentType,createAttribute(),createCDATASection(),createComment(),createDocumentFragment(),"
                + "createElement(),createElementNS(),createEvent(),createNodeIterator(),createNSResolver(),"
                + "createProcessingInstruction(),createRange(),createTextNode(),createTreeWalker(),"
                + "currentScript,defaultView,designMode,doctype,documentElement,documentURI,domain,elementFromPoint(),"
                + "embeds,evaluate(),execCommand(),fgColor,firstElementChild,fonts,forms,"
                + "getElementById(),getElementsByClassName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasFocus(),hidden,images,implementation,"
                + "importNode(),inputEncoding,lastElementChild,lastModified,linkColor,links,location,onabort,"
                + "onafterscriptexecute,onbeforescriptexecute,onblur,oncanplay,oncanplaythrough,onchange,"
                + "onclick,oncontextmenu,oncopy,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,oninput,"
                + "oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmozfullscreenchange,onmozfullscreenerror,onpaste,onpause,onplay,onplaying,onprogress,"
                + "onratechange,onreadystatechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,onwheel,"
                + "plugins,queryCommandEnabled(),queryCommandSupported(),"
                + "querySelector(),querySelectorAll(),readyState,referrer,releaseCapture(),releaseEvents(),rootElement,"
                + "scripts,styleSheets,title,URL,vlinkColor",
            IE = {"activeElement,adoptNode(),alinkColor,all,anchors,applets,bgColor,body,captureEvents(),characterSet,"
                + "charset,clear(),close(),compatMode,cookie,createAttribute(),createCDATASection(),createComment(),"
                + "createDocumentFragment(),createElement(),createElementNS(),createEvent(),createNodeIterator(),"
                + "createProcessingInstruction(),createRange(),createTextNode(),createTreeWalker(),defaultCharset,"
                + "defaultView,designMode,doctype,documentElement,documentMode,domain,elementFromPoint(),embeds,"
                + "execCommand(),fgColor,forms,frames,getElementById(),getElementsByClassName(),getElementsByName(),"
                + "getElementsByTagName(),getElementsByTagNameNS(),hasFocus(),head,hidden,images,implementation,"
                + "importNode(),inputEncoding,lastModified,linkColor,links,location,onabort,onactivate,"
                + "onbeforeactivate,onbeforedeactivate,onblur,oncanplay,oncanplaythrough,onchange,onclick,"
                + "oncontextmenu,ondblclick,ondeactivate,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,"
                + "onhelp,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onmousedown,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onmscontentzoom,"
                + "onmsfullscreenchange,onmsfullscreenerror,onmsgesturechange,onmsgesturedoubletap,onmsgestureend,"
                + "onmsgesturehold,onmsgesturestart,onmsgesturetap,onmsinertiastart,onmsmanipulationstatechanged,"
                + "onmspointercancel,onmspointerdown,onmspointerenter,onmspointerleave,onmspointermove,"
                + "onmspointerout,onmspointerover,onmspointerup,onmssitemodejumplistitemremoved,onmsthumbnailclick,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,"
                + "onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreadystatechange,"
                + "onreset,onscroll,onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled",
                "onstop,onstoragecommit,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,parentWindow,plugins,"
                    + "queryCommandEnabled(),queryCommandSupported(),querySelector(),querySelectorAll(),readyState,"
                    + "referrer,releaseCapture(),releaseEvents(),rootElement,scripts,styleSheets,title,uniqueID,URL,"
                    + "URLUnencoded,vlinkColor,xmlEncoding,xmlStandalone,xmlVersion"})
    public void document() throws Exception {
        testString("", "xmlDocument, document.createTextNode('some text')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "autofocus,blur(),dataset,focus(),nonce,onabort,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onformdata,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,"
                + "onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                + "onwebkittransitionend,onwheel,ownerSVGElement,style,tabIndex,"
                + "viewportElement",
            EDGE = "autofocus,blur(),dataset,focus(),nonce,onabort,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onformdata,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpaste,onpause,onplay,onplaying,"
                + "onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,onpointerout,"
                + "onpointerover,onpointerrawupdate,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,"
                + "onseeked,onseeking,onselect,onselectionchange,onselectstart,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,"
                + "onvolumechange,onwaiting,onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                + "onwebkittransitionend,onwheel,ownerSVGElement,style,tabIndex,"
                + "viewportElement",
            FF = "blur(),dataset,focus(),nonce,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "onformdata,ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,"
                + "onseeked,onseeking,onselect,onselectstart,onstalled,onsubmit,onsuspend,ontimeupdate,"
                + "ontoggle,ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,onvolumechange,"
                + "onwaiting,onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,"
                + "onwebkittransitionend,onwheel,ownerSVGElement,style,tabIndex,viewportElement",
            FF78 = "blur(),dataset,focus(),nonce,onabort,onanimationcancel,onanimationend,onanimationiteration,"
                + "onanimationstart,onauxclick,onblur,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,ondblclick,ondrag,ondragend,ondragenter,ondragexit,"
                + "ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,"
                + "onfocus,onformdata,"
                + "ongotpointercapture,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,"
                + "onloadedmetadata,onloadend,onloadstart,onlostpointercapture,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,onpointerleave,onpointermove,"
                + "onpointerout,onpointerover,onpointerup,onprogress,onratechange,onreset,onresize,onscroll,onseeked,"
                + "onseeking,onselect,onselectstart,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,"
                + "ontransitioncancel,ontransitionend,ontransitionrun,ontransitionstart,onvolumechange,onwaiting,"
                + "onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,"
                + "onwheel,ownerSVGElement,style,tabIndex,viewportElement",
            IE = "-")
    @HtmlUnitNYI(CHROME = "onabort,onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,onvolumechange,onwaiting,onwheel,style",
            EDGE = "onabort,onauxclick,onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,onclose,"
                + "oncontextmenu,oncopy,oncuechange,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,ongotpointercapture,"
                + "oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,"
                + "onlostpointercapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,"
                + "onmouseup,onmousewheel,onpaste,"
                + "onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onseeked,onseeking,onselect,onstalled,onsubmit,onsuspend,"
                + "ontimeupdate,ontoggle,onvolumechange,onwaiting,onwheel,style",
            FF78 = "onabort,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,oninput,oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onshow,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,style",
            FF = "onabort,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncut,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,oninput,oninvalid,onkeydown,onkeypress,"
                + "onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,onmouseenter,onmouseleave,"
                + "onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,onmozfullscreenerror,onpaste,"
                + "onpause,onplay,onplaying,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,"
                + "onselect,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,style")
    public void svgElement() throws Exception {
        testString("", "svg, element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childNodes,cloneNode(),COMMENT_NODE,"
                + "compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,DOCUMENT_POSITION_DISCONNECTED,"
                + "DOCUMENT_POSITION_FOLLOWING,DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,"
                + "DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,getRootNode(),"
                + "hasChildNodes(),insertBefore(),isConnected,isDefaultNamespace(),isEqualNode(),isSameNode(),"
                + "lastChild,localName,lookupNamespaceURI(),lookupPrefix(),name,namespaceURI,nextSibling,nodeName,"
                + "nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,ownerElement,parentElement,parentNode,"
                + "prefix,previousSibling,PROCESSING_INSTRUCTION_NODE,removeChild(),replaceChild(),specified,TEXT_NODE,"
                + "textContent,value",
            EDGE = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childNodes,cloneNode(),COMMENT_NODE,"
                + "compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,DOCUMENT_POSITION_DISCONNECTED,"
                + "DOCUMENT_POSITION_FOLLOWING,DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,"
                + "DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,getRootNode(),"
                + "hasChildNodes(),insertBefore(),isConnected,isDefaultNamespace(),isEqualNode(),isSameNode(),"
                + "lastChild,localName,lookupNamespaceURI(),lookupPrefix(),name,namespaceURI,nextSibling,nodeName,"
                + "nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,ownerElement,parentElement,parentNode,"
                + "prefix,previousSibling,PROCESSING_INSTRUCTION_NODE,removeChild(),replaceChild(),specified,TEXT_NODE,"
                + "textContent,value",
            FF = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childNodes,cloneNode(),COMMENT_NODE,"
                + "compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,DOCUMENT_POSITION_DISCONNECTED,"
                + "DOCUMENT_POSITION_FOLLOWING,DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,"
                + "DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,getRootNode(),"
                + "hasChildNodes(),insertBefore(),isConnected,isDefaultNamespace(),isEqualNode(),isSameNode(),"
                + "lastChild,localName,lookupNamespaceURI(),lookupPrefix(),name,namespaceURI,nextSibling,nodeName,"
                + "nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,ownerElement,parentElement,parentNode,"
                + "prefix,previousSibling,PROCESSING_INSTRUCTION_NODE,removeChild(),replaceChild(),specified,"
                + "TEXT_NODE,textContent,value",
            FF78 = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childNodes,cloneNode(),COMMENT_NODE,"
                + "compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,DOCUMENT_POSITION_DISCONNECTED,"
                + "DOCUMENT_POSITION_FOLLOWING,DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,"
                + "DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,getRootNode(),"
                + "hasChildNodes(),insertBefore(),isConnected,isDefaultNamespace(),isEqualNode(),isSameNode(),"
                + "lastChild,localName,lookupNamespaceURI(),lookupPrefix(),name,namespaceURI,nextSibling,nodeName,"
                + "nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,ownerElement,parentElement,"
                + "parentNode,prefix,previousSibling,PROCESSING_INSTRUCTION_NODE,removeChild(),"
                + "replaceChild(),specified,TEXT_NODE,textContent,value",
            IE = "addEventListener(),appendChild(),ATTRIBUTE_NODE,attributes,CDATA_SECTION_NODE,childNodes,cloneNode(),"
                + "COMMENT_NODE,compareDocumentPosition(),dispatchEvent(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,DOCUMENT_POSITION_DISCONNECTED,"
                + "DOCUMENT_POSITION_FOLLOWING,DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,"
                + "DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,expando,firstChild,"
                + "hasAttributes(),hasChildNodes(),insertBefore(),isDefaultNamespace(),isEqualNode(),isSameNode(),"
                + "isSupported(),lastChild,localName,lookupNamespaceURI(),lookupPrefix(),name,namespaceURI,nextSibling,"
                + "nodeName,nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,ownerElement,parentNode,prefix,"
                + "previousSibling,PROCESSING_INSTRUCTION_NODE,removeChild(),removeEventListener(),replaceChild(),"
                + "specified,TEXT_NODE,textContent,value")
    @HtmlUnitNYI(CHROME = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childNodes,cloneNode(),COMMENT_NODE,"
                + "compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,DOCUMENT_POSITION_DISCONNECTED,"
                + "DOCUMENT_POSITION_FOLLOWING,DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,"
                + "DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,hasChildNodes(),"
                + "insertBefore(),isSameNode(),lastChild,localName,name,namespaceURI,nextSibling,nodeName,nodeType,"
                + "nodeValue,normalize(),NOTATION_NODE,ownerDocument,ownerElement,parentElement,parentNode,prefix,"
                + "previousSibling,PROCESSING_INSTRUCTION_NODE,removeChild(),replaceChild(),specified,TEXT_NODE,"
                + "textContent,value",
            EDGE = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childNodes,cloneNode(),COMMENT_NODE,"
                + "compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,DOCUMENT_POSITION_DISCONNECTED,"
                + "DOCUMENT_POSITION_FOLLOWING,DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,"
                + "DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,hasChildNodes(),"
                + "insertBefore(),isSameNode(),lastChild,localName,name,namespaceURI,nextSibling,nodeName,nodeType,"
                + "nodeValue,normalize(),NOTATION_NODE,ownerDocument,ownerElement,parentElement,parentNode,prefix,"
                + "previousSibling,PROCESSING_INSTRUCTION_NODE,removeChild(),replaceChild(),specified,TEXT_NODE,"
                + "textContent,value",
            FF78 = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childNodes,cloneNode(),COMMENT_NODE,"
                + "compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,"
                + "DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,"
                + "firstChild,hasChildNodes(),insertBefore(),isSameNode(),lastChild,localName,name,namespaceURI,"
                + "nextSibling,nodeName,nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,ownerElement,"
                + "parentElement,parentNode,prefix,previousSibling,PROCESSING_INSTRUCTION_NODE,removeChild(),"
                + "replaceChild(),specified,TEXT_NODE,textContent,value",
            FF = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childNodes,cloneNode(),COMMENT_NODE,"
                + "compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,"
                + "DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,"
                + "firstChild,hasChildNodes(),insertBefore(),isSameNode(),lastChild,localName,name,namespaceURI,"
                + "nextSibling,nodeName,nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,ownerElement,"
                + "parentElement,parentNode,prefix,previousSibling,PROCESSING_INSTRUCTION_NODE,removeChild(),"
                + "replaceChild(),specified,TEXT_NODE,textContent,value",
            IE = "addEventListener(),appendChild(),ATTRIBUTE_NODE,attributes,CDATA_SECTION_NODE,childNodes,"
                + "cloneNode(),COMMENT_NODE,compareDocumentPosition(),dispatchEvent(),DOCUMENT_FRAGMENT_NODE,"
                + "DOCUMENT_NODE,DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,"
                + "ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,expando,firstChild,hasAttributes(),"
                + "hasChildNodes(),insertBefore(),isSameNode(),lastChild,localName,name,namespaceURI,nextSibling,"
                + "nodeName,nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,ownerElement,parentNode,"
                + "prefix,previousSibling,PROCESSING_INSTRUCTION_NODE,removeChild(),removeEventListener(),"
                + "replaceChild(),specified,TEXT_NODE,textContent,value")
    public void nodeAndAttr() throws Exception {
        testString("", "document.createAttribute('some_attrib'), window.performance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),comparePoint(),createContextualFragment(),deleteContents(),detach(),"
                + "END_TO_END,END_TO_START,endContainer,endOffset,expand(),extractContents(),getBoundingClientRect(),"
                + "getClientRects(),insertNode(),intersectsNode(),isPointInRange(),selectNode(),selectNodeContents(),"
                + "setEnd(),setEndAfter(),setEndBefore(),setStart(),setStartAfter(),setStartBefore(),START_TO_END,"
                + "START_TO_START,startContainer,startOffset,surroundContents()",
            IE = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,compareBoundaryPoints(),"
                + "createContextualFragment(),deleteContents(),detach(),END_TO_END,END_TO_START,endContainer,endOffset,"
                + "extractContents(),getBoundingClientRect(),getClientRects(),insertNode(),selectNode(),"
                + "selectNodeContents(),setEnd(),setEndAfter(),setEndBefore(),setStart(),setStartAfter(),"
                + "setStartBefore(),START_TO_END,START_TO_START,startContainer,startOffset,surroundContents()",
            FF78 = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),comparePoint(),createContextualFragment(),deleteContents(),detach(),"
                + "END_TO_END,END_TO_START,endContainer,endOffset,extractContents(),getBoundingClientRect(),"
                + "getClientRects(),insertNode(),intersectsNode(),isPointInRange(),selectNode(),selectNodeContents(),"
                + "setEnd(),setEndAfter(),setEndBefore(),setStart(),setStartAfter(),setStartBefore(),START_TO_END,"
                + "START_TO_START,startContainer,startOffset,surroundContents()",
            FF = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,compareBoundaryPoints(),"
                + "comparePoint(),createContextualFragment(),deleteContents(),detach(),END_TO_END,END_TO_START,"
                + "endContainer,endOffset,extractContents(),getBoundingClientRect(),getClientRects(),insertNode(),"
                + "intersectsNode(),isPointInRange(),selectNode(),selectNodeContents(),setEnd(),setEndAfter(),"
                + "setEndBefore(),setStart(),setStartAfter(),setStartBefore(),START_TO_END,START_TO_START,"
                + "startContainer,startOffset,surroundContents()")
    @HtmlUnitNYI(CHROME = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),createContextualFragment(),deleteContents(),detach(),END_TO_END,"
                + "END_TO_START,endContainer,endOffset,extractContents(),getBoundingClientRect(),getClientRects(),"
                + "insertNode(),selectNode(),selectNodeContents(),setEnd(),setEndAfter(),setEndBefore(),setStart(),"
                + "setStartAfter(),setStartBefore(),START_TO_END,START_TO_START,startContainer,startOffset,"
                + "surroundContents()",
            EDGE = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,"
                + "compareBoundaryPoints(),createContextualFragment(),deleteContents(),detach(),END_TO_END,"
                + "END_TO_START,endContainer,endOffset,extractContents(),getBoundingClientRect(),getClientRects(),"
                + "insertNode(),selectNode(),selectNodeContents(),setEnd(),setEndAfter(),setEndBefore(),setStart(),"
                + "setStartAfter(),setStartBefore(),START_TO_END,START_TO_START,startContainer,startOffset,"
                + "surroundContents()",
            FF78 = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,compareBoundaryPoints(),"
                + "createContextualFragment(),deleteContents(),detach(),END_TO_END,END_TO_START,endContainer,"
                + "endOffset,extractContents(),getBoundingClientRect(),getClientRects(),insertNode(),selectNode(),"
                + "selectNodeContents(),setEnd(),setEndAfter(),setEndBefore(),setStart(),setStartAfter(),"
                + "setStartBefore(),START_TO_END,START_TO_START,startContainer,startOffset,surroundContents()",
            FF = "cloneContents(),cloneRange(),collapse(),collapsed,commonAncestorContainer,compareBoundaryPoints(),"
                + "createContextualFragment(),deleteContents(),detach(),END_TO_END,END_TO_START,endContainer,"
                + "endOffset,extractContents(),getBoundingClientRect(),getClientRects(),insertNode(),selectNode(),"
                + "selectNodeContents(),setEnd(),setEndAfter(),setEndBefore(),setStart(),setStartAfter(),"
                + "setStartBefore(),START_TO_END,START_TO_START,startContainer,startOffset,surroundContents()")
    public void range() throws Exception {
        testString("", "document.createRange(), window.performance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "append(),appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childElementCount,childNodes,"
                + "children,cloneNode(),COMMENT_NODE,compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,"
                + "DOCUMENT_NODE,DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,"
                + "ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,firstElementChild,getElementById(),"
                + "getRootNode(),hasChildNodes(),insertBefore(),isConnected,isDefaultNamespace(),isEqualNode(),"
                + "isSameNode(),lastChild,lastElementChild,lookupNamespaceURI(),lookupPrefix(),nextSibling,nodeName,"
                + "nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,parentElement,parentNode,prepend(),"
                + "previousSibling,PROCESSING_INSTRUCTION_NODE,querySelector(),querySelectorAll(),removeChild(),"
                + "replaceChild(),replaceChildren(),TEXT_NODE,textContent",
            EDGE = "append(),appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childElementCount,childNodes,"
                + "children,cloneNode(),COMMENT_NODE,compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,"
                + "DOCUMENT_NODE,DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,"
                + "ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,firstElementChild,getElementById(),"
                + "getRootNode(),hasChildNodes(),insertBefore(),isConnected,isDefaultNamespace(),isEqualNode(),"
                + "isSameNode(),lastChild,lastElementChild,lookupNamespaceURI(),lookupPrefix(),nextSibling,nodeName,"
                + "nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,parentElement,parentNode,prepend(),"
                + "previousSibling,PROCESSING_INSTRUCTION_NODE,querySelector(),querySelectorAll(),removeChild(),"
                + "replaceChild(),replaceChildren(),TEXT_NODE,textContent",
            FF = "append(),appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childElementCount,childNodes,"
                + "children,cloneNode(),COMMENT_NODE,compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,"
                + "DOCUMENT_NODE,DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,"
                + "ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,firstElementChild,getElementById(),"
                + "getRootNode(),hasChildNodes(),insertBefore(),isConnected,isDefaultNamespace(),isEqualNode(),"
                + "isSameNode(),lastChild,lastElementChild,lookupNamespaceURI(),lookupPrefix(),nextSibling,nodeName,"
                + "nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,parentElement,parentNode,prepend(),"
                + "previousSibling,PROCESSING_INSTRUCTION_NODE,querySelector(),querySelectorAll(),removeChild(),"
                + "replaceChild(),replaceChildren(),TEXT_NODE,textContent",
            FF78 = "append(),appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childElementCount,childNodes,"
                + "children,cloneNode(),COMMENT_NODE,compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,"
                + "DOCUMENT_NODE,DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,"
                + "ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,firstElementChild,getElementById(),"
                + "getRootNode(),hasChildNodes(),insertBefore(),isConnected,isDefaultNamespace(),isEqualNode(),"
                + "isSameNode(),lastChild,lastElementChild,lookupNamespaceURI(),lookupPrefix(),nextSibling,nodeName,"
                + "nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,parentElement,parentNode,prepend(),"
                + "previousSibling,PROCESSING_INSTRUCTION_NODE,querySelector(),querySelectorAll(),removeChild(),"
                + "replaceChild(),replaceChildren(),TEXT_NODE,textContent",
            IE = "addEventListener(),appendChild(),ATTRIBUTE_NODE,attributes,CDATA_SECTION_NODE,childNodes,cloneNode(),"
                + "COMMENT_NODE,compareDocumentPosition(),dispatchEvent(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,DOCUMENT_POSITION_DISCONNECTED,"
                + "DOCUMENT_POSITION_FOLLOWING,DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,"
                + "DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,hasAttributes(),"
                + "hasChildNodes(),insertBefore(),isDefaultNamespace(),isEqualNode(),isSameNode(),isSupported(),"
                + "lastChild,localName,lookupNamespaceURI(),lookupPrefix(),namespaceURI,nextSibling,nodeName,nodeType,"
                + "nodeValue,normalize(),NOTATION_NODE,ownerDocument,parentNode,prefix,previousSibling,"
                + "PROCESSING_INSTRUCTION_NODE,querySelector(),querySelectorAll(),removeChild(),removeEventListener(),"
                + "removeNode(),replaceChild(),replaceNode(),swapNode(),TEXT_NODE,textContent")
    @HtmlUnitNYI(CHROME = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childElementCount,childNodes,"
                + "children,cloneNode(),COMMENT_NODE,compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,"
                + "DOCUMENT_NODE,DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,"
                + "DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,"
                + "firstChild,firstElementChild,getElementById(),hasChildNodes(),insertBefore(),isSameNode(),lastChild,"
                + "lastElementChild,nextSibling,nodeName,nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,"
                + "parentElement,parentNode,previousSibling,PROCESSING_INSTRUCTION_NODE,querySelector(),"
                + "querySelectorAll(),removeChild(),replaceChild(),TEXT_NODE,textContent",
            EDGE = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childElementCount,childNodes,"
                + "children,cloneNode(),COMMENT_NODE,compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,"
                + "DOCUMENT_NODE,DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,"
                + "DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,"
                + "firstChild,firstElementChild,getElementById(),hasChildNodes(),insertBefore(),isSameNode(),lastChild,"
                + "lastElementChild,nextSibling,nodeName,nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,"
                + "parentElement,parentNode,previousSibling,PROCESSING_INSTRUCTION_NODE,querySelector(),"
                + "querySelectorAll(),removeChild(),replaceChild(),TEXT_NODE,textContent",
            FF78 = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childElementCount,childNodes,"
                + "children,cloneNode(),COMMENT_NODE,compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,"
                + "DOCUMENT_NODE,DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,"
                + "DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,"
                + "firstChild,firstElementChild,getElementById(),hasChildNodes(),insertBefore(),isSameNode(),lastChild,"
                + "lastElementChild,nextSibling,nodeName,nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,"
                + "parentElement,parentNode,previousSibling,PROCESSING_INSTRUCTION_NODE,querySelector(),"
                + "querySelectorAll(),removeChild(),replaceChild(),TEXT_NODE,textContent",
            FF = "appendChild(),ATTRIBUTE_NODE,baseURI,CDATA_SECTION_NODE,childElementCount,childNodes,"
                + "children,cloneNode(),COMMENT_NODE,compareDocumentPosition(),contains(),DOCUMENT_FRAGMENT_NODE,"
                + "DOCUMENT_NODE,DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,"
                + "DOCUMENT_POSITION_DISCONNECTED,DOCUMENT_POSITION_FOLLOWING,"
                + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,"
                + "DOCUMENT_POSITION_PRECEDING,DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,"
                + "firstChild,firstElementChild,getElementById(),hasChildNodes(),insertBefore(),isSameNode(),lastChild,"
                + "lastElementChild,nextSibling,nodeName,nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,"
                + "parentElement,parentNode,previousSibling,PROCESSING_INSTRUCTION_NODE,querySelector(),"
                + "querySelectorAll(),removeChild(),replaceChild(),TEXT_NODE,textContent",
            IE = "addEventListener(),appendChild(),ATTRIBUTE_NODE,attributes,CDATA_SECTION_NODE,childNodes,cloneNode(),"
                + "COMMENT_NODE,compareDocumentPosition(),dispatchEvent(),DOCUMENT_FRAGMENT_NODE,DOCUMENT_NODE,"
                + "DOCUMENT_POSITION_CONTAINED_BY,DOCUMENT_POSITION_CONTAINS,DOCUMENT_POSITION_DISCONNECTED,"
                + "DOCUMENT_POSITION_FOLLOWING,DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC,DOCUMENT_POSITION_PRECEDING,"
                + "DOCUMENT_TYPE_NODE,ELEMENT_NODE,ENTITY_NODE,ENTITY_REFERENCE_NODE,firstChild,hasAttributes(),"
                + "hasChildNodes(),insertBefore(),isSameNode(),lastChild,localName,namespaceURI,nextSibling,"
                + "nodeName,nodeType,nodeValue,normalize(),NOTATION_NODE,ownerDocument,parentNode,prefix,"
                + "previousSibling,PROCESSING_INSTRUCTION_NODE,querySelector(),querySelectorAll(),removeChild(),"
                + "removeEventListener(),replaceChild(),TEXT_NODE,textContent")
    public void documentFragment() throws Exception {
        testString("", "document.createDocumentFragment(), window.performance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "addEventListener(),audioWorklet,baseLatency,close(),createAnalyser(),createBiquadFilter(),"
                + "createBuffer(),createBufferSource(),createChannelMerger(),createChannelSplitter(),"
                + "createConstantSource(),createConvolver(),createDelay(),createDynamicsCompressor(),createGain(),"
                + "createIIRFilter(),createMediaElementSource(),createMediaStreamDestination(),"
                + "createMediaStreamSource(),createOscillator(),createPanner(),createPeriodicWave(),"
                + "createScriptProcessor(),createStereoPanner(),createWaveShaper(),currentTime,"
                + "decodeAudioData(),destination,dispatchEvent(),getOutputTimestamp(),listener,"
                + "onstatechange,removeEventListener(),resume(),sampleRate,state,suspend()",
            EDGE = "addEventListener(),audioWorklet,baseLatency,close(),createAnalyser(),createBiquadFilter(),"
                + "createBuffer(),createBufferSource(),createChannelMerger(),createChannelSplitter(),"
                + "createConstantSource(),createConvolver(),createDelay(),createDynamicsCompressor(),createGain(),"
                + "createIIRFilter(),createMediaElementSource(),createMediaStreamDestination(),"
                + "createMediaStreamSource(),createOscillator(),createPanner(),createPeriodicWave(),"
                + "createScriptProcessor(),createStereoPanner(),createWaveShaper(),currentTime,"
                + "decodeAudioData(),destination,dispatchEvent(),getOutputTimestamp(),listener,"
                + "onstatechange,removeEventListener(),resume(),sampleRate,state,suspend()",
            FF = "addEventListener(),audioWorklet,baseLatency,close(),createAnalyser(),createBiquadFilter(),"
                + "createBuffer(),createBufferSource(),createChannelMerger(),createChannelSplitter(),"
                + "createConstantSource(),createConvolver(),createDelay(),createDynamicsCompressor(),createGain(),"
                + "createIIRFilter(),createMediaElementSource(),createMediaStreamDestination(),"
                + "createMediaStreamSource(),createMediaStreamTrackSource(),createOscillator(),createPanner(),"
                + "createPeriodicWave(),createScriptProcessor(),createStereoPanner(),createWaveShaper(),"
                + "currentTime,decodeAudioData(),destination,dispatchEvent(),getOutputTimestamp(),listener,"
                + "onstatechange,outputLatency,removeEventListener(),resume(),sampleRate,state,suspend()",
            FF78 = "addEventListener(),audioWorklet,baseLatency,close(),createAnalyser(),createBiquadFilter(),"
                + "createBuffer(),createBufferSource(),createChannelMerger(),createChannelSplitter(),"
                + "createConstantSource(),createConvolver(),createDelay(),createDynamicsCompressor(),createGain(),"
                + "createIIRFilter(),createMediaElementSource(),createMediaStreamDestination(),"
                + "createMediaStreamSource(),createMediaStreamTrackSource(),createOscillator(),createPanner(),"
                + "createPeriodicWave(),createScriptProcessor(),createStereoPanner(),createWaveShaper(),"
                + "currentTime,decodeAudioData(),destination,dispatchEvent(),getOutputTimestamp(),listener,"
                + "onstatechange,outputLatency,removeEventListener(),resume(),sampleRate,state,suspend()",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "addEventListener(),createBuffer(),createBufferSource(),createGain(),decodeAudioData(),"
                + "dispatchEvent(),removeEventListener()",
            EDGE = "addEventListener(),createBuffer(),createBufferSource(),createGain(),decodeAudioData(),"
                + "dispatchEvent(),removeEventListener()",
            FF78 = "addEventListener(),createBuffer(),createBufferSource(),createGain(),decodeAudioData(),"
                + "dispatchEvent(),removeEventListener()",
            FF = "addEventListener(),createBuffer(),createBufferSource(),createGain(),decodeAudioData(),"
                + "dispatchEvent(),removeEventListener()")
    public void audioContext() throws Exception {
        testString("", "new AudioContext()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "automationRate,cancelAndHoldAtTime(),cancelScheduledValues(),"
                + "defaultValue,exponentialRampToValueAtTime(),linearRampToValueAtTime(),maxValue,minValue,"
                + "setTargetAtTime(),setValueAtTime(),setValueCurveAtTime(),value",
            EDGE = "automationRate,cancelAndHoldAtTime(),cancelScheduledValues(),"
                + "defaultValue,exponentialRampToValueAtTime(),linearRampToValueAtTime(),maxValue,minValue,"
                + "setTargetAtTime(),setValueAtTime(),setValueCurveAtTime(),value",
            FF = "cancelScheduledValues(),defaultValue,exponentialRampToValueAtTime(),"
                + "linearRampToValueAtTime(),maxValue,minValue,setTargetAtTime(),setValueAtTime(),"
                + "setValueCurveAtTime(),value",
            FF78 = "cancelScheduledValues(),defaultValue,exponentialRampToValueAtTime(),"
                + "linearRampToValueAtTime(),maxValue,minValue,setTargetAtTime(),setValueAtTime(),"
                + "setValueCurveAtTime(),value",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "defaultValue,maxValue,minValue,value",
            EDGE = "defaultValue,maxValue,minValue,value",
            FF78 = "defaultValue,maxValue,minValue,value",
            FF = "defaultValue,maxValue,minValue,value")
    public void audioParam() throws Exception {
        testString("var audioCtx = new AudioContext(); var gainNode = new GainNode(audioCtx);", "gainNode.gain");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "addEventListener(),channelCount,channelCountMode,channelInterpretation,connect(),"
                + "context,disconnect(),dispatchEvent(),gain,numberOfInputs,numberOfOutputs,removeEventListener()",
            EDGE = "addEventListener(),channelCount,channelCountMode,channelInterpretation,connect(),"
                + "context,disconnect(),dispatchEvent(),gain,numberOfInputs,numberOfOutputs,removeEventListener()",
            FF = "addEventListener(),channelCount,channelCountMode,channelInterpretation,connect(),"
                + "context,disconnect(),dispatchEvent(),gain,numberOfInputs,numberOfOutputs,removeEventListener()",
            FF78 = "addEventListener(),channelCount,channelCountMode,channelInterpretation,connect(),"
                + "context,disconnect(),dispatchEvent(),gain,numberOfInputs,numberOfOutputs,removeEventListener()",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "addEventListener(),connect(),dispatchEvent(),gain,removeEventListener()",
            EDGE = "addEventListener(),connect(),dispatchEvent(),gain,removeEventListener()",
            FF78 = "addEventListener(),connect(),dispatchEvent(),gain,removeEventListener()",
            FF = "addEventListener(),connect(),dispatchEvent(),gain,removeEventListener()")
    public void gainNode() throws Exception {
        testString("var audioCtx = new AudioContext();", "new GainNode(audioCtx)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "isTrusted,NONE,path,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "isTrusted,NONE,path,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),isTrusted,META_MASK,NONE,originalTarget,"
                + "preventDefault(),returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),isTrusted,META_MASK,NONE,originalTarget,"
                + "preventDefault(),returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "NONE,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "NONE,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "initEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "initEvent(),META_MASK,NONE,"
                + "preventDefault(),returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type")
    public void beforeUnloadEvent() throws Exception {
        testString("", "document.createEvent('BeforeUnloadEvent')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,code,"
                + "composed,composedPath(),currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "isTrusted,NONE,path,preventDefault(),reason,returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,wasClean",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,code,"
                + "composed,composedPath(),currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "isTrusted,NONE,path,preventDefault(),reason,returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,wasClean",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,code,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),isTrusted,META_MASK,NONE,originalTarget,preventDefault(),"
                + "reason,returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,wasClean",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,code,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),isTrusted,META_MASK,NONE,originalTarget,preventDefault(),"
                + "reason,returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,wasClean",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,code,"
                + "composed,currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "NONE,preventDefault(),reason,returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,wasClean",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,code,"
                + "composed,currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "NONE,preventDefault(),reason,returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type,wasClean",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,code,"
                + "composed,CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "initEvent(),META_MASK,NONE,preventDefault(),"
                + "reason,returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,wasClean",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,code,"
                + "composed,CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "initEvent(),META_MASK,NONE,preventDefault(),"
                + "reason,returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type,wasClean")
    public void closeEvent() throws Exception {
        testString("", "new CloseEvent('type-close')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,data,defaultPrevented,eventPhase,initEvent(),"
                + "isTrusted,NONE,path,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timecode,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,data,defaultPrevented,eventPhase,initEvent(),"
                + "isTrusted,NONE,path,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timecode,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,data,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),isTrusted,META_MASK,NONE,originalTarget,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,data,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),isTrusted,META_MASK,NONE,originalTarget,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "currentTarget,data,defaultPrevented,eventPhase,initEvent(),"
                + "NONE,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "currentTarget,data,defaultPrevented,eventPhase,initEvent(),"
                + "NONE,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "CONTROL_MASK,currentTarget,data,defaultPrevented,eventPhase,"
                + "initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,"
                + "CONTROL_MASK,currentTarget,data,defaultPrevented,eventPhase,"
                + "initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type")
    public void blobEvent() throws Exception {
        testString("var debug = {hello: 'world'};"
                    + "var blob = new Blob([JSON.stringify(debug, null, 2)], {type : 'application/json'});",
                    "new BlobEvent('blob', { 'data': blob })");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "acceleration,accelerationIncludingGravity,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,"
                + "cancelBubble,CAPTURING_PHASE,composed,composedPath(),currentTarget,defaultPrevented,"
                + "eventPhase,initEvent(),interval,isTrusted,NONE,path,preventDefault(),returnValue,"
                + "rotationRate,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "acceleration,accelerationIncludingGravity,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,"
                + "cancelBubble,CAPTURING_PHASE,composed,composedPath(),currentTarget,defaultPrevented,"
                + "eventPhase,initEvent(),interval,isTrusted,NONE,path,preventDefault(),returnValue,"
                + "rotationRate,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF = "acceleration,accelerationIncludingGravity,ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,"
                + "cancelable,cancelBubble,CAPTURING_PHASE,composed,composedPath(),CONTROL_MASK,currentTarget,"
                + "defaultPrevented,eventPhase,explicitOriginalTarget,initDeviceMotionEvent(),initEvent(),"
                + "interval,isTrusted,META_MASK,NONE,originalTarget,preventDefault(),returnValue,rotationRate,"
                + "SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF78 = "acceleration,accelerationIncludingGravity,ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,"
                + "cancelable,cancelBubble,CAPTURING_PHASE,composed,composedPath(),CONTROL_MASK,currentTarget,"
                + "defaultPrevented,eventPhase,explicitOriginalTarget,initDeviceMotionEvent(),initEvent(),"
                + "interval,isTrusted,META_MASK,NONE,originalTarget,preventDefault(),returnValue,rotationRate,"
                + "SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type")
    public void deviceMotionEvent() throws Exception {
        testString("", "new DeviceMotionEvent('motion')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,colno,"
                + "composed,composedPath(),currentTarget,defaultPrevented,error,eventPhase,filename,initEvent(),"
                + "isTrusted,lineno,message,NONE,path,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,colno,"
                + "composed,composedPath(),currentTarget,defaultPrevented,error,eventPhase,filename,initEvent(),"
                + "isTrusted,lineno,message,NONE,path,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,colno,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,error,eventPhase,"
                + "explicitOriginalTarget,filename,initEvent(),isTrusted,lineno,message,META_MASK,NONE,"
                + "originalTarget,preventDefault(),returnValue,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,colno,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,error,eventPhase,"
                + "explicitOriginalTarget,filename,initEvent(),isTrusted,lineno,message,META_MASK,NONE,"
                + "originalTarget,preventDefault(),returnValue,SHIFT_MASK,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type")
    public void errorEvent() throws Exception {
        testString("", "new ErrorEvent('error')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,defaultPrevented,eventPhase,gamepad,initEvent(),"
                + "isTrusted,NONE,path,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),currentTarget,defaultPrevented,eventPhase,gamepad,initEvent(),"
                + "isTrusted,NONE,path,preventDefault(),returnValue,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,gamepad,initEvent(),isTrusted,META_MASK,NONE,originalTarget,"
                + "preventDefault(),returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,gamepad,initEvent(),isTrusted,META_MASK,NONE,originalTarget,"
                + "preventDefault(),returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type")
    public void gamepadEvent() throws Exception {
        testString("", "new GamepadEvent('gamepad')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "ADDITION,AT_TARGET,attrChange,attrName,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,composedPath(),currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "initMutationEvent(),isTrusted,MODIFICATION,newValue,NONE,path,preventDefault(),prevValue,"
                + "relatedNode,REMOVAL,returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            EDGE = "ADDITION,AT_TARGET,attrChange,attrName,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,composedPath(),currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "initMutationEvent(),isTrusted,MODIFICATION,newValue,NONE,path,preventDefault(),prevValue,"
                + "relatedNode,REMOVAL,returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            FF = "ADDITION,ALT_MASK,AT_TARGET,attrChange,attrName,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),initMutationEvent(),isTrusted,META_MASK,MODIFICATION,newValue,"
                + "NONE,originalTarget,preventDefault(),prevValue,relatedNode,REMOVAL,returnValue,SHIFT_MASK,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF78 = "ADDITION,ALT_MASK,AT_TARGET,attrChange,attrName,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),initMutationEvent(),isTrusted,META_MASK,MODIFICATION,newValue,"
                + "NONE,originalTarget,preventDefault(),prevValue,relatedNode,REMOVAL,returnValue,SHIFT_MASK,"
                + "srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            IE = "ADDITION,AT_TARGET,attrChange,attrName,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,currentTarget,defaultPrevented,eventPhase,initEvent(),initMutationEvent(),isTrusted,"
                + "MODIFICATION,newValue,preventDefault(),prevValue,relatedNode,REMOVAL,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type")
    @HtmlUnitNYI(CHROME = "ADDITION,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "MODIFICATION,NONE,preventDefault(),REMOVAL,"
                + "returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "ADDITION,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,currentTarget,defaultPrevented,eventPhase,initEvent(),"
                + "MODIFICATION,NONE,preventDefault(),REMOVAL,"
                + "returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF = "ADDITION,ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,CONTROL_MASK,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),META_MASK,MODIFICATION,NONE,"
                + "preventDefault(),REMOVAL,returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            FF78 = "ADDITION,ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,CONTROL_MASK,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),META_MASK,MODIFICATION,NONE,"
                + "preventDefault(),REMOVAL,returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            IE = "ADDITION,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),MODIFICATION,preventDefault(),REMOVAL,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type")
    public void mutationEvent() throws Exception {
        testString("", "document.createEvent('MutationEvent')");
    }

    /**
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
    @Alerts(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,composedPath(),"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),isTrusted,NONE,path,persisted,"
                + "preventDefault(),returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,"
                + "timeStamp,"
                + "type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,composed,composedPath(),"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),isTrusted,NONE,path,persisted,"
                + "preventDefault(),returnValue,srcElement,stopImmediatePropagation(),stopPropagation(),target,"
                + "timeStamp,"
                + "type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),isTrusted,META_MASK,NONE,originalTarget,persisted,"
                + "preventDefault(),returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,"
                + "composed,composedPath(),CONTROL_MASK,currentTarget,defaultPrevented,eventPhase,"
                + "explicitOriginalTarget,initEvent(),isTrusted,META_MASK,NONE,originalTarget,persisted,"
                + "preventDefault(),returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,type",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            EDGE = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),NONE,preventDefault(),returnValue,srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,type",
            FF = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type",
            FF78 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,"
                + "CAPTURING_PHASE,composed,CONTROL_MASK,"
                + "currentTarget,defaultPrevented,eventPhase,initEvent(),META_MASK,NONE,preventDefault(),"
                + "returnValue,SHIFT_MASK,srcElement,stopImmediatePropagation(),stopPropagation(),"
                + "target,timeStamp,type")
    public void pageTransitionEvent() throws Exception {
        testString("", "new PageTransitionEvent('transition')");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.media.SourceBufferList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "addEventListener(),dispatchEvent(),length,onaddsourcebuffer,"
                + "onremovesourcebuffer,removeEventListener()",
            EDGE = "addEventListener(),dispatchEvent(),length,onaddsourcebuffer,"
                + "onremovesourcebuffer,removeEventListener()",
            FF = "addEventListener(),dispatchEvent(),length,onaddsourcebuffer,"
                + "onremovesourcebuffer,removeEventListener()",
            FF78 = "addEventListener(),dispatchEvent(),length,onaddsourcebuffer,"
                + "onremovesourcebuffer,removeEventListener()",
            IE = "addEventListener(),dispatchEvent(),item(),length,removeEventListener()")
    @HtmlUnitNYI(CHROME = "-",
            EDGE = "-",
            FF = "-",
            FF78 = "-",
            IE = "-")
    public void sourceBufferList() throws Exception {
        testString("var mediaSource = new MediaSource;", "mediaSource.sourceBuffers");
    }
}
