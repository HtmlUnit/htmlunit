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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * Base class for MSXML's (ActiveX) JavaScript host objects in HtmlUnit.
 *
 * @version $Revision$
 * @author Frank Danek
 */
public class MSXMLScriptable extends SimpleScriptable {

    private static final Log LOG = LogFactory.getLog(MSXMLScriptable.class);

    private MSXMLJavaScriptEnvironment environment_;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParentScope(final Scriptable m) {
        super.setParentScope(m);

        if (m instanceof MSXMLScriptable) {
            setEnvironment(((MSXMLScriptable) m).getEnvironment());
        }
    }

    /**
     * Builds a new the JavaScript object that corresponds to the specified object.
     * @param domNode the DOM node for which a JS object should be created
     * @return the JavaScript object
     */
    public SimpleScriptable makeScriptableFor(final DomNode domNode) {
        // Get the JS class name for the specified DOM node.
        // Walk up the inheritance chain if necessary.
        Class<? extends MSXMLScriptable> javaScriptClass = null;
        for (Class<?> c = domNode.getClass(); javaScriptClass == null && c != null; c = c.getSuperclass()) {
            javaScriptClass = getEnvironment().getJavaScriptClass(c);
        }

        final MSXMLScriptable scriptable;
        if (javaScriptClass == null) {
            // We don't have a specific subclass for this element so create something generic.
            scriptable = new XMLDOMElement();
            if (LOG.isDebugEnabled()) {
                LOG.debug("No MSXML JavaScript class found for element <" + domNode.getNodeName()
                    + ">. Using XMLDOMElement");
            }
        }
        else {
            try {
                scriptable = javaScriptClass.newInstance();
            }
            catch (final Exception e) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
        }
        initParentScope(domNode, scriptable);

        scriptable.setPrototype(getPrototype(javaScriptClass));
        scriptable.setDomNode(domNode);
        scriptable.setEnvironment(getEnvironment());

        return scriptable;
    }

    /**
     * Gets the prototype object for the given host class.
     * @param javaScriptClass the host class
     * @return the prototype
     */
    @SuppressWarnings("unchecked")
    protected Scriptable getPrototype(final Class<? extends SimpleScriptable> javaScriptClass) {
        final Scriptable prototype = getEnvironment().getPrototype(javaScriptClass);
        if (prototype == null && javaScriptClass != SimpleScriptable.class) {
            return getPrototype((Class<? extends SimpleScriptable>) javaScriptClass.getSuperclass());
        }
        return prototype;
    }

    @Override
    protected boolean isReadOnlySettable(final String name, final Object value) {
        throw ScriptRuntime.typeError3("msg.set.prop.no.setter",
                name, getClassName(), Context.toString(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return "Object";
    }

    /**
     * @return the environment_
     */
    public MSXMLJavaScriptEnvironment getEnvironment() {
        return environment_;
    }

    /**
     * @param environment the environment_ to set
     */
    public void setEnvironment(final MSXMLJavaScriptEnvironment environment) {
        environment_ = environment;
    }
}
