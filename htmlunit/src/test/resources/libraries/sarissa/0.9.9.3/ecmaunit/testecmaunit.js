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

function TestTestCase() {
    this.name = 'TestTestCase';

    this.setUp = function() {
        /* not in use here, didn't have to define it but this might be
           used as a reference
        */
    };

    this.testAssert = function() {
        this.assert(true);
        this.assertThrows(this.assert, undefined, this, false);
    };
        
    this.testAssertEquals = function() {
        this.assertEquals('foo', 'foo');
        this.assertThrows(this.assertEquals, undefined, this, 'foo', 'bar');
    };

    this.testAssertNotEquals = function() {
        this.assertNotEquals('foo', 'bar');
        this.assertThrows(this.assertNotEquals, undefined, this, 'foo', 'foo');
    };

    this.testAssertTrue = function() {
        this.assertTrue(1);
        this.assertTrue('foo');
        this.assertThrows(this.assertTrue, undefined, this, false);
    };

    this.testAssertFalse = function() {
        this.assertFalse(0);
        this.assertFalse('');
        this.assertThrows(this.assertFalse, undefined, this, true);
    };

    this.testAssertThrows = function() {
        this.assertThrows(function() {throw('foo')}, 'foo');
        this.assertThrows(function() {throw(new Array(1,2))}, new Array(1,2));
        this.assertThrows(function() {throw('bar')});
        this.assertThrows(this.assertThrows, undefined, this, 
                            function() {}, 'baz');
    };

    this.tearDown = function() {
        /* not in use here, didn't have to define it but this might be
           used as a reference
        */
    };
};

TestTestCase.prototype = new TestCase;

function TestTestCase2() {
    /* an example unit test */
    this.name = 'TestTestCase2';

    this.setUp = function() {
        function Foo() {
            this.returnfoo = function() {
              return 'foo';
            };
            this.throwfoo = function() {
              throw('foo');
            };
        };
        this.foo = new Foo();
    };

    this.testAssert = function() {
        this.assert(this.foo.returnfoo() == 'foo');
    };
        
    this.testAssertEquals = function() {
        this.assertEquals(this.foo.returnfoo(), 'foo');
    };

    this.testAssertNotEquals = function() {
        this.assertNotEquals(this.foo.returnfoo(), 'bar');
    };

    this.testAssertTrue = function() {
        this.assertTrue(this.foo.returnfoo() == 'foo');
    };

    this.testAssertFalse = function() {
        this.assertFalse(this.foo.returnfoo() == 'bar');
    };

    this.testAssertThrows = function() {
        this.assertThrows(this.foo.throwfoo, 'foo');
    };
};

TestTestCase2.prototype = new TestCase;
