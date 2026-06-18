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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code canPlayType()} covering known MIME types and codec
 * combinations across video, audio, and container categories.
 *
 * @author Ronald Brill
 */
public class HTMLMediaElementCanPlayTypeTest extends WebDriverTestCase {

    // -------------------------------------------------------------------------
    // video/mp4
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_VideoMp4() throws Exception {
        canPlayType("video/mp4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_H264Baseline_AAC() throws Exception {
        canPlayType("video/mp4; codecs=\"avc1.42E01E, mp4a.40.2\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_H264Baseline() throws Exception {
        canPlayType("video/mp4; codecs=\"avc1.42E01E\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_H264Main_AAC() throws Exception {
        canPlayType("video/mp4; codecs=\"avc1.4D401E, mp4a.40.2\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_H264Main() throws Exception {
        canPlayType("video/mp4; codecs=\"avc1.4D401E\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_H264High_AAC() throws Exception {
        canPlayType("video/mp4; codecs=\"avc1.640028, mp4a.40.2\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_H264High() throws Exception {
        canPlayType("video/mp4; codecs=\"avc1.640028\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_AAC() throws Exception {
        canPlayType("video/mp4; codecs=\"mp4a.40.2\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_HEVC_hvc1_AAC() throws Exception {
        canPlayType("video/mp4; codecs=\"hvc1.1.6.L93.90, mp4a.40.2\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_HEVC_hvc1() throws Exception {
        canPlayType("video/mp4; codecs=\"hvc1.1.6.L93.90\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_HEVC_hev1_AAC() throws Exception {
        canPlayType("video/mp4; codecs=\"hev1.1.6.L93.90, mp4a.40.2\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoMp4_HEVC_hev1() throws Exception {
        canPlayType("video/mp4; codecs=\"hev1.1.6.L93.90\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"probably", "probably"})
    public void canPlayType_VideoMp4_AV1_AAC() throws Exception {
        canPlayType("video/mp4; codecs=\"av01.0.01M.08, mp4a.40.2\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"probably", "probably"})
    public void canPlayType_VideoMp4_AV1() throws Exception {
        canPlayType("video/mp4; codecs=\"av01.0.01M.08\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_VideoMp4_Mpeg4Visual() throws Exception {
        canPlayType("video/mp4; codecs=\"mp4v.20.8\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            EDGE = {"probably", "probably"})
    public void canPlayType_VideoMp4_AC3() throws Exception {
        canPlayType("video/mp4; codecs=\"ac-3\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            EDGE = {"probably", "probably"})
    public void canPlayType_VideoMp4_EAC3() throws Exception {
        canPlayType("video/mp4; codecs=\"ec-3\"");
    }

    // -------------------------------------------------------------------------
    // video/webm
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_VideoWebm() throws Exception {
        canPlayType("video/webm");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoWebm_VP8_Vorbis() throws Exception {
        canPlayType("video/webm; codecs=\"vp8, vorbis\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoWebm_VP8() throws Exception {
        canPlayType("video/webm; codecs=\"vp8\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoWebm_VP9_Opus() throws Exception {
        canPlayType("video/webm; codecs=\"vp9, opus\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoWebm_VP9() throws Exception {
        canPlayType("video/webm; codecs=\"vp9\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            FF = {"probably", "probably"},
            FF_ESR = {"probably", "probably"})
    public void canPlayType_VideoWebm_AV1_Opus() throws Exception {
        canPlayType("video/webm; codecs=\"av1, opus\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            FF = {"probably", "probably"},
            FF_ESR = {"probably", "probably"})
    public void canPlayType_VideoWebm_AV1() throws Exception {
        canPlayType("video/webm; codecs=\"av1\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoWebm_Vorbis() throws Exception {
        canPlayType("video/webm; codecs=\"vorbis\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_VideoWebm_Opus() throws Exception {
        canPlayType("video/webm; codecs=\"opus\"");
    }

    // -------------------------------------------------------------------------
    // video/ogg
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"maybe", "maybe"},
            FF = {"", ""},
            FF_ESR = {"", ""})
    public void canPlayType_VideoOgg() throws Exception {
        canPlayType("video/ogg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_VideoOgg_Theora_Vorbis() throws Exception {
        canPlayType("video/ogg; codecs=\"theora, vorbis\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_VideoOgg_Theora() throws Exception {
        canPlayType("video/ogg; codecs=\"theora\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_VideoOgg_Theora_Opus() throws Exception {
        canPlayType("video/ogg; codecs=\"theora, opus\"");
    }

    // -------------------------------------------------------------------------
    // video/3gpp
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            CHROME = {"maybe", "maybe"},
            EDGE = {"maybe", "maybe"})
    public void canPlayType_Video3gpp() throws Exception {
        canPlayType("video/3gpp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_Video3gpp_Mpeg4Visual_AMR() throws Exception {
        canPlayType("video/3gpp; codecs=\"mp4v.20.8, samr\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"probably", "probably"},
            FF = {"", ""},
            FF_ESR = {"", ""})
    public void canPlayType_Video3gpp_H264_AAC() throws Exception {
        canPlayType("video/3gpp; codecs=\"avc1.42E01E, mp4a.40.2\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_Video3gpp2() throws Exception {
        canPlayType("video/3gpp2");
    }

    // -------------------------------------------------------------------------
    // video/quicktime, video/x-matroska, video/x-flv, video/mpeg
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            FF = {"maybe", "maybe"},
            FF_ESR = {"maybe", "maybe"})
    public void canPlayType_VideoQuicktime() throws Exception {
        canPlayType("video/quicktime");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"maybe", "maybe"},
            FF_ESR = {"", ""})
    public void canPlayType_VideoXMatroska() throws Exception {
        canPlayType("video/x-matroska");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"maybe", "maybe"},
            FF = {"", ""},
            FF_ESR = {"", ""})
    public void canPlayType_VideoXMatroska_H264_Vorbis() throws Exception {
        canPlayType("video/x-matroska; codecs=\"avc1, vorbis\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_VideoXFlv() throws Exception {
        canPlayType("video/x-flv");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_VideoMpeg() throws Exception {
        canPlayType("video/mpeg");
    }

    // -------------------------------------------------------------------------
    // audio/mpeg
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"maybe", "maybe"},
            CHROME = {"probably", "probably"},
            EDGE = {"probably", "probably"})
    public void canPlayType_AudioMpeg() throws Exception {
        canPlayType("audio/mpeg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_AudioMpeg_Mp3() throws Exception {
        canPlayType("audio/mpeg; codecs=\"mp3\"");
    }

    // -------------------------------------------------------------------------
    // audio/mp4
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_AudioMp4() throws Exception {
        canPlayType("audio/mp4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_AudioMp4_AacLc() throws Exception {
        canPlayType("audio/mp4; codecs=\"mp4a.40.2\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_AudioMp4_HeAac() throws Exception {
        canPlayType("audio/mp4; codecs=\"mp4a.40.5\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_AudioMp4_HeAacV2() throws Exception {
        canPlayType("audio/mp4; codecs=\"mp4a.40.29\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"probably", "probably"},
            FF = {"", ""},
            FF_ESR = {"", ""})
    public void canPlayType_AudioMp4_Mp3() throws Exception {
        canPlayType("audio/mp4; codecs=\"mp4a.69\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            EDGE = {"probably", "probably"})
    public void canPlayType_AudioMp4_AC3() throws Exception {
        canPlayType("audio/mp4; codecs=\"ac-3\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            EDGE = {"probably", "probably"})
    public void canPlayType_AudioMp4_EAC3() throws Exception {
        canPlayType("audio/mp4; codecs=\"ec-3\"");
    }

    // -------------------------------------------------------------------------
    // audio/aac, audio/x-aac, audio/x-m4a
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"probably", "probably"},
            FF = {"maybe", "maybe"},
            FF_ESR = {"maybe", "maybe"})
    public void canPlayType_AudioAac() throws Exception {
        canPlayType("audio/aac");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            FF = {"maybe", "maybe"},
            FF_ESR = {"maybe", "maybe"})
    public void canPlayType_AudioXAac() throws Exception {
        canPlayType("audio/x-aac");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_AudioXM4a() throws Exception {
        canPlayType("audio/x-m4a");
    }

    // -------------------------------------------------------------------------
    // audio/ogg
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_AudioOgg() throws Exception {
        canPlayType("audio/ogg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_AudioOgg_Vorbis() throws Exception {
        canPlayType("audio/ogg; codecs=\"vorbis\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_AudioOgg_Opus() throws Exception {
        canPlayType("audio/ogg; codecs=\"opus\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_AudioOgg_Flac() throws Exception {
        canPlayType("audio/ogg; codecs=\"flac\"");
    }

    // -------------------------------------------------------------------------
    // audio/webm
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_AudioWebm() throws Exception {
        canPlayType("audio/webm");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_AudioWebm_Vorbis() throws Exception {
        canPlayType("audio/webm; codecs=\"vorbis\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_AudioWebm_Opus() throws Exception {
        canPlayType("audio/webm; codecs=\"opus\"");
    }

    // -------------------------------------------------------------------------
    // audio/wav, audio/wave, audio/x-wav, audio/x-pn-wav
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_AudioWav() throws Exception {
        canPlayType("audio/wav");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"probably", "probably"})
    public void canPlayType_AudioWav_PcmCodec() throws Exception {
        canPlayType("audio/wav; codecs=1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            FF = {"maybe", "maybe"},
            FF_ESR = {"maybe", "maybe"})
    public void canPlayType_AudioWave() throws Exception {
        canPlayType("audio/wave");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_AudioXWav() throws Exception {
        canPlayType("audio/x-wav");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            FF = {"maybe", "maybe"},
            FF_ESR = {"maybe", "maybe"})
    public void canPlayType_AudioXPnWav() throws Exception {
        canPlayType("audio/x-pn-wav");
    }

    // -------------------------------------------------------------------------
    // audio/flac, audio/x-flac
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"probably", "probably"},
            FF = {"maybe", "maybe"},
            FF_ESR = {"maybe", "maybe"})
    public void canPlayType_AudioFlac() throws Exception {
        canPlayType("audio/flac");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            FF = {"maybe", "maybe"},
            FF_ESR = {"maybe", "maybe"})
    public void canPlayType_AudioXFlac() throws Exception {
        canPlayType("audio/x-flac");
    }

    // -------------------------------------------------------------------------
    // audio/3gpp, audio/3gpp2
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            EDGE = {"maybe", "maybe"})
    public void canPlayType_Audio3gpp() throws Exception {
        canPlayType("audio/3gpp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_Audio3gpp_AMR() throws Exception {
        canPlayType("audio/3gpp; codecs=\"samr\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_Audio3gpp2() throws Exception {
        canPlayType("audio/3gpp2");
    }

    // -------------------------------------------------------------------------
    // audio/x-ms-wma, audio/x-ms-wmv (legacy Windows Media)
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_AudioXMsWma() throws Exception {
        canPlayType("audio/x-ms-wma");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_AudioXMsWmv() throws Exception {
        canPlayType("audio/x-ms-wmv");
    }

    // -------------------------------------------------------------------------
    // application/ogg
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_ApplicationOgg() throws Exception {
        canPlayType("application/ogg");
    }

    // -------------------------------------------------------------------------
    // HLS streaming
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            CHROME = {"maybe", "maybe"},
            EDGE = {"maybe", "maybe"})
    public void canPlayType_ApplicationXMpegURL() throws Exception {
        canPlayType("application/x-mpegURL");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            CHROME = {"maybe", "maybe"},
            EDGE = {"maybe", "maybe"})
    public void canPlayType_ApplicationVndAppleMpegurl() throws Exception {
        canPlayType("application/vnd.apple.mpegurl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_VideoMp2t() throws Exception {
        canPlayType("video/mp2t");
    }

    // -------------------------------------------------------------------------
    // MPEG-DASH
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_ApplicationDashXml() throws Exception {
        canPlayType("application/dash+xml");
    }

    // -------------------------------------------------------------------------
    // Border cases
    // -------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_applicationoctetstream() throws Exception {
        canPlayType("application/octet-stream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_empty() throws Exception {
        canPlayType("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_blank() throws Exception {
        canPlayType(" ");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_unknown() throws Exception {
        canPlayType("unknown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_invalidMimeNoSubtype() throws Exception {
        canPlayType("audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_invalidMimeSlashOnly() throws Exception {
        canPlayType("/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_invalidMimeLeadingSlash() throws Exception {
        canPlayType("/mp4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_invalidMimeTrailingSlash() throws Exception {
        canPlayType("video/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_invalidMimeDoubleSlash() throws Exception {
        canPlayType("video//mp4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_unknownType() throws Exception {
        canPlayType("foo/bar");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_unknownAudioSubtype() throws Exception {
        canPlayType("audio/xyz");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_unknownVideoSubtype() throws Exception {
        canPlayType("video/xyz");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_validMimeUnknownCodec() throws Exception {
        canPlayType("video/mp4; codecs=\"unknown\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_validMimeEmptyCodec() throws Exception {
        canPlayType("video/mp4; codecs=\"\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_validMimeMalformedCodecsParam() throws Exception {
        canPlayType("video/mp4; codecs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"maybe", "maybe"})
    public void canPlayType_validMimeRandomParam() throws Exception {
        canPlayType("video/mp4; foo=bar");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_numeric() throws Exception {
        canPlayTypeExpr("42");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_zero() throws Exception {
        canPlayTypeExpr("0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_null() throws Exception {
        canPlayTypeExpr("null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_undefined() throws Exception {
        canPlayTypeExpr("undefined");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_true() throws Exception {
        canPlayTypeExpr("true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_false() throws Exception {
        canPlayTypeExpr("false");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_emptyArray() throws Exception {
        canPlayTypeExpr("[]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ""})
    public void canPlayType_object() throws Exception {
        canPlayTypeExpr("{}");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "TypeError"})
    public void canPlayType_noArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <audio id='a1'/>\n"
            + "  <video id='v1'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    var audio = document.getElementById('a1');\n"
            + "    log(audio.canPlayType());\n"
            + "  } catch(e) { logEx(e); }\n"
            + "  try {\n"
            + "    var video = document.getElementById('v1');\n"
            + "    log(video.canPlayType());\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    private void canPlayType(final String mimeType) throws Exception {
        canPlayTypeExpr("'" + mimeType + "'");
    }

    private void canPlayTypeExpr(final String jsExpr) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <audio id='a1'/>\n"
            + "  <video id='v1'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    var audio = document.getElementById('a1');\n"
            + "      log(audio.canPlayType(" + jsExpr + "));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "  try {\n"
            + "    var video = document.getElementById('v1');\n"
            + "      log(video.canPlayType(" + jsExpr + "));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}