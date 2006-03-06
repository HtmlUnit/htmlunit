/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link ScriptException}.
 * 
 * @version $Revision$
 * @author Marc Guillemot
 */
public final class ScriptExceptionTest extends WebTestCase {

    /**
     * Create an instance.
     * 
     * @param name
     *            The name of the test.
     */
    public ScriptExceptionTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    public void testConstructor() throws Exception {
        final String message = "bla bla";
        final Throwable t = new RuntimeException(message);
        final HtmlPage page = loadPage("<html></html>");
        final ScriptException exception = new ScriptException(page, t);

        assertEquals(t, exception.getCause());
        assertEquals(message, exception.getMessage());
    }

    /**
     * To remove when deprecated method have been removed
     * 
     * @deprecated
     * @throws Exception
     *             if the test fails
     */
    public void testDeprecated() throws Exception {
        final Throwable t = new RuntimeException();
        final HtmlPage page = loadPage("<html></html>");
        final ScriptException exception = new ScriptException(page, t);

        assertEquals(t, exception.getEnclosedException());
    }

    /**
     * Test access to the page where the exception occured from the exception
     * 
     * @throws Exception
     *             if the test fails
     */
    public void testGetPage() throws Exception {
        final String html = "<html><script>notExisting()</script></html>";

        try {
            loadPage(html);
        }
        catch (final ScriptException e) {
            assertEquals(URL_GARGOYLE, e.getPage().getWebResponse().getUrl());
        }
    }
}
