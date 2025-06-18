/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.general.huge;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'S'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfS2Test extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'S' && StringUtils.compareIgnoreCase(input, "SVGG") >= 0;
        });
    }

    @Alerts("true/false")
    void _SVGGElement_SVGGElement() throws Exception {
        test("SVGGElement", "SVGGElement");
    }

    @Alerts("true/true")
    void _SVGGeometryElement_SVGCircleElement() throws Exception {
        test("SVGGeometryElement", "SVGCircleElement");
    }

    @Alerts("true/true")
    void _SVGGeometryElement_SVGEllipseElement() throws Exception {
        test("SVGGeometryElement", "SVGEllipseElement");
    }

    @Alerts("true/false")
    void _SVGGeometryElement_SVGGeometryElement() throws Exception {
        test("SVGGeometryElement", "SVGGeometryElement");
    }

    @Alerts("true/true")
    void _SVGGeometryElement_SVGLineElement() throws Exception {
        test("SVGGeometryElement", "SVGLineElement");
    }

    @Alerts("true/true")
    void _SVGGeometryElement_SVGPathElement() throws Exception {
        test("SVGGeometryElement", "SVGPathElement");
    }

    @Alerts("true/true")
    void _SVGGeometryElement_SVGPolygonElement() throws Exception {
        test("SVGGeometryElement", "SVGPolygonElement");
    }

    @Alerts("true/true")
    void _SVGGeometryElement_SVGPolylineElement() throws Exception {
        test("SVGGeometryElement", "SVGPolylineElement");
    }

    @Alerts("true/true")
    void _SVGGeometryElement_SVGRectElement() throws Exception {
        test("SVGGeometryElement", "SVGRectElement");
    }

    @Alerts("true/false")
    void _SVGGradientElement_SVGGradientElement() throws Exception {
        test("SVGGradientElement", "SVGGradientElement");
    }

    @Alerts("true/true")
    void _SVGGradientElement_SVGLinearGradientElement() throws Exception {
        test("SVGGradientElement", "SVGLinearGradientElement");
    }

    @Alerts("true/true")
    void _SVGGradientElement_SVGRadialGradientElement() throws Exception {
        test("SVGGradientElement", "SVGRadialGradientElement");
    }

    @Alerts("true/true")
    void _SVGGraphicsElement_SVGAElement() throws Exception {
        test("SVGGraphicsElement", "SVGAElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGCircleElement() throws Exception {
        test("SVGGraphicsElement", "SVGCircleElement");
    }

    @Alerts("false/false")
    void _SVGGraphicsElement_SVGClipPathElement() throws Exception {
        test("SVGGraphicsElement", "SVGClipPathElement");
    }

    @Alerts("true/true")
    void _SVGGraphicsElement_SVGDefsElement() throws Exception {
        test("SVGGraphicsElement", "SVGDefsElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGEllipseElement() throws Exception {
        test("SVGGraphicsElement", "SVGEllipseElement");
    }

    @Alerts("true/true")
    void _SVGGraphicsElement_SVGForeignObjectElement() throws Exception {
        test("SVGGraphicsElement", "SVGForeignObjectElement");
    }

    @Alerts("true/true")
    void _SVGGraphicsElement_SVGGElement() throws Exception {
        test("SVGGraphicsElement", "SVGGElement");
    }

    @Alerts("true/true")
    void _SVGGraphicsElement_SVGGeometryElement() throws Exception {
        test("SVGGraphicsElement", "SVGGeometryElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGGraphicsElement() throws Exception {
        test("SVGGraphicsElement", "SVGGraphicsElement");
    }

    @Alerts("true/true")
    void _SVGGraphicsElement_SVGImageElement() throws Exception {
        test("SVGGraphicsElement", "SVGImageElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGLineElement() throws Exception {
        test("SVGGraphicsElement", "SVGLineElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGPathElement() throws Exception {
        test("SVGGraphicsElement", "SVGPathElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGPolygonElement() throws Exception {
        test("SVGGraphicsElement", "SVGPolygonElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGPolylineElement() throws Exception {
        test("SVGGraphicsElement", "SVGPolylineElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGRectElement() throws Exception {
        test("SVGGraphicsElement", "SVGRectElement");
    }

    @Alerts("true/true")
    void _SVGGraphicsElement_SVGSVGElement() throws Exception {
        test("SVGGraphicsElement", "SVGSVGElement");
    }

    @Alerts("true/true")
    void _SVGGraphicsElement_SVGSwitchElement() throws Exception {
        test("SVGGraphicsElement", "SVGSwitchElement");
    }

    @Alerts("true/true")
    void _SVGGraphicsElement_SVGTextContentElement() throws Exception {
        test("SVGGraphicsElement", "SVGTextContentElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGTextElement() throws Exception {
        test("SVGGraphicsElement", "SVGTextElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGTextPathElement() throws Exception {
        test("SVGGraphicsElement", "SVGTextPathElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGTextPositioningElement() throws Exception {
        test("SVGGraphicsElement", "SVGTextPositioningElement");
    }

    @Alerts("true/false")
    void _SVGGraphicsElement_SVGTSpanElement() throws Exception {
        test("SVGGraphicsElement", "SVGTSpanElement");
    }

    @Alerts("true/true")
    void _SVGGraphicsElement_SVGUseElement() throws Exception {
        test("SVGGraphicsElement", "SVGUseElement");
    }

    @Alerts("true/false")
    void _SVGImageElement_SVGImageElement() throws Exception {
        test("SVGImageElement", "SVGImageElement");
    }

    @Alerts("true/false")
    void _SVGLength_SVGLength() throws Exception {
        test("SVGLength", "SVGLength");
    }

    @Alerts("true/false")
    void _SVGLengthList_SVGLengthList() throws Exception {
        test("SVGLengthList", "SVGLengthList");
    }

    @Alerts("true/false")
    void _SVGLinearGradientElement_SVGLinearGradientElement() throws Exception {
        test("SVGLinearGradientElement", "SVGLinearGradientElement");
    }

    @Alerts("true/false")
    void _SVGLineElement_SVGLineElement() throws Exception {
        test("SVGLineElement", "SVGLineElement");
    }

    @Alerts("true/false")
    void _SVGMarkerElement_SVGMarkerElement() throws Exception {
        test("SVGMarkerElement", "SVGMarkerElement");
    }

    @Alerts("true/false")
    void _SVGMaskElement_SVGMaskElement() throws Exception {
        test("SVGMaskElement", "SVGMaskElement");
    }

    @Alerts("true/false")
    void _SVGMatrix_SVGMatrix() throws Exception {
        test("SVGMatrix", "SVGMatrix");
    }

    @Alerts("true/false")
    void _SVGMetadataElement_SVGMetadataElement() throws Exception {
        test("SVGMetadataElement", "SVGMetadataElement");
    }

    @Alerts("true/false")
    void _SVGMPathElement_SVGMPathElement() throws Exception {
        test("SVGMPathElement", "SVGMPathElement");
    }

    @Alerts("true/false")
    void _SVGNumber_SVGNumber() throws Exception {
        test("SVGNumber", "SVGNumber");
    }

    @Alerts("true/false")
    void _SVGNumberList_SVGNumberList() throws Exception {
        test("SVGNumberList", "SVGNumberList");
    }

    @Alerts("true/false")
    void _SVGPathElement_SVGPathElement() throws Exception {
        test("SVGPathElement", "SVGPathElement");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSeg() throws Exception {
        test("SVGPathSeg", "SVGPathSeg");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegArcAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegArcAbs");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegArcRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegArcRel");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegClosePath() throws Exception {
        test("SVGPathSeg", "SVGPathSegClosePath");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegCurvetoCubicAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoCubicAbs");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegCurvetoCubicRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoCubicRel");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegCurvetoCubicSmoothAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoCubicSmoothAbs");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegCurvetoCubicSmoothRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoCubicSmoothRel");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegCurvetoQuadraticAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoQuadraticAbs");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegCurvetoQuadraticRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoQuadraticRel");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegCurvetoQuadraticSmoothAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoQuadraticSmoothAbs");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegCurvetoQuadraticSmoothRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoQuadraticSmoothRel");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegLinetoAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoAbs");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegLinetoHorizontalAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoHorizontalAbs");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegLinetoHorizontalRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoHorizontalRel");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegLinetoRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoRel");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegLinetoVerticalAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoVerticalAbs");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegLinetoVerticalRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoVerticalRel");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegMovetoAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegMovetoAbs");
    }

    @Alerts("false/false")
    void _SVGPathSeg_SVGPathSegMovetoRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegMovetoRel");
    }

    @Alerts("false/false")
    void _SVGPathSegArcAbs_SVGPathSegArcAbs() throws Exception {
        test("SVGPathSegArcAbs", "SVGPathSegArcAbs");
    }

    @Alerts("false/false")
    void _SVGPathSegArcRel_SVGPathSegArcRel() throws Exception {
        test("SVGPathSegArcRel", "SVGPathSegArcRel");
    }

    @Alerts("false/false")
    void _SVGPathSegClosePath_SVGPathSegClosePath() throws Exception {
        test("SVGPathSegClosePath", "SVGPathSegClosePath");
    }

    @Alerts("false/false")
    void _SVGPathSegCurvetoCubicAbs_SVGPathSegCurvetoCubicAbs() throws Exception {
        test("SVGPathSegCurvetoCubicAbs", "SVGPathSegCurvetoCubicAbs");
    }

    @Alerts("false/false")
    void _SVGPathSegCurvetoCubicRel_SVGPathSegCurvetoCubicRel() throws Exception {
        test("SVGPathSegCurvetoCubicRel", "SVGPathSegCurvetoCubicRel");
    }

    @Alerts("false/false")
    void _SVGPathSegCurvetoCubicSmoothAbs_SVGPathSegCurvetoCubicSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothAbs", "SVGPathSegCurvetoCubicSmoothAbs");
    }

    @Alerts("false/false")
    void _SVGPathSegCurvetoCubicSmoothRel_SVGPathSegCurvetoCubicSmoothRel() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothRel", "SVGPathSegCurvetoCubicSmoothRel");
    }

    @Alerts("false/false")
    void _SVGPathSegCurvetoQuadraticAbs_SVGPathSegCurvetoQuadraticAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticAbs", "SVGPathSegCurvetoQuadraticAbs");
    }

    @Alerts("false/false")
    void _SVGPathSegCurvetoQuadraticRel_SVGPathSegCurvetoQuadraticRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticRel", "SVGPathSegCurvetoQuadraticRel");
    }

    @Alerts("false/false")
    void _SVGPathSegCurvetoQuadraticSmoothAbs_SVGPathSegCurvetoQuadraticSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothAbs", "SVGPathSegCurvetoQuadraticSmoothAbs");
    }

    @Alerts("false/false")
    void _SVGPathSegCurvetoQuadraticSmoothRel_SVGPathSegCurvetoQuadraticSmoothRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothRel", "SVGPathSegCurvetoQuadraticSmoothRel");
    }

    @Alerts("false/false")
    void _SVGPathSegLinetoAbs_SVGPathSegLinetoAbs() throws Exception {
        test("SVGPathSegLinetoAbs", "SVGPathSegLinetoAbs");
    }

    @Alerts("false/false")
    void _SVGPathSegLinetoHorizontalAbs_SVGPathSegLinetoHorizontalAbs() throws Exception {
        test("SVGPathSegLinetoHorizontalAbs", "SVGPathSegLinetoHorizontalAbs");
    }

    @Alerts("false/false")
    void _SVGPathSegLinetoHorizontalRel_SVGPathSegLinetoHorizontalRel() throws Exception {
        test("SVGPathSegLinetoHorizontalRel", "SVGPathSegLinetoHorizontalRel");
    }

    @Alerts("false/false")
    void _SVGPathSegLinetoRel_SVGPathSegLinetoRel() throws Exception {
        test("SVGPathSegLinetoRel", "SVGPathSegLinetoRel");
    }

    @Alerts("false/false")
    void _SVGPathSegLinetoVerticalAbs_SVGPathSegLinetoVerticalAbs() throws Exception {
        test("SVGPathSegLinetoVerticalAbs", "SVGPathSegLinetoVerticalAbs");
    }

    @Alerts("false/false")
    void _SVGPathSegLinetoVerticalRel_SVGPathSegLinetoVerticalRel() throws Exception {
        test("SVGPathSegLinetoVerticalRel", "SVGPathSegLinetoVerticalRel");
    }

    @Alerts("false/false")
    void _SVGPathSegList_SVGPathSegList() throws Exception {
        test("SVGPathSegList", "SVGPathSegList");
    }

    @Alerts("false/false")
    void _SVGPathSegMovetoAbs_SVGPathSegMovetoAbs() throws Exception {
        test("SVGPathSegMovetoAbs", "SVGPathSegMovetoAbs");
    }

    @Alerts("false/false")
    void _SVGPathSegMovetoRel_SVGPathSegMovetoRel() throws Exception {
        test("SVGPathSegMovetoRel", "SVGPathSegMovetoRel");
    }

    @Alerts("true/false")
    void _SVGPatternElement_SVGPatternElement() throws Exception {
        test("SVGPatternElement", "SVGPatternElement");
    }

    @Alerts("true/false")
    void _SVGPoint_SVGPoint() throws Exception {
        test("SVGPoint", "SVGPoint");
    }

    @Alerts("true/false")
    void _SVGPointList_SVGPointList() throws Exception {
        test("SVGPointList", "SVGPointList");
    }

    @Alerts("true/false")
    void _SVGPolygonElement_SVGPolygonElement() throws Exception {
        test("SVGPolygonElement", "SVGPolygonElement");
    }

    @Alerts("true/false")
    void _SVGPolylineElement_SVGPolylineElement() throws Exception {
        test("SVGPolylineElement", "SVGPolylineElement");
    }

    @Alerts("true/false")
    void _SVGPreserveAspectRatio_SVGPreserveAspectRatio() throws Exception {
        test("SVGPreserveAspectRatio", "SVGPreserveAspectRatio");
    }

    @Alerts("true/false")
    void _SVGRadialGradientElement_SVGRadialGradientElement() throws Exception {
        test("SVGRadialGradientElement", "SVGRadialGradientElement");
    }

    @Alerts("true/false")
    void _SVGRect_SVGRect() throws Exception {
        test("SVGRect", "SVGRect");
    }

    @Alerts("true/false")
    void _SVGRectElement_SVGRectElement() throws Exception {
        test("SVGRectElement", "SVGRectElement");
    }

    @Alerts("true/false")
    void _SVGScriptElement_SVGScriptElement() throws Exception {
        test("SVGScriptElement", "SVGScriptElement");
    }

    @Alerts("true/false")
    void _SVGSetElement_SVGSetElement() throws Exception {
        test("SVGSetElement", "SVGSetElement");
    }

    @Alerts("true/false")
    void _SVGStopElement_SVGStopElement() throws Exception {
        test("SVGStopElement", "SVGStopElement");
    }

    @Alerts("true/false")
    void _SVGStringList_SVGStringList() throws Exception {
        test("SVGStringList", "SVGStringList");
    }

    @Alerts("true/false")
    void _SVGStyleElement_SVGStyleElement() throws Exception {
        test("SVGStyleElement", "SVGStyleElement");
    }

    @Alerts("true/false")
    void _SVGSVGElement_SVGSVGElement() throws Exception {
        test("SVGSVGElement", "SVGSVGElement");
    }

    @Alerts("true/false")
    void _SVGSwitchElement_SVGSwitchElement() throws Exception {
        test("SVGSwitchElement", "SVGSwitchElement");
    }

    @Alerts("true/false")
    void _SVGSymbolElement_SVGSymbolElement() throws Exception {
        test("SVGSymbolElement", "SVGSymbolElement");
    }

    @Alerts("true/false")
    void _SVGTextContentElement_SVGTextContentElement() throws Exception {
        test("SVGTextContentElement", "SVGTextContentElement");
    }

    @Alerts("true/false")
    void _SVGTextContentElement_SVGTextElement() throws Exception {
        test("SVGTextContentElement", "SVGTextElement");
    }

    @Alerts("true/true")
    void _SVGTextContentElement_SVGTextPathElement() throws Exception {
        test("SVGTextContentElement", "SVGTextPathElement");
    }

    @Alerts("true/true")
    void _SVGTextContentElement_SVGTextPositioningElement() throws Exception {
        test("SVGTextContentElement", "SVGTextPositioningElement");
    }

    @Alerts("true/false")
    void _SVGTextContentElement_SVGTSpanElement() throws Exception {
        test("SVGTextContentElement", "SVGTSpanElement");
    }

    @Alerts("true/false")
    void _SVGTextElement_SVGTextElement() throws Exception {
        test("SVGTextElement", "SVGTextElement");
    }

    @Alerts("true/false")
    void _SVGTextPathElement_SVGTextPathElement() throws Exception {
        test("SVGTextPathElement", "SVGTextPathElement");
    }

    @Alerts("true/true")
    void _SVGTextPositioningElement_SVGTextElement() throws Exception {
        test("SVGTextPositioningElement", "SVGTextElement");
    }

    @Alerts("true/false")
    void _SVGTextPositioningElement_SVGTextPositioningElement() throws Exception {
        test("SVGTextPositioningElement", "SVGTextPositioningElement");
    }

    @Alerts("true/true")
    void _SVGTextPositioningElement_SVGTSpanElement() throws Exception {
        test("SVGTextPositioningElement", "SVGTSpanElement");
    }

    @Alerts("true/false")
    void _SVGTitleElement_SVGTitleElement() throws Exception {
        test("SVGTitleElement", "SVGTitleElement");
    }

    @Alerts("true/false")
    void _SVGTransform_SVGTransform() throws Exception {
        test("SVGTransform", "SVGTransform");
    }

    @Alerts("true/false")
    void _SVGTransformList_SVGTransformList() throws Exception {
        test("SVGTransformList", "SVGTransformList");
    }

    @Alerts("true/false")
    void _SVGTSpanElement_SVGTSpanElement() throws Exception {
        test("SVGTSpanElement", "SVGTSpanElement");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    @HtmlUnitNYI(FF = "true/false",
            FF_ESR = "true/false")
    void _SVGUnitTypes_SVGUnitTypes() throws Exception {
        test("SVGUnitTypes", "SVGUnitTypes");
    }

    @Alerts("true/false")
    void _SVGUseElement_SVGUseElement() throws Exception {
        test("SVGUseElement", "SVGUseElement");
    }

    @Alerts("true/false")
    void _SVGViewElement_SVGViewElement() throws Exception {
        test("SVGViewElement", "SVGViewElement");
    }

    @Alerts("false/false")
    void _SVGZoomEvent_SVGZoomEvent() throws Exception {
        test("SVGZoomEvent", "SVGZoomEvent");
    }

    @Alerts("true/false")
    void _Symbol_Symbol() throws Exception {
        test("Symbol", "Symbol");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _SyncManager_SyncManager() throws Exception {
        test("SyncManager", "SyncManager");
    }
}
