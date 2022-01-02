/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import java.net.URL;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.BuggyWebDriver;

/**
 * Tests for {@link MutationObserver}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 */
@RunWith(BrowserRunner.class)
public class MutationObserverTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void observeNullNode() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var observer = new MutationObserver(function(mutations) {});\n"
            + "\n"
            + "  try {\n"
            + "    observer.observe(div, {});\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>old</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void observeNullInit() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var observer = new MutationObserver(function(mutations) {});\n"
            + "\n"
            + "  try {\n"
            + "    observer.observe(div);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>old</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void observeEmptyInit() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var observer = new MutationObserver(function(mutations) {});\n"
            + "\n"
            + "  try {\n"
            + "    observer.observe(div, {});\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>old</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"exception", "childList", "attributes", "characterData"})
    public void observeRequiredMissingInit() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var observer = new MutationObserver(function(mutations) {});\n"
            + "\n"
            + "  try {\n"
            + "    observer.observe(div, {subtree: true});\n"
            + "  } catch(e) { log('exception'); }\n"
            + "  try {\n"
            + "    observer.observe(div, {childList: true});\n"
            + "    log('childList');\n"
            + "  } catch(e) { log('exception'); }\n"
            + "  try {\n"
            + "    observer.observe(div, {attributes: true});\n"
            + "    log('attributes');\n"
            + "  } catch(e) { log('exception'); }\n"
            + "  try {\n"
            + "    observer.observe(div, {characterData: true});\n"
            + "    log('characterData');\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>old</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"old", "new"})
    public void characterData() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var observer = new MutationObserver(function(mutations) {\n"
            + "    mutations.forEach(function(mutation) {\n"
            + "      log(mutation.oldValue);\n"
            + "      log(mutation.target.textContent);\n"
            + "    });\n"
            + "  });\n"
            + "\n"
            + "  observer.observe(div, {\n"
            + "    characterData: true,\n"
            + "    characterDataOldValue: true,\n"
            + "    subtree: true\n"
            + "  });\n"
            + "\n"
            + "  div.firstChild.textContent = 'new';\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>old</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "new"})
    public void characterDataNoOldValue() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var observer = new MutationObserver(function(mutations) {\n"
            + "    mutations.forEach(function(mutation) {\n"
            + "      log(mutation.oldValue);\n"
            + "      log(mutation.target.textContent);\n"
            + "    });\n"
            + "  });\n"
            + "\n"
            + "  observer.observe(div, {\n"
            + "    characterData: true,\n"
            + "    subtree: true\n"
            + "  });\n"
            + "\n"
            + "  div.firstChild.textContent = 'new';\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>old</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void characterDataNoSubtree() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var observer = new MutationObserver(function(mutations) {\n"
            + "    mutations.forEach(function(mutation) {\n"
            + "      log(mutation.oldValue);\n"
            + "      log(mutation.target.textContent);\n"
            + "    });\n"
            + "  });\n"
            + "\n"
            + "  observer.observe(div, {\n"
            + "    characterData: true,\n"
            + "    characterDataOldValue: true\n"
            + "  });\n"
            + "\n"
            + "  div.firstChild.textContent = 'new';\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>old</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"attributes", "ltr"})
    public void attributes() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var observer = new MutationObserver(function(mutations) {\n"
            + "    mutations.forEach(function(mutation) {\n"
            + "      log(mutation.type);\n"
            + "      log(mutation.oldValue);\n"
            + "    });\n"
            + "  });\n"
            + "\n"
            + "  observer.observe(div, {\n"
            + "    attributes: true,\n"
            + "    attributeFilter: ['dir'],\n"
            + "    attributeOldValue: true\n"
            + "  });\n"
            + "\n"
            + "  div.dir = 'rtl';\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' dir='ltr'>old</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test case for issue #1811.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"heho", "attributes", "value", "null", "x", "abc",
             "heho", "attributes", "value", "null", "y", "abc"})
    public void attributeValue() throws Exception {
        final String html
            = "<html>\n"
            + "<head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var config = { attributes: true, childList: true, characterData: true, subtree: true };\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        log(mutation.type);\n"
            + "        log(mutation.attributeName);\n"
            + "        log(mutation.oldValue);\n"
            + "        log(mutation.target.getAttribute(\"value\"));\n"
            + "        log(mutation.target.value);\n"
            + "      });\n"
            + "    });\n"
            + "    observer.observe(document.getElementById('tester'), config);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='tester' value=''>\n"
            + "  <button id='doAlert' onclick='log(\"heho\");'>DoAlert</button>\n"
            + "  <button id='doIt' "
                        + "onclick='document.getElementById(\"tester\").setAttribute(\"value\", \"x\")'>"
                        + "DoIt</button>\n"
            + "  <button id='doItAgain' "
                        + " onclick='document.getElementById(\"tester\").setAttribute(\"value\", \"y\")'>"
                        + "DoItAgain</button>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("tester")).sendKeys("abc");
        verifyTitle2(driver, new String[] {});

        driver.findElement(By.id("doAlert")).click();
        verifyTitle2(driver, new String[] {"heho"});

        final String[] expected = getExpectedAlerts();
        driver.findElement(By.id("doIt")).click();
        verifyTitle2(driver, Arrays.copyOfRange(expected, 0, 6));

        driver.findElement(By.id("doAlert")).click();
        verifyTitle2(driver, Arrays.copyOfRange(expected, 0, 7));

        driver.findElement(By.id("doItAgain")).click();
        verifyTitle2(driver, expected);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLHeadingElement]-attributes")
    @BuggyWebDriver(FF = "[object HTMLInputElement]-attributes\n"
            + "[object HTMLInputElement]-attributes\n"
            + "[object HTMLInputElement]-attributes\n"
            + "[object HTMLInputElement]-attributes\n"
            + "[object HTMLHeadingElement]-attributes",
            FF_ESR = "[object HTMLInputElement]-attributes\n"
            + "[object HTMLInputElement]-attributes\n"
            + "[object HTMLInputElement]-attributes\n"
            + "[object HTMLInputElement]-attributes\n"
            + "[object HTMLHeadingElement]-attributes")
    public void attributeValue2() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function makeRed() {\n"
            + "    document.getElementById('headline').setAttribute('style', 'color: red');\n"
            + "  }\n"

            + "  function print(mutation) {\n"
            + "    log(mutation.target + '-' + mutation.type);\n"
            + "  }\n"

            + "  function log(x) {\n"
            + "    document.getElementById('log').value += x + '\\n';\n"
            + "  }\n"

            + "  function test() {\n"
            + "    var mobs = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(print)\n"
            + "    });\n"

            + "    mobs.observe(document.getElementById('container'), {\n"
            + "      attributes: true,\n"
            + "      childList: true,\n"
            + "      characterData: true,\n"
            + "      subtree: true\n"
            + "    });\n"

            + "    document.addEventListener('beforeunload', function() {\n"
            + "      mobs.disconnect();\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='container'>\n"
            + "    <h1 id='headline' style='font-style: italic'>Some headline</h1>\n"
            + "    <input id='id1' type='button' onclick='makeRed()' value='Make Red'>\n"
            + "  </div>\n"
            + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
            + "</body></html>\n";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("id1")).click();

        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"before", "after div", "after text", "div observed", "text observed"},
            IE = {"before", "after div", "after text", "text observed"})
    public void callbackOrder() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var divObserver = new MutationObserver(function() {\n"
            + "      log('div observed');\n"
            + "    });\n"
            + "\n"
            + "  divObserver.observe(div, { attributes: true });\n"
            + "\n"
            + "  var text = document.createTextNode('')\n"
            + "  var txtObserver = new MutationObserver(function() {\n"
            + "        log('text observed');\n"
            + "    });\n"
            + "  txtObserver.observe(text, { characterData: true });"
            + "\n"
            + "  log('before');\n"
            + "  div.style = 'background-color: red';\n"
            + "  log('after div');\n"
            + "  text.data = 42;\n"
            + "  log('after text');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' style='color: green'>old</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Content")
    public void callbackRequiresStackSetup() throws Exception {
        final String content = "<html><head><title>Content</title></head><body><p>content</p></body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "content.html"), content);

        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "\n"
            + "  var text = document.createTextNode('')\n"
            + "  var txtObserver = new MutationObserver(function() {\n"
            + "        window.location.href = 'content.html'"
            + "    });\n"
            + "  txtObserver.observe(text, { characterData: true });"
            + "\n"
            + "  text.data = 42\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' dir='ltr'>old</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }
}
