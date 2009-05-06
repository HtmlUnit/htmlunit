/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a CanvasRenderingContext2D.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class CanvasRenderingContext2D extends SimpleScriptable {

    private static final long serialVersionUID = -693023667459214244L;

    /**
     * Returns the "fillStyle" property.
     * @return the "fillStyle" property
     */
    public Object jsxGet_fillStyle() {
        return null;
    }

    /**
     * Sets the "fillStyle" property.
     * @param fillStyle the "fillStyle" property
     */
    public void jsxSet_fillStyle(final Object fillStyle) {
        //empty
    }

    /**
     * Paints the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    public void jsxFunction_fillRect(final double x, final double y, final double w, final double h) {
        //empty
    }
}
