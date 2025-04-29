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
package org.htmlunit.svg;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.htmlunit.SgmlPage;
import org.htmlunit.html.DomAttr;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.ElementFactory;
import org.htmlunit.util.StringUtils;
import org.xml.sax.Attributes;

/**
 * Element factory which creates elements by calling the constructor on a
 * given {@link org.htmlunit.svg.SvgElement} subclass.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class SvgElementFactory implements ElementFactory {

    private static final Class<?>[] CLASSES_ = {SvgAltGlyph.class, SvgAltGlyphDef.class, SvgAltGlyphItem.class,
        SvgAnchor.class, SvgAnimate.class, SvgAnimateColor.class, SvgAnimateMotion.class, SvgAnimateTransform.class,
        SvgCircle.class, SvgClipPath.class, SvgColorProfile.class, SvgCursor.class, SvgDefs.class, SvgDesc.class,
        SvgEllipse.class, SvgFeBlend.class, SvgFeColorMatrix.class, SvgFeComponentTransfer.class,
        SvgFeComposite.class, SvgFeConvolveMatrix.class, SvgFeDiffuseLighting.class, SvgFeDisplacementMap.class,
        SvgFeDistantLight.class, SvgFeFlood.class, SvgFeFuncA.class, SvgFeFuncB.class, SvgFeFuncG.class,
        SvgFeFuncR.class, SvgFeGaussianBlur.class, SvgFeImage.class, SvgFeMerge.class, SvgFeMergeNode.class,
        SvgFeMorphology.class, SvgFeOffset.class, SvgFePointLight.class, SvgFeSpecularLighting.class,
        SvgFeSpotLight.class, SvgFeTile.class, SvgFeTurbulence.class, SvgFilter.class, SvgFont.class,
        SvgFontFace.class, SvgFontFaceFormat.class, SvgFontFaceName.class, SvgFontFaceSrc.class,
        SvgFontFaceURI.class, SvgForeignObject.class, SvgGlyph.class, SvgGlyphRef.class, SvgGroup.class,
        SvgHKern.class, SvgImage.class, SvgLine.class, SvgLinearGradient.class, SvgMarker.class, SvgMask.class,
        SvgMetadata.class, SvgMissingGlyph.class, SvgMPath.class, SvgPath.class, SvgPattern.class, SvgPolygon.class,
        SvgPolyline.class, SvgRadialGradient.class, SvgRect.class, SvgScript.class, SvgSet.class, SvgStop.class,
        SvgStyle.class, SvgSwitch.class, SvgSymbol.class, SvgText.class, SvgTextPath.class,
        SvgTitle.class, SvgTRef.class, SvgTSpan.class, SvgUse.class, SvgView.class, SvgVKern.class
    };

    private static final Map<String, Class<?>> ELEMENTS_ = new ConcurrentHashMap<>();

    static {
        try {
            for (final Class<?> klass : CLASSES_) {
                final String key = klass.getField("TAG_NAME").get(null).toString();
                ELEMENTS_.put(StringUtils.toRootLowerCase(key), klass);
            }
        }
        catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

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
    public DomElement createElementNS(final SgmlPage page, final String namespaceURI, String qualifiedNameLC,
            final Attributes attributes) {

        final Map<String, DomAttr> attributeMap = toMap(page, attributes);
        qualifiedNameLC = StringUtils.toRootLowerCase(qualifiedNameLC);
        String tagNameLC = qualifiedNameLC;
        if (tagNameLC.indexOf(':') != -1) {
            tagNameLC = tagNameLC.substring(tagNameLC.indexOf(':') + 1);
        }
        DomElement element = null;

        final Class<?> klass = ELEMENTS_.get(tagNameLC);
        if (klass != null) {
            try {
                element = (DomElement) klass.getDeclaredConstructors()[0]
                        .newInstance(namespaceURI, qualifiedNameLC, page, attributeMap);
            }
            catch (final Exception e) {
                throw new IllegalStateException(e);
            }
        }
        if (element == null) {
            element = new SvgElement(namespaceURI, qualifiedNameLC, page, attributeMap);
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
        return ELEMENTS_.containsKey(tagNameLowerCase);
    }
}
