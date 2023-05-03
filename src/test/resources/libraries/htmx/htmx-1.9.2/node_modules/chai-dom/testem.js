/* eslint-env node */
module.exports = {
  test_page: 'test/index.html',
  launch_in_ci: [
    'Chrome',
    'Firefox'
  ],
  launch_in_dev: [
    'Chrome'
  ],
  browser_args: {
    Chrome: {
      mode: 'ci',
      args: [
        // --no-sandbox is needed when running Chrome inside a container
        process.env.TRAVIS ? '--no-sandbox' : null,

        '--disable-gpu',
        '--headless',
        '--remote-debugging-port=0',
        '--window-size=1440,900'
      ].filter(Boolean)
    },
    Firefox: {
      mode: 'ci',
      args: [
        '-headless'
      ]
    }
  }
};
