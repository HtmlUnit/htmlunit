/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.junit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.htmlunit.BrowserVersion;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebTestCase;
import org.junit.jupiter.api.extension.ClassTemplateInvocationContext;
import org.junit.jupiter.api.extension.ClassTemplateInvocationContextProvider;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;


/**
 * ClassTemplateInvocationContextProvider that spreads the test for different browsers.
 *
 * @author Ronald Brill
 */
public class BrowserVersionClassTemplateInvocationContextProvider implements ClassTemplateInvocationContextProvider {

    static final String REAL_CHROME = "chrome";
    static final String REAL_FIREFOX = "ff";
    static final String REAL_FIREFOX_ESR = "ff-esr";
    static final String REAL_EDGE = "edge";

    static final String REAL_CHROME_BIDI = "chrome-bidi";
    static final String REAL_FIREFOX_BIDI = "ff-bidi";
    static final String REAL_FIREFOX_ESR_BIDI = "ff-esr-bidi";
    static final String REAL_EDGE_BIDI = "edge-bidi";

    static final String HTMLUNIT_CHROME = "hu-chrome";
    static final String HTMLUNIT_FIREFOX = "hu-ff";
    static final String HTMLUNIT_FIREFOX_ESR = "hu-ff-esr";
    static final String HTMLUNIT_EDGE = "hu-edge";

    @Override
    public boolean supportsClassTemplate(final ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<ClassTemplateInvocationContext>
                provideClassTemplateInvocationContexts(final ExtensionContext context) {

        final ArrayList<ClassTemplateInvocationContext> invocationContexts = new ArrayList<>();

        final Set<String> browsers = WebDriverTestCase.getBrowsersProperties();
        if (browsers.contains(REAL_CHROME)) {
            invocationContexts.add(invocationContext(BrowserVersion.CHROME, true, false));
        }
        if (browsers.contains(REAL_FIREFOX_ESR)) {
            invocationContexts.add(invocationContext(BrowserVersion.FIREFOX_ESR, true, false));
        }
        if (browsers.contains(REAL_FIREFOX)) {
            invocationContexts.add(invocationContext(BrowserVersion.FIREFOX, true, false));
        }
        if (browsers.contains(REAL_EDGE)) {
            invocationContexts.add(invocationContext(BrowserVersion.EDGE, true, false));
        }

        // the real browsers started with BiDi enabled; same expectations as the classic ones
        if (browsers.contains(REAL_CHROME_BIDI)) {
            invocationContexts.add(invocationContext(BrowserVersion.CHROME, true, true));
        }
        if (browsers.contains(REAL_FIREFOX_ESR_BIDI)) {
            invocationContexts.add(invocationContext(BrowserVersion.FIREFOX_ESR, true, true));
        }
        if (browsers.contains(REAL_FIREFOX_BIDI)) {
            invocationContexts.add(invocationContext(BrowserVersion.FIREFOX, true, true));
        }
        if (browsers.contains(REAL_EDGE_BIDI)) {
            invocationContexts.add(invocationContext(BrowserVersion.EDGE, true, true));
        }

        if (browsers.contains(HTMLUNIT_CHROME)) {
            invocationContexts.add(invocationContext(BrowserVersion.CHROME, false, false));
        }
        if (browsers.contains(HTMLUNIT_FIREFOX_ESR)) {
            invocationContexts.add(invocationContext(BrowserVersion.FIREFOX_ESR, false, false));
        }
        if (browsers.contains(HTMLUNIT_FIREFOX)) {
            invocationContexts.add(invocationContext(BrowserVersion.FIREFOX, false, false));
        }
        if (browsers.contains(HTMLUNIT_EDGE)) {
            invocationContexts.add(invocationContext(BrowserVersion.EDGE, false, false));
        }

        return invocationContexts.stream();
    }

    private static ClassTemplateInvocationContext invocationContext(
            final BrowserVersion browserVersion, final boolean realBrowser, final boolean bidi) {

        return new ClassTemplateInvocationContext() {
            @Override
            public String getDisplayName(final int invocationIndex) {
                return (realBrowser ? "Real " : "") + browserVersion.getNickname() + (bidi ? " (BiDi)" : "");
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                final ArrayList<Extension> extensions = new ArrayList<>();

                extensions.add((TestInstancePostProcessor) (testInstance, context) -> {
                    if (testInstance instanceof WebTestCase) {
                        final WebTestCase webTestCase = (WebTestCase) testInstance;

                        webTestCase.setBrowserVersion(browserVersion);
                    }
                    if (testInstance instanceof WebDriverTestCase) {
                        final WebDriverTestCase webDriverTestCase = (WebDriverTestCase) testInstance;

                        webDriverTestCase.setUseRealBrowser(realBrowser);
                        webDriverTestCase.setUseBiDi(bidi);
                    }
                });

                return extensions;
            }
        };
    }
}
