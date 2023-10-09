/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.html;

import static org.htmlunit.BrowserVersionFeatures.CSS_DIALOG_NONE;

import java.util.Map;

import org.htmlunit.SgmlPage;
import org.htmlunit.WebClient;
import org.htmlunit.javascript.HtmlUnitContextFactory;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.html.HTMLDialogElement;

/**
 * Wrapper for the HTML element "dialog".
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlDialog extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "dialog";

    private boolean modal_;
    private String returnValue_;

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlDialog(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        returnValue_ = "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        if (hasFeature(CSS_DIALOG_NONE)) {
            return DisplayStyle.NONE;
        }
        return DisplayStyle.INLINE;
    }

    /**
     * @return the {@code open} property
     */
    public boolean isOpen() {
        return hasAttribute("open");
    }

    /**
     * @return true if the dialog is open and modal.
     */
    public boolean isModal() {
        return modal_;
    }

    /**
     * Sets the open state.
     *
     * @param newValue the new value
     */
    public void setOpen(final boolean newValue) {
        if (newValue) {
            setAttribute("open", "");
        }
        else {
            removeAttribute("open");
            modal_ = false;
        }
    }

    /**
     *  Displays the dialog modelessly.
     */
    public void show() {
        if (!isOpen()) {
            setOpen(true);
        }
    }

    /**
     *  Displays the dialog modal.
     */
    public void showModal() {
        if (!isOpen()) {
            setOpen(true);
            modal_ = true;
        }
    }

    /**
     *  Displays the dialog modal.
     *  @param returnValue the return value
     */
    public void close(final String returnValue) {
        if (isOpen()) {
            setReturnValue(returnValue);
            setOpen(false);

            final SgmlPage page = getPage();
            final WebClient client = page.getWebClient();
            if (client.isJavaScriptEnabled()) {
                final HTMLDialogElement dialogElement = getScriptableObject();
                final Event event = new Event(dialogElement, Event.TYPE_CLOSE);

                final JavaScriptEngine jsEngine = (JavaScriptEngine) client.getJavaScriptEngine();
                final PostponedAction action = new PostponedAction(page, "Dialog.CloseEvent") {
                    @Override
                    public void execute() {
                        final HtmlUnitContextFactory cf = jsEngine.getContextFactory();
                        cf.call(cx -> dialogElement.dispatchEvent(event));
                    }
                };
                jsEngine.addPostponedAction(action);
            }
        }
        modal_ = false;
    }

    /**
     * @return the {@code returnValue} property
     */
    public String getReturnValue() {
        return returnValue_;
    }

    /**
     * Sets the open state.
     *
     * @param newValue the new value
     */
    public void setReturnValue(final String newValue) {
        if (newValue == null) {
            returnValue_ = "";
            return;
        }
        returnValue_ = newValue;
    }
}
