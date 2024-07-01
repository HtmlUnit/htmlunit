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
package org.htmlunit.javascript.host.performance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.json.JsonParser;
import org.htmlunit.corejs.javascript.json.JsonParser.ParseException;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code PerformanceNavigation}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class PerformanceNavigation extends HtmlUnitScriptable {

    private static final Log LOG = LogFactory.getLog(PerformanceNavigation.class);

    /** Navigate. */
    @JsxConstant
    public static final int TYPE_NAVIGATE = 0;

    /** Reload. */
    @JsxConstant
    public static final int TYPE_RELOAD = 1;

    /** Back forward. */
    @JsxConstant
    public static final int TYPE_BACK_FORWARD = 2;

    /** Reserved. */
    @JsxConstant
    public static final int TYPE_RESERVED = 255;

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public int getType() {
        return TYPE_NAVIGATE;
    }

    /**
     * Returns the {@code redirectCount} property.
     * @return the {@code redirectCount} property
     */
    @JsxGetter
    public int getRedirectCount() {
        return 0;
    }

    /**
     * The {@code toJSON} function.
     * @return the {@code toJSON} object
     */
    @JsxFunction
    public Object toJSON() {
        final String jsonString = new StringBuilder()
                .append("{\"type\":")
                .append(getType())
                .append(", \"redirectCount\":")
                .append(getRedirectCount())
                .append('}').toString();
        try {
            return new JsonParser(Context.getCurrentContext(), getParentScope()).parseValue(jsonString);
        }
        catch (final ParseException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Failed parsingJSON '" + jsonString + "' reason: " + e.getMessage());
            }
        }
        return null;
    }

}
