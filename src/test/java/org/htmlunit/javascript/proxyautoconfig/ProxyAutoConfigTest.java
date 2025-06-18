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
package org.htmlunit.javascript.proxyautoconfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.corejs.javascript.Undefined;
import org.htmlunit.javascript.JavaScriptEngine;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ProxyAutoConfig}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
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
        final Calendar calendar = Calendar.getInstance(Locale.ROOT);
        final DateFormat dateFormat = new SimpleDateFormat("EEE", Locale.ROOT);
        final String today = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ROOT);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        final String tomorrow = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ROOT);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        final String yesterday = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ROOT);
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
        final Calendar calendar = Calendar.getInstance(Locale.ROOT);
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
        final Calendar calendar = Calendar.getInstance(Locale.ROOT);
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
    public void simple() {
        final String content = "function FindProxyForURL(url, host) { "
                + "return \"PROXY proxy.example.com:8080; DIRECT\"; }";
        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME, content, URL_FIRST);
        assertEquals("PROXY proxy.example.com:8080; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void someConditions() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (shExpMatch(host,\"*.example.com\")) {\n"
                + "    return \"DIRECT\";\n"
                + "  }\n"

                + "  return \"PROXY proxy.example.com:8000; DIRECT\";\n"
                + "}";

        String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("ftp://www.example.com"));
        assertEquals("DIRECT", value);

        value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME, content, new URL("ftp://example.com"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
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
        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME, content, URL_FIRST);
        assertEquals("com org net edu gov ", value);
    }

    /**
     * Test case for #1649.
     */
    @Test
    public void isInNet() {
        final boolean isInNet = ProxyAutoConfig.isInNet("www.1234abcd.abc", "172.16.0.0", "255.240.0.0");
        assertFalse(isInNet);
    }

    /**
     * Test case for #1911.
     */
    @Test
    public void isInNetRange() {
        final boolean isInNet = ProxyAutoConfig.isInNet("172.22.0.7", "172.16.0.0", "255.240.0.0");
        assertTrue(isInNet);
    }

    /**
     * Test case.
     */
    @Test
    public void isPlainHostNameFalse() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (isPlainHostName('www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void isPlainHostNameTrue() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (isPlainHostName('www')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void dnsDomainIsFalse() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (dnsDomainIs('www', '.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void dnsDomainIsTrue() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (dnsDomainIs('www.mozilla.org', '.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void localHostOrDomainIsFalseDomain() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (localHostOrDomainIs('www.google.com', 'www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "   return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void localHostOrDomainIsFalseHostname() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (localHostOrDomainIs('home.mozilla.org', 'www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void localHostOrDomainIsTrue() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (localHostOrDomainIs('www.mozilla.org', 'www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void localHostOrDomainIsTrueNoDomain() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (localHostOrDomainIs('www', 'www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void isResolvableFalse() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (isResolvable('A17.2547876535.817')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("PROXY proxy.example.com:8000; DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void isResolvableTrue() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  if (isResolvable('www.mozilla.org')) {\n"
                + "    return 'DIRECT';\n"
                + "  }\n"

                + "  return 'PROXY proxy.example.com:8000; DIRECT';\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("DIRECT", value);
    }

    /**
     * Test case.
     */
    @Test
    public void dnsResolve() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  return dnsResolve('www.wetator.org');\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertTrue(value, value.contains("."));
    }

    /**
     * Test case.
     */
    @Test
    public void convertAddr() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  return convert_addr('192.0.2.172');\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertEquals("-1073741140", value);
    }

    /**
     * Test case.
     */
    @Test
    public void myIpAddress() throws MalformedURLException {
        final String content = "function FindProxyForURL(url, host) {\n"
                + "  return myIpAddress();\n"
                + "}";

        final String value = JavaScriptEngine.evaluateProxyAutoConfig(BrowserVersion.CHROME,
                                            content, new URL("https://www.mozilla.org"));
        assertTrue(value, value.contains("."));
    }
}
