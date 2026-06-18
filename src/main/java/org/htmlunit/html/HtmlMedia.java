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
package org.htmlunit.html;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SgmlPage;
import org.htmlunit.util.StringUtils;

/**
 * HTML Media element, e.g. {@link HtmlAudio} or {@link HtmlVideo}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public abstract class HtmlMedia extends HtmlElement {

    private static final HashSet<MediaResourceType> CHROME_MAYBE_ = new HashSet<>();
    private static final HashSet<MediaResourceType> EDGE_MAYBE_ = new HashSet<>();
    private static final HashSet<MediaResourceType> FF_MAYBE_ = new HashSet<>();
    private static final HashSet<MediaResourceType> FF_ESR_MAYBE_ = new HashSet<>();

    private static final HashSet<MediaResourceType> CHROME_PROBABLY_ = new HashSet<>();
    private static final HashSet<MediaResourceType> EDGE_PROBABLY_ = new HashSet<>();
    private static final HashSet<MediaResourceType> FF_PROBABLY_ = new HashSet<>();
    private static final HashSet<MediaResourceType> FF_ESR_PROBABLY_ = new HashSet<>();

    static {
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

        HashSet<MediaResourceType> ff = new HashSet<>(common);
        ff.add(new MediaResourceType("audio/aac", null));
        ff.add(new MediaResourceType("audio/flac", null));
        ff.add(new MediaResourceType("audio/mpeg", null));
        ff.add(new MediaResourceType("audio/wave", null));
        ff.add(new MediaResourceType("audio/x-aac", null));
        ff.add(new MediaResourceType("audio/x-flac", null));
        ff.add(new MediaResourceType("audio/x-pn-wav", null));
        ff.add(new MediaResourceType("video/quicktime", null));
        FF_MAYBE_.addAll(ff);
        FF_ESR_MAYBE_.addAll(ff);
        FF_ESR_MAYBE_.remove(new MediaResourceType("video/x-matroska", null));

        HashSet<MediaResourceType> chrome = new HashSet<>(common);
        chrome.add(new MediaResourceType("application/vnd.apple.mpegurl", null));
        chrome.add(new MediaResourceType("application/x-mpegURL", null));
        chrome.add(new MediaResourceType("video/3gpp", null));
        chrome.add(new MediaResourceType("video/ogg", null));
        chrome.add(new MediaResourceType("video/x-matroska", "avc1,vorbis"));
        CHROME_MAYBE_.addAll(chrome);
        chrome.add(new MediaResourceType("audio/3gpp", null));
        EDGE_MAYBE_.addAll(chrome);

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

        ff = new HashSet<>(common);
        ff.add(new MediaResourceType("video/webm", "av1"));
        ff.add(new MediaResourceType("video/webm", "av1,opus"));
        FF_PROBABLY_.addAll(ff);
        FF_ESR_PROBABLY_.addAll(ff);

        chrome = new HashSet<>(common);
        chrome.add(new MediaResourceType("audio/aac", null));
        chrome.add(new MediaResourceType("audio/flac", null));
        chrome.add(new MediaResourceType("audio/mp4", "mp4a.69"));
        chrome.add(new MediaResourceType("audio/mpeg", null));
        chrome.add(new MediaResourceType("video/3gpp", "avc1.42E01E,mp4a.40.2"));
        CHROME_PROBABLY_.addAll(chrome);
        chrome.add(new MediaResourceType("audio/mp4", "ac-3"));
        chrome.add(new MediaResourceType("audio/mp4", "ec-3"));
        chrome.add(new MediaResourceType("video/mp4", "ac-3"));
        chrome.add(new MediaResourceType("video/mp4", "ec-3"));
        EDGE_PROBABLY_.addAll(chrome);
    }

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlMedia(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Determines whether the specified media type can be played back.
     * @param type the type
     * @param browserVersion the browser version
     * @return "probably", "maybe", or "". The current implementation returns ""
     */
    public String canPlayType(final String type, final BrowserVersion browserVersion) {
        final MediaResourceType mediaType = MediaResourceType.parse(type);
        if (mediaType == null) {
            return "";
        }

        final HashSet<MediaResourceType> maybe;
        if (browserVersion.isChrome()) {
            maybe = CHROME_MAYBE_;
        }
        else if (browserVersion.isEdge()) {
            maybe = EDGE_MAYBE_;
        }
        else if (browserVersion.isFirefoxESR()) {
            maybe = FF_ESR_MAYBE_;
        }
        else {
            maybe = FF_MAYBE_;
        }

        if (maybe.contains(mediaType)) {
            return "maybe";
        }

        final HashSet<MediaResourceType> probably;
        if (browserVersion.isChrome()) {
            probably = CHROME_PROBABLY_;
        }
        else if (browserVersion.isEdge()) {
            probably = EDGE_PROBABLY_;
        }
        else if (browserVersion.isFirefoxESR()) {
            probably = FF_ESR_PROBABLY_;
        }
        else {
            probably = FF_PROBABLY_;
        }

        if (probably.contains(mediaType)) {
            return "probably";
        }

        return "";
    }

    /**
     * Returns the value of the attribute {@code src}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code src} or an empty string if that attribute isn't defined
     */
    public final String getSrcAttribute() {
        return getSrcAttributeNormalized();
    }

    /**
     * @return the value of the {@code src} value
     */
    public String getSrc() {
        final String src = getSrcAttribute();
        if (StringUtils.isEmptyString(src)) {
            return src;
        }
        try {
            final HtmlPage page = (HtmlPage) getPage();
            return page.getFullyQualifiedUrl(src).toExternalForm();
        }
        catch (final MalformedURLException e) {
            final String msg = "Unable to create fully qualified URL for src attribute of media " + e.getMessage();
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * Sets the value of the {@code src} attribute.
     * @param src the value of the {@code src} attribute
     */
    public void setSrc(final String src) {
        setAttribute(SRC_ATTRIBUTE, src);
    }

    /**
     * Returns the absolute URL of the chosen media resource.
     * This could happen, for example, if the web server selects a
     * media file based on the resolution of the user's display.
     * The value is an empty string if the networkState property is EMPTY.
     * @return the absolute URL of the chosen media resource
     */
    public String getCurrentSrc() {
        return "";
    }

    protected static class MediaResourceType {
        private final String mime_;
        private final String codecs_;

        protected MediaResourceType(final String mime, final String codecs) {
            mime_ = mime;
            codecs_ = codecs;
        }

        protected static MediaResourceType parse(final String mediaResourceType) {
            if (StringUtils.isBlank(mediaResourceType)) {
                return null;
            }

            final int semPos = mediaResourceType.indexOf(';');
            if (semPos > -1) {
                final String mime = mediaResourceType.substring(0, semPos).trim();

                final int codecPos = mediaResourceType.indexOf("codecs", semPos);
                if (codecPos > -1) {
                    String codes = mediaResourceType.substring(codecPos + 6).trim();
                    if (codes.length() > 1 && '=' == codes.charAt(0)) {
                        codes = codes.substring(1).trim();

                        if (codes.length() > 1
                                && '"' == codes.charAt(0)
                                && '"' == codes.charAt(codes.length() - 1)) {
                            codes = codes.substring(1, codes.length() - 1).trim();
                        }

                        if (codes.length() == 0) {
                            return new MediaResourceType(mime, null);
                        }

                        final String codecs = Arrays
                                .stream(codes.split(","))
                                .map(String :: trim)
                                .sorted()
                                .collect(Collectors.joining(","));
                        return new MediaResourceType(mime, codecs);
                    }
                }
                return new MediaResourceType(mime, null);
            }

            return new MediaResourceType(mediaResourceType, null);
        }

        @Override
        public int hashCode() {
            return Objects.hash(codecs_, mime_);
        }

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
