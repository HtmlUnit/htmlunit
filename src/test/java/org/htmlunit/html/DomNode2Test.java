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
package org.htmlunit.html;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link DomNode}.
 *
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
public class DomNode2Test extends WebDriverTestCase {

    /**
     * Test for Bug #1253.
     *
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"HierarchyRequestError/DOMException", "0"})
    public void apendChild_recursive() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var e = document.createElement('div');\n"
                + "  try {\n"
                + "    log(e.appendChild(e) === e);\n"
                + "  } catch(e) {logEx(e);}\n"
                + "  log(e.childNodes.length);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for Bug #1253.
     *
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "HierarchyRequestError/DOMException", "1", "0"})
    public void appendChild_recursive_parent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var e1 = document.createElement('div');\n"
                + "  var e2 = document.createElement('div');\n"
                + "  try {\n"
                + "    log(e1.appendChild(e2) === e2);\n"
                + "    log(e2.appendChild(e1) === e1);\n"
                + "  } catch(e) {logEx(e);}\n"
                + "  log(e1.childNodes.length);\n"
                + "  log(e2.childNodes.length);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true", "true", "true"})
    public void ownerDocument() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document === document.body.ownerDocument);\n"
            + "      log(document === document.getElementById('foo').ownerDocument);\n"
            + "      log(document === document.body.firstChild.ownerDocument);\n"

            + "      var div = document.createElement('div');"
            + "      log(document === div.ownerDocument);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "<div id='foo'>bla</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true", "true", "true"})
    public void getRootNode() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (!document.body.getRootNode) {\n"
            + "        log('-'); return;\n"
            + "      }\n"
            + "      log(document === document.body.getRootNode());\n"
            + "      log(document === document.getElementById('foo').getRootNode());\n"
            + "      log(document === document.body.firstChild.getRootNode());\n"

            + "      var div = document.createElement('div');"
            + "      log(div.getRootNode() === div);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "<div id='foo'>bla</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("beforeafter")
    public void textContentCdata() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.getElementById('tester').textContent);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='tester'>before<![CDATA[inside]]>after</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    @Test
    @Alerts({"before:new,old", "after:old,new",
             "prepend:new,old", "append:old,new", "appendChild:old,new",
             "insertBefore:new,old",
             "replaceWith:new", "replaceChild:new", "replaceChildren:new"})
    public void documentFragment_dissolution() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function createFragment() {\n"
                + "  var f = document.createDocumentFragment();\n"
                + "  var s = document.createElement('span'); s.textContent = 'new';\n"
                + "  f.appendChild(s);\n"
                + "  return f;\n"
                + "}\n"
                + "function texts(id) {\n"
                + "  var el = document.getElementById(id);\n"
                + "  var r = [];\n"
                + "  for (var i = 0; i < el.children.length; i++) r.push(el.children[i].textContent);\n"
                + "  return r.join(',');\n"
                + "}\n"
                + "function test() {\n"
                + "  document.getElementById('beforeRef').before(createFragment());\n"
                + "  log('before:' + texts('before'));\n"
                + "\n"
                + "  document.getElementById('afterRef').after(createFragment());\n"
                + "  log('after:' + texts('after'));\n"
                + "\n"
                + "  document.getElementById('prepend').prepend(createFragment());\n"
                + "  log('prepend:' + texts('prepend'));\n"
                + "\n"
                + "  document.getElementById('append').append(createFragment());\n"
                + "  log('append:' + texts('append'));\n"
                + "\n"
                + "  document.getElementById('appendChild').appendChild(createFragment());\n"
                + "  log('appendChild:' + texts('appendChild'));\n"
                + "\n"
                + "  document.getElementById('insertBefore')\n"
                + "    .insertBefore(createFragment(), document.getElementById('insertBeforeRef'));\n"
                + "  log('insertBefore:' + texts('insertBefore'));\n"
                + "\n"
                + "  document.getElementById('replaceWithRef').replaceWith(createFragment());\n"
                + "  log('replaceWith:' + texts('replaceWith'));\n"
                + "\n"
                + "  document.getElementById('replaceChild')\n"
                + "    .replaceChild(createFragment(), document.getElementById('replaceChildRef'));\n"
                + "  log('replaceChild:' + texts('replaceChild'));\n"
                + "\n"
                + "  document.getElementById('replaceChildren').replaceChildren(createFragment());\n"
                + "  log('replaceChildren:' + texts('replaceChildren'));\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<div id='before'><span id='beforeRef'>old</span></div>\n"
                + "<div id='after'><span id='afterRef'>old</span></div>\n"
                + "<div id='prepend'><span>old</span></div>\n"
                + "<div id='append'><span>old</span></div>\n"
                + "<div id='appendChild'><span>old</span></div>\n"
                + "<div id='insertBefore'><span id='insertBeforeRef'>old</span></div>\n"
                + "<div id='replaceWith'><span id='replaceWithRef'>old</span></div>\n"
                + "<div id='replaceChild'><span id='replaceChildRef'>old</span></div>\n"
                + "<div id='replaceChildren'><span>old</span></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The hidden attribute works like a default style 'display: none';
     * an explicit display style (inline or from a style sheet) overrides it.
     * The same is true for the open state of a dialog.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"d1:false", "d2:true", "d3:true", "d4:false", "d5c:false", "d6c:true", "d7:false",
             "d8:false", "d9:true", "d10:true", "d11:false", "d12:true", "d13:true"})
    public void isDisplayedHiddenAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<style>\n"
            + "  .inline { display: inline; }\n"
            + "</style>\n"
            + "</head><body>\n"
            + "<div id='d1' hidden>a</div>\n"
            + "<div id='d2' hidden style='display: block'>b</div>\n"
            + "<span id='d3' hidden class='inline'>c</span>\n"
            + "<div id='d4' hidden style='display: none'>d</div>\n"
            + "<div id='d5' hidden><span id='d5c'>e</span></div>\n"
            + "<div id='d6' hidden style='display: block'><span id='d6c'>f</span></div>\n"
            + "<div id='d7' hidden style='display: block; visibility: hidden'>g</div>\n"
            + "<dialog id='d8' open hidden>h</dialog>\n"
            + "<dialog id='d9' open hidden style='display: block'>i</dialog>\n"
            + "<dialog id='d10' hidden style='display: block'>j</dialog>\n"
            + "<dialog id='d11'>k</dialog>\n"
            + "<dialog id='d12' open>l</dialog>\n"
            + "<dialog id='d13' style='display: block'>m</dialog>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts(), isDisplayed(driver,
                "d1", "d2", "d3", "d4", "d5c", "d6c", "d7", "d8", "d9", "d10", "d11", "d12", "d13"));
    }

    /**
     * Changing the hidden attribute or the open state of a dialog changes the display state.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"d1:true", "d2:true", "d3:false", "d3c:false", "dlg:false",
             "d1:false", "d2:true", "d3:true", "d3c:true", "dlg:true",
             "d1:true", "dlg:false"})
    public void isDisplayedHiddenAttributeChanged() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<div id='d1'>a</div>\n"
            + "<div id='d2' style='display: block'>b</div>\n"
            + "<div id='d3' hidden><span id='d3c'>c</span></div>\n"
            + "<dialog id='dlg'>d</dialog>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final List<String> result = new ArrayList<>(isDisplayed(driver, "d1", "d2", "d3", "d3c", "dlg"));

        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('d1').hidden = true;"
                + "document.getElementById('d2').hidden = true;"
                + "document.getElementById('d3').removeAttribute('hidden');"
                + "document.getElementById('dlg').show();");
        result.addAll(isDisplayed(driver, "d1", "d2", "d3", "d3c", "dlg"));

        // an explicit display style set later also wins
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('d1').style.display = 'block';"
                + "document.getElementById('dlg').close();");
        result.addAll(isDisplayed(driver, "d1", "dlg"));

        assertEquals(getExpectedAlerts(), result);
    }

    /**
     * The text of an element with hidden attribute (or a closed dialog) is only part
     * of the visible text if there is an explicit display style.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a b d", "a b c d"})
    public void getTextHiddenAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<div id='p1'>a <span hidden>x</span> b <dialog>y</dialog> d</div>\n"
            + "<div id='p2'>a <span hidden style='display: inline'>b</span> c <span>d</span></div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final List<String> result = new ArrayList<>();
        result.add(driver.findElement(By.id("p1")).getText());
        result.add(driver.findElement(By.id("p2")).getText());
        assertEquals(getExpectedAlerts(), result);
    }

    private static List<String> isDisplayed(final WebDriver driver, final String... ids) {
        final List<String> result = new ArrayList<>();
        for (final String id : ids) {
            result.add(id + ":" + driver.findElement(By.id(id)).isDisplayed());
        }
        return result;
    }
}
