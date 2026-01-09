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

/**
 * @author Ronald Brill
 * @since 5.0
 */
module org.htmlunit {
    requires java.desktop;
    requires java.xml;
    requires jdk.xml.dom;

    requires htmlunit.websocket.client;

    requires org.htmlunit.cssparser;
    requires org.htmlunit.corejs;
    requires org.htmlunit.csp;
    requires org.htmlunit.cyberneko;
    requires org.htmlunit.xpath;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.logging;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpmime;

    exports org.htmlunit;
    exports org.htmlunit.attachment;
    exports org.htmlunit.css;
    exports org.htmlunit.html;
    exports org.htmlunit.html.impl;
    exports org.htmlunit.html.parser;
    exports org.htmlunit.html.parser.neko;
    exports org.htmlunit.html.serializer;
    exports org.htmlunit.html.xpath;
    exports org.htmlunit.http;
    exports org.htmlunit.httpclient;
    exports org.htmlunit.javascript;
    exports org.htmlunit.javascript.background;
    exports org.htmlunit.javascript.configuration;
    exports org.htmlunit.javascript.host;
    exports org.htmlunit.javascript.host.abort;
    exports org.htmlunit.javascript.host.animations;
    exports org.htmlunit.javascript.host.arrays;
    exports org.htmlunit.javascript.host.canvas;
    exports org.htmlunit.javascript.host.crypto;
    exports org.htmlunit.javascript.host.css;
    exports org.htmlunit.javascript.host.dom;
    exports org.htmlunit.javascript.host.draganddrop;
    exports org.htmlunit.javascript.host.event;
    exports org.htmlunit.javascript.host.fetch;
    exports org.htmlunit.javascript.host.file;
    exports org.htmlunit.javascript.host.geo;
    exports org.htmlunit.javascript.host.html;
    exports org.htmlunit.javascript.host.idb;
    exports org.htmlunit.javascript.host.intl;
    exports org.htmlunit.javascript.host.media;
    exports org.htmlunit.javascript.host.media.midi;
    exports org.htmlunit.javascript.host.media.presentation;
    exports org.htmlunit.javascript.host.media.rtc;
    exports org.htmlunit.javascript.host.network;
    exports org.htmlunit.javascript.host.payment;
    exports org.htmlunit.javascript.host.performance;
    exports org.htmlunit.javascript.host.security;
    exports org.htmlunit.javascript.host.speech;
    exports org.htmlunit.javascript.host.svg;
    exports org.htmlunit.javascript.host.worker;
    exports org.htmlunit.javascript.host.xml;
    exports org.htmlunit.javascript.polyfill;
    exports org.htmlunit.javascript.preprocessor;
    exports org.htmlunit.javascript.proxyautoconfig;
    exports org.htmlunit.platform;
    exports org.htmlunit.platform.canvas.rendering;
    exports org.htmlunit.platform.dom.traversal;
    exports org.htmlunit.platform.font;
    exports org.htmlunit.platform.geom;
    exports org.htmlunit.platform.image;
    exports org.htmlunit.protocol;
    exports org.htmlunit.protocol.about;
    exports org.htmlunit.protocol.data;
    exports org.htmlunit.protocol.javascript;
    exports org.htmlunit.svg;
    exports org.htmlunit.util;
    exports org.htmlunit.util.brotli;
    exports org.htmlunit.util.geometry;
    exports org.htmlunit.websocket;
    exports org.htmlunit.webstart;
    exports org.htmlunit.xml;
}