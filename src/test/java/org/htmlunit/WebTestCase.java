/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.htmlunit.junit.BrowserVersionClassTemplateInvocationContextProvider;
import org.htmlunit.junit.ErrorOutputChecker;
import org.htmlunit.junit.SetExpectedAlertsBeforeTestExecutionCallback;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassTemplate;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

/**
 * Common superclass for HtmlUnit tests.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Michael Ottati
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@ClassTemplate
@ExtendWith({BrowserVersionClassTemplateInvocationContextProvider.class,
             SetExpectedAlertsBeforeTestExecutionCallback.class,
             ErrorOutputChecker.class})
@TestMethodOrder(MethodOrderer.DisplayName.class)
public abstract class WebTestCase {

    /** The html5 doctype. */
    public static final String DOCTYPE_HTML = "<!DOCTYPE html>\n";

    /**
     * Make the test method name available to the tests.
     */
    public TestInfo testInfo_;

    // /** Logging support. */
    // private static final Log LOG = LogFactory.getLog(WebTestCase.class);

    /** save the environment */
    private static final Locale SAVE_LOCALE = Locale.getDefault();

    // 12346 seems to be in use on our CI server

    /** The listener port for the web server. */
    public static final int PORT = Integer.parseInt(System.getProperty("htmlunit.test.port", "22222"));

    /** The second listener port for the web server, used for cross-origin tests. */
    public static final int PORT2 = Integer.parseInt(System.getProperty("htmlunit.test.port2", "22223"));

    /** The third listener port for the web server, used for cross-origin tests. */
    public static final int PORT3 = Integer.parseInt(System.getProperty("htmlunit.test.port3", "22224"));

    /** The listener port used for our primitive server tests. */
    public static final int PORT_PRIMITIVE_SERVER = Integer.parseInt(
                                                        System.getProperty("htmlunit.test.port_primitive", "22225"));

    /** The listener port used for our proxy tests. */
    public static final int PORT_PROXY_SERVER = Integer.parseInt(
                                                        System.getProperty("htmlunit.test.port_proxy", "22226"));

    /** The SOCKS proxy port to use for SOCKS proxy tests. */
    public static final int SOCKS_PROXY_PORT = Integer.parseInt(
            System.getProperty("htmlunit.test.socksproxy.port", "22227"));

    /** The SOCKS proxy host to use for SOCKS proxy tests. */
    public static final String SOCKS_PROXY_HOST = System.getProperty("htmlunit.test.socksproxy.host", "localhost");

    /** The default time used to wait for the expected alerts. */
    protected static final Duration DEFAULT_WAIT_TIME = Duration.ofSeconds(1);

    /** Constant for the URL which is used in the tests. */
    public static final URL URL_FIRST;

    /** Constant for the URL which is used in the tests. */
    public static final URL URL_SECOND;

    /**
     * Constant for the URL which is used in the tests.
     * This URL doesn't use the same host name as {@link #URL_FIRST} and {@link #URL_SECOND}.
     */
    public static final URL URL_THIRD;

    /**
     * Constant for a URL used in tests that responds with Access-Control-Allow-Origin.
     */
    public static final URL URL_CROSS_ORIGIN;

    /**
     * To get an origin header with two things in it, there needs to be a chain of two
     * cross-origin referers. So we need a second extra origin.
     */
    public static final URL URL_CROSS_ORIGIN2;

    /**
     * Constant for the base URL for cross-origin tests.
     */
    public static final URL URL_CROSS_ORIGIN_BASE;

    private BrowserVersion browserVersion_;
    private String[] expectedAlerts_;
    private MockWebConnection mockWebConnection_;

    static {
        try {
            URL_FIRST = new URL("http://localhost:" + PORT + "/");
            URL_SECOND = new URL("http://localhost:" + PORT + "/second/");
            URL_THIRD = new URL("http://127.0.0.1:" + PORT + "/third/");
            URL_CROSS_ORIGIN = new URL("http://127.0.0.1:" + PORT2 + "/corsAllowAll");
            URL_CROSS_ORIGIN2 = new URL("http://localhost:" + PORT3 + "/");
            URL_CROSS_ORIGIN_BASE = new URL("http://localhost:" + PORT2 + "/");
        }
        catch (final MalformedURLException e) {
            // This is theoretically impossible.
            throw new IllegalStateException("Unable to create URL constants");
        }
    }

    /**
     * Constructor.
     */
    protected WebTestCase() {
    }

    @BeforeEach
    void init(final TestInfo testInfo) {
        testInfo_ = testInfo;
    }

    /**
     * Assert that the specified object is null.
     * @param object the object to check
     */
    public static void assertNull(final Object object) {
        Assertions.assertNull(object, "Expected null but found [" + object + "]");
    }

    /**
     * Assert that the specified object is null.
     * @param message the message
     * @param object the object to check
     */
    public static void assertNull(final String message, final Object object) {
        Assertions.assertNull(object, message);
    }

    /**
     * Assert that the specified object is not null.
     * @param object the object to check
     */
    public static void assertNotNull(final Object object) {
        Assertions.assertNotNull(object);
    }

    /**
     * Assert that the specified object is not null.
     * @param message the message
     * @param object the object to check
     */
    public static void assertNotNull(final String message, final Object object) {
        Assertions.assertNotNull(object, message);
    }

    /**
     * Asserts that two objects refer to the same object.
     * @param expected the expected object
     * @param actual the actual object
     */
    public static void assertSame(final Object expected, final Object actual) {
        Assertions.assertSame(expected, actual);
    }

    /**
     * Asserts that two objects refer to the same object.
     * @param message the message
     * @param expected the expected object
     * @param actual the actual object
     */
    public static void assertSame(final String message, final Object expected, final Object actual) {
        Assertions.assertSame(expected, actual, message);
    }

    /**
     * Asserts that two objects do not refer to the same object.
     * @param expected the expected object
     * @param actual the actual object
     */
    public static void assertNotSame(final Object expected, final Object actual) {
        Assertions.assertNotSame(expected, actual);
    }

    /**
     * Asserts that two objects do not refer to the same object.
     * @param message the message
     * @param expected the expected object
     * @param actual the actual object
     */
    public static void assertNotSame(final String message, final Object expected, final Object actual) {
        Assertions.assertNotSame(expected, actual, message);
    }

    /**
     * Facility to test external form of urls. Comparing external form of URLs is
     * really faster than URL.equals() as the host doesn't need to be resolved.
     * @param expectedUrl the expected URL
     * @param actualUrl the URL to test
     */
    protected static void assertEquals(final URL expectedUrl, final URL actualUrl) {
        Assertions.assertEquals(expectedUrl.toExternalForm(), actualUrl.toExternalForm());
    }

    /**
     * Asserts the two objects are equal.
     * @param expected the expected object
     * @param actual the object to test
     */
    protected static void assertEquals(final Object expected, final Object actual) {
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Asserts the two objects are equal.
     * @param message the message
     * @param expected the expected object
     * @param actual the object to test
     */
    protected static void assertEquals(final String message, final Object expected, final Object actual) {
        Assertions.assertEquals(expected, actual, message);
    }

    /**
     * Asserts the two ints are equal.
     * @param expected the expected int
     * @param actual the int to test
     */
    protected static void assertEquals(final int expected, final int actual) {
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Asserts the two boolean are equal.
     * @param expected the expected boolean
     * @param actual the boolean to test
     */
    protected void assertEquals(final boolean expected, final boolean actual) {
        Assertions.assertEquals(Boolean.valueOf(expected), Boolean.valueOf(actual));
    }

    /**
     * Facility to test external form of urls. Comparing external form of URLs is
     * really faster than URL.equals() as the host doesn't need to be resolved.
     * @param message the message to display if assertion fails
     * @param expectedUrl the string representation of the expected URL
     * @param actualUrl the URL to test
     */
    protected void assertEquals(final String message, final URL expectedUrl, final URL actualUrl) {
        Assertions.assertEquals(expectedUrl.toExternalForm(), actualUrl.toExternalForm(), message);
    }

    /**
     * Facility to test external form of a URL.
     * @param expectedUrl the string representation of the expected URL
     * @param actualUrl the URL to test
     */
    protected void assertEquals(final String expectedUrl, final URL actualUrl) {
        Assertions.assertEquals(expectedUrl, actualUrl.toExternalForm());
    }

    /**
     * Facility method to avoid having to create explicitly a list from
     * a String[] (for example when testing received alerts).
     * Transforms the String[] to a List before calling
     * {@link org.junit.Assert#assertEquals(java.lang.Object, java.lang.Object)}.
     * @param expected the expected strings
     * @param actual the collection of strings to test
     */
    protected void assertEquals(final String[] expected, final List<String> actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * Facility method to avoid having to create explicitly a list from
     * a String[] (for example when testing received alerts).
     * Transforms the String[] to a List before calling
     * {@link org.junit.Assert#assertEquals(java.lang.String, java.lang.Object, java.lang.Object)}.
     * @param message the message to display if assertion fails
     * @param expected the expected strings
     * @param actual the collection of strings to test
     */
    protected void assertEquals(final String message, final String[] expected, final List<String> actual) {
        Assertions.assertEquals(Arrays.asList(expected).toString(), actual.toString(), message);
    }

    /**
     * Facility to test external form of a URL.
     * @param message the message to display if assertion fails
     * @param expectedUrl the string representation of the expected URL
     * @param actualUrl the URL to test
     */
    protected void assertEquals(final String message, final String expectedUrl, final URL actualUrl) {
        Assertions.assertEquals(expectedUrl, actualUrl.toExternalForm(), message);
    }

    /**
     * Assert the specified condition is true.
     * @param condition condition to test
     */
    protected void assertTrue(final boolean condition) {
        Assertions.assertTrue(condition);
    }

    /**
     * Assert the specified condition is true.
     * @param message message to show
     * @param condition condition to test
     */
    protected void assertTrue(final String message, final boolean condition) {
        Assertions.assertTrue(condition, message);
    }

    /**
     * Assert the specified condition is false.
     * @param condition condition to test
     */
    protected void assertFalse(final boolean condition) {
        Assertions.assertFalse(condition);
    }

    /**
     * Assert the specified condition is false.
     * @param message message to show
     * @param condition condition to test
     */
    protected void assertFalse(final String message, final boolean condition) {
        Assertions.assertFalse(condition, message);
    }

    /**
     * Sets the browser version.
     * @param browserVersion the browser version
     */
    public void setBrowserVersion(final BrowserVersion browserVersion) {
        browserVersion_ = browserVersion;
    }

    /**
     * Returns the current {@link BrowserVersion}.
     * @return current {@link BrowserVersion}
     */
    public final BrowserVersion getBrowserVersion() {
        if (browserVersion_ == null) {
            throw new IllegalStateException("You must annotate the test class with '@RunWith(BrowserRunner.class)'");
        }
        return browserVersion_;
    }

    /**
     * Sets the expected alerts.
     * @param expectedAlerts the expected alerts
     */
    public void setExpectedAlerts(final String... expectedAlerts) {
        expectedAlerts_ = expectedAlerts;
    }

    /**
     * Returns the expected alerts.
     * @return the expected alerts
     */
    protected String[] getExpectedAlerts() {
        return expectedAlerts_;
    }

    /**
     * Expand "§§URL§§" to the provided URL in the expected alerts.
     * @param url the URL to expand
     */
    protected void expandExpectedAlertsVariables(final URL url) {
        expandExpectedAlertsVariables(url.toExternalForm());
    }

    /**
     * Expand "§§URL§§" to the provided URL in the expected alerts.
     * @param url the URL to expand
     */
    protected void expandExpectedAlertsVariables(final String url) {
        if (expectedAlerts_ == null) {
            throw new IllegalStateException("You must annotate the test class with '@RunWith(BrowserRunner.class)'");
        }
        for (int i = 0; i < expectedAlerts_.length; i++) {
            expectedAlerts_[i] = expectedAlerts_[i].replaceAll("§§URL§§", url);
        }
    }

    /**
     * A generics-friendly version of {@link SerializationUtils#clone(Serializable)}.
     * @param <T> the type of the object being cloned
     * @param object the object being cloned
     * @return a clone of the specified object
     */
    protected <T extends Serializable> T clone(final T object) {
        return SerializationUtils.clone(object);
    }

    /**
     * Prepare the environment.
     * Rhino has localized error message... for instance for French
     */
    @BeforeAll
    public static void beforeClass() {
        Locale.setDefault(Locale.US);
    }

    /**
     * Restore the environment.
     */
    @AfterAll
    public static void afterClass() {
        Locale.setDefault(SAVE_LOCALE);
    }

    /**
     * Verifies the captured alerts.
     * @param func actual string producer
     * @param expected the expected string
     * @throws Exception in case of failure
     */
    protected void verify(final Supplier<String> func, final String expected) throws Exception {
        verify(func, expected, DEFAULT_WAIT_TIME);
    }

    /**
     * Verifies the captured alerts.
     * @param func actual string producer
     * @param expected the expected string
     * @param maxWaitTime the maximum time to wait to get the alerts (in millis)
     * @throws Exception in case of failure
     */
    protected void verify(final Supplier<String> func, final String expected,
            final Duration maxWaitTime) throws Exception {
        final long maxWait = System.currentTimeMillis() + maxWaitTime.toMillis();

        String actual = null;
        while (System.currentTimeMillis() < maxWait) {
            actual = func.get();

            if (Objects.equals(expected, actual)) {
                break;
            }

            Thread.sleep(50);
        }

        assertEquals(expected, actual);
    }

    /**
     * Returns the mock WebConnection instance for the current test.
     * @return the mock WebConnection instance for the current test
     */
    protected MockWebConnection getMockWebConnection() {
        if (mockWebConnection_ == null) {
            mockWebConnection_ = new MockWebConnection();
        }
        return mockWebConnection_;
    }

    /**
     * Cleanup after a test.
     */
    @AfterEach
    public void releaseResources() {
        if (mockWebConnection_ != null) {
            mockWebConnection_.clear();
        }
    }

    /**
     * Gets the active JavaScript threads.
     * @return the threads
     */
    protected List<Thread> getJavaScriptThreads() {
        final Thread[] threads = new Thread[Thread.activeCount() + 10];
        Thread.enumerate(threads);
        final List<Thread> jsThreads = new ArrayList<>();
        for (final Thread t : threads) {
            if (t != null && t.getName().startsWith("JS executor for")) {
                jsThreads.add(t);
            }
        }

        return jsThreads;
    }

    /**
     * Read the content of the given file using our classloader.
     * @param fileName the file name
     * @return the content as string
     * @throws IOException in case of error
     */
    protected String getFileContent(final String fileName) throws IOException {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
        assertNotNull(fileName, stream);
        return IOUtils.toString(stream, ISO_8859_1);
    }

    protected void compareImages(final String expected, final String current) throws IOException {
        final String currentBase64Image = current.split(",")[1];
        final byte[] currentImageBytes = Base64.getDecoder().decode(currentBase64Image);

        try (ByteArrayInputStream currentBis = new ByteArrayInputStream(currentImageBytes)) {
            final BufferedImage currentImage = ImageIO.read(currentBis);

            compareImages(expected, current, currentImage);
        }
    }

    protected void compareImages(final String expected,
            final String current, final BufferedImage currentImage) throws IOException {
        final String expectedBase64Image = expected.split(",")[1];
        final byte[] expectedImageBytes = Base64.getDecoder().decode(expectedBase64Image);

        try (ByteArrayInputStream expectedBis = new ByteArrayInputStream(expectedImageBytes)) {
            final BufferedImage expectedImage = ImageIO.read(expectedBis);

            final ImageComparison imageComparison = new ImageComparison(expectedImage, currentImage);
            // imageComparison.setMinimalRectangleSize(10);
            imageComparison.setPixelToleranceLevel(0.2);
            imageComparison.setAllowingPercentOfDifferentPixels(7);

            final ImageComparisonResult imageComparisonResult = imageComparison.compareImages();
            final ImageComparisonState imageComparisonState = imageComparisonResult.getImageComparisonState();

            if (ImageComparisonState.SIZE_MISMATCH == imageComparisonState) {
                final String dir = "target/" + testInfo_.getDisplayName();
                Files.createDirectories(Paths.get(dir));

                final File expectedOut = new File(dir, "expected.png");
                final File currentOut = new File(dir, "current.png");
                ImageComparisonUtil.saveImage(expectedOut, expectedImage);
                ImageComparisonUtil.saveImage(currentOut, currentImage);

                String fail = "The images are different in size - "
                        + "expected: " + expectedImage.getWidth() + "x" + expectedImage.getHeight()
                        + " current: " + currentImage.getWidth() + "x" + currentImage.getHeight()
                        + " (expected: " + expectedOut.getAbsolutePath()
                            + " current: " + currentOut.getAbsolutePath() + ")";
                if (current != null) {
                    fail += "; current data: '" + current + "'";
                }
                fail(fail);
            }
            else if (ImageComparisonState.MISMATCH == imageComparisonState) {
                final String dir = "target/" + testInfo_.getDisplayName();
                Files.createDirectories(Paths.get(dir));

                final File expectedOut = new File(dir, "expected.png");
                final File currentOut = new File(dir, "current.png");
                final File differenceOut = new File(dir, "difference.png");
                ImageComparisonUtil.saveImage(expectedOut, expectedImage);
                ImageComparisonUtil.saveImage(currentOut, currentImage);
                ImageComparisonUtil.saveImage(differenceOut, imageComparisonResult.getResult());

                String fail = "The images are different (expected: " + expectedOut.getAbsolutePath()
                            + " current: " + currentOut.getAbsolutePath()
                            + " difference: " + differenceOut.getAbsolutePath() + ")";
                if (current != null) {
                    fail += "; current data: '" + current + "'";
                }
                fail(fail);
            }
        }
    }
}
