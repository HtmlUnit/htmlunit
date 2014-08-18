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
package com.gargoylesoftware.htmlunit.javascript.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Tests for {@link HtmlUnitRegExpProxy}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class HtmlUnitRegExpProxy3Test {

    /**
     * Compute replacement value. Following cases occur:
     * - Invalid back references are treated as it in JS but not in Java.
     * - $$: should be replaced by $
     */
    @Test
    public void computeReplacementValue() {
        final String theString = "hello";
        final Matcher matcher0group = Pattern.compile("h").matcher("hello");
        final Matcher matcher1group = Pattern.compile("(h)").matcher("hello");
        matcher1group.find();

        final HtmlUnitRegExpProxy proxy = new HtmlUnitRegExpProxy(null, BrowserVersion.FIREFOX_24);

        Assert.assertEquals("$", proxy.computeReplacementValue("$$", theString, matcher0group));
        Assert.assertEquals("$$x$", proxy.computeReplacementValue("$$$$x$$", theString, matcher0group));

        Assert.assertEquals("$1", proxy.computeReplacementValue("$1", theString, matcher0group));
        Assert.assertEquals("$2", proxy.computeReplacementValue("$2", theString, matcher0group));
        Assert.assertEquals("h", proxy.computeReplacementValue("$1", theString, matcher1group));
        Assert.assertEquals("$2", proxy.computeReplacementValue("$2", theString, matcher1group));

        Assert.assertEquals("$", proxy.computeReplacementValue("$", theString, matcher0group));
        Assert.assertEquals("$", proxy.computeReplacementValue("$", theString, matcher1group));
        Assert.assertEquals("\\\\$", proxy.computeReplacementValue("\\\\$", theString, matcher1group));
        Assert.assertEquals("$", proxy.computeReplacementValue("$", theString, matcher1group));
    }

    /**
     * Verifies that back references in character classes are removed.
     * @see #jqueryPseudo()
     * @see #ignoreBackReferenceInCharacterClass()
     */
    @Test
    public void removeBackReferencesInCharacterClasses() {
        Assert.assertEquals("(a)(b)[^c]", HtmlUnitRegExpProxy.jsRegExpToJavaRegExp("(a)(b)[^\\2c]"));
        Assert.assertEquals("(a)(b)[c]", HtmlUnitRegExpProxy.jsRegExpToJavaRegExp("(a)(b)[\\2c]"));
        Assert.assertEquals("(a)(b)[\\\\2c]", HtmlUnitRegExpProxy.jsRegExpToJavaRegExp("(a)(b)[\\\\2c]"));
    }

    /**
     * Verifies that character without need are "un-escaped".
     * @see #backslash()
     */
    @Test
    public void unescapeIllegallyEscapedChars() {
        Assert.assertEquals("a", HtmlUnitRegExpProxy.jsRegExpToJavaRegExp("\\a"));

        final char[] specials = {'b', 'B', 'c', 'd', 'D', 'f', 'n', 'o', 'r', 's', 'S', 't', 'v', 'w', 'W', 'x'};
        for (final char c : specials) {
            Assert.assertEquals("\\" + c, HtmlUnitRegExpProxy.jsRegExpToJavaRegExp("\\" + c));
        }
    }
}
