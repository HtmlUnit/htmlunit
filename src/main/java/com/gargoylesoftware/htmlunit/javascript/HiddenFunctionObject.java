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
package com.gargoylesoftware.htmlunit.javascript;

import java.lang.reflect.Member;

import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Extended FunctionObject that overrides avoidObjectDetection().
 *
 * @version $Revision$
 * @author Ronald Brill
 */
class HiddenFunctionObject extends FunctionObject {

    /**
     * Constructor.
     */
    public HiddenFunctionObject(final String name, final Member methodOrConstructor, final Scriptable scope) {
        super(name, methodOrConstructor, scope);
    }

    @Override
    public boolean avoidObjectDetection() {
        return true;
    }
}
