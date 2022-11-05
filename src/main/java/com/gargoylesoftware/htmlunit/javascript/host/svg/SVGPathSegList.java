/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.svg;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;

/**
 * A JavaScript object for {@code SVGPathSegList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(IE)
public class SVGPathSegList extends HtmlUnitScriptable {

    /**
     * Creates an instance.
     */
    public SVGPathSegList() {
    }

}
