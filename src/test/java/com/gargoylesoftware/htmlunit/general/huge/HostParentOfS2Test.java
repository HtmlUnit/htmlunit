/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.general.huge;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.junit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'S'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfS2Test extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'S' && StringUtils.compareIgnoreCase(input, "SVGG") >= 0;
        });
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGGElement_SVGGElement() throws Exception {
        test("SVGGElement", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGeometryElement_SVGCircleElement() throws Exception {
        test("SVGGeometryElement", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGeometryElement_SVGEllipseElement() throws Exception {
        test("SVGGeometryElement", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGeometryElement_SVGGeometryElement() throws Exception {
        test("SVGGeometryElement", "SVGGeometryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGeometryElement_SVGLineElement() throws Exception {
        test("SVGGeometryElement", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGeometryElement_SVGPathElement() throws Exception {
        test("SVGGeometryElement", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGeometryElement_SVGPolygonElement() throws Exception {
        test("SVGGeometryElement", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGeometryElement_SVGPolylineElement() throws Exception {
        test("SVGGeometryElement", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGeometryElement_SVGRectElement() throws Exception {
        test("SVGGeometryElement", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGGradientElement_SVGGradientElement() throws Exception {
        test("SVGGradientElement", "SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGGradientElement_SVGLinearGradientElement() throws Exception {
        test("SVGGradientElement", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGGradientElement_SVGRadialGradientElement() throws Exception {
        test("SVGGradientElement", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGAElement() throws Exception {
        test("SVGGraphicsElement", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGCircleElement() throws Exception {
        test("SVGGraphicsElement", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    @HtmlUnitNYI(CHROME = "false",
            EDGE = "false")
    public void _SVGGraphicsElement_SVGClipPathElement() throws Exception {
        test("SVGGraphicsElement", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGDefsElement() throws Exception {
        test("SVGGraphicsElement", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGEllipseElement() throws Exception {
        test("SVGGraphicsElement", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGForeignObjectElement() throws Exception {
        test("SVGGraphicsElement", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGGElement() throws Exception {
        test("SVGGraphicsElement", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGGeometryElement() throws Exception {
        test("SVGGraphicsElement", "SVGGeometryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGGraphicsElement() throws Exception {
        test("SVGGraphicsElement", "SVGGraphicsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGImageElement() throws Exception {
        test("SVGGraphicsElement", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGLineElement() throws Exception {
        test("SVGGraphicsElement", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGPathElement() throws Exception {
        test("SVGGraphicsElement", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGPolygonElement() throws Exception {
        test("SVGGraphicsElement", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGPolylineElement() throws Exception {
        test("SVGGraphicsElement", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGRectElement() throws Exception {
        test("SVGGraphicsElement", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGSVGElement() throws Exception {
        test("SVGGraphicsElement", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGSwitchElement() throws Exception {
        test("SVGGraphicsElement", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGTextContentElement() throws Exception {
        test("SVGGraphicsElement", "SVGTextContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGTextElement() throws Exception {
        test("SVGGraphicsElement", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGTextPathElement() throws Exception {
        test("SVGGraphicsElement", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGTextPositioningElement() throws Exception {
        test("SVGGraphicsElement", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGTSpanElement() throws Exception {
        test("SVGGraphicsElement", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGGraphicsElement_SVGUseElement() throws Exception {
        test("SVGGraphicsElement", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGImageElement_SVGImageElement() throws Exception {
        test("SVGImageElement", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGLength_SVGLength() throws Exception {
        test("SVGLength", "SVGLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGLengthList_SVGLengthList() throws Exception {
        test("SVGLengthList", "SVGLengthList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGLinearGradientElement_SVGLinearGradientElement() throws Exception {
        test("SVGLinearGradientElement", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGLineElement_SVGLineElement() throws Exception {
        test("SVGLineElement", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGMarkerElement_SVGMarkerElement() throws Exception {
        test("SVGMarkerElement", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGMaskElement_SVGMaskElement() throws Exception {
        test("SVGMaskElement", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGMatrix_SVGMatrix() throws Exception {
        test("SVGMatrix", "SVGMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGMetadataElement_SVGMetadataElement() throws Exception {
        test("SVGMetadataElement", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGMPathElement_SVGMPathElement() throws Exception {
        test("SVGMPathElement", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGNumber_SVGNumber() throws Exception {
        test("SVGNumber", "SVGNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGNumberList_SVGNumberList() throws Exception {
        test("SVGNumberList", "SVGNumberList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGPathElement_SVGPathElement() throws Exception {
        test("SVGPathElement", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSeg() throws Exception {
        test("SVGPathSeg", "SVGPathSeg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegArcAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegArcAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegArcRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegArcRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegClosePath() throws Exception {
        test("SVGPathSeg", "SVGPathSegClosePath");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegCurvetoCubicAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoCubicAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegCurvetoCubicRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoCubicRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegCurvetoCubicSmoothAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoCubicSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegCurvetoCubicSmoothRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoCubicSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegCurvetoQuadraticAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoQuadraticAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegCurvetoQuadraticRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoQuadraticRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegCurvetoQuadraticSmoothAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoQuadraticSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegCurvetoQuadraticSmoothRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegCurvetoQuadraticSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegLinetoAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegLinetoHorizontalAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoHorizontalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegLinetoHorizontalRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoHorizontalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegLinetoRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegLinetoVerticalAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoVerticalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegLinetoVerticalRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegLinetoVerticalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegMovetoAbs() throws Exception {
        test("SVGPathSeg", "SVGPathSegMovetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSeg_SVGPathSegMovetoRel() throws Exception {
        test("SVGPathSeg", "SVGPathSegMovetoRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegArcAbs_SVGPathSegArcAbs() throws Exception {
        test("SVGPathSegArcAbs", "SVGPathSegArcAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegArcRel_SVGPathSegArcRel() throws Exception {
        test("SVGPathSegArcRel", "SVGPathSegArcRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegClosePath_SVGPathSegClosePath() throws Exception {
        test("SVGPathSegClosePath", "SVGPathSegClosePath");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegCurvetoCubicAbs_SVGPathSegCurvetoCubicAbs() throws Exception {
        test("SVGPathSegCurvetoCubicAbs", "SVGPathSegCurvetoCubicAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegCurvetoCubicRel_SVGPathSegCurvetoCubicRel() throws Exception {
        test("SVGPathSegCurvetoCubicRel", "SVGPathSegCurvetoCubicRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegCurvetoCubicSmoothAbs_SVGPathSegCurvetoCubicSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothAbs", "SVGPathSegCurvetoCubicSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegCurvetoCubicSmoothRel_SVGPathSegCurvetoCubicSmoothRel() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothRel", "SVGPathSegCurvetoCubicSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegCurvetoQuadraticAbs_SVGPathSegCurvetoQuadraticAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticAbs", "SVGPathSegCurvetoQuadraticAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegCurvetoQuadraticRel_SVGPathSegCurvetoQuadraticRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticRel", "SVGPathSegCurvetoQuadraticRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegCurvetoQuadraticSmoothAbs_SVGPathSegCurvetoQuadraticSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothAbs", "SVGPathSegCurvetoQuadraticSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegCurvetoQuadraticSmoothRel_SVGPathSegCurvetoQuadraticSmoothRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothRel", "SVGPathSegCurvetoQuadraticSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegLinetoAbs_SVGPathSegLinetoAbs() throws Exception {
        test("SVGPathSegLinetoAbs", "SVGPathSegLinetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegLinetoHorizontalAbs_SVGPathSegLinetoHorizontalAbs() throws Exception {
        test("SVGPathSegLinetoHorizontalAbs", "SVGPathSegLinetoHorizontalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegLinetoHorizontalRel_SVGPathSegLinetoHorizontalRel() throws Exception {
        test("SVGPathSegLinetoHorizontalRel", "SVGPathSegLinetoHorizontalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegLinetoRel_SVGPathSegLinetoRel() throws Exception {
        test("SVGPathSegLinetoRel", "SVGPathSegLinetoRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegLinetoVerticalAbs_SVGPathSegLinetoVerticalAbs() throws Exception {
        test("SVGPathSegLinetoVerticalAbs", "SVGPathSegLinetoVerticalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegLinetoVerticalRel_SVGPathSegLinetoVerticalRel() throws Exception {
        test("SVGPathSegLinetoVerticalRel", "SVGPathSegLinetoVerticalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF_ESR = "true",
            IE = "true")
    public void _SVGPathSegList_SVGPathSegList() throws Exception {
        test("SVGPathSegList", "SVGPathSegList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegMovetoAbs_SVGPathSegMovetoAbs() throws Exception {
        test("SVGPathSegMovetoAbs", "SVGPathSegMovetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGPathSegMovetoRel_SVGPathSegMovetoRel() throws Exception {
        test("SVGPathSegMovetoRel", "SVGPathSegMovetoRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGPatternElement_SVGPatternElement() throws Exception {
        test("SVGPatternElement", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGPoint_SVGPoint() throws Exception {
        test("SVGPoint", "SVGPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGPointList_SVGPointList() throws Exception {
        test("SVGPointList", "SVGPointList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGPolygonElement_SVGPolygonElement() throws Exception {
        test("SVGPolygonElement", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGPolylineElement_SVGPolylineElement() throws Exception {
        test("SVGPolylineElement", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGPreserveAspectRatio_SVGPreserveAspectRatio() throws Exception {
        test("SVGPreserveAspectRatio", "SVGPreserveAspectRatio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGRadialGradientElement_SVGRadialGradientElement() throws Exception {
        test("SVGRadialGradientElement", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGRect_SVGRect() throws Exception {
        test("SVGRect", "SVGRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGRectElement_SVGRectElement() throws Exception {
        test("SVGRectElement", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGScriptElement_SVGScriptElement() throws Exception {
        test("SVGScriptElement", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGSetElement_SVGSetElement() throws Exception {
        test("SVGSetElement", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGStopElement_SVGStopElement() throws Exception {
        test("SVGStopElement", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGStringList_SVGStringList() throws Exception {
        test("SVGStringList", "SVGStringList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGStyleElement_SVGStyleElement() throws Exception {
        test("SVGStyleElement", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGSVGElement_SVGSVGElement() throws Exception {
        test("SVGSVGElement", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGSwitchElement_SVGSwitchElement() throws Exception {
        test("SVGSwitchElement", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGSymbolElement_SVGSymbolElement() throws Exception {
        test("SVGSymbolElement", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTextContentElement_SVGTextContentElement() throws Exception {
        test("SVGTextContentElement", "SVGTextContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTextContentElement_SVGTextElement() throws Exception {
        test("SVGTextContentElement", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTextContentElement_SVGTextPathElement() throws Exception {
        test("SVGTextContentElement", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTextContentElement_SVGTextPositioningElement() throws Exception {
        test("SVGTextContentElement", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTextContentElement_SVGTSpanElement() throws Exception {
        test("SVGTextContentElement", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTextElement_SVGTextElement() throws Exception {
        test("SVGTextElement", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTextPathElement_SVGTextPathElement() throws Exception {
        test("SVGTextPathElement", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTextPositioningElement_SVGTextElement() throws Exception {
        test("SVGTextPositioningElement", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTextPositioningElement_SVGTextPositioningElement() throws Exception {
        test("SVGTextPositioningElement", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTextPositioningElement_SVGTSpanElement() throws Exception {
        test("SVGTextPositioningElement", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTitleElement_SVGTitleElement() throws Exception {
        test("SVGTitleElement", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTransform_SVGTransform() throws Exception {
        test("SVGTransform", "SVGTransform");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTransformList_SVGTransformList() throws Exception {
        test("SVGTransformList", "SVGTransformList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGTSpanElement_SVGTSpanElement() throws Exception {
        test("SVGTSpanElement", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    @HtmlUnitNYI(FF = "true",
            FF_ESR = "true",
            IE = "true")
    public void _SVGUnitTypes_SVGUnitTypes() throws Exception {
        test("SVGUnitTypes", "SVGUnitTypes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGUseElement_SVGUseElement() throws Exception {
        test("SVGUseElement", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGViewElement_SVGViewElement() throws Exception {
        test("SVGViewElement", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _SVGZoomEvent_SVGZoomEvent() throws Exception {
        test("SVGZoomEvent", "SVGZoomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Symbol_Symbol() throws Exception {
        test("Symbol", "Symbol");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _SyncManager_SyncManager() throws Exception {
        test("SyncManager", "SyncManager");
    }
}
