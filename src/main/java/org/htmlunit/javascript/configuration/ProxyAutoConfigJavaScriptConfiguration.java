/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.configuration;

import java.util.Map;
import java.util.WeakHashMap;

import org.htmlunit.BrowserVersion;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.proxyautoconfig.ProxyAutoConfig;

/**
 * A container for all the JavaScript configuration information used for the
 * proxy AutoConfig support.
 *
 * @author Ronald Brill
 */
public final class ProxyAutoConfigJavaScriptConfiguration extends AbstractJavaScriptConfiguration {

    @SuppressWarnings("unchecked")
    static final Class<? extends HtmlUnitScriptable>[] CLASSES_ = new Class[] {ProxyAutoConfig.class};

    /** Cache of browser versions and their corresponding JavaScript configurations. */
    private static final Map<String, ProxyAutoConfigJavaScriptConfiguration> CONFIGURATION_MAP_ = new WeakHashMap<>();

    /**
     * Constructor is only called from {@link #getInstance(BrowserVersion)} which is synchronized.
     * @param browser the browser version to use
     */
    private ProxyAutoConfigJavaScriptConfiguration(final BrowserVersion browser) {
        super(browser);
    }

    /**
     * Returns the instance that represents the configuration for the specified {@link BrowserVersion}.
     * This method is synchronized to allow multi-threaded access to the JavaScript configuration.
     * @param browserVersion the {@link BrowserVersion}
     * @return the instance for the specified {@link BrowserVersion}
     */
    public static synchronized ProxyAutoConfigJavaScriptConfiguration getInstance(final BrowserVersion browserVersion) {
        if (browserVersion == null) {
            throw new IllegalArgumentException("BrowserVersion must be provided");
        }
        ProxyAutoConfigJavaScriptConfiguration configuration = CONFIGURATION_MAP_.get(browserVersion.getNickname());

        if (configuration == null) {
            configuration = new ProxyAutoConfigJavaScriptConfiguration(browserVersion);
            CONFIGURATION_MAP_.put(browserVersion.getNickname(), configuration);
        }
        return configuration;
    }

    @Override
    protected Class<? extends HtmlUnitScriptable>[] getClasses() {
        return CLASSES_;
    }
}
