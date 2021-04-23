/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CANVAS_DATA_URL_CHROME_PNG;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CANVAS_DATA_URL_IE_PNG;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.html.HtmlCanvas;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * The JavaScript object {@code HTMLCanvasElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlCanvas.class)
public class HTMLCanvasElement extends HTMLElement {

    private CanvasRenderingContext2D context2d_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLCanvasElement() {
    }

    /**
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @Override
    @JsxGetter
    public int getWidth() {
        final String value = getDomNodeOrDie().getAttributeDirect("width");
        final Integer intValue = getValue(value);
        if (intValue != null) {
            return intValue;
        }
        return 300;
    }

    static Integer getValue(final String value) {
        int index = -1;
        while (index + 1 < value.length() && Character.isDigit(value.charAt(index + 1))) {
            index++;
        }
        if (index != -1) {
            return Integer.parseInt(value.substring(0, index + 1));
        }
        return null;
    }

    /**
     * Sets the {@code width} property.
     * @param width the {@code width} property
     */
    @JsxSetter
    public void setWidth(final int width) {
        getDomNodeOrDie().setAttribute("width", Integer.toString(width));
    }

    /**
     * Returns the {@code height} property.
     * @return the {@code height} property
     */
    @Override
    @JsxGetter
    public int getHeight() {
        final String value = getDomNodeOrDie().getAttributeDirect("height");
        final Integer intValue = getValue(value);
        if (intValue != null) {
            return intValue;
        }
        return 150;
    }

    /**
     * Sets the {@code height} property.
     * @param height the {@code height} property
     */
    @JsxSetter
    public void setHeight(final int height) {
        getDomNodeOrDie().setAttribute("height", Integer.toString(height));
    }

    /**
     * Gets the context.
     * @param contextId the context id
     * @return Returns an object that exposes an API for drawing on the canvas,
     * or null if the given context ID is not supported
     */
    @JsxFunction
    public Object getContext(final String contextId) {
        if ("2d".equals(contextId)) {
            if (context2d_ == null) {
                final CanvasRenderingContext2D context = new CanvasRenderingContext2D(this);
                context.setParentScope(getParentScope());
                context.setPrototype(getPrototype(context.getClass()));
                context2d_ = context;
            }
            return context2d_;
        }
        return null;
    }

    /**
     * Get the data: URL representation of the Canvas element.
     * Here we return an empty image.
     * @param type the type (optional)
     * @return the data URL
     */
    @JsxFunction
    public String toDataURL(final Object type) {
        if (context2d_ != null) {
            final String typeInUse;
            if (Undefined.isUndefined(type)) {
                typeInUse = null;
            }
            else {
                typeInUse = Context.toString(type);
            }
            return context2d_.toDataURL(typeInUse);
        }

        if (getBrowserVersion().hasFeature(JS_CANVAS_DATA_URL_IE_PNG)) {
            return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASwAAACWCAYAAABkW7XSAAAAAXNSR0IArs4c6QAAAARnQU1BAA"
                + "Cxjwv8YQUAAADGSURBVHhe7cExAQAAAMKg9U9tCF8gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAONUAv9QAAcDhjokAAAAASUV"
                + "ORK5CYII=";
        }

        if (getBrowserVersion().hasFeature(JS_CANVAS_DATA_URL_CHROME_PNG)) {
            return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASwAAACWCAYAAABkW7XSAAAEYklEQVR4Xu3UAQkAAAwCwdm/"
                + "9HI83BLIOdw5AgQIRAQWySkmAQIEzmB5AgIEMgIGK1OVoAQIGCw/QIBARsBgZaoSlAABg+UHCBDICBisTFWCEiBgsPwAAQIZAYO"
                + "VqUpQAgQMlh8gQCAjYLAyVQlKgIDB8gMECGQEDFamKkEJEDBYfoAAgYyAwcpUJSgBAgbLDxAgkBEwWJmqBCVAwGD5AQIEMgIGK1"
                + "OVoAQIGCw/QIBARsBgZaoSlAABg+UHCBDICBisTFWCEiBgsPwAAQIZAYOVqUpQAgQMlh8gQCAjYLAyVQlKgIDB8gMECGQEDFamK"
                + "kEJEDBYfoAAgYyAwcpUJSgBAgbLDxAgkBEwWJmqBCVAwGD5AQIEMgIGK1OVoAQIGCw/QIBARsBgZaoSlAABg+UHCBDICBisTFWC"
                + "EiBgsPwAAQIZAYOVqUpQAgQMlh8gQCAjYLAyVQlKgIDB8gMECGQEDFamKkEJEDBYfoAAgYyAwcpUJSgBAgbLDxAgkBEwWJmqBCV"
                + "AwGD5AQIEMgIGK1OVoAQIGCw/QIBARsBgZaoSlAABg+UHCBDICBisTFWCEiBgsPwAAQIZAYOVqUpQAgQMlh8gQCAjYLAyVQlKgI"
                + "DB8gMECGQEDFamKkEJEDBYfoAAgYyAwcpUJSgBAgbLDxAgkBEwWJmqBCVAwGD5AQIEMgIGK1OVoAQIGCw/QIBARsBgZaoSlAABg"
                + "+UHCBDICBisTFWCEiBgsPwAAQIZAYOVqUpQAgQMlh8gQCAjYLAyVQlKgIDB8gMECGQEDFamKkEJEDBYfoAAgYyAwcpUJSgBAgbL"
                + "DxAgkBEwWJmqBCVAwGD5AQIEMgIGK1OVoAQIGCw/QIBARsBgZaoSlAABg+UHCBDICBisTFWCEiBgsPwAAQIZAYOVqUpQAgQMlh8"
                + "gQCAjYLAyVQlKgIDB8gMECGQEDFamKkEJEDBYfoAAgYyAwcpUJSgBAgbLDxAgkBEwWJmqBCVAwGD5AQIEMgIGK1OVoAQIGCw/QI"
                + "BARsBgZaoSlAABg+UHCBDICBisTFWCEiBgsPwAAQIZAYOVqUpQAgQMlh8gQCAjYLAyVQlKgIDB8gMECGQEDFamKkEJEDBYfoAAg"
                + "YyAwcpUJSgBAgbLDxAgkBEwWJmqBCVAwGD5AQIEMgIGK1OVoAQIGCw/QIBARsBgZaoSlAABg+UHCBDICBisTFWCEiBgsPwAAQIZ"
                + "AYOVqUpQAgQMlh8gQCAjYLAyVQlKgIDB8gMECGQEDFamKkEJEDBYfoAAgYyAwcpUJSgBAgbLDxAgkBEwWJmqBCVAwGD5AQIEMgI"
                + "GK1OVoAQIGCw/QIBARsBgZaoSlAABg+UHCBDICBisTFWCEiBgsPwAAQIZAYOVqUpQAgQMlh8gQCAjYLAyVQlKgIDB8gMECGQEDF"
                + "amKkEJEDBYfoAAgYyAwcpUJSgBAgbLDxAgkBEwWJmqBCVAwGD5AQIEMgIGK1OVoAQIGCw/QIBARsBgZaoSlACBB1YxAJfjJb2jA"
                + "AAAAElFTkSuQmCC";
        }

        return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASwAAACWCAYAAABkW7XSAAAAxUlEQVR4nO3BMQEAAADCoPVPbQhf"
            + "oAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAOA1v9QAATX68/0AAAAASUVORK5CYII=";
    }
}
