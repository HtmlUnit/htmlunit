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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Allows to specify for which {@link BrowserVersion} a style attribute is defined.
 * Quite experimental: it allows to do more than what we had previously but let's see if
 * this is the right way.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Frank Danek
 *
 */
class BrowserConfiguration {
    private String browserFamily_;
    private String defaultValue_;
    private int minVersionNumber_ = -1;
    private int maxVersionNumber_ = Integer.MAX_VALUE;

    public static BrowserConfiguration ff(final String defaultValue) {
        final BrowserConfiguration browserConfiguration = new BrowserConfiguration();
        browserConfiguration.browserFamily_ = "FF";
        browserConfiguration.defaultValue_ = defaultValue;
        return browserConfiguration;
    }

    public static BrowserConfiguration ie(final String defaultValue) {
        final BrowserConfiguration browserConfiguration = new BrowserConfiguration();
        browserConfiguration.browserFamily_ = "IE";
        browserConfiguration.defaultValue_ = defaultValue;
        return browserConfiguration;
    }

    public static BrowserConfiguration ffBelow24(final String defaultValue) {
        return ff(defaultValue).upTo(23);
    }

    public static BrowserConfiguration ff24up(final String defaultValue) {
        return ff(defaultValue).startingWith(24);
    }

    public static BrowserConfiguration ffBelow31(final String defaultValue) {
        return ff(defaultValue).upTo(30);
    }

    public static BrowserConfiguration ff31up(final String defaultValue) {
        return ff(defaultValue).startingWith(31);
    }

    public static BrowserConfiguration ie8up(final String defaultValue) {
        return ie(defaultValue).startingWith(8);
    }

    public static BrowserConfiguration ieBelow11(final String defaultValue) {
        return ie(defaultValue).upTo(10);
    }

    public static BrowserConfiguration ie11up(final String defaultValue) {
        return ie(defaultValue).startingWith(11);
    }

    public static boolean isDefined(final BrowserVersion browserVersion,
            final BrowserConfiguration[] browserConfigurations) {
        if (browserConfigurations == null) {
            return true; // defined for all browsers
        }

        return getMatchingConfiguration(browserVersion, browserConfigurations) != null;
    }

    public static BrowserConfiguration getMatchingConfiguration(
            final BrowserVersion browserVersion,
            final BrowserConfiguration[] browserConfigurations) {

        for (final BrowserConfiguration browserConfiguration : browserConfigurations) {
            if (browserVersion.getNickname().startsWith(browserConfiguration.browserFamily_)
                    && browserVersion.getBrowserVersionNumeric() >= browserConfiguration.minVersionNumber_
                    && browserVersion.getBrowserVersionNumeric() <= browserConfiguration.maxVersionNumber_) {
                return browserConfiguration;
            }
        }

        return null;
    }

    public String getDefaultValue() {
        return defaultValue_;
    }

    public BrowserConfiguration startingWith(final int minVersionNumber) {
        if (minVersionNumber_ != -1) {
            throw new IllegalStateException("startingWith has already been set to " + minVersionNumber_);
        }
        minVersionNumber_ = minVersionNumber;
        return this;
    }

    public BrowserConfiguration upTo(final int maxVersionNumber) {
        if (maxVersionNumber_ != Integer.MAX_VALUE) {
            throw new IllegalStateException("below has already been set to " + maxVersionNumber_);
        }
        maxVersionNumber_ = maxVersionNumber;
        return this;
    }
}
