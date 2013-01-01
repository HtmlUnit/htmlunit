/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
 * @author Kostadin Chikov
 * @author Ronald Brill
 */
class GAEJavaScriptExecutor extends DefaultJavaScriptExecutor {
    private static final long serialVersionUID = 6730370472962866668L;

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
        long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + timeoutMillis;

        while (currentTime < expirationTime) {
            final JavaScriptJobManager jobManager = getJobManagerWithEarliestJob();
            if (jobManager == null) {
                break;
            }

            final JavaScriptJob earliestJob = jobManager.getEarliestJob();
            if (earliestJob == null) {
                break;
            }
            if (expirationTime < earliestJob.getTargetExecutionTime()) {
                break;
            }

            // sleep if there is time remaining in the earliestJob.
            final long sleepTime = earliestJob.getTargetExecutionTime() - currentTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            final boolean ran = jobManager.runSingleJob(earliestJob);
            if (ran) {
                count++;
            }
            currentTime = System.currentTimeMillis();
        }
        return count;
    }

}
