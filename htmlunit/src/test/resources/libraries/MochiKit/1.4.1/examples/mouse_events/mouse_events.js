/*

    Mouse Events: Simple Mouse Scrolling Handlers

*/

function showMouseEvent(evt) {
    getElement("show-mouse-event-repr").innerHTML = repr(evt);
    evt.stop();
};

function stopPageFromScrolling( event ) {
  var src = event.src();
  var scrollTop = src.scrollTop;

  //  While trying to stop scrolling events for IE, found that it
  //  jumped around a bit.  The following fudgetFactor is NOT the way
  //  to handle this but was the best I could do with the limited time
  //  I had.
  var fudgeFactor = /MSIE/.test(navigator.userAgent) ? 25 : 0;

  // scrolling up
  if (event.mouse().wheel.y < 0) {
    // Following test should probably be "if (scrollTop == 0)" which
    // works in FF but not IE.
    if (scrollTop <= fudgeFactor) {
      event.stop();
    }
  }
  //..scrolling down
  else {
    // Following test should be "if (scrollTop == src.scrollHeight - src.clientHeight)",
    // see comment above.
    if (src.scrollHeight <= (scrollTop + src.clientHeight + fudgeFactor)) {
      event.stop();
    }
  }
};

function connectEvents(){
  connect("show-mouse-event", "onmousemove", showMouseEvent);
  connect("show-mouse-event", "onmousedown", showMouseEvent);
  connect("show-mouse-event", "onmouseup", showMouseEvent);
  connect("show-mouse-event", "onmousewheel", showMouseEvent);
  connect("no-scroll-page", "onmousewheel", stopPageFromScrolling);
};

connect(window, 'onload',
    function() {
          connectEvents();
          var elems = getElementsByTagAndClassName("A", "view-source");
          var page = "mouse_events/";
          for (var i = 0; i < elems.length; i++) {
            var elem = elems[i];
            var href = elem.href.split(/\//).pop();
            elem.target = "_blank";
            elem.href = "../view-source/view-source.html#" + page + href;
          }
    });
