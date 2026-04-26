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
package org.htmlunit.javascript.host.crypto;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code CryptoKey}.
 *
 * @see <a href="https://w3c.github.io/webcrypto/#dfn-CryptoKey">CryptoKey</a>
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
@JsxClass
public class CryptoKey extends HtmlUnitScriptable {

    private Key internalKey_;
    private String type_;
    private boolean isExtractable_;
    private Scriptable algorithm_;
    private Set<String> usages_;

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * Creates a properly scoped CryptoKey from the given parameters.
     *
     * @param scope the JS scope
     * @param internalKey the Java key (SecretKey, PublicKey, or PrivateKey)
     * @param isExtractable whether the key can be exported
     * @param algorithm the JS algorithm descriptor object
     * @param usages the permitted key usages
     * @return the new CryptoKey
     */
    static CryptoKey create(final VarScope scope, final Key internalKey, final boolean isExtractable,
            final Scriptable algorithm, final Collection<String> usages) {
        if (internalKey == null) {
            throw new NullPointerException("The provided key can't be null");
        }

        final CryptoKey key = new CryptoKey();
        key.internalKey_ = internalKey;

        if (internalKey instanceof PublicKey) {
            key.type_ = "public";
        }
        else if (internalKey instanceof PrivateKey) {
            key.type_ = "private";
        }
        else if (internalKey instanceof SecretKey) {
            key.type_ = "secret";
        }
        else {
            throw new IllegalStateException("Unsupported key type: " + internalKey.getClass());
        }

        key.isExtractable_ = isExtractable;
        key.algorithm_ = algorithm;
        key.usages_ = new LinkedHashSet<>(usages);

        key.setParentScope(scope);
        key.setPrototype(getWindow(key).getPrototype(CryptoKey.class));
        return key;
    }

    /**
     * @return the Java key (opaque {@code [[handle]]} internal slot)
     */
    public Key getInternalKey() {
        return internalKey_;
    }

    /**
     * @return the key type: "public", "private", or "secret"
     */
    @JsxGetter
    public String getType() {
        return type_;
    }

    /**
     * @return whether the key material may be exported
     */
    @JsxGetter
    public boolean getExtractable() {
        return isExtractable_;
    }

    /**
     * @return the algorithm descriptor object
     */
    @JsxGetter
    public Scriptable getAlgorithm() {
        return algorithm_;
    }

    /**
     * @return the permitted key usages as a JS array
     */
    @JsxGetter
    public Scriptable getUsages() {
        return JavaScriptEngine.newArray(getParentScope(), usages_.toArray());
    }

    /**
     * @return the permitted key usages as a Java set (for internal use)
     */
    public Set<String> getUsagesInternal() {
        return usages_;
    }
}
