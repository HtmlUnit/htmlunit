/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.runners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * A {@link FrameworkMethod} with parameters.
 *
 * @author Ahmed Ashour
 */
public class FrameworkMethodWithParameters extends FrameworkMethod {

    private final TestClass testClass_;
    private final List<Object> parameters_;

    /**
     * The constructor.
     *
     * @param testClass the test class
     * @param method the method
     * @param parameters the parameters
     */
    public FrameworkMethodWithParameters(final TestClass testClass, final Method method,
            final List<Object> parameters) {
        super(method);
        testClass_ = testClass;
        parameters_ = parameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invokeExplosively(final Object target, final Object... params)
        throws Throwable {
        if (!parameters_.isEmpty()) {
            final List<FrameworkField> annotatedFieldsByParameter = testClass_.getAnnotatedFields(Parameter.class);
            if (annotatedFieldsByParameter.size() != parameters_.size()) {
                throw new Exception(
                        "Wrong number of parameters and @Parameter fields."
                                + " @Parameter fields counted: "
                                + annotatedFieldsByParameter.size()
                                + ", available parameters: " + parameters_.size()
                                + ".");
            }
            for (final FrameworkField each : annotatedFieldsByParameter) {
                final Field field = each.getField();
                final Parameter annotation = field.getAnnotation(Parameter.class);
                final int index = annotation.value();
                try {
                    field.set(target, parameters_.get(index));
                }
                catch (final IllegalArgumentException iare) {
                    throw new Exception(testClass_.getName()
                            + ": Trying to set " + field.getName()
                            + " with the value " + parameters_.get(index)
                            + " that is not the right type ("
                            + parameters_.get(index).getClass().getSimpleName()
                            + " instead of " + field.getType().getSimpleName()
                            + ").", iare);
                }
            }
        }
        return super.invokeExplosively(target, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        if (!parameters_.isEmpty()) {
            return '_' + StringUtils.join(parameters_, '_');
        }
        return super.getName();
    }
}
