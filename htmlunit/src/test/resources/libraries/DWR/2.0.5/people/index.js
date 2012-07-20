
function init() {
  dwr.util.useLoadingMessage();
  Tabs.init('tabList', 'tabContents');
  fillTable();
}

var peopleCache = { };
var viewed = -1;

function fillTable() {
  People.getAllPeople(function(people) {
    // Delete all the rows except for the "pattern" row
    dwr.util.removeAllRows("peoplebody", { filter:function(tr) {
      return (tr.id != "pattern");
    }});
    // Create a new set cloned from the pattern row
    var person, id;
    people.sort(function(p1, p2) { return p1.name.localeCompare(p2.name); });
    for (var i = 0; i < people.length; i++) {
      person = people[i];
      id = person.id;
      dwr.util.cloneNode("pattern", { idSuffix:id });
      dwr.util.setValue("tableName" + id, person.name);
      dwr.util.setValue("tableSalary" + id, person.salary);
      dwr.util.setValue("tableAddress" + id, person.address);
      $("pattern" + id).style.display = ""; // officially we should use table-row, but IE prefers "" for some reason
      peopleCache[id] = person;
    }
  });
}

function editClicked(eleid) {
  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
  var person = peopleCache[eleid.substring(4)];
  dwr.util.setValues(person);
}

function deleteClicked(eleid) {
  // we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
  var person = peopleCache[eleid.substring(6)];
  if (confirm("Are you sure you want to delete " + person.name + "?")) {
    dwr.engine.beginBatch();
    People.deletePerson({ id:person.id });
    fillTable();
    dwr.engine.endBatch();
  }
}

function writePerson() {
  var person = { id:viewed, name:null, address:null, salary:null };
  dwr.util.getValues(person);

  dwr.engine.beginBatch();
  People.setPerson(person);
  fillTable();
  dwr.engine.endBatch();
}

function clearPerson() {
  viewed = -1;
  dwr.util.setValues({ id:-1, name:null, address:null, salary:null });
}
