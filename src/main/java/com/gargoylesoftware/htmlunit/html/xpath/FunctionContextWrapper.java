/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html.xpath;

import java.util.HashMap;
import java.util.Map;

import org.jaxen.Function;
import org.jaxen.FunctionContext;
import org.jaxen.UnresolvableException;

import com.gargoylesoftware.htmlunit.util.AssertionUtils;

/**
 * A wrapper allowing to add new functions without affecting the original context.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class FunctionContextWrapper implements FunctionContext {
    private final FunctionContext wrappedContext_;
    private final Map<String, Function> localFunctions_ = new HashMap<String, Function>();

    /**
     * Wraps an existing context
     * @param functionContext the context to wrap
     */
    public FunctionContextWrapper(final FunctionContext functionContext) {
        AssertionUtils.notNull("function context", functionContext);
        wrappedContext_ = functionContext;
    }

    /**
     * First look at the locally defined function and if none found calls the wrapped
     * context.
     * {@inheritDoc}
     */
    public Function getFunction(final String namespaceURI, final String prefix, final String localName)
        throws UnresolvableException {

        final Function localFunction = (Function) localFunctions_.get(localName);
        if (localFunction != null) {
            return localFunction;
        }
        return wrappedContext_.getFunction(namespaceURI, prefix, localName);
    }

    /**
     * Registers a function for this context.
     * @param localName The non-prefixed local portion of the function to be registered with this context.
     * @param function the object to be used when evaluating the function.
     */
    public void registerFunction(final String localName, final Function function) {
        AssertionUtils.notNull("function", function);
        AssertionUtils.notNull("localName", localName);
        
        localFunctions_.put(localName, function);
    }
}
