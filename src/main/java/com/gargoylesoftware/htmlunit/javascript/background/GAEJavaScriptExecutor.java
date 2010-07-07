/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Google App Engine specific subclass to facilitate execution of JS jobs.
 *
 * @version $Revision$
 * @author Amit Manjhi
 *
 */
public class GAEJavaScriptExecutor extends JavaScriptExecutor {

    private static final long serialVersionUID = 6720347050164623356L;

    /** Creates an EventLoop for the webClient.
     *
     * @param webClient the provided webClient
     */
    public GAEJavaScriptExecutor(final WebClient webClient) {
        super(webClient);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void startThreadIfNeeded() {
        // no-op
    }

    /**
     * Executes the jobs in the eventLoop till timeoutMillis expires or the eventLoop becomes empty.
     * @param timeoutMillis the timeout in milliseconds
     * @return the number of jobs executed
     */
    @Override
    public int pumpEventLoop(final long timeoutMillis) {
        int count = 0;
        final long expirationTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < expirationTime) {
            final JobExecutor jobExecutor = getEarliestJob();
            if (jobExecutor == null) {
                break;
            }
            if (expirationTime < jobExecutor.getEarliestJob().getTargetExecutionTime()) {
                break;
            }
            // sleep if there is time remaining in the earliestJob.
            final long sleepTime = jobExecutor.getEarliestJob().getTargetExecutionTime()
                - System.currentTimeMillis();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            final boolean ran = jobExecutor.getJobManager().runSingleJob(jobExecutor.getEarliestJob());
            if (ran) {
                count++;
            }
        }
        return count;
    }

}
