/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.html.DomElement;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.util.XmlUtils;
import org.htmlunit.xpath.xml.utils.PrefixResolver;

/**
 * A JavaScript object for {@code XPathNSResolver}.
 *
 * @author Ahmed Ashour
 * @author Chuck Dumont
 * @author Ronald Brill
 */
@JsxClass(className = "NativeXPathNSResolver", isJSObject = false)
public class XPathNSResolver extends HtmlUnitScriptable implements PrefixResolver {

    private Node element_;

    /**
     * Sets the element to start lookup from.
     * @param element {@link org.htmlunit.javascript.host.html.HTMLElement}
     *        or {@link org.htmlunit.javascript.host.Element} to start lookup from
     */
    public void setElement(final Node element) {
        element_ = element;
    }

    /**
     * Look up the namespace URI associated to the given namespace prefix.
     * @param prefix the prefix to look for
     * @return the associated namespace URI or null if none is found
     */
    @JsxFunction
    public String lookupNamespaceURI(final String prefix) {
        return XmlUtils.lookupNamespaceURI((DomElement) element_.getDomNodeOrDie(), prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceForPrefix(final String prefix) {
        return lookupNamespaceURI(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceForPrefix(final String prefix, final org.w3c.dom.Node context) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handlesNullPrefixes() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return element_.getDefaultValue(hint);
    }
}
