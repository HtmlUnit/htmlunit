
function giLoaded() {
  OpenAjax.subscribe("gidemo", "corporation", objectPublished);
  dwr.engine.setActiveReverseAjax(true);
}

function objectPublished(prefix, name, handlerData, corporation) {
  var matrix = giApp.getJSXByName("matrix");
  var inserted = matrix.getRecordNode(corporation.jsxid);
  matrix.insertRecord(corporation, null, inserted == null);

  // There are many ways to get a table to repaint.
  // One easy way is to ask it to repaint:
  // matrix.repaintData();

  // But we are going for a fancy option that does highlighting
  for (var property in corporation) {
    if (property != "jsxid") {
      var ox = matrix.getContentElement(corporation.jsxid, property);
      if (ox) {
        var current = ox.innerHTML;
        if (current != "" + corporation[property]) {
          ox.style.backgroundColor = "#FDE4EB";
          var callback = function(ele) {
            return function() { ele.style.backgroundColor = "#FFFFFF"; };
          }(ox);
          setTimeout(callback, 1000);
          ox.innerHTML = corporation[property];
        }
      }
    }
  }
}
