/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code WebGLRenderingContext}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class WebGLRenderingContext extends SimpleScriptable {

    /** The constant {@code DEPTH_BUFFER_BIT}. */
    @JsxConstant
    public static final int DEPTH_BUFFER_BIT = 0x0100;
    /** The constant {@code STENCIL_BUFFER_BIT}. */
    @JsxConstant
    public static final int STENCIL_BUFFER_BIT = 0x0400;
    /** The constant {@code COLOR_BUFFER_BIT}. */
    @JsxConstant
    public static final int COLOR_BUFFER_BIT = 0x4000;

    /** The constant {@code POINTS}. */
    @JsxConstant
    public static final int POINTS = 0x0;
    /** The constant {@code LINES}. */
    @JsxConstant
    public static final int LINES = 0x0001;
    /** The constant {@code LINE_LOOP}. */
    @JsxConstant
    public static final int LINE_LOOP = 0x0002;
    /** The constant {@code LINE_STRIP}. */
    @JsxConstant
    public static final int LINE_STRIP = 0x0003;
    /** The constant {@code TRIANGLES}. */
    @JsxConstant
    public static final int TRIANGLES = 0x0004;
    /** The constant {@code TRIANGLE_STRIP}. */
    @JsxConstant
    public static final int TRIANGLE_STRIP = 0x0005;
    /** The constant {@code TRIANGLE_FAN}. */
    @JsxConstant
    public static final int TRIANGLE_FAN = 0x0006;

    /** The constant {@code ZERO}. */
    @JsxConstant
    public static final int ZERO = 0x0;
    /** The constant {@code ONE}. */
    @JsxConstant
    public static final int ONE = 0x0001;
    /** The constant {@code SRC_COLOR}. */
    @JsxConstant
    public static final int SRC_COLOR = 0x0300;
    /** The constant {@code ONE_MINUS_SRC_COLOR}. */
    @JsxConstant
    public static final int ONE_MINUS_SRC_COLOR = 0x0301;
    /** The constant {@code SRC_ALPHA}. */
    @JsxConstant
    public static final int SRC_ALPHA = 0x0302;
    /** The constant {@code ONE_MINUS_SRC_ALPHA}. */
    @JsxConstant
    public static final int ONE_MINUS_SRC_ALPHA = 0x0303;
    /** The constant {@code DST_ALPHA}. */
    @JsxConstant
    public static final int DST_ALPHA = 0x0304;
    /** The constant {@code ONE_MINUS_DST_ALPHA}. */
    @JsxConstant
    public static final int ONE_MINUS_DST_ALPHA = 0x0305;
    /** The constant {@code DST_COLOR}. */
    @JsxConstant
    public static final int DST_COLOR = 0x0306;
    /** The constant {@code ONE_MINUS_DST_COLOR}. */
    @JsxConstant
    public static final int ONE_MINUS_DST_COLOR = 0x0307;
    /** The constant {@code SRC_ALPHA_SATURATE}. */
    @JsxConstant
    public static final int SRC_ALPHA_SATURATE = 0x0308;
    /** The constant {@code CONSTANT_COLOR}. */
    @JsxConstant
    public static final int CONSTANT_COLOR = 0x8001;
    /** The constant {@code ONE_MINUS_CONSTANT_COLOR}. */
    @JsxConstant
    public static final int ONE_MINUS_CONSTANT_COLOR = 0x8002;
    /** The constant {@code CONSTANT_ALPHA}. */
    @JsxConstant
    public static final int CONSTANT_ALPHA = 0x8003;
    /** The constant {@code ONE_MINUS_CONSTANT_ALPHA}. */
    @JsxConstant
    public static final int ONE_MINUS_CONSTANT_ALPHA = 0x8004;

    /** The constant {@code FUNC_ADD}. */
    @JsxConstant
    public static final int FUNC_ADD = 0x8006;
    /** The constant {@code FUNC_REVERSE_SUBTRACT}. */
    @JsxConstant
    public static final int FUNC_REVERSE_SUBTRACT = 0x800B;

    /** The constant {@code BLEND_EQUATION}. */
    @JsxConstant
    public static final int BLEND_EQUATION = 0x8009;
    /** The constant {@code BLEND_EQUATION_RGB}. */
    @JsxConstant
    public static final int BLEND_EQUATION_RGB = 0x8009;
    /** The constant {@code BLEND_EQUATION_ALPHA}. */
    @JsxConstant
    public static final int BLEND_EQUATION_ALPHA = 0x883D;
    /** The constant {@code BLEND_DST_RGB}. */
    @JsxConstant
    public static final int BLEND_DST_RGB = 0x80C8;

    /** The constant {@code NO_ERROR}. */
    @JsxConstant
    public static final int NO_ERROR = 0x0;
    /** The constant {@code NONE}. */
    @JsxConstant
    public static final int NONE = 0x0;
    /** The constant {@code NEVER}. */
    @JsxConstant
    public static final int NEVER = 0x0200;
    /** The constant {@code LESS}. */
    @JsxConstant
    public static final int LESS = 0x0201;
    /** The constant {@code EQUAL}. */
    @JsxConstant
    public static final int EQUAL = 0x0202;
    /** The constant {@code LEQUAL}. */
    @JsxConstant
    public static final int LEQUAL = 0x0203;
    /** The constant {@code GREATER}. */
    @JsxConstant
    public static final int GREATER = 0x0204;
    /** The constant {@code NOTEQUAL}. */
    @JsxConstant
    public static final int NOTEQUAL = 0x0205;
    /** The constant {@code GEQUAL}. */
    @JsxConstant
    public static final int GEQUAL = 0x0206;
    /** The constant {@code ALWAYS}. */
    @JsxConstant
    public static final int ALWAYS = 0x0207;
    /** The constant {@code FRONT}. */
    @JsxConstant
    public static final int FRONT = 0x0404;
    /** The constant {@code BACK}. */
    @JsxConstant
    public static final int BACK = 0x0405;
    /** The constant {@code FRONT_AND_BACK}. */
    @JsxConstant
    public static final int FRONT_AND_BACK = 0x0408;
    /** The constant {@code INVALID_ENUM}. */
    @JsxConstant
    public static final int INVALID_ENUM = 0x0500;
    /** The constant {@code INVALID_VALUE}. */
    @JsxConstant
    public static final int INVALID_VALUE = 0x0501;
    /** The constant {@code INVALID_OPERATION}. */
    @JsxConstant
    public static final int INVALID_OPERATION = 0x0502;
    /** The constant {@code OUT_OF_MEMORY}. */
    @JsxConstant
    public static final int OUT_OF_MEMORY = 0x0505;
    /** The constant {@code INVALID_FRAMEBUFFER_OPERATION}. */
    @JsxConstant
    public static final int INVALID_FRAMEBUFFER_OPERATION = 0x0506;
    /** The constant {@code CW}. */
    @JsxConstant
    public static final int CW = 0x0900;
    /** The constant {@code CCW}. */
    @JsxConstant
    public static final int CCW = 0x0901;
    /** The constant {@code LINE_WIDTH}. */
    @JsxConstant
    public static final int LINE_WIDTH = 0x0B21;
    /** The constant {@code CULL_FACE}. */
    @JsxConstant
    public static final int CULL_FACE = 0x0B44;
    /** The constant {@code CULL_FACE_MODE}. */
    @JsxConstant
    public static final int CULL_FACE_MODE = 0x0B45;
    /** The constant {@code FRONT_FACE}. */
    @JsxConstant
    public static final int FRONT_FACE = 0x0B46;
    /** The constant {@code DEPTH_RANGE}. */
    @JsxConstant
    public static final int DEPTH_RANGE = 0x0B70;
    /** The constant {@code DEPTH_TEST}. */
    @JsxConstant
    public static final int DEPTH_TEST = 0x0B71;
    /** The constant {@code DEPTH_WRITEMASK}. */
    @JsxConstant
    public static final int DEPTH_WRITEMASK = 0x0B72;
    /** The constant {@code DEPTH_CLEAR_VALUE}. */
    @JsxConstant
    public static final int DEPTH_CLEAR_VALUE = 0x0B73;
    /** The constant {@code DEPTH_FUNC}. */
    @JsxConstant
    public static final int DEPTH_FUNC = 0x0B74;
    /** The constant {@code STENCIL_TEST}. */
    @JsxConstant
    public static final int STENCIL_TEST = 0x0B90;
    /** The constant {@code STENCIL_CLEAR_VALUE}. */
    @JsxConstant
    public static final int STENCIL_CLEAR_VALUE = 0x0B91;
    /** The constant {@code STENCIL_FUNC}. */
    @JsxConstant
    public static final int STENCIL_FUNC = 0x0B92;
    /** The constant {@code STENCIL_VALUE_MASK}. */
    @JsxConstant
    public static final int STENCIL_VALUE_MASK = 0x0B93;
    /** The constant {@code STENCIL_FAIL}. */
    @JsxConstant
    public static final int STENCIL_FAIL = 0x0B94;
    /** The constant {@code STENCIL_PASS_DEPTH_FAIL}. */
    @JsxConstant
    public static final int STENCIL_PASS_DEPTH_FAIL = 0x0B95;
    /** The constant {@code STENCIL_PASS_DEPTH_PASS}. */
    @JsxConstant
    public static final int STENCIL_PASS_DEPTH_PASS = 0x0B96;
    /** The constant {@code STENCIL_REF}. */
    @JsxConstant
    public static final int STENCIL_REF = 0x0B97;
    /** The constant {@code STENCIL_WRITEMASK}. */
    @JsxConstant
    public static final int STENCIL_WRITEMASK = 0x0B98;
    /** The constant {@code VIEWPORT}. */
    @JsxConstant
    public static final int VIEWPORT = 0x0BA2;
    /** The constant {@code DITHER}. */
    @JsxConstant
    public static final int DITHER = 0x0BD0;
    /** The constant {@code BLEND}. */
    @JsxConstant
    public static final int BLEND = 0x0BE2;
    /** The constant {@code SCISSOR_BOX}. */
    @JsxConstant
    public static final int SCISSOR_BOX = 0x0C10;
    /** The constant {@code SCISSOR_TEST}. */
    @JsxConstant
    public static final int SCISSOR_TEST = 0x0C11;
    /** The constant {@code COLOR_CLEAR_VALUE}. */
    @JsxConstant
    public static final int COLOR_CLEAR_VALUE = 0x0C22;
    /** The constant {@code COLOR_WRITEMASK}. */
    @JsxConstant
    public static final int COLOR_WRITEMASK = 0x0C23;
    /** The constant {@code UNPACK_ALIGNMENT}. */
    @JsxConstant
    public static final int UNPACK_ALIGNMENT = 0x0Cf5;
    /** The constant {@code PACK_ALIGNMENT}. */
    @JsxConstant
    public static final int PACK_ALIGNMENT = 0x0D05;
    /** The constant {@code MAX_TEXTURE_SIZE}. */
    @JsxConstant
    public static final int MAX_TEXTURE_SIZE = 0x0D33;
    /** The constant {@code MAX_VIEWPORT_DIMS}. */
    @JsxConstant
    public static final int MAX_VIEWPORT_DIMS = 0x0D3A;
    /** The constant {@code SUBPIXEL_BITS}. */
    @JsxConstant
    public static final int SUBPIXEL_BITS = 0x0D50;
    /** The constant {@code RED_BITS}. */
    @JsxConstant
    public static final int RED_BITS = 0x0D52;
    /** The constant {@code GREEN_BITS}. */
    @JsxConstant
    public static final int GREEN_BITS = 0x0D53;
    /** The constant {@code BLUE_BITS}. */
    @JsxConstant
    public static final int BLUE_BITS = 0x0D54;
    /** The constant {@code ALPHA_BITS}. */
    @JsxConstant
    public static final int ALPHA_BITS = 0x0D55;
    /** The constant {@code DEPTH_BITS}. */
    @JsxConstant
    public static final int DEPTH_BITS = 0x0D56;
    /** The constant {@code STENCIL_BITS}. */
    @JsxConstant
    public static final int STENCIL_BITS = 0x0D57;
    /** The constant {@code TEXTURE_2D}. */
    @JsxConstant
    public static final int TEXTURE_2D = 0x0DE1;
    /** The constant {@code DONT_CARE}. */
    @JsxConstant
    public static final int DONT_CARE = 0x1100;
    /** The constant {@code FASTEST}. */
    @JsxConstant
    public static final int FASTEST = 0x1101;
    /** The constant {@code NICEST}. */
    @JsxConstant
    public static final int NICEST = 0x1102;
    /** The constant {@code BYTE}. */
    @JsxConstant
    public static final int BYTE = 0x1400;
    /** The constant {@code UNSIGNED_BYTE}. */
    @JsxConstant
    public static final int UNSIGNED_BYTE = 0x1401;
    /** The constant {@code SHORT}. */
    @JsxConstant
    public static final int SHORT = 0x1402;
    /** The constant {@code UNSIGNED_SHORT}. */
    @JsxConstant
    public static final int UNSIGNED_SHORT = 0x1403;
    /** The constant {@code INT}. */
    @JsxConstant
    public static final int INT = 0x1404;
    /** The constant {@code UNSIGNED_INT}. */
    @JsxConstant
    public static final int UNSIGNED_INT = 0x1405;
    /** The constant {@code FLOAT}. */
    @JsxConstant
    public static final int FLOAT = 0x1406;
    /** The constant {@code INVERT}. */
    @JsxConstant
    public static final int INVERT = 0x150A;
    /** The constant {@code TEXTURE}. */
    @JsxConstant
    public static final int TEXTURE = 0x1702;
    /** The constant {@code STENCIL_INDEX}. */
    @JsxConstant(IE)
    public static final int STENCIL_INDEX = 0x1901;
    /** The constant {@code DEPTH_COMPONENT}. */
    @JsxConstant
    public static final int DEPTH_COMPONENT = 0x1902;
    /** The constant {@code ALPHA}. */
    @JsxConstant
    public static final int ALPHA = 0x1906;
    /** The constant {@code RGB}. */
    @JsxConstant
    public static final int RGB = 0x1907;
    /** The constant {@code RGBA}. */
    @JsxConstant
    public static final int RGBA = 0x1908;
    /** The constant {@code LUMINANCE}. */
    @JsxConstant
    public static final int LUMINANCE = 0x1909;
    /** The constant {@code LUMINANCE_ALPHA}. */
    @JsxConstant
    public static final int LUMINANCE_ALPHA = 0x190A;
    /** The constant {@code KEEP}. */
    @JsxConstant
    public static final int KEEP = 0x1E00;
    /** The constant {@code REPLACE}. */
    @JsxConstant
    public static final int REPLACE = 0x1E01;
    /** The constant {@code INCR}. */
    @JsxConstant
    public static final int INCR = 0x1E02;
    /** The constant {@code DECR}. */
    @JsxConstant
    public static final int DECR = 0x1E03;
    /** The constant {@code VENDOR}. */
    @JsxConstant
    public static final int VENDOR = 0x1F00;
    /** The constant {@code RENDERER}. */
    @JsxConstant
    public static final int RENDERER = 0x1F01;
    /** The constant {@code VERSION}. */
    @JsxConstant
    public static final int VERSION = 0x1F02;
    /** The constant {@code NEAREST}. */
    @JsxConstant
    public static final int NEAREST = 0x2600;
    /** The constant {@code LINEAR}. */
    @JsxConstant
    public static final int LINEAR = 0x2601;
    /** The constant {@code NEAREST_MIPMAP_NEAREST}. */
    @JsxConstant
    public static final int NEAREST_MIPMAP_NEAREST = 0x2700;
    /** The constant {@code LINEAR_MIPMAP_NEAREST}. */
    @JsxConstant
    public static final int LINEAR_MIPMAP_NEAREST = 0x2701;
    /** The constant {@code NEAREST_MIPMAP_LINEAR}. */
    @JsxConstant
    public static final int NEAREST_MIPMAP_LINEAR = 0x2702;
    /** The constant {@code LINEAR_MIPMAP_LINEAR}. */
    @JsxConstant
    public static final int LINEAR_MIPMAP_LINEAR = 0x2703;
    /** The constant {@code TEXTURE_MAG_FILTER}. */
    @JsxConstant
    public static final int TEXTURE_MAG_FILTER = 0x2800;
    /** The constant {@code TEXTURE_MIN_FILTER}. */
    @JsxConstant
    public static final int TEXTURE_MIN_FILTER = 0x2801;
    /** The constant {@code TEXTURE_WRAP_S}. */
    @JsxConstant
    public static final int TEXTURE_WRAP_S = 0x2802;
    /** The constant {@code TEXTURE_WRAP_T}. */
    @JsxConstant
    public static final int TEXTURE_WRAP_T = 0x2803;
    /** The constant {@code REPEAT}. */
    @JsxConstant
    public static final int REPEAT = 0x2901;
    /** The constant {@code POLYGON_OFFSET_UNITS}. */
    @JsxConstant
    public static final int POLYGON_OFFSET_UNITS = 0x2A00;
    /** The constant {@code BLEND_COLOR}. */
    @JsxConstant
    public static final int BLEND_COLOR = 0x8005;
    /** The constant {@code FUNC_SUBTRACT}. */
    @JsxConstant
    public static final int FUNC_SUBTRACT = 0x800A;
    /** The constant {@code UNSIGNED_SHORT_4_4_4_4}. */
    @JsxConstant
    public static final int UNSIGNED_SHORT_4_4_4_4 = 0x8033;
    /** The constant {@code UNSIGNED_SHORT_5_5_5_1}. */
    @JsxConstant
    public static final int UNSIGNED_SHORT_5_5_5_1 = 0x8034;
    /** The constant {@code POLYGON_OFFSET_FILL}. */
    @JsxConstant
    public static final int POLYGON_OFFSET_FILL = 0x8037;
    /** The constant {@code POLYGON_OFFSET_FACTOR}. */
    @JsxConstant
    public static final int POLYGON_OFFSET_FACTOR = 0x8038;
    /** The constant {@code RGBA4}. */
    @JsxConstant
    public static final int RGBA4 = 0x8056;
    /** The constant {@code RGB5_A1}. */
    @JsxConstant
    public static final int RGB5_A1 = 0x8057;
    /** The constant {@code TEXTURE_BINDING_2D}. */
    @JsxConstant
    public static final int TEXTURE_BINDING_2D = 0x8069;
    /** The constant {@code SAMPLE_ALPHA_TO_COVERAGE}. */
    @JsxConstant
    public static final int SAMPLE_ALPHA_TO_COVERAGE = 0x809E;
    /** The constant {@code SAMPLE_COVERAGE}. */
    @JsxConstant
    public static final int SAMPLE_COVERAGE = 0x80A0;
    /** The constant {@code SAMPLE_BUFFERS}. */
    @JsxConstant
    public static final int SAMPLE_BUFFERS = 0x80A8;
    /** The constant {@code SAMPLES}. */
    @JsxConstant
    public static final int SAMPLES = 0x80A9;
    /** The constant {@code SAMPLE_COVERAGE_VALUE}. */
    @JsxConstant
    public static final int SAMPLE_COVERAGE_VALUE = 0x80AA;
    /** The constant {@code SAMPLE_COVERAGE_INVERT}. */
    @JsxConstant
    public static final int SAMPLE_COVERAGE_INVERT = 0x80AB;
    /** The constant {@code BLEND_SRC_RGB}. */
    @JsxConstant
    public static final int BLEND_SRC_RGB = 0x80C9;
    /** The constant {@code BLEND_DST_ALPHA}. */
    @JsxConstant
    public static final int BLEND_DST_ALPHA = 0x80CA;
    /** The constant {@code BLEND_SRC_ALPHA}. */
    @JsxConstant
    public static final int BLEND_SRC_ALPHA = 0x80CB;
    /** The constant {@code CLAMP_TO_EDGE}. */
    @JsxConstant
    public static final int CLAMP_TO_EDGE = 0x812F;
    /** The constant {@code GENERATE_MIPMAP_HINT}. */
    @JsxConstant
    public static final int GENERATE_MIPMAP_HINT = 0x8192;
    /** The constant {@code DEPTH_COMPONENT16}. */
    @JsxConstant
    public static final int DEPTH_COMPONENT16 = 0x81A5;
    /** The constant {@code DEPTH_STENCIL_ATTACHMENT}. */
    @JsxConstant
    public static final int DEPTH_STENCIL_ATTACHMENT = 0x821A;
    /** The constant {@code UNSIGNED_SHORT_5_6_5}. */
    @JsxConstant
    public static final int UNSIGNED_SHORT_5_6_5 = 0x8363;
    /** The constant {@code MIRRORED_REPEAT}. */
    @JsxConstant
    public static final int MIRRORED_REPEAT = 0x8370;
    /** The constant {@code ALIASED_POINT_SIZE_RANGE}. */
    @JsxConstant
    public static final int ALIASED_POINT_SIZE_RANGE = 0x846D;
    /** The constant {@code ALIASED_LINE_WIDTH_RANGE}. */
    @JsxConstant
    public static final int ALIASED_LINE_WIDTH_RANGE = 0x846E;
    /** The constant {@code TEXTURE0}. */
    @JsxConstant
    public static final int TEXTURE0 = 0x84C0;
    /** The constant {@code TEXTURE1}. */
    @JsxConstant
    public static final int TEXTURE1 = 0x84C1;
    /** The constant {@code TEXTURE2}. */
    @JsxConstant
    public static final int TEXTURE2 = 0x84C2;
    /** The constant {@code TEXTURE3}. */
    @JsxConstant
    public static final int TEXTURE3 = 0x84C3;
    /** The constant {@code TEXTURE4}. */
    @JsxConstant
    public static final int TEXTURE4 = 0x84C4;
    /** The constant {@code TEXTURE5}. */
    @JsxConstant
    public static final int TEXTURE5 = 0x84C5;
    /** The constant {@code TEXTURE6}. */
    @JsxConstant
    public static final int TEXTURE6 = 0x84C6;
    /** The constant {@code TEXTURE7}. */
    @JsxConstant
    public static final int TEXTURE7 = 0x84C7;
    /** The constant {@code TEXTURE8}. */
    @JsxConstant
    public static final int TEXTURE8 = 0x84C8;
    /** The constant {@code TEXTURE9}. */
    @JsxConstant
    public static final int TEXTURE9 = 0x84C9;
    /** The constant {@code TEXTURE10}. */
    @JsxConstant
    public static final int TEXTURE10 = 0x84CA;
    /** The constant {@code TEXTURE11}. */
    @JsxConstant
    public static final int TEXTURE11 = 0x84CB;
    /** The constant {@code TEXTURE12}. */
    @JsxConstant
    public static final int TEXTURE12 = 0x84CC;
    /** The constant {@code TEXTURE13}. */
    @JsxConstant
    public static final int TEXTURE13 = 0x84CD;
    /** The constant {@code TEXTURE14}. */
    @JsxConstant
    public static final int TEXTURE14 = 0x84CE;
    /** The constant {@code TEXTURE15}. */
    @JsxConstant
    public static final int TEXTURE15 = 0x84CF;
    /** The constant {@code TEXTURE16}. */
    @JsxConstant
    public static final int TEXTURE16 = 0x84D0;
    /** The constant {@code TEXTURE17}. */
    @JsxConstant
    public static final int TEXTURE17 = 0x84D1;
    /** The constant {@code TEXTURE18}. */
    @JsxConstant
    public static final int TEXTURE18 = 0x84D2;
    /** The constant {@code TEXTURE19}. */
    @JsxConstant
    public static final int TEXTURE19 = 0x84D3;
    /** The constant {@code TEXTURE20}. */
    @JsxConstant
    public static final int TEXTURE20 = 0x84D4;
    /** The constant {@code TEXTURE21}. */
    @JsxConstant
    public static final int TEXTURE21 = 0x84D5;
    /** The constant {@code TEXTURE22}. */
    @JsxConstant
    public static final int TEXTURE22 = 0x84D6;
    /** The constant {@code TEXTURE23}. */
    @JsxConstant
    public static final int TEXTURE23 = 0x84D7;
    /** The constant {@code TEXTURE24}. */
    @JsxConstant
    public static final int TEXTURE24 = 0x84D8;
    /** The constant {@code TEXTURE25}. */
    @JsxConstant
    public static final int TEXTURE25 = 0x84D9;
    /** The constant {@code TEXTURE26}. */
    @JsxConstant
    public static final int TEXTURE26 = 0x84DA;
    /** The constant {@code TEXTURE27}. */
    @JsxConstant
    public static final int TEXTURE27 = 0x84DB;
    /** The constant {@code TEXTURE28}. */
    @JsxConstant
    public static final int TEXTURE28 = 0x84DC;
    /** The constant {@code TEXTURE29}. */
    @JsxConstant
    public static final int TEXTURE29 = 0x84DD;
    /** The constant {@code TEXTURE30}. */
    @JsxConstant
    public static final int TEXTURE30 = 0x84DE;
    /** The constant {@code TEXTURE31}. */
    @JsxConstant
    public static final int TEXTURE31 = 0x84DF;
    /** The constant {@code ACTIVE_TEXTURE}. */
    @JsxConstant
    public static final int ACTIVE_TEXTURE = 0x84E0;
    /** The constant {@code MAX_RENDERBUFFER_SIZE}. */
    @JsxConstant
    public static final int MAX_RENDERBUFFER_SIZE = 0x84E8;
    /** The constant {@code DEPTH_STENCIL}. */
    @JsxConstant
    public static final int DEPTH_STENCIL = 0x84F9;
    /** The constant {@code INCR_WRAP}. */
    @JsxConstant
    public static final int INCR_WRAP = 0x8507;
    /** The constant {@code DECR_WRAP}. */
    @JsxConstant
    public static final int DECR_WRAP = 0x8508;
    /** The constant {@code TEXTURE_CUBE_MAP}. */
    @JsxConstant
    public static final int TEXTURE_CUBE_MAP = 0x8513;
    /** The constant {@code TEXTURE_BINDING_CUBE_MAP}. */
    @JsxConstant
    public static final int TEXTURE_BINDING_CUBE_MAP = 0x8514;
    /** The constant {@code TEXTURE_CUBE_MAP_POSITIVE_X}. */
    @JsxConstant
    public static final int TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515;
    /** The constant {@code TEXTURE_CUBE_MAP_NEGATIVE_X}. */
    @JsxConstant
    public static final int TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516;
    /** The constant {@code TEXTURE_CUBE_MAP_POSITIVE_Y}. */
    @JsxConstant
    public static final int TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517;
    /** The constant {@code TEXTURE_CUBE_MAP_NEGATIVE_Y}. */
    @JsxConstant
    public static final int TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518;
    /** The constant {@code TEXTURE_CUBE_MAP_POSITIVE_Z}. */
    @JsxConstant
    public static final int TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519;
    /** The constant {@code TEXTURE_CUBE_MAP_NEGATIVE_Z}. */
    @JsxConstant
    public static final int TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851A;
    /** The constant {@code MAX_CUBE_MAP_TEXTURE_SIZE}. */
    @JsxConstant
    public static final int MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C;
    /** The constant {@code VERTEX_ATTRIB_ARRAY_ENABLED}. */
    @JsxConstant
    public static final int VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622;
    /** The constant {@code VERTEX_ATTRIB_ARRAY_SIZE}. */
    @JsxConstant
    public static final int VERTEX_ATTRIB_ARRAY_SIZE = 0x8623;
    /** The constant {@code VERTEX_ATTRIB_ARRAY_STRIDE}. */
    @JsxConstant
    public static final int VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624;
    /** The constant {@code VERTEX_ATTRIB_ARRAY_TYPE}. */
    @JsxConstant
    public static final int VERTEX_ATTRIB_ARRAY_TYPE = 0x8625;
    /** The constant {@code CURRENT_VERTEX_ATTRIB}. */
    @JsxConstant
    public static final int CURRENT_VERTEX_ATTRIB = 0x8626;
    /** The constant {@code VERTEX_ATTRIB_ARRAY_POINTER}. */
    @JsxConstant
    public static final int VERTEX_ATTRIB_ARRAY_POINTER = 0x8645;
    /** The constant {@code COMPRESSED_TEXTURE_FORMATS}. */
    @JsxConstant
    public static final int COMPRESSED_TEXTURE_FORMATS = 0x86A3;
    /** The constant {@code BUFFER_SIZE}. */
    @JsxConstant
    public static final int BUFFER_SIZE = 0x8764;
    /** The constant {@code BUFFER_USAGE}. */
    @JsxConstant
    public static final int BUFFER_USAGE = 0x8765;
    /** The constant {@code STENCIL_BACK_FUNC}. */
    @JsxConstant
    public static final int STENCIL_BACK_FUNC = 0x8800;
    /** The constant {@code STENCIL_BACK_FAIL}. */
    @JsxConstant
    public static final int STENCIL_BACK_FAIL = 0x8801;
    /** The constant {@code STENCIL_BACK_PASS_DEPTH_FAIL}. */
    @JsxConstant
    public static final int STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802;
    /** The constant {@code STENCIL_BACK_PASS_DEPTH_PASS}. */
    @JsxConstant
    public static final int STENCIL_BACK_PASS_DEPTH_PASS = 0x8803;
    /** The constant {@code MAX_VERTEX_ATTRIBS}. */
    @JsxConstant
    public static final int MAX_VERTEX_ATTRIBS = 0x8869;
    /** The constant {@code VERTEX_ATTRIB_ARRAY_NORMALIZED}. */
    @JsxConstant
    public static final int VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886A;
    /** The constant {@code MAX_TEXTURE_IMAGE_UNITS}. */
    @JsxConstant
    public static final int MAX_TEXTURE_IMAGE_UNITS = 0x8872;
    /** The constant {@code ARRAY_BUFFER}. */
    @JsxConstant
    public static final int ARRAY_BUFFER = 0x8892;
    /** The constant {@code ELEMENT_ARRAY_BUFFER}. */
    @JsxConstant
    public static final int ELEMENT_ARRAY_BUFFER = 0x8893;
    /** The constant {@code ARRAY_BUFFER_BINDING}. */
    @JsxConstant
    public static final int ARRAY_BUFFER_BINDING = 0x8894;
    /** The constant {@code ELEMENT_ARRAY_BUFFER_BINDING}. */
    @JsxConstant
    public static final int ELEMENT_ARRAY_BUFFER_BINDING = 0x8895;
    /** The constant {@code VERTEX_ATTRIB_ARRAY_BUFFER_BINDING}. */
    @JsxConstant
    public static final int VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889F;
    /** The constant {@code STREAM_DRAW}. */
    @JsxConstant
    public static final int STREAM_DRAW = 0x88E0;
    /** The constant {@code STATIC_DRAW}. */
    @JsxConstant
    public static final int STATIC_DRAW = 0x88E4;
    /** The constant {@code DYNAMIC_DRAW}. */
    @JsxConstant
    public static final int DYNAMIC_DRAW = 0x88E8;
    /** The constant {@code FRAGMENT_SHADER}. */
    @JsxConstant
    public static final int FRAGMENT_SHADER = 0x8B30;
    /** The constant {@code VERTEX_SHADER}. */
    @JsxConstant
    public static final int VERTEX_SHADER = 0x8B31;
    /** The constant {@code MAX_VERTEX_TEXTURE_IMAGE_UNITS}. */
    @JsxConstant
    public static final int MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C;
    /** The constant {@code MAX_COMBINED_TEXTURE_IMAGE_UNITS}. */
    @JsxConstant
    public static final int MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D;
    /** The constant {@code SHADER_TYPE}. */
    @JsxConstant
    public static final int SHADER_TYPE = 0x8B4F;
    /** The constant {@code FLOAT_VEC2}. */
    @JsxConstant
    public static final int FLOAT_VEC2 = 0x8B50;
    /** The constant {@code FLOAT_VEC3}. */
    @JsxConstant
    public static final int FLOAT_VEC3 = 0x8B51;
    /** The constant {@code FLOAT_VEC4}. */
    @JsxConstant
    public static final int FLOAT_VEC4 = 0x8B52;
    /** The constant {@code INT_VEC2}. */
    @JsxConstant
    public static final int INT_VEC2 = 0x8B53;
    /** The constant {@code INT_VEC3}. */
    @JsxConstant
    public static final int INT_VEC3 = 0x8B54;
    /** The constant {@code INT_VEC4}. */
    @JsxConstant
    public static final int INT_VEC4 = 0x8B55;
    /** The constant {@code BOOL}. */
    @JsxConstant
    public static final int BOOL = 0x8B56;
    /** The constant {@code BOOL_VEC2}. */
    @JsxConstant
    public static final int BOOL_VEC2 = 0x8B57;
    /** The constant {@code BOOL_VEC3}. */
    @JsxConstant
    public static final int BOOL_VEC3 = 0x8B58;
    /** The constant {@code BOOL_VEC4}. */
    @JsxConstant
    public static final int BOOL_VEC4 = 0x8B59;
    /** The constant {@code FLOAT_MAT2}. */
    @JsxConstant
    public static final int FLOAT_MAT2 = 0x8B5A;
    /** The constant {@code FLOAT_MAT3}. */
    @JsxConstant
    public static final int FLOAT_MAT3 = 0x8B5B;
    /** The constant {@code FLOAT_MAT4}. */
    @JsxConstant
    public static final int FLOAT_MAT4 = 0x8B5C;
    /** The constant {@code SAMPLER_2D}. */
    @JsxConstant
    public static final int SAMPLER_2D = 0x8B5E;
    /** The constant {@code SAMPLER_CUBE}. */
    @JsxConstant
    public static final int SAMPLER_CUBE = 0x8B60;
    /** The constant {@code DELETE_STATUS}. */
    @JsxConstant
    public static final int DELETE_STATUS = 0x8B80;
    /** The constant {@code COMPILE_STATUS}. */
    @JsxConstant
    public static final int COMPILE_STATUS = 0x8B81;
    /** The constant {@code LINK_STATUS}. */
    @JsxConstant
    public static final int LINK_STATUS = 0x8B82;
    /** The constant {@code VALIDATE_STATUS}. */
    @JsxConstant
    public static final int VALIDATE_STATUS = 0x8B83;
    /** The constant {@code ATTACHED_SHADERS}. */
    @JsxConstant
    public static final int ATTACHED_SHADERS = 0x8B85;
    /** The constant {@code ACTIVE_UNIFORMS}. */
    @JsxConstant
    public static final int ACTIVE_UNIFORMS = 0x8B86;
    /** The constant {@code ACTIVE_ATTRIBUTES}. */
    @JsxConstant
    public static final int ACTIVE_ATTRIBUTES = 0x8B89;
    /** The constant {@code SHADING_LANGUAGE_VERSION}. */
    @JsxConstant
    public static final int SHADING_LANGUAGE_VERSION = 0x8B8C;
    /** The constant {@code CURRENT_PROGRAM}. */
    @JsxConstant
    public static final int CURRENT_PROGRAM = 0x8B8D;
    /** The constant {@code IMPLEMENTATION_COLOR_READ_TYPE}. */
    @JsxConstant
    public static final int IMPLEMENTATION_COLOR_READ_TYPE = 0x8B9A;
    /** The constant {@code IMPLEMENTATION_COLOR_READ_FORMAT}. */
    @JsxConstant
    public static final int IMPLEMENTATION_COLOR_READ_FORMAT = 0x8B9B;
    /** The constant {@code STENCIL_BACK_REF}. */
    @JsxConstant
    public static final int STENCIL_BACK_REF = 0x8CA3;
    /** The constant {@code STENCIL_BACK_VALUE_MASK}. */
    @JsxConstant
    public static final int STENCIL_BACK_VALUE_MASK = 0x8CA4;
    /** The constant {@code STENCIL_BACK_WRITEMASK}. */
    @JsxConstant
    public static final int STENCIL_BACK_WRITEMASK = 0x8CA5;
    /** The constant {@code FRAMEBUFFER_BINDING}. */
    @JsxConstant
    public static final int FRAMEBUFFER_BINDING = 0x8CA6;
    /** The constant {@code RENDERBUFFER_BINDING}. */
    @JsxConstant
    public static final int RENDERBUFFER_BINDING = 0x8CA7;
    /** The constant {@code FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE}. */
    @JsxConstant
    public static final int FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8CD0;
    /** The constant {@code FRAMEBUFFER_ATTACHMENT_OBJECT_NAME}. */
    @JsxConstant
    public static final int FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8CD1;
    /** The constant {@code FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL}. */
    @JsxConstant
    public static final int FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8CD2;
    /** The constant {@code FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE}. */
    @JsxConstant
    public static final int FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8CD3;
    /** The constant {@code FRAMEBUFFER_COMPLETE}. */
    @JsxConstant
    public static final int FRAMEBUFFER_COMPLETE = 0x8CD5;
    /** The constant {@code FRAMEBUFFER_INCOMPLETE_ATTACHMENT}. */
    @JsxConstant
    public static final int FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6;
    /** The constant {@code FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT}. */
    @JsxConstant
    public static final int FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7;
    /** The constant {@code FRAMEBUFFER_INCOMPLETE_DIMENSIONS}. */
    @JsxConstant
    public static final int FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8CD9;
    /** The constant {@code FRAMEBUFFER_UNSUPPORTED}. */
    @JsxConstant
    public static final int FRAMEBUFFER_UNSUPPORTED = 0x8CDD;
    /** The constant {@code COLOR_ATTACHMENT0}. */
    @JsxConstant
    public static final int COLOR_ATTACHMENT0 = 0x8ce0;
    /** The constant {@code DEPTH_ATTACHMENT}. */
    @JsxConstant
    public static final int DEPTH_ATTACHMENT = 0x8D00;
    /** The constant {@code STENCIL_ATTACHMENT}. */
    @JsxConstant
    public static final int STENCIL_ATTACHMENT = 0x8D20;
    /** The constant {@code FRAMEBUFFER}. */
    @JsxConstant
    public static final int FRAMEBUFFER = 0x8D40;
    /** The constant {@code RENDERBUFFER}. */
    @JsxConstant
    public static final int RENDERBUFFER = 0x8D41;
    /** The constant {@code RENDERBUFFER_WIDTH}. */
    @JsxConstant
    public static final int RENDERBUFFER_WIDTH = 0x8D42;
    /** The constant {@code RENDERBUFFER_HEIGHT}. */
    @JsxConstant
    public static final int RENDERBUFFER_HEIGHT = 0x8D43;
    /** The constant {@code RENDERBUFFER_INTERNAL_FORMAT}. */
    @JsxConstant
    public static final int RENDERBUFFER_INTERNAL_FORMAT = 0x8D44;
    /** The constant {@code STENCIL_INDEX8}. */
    @JsxConstant
    public static final int STENCIL_INDEX8 = 0x8D48;
    /** The constant {@code RENDERBUFFER_RED_SIZE}. */
    @JsxConstant
    public static final int RENDERBUFFER_RED_SIZE = 0x8D50;
    /** The constant {@code RENDERBUFFER_GREEN_SIZE}. */
    @JsxConstant
    public static final int RENDERBUFFER_GREEN_SIZE = 0x8D51;
    /** The constant {@code RENDERBUFFER_BLUE_SIZE}. */
    @JsxConstant
    public static final int RENDERBUFFER_BLUE_SIZE = 0x8D52;
    /** The constant {@code RENDERBUFFER_ALPHA_SIZE}. */
    @JsxConstant
    public static final int RENDERBUFFER_ALPHA_SIZE = 0x8D53;
    /** The constant {@code RENDERBUFFER_DEPTH_SIZE}. */
    @JsxConstant
    public static final int RENDERBUFFER_DEPTH_SIZE = 0x8D54;
    /** The constant {@code RENDERBUFFER_STENCIL_SIZE}. */
    @JsxConstant
    public static final int RENDERBUFFER_STENCIL_SIZE = 0x8D55;
    /** The constant {@code RGB565}. */
    @JsxConstant
    public static final int RGB565 = 0x8D62;
    /** The constant {@code LOW_FLOAT}. */
    @JsxConstant
    public static final int LOW_FLOAT = 0x8DF0;
    /** The constant {@code MEDIUM_FLOAT}. */
    @JsxConstant
    public static final int MEDIUM_FLOAT = 0x8DF1;
    /** The constant {@code HIGH_FLOAT}. */
    @JsxConstant
    public static final int HIGH_FLOAT = 0x8DF2;
    /** The constant {@code LOW_INT}. */
    @JsxConstant
    public static final int LOW_INT = 0x8DF3;
    /** The constant {@code MEDIUM_INT}. */
    @JsxConstant
    public static final int MEDIUM_INT = 0x8DF4;
    /** The constant {@code HIGH_INT}. */
    @JsxConstant
    public static final int HIGH_INT = 0x8DF5;
    /** The constant {@code MAX_VERTEX_UNIFORM_VECTORS}. */
    @JsxConstant
    public static final int MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB;
    /** The constant {@code MAX_VARYING_VECTORS}. */
    @JsxConstant
    public static final int MAX_VARYING_VECTORS = 0x8DFC;
    /** The constant {@code MAX_FRAGMENT_UNIFORM_VECTORS}. */
    @JsxConstant
    public static final int MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD;
    /** The constant {@code UNPACK_FLIP_Y_WEBGL}. */
    @JsxConstant
    public static final int UNPACK_FLIP_Y_WEBGL = 0x9240;
    /** The constant {@code UNPACK_PREMULTIPLY_ALPHA_WEBGL}. */
    @JsxConstant
    public static final int UNPACK_PREMULTIPLY_ALPHA_WEBGL = 0x9241;
    /** The constant {@code CONTEXT_LOST_WEBGL}. */
    @JsxConstant
    public static final int CONTEXT_LOST_WEBGL = 0x9242;
    /** The constant {@code UNPACK_COLORSPACE_CONVERSION_WEBGL}. */
    @JsxConstant
    public static final int UNPACK_COLORSPACE_CONVERSION_WEBGL = 0x9243;
    /** The constant {@code BROWSER_DEFAULT_WEBGL}. */
    @JsxConstant
    public static final int BROWSER_DEFAULT_WEBGL = 0x9244;

    /**
     * Default constructor.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public WebGLRenderingContext() {
    }
}
