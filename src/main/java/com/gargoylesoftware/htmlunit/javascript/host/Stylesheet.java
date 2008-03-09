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

import java.io.StringReader;
import java.util.List;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.parser.CSSOMParser;
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

    /** The parser used to parse CSS; its parent stylesheet and parent rule should not be set. */
    private static final CSSOMParser PARSER = new CSSOMParser();

    /**
     * The input source which contains the CSS stylesheet which this stylesheet host object
     * represents.
     */
    private InputSource source_;

    /** The parsed stylesheet which this host object wraps (initialized lazily). */
    private CSSStyleSheet wrapped_;

    /**
     * Creates a new empty stylesheet.
     */
    public Stylesheet() {
        this(null);
    }

    /**
     * Creates a new stylesheet representing the CSS stylesheet at the specified input source.
     *
     * @param source the input source which contains the CSS stylesheet which this stylesheet host object represents
     */
    public Stylesheet(final InputSource source) {
        source_ = source;
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
        final HtmlPage page = e.getPage();
        final CSSRuleList rules = getWrappedSheet().getCssRules();
        if (rules == null) {
            return;
        }
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            if (rule.getType() == CSSRule.STYLE_RULE) {
                final CSSStyleRule styleRule = (CSSStyleRule) rule;
                final String s = styleRule.getSelectorText();
                final SelectorList selectors = parseSelectors(new InputSource(new StringReader(s)));
                for (int j = 0; j < selectors.getLength(); j++) {
                    final Selector selector = selectors.item(j);
                    final String xpath = translateToXPath(selector);
                    if (xpath != null) {
                        final List< ? extends Object> results = page.getByXPath(xpath);
                        if (results.contains(e)) {
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
            ss = PARSER.parseStyleSheet(source);
        }
        catch (final Exception e) {
            getLog().error(e.getMessage(), e);
            ss = new CSSStyleSheetImpl();
        }
        return ss;
    }

    /**
     * Parses the selectors at the specified input source. If anything at all goes wrong, this
     * method returns an empty stylesheet.
     *
     * @param source the source from which to retrieve the selectors to be parsed
     * @return the selectors parsed from the specified input source
     */
    SelectorList parseSelectors(final InputSource source) {
        SelectorList selectors;
        try {
            selectors = PARSER.parseSelectors(source);
        }
        catch (final Exception e) {
            getLog().error(e.getMessage(), e);
            selectors = new SelectorListImpl();
        }
        return selectors;
    }

    /**
     * Translates the specified selector to an XPath expression. If the specified selector is a type
     * of selector that we don't care about, this method returns <tt>null</tt>. See <a
     * href="http://plasmasturm.org/log/444/">this page</a> for more information.
     *
     * @param selector the selector to be translated
     * @return an XPath version of the specified selector
     */
    String translateToXPath(final Selector selector) {
        final String response;
        switch (selector.getSelectorType()) {
            case Selector.SAC_ANY_NODE_SELECTOR:
                return "*";
            case Selector.SAC_CDATA_SECTION_NODE_SELECTOR:
                return null;
            case Selector.SAC_CHILD_SELECTOR:
                final DescendantSelector cs = (DescendantSelector) selector;
                final String p = translateToXPath(cs.getAncestorSelector());
                final String c = translateToXPath(cs.getSimpleSelector());
                response = p + "/" + c;
                break;
            case Selector.SAC_COMMENT_NODE_SELECTOR:
                return null;
            case Selector.SAC_CONDITIONAL_SELECTOR:
                final ConditionalSelector conditional = (ConditionalSelector) selector;
                String e = translateToXPath(conditional.getSimpleSelector());
                final String cond = translateToXPath(conditional.getCondition());
                if (cond != null) {
                    if (e.equals("*")) {
                        e = "//*";
                    }
                    response = e + "[" + cond + "]";
                }
                else {
                    response = e;
                }
                break;
            case Selector.SAC_DESCENDANT_SELECTOR:
                final DescendantSelector ds = (DescendantSelector) selector;
                final String a = translateToXPath(ds.getAncestorSelector());
                final String d = translateToXPath(ds.getSimpleSelector());
                response = a + "//" + d;
                break;
            case Selector.SAC_DIRECT_ADJACENT_SELECTOR:
                return null;
            case Selector.SAC_ELEMENT_NODE_SELECTOR:
                final ElementSelector es = (ElementSelector) selector;
                final String name = es.getLocalName();
                if (name != null) {
                    response = "//" + name;
                }
                else {
                    response = "*";
                }
                break;
            case Selector.SAC_NEGATIVE_SELECTOR:
                return null;
            case Selector.SAC_PROCESSING_INSTRUCTION_NODE_SELECTOR:
                return null;
            case Selector.SAC_PSEUDO_ELEMENT_SELECTOR:
                return null;
            case Selector.SAC_ROOT_NODE_SELECTOR:
                return "html";
            case Selector.SAC_TEXT_NODE_SELECTOR:
                return null;
            default:
                getLog().error("Unknown selector type '" + selector.getSelectorType() + "'.");
                return null;
        }
        
        return response.replaceAll("/{3,}", "//");
    }

    /**
     * Translates the specified selector condition to an XPath expression. If the specified
     * condition is a type of condition that we don't care about, this method returns <tt>null</tt>.
     * See <a href="http://plasmasturm.org/log/444/">this page</a> for more information.
     *
     * @param condition the selector condition to be translated
     * @return an XPath version of the specified selector condition
     */
    private String translateToXPath(final Condition condition) {
        switch (condition.getConditionType()) {
            case Condition.SAC_AND_CONDITION:
                final CombinatorCondition cc1 = (CombinatorCondition) condition;
                return "(" + translateToXPath(cc1.getFirstCondition()) + ") and ("
                                + translateToXPath(cc1.getSecondCondition()) + ")";
            case Condition.SAC_ATTRIBUTE_CONDITION:
                final AttributeCondition ac1 = (AttributeCondition) condition;
                if (ac1.getSpecified()) {
                    return "@" + ac1.getLocalName() + " = '" + ac1.getValue() + "'";
                }
                else {
                    return "@" + ac1.getLocalName();
                }
            case Condition.SAC_BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
                final AttributeCondition ac2 = (AttributeCondition) condition;
                return "@" + ac2.getLocalName() + " = '" + ac2.getValue() + "' " + "or starts-with( @"
                                + ac2.getLocalName() + ", concat( '" + ac2.getValue() + "', '-' ) )";
            case Condition.SAC_CLASS_CONDITION:
                final AttributeCondition ac3 = (AttributeCondition) condition;
                return "contains( concat(' ', @class, ' '), concat(' ', '" + ac3.getValue() + "', ' ') )";
            case Condition.SAC_CONTENT_CONDITION:
                return null;
            case Condition.SAC_ID_CONDITION:
                final AttributeCondition ac4 = (AttributeCondition) condition;
                return "@id='" + ac4.getValue() + "'";
            case Condition.SAC_LANG_CONDITION:
                return null;
            case Condition.SAC_NEGATIVE_CONDITION:
                return null;
            case Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION:
                final AttributeCondition ac5 = (AttributeCondition) condition;
                return "contains( concat(' ', @" + ac5.getLocalName() + ", ' '), " + "concat(' ', '"
                                + ac5.getValue() + "', ' ') )";
            case Condition.SAC_ONLY_CHILD_CONDITION:
                return null;
            case Condition.SAC_ONLY_TYPE_CONDITION:
                return null;
            case Condition.SAC_OR_CONDITION:
                final CombinatorCondition cc2 = (CombinatorCondition) condition;
                return "(" + translateToXPath(cc2.getFirstCondition()) + ") or ("
                                + translateToXPath(cc2.getSecondCondition()) + ")";
            case Condition.SAC_POSITIONAL_CONDITION:
                return null;
            case Condition.SAC_PSEUDO_CLASS_CONDITION:
                return null;
            default:
                return null;
        }
    }

}
