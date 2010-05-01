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
package com.gargoylesoftware.htmlunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * A class runner with retries.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class RetriesRunner extends BlockJUnit4ClassRunner {

    /**
     * Constructs a new instance.
     *
     * @param klass the class
     * @throws InitializationError if the test class is malformed.
     */
    public RetriesRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    /**
     * The number of retries that test will be executed.
     * The test will fail if and only if all retrials failed.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Reries {

        /**
         * The value.
         */
        int value() default 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Statement methodBlock(final FrameworkMethod method) {
        final Statement statement = super.methodBlock(method);
        final int retries = Math.max(getRetries(method), 1);
        return new RetriesStatement(statement, method, retries);
    }

    private int getRetries(final FrameworkMethod method) {
        final Reries retries = method.getAnnotation(Reries.class);
        return retries != null ? retries.value() : 1;
    }
}
