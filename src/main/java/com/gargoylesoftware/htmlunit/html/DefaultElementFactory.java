/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.xml.sax.Attributes;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

/**
 * Element factory which creates elements by calling the constructor on a
 * given {@link HtmlElement} subclass.
 * The constructor is expected to take 2 arguments of type
 * {@link HtmlPage} and {@link java.util.Map}
 * where the first one is the owning page of the element, the second one is a map
 * holding the initial attributes for the element.
 *
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author David K. Taylor
 * @author Ronald Brill
 * @author Frank Danek
 */
class DefaultElementFactory implements ElementFactory {

    /*
     * You can generate your own test cases by looking into ElementTestSource.generateTestForHtmlElements
     */
    static final List<String> SUPPORTED_TAGS_ = Arrays.asList(HtmlAbbreviated.TAG_NAME, HtmlAcronym.TAG_NAME,
            HtmlAnchor.TAG_NAME, HtmlAddress.TAG_NAME, HtmlApplet.TAG_NAME, HtmlArea.TAG_NAME,
            HtmlArticle.TAG_NAME, HtmlAside.TAG_NAME, HtmlAudio.TAG_NAME,
            HtmlBackgroundSound.TAG_NAME, HtmlBase.TAG_NAME, HtmlBaseFont.TAG_NAME,
            HtmlBidirectionalIsolation.TAG_NAME, HtmlBidirectionalOverride.TAG_NAME, HtmlBig.TAG_NAME,
            HtmlBlink.TAG_NAME, HtmlBlockQuote.TAG_NAME, HtmlBody.TAG_NAME, HtmlBold.TAG_NAME,
            HtmlBreak.TAG_NAME, HtmlButton.TAG_NAME, HtmlCanvas.TAG_NAME, HtmlCaption.TAG_NAME,
            HtmlCenter.TAG_NAME, HtmlCitation.TAG_NAME, HtmlCode.TAG_NAME,
            HtmlCommand.TAG_NAME, HtmlContent.TAG_NAME, HtmlData.TAG_NAME, HtmlDataList.TAG_NAME,
            HtmlDefinition.TAG_NAME, HtmlDefinitionDescription.TAG_NAME,
            HtmlDeletedText.TAG_NAME, HtmlDetails.TAG_NAME, HtmlDialog.TAG_NAME, HtmlDirectory.TAG_NAME,
            HtmlDivision.TAG_NAME, HtmlDefinitionList.TAG_NAME,
            HtmlDefinitionTerm.TAG_NAME, HtmlEmbed.TAG_NAME,
            HtmlEmphasis.TAG_NAME,
            HtmlFieldSet.TAG_NAME, HtmlFigureCaption.TAG_NAME, HtmlFigure.TAG_NAME,
            HtmlFont.TAG_NAME, HtmlForm.TAG_NAME, HtmlFooter.TAG_NAME,
            HtmlFrame.TAG_NAME, HtmlFrameSet.TAG_NAME,
            HtmlHead.TAG_NAME, HtmlHeader.TAG_NAME,
            HtmlHeading1.TAG_NAME, HtmlHeading2.TAG_NAME, HtmlHeading3.TAG_NAME,
            HtmlHeading4.TAG_NAME, HtmlHeading5.TAG_NAME, HtmlHeading6.TAG_NAME,
            HtmlHorizontalRule.TAG_NAME, HtmlHtml.TAG_NAME, HtmlInlineFrame.TAG_NAME,
            HtmlInlineQuotation.TAG_NAME,
            HtmlImage.TAG_NAME, HtmlImage.TAG_NAME2, HtmlInsertedText.TAG_NAME, HtmlIsIndex.TAG_NAME,
            HtmlItalic.TAG_NAME,
            HtmlKeyboard.TAG_NAME, HtmlKeygen.TAG_NAME,
            HtmlLabel.TAG_NAME, HtmlLayer.TAG_NAME,
            HtmlLegend.TAG_NAME, HtmlListing.TAG_NAME, HtmlListItem.TAG_NAME,
            HtmlLink.TAG_NAME, HtmlMain.TAG_NAME, HtmlMap.TAG_NAME, HtmlMark.TAG_NAME, HtmlMarquee.TAG_NAME,
            HtmlMenu.TAG_NAME, HtmlMenuItem.TAG_NAME, HtmlMeta.TAG_NAME, HtmlMeter.TAG_NAME, HtmlMultiColumn.TAG_NAME,
            HtmlNav.TAG_NAME, HtmlNextId.TAG_NAME,
            HtmlNoBreak.TAG_NAME, HtmlNoEmbed.TAG_NAME, HtmlNoFrames.TAG_NAME,
            HtmlNoLayer.TAG_NAME,
            HtmlNoScript.TAG_NAME, HtmlObject.TAG_NAME, HtmlOrderedList.TAG_NAME,
            HtmlOptionGroup.TAG_NAME, HtmlOption.TAG_NAME, HtmlOutput.TAG_NAME,
            HtmlParagraph.TAG_NAME,
            HtmlParameter.TAG_NAME, HtmlPicture.TAG_NAME, HtmlPlainText.TAG_NAME, HtmlPreformattedText.TAG_NAME,
            HtmlProgress.TAG_NAME,
            HtmlRp.TAG_NAME, HtmlRt.TAG_NAME, HtmlRuby.TAG_NAME,
            HtmlS.TAG_NAME, HtmlSample.TAG_NAME,
            HtmlScript.TAG_NAME, HtmlSection.TAG_NAME, HtmlSelect.TAG_NAME, HtmlSmall.TAG_NAME,
            HtmlSource.TAG_NAME, HtmlSpan.TAG_NAME,
            HtmlStrike.TAG_NAME, HtmlStrong.TAG_NAME, HtmlStyle.TAG_NAME,
            HtmlSubscript.TAG_NAME, HtmlSummary.TAG_NAME, HtmlSuperscript.TAG_NAME,
            HtmlTable.TAG_NAME, HtmlTableColumn.TAG_NAME, HtmlTableColumnGroup.TAG_NAME,
            HtmlTableBody.TAG_NAME, HtmlTableDataCell.TAG_NAME, HtmlTableHeaderCell.TAG_NAME,
            HtmlTableRow.TAG_NAME, HtmlTextArea.TAG_NAME, HtmlTableFooter.TAG_NAME,
            HtmlTableHeader.TAG_NAME, HtmlTeletype.TAG_NAME, HtmlTemplate.TAG_NAME, HtmlTime.TAG_NAME,
            HtmlTitle.TAG_NAME, HtmlTrack.TAG_NAME, HtmlUnderlined.TAG_NAME, HtmlUnorderedList.TAG_NAME,
            HtmlVariable.TAG_NAME, HtmlVideo.TAG_NAME, HtmlWordBreak.TAG_NAME, HtmlExample.TAG_NAME
    );

    /**
     * @param page the owning page
     * @param tagName the HTML tag name
     * @param attributes initial attributes, possibly {@code null}
     * @return the newly created element
     */
    @Override
    public HtmlElement createElement(final SgmlPage page, final String tagName, final Attributes attributes) {
        return createElementNS(page, null, tagName, attributes);
    }

    /**
     * @param page the owning page
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param attributes initial attributes, possibly {@code null}
     * @return the newly created element
     */
    @Override
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes) {
        return createElementNS(page, namespaceURI, qualifiedName, attributes, false);
    }

    /**
     * @param page the owning page
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param attributes initial attributes, possibly {@code null}
     * @param checkBrowserCompatibility if true and the page doesn't support this element, return null
     * @return the newly created element
     */
    @Override
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes, final boolean checkBrowserCompatibility) {
        final Map<String, DomAttr> attributeMap = setAttributes(page, attributes);

        final HtmlElement element;
        final String tagName;
        final int colonIndex = qualifiedName.indexOf(':');
        if (colonIndex == -1) {
            tagName = qualifiedName.toLowerCase(Locale.ROOT);
        }
        else {
            tagName = qualifiedName.substring(colonIndex + 1).toLowerCase(Locale.ROOT);
        }

        switch (tagName) {
            case HtmlAbbreviated.TAG_NAME:
                element = new HtmlAbbreviated(qualifiedName, page, attributeMap);
                break;

            case HtmlAcronym.TAG_NAME:
                element = new HtmlAcronym(qualifiedName, page, attributeMap);
                break;

            case HtmlAddress.TAG_NAME:
                element = new HtmlAddress(qualifiedName, page, attributeMap);
                break;

            case HtmlAnchor.TAG_NAME:
                element = new HtmlAnchor(qualifiedName, page, attributeMap);
                break;

            case HtmlApplet.TAG_NAME:
                element = new HtmlApplet(qualifiedName, page, attributeMap);
                break;

            case HtmlArea.TAG_NAME:
                element = new HtmlArea(qualifiedName, page, attributeMap);
                break;

            case HtmlArticle.TAG_NAME:
                element = new HtmlArticle(qualifiedName, page, attributeMap);
                break;

            case HtmlAside.TAG_NAME:
                element = new HtmlAside(qualifiedName, page, attributeMap);
                break;

            case HtmlAudio.TAG_NAME:
                element = new HtmlAudio(qualifiedName, page, attributeMap);
                break;

            case HtmlBackgroundSound.TAG_NAME:
                element = new HtmlBackgroundSound(qualifiedName, page, attributeMap);
                break;

            case HtmlBase.TAG_NAME:
                element = new HtmlBase(qualifiedName, page, attributeMap);
                break;

            case HtmlBaseFont.TAG_NAME:
                element = new HtmlBaseFont(qualifiedName, page, attributeMap);
                break;

            case HtmlBidirectionalIsolation.TAG_NAME:
                element = new HtmlBidirectionalIsolation(qualifiedName, page, attributeMap);
                break;

            case HtmlBidirectionalOverride.TAG_NAME:
                element = new HtmlBidirectionalOverride(qualifiedName, page, attributeMap);
                break;

            case HtmlBig.TAG_NAME:
                element = new HtmlBig(qualifiedName, page, attributeMap);
                break;

            case HtmlBlink.TAG_NAME:
                element = new HtmlBlink(qualifiedName, page, attributeMap);
                break;

            case HtmlBlockQuote.TAG_NAME:
                element = new HtmlBlockQuote(qualifiedName, page, attributeMap);
                break;

            case HtmlBody.TAG_NAME:
                element = new HtmlBody(qualifiedName, page, attributeMap, false);
                break;

            case HtmlBold.TAG_NAME:
                element = new HtmlBold(qualifiedName, page, attributeMap);
                break;

            case HtmlBreak.TAG_NAME:
                element = new HtmlBreak(qualifiedName, page, attributeMap);
                break;

            case HtmlButton.TAG_NAME:
                element = new HtmlButton(qualifiedName, page, attributeMap);
                break;

            case HtmlCanvas.TAG_NAME:
                element = new HtmlCanvas(qualifiedName, page, attributeMap);
                break;

            case HtmlCaption.TAG_NAME:
                element = new HtmlCaption(qualifiedName, page, attributeMap);
                break;

            case HtmlCenter.TAG_NAME:
                element = new HtmlCenter(qualifiedName, page, attributeMap);
                break;

            case HtmlCitation.TAG_NAME:
                element = new HtmlCitation(qualifiedName, page, attributeMap);
                break;

            case HtmlCode.TAG_NAME:
                element = new HtmlCode(qualifiedName, page, attributeMap);
                break;

            case HtmlCommand.TAG_NAME:
                element = new HtmlCommand(qualifiedName, page, attributeMap);
                break;

            case HtmlContent.TAG_NAME:
                element = new HtmlContent(qualifiedName, page, attributeMap);
                break;

            case HtmlData.TAG_NAME:
                element = new HtmlData(qualifiedName, page, attributeMap);
                break;

            case HtmlDataList.TAG_NAME:
                element = new HtmlDataList(qualifiedName, page, attributeMap);
                break;

            case HtmlDefinition.TAG_NAME:
                element = new HtmlDefinition(qualifiedName, page, attributeMap);
                break;

            case HtmlDefinitionDescription.TAG_NAME:
                element = new HtmlDefinitionDescription(qualifiedName, page, attributeMap);
                break;

            case HtmlDefinitionList.TAG_NAME:
                element = new HtmlDefinitionList(qualifiedName, page, attributeMap);
                break;

            case HtmlDefinitionTerm.TAG_NAME:
                element = new HtmlDefinitionTerm(qualifiedName, page, attributeMap);
                break;

            case HtmlDeletedText.TAG_NAME:
                element = new HtmlDeletedText(qualifiedName, page, attributeMap);
                break;

            case HtmlDetails.TAG_NAME:
                element = new HtmlDetails(qualifiedName, page, attributeMap);
                break;

            case HtmlDialog.TAG_NAME:
                element = new HtmlDialog(qualifiedName, page, attributeMap);
                break;

            case HtmlDirectory.TAG_NAME:
                element = new HtmlDirectory(qualifiedName, page, attributeMap);
                break;

            case HtmlDivision.TAG_NAME:
                element = new HtmlDivision(qualifiedName, page, attributeMap);
                break;

            case HtmlEmbed.TAG_NAME:
                element = new HtmlEmbed(qualifiedName, page, attributeMap);
                break;

            case HtmlEmphasis.TAG_NAME:
                element = new HtmlEmphasis(qualifiedName, page, attributeMap);
                break;

            case HtmlExample.TAG_NAME:
                element = new HtmlExample(qualifiedName, page, attributeMap);
                break;

            case HtmlFieldSet.TAG_NAME:
                element = new HtmlFieldSet(qualifiedName, page, attributeMap);
                break;

            case HtmlFigure.TAG_NAME:
                element = new HtmlFigure(qualifiedName, page, attributeMap);
                break;

            case HtmlFigureCaption.TAG_NAME:
                element = new HtmlFigureCaption(qualifiedName, page, attributeMap);
                break;

            case HtmlFont.TAG_NAME:
                element = new HtmlFont(qualifiedName, page, attributeMap);
                break;

            case HtmlForm.TAG_NAME:
                element = new HtmlForm(qualifiedName, page, attributeMap);
                break;

            case HtmlFooter.TAG_NAME:
                element = new HtmlFooter(qualifiedName, page, attributeMap);
                break;

            case HtmlFrame.TAG_NAME:
                if (attributeMap != null) {
                    final DomAttr srcAttribute = attributeMap.get("src");
                    if (srcAttribute != null) {
                        srcAttribute.setValue(srcAttribute.getValue().trim());
                    }
                }
                element = new HtmlFrame(qualifiedName, page, attributeMap);
                break;

            case HtmlFrameSet.TAG_NAME:
                element = new HtmlFrameSet(qualifiedName, page, attributeMap);
                break;

            case HtmlHead.TAG_NAME:
                element = new HtmlHead(qualifiedName, page, attributeMap);
                break;

            case HtmlHeader.TAG_NAME:
                element = new HtmlHeader(qualifiedName, page, attributeMap);
                break;

            case HtmlHeading1.TAG_NAME:
                element = new HtmlHeading1(qualifiedName, page, attributeMap);
                break;

            case HtmlHeading2.TAG_NAME:
                element = new HtmlHeading2(qualifiedName, page, attributeMap);
                break;

            case HtmlHeading3.TAG_NAME:
                element = new HtmlHeading3(qualifiedName, page, attributeMap);
                break;

            case HtmlHeading4.TAG_NAME:
                element = new HtmlHeading4(qualifiedName, page, attributeMap);
                break;

            case HtmlHeading5.TAG_NAME:
                element = new HtmlHeading5(qualifiedName, page, attributeMap);
                break;

            case HtmlHeading6.TAG_NAME:
                element = new HtmlHeading6(qualifiedName, page, attributeMap);
                break;

            case HtmlHorizontalRule.TAG_NAME:
                element = new HtmlHorizontalRule(qualifiedName, page, attributeMap);
                break;

            case HtmlHtml.TAG_NAME:
                element = new HtmlHtml(qualifiedName, page, attributeMap);
                break;

            case HtmlImage.TAG_NAME:
            case HtmlImage.TAG_NAME2:
                element = new HtmlImage(qualifiedName, page, attributeMap);
                break;

            case HtmlInlineFrame.TAG_NAME:
                if (attributeMap != null) {
                    final DomAttr srcAttribute = attributeMap.get("src");
                    if (srcAttribute != null) {
                        srcAttribute.setValue(srcAttribute.getValue().trim());
                    }
                }
                element = new HtmlInlineFrame(qualifiedName, page, attributeMap);
                break;

            case HtmlInlineQuotation.TAG_NAME:
                element = new HtmlInlineQuotation(qualifiedName, page, attributeMap);
                break;

            case HtmlInsertedText.TAG_NAME:
                element = new HtmlInsertedText(qualifiedName, page, attributeMap);
                break;

            case HtmlIsIndex.TAG_NAME:
                element = new HtmlIsIndex(qualifiedName, page, attributeMap);
                break;

            case HtmlItalic.TAG_NAME:
                element = new HtmlItalic(qualifiedName, page, attributeMap);
                break;

            case HtmlKeyboard.TAG_NAME:
                element = new HtmlKeyboard(qualifiedName, page, attributeMap);
                break;

            case HtmlKeygen.TAG_NAME:
                element = new HtmlKeygen(qualifiedName, page, attributeMap);
                break;

            case HtmlLabel.TAG_NAME:
                element = new HtmlLabel(qualifiedName, page, attributeMap);
                break;

            case HtmlLayer.TAG_NAME:
                element = new HtmlLayer(qualifiedName, page, attributeMap);
                break;

            case HtmlLegend.TAG_NAME:
                element = new HtmlLegend(qualifiedName, page, attributeMap);
                break;

            case HtmlLink.TAG_NAME:
                element = new HtmlLink(qualifiedName, page, attributeMap);
                break;

            case HtmlListing.TAG_NAME:
                element = new HtmlListing(qualifiedName, page, attributeMap);
                break;

            case HtmlListItem.TAG_NAME:
                element = new HtmlListItem(qualifiedName, page, attributeMap);
                break;

            case HtmlMain.TAG_NAME:
                element = new HtmlMain(qualifiedName, page, attributeMap);
                break;

            case HtmlMap.TAG_NAME:
                element = new HtmlMap(qualifiedName, page, attributeMap);
                break;

            case HtmlMark.TAG_NAME:
                element = new HtmlMark(qualifiedName, page, attributeMap);
                break;

            case HtmlMarquee.TAG_NAME:
                element = new HtmlMarquee(qualifiedName, page, attributeMap);
                break;

            case HtmlMenu.TAG_NAME:
                element = new HtmlMenu(qualifiedName, page, attributeMap);
                break;

            case HtmlMenuItem.TAG_NAME:
                element = new HtmlMenuItem(qualifiedName, page, attributeMap);
                break;

            case HtmlMeta.TAG_NAME:
                element = new HtmlMeta(qualifiedName, page, attributeMap);
                break;

            case HtmlMeter.TAG_NAME:
                element = new HtmlMeter(qualifiedName, page, attributeMap);
                break;

            case HtmlMultiColumn.TAG_NAME:
                element = new HtmlMultiColumn(qualifiedName, page, attributeMap);
                break;

            case HtmlNav.TAG_NAME:
                element = new HtmlNav(qualifiedName, page, attributeMap);
                break;

            case HtmlNextId.TAG_NAME:
                element = new HtmlNextId(qualifiedName, page, attributeMap);
                break;

            case HtmlNoBreak.TAG_NAME:
                element = new HtmlNoBreak(qualifiedName, page, attributeMap);
                break;

            case HtmlNoEmbed.TAG_NAME:
                element = new HtmlNoEmbed(qualifiedName, page, attributeMap);
                break;

            case HtmlNoFrames.TAG_NAME:
                element = new HtmlNoFrames(qualifiedName, page, attributeMap);
                break;

            case HtmlNoLayer.TAG_NAME:
                element = new HtmlNoLayer(qualifiedName, page, attributeMap);
                break;

            case HtmlNoScript.TAG_NAME:
                element = new HtmlNoScript(qualifiedName, page, attributeMap);
                break;

            case HtmlObject.TAG_NAME:
                element = new HtmlObject(qualifiedName, page, attributeMap);
                break;

            case HtmlOption.TAG_NAME:
                element = new HtmlOption(qualifiedName, page, attributeMap);
                break;

            case HtmlOptionGroup.TAG_NAME:
                element = new HtmlOptionGroup(qualifiedName, page, attributeMap);
                break;

            case HtmlOrderedList.TAG_NAME:
                element = new HtmlOrderedList(qualifiedName, page, attributeMap);
                break;

            case HtmlOutput.TAG_NAME:
                element = new HtmlOutput(qualifiedName, page, attributeMap);
                break;

            case HtmlParagraph.TAG_NAME:
                element = new HtmlParagraph(qualifiedName, page, attributeMap);
                break;

            case HtmlParameter.TAG_NAME:
                element = new HtmlParameter(qualifiedName, page, attributeMap);
                break;

            case HtmlPicture.TAG_NAME:
                element = new HtmlPicture(qualifiedName, page, attributeMap);
                break;

            case HtmlPlainText.TAG_NAME:
                element = new HtmlPlainText(qualifiedName, page, attributeMap);
                break;

            case HtmlPreformattedText.TAG_NAME:
                element = new HtmlPreformattedText(qualifiedName, page, attributeMap);
                break;

            case HtmlProgress.TAG_NAME:
                element = new HtmlProgress(qualifiedName, page, attributeMap);
                break;

            case HtmlRp.TAG_NAME:
                element = new HtmlRp(qualifiedName, page, attributeMap);
                break;

            case HtmlRt.TAG_NAME:
                element = new HtmlRt(qualifiedName, page, attributeMap);
                break;

            case HtmlRuby.TAG_NAME:
                element = new HtmlRuby(qualifiedName, page, attributeMap);
                break;

            case HtmlS.TAG_NAME:
                element = new HtmlS(qualifiedName, page, attributeMap);
                break;

            case HtmlSample.TAG_NAME:
                element = new HtmlSample(qualifiedName, page, attributeMap);
                break;

            case HtmlScript.TAG_NAME:
                element = new HtmlScript(qualifiedName, page, attributeMap);
                break;

            case HtmlSection.TAG_NAME:
                element = new HtmlSection(qualifiedName, page, attributeMap);
                break;

            case HtmlSelect.TAG_NAME:
                element = new HtmlSelect(qualifiedName, page, attributeMap);
                break;

            case HtmlSmall.TAG_NAME:
                element = new HtmlSmall(qualifiedName, page, attributeMap);
                break;

            case HtmlSource.TAG_NAME:
                element = new HtmlSource(qualifiedName, page, attributeMap);
                break;

            case HtmlSpan.TAG_NAME:
                element = new HtmlSpan(qualifiedName, page, attributeMap);
                break;

            case HtmlStrike.TAG_NAME:
                element = new HtmlStrike(qualifiedName, page, attributeMap);
                break;

            case HtmlStrong.TAG_NAME:
                element = new HtmlStrong(qualifiedName, page, attributeMap);
                break;

            case HtmlStyle.TAG_NAME:
                element = new HtmlStyle(qualifiedName, page, attributeMap);
                break;

            case HtmlSubscript.TAG_NAME:
                element = new HtmlSubscript(qualifiedName, page, attributeMap);
                break;

            case HtmlSummary.TAG_NAME:
                element = new HtmlSummary(qualifiedName, page, attributeMap);
                break;

            case HtmlSuperscript.TAG_NAME:
                element = new HtmlSuperscript(qualifiedName, page, attributeMap);
                break;

            case HtmlTable.TAG_NAME:
                element = new HtmlTable(qualifiedName, page, attributeMap);
                break;

            case HtmlTableBody.TAG_NAME:
                element = new HtmlTableBody(qualifiedName, page, attributeMap);
                break;

            case HtmlTableColumn.TAG_NAME:
                element = new HtmlTableColumn(qualifiedName, page, attributeMap);
                break;

            case HtmlTableColumnGroup.TAG_NAME:
                element = new HtmlTableColumnGroup(qualifiedName, page, attributeMap);
                break;

            case HtmlTableDataCell.TAG_NAME:
                element = new HtmlTableDataCell(qualifiedName, page, attributeMap);
                break;

            case HtmlTableFooter.TAG_NAME:
                element = new HtmlTableFooter(qualifiedName, page, attributeMap);
                break;

            case HtmlTableHeader.TAG_NAME:
                element = new HtmlTableHeader(qualifiedName, page, attributeMap);
                break;

            case HtmlTableHeaderCell.TAG_NAME:
                element = new HtmlTableHeaderCell(qualifiedName, page, attributeMap);
                break;

            case HtmlTableRow.TAG_NAME:
                element = new HtmlTableRow(qualifiedName, page, attributeMap);
                break;

            case HtmlTeletype.TAG_NAME:
                element = new HtmlTeletype(qualifiedName, page, attributeMap);
                break;

            case HtmlTemplate.TAG_NAME:
                element = new HtmlTemplate(qualifiedName, page, attributeMap);
                break;

            case HtmlTextArea.TAG_NAME:
                element = new HtmlTextArea(qualifiedName, page, attributeMap);
                break;

            case HtmlTime.TAG_NAME:
                element = new HtmlTime(qualifiedName, page, attributeMap);
                break;

            case HtmlTitle.TAG_NAME:
                element = new HtmlTitle(qualifiedName, page, attributeMap);
                break;

            case HtmlTrack.TAG_NAME:
                element = new HtmlTrack(qualifiedName, page, attributeMap);
                break;

            case HtmlUnderlined.TAG_NAME:
                element = new HtmlUnderlined(qualifiedName, page, attributeMap);
                break;

            case HtmlUnorderedList.TAG_NAME:
                element = new HtmlUnorderedList(qualifiedName, page, attributeMap);
                break;

            case HtmlVariable.TAG_NAME:
                element = new HtmlVariable(qualifiedName, page, attributeMap);
                break;

            case HtmlVideo.TAG_NAME:
                element = new HtmlVideo(qualifiedName, page, attributeMap);
                break;

            case HtmlWordBreak.TAG_NAME:
                element = new HtmlWordBreak(qualifiedName, page, attributeMap);
                break;

            default:
                throw new IllegalStateException("Cannot find HtmlElement for " + qualifiedName);
        }

        final JavaScriptConfiguration config = page.getWebClient().getJavaScriptEngine().getJavaScriptConfiguration();
        if (!"td".equals(tagName) && !"th".equals(tagName)
                && checkBrowserCompatibility && config.getDomJavaScriptMapping().get(element.getClass()) == null) {
            return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
        }
        return element;
    }

    /**
     * Converts {@link Attributes} into the map needed by {@link HtmlElement}s.
     *
     * @param page the page which contains the specified attributes
     * @param attributes the SAX attributes
     * @return the map of attribute values for {@link HtmlElement}s
     */
    static Map<String, DomAttr> setAttributes(final SgmlPage page, final Attributes attributes) {
        Map<String, DomAttr> attributeMap = null;
        if (attributes != null) {
            attributeMap = new LinkedHashMap<>(attributes.getLength());
            for (int i = 0; i < attributes.getLength(); i++) {
                final String qName = attributes.getQName(i);
                // browsers consider only first attribute (ex: <div id='foo' id='something'>...</div>)
                if (!attributeMap.containsKey(qName)) {
                    String namespaceURI = attributes.getURI(i);
                    if (namespaceURI != null && namespaceURI.isEmpty()) {
                        namespaceURI = null;
                    }
                    final DomAttr newAttr = new DomAttr(page, namespaceURI, qName, attributes.getValue(i), true);
                    attributeMap.put(qName, newAttr);
                }
            }
        }
        return attributeMap;
    }
}
