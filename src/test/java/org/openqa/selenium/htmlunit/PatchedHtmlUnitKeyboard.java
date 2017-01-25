/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

package org.openqa.selenium.htmlunit;

import java.lang.reflect.Field;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;

/**
 * Temporary work around for HtmlUnitDriver.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
public final class PatchedHtmlUnitKeyboard extends HtmlUnitKeyboard {

    /**
     * Constructor.
     *
     * @param parent the driver
     */
    public PatchedHtmlUnitKeyboard(final HtmlUnitDriver parent) {
        super(parent);
    }

    /**
     * {@inheritDoc}
     * Overridden because .setValueAttribute moved from HtmlFileInput to HtmlInput
     */
    @Override
    public void sendKeys(final HtmlElement element, final String currentValue, final InputKeysContainer keysToSend,
            final boolean releaseAllAtEnd) {
        final KeyboardModifiersState modifiersState = (KeyboardModifiersState) getPrivateField("modifiersState");
        keysToSend.setCapitalization(modifiersState.isShiftPressed());
        String keysSequence = keysToSend.toString();
        if (element instanceof HtmlFileInput) {
            HtmlFileInput fileInput = (HtmlFileInput) element;
            fileInput.setValueAttribute(keysSequence);
            return;
        }
        super.sendKeys(element, currentValue, keysToSend, releaseAllAtEnd);
    }

    private Object getPrivateField(final String fieldName) {
        try {
            final Field f = getClass().getSuperclass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(this);
        } catch (Exception e) {
            return null;
        }
    }
}
