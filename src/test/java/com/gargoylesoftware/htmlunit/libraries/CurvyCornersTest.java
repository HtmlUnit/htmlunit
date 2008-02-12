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

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase2;

/**
 * Tests for compatibility with <a href="http://www.curvycorners.net">curvyCorners</a>.
 *
 * @version $Revision$
 * @author Gareth Davis
 */
public class CurvyCornersTest extends WebTestCase2 {

    private static final String BASE_FILE_PATH = "curvyCorners/1.2.9-beta/";
    
    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void testDemoIE7() throws Exception {
        doTest("demo.html", BrowserVersion.INTERNET_EXPLORER_7_0);
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void testDemo2IE7() throws Exception {
        doTest("demo2.html", BrowserVersion.INTERNET_EXPLORER_7_0);
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void testDemoDefault() throws Exception {
        doTest("demo.html", BrowserVersion.getDefault());
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void testDemo2Default() throws Exception {
        doTest("demo2.html", BrowserVersion.getDefault());
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void testDemoFF2() throws Exception {
        doTest("demo.html", BrowserVersion.FIREFOX_2);
    }

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void testDemo2FF2() throws Exception {
        doTest("demo2.html", BrowserVersion.FIREFOX_2);
    }
    
    private void doTest(final String fileName, final BrowserVersion version) throws Exception {
        final URL url = getClass().getClassLoader().getResource(BASE_FILE_PATH + fileName);
        assertNotNull(url);

        final WebClient client = new WebClient(version);
        client.getPage(url);
    }

}
