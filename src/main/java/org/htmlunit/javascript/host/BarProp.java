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
package org.htmlunit.javascript.host;

import org.htmlunit.corejs.javascript.ClassDescriptor;
import org.htmlunit.corejs.javascript.Undefined;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.HtmlUnitClassDescriptor;
import org.htmlunit.javascript.configuration.SupportedBrowser;

/**
 * A JavaScript object for {@code BarProp}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class BarProp extends HtmlUnitScriptable {

    /** Descriptor for registering this class with the JavaScript engine. */
    public static final HtmlUnitClassDescriptor HTMLUNIT_DESCRIPTOR = new HtmlUnitClassDescriptor() {

        private static final ClassDescriptor DESCRIPTOR_ = new ClassDescriptor.Builder("BarProp", 0,
                (cx, f, callerObj, scope, thisObj, args) -> Undefined.instance)
                .build();

        @Override
        public ClassDescriptor forBrowser(final SupportedBrowser browser) {
            return DESCRIPTOR_;
        }

        @Override
        public Class<? extends HtmlUnitScriptable> getHostClass() {
            return BarProp.class;
        }

        @Override
        public Class<?>[] getDomClasses() {
            return new Class<?>[0];
        }

        @Override
        public boolean isJsObject() {
            return true;
        }

        @Override
        public String getExtendedClassName() {
            return "";
        }
    };
}
