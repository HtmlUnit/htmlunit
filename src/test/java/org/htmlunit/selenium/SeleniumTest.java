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

import org.htmlunit.ClipboardHandler;
import org.htmlunit.WebDriverTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

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

        // use a simple clipboard handler to run in headless mode also
        if (driver instanceof HtmlUnitDriver htmlUnitDriver) {
            htmlUnitDriver.getWebClient().setClipboardHandler(
                    new ClipboardHandler() {
                        private String content_;

                        @Override
                        public String getClipboardContent() {
                            return content_;
                        }

                        @Override
                        public void setClipboardContent(final String content) {
                            content_ = content;
                        }

                    });
        }

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
     * Creates a new fluent assertion for a {@link Boolean} value.
     *
     * @param actual the boolean value to inspect
     * @return a {@link BooleanAssert} instance
     */
    public static BooleanAssert assertThat(Boolean actual) {
        return new BooleanAssert(actual);
    }

    /**
     * Creates a new fluent assertion for a {@link Number} value.
     *
     * @param actual the number value to inspect
     * @return a {@link NumberAssert} instance
     */
    public static NumberAssert assertThat(Number actual) {
        return new NumberAssert(actual);
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
     * Creates a new fluent assertion for an exception type.
     *
     * @param <T> the type of the expected throwable
     * @param type the class of the expected throwable
     * @return an {@link ExceptionAssert} instance
     */
    public static <T extends Throwable> ExceptionAssert<T> assertThatExceptionOfType(Class<T> type) {
        return new ExceptionAssert<>(type);
    }

    /**
     * Provides basic fluent assertions for object instances.
     *
     * @param <T> the type of the object under test
     */
    public static class SimpleAssert<T> {

        /** The actual value under test. */
        protected final T actual_;

        /** Custom contextual description for assertion failure messages. */
        protected String description_;

        /**
         * Constructs a new assertion wrapper around the given actual value.
         *
         * @param actual the actual value
         */
        public SimpleAssert(T actual) {
            actual_ = actual;
        }

        /**
         * Sets a custom contextual description for this assertion to be included
         * in the assertion error message if the assertion fails.
         *
         * @param description the description pattern or message
         * @param args optional arguments for formatting the description pattern
         * @return {@code this} assertion instance for method chaining
         */
        public SimpleAssert<T> as(String description, Object... args) {
            if (description != null && args != null && args.length > 0) {
                description_ = String.format(description, args);
            }
            else {
                description_ = description;
            }
            return this;
        }

        /**
         * Formats the error message by prepending the custom description if set.
         *
         * @param message the base assertion error message
         * @return the formatted error message
         */
        protected String formatMessage(String message) {
            if (description_ != null && !description_.isEmpty()) {
                return "[" + description_ + "] " + message;
            }
            return message;
        }

        /**
         * Verifies that the actual value is equal to the expected value.
         *
         * @param expected the expected value
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual value is not equal to the expected value
         */
        public SimpleAssert<T> isEqualTo(T expected) {
            if (!Objects.equals(actual_, expected)) {
                throw new AssertionError(
                    formatMessage(String.format("Expected: <%s> but was: <%s>", expected, actual_))
                );
            }
            return this;
        }

        /**
         * Verifies that the actual value is not equal to the expected value.
         *
         * @param expected the unexpected value
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual value is equal to the expected value
         */
        public SimpleAssert<T> isNotEqualTo(T expected) {
            if (Objects.equals(actual_, expected)) {
                throw new AssertionError(
                    formatMessage(String.format("Expected value not to be equal to <%s>", expected))
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
                throw new AssertionError(formatMessage("Expected values array must not be null"));
            }
            for (T value : values) {
                if (Objects.equals(actual_, value)) {
                    return this;
                }
            }
            throw new AssertionError(formatMessage(String.format("Expected <%s> to be in %s", actual_, Arrays.toString(values))));
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
                throw new AssertionError(formatMessage("Expected collection must not be null"));
            }
            if (!values.contains(actual_)) {
                throw new AssertionError(
                    formatMessage(String.format("Expected <%s> to be in %s", actual_, values))
                );
            }
            return this;
        }
    }

    /**
     * Provides fluent assertion capabilities specifically for {@link Boolean} instances.
     */
    public static class BooleanAssert extends SimpleAssert<Boolean> {

        /**
         * Constructs a new boolean assertion wrapper around the given actual boolean.
         *
         * @param actual the actual boolean value
         */
        public BooleanAssert(Boolean actual) {
            super(actual);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BooleanAssert as(String description, Object... args) {
            super.as(description, args);
            return this;
        }

        /**
         * Verifies that the actual boolean value is {@code true}.
         *
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual value is not {@code true}
         */
        public BooleanAssert isTrue() {
            if (!Boolean.TRUE.equals(actual_)) {
                throw new AssertionError(
                    formatMessage(String.format("Expected true, but was: <%s>", actual_))
                );
            }
            return this;
        }

        /**
         * Verifies that the actual boolean value is {@code false}.
         *
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual value is not {@code false}
         */
        public BooleanAssert isFalse() {
            if (!Boolean.FALSE.equals(actual_)) {
                throw new AssertionError(
                    formatMessage(String.format("Expected false, but was: <%s>", actual_))
                );
            }
            return this;
        }
    }

    /**
     * Provides fluent assertion capabilities specifically for {@link Number} instances.
     */
    public static class NumberAssert extends SimpleAssert<Number> {

        /**
         * Constructs a new number assertion wrapper around the given actual number.
         *
         * @param actual the actual number value
         */
        public NumberAssert(Number actual) {
            super(actual);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NumberAssert as(String description, Object... args) {
            super.as(description, args);
            return this;
        }

        /**
         * Verifies that the actual number is zero.
         *
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual value is {@code null} or not zero
         */
        public NumberAssert isZero() {
            if (actual_ == null || actual_.doubleValue() != 0.0) {
                throw new AssertionError(
                    formatMessage(String.format("Expected zero, but was: <%s>", actual_))
                );
            }
            return this;
        }

        /**
         * Verifies that the actual number is not zero.
         *
         * @return {@code this} assertion instance for method chaining
         * @throws AssertionError if the actual value is {@code null} or zero
         */
        public NumberAssert isNotZero() {
            if (actual_ == null || actual_.doubleValue() == 0.0) {
                throw new AssertionError(
                    formatMessage(String.format("Expected non-zero, but was: <%s>", actual_))
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
        public StringAssert as(String description, Object... args) {
            super.as(description, args);
            return this;
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
        public StringAssert isNotEqualTo(String expected) {
            super.isNotEqualTo(expected);
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
                if (!actual_.contains(value)) {
                    throw new AssertionError(
                        formatMessage(String.format("Expected string to contain <%s> but was <%s>", value, actual_))
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
            if (!actual_.isEmpty()) {
                throw new AssertionError(
                    formatMessage(String.format("Expected string to be empty but was <%s>", actual_))
                );
            }
            return this;
        }
    }

    /**
     * Provides fluent assertion capabilities for testing thrown exceptions.
     *
     * @param <T> the type of the expected throwable
     */
    public static class ExceptionAssert<T extends Throwable> {

        private final Class<T> expectedType_;
        private String description_;

        /**
         * Constructs a new exception assertion for the given exception class.
         *
         * @param expectedType the expected exception type
         */
        public ExceptionAssert(Class<T> expectedType) {
            expectedType_ = expectedType;
        }

        /**
         * Sets a custom contextual description for this assertion to be included
         * in the assertion error message if the assertion fails.
         *
         * @param description the description pattern or message
         * @param args optional arguments for formatting the description pattern
         * @return {@code this} assertion instance for method chaining
         */
        public ExceptionAssert<T> as(String description, Object... args) {
            if (description != null && args != null && args.length > 0) {
                description = String.format(description, args);
            }
            else {
                description_ = description;
            }
            return this;
        }

        /**
         * Formats the error message by prepending the custom description if set.
         *
         * @param message the base assertion error message
         * @return the formatted error message
         */
        protected String formatMessage(String message) {
            if (description_ != null && !description_.isEmpty()) {
                return "[" + description_ + "] " + message;
            }
            return message;
        }

        /**
         * Asserts that executing the runnable throws an exception of the expected type.
         *
         * @param runnable the code block expected to throw an exception
         * @throws AssertionError if no exception is thrown or an exception of an unexpected type is thrown
         */
        public void isThrownBy(ThrowableRunnable runnable) {
            try {
                runnable.run();
            }
            catch (Throwable actual) {
                if (!expectedType_.isInstance(actual)) {
                    throw new AssertionError(
                        formatMessage(String.format("Expected %s to be thrown, but %s was thrown instead.",
                            expectedType_.getName(), actual.getClass().getName())), actual);
                }
                return;
            }
            throw new AssertionError(
                formatMessage(String.format("Expected %s to be thrown, but nothing was thrown.", expectedType_.getName()))
            );
        }
    }
}