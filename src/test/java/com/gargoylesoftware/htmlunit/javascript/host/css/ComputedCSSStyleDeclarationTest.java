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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link ComputedCSSStyleDeclaration}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 * @author Dennis Duysak
 */
@RunWith(BrowserRunner.class)
public class ComputedCSSStyleDeclarationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void cssFloat() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    var s = window.getComputedStyle(e, null);\n"
            + "    log(s.cssFloat);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Compares all {@code style} and {@code getComputedStyle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void stringProperties() throws Exception {
        final String html
            = "<html><head><body>\n"
            + "  <div id='myDiv'><br>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "  </div>\n"
            + "<script>\n"
            + "var e = document.getElementById('myDiv');\n"
            + "var array = [];\n"
            + "try {\n"
            + "  for (var i in e.style) {\n"
            + "    var s1 = e.style[i];\n"
            + "    var s2 = window.getComputedStyle(e, null)[i];\n"
            + "    if ('height' == i || 'width' == i || 'cssText' == i) {\n"
            + "      s2 = 'skipped';\n"
            + "    }\n"
            + "    if(typeof s1 == 'string')\n"
            + "      array.push(i + '=' + s1 + ':' + s2);\n"
            + "  }\n"
            + "} catch (e) { array[array.length] = 'exception'; }\n"
            + "array.sort();\n"
            + "document.getElementById('myTextarea').value = array.join('\\n');\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        final String expected = loadExpectation("ComputedCSSStyleDeclarationTest.properties", ".txt");
        final String actual = driver.findElement(By.id("myTextarea")).getAttribute("value");
        assertEquals(expected, actual);
    }

    /**
     * Compares all {@code style} and {@code getComputedStyle}, for not-attached elements.
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void stringPropertiesNotAttached() throws Exception {
        // to fix Chrome, look into ComputedCSSStyleDeclaration.defaultIfEmpty first condition
        final String html
            = "<html><head><body>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "  </div>\n"
            + "<script>\n"
            + "var e = document.createElement('div');\n"
            + "var array = [];\n"
            + "try {\n"
            + "  for (var i in e.style) {\n"
            + "    var s1 = e.style[i];\n"
            + "    var s2 = window.getComputedStyle(e, null)[i];\n"
            + "    if ('height' == i || 'width' == i || 'cssText' == i) {\n"
            + "      s2 = 'skipped';\n"
            + "    }\n"
            + "    if(typeof s1 == 'string')\n"
            + "      array.push(i + '=' + s1 + ':' + s2);\n"
            + "  }\n"
            + "} catch (e) { array[array.length] = 'exception'; }\n"
            + "array.sort();\n"
            + "document.getElementById('myTextarea').value = array.join('\\n');\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        final String expected = loadExpectation("ComputedCSSStyleDeclarationTest.properties.notAttached", ".txt");
        final String actual = driver.findElement(By.id("myTextarea")).getAttribute("value");
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "auto", "pointer"})
    public void styleElement() throws Exception {
        final String html = "<html><head>\n"
            + "<style type='text/css'>\n"
            + "  /* <![CDATA[ */\n"
            + "  #myDiv2 {cursor: pointer}\n"
            + "  /* ]]> */\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('myDiv1');\n"
            + "    var div2 = document.getElementById('myDiv2');\n"
            + "    log(div1.style.cursor);\n"
            + "    log(div2.style.cursor);\n"
            + "    log(window.getComputedStyle(div1, null).cursor);\n"
            + "    log(window.getComputedStyle(div2, null).cursor);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv1'/>\n"
            + "  <div id='myDiv2'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Some style tests. There are two points in this case:
     * <ol>
     *  <li>http://sourceforge.net/p/cssparser/bugs/17/</li>
     *  <li>the "pointer" value gets inherited by "myDiv2", which is parsed as a child of "style_test_1"</li>
     * </ol>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "pointer", "pointer"})
    public void styleElement2() throws Exception {
        final String html = "<html><head>\n"
            + "<style type='text/css'>\n"
            + "  /* <![CDATA[ */\n"
            + "  #style_test_1 {cursor: pointer}\n"
            + "  /* ]]> */\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('style_test_1');\n"
            + "    var div2 = document.getElementById('myDiv2');\n"
            + "    log(div1.style.cursor);\n"
            + "    log(div2.style.cursor);\n"
            + "    log(window.getComputedStyle(div1, null).cursor);\n"
            + "    log(window.getComputedStyle(div2, null).cursor);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='style_test_1'/>\n"
            + "  <div id='myDiv2'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"auto", "string"})
    public void zIndex() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d = document.getElementById('myDiv');\n"
            + "    var style = window.getComputedStyle(d, null);\n"
            + "    log(style.zIndex);\n"
            + "    log(typeof style.zIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("50px")
    public void styleAttributePreferredOverStylesheet() throws Exception {
        final String html = "<html>\n"
            + "<head><style>div { width: 30px; }</style></head>\n"
            + "<body>\n"
            + "<div id='d' style='width:50px'>foo</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var d = document.getElementById('d');\n"
            + "var style = window.getComputedStyle(d, null);\n"
            + "log(style.width);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px",
                   "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px"})
    public void lengthsConvertedToPixels() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d' style='width:1em; height:1em; border:1em solid black; padding:1em; margin:1em;'>d</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var d = document.getElementById('d');\n"
            + "var cs = window.getComputedStyle(d, null);\n"
            + "log(d.style.width + ' ' + cs.width);\n"
            + "log(d.style.height + ' ' + cs.height);\n"
            + "log(d.style.borderBottomWidth + ' ' + cs.borderBottomWidth);\n"
            + "log(d.style.borderLeftWidth + ' ' + cs.borderLeftWidth);\n"
            + "log(d.style.borderTopWidth + ' ' + cs.borderTopWidth);\n"
            + "log(d.style.borderRightWidth + ' ' + cs.borderRightWidth);\n"
            + "log(d.style.paddingBottom + ' ' + cs.paddingBottom);\n"
            + "log(d.style.paddingLeft + ' ' + cs.paddingLeft);\n"
            + "log(d.style.paddingTop + ' ' + cs.paddingTop);\n"
            + "log(d.style.paddingRight + ' ' + cs.paddingRight);\n"
            + "log(d.style.marginBottom + ' ' + cs.marginBottom);\n"
            + "log(d.style.marginLeft + ' ' + cs.marginLeft);\n"
            + "log(d.style.marginTop + ' ' + cs.marginTop);\n"
            + "log(d.style.marginRight + ' ' + cs.marginRight);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"inline", "inline", "inline", "block", /* "inline-block", */ "inline", "block", "block", "none"},
            FF = {"inline", "inline", "inline", "block", /* "inline-block", */ "none", "block", "block", "none"},
            FF78 = {"inline", "inline", "inline", "block", /* "inline-block", */ "none", "block", "block", "none"})
    public void defaultDisplayValues_A() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <p id='p'>\n"
            + "    <a id='a'></a>\n"
            + "    <abbr id='abbr'></abbr>\n"
            + "    <acronym id='acronym'></acronym>\n"
            + "    <address id='address'></address>\n"
            + "    <article id='article'></article>\n"
            + "    <aside id='aside'></aside>\n"
            + "    <audio id='audio'></audio>\n"
            + "  </p>\n"

            // + "  <applet id='applet'></applet>\n"

            + "  <img usemap='#imgmap'>\n"
            + "    <map name='imgmap'>\n"
            + "      <area id='area'>\n"
            + "    </map>\n"
            + "  </img>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('a');\n"
            + "    x('abbr');\n"
            + "    x('acronym');\n"
            + "    x('address');\n"
            // + "    x('applet');\n"
            + "    x('area');\n"
            + "    x('article');\n"
            + "    x('aside');\n"
            + "    x('audio');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"inline", "inline", "inline", "inline", "block", "inline", "inline-block"})
    public void defaultDisplayValues_B() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <p id='p'>\n"
            + "    <b id='b'></b>\n"
            + "    <bdi id='bdi'></bdi>\n"
            + "    <bdo id='bdo'></bdo>\n"
            + "    <big id='big'></big>\n"
            + "    <blockquote id='blockquote'></blockquote>\n"
            + "    <br id='br'>\n"
            + "    <button id='button' type='button'></button>\n"
            + "  </p>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('b');\n"
            + "    x('bdi');\n"

            + "    x('bdo');\n"
            + "    x('big');\n"
            + "    x('blockquote');\n"
            + "    x('br');\n"
            + "    x('button');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"inline", "table-caption", "block", "inline", "inline", "table-column", "table-column-group", "inline"})
    public void defaultDisplayValues_C() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <canvas id='canvas'></canvas>\n"
            + "  <center id='center'></center>\n"
            + "  <code id='code'></code>\n"

            + "  <table>\n"
            + "    <caption id='caption'></caption>\n"
            + "    <colgroup id='colgroup'>\n"
            + "      <col id='col'>\n"
            + "    </colgroup>\n"
            + "  </table>\n"

            + "  <p id='p'>\n"
            + "    <cite id='cite'></cite>\n"
            + "  </p>\n"

            + "  <menu>\n"
            + "    <command id='command'></command>\n"
            + "  </menu>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('canvas');\n"
            + "    x('caption');\n"
            + "    x('center');\n"
            + "    x('cite');\n"
            + "    x('code');\n"
            + "    x('col');\n"
            + "    x('colgroup');\n"
            + "    x('command');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"none", "block", "inline", "block", "inline", "none", "block", "block", "block", "block"},
            IE = {"none", "block", "inline", "inline", "inline", "inline", "block", "block", "block", "block"})
    public void defaultDisplayValues_D() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <datalist id='datalist'></datalist>\n"

            + "  <dl id='dl'>\n"
            + "    <dt id='dt'></dt>\n"
            + "      <dd id='dd'><dd>\n"
            + "  </dl>\n"

            + "  <p id='p'>\n"
            + "    <del id='del'></del>\n"
            + "  </p>\n"

            + "  <details id='details'></details>\n"
            + "  <dfn id='dfn'></dfn>\n"
            + "  <dialog id='dialog'></dialog>\n"
            + "  <dir id='dir'></dir>\n"
            + "  <dir id='div'></div>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('datalist');\n"
            + "    x('dd');\n"
            + "    x('del');\n"
            + "    x('details');\n"
            + "    x('dfn');\n"
            + "    x('dialog');\n"
            + "    x('dir');\n"
            + "    x('div');\n"
            + "    x('dl');\n"
            + "    x('dt');\n"

            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"inline", "inline"})
    public void defaultDisplayValues_E() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <p id='p'>\n"
            + "    <em id='em'></em>\n"
            + "  </p>\n"

            + "  <embed id='embed'>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('em');\n"
            + "    x('embed');\n"

            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"block", "block", "block", "inline", "block", "block"})
    public void defaultDisplayValues_F() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <form id='form'>\n"
            + "    <fieldset id='fieldset'></fieldset>\n"
            + "  </form>\n"

            + "  <figure id='figure'>\n"
            + "    <figcaption id='figcaption'></figcaption>\n"
            + "  </figure>\n"

            + "  <p id='p'>\n"
            + "    <font id='font'></font>\n"
            + "  </p>\n"

            + "  <footer id='footer'></footer>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('fieldset');\n"
            + "    x('figcaption');\n"
            + "    x('figure');\n"
            + "    x('font');\n"
            + "    x('footer');\n"
            + "    x('form');\n"

            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"block", "block", "block", "block", "block", "block", "block", "block"})
    public void defaultDisplayValues_H() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <h1 id='h1'></h1>\n"
            + "  <h2 id='h2'></h2>\n"
            + "  <h3 id='h3'></h3>\n"
            + "  <h4 id='h4'></h4>\n"
            + "  <h5 id='h5'></h5>\n"
            + "  <h6 id='h6'></h6>\n"

            + "  <header id='header'></header>\n"
            + "  <hr id='hr'>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('h1');\n"
            + "    x('h2');\n"
            + "    x('h3');\n"
            + "    x('h4');\n"
            + "    x('h5');\n"
            + "    x('h6');\n"
            + "    x('header');\n"
            + "    x('hr');\n"

            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"inline", "inline", "inline", "inline-block", "inline-block",
                    "inline-block", "inline-block", "inline-block", "inline-block", "inline"})
    public void defaultDisplayValues_I() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <p id='p'>\n"
            + "    <i id='i'></i>\n"
            + "    <ins id='ins'></ins>\n"
            + "  </p>\n"

            + "  <iframe id='iframe'></iframe>\n"
            + "  <img id='img'></img>\n"

            + "  <form id='form'>\n"
            + "    <input id='submit' type='submit'>\n"
            + "    <input id='reset' type='reset'>\n"
            + "    <input id='text' type='text'>\n"
            + "    <input id='password' type='password'>\n"
            + "    <input id='checkbox' type='checkbox'>\n"
            + "    <input id='radio' type='radio'>\n"
            + "  </form>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('i');\n"
            + "    x('iframe');\n"
            + "    x('img');\n"

            + "    x('submit');\n"
            + "    x('reset');\n"
            + "    x('text');\n"
            + "    x('password');\n"
            + "    x('checkbox');\n"
            + "    x('radio');\n"

            + "    x('ins');\n"

            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "inline", "inline", "inline", "block", "list-item" },
            IE = {"inline", "inline", "inline", "inline", "list-item"})
    public void defaultDisplayValues_KL() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <p id='p'>\n"
            + "    <kbd id='kbd'></kbd>\n"
            + "  </p>\n"

            + "  <ol>\n"
            + "    <li id='li'></li>\n"
            + "  </ol>\n"

            + "  <form id='form'>\n"
            + "    <keygen id='keygen'>\n"
            + "    <label id='label'>\n"
            + "    <fieldset id='fieldset'>\n"
            + "      <legend id='legend'></legend>\n"
            + "    </fieldset>\n"
            + "  </form>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('kbd');\n"
            + "    x('keygen');\n"

            + "    x('label');\n"
            + "    x('legend');\n"
            + "    x('li');\n"

            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"inline", "inline", "block", "inline-block"},
            IE = {"inline", "inline", "block", "inline"})
    public void defaultDisplayValues_M() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <img usemap='#imgmap'>\n"
            + "    <map id='map' name='imgmap'>\n"
            + "      <area id='area'>\n"
            + "    </map>\n"
            + "  </img>\n"

            + "  <p id='p'>\n"
            + "    <mark id='mark'></mark>\n"
            + "  </p>\n"

            + "  <menu id='menu'>\n"
            + "    <li id='li'></li>\n"
            + "  </menu>\n"

            + "  <meter id='meter'></meter>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('map');\n"
            + "    x('mark');\n"
            + "    x('menu');\n"
            + "    x('meter');\n"

            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"block", "none", "inline", "block", "block", "block", "inline"},
            CHROME = {"block", "inline", "inline", "block", "block", "block", "inline"},
            EDGE = {"block", "inline", "inline", "block", "block", "block", "inline"},
            IE = {"block", "none", "inline", "block", "inline", "inline", "inline"})
    public void defaultDisplayValues_NO() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <nav id='nav'>\n"
            + "    <a id='a'></a>\n"
            + "  </nav>\n"

            + "  <noscript id='noscript'></noscript> \n"

            + "  <object id='object'></object> "
            + "  <ol id='ol'>\n"
            + "    <li></li>\n"
            + "  </ol>\n"

            + "  <form>\n"
            + "    <select>\n"
            + "      <optgroup id='optgroup'>\n"
            + "        <option></option>\n"
            + "      </optgroup>\n"
            + "      <option id='option'></option>\n"
            + "    </select>\n"
            + "    <output id='output'></output>\n"
            + "  </form>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('nav');\n"
            + "    x('noscript');\n"

            + "    x('object');\n"
            + "    x('ol');\n"
            + "    x('optgroup');\n"
            + "    x('option');\n"
            + "    x('output');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"block", "none", "block", "inline-block", "inline"},
            IE = {"block", "inline", "block", "inline", "inline"})
    public void defaultDisplayValues_PQ() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <p id='p'><q id='q'></q></p>\n"

            + "  <object>\n"
            + "    <param id='param' name='movie' value=''></param>\n"
            + "  </object> "

            + "  <pre id='pre'></pre>\n"
            + "  <progress id='progress'></progress>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('p');\n"
            + "    x('param');\n"
            + "    x('pre');\n"
            + "    x('progress');\n"

            + "    x('q');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"inline", "block", "none"},
            FF = {"ruby", "ruby-text", "none"},
            FF78 = {"ruby", "ruby-text", "none"},
            IE = {"ruby", "ruby-text", "inline"})
    public void defaultDisplayValues_R() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <ruby id='ruby'>\n"
            + "    <rt id='rt'>\n"
            + "      <rp id='rp'></rp>\n"
            + "    </rt>\n"
            + "  </ruby> \n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('ruby');\n"
            + "    x('rt');\n"
            + "    x('rp');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"inline", "inline", "none", "block", "inline-block", "inline",
                        "inline", "inline", "inline", "inline", "inline", "block", "inline"},
            IE = {"inline", "inline", "none", "block", "inline-block", "inline",
                        "inline", "inline", "inline", "inline", "inline", "inline", "inline"})
    public void defaultDisplayValues_S() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <p>\n"
            + "    <s id='s'></s>\n"
            + "    <small id='small'></small>\n"
            + "    <span id='span'></span>\n"
            + "    <strike id='strike'></strike>\n"
            + "    <strong id='strong'></strong>\n"
            + "    <sub id='sub'></sub>\n"
            + "    <sup id='sup'></sup>\n"
            + "  </p> \n"

            + "  <samp id='samp'></samp>\n"
            + "  <section id='section'></section>\n"
            + "  <summary id='summary'></summary>\n"

            + "  <audio>\n"
            + "    <source id='source'>\n"
            + "  </audio>\n"

            + "  <form>\n"
            + "    <select id='select'>\n"
            + "      <option></option>\n"
            + "    </select>\n"
            + "  </form>\n"

            + "  <script id='script'>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('s');\n"
            + "    x('samp');\n"
            + "    x('script');\n"
            + "    x('section');\n"
            + "    x('select');\n"
            + "    x('small');\n"
            + "    x('source');\n"
            + "    x('span');\n"
            + "    x('strike');\n"
            + "    x('strong');\n"
            + "    x('sub');\n"
            + "    x('summary');\n"
            + "    x('sup');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"table", "table-row-group", "table-cell", "inline-block", "table-footer-group",
                    "table-cell", "table-header-group", "inline", "table-row", "inline", "inline"},
            FF = {"table", "table-row-group", "table-cell", "inline", "table-footer-group",
                    "table-cell", "table-header-group", "inline", "table-row", "inline", "inline"},
            FF78 = {"table", "table-row-group", "table-cell", "inline", "table-footer-group",
                    "table-cell", "table-header-group", "inline", "table-row", "inline", "inline"})
    public void defaultDisplayValues_T() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <table id='table'>\n"
            + "    <thead id='thead'><tr id='tr'><th id='th'>header</th></tr></thead>\n"
            + "    <tfoot id='tfoot'><tr><td>footer</td></tr></tfoot>\n"
            + "    <tbody id='tbody'><tr><td id='td'>body</td></tr></tbody>\n"
            + "  </table>\n"

            + "  <form>\n"
            + "    <textarea id='textarea'></textarea>\n"
            + "  </form>\n"

            + "  <p>\n"
            + "    <time id='time'></time>\n"
            + "    <tt id='tt'></tt>\n"
            + "  </p> \n"

            + "  <video>\n"
            + "    <track id='track'>\n"
            + "  </video>\n"

            + "  <script id='script'>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('table');\n"
            + "    x('tbody');\n"
            + "    x('td');\n"
            + "    x('textarea');\n"
            + "    x('tfoot');\n"
            + "    x('th');\n"
            + "    x('thead');\n"
            + "    x('time');\n"
            + "    x('tr');\n"
            + "    x('track');\n"
            + "    x('tt');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"inline", "block", "inline", "inline", "inline"})
    public void defaultDisplayValues_UVW() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <p>\n"
            + "    <u id='u'></u>\n"
            + "    <wbr id='wbr'></wbr>\n"
            + "  </p> \n"

            + "  <ul id='ul'>\n"
            + "    <li></li>\n"
            + "  </ul>\n"

            + "  <var id='var'></var>\n"
            + "  <video id='video'></video>\n"

            + "  <script id='script'>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      log(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('u');\n"
            + "    x('ul');\n"
            + "    x('var');\n"
            + "    x('video');\n"
            + "    x('wbr');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"rgba(0, 0, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 255, 255)"},
            IE = {"transparent", "rgb(255, 0, 0)", "rgb(255, 255, 255)"})
    public void backgroundColor() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d0'>div 0</div>\n"
            + "<div id='d1' style='background: red'>d</div>\n"
            + "<div id='d2' style='background: white url(http://htmlunit.sf.net/foo.png) repeat-x fixed top right'>\n"
            + "second div</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function getStyle(x) {\n"
            + "  var d = document.getElementById(x);\n"
            + "  var cs = window.getComputedStyle(d, null);\n"
            + "  return cs;\n"
            + "}\n"
            + "var cs0 = getStyle('d0');\n"
            + "log(cs0.backgroundColor);\n"
            + "var cs1 = getStyle('d1');\n"
            + "log(cs1.backgroundColor);\n"
            + "var cs2 = getStyle('d2');\n"
            + "log(cs2.backgroundColor);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10px")
    public void fontSize() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d0' style='font-size: 10px;'>\n"
            + "<div id='d1'>inside</div>\n"
            + "</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function getStyle(x) {\n"
            + "  var d = document.getElementById(x);\n"
            + "  var cs = window.getComputedStyle(d, null);\n"
            + "  return cs;\n"
            + "}\n"
            + "var cs1 = getStyle('d1');\n"
            + "log(cs1.fontSize);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9.6px")
    @HtmlUnitNYI(CHROME = "10px",
            EDGE =  "10px",
            FF = "10px",
            FF78 = "10px",
            IE = "10px")
    public void fontSize2() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d0' style='font-size: 0.6em;'>\n"
            + "<div id='d1'>inside</div>\n"
            + "</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function getStyle(x) {\n"
            + "    var d = document.getElementById(x);\n"
            + "    var cs = window.getComputedStyle(d, null);\n"
            + "    return cs;\n"
            + "  }\n"
            + "  var cs1 = getStyle('d1');\n"
            + "  log(cs1.fontSize);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "4.11667px",
            CHROME = "3.824px",
            EDGE = "3.836px",
            IE = "0px")
    @HtmlUnitNYI(CHROME = "1px",
            EDGE =  "1px",
            FF = "1px",
            FF78 = "1px",
            IE = "1px")
    public void fontSizeVH() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d0' style='font-size: 0.6vh;'>\n"
            + "<div id='d1'>inside</div>\n"
            + "</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function getStyle(x) {\n"
            + "    var d = document.getElementById(x);\n"
            + "    var cs = window.getComputedStyle(d, null);\n"
            + "    return cs;\n"
            + "  }\n"
            + "  var cs1 = getStyle('d1');\n"
            + "  log(cs1.fontSize);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "7.55px",
            CHROME = "7.548px",
            EDGE = "7.548px",
            IE = "0px")
    @HtmlUnitNYI(CHROME = "1px",
            EDGE =  "1px",
            FF = "1px",
            FF78 = "1px",
            IE = "1px")
    public void fontSizeVW() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d0' style='font-size: 0.6vw;'>\n"
            + "<div id='d1'>inside</div>\n"
            + "</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function getStyle(x) {\n"
            + "    var d = document.getElementById(x);\n"
            + "    var cs = window.getComputedStyle(d, null);\n"
            + "    return cs;\n"
            + "  }\n"
            + "  var cs1 = getStyle('d1');\n"
            + "  log(cs1.fontSize);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"111px", "auto"})
    @NotYetImplemented
    public void computedWidthOfHiddenElements() throws Exception {
        final String content = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('myDiv1');\n"
            + "    var cs1 = window.getComputedStyle(div1, null);\n"
            + "    log(cs1.width);\n"
            + "    var div2 = document.getElementById('myDiv2');\n"
            + "    var cs2 = window.getComputedStyle(div2, null);\n"
            + "    log(cs2.width);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div style='width: 111px'>\n"
            + "    <div id='myDiv1'></div>\n"
            + "    <div id='myDiv2' style='display:none'/>\n"
            + "  </div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(content);
    }

    /**
     * Verifies that at least one CSS attribute is correctly inherited by default.
     * Required by the MochiKit tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({",", "separate,separate", "collapse,", "collapse,collapse"})
    public void inheritedImplicitly() throws Exception {
        final String html
            = "<html><body><table id='a'><tr id='b'><td>a</td></tr></table><script>\n"
            + LOG_TITLE_FUNCTION
            + "var a = document.getElementById('a');\n"
            + "var b = document.getElementById('b');\n"
            + "var as = a.style;\n"
            + "var bs = b.style;\n"
            + "var acs = window.getComputedStyle(a, null);\n"
            + "var bcs = window.getComputedStyle(b, null);\n"
            + "log(as.borderCollapse + ',' + bs.borderCollapse);\n"
            + "log(acs.borderCollapse + ',' + bcs.borderCollapse);\n"
            + "as.borderCollapse = 'collapse';\n"
            + "log(as.borderCollapse + ',' + bs.borderCollapse);\n"
            + "log(acs.borderCollapse + ',' + bcs.borderCollapse);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that when the class of an ancestor node matters for the effective style,
     * it is recomputed if the class of the ancestor node changes.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"underline", "none", "underline"},
            CHROME = { "underline solid rgb(0, 0, 0)", "none solid rgb(0, 0, 0)", "underline solid rgb(0, 0, 0)"},
            EDGE = { "underline solid rgb(0, 0, 0)", "none solid rgb(0, 0, 0)", "underline solid rgb(0, 0, 0)"},
            FF = {"underline rgb(0, 0, 0)", "rgb(0, 0, 0)", "underline rgb(0, 0, 0)"},
            FF78 = {"underline rgb(0, 0, 0)", "rgb(0, 0, 0)", "underline rgb(0, 0, 0)"})
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void changeInParentClassNodeReferencedByRule() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function readDecoration(id) {\n"
            + "  var e = document.getElementById(id);\n"
            + "  var s = window.getComputedStyle(e, null);\n"
            + "  log(s.textDecoration);\n"
            + "}\n"
            + "function test() {\n"
            + "  var fooA = document.getElementById('fooA');\n"
            + "  readDecoration('fooB');\n"
            + "  fooA.setAttribute('class', '');\n"
            + "  readDecoration('fooB');\n"
            + "  fooA.setAttribute('class', 'A');\n"
            + "  readDecoration('fooB');\n"
            + "}\n"
            + "</script>\n"
            + "<style>\n"
            + ".A .B { text-decoration: underline }\n"
            + "</style>\n"
            + "</head><body onload='test()'>\n"
            + "<div class='A' id='fooA'>A\n"
            + "<div class='B' id='fooB'>B</div></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"200px,400px", "200,400", "200px,400px", "50%,25%", "100,100", "100px,100px"})
    public void widthAndHeightPercentagesAndPx() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:200px;height:400px'><div id='d2' style='width:50%;height:25%'></div></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('d1');\n"
            + "    var s1 = window.getComputedStyle(d1, null);\n"
            + "    var d2 = document.getElementById('d2');\n"
            + "    var s2 = window.getComputedStyle(d2, null);\n"
            + "    log(d1.style.width + ',' + d1.style.height);\n"
            + "    log(d1.offsetWidth + ',' + d1.offsetHeight);\n"
            + "    log(s1.width + ',' + s1.height);\n"
            + "    log(d2.style.width + ',' + d2.style.height);\n"
            + "    log(d2.offsetWidth + ',' + d2.offsetHeight);\n"
            + "    log(s2.width + ',' + s2.height);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"10em,20em", "160,320", "160px,320px", "50%,25%", "80,80", "80px,80px"})
    public void widthAndHeightPercentagesAndEm() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:10em;height:20em'><div id='d2' style='width:50%;height:25%'></div></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('d1');\n"
            + "    var s1 = window.getComputedStyle(d1, null);\n"
            + "    var d2 = document.getElementById('d2');\n"
            + "    var s2 = window.getComputedStyle(d2, null);\n"
            + "    log(d1.style.width + ',' + d1.style.height);\n"
            + "    log(d1.offsetWidth + ',' + d1.offsetHeight);\n"
            + "    log(s1.width + ',' + s1.height);\n"
            + "    log(d2.style.width + ',' + d2.style.height);\n"
            + "    log(d2.offsetWidth + ',' + d2.offsetHeight);\n"
            + "    log(s2.width + ',' + s2.height);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * NullPointerException occurred in offsetX computation in HtmlUnit-2.7-SNAPSHOT (19.01.2010).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    public void widthAndHeightPercentagesHTML() throws Exception {
        final String html = "<html style='height: 100%'>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var h = document.documentElement;\n"
            + "  log(h.offsetWidth > 0);\n"
            + "  log(h.offsetHeight > 0);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true", "true", "true", "true",
                "true", "true", "true", "true", "false", "false",
                "true", "true", "true", "true"})
    public void widthAndHeightInputElements() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <form id='form'>\n"
            + "    <input id='submit' type='submit'>\n"
            + "    <input id='reset' type='reset'>\n"
            + "    <input id='text' type='text'>\n"
            + "    <input id='password' type='password'>\n"
            + "    <input id='checkbox' type='checkbox'>\n"
            + "    <input id='radio' type='radio'>\n"
            + "    <input id='hidden' type='hidden'>\n"
            + "    <button id='button' type='button'></button>\n"
            + "    <textarea id='myTextarea'></textarea>\n"
            + "  </form>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      log(e.offsetWidth > 0);\n"
            + "      log(e.offsetHeight > 0);\n"
            + "    }\n"
            + "  </script>\n"

            + "  <script>\n"
            + "    x('submit');\n"
            + "    x('reset');\n"
            + "    x('text');\n"
            + "    x('password');\n"
            + "    x('checkbox');\n"
            + "    x('radio');\n"
            + "    x('hidden');\n"
            + "    x('button');\n"
            + "    x('myTextarea');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            IE = {"auto", "auto"})
    public void widthAndHeightDisconnected() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var e = document.createElement('div');\n"
            + "      var style = window.getComputedStyle(e, null);\n"
            + "      log(style.width);\n"
            + "      log(style.height);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "true", "false", "false", "true", "true", "true", "false"})
    public void widthAuto() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      tester(document.body);\n"
                + "      tester(document.getElementById('div'));\n"
                + "    }\n"
                + "    function tester(el) {\n"
                + "      log(el.style.width == 'auto');\n"
                + "      log(el.clientWidth > 100);\n"
                + "      log(el.offsetWidth > 100);\n"

                + "      var style = window.getComputedStyle(el, null);\n"
                + "      log(/\\d+px/.test(style.width));\n"
                + "      log(style.width == 'auto');\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body style='width: auto' onload='test();'>\n"
                + "<div id='div'></div>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "rgb(0, 0, 255)"})
    @NotYetImplemented
    public void getPropertyValue() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    var d = document.getElementById('div1');\n"
            + "    var s = window.getComputedStyle(d, null);\n"
            + "    log(s.getPropertyValue('test'));\n"
            + "    log(s.getPropertyValue('color'));\n"
            + "  } catch (e) { log('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "<style>#div1 { test: red }</style>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: blue'>foo</div></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"roman", "swiss", "roman"})
    public void handleImportant() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    alertFF(document.getElementById('div1'));\n"
            + "    alertFF(document.getElementById('div2'));\n"
            + "    alertFF(document.getElementById('div3'));\n"
            + "  }\n"
            + "  function alertFF(e) {\n"
            + "    var s = window.getComputedStyle(e, null);\n"
            + "    log(s.getPropertyValue('font-family'));\n"
            + "  }\n"
            + "</script>\n"
            + "  <style>#div1 { font-family: swiss }</style>\n"
            + "  <style>#div2 { font-family: swiss !important }</style>\n"
            + "  <style>#div3 { font-family: swiss !important }</style>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <div id='div1' style='font-family: roman'>foo</div>\n"
            + "  <div id='div2' style='font-family: roman'>foo</div>\n"
            + "  <div id='div3' style='font-family: roman !important'>foo</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void offsetHeight_empty_tag() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('div1').offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void offsetHeight_empty() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('div1').offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "</body></html>";
        loadPage2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void offsetHeight_displayNone() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('div1').offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' style='display: none'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("18")
    public void offsetHeight_with_content() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('div1').offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'>foo</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("18")
    public void offsetHeight_with_child() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('div1').offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'><div>foo</div></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("81")
    @NotYetImplemented
    public void offsetHeight_with_childHeight() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('div1').offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'><iframe height='77'>foo</iframe></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void offsetHeight_setting_height() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "  .v-loading-indicator {\n"
            + "    height: 100%\n"
            + "  }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    log(div1.offsetHeight == 0);\n"
            + "    div1.className = 'v-loading-indicator';\n"
            + "    log(div1.offsetHeight == 0);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void scrollbarWidth() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var scroller = document.createElement('div');\n"
            + "    scroller.style['width'] = '50px';\n"
            + "    scroller.style['height'] = '50px';\n"
            + "    scroller.style['overflow'] = 'scroll';\n"
            + "    log(scroller.offsetWidth - scroller.clientWidth == 0);\n"
            + "    document.body.appendChild(scroller);\n"
            + "    log(scroller.offsetWidth - scroller.clientWidth == 0);\n"
            + "    document.body.removeChild(scroller);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void scrollbarHeight() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var scroller = document.createElement('div');\n"
            + "    scroller.style['width'] = '50px';\n"
            + "    scroller.style['height'] = '50px';\n"
            + "    scroller.style['overflow'] = 'scroll';\n"
            + "    log(scroller.offsetHeight - scroller.clientHeight == 0);\n"
            + "    document.body.appendChild(scroller);\n"
            + "    log(scroller.offsetHeight - scroller.clientHeight == 0);\n"
            + "    document.body.removeChild(scroller);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "10", "10", "rgb(0, 128, 0)"})
    public void zIndexComputed() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "  .abc { z-index: 10; color:green }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    log(div.style.zIndex);\n"
            + "    log(div.style['z-index']);\n"
            + "    log(div.style.color);\n"
            + "    log(window.getComputedStyle(div, '').zIndex);\n"
            + "    log(window.getComputedStyle(div, '')['z-index']);\n"
            + "    log(window.getComputedStyle(div, '').color);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' class='abc'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "0", "0", "0", "", "", "", "", "", "", "", "",
                    "104", "104", "104", "104", "auto", "100px", "100px", "3px", "block", "content-box", "0px", "0px"},
            IE = {"0", "0", "0", "0", "auto", "100px", "100px", "3px", "block", "content-box", "0px", "0px",
                    "104", "104", "104", "104", "auto", "100px", "100px", "3px", "block", "content-box", "0px", "0px"})
    public void offsetWidth() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    div.style.width = '100px';\n"
            + "    div.style.height = '100px';\n"
            + "    div.style.padding = '2px';\n"
            + "    div.style.margin = '3px';\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    log(div.offsetWidth);\n"
            + "    log(div.offsetHeight);\n"
            + "    log(div.clientWidth);\n"
            + "    log(div.clientHeight);\n"
            + "    log(style.top);\n"
            + "    log(style.width);\n"
            + "    log(style.height);\n"
            + "    log(style.marginRight);\n"
            + "    log(style.display);\n"
            + "    log(style.boxSizing);\n"
            + "    log(style.borderRightWidth);\n"
            + "    log(style.borderLeftWidth);\n"
            + "    document.body.appendChild(div);\n"
            + "    log(div.offsetWidth);\n"
            + "    log(div.offsetHeight);\n"
            + "    log(div.clientWidth);\n"
            + "    log(div.clientHeight);\n"
            + "    log(style.top);\n"
            + "    log(style.width);\n"
            + "    log(style.height);\n"
            + "    log(style.marginRight);\n"
            + "    log(style.display);\n"
            + "    log(style.boxSizing);\n"
            + "    log(style.borderRightWidth);\n"
            + "    log(style.borderLeftWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "", "left", "left", "right", "right"},
            IE = {"undefined", "left", "undefined", "left", "undefined", "right"})
    @NotYetImplemented(IE)
    public void cssFloat2() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "  .abc { float: right }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    div.style.float = 'left';\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    log(style.float);\n"
            + "    log(style.cssFloat);\n"
            + "    document.body.appendChild(div);\n"
            + "    log(style.float);\n"
            + "    log(style.cssFloat);\n"
            + "    div = document.getElementById('mydiv');\n"
            + "    style = window.getComputedStyle(div, null);\n"
            + "    log(style.float);\n"
            + "    log(style.cssFloat);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv' class='abc'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void offsetTopTableRows() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <table>\n"
            + "    <tr id='row1'><td>row1</td></tr>\n"
            + "    <tr id='row2'><td>row2</td></tr>\n"
            + "  </table>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var r1 = document.getElementById('row1');\n"
            + "  var r2 = document.getElementById('row2');\n"
            + "  log(r2.offsetTop > r1.offsetTop);\n"
            + "</script>\n"

            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void offsetTopListItems() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul>\n"
            + "    <li id='li1'>row1</li>\n"
            + "    <li id='li2'>row2</li>\n"
            + "  </ul>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var li1 = document.getElementById('li1');\n"
            + "  var li2 = document.getElementById('li2');\n"
            + "  log(li2.offsetTop > li1.offsetTop);\n"
            + "</script>\n"

            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void offsetLeftAfterTable() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <table>\n"
            + "    <tr><td>abcdefghijklmnopqrstuvwxyz</td></tr>\n"
            + "  </table>\n"
            + "  <div id='mydiv'>Heho</div>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d1 = document.getElementById('mydiv');\n"
            + "  log(d1.offsetLeft < 10);\n"
            + "</script>\n"

            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = "1")
    public void custom() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "  .abc { xyz: 1 }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    log(style.xyz);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv' class='abc'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void selector() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.querySelectorAll('div *').length);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'>\n"
            + "    <p>p</p>\n"
            + "  </div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "0px", "20%", "80px", "25%", "100px"},
            FF = {"", "0px", "20%", "360px", "25%", "100px"},
            FF78 = {"", "0px", "20%", "360px", "25%", "100px"})
    @NotYetImplemented({FF, FF78})
    public void marginLeftRight() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    var container = document.createElement('div');\n"
            + "    container.style.width = '400px';\n"
            + "    div1.appendChild(container);\n"
            + "    log(container.style.marginRight);\n"
            + "    log(window.getComputedStyle(container, null).marginRight);\n"
            + "\n"
            + "    var el = document.createElement('div');\n"
            + "    el.style.width = '10%';\n"
            + "    el.style.marginRight = '20%';\n"
            + "    container.appendChild(el);\n"
            + "    log(el.style.marginRight);\n"
            + "    log(window.getComputedStyle(el, null).marginRight);\n"
            + "\n"
            + "    el = document.createElement('div');\n"
            + "    el.style.width = '30%';\n"
            + "    el.style.minWidth = '300px';\n"
            + "    el.style.marginLeft = '25%';\n"
            + "    container.appendChild(el);\n"
            + "    log(el.style.marginLeft);\n"
            + "    log(window.getComputedStyle(el, null).marginLeft);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "</body></html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "0px", "", "0px", "50%", "100px", "50%", "100px"},
            IE = {"", "auto", "", "auto", "", "auto", "", "auto"})
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void topLeft() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    var parent = document.createElement('div');\n"
            + "    parent.style = 'position: relative; width: 200px; height: 200px; margin: 0; padding: 0;"
            + " border-width: 0';\n"
            + "    div1.appendChild(parent);\n"
            + "\n"
            + "    var div = document.createElement('div');\n"
            + "    div.style = 'position: absolute; width: 20px; height: 20px; top: 50%; left: 50%';\n"
            + "    parent.appendChild(div);\n"
            + "\n"
            + "    log(parent.style.top);\n"
            + "    log(window.getComputedStyle(parent, null).top);\n"
            + "    log(parent.style.left);\n"
            + "    log(window.getComputedStyle(parent, null).left);\n"
            + "    log(div.style.top);\n"
            + "    log(window.getComputedStyle(div, null).top);\n"
            + "    log(div.style.left);\n"
            + "    log(window.getComputedStyle(div, null).left);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "</body></html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"10", "0"},
            IE = {"0", "0"})
    public void borderBoxAffectsOffsetWidth() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    var empty = getOffsetWidth('width: 300px; height: 300px;');\n"
            + "    var marginAndPadding = getOffsetWidth('width: 300px; height: 300px; margin: 3px; padding: 5px;');\n"
            + "    var withBorderBox = getOffsetWidth('width: 300px; height: 300px; margin: 3px; padding: 5px;"
            + " box-sizing: border-box;');\n"
            + "    log(marginAndPadding - empty);\n"
            + "    log(withBorderBox - empty);\n"
            + "  }\n"
            + "  function getOffsetWidth(style) {\n"
            + "    var d = document.createElement('div');\n"
            + "    d.appendChild(document.createTextNode('test'));\n"
            + "    d.style = style;\n"
            + "    div1.appendChild(d);\n"
            + "    var offsetWidth = d.offsetWidth;\n"
            + "    div1.removeChild(d);\n"
            + "    return offsetWidth;\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "</body>\n"
            + "</html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"10", "0"},
            IE = {"0", "0"})
    public void borderBoxAffectsOffsetHeight() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div1 = document.getElementById('div1');\n"

            + "    var empty = getOffsetHeight('width: 300px; height: 300px;');\n"
            + "    var marginAndPadding = getOffsetHeight('width: 300px; height: 300px; margin: 3px; padding: 5px;');\n"
            + "    var withBorderBox = getOffsetHeight('width: 300px; height: 300px; margin: 3px; padding: 5px;"
                                                            + " box-sizing: border-box;');\n"
            + "    log(marginAndPadding - empty);\n"
            + "    log(withBorderBox - empty);\n"
            + "  }\n"
            + "  function getOffsetHeight(style) {\n"
            + "    var d = document.createElement('div');\n"
            + "    d.appendChild(document.createTextNode('test'));\n"
            + "    d.style = style;\n"
            + "    div1.appendChild(d);\n"
            + "    var offsetHeight = d.offsetHeight;\n"
            + "    div1.removeChild(d);\n"
            + "    return offsetHeight;\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "</body>\n"
            + "</html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void offsetWidthWithDisplayInline() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + LOG_TITLE_FUNCTION
            + "    var div = document.createElement('div');\n"
            + "    document.body.appendChild(div);\n"
            + "    div.style.cssText = 'display: inline; margin:0; border: 0; padding: 5px; width: 7px';\n"
            + "    log(div.offsetWidth);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "100",
            IE = "true")
    public void borderBoxAffectsOffsetWidth2() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var divNormal = document.createElement('div');\n"
            + "    divNormal.style = 'box-sizing: border-box; width: 100px; height: 100px; border: 10px solid white;"
            + " padding: 2px; margin: 3px';\n"
            + "    document.body.appendChild(divNormal);\n"
            + "\n"
            + "   if (window.navigator.userAgent.indexOf('Trident/') == -1) {\n"
            + "     log(divNormal.offsetWidth);\n"
            + "   } else {\n"
            + "     log(divNormal.offsetWidth == window.innerWidth - 16);\n"
            + "   }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "100",
            IE = "0")
    public void borderBoxAffectsOffsetHeight2() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var divNormal = document.createElement('div');\n"
            + "    divNormal.style = 'box-sizing: border-box; width: 100px; height: 100px; border: 10px solid white;"
            + " padding: 2px; margin: 3px';\n"
            + "    document.body.appendChild(divNormal);\n"
            + "\n"
            + "   if (window.navigator.userAgent.indexOf('Trident/') == -1) {\n"
            + "     log(divNormal.offsetHeight);\n"
            + "   } else {\n"
            + "     log(divNormal.offsetHeight);\n"
            + "   }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "0", "16"},
            CHROME = {"8px", "0", "16"},
            EDGE = {"8px", "0", "16"})
    @NotYetImplemented({CHROME, EDGE, IE})
    public void bodyOffsetWidth() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var win = window.innerWidth;\n"
            + "    var html = document.documentElement.offsetWidth;\n"
            + "    var body = document.body.offsetWidth;\n"
            + "    log(window.getComputedStyle(document.body, null).margin);\n"
            + "    log(win - html);\n"
            + "    log(win - body);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "24"})
    @NotYetImplemented
    public void offsetHeightTable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var table = document.createElement('table');\n"
            + "    table.style.fontSize = '16px';\n"
            + "    document.getElementById('myDiv').appendChild(table);\n"
            + "    log(table.offsetHeight);\n"
            + "\n"
            + "    var tr = document.createElement('tr');\n"
            + "    table.appendChild(tr);\n"
            + "    var td = document.createElement('td');\n"
            + "    tr.appendChild(td);\n"
            + "    td.appendChild(document.createTextNode('DATA'));\n"
            + "    log(table.offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false"})
    public void height() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <style>\n"
            + "    .autoheight {\n"
            + "      height: auto;\n"
            + "    }\n"
            + "  </style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    log(style.height == '0px');\n"
            + "    div.className = 'autoheight';\n"
            + "    log(style.height == '0px');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>A</div>\n"
            + "</body></html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"18", "18px", "36", "36px", "54", "54px"},
            IE = {"18", "18.4px", "37", "36.8px", "55", "55.2px"})
    @NotYetImplemented(IE)
    public void heightManyLines() throws Exception {
        final String html = "<html>\n"
            + "<head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('test1');\n"
            + "    log(div.offsetHeight);\n"
            + "    log(window.getComputedStyle(div, null).height);\n"
            + "    div = document.getElementById('test2');\n"
            + "    log(div.offsetHeight);\n"
            + "    log(window.getComputedStyle(div, null).height);\n"
            + "    div = document.getElementById('test3');\n"
            + "    log(div.offsetHeight);\n"
            + "    log(window.getComputedStyle(div, null).height);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id=\"test1\">This is a long string of text.</div>\n"
            + "  <div id=\"test2\">This is a long string of text.<br>Some more text<br></div>\n"
            + "  <div id=\"test3\">This is a long string of text.<br>Some more text<br>and some more...</div>\n"
            + "</body></html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0 17")
    @HtmlUnitNYI(CHROME = "0 0",
            EDGE = "0 0",
            FF = "0 0",
            FF78 = "0 0",
            IE = "0 0")
    public void iFrameInnerWidth() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var iframe = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe);\n"
            + "    iframe.style.cssText = 'width: 500px; height: 500px;';\n"
            + "    iframe.contentWindow.location = 'test2.html';\n"
            + "    var win = iframe.contentWindow;\n"
            + "    document.title += ' ' + (win.innerWidth - win.document.documentElement.clientWidth);\n"
            + "    iframe.onload = function() {\n"
            + "      document.title += ' ' + (win.innerWidth - win.document.documentElement.clientWidth);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>\n";
        getMockWebConnection().setDefaultResponse("<html><head>\n"
                + "<style>\n"
                + "  body {\n"
                + "    width: 600px;\n"
                + "    height: 600px;\n"
                + "  }\n"
                + "</style>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>");

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "auto" })
    @NotYetImplemented
    public void getHeightInvisible() throws Exception {
        final String html = "<html><head>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "  function test() {\n"
              + "    var node = document.getElementById('outer');\n"
              + "    var style = node.style;\n"
              + "    log(style.height);\n"
              + "    var style = window.getComputedStyle(node, null);\n"
              + "    log(style.height);\n" + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body onload='test()'>\n"
              + "  <div id='outer' style='display: none'>\n"
              + "    <div>line 1</div>\n"
              + "    <div>line 2</div>\n"
              + "  </div>\n"
              + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"\"@\"", "\"@\"", "\"@\"", "\"#\"", "\"#\"", "\"#\""},
            FF = {"\"@\"", "normal", "\"@\"", "\"#\"", "normal", "\"#\""},
            FF78 = {"\"@\"", "normal", "\"@\"", "\"#\"", "normal", "\"#\""},
            IE = {"\"@\"", "normal", "\"@\"", "\"#\"", "normal", "\"#\""})
    public void pseudoBefore() throws Exception {
        final String html = "<html><head>\n"
                + "<style type='text/css'>\n"
                + "  /* <![CDATA[ */\n"
                + "  #myDiv:before { content: '@' }\n"
                + "  #myDiv2::before { content: '#' }\n"
                + "  /* ]]> */\n"
                + "</style>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "  function test() {\n"
              + "    var node = document.getElementById('myDiv');\n"
              + "    var style = window.getComputedStyle(node, ':before');\n"
              + "    log(style.content);\n"
              + "    var style = window.getComputedStyle(node, 'before');\n"
              + "    log(style.content);\n"
              + "    var style = window.getComputedStyle(node, '::before');\n"
              + "    log(style.content);\n"

              + "    node = document.getElementById('myDiv2');\n"
              + "    var style = window.getComputedStyle(node, ':before');\n"
              + "    log(style.content);\n"
              + "    var style = window.getComputedStyle(node, 'before');\n"
              + "    log(style.content);\n"
              + "    var style = window.getComputedStyle(node, '::before');\n"
              + "    log(style.content);\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body onload='test()'>\n"
              + "  <div id='myDiv' >Test</div>\n"
              + "  <div id='myDiv2' >Test</div>\n"
              + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void calculateContentHeightAfterSVG() throws Exception {
        final String html =
                "<html><body>\n"
                    + "<svg/>\n"
                    + "<img />\n"
                    + "<textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
                    + "<script>\n"
                    + LOG_TITLE_FUNCTION
                    + "  log(document.body.offsetHeight > 10);\n"
                    + "</script>\n"
                    + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("131")
    public void combineStyles() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<style type='text/css'>\n"
            + "  div { margin: 10px 20px 30px 40px; }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('div1');\n"
            + "    var left = div.style.marginLeft;\n" // force the resolution
            + "    log(div.offsetLeft);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' style='margin-left: 123px'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("48")
    public void combineStylesImportant() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<style type='text/css'>\n"
            + "  div { margin: 10px 20px 30px 40px !important; }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('div1');\n"
            + "    var left = div.style.marginLeft;\n" // force the resolution
            + "    log(div.offsetLeft);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' style='margin-left: 123px'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("23")
    public void combineStylesBrowserDefaults() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<style type='text/css'>\n"
            + "  body { margin: 3px; }\n"
            + "  div { margin: 20px; }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('div1');\n"
            + "    var left = div.style.marginLeft;\n" // force the resolution
            + "    log(div.offsetLeft);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void boundingClientRectIgnoreSiblingWhitespace() throws Exception {
        final String html = "<html><body>\n"
            + "<table>\n"
            + "<tr>\n"
            + "  <td>  \n\t    <div id='a'>a</div></td>\n"
            + "</tr>\n"
            + "</table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var e = document.getElementById('a');\n"
            + "  log(e.getBoundingClientRect().left < 12);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void boundingClientRectTopOnlyHasToTakeCareOfPreviousBlockSibling() throws Exception {
        final String html = "<html><body>\n"
            + "<div style='height: 100'>100</div>\n"
            + "<span style='height: 200'>200</span>\n"
            + "<span id='tester'>tester</span>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var e = document.getElementById('tester');\n"
            + "  log(e.getBoundingClientRect().top < 120);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * See https://github.com/HtmlUnit/htmlunit/issues/173.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void offsetLeft() throws Exception {
        final String html = "<html>\n"
            + "<body style='padding:'>\n"
            + "  <div id='mydiv' >Heho</div>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d1 = document.getElementById('mydiv');\n"
            + "  log(d1.offsetLeft < 10);\n"
            + "</script>\n"

            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }
}
