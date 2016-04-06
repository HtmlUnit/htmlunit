/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserVersion.BEST_SUPPORTED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Tests against external websites, this should be done once every while.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ExternalTest {

    private static final DateFormat TEAM_CITY_FORMAT_ = new SimpleDateFormat("dd MMM yy HH:mm", Locale.ROOT);

    /**
     * Tests that POM dependencies are the latest.
     *
     * Currently it is configured to check every week.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void pom() throws Exception {
        if (isDifferentWeek()) {
            final List<String> lines = FileUtils.readLines(new File("pom.xml"));
            for (int i = 0; i < lines.size(); i++) {
                final String line = lines.get(i);
                if (line.contains("artifactId") && !line.contains(">htmlunit<")) {
                    final String artifactId = getValue(line);
                    final String groupId = getValue(lines.get(i - 1));
                    if (!lines.get(i + 1).contains("</exclusion>")) {
                        final String version = getValue(lines.get(i + 1));
                        assertVersion(groupId, artifactId, version);
                    }
                }
            }
            assertVersion("org.sonatype.oss", "oss-parent", "9");
            assertChromeDriver("2.21");
        }
    }

    private static void assertChromeDriver(final String version) throws Exception {
        try (final WebClient webClient = getWebClient()) {
            final AbstractPage page = webClient.getPage("http://chromedriver.storage.googleapis.com/LATEST_RELEASE");
            final String pageContent = page.getWebResponse().getContentAsString().trim();
            assertEquals("Chrome Driver", pageContent, version);
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
        if (isDifferentWeek()) {
            final List<String> lines = FileUtils.readLines(new File("pom.xml"));
            String version = null;
            for (int i = 0; i < lines.size(); i++) {
                if ("<artifactId>htmlunit</artifactId>".equals(lines.get(i).trim())) {
                    version = getValue(lines.get(i + 1));
                    break;
                }
            }
            assertNotNull(version);
            if (version.contains("SNAPSHOT")) {
                try (final WebClient webClient = getWebClient()) {
                    final XmlPage page = webClient.getPage("https://oss.sonatype.org/content/repositories/snapshots/"
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
    }

    private static void assertVersion(final String groupId, final String artifactId, final String version)
            throws Exception {
        String latestVersion = null;
        String url = "https://repo1.maven.org/maven2/"
                        + groupId.replace('.', '/') + '/'
                        + artifactId.replace('.', '/');
        if (!url.endsWith("/")) {
            url += "/";
        }
        try (final WebClient webClient = getWebClient()) {
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
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
            int i1 = Integer.parseInt(values1[i]);
            int i2 = Integer.parseInt(values2[i]);
            if (i1 > 2000 && i2 < 2000) {
                i1 = 0;
            }
            if (i2 > 2000 && i1 < 2000) {
                i2 = 0;
            }
            if (i1 < i2) {
                return false;
            }
            if (i1 > i2) {
                return true;
            }
        }
        return false;
    }

    private static boolean isIgnored(final String groupId, final String artifactId, final String version) {
        // Needs Java 8
        return groupId.startsWith("org.eclipse.jetty") && version.startsWith("9.3.");
    }

    private static String getValue(final String line) {
        return line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'));
    }

    private static WebClient getWebClient() {
        return new WebClient(BEST_SUPPORTED);
    }

    /**
     * Returns if now we are in different week than the last finished build one.
     */
    private static boolean isDifferentWeek() throws Exception {
        try (final WebClient webClient = getWebClient()) {
            HtmlPage page = webClient.getPage("https://ci.canoo.com/teamcity/viewLog.html"
                    + "?buildTypeId=HtmlUnit_FastBuild&buildId=lastSuccessful");
            page = page.getAnchorByText("Log in as guest").click();
            webClient.waitForBackgroundJavaScript(1000);
            final HtmlTable table = page.getFirstByXPath("//table[@class='statusTable']");
            assumeNotNull(page.asXml(), table);
            final HtmlTableCell cell = table.getRow(1).getCell(3);
            final String triggerText = cell.asText();

            final String marker = " on ";
            final int start = triggerText.indexOf(marker);
            final String triggerDate = triggerText.substring(start + marker.length());

            final Calendar buildCalendar = Calendar.getInstance(Locale.ROOT);
            buildCalendar.setTime(TEAM_CITY_FORMAT_.parse(triggerDate));
            return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) != buildCalendar.get(Calendar.WEEK_OF_YEAR);
        }
    }
}
