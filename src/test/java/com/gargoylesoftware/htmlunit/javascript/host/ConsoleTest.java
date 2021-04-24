/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Console}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ConsoleTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "object", "true"})
    public void prototype() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  try {\n"
            + "    alert(window.console == undefined);\n"
            + "    alert(typeof window.console);\n"
            + "    alert('console' in window);\n"
            + "  } catch(e) { alert('exception');}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "undefined", "false"},
            IE = {"false", "object", "true"})
    public void prototypeUppercase() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  try {\n"
            + "    alert(window.Console == undefined);\n"
            + "    alert(typeof window.Console);\n"
            + "    alert('Console' in window);\n"
            + "  } catch(e) { alert('exception');}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "window.console.timeStamp not available")
    public void timeStamp() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  if (window.console && window.console.timeStamp) {\n"
            + "    console.timeStamp();\n"
            + "    console.timeStamp('ready');\n"
            + "  } else { alert('window.console.timeStamp not available');}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "function", "function", "function", "function", "function"},
            IE = {"function", "function", "function", "function", "function", "undefined"})
    public void methods() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  alert(typeof console.log);\n"
            + "  alert(typeof console.info);\n"
            + "  alert(typeof console.warn);\n"
            + "  alert(typeof console.error);\n"
            + "  alert(typeof console.debug);\n"
            + "  alert(typeof console.timeStamp);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "success",
            IE = "exception")
    public void fromWindow() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  try {\n"
            + "    var x = console.error;\n"
            + "    x('hello');\n"
            + "    alert('success');\n"
            + "  } catch(e) {alert('exception')}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void simpleString() throws Exception {
        final String html
            = "<html>\n"
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
        final String html
            = "<html>\n"
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
        final String html
            = "<html>\n"
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
        final String html
            = "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  number = 1;\n"
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
                .contains("Assertion failed: ({number: 1.0, errorMsg: \"the # is not even\"})"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void assertObjects() throws Exception {
        final String html
            = "<html>\n"
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
                .contains("Assertion failed: ({number: 1.0}) ({errorMsg: \"the # is not even\"})"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @BuggyWebDriver
    public void assertParams() throws Exception {
        final String html
            = "<html>\n"
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
}
