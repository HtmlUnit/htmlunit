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
package com.gargoylesoftware.htmlunit.javascript;
import java.net.URL;

/**
 * Provides an implementation of Proxy Auto-Config (PAC).
 *
 * @see <a href="http://lib.ru/WEBMASTER/proxy-live.txt">PAC file format</a>
 *
 * @version $Revision$
 * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig}
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@Deprecated
public final class ProxyAutoConfig {

    private ProxyAutoConfig() {
    }

    /**
     * Evaluates the <tt>FindProxyForURL</tt> method of the specified content.
     * @param content the JavaScript content
     * @param url the URL to be retrieved
     * @return semicolon-separated result
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig#evaluate(String, URL)}
     */
    @Deprecated
    public static String evaluate(final String content, final URL url) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.evaluate(content, url);
    }

    /**
     * Returns true if there is no domain name in the hostname (no dots).
     * @param host the hostname from the URL (excluding port number).
     * @return true if there is no domain name in the hostname (no dots).
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig#isPlainHostName(String)}
     */
    @Deprecated
    public static boolean isPlainHostName(final String host) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.isPlainHostName(host);
    }

    /**
     * Returns true if the domain of hostname matches.
     * @param host the hostname from the URL
     * @param domain the domain name to test the hostname against
     * @return true if the domain of hostname matches.
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig#dnsDomainIs(String, String)}
     */
    @Deprecated
    public static boolean dnsDomainIs(final String host, final String domain) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.dnsDomainIs(host, domain);
    }

    /**
     * Returns true if the hostname matches exactly the specified hostname,
     * or if there is no domain name part in the hostname, but the unqualified hostname matches.
     * @param host the hostname from the URL
     * @param hostdom fully qualified hostname to match against
     * @return true if the hostname matches exactly the specified hostname,
     * or if there is no domain name part in the hostname, but the unqualified hostname matches.
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig
     *  #localHostOrDomainIs(String, String)}
     */
    @Deprecated
    public static boolean localHostOrDomainIs(final String host, final String hostdom) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.localHostOrDomainIs(host, hostdom);
    }

    /**
     * Tries to resolve the hostname. Returns true if succeeds.
     * @param host the hostname from the URL.
     * @return true if the specific hostname is resolvable.
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig#isResolvable(String)}
     */
    @Deprecated
    public static boolean isResolvable(final String host) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.isResolvable(host);
    }

    /**
     * Returns true if the IP address of the host matches the specified IP address pattern.
     * @param host a DNS hostname, or IP address.
     * If a hostname is passed, it will be resolved into an IP address by this function.
     * @param pattern an IP address pattern in the dot-separated format
     * @param mask mask for the IP address pattern informing which parts of the IP address should be matched against.
     * 0 means ignore, 255 means match
     * @return true if the IP address of the host matches the specified IP address pattern.
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig#isInNet(String, String, String)}
     */
    @Deprecated
    public static boolean isInNet(final String host, final String pattern, final String mask) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.isInNet(host, pattern, mask);
    }

    /**
     * Resolves the given DNS hostname into an IP address, and returns it in the dot separated format as a string.
     * @param host the hostname to resolve
     * @return the resolved IP address
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig#dnsResolve(String)}
     */
    @Deprecated
    public static String dnsResolve(final String host) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.dnsResolve(host);
    }

    /**
     * Returns the IP address of the local host, as a string in the dot-separated integer format.
     * @return the IP address of the local host, as a string in the dot-separated integer format.
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig#myIpAddress()}
     */
    @Deprecated
    public static String myIpAddress() {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.myIpAddress();
    }

    /**
     * Returns the number (integer) of DNS domain levels (number of dots) in the hostname.
     * @param host the hostname from the URL
     * @return the number (integer) of DNS domain levels (number of dots) in the hostname.
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig#dnsDomainLevels(String)}
     */
    @Deprecated
    public static int dnsDomainLevels(final String host) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.dnsDomainLevels(host);
    }

    /**
     * Matches the specified string against a shell expression, not regular expression.
     * @param str a string to match
     * @param shexp the shell expression
     * @return if the string matches
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig#shExpMatch(String, String)}
     */
    @Deprecated
    public static boolean shExpMatch(final String str, final String shexp) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.shExpMatch(str, shexp);
    }

    /**
     * Checks if today is included in the specified range.
     * @param wd1 week day 1
     * @param wd2 week day 2, optional
     * @param gmt string of "GMT", or not specified
     * @return if today is in range
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig
     *  #weekdayRange(String, Object, Object)}
     */
    @Deprecated
    public static boolean weekdayRange(final String wd1, final Object wd2, final Object gmt) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.weekdayRange(wd1, wd2, gmt);
    }

    /**
     * Checks if today is included in the specified range.
     * @param value1 the value 1
     * @param value2 the value 2
     * @param value3 the value 3
     * @param value4 the value 4
     * @param value5 the value 5
     * @param value6 the value 6
     * @param value7 the value 7
     * @return if today is in range
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig
     *  #dateRange(String, Object, Object, Object, Object, Object, Object)}
     */
    @Deprecated
    public static boolean dateRange(final String value1, final Object value2, final Object value3,
            final Object value4, final Object value5, final Object value6, final Object value7) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.
                dateRange(value1, value2, value3, value4, value5, value6, value7);
    }

    /**
     * Checks if the time now is included in the specified range.
     * @param value1 the value 1
     * @param value2 the value 2
     * @param value3 the value 3
     * @param value4 the value 4
     * @param value5 the value 5
     * @param value6 the value 6
     * @param value7 the value 7
     * @return if the time now is in the range
     *
     * @deprecated as of 2.16, use {@link com.gargoylesoftware.htmlunit.ProxyAutoConfig
     *  #timeRange(String, Object, Object, Object, Object, Object, Object)}
     */
    @Deprecated
    public static boolean timeRange(final String value1, final Object value2, final Object value3,
            final Object value4, final Object value5, final Object value6, final Object value7) {
        return com.gargoylesoftware.htmlunit.ProxyAutoConfig.
                timeRange(value1, value2, value3, value4, value5, value6, value7);
    }
}
