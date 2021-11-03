describe("hx-vars attribute", function() {
    beforeEach(function () {
        this.server = makeServer();
        clearWorkArea();
    });
    afterEach(function () {
        this.server.restore();
        clearWorkArea();
    });

    it('basic hx-vars works', function () {
        this.server.respondWith("POST", "/vars", function (xhr) {
            var params = getParameters(xhr);
            params['i1'].should.equal("test");
            xhr.respond(200, {}, "Clicked!")
        });
        var div = make('<div hx-post="/vars" hx-vars="i1:\'test\'"></div>')
        div.click();
        this.server.respond();
        div.innerHTML.should.equal("Clicked!");
    });

    it('hx-vars works with braces', function () {
        this.server.respondWith("POST", "/vars", function (xhr) {
            var params = getParameters(xhr);
            params['i1'].should.equal("test");
            xhr.respond(200, {}, "Clicked!")
        });
        var div = make('<div hx-post="/vars" hx-vars="{i1:\'test\'}"></div>')
        div.click();
        this.server.respond();
        div.innerHTML.should.equal("Clicked!");
    });

    it('multiple hx-vars works', function () {
        this.server.respondWith("POST", "/vars", function (xhr) {
            var params = getParameters(xhr);
            params['v1'].should.equal("test");
            params['v2'].should.equal("42");
            xhr.respond(200, {}, "Clicked!")
        });
        var div = make('<div hx-post="/vars" hx-vars="v1:\'test\', v2:42"></div>')
        div.click();
        this.server.respond();
        div.innerHTML.should.equal("Clicked!");
    });

    it('hx-vars can be on parents', function () {
        this.server.respondWith("POST", "/vars", function (xhr) {
            var params = getParameters(xhr);
            params['i1'].should.equal("test");
            xhr.respond(200, {}, "Clicked!")
        });
        make('<div hx-vars="i1:\'test\'"><div id="d1" hx-post="/vars"></div></div>')
        var div = byId("d1");
        div.click();
        this.server.respond();
        div.innerHTML.should.equal("Clicked!");
    });

    it('hx-vars can override parents', function () {
        this.server.respondWith("POST", "/vars", function (xhr) {
            var params = getParameters(xhr);
            params['i1'].should.equal("best");
            xhr.respond(200, {}, "Clicked!")
        });
        make('<div hx-vars="i1:\'test\'"><div id="d1" hx-vars="i1:\'best\'" hx-post="/vars"></div></div>')
        var div = byId("d1");
        div.click();
        this.server.respond();
        div.innerHTML.should.equal("Clicked!");
    });

    it('hx-vars overrides inputs', function () {
        this.server.respondWith("POST", "/include", function (xhr) {
            var params = getParameters(xhr);
            params['i1'].should.equal("best");
            xhr.respond(200, {}, "Clicked!")
        });
        var div = make('<div hx-target="this"><input hx-post="/include" hx-vars="i1:\'best\'" hx-trigger="click" id="i1" name="i1" value="test"/></div>')
        var input = byId("i1")
        input.click();
        this.server.respond();
        div.innerHTML.should.equal("Clicked!");
    });

});