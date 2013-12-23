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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CANVAS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML5_RUBY_TAGS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML5_TAGS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLBASEFONT_SUPPORTED;

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
 * @version $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author David K. Taylor
 * @author Ronald Brill
 * @author Frank Danek
 */
class DefaultElementFactory implements ElementFactory {

    /*
     * Whenever you add a tag, please add corresponding test cases in the below:
     * There are few areas, where we iterate over all elements, you can search in the project for ("xmp")
     * For example HTMLParser2Test.childNodes_xmp
     * You can generate your own test cases by looking into TestSource.generateTestForHtmlElements
     */
    static final List<String> SUPPORTED_TAGS_ = Arrays.asList(HtmlAbbreviated.TAG_NAME, HtmlAcronym.TAG_NAME,
            HtmlAnchor.TAG_NAME, HtmlAddress.TAG_NAME, HtmlApplet.TAG_NAME, HtmlArea.TAG_NAME,
            HtmlArticle.TAG_NAME, HtmlAside.TAG_NAME, HtmlAudio.TAG_NAME,
            HtmlBackgroundSound.TAG_NAME, HtmlBase.TAG_NAME, HtmlBaseFont.TAG_NAME,
            HtmlBidirectionalOverride.TAG_NAME, HtmlBig.TAG_NAME, HtmlBlink.TAG_NAME,
            HtmlBlockQuote.TAG_NAME, HtmlBody.TAG_NAME, HtmlBold.TAG_NAME,
            HtmlBreak.TAG_NAME, HtmlButton.TAG_NAME, HtmlCanvas.TAG_NAME, HtmlCaption.TAG_NAME,
            HtmlCenter.TAG_NAME, HtmlCitation.TAG_NAME, HtmlCode.TAG_NAME,
            HtmlDataList.TAG_NAME,
            HtmlDefinition.TAG_NAME, HtmlDefinitionDescription.TAG_NAME,
            HtmlDeletedText.TAG_NAME, HtmlDirectory.TAG_NAME,
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
            HtmlLabel.TAG_NAME,
            HtmlLegend.TAG_NAME, HtmlListing.TAG_NAME, HtmlListItem.TAG_NAME,
            HtmlLink.TAG_NAME, HtmlMap.TAG_NAME, HtmlMark.TAG_NAME, HtmlMarquee.TAG_NAME,
            HtmlMenu.TAG_NAME, HtmlMeta.TAG_NAME, HtmlMeter.TAG_NAME, HtmlMultiColumn.TAG_NAME,
            HtmlNav.TAG_NAME, HtmlNextId.TAG_NAME,
            HtmlNoBreak.TAG_NAME, HtmlNoEmbed.TAG_NAME, HtmlNoFrames.TAG_NAME,
            HtmlNoScript.TAG_NAME, HtmlObject.TAG_NAME, HtmlOrderedList.TAG_NAME,
            HtmlOptionGroup.TAG_NAME, HtmlOption.TAG_NAME, HtmlOutput.TAG_NAME,
            HtmlParagraph.TAG_NAME,
            HtmlParameter.TAG_NAME, HtmlPlainText.TAG_NAME, HtmlPreformattedText.TAG_NAME,
            HtmlProgress.TAG_NAME,
            HtmlRp.TAG_NAME, HtmlRt.TAG_NAME, HtmlRuby.TAG_NAME,
            HtmlS.TAG_NAME, HtmlSample.TAG_NAME,
            HtmlScript.TAG_NAME, HtmlSection.TAG_NAME, HtmlSelect.TAG_NAME, HtmlSmall.TAG_NAME,
            HtmlSource.TAG_NAME, HtmlSpan.TAG_NAME,
            HtmlStrike.TAG_NAME, HtmlStrong.TAG_NAME, HtmlStyle.TAG_NAME,
            HtmlSubscript.TAG_NAME, HtmlSuperscript.TAG_NAME,
            HtmlTable.TAG_NAME, HtmlTableColumn.TAG_NAME, HtmlTableColumnGroup.TAG_NAME,
            HtmlTableBody.TAG_NAME, HtmlTableDataCell.TAG_NAME, HtmlTableHeaderCell.TAG_NAME,
            HtmlTableRow.TAG_NAME, HtmlTextArea.TAG_NAME, HtmlTableFooter.TAG_NAME,
            HtmlTableHeader.TAG_NAME, HtmlTeletype.TAG_NAME, HtmlTime.TAG_NAME, HtmlTitle.TAG_NAME,
            HtmlUnderlined.TAG_NAME, HtmlUnorderedList.TAG_NAME,
            HtmlVariable.TAG_NAME, HtmlVideo.TAG_NAME,
            HtmlWordBreak.TAG_NAME, HtmlExample.TAG_NAME
    );

    /**
     * @param page the owning page
     * @param tagName the HTML tag name
     * @param attributes initial attributes, possibly <code>null</code>
     * @return the newly created element
     */
    public HtmlElement createElement(final SgmlPage page, final String tagName, final Attributes attributes) {
        return createElementNS(page, null, tagName, attributes);
    }

    /**
     * @param page the owning page
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param attributes initial attributes, possibly <code>null</code>
     * @return the newly created element
     */
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes) {
        return createElementNS(page, namespaceURI, qualifiedName, attributes, false);
    }

    /**
     * @param page the owning page
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param attributes initial attributes, possibly <code>null</code>
     * @param checkBrowserCompatibility if true and the page doesn't support this element, return null
     * @return the newly created element
     */
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes, final boolean checkBrowserCompatibility) {
        final Map<String, DomAttr> attributeMap = setAttributes(page, attributes);

        final HtmlElement element;
        final String tagName;
        final int colonIndex = qualifiedName.indexOf(':');
        if (colonIndex == -1) {
            tagName = qualifiedName.toLowerCase(Locale.ENGLISH);
        }
        else {
            tagName = qualifiedName.substring(colonIndex + 1).toLowerCase(Locale.ENGLISH);
        }

//        final Class<? extends HtmlElement> klass = JavaScriptConfiguration.getHtmlTagNameMapping().get(tagName);
//        if (klass != null) {
//            String jsClassName = JavaScriptConfiguration.getHtmlJavaScriptMapping().get(klass).getName();
//            jsClassName = jsClassName.substring(jsClassName.lastIndexOf('.') + 1);
//            final ClassConfiguration config =
//                JavaScriptConfiguration.getInstance(page.getWebClient().getBrowserVersion())
//                    .getClassConfiguration(jsClassName);
//            if (config == null) {
//                return UnknownElementFactory.instance.createElementNS(
//                        page, namespaceURI, qualifiedName, attributes);
//            }
//        }
        if (tagName.equals(HtmlAbbreviated.TAG_NAME)) {
            element = new HtmlAbbreviated(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlAcronym.TAG_NAME)) {
            element = new HtmlAcronym(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlAddress.TAG_NAME)) {
            element = new HtmlAddress(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlAnchor.TAG_NAME)) {
            element = new HtmlAnchor(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlApplet.TAG_NAME)) {
            element = new HtmlApplet(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlArea.TAG_NAME)) {
            element = new HtmlArea(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlArticle.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlArticle(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlAside.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlAside(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlAudio.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlAudio(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlBackgroundSound.TAG_NAME)) {
            element = new HtmlBackgroundSound(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBase.TAG_NAME)) {
            element = new HtmlBase(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBaseFont.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTMLBASEFONT_SUPPORTED)) {
                element = new HtmlBaseFont(qualifiedName, page, attributeMap);
            }
            else {
                element = new HtmlSpan(qualifiedName, page, attributeMap);
            }
        }
        else if (tagName.equals(HtmlBidirectionalOverride.TAG_NAME)) {
            element = new HtmlBidirectionalOverride(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBig.TAG_NAME)) {
            element = new HtmlBig(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBlink.TAG_NAME)) {
            element = new HtmlBlink(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBlockQuote.TAG_NAME)) {
            element = new HtmlBlockQuote(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBody.TAG_NAME)) {
            element = new HtmlBody(qualifiedName, page, attributeMap, false);
        }
        else if (tagName.equals(HtmlBold.TAG_NAME)) {
            element = new HtmlBold(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlBreak.TAG_NAME)) {
            element = new HtmlBreak(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlButton.TAG_NAME)) {
            element = new HtmlButton(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlCanvas.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(CANVAS)) {
                element = new HtmlCanvas(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlCaption.TAG_NAME)) {
            element = new HtmlCaption(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlCenter.TAG_NAME)) {
            element = new HtmlCenter(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlCitation.TAG_NAME)) {
            element = new HtmlCitation(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlCode.TAG_NAME)) {
            element = new HtmlCode(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDataList.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlDataList(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlDefinition.TAG_NAME)) {
            element = new HtmlDefinition(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDefinitionDescription.TAG_NAME)) {
            element = new HtmlDefinitionDescription(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDefinitionList.TAG_NAME)) {
            element = new HtmlDefinitionList(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDefinitionTerm.TAG_NAME)) {
            element = new HtmlDefinitionTerm(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDeletedText.TAG_NAME)) {
            element = new HtmlDeletedText(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDirectory.TAG_NAME)) {
            element = new HtmlDirectory(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlDivision.TAG_NAME)) {
            element = new HtmlDivision(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlEmbed.TAG_NAME)) {
            element = new HtmlEmbed(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlEmphasis.TAG_NAME)) {
            element = new HtmlEmphasis(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlFieldSet.TAG_NAME)) {
            element = new HtmlFieldSet(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlFigure.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlFigure(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlFigureCaption.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlFigureCaption(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlFont.TAG_NAME)) {
            element = new HtmlFont(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlForm.TAG_NAME)) {
            element = new HtmlForm(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlFooter.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlFooter(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlFrame.TAG_NAME)) {
            if (attributeMap != null) {
                final DomAttr srcAttribute = attributeMap.get("src");
                if (srcAttribute != null) {
                    srcAttribute.setValue(srcAttribute.getValue().trim());
                }
            }
            element = new HtmlFrame(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlFrameSet.TAG_NAME)) {
            element = new HtmlFrameSet(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHead.TAG_NAME)) {
            element = new HtmlHead(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeader.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlHeader(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlHeading1.TAG_NAME)) {
            element = new HtmlHeading1(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading2.TAG_NAME)) {
            element = new HtmlHeading2(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading3.TAG_NAME)) {
            element = new HtmlHeading3(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading4.TAG_NAME)) {
            element = new HtmlHeading4(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading5.TAG_NAME)) {
            element = new HtmlHeading5(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHeading6.TAG_NAME)) {
            element = new HtmlHeading6(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHorizontalRule.TAG_NAME)) {
            element = new HtmlHorizontalRule(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlHtml.TAG_NAME)) {
            element = new HtmlHtml(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlImage.TAG_NAME) || tagName.equals(HtmlImage.TAG_NAME2)) {
            element = new HtmlImage(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlInlineFrame.TAG_NAME)) {
            if (attributeMap != null) {
                final DomAttr srcAttribute = attributeMap.get("src");
                if (srcAttribute != null) {
                    srcAttribute.setValue(srcAttribute.getValue().trim());
                }
            }
            element = new HtmlInlineFrame(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlInlineQuotation.TAG_NAME)) {
            element = new HtmlInlineQuotation(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlInsertedText.TAG_NAME)) {
            element = new HtmlInsertedText(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlIsIndex.TAG_NAME)) {
            element = new HtmlIsIndex(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlItalic.TAG_NAME)) {
            element = new HtmlItalic(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlKeyboard.TAG_NAME)) {
            element = new HtmlKeyboard(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlKeygen.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlKeygen(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlLabel.TAG_NAME)) {
            element = new HtmlLabel(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlLegend.TAG_NAME)) {
            element = new HtmlLegend(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlLink.TAG_NAME)) {
            element = new HtmlLink(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlListing.TAG_NAME)) {
            element = new HtmlListing(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlListItem.TAG_NAME)) {
            element = new HtmlListItem(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlMap.TAG_NAME)) {
            element = new HtmlMap(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlMark.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlMark(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlMarquee.TAG_NAME)) {
            element = new HtmlMarquee(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlMenu.TAG_NAME)) {
            element = new HtmlMenu(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlMeta.TAG_NAME)) {
            element = new HtmlMeta(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlMeter.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlMeter(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlMultiColumn.TAG_NAME)) {
            element = new HtmlMultiColumn(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlNav.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlNav(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlNextId.TAG_NAME)) {
            element = new HtmlNextId(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlNoBreak.TAG_NAME)) {
            element = new HtmlNoBreak(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlNoEmbed.TAG_NAME)) {
            element = new HtmlNoEmbed(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlNoFrames.TAG_NAME)) {
            element = new HtmlNoFrames(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlNoScript.TAG_NAME)) {
            element = new HtmlNoScript(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlObject.TAG_NAME)) {
            element = new HtmlObject(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlOption.TAG_NAME)) {
            element = new HtmlOption(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlOptionGroup.TAG_NAME)) {
            element = new HtmlOptionGroup(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlOrderedList.TAG_NAME)) {
            element = new HtmlOrderedList(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlOutput.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlOutput(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlParagraph.TAG_NAME)) {
            element = new HtmlParagraph(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlParameter.TAG_NAME)) {
            element = new HtmlParameter(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlPlainText.TAG_NAME)) {
            element = new HtmlPlainText(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlPreformattedText.TAG_NAME)) {
            element = new HtmlPreformattedText(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlProgress.TAG_NAME)) {
            element = new HtmlProgress(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlRp.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_RUBY_TAGS)) {
                element = new HtmlRp(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlRt.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_RUBY_TAGS)) {
                element = new HtmlRt(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlRuby.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_RUBY_TAGS)) {
                element = new HtmlRuby(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlS.TAG_NAME)) {
            element = new HtmlS(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlSample.TAG_NAME)) {
            element = new HtmlSample(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlScript.TAG_NAME)) {
            element = new HtmlScript(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlSection.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlSection(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlSelect.TAG_NAME)) {
            element = new HtmlSelect(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlSmall.TAG_NAME)) {
            element = new HtmlSmall(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlSource.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlSource(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlSpan.TAG_NAME)) {
            element = new HtmlSpan(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlStrike.TAG_NAME)) {
            element = new HtmlStrike(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlStrong.TAG_NAME)) {
            element = new HtmlStrong(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlStyle.TAG_NAME)) {
            element = new HtmlStyle(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlSubscript.TAG_NAME)) {
            element = new HtmlSubscript(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlSuperscript.TAG_NAME)) {
            element = new HtmlSuperscript(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTable.TAG_NAME)) {
            element = new HtmlTable(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableBody.TAG_NAME)) {
            element = new HtmlTableBody(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableColumn.TAG_NAME)) {
            element = new HtmlTableColumn(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableColumnGroup.TAG_NAME)) {
            element = new HtmlTableColumnGroup(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableDataCell.TAG_NAME)) {
            element = new HtmlTableDataCell(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableFooter.TAG_NAME)) {
            element = new HtmlTableFooter(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableHeader.TAG_NAME)) {
            element = new HtmlTableHeader(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableHeaderCell.TAG_NAME)) {
            element = new HtmlTableHeaderCell(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTableRow.TAG_NAME)) {
            element = new HtmlTableRow(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTeletype.TAG_NAME)) {
            element = new HtmlTeletype(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTextArea.TAG_NAME)) {
            element = new HtmlTextArea(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTitle.TAG_NAME)) {
            element = new HtmlTitle(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlTime.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlTime(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlUnderlined.TAG_NAME)) {
            element = new HtmlUnderlined(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlUnorderedList.TAG_NAME)) {
            element = new HtmlUnorderedList(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlVariable.TAG_NAME)) {
            element = new HtmlVariable(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlVideo.TAG_NAME)) {
            if (page.getWebClient().getBrowserVersion().hasFeature(HTML5_TAGS)) {
                element = new HtmlVideo(qualifiedName, page, attributeMap);
            }
            else {
                return UnknownElementFactory.instance.createElementNS(page, namespaceURI, qualifiedName, attributes);
            }
        }
        else if (tagName.equals(HtmlWordBreak.TAG_NAME)) {
            element = new HtmlWordBreak(qualifiedName, page, attributeMap);
        }
        else if (tagName.equals(HtmlExample.TAG_NAME)) {
            element = new HtmlExample(qualifiedName, page, attributeMap);
        }
        else {
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
            attributeMap = new LinkedHashMap<String, DomAttr>(attributes.getLength());
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
