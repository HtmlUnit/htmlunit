/*****************************************************************************
 *
 * Copyright (c) 2003-2004 EcmaUnit Contributors. All rights reserved.
 *
 * This software is distributed under the terms of the EcmaUnit
 * License. See LICENSE.txt for license text. For a list of EcmaUnit
 * Contributors see CREDITS.txt.
 *
 *****************************************************************************/

// $Id$

/*
   Object-oriented prototype-based unit test suite
*/

function TestCase() {
    /* a single test case */
    this.name = 'TestCase';
};

TestCase.prototype.initialize = function(reporter) {
    // this array's contents will be displayed when done (if it
    // contains anything)
    this._exceptions = new Array();
    this._reporter = reporter;
};

TestCase.prototype.setUp = function() {
    /* this will be called on before each test method that is ran */
};

TestCase.prototype.tearDown = function() {
    /* this will be called after each test method that has been ran */
};

TestCase.prototype.assertEquals = function(var1, var2, message) {
    /* assert whether 2 vars have the same value */
    if (!message)  {
        message = '';
    } else {
        message = "'" + message + "' ";
    }
    if (var1 != var2 &&
            (!(var1 instanceof Array && var2 instanceof Array) ||
                !this._arrayDeepCompare(var1, var2))) {
        this._throwException('Assertion ' + message + 'failed: ' + 
                                var1 + ' != ' + var2);
    };
};

TestCase.prototype.assertNotEquals = function(var1, var2, message) {
    /* assert whether 2 vars have different values */
    if (!message)  {
        message = '';
    } else {
        message = "'" + message + "' ";
    }
    if (var1 && var1.toSource && var2 && var2.toSource) {
        if (var1.toSource() == var2.toSource()) {
            this._throwException('Assertion ' + message + 'failed: ' + 
                                    var1 + ' == ' + var2);
        };
    } else {
        if (var1 == var2) {
            this._throwException('Assertion ' + message + 'failed: ' + 
                                    var1 + ' == ' + var2);
        };
    };
};

TestCase.prototype.debug = function(msg) {
    this._reporter.debug(msg);
}
TestCase.prototype.assert = function(statement, message) {
    /* assert whether a variable resolves to true */
    if (!statement) {
        if (!message) message = (statement && statement.toString) ? 
                                    statement.toString() : statement;
        this._throwException('Assertion \'' + message + '\' failed');
    };
};

TestCase.prototype.assertTrue = TestCase.prototype.assert;

TestCase.prototype.assertFalse = function(statement, message) {
    /* assert whether a variable resolves to false */
    if (statement) {
        if (!message) message = statement.toString ? 
                statement.toString() : statement;
        this._throwException('AssertFalse \'' + message + '\' failed');
    };
};

TestCase.prototype.assertThrows = function(func, exception, context) {
    /* assert whether a certain exception is raised */
    if (!context) {
        context = null;
    };
    var exception_thrown = false;
    // remove the first three args, they're the function's normal args
    var args = [];
    for (var i=3; i < arguments.length; i++) {
        args.push(arguments[i]);
    };
    try {
        func.apply(context, args);
    } catch(e) {
        // allow catching undefined exceptions too
        if (exception === undefined) {
        } else if (exception) {
            var isinstance = false;
            try {
                if (e instanceof exception) {
                    isinstance = true;
                };
            } catch(f) {
            };
            if (!isinstance) {
                if (exception.toSource && e.toSource) {
                    exception = exception.toSource();
                    e = e.toSource();
                };
                if (exception.toString && e.toString) {
                    exception = exception.toString();
                    e = e.toString();
                };
                if (e != exception) {
                    this._throwException('Function threw the wrong ' +
                            'exception ' + e.toString() + 
                            ', while expecting ' + exception.toString());
                };
            };
        };
        exception_thrown = true;
    };
    if (!exception_thrown) {
        if (exception) {
            this._throwException("function didn\'t raise exception \'" + 
                                    exception.toString() + "'");
        } else {
            this._throwException('function didn\'t raise exception');
        };
    };
};

TestCase.prototype.runTests = function() {
    /* find all methods of which the name starts with 'test'
        and call them */
    var ret = this._runHelper();
    this._reporter.summarize(ret[0], ret[1], this._exceptions);
};

TestCase.prototype._runHelper = function() {
    /* this actually runs the tests
        return value is an array [total tests ran, total time spent (ms)]
    */
    var now = new Date();
    var starttime = now.getTime();
    var numtests = 0;
    for (var attr in this) {
        if (attr.substr(0, 4) == 'test') {
            this.setUp();
            try {
                this[attr]();
                this._reporter.reportSuccess(this.name, attr);
            } catch(e) {
                var raw = e;
                if (e && e.name && e.message) { // Microsoft
                    e = e.name + ': ' + e.message;
                }
                this._reporter.reportError(this.name, attr, e, raw);
                this._exceptions.push(new Array(this.name, attr, e, raw));
            };
            this.tearDown();
            numtests++;
        };
    };
    var now = new Date();
    var totaltime = now.getTime() - starttime;
    return new Array(numtests, totaltime);
};

TestCase.prototype._throwException = function(message) {
    var lineno = this._getLineNo();
    if (lineno) {
        message = 'line ' + lineno + ' - ' + message;
    };
    throw(message);
};

TestCase.prototype._getLineNo = function() {
    /* tries to get the line no in Moz */
    var stack = undefined;
    try {notdefined()} catch(e) {stack = e.stack};
    if (stack) {
        stack = stack.toString().split('\n');
        for (var i=0; i < stack.length; i++) {
            var line = stack[i].split('@')[1];
            if (line.indexOf('ecmaunit') == -1) {
                // return the first line after we get out of ecmaunit
                var chunks = line.split(':');
                var lineno = chunks[chunks.length - 1];
                if (lineno != '0') {
                    return lineno;
                };
            };
        };
    } else {
        return false;
    };
};

TestCase.prototype._arrayDeepCompare = function(a1, a2) {
    if (!(a1 instanceof Array && a2 instanceof Array)) {
        return false;
    };
    if (a1.length != a2.length) {
        return false;
    };
    for (var i=0; i < a1.length; i++) {
        if (a1[i] instanceof Array) {
            if (!this._arrayDeepCompare(a1[i], a2[i])) {
                return false;
            };
        } else if (a1[i] != a2[i]) {
            return false;
        };
    };
    return true;
};

function TestSuite(reporter) {
    /* run a suite of tests */
    if (reporter) {
        this._reporter = reporter;
        this._tests = new Array();
        this._exceptions = new Array();
    };
};

TestSuite.prototype.registerTest = function(test) {
    /* register a test */
    if (!test) {
        throw('TestSuite.registerTest() requires a testcase as argument');
    };
    this._tests.push(test);
};

TestSuite.prototype.runSuite = function() {
    /* run the suite */
    var now = new Date();
    var starttime = now.getTime();
    var testsran = 0;
    for (var i=0; i < this._tests.length; i++) {
        var test = new this._tests[i]();
        test.initialize(this._reporter);
        testsran += test._runHelper()[0];
        // the TestCase class handles output of dots and Fs, but we
        // should take care of the exceptions
        if (test._exceptions.length) {
            for (var j=0; j < test._exceptions.length; j++) {
                // attr, exc in the org array, so here it becomes
                // name, attr, exc
                var excinfo = test._exceptions[j];
                this._exceptions.push(excinfo);
            };
        };
    };
    var now = new Date();
    var totaltime = now.getTime() - starttime;
    this._reporter.summarize(testsran, totaltime, this._exceptions);
};

function StdoutReporter(verbose) {
    if (verbose) {
        this.verbose = verbose;
    };
};

StdoutReporter.prototype.debug = function(text) {
    print(text+"\n");
}

StdoutReporter.prototype.reportSuccess = function(testcase, attr) {
    /* report a test success */
    if (this.verbose) {
        print(testcase + '.' + attr + '(): OK');
    } else {
        print('.');
    };
};

StdoutReporter.prototype.reportError = function(testcase, attr, 
                                                exception, raw) {
    /* report a test failure */
    if (this.verbose) {
        print(testcase + '.' + attr + '(): FAILED!');
    } else {
        print('F');
    };
};

StdoutReporter.prototype.summarize = function(numtests, time, exceptions) {
    print('\n' + numtests + ' tests ran in ' + time / 1000.0 + 
            ' seconds\n');
    if (exceptions.length) {
        for (var i=0; i < exceptions.length; i++) {
            var testcase = exceptions[i][0];
            var attr = exceptions[i][1];
            var exception = exceptions[i][2];
            var raw = exceptions[i][3];
            print(testcase + '.' + attr + ', exception: ' + exception);
            if (this.verbose) {
                this._printStackTrace(raw);
            };
        };
        print('NOT OK!');
    } else {
        print('OK!');
    };
};

StdoutReporter.prototype._printStackTrace = function(exc) {
    if (!exc.stack) {
        print('no stacktrace available');
        return;
    };
    var lines = exc.stack.toString().split('\n');
    var toprint = [];
    for (var i=0; i < lines.length; i++) {
        var line = lines[i];
        if (line.indexOf('ecmaunit.js') > -1) {
            // remove useless bit of traceback
            break;
        };
        if (line.charAt(0) == '(') {
            line = 'function' + line;
        };
        var chunks = line.split('@');
        toprint.push(chunks);
    };
    toprint.reverse();
    for (var i=0; i < toprint.length; i++) {
        print('  ' + toprint[i][1]);
        print('    ' + toprint[i][0]);
    };
    print();
};

function HTMLReporter(outputelement, verbose) {
    if (outputelement) {
        this.outputelement = outputelement;
        this.document = outputelement.ownerDocument;
        this.verbose = verbose;
    };
};

HTMLReporter.prototype.debug = function(text) {
    var msg = this.document.createTextNode(text);
    var div = this.document.createElement('div');
    div.appendChild(msg);
    this.outputelement.appendChild(div);
};

HTMLReporter.prototype.reportSuccess = function(testcase, attr) {
    /* report a test success */
    // a single dot looks rather small
    var dot = this.document.createTextNode('+');
    this.outputelement.appendChild(dot);
};

HTMLReporter.prototype.reportError = function(testcase, attr, exception, raw) {
    /* report a test failure */
    var f = this.document.createTextNode('F');
    this.outputelement.appendChild(f);
};

HTMLReporter.prototype.summarize = function(numtests, time, exceptions) {
    /* write the result output to the html node */
    var p = this.document.createElement('p');
    var text = this.document.createTextNode(numtests + ' tests ran in ' + 
                                            time / 1000.0 + ' seconds');
    p.appendChild(text);
    this.outputelement.appendChild(p);
    if (exceptions.length) {
        for (var i=0; i < exceptions.length; i++) {
            var testcase = exceptions[i][0];
            var attr = exceptions[i][1];
            var exception = exceptions[i][2].toString();
            var raw = exceptions[i][3];
            var div = this.document.createElement('div');
            var lines = exception.toString().split('\n');
            var text = this.document.createTextNode(
                testcase + '.' + attr + ', exception ');
            div.appendChild(text);
            // add some formatting for Opera: this browser displays nice
            // tracebacks...
            for (var j=0; j < lines.length; j++) {
                var text = lines[j];
                if (j > 0) {
                    text = '\xa0\xa0\xa0\xa0' + text;
                };
                div.appendChild(this.document.createTextNode(text));
                div.appendChild(this.document.createElement('br'));
            };
            div.style.color = 'red';
            this.outputelement.appendChild(div);
            if (this.verbose) {
                // display stack trace on Moz
                this._displayStackTrace(raw);
            };
        };
        var div = this.document.createElement('div');
        var text = this.document.createTextNode('NOT OK!');
        div.appendChild(text);
        div.style.backgroundColor = 'red';
        div.style.color = 'black';
        div.style.fontWeight = 'bold';
        div.style.textAlign = 'center';
        div.style.marginTop = '1em';
        this.outputelement.appendChild(div);
    } else {
        var div = this.document.createElement('div');
        var text = this.document.createTextNode('OK!');
        div.appendChild(text);
        div.style.backgroundColor = 'lightgreen';
        div.style.color = 'black';
        div.style.fontWeight = 'bold';
        div.style.textAlign = 'center';
        div.style.marginTop = '1em';
        this.outputelement.appendChild(div);
    };
};

HTMLReporter.prototype._displayStackTrace = function(exc) {
    /*
    if (arguments.caller) {
        // IE
        var caller = arguments;
        toprint = [];
        while (caller) {
            var callee = caller.callee.toString();
            callee = callee.replace('\n', '').replace(/\s+/g, ' ');
            var funcsig = /(.*?)\s*\{/.exec(callee)[1];
            var args = caller.callee.arguments;
            var displayargs = [];
            for (var i=0; i < args.length; i++) {
                displayargs.push(args[i].toString());
            };
            toprint.push((funcsig + ' - (' + displayargs + ')'));
            caller = caller.caller;
        };
        toprint.reverse();
        var pre = this.document.createElement('pre');
        for (var i=0; i < toprint.length; i++) {
            pre.appendChild(document.createTextNode(toprint[i]));
            pre.appendChild(document.createElement('br'));
        };
        this.outputelement.appendChild(pre);
    };
    */
    if (exc.stack) {
        // Moz (sometimes)
        var lines = exc.stack.toString().split('\n');
        var toprint = []; // need to reverse this before outputting
        for (var i=0; i < lines.length; i++) {
            var line = lines[i];
            if (line.indexOf('ecmaunit.js') > -1) {
                // remove useless bit of traceback
                break;
            };
            if (line[0] == '(') {
                line = 'function' + line;
            };
            line = line.split('@');
            toprint.push(line);
        };
        toprint.reverse();
        var pre = this.document.createElement('pre');
        for (var i=0; i < toprint.length; i++) {
            pre.appendChild(
                this.document.createTextNode(
                    '  ' + toprint[i][1] + '\n    ' + toprint[i][0] + '\n'
                )
            );
        };
        pre.appendChild(document.createTextNode('\n'));
        this.outputelement.appendChild(pre);
    };
};
