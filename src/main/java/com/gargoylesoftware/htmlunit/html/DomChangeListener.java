/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import java.io.Serializable;

/**
 * Implementations of this interface receive notifications of changes to the DOM structure.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @see DomChangeEvent
 */
public interface DomChangeListener extends Serializable {

    /**
     * Notification that a new node was added. Called after the node is added.
     *
     * @param event the node addition event
     */
    void nodeAdded(final DomChangeEvent event);

    /**
     * Notification that a new node was deleted. Called after the node is deleted.
     *
     * @param event the node deletion event
     */
    void nodeDeleted(final DomChangeEvent event);

}
