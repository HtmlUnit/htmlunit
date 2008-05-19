/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import java.util.Iterator;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * Tests for compatibility with version 1.1.2 of the <a href="http://jquery.com/">jQuery JavaScript library</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class JQuery112Test extends JQueryTestBase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getVersion() {
        return "1.1.2";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verify(final Iterator<HtmlElement> i, final boolean ie) throws Exception {
        ok(i, "core module: Basic requirements", 0, 7);
        ok(i, "core module: $()", 0, 1);
        ok(i, "core module: length", 0, 1);
        ok(i, "core module: size()", 0, 1);
        ok(i, "core module: get()", 0, 1);
        ok(i, "core module: get(Number)", 0, 1);
        ok(i, "core module: add(String|Element|Array)", 0, 7);
        ok(i, "core module: each(Function)", 0, 1);
        ok(i, "core module: index(Object)", 0, 8);
        i.next(); // ok(i, "core module: attr(String)", 2, 13); // TODO: all 15 pass!
        ok(i, "core module: attr(String, Function)", 0, 2);
        ok(i, "core module: attr(Hash)", 0, 1);
        ok(i, "core module: attr(String, Object)", 0, 7);
        ok(i, "core module: css(String|Hash)", 0, 8);
        ok(i, "core module: css(String, Object)", 0, 7);
        ok(i, "core module: text()", 0, 1);
        ok(i, "core module: wrap(String|Element)", 0, 4);
        ok(i, "core module: append(String|Element|Array<Element>|jQuery)", 0, 10);
        ok(i, "core module: appendTo(String|Element|Array<Element>|jQuery)", 0, 5);
        ok(i, "core module: prepend(String|Element|Array<Element>|jQuery)", 0, 5);
        ok(i, "core module: prependTo(String|Element|Array<Element>|jQuery)", 0, 5);
        ok(i, "core module: before(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: insertBefore(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: after(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: insertAfter(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: end()", 0, 3);
        ok(i, "core module: find(String)", 0, 1);
        ok(i, "core module: clone()", 0, 3);
        ok(i, "core module: is(String)", 2, 24);
        ok(i, "core module: $.extend(Object, Object)", 0, 2);
        ok(i, "core module: $.extend(Object, Object, Object, Object)", 0, 4);
        ok(i, "core module: val()", 0, 2);
        ok(i, "core module: val(String)", 0, 2);
        ok(i, "core module: html(String)", 0, 1);
        ok(i, "core module: filter()", 0, 4);
        ok(i, "core module: not()", 0, 3);
        ok(i, "core module: siblings([String])", 0, 4);
        ok(i, "core module: children([String])", 0, 3);
        ok(i, "core module: parent[s]([String])", 0, 8);
        ok(i, "core module: next/prev([String])", 0, 8);
        ok(i, "core module: show()", 0, 1);
        ok(i, "core module: addClass(String)", 0, 1);
        ok(i, "core module: removeClass(String) - simple", 0, 1);
        ok(i, "core module: removeClass(String) - add three classes and remove again", 0, 1);
        ok(i, "core module: toggleClass(String)", 0, 3);
        ok(i, "core module: removeAttr(String", 0, 1);
        ok(i, "core module: text(String)", 0, 1);
        ok(i, "core module: $.each(Object,Function)", 0, 8);
        ok(i, "core module: $.prop", 0, 2);
        ok(i, "core module: $.className", 0, 6);
        ok(i, "core module: remove()", 0, 4);
        ok(i, "core module: empty()", 0, 2);
        ok(i, "core module: eq(), gt(), lt(), contains()", 0, 4);
        ok(i, "core module: click() context", 0, 2);

        ok(i, "selector module: expressions - element", 0, 6);
        ok(i, "selector module: expressions - id", 1, 12);
        ok(i, "selector module: expressions - class", 0, 4);
        ok(i, "selector module: expressions - multiple", 0, 4);
        ok(i, "selector module: expressions - child and adjacent", 0, 14);
        ok(i, "selector module: expressions - attributes", 0, 19);
        ok(i, "selector module: expressions - pseudo (:) selctors", 0, 30);
        ok(i, "selector module: expressions - basic xpath", 1, 14);

        final int failed;
        final int passed;
        if (ie) {
            failed = 3;
            passed = 0;
        }
        else {
            failed = 0;
            passed = 1;
        }

        ok(i, "event module: toggle(Function, Function) - add toggle event and fake a few clicks", failed, passed);
        ok(i, "event module: unbind(event)", 0, 4);
        ok(i, "event module: trigger(event, [data]", 0, 3);
        ok(i, "event module: bind() with data", 0, 2);
        ok(i, "event module: bind() with data and trigger() with data", 0, 4);

        ok(i, "fx module: animate(Hash, Object, Function) - assert that animate doesn't modify its arguments", 0, 1);
        ok(i, "fx module: toggle()", 0, 3);
    }

}
