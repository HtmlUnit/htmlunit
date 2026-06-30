// src/hello.js
// Plain, old-school JavaScript on purpose - no classes, no arrow functions,
// no template literals, no let/const. Just var + function, to keep the
// syntax requirements as low as possible while smoke-testing Jasmine itself.

function greet(name) {
  if (!name) {
    return "Hello, World!";
  }
  return "Hello, " + name + "!";
}

function add(a, b) {
  return a + b;
}

function isEven(n) {
  return n % 2 === 0;
}
