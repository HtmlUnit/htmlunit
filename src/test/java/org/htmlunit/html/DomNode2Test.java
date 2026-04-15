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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DomNode}.
 *
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class DomNode2Test extends WebDriverTestCase {

    /**
     * Test for Bug #1253.
     *
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"HierarchyRequestError/DOMException", "0"})
    public void appendChild_recursive() throws Exception {
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

}
