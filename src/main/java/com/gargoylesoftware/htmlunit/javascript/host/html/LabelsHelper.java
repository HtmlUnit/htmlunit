package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;

/**
 * A helper class to implement {@code .labels} property.
 *
 * @author Ahmed Ashour
 *
 */
class LabelsHelper extends NodeList {

    /**
     * Creates an instance.
     *
     * @param domeNode the {@link DomNode}
     * @param description a text useful for debugging
     */
    public LabelsHelper(final DomElement domeNode) {
        super(domeNode, false);
    }


    /**
     * This is overridden instead of {@link #computeElements()} in order to prevent caching at all.
     *
     * {@inheritDoc}
     */
    @Override
    public List<Object> getElements() {
        final List<Object> response = new ArrayList<>();
        final DomElement domElement = (DomElement) getDomNodeOrDie();
        for (DomNode parent = domElement.getParentNode(); parent != null; parent = parent.getParentNode()) {
            if (parent instanceof HtmlLabel) {
                response.add(parent);
            }
        }
        final String id = domElement.getId();
        if (id != DomElement.ATTRIBUTE_NOT_DEFINED) {
            for (final DomElement label : domElement.getHtmlPageOrNull().getElementsByTagName("label")) {
                if (id.equals(label.getAttribute("for"))) {
                    response.add(label);
                }
            }
        }

        return response;
    }
}
