[简体中文](https://gitee.com/ambit/es6-proxy-polyfill#git-readme) | English


# ES6 Proxy Polyfill&nbsp;&nbsp;![Version](https://img.shields.io/npm/v/es6-proxy-polyfill.svg)

This is a polyfill for ES6 `Proxy`, supports  **IE6+** , Node.js, etc.

So far, it supports more features than the <a href="https://github.com/GoogleChrome/proxy-polyfill" target="_blank">proxy-polyfill</a> of GoogleChrome.

The polyfill supports just a limited number of proxy 'trap':
* get
* set
* apply
* construct

The `Proxy.revocable` method is also supported, but only for calls to the above traps.


#### Installation
```javascript
npm i -S es6-proxy-polyfill
```


#### Usage
1. Browser:
```html
<!--[if lte IE 8]>
<script src="path/to/object-defineproperty-ie.js" type="text/javascript"></script>
<![endif]-->
<script src="path/to/es6-proxy-polyfill.js" type="text/javascript"></script>
<script type="text/javascript">
    var proxy = new Proxy({}, {});
</script>
```
2. Node.js:
```javascript
const Proxy = require('es6-proxy-polyfill');

let proxy = new Proxy({}, {});
```


#### Notice
1. For **non-array** object, the properties you want to proxy **must be known at creation time**;
1. In IE8 or below, it depends on `Object.defineProperties` and `Object.getOwnPropertyDescriptor` provided by library "<a href="https://github.com/ambit-tsai/object-defineproperty-ie" target="_blank">object-defineproperty-ie</a>";
1. The support of traps:

||Object|Function|Array|
|:-:|:-:|:-:|:-:|
|>=IE9|get, set|get, set, apply, construct|get, set|
|<=IE8|get, set|apply, construct|-|


#### Testing
1. Access `test/browser/index.html` with browser
1. Tested in IE6-8, IE11


#### Contact Us
1. WeChat: ambit_tsai
1. QQ Group: 663286147
1. E-mail: ambit_tsai@qq.com