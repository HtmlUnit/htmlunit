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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.ContentCondition;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS21;
import com.steadystate.css.parser.SelectorListImpl;

/**
 * A JavaScript object for a stylesheet.
 *
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535871.aspx">MSDN doc</a>
 * @version $Revision$
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class Stylesheet extends SimpleScriptable {

    private static final long serialVersionUID = -8341675386925348206L;

    /**
     * The input source which contains the CSS stylesheet which this stylesheet host object
     * represents.
     */
    private InputSource source_;

    /** The parsed stylesheet which this host object wraps (initialized lazily). */
    private CSSStyleSheet wrapped_;

    /** The HTML element which owns this stylesheet. */
    private final HTMLElement ownerNode_;

    /**
     * Creates a new empty stylesheet.
     */
    public Stylesheet() {
        ownerNode_ = null;
    }

    /**
     * Creates a new stylesheet representing the CSS stylesheet at the specified input source.
     * @param element the owning node
     *
     * @param source the input source which contains the CSS stylesheet which this stylesheet host object represents
     */
    public Stylesheet(final HTMLElement element, final InputSource source) {
        source_ = source;
        ownerNode_ = element;
        setParentScope(element.getWindow());
        setPrototype(getPrototype(Stylesheet.class));
    }

    /**
     * Returns the wrapped stylesheet, initializing it first if necessary.
     *
     * @return the wrapped stylesheet
     */
    private CSSStyleSheet getWrappedSheet() {
        if (wrapped_ == null) {
            if (source_ != null) {
                wrapped_ = parseCSS(source_);
            }
            else {
                wrapped_ = new CSSStyleSheetImpl();
            }
        }
        return wrapped_;
    }

    /**
     * Modifies the specified style object by adding any style rules which apply to the specified
     * element.
     *
     * @param style the style to modify
     * @param element the element to which style rules must apply in order for them to be added to
     *        the specified style
     */
    void modifyIfNecessary(final ComputedCSSStyleDeclaration style, final HTMLElement element) {
        final HtmlElement e = element.getHtmlElementOrDie();
        final CSSRuleList rules = getWrappedSheet().getCssRules();
        if (rules == null) {
            return;
        }
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            if (rule.getType() == CSSRule.STYLE_RULE) {
                final CSSStyleRuleImpl styleRule = (CSSStyleRuleImpl) rule;
                final SelectorList selectors = styleRule.getSelectors();
                for (int j = 0; j < selectors.getLength(); j++) {
                    final Selector selector = selectors.item(j);
                    final boolean selected = selects(selector, e);
                    if (selected) {
                        final org.w3c.dom.css.CSSStyleDeclaration dec = styleRule.getStyle();
                        for (int k = 0; k < dec.getLength(); k++) {
                            final String name = dec.item(k);
                            final String value = dec.getPropertyValue(name);
                            style.setLocalStyleAttribute(name, value);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns <tt>true</tt> if the specified selector selects the specified element.
     *
     * TODO: needs review; may not be fully finished.
     *
     * @param selector the selector to test
     * @param element the element to test
     * @return <tt>true</tt> if it does apply, <tt>false</tt> if it doesn't apply
     */
    boolean selects(final Selector selector, final HtmlElement element) {
        final String tagName = element.getTagName();
        switch (selector.getSelectorType()) {
            case Selector.SAC_ANY_NODE_SELECTOR:
                return true;
            case Selector.SAC_CHILD_SELECTOR:
                final DescendantSelector cs = (DescendantSelector) selector;
                final HtmlElement parent = (HtmlElement) element.getParentNode();
                return selects(cs.getSimpleSelector(), element) && parent != null
                    && selects(cs.getAncestorSelector(), parent);
            case Selector.SAC_DESCENDANT_SELECTOR:
                final DescendantSelector ds = (DescendantSelector) selector;
                if (selects(ds.getSimpleSelector(), element)) {
                    DomNode ancestor = element.getParentNode();
                    while (ancestor instanceof HtmlElement) {
                        if (selects(ds.getAncestorSelector(), (HtmlElement) ancestor)) {
                            return true;
                        }
                        ancestor = ancestor.getParentNode();
                    }
                }
                return false;
            case Selector.SAC_CONDITIONAL_SELECTOR:
                final ConditionalSelector conditional = (ConditionalSelector) selector;
                final Condition condition = conditional.getCondition();
                return selects(conditional.getSimpleSelector(), element) && selects(condition, element);
            case Selector.SAC_ELEMENT_NODE_SELECTOR:
                final ElementSelector es = (ElementSelector) selector;
                final String name = es.getLocalName();
                return name == null || tagName.equalsIgnoreCase(name);
            case Selector.SAC_ROOT_NODE_SELECTOR:
                return HtmlHtml.TAG_NAME.equalsIgnoreCase(tagName);
            case Selector.SAC_DIRECT_ADJACENT_SELECTOR:
                final SiblingSelector ss = (SiblingSelector) selector;
                final HtmlElement pre = (HtmlElement) element.getPreviousSibling();
                return pre != null && selects(ss.getSelector(), pre) && selects(ss.getSiblingSelector(), element);
            case Selector.SAC_NEGATIVE_SELECTOR:
                final NegativeSelector ns = (NegativeSelector) selector;
                return !selects(ns.getSimpleSelector(), element);
            case Selector.SAC_PSEUDO_ELEMENT_SELECTOR:
            case Selector.SAC_COMMENT_NODE_SELECTOR:
            case Selector.SAC_CDATA_SECTION_NODE_SELECTOR:
            case Selector.SAC_PROCESSING_INSTRUCTION_NODE_SELECTOR:
            case Selector.SAC_TEXT_NODE_SELECTOR:
                return false;
            default:
                getLog().error("Unknown CSS selector type '" + selector.getSelectorType() + "'.");
                return false;
        }
    }

    /**
     * Returns <tt>true</tt> if the specified condition selects the specified element.
     *
     * TODO: needs review; may not be fully finished.
     *
     * @param condition the condition to test
     * @param element the element to test
     * @return <tt>true</tt> if it does apply, <tt>false</tt> if it doesn't apply
     */
    boolean selects(final Condition condition, final HtmlElement element) {
        switch (condition.getConditionType()) {
            case Condition.SAC_ID_CONDITION:
                final AttributeCondition ac4 = (AttributeCondition) condition;
                return ac4.getValue().equals(element.getId());
            case Condition.SAC_CLASS_CONDITION:
                final AttributeCondition ac3 = (AttributeCondition) condition;
                final String v3 = ac3.getValue();
                final String a3 = element.getAttributeValue("class");
                return a3.equals(v3) || a3.startsWith(v3 + " ") || a3.endsWith(" " + v3) || a3.contains(" " + v3 + " ");
            case Condition.SAC_AND_CONDITION:
                final CombinatorCondition cc1 = (CombinatorCondition) condition;
                return selects(cc1.getFirstCondition(), element) && selects(cc1.getSecondCondition(), element);
            case Condition.SAC_ATTRIBUTE_CONDITION:
                final AttributeCondition ac1 = (AttributeCondition) condition;
                if (ac1.getSpecified()) {
                    return element.getAttributeValue(ac1.getLocalName()).equals(ac1.getValue());
                }
                else {
                    return element.hasAttribute(ac1.getLocalName());
                }
            case Condition.SAC_BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
                final AttributeCondition ac2 = (AttributeCondition) condition;
                final String v = ac2.getValue();
                final String a = element.getAttributeValue(ac2.getLocalName());
                return a.equals(v) || a.startsWith(v + "-") || a.endsWith("-" + v) || a.contains("-" + v + "-");
            case Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION:
                final AttributeCondition ac5 = (AttributeCondition) condition;
                final String v2 = ac5.getValue();
                final String a2 = element.getAttributeValue(ac5.getLocalName());
                return a2.equals(v2) || a2.startsWith(v2 + " ") || a2.endsWith(" " + v2) || a2.contains(" " + v2 + " ");
            case Condition.SAC_OR_CONDITION:
                final CombinatorCondition cc2 = (CombinatorCondition) condition;
                return selects(cc2.getFirstCondition(), element) || selects(cc2.getSecondCondition(), element);
            case Condition.SAC_NEGATIVE_CONDITION:
                final NegativeCondition nc = (NegativeCondition) condition;
                return !selects(nc.getCondition(), element);
            case Condition.SAC_ONLY_CHILD_CONDITION:
                return element.getParentNode().getChildNodes().getLength() == 1;
            case Condition.SAC_CONTENT_CONDITION:
                final ContentCondition cc = (ContentCondition) condition;
                return element.asText().contains(cc.getData());
            case Condition.SAC_LANG_CONDITION:
                final LangCondition lc = (LangCondition) condition;
                for (HtmlElement e = element; e != null; e = (HtmlElement) e.getParentNode()) {
                    if (e.getAttributeValue("lang").startsWith(lc.getLang())) {
                        return true;
                    }
                }
                return false;
            case Condition.SAC_ONLY_TYPE_CONDITION:
                final String tagName = element.getTagName();
                return element.getPage().getElementsByTagName(tagName).getLength() == 1;
            case Condition.SAC_POSITIONAL_CONDITION:
            case Condition.SAC_PSEUDO_CLASS_CONDITION:
                return false;
            default:
                getLog().error("Unknown CSS condition type '" + condition.getConditionType() + "'.");
                return false;
        }
    }

    /**
     * Parses the CSS at the specified input source. If anything at all goes wrong, this method
     * returns an empty stylesheet.
     *
     * @param source the source from which to retrieve the CSS to be parsed
     * @return the stylesheet parsed from the specified input source
     */
    private CSSStyleSheet parseCSS(final InputSource source) {
        CSSStyleSheet ss;
        try {
            final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
            ss = parser.parseStyleSheet(source, null, null);
        }
        catch (final Exception e) {
            getLog().error("Error parsing CSS from '" + toString(source) + "': " + e.getMessage(), e);
            ss = new CSSStyleSheetImpl();
        }
        catch (final Error e) {
            // SACParser sometimes throws Error: "Missing return statement in function"
            getLog().error("Error parsing CSS from '" + toString(source) + "': " + e.getMessage(), e);
            ss = new CSSStyleSheetImpl();
        }
        return ss;
    }

    /**
     * Parses the selectors at the specified input source. If anything at all goes wrong, this
     * method returns an empty selector list.
     *
     * @param source the source from which to retrieve the selectors to be parsed
     * @return the selectors parsed from the specified input source
     */
    SelectorList parseSelectors(final InputSource source) {
        SelectorList selectors;
        try {
            final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
            selectors = parser.parseSelectors(source);
        }
        catch (final Exception e) {
            getLog().error("Error parsing CSS selectors from '" + toString(source) + "': " + e.getMessage(), e);
            selectors = new SelectorListImpl();
        }
        catch (final Error e) {
            // SACParser sometimes throws Error: "Missing return statement in function"
            getLog().error("Error parsing CSS selectors from '" + toString(source) + "': " + e.getMessage(), e);
            selectors = new SelectorListImpl();
        }
        return selectors;
    }

    /**
     * Returns the contents of the specified input source.
     */
    private String toString(final InputSource source) {
        try {
            return IOUtils.toString(source.getCharacterStream());
        }
        catch (final IOException e) {
            return "";
        }
    }

    /**
     * For Firefox.
     * @return the owner
     */
    public Object jsxGet_ownerNode() {
        return ownerNode_;
    }

    /**
     * For Internet Explorer.
     * @return the owner
     */
    public Object jsxGet_owningElement() {
        return ownerNode_;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object getDefaultValue(final Class hint) {
        if (String.class.equals(hint) || hint == null) {
            if (getBrowserVersion().isIE()) {
                return "[object]"; // the super helpful IE solution
            }
            else {
                return "[object CSSStyleSheet]";
            }
        }
        else {
            return super.getDefaultValue(hint);
        }
    }
}
