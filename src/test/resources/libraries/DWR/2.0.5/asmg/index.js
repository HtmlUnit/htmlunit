
function verifyName() {
  var name = dwr.util.getValue("name");
  if (name == "") {
    dwr.util.setValue("nameError", "Please enter a name.");
  }
  else {
    dwr.util.setValue("nameError", "");
  }
}

function verifyAddress() {
  var address = dwr.util.getValue("address");
  EmailValidator.isValid(address, function(valid) {
    dwr.util.setValue("addressError", valid ? "" : "Please enter a valid email address");
  });
}

function process() {
  var address = dwr.util.getValue("address");
  var name = dwr.util.getValue("name");
  Generator.generateAntiSpamMailto(name, address, function(contents) {
    dwr.util.setValue("outputFull", contents, { escapeHtml:false });
    dwr.util.byId("output").style.display = "block";
  });
}
