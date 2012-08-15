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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.html.IEConditionalCommentExpressionEvaluator.evaluate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link IEConditionalCommentExpressionEvaluator}.
 * Due to current implementation, conditional comment expressions get evaluated only when the simulated browser is IE.
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class IEConditionalCommentExpressionEvaluatorTest extends SimpleWebTestCase {

    /**
     * Test for expression [if IE].
     */
    @Test
    @Alerts("true")
    public void IE() {
        doTest("IE");
    }

    /**
     * Test for expressions [if IE 5].
     */
    @Test
    @Alerts("false")
    public void IE_5() {
        doTest("IE 5");
    }

    /**
     * Test for expressions [if IE 6].
     */
    @Test
    @Alerts(IE6 = "true", IE = "false")
    public void IE_6() {
        doTest("IE 6");
    }

    /**
     * Test for expressions [if IE 7].
     */
    @Test
    @Alerts(IE7 = "true", IE = "false")
    public void IE_7() {
        doTest("IE 7");
    }

    /**
     * Test for expressions [if IE 8].
     */
    @Test
    @Alerts(IE8 = "true", IE = "false")
    public void IE_8() {
        doTest("IE 8");
    }

    /**
     * Test for expression [if !IE].
     */
    @Test
    @Alerts("false")
    public void notIE() {
        doTest("!IE");
    }

    /**
     * Test for expressions [if lt IE 5.5].
     */
    @Test
    @Alerts(IE = "false", FF = "true")
    public void lt_IE_5_5() {
        doTest("lt IE 5.5");
    }

    /**
     * Test for expressions [if lt IE 6].
     */
    @Test
    @Alerts("false")
    public void lt_IE_6() {
        doTest("lt IE 6");
    }

    /**
     * Test for expressions [if lt IE 7].
     */
    @Test
    @Alerts(IE6 = "true", IE = "false")
    public void lt_IE_7() {
        doTest("lt IE 7");
    }

    /**
     * Test for expressions [if lt IE 8].
     */
    @Test
    @Alerts(IE6 = "true", IE7 = "true", IE = "false")
    public void lt_IE_8() {
        doTest("lt IE 8");
    }

    /**
     * Test for expressions [if lt IE 9].
     */
    @Test
    @Alerts("true")
    public void lt_IE_9() {
        doTest("lt IE 9");
    }

    /**
     * Test for expressions [if gt IE 5.5].
     */
    @Test
    @Alerts("true")
    public void gt_IE_5_5() {
        doTest("gt IE 5.5");
    }

    /**
     * Test for expressions [if gt IE 6].
     */
    @Test
    @Alerts(IE6 = "false", IE = "true")
    public void gt_IE_6() {
        doTest("gt IE 6");
    }

    /**
     * Test for expressions [if gt IE 7].
     */
    @Test
    @Alerts(IE8 = "true", IE = "false")
    public void gt_IE_7() {
        doTest("gt IE 7");
    }

    /**
     * Test for expressions [if gt IE 8].
     */
    @Test
    @Alerts("false")
    public void gt_IE_8() {
        doTest("gt IE 8");
    }

    /**
     * Test for expressions [if gte IE 5.5].
     */
    @Test
    @Alerts("true")
    public void gte_IE_5_5() {
        doTest("gte IE 5.5");
    }

    /**
     * Test for expressions [if gte IE 6].
     */
    @Test
    @Alerts("true")
    public void gte_IE_6() {
        doTest("gte IE 6");
    }

    /**
     * Test for expressions [if gte IE 7].
     */
    @Test
    @Alerts(IE6 = "false", IE = "true")
    public void gte_IE_7() {
        doTest("gte IE 7");
    }

    /**
     * Test for expressions [if gte IE 8].
     */
    @Test
    @Alerts(IE6 = "false", IE7 = "false", IE = "true")
    public void gte_IE_8() {
        doTest("gte IE 8");
    }

    /**
     * Test for expressions [if !(IE 5)].
     */
    @Test
    @Alerts("true")
    public void parenthese_5() {
        doTest("!(IE 5)");
    }

    /**
     * Test for expressions [if !(IE 6)].
     */
    @Test
    @Alerts(IE6 = "false", IE = "true")
    public void parenthese_6() {
        doTest("!(IE 6)");
    }

    /**
     * Test for expressions [if !(IE 7)].
     */
    @Test
    @Alerts(IE7 = "false", IE = "true")
    public void parenthese_7() {
        doTest("!(IE 7)");
    }

    /**
     * Test for expressions [if !(IE 8)].
     */
    @Test
    @Alerts(IE8 = "false", IE = "true")
    public void parenthese_8() {
        doTest("!(IE 8)");
    }

    /**
     * Test for expressions [(gt IE 6)&(lt IE 8)].
     */
    @Test
    @Alerts(IE7 = "true", IE = "false")
    public void and() {
        doTest("(gt IE 6)&(lt IE 8)");
    }

    /**
     * Test for expressions [if (IE 6)|(IE 7)].
     */
    @Test
    @Alerts(IE6 = "true", IE7 = "true", IE = "false")
    public void or() {
        doTest("(IE 6)|(IE 7)");
    }

    /**
     * Test for expressions [if true].
     */
    @Test
    @Alerts("true")
    public void true_() {
        doTest("true");
    }

    /**
     * Test for expressions [if false].
     */
    @Test
    @Alerts("false")
    public void false_() {
        doTest("false");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     */
    @Test
    @Alerts("false")
    public void mso_1() {
        doTest("mso 9");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     */
    @Test
    @Alerts("false")
    public void mso_2() {
        doTest("gte mso 9");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     */
    @Test
    @Alerts("true")
    public void mso_3() {
        doTest("lt mso 9");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     */
    @Test
    @Alerts("true")
    public void mso_4() {
        doTest("lt mso 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     */
    @Test
    @Alerts("false")
    public void unknown_1() {
        doTest("foo 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     */
    @Test
    @Alerts("false")
    public void unknown_2() {
        doTest("gte foo 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     */
    @Test
    @Alerts("false")
    public void unknown_3() {
        doTest("gt foo 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     */
    @Test
    @Alerts("true")
    public void unknown_4() {
        doTest("lt foo 1");
    }

    private void doTest(final String expression) {
        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.isFirefox()) {
            return;
        }
        final String expected = getExpectedAlerts()[0];
        Assert.assertEquals(expression + " for " + browserVersion.getNickname(),
                expected, Boolean.toString(evaluate(expression, browserVersion)));
    }
}
