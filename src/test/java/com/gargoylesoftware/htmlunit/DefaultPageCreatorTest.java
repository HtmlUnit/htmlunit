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
package com.gargoylesoftware.htmlunit;

import org.junit.Test;

/**
 * Tests for {@link DefaultPageCreator}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DefaultPageCreatorTest extends WebTestCase {

    /**
     * Test for {@link DefaultPageCreator#determinePageType(String)}.
     */
    @Test
    public void testDeterminePageType() {
        final DefaultPageCreator creator = new DefaultPageCreator();

        assertEquals("html", creator.determinePageType("application/vnd.wap.xhtml+xml"));
        assertEquals("html", creator.determinePageType("application/xhtml+xml"));
        assertEquals("html", creator.determinePageType("text/html"));
        assertEquals("html", creator.determinePageType("text/xhtml"));

        assertEquals("javascript", creator.determinePageType("text/javascript"));
        assertEquals("javascript", creator.determinePageType("application/x-javascript"));

        assertEquals("xml", creator.determinePageType("text/xml"));
        assertEquals("xml", creator.determinePageType("application/xml"));
        assertEquals("xml", creator.determinePageType("text/vnd.wap.wml"));
        assertEquals("xml", creator.determinePageType("application/vnd.mozilla.xul+xml"));
        assertEquals("xml", creator.determinePageType("application/rdf+xml"));
        assertEquals("xml", creator.determinePageType("image/svg+xml"));

        assertEquals("text", creator.determinePageType("text/plain"));
        assertEquals("text", creator.determinePageType("text/csv"));
        assertEquals("text", creator.determinePageType("text/css"));

        assertEquals("unknown", creator.determinePageType("application/pdf"));
        assertEquals("unknown", creator.determinePageType("application/x-shockwave-flash"));
    }
}
