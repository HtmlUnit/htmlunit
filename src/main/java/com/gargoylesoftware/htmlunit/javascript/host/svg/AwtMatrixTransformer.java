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

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

/**
 * {@link MatrixTransformer} that uses AWT {@link AffineTransform}.
 *
 * @author Ronald Brill
 */
public class AwtMatrixTransformer implements MatrixTransformer {
    private static final AffineTransform FLIP_X_TRANSFORM = new AffineTransform(-1, 0, 0, 1, 0, 0);
    private static final AffineTransform FLIP_Y_TRANSFORM = new AffineTransform(1, 0, 0, -1, 0, 0);

    /**
     * Creates an instance.
     */
    public AwtMatrixTransformer() {
    }

    private static AffineTransform toAffineTransform(final SvgMatrix svgMatrix) {
        return new AffineTransform(
                svgMatrix.getScaleX(),
                svgMatrix.getShearY(),
                svgMatrix.getShearX(),
                svgMatrix.getScaleY(),
                svgMatrix.getTranslateX(),
                svgMatrix.getTranslateY());
    }

    private static SvgMatrix toSvgMatrix(final AffineTransform affineTransform) {
        return new SvgMatrix(
                affineTransform.getShearX(),
                affineTransform.getShearY(),
                affineTransform.getScaleX(),
                affineTransform.getScaleY(),
                affineTransform.getTranslateX(),
                affineTransform.getTranslateY());
    }

    @Override
    public SvgMatrix flipX(final SvgMatrix svgMatrix) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.concatenate(FLIP_X_TRANSFORM);
        return toSvgMatrix(tr);
    }

    @Override
    public SvgMatrix flipY(final SvgMatrix svgMatrix) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.concatenate(FLIP_Y_TRANSFORM);
        return toSvgMatrix(tr);
    }

    @Override
    public SvgMatrix inverse(final SvgMatrix svgMatrix) {
        try {
            AffineTransform tr = toAffineTransform(svgMatrix);
            tr = tr.createInverse();
            return toSvgMatrix(tr);
        }
        catch (final NoninvertibleTransformException e) {
            throw new IllegalArgumentException(
                    "Failed to execute 'inverse' on 'SVGMatrix': The matrix is not invertible.");
        }
    }

    @Override
    public SvgMatrix multiply(final SvgMatrix factor1, final SvgMatrix factor2) {
        final AffineTransform f1 = toAffineTransform(factor1);
        final AffineTransform f2 = toAffineTransform(factor2);
        f1.concatenate(f2);
        return toSvgMatrix(f1);
    }

    @Override
    public SvgMatrix rotate(final SvgMatrix svgMatrix, final double angle) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.rotate(Math.toRadians(angle));
        return toSvgMatrix(tr);
    }

    @Override
    public SvgMatrix rotateFromVector(final SvgMatrix svgMatrix, final double x, final double y) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.rotate(Math.atan2(y, x));
        return toSvgMatrix(tr);
    }

    @Override
    public SvgMatrix scale(final SvgMatrix svgMatrix, final double factor) {
        return scaleNonUniform(svgMatrix, factor, factor);
    }

    @Override
    public SvgMatrix scaleNonUniform(final SvgMatrix svgMatrix, final double factorX, final double factorY) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.scale(factorX, factorY);
        return toSvgMatrix(tr);
    }

    @Override
    public SvgMatrix skewX(final SvgMatrix svgMatrix, final double angle) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.concatenate(AffineTransform.getShearInstance(Math.tan(Math.toRadians(angle)), 0));
        return toSvgMatrix(tr);
    }

    @Override
    public SvgMatrix skewY(final SvgMatrix svgMatrix, final double angle) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.concatenate(AffineTransform.getShearInstance(0, Math.tan(Math.toRadians(angle))));
        return toSvgMatrix(tr);
    }

    @Override
    public SvgMatrix translate(final SvgMatrix svgMatrix, final double x, final double y) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.translate(x, y);
        return toSvgMatrix(tr);
    }
}
