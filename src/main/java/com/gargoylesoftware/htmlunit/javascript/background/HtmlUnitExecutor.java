/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.background;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Wrapper for a {@link ScheduledThreadPoolExecutor} that helps us use
 * it deterministically.
 * @author Brad Clarke
 * @version $Revision$
 */
public class HtmlUnitExecutor extends ScheduledThreadPoolExecutor {

    /**
     * Event handler for thread start/stop.
     */
    public abstract static class EventHandler {
        /**
         * @param future The job that was started.
         */
        public abstract void onStart(final ScheduledFuture< ? > future);
        /**
         * @param future The job that has finished.
         */
        public abstract void onFinish(final ScheduledFuture< ? > future);
    }

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(HtmlUnitExecutor.class);

    private EventHandler eventHandler_ = null;

    /**
     * Constructs a HtmlUnitExecutor. By default this is a {@link ScheduledThreadPoolExecutor}
     * with a fixed pool size of 1.
     */
    public HtmlUnitExecutor() {
        super(1);
        setMaximumPoolSize(1);
        setKeepAliveTime(1000, TimeUnit.MILLISECONDS);
    }

    /** {@inheritDoc} */
    @Override
    protected void beforeExecute(final Thread threadForJob, final Runnable runnable) {
        super.beforeExecute(threadForJob, runnable);
        final ScheduledFuture< ? > future = (ScheduledFuture< ? >) runnable;
        LOG.debug("Starting job: " + future);
        if (eventHandler_ != null) {
            eventHandler_.onStart(future);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void afterExecute(final Runnable runnable, final Throwable exceptionFromJob) {
        super.afterExecute(runnable, exceptionFromJob);
        final ScheduledFuture< ? > future = (ScheduledFuture< ? >) runnable;
        LOG.debug("Ending job: " + future);
        if (eventHandler_ != null) {
            eventHandler_.onFinish(future);
        }
    }

    /**
     * Sets and event handler for notification of job start/stop.
     * @param eventHandler The new handler
     */
    public void setEventHandler(final EventHandler eventHandler) {
        eventHandler_ = eventHandler;
    }
}
