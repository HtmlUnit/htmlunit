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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a TextRange.
 *
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535872.aspx">MSDN doc</a>
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class TextRange extends SimpleScriptable {

    private static final long serialVersionUID = -3763822832184277966L;

    /**
     * Retrieves the text contained within the range.
     * @return the text contained within the range
     */
    public String jsxGet_text() {
        final HtmlPage page = (HtmlPage) getWindow().getDomNodeOrDie();
        final HtmlElement focused = page.getFocusedElement();
        if (focused instanceof HtmlTextInput) {
            final HtmlTextInput input = (HtmlTextInput) focused;
            return input.getValueAttribute().substring(input.getSelectionStart(), input.getSelectionEnd());
        }
        else if (focused instanceof HtmlTextArea) {
            final HtmlTextArea input = (HtmlTextArea) focused;
            return input.getText().substring(input.getSelectionStart(), input.getSelectionEnd());
        }
        return "";
    }

    /**
     * Sets the text contained within the range.
     * @param text the text contained within the range
     */
    public void jsxSet_text(final String text) {
        final HtmlPage page = (HtmlPage) getWindow().getDomNodeOrDie();
        final HtmlElement focused = page.getFocusedElement();
        if (focused instanceof HtmlTextInput) {
            final HtmlTextInput input = (HtmlTextInput) focused;
            final String oldValue = input.getValueAttribute();
            input.setValueAttribute(oldValue.substring(0, input.getSelectionStart()) + text
                    + oldValue.substring(input.getSelectionEnd()));
        }
        else if (focused instanceof HtmlTextArea) {
            final HtmlTextArea input = (HtmlTextArea) focused;
            final String oldValue = input.getText();
            input.setText(oldValue.substring(0, input.getSelectionStart()) + text
                    + oldValue.substring(input.getSelectionEnd()));
        }
    }

    /**
     * Duplicates this instance.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536416.aspx">MSDN doc</a>
     * @return a duplicate
     */
    public Object jsxFunction_duplicate() {
        final TextRange range = new TextRange();
        range.setParentScope(getParentScope());
        range.setPrototype(getPrototype());
        return range;
    }
}
