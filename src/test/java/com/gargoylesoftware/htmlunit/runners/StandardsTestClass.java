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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.internal.MethodSorter;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * Test class annotated with {@link com.gargoylesoftware.htmlunit.annotations.StandardsMode}.
 * @author Ahmed Ashour
 */
public class StandardsTestClass extends TestClass {

    /**
     * The constructor.
     * @param clazz the class
     */
    public StandardsTestClass(final Class<?> clazz) {
        super(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void scanAnnotatedMembers(final Map<Class<? extends Annotation>,
            List<FrameworkMethod>> methodsForAnnotations,
            final Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations) {
        for (final Class<?> eachClass : getSuperClasses(getJavaClass())) {
            for (final Method eachMethod : MethodSorter.getDeclaredMethods(eachClass)) {
                addToAnnotationLists(new FrameworkMethod(eachMethod), methodsForAnnotations);
            }
            // Fields are ignored
        }
        for (final  Map.Entry<Class<? extends Annotation>,
                List<FrameworkMethod>> methodsEntry : methodsForAnnotations.entrySet()) {
            final Class<? extends Annotation> key = methodsEntry.getKey();
            if (key == Test.class) {
                final List<FrameworkMethod> methods = methodsEntry.getValue();
                final List<FrameworkMethod> newMethods = new ArrayList<>(methods.size() * 2);
                for (final FrameworkMethod m : methods) {
                    newMethods.add(new StandardsFrameworkMethod(m.getMethod(), false));
                    newMethods.add(new StandardsFrameworkMethod(m.getMethod(), true));
                }
                methodsForAnnotations.put(key, newMethods);
            }
        }
    }

    private static List<Class<?>> getSuperClasses(final Class<?> testClass) {
        final ArrayList<Class<?>> results = new ArrayList<>();
        Class<?> current = testClass;
        while (current != null) {
            results.add(current);
            current = current.getSuperclass();
        }
        return results;
    }
}
