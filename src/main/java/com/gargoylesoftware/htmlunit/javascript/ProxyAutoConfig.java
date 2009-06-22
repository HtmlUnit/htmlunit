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
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * Provides an implementation of Proxy Auto-Config (PAC).
 *
 * @see <a href="http://lib.ru/WEBMASTER/proxy-live.txt">PAC file format</a>
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class ProxyAutoConfig {

    private ProxyAutoConfig() {
    }

    /**
     * Evaluates the <tt>FindProxyForURL</tt> method of the specified content.
     * @param content the JavaScript content
     * @param url the URL to be retrieved
     * @return semicolon-separated result
     */
    public static String evaluate(final String content, final URL url) {
        final Context cx = ContextFactory.getGlobal().enterContext();
        try {
            final ProxyAutoConfig config = new ProxyAutoConfig();
            final Scriptable scope = cx.initStandardObjects();

            config.defineMethod("isPlainHostName", scope);
            config.defineMethod("dnsDomainIs", scope);
            config.defineMethod("localHostOrDomainIs", scope);
            config.defineMethod("isResolvable", scope);
            config.defineMethod("isInNet", scope);
            config.defineMethod("dnsResolve", scope);
            config.defineMethod("myIpAddress", scope);
            config.defineMethod("dnsDomainLevels", scope);
            config.defineMethod("shExpMatch", scope);
            config.defineMethod("weekdayRange", scope);

            cx.evaluateString(scope, content, "<cmd>", 1, null);
            final Object functionArgs[] = {url.toExternalForm(), url.getHost()};
            final Object fObj = scope.get("FindProxyForURL", scope);

            final NativeFunction f = (NativeFunction) fObj;
            final Object result = f.call(cx, scope, scope, functionArgs);
            return Context.toString(result);
        }
        finally {
            Context.exit();
        }
    }

    private void defineMethod(final String methodName, final Scriptable scope) {
        for (Method method : getClass().getMethods()) {
            if (method.getName().equals(methodName)) {
                final FunctionObject functionObject = new FunctionObject(methodName, method, scope);
                ((ScriptableObject) scope).defineProperty(methodName, functionObject, ScriptableObject.EMPTY);
            }
        }
    }

    /**
     * Returns true if there is no domain name in the hostname (no dots).
     * @param host the hostname from the URL (excluding port number).
     * @return true if there is no domain name in the hostname (no dots).
     */
    public static boolean isPlainHostName(final String host) {
        return host.indexOf('.') == -1;
    }

    /**
     * Returns true if the domain of hostname matches.
     * @param host the hostname from the URL
     * @param domain the domain name to test the hostname against
     * @return true if the domain of hostname matches.
     */
    public static boolean dnsDomainIs(final String host, final String domain) {
        return host.endsWith(domain);
    }

    /**
     * Returns true if the hostname matches exactly the specified hostname,
     * or if there is no domain name part in the hostname, but the unqualified hostname matches.
     * @param host the hostname from the URL
     * @param hostdom fully qualified hostname to match against
     * @return true if the hostname matches exactly the specified hostname,
     * or if there is no domain name part in the hostname, but the unqualified hostname matches.
     */
    public static boolean localHostOrDomainIs(final String host, final String hostdom) {
        return host.length() > 1 && host.equals(hostdom) || host.indexOf('.') == -1 && hostdom.startsWith(host);
    }

    /**
     * Tries to resolve the hostname. Returns true if succeeds.
     * @param host the hostname from the URL.
     * @return true if the specific hostname is resolvable.
     */
    public static boolean isResolvable(final String host) {
        return dnsResolve(host) != null;
    }

    /**
     * Returns true if the IP address of the host matches the specified IP address pattern.
     * @param host a DNS hostname, or IP address.
     * If a hostname is passed, it will be resolved into an IP address by this function.
     * @param pattern an IP address pattern in the dot-separated format
     * @param mask mask for the IP address pattern informing which parts of the IP address should be matched against.
     * 0 means ignore, 255 means match
     * @return true if the IP address of the host matches the specified IP address pattern.
     */
    public static boolean isInNet(final String host, final String pattern, final String mask) {
        final String[] hostTokens = dnsResolve(host).split("\\.");
        final String[] patternTokens = pattern.split("\\.");
        final String[] maskTokens = mask.split("\\.");
        for (int i = 0; i < hostTokens.length; i++) {
            if (Integer.parseInt(maskTokens[i]) != 0 && !hostTokens[i].equals(patternTokens[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Resolves the given DNS hostname into an IP address, and returns it in the dot separated format as a string.
     * @param host the hostname to resolve
     * @return the resolved IP address
     */
    public static String dnsResolve(final String host) {
        try {
            return InetAddress.getByName(host).getHostAddress();
        }
        catch (final Exception e) {
            return null;
        }
    }

    /**
     * Returns the IP address of the local host, as a string in the dot-separated integer format.
     * @return the IP address of the local host, as a string in the dot-separated integer format.
     */
    public static String myIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (final Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Returns the number (integer) of DNS domain levels (number of dots) in the hostname.
     * @param host the hostname from the URL
     * @return the number (integer) of DNS domain levels (number of dots) in the hostname.
     */
    public static int dnsDomainLevels(final String host) {
        int levels = 0;
        for (int i = host.length() - 1; i >= 0; i--) {
            if (host.charAt(i) == '.') {
                levels++;
            }
        }
        return levels;
    }

    /**
     * Matches the specified string against a shell expression, not regular expression.
     * @param str a string to match
     * @param shexp the shell expression
     * @return if the string matches
     */
    public static boolean shExpMatch(final String str, final String shexp) {
        final String regexp = shexp.replace(".", "\\.").replace("*", ".*").replace("?", ".");
        return str.matches(regexp);
    }

    /**
     * Checks if today is inclusive in the specified parameters.
     * @param wd1 week day 1
     * @param wd2 week day 2, optional
     * @param gmt string of "GMT", or not specified
     * @return if the string matches
     */
    public static boolean weekdayRange(final String wd1, Object wd2, final Object gmt) {
        TimeZone timezone = TimeZone.getDefault();
        if ("GMT".equals(Context.toString(gmt)) || "GMT".equals(Context.toString(wd2))) {
            timezone = TimeZone.getTimeZone("GMT");
        }
        if (wd2 == Undefined.instance || "GMT".equals(Context.toString(wd2))) {
            wd2 = wd1;
        }
        final Calendar calendar = Calendar.getInstance(timezone);
        for (int i = 0; i < 7; i++) {
            final String day = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
            if (day.equals(wd2)) {
                return true;
            }
            if (day.equals(wd1)) {
                return i == 0;
            }
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        return false;
    }
}
