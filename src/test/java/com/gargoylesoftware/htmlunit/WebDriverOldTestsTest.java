/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Runs the HtmlUnit "traditional" tests that have been captured using WebDriver.
 * Tests gets "captured" when they use {@link WebTestCase#createTestPageForRealBrowserIfNeeded(String, List)}.
 * In this case an HTML file (as well as the expected results) that can be run by this class is saved
 * in the target/generated_tests folder.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(Parameterized.class)
public class WebDriverOldTestsTest extends WebDriverTestCase {
    private static final Log log_ = LogFactory.getLog(WebDriverOldTestsTest.class);
    private final URL testFile_;
    private final List<String> expectedLog_ = new ArrayList<String>();
    
    @SuppressWarnings("unchecked")
    public WebDriverOldTestsTest(final File expectedFile) throws Exception {
        final FileInputStream fis = new FileInputStream(expectedFile);
        final ObjectInputStream oos = new ObjectInputStream(fis);
        final List<String> list = (List<String>) oos.readObject();
        for (final String s: list) {
            expectedLog_.add(s.trim());
        }
        oos.close();
        
        final String testFileName = expectedFile.getName().replaceFirst("\\.html\\..*", ".html");
        testFile_ = new File(expectedFile.getParentFile(), testFileName).toURL();
    }

    /**
     * Provides the data, ie the files on which the tests should run.
     * TODO: use a dedicated test runner instead of this parameterized runner. 
     * @return the tests files on which to run the tests
     */
    @org.junit.runners.Parameterized.Parameters
    public static Collection<File[]> data() {
        final File testsDir = new File("target/generated_tests");
        
        final File[] testFiles = testsDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                final String name = pathname.getName();
                return (name.endsWith(".html.expected") || name.endsWith(".html.FF2.expected"));
            }
        });
        
        final List<File[]> response = new ArrayList<File[]>();
        for (final File f : testFiles) {
            response.add(new File[] { f });
        }
        log_.info(response.size() + " tests found in folder " + testsDir);
        return response;
    }

    /**
     * Runs the test contained in the test file and compares the result with the expected ones.
     * @throws Throwable if the test fails
     */
    @Test
    public void test() throws Throwable {
        getWebDriver().get(testFile_.toExternalForm());

        // verifications
        Assert.assertEquals(testFile_.toExternalForm(), expectedLog_, getEntries("log"));
    }
}
