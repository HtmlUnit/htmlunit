package com.gargoylesoftware.htmlunit.css;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.DomElement;

public class CssStyleDeclarationBuilder {
    private static final Log LOG = LogFactory.getLog(CssStyleDeclarationBuilder.class);

    public static CssStyleDeclaration build(Collection<CssStyleSheet> sheets, DomElement domNode,
        String normalizedPseudo) {
        final CssStyleDeclarationImpl style = new CssStyleDeclarationImpl(domNode);
        final boolean trace = LOG.isTraceEnabled();
        for (CssStyleSheet sheet : sheets) {
            if (sheet.isActive() && sheet.isEnabled()) {
                if (trace) {
                    LOG.trace("modifyIfNecessary: " + sheet + ", " + style + ", " + domNode);
                }
                sheet.modifyIfNecessary(style, domNode, normalizedPseudo);
            }
        }
        return style;
    }

}
