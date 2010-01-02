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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Common functionalities for library tests.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public abstract class LibraryTestCase extends WebTestCase {

    /**
     * Loads an expectation file for the given test in the library folder.
     * TODO: use browser version specific variations too
     * @param testName the base name for the file
     * @return the content of the file
     * @throws Exception in case of error
     */
    protected String loadExpectation(final String testName) throws Exception {
        // TODO: check first if a file specific to current browserVersion exists first with version number then without
        final String resource = getLibraryDir() + "/" + testName + ".expected.txt";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);
        final File file = new File(url.toURI());

        return FileUtils.readFileToString(file, "UTF-8");
    }

    /**
     * Gets the base folder containing the files for this JS library.
     * @return the path without "/" at the end
     */
    protected abstract String getLibraryDir();

}
