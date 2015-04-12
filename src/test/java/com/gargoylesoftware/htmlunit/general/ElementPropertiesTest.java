/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.general;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF31;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.SortOrder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests all properties of an object.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ElementPropertiesTest extends WebDriverTestCase {

    private static DefaultCategoryDataset DATASET_;
    private static StringBuilder HTML_ = new StringBuilder();
    private static BrowserVersion BROWSER_VERSION_;
    private static int IMPLEMENTED_COUNT_;
    private static int TOTAL_COUNT_;

    private void test(final String tagName) throws Exception {
        testString("document.createElement('" + tagName + "'), unknown");
    }

    private void testString(final String string) throws Exception {
        final String html = "<html><head><script>\n"
                + "  var ieMethods = ['abort', 'add', 'addBehavior', 'AddChannel', 'AddDesktopComponent', "
                + "'addElement', 'AddFavorite', 'addFilter', 'addImport', 'AddInPrivateSubscription', 'addPageRule', "
                + "'addReadRequest', 'addRule', 'AddSearchProvider', 'AddService', 'AddToFavoritesBar', 'alert', "
                + "'appendChild', 'appendData', 'applyElement', 'assign', 'attachEvent', 'AutoCompleteSaveForm', "
                + "'AutoScan', 'back', 'blur', 'bubbleEvent', 'ChooseColorDlg', 'clear', 'clearAttributes', "
                + "'clearData', 'clearInterval', 'clearRequest', 'clearTimeout', 'click', 'cloneNode', 'close', "
                + "'collapse', 'compareEndPoints', 'componentFromPoint', 'confirm', 'contains', "
                + "'ContentDiscoveryReset', 'createAttribute', 'createCaption', 'createComment', "
                + "'createControlRange', 'createDocumentFragment', 'createElement', 'createEventObject', "
                + "'createPopup', 'createRange', 'createRangeCollection', 'createStyleSheet', 'createTextNode', "
                + "'createTextRange', 'createTFoot', 'createTHead', 'deleteCaption', 'deleteCell', 'deleteData', "
                + "'deleteRow', 'deleteTFoot', 'deleteTHead', 'detachEvent', 'doImport', 'doReadRequest', 'doScroll', "
                + "'dragDrop', 'duplicate', 'elementFromPoint', 'empty', 'execCommand', 'execScript', 'expand', "
                + "'findText', 'fireEvent', 'firstPage', 'focus', 'forward', 'getAdjacentText', "
                + "'getAllResponseHeaders', 'getAttribute', 'getAttributeNode', 'getBookmark', "
                + "'getBoundingClientRect', 'getCharset', 'getClientRects', 'getData', 'getElementById', "
                + "'getElementsByName', 'getElementsByTagName', 'getExpression', 'getItem', 'getNamedItem', "
                + "'getResponseHeader', 'go', 'hasAttribute', 'hasChildNodes', 'hasFeature', 'hasFocus', 'hide', "
                + "'ImportExportFavorites', 'inRange', 'insertAdjacentElement', 'insertAdjacentHTML', "
                + "'insertAdjacentText', 'insertBefore', 'insertCell', 'insertData', 'insertRow', 'isEqual', "
                + "'IsSearchProviderInstalled', 'IsServiceInstalled', 'IsSubscribed', 'item', 'Item', 'javaEnabled', "
                + "'key', 'lastPage', 'mergeAttributes', 'move', 'moveBy', 'moveEnd', 'moveRow', 'moveStart', "
                + "'moveTo', 'moveToBookmark', 'moveToElementText', 'moveToPoint', 'namedItem', 'namedRecordset', "
                + "'navigate', 'NavigateAndFind', 'nextPage', 'normalize', 'open', 'parentElement', 'pasteHTML', "
                + "'postMessage', 'previousPage', 'print', 'prompt', 'queryCommandEnabled', 'queryCommandIndeterm', "
                + "'queryCommandState', 'queryCommandSupported', 'queryCommandValue', 'querySelector', "
                + "'querySelectorAll', 'raiseEvent', 'recalc', 'refresh', 'releaseCapture', 'reload', 'remove', "
                + "'removeAttribute', 'removeAttributeNode', 'removeBehavior', 'removeChild', 'removeExpression', "
                + "'removeImport', 'removeItem', 'removeNamedItem', 'removeNode', 'removeRule', 'replace', "
                + "'replaceAdjacentText', 'replaceChild', 'replaceData', 'replaceNode', 'reset', 'resizeBy', "
                + "'resizeTo', 'scroll', 'scrollBy', 'scrollIntoView', 'scrollTo', 'select', 'send', 'setActive', "
                + "'setAttribute', 'setAttributeNode', 'setCapture', 'setContextMenu', 'setData', 'setEndPoint', "
                + "'setExpression', 'setInterval', 'setItem', 'setNamedItem', 'setRequestHeader', 'setTimeout', "
                + "'show', 'ShowBrowserUI', 'showHelp', 'showModalDialog', 'showModelessDialog', 'splitText', "
                + "'start', 'stop', 'submit', 'substringData', 'swapNode', 'tags', 'taintEnabled', 'toStaticHTML', "
                + "'updateSettings', 'urns', 'write', 'writeln'];\n"

                + "  function test(event) {\n"
                + "    var xmlDocument = createXmlDocument();\n"
                + "    var element = xmlDocument.createElement('wakwak');\n"
                + "    var unknown = document.createElement('harhar');\n"
                + "    var div = document.createElement('div');\n"
                + "    process(" + string + ");\n"
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
                + "    if (document.all) {\n"
                + "      for (var index in ieMethods) {\n"
                + "        var method = ieMethods[index];\n"
                + "        try {\n"
                + "          if (object[method] !== undefined && (parent == null || parent[method] === undefined)) {\n"
                + "            all.push(method + '()');\n"
                + "          }\n"
                + "        } catch(e) {\n"
                + "           all.push(method + '()');\n"
                + "        }\n"
                + "      }\n"
                + "    }\n"
                + "    all.sort(sortFunction);\n"
                + "    alert(all);\n"
                + "  }\n"
                + "  function sortFunction(s1, s2) {\n"
                + "    return s1.toLowerCase() > s2.toLowerCase() ? 1 : -1;\n"
                + "  }\n"
                + "  function createXmlDocument() {\n"
                + "    if (document.implementation && document.implementation.createDocument)\n"
                + "      return document.implementation.createDocument('', '', null);\n"
                + "    else if (window.ActiveXObject)\n"
                + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
                + "  }\n"
                + "</script></head><body onload='test(event)'>\n"
                + "</body></html>";

        if (BROWSER_VERSION_ == null) {
            BROWSER_VERSION_ = getBrowserVersion();
        }
        final String expected = getExpectedAlerts().length == 0 ? "" : getExpectedAlerts()[0];
        String actual;

        ComparisonFailure failure = null;
        try {
            loadPageWithAlerts2(html);
            actual = expected;
        }
        catch (final ComparisonFailure c) {
            failure = c;
            actual = c.getActual();
            actual = actual.substring(1, actual.length() - 1);
        }

        final List<String> realProperties = stringAsArray(expected);
        final List<String> simulatedProperties = stringAsArray(actual);

        final List<String> erroredProperties = new ArrayList<>(simulatedProperties);
        erroredProperties.removeAll(realProperties);

        final List<String> implementedProperties = new ArrayList<>(simulatedProperties);
        implementedProperties.retainAll(realProperties);

        IMPLEMENTED_COUNT_ += implementedProperties.size();
        TOTAL_COUNT_ += realProperties.size();

        String methodName = null;
        for (final StackTraceElement e : new Exception().getStackTrace()) {
            if (e.getClassName().equals(getClass().getName())) {
                methodName = e.getMethodName();
            }
            else {
                break;
            }
        }

        htmlDetails(methodName, HTML_, realProperties, implementedProperties, erroredProperties);

        DATASET_.addValue(implementedProperties.size(), "Implemented", methodName);
        DATASET_.addValue(realProperties.size(),
            getBrowserVersion().getNickname().replace("FF", "Firefox ").replace("IE", "Internet Explorer "),
            methodName);
        DATASET_.addValue(erroredProperties.size(), "Should not be implemented", methodName);

        if (failure != null) {
            throw failure;
        }
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
        DATASET_ = new DefaultCategoryDataset();
        HTML_.setLength(0);
        BROWSER_VERSION_ = null;
        IMPLEMENTED_COUNT_ = 0;
        TOTAL_COUNT_ = 0;
    }

    /**
     * Saves HTML and PNG files.
     *
     * @throws IOException if an error occurs
     */
    @AfterClass
    public static void saveAll() throws IOException {
        saveChart();

        FileUtils.writeStringToFile(new File(getTargetDirectory()
                + "/properties-" + BROWSER_VERSION_.getNickname() + ".html"),
                htmlHeader()
                    .append(overview())
                    .append(htmlDetailsHeader())
                    .append(HTML_)
                    .append(htmlDetailsFooter())
                    .append(htmlFooter()).toString());
    }

    private static void saveChart() throws IOException {
        final JFreeChart chart = ChartFactory.createBarChart(
            "HtmlUnit implemented properties and methods for " + BROWSER_VERSION_.getNickname(), "Objects",
            "Count", DATASET_, PlotOrientation.HORIZONTAL, true, true, false);
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

    private static StringBuilder overview() {
        final StringBuilder html = new StringBuilder();
        html.append("<table class='bottomBorder'>");
        html.append("<tr>\n");

        html.append("<th>Total Implemented:</th>\n");
        html.append("<td>" + IMPLEMENTED_COUNT_)
            .append(" / " + TOTAL_COUNT_).append("</td>\n");

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
        if (realProperties.isEmpty()) {
            html.append("&nbsp;");
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
            IE11 = "appendData(),data,deleteData(),insertData(),length,replaceData(),replaceWholeText(),splitText(),"
                + "substringData(),"
                + "wholeText",
            IE8 = "appendData(),data,deleteData(),insertData(),length,replaceData(),splitText(),substringData()")
    @NotYetImplemented(IE11)
    public void text() throws Exception {
        testString("document.createTextNode('some text'), unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "name,ownerElement,specified,value",
            IE = "expando,name,ownerElement,specified,value")
    @NotYetImplemented(IE8)
    public void attr() throws Exception {
        testString("document.createAttribute('some_attrib'), unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "appendData(),data,deleteData(),insertData(),length,replaceData(),substringData()",
            IE11 = "appendData(),data,deleteData(),insertData(),length,replaceData(),substringData(),text",
            IE8 = "appendData(),atomic,data,deleteData(),insertData(),length,replaceData(),substringData(),text")
    @NotYetImplemented(IE8)
    public void comment() throws Exception {
        testString("document.createComment('come_comment'), unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE11 = "namedRecordset(),recordset",
            IE8 = "namedRecordset()")
    @NotYetImplemented(IE)
    public void unknown() throws Exception {
        testString("unknown, div");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "accessKey,click(),contentEditable,dir,draggable,hidden,innerText,isContentEditable,lang,onabort,"
                + "onautocomplete,onautocompleteerror,onblur,oncancel,oncanplay,oncanplaythrough,onchange,onclick,"
                + "onclose,oncontextmenu,oncuechange,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,"
                + "ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,oninput,oninvalid,"
                + "onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onpause,"
                + "onplay,onplaying,onprogress,onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onshow,onstalled,onsubmit,onsuspend,ontimeupdate,ontoggle,onvolumechange,onwaiting,outerText,"
                + "spellcheck,tabIndex,title,translate,"
                + "webkitdropzone",
            FF31 = "accessKey,accessKeyLabel,blur(),className,click(),contentEditable,contextMenu,dataset,dir,"
                + "draggable,focus(),hidden,isContentEditable,itemId,itemProp,itemRef,itemScope,itemType,itemValue,"
                + "lang,offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,onabort,onblur,oncanplay,"
                + "oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncut,ondblclick,ondrag,ondragend,"
                + "ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,"
                + "onfocus,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,"
                + "onloadstart,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmozfullscreenchange,onmozfullscreenerror,onmozpointerlockchange,onmozpointerlockerror,onpaste,"
                + "onpause,onplay,onplaying,onprogress,onratechange,onreset,onscroll,onseeked,onseeking,onselect,"
                + "onshow,onstalled,onsubmit,onsuspend,ontimeupdate,onvolumechange,onwaiting,properties,spellcheck,"
                + "style,tabIndex,"
                + "title",
            IE11 = "accessKey,applyElement(),blur(),canHaveChildren,canHaveHTML,children,classList,className,"
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
                + "uniqueNumber",
            IE8 = "accessKey,addBehavior(),all,appendChild(),applyElement(),aria-activedescendant,aria-busy,"
                + "aria-checked,aria-controls,aria-describedby,aria-disabled,aria-expanded,aria-flowto,"
                + "aria-haspopup,aria-hidden,aria-invalid,aria-labelledby,aria-level,aria-live,aria-multiselectable,"
                + "aria-owns,aria-posinset,aria-pressed,aria-readonly,aria-relevant,aria-required,aria-secret,"
                + "aria-selected,aria-setsize,aria-valuemax,aria-valuemin,aria-valuenow,attachEvent(),behaviorUrns,"
                + "blur(),canHaveChildren,canHaveHTML,children,className,clearAttributes(),click(),clientHeight,"
                + "clientLeft,clientTop,clientWidth,cloneNode(),componentFromPoint(),contains(),contentEditable,"
                + "createControlRange(),currentStyle,detachEvent(),dir,disabled,document,doScroll(),dragDrop(),"
                + "filters,fireEvent(),focus(),getAdjacentText(),getAttribute(),getAttributeNode(),"
                + "getBoundingClientRect(),getClientRects(),getElementsByTagName(),getExpression(),hideFocus,id,"
                + "innerHTML,innerText,insertAdjacentElement(),insertAdjacentHTML(),insertAdjacentText(),"
                + "insertBefore(),isContentEditable,isDisabled,isMultiLine,isTextEdit,lang,language,"
                + "mergeAttributes(),namedRecordset(),offsetHeight,offsetLeft,offsetParent,offsetTop,offsetWidth,"
                + "onactivate,onafterupdate,onbeforeactivate,onbeforecopy,onbeforecut,onbeforedeactivate,"
                + "onbeforeeditfocus,onbeforepaste,onbeforeupdate,onblur,oncellchange,onclick,oncontextmenu,"
                + "oncontrolselect,oncopy,oncut,ondataavailable,ondatasetchanged,ondatasetcomplete,ondblclick,"
                + "ondeactivate,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "onerrorupdate,onfilterchange,onfocus,onfocusin,onfocusout,onhelp,onkeydown,onkeypress,onkeyup,"
                + "onlayoutcomplete,onlosecapture,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,"
                + "onmouseover,onmouseup,onmousewheel,onmove,onmoveend,onmovestart,onpage,onpaste,onpropertychange,"
                + "onreadystatechange,onresize,onresizeend,onresizestart,onrowenter,onrowexit,onrowsdelete,"
                + "onrowsinserted,onscroll,onselectstart,outerHTML,outerText,parentElement,parentElement(),"
                + "parentTextEdit,readyState,recordNumber,releaseCapture(),removeAttribute(),removeAttributeNode(),"
                + "removeBehavior(),removeChild(),removeExpression(),removeNode(),replaceAdjacentText(),"
                + "replaceChild(),replaceNode(),role,runtimeStyle,scopeName,scrollHeight,scrollIntoView(),"
                + "scrollLeft,scrollTop,scrollWidth,setActive(),setAttribute(),setAttributeNode(),setCapture(),"
                + "setExpression(),sourceIndex,style,swapNode(),tabIndex,tagUrn,"
                + "title")
    @NotYetImplemented
    public void htmlElement() throws Exception {
        testString("unknown, element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "blockDirection,clipBottom,clipLeft,clipRight,clipTop,hasLayout")
    @NotYetImplemented(IE)
    public void currentStyle() throws Exception {
        testString("document.body.currentStyle, document.body.style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "AT_TARGET,BLUR,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,CHANGE,CLICK,"
                + "currentTarget,DBLCLICK,defaultPrevented,DRAGDROP,eventPhase,FOCUS,initEvent(),KEYDOWN,KEYPRESS,"
                + "KEYUP,MOUSEDOWN,MOUSEDRAG,MOUSEMOVE,MOUSEOUT,MOUSEOVER,MOUSEUP,NONE,path,preventDefault(),"
                + "returnValue,SELECT,srcElement,stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                + "type",
            FF31 = "ALT_MASK,AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,CAPTURING_PHASE,CONTROL_MASK,currentTarget,"
                + "defaultPrevented,eventPhase,explicitOriginalTarget,getPreventDefault(),initEvent(),isTrusted,"
                + "META_MASK,NONE,originalTarget,preventDefault(),SHIFT_MASK,stopImmediatePropagation(),"
                + "stopPropagation(),target,timeStamp,"
                + "type",
            IE11 = "AT_TARGET,bubbles,BUBBLING_PHASE,cancelable,cancelBubble,CAPTURING_PHASE,currentTarget,"
                + "defaultPrevented,eventPhase,initEvent(),isTrusted,preventDefault(),srcElement,"
                + "stopImmediatePropagation(),stopPropagation(),target,timeStamp,"
                + "type",
            IE8 = "altKey,altLeft,behaviorCookie,behaviorPart,bookmarks,boundElements,button,cancelBubble,clientX,"
                + "clientY,contentOverflow,ctrlKey,ctrlLeft,data,dataFld,dataTransfer,fromElement,getAttribute(),"
                + "keyCode,nextPage,nextPage(),offsetX,offsetY,origin,propertyName,qualifier,reason,recordset,"
                + "removeAttribute(),repeat,returnValue,screenX,screenY,setAttribute(),shiftKey,shiftLeft,source,"
                + "srcElement,srcFilter,srcUrn,toElement,type,url,wheelDelta,x,"
                + "y")
    @NotYetImplemented
    public void event() throws Exception {
        testString("event ? event : window.event, null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "addEventListener(),alert(),applicationCache,atob(),blur(),btoa(),cancelAnimationFrame(),"
                + "captureEvents(),chrome,clearInterval(),clearTimeout(),clientInformation,close(),closed,confirm(),"
                + "console,createXmlDocument(),crypto,CSS,defaultStatus,defaultstatus,devicePixelRatio,"
                + "dispatchEvent(),document,external,find(),focus(),frameElement,frames,getComputedStyle(),"
                + "getMatchedCSSRules(),getSelection(),history,ieMethods,indexedDB,innerHeight,innerWidth,length,"
                + "localStorage,location,locationbar,matchMedia(),menubar,moveBy(),moveTo(),name,navigator,onabort,"
                + "onautocomplete,onautocompleteerror,onbeforeunload,onblur,oncancel,oncanplay,oncanplaythrough,"
                + "onchange,onclick,onclose,oncontextmenu,oncuechange,ondblclick,ondevicemotion,ondeviceorientation,"
                + "ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,"
                + "onemptied,onended,onerror,onfocus,onhashchange,oninput,oninvalid,onkeydown,onkeypress,onkeyup,"
                + "onlanguagechange,onload(),onloadeddata,onloadedmetadata,onloadstart,onmessage,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onoffline,"
                + "ononline,onpagehide,onpageshow,onpause,onplay,onplaying,onpopstate,onprogress,onratechange,"
                + "onreset,onresize,onscroll,onsearch,onseeked,onseeking,onselect,onshow,onstalled,onstorage,"
                + "onsubmit,onsuspend,ontimeupdate,ontoggle,ontransitionend,onunload,onvolumechange,onwaiting,"
                + "onwebkitanimationend,onwebkitanimationiteration,onwebkitanimationstart,onwebkittransitionend,"
                + "onwheel,open(),openDatabase(),opener,outerHeight,outerWidth,pageXOffset,pageYOffset,parent,"
                + "performance,PERSISTENT,personalbar,postMessage(),print(),process(),prompt(),releaseEvents(),"
                + "removeEventListener(),requestAnimationFrame(),resizeBy(),resizeTo(),screen,screenLeft,screenTop,"
                + "screenX,screenY,scroll(),scrollbars,scrollBy(),scrollTo(),scrollX,scrollY,self,sessionStorage,"
                + "setInterval(),setTimeout(),sortFunction(),speechSynthesis,status,statusbar,stop(),styleMedia,"
                + "TEMPORARY,test(),toolbar,top,webkitCancelAnimationFrame(),webkitCancelRequestAnimationFrame(),"
                + "webkitIndexedDB,webkitRequestAnimationFrame(),webkitRequestFileSystem(),"
                + "webkitResolveLocalFileSystemURL(),webkitStorageInfo,"
                + "window",
            FF31 = "addEventListener(),alert(),applicationCache,atob(),back(),blur(),btoa(),cancelAnimationFrame(),"
                + "captureEvents(),clearInterval(),clearTimeout(),close(),closed,confirm(),console,content,"
                + "createXmlDocument(),crypto,devicePixelRatio,dispatchEvent(),document,dump(),external,find(),"
                + "focus(),forward(),frameElement,frames,fullScreen,getComputedStyle(),getDefaultComputedStyle(),"
                + "getInterface(),getSelection(),history,home(),ieMethods,indexedDB,innerHeight,innerWidth,"
                + "InstallTrigger,length,localStorage,location,locationbar,matchMedia(),menubar,moveBy(),moveTo(),"
                + "mozAnimationStartTime,mozCancelAnimationFrame(),mozCancelRequestAnimationFrame(),mozIndexedDB,"
                + "mozInnerScreenX,mozInnerScreenY,mozPaintCount,mozRequestAnimationFrame(),name,navigator,onabort,"
                + "onafterprint,onafterscriptexecute,onbeforeprint,onbeforescriptexecute,onbeforeunload,onblur,"
                + "oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,oncopy,oncut,ondblclick,ondevicelight,"
                + "ondevicemotion,ondeviceorientation,ondeviceproximity,ondrag,ondragend,ondragenter,ondragleave,"
                + "ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,"
                + "ongotpointercapture,onhashchange,oninput,oninvalid,onkeydown,onkeypress,onkeyup,onload(),"
                + "onloadeddata,onloadedmetadata,onloadstart,onlostpointercapture,onmessage,onmousedown,"
                + "onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,onmozfullscreenchange,"
                + "onmozfullscreenerror,onmozpointerlockchange,onmozpointerlockerror,onoffline,ononline,onpagehide,"
                + "onpageshow,onpaste,onpause,onplay,onplaying,onpointercancel,onpointerdown,onpointerenter,"
                + "onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onpopstate,onprogress,"
                + "onratechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onshow,onstalled,onsubmit,"
                + "onsuspend,ontimeupdate,onunload,onuserproximity,onvolumechange,onwaiting,onwheel,open(),"
                + "openDialog(),opener,outerHeight,outerWidth,pageXOffset,pageYOffset,parent,performance,"
                + "personalbar,postMessage(),print(),process(),prompt(),releaseEvents(),removeEventListener(),"
                + "requestAnimationFrame(),resizeBy(),resizeTo(),screen,screenX,screenY,scroll(),scrollbars,"
                + "scrollBy(),scrollByLines(),scrollByPages(),scrollMaxX,scrollMaxY,scrollTo(),scrollX,scrollY,self,"
                + "sessionStorage,setInterval(),setResizable(),setTimeout(),showModalDialog(),sidebar,"
                + "sizeToContent(),sortFunction(),speechSynthesis,status,statusbar,stop(),test(),toolbar,top,"
                + "updateCommands(),"
                + "window",
            IE11 = "addEventListener(),alert(),animationStartTime,applicationCache,atob(),blur(),btoa(),"
                + "cancelAnimationFrame(),captureEvents(),clearImmediate(),clearInterval(),clearTimeout(),"
                + "clientInformation,clipboardData,close(),closed,confirm(),console,createXmlDocument(),"
                + "defaultStatus,devicePixelRatio,dispatchEvent(),document,doNotTrack,event,external,focus(),"
                + "frameElement,frames,getComputedStyle(),getSelection(),history,ieMethods,indexedDB,innerHeight,"
                + "innerWidth,item(),length,localStorage,location,matchMedia(),maxConnectionsPerServer,moveBy(),"
                + "moveTo(),msAnimationStartTime,msCancelRequestAnimationFrame(),msClearImmediate(),msCrypto,"
                + "msIndexedDB,msIsStaticHTML(),msMatchMedia(),msRequestAnimationFrame(),msSetImmediate(),"
                + "msWriteProfilerMark(),name,navigate(),navigator,offscreenBuffering,onabort,onafterprint,"
                + "onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,"
                + "ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,"
                + "ondurationchange,onemptied,onended,onerror,onfocus,onfocusin,onfocusout,onhashchange,onhelp,"
                + "oninput,onkeydown,onkeypress,onkeyup,onload(),onloadeddata,onloadedmetadata,onloadstart,"
                + "onmessage,onmousedown,onmouseenter,onmouseleave,onmousemove,onmouseout,onmouseover,onmouseup,"
                + "onmousewheel,onmsgesturechange,onmsgesturedoubletap,onmsgestureend,onmsgesturehold,"
                + "onmsgesturestart,onmsgesturetap,onmsinertiastart,onmspointercancel,onmspointerdown,"
                + "onmspointerenter,onmspointerleave,onmspointermove,onmspointerout,onmspointerover,onmspointerup,"
                + "onoffline,ononline,onpagehide,onpageshow,onpause,onplay,onplaying,onpointercancel,onpointerdown,"
                + "onpointerenter,onpointerleave,onpointermove,onpointerout,onpointerover,onpointerup,onpopstate,"
                + "onprogress,onratechange,onreadystatechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,"
                + "onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,onunload,onvolumechange,onwaiting,open(),"
                + "opener,outerHeight,outerWidth,pageXOffset,pageYOffset,parent,performance,postMessage(),print(),"
                + "process(),prompt(),releaseEvents(),removeEventListener(),requestAnimationFrame(),resizeBy(),"
                + "resizeTo(),screen,screenLeft,screenTop,screenX,screenY,scroll(),scrollBy(),scrollTo(),self,"
                + "sessionStorage,setImmediate(),setInterval(),setTimeout(),showHelp(),showModalDialog(),"
                + "showModelessDialog(),sortFunction(),status,styleMedia,test(),top,toStaticHTML(),toString(),"
                + "window",
            IE8 = "alert(),attachEvent(),blur(),clearInterval(),clearTimeout(),clientInformation,clipboardData,"
                + "close(),closed,confirm(),createPopup(),defaultStatus,detachEvent(),document,event,execScript(),"
                + "external,focus(),frameElement,frames,history,item(),length,localStorage,location,"
                + "maxConnectionsPerServer,moveBy(),moveTo(),name,navigate(),navigator,offscreenBuffering,"
                + "onafterprint,onbeforeprint,onbeforeunload,onblur,onerror,onfocus,onhashchange,onhelp,onload(),"
                + "onmessage,onresize,onscroll,onunload,open(),opener,parent,postMessage(),print(),prompt(),"
                + "resizeBy(),resizeTo(),screen,screenLeft,screenTop,scroll(),scrollBy(),scrollTo(),self,"
                + "sessionStorage,setInterval(),setTimeout(),showHelp(),showModalDialog(),showModelessDialog(),"
                + "status,top,toStaticHTML(),"
                + "window")
    @NotYetImplemented
    public void window() throws Exception {
        testString("window, null");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAbbreviated}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void abbr() throws Exception {
        test("abbr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAcronym}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
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
                + "port,protocol,rel,rev,search,shape,target,text,type,"
                + "username",
            FF31 = "charset,coords,download,hash,host,hostname,href,hreflang,name,origin,password,pathname,ping,"
                + "port,protocol,rel,relList,rev,search,searchParams,shape,target,text,type,"
                + "username",
            IE11 = "charset,coords,hash,host,hostname,href,hreflang,Methods,mimeType,name,nameProp,pathname,port,"
                + "protocol,protocolLong,rel,rev,search,shape,target,text,type,"
                + "urn",
            IE8 = "charset,coords,dataFld,dataFormatAs,dataSrc,hash,host,hostname,href,hreflang,Methods,mimeType,"
                + "name,nameProp,pathname,port,protocol,protocolLong,rel,rev,search,shape,target,type,"
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
    @Alerts(IE11 = "cite,clear,width",
            IE8 = "cite,clear,clear(),width")
    @NotYetImplemented({ IE8, IE11 })
    public void address() throws Exception {
        test("address");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlApplet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,alt,archive,code,codeBase,height,hspace,name,object,vspace,width",
            IE11 = "align,alt,altHtml,archive,BaseHref,border,classid,code,codeBase,codeType,contentDocument,data,"
                + "declare,form,height,hspace,name,object,standby,type,useMap,vspace,"
                + "width",
            IE8 = "align,alt,altHtml,archive,BaseHref,border,code,codeBase,codeType,contentDocument,dataFld,"
                + "dataFormatAs,dataSrc,declare,form,height,hspace,name,onerror,standby,type,useMap,vspace,"
                + "width")
    @NotYetImplemented
    public void applet() throws Exception {
        test("applet");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "alt,coords,hash,host,hostname,href,noHref,origin,password,pathname,ping,port,protocol,search,"
                + "shape,target,"
                + "username",
            FF31 = "alt,coords,download,hash,host,hostname,href,noHref,origin,password,pathname,ping,port,protocol,"
                + "rel,relList,search,searchParams,shape,target,"
                + "username",
            IE = "alt,coords,hash,host,hostname,href,noHref,pathname,port,protocol,search,shape,target")
    @NotYetImplemented
    public void area() throws Exception {
        test("area");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlArticle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void article() throws Exception {
        test("article");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAside}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void aside() throws Exception {
        test("aside");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAudio}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "addTextTrack(),autoplay,buffered,canPlayType(),controls,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,duration,ended,error,HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,"
                + "HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,load(),loop,muted,NETWORK_EMPTY,NETWORK_IDLE,"
                + "NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,onwebkitkeyadded,onwebkitkeyerror,"
                + "onwebkitkeymessage,onwebkitneedkey,pause(),paused,play(),playbackRate,played,preload,readyState,"
                + "seekable,seeking,src,textTracks,volume,webkitAddKey(),webkitAudioDecodedByteCount,"
                + "webkitCancelKeyRequest(),webkitGenerateKeyRequest(),"
                + "webkitVideoDecodedByteCount",
            FF31 = "addTextTrack(),autoplay,buffered,canPlayType(),controls,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,duration,ended,error,fastSeek(),HAVE_CURRENT_DATA,"
                + "HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,load(),loop,mozAudioCaptured,"
                + "mozAudioChannelType,mozAutoplayEnabled,mozCaptureStream(),mozCaptureStreamUntilEnded(),"
                + "mozFragmentEnd,mozGetMetadata(),mozPreservesPitch,mozSrcObject,muted,NETWORK_EMPTY,NETWORK_IDLE,"
                + "NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,pause(),paused,play(),playbackRate,played,preload,"
                + "readyState,seekable,seeking,src,textTracks,"
                + "volume",
            IE11 = "addTextTrack(),audioTracks,autobuffer,autoplay,buffered,canPlayType(),controls,currentSrc,"
                + "currentTime,defaultPlaybackRate,duration,ended,error,HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,"
                + "HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,initialTime,load(),loop,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,muted,NETWORK_EMPTY,NETWORK_IDLE,NETWORK_LOADING,"
                + "NETWORK_NO_SOURCE,networkState,pause(),paused,play(),playbackRate,played,preload,readyState,"
                + "seekable,seeking,src,textTracks,"
                + "volume",
            IE8 = "")
    @NotYetImplemented({ CHROME, IE11, FF24, FF31 })
    public void audio() throws Exception {
        test("audio");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBackgroundSound}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "balance,loop,src,volume")
    @NotYetImplemented(IE)
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
    @Alerts(IE = "color,face,size")
    public void basefont() throws Exception {
        test("basefont");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBidirectionalIsolation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void bdi() throws Exception {
        test("bdi");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void bdo() throws Exception {
        test("bdo");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBig}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void big() throws Exception {
        test("big");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBlink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
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
            IE11 = "cite,clear,width",
            IE8 = "cite,clear,clear(),width")
    @NotYetImplemented(IE)
    public void blockquote() throws Exception {
        test("blockquote");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "aLink,background,bgColor,link,onbeforeunload,onhashchange,onlanguagechange,onmessage,onoffline,"
                + "ononline,onpagehide,onpageshow,onpopstate,onstorage,onunload,text,"
                + "vLink",
            FF31 = "aLink,background,bgColor,link,onafterprint,onbeforeprint,onbeforeunload,onhashchange,onmessage,"
                + "onoffline,ononline,onpagehide,onpageshow,onpopstate,onresize,onunload,text,"
                + "vLink",
            IE11 = "aLink,background,bgColor,bgProperties,bottomMargin,createTextRange(),leftMargin,link,noWrap,"
                + "onafterprint,onbeforeprint,onbeforeunload,onhashchange,onmessage,onoffline,ononline,onpagehide,"
                + "onpageshow,onpopstate,onresize,onstorage,onunload,rightMargin,scroll,text,topMargin,"
                + "vLink",
            IE8 = "aLink,background,bgColor,bgProperties,bottomMargin,createTextRange(),leftMargin,link,noWrap,"
                + "onafterprint,onbeforeprint,onbeforeunload,onhashchange,onload,onoffline,ononline,onselect,"
                + "onunload,rightMargin,scroll,scroll(),text,topMargin,"
                + "vLink")
    @NotYetImplemented
    public void body() throws Exception {
        test("body");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBold}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void b() throws Exception {
        test("b");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "clear",
            IE8 = "clear,clear()")
    public void br() throws Exception {
        test("br");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "autofocus,checkValidity(),disabled,form,formAction,formEnctype,formMethod,formNoValidate,"
                + "formTarget,labels,name,reportValidity(),setCustomValidity(),type,validationMessage,validity,"
                + "value,"
                + "willValidate",
            FF31 = "autofocus,checkValidity(),disabled,form,formAction,formEnctype,formMethod,formNoValidate,"
                + "formTarget,name,setCustomValidity(),type,validationMessage,validity,value,"
                + "willValidate",
            IE11 = "autofocus,checkValidity(),createTextRange(),form,formAction,formEnctype,formMethod,"
                + "formNoValidate,formTarget,name,setCustomValidity(),status,type,validationMessage,validity,value,"
                + "willValidate",
            IE8 = "createTextRange(),dataFld,dataFormatAs,dataSrc,form,name,status,type,value")
    @NotYetImplemented
    public void button() throws Exception {
        test("button");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCanvas}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "getContext(),height,toDataURL(),width",
            FF31 = "getContext(),height,mozGetAsFile(),mozOpaque,mozPrintCallback,toBlob(),toDataURL(),width",
            IE11 = "getContext(),height,msToBlob(),toDataURL(),width",
            IE8 = "")
    @NotYetImplemented({ IE11, FF31 })
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
    @Alerts(IE11 = "cite,clear,width",
            IE8 = "cite,clear,clear(),width")
    @NotYetImplemented({ IE8, IE11 })
    public void center() throws Exception {
        test("center");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCitation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void cite() throws Exception {
        test("cite");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void code() throws Exception {
        test("code");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCommand}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void command() throws Exception {
        test("command");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDataList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "options",
            IE8 = "")
    public void datalist() throws Exception {
        test("datalist");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinition}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void dfn() throws Exception {
        test("dfn");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "noWrap")
    @NotYetImplemented(IE)
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
    @NotYetImplemented(IE8)
    public void del() throws Exception {
        test("del");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDetails}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            CHROME = "open")
    @NotYetImplemented(CHROME)
    public void details() throws Exception {
        test("details");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDialog}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            CHROME = "close(),open,returnValue,show(),showModal()")
    @NotYetImplemented(CHROME)
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
    @NotYetImplemented(IE11)
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
            IE11 = "align,noWrap",
            IE8 = "align,dataFld,dataFormatAs,dataSrc,noWrap")
    @NotYetImplemented(IE)
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
    @Alerts(IE = "noWrap")
    @NotYetImplemented(IE)
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
            IE11 = "getSVGDocument(),height,msPlayToDisabled,msPlayToPreferredSourceUri,msPlayToPrimary,name,palette,"
                + "pluginspage,readyState,src,units,"
                + "width",
            IE8 = "height,hidden,name,palette,pluginspage,src,units,width")
    @NotYetImplemented
    public void embed() throws Exception {
        test("embed");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlEmphasis}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
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
                + "validationMessage,validity,"
                + "willValidate",
            FF31 = "checkValidity(),disabled,elements,form,name,setCustomValidity(),type,validationMessage,validity,"
                + "willValidate",
            IE11 = "align,checkValidity(),form,setCustomValidity(),validationMessage,validity,willValidate",
            IE8 = "align,form")
    @NotYetImplemented({ CHROME, IE11, FF24, FF31 })
    public void fieldset() throws Exception {
        test("fieldset");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFigureCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void figcaption() throws Exception {
        test("figcaption");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFigure}.
     *
     * @throws Exception if the test fails
     */
    @Test
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
    @Alerts(DEFAULT = "acceptCharset,action,autocomplete,checkValidity(),elements,encoding,enctype,length,method,name,"
                + "noValidate,reportValidity(),requestAutocomplete(),reset(),submit(),"
                + "target",
            FF31 = "acceptCharset,action,autocomplete,checkValidity(),elements,encoding,enctype,length,method,name,"
                + "noValidate,reset(),submit(),"
                + "target",
            IE11 = "acceptCharset,action,autocomplete,checkValidity(),elements,encoding,enctype,item(),length,method,"
                + "name,namedItem(),noValidate,reset(),submit(),"
                + "target",
            IE8 = "acceptCharset,action,elements,encoding,item(),length,method,name,namedItem(),onreset,onsubmit,"
                + "reset(),submit(),tags(),target,"
                + "urns()")
    @NotYetImplemented
    public void form() throws Exception {
        test("form");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void footer() throws Exception {
        test("footer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "contentDocument,contentWindow,frameBorder,getSVGDocument(),longDesc,marginHeight,marginWidth,"
                + "name,noResize,scrolling,"
                + "src",
            FF31 = "contentDocument,contentWindow,frameBorder,longDesc,marginHeight,marginWidth,name,noResize,"
                + "scrolling,"
                + "src",
            IE11 = "border,borderColor,contentDocument,contentWindow,frameBorder,frameSpacing,getSVGDocument(),"
                + "height,longDesc,marginHeight,marginWidth,name,noResize,scrolling,security,src,"
                + "width",
            IE8 = "allowTransparency,border,borderColor,contentDocument,contentWindow,dataFld,dataFormatAs,dataSrc,"
                + "frameBorder,frameSpacing,height,longDesc,marginHeight,marginWidth,name,noResize,onload,scrolling,"
                + "src,"
                + "width")
    @NotYetImplemented
    public void frame() throws Exception {
        test("frame");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFrameSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "cols,onbeforeunload,onhashchange,onlanguagechange,onmessage,onoffline,ononline,onpagehide,"
                + "onpageshow,onpopstate,onstorage,onunload,"
                + "rows",
            FF31 = "cols,onafterprint,onbeforeprint,onbeforeunload,onhashchange,onmessage,onoffline,ononline,"
                + "onpagehide,onpageshow,onpopstate,onresize,onunload,"
                + "rows",
            IE11 = "border,borderColor,cols,frameBorder,frameSpacing,name,onafterprint,onbeforeprint,onbeforeunload,"
                + "onhashchange,onmessage,onoffline,ononline,onpagehide,onpageshow,onresize,onstorage,onunload,"
                + "rows",
            IE8 = "border,borderColor,cols,frameBorder,frameSpacing,name,onafterprint,onbeforeprint,onbeforeunload,"
                + "onload,onunload,"
                + "rows")
    @NotYetImplemented
    public void frameset() throws Exception {
        test("frameset");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHead}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "profile")
    @NotYetImplemented(IE)
    public void head() throws Exception {
        test("head");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
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
            IE11 = "align,clear",
            IE8 = "align,clear,clear()")
    @NotYetImplemented(IE)
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
            IE11 = "align,clear",
            IE8 = "align,clear,clear()")
    @NotYetImplemented(IE)
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
            IE11 = "align,clear",
            IE8 = "align,clear,clear()")
    @NotYetImplemented(IE)
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
            IE11 = "align,clear",
            IE8 = "align,clear,clear()")
    @NotYetImplemented(IE)
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
            IE11 = "align,clear",
            IE8 = "align,clear,clear()")
    @NotYetImplemented(IE)
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
            IE11 = "align,clear",
            IE8 = "align,clear,clear()")
    @NotYetImplemented(IE)
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
    @NotYetImplemented
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
    @NotYetImplemented
    public void html() throws Exception {
        test("html");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInlineFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,allowFullscreen,contentDocument,contentWindow,frameBorder,getSVGDocument(),height,"
                + "longDesc,marginHeight,marginWidth,name,sandbox,scrolling,src,srcdoc,"
                + "width",
            IE11 = "align,border,contentDocument,contentWindow,frameBorder,frameSpacing,getSVGDocument(),height,"
                + "hspace,longDesc,marginHeight,marginWidth,name,noResize,sandbox,scrolling,security,src,vspace,"
                + "width",
            IE8 = "align,allowTransparency,border,contentDocument,contentWindow,dataFld,dataFormatAs,dataSrc,"
                + "frameBorder,frameSpacing,height,hspace,longDesc,marginHeight,marginWidth,name,noResize,onload,"
                + "scrolling,src,vspace,"
                + "width")
    @NotYetImplemented
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
    @NotYetImplemented(IE8)
    public void q() throws Exception {
        test("q");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,alt,border,complete,crossOrigin,currentSrc,height,hspace,isMap,longDesc,lowsrc,name,"
                + "naturalHeight,naturalWidth,sizes,src,srcset,useMap,vspace,width,x,"
                + "y",
            FF31 = "align,alt,border,complete,crossOrigin,height,hspace,isMap,longDesc,lowsrc,name,naturalHeight,"
                + "naturalWidth,src,useMap,vspace,width,x,"
                + "y",
            IE11 = "align,alt,border,complete,crossOrigin,dynsrc,fileCreatedDate,fileModifiedDate,fileUpdatedDate,"
                + "height,href,hspace,isMap,longDesc,loop,lowsrc,mimeType,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,name,nameProp,naturalHeight,naturalWidth,protocol,src,"
                + "start,useMap,vrml,vspace,"
                + "width",
            IE8 = "align,alt,border,complete,dataFld,dataFormatAs,dataSrc,dynsrc,fileCreatedDate,fileModifiedDate,"
                + "fileSize,fileUpdatedDate,height,href,hspace,isMap,longDesc,loop,lowsrc,mimeType,name,nameProp,"
                + "onabort,onerror,onload,protocol,src,start,start(),useMap,vrml,vspace,"
                + "width")
    @NotYetImplemented
    public void img() throws Exception {
        test("img");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE11 = "align,alt,border,complete,crossOrigin,dynsrc,fileCreatedDate,fileModifiedDate,fileUpdatedDate,"
                + "height,href,hspace,isMap,longDesc,loop,lowsrc,mimeType,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,name,nameProp,naturalHeight,naturalWidth,protocol,src,"
                + "start,useMap,vrml,vspace,"
                + "width",
            IE8 = "align,alt,border,complete,dataFld,dataFormatAs,dataSrc,dynsrc,fileCreatedDate,fileModifiedDate,"
                + "fileSize,fileUpdatedDate,height,href,hspace,isMap,longDesc,loop,lowsrc,mimeType,name,nameProp,"
                + "onabort,onerror,onload,protocol,src,start,start(),useMap,vrml,vspace,"
                + "width")
    @NotYetImplemented({ IE8, IE11 })
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
    @NotYetImplemented(IE8)
    public void ins() throws Exception {
        test("ins");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlIsIndex}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE11 = "action,form,prompt",
            IE8 = "action,form,prompt,prompt()")
    @NotYetImplemented(IE)
    public void isindex() throws Exception {
        test("isindex");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlItalic}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void i() throws Exception {
        test("i");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlKeyboard}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void kbd() throws Exception {
        test("kbd");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlKeygen}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            CHROME = "autofocus,challenge,checkValidity(),disabled,form,keytype,labels,name,reportValidity(),"
                + "setCustomValidity(),type,validationMessage,validity,"
                + "willValidate",
            IE11 = "cite,clear,width")
    @NotYetImplemented({ IE11, CHROME })
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
            IE11 = "form,htmlFor",
            IE8 = "dataFld,dataFormatAs,dataSrc,form,htmlFor")
    @NotYetImplemented({ FF31, CHROME })
    public void label() throws Exception {
        test("label");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void layer() throws Exception {
        test("layer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLegend}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,form",
            IE8 = "align,dataFld,dataFormatAs,dataSrc,form")
    @NotYetImplemented
    public void legend() throws Exception {
        test("legend");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlListing}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "width",
            FF31 = "",
            IE11 = "cite,clear,width",
            IE8 = "cite,clear,clear(),width")
    @NotYetImplemented({ IE8, IE11, CHROME })
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
    @NotYetImplemented
    public void li() throws Exception {
        test("li");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "charset,crossOrigin,disabled,href,hreflang,import,media,rel,rev,sheet,sizes,target,type",
            FF31 = "charset,crossOrigin,disabled,href,hreflang,media,rel,relList,rev,sheet,sizes,target,type",
            IE11 = "charset,href,hreflang,media,rel,rev,sheet,target,type",
            IE8 = "charset,href,hreflang,media,onerror,onload,rel,rev,styleSheet,target,type")
    @NotYetImplemented
    public void link() throws Exception {
        test("link");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMain}.
     *
     * @throws Exception if the test fails
     */
    @Test
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
    @NotYetImplemented
    public void map() throws Exception {
        test("map");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMark}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void mark() throws Exception {
        test("mark");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMarquee}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "behavior,bgColor,direction,height,hspace,loop,scrollAmount,scrollDelay,start(),stop(),trueSpeed,"
                + "vspace,"
                + "width",
            FF31 = "align",
            IE11 = "behavior,bgColor,direction,height,hspace,loop,onbounce,onfinish,onstart,scrollAmount,scrollDelay,"
                + "start(),stop(),trueSpeed,vspace,"
                + "width",
            IE8 = "behavior,bgColor,dataFld,dataFormatAs,dataSrc,direction,height,hspace,loop,onbounce,onfinish,"
                + "onstart,scrollAmount,scrollDelay,start(),stop(),trueSpeed,vspace,"
                + "width")
    @NotYetImplemented({ IE8, IE11, CHROME })
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
            FF31 = "compact,label,type",
            IE = "compact,type")
    @NotYetImplemented({ IE11, FF31 })
    public void menu() throws Exception {
        test("menu");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMenuItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF31 = "checked,defaultChecked,disabled,icon,label,radiogroup,type")
    @NotYetImplemented(FF31)
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
            FF31 = "high,low,max,min,optimum,value",
            IE = "")
    @NotYetImplemented(CHROME)
    public void meter() throws Exception {
        test("meter");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMultiColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void multicol() throws Exception {
        test("multicol");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNav}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void nav() throws Exception {
        test("nav");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNextId}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "n")
    @NotYetImplemented(IE)
    public void nextid() throws Exception {
        test("nextid");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void nobr() throws Exception {
        test("nobr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void noembed() throws Exception {
        test("noembed");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoFrames}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void noframes() throws Exception {
        test("noframes");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void nolayer() throws Exception {
        test("nolayer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void noscript() throws Exception {
        test("noscript");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlObject}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "align,archive,border,checkValidity(),code,codeBase,codeType,contentDocument,data,declare,form,"
                + "getSVGDocument(),height,hspace,name,reportValidity(),setCustomValidity(),standby,type,useMap,"
                + "validationMessage,validity,vspace,width,"
                + "willValidate",
            FF31 = "align,archive,border,checkValidity(),code,codeBase,codeType,contentDocument,contentWindow,data,"
                + "declare,form,getSVGDocument(),height,hspace,name,setCustomValidity(),standby,type,typeMustMatch,"
                + "useMap,validationMessage,validity,vspace,width,"
                + "willValidate",
            IE11 = "align,alt,altHtml,archive,BaseHref,border,checkValidity(),classid,code,codeBase,codeType,"
                + "contentDocument,data,declare,form,getSVGDocument(),height,hspace,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,name,object,readyState,setCustomValidity(),standby,"
                + "type,useMap,validationMessage,validity,vspace,width,"
                + "willValidate",
            IE8 = "align,alt,altHtml,archive,BaseHref,border,code,codeBase,codeType,contentDocument,dataFld,"
                + "dataFormatAs,dataSrc,declare,form,height,hspace,name,onerror,standby,type,useMap,vspace,"
                + "width")
    @NotYetImplemented
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
            IE11 = "compact,start,type",
            IE8 = "compact,start,start(),type")
    @NotYetImplemented
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
            IE11 = "defaultSelected,form,index,label,selected,text,value",
            IE8 = "dataFld,dataFormatAs,dataSrc,defaultSelected,form,index,label,selected,text,value")
    @NotYetImplemented({ IE8, IE11 })
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
            IE11 = "defaultSelected,form,index,label,selected,text,value",
            IE8 = "dataFld,dataFormatAs,dataSrc,defaultSelected,form,index,label,selected,text,value")
    @NotYetImplemented
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
            FF31 = "checkValidity(),defaultValue,form,htmlFor,name,setCustomValidity(),type,validationMessage,"
                + "validity,value,"
                + "willValidate",
            IE = "")
    @NotYetImplemented({ FF31, CHROME })
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
            IE11 = "align,clear",
            IE8 = "align,clear,clear()")
    @NotYetImplemented(IE)
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
    @NotYetImplemented
    public void param() throws Exception {
        test("param");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPlainText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE11 = "cite,clear,width",
            IE8 = "cite,clear,clear(),width")
    @NotYetImplemented({ IE8, IE11 })
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
            IE11 = "cite,clear,width",
            IE8 = "cite,clear,clear(),width")
    @NotYetImplemented
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
            FF31 = "max,position,value",
            IE11 = "form,max,position,value",
            IE8 = "")
    @NotYetImplemented({ CHROME, IE11, FF24, FF31 })
    public void progress() throws Exception {
        test("progress");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRp}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    @NotYetImplemented(IE)
    public void rp() throws Exception {
        test("rp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRt}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    @NotYetImplemented(IE)
    public void rt() throws Exception {
        test("rt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRuby}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    @NotYetImplemented(IE)
    public void ruby() throws Exception {
        test("ruby");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlS}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void s() throws Exception {
        test("s");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void samp() throws Exception {
        test("samp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "async,charset,crossOrigin,defer,event,htmlFor,src,text,type",
            IE11 = "async,charset,defer,event,htmlFor,src,text,type",
            IE8 = "charset,defer,event,htmlFor,onerror,src,text,type")
    @NotYetImplemented
    public void script() throws Exception {
        test("script");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void section() throws Exception {
        test("section");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "add(),autofocus,checkValidity(),disabled,form,item(),labels,length,multiple,name,namedItem(),"
                + "options,reportValidity(),required,selectedIndex,selectedOptions,setCustomValidity(),size,type,"
                + "validationMessage,validity,value,"
                + "willValidate",
            FF31 = "@@iterator(),add(),autofocus,checkValidity(),disabled,form,item(),length,multiple,name,"
                + "namedItem(),options,required,selectedIndex,selectedOptions,setCustomValidity(),size,type,"
                + "validationMessage,validity,value,"
                + "willValidate",
            IE11 = "add(),autofocus,checkValidity(),form,item(),length,multiple,name,namedItem(),options,remove(),"
                + "required,selectedIndex,setCustomValidity(),size,type,validationMessage,validity,value,"
                + "willValidate",
            IE8 = "add(),dataFld,dataFormatAs,dataSrc,form,item(),length,multiple,name,namedItem(),onchange,options,"
                + "remove(),selectedIndex,size,tags(),type,urns(),"
                + "value")
    @NotYetImplemented
    public void select() throws Exception {
        test("select");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSmall}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
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
            FF31 = "media,src,type",
            IE11 = "media,src,type",
            IE8 = "")
    @NotYetImplemented({ CHROME, IE11, FF24, FF31 })
    public void source() throws Exception {
        test("source");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSpan}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = "dataFld,dataFormatAs,dataSrc")
    public void span() throws Exception {
        test("span");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStrike}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void strike() throws Exception {
        test("strike");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStrong}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
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
            FF31 = "disabled,media,scoped,sheet,type",
            IE11 = "media,sheet,type",
            IE8 = "media,onerror,onload,styleSheet,type")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void style() throws Exception {
        test("style");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSubscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void sub() throws Exception {
        test("sub");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSummary}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void summary() throws Exception {
        test("summary");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
    public void sup() throws Exception {
        test("sup");
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
            IE11 = "align,background,bgColor,border,borderColor,borderColorDark,borderColorLight,caption,cellPadding,"
                + "cells,cellSpacing,cols,createCaption(),createTBody(),createTFoot(),createTHead(),deleteCaption(),"
                + "deleteRow(),deleteTFoot(),deleteTHead(),frame,height,insertRow(),moveRow(),rows,rules,summary,"
                + "tBodies,tFoot,tHead,"
                + "width",
            IE8 = "align,background,bgColor,border,borderColor,borderColorDark,borderColorLight,caption,cellPadding,"
                + "cells,cellSpacing,cols,createCaption(),createTFoot(),createTHead(),dataFld,dataFormatAs,"
                + "dataPageSize,dataSrc,deleteCaption(),deleteRow(),deleteTFoot(),deleteTHead(),firstPage(),frame,"
                + "height,insertRow(),lastPage(),moveRow(),nextPage(),previousPage(),refresh(),rows,rules,summary,"
                + "tBodies,tFoot,tHead,"
                + "width")
    @NotYetImplemented
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
    @NotYetImplemented
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
    @NotYetImplemented
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
    @NotYetImplemented({ IE8, IE11 })
    public void tr() throws Exception {
        test("tr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTextArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "autofocus,checkValidity(),cols,defaultValue,dirName,disabled,form,labels,maxLength,minLength,"
                + "name,placeholder,readOnly,reportValidity(),required,rows,select(),selectionDirection,"
                + "selectionEnd,selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),textLength,"
                + "type,validationMessage,validity,value,willValidate,"
                + "wrap",
            FF31 = "autofocus,checkValidity(),cols,defaultValue,disabled,form,maxLength,name,placeholder,readOnly,"
                + "required,rows,select(),selectionDirection,selectionEnd,selectionStart,setCustomValidity(),"
                + "setRangeText(),setSelectionRange(),textLength,type,validationMessage,validity,value,willValidate,"
                + "wrap",
            IE11 = "autofocus,checkValidity(),cols,createTextRange(),defaultValue,form,maxLength,name,placeholder,"
                + "readOnly,required,rows,select(),selectionEnd,selectionStart,setCustomValidity(),"
                + "setSelectionRange(),status,type,validationMessage,validity,value,willValidate,"
                + "wrap",
            IE8 = "cols,createTextRange(),dataFld,dataFormatAs,dataSrc,form,name,onchange,onselect,readOnly,rows,"
                + "select(),status,type,value,"
                + "wrap")
    @NotYetImplemented
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
    @Alerts(IE = "cite,dateTime")
    public void tt() throws Exception {
        test("tt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTime}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF31 = "dateTime")
    @NotYetImplemented(FF31)
    public void time() throws Exception {
        test("time");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTitle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text",
            IE8 = "")
    public void title() throws Exception {
        test("title");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTrack}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "default,ERROR,kind,label,LOADED,LOADING,NONE,readyState,src,srclang,track",
            IE8 = "")
    @NotYetImplemented({ CHROME, IE11, FF24, FF31 })
    public void track() throws Exception {
        test("track");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlUnderlined}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "cite,dateTime")
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
    @Alerts(IE = "cite,dateTime")
    public void var() throws Exception {
        test("var");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlVideo}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "addTextTrack(),autoplay,buffered,canPlayType(),controls,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,duration,ended,error,HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,"
                + "HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,height,load(),loop,muted,NETWORK_EMPTY,NETWORK_IDLE,"
                + "NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,onwebkitkeyadded,onwebkitkeyerror,"
                + "onwebkitkeymessage,onwebkitneedkey,pause(),paused,play(),playbackRate,played,poster,preload,"
                + "readyState,seekable,seeking,src,textTracks,videoHeight,videoWidth,volume,webkitAddKey(),"
                + "webkitAudioDecodedByteCount,webkitCancelKeyRequest(),webkitDecodedFrameCount,"
                + "webkitDisplayingFullscreen,webkitDroppedFrameCount,webkitEnterFullscreen(),"
                + "webkitEnterFullScreen(),webkitExitFullscreen(),webkitExitFullScreen(),webkitGenerateKeyRequest(),"
                + "webkitSupportsFullscreen,webkitVideoDecodedByteCount,"
                + "width",
            FF31 = "addTextTrack(),autoplay,buffered,canPlayType(),controls,crossOrigin,currentSrc,currentTime,"
                + "defaultMuted,defaultPlaybackRate,duration,ended,error,fastSeek(),HAVE_CURRENT_DATA,"
                + "HAVE_ENOUGH_DATA,HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,height,load(),loop,mozAudioCaptured,"
                + "mozAudioChannelType,mozAutoplayEnabled,mozCaptureStream(),mozCaptureStreamUntilEnded(),"
                + "mozDecodedFrames,mozFragmentEnd,mozFrameDelay,mozGetMetadata(),mozHasAudio,mozPaintedFrames,"
                + "mozParsedFrames,mozPresentedFrames,mozPreservesPitch,mozSrcObject,muted,NETWORK_EMPTY,"
                + "NETWORK_IDLE,NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,pause(),paused,play(),playbackRate,"
                + "played,poster,preload,readyState,seekable,seeking,src,textTracks,videoHeight,videoWidth,volume,"
                + "width",
            IE11 = "addTextTrack(),audioTracks,autobuffer,autoplay,buffered,canPlayType(),controls,currentSrc,"
                + "currentTime,defaultPlaybackRate,duration,ended,error,HAVE_CURRENT_DATA,HAVE_ENOUGH_DATA,"
                + "HAVE_FUTURE_DATA,HAVE_METADATA,HAVE_NOTHING,height,initialTime,load(),loop,msPlayToDisabled,"
                + "msPlayToPreferredSourceUri,msPlayToPrimary,msZoom,muted,NETWORK_EMPTY,NETWORK_IDLE,"
                + "NETWORK_LOADING,NETWORK_NO_SOURCE,networkState,pause(),paused,play(),playbackRate,played,poster,"
                + "preload,readyState,seekable,seeking,src,textTracks,videoHeight,videoWidth,volume,"
                + "width",
            IE8 = "")
    @NotYetImplemented({ CHROME, IE11, FF24, FF31 })
    public void video() throws Exception {
        test("video");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlWordBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
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
            FF31 = "",
            IE11 = "cite,clear,width",
            IE8 = "cite,clear,clear(),width")
    @NotYetImplemented({ IE8, IE11, CHROME })
    public void xmp() throws Exception {
        test("xmp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = "accept,align,alt,border,checked,complete,createTextRange(),dataFld,dataFormatAs,dataSrc,"
                + "defaultChecked,dynsrc,form,height,hspace,indeterminate,loop,lowsrc,maxLength,name,onabort,"
                + "onchange,onerror,onload,onselect,readOnly,select(),size,src,start,start(),status,type,useMap,"
                + "value,vrml,vspace,"
                + "width",
            FF31 = "accept,align,alt,autocomplete,autofocus,checked,checkValidity(),defaultChecked,defaultValue,"
                + "disabled,files,form,formAction,formEnctype,formMethod,formNoValidate,formTarget,height,"
                + "indeterminate,list,max,maxLength,min,mozIsTextField(),multiple,name,pattern,placeholder,readOnly,"
                + "required,select(),selectionDirection,selectionEnd,selectionStart,setCustomValidity(),"
                + "setRangeText(),setSelectionRange(),size,src,step,stepDown(),stepUp(),textLength,type,useMap,"
                + "validationMessage,validity,value,valueAsNumber,width,"
                + "willValidate",
            IE11 = "accept,align,alt,autocomplete,autofocus,border,checked,checkValidity(),complete,"
                + "createTextRange(),defaultChecked,defaultValue,dynsrc,files,form,formAction,formEnctype,"
                + "formMethod,formNoValidate,formTarget,height,hspace,indeterminate,list,loop,lowsrc,max,maxLength,"
                + "min,multiple,name,pattern,placeholder,readOnly,required,select(),selectionEnd,selectionStart,"
                + "setCustomValidity(),setSelectionRange(),size,src,start,status,step,stepDown(),stepUp(),type,"
                + "useMap,validationMessage,validity,value,valueAsNumber,vrml,vspace,width,"
                + "willValidate",
            CHROME = "accept,align,alt,autocomplete,autofocus,checked,checkValidity(),defaultChecked,defaultValue,"
                + "dirName,disabled,files,form,formAction,formEnctype,formMethod,formNoValidate,formTarget,height,"
                + "incremental,indeterminate,labels,list,max,maxLength,min,minLength,multiple,name,pattern,"
                + "placeholder,readOnly,reportValidity(),required,select(),selectionDirection,selectionEnd,"
                + "selectionStart,setCustomValidity(),setRangeText(),setSelectionRange(),size,src,step,stepDown(),"
                + "stepUp(),type,useMap,validationMessage,validity,value,valueAsDate,valueAsNumber,webkitdirectory,"
                + "webkitEntries,width,"
                + "willValidate")
    @NotYetImplemented
    public void input() throws Exception {
        test("input");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "value")
    @NotYetImplemented(FF)
    public void data() throws Exception {
        test("data");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlContent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "getDistributedNodes(),select",
            IE = "")
    @NotYetImplemented({ CHROME, FF })
    public void content() throws Exception {
        test("content");
    }
}
