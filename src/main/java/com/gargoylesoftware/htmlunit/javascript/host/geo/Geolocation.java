/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.geo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;

/**
 * A JavaScript object for Geolocation.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass
public class Geolocation extends SimpleScriptable {

    private static final Log LOG = LogFactory.getLog(Geolocation.class);

    /* Do not use this URL without Google permission! */
    private static String PROVIDER_URL_ = "https://maps.googleapis.com/maps/api/browserlocation/json";
    private Function successHandler_;
    private Function errorHandler_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Geolocation() {
        // Empty.
    }

    /**
     * Gets the current position.
     * @param successCallback success callback
     * @param errorCallback optional error callback
     * @param options optional options
     */
    @JsxFunction
    public void getCurrentPosition(final Function successCallback, final Object errorCallback,
            final Object options) {
        successHandler_ = successCallback;
        if (errorCallback instanceof Function) {
            errorHandler_ = (Function) errorCallback;
        }
        else {
            errorHandler_ = null;
        }
        final WebWindow webWindow = getWindow().getWebWindow();
        if (webWindow.getWebClient().getOptions().isGeolocationEnabled()) {
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
                    .createJavaScriptJob(0, null, new Runnable() {
                        @Override
                        public void run() {
                            doGetPosition();
                        }
                    });
            webWindow.getJobManager().addJob(job, getWindow().getWebWindow().getEnclosedPage());
        }
    }

    /**
     * Notifies the callbacks whenever the position changes, till clearWatch() is called.
     * @param successCallback success callback
     * @param errorCallback optional error callback
     * @param options optional options
     * @return the watch id
     */
    @JsxFunction
    public int watchPosition(final Function successCallback, final Object errorCallback,
            final Object options) {
        return 0;
    }

    /**
     * Clears the specified watch ID.
     * @param watchId the watch id
     */
    @JsxFunction
    public void clearWatch(final int watchId) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        if (getWindow().getWebWindow() != null
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.GEO_GEOLOCATION)) {
            return "GeoGeolocation";
        }
        return super.getClassName();
    }

    private void doGetPosition() {
        final String os = System.getProperty("os.name").toLowerCase();
        String wifiStringString = null;
        if (os.contains("win")) {
            wifiStringString = getWifiStringWindows();
        }
        if (wifiStringString != null) {
            String url = PROVIDER_URL_;
            if (url.contains("?")) {
                url += '&';
            }
            else {
                url += '?';
            }
            url += "browser=firefox&sensor=true";
            url += wifiStringString;

            while (url.length() >= 1900) {
                url = url.substring(0, url.lastIndexOf("&wifi="));
            }

            if (LOG.isInfoEnabled()) {
                LOG.info("Invoking URL: " + url);
            }

            final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);
            try {
                final Page page = webClient.getPage(url);
                final String content = page.getWebResponse().getContentAsString();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Receieved Content: " + content);
                }
                final double latitude = Double.parseDouble(getJSONValue(content, "lat"));
                final double longitude = Double.parseDouble(getJSONValue(content, "lng"));
                final double accuracy = Double.parseDouble(getJSONValue(content, "accuracy"));

                final Coordinates coordinates = new Coordinates(latitude, longitude, accuracy);
                coordinates.setPrototype(getPrototype(coordinates.getClass()));

                final Position position = new Position(coordinates);
                position.setPrototype(getPrototype(position.getClass()));

                final JavaScriptEngine jsEngine = getWindow().getWebWindow().getWebClient().getJavaScriptEngine();
                jsEngine.callFunction((HtmlPage) getWindow().getWebWindow().getEnclosedPage(), successHandler_, this,
                        getParentScope(), new Object[] {position});
            }
            catch (final Exception e) {
                LOG.error("", e);
            }
            finally {
                webClient.closeAllWindows();
            }
        }
        else {
            LOG.error("Operating System not supported: " + os);
        }
    }

    private String getJSONValue(final String content, final String key) {
        final StringBuilder builder = new StringBuilder();
        int index = content.indexOf("\"" + key + "\"") + key.length() + 2;
        for (index = content.indexOf(':', index) + 1; index < content.length(); index++) {
            final char ch = content.charAt(index);
            if (ch == ',' || ch == '}') {
                break;
            }
            builder.append(ch);
        }
        return builder.toString().trim();
    }

    String getWifiStringWindows() {
        final StringBuilder builder = new StringBuilder();
        try {
            final List<String> lines = runCommand("netsh wlan show networks mode=bssid");
            for (final Iterator<String> it = lines.iterator(); it.hasNext();) {
                String line = it.next();
                if (line.startsWith("SSID ")) {
                    final String name = line.substring(line.lastIndexOf(' ') + 1);
                    if (it.hasNext()) {
                        it.next();
                    }
                    if (it.hasNext()) {
                        it.next();
                    }
                    if (it.hasNext()) {
                        it.next();
                    }
                    while (it.hasNext()) {
                        line = it.next();
                        if (line.trim().startsWith("BSSID ")) {
                            final String mac = line.substring(line.lastIndexOf(' ') + 1);
                            if (it.hasNext()) {
                                line = it.next().trim();
                                if (line.startsWith("Signal")) {
                                    final String signal = line.substring(line.lastIndexOf(' ') + 1, line.length() - 1);
                                    final int signalStrength = Integer.parseInt(signal) / 2 - 100;
                                    builder.append("&wifi=")
                                        .append("mac:")
                                        .append(mac.replace(':', '-'))
                                        .append("%7C")
                                        .append("ssid:")
                                        .append(name)
                                        .append("%7C")
                                        .append("ss:")
                                        .append(signalStrength);
                                }
                            }
                        }
                        if (line.trim().isEmpty()) {
                            break;
                        }
                    }
                }
            }
        }
        catch (final IOException e) {
            //
        }
        return builder.toString();
    }

    private List<String> runCommand(final String command) throws IOException {
        final List<String> list = new ArrayList<String>();
        final Process p = Runtime.getRuntime().exec(command);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            list.add(line);
        }
        reader.close();
        return list;
    }

    static void setProviderUrl(final String url) {
        PROVIDER_URL_ = url;
    }
}
