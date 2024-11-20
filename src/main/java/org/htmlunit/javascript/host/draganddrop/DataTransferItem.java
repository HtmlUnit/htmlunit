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

import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Callable;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.file.File;

/**
 * A JavaScript object for {@code DataTransferItem}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class DataTransferItem extends HtmlUnitScriptable {

    private static final String KIND_STRING = "string";
    private static final String KIND_FILE = "file";

    private final String kind_;
    private final String type_;
    private Object data_;

    /**
     * Ctor.
     */
    public DataTransferItem() {
        this(null, null, null);
    }

    /**
     * Creates an instance.
     */
    private DataTransferItem(final String kind, final String type, final Object data) {
        super();

        kind_ = kind;
        type_ = type;
        data_ = data;
    }

    public static DataTransferItem buildStringItem(final CharSequence data, final String type) {
        return new DataTransferItem(KIND_STRING, type, data);
    }

    public static DataTransferItem buildFileItem(final File file) {
        return new DataTransferItem(KIND_FILE, file.getType(), file);
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * @return the {@code type} property
     */
    @JsxGetter
    public String getKind() {
        return kind_;
    }

    /**
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        return type_;
    }

    /**
     * Invokes the given callback with the drag data item's string data as the argument
     * if the item's kind is a Plain unicode string (i.e. kind is string).
     * @param callback Function to execute
     */
    @JsxFunction
    public void getAsString(final Object callback) {
        if (!(callback instanceof Callable)) {
            throw JavaScriptEngine.typeError(
                    "getAsString callback '" + JavaScriptEngine.toString(callback) + "' is not a function");
        }

        if (isFile()) {
            return;
        }

        final Callable fun = (Callable) callback;
        final Object[] args = {data_};

        final WebWindow webWindow = getWindow().getWebWindow();
        final PostponedAction action = new PostponedAction(webWindow.getEnclosedPage(), "getAsString callback") {
            @Override
            public void execute() {
                fun.call(Context.getCurrentContext(), getParentScope(), DataTransferItem.this, args);
            }
        };

        final AbstractJavaScriptEngine<?> engine = webWindow.getWebClient().getJavaScriptEngine();
        engine.addPostponedAction(action);
    }

    /**
     * @return the drag data item's File object. If the item is not a file, this method returns null.
     */
    @JsxFunction
    public File getAsFile() {
        if (!isFile()) {
            return null;
        }

        return (File) data_;
    }

    /**
     * @return true if this is a file
     */
    public boolean isFile() {
        return kind_ == KIND_FILE;
    }
}
