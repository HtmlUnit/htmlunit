/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.platform.canvas.rendering;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.platform.image.ImageIOImageData;
import org.htmlunit.util.StringUtils;

/**
 * The default implementation of {@link RenderingBackend}.
 *
 * @author Ronald Brill
 */
public class AwtRenderingBackend implements RenderingBackend {

    private static final Log LOG = LogFactory.getLog(AwtRenderingBackend.class);
    private static int ID_GENERATOR_;

    private static final Map<String, Color> KNOWN_COLORS = new HashMap<>();

    private final int id_;
    private final BufferedImage image_;
    private final Graphics2D graphics2D_;

    private AffineTransform transformation_;
    private float globalAlpha_;
    private int lineWidth_;
    private Color fillColor_;
    private Color strokeColor_;

    private final List<Path2D> subPaths_;
    private final Deque<SaveState> savedStates_;

    static {
        // see https://developer.mozilla.org/en-US/docs/Web/CSS/color_value

        // CSS Level 1
        KNOWN_COLORS.put("black", Color.decode("#000000"));
        KNOWN_COLORS.put("silver", Color.decode("#c0c0c0"));
        KNOWN_COLORS.put("gray", Color.decode("#808080"));
        KNOWN_COLORS.put("white", Color.decode("#ffffff"));
        KNOWN_COLORS.put("maroon", Color.decode("#800000"));
        KNOWN_COLORS.put("red", Color.decode("#ff0000"));
        KNOWN_COLORS.put("purple", Color.decode("#800080"));
        KNOWN_COLORS.put("fuchsia", Color.decode("#ff00ff"));
        KNOWN_COLORS.put("green", Color.decode("#008000"));
        KNOWN_COLORS.put("lime", Color.decode("#00ff00"));
        KNOWN_COLORS.put("olive", Color.decode("#808000"));
        KNOWN_COLORS.put("yellow", Color.decode("#ffff00"));
        KNOWN_COLORS.put("navy", Color.decode("#000080"));
        KNOWN_COLORS.put("blue", Color.decode("#0000ff"));
        KNOWN_COLORS.put("teal", Color.decode("#008080"));
        KNOWN_COLORS.put("aqua", Color.decode("#00ffff"));

        // CSS Level 2 (Revision 1)
        KNOWN_COLORS.put("orange", Color.decode("#ffa500"));

        // CSS Color Module Level 3
        KNOWN_COLORS.put("aliceblue", Color.decode("#f0f8ff"));
        KNOWN_COLORS.put("antiquewhite", Color.decode("#faebd7"));
        KNOWN_COLORS.put("aquamarine", Color.decode("#7fffd4"));
        KNOWN_COLORS.put("azure", Color.decode("#f0ffff"));
        KNOWN_COLORS.put("beige", Color.decode("#f5f5dc"));
        KNOWN_COLORS.put("bisque", Color.decode("#ffe4c4"));
        KNOWN_COLORS.put("blanchedalmond", Color.decode("#ffebcd"));
        KNOWN_COLORS.put("blueviolet", Color.decode("#8a2be2"));
        KNOWN_COLORS.put("brown", Color.decode("#a52a2a"));
        KNOWN_COLORS.put("burlywood", Color.decode("#deb887"));
        KNOWN_COLORS.put("cadetblue", Color.decode("#5f9ea0"));
        KNOWN_COLORS.put("chartreuse", Color.decode("#7fff00"));
        KNOWN_COLORS.put("chocolate", Color.decode("#d2691e"));
        KNOWN_COLORS.put("coral", Color.decode("#ff7f50"));
        KNOWN_COLORS.put("cornflowerblue", Color.decode("#6495ed"));
        KNOWN_COLORS.put("cornsilk", Color.decode("#fff8dc"));
        KNOWN_COLORS.put("crimson", Color.decode("#dc143c"));
        KNOWN_COLORS.put("cyan", Color.decode("#00ffff")); // synonym of aqua
        KNOWN_COLORS.put("darkblue", Color.decode("#00008b"));
        KNOWN_COLORS.put("darkcyan", Color.decode("#008b8b"));
        KNOWN_COLORS.put("darkgoldenrod", Color.decode("#b8860b"));
        KNOWN_COLORS.put("darkgray", Color.decode("#a9a9a9"));
        KNOWN_COLORS.put("darkgreen", Color.decode("#006400"));
        KNOWN_COLORS.put("darkgrey", Color.decode("#a9a9a9"));
        KNOWN_COLORS.put("darkkhaki", Color.decode("#bdb76b"));
        KNOWN_COLORS.put("darkmagenta", Color.decode("#8b008b"));
        KNOWN_COLORS.put("darkolivegreen", Color.decode("#556b2f"));
        KNOWN_COLORS.put("darkorange", Color.decode("#ff8c00"));
        KNOWN_COLORS.put("darkorchid", Color.decode("#9932cc"));
        KNOWN_COLORS.put("darkred", Color.decode("#8b0000"));
        KNOWN_COLORS.put("darksalmon", Color.decode("#e9967a"));
        KNOWN_COLORS.put("darkseagreen", Color.decode("#8fbc8f"));
        KNOWN_COLORS.put("darkslateblue", Color.decode("#483d8b"));
        KNOWN_COLORS.put("darkslategray", Color.decode("#2f4f4f"));
        KNOWN_COLORS.put("darkslategrey", Color.decode("#2f4f4f"));
        KNOWN_COLORS.put("darkturquoise", Color.decode("#00ced1"));
        KNOWN_COLORS.put("darkviolet", Color.decode("#9400d3"));
        KNOWN_COLORS.put("deeppink", Color.decode("#ff1493"));
        KNOWN_COLORS.put("deepskyblue", Color.decode("#00bfff"));
        KNOWN_COLORS.put("dimgray", Color.decode("#696969"));
        KNOWN_COLORS.put("dimgrey", Color.decode("#696969"));
        KNOWN_COLORS.put("dodgerblue", Color.decode("#1e90ff"));
        KNOWN_COLORS.put("firebrick", Color.decode("#b22222"));
        KNOWN_COLORS.put("floralwhite", Color.decode("#fffaf0"));
        KNOWN_COLORS.put("forestgreen", Color.decode("#228b22"));
        KNOWN_COLORS.put("gainsboro", Color.decode("#dcdcdc"));
        KNOWN_COLORS.put("ghostwhite", Color.decode("#f8f8ff"));
        KNOWN_COLORS.put("gold", Color.decode("#ffd700"));
        KNOWN_COLORS.put("goldenrod", Color.decode("#daa520"));
        KNOWN_COLORS.put("greenyellow", Color.decode("#adff2f"));
        KNOWN_COLORS.put("grey", Color.decode("#808080"));
        KNOWN_COLORS.put("honeydew", Color.decode("#f0fff0"));
        KNOWN_COLORS.put("hotpink", Color.decode("#ff69b4"));
        KNOWN_COLORS.put("indianred", Color.decode("#cd5c5c"));
        KNOWN_COLORS.put("indigo", Color.decode("#4b0082"));
        KNOWN_COLORS.put("ivory", Color.decode("#fffff0"));
        KNOWN_COLORS.put("khaki", Color.decode("#f0e68c"));
        KNOWN_COLORS.put("lavender", Color.decode("#e6e6fa"));
        KNOWN_COLORS.put("lavenderblush", Color.decode("#fff0f5"));
        KNOWN_COLORS.put("lawngreen", Color.decode("#7cfc00"));
        KNOWN_COLORS.put("lemonchiffon", Color.decode("#fffacd"));
        KNOWN_COLORS.put("lightblue", Color.decode("#add8e6"));
        KNOWN_COLORS.put("lightcoral", Color.decode("#f08080"));
        KNOWN_COLORS.put("lightcyan", Color.decode("#e0ffff"));
        KNOWN_COLORS.put("lightgoldenrodyellow", Color.decode("#fafad2"));
        KNOWN_COLORS.put("lightgray", Color.decode("#d3d3d3"));
        KNOWN_COLORS.put("lightgreen", Color.decode("#90ee90"));
        KNOWN_COLORS.put("lightgrey", Color.decode("#d3d3d3"));
        KNOWN_COLORS.put("lightpink", Color.decode("#ffb6c1"));
        KNOWN_COLORS.put("lightsalmon", Color.decode("#ffa07a"));
        KNOWN_COLORS.put("lightseagreen", Color.decode("#20b2aa"));
        KNOWN_COLORS.put("lightskyblue", Color.decode("#87cefa"));
        KNOWN_COLORS.put("lightslategray", Color.decode("#778899"));
        KNOWN_COLORS.put("lightslategrey", Color.decode("#778899"));
        KNOWN_COLORS.put("lightsteelblue", Color.decode("#b0c4de"));
        KNOWN_COLORS.put("lightyellow", Color.decode("#ffffe0"));
        KNOWN_COLORS.put("limegreen", Color.decode("#32cd32"));
        KNOWN_COLORS.put("linen", Color.decode("#faf0e6"));
        KNOWN_COLORS.put("magenta", Color.decode("#ff00ff")); // synonym of fuchsia
        KNOWN_COLORS.put("mediumaquamarine", Color.decode("#66cdaa"));
        KNOWN_COLORS.put("mediumblue", Color.decode("#0000cd"));
        KNOWN_COLORS.put("mediumorchid", Color.decode("#ba55d3"));
        KNOWN_COLORS.put("mediumpurple", Color.decode("#9370db"));
        KNOWN_COLORS.put("mediumseagreen", Color.decode("#3cb371"));
        KNOWN_COLORS.put("mediumslateblue", Color.decode("#7b68ee"));
        KNOWN_COLORS.put("mediumspringgreen", Color.decode("#00fa9a"));
        KNOWN_COLORS.put("mediumturquoise", Color.decode("#48d1cc"));
        KNOWN_COLORS.put("mediumvioletred", Color.decode("#c71585"));
        KNOWN_COLORS.put("midnightblue", Color.decode("#191970"));
        KNOWN_COLORS.put("mintcream", Color.decode("#f5fffa"));
        KNOWN_COLORS.put("mistyrose", Color.decode("#ffe4e1"));
        KNOWN_COLORS.put("moccasin", Color.decode("#ffe4b5"));
        KNOWN_COLORS.put("navajowhite", Color.decode("#ffdead"));
        KNOWN_COLORS.put("oldlace", Color.decode("#fdf5e6"));
        KNOWN_COLORS.put("olivedrab", Color.decode("#6b8e23"));
        KNOWN_COLORS.put("orangered", Color.decode("#ff4500"));
        KNOWN_COLORS.put("orchid", Color.decode("#da70d6"));
        KNOWN_COLORS.put("palegoldenrod", Color.decode("#eee8aa"));
        KNOWN_COLORS.put("palegreen", Color.decode("#98fb98"));
        KNOWN_COLORS.put("paleturquoise", Color.decode("#afeeee"));
        KNOWN_COLORS.put("palevioletred", Color.decode("#db7093"));
        KNOWN_COLORS.put("papayawhip", Color.decode("#ffefd5"));
        KNOWN_COLORS.put("peachpuff", Color.decode("#ffdab9"));
        KNOWN_COLORS.put("peru", Color.decode("#cd853f"));
        KNOWN_COLORS.put("pink", Color.decode("#ffc0cb"));
        KNOWN_COLORS.put("plum", Color.decode("#dda0dd"));
        KNOWN_COLORS.put("powderblue", Color.decode("#b0e0e6"));
        KNOWN_COLORS.put("rosybrown", Color.decode("#bc8f8f"));
        KNOWN_COLORS.put("royalblue", Color.decode("#4169e1"));
        KNOWN_COLORS.put("saddlebrown", Color.decode("#8b4513"));
        KNOWN_COLORS.put("salmon", Color.decode("#fa8072"));
        KNOWN_COLORS.put("sandybrown", Color.decode("#f4a460"));
        KNOWN_COLORS.put("seagreen", Color.decode("#2e8b57"));
        KNOWN_COLORS.put("seashell", Color.decode("#fff5ee"));
        KNOWN_COLORS.put("sienna", Color.decode("#a0522d"));
        KNOWN_COLORS.put("skyblue", Color.decode("#87ceeb"));
        KNOWN_COLORS.put("slateblue", Color.decode("#6a5acd"));
        KNOWN_COLORS.put("slategray", Color.decode("#708090"));
        KNOWN_COLORS.put("slategrey", Color.decode("#708090"));
        KNOWN_COLORS.put("snow", Color.decode("#fffafa"));
        KNOWN_COLORS.put("springgreen", Color.decode("#00ff7f"));
        KNOWN_COLORS.put("steelblue", Color.decode("#4682b4"));
        KNOWN_COLORS.put("tan", Color.decode("#d2b48c"));
        KNOWN_COLORS.put("thistle", Color.decode("#d8bfd8"));
        KNOWN_COLORS.put("tomato", Color.decode("#ff6347"));
        KNOWN_COLORS.put("turquoise", Color.decode("#40e0d0"));
        KNOWN_COLORS.put("violet", Color.decode("#ee82ee"));
        KNOWN_COLORS.put("wheat", Color.decode("#f5deb3"));
        KNOWN_COLORS.put("whitesmoke", Color.decode("#f5f5f5"));
        KNOWN_COLORS.put("yellowgreen", Color.decode("#9acd32"));
        // CSS Color Module Level 4
        KNOWN_COLORS.put("rebeccapurple", Color.decode("#663399"));
    }

    private static synchronized int nextId() {
        return ID_GENERATOR_++;
    }

    /**
     * Constructor.
     * @param imageWidth the width
     * @param imageHeight the height
     */
    public AwtRenderingBackend(final int imageWidth, final int imageHeight) {
        id_ = nextId();
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] AwtRenderingBackend(" + imageWidth + ", " + imageHeight + ")");
        }

        image_ = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        graphics2D_ = image_.createGraphics();

        graphics2D_.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D_.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D_.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // reset
        fillColor_ = Color.black;
        strokeColor_ = Color.black;
        lineWidth_ = 1;
        transformation_ = new AffineTransform();
        updateGlobalAlpha(1f);
        graphics2D_.setClip(null);

        final Font font = new Font("SansSerif", Font.PLAIN, 10);
        graphics2D_.setFont(font);

        graphics2D_.setBackground(new Color(0f, 0f, 0f, 0f));
        graphics2D_.setColor(Color.black);
        graphics2D_.clearRect(0, 0, imageWidth, imageHeight);

        subPaths_ = new ArrayList<>();
        savedStates_ = new ArrayDeque<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGlobalAlpha() {
        return globalAlpha_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGlobalAlpha(final double globalAlpha) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] setGlobalAlpha(" + globalAlpha + ")");
        }

        if (globalAlpha >= 0 && globalAlpha <= 1) {
            updateGlobalAlpha((float) globalAlpha);
        }
    }

    private void updateGlobalAlpha(final float globalAlpha) {
        globalAlpha_ = globalAlpha;
        final AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, globalAlpha_);
        graphics2D_.setComposite(composite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginPath() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] beginPath()");
        }
        subPaths_.clear();
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

        final Path2D subPath = getCurrentSubPath();
        if (subPath != null) {
            final Point2D p = transformation_.transform(new Point2D.Double(x, y), null);
            final double startAngleDegree = 360 - (startAngle * 180 / Math.PI);
            final double endAngleDegree = 360 - (endAngle * 180 / Math.PI);

            double extendAngle = startAngleDegree - endAngleDegree;
            extendAngle = Math.min(360, Math.abs(extendAngle));
            if (anticlockwise && extendAngle < 360) {
                extendAngle = extendAngle - 360;
            }

            final AffineTransform transformation = new AffineTransform();
            transformation.rotate(rotation, p.getX(), p.getY());
            final Arc2D arc = new Arc2D.Double(p.getX() - radiusX, p.getY() - radiusY, radiusX * 2, radiusY * 2,
                                            startAngleDegree, extendAngle * -1, Arc2D.OPEN);
            subPath.append(transformation.createTransformedShape(arc), false);
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] arc()");
        }

        final Path2D subPath = getCurrentSubPath();
        if (subPath != null) {
            final Point2D p = transformation_.transform(new Point2D.Double(x, y), null);
            final double startAngleDegree = 360 - (startAngle * 180 / Math.PI);
            final double endAngleDegree = 360 - (endAngle * 180 / Math.PI);

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
    public void clearRect(final double x, final double y, final double w, final double h) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] clearRect(" + x + ", " + y + ", " + w + ", " + h + ")");
        }

        final Composite saved = graphics2D_.getComposite();

        graphics2D_.setColor(Color.BLACK);
        graphics2D_.setComposite(AlphaComposite.Clear); // overpaint
        final Rectangle2D rect = new Rectangle2D.Double(x, y, w, h);
        graphics2D_.fill(transformation_.createTransformedShape(rect));

        graphics2D_.setComposite(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawImage(final org.htmlunit.platform.image.ImageData imageData,
            final int sx, final int sy, final Integer sWidth, final Integer sHeight,
            final int dx, final int dy, final Integer dWidth, final Integer dHeight) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] drawImage(" + sx + ", " + sy + ", " + sWidth + ", " + sHeight
                    + "," + dx + ", " + dy + ", " + dWidth + ", " + dHeight + ")");
        }

        try {
            final ImageReader imageReader = ((ImageIOImageData) imageData).getImageReader();

            if (imageReader.getNumImages(true) != 0) {
                final BufferedImage img = imageReader.read(0);

                final AffineTransform savedTransform = graphics2D_.getTransform();
                try {
                    graphics2D_.setTransform(transformation_);
                    graphics2D_.setColor(fillColor_);

                    final int sx2;
                    if (sWidth == null) {
                        sx2 = sx + img.getWidth();
                    }
                    else {
                        sx2 = sx + sWidth;
                    }

                    final int sy2;
                    if (sHeight == null) {
                        sy2 = sy + img.getHeight();
                    }
                    else {
                        sy2 = sy + sHeight;
                    }

                    int dx1 = dx;
                    final int dx2;
                    if (dWidth == null) {
                        dx2 = dx + img.getWidth();
                    }
                    else {
                        if (dWidth < 0) {
                            dx1 = dx1 + dWidth;
                            dx2 = dx1 - dWidth;
                        }
                        else {
                            dx2 = dx1 + dWidth;
                        }
                    }

                    int dy1 = dy;
                    final int dy2;
                    if (dHeight == null) {
                        dy2 = dy + img.getHeight();
                    }
                    else {
                        if (dHeight < 0) {
                            dy1 = dy1 + dHeight;
                            dy2 = dy1 - dHeight;
                        }
                        else {
                            dy2 = dy1 + dHeight;
                        }
                    }

                    final Object done = new Object();
                    final ImageObserver imageObserver = (img1, flags, x, y, width, height) -> {

                        if ((flags & ImageObserver.ALLBITS) == ImageObserver.ALLBITS) {
                            return true;
                        }

                        if ((flags & ImageObserver.ABORT) == ImageObserver.ABORT
                                || (flags & ImageObserver.ERROR) == ImageObserver.ERROR) {
                            return true;
                        }

                        synchronized (done) {
                            done.notify();
                        }

                        return false;
                    };

                    synchronized (done) {
                        final boolean completelyLoaded =
                                graphics2D_.drawImage(img, dx1, dy1, dx2, dy2, sx, sy, sx2, sy2, imageObserver);
                        if (!completelyLoaded) {
                            while (true) {
                                try {
                                    done.wait(4 * 1000); // max 4s
                                    break;
                                }
                                catch (final InterruptedException e) {
                                    LOG.error("[" + id_ + "] AwtRenderingBackend interrupted "
                                            + "while waiting for drawImage to finish.", e);

                                    // restore interrupted status
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                    }
                }
                finally {
                    graphics2D_.setTransform(savedTransform);
                }
            }
        }
        catch (final ClassCastException e) {
            LOG.error("[" + id_ + "] drawImage(..) failed", e);
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
            return new String(Base64.getEncoder().encode(imageBytes), StandardCharsets.US_ASCII);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] fill()");
        }

        graphics2D_.setStroke(new BasicStroke(getLineWidth()));
        graphics2D_.setColor(fillColor_);
        for (final Path2D path2d : subPaths_) {
            graphics2D_.fill(path2d);
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

        graphics2D_.setColor(fillColor_);
        final Rectangle2D rect = new Rectangle2D.Double(x, y, w, h);
        graphics2D_.fill(transformation_.createTransformedShape(rect));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillText(final String text, final double x, final double y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] fillText('" + text + "', "  + x + ", "  + y + ")");
        }

        final AffineTransform savedTransform = graphics2D_.getTransform();
        try {
            graphics2D_.setTransform(transformation_);
            graphics2D_.setColor(fillColor_);
            graphics2D_.drawString(text, (int) x, (int) y);
        }
        finally {
            graphics2D_.setTransform(savedTransform);
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

        final byte[] array = new byte[width * height * 4];
        int index = 0;
        for (int x = sx; x < sx + width; x++) {
            if (x < 0 || x >= image_.getWidth()) {
                array[index++] = (byte) 0;
                array[index++] = (byte) 0;
                array[index++] = (byte) 0;
                array[index++] = (byte) 0;
            }
            else {
                for (int y = sy; y < sy + height; y++) {
                    if (y < 0 || y >= image_.getHeight()) {
                        array[index++] = (byte) 0;
                        array[index++] = (byte) 0;
                        array[index++] = (byte) 0;
                        array[index++] = (byte) 0;
                    }
                    else {
                        final int color = image_.getRGB(x, y);
                        array[index++] = (byte) ((color & 0xff0000) >> 16);
                        array[index++] = (byte) ((color & 0xff00) >> 8);
                        array[index++] = (byte) (color & 0xff);
                        array[index++] = (byte) ((color & 0xff000000) >>> 24);
                    }
                }
            }
        }
        return array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lineTo(final double x, final double y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] lineTo(" + x + ", " + y + ")");
        }

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
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] moveTo(" + x + ", " + y + ")");
        }

        final Path2D subPath = new Path2D.Double();
        final Point2D p = transformation_.transform(new Point2D.Double(x, y), null);
        subPath.moveTo(p.getX(), p.getY());
        subPaths_.add(subPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putImageData(final byte[] imageDataBytes,
            final int imageDataWidth, final int imageDataHeight,
            final int dx, final int dy, final int dirtyX, final int dirtyY,
            final int dirtyWidth, final int dirtyHeight) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] putImageData()");
        }

        final Color orgColor = graphics2D_.getColor();

        final int width = dx + imageDataWidth;
        final int height = dy + imageDataHeight;
        final int imageWidth = dirtyX + dirtyWidth;
        final int imageHeight = dirtyY + dirtyHeight;

        int byteIdx = 0;
        int imageX = 0;
        int imageY = 0;
        for (int insertY = dy; insertY < height; insertY++) {
            for (int insertX = dx; insertX < width; insertX++) {
                if (0 <= insertX && insertX < image_.getWidth()
                        && 0 <= insertY && insertY < image_.getHeight()
                        && dirtyX <= imageX && imageX < imageWidth
                        && dirtyY <= imageY && imageY < imageHeight) {
                    final int r = imageDataBytes[byteIdx++] & 0xFF;
                    final int g = imageDataBytes[byteIdx++] & 0xFF;
                    final int b = imageDataBytes[byteIdx++] & 0xFF;
                    final int a = imageDataBytes[byteIdx++] & 0xFF;
                    final Color color = new Color(r, g, b, a);
                    graphics2D_.setColor(color);
                    graphics2D_.drawLine(insertX, insertY, insertX, insertY);
                }
                else {
                    byteIdx += 4;
                }

                imageX++;
                if (imageX == imageDataWidth) {
                    imageX = 0;
                    imageY++;
                }
            }
        }

        graphics2D_.setColor(orgColor);
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] rect()");
        }

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
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] setFillStyle(" + fillStyle + ")");
        }

        final Color color = extractColor(fillStyle);
        if (color != null) {
            fillColor_ = color;
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

        final Color color = extractColor(strokeStyle);
        if (color != null) {
            strokeColor_ = color;
        }
    }

    private static Color extractColor(final String style) {
        final String tmpStyle = style.replaceAll("\\s", "");

        Color color = toAwtColor(StringUtils.findColorRGB(tmpStyle));
        if (color == null) {
            color = toAwtColor(StringUtils.findColorRGBA(tmpStyle));
        }
        if (color == null) {
            color = toAwtColor(StringUtils.findColorHSL(tmpStyle));
        }

        if (color == null) {
            if (tmpStyle.length() > 0 && tmpStyle.charAt(0) == '#') {
                color = toAwtColor(StringUtils.asColorHexadecimal(tmpStyle));
            }
            else {
                color = KNOWN_COLORS.get(tmpStyle.toLowerCase(Locale.ROOT));
            }
        }
        return color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLineWidth() {
        return lineWidth_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restore() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] restore()");
        }

        if (savedStates_.isEmpty()) {
            return;
        }

        savedStates_.pop().applyOn(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rotate(final double angle) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] rotate()");
        }

        transformation_.rotate(angle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] save()");
        }

        savedStates_.push(new SaveState(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLineWidth(final int lineWidth) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] setLineWidth(" + lineWidth + ")");
        }

        lineWidth_ = lineWidth;
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

        transformation_ = new AffineTransform(m11, m12, m21, m22, dx, dy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stroke() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] stroke()");
        }

        graphics2D_.setStroke(new BasicStroke(getLineWidth()));
        graphics2D_.setColor(strokeColor_);
        for (final Path2D path2d : subPaths_) {
            graphics2D_.draw(path2d);
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

        graphics2D_.setColor(strokeColor_);
        final Rectangle2D rect = new Rectangle2D.Double(x, y, w, h);
        graphics2D_.draw(transformation_.createTransformedShape(rect));
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

        transformation_.concatenate(new AffineTransform(m11, m12, m21, m22, dx, dy));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(final int x, final int y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] translate()");
        }

        transformation_.translate(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clip(final RenderingBackend.WindingRule windingRule,
            final org.htmlunit.javascript.host.canvas.Path2D path) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] clip(" + windingRule + ", " + path + ")");
        }

        if (path == null && subPaths_.isEmpty()) {
            graphics2D_.setClip(null);
            return;
        }

        final Path2D currentPath;
        if (path == null) {
            currentPath = subPaths_.get(subPaths_.size() - 1);
        }
        else {
            // currentPath = path.getPath2D();
            currentPath = null;
        }
        currentPath.closePath();

        if (windingRule == WindingRule.NON_ZERO) {
            currentPath.setWindingRule(Path2D.WIND_NON_ZERO);
        }
        else {
            currentPath.setWindingRule(Path2D.WIND_EVEN_ODD);
        }

        graphics2D_.clip(currentPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closePath() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] closePath()");
        }

        if (subPaths_.isEmpty()) {
            return;
        }
        subPaths_.get(subPaths_.size() - 1).closePath();
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
        private final AffineTransform transformation_;
        private final float globalAlpha_;
        private final int lineWidth_;
        private final Color fillColor_;
        private final Color strokeColor_;
        private final Shape clip_;

        SaveState(final AwtRenderingBackend backend) {
            transformation_ = backend.transformation_;
            globalAlpha_ = backend.globalAlpha_;
            lineWidth_ = backend.lineWidth_;
            fillColor_ = backend.fillColor_;
            strokeColor_ = backend.strokeColor_;

            clip_ = backend.graphics2D_.getClip();
        }

        void applyOn(final AwtRenderingBackend backend) {
            backend.transformation_ = transformation_;
            backend.globalAlpha_ = globalAlpha_;
            backend.lineWidth_ = lineWidth_;
            backend.fillColor_ = fillColor_;
            backend.strokeColor_ = strokeColor_;

            backend.graphics2D_.setClip(clip_);
        }
    }

    private static Color toAwtColor(final org.htmlunit.html.impl.Color color) {
        if (color == null) {
            return null;
        }
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}
