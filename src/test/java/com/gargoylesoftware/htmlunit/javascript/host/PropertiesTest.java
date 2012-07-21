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
package com.gargoylesoftware.htmlunit.javascript.host;

import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.SortOrder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for all host properties and methods.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 */
@RunWith(Parameterized.class)
public class PropertiesTest extends WebTestCase {

    private static final Log LOG = LogFactory.getLog(PropertiesTest.class);

    private static List<String> IE6_;
    private static List<String> IE7_;
    private static List<String> IE8_;
    private static List<String> FF3_6_;

    private static List<String> IE6_SIMULATED_;
    private static List<String> IE7_SIMULATED_;
    private static List<String> IE8_SIMULATED_;
    private static List<String> FF3_6_SIMULATED_;

    private static DefaultCategoryDataset CATEGORY_DATASET_IE6_ = new DefaultCategoryDataset();
    private static DefaultCategoryDataset CATEGORY_DATASET_IE7_ = new DefaultCategoryDataset();
    private static DefaultCategoryDataset CATEGORY_DATASET_IE8_ = new DefaultCategoryDataset();
    private static DefaultCategoryDataset CATEGORY_DATASET_FF3_6_ = new DefaultCategoryDataset();

    private static StringBuilder IE6_HTML_ = new StringBuilder();
    private static StringBuilder IE7_HTML_ = new StringBuilder();
    private static StringBuilder IE8_HTML_ = new StringBuilder();
    private static StringBuilder FF3_6_HTML_ = new StringBuilder();

    private static MutableInt IE6_ACTUAL_PROPERTY_COUNT_ = new MutableInt();
    private static MutableInt IE7_ACTUAL_PROPERTY_COUNT_ = new MutableInt();
    private static MutableInt IE8_ACTUAL_PROPERTY_COUNT_ = new MutableInt();
    private static MutableInt FF3_6_ACTUAL_PROPERTY_COUNT_ = new MutableInt();

    private static MutableInt IE6_REMAINING_PROPERTY_COUNT_ = new MutableInt();
    private static MutableInt IE7_REMAINING_PROPERTY_COUNT_ = new MutableInt();
    private static MutableInt IE8_REMAINING_PROPERTY_COUNT_ = new MutableInt();
    private static MutableInt FF3_6_REMAINING_PROPERTY_COUNT_ = new MutableInt();

    private final String name_;
    private final BrowserVersion browserVersion_;

    /**
     * Returns the data for this parameterized test.
     * @return list of all test parameters
     * @throws Exception If an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        IE6_ = getProperties(BrowserVersion.INTERNET_EXPLORER_6);
        IE7_ = getProperties(BrowserVersion.INTERNET_EXPLORER_7);
        IE8_ = getProperties(BrowserVersion.INTERNET_EXPLORER_8);
        FF3_6_ = getProperties(BrowserVersion.FIREFOX_3_6);
        Assert.assertEquals(IE6_.size(), IE7_.size());
        Assert.assertEquals(IE6_.size(), IE8_.size());
        Assert.assertEquals(IE6_.size(), FF3_6_.size());
        IE6_SIMULATED_ = getSimulatedProperties(BrowserVersion.INTERNET_EXPLORER_6);
        IE7_SIMULATED_ = getSimulatedProperties(BrowserVersion.INTERNET_EXPLORER_7);
        IE8_SIMULATED_ = getSimulatedProperties(BrowserVersion.INTERNET_EXPLORER_8);
        FF3_6_SIMULATED_ = getSimulatedProperties(BrowserVersion.FIREFOX_3_6);
        Assert.assertEquals(IE6_SIMULATED_.size(), IE7_SIMULATED_.size());
        Assert.assertEquals(IE6_SIMULATED_.size(), IE8_SIMULATED_.size());
        Assert.assertEquals(IE6_SIMULATED_.size(), FF3_6_SIMULATED_.size());
        final Collection<Object[]> list = new ArrayList<Object[]>();
        for (final String line : IE6_) {
            final String name = line.substring(0, line.indexOf(':'));
            list.add(new Object[] {name, BrowserVersion.INTERNET_EXPLORER_6});
            list.add(new Object[] {name, BrowserVersion.INTERNET_EXPLORER_7});
            list.add(new Object[] {name, BrowserVersion.INTERNET_EXPLORER_8});
            list.add(new Object[] {name, BrowserVersion.FIREFOX_3_6});
        }
        return list;
    }

    private static List<String> getProperties(final BrowserVersion browserVersion) throws Exception {
        final URL url = PropertiesTest.class.getClassLoader().getResource(
                "objects/properties." + browserVersion.getNickname() + ".txt");
        return FileUtils.readLines(new File(url.toURI()));
    }

    private static List<String> getSimulatedProperties(final BrowserVersion browserVersion) throws Exception {
        final URL url = PropertiesTest.class.getClassLoader().getResource("objects/properties.html");
        final WebClient webClient = new WebClient(browserVersion);
        final HtmlPage page = webClient.getPage(url);
        final HtmlTextArea textarea = page.getHtmlElementById("myTextarea");
        return Arrays.asList(textarea.getText().split("\r\n|\n"));
    }

    /**
     * Constructs a new test.
     * @param name the name of the object
     * @param browserVersion the browser version
     */
    public PropertiesTest(final String name, final BrowserVersion browserVersion) {
        name_ = name;
        browserVersion_ = browserVersion;
    }

    /**
     * Test.
     * @throws IOException If an error occurs
     */
    @Test
    public void test() throws IOException {
        final List<String> realList;
        final List<String> simulatedList;
        final DefaultCategoryDataset dataset;
        final StringBuilder html;
        final MutableInt actualPropertyCount;
        final MutableInt remainingPropertyCount;
        if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_6) {
            realList = IE6_;
            simulatedList = IE6_SIMULATED_;
            dataset = CATEGORY_DATASET_IE6_;
            html = IE6_HTML_;
            actualPropertyCount = IE6_ACTUAL_PROPERTY_COUNT_;
            remainingPropertyCount = IE6_REMAINING_PROPERTY_COUNT_;
        }
        else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_7) {
            realList = IE7_;
            simulatedList = IE7_SIMULATED_;
            dataset = CATEGORY_DATASET_IE7_;
            html = IE7_HTML_;
            actualPropertyCount = IE7_ACTUAL_PROPERTY_COUNT_;
            remainingPropertyCount = IE7_REMAINING_PROPERTY_COUNT_;
        }
        else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_8) {
            realList = IE8_;
            simulatedList = IE8_SIMULATED_;
            dataset = CATEGORY_DATASET_IE8_;
            html = IE8_HTML_;
            actualPropertyCount = IE8_ACTUAL_PROPERTY_COUNT_;
            remainingPropertyCount = IE8_REMAINING_PROPERTY_COUNT_;
        }
        else if (browserVersion_ == BrowserVersion.FIREFOX_3_6) {
            realList = FF3_6_;
            simulatedList = FF3_6_SIMULATED_;
            dataset = CATEGORY_DATASET_FF3_6_;
            html = FF3_6_HTML_;
            actualPropertyCount = FF3_6_ACTUAL_PROPERTY_COUNT_;
            remainingPropertyCount = FF3_6_REMAINING_PROPERTY_COUNT_;
        }
        else {
            fail("Unknown BrowserVersion " + browserVersion_);
            return;
        }

        List<String> realProperties = Arrays.asList(getValueOf(realList, name_).split(","));
        List<String> simulatedProperties = Arrays.asList(getValueOf(simulatedList, name_).split(","));
        if (realProperties.size() == 1 && realProperties.get(0).length() == 0) {
            realProperties = new ArrayList<String>();
        }
        if (simulatedProperties.size() == 1 && simulatedProperties.get(0).length() == 0) {
            simulatedProperties = new ArrayList<String>();
        }
        final List<String> originalRealProperties = new ArrayList<String>(realProperties);
        removeParentheses(realProperties);
        removeParentheses(simulatedProperties);

        final List<String> erroredProperties = new ArrayList<String>(simulatedProperties);
        erroredProperties.removeAll(realProperties);

        final List<String> implementedProperties = new ArrayList<String>(simulatedProperties);
        implementedProperties.retainAll(realProperties);

        dataset.addValue(implementedProperties.size(), "Implemented", name_);
        dataset.addValue(realProperties.size(),
            browserVersion_.getNickname().replace("FF", "Firefox ").replace("IE", "Internet Explorer "), name_);
        dataset.addValue(erroredProperties.size(), "Should not be implemented", name_);

        final List<String> remainingProperties = new ArrayList<String>(realProperties);
        remainingProperties.removeAll(implementedProperties);

        actualPropertyCount.add(realProperties.size());
        remainingPropertyCount.add(remainingProperties.size());

        if (LOG.isDebugEnabled()) {
            LOG.debug(name_ + ':' + browserVersion_.getNickname() + ':' + realProperties);
            LOG.debug("Remaining" + ':' + remainingProperties);
            LOG.debug("Error" + ':' + erroredProperties);
        }

        appendHtml(html, originalRealProperties, simulatedProperties, erroredProperties);
        if (dataset.getColumnCount() == IE7_.size()) {
            saveChart(dataset);
            html.append("<tr><td colspan='3' align='right'><b>Total Implemented: ")
                .append(actualPropertyCount.intValue() - remainingPropertyCount.intValue()).append(" / ")
                .append(actualPropertyCount.intValue()).append("</b></td></tr>");
            html.append("</table>").append('\n').append("<br>").append("Legend:").append("<br>")
                .append("<span style='color: blue'>").append("To be implemented").append("</span>").append("<br>")
                .append("<span style='color: green'>").append("Implemented").append("</span>").append("<br>")
                .append("<span style='color: red'>").append("Should not be implemented").append("</span>")
                .append("</html>");

            FileUtils.writeStringToFile(new File(getArtifactsDirectory()
                + "/properties-"
                + browserVersion_.getNickname()
                + ".html"), html.toString());
        }
    }

    private void appendHtml(final StringBuilder html, final List<String> originalRealProperties,
            final List<String> simulatedProperties, final List<String> erroredProperties) {
        if (html.length() == 0) {
            html.append("<html>").append('\n').append("<div align='center'>").append("<h2>")
            .append("HtmlUnit implemented properties and methods for " + browserVersion_.getNickname())
            .append("</h2>").append("</div>").append("<table width='100%' border='1'>");
        }
        html.append("<tr>").append('\n').append("<td rowspan='2'>").append("<a name='" + name_ + "'>").append(name_)
            .append("</a>").append("</td>").append('\n').append("<td>");
        int implementedCount = 0;
        for (int i = 0; i < originalRealProperties.size(); i++) {
            String propertyTrimmed = originalRealProperties.get(i);
            if (propertyTrimmed.endsWith("()")) {
                propertyTrimmed = propertyTrimmed.substring(0, propertyTrimmed.length() - 2);
            }
            final String color;
            if (simulatedProperties.contains(propertyTrimmed)) {
                color = "green";
                implementedCount++;
            }
            else {
                color = "blue";
            }
            html.append("<span style='color: " + color + "'>").append(originalRealProperties.get(i)).append("</span>");
            if (i < originalRealProperties.size() - 1) {
                html.append(',').append(' ');
            }
        }
        if (originalRealProperties.isEmpty()) {
            html.append("&nbsp;");
        }
        html.append("</td>").append("<td>").append(implementedCount).append('/')
            .append(originalRealProperties.size()).append("</td>").append("</tr>").append('\n');
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
            .append("<td>").append(erroredProperties.size()).append("</td>").append("</tr>").append('\n');
    }

    private String getValueOf(final List<String> list, final String name) {
        for (final String line : list) {
            if (line.substring(0, line.indexOf(':')).equals(name)) {
                return line.substring(line.indexOf(':') + 1);
            }
        }
        return null;
    }

    /**
     * To be removed once {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollectionTest#typeof()}
     * is fixed.
     */
    private void removeParentheses(final List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            final String string = list.get(i);
            if (string.endsWith("()")) {
                list.set(i, string.substring(0, string.length() - 2));
            }
        }
    }

    private void saveChart(final DefaultCategoryDataset dataset) throws IOException {
        final JFreeChart chart = ChartFactory.createBarChart(
            "HtmlUnit implemented properties and methods for " + browserVersion_.getNickname(), "Objects",
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
            new File(getArtifactsDirectory() + "/properties-" + browserVersion_.getNickname() + ".png"));
    }

    /**
     * Returns the 'artifacts' directory.
     * @return the 'artifacts' directory
     */
    public static String getArtifactsDirectory() {
        final String dirName = "./artifacts";
        final File dir = new File(dirName);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new RuntimeException("Could not create artifacts directory");
            }
        }
        return dirName;
    }
}
