========================
ecmaunit unit test suite
========================

This is an object oriented unit test suite library for JavaScript. It is
tested on Internet Explorer and Mozilla and should run on all browers with
decent JavaScript support.

This package can just be placed somewhere on a filesystem or webserver and
used directly by other JavaScripts. For an example (this should be enough to 
get you started) see 'testecmaunit.js' and 'testecmaunit.html' (which happen 
to be tests for the system as well, although they're a bit awkward for some 
of them are supposed to fail for the demo).

EcmaUnit also supports running test from the command-line with a
proper JavaScript interpreter. Spidermonkey of the Mozilla project has
proven to work. To run the ecmaunit tests from the command line using
the spidermonkey interpreter js, simply issue the following command:

  $ js -f ecmaunit.js -f testecmaunit.js runtests.js

If you have any questions, want to report bugs or have suggestions about how
to improve the product, please send an email to Guido Wesdorp 
(guido@infrae.com).

Thanks for using EcmaUnit!
