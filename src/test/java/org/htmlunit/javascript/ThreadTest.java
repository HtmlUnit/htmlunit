/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.htmlunit.CollectingAlertHandler;
import org.htmlunit.MockWebConnection;
import org.htmlunit.WebClient;
import org.htmlunit.WebTestCase;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

/**
 * Multi-threaded JavaScript engine test.
 *
 * @author David D. Kilzer
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ThreadTest {

    /**
     * @throws InterruptedException if the test fails
     */
    private void testJavaScriptEngineInMultipleThreads() throws InterruptedException {
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

        Assertions.assertTrue(thread1.isSuccessful(), "thread1 not successful");
        Assertions.assertTrue(thread2.isSuccessful(), "thread2 not successful");
        Assertions.assertTrue(thread3.isSuccessful(), "thread3 not successful");
        Assertions.assertTrue(thread4.isSuccessful(), "thread4 not successful");
    }

    /**
     * Repeated test - runs the multi-threaded test 99 times.
     */
    @RepeatedTest(value = 99, name = "Multi-threaded JS test repetition {currentRepetition}/{totalRepetitions}")
    public void repeatedMultiThreadTest() throws InterruptedException {
        testJavaScriptEngineInMultipleThreads();
    }

    /** A test object for threads. */
    private static class TestThread extends Thread {

        private boolean successful_ = false;

        /**
         * Creates an instance.
         * @param name the name of the thread
         */
        TestThread(final String name) {
            super(name);
        }

        /**
         * @see Thread#run()
         */
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

        /**
         * @return true if the test was successful
         */
        public boolean isSuccessful() {
            return successful_;
        }

        /**
         * @see HtmlUnitScriptableTest#callInheritedFunction()
         * @throws Exception if the test failed
         */
        public void testCallInheritedFunction() throws Exception {
            final String html =
                "<html><head><title>foo</title><script>\n"
                + "function doTest() {\n"
                + "  document.form1.textfield1.focus();\n"
                + "  alert('past focus');\n"
                + "}\n"
                + "</script></head><body onload='doTest()'>\n"
                + "<p>hello world</p>\n"
                + "<form name='form1'>\n"
                + "  <input type='text' name='textfield1' id='textfield1' value='foo'>\n"
                + "</form>\n"
                + "</body></html>";

            final List<String> collectedAlerts = new ArrayList<>();

            try (WebClient webClient = new WebClient()) {
                webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
                final MockWebConnection connection = new MockWebConnection();
                connection.setDefaultResponse(html);
                webClient.setWebConnection(connection);

                final HtmlPage page = webClient.getPage(WebTestCase.URL_FIRST);

                Assertions.assertEquals("foo", page.getTitleText());
                Assertions.assertEquals(
                             page.getFormByName("form1").getInputByName("textfield1"),
                             page.getFocusedElement(),
                             "focus not changed to textfield1");
                final List<String> expectedAlerts = Collections.singletonList("past focus");
                Assertions.assertEquals(expectedAlerts, collectedAlerts);
            }
        }
    }

}
