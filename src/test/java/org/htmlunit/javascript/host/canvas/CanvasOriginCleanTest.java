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
package org.htmlunit.javascript.host.canvas;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;

/**
 * Tests for the origin-clean flag of the canvas (canvas tainting); the content of a canvas
 * with cross-origin image data can't be read back by script.
 *
 * @author Ronald Brill
 * @see <a href="https://html.spec.whatwg.org/multipage/canvas.html#security-with-canvas-elements">
 *     HTML Living Standard - Security with canvas elements</a>
 */
public class CanvasOriginCleanTest extends WebDriverTestCase {

    // waits for the image, draws it and tries to read the canvas back
    private static final String DRAW_JS =
            "function readBack(canvas) {\n"
            + "  var ctx = canvas.getContext('2d');\n"
            + "  try {\n"
            + "    ctx.getImageData(0, 0, 1, 1);\n"
            + "    log('getImageData ok');\n"
            + "  } catch(e) { log('getImageData ' + e.name); }\n"
            + "  try {\n"
            + "    canvas.toDataURL();\n"
            + "    log('toDataURL ok');\n"
            + "  } catch(e) { log('toDataURL ' + e.name); }\n"
            + "}\n"
            + "function draw(img) {\n"
            + "  var canvas = document.getElementById('c');\n"
            + "  canvas.getContext('2d').drawImage(img, 0, 0);\n"
            + "  readBack(canvas);\n"
            + "  return canvas;\n"
            + "}\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * An image of the same origin does not taint the canvas.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData ok", "toDataURL ok"})
    public void sameOrigin() throws Exception {
        setImage(URL_FIRST, "img.png");
        loadPageVerify("", "img.png", "draw(img);\n");
    }

    /**
     * An image of another origin taints the canvas.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData SecurityError", "toDataURL SecurityError"})
    public void crossOrigin() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "img.png", "draw(img);\n");
    }

    /**
     * An image of the same origin redirecting to another origin taints the canvas.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData SecurityError", "toDataURL SecurityError"})
    public void redirectToCrossOrigin() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        setRedirect(new URL(URL_FIRST, "redirect"), new URL(URL_CROSS_ORIGIN_BASE, "img.png"));
        loadPageVerify("", "redirect", "draw(img);\n");
    }

    /**
     * An image of another origin redirecting back to the origin of the page taints the canvas
     * (the response tainting stays opaque).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData SecurityError", "toDataURL SecurityError"})
    public void redirectFromCrossOriginToSameOrigin() throws Exception {
        setImage(URL_FIRST, "img.png");
        setRedirect(new URL(URL_CROSS_ORIGIN_BASE, "redirect"), new URL(URL_FIRST, "img.png"));
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "redirect", "draw(img);\n");
    }

    /**
     * A redirect chain from the origin of the page over another origin back to the origin
     * of the page taints the canvas (the response tainting stays opaque); only the redirect
     * filter of HtmlImage sees the url in the middle.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData SecurityError", "toDataURL SecurityError"})
    public void redirectOverCrossOriginBackToSameOrigin() throws Exception {
        setImage(URL_FIRST, "img.png");
        setRedirect(new URL(URL_FIRST, "redirect"), new URL(URL_CROSS_ORIGIN_BASE, "redirect"));
        setRedirect(new URL(URL_CROSS_ORIGIN_BASE, "redirect"), new URL(URL_FIRST, "img.png"));
        loadPageVerify("", "redirect", "draw(img);\n");
    }

    /**
     * A redirect inside the origin of the page does not taint the canvas.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData ok", "toDataURL ok"})
    public void redirectSameOrigin() throws Exception {
        setImage(URL_FIRST, "img.png");
        setRedirect(new URL(URL_FIRST, "redirect"), new URL(URL_FIRST, "img.png"));
        loadPageVerify("", "redirect", "draw(img);\n");
    }

    /**
     * With a Content-Security-Policy the redirect filter of HtmlImage checks the policy
     * and records the cross-origin redirect.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData SecurityError", "toDataURL SecurityError"})
    public void redirectOverCrossOriginBackToSameOriginWithPolicy() throws Exception {
        setImage(URL_FIRST, "img.png");
        setRedirect(new URL(URL_FIRST, "redirect"), new URL(URL_CROSS_ORIGIN_BASE, "redirect"));
        setRedirect(new URL(URL_CROSS_ORIGIN_BASE, "redirect"), new URL(URL_FIRST, "img.png"));
        loadPageVerify("<meta http-equiv='Content-Security-Policy' "
                    + "content=\"img-src 'self' http://localhost:" + PORT2 + "\">\n",
                "", "redirect", "draw(img);\n");
    }

    /**
     * A new src of the image starts a new download; the state of the previous
     * (cross-origin) image does not taint a canvas the new (same-origin) image is drawn to.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData SecurityError", "toDataURL SecurityError",
             "load", "getImageData ok", "toDataURL ok"})
    public void newSrcResetsCrossOrigin() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        setImage(URL_FIRST, "img.png");
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "img.png",
                "if (img.src.indexOf('" + URL_CROSS_ORIGIN_BASE + "') == 0) {\n"
                + "  draw(img);\n"
                + "  img.src = 'img.png';\n"
                + "} else {\n"
                + "  var other = document.createElement('canvas');\n"
                + "  other.getContext('2d').drawImage(img, 0, 0);\n"
                + "  readBack(other);\n"
                + "}\n");
    }

    /**
     * A data url image does not taint the canvas.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData ok", "toDataURL ok"})
    public void dataUrl() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-png.img")) {
            final String dataUrl = "data:image/png;base64,"
                    + Base64.getEncoder().encodeToString(IOUtils.toByteArray(is));
            loadPageVerify("", dataUrl, "draw(img);\n");
        }
    }

    /**
     * A crossorigin image with a matching Access-Control-Allow-Origin header does not taint the canvas.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData ok", "toDataURL ok"})
    public void crossOriginAttributeWithCorsHeader() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png",
                new NameValuePair(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "*"));
        loadPageVerify("crossorigin='anonymous'", URL_CROSS_ORIGIN_BASE + "img.png", "draw(img);\n");
    }

    /**
     * A crossorigin image without Access-Control-Allow-Origin header fails to load.
     * <p>The CORS check fails; the image is broken (error event, drawImage() throws).</p>
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "drawImage InvalidStateError"})
    public void crossOriginAttributeWithoutCorsHeader() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        loadPageVerify("crossorigin='anonymous'", URL_CROSS_ORIGIN_BASE + "img.png",
                "try { draw(img); } catch(e) { log('drawImage ' + e.name); }\n");
    }

    /**
     * The Access-Control-Allow-Origin header without crossorigin attribute; the image
     * is loaded in no-cors mode and taints the canvas.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData SecurityError", "toDataURL SecurityError"})
    public void corsHeaderWithoutCrossOriginAttribute() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png",
                new NameValuePair(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "*"));
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "img.png", "draw(img);\n");
    }

    /**
     * The rule crossorigin='use-credentials' needs the origin and Access-Control-Allow-Credentials.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData ok", "toDataURL ok"})
    public void useCredentialsWithCorsHeaders() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png",
                new NameValuePair(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:" + PORT),
                new NameValuePair(HttpHeader.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));
        loadPageVerify("crossorigin='use-credentials'", URL_CROSS_ORIGIN_BASE + "img.png",
                "try { draw(img); } catch(e) { log('drawImage ' + e.name); }\n");
    }

    /**
     * The rule crossorigin='use-credentials' does not accept the wildcard.
     * <p>The CORS check fails; the image is broken (error event, drawImage() throws).</p>
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "drawImage InvalidStateError"})
    public void useCredentialsWithWildcard() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png",
                new NameValuePair(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "*"));
        loadPageVerify("crossorigin='use-credentials'", URL_CROSS_ORIGIN_BASE + "img.png",
                "try { draw(img); } catch(e) { log('drawImage ' + e.name); }\n");
    }

    /**
     * A crossorigin image of the same origin needs no CORS headers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData ok", "toDataURL ok"})
    public void crossOriginAttributeSameOrigin() throws Exception {
        setImage(URL_FIRST, "img.png");
        loadPageVerify("crossorigin='anonymous'", "img.png",
                "try { draw(img); } catch(e) { log('drawImage ' + e.name); }\n");
    }

    /**
     * A crossorigin image redirecting to another origin without CORS headers is broken.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "drawImage InvalidStateError"})
    public void crossOriginAttributeRedirectToCrossOriginWithoutCorsHeader() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        setRedirect(new URL(URL_FIRST, "redirect"), new URL(URL_CROSS_ORIGIN_BASE, "img.png"));
        loadPageVerify("crossorigin='anonymous'", "redirect",
                "try { draw(img); } catch(e) { log('drawImage ' + e.name); }\n");
    }

    /**
     * Drawing an image that failed to load (404).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "drawImage InvalidStateError"})
    public void drawBrokenImageNotFound() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "img.png"), "", 404, "Not Found",
                MimeType.TEXT_HTML, new ArrayList<>());
        loadPageVerify("", "img.png",
                "try { draw(img); } catch(e) { log('drawImage ' + e.name); }\n");
    }

    /**
     * Drawing an image blocked by the Content-Security-Policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "drawImage InvalidStateError"})
    public void drawBrokenImageBlockedByPolicy() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        loadPageVerify("<meta http-equiv='Content-Security-Policy' content=\"img-src 'self'\">\n",
                "", URL_CROSS_ORIGIN_BASE + "img.png",
                "try { draw(img); } catch(e) { log('drawImage ' + e.name); }\n");
    }

    /**
     * Drawing an image without src does nothing (the image is not broken).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"getImageData ok", "toDataURL ok"})
    public void drawImageWithoutSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + DRAW_JS
            + "</script>\n"
            + "</head><body>\n"
            + "<canvas id='c' width='10' height='10'></canvas>\n"
            + "<script>\n"
            + "  try { draw(new Image()); } catch(e) { log('drawImage ' + e.name); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Setting the size clears the bitmap; Chrome and Edge reset the origin-clean flag also,
     * Firefox keeps it.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"load", "getImageData SecurityError", "toDataURL SecurityError",
                       "getImageData ok", "toDataURL ok"},
            FF = {"load", "getImageData SecurityError", "toDataURL SecurityError",
                  "getImageData SecurityError", "toDataURL SecurityError"},
            FF_ESR = {"load", "getImageData SecurityError", "toDataURL SecurityError",
                      "getImageData SecurityError", "toDataURL SecurityError"})
    public void resizeResetsTaint() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "img.png",
                "var canvas = draw(img);\n"
                + "canvas.width = 20;\n"
                + "canvas.getContext('2d').clearRect(0, 0, 20, 20);\n"
                + "readBack(canvas);\n");
    }

    /**
     * The cross-origin content is gone after the reset of the flag; setting the
     * width to the current value clears the bitmap also.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"load", "getImageData SecurityError", "toDataURL SecurityError", "0,0,0,0"},
            FF = {"load", "getImageData SecurityError", "toDataURL SecurityError", "SecurityError"},
            FF_ESR = {"load", "getImageData SecurityError", "toDataURL SecurityError", "SecurityError"})
    public void resizeClearsCrossOriginContent() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "img.png",
                "var canvas = draw(img);\n"
                + "canvas.width = canvas.width;\n"
                + "try {\n"
                + "  log(canvas.getContext('2d').getImageData(0, 0, 1, 1).data.join(','));\n"
                + "} catch(e) { log(e.name); }\n");
    }

    /**
     * Setting the height resets the flag like the width.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"load", "getImageData SecurityError", "toDataURL SecurityError",
                       "getImageData ok", "toDataURL ok"},
            FF = {"load", "getImageData SecurityError", "toDataURL SecurityError",
                  "getImageData SecurityError", "toDataURL SecurityError"},
            FF_ESR = {"load", "getImageData SecurityError", "toDataURL SecurityError",
                      "getImageData SecurityError", "toDataURL SecurityError"})
    public void resizeHeightResetsTaint() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "img.png",
                "var canvas = draw(img);\n"
                + "canvas.height = 20;\n"
                + "readBack(canvas);\n");
    }

    /**
     * Setting the size using setAttribute() resets the bitmap like the property.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"load", "getImageData SecurityError", "toDataURL SecurityError",
                       "getImageData ok", "toDataURL ok"},
            FF = {"load", "getImageData SecurityError", "toDataURL SecurityError",
                  "getImageData SecurityError", "toDataURL SecurityError"},
            FF_ESR = {"load", "getImageData SecurityError", "toDataURL SecurityError",
                      "getImageData SecurityError", "toDataURL SecurityError"})
    public void resizeBySetAttribute() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "img.png",
                "var canvas = draw(img);\n"
                + "canvas.setAttribute('width', '20');\n"
                + "readBack(canvas);\n");
    }

    /**
     * Drawing a tainted canvas taints the target canvas.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData SecurityError", "toDataURL SecurityError",
             "getImageData SecurityError", "toDataURL SecurityError"})
    public void drawTaintedCanvas() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "img.png",
                "var canvas = draw(img);\n"
                + "var target = document.createElement('canvas');\n"
                + "target.getContext('2d').drawImage(canvas, 0, 0);\n"
                + "readBack(target);\n");
    }

    /**
     * Drawing a clean canvas does not taint the target canvas.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData ok", "toDataURL ok", "getImageData ok", "toDataURL ok"})
    public void drawCleanCanvas() throws Exception {
        setImage(URL_FIRST, "img.png");
        loadPageVerify("", "img.png",
                "var canvas = draw(img);\n"
                + "var target = document.createElement('canvas');\n"
                + "target.getContext('2d').drawImage(canvas, 0, 0);\n"
                + "readBack(target);\n");
    }

    /**
     * Method getImageData() with a size of zero on a tainted canvas; the order of the checks.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "SecurityError"})
    public void getImageDataZeroSizeTainted() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "img.png",
                "var canvas = document.getElementById('c');\n"
                + "var ctx = canvas.getContext('2d');\n"
                + "ctx.drawImage(img, 0, 0);\n"
                + "try { ctx.getImageData(0, 0, 0, 1); log('ok'); } catch(e) { log(e.name); }\n");
    }

    /**
     * Only the canvas the image was drawn to is tainted.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "getImageData SecurityError", "toDataURL SecurityError",
             "getImageData ok", "toDataURL ok"})
    public void otherCanvasStaysClean() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.png");
        loadPageVerify("", URL_CROSS_ORIGIN_BASE + "img.png",
                "draw(img);\n"
                + "var other = document.createElement('canvas');\n"
                + "other.getContext('2d').fillRect(0, 0, 1, 1);\n"
                + "readBack(other);\n");
    }

    private void setImage(final URL base, final String path, final NameValuePair... headers) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-png.img")) {
            final byte[] bytes = IOUtils.toByteArray(is);
            getMockWebConnection().setResponse(new URL(base, path), bytes, 200, "OK", MimeType.IMAGE_PNG,
                    List.of(headers));
        }
    }

    private void setRedirect(final URL from, final URL to) {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", to.toExternalForm()));
        getMockWebConnection().setResponse(from, "", 302, "Found", MimeType.TEXT_HTML, headers);
    }

    private void loadPageVerify(final String imageAttributes, final String src, final String testJs)
            throws Exception {
        loadPageVerify("", imageAttributes, src, testJs);
    }

    private void loadPageVerify(final String head, final String imageAttributes, final String src,
            final String testJs) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + head
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + DRAW_JS
            + "</script>\n"
            + "</head><body>\n"
            + "<canvas id='c' width='10' height='10'></canvas>\n"
            + "<script>\n"
            + "  var img = document.createElement('img');\n"
            + "  img.onload = function() {\n"
            + "    log('load');\n"
            + testJs
            + "  };\n"
            + "  img.onerror = function() {\n"
            + "    log('error');\n"
            + testJs
            + "  };\n"
            + (imageAttributes.isEmpty() ? ""
                    : "  img.setAttribute('" + imageAttributes.substring(0, imageAttributes.indexOf('='))
                        + "', " + imageAttributes.substring(imageAttributes.indexOf('=') + 1) + ");\n")
            + "  img.src = '" + src + "';\n"
            + "</script>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
