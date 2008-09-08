function log(text)
{
	var log = document.getElementById('log');
	if (log == null)
	{
		log = document.createElement('ol');
		log.id = "log";
		document.body.appendChild(log);
	}
	var item = document.createElement("li");
	item.appendChild(document.createTextNode(String(text)))
	log.appendChild(item);
}