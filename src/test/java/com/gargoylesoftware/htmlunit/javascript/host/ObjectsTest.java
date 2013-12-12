/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
/**
 * Tests for all host objects.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(Parameterized.class)
public class ObjectsTest extends SimpleWebTestCase {

    private static List<String> IE8_;
    private static List<String> IE9_;
    private static List<String> IE11_;
    private static List<String> FF17_;
    private static List<String> FF24_;

    private static List<String> IE8_SIMULATED_;
    private static List<String> IE9_SIMULATED_;
    private static List<String> IE11_SIMULATED_;
    private static List<String> FF17_SIMULATED_;
    private static List<String> FF24_SIMULATED_;

    private final String name_;
    private final BrowserVersion browserVersion_;

    /**
     * Returns the data for this parameterized test.
     * @return list of all test parameters
     * @throws Exception If an error occurs
     */
    @Parameters(name = "{index}: {0} - {1}")
    public static Collection<Object[]> data() throws Exception {
        IE8_ = getObjects(BrowserVersion.INTERNET_EXPLORER_8);
        IE9_ = getObjects(BrowserVersion.INTERNET_EXPLORER_9);
        IE11_ = getObjects(BrowserVersion.INTERNET_EXPLORER_11);
        FF17_ = getObjects(BrowserVersion.FIREFOX_17);
        FF24_ = getObjects(BrowserVersion.FIREFOX_24);
        Assert.assertEquals(IE8_.size(), IE9_.size());
        Assert.assertEquals(IE8_.size(), IE11_.size());
        Assert.assertEquals(IE8_.size(), FF17_.size());
        Assert.assertEquals(IE8_.size(), FF24_.size());
        IE8_SIMULATED_ = getSimulatedObjects(BrowserVersion.INTERNET_EXPLORER_8);
        IE9_SIMULATED_ = getSimulatedObjects(BrowserVersion.INTERNET_EXPLORER_9);
        IE11_SIMULATED_ = getSimulatedObjects(BrowserVersion.INTERNET_EXPLORER_11);
        FF17_SIMULATED_ = getSimulatedObjects(BrowserVersion.FIREFOX_17);
        FF24_SIMULATED_ = getSimulatedObjects(BrowserVersion.FIREFOX_24);
        Assert.assertEquals(IE8_SIMULATED_.size(), IE9_SIMULATED_.size());
        Assert.assertEquals(IE8_SIMULATED_.size(), IE11_SIMULATED_.size());
        Assert.assertEquals(IE8_SIMULATED_.size(), FF17_SIMULATED_.size());
        Assert.assertEquals(IE8_SIMULATED_.size(), FF24_SIMULATED_.size());
        final Collection<Object[]> list = new ArrayList<Object[]>();
        for (final String line : IE8_) {
//            final String name = line.substring(0, line.indexOf(':'));
//            list.add(new Object[] {name, BrowserVersion.INTERNET_EXPLORER_8});
//            list.add(new Object[] {name, BrowserVersion.INTERNET_EXPLORER_9});
//            list.add(new Object[] {name, BrowserVersion.INTERNET_EXPLORER_11});
//            list.add(new Object[] {name, BrowserVersion.FIREFOX_17});
//            list.add(new Object[] {name, BrowserVersion.FIREFOX_24});
        }
        return list;
    }

    private static List<String> getObjects(final BrowserVersion browserVersion) throws Exception {
        final URL url = ObjectsTest.class.getClassLoader().getResource(
                "objects/objects." + browserVersion.getNickname() + ".txt");
        return FileUtils.readLines(new File(url.toURI()));
    }

    private static List<String> getSimulatedObjects(final BrowserVersion browserVersion) throws Exception {
        final URL url = ObjectsTest.class.getClassLoader().getResource("objects/objects.html");
        final WebClient webClient = new WebClient(browserVersion);
        final HtmlPage page = webClient.getPage(url);
        final HtmlTextArea textarea = page.getHtmlElementById("myTextarea");
        return Arrays.asList(textarea.getText().split("\r\n|\n"));
    }

    /**
     * Constructs a new test.
     * @param name the name of the object
     * @param browserVersion the browser version
     */
    public ObjectsTest(final String name, final BrowserVersion browserVersion) {
        name_ = name;
        browserVersion_ = browserVersion;
    }

    /**
     * Test.
     */
    @Test
    public void test() {
        final List<String> realList;
        final List<String> simulatedList;
        if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_8) {
            realList = IE8_;
            simulatedList = IE8_SIMULATED_;
        }
        else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_9) {
            realList = IE9_;
            simulatedList = IE9_SIMULATED_;
        }
        else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_11) {
            realList = IE11_;
            simulatedList = IE11_SIMULATED_;
        }
        else if (browserVersion_ == BrowserVersion.FIREFOX_17) {
            realList = FF17_;
            simulatedList = FF17_SIMULATED_;
        }
        else if (browserVersion_ == BrowserVersion.FIREFOX_24) {
            realList = FF24_;
            simulatedList = FF24_SIMULATED_;
        }
        else {
            fail("Unknown BrowserVersion " + browserVersion_);
            return;
        }

        Assert.assertEquals("Test for [" + browserVersion_.getNickname() + ':' + name_ + ']',
                getValueOf(realList, name_), getValueOf(simulatedList, name_));
    }

    private String getValueOf(final List<String> list, final String name) {
        for (final String line : list) {
            if (line.substring(0, line.indexOf(':')).equals(name)) {
                return line.substring(line.indexOf(':') + 1);
            }
        }
        return null;
    }
}
