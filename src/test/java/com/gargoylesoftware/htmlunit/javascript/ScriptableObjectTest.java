/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for general scriptable objects in the browser context.
 *
 * @version $Revision$
 * @author Jake Cobb
 */
@RunWith(BrowserRunner.class)
public class ScriptableObjectTest extends WebDriverTestCase {
	
	/**
	 * Tests that writing a property which is a read-only in the prototype
	 * behaves as expected.
	 * 
	 * @see https://sourceforge.net/p/htmlunit/bugs/1633/
	 * @throws Exception on failure
	 */
	@Test
	@Alerts({"default", "default", "default"})
	public void testReadOnlyPrototype() throws Exception {
		final String html = "<html><body><script>"
			+ "var proto = Object.create(Object.prototype, {\n"
			+ "    myProp: {\n"
			+ "        get: function() { return 'default'; }\n"
			+ "    }\n"
			+ "});\n"
			+ "var o1 = Object.create(proto), o2 = Object.create(proto);\n"
			+ "o2.myProp = 'bar';\n"
			+ "alert(o2.myProp);\n"
			+ "alert(o1.myProp);\n"
			+ "alert(proto.myProp)";
		loadPageWithAlerts2(html);
	}

}
