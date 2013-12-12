/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMImplementation.<br>
 * Provides methods that are independent of any particular instance of the Document Object Model (DOM).
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms762259.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Frank Danek
 */
@JsxClass(browsers = @WebBrowser(IE))
public class XMLDOMImplementation extends MSXMLScriptable {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLDOMImplementation() {
    }

    /**
     * Indicates support for the specified feature.
     * @param feature a string that specifies the feature to test
     * @param version a string that specifies the version number to test
     * @return true if the specified feature is implemented; otherwise false
     */
    @JsxFunction
    public boolean hasFeature(final String feature, final String version) {
        if (feature == null || "null".equals(feature) || version == null || "null".equals(version)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        if ("XML".equals(feature) && "1.0".equals(version)) {
            return true;
        }
        else if ("DOM".equals(feature) && "1.0".equals(version)) {
            return true;
        }
        else if ("MS-DOM".equals(feature) && ("1.0".equals(version) || "2.0".equals(version))) {
            return true;
        }
        return false;
    }
}
