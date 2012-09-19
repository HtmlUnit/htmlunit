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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;

/**
 * An array of elements. It is very similar to {@link HTMLCollection}, but with 'tags' property for Firefox.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@JsxClass(extend = "HTMLCollection")
public class HTMLCollectionTags extends HTMLCollection {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     * Don't call.
     */
    @Deprecated
    public HTMLCollectionTags() {
        // Empty.
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     * @param description a text useful for debugging
     */
    public HTMLCollectionTags(final DomNode parentScope, final String description) {
        super(parentScope, false, description);
    }

    /**
     * Called for the js "==".
     * {@inheritDoc}
     */
    @Override
    protected Object equivalentValues(final Object other) {
        if (!(other instanceof HTMLCollectionTags)) {
            return Boolean.FALSE;
        }
        return super.equivalentValues(other);
    }
}
