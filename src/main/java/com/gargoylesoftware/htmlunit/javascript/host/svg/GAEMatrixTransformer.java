/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.svg;

/**
 * {@link MatrixTransformer} for GAE environment.
 * At the moment this is an empty implementation.
 *
 * @author Ronald Brill
 */
public class GAEMatrixTransformer implements MatrixTransformer {

    /**
     * Creates an instance.
     */
    public GAEMatrixTransformer() {
    }

    @Override
    public SvgMatrix flipX(final SvgMatrix svgMatrix) {
        return svgMatrix;
    }

    @Override
    public SvgMatrix flipY(final SvgMatrix svgMatrix) {
        return svgMatrix;
    }

    @Override
    public SvgMatrix inverse(final SvgMatrix svgMatrix) {
        return svgMatrix;
    }

    @Override
    public SvgMatrix multiply(final SvgMatrix factor1, final SvgMatrix factor2) {
        return factor1;
    }

    @Override
    public SvgMatrix rotate(final SvgMatrix svgMatrix, final double angle) {
        return svgMatrix;
    }

    @Override
    public SvgMatrix rotateFromVector(final SvgMatrix svgMatrix, final double x, final double y) {
        return svgMatrix;
    }

    @Override
    public SvgMatrix scale(final SvgMatrix svgMatrix, final double factor) {
        return svgMatrix;
    }

    @Override
    public SvgMatrix scaleNonUniform(final SvgMatrix svgMatrix, final double factorX, final double factorY) {
        return svgMatrix;
    }

    @Override
    public SvgMatrix skewX(final SvgMatrix svgMatrix, final double angle) {
        return svgMatrix;
    }

    @Override
    public SvgMatrix skewY(final SvgMatrix svgMatrix, final double angle) {
        return svgMatrix;
    }

    @Override
    public SvgMatrix translate(final SvgMatrix svgMatrix, final double x, final double y) {
        return svgMatrix;
    }
}
