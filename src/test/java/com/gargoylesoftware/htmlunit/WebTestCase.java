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
package com.gargoylesoftware.htmlunit;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

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
public abstract class WebTestCase {

    /**
     * Make the test method name available to the tests.
     */
    @Rule
    public TestName testMethodName_ = new TestName();

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(WebTestCase.class);

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

    /** Succeeds the SOCKS proxy tests (for windows) . */
    public static final boolean GEOLOCATION_IGNORE = Boolean.valueOf(
            System.getProperty("htmlunit.test.geolocation.ignore", "false"));

    /** The default time used to wait for the expected alerts. */
    protected static final long DEFAULT_WAIT_TIME = 1000;

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

    /**
     * The name of the system property used to determine if files should be generated
     * or not in {@link #createTestPageForRealBrowserIfNeeded(String,List)}.
     */
    public static final String PROPERTY_GENERATE_TESTPAGES
        = "com.gargoylesoftware.htmlunit.WebTestCase.GenerateTestpages";

    /** To be documented. */
    protected static final BrowserVersion FLAG_ALL_BROWSERS
        = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.BEST_SUPPORTED)
                    .setApplicationName("FLAG_ALL_BROWSERS")
                    .build();

    /** To be documented. */
    protected static final ThreadLocal<BrowserVersion> generateTest_browserVersion_ = new ThreadLocal<>();

    private BrowserVersion browserVersion_;
    private String[] expectedAlerts_;
    private MockWebConnection mockWebConnection_;
    private String generateTest_content_;
    private List<String> generateTest_expectedAlerts_;
    private boolean generateTest_notYetImplemented_;
    private String generateTest_testName_;

    /**
     * JUnit 4 {@link Rule} controlling System.err.
     */
    @Rule
    public final TestRule errOutputChecker_ = new ErrorOutputChecker();

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
        generateTest_browserVersion_.remove();
    }

    /**
     * Assert that the specified object is null.
     * @param object the object to check
     */
    public static void assertNull(final Object object) {
        Assert.assertNull("Expected null but found [" + object + "]", object);
    }

    /**
     * Assert that the specified object is null.
     * @param message the message
     * @param object the object to check
     */
    public static void assertNull(final String message, final Object object) {
        Assert.assertNull(message, object);
    }

    /**
     * Assert that the specified object is not null.
     * @param object the object to check
     */
    public static void assertNotNull(final Object object) {
        Assert.assertNotNull(object);
    }

    /**
     * Assert that the specified object is not null.
     * @param message the message
     * @param object the object to check
     */
    public static void assertNotNull(final String message, final Object object) {
        Assert.assertNotNull(message, object);
    }

    /**
     * Asserts that two objects refer to the same object.
     * @param expected the expected object
     * @param actual the actual object
     */
    public static void assertSame(final Object expected, final Object actual) {
        Assert.assertSame(expected, actual);
    }

    /**
     * Asserts that two objects refer to the same object.
     * @param message the message
     * @param expected the expected object
     * @param actual the actual object
     */
    public static void assertSame(final String message, final Object expected, final Object actual) {
        Assert.assertSame(message, expected, actual);
    }

    /**
     * Asserts that two objects do not refer to the same object.
     * @param expected the expected object
     * @param actual the actual object
     */
    public static void assertNotSame(final Object expected, final Object actual) {
        Assert.assertNotSame(expected, actual);
    }

    /**
     * Asserts that two objects do not refer to the same object.
     * @param message the message
     * @param expected the expected object
     * @param actual the actual object
     */
    public static void assertNotSame(final String message, final Object expected, final Object actual) {
        Assert.assertNotSame(message, expected, actual);
    }

    /**
     * Facility to test external form of urls. Comparing external form of URLs is
     * really faster than URL.equals() as the host doesn't need to be resolved.
     * @param expectedUrl the expected URL
     * @param actualUrl the URL to test
     */
    protected static void assertEquals(final URL expectedUrl, final URL actualUrl) {
        Assert.assertEquals(expectedUrl.toExternalForm(), actualUrl.toExternalForm());
    }

    /**
     * Asserts the two objects are equal.
     * @param expected the expected object
     * @param actual the object to test
     */
    protected static void assertEquals(final Object expected, final Object actual) {
        Assert.assertEquals(expected, actual);
    }

    /**
     * Asserts the two objects are equal.
     * @param message the message
     * @param expected the expected object
     * @param actual the object to test
     */
    protected static void assertEquals(final String message, final Object expected, final Object actual) {
        Assert.assertEquals(message, expected, actual);
    }

    /**
     * Asserts the two ints are equal.
     * @param expected the expected int
     * @param actual the int to test
     */
    protected static void assertEquals(final int expected, final int actual) {
        Assert.assertEquals(expected, actual);
    }

    /**
     * Asserts the two boolean are equal.
     * @param expected the expected boolean
     * @param actual the boolean to test
     */
    protected void assertEquals(final boolean expected, final boolean actual) {
        Assert.assertEquals(Boolean.valueOf(expected), Boolean.valueOf(actual));
    }

    /**
     * Facility to test external form of urls. Comparing external form of URLs is
     * really faster than URL.equals() as the host doesn't need to be resolved.
     * @param message the message to display if assertion fails
     * @param expectedUrl the string representation of the expected URL
     * @param actualUrl the URL to test
     */
    protected void assertEquals(final String message, final URL expectedUrl, final URL actualUrl) {
        Assert.assertEquals(message, expectedUrl.toExternalForm(), actualUrl.toExternalForm());
    }

    /**
     * Facility to test external form of a URL.
     * @param expectedUrl the string representation of the expected URL
     * @param actualUrl the URL to test
     */
    protected void assertEquals(final String expectedUrl, final URL actualUrl) {
        Assert.assertEquals(expectedUrl, actualUrl.toExternalForm());
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
        Assert.assertEquals(message, Arrays.asList(expected).toString(), actual.toString());
    }

    /**
     * Facility to test external form of a URL.
     * @param message the message to display if assertion fails
     * @param expectedUrl the string representation of the expected URL
     * @param actualUrl the URL to test
     */
    protected void assertEquals(final String message, final String expectedUrl, final URL actualUrl) {
        Assert.assertEquals(message, expectedUrl, actualUrl.toExternalForm());
    }

    /**
     * Assert the specified condition is true.
     * @param condition condition to test
     */
    protected void assertTrue(final boolean condition) {
        Assert.assertTrue(condition);
    }

    /**
     * Assert the specified condition is true.
     * @param message message to show
     * @param condition condition to test
     */
    protected void assertTrue(final String message, final boolean condition) {
        Assert.assertTrue(message, condition);
    }

    /**
     * Assert the specified condition is false.
     * @param condition condition to test
     */
    protected void assertFalse(final boolean condition) {
        Assert.assertFalse(condition);
    }

    /**
     * Assert the specified condition is false.
     * @param message message to show
     * @param condition condition to test
     */
    protected void assertFalse(final String message, final boolean condition) {
        Assert.assertFalse(message, condition);
    }

    /**
     * Returns an input stream for the specified file name. Refer to {@link #getFileObject(String)}
     * for details on how the file is located.
     * @param fileName the base file name
     * @return the input stream
     * @throws FileNotFoundException if the file cannot be found
     */
    public static InputStream getFileAsStream(final String fileName) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(getFileObject(fileName)));
    }

    /**
     * Returns a File object for the specified file name. This is different from just
     * <code>new File(fileName)</code> because it will adjust the location of the file
     * depending on how the code is being executed.
     *
     * @param fileName the base filename
     * @return the new File object
     * @throws FileNotFoundException if the file doesn't exist
     */
    public static File getFileObject(final String fileName) throws FileNotFoundException {
        final String localizedName = fileName.replace('/', File.separatorChar);

        File file = new File(localizedName);
        if (!file.exists()) {
            file = new File("../../" + localizedName);
        }

        if (!file.exists()) {
            try {
                System.out.println("currentDir=" + new File(".").getCanonicalPath());
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
            throw new FileNotFoundException(localizedName);
        }
        return file;
    }

    /**
     * Facility method transforming expectedAlerts to a list and calling
     * {@link #createTestPageForRealBrowserIfNeeded(String, List)}.
     * @param content the content of the HTML page
     * @param expectedAlerts the expected alerts
     * @throws IOException if writing file fails
     */
    protected void createTestPageForRealBrowserIfNeeded(final String content, final String[] expectedAlerts)
        throws IOException {
        createTestPageForRealBrowserIfNeeded(content, Arrays.asList(expectedAlerts));
    }

    /**
     * Generates an instrumented HTML file in the temporary dir to easily make a manual test in a real browser.
     * The file is generated only if the system property {@link #PROPERTY_GENERATE_TESTPAGES} is set.
     * @param content the content of the HTML page
     * @param expectedAlerts the expected alerts
     * @throws IOException if writing file fails
     */
    protected void createTestPageForRealBrowserIfNeeded(final String content, final List<String> expectedAlerts)
        throws IOException {

        // save the information to create a test for WebDriver
        generateTest_content_ = content;
        generateTest_expectedAlerts_ = expectedAlerts;
        final Method testMethod = findRunningJUnitTestMethod();
        generateTest_testName_ = testMethod.getDeclaringClass().getSimpleName() + "_" + testMethod.getName() + ".html";

        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null) {
            // should be optimized....

            // calls to alert() should be replaced by call to custom function
            String newContent = StringUtils.replace(content, "alert(", "htmlunitReserved_caughtAlert(");

            final String instrumentationJS = createInstrumentationScript(expectedAlerts);

            // first version, we assume that there is a <head> and a </body> or a </frameset>
            if (newContent.indexOf("<head>") > -1) {
                newContent = StringUtils.replaceOnce(newContent, "<head>", "<head>" + instrumentationJS);
            }
            else {
                newContent = StringUtils.replaceOnce(newContent, "<html>",
                        "<html>\n<head>\n" + instrumentationJS + "\n</head>\n");
            }
            final String endScript = "\n<script>htmlunitReserved_addSummaryAfterOnload();</script>\n";
            if (newContent.contains("</body>")) {
                newContent = StringUtils.replaceOnce(newContent, "</body>", endScript + "</body>");
            }
            else {
                LOG.info("No test generated: currently only content with a <head> and a </body> is supported");
            }

            final File f = File.createTempFile("TEST" + '_', ".html");
            FileUtils.writeStringToFile(f, newContent, "ISO-8859-1");
            LOG.info("Test file written: " + f.getAbsolutePath());
        }
        else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("System property \"" + PROPERTY_GENERATE_TESTPAGES
                    + "\" not set, don't generate test HTML page for real browser");
            }
        }
    }

    /**
     * @param expectedAlerts the list of the expected alerts
     * @return the script to be included at the beginning of the generated HTML file
     * @throws IOException in case of problem
     */
    private String createInstrumentationScript(final List<String> expectedAlerts) throws IOException {
        // generate the js code
        final String baseJS = getFileContent("alertVerifier.js");

        final StringBuilder sb = new StringBuilder();
        sb.append("\n<script type='text/javascript'>\n");
        sb.append("var htmlunitReserved_tab = [");
        for (final ListIterator<String> iter = expectedAlerts.listIterator(); iter.hasNext();) {
            if (iter.hasPrevious()) {
                sb.append(", ");
            }
            String message = iter.next();
            message = StringUtils.replace(message, "\\", "\\\\");
            message = message.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r");
            sb.append("{expected: \"").append(message).append("\"}");
        }
        sb.append("];\n\n");
        sb.append(baseJS);
        sb.append("</script>\n");
        return sb.toString();
    }

    /**
     * Finds from the call stack the active running JUnit test case
     * @return the test case method
     * @throws RuntimeException if no method could be found
     */
    private Method findRunningJUnitTestMethod() {
        final Class<?> cl = getClass();
        final Class<?>[] args = {};

        // search the initial junit test
        final Throwable t = new Exception();
        for (int i = t.getStackTrace().length - 1; i >= 0; i--) {
            final StackTraceElement element = t.getStackTrace()[i];
            if (element.getClassName().equals(cl.getName())) {
                try {
                    final Method m = cl.getMethod(element.getMethodName(), args);
                    if (isPublicTestMethod(m)) {
                        return m;
                    }
                }
                catch (final Exception e) {
                    // can't access, ignore it
                }
            }
        }
        throw new RuntimeException("No JUnit test case method found in call stack");
    }

    /**
     * From Junit. Test if the method is a junit test.
     * @param method the method
     * @return {@code true} if this is a junit test
     */
    private static boolean isPublicTestMethod(final Method method) {
        return method.getParameterTypes().length == 0
            && (method.getName().startsWith("test") || method.getAnnotation(Test.class) != null)
            && method.getReturnType() == Void.TYPE
            && Modifier.isPublic(method.getModifiers());
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
    protected final BrowserVersion getBrowserVersion() {
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
    @BeforeClass
    public static void beforeClass() {
        Locale.setDefault(Locale.US);
    }

    /**
     * Restore the environment.
     */
    @AfterClass
    public static void afterClass() {
        Locale.setDefault(SAVE_LOCALE);
    }

    /**
     * Generates an HTML file that can be loaded and understood as a test.
     * @throws IOException in case of problem
     */
    @After
    public void generateTestForWebDriver() throws IOException {
        // cleanup
        expectedAlerts_ = null;
        mockWebConnection_ = null;

        if (generateTest_content_ != null && !generateTest_notYetImplemented_) {
            final File targetDir = new File("target/generated_tests");
            FileUtils.forceMkdir(targetDir);

            final File outFile = new File(targetDir, generateTest_testName_);

            FileUtils.writeStringToFile(outFile, generateTest_content_, ISO_8859_1);

            // write the expected alerts
            final String suffix;
            BrowserVersion browser = generateTest_browserVersion_.get();
            if (browser == null) {
                browser = getBrowserVersion();
            }
            if (browser == FLAG_ALL_BROWSERS) {
                suffix = ".expected";
            }
            else {
                suffix = "." + browser.getNickname() + ".expected";
            }

            final File expectedLog = new File(outFile.getParentFile(), outFile.getName() + suffix);

            try (FileOutputStream fos = new FileOutputStream(expectedLog)) {
                try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(generateTest_expectedAlerts_);
                }
            }
        }
        generateTest_content_ = null;
        generateTest_expectedAlerts_ = null;
        generateTest_testName_ = null;
    }

    /**
     * To be documented.
     * @param status the status
     */
    protected void setGenerateTest_notYetImplemented(final boolean status) {
        generateTest_notYetImplemented_ = status;
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
    @After
    public void releaseResources() {
        mockWebConnection_ = null;
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

            compareImages(expected, currentImage);
        }
    }

    protected void compareImages(final String expected, final BufferedImage currentImage) throws IOException {
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
                final String dir = "target/" + testMethodName_.getMethodName();
                Files.createDirectories(Paths.get(dir));

                final File expectedOut = new File(dir, "expected.png");
                final File currentOut = new File(dir, "current.png");
                ImageComparisonUtil.saveImage(expectedOut, expectedImage);
                ImageComparisonUtil.saveImage(currentOut, currentImage);

                fail("The images are differnet in size - "
                        + "expected: " + expectedImage.getWidth() + "x" + expectedImage.getHeight()
                        + " current: " + currentImage.getWidth() + "x" + currentImage.getHeight()
                        + " (expected: " + expectedOut.getAbsolutePath()
                            + " current: " + currentOut.getAbsolutePath() + ")");
            }
            else if (ImageComparisonState.MISMATCH == imageComparisonState) {
                final String dir = "target/" + testMethodName_.getMethodName();
                Files.createDirectories(Paths.get(dir));

                final File expectedOut = new File(dir, "expected.png");
                final File currentOut = new File(dir, "current.png");
                final File differenceOut = new File(dir, "difference.png");
                ImageComparisonUtil.saveImage(expectedOut, expectedImage);
                ImageComparisonUtil.saveImage(currentOut, currentImage);
                ImageComparisonUtil.saveImage(differenceOut, imageComparisonResult.getResult());
                fail("The images are differnet (expected: " + expectedOut.getAbsolutePath()
                            + " current: " + currentOut.getAbsolutePath()
                            + " difference: " + differenceOut.getAbsolutePath() + ")");
            }
        }
    }
}
