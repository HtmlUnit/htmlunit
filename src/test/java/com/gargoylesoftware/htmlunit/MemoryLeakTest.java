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

import com.gargoylesoftware.htmlunit.util.MemoryLeakDetector;

/**
 * Tests for memory leaks.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class MemoryLeakTest extends WebTestCase {

    /**
     * Verifies that windows don't get leaked, especially when there are long-running background JS tasks
     * scheduled via <tt>setTimeout</tt> or <tt>setInterval</tt>. See the following bugs:
     *    https://sourceforge.net/tracker/index.php?func=detail&aid=2003396&group_id=47038&atid=448266
     *    https://sourceforge.net/tracker/index.php?func=detail&aid=2014629&group_id=47038&atid=448266
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testWindowLeaks() throws Exception {

        final MemoryLeakDetector detector = new MemoryLeakDetector();

        WebClient client = new WebClient();
        detector.register("w", client.getCurrentWindow());

        MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, "<html><body><script>setInterval('alert(1)',5000)</script></body></html>");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);

        client = null;
        conn = null;

        assertTrue("Window can't be garbage collected.", detector.canBeGCed("w"));
    }

}
