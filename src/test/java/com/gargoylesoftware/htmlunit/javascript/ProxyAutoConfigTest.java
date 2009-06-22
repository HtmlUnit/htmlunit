/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for the {@link ProxyAutoConfig}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class ProxyAutoConfigTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void shExpMatch() throws Exception {
        assertTrue(ProxyAutoConfig.shExpMatch("http://home.netscape.com/people/ari/index.html", "*/ari/*"));
        assertFalse(ProxyAutoConfig.shExpMatch("http://home.netscape.com/people/montulli/index.html", "*/ari/*"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void weekdayRange() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        final String today = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        final String tomorrow = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        final String yesterday = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
        assertTrue(ProxyAutoConfig.weekdayRange(today, Undefined.instance, Undefined.instance));
        assertTrue(ProxyAutoConfig.weekdayRange(today, tomorrow, Undefined.instance));
        assertTrue(ProxyAutoConfig.weekdayRange(yesterday, today, Undefined.instance));
        assertTrue(ProxyAutoConfig.weekdayRange(yesterday, tomorrow, Undefined.instance));
        assertFalse(ProxyAutoConfig.weekdayRange(tomorrow, yesterday, Undefined.instance));
    }
}
