// spec/helloSpec.js
// Plain ES5 Jasmine spec - var + function only, no class/let/const/arrow
// functions - so this only exercises the most basic JS + Jasmine API surface.

describe("greet", function () {

  it("says hello world when no name is given", function () {
    expect(greet()).toEqual("Hello, World!");
  });

  it("greets the given name", function () {
    expect(greet("Jasmine")).toEqual("Hello, Jasmine!");
  });

});

describe("add", function () {

  it("adds two positive numbers", function () {
    expect(add(2, 3)).toEqual(5);
  });

  it("handles negative numbers", function () {
    expect(add(-1, 1)).toEqual(0);
  });

});

describe("isEven", function () {

  it("returns true for even numbers", function () {
    expect(isEven(4)).toBe(true);
  });

  it("returns false for odd numbers", function () {
    expect(isEven(3)).toBe(false);
  });

});
