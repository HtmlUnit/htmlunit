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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A collection of constants that represent the various ways a page can be submitted.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public final class SubmitMethod implements Serializable {

    private static final long serialVersionUID = 6202782549629170616L;

    /**
     * OPTIONS.
     * @see <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>
     */
    public static final SubmitMethod OPTIONS = new SubmitMethod("options");

    /**
     * GET.
     * @see <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>
     */
    public static final SubmitMethod GET = new SubmitMethod("get");

    /**
     * HEAD.
     * @see <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>
     */
    public static final SubmitMethod HEAD = new SubmitMethod("head");

    /**
     * POST.
     * @see <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>
     */
    public static final SubmitMethod POST = new SubmitMethod("post");

    /**
     * PUT.
     * @see <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>
     */
    public static final SubmitMethod PUT = new SubmitMethod("put");

    /**
     * DELETE.
     * @see <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>
     */
    public static final SubmitMethod DELETE = new SubmitMethod("delete");

    /**
     * TRACE.
     * @see <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>
     */
    public static final SubmitMethod TRACE = new SubmitMethod("trace");

    private final String name_;
    private static final Map<String, SubmitMethod> methods_ =
        toMap(new SubmitMethod[] {OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE});
    
    private static Map<String, SubmitMethod> toMap(final SubmitMethod[] methods) {
        final HashMap<String, SubmitMethod> map = new HashMap<String, SubmitMethod>(methods.length);
        for (final SubmitMethod method : methods) {
            map.put(method.getName(), method);
        }
        return map;
    }

    private SubmitMethod(final String name) {
        name_ = name;
    }

    /**
     * Returns the name of this submit method.
     *
     * @return the name of this submit method
     */
    public String getName() {
        return name_;
    }

    /**
     * Returns the constant that matches the given name (case insensitive).
     * @param name the name to search by
     * @return see above
     */
    public static SubmitMethod getInstance(final String name) {
        final SubmitMethod method = methods_.get(name.toLowerCase());
        if (method == null) {
            throw new IllegalArgumentException("No method found for [" + name + "]");
        }
        
        return method;
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "SubmitMethod[name=" + getName() + "]";
    }
}
