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
package com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.javascript.host.canvas.ImageData;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * The default implementation of {@link RenderingBackend}.
 *
 * @author Ronald Brill
 */
public class AwtRenderingBackend implements RenderingBackend {

    private static final Log LOG = LogFactory.getLog(AwtRenderingBackend.class);
    private static int ID_GENERATOR_ = 0;

    private static final Map<String, Color> knownColors = new HashMap<>();

    private final int id_;
    private final BufferedImage image_;
    private final Graphics2D graphics2D_;

    private AffineTransform transformation_;
    private float globalAlpha_;
    private int lineWidth_;
    private Color fillColor_;
    private Color strokeColor_;

    private List<Path2D> subPaths_;
    private Deque<SaveState> savedStates_;

    static {
        // see https://developer.mozilla.org/en-US/docs/Web/CSS/color_value

        // CSS Level 1
        knownColors.put("black", Color.decode("#000000"));
        knownColors.put("silver", Color.decode("#c0c0c0"));
        knownColors.put("gray", Color.decode("#808080"));
        knownColors.put("white", Color.decode("#ffffff"));
        knownColors.put("maroon", Color.decode("#800000"));
        knownColors.put("red", Color.decode("#ff0000"));
        knownColors.put("purple", Color.decode("#800080"));
        knownColors.put("fuchsia", Color.decode("#ff00ff"));
        knownColors.put("green", Color.decode("#008000"));
        knownColors.put("lime", Color.decode("#00ff00"));
        knownColors.put("olive", Color.decode("#808000"));
        knownColors.put("yellow", Color.decode("#ffff00"));
        knownColors.put("navy", Color.decode("#000080"));
        knownColors.put("blue", Color.decode("#0000ff"));
        knownColors.put("teal", Color.decode("#008080"));
        knownColors.put("aqua", Color.decode("#00ffff"));

        // CSS Level 2 (Revision 1)
        knownColors.put("orange", Color.decode("#ffa500"));

        // CSS Color Module Level 3
        knownColors.put("aliceblue", Color.decode("#f0f8ff"));
        knownColors.put("antiquewhite", Color.decode("#faebd7"));
        knownColors.put("aquamarine", Color.decode("#7fffd4"));
        knownColors.put("azure", Color.decode("#f0ffff"));
        knownColors.put("beige", Color.decode("#f5f5dc"));
        knownColors.put("bisque", Color.decode("#ffe4c4"));
        knownColors.put("blanchedalmond", Color.decode("#ffebcd"));
        knownColors.put("blueviolet", Color.decode("#8a2be2"));
        knownColors.put("brown", Color.decode("#a52a2a"));
        knownColors.put("burlywood", Color.decode("#deb887"));
        knownColors.put("cadetblue", Color.decode("#5f9ea0"));
        knownColors.put("chartreuse", Color.decode("#7fff00"));
        knownColors.put("chocolate", Color.decode("#d2691e"));
        knownColors.put("coral", Color.decode("#ff7f50"));
        knownColors.put("cornflowerblue", Color.decode("#6495ed"));
        knownColors.put("cornsilk", Color.decode("#fff8dc"));
        knownColors.put("crimson", Color.decode("#dc143c"));
        knownColors.put("cyan", Color.decode("#00ffff")); // synonym of aqua
        knownColors.put("darkblue", Color.decode("#00008b"));
        knownColors.put("darkcyan", Color.decode("#008b8b"));
        knownColors.put("darkgoldenrod", Color.decode("#b8860b"));
        knownColors.put("darkgray", Color.decode("#a9a9a9"));
        knownColors.put("darkgreen", Color.decode("#006400"));
        knownColors.put("darkgrey", Color.decode("#a9a9a9"));
        knownColors.put("darkkhaki", Color.decode("#bdb76b"));
        knownColors.put("darkmagenta", Color.decode("#8b008b"));
        knownColors.put("darkolivegreen", Color.decode("#556b2f"));
        knownColors.put("darkorange", Color.decode("#ff8c00"));
        knownColors.put("darkorchid", Color.decode("#9932cc"));
        knownColors.put("darkred", Color.decode("#8b0000"));
        knownColors.put("darksalmon", Color.decode("#e9967a"));
        knownColors.put("darkseagreen", Color.decode("#8fbc8f"));
        knownColors.put("darkslateblue", Color.decode("#483d8b"));
        knownColors.put("darkslategray", Color.decode("#2f4f4f"));
        knownColors.put("darkslategrey", Color.decode("#2f4f4f"));
        knownColors.put("darkturquoise", Color.decode("#00ced1"));
        knownColors.put("darkviolet", Color.decode("#9400d3"));
        knownColors.put("deeppink", Color.decode("#ff1493"));
        knownColors.put("deepskyblue", Color.decode("#00bfff"));
        knownColors.put("dimgray", Color.decode("#696969"));
        knownColors.put("dimgrey", Color.decode("#696969"));
        knownColors.put("dodgerblue", Color.decode("#1e90ff"));
        knownColors.put("firebrick", Color.decode("#b22222"));
        knownColors.put("floralwhite", Color.decode("#fffaf0"));
        knownColors.put("forestgreen", Color.decode("#228b22"));
        knownColors.put("gainsboro", Color.decode("#dcdcdc"));
        knownColors.put("ghostwhite", Color.decode("#f8f8ff"));
        knownColors.put("gold", Color.decode("#ffd700"));
        knownColors.put("goldenrod", Color.decode("#daa520"));
        knownColors.put("greenyellow", Color.decode("#adff2f"));
        knownColors.put("grey", Color.decode("#808080"));
        knownColors.put("honeydew", Color.decode("#f0fff0"));
        knownColors.put("hotpink", Color.decode("#ff69b4"));
        knownColors.put("indianred", Color.decode("#cd5c5c"));
        knownColors.put("indigo", Color.decode("#4b0082"));
        knownColors.put("ivory", Color.decode("#fffff0"));
        knownColors.put("khaki", Color.decode("#f0e68c"));
        knownColors.put("lavender", Color.decode("#e6e6fa"));
        knownColors.put("lavenderblush", Color.decode("#fff0f5"));
        knownColors.put("lawngreen", Color.decode("#7cfc00"));
        knownColors.put("lemonchiffon", Color.decode("#fffacd"));
        knownColors.put("lightblue", Color.decode("#add8e6"));
        knownColors.put("lightcoral", Color.decode("#f08080"));
        knownColors.put("lightcyan", Color.decode("#e0ffff"));
        knownColors.put("lightgoldenrodyellow", Color.decode("#fafad2"));
        knownColors.put("lightgray", Color.decode("#d3d3d3"));
        knownColors.put("lightgreen", Color.decode("#90ee90"));
        knownColors.put("lightgrey", Color.decode("#d3d3d3"));
        knownColors.put("lightpink", Color.decode("#ffb6c1"));
        knownColors.put("lightsalmon", Color.decode("#ffa07a"));
        knownColors.put("lightseagreen", Color.decode("#20b2aa"));
        knownColors.put("lightskyblue", Color.decode("#87cefa"));
        knownColors.put("lightslategray", Color.decode("#778899"));
        knownColors.put("lightslategrey", Color.decode("#778899"));
        knownColors.put("lightsteelblue", Color.decode("#b0c4de"));
        knownColors.put("lightyellow", Color.decode("#ffffe0"));
        knownColors.put("limegreen", Color.decode("#32cd32"));
        knownColors.put("linen", Color.decode("#faf0e6"));
        knownColors.put("magenta", Color.decode("#ff00ff")); // synonym of fuchsia
        knownColors.put("mediumaquamarine", Color.decode("#66cdaa"));
        knownColors.put("mediumblue", Color.decode("#0000cd"));
        knownColors.put("mediumorchid", Color.decode("#ba55d3"));
        knownColors.put("mediumpurple", Color.decode("#9370db"));
        knownColors.put("mediumseagreen", Color.decode("#3cb371"));
        knownColors.put("mediumslateblue", Color.decode("#7b68ee"));
        knownColors.put("mediumspringgreen", Color.decode("#00fa9a"));
        knownColors.put("mediumturquoise", Color.decode("#48d1cc"));
        knownColors.put("mediumvioletred", Color.decode("#c71585"));
        knownColors.put("midnightblue", Color.decode("#191970"));
        knownColors.put("mintcream", Color.decode("#f5fffa"));
        knownColors.put("mistyrose", Color.decode("#ffe4e1"));
        knownColors.put("moccasin", Color.decode("#ffe4b5"));
        knownColors.put("navajowhite", Color.decode("#ffdead"));
        knownColors.put("oldlace", Color.decode("#fdf5e6"));
        knownColors.put("olivedrab", Color.decode("#6b8e23"));
        knownColors.put("orangered", Color.decode("#ff4500"));
        knownColors.put("orchid", Color.decode("#da70d6"));
        knownColors.put("palegoldenrod", Color.decode("#eee8aa"));
        knownColors.put("palegreen", Color.decode("#98fb98"));
        knownColors.put("paleturquoise", Color.decode("#afeeee"));
        knownColors.put("palevioletred", Color.decode("#db7093"));
        knownColors.put("papayawhip", Color.decode("#ffefd5"));
        knownColors.put("peachpuff", Color.decode("#ffdab9"));
        knownColors.put("peru", Color.decode("#cd853f"));
        knownColors.put("pink", Color.decode("#ffc0cb"));
        knownColors.put("plum", Color.decode("#dda0dd"));
        knownColors.put("powderblue", Color.decode("#b0e0e6"));
        knownColors.put("rosybrown", Color.decode("#bc8f8f"));
        knownColors.put("royalblue", Color.decode("#4169e1"));
        knownColors.put("saddlebrown", Color.decode("#8b4513"));
        knownColors.put("salmon", Color.decode("#fa8072"));
        knownColors.put("sandybrown", Color.decode("#f4a460"));
        knownColors.put("seagreen", Color.decode("#2e8b57"));
        knownColors.put("seashell", Color.decode("#fff5ee"));
        knownColors.put("sienna", Color.decode("#a0522d"));
        knownColors.put("skyblue", Color.decode("#87ceeb"));
        knownColors.put("slateblue", Color.decode("#6a5acd"));
        knownColors.put("slategray", Color.decode("#708090"));
        knownColors.put("slategrey", Color.decode("#708090"));
        knownColors.put("snow", Color.decode("#fffafa"));
        knownColors.put("springgreen", Color.decode("#00ff7f"));
        knownColors.put("steelblue", Color.decode("#4682b4"));
        knownColors.put("tan", Color.decode("#d2b48c"));
        knownColors.put("thistle", Color.decode("#d8bfd8"));
        knownColors.put("tomato", Color.decode("#ff6347"));
        knownColors.put("turquoise", Color.decode("#40e0d0"));
        knownColors.put("violet", Color.decode("#ee82ee"));
        knownColors.put("wheat", Color.decode("#f5deb3"));
        knownColors.put("whitesmoke", Color.decode("#f5f5f5"));
        knownColors.put("yellowgreen", Color.decode("#9acd32"));
        // CSS Color Module Level 4
        knownColors.put("rebeccapurple", Color.decode("#663399"));
    }

    /**
     * Constructor.
     * @param imageWidth the width
     * @param imageHeight the height
     */
    public AwtRenderingBackend(final int imageWidth, final int imageHeight) {
        id_ = ID_GENERATOR_++;
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
        setGlobalAlpha(1.0);
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
            globalAlpha_ = (float) globalAlpha;
            final AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, globalAlpha_);
            graphics2D_.setComposite(composite);
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
    public void drawImage(final ImageReader imageReader,
            final int sx, final int sy, final Integer sWidth, final Integer sHeight,
            final int dx, final int dy, final Integer dWidth, final Integer dHeight) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] drawImage(" + sx + ", " + sy + ", " + sWidth + ", " + sHeight
                    + "," + dx + ", " + dy + ", " + dWidth + ", " + dHeight + ")");
        }

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

                graphics2D_.drawImage(img, dx1, dy1, dx2, dy2, sx, sy, sx2, sy2, null);
            }
            finally {
                graphics2D_.setTransform(savedTransform);
            }
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
    public void putImageData(final ImageData imageData,
            final int dx, final int dy, final int dirtyX, final int dirtyY,
            final int dirtyWidth, final int dirtyHeight) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] putImageData()");
        }

        final Color orgColor = graphics2D_.getColor();

        final int width = dx + imageData.getWidth();
        final int height = dy + imageData.getHeight();
        final int imageWidth = dirtyX + dirtyWidth;
        final int imageHeight = dirtyY + dirtyHeight;

        final byte[] bytes = imageData.getData().getBuffer().getBuffer();
        int byteIdx = 0;
        int imageX = 0;
        int imageY = 0;
        for (int insertY = dy; insertY < height; insertY++) {
            for (int insertX = dx; insertX < width; insertX++) {
                if (0 <= insertX && insertX < image_.getWidth()
                        && 0 <= insertY && insertY < image_.getHeight()
                        && dirtyX <= imageX && imageX < imageWidth
                        && dirtyY <= imageY && imageY < imageHeight) {
                    final int r = bytes[byteIdx++] & 0xFF;
                    final int g = bytes[byteIdx++] & 0xFF;
                    final int b = bytes[byteIdx++] & 0xFF;
                    final int a = bytes[byteIdx++] & 0xFF;
                    final Color color = new Color(r, g, b, a);
                    graphics2D_.setColor(color);
                    graphics2D_.drawLine(insertX, insertY, insertX, insertY);
                }
                else {
                    byteIdx += 4;
                }

                imageX++;
                if (imageX == imageData.getWidth()) {
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

        fillColor_ = extractColor(fillStyle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStrokeStyle(final String strokeStyle) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[" + id_ + "] setStrokeStyle(" + strokeStyle + ")");
        }

        strokeColor_ = extractColor(strokeStyle);
    }

    private static Color extractColor(final String style) {
        final String tmpStyle = style.replaceAll("\\s", "");

        Color color = StringUtils.findColorRGB(tmpStyle);
        if (color == null) {
            color = StringUtils.findColorRGBA(tmpStyle);
        }
        if (color == null) {
            color = StringUtils.findColorHSL(tmpStyle);
        }

        if (color == null) {
            if (tmpStyle.length() > 0 && tmpStyle.charAt(0) == '#') {
                color = StringUtils.asColorHexadecimal(tmpStyle);
            }
            else {
                color = knownColors.get(tmpStyle.toLowerCase(Locale.ROOT));
                if (color == null) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Can not find color '" + tmpStyle + '\'');
                    }
                    color = Color.black;
                }
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
            final com.gargoylesoftware.htmlunit.javascript.host.canvas.Path2D path) {
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

        switch (windingRule) {
            case NON_ZERO:
                currentPath.setWindingRule(Path2D.WIND_NON_ZERO);
                break;
            default:
                currentPath.setWindingRule(Path2D.WIND_EVEN_ODD);
                break;
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
        private AffineTransform transformation_;
        private float globalAlpha_;
        private int lineWidth_;
        private Color fillColor_;
        private Color strokeColor_;
        private Shape clip_;

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
}
