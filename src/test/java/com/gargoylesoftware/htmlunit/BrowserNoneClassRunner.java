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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.NONE;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.MethodRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * The runner for test methods that run without any browser ({@link BrowserRunner.Browser#NONE})
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
class BrowserNoneClassRunner extends BlockJUnit4ClassRunner {

    public BrowserNoneClassRunner(final Class<WebTestCase> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodBlock(final FrameworkMethod method) {
        final Object test;
        final WebTestCase testCase;
        try {
            testCase = (WebTestCase) createTest();
            test = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                    return testCase;
                }
            } .run();
        }
        catch (final Throwable e) {
            return new Fail(e);
        }

        Statement statement = methodInvoker(method, test);
        statement = possiblyExpectingExceptions(method, test, statement);
        statement = withPotentialTimeout(method, test, statement);
        statement = withBefores(method, test, statement);
        statement = withAfters(method, test, statement);
        statement = withRules(method, test, statement);

        final NotYetImplemented notYetImplementedBrowsers = method.getAnnotation(NotYetImplemented.class);
        final boolean notYetImplemented = notYetImplementedBrowsers != null;
        statement = new BrowserStatement(statement, method.getMethod(), false,
                notYetImplemented, 1, "");
        return statement;
    }

    @Override
    protected String getName() {
        return "[No Browser]";
    }

    @Override
    protected String testName(final FrameworkMethod method) {
        if (!BrowserVersionClassRunner.maven_) {
            return super.testName(method);
        }
        String className = method.getMethod().getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        return String.format("%s[No Browser]", className + '.' + method.getName());
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        final List<FrameworkMethod> methods = super.computeTestMethods();
        for (int i = 0; i < methods.size(); i++) {
            final Method method = methods.get(i).getMethod();
            final Browsers browsers = method.getAnnotation(Browsers.class);
            if (browsers == null || browsers.value()[0] != NONE) {
                methods.remove(i--);
            }
        }
        return methods;
    }

    static boolean containsTestMethods(final Class<WebTestCase> klass) {
        for (final Method method : klass.getMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                final Browsers browsers = method.getAnnotation(Browsers.class);
                if (browsers != null && browsers.value()[0] == NONE) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void validateTestMethods(final List<Throwable> errors) {
        super.validateTestMethods(errors);
        final List<Throwable> collectederrors = new ArrayList<Throwable>();
        for (final FrameworkMethod method : computeTestMethods()) {
            final Browsers browsers = method.getAnnotation(Browsers.class);
            if (browsers != null) {
                for (final Browser browser : browsers.value()) {
                    if (browser == NONE && browsers.value().length > 1) {
                        collectederrors.add(new Exception("Method " + method.getName()
                                + "() cannot have Browser.NONE along with other Browsers."));
                    }
                }
            }
        }
        for (final Throwable error : collectederrors) {
            errors.add(error);
        }
    }

    private Statement withRules(final FrameworkMethod method, final Object target, final Statement statement) {
        Statement result = statement;
        for (final MethodRule each : getTestClass().getAnnotatedFieldValues(target, Rule.class, MethodRule.class)) {
            result = each.apply(result, method, target);
        }
        return result;
    }
}
