/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.css;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import com.gargoylesoftware.css.parser.selector.SelectorSpecificity;

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

    /** The current style element index. */
    private static final AtomicLong ElementIndex_ = new AtomicLong();

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
        specificity_ = specificity;
        index_ = index;
    }

    /**
     * Creates a new instance.
     * @param name the style element's name
     * @param value the style element's value
     * @param priority the style element's priority like "important"
     * @param specificity the specificity of the rule providing this style information
     */
    public StyleElement(final String name, final String value, final String priority,
            final SelectorSpecificity specificity) {
        this(name, value, priority, specificity, StyleElement.ElementIndex_.incrementAndGet());
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
     * @return true if priority is 'important'
     */
    public boolean isImportant() {
        return StyleElement.PRIORITY_IMPORTANT.equals(getPriority());
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
        if (e == null) {
            return 1;
        }

        if (isImportant()) {
            if (!e.isImportant()) {
                return 1;
            }
        }
        else {
            if (e.isImportant()) {
                return -1;
            }
        }

        final int comp = getSpecificity().compareTo(e.getSpecificity());
        if (comp == 0) {
            return Long.compare(getIndex(), e.getIndex());
        }
        return comp;
    }
}
