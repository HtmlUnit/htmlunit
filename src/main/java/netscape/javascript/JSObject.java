/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package netscape.javascript;

import java.applet.Applet;

/**
 * Stub for the JSException. This is part of the Applet
 * LiveConnect simulation.
 *
 * TODO: we have to evaluate if it is possible to use plugin.jar from jdk
 *
 * @version $Revision$
 * @author Ronald Brill
 */
public class JSObject {

    /**
     * Empty stub.
     *
     * @param paramString the paramString
     * @param paramArrayOfObject the paramArrayOfObject
     * @return result Object
     * @throws JSException in case or error
     */
    public Object call(final String paramString, final Object[] paramArrayOfObject) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.call(String, Object[])).");
    }

    /**
     * Empty stub.
     *
     * @param paramString the paramString
     * @return result Object
     * @throws JSException in case or error
     */
    public Object eval(final String paramString) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.eval(String)).");
    }

    /**
     * Empty stub.
     *
     * @param paramString the paramString
     * @return result Object
     * @throws JSException in case or error
     */
    public Object getMember(final String paramString) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.getMember(String)).");
    }

    /**
     * Empty stub.
     *
     * @param paramString the paramString
     * @param paramObject the paramObject
     * @throws JSException in case or error
     */
    public void setMember(final String paramString, final Object paramObject) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.setMember(String, Object)).");
    }

    /**
     * Empty stub.
     *
     * @param paramString the paramString
     * @throws JSException in case or error
     */
    public void removeMember(final String paramString) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.removeMember(String)).");
    }

    /**
     * Empty stub.
     *
     * @param paramInt the paramInt
     * @return result Object
     * @throws JSException in case or error
     */
    public Object getSlot(final int paramInt) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.getSlot(int)).");
    }

    /**
     * Empty stub.
     *
     * @param paramInt the paramInt
     * @param paramObject the paramObject
     * @throws JSException in case or error
     */
    public void setSlot(final int paramInt, final Object paramObject) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.setSlot(int, Object)).");
    }

    /**
     * Empty stub.
     *
     * @param paramApplet the paramApplet
     * @return result Object
     * @throws JSException in case or error
     */
    public static JSObject getWindow(final Applet paramApplet) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.getWindow(Applet)).");
    }
}
