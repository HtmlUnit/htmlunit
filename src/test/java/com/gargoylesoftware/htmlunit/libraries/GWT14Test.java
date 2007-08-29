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
package com.gargoylesoftware.htmlunit.libraries;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for 1.4 version of <a href="http://code.google.com/webtoolkit">Google Web Toolkit</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class GWT14Test extends WebTestCase {

    /**
     * Creates an instance.
     *
     * @param name The name of the test.
     */
    public GWT14Test(final String name) {
        super(name);
    }

    /**
     * @throws Exception If an error occurs.
     */
    public void testHello() throws Exception {
        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.getDefault(), collectedAlerts, "Hello");
    }

    /**
     * Returns the GWT directory being tested.
     * @return the GWT directory being tested.
     */
    protected String getDirectory() {
        return "1.4.60";
    }

    /**
     * Loads the GWT unit test index page using the specified browser version, and subdirectory.
     *
     * @param version The browser version to use.
     * @param collectedAlerts The List to collect alerts into.
     * @param testName The name of the sample GWT test.
     * @throws Exception if an error occurs.
     * @return The loaded page.
     */
    protected HtmlPage loadPage(final BrowserVersion version, final List collectedAlerts, final String testName)
        throws Exception {
        final String resource = "gwt/" + getDirectory() + "/" + testName + "/" + testName + ".html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        final WebClient client = new WebClient(version);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        return (HtmlPage) client.getPage(url);
    }

}
