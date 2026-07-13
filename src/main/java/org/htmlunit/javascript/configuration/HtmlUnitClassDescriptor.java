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
package org.htmlunit.javascript.configuration;

import org.htmlunit.corejs.javascript.ClassDescriptor;
import org.htmlunit.javascript.HtmlUnitScriptable;

/**
 * Descriptor-based registration for a JavaScript host class.
 *
 * <p>This interface is part of the incremental migration from the annotation-based
 * setup ({@code @JsxClass}, {@code @JsxFunction}, etc.) to Rhino's
 * {@link ClassDescriptor} API. Both paths can coexist during the transition.</p>
 *
 * <p>Classes that have been migrated implement this interface as a static
 * {@code HTMLUNIT_DESCRIPTOR} constant and no longer carry {@code @Jsx*} annotations.
 * They are registered via {@link AbstractJavaScriptConfiguration#getDescriptors()}
 * rather than being listed in the {@code getClasses()} array.</p>
 *
 * @author Ronald Brill
 */
public interface HtmlUnitClassDescriptor {

    /**
     * Returns the {@link ClassDescriptor} for the given browser, or {@code null}
     * if this class is not supported by that browser.
     *
     * @param browser the target browser
     * @return the descriptor, or {@code null} if unsupported
     */
    ClassDescriptor forBrowser(SupportedBrowser browser);

    /**
     * Returns the host class that implements this JavaScript object.
     *
     * @return the host class
     */
    Class<? extends HtmlUnitScriptable> getHostClass();

    /**
     * Returns the DOM classes that this JavaScript object wraps.
     * May be an empty array if the object has no DOM counterpart.
     *
     * @return the DOM classes, never {@code null}
     */
    Class<?>[] getDomClasses();

    /**
     * Returns whether this object is visible as a named property of the global object
     * (i.e. {@code window.ClassName} is defined).
     *
     * @return {@code true} if the constructor is exposed on the global object
     */
    boolean isJsObject();

    /**
     * Returns the simple name of the parent JavaScript class, or an empty string
     * if the class does not extend another HtmlUnit scriptable class.
     *
     * @return the extended class name, never {@code null}
     */
    String getExtendedClassName();

    /**
     * Returns an additional name under which the constructor should be registered
     * on the global object, or {@code null} if no alias is needed.
     *
     * @return the alias, or {@code null}
     */
    default String getJsConstructorAlias() {
        return null;
    }
}
