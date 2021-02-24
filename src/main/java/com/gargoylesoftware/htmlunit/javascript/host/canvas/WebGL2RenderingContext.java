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
 * A JavaScript object for {@code WebGL2RenderingContext}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE, FF, FF78})
public class WebGL2RenderingContext extends SimpleScriptable {

    /** The constant {@code ACTIVE_ATTRIBUTES}. */
    @JsxConstant
    public static final long ACTIVE_ATTRIBUTES = 35_721L;

    /** The constant {@code ACTIVE_TEXTURE}. */
    @JsxConstant
    public static final long ACTIVE_TEXTURE = 34_016L;

    /** The constant {@code ACTIVE_UNIFORMS}. */
    @JsxConstant
    public static final long ACTIVE_UNIFORMS = 35_718L;

    /** The constant {@code ACTIVE_UNIFORM_BLOCKS}. */
    @JsxConstant
    public static final long ACTIVE_UNIFORM_BLOCKS = 35_382L;

    /** The constant {@code ALIASED_LINE_WIDTH_RANGE}. */
    @JsxConstant
    public static final long ALIASED_LINE_WIDTH_RANGE = 33_902L;

    /** The constant {@code ALIASED_POINT_SIZE_RANGE}. */
    @JsxConstant
    public static final long ALIASED_POINT_SIZE_RANGE = 33_901L;

    /** The constant {@code ALPHA}. */
    @JsxConstant
    public static final long ALPHA = 6_406L;

    /** The constant {@code ALPHA_BITS}. */
    @JsxConstant
    public static final long ALPHA_BITS = 3_413L;

    /** The constant {@code ALREADY_SIGNALED}. */
    @JsxConstant
    public static final long ALREADY_SIGNALED = 37_146L;

    /** The constant {@code ALWAYS}. */
    @JsxConstant
    public static final long ALWAYS = 519L;

    /** The constant {@code ANY_SAMPLES_PASSED}. */
    @JsxConstant
    public static final long ANY_SAMPLES_PASSED = 35_887L;

    /** The constant {@code ANY_SAMPLES_PASSED_CONSERVATIVE}. */
    @JsxConstant
    public static final long ANY_SAMPLES_PASSED_CONSERVATIVE = 36_202L;

    /** The constant {@code ARRAY_BUFFER}. */
    @JsxConstant
    public static final long ARRAY_BUFFER = 34_962L;

    /** The constant {@code ARRAY_BUFFER_BINDING}. */
    @JsxConstant
    public static final long ARRAY_BUFFER_BINDING = 34_964L;

    /** The constant {@code ATTACHED_SHADERS}. */
    @JsxConstant
    public static final long ATTACHED_SHADERS = 35_717L;

    /** The constant {@code BACK}. */
    @JsxConstant
    public static final long BACK = 1_029L;

    /** The constant {@code BLEND}. */
    @JsxConstant
    public static final long BLEND = 3_042L;

    /** The constant {@code BLEND_COLOR}. */
    @JsxConstant
    public static final long BLEND_COLOR = 32_773L;

    /** The constant {@code BLEND_DST_ALPHA}. */
    @JsxConstant
    public static final long BLEND_DST_ALPHA = 32_970L;

    /** The constant {@code BLEND_DST_RGB}. */
    @JsxConstant
    public static final long BLEND_DST_RGB = 32_968L;

    /** The constant {@code BLEND_EQUATION}. */
    @JsxConstant
    public static final long BLEND_EQUATION = 32_777L;

    /** The constant {@code BLEND_EQUATION_ALPHA}. */
    @JsxConstant
    public static final long BLEND_EQUATION_ALPHA = 34_877L;

    /** The constant {@code BLEND_EQUATION_RGB}. */
    @JsxConstant
    public static final long BLEND_EQUATION_RGB = 32_777L;

    /** The constant {@code BLEND_SRC_ALPHA}. */
    @JsxConstant
    public static final long BLEND_SRC_ALPHA = 32_971L;

    /** The constant {@code BLEND_SRC_RGB}. */
    @JsxConstant
    public static final long BLEND_SRC_RGB = 32_969L;

    /** The constant {@code BLUE_BITS}. */
    @JsxConstant
    public static final long BLUE_BITS = 3_412L;

    /** The constant {@code BOOL}. */
    @JsxConstant
    public static final long BOOL = 35_670L;

    /** The constant {@code BOOL_VEC2}. */
    @JsxConstant
    public static final long BOOL_VEC2 = 35_671L;

    /** The constant {@code BOOL_VEC3}. */
    @JsxConstant
    public static final long BOOL_VEC3 = 35_672L;

    /** The constant {@code BOOL_VEC4}. */
    @JsxConstant
    public static final long BOOL_VEC4 = 35_673L;

    /** The constant {@code BROWSER_DEFAULT_WEBGL}. */
    @JsxConstant
    public static final long BROWSER_DEFAULT_WEBGL = 37_444L;

    /** The constant {@code BUFFER_SIZE}. */
    @JsxConstant
    public static final long BUFFER_SIZE = 34_660L;

    /** The constant {@code BUFFER_USAGE}. */
    @JsxConstant
    public static final long BUFFER_USAGE = 34_661L;

    /** The constant {@code BYTE}. */
    @JsxConstant
    public static final long BYTE = 5_120L;

    /** The constant {@code CCW}. */
    @JsxConstant
    public static final long CCW = 2_305L;

    /** The constant {@code CLAMP_TO_EDGE}. */
    @JsxConstant
    public static final long CLAMP_TO_EDGE = 33_071L;

    /** The constant {@code COLOR}. */
    @JsxConstant
    public static final long COLOR = 6_144L;

    /** The constant {@code COLOR_ATTACHMENT0}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT0 = 36_064L;

    /** The constant {@code COLOR_ATTACHMENT1}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT1 = 36_065L;

    /** The constant {@code COLOR_ATTACHMENT10}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT10 = 36_074L;

    /** The constant {@code COLOR_ATTACHMENT11}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT11 = 36_075L;

    /** The constant {@code COLOR_ATTACHMENT12}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT12 = 36_076L;

    /** The constant {@code COLOR_ATTACHMENT13}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT13 = 36_077L;

    /** The constant {@code COLOR_ATTACHMENT14}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT14 = 36_078L;

    /** The constant {@code COLOR_ATTACHMENT15}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT15 = 36_079L;

    /** The constant {@code COLOR_ATTACHMENT2}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT2 = 36_066L;

    /** The constant {@code COLOR_ATTACHMENT3}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT3 = 36_067L;

    /** The constant {@code COLOR_ATTACHMENT4}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT4 = 36_068L;

    /** The constant {@code COLOR_ATTACHMENT5}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT5 = 36_069L;

    /** The constant {@code COLOR_ATTACHMENT6}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT6 = 36_070L;

    /** The constant {@code COLOR_ATTACHMENT7}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT7 = 36_071L;

    /** The constant {@code COLOR_ATTACHMENT8}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT8 = 36_072L;

    /** The constant {@code COLOR_ATTACHMENT9}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT9 = 36_073L;

    /** The constant {@code COLOR_BUFFER_BIT}. */
    @JsxConstant
    public static final long COLOR_BUFFER_BIT = 16_384L;

    /** The constant {@code COLOR_CLEAR_VALUE}. */
    @JsxConstant
    public static final long COLOR_CLEAR_VALUE = 3_106L;

    /** The constant {@code COLOR_WRITEMASK}. */
    @JsxConstant
    public static final long COLOR_WRITEMASK = 3_107L;

    /** The constant {@code COMPARE_REF_TO_TEXTURE}. */
    @JsxConstant
    public static final long COMPARE_REF_TO_TEXTURE = 34_894L;

    /** The constant {@code COMPILE_STATUS}. */
    @JsxConstant
    public static final long COMPILE_STATUS = 35_713L;

    /** The constant {@code COMPRESSED_TEXTURE_FORMATS}. */
    @JsxConstant
    public static final long COMPRESSED_TEXTURE_FORMATS = 34_467L;

    /** The constant {@code CONDITION_SATISFIED}. */
    @JsxConstant
    public static final long CONDITION_SATISFIED = 37_148L;

    /** The constant {@code CONSTANT_ALPHA}. */
    @JsxConstant
    public static final long CONSTANT_ALPHA = 32_771L;

    /** The constant {@code CONSTANT_COLOR}. */
    @JsxConstant
    public static final long CONSTANT_COLOR = 32_769L;

    /** The constant {@code CONTEXT_LOST_WEBGL}. */
    @JsxConstant
    public static final long CONTEXT_LOST_WEBGL = 37_442L;

    /** The constant {@code COPY_READ_BUFFER}. */
    @JsxConstant
    public static final long COPY_READ_BUFFER = 36_662L;

    /** The constant {@code COPY_READ_BUFFER_BINDING}. */
    @JsxConstant
    public static final long COPY_READ_BUFFER_BINDING = 36_662L;

    /** The constant {@code COPY_WRITE_BUFFER}. */
    @JsxConstant
    public static final long COPY_WRITE_BUFFER = 36_663L;

    /** The constant {@code COPY_WRITE_BUFFER_BINDING}. */
    @JsxConstant
    public static final long COPY_WRITE_BUFFER_BINDING = 36_663L;

    /** The constant {@code CULL_FACE}. */
    @JsxConstant
    public static final long CULL_FACE = 2_884L;

    /** The constant {@code CULL_FACE_MODE}. */
    @JsxConstant
    public static final long CULL_FACE_MODE = 2_885L;

    /** The constant {@code CURRENT_PROGRAM}. */
    @JsxConstant
    public static final long CURRENT_PROGRAM = 35_725L;

    /** The constant {@code CURRENT_QUERY}. */
    @JsxConstant
    public static final long CURRENT_QUERY = 34_917L;

    /** The constant {@code CURRENT_VERTEX_ATTRIB}. */
    @JsxConstant
    public static final long CURRENT_VERTEX_ATTRIB = 34_342L;

    /** The constant {@code CW}. */
    @JsxConstant
    public static final long CW = 2_304L;

    /** The constant {@code DECR}. */
    @JsxConstant
    public static final long DECR = 7_683L;

    /** The constant {@code DECR_WRAP}. */
    @JsxConstant
    public static final long DECR_WRAP = 34_056L;

    /** The constant {@code DELETE_STATUS}. */
    @JsxConstant
    public static final long DELETE_STATUS = 35_712L;

    /** The constant {@code DEPTH}. */
    @JsxConstant
    public static final long DEPTH = 6_145L;

    /** The constant {@code DEPTH24_STENCIL8}. */
    @JsxConstant
    public static final long DEPTH24_STENCIL8 = 35_056L;

    /** The constant {@code DEPTH32F_STENCIL8}. */
    @JsxConstant
    public static final long DEPTH32F_STENCIL8 = 36_013L;

    /** The constant {@code DEPTH_ATTACHMENT}. */
    @JsxConstant
    public static final long DEPTH_ATTACHMENT = 36_096L;

    /** The constant {@code DEPTH_BITS}. */
    @JsxConstant
    public static final long DEPTH_BITS = 3_414L;

    /** The constant {@code DEPTH_BUFFER_BIT}. */
    @JsxConstant
    public static final long DEPTH_BUFFER_BIT = 256L;

    /** The constant {@code DEPTH_CLEAR_VALUE}. */
    @JsxConstant
    public static final long DEPTH_CLEAR_VALUE = 2_931L;

    /** The constant {@code DEPTH_COMPONENT}. */
    @JsxConstant
    public static final long DEPTH_COMPONENT = 6_402L;

    /** The constant {@code DEPTH_COMPONENT16}. */
    @JsxConstant
    public static final long DEPTH_COMPONENT16 = 33_189L;

    /** The constant {@code DEPTH_COMPONENT24}. */
    @JsxConstant
    public static final long DEPTH_COMPONENT24 = 33_190L;

    /** The constant {@code DEPTH_COMPONENT32F}. */
    @JsxConstant
    public static final long DEPTH_COMPONENT32F = 36_012L;

    /** The constant {@code DEPTH_FUNC}. */
    @JsxConstant
    public static final long DEPTH_FUNC = 2_932L;

    /** The constant {@code DEPTH_RANGE}. */
    @JsxConstant
    public static final long DEPTH_RANGE = 2_928L;

    /** The constant {@code DEPTH_STENCIL}. */
    @JsxConstant
    public static final long DEPTH_STENCIL = 34_041L;

    /** The constant {@code DEPTH_STENCIL_ATTACHMENT}. */
    @JsxConstant
    public static final long DEPTH_STENCIL_ATTACHMENT = 33_306L;

    /** The constant {@code DEPTH_TEST}. */
    @JsxConstant
    public static final long DEPTH_TEST = 2_929L;

    /** The constant {@code DEPTH_WRITEMASK}. */
    @JsxConstant
    public static final long DEPTH_WRITEMASK = 2_930L;

    /** The constant {@code DITHER}. */
    @JsxConstant
    public static final long DITHER = 3_024L;

    /** The constant {@code DONT_CARE}. */
    @JsxConstant
    public static final long DONT_CARE = 4_352L;

    /** The constant {@code DRAW_BUFFER0}. */
    @JsxConstant
    public static final long DRAW_BUFFER0 = 34_853L;

    /** The constant {@code DRAW_BUFFER1}. */
    @JsxConstant
    public static final long DRAW_BUFFER1 = 34_854L;

    /** The constant {@code DRAW_BUFFER10}. */
    @JsxConstant
    public static final long DRAW_BUFFER10 = 34_863L;

    /** The constant {@code DRAW_BUFFER11}. */
    @JsxConstant
    public static final long DRAW_BUFFER11 = 34_864L;

    /** The constant {@code DRAW_BUFFER12}. */
    @JsxConstant
    public static final long DRAW_BUFFER12 = 34_865L;

    /** The constant {@code DRAW_BUFFER13}. */
    @JsxConstant
    public static final long DRAW_BUFFER13 = 34_866L;

    /** The constant {@code DRAW_BUFFER14}. */
    @JsxConstant
    public static final long DRAW_BUFFER14 = 34_867L;

    /** The constant {@code DRAW_BUFFER15}. */
    @JsxConstant
    public static final long DRAW_BUFFER15 = 34_868L;

    /** The constant {@code DRAW_BUFFER2}. */
    @JsxConstant
    public static final long DRAW_BUFFER2 = 34_855L;

    /** The constant {@code DRAW_BUFFER3}. */
    @JsxConstant
    public static final long DRAW_BUFFER3 = 34_856L;

    /** The constant {@code DRAW_BUFFER4}. */
    @JsxConstant
    public static final long DRAW_BUFFER4 = 34_857L;

    /** The constant {@code DRAW_BUFFER5}. */
    @JsxConstant
    public static final long DRAW_BUFFER5 = 34_858L;

    /** The constant {@code DRAW_BUFFER6}. */
    @JsxConstant
    public static final long DRAW_BUFFER6 = 34_859L;

    /** The constant {@code DRAW_BUFFER7}. */
    @JsxConstant
    public static final long DRAW_BUFFER7 = 34_860L;

    /** The constant {@code DRAW_BUFFER8}. */
    @JsxConstant
    public static final long DRAW_BUFFER8 = 34_861L;

    /** The constant {@code DRAW_BUFFER9}. */
    @JsxConstant
    public static final long DRAW_BUFFER9 = 34_862L;

    /** The constant {@code DRAW_FRAMEBUFFER}. */
    @JsxConstant
    public static final long DRAW_FRAMEBUFFER = 36_009L;

    /** The constant {@code DRAW_FRAMEBUFFER_BINDING}. */
    @JsxConstant
    public static final long DRAW_FRAMEBUFFER_BINDING = 36_006L;

    /** The constant {@code DST_ALPHA}. */
    @JsxConstant
    public static final long DST_ALPHA = 772L;

    /** The constant {@code DST_COLOR}. */
    @JsxConstant
    public static final long DST_COLOR = 774L;

    /** The constant {@code DYNAMIC_COPY}. */
    @JsxConstant
    public static final long DYNAMIC_COPY = 35_050L;

    /** The constant {@code DYNAMIC_DRAW}. */
    @JsxConstant
    public static final long DYNAMIC_DRAW = 35_048L;

    /** The constant {@code DYNAMIC_READ}. */
    @JsxConstant
    public static final long DYNAMIC_READ = 35_049L;

    /** The constant {@code ELEMENT_ARRAY_BUFFER}. */
    @JsxConstant
    public static final long ELEMENT_ARRAY_BUFFER = 34_963L;

    /** The constant {@code ELEMENT_ARRAY_BUFFER_BINDING}. */
    @JsxConstant
    public static final long ELEMENT_ARRAY_BUFFER_BINDING = 34_965L;

    /** The constant {@code EQUAL}. */
    @JsxConstant
    public static final long EQUAL = 514L;

    /** The constant {@code FASTEST}. */
    @JsxConstant
    public static final long FASTEST = 4_353L;

    /** The constant {@code FLOAT}. */
    @JsxConstant
    public static final long FLOAT = 5_126L;

    /** The constant {@code FLOAT_32_UNSIGNED_INT_24_8_REV}. */
    @JsxConstant
    public static final long FLOAT_32_UNSIGNED_INT_24_8_REV = 36_269L;

    /** The constant {@code FLOAT_MAT2}. */
    @JsxConstant
    public static final long FLOAT_MAT2 = 35_674L;

    /** The constant {@code FLOAT_MAT2x3}. */
    @JsxConstant
    public static final long FLOAT_MAT2x3 = 35_685L;

    /** The constant {@code FLOAT_MAT2x4}. */
    @JsxConstant
    public static final long FLOAT_MAT2x4 = 35_686L;

    /** The constant {@code FLOAT_MAT3}. */
    @JsxConstant
    public static final long FLOAT_MAT3 = 35_675L;

    /** The constant {@code FLOAT_MAT3x2}. */
    @JsxConstant
    public static final long FLOAT_MAT3x2 = 35_687L;

    /** The constant {@code FLOAT_MAT3x4}. */
    @JsxConstant
    public static final long FLOAT_MAT3x4 = 35_688L;

    /** The constant {@code FLOAT_MAT4}. */
    @JsxConstant
    public static final long FLOAT_MAT4 = 35_676L;

    /** The constant {@code FLOAT_MAT4x2}. */
    @JsxConstant
    public static final long FLOAT_MAT4x2 = 35_689L;

    /** The constant {@code FLOAT_MAT4x3}. */
    @JsxConstant
    public static final long FLOAT_MAT4x3 = 35_690L;

    /** The constant {@code FLOAT_VEC2}. */
    @JsxConstant
    public static final long FLOAT_VEC2 = 35_664L;

    /** The constant {@code FLOAT_VEC3}. */
    @JsxConstant
    public static final long FLOAT_VEC3 = 35_665L;

    /** The constant {@code FLOAT_VEC4}. */
    @JsxConstant
    public static final long FLOAT_VEC4 = 35_666L;

    /** The constant {@code FRAGMENT_SHADER}. */
    @JsxConstant
    public static final long FRAGMENT_SHADER = 35_632L;

    /** The constant {@code FRAGMENT_SHADER_DERIVATIVE_HINT}. */
    @JsxConstant
    public static final long FRAGMENT_SHADER_DERIVATIVE_HINT = 35_723L;

    /** The constant {@code FRAMEBUFFER}. */
    @JsxConstant
    public static final long FRAMEBUFFER = 36_160L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 33_301L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_BLUE_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 33_300L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 33_296L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 33_297L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 33_302L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_GREEN_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 33_299L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_OBJECT_NAME}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 36_049L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 36_048L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_RED_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_RED_SIZE = 33_298L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 33_303L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 36_051L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 36_052L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 36_050L;

    /** The constant {@code FRAMEBUFFER_BINDING}. */
    @JsxConstant
    public static final long FRAMEBUFFER_BINDING = 36_006L;

    /** The constant {@code FRAMEBUFFER_COMPLETE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_COMPLETE = 36_053L;

    /** The constant {@code FRAMEBUFFER_DEFAULT}. */
    @JsxConstant
    public static final long FRAMEBUFFER_DEFAULT = 33_304L;

    /** The constant {@code FRAMEBUFFER_INCOMPLETE_ATTACHMENT}. */
    @JsxConstant
    public static final long FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36_054L;

    /** The constant {@code FRAMEBUFFER_INCOMPLETE_DIMENSIONS}. */
    @JsxConstant
    public static final long FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 36_057L;

    /** The constant {@code FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT}. */
    @JsxConstant
    public static final long FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36_055L;

    /** The constant {@code FRAMEBUFFER_INCOMPLETE_MULTISAMPLE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 36_182L;

    /** The constant {@code FRAMEBUFFER_UNSUPPORTED}. */
    @JsxConstant
    public static final long FRAMEBUFFER_UNSUPPORTED = 36_061L;

    /** The constant {@code FRONT}. */
    @JsxConstant
    public static final long FRONT = 1_028L;

    /** The constant {@code FRONT_AND_BACK}. */
    @JsxConstant
    public static final long FRONT_AND_BACK = 1_032L;

    /** The constant {@code FRONT_FACE}. */
    @JsxConstant
    public static final long FRONT_FACE = 2_886L;

    /** The constant {@code FUNC_ADD}. */
    @JsxConstant
    public static final long FUNC_ADD = 32_774L;

    /** The constant {@code FUNC_REVERSE_SUBTRACT}. */
    @JsxConstant
    public static final long FUNC_REVERSE_SUBTRACT = 32_779L;

    /** The constant {@code FUNC_SUBTRACT}. */
    @JsxConstant
    public static final long FUNC_SUBTRACT = 32_778L;

    /** The constant {@code GENERATE_MIPMAP_HINT}. */
    @JsxConstant
    public static final long GENERATE_MIPMAP_HINT = 33_170L;

    /** The constant {@code GEQUAL}. */
    @JsxConstant
    public static final long GEQUAL = 518L;

    /** The constant {@code GREATER}. */
    @JsxConstant
    public static final long GREATER = 516L;

    /** The constant {@code GREEN_BITS}. */
    @JsxConstant
    public static final long GREEN_BITS = 3_411L;

    /** The constant {@code HALF_FLOAT}. */
    @JsxConstant
    public static final long HALF_FLOAT = 5_131L;

    /** The constant {@code HIGH_FLOAT}. */
    @JsxConstant
    public static final long HIGH_FLOAT = 36_338L;

    /** The constant {@code HIGH_INT}. */
    @JsxConstant
    public static final long HIGH_INT = 36_341L;

    /** The constant {@code IMPLEMENTATION_COLOR_READ_FORMAT}. */
    @JsxConstant
    public static final long IMPLEMENTATION_COLOR_READ_FORMAT = 35_739L;

    /** The constant {@code IMPLEMENTATION_COLOR_READ_TYPE}. */
    @JsxConstant
    public static final long IMPLEMENTATION_COLOR_READ_TYPE = 35_738L;

    /** The constant {@code INCR}. */
    @JsxConstant
    public static final long INCR = 7_682L;

    /** The constant {@code INCR_WRAP}. */
    @JsxConstant
    public static final long INCR_WRAP = 34_055L;

    /** The constant {@code INT}. */
    @JsxConstant
    public static final long INT = 5_124L;

    /** The constant {@code INTERLEAVED_ATTRIBS}. */
    @JsxConstant
    public static final long INTERLEAVED_ATTRIBS = 35_980L;

    /** The constant {@code INT_2_10_10_10_REV}. */
    @JsxConstant
    public static final long INT_2_10_10_10_REV = 36_255L;

    /** The constant {@code INT_SAMPLER_2D}. */
    @JsxConstant
    public static final long INT_SAMPLER_2D = 36_298L;

    /** The constant {@code INT_SAMPLER_2D_ARRAY}. */
    @JsxConstant
    public static final long INT_SAMPLER_2D_ARRAY = 36_303L;

    /** The constant {@code INT_SAMPLER_3D}. */
    @JsxConstant
    public static final long INT_SAMPLER_3D = 36_299L;

    /** The constant {@code INT_SAMPLER_CUBE}. */
    @JsxConstant
    public static final long INT_SAMPLER_CUBE = 36_300L;

    /** The constant {@code INT_VEC2}. */
    @JsxConstant
    public static final long INT_VEC2 = 35_667L;

    /** The constant {@code INT_VEC3}. */
    @JsxConstant
    public static final long INT_VEC3 = 35_668L;

    /** The constant {@code INT_VEC4}. */
    @JsxConstant
    public static final long INT_VEC4 = 35_669L;

    /** The constant {@code INVALID_ENUM}. */
    @JsxConstant
    public static final long INVALID_ENUM = 1_280L;

    /** The constant {@code INVALID_FRAMEBUFFER_OPERATION}. */
    @JsxConstant
    public static final long INVALID_FRAMEBUFFER_OPERATION = 1_286L;

    /** The constant {@code INVALID_INDEX}. */
    @JsxConstant
    public static final long INVALID_INDEX = 4_294_967_295L;

    /** The constant {@code INVALID_OPERATION}. */
    @JsxConstant
    public static final long INVALID_OPERATION = 1_282L;

    /** The constant {@code INVALID_VALUE}. */
    @JsxConstant
    public static final long INVALID_VALUE = 1_281L;

    /** The constant {@code INVERT}. */
    @JsxConstant
    public static final long INVERT = 5_386L;

    /** The constant {@code KEEP}. */
    @JsxConstant
    public static final long KEEP = 7_680L;

    /** The constant {@code LEQUAL}. */
    @JsxConstant
    public static final long LEQUAL = 515L;

    /** The constant {@code LESS}. */
    @JsxConstant
    public static final long LESS = 513L;

    /** The constant {@code LINEAR}. */
    @JsxConstant
    public static final long LINEAR = 9_729L;

    /** The constant {@code LINEAR_MIPMAP_LINEAR}. */
    @JsxConstant
    public static final long LINEAR_MIPMAP_LINEAR = 9_987L;

    /** The constant {@code LINEAR_MIPMAP_NEAREST}. */
    @JsxConstant
    public static final long LINEAR_MIPMAP_NEAREST = 9_985L;

    /** The constant {@code LINES}. */
    @JsxConstant
    public static final long LINES = 1L;

    /** The constant {@code LINE_LOOP}. */
    @JsxConstant
    public static final long LINE_LOOP = 2L;

    /** The constant {@code LINE_STRIP}. */
    @JsxConstant
    public static final long LINE_STRIP = 3L;

    /** The constant {@code LINE_WIDTH}. */
    @JsxConstant
    public static final long LINE_WIDTH = 2_849L;

    /** The constant {@code LINK_STATUS}. */
    @JsxConstant
    public static final long LINK_STATUS = 35_714L;

    /** The constant {@code LOW_FLOAT}. */
    @JsxConstant
    public static final long LOW_FLOAT = 36_336L;

    /** The constant {@code LOW_INT}. */
    @JsxConstant
    public static final long LOW_INT = 36_339L;

    /** The constant {@code LUMINANCE}. */
    @JsxConstant
    public static final long LUMINANCE = 6_409L;

    /** The constant {@code LUMINANCE_ALPHA}. */
    @JsxConstant
    public static final long LUMINANCE_ALPHA = 6_410L;

    /** The constant {@code MAX}. */
    @JsxConstant
    public static final long MAX = 32_776L;

    /** The constant {@code MAX_3D_TEXTURE_SIZE}. */
    @JsxConstant
    public static final long MAX_3D_TEXTURE_SIZE = 32_883L;

    /** The constant {@code MAX_ARRAY_TEXTURE_LAYERS}. */
    @JsxConstant
    public static final long MAX_ARRAY_TEXTURE_LAYERS = 35_071L;

    /** The constant {@code MAX_CLIENT_WAIT_TIMEOUT_WEBGL}. */
    @JsxConstant
    public static final long MAX_CLIENT_WAIT_TIMEOUT_WEBGL = 37_447L;

    /** The constant {@code MAX_COLOR_ATTACHMENTS}. */
    @JsxConstant
    public static final long MAX_COLOR_ATTACHMENTS = 36_063L;

    /** The constant {@code MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 35_379L;

    /** The constant {@code MAX_COMBINED_TEXTURE_IMAGE_UNITS}. */
    @JsxConstant
    public static final long MAX_COMBINED_TEXTURE_IMAGE_UNITS = 35_661L;

    /** The constant {@code MAX_COMBINED_UNIFORM_BLOCKS}. */
    @JsxConstant
    public static final long MAX_COMBINED_UNIFORM_BLOCKS = 35_374L;

    /** The constant {@code MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 35_377L;

    /** The constant {@code MAX_CUBE_MAP_TEXTURE_SIZE}. */
    @JsxConstant
    public static final long MAX_CUBE_MAP_TEXTURE_SIZE = 34_076L;

    /** The constant {@code MAX_DRAW_BUFFERS}. */
    @JsxConstant
    public static final long MAX_DRAW_BUFFERS = 34_852L;

    /** The constant {@code MAX_ELEMENTS_INDICES}. */
    @JsxConstant
    public static final long MAX_ELEMENTS_INDICES = 33_001L;

    /** The constant {@code MAX_ELEMENTS_VERTICES}. */
    @JsxConstant
    public static final long MAX_ELEMENTS_VERTICES = 33_000L;

    /** The constant {@code MAX_ELEMENT_INDEX}. */
    @JsxConstant
    public static final long MAX_ELEMENT_INDEX = 36_203L;

    /** The constant {@code MAX_FRAGMENT_INPUT_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_FRAGMENT_INPUT_COMPONENTS = 37_157L;

    /** The constant {@code MAX_FRAGMENT_UNIFORM_BLOCKS}. */
    @JsxConstant
    public static final long MAX_FRAGMENT_UNIFORM_BLOCKS = 35_373L;

    /** The constant {@code MAX_FRAGMENT_UNIFORM_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_FRAGMENT_UNIFORM_COMPONENTS = 35_657L;

    /** The constant {@code MAX_FRAGMENT_UNIFORM_VECTORS}. */
    @JsxConstant
    public static final long MAX_FRAGMENT_UNIFORM_VECTORS = 36_349L;

    /** The constant {@code MAX_PROGRAM_TEXEL_OFFSET}. */
    @JsxConstant
    public static final long MAX_PROGRAM_TEXEL_OFFSET = 35_077L;

    /** The constant {@code MAX_RENDERBUFFER_SIZE}. */
    @JsxConstant
    public static final long MAX_RENDERBUFFER_SIZE = 34_024L;

    /** The constant {@code MAX_SAMPLES}. */
    @JsxConstant
    public static final long MAX_SAMPLES = 36_183L;

    /** The constant {@code MAX_SERVER_WAIT_TIMEOUT}. */
    @JsxConstant
    public static final long MAX_SERVER_WAIT_TIMEOUT = 37_137L;

    /** The constant {@code MAX_TEXTURE_IMAGE_UNITS}. */
    @JsxConstant
    public static final long MAX_TEXTURE_IMAGE_UNITS = 34_930L;

    /** The constant {@code MAX_TEXTURE_LOD_BIAS}. */
    @JsxConstant
    public static final long MAX_TEXTURE_LOD_BIAS = 34_045L;

    /** The constant {@code MAX_TEXTURE_SIZE}. */
    @JsxConstant
    public static final long MAX_TEXTURE_SIZE = 3_379L;

    /** The constant {@code MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 35_978L;

    /** The constant {@code MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS}. */
    @JsxConstant
    public static final long MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 35_979L;

    /** The constant {@code MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 35_968L;

    /** The constant {@code MAX_UNIFORM_BLOCK_SIZE}. */
    @JsxConstant
    public static final long MAX_UNIFORM_BLOCK_SIZE = 35_376L;

    /** The constant {@code MAX_UNIFORM_BUFFER_BINDINGS}. */
    @JsxConstant
    public static final long MAX_UNIFORM_BUFFER_BINDINGS = 35_375L;

    /** The constant {@code MAX_VARYING_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_VARYING_COMPONENTS = 35_659L;

    /** The constant {@code MAX_VARYING_VECTORS}. */
    @JsxConstant
    public static final long MAX_VARYING_VECTORS = 36_348L;

    /** The constant {@code MAX_VERTEX_ATTRIBS}. */
    @JsxConstant
    public static final long MAX_VERTEX_ATTRIBS = 34_921L;

    /** The constant {@code MAX_VERTEX_OUTPUT_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_VERTEX_OUTPUT_COMPONENTS = 37_154L;

    /** The constant {@code MAX_VERTEX_TEXTURE_IMAGE_UNITS}. */
    @JsxConstant
    public static final long MAX_VERTEX_TEXTURE_IMAGE_UNITS = 35_660L;

    /** The constant {@code MAX_VERTEX_UNIFORM_BLOCKS}. */
    @JsxConstant
    public static final long MAX_VERTEX_UNIFORM_BLOCKS = 35_371L;

    /** The constant {@code MAX_VERTEX_UNIFORM_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_VERTEX_UNIFORM_COMPONENTS = 35_658L;

    /** The constant {@code MAX_VERTEX_UNIFORM_VECTORS}. */
    @JsxConstant
    public static final long MAX_VERTEX_UNIFORM_VECTORS = 36_347L;

    /** The constant {@code MAX_VIEWPORT_DIMS}. */
    @JsxConstant
    public static final long MAX_VIEWPORT_DIMS = 3_386L;

    /** The constant {@code MEDIUM_FLOAT}. */
    @JsxConstant
    public static final long MEDIUM_FLOAT = 36_337L;

    /** The constant {@code MEDIUM_INT}. */
    @JsxConstant
    public static final long MEDIUM_INT = 36_340L;

    /** The constant {@code MIN}. */
    @JsxConstant
    public static final long MIN = 32_775L;

    /** The constant {@code MIN_PROGRAM_TEXEL_OFFSET}. */
    @JsxConstant
    public static final long MIN_PROGRAM_TEXEL_OFFSET = 35_076L;

    /** The constant {@code MIRRORED_REPEAT}. */
    @JsxConstant
    public static final long MIRRORED_REPEAT = 33_648L;

    /** The constant {@code NEAREST}. */
    @JsxConstant
    public static final long NEAREST = 9_728L;

    /** The constant {@code NEAREST_MIPMAP_LINEAR}. */
    @JsxConstant
    public static final long NEAREST_MIPMAP_LINEAR = 9_986L;

    /** The constant {@code NEAREST_MIPMAP_NEAREST}. */
    @JsxConstant
    public static final long NEAREST_MIPMAP_NEAREST = 9_984L;

    /** The constant {@code NEVER}. */
    @JsxConstant
    public static final long NEVER = 512L;

    /** The constant {@code NICEST}. */
    @JsxConstant
    public static final long NICEST = 4_354L;

    /** The constant {@code NONE}. */
    @JsxConstant
    public static final long NONE = 0L;

    /** The constant {@code NOTEQUAL}. */
    @JsxConstant
    public static final long NOTEQUAL = 517L;

    /** The constant {@code NO_ERROR}. */
    @JsxConstant
    public static final long NO_ERROR = 0L;

    /** The constant {@code OBJECT_TYPE}. */
    @JsxConstant
    public static final long OBJECT_TYPE = 37_138L;

    /** The constant {@code ONE}. */
    @JsxConstant
    public static final long ONE = 1L;

    /** The constant {@code ONE_MINUS_CONSTANT_ALPHA}. */
    @JsxConstant
    public static final long ONE_MINUS_CONSTANT_ALPHA = 32_772L;

    /** The constant {@code ONE_MINUS_CONSTANT_COLOR}. */
    @JsxConstant
    public static final long ONE_MINUS_CONSTANT_COLOR = 32_770L;

    /** The constant {@code ONE_MINUS_DST_ALPHA}. */
    @JsxConstant
    public static final long ONE_MINUS_DST_ALPHA = 773L;

    /** The constant {@code ONE_MINUS_DST_COLOR}. */
    @JsxConstant
    public static final long ONE_MINUS_DST_COLOR = 775L;

    /** The constant {@code ONE_MINUS_SRC_ALPHA}. */
    @JsxConstant
    public static final long ONE_MINUS_SRC_ALPHA = 771L;

    /** The constant {@code ONE_MINUS_SRC_COLOR}. */
    @JsxConstant
    public static final long ONE_MINUS_SRC_COLOR = 769L;

    /** The constant {@code OUT_OF_MEMORY}. */
    @JsxConstant
    public static final long OUT_OF_MEMORY = 1_285L;

    /** The constant {@code PACK_ALIGNMENT}. */
    @JsxConstant
    public static final long PACK_ALIGNMENT = 3_333L;

    /** The constant {@code PACK_ROW_LENGTH}. */
    @JsxConstant
    public static final long PACK_ROW_LENGTH = 3_330L;

    /** The constant {@code PACK_SKIP_PIXELS}. */
    @JsxConstant
    public static final long PACK_SKIP_PIXELS = 3_332L;

    /** The constant {@code PACK_SKIP_ROWS}. */
    @JsxConstant
    public static final long PACK_SKIP_ROWS = 3_331L;

    /** The constant {@code PIXEL_PACK_BUFFER}. */
    @JsxConstant
    public static final long PIXEL_PACK_BUFFER = 35_051L;

    /** The constant {@code PIXEL_PACK_BUFFER_BINDING}. */
    @JsxConstant
    public static final long PIXEL_PACK_BUFFER_BINDING = 35_053L;

    /** The constant {@code PIXEL_UNPACK_BUFFER}. */
    @JsxConstant
    public static final long PIXEL_UNPACK_BUFFER = 35_052L;

    /** The constant {@code PIXEL_UNPACK_BUFFER_BINDING}. */
    @JsxConstant
    public static final long PIXEL_UNPACK_BUFFER_BINDING = 35_055L;

    /** The constant {@code POINTS}. */
    @JsxConstant
    public static final long POINTS = 0L;

    /** The constant {@code POLYGON_OFFSET_FACTOR}. */
    @JsxConstant
    public static final long POLYGON_OFFSET_FACTOR = 32_824L;

    /** The constant {@code POLYGON_OFFSET_FILL}. */
    @JsxConstant
    public static final long POLYGON_OFFSET_FILL = 32_823L;

    /** The constant {@code POLYGON_OFFSET_UNITS}. */
    @JsxConstant
    public static final long POLYGON_OFFSET_UNITS = 10_752L;

    /** The constant {@code QUERY_RESULT}. */
    @JsxConstant
    public static final long QUERY_RESULT = 34_918L;

    /** The constant {@code QUERY_RESULT_AVAILABLE}. */
    @JsxConstant
    public static final long QUERY_RESULT_AVAILABLE = 34_919L;

    /** The constant {@code R11F_G11F_B10F}. */
    @JsxConstant
    public static final long R11F_G11F_B10F = 35_898L;

    /** The constant {@code R16F}. */
    @JsxConstant
    public static final long R16F = 33_325L;

    /** The constant {@code R16I}. */
    @JsxConstant
    public static final long R16I = 33_331L;

    /** The constant {@code R16UI}. */
    @JsxConstant
    public static final long R16UI = 33_332L;

    /** The constant {@code R32F}. */
    @JsxConstant
    public static final long R32F = 33_326L;

    /** The constant {@code R32I}. */
    @JsxConstant
    public static final long R32I = 33_333L;

    /** The constant {@code R32UI}. */
    @JsxConstant
    public static final long R32UI = 33_334L;

    /** The constant {@code R8}. */
    @JsxConstant
    public static final long R8 = 33_321L;

    /** The constant {@code R8I}. */
    @JsxConstant
    public static final long R8I = 33_329L;

    /** The constant {@code R8UI}. */
    @JsxConstant
    public static final long R8UI = 33_330L;

    /** The constant {@code R8_SNORM}. */
    @JsxConstant
    public static final long R8_SNORM = 36_756L;

    /** The constant {@code RASTERIZER_DISCARD}. */
    @JsxConstant
    public static final long RASTERIZER_DISCARD = 35_977L;

    /** The constant {@code READ_BUFFER}. */
    @JsxConstant
    public static final long READ_BUFFER = 3_074L;

    /** The constant {@code READ_FRAMEBUFFER}. */
    @JsxConstant
    public static final long READ_FRAMEBUFFER = 36_008L;

    /** The constant {@code READ_FRAMEBUFFER_BINDING}. */
    @JsxConstant
    public static final long READ_FRAMEBUFFER_BINDING = 36_010L;

    /** The constant {@code RED}. */
    @JsxConstant
    public static final long RED = 6_403L;

    /** The constant {@code RED_BITS}. */
    @JsxConstant
    public static final long RED_BITS = 3_410L;

    /** The constant {@code RED_INTEGER}. */
    @JsxConstant
    public static final long RED_INTEGER = 36_244L;

    /** The constant {@code RENDERBUFFER}. */
    @JsxConstant
    public static final long RENDERBUFFER = 36_161L;

    /** The constant {@code RENDERBUFFER_ALPHA_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_ALPHA_SIZE = 36_179L;

    /** The constant {@code RENDERBUFFER_BINDING}. */
    @JsxConstant
    public static final long RENDERBUFFER_BINDING = 36_007L;

    /** The constant {@code RENDERBUFFER_BLUE_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_BLUE_SIZE = 36_178L;

    /** The constant {@code RENDERBUFFER_DEPTH_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_DEPTH_SIZE = 36_180L;

    /** The constant {@code RENDERBUFFER_GREEN_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_GREEN_SIZE = 36_177L;

    /** The constant {@code RENDERBUFFER_HEIGHT}. */
    @JsxConstant
    public static final long RENDERBUFFER_HEIGHT = 36_163L;

    /** The constant {@code RENDERBUFFER_INTERNAL_FORMAT}. */
    @JsxConstant
    public static final long RENDERBUFFER_INTERNAL_FORMAT = 36_164L;

    /** The constant {@code RENDERBUFFER_RED_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_RED_SIZE = 36_176L;

    /** The constant {@code RENDERBUFFER_SAMPLES}. */
    @JsxConstant
    public static final long RENDERBUFFER_SAMPLES = 36_011L;

    /** The constant {@code RENDERBUFFER_STENCIL_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_STENCIL_SIZE = 36_181L;

    /** The constant {@code RENDERBUFFER_WIDTH}. */
    @JsxConstant
    public static final long RENDERBUFFER_WIDTH = 36_162L;

    /** The constant {@code RENDERER}. */
    @JsxConstant
    public static final long RENDERER = 7_937L;

    /** The constant {@code REPEAT}. */
    @JsxConstant
    public static final long REPEAT = 10_497L;

    /** The constant {@code REPLACE}. */
    @JsxConstant
    public static final long REPLACE = 7_681L;

    /** The constant {@code RG}. */
    @JsxConstant
    public static final long RG = 33_319L;

    /** The constant {@code RG16F}. */
    @JsxConstant
    public static final long RG16F = 33_327L;

    /** The constant {@code RG16I}. */
    @JsxConstant
    public static final long RG16I = 33_337L;

    /** The constant {@code RG16UI}. */
    @JsxConstant
    public static final long RG16UI = 33_338L;

    /** The constant {@code RG32F}. */
    @JsxConstant
    public static final long RG32F = 33_328L;

    /** The constant {@code RG32I}. */
    @JsxConstant
    public static final long RG32I = 33_339L;

    /** The constant {@code RG32UI}. */
    @JsxConstant
    public static final long RG32UI = 33_340L;

    /** The constant {@code RG8}. */
    @JsxConstant
    public static final long RG8 = 33_323L;

    /** The constant {@code RG8I}. */
    @JsxConstant
    public static final long RG8I = 33_335L;

    /** The constant {@code RG8UI}. */
    @JsxConstant
    public static final long RG8UI = 33_336L;

    /** The constant {@code RG8_SNORM}. */
    @JsxConstant
    public static final long RG8_SNORM = 36_757L;

    /** The constant {@code RGB}. */
    @JsxConstant
    public static final long RGB = 6_407L;

    /** The constant {@code RGB10_A2}. */
    @JsxConstant
    public static final long RGB10_A2 = 32_857L;

    /** The constant {@code RGB10_A2UI}. */
    @JsxConstant
    public static final long RGB10_A2UI = 36_975L;

    /** The constant {@code RGB16F}. */
    @JsxConstant
    public static final long RGB16F = 34_843L;

    /** The constant {@code RGB16I}. */
    @JsxConstant
    public static final long RGB16I = 36_233L;

    /** The constant {@code RGB16UI}. */
    @JsxConstant
    public static final long RGB16UI = 36_215L;

    /** The constant {@code RGB32F}. */
    @JsxConstant
    public static final long RGB32F = 34_837L;

    /** The constant {@code RGB32I}. */
    @JsxConstant
    public static final long RGB32I = 36_227L;

    /** The constant {@code RGB32UI}. */
    @JsxConstant
    public static final long RGB32UI = 36_209L;

    /** The constant {@code RGB565}. */
    @JsxConstant
    public static final long RGB565 = 36_194L;

    /** The constant {@code RGB5_A1}. */
    @JsxConstant
    public static final long RGB5_A1 = 32_855L;

    /** The constant {@code RGB8}. */
    @JsxConstant
    public static final long RGB8 = 32_849L;

    /** The constant {@code RGB8I}. */
    @JsxConstant
    public static final long RGB8I = 36_239L;

    /** The constant {@code RGB8UI}. */
    @JsxConstant
    public static final long RGB8UI = 36_221L;

    /** The constant {@code RGB8_SNORM}. */
    @JsxConstant
    public static final long RGB8_SNORM = 36_758L;

    /** The constant {@code RGB9_E5}. */
    @JsxConstant
    public static final long RGB9_E5 = 35_901L;

    /** The constant {@code RGBA}. */
    @JsxConstant
    public static final long RGBA = 6_408L;

    /** The constant {@code RGBA16F}. */
    @JsxConstant
    public static final long RGBA16F = 34_842L;

    /** The constant {@code RGBA16I}. */
    @JsxConstant
    public static final long RGBA16I = 36_232L;

    /** The constant {@code RGBA16UI}. */
    @JsxConstant
    public static final long RGBA16UI = 36_214L;

    /** The constant {@code RGBA32F}. */
    @JsxConstant
    public static final long RGBA32F = 34_836L;

    /** The constant {@code RGBA32I}. */
    @JsxConstant
    public static final long RGBA32I = 36_226L;

    /** The constant {@code RGBA32UI}. */
    @JsxConstant
    public static final long RGBA32UI = 36_208L;

    /** The constant {@code RGBA4}. */
    @JsxConstant
    public static final long RGBA4 = 32_854L;

    /** The constant {@code RGBA8}. */
    @JsxConstant
    public static final long RGBA8 = 32_856L;

    /** The constant {@code RGBA8I}. */
    @JsxConstant
    public static final long RGBA8I = 36_238L;

    /** The constant {@code RGBA8UI}. */
    @JsxConstant
    public static final long RGBA8UI = 36_220L;

    /** The constant {@code RGBA8_SNORM}. */
    @JsxConstant
    public static final long RGBA8_SNORM = 36_759L;

    /** The constant {@code RGBA_INTEGER}. */
    @JsxConstant
    public static final long RGBA_INTEGER = 36_249L;

    /** The constant {@code RGB_INTEGER}. */
    @JsxConstant
    public static final long RGB_INTEGER = 36_248L;

    /** The constant {@code RG_INTEGER}. */
    @JsxConstant
    public static final long RG_INTEGER = 33_320L;

    /** The constant {@code SAMPLER_2D}. */
    @JsxConstant
    public static final long SAMPLER_2D = 35_678L;

    /** The constant {@code SAMPLER_2D_ARRAY}. */
    @JsxConstant
    public static final long SAMPLER_2D_ARRAY = 36_289L;

    /** The constant {@code SAMPLER_2D_ARRAY_SHADOW}. */
    @JsxConstant
    public static final long SAMPLER_2D_ARRAY_SHADOW = 36_292L;

    /** The constant {@code SAMPLER_2D_SHADOW}. */
    @JsxConstant
    public static final long SAMPLER_2D_SHADOW = 35_682L;

    /** The constant {@code SAMPLER_3D}. */
    @JsxConstant
    public static final long SAMPLER_3D = 35_679L;

    /** The constant {@code SAMPLER_BINDING}. */
    @JsxConstant
    public static final long SAMPLER_BINDING = 35_097L;

    /** The constant {@code SAMPLER_CUBE}. */
    @JsxConstant
    public static final long SAMPLER_CUBE = 35_680L;

    /** The constant {@code SAMPLER_CUBE_SHADOW}. */
    @JsxConstant
    public static final long SAMPLER_CUBE_SHADOW = 36_293L;

    /** The constant {@code SAMPLES}. */
    @JsxConstant
    public static final long SAMPLES = 32_937L;

    /** The constant {@code SAMPLE_ALPHA_TO_COVERAGE}. */
    @JsxConstant
    public static final long SAMPLE_ALPHA_TO_COVERAGE = 32_926L;

    /** The constant {@code SAMPLE_BUFFERS}. */
    @JsxConstant
    public static final long SAMPLE_BUFFERS = 32_936L;

    /** The constant {@code SAMPLE_COVERAGE}. */
    @JsxConstant
    public static final long SAMPLE_COVERAGE = 32_928L;

    /** The constant {@code SAMPLE_COVERAGE_INVERT}. */
    @JsxConstant
    public static final long SAMPLE_COVERAGE_INVERT = 32_939L;

    /** The constant {@code SAMPLE_COVERAGE_VALUE}. */
    @JsxConstant
    public static final long SAMPLE_COVERAGE_VALUE = 32_938L;

    /** The constant {@code SCISSOR_BOX}. */
    @JsxConstant
    public static final long SCISSOR_BOX = 3_088L;

    /** The constant {@code SCISSOR_TEST}. */
    @JsxConstant
    public static final long SCISSOR_TEST = 3_089L;

    /** The constant {@code SEPARATE_ATTRIBS}. */
    @JsxConstant
    public static final long SEPARATE_ATTRIBS = 35_981L;

    /** The constant {@code SHADER_TYPE}. */
    @JsxConstant
    public static final long SHADER_TYPE = 35_663L;

    /** The constant {@code SHADING_LANGUAGE_VERSION}. */
    @JsxConstant
    public static final long SHADING_LANGUAGE_VERSION = 35_724L;

    /** The constant {@code SHORT}. */
    @JsxConstant
    public static final long SHORT = 5_122L;

    /** The constant {@code SIGNALED}. */
    @JsxConstant
    public static final long SIGNALED = 37_145L;

    /** The constant {@code SIGNED_NORMALIZED}. */
    @JsxConstant
    public static final long SIGNED_NORMALIZED = 36_764L;

    /** The constant {@code SRC_ALPHA}. */
    @JsxConstant
    public static final long SRC_ALPHA = 770L;

    /** The constant {@code SRC_ALPHA_SATURATE}. */
    @JsxConstant
    public static final long SRC_ALPHA_SATURATE = 776L;

    /** The constant {@code SRC_COLOR}. */
    @JsxConstant
    public static final long SRC_COLOR = 768L;

    /** The constant {@code SRGB}. */
    @JsxConstant
    public static final long SRGB = 35_904L;

    /** The constant {@code SRGB8}. */
    @JsxConstant
    public static final long SRGB8 = 35_905L;

    /** The constant {@code SRGB8_ALPHA8}. */
    @JsxConstant
    public static final long SRGB8_ALPHA8 = 35_907L;

    /** The constant {@code STATIC_COPY}. */
    @JsxConstant
    public static final long STATIC_COPY = 35_046L;

    /** The constant {@code STATIC_DRAW}. */
    @JsxConstant
    public static final long STATIC_DRAW = 35_044L;

    /** The constant {@code STATIC_READ}. */
    @JsxConstant
    public static final long STATIC_READ = 35_045L;

    /** The constant {@code STENCIL}. */
    @JsxConstant
    public static final long STENCIL = 6_146L;

    /** The constant {@code STENCIL_ATTACHMENT}. */
    @JsxConstant
    public static final long STENCIL_ATTACHMENT = 36_128L;

    /** The constant {@code STENCIL_BACK_FAIL}. */
    @JsxConstant
    public static final long STENCIL_BACK_FAIL = 34_817L;

    /** The constant {@code STENCIL_BACK_FUNC}. */
    @JsxConstant
    public static final long STENCIL_BACK_FUNC = 34_816L;

    /** The constant {@code STENCIL_BACK_PASS_DEPTH_FAIL}. */
    @JsxConstant
    public static final long STENCIL_BACK_PASS_DEPTH_FAIL = 34_818L;

    /** The constant {@code STENCIL_BACK_PASS_DEPTH_PASS}. */
    @JsxConstant
    public static final long STENCIL_BACK_PASS_DEPTH_PASS = 34_819L;

    /** The constant {@code STENCIL_BACK_REF}. */
    @JsxConstant
    public static final long STENCIL_BACK_REF = 36_003L;

    /** The constant {@code STENCIL_BACK_VALUE_MASK}. */
    @JsxConstant
    public static final long STENCIL_BACK_VALUE_MASK = 36_004L;

    /** The constant {@code STENCIL_BACK_WRITEMASK}. */
    @JsxConstant
    public static final long STENCIL_BACK_WRITEMASK = 36_005L;

    /** The constant {@code STENCIL_BITS}. */
    @JsxConstant
    public static final long STENCIL_BITS = 3_415L;

    /** The constant {@code STENCIL_BUFFER_BIT}. */
    @JsxConstant
    public static final long STENCIL_BUFFER_BIT = 1_024L;

    /** The constant {@code STENCIL_CLEAR_VALUE}. */
    @JsxConstant
    public static final long STENCIL_CLEAR_VALUE = 2_961L;

    /** The constant {@code STENCIL_FAIL}. */
    @JsxConstant
    public static final long STENCIL_FAIL = 2_964L;

    /** The constant {@code STENCIL_FUNC}. */
    @JsxConstant
    public static final long STENCIL_FUNC = 2_962L;

    /** The constant {@code STENCIL_INDEX}. */
    @JsxConstant(IE)
    public static final long STENCIL_INDEX = 6_401L;

    /** The constant {@code STENCIL_INDEX8}. */
    @JsxConstant
    public static final long STENCIL_INDEX8 = 36_168L;

    /** The constant {@code STENCIL_PASS_DEPTH_FAIL}. */
    @JsxConstant
    public static final long STENCIL_PASS_DEPTH_FAIL = 2_965L;

    /** The constant {@code STENCIL_PASS_DEPTH_PASS}. */
    @JsxConstant
    public static final long STENCIL_PASS_DEPTH_PASS = 2_966L;

    /** The constant {@code STENCIL_REF}. */
    @JsxConstant
    public static final long STENCIL_REF = 2_967L;

    /** The constant {@code STENCIL_TEST}. */
    @JsxConstant
    public static final long STENCIL_TEST = 2_960L;

    /** The constant {@code STENCIL_VALUE_MASK}. */
    @JsxConstant
    public static final long STENCIL_VALUE_MASK = 2_963L;

    /** The constant {@code STENCIL_WRITEMASK}. */
    @JsxConstant
    public static final long STENCIL_WRITEMASK = 2_968L;

    /** The constant {@code STREAM_COPY}. */
    @JsxConstant
    public static final long STREAM_COPY = 35_042L;

    /** The constant {@code STREAM_DRAW}. */
    @JsxConstant
    public static final long STREAM_DRAW = 35_040L;

    /** The constant {@code STREAM_READ}. */
    @JsxConstant
    public static final long STREAM_READ = 35_041L;

    /** The constant {@code SUBPIXEL_BITS}. */
    @JsxConstant
    public static final long SUBPIXEL_BITS = 3_408L;

    /** The constant {@code SYNC_CONDITION}. */
    @JsxConstant
    public static final long SYNC_CONDITION = 37_139L;

    /** The constant {@code SYNC_FENCE}. */
    @JsxConstant
    public static final long SYNC_FENCE = 37_142L;

    /** The constant {@code SYNC_FLAGS}. */
    @JsxConstant
    public static final long SYNC_FLAGS = 37_141L;

    /** The constant {@code SYNC_FLUSH_COMMANDS_BIT}. */
    @JsxConstant
    public static final long SYNC_FLUSH_COMMANDS_BIT = 1L;

    /** The constant {@code SYNC_GPU_COMMANDS_COMPLETE}. */
    @JsxConstant
    public static final long SYNC_GPU_COMMANDS_COMPLETE = 37_143L;

    /** The constant {@code SYNC_STATUS}. */
    @JsxConstant
    public static final long SYNC_STATUS = 37_140L;

    /** The constant {@code TEXTURE}. */
    @JsxConstant
    public static final long TEXTURE = 5_890L;

    /** The constant {@code TEXTURE0}. */
    @JsxConstant
    public static final long TEXTURE0 = 33_984L;

    /** The constant {@code TEXTURE1}. */
    @JsxConstant
    public static final long TEXTURE1 = 33_985L;

    /** The constant {@code TEXTURE10}. */
    @JsxConstant
    public static final long TEXTURE10 = 33_994L;

    /** The constant {@code TEXTURE11}. */
    @JsxConstant
    public static final long TEXTURE11 = 33_995L;

    /** The constant {@code TEXTURE12}. */
    @JsxConstant
    public static final long TEXTURE12 = 33_996L;

    /** The constant {@code TEXTURE13}. */
    @JsxConstant
    public static final long TEXTURE13 = 33_997L;

    /** The constant {@code TEXTURE14}. */
    @JsxConstant
    public static final long TEXTURE14 = 33_998L;

    /** The constant {@code TEXTURE15}. */
    @JsxConstant
    public static final long TEXTURE15 = 33_999L;

    /** The constant {@code TEXTURE16}. */
    @JsxConstant
    public static final long TEXTURE16 = 34_000L;

    /** The constant {@code TEXTURE17}. */
    @JsxConstant
    public static final long TEXTURE17 = 34_001L;

    /** The constant {@code TEXTURE18}. */
    @JsxConstant
    public static final long TEXTURE18 = 34_002L;

    /** The constant {@code TEXTURE19}. */
    @JsxConstant
    public static final long TEXTURE19 = 34_003L;

    /** The constant {@code TEXTURE2}. */
    @JsxConstant
    public static final long TEXTURE2 = 33_986L;

    /** The constant {@code TEXTURE20}. */
    @JsxConstant
    public static final long TEXTURE20 = 34_004L;

    /** The constant {@code TEXTURE21}. */
    @JsxConstant
    public static final long TEXTURE21 = 34_005L;

    /** The constant {@code TEXTURE22}. */
    @JsxConstant
    public static final long TEXTURE22 = 34_006L;

    /** The constant {@code TEXTURE23}. */
    @JsxConstant
    public static final long TEXTURE23 = 34_007L;

    /** The constant {@code TEXTURE24}. */
    @JsxConstant
    public static final long TEXTURE24 = 34_008L;

    /** The constant {@code TEXTURE25}. */
    @JsxConstant
    public static final long TEXTURE25 = 34_009L;

    /** The constant {@code TEXTURE26}. */
    @JsxConstant
    public static final long TEXTURE26 = 34_010L;

    /** The constant {@code TEXTURE27}. */
    @JsxConstant
    public static final long TEXTURE27 = 34_011L;

    /** The constant {@code TEXTURE28}. */
    @JsxConstant
    public static final long TEXTURE28 = 34_012L;

    /** The constant {@code TEXTURE29}. */
    @JsxConstant
    public static final long TEXTURE29 = 34_013L;

    /** The constant {@code TEXTURE3}. */
    @JsxConstant
    public static final long TEXTURE3 = 33_987L;

    /** The constant {@code TEXTURE30}. */
    @JsxConstant
    public static final long TEXTURE30 = 34_014L;

    /** The constant {@code TEXTURE31}. */
    @JsxConstant
    public static final long TEXTURE31 = 34_015L;

    /** The constant {@code TEXTURE4}. */
    @JsxConstant
    public static final long TEXTURE4 = 33_988L;

    /** The constant {@code TEXTURE5}. */
    @JsxConstant
    public static final long TEXTURE5 = 33_989L;

    /** The constant {@code TEXTURE6}. */
    @JsxConstant
    public static final long TEXTURE6 = 33_990L;

    /** The constant {@code TEXTURE7}. */
    @JsxConstant
    public static final long TEXTURE7 = 33_991L;

    /** The constant {@code TEXTURE8}. */
    @JsxConstant
    public static final long TEXTURE8 = 33_992L;

    /** The constant {@code TEXTURE9}. */
    @JsxConstant
    public static final long TEXTURE9 = 33_993L;

    /** The constant {@code TEXTURE_2D}. */
    @JsxConstant
    public static final long TEXTURE_2D = 3_553L;

    /** The constant {@code TEXTURE_2D_ARRAY}. */
    @JsxConstant
    public static final long TEXTURE_2D_ARRAY = 35_866L;

    /** The constant {@code TEXTURE_3D}. */
    @JsxConstant
    public static final long TEXTURE_3D = 32_879L;

    /** The constant {@code TEXTURE_BASE_LEVEL}. */
    @JsxConstant
    public static final long TEXTURE_BASE_LEVEL = 33_084L;

    /** The constant {@code TEXTURE_BINDING_2D}. */
    @JsxConstant
    public static final long TEXTURE_BINDING_2D = 32_873L;

    /** The constant {@code TEXTURE_BINDING_2D_ARRAY}. */
    @JsxConstant
    public static final long TEXTURE_BINDING_2D_ARRAY = 35_869L;

    /** The constant {@code TEXTURE_BINDING_3D}. */
    @JsxConstant
    public static final long TEXTURE_BINDING_3D = 32_874L;

    /** The constant {@code TEXTURE_BINDING_CUBE_MAP}. */
    @JsxConstant
    public static final long TEXTURE_BINDING_CUBE_MAP = 34_068L;

    /** The constant {@code TEXTURE_COMPARE_FUNC}. */
    @JsxConstant
    public static final long TEXTURE_COMPARE_FUNC = 34_893L;

    /** The constant {@code TEXTURE_COMPARE_MODE}. */
    @JsxConstant
    public static final long TEXTURE_COMPARE_MODE = 34_892L;

    /** The constant {@code TEXTURE_CUBE_MAP}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP = 34_067L;

    /** The constant {@code TEXTURE_CUBE_MAP_NEGATIVE_X}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_NEGATIVE_X = 34_070L;

    /** The constant {@code TEXTURE_CUBE_MAP_NEGATIVE_Y}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_NEGATIVE_Y = 34_072L;

    /** The constant {@code TEXTURE_CUBE_MAP_NEGATIVE_Z}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_NEGATIVE_Z = 34_074L;

    /** The constant {@code TEXTURE_CUBE_MAP_POSITIVE_X}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_POSITIVE_X = 34_069L;

    /** The constant {@code TEXTURE_CUBE_MAP_POSITIVE_Y}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_POSITIVE_Y = 34_071L;

    /** The constant {@code TEXTURE_CUBE_MAP_POSITIVE_Z}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_POSITIVE_Z = 34_073L;

    /** The constant {@code TEXTURE_IMMUTABLE_FORMAT}. */
    @JsxConstant
    public static final long TEXTURE_IMMUTABLE_FORMAT = 37_167L;

    /** The constant {@code TEXTURE_IMMUTABLE_LEVELS}. */
    @JsxConstant
    public static final long TEXTURE_IMMUTABLE_LEVELS = 33_503L;

    /** The constant {@code TEXTURE_MAG_FILTER}. */
    @JsxConstant
    public static final long TEXTURE_MAG_FILTER = 10_240L;

    /** The constant {@code TEXTURE_MAX_LEVEL}. */
    @JsxConstant
    public static final long TEXTURE_MAX_LEVEL = 33_085L;

    /** The constant {@code TEXTURE_MAX_LOD}. */
    @JsxConstant
    public static final long TEXTURE_MAX_LOD = 33_083L;

    /** The constant {@code TEXTURE_MIN_FILTER}. */
    @JsxConstant
    public static final long TEXTURE_MIN_FILTER = 10_241L;

    /** The constant {@code TEXTURE_MIN_LOD}. */
    @JsxConstant
    public static final long TEXTURE_MIN_LOD = 33_082L;

    /** The constant {@code TEXTURE_WRAP_R}. */
    @JsxConstant
    public static final long TEXTURE_WRAP_R = 32_882L;

    /** The constant {@code TEXTURE_WRAP_S}. */
    @JsxConstant
    public static final long TEXTURE_WRAP_S = 10_242L;

    /** The constant {@code TEXTURE_WRAP_T}. */
    @JsxConstant
    public static final long TEXTURE_WRAP_T = 10_243L;

    /** The constant {@code TIMEOUT_EXPIRED}. */
    @JsxConstant
    public static final long TIMEOUT_EXPIRED = 37_147L;

    /** The constant {@code TIMEOUT_IGNORED}. */
    @JsxConstant
    public static final long TIMEOUT_IGNORED = -1L;

    /** The constant {@code TRANSFORM_FEEDBACK}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK = 36_386L;

    /** The constant {@code TRANSFORM_FEEDBACK_ACTIVE}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_ACTIVE = 36_388L;

    /** The constant {@code TRANSFORM_FEEDBACK_BINDING}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BINDING = 36_389L;

    /** The constant {@code TRANSFORM_FEEDBACK_BUFFER}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BUFFER = 35_982L;

    /** The constant {@code TRANSFORM_FEEDBACK_BUFFER_BINDING}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BUFFER_BINDING = 35_983L;

    /** The constant {@code TRANSFORM_FEEDBACK_BUFFER_MODE}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BUFFER_MODE = 35_967L;

    /** The constant {@code TRANSFORM_FEEDBACK_BUFFER_SIZE}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BUFFER_SIZE = 35_973L;

    /** The constant {@code TRANSFORM_FEEDBACK_BUFFER_START}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BUFFER_START = 35_972L;

    /** The constant {@code TRANSFORM_FEEDBACK_PAUSED}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_PAUSED = 36_387L;

    /** The constant {@code TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 35_976L;

    /** The constant {@code TRANSFORM_FEEDBACK_VARYINGS}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_VARYINGS = 35_971L;

    /** The constant {@code TRIANGLES}. */
    @JsxConstant
    public static final long TRIANGLES = 4L;

    /** The constant {@code TRIANGLE_FAN}. */
    @JsxConstant
    public static final long TRIANGLE_FAN = 6L;

    /** The constant {@code TRIANGLE_STRIP}. */
    @JsxConstant
    public static final long TRIANGLE_STRIP = 5L;

    /** The constant {@code UNIFORM_ARRAY_STRIDE}. */
    @JsxConstant
    public static final long UNIFORM_ARRAY_STRIDE = 35_388L;

    /** The constant {@code UNIFORM_BLOCK_ACTIVE_UNIFORMS}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_ACTIVE_UNIFORMS = 35_394L;

    /** The constant {@code UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 35_395L;

    /** The constant {@code UNIFORM_BLOCK_BINDING}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_BINDING = 35_391L;

    /** The constant {@code UNIFORM_BLOCK_DATA_SIZE}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_DATA_SIZE = 35_392L;

    /** The constant {@code UNIFORM_BLOCK_INDEX}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_INDEX = 35_386L;

    /** The constant {@code UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 35_398L;

    /** The constant {@code UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 35_396L;

    /** The constant {@code UNIFORM_BUFFER}. */
    @JsxConstant
    public static final long UNIFORM_BUFFER = 35_345L;

    /** The constant {@code UNIFORM_BUFFER_BINDING}. */
    @JsxConstant
    public static final long UNIFORM_BUFFER_BINDING = 35_368L;

    /** The constant {@code UNIFORM_BUFFER_OFFSET_ALIGNMENT}. */
    @JsxConstant
    public static final long UNIFORM_BUFFER_OFFSET_ALIGNMENT = 35_380L;

    /** The constant {@code UNIFORM_BUFFER_SIZE}. */
    @JsxConstant
    public static final long UNIFORM_BUFFER_SIZE = 35_370L;

    /** The constant {@code UNIFORM_BUFFER_START}. */
    @JsxConstant
    public static final long UNIFORM_BUFFER_START = 35_369L;

    /** The constant {@code UNIFORM_IS_ROW_MAJOR}. */
    @JsxConstant
    public static final long UNIFORM_IS_ROW_MAJOR = 35_390L;

    /** The constant {@code UNIFORM_MATRIX_STRIDE}. */
    @JsxConstant
    public static final long UNIFORM_MATRIX_STRIDE = 35_389L;

    /** The constant {@code UNIFORM_OFFSET}. */
    @JsxConstant
    public static final long UNIFORM_OFFSET = 35_387L;

    /** The constant {@code UNIFORM_SIZE}. */
    @JsxConstant
    public static final long UNIFORM_SIZE = 35_384L;

    /** The constant {@code UNIFORM_TYPE}. */
    @JsxConstant
    public static final long UNIFORM_TYPE = 35_383L;

    /** The constant {@code UNPACK_ALIGNMENT}. */
    @JsxConstant
    public static final long UNPACK_ALIGNMENT = 3_317L;

    /** The constant {@code UNPACK_COLORSPACE_CONVERSION_WEBGL}. */
    @JsxConstant
    public static final long UNPACK_COLORSPACE_CONVERSION_WEBGL = 37_443L;

    /** The constant {@code UNPACK_FLIP_Y_WEBGL}. */
    @JsxConstant
    public static final long UNPACK_FLIP_Y_WEBGL = 37_440L;

    /** The constant {@code UNPACK_IMAGE_HEIGHT}. */
    @JsxConstant
    public static final long UNPACK_IMAGE_HEIGHT = 32_878L;

    /** The constant {@code UNPACK_PREMULTIPLY_ALPHA_WEBGL}. */
    @JsxConstant
    public static final long UNPACK_PREMULTIPLY_ALPHA_WEBGL = 37_441L;

    /** The constant {@code UNPACK_ROW_LENGTH}. */
    @JsxConstant
    public static final long UNPACK_ROW_LENGTH = 3_314L;

    /** The constant {@code UNPACK_SKIP_IMAGES}. */
    @JsxConstant
    public static final long UNPACK_SKIP_IMAGES = 32_877L;

    /** The constant {@code UNPACK_SKIP_PIXELS}. */
    @JsxConstant
    public static final long UNPACK_SKIP_PIXELS = 3_316L;

    /** The constant {@code UNPACK_SKIP_ROWS}. */
    @JsxConstant
    public static final long UNPACK_SKIP_ROWS = 3_315L;

    /** The constant {@code UNSIGNALED}. */
    @JsxConstant
    public static final long UNSIGNALED = 37_144L;

    /** The constant {@code UNSIGNED_BYTE}. */
    @JsxConstant
    public static final long UNSIGNED_BYTE = 5_121L;

    /** The constant {@code UNSIGNED_INT}. */
    @JsxConstant
    public static final long UNSIGNED_INT = 5_125L;

    /** The constant {@code UNSIGNED_INT_10F_11F_11F_REV}. */
    @JsxConstant
    public static final long UNSIGNED_INT_10F_11F_11F_REV = 35_899L;

    /** The constant {@code UNSIGNED_INT_24_8}. */
    @JsxConstant
    public static final long UNSIGNED_INT_24_8 = 34_042L;

    /** The constant {@code UNSIGNED_INT_2_10_10_10_REV}. */
    @JsxConstant
    public static final long UNSIGNED_INT_2_10_10_10_REV = 33_640L;

    /** The constant {@code UNSIGNED_INT_5_9_9_9_REV}. */
    @JsxConstant
    public static final long UNSIGNED_INT_5_9_9_9_REV = 35_902L;

    /** The constant {@code UNSIGNED_INT_SAMPLER_2D}. */
    @JsxConstant
    public static final long UNSIGNED_INT_SAMPLER_2D = 36_306L;

    /** The constant {@code UNSIGNED_INT_SAMPLER_2D_ARRAY}. */
    @JsxConstant
    public static final long UNSIGNED_INT_SAMPLER_2D_ARRAY = 36_311L;

    /** The constant {@code UNSIGNED_INT_SAMPLER_3D}. */
    @JsxConstant
    public static final long UNSIGNED_INT_SAMPLER_3D = 36_307L;

    /** The constant {@code UNSIGNED_INT_SAMPLER_CUBE}. */
    @JsxConstant
    public static final long UNSIGNED_INT_SAMPLER_CUBE = 36_308L;

    /** The constant {@code UNSIGNED_INT_VEC2}. */
    @JsxConstant
    public static final long UNSIGNED_INT_VEC2 = 36_294L;

    /** The constant {@code UNSIGNED_INT_VEC3}. */
    @JsxConstant
    public static final long UNSIGNED_INT_VEC3 = 36_295L;

    /** The constant {@code UNSIGNED_INT_VEC4}. */
    @JsxConstant
    public static final long UNSIGNED_INT_VEC4 = 36_296L;

    /** The constant {@code UNSIGNED_NORMALIZED}. */
    @JsxConstant
    public static final long UNSIGNED_NORMALIZED = 35_863L;

    /** The constant {@code UNSIGNED_SHORT}. */
    @JsxConstant
    public static final long UNSIGNED_SHORT = 5_123L;

    /** The constant {@code UNSIGNED_SHORT_4_4_4_4}. */
    @JsxConstant
    public static final long UNSIGNED_SHORT_4_4_4_4 = 32_819L;

    /** The constant {@code UNSIGNED_SHORT_5_5_5_1}. */
    @JsxConstant
    public static final long UNSIGNED_SHORT_5_5_5_1 = 32_820L;

    /** The constant {@code UNSIGNED_SHORT_5_6_5}. */
    @JsxConstant
    public static final long UNSIGNED_SHORT_5_6_5 = 33_635L;

    /** The constant {@code VALIDATE_STATUS}. */
    @JsxConstant
    public static final long VALIDATE_STATUS = 35_715L;

    /** The constant {@code VENDOR}. */
    @JsxConstant
    public static final long VENDOR = 7_936L;

    /** The constant {@code VERSION}. */
    @JsxConstant
    public static final long VERSION = 7_938L;

    /** The constant {@code VERTEX_ARRAY_BINDING}. */
    @JsxConstant
    public static final long VERTEX_ARRAY_BINDING = 34_229L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_BUFFER_BINDING}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 34_975L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_DIVISOR}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_DIVISOR = 35_070L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_ENABLED}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_ENABLED = 34_338L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_INTEGER}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_INTEGER = 35_069L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_NORMALIZED}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_NORMALIZED = 34_922L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_POINTER}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_POINTER = 34_373L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_SIZE}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_SIZE = 34_339L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_STRIDE}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_STRIDE = 34_340L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_TYPE}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_TYPE = 34_341L;

    /** The constant {@code VERTEX_SHADER}. */
    @JsxConstant
    public static final long VERTEX_SHADER = 35_633L;

    /** The constant {@code VIEWPORT}. */
    @JsxConstant
    public static final long VIEWPORT = 2_978L;

    /** The constant {@code WAIT_FAILED}. */
    @JsxConstant
    public static final long WAIT_FAILED = 37_149L;

    /** The constant {@code ZERO}. */
    @JsxConstant
    public static final long ZERO = 0L;

    /**
     * Default constructor.
     */
    @JsxConstructor
    public WebGL2RenderingContext() {
    }
}
