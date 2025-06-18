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

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlWeekInput}.
 *
 * @author Anton Demydenko
 * @author Ronald Brill
 */
public class HtmlWeekInput2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false", "true", "true", "true", "true"})
    @HtmlUnitNYI(FF = {"true", "true", "true", "true", "true", "true"},
            FF_ESR = {"true", "true", "true", "true", "true", "true"})
    public void minValidation() throws Exception {
        final String htmlContent = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='week' id='first' min='2018-W10'>\n"
                + "  <input type='week' id='second'>\n"
                + "  <input type='week' id='third' min='foo'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlWeekInput first = (HtmlWeekInput) page.getElementById("first");
        final HtmlWeekInput second = (HtmlWeekInput) page.getElementById("second");
        final HtmlWeekInput third = (HtmlWeekInput) page.getElementById("third");

        // empty
        assertEquals(getExpectedAlerts()[0], Boolean.toString(first.isValid()));
        // lesser
        first.setValue("2018-W09");
        assertEquals(getExpectedAlerts()[1], Boolean.toString(first.isValid()));
        // equal
        first.setValue("2018-W10");
        assertEquals(getExpectedAlerts()[2], Boolean.toString(first.isValid()));
        // bigger
        first.setValue("2018-W11");
        assertEquals(getExpectedAlerts()[3], Boolean.toString(first.isValid()));

        second.setValue("2018-W10");
        assertEquals(getExpectedAlerts()[4], Boolean.toString(second.isValid()));
        third.setValue("2018-W10");
        assertEquals(getExpectedAlerts()[5], Boolean.toString(third.isValid()));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "false", "true", "true"})
    @HtmlUnitNYI(FF = {"true", "true", "true", "true", "true", "true"},
            FF_ESR = {"true", "true", "true", "true", "true", "true"})
    public void naxValidation() throws Exception {
        final String htmlContent = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='week' id='first' max='2018-W10'>\n"
                + "  <input type='week' id='second'>\n"
                + "  <input type='week' id='third' max='foo'>\n"
                + "</form>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlWeekInput first = (HtmlWeekInput) page.getElementById("first");
        final HtmlWeekInput second = (HtmlWeekInput) page.getElementById("second");
        final HtmlWeekInput third = (HtmlWeekInput) page.getElementById("third");

        // empty
        assertEquals(getExpectedAlerts()[0], Boolean.toString(first.isValid()));
        // lesser
        first.setValue("2018-W09");
        assertEquals(getExpectedAlerts()[1], Boolean.toString(first.isValid()));
        // equal
        first.setValue("2018-W10");
        assertEquals(getExpectedAlerts()[2], Boolean.toString(first.isValid()));
        // bigger
        first.setValue("2018-W11");
        assertEquals(getExpectedAlerts()[3], Boolean.toString(first.isValid()));

        second.setValue("2018-W10");
        assertEquals(getExpectedAlerts()[4], Boolean.toString(second.isValid()));
        third.setValue("2018-W10");
        assertEquals(getExpectedAlerts()[5], Boolean.toString(third.isValid()));
    }
}
