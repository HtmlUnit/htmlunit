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
package com.gargoylesoftware.htmlunit.javascript.background;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.MemoryLeakDetector;

/**
 * <p>Tests for memory leaks. This test passes when run independently in Eclipse or via
 * Maven (mvn test -Dtest=MemoryLeakTest), but not when all tests are run via Maven
 * (mvn test). Note that this probably constitutes a false positive, meaning that the
 * window isn't leaked in this situation -- we just can't get the JVM to GC it because
 * there is no surefire way to force complete garbage collection.</p>
 *
 * <p>All suspected memory leaks should be verified with a profiler before a fix and/or
 * unit test is committed. In JProfiler, for example, you can run a unit test and tell
 * the profiler to keep the VM alive when it has finished running; once it has finished
 * running you can go to the Heap Walker, take a snapshot of the heap, sort the resultant
 * table by class name and find the class you're interested in, double-click on it, choose
 * to view References to instances of this class, then right-click on the class and choose
 * Show Paths to GC Root. This will give you a list of references which need to be
 * eliminated. Once you have a fix, repeat the above steps in order to verify that the
 * fix works.</p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class MemoryLeakTest extends SimpleWebTestCase {

    /**
     * Empty test which keeps Maven/JUnit from complaining about a test class with no tests. This test
     * class cannot be run automatically via Maven for the reasons outlined above.
     * @throws Exception if an error occurs
     */
    @Test
    public void testLeaks() throws Exception {
        // windowLeaks();
    }

    /**
     * Verifies that windows don't get leaked, especially when there are long-running background JS tasks
     * scheduled via <tt>setTimeout</tt> or <tt>setInterval</tt>. See the following bugs:
     *    http://sourceforge.net/p/htmlunit/bugs/639/
     *    http://sourceforge.net/p/htmlunit/bugs/648/
     *
     * @throws Exception if an error occurs
     */
    protected void windowLeaks() throws Exception {
        final MemoryLeakDetector detector = new MemoryLeakDetector();

        WebClient client = new WebClient();
        detector.register("w", client.getCurrentWindow());

        MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, "<html><body><script>setInterval('alert(1)',5000)</script></body></html>");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);

        client = null;
        conn = null;

        assertTrue("Window can't be garbage collected.", detector.canBeGCed("w"));
    }

}
