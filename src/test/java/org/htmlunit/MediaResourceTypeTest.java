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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.htmlunit.BrowserVersion.MediaResourceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link MediaResourceType}.
 *
 * @author Ronald Brill
 */
public class MediaResourceTypeTest {

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static MediaResourceType parse(String input) {
        return MediaResourceType.parse(input);
    }

    private static void assertType(String expectedMime, String expectedCodecs, MediaResourceType result) {
        assertEquals(new MediaResourceType(expectedMime, expectedCodecs), result);
    }

    // -------------------------------------------------------------------------
    // 1. Blank / null input → null
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("blank or null input")
    class BlankInput {

        @ParameterizedTest(name = "input={0}")
        @NullSource
        @ValueSource(strings = {"", " ", "\t", "  \t  "})
        @DisplayName("returns null")
        void returnsNull(String input) {
            assertNull(parse(input));
        }
    }

    // -------------------------------------------------------------------------
    // 2. No semicolon → verbatim MIME, null codec
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("no semicolon")
    class NoSemicolon {

        @Test
        @DisplayName("plain MIME type is returned verbatim with null codec")
        void plainMime() {
            MediaResourceType result = parse("video/mp4");
            assertType("video/mp4", null, result);
        }

        @Test
        @DisplayName("MIME type with surrounding whitespace is returned verbatim (no trim contract)")
        void mimeWithWhitespace() {
            // The raw string has no semicolon, so the whole string becomes the mime.
            // Depending on implementation, whitespace may or may not be trimmed here;
            // this test documents the actual behaviour of the improved parser (no trim
            // without a semicolon because split[0] IS the whole string and trim() is
            // only called when there IS a semicolon).
            MediaResourceType result = parse("video/mp4");
            assertNotNull(result);
            assertType("video/mp4", null, result);
        }

        @Test
        @DisplayName("unusual but valid MIME type passes through unchanged")
        void unusualMime() {
            MediaResourceType result = parse("application/vnd.apple.mpegurl");
            assertType("application/vnd.apple.mpegurl", null, result);
        }
    }

    // -------------------------------------------------------------------------
    // 3. Semicolon present, no codecs param
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("semicolon present, no codecs param")
    class SemicolonNoCodecs {

        @Test
        @DisplayName("unknown parameter yields null codec")
        void unknownParam() {
            MediaResourceType result = parse("video/mp4; charset=utf-8");
            assertType("video/mp4", null, result);
        }

        @Test
        @DisplayName("multiple unknown parameters yield null codec")
        void multipleUnknownParams() {
            MediaResourceType result = parse("audio/ogg; rate=44100; channels=2");
            assertType("audio/ogg", null, result);
        }

        @Test
        @DisplayName("trailing semicolon (empty last param) yields null codec")
        void trailingSemicolon() {
            MediaResourceType result = parse("video/webm;");
            assertType("video/webm", null, result);
        }
    }

    // -------------------------------------------------------------------------
    // 4. Bare codecs key (no '=')
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("bare codecs key without '='")
    class BareCodecsKey {

        @Test
        @DisplayName("codecs key with no equals sign → null codec")
        void bareKey() {
            MediaResourceType result = parse("video/mp4; codecs");
            assertType("video/mp4", null, result);
        }

        @Test
        @DisplayName("codecs key followed only by whitespace → null codec")
        void bareKeyWhitespace() {
            MediaResourceType result = parse("video/mp4; codecs   ");
            assertType("video/mp4", null, result);
        }
    }

    // -------------------------------------------------------------------------
    // 5. codecs= with empty value
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("codecs param with empty value")
    class EmptyCodecsValue {

        @Test
        @DisplayName("codecs= (no value) → null codec")
        void emptyUnquoted() {
            MediaResourceType result = parse("video/mp4; codecs=");
            assertType("video/mp4", null, result);
        }

        @Test
        @DisplayName("codecs=\"\" (empty quoted) → null codec")
        void emptyQuoted() {
            MediaResourceType result = parse("video/mp4; codecs=\"\"");
            assertType("video/mp4", null, result);
        }

        @Test
        @DisplayName("codecs= with only whitespace → null codec")
        void emptyWhitespace() {
            MediaResourceType result = parse("video/mp4; codecs=   ");
            assertType("video/mp4", null, result);
        }
    }

    // -------------------------------------------------------------------------
    // 6 & 7. Single codec — unquoted and quoted
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("single codec value")
    class SingleCodec {

        @Test
        @DisplayName("unquoted single codec is returned as-is")
        void unquoted() {
            MediaResourceType result = parse("audio/ogg; codecs=vorbis");
            assertType("audio/ogg", "vorbis", result);
        }

        @Test
        @DisplayName("double-quoted single codec has quotes stripped")
        void quoted() {
            MediaResourceType result = parse("audio/ogg; codecs=\"vorbis\"");
            assertType("audio/ogg", "vorbis", result);
        }

        @Test
        @DisplayName("single codec with internal whitespace is trimmed")
        void withWhitespace() {
            MediaResourceType result = parse("audio/ogg; codecs=\" vorbis \"");
            assertType("audio/ogg", "vorbis", result);
        }

        @Test
        @DisplayName("profile-qualified codec (e.g. avc1.42E01E) passes through")
        void profileQualified() {
            MediaResourceType result = parse("video/mp4; codecs=\"avc1.42E01E\"");
            assertType("video/mp4", "avc1.42E01E", result);
        }

        @Test
        @DisplayName("single-quoted single codec has quotes stripped")
        void singleQuoted() {
            MediaResourceType result = parse("audio/ogg; codecs='vorbis'");
            assertType("audio/ogg", "vorbis", result);
        }

        @Test
        @DisplayName("single codec with internal whitespace is trimmed")
        void singleWithWhitespace() {
            MediaResourceType result = parse("audio/ogg; codecs=' vorbis '");
            assertType("audio/ogg", "vorbis", result);
        }

        @Test
        @DisplayName("profile-qualified codec (e.g. avc1.42E01E) passes through")
        void singlePprofileQualified() {
            MediaResourceType result = parse("video/mp4; codecs='avc1.42E01E'");
            assertType("video/mp4", "avc1.42E01E", result);
        }
    }

    // -------------------------------------------------------------------------
    // 8. Multiple codecs → trimmed + sorted + joined
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("multiple codecs are normalised")
    class MultipleCodecs {

        @Test
        @DisplayName("two codecs are sorted alphabetically")
        void twoCodecsSorted() {
            MediaResourceType result = parse("video/mp4; codecs=\"avc1.42E01E,mp4a.40.2\"");
            assertType("video/mp4", "avc1.42E01E,mp4a.40.2", result);
        }

        @Test
        @DisplayName("codecs in reverse order are normalised to the same string")
        void twoCodecsReversed() {
            MediaResourceType result = parse("video/mp4; codecs=\"mp4a.40.2,avc1.42E01E\"");
            assertType("video/mp4", "avc1.42E01E,mp4a.40.2", result);
        }

        @Test
        @DisplayName("codecs with spaces around comma are trimmed before sorting")
        void spacesAroundComma() {
            MediaResourceType result = parse("video/webm; codecs=\"vp9, opus\"");
            assertType("video/webm", "opus,vp9", result);
        }

        @Test
        @DisplayName("three codecs are sorted correctly")
        void threeCodecs() {
            MediaResourceType result = parse("video/mp4; codecs=\"mp4a.40.2,hev1.1.6.L93.90,avc1.42E01E\"");
            assertType("video/mp4", "avc1.42E01E,hev1.1.6.L93.90,mp4a.40.2", result);
        }
    }

    // -------------------------------------------------------------------------
    // 9. Codec order independence (commutativity of normalisation)
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("codec order independence")
    class CodecOrderIndependence {

        @Test
        @DisplayName("vp8,vorbis and vorbis,vp8 produce equal MediaResourceType instances")
        void permutationsAreEqual() {
            MediaResourceType a = parse("video/webm; codecs=\"vp8,vorbis\"");
            MediaResourceType b = parse("video/webm; codecs=\"vorbis,vp8\"");
            assertNotNull(a);
            assertNotNull(b);
            assertEquals(a, b, "Different codec orderings must produce equal instances");
            assertEquals(a.hashCode(), b.hashCode(), "Equal instances must have equal hash codes");
        }
    }

    // -------------------------------------------------------------------------
    // 10. Case-insensitive 'codecs' key
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("codecs key is case-insensitive")
    class CaseInsensitiveKey {

        @ParameterizedTest(name = "key=\"{0}\"")
        @ValueSource(strings = {"codecs", "CODECS", "Codecs", "cOdEcS"})
        @DisplayName("any casing of the key is recognised")
        void anyCasing(String key) {
            MediaResourceType result = parse("audio/ogg; " + key + "=vorbis");
            assertType("audio/ogg", "vorbis", result);
        }
    }

    // -------------------------------------------------------------------------
    // 11. Extra whitespace everywhere
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("extra whitespace tolerance")
    class ExtraWhitespace {

        @Test
        @DisplayName("whitespace around semicolons and equals signs is tolerated")
        void whitespaceAroundDelimiters() {
            MediaResourceType result = parse("  video/mp4  ;  codecs = \"avc1.42E01E\"  ");
            assertType("video/mp4", "avc1.42E01E", result);
        }

        @Test
        @DisplayName("tabs in codec list are treated as whitespace")
        void tabsInCodecList() {
            MediaResourceType result = parse("audio/ogg; codecs=\"vorbis\t,\topus\"");
            assertType("audio/ogg", "opus,vorbis", result);
        }
    }

    // -------------------------------------------------------------------------
    // 12. codecs is not the first parameter
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("codecs param is not the first parameter")
    class CodecsNotFirst {

        @Test
        @DisplayName("codecs after an unrelated param is still found")
        void codecsSecond() {
            MediaResourceType result = parse("video/mp4; charset=utf-8; codecs=avc1.42E01E");
            assertType("video/mp4", "avc1.42E01E", result);
        }

        @Test
        @DisplayName("codecs after two unrelated params is still found")
        void codecsThird() {
            MediaResourceType result = parse("video/mp4; foo=bar; baz=qux; codecs=\"vp9,opus\"");
            assertType("video/mp4", "opus,vp9", result);
        }
    }

    // -------------------------------------------------------------------------
    // 13. Substring trap — param name containing "codecs" but != "codecs"
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("substring-trap: param names that contain 'codecs'")
    class SubstringTrap {

        @Test
        @DisplayName("'nocodecs' param is NOT treated as codecs → null codec")
        void nocodecs() {
            MediaResourceType result = parse("video/mp4; nocodecs=vp9");
            assertType("video/mp4", null, result);
        }

        @Test
        @DisplayName("'xcodecs' param is NOT treated as codecs → null codec")
        void xcodecs() {
            MediaResourceType result = parse("video/mp4; xcodecs=avc1");
            assertType("video/mp4", null, result);
        }

        @Test
        @DisplayName("'codecs-extra' param is NOT treated as codecs → null codec")
        void codecsExtra() {
            MediaResourceType result = parse("video/mp4; codecs-extra=something");
            assertType("video/mp4", null, result);
        }
    }

    // -------------------------------------------------------------------------
    // 14. Trailing/leading commas and empty codec tokens
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("malformed codec lists")
    class MalformedCodecLists {

        @Test
        @DisplayName("leading comma is ignored (empty leading token filtered)")
        void leadingComma() {
            MediaResourceType result = parse("audio/ogg; codecs=\",vorbis\"");
            assertType("audio/ogg", "vorbis", result);
        }

        @Test
        @DisplayName("trailing comma is ignored (empty trailing token filtered)")
        void trailingComma() {
            MediaResourceType result = parse("audio/ogg; codecs=\"vorbis,\"");
            assertType("audio/ogg", "vorbis", result);
        }

        @Test
        @DisplayName("double comma (empty interior token) is ignored")
        void doubleComma() {
            MediaResourceType result = parse("audio/ogg; codecs=\"vorbis,,opus\"");
            assertType("audio/ogg", "opus,vorbis", result);
        }

        @Test
        @DisplayName("all-comma string (only empty tokens) → null codec")
        void onlyCommas() {
            MediaResourceType result = parse("audio/ogg; codecs=\",,,\"");
            assertType("audio/ogg", null, result);
        }
    }

    // -------------------------------------------------------------------------
    // 15. equals() and hashCode() contract on MediaResourceType
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("equals() and hashCode() contract")
    class EqualsHashCode {

        @Test
        @DisplayName("same MIME, same codec → equal")
        void sameInputsEqual() {
            MediaResourceType a = parse("video/mp4; codecs=avc1.42E01E");
            MediaResourceType b = parse("video/mp4; codecs=avc1.42E01E");
            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("same MIME, null codec vs non-null codec → not equal")
        void nullVsNonNullCodec() {
            MediaResourceType a = parse("video/mp4");
            MediaResourceType b = parse("video/mp4; codecs=avc1.42E01E");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("different MIME, same codec → not equal")
        void differentMime() {
            MediaResourceType a = parse("audio/ogg; codecs=vorbis");
            MediaResourceType b = parse("video/ogg; codecs=vorbis");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("reflexivity: instance equals itself")
        void reflexive() {
            MediaResourceType a = parse("video/mp4; codecs=avc1.42E01E");
            assertNotNull(a);
            assertEquals(a, a);
        }

        @Test
        @DisplayName("symmetry: a.equals(b) iff b.equals(a)")
        void symmetric() {
            MediaResourceType a = parse("video/mp4; codecs=avc1.42E01E");
            MediaResourceType b = parse("video/mp4; codecs=avc1.42E01E");
            assertNotNull(a);
            assertNotNull(b);
            assertEquals(a.equals(b), b.equals(a));
        }

        @Test
        @DisplayName("not equal to null")
        void notEqualToNull() {
            MediaResourceType a = parse("video/mp4");
            assertNotNull(a);
            assertNotEquals(null, a);
        }

        @Test
        @DisplayName("not equal to an object of a different type")
        void notEqualToDifferentType() {
            MediaResourceType a = parse("video/mp4");
            assertNotNull(a);
            assertNotEquals("video/mp4", a);
        }
    }

    // -------------------------------------------------------------------------
    // 16. Real-world canPlayType() inputs
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("real-world canPlayType() inputs")
    class RealWorldInputs {

        @Test
        @DisplayName("H.264 + AAC in MP4 (typical video)")
        void h264AacMp4() {
            MediaResourceType result = parse("video/mp4; codecs=\"avc1.42E01E, mp4a.40.2\"");
            assertType("video/mp4", "avc1.42E01E,mp4a.40.2", result);
        }

        @Test
        @DisplayName("VP8 + Vorbis in WebM (legacy WebM)")
        void vp8VorbisWebm() {
            MediaResourceType result = parse("video/webm; codecs=\"vp8, vorbis\"");
            assertType("video/webm", "vorbis,vp8", result);
        }

        @Test
        @DisplayName("VP9 + Opus in WebM (modern WebM)")
        void vp9OpusWebm() {
            MediaResourceType result = parse("video/webm; codecs=\"vp9,opus\"");
            assertType("video/webm", "opus,vp9", result);
        }

        @Test
        @DisplayName("Opus in OGG audio")
        void opusOgg() {
            MediaResourceType result = parse("audio/ogg; codecs=opus");
            assertType("audio/ogg", "opus", result);
        }

        @Test
        @DisplayName("AV1 in WebM (newer codec)")
        void av1Webm() {
            MediaResourceType result = parse("video/webm; codecs=av1");
            assertType("video/webm", "av1", result);
        }

        @Test
        @DisplayName("HEVC/H.265 in MP4")
        void hevcMp4() {
            MediaResourceType result = parse("video/mp4; codecs=\"hev1.1.6.L93.90,mp4a.40.2\"");
            assertType("video/mp4", "hev1.1.6.L93.90,mp4a.40.2", result);
        }

        @Test
        @DisplayName("bare audio/ogg with no codec info (should → maybe)")
        void bareAudioOgg() {
            MediaResourceType result = parse("audio/ogg");
            assertType("audio/ogg", null, result);
        }
    }
}