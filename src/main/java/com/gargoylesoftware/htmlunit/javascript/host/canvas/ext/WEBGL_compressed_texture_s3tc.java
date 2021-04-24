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
package com.gargoylesoftware.htmlunit.javascript.host.canvas.ext;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;

/**
 * A JavaScript object for {@code WEBGL_compressed_texture_s3tc}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(IE)
public class WEBGL_compressed_texture_s3tc extends SimpleScriptable {

    /** The constant {@code COMPRESSED_RGBA_S3TC_DXT1_EXT}. */
    @JsxConstant
    public static final int COMPRESSED_RGBA_S3TC_DXT1_EXT = 33_777;
    /** The constant {@code COMPRESSED_RGBA_S3TC_DXT3_EXT}. */
    @JsxConstant
    public static final int COMPRESSED_RGBA_S3TC_DXT3_EXT = 33_778;
    /** The constant {@code COMPRESSED_RGBA_S3TC_DXT5_EXT}. */
    @JsxConstant
    public static final int COMPRESSED_RGBA_S3TC_DXT5_EXT = 33_779;
    /** The constant {@code COMPRESSED_RGB_S3TC_DXT1_EXT}. */
    @JsxConstant
    public static final int COMPRESSED_RGB_S3TC_DXT1_EXT = 33_776;

    /**
     * Default constructor.
     */
    public WEBGL_compressed_texture_s3tc() {
    }
}
