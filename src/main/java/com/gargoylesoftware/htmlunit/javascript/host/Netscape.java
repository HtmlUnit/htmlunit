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
package com.gargoylesoftware.htmlunit.javascript.host;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for window.netscape (FF simulation only).
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class Netscape extends SimpleScriptable {

    Netscape(final Window window) {
        setParentScope(window);

        // simply put "new Object()" for property "security"
        put("security", this, Context.getCurrentContext().newObject(window));
    }

    @Override
    public String getClassName() {
        return "Object";
    }
}
