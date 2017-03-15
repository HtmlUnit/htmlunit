
function update() {
  var name = dwr.util.getValue("demoName");
  Demo.sayHello(name, function(data) {
    dwr.util.setValue("demoReply", data);
  });
}
