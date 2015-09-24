
/**
 * Make up for hole in Safari:
 */
if (!String.prototype.localeCompare) {
  String.prototype.localeCompare = function(other) {
    if (this < other) return -1;
    else if (this > other) return 1;
    else return 0;
  }
}

/**
 *
 */
function callOnLoad(load) {
  if (window.addEventListener){
    window.addEventListener("load", load, false);
  }
  else if (window.attachEvent) {
    window.attachEvent("onload", load);
  }
  else {
    window.onload = load;
  }
}

/**
 * eval() breaks when we use it to get an object using the { a:42, b:'x' }
 * syntax because it thinks that { and } surround a block and not an object
 * So we wrap it in () to get around this.
 */
function objectEval(text) {
  return eval("(" + text + ")");
}

/**
 *
 */
function testEquals(actual, expected, depth) {
  // Rather than failing we assume that it works!
  if (depth > 10) return true;

  if (expected == null) {
    if (actual != null) {
      return "expected: null, actual non-null: " + dwr.util.toDescriptiveString(actual);
    }
    return true;
  }

  if (actual == null) {
    if (expected != null) {
      return "actual: null, expected non-null: " + dwr.util.toDescriptiveString(expected);
    }
    return true; // we wont get here of course ...
  }

  if (typeof expected == "object") {
    if (!(typeof actual == "object")) {
      return "expected object, actual not an object";
    }

    var actualLength = 0;
    for (var prop in actual) {
      if (typeof actual[prop] != "function" || typeof expected[prop] != "function") {
        var nest = testEquals(actual[prop], expected[prop], depth + 1);
        if (typeof nest != "boolean" || !nest) {
          return "element '" + prop + "' does not match: " + nest;
        }
      }
      actualLength++;
    }

    // need to check length too
    var expectedLength = 0;
    for (prop in expected) expectedLength++;
    if (actualLength != expectedLength) {
      return "expected object size = " + expectedLength + ", actual object size = " + actualLength;
    }
    return true;
  }

  if (actual != expected) {
    return "expected = " + expected + " (type=" + typeof expected + "), actual = " + actual + " (type=" + typeof actual + ")";
  }

  if (expected instanceof Array) {
    if (!(actual instanceof Array)) {
      return "expected array, actual not an array";
    }
    if (actual.length != expected.length) {
      return "expected array length = " + expected.length + ", actual array length = " + actual.length;
    }
    for (var i = 0; i < actual.length; i++) {
      var inner = testEquals(actual[i], expected[i], depth + 1);
      if (typeof inner != "boolean" || !inner) {
        return "element " + i + " does not match: " + inner;
      }
    }

    return true;
  }

  return true;
}

