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

import java.util.EventObject;

/**
 * This is the event class for notifications about changes to the DOM structure.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @see DomChangeListener
 */
public class DomChangeEvent extends EventObject {

    private final DomNode changedNode_;

    /**
     * Constructs a new DomChangeEvent from the given parent node and a changed node.
     *
     * @param parentNode the parent of the node that was changed
     * @param changedNode the node that has been added or deleted
     */
    public DomChangeEvent(final DomNode parentNode, final DomNode changedNode) {
        super(parentNode);
        changedNode_ = changedNode;
    }

    /**
     * Returns the parent of the node that was changed.
     * @return the parent of the node that was changed
     */
    public DomNode getParentNode() {
        return (DomNode) getSource();
    }

    /**
     * Returns the node that has been added or deleted.
     * @return the node that has been added or deleted
     */
    public DomNode getChangedNode() {
        return changedNode_;
    }
}
