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
package com.gargoylesoftware.htmlunit.svg;

import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.Attributes;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.ElementFactory;

/**
 * Element factory which creates elements by calling the constructor on a
 * given {@link com.gargoylesoftware.htmlunit.svg.SvgElement} subclass.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class SvgElementFactory implements ElementFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement createElement(final SgmlPage page, final String tagName, final Attributes attributes) {
        throw new IllegalStateException("SVG.createElement not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement createElementNS(final SgmlPage page, final String namespaceURI, final String qualifiedName,
            final Attributes attributes) {

        final Map<String, DomAttr> attributeMap = toMap(page, attributes);
        final String tagName = qualifiedName;
        DomElement element = null;
        if (tagName.equalsIgnoreCase(SvgAltGlyph.TAG_NAME)) {
            element = new SvgAltGlyph(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgAnchor.TAG_NAME)) {
            element = new SvgAnchor(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgAnimate.TAG_NAME)) {
            element = new SvgAnimate(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgAnimateMotion.TAG_NAME)) {
            element = new SvgAnimateMotion(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgAnimateTransform.TAG_NAME)) {
            element = new SvgAnimateTransform(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgCircle.TAG_NAME)) {
            element = new SvgCircle(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgClipPath.TAG_NAME)) {
            element = new SvgClipPath(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgDefs.TAG_NAME)) {
            element = new SvgDefs(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgDesc.TAG_NAME)) {
            element = new SvgDesc(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgEllipse.TAG_NAME)) {
            element = new SvgEllipse(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeBlend.TAG_NAME)) {
            element = new SvgFeBlend(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeColorMatrix.TAG_NAME)) {
            element = new SvgFeColorMatrix(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeComponentTransfer.TAG_NAME)) {
            element = new SvgFeComponentTransfer(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgSvg.TAG_NAME)) {
            element = new SvgSvg(namespaceURI, qualifiedName, page, attributeMap);
        }
        if (element == null) {
            element = new SvgElement(namespaceURI, qualifiedName, page, attributeMap);
        }
        return element;
    }

    private Map<String, DomAttr> toMap(final SgmlPage page, final Attributes attributes) {
        Map<String, DomAttr> attributeMap = null;
        if (attributes != null) {
            attributeMap = new LinkedHashMap<String, DomAttr>(attributes.getLength());
            for (int i = 0; i < attributes.getLength(); i++) {
                final String qName = attributes.getQName(i);
                // browsers consider only first attribute (ex: <div id='foo' id='something'>...</div>)
                if (!attributeMap.containsKey(qName)) {
                    String namespaceURI = attributes.getURI(i);
                    if (namespaceURI != null && namespaceURI.isEmpty()) {
                        namespaceURI = null;
                    }
                    final DomAttr newAttr = new DomAttr(page, namespaceURI, qName, attributes.getValue(i), true);
                    attributeMap.put(qName, newAttr);
                }
            }
        }
        return attributeMap;
    }
}
