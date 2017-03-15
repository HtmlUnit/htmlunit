// permits us to generate an html file testing that the generated alerts are as expected

var htmlunitReserved_i = 0;
function htmlunitReserved_caughtAlert(_str)
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

function htmlunitReserved_addSummaryAfterOnload()
{
	var originalOnload = window.onload;
	window.onload = function() {
			if (originalOnload)
				originalOnload();
			htmlunitReserved_displaySummary();
		};
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
		if (oResult.expected == String(oResult.received))
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
	alert(str);
}
