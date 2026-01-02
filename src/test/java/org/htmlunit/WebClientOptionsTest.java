/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WebClientOptions}.
 *
 * @author Ronald Brill
 */
public class WebClientOptionsTest extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final WebClientOptions original = new WebClientOptions();

        final byte[] bytes = SerializationUtils.serialize(original);
        final WebClientOptions deserialized = SerializationUtils.deserialize(bytes);

        assertEquals(original.isJavaScriptEnabled(), deserialized.isJavaScriptEnabled());
        assertEquals(original.isCssEnabled(), deserialized.isCssEnabled());

        assertEquals(original.isPrintContentOnFailingStatusCode(),
                        deserialized.isPrintContentOnFailingStatusCode());
        assertEquals(original.isThrowExceptionOnFailingStatusCode(),
                        deserialized.isThrowExceptionOnFailingStatusCode());
        assertEquals(original.isThrowExceptionOnScriptError(),
                        deserialized.isThrowExceptionOnScriptError());
        assertEquals(original.isPopupBlockerEnabled(),
                        deserialized.isPopupBlockerEnabled());
        assertEquals(original.isRedirectEnabled(),
                        deserialized.isRedirectEnabled());

        assertEquals(original.isGeolocationEnabled(),
                        deserialized.isGeolocationEnabled());
        assertEquals(original.getGeolocation(),
                        deserialized.getGeolocation());

        assertEquals(original.getNekoReaderBufferSize(),
                        deserialized.getNekoReaderBufferSize());

        assertEquals(original.isWebSocketEnabled(),
                        deserialized.isWebSocketEnabled());
        assertEquals(original.getWebSocketMaxTextMessageSize(),
                        deserialized.getWebSocketMaxTextMessageSize());
        assertEquals(original.getWebSocketMaxTextMessageBufferSize(),
                        deserialized.getWebSocketMaxTextMessageBufferSize());
        assertEquals(original.getWebSocketMaxBinaryMessageSize(),
                        deserialized.getWebSocketMaxBinaryMessageSize());
        assertEquals(original.getWebSocketMaxBinaryMessageBufferSize(),
                        deserialized.getWebSocketMaxBinaryMessageBufferSize());

        assertEquals(original.isFetchPolyfillEnabled(), deserialized.isFetchPolyfillEnabled());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serializationChanged() throws Exception {
        final WebClientOptions original = new WebClientOptions();
        original.setJavaScriptEnabled(false);
        original.setCssEnabled(false);

        original.setPrintContentOnFailingStatusCode(false);
        original.setThrowExceptionOnFailingStatusCode(false);
        original.setThrowExceptionOnScriptError(false);
        original.setPopupBlockerEnabled(true);
        original.setRedirectEnabled(false);

        original.setGeolocationEnabled(true);
        original.setGeolocation(new WebClientOptions.Geolocation(1d, 2d, 3d, 4d, 5d, 6d, 7d));

        original.setNekoReaderBufferSize(1234567);

        original.setWebSocketEnabled(false);
        original.setWebSocketMaxTextMessageSize(77);
        original.setWebSocketMaxTextMessageBufferSize(771);
        original.setWebSocketMaxBinaryMessageSize(44);
        original.setWebSocketMaxBinaryMessageBufferSize(441);

        original.setFetchPolyfillEnabled(true);

        final byte[] bytes = SerializationUtils.serialize(original);
        final WebClientOptions deserialized = SerializationUtils.deserialize(bytes);

        assertEquals(original.isJavaScriptEnabled(), deserialized.isJavaScriptEnabled());
        assertEquals(original.isCssEnabled(), deserialized.isCssEnabled());

        assertEquals(original.isPrintContentOnFailingStatusCode(), deserialized.isPrintContentOnFailingStatusCode());
        assertEquals(original.isThrowExceptionOnFailingStatusCode(),
                        deserialized.isThrowExceptionOnFailingStatusCode());
        assertEquals(original.isThrowExceptionOnScriptError(), deserialized.isThrowExceptionOnScriptError());
        assertEquals(original.isPopupBlockerEnabled(), deserialized.isPopupBlockerEnabled());
        assertEquals(original.isRedirectEnabled(), deserialized.isRedirectEnabled());

        assertEquals(original.isGeolocationEnabled(), deserialized.isGeolocationEnabled());
        assertEquals(original.getGeolocation().getAccuracy(), deserialized.getGeolocation().getAccuracy());
        assertEquals(original.getGeolocation().getLatitude(), deserialized.getGeolocation().getLatitude());
        assertEquals(original.getGeolocation().getLongitude(), deserialized.getGeolocation().getLongitude());
        assertEquals(original.getGeolocation().getAltitude(), deserialized.getGeolocation().getAltitude());
        assertEquals(original.getGeolocation().getAltitudeAccuracy(),
                deserialized.getGeolocation().getAltitudeAccuracy());
        assertEquals(original.getGeolocation().getHeading(), deserialized.getGeolocation().getHeading());
        assertEquals(original.getGeolocation().getSpeed(), deserialized.getGeolocation().getSpeed());

        assertEquals(original.getNekoReaderBufferSize(), deserialized.getNekoReaderBufferSize());

        assertEquals(original.isWebSocketEnabled(),
                        deserialized.isWebSocketEnabled());
        assertEquals(original.getWebSocketMaxTextMessageSize(),
                        deserialized.getWebSocketMaxTextMessageSize());
        assertEquals(original.getWebSocketMaxTextMessageBufferSize(),
                        deserialized.getWebSocketMaxTextMessageBufferSize());
        assertEquals(original.getWebSocketMaxBinaryMessageSize(),
                        deserialized.getWebSocketMaxBinaryMessageSize());
        assertEquals(original.getWebSocketMaxBinaryMessageBufferSize(),
                        deserialized.getWebSocketMaxBinaryMessageBufferSize());

        assertEquals(original.isFetchPolyfillEnabled(), deserialized.isFetchPolyfillEnabled());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serializationSSLContextProvider() throws Exception {
        final WebClientOptions original = new WebClientOptions();
        original.setSSLContext(SSLContext.getDefault());

        final byte[] bytes = SerializationUtils.serialize(original);
        final WebClientOptions deserialized = SerializationUtils.deserialize(bytes);

        assertNull(deserialized.getSSLContext());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serializationSSLTrustStore() throws Exception {
        final WebClientOptions original = new WebClientOptions();

        final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        original.setSSLTrustStore(keyStore);

        final byte[] bytes = SerializationUtils.serialize(original);
        final WebClientOptions deserialized = SerializationUtils.deserialize(bytes);

        assertNull(deserialized.getSSLTrustStore());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void sslClientCertificateStore() throws Exception {
        final WebClientOptions original = new WebClientOptions();

        final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        original.setSSLClientCertificateKeyStore(keyStore, "secret".toCharArray());

        final byte[] bytes = SerializationUtils.serialize(original);
        final WebClientOptions deserialized = SerializationUtils.deserialize(bytes);

        assertNull(deserialized.getSSLClientCertificateStore());
        Assertions.assertArrayEquals("secret".toCharArray(), deserialized.getSSLClientCertificatePassword());
    }
}
