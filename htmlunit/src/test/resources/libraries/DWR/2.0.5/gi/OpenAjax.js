/*******************************************************************************
 * OpenAjax.js / OpenAjaxBootstrap.js:
 *
 * Component of the OpenAjax Hub, as specified by OpenAjax Alliance.
 * Specification is under development at: 
 *
 *   http://www.openajax.org/member/wiki/OpenAjax_Hub_Specification
 *
 * In the current implementation, the OpenAjax.js file is created by simply 
 * concatenating the individual component JavaScript files in a particular order, with
 * where this file must be the first one in the list.
 *
 * Copyright 2006 OpenAjax Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at http://www.apache.org/licenses/LICENSE-2.0 . Unless 
 * required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *
 ******************************************************************************/

/* Global variable that is named so that it is unique to this particular implementation/version.
	"oahri" = OpenAjax Hub reference implementation. "0_1" = release/version 0.1. */
var oahri_0_2 = "oahri_0_2";

/*
 * OpenAjax Hub configuration parameters.
 * These configuration parameters are meant for use by the application developer,
 * not the Ajax runtime library developer. 
 *
 * The following is a quick summary of available parameters.
 * Refer to the OpenAjax Hub Specification for the official and detailed definition of these features.
 *
 * bootstrapFile = string 
 *		Holds name of JavaScript file that initializes OpenAjax object and includes definition of
 *		OpenAjax.require() function for dynamic module loading.
 * idsToScan = array of strings (Default is [])
 *		Provides to the OpenAjax Hub a list of element IDs upon which to invoke the
 *		markup scanner.
 * scanPage = boolean (Default is true)
 *		Tells the OpenAjax Hub whether to auto-invoke its markup scanner 
 *		on the BODY element in the document.
 */
if (typeof OpenAjaxConfig == "undefined") {
	OpenAjaxConfig = {};
}
if (typeof OpenAjaxConfig.bootstrapFile == "undefined") {
	OpenAjaxConfig.bootstrapFile = "OpenAjaxBootstrap.js";
}
if (typeof OpenAjaxConfig.idsToScan == "undefined") {
	OpenAjaxConfig.idsToScan = [];
}
if (typeof OpenAjaxConfig.scanPage == "undefined") {
	OpenAjaxConfig.scanPage = true;
}


/*
 * Only create an OpenAjax object if it doesn't exist already.
 * (If it already exists, then assume the prior instance provides the services that are needed.
 * Also, attempts to initialize a second time probably will clobber key data from existing OpenAjax object.)
 */
if (typeof window.OpenAjax == "undefined") {

	OpenAjax = {

		implementer: "http://openajaxallianc.sourceforge.net",
		implVersion: "0.2",
		specVersion: "0.2",
		implExtraData: {},
		_allModules: "Libraries,LoadUnload,Globals,PublishSubscribe,MarkupScanner",
		_oahri: oahri_0_2,	// Private variable. Holds unique ID string for this impl/version.
		_error: null		// Private variable. When not null, things are messed up.
	};

	// Private function. If module has not been loaded already, load it now.
	OpenAjax._loadModule = function(module) {
		var modname = OpenAjax._allModules.match(module);
		if (modname) {
			if (modname=="Libraries" && typeof OpenAjax.registerLibrary!="undefined") {
				modname=null;
			}
			if (modname=="Globals" && typeof OpenAjax.registerGlobals!="undefined") {
				modname=null;
			}
			if (modname=="LoadUnload" && typeof OpenAjax.addOnLoad!="undefined") {
				modname=null;
			}
			if (modname=="PublishSubscribe" && typeof OpenAjax.subscribe!="undefined") {
				modname=null;
			}
			if (modname=="MarkupScanner" && typeof OpenAjax.registerCustomScannerCB!="undefined") {
				modname=null;
			}
			if (modname) {
				var fname=OpenAjax._path+modname+".js";
				// inserting via DOM fails in Safari 2.0, so brute force approach
				document.write('<scr'+'ipt language="JavaScript" type="text/javascript" src="'+fname+'"></script>');
			}
		}
	}

	// Public function. Loads the given comma-separated list of modules (if not already loaded).
	OpenAjax.require = function(modules) {
		var modarray = modules.split(',');
		for (var i=0; i<modarray.length; i++) {
			OpenAjax._loadModule(modarray[i]);
		}
	}

	/* Find path to bootstrap file (for case where bootstrapping approach is used). */
	var script_elems = document.getElementsByTagName("script");
	OpenAjax._path = null;
	for (var i=0;i<script_elems.length;i++) {
		var s = script_elems[i];
		var re = new RegExp(OpenAjaxConfig.bootstrapFile+".*$");
		if (s.src && s.src.match(re)) {
			// FIXME: We need automated tests for all of this bootstrapping logic.
			// FIXME: load= has yet to be tested. May not work at all.
			OpenAjax._path = s.src.replace(re,'');
			var requiredModules = s.src.match(/\?.*load=([A-Za-z,]*)/);
			if (requiredModules) {
				OpenAjax.require(requiredModules);
			} else {
				OpenAjax.require(OpenAjax._allModules);
			}
			break;
		}
	}

}

/* For single-file distribution of entire Hub, the other OpenAjax Hub JavaScript files 
	should be concatenated after this comment into file OpenAjax.js. */


/*******************************************************************************
 * Libraries.js:
 *
 * A component of the OpenAjax Hub, as specified by OpenAjax Alliance.
 * Specification is under development at: 
 *
 *   http://www.openajax.org/member/wiki/OpenAjax_Hub_Specification
 *
 * This file manages the Ajax libraries that register themselves with
 * the OpenAjax Hub via the OpenAjax.registerLibrary() method.
 *
 * The logic from OpenAjaxBootstrap.js must have been loaded before this file.
 *
 * Copyright 2006 OpenAjax Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at http://www.apache.org/licenses/LICENSE-2.0 . Unless 
 * required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *
 ******************************************************************************/

/* Only execute the code in this file if OpenAjax object exists, 
	this module hasn't been loaded yet, and module comes from same implementation. */
if (typeof OpenAjax!='undefined' &&
	typeof OpenAjax.registerLibrary=='undefined' &&
	typeof OpenAjax._oahri!='undefined' && OpenAjax._oahri == oahri_0_2) {

	// Private variable, holds the libraries that have been registered so far.
	OpenAjax._libraries = { };

	/*
	 *	Registers an Ajax library with the OpenAjax Hub.
	 */
	OpenAjax.registerLibrary = function (
			prefix,			// Unique library ID string, suitable as prefix in xmlns declaration
			namespaceURI,   // Namespace URI string suitable for use with xmlns attribute
			version,		// Library version in the form of #[.#]*, such as "1", "1.1" or "1.20.2".
			extraData){		// Optional, can be null. Arbitrary Object holding extra info about library.

		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}
		if (this._libraries[prefix]) {
			throw new Error("Attempt to register library '" + prefix + "' which has already been registered.");
		} else {
			this._libraries[prefix] = {
				prefix: prefix,
				namespaceURI: namespaceURI,
				version: version,
				extraData: extraData
			};
		}

	}

	/*
	 *	Unregisters an Ajax library with the OpenAjax Hub.
	 */

	OpenAjax.unregisterLibrary = function(
			prefix ){			// Library prefix that was passed to registerLibrary().

		if (!this._libraries[prefix]) {
			throw new Error("Attempt to unregister a library '" + prefix + "' that has not been registered.");
		} else {
			// Remove references to any globals registered to this library.
			// (See Globals.js)
			if (this._globals && this._globals[prefix])
				delete this._globals[prefix];
			
			// Remove registration for this library.
			delete this._libraries[prefix];
		}

	},

	/*
	 * Returns the list of registered libraries.
	 */
	OpenAjax.queryLibraries = function( ){
		return this._deepclone(this._libraries);
	}

	/*
	 * If a library with the given prefix has been registered, then returns an object
	 * with properties passed to registerLibrary(). If not registered, returns null.
	 */
	OpenAjax.queryLibrary = function(
			prefix){

		/*
		 * prefix:
		 *		The unique string that identifies the library being registered.
		 */

		if (this._libraries[prefix]) {
			return this._deepclone(this._libraries[prefix]);
		} else {
			return null;
		}

	}

	// -------------------------- PRIVATE FUNCTIONS ------------------------

	/*
	 * Private function that returns a deep clone of a given object.
	 */
	OpenAjax._deepclone = function(
			object) {		// object to clone

		var objectClone = new object.constructor();
		for (var property in object) {
			if (typeof this[property] == 'object')
				objectClone[property] = _deepclone(object[property]);
			else
				objectClone[property] = object[property];
		}
		return objectClone;
	}

	// Register the OpenAjax Hub itself as a library.
	OpenAjax.registerLibrary("OpenAjax", "http://www.openajax.org/hub", "0.1", {});
}
/*******************************************************************************
 * LoadUnload.js:
 *
 * A component of the OpenAjax Hub, as specified by OpenAjax Alliance.
 * Specification is under development at: 
 *
 *   http://www.openajax.org/member/wiki/OpenAjax_Hub_Specification
 *
 * This file manages load and unload event handlers on the BODY element.
 *
 * The logic from OpenAjaxBootstrap.js must have been loaded before this file.
 *
 * Copyright 2006 OpenAjax Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at http://www.apache.org/licenses/LICENSE-2.0 . Unless 
 * required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *
 ******************************************************************************/

/* Only execute the code in this file if OpenAjax object exists, 
	this module hasn't been loaded yet, and module comes from same implementation. */
if (typeof OpenAjax!='undefined' &&
	typeof OpenAjax.addOnLoad=='undefined' &&
	typeof OpenAjax._oahri!='undefined' && OpenAjax._oahri == oahri_0_2) {

	// Public functions that are targeted for automated testing purposes and not meant
	// for normal applications development. Clears list of registered load/unload handlers.
	OpenAjax.removeAllOnLoad = function() {
		// Private variables that hold the lists of load callbacks that have been registered.
		OpenAjax._libraryLoadCBs = [];
		OpenAjax._componentLoadCBs = [];
		OpenAjax._applicationLoadCBs = [];
	}
	// Initialize the private arrays.
	OpenAjax.removeAllOnLoad(); 

	OpenAjax.removeAllOnUnload = function() {
		// Private variables that hold the lists of unad callbacks that have been registered.
		OpenAjax._libraryUnloadCBs = [];
		OpenAjax._componentUnloadCBs = [];
		OpenAjax._applicationUnloadCBs = [];
	}
	// Initialize the private arrays.
	OpenAjax.removeAllOnUnload();

	/*
	 * Registers an event handler non-destructively to be fired
	 * when the window's onload handler is executed
	 */
	OpenAjax.addOnLoad = function(	
			refOrName,	// callback function or string that holds name of function
			scope,		// object, defaults is window
			phase){		// "library", "component", or "application", default is "application"

		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}
		if (arguments.length < 1)
			throw new Error("Insufficient number of arguments");
		if(!scope){
			scope = window;
		}
		if(typeof refOrName == "string"){
			// get a function object
			refOrName = scope[refOrName];
		}
		if(typeof refOrName != "function"){
			throw new Error("invalid function reference passed to OpenAjax.addOnLoad()");
		}
		if (!phase || phase=="application") {
			OpenAjax._applicationLoadCBs.push(refOrName);
		} else if (phase == "library") {
			OpenAjax._libraryLoadCBs.push(refOrName);
		} else if (phase == "component") {
			OpenAjax._componentLoadCBs.push(refOrName);
		} else {
			throw new Error("invalid phase value passed to OpenAjax.addOnLoad()");
		}
	}

	/*
	 * Registers an event handler non-destructively to be fired
	 * when the window's onunload handler is executed
	 */
	OpenAjax.addOnUnload = function(
			refOrName,	// callback function or string that holds name of function
			scope,		// object, defaults is window
			phase){		// "library", "component", or "application", default is "application"

		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}
		if (arguments.length < 1)
			throw new Error("Insufficient number of arguments");
		if(!scope){
			scope = window;
		}
		if(typeof refOrName == "string"){
			// get a function object
			refOrName = scope[refOrName];
		}
		if(typeof refOrName != "function"){
			throw new Error("invalid function reference passed to OpenAjax.addOnUnload()");
		}
		if (!phase || phase=="application") {
			OpenAjax._applicationUnloadCBs.push(refOrName);
		} else if (phase == "library") {
			OpenAjax._libraryUnloadCBs.push(refOrName);
		} else if (phase == "component") {
			OpenAjax._componentUnloadCBs.push(refOrName);
		} else {
			throw new Error("invalid phase value passed to OpenAjax.addOnUnload()");
		}
	}

	/*
	 * Private function that attaches an event listener for a given event
	 * on the specified target object.
	 */
	OpenAjax._addOnEvt = function(
			type,		// event type string
			refOrName,	// callback function or string that holds name of function
			scope){		// object, defaults is window
		if(!scope){ scope = window; }

		var funcRef = refOrName;
		if(typeof refOrName == "string"){
			funcRef = scope[refOrName];
		}
		var enclosedFunc = function(){ return funcRef.apply(scope, arguments); };

		if(window["attachEvent"]){
			window.attachEvent("on"+type, enclosedFunc);
		} else if(window["addEventListener"]){
			window.addEventListener(type, enclosedFunc, false);
		} else if(document["addEventListener"]){
			document.addEventListener(type, enclosedFunc, false);
		}
	}

	/*
	 * This is the OpenAjax onload event handler that the OpenAjax library
	 * registers on the BODY element.
	 */
	OpenAjax.OnLoadHandler = function() {
		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}

		// Invoke registered load event handlers for "library" phase.
		if (OpenAjax._libraryLoadCBs.length) {
			for (var i=0; i < OpenAjax._libraryLoadCBs.length; i++) {
				OpenAjax._libraryLoadCBs[i].call();
			}
		}
		// Invoke registered load event handlers for "component" phase.
		if (OpenAjax._componentLoadCBs.length) {
			for (var i=0; i < OpenAjax._componentLoadCBs.length; i++) {
				OpenAjax._componentLoadCBs[i].call();
			}
		}
		// Invoke markup scanner.
		OpenAjax.scanDocument();
		// Invoke registered load event handlers for "application" phase.
		if (OpenAjax._applicationLoadCBs.length) {
			for (var i=0; i < OpenAjax._applicationLoadCBs.length; i++) {
				OpenAjax._applicationLoadCBs[i].call();
			}
		}
	}


	/*
	 * This is the OpenAjax onunload event handler that the OpenAjax library
	 * registers on the BODY element.
	 */
	OpenAjax.OnUnloadHandler = function() {
		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}

		// Invoke registered unload event handlers for "application" phase.
		if (OpenAjax._applicationUnloadCBs.length) {
			for (var i=0; i < OpenAjax._applicationUnloadCBs.length; i++) {
				OpenAjax._applicationUnloadCBs[i].call();
			}
		}
		// Invoke registered unload event handlers for "component" phase.
		if (OpenAjax._componentUnloadCBs.length) {
			for (var i=0; i < OpenAjax._componentUnloadCBs.length; i++) {
				OpenAjax._componentUnloadCBs[i].call();
			}
		}
		// Invoke registered unload event handlers for "library" phase.
		if (OpenAjax._libraryUnloadCBs.length) {
			for (var i=0; i < OpenAjax._libraryUnloadCBs.length; i++) {
				OpenAjax._libraryUnloadCBs[i].call();
			}
		}
	}

	/*
	 * Register the OpenAjax load and unload event handlers on the BODY element.
	 */
	OpenAjax._addOnEvt("load", OpenAjax.OnLoadHandler);
	OpenAjax._addOnEvt("unload", OpenAjax.OnUnloadHandler);

}
/*******************************************************************************
 * Globals.js:
 *
 * A component of the OpenAjax Hub, as specified by OpenAjax Alliance.
 * Specification is under development at: 
 *
 *   http://www.openajax.org/member/wiki/OpenAjax_Hub_Specification
 *
 * This file manages the global objects that are added to the JavaScript
 * runtime environment by the Ajax libraries.
 *
 * Includes function OpenAjax.globalsCollisionCheck(globals, prefix), 
 * which determines whether the list of global objects in array 'globals'
 * collides with any of the global objects used by currently
 * registered Ajax libraries.
 *
 * The logic from OpenAjaxBootstrap.js must have been loaded before this file.
 *
 * Copyright 2006 OpenAjax Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at http://www.apache.org/licenses/LICENSE-2.0 . Unless 
 * required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *
 ******************************************************************************/

/* Only execute the code in this file if OpenAjax object exists, 
	this module hasn't been loaded yet, and module comes from same implementation. */
if (typeof OpenAjax!='undefined' &&
	typeof OpenAjax.registerGlobals=='undefined' &&
	typeof OpenAjax._oahri!='undefined' && OpenAjax._oahri == oahri_0_2) {

	// Private variable, holds the global objects that have been registered so far.
	OpenAjax._globals = { };

	/*
	 *	Identifies the global objects that the given Ajax library is adding to
	 *	the JavaScript runtime environment.
	 */
	OpenAjax.registerGlobals = function (
			prefix,			// Library prefix that was passed to registerLibrary().
			globals){		// An array of strings that identify the globals added 
							// to the JavaScript runtime environment by this library.
		var errorMessage;

		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}

		if (this._globals[prefix]) {
			throw new Error("Attempt to register globals for library '" + prefix + "', but globals have already been registered.");
		} else {
			// If file GlobalsCollisionCheck.js has been loaded (and thus this.globalsCollisionCheck exists),
			// then check the list of globals against the previously registered globals.
			// If any collisions are found, a runtime error will be raised.
			// FIXME: Why make GlobalsCollisionCheck.js an optional component? Remove this "if".
			if (this.globalsCollisionCheck) {
				errorMessage = this.globalsCollisionCheck(globals, prefix);
				if (errorMessage) {
					throw new Error(errorMessage);
				}
			}

			// Everything is OK, so register the globals for this library.
			this._globals[prefix] = globals;
		}
	}

	/*
	 *	Returns the list of globals registered by a given library.
	 */
	OpenAjax.getGlobals = function (
			prefix){			// Library prefix that was passed to registerLibrary().

		if (!this._globals[prefix]) {
			return null;
		} else {
			return this._globals[prefix];
		}
	}


	/*
	 *	Determines whether the list of global objects in array 'candidate_globals'
	 *	collides with any of the global objects that have been listed by currently
	 *	registered Ajax libraries.
	 */
	OpenAjax.globalsCollisionCheck = function (
			candidate_globals,	// An array of strings that identify the globals against which we want to check
			prefix){			// If provided, then don't compare 'globals' against globals registered by 'prefix'.
		var errorString = null;

		if (!candidate_globals) {
			return null;
		}
		if (!this._globals) {
			// If this error is raised, then Globals.js has not been loaded.
			throw new Error("Variable OpenAjax._globals in not defined.");
		} else {

			// see if there are collisions
			for (var registered_library in this._globals) {
				// do not check the globals for 'prefix'.
				if (registered_library != prefix) {
					var registered_library_globals = this._globals[registered_library];
					for (var x=0, xmax=candidate_globals.length; x<xmax; x++) {
						candidate_global = candidate_globals[x];
						for (var y=0, ymax=registered_library_globals.length; y<ymax; y++) {
							if (candidate_global == registered_library_globals[y]) {
								errorString = "Global object collision for object '"+candidate_global+"'. "+
										"Previously registered by library '"+registered_library+"'.";
								if (prefix) {
									errorString += " Library attempting to register this global: '"+prefix+"'.";
								}
								break;
							}
						}
						if (errorString) {
							break;
						}
					}
				}
				if (errorString) {
					break;
				}
			}
		}
		return errorString;
	}

	// Register the OpenAjax Hub's globals
	OpenAjax.registerGlobals("OpenAjax", ["OpenAjax","OpenAjaxConfig","oahri_0_2"]);

}
/*******************************************************************************
 * PublishSubscribe.js:
 *
 * A component of the OpenAjax Hub, as specified by OpenAjax Alliance.
 * Specification is under development at: 
 *
 *   http://www.openajax.org/member/wiki/OpenAjax_Hub_Specification
 *
 * This file provides a publish/subscribe mechanism such that Ajax libraries
 * can communication with each other. An Ajax library can publish events
 * to which other Ajax libraries can subscribe.
 *
 * The logic from OpenAjaxBootstrap.js must have been loaded before this file.
 *
 * Copyright 2006 OpenAjax Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at http://www.apache.org/licenses/LICENSE-2.0 . Unless 
 * required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *
 ******************************************************************************/

/* FIXME
add unsubcribe method
*/

/* Only execute the code in this file if OpenAjax object exists, 
	this module hasn't been loaded yet, and module comes from same implementation. */
if (typeof OpenAjax!='undefined' &&
	typeof OpenAjax.subscribe=='undefined' &&
	typeof OpenAjax._oahri!='undefined' && OpenAjax._oahri == oahri_0_2) {

	// Private variable, holds the listeners that Ajax libraries have registered.
	OpenAjax._listeners = { "*": { "*": [] } };

	/*
	 *	Allows registration of interest in named events based on
	 *	toolkit-specific prefix and event name. Global event
	 *	matching is provided by passing "*" in the prefix and/or
	 *	name arguments. Optional arguments may be specified for
	 *	executing the specified handler function in a provided
	 *	scope and for further filtering events prior to application
	 */
	OpenAjax.subscribe = function (
			prefix, 	// Library prefix that was passed to registerLibrary().string, or null, or "*" to match all.
			name,   	// Event name. Can be "*" to match all.
			refOrName,	// callback function or string that holds name of function
			scope,		// object, default is window
			subscriberData,	// arbitrary object, can be null
			filter){		// function

		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}
		if (arguments.length < 3)
			throw new Error("Insufficient number of arguments");

		// NOTE: the following code is written in a verbose style for the
		// sake of readability. Non-reference implementations should use a
		// terser variant.

		if (prefix == null)
			prefix = "__null__";
		else if (prefix == "")
			throw new Error("prefix cannot be the empty string");
		if (name == null)
			throw new Error("event name cannot be null");
		if (name == "")
			throw new Error("event name cannot be empty string");

		if(!this._listeners[prefix]){
			this._listeners[prefix] = { "*": [] };
		}

		if(!this._listeners[prefix][name]){
			this._listeners[prefix][name] = [];
		}

		if (typeof subscriberData == 'undefined')
			subscriberData = null;

		if(!scope){
			scope = window;
		}

		if(typeof refOrName == "string"){
			// get a function object
			refOrName = scope[refOrName];
		}

		if(typeof refOrName != "function"){
			throw new Error("invalid function reference passed to OpenAjax.subscribe()");
		}

		this._listeners[prefix][name].push({
			"func": refOrName, 
			"data": subscriberData,
			"scope": scope, 
			"filter": filter
		});
	}


	/*
	 *	Publish events based on toolkit-specific prefix and event name.
	 */
	OpenAjax.publish = function (
			prefix, 		// Library prefix that was passed to registerLibrary().string, or null.
			name, 	  		// Event name.
			publisherData){	// arbitrary object, can be null

		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}
		if (arguments.length < 2)
			throw new Error("Insufficient number of arguments");

		// NOTE: the following code is written in a verbose style for the
		// sake of readability. Non-reference implementations should use a
		// terser variant.

		if (prefix == null)
			prefix = "__null__";
		else if (prefix == "")
			throw new Error("prefix cannot be the empty string");
		if (name == null)
			throw new Error("event name cannot be null");
		if (name == "")
			throw new Error("event name cannot be empty string");
		if (typeof publisherData == 'undefined')
			publisherData = null;

		var globals = this._listeners["*"]["*"];

		var handlers = [];

		// aggregate the various handler sets to form a single list of handlers
		if(this._listeners[prefix]){

			if(this._listeners[prefix][name]){
				handlers = handlers.concat( this._listeners[prefix][name] );
			}

			if(this._listeners[prefix]["*"]){
				handlers = handlers.concat( this._listeners[prefix]["*"] );
			}

		}else{
			handlers = globals;
		}

		if(this._listeners["*"][name]){
			handlers = handlers.concat( this._listeners["*"][name] );
		}

		if(handlers.length == 0){
			// avoid iterations if we aren't going to call any listeners anyway
			return false;
		}

		var argsArr = [];

		for(var x=0; x<handlers.length; x++){

			argsArr[0] = prefix;
			argsArr[1] = name;
			argsArr[2] = handlers[x]["data"];
			argsArr[3] = publisherData;
			if(typeof handlers[x]["filter"] == "function"){
				if( !handlers[x]["filter"].apply(window, argsArr) ){ continue; }
			}

			handlers[x].func.apply(handlers[x].scope, argsArr);

		}
		return true;
	}

}
/*******************************************************************************
 * MarkupScanner.js:
 *
 * A component of the OpenAjax Hub, as specified by OpenAjax Alliance.
 * Specification is under development at: 
 *
 *   http://www.openajax.org/member/wiki/OpenAjax_Hub_Specification
 *
 * This file manages the process of dispatching particular elements within
 * the document to particular Ajax runtime libraries in order to
 * perform that library's DOM transformation on the subtree headed by
 * that particular element.
 *
 * An Ajax library registers callbacks which are invoked either
 * before, during, or after a top-down analysis of the document DOM.
 *
 * The logic from OpenAjaxBootstrap.js must have been loaded before this file.
 *
 * Copyright 2006 OpenAjax Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at http://www.apache.org/licenses/LICENSE-2.0 . Unless 
 * required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *
 ******************************************************************************/

/* Only execute the code in this file if OpenAjax object exists, 
	this module hasn't been loaded yet, and module comes from same implementation. */
if (typeof OpenAjax!='undefined' &&
	typeof OpenAjax.registerCustomScannerCB=='undefined' &&
	typeof OpenAjax._oahri!='undefined' && OpenAjax._oahri == oahri_0_2) {

	/* -------------- PRIVATE VARIABLES AND FUNCTIONS -------------------- */

	// Private variables, holds the markup scanner callbacks to invoke in response
	// to various built-in scanning features.
	// The value of each is an array of objects, where each object
	//    holds the parameters from the corresponding register function.
	OpenAjax._TagNameCBs = [];			// Examine element's tagname, possibly prefixed
	OpenAjax._AttrNameCBs = [];			// Look for a particular attribute, possibly prefixed

	// Private variable, holds the custom markup scanner callbacks that have been registered so far.
	// The value of each entry is a function pointer to the registered markup scanner callback.
	OpenAjax._CustomScannerCBs = { };

	/*
	 *	Utility routines used by the public functions listed further down in the file.
	 */

	/* Add a given callback function that the appropriate private array. */
	OpenAjax._regScanCB = function (
			functionName,		// Public function name for error messages
			arrayToAdjust,		// The array to which this registration will be added.
			prefix,				// The prefix that corresponds to this library.
			nodeName,			// For attributes: attr name. For elements: tag name. Can have namespace prefix.
			checkType,			// "match"=must match entire string, "match-start"=must match initial characters
								// For attribute matching, additional options are available for space-separated
								// value lists, such as 'class' attribute:
								// "token"=must match entire token, "token-start"=must mast initial characters
								// For element matching, additional options are available.
								// "match-namespace"=matches given namespace URI, "match-prefix"=matches given prefix.
			checkString,		// string to check
			refOrName,			// function or string that will be called with the HTML subtree root element
			scope){				// object, default is window

		var nsuri;
		if(!scope){
			scope = window;
		}
		if(typeof refOrName == "string"){
			// get a function object
			refOrName = scope[refOrName];
		}
		if(typeof refOrName != "function"){
			throw new Error("invalid function reference passed to "+functionName+"()");
		}
		if (nodeName) {
			if(typeof nodeName != "string"){
				throw new Error("nodeName is not a string in "+functionName+"()");
			}
			var t = nodeName.split(":");
			if (t.length > 2) {
				throw new Error("nodeName has too many colons in "+functionName+"()");
			}
			if (t.length == 2) {
				// Attribute or element given as QName. Go get the namespace URI to matches prefix.
				var library = OpenAjax.queryLibrary(t[0]);
				nsuri = library["namespaceURI"];
				nodeName = t[1];
			} else {
				nsuri = null;
				// No namespace prefix, so nodeName already has correct value.
			}
		}
		arrayToAdjust.push({
			"prefix": prefix, 
			"nsuri": nsuri, 
			"nodeName": nodeName, 
			"checkType": checkType,
			"checkString": checkString, 
			"func": refOrName
		});
	}

	/* Remove callback functions from the appropriate private array. */
	OpenAjax._unregScanCB = function (
			functionName,		// Public function name for error messages
			arrayToAdjust,		// The array from which registrations will be removed.
			prefix){			// The prefix that corresponds to this library.
		for (var i=arrayToAdjust.length-1; i >=0 ; i--) {
			if (arrayToAdjust[i].prefix == prefix) {
				// Use 'slice' instead of 'splice' due to bug in IE7.
				var tail = arrayToAdjust.slice(i+1);
				var len = arrayToAdjust.length;
				for (var p=i; p<len; p++) {
					arrayToAdjust.pop();
				}
				arrayToAdjust = arrayToAdjust.concat(tail);
			}
		}
		return(arrayToAdjust);
	}

	/* -------------- PUBLIC FUNCTIONS -------------------- */
	/* -------------- BUILT-IN SCANNER CHECKS ------------- */

	/*
	 *	Registers a callback function that is invoked for element nodes in the document
	 *	that have a particular attribute set to a particular value.
	 */
	OpenAjax.registerAttrScanCB = function (
			prefix,				// The prefix that corresponds to this library.
			attrName,			// attribute to check
			checkType,			// "match"=must match entire string, "match-start"=must match initial characters
								// For attributes with space-separated values,
								// "token"=must match entire token, "token-start"=must mast initial characters
			checkString,		// value string to check
			refOrName,			// function or string that will be called with the HTML subtree root element
			scope){				// object, default is window
		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}
		if (arguments.length < 5) {
			throw new Error("Insufficient number of arguments in OpenAjax.registerAttrScanCB");
		}
		OpenAjax._regScanCB("registerAttrScanCB", OpenAjax._AttrNameCBs,
							prefix, attrName, checkType, checkString, refOrName, scope);
	}

	/*
	 *	Unregisters all callback functions registered by the given library for the attribute.
	 */
	OpenAjax.unregisterAttrScanCB = function(prefix){ 
		OpenAjax._AttrNameCBs = OpenAjax._unregScanCB("unregisterAttrScanCB", OpenAjax._AttrNameCBs, prefix);
	}

	/*
	 *	Registers a callback function that is invoked for element nodes in the document
	 *	that have a particular tagName (with or without NS), namespace, or prefix.
	 */
	OpenAjax.registerTagnameScanCB = function (
			prefix,				// The prefix that corresponds to this library.
			checkType,			// "match"=must match localName exactly, along with NS or prefix if not null.
								// "match-start"=must match first N chars of localName, along with NS or prefix if not null.
								// "match-namespace"=must be in a given NS (requires DOM Level 2 namespaces),
								// "match-prefix"=must have given prefix (with or without DOM Level 2 namespaces).
			checkString,		// value string to check
			refOrName,			// function or string that will be called with the HTML subtree root element
			scope){				// object, default is window
		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}
		if (arguments.length < 4) {
			throw new Error("Insufficient number of arguments in OpenAjax.registerTagnameScanCB");
		}
		OpenAjax._regScanCB("registerTagnameScanCB", OpenAjax._TagNameCBs,
							prefix, checkString, checkType, null, refOrName, scope);
	}

	/*
	 *	Unregisters all callback functions registered by the given library for the attribute.
	 */
	OpenAjax.unregisterTagnameScanCB = function(prefix){ 
		OpenAjax._TagNameCBs = OpenAjax._unregScanCB("unregisterTagnameScanCB", OpenAjax._TagNameCBs, prefix);
	}

	/* -------------- PUBLIC FUNCTIONS -------------------- */
	/* -------------- CUSTOM SCANNER CHECKS ------------- */

	/*
	 *	Registers a custom callback function to invoke for element nodes in the document
	 *	during the (top-to-bottom) document scanning process.
	 */
	OpenAjax.registerCustomScannerCB = function (
			prefix,		// Library prefix that was passed to registerLibrary().
			refOrName,	// The callback function object reference or the name of a function to be called.
			scope){		// Optional. An Object in which to execute refOrName. If null, default is window.

		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}
		if (arguments.length < 2)
			throw new Error("Insufficient number of arguments in OpenAjax.registerCustomScanner");
		if (prefix == null)
			throw new Error("prefix cannot be null in OpenAjax.registerCustomScanner()");
		if (prefix == "")
			throw new Error("prefix cannot be empty string in OpenAjax.registerCustomScanner()");

		if(!scope){
			scope = window;
		}

		if(typeof refOrName == "string"){
			// get a function object
			refOrName = scope[refOrName];
		}

		if(typeof refOrName != "function"){
			throw new Error("invalid function reference passed to OpenAjax.registerCustomScanner()");
		}

		OpenAjax._CustomScannerCBs[prefix] = refOrName;
	}

	/*
	 *	Unregisters a custom callback function .
	 */
	OpenAjax.unregisterCustomScannerCB = function(
		prefix){	// Library prefix that was passed to registerLibrary().

		if (!OpenAjax._CustomScannerCBs[prefix]) {
			throw new Error("Attempt to unregister a CustomScannerCB for '" + prefix + "' that has not been registered.");
		} else {	
			// Remove registration for this library.
			delete OpenAjax._CustomScannerCBs[prefix];
		}
	}

	/* -------------- PUBLIC FUNCTIONS -------------------- */
	/* -------------- DOCUMENT AND NODE SCANNERS ---------- */

	/*
	 *	Perform markup scanning on the document.
	 */
	OpenAjax.scanDocument = function() {
		var any = false;

		if (OpenAjax._error) {
			throw OpenAjax._error;	// Hub is in bad state. Bail with error message.
		}

		// See if any attr=value scanner CBs have been registered.
		if (OpenAjax._AttrNameCBs.length) { 
			any = true;
		}

		// See if any tagname scanner CBs have been registered.
		if (OpenAjax._TagNameCBs.length) { 
			any = true;
		}

		// See if OpenAjax._CustomScannerCBs has any entries.
		for (prefix in OpenAjax._CustomScannerCBs) {
			any = true;
			break;
		}

		if (any) {
			if (OpenAjaxConfig.idsToScan.length > 0) {
				for (var i = 0 ; i<OpenAjaxConfig.idsToScan.length; i++){
					OpenAjax.scanNode(document.getElementById(OpenAjaxConfig.idsToScan[i]));
				}	
			} else if (OpenAjaxConfig.scanPage) {
				OpenAjax.scanNode(document.body);
			}
		}
	}

	/*
	 *	Invoke any registered markup handler callbacks on this element node.
	 */
	OpenAjax.scanNode = function (
			node ,			//the node to recursively process
			shallowScan ){	//if true, only scan this node not any children. defaults to false=deep scan.
		var returnValue;

		// Ignore everything but element nodes (nodeType==1)
		if (!node || node.nodeType!=1) {
			return;
		}
		// IE has a bug where it sometimes treats close tag as an open tag.
		// So, ignore any so-called elements where first character of nodeName is "/"
		if (!node.nodeName || node.nodeName.charAt(0) == "/") {
			return;
		}
		if (typeof shallowScan == 'undefined') {
			shallowScan = false;
		}

		var stopScanChildNodes = false;
		var stopScanThisNode = false;

		// Check if any attribute=value callbacks have been registered.
		if (!stopScanThisNode) {
			if (OpenAjax._AttrNameCBs.length) {
				for (var i=0; i<OpenAjax._AttrNameCBs.length; i++) {
					var attrValue = null;
					var cbObject = OpenAjax._AttrNameCBs[i];
					var nsuri = cbObject.nsuri;
					var attrName = cbObject.nodeName;
					if (nsuri) {
						if (node.getAttributeNS) {
							attrValue = node.getAttributeNS(nsuri, attrName);
							// Workaround for Safari 2.0.4, which incorrectly implements getAttributeNS
							// where it matches against any old nsuri.
							var tempValue = node.getAttributeNS("zzz-bogus-uri", attrName);
							if (tempValue == attrValue) {
								attrValue = null;
							}
						}
						// Look for prefix:attrName for browsers that do not have namespace support
						// or which choose to treat the document as HTML4 without namespace support.
						if (!attrValue || attrValue == "") {
							attrValue = node.getAttribute(cbObject.prefix+":"+attrName);
						}
					} else {
						attrValue = node.getAttribute(attrName);
					}
					// IE has a bug where it doesn't support 'class' via getAttribute.
					if (!attrValue && !nsuri && attrName == "class" && node.className) {
						attrValue = node.className;
					}
					if (attrValue) {
						returnValue = null;
						var checkType = cbObject.checkType;
						if (checkType == "match") {
							if (attrValue.length == cbObject.checkString.length && attrValue.indexOf(cbObject.checkString) == 0) {
								returnValue = cbObject.func.call(window, node);
							}
						} else if (checkType == "match-start") {
							if (attrValue.indexOf(cbObject.checkString) == 0) {
								returnValue = cbObject.func.call(window, node);
							}
						} else if (checkType == "token" || checkType == "token-start") {
							var str_array = attrValue.split(" ");
							for (var j=0; j < str_array.length; j++) {
								var token = str_array[j];
								if (checkType == "token") {
									if (token.length == cbObject.checkString.length && token.indexOf(cbObject.checkString) == 0) {
										returnValue = cbObject.func.call(window, node);
										break;
									}
								} else if (checkType == "token-start") {
									if (token.indexOf(cbObject.checkString) == 0) {
										returnValue = cbObject.func.call(window, node);
										break;
									}
								}
							}
						}
						if (returnValue) {
							// If any callbacks sets stopScanChildNodes to true, then don't scan child nodes.
							if (returnValue.stopScanChildNodes) {
								stopScanChildNodes = true;
							}
							// If returnValue says to cancel all further scanning, then break out of this loop.
							if (returnValue.stopScanThisNode) {
								stopScanThisNode = true;
								break;
							}
						}
					}
				}
			}
		}

		// Check if any tagname callbacks have been registered.
		if (!stopScanThisNode) {
			if (OpenAjax._TagNameCBs.length) {
				for (var i=0; i<OpenAjax._TagNameCBs.length; i++) {
					var cbObject = OpenAjax._TagNameCBs[i];
					var tagPrefix = null;
					var tagNS = null;
					var tagLocalName = null;
					if (node.prefix && node.namespaceURI && node.localName) {
						// DOM Level 2 namespaces is supported.
						tagPrefix = node.prefix;
						tagNS = node.namespaceURI;
						tagLocalName = node.localName;
					} else {
						if (node.tagName) {
							var tagName = node.tagName;
							if (tagName.indexOf(":") != -1) {
								var temp_array = tagName.split(":");
								tagPrefix = temp_array[0];
								tagLocalName = temp_array[1];
							} else {
								tagLocalName = tagName;
							}
						}
					}
					if (tagLocalName) {
						returnValue = null;
						var checkType = cbObject.checkType;
						if (checkType == "match-namespace") {
							// FIXME: Need to create test cases for namespace option.
							// FIXME: May need to uppercase the namespace URIs before comparisons.
							if (tagNS && tagNS == cbObject.nsuri) {
								// FIXME - Need to review code associated with 3rd and 4th params on callbacks.
								returnValue = cbObject.func.call(window, node);
							}
						} else if (checkType == "match-prefix") {
							var checkPrefix = cbObject.prefix.toUpperCase();
							if (tagPrefix && tagPrefix == checkPrefix) {
								returnValue = cbObject.func.call(window, node);
							}
						} else if (checkType == "match" || checkType == "match-start") {
							var checkPrefix = cbObject.prefix.toUpperCase();
							var checkNS = cbObject.nsuri;
							var checkLocalName = cbObject.nodeName.toUpperCase();
							if ((checkType == "match" && checkLocalName == tagLocalName) ||
									(checkType == "match-start" && tagLocalName.indexOf(checkLocalName) == 0)) {
								if ((!tagPrefix && !tagNS) || 
										(tagNS && tagNS == checkNS) ||
										(tagPrefix && tagPrefix == checkPrefix)) {
									// FIXME - Need to review code associated with 3rd and 4th params on callbacks.
									returnValue = cbObject.func.call(window, node);
								}
							}
						}
						if (returnValue) {
							// If any callbacks sets stopScanChildNodes to true, then don't scan child nodes.
							if (returnValue.stopScanChildNodes) {
								stopScanChildNodes = true;
							}
							// If returnValue says to cancel all further scanning, then break out of this loop.
							if (returnValue.stopScanThisNode) {
								stopScanThisNode = true;
								break;
							}
						}
					}
				}
			}
		}

		if (!stopScanThisNode) {
			for (prefix in OpenAjax._CustomScannerCBs) {
				returnValue = OpenAjax._CustomScannerCBs[prefix].call(window, node);
				if (returnValue) {
					// If any callbacks sets stopScanChildNodes to true, then don't scan child nodes.
					if (returnValue.stopScanChildNodes) {
						stopScanChildNodes = true;
					}
					// If returnValue says to cancel all further scanning, then break out of this loop.
					if (returnValue.stopScanThisNode) {
						stopScanThisNode = true;
						break;
					}
				}
			}
		}
		if (!shallowScan && !stopScanChildNodes) {
			var childNode, i = 0, childNodes = node.childNodes;
			while(childNode = childNodes[i++]){
				if (childNode.nodeType!=1)
					continue;
				OpenAjax.scanNode(childNode);
			}
		}
	}

}
