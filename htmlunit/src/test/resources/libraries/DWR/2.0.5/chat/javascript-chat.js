
function init() {
  dwr.engine.setActiveReverseAjax(true);
}

function sendMessage() {
  var text = dwr.util.getValue("text");
  dwr.util.setValue("text", "");
  JavascriptChat.addMessage(text);
}

function receiveMessages(messages) {
  var chatlog = "";
  for (var data in messages) {
    chatlog = "<div>" + dwr.util.escapeHtml(messages[data].text) + "</div>" + chatlog;
  }
  dwr.util.setValue("chatlog", chatlog, { escapeHtml:false });
}
