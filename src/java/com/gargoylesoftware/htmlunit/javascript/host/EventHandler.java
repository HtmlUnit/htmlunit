/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * Allows to wrap event handler code as Function object.
 * @version  $Revision$
 * @author Marc Guillemot
 */
public class EventHandler extends BaseFunction {
    private static final long serialVersionUID = 3257850965406068787L;
    
    private String eventHandlerWrapperName_;
    private final String functionDeclaration_;
    private boolean compiled_;

    /**
     * Counter to provide unique function names when defining event handlers 
     */
    private static int WrapperFunctionCounter_;

    /**
     * Builds a function that will execute the javascript code provided
     * @param jsSnippet the javascript code
     */
    public EventHandler(final String jsSnippet) {
        functionDeclaration_ = wrapSnippet(jsSnippet);
        
    }

    /**
     * The function call.
     * @see org.mozilla.javascript.BaseFunction#call(Context, Scriptable, Scriptable, Object[])
     */
    public Object call(final Context cx, final Scriptable scope,
        final Scriptable thisObj, final Object[] args)
        throws JavaScriptException {

        if (!compiled_) {
            cx.evaluateString(scope, functionDeclaration_, HtmlElement.class.getName(), 1, null);
            compiled_ = true;
        }

        final StringBuffer expression = new StringBuffer(eventHandlerWrapperName_);

        if (args.length==1) {
            ScriptableObject.putProperty(scope, "event", args[0]);
            expression.append("(event)");
        }
        else {
            expression.append("()");
        }

        final Object result = cx.evaluateString(scope, expression.toString(), "sourceName", 1, null);

        return result;
    }

    private String wrapSnippet(final String jsSnippet) {
        eventHandlerWrapperName_ = "gargoyleEventHandlerWrapper" + WrapperFunctionCounter_++;

        final StringBuffer buffer = new StringBuffer();

        buffer.append("function ");
        buffer.append(eventHandlerWrapperName_);
        buffer.append("(event) {");
        buffer.append(jsSnippet);
        buffer.append("}");
        return buffer.toString();
    }
    
    /**
     * @see org.mozilla.javascript.ScriptableObject#getDefaultValue(java.lang.Class)
     * @param typeHint the type hint
     * @return the js code of the function declaration
     */
    public Object getDefaultValue(final Class typeHint) {
        return functionDeclaration_;
    }
    
    /**
     * 
     * @see org.mozilla.javascript.IdScriptableObject#get(java.lang.String, org.mozilla.javascript.Scriptable)
     */
    public Object get(final String name, final Scriptable start) {
        // quick and dirty
        if ("toString".equals(name)) {
            return new BaseFunction() {
                private static final long serialVersionUID = 3761409724511435061L;

                public Object call(final Context cx, final Scriptable scope,
                        final Scriptable thisObj, final Object[] args) {
                    return functionDeclaration_;
                }
            };
        }

        return super.get(name, start);
    }
    
  }
