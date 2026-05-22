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
package org.htmlunit.javascript.host.xml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.htmlunit.SgmlPage;
import org.htmlunit.html.DomAttr;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlAbbreviated;
import org.htmlunit.html.HtmlAcronym;
import org.htmlunit.html.HtmlAddress;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlAudio;
import org.htmlunit.html.HtmlBidirectionalOverride;
import org.htmlunit.html.HtmlBig;
import org.htmlunit.html.HtmlBlockQuote;
import org.htmlunit.html.HtmlBody;
import org.htmlunit.html.HtmlBold;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlCanvas;
import org.htmlunit.html.HtmlCaption;
import org.htmlunit.html.HtmlCenter;
import org.htmlunit.html.HtmlCitation;
import org.htmlunit.html.HtmlCode;
import org.htmlunit.html.HtmlDefinition;
import org.htmlunit.html.HtmlDefinitionDescription;
import org.htmlunit.html.HtmlDefinitionList;
import org.htmlunit.html.HtmlDefinitionTerm;
import org.htmlunit.html.HtmlDeletedText;
import org.htmlunit.html.HtmlDirectory;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlEmbed;
import org.htmlunit.html.HtmlEmphasis;
import org.htmlunit.html.HtmlExample;
import org.htmlunit.html.HtmlFieldSet;
import org.htmlunit.html.HtmlFont;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlFrame;
import org.htmlunit.html.HtmlFrameSet;
import org.htmlunit.html.HtmlHead;
import org.htmlunit.html.HtmlHeading1;
import org.htmlunit.html.HtmlHeading2;
import org.htmlunit.html.HtmlHeading3;
import org.htmlunit.html.HtmlHeading4;
import org.htmlunit.html.HtmlHeading5;
import org.htmlunit.html.HtmlHeading6;
import org.htmlunit.html.HtmlHeadingGroup;
import org.htmlunit.html.HtmlHtml;
import org.htmlunit.html.HtmlInlineFrame;
import org.htmlunit.html.HtmlInlineQuotation;
import org.htmlunit.html.HtmlInsertedText;
import org.htmlunit.html.HtmlItalic;
import org.htmlunit.html.HtmlKeyboard;
import org.htmlunit.html.HtmlLabel;
import org.htmlunit.html.HtmlLegend;
import org.htmlunit.html.HtmlListItem;
import org.htmlunit.html.HtmlListing;
import org.htmlunit.html.HtmlMap;
import org.htmlunit.html.HtmlMarquee;
import org.htmlunit.html.HtmlMenu;
import org.htmlunit.html.HtmlNoBreak;
import org.htmlunit.html.HtmlNoEmbed;
import org.htmlunit.html.HtmlNoFrames;
import org.htmlunit.html.HtmlNoScript;
import org.htmlunit.html.HtmlObject;
import org.htmlunit.html.HtmlOption;
import org.htmlunit.html.HtmlOptionGroup;
import org.htmlunit.html.HtmlOrderedList;
import org.htmlunit.html.HtmlParagraph;
import org.htmlunit.html.HtmlPlainText;
import org.htmlunit.html.HtmlPreformattedText;
import org.htmlunit.html.HtmlS;
import org.htmlunit.html.HtmlSample;
import org.htmlunit.html.HtmlScript;
import org.htmlunit.html.HtmlSelect;
import org.htmlunit.html.HtmlSmall;
import org.htmlunit.html.HtmlSource;
import org.htmlunit.html.HtmlSpan;
import org.htmlunit.html.HtmlStrike;
import org.htmlunit.html.HtmlStrong;
import org.htmlunit.html.HtmlStyle;
import org.htmlunit.html.HtmlSubscript;
import org.htmlunit.html.HtmlSuperscript;
import org.htmlunit.html.HtmlTable;
import org.htmlunit.html.HtmlTableBody;
import org.htmlunit.html.HtmlTableColumn;
import org.htmlunit.html.HtmlTableColumnGroup;
import org.htmlunit.html.HtmlTableDataCell;
import org.htmlunit.html.HtmlTableFooter;
import org.htmlunit.html.HtmlTableHeader;
import org.htmlunit.html.HtmlTableHeaderCell;
import org.htmlunit.html.HtmlTableRow;
import org.htmlunit.html.HtmlTeletype;
import org.htmlunit.html.HtmlTextArea;
import org.htmlunit.html.HtmlTitle;
import org.htmlunit.html.HtmlUnderlined;
import org.htmlunit.html.HtmlUnorderedList;
import org.htmlunit.html.HtmlVariable;
import org.htmlunit.html.HtmlVideo;
import org.htmlunit.html.HtmlWordBreak;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.Element;
import org.htmlunit.javascript.host.dom.Document;
import org.htmlunit.javascript.host.dom.DocumentFragment;
import org.htmlunit.javascript.host.dom.Node;
import org.htmlunit.util.StringUtils;
import org.w3c.dom.NamedNodeMap;

/**
 * A JavaScript object for {@code XMLSerializer}.
 * see https://w3c.github.io/DOM-Parsing/#the-xmlserializer-interface
 *
 * @author Ahmed Ashour
 * @author Darrell DeBoer
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass
public class XMLSerializer extends HtmlUnitScriptable {

    // this is a bit strange but it is the way FF works
    // output of empty tags are not allowed for these HTML tags
    private static final Set<String> NON_EMPTY_TAGS = new HashSet<>(Arrays.asList(
            HtmlAbbreviated.TAG_NAME, HtmlAcronym.TAG_NAME,
            HtmlAnchor.TAG_NAME, HtmlAddress.TAG_NAME, HtmlAudio.TAG_NAME,
            HtmlBidirectionalOverride.TAG_NAME, HtmlBig.TAG_NAME,
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
            HtmlHeading6.TAG_NAME, HtmlHead.TAG_NAME, HtmlHeadingGroup.TAG_NAME,
            HtmlHtml.TAG_NAME, HtmlInlineFrame.TAG_NAME,
            HtmlInsertedText.TAG_NAME,
            HtmlItalic.TAG_NAME, HtmlKeyboard.TAG_NAME, HtmlLabel.TAG_NAME,
            HtmlLegend.TAG_NAME, HtmlListing.TAG_NAME, HtmlListItem.TAG_NAME,
            HtmlMap.TAG_NAME, HtmlMarquee.TAG_NAME,
            HtmlMenu.TAG_NAME,
            HtmlNoBreak.TAG_NAME, HtmlNoEmbed.TAG_NAME, HtmlNoFrames.TAG_NAME,
            HtmlNoScript.TAG_NAME, HtmlObject.TAG_NAME, HtmlOrderedList.TAG_NAME,
            HtmlOptionGroup.TAG_NAME, HtmlOption.TAG_NAME, HtmlParagraph.TAG_NAME,
            HtmlPlainText.TAG_NAME, HtmlPreformattedText.TAG_NAME,
            HtmlInlineQuotation.TAG_NAME, HtmlS.TAG_NAME, HtmlSample.TAG_NAME,
            HtmlScript.TAG_NAME, HtmlSelect.TAG_NAME, HtmlSmall.TAG_NAME,
            HtmlSource.TAG_NAME, HtmlSpan.TAG_NAME,
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
        // nothing to do
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

        if (root instanceof DocumentFragment) {
            Node node = root.getFirstChild();
            if (node == null) {
                return "";
            }

            final StringBuilder builder = new StringBuilder();
            while (node != null) {
                builder.append(serializeToString(node));
                node = node.getNextSibling();
            }
            return builder.toString().trim();
        }

        final boolean rootIsDocument = root instanceof Document;
        if (rootIsDocument) {
            root = ((Document) root).getDocumentElement();
        }

        if (root instanceof Element) {
            final StringBuilder builder = new StringBuilder();
            final DomNode node = root.getDomNodeOrDie();
            final SgmlPage page = node.getPage();
            final boolean isHtmlPage = page != null && page.isHtmlPage();

            String forcedNamespace = null;
            if (!rootIsDocument && isHtmlPage) {
                forcedNamespace = "http://www.w3.org/1999/xhtml";
            }
            toXml(1, node, builder, forcedNamespace);

            return builder.toString();
        }

        return root.getDomNodeOrDie().asXml();
    }

    private void toXml(final int indent,
            final DomNode node, final StringBuilder builder, final String foredNamespace) {
        final String nodeName = node.getNodeName();
        builder.append('<').append(nodeName);

        String optionalPrefix = "";
        final String namespaceURI = node.getNamespaceURI();
        final String prefix = node.getPrefix();
        if (namespaceURI != null && prefix != null) {
            boolean sameNamespace = false;
            for (DomNode parentNode = node.getParentNode(); parentNode instanceof DomElement;
                    parentNode = parentNode.getParentNode()) {
                if (namespaceURI.equals(parentNode.getNamespaceURI())) {
                    sameNamespace = true;
                    break;
                }
            }
            if (node.getParentNode() == null || !sameNamespace) {
                ((DomElement) node).setAttribute("xmlns:" + prefix, namespaceURI);
            }
        }
        else if (foredNamespace != null) {
            builder.append(" xmlns=\"").append(foredNamespace).append('"');
            optionalPrefix = " ";
        }

        final NamedNodeMap attributesMap = node.getAttributes();
        final int length = attributesMap.getLength();
        for (int i = 0; i < length; i++) {
            final DomAttr attrib = (DomAttr) attributesMap.item(i);
            builder.append(' ').append(attrib.getQualifiedName())
                   .append("=\"").append(attrib.getValue()).append('"');
        }
        boolean startTagClosed = false;
        for (final DomNode child : node.getChildren()) {
            if (!startTagClosed) {
                builder.append(optionalPrefix).append('>');
                startTagClosed = true;
            }
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    toXml(indent + 1, child, builder, null);
                    break;

                case Node.TEXT_NODE:
                    String value = child.getNodeValue();
                    value = StringUtils.escapeXmlChars(value);
                    builder.append(value);
                    break;

                case Node.CDATA_SECTION_NODE:
                case Node.COMMENT_NODE:
                    builder.append(child.asXml());
                    break;

                default:
                    break;
            }
        }

        if (startTagClosed) {
            builder.append("</").append(nodeName).append('>');
        }
        else {
            final String tagName = StringUtils.toRootLowerCase(nodeName);
            if (NON_EMPTY_TAGS.contains(tagName)) {
                builder.append("></").append(nodeName).append('>');
            }
            else {
                builder.append(optionalPrefix).append("/>");
            }
        }
    }

}
