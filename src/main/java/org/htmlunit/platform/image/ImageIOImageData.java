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
package org.htmlunit.platform.image;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Cleaner;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.htmlunit.WebClient;
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
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Christian Sell
 * @author Ahmed Ashour
 * @author Knut Johannes Dahle
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 * @author Alex Gorbatovsky
 */
public class ImageIOImageData implements ImageData {

    // private static final Log LOG = LogFactory.getLog(ImageIOImageData.class);

    private IntDimension2D dim_;

    private final ImageIOImageDataCleaningAction cleaningAction_;
    private final Cleaner.Cleanable cleanable_;

    private static final class ImageIOImageDataCleaningAction implements Runnable {
        private ImageReader imageReader_;
        private InputStream inputStream_;

        ImageIOImageDataCleaningAction(final ImageReader imageReader, final InputStream inputStream) {
            imageReader_ = imageReader;
            inputStream_ = inputStream;
        }

        synchronized ImageReader getImageReader() {
            if (imageReader_ == null) {
                throw new IllegalStateException("ImageIOImageData is closed");
            }
            return imageReader_;
        }

        @Override
        public synchronized void run() {
            if (imageReader_ == null) {
                return;
            }

            try (ImageInputStream stream = (ImageInputStream) imageReader_.getInput()) {
                // nothing
            }
            catch (final IOException e) {
                // optionally log
            }
            finally {
                imageReader_.setInput(null);
                imageReader_.dispose();
                imageReader_ = null;
            }

            try {
                inputStream_.close();
            }
            catch (final IOException e) {
                // optionally log
            }
            finally {
                inputStream_ = null;
            }
        }
    }

    /**
     * Ctor.
     * @param inputStream the {@link InputStream} to read from
     * @throws IOException in case of error
     */
    public ImageIOImageData(final InputStream inputStream) throws IOException {
        final ImageInputStream iis = ImageIO.createImageInputStream(inputStream);
        final Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
            iis.close();
            inputStream.close();
            throw new IOException("No image detected in response");
        }

        final ImageReader imageReader = iter.next();
        imageReader.setInput(iis);

        cleaningAction_ = new ImageIOImageDataCleaningAction(imageReader, inputStream);
        cleanable_ = WebClient.registerCleanerAction(this, cleaningAction_);

        // dispose all others
        while (iter.hasNext()) {
            iter.next().dispose();
        }
    }

    /**
     * Returns the {@link ImageReader}.
     *
     * @return the {@link ImageReader}
     */
    public ImageReader getImageReader() {
        return cleaningAction_.getImageReader();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IntDimension2D getWidthHeight() throws IOException {
        if (dim_ == null) {
            final ImageReader imgReader = cleaningAction_.getImageReader();
            dim_ = new IntDimension2D(imgReader.getWidth(0), imgReader.getHeight(0));
        }
        return dim_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        cleanable_.clean();
    }
}
