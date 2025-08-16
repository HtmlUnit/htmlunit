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
package org.htmlunit;

import org.htmlunit.html.HtmlPage;

/**
 * Class to display version information about HtmlUnit. This is the class
 * that will be executed if the JAR file is run.
 *
 * @author Mike Bowler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class Version {

    /** Prevent instantiation. */
    private Version() {
        // Empty.
    }

    /**
     * The main entry point into this class.
     * @param args the arguments passed on the command line
     * @throws Exception if an error occurs
     */
    public static void main(final String[] args) throws Exception {
        if (args.length == 1 && "-SanityCheck".equals(args[0])) {
            runSanityCheck();
            return;
        }
        System.out.println(getProductName());
        System.out.println(getCopyright());
        System.out.println("Version: " + getProductVersion());
    }

    /**
     * Runs the sanity check.
     * @throws Exception if anything goes wrong
     */
    private static void runSanityCheck() throws Exception {
        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://www.htmlunit.org/index.html");
            page.executeJavaScript("document.location");
            System.out.println("Sanity check complete.");
        }
    }

    /**
     * Returns "HtmlUnit".
     * @return "HtmlUnit"
     */
    public static String getProductName() {
        return "HtmlUnit";
    }

    /**
     * Returns the current implementation version.
     * @return the current implementation version
     */
    public static String getProductVersion() {
        return Version.class.getPackage().getImplementationVersion();
    }

    /**
     * Returns the copyright notice.
     * @return the copyright notice
     */
    public static String getCopyright() {
        return "Copyright (c) 2002-2025 Gargoyle Software Inc. All rights reserved.";
    }
}
