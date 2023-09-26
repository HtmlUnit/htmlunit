describe("Core htmx AJAX Tests", function(){
    beforeEach(function() {
        this.server = makeServer();
        clearWorkArea();
    });
    afterEach(function()  {
        this.server.restore();
        clearWorkArea();
    });

    it('properly handles inputs external to form', function () {
        var values;
        this.server.respondWith("Post", "/test", function (xhr) {
            values = getParameters(xhr);
            xhr.respond(204, {}, "");
        });

        make('<form id="externalForm" hx-post="/test">' +
            '   <input type="hidden" name="b1" value="inputValue">' +
            '</form>' +
            '<input type="text" name="t1" value="textValue" form="externalForm">' +
            '<select name="s1" form="externalForm">' +
            '   <option value="someValue"></option>' +
            '   <option value="selectValue" selected></option>' +
            '</select>' +
            '<button id="submit" form="externalForm" type="submit" name="b1" value="buttonValue">button</button>');

        byId("submit").click();
        this.server.respond();
console.log('#############'  + JSON.stringify(values));
        values.should.deep.equal({t1: 'textValue', b1: ['inputValue', 'buttonValue'], s1: "selectValue"});
    })
})
