/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

import java.lang.ref.WeakReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This {@link AjaxController} resynchronizes calls calling from the main thread.
 * The idea is that asynchron AJAX calls performed directly in response to a user
 * action (therefore in the "main" thread and not in the thread of a background task)
 * are directly useful for the user. To easily have a testable state, these calls
 * are performed synchron.
 *
 * <span style="color:red">EXPERIMENTAL - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 *
 * @version $Revision: 1165 $
 * @author Marc Guillemot
 */
public class NicelyResynchronizingAjaxController extends AjaxController {

    private static final long serialVersionUID = -5406000795046341395L;

    private final WeakReference originatedThread_;

    /**
     * Create an instance.
     */
    public NicelyResynchronizingAjaxController() {
        originatedThread_ = new WeakReference(Thread.currentThread());
    }

    /**
     * Return the log
     * @return The log.
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     * Resynchronizes calls performed from the thread where this instance has
     * been created.
     * {@inheritDoc}
     */
    public boolean processSynchron(final HtmlPage page, final WebRequestSettings requestSettings,
            final boolean async) {
        
        if (async && isInOriginalThread()) {
            getLog().info("Re-synchronized call to " + requestSettings.getURL());
            return true;
        }
        else {
            return !async;
        }
    }

    /**
     * Indicates if currently executing thread is the one in which this instance has been
     * created
     * @return <code>true</code> if it's the same thread
     */
    boolean isInOriginalThread() {
        return Thread.currentThread() == originatedThread_.get();
    }
}
