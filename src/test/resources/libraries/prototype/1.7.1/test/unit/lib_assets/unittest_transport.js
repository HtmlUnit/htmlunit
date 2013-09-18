function postUnittestResults(results) {
  var fields = ['tests', 'assertions', 'failures', 'errors'],
      prefix = 'unittest_js_',
      prop;
      
  for (var i = 0; i < fields.length; i++) {
    prop = fields[i]
    document.getElementById(prefix + prop).value = results[prop];
  }
  document.getElementById(prefix + 'transport_form').submit();
};