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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

/**
 * Tests against external websites, this should be done once every while.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class ExternalTest {

    private static final DateFormat TEAM_CITY_FORMAT_ = new SimpleDateFormat("dd MMM yy HH:mm");

    /**
     * Tests that pom dependencies are the latest.
     *
     * Currently it is configured to run every week.
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
        }
    }

    private static void assertVersion(final String groupId, final String artifactId, final String version)
            throws Exception {
        String latestVersion = null;
        try (final WebClient webClient = getWebClient()) {
            final HtmlPage page = webClient.getPage("https://repo1.maven.org/maven2/" + groupId.replace('.', '/') + '/'
                    + artifactId.replace('.', '/'));
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
        assertEquals(groupId + ":" + artifactId, latestVersion, version);
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
        final WebClient webClient = new WebClient();
        return webClient;
    }

    /**
     * Returns if now we are in different week than the last finished build one.
     */
    private static boolean isDifferentWeek() throws Exception {
        try (final WebClient webClient = getWebClient()) {
            HtmlPage page = webClient.getPage("https://ci.canoo.com/teamcity/viewLog.html"
                    + "?buildTypeId=HtmlUnit_FastBuild&buildId=lastFinished");
            page = page.getAnchorByText("Log in as guest").click();
            final HtmlTable table = page.getFirstByXPath("//table[@class='statusTable']");
            final HtmlTableCell cell = table.getRow(1).getCell(3);
            final String string = cell.asText().substring("Subversion on ".length());
            final Calendar buildCalendar = Calendar.getInstance();
            buildCalendar.setTime(TEAM_CITY_FORMAT_.parse(string));
            return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) != buildCalendar.get(Calendar.WEEK_OF_YEAR);
        }
    }
}
