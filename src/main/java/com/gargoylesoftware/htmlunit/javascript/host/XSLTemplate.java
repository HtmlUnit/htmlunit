/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for XSLTemplate.
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms767644.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XSLTemplate extends SimpleScriptable {

    private static final long serialVersionUID = 2820794481694666278L;

    private Node stylesheet_;

    /**
     * JavaScript constructor.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * Sets the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template.
     * @param node the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template
     */
    public void jsxSet_stylesheet(final Node node) {
        stylesheet_ = node;
    }

    /**
     * Returns the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template.
     * @return the Extensible Stylesheet Language (XSL) style sheet to compile into an XSL template
     */
    public Node jsxGet_stylesheet() {
        return stylesheet_;
    }

    /**
     * Creates a rental-model XSLProcessor object that will use this template.
     * @return the XSLTProcessor
     */
    public XSLTProcessor jsxFunction_createProcessor() {
        final XSLTProcessor processor = new XSLTProcessor();
        processor.setPrototype(getPrototype(processor.getClass()));
        processor.setParentScope(getParentScope());
        processor.jsxFunction_importStylesheet(stylesheet_);
        return processor;
    }
}
