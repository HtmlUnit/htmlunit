/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
