/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * JUnit 4 {@link org.junit.Rule} verifying that nothing is printed to {@link System#err}
 * during test execution. If this is the case, the rule generates a failure for
 * the unit test.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class ErrorOutputChecker implements TestRule {
    private PrintStream originalErr_;
    private final ByteArrayOutputStream baos_ = new ByteArrayOutputStream();
    private static final Pattern WEB_DRIVER_CHROME_MSG =
            Pattern.compile("Starting ChromeDriver \\(v[\\.0-9]*\\) on port \\d*\r?\n"
                    + "(Only local connections are allowed\\.\r?\n)?");
    private static final Pattern WEB_DRIVER_IE_MSG =
            Pattern.compile("Started InternetExplorerDriver server \\(\\d\\d\\-bit\\)\r?\n"
                    + ".*\r?\nListening on port \\d*\r?\n");

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement apply(final Statement base, final Description description) {
        wrapSystemErr();

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
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

    private void verifyNoOutput() {
        if (baos_.size() != 0) {
            String output = baos_.toString();
            // remove webdriver message
            output = WEB_DRIVER_CHROME_MSG.matcher(output).replaceAll("");
            output = WEB_DRIVER_IE_MSG.matcher(output).replaceAll("");
            if (output.length() > 0) {
                throw new RuntimeException("Test has produced output to System.err: " + output);
            }
        }
    }

    private void wrapSystemErr() {
        originalErr_ = System.err;
        System.setErr(new NSAPrintStreamWrapper(originalErr_, baos_));
    }

    private void restoreSystemErr() {
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
    public int hashCode() {
        return wrapped_.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object obj) {
        return wrapped_.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return wrapped_.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void flush() {
        super.flush();
        wrapped_.flush();
    }

    /**
     * {@inheritDoc}
     */
    public void close() {
        super.close();
        wrapped_.close();
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkError() {
        super.checkError();
        return wrapped_.checkError();
    }

    /**
     * {@inheritDoc}
     */
    public void write(final int b) {
        super.write(b);
        wrapped_.write(b);
    }

    /**
     * {@inheritDoc}
     */
    public void write(final byte[] buf, final int off, final int len) {
        super.write(buf, off, len);
        wrapped_.write(buf, off, len);
    }
}
