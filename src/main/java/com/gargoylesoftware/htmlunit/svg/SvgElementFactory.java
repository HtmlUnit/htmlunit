/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.ElementFactory;

/**
 * Element factory which creates elements by calling the constructor on a
 * given {@link com.gargoylesoftware.htmlunit.svg.SvgElement} subclass.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class SvgElementFactory implements ElementFactory {

    /*
     * You can generate your own test cases by looking into ElementTestSource.generateTestForHtmlElements
     */
    static final Set<String> SUPPORTED_TAGS_ = new HashSet<String>(Arrays.asList(
            SvgAltGlyph.TAG_NAME, SvgAltGlyphDef.TAG_NAME, SvgAltGlyphItem.TAG_NAME,
            SvgAnchor.TAG_NAME, SvgAnimate.TAG_NAME, SvgAnimateColor.TAG_NAME,
            SvgAnimateMotion.TAG_NAME, SvgAnimateTransform.TAG_NAME,
            SvgCircle.TAG_NAME, SvgClipPath.TAG_NAME, SvgColorProfile.TAG_NAME,
            SvgCursor.TAG_NAME, SvgDefs.TAG_NAME, SvgDesc.TAG_NAME,
            SvgEllipse.TAG_NAME, SvgFeBlend.TAG_NAME, SvgFeColorMatrix.TAG_NAME, SvgFeComponentTransfer.TAG_NAME,
            SvgFeComposite.TAG_NAME, SvgFeConvolveMatrix.TAG_NAME, SvgFeDiffuseLighting.TAG_NAME,
            SvgFeDisplacementMap.TAG_NAME,
            SvgFeDistantLight.TAG_NAME, SvgFeFlood.TAG_NAME, SvgFeFuncA.TAG_NAME, SvgFeFuncB.TAG_NAME,
            SvgFeFuncG.TAG_NAME,
            SvgFeFuncR.TAG_NAME, SvgFeGaussianBlur.TAG_NAME, SvgFeImage.TAG_NAME, SvgFeMerge.TAG_NAME,
            SvgFeMergeNode.TAG_NAME,
            SvgFeMorphology.TAG_NAME, SvgFeOffset.TAG_NAME, SvgFePointLight.TAG_NAME, SvgFeSpecularLighting.TAG_NAME,
            SvgFeSpotLight.TAG_NAME, SvgFeTile.TAG_NAME, SvgFeTurbulence.TAG_NAME, SvgFilter.TAG_NAME, SvgFont.TAG_NAME,
            SvgFontFace.TAG_NAME, SvgFontFaceFormat.TAG_NAME, SvgFontFaceName.TAG_NAME, SvgFontFaceSrc.TAG_NAME,
            SvgFontFaceURI.TAG_NAME, SvgForeignObject.TAG_NAME, SvgGlyph.TAG_NAME, SvgGlyphRef.TAG_NAME,
            SvgGroup.TAG_NAME,
            SvgHKern.TAG_NAME, SvgImage.TAG_NAME, SvgLine.TAG_NAME, SvgLinearGradient.TAG_NAME,
            SvgMarker.TAG_NAME, SvgMask.TAG_NAME,
            SvgMetadata.TAG_NAME, SvgMissingGlyph.TAG_NAME, SvgMPath.TAG_NAME, SvgPath.TAG_NAME,
            SvgPattern.TAG_NAME, SvgPolygon.TAG_NAME,
            SvgPolyline.TAG_NAME, SvgRadialGradient.TAG_NAME, SvgRect.TAG_NAME, SvgScript.TAG_NAME,
            SvgSet.TAG_NAME, SvgStop.TAG_NAME,
            SvgStyle.TAG_NAME, SvgSvg.TAG_NAME, SvgSwitch.TAG_NAME, SvgSymbol.TAG_NAME, SvgText.TAG_NAME,
            SvgTextPath.TAG_NAME,
            SvgTitle.TAG_NAME, SvgTRef.TAG_NAME, SvgTSpan.TAG_NAME, SvgUse.TAG_NAME, SvgView.TAG_NAME, SvgVKern.TAG_NAME
    ));

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
    public DomElement createElementNS(final SgmlPage page, final String namespaceURI, String qualifiedNameLC,
            final Attributes attributes, final boolean checkBrowserCompatibility) {

        final Map<String, DomAttr> attributeMap = toMap(page, attributes);
        qualifiedNameLC = qualifiedNameLC.toLowerCase(Locale.ROOT);
        String tagNameLC = qualifiedNameLC;
        if (tagNameLC.indexOf(':') != -1) {
            tagNameLC = tagNameLC.substring(tagNameLC.indexOf(':') + 1);
        }
        DomElement element = null;

        switch (tagNameLC) {

            case SvgAltGlyph.TAG_NAME:
                element = new SvgAltGlyph(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgAltGlyphDef.TAG_NAME:
                element = new SvgAltGlyphDef(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgAltGlyphItem.TAG_NAME:
                element = new SvgAltGlyphItem(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgAnchor.TAG_NAME:
                element = new SvgAnchor(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgAnimate.TAG_NAME:
                element = new SvgAnimate(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgAnimateColor.TAG_NAME:
                element = new SvgAnimateColor(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgAnimateMotion.TAG_NAME:
                element = new SvgAnimateMotion(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgAnimateTransform.TAG_NAME:
                element = new SvgAnimateTransform(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgCircle.TAG_NAME:
                element = new SvgCircle(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgClipPath.TAG_NAME:
                element = new SvgClipPath(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgColorProfile.TAG_NAME:
                element = new SvgColorProfile(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgCursor.TAG_NAME:
                element = new SvgCursor(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgDefs.TAG_NAME:
                element = new SvgDefs(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgDesc.TAG_NAME:
                element = new SvgDesc(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgEllipse.TAG_NAME:
                element = new SvgEllipse(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeBlend.TAG_NAME:
                element = new SvgFeBlend(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeColorMatrix.TAG_NAME:
                element = new SvgFeColorMatrix(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeComponentTransfer.TAG_NAME:
                element = new SvgFeComponentTransfer(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeComposite.TAG_NAME:
                element = new SvgFeComposite(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeConvolveMatrix.TAG_NAME:
                element = new SvgFeConvolveMatrix(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeDiffuseLighting.TAG_NAME:
                element = new SvgFeDiffuseLighting(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeDisplacementMap.TAG_NAME:
                element = new SvgFeDisplacementMap(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeDistantLight.TAG_NAME:
                element = new SvgFeDistantLight(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeFlood.TAG_NAME:
                element = new SvgFeFlood(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeFuncA.TAG_NAME:
                element = new SvgFeFuncA(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeFuncB.TAG_NAME:
                element = new SvgFeFuncB(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeFuncG.TAG_NAME:
                element = new SvgFeFuncG(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeFuncR.TAG_NAME:
                element = new SvgFeFuncR(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeGaussianBlur.TAG_NAME:
                element = new SvgFeGaussianBlur(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeImage.TAG_NAME:
                element = new SvgFeImage(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeMerge.TAG_NAME:
                element = new SvgFeMerge(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeMergeNode.TAG_NAME:
                element = new SvgFeMergeNode(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeMorphology.TAG_NAME:
                element = new SvgFeMorphology(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeOffset.TAG_NAME:
                element = new SvgFeOffset(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFePointLight.TAG_NAME:
                element = new SvgFePointLight(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeSpecularLighting.TAG_NAME:
                element = new SvgFeSpecularLighting(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeSpotLight.TAG_NAME:
                element = new SvgFeSpotLight(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeTile.TAG_NAME:
                element = new SvgFeTile(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFeTurbulence.TAG_NAME:
                element = new SvgFeTurbulence(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFilter.TAG_NAME:
                element = new SvgFilter(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFont.TAG_NAME:
                element = new SvgFont(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFontFace.TAG_NAME:
                element = new SvgFontFace(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFontFaceFormat.TAG_NAME:
                element = new SvgFontFaceFormat(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFontFaceName.TAG_NAME:
                element = new SvgFontFaceName(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFontFaceSrc.TAG_NAME:
                element = new SvgFontFaceSrc(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgFontFaceURI.TAG_NAME:
                element = new SvgFontFaceURI(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgForeignObject.TAG_NAME:
                element = new SvgForeignObject(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgGlyph.TAG_NAME:
                element = new SvgGlyph(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgGlyphRef.TAG_NAME:
                element = new SvgGlyphRef(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgGroup.TAG_NAME:
                element = new SvgGroup(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgHKern.TAG_NAME:
                element = new SvgHKern(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgImage.TAG_NAME:
                element = new SvgImage(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgLine.TAG_NAME:
                element = new SvgLine(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgLinearGradient.TAG_NAME:
                element = new SvgLinearGradient(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgMarker.TAG_NAME:
                element = new SvgMarker(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgMask.TAG_NAME:
                element = new SvgMask(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgMetadata.TAG_NAME:
                element = new SvgMetadata(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgMissingGlyph.TAG_NAME:
                element = new SvgMissingGlyph(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgMPath.TAG_NAME:
                element = new SvgMPath(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgPath.TAG_NAME:
                element = new SvgPath(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgPattern.TAG_NAME:
                element = new SvgPattern(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgPolygon.TAG_NAME:
                element = new SvgPolygon(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgPolyline.TAG_NAME:
                element = new SvgPolyline(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgRadialGradient.TAG_NAME:
                element = new SvgRadialGradient(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgRect.TAG_NAME:
                element = new SvgRect(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgScript.TAG_NAME:
                element = new SvgScript(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgSet.TAG_NAME:
                element = new SvgSet(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgStop.TAG_NAME:
                element = new SvgStop(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgStyle.TAG_NAME:
                element = new SvgStyle(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgSvg.TAG_NAME:
                element = new SvgSvg(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgSwitch.TAG_NAME:
                element = new SvgSwitch(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgSymbol.TAG_NAME:
                element = new SvgSymbol(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgText.TAG_NAME:
                element = new SvgText(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgTextPath.TAG_NAME:
                element = new SvgTextPath(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgTitle.TAG_NAME:
                element = new SvgTitle(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgTRef.TAG_NAME:
                element = new SvgTRef(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgTSpan.TAG_NAME:
                element = new SvgTSpan(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgUse.TAG_NAME:
                element = new SvgUse(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgView.TAG_NAME:
                element = new SvgView(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            case SvgVKern.TAG_NAME:
                element = new SvgVKern(namespaceURI, qualifiedNameLC, page, attributeMap);
                break;

            default:
                if (page.getWebClient().getBrowserVersion().hasFeature(SVG_UNKNOWN_ARE_DOM)) {
                    element = new DomElement(namespaceURI, qualifiedNameLC, page, attributeMap);
                }
                else {
                    element = new SvgElement(namespaceURI, qualifiedNameLC, page, attributeMap);
                }
                break;
        }

        return element;
    }

    private static Map<String, DomAttr> toMap(final SgmlPage page, final Attributes attributes) {
        Map<String, DomAttr> attributeMap = null;
        if (attributes != null) {
            attributeMap = new LinkedHashMap<>(attributes.getLength());
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

    /**
     * Returns whether the specified name is a valid SVG tag name.
     * @param tagNameLowerCase the tag name in lower case
     * @return whether the specified name is a valid SVG tag name or not
     */
    public boolean isSupported(final String tagNameLowerCase) {
        return SUPPORTED_TAGS_.contains(tagNameLowerCase);
    }
}
