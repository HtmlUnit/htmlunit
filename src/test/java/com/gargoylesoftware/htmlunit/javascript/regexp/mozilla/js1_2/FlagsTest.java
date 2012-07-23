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
package com.gargoylesoftware.htmlunit.javascript.regexp.mozilla.js1_2;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/flags.js'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class FlagsTest extends WebDriverTestCase {

    /**
     * Tests 'aBCdEfGHijKLmno'.match(/fghijk/i).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("fGHijK")
    public void test1() throws Exception {
        test("'aBCdEfGHijKLmno'.match(/fghijk/i)");
    }

    /**
     * Tests 'aBCdEfGHijKLmno'.match(new RegExp("fghijk","i")).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("fGHijK")
    public void test2() throws Exception {
        test("'aBCdEfGHijKLmno'.match(new RegExp(\"fghijk\",\"i\"))");
    }

    /**
     * Tests 'xa xb xc xd xe xf'.match(/x./g).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,xb,xc,xd,xe,xf")
    public void test3() throws Exception {
        test("'xa xb xc xd xe xf'.match(/x./g)");
    }

    /**
     * Tests 'xa xb xc xd xe xf'.match(new RegExp('x.','g')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,xb,xc,xd,xe,xf")
    public void test4() throws Exception {
        test("'xa xb xc xd xe xf'.match(new RegExp('x.','g'))");
    }

    /**
     * Tests 'xa Xb xc xd Xe xf'.match(/x./gi).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,Xb,xc,xd,Xe,xf")
    public void test5() throws Exception {
        test("'xa Xb xc xd Xe xf'.match(/x./gi)");
    }

    /**
     * Tests 'xa Xb xc xd Xe xf'.match(new RegExp('x.','gi')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,Xb,xc,xd,Xe,xf")
    public void test6() throws Exception {
        test("'xa Xb xc xd Xe xf'.match(new RegExp('x.','gi'))");
    }

    /**
     * Tests 'xa Xb xc xd Xe xf'.match(/x./ig).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,Xb,xc,xd,Xe,xf")
    public void test7() throws Exception {
        test("'xa Xb xc xd Xe xf'.match(/x./ig)");
    }

    /**
     * Tests 'xa Xb xc xd Xe xf'.match(new RegExp('x.','ig')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,Xb,xc,xd,Xe,xf")
    public void test8() throws Exception {
        test("'xa Xb xc xd Xe xf'.match(new RegExp('x.','ig'))");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
