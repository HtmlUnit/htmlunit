/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering;

import java.io.IOException;

import javax.imageio.ImageReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.javascript.host.canvas.ImageData;

/**
 * The default implementation of {@link RenderingBackend}.
 *
 * @author Ronald Brill
 */
public class NoOpRenderingBackend implements RenderingBackend {

    private static final Log LOG = LogFactory.getLog(NoOpRenderingBackend.class);

    private static int ID_GENERATOR_;
    private final int id_;

    /**
     * Constructor.
     * @param imageWidth the width
     * @param imageHeight the height
     */
    public NoOpRenderingBackend(final int imageWidth, final int imageHeight) {
        id_ = ID_GENERATOR_++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGlobalAlpha() {
        return 1.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGlobalAlpha(final double globalAlpha) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] setGlobalAlpha(" + globalAlpha + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginPath() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] beginPath()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ellipse(final double x, final double y,
            final double radiusX, final double radiusY,
            final double rotation, final double startAngle, final double endAngle,
            final boolean anticlockwise) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] ellipse()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bezierCurveTo(final double cp1x, final double cp1y,
            final double cp2x, final double cp2y, final double x, final double y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] bezierCurveTo()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void arc(final double x, final double y, final double radius, final double startAngle,
            final double endAngle, final boolean anticlockwise) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] arc()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearRect(final double x, final double y, final double w, final double h) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] clearRect(" + x + ", " + y + ", " + w + ", " + h + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawImage(final ImageReader imageReader,
            final int sx, final int sy, final Integer sWidth, final Integer sHeight,
            final int dx, final int dy, final Integer dWidth, final Integer dHeight) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] drawImage(" + sx + ", " + sy + ", " + sWidth + ", " + sHeight
                    + "," + dx + ", " + dy + ", " + dWidth + ", " + dHeight + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encodeToString(final String type) throws IOException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] fill()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillRect(final int x, final int y, final int w, final int h) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] fillRect(" + x + ", "  + y + ", "  + w + ", "  + h + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillText(final String text, final double x, final double y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] fillText('" + text + "', "  + x + ", "  + y + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getBytes(final int width, final int height, final int sx, final int sy) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] getBytes(" + width + ", " + height + ", " + sx + ", " + sy + ")");
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lineTo(final double x, final double y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] lineTo(" + x + ", " + y + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveTo(final double x, final double y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] moveTo(" + x + ", " + y + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putImageData(final ImageData imageData,
            final int dx, final int dy, final int dirtyX, final int dirtyY,
            final int dirtyWidth, final int dirtyHeight) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] putImageData()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quadraticCurveTo(final double cpx, final double cpy,
                    final double x, final double y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] quadraticCurveTo()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rect(final double x, final double y, final double w, final double h) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] rect()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFillStyle(final String fillStyle) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] setFillStyle(" + fillStyle + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStrokeStyle(final String strokeStyle) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] setStrokeStyle(" + strokeStyle + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLineWidth() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restore() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] restore()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rotate(final double angle) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] rotate()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] save()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLineWidth(final int lineWidth) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] setLineWidth(" + lineWidth + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTransform(final double m11, final double m12,
                    final double m21, final double m22, final double dx, final double dy) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] setTransform("
                        + m11 + ", "  + m12 + ", "  + m21 + ", "  + m22 + ", "  + dx + ", "  + dy + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stroke() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] stroke()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void strokeRect(final int x, final int y, final int w, final int h) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] strokeRect(" + x + ", "  + y + ", "  + w + ", "  + h + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transform(final double m11, final double m12,
                    final double m21, final double m22, final double dx, final double dy) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] transform()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(final int x, final int y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] translate()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clip(final RenderingBackend.WindingRule windingRule,
            final com.gargoylesoftware.htmlunit.javascript.host.canvas.Path2D path) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] clip(" + windingRule + ", " + path + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closePath() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] closePath()");
        }
    }
}
