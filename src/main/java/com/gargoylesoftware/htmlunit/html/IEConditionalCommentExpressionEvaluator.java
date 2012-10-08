/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Evaluator for IE conditional expressions.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms537512.aspx">MSDN documentation</a>
 * @version $Revision$
 * @author Marc Guillemot
 */
final class IEConditionalCommentExpressionEvaluator {

    /**
     * Hide constructor of utility class
     */
    private IEConditionalCommentExpressionEvaluator() {
        // nothing
    }

    /**
     * Evaluates the condition.
     * @param condition the condition like "lt IE 7"
     * @param browserVersion the browser version. Note that currently it can only be an IE browser.
     * @return the evaluation result
     */
    public static boolean evaluate(String condition, final BrowserVersion browserVersion) {
        condition = condition.trim();
        if ("IE".equals(condition)) {
            return true;
        }
        else if ("true".equals(condition)) {
            return true;
        }
        else if ("false".equals(condition)) {
            return false;
        }
        else if (condition.contains("&")) {
            return evaluate(StringUtils.substringBefore(condition, "&"), browserVersion)
                && evaluate(StringUtils.substringAfter(condition, "&"), browserVersion);
        }
        else if (condition.contains("|")) {
            return evaluate(StringUtils.substringBefore(condition, "|"), browserVersion)
                || evaluate(StringUtils.substringAfter(condition, "|"), browserVersion);
        }
        else if (condition.startsWith("!")) {
            return !evaluate(condition.substring(1), browserVersion);
        }
        else if (condition.startsWith("IE")) {
            final String currentVersion = Float.toString(browserVersion.getBrowserVersionNumeric());
            return currentVersion.startsWith(condition.substring(2).trim());
        }
        else if (condition.startsWith("lte IE")) {
            return browserVersion.getBrowserVersionNumeric() <= parseVersion(condition.substring(6));
        }
        else if (condition.startsWith("lt IE")) {
            return browserVersion.getBrowserVersionNumeric() < parseVersion(condition.substring(5));
        }
        else if (condition.startsWith("gt IE")) {
            return browserVersion.getBrowserVersionNumeric() > parseVersion(condition.substring(5));
        }
        else if (condition.startsWith("gte IE")) {
            return browserVersion.getBrowserVersionNumeric() >= parseVersion(condition.substring(6));
        }
        else if (condition.startsWith("lt")) {
            return true;
        }
        else if (condition.startsWith("gt")) {
            return false;
        }
        else if (condition.startsWith("(")) {
            // in fact not fully correct if () can be nested
            return evaluate(StringUtils.substringBetween(condition, "(", ")"), browserVersion);
        }
        else {
            return false;
        }
    }

    private static float parseVersion(final String s) {
        return Float.parseFloat(s);
    }
}
