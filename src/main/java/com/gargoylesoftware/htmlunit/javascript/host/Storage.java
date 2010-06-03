/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * The JavaScript object that represents a Storage.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class Storage extends SimpleScriptable {

    private static final long serialVersionUID = 7181399874147128543L;

    static enum Type { GLOBAL_STORAGE, LOCAL_STORAGE, SESSION_STORAGE };

    private Type type_;

    void setType(final Type type) {
        type_ = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (type_ == null) {
            super.put(name, start, value);
            return;
        }
        StorageImpl.getInstance().set(type_, (HtmlPage) getWindow().getWebWindow().getEnclosedPage(),
                name, Context.toString(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (type_ == null) {
            return super.get(name, start);
        }
        return StorageImpl.getInstance().get(type_,
                (HtmlPage) getWindow().getWebWindow().getEnclosedPage(), name);
    }

}
