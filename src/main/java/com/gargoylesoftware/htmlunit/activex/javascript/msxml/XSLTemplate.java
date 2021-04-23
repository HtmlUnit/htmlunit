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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for MSXML's (ActiveX) XSLTemplate.<br>
 * Used for caching compiled XSL Transformations (XSLT) templates.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms767644.aspx">MSDN documentation</a>
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass(IE)
public class XSLTemplate extends MSXMLScriptable {

    private XMLDOMNode stylesheet_;

    /**
     * Sets the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template.
     * @param node the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template
     */
    @JsxSetter
    public void setStylesheet(final XMLDOMNode node) {
        stylesheet_ = node;
    }

    /**
     * Returns the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template.
     * @return the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template
     */
    @JsxGetter
    public XMLDOMNode getStylesheet() {
        return stylesheet_;
    }

    /**
     * Creates a rental-model XSLProcessor object that will use this template.
     * @return the XSLTProcessor
     */
    @JsxFunction
    public XSLProcessor createProcessor() {
        final XSLProcessor processor = new XSLProcessor();
        processor.setPrototype(getPrototype(processor.getClass()));
        processor.setParentScope(this);
        processor.importStylesheet(stylesheet_);
        return processor;
    }
}
