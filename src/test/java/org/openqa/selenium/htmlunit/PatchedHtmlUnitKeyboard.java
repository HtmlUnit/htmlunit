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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;

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
        final KeyboardModifiersState modifiersState =
                getPrivateField(getClass().getSuperclass(), this, "modifiersState");
        keysToSend.setCapitalization(modifiersState.isShiftPressed());
        final String keysSequence = keysToSend.toString();
        if (element instanceof HtmlFileInput) {
            final HtmlFileInput fileInput = (HtmlFileInput) element;
            try {
                fileInput.setPaths(Paths.get(new URI(keysSequence)));
            }
            catch (final URISyntaxException e) {
                fileInput.setPaths(Paths.get(keysSequence));
            }
            return;
        }
        super.sendKeys(element, currentValue, keysToSend, releaseAllAtEnd);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(final Class<?> klass, final Object obj, final String fieldName) {
        try {
            final Field f = klass.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (T) f.get(obj);
        }
        catch (final Exception e) {
            return null;
        }
    }

    @Override
    public void pressKey(final CharSequence keyToPress) {
        super.pressKey(keyToPress);
        final KeyboardModifiersState modifiersState = getModifierState();
        for (int i = 0; i < keyToPress.length(); i++) {
            final char ch = keyToPress.charAt(i);
            modifiersState.storeKeyDown(ch);
        }
    }

    @Override
    public void releaseKey(final CharSequence keyToRelease) {
        super.releaseKey(keyToRelease);
        final KeyboardModifiersState modifiersState = getModifierState();
        for (int i = 0; i < keyToRelease.length(); i++) {
            final char ch = keyToRelease.charAt(i);
            modifiersState.storeKeyUp(ch);
        }
    }

    private KeyboardModifiersState getModifierState() {
        final WebDriver driver = getPrivateField(getClass().getSuperclass(), this, "parent");
        final Mouse mouse = ((HasInputDevices) driver).getMouse();
        final HtmlUnitKeyboard htmlUnitKeyboard = getPrivateField(mouse.getClass(), mouse, "keyboard");
        return getPrivateField(htmlUnitKeyboard.getClass(), htmlUnitKeyboard, "modifiersState");
    }
}
