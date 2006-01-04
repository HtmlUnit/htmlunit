/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 * 
 * This is a class that provides thread handling services to internal clients
 * as well as exposes some of the status of these threads to the public API.
 * 
 * @version  $Revision$
 * @author Brad Clarke
 *
 */
public class ThreadManager {

    private Map threadMap_ = Collections.synchronizedMap(new HashMap());
    
    /**
     * @return The number of tracked threads. 
     */
    public int activeCount() {
        return threadMap_.size();
    }

    /**
     * Threads are given numeric IDs in JavaScript and that number is
     * stored here. If more than 65534 threads are started in the same
     * JVM this could become unpredictable.
     */
    private static int NextThreadID_ = 1;
    /**
     * HtmlUnit threads are started at a higher priority than the priority
     * of the first thread to ask for HtmlUnit thread handling services. Assuming
     * there are no users screwing with their own thread priority after starting 
     * a test this should be enough to encourage background threads to execute
     * quicker than the test they were started from, if possible.
     */
    private static final int PRIORITY = Math.min(Thread.MAX_PRIORITY, Thread
            .currentThread()
            .getPriority() + 1);
    /** 
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * 
     * Starts a new job on a background thread. Threads started by this method
     * are always considered daemon threads and will not prevent the JVM from
     * shutting down. For our purposes the JUnit test is the only thread that 
     * matters and the background threads are just there to keep the HtmlUnit 
     * objects as up to date as possible.
     * 
     * @param job The job to start
     * @return ID of the new thread, suitable for use in JavaScript and required
     * when calling {@link #stopThread(int)} 
     */
    public int startThread(final Runnable job) {
        final int myThreadID = NextThreadID_++;
        final Thread newThread = new Thread(job, "HtmlUnit Managed Thread #" + myThreadID) {
            public void run() {
                try {
                    super.run();
                }
                finally {
                    threadMap_.remove(new Integer(myThreadID));
                }
            }
        };
        newThread.setPriority(PRIORITY);
        newThread.setDaemon(true);
        threadMap_.put(new Integer(myThreadID), newThread);
        newThread.start();
        return myThreadID;
    }
    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * 
     * Stops a thread that was started in this thread manager.
     * 
     * Note: this does not immediately stop the thread, only interrupt
     * it and remove it from being tracked by this manager. The thread
     * is responsible for handling being interrupted properly and shutting
     * itself down.
     * 
     * @param threadID the ID of the thread to stop
     */
    public void stopThread(final int threadID) {
        final Thread thread = (Thread) threadMap_.remove(new Integer(threadID));
        if (thread != null) {
            thread.interrupt();
        }
    }

    /**
     * Wait for any executing background threads to complete.
     * 
     * @param maxWaitMillis The maximum time that should be waited, in milliseconds. 
     *        This is not an exact time but will be fairly close.
     * @throws InterruptedException if any of the worker threads do (really
     *         shouldn't happen)
     * @return true if all threads expired in the specified time
     */
    public boolean joinAll(long maxWaitMillis) throws InterruptedException {
        master: while (maxWaitMillis > 0 && threadMap_.size() > 0) {
            final Iterator iter = threadMap_.values().iterator();
            while (maxWaitMillis > 0 && iter.hasNext()) {
                final Thread thread;
                try {
                    thread = (Thread) iter.next();
                }
                catch (final ConcurrentModificationException e) {
                    //need to get a new iterator
                    continue master;
                }
                final long before = System.currentTimeMillis();
                thread.join(maxWaitMillis);
                maxWaitMillis = maxWaitMillis - (System.currentTimeMillis() - before);
            }
        }
        return threadMap_.size() == 0;
    }
    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * 
     * Attempts to stop running threads. Currently allows up to 
     * a 1 second wait for those threads to stop just to be sure.
     */
    public void interruptAll() {
        final Iterator iter = threadMap_.values().iterator();
        while (iter.hasNext()) {
            final Thread thread = (Thread) iter.next();
            thread.interrupt();
        }
        try {
            joinAll(1000);
        }
        catch (final InterruptedException e) {
            throw new RuntimeException("Main thread interrupted", e);
        }
    }
}
