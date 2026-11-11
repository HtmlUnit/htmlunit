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

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the Content-Security-Policy directive media-src (audio, video, source and track elements).
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and collects all securitypolicyviolation events. The function track(id) collects the
 * loadedmetadata, load and error events of an element. The events (sorted) and the violations
 * are logged at the end (300ms after the load event) because the browsers fire them async.
 * The test code is placed in the external script 'test.js'. All policies allow these scripts.
 * The media resource is a tiny wav file (delivered with Cache-Control: no-store).
 * </p>
 * <p>
 * HtmlUnit does not load media; therefore there are no loadedmetadata events (HtmlUnitNYI).
 * Firefox does not block (and report) the media in some cases; HtmlUnit follows Chrome.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyMediaTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "var events = [];\n"
            + "var violations = [];\n"
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  violations.push('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n"
            + "function track(id) {\n"
            + "  var el = typeof id == 'string' ? document.getElementById(id) : id;\n"
            + "  var name = el.id;\n"
            + "  ['loadedmetadata', 'load', 'error'].forEach(function(type) {\n"
            + "    el.addEventListener(type, function() { events.push(type + ' ' + name); });\n"
            + "  });\n"
            + "}\n"
            + "window.addEventListener('load', function() {\n"
            + "  setTimeout(function() {\n"
            + "    events.sort();\n"
            + "    for (var i = 0; i < events.length; i++) { log(events[i]); }\n"
            + "    for (var i = 0; i < violations.length; i++) { log(violations[i]); }\n"
            + "    log('done');\n"
            + "  }, 300);\n"
            + "});\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Without a policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loadedmetadata a", "done"})
    @HtmlUnitNYI(CHROME = {"done"},
            EDGE = {"done"},
            FF = {"done"},
            FF_ESR = {"done"})
    public void noPolicy() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(null, audio("a", URL_CROSS_ORIGIN_BASE + "audio.wav"), "track('a');\n");
    }

    /**
     * The directive media-src 'none' blocks all media.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error a", "violation media-src §§URL§§/audio.wav enforce", "done"})
    public void none() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'none'", audio("a", "audio.wav"), "track('a');\n");
    }

    /**
     * Same origin media, media-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loadedmetadata a", "done"})
    @HtmlUnitNYI(CHROME = {"done"},
            EDGE = {"done"},
            FF = {"done"},
            FF_ESR = {"done"})
    public void self_sameOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'self'", audio("a", "audio.wav"), "track('a');\n");
    }

    /**
     * Cross origin media, media-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF = {"loadedmetadata a", "done"},
            FF_ESR = {"loadedmetadata a", "done"})
    @HtmlUnitNYI(FF = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF_ESR = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"})
    public void self_crossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'self'", audio("a", URL_CROSS_ORIGIN_BASE + "audio.wav"), "track('a');\n");
    }

    /**
     * Media-src falls back to default-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"})
    public void defaultSrcFallback() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("default-src 'self'", audio("a", URL_CROSS_ORIGIN_BASE + "audio.wav"), "track('a');\n");
    }

    /**
     * Video element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error v", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF = {"loadedmetadata v", "done"},
            FF_ESR = {"loadedmetadata v", "done"})
    @HtmlUnitNYI(FF = {"error v", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF_ESR = {"error v", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"})
    public void video() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'self'",
                "<video id='v' preload='auto' src='" + URL_CROSS_ORIGIN_BASE + "audio.wav'></video>\n",
                "track('v');\n");
    }

    /**
     * Source elements; the first one is blocked, the second one is used.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error s1",
                       "loadedmetadata a",
                       "violation media-src http://localhost:§§PORT§§/audio.wav enforce",
                       "done"},
            FF = {"loadedmetadata a", "done"},
            FF_ESR = {"loadedmetadata a", "done"})
    @HtmlUnitNYI(CHROME = {"error s1", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            EDGE = {"error s1", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF = {"error s1", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF_ESR = {"error s1", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"})
    public void sources() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'self'",
                "<audio id='a' preload='auto'>\n"
                + "  <source id='s1' src='" + URL_CROSS_ORIGIN_BASE + "audio.wav' type='audio/wav'>\n"
                + "  <source id='s2' src='audio.wav' type='audio/wav'>\n"
                + "</audio>\n",
                "track('a');\ntrack('s1');\ntrack('s2');\n");
    }

    /**
     * Source elements, all blocked.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error s1",
                       "error s2",
                       "violation media-src §§URL§§/audio.wav enforce",
                       "violation media-src http://localhost:§§PORT§§/audio.wav enforce",
                       "done"},
            FF = {"loadedmetadata a", "done"},
            FF_ESR = {"loadedmetadata a", "done"})
    @HtmlUnitNYI(FF = {"error s1",
                       "error s2",
                       "violation media-src §§URL§§/audio.wav enforce",
                       "violation media-src http://localhost:§§PORT§§/audio.wav enforce",
                       "done"},
            FF_ESR = {"error s1",
                      "error s2",
                      "violation media-src §§URL§§/audio.wav enforce",
                      "violation media-src http://localhost:§§PORT§§/audio.wav enforce",
                      "done"})
    public void sourcesAllBlocked() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'none'",
                "<audio id='a' preload='auto'>\n"
                + "  <source id='s1' src='audio.wav' type='audio/wav'>\n"
                + "  <source id='s2' src='" + URL_CROSS_ORIGIN_BASE + "audio.wav' type='audio/wav'>\n"
                + "</audio>\n",
                "track('a');\ntrack('s1');\ntrack('s2');\n");
    }

    /**
     * Track element (default track).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error t",
                       "loadedmetadata v",
                       "violation media-src http://localhost:§§PORT§§/track.vtt enforce",
                       "done"},
            FF = {"error t", "loadedmetadata v", "done"},
            FF_ESR = {"error t", "loadedmetadata v", "done"})
    @HtmlUnitNYI(CHROME = {"error t", "violation media-src http://localhost:§§PORT§§/track.vtt enforce", "done"},
            EDGE = {"error t", "violation media-src http://localhost:§§PORT§§/track.vtt enforce", "done"},
            FF = {"error t", "violation media-src http://localhost:§§PORT§§/track.vtt enforce", "done"},
            FF_ESR = {"error t", "violation media-src http://localhost:§§PORT§§/track.vtt enforce", "done"})
    public void track() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'self'",
                "<video id='v' preload='auto' src='audio.wav'>\n"
                + "  <track id='t' default kind='subtitles' src='" + URL_CROSS_ORIGIN_BASE + "track.vtt'>\n"
                + "</video>\n",
                "track('v');\ntrack('t');\n");
    }

    /**
     * Media element created by script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"})
    public void dynamic() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'self'", "",
                "var a = document.createElement('audio');\n"
                + "a.id = 'a';\n"
                + "a.preload = 'auto';\n"
                + "track(a);\n"
                + "a.src = '" + URL_CROSS_ORIGIN_BASE + "audio.wav';\n"
                + "document.body.appendChild(a);\n");
    }

    /**
     * Media element created by script but not added to the document.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"})
    public void detached() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'self'", "",
                "var a = document.createElement('audio');\n"
                + "a.id = 'a';\n"
                + "a.preload = 'auto';\n"
                + "track(a);\n"
                + "a.src = '" + URL_CROSS_ORIGIN_BASE + "audio.wav';\n");
    }

    /**
     * The src of an allowed media element is changed to a blocked url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF = {"loadedmetadata a", "done"},
            FF_ESR = {"loadedmetadata a", "done"})
    @HtmlUnitNYI(FF = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF_ESR = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"})
    public void srcChanged() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'self'", audio("a", "audio.wav"),
                "track('a');\n"
                + "document.getElementById('a').src = '" + URL_CROSS_ORIGIN_BASE + "audio.wav';\n");
    }

    /**
     * Preload none; the browsers do not load the media.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF = "done",
            FF_ESR = "done")
    @HtmlUnitNYI(FF = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF_ESR = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"})
    public void preloadNone() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("media-src 'self'",
                "<audio id='a' preload='none' src='" + URL_CROSS_ORIGIN_BASE + "audio.wav'></audio>\n",
                "track('a');\n");
    }

    /**
     * Report only policies do not block the media.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"loadedmetadata a",
                       "violation media-src http://localhost:§§PORT§§/audio.wav report",
                       "violation media-src http://localhost:§§PORT§§/audio.wav report",
                       "done"},
            FF = {"loadedmetadata a", "done"},
            FF_ESR = {"loadedmetadata a", "done"})
    @HtmlUnitNYI(CHROME = {"violation media-src http://localhost:§§PORT§§/audio.wav report", "done"},
            EDGE = {"violation media-src http://localhost:§§PORT§§/audio.wav report", "done"},
            FF = {"violation media-src http://localhost:§§PORT§§/audio.wav report", "done"},
            FF_ESR = {"violation media-src http://localhost:§§PORT§§/audio.wav report", "done"})
    public void reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HEADER_REPORT_ONLY, "media-src 'self'",
                audio("a", URL_CROSS_ORIGIN_BASE + "audio.wav"), "track('a');\n");
    }

    /**
     * The policy can be delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            FF_ESR = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"})
    @HtmlUnitNYI(CHROME = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"},
            EDGE = {"error a", "violation media-src http://localhost:§§PORT§§/audio.wav enforce", "done"})
    public void meta() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"media-src 'self'\">\n",
                audio("a", URL_CROSS_ORIGIN_BASE + "audio.wav"), "track('a');\n");
    }

    private static String audio(final String id, final String src) {
        return "<audio id='" + id + "' preload='auto' src='" + src + "'></audio>\n";
    }

    /**
     * A tiny wav file (8kHz, 8 bit, mono, 0.1s silence).
     */
    private static byte[] wav() {
        final int samples = 800;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeString(out, "RIFF");
        writeInt(out, 36 + samples);
        writeString(out, "WAVE");
        writeString(out, "fmt ");
        writeInt(out, 16);
        writeShort(out, 1);
        writeShort(out, 1);
        writeInt(out, 8000);
        writeInt(out, 8000);
        writeShort(out, 1);
        writeShort(out, 8);
        writeString(out, "data");
        writeInt(out, samples);
        for (int i = 0; i < samples; i++) {
            out.write(128);
        }
        return out.toByteArray();
    }

    private static void writeString(final ByteArrayOutputStream out, final String value) {
        for (int i = 0; i < value.length(); i++) {
            out.write(value.charAt(i));
        }
    }

    private static void writeInt(final ByteArrayOutputStream out, final int value) {
        out.write(value & 0xFF);
        out.write((value >> 8) & 0xFF);
        out.write((value >> 16) & 0xFF);
        out.write((value >> 24) & 0xFF);
    }

    private static void writeShort(final ByteArrayOutputStream out, final int value) {
        out.write(value & 0xFF);
        out.write((value >> 8) & 0xFF);
    }

    private void loadPageVerify(final String policy, final String body, final String testJs) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, "", body, testJs);
    }

    private void loadPageVerify(final String header, final String policy,
            final String body, final String testJs) throws Exception {
        loadPageVerify(header, policy, "", body, testJs);
    }

    /**
     * Loads a page containing the harness script, the given head and body and the test script;
     * the page is delivered with the given policy header.
     */
    private void loadPageVerify(final String header, final String policy,
            final String head, final String body, final String testJs) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + head
            + "<script src='" + URL_FIRST + "harness.js'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + body
            + "<script src='" + URL_FIRST + "test.js'></script>\n"
            + "</body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        if (policy != null) {
            headers.add(new NameValuePair(header, policy));
        }
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, headers);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "harness.js"), HARNESS_JS,
                MimeType.TEXT_JAVASCRIPT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"), testJs,
                MimeType.TEXT_JAVASCRIPT);

        // no caching; the browsers may use cached media without checking the policy
        final List<NameValuePair> noStore = Collections.singletonList(
                new NameValuePair(HttpHeader.CACHE_CONTROL, "no-store"));
        final byte[] wav = wav();
        getMockWebConnection().setResponse(new URL(URL_FIRST, "audio.wav"), wav, 200, "OK", "audio/wav",
                noStore);
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "audio.wav"), wav, 200, "OK",
                "audio/wav", noStore);
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "track.vtt"), "WEBVTT\n",
                200, "OK", "text/vtt", noStore);

        final WebDriver driver = loadPage2(URL_FIRST, ISO_8859_1);
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
