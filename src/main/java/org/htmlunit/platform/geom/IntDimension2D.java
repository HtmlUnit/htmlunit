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
package org.htmlunit.platform.geom;

/**
 * Simple data container because Dimension2D is not available on Android.
 *
 * @author Ronald Brill
 */
public class IntDimension2D {

    // private static final Log LOG = LogFactory.getLog(IntDimension2D.class);

    private final int width_;
    private final int height_;

    /**
     * Constructor.
     * @param width the width
     * @param height the height
     */
    public IntDimension2D(final int width, final int height) {
        width_ = width;
        height_ = height;
    }

    /**
     * @return the width of this as integer.
     */
    public int getWidth() {
        return width_;
    }

    /**
     * @return the height of this as integer.
     */
    public int getHeight() {
        return height_;
    }
}
