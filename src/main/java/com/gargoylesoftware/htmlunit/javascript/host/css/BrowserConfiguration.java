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

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Allows to specify for which {@link BrowserVersion} a style attribute is defined.
 * Quite experimental: it allows to do more than what we had previously but let's see if
 * this is the right way.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
abstract class BrowserConfiguration {

    private String defaultValue_;

    BrowserConfiguration(final String defaultValue) {
        defaultValue_ = defaultValue;
    }

    String getDefaultValue() {
        return defaultValue_;
    }

    abstract boolean matches(BrowserVersion browserVersion);

    public boolean isIteratable() {
        return true;
    }

    static BrowserConfiguration getMatchingConfiguration(
            final BrowserVersion browserVersion,
            final BrowserConfiguration[] browserConfigurations) {

        for (final BrowserConfiguration browserConfiguration : browserConfigurations) {
            if (browserConfiguration.matches(browserVersion)) {
                return browserConfiguration;
            }
        }

        return null;
    }

    static BrowserConfiguration chromeAndEdge(final String defaultValue) {
        return new ChromeAndEdge(defaultValue);
    }

    static BrowserConfiguration edge(final String defaultValue) {
        return new Edge(defaultValue);
    }

    static BrowserConfiguration chromeAndEdgeAndFirefox(final String defaultValue) {
        return new ChromeAndEdgeAndFirefox(defaultValue);
    }

    static BrowserConfiguration chromeAndEdgeNotIterable(final String defaultValue) {
        return new ChromeAndEdgeNotIterable(defaultValue);
    }

    static BrowserConfiguration ff(final String defaultValue) {
        return new FF(defaultValue);
    }

    static BrowserConfiguration ffNotIterable(final String defaultValue) {
        return new FFNotIterable(defaultValue);
    }

    static BrowserConfiguration ffLatest(final String defaultValue) {
        return new FFLatest(defaultValue);
    }

    static BrowserConfiguration ie(final String defaultValue) {
        return new IE(defaultValue);
    }

    static BrowserConfiguration ieNotIterable(final String defaultValue) {
        return new IENotIterable(defaultValue);
    }

    static class ChromeAndEdgeNotIterable extends BrowserConfiguration {
        ChromeAndEdgeNotIterable(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isChrome() || browserVersion == BrowserVersion.EDGE;
        }

        @Override
        public boolean isIteratable() {
            return false;
        }
    }

    private static class Edge extends BrowserConfiguration {
        Edge(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isEdge();
        }
    }

    private static class ChromeAndEdge extends BrowserConfiguration {
        ChromeAndEdge(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isChrome() || browserVersion == BrowserVersion.EDGE;
        }
    }

    private static class ChromeAndEdgeAndFirefox extends BrowserConfiguration {
        ChromeAndEdgeAndFirefox(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isChrome()
                    || browserVersion == BrowserVersion.EDGE
                    || browserVersion.isFirefox();
        }
    }

    private static class FF extends BrowserConfiguration {
        FF(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isFirefox();
        }
    }

    private static class FFNotIterable extends BrowserConfiguration {
        FFNotIterable(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isFirefox();
        }

        @Override
        public boolean isIteratable() {
            return false;
        }
    }

    private static class FFLatest extends BrowserConfiguration {
        FFLatest(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isFirefox()
                    && browserVersion.getBrowserVersionNumeric()
                        > BrowserVersion.FIREFOX_78.getBrowserVersionNumeric();
        }
    }

    private static class IE extends BrowserConfiguration {
        IE(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isIE();
        }
    }

    private static class IENotIterable extends BrowserConfiguration {
        IENotIterable(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isIE();
        }

        @Override
        public boolean isIteratable() {
            return false;
        }
    }
}
