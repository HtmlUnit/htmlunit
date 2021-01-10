/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.css;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.css.dom.CSSStyleSheetImpl;
import com.gargoylesoftware.css.dom.CSSStyleSheetImpl.SelectorEntry;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.SelectorSpecificity;
import com.gargoylesoftware.htmlunit.html.DomElement;

public class CssStyleDeclarationBuilder {

    private static final Log LOG = LogFactory.getLog(CssStyleDeclarationBuilder.class);

    public static ComputedCssStyleDeclaration build(Collection<CssStyleSheet> sheets, DomElement domNode,
            String normalizedPseudo) {
        final boolean trace = LOG.isTraceEnabled();
        Map<String, StyleElement> modifications = getDefaults(domNode);
        for (CssStyleSheet sheet : sheets) {
            if (sheet.isActive() && sheet.isEnabled()) {
                if (trace) {
                    LOG.trace("modifyIfNecessary: " + sheet + ", " + domNode);
                }
                modifyIfNecessary(modifications, sheet, domNode, normalizedPseudo);
            }
        }
        return new ComputedCssStyleDeclaration(new ElementCssStyleDeclaration(domNode), modifications);
    }

    private static Map<String, StyleElement> getDefaults(DomElement domElement) {
        return domElement.getDefaultStyles()
                .entrySet().stream()
                .collect(toMap(
                        Entry::getKey,
                        e -> {
                            String name = e.getKey();
                            String value = e.getValue();
                            return new StyleElement(name, value, "",
                                    SelectorSpecificity.DEFAULT_STYLE_ATTRIBUTE);
                        }));
    }

    private static void modifyIfNecessary(
            Map<String, StyleElement> modifications,
            final CssStyleSheet sheet,
            final DomElement element,
            final String pseudoElement) {
        final List<SelectorEntry> matchingRules = sheet.selects(element, pseudoElement, false);
        for (final CSSStyleSheetImpl.SelectorEntry entry : matchingRules) {
            final com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl dec = entry.getRule().getStyle();
            applyStyleFromSelector(modifications, dec, entry.getSelector());
        }
    }

    private static void applyStyleFromSelector(
            final Map<String, StyleElement> modifications,
            final com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl declaration,
            final Selector selector) {
        final SelectorSpecificity specificity = selector.getSelectorSpecificity();
        for (final Property prop : declaration.getProperties()) {
            final String name = prop.getName();
            final String value = declaration.getPropertyValue(name);
            final String priority = declaration.getPropertyPriority(name);
            applyLocalStyleAttribute(modifications, name, value, priority, specificity);
        }
    }

    private static void applyLocalStyleAttribute(
            Map<String, StyleElement> modifications,
            final String name, final String newValue, final String priority,
            final SelectorSpecificity specificity) {
        if (!StyleElement.PRIORITY_IMPORTANT.equals(priority)) {
            final StyleElement existingElement = modifications.get(name);
            if (existingElement != null) {
                if (StyleElement.PRIORITY_IMPORTANT.equals(existingElement.getPriority())) {
                    return; // can't override a !important rule by a normal rule. Ignore it!
                } else if (specificity.compareTo(existingElement.getSpecificity()) < 0) {
                    return; // can't override a rule with a rule having higher specificity
                }
            }
        }
        final StyleElement element = new StyleElement(name, newValue, priority, specificity);
        modifications.put(name, element);
    }
}
