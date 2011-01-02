/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.List;

import org.w3c.dom.NodeList;

/**
 * A list of {@link DomNode}s which is both a W3C {@link NodeList} and a java {@link List}.
 *
 * @version $Revision$
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 * @param <E> the element type
 */
public interface DomNodeList<E extends DomNode> extends NodeList, List<E> {
}
