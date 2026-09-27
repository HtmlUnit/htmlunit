/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.selenium;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.htmlunit.WebDriverTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

/**
 * The parent class of Selenium tests.
 * <p>
 * Providing a lightweight fluent assertion API for unit tests.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class SeleniumTest extends WebDriverTestCase {

    /**
     * Starts the web server.
     */
    @Override
    @BeforeEach
    public void beforeTest() {
        try {
            startWebServer("src/test/resources/selenium", null);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the {@code WebDriver} after loading the URL with the specified relative path.
     * @param relativePath the relative path
     * @return the {@code WebDriver}
     */
    protected WebDriver getWebDriver(final String relativePath) {
        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + relativePath);
        return driver;
    }

    /**
     * Creates a new fluent assertion for a {@link String} value.
     *
     * @param actual the string value to inspect
     * @return a {@link StringAssert} instance
     */
    public static StringAssert assertThat(String actual) {
        return new StringAssert(actual);
    }

    /**
     * Creates a new fluent assertion for an object value.
     *
     * @param <T> the type of the value being asserted
     * @param actual the actual object value to inspect
     * @return a {@link SimpleAssert} instance
     */
    public static <T> SimpleAssert<T> assertThat(T actual) {
        return new SimpleAssert<>(actual);
    }

    /**
     * Functional interface representing an action that may throw an exception.
     */
    @FunctionalInterface
    public interface ThrowableRunnable {
        /**
         * Executes the action.
         *
         * @throws Throwable if an error occurs
         */
        void run() throws Throwable;
    }

    /**
     * Provides basic fluent assertions for object instances.
     *
     * @param <T> the type of the object under test
     */
    public static class SimpleAssert<T> {

        /** The actual value under test. */
        protected final T actual;

        /**
         * Constructs a new assertion wrapper around the given actual value.
         *
         * @param actual the actual value
         */
        public SimpleAssert(T actual) {
            this.actual = actual;
        }

        /**
         * Verifies that the actual value is equal to the expected value.
         *
         * @param expected the expected value
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual value is not equal to the expected value
         */
        public SimpleAssert<T> isEqualTo(T expected) {
            if (!Objects.equals(actual, expected)) {
                throw new AssertionError(
                    String.format("Expected: <%s> but was: <%s>", expected, actual)
                );
            }
            return this;
        }

        /**
         * Verifies that the actual value is contained in the given array/varargs of values.
         *
         * @param values the array of acceptable values
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual value is not in the allowed values
         */
        public SimpleAssert<T> isIn(T... values) {
            if (values == null) {
                throw new AssertionError("Expected values array must not be null");
            }
            for (T value : values) {
                if (Objects.equals(actual, value)) {
                    return this;
                }
            }
            throw new AssertionError(String.format("Expected <%s> to be in %s", actual, Arrays.toString(values)));
        }

        /**
         * Verifies that the actual value is contained in the given collection.
         *
         * @param values the collection of acceptable values
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual value is not in the collection
         */
        public SimpleAssert<T> isIn(Collection<?> values) {
            if (values == null) {
                throw new AssertionError("Expected collection must not be null");
            }
            if (!values.contains(actual)) {
                throw new AssertionError(
                    String.format("Expected <%s> to be in %s", actual, values)
                );
            }
            return this;
        }
    }

    /**
     * Provides fluent assertion capabilities specifically for {@link String} instances.
     */
    public static class StringAssert extends SimpleAssert<String> {

        /**
         * Constructs a new string assertion wrapper around the given actual string.
         *
         * @param actual the actual string value
         */
        public StringAssert(String actual) {
            super(actual);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public StringAssert isEqualTo(String expected) {
            super.isEqualTo(expected);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public StringAssert isIn(String... values) {
            super.isIn(values);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public StringAssert isIn(Collection<?> values) {
            super.isIn(values);
            return this;
        }

        /**
         * Verifies that the actual string contains all of the given character sequences.
         *
         * @param values one or more character sequences expected to be present in the actual string
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual string is {@code null} or does not contain any of the expected sequences
         */
        public StringAssert contains(CharSequence... values) {
            for (CharSequence value : values) {
                if (!actual.contains(value)) {
                    throw new AssertionError(
                        String.format("Expected string to contain <%s> but was <%s>", value, actual)
                    );
                }
            }
            return this;
        }

        /**
         * Verifies that the actual string is empty (has length 0).
         *
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual string is {@code null} or not empty
         */
        public StringAssert isEmpty() {
            if (!actual.isEmpty()) {
                throw new AssertionError(
                    String.format("Expected string to be empty but was <%s>", actual)
                );
            }
            return this;
        }
    }
}
