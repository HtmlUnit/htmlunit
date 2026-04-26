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
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.NativePromise;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
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
     * @see <a href="https://w3c.github.io/webcrypto/#aes-gcm-operations">AES-GCM encrypt, step 6</a>
     */
    private static final Set<Integer> VALID_AES_GCM_TAG_LENGTHS = Set.of(32, 64, 96, 104, 112, 120, 128);

    private static class InvalidAccessException extends RuntimeException {
        InvalidAccessException(final String message) {
            super(message);
        }
    }

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
     * Encrypts data using the given key and algorithm.
     * @see <a href="https://w3c.github.io/webcrypto/#SubtleCrypto-method-encrypt">SubtleCrypto.encrypt()</a>
     * @param algorithm the algorithm identifier with parameters
     * @param key the CryptoKey to encrypt with
     * @param data the data to encrypt
     * @return a Promise that fulfills with an ArrayBuffer containing the ciphertext
     */
    @JsxFunction
    public NativePromise encrypt(final Object algorithm, final CryptoKey key, final Object data) {
        return doCipher(algorithm, key, data, Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypts data using the given key and algorithm.
     * @see <a href="https://w3c.github.io/webcrypto/#SubtleCrypto-method-decrypt">SubtleCrypto.decrypt()</a>
     * @param algorithm the algorithm identifier with parameters
     * @param key the CryptoKey to decrypt with
     * @param data the data to decrypt
     * @return a Promise that fulfills with an ArrayBuffer containing the plaintext
     */
    @JsxFunction
    public NativePromise decrypt(final Object algorithm, final CryptoKey key, final Object data) {
        return doCipher(algorithm, key, data, Cipher.DECRYPT_MODE);
    }

    /**
     * Shared encrypt/decrypt implementation.
     */
    private NativePromise doCipher(final Object algorithm, final CryptoKey key,
            final Object data, final int cipherMode) {
        final String operation = switch (cipherMode) {
            case Cipher.ENCRYPT_MODE -> "encrypt";
            case Cipher.DECRYPT_MODE -> "decrypt";
            default -> throw new IllegalArgumentException("Invalid cipher mode: " + cipherMode);
        };

        final byte[] result;
        try {
            final String algorithmName = resolveAlgorithmName(algorithm);
            ensureAlgorithmIsSupported(operation, algorithmName);
            ensureKeyAlgorithmMatches(algorithmName, key);
            ensureKeyUsage(key, operation);

            final ByteBuffer inputData = asByteBuffer(data);

            // encrypt/decrypt requires algorithm parameters as an object (iv, counter, etc.)
            if (!(algorithm instanceof Scriptable algorithmObj)) {
                throw new IllegalArgumentException("An invalid or illegal string was specified");
            }

            switch (algorithmName) {
                case "AES-CBC": {
                    // https://w3c.github.io/webcrypto/#aes-cbc-operations
                    final byte[] iv = extractBuffer(algorithmObj, "iv");
                    if (iv == null || iv.length != 16) {
                        throw new IllegalArgumentException(
                                "Data provided to an operation does not meet requirements");
                    }
                    final SecretKey secretKey = getInternalKey(key, SecretKey.class);
                    final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    cipher.init(cipherMode, secretKey, new IvParameterSpec(iv));
                    result = cipher.doFinal(toByteArray(inputData));
                    break;
                }
                case "AES-GCM": {
                    // https://w3c.github.io/webcrypto/#aes-gcm-operations
                    final byte[] iv = extractBuffer(algorithmObj, "iv");
                    if (iv == null || iv.length == 0) {
                        throw new IllegalArgumentException(
                                "Data provided to an operation does not meet requirements");
                    }

                    final int tagLength;
                    final Object tagLengthProp = ScriptableObject.getProperty(algorithmObj, "tagLength");
                    if (tagLengthProp instanceof Number num) {
                        tagLength = num.intValue();
                        if (!VALID_AES_GCM_TAG_LENGTHS.contains(tagLength)) {
                            throw new IllegalArgumentException(
                                    "Data provided to an operation does not meet requirements");
                        }
                    }
                    else {
                        tagLength = 128;
                    }

                    final SecretKey secretKey = getInternalKey(key, SecretKey.class);
                    final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    cipher.init(cipherMode, secretKey, new GCMParameterSpec(tagLength, iv));

                    final Object aadProp = ScriptableObject.getProperty(algorithmObj, "additionalData");
                    if (aadProp instanceof Scriptable) {
                        final ByteBuffer aad = asByteBuffer(aadProp);
                        cipher.updateAAD(toByteArray(aad));
                    }

                    result = cipher.doFinal(toByteArray(inputData));
                    break;
                }
                case "AES-CTR": {
                    // https://w3c.github.io/webcrypto/#aes-ctr-operations
                    final byte[] counter = extractBuffer(algorithmObj, "counter");
                    if (counter == null || counter.length != 16) {
                        throw new IllegalArgumentException(
                                "Data provided to an operation does not meet requirements");
                    }

                    final Object lengthProp = ScriptableObject.getProperty(algorithmObj, "length");
                    if (!(lengthProp instanceof Number numLength)) {
                        throw new IllegalArgumentException(
                                "Data provided to an operation does not meet requirements");
                    }
                    final int counterLength = numLength.intValue();
                    if (counterLength < 1 || counterLength > 128) {
                        throw new IllegalArgumentException(
                                "Data provided to an operation does not meet requirements");
                    }

                    final SecretKey secretKey = getInternalKey(key, SecretKey.class);
                    // Java always increments the full 128-bit counter, ignoring the 'length' partitioning.
                    // This only becomes an issue when data exceeds 2^length AES blocks (16 bytes each),
                    // but in real-world usage (length >= 64) it's pretty much unreachable.
                    final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
                    cipher.init(cipherMode, secretKey, new IvParameterSpec(counter));
                    result = cipher.doFinal(toByteArray(inputData));
                    break;
                }
                case "RSA-OAEP": {
                    // https://w3c.github.io/webcrypto/#rsa-oaep-operations
                    final Scriptable keyAlgorithm = key.getAlgorithm();
                    final Object hashObj = ScriptableObject.getProperty(keyAlgorithm, "hash");
                    final String hash = resolveAlgorithmName(hashObj);

                    final byte[] label;
                    final Object labelProp = ScriptableObject.getProperty(algorithmObj, "label");
                    if (labelProp instanceof Scriptable) {
                        final ByteBuffer labelBuf = asByteBuffer(labelProp);
                        label = toByteArray(labelBuf);
                    }
                    else {
                        label = new byte[0];
                    }

                    final MGF1ParameterSpec mgf1Spec = new MGF1ParameterSpec(hash);
                    final AlgorithmParameterSpec oaepSpec = new OAEPParameterSpec(
                            hash, "MGF1", mgf1Spec, new PSource.PSpecified(label));

                    final Key internalKey;
                    if (cipherMode == Cipher.ENCRYPT_MODE) {
                        internalKey = getInternalKey(key, PublicKey.class);
                    }
                    else {
                        internalKey = getInternalKey(key, PrivateKey.class);
                    }

                    final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
                    cipher.init(cipherMode, internalKey, oaepSpec);
                    result = cipher.doFinal(toByteArray(inputData));
                    break;
                }
                default:
                    throw new UnsupportedOperationException(operation + " " + algorithmName);
            }
        }
        catch (final EcmaError e) {
            return setupRejectedPromise(() -> e);
        }
        catch (final InvalidAccessException e) {
            return setupRejectedPromise(() -> new DOMException(e.getMessage(), DOMException.INVALID_ACCESS_ERR));
        }
        catch (final IllegalArgumentException e) {
            return setupRejectedPromise(() -> new DOMException(e.getMessage(), DOMException.SYNTAX_ERR));
        }
        catch (final GeneralSecurityException | UnsupportedOperationException e) {
            return setupRejectedPromise(() -> new DOMException("Operation is not supported: " + e.getMessage(),
                    DOMException.NOT_SUPPORTED_ERR));
        }
        return setupPromise(() -> createArrayBuffer(result));
    }

    /**
     * Signs data using the given key.
     * @see <a href="https://w3c.github.io/webcrypto/#SubtleCrypto-method-sign">SubtleCrypto.sign()</a>
     * @param algorithm the algorithm identifier (String or object with name property)
     * @param key the CryptoKey to sign with
     * @param data the data to sign
     * @return a Promise that fulfills with an ArrayBuffer containing the signature
     */
    @JsxFunction
    public NativePromise sign(final Object algorithm, final CryptoKey key, final Object data) {
        return doSignOrVerify(algorithm, key, null, data, true);
    }

    /**
     * Verifies a signature using the given key.
     * @see <a href="https://w3c.github.io/webcrypto/#SubtleCrypto-method-verify">SubtleCrypto.verify()</a>
     * @param algorithm the algorithm identifier (String or object with name property)
     * @param key the CryptoKey to verify with
     * @param signature the signature to verify
     * @param data the data that was signed
     * @return a Promise that fulfills with a boolean indicating whether the signature is valid
     */
    @JsxFunction
    public NativePromise verify(final Object algorithm, final CryptoKey key,
            final Object signature, final Object data) {
        return doSignOrVerify(algorithm, key, signature, data, false);
    }

    /**
     * Shared sign/verify implementation.
     */
    private NativePromise doSignOrVerify(final Object algorithm, final CryptoKey key,
            final Object existingSignature, final Object data, final boolean isSigning) {
        final Object result;
        try {
            final String algorithmName = resolveAlgorithmName(algorithm);
            final String operation = isSigning ? "sign" : "verify";
            ensureAlgorithmIsSupported(operation, algorithmName);
            ensureKeyAlgorithmMatches(algorithmName, key);
            ensureKeyUsage(key, operation);

            final ByteBuffer inputData = asByteBuffer(data);

            switch (algorithmName) {
                case "HMAC": {
                    // https://w3c.github.io/webcrypto/#hmac-operations
                    final SecretKey secretKey = getInternalKey(key, SecretKey.class);
                    final Mac mac = Mac.getInstance(secretKey.getAlgorithm());
                    mac.init(secretKey);
                    mac.update(inputData);
                    final byte[] macBytes = mac.doFinal();
                    if (isSigning) {
                        result = macBytes;
                    }
                    else {
                        result = MessageDigest.isEqual(macBytes,
                                toByteArray(asByteBuffer(existingSignature)));
                    }
                    break;
                }
                case "RSASSA-PKCS1-v1_5":
                    // https://w3c.github.io/webcrypto/#rsassa-pkcs1
                case "RSA-PSS":
                    // https://w3c.github.io/webcrypto/#rsa-pss
                case "ECDSA": {
                    // https://w3c.github.io/webcrypto/#ecdsa-operations
                    final Signature sig = "ECDSA".equals(algorithmName)
                            ? resolveEcdsaSignature(algorithm)
                            : resolveRsaSignature(algorithmName, algorithm, key);
                    if (isSigning) {
                        sig.initSign(getInternalKey(key, PrivateKey.class));
                        sig.update(inputData);
                        result = sig.sign();
                    }
                    else {
                        sig.initVerify(getInternalKey(key, PublicKey.class));
                        sig.update(inputData);
                        result = sig.verify(toByteArray(asByteBuffer(existingSignature)));
                    }
                    break;
                }
                default:
                    throw new UnsupportedOperationException(operation + " " + algorithmName);
            }
        }
        catch (final EcmaError e) {
            return setupRejectedPromise(() -> e);
        }
        catch (final InvalidAccessException e) {
            return setupRejectedPromise(() -> new DOMException(e.getMessage(), DOMException.INVALID_ACCESS_ERR));
        }
        catch (final IllegalArgumentException e) {
            return setupRejectedPromise(() -> new DOMException(e.getMessage(), DOMException.SYNTAX_ERR));
        }
        catch (final GeneralSecurityException | UnsupportedOperationException e) {
            return setupRejectedPromise(() -> new DOMException("Operation is not supported: " + e.getMessage(),
                    DOMException.NOT_SUPPORTED_ERR));
        }

        if (isSigning) {
            return setupPromise(() -> createArrayBuffer((byte[]) result));
        }
        return setupPromise(() -> result);
    }

    /**
     * Resolves the RSA {@link Signature} instance for the given algorithm.
     */
    private static Signature resolveRsaSignature(final String algorithmName, final Object algorithmParams,
            final CryptoKey key) throws GeneralSecurityException {
        final Object hashObj = ScriptableObject.getProperty(key.getAlgorithm(), "hash");
        final String hash = resolveAlgorithmName(hashObj);
        final String javaHash = hash.replace("-", "");

        if ("RSASSA-PKCS1-v1_5".equals(algorithmName)) {
            return Signature.getInstance(javaHash + "withRSA");
        }

        if (!(algorithmParams instanceof Scriptable obj)) {
            throw new IllegalArgumentException("Data provided to an operation does not meet requirements");
        }
        final Object saltLengthProp = ScriptableObject.getProperty(obj, "saltLength");
        if (!(saltLengthProp instanceof Number num)) {
            throw new IllegalArgumentException("Data provided to an operation does not meet requirements");
        }
        final int saltLength = num.intValue();

        final MGF1ParameterSpec mgf1Spec = new MGF1ParameterSpec(hash);
        final PSSParameterSpec pssSpec = new PSSParameterSpec(hash, "MGF1", mgf1Spec, saltLength, 1);
        final Signature sig = Signature.getInstance("RSASSA-PSS");
        sig.setParameter(pssSpec);
        return sig;
    }

    /**
     * Resolves the ECDSA {@link Signature} instance for the given algorithm params.
     */
    private static Signature resolveEcdsaSignature(final Object algorithmParams)
            throws GeneralSecurityException {
        if (!(algorithmParams instanceof Scriptable obj)) {
            throw new IllegalArgumentException("Data provided to an operation does not meet requirements");
        }
        final Object hashProp = ScriptableObject.getProperty(obj, "hash");
        final String hash = resolveAlgorithmName(hashProp);
        final String javaHash = hash.replace("-", "");
        return Signature.getInstance(javaHash + "withECDSAinP1363Format");
    }

    private static byte[] toByteArray(final ByteBuffer buffer) {
        final byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
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

            final VarScope scope = keyGenParams.getParentScope();

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
            final boolean isExtractable, final List<String> allUsages, final VarScope scope) {
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

        final Scriptable keyPairObj = JavaScriptEngine.newObject(scope);
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
     * Imports a key from external, portable key material.
     * @see <a href="https://w3c.github.io/webcrypto/#SubtleCrypto-method-importKey">SubtleCrypto.importKey()</a>
     * @param format the data format ("raw", "pkcs8", "spki", "jwk")
     * @param keyData the key material (BufferSource for raw/pkcs8/spki, JsonWebKey for jwk)
     * @param keyImportParams algorithm-specific import parameters
     * @param isExtractable whether the key can be exported
     * @param keyUsages permitted operations for this key
     * @return a Promise that fulfills with the imported CryptoKey
     */
    @JsxFunction
    public NativePromise importKey(final String format, final Scriptable keyData,
            final Scriptable keyImportParams, final boolean isExtractable, final Scriptable keyUsages) {
        final CryptoKey key;
        try {
            final String algorithm = resolveAlgorithmName(keyImportParams);
            ensureAlgorithmIsSupported("importKey", algorithm);

            switch (format) {
                case "raw":
                    key = importRawKey(algorithm, keyData, keyImportParams, isExtractable, keyUsages);
                    break;
                case "pkcs8":
                case "spki":
                case "jwk":
                    return notImplemented();
                default:
                    throw new IllegalArgumentException("An invalid or illegal string was specified");
            }
        }
        catch (final EcmaError e) {
            return setupRejectedPromise(() -> e);
        }
        catch (final IllegalArgumentException e) {
            return setupRejectedPromise(() -> new DOMException(e.getMessage(), DOMException.SYNTAX_ERR));
        }
        catch (final UnsupportedOperationException e) {
            return setupRejectedPromise(() -> new DOMException("Operation is not supported: " + e.getMessage(),
                    DOMException.NOT_SUPPORTED_ERR));
        }
        return setupPromise(() -> key);
    }

    private CryptoKey importRawKey(final String algorithm, final Scriptable keyData,
            final Scriptable keyImportParams, final boolean isExtractable, final Scriptable keyUsages) {
        final ByteBuffer byteBuffer = asByteBuffer(keyData);
        final byte[] rawBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(rawBytes);
        final int bitLength = rawBytes.length * 8;
        if (bitLength == 0) {
            throw new IllegalArgumentException("Data provided to an operation does not meet requirements");
        }

        final List<String> usages = resolveKeyUsages(algorithm, keyUsages);
        if (usages.isEmpty()) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        if ("HMAC".equals(algorithm)) {
            final HmacKeyAlgorithm params = HmacKeyAlgorithm.from(keyImportParams, bitLength);
            final int length = params.getLength();
            if (length > bitLength || length <= bitLength - 8) {
                throw new IllegalArgumentException("Data provided to an operation does not meet requirements");
            }

            final Scriptable scriptableAlgorithm = params.toScriptableObject(keyImportParams.getParentScope());
            final SecretKey internalKey = new SecretKeySpec(rawBytes, params.getJavaName());
            return CryptoKey.create(getParentScope(), internalKey, isExtractable, scriptableAlgorithm, usages);
        }

        if (AesKeyAlgorithm.isSupported(algorithm)) {
            final AesKeyAlgorithm aesAlgo = new AesKeyAlgorithm(algorithm, bitLength);
            final Scriptable scriptableAlgorithm = aesAlgo.toScriptableObject(keyImportParams.getParentScope());
            final SecretKey internalKey = new SecretKeySpec(rawBytes, "AES");
            return CryptoKey.create(getParentScope(), internalKey, isExtractable, scriptableAlgorithm, usages);
        }

        throw new UnsupportedOperationException("importKey raw " + algorithm);
    }

    /**
     * Exports a key in the specified format.
     * @see <a href="https://w3c.github.io/webcrypto/#SubtleCrypto-method-exportKey">SubtleCrypto.exportKey()</a>
     * @param format the data format ("raw", "pkcs8", "spki", "jwk")
     * @param key the CryptoKey to export
     * @return a Promise that fulfills with the key data
     */
    @JsxFunction
    public NativePromise exportKey(final String format, final CryptoKey key) {
        final byte[] result;
        try {
            if (!key.getExtractable()) {
                return setupRejectedPromise(() -> new DOMException(
                        "A parameter or an operation is not supported by the underlying object",
                        DOMException.INVALID_ACCESS_ERR));
            }

            switch (format) {
                case "raw": {
                    if (!(key.getInternalKey() instanceof SecretKey secretKey)) {
                        throw new IllegalArgumentException(
                                "Data provided to an operation does not meet requirements");
                    }
                    result = secretKey.getEncoded();
                    break;
                }
                case "pkcs8":
                case "spki":
                case "jwk":
                    return notImplemented();
                default:
                    throw new IllegalArgumentException("An invalid or illegal string was specified");
            }
        }
        catch (final IllegalArgumentException e) {
            return setupRejectedPromise(() -> new DOMException(e.getMessage(), DOMException.SYNTAX_ERR));
        }
        catch (final UnsupportedOperationException e) {
            return setupRejectedPromise(() -> new DOMException("Operation is not supported: " + e.getMessage(),
                    DOMException.NOT_SUPPORTED_ERR));
        }
        return setupPromise(() -> createArrayBuffer(result));
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
     * Verifies that the operation's algorithm name matches the key's algorithm name.
     * @param algorithmName the algorithm name from the operation parameters
     * @param key the CryptoKey being used
     * @throws InvalidAccessException if the algorithm names don't match
     */
    private static void ensureKeyAlgorithmMatches(final String algorithmName, final CryptoKey key) {
        final String keyAlgoName = resolveAlgorithmName(key.getAlgorithm());
        if (!algorithmName.equals(keyAlgoName)) {
            throw new InvalidAccessException(
                    "A parameter or an operation is not supported by the underlying object");
        }
    }

    /**
     * Verifies that the key's usages include the specified usage.
     * @param key the CryptoKey being used
     * @param usage the required usage (e.g. "encrypt", "sign")
     * @throws InvalidAccessException if the key doesn't have the required usage
     */
    private static void ensureKeyUsage(final CryptoKey key, final String usage) {
        if (!key.getUsagesInternal().contains(usage)) {
            throw new InvalidAccessException(
                    "A parameter or an operation is not supported by the underlying object");
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
            throw JavaScriptEngine.typeError(
                    "Argument could not be converted to any of: ArrayBufferView, ArrayBuffer.");
        }
    }

    /**
     * Reads a property from a JS object and converts it to a byte array.
     * @param obj the JS object containing the property
     * @param property the property name (e.g. "iv", "counter", "label")
     * @return the byte array, or {@code null} if the property is absent or not convertible
     */
    private static byte[] extractBuffer(final Scriptable obj, final String property) {
        final Object prop = ScriptableObject.getProperty(obj, property);
        if (prop instanceof Scriptable) {
            final ByteBuffer buf = asByteBuffer(prop);
            return toByteArray(buf);
        }
        return null;
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
        buffer.setPrototype(ScriptableObject.getClassPrototype(getParentScope(), buffer.getClassName()));
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
        if (!JavaScriptEngine.isArrayLike(keyUsages)) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        final Set<String> supportedKeyUsages = new HashSet<>();
        JavaScriptEngine.iterateArrayLike(null, keyUsages, usage -> {
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
        });

        // maintain canonical ordering per RECOGNIZED_KEY_USAGES
        final List<String> sortedKeyUsages = new ArrayList<>();
        for (final String keyUsage : RECOGNIZED_KEY_USAGES) {
            if (supportedKeyUsages.contains(keyUsage)) {
                sortedKeyUsages.add(keyUsage);
            }
        }

        return sortedKeyUsages;
    }

    /**
     * Extracts the internal Java key from a CryptoKey, validating it is the expected type.
     * @param <T> the expected key type
     * @param cryptoKey the CryptoKey
     * @param expectedKeyType the expected class (e.g. SecretKey.class)
     * @return the internal key cast to the expected type
     * @throws InvalidAccessException if the key is not the expected type
     */
    static <T extends Key> T getInternalKey(final CryptoKey cryptoKey, final Class<T> expectedKeyType) {
        final Key internalKey = cryptoKey.getInternalKey();
        if (!expectedKeyType.isInstance(internalKey)) {
            throw new InvalidAccessException("A parameter or an operation is not supported by the underlying object");
        }
        return expectedKeyType.cast(internalKey);
    }
}
