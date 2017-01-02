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

import java.io.IOException;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.Keyboard;

/**
 * Temporary work around for HtmlUnitDriver.
 *
 * @author Ronald Brill
 */
public final class PatchedHtmlUnitKeyboard extends HtmlUnitKeyboard {

    private KeyboardModifiersState modifiersState_ = new KeyboardModifiersState();
    private final HtmlUnitDriver parent_;
    private HtmlElement lastElement_;

    /**
     * Ctor.
     * @param parent the driver
     */
    public PatchedHtmlUnitKeyboard(final HtmlUnitDriver parent) {
        super(parent);
        this.parent_ = parent;
    }

    private HtmlUnitWebElement getElementToSend(final WebElement toElement) {
        WebElement sendToElement = toElement;
        if (sendToElement == null) {
            sendToElement = parent_.switchTo().activeElement();
        }

        return (HtmlUnitWebElement) sendToElement;
    }

    @Override
    public void sendKeys(final CharSequence... keysToSend) {
        final WebElement toElement = parent_.switchTo().activeElement();

        final HtmlUnitWebElement htmlElem = getElementToSend(toElement);
        htmlElem.sendKeys(false, keysToSend);
    }

    @Override
    public void sendKeys(final HtmlElement element, final String currentValue, final InputKeysContainer keysToSend,
        final boolean releaseAllAtEnd) {
        keysToSend.setCapitalization(modifiersState_.isShiftPressed());
        final String keysSequence = keysToSend.toString();

        if (element instanceof HtmlFileInput) {
            ((HtmlFileInput) element).setValueAttribute(keysSequence);
        }
        else {
            try {
                final Keyboard keyboard = asHtmlUnitKeyboard(lastElement_ != element, keysSequence, true);
                if (releaseAllAtEnd) {
                    if (isShiftPressed()) {
                        addToKeyboard(keyboard, Keys.SHIFT.charAt(0), false);
                    }
                    if (isAltPressed()) {
                        addToKeyboard(keyboard, Keys.ALT.charAt(0), false);
                    }
                    if (isCtrlPressed()) {
                        addToKeyboard(keyboard, Keys.CONTROL.charAt(0), false);
                    }
                }
                element.type(keyboard);
            }
            catch (final IOException e) {
                throw new WebDriverException(e);
            }
        }
        lastElement_ = element;
    }

    private Keyboard asHtmlUnitKeyboard(final boolean startAtEnd, final CharSequence keysSequence,
        final boolean isPress) {
        final Keyboard keyboard = new Keyboard(startAtEnd);
        for (int i = 0; i < keysSequence.length(); i++) {
            final char ch = keysSequence.charAt(i);
            addToKeyboard(keyboard, ch, isPress);
        }
        return keyboard;
    }

    private void addToKeyboard(final Keyboard keyboard, final char ch, final boolean isPress) {
        if (HtmlUnitKeyboardMapping.isSpecialKey(ch)) {
            final int keyCode = HtmlUnitKeyboardMapping.getKeysMapping(ch);
            if (isPress) {
                keyboard.press(keyCode);
                modifiersState_.storeKeyDown(ch);
            }
            else {
                keyboard.release(keyCode);
                modifiersState_.storeKeyUp(ch);
            }
        }
        else {
            keyboard.type(ch);
        }
    }

    @Override
    public void pressKey(final CharSequence keyToPress) {
        final WebElement toElement = parent_.switchTo().activeElement();

        final HtmlUnitWebElement htmlElement = getElementToSend(toElement);
        final HtmlElement element = (HtmlElement) htmlElement.element;
        try {
            element.type(asHtmlUnitKeyboard(lastElement_ != element, keyToPress, true));
        }
        catch (final IOException e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public void releaseKey(final CharSequence keyToRelease) {
        final WebElement toElement = parent_.switchTo().activeElement();

        final HtmlUnitWebElement htmlElement = getElementToSend(toElement);
        final HtmlElement element = (HtmlElement) htmlElement.element;
        try {
            element.type(asHtmlUnitKeyboard(lastElement_ != element, keyToRelease, false));
        }
        catch (final IOException e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public boolean isShiftPressed() {
        return modifiersState_.isShiftPressed();
    }

    @Override
    public boolean isCtrlPressed() {
        return modifiersState_.isCtrlPressed();
    }

    @Override
    public boolean isAltPressed() {
        return modifiersState_.isAltPressed();
    }
}
