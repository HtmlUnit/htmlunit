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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.lang.reflect.Method;

import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * An implementation of native ActiveX components using <a href="http://jacob-project.wiki.sourceforge.net/">Jacob</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class ActiveXObjectImpl extends SimpleScriptable {

    private static final long serialVersionUID = 6954481782205807262L;

    private final Object activXComponent_;
    private final Method getMethod_;

    /**
     * Constructs a new instance.
     *
     * @param activeXName ActiveX object name
     * @throws Exception if failed to initiate Jacob
     */
    @SuppressWarnings("unchecked")
    public ActiveXObjectImpl(final String activeXName) throws Exception {
        final Class clazz = Class.forName("com.jacob.activeX.ActiveXComponent");
        getMethod_ = clazz.getMethod("getProperty", String.class);
        activXComponent_ = clazz.getConstructor(String.class).newInstance(activeXName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        try {
            return getMethod_.invoke(activXComponent_, name);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
