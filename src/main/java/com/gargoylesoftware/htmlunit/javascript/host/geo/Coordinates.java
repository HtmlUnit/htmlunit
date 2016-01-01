/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@code Coordinates}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(browsers = { @WebBrowser(IE), @WebBrowser(EDGE) })
public class Coordinates extends SimpleScriptable {

    private double latitude_;
    private double longitude_;
    private double accuracy_;

    /**
     * Creates an instance.
     */
    @JsxConstructor(@WebBrowser(EDGE))
    public Coordinates() {
    }

    Coordinates(final double latitude, final double longitude, final double accuracy) {
        latitude_ = latitude;
        longitude_ = longitude;
        accuracy_ = accuracy;
    }

    /**
     * Returns the latitude.
     * @return the latitude
     */
    @JsxGetter
    public double getLatitude() {
        return latitude_;
    }

    /**
     * Returns the longitude.
     * @return the longitude
     */
    @JsxGetter
    public double getLongitude() {
        return longitude_;
    }

    /**
     * Returns the accuracy.
     * @return the accuracy
     */
    @JsxGetter
    public double getAccuracy() {
        return accuracy_;
    }

}
