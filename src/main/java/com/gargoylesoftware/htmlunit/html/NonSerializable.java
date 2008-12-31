/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
 * <p>Empty interface which flags a class as not being serializable. May be added to implementations of
 * {@link DomChangeListener} and {@link HtmlAttributeChangeListener} so that those instances will not
 * be serialized when {@link com.gargoylesoftware.htmlunit.Page}s are serialized.</p>
 *
 * <p>Note that this functionality is mainly intended for internal HtmlUnit usage, as there are few
 * reasons why a user-defined listener would want to avoid serialization.</p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public interface NonSerializable {

    // Empty.

}
