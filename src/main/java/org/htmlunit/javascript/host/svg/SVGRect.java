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
package org.htmlunit.javascript.host.svg;

import org.htmlunit.corejs.javascript.ClassDescriptor;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.Undefined;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.HtmlUnitClassDescriptor;
import org.htmlunit.javascript.configuration.SupportedBrowser;

/**
 * A JavaScript object for {@code SVGRect}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class SVGRect extends HtmlUnitScriptable {

    /** Descriptor for registering this class with the JavaScript engine. */
    public static final HtmlUnitClassDescriptor HTMLUNIT_DESCRIPTOR = new HtmlUnitClassDescriptor() {

        private static final ClassDescriptor DESCRIPTOR_ = new ClassDescriptor.Builder("SVGRect", 0,
                (cx, f, callerObj, scope, thisObj, args) -> Undefined.instance)
                .withProp(ClassDescriptor.Destination.PROTO, "x",
                        (ScriptableObject.LambdaGetterFunction) thisObj ->
                                ((SVGRect) thisObj).getX(),
                        (ScriptableObject.LambdaSetterFunction) (owner, value) ->
                                ((SVGRect) owner).setX(JavaScriptEngine.toNumber(value)),
                        ScriptableObject.DONTENUM)
                .withProp(ClassDescriptor.Destination.PROTO, "y",
                        (ScriptableObject.LambdaGetterFunction) thisObj ->
                                ((SVGRect) thisObj).getY(),
                        (ScriptableObject.LambdaSetterFunction) (owner, value) ->
                                ((SVGRect) owner).setY(JavaScriptEngine.toNumber(value)),
                        ScriptableObject.DONTENUM)
                .withProp(ClassDescriptor.Destination.PROTO, "width",
                        (ScriptableObject.LambdaGetterFunction) thisObj ->
                                ((SVGRect) thisObj).getWidth(),
                        (ScriptableObject.LambdaSetterFunction) (owner, value) ->
                                ((SVGRect) owner).setWidth(JavaScriptEngine.toNumber(value)),
                        ScriptableObject.DONTENUM)
                .withProp(ClassDescriptor.Destination.PROTO, "height",
                        (ScriptableObject.LambdaGetterFunction) thisObj ->
                                ((SVGRect) thisObj).getHeight(),
                        (ScriptableObject.LambdaSetterFunction) (owner, value) ->
                                ((SVGRect) owner).setHeight(JavaScriptEngine.toNumber(value)),
                        ScriptableObject.DONTENUM)
                .build();

        @Override
        public ClassDescriptor forBrowser(final SupportedBrowser browser) {
            return DESCRIPTOR_;
        }

        @Override
        public Class<? extends HtmlUnitScriptable> getHostClass() {
            return SVGRect.class;
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

    private double xValue_;
    private double yValue_;
    private double width_;
    private double height_;

    /**
     * Gets x.
     * @return x
     */
    public double getX() {
        return xValue_;
    }

    /**
     * Sets x.
     * @param x the x
     */
    public void setX(final double x) {
        xValue_ = x;
    }

    /**
     * Gets y.
     * @return y
     */
    public double getY() {
        return yValue_;
    }

    /**
     * Sets y.
     * @param y the y
     */
    public void setY(final double y) {
        yValue_ = y;
    }

    /**
     * Gets width.
     * @return width
     */
    public double getWidth() {
        return width_;
    }

    /**
     * Sets width.
     * @param width the width
     */
    public void setWidth(final double width) {
        width_ = width;
    }

    /**
     * Gets height.
     * @return height
     */
    public double getHeight() {
        return height_;
    }

    /**
     * Sets height.
     * @param height the height
     */
    public void setHeight(final double height) {
        height_ = height;
    }

}
