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
package com.gargoylesoftware.htmlunit.javascript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Multi-threaded JavaScript engine test.
 *
 * @version $Revision$
 * @author David D. Kilzer
 * @author Ahmed Ashour
 */
public class ThreadTest extends TestCase {

    /**
     * Creates a new instance.
     * @param name the name of the test
     */
    public ThreadTest(final String name) {
        super(name);
    }

    /**
     * @throws InterruptedException if the test fails
     */
    public void testJavaScriptEngineInMultipleThreads() throws InterruptedException {
        final TestThread thread1 = new TestThread("thread1");
        final TestThread thread2 = new TestThread("thread2");
        final TestThread thread3 = new TestThread("thread3");
        final TestThread thread4 = new TestThread("thread4");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        assertTrue("thread1 not successful", thread1.isSuccessful());
        assertTrue("thread2 not successful", thread2.isSuccessful());
        assertTrue("thread3 not successful", thread3.isSuccessful());
        assertTrue("thread4 not successful", thread4.isSuccessful());
    }

    /**
     * Runs this test many times.
     * @return a suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite("Run this many times");
        suite.addTestSuite(ThreadTest.class);
        for (int i = 1; i < 100; i++) {
            suite.addTest(new ThreadTest("testJavaScriptEngineInMultipleThreads"));
        }
        return suite;
    }
    
    /** A test object for threads*/
    private static class TestThread extends Thread {

        private boolean successful_ = false;

        /**
         * Creates an instance.
         * @param name the name of the thread
         */
        public TestThread(final String name) {
            super(name);
        }

        /** @see Thread#run() */
        @Override
        public void run() {
            try {
                testCallInheritedFunction();
                successful_ = true;
            }
            catch (final Exception e) {
                System.err.println(">>> Thread " + getName());
                e.printStackTrace(System.err);
                successful_ = false;
            }
        }

        /** @return true if the test was successful */
        public boolean isSuccessful() {
            return successful_;
        }

        /**
         * @see SimpleScriptableTest#callInheritedFunction()
         * @throws Exception if the test failed
         */
        public void testCallInheritedFunction() throws Exception {
            final String content
                = "<html><head><title>foo</title><script>\n"
                + "function doTest() {\n"
                + "    document.form1.textfield1.focus();\n"
                + "    alert('past focus');\n"
                + "}\n"
                + "</script></head><body onload='doTest()'>\n"
                + "<p>hello world</p>\n"
                + "<form name='form1'>\n"
                + "    <input type='text' name='textfield1' id='textfield1' value='foo'>\n"
                + "</form>\n"
                + "</body></html>";

            final List<String> collectedAlerts = new ArrayList<String>();
            final HtmlPage page = WebTestCase.loadPage(content, collectedAlerts);
            
            assertEquals("foo", page.getTitleText());
            assertEquals("focus not changed to textfield1",
                         page.getFormByName("form1").getInputByName("textfield1"),
                         page.getFocusedElement());
            final List<String> expectedAlerts = Collections.singletonList("past focus");
            assertEquals(expectedAlerts, collectedAlerts);
        }
    }

}
