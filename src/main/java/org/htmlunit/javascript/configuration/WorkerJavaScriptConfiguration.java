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
package org.htmlunit.javascript.configuration;

import java.util.Map;
import java.util.WeakHashMap;

import org.htmlunit.BrowserVersion;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.host.BroadcastChannel;
import org.htmlunit.javascript.host.Cache;
import org.htmlunit.javascript.host.CacheStorage;
import org.htmlunit.javascript.host.ClientRect;
import org.htmlunit.javascript.host.FontFace;
import org.htmlunit.javascript.host.FontFaceSet;
import org.htmlunit.javascript.host.ImageBitmap;
import org.htmlunit.javascript.host.MessageChannel;
import org.htmlunit.javascript.host.MessagePort;
import org.htmlunit.javascript.host.Notification;
import org.htmlunit.javascript.host.PerformanceObserver;
import org.htmlunit.javascript.host.PerformanceObserverEntryList;
import org.htmlunit.javascript.host.PermissionStatus;
import org.htmlunit.javascript.host.Permissions;
import org.htmlunit.javascript.host.PushManager;
import org.htmlunit.javascript.host.PushSubscription;
import org.htmlunit.javascript.host.PushSubscriptionOptions;
import org.htmlunit.javascript.host.ReadableStream;
import org.htmlunit.javascript.host.StorageManager;
import org.htmlunit.javascript.host.TextDecoder;
import org.htmlunit.javascript.host.TextEncoder;
import org.htmlunit.javascript.host.URL;
import org.htmlunit.javascript.host.URLSearchParams;
import org.htmlunit.javascript.host.WebSocket;
import org.htmlunit.javascript.host.arrays.Atomics;
import org.htmlunit.javascript.host.canvas.CanvasGradient;
import org.htmlunit.javascript.host.canvas.CanvasPattern;
import org.htmlunit.javascript.host.canvas.ImageBitmapRenderingContext;
import org.htmlunit.javascript.host.canvas.ImageData;
import org.htmlunit.javascript.host.canvas.Path2D;
import org.htmlunit.javascript.host.canvas.TextMetrics;
import org.htmlunit.javascript.host.canvas.WebGL2RenderingContext;
import org.htmlunit.javascript.host.canvas.WebGLActiveInfo;
import org.htmlunit.javascript.host.canvas.WebGLBuffer;
import org.htmlunit.javascript.host.canvas.WebGLFramebuffer;
import org.htmlunit.javascript.host.canvas.WebGLProgram;
import org.htmlunit.javascript.host.canvas.WebGLQuery;
import org.htmlunit.javascript.host.canvas.WebGLRenderbuffer;
import org.htmlunit.javascript.host.canvas.WebGLRenderingContext;
import org.htmlunit.javascript.host.canvas.WebGLSampler;
import org.htmlunit.javascript.host.canvas.WebGLShader;
import org.htmlunit.javascript.host.canvas.WebGLShaderPrecisionFormat;
import org.htmlunit.javascript.host.canvas.WebGLSync;
import org.htmlunit.javascript.host.canvas.WebGLTexture;
import org.htmlunit.javascript.host.canvas.WebGLTransformFeedback;
import org.htmlunit.javascript.host.canvas.WebGLUniformLocation;
import org.htmlunit.javascript.host.canvas.WebGLVertexArrayObject;
import org.htmlunit.javascript.host.crypto.Crypto;
import org.htmlunit.javascript.host.crypto.CryptoKey;
import org.htmlunit.javascript.host.crypto.SubtleCrypto;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.dom.DOMMatrix;
import org.htmlunit.javascript.host.dom.DOMMatrixReadOnly;
import org.htmlunit.javascript.host.dom.DOMPoint;
import org.htmlunit.javascript.host.dom.DOMPointReadOnly;
import org.htmlunit.javascript.host.dom.DOMRectReadOnly;
import org.htmlunit.javascript.host.dom.DOMRequest;
import org.htmlunit.javascript.host.dom.DOMStringList;
import org.htmlunit.javascript.host.event.CloseEvent;
import org.htmlunit.javascript.host.event.CustomEvent;
import org.htmlunit.javascript.host.event.ErrorEvent;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventSource;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.javascript.host.event.IDBVersionChangeEvent;
import org.htmlunit.javascript.host.event.MessageEvent;
import org.htmlunit.javascript.host.event.ProgressEvent;
import org.htmlunit.javascript.host.event.PromiseRejectionEvent;
import org.htmlunit.javascript.host.event.SecurityPolicyViolationEvent;
import org.htmlunit.javascript.host.event.WebGLContextEvent;
import org.htmlunit.javascript.host.fetch.Headers;
import org.htmlunit.javascript.host.fetch.Request;
import org.htmlunit.javascript.host.fetch.Response;
import org.htmlunit.javascript.host.file.Blob;
import org.htmlunit.javascript.host.file.File;
import org.htmlunit.javascript.host.file.FileList;
import org.htmlunit.javascript.host.file.FileReader;
import org.htmlunit.javascript.host.idb.IDBCursor;
import org.htmlunit.javascript.host.idb.IDBCursorWithValue;
import org.htmlunit.javascript.host.idb.IDBDatabase;
import org.htmlunit.javascript.host.idb.IDBFactory;
import org.htmlunit.javascript.host.idb.IDBIndex;
import org.htmlunit.javascript.host.idb.IDBKeyRange;
import org.htmlunit.javascript.host.idb.IDBObjectStore;
import org.htmlunit.javascript.host.idb.IDBOpenDBRequest;
import org.htmlunit.javascript.host.idb.IDBRequest;
import org.htmlunit.javascript.host.idb.IDBTransaction;
import org.htmlunit.javascript.host.media.MediaSource;
import org.htmlunit.javascript.host.media.PeriodicSyncManager;
import org.htmlunit.javascript.host.media.SourceBuffer;
import org.htmlunit.javascript.host.media.SourceBufferList;
import org.htmlunit.javascript.host.network.NetworkInformation;
import org.htmlunit.javascript.host.performance.Performance;
import org.htmlunit.javascript.host.performance.PerformanceEntry;
import org.htmlunit.javascript.host.performance.PerformanceMark;
import org.htmlunit.javascript.host.performance.PerformanceMeasure;
import org.htmlunit.javascript.host.performance.PerformanceResourceTiming;
import org.htmlunit.javascript.host.worker.DedicatedWorkerGlobalScope;
import org.htmlunit.javascript.host.worker.ServiceWorkerRegistration;
import org.htmlunit.javascript.host.worker.SyncManager;
import org.htmlunit.javascript.host.worker.Worker;
import org.htmlunit.javascript.host.worker.WorkerLocation;
import org.htmlunit.javascript.host.worker.WorkerNavigator;
import org.htmlunit.javascript.host.xml.FormData;
import org.htmlunit.javascript.host.xml.XMLHttpRequest;
import org.htmlunit.javascript.host.xml.XMLHttpRequestEventTarget;
import org.htmlunit.javascript.host.xml.XMLHttpRequestUpload;

/**
 * A container for all the JavaScript configuration information for workers.
 *
 * @author Ronald Brill
 */
public final class WorkerJavaScriptConfiguration extends AbstractJavaScriptConfiguration {

    @SuppressWarnings("unchecked")
    static final Class<? extends HtmlUnitScriptable>[] CLASSES_ = new Class[] {
        Atomics.class,
        Blob.class, BroadcastChannel.class,
        Cache.class, CacheStorage.class, CanvasGradient.class, CanvasPattern.class,
        ClientRect.class, CloseEvent.class,
        Crypto.class, CryptoKey.class, CustomEvent.class,
        DedicatedWorkerGlobalScope.class, DOMException.class, DOMMatrix.class, DOMMatrixReadOnly.class,
        DOMPoint.class, DOMPointReadOnly.class, DOMRectReadOnly.class, DOMRequest.class, DOMStringList.class,
        ErrorEvent.class, Event.class, EventSource.class, EventTarget.class,
        File.class, FileList.class, FileReader.class,
        FontFace.class, FontFaceSet.class, FormData.class,
        Headers.class,
        IDBCursor.class, IDBCursorWithValue.class, IDBDatabase.class, IDBFactory.class, IDBIndex.class,
        IDBKeyRange.class, IDBObjectStore.class, IDBOpenDBRequest.class, IDBRequest.class, IDBTransaction.class,
        IDBVersionChangeEvent.class, ImageBitmap.class, ImageBitmapRenderingContext.class, ImageData.class,
        MediaSource.class, MessageChannel.class, MessageEvent.class, MessagePort.class,
        NetworkInformation.class, Notification.class,
        Path2D.class, Performance.class, PerformanceEntry.class, PerformanceMark.class, PerformanceMeasure.class,
        PerformanceObserver.class, PerformanceObserverEntryList.class, PerformanceResourceTiming.class,
        PeriodicSyncManager.class, Permissions.class, PermissionStatus.class,
        ProgressEvent.class, PromiseRejectionEvent.class,
        PushManager.class, PushSubscription.class, PushSubscriptionOptions.class,
        ReadableStream.class, Request.class, Response.class,
        SecurityPolicyViolationEvent.class, ServiceWorkerRegistration.class, SourceBuffer.class, SourceBufferList.class,
        StorageManager.class, SubtleCrypto.class, SyncManager.class,
        TextDecoder.class, TextEncoder.class, TextMetrics.class,
        URL.class, URLSearchParams.class,
        WebGLContextEvent.class, WebGL2RenderingContext.class, WebGLActiveInfo.class, WebGLBuffer.class,
        WebGLFramebuffer.class, WebGLProgram.class, WebGLQuery.class,
        WebGLRenderbuffer.class, WebGLRenderingContext.class,
        WebGLSampler.class, WebGLShader.class, WebGLShaderPrecisionFormat.class, WebGLSync.class, WebGLTexture.class,
        WebGLTransformFeedback.class, WebGLUniformLocation.class, WebGLVertexArrayObject.class, WebSocket.class,
        Worker.class, WorkerLocation.class, WorkerNavigator.class,
        XMLHttpRequest.class, XMLHttpRequestEventTarget.class, XMLHttpRequestUpload.class
    };

    /** Cache of browser versions and their corresponding JavaScript configurations. */
    private static final Map<String, WorkerJavaScriptConfiguration> CONFIGURATION_MAP_ = new WeakHashMap<>();

    /**
     * Constructor is only called from {@link #getInstance(BrowserVersion)} which is synchronized.
     * @param browser the browser version to use
     */
    private WorkerJavaScriptConfiguration(final BrowserVersion browser) {
        super(browser);
    }

    /**
     * Returns the instance that represents the configuration for the specified {@link BrowserVersion}.
     * This method is synchronized to allow multi-threaded access to the JavaScript configuration.
     * @param browserVersion the {@link BrowserVersion}
     * @return the instance for the specified {@link BrowserVersion}
     */
    @SuppressWarnings("PMD.SingletonClassReturningNewInstance")
    public static synchronized WorkerJavaScriptConfiguration getInstance(final BrowserVersion browserVersion) {
        if (browserVersion == null) {
            throw new IllegalArgumentException("BrowserVersion must be provided");
        }
        WorkerJavaScriptConfiguration configuration = CONFIGURATION_MAP_.get(browserVersion.getNickname());

        if (configuration == null) {
            configuration = new WorkerJavaScriptConfiguration(browserVersion);
            CONFIGURATION_MAP_.put(browserVersion.getNickname(), configuration);
        }
        return configuration;
    }

    @Override
    protected Class<? extends HtmlUnitScriptable>[] getClasses() {
        return CLASSES_;
    }
}
