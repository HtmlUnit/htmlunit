/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Retries Statement.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
class RetriesStatement extends Statement {

    private Statement next_;
    private final FrameworkMethod method_;
    private final int tries_;

    RetriesStatement(final Statement next, final FrameworkMethod method, final int tries) {
        next_ = next;
        method_ = method;
        tries_ = tries;
    }

    @Override
    public void evaluate() throws Throwable {
        for (int i = 0; i < tries_; i++) {
            try {
                next_.evaluate();
                break;
            }
            catch (final Throwable t) {
                System.out.println("Failed test "
                        + method_.getMethod().getDeclaringClass().getName() + '.'
                        + method_.getMethod().getName() + " #" + (i + 1));
                if (i == tries_ - 1) {
                    throw t;
                }
            }
        }
    }

}
