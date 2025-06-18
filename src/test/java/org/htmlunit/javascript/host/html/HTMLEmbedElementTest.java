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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HTMLEmbedElement}.
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HTMLEmbedElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"left", "right", "bottom", "middle", "top",
                       "absbottom", "absmiddle", "baseline", "texttop", "wrong", ""},
            FF = {"left", "right", "bottom", "middle", "top",
                  "absbottom", "absmiddle", "bottom", "texttop", "wrong", ""},
            FF_ESR = {"left", "right", "bottom", "middle", "top",
                      "absbottom", "absmiddle", "bottom", "texttop", "wrong", ""})
    @HtmlUnitNYI(FF = {"left", "right", "bottom", "middle", "top",
                       "absbottom", "absmiddle", "baseline", "texttop", "wrong", ""},
            FF_ESR = {"left", "right", "bottom", "middle", "top",
                      "absbottom", "absmiddle", "baseline", "texttop", "wrong", ""})
    public void getAlign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <embed id='e1' align='left' ></embed>\n"
            + "  <embed id='e2' align='right' ></embed>\n"
            + "  <embed id='e3' align='bottom' ></embed>\n"
            + "  <embed id='e4' align='middle' ></embed>\n"
            + "  <embed id='e5' align='top' ></embed>\n"
            + "  <embed id='e6' align='absbottom' ></embed>\n"
            + "  <embed id='e7' align='absmiddle' ></embed>\n"
            + "  <embed id='e8' align='baseline' ></embed>\n"
            + "  <embed id='e9' align='texttop' ></embed>\n"
            + "  <embed id='e10' align='wrong' ></embed>\n"
            + "  <embed id='e11' ></embed>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 11; i++) {\n"
            + "    log(document.getElementById('e' + i).align);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CenTer", "8", "foo", "left", "right", "bottom", "middle", "top",
                       "absbottom", "absmiddle", "baseline", "texttop"},
            FF = {"CenTer", "8", "foo", "left", "right", "bottom", "middle", "top",
                  "absbottom", "absmiddle", "bottom", "texttop"},
            FF_ESR = {"CenTer", "8", "foo", "left", "right", "bottom", "middle", "top",
                      "absbottom", "absmiddle", "bottom", "texttop"})
    @HtmlUnitNYI(FF = {"CenTer", "8", "foo", "left", "right", "bottom", "middle", "top",
                       "absbottom", "absmiddle", "baseline", "texttop"},
            FF_ESR = {"CenTer", "8", "foo", "left", "right", "bottom", "middle", "top",
                      "absbottom", "absmiddle", "baseline", "texttop"})
    public void setAlign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <embed id='e1' align='left' ></embed>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch(e) { logEx(e); }\n"
            + "    log(elem.align);\n"
            + "  }\n"

            + "  var elem = document.getElementById('e1');\n"
            + "  setAlign(elem, 'CenTer');\n"

            + "  setAlign(elem, '8');\n"
            + "  setAlign(elem, 'foo');\n"

            + "  setAlign(elem, 'left');\n"
            + "  setAlign(elem, 'right');\n"
            + "  setAlign(elem, 'bottom');\n"
            + "  setAlign(elem, 'middle');\n"
            + "  setAlign(elem, 'top');\n"
            + "  setAlign(elem, 'absbottom');\n"
            + "  setAlign(elem, 'absmiddle');\n"
            + "  setAlign(elem, 'baseline');\n"
            + "  setAlign(elem, 'texttop');\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"10px", "20em", "80%", "40", "wrong", ""})
    public void getHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <embed id='e1' height='10px' ></embed>\n"
            + "  <embed id='e2' height='20em' ></embed>\n"
            + "  <embed id='e3' height='80%' ></embed>\n"
            + "  <embed id='e4' height='40' ></embed>\n"
            + "  <embed id='e5' height='wrong' ></embed>\n"
            + "  <embed id='e6' ></embed>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    log(document.getElementById('e' + i).height);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"20px", "8", "foo"})
    public void setHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <embed id='e1' height='10px' ></embed>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setHeight(elem, value) {\n"
            + "    try {\n"
            + "      elem.height = value;\n"
            + "    } catch(e) { logEx(e); }\n"
            + "    log(elem.height);\n"
            + "  }\n"

            + "  var elem = document.getElementById('e1');\n"
            + "  setHeight(elem, '20px');\n"

            + "  setHeight(elem, '8');\n"
            + "  setHeight(elem, 'foo');\n"

            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"10px", "20em", "80%", "40", "wrong", ""})
    public void getWidth() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <embed id='e1' width='10px' ></embed>\n"
            + "  <embed id='e2' width='20em' ></embed>\n"
            + "  <embed id='e3' width='80%' ></embed>\n"
            + "  <embed id='e4' width='40' ></embed>\n"
            + "  <embed id='e5' width='wrong' ></embed>\n"
            + "  <embed id='e6' ></embed>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    log(document.getElementById('e' + i).width);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"20px", "8", "foo"})
    public void setWidth() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <embed id='e1' width='10px' ></embed>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setWidth(elem, value) {\n"
            + "    try {\n"
            + "      elem.width = value;\n"
            + "    } catch(e) { logEx(e); }\n"
            + "    log(elem.width);\n"
            + "  }\n"

            + "  var elem = document.getElementById('e1');\n"
            + "  setWidth(elem, '20px');\n"

            + "  setWidth(elem, '8');\n"
            + "  setWidth(elem, 'foo');\n"

            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
