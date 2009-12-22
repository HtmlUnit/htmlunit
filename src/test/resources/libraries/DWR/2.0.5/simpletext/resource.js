
function forward() {
  Demo.getInclude(function(data) {
    dwr.util.setValue("forward", data, { escapeHtml:false });
  });
}
