/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.javascript.host.event.KeyboardEvent;

/**
 * Keeps track of the typed keys.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class Keyboard {

    /**
     * Represents a single keyboard input action.
     *
     * <p>A {@code KeyAction} is either a typed character ({@link TypedChar})
     * or a key-code press or release ({@link KeyCodeAction}).
     * </p>
     */
    public sealed interface KeyAction permits TypedChar, KeyCodeAction { }

    /**
     * A character typed directly, e.g. via {@link Keyboard#type(char)}.
     *
     * @param ch the typed character
     */
    public record TypedChar(char ch) implements KeyAction { }

    /**
     * A key-code press or release, e.g. via {@link Keyboard#press(int)}
     * or {@link Keyboard#release(int)}.
     *
     * @param keyCode the DOM key code
     * @param pressed {@code true} if the key is being pressed,
     *                {@code false} if it is being released
     */
    public record KeyCodeAction(int keyCode, boolean pressed) implements KeyAction { }

    private final List<KeyAction> keys_ = new ArrayList<>();
    private final boolean startAtEnd_;

    /**
     * Creates a new instance.
     */
    public Keyboard() {
        this(false);
    }

    /**
     * Creates a new instance, specifying whether typing should start at the text end or not.
     * @param startAtEnd whether typing should start at the text end or not
     */
    public Keyboard(final boolean startAtEnd) {
        startAtEnd_ = startAtEnd;
    }

    /**
     * Types the specified character.
     * @param ch the character
     */
    public void type(final char ch) {
        keys_.add(new TypedChar(ch));
    }

    /**
     * Press the specified key code (without releasing it).
     * <p>
     * An example of predefined values is
     * {@link org.htmlunit.javascript.host.event.KeyboardEvent#DOM_VK_PAGE_DOWN}.
     * </p>
     *
     * @param keyCode the key code
     */
    public void press(final int keyCode) {
        if (keyCode >= KeyboardEvent.DOM_VK_A && keyCode <= KeyboardEvent.DOM_VK_Z) {
            throw new IllegalArgumentException("For key code " + keyCode + ", use type(char) instead");
        }
        keys_.add(new KeyCodeAction(keyCode, true));
    }

    /**
     * Releases the specified key code.
     * <p>
     * An example of predefined values is
     * {@link org.htmlunit.javascript.host.event.KeyboardEvent#DOM_VK_PAGE_DOWN}.
     * </p>
     *
     * @param keyCode the key code.
     */
    public void release(final int keyCode) {
        keys_.add(new KeyCodeAction(keyCode, false));
    }

    /**
     * Clears all keys.
     */
    public void clear() {
        keys_.clear();
    }

    /**
     * Returns the keys.
     *
     * @return the key actions
     */
    List<KeyAction> getKeys() {
        return keys_;
    }

    /**
     * Returns whether typing should start at the text end or not.
     * @return whether typing should start at the text end or not
     */
    public boolean isStartAtEnd() {
        return startAtEnd_;
    }
}
