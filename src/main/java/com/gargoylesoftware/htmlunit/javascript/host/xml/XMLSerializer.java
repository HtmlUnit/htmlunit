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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XML_SERIALIZER_ADD_XHTML_NAMESPACE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XML_SERIALIZER_APPENDS_CRLF;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XML_SERIALIZER_NON_EMPTY_TAGS;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Document;
import com.gargoylesoftware.htmlunit.javascript.host.DocumentFragment;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * A JavaScript object for XMLSerializer.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Darrell DeBoer
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(browsers = { @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 10) })
public class XMLSerializer extends SimpleScriptable {

    // this is a bit strange but it is the way FF works
    // output of empty tags are not allowed for these HTML tags
    private static final Set<String> NON_EMPTY_TAGS = new HashSet<String>(Arrays.asList(
            HtmlAbbreviated.TAG_NAME, HtmlAcronym.TAG_NAME,
            HtmlAnchor.TAG_NAME, HtmlApplet.TAG_NAME, HtmlAddress.TAG_NAME, HtmlAudio.TAG_NAME,
            HtmlBackgroundSound.TAG_NAME,
            HtmlBidirectionalOverride.TAG_NAME, HtmlBig.TAG_NAME, HtmlBlink.TAG_NAME,
            HtmlBlockQuote.TAG_NAME, HtmlBody.TAG_NAME, HtmlBold.TAG_NAME,
            HtmlButton.TAG_NAME, HtmlCanvas.TAG_NAME, HtmlCaption.TAG_NAME,
            HtmlCenter.TAG_NAME, HtmlCitation.TAG_NAME, HtmlCode.TAG_NAME,
            HtmlDefinition.TAG_NAME, HtmlDefinitionDescription.TAG_NAME,
            HtmlDeletedText.TAG_NAME, HtmlDirectory.TAG_NAME,
            HtmlDivision.TAG_NAME,
            HtmlDefinitionList.TAG_NAME,
            HtmlDefinitionTerm.TAG_NAME, HtmlEmbed.TAG_NAME,
            HtmlEmphasis.TAG_NAME, HtmlFieldSet.TAG_NAME,
            HtmlFont.TAG_NAME, HtmlForm.TAG_NAME,
            HtmlFrame.TAG_NAME, HtmlFrameSet.TAG_NAME, HtmlHeading1.TAG_NAME,
            HtmlHeading2.TAG_NAME, HtmlHeading3.TAG_NAME,
            HtmlHeading4.TAG_NAME, HtmlHeading5.TAG_NAME,
            HtmlHeading6.TAG_NAME, HtmlHead.TAG_NAME,
            HtmlHtml.TAG_NAME, HtmlInlineFrame.TAG_NAME,
            HtmlInsertedText.TAG_NAME, HtmlIsIndex.TAG_NAME,
            HtmlItalic.TAG_NAME, HtmlKeyboard.TAG_NAME, HtmlLabel.TAG_NAME,
            HtmlLegend.TAG_NAME, HtmlListing.TAG_NAME, HtmlListItem.TAG_NAME,
            HtmlMap.TAG_NAME, HtmlMarquee.TAG_NAME,
            HtmlMenu.TAG_NAME, HtmlMultiColumn.TAG_NAME,
            HtmlNoBreak.TAG_NAME, HtmlNoEmbed.TAG_NAME, HtmlNoFrames.TAG_NAME,
            HtmlNoScript.TAG_NAME, HtmlObject.TAG_NAME, HtmlOrderedList.TAG_NAME,
            HtmlOptionGroup.TAG_NAME, HtmlOption.TAG_NAME, HtmlParagraph.TAG_NAME,
            HtmlPlainText.TAG_NAME, HtmlPreformattedText.TAG_NAME,
            HtmlInlineQuotation.TAG_NAME, HtmlS.TAG_NAME, HtmlSample.TAG_NAME,
            HtmlScript.TAG_NAME, HtmlSelect.TAG_NAME, HtmlSmall.TAG_NAME,
            HtmlSource.TAG_NAME, HtmlSpacer.TAG_NAME, HtmlSpan.TAG_NAME,
            HtmlStrike.TAG_NAME, HtmlStrong.TAG_NAME, HtmlStyle.TAG_NAME,
            HtmlSubscript.TAG_NAME, HtmlSuperscript.TAG_NAME, HtmlTitle.TAG_NAME,
            HtmlTable.TAG_NAME, HtmlTableColumn.TAG_NAME, HtmlTableColumnGroup.TAG_NAME,
            HtmlTableBody.TAG_NAME, HtmlTableDataCell.TAG_NAME, HtmlTableHeaderCell.TAG_NAME,
            HtmlTableRow.TAG_NAME, HtmlTextArea.TAG_NAME, HtmlTableFooter.TAG_NAME,
            HtmlTableHeader.TAG_NAME, HtmlTeletype.TAG_NAME, HtmlUnderlined.TAG_NAME,
            HtmlUnorderedList.TAG_NAME, HtmlVariable.TAG_NAME, HtmlVideo.TAG_NAME,
            HtmlWordBreak.TAG_NAME, HtmlExample.TAG_NAME
    ));

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // Empty.
    }

    /**
     * The subtree rooted by the specified element is serialized to a string.
     * @param root the root of the subtree to be serialized (this may be any node, even a document)
     * @return the serialized string
     */
    @JsxFunction
    public String serializeToString(Node root) {
        if (root == null) {
            return "";
        }
        if (root instanceof Document) {
            root = ((Document) root).getDocumentElement();
        }
        else if (root instanceof DocumentFragment) {
            root = root.getFirstChild();
        }
        if (root instanceof Element) {
            final StringBuilder buffer = new StringBuilder();
            final DomNode node = root.getDomNodeOrDie();
            final SgmlPage page = node.getPage();
            final boolean isHtmlPage = page != null && page.isHtmlPage();
            final boolean appendCrlf = getBrowserVersion().hasFeature(JS_XML_SERIALIZER_APPENDS_CRLF);
            final boolean addXhtmlNamespace = getBrowserVersion().hasFeature(JS_XML_SERIALIZER_ADD_XHTML_NAMESPACE);

            String forcedNamespace = null;
            if (addXhtmlNamespace) {
                if (isHtmlPage) {
                    forcedNamespace = "http://www.w3.org/1999/xhtml";
                }
            }
            toXml(1, node, buffer, forcedNamespace, appendCrlf);

            if (appendCrlf) {
                buffer.append("\r\n");
            }
            return buffer.toString();
        }
        return root.getDomNodeOrDie().asXml();
    }

    private void toXml(final int indent,
            final DomNode node, final StringBuilder buffer,
            final String foredNamespace, final boolean appendCrLf) {
        final String nodeName = node.getNodeName();
        buffer.append('<').append(nodeName);

        String optionalPrefix = "";
        final String namespaceURI = node.getNamespaceURI();
        final String prefix = node.getPrefix();
        if (namespaceURI != null && prefix != null) {
            boolean sameNamespace = false;
            for (DomNode parentNode = node.getParentNode(); parentNode instanceof DomElement;
                    parentNode = parentNode.getParentNode()) {
                if (namespaceURI.equals(parentNode.getNamespaceURI())) {
                    sameNamespace = true;
                }
            }
            if (node.getParentNode() == null || !sameNamespace) {
                ((DomElement) node).setAttribute("xmlns:" + prefix, namespaceURI);
            }
        }
        else if (foredNamespace != null) {
            buffer.append(" xmlns=\"").append(foredNamespace).append('"');
            optionalPrefix = " ";
        }

        final NamedNodeMap attributesMap = node.getAttributes();
        for (int i = 0; i < attributesMap.getLength(); i++) {
            final DomAttr attrib = (DomAttr) attributesMap.item(i);
            buffer.append(' ').append(attrib.getQualifiedName()).append('=')
                .append('"').append(attrib.getValue()).append('"');
        }
        boolean startTagClosed = false;
        for (final DomNode child : node.getChildren()) {
            if (!startTagClosed) {
                buffer.append(optionalPrefix).append('>');
                startTagClosed = true;
            }
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    toXml(indent + 1, child, buffer, null, appendCrLf);
                    break;

                case Node.TEXT_NODE:
                    String value = child.getNodeValue();
                    value = StringUtils.escapeXmlChars(value);
                    if (appendCrLf && org.apache.commons.lang3.StringUtils.isBlank(value)) {
                        buffer.append("\r\n");
                        final DomNode sibling = child.getNextSibling();
                        if (sibling != null && sibling.getNodeType() == Node.ELEMENT_NODE) {
                            for (int i = 0; i < indent; i++) {
                                buffer.append('\t');
                            }
                        }
                    }
                    else {
                        buffer.append(value);
                    }
                    break;

                case Node.CDATA_SECTION_NODE:
                case Node.COMMENT_NODE:
                    buffer.append(child.asXml());
                    break;

                default:

            }
        }
        if (!startTagClosed) {
            final String tagName = nodeName.toLowerCase(Locale.ENGLISH);
            final boolean nonEmptyTagsSupported = getBrowserVersion().hasFeature(JS_XML_SERIALIZER_NON_EMPTY_TAGS);
            if (nonEmptyTagsSupported && NON_EMPTY_TAGS.contains(tagName)) {
                buffer.append('>');
                buffer.append("</").append(nodeName).append('>');
            }
            else {
                buffer.append(optionalPrefix).append("/>");
            }
        }
        else {
            buffer.append('<').append('/').append(nodeName).append('>');
        }
    }

}
