# chai-dom

[![Build Status](https://secure.travis-ci.org/nathanboktae/chai-dom.png)](http://travis-ci.org/nathanboktae/chai-dom)

chai-dom is an extension to the [chai](http://chaijs.com/) assertion library that
provides a set of assertions when working with the DOM (specifically [HTMLElement][] and [NodeList][])

Forked from [chai-jquery](https://github.com/chaijs/chai-jquery) to use for those of us freed of jQuery's baggage.

## Assertions

### `attr(name[, value])`
### `attribute(name[, value])`

Assert that the [HTMLElement][] has the given attribute, using [`getAttribute`](https://developer.mozilla.org/en-US/docs/Web/API/Element/getAttribute).
Optionally, assert a particular value as well. The return value is available for chaining.

```js
document.getElementById('header').should.have.attr('foo')
expect(document.querySelector('main article')).to.have.attribute('foo', 'bar')
expect(document.querySelector('main article')).to.have.attr('foo').match(/bar/)
```

### `class(className)`
Assert that the [HTMLElement][] has the given class, using [`classList`](https://developer.mozilla.org/en-US/docs/Web/API/Element/classList).

```js
document.getElementsByName('bar').should.have.class('foo')
expect(document.querySelector('main article')).to.have.class('foo')
```

Also accepts regex as argument.

```js
document.getElementsByName('bar').should.have.class(/foo/)
expect(document.querySelector('main article')).to.have.class(/foo/)
```

### `id(id)`
Assert that the [HTMLElement][] has the given id.

```js
document.querySelector('section').should.have.id('#main')
expect(document.querySelector('section')).to.have.id('foo')
```

### `html(html)`
Assert that the html of the [HTMLElement][] is equal to or contains the given html.

```js
document.querySelector('.name').should.have.html('<em>John Doe</em>')
expect(document.querySelector('#title')).to.have.html('Chai Tea')
```
```js
document.querySelector('.name').should.contain.html('<span>Doe</span>')
expect(document.querySelector('#title')).to.contain.html('<em>Tea</em>')
```

### `text(text)`
Assert that the text of the [HTMLElement][] or combined text of the [NodeList][] is equal to or contains the given text, using [`textContent`][textContent]. Chaining flags:

`trimmed`  - will trim the text before comparing\
`rendered` - will use [`innerText`][innerText] when comparing

```js
document.querySelector('.name').should.have.text('John Doe')
expect(document.querySelector('#title')).to.have.text('Chai Tea')
document.querySelectorAll('ul li').should.have.text('JohnJaneJessie')
document.querySelector('h1').should.have.trimmed.text('chai-tests')
expect(document.querySelector('article')).to.have.rendered.text('Chai Tea is great')
```

```js
document.querySelector('.name').should.contain.text('John')
expect(document.querySelector('#title')).to.contain.text('Chai')
document.querySelectorAll('ul li').should.contain.text('Jane')
```

### `text(text[])`
Assert that the [`textContent`][textContent] of the [NodeList][] children deep equal those text, or when using the contains flag, all the text items are somewhere in the [NodeList][].

```js
document.querySelectorAll('.name').should.have.text(['John Doe', 'Jane'])
expect(document.querySelectorAll('ul li')).to.have.text(['John', 'Jane', 'Jessie'])
```

```js
document.querySelectorAll('.name').should.contain.text(['John Doe'])
expect(document.querySelectorAll('ul li')).to.contain.text(['John', 'Jessie'])
```

### `value(value)`
Assert that the [HTMLElement][] has the given value

```js
document.querySelector('.name').should.have.value('John Doe')
expect(document.querySelector('input.year')).to.have.value('2012')
```

### `empty`
Assert that the [HTMLElement][] or [NodeList][] has no child nodes. If the object asserted against is neither of those, the original implementation will be called.

```js
document.querySelector('.empty').should.be.empty
expect(document.querySelector('section')).not.to.be.empty
```

### `length(n)`
Assert that the [HTMLElement][] or [NodeList][] has exactly `n` child nodes. If the object asserted against is neither of those, the original implementation will be called.

```js
document.querySelector('ul').should.have.length(2)
document.querySelector('li').should.have.length(2)
expect(document.querySelector('ul')).not.to.have.length(3)
```

### `exist`
Assert that the [NodeList][] is not empty. If the object asserted
against is not a [NodeList][], the original implementation will be called.

```js
document.querySelectorAll('dl dd').should.exist
expect(document.querySelectorAll('.nonexistent')).not.to.exist
```

### `match(selector)`
Assert that the selection matches an [HTMLElement][] or all elements in a [NodeList][], using [`matches`](https://developer.mozilla.org/en-US/docs/Web/API/Element/matches). If the object asserted against is neither of those, the original implementation will be called.

Note `matches` is DOM Level 4, so you may [need a polyfill](https://github.com/WebReflection/dom4) for it.

```js
document.querySelectorAll('input').should.match('[name="bar"]')
expect(document.getElementById('empty')).to.match('.disabled')
```

### `contain(selector or element)`
Assert that the [HTMLElement][] contains the given element, using [`querySelector`][querySelector] for selector strings or using [`contains`][contains] for elements. If the object asserted against is not an [HTMLElement][], or if `contain` is not called as a function, the original
implementation will be called.

```js
document.querySelector('section').should.contain('ul.items')
document.querySelector('section').should.contain(document.querySelector('section div'))
expect(document.querySelector('#content')).to.contain('p')
```

### `descendant(selector or element)`
Same as `contain` but changes the assertion subject to the matched element.

```js
document.querySelector('section').should.have.descendant('ul').and.have.class('items')
document.querySelector('section').should.have.descendant(document.querySelector('section div'))
expect(document.querySelector('#content')).to.have.descendant('p')
```

### `descendants(selector)`
Same as `descendant` but uses [`querySelectorAll`][querySelectorAll] instead of [`querySelector`][querySelector] to change the assertion subject to a [NodeList][] instead of a single element.

```js
document.querySelector('section').should.have.descendants('ul li').and.have.length(3)
```

### `displayed`
Assert that the [HTMLElement][] is displayed (that display is not equal to "none"). If the element is attached to the body, it will call [`getComputedStyle`](https://developer.mozilla.org/en-US/docs/Web/API/Window/getComputedStyle); otherwise it will look at the inline display attribute.

```js
document.querySelector('dl dd').should.be.displayed
expect(document.querySelector('.hidden')).not.to.be.displayed
```

### `visible`
Assert that the [HTMLElement][] is visible (that visibility is not equal to "hidden" or "collapse"). If the element is attached to the body, it will call [`getComputedStyle`](https://developer.mozilla.org/en-US/docs/Web/API/Window/getComputedStyle); otherwise it will look at the inline visibility attribute.

```js
document.querySelector('dl dd').should.be.visible
expect(document.querySelector('.invisible')).not.to.be.visible
```

### `tagName(name)`
Assert that the [HTMLElement][] has the given tagName.

```js
document.querySelector('.container').should.have.tagName('div')
expect(document.querySelector('.container')).not.to.have.tagName('span')
```

### `style(styleProp, styleValue)`
Assert that the [HTMLElement][] has the given style prop name value equal to a given value.

```js
document.querySelector('.container').should.have.style('color', 'rgb(55, 66, 77)')
expect(document.querySelector('.container')).not.to.have.style('borderWidth', '3px')
```

### `focus`

Assert that the [HTMLElement][] has set focus.

```js
document.querySelector('input').should.have.focus
expect(document.querySelector('.container')).not.to.have.focus
```

### `checked`

Assert that the [HTMLElement][] is an [HTMLInputElement][] with `type` of "checkbox" or "radio", and that its `checked` state is true or false.

```js
document.querySelector('input').should.be.checked
expect(document.querySelector('.checkbox')).not.to.be.checked
```

## Installation

### npm

```
npm install chai-dom
```

### bower

```
bower install chai-dom
```

## Usage

### CommonJS

```javascript
var chai = require('chai')
chai.use(require('chai-dom'))
```

### AMD

```javascript
require(['chai', 'chai-dom'], function(chai, chaiDom) {
  chai.use(chaiDom)
})
```

### Global

```html
<script src="chai.js"></script>
<script src="chai-dom.js"></script>
```

Use the assertions with chai's `expect` or `should` assertions.

## Contributing

To run the test suite, run `npm install` (requires
[Node.js](http://nodejs.org/) to be installed on your system), and run `npm test` or open
`test/index.html` in your web browser.

## License

MIT License (see the LICENSE file)

[HTMLElement]: https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement
[NodeList]: https://developer.mozilla.org/en-US/docs/Web/API/NodeList
[textContent]: https://developer.mozilla.org/en-US/docs/Web/API/Node/textContent
[innerText]: https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement/innerText
[querySelector]: https://developer.mozilla.org/en-US/docs/Web/API/Element/querySelector
[querySelectorAll]: https://developer.mozilla.org/en-US/docs/Web/API/Element/querySelectorAll
