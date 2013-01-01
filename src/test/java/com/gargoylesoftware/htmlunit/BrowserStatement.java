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
package com.gargoylesoftware.htmlunit;

import java.lang.reflect.Method;

import org.junit.runners.model.Statement;

/**
 * The Browser Statement.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
class BrowserStatement extends Statement {

    private Statement next_;
    private final boolean shouldFail_;
    private final boolean notYetImplemented_;
    private final Method method_;
    private final String browserVersionString_;
    private final int tries_;

    BrowserStatement(final Statement next, final Method method, final boolean shouldFail,
            final boolean notYetImplemented, final int tries, final String browserVersionString) {
        next_ = next;
        method_ = method;
        shouldFail_ = shouldFail;
        notYetImplemented_ = notYetImplemented;
        tries_ = tries;
        browserVersionString_ = browserVersionString;
    }

    @Override
    public void evaluate() throws Throwable {
        for (int i = 0; i < tries_; i++) {
            try {
                evaluateSolo();
                break;
            }
            catch (final Throwable t) {
                if (shouldFail_ || notYetImplemented_) {
                    throw t;
                }
                System.out.println("Failed test "
                        + method_.getDeclaringClass().getName() + '.' + method_.getName() + " #" + (i + 1));
                if (i == tries_ - 1) {
                    throw t;
                }
            }
        }
    }

    public void evaluateSolo() throws Throwable {
        Exception toBeThrown = null;
        try {
            next_.evaluate();
            if (shouldFail_) {
                final String errorMessage;
                if (browserVersionString_ == null) {
                    errorMessage = method_.getName() + " is marked to fail with "
                        + browserVersionString_ + ", but succeeds";
                }
                else {
                    errorMessage = method_.getName() + " is marked to fail, but succeeds";
                }
                toBeThrown = new Exception(errorMessage);
            }
            else if (notYetImplemented_) {
                final String errorMessage;
                if (browserVersionString_ == null) {
                    errorMessage = method_.getName() + " is marked as not implemented but already works";
                }
                else {
                    errorMessage = method_.getName() + " is marked as not implemented with "
                        + browserVersionString_ + " but already works";
                }
                toBeThrown = new Exception(errorMessage);
            }
        }
        catch (final Throwable e) {
            if (!shouldFail_ && !notYetImplemented_) {
                throw e;
            }
        }
        if (toBeThrown != null) {
            throw toBeThrown;
        }
    }
}
