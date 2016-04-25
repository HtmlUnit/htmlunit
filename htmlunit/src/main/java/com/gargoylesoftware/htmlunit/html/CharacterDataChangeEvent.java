/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.EventObject;

/**
 * This is the event class for notifications about changes to the Character Data.
 *
 * @author Ahmed Ashour
 * @see CharacterDataChangeListener
 */
public class CharacterDataChangeEvent extends EventObject {

    private final String oldValue_;

    /**
     * Constructs a new CharacterDataChangeEvent from the given character data and the old value.
     *
     * @param characterData the character data which is changed
     * @param oldValue the old value
     */
    public CharacterDataChangeEvent(final DomCharacterData characterData, final String oldValue) {
        super(characterData);
        oldValue_ = oldValue;
    }

    /**
     * Returns the character data that was changed.
     * @return the character data that was changed
     */
    public DomCharacterData getCharacterData() {
        return (DomCharacterData) getSource();
    }

    /**
     * Returns the old value.
     * @return the old value
     */
    public String getOldValue() {
        return oldValue_;
    }
}
