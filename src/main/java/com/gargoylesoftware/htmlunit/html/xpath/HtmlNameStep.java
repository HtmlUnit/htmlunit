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

import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;
import org.jaxen.Navigator;
import org.jaxen.UnresolvableException;
import org.jaxen.expr.DefaultNameStep;
import org.jaxen.expr.PredicateSet;
import org.jaxen.expr.iter.IterableAxis;
import org.jaxen.saxpath.Axis;

/**
 * Html implementation of {@link NameStep}, which ignores case-sensitivity of attribute names.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
class HtmlNameStep extends DefaultNameStep {
    
    /** Quick flag denoting if the local name was '*' */
    private final boolean matchesAnyName_;

    /** Quick flag denoting if we have a namespace prefix **/
    private final boolean hasPrefix_;

    /**
     * Constructor.
     *
     * @param axis the axis to work through
     * @param prefix the name prefix
     * @param localName the local name
     * @param predicateSet the set of predicates
     */
    public HtmlNameStep(final IterableAxis axis, final String prefix, final String localName,
            final PredicateSet predicateSet) {
        super(axis, prefix, localName, predicateSet);
        this.matchesAnyName_ = "*".equals(localName);
        this.hasPrefix_ = prefix != null && prefix.length() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(final Object node, final ContextSupport contextSupport) throws JaxenException {
        final Navigator nav  = contextSupport.getNavigator();
        String myUri = null;
        String nodeName = null;
        String nodeUri = null;

        if (nav.isElement(node)) {
            nodeName = nav.getElementName(node);
            nodeUri = nav.getElementNamespaceUri(node);
        }
        else if (nav.isText(node)) {
            return false;
        }
        else if (nav.isAttribute(node)) {
            if (getAxis() != Axis.ATTRIBUTE) {
                return false;
            }
            nodeName = nav.getAttributeName(node);
            nodeUri = nav.getAttributeNamespaceUri(node);
        }
        else if (nav.isDocument(node)) {
            return false;
        }
        else if (nav.isNamespace(node)) {
            if (getAxis() != Axis.NAMESPACE) {
                // Only works for namespace::*
                return false;
            }
            nodeName = nav.getNamespacePrefix(node);
        }
        else {
            return false;
        }

        if (hasPrefix_) {
            myUri = contextSupport.translateNamespacePrefixToUri(getPrefix());
            if (myUri == null) {
                throw new UnresolvableException("Cannot resolve namespace prefix '" + getPrefix() + "'");
            }
        }
        else if (matchesAnyName_) {
            return true;
        }

        // If we map to a non-empty namespace and the node does not
        // or vice-versa, fail-fast.
        if (hasNamespace(myUri) != hasNamespace(nodeUri)) {
            return false;
        }
        
        // To fail-fast, we check the equality of
        // local-names first.  Shorter strings compare
        // quicker.
        if (matchesAnyName_ || nodeName.equalsIgnoreCase(getLocalName())) {
            return matchesNamespaceURIs(myUri, nodeUri);
        }

        return false;
    }

    /**
     * Checks whether the URI represents a namespace.
     *
     * @param uri  the URI to check
     * @return true if non-null and non-empty
     */
    private boolean hasNamespace(final String uri) {
        return (uri != null && uri.length() > 0);
    }

}
