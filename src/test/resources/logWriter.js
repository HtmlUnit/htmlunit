function addLogToBody()
{
	var log = document.htmlUnitLog;
	if (log.alreadyAdded)
		return;
	if (document.body)
	{
		document.body.appendChild(log);
		log.alreadyAdded = true;
	}
	else if (window.addEventListener)
        window.addEventListener('load', addLogToBody, true);
	else
        window.attachEvent('onload', addLogToBody);
}
function log(text)
{
	var log = document.htmlUnitLog;
	if (log == null)
	{
		log = document.createElement('ol');
		log.id = "log";
		document.htmlUnitLog = log;
	}
	addLogToBody(log);
	var item = document.createElement("li");
	item.appendChild(document.createTextNode(String(text)))
	log.appendChild(item);
}