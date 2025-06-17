/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import java.util.List;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;

/**
 * Tests for Console.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class ConsoleTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "object", "true"})
    public void prototype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log(window.console == undefined);\n"
            + "    log(typeof window.console);\n"
            + "    log('console' in window);\n"
            + "  } catch(e) { logEx(e);}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "undefined", "false"})
    public void prototypeUppercase() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log(window.Console == undefined);\n"
            + "    log(typeof window.Console);\n"
            + "    log('Console' in window);\n"
            + "  } catch(e) { logEx(e);}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void timeStamp() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (window.console && window.console.timeStamp) {\n"
            + "    console.timeStamp();\n"
            + "    console.timeStamp('ready');\n"
            + "  } else { log('window.console.timeStamp not available');}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function", "function", "function", "function", "function"})
    public void methods() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof console.log);\n"
            + "  log(typeof console.info);\n"
            + "  log(typeof console.warn);\n"
            + "  log(typeof console.error);\n"
            + "  log(typeof console.debug);\n"
            + "  log(typeof console.timeStamp);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void windowProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    var x = Object.getOwnPropertyNames(window).indexOf('console');\n"
            + "    log(x >= 0);\n"
            + "  } catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void fromWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    var x = console.error;\n"
            + "    x('hello');\n"
            + "    log('success');\n"
            + "  } catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void simpleString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  for (i = 0; i < 4; i++) {\n"
            + "    console.log('test log ' + i);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Logs logs = driver.manage().logs();
        final LogEntries logEntries = logs.get(LogType.BROWSER);
        final List<LogEntry> logEntryList = logEntries.getAll();

        final int count = logEntryList.size();
        assertTrue(count > 0);

        long timestamp = 0;
        for (int i = 0; i < 4; i++) {
            final LogEntry logEntry = logEntryList.get(i);
            assertTrue(logEntry.getMessage(), logEntry.getMessage().contains("test log " + i));
            assertTrue(logEntry.getTimestamp() >= timestamp);
            timestamp = logEntry.getTimestamp();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void assertOnly() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  number = 1;\n"
            + "  console.assert(number % 2 === 0);\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Logs logs = driver.manage().logs();
        final LogEntries logEntries = logs.get(LogType.BROWSER);
        final List<LogEntry> logEntryList = logEntries.getAll();

        assertEquals(1, logEntryList.size());

        final LogEntry logEntry = logEntryList.get(0);
        assertTrue(logEntry.getMessage(), logEntry.getMessage().contains("Assertion failed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void assertString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  number = 1;\n"
            + "  console.assert(number % 2 === 0, 'the # is not even');\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Logs logs = driver.manage().logs();
        final LogEntries logEntries = logs.get(LogType.BROWSER);
        final List<LogEntry> logEntryList = logEntries.getAll();

        assertEquals(1, logEntryList.size());

        final LogEntry logEntry = logEntryList.get(0);
        assertTrue(logEntry.getMessage(), logEntry.getMessage().contains("Assertion failed: the # is not even"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void assertObject() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  var number = 1;\n"
            + "  console.assert(number % 2 === 0, {number: number, errorMsg: 'the # is not even'});\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Logs logs = driver.manage().logs();
        final LogEntries logEntries = logs.get(LogType.BROWSER);
        final List<LogEntry> logEntryList = logEntries.getAll();

        assertEquals(1, logEntryList.size());

        final LogEntry logEntry = logEntryList.get(0);
        assertTrue(logEntry.getMessage(), logEntry.getMessage()
                .contains("Assertion failed: {\"number\":1,\"errorMsg\":\"the # is not even\"}"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void assertObjects() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  number = 1;\n"
            + "  console.assert(number % 2 === 0, {number: number}, {errorMsg: 'the # is not even'});\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Logs logs = driver.manage().logs();
        final LogEntries logEntries = logs.get(LogType.BROWSER);
        final List<LogEntry> logEntryList = logEntries.getAll();

        assertEquals(1, logEntryList.size());

        final LogEntry logEntry = logEntryList.get(0);
        assertTrue(logEntry.getMessage(), logEntry.getMessage()
                .contains("Assertion failed: {\"number\":1} {\"errorMsg\":\"the # is not even\"}"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void assertParams() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  console.assert(false, 'the word is %s', 'foo');\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Logs logs = driver.manage().logs();
        final LogEntries logEntries = logs.get(LogType.BROWSER);
        final List<LogEntry> logEntryList = logEntries.getAll();

        assertEquals(1, logEntryList.size());

        final LogEntry logEntry = logEntryList.get(0);
        assertTrue(logEntry.getMessage(), logEntry.getMessage()
                .contains("Assertion failed: the word is foo"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void trace() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  function foo() {\n"
            + "    function bar() {\n"
            + "      console.trace();\n"
            + "    }\n"
            + "    bar();\n"
            + "  }\n"
            + "  foo();\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Logs logs = driver.manage().logs();
        final LogEntries logEntries = logs.get(LogType.BROWSER);
        final List<LogEntry> logEntryList = logEntries.getAll();

        assertEquals(1, logEntryList.size());

        final LogEntry logEntry = logEntryList.get(0);
        final String logMsg = logEntry.getMessage();
        assertTrue(logMsg, logMsg
                .matches("bar\\(\\)@script in http.*:6\\n"
                        + "foo\\(\\)@script in http.*:8\\n"
                        + "@script in http.*:10"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void errorCall() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  function foo() {\n"
            + "    (undefined || console.error)('he ho');\n"
            + "  }\n"
            + "  foo();\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Logs logs = driver.manage().logs();
        final LogEntries logEntries = logs.get(LogType.BROWSER);
        final List<LogEntry> logEntryList = logEntries.getAll();

        assertEquals(1, logEntryList.size());

        final LogEntry logEntry = logEntryList.get(0);
        final String logMsg = logEntry.getMessage();
        assertTrue(logMsg, logMsg.contains("he ho"));
    }
}
