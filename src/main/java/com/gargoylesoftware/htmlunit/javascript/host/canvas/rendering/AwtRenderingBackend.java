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
package com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default implementation of {@link RenderingBackend}.
 *
 * @author Ronald Brill
 */
public class AwtRenderingBackend implements RenderingBackend {

    private static final Log LOG = LogFactory.getLog(AwtRenderingBackend.class);

    private final BufferedImage image_;
    private final Graphics2D graphics2D_;

    private AffineTransform transformation_;
    private int lineWidth_;
    private Color fillColor_;
    private Color strokeColor_;

    private List<Path2D> subPaths_;
    private Deque<SaveState> savedStates_;

    /**
     * Constructor.
     * @param imageWidth the width
     * @param imageHeight the height
     */
    public AwtRenderingBackend(final int imageWidth, final int imageHeight) {
        image_ = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        graphics2D_ = image_.createGraphics();

        graphics2D_.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D_.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D_.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        reset();

        graphics2D_.setBackground(new Color(0f, 0f, 0f, 0f));
        graphics2D_.setColor(Color.black);
        graphics2D_.clearRect(0, 0, imageWidth, imageHeight);

        subPaths_ = new ArrayList<>();
        savedStates_ = new ArrayDeque<>();
    }

    private void reset() {
        fillColor_ = Color.black;
        strokeColor_ = Color.black;
        lineWidth_ = 1;
        transformation_ = new AffineTransform();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginPath() {
        subPaths_.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bezierCurveTo(final double cp1x, final double cp1y,
            final double cp2x, final double cp2y, final double x, final double y) {
        final Path2D subPath = getCurrentSubPath();
        if (subPath != null) {
            final Point2D cp1 = transformation_.transform(new Point2D.Double(cp1x, cp1y), null);
            final Point2D cp2 = transformation_.transform(new Point2D.Double(cp2x, cp2y), null);
            final Point2D p = transformation_.transform(new Point2D.Double(x, y), null);
            subPath.curveTo(cp1.getX(), cp1.getY(), cp2.getX(), cp2.getY(), p.getX(), p.getY());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void arc(final double x, final double y, final double radius, final double startAngle,
            final double endAngle, final boolean anticlockwise) {
        final Path2D subPath = getCurrentSubPath();
        if (subPath != null) {
            final Point2D p = transformation_.transform(new Point2D.Double(x, y), null);
            final double startAngleDegree = startAngle * 180 / Math.PI;
            final double endAngleDegree = endAngle * 180 / Math.PI;

            double extendAngle = startAngleDegree - endAngleDegree;
            extendAngle = Math.min(360, Math.abs(extendAngle));
            if (anticlockwise && extendAngle < 360) {
                extendAngle = extendAngle - 360;
            }
            final Arc2D arc = new Arc2D.Double(p.getX() - radius, p.getY() - radius, radius * 2, radius * 2,
                                            startAngleDegree, extendAngle * -1, Arc2D.OPEN);
            subPath.append(arc, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearRect(final int x, final int y, final int w, final int h) {
        graphics2D_.setTransform(transformation_);
        graphics2D_.setColor(fillColor_);
        graphics2D_.clearRect(x, y, w, h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawImage(final ImageReader imageReader, final int dxI, final int dyI) throws IOException {
        if (imageReader.getNumImages(true) != 0) {
            final BufferedImage img = imageReader.read(0);
            graphics2D_.setTransform(transformation_);
            graphics2D_.setColor(fillColor_);
            graphics2D_.drawImage(img, dxI, dyI, image_.getWidth(), image_.getHeight(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encodeToString(final String type) throws IOException {
        String imageType = type;
        if (imageType != null && imageType.startsWith("image/")) {
            imageType = imageType.substring(6);
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(image_, imageType, bos);

            final byte[] imageBytes = bos.toByteArray();
            return new String(new Base64().encode(imageBytes), StandardCharsets.US_ASCII);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill() {
        graphics2D_.setTransform(new AffineTransform());
        graphics2D_.setStroke(new BasicStroke(getLineWidth()));
        graphics2D_.setColor(fillColor_);
        for (Path2D path2d : subPaths_) {
            graphics2D_.fill(path2d);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillRect(final int x, final int y, final int w, final int h) {
        graphics2D_.setTransform(transformation_);
        graphics2D_.setColor(fillColor_);
        graphics2D_.fillRect(x, y, w, h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillText(final String text, final int x, final int y) {
        graphics2D_.setTransform(new AffineTransform());

        final FontMetrics metrics = graphics2D_.getFontMetrics();
        final int width = metrics.stringWidth(text);
        final int ascent = metrics.getAscent();

        final float posX = (float) (x - width / 2);
        final float posY = (float) (y + ascent / 2);

        graphics2D_.setTransform(transformation_);
        graphics2D_.setColor(fillColor_);
        graphics2D_.drawString(text, posX, posY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getBytes(final int width, final int height, final int sx, final int sy) {
        final byte[] array = new byte[width * height * 4];
        int index = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int color = image_.getRGB(sx + x, sy + y);
                array[index++] = (byte) ((color & 0xff0000) >> 16);
                array[index++] = (byte) ((color & 0xff00) >> 8);
                array[index++] = (byte) (color & 0xff);
                array[index++] = (byte) ((color & 0xff000000) >>> 24);
            }
        }
        return array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lineTo(final double x, final double y) {
        final Path2D subPath = getCurrentSubPath();
        if (subPath != null) {
            final Point2D p = transformation_.transform(new Point2D.Double(x, y), null);
            subPath.lineTo(p.getX(), p.getY());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveTo(final double x, final double y) {
        final Path2D subPath = new Path2D.Double();
        final Point2D p = transformation_.transform(new Point2D.Double(x, y), null);
        subPath.moveTo(p.getX(), p.getY());
        subPaths_.add(subPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quadraticCurveTo(final double cpx, final double cpy,
                    final double x, final double y) {
        final Path2D subPath = getCurrentSubPath();
        if (subPath != null) {
            final Point2D cp = transformation_.transform(new Point2D.Double(cpx, cpy), null);
            final Point2D p = transformation_.transform(new Point2D.Double(x, y), null);
            subPath.quadTo(cp.getX(), cp.getY(), p.getX(), p.getY());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rect(final double x, final double y, final double w, final double h) {
        final Path2D subPath = getCurrentSubPath();
        if (subPath != null) {
            final Point2D p = transformation_.transform(new Point2D.Double(x, y), null);
            final Rectangle2D rect = new Rectangle2D.Double(p.getX(), p.getY(), w, h);
            subPath.append(rect, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFillStyle(final String fillStyle) {
        final String tmpFillStyle = fillStyle.replaceAll("\\s", "");
        Color color = null;
        if (tmpFillStyle.startsWith("rgb(")) {
            final String[] colors = tmpFillStyle.substring(4, tmpFillStyle.length() - 1).split(",");
            color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
        }
        else if (tmpFillStyle.startsWith("rgba(")) {
            final String[] colors = tmpFillStyle.substring(5, tmpFillStyle.length() - 1).split(",");
            color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]),
                (int) (Float.parseFloat(colors[3]) * 255));
        }
        else if (tmpFillStyle.startsWith("#")) {
            color = Color.decode(tmpFillStyle);
        }
        else {
            try {
                final Field f = Color.class.getField(tmpFillStyle);
                color = (Color) f.get(null);
            }
            catch (final Exception e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Can not find color '" + tmpFillStyle + '\'');
                }
                color = Color.black;
            }
        }
        fillColor_ = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStrokeStyle(final String strokeStyle) {
        final String tmpFillStyle = strokeStyle.replaceAll("\\s", "");
        Color color = null;
        if (tmpFillStyle.startsWith("rgb(")) {
            final String[] colors = tmpFillStyle.substring(4, tmpFillStyle.length() - 1).split(",");
            color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
        }
        else if (tmpFillStyle.startsWith("rgba(")) {
            final String[] colors = tmpFillStyle.substring(5, tmpFillStyle.length() - 1).split(",");
            color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]),
                (int) (Float.parseFloat(colors[3]) * 255));
        }
        else if (tmpFillStyle.startsWith("#")) {
            color = Color.decode(tmpFillStyle);
        }
        else {
            try {
                final Field f = Color.class.getField(tmpFillStyle);
                color = (Color) f.get(null);
            }
            catch (final Exception e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Can not find color '" + tmpFillStyle + '\'');
                }
                color = Color.black;
            }
        }
        strokeColor_ = color;
    }

    /**
     * {@inheritDoc}
     */
    public int getLineWidth() {
        return lineWidth_;
    }

    /**
     * {@inheritDoc}
     */
    public void restore() {
        if (savedStates_.isEmpty()) {
            return;
        }

        savedStates_.pop().applyOn(this);
    }

    /**
     * {@inheritDoc}
     */
    public void rotate(final double angle) {
        transformation_.rotate(angle);
    }

    /**
     * {@inheritDoc}
     */
    public void save() {
        savedStates_.push(new SaveState(this));
        reset();
    }

    /**
     * {@inheritDoc}
     */
    public void setLineWidth(final int lineWidth) {
        lineWidth_ = lineWidth;
    }

    /**
     * {@inheritDoc}
     */
    public void setTransform(final double m11, final double m12,
                    final double m21, final double m22, final double dx, final double dy) {
        transformation_ = new AffineTransform(m11, m12, m21, m22, dx, dy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stroke() {
        graphics2D_.setTransform(new AffineTransform());
        graphics2D_.setStroke(new BasicStroke(getLineWidth()));
        graphics2D_.setColor(strokeColor_);
        for (Path2D path2d : subPaths_) {
            graphics2D_.draw(path2d);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void strokeRect(final int x, final int y, final int w, final int h) {
        graphics2D_.setTransform(transformation_);
        graphics2D_.setColor(strokeColor_);
        graphics2D_.drawRect(x, y, w, h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transform(final double m11, final double m12,
                    final double m21, final double m22, final double dx, final double dy) {
        transformation_.concatenate(new AffineTransform(m11, m12, m21, m22, dx, dy));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(final int x, final int y) {
        transformation_.translate(x, y);
    }

    private Path2D getCurrentSubPath() {
        if (subPaths_.isEmpty()) {
            final Path2D subPath = new Path2D.Double();
            subPaths_.add(subPath);
            return subPath;
        }
        return subPaths_.get(subPaths_.size() - 1);
    }

    private static final class SaveState {
        private AffineTransform transformation_;
        private int lineWidth_;
        private Color fillColor_;
        private Color strokeColor_;

        private SaveState(final AwtRenderingBackend backend) {
            transformation_ = backend.transformation_;
            lineWidth_ = backend.lineWidth_;
            fillColor_ = backend.fillColor_;
            strokeColor_ = backend.strokeColor_;
        }

        private void applyOn(final AwtRenderingBackend backend) {
            backend.transformation_ = transformation_;
            backend.lineWidth_ = lineWidth_;
            backend.fillColor_ = fillColor_;
            backend.strokeColor_ = strokeColor_;
        }
    }
}
