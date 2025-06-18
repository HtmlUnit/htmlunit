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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for HtmlUnit's support of IE conditional comments.
 * Due to current implementation, conditional comment expressions get evaluated only when the simulated browser is IE.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class IEConditionalCommentExpressionEvaluatorTest extends WebDriverTestCase {

    /**
     * Test for expression {@code if IE}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void IE() throws Exception {
        doTest("IE");
    }

    /**
     * Test for expression {@code if IE 5}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void IE_5() throws Exception {
        doTest("IE 5");
    }

    /**
     * Test for expression {@code if IE 6}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void IE_6() throws Exception {
        doTest("IE 6");
    }

    /**
     * Test for expression {@code if IE 7}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void IE_7() throws Exception {
        doTest("IE 7");
    }

    /**
     * Test for expression {@code if IE 8}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void IE_8() throws Exception {
        doTest("IE 8");
    }

    /**
     * Test for expression {@code if !IE}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void notIE() throws Exception {
        doTest("!IE");
    }

    /**
     * Test for expression {@code if lt IE 5.5}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void lt_IE_5_5() throws Exception {
        doTest("lt IE 5.5");
    }

    /**
     * Test for expression {@code if lt IE 6}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void lt_IE_6() throws Exception {
        doTest("lt IE 6");
    }

    /**
     * Test for expression {@code if lt IE 7}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void lt_IE_7() throws Exception {
        doTest("lt IE 7");
    }

    /**
     * Test for expressions {@code if lt IE 8}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void lt_IE_8() throws Exception {
        doTest("lt IE 8");
    }

    /**
     * Test for expression {@code if lt IE 9}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void lt_IE_9() throws Exception {
        doTest("lt IE 9");
    }

    /**
     * Test for expression {@code if gt IE 5.5}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void gt_IE_5_5() throws Exception {
        doTest("gt IE 5.5");
    }

    /**
     * Test for expression {@code if gt IE 6}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void gt_IE_6() throws Exception {
        doTest("gt IE 6");
    }

    /**
     * Test for expression {@code if gt IE 7}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void gt_IE_7() throws Exception {
        doTest("gt IE 7");
    }

    /**
     * Test for expression {@code if gt IE 8}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void gt_IE_8() throws Exception {
        doTest("gt IE 8");
    }

    /**
     * Test for expression {@code if gte IE 5.5}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void gte_IE_5_5() throws Exception {
        doTest("gte IE 5.5");
    }

    /**
     * Test for expression {@code if gte IE 6}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void gte_IE_6() throws Exception {
        doTest("gte IE 6");
    }

    /**
     * Test for expressions {@code if gte IE 7}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void gte_IE_7() throws Exception {
        doTest("gte IE 7");
    }

    /**
     * Test for expressions {@code if gte IE 8}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void gte_IE_8() throws Exception {
        doTest("gte IE 8");
    }

    /**
     * Test for expressions {@code if !(IE 5)}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void parenthese_5() throws Exception {
        doTest("!(IE 5)");
    }

    /**
     * Test for expressions {@code if !(IE 6)}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void parenthese_6() throws Exception {
        doTest("!(IE 6)");
    }

    /**
     * Test for expressions {@code if !(IE 7)}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void parenthese_7() throws Exception {
        doTest("!(IE 7)");
    }

    /**
     * Test for expressions {@code if !(IE 8)}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void parenthese_8() throws Exception {
        doTest("!(IE 8)");
    }

    /**
     * Test for expressions {@code (gt IE 6)&(lt IE 8)}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void and() throws Exception {
        doTest("(gt IE 6)&(lt IE 8)");
    }

    /**
     * Test for expressions {@code if (IE 6)|(IE 7)}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void or() throws Exception {
        doTest("(IE 6)|(IE 7)");
    }

    /**
     * Test for expressions {@code if true}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void true_() throws Exception {
        doTest("true");
    }

    /**
     * Test for expressions [if false].
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void false_() throws Exception {
        doTest("false");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void mso_1() throws Exception {
        doTest("mso 9");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void mso_2() throws Exception {
        doTest("gte mso 9");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void mso_3() throws Exception {
        doTest("lt mso 9");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void mso_4() throws Exception {
        doTest("lt mso 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void unknown_1() throws Exception {
        doTest("foo 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void unknown_2() throws Exception {
        doTest("gte foo 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void unknown_3() throws Exception {
        doTest("gt foo 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void unknown_4() throws Exception {
        doTest("lt foo 1");
    }

    private void doTest(final String expression) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<!--[if " + expression + "]><script>alert('cond');</script><![endif]-->\n"
            + "<script>alert('done');</script>\n"
            + "</head><body></body></html>";
        loadPageWithAlerts2(html);
    }
}
