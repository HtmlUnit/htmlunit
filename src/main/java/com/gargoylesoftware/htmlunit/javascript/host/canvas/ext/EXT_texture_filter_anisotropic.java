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
 * A JavaScript object for {@code EXT_texture_filter_anisotropic}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(IE)
public class EXT_texture_filter_anisotropic extends SimpleScriptable {

    /** The constant {@code MAX_TEXTURE_MAX_ANISOTROPY_EXT}. */
    @JsxConstant
    public static final int MAX_TEXTURE_MAX_ANISOTROPY_EXT = 34_047;
    /** The constant {@code TEXTURE_MAX_ANISOTROPY_EXT}. */
    @JsxConstant
    public static final int TEXTURE_MAX_ANISOTROPY_EXT = 34_046;

    /**
     * Default constructor.
     */
    public EXT_texture_filter_anisotropic() {
    }
}
