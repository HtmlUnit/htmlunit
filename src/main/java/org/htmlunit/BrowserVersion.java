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

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.htmlunit.css.CssPixelValueConverter;
import org.htmlunit.javascript.configuration.BrowserFeature;
import org.htmlunit.javascript.configuration.SupportedBrowser;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.StringUtils;

/**
 * Objects of this class represent one specific version of a given browser. Predefined
 * constants are provided for common browser versions.
 *
 * <p>You can create a different browser setup by using the BrowserVersionFactory.
 * </p>
 * <pre>
 *         final String applicationName = "APPNAME";
 *         final String applicationVersion = "APPVERSION";
 *         final String userAgent = "USERAGENT";
 * </pre>
 * <pre>
 *         final BrowserVersion browser =
 *                 new BrowserVersion.BrowserVersionBuilder(BrowserVersion.FIREFOX)
 *                     .setApplicationName(applicationName)
 *                     .setApplicationVersion(applicationVersion)
 *                     .setUserAgent(userAgent)
 *                     .build();
 * </pre>
 * <p>But keep in mind this new one still behaves like an FF, only the stuff reported to the
 * outside is changed. This is more or less the same you can do with real browsers installing
 * plugins like UserAgentSwitcher.</p>
 *
 * @author Mike Bowler
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.TooManyFields"})
public final class BrowserVersion implements Serializable {

    /** Latest Firefox. */
    public static final BrowserVersion FIREFOX = new BrowserVersion(152, "FF");

    private static final int FIREFOX_ESR_NUMERIC = 140;

    /** Firefox ESR. */
    public static final BrowserVersion FIREFOX_ESR = new BrowserVersion(FIREFOX_ESR_NUMERIC, "FF-ESR");

    /** Latest Chrome. */
    public static final BrowserVersion CHROME = new BrowserVersion(150, "Chrome");

    /** Latest Edge. */
    public static final BrowserVersion EDGE = new BrowserVersion(150, "Edge");

    /**
     * Array with all supported browsers.
     */
    public static final BrowserVersion[] ALL_SUPPORTED_BROWSERS = {CHROME, EDGE, FIREFOX, FIREFOX_ESR};

    /**
     * The best supported browser version at the moment.
     */
    public static final BrowserVersion BEST_SUPPORTED = CHROME;

    /** The default browser version. */
    private static BrowserVersion DefaultBrowserVersion_ = BEST_SUPPORTED;

    static {
        FIREFOX_ESR.applicationVersion_ = "5.0 (Windows)";
        FIREFOX_ESR.userAgent_ = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:"
                                    + FIREFOX_ESR.getBrowserVersionNumeric() + ".0) Gecko/20100101 Firefox/"
                                    + FIREFOX_ESR.getBrowserVersionNumeric() + ".0";
        FIREFOX_ESR.buildId_ = "20181001000000";
        FIREFOX_ESR.vendor_ = "";
        FIREFOX_ESR.productSub_ = "20100101";
        FIREFOX_ESR.headerNamesOrdered_ = new String[] {
            HttpHeader.HOST,
            HttpHeader.USER_AGENT,
            HttpHeader.ACCEPT,
            HttpHeader.ACCEPT_LANGUAGE,
            HttpHeader.ACCEPT_ENCODING,
            HttpHeader.CONNECTION,
            HttpHeader.REFERER,
            HttpHeader.COOKIE,
            HttpHeader.UPGRADE_INSECURE_REQUESTS,
            HttpHeader.SEC_FETCH_DEST,
            HttpHeader.SEC_FETCH_MODE,
            HttpHeader.SEC_FETCH_SITE,
            HttpHeader.SEC_FETCH_USER,
            HttpHeader.PRIORITY};
        FIREFOX_ESR.htmlAcceptHeader_ = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        FIREFOX_ESR.acceptLanguageHeader_ = "en-US,en;q=0.5";
        FIREFOX_ESR.xmlHttpRequestAcceptHeader_ = "*/*";
        FIREFOX_ESR.imgAcceptHeader_ = "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5";
        FIREFOX_ESR.cssAcceptHeader_ = "text/css,*/*;q=0.1";
        FIREFOX_ESR.fontHeights_ = new int[] {
            0, 2, 3, 5, 6, 6, 7, 9, 10, 11, 12, 13, 15, 16, 16, 17, 18, 20, 21, 22, 23, 25, 26, 26, 28, 29,
            31, 32, 33, 34, 35, 37, 38, 38, 39, 41, 42, 43, 44, 45, 47, 48, 48, 49, 51, 52, 53, 54, 56, 58, 59, 59,
            60, 61, 63, 64, 65, 66, 68, 69, 69, 70, 71, 73, 74, 75, 76, 77, 79, 79,
            80, 82, 84, 85, 86, 87, 88, 90, 91, 91, 92, 94, 95, 96, 97, 98,
            100, 101, 101, 102, 103, 105, 106, 107, 108, 111, 112, 112, 113, 114, 116, 117, 118, 119,
            120, 122, 122, 123, 124, 126, 127, 128, 129, 130, 132, 132, 133, 134, 137, 138, 139,
            140, 141, 143, 143, 144, 145, 146, 148};

        FIREFOX.applicationVersion_ = "5.0 (Windows)";
        FIREFOX.userAgent_ = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:"
                                            + FIREFOX.getBrowserVersionNumeric() + ".0) Gecko/20100101 Firefox/"
                                            + FIREFOX.getBrowserVersionNumeric() + ".0";
        FIREFOX.buildId_ = "20181001000000";
        FIREFOX.vendor_ = "";
        FIREFOX.productSub_ = "20100101";
        FIREFOX.headerNamesOrdered_ = new String[] {
            HttpHeader.HOST,
            HttpHeader.USER_AGENT,
            HttpHeader.ACCEPT,
            HttpHeader.ACCEPT_LANGUAGE,
            HttpHeader.ACCEPT_ENCODING,
            HttpHeader.CONNECTION,
            HttpHeader.REFERER,
            HttpHeader.COOKIE,
            HttpHeader.UPGRADE_INSECURE_REQUESTS,
            HttpHeader.SEC_FETCH_DEST,
            HttpHeader.SEC_FETCH_MODE,
            HttpHeader.SEC_FETCH_SITE,
            HttpHeader.SEC_FETCH_USER,
            HttpHeader.PRIORITY};
        FIREFOX.htmlAcceptHeader_ = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        FIREFOX.acceptLanguageHeader_ = "en-US,en;q=0.9";
        FIREFOX.xmlHttpRequestAcceptHeader_ = "*/*";
        FIREFOX.imgAcceptHeader_ = "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5";
        FIREFOX.cssAcceptHeader_ = "text/css,*/*;q=0.1";
        FIREFOX.fontHeights_ = new int[] {
            0, 2, 3, 5, 6, 6, 7, 9, 10, 11, 12, 13, 15, 16, 16, 17, 18, 20, 21, 22, 23, 25, 26, 26, 28, 29,
            31, 32, 33, 34, 35, 37, 38, 38, 39, 41, 42, 43, 44, 45, 47, 48, 48, 49, 51, 52, 53, 54, 56, 58, 59, 59,
            60, 61, 63, 64, 65, 66, 68, 69, 69, 70, 71, 73, 74, 75, 76, 77, 79, 79,
            80, 82, 84, 85, 86, 87, 88, 90, 91, 91, 92, 94, 95, 96, 97, 98,
            100, 101, 101, 102, 103, 105, 106, 107, 108, 111, 112, 112, 113, 114, 116, 117, 118, 119,
            120, 122, 122, 123, 124, 126, 127, 128, 129, 130, 132, 132, 133, 134, 137, 138, 139,
            140, 141, 143, 143, 144, 145, 146, 148};

        // CHROME (Win10 64bit)
        CHROME.applicationVersion_ = "5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
                                        + CHROME.getBrowserVersionNumeric() + ".0.0.0 Safari/537.36";
        CHROME.userAgent_ = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
                                        + CHROME.getBrowserVersionNumeric() + ".0.0.0 Safari/537.36";

        CHROME.vendor_ = "Google Inc.";
        CHROME.productSub_ = "20030107";
        CHROME.headerNamesOrdered_ = new String[] {
            HttpHeader.HOST,
            HttpHeader.CONNECTION,
            HttpHeader.SEC_CH_UA,
            HttpHeader.SEC_CH_UA_MOBILE,
            HttpHeader.SEC_CH_UA_PLATFORM,
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
        CHROME.acceptLanguageHeader_ = "en-US,en;q=0.9";
        CHROME.htmlAcceptHeader_ = "text/html,application/xhtml+xml,application/xml;"
                                            + "q=0.9,image/avif,image/webp,image/apng,*/*;"
                                            + "q=0.8,application/signed-exchange;v=b3;q=0.7";
        CHROME.imgAcceptHeader_ = "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8";
        CHROME.cssAcceptHeader_ = "text/css,*/*;q=0.1";
        CHROME.scriptAcceptHeader_ = "*/*";

        CHROME.secClientHintUserAgentHeader_ = "\"Not;A=Brand\";v=\"8\", \"Chromium\";v=\""
                + CHROME.getBrowserVersionNumeric() + "\", \"Google Chrome\";v=\""
                + CHROME.getBrowserVersionNumeric() + "\"";

        CHROME.fontHeights_ = new int[] {
            0, 1, 2, 4, 5, 5, 6, 8, 9, 10, 11, 12, 15, 16, 16, 17, 18, 20, 21, 22, 23, 25, 26, 26,
            27, 28, 30, 31, 32, 33, 34, 36, 37, 37, 38, 40, 42, 43, 44, 45, 47, 48, 48, 49, 51, 52, 53, 54, 55, 57,
            58, 58, 59, 60, 62, 63, 64, 65, 67, 69, 69, 70, 71, 73, 74, 75, 76, 77, 79, 79, 80, 81, 83, 84, 85, 86,
            87, 89, 90, 90, 91, 93, 94, 96, 97, 98, 100, 101, 101, 102, 103, 105, 106, 107, 108, 110, 111, 111, 112,
            113, 115, 116, 117, 118, 119, 121, 122, 123, 124, 126, 127, 128, 129, 130, 132, 132, 133, 134, 136, 137,
            138, 139, 140, 142, 142, 143, 144, 145, 147};

        // EDGE (Win10 64bit)
        EDGE.applicationVersion_ = "5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
                                        + EDGE.getBrowserVersionNumeric() + ".0.0.0 Safari/537.36 Edg/"
                                        + EDGE.getBrowserVersionNumeric() + ".0.0.0";
        EDGE.userAgent_ = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
                                        + EDGE.getBrowserVersionNumeric() + ".0.0.0 Safari/537.36 Edg/"
                                        + EDGE.getBrowserVersionNumeric() + ".0.0.0";

        EDGE.vendor_ = "Google Inc.";
        EDGE.productSub_ = "20030107";
        EDGE.headerNamesOrdered_ = new String[] {
            HttpHeader.HOST,
            HttpHeader.CONNECTION,
            HttpHeader.SEC_CH_UA,
            HttpHeader.SEC_CH_UA_MOBILE,
            HttpHeader.SEC_CH_UA_PLATFORM,
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
        EDGE.acceptLanguageHeader_ = "en-US,en;q=0.9";
        EDGE.htmlAcceptHeader_ = "text/html,application/xhtml+xml,application/xml;"
                                            + "q=0.9,image/avif,image/webp,image/apng,*/*;"
                                            + "q=0.8,application/signed-exchange;v=b3;q=0.7";
        EDGE.imgAcceptHeader_ = "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8";
        EDGE.cssAcceptHeader_ = "text/css,*/*;q=0.1";
        EDGE.scriptAcceptHeader_ = "*/*";

        EDGE.secClientHintUserAgentHeader_ = "\"Not;A=Brand\";v=\"8\", \"Chromium\";v=\""
                + EDGE.getBrowserVersionNumeric() + "\", \"Microsoft Edge\";v=\""
                + EDGE.getBrowserVersionNumeric() + "\"";

        EDGE.fontHeights_ = new int[] {
            0, 1, 2, 4, 5, 5, 6, 8, 9, 10, 11, 12, 15, 16, 16, 17, 18, 20, 21, 22, 23, 25, 26, 26,
            27, 28, 30, 31, 32, 33, 34, 36, 37, 37, 38, 40, 42, 43, 44, 45, 47, 48, 48, 49, 51, 52, 53, 54, 55, 57,
            58, 58, 59, 60, 62, 63, 64, 65, 67, 69, 69, 70, 71, 73, 74, 75, 76, 77, 79, 79, 80, 81, 83, 84, 85, 86,
            87, 89, 90, 90, 91, 93, 94, 96, 97, 98, 100, 101, 101, 102, 103, 105, 106, 107, 108, 110, 111, 111, 112,
            113, 115, 116, 117, 118, 119, 121, 122, 123, 124, 126, 127, 128, 129, 130, 132, 132, 133, 134, 136, 137,
            138, 139, 140, 142, 142, 143, 144, 145, 147};

        initUploadMimeTypes();
        initMediaResources();
    }

    private final int browserVersionNumeric_;
    private final String nickname_;

    private String applicationCodeName_;
    private String applicationMinorVersion_;
    private String applicationName_;
    private String applicationVersion_;
    private String buildId_;
    private String productSub_;
    private String vendor_;
    private Locale browserLocale_;
    private boolean onLine_;
    private String platform_;
    private TimeZone systemTimezone_;
    private String userAgent_;
    private final Set<BrowserVersionFeatures> features_;
    private String acceptEncodingHeader_;
    private String acceptLanguageHeader_;
    private String htmlAcceptHeader_;
    private String imgAcceptHeader_;
    private String cssAcceptHeader_;
    private String scriptAcceptHeader_;
    private String secClientHintUserAgentHeader_;
    private String secClientHintUserAgentPlatformHeader_;
    private String xmlHttpRequestAcceptHeader_;
    private String[] headerNamesOrdered_;
    private int[] fontHeights_;
    private final Map<String, String> uploadMimeTypes_;
    private final HashSet<MediaResourceType> maybeMediaResource_;
    private final HashSet<MediaResourceType> probablyMediaResources_;

    /**
     * Creates a new browser version instance.
     *
     * @param browserVersionNumeric the floating number version of the browser
     * @param nickname the short name of the browser (like "FF", "CHROME", ...) - has to be unique
     */
    BrowserVersion(final int browserVersionNumeric, final String nickname) {
        browserVersionNumeric_ = browserVersionNumeric;
        nickname_ = nickname;

        applicationCodeName_ = "Mozilla";
        applicationMinorVersion_ = "0";
        applicationName_ = "Netscape";
        onLine_ = true;
        platform_ = "Win32";

        browserLocale_ = Locale.forLanguageTag("en-US");
        systemTimezone_ = TimeZone.getTimeZone("America/New_York");

        acceptEncodingHeader_ = "gzip, deflate, br";
        htmlAcceptHeader_ = "*/*";
        imgAcceptHeader_ = "*/*";
        cssAcceptHeader_ = "*/*";
        scriptAcceptHeader_ = "*/*";
        xmlHttpRequestAcceptHeader_ = "*/*";
        secClientHintUserAgentHeader_ = "";
        secClientHintUserAgentPlatformHeader_ = "\"Windows\"";

        features_ = EnumSet.noneOf(BrowserVersionFeatures.class);
        uploadMimeTypes_ = new HashMap<>();

        maybeMediaResource_ = new HashSet<>();
        probablyMediaResources_ = new HashSet<>();

        initFeatures();
    }

    /**
     * Returns whether the same browser.
     *
     * @param other the {@link BrowserVersion} to compare with
     * @return true if the nickname and the numeric version are the same
     */
    public boolean isSameBrowser(final BrowserVersion other) {
        return Objects.equals(nickname_, other.nickname_) && browserVersionNumeric_ == other.browserVersionNumeric_;
    }

    private void initFeatures() {
        final SupportedBrowser expectedBrowser;
        if (isEdge()) {
            expectedBrowser = SupportedBrowser.EDGE;
        }
        else if (isFirefoxESR()) {
            expectedBrowser = SupportedBrowser.FF_ESR;
        }
        else if (isFirefox()) {
            expectedBrowser = SupportedBrowser.FF;
        }
        else {
            expectedBrowser = SupportedBrowser.CHROME;
        }

        for (final BrowserVersionFeatures features : BrowserVersionFeatures.values()) {
            try {
                final Field field = BrowserVersionFeatures.class.getField(features.name());
                final BrowserFeature browserFeature = field.getAnnotation(BrowserFeature.class);
                if (browserFeature != null) {
                    for (final SupportedBrowser browser : browserFeature.value()) {
                        if (expectedBrowser == browser) {
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

    private static void initUploadMimeTypes() {
        // shared by all four browsers
        final BrowserVersion[] all = {CHROME, EDGE, FIREFOX, FIREFOX_ESR};
        for (final BrowserVersion browser : all) {
            browser.registerUploadMimeType("html",  MimeType.TEXT_HTML);
            browser.registerUploadMimeType("htm",   MimeType.TEXT_HTML);
            browser.registerUploadMimeType("css",   MimeType.TEXT_CSS);
            browser.registerUploadMimeType("xml",   MimeType.TEXT_XML);
            browser.registerUploadMimeType("gif",   MimeType.IMAGE_GIF);
            browser.registerUploadMimeType("jpeg",  MimeType.IMAGE_JPEG);
            browser.registerUploadMimeType("jpg",   MimeType.IMAGE_JPEG);
            browser.registerUploadMimeType("png",   MimeType.IMAGE_PNG);
            browser.registerUploadMimeType("pdf",   "application/pdf");
            browser.registerUploadMimeType("webp",  "image/webp");
            browser.registerUploadMimeType("mp4",   "video/mp4");
            browser.registerUploadMimeType("m4v",   "video/mp4");
            browser.registerUploadMimeType("mp3",   "audio/mpeg");
            browser.registerUploadMimeType("ogv",   "video/ogg");
            browser.registerUploadMimeType("ogm",   "video/ogg");
            browser.registerUploadMimeType("oga",   "audio/ogg");
            browser.registerUploadMimeType("opus",  "audio/ogg");
            browser.registerUploadMimeType("webm",  "video/webm");
            browser.registerUploadMimeType("wav",   "audio/wav");
            browser.registerUploadMimeType("xhtml", "application/xhtml+xml");
            browser.registerUploadMimeType("xht",   "application/xhtml+xml");
            browser.registerUploadMimeType("txt",   MimeType.TEXT_PLAIN);
            browser.registerUploadMimeType("text",  MimeType.TEXT_PLAIN);
        }

        // Chrome / Edge overrides (and additions)
        for (final BrowserVersion browser : new BrowserVersion[]{CHROME, EDGE}) {
            browser.registerUploadMimeType("m4a",  "audio/x-m4a");   // differs
            browser.registerUploadMimeType("ogg",  "audio/ogg");      // differs
            browser.registerUploadMimeType("flac", "audio/flac");     // differs

            browser.registerUploadMimeType("xhtm", "application/xhtml+xml"); // Chrome/Edge only
        }
        // Firefox / Firefox ESR overrides
        for (final BrowserVersion browser : new BrowserVersion[]{FIREFOX, FIREFOX_ESR}) {
            browser.registerUploadMimeType("m4a",  "audio/mp4");       // differs
            browser.registerUploadMimeType("ogg",  "application/ogg"); // differs
            browser.registerUploadMimeType("flac", "audio/x-flac");    // differs
            // xhtm absent in Firefox — intentionally not registered
        }
    }

    private static void initMediaResources() {
        // --- maybe: entries shared across all browsers ---
        final HashSet<MediaResourceType> common = new HashSet<>();
        common.add(new MediaResourceType("application/ogg", null));
        common.add(new MediaResourceType("audio/mp4", null));
        common.add(new MediaResourceType("audio/ogg", null));
        common.add(new MediaResourceType("audio/wav", null));
        common.add(new MediaResourceType("audio/webm", null));
        common.add(new MediaResourceType("audio/x-m4a", null));
        common.add(new MediaResourceType("audio/x-wav", null));
        common.add(new MediaResourceType("video/mp4", null));
        common.add(new MediaResourceType("video/webm", null));
        common.add(new MediaResourceType("video/x-matroska", null));

        // --- maybe: Firefox / Firefox ESR ---
        HashSet<MediaResourceType> ff = new HashSet<>(common);
        ff.add(new MediaResourceType("audio/aac", null));
        ff.add(new MediaResourceType("audio/flac", null));
        ff.add(new MediaResourceType("audio/mpeg", null));
        ff.add(new MediaResourceType("audio/wave", null));
        ff.add(new MediaResourceType("audio/x-aac", null));
        ff.add(new MediaResourceType("audio/x-flac", null));
        ff.add(new MediaResourceType("audio/x-pn-wav", null));
        ff.add(new MediaResourceType("video/quicktime", null));
        FIREFOX.maybeMediaResource_.addAll(ff);

        FIREFOX_ESR.maybeMediaResource_.addAll(ff);
        FIREFOX_ESR.maybeMediaResource_.remove(new MediaResourceType("video/x-matroska", null));

        // --- maybe: Chrome / Edge (Edge is a superset of Chrome) ---
        HashSet<MediaResourceType> chrome = new HashSet<>(common);
        chrome.add(new MediaResourceType("application/vnd.apple.mpegurl", null));
        chrome.add(new MediaResourceType("application/x-mpegURL", null));
        chrome.add(new MediaResourceType("video/3gpp", null));
        chrome.add(new MediaResourceType("video/ogg", null));
        chrome.add(new MediaResourceType("video/x-matroska", "avc1,vorbis"));
        CHROME.maybeMediaResource_.addAll(chrome);
        chrome.add(new MediaResourceType("audio/3gpp", null));
        EDGE.maybeMediaResource_.addAll(chrome);

        // --- probably: entries shared across all browsers ---
        common.clear();
        common.add(new MediaResourceType("audio/mp4", "mp4a.40.2"));
        common.add(new MediaResourceType("audio/mp4", "mp4a.40.5"));
        common.add(new MediaResourceType("audio/mp4", "mp4a.40.29"));
        common.add(new MediaResourceType("audio/mpeg", "mp3"));
        common.add(new MediaResourceType("audio/ogg", "flac"));
        common.add(new MediaResourceType("audio/ogg", "opus"));
        common.add(new MediaResourceType("audio/ogg", "vorbis"));
        common.add(new MediaResourceType("audio/wav", "1"));
        common.add(new MediaResourceType("audio/webm", "opus"));
        common.add(new MediaResourceType("audio/webm", "vorbis"));
        common.add(new MediaResourceType("video/mp4", "mp4a.40.2"));
        common.add(new MediaResourceType("video/mp4", "av01.0.01M.08"));
        common.add(new MediaResourceType("video/mp4", "av01.0.01M.08,mp4a.40.2"));
        common.add(new MediaResourceType("video/mp4", "avc1.42E01E"));
        common.add(new MediaResourceType("video/mp4", "avc1.42E01E,mp4a.40.2"));
        common.add(new MediaResourceType("video/mp4", "avc1.640028"));
        common.add(new MediaResourceType("video/mp4", "avc1.640028,mp4a.40.2"));
        common.add(new MediaResourceType("video/mp4", "avc1.4D401E"));
        common.add(new MediaResourceType("video/mp4", "avc1.4D401E,mp4a.40.2"));
        common.add(new MediaResourceType("video/mp4", "hev1.1.6.L93.90"));
        common.add(new MediaResourceType("video/mp4", "hev1.1.6.L93.90,mp4a.40.2"));
        common.add(new MediaResourceType("video/mp4", "hvc1.1.6.L93.90"));
        common.add(new MediaResourceType("video/mp4", "hvc1.1.6.L93.90,mp4a.40.2"));
        common.add(new MediaResourceType("video/webm", "opus"));
        common.add(new MediaResourceType("video/webm", "opus,vp9"));
        common.add(new MediaResourceType("video/webm", "vorbis"));
        common.add(new MediaResourceType("video/webm", "vorbis,vp8"));
        common.add(new MediaResourceType("video/webm", "vp8"));
        common.add(new MediaResourceType("video/webm", "vp9"));

        // --- probably: Firefox / Firefox ESR (adds AV1-in-WebM) ---
        ff = new HashSet<>(common);
        ff.add(new MediaResourceType("video/webm", "av1"));
        ff.add(new MediaResourceType("video/webm", "av1,opus"));
        FIREFOX.probablyMediaResources_.addAll(ff);
        FIREFOX_ESR.probablyMediaResources_.addAll(ff);

        // --- probably: Chrome / Edge (Edge adds Dolby AC-3 / E-AC-3) ---
        chrome = new HashSet<>(common);
        chrome.add(new MediaResourceType("audio/aac", null));
        chrome.add(new MediaResourceType("audio/flac", null));
        chrome.add(new MediaResourceType("audio/mp4", "mp4a.69"));
        chrome.add(new MediaResourceType("audio/mpeg", null));
        chrome.add(new MediaResourceType("video/3gpp", "avc1.42E01E,mp4a.40.2"));
        CHROME.probablyMediaResources_.addAll(chrome);
        chrome.add(new MediaResourceType("audio/mp4", "ac-3"));
        chrome.add(new MediaResourceType("audio/mp4", "ec-3"));
        chrome.add(new MediaResourceType("video/mp4", "ac-3"));
        chrome.add(new MediaResourceType("video/mp4", "ec-3"));
        EDGE.probablyMediaResources_.addAll(chrome);
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
     * Returns whether the chrome.
     *
     * Returns {@code true} if this <code>BrowserVersion</code> instance represents some
     * version of Google Chrome. Note that Google Chrome does not return 'Chrome'
     * in the application name, we have to look in the nickname.
     * @return whether this version is a version of a Chrome browser
     */
    public boolean isChrome() {
        return getNickname().startsWith("Chrome");
    }

    /**
     * Returns whether the edge.
     *
     * Returns {@code true} if this <code>BrowserVersion</code> instance represents some
     * version of Microsoft Edge.
     * @return whether this version is a version of the Edge browser
     */
    public boolean isEdge() {
        return getNickname().startsWith("Edge");
    }

    /**
     * Returns whether the firefox.
     *
     * Returns {@code true} if this <code>BrowserVersion</code> instance represents some
     * version of Firefox.
     * @return whether this version is a version of a Firefox browser
     */
    public boolean isFirefox() {
        return getNickname().startsWith("FF");
    }

    /**
     * Returns whether the firefox esr.
     *
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * @return whether this represents the Firefox Extended Support Release (ESR) version
     */
    public boolean isFirefoxESR() {
        return isFirefox() && getBrowserVersionNumeric() == FIREFOX_ESR_NUMERIC;
    }

    /**
     * Returns the short name of the browser like {@code FF}, {@code CHROME}, etc.
     *
     * @return the short name (if any)
     */
    public String getNickname() {
        return nickname_;
    }

    /**
     * Returns the browser version numeric.
     *
     * @return the browserVersionNumeric
     */
    public int getBrowserVersionNumeric() {
        return browserVersionNumeric_;
    }

    /**
     * Returns the application code name, for example "Mozilla".
     * Default value is "Mozilla" if not explicitly configured.
     * @return the application code name
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Navigator/appCodeName">MDN documentation</a>
     */
    public String getApplicationCodeName() {
        return applicationCodeName_;
    }

    /**
     * Returns the application minor version, for example "0".
     * Default value is "0" if not explicitly configured.
     * This is a legacy, non-standard property that was only ever implemented by Internet
     * Explorer (as {@code navigator.appMinorVersion}).
     * @return the application minor version
     */
    public String getApplicationMinorVersion() {
        return applicationMinorVersion_;
    }

    /**
     * Returns the application name, for example "Netscape".
     * @return the application name
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Navigator/appName">MDN documentation</a>
     */
    public String getApplicationName() {
        return applicationName_;
    }

    /**
     * Returns the application version, for example "4.0 (compatible; MSIE 6.0b; Windows 98)".
     * @return the application version
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Navigator/appVersion">MDN documentation</a>
     */
    public String getApplicationVersion() {
        return applicationVersion_;
    }

    /**
     * Returns the vendor.
     *
     * @return the vendor
     */
    public String getVendor() {
        return vendor_;
    }

    /**
     * Returns the browser locale.
     * Default value is {@code Locale.forLanguageTag("en-US")} if not explicitly configured.
     * @return the system locale
     */
    public Locale getBrowserLocale() {
        return browserLocale_;
    }

    /**
     * Returns the browser application language, for example "en-us".
     * Default value is {@code "en-US"} if not explicitly configured.
     * @return the browser application language
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Navigator/language">MDN documentation</a>
     */
    public String getBrowserLanguage() {
        return browserLocale_.toLanguageTag();
    }

    /**
     * Returns {@code true} if the browser is currently online.
     * Default value is {@code true} if not explicitly configured.
     * @return {@code true} if the browser is currently online
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Navigator/onLine">MDN documentation</a>
     */
    public boolean isOnLine() {
        return onLine_;
    }

    /**
     * Returns the platform on which the application is running, for example "Win32".
     * Default value is 'Win32' if not explicitly configured.
     * @return the platform on which the application is running
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Navigator/platform">MDN documentation</a>
     */
    public String getPlatform() {
        return platform_;
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
     * Returns the script accept header.
     *
     * Returns the value used by the browser for the {@code Accept} header
     * if requesting a script.
     * @return the accept header string
     */
    public String getScriptAcceptHeader() {
        return scriptAcceptHeader_;
    }

    /**
     * Returns the xml http request accept header.
     *
     * Returns the value used by the browser for the {@code Accept} header
     * if performing an XMLHttpRequest.
     * @return the accept header string
     */
    public String getXmlHttpRequestAcceptHeader() {
        return xmlHttpRequestAcceptHeader_;
    }

    /**
     * Returns the img accept header.
     *
     * Returns the value used by the browser for the {@code Accept} header
     * if requesting an image.
     * @return the accept header string
     */
    public String getImgAcceptHeader() {
        return imgAcceptHeader_;
    }

    /**
     * Returns the css accept header.
     *
     * Returns the value used by the browser for the {@code Accept} header
     * if requesting a CSS declaration.
     * @return the accept header string
     */
    public String getCssAcceptHeader() {
        return cssAcceptHeader_;
    }

    /**
     * Returns the value used by the browser for the {@code sec-ch-ua} header.
     * @return the sec-ch-ua header string
     */
    public String getSecClientHintUserAgentHeader() {
        return secClientHintUserAgentHeader_;
    }

    /**
     * Returns the value used by the browser for the {@code sec-ch-ua-platform} header.
     * @return the sec-ch-ua-platform header string
     */
    public String getSecClientHintUserAgentPlatformHeader() {
        return secClientHintUserAgentPlatformHeader_;
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
     * @return the productSub
     */
    public String getProductSub() {
        return productSub_;
    }

    /**
     * Gets the header names, so they are sent in the given order (if included in the request).
     * @return the header names, in the order they should be sent
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
     * Returns the corresponding height of the specified {@code fontSize}.
     * @param fontSize the font size
     * @return the corresponding height
     */
    public int getFontHeight(final String fontSize) {
        if (fontHeights_ == null || StringUtils.isEmptyOrNull(fontSize)) {
            return 18;
        }

        if ("xx-small".equalsIgnoreCase(fontSize)) {
            return fontHeights_[9];
        }
        if ("x-small".equalsIgnoreCase(fontSize)) {
            return fontHeights_[10];
        }
        if ("small".equalsIgnoreCase(fontSize)) {
            return fontHeights_[13];
        }
        if ("medium".equalsIgnoreCase(fontSize)) {
            return fontHeights_[16];
        }
        if ("large".equalsIgnoreCase(fontSize)) {
            return fontHeights_[18];
        }
        if ("x-large".equalsIgnoreCase(fontSize)) {
            return fontHeights_[24];
        }
        if ("xx-large".equalsIgnoreCase(fontSize)) {
            return fontHeights_[32];
        }
        if ("xxx-large".equalsIgnoreCase(fontSize)) {
            return fontHeights_[48];
        }

        if ("smaller".equalsIgnoreCase(fontSize)) {
            return fontHeights_[13];
        }
        if ("larger".equalsIgnoreCase(fontSize)) {
            return fontHeights_[19];
        }

        final int fontSizeInt = CssPixelValueConverter.pixelValue(fontSize);
        if (fontSizeInt < fontHeights_.length) {
            return fontHeights_[fontSizeInt];
        }

        return (int) (fontSizeInt * 1.2);
    }

    /**
     * Performs this operation.
     *
     * @return the pixesPerChar
     *
     * @deprecated as of version 5.3.0; use {@link #getPixelsPerChar()} instead.
     */
    @Deprecated(since = "5.3.0", forRemoval = true)
    public int getPixesPerChar() {
        return getPixelsPerChar();
    }

    /**
     * Returns the pixels per char.
     *
     * @return the pixelsPerChar (currently hard coded 10)
     */
    public int getPixelsPerChar() {
        return 10;
    }

    /**
     * Performs this operation.
     *
     * Determines whether this browser thinks it can play the given media resource type,
     * mirroring the result of the DOM method {@code HTMLMediaElement.canPlayType()}.
     *
     * <p>The {@code type} is parsed into a MIME type and an optional {@code codecs}
     * parameter (see {@link MediaResourceType#parse(String)} for the exact parsing rules).
     * The parsed value is then looked up first against this browser's "probably" playable
     * resources (MIME type plus a known-good codec list) and, failing that, against its
     * "maybe" playable resources (MIME type alone, with no codec guarantee).</p>
     *
     * @param type the media type string to test, e.g. {@code "video/mp4"} or
     *             {@code "video/mp4; codecs=\"avc1.42E01E, mp4a.40.2\""}
     * @return {@code "probably"} if this exact MIME type and codec combination is known to
     *         be supported, {@code "maybe"} if the MIME type alone (with no codec specified)
     *         is known to be supported, or {@code ""} if {@code type} is blank, or if a
     *         codec was specified but that exact MIME/codec pairing isn't a known
     *         {@code "probably"} match
     * @see MediaResourceType#parse(String)
     * @see <a href="https://html.spec.whatwg.org/multipage/media.html#dom-navigator-canplaytype">
     *      HTML Living Standard – canPlayType</a>
     */
    public String canPlayType(final String type) {
        final MediaResourceType mediaType = MediaResourceType.parse(type);
        if (mediaType == null) {
            return "";
        }

        if (probablyMediaResources_.contains(mediaType)) {
            return "probably";
        }

        if (maybeMediaResource_.contains(mediaType)) {
            return "maybe";
        }

        return "";
    }

    @Override
    public String toString() {
        return nickname_;
    }

    /**
     * Because BrowserVersion is immutable we need a builder
     * for this complex object setup.
     */
    @SuppressWarnings("PMD.LinguisticNaming")
    public static class BrowserVersionBuilder {
        private final BrowserVersion workPiece_;

        /**
         * Performs this operation.
         *
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
                .setOnLine(version.isOnLine())
                .setPlatform(version.getPlatform())
                .setSystemTimezone(version.getSystemTimezone())
                .setBuildId(version.getBuildId())
                .setProductSub(version.getProductSub())
                .setAcceptEncodingHeader(version.getAcceptEncodingHeader())
                .setAcceptLanguageHeader(version.getAcceptLanguageHeader())
                .setHtmlAcceptHeader(version.getHtmlAcceptHeader())
                .setImgAcceptHeader(version.getImgAcceptHeader())
                .setCssAcceptHeader(version.getCssAcceptHeader())
                .setScriptAcceptHeader(version.getScriptAcceptHeader())
                .setXmlHttpRequestAcceptHeader(version.getXmlHttpRequestAcceptHeader())
                .setSecClientHintUserAgentHeader(version.getSecClientHintUserAgentHeader())
                .setSecClientHintUserAgentPlatformHeader(version.getSecClientHintUserAgentPlatformHeader())
                .setHeaderNamesOrdered(version.getHeaderNamesOrdered())
                .setFontHeights(version.fontHeights_);

            workPiece_.features_.addAll(version.features_);
            workPiece_.uploadMimeTypes_.putAll(version.uploadMimeTypes_);
            workPiece_.maybeMediaResource_.addAll(version.maybeMediaResource_);
            workPiece_.probablyMediaResources_.addAll(version.probablyMediaResources_);
        }

        /**
         * Performs this operation.
         *
         * @return the new immutable browser version
         */
        public BrowserVersion build() {
            return workPiece_;
        }

        /**
         * Sets the application minor version.
         *
         * @param applicationMinorVersion the applicationMinorVersion to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setApplicationMinorVersion(final String applicationMinorVersion) {
            workPiece_.applicationMinorVersion_ = applicationMinorVersion;
            return this;
        }

        /**
         * Sets the application name.
         *
         * @param applicationName the applicationName to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setApplicationName(final String applicationName) {
            workPiece_.applicationName_ = applicationName;
            return this;
        }

        /**
         * Sets the application version.
         *
         * @param applicationVersion the applicationVersion to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setApplicationVersion(final String applicationVersion) {
            workPiece_.applicationVersion_ = applicationVersion;
            return this;
        }

        /**
         * Sets the vendor.
         *
         * @param vendor the vendor to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setVendor(final String vendor) {
            workPiece_.vendor_ = vendor;
            return this;
        }

        /**
         * Sets the application code name.
         *
         * @param applicationCodeName the applicationCodeName to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setApplicationCodeName(final String applicationCodeName) {
            workPiece_.applicationCodeName_ = applicationCodeName;
            return this;
        }

        /**
         * Changes the browser language property. This is used for various output
         * formating. If you like change the language the browser requests from the server
         * you have to adjust the {@link #setAcceptLanguageHeader(String)}.
         *
         * @param browserLanguage the browserLanguage to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setBrowserLanguage(final String browserLanguage) {
            workPiece_.browserLocale_ = Locale.forLanguageTag(browserLanguage);
            return this;
        }

        /**
         * Sets the on line.
         *
         * @param onLine the onLine to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setOnLine(final boolean onLine) {
            workPiece_.onLine_ = onLine;
            return this;
        }

        /**
         * Sets the platform.
         *
         * @param platform the platform to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setPlatform(final String platform) {
            workPiece_.platform_ = platform;
            return this;
        }

        /**
         * Sets the system timezone.
         *
         * @param systemTimezone the systemTimezone to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setSystemTimezone(final TimeZone systemTimezone) {
            workPiece_.systemTimezone_ = systemTimezone;
            return this;
        }

        /**
         * Sets the user agent.
         *
         * @param userAgent the userAgent to set
         * @return this for fluent use
         */
        public BrowserVersionBuilder setUserAgent(final String userAgent) {
            workPiece_.userAgent_ = userAgent;
            return this;
        }

        /**
         * Sets the accept encoding header.
         *
         * @param acceptEncodingHeader the {@code Accept-Encoding} header
         * @return this for fluent use
         */
        public BrowserVersionBuilder setAcceptEncodingHeader(final String acceptEncodingHeader) {
            workPiece_.acceptEncodingHeader_ = acceptEncodingHeader;
            return this;
        }

        /**
         * Sets the accept language header.
         *
         * @param acceptLanguageHeader the {@code Accept-Language} header
         * @return this for fluent use
         */
        public BrowserVersionBuilder setAcceptLanguageHeader(final String acceptLanguageHeader) {
            workPiece_.acceptLanguageHeader_ = acceptLanguageHeader;
            return this;
        }

        /**
         * Sets the html accept header.
         *
         * @param htmlAcceptHeader the {@code Accept} header to be used when retrieving pages
         * @return this for fluent use
         */
        public BrowserVersionBuilder setHtmlAcceptHeader(final String htmlAcceptHeader) {
            workPiece_.htmlAcceptHeader_ = htmlAcceptHeader;
            return this;
        }

        /**
         * Sets the img accept header.
         *
         * @param imgAcceptHeader the {@code Accept} header to be used when retrieving images
         * @return this for fluent use
         */
        public BrowserVersionBuilder setImgAcceptHeader(final String imgAcceptHeader) {
            workPiece_.imgAcceptHeader_ = imgAcceptHeader;
            return this;
        }

        /**
         * Sets the css accept header.
         *
         * @param cssAcceptHeader the {@code Accept} header to be used when retrieving pages
         * @return this for fluent use
         */
        public BrowserVersionBuilder setCssAcceptHeader(final String cssAcceptHeader) {
            workPiece_.cssAcceptHeader_ = cssAcceptHeader;
            return this;
        }

        /**
         * Sets the script accept header.
         *
         * @param scriptAcceptHeader the {@code Accept} header to be used when retrieving scripts
         * @return this for fluent use
         */
        public BrowserVersionBuilder setScriptAcceptHeader(final String scriptAcceptHeader) {
            workPiece_.scriptAcceptHeader_ = scriptAcceptHeader;
            return this;
        }

        /**
         * Sets the xml http request accept header.
         *
         * @param xmlHttpRequestAcceptHeader the {@code Accept} header to be used when
         *        performing XMLHttpRequests
         * @return this for fluent use
         */
        public BrowserVersionBuilder setXmlHttpRequestAcceptHeader(final String xmlHttpRequestAcceptHeader) {
            workPiece_.xmlHttpRequestAcceptHeader_ = xmlHttpRequestAcceptHeader;
            return this;
        }

        /**
         * Sets the sec client hint user agent header.
         *
         * @param secClientHintUserAgentHeader the {@code sec-ch-ua} header value
         * @return this for fluent use
         */
        public BrowserVersionBuilder setSecClientHintUserAgentHeader(final String secClientHintUserAgentHeader) {
            workPiece_.secClientHintUserAgentHeader_ = secClientHintUserAgentHeader;
            return this;
        }

        /**
         * Sets the sec client hint user agent platform header.
         *
         * @param secClientHintUserAgentPlatformHeader the {@code sec-ch-ua-platform} header value
         * @return this for fluent use
         */
        public BrowserVersionBuilder setSecClientHintUserAgentPlatformHeader(final String secClientHintUserAgentPlatformHeader) {
            workPiece_.secClientHintUserAgentPlatformHeader_ = secClientHintUserAgentPlatformHeader;
            return this;
        }

        /**
         * Sets the product sub.
         *
         * @param productSub the productSub
         * @return this for fluent use
         */
        BrowserVersionBuilder setProductSub(final String productSub) {
            workPiece_.productSub_ = productSub;
            return this;
        }

        /**
         * Sets the header names ordered.
         *
         * @param headerNamesOrdered the headerNamesOrdered
         * @return this for fluent use
         */
        BrowserVersionBuilder setHeaderNamesOrdered(final String[] headerNamesOrdered) {
            workPiece_.headerNamesOrdered_ = headerNamesOrdered;
            return this;
        }

        /**
         * Sets the font heights.
         *
         * @param fontHeights the fontHeights
         * @return this for fluent use
         */
        BrowserVersionBuilder setFontHeights(final int[] fontHeights) {
            workPiece_.fontHeights_ = fontHeights;
            return this;
        }

        /**
         * Sets the build id.
         *
         * @param buildId the buildId
         * @return this for fluent use
         */
        BrowserVersionBuilder setBuildId(final String buildId) {
            workPiece_.buildId_ = buildId;
            return this;
        }
    }

    /**
     * Performs this operation.
     *
     * Represents a media type that a browser can play, combining a MIME type
     * with an optional set of codecs.
     * <p>Instances are immutable.</p>
     *
     * <p>Instances are used as keys in the two lookup sets held by each
     * {@link BrowserVersion} ({@code maybeMediaResource_} and
     * {@code probablyMediaResources_}) and are queried by
     * {@link BrowserVersion#canPlayType(String)} to determine the value
     * returned by the {@code HTMLMediaElement.canPlayType()} DOM method:
     * </p>
     * <ul>
     *   <li>{@code "maybe"}    — the MIME type alone is in the
     *       {@code maybeMediaResource_} set (no codec information given).</li>
     *   <li>{@code "probably"} — the MIME type together with the specific
     *       codec list is in the {@code probablyMediaResources_} set.</li>
     *   <li>{@code ""}         — neither set contains a matching entry.</li>
     * </ul>
     *
     * <p>Equality and hashing are based on both the MIME type string and the
     * normalised codec string, so {@code new MediaResourceType("video/mp4", null)}
     * and {@code new MediaResourceType("video/mp4", "avc1.42E01E")} are
     * considered distinct entries.</p>
     *
     * @see BrowserVersion#canPlayType(String)
     * @see <a href="https://html.spec.whatwg.org/multipage/media.html#dom-navigator-canplaytype">
     *      HTML Living Standard – canPlayType</a>
     */
    public static class MediaResourceType implements Serializable {
        /** The MIME type part of the media resource type, e.g. {@code "video/mp4"}. */
        private final String mime_;

        /**
         * The normalised, comma-separated, alphabetically sorted codec list,
         * or {@code null} when no {@code codecs} parameter was supplied.
         */
        private final String codecs_;

         /**
          * Performs this operation.
          *
          * Creates a new {@code MediaResourceType} with the given MIME type and
          * optional codec string.
          *
          * @param mime   the MIME type, e.g. {@code "audio/ogg"};
          *               must not be {@code null}
          * @param codecs the normalised codec list, e.g. {@code "opus"} or
          *               {@code "avc1.42E01E,mp4a.40.2"}, or {@code null} if
          *               no codec information is associated with this entry
          */
        public MediaResourceType(final String mime, final String codecs) {
            mime_ = mime;
            codecs_ = codecs;
        }

        /**
         * Performs this operation.
         *
         * Parses a raw {@code canPlayType()} argument string into a
         * {@code MediaResourceType} suitable for a set lookup.
         *
         * <p>The parsing rules follow the
         * <a href="https://html.spec.whatwg.org/multipage/media.html#dom-navigator-canplaytype">
         * HTML Living Standard</a>:
         * </p>
         * <ol>
         *   <li>Blank or whitespace-only input returns {@code null} (the caller
         *       will map this to {@code ""}).</li>
         *   <li>If a semicolon ({@code ;}) is present, everything before it is
         *       treated as the MIME type and the remainder is inspected for a
         *       {@code codecs} parameter.</li>
         *   <li>When a {@code codecs} parameter is found its value is
         *       <em>normalised</em>: surrounding quotes are stripped, individual
         *       codec tokens are trimmed and sorted alphabetically, then
         *       re-joined with a comma. This ensures that
         *       {@code "vp8, vorbis"} and {@code "vorbis,vp8"} resolve to the
         *       same entry.</li>
         *   <li>A {@code codecs} key without a value (e.g. {@code "video/mp4; codecs"})
         *       is treated as if no codec information were present, yielding a
         *       codec of {@code null}.</li>
         *   <li>Input without a semicolon is used verbatim as the MIME type
         *       with a {@code null} codec.</li>
         * </ol>
         *
         * @param mediaResourceType the raw string passed to
         *                          {@code HTMLMediaElement.canPlayType()},
         *                          e.g. {@code "video/mp4; codecs=\"avc1.42E01E, mp4a.40.2\""}
         * @return the parsed {@code MediaResourceType}, or {@code null} if the
         *         input is blank (indicating an immediate {@code ""} result)
         */
        public static MediaResourceType parse(final String mediaResourceType) {
            if (StringUtils.isBlank(mediaResourceType)) {
                return null;
            }

            final String[] parts = mediaResourceType.split(";", -1);
            final String mime = parts[0].trim();

            if (parts.length == 1) {
                return new MediaResourceType(mime, null);
            }

            // Walk each parameter after the MIME type (parts[1..n]).
            for (int i = 1; i < parts.length; i++) {
                final String param = parts[i].trim();
                final int eqPos = param.indexOf('=');

                if (eqPos < 0) {
                    continue; // bare key with no value — skip
                }

                final String key = param.substring(0, eqPos).trim();
                if (!"codecs".equalsIgnoreCase(key)) {
                    continue; // not the codecs param — keep looking
                }

                String value = param.substring(eqPos + 1).trim();

                // Strip surrounding double-quotes if present.
                if (value.length() >= 2
                        && value.charAt(0) == '"'
                        && value.charAt(value.length() - 1) == '"') {
                    value = value.substring(1, value.length() - 1).trim();
                }

                // Strip surrounding single-quotes if present.
                if (value.length() >= 2
                        && value.charAt(0) == '\''
                        && value.charAt(value.length() - 1) == '\'') {
                    value = value.substring(1, value.length() - 1).trim();
                }

                if (value.isEmpty()) {
                    return new MediaResourceType(mime, null);
                }

                // Normalise: trim each token, sort, re-join.
                final String codecs = Arrays.stream(value.split(",", -1))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .sorted()
                        .collect(Collectors.joining(","));

                return new MediaResourceType(mime, codecs.isEmpty() ? null : codecs);
            }

            // Semicolons present but no codecs param found.
            return new MediaResourceType(mime, null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(codecs_, mime_);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MediaResourceType other = (MediaResourceType) obj;
            return Objects.equals(codecs_, other.codecs_) && Objects.equals(mime_, other.mime_);
        }
    }
}
