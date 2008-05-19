/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
 * @deprecated As of 2.2, please use {@link HttpMethod} instead.
 */
@Deprecated
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
