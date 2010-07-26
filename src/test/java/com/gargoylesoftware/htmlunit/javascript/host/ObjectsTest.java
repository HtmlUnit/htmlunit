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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
/**
 * Tests for all host objects.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(Parameterized.class)
public class ObjectsTest extends WebTestCase {

    private static List<String> IE6_;
    private static List<String> IE7_;
    private static List<String> IE8_;
    private static List<String> FF2_;
    private static List<String> FF3_;
    private static List<String> FF3_6_;

    private static List<String> IE6_SIMULATED_;
    private static List<String> IE7_SIMULATED_;
    private static List<String> IE8_SIMULATED_;
    private static List<String> FF2_SIMULATED_;
    private static List<String> FF3_SIMULATED_;
    private static List<String> FF3_6_SIMULATED_;

    private final String name_;
    private final BrowserVersion browserVersion_;

    /**
     * Returns the data for this parameterized test.
     * @return list of all test parameters
     * @throws Exception If an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        IE6_ = getObjects(BrowserVersion.INTERNET_EXPLORER_6);
        IE7_ = getObjects(BrowserVersion.INTERNET_EXPLORER_7);
        IE8_ = getObjects(BrowserVersion.INTERNET_EXPLORER_8);
        FF2_ = getObjects(BrowserVersion.FIREFOX_2);
        FF3_ = getObjects(BrowserVersion.FIREFOX_3);
        FF3_6_ = getObjects(BrowserVersion.FIREFOX_3_6);
        Assert.assertEquals(IE6_.size(), IE7_.size());
        Assert.assertEquals(IE6_.size(), IE8_.size());
        Assert.assertEquals(IE6_.size(), FF2_.size());
        Assert.assertEquals(IE6_.size(), FF3_.size());
        Assert.assertEquals(IE6_.size(), FF3_6_.size());
        IE6_SIMULATED_ = getSimulatedObjects(BrowserVersion.INTERNET_EXPLORER_6);
        IE7_SIMULATED_ = getSimulatedObjects(BrowserVersion.INTERNET_EXPLORER_7);
        IE8_SIMULATED_ = getSimulatedObjects(BrowserVersion.INTERNET_EXPLORER_8);
        FF2_SIMULATED_ = getSimulatedObjects(BrowserVersion.FIREFOX_2);
        FF3_SIMULATED_ = getSimulatedObjects(BrowserVersion.FIREFOX_3);
        FF3_6_SIMULATED_ = getSimulatedObjects(BrowserVersion.FIREFOX_3_6);
        Assert.assertEquals(IE6_SIMULATED_.size(), IE7_SIMULATED_.size());
        Assert.assertEquals(IE6_SIMULATED_.size(), IE8_SIMULATED_.size());
        Assert.assertEquals(IE6_SIMULATED_.size(), FF2_SIMULATED_.size());
        Assert.assertEquals(IE6_SIMULATED_.size(), FF3_SIMULATED_.size());
        Assert.assertEquals(IE6_SIMULATED_.size(), FF3_6_SIMULATED_.size());
        final Collection<Object[]> list = new ArrayList<Object[]>();
        for (final String line : IE6_) {
            final String name = line.substring(0, line.indexOf(':'));
            list.add(new Object[] {name, BrowserVersion.INTERNET_EXPLORER_6});
            list.add(new Object[] {name, BrowserVersion.INTERNET_EXPLORER_7});
            list.add(new Object[] {name, BrowserVersion.INTERNET_EXPLORER_8});
            list.add(new Object[] {name, BrowserVersion.FIREFOX_2});
            list.add(new Object[] {name, BrowserVersion.FIREFOX_3});
            list.add(new Object[] {name, BrowserVersion.FIREFOX_3_6});
        }
        return list;
    }

    @SuppressWarnings("unchecked")
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
        if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_6) {
            realList = IE6_;
            simulatedList = IE6_SIMULATED_;
        }
        else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_7) {
            realList = IE7_;
            simulatedList = IE7_SIMULATED_;
        }
        else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_8) {
            realList = IE8_;
            simulatedList = IE8_SIMULATED_;
        }
        else if (browserVersion_ == BrowserVersion.FIREFOX_3) {
            realList = FF3_;
            simulatedList = FF3_SIMULATED_;
        }
        else if (browserVersion_ == BrowserVersion.FIREFOX_3_6) {
            realList = FF3_6_;
            simulatedList = FF3_6_SIMULATED_;
        }
        else if (browserVersion_ == BrowserVersion.FIREFOX_2) {
            realList = FF2_;
            simulatedList = FF2_SIMULATED_;
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
