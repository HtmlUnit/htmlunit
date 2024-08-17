/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.draganddrop;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.file.FileList;

/**
 * A JavaScript object for {@code DataTransfer}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class DataTransfer extends HtmlUnitScriptable {

    private DataTransferItemList items_;

    /**
     * Ctor.
     */
    public DataTransfer() {
        super();
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * @return the {@code files} property
     */
    @JsxGetter
    public FileList getFiles() {
        return getItems().getFiles();
    }

    /**
     * @return the {@code items} property
     */
    @JsxGetter
    public DataTransferItemList getItems() {
        if (items_ == null) {
            final DataTransferItemList list = new DataTransferItemList();
            list.setParentScope(getParentScope());
            list.setPrototype(getPrototype(list.getClass()));
            items_ = list;
        }
        return items_;
    }
}
