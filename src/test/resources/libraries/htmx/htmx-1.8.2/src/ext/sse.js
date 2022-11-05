/*
Server Sent Events Extension
============================
This extension adds support for Server Sent Events to htmx.  See /www/extensions/sse.md for usage instructions.

*/

(function(){

	/** @type {import("../htmx").HtmxInternalApi} */
	var api;

	htmx.defineExtension("sse", {

		/**
		 * Init saves the provided reference to the internal HTMX API.
		 * 
		 * @param {import("../htmx").HtmxInternalApi} api 
		 * @returns void
		 */
		init: function(apiRef) {
			// store a reference to the internal API.
			api = apiRef;

			// set a function in the public API for creating new EventSource objects
			if (htmx.createEventSource == undefined) {
				htmx.createEventSource = createEventSource;
			}
		},

		/**
		 * onEvent handles all events passed to this extension.
		 * 
		 * @param {string} name 
		 * @param {Event} evt 
		 * @returns void
		 */
		onEvent: function(name, evt) {

			switch (name) {

			// Try to remove remove an EventSource when elements are removed
			case "htmx:beforeCleanupElement":
				var internalData = api.getInternalData(evt.target)
				if (internalData.sseEventSource) {
					internalData.sseEventSource.close();
				}
				return;

			// Try to create EventSources when elements are processed
			case "htmx:afterProcessNode":
				createEventSourceOnElement(evt.target);
			}
		}
	});

	///////////////////////////////////////////////
	// HELPER FUNCTIONS
	///////////////////////////////////////////////


	/**
	 * createEventSource is the default method for creating new EventSource objects.
	 * it is hoisted into htmx.config.createEventSource to be overridden by the user, if needed.
	 * 
	 * @param {string} url 
	 * @returns EventSource
	 */
	 function createEventSource(url) {
		return new EventSource(url, {withCredentials:true});
	}

	function splitOnWhitespace(trigger) {
		return trigger.trim().split(/\s+/);
	}

	function getLegacySSEURL(elt) {
		var legacySSEValue = api.getAttributeValue(elt, "hx-sse");
		if (legacySSEValue) {
			var values = splitOnWhitespace(legacySSEValue);
			for (var i = 0; i < values.length; i++) {
				var value = values[i].split(/:(.+)/);
				if (value[0] === "connect") {
					return value[1];
				}
			}
		}
	}

	function getLegacySSESwaps(elt) {
		var legacySSEValue = api.getAttributeValue(elt, "hx-sse");
		var returnArr = [];
		if (legacySSEValue) {
			var values = splitOnWhitespace(legacySSEValue);
			for (var i = 0; i < values.length; i++) {
				var value = values[i].split(/:(.+)/);
				if (value[0] === "swap") {
					returnArr.push(value[1]);
				}
			}
		}
		return returnArr;
	}

	/**
	 * createEventSourceOnElement creates a new EventSource connection on the provided element.
	 * If a usable EventSource already exists, then it is returned.  If not, then a new EventSource
	 * is created and stored in the element's internalData.
	 * @param {HTMLElement} elt
	 * @param {number} retryCount
	 * @returns {EventSource | null}
	 */
	function createEventSourceOnElement(elt, retryCount) {

		if (elt == null) {
			return null;
		}

		var internalData = api.getInternalData(elt);

		// get URL from element's attribute
		var sseURL = api.getAttributeValue(elt, "sse-connect");


		if (sseURL == undefined) {
			var legacyURL = getLegacySSEURL(elt)
			if (legacyURL) {
				sseURL = legacyURL;
			} else {
				return null;
			}
		}

		// Connect to the EventSource
		var source = htmx.createEventSource(sseURL);
		internalData.sseEventSource = source;

		// Create event handlers
		source.onerror = function (err) {

			// Log an error event
			api.triggerErrorEvent(elt, "htmx:sseError", {error:err, source:source});

			// If parent no longer exists in the document, then clean up this EventSource
			if (maybeCloseSSESource(elt)) {
				return;
			}

			// Otherwise, try to reconnect the EventSource
			if (source.readyState === EventSource.CLOSED) {
				retryCount = retryCount || 0;
				var timeout = Math.random() * (2 ^ retryCount) * 500;
				window.setTimeout(function() {
					createEventSourceOnElement(elt, Math.min(7, retryCount+1));
				}, timeout);
			}			
		};
		
		// Add message handlers for every `sse-swap` attribute
		queryAttributeOnThisOrChildren(elt, "sse-swap").forEach(function(child) {

			var sseSwapAttr = api.getAttributeValue(child, "sse-swap");
			if (sseSwapAttr) {
				var sseEventNames = sseSwapAttr.split(",");
			} else {
				var sseEventNames = getLegacySSESwaps(child);
			}

			for (var i = 0 ; i < sseEventNames.length ; i++) {
				var sseEventName = sseEventNames[i].trim();
				var listener = function(event) {

					// If the parent is missing then close SSE and remove listener
					if (maybeCloseSSESource(elt)) {
						source.removeEventListener(sseEventName, listener);
						return;
					}

					// swap the response into the DOM and trigger a notification
					swap(child, event.data);
					api.triggerEvent(elt, "htmx:sseMessage", event);
				};

				// Register the new listener
				api.getInternalData(elt).sseEventListener = listener;
				source.addEventListener(sseEventName, listener);
			}
		});

		// Add message handlers for every `hx-trigger="sse:*"` attribute
		queryAttributeOnThisOrChildren(elt, "hx-trigger").forEach(function(child) {

			var sseEventName = api.getAttributeValue(child, "hx-trigger");
			if (sseEventName == null) {
				return;
			}

			// Only process hx-triggers for events with the "sse:" prefix
			if (sseEventName.slice(0, 4) != "sse:") {
				return;
			}

			var listener = function(event) {

				// If parent is missing, then close SSE and remove listener
				if (maybeCloseSSESource(elt)) {
					source.removeEventListener(sseEventName, listener);
					return;
				}

				// Trigger events to be handled by the rest of htmx
				htmx.trigger(child, sseEventName, event);
				htmx.trigger(child, "htmx:sseMessage", event);
			}

			// Register the new listener
			api.getInternalData(elt).sseEventListener = listener;
			source.addEventListener(sseEventName.slice(4), listener);
		});
	}

	/**
	 * maybeCloseSSESource confirms that the parent element still exists.
	 * If not, then any associated SSE source is closed and the function returns true.
	 * 
	 * @param {HTMLElement} elt 
	 * @returns boolean
	 */
	function maybeCloseSSESource(elt) {
		if (!api.bodyContains(elt)) {
			var source = api.getInternalData(elt).sseEventSource;
			if (source != undefined) {
				source.close();
				// source = null
				return true;
			}
		}
		return false;
	}

	/**
	 * queryAttributeOnThisOrChildren returns all nodes that contain the requested attributeName, INCLUDING THE PROVIDED ROOT ELEMENT.
	 * 
	 * @param {HTMLElement} elt 
	 * @param {string} attributeName 
	 */
	function queryAttributeOnThisOrChildren(elt, attributeName) {

		var result = [];

		// If the parent element also contains the requested attribute, then add it to the results too.
		if (api.hasAttribute(elt, attributeName) || api.hasAttribute(elt, "hx-sse")) {
			result.push(elt);
		}

		// Search all child nodes that match the requested attribute
		elt.querySelectorAll("[" + attributeName + "], [data-" + attributeName + "], [hx-sse], [data-hx-sse]").forEach(function(node) {
			result.push(node);
		});

		return result;
	}

	/**
	 * @param {HTMLElement} elt
	 * @param {string} content 
	 */
	function swap(elt, content) {

		api.withExtensions(elt, function(extension) {
			content = extension.transformResponse(content, null, elt);
		});

		var swapSpec = api.getSwapSpecification(elt);
		var target = api.getTarget(elt);
		var settleInfo = api.makeSettleInfo(elt);

		api.selectAndSwap(swapSpec.swapStyle, target, elt, content, settleInfo);

		settleInfo.elts.forEach(function (elt) {
			if (elt.classList) {
				elt.classList.add(htmx.config.settlingClass);
			}
			api.triggerEvent(elt, 'htmx:beforeSettle');
		});

		// Handle settle tasks (with delay if requested)
		if (swapSpec.settleDelay > 0) {
			setTimeout(doSettle(settleInfo), swapSpec.settleDelay);
		} else {
			doSettle(settleInfo)();
		}
	}

	/**
	 * doSettle mirrors much of the functionality in htmx that 
	 * settles elements after their content has been swapped.
	 * TODO: this should be published by htmx, and not duplicated here
	 * @param {import("../htmx").HtmxSettleInfo} settleInfo 
	 * @returns () => void
	 */
	function doSettle(settleInfo) {

		return function() {
			settleInfo.tasks.forEach(function (task) {
				task.call();
			});

			settleInfo.elts.forEach(function (elt) {
				if (elt.classList) {
					elt.classList.remove(htmx.config.settlingClass);
				}
				api.triggerEvent(elt, 'htmx:afterSettle');
			});
		}
	}

})();