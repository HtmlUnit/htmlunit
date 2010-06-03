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
import com.gargoylesoftware.htmlunit.javascript.host.Storage.Type;

/**
 * The JavaScript object that represents a StorageList.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class StorageList extends SimpleScriptable {

    private static final long serialVersionUID = -7105465959490778967L;

    private Storage storage_;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (name.equals(((HtmlPage) getWindow().getWebWindow().getEnclosedPage()).getUrl().getHost())) {
            if (storage_ == null) {
                storage_ = new Storage();
                storage_.setParentScope(getParentScope());
                storage_.setPrototype(getPrototype(storage_.getClass()));
                storage_.setType(Type.GLOBAL_STORAGE);
            }
            return storage_;
        }
        else {
            Context.reportError("Security error: can not access the specified host");
            return null;
        }
    }
}
