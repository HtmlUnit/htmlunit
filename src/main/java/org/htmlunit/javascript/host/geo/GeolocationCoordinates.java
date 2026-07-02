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
package org.htmlunit.javascript.host.geo;

import org.htmlunit.corejs.javascript.ClassDescriptor;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.HtmlUnitClassDescriptor;
import org.htmlunit.javascript.configuration.SupportedBrowser;

/**
 * A JavaScript object for GeolocationCoordinates.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class GeolocationCoordinates extends HtmlUnitScriptable {

    private double latitude_;
    private double longitude_;
    private double accuracy_;

    /** Descriptor for registering this class with the JavaScript engine. */
    public static final HtmlUnitClassDescriptor HTMLUNIT_DESCRIPTOR = new HtmlUnitClassDescriptor() {

        private static final ClassDescriptor DESCRIPTOR_ = new ClassDescriptor.Builder("GeolocationCoordinates", 0,
                (cx, f, callerObj, scope, thisObj, args) -> {
                    throw JavaScriptEngine.typeErrorIllegalConstructor();
                })
                .withProp(ClassDescriptor.Destination.PROTO, "latitude",
                        (ScriptableObject.LambdaGetterFunction) thisObj ->
                                ((GeolocationCoordinates) thisObj).getLatitude(),
                        null,
                        ScriptableObject.DONTENUM | ScriptableObject.READONLY)
                .withProp(ClassDescriptor.Destination.PROTO, "longitude",
                        (ScriptableObject.LambdaGetterFunction) thisObj ->
                                ((GeolocationCoordinates) thisObj).getLongitude(),
                        null,
                        ScriptableObject.DONTENUM | ScriptableObject.READONLY)
                .withProp(ClassDescriptor.Destination.PROTO, "accuracy",
                        (ScriptableObject.LambdaGetterFunction) thisObj ->
                                ((GeolocationCoordinates) thisObj).getAccuracy(),
                        null,
                        ScriptableObject.DONTENUM | ScriptableObject.READONLY)
                .build();

        @Override
        public ClassDescriptor forBrowser(final SupportedBrowser browser) {
            return DESCRIPTOR_;
        }

        @Override
        public Class<? extends HtmlUnitScriptable> getHostClass() {
            return GeolocationCoordinates.class;
        }

        @Override
        public Class<?>[] getDomClasses() {
            return new Class<?>[0];
        }

        @Override
        public boolean isJsObject() {
            return true;
        }

        @Override
        public String getExtendedClassName() {
            return "";
        }
    };

    /**
     * Creates an instance.
     */
    public GeolocationCoordinates() {
        super();
    }

    GeolocationCoordinates(final double latitude, final double longitude, final double accuracy) {
        super();
        latitude_ = latitude;
        longitude_ = longitude;
        accuracy_ = accuracy;
    }

    /**
     * Returns the latitude.
     * @return the latitude
     */
    public double getLatitude() {
        return latitude_;
    }

    /**
     * Returns the longitude.
     * @return the longitude
     */
    public double getLongitude() {
        return longitude_;
    }

    /**
     * Returns the accuracy.
     * @return the accuracy
     */
    public double getAccuracy() {
        return accuracy_;
    }

}
