/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import java.util.Iterator;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.RetriesRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.RetriesRunner.Reries;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with version 1.0.2 of the <a href="http://dojotoolkit.org/">Dojo
 * JavaScript library</a>.
 *
 * TODO: add tests for IE6 and IE7
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@RunWith(RetriesRunner.class)
public class Dojo102Test extends WebServerTestCase {

    private WebClient client_;

    private static final String GROUP_DELIMITER = "------------------------------------------------------------";

    /**
     * Constructor.
     * @throws Exception if an error occurs
     */
    public Dojo102Test() throws Exception {
        startWebServer("src/test/resources/libraries/dojo/1.0.2");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Reries(3)
    public void dojo() throws Exception {
        client_ = new WebClient(BrowserVersion.FIREFOX_3);
        final String url = "http://localhost:" + PORT + "/util/doh/runner.html";

        final HtmlPage page = client_.getPage(url);
        client_.waitForBackgroundJavaScript(10000);

        final HtmlElement logBody = page.getHtmlElementById("logBody");
        DomNode lastChild = logBody.getLastChild();
        while (true) {
            Thread.sleep(10000);
            final DomNode newLastChild = logBody.getLastChild();
            if (lastChild != newLastChild) {
                lastChild = newLastChild;
            }
            else {
                break;
            }
        }

        final Iterator<HtmlElement> logs = logBody.getChildElements().iterator();
        eq("345 tests to run in 41 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base._loader.bootstrap\" has 5 tests to run", logs);
        eq("PASSED test: hasConsole", logs);
        eq("PASSED test: hasDjConfig", logs);
        eq("PASSED test: getObject", logs);
        eq("PASSED test: exists", logs);
        eq("PASSED test: evalWorks", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base._loader.loader\" has 3 tests to run", logs);
        eq("PASSED test: baseUrl", logs);
        eq("PASSED test: modulePaths", logs);
        eq("PASSED test: moduleUrls", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base._loader.hostenv_browser\" has 1 test to run", logs);
        eq("PASSED test: getText", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.array\" has 13 tests to run", logs);
        eq("PASSED test: testIndexOf", logs);
        eq("PASSED test: testIndexOfFromIndex", logs);
        eq("PASSED test: testLastIndexOf", logs);
        eq("PASSED test: testLastIndexOfFromIndex", logs);
        eq("PASSED test: testForEach", logs);
        eq("PASSED test: testForEach_str", logs);
        eq("PASSED test: testEvery", logs);
        eq("PASSED test: testEvery_str", logs);
        eq("PASSED test: testSome", logs);
        eq("PASSED test: testSome_str", logs);
        eq("PASSED test: testFilter", logs);
        eq("PASSED test: testFilter_str", logs);
        eq("PASSED test: testMap", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.Color\" has 12 tests to run", logs);
        eq("PASSED test: testColor1", logs);
        eq("PASSED test: testColor2", logs);
        eq("PASSED test: testColor3", logs);
        eq("PASSED test: testColor4", logs);
        eq("PASSED test: testColor5", logs);
        eq("PASSED test: testColor6", logs);
        eq("PASSED test: testColor7", logs);
        eq("PASSED test: testColor8", logs);
        eq("PASSED test: testColor9", logs);
        eq("PASSED test: testColor10", logs);
        eq("PASSED test: testColor11", logs);
        eq("PASSED test: testColor12", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.lang\" has 13 tests to run", logs);
        eq("PASSED test: mixin", logs);
        eq("PASSED test: extend", logs);
        eq("PASSED test: isObject", logs);
        eq("PASSED test: isArray", logs);
        eq("PASSED test: isArrayLike", logs);
        eq("PASSED test: isString", logs);
        eq("PASSED test: partial", logs);
        eq("PASSED test: nestedPartial", logs);
        eq("PASSED test: hitch", logs);
        eq("PASSED test: hitchWithArgs", logs);
        eq("PASSED test: hitchAsPartial", logs);
        eq("PASSED test: _toArray", logs);
        eq("PASSED test: clone", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.declare\" has 12 tests to run", logs);
        eq("PASSED test: smokeTest", logs);
        eq("PASSED test: smokeTest2", logs);
        eq("PASSED test: smokeTestWithCtor", logs);
        eq("PASSED test: smokeTestCompactArgs", logs);
        eq("PASSED test: subclass", logs);
        eq("PASSED test: subclassWithCtor", logs);
        eq("PASSED test: mixinSubclass", logs);
        eq("PASSED test: superclassRef", logs);
        eq("PASSED test: inheritedCall", logs);
        eq("PASSED test: inheritedExplicitCall", logs);
        eq("PASSED test: inheritedMixinCalls", logs);
        eq("PASSED test: mixinPreamble", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.connect\" has 10 tests to run", logs);
        eq("PASSED test: smokeTest", logs);
        eq("PASSED test: basicTest", logs);
        eq("PASSED test: hubConnectDisconnect1000", logs);
        eq("PASSED test: args4Test", logs);
        eq("PASSED test: args3Test", logs);
        eq("PASSED test: args2Test", logs);
        eq("PASSED test: scopeTest1", logs);
        eq("PASSED test: scopeTest2", logs);
        eq("PASSED test: connectPublisher", logs);
        eq("PASSED test: publishSubscribe1000", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.Deferred\" has 4 tests to run", logs);
        eq("debug from dojo.Deferred callback", logs);
        eq("PASSED test: callback", logs);
        eq("PASSED test: errback", logs);
        eq("PASSED test: callbackTwice", logs);
        eq("PASSED test: addBoth", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.json\" has 1 test to run", logs);
        eq("PASSED test: toAndFromJson", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.html\" has 1 test to run", logs);
        eq("57 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 57 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.marginBox('sq100').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.marginBox('sq100').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(120, dojo.marginBox('sq100margin10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(120, dojo.marginBox('sq100margin10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.contentBox('sq100margin10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.contentBox('sq100margin10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(140, dojo.marginBox('sq100margin10pad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(140, dojo.marginBox('sq100margin10pad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(120, dojo.marginBox('sq100pad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(120, dojo.marginBox('sq100pad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(110, dojo.marginBox('sq100ltpad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(110, dojo.marginBox('sq100ltpad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.contentBox('sq100ltpad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.contentBox('sq100ltpad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(120, dojo.marginBox('sq100ltpad10rbmargin10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(120, dojo.marginBox('sq100ltpad10rbmargin10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(120, dojo.marginBox('sq100border10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(120, dojo.marginBox('sq100border10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.contentBox('sq100border10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.contentBox('sq100border10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(140, dojo.marginBox('sq100border10margin10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(140, dojo.marginBox('sq100border10margin10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.contentBox('sq100border10margin10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.contentBox('sq100border10margin10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(160, dojo.marginBox('sq100border10margin10pad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(160, dojo.marginBox('sq100border10margin10pad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.contentBox('sq100border10margin10pad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.contentBox('sq100border10margin10pad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(100, dojo.marginBox('sq100nopos').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(10, dojo._getPadExtents(dojo.byId('sq100ltpad10rbmargin10')).l);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(10, dojo._getPadExtents(dojo.byId('sq100ltpad10rbmargin10')).t);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(10, dojo._getPadExtents(dojo.byId('sq100ltpad10rbmargin10')).w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(10, dojo._getPadExtents(dojo.byId('sq100ltpad10rbmargin10')).h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(0, dojo._getMarginExtents(dojo.byId('sq100ltpad10rbmargin10')).l);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(0, dojo._getMarginExtents(dojo.byId('sq100ltpad10rbmargin10')).t);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(10, dojo._getMarginExtents(dojo.byId('sq100ltpad10rbmargin10')).w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(10, dojo._getMarginExtents(dojo.byId('sq100ltpad10rbmargin10')).h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(10, dojo._getBorderExtents(dojo.byId('sq100border10margin10pad10')).l);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(10, dojo._getBorderExtents(dojo.byId('sq100border10margin10pad10')).t);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(20, dojo._getBorderExtents(dojo.byId('sq100border10margin10pad10')).w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(20, dojo._getBorderExtents(dojo.byId('sq100border10margin10pad10')).h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(20, dojo._getPadBorderExtents(dojo.byId('sq100border10margin10pad10')).l);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(20, dojo._getPadBorderExtents(dojo.byId('sq100border10margin10pad10')).t);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(40, dojo._getPadBorderExtents(dojo.byId('sq100border10margin10pad10')).w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(40, dojo._getPadBorderExtents(dojo.byId('sq100border10margin10pad10')).h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::coordsBasic", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::coordsMargin", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::sq100nopos", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::coordsScrolled", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(1, dojo.style('sq100nopos', 'opacity'));", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(0.1, dojo.style('sq100nopos', 'opacity', 0.1));", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is(0.8, dojo.style('sq100nopos', 'opacity', 0.8));", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::t.is('static', dojo.style('sq100nopos', 'position'));", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::getBgcolor", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::isDescendant", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::isDescendantIframe", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html::t::testClassFunctions", logs);
        eq("PASSED test: ../../dojo/tests/_base/html.html", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.html_rtl\" has 1 test to run", logs);
        eq("3 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 3 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_rtl.html::t::coordsWithVertScrollbar", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_rtl.html::t::coordsWithHorzScrollbar", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_rtl.html::t::eventClientXY", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_rtl.html", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.html_quirks\" has 1 test to run", logs);
        eq("32 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 32 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.marginBox('sq100').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.marginBox('sq100').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(120, dojo.marginBox('sq100margin10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(120, dojo.marginBox('sq100margin10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.contentBox('sq100margin10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.contentBox('sq100margin10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.marginBox('sq100nopos').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::coordsBasic", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::coordsMargin", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::sq100nopos", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(140, dojo.marginBox('sq100margin10pad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(140, dojo.marginBox('sq100margin10pad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(120, dojo.marginBox('sq100pad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(120, dojo.marginBox('sq100pad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(110, dojo.marginBox('sq100ltpad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(110, dojo.marginBox('sq100ltpad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.contentBox('sq100ltpad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.contentBox('sq100ltpad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(120, dojo.marginBox('sq100ltpad10rbmargin10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(120, dojo.marginBox('sq100ltpad10rbmargin10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(120, dojo.marginBox('sq100border10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(120, dojo.marginBox('sq100border10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.contentBox('sq100border10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.contentBox('sq100border10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(140, dojo.marginBox('sq100border10margin10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(140, dojo.marginBox('sq100border10margin10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.contentBox('sq100border10margin10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.contentBox('sq100border10margin10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(160, dojo.marginBox('sq100border10margin10pad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(160, dojo.marginBox('sq100border10margin10pad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.contentBox('sq100border10margin10pad10').w);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html::t::t.is(100, dojo.contentBox('sq100border10margin10pad10').h);", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_quirks.html", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.html_box\" has 1 test to run", logs);
        eq("6 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 6 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box.html::t::reciprocalTests", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box.html::t::fitTests", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box.html::t::fitTestsOverflow", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box.html::t::fitTestsFloat", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box.html::t::reciprocalTestsInline", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box.html::t::reciprocalTestsButtonChild", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box.html", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.html_box_quirks\" has 1 test to run", logs);
        eq("6 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 6 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box_quirks.html::t::reciprocalTests", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box_quirks.html::t::fitTests", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box_quirks.html::t::fitTestsOverflow", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box_quirks.html::t::fitTestsFloat", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box_quirks.html::t::reciprocalTestsInline", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box_quirks.html::t::reciprocalTestsButtonChild", logs);
        eq("PASSED test: ../../dojo/tests/_base/html_box_quirks.html", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.fx\" has 1 test to run", logs);
        eq("5 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 5 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/_base/fx.html::t::fadeOut", logs);
        eq("PASSED test: ../../dojo/tests/_base/fx.html::t::fadeIn", logs);
        eq("PASSED test: ../../dojo/tests/_base/fx.html::t::animateColor", logs);
        eq("PASSED test: ../../dojo/tests/_base/fx.html::t::animateColorBack", logs);
        // TODO: requires awareness of CSS 3 box-sizing; see http://www.quirksmode.org/css/box.html
        // eq("PASSED test: ../../dojo/tests/_base/fx.html::t::animateHeight", logs);
        logs.next();
        logs.next();
        logs.next();
        eq("PASSED test: ../../dojo/tests/_base/fx.html", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.query\" has 1 test to run", logs);
        eq("70 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 70 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(4, (dojo.query('h3')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('h1:first-child')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, (dojo.query('h3:first-child')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('#t')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('#bug')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(4, (dojo.query('#t h3')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('div#t')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(4, (dojo.query('div#t h3')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(0, (dojo.query('span#t')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('#t div > h3')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, (dojo.query('.foo')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('.foo.bar')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, (dojo.query('.baz')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, (dojo.query('#t > h3')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(12, (dojo.query('#t > *')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(12, (dojo.query('#t >')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, (dojo.query('.foo >')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, (dojo.query('.foo > *')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, (dojo.query('> *', 'container')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, (dojo.query('> h3', 't')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, (dojo.query('.foo, .bar')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, (dojo.query('.foo,.bar')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('.foo.bar')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, (dojo.query('.foo')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, (dojo.query('.baz')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('span.baz')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('sPaN.baz')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('SPAN.baz')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, (dojo.query('[foo~=\"bar\"]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, (dojo.query('[ foo ~= \"bar\" ]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, (dojo.query('[foo]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('[foo$=\"thud\"]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('[foo$=thud]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('[foo$=\"thudish\"]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('#t [foo$=thud]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('#t [ title $= thud ]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(0, (dojo.query('#t span[ title $= thud ]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('[foo|=\"bar\"]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, (dojo.query('[foo|=\"bar-baz\"]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(0, (dojo.query('[foo|=\"baz\"]')).length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(dojo.byId('_foo'), dojo.query('.foo:nth-child(2)')[0]);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(dojo.query('style')[0], dojo.query(':nth-child(2)')[0]);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, dojo.query('>', 'container').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, dojo.query('> *', 'container').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, dojo.query('> [qux]', 'container').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is('child1', dojo.query('> [qux]', 'container')[0].id);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is('child3', dojo.query('> [qux]', 'container')[1].id);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, dojo.query('>', 'container').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, dojo.query('> *', 'container').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is('passed', dojo.query('#bug')[0].value);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, dojo.query('#t span.foo:not(span:first-child)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, dojo.query('#t span.foo:not(:first-child)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, dojo.query('#t > h3:nth-child(odd)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, dojo.query('#t h3:nth-child(odd)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, dojo.query('#t h3:nth-child(2n+1)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, dojo.query('#t h3:nth-child(even)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, dojo.query('#t h3:nth-child(2n)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(0, dojo.query('#t h3:nth-child(2n+3)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(2, dojo.query('#t h3:nth-child(1)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, dojo.query('#t > h3:nth-child(1)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(3, dojo.query('#t :nth-child(3)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(0, dojo.query('#t > div:nth-child(1)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(7, dojo.query('#t span').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(4, dojo.query('#t > span:empty').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(6, dojo.query('#t span:empty').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(0, dojo.query('h3 span:empty').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::doh.is(1, dojo.query('h3 :not(:empty)').length);", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::silly_IDs1", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::NodeList_identity", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html::t::xml", logs);
        eq("PASSED test: ../../dojo/tests/_base/query.html", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.NodeList\" has 1 test to run", logs);
        eq("25 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 25 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::ctor", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::ctorArgs", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::ctorArgs2", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::forEach", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::indexOf", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::lastIndexOf", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::every", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::some", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::map", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::slice", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::splice", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::spliceInsert", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::spliceDel", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::spliceInsertDel", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::query", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::filter", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::coords", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::styleGet", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::styleSet", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::styles", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::concat", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::concat2", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::place", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::orphan", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html::t::connect", logs);
        eq("PASSED test: ../../dojo/tests/_base/NodeList.html", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests._base.xhr\" has 1 test to run", logs);
        eq("27 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 27 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::formNodeToObject", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::formIdToObject", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::formToObjectWithMultiSelect", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::objectToQuery", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::objectToQueryArr", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::formToQuery", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::formToQueryArr", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::formToJson", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::formToJsonArr", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::queryToObject", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::textContentHandler", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::jsonContentHandler", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::jsonCFContentHandler", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::jsContentHandler", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xmlContentHandler", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrGet", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrGet404", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrGetContent", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrGetForm", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrGetFormWithContent", logs);
        // TODO: these tests fail in FF when hitting the Jetty server, but not when hitting the filesystem.
        logs.next(); // eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrPost", logs);
        logs.next(); // eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrPostWithContent", logs);
        logs.next(); // eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrPostForm", logs);
        logs.next(); // eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::rawXhrPost", logs);
        logs.next(); // eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrPut", logs);
        logs.next(); // eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrDelete", logs);
        logs.next();
        logs.next();
        logs.next();
        logs.next();
        logs.next();
        logs.next();
        logs.next();
        logs.next();
        logs.next();
        logs.next();
        logs.next();
        logs.next();
        eq("PASSED test: ../../dojo/tests/_base/xhr.html::t::xhrCancel", logs);
        eq("PASSED test: ../../dojo/tests/_base/xhr.html", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.i18n\" has 9 tests to run", logs);
        eq("PASSED test: salutations-de", logs);
        eq("PASSED test: salutations-en", logs);
        eq("PASSED test: salutations-en-au", logs);
        eq("PASSED test: salutations-en-us", logs);
        eq("PASSED test: salutations-en-us-texas", logs);
        eq("PASSED test: salutations-en-us-new_york", logs);
        eq("PASSED test: salutations-en-us-new_york-brooklyn", logs);
        eq("PASSED test: salutations-xx", logs);
        eq("PASSED test: salutations-zh-cn", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.back.hash\" has 1 test to run", logs);
        eq("PASSED test: getAndSet", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.cldr\" has 1 test to run", logs);
        eq("PASSED test: test_date_getWeekend", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.data.utils\" has 18 tests to run", logs);
        eq("PASSED test: testWildcardFilter_1", logs);
        eq("PASSED test: testWildcardFilter_2", logs);
        eq("PASSED test: testWildcardFilter_3", logs);
        eq("PASSED test: testWildcardFilter_4", logs);
        eq("PASSED test: testWildcardFilter_5", logs);
        eq("PASSED test: testWildcardFilter_caseInsensitive", logs);
        eq("PASSED test: testSingleChar_1", logs);
        eq("PASSED test: testSingleChar_2", logs);
        eq("PASSED test: testBracketChar", logs);
        eq("PASSED test: testBraceChar", logs);
        eq("PASSED test: testParenChar", logs);
        eq("PASSED test: testPlusChar", logs);
        eq("PASSED test: testPeriodChar", logs);
        eq("PASSED test: testBarChar", logs);
        eq("PASSED test: testDollarSignChar", logs);
        eq("PASSED test: testCarrotChar", logs);
        eq("PASSED test: testEscapeChar", logs);
        eq("PASSED test: testAbsoluteMatch", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.data.readOnlyItemFileTestTemplates, with datastore dojo.data.ItemFileReadStore\" has 56 tests to run", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity()", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity() notFound", logs);
        eq("PASSED test: Identity API: getIdentityAttributes()", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity() commentFilteredJson", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity() nullValue", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity() booleanValue", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity() withoutSpecifiedIdInData", logs);
        eq("PASSED test: Identity API: getIdentity()", logs);
        eq("PASSED test: Identity API: getIdentity() withoutSpecifiedId", logs);
        eq("PASSED test: Read API: fetch() all", logs);
        eq("PASSED test: Read API: fetch() one", logs);
        eq("PASSED test: Read API: fetch() shallow", logs);
        eq("PASSED test: Read API: fetch() Multiple", logs);
        eq("PASSED test: Read API: fetch() MultipleMixedFetch", logs);
        eq("PASSED test: Read API: fetch() deep", logs);
        eq("PASSED test: Read API: fetch() one_commentFilteredJson", logs);
        eq("PASSED test: Read API: fetch() withNull", logs);
        eq("PASSED test: Read API: fetch() all_streaming", logs);
        eq("PASSED test: Read API: fetch() paging", logs);
        eq("PASSED test: Read API: fetch() with MultiType Match", logs);
        eq("PASSED test: Read API: fetch() with MultiType, MultiValue Match", logs);
        eq("PASSED test: Read API: getLabel()", logs);
        eq("PASSED test: Read API: getLabelAttributes()", logs);
        eq("PASSED test: Read API: getValue()", logs);
        eq("PASSED test: Read API: getValues()", logs);
        eq("PASSED test: Read API: isItem()", logs);
        eq("PASSED test: Read API: isItem() multistore", logs);
        eq("PASSED test: Read API: hasAttribute()", logs);
        eq("PASSED test: Read API: containsValue()", logs);
        eq("PASSED test: Read API: getAttributes()", logs);
        eq("PASSED test: Read API: getFeatures()", logs);
        eq("PASSED test: Read API: fetch() patternMatch0", logs);
        eq("PASSED test: Read API: fetch() patternMatch1", logs);
        eq("PASSED test: Read API: fetch() patternMatch2", logs);
        eq("PASSED test: Read API: fetch() patternMatch_caseSensitive", logs);
        eq("PASSED test: Read API: fetch() patternMatch_caseInsensitive", logs);
        eq("PASSED test: Read API: fetch() sortNumeric", logs);
        eq("PASSED test: Read API: fetch() sortNumericDescending", logs);
        eq("PASSED test: Read API: fetch() sortNumericWithCount", logs);
        eq("PASSED test: Read API: fetch() sortAlphabetic", logs);
        eq("PASSED test: Read API: fetch() sortAlphabeticDescending", logs);
        eq("PASSED test: Read API: fetch() sortDate", logs);
        eq("PASSED test: Read API: fetch() sortDateDescending", logs);
        eq("PASSED test: Read API: fetch() sortMultiple", logs);
        eq("PASSED test: Read API: fetch() sortMultipleSpecialComparator", logs);
        eq("PASSED test: Read API: fetch() sortAlphabeticWithUndefined", logs);
        eq("PASSED test: Read API: errorCondition_idCollision_inMemory", logs);
        eq("PASSED test: Read API: errorCondition_idCollision_xhr", logs);
        eq("PASSED test: Read API: Date_datatype", logs);
        eq("PASSED test: Read API: custom_datatype_Color_SimpleMapping", logs);
        eq("PASSED test: Read API: custom_datatype_Color_GeneralMapping", logs);
        eq("PASSED test: Read API: hierarchical_data", logs);
        eq("PASSED test: Identity API: no_identifier_specified", logs);
        eq("PASSED test: Identity API: hierarchical_data", logs);
        eq("PASSED test: Read API: functionConformance", logs);
        eq("PASSED test: Identity API: functionConformance", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.data.readOnlyItemFileTestTemplates, with datastore dojo.data.ItemFileWriteStore\" has 56 tests to run", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity()", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity() notFound", logs);
        eq("PASSED test: Identity API: getIdentityAttributes()", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity() commentFilteredJson", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity() nullValue", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity() booleanValue", logs);
        eq("PASSED test: Identity API: fetchItemByIdentity() withoutSpecifiedIdInData", logs);
        eq("PASSED test: Identity API: getIdentity()", logs);
        eq("PASSED test: Identity API: getIdentity() withoutSpecifiedId", logs);
        eq("PASSED test: Read API: fetch() all", logs);
        eq("PASSED test: Read API: fetch() one", logs);
        eq("PASSED test: Read API: fetch() shallow", logs);
        eq("PASSED test: Read API: fetch() Multiple", logs);
        eq("PASSED test: Read API: fetch() MultipleMixedFetch", logs);
        eq("PASSED test: Read API: fetch() deep", logs);
        eq("PASSED test: Read API: fetch() one_commentFilteredJson", logs);
        eq("PASSED test: Read API: fetch() withNull", logs);
        eq("PASSED test: Read API: fetch() all_streaming", logs);
        eq("PASSED test: Read API: fetch() paging", logs);
        eq("PASSED test: Read API: fetch() with MultiType Match", logs);
        eq("PASSED test: Read API: fetch() with MultiType, MultiValue Match", logs);
        eq("PASSED test: Read API: getLabel()", logs);
        eq("PASSED test: Read API: getLabelAttributes()", logs);
        eq("PASSED test: Read API: getValue()", logs);
        eq("PASSED test: Read API: getValues()", logs);
        eq("PASSED test: Read API: isItem()", logs);
        eq("PASSED test: Read API: isItem() multistore", logs);
        eq("PASSED test: Read API: hasAttribute()", logs);
        eq("PASSED test: Read API: containsValue()", logs);
        eq("PASSED test: Read API: getAttributes()", logs);
        eq("PASSED test: Read API: getFeatures()", logs);
        eq("PASSED test: Read API: fetch() patternMatch0", logs);
        eq("PASSED test: Read API: fetch() patternMatch1", logs);
        eq("PASSED test: Read API: fetch() patternMatch2", logs);
        eq("PASSED test: Read API: fetch() patternMatch_caseSensitive", logs);
        eq("PASSED test: Read API: fetch() patternMatch_caseInsensitive", logs);
        eq("PASSED test: Read API: fetch() sortNumeric", logs);
        eq("PASSED test: Read API: fetch() sortNumericDescending", logs);
        eq("PASSED test: Read API: fetch() sortNumericWithCount", logs);
        eq("PASSED test: Read API: fetch() sortAlphabetic", logs);
        eq("PASSED test: Read API: fetch() sortAlphabeticDescending", logs);
        eq("PASSED test: Read API: fetch() sortDate", logs);
        eq("PASSED test: Read API: fetch() sortDateDescending", logs);
        eq("PASSED test: Read API: fetch() sortMultiple", logs);
        eq("PASSED test: Read API: fetch() sortMultipleSpecialComparator", logs);
        eq("PASSED test: Read API: fetch() sortAlphabeticWithUndefined", logs);
        eq("PASSED test: Read API: errorCondition_idCollision_inMemory", logs);
        eq("PASSED test: Read API: errorCondition_idCollision_xhr", logs);
        eq("PASSED test: Read API: Date_datatype", logs);
        eq("PASSED test: Read API: custom_datatype_Color_SimpleMapping", logs);
        eq("PASSED test: Read API: custom_datatype_Color_GeneralMapping", logs);
        eq("PASSED test: Read API: hierarchical_data", logs);
        eq("PASSED test: Identity API: no_identifier_specified", logs);
        eq("PASSED test: Identity API: hierarchical_data", logs);
        eq("PASSED test: Read API: functionConformance", logs);
        eq("PASSED test: Identity API: functionConformance", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.data.ItemFileWriteStore\" has 25 tests to run", logs);
        eq("PASSED test: test_getFeatures", logs);
        eq("PASSED test: testWriteAPI_setValue", logs);
        eq("PASSED test: testWriteAPI_setValues", logs);
        eq("PASSED test: testWriteAPI_unsetAttribute", logs);
        eq("PASSED test: testWriteAPI_newItem", logs);
        eq("PASSED test: testWriteAPI_newItem_withParent", logs);
        eq("PASSED test: testWriteAPI_newItem_multiple_withParent", logs);
        eq("PASSED test: testWriteAPI_deleteItem", logs);
        eq("PASSED test: testWriteAPI_isDirty", logs);
        eq("PASSED test: testWriteAPI_revert", logs);
        eq("PASSED test: testWriteAPI_save", logs);
        eq("PASSED test: testWriteAPI_saveVerifyState", logs);
        eq("PASSED test: testWriteAPI_saveEverything", logs);
        eq("PASSED test: testWriteAPI_saveEverything_withDateType", logs);
        eq("PASSED test: testWriteAPI_saveEverything_withCustomColorTypeSimple", logs);
        eq("PASSED test: testWriteAPI_saveEverything_withCustomColorTypeGeneral", logs);
        eq("PASSED test: testWriteAPI_newItem_revert", logs);
        eq("PASSED test: testNotificationAPI_onSet", logs);
        eq("PASSED test: testNotificationAPI_onNew", logs);
        eq("PASSED test: testNotificationAPI_onDelete", logs);
        eq("PASSED test: testReadAPI_functionConformanceToo", logs);
        eq("PASSED test: testWriteAPI_functionConformance", logs);
        eq("PASSED test: testNotificationAPI_functionConformance", logs);
        eq("PASSED test: testIdentityAPI_noIdentifierSpecified", logs);
        eq("PASSED test: testIdentityAPI_noIdentifierSpecified_revert", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.date.util\" has 3 tests to run", logs);
        eq("PASSED test: test_date_getDaysInMonth", logs);
        eq("PASSED test: test_date_isLeapYear", logs);
        eq("PASSED test: test_date_getTimezoneName", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.date.math\" has 12 tests to run", logs);
        eq("PASSED test: test_date_compare", logs);
        eq("PASSED test: test_date_add", logs);
        eq("PASSED test: test_date_diff", logs);
        eq("PASSED test: test_date_add_diff_year", logs);
        eq("PASSED test: test_date_add_diff_quarter", logs);
        eq("PASSED test: test_date_add_diff_month", logs);
        eq("PASSED test: test_date_add_diff_week", logs);
        eq("PASSED test: test_date_add_diff_day", logs);
        eq("PASSED test: test_date_add_diff_weekday", logs);
        eq("PASSED test: test_date_add_diff_hour", logs);
        eq("PASSED test: test_date_add_diff_minute", logs);
        eq("PASSED test: test_date_add_diff_second", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.date.locale\" has 9 tests to run", logs);
        eq("PASSED test: date.locale", logs);
        eq("PASSED test: isWeekend", logs);
        eq("PASSED test: format", logs);
        eq("PASSED test: parse_dates", logs);
        eq("PASSED test: parse_dates_neg", logs);
        eq("PASSED test: parse_datetimes", logs);
        eq("PASSED test: parse_times", logs);
        eq("PASSED test: day_of_year", logs);
        eq("PASSED test: week_of_year", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.date.stamp\" has 2 tests to run", logs);
        eq("PASSED test: test_date_iso", logs);
        eq("PASSED test: test_date_iso_tz", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.number\" has 26 tests to run", logs);
        eq("PASSED test: number", logs);
        eq("PASSED test: format", logs);
        eq("PASSED test: parse", logs);
        eq("PASSED test: format_icu4j3_6", logs);
        eq("PASSED test: format_patterns", logs);
        eq("PASSED test: exponential", logs);
        eq("PASSED test: format_quotes", logs);
        eq("PASSED test: format_rounding", logs);
        eq("PASSED test: format_scientific", logs);
        eq("PASSED test: format_perMill", logs);
        eq("PASSED test: format_grouping", logs);
        eq("PASSED test: format_pad", logs);
        eq("PASSED test: parse_icu4j3_6", logs);
        eq("PASSED test: parse_whitespace", logs);
        eq("PASSED test: number_regression_1", logs);
        eq("PASSED test: number_regression_2", logs);
        eq("PASSED test: number_regression_3", logs);
        eq("PASSED test: number_regression_4", logs);
        eq("PASSED test: number_regression_5", logs);
        eq("PASSED test: number_regression_6", logs);
        eq("PASSED test: number_regression_7", logs);
        eq("PASSED test: number_regression_8", logs);
        eq("PASSED test: number_regression_9", logs);
        eq("PASSED test: number_regression_10", logs);
        eq("PASSED test: number_regression_11", logs);
        eq("PASSED test: number_regression_12", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.currency\" has 1 test to run", logs);
        eq("PASSED test: currency", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.AdapterRegistry\" has 5 tests to run", logs);
        eq("PASSED test: ctor", logs);
        eq("PASSED test: register", logs);
        eq("PASSED test: noMatch", logs);
        eq("PASSED test: returnWrappers", logs);
        eq("PASSED test: unregister", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.io.script\" has 1 test to run", logs);
        eq("3 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 3 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/io/script.html::t::ioScriptSimple", logs);
        eq("PASSED test: ../../dojo/tests/io/script.html::t::ioScriptJsonp", logs);
        // ioScriptJsonpTimeout passes when using a single thread for all background JS tasks.
        eq("PASSED test: ../../dojo/tests/io/script.html::t::ioScriptJsonpTimeout", logs);
        eq("PASSED test: ../../dojo/tests/io/script.html", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"tests.io.iframe\" has 1 test to run", logs);
        eq("4 tests to run in 1 groups", logs);
        eq(GROUP_DELIMITER, logs);

        eq("GROUP \"t\" has 4 tests to run", logs);
        eq("PASSED test: ../../dojo/tests/io/iframe.html::t::ioIframeGetText", logs);
        eq("PASSED test: ../../dojo/tests/io/iframe.html::t::ioIframeGetJson", logs);
        eq("PASSED test: ../../dojo/tests/io/iframe.html::t::ioIframeGetJavascript", logs);
        eq("PASSED test: ../../dojo/tests/io/iframe.html::t::ioIframeGetHtml", logs);
        eq("PASSED test: ../../dojo/tests/io/iframe.html", logs);
        eq(GROUP_DELIMITER, logs);
    }

    private void eq(final String expected, final Iterator<HtmlElement> i) {
        assertEquals(expected, i.next().asText().trim());
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        client_.closeAllWindows();
    }
}
