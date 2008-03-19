/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
                addFailure(new AssertionError(e.getCause()));
            }
        }
    }
}
