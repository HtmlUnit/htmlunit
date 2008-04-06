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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * An array of elements. It is very similar to {@link HTMLCollection}, but with 'tags' property for Firefox.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLCollectionTags extends HTMLCollection {

    private static final long serialVersionUID = -1772897974886997756L;

    /**
     * Create an instance. JavaScript objects must have a default constructor.
     * Don't call.
     */
    @Deprecated
    public HTMLCollectionTags() {
        // nothing
    }

    /**
     * Create an instance.
     * @param parentScope parent scope
     */
    public HTMLCollectionTags(final SimpleScriptable parentScope) {
        super(parentScope);
    }

    /**
     * Called for the js "==".
     * {@inheritDoc}
     */
    @Override
    protected Object equivalentValues(final Object other) {
        if (other == this) {
            return Boolean.TRUE;
        }
        else if (other instanceof HTMLCollectionTags) {
            final HTMLCollectionTags otherArray = (HTMLCollectionTags) other;
            if (getNode() == otherArray.getNode()
                    && getXpath().toString().equals(otherArray.getXpath().toString())
                    && getTransformer().equals(otherArray.getTransformer())) {
                return Boolean.TRUE;
            }
            else {
                return NOT_FOUND;
            }
        }

        return super.equivalentValues(other);
    }
}
