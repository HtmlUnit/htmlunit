/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.io.FilenameUtils;

import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.BrowserFeature;
import com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Objects of this class represent one specific version of a given browser. Predefined
 * constants are provided for common browser versions.
 *
 * <p>You can create a different browser setup by using the BrowserVersionFactory.
 * <pre id='htmlUnitCode'>
 *         final String applicationName = "APPNAME";
 *         final String applicationVersion = "APPVERSION";
 *         final String userAgent = "USERAGENT";
 *
 *         final BrowserVersion browser =
 *                 new BrowserVersion.BrowserVersionBuilder(BrowserVersion.FIREFOX_78)
 *                     .setApplicationName(applicationName)
 *                     .setApplicationVersion(applicationVersion)
 *                     .setUserAgent(userAgent)
 *                     .build();
 * </pre>
 * <p>But keep in mind this now one still behaves like a FF78, only the stuff reported to the
 * outside is changed. This is more or less the same you can do with real browsers installing
 * plugins like UserAgentSwitcher.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public final class BrowserVersion implements Serializable {

    /**
     * Application name the Netscape navigator series of browsers.
     */
    private static final String NETSCAPE = "Netscape";

    /**
     * United States English language identifier.
     */
    private static final String LANGUAGE_ENGLISH_US = "en-US";

    /**
     * United States.
     */
    private static final String TIMEZONE_NEW_YORK = "America/New_York";

    /**
     * The X86 CPU class.
     */
    private static final String CPU_CLASS_X86 = "x86";

    /**
     * The WIN32 platform.
     */
    private static final String PLATFORM_WIN32 = "Win32";

    /**
     * The WIN64 platform.
     */
    private static final String PLATFORM_WIN64 = "Win64";

    /** Latest Firefox. */
    public static final BrowserVersion FIREFOX = new BrowserVersion(85, "FF");

    /** Firefox 78 ESR. */
    public static final BrowserVersion FIREFOX_78 = new BrowserVersion(78, "FF78");

    /** Internet Explorer 11. */
    public static final BrowserVersion INTERNET_EXPLORER = new BrowserVersion(11, "IE");

    /** Latest Edge */
    public static final BrowserVersion EDGE = new BrowserVersion(88, "Edge");

    /** Latest Chrome. */
    public static final BrowserVersion CHROME = new BrowserVersion(88, "Chrome");

    /**
     * Array with all supported browsers
     */
    public static final BrowserVersion[] ALL_SUPPORTED_BROWSERS = new BrowserVersion[] {CHROME, EDGE, FIREFOX, FIREFOX_78, INTERNET_EXPLORER};

    /**
     * The best supported browser version at the moment.
     */
    public static final BrowserVersion BEST_SUPPORTED = CHROME;

    /** The default browser version. */
    private static BrowserVersion DefaultBrowserVersion_ = BEST_SUPPORTED;

    /** Register plugins for the browser versions. */
    static {
        // FF68
        FIREFOX_78.applicationVersion_ = "5.0 (Windows)";
        FIREFOX_78.userAgent_ = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:"
                                    + FIREFOX_78.getBrowserVersionNumeric() + ".0) Gecko/20100101 Firefox/"
                                    + FIREFOX_78.getBrowserVersionNumeric() + ".0";
        FIREFOX_78.buildId_ = "20181001000000";
        FIREFOX_78.productSub_ = "20100101";
        FIREFOX_78.headerNamesOrdered_ = new String[] {
            HttpHeader.HOST,
            HttpHeader.USER_AGENT,
            HttpHeader.ACCEPT,
            HttpHeader.ACCEPT_LANGUAGE,
            HttpHeader.ACCEPT_ENCODING,
            HttpHeader.CONNECTION,
            HttpHeader.REFERER,
            HttpHeader.COOKIE};
        FIREFOX_78.htmlAcceptHeader_ = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
        FIREFOX_78.acceptLanguageHeader_ = "en-US,en;q=0.5";
        FIREFOX_78.xmlHttpRequestAcceptHeader_ = "*/*";
        FIREFOX_78.imgAcceptHeader_ = "image/webp,*/*";
        FIREFOX_78.cssAcceptHeader_ = "text/css,*/*;q=0.1";
        FIREFOX_78.fontHeights_ = new int[] {
            0, 2, 3, 5, 6, 6, 7, 9, 10, 11, 12, 13, 15, 16, 16, 17, 18, 20, 21, 22, 23, 25, 26, 26, 28, 29,
            31, 32, 33, 34, 35, 37, 38, 38, 39, 41, 42, 43, 44, 45, 47, 48, 48, 49, 51, 52, 53, 54, 56, 58, 59, 59,
            60, 61, 63, 64, 65, 66, 68, 69, 69, 70, 71, 73, 74, 75, 76, 77, 79, 79,
            80, 82, 84, 85, 86, 87, 88, 90, 91, 91, 92, 94, 95, 96, 97, 98,
            100, 101, 101, 102, 103, 105, 106, 107, 108, 111, 112, 112, 113, 114, 116, 117, 118, 119,
            120, 122, 122, 123, 124, 126, 127, 128, 129, 130, 132, 132, 133, 134, 137, 138, 139,
            140, 141, 143, 143, 144, 145, 146, 148};

        // FF
        FIREFOX.applicationVersion_ = "5.0 (Windows)";
        FIREFOX.userAgent_ = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:"
                                            + FIREFOX.getBrowserVersionNumeric() + ".0) Gecko/20100101 Firefox/"
                                            + FIREFOX.getBrowserVersionNumeric() + ".0";
        FIREFOX.buildId_ = "20181001000000";
        FIREFOX.productSub_ = "20100101";
        FIREFOX.headerNamesOrdered_ = new String[] {
            HttpHeader.HOST,
            HttpHeader.USER_AGENT,
            HttpHeader.ACCEPT,
            HttpHeader.ACCEPT_LANGUAGE,
            HttpHeader.ACCEPT_ENCODING,
            HttpHeader.CONNECTION,
            HttpHeader.REFERER,
            HttpHeader.COOKIE};
        FIREFOX.htmlAcceptHeader_ = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
        FIREFOX.acceptLanguageHeader_ = "en-US,en;q=0.5";
        FIREFOX.xmlHttpRequestAcceptHeader_ = "*/*";
        FIREFOX.imgAcceptHeader_ = "image/webp,*/*";
        FIREFOX.cssAcceptHeader_ = "text/css,*/*;q=0.1";
        FIREFOX.fontHeights_ = new int[] {
            0, 2, 3, 5, 6, 6, 7, 9, 10, 11, 12, 13, 15, 16, 16, 17, 18, 20, 21, 22, 23, 25, 26, 26, 28, 29,
            31, 32, 33, 34, 35, 37, 38, 38, 39, 41, 42, 43, 44, 45, 47, 48, 48, 49, 51, 52, 53, 54, 56, 58, 59, 59,
            60, 61, 63, 64, 65, 66, 68, 69, 69, 70, 71, 73, 74, 75, 76, 77, 79, 79,
            80, 82, 84, 85, 86, 87, 88, 90, 91, 91, 92, 94, 95, 96, 97, 98,
            100, 101, 101, 102, 103, 105, 106, 107, 108, 111, 112, 112, 113, 114, 116, 117, 118, 119,
            120, 122, 122, 123, 124, 126, 127, 128, 129, 130, 132, 132, 133, 134, 137, 138, 139,
            140, 141, 143, 143, 144, 145, 146, 148};

        // IE
        INTERNET_EXPLORER.applicationVersion_ = "5.0 (Windows NT 10.0; WOW64; Trident/7.0; Zoom 3.6.0; rv:"
                                                    + INTERNET_EXPLORER.getBrowserVersionNumeric() + ".0) like Gecko";
        INTERNET_EXPLORER.userAgent_ = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; Zoom 3.6.0; rv:11.0) like Gecko";
        INTERNET_EXPLORER.headerNamesOrdered_ = new String[] {
            HttpHeader.ACCEPT,
            HttpHeader.REFERER,
            HttpHeader.ACCEPT_LANGUAGE,
            HttpHeader.USER_AGENT,
            HttpHeader.ACCEPT_ENCODING,
            HttpHeader.HOST,
            HttpHeader.DNT,
            HttpHeader.CONNECTION,
            HttpHeader.COOKIE};
        INTERNET_EXPLORER.htmlAcceptHeader_ = "text/html, application/xhtml+xml, image/jxr, */*";
        INTERNET_EXPLORER.acceptLanguageHeader_ = "en-US,en;q=0.9";
        INTERNET_EXPLORER.imgAcceptHeader_ = "image/png, image/svg+xml, image/jxr, image/*;q=0.8, */*;q=0.5";
        INTERNET_EXPLORER.cssAcceptHeader_ = "text/css, */*";
        INTERNET_EXPLORER.scriptAcceptHeader_ = "application/javascript, */*;q=0.8";
        INTERNET_EXPLORER.fontHeights_ = new int[] {
            0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 17, 18, 20, 21, 22, 23, 24, 25, 26, 28,
            29, 30, 31, 32, 33, 35, 36, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 49, 51, 52, 53, 54, 55, 56, 58,
            59, 60, 61, 62, 63, 64, 66, 67, 68, 69, 70, 71, 72, 74, 75, 76, 77, 78, 79, 80, 82, 83, 84, 85, 86, 87,
            89, 90, 91, 92, 93, 94, 95, 97, 98, 99, 100, 101, 102, 103, 105, 106, 107, 108, 109, 110, 112, 113, 114,
            115, 116, 117, 118, 120, 121, 122, 123, 124, 125, 126, 128, 129, 130, 131, 132, 133, 135, 136, 137, 138,
            139, 140, 141, 143, 144, 145, 146, 147};

        // CHROME (Win10 64bit)
        CHROME.applicationVersion_ = "5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
                                        + CHROME.getBrowserVersionNumeric() + ".0.4324.96 Safari/537.36";
        CHROME.userAgent_ = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
                                        + CHROME.getBrowserVersionNumeric() + ".0.4324.96 Safari/537.36";

        CHROME.applicationCodeName_ = "Mozilla";
        CHROME.vendor_ = "Google Inc.";
        CHROME.cpuClass_ = null;
        CHROME.productSub_ = "20030107";
        CHROME.headerNamesOrdered_ = new String[] {
            HttpHeader.HOST,
            HttpHeader.CONNECTION,
            "Upgrade-Insecure-Requests",
            HttpHeader.USER_AGENT,
            HttpHeader.ACCEPT,
            HttpHeader.SEC_FETCH_SITE,
            HttpHeader.SEC_FETCH_MODE,
            HttpHeader.SEC_FETCH_USER,
            HttpHeader.SEC_FETCH_DEST,
            HttpHeader.REFERER,
            HttpHeader.ACCEPT_ENCODING,
            HttpHeader.ACCEPT_LANGUAGE,
            HttpHeader.COOKIE};
        CHROME.acceptEncodingHeader_ = "gzip, deflate, br";
        CHROME.acceptLanguageHeader_ = "en-US,en;q=0.9";
        CHROME.htmlAcceptHeader_ = "text/html,application/xhtml+xml,application/xml;"
                                            + "q=0.9,image/avif,image/webp,image/apng,*/*;"
                                            + "q=0.8,application/signed-exchange;v=b3;q=0.9";
        CHROME.imgAcceptHeader_ = "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8";
        CHROME.cssAcceptHeader_ = "text/css,*/*;q=0.1";
        CHROME.scriptAcceptHeader_ = "*/*";
        // there are other issues with Chrome; a different productSub, etc.
        CHROME.fontHeights_ = new int[] {
            0, 1, 2, 4, 5, 5, 6, 8, 9, 10, 11, 12, 15, 16, 16, 17, 18, 20, 21, 22, 23, 25, 26, 26,
            27, 28, 30, 31, 32, 33, 34, 36, 37, 37, 38, 40, 42, 43, 44, 45, 47, 48, 48, 49, 51, 52, 53, 54, 55, 57,
            58, 58, 59, 60, 62, 63, 64, 65, 67, 69, 69, 70, 71, 73, 74, 75, 76, 77, 79, 79, 80, 81, 83, 84, 85, 86,
            87, 89, 90, 90, 91, 93, 94, 96, 97, 98, 100, 101, 101, 102, 103, 105, 106, 107, 108, 110, 111, 111, 112,
            113, 115, 116, 117, 118, 119, 121, 122, 123, 124, 126, 127, 128, 129, 130, 132, 132, 133, 134, 136, 137,
            138, 139, 140, 142, 142, 143, 144, 145, 147};

        // EDGE (Win10 64bit)
        EDGE.applicationVersion_ = "5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
                                        + EDGE.getBrowserVersionNumeric() + ".0.4324.96 Safari/537.36 Edg/"
                                        + EDGE.getBrowserVersionNumeric() + ".0.705.50";
        EDGE.userAgent_ = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
                                        + EDGE.getBrowserVersionNumeric() + ".0.4324.96 Safari/537.36 Edg/"
                                        + EDGE.getBrowserVersionNumeric() + ".0.705.50";

        EDGE.applicationCodeName_ = "Mozilla";
        EDGE.vendor_ = "Google Inc.";
        EDGE.cpuClass_ = null;
        EDGE.productSub_ = "20030107";
        EDGE.headerNamesOrdered_ = new String[] {
            HttpHeader.HOST,
            HttpHeader.CONNECTION,
            "Upgrade-Insecure-Requests",
            HttpHeader.USER_AGENT,
            HttpHeader.ACCEPT,
            HttpHeader.SEC_FETCH_SITE,
            HttpHeader.SEC_FETCH_MODE,
            HttpHeader.SEC_FETCH_USER,
            HttpHeader.SEC_FETCH_DEST,
            HttpHeader.REFERER,
            HttpHeader.ACCEPT_ENCODING,
            HttpHeader.ACCEPT_LANGUAGE,
            HttpHeader.COOKIE};
        EDGE.acceptEncodingHeader_ = "gzip, deflate, br";
        EDGE.acceptLanguageHeader_ = "en-US,en;q=0.9";
        EDGE.htmlAcceptHeader_ = "text/html,application/xhtml+xml,application/xml;"
                                            + "q=0.9,image/webp,image/apng,*/*;"
                                            + "q=0.8,application/signed-exchange;v=b3;q=0.9";
        EDGE.imgAcceptHeader_ = "image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8";
        EDGE.cssAcceptHeader_ = "text/css,*/*;q=0.1";
        EDGE.scriptAcceptHeader_ = "*/*";
        // there are other issues with Chrome; a different productSub, etc.
        EDGE.fontHeights_ = new int[] {
            0, 1, 2, 4, 5, 5, 6, 8, 9, 10, 11, 12, 15, 16, 16, 17, 18, 20, 21, 22, 23, 25, 26, 26,
            27, 28, 30, 31, 32, 33, 34, 36, 37, 37, 38, 40, 42, 43, 44, 45, 47, 48, 48, 49, 51, 52, 53, 54, 55, 57,
            58, 58, 59, 60, 62, 63, 64, 65, 67, 69, 69, 70, 71, 73, 74, 75, 76, 77, 79, 79, 80, 81, 83, 84, 85, 86,
            87, 89, 90, 90, 91, 93, 94, 96, 97, 98, 100, 101, 101, 102, 103, 105, 106, 107, 108, 110, 111, 111, 112,
            113, 115, 116, 117, 118, 119, 121, 122, 123, 124, 126, 127, 128, 129, 130, 132, 132, 133, 134, 136, 137,
            138, 139, 140, 142, 142, 143, 144, 145, 147};

        // default file upload mime types
        CHROME.registerUploadMimeType("html", MimeType.TEXT_HTML);
        CHROME.registerUploadMimeType("htm", MimeType.TEXT_HTML);
        CHROME.registerUploadMimeType("css", MimeType.TEXT_CSS);
        CHROME.registerUploadMimeType("xml", MimeType.TEXT_XML);
        CHROME.registerUploadMimeType("gif", "image/gif");
        CHROME.registerUploadMimeType("jpeg", "image/jpeg");
        CHROME.registerUploadMimeType("jpg", "image/jpeg");
        CHROME.registerUploadMimeType("png", "image/png");
        CHROME.registerUploadMimeType("webp", "image/webp");
        CHROME.registerUploadMimeType("mp4", "video/mp4");
        CHROME.registerUploadMimeType("m4v", "video/mp4");
        CHROME.registerUploadMimeType("m4a", "audio/x-m4a");
        CHROME.registerUploadMimeType("mp3", "audio/mpeg");
        CHROME.registerUploadMimeType("ogv", "video/ogg");
        CHROME.registerUploadMimeType("ogm", "video/ogg");
        CHROME.registerUploadMimeType("ogg", "audio/ogg");
        CHROME.registerUploadMimeType("oga", "audio/ogg");
        CHROME.registerUploadMimeType("opus", "audio/ogg");
        CHROME.registerUploadMimeType("webm", "video/webm");
        CHROME.registerUploadMimeType("wav", "audio/wav");
        CHROME.registerUploadMimeType("flac", "audio/flac");
        CHROME.registerUploadMimeType("xhtml", "application/xhtml+xml");
        CHROME.registerUploadMimeType("xht", "application/xhtml+xml");
        CHROME.registerUploadMimeType("xhtm", "application/xhtml+xml");
        CHROME.registerUploadMimeType("txt", MimeType.TEXT_PLAIN);
        CHROME.registerUploadMimeType("text", MimeType.TEXT_PLAIN);

        EDGE.registerUploadMimeType("html", MimeType.TEXT_HTML);
        EDGE.registerUploadMimeType("htm", MimeType.TEXT_HTML);
        EDGE.registerUploadMimeType("css", MimeType.TEXT_CSS);
        EDGE.registerUploadMimeType("xml", MimeType.TEXT_XML);
        EDGE.registerUploadMimeType("gif", "image/gif");
        EDGE.registerUploadMimeType("jpeg", "image/jpeg");
        EDGE.registerUploadMimeType("jpg", "image/jpeg");
        EDGE.registerUploadMimeType("png", "image/png");
        EDGE.registerUploadMimeType("webp", "image/webp");
        EDGE.registerUploadMimeType("mp4", "video/mp4");
        EDGE.registerUploadMimeType("m4v", "video/mp4");
        EDGE.registerUploadMimeType("m4a", "audio/x-m4a");
        EDGE.registerUploadMimeType("mp3", "audio/mpeg");
        EDGE.registerUploadMimeType("ogv", "video/ogg");
        EDGE.registerUploadMimeType("ogm", "video/ogg");
        EDGE.registerUploadMimeType("ogg", "audio/ogg");
        EDGE.registerUploadMimeType("oga", "audio/ogg");
        EDGE.registerUploadMimeType("opus", "audio/ogg");
        EDGE.registerUploadMimeType("webm", "video/webm");
        EDGE.registerUploadMimeType("wav", "audio/wav");
        EDGE.registerUploadMimeType("flac", "audio/flac");
        EDGE.registerUploadMimeType("xhtml", "application/xhtml+xml");
        EDGE.registerUploadMimeType("xht", "application/xhtml+xml");
        EDGE.registerUploadMimeType("xhtm", "application/xhtml+xml");
        EDGE.registerUploadMimeType("txt", MimeType.TEXT_PLAIN);
        EDGE.registerUploadMimeType("text", MimeType.TEXT_PLAIN);

        FIREFOX_78.registerUploadMimeType("html", MimeType.TEXT_HTML);
        FIREFOX_78.registerUploadMimeType("htm", MimeType.TEXT_HTML);
        FIREFOX_78.registerUploadMimeType("css", MimeType.TEXT_CSS);
        FIREFOX_78.registerUploadMimeType("xml", MimeType.TEXT_XML);
        FIREFOX_78.registerUploadMimeType("gif", "image/gif");
        FIREFOX_78.registerUploadMimeType("jpeg", "image/jpeg");
        FIREFOX_78.registerUploadMimeType("jpg", "image/jpeg");
        FIREFOX_78.registerUploadMimeType("mp4", "video/mp4");
        FIREFOX_78.registerUploadMimeType("m4v", "video/mp4");
        FIREFOX_78.registerUploadMimeType("m4a", "audio/mp4");
        FIREFOX_78.registerUploadMimeType("png", "image/png");
        FIREFOX_78.registerUploadMimeType("mp3", "audio/mpeg");
        FIREFOX_78.registerUploadMimeType("ogv", "video/ogg");
        FIREFOX_78.registerUploadMimeType("ogm", "video/ogg");
        FIREFOX_78.registerUploadMimeType("ogg", "video/ogg");
        FIREFOX_78.registerUploadMimeType("oga", "audio/ogg");
        FIREFOX_78.registerUploadMimeType("opus", "audio/ogg");
        FIREFOX_78.registerUploadMimeType("webm", "video/webm");
        FIREFOX_78.registerUploadMimeType("webp", "image/webp");
        FIREFOX_78.registerUploadMimeType("wav", "audio/wav");
        FIREFOX_78.registerUploadMimeType("flac", "audio/x-flac");
        FIREFOX_78.registerUploadMimeType("xhtml", "application/xhtml+xml");
        FIREFOX_78.registerUploadMimeType("xht", "application/xhtml+xml");
        FIREFOX_78.registerUploadMimeType("txt", MimeType.TEXT_PLAIN);
        FIREFOX_78.registerUploadMimeType("text", MimeType.TEXT_PLAIN);

        FIREFOX.registerUploadMimeType("html", MimeType.TEXT_HTML);
        FIREFOX.registerUploadMimeType("htm", MimeType.TEXT_HTML);
        FIREFOX.registerUploadMimeType("css", MimeType.TEXT_CSS);
        FIREFOX.registerUploadMimeType("xml", MimeType.TEXT_XML);
        FIREFOX.registerUploadMimeType("gif", "image/gif");
        FIREFOX.registerUploadMimeType("jpeg", "image/jpeg");
        FIREFOX.registerUploadMimeType("jpg", "image/jpeg");
        FIREFOX.registerUploadMimeType("mp4", "video/mp4");
        FIREFOX.registerUploadMimeType("m4v", "video/mp4");
        FIREFOX.registerUploadMimeType("m4a", "audio/mp4");
        FIREFOX.registerUploadMimeType("png", "image/png");
        FIREFOX.registerUploadMimeType("mp3", "audio/mpeg");
        FIREFOX.registerUploadMimeType("ogv", "video/ogg");
        FIREFOX.registerUploadMimeType("ogm", "video/ogg");
        FIREFOX.registerUploadMimeType("ogg", "video/ogg");
        FIREFOX.registerUploadMimeType("oga", "audio/ogg");
        FIREFOX.registerUploadMimeType("opus", "audio/ogg");
        FIREFOX.registerUploadMimeType("webm", "video/webm");
        FIREFOX.registerUploadMimeType("webp", "image/webp");
        FIREFOX.registerUploadMimeType("wav", "audio/wav");
        FIREFOX.registerUploadMimeType("flac", "audio/x-flac");
        FIREFOX.registerUploadMimeType("xhtml", "application/xhtml+xml");
        FIREFOX.registerUploadMimeType("xht", "application/xhtml+xml");
        FIREFOX.registerUploadMimeType("txt", MimeType.TEXT_PLAIN);
        FIREFOX.registerUploadMimeType("text", MimeType.TEXT_PLAIN);

        INTERNET_EXPLORER.registerUploadMimeType("html", MimeType.TEXT_HTML);
        INTERNET_EXPLORER.registerUploadMimeType("htm", MimeType.TEXT_HTML);
        INTERNET_EXPLORER.registerUploadMimeType("css", MimeType.TEXT_CSS);
        INTERNET_EXPLORER.registerUploadMimeType("xml", MimeType.TEXT_XML);
        INTERNET_EXPLORER.registerUploadMimeType("gif", "image/gif");
        INTERNET_EXPLORER.registerUploadMimeType("jpeg", "image/jpeg");
        INTERNET_EXPLORER.registerUploadMimeType("jpg", "image/jpeg");
        INTERNET_EXPLORER.registerUploadMimeType("png", "image/png");
        INTERNET_EXPLORER.registerUploadMimeType("mp4", "video/mp4");
        INTERNET_EXPLORER.registerUploadMimeType("m4v", "video/mp4");
        INTERNET_EXPLORER.registerUploadMimeType("m4a", "audio/mp4");
        INTERNET_EXPLORER.registerUploadMimeType("mp3", "audio/mpeg");
        INTERNET_EXPLORER.registerUploadMimeType("ogv", "video/ogg");
        INTERNET_EXPLORER.registerUploadMimeType("ogm", "video/ogg");
        INTERNET_EXPLORER.registerUploadMimeType("ogg", "audio/ogg");
        INTERNET_EXPLORER.registerUploadMimeType("oga", "audio/ogg");
        INTERNET_EXPLORER.registerUploadMimeType("opus", "audio/ogg");
        INTERNET_EXPLORER.registerUploadMimeType("webm", "video/webm");
        INTERNET_EXPLORER.registerUploadMimeType("wav", "audio/wav");
        INTERNET_EXPLORER.registerUploadMimeType("flac", "audio/x-flac");
        INTERNET_EXPLORER.registerUploadMimeType("xhtml", "application/xhtml+xml");
        INTERNET_EXPLORER.registerUploadMimeType("xht", "application/xhtml+xml");
        INTERNET_EXPLORER.registerUploadMimeType("txt", MimeType.TEXT_PLAIN);

        // flush plugin (windows version)
        final PluginConfiguration flash = new PluginConfiguration("Shockwave Flash",
                "Shockwave Flash 32.0 r0", "32.0.0.387", "Flash.ocx"); //NOPMD
        flash.getMimeTypes().add(new PluginConfiguration.MimeType("application/x-shockwave-flash",
                "Shockwave Flash", "swf"));
        INTERNET_EXPLORER.plugins_.add(flash);
    }

    private final int browserVersionNumeric_;
    private final String nickname_;

    private String applicationCodeName_ = "Mozilla";
    private String applicationMinorVersion_ = "0";
    private String applicationName_;
    private String applicationVersion_;
    private String buildId_;
    private String productSub_;
    private String vendor_ = "";
    private String browserLanguage_ = LANGUAGE_ENGLISH_US;
    private String cpuClass_ = CPU_CLASS_X86;
    private boolean onLine_ = true;
    private String platform_ = PLATFORM_WIN32;
    private String systemLanguage_ = LANGUAGE_ENGLISH_US;
    private TimeZone systemTimezone_ = TimeZone.getTimeZone(TIMEZONE_NEW_YORK);
    private String userAgent_;
    private String userLanguage_ = LANGUAGE_ENGLISH_US;
    private final Set<PluginConfiguration> plugins_;
    private final Set<BrowserVersionFeatures> features_;
    private String acceptEncodingHeader_;
    private String acceptLanguageHeader_;
    private String htmlAcceptHeader_;
    private String imgAcceptHeader_;
    private String cssAcceptHeader_;
    private String scriptAcceptHeader_;
    private String xmlHttpRequestAcceptHeader_;
    private String[] headerNamesOrdered_;
    private int[] fontHeights_;
    private final Map<String, String> uploadMimeTypes_;

    /**
     * Creates a new browser version instance.
     *
     * @param browserVersionNumeric the floating number version of the browser
     * @param nickname the short name of the browser (like "FF78", "IE", ...) - has to be unique
     */
    BrowserVersion(final int browserVersionNumeric, final String nickname) {
        browserVersionNumeric_ = browserVersionNumeric;
        nickname_ = nickname;

        applicationName_ = NETSCAPE;
        acceptEncodingHeader_ = "gzip, deflate";
        htmlAcceptHeader_ = "*/*";
        imgAcceptHeader_ = "*/*";
        cssAcceptHeader_ = "*/*";
        scriptAcceptHeader_ = "*/*";
        xmlHttpRequestAcceptHeader_ = "*/*";

        plugins_ = new HashSet<>();
        features_ = EnumSet.noneOf(BrowserVersionFeatures.class);
        uploadMimeTypes_ = new HashMap<>();

        initFeatures();
    }

    private void initFeatures() {
        final SupportedBrowser expectedBrowser;
        if (isChrome()) {
            expectedBrowser = SupportedBrowser.CHROME;
        }
        else if (isEdge()) {
            expectedBrowser = SupportedBrowser.EDGE;
        }
        else if (isFirefox78()) {
            expectedBrowser = SupportedBrowser.FF78;
        }
        else if (isFirefox()) {
            expectedBrowser = SupportedBrowser.FF;
        }
        else {
            expectedBrowser = SupportedBrowser.IE;
        }

        for (final BrowserVersionFeatures features : BrowserVersionFeatures.values()) {
            try {
                final Field field = BrowserVersionFeatures.class.getField(features.name());
                final BrowserFeature browserFeature = field.getAnnotation(BrowserFeature.class);
                if (browserFeature != null) {
                    for (final SupportedBrowser browser : browserFeature.value()) {
                        if (AbstractJavaScriptConfiguration.isCompatible(expectedBrowser, browser)) {
                            features_.add(features);
                        }
                    }
                }
            }
            catch (final NoSuchFieldException e) {
                // should never happen
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * Returns the default browser version that is used whenever a specific version isn't specified.
     * Defaults to {@link #BEST_SUPPORTED}.
     * @return the default browser version
     */
    public static BrowserVersion getDefault() {
        return DefaultBrowserVersion_;
    }

    /**
     * Sets the default browser version that is used whenever a specific version isn't specified.
     * @param newBrowserVersion the new default browser version
     */
    public static void setDefault(final BrowserVersion newBrowserVersion) {
        WebAssert.notNull("newBrowserVersion", newBrowserVersion);
        DefaultBrowserVersion_ = newBrowserVersion;
    }

    /**
     * Returns {@code true} if this <tt>BrowserVersion</tt> instance represents some
     * version of Internet Explorer.
     * @return whether or not this version is a version of IE
     */
    public boolean isIE() {
        return getNickname().startsWith("IE");
    }

    /**
     * Returns {@code true} if this <tt>BrowserVersion</tt> instance represents some
     * version of Google Chrome. Note that Google Chrome does not return 'Chrome'
     * in the application name, we have to look in the nickname.
     * @return whether or not this version is a version of a Chrome browser
     */
    public boolean isChrome() {
        return getNickname().startsWith("Chrome");
    }

    /**
     * Returns {@code true} if this <tt>BrowserVersion</tt> instance represents some
     * version of Microsoft Edge.
     * @return whether or not this version is a version of a Chrome browser
     */
    public boolean isEdge() {
        return getNickname().startsWith("Edge");
    }

    /**
     * Returns {@code true} if this <tt>BrowserVersion</tt> instance represents some
     * version of Firefox.
     * @return whether or not this version is a version of a Firefox browser
     */
    public boolean isFirefox() {
        return getNickname().startsWith("FF");
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * @return whether or not this version version 60 of a Firefox browser
     */
    public boolean isFirefox78() {
        return isFirefox() && getBrowserVersionNumeric() == 78;
    }

    /**
     * Returns the short name of the browser like {@code FF}, {@code IE}, etc.
     *
     * @return the short name (if any)
     */
    public String getNickname() {
        return nickname_;
    }

    /**
     * @return the browserVersionNumeric
     */
    public int getBrowserVersionNumeric() {
        return browserVersionNumeric_;
    }

    /**
     * Returns the application code name, for example "Mozilla".
     * Default value is "Mozilla" if not explicitly configured.
     * @return the application code name
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533077.aspx">MSDN documentation</a>
     */
    public String getApplicationCodeName() {
        return applicationCodeName_;
    }

    /**
     * Returns the application minor version, for example "0".
     * Default value is "0" if not explicitly configured.
     * @return the application minor version
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533078.aspx">MSDN documentation</a>
     */
    public String getApplicationMinorVersion() {
        return applicationMinorVersion_;
    }

    /**
     * Returns the application name, for example "Microsoft Internet Explorer".
     * @return the application name
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533079.aspx">MSDN documentation</a>
     */
    public String getApplicationName() {
        return applicationName_;
    }

    /**
     * Returns the application version, for example "4.0 (compatible; MSIE 6.0b; Windows 98)".
     * @return the application version
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533080.aspx">MSDN documentation</a>
     */
    public String getApplicationVersion() {
        return applicationVersion_;
    }

    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor_;
    }

    /**
     * Returns the browser application language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured.
     * @return the browser application language
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533542.aspx">MSDN documentation</a>
     */
    public String getBrowserLanguage() {
        return browserLanguage_;
    }

    /**
     * Returns the type of CPU in the machine, for example "x86".
     * Default value is {@link #CPU_CLASS_X86} if not explicitly configured.
     * @return the type of CPU in the machine
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533697.aspx">MSDN documentation</a>
     */
    public String getCpuClass() {
        return cpuClass_;
    }

    /**
     * Returns {@code true} if the browser is currently online.
     * Default value is {@code true} if not explicitly configured.
     * @return {@code true} if the browser is currently online
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534307.aspx">MSDN documentation</a>
     */
    public boolean isOnLine() {
        return onLine_;
    }

    /**
     * Returns the platform on which the application is running, for example "Win32".
     * Default value is {@link #PLATFORM_WIN32} if not explicitly configured.
     * @return the platform on which the application is running
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534340.aspx">MSDN documentation</a>
     */
    public String getPlatform() {
        return platform_;
    }

    /**
     * Returns the system language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured.
     * @return the system language
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534653.aspx">MSDN documentation</a>
     */
    public String getSystemLanguage() {
        return systemLanguage_;
    }

    /**
     * Returns the system {@link TimeZone}.
     * Default value is {@code America/New_York} if not explicitly configured.
     * @return the system {@link TimeZone}
     */
    public TimeZone getSystemTimezone() {
        return systemTimezone_;
    }

    /**
     * Returns the user agent string, for example "Mozilla/4.0 (compatible; MSIE 6.0b; Windows 98)".
     * @return the user agent string
     */
    public String getUserAgent() {
        return userAgent_;
    }

    /**
     * Returns the user language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured.
     * @return the user language
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534713.aspx">MSDN documentation</a>
     */
    public String getUserLanguage() {
        return userLanguage_;
    }

    /**
     * Returns the value used by the browser for the {@code Accept_Encoding} header.
     * @return the accept encoding header string
     */
    public String getAcceptEncodingHeader() {
        return acceptEncodingHeader_;
    }

    /**
     * Returns the value used by the browser for the {@code Accept_Language} header.
     * @return the accept language header string
     */
    public String getAcceptLanguageHeader() {
        return acceptLanguageHeader_;
    }

    /**
     * Returns the value used by the browser for the {@code Accept} header if requesting a page.
     * @return the accept header string
     */
    public String getHtmlAcceptHeader() {
        return htmlAcceptHeader_;
    }

    /**
     * Returns the value used by the browser for the {@code Accept} header
     * if requesting a script.
     * @return the accept header string
     */
    public String getScriptAcceptHeader() {
        return scriptAcceptHeader_;
    }

    /**
     * Returns the value used by the browser for the {@code Accept} header
     * if performing an XMLHttpRequest.
     * @return the accept header string
     */
    public String getXmlHttpRequestAcceptHeader() {
        return xmlHttpRequestAcceptHeader_;
    }

    /**
     * Returns the value used by the browser for the {@code Accept} header
     * if requesting an image.
     * @return the accept header string
     */
    public String getImgAcceptHeader() {
        return imgAcceptHeader_;
    }

    /**
     * Returns the value used by the browser for the {@code Accept} header
     * if requesting a CSS declaration.
     * @return the accept header string
     */
    public String getCssAcceptHeader() {
        return cssAcceptHeader_;
    }

    /**
     * Returns the available plugins. This makes only sense for Firefox as only this
     * browser makes this kind of information available via JavaScript.
     * @return the available plugins
     */
    public Set<PluginConfiguration> getPlugins() {
        return plugins_;
    }

    /**
     * Indicates if this instance has the given feature. Used for HtmlUnit internal processing.
     * @param property the property name
     * @return {@code false} if this browser doesn't have this feature
     */
    public boolean hasFeature(final BrowserVersionFeatures property) {
        return features_.contains(property);
    }

    /**
     * Returns the buildId.
     * @return the buildId
     */
    public String getBuildId() {
        return buildId_;
    }

    /**
     * Returns the productSub.
     * @return the buildId
     */
    public String getProductSub() {
        return productSub_;
    }

    /**
     * Gets the headers names, so they are sent in the given order (if included in the request).
     * @return headerNames the header names in ordered manner
     */
    public String[] getHeaderNamesOrdered() {
        return headerNamesOrdered_;
    }

    /**
     * Registers a new mime type for the provided file extension.
     * @param fileExtension the file extension used to determine the mime type
     * @param mimeType the mime type to be used when uploading files with this extension
     */
    public void registerUploadMimeType(final String fileExtension, final String mimeType) {
        if (fileExtension != null) {
            uploadMimeTypes_.put(fileExtension.toLowerCase(Locale.ROOT), mimeType);
        }
    }

    /**
     * Determines the content type for the given file.
     * @param file the file
     * @return a content type or an empty string if unknown
     */
    public String getUploadMimeType(final File file) {
        if (file == null) {
            return "";
        }

        final String fileExtension = FilenameUtils.getExtension(file.getName());

        final String mimeType = uploadMimeTypes_.get(fileExtension.toLowerCase(Locale.ROOT));
        if (mimeType != null) {
            return mimeType;
        }
        return "";
    }

    /**
     * Returns the corresponding height of the specified {@code fontSize}
     * @param fontSize the font size
     * @return the corresponding height
     */
    public int getFontHeight(final String fontSize) {
        if (fontHeights_ == null) {
            return 18;
        }
        final int fontSizeInt = Integer.parseInt(fontSize.substring(0, fontSize.length() - 2));
        if (fontSizeInt < fontHeights_.length) {
            return fontHeights_[fontSizeInt];
        }
        return (int) (fontSizeInt * 1.2);
    }

    /**
     * @return the pixesPerChar based on the specified {@code fontSize}
     */
    public int getPixesPerChar() {
        return 10;
    }

    @Override
    public String toString() {
        return nickname_;
    }

    /**
     * Because BrowserVersion is immutable we need a builder
     * for this complex object setup.
     */
    public static class BrowserVersionBuilder {
        private final BrowserVersion workPiece_;

        /**
         * Creates a new BrowserVersionBuilder using the given browser version
         * as template for the browser to be constructed.
         * @param version the blueprint
         */
        public BrowserVersionBuilder(final BrowserVersion version) {
            workPiece_ = new BrowserVersion(version.getBrowserVersionNumeric(), version.getNickname());

            setApplicationVersion(version.getApplicationVersion())
                .setUserAgent(version.getUserAgent())
                .setApplicationName(version.getApplicationName())
                .setApplicationCodeName(version.getApplicationCodeName())
                .setApplicationMinorVersion(version.getApplicationMinorVersion())
                .setVendor(version.getVendor())
                .setBrowserLanguage(version.getBrowserLanguage())
                .setCpuClass(version.getCpuClass())
                .setOnLine(version.isOnLine())
                .setPlatform(version.getPlatform())
                .setSystemLanguage(version.getSystemLanguage())
                .setSystemTimezone(version.getSystemTimezone())
                .setUserLanguage(version.getUserLanguage())
                .setBuildId(version.getBuildId())
                .setProductSub(version.getProductSub())
                .setAcceptEncodingHeader(version.getAcceptEncodingHeader())
                .setAcceptLanguageHeader(version.getAcceptLanguageHeader())
                .setHtmlAcceptHeader(version.getHtmlAcceptHeader())
                .setImgAcceptHeader(version.getImgAcceptHeader())
                .setCssAcceptHeader(version.getCssAcceptHeader())
                .setScriptAcceptHeader(version.getScriptAcceptHeader())
                .setXmlHttpRequestAcceptHeader(version.getXmlHttpRequestAcceptHeader())
                .setHeaderNamesOrdered(version.getHeaderNamesOrdered())
                .setFontHeights(version.fontHeights_);

            for (final PluginConfiguration pluginConf : version.getPlugins()) {
                workPiece_.plugins_.add(pluginConf.clone());
            }

            workPiece_.features_.addAll(version.features_);
            workPiece_.uploadMimeTypes_.putAll(version.uploadMimeTypes_);
        }

        /**
         * @return the new immutable browser version
         */
        public BrowserVersion build() {
            return workPiece_;
        }

        /**
         * @param applicationMinorVersion the applicationMinorVersion to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setApplicationMinorVersion(final String applicationMinorVersion) {
            workPiece_.applicationMinorVersion_ = applicationMinorVersion;
            return this;
        }

        /**
         * @param applicationName the applicationName to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setApplicationName(final String applicationName) {
            workPiece_.applicationName_ = applicationName;
            return this;
        }

        /**
         * @param applicationVersion the applicationVersion to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setApplicationVersion(final String applicationVersion) {
            workPiece_.applicationVersion_ = applicationVersion;
            return this;
        }

        /**
         * @param vendor the vendor to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setVendor(final String vendor) {
            workPiece_.vendor_ = vendor;
            return this;
        }

        /**
         * @param applicationCodeName the applicationCodeName to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setApplicationCodeName(final String applicationCodeName) {
            workPiece_.applicationCodeName_ = applicationCodeName;
            return this;
        }

        /**
         * @param browserLanguage the browserLanguage to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setBrowserLanguage(final String browserLanguage) {
            workPiece_.browserLanguage_ = browserLanguage;
            return this;
        }

        /**
         * @param cpuClass the cpuClass to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setCpuClass(final String cpuClass) {
            workPiece_.cpuClass_ = cpuClass;
            return this;
        }

        /**
         * @param onLine the onLine to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setOnLine(final boolean onLine) {
            workPiece_.onLine_ = onLine;
            return this;
        }

        /**
         * @param platform the platform to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setPlatform(final String platform) {
            workPiece_.platform_ = platform;
            return this;
        }

        /**
         * @param systemLanguage the systemLanguage to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setSystemLanguage(final String systemLanguage) {
            workPiece_.systemLanguage_ = systemLanguage;
            return this;
        }

        /**
         * @param systemTimezone the systemTimezone to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setSystemTimezone(final TimeZone systemTimezone) {
            workPiece_.systemTimezone_ = systemTimezone;
            return this;
        }

        /**
         * @param userAgent the userAgent to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setUserAgent(final String userAgent) {
            workPiece_.userAgent_ = userAgent;
            return this;
        }

        /**
         * @param userLanguage the userLanguage to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setUserLanguage(final String userLanguage) {
            workPiece_.userLanguage_ = userLanguage;
            return this;
        }

        /**
         * @param acceptEncodingHeader the {@code Accept-Encoding} header
         * @return this for fluent use
         */
        public BrowserVersionBuilder setAcceptEncodingHeader(final String acceptEncodingHeader) {
            workPiece_.acceptEncodingHeader_ = acceptEncodingHeader;
            return this;
        }

        /**
         * @param acceptLanguageHeader the {@code Accept-Language} header
         * @return this for fluent use
         */
        public BrowserVersionBuilder setAcceptLanguageHeader(final String acceptLanguageHeader) {
            workPiece_.acceptLanguageHeader_ = acceptLanguageHeader;
            return this;
        }

        /**
         * @param htmlAcceptHeader the {@code Accept} header to be used when retrieving pages
         * @return this for fluent use
         */
        public BrowserVersionBuilder setHtmlAcceptHeader(final String htmlAcceptHeader) {
            workPiece_.htmlAcceptHeader_ = htmlAcceptHeader;
            return this;
        }

        /**
         * @param imgAcceptHeader the {@code Accept} header to be used when retrieving images
         * @return this for fluent use
         */
        public BrowserVersionBuilder setImgAcceptHeader(final String imgAcceptHeader) {
            workPiece_.imgAcceptHeader_ = imgAcceptHeader;
            return this;
        }

        /**
         * @param cssAcceptHeader the {@code Accept} header to be used when retrieving pages
         * @return this for fluent use
         */
        public BrowserVersionBuilder setCssAcceptHeader(final String cssAcceptHeader) {
            workPiece_.cssAcceptHeader_ = cssAcceptHeader;
            return this;
        }

        /**
         * @param scriptAcceptHeader the {@code Accept} header to be used when retrieving scripts
         * @return this for fluent use
         */
        public BrowserVersionBuilder setScriptAcceptHeader(final String scriptAcceptHeader) {
            workPiece_.scriptAcceptHeader_ = scriptAcceptHeader;
            return this;
        }

        /**
         * @param xmlHttpRequestAcceptHeader the {@code Accept} header to be used when
         * performing XMLHttpRequests
         * @return this for fluent use
         */
        public BrowserVersionBuilder setXmlHttpRequestAcceptHeader(final String xmlHttpRequestAcceptHeader) {
            workPiece_.xmlHttpRequestAcceptHeader_ = xmlHttpRequestAcceptHeader;
            return this;
        }

        /**
         * @param productSub the productSub
         * @return this for fluent use
         */
        BrowserVersionBuilder setProductSub(final String productSub) {
            workPiece_.productSub_ = productSub;
            return this;
        }

        /**
         * @param headerNamesOrdered the headerNamesOrdered
         * @return this for fluent use
         */
        BrowserVersionBuilder setHeaderNamesOrdered(final String[] headerNamesOrdered) {
            workPiece_.headerNamesOrdered_ = headerNamesOrdered;
            return this;
        }

        /**
         * @param headerNamesOrdered the headerNamesOrdered
         * @return this for fluent use
         */
        BrowserVersionBuilder setFontHeights(final int[] fontHeights) {
            workPiece_.fontHeights_ = fontHeights;
            return this;
        }

        /**
         * @param buildId the buildId
         * @return this for fluent use
         */
        BrowserVersionBuilder setBuildId(final String buildId) {
            workPiece_.buildId_ = buildId;
            return this;
        }
    }
}
