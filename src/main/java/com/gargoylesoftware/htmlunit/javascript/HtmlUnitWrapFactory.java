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

import java.io.Serializable;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.WrapFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * Called by Rhino to Wrap Object as {@link Scriptable}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class HtmlUnitWrapFactory extends WrapFactory implements Serializable {
    /**
     * Constructor.
     */
    public HtmlUnitWrapFactory() {
        setJavaPrimitiveWrap(false); // We don't want to wrap String & Co.
    }

    /**
     * Wraps some objects used by HtmlUnit (like {@link NodeList}), or delegates directly to the parent class.
     * {@inheritDoc}
     * @see WrapFactory#wrapAsJavaObject(Context, Scriptable, Object, Class)
     */
    @Override
    public Scriptable wrapAsJavaObject(final Context context,
            final Scriptable scope, final Object javaObject, final Class<?> staticType) {

        // TODO: should depend on the js configuration file
        final Scriptable resp;
        if (NodeList.class.equals(staticType)
                || NamedNodeMap.class.equals(staticType)) {
            resp = new ScriptableWrapper(scope, javaObject, staticType);
        }
        else {
            resp = super.wrapAsJavaObject(context, scope, javaObject,
                    staticType);
        }
        return resp;
    }
}
