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

    @Test
    @Alerts(DEFAULT = "default",
            CHROME = "chrome",
            EDGE = "edge",
            FF = "ff",
            FF_ESR = "ff esr")
    /**
     * @throws Exception if something goes wrong
     */
    public void obsoleteDefaultBecauseAllBrowserExpectationsDefinedIndividually() throws Exception {
        testFail("Obsolete DEFAULT because all browser expectations defined individually",
                "obsoleteDefaultBecauseAllBrowserExpectationsDefinedIndividually");
    }

    @Test
    @Alerts(DEFAULT = "redundant",
            CHROME = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantAlertChrome() throws Exception {
        testFail("Redundant @Alerts for Chrome in AnnotationUtilsTest.redundantAlertChrome()",
                "redundantAlertChrome");
    }

    @Test
    @Alerts(DEFAULT = "redundant",
            EDGE = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantAlertEdge() throws Exception {
        testFail("Redundant @Alerts for Edge in AnnotationUtilsTest.redundantAlertEdge()",
                "redundantAlertEdge");
    }

    @Test
    @Alerts(DEFAULT = "redundant",
            FF = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantAlertFf() throws Exception {
        testFail("Redundant @Alerts for FF in AnnotationUtilsTest.redundantAlertFf()",
                "redundantAlertFf");
    }

    @Test
    @Alerts(DEFAULT = "redundant",
            FF_ESR = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantAlertFfEsr() throws Exception {
        testFail("Redundant @Alerts for FF-ESR in AnnotationUtilsTest.redundantAlertFfEsr()",
                "redundantAlertFfEsr");
    }

    @Test
    @Alerts("redundant")
    @HtmlUnitNYI("redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantHtmlUnitNYI() throws Exception {
        testFail("Redundant @HtmlUnitNYI for DEFAULT in AnnotationUtilsTest.redundantHtmlUnitNYI()",
                "redundantHtmlUnitNYI");
    }

    @Test
    @Alerts("redundant")
    @HtmlUnitNYI(CHROME = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantHtmlUnitNYIChrome() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Chrome in AnnotationUtilsTest.redundantHtmlUnitNYIChrome()",
                "redundantHtmlUnitNYIChrome");
    }

    @Test
    @Alerts("redundant")
    @HtmlUnitNYI(EDGE = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantHtmlUnitNYIEdge() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Edge in AnnotationUtilsTest.redundantHtmlUnitNYIEdge()",
                "redundantHtmlUnitNYIEdge");
    }

    @Test
    @Alerts("redundant")
    @HtmlUnitNYI(FF = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantHtmlUnitNYIFf() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF in AnnotationUtilsTest.redundantHtmlUnitNYIFf()",
                "redundantHtmlUnitNYIFf");
    }

    @Test
    @Alerts("redundant")
    @HtmlUnitNYI(FF_ESR = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantHtmlUnitNYIFfEsr() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF-ESR in AnnotationUtilsTest.redundantHtmlUnitNYIFfEsr()",
                "redundantHtmlUnitNYIFfEsr");
    }

    @Test
    @Alerts(DEFAULT = "",
            CHROME = "redundant")
    @HtmlUnitNYI(CHROME = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantChromeHtmlUnitNYIChrome() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Chrome in AnnotationUtilsTest.redundantChromeHtmlUnitNYIChrome()",
                "redundantChromeHtmlUnitNYIChrome");
    }

    @Test
    @Alerts(DEFAULT = "",
            EDGE = "redundant")
    @HtmlUnitNYI(EDGE = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantEdgeHtmlUnitNYIEdge() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Edge in AnnotationUtilsTest.redundantEdgeHtmlUnitNYIEdge()",
                "redundantEdgeHtmlUnitNYIEdge");
    }

    @Test
    @Alerts(DEFAULT = "",
            FF = "redundant")
    @HtmlUnitNYI(FF = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantFfHtmlUnitNYIFf() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF in AnnotationUtilsTest.redundantFfHtmlUnitNYIFf()",
                "redundantFfHtmlUnitNYIFf");
    }

    @Test
    @Alerts(DEFAULT = "",
            FF_ESR = "redundant")
    @HtmlUnitNYI(FF_ESR = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantFfEsrHtmlUnitNYIFfEsr() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF-ESR in AnnotationUtilsTest.redundantFfEsrHtmlUnitNYIFfEsr()",
                "redundantFfEsrHtmlUnitNYIFfEsr");
    }

    @Test
    @Alerts(CHROME = "redundant")
    @HtmlUnitNYI(CHROME = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantChrome2HtmlUnitNYIChrome() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Chrome in AnnotationUtilsTest.redundantChrome2HtmlUnitNYIChrome()",
                "redundantChrome2HtmlUnitNYIChrome");
    }

    @Test
    @Alerts(EDGE = "redundant")
    @HtmlUnitNYI(EDGE = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantEdge2HtmlUnitNYIEdge() throws Exception {
        testFail("Redundant @HtmlUnitNYI for Edge in AnnotationUtilsTest.redundantEdge2HtmlUnitNYIEdge()",
                "redundantEdge2HtmlUnitNYIEdge");
    }

    @Test
    @Alerts(FF = "redundant")
    @HtmlUnitNYI(FF = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
    public void redundantFf2HtmlUnitNYIFf() throws Exception {
        testFail("Redundant @HtmlUnitNYI for FF in AnnotationUtilsTest.redundantFf2HtmlUnitNYIFf()",
                "redundantFf2HtmlUnitNYIFf");
    }

    @Test
    @Alerts(FF_ESR = "redundant")
    @HtmlUnitNYI(FF_ESR = "redundant")
    /**
     * @throws Exception if something goes wrong
     */
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
