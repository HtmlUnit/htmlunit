/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.html.serializer;

import static org.htmlunit.BrowserVersionFeatures.JS_INNER_HTML_XHTML_BOOLEAN_ATTRIBUTE_VALUE;
import static org.htmlunit.BrowserVersionFeatures.JS_INNER_HTML_XHTML_HEX_LINE_BREAK;
import static org.htmlunit.BrowserVersionFeatures.JS_INNER_HTML_XHTML_NS_DECLARATION_AFTER_ATTRIBUTE;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.XMLConstants;

import org.htmlunit.BrowserVersion;
import org.htmlunit.html.DomAttr;
import org.htmlunit.html.DomCDataSection;
import org.htmlunit.html.DomComment;
import org.htmlunit.html.DomDocumentFragment;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomProcessingInstruction;
import org.htmlunit.html.DomText;
import org.htmlunit.html.Html;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlTemplate;
import org.htmlunit.util.StringUtils;
import org.htmlunit.util.XmlUtils;

/**
 * The XML serialization used by innerHTML/outerHTML for XHTML and XML documents.
 * <p>This is a simplified implementation of the algorithm described in
 * <a href="https://w3c.github.io/DOM-Parsing/#xml-serialization">DOM Parsing and Serialization -
 * XML serialization</a>: namespace declarations are added if the namespace of an element
 * (or the prefix of an attribute) is not declared by the context; elements without children
 * are written as empty elements.</p>
 *
 * @author Ronald Brill
 */
public final class XmlMarkupSerializer {

    /** The HTML void elements (written as {@code <br />} in the XHTML namespace). */
    private static final Set<String> VOID_ELEMENTS = Set.of(
            "area", "base", "basefont", "bgsound", "br", "col", "embed", "frame", "hr",
            "img", "input", "keygen", "link", "menuitem", "meta", "param", "source", "track", "wbr");

    /** Boolean attributes written as name="name" if the value is empty (Firefox). */
    private static final Set<String> BOOLEAN_ATTRIBUTES = Set.of(
            "checked", "compact", "declare", "defer", "disabled", "ismap", "multiple",
            "nohref", "noresize", "noshade", "nowrap", "readonly", "selected");

    private final boolean booleanAttributeValue_;
    private final boolean hexLineBreak_;
    private final boolean declarationAfterAttribute_;

    /**
     * Ctor.
     *
     * @param browserVersion the browser version used to determine the browser specific details
     */
    private XmlMarkupSerializer(final BrowserVersion browserVersion) {
        booleanAttributeValue_ = browserVersion.hasFeature(JS_INNER_HTML_XHTML_BOOLEAN_ATTRIBUTE_VALUE);
        hexLineBreak_ = browserVersion.hasFeature(JS_INNER_HTML_XHTML_HEX_LINE_BREAK);
        declarationAfterAttribute_ = browserVersion.hasFeature(JS_INNER_HTML_XHTML_NS_DECLARATION_AFTER_ATTRIBUTE);
    }

    /**
     * Serializes the children of the given node (the innerHTML).
     *
     * @param node the node whose children are serialized
     * @param browserVersion the browser version used to determine the browser specific details
     * @return the serialized children
     */
    public static String serializeChildren(final DomNode node, final BrowserVersion browserVersion) {
        final StringBuilder builder = new StringBuilder();
        new XmlMarkupSerializer(browserVersion).appendChildren(builder, node, null, newPrefixMap());
        return builder.toString();
    }

    /**
     * Serializes the given node including the node itself (the outerHTML).
     *
     * @param node the node to serialize
     * @param browserVersion the browser version used to determine the browser specific details
     * @return the serialized node
     */
    public static String serialize(final DomNode node, final BrowserVersion browserVersion) {
        final StringBuilder builder = new StringBuilder();
        new XmlMarkupSerializer(browserVersion).appendNode(builder, node, null, newPrefixMap());
        return builder.toString();
    }

    /**
     * Creates the initial prefix map; only the xml prefix is declared.
     *
     * @return the prefix map (prefix to namespace)
     */
    private static Map<String, String> newPrefixMap() {
        final Map<String, String> prefixMap = new HashMap<>();
        prefixMap.put(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
        return prefixMap;
    }

    /**
     * Serializes all children of the given node; the children of a template
     * are the children of the template content.
     *
     * @param builder the builder to write to
     * @param node the parent node
     * @param contextNamespace the default namespace of the context
     * @param prefixMap the prefixes declared by the context (prefix to namespace)
     */
    private void appendChildren(final StringBuilder builder, final DomNode node,
            final String contextNamespace, final Map<String, String> prefixMap) {
        final DomNode parent = node instanceof HtmlTemplate template ? template.getContent() : node;
        for (final DomNode child : parent.getChildren()) {
            appendNode(builder, child, contextNamespace, prefixMap);
        }
    }

    /**
     * Serializes a single node.
     *
     * @param builder the builder to write to
     * @param node the node
     * @param contextNamespace the default namespace of the context
     * @param prefixMap the prefixes declared by the context (prefix to namespace)
     */
    private void appendNode(final StringBuilder builder, final DomNode node,
            final String contextNamespace, final Map<String, String> prefixMap) {
        if (node instanceof DomElement element) {
            appendElement(builder, element, contextNamespace, prefixMap);
        }
        else if (node instanceof DomCDataSection) {
            builder.append("<![CDATA[").append(node.getNodeValue()).append("]]>");
        }
        else if (node instanceof DomComment) {
            builder.append("<!--").append(node.getNodeValue()).append("-->");
        }
        else if (node instanceof DomText) {
            builder.append(StringUtils.escapeXmlChars(node.getNodeValue()));
        }
        else if (node instanceof DomProcessingInstruction pi) {
            builder.append("<?").append(pi.getTarget());
            final String data = pi.getData();
            if (!StringUtils.isEmptyOrNull(data)) {
                builder.append(' ').append(data);
            }
            builder.append("?>");
        }
        else if (node instanceof DomDocumentFragment) {
            appendChildren(builder, node, contextNamespace, prefixMap);
        }
    }

    /**
     * Serializes an element.
     *
     * @param builder the builder to write to
     * @param element the element
     * @param contextNamespace the default namespace of the context
     * @param contextPrefixMap the prefixes declared by the context (prefix to namespace)
     */
    private void appendElement(final StringBuilder builder, final DomElement element,
            final String contextNamespace, final Map<String, String> contextPrefixMap) {
        final String namespace = namespaceOf(element);
        final String prefix = element.getPrefix();
        final Map<String, String> prefixMap = new HashMap<>(contextPrefixMap);

        // namespace declarations of the element itself (xmlns and xmlns:foo attributes)
        String localDefaultNamespace = null;
        boolean hasDefaultNamespaceAttribute = false;
        for (final DomAttr attr : element.getAttributesMap().values()) {
            final String name = attr.getName();
            if (XMLConstants.XMLNS_ATTRIBUTE.equals(name)) {
                hasDefaultNamespaceAttribute = true;
                localDefaultNamespace = StringUtils.isEmptyOrNull(attr.getValue()) ? null : attr.getValue();
            }
            else if (name.startsWith(XMLConstants.XMLNS_ATTRIBUTE + ":")) {
                prefixMap.put(name.substring(XMLConstants.XMLNS_ATTRIBUTE.length() + 1), attr.getValue());
            }
        }

        final StringBuilder declarations = new StringBuilder();
        String childNamespace = contextNamespace;
        if (prefix != null) {
            if (!Objects.equals(prefixMap.get(prefix), namespace)) {
                appendDeclaration(declarations, prefix, namespace);
                prefixMap.put(prefix, namespace);
            }
        }
        else if (hasDefaultNamespaceAttribute) {
            childNamespace = localDefaultNamespace;
        }
        else {
            if (!Objects.equals(namespace, contextNamespace)) {
                appendDeclaration(declarations, null, namespace);
            }
            childNamespace = namespace;
        }

        // prefixes used by the attributes; written in front of all attributes
        // or (Firefox) after the attribute
        final Map<DomAttr, String> attributeDeclarations = new HashMap<>();
        for (final DomAttr attr : element.getAttributesMap().values()) {
            final String attrNamespace = attr.getNamespaceURI();
            final String attrPrefix = attr.getPrefix();
            if (attrNamespace != null && attrPrefix != null
                    && !XMLConstants.XML_NS_URI.equals(attrNamespace)
                    && !XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attrNamespace)
                    && !XMLConstants.XMLNS_ATTRIBUTE.equals(attrPrefix)
                    && !attrNamespace.equals(prefixMap.get(attrPrefix))) {
                if (declarationAfterAttribute_) {
                    final StringBuilder declaration = new StringBuilder();
                    appendDeclaration(declaration, attrPrefix, attrNamespace);
                    attributeDeclarations.put(attr, declaration.toString());
                }
                else {
                    appendDeclaration(declarations, attrPrefix, attrNamespace);
                }
                prefixMap.put(attrPrefix, attrNamespace);
            }
        }

        final String qualifiedName = prefix == null ? element.getLocalName() : prefix + ":" + element.getLocalName();
        builder.append('<').append(qualifiedName).append(declarations);
        for (final DomAttr attr : element.getAttributesMap().values()) {
            if (attr.getSpecified()) {
                builder.append(' ').append(attr.getName()).append("=\"")
                    .append(attributeValue(attr, namespace)).append('"');
                final String declaration = attributeDeclarations.get(attr);
                if (declaration != null) {
                    builder.append(declaration);
                }
            }
        }

        final DomNode contentParent = element instanceof HtmlTemplate template ? template.getContent() : element;
        if (contentParent.getFirstChild() == null) {
            if (Html.XHTML_NAMESPACE.equals(namespace)) {
                if (VOID_ELEMENTS.contains(element.getLocalName())) {
                    builder.append(" />");
                    return;
                }
                builder.append("></").append(qualifiedName).append('>');
                return;
            }
            builder.append("/>");
            return;
        }

        builder.append('>');
        appendChildren(builder, element, childNamespace, prefixMap);
        builder.append("</").append(qualifiedName).append('>');
    }

    /**
     * Returns the escaped value of the attribute.
     *
     * @param attr the attribute
     * @param elementNamespace the namespace of the element
     * @return the escaped value
     */
    private String attributeValue(final DomAttr attr, final String elementNamespace) {
        String value = attr.getValue();
        if (booleanAttributeValue_
                && StringUtils.isEmptyOrNull(value)
                && attr.getPrefix() == null
                && Html.XHTML_NAMESPACE.equals(elementNamespace)
                && BOOLEAN_ATTRIBUTES.contains(attr.getName())) {
            value = attr.getName();
        }

        value = XmlUtils.escapeXmlAttributeValue(value);
        if (hexLineBreak_) {
            value = value.replace("&#10;", "&#xA;").replace("&#13;", "&#xD;");
        }
        return value;
    }

    /**
     * Appends a namespace declaration.
     *
     * @param builder the builder to write to
     * @param prefix the prefix or {@code null} for the default namespace
     * @param namespace the namespace or {@code null}
     */
    private static void appendDeclaration(final StringBuilder builder, final String prefix, final String namespace) {
        builder.append(' ').append(XMLConstants.XMLNS_ATTRIBUTE);
        if (prefix != null) {
            builder.append(':').append(prefix);
        }
        builder.append("=\"");
        if (namespace != null) {
            builder.append(XmlUtils.escapeXmlAttributeValue(namespace));
        }
        builder.append('"');
    }

    /**
     * Returns the namespace of the element; HTML elements without namespace
     * are in the XHTML namespace.
     *
     * @param element the element
     * @return the namespace or {@code null}
     */
    private static String namespaceOf(final DomElement element) {
        final String namespace = element.getNamespaceURI();
        if (StringUtils.isEmptyOrNull(namespace)) {
            return element instanceof HtmlElement ? Html.XHTML_NAMESPACE : null;
        }
        return namespace;
    }
}
