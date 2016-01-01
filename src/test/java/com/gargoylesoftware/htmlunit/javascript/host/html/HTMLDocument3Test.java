/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HTMLDocument}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HTMLDocument3Test extends WebTestCase {

    /**
     * Test having &lt; and &gt; in attribute values.
     */
    @Test
    public void canAlreadyBeParsed() {
        assertTrue(HTMLDocument.canAlreadyBeParsed("<p>hallo</p>"));
        assertTrue(HTMLDocument.canAlreadyBeParsed("<img src='foo' alt=\"<'>\"></img>"));

        // double close is ok
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script></script></script>"));

        // check for correct string quoting in script
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc';</script>"));
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script>var test =\"abc\";</script>"));
        assertFalse(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc\";</script>"));
        assertFalse(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc;</script>"));
        assertFalse(HTMLDocument.canAlreadyBeParsed("<script>var test =\"abc;</script>"));

        // check quoting only inside script tags
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc';</script><p>it's fun</p>"));
    }
}
