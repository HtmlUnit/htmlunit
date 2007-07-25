/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.regexp;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * Tests for {@link HtmlUnitRegExpProxy}.
 * @version $Revision$
 * @author Marc Guillemot
 */
public class HtmlUnitRegExpProxyTest extends WebTestCase {

    private final String str_ = "(?:<script.*?>)((\\n|\\r|.)*?)(?:<\\/script>)";
    private final String begin_ = "<div>bla</div>\n";
    private final String end_ = "foo\n<span>bla2</span>\n";
    private final String text_ = begin_
        + "<script>var a = 123;</script>"
        + end_;
    private final String expected_ = begin_ + end_;
    private final String src_ = "var re = new RegExp(str, 'img');\n"
        + "var s = text.replace(re, '');\n"
        + "if (s != expected)"
        + " throw 'Expected >' + expected + '< but got >' + s + '<';";

    /**
     * Create an instance
     * @param name The name of the test
     */
    public HtmlUnitRegExpProxyTest(final String name) {
        super(name);
    }

    /**
     * Test that string.replace works correctly (?) in htmlunit
     * @throws Exception if the test fails
     */
    public void testFixedInHtmlUnit() throws Exception {
        final String html = "<html></html>";
        final HtmlPage page = loadPage(html);
        final Window topScope = ((Window) page.getEnclosingWindow().getScriptObject());
        topScope.put("str", topScope, str_);
        topScope.put("text", topScope, text_);
        topScope.put("expected", topScope, expected_);
        page.executeJavaScriptIfPossible(src_, "custom", page.getDocumentElement());
    }

    /**
     * Test if custom patch is still needed
     */
    public void testNeedCustomFix() {
        final Context ctx = Context.enter();
        final ScriptableObject topScope = ctx.initStandardObjects();
        
        topScope.put("str", topScope, str_);
        topScope.put("text", topScope, text_);
        topScope.put("expected", topScope, expected_);
        
        assertEquals(begin_ + end_, text_.replaceAll(str_, ""));
        try {
            ctx.evaluateString(topScope, src_, "test script", 0, null);
        }
        catch (final JavaScriptException e) {
            assertTrue(e.getMessage().indexOf("Expected >") == 0);
        }
    }
}
