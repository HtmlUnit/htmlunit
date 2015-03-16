/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.runners.model.FrameworkMethod;

/**
 * A sub class of {@link FrameworkMethod}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class BrowserFrameworkMethod extends FrameworkMethod {

    private List<Object> parameters_;

    /**
     * The constructor.
     * @param method the method
     */
    public BrowserFrameworkMethod(final Method method) {
        super(method);
    }

    /**
     * Sets the parameters.
     * @param parameters the parameters
     */
    public void setParameters(final List<Object> parameters) {
        this.parameters_ = parameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invokeExplosively(final Object target, final Object... params)
        throws Throwable {
        if (parameters_ != null) {
            setPrivateField(target, "fInput", parameters_.get(0));
            setPrivateField(target, "fExpected", parameters_.get(1));
        }

        return super.invokeExplosively(target, params);
    }

    private void setPrivateField(final Object target, final String field, final Object value) {
        try {
            final Field f = target.getClass().getDeclaredField(field);
            f.set(target, value);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the parameters as string.
     * @return the string
     */
    public String getParametersAsString() {
        return StringUtils.join(parameters_, '_');
    }
}
