new Vue({
  el: "#app",
  data: {
    message: "from HtmlUnit"
  },
  methods: {
    do_click: function (event) {
      alert('Hello ' + this.message + '!')
    }
  }
});