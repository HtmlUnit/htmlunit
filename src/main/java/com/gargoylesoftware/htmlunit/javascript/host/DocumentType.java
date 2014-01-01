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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCTYPE_ENTITIES_EMPTY_STRING;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCTYPE_ENTITIES_NULL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCTYPE_NOTATIONS_EMPTY_STRING;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCTYPE_NOTATIONS_NULL;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.html.DomDocumentType;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for a DocumentType.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms762752.aspx">MSDN documentation</a>
 * @see <a href="http://www.xulplanet.com/references/objref/DocumentType.html">XUL Planet</a>
 */
@JsxClass(domClass = DomDocumentType.class)
public class DocumentType extends Node {

    /**
     * Returns the name.
     * @return the name
     */
    @JsxGetter
    public String getName() {
        return ((DomDocumentType) getDomNodeOrDie()).getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return getName();
    }

    /**
     * Returns the publicId.
     * @return the publicId
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public String getPublicId() {
        return ((DomDocumentType) getDomNodeOrDie()).getPublicId();
    }

    /**
     * Returns the systemId.
     * @return the systemId
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public String getSystemId() {
        return ((DomDocumentType) getDomNodeOrDie()).getSystemId();
    }

    /**
     * Returns the internal subset.
     * @return the internal subset
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public String getInternalSubset() {
        final String subset = ((DomDocumentType) getDomNodeOrDie()).getInternalSubset();
        if (StringUtils.isNotEmpty(subset)) {
            return subset;
        }

        return null;
    }

    /**
     * Returns entities.
     * @return entities
     */
    @JsxGetter
    public Object getEntities() {
        final NamedNodeMap entities = ((DomDocumentType) getDomNodeOrDie()).getEntities();
        if (null != entities) {
            return entities;
        }

        if (getBrowserVersion().hasFeature(JS_DOCTYPE_ENTITIES_NULL)) {
            return null;
        }
        if (getBrowserVersion().hasFeature(JS_DOCTYPE_ENTITIES_EMPTY_STRING)) {
            return "";
        }
        return Context.getUndefinedValue();
    }

    /**
     * Returns notations.
     * @return notations
     */
    @JsxGetter
    public Object getNotations() {
        final NamedNodeMap notations = ((DomDocumentType) getDomNodeOrDie()).getNotations();
        if (null != notations) {
            return notations;
        }

        if (getBrowserVersion().hasFeature(JS_DOCTYPE_NOTATIONS_NULL)) {
            return null;
        }
        if (getBrowserVersion().hasFeature(JS_DOCTYPE_NOTATIONS_EMPTY_STRING)) {
            return "";
        }
        return Context.getUndefinedValue();
    }
}
