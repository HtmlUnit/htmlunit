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

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.Serializable;

/**
 * Helper to support various matrix transform operations.
 *
 * @author Ronald Brill
 */
class MatrixTransformer {
    private static final AffineTransform FLIP_X_TRANSFORM = new AffineTransform(-1, 0, 0, 1, 0, 0);
    private static final AffineTransform FLIP_Y_TRANSFORM = new AffineTransform(1, 0, 0, -1, 0, 0);

    /**
     * Flip X.
     *
     * @param svgMatrix the input
     * @return the new matrix
     */
    public SvgMatrix flipX(final SvgMatrix svgMatrix) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.concatenate(FLIP_X_TRANSFORM);
        return toSvgMatrix(tr);
    }

    /**
     * Flip Y.
     *
     * @param svgMatrix the input
     * @return the new matrix
     */
    public SvgMatrix flipY(final SvgMatrix svgMatrix) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.concatenate(FLIP_Y_TRANSFORM);
        return toSvgMatrix(tr);
    }

    /**
     * Inverse.
     *
     * @param svgMatrix the input
     * @return the new matrix
     */
    public SvgMatrix inverse(final SvgMatrix svgMatrix) {
        try {
            AffineTransform tr = toAffineTransform(svgMatrix);
            tr = tr.createInverse();
            return toSvgMatrix(tr);
        }
        catch (final NoninvertibleTransformException e) {
            throw new IllegalArgumentException(
                    "Failed to execute 'inverse' on 'SVGMatrix': The matrix is not invertible.", e);
        }
    }

    /**
     * Multiply.
     *
     * @param factor1 the first factor
     * @param factor2 the second factor
     * @return the new matrix
     */
    public SvgMatrix multiply(final SvgMatrix factor1, final SvgMatrix factor2) {
        final AffineTransform f1 = toAffineTransform(factor1);
        final AffineTransform f2 = toAffineTransform(factor2);
        f1.concatenate(f2);
        return toSvgMatrix(f1);
    }

    /**
     * Rotate.
     *
     * @param svgMatrix the input
     * @param angle rotation angle
     * @return the new matrix
     */
    public SvgMatrix rotate(final SvgMatrix svgMatrix, final double angle) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.rotate(Math.toRadians(angle));
        return toSvgMatrix(tr);
    }

    /**
     * Rotate.
     *
     * @param svgMatrix the input
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     * @return the new matrix
     */
    public SvgMatrix rotateFromVector(final SvgMatrix svgMatrix, final double x, final double y) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.rotate(Math.atan2(y, x));
        return toSvgMatrix(tr);
    }

    /**
     * Scale.
     *
     * @param svgMatrix the input
     * @param factor the scale factor
     * @return the new matrix
     */
    public SvgMatrix scale(final SvgMatrix svgMatrix, final double factor) {
        return scaleNonUniform(svgMatrix, factor, factor);
    }

    /**
     * Scale.
     *
     * @param svgMatrix the input
     * @param factorX the factor for the x-axis
     * @param factorY the factor for the y-axis
     * @return the new matrix
     */
    public SvgMatrix scaleNonUniform(final SvgMatrix svgMatrix, final double factorX, final double factorY) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.scale(factorX, factorY);
        return toSvgMatrix(tr);
    }

    /**
     * SkewX.
     *
     * @param svgMatrix the input
     * @param angle the skew angle
     * @return the new matrix
     */
    public SvgMatrix skewX(final SvgMatrix svgMatrix, final double angle) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.concatenate(AffineTransform.getShearInstance(Math.tan(Math.toRadians(angle)), 0));
        return toSvgMatrix(tr);
    }

    /**
     * SkewY.
     *
     * @param svgMatrix the input
     * @param angle the skew angle
     * @return the new matrix
     */
    public SvgMatrix skewY(final SvgMatrix svgMatrix, final double angle) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.concatenate(AffineTransform.getShearInstance(0, Math.tan(Math.toRadians(angle))));
        return toSvgMatrix(tr);
    }

    /**
     * Translate.
     *
     * @param svgMatrix the input
     * @param x the distance along the x-axis
     * @param y the distance along the y-axis
     * @return the new matrix
     */
    public SvgMatrix translate(final SvgMatrix svgMatrix, final double x, final double y) {
        final AffineTransform tr = toAffineTransform(svgMatrix);
        tr.translate(x, y);
        return toSvgMatrix(tr);
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

    /**
     * Matrix value object.
     *
     */
    static final class SvgMatrix implements Serializable {
        private double shearX_;
        private double shearY_;
        private double scaleX_;
        private double scaleY_;
        private double translateX_;
        private double translateY_;

        /**
         * Ctor.
         */
        SvgMatrix() {
            this(0, 0, 1, 1, 0, 0);
        }

        /**
         * Ctor.
         * @param shearX shearX value
         * @param shearY shearY value
         * @param scaleX scaleX value
         * @param scaleY scaleY value
         * @param translateX translateX value
         * @param translateY translateY value
         */
        SvgMatrix(final double shearX,
                         final double shearY,
                         final double scaleX,
                         final double scaleY,
                         final double translateX,
                         final double translateY) {
            shearX_ = shearX;
            shearY_ = shearY;
            scaleX_ = scaleX;
            scaleY_ = scaleY;
            translateX_ = translateX;
            translateY_ = translateY;
        }

        /**
         * @return shearX
         */
        public double getShearX() {
            return shearX_;
        }

        /**
         * @param shearX new value
         */
        public void setShearX(final double shearX) {
            shearX_ = shearX;
        }

        /**
         * @return shearY
         */
        public double getShearY() {
            return shearY_;
        }

        /**
         * @param shearY new value
         */
        public void setShearY(final double shearY) {
            shearY_ = shearY;
        }

        /**
         * @return scaleX
         */
        public double getScaleX() {
            return scaleX_;
        }

        /**
         * @param scaleX new value
         */
        public void setScaleX(final double scaleX) {
            scaleX_ = scaleX;
        }

        /**
         * @return scaleY
         */
        public double getScaleY() {
            return scaleY_;
        }

        /**
         * @param scaleY new value
         */
        public void setScaleY(final double scaleY) {
            scaleY_ = scaleY;
        }

        /**
         * @return translateX
         */
        public double getTranslateX() {
            return translateX_;
        }

        /**
         * @param translateX new value
         */
        public void setTranslateX(final double translateX) {
            translateX_ = translateX;
        }

        /**
         * @return translateY
         */
        public double getTranslateY() {
            return translateY_;
        }

        /**
         * @param translateY new value
         */
        public void setTranslateY(final double translateY) {
            translateY_ = translateY;
        }
    }
}
