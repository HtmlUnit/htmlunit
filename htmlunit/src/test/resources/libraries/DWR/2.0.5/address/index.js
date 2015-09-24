
function fillAddress() {
  var postcode = dwr.util.getValue("postcode");
  AddressLookup.fillAddress(postcode, function(address) {
    dwr.util.setValue("line2", address.line2);
    dwr.util.setValue("line3", address.line3);
    dwr.util.setValue("line4", address.line4);
  });
}
