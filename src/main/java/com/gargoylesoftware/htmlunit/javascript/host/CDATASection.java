/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;

/**
 * A JavaScript object for CDATASection.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(domClasses = DomCDataSection.class)
public final class CDATASection extends Text {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public CDATASection() {
    }

}
