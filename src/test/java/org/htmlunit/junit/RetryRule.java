/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.junit;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Simple rule supports retry tests. We need this at least for async tests on
 * the ci server.
 *
 * @author Ronald Brill
 */
public class RetryRule implements TestRule {
    private AtomicInteger retryCount_;

    public RetryRule(final int retryCount) {
        retryCount_ = new AtomicInteger(retryCount);
    }

    @Override
    public Statement apply(final Statement stmt, final Description desc) {
        return statement(stmt, desc);
    }

    private Statement statement(final Statement stmt, final Description desc) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                while (retryCount_.getAndDecrement() > 0) {
                    try {
                        System.out.println("ret " + retryCount_.get());
                        stmt.evaluate();
                        return;
                    }
                    catch (final Throwable t) {
                        if (retryCount_.get() == 0 || desc.getAnnotation(Retry.class) == null) {
                            throw t;
                        }
                    }
                }
            }
        };
    }
}
