/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * Contains information about a single style element, including its name, its value, and an index which
 * can be compared against other indices in order to determine precedence.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
public class StyleElement implements Comparable<StyleElement>, Serializable {
    /** CSS important property constant. */
    public static final String PRIORITY_IMPORTANT = "important";

    private final String name_;
    private final String value_;
    private final String priority_;
    private final long index_;
    private final SelectorSpecificity specificity_;

    /**
     * Creates a new instance.
     * @param name the style element's name
     * @param value the style element's value
     * @param priority the style element's priority like "important"
     * @param specificity the specificity of the rule providing this style information
     * @param index the style element's index
     */
    public StyleElement(final String name, final String value, final String priority,
            final SelectorSpecificity specificity, final long index) {
        name_ = name;
        value_ = value;
        priority_ = priority;
        index_ = index;
        specificity_ = specificity;
    }

    /**
     * Creates a new instance.
     * @param name the style element's name
     * @param value the style element's value
     * @param index the style element's index
     */
    protected StyleElement(final String name, final String value, final long index) {
        this(name, value, "", SelectorSpecificity.FROM_STYLE_ATTRIBUTE, index);
    }

    /**
     * Creates a new default instance.
     * @param name the style element's name
     * @param value the style element's value
     */
    public StyleElement(final String name, final String value) {
        this(name, value, Long.MIN_VALUE);
    }

    /**
     * Returns the style element's name.
     * @return the style element's name
     */
    public String getName() {
        return name_;
    }

    /**
     * Returns the style element's value.
     * @return the style element's value
     */
    public String getValue() {
        return value_;
    }

    /**
     * Returns the style element's priority.
     * @return the style element's priority
     */
    public String getPriority() {
        return priority_;
    }

    /**
     * Returns the specificity of the rule specifying this element.
     * @return the specificity
     */
    public SelectorSpecificity getSpecificity() {
        return specificity_;
    }

    /**
     * Returns the style element's index.
     * @return the style element's index
     */
    public long getIndex() {
        return index_;
    }

    /**
     * Returns {@code true} if this style element contains a default value. This method isn't
     * currently used anywhere because default style elements are applied before non-default
     * style elements, so the natural ordering results in correct precedence rules being applied
     * (i.e. default style elements don't override non-default style elements) without the need
     * for special checks.
     * @return {@code true} if this style element contains a default value
     */
    public boolean isDefault() {
        return index_ == Long.MIN_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[" + index_ + "]" + name_  + "=" + value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final StyleElement e) {
        if (e != null) {
            final long styleIndex = e.index_;
            // avoid conversion to long
            return (index_ < styleIndex) ? -1 : (index_ == styleIndex) ? 0 : 1;
        }
        return 1;
    }
}
