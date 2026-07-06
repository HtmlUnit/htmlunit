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

import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlMeter;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.dom.NodeList;

/**
 * The JavaScript object {@code HTMLMeterElement}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Ahmed Ashour
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLMeterElement">MDN Documentation</a>
 */
@JsxClass(domClass = HtmlMeter.class)
public class HTMLMeterElement extends HTMLElement {

    /** "Live" labels collection; has to be a member to have equality (==) working. */
    private NodeList labels_;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the {@code value} property.
     * @return the {@code value} property
     */
    @JsxGetter
    @Override
    public Double getValue() {
        return getAttributeAsDouble(DomElement.VALUE_ATTRIBUTE, 0);
    }

    /**
     * Returns the {@code min} property.
     * @return the {@code min} property
     */
    @JsxGetter
    public double getMin() {
        return getAttributeAsDouble("min", 0);
    }

    /**
     * Returns the {@code max} property.
     * @return the {@code max} property
     */
    @JsxGetter
    public double getMax() {
        return getAttributeAsDouble("max", 1);
    }

    /**
     * Returns the {@code low} property.
     * @return the {@code low} property
     */
    @JsxGetter
    public double getLow() {
        final double val = getAttributeAsDouble("low", Double.MAX_VALUE);
        if (val == Double.MAX_VALUE) {
            return getMin();
        }
        return val;
    }

    /**
     * Returns the {@code high} property.
     * @return the {@code high} property
     */
    @JsxGetter
    public double getHigh() {
        final double val = getAttributeAsDouble("high", Double.MIN_VALUE);
        if (val == Double.MIN_VALUE) {
            return getMax();
        }
        return val;
    }

    /**
     * Returns the {@code optimum} property.
     * @return the {@code optimum} property
     */
    @JsxGetter
    public double getOptimum() {
        final double val = getAttributeAsDouble("optimum", Double.MAX_VALUE);
        if (val == Double.MAX_VALUE) {
            return getValue();
        }
        return val;
    }

    private double getAttributeAsDouble(final String attributeName, final double defaultValue) {
        try {
            return Double.parseDouble(getDomNodeOrDie().getAttribute(attributeName));
        }
        catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Returns the labels associated with the element.
     * @return the labels associated with the element
     */
    @JsxGetter
    public NodeList getLabels() {
        if (labels_ == null) {
            labels_ = new LabelsNodeList(getDomNodeOrDie());
        }
        return labels_;
    }

}
