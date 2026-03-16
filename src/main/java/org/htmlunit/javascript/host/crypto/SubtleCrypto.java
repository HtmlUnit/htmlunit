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

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.NativeObject;
import org.htmlunit.corejs.javascript.NativePromise;
import org.htmlunit.corejs.javascript.ScriptRuntime;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.TopLevel;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.dom.DOMException;

/**
 * A JavaScript object for {@code SubtleCrypto}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 * @author Lai Quang Duong
 */
@JsxClass
public class SubtleCrypto extends HtmlUnitScriptable {

    /**
     * Maps each crypto operation to its supported algorithm names.
     * @see <a href="https://w3c.github.io/webcrypto/#algorithm-overview">Algorithm Overview</a>
     */
    private static final Map<String, Set<String>> OPERATION_TO_SUPPORTED_ALGORITHMS = Map.ofEntries(
            Map.entry("encrypt", Set.of("RSA-OAEP", "AES-CTR", "AES-CBC", "AES-GCM")),
            Map.entry("decrypt", Set.of("RSA-OAEP", "AES-CTR", "AES-CBC", "AES-GCM")),
            Map.entry("sign", Set.of("RSASSA-PKCS1-v1_5", "RSA-PSS", "ECDSA", "HMAC")),
            Map.entry("verify", Set.of("RSASSA-PKCS1-v1_5", "RSA-PSS", "ECDSA", "HMAC")),
            Map.entry("digest", Set.of("SHA-1", "SHA-256", "SHA-384", "SHA-512")),
            Map.entry("generateKey", Set.of("RSASSA-PKCS1-v1_5", "RSA-PSS", "RSA-OAEP",
                    "ECDSA", "ECDH", "AES-CTR", "AES-CBC", "AES-GCM", "AES-KW", "HMAC")),
            Map.entry("importKey", Set.of("RSASSA-PKCS1-v1_5", "RSA-PSS", "RSA-OAEP", "ECDSA", "ECDH",
                    "AES-CTR", "AES-CBC", "AES-GCM", "AES-KW", "HMAC", "HKDF", "PBKDF2")),
            Map.entry("wrapKey", Set.of("RSA-OAEP", "AES-CTR", "AES-CBC", "AES-GCM", "AES-KW")),
            Map.entry("unwrapKey", Set.of("RSA-OAEP", "AES-CTR", "AES-CBC", "AES-GCM", "AES-KW")),
            Map.entry("deriveBits", Set.of("ECDH", "HKDF", "PBKDF2")),
            Map.entry("deriveKey", Set.of("ECDH", "HKDF", "PBKDF2"))
    );

    /**
     * @see <a href="https://w3c.github.io/webcrypto/#dfn-RecognizedKeyUsage">RecognizedKeyUsage</a>
     */
    private static final Set<String> RECOGNIZED_KEY_USAGES = Collections.unmodifiableSet(
            new LinkedHashSet<>(List.of("encrypt", "decrypt", "sign", "verify",
                    "deriveKey", "deriveBits", "wrapKey", "unwrapKey")));

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    private NativePromise notImplemented() {
        return setupRejectedPromise(() ->
                new DOMException("Operation is not supported", DOMException.NOT_SUPPORTED_ERR));
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with the encrypted data (also known as "ciphertext")
     */
    @JsxFunction
    public NativePromise encrypt() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with the decrypted data (also known as "plaintext")
     */
    @JsxFunction
    public NativePromise decrypt() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with the signature
     */
    @JsxFunction
    public NativePromise sign() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with a boolean value indicating whether the signature is valid
     */
    @JsxFunction
    public NativePromise verify() {
        return notImplemented();
    }

    /**
     * Generates a digest of the given data.
     * @see <a href="https://w3c.github.io/webcrypto/#SubtleCrypto-method-digest">SubtleCrypto.digest()</a>
     * @param hashAlgorithm a string or an object with a single property name containing the hash algorithm to use
     * @param data an object containing the data to be digested
     * @return a Promise that fulfills with an ArrayBuffer containing the digest
     */
    @JsxFunction
    public NativePromise digest(final Object hashAlgorithm, final Object data) {
        final byte[] digest;
        try {
            final ByteBuffer inputData = asByteBuffer(data);
            final String algorithm = resolveAlgorithmName(hashAlgorithm);
            ensureAlgorithmIsSupported("digest", algorithm);

            final MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(inputData);
            digest = messageDigest.digest();
        }
        catch (final EcmaError e) {
            return setupRejectedPromise(() -> e);
        }
        catch (final IllegalArgumentException e) {
            return setupRejectedPromise(() -> new DOMException(e.getMessage(), DOMException.SYNTAX_ERR));
        }
        catch (final GeneralSecurityException | UnsupportedOperationException e) {
            return setupRejectedPromise(() -> new DOMException("Operation is not supported: " + e.getMessage(),
                    DOMException.NOT_SUPPORTED_ERR));
        }
        return setupPromise(() -> createArrayBuffer(digest));
    }

    /**
     * Generates a new key (for symmetric algorithms) or key pair (for public-key algorithms).
     * @see <a href="https://w3c.github.io/webcrypto/#SubtleCrypto-method-generateKey">SubtleCrypto.generateKey()</a>
     * @param keyGenParams algorithm-specific key generation parameters
     * @param isExtractable whether the key(s) can be exported
     * @param keyUsages permitted operations for the key(s)
     * @return a Promise that fulfills with a CryptoKey or CryptoKeyPair
     */
    @JsxFunction
    public NativePromise generateKey(final Scriptable keyGenParams, final boolean isExtractable,
            final Scriptable keyUsages) {
        final Object result;
        try {
            final String algorithm = resolveAlgorithmName(keyGenParams);
            ensureAlgorithmIsSupported("generateKey", algorithm);

            final Scriptable scope = keyGenParams.getParentScope();

            switch (algorithm) {
                case "RSASSA-PKCS1-v1_5":
                case "RSA-PSS":
                case "RSA-OAEP": {
                    final RsaHashedKeyAlgorithm rsaParams = RsaHashedKeyAlgorithm.from(keyGenParams);
                    final List<String> usages = resolveKeyUsages(algorithm, keyUsages);

                    final KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
                    keyPairGen.initialize(new RSAKeyGenParameterSpec(
                            rsaParams.getModulusLength(), rsaParams.getPublicExponentAsBigInteger()));
                    final KeyPair keyPair = keyPairGen.generateKeyPair();

                    final Scriptable algoObj = rsaParams.toScriptableObject(scope);
                    result = createKeyPair(keyPair, algoObj, isExtractable, usages, scope);
                    break;
                }
                case "ECDSA":
                case "ECDH": {
                    final EcKeyAlgorithm ecParams = EcKeyAlgorithm.from(keyGenParams);
                    final List<String> usages = resolveKeyUsages(algorithm, keyUsages);

                    final KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
                    keyPairGen.initialize(new ECGenParameterSpec(ecParams.getJavaCurveName()));
                    final KeyPair keyPair = keyPairGen.generateKeyPair();

                    final Scriptable algoObj = ecParams.toScriptableObject(scope);
                    result = createKeyPair(keyPair, algoObj, isExtractable, usages, scope);
                    break;
                }
                case "AES-CBC":
                case "AES-CTR":
                case "AES-GCM":
                case "AES-KW": {
                    final AesKeyAlgorithm aesParams = AesKeyAlgorithm.from(keyGenParams);
                    final List<String> usages = resolveKeyUsages(algorithm, keyUsages);
                    if (usages.isEmpty()) {
                        throw new IllegalArgumentException("An invalid or illegal string was specified");
                    }

                    final KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                    keyGen.init(aesParams.getLength());
                    final SecretKey secretKey = keyGen.generateKey();

                    final Scriptable algoObj = aesParams.toScriptableObject(scope);
                    result = CryptoKey.create(getParentScope(), secretKey, isExtractable, algoObj, usages);
                    break;
                }
                case "HMAC": {
                    final HmacKeyAlgorithm hmacParams = HmacKeyAlgorithm.from(keyGenParams);
                    final List<String> usages = resolveKeyUsages("HMAC", keyUsages);
                    if (usages.isEmpty()) {
                        throw new IllegalArgumentException("An invalid or illegal string was specified");
                    }

                    final KeyGenerator keyGen = KeyGenerator.getInstance(hmacParams.getJavaName());
                    keyGen.init(hmacParams.getLength());
                    final SecretKey secretKey = keyGen.generateKey();

                    final Scriptable algoObj = hmacParams.toScriptableObject(scope);
                    result = CryptoKey.create(getParentScope(), secretKey, isExtractable, algoObj, usages);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("generateKey " + algorithm);
            }
        }
        catch (final EcmaError e) {
            return setupRejectedPromise(() -> e);
        }
        catch (final IllegalArgumentException e) {
            return setupRejectedPromise(() -> new DOMException(e.getMessage(), DOMException.SYNTAX_ERR));
        }
        catch (final GeneralSecurityException | UnsupportedOperationException e) {
            return setupRejectedPromise(() -> new DOMException("Operation is not supported: " + e.getMessage(),
                    DOMException.NOT_SUPPORTED_ERR));
        }
        return setupPromise(() -> result);
    }

    /**
     * Creates a CryptoKeyPair (plain JS object with publicKey/privateKey) from a Java KeyPair.
     * The public key is always extractable regardless of the extractable parameter.
     * Usages are split: public gets {encrypt,verify,wrapKey},
     * private gets {decrypt,sign,unwrapKey,deriveBits,deriveKey}.
     */
    private Scriptable createKeyPair(final KeyPair keyPair, final Scriptable algoObj,
            final boolean isExtractable, final List<String> allUsages, final Scriptable scope) {
        final Set<String> publicUsageSet = Set.of("encrypt", "verify", "wrapKey");
        final Set<String> privateUsageSet = Set.of("decrypt", "sign", "unwrapKey", "deriveBits", "deriveKey");

        final List<String> publicUsages = new ArrayList<>();
        final List<String> privateUsages = new ArrayList<>();
        for (final String usage : allUsages) {
            if (publicUsageSet.contains(usage)) {
                publicUsages.add(usage);
            }
            if (privateUsageSet.contains(usage)) {
                privateUsages.add(usage);
            }
        }

        // if privateKey usages would be empty, throw SyntaxError
        if (privateUsages.isEmpty()) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        // public key is always extractable
        final CryptoKey publicKey = CryptoKey.create(
                getParentScope(), keyPair.getPublic(), true, algoObj, publicUsages);
        final CryptoKey privateKey = CryptoKey.create(
                getParentScope(), keyPair.getPrivate(), isExtractable, algoObj, privateUsages);

        final NativeObject keyPairObj = new NativeObject();
        ScriptRuntime.setBuiltinProtoAndParent(keyPairObj, scope, TopLevel.Builtins.Object);
        ScriptableObject.putProperty(keyPairObj, "publicKey", publicKey);
        ScriptableObject.putProperty(keyPairObj, "privateKey", privateKey);
        return keyPairObj;
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with a CryptoKey object representing the new key
     */
    @JsxFunction
    public NativePromise deriveKey() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with an ArrayBuffer containing the derived bits
     */
    @JsxFunction
    public NativePromise deriveBits() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a CryptoKey object that you can use in the Web Crypto API
     */
    @JsxFunction
    public NativePromise importKey() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return the key in an external, portable format
     */
    @JsxFunction
    public NativePromise exportKey() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise that fulfills with an ArrayBuffer containing the encrypted exported key
     */
    @JsxFunction
    public NativePromise wrapKey() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise that fulfills with the unwrapped key as a CryptoKey object
     */
    @JsxFunction
    public NativePromise unwrapKey() {
        return notImplemented();
    }

    /**
     * Checks if the specified crypto operation supports the given algorithm.
     * @see <a href="https://w3c.github.io/webcrypto/#algorithm-overview">Algorithm Overview</a>
     * @param operation the crypto operation (e.g. "digest", "sign")
     * @param algorithm the algorithm name (e.g. "SHA-256", "HMAC")
     * @throws UnsupportedOperationException if the operation does not support the algorithm
     */
    private static void ensureAlgorithmIsSupported(final String operation, final String algorithm) {
        final Set<String> supportedAlgorithms = OPERATION_TO_SUPPORTED_ALGORITHMS.get(operation);
        if (supportedAlgorithms == null || !supportedAlgorithms.contains(algorithm)) {
            throw new UnsupportedOperationException(operation + " " + algorithm);
        }
    }

    /**
     * Resolves the algorithm name from the given {@code AlgorithmIdentifier}.
     * @see <a href="https://w3c.github.io/webcrypto/#dfn-AlgorithmIdentifier">
     *     AlgorithmIdentifier</a>
     * @param algorithm the algorithm identifier (String or Scriptable with name property)
     * @return the resolved algorithm name
     * @throws IllegalArgumentException if the identifier cannot be resolved
     */
    static String resolveAlgorithmName(final Object algorithm) {
        if (algorithm instanceof String str) {
            return str;
        }
        if (algorithm instanceof Scriptable obj) {
            final Object name = ScriptableObject.getProperty(obj, "name");
            if (name instanceof String nameStr) {
                return nameStr;
            }
        }
        throw new IllegalArgumentException("An invalid or illegal string was specified");
    }

    /**
     * Converts ArrayBuffer or ArrayBufferView to a ByteBuffer.
     * @param data the buffer source object
     * @return the ByteBuffer wrapping the data
     * @throws IllegalArgumentException if data is not a Scriptable or is NOT_FOUND
     * @throws EcmaError if data is not an ArrayBuffer or ArrayBufferView
     */
    static ByteBuffer asByteBuffer(final Object data) {
        if (!(data instanceof Scriptable)) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }
        if (data == Scriptable.NOT_FOUND) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }
        if (data instanceof NativeArrayBuffer nativeBuffer) {
            return ByteBuffer.wrap(nativeBuffer.getBuffer());
        }
        else if (data instanceof NativeArrayBufferView arrayBufferView) {
            final NativeArrayBuffer arrayBuffer = arrayBufferView.getBuffer();
            return ByteBuffer.wrap(
                    arrayBuffer.getBuffer(), arrayBufferView.getByteOffset(), arrayBufferView.getByteLength());
        }
        else {
            throw JavaScriptEngine.typeError("Argument could not be converted to any of: ArrayBufferView, ArrayBuffer.");
        }
    }

    /**
     * Creates a NativeArrayBuffer with proper scope and prototype from the given bytes.
     * @param data the byte array to wrap
     * @return the new NativeArrayBuffer
     */
    NativeArrayBuffer createArrayBuffer(final byte[] data) {
        final NativeArrayBuffer buffer = new NativeArrayBuffer(data.length);
        System.arraycopy(data, 0, buffer.getBuffer(), 0, data.length);
        buffer.setParentScope(getParentScope());
        buffer.setPrototype(ScriptableObject.getClassPrototype(getWindow(), buffer.getClassName()));
        return buffer;
    }

    /**
     * Resolves and validates key usages from the JS array against the algorithm's supported operations.
     * @param algorithm the algorithm name
     * @param keyUsages the JS usages array
     * @return the validated, ordered list of usages
     * @throws IllegalArgumentException if usages array is invalid or contains unrecognized values
     */
    static List<String> resolveKeyUsages(final String algorithm, final Scriptable keyUsages) {
        if (!ScriptRuntime.isArrayLike(keyUsages)) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        final Set<String> supportedKeyUsages = new HashSet<>();
        for (final Object usage : ScriptRuntime.getArrayElements(keyUsages)) {
            if (!(usage instanceof String usageStr)) {
                throw new IllegalArgumentException("An invalid or illegal string was specified");
            }
            if (!RECOGNIZED_KEY_USAGES.contains(usageStr)) {
                throw new IllegalArgumentException("An invalid or illegal string was specified");
            }

            final Set<String> supportedAlgorithms = OPERATION_TO_SUPPORTED_ALGORITHMS.get(usageStr);
            if (supportedAlgorithms != null && supportedAlgorithms.contains(algorithm)) {
                supportedKeyUsages.add(usageStr);
            }
        }

        // maintain canonical ordering per RECOGNIZED_KEY_USAGES
        final List<String> sortedKeyUsages = new ArrayList<>();
        for (final String keyUsage : RECOGNIZED_KEY_USAGES) {
            if (supportedKeyUsages.contains(keyUsage)) {
                sortedKeyUsages.add(keyUsage);
            }
        }

        return sortedKeyUsages;
    }
}
