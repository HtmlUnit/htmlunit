// permits to generate html file testing that the generated alerst are as expected
// currently only works with Mozilla
var htmlunitReserved_alert = alert;

var htmlunitReserved_i = 0;
function htmlunitReserved_catchedAlert(_str)
{
	var tab = htmlunitReserved_tab;
	var index = htmlunitReserved_i++;
	if (index < tab.length)
	{
		tab[index].received = _str;
	}
	else
	{
		tab[index] = {received: _str};
	}
}
alert = htmlunitReserved_catchedAlert;
if (addEventListener)
	addEventListener('load', htmlunitReserved_addSummaryAfterOnload, true);
else
	attachEvent("submit", htmlunitReserved_addSummaryAfterOnload);

function htmlunitReserved_addSummaryAfterOnload()
{
	var originalOnload = window.onload; 
	window.onload = function() {originalOnload(); htmlunitReserved_displaySummary();}
}

function htmlunitReserved_displaySummary()
{
	var str = "Results:\n";
	var tab = htmlunitReserved_tab;
	var iNbFailure = 0;
	var iNbOk = 0;
	for (var i=0; i<tab.length; ++i)
	{
		var oResult = tab[i];
		str += i + "- expected: ";
		if (oResult.expected == undefined)
			str += "-- none --";
		else
			str += oResult.expected;

		str += ", found: " + oResult.received + " => ";
		if (oResult.expected == oResult.received)
		{
			iNbOk++;
			str += "ok";
		}
		else
		{
			iNbFailure++;
			str += "FAILURE";
		}
		str += "\n";
	}
	str += "\n\n===> Result: " + ((iNbFailure == 0) ? "success" : "FAILED");
	htmlunitReserved_alert(str);
}
