/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Responsible for URL creation.
 * @author Marc Guillemot
 * @version $Revision$
 */
abstract class URLCreator {
    abstract URL toUrlUnsafeClassic(final String url) throws MalformedURLException;

    /**
     * Gets the instance responsible for URL creating, detecting if we are running on GoogleAppEngine
     * where custom URLStreamHandler is prohibited.
     */
    static URLCreator getCreator() {
        try {
            return new URLCreatorStandard();
        }
        catch (final NoClassDefFoundError e) {
            return new URLCreatorGAE();
        }
    }

    /**
     * For the normal case.
     */
    static class URLCreatorStandard extends URLCreator {
        private static final URLStreamHandler JS_HANDLER
            = new com.gargoylesoftware.htmlunit.protocol.javascript.Handler();
        private static final URLStreamHandler ABOUT_HANDLER
            = new com.gargoylesoftware.htmlunit.protocol.about.Handler();
        private static final URLStreamHandler DATA_HANDLER = new com.gargoylesoftware.htmlunit.protocol.data.Handler();

        @Override
        URL toUrlUnsafeClassic(final String url) throws MalformedURLException {
            if (TextUtil.startsWithIgnoreCase(url, "javascript:")) {
                return new URL(null, url, JS_HANDLER);
            }
            else if (TextUtil.startsWithIgnoreCase(url, "about:")) {
                if (WebClient.URL_ABOUT_BLANK != null
                        && org.apache.commons.lang.StringUtils.equalsIgnoreCase(
                                WebClient.URL_ABOUT_BLANK.toExternalForm(), url)) {
                    return WebClient.URL_ABOUT_BLANK;
                }
                return new URL(null, url, ABOUT_HANDLER);
            }
            else if (TextUtil.startsWithIgnoreCase(url, "data:")) {
                return new URL(null, url, DATA_HANDLER);
            }
            else {
                return new URL(url);
            }
        }
    }

    /**
     * For working on GoogleAppEngine. The URL hack will require special handling from a dedicated WebConnection.
     */
    static class URLCreatorGAE extends URLCreator {

        @Override
        URL toUrlUnsafeClassic(final String url) throws MalformedURLException {
            if (TextUtil.startsWithIgnoreCase(url, "javascript:")) {
                return new URL("http://gaeHack_" + url.replaceFirst(":", "/"));
            }
            else if (TextUtil.startsWithIgnoreCase(url, "about:")) {
                if (WebClient.URL_ABOUT_BLANK != null
                        && org.apache.commons.lang.StringUtils.equalsIgnoreCase(
                                WebClient.URL_ABOUT_BLANK.toExternalForm(), url)) {
                    return WebClient.URL_ABOUT_BLANK;
                }
                return new URL("http://gaeHack_" + url.replaceFirst(":", "/"));
            }
            else if (TextUtil.startsWithIgnoreCase(url, "data:")) {
                return new URL("http://gaeHack_" + url.replaceFirst(":", "/"));
            }
            else {
                return new URL(url);
            }
        }
    }
}

