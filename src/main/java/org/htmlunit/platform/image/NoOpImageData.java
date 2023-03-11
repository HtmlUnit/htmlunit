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

import org.htmlunit.platform.geom.IntDimension2D;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * Simple no op {@link ImageData} implementation.
 *
 * @author Ronald Brill
 */
public class NoOpImageData implements ImageData {

    // private static final Log LOG = LogFactory.getLog(NoOpImageData.class);

    /**
     * Ctor.
     */
    public NoOpImageData() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IntDimension2D getWidthHeight() throws IOException {
        return new IntDimension2D(0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
    }
}
