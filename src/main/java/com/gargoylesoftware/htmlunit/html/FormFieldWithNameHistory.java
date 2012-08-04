/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import java.util.Collection;

/**
 * Interface for form fields where the original field name still matters even once it
 * has been changed.
 * <p>
 * Example:<br/>
 * With<br/>
 * <code>&lt;input name="foo"/&gt;</code><br/>
 * following script will work<br/>
 * <code>
 * theForm.foo.name = 'newName';<br/>
 * theForm.foo.value = 'some value';
 * </code><br/>
 * Depending on the simulated browser the form field is reachable only through its original name
 * or through all the names it has had.
 * @version $Revision$
 * @author Marc Guillemot
 */
public interface FormFieldWithNameHistory {
    /**
     * Gets the first value of the <code>name</code> attribute of this field before any change.
     * @return the original name (which is the same as the current one when no change has been made)
     */
    String getOriginalName();

    /**
     * Get all the names this field had before the current one.
     * @return an empty collection if the name attribute has never been changed.
     */
    Collection<String> getPreviousNames();
}
