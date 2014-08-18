/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.DefaultPageCreator.PageType;

/**
 * Tests for {@link DefaultPageCreator}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class DefaultPageCreator2Test extends WebTestCase {

    /**
     * Test for {@link DefaultPageCreator#determinePageType(String)}.
     */
    @Test
    public void determinePageType() {
        assertEquals(PageType.HTML, DefaultPageCreator.determinePageType("text/html"));

        assertEquals(PageType.JAVASCRIPT, DefaultPageCreator.determinePageType("text/javascript"));
        assertEquals(PageType.JAVASCRIPT, DefaultPageCreator.determinePageType("application/x-javascript"));
        assertEquals(PageType.JAVASCRIPT, DefaultPageCreator.determinePageType("application/javascript"));

        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("text/xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("application/xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("application/xhtml+xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("text/vnd.wap.wml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("application/vnd.mozilla.xul+xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("application/vnd.wap.xhtml+xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("application/rdf+xml"));
        assertEquals(PageType.XML, DefaultPageCreator.determinePageType("image/svg+xml"));

        assertEquals(PageType.TEXT, DefaultPageCreator.determinePageType("text/plain"));
        assertEquals(PageType.TEXT, DefaultPageCreator.determinePageType("text/csv"));
        assertEquals(PageType.TEXT, DefaultPageCreator.determinePageType("text/css"));
        assertEquals(PageType.TEXT, DefaultPageCreator.determinePageType("text/xhtml"));

        assertEquals(PageType.UNKNOWN, DefaultPageCreator.determinePageType(null));
        assertEquals(PageType.UNKNOWN, DefaultPageCreator.determinePageType(""));
        assertEquals(PageType.UNKNOWN, DefaultPageCreator.determinePageType(" \t"));
        assertEquals(PageType.UNKNOWN, DefaultPageCreator.determinePageType("application/pdf"));
        assertEquals(PageType.UNKNOWN, DefaultPageCreator.determinePageType("application/x-shockwave-flash"));
    }
}
