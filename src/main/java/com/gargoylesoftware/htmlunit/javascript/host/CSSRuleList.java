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

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a CSSRuleList.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class CSSRuleList extends SimpleScriptable {

    private static final long serialVersionUID = 6068213884501456020L;

    private final org.w3c.dom.css.CSSRuleList rules_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public CSSRuleList() {
        rules_ = null;
    }

    CSSRuleList(final Stylesheet stylesheet) {
        rules_ = stylesheet.getWrappedSheet().getCssRules();
        setParentScope(stylesheet.getParentScope());
        setPrototype(getPrototype(getClass()));
    }
    
    /**
     * Returns the length of this list.
     * @return the length of this list.
     */
    public int jsxGet_length() {
        if (rules_ != null) {
            return rules_.getLength();
        }
        return 0;
    }

    /**
     * Returns the item in the given index.
     * @param index the index
     * @return the item in the given index
     */
    public Object jsxFunction_item(final int index) {
        return null;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Object[] getIds() {
        final List<String> idList = new ArrayList<String>();

        final int length = jsxGet_length();
        if (!getBrowserVersion().isIE()) {
            for (int i = 0; i < length; i++) {
                idList.add(Integer.toString(i));
            }

            idList.add("length");
            idList.add("item");
        }
        else {
            idList.add("length");

            for (int i = 0; i < length; i++) {
                idList.add(Integer.toString(i));
            }
        }
        return idList.toArray();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        if (name.equals("length") || name.equals("item")) {
            return true;
        }
        try {
            final int index = Integer.parseInt(name);
            final int length = jsxGet_length();
            if (index >= 0 && index < length) {
                return true;
            }
        }
        catch (final Exception e) {
            //ignore
        }
        return false;
    }
}
