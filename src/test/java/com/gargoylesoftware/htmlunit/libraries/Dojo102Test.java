/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.libraries;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpWebConnectionTest;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with version 1.0.2 of the <a href="http://dojotoolkit.org/">Dojo
 * JavaScript library</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class Dojo102Test extends WebTestCase {

    private Server server_;

    private static final String GROUP_DELIMITER = "------------------------------------------------------------";

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo() throws Exception {
        // TODO: blocked by property enumeration order bug in Rhino, then some CSS stuff
        // https://bugzilla.mozilla.org/show_bug.cgi?id=419090
        if (notYetImplemented()) {
            return;
        }

        final WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_7_0);
        final String url = "http://localhost:" + HttpWebConnectionTest.PORT + "/util/doh/runner.html";

        final HtmlPage page = (HtmlPage) client.getPage(url);
        page.getEnclosingWindow().getThreadManager().joinAll(10000);

        final HtmlElement logBody = page.getHtmlElementById("logBody");
        DomNode lastChild = logBody.getLastDomChild();
        while (true) {
            Thread.sleep(10000);
            final DomNode newLastChild = logBody.getLastDomChild();
            if (lastChild != newLastChild) {
                lastChild = newLastChild;
            }
            else {
                break;
            }
        }

        final Iterator<HtmlElement> logs = logBody.getChildElements().iterator();
        eq("345 tests to run in 41 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base._loader.bootstrap\" has 5 tests to run", logs);
        eq("PASSED test: hasConsole", logs);
        eq("PASSED test: hasDjConfig", logs);
        eq("PASSED test: getObject", logs);
        eq("PASSED test: exists", logs);
        eq("PASSED test: evalWorks", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base._loader.loader\" has 3 tests to run", logs);
        eq("PASSED test: baseUrl", logs);
        eq("PASSED test: modulePaths", logs);
        eq("PASSED test: moduleUrls", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base._loader.hostenv_browser\" has 1 test to run", logs);
        eq("PASSED test: getText", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.array\" has 13 tests to run", logs);
        eq("PASSED test: testIndexOf", logs);
        eq("PASSED test: testIndexOfFromIndex", logs);
        eq("PASSED test: testLastIndexOf", logs);
        eq("PASSED test: testLastIndexOfFromIndex", logs);
        eq("PASSED test: testForEach", logs);
        eq("PASSED test: testForEach_str", logs);
        eq("PASSED test: testEvery", logs);
        eq("PASSED test: testEvery_str", logs);
        eq("PASSED test: testSome", logs);
        eq("PASSED test: testSome_str", logs);
        eq("PASSED test: testFilter", logs);
        eq("PASSED test: testFilter_str", logs);
        eq("PASSED test: testMap", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.Color\" has 12 tests to run", logs);
        eq("PASSED test: testColor1", logs);
        eq("PASSED test: testColor2", logs);
        eq("PASSED test: testColor3", logs);
        eq("PASSED test: testColor4", logs);
        eq("PASSED test: testColor5", logs);
        eq("PASSED test: testColor6", logs);
        eq("PASSED test: testColor7", logs);
        eq("PASSED test: testColor8", logs);
        eq("PASSED test: testColor9", logs);
        eq("PASSED test: testColor10", logs);
        eq("PASSED test: testColor11", logs);
        eq("PASSED test: testColor12", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.lang\" has 13 tests to run", logs);
        eq("PASSED test: mixin", logs);
        eq("PASSED test: extend", logs);
        eq("PASSED test: isObject", logs);
        eq("PASSED test: isArray", logs);
        eq("PASSED test: isArrayLike", logs);
        eq("PASSED test: isString", logs);
        eq("PASSED test: partial", logs);
        eq("PASSED test: nestedPartial", logs);
        eq("PASSED test: hitch", logs);
        eq("PASSED test: hitchWithArgs", logs);
        eq("PASSED test: hitchAsPartial", logs);
        eq("PASSED test: _toArray", logs);
        eq("PASSED test: clone", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.declare\" has 12 tests to run", logs);
        eq("PASSED test: smokeTest", logs);
        eq("PASSED test: smokeTest2", logs);
        eq("PASSED test: smokeTestWithCtor", logs);
        eq("PASSED test: smokeTestCompactArgs", logs);
        eq("PASSED test: subclass", logs);
        eq("PASSED test: subclassWithCtor", logs);
        eq("PASSED test: mixinSubclass", logs);
        eq("PASSED test: superclassRef", logs);
        eq("PASSED test: inheritedCall", logs);
        eq("PASSED test: inheritedExplicitCall", logs);
        eq("PASSED test: inheritedMixinCalls", logs);
        eq("PASSED test: mixinPreamble", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.connect\" has 10 tests to run", logs);
        eq("PASSED test: smokeTest", logs);
        eq("PASSED test: basicTest", logs);
        eq("PASSED test: hubConnectDisconnect1000", logs);
        eq("PASSED test: args4Test", logs);
        eq("PASSED test: args3Test", logs);
        eq("PASSED test: args2Test", logs);
        eq("PASSED test: scopeTest1", logs);
        eq("PASSED test: scopeTest2", logs);
        eq("PASSED test: connectPublisher", logs);
        eq("PASSED test: publishSubscribe1000", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.Deferred\" has 4 tests to run", logs);
        eq("debug from dojo.Deferred callback", logs);
        eq("PASSED test: callback", logs);
        eq("PASSED test: errback", logs);
        eq("PASSED test: callbackTwice", logs);
        eq("PASSED test: addBoth", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.json\" has 1 test to run", logs);
        eq("PASSED test: toAndFromJson", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.html\" has 1 test to run", logs);
        eq("57 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 57 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.marginBox('sq100').w);", logs);

        // TODO: a gazillion more assertions...
    }

    private void eq(final String expected, final Iterator<HtmlElement> i) {
        assertEquals(expected, i.next().asText());
    }

    /**
     * Before
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        server_ = HttpWebConnectionTest.startWebServer("src/test/resources/dojo/1.0.2");
    }

    /**
     * After
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        HttpWebConnectionTest.stopWebServer(server_);
        server_ = null;
    }
}
