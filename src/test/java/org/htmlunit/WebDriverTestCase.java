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
package org.htmlunit;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.htmlunit.MockWebConnection.RawResponseData;
import org.htmlunit.WebServerTestCase.SSLVariant;
import org.htmlunit.attachment.DownloadingAttachmentHandler;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.junit.TestCaseCorrector;
import org.htmlunit.util.JettyServerUtils;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v156.emulation.Emulation;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverService;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.htmlunit.options.HtmlUnitDriverOptions;
import org.openqa.selenium.htmlunit.options.HtmlUnitOption;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.opentest4j.AssertionFailedError;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Base class for tests using WebDriver.
 * <p>
 * By default, this test runs with HtmlUnit, but this behavior can be changed by having a property file named
 * "{@code test.properties}" in the HtmlUnit root directory.
 * Sample (remove the part not matching your os):
 * <pre>
   browsers=hu,ff,chrome,chrome-bidi

   ff.bin=/usr/bin/firefox                              [Unix]
   ff-esr.bin=/usr/bin/firefox-esr                      [Unix]
   geckodriver.bin=/usr/bin/driver/geckodriver          [Unix]
   chrome.bin=/path/to/chromedriver                     [Unix]
   edge.bin=/path/to/chromedriver                       [Unix]

   geckodriver.bin=C:\\path\\to\\geckodriver.exe              [Windows]
   ff.bin=C:\\path\\to\\Mozilla Firefox\\firefox.exe          [Windows]
   ff-esr.bin=C:\\path\\to\\Mozilla Firefox ESR\\firefox.exe  [Windows]
   chrome.bin=C:\\path\\to\\chromedriver.exe                  [Windows]
   edge.bin=C:\\path\\to\\msedgedriver.exe                    [Windows]

   autofix=false
   </pre>

 * The file could contain some properties:
 * <ul>
 *   <li>browsers: is a comma separated list contains any combination of
 *     <ul>
 *       <li>hu (for HtmlUnit with all browser versions),</li>
 *       <li>hu-ff,</li>
 *       <li>hu-ff-esr,</li>
 *       <li>hu-chrome,</li>
 *       <li>hu-edge,</li>
 *       <li>ff, (running test using real Firefox),</li>
 *       <li>ff-esr, (running test using real Firefox ESR),</li>
 *       <li>chrome (running test using real Chrome),</li>
 *       <li>edge (running test using real Edge),</li>
 *       <li>ff-bidi, ff-esr-bidi, chrome-bidi, edge-bidi (the same real browsers but the session is
 *       started with BiDi enabled; the expectations are the same as for the classic variants),</li>
 *     </ul>
 *   </li>
 *
 *   <li>chrome.bin (mandatory if it does not exist in the <i>path</i>): is the location of the ChromeDriver binary (see
 *   <a href="https://googlechromelabs.github.io/chrome-for-testing/last-known-good-versions-with-downloads.json">Chrome Driver downloads</a>)</li>
 *   <li>geckodriver.bin (mandatory if it does not exist in the <i>path</i>): is the location of the GeckoDriver binary
 *   (see <a href="https://github.com/mozilla/geckodriver/releases">Gecko Driver Releases</a>)</li>
 *   <li>ff.bin (optional): is the location of the FF binary, in Windows use double back-slashes</li>
 *   <li>ff-esr.bin (optional): is the location of the FF binary, in Windows use double back-slashes</li>
 *   <li>edge.bin (mandatory if it does not exist in the <i>path</i>): is the location of the MicrosoftWebDriver binary
 *   (see <a href="https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/">Microsoft Edge WebDriver downloads</a>)</li>
 *   <li>autofix (optional): if {@code true}, try to automatically fix the real browser expectations,
 *   or add/remove {@code @NotYetImplemented} annotations, use with caution!</li>
 * </ul>
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@ExtendWith(TestCaseCorrector.class)
public abstract class WebDriverTestCase extends WebTestCase {

    private static final String LOG_EX_FUNCTION =
            "  function logEx(e) {\n"
            + "    let toStr = null;\n"
            + "    if (toStr === null && e instanceof EvalError) { toStr = ''; }\n"
            + "    if (toStr === null && e instanceof RangeError) { toStr = ''; }\n"
            + "    if (toStr === null && e instanceof ReferenceError) { toStr = ''; }\n"
            + "    if (toStr === null && e instanceof SyntaxError) { toStr = ''; }\n"
            + "    if (toStr === null && e instanceof TypeError) { toStr = ''; }\n"
            + "    if (toStr === null && e instanceof URIError) { toStr = ''; }\n"
            + "    if (toStr === null && e instanceof AggregateError) { toStr = '/AggregateError'; }\n"
            + "    if (toStr === null && typeof InternalError == 'function' "
                          + "&& e instanceof InternalError) { toStr = '/InternalError'; }\n"
            + "    if (toStr === null) {\n"
            + "      let rx = /\\[object (.*)\\]/;\n"
            + "      toStr = Object.prototype.toString.call(e);\n"
            + "      let match = rx.exec(toStr);\n"
            + "      if (match != null) { toStr = '/' + match[1]; }\n"
            + "    }"
            + "    log(e.name + toStr);\n"
            + "  }\n";

    /**
     * Function used in many tests.
     */
    public static final String LOG_TITLE_FUNCTION =
            "  function log(msg) { window.document.title += msg + '\\u00a7'; }\n"
            + LOG_EX_FUNCTION;

    /**
     * Function used in many tests.
     */
    public static final String LOG_TITLE_FUNCTION_NORMALIZE =
            "  function log(msg) { "
                    + "msg = '' + msg; "
                    + "msg = msg.replace(/ /g, '\\\\s'); "
                    + "msg = msg.replace(/\\n/g, '\\\\n'); "
                    + "msg = msg.replace(/\\r/g, '\\\\r'); "
                    + "msg = msg.replace(/\\t/g, '\\\\t'); "
                    + "msg = msg.replace(/\\u00a0/g, '\\\\u00a0'); "
                    + "msg = msg.replace(/\\u001e/g, '\\\\u001e'); "
                    + "window.document.title += msg + '\u00A7';}\n"

                    + LOG_EX_FUNCTION;

    /**
     * Function used in many tests.
     */
    public static final String LOG_WINDOW_NAME_FUNCTION =
            "  function log(msg) { window.top.name += msg + '\\u00a7'; }\n"
            + "  window.top.name = '';"
            + LOG_EX_FUNCTION;

    /**
     * Function used in many tests.
     */
    public static final String LOG_SESSION_STORAGE_FUNCTION =
            "  function log(msg) { "
            + "var l = sessionStorage.getItem('Log');"
            + "sessionStorage.setItem('Log', (null === l?'':l) + msg + '\\u00a7'); }\n";

    /**
     * Function used in many tests.
     */
    public static final String LOG_TEXTAREA_FUNCTION = "  function log(msg) { "
            + "document.getElementById('myLog').value += msg + '\u00A7';}\n"
            + LOG_EX_FUNCTION;

    /**
     * HtmlSniped to insert text area used for logging.
     */
    public static final String LOG_TEXTAREA = "  <textarea id='myLog' cols='80' rows='22'></textarea>\n";

    /**
     * The system property for automatically fixing the test case expectations.
     */
    public static final String AUTOFIX_ = "htmlunit.autofix";

    /**
     * All browsers supported.
     */
    private static final List<BrowserVersion> ALL_BROWSERS_ = List.of(BrowserVersion.CHROME, BrowserVersion.EDGE, BrowserVersion.FIREFOX, BrowserVersion.FIREFOX_ESR);

    /**
     * Browsers which run by default.
     */
    private static final BrowserVersion[] DEFAULT_RUNNING_BROWSERS_ =
        {BrowserVersion.CHROME,
            BrowserVersion.EDGE,
            BrowserVersion.FIREFOX,
            BrowserVersion.FIREFOX_ESR};

    private static final Log LOG = LogFactory.getLog(WebDriverTestCase.class);

    private static Set<String> BROWSERS_PROPERTIES_;
    private static String CHROME_BIN_;
    private static String EDGE_BIN_;
    private static String GECKO_BIN_;
    private static String FF_BIN_;
    private static String FF_ESR_BIN_;

    /** The driver cache. */
    protected static final Map<BrowserVersion, WebDriver> WEB_DRIVERS_ = new HashMap<>();

    /**
     * The key of the driver cache for real browsers; the classic and the BiDi variant of a browser
     * and the drivers configured for downloads need their own driver.
     */
    protected record RealBrowserKey(BrowserVersion browserVersion, boolean bidi, boolean downloads) {
    }

    /** The download directories of the real browsers (only used by tests that {@link #needDownloads()}). */
    private static final Map<RealBrowserKey, File> DOWNLOAD_DIRS_ = new HashMap<>();

    /** The driver cache for real browsers. */
    protected static final Map<RealBrowserKey, WebDriver> WEB_DRIVERS_REAL_BROWSERS = new HashMap<>();
    private static final Map<RealBrowserKey, Integer> WEB_DRIVERS_REAL_BROWSERS_USAGE_COUNT = new HashMap<>();

    private static Server STATIC_SERVER_;
    private static String STATIC_SERVER_STARTER_; // stack trace to save the server start code location
    // second server for cross-origin tests.
    private static Server STATIC_SERVER2_;
    private static String STATIC_SERVER2_STARTER_; // stack trace to save the server start code location
    // third server for multi-origin cross-origin tests.
    private static Server STATIC_SERVER3_;
    private static String STATIC_SERVER3_STARTER_; // stack trace to save the server start code location

    private static Boolean LAST_TEST_UsesMockWebConnection_;
    private static final Executor EXECUTOR_POOL = Executors.newFixedThreadPool(4);

    private boolean useRealBrowser_;
    private boolean useBiDi_;

    /**
     * The HtmlUnitDriver.
     */
    private HtmlUnitDriver webDriver_;

    /**
     * Override this function in a test class to ask for STATIC_SERVER2_ to be set up.
     * @return true if two servers are needed.
     */
    protected boolean needThreeConnections() {
        return false;
    }

    /**
     * Override this function in a test class to run the test with a driver that is able to
     * handle downloads (attachments).
     * <ul>
     *   <li>Real browsers are started with a download directory and without any download prompt;
     *   such a browser is cached separately, so the other tests are not affected.</li>
     *   <li>HtmlUnit gets a {@link DownloadingAttachmentHandler} that stores the files in a download
     *   directory, so HtmlUnit and the real browsers are checked the same way.</li>
     * </ul>
     * Use {@link #waitForDownloads(int, Duration)} to get the names of the downloaded files.
     * @return true if the test class needs download support
     */
    protected boolean needDownloads() {
        return false;
    }

    /**
     * @return the download directory of the browser (HtmlUnit or real browser) or {@code null}
     *         if {@link #needDownloads()} is {@code false}
     */
    protected final File getDownloadDirectory() {
        if (!needDownloads()) {
            return null;
        }
        synchronized (WEB_DRIVERS_REAL_BROWSERS) {
            return DOWNLOAD_DIRS_.get(realBrowserKey());
        }
    }

    /**
     * Waits for the downloads of the test and returns their file names (sorted).
     * Unfinished downloads of the real browsers (like {@code *.crdownload}) are ignored.
     * Returns what is available after the max wait time if there are fewer than expected downloads;
     * assert the result.
     *
     * @param expectedCount the number of expected downloads
     * @param maxWaitTime the maximum time to wait
     * @return the sorted file names
     * @throws InterruptedException in case of an interrupt
     */
    protected final List<String> waitForDownloads(final int expectedCount, final Duration maxWaitTime)
            throws InterruptedException {
        final long maxWait = System.currentTimeMillis() + maxWaitTime.toMillis();

        List<String> names = currentDownloads();
        while (names.size() < expectedCount && System.currentTimeMillis() < maxWait) {
            Thread.sleep(10);
            names = currentDownloads();
        }
        return names;
    }

    private List<String> currentDownloads() {
        final List<String> names = new ArrayList<>();
        final File dir = getDownloadDirectory();
        final File[] files = dir == null ? null : dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                final String name = file.getName();
                // unfinished downloads
                if (file.isFile()
                        && !name.endsWith(".crdownload") && !name.endsWith(".part")
                        && !name.endsWith(".tmp") && !name.endsWith(".download")) {
                    names.add(name);
                }
            }
        }
        Collections.sort(names);
        return names;
    }

    private void clearDownloads() {
        final File dir = getDownloadDirectory();
        if (dir != null) {
            final File[] files = dir.listFiles();
            if (files != null) {
                for (final File file : files) {
                    FileUtils.deleteQuietly(file);
                }
            }
        }
    }

    private File createDownloadDirectory() throws IOException {
        synchronized (WEB_DRIVERS_REAL_BROWSERS) {
            File dir = DOWNLOAD_DIRS_.get(realBrowserKey());
            if (dir == null) {
                dir = Files.createTempDirectory("htmlunit-downloads-").toFile();
                DOWNLOAD_DIRS_.put(realBrowserKey(), dir);
            }
            return dir;
        }
    }

    private void addChromeDownloadPreferences(final Map<String, Object> prefs) throws IOException {
        if (needDownloads()) {
            prefs.put("download.default_directory", createDownloadDirectory().getAbsolutePath());
            prefs.put("download.prompt_for_download", false);
            prefs.put("download.directory_upgrade", true);
        }
    }

    /**
     * @return the browser properties (and initializes them lazy)
     */
    public static Set<String> getBrowsersProperties() {
        if (BROWSERS_PROPERTIES_ == null) {
            try {
                final Properties properties = new Properties();
                final File file = new File("test.properties");
                if (file.exists()) {
                    try (FileInputStream in = new FileInputStream(file)) {
                        properties.load(in);
                    }

                    String browsersValue = properties.getProperty("browsers");
                    if (browsersValue == null || browsersValue.isEmpty()) {
                        browsersValue = "hu";
                    }
                    BROWSERS_PROPERTIES_ = new HashSet<>(Arrays.asList(browsersValue.replaceAll(" ", "")
                            .toLowerCase(Locale.ROOT).split(",")));
                    CHROME_BIN_ = properties.getProperty("chrome.bin");
                    EDGE_BIN_ = properties.getProperty("edge.bin");

                    GECKO_BIN_ = properties.getProperty("geckodriver.bin");
                    FF_BIN_ = properties.getProperty("ff.bin");
                    FF_ESR_BIN_ = properties.getProperty("ff-esr.bin");

                    final boolean autofix = Boolean.parseBoolean(properties.getProperty("autofix"));
                    System.setProperty(AUTOFIX_, Boolean.toString(autofix));
                }
            }
            catch (final Exception e) {
                LOG.error("Error reading htmlunit.properties. Ignoring!", e);
            }
            if (BROWSERS_PROPERTIES_ == null) {
                BROWSERS_PROPERTIES_ = new HashSet<>(Arrays.asList("hu"));
            }
            if (BROWSERS_PROPERTIES_.contains("hu")) {
                for (final BrowserVersion browserVersion : DEFAULT_RUNNING_BROWSERS_) {
                    BROWSERS_PROPERTIES_.add("hu-" + browserVersion.getNickname().toLowerCase());
                }
            }
        }
        return BROWSERS_PROPERTIES_;
    }

    /**
     * @return the list of supported browsers
     */
    public static List<BrowserVersion> allBrowsers() {
        return ALL_BROWSERS_;
    }

    /**
     * Configure the driver only once.
     * @return the driver
     */
    protected WebDriver getWebDriver() {
        final BrowserVersion browserVersion = getBrowserVersion();
        final RealBrowserKey realBrowserKey = realBrowserKey();
        WebDriver driver;
        if (useRealBrowser()) {
            synchronized (WEB_DRIVERS_REAL_BROWSERS) {
                driver = WEB_DRIVERS_REAL_BROWSERS.get(realBrowserKey);
                if (driver != null) {
                    // there seems to be a memory leak at least with FF;
                    // we have to restart sometimes
                    Integer count = WEB_DRIVERS_REAL_BROWSERS_USAGE_COUNT.get(realBrowserKey);
                    if (null == count) {
                        count = -1;
                    }
                    count += 1;
                    if (count >= 1000) {
                        shutDownReal(realBrowserKey);
                        driver = null;
                    }
                    else {
                        WEB_DRIVERS_REAL_BROWSERS_USAGE_COUNT.put(realBrowserKey, count);
                    }
                }

                if (driver == null) {
                    try {
                        driver = buildWebDriver();
                    }
                    catch (final IOException e) {
                        throw new RuntimeException(e);
                    }

                    WEB_DRIVERS_REAL_BROWSERS.put(realBrowserKey, driver);
                    WEB_DRIVERS_REAL_BROWSERS_USAGE_COUNT.put(realBrowserKey, 0);
                }
            }
        }
        else {
            driver = WEB_DRIVERS_.get(browserVersion);
            if (driver == null) {
                try {
                    driver = buildWebDriver();
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }

                if (isWebClientCached()) {
                    WEB_DRIVERS_.put(browserVersion, driver);
                }
            }
        }
        return driver;
    }

    /**
     * Closes the drivers.
     * @throws Exception If an error occurs
     */
    @AfterAll
    public static void shutDownAll() throws Exception {
        for (final WebDriver driver : WEB_DRIVERS_.values()) {
            driver.quit();
        }
        WEB_DRIVERS_.clear();

        shutDownRealBrowsers();

        synchronized (WEB_DRIVERS_REAL_BROWSERS) {
            for (final File dir : DOWNLOAD_DIRS_.values()) {
                FileUtils.deleteQuietly(dir);
            }
            DOWNLOAD_DIRS_.clear();
        }

        stopWebServers();
    }

    /**
     * Closes the real browser drivers.
     */
    private static void shutDownRealBrowsers() {
        synchronized (WEB_DRIVERS_REAL_BROWSERS) {
            for (final WebDriver driver : WEB_DRIVERS_REAL_BROWSERS.values()) {
                quit(driver);
            }
            WEB_DRIVERS_REAL_BROWSERS.clear();
            WEB_DRIVERS_REAL_BROWSERS_USAGE_COUNT.clear();
        }
    }

    /**
     * Closes the real browser driver.
     * @param key the real browser to close
     */
    private static void shutDownReal(final RealBrowserKey key) {
        synchronized (WEB_DRIVERS_REAL_BROWSERS) {
            final WebDriver driver = WEB_DRIVERS_REAL_BROWSERS.get(key);
            if (driver != null) {
                quit(driver);
                WEB_DRIVERS_REAL_BROWSERS.remove(key);
                WEB_DRIVERS_REAL_BROWSERS_USAGE_COUNT.remove(key);
            }
        }
    }

    private static void quit(final WebDriver driver) {
        if (driver != null) {
            try {
                driver.quit();
            }
            catch (final UnreachableBrowserException e) {
                LOG.error("Can't quit browser", e);
                // ignore, the browser is gone
            }
            catch (final NoClassDefFoundError e) {
                LOG.error("Can't quit browser", e);
                // ignore, the browser is gone
            }
            catch (final UnsatisfiedLinkError e) {
                LOG.error("Can't quit browser", e);
                // ignore, the browser is gone
            }
        }
    }

    /**
     * Asserts all static servers are null.
     * @throws Exception if it fails
     */
    protected static void assertWebServersStopped() throws Exception {
        Assertions.assertNull(STATIC_SERVER_, STATIC_SERVER_STARTER_);
        Assertions.assertNull(STATIC_SERVER2_, STATIC_SERVER2_STARTER_);
        Assertions.assertNull(STATIC_SERVER3_, STATIC_SERVER3_STARTER_);
    }

    /**
     * Stops all WebServers.
     * @throws Exception if it fails
     */
    protected static void stopWebServers() throws Exception {
        JettyServerUtils.stopServer(STATIC_SERVER_);
        STATIC_SERVER_ = null;

        JettyServerUtils.stopServer(STATIC_SERVER2_);
        STATIC_SERVER2_ = null;

        JettyServerUtils.stopServer(STATIC_SERVER3_);
        STATIC_SERVER3_ = null;

        LAST_TEST_UsesMockWebConnection_ = null;
    }

    /**
     * @return whether to use real browser or not.
     */
    public boolean useRealBrowser() {
        return useRealBrowser_;
    }

    /**
     * Sets whether to use real browser or not.
     * @param useRealBrowser whether to use real browser or not
     */
    public void setUseRealBrowser(final boolean useRealBrowser) {
        useRealBrowser_ = useRealBrowser;
    }

    /**
     * @return whether the real browser is started with BiDi enabled or not.
     */
    public boolean useBiDi() {
        return useBiDi_;
    }

    /**
     * Sets whether the real browser is started with BiDi enabled or not.
     * @param useBiDi whether to enable BiDi or not
     */
    public void setUseBiDi(final boolean useBiDi) {
        useBiDi_ = useBiDi;
    }

    private RealBrowserKey realBrowserKey() {
        return new RealBrowserKey(getBrowserVersion(), useBiDi(), needDownloads());
    }

    /**
     * When using a real browser seep for some timem.
     *
     * @param  millis the length of time to sleep in milliseconds
     * @throws InterruptedException in case of an interrupt
     */
    public void sleepRealBrowser(final long millis) throws InterruptedException {
        if (useRealBrowser()) {
            Thread.sleep(millis);
        }
    }

    /**
     * Builds a new WebDriver instance.
     * @return the instance
     * @throws IOException in case of exception
     */
    protected WebDriver buildWebDriver() throws IOException {
        if (useRealBrowser()) {
            if (BrowserVersion.EDGE.isSameBrowser(getBrowserVersion())) {
                final EdgeDriverService service = new EdgeDriverService.Builder()
                        .withLogOutput(System.out)
                        .usingDriverExecutable(new File(EDGE_BIN_))

                        .withAppendLog(true)
                        .withReadableTimestamp(true)

                        .build();

                final String locale = getBrowserVersion().getBrowserLocale().toLanguageTag();

                final EdgeOptions options = new EdgeOptions();
                if (useBiDi()) {
                    options.setCapability("webSocketUrl", true);
                }

                options.addArguments("--lang=" + locale);
                // https://stackoverflow.com/questions/11289597/webdriver-how-to-specify-preferred-languages-for-chrome
                final Map<String, Object> prefs = new HashMap<>();
                prefs.put("intl.accept_languages", locale);
                addChromeDownloadPreferences(prefs);
                options.setExperimentalOption("prefs", prefs);
                options.addArguments("--remote-allow-origins=*");

                // seems to be not required for edge
                // options.addArguments("--disable-search-engine-choice-screen");
                // see https://www.selenium.dev/blog/2024/chrome-browser-woes/
                // options.addArguments("--disable-features=OptimizationGuideModelDownloading,"
                //         + "OptimizationHintsFetching,OptimizationTargetPrediction,OptimizationHints");

                final EdgeDriver edge = new EdgeDriver(service, options);

                final DevTools devTools = edge.getDevTools();
                devTools.createSession();

                final String tz = getBrowserVersion().getSystemTimezone().getID();
                devTools.send(Emulation.setTimezoneOverride(tz));

                return edge;
            }

            if (BrowserVersion.CHROME.isSameBrowser(getBrowserVersion())) {
                final ChromeDriverService service = new ChromeDriverService.Builder()
                        .withLogOutput(System.out)
                        .usingDriverExecutable(new File(CHROME_BIN_))

                        .withAppendLog(true)
                        .withReadableTimestamp(true)

                        .build();

                final String locale = getBrowserVersion().getBrowserLocale().toLanguageTag();

                final ChromeOptions options = new ChromeOptions();
                if (useBiDi()) {
                    options.setCapability("webSocketUrl", true);
                }

                options.addArguments("--lang=" + locale);
                // https://stackoverflow.com/questions/11289597/webdriver-how-to-specify-preferred-languages-for-chrome
                final Map<String, Object> prefs = new HashMap<>();
                prefs.put("intl.accept_languages", locale);
                addChromeDownloadPreferences(prefs);
                options.setExperimentalOption("prefs", prefs);
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--disable-search-engine-choice-screen");
                // see https://www.selenium.dev/blog/2024/chrome-browser-woes/
                options.addArguments("--disable-features=OptimizationGuideModelDownloading,"
                        + "OptimizationHintsFetching,OptimizationTargetPrediction,OptimizationHints");

                final ChromeDriver chrome = new ChromeDriver(service, options);

                final DevTools devTools = chrome.getDevTools();
                devTools.createSession();

                final String tz = getBrowserVersion().getSystemTimezone().getID();
                devTools.send(Emulation.setTimezoneOverride(tz));

                return chrome;
            }

            if (BrowserVersion.FIREFOX.isSameBrowser(getBrowserVersion())) {
                return createFirefoxDriver(GECKO_BIN_, FF_BIN_, useBiDi());
            }

            if (BrowserVersion.FIREFOX_ESR.isSameBrowser(getBrowserVersion())) {
                return createFirefoxDriver(GECKO_BIN_, FF_ESR_BIN_, useBiDi());
            }

            throw new RuntimeException("Unexpected BrowserVersion: " + getBrowserVersion());
        }

        if (webDriver_ == null) {
            final HtmlUnitDriverOptions driverOptions = new HtmlUnitDriverOptions(getBrowserVersion());

            if (isWebClientCached()) {
                driverOptions.setCapability(HtmlUnitOption.optHistorySizeLimit, 0);
            }

            if (getWebClientTimeout() != null) {
                driverOptions.setCapability(HtmlUnitOption.optTimeout, getWebClientTimeout());
            }
            webDriver_ = new HtmlUnitDriver(driverOptions);
            webDriver_.setExecutor(EXECUTOR_POOL);

            if (needDownloads()) {
                try {
                    webDriver_.getWebClient().setAttachmentHandler(
                            new DownloadingAttachmentHandler(createDownloadDirectory().toPath()));
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return webDriver_;
    }

    private FirefoxDriver createFirefoxDriver(final String geckodriverBinary, final String binary,
            final boolean bidi) throws IOException {
        final FirefoxDriverService service = new GeckoDriverService.Builder()
                .withLogOutput(System.out)
                .usingDriverExecutable(new File(geckodriverBinary))
                .build();

        final FirefoxOptions options = new FirefoxOptions();
        options.addPreference("browser.shell.checkDefaultBrowser", false);
        options.addPreference("browser.startup.homepage_welcomeurl", "about:blank");
        options.addPreference("startup.homepage_welcome_url", "about:blank");
        options.addPreference("startup.homepage_override_url", "about:blank");
        options.addPreference("browser.aboutwelcome.enabled", false);

        if (bidi) {
            options.setCapability("webSocketUrl", true);
        }

        options.setBinary(binary);

        String locale = getBrowserVersion().getBrowserLocale().toLanguageTag();
        locale = locale + "," + getBrowserVersion().getBrowserLocale().getLanguage();

        final FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("intl.accept_languages", locale);
        if (needDownloads()) {
            profile.setPreference("browser.download.folderList", 2);
            profile.setPreference("browser.download.dir", createDownloadDirectory().getAbsolutePath());
            profile.setPreference("browser.download.useDownloadDir", true);
            profile.setPreference("browser.download.manager.showWhenStarting", false);
            profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                    "application/octet-stream,application/pdf,application/zip,text/plain,text/html,text/csv");
            profile.setPreference("pdfjs.disabled", true);
        }
        // no idea so far how to set this
        // final String tz = getBrowserVersion().getSystemTimezone().getID();
        // profile.setPreference("intl.tz", tz);
        options.setProfile(profile);

        return new FirefoxDriver(service, options);
    }

    /**
     * Starts the web server delivering response from the provided connection.
     * @param mockConnection the sources for responses
     * @param serverCharset the {@link Charset} at the server side
     * @throws Exception if a problem occurs
     */
    protected void startWebServer(final MockWebConnection mockConnection, final Charset serverCharset)
            throws Exception {
        if (Boolean.FALSE.equals(LAST_TEST_UsesMockWebConnection_)) {
            stopWebServers();
        }

        // The mock connection servlet call site under both servers, so long as tests
        // keep the URLs distinct.
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);

        LAST_TEST_UsesMockWebConnection_ = Boolean.TRUE;

        if (STATIC_SERVER_ == null) {

            final Server server = JettyServerUtils.startWebServer(PORT, "./", servlets, serverCharset, isBasicAuthentication(), SSLVariant.NONE);
            STATIC_SERVER_STARTER_ = ExceptionUtils.getStackTrace(new Throwable("StaticServerStarter"));
            STATIC_SERVER_ = server;
        }
        MockWebConnectionServlet.MockConnection_ = mockConnection;

        if (STATIC_SERVER2_ == null && needThreeConnections()) {
            final Server server2 = JettyServerUtils.startWebServer(PORT2, "./", servlets, null, false, SSLVariant.NONE);
            STATIC_SERVER2_STARTER_ = ExceptionUtils.getStackTrace(new Throwable("StaticServer2Starter"));
            STATIC_SERVER2_ = server2;

            final Server server3 = JettyServerUtils.startWebServer(PORT3, "./", servlets, null, false, SSLVariant.NONE);
            STATIC_SERVER3_STARTER_ = ExceptionUtils.getStackTrace(new Throwable("StaticServer3Starter"));
            STATIC_SERVER3_ = server3;
        }
    }

    /**
     * Returns whether to use basic authentication for all resources or not.
     * The default implementation returns false.
     * @return whether to use basic authentication or not
     */
    protected boolean isBasicAuthentication() {
        return false;
    }

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @param servlets map of {String, Class} pairs: String is the path spec, while class is the class
     * @throws Exception if the test fails
     */
    protected static void startWebServer(final String resourceBase, final Map<String, Class<? extends Servlet>> servlets) throws Exception {
        stopWebServers();
        LAST_TEST_UsesMockWebConnection_ = Boolean.FALSE;

        STATIC_SERVER_STARTER_ = ExceptionUtils.getStackTrace(new Throwable("StaticServerStarter"));
        STATIC_SERVER_ = JettyServerUtils.startWebServer(PORT, resourceBase, servlets, null, false, SSLVariant.NONE);
    }

    /**
     * Starts the <b>second</b> web server on the default {@link #PORT2}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @param servlets map of {String, Class} pairs: String is the path spec, while class is the class
     * @throws Exception if the test fails
     */
    protected static void startWebServer2(final String resourceBase, final Map<String, Class<? extends Servlet>> servlets) throws Exception {
        if (STATIC_SERVER2_ != null) {
            JettyServerUtils.stopServer(STATIC_SERVER2_);
        }
        STATIC_SERVER2_STARTER_ = ExceptionUtils.getStackTrace(new Throwable("StaticServer2Starter"));
        STATIC_SERVER2_ = JettyServerUtils.startWebServer(PORT2, resourceBase, servlets, null, false, SSLVariant.NONE);
    }

    /**
     * Servlet delivering content from a MockWebConnection.
     */
    public static class MockWebConnectionServlet extends HttpServlet {
        private static MockWebConnection MockConnection_;

        public static void setMockconnection(final MockWebConnection connection) {
            MockConnection_ = connection;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void service(final HttpServletRequest request, final HttpServletResponse response)
                throws ServletException, IOException {

            try {
                doService(request, response);
            }
            catch (final ServletException e) {
                throw e;
            }
            catch (final IOException e) {
                throw e;
            }
            catch (final Exception e) {
                throw new ServletException(e);
            }
        }

        private static void doService(final HttpServletRequest request, final HttpServletResponse response)
                throws Exception {
            String url = request.getRequestURL().toString();
            if (LOG.isDebugEnabled()) {
                LOG.debug(request.getMethod() + " " + url);
            }

            if (url.endsWith("/favicon.ico")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (url.contains("/delay")) {
                final String delay = StringUtils.substringBetween(url, "/delay", "/");
                final int ms = Integer.parseInt(delay);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Sleeping for " + ms + " before to deliver " + url);
                }
                Thread.sleep(ms);
            }

            // copy parameters
            final List<NameValuePair> requestParameters = new ArrayList<>();
            try {
                for (final Enumeration<String> paramNames = request.getParameterNames();
                        paramNames.hasMoreElements();) {
                    final String name = paramNames.nextElement();
                    final String[] values = request.getParameterValues(name);
                    for (final String value : values) {
                        requestParameters.add(new NameValuePair(name, value));
                    }
                }
            }
            catch (final IllegalArgumentException e) {
                // Jetty 8.1.7 throws it in getParameterNames for a query like "cb=%%RANDOM_NUMBER%%"
                // => we should use a more low level test server
                requestParameters.clear();
                final String query = request.getQueryString();
                if (query != null) {
                    url += "?" + query;
                }
            }

            final String queryString = request.getQueryString();
            if (StringUtils.isNotBlank(queryString)) {
                url = url + "?" + queryString;
            }
            final URL requestedUrl = new URL(url);
            final WebRequest webRequest = new WebRequest(requestedUrl);

            final String method = request.getMethod().toUpperCase(Locale.ROOT);
            webRequest.setHttpMethod(HttpMethod.valueOf(method));

            // copy headers
            for (final Enumeration<String> en = request.getHeaderNames(); en.hasMoreElements();) {
                final String headerName = en.nextElement();
                final String headerValue = request.getHeader(headerName);
                webRequest.setAdditionalHeader(headerName, headerValue);
            }

            if (requestParameters.isEmpty() && request.getContentLength() > 0) {
                final byte[] buffer = new byte[request.getContentLength()];
                IOUtils.read(request.getInputStream(), buffer, 0, buffer.length);

                final String encoding = request.getCharacterEncoding();
                if (encoding == null) {
                    webRequest.setRequestBody(new String(buffer, ISO_8859_1));
                    webRequest.setCharset(null);
                }
                else {
                    webRequest.setRequestBody(new String(buffer, encoding));
                    webRequest.setCharset(Charset.forName(encoding));
                }
            }
            else {
                webRequest.setRequestParameters(requestParameters);
            }

            // check content type if it is multipart enctype
            if (request.getContentType() != null
                        && request.getContentType().startsWith(FormEncodingType.MULTIPART.getName())) {
                webRequest.setEncodingType(FormEncodingType.MULTIPART);
            }

            final RawResponseData resp = MockConnection_.getRawResponse(webRequest);

            // write WebResponse to HttpServletResponse
            response.setStatus(resp.getStatusCode());

            boolean charsetInContentType = false;
            for (final NameValuePair responseHeader : resp.getHeaders()) {
                final String headerName = responseHeader.getName();
                if (HttpHeader.CONTENT_TYPE.equals(headerName) && responseHeader.getValue().contains("charset=")) {
                    charsetInContentType = true;
                }
                response.addHeader(headerName, responseHeader.getValue());
            }

            if (resp.getByteContent() != null) {
                response.getOutputStream().write(resp.getByteContent());
            }
            else {
                if (!charsetInContentType) {
                    response.setCharacterEncoding(resp.getCharset().name());
                }
                response.getWriter().print(resp.getStringContent());
            }
            response.flushBuffer();
        }
    }

    /**
     * Same as {@link #loadPageWithAlerts2(String)}... but doesn't verify the alerts.
     * @param html the HTML to use
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final String html) throws Exception {
        return loadPage2(html, URL_FIRST);
    }

    /**
     * Same as {@link #loadPageWithAlerts2(String)}... but doesn't verify the alerts.
     * @param html the HTML to use
     * @param url the url to use to load the page
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final String html, final URL url) throws Exception {
        return loadPage2(html, url, "text/html;charset=ISO-8859-1", ISO_8859_1, null);
    }

    /**
     * Same as {@link #loadPageWithAlerts2(String)}... but doesn't verify the alerts.
     * @param html the HTML to use
     * @param url the url to use to load the page
     * @param contentType the content type to return
     * @param charset the charset
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final String html, final URL url,
            final String contentType, final Charset charset) throws Exception {
        return loadPage2(html, url, contentType, charset, null);
    }

    /**
     * Same as {@link #loadPageWithAlerts2(String)}... but doesn't verify the alerts.
     * @param html the HTML to use
     * @param url the url to use to load the page
     * @param contentType the content type to return
     * @param charset the charset
     * @param serverCharset the charset at the server side.
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final String html, final URL url,
            final String contentType, final Charset charset, final Charset serverCharset) throws Exception {
        getMockWebConnection().setResponse(url, html, contentType, charset);
        return loadPage2(url, serverCharset);
    }

    /**
     * Load the page from the url.
     * @param url the url to use to load the page
     * @param serverCharset the charset at the server side.
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final URL url, final Charset serverCharset) throws Exception {
        startWebServer(getMockWebConnection(), serverCharset);

        WebDriver driver = getWebDriver();
        if (!(driver instanceof HtmlUnitDriver)) {
            try {
                resizeIfNeeded(driver);
            }
            catch (final NoSuchSessionException e) {
                // maybe the driver was killed by the test before; setup a new one
                shutDownRealBrowsers();

                driver = getWebDriver();
                resizeIfNeeded(driver);
            }
        }
        driver.get(url.toExternalForm());

        return driver;
    }

    /**
     * Same as {@link #loadPage2(String)} with additional servlet configuration.
     * @param html the HTML to use for the default response
     * @param servlets the additional servlets to configure with their mapping
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final String html,
            final Map<String, Class<? extends Servlet>> servlets) throws Exception {
        return loadPage2(html, URL_FIRST, servlets, null);
    }

    /**
     * Same as {@link #loadPage2(String, URL)}, but with additional servlet configuration.
     * @param html the HTML to use for the default page
     * @param url the URL to use to load the page
     * @param servlets the additional servlets to configure with their mapping
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final String html, final URL url,
            final Map<String, Class<? extends Servlet>> servlets) throws Exception {
        return loadPage2(html, url, servlets, null);
    }

    /**
     * Same as {@link #loadPage2(String, URL)}, but with additional servlet configuration.
     * @param html the HTML to use for the default page
     * @param url the URL to use to load the page
     * @param servlets the additional servlets to configure with their mapping
     * @param servlets2 the additional servlets to configure with their mapping for a second server
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPage2(final String html, final URL url,
            final Map<String, Class<? extends Servlet>> servlets,
            final Map<String, Class<? extends Servlet>> servlets2) throws Exception {
        servlets.put("/*", MockWebConnectionServlet.class);
        getMockWebConnection().setResponse(url, html);
        MockWebConnectionServlet.MockConnection_ = getMockWebConnection();

        startWebServer("./", servlets);
        if (servlets2 != null) {
            startWebServer2("./", servlets2);
        }

        WebDriver driver = getWebDriver();
        if (!(driver instanceof HtmlUnitDriver)) {
            try {
                resizeIfNeeded(driver);
            }
            catch (final NoSuchSessionException e) {
                // maybe the driver was killed by the test before; setup a new one
                shutDownRealBrowsers();

                driver = getWebDriver();
                resizeIfNeeded(driver);
            }
        }
        driver.get(url.toExternalForm());

        return driver;
    }

    protected void resizeIfNeeded(final WebDriver driver) {
        final Dimension size = driver.manage().window().getSize();
        if (size.getWidth() != 1272 || size.getHeight() != 768) {
            // only resize if needed because it may be quite expensive
            driver.manage().window().setSize(new Dimension(1272, 768));
        }
    }

    /**
     * Defines the provided HTML as the response for {@link WebTestCase#URL_FIRST}
     * and loads the page with this URL using the current WebDriver version; finally, asserts that the
     * alerts equal the expected alerts.
     * @param html the HTML to use
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html) throws Exception {
        return loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * Defines the provided HTML as the response for {@link WebTestCase#URL_FIRST}
     * and loads the page with this URL using the current WebDriver version; finally, asserts that the
     * alerts equal the expected alerts.
     * @param html the HTML to use
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageVerifyTitle2(final String html) throws Exception {
        return loadPageVerifyTitle2(html, getExpectedAlerts());
    }

    protected final WebDriver loadPageVerifyTitle2(final String html, final String... expectedAlerts) throws Exception {
        final WebDriver driver = loadPage2(html);
        return verifyTitle2(driver, expectedAlerts);
    }

    protected final WebDriver verifyTitle2(final Duration maxWaitTime, final WebDriver driver,
            final String... expectedAlerts) throws Exception {

        final StringBuilder expected = new StringBuilder();
        for (String expectedAlert : expectedAlerts) {
            expected.append(expectedAlert).append('\u00A7');
        }
        final String expectedTitle = expected.toString();

        final long maxWait = System.currentTimeMillis() + maxWaitTime.toMillis();

        while (System.currentTimeMillis() < maxWait) {
            try {
                final String title = driver.getTitle();
                assertEquals(expectedTitle, title);
                return driver;
            }
            catch (final AssertionError e) {
                // ignore and wait
            }
        }

        assertEquals(expectedTitle, driver.getTitle());
        return driver;
    }

    protected final WebDriver verifyTitle2(final WebDriver driver,
            final String... expectedAlerts) throws Exception {
        if (expectedAlerts.length == 0) {
            assertEquals("", driver.getTitle());
            return driver;
        }

        final StringBuilder expected = new StringBuilder();
        for (String expectedAlert : expectedAlerts) {
            expected.append(expectedAlert).append('\u00A7');
        }

        final String title = driver.getTitle();
        try {
            assertEquals(expected.toString(), title);
        }
        catch (final AssertionError e) {
            if (useRealBrowser() && StringUtils.isEmpty(title)) {
                Thread.sleep(42);
                assertEquals(expected.toString(), driver.getTitle());
                return driver;
            }
            throw e;
        }
        return driver;
    }

    protected final WebDriver loadPageVerifyTextArea2(final String html) throws Exception {
        return loadPageTextArea2(html, getExpectedAlerts());
    }

    protected final WebDriver loadPageTextArea2(final String html, final String... expectedAlerts) throws Exception {
        final WebDriver driver = loadPage2(html);
        return verifyTextArea2(driver, expectedAlerts);
    }

    protected final WebDriver verifyTextArea2(final WebDriver driver,
            final String... expectedAlerts) throws Exception {
        final WebElement textArea = driver.findElement(By.id("myLog"));
        String value = textArea.getDomProperty("value");

        if (expectedAlerts.length == 0) {
            assertEquals("", value);
            return driver;
        }

        final StringBuilder expected = new StringBuilder();
        for (String expectedAlert : expectedAlerts) {
            expected.append(expectedAlert).append('\u00A7');
        }

        if (expectedAlerts.length == 1 && expectedAlerts[0].startsWith("data:image/png;base64,")) {
            try {
                // fastpath
                assertEquals(expected.toString(), value);
            } catch (final AssertionFailedError e) {
                // starting with FF151 we have canvas fingerprinting protection
                // therefore we have to compare the pictures
                if (value.endsWith("\u00A7")) {
                    value = value.substring(0, value.length() - 1);
                }

                compareImages(expectedAlerts[0], value);
                return driver;
            }
        }

        verify(() -> textArea.getDomProperty("value"), expected.toString());
        return driver;
    }

    protected final String getJsVariableValue(final WebDriver driver, final String varName) throws Exception {
        final String script = "return String(" + varName + ")";
        final String result = (String) ((JavascriptExecutor) driver).executeScript(script);

        return result;
    }

    protected final WebDriver verifyJsVariable(final WebDriver driver, final String varName,
            final String expected) throws Exception {
        final String result = getJsVariableValue(driver, varName);
        assertEquals(expected, result);

        return driver;
    }

    protected final WebDriver verifyWindowName2(final Duration maxWaitTime, final WebDriver driver,
            final String... expectedAlerts) throws Exception {
        final long maxWait = System.currentTimeMillis() + maxWaitTime.toMillis();

        while (System.currentTimeMillis() < maxWait) {
            try {
                return verifyWindowName2(driver, expectedAlerts);
            }
            catch (final AssertionError e) {
                // ignore and wait
            }
        }

        return verifyWindowName2(driver, expectedAlerts);
    }

    protected final WebDriver verifyWindowName2(final WebDriver driver,
            final String... expectedAlerts) throws Exception {
        final StringBuilder expected = new StringBuilder();
        for (String expectedAlert : expectedAlerts) {
            expected.append(expectedAlert).append('\u00A7');
        }

        return verifyJsVariable(driver, "window.top.name", expected.toString());
    }

    protected final WebDriver verifySessionStorage2(final WebDriver driver,
            final String... expectedAlerts) throws Exception {
        final StringBuilder expected = new StringBuilder();
        for (String expectedAlert : expectedAlerts) {
            expected.append(expectedAlert).append('\u00A7');
        }

        return verifyJsVariable(driver, "sessionStorage.getItem('Log')", expected.toString());
    }

    /**
     * Defines the provided HTML as the response for {@link WebTestCase#URL_FIRST}
     * and loads the page with this URL using the current WebDriver version; finally, asserts that the
     * alerts equal the expected alerts.
     * @param html the HTML to use
     * @param maxWaitTime the maximum time to wait to get the alerts (in millis)
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html, final Duration maxWaitTime) throws Exception {
        return loadPageWithAlerts2(html, URL_FIRST, maxWaitTime);
    }

    /**
     * Same as {@link #loadPageWithAlerts2(String)}, but specifying the default URL.
     * @param html the HTML to use
     * @param url the URL to use to load the page
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html, final URL url) throws Exception {
        return loadPageWithAlerts2(html, url, DEFAULT_WAIT_TIME);
    }

    /**
     * Same as {@link #loadPageWithAlerts2(String, long)}, but specifying the default URL.
     * @param html the HTML to use
     * @param url the URL to use to load the page
     * @param maxWaitTime the maximum time to wait to get the alerts (in millis)
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html, final URL url, final Duration maxWaitTime)
            throws Exception {
        final WebDriver driver = loadPage2(html, url);

        verifyAlerts(maxWaitTime, driver, getExpectedAlerts());
        return driver;
    }

    /**
     * Verifies the captured alerts.
     * @param driver the driver instance
     * @param expectedAlerts the expected alerts
     * @throws Exception in case of failure
     */
    protected void verifyAlerts(final WebDriver driver, final String... expectedAlerts) throws Exception {
        verifyAlerts(DEFAULT_WAIT_TIME, driver, expectedAlerts);
    }

    /**
     * Verifies the captured alerts.
     *
     * @param maxWaitTime the maximum time to wait for the expected alert to be found
     * @param driver the driver instance
     * @param expected the expected alerts
     * @throws Exception in case of failure
     */
    protected void verifyAlerts(final Duration maxWaitTime, final WebDriver driver, final String... expected)
            throws Exception {
        final List<String> actualAlerts = getCollectedAlerts(maxWaitTime, driver, expected.length);

        assertEquals(expected.length, actualAlerts.size());

        if (!useRealBrowser()) {
            // check if we have data-image Url
            for (String s : expected) {
                if (s.startsWith("data:image/png;base64,")) {
                    // we have to compare element by element
                    for (int j = 0; j < expected.length; j++) {
                        if (expected[j].startsWith("data:image/png;base64,")) {
                            compareImages(expected[j], actualAlerts.get(j));
                        } else {
                            assertEquals(expected[j], actualAlerts.get(j));
                        }
                    }
                    return;
                }
            }
        }

        assertEquals(expected, actualAlerts);
    }

    /**
     * Same as {@link #loadPageWithAlerts2(String)} with additional servlet configuration.
     * @param html the HTML to use for the default response
     * @param servlets the additional servlets to configure with their mapping
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html,
            final Map<String, Class<? extends Servlet>> servlets) throws Exception {
        return loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME, servlets);
    }

    /**
     * Same as {@link #loadPageWithAlerts2(String, URL, long)}, but with additional servlet configuration.
     * @param html the HTML to use for the default page
     * @param url the URL to use to load the page
     * @param maxWaitTime the maximum time to wait to get the alerts (in millis)
     * @param servlets the additional servlets to configure with their mapping
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final String html, final URL url, final Duration maxWaitTime,
            final Map<String, Class<? extends Servlet>> servlets) throws Exception {

        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html, url, servlets);
        verifyAlerts(maxWaitTime, driver, getExpectedAlerts());

        return driver;
    }

    /**
     * Loads the provided URL serving responses from {@link #getMockWebConnection()}
     * and verifies that the captured alerts are correct.
     * @param url the URL to use to load the page
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final URL url) throws Exception {
        return loadPageWithAlerts2(url, DEFAULT_WAIT_TIME);
    }

    /**
     * Same as {@link #loadPageWithAlerts2(URL)}, but using with timeout.
     * @param url the URL to use to load the page
     * @param maxWaitTime the maximum time to wait to get the alerts (in millis)
     * @return the web driver
     * @throws Exception if something goes wrong
     */
    protected final WebDriver loadPageWithAlerts2(final URL url, final Duration maxWaitTime) throws Exception {
        startWebServer(getMockWebConnection(), null);

        final WebDriver driver = getWebDriver();
        driver.get(url.toExternalForm());

        verifyAlerts(maxWaitTime, driver, getExpectedAlerts());
        return driver;
    }

    /**
     * Gets the alerts collected by the driver.
     * Note: it currently works only if no new page has been loaded in the window
     * @param driver the driver
     * @return the collected alerts
     * @throws Exception in case of problem
     */
    protected List<String> getCollectedAlerts(final WebDriver driver) throws Exception {
        return getCollectedAlerts(driver, getExpectedAlerts().length);
    }

    /**
     * Gets the alerts collected by the driver.
     * Note: it currently works only if no new page has been loaded in the window
     * @param driver the driver
     * @param alertsLength the expected length of Alerts
     * @return the collected alerts
     * @throws Exception in case of problem
     */
    protected List<String> getCollectedAlerts(final WebDriver driver, final int alertsLength) throws Exception {
        return getCollectedAlerts(DEFAULT_WAIT_TIME, driver, alertsLength);
    }

    /**
     * Gets the alerts collected by the driver.
     * Note: it currently works only if no new page has been loaded in the window
     * @param maxWaitTime the maximum time to wait to get the alerts (in millis)
     * @param driver the driver
     * @param alertsLength the expected length of Alerts
     * @return the collected alerts
     * @throws Exception in case of problem
     */
    protected List<String> getCollectedAlerts(final Duration maxWaitTime,
            final WebDriver driver, final int alertsLength) throws Exception {
        final List<String> collectedAlerts = new ArrayList<>();

        long maxWait = System.currentTimeMillis() + maxWaitTime.toMillis();

        while (collectedAlerts.size() < alertsLength && System.currentTimeMillis() < maxWait) {
            try {
                final Alert alert = driver.switchTo().alert();
                final String text = alert.getText();

                collectedAlerts.add(text);
                alert.accept();

                // handling of alerts requires some time
                // at least for tests with many alerts we have to take this into account
                maxWait += 100;
            }
            catch (final NoAlertPresentException e) {
                Thread.sleep(10);
            }
        }

        return collectedAlerts;
    }

    /**
     * Returns the HtmlElement of the specified WebElement.
     * @param webElement the webElement
     * @return the HtmlElement
     * @throws Exception if an error occurs
     */
    protected HtmlElement toHtmlElement(final WebElement webElement) throws Exception {
        return (HtmlElement) ((HtmlUnitWebElement) webElement).getElement();
    }

    /**
     * Loads an expectation file for the specified browser search first for a browser specific resource
     * and falling back in a general resource.
     * @param resourcePrefix the start of the resource name
     * @param resourceSuffix the end of the resource name
     * @return the content of the file
     * @throws Exception in case of error
     */
    protected String loadExpectation(final String resourcePrefix, final String resourceSuffix) throws Exception {
        final Class<?> referenceClass = getClass();
        final BrowserVersion browserVersion = getBrowserVersion();

        String realBrowserNyiExpectation = null;
        String realNyiExpectation = null;
        final String browserExpectation;
        final String expectation;
        if (!useRealBrowser()) {
            // first try nyi
            final String browserSpecificNyiResource
                    = resourcePrefix + "." + browserVersion.getNickname() + "_NYI" + resourceSuffix;
            realBrowserNyiExpectation = loadContent(referenceClass.getResource(browserSpecificNyiResource));

            // next nyi without browser
            final String nyiResource = resourcePrefix + ".NYI" + resourceSuffix;
            realNyiExpectation = loadContent(referenceClass.getResource(nyiResource));
        }

        // implemented - browser specific
        final String browserSpecificResource = resourcePrefix + "." + browserVersion.getNickname() + resourceSuffix;
        browserExpectation = loadContent(referenceClass.getResource(browserSpecificResource));

        // implemented - all browsers
        final String resource = resourcePrefix + resourceSuffix;
        expectation = loadContent(referenceClass.getResource(resource));

        // check for duplicates
        if (realBrowserNyiExpectation != null) {
            if (realNyiExpectation != null) {
                Assertions.assertNotEquals(realBrowserNyiExpectation, realNyiExpectation,
                        "Duplicate NYI Expectation for Browser " + browserVersion.getNickname());
            }

            if (browserExpectation == null) {
                if (expectation != null) {
                    Assertions.assertNotEquals(realBrowserNyiExpectation, expectation,
                            "NYI Expectation matches the expected "
                                    + "result for Browser " + browserVersion.getNickname());
                }
            }
            else {
                Assertions.assertNotEquals(realBrowserNyiExpectation, browserExpectation,
                        "NYI Expectation matches the expected "
                                + "browser specific result for Browser "
                                + browserVersion.getNickname());
            }

            return realBrowserNyiExpectation;
        }

        if (realNyiExpectation != null) {
            if (browserExpectation == null) {
                if (expectation != null) {
                    Assertions.assertNotEquals(realNyiExpectation, expectation,
                            "NYI Expectation matches the expected "
                                    + "result for Browser " + browserVersion.getNickname());
                }
            }
            else {
                Assertions.assertNotEquals(realNyiExpectation, browserExpectation,
                            "NYI Expectation matches the expected "
                                    + "browser specific result for Browser "
                                    + browserVersion.getNickname());
            }
            return realNyiExpectation;
        }

        if (browserExpectation != null) {
            if (expectation != null) {
                Assertions.assertNotEquals(browserExpectation, expectation,
                            "Browser specific NYI Expectation matches the expected "
                                    + "result for Browser " + browserVersion.getNickname());
            }
            return browserExpectation;
        }
        return expectation;
    }

    private static String loadContent(final URL url) throws URISyntaxException, IOException {
        if (url == null) {
            return null;
        }

        final File file = new File(url.toURI());
        final String content = FileUtils.readFileToString(file, UTF_8)
                                        .replace("\r\n", "\n");
        return content;
    }

    /**
     * Reads the number of JS threads remaining from unit tests run before.
     * This should be always 0, if {@link #isWebClientCached()} is {@code false}.
     * @throws InterruptedException in case of problems
     */
    @BeforeEach
    public void beforeTest() throws InterruptedException {
        if (needDownloads()) {
            clearDownloads();
        }
        if (!isWebClientCached()) {
            if (!getJavaScriptThreads().isEmpty()) {
                Thread.sleep(200);
            }
            assertTrue("There are already JS threads running before the test", getJavaScriptThreads().isEmpty());
        }
    }

    /**
     * Asserts the current title is equal to the expectation string.
     * @param webdriver the driver in use
     * @param expected the expected object
     * @throws Exception in case of failure
     */
    protected void assertTitle(final WebDriver webdriver, final String expected) throws Exception {
        final long maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME.toMillis();

        while (true) {
            final String title = webdriver.getTitle();
            try {
                assertEquals(expected, title);
                return;
            }
            catch (final org.opentest4j.AssertionFailedError e) {
                if (expected.length() <= title.length()
                        || System.currentTimeMillis() > maxWait) {
                    throw e;
                }
                Thread.sleep(10);
            }
        }
    }

    /**
     * Release resources but DON'T close the browser if we are running with a real browser.
     * Note that HtmlUnitDriver is not cached by default, but that can be configured by {@link #isWebClientCached()}.
     */
    @AfterEach
    @Override
    public void releaseResources() {
        final List<String> unhandledAlerts = new ArrayList<>();
        if (webDriver_ != null) {
            UnhandledAlertException ex = null;
            do {
                ex = null;
                try {
                    // getTitle will do an implicit check for open alerts
                    webDriver_.getTitle();
                }
                catch (final NoSuchWindowException e) {
                    // ignore
                }
                catch (final UnhandledAlertException e) {
                    ex = e;
                    unhandledAlerts.add(e.getMessage());
                }
            }
            while (ex != null);
        }

        super.releaseResources();

        if (!isWebClientCached()) {
            boolean rhino = false;
            if (webDriver_ != null) {
                try {
                    rhino = getWebClient().getJavaScriptEngine() instanceof JavaScriptEngine;
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
                webDriver_.quit();
                webDriver_ = null;
            }
            if (rhino) {
                assertTrue("There are still JS threads running after the test", getJavaScriptThreads().isEmpty());
            }
        }

        if (useRealBrowser()) {
            synchronized (WEB_DRIVERS_REAL_BROWSERS) {
                final RealBrowserKey realBrowserKey = realBrowserKey();
                final WebDriver driver = WEB_DRIVERS_REAL_BROWSERS.get(realBrowserKey);
                if (driver != null) {
                    try {
                        final String currentWindow = driver.getWindowHandle();

                        final Set<String> handles = driver.getWindowHandles();
                        // close all windows except the current one
                        handles.remove(currentWindow);
                        if (needDownloads()) {
                            // some browsers (edge) open an extra window/tab for a download and
                            // need 20s to close it (or fail); leave it open, the browser is
                            // quit at the end anyway
                            handles.clear();
                        }

                        if (!handles.isEmpty()) {
                            for (final String handle : handles) {
                                try {
                                    driver.switchTo().window(handle);
                                    driver.close();
                                }
                                catch (final NoSuchWindowException e) {
                                    LOG.error("Error switching to browser window; quit browser.", e);
                                    WEB_DRIVERS_REAL_BROWSERS.remove(realBrowserKey);
                                    WEB_DRIVERS_REAL_BROWSERS_USAGE_COUNT.remove(realBrowserKey);
                                    driver.quit();
                                    return;
                                }
                            }

                            // we have to force WebDriver to treat the remaining window
                            // as the one we like to work with from now on
                            // looks like a web driver issue to me (version 2.47.2)
                            driver.switchTo().window(currentWindow);
                        }

                        driver.manage().deleteAllCookies();

                        // in the remaining window, load a blank page
                        driver.get("about:blank");
                    }
                    catch (final NoSuchSessionException e) {
                        LOG.error("Error browser session no longer available.", e);
                        WEB_DRIVERS_REAL_BROWSERS.remove(realBrowserKey);
                        WEB_DRIVERS_REAL_BROWSERS_USAGE_COUNT.remove(realBrowserKey);
                        return;
                    }
                    catch (final WebDriverException e) {
                        shutDownRealBrowsers();
                    }
                }
            }
        }
        assertTrue("There are still unhandled alerts: " + String.join("; ", unhandledAlerts),
                        unhandledAlerts.isEmpty());
    }

    /**
     * Returns the underlying WebWindow of the specified driver.
     *
     * <b>Your test shouldn't depend primarily on WebClient</b>
     *
     * @return the current web window
     * @see #toHtmlElement(WebElement)
     */
    protected WebWindow getWebWindow() {
        return webDriver_.getCurrentWindow().getWebWindow();
    }

    /**
     * Whether {@link WebClient} is cached or not, defaults to {@code false}.
     *
     * <p>This is needed to be {@code true} for huge test class, as we could run out of sockets.
     *
     * @return whether {@link WebClient} is cached or not
     */
    protected boolean isWebClientCached() {
        return false;
    }

    /**
     * Configure {@link WebClientOptions#getTimeout()}.
     *
     * @return null if unchanged otherwise the timeout as int
     */
    protected Integer getWebClientTimeout() {
        return null;
    }

    protected Page getEnclosedPage() {
        return getWebWindow().getEnclosedPage();
    }

    protected WebClient getWebClient() {
        return webDriver_.getWebClient();
    }
}
