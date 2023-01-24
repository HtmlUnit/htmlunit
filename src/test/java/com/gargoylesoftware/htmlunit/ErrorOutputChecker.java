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
package com.gargoylesoftware.htmlunit;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * JUnit 4 {@link org.junit.Rule} verifying that nothing is printed to {@link System#err}
 * during test execution. If this is the case, the rule generates a failure for
 * the unit test.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class ErrorOutputChecker implements MethodRule {
    private PrintStream originalErr_;
    private final ByteArrayOutputStream baos_ = new ByteArrayOutputStream();
    private static final Pattern[] PATTERNS = {
            // jetty
            Pattern.compile(".*Logging initialized .* to org.eclipse.jetty.util.log.StdErrLog.*\r?\n"),

            // slf4j
            Pattern.compile("SLF4J: .*\r?\n"),

            // Quercus
            Pattern.compile(".*com.caucho.quercus.servlet.QuercusServlet initImpl\r?\n"),
            Pattern.compile(".*QuercusServlet starting as QuercusServletImpl\r?\n"),
            Pattern.compile(".*Quercus finished initialization in \\d*ms\r?\n"),

            // Xalan
            Pattern.compile("ERROR:\\s*'Use of the extension function "
                    + "'http://xml\\.apache\\.org/xalan/java/.*' "
                    + "is not allowed when the secure processing feature is set to true\\.'\r?\n"),
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        if (target instanceof WebDriverTestCase) {
            final WebDriverTestCase testCase = (WebDriverTestCase) target;
            if (testCase.useRealBrowser()) {
                return base;
            }
        }

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                wrapSystemErr();
                try {
                    base.evaluate();
                    verifyNoOutput();
                }
                finally {
                    restoreSystemErr();
                }
            }
        };
    }

    void verifyNoOutput() {
        if (baos_.size() != 0) {
            String output = baos_.toString();

            // remove webdriver messages
            for (final Pattern pattern : PATTERNS) {
                output = pattern.matcher(output).replaceAll("");
            }

            if (!output.isEmpty()) {
                if (output.contains("ChromeDriver")) {
                    throw new RuntimeException("Outdated Chrome driver version: " + output);
                }
                if (output.contains("geckodriver")) {
                    throw new RuntimeException("Outdated Gecko driver version: " + output);
                }
                output = StringUtils.replaceEach(output, new String[] {"\n", "\r"}, new String[]{"\\n", "\\r"});
                throw new RuntimeException("Test has produced output to System.err: " + output);
            }
        }
    }

    private void wrapSystemErr() {
        originalErr_ = System.err;
        System.setErr(new NSAPrintStreamWrapper(originalErr_, baos_));
    }

    void restoreSystemErr() {
        System.setErr(originalErr_);
    }
}

/**
 * A {@link PrintStream} spying what is written on the wrapped stream.
 * It prints the content to the wrapped {@link PrintStream} and captures it simultaneously.
 * @author Marc Guillemot
 */
class NSAPrintStreamWrapper extends PrintStream {
    private PrintStream wrapped_;

    NSAPrintStreamWrapper(final PrintStream original, final OutputStream spyOut) {
        super(spyOut, true);
        wrapped_ = original;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return wrapped_.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        return wrapped_.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return wrapped_.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        super.flush();
        wrapped_.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        super.close();
        wrapped_.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkError() {
        super.checkError();
        return wrapped_.checkError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final int b) {
        super.write(b);
        wrapped_.write(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final byte[] buf, final int off, final int len) {
        super.write(buf, off, len);
        wrapped_.write(buf, off, len);
    }
}
