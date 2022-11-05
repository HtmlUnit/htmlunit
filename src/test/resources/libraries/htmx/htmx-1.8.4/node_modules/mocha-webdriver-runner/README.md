# mocha-webdriver-runner

[![npm](https://img.shields.io/npm/v/mocha-webdriver-runner.svg?style=flat-square)](http://www.npmjs.com/package/mocha-webdriver-runner)
[![Build status](https://travis-ci.org/zbigg/mocha-webdriver-runner.svg?branch=master)](https://travis-ci.org/zbigg/mocha-webdriver-runner)

Run Mocha tests in browsers using Selenium WebDriver.

Inspired by [mocha-chrome](https://www.npmjs.com/package/mocha-chrome), but with following
features implemented from start:

-   drives browser using Selenium WebDriver, so you can run tests on everything that selenium WebDriver supports. Hint: it supports everything (tm).
-   runs reporters locally, in node environment so most of reporters (which are designed to work in node environment) should work out-of-box:
    -   tested mocha builtins: spec, xunit, tap, etc ...
    -   support for `mochawesome` (including usage of `addContext`)

That's it, have fun.

## Install

```
$ npm install mocha-webdriver-runner
```

(Also ensure that you've got proper drivers or Selenium Grid available, see [Browser Driver](#browser-drivers) section below).

## Usage

Prepare your tests to run in browser as described on [Mocha website](https://mochajs.org/#running-mocha-in-the-browser).

Add `mocha-webdriver-runner` browser side client just after normal `mocha.js` `<script>` tag:

       <script src="../node_modules/mocha/mocha.js"></script
     + <script src="../node_modules/mocha-webdriver-runner/dist/mocha-webdriver-client.js"></script>

Run the test suite against local browser:

    SELENIUM_BROWSER=chrome npx mocha-webdriver-runner test/index.html

    SELENIUM_BROWSER=firefox npx mocha-webdriver-runner test/index.html --reporter=tap

(assuming your tests are in test/index.html).

The HTML test page works in two environments:
 * normal browser window - HTML report is generated in browser window as usual
 * when ran through `mocha-webdriver-runner`, report is forwarded to reporter running in node
   (default is `spec`)

See `package.json` scripts and `test/sample-suite/index-headless.html` for reference.

## Browser capabilities

Use `-C key[=value]` (or `--capability`) options to set requested browser capabilities.
Value may be plain string, or JSON value, examples:

```
-C browserName=firefox
-C moz:firefoxOptions.args='["-headless"]'
-C browserName=chrome
-C goog:chromeOptions.args='["--headless", "--window-size=300,300"]'
```

Convenience shortcuts:

| Shortcut option | Resolves to
| -               | ----
| `--headless-chrome`  | `-C browserName=chrome -C goog:chromeOptions.args='["--headless"]'`
| `--chrome`           | `-C browserName=chrome`
| `--headless-firefox` | `-C browserName=firefox -C moz:firefoxOptions.args='["-headless"]'`
| `--firefox`          | `-C browserName=firefox`
| `--edge`             | `-C browserName=MicrosoftEdge`
| `--safari`           | `-C browserName=safari`

Useful links:

-   [Selenium Capabilities](https://github.com/SeleniumHQ/selenium/wiki/DesiredCapabilities)
-   [Gecko driver capabilities](https://firefox-source-docs.mozilla.org/testing/geckodriver/geckodriver/Capabilities.html)
-   [Chrome driver capabilities](https://sites.google.com/a/chromium.org/chromedriver/capabilities)

Selenium WebDriverJS accepts capabilities passed by environment variables as below:

```
SELENIUM_BROWSER=chrome
SELENIUM_BROWSER=firefox:52
SELENIUM_REMOTE_URL=http://my-selenium-grid:4444/wd/hub
```

See [WebDriverJS Builder](https://seleniumhq.github.io/selenium/docs/api/javascript/module/selenium-webdriver/index_exports_Builder.html)

## Options

```
  -c, --config <FILE>                     config file (default: ".mocha-webdriver-runner.json")
  -C, --capability <name[=value]>         required browser capability
  -O, --reporter-options <k=v,k2=v2,...>  reporter-specific options
  -R, --reporter <name>                   specify the reporter to use (default: "spec")
  -t, --timeout <ms>                      set test-case timeout in milliseconds (default: 2000)
  -L, --capture-console-log <boolean>     whether to capture console.log in browser context (default: true)
  -g, --grep <pattern>                    only run tests/suites that match pattern
  -V, --version                           output the version number
  --chrome                                use Chrome
  --headless-chrome                       use headless Chrome
  --firefox                               use Firefox
  --headless-firefox                      use headless Firefox
  --safari                                use Safari
  --edge                                  use Edge
```

## Config file

`mocha-webdriver-runner` can load options from config file. Use `-c FILE` option to specify custom file.
If `-c` is not specified, `mocha-webdriver-runner` will attempt to load it from `.mocha-webdriver-runner.json`.

Config file, is JSON with, following properties:

 * `capabilities` - object representing `WebDriver` capabilities
 * all other CLI options are available as properties

Example config for tests on Headless Chrome with no GPU and selecting only tests with `#performance` tag:
```
{
    "timeout": 0,
    "grep": "#performance
    "capabilities": {
        "browserName": "chrome",
        "goog:chromeOptions": {
            "args": ["--headless", "--disable-gpu=true"]
        }
    }
}
```


## Browser Drivers

Testing against browser that runs on your desktop requires that proper drivers are installed in your environment.

* Chrome - requires `chromedriver`
   * available as NPM packet `chromedriver`
   * documentation & manual download: http://chromedriver.chromium.org/
* Firefox - requires `geckdriver`
   * available as NPM packet `geckodriver`
   * documentation & manual download: https://firefox-source-docs.mozilla.org/testing/geckodriver/geckodriver/
* Safari - requires `safaridriver`
   * SafariDriver (now) is installed by default, see https://developer.apple.com/documentation/webkit/testing_with_webdriver_in_safari

For `mocha-webdriver-runner` to work, particular webdriver must be installed somwehere in `PATH`.

Note,  _convenience_ NPM packages -`chromedriver` and `geckdriver` - install them it to `./node_modules/.bin`, so if you run your tests via npm drivers are found automagically.

## API

### Node.Js

From Node.js you can start tests using `runMochaWebDriverTest`.

```javascript
// in node.js context
import { runMochaWebDriverTest } from "mocha-webdriver-runner";

const webDriverCapabilities = {
    browserName: "firefox"
};

runMochaWebDriverTest(webDriverCapabilities, "https://localhost:8080/test/index.html")
    .then(result => {
        // result is boolean i.e ok or not ok
        console.log("test result", result ? ":)" : ":(");
    })
    .catch(error => {
        // something bad happened with runner itself i.e webdriver error or something
    });
```

### Browser general API

Browser module export global object `MochaWebdriverClient`.

Import examples:

```html
<!-- from CDN -->
<script src="https://unpkg.com/mocha-webdriver-runner/dist/mocha-webdriver-client.js"></script>
<!-- from local node_modules -->
<script src="../node_modules/mocha-webdriver-runner/dist/mocha-webdriver-client.js"></script>
```

`MochaWebdriverClient` API

-   `addMochaSource(mocha)` - instruments `mocha` instance to send runner events back to
    `mocha-selenium-runner` process.

    Example:

    ```javascript
    mocha.setup({ ui: "bdd" });
    MochaWebdriverClient.install(mocha);
    // load sources
    mocha.run();
    ```

-   `addWorkerSource(worker: Worker)` - forwards all `mocha-selenium-runner` related events from
    `worker` back to `mocha-selenium-runner` process (requires properly initialized `mocha` in
    `worker` context

    Example:

    ```javascript
    const worker = new Worker("some-test-worker.js");
    MochaWebdriverClient.addWorkerSource(worker);
    ```

Examples:

-   [basic tests running in browser](test/sample-suite/index-headless.html)
-   [require.js based test runner](test/sample-suite/requirejs.html)
-   [tests ran in worker](test/sample-suite/worker-test.html)
-   [tests ran in worker in auto mode](test/sample-suite/worker-test-auto.html)
-   [example library / coverage report](test/sample-lib/README.md)

## Contribute

PRs accepted.

## License

MIT © Zbigniew Zagórski
