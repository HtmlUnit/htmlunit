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
package org.htmlunit.javascript.host.worker;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.network.NetworkInformation;

/**
 * A JavaScript object for WorkerNavigator.
 *
 * @author Ronald Brill
 */
@JsxClass
public class WorkerNavigator extends HtmlUnitScriptable {

    private final BrowserVersion browserVersion_;

    /**
     * Default constructor.
     */
    public WorkerNavigator() {
        // prototype constructor
        super();
        browserVersion_ = null;
    }

    WorkerNavigator(final BrowserVersion browserVersion) {
        browserVersion_ = browserVersion;
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BrowserVersion getBrowserVersion() {
        return browserVersion_;
    }

    /**
     * Returns the {@code appCodeName} property.
     * @return the {@code appCodeName} property
     */
    @JsxGetter
    public String getAppCodeName() {
        return getBrowserVersion().getApplicationCodeName();
    }

    /**
     * Returns the {@code appName} property.
     * @return the {@code appName} property
     */
    @JsxGetter
    public String getAppName() {
        return getBrowserVersion().getApplicationName();
    }

    /**
     * Returns the {@code appVersion} property.
     * @return the {@code appVersion} property
     */
    @JsxGetter
    public String getAppVersion() {
        return getBrowserVersion().getApplicationVersion();
    }

    /**
     * Returns the language of the browser.
     * @return the language
     */
    @JsxGetter
    public String getLanguage() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the language of the browser.
     * @return the language
     */
    @JsxGetter
    public Scriptable getLanguages() {
        final String acceptLang = getBrowserVersion().getAcceptLanguageHeader();
        if (org.htmlunit.util.StringUtils.isEmptyOrNull(acceptLang)) {
            return JavaScriptEngine.newArray(this, 0);
        }

        final ArrayList<String> res = new ArrayList<>();
        final String[] parts = org.htmlunit.util.StringUtils.splitAtComma(acceptLang);
        for (final String part : parts) {
            if (!org.htmlunit.util.StringUtils.isEmptyOrNull(part)) {
                final String lang = StringUtils.substringBefore(part, ";").trim();
                if (!org.htmlunit.util.StringUtils.isEmptyOrNull(part)) {
                    res.add(lang);
                }
            }
        }

        return JavaScriptEngine.newArray(this, res.toArray());
    }

    /**
     * Returns the {@code platform} property.
     * @return the {@code platform} property
     */
    @JsxGetter
    public String getPlatform() {
        return getBrowserVersion().getPlatform();
    }

    /**
     * Returns the {@code product} property.
     * @return the {@code product} property
     */
    @JsxGetter
    public String getProduct() {
        return "Gecko";
    }

    /**
     * Returns the property {@code userAgent}.
     * @return the property {@code userAgent}
     */
    @JsxGetter
    public String getUserAgent() {
        return getBrowserVersion().getUserAgent();
    }

    /**
     * Returns the {@code connection} property.
     * @return the {@code connection} property
     */
    @JsxGetter({CHROME, EDGE})
    public NetworkInformation getConnection() {
        final NetworkInformation networkInformation = new NetworkInformation();
        networkInformation.setPrototype(getPrototype(networkInformation.getClass()));
        networkInformation.setParentScope(getParentScope());
        return networkInformation;
    }
}
