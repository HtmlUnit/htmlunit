/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Tests against external web sites, this should be done once every while.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ExternalTest {

    static String SONATYPE_SNAPSHOT_REPO_URL_ = "https://oss.sonatype.org/content/repositories/snapshots/";
    static String MAVEN_REPO_URL_ = "https://repo1.maven.org/maven2/";

    /** Chrome driver. */
    static String CHROME_DRIVER_ = "101.0.4951.41";
    static String CHROME_DRIVER_URL_ = "https://chromedriver.chromium.org/downloads";

    static String EDGE_DRIVER_ = "101.0.1210.53";
    static String EDGE_DRIVER_URL_ = "https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/";

    /** Gecko driver. */
    static String GECKO_DRIVER_ = "0.31.0";
    static String GECKO_DRIVER_URL_ = "https://github.com/mozilla/geckodriver/releases/latest";

    /** IE driver. */
    static String IE_DRIVER_ = "3.150.1.0";

    /**
     * Tests the current environment matches the expected setup.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEnvironment() throws Exception {
        assertEquals("en_US", Locale.getDefault().toString());
    }

    /**
     * Tests that POM dependencies are the latest.
     *
     * Currently it is configured to check every week.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void pom() throws Exception {
        final Map<String, String> properties = new HashMap<>();
        final List<String> lines = FileUtils.readLines(new File("pom.xml"), ISO_8859_1);

        final List<String> wrongVersions = new LinkedList<String>();
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            if (line.trim().equals("<properties>")) {
                processProperties(lines, i + 1, properties);
            }
            if (line.contains("artifactId") && !line.contains(">htmlunit<")) {
                final String artifactId = getValue(line);
                final String groupId = getValue(lines.get(i - 1));
                if (!lines.get(i + 1).contains("</exclusion>")) {
                    String version = getValue(lines.get(i + 1));
                    if (version.startsWith("${")) {
                        version = properties.get(version.substring(2, version.length() - 1));
                    }
                    try {
                        assertVersion(groupId, artifactId, version);
                    }
                    catch (final AssertionError e) {
                        wrongVersions.add(e.getMessage());
                    }
                }
            }
        }

        if (wrongVersions.size() > 0) {
            Assert.fail(String.join("\n ", wrongVersions));
        }

        assertVersion("org.sonatype.oss", "oss-parent", "9");
    }

    private static void processProperties(final List<String> lines, int i, final Map<String, String> map) {
        for ( ; i < lines.size(); i++) {
            final String line = lines.get(i).trim();
            if (StringUtils.isNotBlank(line) && !line.startsWith("<!--")) {
                if ("</properties>".equals(line)) {
                    break;
                }
                final String name = line.substring(line.indexOf('<') + 1, line.indexOf('>'));
                map.put(name, getValue(line));
            }
        }
    }

    /**
     * Tests that we use the latest chrome driver.
     * @throws Exception if an error occurs
     */
    @Test
    public void assertChromeDriver() throws Exception {
        try (WebClient webClient = buildWebClient()) {
            final HtmlPage page = webClient.getPage(CHROME_DRIVER_URL_);
            String content = page.asNormalizedText();
            content = content.substring(content.indexOf("Current Releases"));
            content = content.replace("\r\n", "");
            String version = "0.0.0.0";
            final Pattern regex =
                    Pattern.compile("If you are using Chrome version "
                            + BrowserVersion.CHROME.getBrowserVersionNumeric()
                            + ", please download ChromeDriver (\\d*\\.\\d*\\.\\d*\\.\\d*)");
            final Matcher matcher = regex.matcher(content);
            while (matcher.find()) {
                if (version.compareTo(matcher.group(1)) < 0) {
                    version = matcher.group(1);
                    break;
                }
            }
            assertEquals("Chrome Driver", version, CHROME_DRIVER_);
        }
    }

    /**
     * Tests that we use the latest edge driver.
     * @throws Exception if an error occurs
     */
    @Test
    public void assertEdgeDriver() throws Exception {
        try (WebClient webClient = buildWebClient()) {
            final HtmlPage page = webClient.getPage(EDGE_DRIVER_URL_);
            String content = page.asNormalizedText();
            content = content.substring(content.indexOf("Recent versions"));
            content = content.replace("\r\n", "");

            String version = "0.0.0.0";
            final Pattern regex =
                    Pattern.compile("Version: ("
                                + BrowserVersion.EDGE.getBrowserVersionNumeric()
                                + "\\.\\d*\\.\\d*\\.\\d*):\\sx86\\s\\|\\sx64");
            final Matcher matcher = regex.matcher(content);
            while (matcher.find()) {
                if (version.compareTo(matcher.group(1)) < 0) {
                    version = matcher.group(1);
                    break;
                }
            }
            assertEquals("Edge Driver", version, EDGE_DRIVER_);
        }
    }

    /**
     * Tests that we use the latest gecko driver.
     * @throws Exception if an error occurs
     */
    @Test
    public void assertGeckoDriver() throws Exception {
        try (WebClient webClient = buildWebClient()) {
            try {
                final HtmlPage page = webClient.getPage(GECKO_DRIVER_URL_);
                final DomNodeList<DomNode> divs = page.querySelectorAll("li.breadcrumb-item-selected");
                assertEquals("Gecko Driver", divs.get(0).asNormalizedText(), "v" + GECKO_DRIVER_);
            }
            catch (final FailingHttpStatusCodeException e) {
                // ignore
            }
        }
    }

    /**
     * Tests that the deployed snapshot is not more than two weeks old.
     *
     * Currently it is configured to check every week.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void snapshot() throws Exception {
        final List<String> lines = FileUtils.readLines(new File("pom.xml"), ISO_8859_1);
        String version = null;
        for (int i = 0; i < lines.size(); i++) {
            if ("<artifactId>htmlunit</artifactId>".equals(lines.get(i).trim())) {
                version = getValue(lines.get(i + 1));
                break;
            }
        }
        assertNotNull(version);
        if (version.contains("SNAPSHOT")) {
            try (WebClient webClient = buildWebClient()) {
                final XmlPage page = webClient.getPage(SONATYPE_SNAPSHOT_REPO_URL_
                                        + "net/sourceforge/htmlunit/htmlunit/" + version + "/maven-metadata.xml");
                final String timestamp = page.getElementsByTagName("timestamp").get(0).getTextContent();
                final DateFormat format = new SimpleDateFormat("yyyyMMdd.HHmmss", Locale.ROOT);
                final long snapshotMillis = format.parse(timestamp).getTime();
                final long nowMillis = System.currentTimeMillis();
                final long days = TimeUnit.MILLISECONDS.toDays(nowMillis - snapshotMillis);
                assertTrue("Snapshot not deployed for " + days + " days", days < 14);
            }
        }
    }

    private static void assertVersion(final String groupId, final String artifactId, final String version)
            throws Exception {
        String latestVersion = null;
        String url = MAVEN_REPO_URL_
                        + groupId.replace('.', '/') + '/'
                        + artifactId.replace('.', '/');
        if (!url.endsWith("/")) {
            url += "/";
        }
        try (WebClient webClient = buildWebClient()) {
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

            try {
                final HtmlPage page = webClient.getPage(url);
                for (final HtmlAnchor anchor : page.getAnchors()) {
                    String itemVersion = anchor.getTextContent();
                    itemVersion = itemVersion.substring(0, itemVersion.length() - 1);
                    if (!isIgnored(groupId, artifactId, itemVersion)) {
                        if (isVersionAfter(itemVersion, latestVersion)) {
                            latestVersion = itemVersion;
                        }
                    }
                }
            }
            catch (final UnknownHostException e) {
                // ignore because our ci machine sometimes fails
            }
        }
        if (!version.endsWith("-SNAPSHOT")
                || !isVersionAfter(version.substring(0, version.length() - "-SNAPSHOT".length()), latestVersion)) {
            assertEquals(groupId + ":" + artifactId, latestVersion, version);
        }
    }

    private static boolean isVersionAfter(final String version1, final String version2) {
        if (version2 == null) {
            return true;
        }
        final String[] values1 = version1.split("\\.");
        final String[] values2 = version2.split("\\.");
        for (int i = 0; i < values1.length; i++) {
            if (values1[i].startsWith("v")) {
                values1[i] = values1[i].substring(1);
            }
            try {
                Integer.parseInt(values1[i]);
            }
            catch (final NumberFormatException e) {
                return false;
            }
        }
        for (int i = 0; i < values2.length; i++) {
            if (values2[i].startsWith("v")) {
                values2[i] = values2[i].substring(1);
            }
            try {
                Integer.parseInt(values2[i]);
            }
            catch (final NumberFormatException e) {
                return true;
            }
        }
        for (int i = 0; i < values1.length; i++) {
            if (i == values2.length) {
                return true;
            }
            final int i1 = Integer.parseInt(values1[i]);
            final int i2 = Integer.parseInt(values2[i]);
            if (i1 < i2) {
                return false;
            }
            if (i1 > i2) {
                return true;
            }
        }
        return false;
    }

    private static boolean isIgnored(@SuppressWarnings("unused") final String groupId,
            @SuppressWarnings("unused") final String artifactId, @SuppressWarnings("unused") final String version) {
        if (groupId.startsWith("org.eclipse.jetty")
                && (version.startsWith("11.") || version.startsWith("10."))) {
            return true;
        }

        if ("org.seleniumhq.selenium".equals(groupId) && (version.startsWith("4."))) {
            return true;
        }

        if ("commons-codec".equals(groupId)
                && "commons-codec".equals(artifactId)
                && "20041127.091804".equals(version)) {
            return true;
        }

        if ("org.seleniumhq.selenium".equals(groupId)
                && "htmlunit-driver".equals(artifactId)
                && (version.startsWith("3."))) {
            return true;
        }


        // there is a serious bug
        // https://issues.apache.org/jira/browse/IO-744
        if ("commons-io".equals(artifactId) && (version.startsWith("2.11.0"))) {
            return true;
        }

        // version 10 requires JDK 11
        if ("checkstyle".equals(artifactId) && (version.startsWith("10."))) {
            return true;
        }
        // version 3.11.x seem to requires JDK11
        if ("maven-site-plugin".equals(artifactId)
                && (version.startsWith("3.11.") || version.startsWith("3.12."))) {
            return true;
        }

        // really old common versions
        if ("commons-io".equals(artifactId) && (version.startsWith("2003"))) {
            return true;
        }
        if ("commons-net".equals(artifactId) && (version.startsWith("2003"))) {
            return true;
        }

        return false;
    }

    private static String getValue(final String line) {
        return line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'));
    }

    private static WebClient buildWebClient() {
        final WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        return webClient;
    }
}
