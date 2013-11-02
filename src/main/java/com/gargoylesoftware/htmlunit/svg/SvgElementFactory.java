/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.SVG_UNKNOWN_ARE_DOM;

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
 * @author Frank Danek
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
        return createElementNS(page, namespaceURI, qualifiedName, attributes, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement createElementNS(final SgmlPage page, final String namespaceURI, final String qualifiedName,
            final Attributes attributes, final boolean checkBrowserCompatibility) {

        final Map<String, DomAttr> attributeMap = toMap(page, attributes);
        final String tagName = qualifiedName;
        DomElement element = null;
        if (tagName.equalsIgnoreCase(SvgAltGlyph.TAG_NAME)) {
            element = new SvgAltGlyph(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgAltGlyphDef.TAG_NAME)) {
            element = new SvgAltGlyphDef(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgAltGlyphItem.TAG_NAME)) {
            element = new SvgAltGlyphItem(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgAnchor.TAG_NAME)) {
            element = new SvgAnchor(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgAnimate.TAG_NAME)) {
            element = new SvgAnimate(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgAnimateColor.TAG_NAME)) {
            element = new SvgAnimateColor(namespaceURI, qualifiedName, page, attributeMap);
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
        else if (tagName.equalsIgnoreCase(SvgColorProfile.TAG_NAME)) {
            element = new SvgColorProfile(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgCursor.TAG_NAME)) {
            element = new SvgCursor(namespaceURI, qualifiedName, page, attributeMap);
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
        else if (tagName.equalsIgnoreCase(SvgFeComposite.TAG_NAME)) {
            element = new SvgFeComposite(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeConvolveMatrix.TAG_NAME)) {
            element = new SvgFeConvolveMatrix(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeDiffuseLighting.TAG_NAME)) {
            element = new SvgFeDiffuseLighting(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeDisplacementMap.TAG_NAME)) {
            element = new SvgFeDisplacementMap(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeDistantLight.TAG_NAME)) {
            element = new SvgFeDistantLight(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeFlood.TAG_NAME)) {
            element = new SvgFeFlood(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeFuncA.TAG_NAME)) {
            element = new SvgFeFuncA(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeFuncB.TAG_NAME)) {
            element = new SvgFeFuncB(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeFuncG.TAG_NAME)) {
            element = new SvgFeFuncG(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeFuncR.TAG_NAME)) {
            element = new SvgFeFuncR(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeGaussianBlur.TAG_NAME)) {
            element = new SvgFeGaussianBlur(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeImage.TAG_NAME)) {
            element = new SvgFeImage(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeMerge.TAG_NAME)) {
            element = new SvgFeMerge(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeMergeNode.TAG_NAME)) {
            element = new SvgFeMergeNode(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeMorphology.TAG_NAME)) {
            element = new SvgFeMorphology(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeOffset.TAG_NAME)) {
            element = new SvgFeOffset(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFePointLight.TAG_NAME)) {
            element = new SvgFePointLight(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeSpecularLighting.TAG_NAME)) {
            element = new SvgFeSpecularLighting(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeSpotLight.TAG_NAME)) {
            element = new SvgFeSpotLight(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeTile.TAG_NAME)) {
            element = new SvgFeTile(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFeTurbulence.TAG_NAME)) {
            element = new SvgFeTurbulence(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFilter.TAG_NAME)) {
            element = new SvgFilter(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFont.TAG_NAME)) {
            element = new SvgFont(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFontFace.TAG_NAME)) {
            element = new SvgFontFace(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFontFaceFormat.TAG_NAME)) {
            element = new SvgFontFaceFormat(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFontFaceName.TAG_NAME)) {
            element = new SvgFontFaceName(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFontFaceSrc.TAG_NAME)) {
            element = new SvgFontFaceSrc(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgFontFaceURI.TAG_NAME)) {
            element = new SvgFontFaceURI(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgForeignObject.TAG_NAME)) {
            element = new SvgForeignObject(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgGlyph.TAG_NAME)) {
            element = new SvgGlyph(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgGlyphRef.TAG_NAME)) {
            element = new SvgGlyphRef(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgGroup.TAG_NAME)) {
            element = new SvgGroup(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgHKern.TAG_NAME)) {
            element = new SvgHKern(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgImage.TAG_NAME)) {
            element = new SvgImage(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgLine.TAG_NAME)) {
            element = new SvgLine(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgLinearGradient.TAG_NAME)) {
            element = new SvgLinearGradient(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgMarker.TAG_NAME)) {
            element = new SvgMarker(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgMask.TAG_NAME)) {
            element = new SvgMask(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgMetadata.TAG_NAME)) {
            element = new SvgMetadata(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgMissingGlyph.TAG_NAME)) {
            element = new SvgMissingGlyph(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgMpath.TAG_NAME)) {
            element = new SvgMpath(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgPath.TAG_NAME)) {
            element = new SvgPath(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgPattern.TAG_NAME)) {
            element = new SvgPattern(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgPolygon.TAG_NAME)) {
            element = new SvgPolygon(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgPolyline.TAG_NAME)) {
            element = new SvgPolyline(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgRadialGradient.TAG_NAME)) {
            element = new SvgRadialGradient(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgRect.TAG_NAME)) {
            element = new SvgRect(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgScript.TAG_NAME)) {
            element = new SvgScript(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgSet.TAG_NAME)) {
            element = new SvgSet(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgStop.TAG_NAME)) {
            element = new SvgStop(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgStyle.TAG_NAME)) {
            element = new SvgStyle(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgSvg.TAG_NAME)) {
            element = new SvgSvg(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgSwitch.TAG_NAME)) {
            element = new SvgSwitch(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgSymbol.TAG_NAME)) {
            element = new SvgSymbol(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgText.TAG_NAME)) {
            element = new SvgText(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgTextPath.TAG_NAME)) {
            element = new SvgTextPath(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgTitle.TAG_NAME)) {
            element = new SvgTitle(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgTRef.TAG_NAME)) {
            element = new SvgTRef(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgTSpan.TAG_NAME)) {
            element = new SvgTSpan(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgUse.TAG_NAME)) {
            element = new SvgUse(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgView.TAG_NAME)) {
            element = new SvgView(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (tagName.equalsIgnoreCase(SvgVKern.TAG_NAME)) {
            element = new SvgVKern(namespaceURI, qualifiedName, page, attributeMap);
        }
        if (element == null) {
            if (page.getWebClient().getBrowserVersion().hasFeature(SVG_UNKNOWN_ARE_DOM)) {
                element = new DomElement(namespaceURI, qualifiedName, page, attributeMap);
            }
            else {
                element = new SvgElement(namespaceURI, qualifiedName, page, attributeMap);
            }
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
