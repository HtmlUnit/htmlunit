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

import java.lang.reflect.Method;

import org.junit.runners.model.FrameworkMethod;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * A method of a class annotated with {@link com.gargoylesoftware.htmlunit.annotations.StandardsMode}.
 * @author Ahmed Ashour
 */
public class StandardsFrameworkMethod extends FrameworkMethod {

    private final boolean standards_;

    /**
     * The constructor.
     * @param method the method
     * @param standards whether in Standards Mode or not
     */
    public StandardsFrameworkMethod(final Method method, final boolean standards) {
        super(method);
        standards_ = standards;
    }

    /**
     * Returns whether we are in Standards Mode or not.
     * @return whether we are in Standards Mode or not
     */
    public boolean isStandards() {
        return standards_;
    }

    @Override
    public boolean isShadowedBy(final FrameworkMethod other) {
        if (!other.getName().equals(getName())) {
            return false;
        }
        if (other.getMethod().getParameterTypes().length != getMethod().getParameterTypes().length) {
            return false;
        }
        for (int i = 0; i < other.getMethod().getParameterTypes().length; i++) {
            if (!other.getMethod().getParameterTypes()[i].equals(getMethod().getParameterTypes()[i])) {
                return false;
            }
        }
        if (((StandardsFrameworkMethod) other).standards_ != standards_) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!FrameworkMethod.class.isInstance(obj)) {
            return false;
        }
        return ((FrameworkMethod) obj).getMethod().equals(getMethod())
                && ((StandardsFrameworkMethod) obj).standards_ == standards_;
    }

    @Override
    public int hashCode() {
        return getMethod().hashCode() + (standards_ ? 1 : 0);
    }

    @Override
    public String getName() {
        return super.getName() + (standards_ ? " [Standards]" : "");
    }

    @Override
    public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
        if (target instanceof WebDriverTestCase) {
            ((WebDriverTestCase) target).setUseStandards(standards_);
        }
        return super.invokeExplosively(target, params);
    }
}
