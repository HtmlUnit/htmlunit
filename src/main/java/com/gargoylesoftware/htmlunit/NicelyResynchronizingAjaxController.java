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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This {@link AjaxController} resynchronizes calls calling from the main thread.
 * The idea is that asynchronous AJAX calls performed directly in response to a user
 * action (therefore in the "main" thread and not in the thread of a background task)
 * are directly useful for the user. To easily have a testable state, these calls
 * are performed synchronously.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class NicelyResynchronizingAjaxController extends AjaxController {

    private static final Log LOG = LogFactory.getLog(NicelyResynchronizingAjaxController.class);

    private transient WeakReference<Thread> originatedThread_;

    /**
     * Creates an instance.
     */
    public NicelyResynchronizingAjaxController() {
        init();
    }

    /**
     * Initializes this instance.
     */
    private void init() {
        originatedThread_ = new WeakReference<Thread>(Thread.currentThread());
    }

    /**
     * Resynchronizes calls performed from the thread where this instance has been created.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean processSynchron(final HtmlPage page, final WebRequest settings, final boolean async) {
        if (async && isInOriginalThread()) {
            LOG.info("Re-synchronized call to " + settings.getUrl());
            return true;
        }
        return !async;
    }

    /**
     * Indicates if the currently executing thread is the one in which this instance has been created.
     * @return <code>true</code> if it's the same thread
     */
    boolean isInOriginalThread() {
        return Thread.currentThread() == originatedThread_.get();
    }

    /**
     * Custom deserialization logic.
     * @param stream the stream from which to read the object
     * @throws IOException if an IO error occurs
     * @throws ClassNotFoundException if a class cannot be found
     */
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        init();
    }

}
