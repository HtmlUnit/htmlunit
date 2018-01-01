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

import java.io.Serializable;

/**
 * Interface redirection to support GAE also.
 *
 * @author Ronald Brill
 */
public interface MatrixTransformer {

    /**
     * Flip X.
     *
     * @param svgMatrix the input
     * @return the new matrix
     */
    SvgMatrix flipX(SvgMatrix svgMatrix);

    /**
     * Flip X.
     *
     * @param svgMatrix the input
     * @return the new matrix
     */
    SvgMatrix flipY(SvgMatrix svgMatrix);

    /**
     * Inverse.
     *
     * @param svgMatrix the input
     * @return the new matrix
     */
    SvgMatrix inverse(SvgMatrix svgMatrix);

    /**
     * Multiply.
     *
     * @param factor1 the first factor
     * @param factor2 the second factor
     * @return the new matrix
     */
    SvgMatrix multiply(SvgMatrix factor1, SvgMatrix factor2);

    /**
     * Rotate.
     *
     * @param svgMatrix the input
     * @param angle rotation angle
     * @return the new matrix
     */
    SvgMatrix rotate(SvgMatrix svgMatrix, double angle);

    /**
     * Rotate.
     *
     * @param svgMatrix the input
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     * @return the new matrix
     */
    SvgMatrix rotateFromVector(SvgMatrix svgMatrix, double x, double y);

    /**
     * Scale.
     *
     * @param svgMatrix the input
     * @param factor the scale factor
     * @return the new matrix
     */
    SvgMatrix scale(SvgMatrix svgMatrix, double factor);

    /**
     * Scale.
     *
     * @param svgMatrix the input
     * @param factorX the factor for the x-axis
     * @param factorY the factor for the y-axis
     * @return the new matrix
     */
    SvgMatrix scaleNonUniform(SvgMatrix svgMatrix, double factorX, double factorY);

    /**
     * SkewX.
     *
     * @param svgMatrix the input
     * @param angle the skew angle
     * @return the new matrix
     */
    SvgMatrix skewX(SvgMatrix svgMatrix, double angle);

    /**
     * SkewX.
     *
     * @param svgMatrix the input
     * @param angle the skew angle
     * @return the new matrix
     */
    SvgMatrix skewY(SvgMatrix svgMatrix, double angle);

    /**
     * SkewX.
     *
     * @param svgMatrix the input
     * @param x the distance along the x-axis
     * @param y the distance along the y-axis
     * @return the new matrix
     */
    SvgMatrix translate(SvgMatrix svgMatrix, double x, double y);

    /**
     * Matrix value object.
     *
     */
    final class SvgMatrix implements Serializable {
        private double shearX_;
        private double shearY_;
        private double scaleX_;
        private double scaleY_;
        private double translateX_;
        private double translateY_;

        /**
         * Ctor.
         */
        public SvgMatrix() {
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
        public SvgMatrix(final double shearX,
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
