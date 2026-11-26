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
package org.htmlunit.javascript.host.canvas;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Path2D}.
 * The geometry is checked using {@code isPointInPath()} and {@code isPointInStroke()}.
 *
 * @author Ronald Brill
 */
public class Path2DTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("11 functions")
    public void functions() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    var names = ['addPath', 'arc', 'arcTo', 'bezierCurveTo', 'closePath', 'ellipse',\n"
            + "                 'lineTo', 'moveTo', 'quadraticCurveTo', 'rect', 'roundRect'];\n"
            + "    var count = 0;\n"
            + "    for (var i = 0; i < names.length; i++) {\n"
            + "      if (typeof p[names[i]] == 'function') { count++; } else { log(names[i]); }\n"
            + "    }\n"
            + "    log(count + ' functions');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void constructorEmpty() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    log(ctx.isPointInPath(p, 10, 10));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void constructorUndefined() throws Exception {
        final String js =
            "    var p = new Path2D(undefined);\n"
            + "    log(ctx.isPointInPath(p, 10, 10));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorCopy() throws Exception {
        final String js =
            "    var p1 = new Path2D();\n"
            + "    p1.rect(0, 0, 50, 50);\n"
            + "    var p = new Path2D(p1);\n"
            + "    p1.rect(100, 100, 50, 50);\n"
            + "    log(ctx.isPointInPath(p, 10, 10));\n"
            + "    log(ctx.isPointInPath(p, 120, 120));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void constructorCopyIndependent() throws Exception {
        final String js =
            "    var p1 = new Path2D();\n"
            + "    var p = new Path2D(p1);\n"
            + "    p.rect(0, 0, 50, 50);\n"
            + "    log(ctx.isPointInPath(p1, 10, 10));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvg() throws Exception {
        final String js =
            "    var p = new Path2D('M10 10 h 80 v 80 h -80 Z');\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n"
            + "    log(ctx.isPointInPath(p, 95, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgAbsolute() throws Exception {
        final String js =
            "    var p = new Path2D('M10,10 H90 V90 H10 Z');\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n"
            + "    log(ctx.isPointInPath(p, 95, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgRelative() throws Exception {
        final String js =
            "    var p = new Path2D('m10 10 l80 0 l0 80 l-80 0 z');\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n"
            + "    log(ctx.isPointInPath(p, 95, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgImplicitLineTo() throws Exception {
        final String js =
            "    var p = new Path2D('M10 10 90 10 90 90 10 90z');\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n"
            + "    log(ctx.isPointInPath(p, 95, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgCompact() throws Exception {
        final String js =
            "    var p = new Path2D('M10,10L90,10L90,90L10,90Z');\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n"
            + "    log(ctx.isPointInPath(p, 95, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgNumbers() throws Exception {
        final String js =
            "    var p = new Path2D('M1e1 1E1 l+80-0 l.0 8e1 l-80 0z');\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n"
            + "    log(ctx.isPointInPath(p, 95, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgArc() throws Exception {
        final String js =
            "    var p = new Path2D('M 10 50 A 40 40 0 0 1 90 50 Z');\n"
            + "    log(ctx.isPointInPath(p, 50, 30));\n"
            + "    log(ctx.isPointInPath(p, 50, 70));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void constructorSvgArcSweep() throws Exception {
        final String js =
            "    var p = new Path2D('M 10 50 A 40 40 0 0 0 90 50 Z');\n"
            + "    log(ctx.isPointInPath(p, 50, 30));\n"
            + "    log(ctx.isPointInPath(p, 50, 70));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgArcRadiusTooSmall() throws Exception {
        final String js =
            "    var p = new Path2D('M 10 50 A 1 1 0 0 1 90 50 Z');\n"
            + "    log(ctx.isPointInPath(p, 50, 30));\n"
            + "    log(ctx.isPointInPath(p, 50, 70));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgArcFlagsWithoutSeparator() throws Exception {
        final String js =
            "    var p = new Path2D('M10 50A40 40 0 0190 50Z');\n"
            + "    log(ctx.isPointInPath(p, 50, 30));\n"
            + "    log(ctx.isPointInPath(p, 50, 70));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgCubic() throws Exception {
        final String js =
            "    var p = new Path2D('M10 50 C 10 0, 90 0, 90 50 Z');\n"
            + "    log(ctx.isPointInPath(p, 50, 30));\n"
            + "    log(ctx.isPointInPath(p, 50, 60));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgSmoothCubic() throws Exception {
        final String js =
            "    var p = new Path2D('M10 50 C 10 0, 50 0, 50 50 S 90 100, 90 50 Z');\n"
            + "    log(ctx.isPointInPath(p, 30, 30));\n"
            + "    log(ctx.isPointInPath(p, 30, 60));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgQuadratic() throws Exception {
        final String js =
            "    var p = new Path2D('M10 50 Q 50 0 90 50 Z');\n"
            + "    log(ctx.isPointInPath(p, 50, 30));\n"
            + "    log(ctx.isPointInPath(p, 50, 20));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgSmoothQuadratic() throws Exception {
        final String js =
            "    var p = new Path2D('M10 50 Q 30 0 50 50 T 90 50 Z');\n"
            + "    log(ctx.isPointInPath(p, 30, 40));\n"
            + "    log(ctx.isPointInPath(p, 70, 40));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void constructorSvgInvalid() throws Exception {
        final String js =
            "    var p = new Path2D('M10 10 L 90 10 L 90 90 X 10 90 Z');\n"
            + "    log(ctx.isPointInPath(p, 80, 20));\n"
            + "    log(ctx.isPointInPath(p, 20, 80));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void constructorSvgWithoutMoveTo() throws Exception {
        final String js =
            "    var p = new Path2D('L10 10 H90 V90 H10 Z');\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void constructorSvgEmpty() throws Exception {
        final String js =
            "    var p = new Path2D('');\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void constructorSvgNumber() throws Exception {
        final String js =
            "    var p = new Path2D(42);\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false", "true"})
    public void constructorSvgTwoSubpaths() throws Exception {
        final String js =
            "    var p = new Path2D('M0 0 h100 v100 h-100 z M25 25 h50 v50 h-50 z');\n"
            + "    log(ctx.isPointInPath(p, 10, 10));\n"
            + "    log(ctx.isPointInPath(p, 50, 50, 'evenodd'));\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void addPath() throws Exception {
        final String js =
            "    var p1 = new Path2D();\n"
            + "    p1.rect(0, 0, 10, 10);\n"
            + "    var p = new Path2D();\n"
            + "    p.addPath(p1);\n"
            + "    log(ctx.isPointInPath(p, 5, 5));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void addPathCopy() throws Exception {
        final String js =
            "    var p1 = new Path2D();\n"
            + "    var p = new Path2D();\n"
            + "    p.addPath(p1);\n"
            + "    p1.rect(0, 0, 10, 10);\n"
            + "    log(ctx.isPointInPath(p, 5, 5));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void addPathMatrix() throws Exception {
        final String js =
            "    var p1 = new Path2D();\n"
            + "    p1.rect(0, 0, 10, 10);\n"
            + "    var p = new Path2D();\n"
            + "    p.addPath(p1, {a: 2, d: 2, e: 100, f: 0});\n"
            + "    log(ctx.isPointInPath(p, 5, 5));\n"
            + "    log(ctx.isPointInPath(p, 115, 15));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void addPathMatrixAlias() throws Exception {
        final String js =
            "    var p1 = new Path2D();\n"
            + "    p1.rect(0, 0, 10, 10);\n"
            + "    var p = new Path2D();\n"
            + "    p.addPath(p1, {m11: 2, m22: 2, m41: 0, m42: 0});\n"
            + "    log(ctx.isPointInPath(p, 15, 15));\n"
            + "    log(ctx.isPointInPath(p, 25, 25));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void addPathDOMMatrix() throws Exception {
        final String js =
            "    var p1 = new Path2D();\n"
            + "    p1.rect(0, 0, 10, 10);\n"
            + "    var p = new Path2D();\n"
            + "    p.addPath(p1, new DOMMatrix([1, 0, 0, 1, 50, 50]));\n"
            + "    log(ctx.isPointInPath(p, 5, 5));\n"
            + "    log(ctx.isPointInPath(p, 55, 55));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void addPathMatrixMismatch() throws Exception {
        final String js =
            "    var p1 = new Path2D();\n"
            + "    var p = new Path2D();\n"
            + "    p.addPath(p1, {a: 2, m11: 3});\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void addPathMatrixNotFinite() throws Exception {
        final String js =
            "    var p1 = new Path2D();\n"
            + "    p1.rect(0, 0, 10, 10);\n"
            + "    var p = new Path2D();\n"
            + "    p.addPath(p1, {a: NaN});\n"
            + "    log(ctx.isPointInPath(p, 5, 5));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void addPathNoPath() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.addPath({});\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void addPathNull() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.addPath(null);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void addPathSelf() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(0, 0, 10, 10);\n"
            + "    p.addPath(p, {e: 50, f: 50});\n"
            + "    log(ctx.isPointInPath(p, 5, 5));\n"
            + "    log(ctx.isPointInPath(p, 55, 55));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void moveToLineTo() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(0, 0);\n"
            + "    p.lineTo(100, 0);\n"
            + "    p.lineTo(0, 100);\n"
            + "    p.closePath();\n"
            + "    log(ctx.isPointInPath(p, 20, 20));\n"
            + "    log(ctx.isPointInPath(p, 80, 80));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void lineToNotFinite() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(0, 0);\n"
            + "    p.lineTo(100, 0);\n"
            + "    p.lineTo(NaN, 50);\n"
            + "    p.lineTo(100, Infinity);\n"
            + "    p.lineTo(0, 100);\n"
            + "    log(ctx.isPointInPath(p, 20, 20));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void bezierCurveTo() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(10, 50);\n"
            + "    p.bezierCurveTo(10, 0, 90, 0, 90, 50);\n"
            + "    log(ctx.isPointInPath(p, 50, 30));\n"
            + "    log(ctx.isPointInPath(p, 50, 60));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void quadraticCurveTo() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(10, 50);\n"
            + "    p.quadraticCurveTo(50, 0, 90, 50);\n"
            + "    log(ctx.isPointInPath(p, 50, 30));\n"
            + "    log(ctx.isPointInPath(p, 50, 20));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void arc() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.arc(50, 50, 40, 0, 2 * Math.PI);\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n"
            + "    log(ctx.isPointInPath(p, 95, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void arcAnticlockwise() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.arc(50, 50, 40, Math.PI, 0, true);\n"
            + "    log(ctx.isPointInPath(p, 50, 30));\n"
            + "    log(ctx.isPointInPath(p, 50, 70));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void arcClockwise() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.arc(50, 50, 40, Math.PI, 0, false);\n"
            + "    log(ctx.isPointInPath(p, 50, 30));\n"
            + "    log(ctx.isPointInPath(p, 50, 70));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("IndexSizeError/DOMException")
    public void arcNegativeRadius() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.arc(10, 10, -1, 0, Math.PI);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void arcNotFinite() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.arc(NaN, 10, -1, 0, Math.PI);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void arcTo() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(0, 0);\n"
            + "    p.arcTo(100, 0, 100, 100, 50);\n"
            + "    p.lineTo(100, 100);\n"
            + "    p.lineTo(0, 100);\n"
            + "    p.closePath();\n"
            + "    log(ctx.isPointInPath(p, 98, 2));\n"
            + "    log(ctx.isPointInPath(p, 60, 20));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void arcToCollinear() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(0, 10);\n"
            + "    p.arcTo(50, 10, 100, 10, 20);\n"
            + "    log(ctx.isPointInStroke(p, 25, 10));\n"
            + "    log(ctx.isPointInStroke(p, 75, 10));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void arcToRadiusZero() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(0, 0);\n"
            + "    p.arcTo(100, 0, 100, 100, 0);\n"
            + "    p.lineTo(100, 100);\n"
            + "    p.lineTo(0, 100);\n"
            + "    p.closePath();\n"
            + "    log(ctx.isPointInPath(p, 99, 1));\n"
            + "    log(ctx.isPointInPath(p, 101, 1));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void arcToEmptyPath() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.arcTo(10, 10, 100, 10, 5);\n"
            + "    p.lineTo(100, 100);\n"
            + "    log(ctx.isPointInStroke(p, 10, 10));\n"
            + "    log(ctx.isPointInStroke(p, 100, 10));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("IndexSizeError/DOMException")
    public void arcToNegativeRadius() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.arcTo(10, 10, 20, 20, -1);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void ellipse() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.ellipse(50, 50, 40, 10, 0, 0, 2 * Math.PI);\n"
            + "    log(ctx.isPointInPath(p, 50, 55));\n"
            + "    log(ctx.isPointInPath(p, 50, 65));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void ellipseRotated() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.ellipse(50, 50, 40, 10, Math.PI / 2, 0, 2 * Math.PI);\n"
            + "    log(ctx.isPointInPath(p, 50, 85));\n"
            + "    log(ctx.isPointInPath(p, 85, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("IndexSizeError/DOMException")
    public void ellipseNegativeRadiusX() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.ellipse(10, 10, -1, 1, 0, 0, Math.PI);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("IndexSizeError/DOMException")
    public void ellipseNegativeRadiusY() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.ellipse(10, 10, 1, -1, 0, 0, Math.PI);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void rect() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(10, 10, 50, 50);\n"
            + "    log(ctx.isPointInPath(p, 30, 30));\n"
            + "    log(ctx.isPointInPath(p, 70, 30));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void rectNegativeSize() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(60, 60, -50, -50);\n"
            + "    log(ctx.isPointInPath(p, 30, 30));\n"
            + "    log(ctx.isPointInPath(p, 70, 30));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void rectHoleNonZero() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(0, 0, 100, 100);\n"
            + "    p.rect(25, 25, 50, 50);\n"
            + "    log(ctx.isPointInPath(p, 50, 50, 'nonzero'));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void rectHoleEvenOdd() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(0, 0, 100, 100);\n"
            + "    p.rect(25, 25, 50, 50);\n"
            + "    log(ctx.isPointInPath(p, 50, 50, 'evenodd'));\n"
            + "    log(ctx.isPointInPath(p, 10, 10, 'evenodd'));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "true"})
    public void roundRect() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, 20);\n"
            + "    log(ctx.isPointInPath(p, 2, 2));\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n"
            + "    log(ctx.isPointInPath(p, 1, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void roundRectWithoutRadii() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100);\n"
            + "    log(ctx.isPointInPath(p, 1, 1));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void roundRectTwoRadii() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, [20, 0]);\n"
            + "    log(ctx.isPointInPath(p, 2, 2));\n"
            + "    log(ctx.isPointInPath(p, 98, 2));\n"
            + "    log(ctx.isPointInPath(p, 98, 98));\n"
            + "    log(ctx.isPointInPath(p, 2, 98));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false", "true", "false"})
    public void roundRectThreeRadii() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, [0, 20, 0]);\n"
            + "    log(ctx.isPointInPath(p, 2, 2));\n"
            + "    log(ctx.isPointInPath(p, 98, 2));\n"
            + "    log(ctx.isPointInPath(p, 98, 98));\n"
            + "    log(ctx.isPointInPath(p, 2, 98));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "true", "true"})
    public void roundRectFourRadii() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, [20, 0, 0, 0]);\n"
            + "    log(ctx.isPointInPath(p, 2, 2));\n"
            + "    log(ctx.isPointInPath(p, 98, 2));\n"
            + "    log(ctx.isPointInPath(p, 98, 98));\n"
            + "    log(ctx.isPointInPath(p, 2, 98));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void roundRectDOMPoint() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, {x: 40, y: 10});\n"
            + "    log(ctx.isPointInPath(p, 2, 5));\n"
            + "    log(ctx.isPointInPath(p, 50, 2));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void roundRectNegativeWidth() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(100, 0, -100, 100, [20, 0, 0, 0]);\n"
            + "    log(ctx.isPointInPath(p, 2, 2));\n"
            + "    log(ctx.isPointInPath(p, 98, 2));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false", "true"})
    public void roundRectRadiiTooLarge() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, 100);\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n"
            + "    log(ctx.isPointInPath(p, 10, 10));\n"
            + "    log(ctx.isPointInPath(p, 50, 1));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void roundRectNoRadius() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, []);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void roundRectFiveRadii() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, [1, 2, 3, 4, 5]);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void roundRectNegativeRadius() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, -5);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void roundRectNegativeDOMPoint() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, {x: 5, y: -5});\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void roundRectNotFiniteRadius() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.roundRect(0, 0, 100, 100, [5, NaN]);\n"
            + "    log(ctx.isPointInPath(p, 50, 50));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void closePath() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(0, 10);\n"
            + "    p.lineTo(100, 10);\n"
            + "    p.lineTo(100, 100);\n"
            + "    p.closePath();\n"
            + "    log(ctx.isPointInStroke(p, 50, 55));\n"
            + "    log(ctx.isPointInStroke(p, 10, 55));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false", "false"})
    public void isPointInStroke() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(10, 10);\n"
            + "    p.lineTo(100, 10);\n"
            + "    log(ctx.isPointInStroke(p, 50, 10));\n"
            + "    log(ctx.isPointInStroke(p, 50, 12));\n"
            + "    log(ctx.isPointInStroke(p, 105, 10));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void isPointInStrokeLineWidth() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(10, 10);\n"
            + "    p.lineTo(100, 10);\n"
            + "    ctx.lineWidth = 10;\n"
            + "    log(ctx.isPointInStroke(p, 50, 14));\n"
            + "    log(ctx.isPointInStroke(p, 50, 16));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void isPointInStrokeLineCap() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.moveTo(10, 10);\n"
            + "    p.lineTo(100, 10);\n"
            + "    ctx.lineWidth = 10;\n"
            + "    log(ctx.isPointInStroke(p, 103, 10));\n"
            + "    ctx.lineCap = 'square';\n"
            + "    log(ctx.isPointInStroke(p, 103, 10));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void isPointInPathTransform() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(0, 0, 10, 10);\n"
            + "    ctx.translate(50, 50);\n"
            + "    log(ctx.isPointInPath(p, 5, 5));\n"
            + "    log(ctx.isPointInPath(p, 55, 55));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void isPointInPathCurrentPathUnchanged() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(100, 100, 10, 10);\n"
            + "    ctx.beginPath();\n"
            + "    ctx.rect(0, 0, 10, 10);\n"
            + "    ctx.fill(p);\n"
            + "    ctx.stroke(p);\n"
            + "    log(ctx.isPointInPath(5, 5));\n"
            + "    log(ctx.isPointInPath(105, 105));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void isPointInPathInvalidRule() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    ctx.isPointInPath(p, 5, 5, 'foo');\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void isPointInPathNotFinite() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(0, 0, 10, 10);\n"
            + "    log(ctx.isPointInPath(p, NaN, 5));\n"
            + "    log(ctx.isPointInPath(p, 5, Infinity));\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void fill() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(0, 0, 10, 10);\n"
            + "    ctx.fill(p);\n"
            + "    ctx.fill(p, 'evenodd');\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void fillInvalidRule() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    ctx.fill(p, 'foo');\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void fillNoPath() throws Exception {
        final String js =
            "    ctx.fill({}, 'evenodd');\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void stroke() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(0, 0, 10, 10);\n"
            + "    ctx.stroke(p);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void strokeNull() throws Exception {
        final String js =
            "    ctx.stroke(null);\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void clip() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    p.rect(0, 0, 10, 10);\n"
            + "    ctx.clip(p);\n"
            + "    ctx.clip(p, 'evenodd');\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void clipInvalidRule() throws Exception {
        final String js =
            "    var p = new Path2D();\n"
            + "    ctx.clip(p, 'foo');\n"
            + "    log('done');\n";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void clipNoPath() throws Exception {
        final String js =
            "    ctx.clip({}, 'nonzero');\n"
            + "    log('done');\n";
        test(js);
    }

    private void test(final String js) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<canvas id='myCanvas' width='200' height='200'></canvas>\n"
            + LOG_TEXTAREA
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  var ctx = document.getElementById('myCanvas').getContext('2d');\n"
            + "  try {\n"
            + js
            + "  } catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }
}
