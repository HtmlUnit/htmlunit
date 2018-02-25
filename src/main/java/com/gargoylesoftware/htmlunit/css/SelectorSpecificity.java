/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.css.parser.condition.AttributeCondition;
import com.gargoylesoftware.css.parser.condition.BeginHyphenAttributeCondition;
import com.gargoylesoftware.css.parser.condition.Condition;
import com.gargoylesoftware.css.parser.condition.OneOfAttributeCondition;
import com.gargoylesoftware.css.parser.condition.PrefixAttributeCondition;
import com.gargoylesoftware.css.parser.condition.SubstringAttributeCondition;
import com.gargoylesoftware.css.parser.condition.SuffixAttributeCondition;
import com.gargoylesoftware.css.parser.selector.ChildSelector;
import com.gargoylesoftware.css.parser.selector.DescendantSelector;
import com.gargoylesoftware.css.parser.selector.DirectAdjacentSelector;
import com.gargoylesoftware.css.parser.selector.ElementSelector;
import com.gargoylesoftware.css.parser.selector.GeneralAdjacentSelector;
import com.gargoylesoftware.css.parser.selector.PseudoElementSelector;
import com.gargoylesoftware.css.parser.selector.Selector;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * Calculates a selector's specificity.
 * @see <a href="http://www.w3.org/TR/CSS21/cascade.html#specificity">W3C CSS21</a>
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class SelectorSpecificity implements Comparable<SelectorSpecificity>, Serializable {
    private static final Log LOG = LogFactory.getLog(SelectorSpecificity.class);

    /**
     * The specificity for declarations made in the style attributes of an element.
     */
    public static final SelectorSpecificity FROM_STYLE_ATTRIBUTE = new SelectorSpecificity(1, 0, 0, 0);
    /**
     * The specificity for browser defaults.
     */
    public static final SelectorSpecificity DEFAULT_STYLE_ATTRIBUTE = new SelectorSpecificity(0, 0, 0, 0);

    private int fieldA_;
    private int fieldB_;
    private int fieldC_;
    private int fieldD_;

    /**
     * Ctor.
     * @param selector the selector to read from
     */
    public SelectorSpecificity(final Selector selector) {
        readSelectorSpecificity(selector);
    }

    private SelectorSpecificity(final int a, final int b, final int c, final int d) {
        fieldA_ = a;
        fieldB_ = b;
        fieldC_ = c;
        fieldD_ = d;
    }

    private void readSelectorSpecificity(final Selector selector) {
        switch (selector.getSelectorType()) {
            case DESCENDANT_SELECTOR:
                final DescendantSelector ds = (DescendantSelector) selector;
                readSelectorSpecificity(ds.getAncestorSelector());
                readSelectorSpecificity(ds.getSimpleSelector());
                return;
            case CHILD_SELECTOR:
                final ChildSelector cs = (ChildSelector) selector;
                readSelectorSpecificity(cs.getAncestorSelector());
                readSelectorSpecificity(cs.getSimpleSelector());
                return;
            case ELEMENT_NODE_SELECTOR:
                final ElementSelector es = (ElementSelector) selector;
                if (es.getLocalName() != null) {
                    fieldD_++;
                }
                if (es.getConditions() != null) {
                    for (Condition condition : es.getConditions()) {
                        readSelectorSpecificity(condition);
                    }
                }
                return;
            case PSEUDO_ELEMENT_SELECTOR:
                final PseudoElementSelector pes = (PseudoElementSelector) selector;
                final String pesName = pes.getLocalName();
                if (pesName != null) {
                    fieldD_++;
                }
                return;
            case DIRECT_ADJACENT_SELECTOR:
                final DirectAdjacentSelector das = (DirectAdjacentSelector) selector;
                readSelectorSpecificity(das.getSelector());
                readSelectorSpecificity(das.getSiblingSelector());
                return;
            case GENERAL_ADJACENT_SELECTOR:
                final GeneralAdjacentSelector gas = (GeneralAdjacentSelector) selector;
                readSelectorSpecificity(gas.getSelector());
                readSelectorSpecificity(gas.getSiblingSelector());
                return;
            default:
                LOG.warn("Unhandled CSS selector type for specificity computation: '"
                        + selector.getSelectorType() + "'.");
                return;
        }
    }

    private void readSelectorSpecificity(final Condition condition) {
        switch (condition.getConditionType()) {
            case ID_CONDITION:
                fieldB_++;
                return;
            case CLASS_CONDITION:
                fieldC_++;
                return;
            case ATTRIBUTE_CONDITION:
                if ("id".equalsIgnoreCase(((AttributeCondition) condition).getLocalName())) {
                    fieldB_++;
                }
                else {
                    fieldC_++;
                }
                return;
            case SUBSTRING_ATTRIBUTE_CONDITION:
                if ("id".equalsIgnoreCase(((SubstringAttributeCondition) condition).getLocalName())) {
                    fieldB_++;
                }
                else {
                    fieldC_++;
                }
                return;
            case SUFFIX_ATTRIBUTE_CONDITION:
                if ("id".equalsIgnoreCase(((SuffixAttributeCondition) condition).getLocalName())) {
                    fieldB_++;
                }
                else {
                    fieldC_++;
                }
                return;
            case PREFIX_ATTRIBUTE_CONDITION:
                if ("id".equalsIgnoreCase(((PrefixAttributeCondition) condition).getLocalName())) {
                    fieldB_++;
                }
                else {
                    fieldC_++;
                }
                return;
            case BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
                if ("id".equalsIgnoreCase(((BeginHyphenAttributeCondition) condition).getLocalName())) {
                    fieldB_++;
                }
                else {
                    fieldC_++;
                }
                return;
            case ONE_OF_ATTRIBUTE_CONDITION:
                if ("id".equalsIgnoreCase(((OneOfAttributeCondition) condition).getLocalName())) {
                    fieldB_++;
                }
                else {
                    fieldC_++;
                }
                return;
            case PSEUDO_CLASS_CONDITION:
                fieldD_++;
                return;
            default:
                LOG.warn("Unhandled CSS condition type for specifity computation: '"
                        + condition.getConditionType() + "'.");
                return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return fieldA_ + "," + fieldB_ + "," + fieldC_ + "," + fieldD_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final SelectorSpecificity other) {
        if (fieldA_ != other.fieldA_) {
            return fieldA_ - other.fieldA_;
        }
        else if (fieldB_ != other.fieldB_) {
            return fieldB_ - other.fieldB_;
        }
        else if (fieldC_ != other.fieldC_) {
            return fieldC_ - other.fieldC_;
        }
        else if (fieldD_ != other.fieldD_) {
            return fieldD_ - other.fieldD_;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fieldA_;
        result = prime * result + fieldB_;
        result = prime * result + fieldC_;
        result = prime * result + fieldD_;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SelectorSpecificity other = (SelectorSpecificity) obj;
        if (fieldA_ != other.fieldA_) {
            return false;
        }
        if (fieldB_ != other.fieldB_) {
            return false;
        }
        if (fieldC_ != other.fieldC_) {
            return false;
        }
        if (fieldD_ != other.fieldD_) {
            return false;
        }
        return true;
    }
}
