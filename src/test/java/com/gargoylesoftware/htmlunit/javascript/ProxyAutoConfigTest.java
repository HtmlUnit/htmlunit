/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for the {@link ProxyAutoConfig}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class ProxyAutoConfigTest extends SimpleWebTestCase {

    /**
     * Test case.
     */
    @Test
    public void shExpMatch() {
        assertTrue(ProxyAutoConfig.shExpMatch("http://home.netscape.com/people/ari/index.html", "*/ari/*"));
        assertFalse(ProxyAutoConfig.shExpMatch("http://home.netscape.com/people/montulli/index.html", "*/ari/*"));
    }

    /**
     * Test case.
     */
    @Test
    public void weekdayRange() {
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

    /**
     * Test case.
     */
    @Test
    public void dateRange() {
        final Object undefined = Undefined.instance;
        final Calendar calendar = Calendar.getInstance();
        final int today = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        final int tomorrow = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        final int yesterday = calendar.get(Calendar.DAY_OF_MONTH);
        assertTrue(ProxyAutoConfig.dateRange(String.valueOf(today),
                undefined, undefined, undefined, undefined, undefined, undefined));
        assertFalse(ProxyAutoConfig.dateRange(String.valueOf(yesterday),
                undefined, undefined, undefined, undefined, undefined, undefined));
        assertFalse(ProxyAutoConfig.dateRange(String.valueOf(tomorrow),
                undefined, undefined, undefined, undefined, undefined, undefined));
    }

    /**
     * Test case.
     */
    @Test
    public void timeRange() {
        final Object undefined = Undefined.instance;
        final Calendar calendar = Calendar.getInstance();
        final int now = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        final int after = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.add(Calendar.HOUR_OF_DAY, -4);
        final int before = calendar.get(Calendar.HOUR_OF_DAY);
        assertTrue(ProxyAutoConfig.timeRange(String.valueOf(now),
                undefined, undefined, undefined, undefined, undefined, undefined));
        assertFalse(ProxyAutoConfig.timeRange(String.valueOf(before),
                undefined, undefined, undefined, undefined, undefined, undefined));
        assertFalse(ProxyAutoConfig.timeRange(String.valueOf(after),
                undefined, undefined, undefined, undefined, undefined, undefined));
    }

    /**
     * Test case.
     */
    @Test
    public void bindings() {
        final String content = "ProxyConfig.bindings.com = 'my_com';\n"
            + "ProxyConfig.bindings.org = 'my_org';\n"
            + "ProxyConfig.bindings.net = 'my_net';\n"
            + "ProxyConfig.bindings.edu = 'my_edu';\n"
            + "ProxyConfig.bindings.gov = 'my_gov';\n"
            + "function FindProxyForURL(url, host) {\n"
            + "  var returnValue = '';\n"
            + "  for (var x in ProxyConfig.bindings) {\n"
            + "    returnValue += x + ' ';\n"
            + "  }\n"
            + "  return returnValue;\n"
            + "}\n";
        final String value = ProxyAutoConfig.evaluate(content, URL_FIRST);
        assertEquals("com org net edu gov ", value);
    }

}
