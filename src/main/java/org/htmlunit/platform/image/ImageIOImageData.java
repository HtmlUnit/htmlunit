/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.platform.image;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.htmlunit.platform.geom.IntDimension2D;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * Wraps the ImageReader for an HtmlImage. This is necessary because an object with a finalize()
 * method is only garbage collected after the method has been run. Which causes all referenced
 * objects to also not be garbage collected until this happens. Because a HtmlImage references a lot
 * of objects which could all be garbage collected without impacting the ImageReader it is better to
 * wrap it in another class.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author <a href="mailto:knut.johannes.dahle@gmail.com">Knut Johannes Dahle</a>
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 * @author Alex Gorbatovsky
 */
public class ImageIOImageData implements ImageData {

    // private static final Log LOG = LogFactory.getLog(ImageIOImageData.class);

    private final ImageReader imageReader_;

    public ImageIOImageData(final InputStream inputStream) throws IOException {
        final ImageInputStream iis = ImageIO.createImageInputStream(inputStream);
        final Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
            iis.close();
            throw new IOException("No image detected in response");
        }
        final ImageReader imageReader = iter.next();
        imageReader.setInput(iis);

        imageReader_ = imageReader;

        // dispose all others
        while (iter.hasNext()) {
            iter.next().dispose();
        }
    }

    /**
     * @return the {@link ImageReader}
     */
    public ImageReader getImageReader() {
        return imageReader_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IntDimension2D getWidthHeight() throws IOException {
        return new IntDimension2D(imageReader_.getWidth(0), imageReader_.getHeight(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    @Override
    public void close() throws IOException {
        if (imageReader_ != null) {
            try {
                try (ImageInputStream stream = (ImageInputStream) imageReader_.getInput()) {
                    // nothing
                }
            }
            finally {
                imageReader_.setInput(null);
                imageReader_.dispose();
            }
        }
    }
}
