/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link ComputedCSSStyleDeclaration}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
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
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    var s = window.getComputedStyle(e, null);\n"
            + "    alert(s.cssFloat);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
    @Alerts({ "", "", "auto", "pointer" })
    public void styleElement() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<style type='text/css'>\n"
            + "  /* <![CDATA[ */\n"
            + "  #myDiv2 {cursor: pointer}\n"
            + "  /* ]]> */\n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div1 = document.getElementById('myDiv1');\n"
            + "    var div2 = document.getElementById('myDiv2');\n"
            + "    alert(div1.style.cursor);\n"
            + "    alert(div2.style.cursor);\n"
            + "    alert(window.getComputedStyle(div1, null).cursor);\n"
            + "    alert(window.getComputedStyle(div2, null).cursor);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv1'/>\n"
            + "  <div id='myDiv2'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
    @Alerts({ "", "", "pointer", "pointer" })
    public void styleElement2() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<style type='text/css'>\n"
            + "  /* <![CDATA[ */\n"
            + "  #style_test_1 {cursor: pointer}\n"
            + "  /* ]]> */\n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div1 = document.getElementById('style_test_1');\n"
            + "    var div2 = document.getElementById('myDiv2');\n"
            + "    alert(div1.style.cursor);\n"
            + "    alert(div2.style.cursor);\n"
            + "    alert(window.getComputedStyle(div1, null).cursor);\n"
            + "    alert(window.getComputedStyle(div2, null).cursor);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='style_test_1'/>\n"
            + "  <div id='myDiv2'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "auto", "string" })
    public void zIndex() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var d = document.getElementById('myDiv');\n"
            + "    var style = window.getComputedStyle(d, null);\n"
            + "    alert(style.zIndex);\n"
            + "    alert(typeof style.zIndex);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "var d = document.getElementById('d');\n"
            + "var style = window.getComputedStyle(d, null);\n"
            + "alert(style.width);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px",
                   "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px", "1em 16px" })
    public void lengthsConvertedToPixels() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d' style='width:1em; height:1em; border:1em solid black; padding:1em; margin:1em;'>d</div>\n"
            + "<script>\n"
            + "var d = document.getElementById('d');\n"
            + "var cs = window.getComputedStyle(d, null);\n"
            + "alert(d.style.width + ' ' + cs.width);\n"
            + "alert(d.style.height + ' ' + cs.height);\n"
            + "alert(d.style.borderBottomWidth + ' ' + cs.borderBottomWidth);\n"
            + "alert(d.style.borderLeftWidth + ' ' + cs.borderLeftWidth);\n"
            + "alert(d.style.borderTopWidth + ' ' + cs.borderTopWidth);\n"
            + "alert(d.style.borderRightWidth + ' ' + cs.borderRightWidth);\n"
            + "alert(d.style.paddingBottom + ' ' + cs.paddingBottom);\n"
            + "alert(d.style.paddingLeft + ' ' + cs.paddingLeft);\n"
            + "alert(d.style.paddingTop + ' ' + cs.paddingTop);\n"
            + "alert(d.style.paddingRight + ' ' + cs.paddingRight);\n"
            + "alert(d.style.marginBottom + ' ' + cs.marginBottom);\n"
            + "alert(d.style.marginLeft + ' ' + cs.marginLeft);\n"
            + "alert(d.style.marginTop + ' ' + cs.marginTop);\n"
            + "alert(d.style.marginRight + ' ' + cs.marginRight);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "inline", "inline", "inline", "block", /* "inline-block", */ "none", "block", "block", "none" },
            CHROME = { "inline", "inline", "inline", "block", /* "inline-block", */ "inline", "block",
                            "block", "none" },
            IE = { "inline", "inline", "inline", "block", /* "inline-block", */ "inline", "block", "block", "none" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "inline", "inline", "inline", "inline", "block", "inline", "inline-block" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "inline", "table-caption", "block", "inline", "inline", "table-column", "table-column-group", "inline" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "none", "block", "inline", "inline", "inline", "inline", "block", "block", "block", "block" },
            CHROME = { "none", "block", "inline", "block", "inline", "none", "block", "block", "block", "block" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "inline", "inline" })
    public void defaultDisplayValues_E() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <p id='p'>\n"
            + "    <em id='em'></em>\n"
            + "  </p>\n"

            + "  <embed id='embed'>\n"

            + "  <script>\n"
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('em');\n"
            + "    x('embed');\n"

            + "  </script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "block", "block", "block", "inline", "block", "block" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "block", "block", "block", "block", "block", "block", "block", "block" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "inline", "inline", "inline", "inline-block", "inline-block",
                    "inline-block", "inline-block", "inline-block", "inline-block", "inline" },
            FF = { "inline", "inline", "inline", "inline", "inline", "inline",
                    "inline", "inline", "inline", "inline" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "inline", "inline-block", "inline", "block", "list-item" },
            IE = { "inline", "inline", "inline", "inline", "list-item" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "inline", "inline", "block", "inline-block" },
            IE = { "inline", "inline", "block", "inline" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('map');\n"
            + "    x('mark');\n"
            + "    x('menu');\n"
            + "    x('meter');\n"

            + "  </script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "block", "none", "inline", "block", "block", "block", "inline" },
            CHROME = { "block", "inline", "inline", "block", "block", "block", "inline" },
            IE = { "block", "none", "inline", "block", "inline", "inline", "inline" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "block", "none", "block", "inline-block", "inline" },
            IE = { "block", "inline", "block", "inline", "inline" })
    public void defaultDisplayValues_PQ() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <p id='p'><q id='q'></q></p>\n"

            + "  <object>\n"
            + "    <param id='param' name='movie' value=''></param>\n"
            + "  </object> "

            + "  <pre id='pre'></pre>\n"
            + "  <progress id='progress'></progress>\n"

            + "  <script>\n"
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "inline", "inline", "inline" },
            CHROME = { "inline", "block", "none" },
            FF = { "ruby", "ruby-text", "none" },
            IE = { "ruby", "ruby-text", "inline" })
    public void defaultDisplayValues_R() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><body>\n"
            + "  <ruby id='ruby'>\n"
            + "    <rt id='rt'>\n"
            + "      <rp id='rp'></rp>\n"
            + "    </rt>\n"
            + "  </ruby> \n"

            + "  <script>\n"
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
            + "    }\n"
            + "  </script>\n"
            + "  <script>\n"
            + "    x('ruby');\n"
            + "    x('rt');\n"
            + "    x('rp');\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "inline", "inline", "none", "block", "inline-block", "inline",
                        "inline", "inline", "inline", "inline", "inline", "inline", "inline" },
            CHROME = { "inline", "inline", "none", "block", "inline-block", "inline",
                        "inline", "inline", "inline", "inline", "inline", "block", "inline" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "table", "table-row-group", "table-cell", "inline", "table-footer-group",
                        "table-cell", "table-header-group", "inline", "table-row", "inline", "inline" },
            CHROME = { "table", "table-row-group", "table-cell", "inline-block", "table-footer-group",
                    "table-cell", "table-header-group", "inline", "table-row", "inline", "inline" },
            IE = { "table", "table-row-group", "table-cell", "inline-block", "table-footer-group",
                        "table-cell", "table-header-group", "inline", "table-row", "inline", "inline" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "inline", "block", "inline", "inline", "inline" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      var cs = window.getComputedStyle(e, null);\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "transparent", "rgb(255, 0, 0)", "rgb(255, 255, 255)" },
            CHROME = { "rgba(0, 0, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 255, 255)" })
    public void backgroundColor() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d0'>div 0</div>\n"
            + "<div id='d1' style='background: red'>d</div>\n"
            + "<div id='d2' style='background: white url(http://htmlunit.sf.net/foo.png) repeat-x fixed top right'>"
            + "second div</div>\n"
            + "<script>\n"
            + "function getStyle(x) {\n"
            + "  var d = document.getElementById(x);\n"
            + "  var cs = window.getComputedStyle(d, null);\n"
            + "  return cs;\n"
            + "}\n"
            + "var cs0 = getStyle('d0');\n"
            + "alert(cs0.backgroundColor);\n"
            + "var cs1 = getStyle('d1');\n"
            + "alert(cs1.backgroundColor);\n"
            + "var cs2 = getStyle('d2');\n"
            + "alert(cs2.backgroundColor);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
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
            + "function getStyle(x) {\n"
            + "  var d = document.getElementById(x);\n"
            + "  var cs = window.getComputedStyle(d, null);\n"
            + "  return cs;\n"
            + "}\n"
            + "var cs1 = getStyle('d1');\n"
            + "alert(cs1.fontSize);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1240px", "auto" },
            FF = { "1242px", "auto" })
    @NotYetImplemented
    public void computedWidthOfHiddenElements() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('myDiv1');\n"
            + "     var cs1 = window.getComputedStyle(div1, null);\n"
            + "     alert(cs1.width);\n"
            + "     var div2 = document.getElementById('myDiv2');\n"
            + "     var cs2 = window.getComputedStyle(div2, null);\n"
            + "     alert(cs2.width);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv1'></div>\n"
            + "  <div id='myDiv2' style='display:none'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(content);
    }

    /**
     * Verifies that at least one CSS attribute is correctly inherited by default.
     * Required by the MochiKit tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ ",", "separate,separate", "collapse,", "collapse,collapse" })
    public void inheritedImplicitly() throws Exception {
        final String html
            = "<html><body><table id='a'><tr id='b'><td>a</td></tr></table><script>\n"
            + "var a = document.getElementById('a');\n"
            + "var b = document.getElementById('b');\n"
            + "var as = a.style;\n"
            + "var bs = b.style;\n"
            + "var acs = window.getComputedStyle(a, null);\n"
            + "var bcs = window.getComputedStyle(b, null);\n"
            + "alert(as.borderCollapse + ',' + bs.borderCollapse);\n"
            + "alert(acs.borderCollapse + ',' + bcs.borderCollapse);\n"
            + "as.borderCollapse = 'collapse';\n"
            + "alert(as.borderCollapse + ',' + bs.borderCollapse);\n"
            + "alert(acs.borderCollapse + ',' + bcs.borderCollapse);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that when the class of an ancestor node matters for the effective style,
     * it is recomputed if the class of the ancestor node changes.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "underline", "none", "underline" })
    public void changeInParentClassNodeReferencedByRule() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function readDecoration(id) {\n"
            + "  var e = document.getElementById(id);\n"
            + "  var s = window.getComputedStyle(e, null);\n"
            + "  alert(s.textDecoration)\n"
            + "}\n"
            + "function test() {\n"
            + "  var fooA = document.getElementById('fooA');\n"
            + "  readDecoration('fooB')\n"
            + "  fooA.setAttribute('class', '');\n"
            + "  readDecoration('fooB')\n"
            + "  fooA.setAttribute('class', 'A');\n"
            + "  readDecoration('fooB')\n"
            + "}\n"
            + "</script>\n"
            + "<style>\n"
            + ".A .B { text-decoration: underline }\n"
            + "</style>\n"
            + "</head><body onload='test()'>\n"
            + "<div class='A' id='fooA'>A\n"
            + "<div class='B' id='fooB'>B</div></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "200px,400px", "200,400", "200px,400px", "50%,25%", "100,100", "100px,100px" })
    public void widthAndHeightPercentagesAndPx() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:200px;height:400px'><div id='d2' style='width:50%;height:25%'></div></div>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var d1 = document.getElementById('d1');\n"
            + "    var s1 = window.getComputedStyle(d1, null);\n"
            + "    var d2 = document.getElementById('d2');\n"
            + "    var s2 = window.getComputedStyle(d2, null);\n"
            + "    alert(d1.style.width + ',' + d1.style.height);\n"
            + "    alert(d1.offsetWidth + ',' + d1.offsetHeight);\n"
            + "    alert(s1.width + ',' + s1.height);\n"
            + "    alert(d2.style.width + ',' + d2.style.height);\n"
            + "    alert(d2.offsetWidth + ',' + d2.offsetHeight);\n"
            + "    alert(s2.width + ',' + s2.height);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "10em,20em", "160,320", "160px,320px", "50%,25%", "80,80", "80px,80px" })
    public void widthAndHeightPercentagesAndEm() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:10em;height:20em'><div id='d2' style='width:50%;height:25%'></div></div>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var d1 = document.getElementById('d1');\n"
            + "    var s1 = window.getComputedStyle(d1, null);\n"
            + "    var d2 = document.getElementById('d2');\n"
            + "    var s2 = window.getComputedStyle(d2, null);\n"
            + "    alert(d1.style.width + ',' + d1.style.height);\n"
            + "    alert(d1.offsetWidth + ',' + d1.offsetHeight);\n"
            + "    alert(s1.width + ',' + s1.height);\n"
            + "    alert(d2.style.width + ',' + d2.style.height);\n"
            + "    alert(d2.offsetWidth + ',' + d2.offsetHeight);\n"
            + "    alert(s2.width + ',' + s2.height);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * NullPointerException occurred in offsetX computation in HtmlUnit-2.7-SNAPSHOT (19.01.2010).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "true" })
    public void widthAndHeightPercentagesHTML() throws Exception {
        final String html = "<html style='height: 100%'>\n"
            + "<body>\n"
            + "<script>\n"
            + "  var h = document.documentElement;\n"
            + "  alert(h.offsetWidth > 0);\n"
            + "  alert(h.offsetHeight > 0);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "true", "true", "true", "true", "true", "true", "true",
                "true", "true", "true", "true", "false", "false",
                "true", "true", "true", "true" })
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
            + "    function x(id) {\n"
            + "      var e = document.getElementById(id);\n"
            + "      alert(e.offsetWidth > 0);\n"
            + "      alert(e.offsetHeight > 0);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "auto", "auto" },
            CHROME = { "", "" })
    public void widthAndHeightDisconnected() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var e = document.createElement('div');\n"
            + "      var style = window.getComputedStyle(e, null);\n"
            + "      alert(style.width);\n"
            + "      alert(style.height);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "true", "true", "true", "false", "false", "true", "true", "true", "false" })
    public void widthAuto() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      tester(document.body);\n"
                + "      tester(document.getElementById('div'));\n"
                + "    }\n"
                + "    function tester(el) {\n"
                + "      alert(el.style.width=='auto');\n"
                + "      alert(el.clientWidth > 100);\n"
                + "      alert(el.offsetWidth > 100);\n"

                + "      var style = window.getComputedStyle(el, null);\n"
                + "      alert(/\\d+px/.test(style.width));\n"
                + "      alert(style.width=='auto');\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body style='width: auto' onload='test();'>\n"
                + "<div id='div'></div>\n"
                + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "rgb(0, 0, 255)" })
    @NotYetImplemented
    public void getPropertyValue() throws Exception {
        final String html = "<html><head><script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var d = document.getElementById('div1');\n"
            + "    var s = window.getComputedStyle(d, null);\n"
            + "    alert(s.getPropertyValue('test'));\n"
            + "    alert(s.getPropertyValue('color'));\n"
            + "  } catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "<style>#div1 { test: red }</style>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: blue'>foo</div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "roman", "swiss", "roman" })
    public void handleImportant() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function doTest() {\n"
            + "    alertFF(document.getElementById('div1'));\n"
            + "    alertFF(document.getElementById('div2'));\n"
            + "    alertFF(document.getElementById('div3'));\n"
            + "  }\n"
            + "  function alertFF(e) {\n"
            + "    var s = window.getComputedStyle(e, null);\n"
            + "    alert(s.getPropertyValue('font-family'));\n"
            + "  }\n"
            + "</script>\n"
            + "  <style>#div1 { font-family: swiss }</style>\n"
            + "  <style>#div2 { font-family: swiss !important }</style>\n"
            + "  <style>#div3 { font-family: swiss !important }</style>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <div id='div1' style='font-family: roman'>foo</div>"
            + "  <div id='div2' style='font-family: roman'>foo</div>"
            + "  <div id='div3' style='font-family: roman !important'>foo</div>"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void offsetHeight_empty_tag() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('div1').offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void offsetHeight_empty() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('div1').offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "18",
            FF = "20")
    public void offsetHeight_with_content() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('div1').offsetHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'>foo</div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "false" })
    public void offsetHeight_setting_height() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "  .v-loading-indicator {\n"
            + "    height: 100%\n"
            + "  }\n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    alert(div1.offsetHeight == 0);\n"
            + "    div1.className = 'v-loading-indicator';"
            + "    alert(div1.offsetHeight == 0);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "false" })
    public void scrollbarWidth() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var scroller = document.createElement('div');\n"
            + "    scroller.style['width'] = '50px';\n"
            + "    scroller.style['height'] = '50px';\n"
            + "    scroller.style['overflow'] = 'scroll';\n"
            + "    alert(scroller.offsetWidth - scroller.clientWidth == 0);\n"
            + "    document.body.appendChild(scroller);\n"
            + "    alert(scroller.offsetWidth - scroller.clientWidth == 0);\n"
            + "    document.body.removeChild(scroller);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "false" },
            FF = { "true", "true" })
    @NotYetImplemented(FF)
    public void scrollbarHeight() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var scroller = document.createElement('div');\n"
            + "    scroller.style['width'] = '50px';\n"
            + "    scroller.style['height'] = '50px';\n"
            + "    scroller.style['overflow'] = 'scroll';\n"
            + "    alert(scroller.offsetHeight - scroller.clientHeight == 0);\n"
            + "    document.body.appendChild(scroller);\n"
            + "    alert(scroller.offsetHeight - scroller.clientHeight == 0);\n"
            + "    document.body.removeChild(scroller);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "", "", "10", "10", "rgb(0, 128, 0)" },
            CHROME = { "", "", "", "auto", "auto", "rgb(0, 128, 0)" })
    public void zIndexComputed() throws Exception {
        final String html = "<html><head>"
            + "<style>\n"
            + "  .abc { z-index: 10; color:green }\n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    alert(div.style.zIndex);\n"
            + "    alert(div.style['z-index']);\n"
            + "    alert(div.style.color);\n"
            + "    alert(window.getComputedStyle(div, '').zIndex);\n"
            + "    alert(window.getComputedStyle(div, '')['z-index']);\n"
            + "    alert(window.getComputedStyle(div, '').color);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' class='abc'></div>"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "0", "0", "0", "auto", "100px", "100px", "3px", "block", "content-box", "0px", "0px",
                "104", "104", "104", "104", "auto", "100px", "100px", "3px", "block", "content-box", "0px", "0px" },
            CHROME = { "0", "0", "0", "0", "", "", "", "", "", "", "", "",
                    "104", "104", "104", "104", "auto", "100px", "100px", "3px", "block", "content-box", "0px", "0px" })
    public void offsetWidth() throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    div.style.width = '100px';\n"
            + "    div.style.height = '100px';\n"
            + "    div.style.padding = '2px';\n"
            + "    div.style.margin = '3px';\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    alert(div.offsetWidth);\n"
            + "    alert(div.offsetHeight);\n"
            + "    alert(div.clientWidth);\n"
            + "    alert(div.clientHeight);\n"
            + "    alert(style.top);\n"
            + "    alert(style.width);\n"
            + "    alert(style.height);\n"
            + "    alert(style.marginRight);\n"
            + "    alert(style.display);\n"
            + "    alert(style.boxSizing);\n"
            + "    alert(style.borderRightWidth);\n"
            + "    alert(style.borderLeftWidth);\n"
            + "    document.body.appendChild(div);\n"
            + "    alert(div.offsetWidth);\n"
            + "    alert(div.offsetHeight);\n"
            + "    alert(div.clientWidth);\n"
            + "    alert(div.clientHeight);\n"
            + "    alert(style.top);\n"
            + "    alert(style.width);\n"
            + "    alert(style.height);\n"
            + "    alert(style.marginRight);\n"
            + "    alert(style.display);\n"
            + "    alert(style.boxSizing);\n"
            + "    alert(style.borderRightWidth);\n"
            + "    alert(style.borderLeftWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "", "left", "left", "right", "right" },
            FF = { "left", "left", "left", "left", "right", "right" },
            IE = { "undefined", "left", "undefined", "left", "undefined", "right" })
    @NotYetImplemented(IE)
    public void cssFloat2() throws Exception {
        final String html = "<html><head>"
            + "<style>\n"
            + "  .abc { float: right }\n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    div.style.float = 'left';\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    alert(style.float);\n"
            + "    alert(style.cssFloat);\n"
            + "    document.body.appendChild(div);\n"
            + "    alert(style.float);\n"
            + "    alert(style.cssFloat);\n"
            + "    div = document.getElementById('mydiv');\n"
            + "    style = window.getComputedStyle(div, null);\n"
            + "    alert(style.float);\n"
            + "    alert(style.cssFloat);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv' class='abc'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = "1")
    public void custom() throws Exception {
        final String html = "<html><head>"
            + "<style>\n"
            + "  .abc { xyz: 1 }\n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    alert(style.xyz);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv' class='abc'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "16px", "2em", "32px", "150%", "24px" })
    public void fontSizeEm() throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    alert(div.style.fontSize);\n"
            + "    alert(style.fontSize);\n"
            + "    div.style.fontSize = '2em';\n"
            + "    alert(div.style.fontSize);\n"
            + "    alert(style.fontSize);\n"
            + "    div.style.fontSize = '150%';\n"
            + "    alert(div.style.fontSize);\n"
            + "    alert(style.fontSize);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "normal normal normal normal 16px / normal 'Times New Roman'",
                "", "normal", "", "normal", "", "normal", "", "16px", "", "normal", "", "'Times New Roman'" },
            FF = { "", "", "", "normal", "", "normal", "", "400", "", "16px", "", "20px", "", "serif",
                "", "", "", "normal", "", "normal", "", "400", "", "16px", "", "20px", "", "serif" },
            IE = { "", "", "", "normal", "", "normal", "", "400", "", "16px", "", "normal", "", "Times New Roman",
                "", "", "", "normal", "", "normal", "", "400", "", "16px", "", "normal", "", "Times New Roman" })
    public void fontInitial() throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.createElement('div');\n"
            + "    debug(div);\n"
            + "    document.body.appendChild(div);\n"
            + "    debug(div);\n"
            + "  }\n"
            + "  function debug(div) {\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    alert(div.style.font);\n"
            + "    alert(style.font);\n"
            + "    alert(div.style.fontStyle);\n"
            + "    alert(style.fontStyle);\n"
            + "    alert(div.style.fontVariant);\n"
            + "    alert(style.fontVariant);\n"
            + "    alert(div.style.fontWeight);\n"
            + "    alert(style.fontWeight);\n"
            + "    alert(div.style.fontSize);\n"
            + "    alert(style.fontSize);\n"
            + "    alert(div.style.lineHeight);\n"
            + "    alert(style.lineHeight);\n"
            + "    alert(div.style.fontFamily);\n"
            + "    alert(style.fontFamily);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "15px arial, sans-serif", "normal normal normal normal 15px / normal arial, sans-serif",
                "normal", "normal",
                "oblique 15px arial, sans-serif", "oblique normal normal normal 15px / normal arial, sans-serif",
                "oblique", "oblique" },
            FF = { "15px arial,sans-serif", "", "normal", "normal",
                    "oblique 15px arial,sans-serif", "", "oblique", "oblique" },
            IE = { "15px/normal arial, sans-serif", "", "normal", "normal",
                    "oblique 15px/normal arial, sans-serif", "", "oblique", "oblique" })
    @NotYetImplemented
    public void fontStyle() throws Exception {
        font("15px arial, sans-serif", "fontStyle", "oblique");
    }

    private void font(final String fontToSet, final String property, final String value) throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('mydiv');\n"
            + "    div.style.font = '" + fontToSet + "';\n"
            + "    debug(div);\n"
            + "    div.style." + property + " = '" + value + "';\n"
            + "    debug(div);\n"
            + "  }\n"
            + "  function debug(div) {\n"
            + "    var style = window.getComputedStyle(div, null);\n"
            + "    alert(div.style.font);\n"
            + "    alert(style.font);\n"
            + "    alert(div.style." + property + ");\n"
            + "    alert(style." + property + ");\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
