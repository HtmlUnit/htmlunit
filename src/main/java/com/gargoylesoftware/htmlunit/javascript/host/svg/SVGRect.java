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
package com.gargoylesoftware.htmlunit.javascript.host.svg;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for {@code SVGRect}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class SVGRect extends SimpleScriptable {

    private double xValue_;
    private double yValue_;
    private double width_;
    private double height_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public SVGRect() {
    }

    /**
     * Gets x.
     * @return x
     */
    @JsxGetter
    public double getX() {
        return xValue_;
    }

    /**
     * Sets x.
     * @param x the x
     */
    @JsxSetter
    public void setX(final double x) {
        xValue_ = x;
    }

    /**
     * Gets y.
     * @return y
     */
    @JsxGetter
    public double getY() {
        return yValue_;
    }

    /**
     * Sets y.
     * @param y the y
     */
    @JsxSetter
    public void setY(final double y) {
        yValue_ = y;
    }

    /**
     * Gets width.
     * @return width
     */
    @JsxGetter
    public double getWidth() {
        return width_;
    }

    /**
     * Sets width.
     * @param width the width
     */
    @JsxSetter
    public void setWidth(final double width) {
        width_ = width;
    }

    /**
     * Gets height.
     * @return height
     */
    @JsxGetter
    public double getHeight() {
        return height_;
    }

    /**
     * Sets height.
     * @param height the height
     */
    @JsxSetter
    public void setHeight(final double height) {
        height_ = height;
    }

}
