/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import org.junit.internal.runners.MethodRoadie;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

/**
 * BrowserRoadie.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
class BrowserRoadie extends MethodRoadie {

    private final Object test_;
    private final TestMethod testMethod_;
    private final Method method_;
    private final boolean shouldFail_;
    private final String browserVersionString_;
    private final boolean notYetImplemented_;

    public BrowserRoadie(final Object test, final TestMethod testMethod, final RunNotifier notifier,
            final Description description, final Method method, final boolean shouldFail,
            final boolean notYetImplemented, final String browserVersionString) {
        super(test, testMethod, notifier, description);
        test_ = test;
        testMethod_ = testMethod;
        method_ = method;
        shouldFail_ = shouldFail;
        notYetImplemented_ = notYetImplemented;
        browserVersionString_ = browserVersionString;
    }

    @Override
    protected void runTestMethod() {
        try {
            testMethod_.invoke(test_);
            if (shouldFail_) {
                final String errorMessage;
                if (browserVersionString_ == null) {
                    errorMessage = method_.getName() + " is marked to fail with "
                        + browserVersionString_ + ", but succeeds";
                }
                else {
                    errorMessage = method_.getName() + " is marked to fail, but succeeds";
                }
                addFailure(new AssertionError(errorMessage));
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
                addFailure(new AssertionError(errorMessage));
            }
        }
        catch (final Throwable e) {
            if (!shouldFail_ && !notYetImplemented_) {
                addFailure(e.getCause());
            }
        }
    }
}
