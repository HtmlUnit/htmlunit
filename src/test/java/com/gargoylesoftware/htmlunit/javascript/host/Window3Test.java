/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF17;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import java.net.URL;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link Window}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author David K. Taylor
 * @author Darrell DeBoer
 * @author Marc Guillemot
 * @author Dierk Koenig
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Window3Test extends WebDriverTestCase {

    /**
     * Regression test to reproduce a known bug.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("about:blank")
    public void openWindow_emptyUrl() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "var w = window.open('');\n"
            + "alert(w ? w.document.location : w);\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "null", "one", "two", "three" },
            IE8 = { "undefined", "one", "two", "three" })
    @BuggyWebDriver({ FF24, IE8, IE11 })
    @NotYetImplemented(IE8)
    public void opener() throws Exception {
        final URL urlThird = new URL(URL_FIRST, "third/");

        final String firstContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    alert(window.opener);\n"
            + "    alert('one');\n"
            + "    open('" + URL_SECOND + "', 'foo');\n"
            + "}\n"
            + "function callAlert(text) {\n"
            + "    alert(text);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String secondContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Second</title><script>\n"
            + "function test() {\n"
            + "    opener.callAlert('two');\n"
            + "    document.form1.submit();\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='form1' action='" + urlThird + "' method='post'><input type='submit'></form>\n"
            + "</body></html>";
        final String thirdContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Third</title><script>\n"
            + "function test() {\n"
            + "    opener.callAlert('three');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_FIRST, firstContent);
        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(urlThird, thirdContent);

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);
        assertEquals("First", driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("one")
    public void windowFrames() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><script language='JavaScript'>\n"
            + "if (typeof top.frames['anyXXXname'] == 'undefined') {\n"
            + "alert('one');\n"
            + "};\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Variables that are defined inside JavaScript should be accessible through the
     * window object (ie window.myVariable). Test that this works.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void javascriptVariableFromWindow() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>first</title></head><body><script>\n"
            + "myVariable = 'foo';\n"
            + "alert(window.myVariable);\n"
            + "</script></body></head>";

        loadPageWithAlerts2(html);
    }

    /**
     * Variables that are defined inside JavaScript should be accessible through the
     * window object (ie window.myVariable). Test that this works.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "parent.myVariable = second", "top.myVariable = first" })
    public void javascriptVariableFromTopAndParentFrame() throws Exception {
        final URL urlThird = new URL(URL_FIRST, "third/");

        final String firstContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title></head><body><script>\n"
            + "myVariable = 'first'"
            + "  </script><iframe name='left' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(URL_FIRST, firstContent);

        final String secondContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Second</title></head><body><script>\n"
            + "myVariable = 'second'"
            + "  </script><iframe name='innermost' src='" + urlThird + "'></iframe>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final String thirdContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Third</title><script>\n"
            + "myVariable = 'third';\n"
            + "function doTest() {\n"
            + "alert('parent.myVariable = ' + parent.myVariable);\n"
            + "alert('top.myVariable = ' + top.myVariable);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'></body></html>";
        getMockWebConnection().setResponse(urlThird, thirdContent);

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);
        assertEquals("First", driver.getTitle());
    }

    /**
     * Variables that are defined inside JavaScript should be accessible through the
     * window object (ie window.myVariable). Test that this works.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "parent.second.myVariable = second", "parent.third.myVariable = third" })
    public void javascriptVariableFromNamedFrame() throws Exception {
        final URL urlThird = new URL(URL_FIRST, "third/");
        final URL urlFourth = new URL(URL_FIRST, "fourth/");

        final String firstContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frameset rows='30%,70%'>\n"
            + "        <frame src='" + URL_SECOND + "' name='second'>\n"
            + "        <frame src='" + urlThird + "' name='third'>\n"
            + "    </frameset>\n"
            + "    <frame src='" + urlFourth + "' name='fourth'>\n"
            + "</frameset></html>";
        getMockWebConnection().setResponse(URL_FIRST, firstContent);

        final String secondContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>second</title></head><body><script>\n"
            + "myVariable = 'second';\n"
            + "</script><p>second</p></body></html>";
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final String thirdContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>third</title></head><body><script>\n"
            + "myVariable = 'third';\n"
            + "</script><p>third</p></body></html>";
        getMockWebConnection().setResponse(urlThird, thirdContent);

        final String fourthContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>fourth</title></head><body onload='doTest()'><script>\n"
            + "myVariable = 'fourth';\n"
            + "function doTest() {\n"
            + "alert('parent.second.myVariable = ' + parent.second.myVariable);\n"
            + "alert('parent.third.myVariable = ' + parent.third.myVariable);\n"
            + "}\n"
            + "</script></body></html>";
        getMockWebConnection().setResponse(urlFourth, fourthContent);

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);
        assertEquals("first", driver.getTitle());
    }

    /**
     * Variables that have not been defined should return null when accessed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void javascriptVariableFromWindow_NotFound() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>first</title></head><body><script>\n"
            + "myVariable = 'foo';\n"
            + "alert(window.myOtherVariable == null);\n"
            + "</script></body></head>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "fourth-second=Â§Â§URL2Â§Â§", "fourth-third=Â§Â§URL3Â§Â§" })
    public void getFrameByName() throws Exception {
        final URL urlThird = new URL(URL_FIRST, "third/");
        final URL urlFourth = new URL(URL_FIRST, "fourth/");

        final String firstContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frameset rows='30%,70%'>\n"
            + "        <frame src='" + URL_SECOND + "' name='second'>\n"
            + "        <frame src='" + urlThird + "' name='third'>\n"
            + "    </frameset>\n"
            + "    <frame src='" + urlFourth + "' name='fourth'>\n"
            + "</frameset></html>";
        getMockWebConnection().setResponse(URL_FIRST, firstContent);

        final String secondContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>second</title></head><body><p>second</p></body></html>";
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final String thirdContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>third</title></head><body><p>third</p></body></html>";
        getMockWebConnection().setResponse(urlThird, thirdContent);

        final String fourthContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>fourth</title></head><body onload='doTest()'><script>\n"
            + "function doTest() {\n"
            + "alert('fourth-second='+parent.second.document.location);\n"
            + "alert('fourth-third='+parent.third.document.location);\n"
            + "}\n"
            + "</script></body></html>";
        getMockWebConnection().setResponse(urlFourth, fourthContent);

        final String[] expectedAlerts = getExpectedAlerts();
        for (int i = 0; i < expectedAlerts.length; ++i) {
            expectedAlerts[i] = expectedAlerts[i].replaceAll("Â§Â§URL2Â§Â§", URL_SECOND.toExternalForm())
                    .replaceAll("Â§Â§URL3Â§Â§", urlThird.toExternalForm());
        }
        setExpectedAlerts(expectedAlerts);

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);
        assertEquals("first", driver.getTitle());
    }

    /**
     * Test the <tt>window.closed</tt> property.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "false", "false", "true" })
    @BuggyWebDriver({ CHROME, IE8, IE11 })
    public void closed() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  alert(window.closed);\n"
            + "  var newWindow = window.open('about:blank', 'foo');\n"
            + "  alert(newWindow.closed);\n"
            + "  newWindow.close();\n"
            + "  alert(newWindow.closed);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.moveTo method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void moveTo() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.moveTo(10, 20)\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.moveBy method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void moveBy() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.moveBy(10, 20)\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests that the Window.resizeTo method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void resizeTo() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.resizeTo(10, 20);\n"
            + "window.resizeTo(-10, 20);\n"
            + "</script></head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests that the Window.resizeBy method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void resizeBy() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.resizeBy(10, 20);\n"
            + "window.resizeBy(-10, 20);\n"
            + "</script></head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.scroll method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void scroll() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.scroll(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.scrollBy method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void scrollBy() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.scrollBy(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.scrollByLines method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "")
    public void scrollByLines() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "try {\n"
            + "  window.scrollByLines(2);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.scrollByPages method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "")
    public void scrollByPages() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "try {\n"
            + "  window.scrollByPages(2);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.scrollTo method gets correctly called and handled by the scripting engine.
     * @throws Exception if the test fails
     */
    @Test
    public void scrollTo() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "window.scrollTo(10, 20);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "form1", "form1", "2", "2" },
            FF17 = { "exception:w.f1", "form1", "exception:w.f2", "2" })
    @NotYetImplemented(FF17)
    public void formByName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(window.form1.name);\n"
            + "    } catch (e) { alert('exception:w.f1') }\n"
            + "    try {\n"
            + "      alert(form1.name);\n"
            + "    } catch (e) { alert('exception:f1') }\n"
            + "    try {\n"
            + "      alert(window.form2.length);\n"
            + "    } catch (e) { alert('exception:w.f2') }\n"
            + "    try {\n"
            + "      alert(form2.length);\n"
            + "    } catch (e) { alert('exception:f2') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form name='form1'></form>\n"
            + "  <form name='form2'></form>\n"
            + "  <form name='form2'></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "frame1", "frame1", "0", "0" },
            IE = { "frame1", "frame1", "2", "2" },
            IE8 = { "frame1", "frame1", "exception:w.f2", "exception:f2" })
    @NotYetImplemented(IE)
    // TODO [IE11] HTMLCollection problem
    public void frameByName() throws Exception {
        final String html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\""
            + "\"http://www.w3.org/TR/html4/frameset.dtd\">\n"
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(window.frame1.name);\n"
            + "    } catch (e) { alert('exception:w.f1') }\n"
            + "    try {\n"
            + "      alert(frame1.name);\n"
            + "    } catch (e) { alert('exception:f1') }\n"
            + "    try {\n"
            + "      alert(window.frame2.length);\n"
            + "    } catch (e) { alert('exception:w.f2') }\n"
            + "    try {\n"
            + "      alert(frame2.length);\n"
            + "    } catch (e) { alert('exception:f2') }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<frameset onload='test()'>\n"
            + "  <frame src='" + URL_SECOND + "' name='frame1'>\n"
            + "  <frame src='" + URL_SECOND + "' name='frame2'>\n"
            + "  <frame src='" + URL_SECOND + "' name='frame2'>\n"
            + "</frameset>"
            + "</html>";

        final String frame = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><title>frame</title></head><body></body></html>";
        getMockWebConnection().setDefaultResponse(frame);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "frame1", "frame1", "0", "0" })
    public void iframeByName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(window.frame1.name);\n"
            + "    } catch (e) { alert('exception:w.f1') }\n"
            + "    try {\n"
            + "      alert(frame1.name);\n"
            + "    } catch (e) { alert('exception:f1') }\n"
            + "    try {\n"
            + "      alert(window.frame2.length);\n"
            + "    } catch (e) { alert('exception:w.f2') }\n"
            + "    try {\n"
            + "      alert(frame2.length);\n"
            + "    } catch (e) { alert('exception:f2') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <iframe name='frame1'></iframe>\n"
            + "  <iframe name='frame2'></iframe>\n"
            + "  <iframe name='frame2'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

     /**
      * @throws Exception if the test fails
      */
    @Test
    @Alerts(DEFAULT = { "exception:w.i1", "exception:i1", "exception:w.i2", "exception:i2" },
            IE = { "input1", "input1", "2", "2" })
    @NotYetImplemented(FF)
    // TODO [IE11] HTMLCollection problem
    public void inputByName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(window.input1.name);\n"
            + "    } catch (e) { alert('exception:w.i1') }\n"
            + "    try {\n"
            + "      alert(input1.name);\n"
            + "    } catch (e) { alert('exception:i1') }\n"
            + "    try {\n"
            + "      alert(window.input2.length);\n"
            + "    } catch (e) { alert('exception:w.i2') }\n"
            + "    try {\n"
            + "      alert(input2.length);\n"
            + "    } catch (e) { alert('exception:i2') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <input type='text' name='input1' value='1'/>\n"
            + "  <input type='text' name='input2' value='2'/>\n"
            + "  <input type='text' name='input2' value='3'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "exception:w.a1", "exception:a1", "exception:w.a2", "exception:a2" },
            IE = { "anchor1", "anchor1", "2", "2" })
    @NotYetImplemented(FF)
    // TODO [IE11] HTMLCollection problem
    public void anchorByName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(window.anchor1.name);\n"
            + "    } catch (e) { alert('exception:w.a1') }\n"
            + "    try {\n"
            + "      alert(anchor1.name);\n"
            + "    } catch (e) { alert('exception:a1') }\n"
            + "    try {\n"
            + "      alert(window.anchor2.length);\n"
            + "    } catch (e) { alert('exception:w.a2') }\n"
            + "    try {\n"
            + "      alert(anchor2.length);\n"
            + "    } catch (e) { alert('exception:a2') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <a name='anchor1'></a>\n"
            + "  <a name='anchor2'></a>\n"
            + "  <a name='anchor2'></a>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "image1", "image1", "2", "2" },
            FF17 = { "exception:w.i1", "image1", "exception:w.i2", "2" })
    @NotYetImplemented(FF17)
    public void imageByName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(window.image1.name);\n"
            + "    } catch (e) { alert('exception:w.i1') }\n"
            + "    try {\n"
            + "      alert(image1.name);\n"
            + "    } catch (e) { alert('exception:i1') }\n"
            + "    try {\n"
            + "      alert(window.image2.length);\n"
            + "    } catch (e) { alert('exception:w.i2') }\n"
            + "    try {\n"
            + "      alert(image2.length);\n"
            + "    } catch (e) { alert('exception:i2') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <img name='image1'>\n"
            + "  <img name='image2'>\n"
            + "  <img name='image2'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "exception:w.e1", "exception:e1", "exception:w.e2", "exception:e2" })
    @NotYetImplemented
    // TODO [IE11] HTMLCollection problem
    public void elementByName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(window.element1.name);\n"
            + "    } catch (e) { alert('exception:w.e1') }\n"
            + "    try {\n"
            + "      alert(element1.name);\n"
            + "    } catch (e) { alert('exception:e1') }\n"
            + "    try {\n"
            + "      alert(window.element2.length);\n"
            + "    } catch (e) { alert('exception:w.e2') }\n"
            + "    try {\n"
            + "      alert(element2.length);\n"
            + "    } catch (e) { alert('exception:e2') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div name='element1'></table>\n"
            + "  <div name='element2'></div>\n"
            + "  <div name='element2'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "2-2", "3-3", "4-4", "5-5", "6-6", "7-7", "8-8", "9-9", "10-10", "11-11", "10-10" },
            FF17 = { "0-2", "0-3", "0-4", "0-5", "0-6", "0-7", "0-8", "0-9", "0-10", "0-11", "0-10" },
            IE8 = { "2-2", "3-3", "4-4", "5-5", "6-6", "7-7", "8-8", "9-9", "10-10",
                "exception:setAttributeNS", "9-9" })
    @NotYetImplemented(FF17)
    public void elementsByName_changedAfterGet() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            // 2
            + "    var collection1 = window.image1;\n"
            + "    var collection2 = image1;\n"
            + "    if (!collection1) {\n"
            + "      collection1 = [];\n"
            + "    }\n"
            + "    if (!collection2) {\n"
            + "      collection2 = [];\n"
            + "    }\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 3
            + "    var newImage1 = document.createElement('img');\n"
            + "    newImage1.name = 'image1';\n"
            + "    document.getElementById('outer1').appendChild(newImage1);\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 4
            + "    var newImage2 = document.createElement('img');\n"
            + "    newImage2.name = 'image1';\n"
            + "    document.getElementById('outer2').insertBefore(newImage2, null);\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 5
            + "    var newImage3 = document.createElement('img');\n"
            + "    newImage3.name = 'image1';\n"
            + "    document.getElementById('outer3').replaceChild(newImage3, document.getElementById('inner3'));\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 6
            + "    document.getElementById('outer4').outerHTML = '<img name=\"image1\">';\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 7
            + "    document.getElementById('outer5').innerHTML = '<img name=\"image1\">';\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 8
            + "    document.getElementById('outer6').insertAdjacentHTML('beforeend', '<img name=\"image1\">');\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 9
            + "    document.getElementById('image3').setAttribute('name', 'image1');\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 10
            + "    var newAttr = document.createAttribute('name');\n"
            + "    newAttr.nodeValue = 'image1';\n"
            + "    document.getElementById('image4').setAttributeNode(newAttr);\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 11
            + "    try {\n"
            + "      document.getElementById('image5').setAttributeNS(null, 'name', 'image1');\n"
            + "      alert(collection1.length + '-' + collection2.length);\n"
            + "    } catch (e) { alert('exception:setAttributeNS') }\n"

            // 10
            + "    document.getElementById('outer1').removeChild(newImage1);\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <img name='image1'>\n"
            + "  <img name='image1'>\n"
            + "  <div id='outer1'></div>\n"
            + "  <div id='outer2'></div>\n"
            + "  <div id='outer3'><div id='inner3'></div></div>\n"
            + "  <div id='outer4'></div>\n"
            + "  <div id='outer5'></div>\n"
            + "  <div id='outer6'></div>\n"
            + "  <img id='image2'>\n"
            + "  <img id='image3'>\n"
            + "  <img id='image4'>\n"
            + "  <img id='image5'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "2-2", "3-3" },
            FF17 = { "0-2", "0-3" })
    @NotYetImplemented
    public void elementsByName_changedAfterGet_nyi() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            // 2
            + "    var collection1 = window.image1;\n"
            + "    var collection2 = image1;\n"
            + "    if (!collection1) {\n"
            + "      collection1 = [];\n"
            + "    }\n"
            + "    if (!collection2) {\n"
            + "      collection2 = [];\n"
            + "    }\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"

            // 3
            + "    document.getElementById('image2').name = 'image1';\n"
            + "    alert(collection1.length + '-' + collection2.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <img name='image1'>\n"
            + "  <img name='image1'>\n"
            + "  <img id='image2'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "form1", "form1", "f1", "f1", "input1", "input1", "anchor1", "anchor1", "image1",
                "image1", "element1", "element1" },
            FF17 = { "exception:w.f1", "form1", "exception:w.f1", "f1", "exception:w.i1", "input1",
                "exception:w.a1", "anchor1", "exception:w.i1", "image1", "exception:w.e1", "element1" })
    @NotYetImplemented(FF17)
    public void elementsById() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(window.form1.id);\n"
            + "    } catch (e) { alert('exception:w.f1') }\n"
            + "    try {\n"
            + "      alert(form1.id);\n"
            + "    } catch (e) { alert('exception:f1') }\n"
            + "    try {\n"
            + "      alert(window.frame1.name);\n"
            + "    } catch (e) { alert('exception:w.f1') }\n"
            + "    try {\n"
            + "      alert(frame1.name);\n"
            + "    } catch (e) { alert('exception:f1') }\n"
            + "    try {\n"
            + "      alert(window.input1.id);\n"
            + "    } catch (e) { alert('exception:w.i1') }\n"
            + "    try {\n"
            + "      alert(input1.id);\n"
            + "    } catch (e) { alert('exception:i1') }\n"
            + "    try {\n"
            + "      alert(window.anchor1.id);\n"
            + "    } catch (e) { alert('exception:w.a1') }\n"
            + "    try {\n"
            + "      alert(anchor1.id);\n"
            + "    } catch (e) { alert('exception:a1') }\n"
            + "    try {\n"
            + "      alert(window.image1.id);\n"
            + "    } catch (e) { alert('exception:w.i1') }\n"
            + "    try {\n"
            + "      alert(image1.id);\n"
            + "    } catch (e) { alert('exception:i1') }\n"
            + "    try {\n"
            + "      alert(window.element1.id);\n"
            + "    } catch (e) { alert('exception:w.e1') }\n"
            + "    try {\n"
            + "      alert(element1.id);\n"
            + "    } catch (e) { alert('exception:e1') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form id='form1'></form>\n"
            + "  <iframe id='frame1' name='f1'></iframe>\n"
            + "  <input type='text' id='input1' value='1'/>\n"
            + "  <a id='anchor1'></a>\n"
            + "  <img id='image1'>\n"
            + "  <div id='element1'></table>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "f1", "f1" },
            FF17 = { "exception:w.f1", "f1" })
    @NotYetImplemented(FF17)
    public void frameById() throws Exception {
        final String html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\""
            + "\"http://www.w3.org/TR/html4/frameset.dtd\">\n"
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(window.frame1.name);\n"
            + "    } catch (e) { alert('exception:w.f1') }\n"
            + "    try {\n"
            + "      alert(frame1.name);\n"
            + "    } catch (e) { alert('exception:f1') }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<frameset onload='test()'>\n"
            + "  <frame src='" + URL_SECOND + "' id='frame1' name='f1'>\n"
            + "</frameset>"
            + "</html>";

        final String frame = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><title>frame</title></head><body></body></html>";
        getMockWebConnection().setDefaultResponse(frame);

        loadPageWithAlerts2(html);
    }

    /**
     * Test that Window.execScript method gets called correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE8 = { "JavaScript", "JScript", "exception1", "exception2: Invalid class string" })
    public void execScript() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      window.execScript('alert(\"JavaScript\")', 'JavaScript');\n"
            + "      window.execScript('alert(\"JScript\")',    'JScript');\n"
            + "      try {\n"
            + "        window.execScript('alert(\"VBScript\")',   'VBScript');\n"
            + "      } catch (e) { alert('exception1'); }\n"
            + "      try {\n"
            + "        window.execScript('alert(\"BadLanguage\")', 'BadLanguage');\n"
            + "      } catch (e) {\n"
            + "        alert('exception2: ' + e.message.substr(0, 20)); // msg now contains info on error location\n"
            + "      }\n"
            + "    } catch (e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'>blah</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

   /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "test2", "test" })
    public void onLoadFunction() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test()\n"
            + "  {\n"
            + "    alert('test');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<script>\n"
            + "  var oldOnLoad = window.onload;\n"
            + "  window.onload = test2;\n"
            + "  function test2()\n"
            + "  {\n"
            + "    alert('test2');\n"
            + "    oldOnLoad();\n"
            + "  }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that you can set window.onload to something else than a function.
     * See bug 1708532 & 1201561.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "a", "null" },
            IE8 = { "a", "exception", "[object Object]" })
    public void onloadNotAFunction() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><script>\n"
            + "window.onload = new function() {alert('a')};\n"
            + "try {\n"
            + "  window.onload = undefined;\n"
            + "} catch (e) { alert('exception'); }\n"
            + "alert(window.onload);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "test1", "test2", "onload" },
            IE8 = { "true", "true", "exception", "onload" })
    public void addOnLoadEventListener() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test1() {alert('test1');}\n"
            + "  function test2() {alert('test2');}\n"
            + "  function test3() {alert('test3');}\n"
            + "alert(window.addEventListener == null);\n"
            + "alert(window.removeEventListener == null);\n"
            + "try {\n"
            + "  window.addEventListener('load', test1, true);\n"
            + "  window.addEventListener('load', test1, true);\n"
            + "  window.addEventListener('load', test2, true);\n"
            + "  window.addEventListener('load', test3, true);\n"
            + "  window.removeEventListener('load', test3, true);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head>\n"
            + "<body onload='alert(\"onload\")'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "exception", "onload" },
            IE8 = { "false", "false", "onload", "test2", "test1, param null: false", "test1, param null: false" })
    @NotYetImplemented(IE8)
    public void attachOnLoadEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "  function test1(_e) {alert('test1, param null: ' + (_e == null));}\n"
            + "  function test2() {alert('test2');}\n"
            + "  function test3() {alert('test3');}\n"
            + "alert(window.attachEvent == null);\n"
            + "alert(window.detachEvent == null);\n"
            + "try {\n"
            + "  window.attachEvent('onload', test1);\n"
            + "  window.attachEvent('onload', test1);\n"
            + "  window.attachEvent('onload', test2);\n"
            + "  window.attachEvent('onload', test3);\n"
            + "  window.detachEvent('onload', test3);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head>\n"
            + "<body onload='alert(\"onload\")'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 1596926.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE8 = "detached")
    public void detachEventInAttachEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  window.detachEvent('onload', test);\n"
            + "  alert('detached');\n"
            + "}\n"
            + "try {\n"
            + "  window.attachEvent('onload', test);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test <code>window.name</code>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "window.name before: ", "window.name after: main" })
    public void windowName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>window.name test</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "alert('window.name before: ' + window.name);\n"
            + "window.name = 'main';\n"
            + "alert('window.name after: ' + window.name);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests Mozilla viewport properties.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "number", "number", "number", "number" },
            IE8 = { "undefined", "undefined", "undefined", "undefined" })
    public void mozillaViewport() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "alert(typeof window.innerWidth);\n"
            + "alert(typeof window.innerHeight);\n"
            + "alert(typeof window.outerWidth);\n"
            + "alert(typeof window.outerHeight);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test the <tt>Referer</tt> HTTP header by <tt>window.open</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "§§URL§§",
            FF = "§§URL§§")
    public void openWindow_refererHeader() throws Exception {
        final String firstContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head></head>\n"
            + "<body>\n"
            + "<button id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
            + "</body></html>";

        final String secondContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, firstContent);
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedAlerts = getExpectedAlerts();
        setExpectedAlerts();

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);

        driver.findElement(By.id("clickme")).click();

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        if (expectedAlerts.length == 0) {
            assertNull(lastAdditionalHeaders.get("Referer"));
        }
        else {
            assertEquals(expectedAlerts[0], lastAdditionalHeaders.get("Referer"));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "")
    @NotYetImplemented(IE8)
    public void evalScopeOtherWindow() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<iframe src='iframe.html'></iframe>\n"
            + "</body></html>";
        final String iframe = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<script>\n"
            + "window.parent.eval('var foo = 1');\n"
            + "alert(window.parent.foo)\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(iframe);
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for #408 JavaScript: window.eval does evaluate local scope.
     * See http://sourceforge.net/p/htmlunit/bugs/408/.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "elementValue", "elementValue", "elementValue" })
    public void evalScopeLocal() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><form id='formtest'><input id='element' value='elementValue'/></form>\n"
            + "<script>\n"
            + "var docPatate = 'patate';\n"
            + "function test() {\n"
            + " var f = document.forms['formtest'];\n"
            + " alert(eval(\"document.forms['formtest'].element.value\"));\n"
            + " alert(f.element.value);\n"
            + " alert(eval('f.element.value'));\n"
            + "}\n"
            + "test();\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that eval() works correctly when triggered from an event handler. Event handlers are
     * executed in a child scope of the global window scope, so variables set from inside eval()
     * should go to this child scope, and not to the window scope.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("string")
    public void evalScopeEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      var s = 'string';\n"
            + "      var f = 'initial';\n"
            + "      eval('f = function(){alert(s);}');\n"
            + "      invoke(f);\n"
            + "   };\n"
            + "   function invoke(fn) {\n"
            + "      fn();\n"
            + "   }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void functionEquality() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<script>\n"
            + "alert(window.focus == window.focus)\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for 1225021.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "123", "captured" },
            IE8 = { "exception", "123" })
    @BuggyWebDriver(IE8)
    @NotYetImplemented(IE8)
    public void captureEvents() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function t() { alert('captured'); }\n"
            + "try {\n"
            + "window.captureEvents(Event.CLICK);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "window.onclick = t;\n"
            + "</script></head><body>\n"
            + "<div id='theDiv' onclick='alert(123)'>foo</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(content);

        driver.findElement(By.id("theDiv")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Verifies that the onload handler is executed with "this" referring to the window.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void onLoadContext() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body><script>\n"
            + "var x = function() { alert(this==window) };\n"
            + "window.onload = x;\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Added test for [ 1727599 ] Bad context in evaluation of the JavaScript.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("INPUT")
    public void eval() throws Exception {
        final String content = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<input type='button' id='myButton' value='Click Me' onclick='test(this)'>\n"
            + "<script>\n"
            + "function test(f) {\n"
            + "   alert(eval('f.tagName'));\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(content);

        driver.findElement(By.id("myButton")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "undefined", "undefined", "true" })
    public void undefinedProperty() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(window['something']);\n"
            + "    alert(typeof window['something']);\n"
            + "    alert(typeof window['something']=='undefined');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void frames() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title></head>\n"
            + "<frameset id='fs' rows='20%,*'>\n"
            + "    <frame name='top' src='" + URL_SECOND + "' />\n"
            + "    <frame name='bottom' src='about:blank' />\n"
            + "</frameset>\n"
            + "</html>";

        final String frameContent = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>TopFrame</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var bottomFrame = window.top.frames['bottom'];\n"
            + "    bottomFrame.location = 'about:blank';\n"
            + "}</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, frameContent);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void openWindow_numericName() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  var w1 = window.open('about:blank', 1);\n"
            + "  alert(w1 != null);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<iframe name='myFrame' id='myFrame'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "error")
    public void stop() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    window.stop();\n"
            + "    alert(true);\n"
            + "  } catch (e) {\n"
            + "    alert('error');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void open() throws Exception {
        final String firstHtml = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head></head>\n"
            + "<body>\n"
            + "<button id='clickme' onClick='window.open(new String(\"" + URL_SECOND + "\"));'>Click me</a>\n"
            + "</body></html>";

        final String secondHtml = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, firstHtml);
        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final WebDriver driver = loadPageWithAlerts2(URL_FIRST);

        driver.findElement(By.id("clickme")).click();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "First", IE = "Second")
    public void navigate() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    if (window.navigate) {\n"
            + "      window.navigate('" + URL_SECOND + "');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent, URL_FIRST);
        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }
}
