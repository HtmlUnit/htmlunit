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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlRadioButtonInput;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.configuration.JsxSymbol;

import java.util.List;

import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

/**
 * A JavaScript object for {@code RadioNodeList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
@JsxClass
public class RadioNodeList extends NodeList {

    /**
     * Creates an instance.
     */
    public RadioNodeList() {
        super();
    }

    /**
     * Creates an instance.
     *
     * @param domNode the {@link DomNode}
     */
    public RadioNodeList(final DomNode domNode) {
        super(domNode, true);
    }

    /**
     * Creates an instance.
     *
     * @param domNode the {@link DomNode}
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     */
    public RadioNodeList(final DomNode domNode, final boolean attributeChangeSensitive) {
        super(domNode, attributeChangeSensitive);
    }

    /**
     * Constructs an instance with an initial cache value.
     * @param domNode the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     */
    public RadioNodeList(final DomNode domNode, final List<DomNode> initialElements) {
        super(domNode, initialElements);
    }

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the value of the first checked radio button represented by radioNodeList.
     * @return the value of the first checked radio button represented by radioNodeList ("on" if value attribute
     * is not defined) or an empty string if no radio button is checked.
     * @see <a href="https://html.spec.whatwg.org/multipage/common-dom-interfaces.html#the-htmlformcontrolscollection-interface">HTML Standard</a>
     */
    @JsxGetter
    public String getValue() {
        for (final DomNode node : getElements()) {
            if (node instanceof HtmlRadioButtonInput && ((HtmlRadioButtonInput) node).isChecked()) {
                final String value = ((HtmlRadioButtonInput) node).getValueAttribute();
                return value == ATTRIBUTE_NOT_DEFINED ? "on" : value;
            }
        }

        return "";
    }

    /**
     * Checks the first radio button represented by radioNodeList that has value equal the specified value.
     * @param newValue the value of the radio button to be checked.
     * @see <a href="https://html.spec.whatwg.org/multipage/common-dom-interfaces.html#the-htmlformcontrolscollection-interface">HTML Standard</a>
     */
    @JsxSetter
    public void setValue(final String newValue) {
        for (final DomNode node : getElements()) {
            if (node instanceof HtmlRadioButtonInput) {
                String value = ((HtmlRadioButtonInput) node).getValueAttribute();
                if (value == ATTRIBUTE_NOT_DEFINED) {
                    value = "on";
                }
                if (newValue.equals(value)) {
                    ((HtmlRadioButtonInput) node).setChecked(true);
                    break;
                }
            }
        }
    }

    @JsxSymbol
    public Scriptable iterator() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }
}
