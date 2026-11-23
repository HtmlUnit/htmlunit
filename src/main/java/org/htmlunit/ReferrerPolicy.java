/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit;

import static org.htmlunit.BrowserVersionFeatures.REFERRER_POLICY_IGNORE_CROSS_SITE_RELAXING;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.htmlunit.util.StringUtils;
import org.htmlunit.util.UrlUtils;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * The referrer policies; a referrer policy determines the referrer (the Referer header)
 * of a request.
 *
 * @author Ronald Brill
 * @see <a href="https://w3c.github.io/webappsec-referrer-policy/">Referrer Policy</a>
 */
public enum ReferrerPolicy {

    /** No referrer at all. */
    NO_REFERRER("no-referrer"),

    /** The full url, but no referrer from https to http. */
    NO_REFERRER_WHEN_DOWNGRADE("no-referrer-when-downgrade"),

    /** The full url for same-origin requests, no referrer for cross-origin requests. */
    SAME_ORIGIN("same-origin"),

    /** Only the origin. */
    ORIGIN("origin"),

    /** Only the origin, but no referrer from https to http. */
    STRICT_ORIGIN("strict-origin"),

    /** The full url for same-origin requests, only the origin for cross-origin requests. */
    ORIGIN_WHEN_CROSS_ORIGIN("origin-when-cross-origin"),

    /**
     * The full url for same-origin requests, only the origin for cross-origin requests,
     * no referrer from https to http; this is the default.
     */
    STRICT_ORIGIN_WHEN_CROSS_ORIGIN("strict-origin-when-cross-origin"),

    /** Always the full url. */
    UNSAFE_URL("unsafe-url");

    /** The default policy. */
    public static final ReferrerPolicy DEFAULT = STRICT_ORIGIN_WHEN_CROSS_ORIGIN;

    // the referrer url is limited to 4096 chars, longer urls are replaced by the origin
    private static final int MAX_REFERRER_LENGTH = 4096;

    private final String token_;

    ReferrerPolicy(final String token) {
        token_ = token;
    }

    /**
     * Returns the token of this policy (e.g. 'no-referrer').
     *
     * @return the token
     */
    public String getToken() {
        return token_;
    }

    /**
     * Returns the policy for the given token (case-insensitive); used for the referrerpolicy
     * attribute.
     *
     * @param token the token
     * @return the policy or {@code null} if the token is not a known policy
     */
    public static ReferrerPolicy fromToken(final String token) {
        if (token == null) {
            return null;
        }
        final String normalized = token.trim().toLowerCase(Locale.ROOT);
        for (final ReferrerPolicy policy : values()) {
            if (policy.token_.equals(normalized)) {
                return policy;
            }
        }
        return null;
    }

    /**
     * Returns the policy for the content of a meta element with the name 'referrer';
     * the legacy values ('never', 'default', 'always' and 'origin-when-crossorigin') are supported.
     *
     * @param content the content
     * @return the policy or {@code null} if the content is not a known policy
     * @see <a href="https://html.spec.whatwg.org/multipage/semantics.html#meta-referrer">meta referrer</a>
     */
    public static ReferrerPolicy fromMetaContent(final String content) {
        if (content == null) {
            return null;
        }
        switch (content.trim().toLowerCase(Locale.ROOT)) {
            case "never":
                return NO_REFERRER;
            case "default":
                return DEFAULT;
            case "always":
                return UNSAFE_URL;
            case "origin-when-crossorigin":
                return ORIGIN_WHEN_CROSS_ORIGIN;
            default:
                return fromToken(content);
        }
    }

    /**
     * Returns the policy of the value of the Referrer-Policy header: the last known token of the
     * comma separated list.
     *
     * @param headerValue the header value
     * @return the policy or {@code null} if the value contains no known token
     * @see <a href="https://w3c.github.io/webappsec-referrer-policy/#parse-referrer-policy-from-header">
     *     Parse a referrer policy from a Referrer-Policy header</a>
     */
    public static ReferrerPolicy fromHeader(final String headerValue) {
        if (StringUtils.isEmptyOrNull(headerValue)) {
            return null;
        }
        ReferrerPolicy result = null;
        for (final String token : StringUtils.splitAtComma(headerValue)) {
            final ReferrerPolicy policy = fromToken(token);
            if (policy != null) {
                result = policy;
            }
        }
        return result;
    }

    /**
     * Determines the referrer for a request to the target url.
     *
     * @param referrerUrl the url of the referrer (without user info and ref)
     * @param targetUrl the url of the request
     * @param browserVersion the browser version
     * @return the referrer (the value of the Referer header) or {@code null} if there is no referrer
     * @see <a href="https://w3c.github.io/webappsec-referrer-policy/#determine-requests-referrer">
     *     Determine request's Referrer</a>
     */
    public String getReferrer(final URL referrerUrl, final URL targetUrl, final BrowserVersion browserVersion) {
        final String full = referrerUrl.toExternalForm();
        final String origin;
        try {
            origin = UrlUtils.getUrlWithProtocolAndAuthority(referrerUrl).toExternalForm() + "/";
        }
        catch (final MalformedURLException e) {
            return null;
        }
        final String referrer = full.length() > MAX_REFERRER_LENGTH ? origin : full;

        final boolean sameOrigin = UrlUtils.isSameOrigin(referrerUrl, targetUrl);
        final boolean downgrade = isTlsProtected(referrerUrl) && !isPotentiallyTrustworthy(targetUrl);

        ReferrerPolicy policy = this;
        if ((policy == UNSAFE_URL || policy == NO_REFERRER_WHEN_DOWNGRADE)
                && browserVersion.hasFeature(REFERRER_POLICY_IGNORE_CROSS_SITE_RELAXING)
                && !WebClient.isSameSite(referrerUrl, targetUrl)) {
            // Firefox ignores the less restrictive policies for cross-site requests
            policy = DEFAULT;
        }

        switch (policy) {
            case NO_REFERRER:
                return null;
            case NO_REFERRER_WHEN_DOWNGRADE:
                return downgrade ? null : referrer;
            case SAME_ORIGIN:
                return sameOrigin ? referrer : null;
            case ORIGIN:
                return origin;
            case STRICT_ORIGIN:
                return downgrade ? null : origin;
            case ORIGIN_WHEN_CROSS_ORIGIN:
                return sameOrigin ? referrer : origin;
            case UNSAFE_URL:
                return referrer;
            case STRICT_ORIGIN_WHEN_CROSS_ORIGIN:
            default:
                if (sameOrigin) {
                    return referrer;
                }
                return downgrade ? null : origin;
        }
    }

    private static boolean isTlsProtected(final URL url) {
        return "https".equals(url.getProtocol()) || "wss".equals(url.getProtocol());
    }

    /**
     * Returns whether the url is potentially trustworthy: https and localhost urls.
     * @see <a href="https://w3c.github.io/webappsec-secure-contexts/#is-url-trustworthy">
     *     Is url potentially trustworthy?</a>
     */
    private static boolean isPotentiallyTrustworthy(final URL url) {
        if (isTlsProtected(url)) {
            return true;
        }
        final String host = url.getHost().toLowerCase(Locale.ROOT);
        return "localhost".equals(host) || host.endsWith(".localhost")
                || host.startsWith("127.") || "[::1]".equals(host);
    }
}
