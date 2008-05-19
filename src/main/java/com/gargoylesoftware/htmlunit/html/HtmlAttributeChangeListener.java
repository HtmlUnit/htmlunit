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
package com.gargoylesoftware.htmlunit.html;

/**
 * Implementations of this interface receive notifications of changes to the attribute
 * list on the HtmlElement.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @see HtmlAttributeChangeEvent
 */
public interface HtmlAttributeChangeListener {

    /**
     * Notification that a new attribute was added to the HtmlElement. Called after the attribute is added.
     *
     * @param event the attibute change event
     */
    void attributeAdded(final HtmlAttributeChangeEvent event);

    /**
     * Notification that an existing attribute has been removed from the HtmlElement.
     * Called after the attribute is removed.
     *
     * @param event the attribute change event
     */
    void attributeRemoved(final HtmlAttributeChangeEvent event);

    /**
     * Notification that an attribute on the HtmlElement has been replaced. Called after the attribute is replaced.
     *
     * @param event the attribute change event
     */
    void attributeReplaced(final HtmlAttributeChangeEvent event);

}
