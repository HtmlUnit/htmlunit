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
package com.gargoylesoftware.htmlunit.libraries;

import java.util.Iterator;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * Tests for compatibility with version 1.1.3.1 of the <a href="http://jquery.com/">jQuery JavaScript library</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class JQuery1131Test extends JQueryTestBase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getVersion() {
        return "1.1.3.1";
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void testJQueryWithFirefox2() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        super.testJQueryWithFirefox2();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verify(final Iterator<HtmlElement> i, final boolean ie) throws Exception {

        final int failedDollar, passedDollar;
        final int failedAppend, passedAppend;
        final int failedElement, passedElement;
        final int failedMultiple, passedMultiple;

        if (ie) {
            failedDollar = 1; passedDollar = 2;
            failedAppend = 0; passedAppend = 17; // TODO: all 17 pass, but one shoud fail!
            failedElement = 1; passedElement = 8;
            failedMultiple = 4; passedMultiple = 0;
        }
        else {
            failedDollar = 0; passedDollar = 3;
            failedAppend = 0; passedAppend = 17;
            failedElement = 0; passedElement = 9;
            failedMultiple = 0; passedMultiple = 4;
        }

        ok(i, "core module: Basic requirements", 0, 7);
        ok(i, "core module: $()", failedDollar, passedDollar);
        ok(i, "core module: isFunction", 0, 21);
        ok(i, "core module: length", 0, 1);
        ok(i, "core module: size()", 0, 1);
        ok(i, "core module: get()", 0, 1);
        ok(i, "core module: get(Number)", 0, 1);
        ok(i, "core module: add(String|Element|Array)", 0, 7);
        ok(i, "core module: each(Function)", 0, 1);
        ok(i, "core module: index(Object)", 0, 8);
        ok(i, "core module: attr(String)", 0, 13);
        ok(i, "core module: attr(String, Function)", 0, 2);
        ok(i, "core module: attr(Hash)", 0, 1);
        ok(i, "core module: attr(String, Object)", 0, 8);
        ok(i, "core module: css(String|Hash)", 0, 19);
        ok(i, "core module: css(String, Object)", 0, 18);
        ok(i, "core module: text()", 0, 1);
        ok(i, "core module: wrap(String|Element)", 0, 7);
        ok(i, "core module: append(String|Element|Array<Element>|jQuery)", failedAppend, passedAppend);
        ok(i, "core module: appendTo(String|Element|Array<Element>|jQuery)", 0, 6);
        ok(i, "core module: prepend(String|Element|Array<Element>|jQuery)", 0, 5);
        ok(i, "core module: prependTo(String|Element|Array<Element>|jQuery)", 0, 6);
        ok(i, "core module: before(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: insertBefore(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: after(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: insertAfter(String|Element|Array<Element>|jQuery)", 0, 4);
        ok(i, "core module: end()", 0, 3);
        ok(i, "core module: find(String)", 0, 1);
        ok(i, "core module: clone()", 0, 3);
        ok(i, "core module: is(String)", 0, 26);
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
        ok(i, "core module: removeClass(String) - simple", 0, 3);
        ok(i, "core module: toggleClass(String)", 0, 3);
        ok(i, "core module: removeAttr(String", 0, 1);
        ok(i, "core module: text(String)", 0, 1);
        ok(i, "core module: $.each(Object,Function)", 0, 8);
        ok(i, "core module: $.prop", 0, 2);
        ok(i, "core module: $.className", 0, 6);
        ok(i, "core module: remove()", 0, 4);
        ok(i, "core module: empty()", 0, 2);
        ok(i, "core module: eq(), gt(), lt(), contains()", 0, 4);

        ok(i, "selector module: element", failedElement, passedElement);
        ok(i, "selector module: broken", 0, 7);
        ok(i, "selector module: id", 0, 24);
        ok(i, "selector module: class", 0, 16);
        ok(i, "selector module: multiple", failedMultiple, passedMultiple);
        ok(i, "selector module: child and adjacent", 0, 19);
        ok(i, "selector module: attributes", 0, 20);
        ok(i, "selector module: pseudo (:) selectors", 0, 30);
        ok(i, "selector module: basic xpath", 0, 15);

        ok(i, "event module: bind()", 0, 12);
        ok(i, "event module: click()", 0, 3);
        ok(i, "event module: unbind(event)", 0, 6);
        ok(i, "event module: trigger(event, [data]", 0, 3);
        ok(i, "event module: toggle(Function, Function)", 0, 4);

        ok(i, "fx module: animate(Hash, Object, Function)", 0, 1);
        ok(i, "fx module: toggle()", 0, 3);
    }

}
