/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.geo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.WebClient;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import org.htmlunit.javascript.background.JavaScriptJob;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;

/**
 * A JavaScript object for Geolocation.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class Geolocation extends HtmlUnitScriptable {

    private static final Log LOG = LogFactory.getLog(Geolocation.class);

    private Function successHandler_;
    private Function errorHandler_;

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeError("Illegal constructor.");
    }

    /**
     * Gets the current position.
     * @param successCallback success callback
     * @param errorCallback optional error callback
     * @param options optional options
     */
    @JsxFunction
    public void getCurrentPosition(final Function successCallback, final Function errorCallback,
            final Object options) {
        successHandler_ = successCallback;
        errorHandler_ = errorCallback;

        final WebWindow webWindow = getWindow().getWebWindow();
        if (webWindow.getWebClient().getOptions().isGeolocationEnabled()) {
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
                    .createJavaScriptJob(0, null, () -> doGetPosition());
            webWindow.getJobManager().addJob(job, webWindow.getEnclosedPage());
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
        // nothing to do
    }

    void doGetPosition() {
        final WebWindow ww = getWindow().getWebWindow();
        final WebClient webClient = ww.getWebClient();

        final org.htmlunit.WebClientOptions.Geolocation geolocation = webClient.getOptions().getGeolocation();

        final GeolocationCoordinates coordinates = new GeolocationCoordinates(
                geolocation.getLatitude(), geolocation.getLongitude(), geolocation.getAccuracy());
        coordinates.setPrototype(getPrototype(coordinates.getClass()));

        final GeolocationPosition position = new GeolocationPosition(coordinates);
        position.setPrototype(getPrototype(position.getClass()));

        final JavaScriptEngine jsEngine = (JavaScriptEngine) ww.getWebClient().getJavaScriptEngine();
        jsEngine.callFunction((HtmlPage) ww.getEnclosedPage(), successHandler_, this,
                getParentScope(), new Object[] {position});
    }
}
