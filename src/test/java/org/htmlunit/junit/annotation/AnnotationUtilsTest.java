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
package org.htmlunit.junit.annotation;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link AnnotationUtils}.
 *
 * @author Ronald Brill
 */
public class AnnotationUtilsTest {

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "default",
            CHROME = "chrome",
            EDGE = "edge",
            FF = "ff",
            FF_ESR = "ff esr")
    public void obsoleteDefaultBecauseAllBrowserExpectationsDefinedIndividually() throws Exception {
        testFail("Obsolete DEFAULT because all browser expectations defined individually",
                "obsoleteDefaultBecauseAllBrowserExpectationsDefinedIndividually");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "redundant",
            CHROME = "redundant")
    public void redundantAlertChrome() throws Exception {
        testFail("Redundant @Alerts for Chrome in AnnotationUtilsTest.redundantAlertChrome()",
                "redundantAlertChrome");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "redundant",
            EDGE = "redundant")
    public void redundantAlertEdge() throws Exception {
        testFail("Redundant @Alerts for Edge in AnnotationUtilsTest.redundantAlertEdge()",
                "redundantAlertEdge");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "redundant",
            FF = "redundant")
    public void redundantAlertFf() throws Exception {
        testFail("Redundant @Alerts for FF in AnnotationUtilsTest.redundantAlertFf()",
                "redundantAlertFf");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "redundant",
            FF_ESR = "redundant")
    public void redundantAlertFfEsr() throws Exception {
        testFail("Redundant @Alerts for FF-ESR in AnnotationUtilsTest.redundantAlertFfEsr()",
                "redundantAlertFfEsr");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("redundant")
    @HtmlUnitNYI("redundant")
    public void redundantHtmlUnitNYI() throws Exception {
        testFail("Redundant @HtmlUnitNYI for DEFAULT in AnnotationUtilsTest.redundantHtmlUnitNYI()",
                "redundantHtmlUnitNYI");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("redundant")
    @HtmlUnitNYI(CHROME = "redundant")
    public void redundantHtmlUnitNYIChrome() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Chrome in AnnotationUtilsTest.redundantHtmlUnitNYIChrome()",
                "redundantHtmlUnitNYIChrome");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("redundant")
    @HtmlUnitNYI(EDGE = "redundant")
    public void redundantHtmlUnitNYIEdge() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Edge in AnnotationUtilsTest.redundantHtmlUnitNYIEdge()",
                "redundantHtmlUnitNYIEdge");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("redundant")
    @HtmlUnitNYI(FF = "redundant")
    public void redundantHtmlUnitNYIFf() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF in AnnotationUtilsTest.redundantHtmlUnitNYIFf()",
                "redundantHtmlUnitNYIFf");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("redundant")
    @HtmlUnitNYI(FF_ESR = "redundant")
    public void redundantHtmlUnitNYIFfEsr() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF-ESR in AnnotationUtilsTest.redundantHtmlUnitNYIFfEsr()",
                "redundantHtmlUnitNYIFfEsr");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "",
            CHROME = "redundant")
    @HtmlUnitNYI(CHROME = "redundant")
    public void redundantChromeHtmlUnitNYIChrome() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Chrome in AnnotationUtilsTest.redundantChromeHtmlUnitNYIChrome()",
                "redundantChromeHtmlUnitNYIChrome");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "",
            EDGE = "redundant")
    @HtmlUnitNYI(EDGE = "redundant")
    public void redundantEdgeHtmlUnitNYIEdge() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Edge in AnnotationUtilsTest.redundantEdgeHtmlUnitNYIEdge()",
                "redundantEdgeHtmlUnitNYIEdge");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "",
            FF = "redundant")
    @HtmlUnitNYI(FF = "redundant")
    public void redundantFfHtmlUnitNYIFf() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF in AnnotationUtilsTest.redundantFfHtmlUnitNYIFf()",
                "redundantFfHtmlUnitNYIFf");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "",
            FF_ESR = "redundant")
    @HtmlUnitNYI(FF_ESR = "redundant")
    public void redundantFfEsrHtmlUnitNYIFfEsr() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF-ESR in AnnotationUtilsTest.redundantFfEsrHtmlUnitNYIFfEsr()",
                "redundantFfEsrHtmlUnitNYIFfEsr");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(CHROME = "redundant")
    @HtmlUnitNYI(CHROME = "redundant")
    public void redundantChrome2HtmlUnitNYIChrome() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Chrome in AnnotationUtilsTest.redundantChrome2HtmlUnitNYIChrome()",
                "redundantChrome2HtmlUnitNYIChrome");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(EDGE = "redundant")
    @HtmlUnitNYI(EDGE = "redundant")
    public void redundantEdge2HtmlUnitNYIEdge() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Edge in AnnotationUtilsTest.redundantEdge2HtmlUnitNYIEdge()",
                "redundantEdge2HtmlUnitNYIEdge");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(FF = "redundant")
    @HtmlUnitNYI(FF = "redundant")
    public void redundantFf2HtmlUnitNYIFf() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF in AnnotationUtilsTest.redundantFf2HtmlUnitNYIFf()",
                "redundantFf2HtmlUnitNYIFf");
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(FF_ESR = "redundant")
    @HtmlUnitNYI(FF_ESR = "redundant")
    public void redundantFfEsr2HtmlUnitNYIFfEsr() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF-ESR in AnnotationUtilsTest.redundantFfEsr2HtmlUnitNYIFfEsr()",
                "redundantFfEsr2HtmlUnitNYIFfEsr");
    }

    private void testFail(final String expectedMsg, final String methodName) throws Exception {
        final Method method = getClass().getMethod(methodName, (Class[]) null);

        try {
            AnnotationUtils.assertAlerts(method);
            Assert.fail("AssertionError expected");
        }
        catch (final AssertionError e) {
            Assert.assertEquals(expectedMsg, e.getMessage());
        }
    }
}
