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
package com.gargoylesoftware.htmlunit.html.xpath;

import javax.xml.transform.TransformerException;

import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.IntStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionDef1Arg;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.HtmlForm;

/**
 * An XPath function which returns <tt>true</tt> if the current node is a descendant of the node used as the
 * initial XPath context. In addition, if the node used as the initial XPath context was an {@link HtmlForm}
 * instance, this function considers {@link HtmlForm#getLostChildren() lost children} to be descendants of the
 * form, even if they aren't really descendants from a DOM point of view.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @deprecated since HtmlUnit-2.8 without replacement as there is no internal usage for this classe.
 */
@Deprecated
public class IsDescendantOfContextualFormFunction extends FunctionDef1Arg {

    /**
     * {@inheritDoc}
     */
    @Override
    public XObject execute(final XPathContext ctx) throws TransformerException {
        boolean descendant = false;

        final int possibleAncestor;
        final IntStack nodeStack = ctx.getCurrentNodeStack();
        if (nodeStack.size() > 1) {
            possibleAncestor = nodeStack.elementAt(1);
        }
        else {
            possibleAncestor = DTM.NULL;
        }

        if (DTM.NULL != possibleAncestor) {
            final int currentNode = ctx.getContextNode();
            final DTM dtm = ctx.getDTM(currentNode);
            for (int ancestor = dtm.getParent(currentNode); ancestor != DTM.NULL; ancestor = dtm.getParent(ancestor)) {
                if (ancestor == possibleAncestor) {
                    descendant = true;
                    break;
                }
            }
            if (!descendant) {
                final Node n = dtm.getNode(possibleAncestor);
                if (n instanceof HtmlForm) {
                    final HtmlForm f = (HtmlForm) n;
                    descendant = f.getLostChildren().contains(dtm.getNode(currentNode));
                }
            }
        }

        return new XBoolean(descendant);
    }

}
