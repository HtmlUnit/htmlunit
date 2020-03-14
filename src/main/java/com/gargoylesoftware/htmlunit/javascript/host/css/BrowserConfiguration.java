/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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

    static BrowserConfiguration chrome(final String defaultValue) {
        return new Chrome(defaultValue);
    }

    static BrowserConfiguration chromeAndFirefox(final String defaultValue) {
        return new ChromeAndFirefox(defaultValue);
    }

    static BrowserConfiguration chromeNotIterable(final String defaultValue) {
        return new ChromeNotIterable(defaultValue);
    }

    static BrowserConfiguration ff(final String defaultValue) {
        return new FF(defaultValue);
    }

    static BrowserConfiguration ffNotIterable(final String defaultValue) {
        return new FFNotIterable(defaultValue);
    }

    static BrowserConfiguration ff60(final String defaultValue) {
        return new FF60(defaultValue);
    }

    static BrowserConfiguration ff60And68(final String defaultValue) {
        return new FF60And68(defaultValue);
    }

    static BrowserConfiguration ff68(final String defaultValue) {
        return new FF68(defaultValue);
    }

    static BrowserConfiguration ff68AndUp(final String defaultValue) {
        return new FF68AndUp(defaultValue);
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

    private static class Chrome extends BrowserConfiguration {
        Chrome(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isChrome();
        }
    }

    static class ChromeNotIterable extends BrowserConfiguration {
        ChromeNotIterable(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isChrome();
        }

        @Override
        public boolean isIteratable() {
            return false;
        }
    }

    private static class ChromeAndFirefox extends BrowserConfiguration {
        ChromeAndFirefox(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isChrome() || browserVersion.isFirefox();
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

    private static class FF60 extends BrowserConfiguration {
        FF60(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isFirefox() && browserVersion.getBrowserVersionNumeric() == 60;
        }
    }

    private static class FF68 extends BrowserConfiguration {
        FF68(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isFirefox() && browserVersion.getBrowserVersionNumeric() == 68;
        }
    }

    private static class FF60And68 extends BrowserConfiguration {
        FF60And68(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isFirefox() && browserVersion.getBrowserVersionNumeric() <= 68;
        }
    }

    private static class FF68AndUp extends BrowserConfiguration {
        FF68AndUp(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isFirefox() && browserVersion.getBrowserVersionNumeric() >= 68;
        }
    }

    private static class FFLatest extends BrowserConfiguration {
        FFLatest(final String defaultValue) {
            super(defaultValue);
        }

        @Override
        public boolean matches(final BrowserVersion browserVersion) {
            return browserVersion.isFirefox() && browserVersion.getBrowserVersionNumeric() > 68;
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
