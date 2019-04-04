/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF52;
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
@JsxClass({CHROME, FF})
public class WebGL2RenderingContext extends SimpleScriptable {

    /** The constant {@code ACTIVE_ATTRIBUTES}. */
    @JsxConstant
    public static final long ACTIVE_ATTRIBUTES = 35721L;

    /** The constant {@code ACTIVE_TEXTURE}. */
    @JsxConstant
    public static final long ACTIVE_TEXTURE = 34016L;

    /** The constant {@code ACTIVE_UNIFORMS}. */
    @JsxConstant
    public static final long ACTIVE_UNIFORMS = 35718L;

    /** The constant {@code ACTIVE_UNIFORM_BLOCKS}. */
    @JsxConstant
    public static final long ACTIVE_UNIFORM_BLOCKS = 35382L;

    /** The constant {@code ALIASED_LINE_WIDTH_RANGE}. */
    @JsxConstant
    public static final long ALIASED_LINE_WIDTH_RANGE = 33902L;

    /** The constant {@code ALIASED_POINT_SIZE_RANGE}. */
    @JsxConstant
    public static final long ALIASED_POINT_SIZE_RANGE = 33901L;

    /** The constant {@code ALPHA}. */
    @JsxConstant
    public static final long ALPHA = 6406L;

    /** The constant {@code ALPHA_BITS}. */
    @JsxConstant
    public static final long ALPHA_BITS = 3413L;

    /** The constant {@code ALREADY_SIGNALED}. */
    @JsxConstant
    public static final long ALREADY_SIGNALED = 37146L;

    /** The constant {@code ALWAYS}. */
    @JsxConstant
    public static final long ALWAYS = 519L;

    /** The constant {@code ANY_SAMPLES_PASSED}. */
    @JsxConstant
    public static final long ANY_SAMPLES_PASSED = 35887L;

    /** The constant {@code ANY_SAMPLES_PASSED_CONSERVATIVE}. */
    @JsxConstant
    public static final long ANY_SAMPLES_PASSED_CONSERVATIVE = 36202L;

    /** The constant {@code ARRAY_BUFFER}. */
    @JsxConstant
    public static final long ARRAY_BUFFER = 34962L;

    /** The constant {@code ARRAY_BUFFER_BINDING}. */
    @JsxConstant
    public static final long ARRAY_BUFFER_BINDING = 34964L;

    /** The constant {@code ATTACHED_SHADERS}. */
    @JsxConstant
    public static final long ATTACHED_SHADERS = 35717L;

    /** The constant {@code BACK}. */
    @JsxConstant
    public static final long BACK = 1029L;

    /** The constant {@code BLEND}. */
    @JsxConstant
    public static final long BLEND = 3042L;

    /** The constant {@code BLEND_COLOR}. */
    @JsxConstant
    public static final long BLEND_COLOR = 32773L;

    /** The constant {@code BLEND_DST_ALPHA}. */
    @JsxConstant
    public static final long BLEND_DST_ALPHA = 32970L;

    /** The constant {@code BLEND_DST_RGB}. */
    @JsxConstant
    public static final long BLEND_DST_RGB = 32968L;

    /** The constant {@code BLEND_EQUATION}. */
    @JsxConstant
    public static final long BLEND_EQUATION = 32777L;

    /** The constant {@code BLEND_EQUATION_ALPHA}. */
    @JsxConstant
    public static final long BLEND_EQUATION_ALPHA = 34877L;

    /** The constant {@code BLEND_EQUATION_RGB}. */
    @JsxConstant
    public static final long BLEND_EQUATION_RGB = 32777L;

    /** The constant {@code BLEND_SRC_ALPHA}. */
    @JsxConstant
    public static final long BLEND_SRC_ALPHA = 32971L;

    /** The constant {@code BLEND_SRC_RGB}. */
    @JsxConstant
    public static final long BLEND_SRC_RGB = 32969L;

    /** The constant {@code BLUE_BITS}. */
    @JsxConstant
    public static final long BLUE_BITS = 3412L;

    /** The constant {@code BOOL}. */
    @JsxConstant
    public static final long BOOL = 35670L;

    /** The constant {@code BOOL_VEC2}. */
    @JsxConstant
    public static final long BOOL_VEC2 = 35671L;

    /** The constant {@code BOOL_VEC3}. */
    @JsxConstant
    public static final long BOOL_VEC3 = 35672L;

    /** The constant {@code BOOL_VEC4}. */
    @JsxConstant
    public static final long BOOL_VEC4 = 35673L;

    /** The constant {@code BROWSER_DEFAULT_WEBGL}. */
    @JsxConstant
    public static final long BROWSER_DEFAULT_WEBGL = 37444L;

    /** The constant {@code BUFFER_SIZE}. */
    @JsxConstant
    public static final long BUFFER_SIZE = 34660L;

    /** The constant {@code BUFFER_USAGE}. */
    @JsxConstant
    public static final long BUFFER_USAGE = 34661L;

    /** The constant {@code BYTE}. */
    @JsxConstant
    public static final long BYTE = 5120L;

    /** The constant {@code CCW}. */
    @JsxConstant
    public static final long CCW = 2305L;

    /** The constant {@code CLAMP_TO_EDGE}. */
    @JsxConstant
    public static final long CLAMP_TO_EDGE = 33071L;

    /** The constant {@code COLOR}. */
    @JsxConstant
    public static final long COLOR = 6144L;

    /** The constant {@code COLOR_ATTACHMENT0}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT0 = 36064L;

    /** The constant {@code COLOR_ATTACHMENT1}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT1 = 36065L;

    /** The constant {@code COLOR_ATTACHMENT10}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT10 = 36074L;

    /** The constant {@code COLOR_ATTACHMENT11}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT11 = 36075L;

    /** The constant {@code COLOR_ATTACHMENT12}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT12 = 36076L;

    /** The constant {@code COLOR_ATTACHMENT13}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT13 = 36077L;

    /** The constant {@code COLOR_ATTACHMENT14}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT14 = 36078L;

    /** The constant {@code COLOR_ATTACHMENT15}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT15 = 36079L;

    /** The constant {@code COLOR_ATTACHMENT2}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT2 = 36066L;

    /** The constant {@code COLOR_ATTACHMENT3}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT3 = 36067L;

    /** The constant {@code COLOR_ATTACHMENT4}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT4 = 36068L;

    /** The constant {@code COLOR_ATTACHMENT5}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT5 = 36069L;

    /** The constant {@code COLOR_ATTACHMENT6}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT6 = 36070L;

    /** The constant {@code COLOR_ATTACHMENT7}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT7 = 36071L;

    /** The constant {@code COLOR_ATTACHMENT8}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT8 = 36072L;

    /** The constant {@code COLOR_ATTACHMENT9}. */
    @JsxConstant
    public static final long COLOR_ATTACHMENT9 = 36073L;

    /** The constant {@code COLOR_BUFFER_BIT}. */
    @JsxConstant
    public static final long COLOR_BUFFER_BIT = 16384L;

    /** The constant {@code COLOR_CLEAR_VALUE}. */
    @JsxConstant
    public static final long COLOR_CLEAR_VALUE = 3106L;

    /** The constant {@code COLOR_WRITEMASK}. */
    @JsxConstant
    public static final long COLOR_WRITEMASK = 3107L;

    /** The constant {@code COMPARE_REF_TO_TEXTURE}. */
    @JsxConstant
    public static final long COMPARE_REF_TO_TEXTURE = 34894L;

    /** The constant {@code COMPILE_STATUS}. */
    @JsxConstant
    public static final long COMPILE_STATUS = 35713L;

    /** The constant {@code COMPRESSED_TEXTURE_FORMATS}. */
    @JsxConstant
    public static final long COMPRESSED_TEXTURE_FORMATS = 34467L;

    /** The constant {@code CONDITION_SATISFIED}. */
    @JsxConstant
    public static final long CONDITION_SATISFIED = 37148L;

    /** The constant {@code CONSTANT_ALPHA}. */
    @JsxConstant
    public static final long CONSTANT_ALPHA = 32771L;

    /** The constant {@code CONSTANT_COLOR}. */
    @JsxConstant
    public static final long CONSTANT_COLOR = 32769L;

    /** The constant {@code CONTEXT_LOST_WEBGL}. */
    @JsxConstant
    public static final long CONTEXT_LOST_WEBGL = 37442L;

    /** The constant {@code COPY_READ_BUFFER}. */
    @JsxConstant
    public static final long COPY_READ_BUFFER = 36662L;

    /** The constant {@code COPY_READ_BUFFER_BINDING}. */
    @JsxConstant
    public static final long COPY_READ_BUFFER_BINDING = 36662L;

    /** The constant {@code COPY_WRITE_BUFFER}. */
    @JsxConstant
    public static final long COPY_WRITE_BUFFER = 36663L;

    /** The constant {@code COPY_WRITE_BUFFER_BINDING}. */
    @JsxConstant
    public static final long COPY_WRITE_BUFFER_BINDING = 36663L;

    /** The constant {@code CULL_FACE}. */
    @JsxConstant
    public static final long CULL_FACE = 2884L;

    /** The constant {@code CULL_FACE_MODE}. */
    @JsxConstant
    public static final long CULL_FACE_MODE = 2885L;

    /** The constant {@code CURRENT_PROGRAM}. */
    @JsxConstant
    public static final long CURRENT_PROGRAM = 35725L;

    /** The constant {@code CURRENT_QUERY}. */
    @JsxConstant
    public static final long CURRENT_QUERY = 34917L;

    /** The constant {@code CURRENT_VERTEX_ATTRIB}. */
    @JsxConstant
    public static final long CURRENT_VERTEX_ATTRIB = 34342L;

    /** The constant {@code CW}. */
    @JsxConstant
    public static final long CW = 2304L;

    /** The constant {@code DECR}. */
    @JsxConstant
    public static final long DECR = 7683L;

    /** The constant {@code DECR_WRAP}. */
    @JsxConstant
    public static final long DECR_WRAP = 34056L;

    /** The constant {@code DELETE_STATUS}. */
    @JsxConstant
    public static final long DELETE_STATUS = 35712L;

    /** The constant {@code DEPTH}. */
    @JsxConstant
    public static final long DEPTH = 6145L;

    /** The constant {@code DEPTH24_STENCIL8}. */
    @JsxConstant
    public static final long DEPTH24_STENCIL8 = 35056L;

    /** The constant {@code DEPTH32F_STENCIL8}. */
    @JsxConstant
    public static final long DEPTH32F_STENCIL8 = 36013L;

    /** The constant {@code DEPTH_ATTACHMENT}. */
    @JsxConstant
    public static final long DEPTH_ATTACHMENT = 36096L;

    /** The constant {@code DEPTH_BITS}. */
    @JsxConstant
    public static final long DEPTH_BITS = 3414L;

    /** The constant {@code DEPTH_BUFFER_BIT}. */
    @JsxConstant
    public static final long DEPTH_BUFFER_BIT = 256L;

    /** The constant {@code DEPTH_CLEAR_VALUE}. */
    @JsxConstant
    public static final long DEPTH_CLEAR_VALUE = 2931L;

    /** The constant {@code DEPTH_COMPONENT}. */
    @JsxConstant
    public static final long DEPTH_COMPONENT = 6402L;

    /** The constant {@code DEPTH_COMPONENT16}. */
    @JsxConstant
    public static final long DEPTH_COMPONENT16 = 33189L;

    /** The constant {@code DEPTH_COMPONENT24}. */
    @JsxConstant
    public static final long DEPTH_COMPONENT24 = 33190L;

    /** The constant {@code DEPTH_COMPONENT32F}. */
    @JsxConstant
    public static final long DEPTH_COMPONENT32F = 36012L;

    /** The constant {@code DEPTH_FUNC}. */
    @JsxConstant
    public static final long DEPTH_FUNC = 2932L;

    /** The constant {@code DEPTH_RANGE}. */
    @JsxConstant
    public static final long DEPTH_RANGE = 2928L;

    /** The constant {@code DEPTH_STENCIL}. */
    @JsxConstant
    public static final long DEPTH_STENCIL = 34041L;

    /** The constant {@code DEPTH_STENCIL_ATTACHMENT}. */
    @JsxConstant
    public static final long DEPTH_STENCIL_ATTACHMENT = 33306L;

    /** The constant {@code DEPTH_TEST}. */
    @JsxConstant
    public static final long DEPTH_TEST = 2929L;

    /** The constant {@code DEPTH_WRITEMASK}. */
    @JsxConstant
    public static final long DEPTH_WRITEMASK = 2930L;

    /** The constant {@code DITHER}. */
    @JsxConstant
    public static final long DITHER = 3024L;

    /** The constant {@code DONT_CARE}. */
    @JsxConstant
    public static final long DONT_CARE = 4352L;

    /** The constant {@code DRAW_BUFFER0}. */
    @JsxConstant
    public static final long DRAW_BUFFER0 = 34853L;

    /** The constant {@code DRAW_BUFFER1}. */
    @JsxConstant
    public static final long DRAW_BUFFER1 = 34854L;

    /** The constant {@code DRAW_BUFFER10}. */
    @JsxConstant
    public static final long DRAW_BUFFER10 = 34863L;

    /** The constant {@code DRAW_BUFFER11}. */
    @JsxConstant
    public static final long DRAW_BUFFER11 = 34864L;

    /** The constant {@code DRAW_BUFFER12}. */
    @JsxConstant
    public static final long DRAW_BUFFER12 = 34865L;

    /** The constant {@code DRAW_BUFFER13}. */
    @JsxConstant
    public static final long DRAW_BUFFER13 = 34866L;

    /** The constant {@code DRAW_BUFFER14}. */
    @JsxConstant
    public static final long DRAW_BUFFER14 = 34867L;

    /** The constant {@code DRAW_BUFFER15}. */
    @JsxConstant
    public static final long DRAW_BUFFER15 = 34868L;

    /** The constant {@code DRAW_BUFFER2}. */
    @JsxConstant
    public static final long DRAW_BUFFER2 = 34855L;

    /** The constant {@code DRAW_BUFFER3}. */
    @JsxConstant
    public static final long DRAW_BUFFER3 = 34856L;

    /** The constant {@code DRAW_BUFFER4}. */
    @JsxConstant
    public static final long DRAW_BUFFER4 = 34857L;

    /** The constant {@code DRAW_BUFFER5}. */
    @JsxConstant
    public static final long DRAW_BUFFER5 = 34858L;

    /** The constant {@code DRAW_BUFFER6}. */
    @JsxConstant
    public static final long DRAW_BUFFER6 = 34859L;

    /** The constant {@code DRAW_BUFFER7}. */
    @JsxConstant
    public static final long DRAW_BUFFER7 = 34860L;

    /** The constant {@code DRAW_BUFFER8}. */
    @JsxConstant
    public static final long DRAW_BUFFER8 = 34861L;

    /** The constant {@code DRAW_BUFFER9}. */
    @JsxConstant
    public static final long DRAW_BUFFER9 = 34862L;

    /** The constant {@code DRAW_FRAMEBUFFER}. */
    @JsxConstant
    public static final long DRAW_FRAMEBUFFER = 36009L;

    /** The constant {@code DRAW_FRAMEBUFFER_BINDING}. */
    @JsxConstant
    public static final long DRAW_FRAMEBUFFER_BINDING = 36006L;

    /** The constant {@code DST_ALPHA}. */
    @JsxConstant
    public static final long DST_ALPHA = 772L;

    /** The constant {@code DST_COLOR}. */
    @JsxConstant
    public static final long DST_COLOR = 774L;

    /** The constant {@code DYNAMIC_COPY}. */
    @JsxConstant
    public static final long DYNAMIC_COPY = 35050L;

    /** The constant {@code DYNAMIC_DRAW}. */
    @JsxConstant
    public static final long DYNAMIC_DRAW = 35048L;

    /** The constant {@code DYNAMIC_READ}. */
    @JsxConstant
    public static final long DYNAMIC_READ = 35049L;

    /** The constant {@code ELEMENT_ARRAY_BUFFER}. */
    @JsxConstant
    public static final long ELEMENT_ARRAY_BUFFER = 34963L;

    /** The constant {@code ELEMENT_ARRAY_BUFFER_BINDING}. */
    @JsxConstant
    public static final long ELEMENT_ARRAY_BUFFER_BINDING = 34965L;

    /** The constant {@code EQUAL}. */
    @JsxConstant
    public static final long EQUAL = 514L;

    /** The constant {@code FASTEST}. */
    @JsxConstant
    public static final long FASTEST = 4353L;

    /** The constant {@code FLOAT}. */
    @JsxConstant
    public static final long FLOAT = 5126L;

    /** The constant {@code FLOAT_32_UNSIGNED_INT_24_8_REV}. */
    @JsxConstant
    public static final long FLOAT_32_UNSIGNED_INT_24_8_REV = 36269L;

    /** The constant {@code FLOAT_MAT2}. */
    @JsxConstant
    public static final long FLOAT_MAT2 = 35674L;

    /** The constant {@code FLOAT_MAT2x3}. */
    @JsxConstant
    public static final long FLOAT_MAT2x3 = 35685L;

    /** The constant {@code FLOAT_MAT2x4}. */
    @JsxConstant
    public static final long FLOAT_MAT2x4 = 35686L;

    /** The constant {@code FLOAT_MAT3}. */
    @JsxConstant
    public static final long FLOAT_MAT3 = 35675L;

    /** The constant {@code FLOAT_MAT3x2}. */
    @JsxConstant
    public static final long FLOAT_MAT3x2 = 35687L;

    /** The constant {@code FLOAT_MAT3x4}. */
    @JsxConstant
    public static final long FLOAT_MAT3x4 = 35688L;

    /** The constant {@code FLOAT_MAT4}. */
    @JsxConstant
    public static final long FLOAT_MAT4 = 35676L;

    /** The constant {@code FLOAT_MAT4x2}. */
    @JsxConstant
    public static final long FLOAT_MAT4x2 = 35689L;

    /** The constant {@code FLOAT_MAT4x3}. */
    @JsxConstant
    public static final long FLOAT_MAT4x3 = 35690L;

    /** The constant {@code FLOAT_VEC2}. */
    @JsxConstant
    public static final long FLOAT_VEC2 = 35664L;

    /** The constant {@code FLOAT_VEC3}. */
    @JsxConstant
    public static final long FLOAT_VEC3 = 35665L;

    /** The constant {@code FLOAT_VEC4}. */
    @JsxConstant
    public static final long FLOAT_VEC4 = 35666L;

    /** The constant {@code FRAGMENT_SHADER}. */
    @JsxConstant
    public static final long FRAGMENT_SHADER = 35632L;

    /** The constant {@code FRAGMENT_SHADER_DERIVATIVE_HINT}. */
    @JsxConstant
    public static final long FRAGMENT_SHADER_DERIVATIVE_HINT = 35723L;

    /** The constant {@code FRAMEBUFFER}. */
    @JsxConstant
    public static final long FRAMEBUFFER = 36160L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 33301L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_BLUE_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 33300L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 33296L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 33297L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 33302L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_GREEN_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 33299L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_OBJECT_NAME}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 36049L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 36048L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_RED_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_RED_SIZE = 33298L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 33303L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 36051L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 36052L;

    /** The constant {@code FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL}. */
    @JsxConstant
    public static final long FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 36050L;

    /** The constant {@code FRAMEBUFFER_BINDING}. */
    @JsxConstant
    public static final long FRAMEBUFFER_BINDING = 36006L;

    /** The constant {@code FRAMEBUFFER_COMPLETE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_COMPLETE = 36053L;

    /** The constant {@code FRAMEBUFFER_DEFAULT}. */
    @JsxConstant
    public static final long FRAMEBUFFER_DEFAULT = 33304L;

    /** The constant {@code FRAMEBUFFER_INCOMPLETE_ATTACHMENT}. */
    @JsxConstant
    public static final long FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054L;

    /** The constant {@code FRAMEBUFFER_INCOMPLETE_DIMENSIONS}. */
    @JsxConstant
    public static final long FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 36057L;

    /** The constant {@code FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT}. */
    @JsxConstant
    public static final long FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055L;

    /** The constant {@code FRAMEBUFFER_INCOMPLETE_MULTISAMPLE}. */
    @JsxConstant
    public static final long FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 36182L;

    /** The constant {@code FRAMEBUFFER_UNSUPPORTED}. */
    @JsxConstant
    public static final long FRAMEBUFFER_UNSUPPORTED = 36061L;

    /** The constant {@code FRONT}. */
    @JsxConstant
    public static final long FRONT = 1028L;

    /** The constant {@code FRONT_AND_BACK}. */
    @JsxConstant
    public static final long FRONT_AND_BACK = 1032L;

    /** The constant {@code FRONT_FACE}. */
    @JsxConstant
    public static final long FRONT_FACE = 2886L;

    /** The constant {@code FUNC_ADD}. */
    @JsxConstant
    public static final long FUNC_ADD = 32774L;

    /** The constant {@code FUNC_REVERSE_SUBTRACT}. */
    @JsxConstant
    public static final long FUNC_REVERSE_SUBTRACT = 32779L;

    /** The constant {@code FUNC_SUBTRACT}. */
    @JsxConstant
    public static final long FUNC_SUBTRACT = 32778L;

    /** The constant {@code GENERATE_MIPMAP_HINT}. */
    @JsxConstant
    public static final long GENERATE_MIPMAP_HINT = 33170L;

    /** The constant {@code GEQUAL}. */
    @JsxConstant
    public static final long GEQUAL = 518L;

    /** The constant {@code GREATER}. */
    @JsxConstant
    public static final long GREATER = 516L;

    /** The constant {@code GREEN_BITS}. */
    @JsxConstant
    public static final long GREEN_BITS = 3411L;

    /** The constant {@code HALF_FLOAT}. */
    @JsxConstant
    public static final long HALF_FLOAT = 5131L;

    /** The constant {@code HIGH_FLOAT}. */
    @JsxConstant
    public static final long HIGH_FLOAT = 36338L;

    /** The constant {@code HIGH_INT}. */
    @JsxConstant
    public static final long HIGH_INT = 36341L;

    /** The constant {@code IMPLEMENTATION_COLOR_READ_FORMAT}. */
    @JsxConstant
    public static final long IMPLEMENTATION_COLOR_READ_FORMAT = 35739L;

    /** The constant {@code IMPLEMENTATION_COLOR_READ_TYPE}. */
    @JsxConstant
    public static final long IMPLEMENTATION_COLOR_READ_TYPE = 35738L;

    /** The constant {@code INCR}. */
    @JsxConstant
    public static final long INCR = 7682L;

    /** The constant {@code INCR_WRAP}. */
    @JsxConstant
    public static final long INCR_WRAP = 34055L;

    /** The constant {@code INT}. */
    @JsxConstant
    public static final long INT = 5124L;

    /** The constant {@code INTERLEAVED_ATTRIBS}. */
    @JsxConstant
    public static final long INTERLEAVED_ATTRIBS = 35980L;

    /** The constant {@code INT_2_10_10_10_REV}. */
    @JsxConstant
    public static final long INT_2_10_10_10_REV = 36255L;

    /** The constant {@code INT_SAMPLER_2D}. */
    @JsxConstant
    public static final long INT_SAMPLER_2D = 36298L;

    /** The constant {@code INT_SAMPLER_2D_ARRAY}. */
    @JsxConstant
    public static final long INT_SAMPLER_2D_ARRAY = 36303L;

    /** The constant {@code INT_SAMPLER_3D}. */
    @JsxConstant
    public static final long INT_SAMPLER_3D = 36299L;

    /** The constant {@code INT_SAMPLER_CUBE}. */
    @JsxConstant
    public static final long INT_SAMPLER_CUBE = 36300L;

    /** The constant {@code INT_VEC2}. */
    @JsxConstant
    public static final long INT_VEC2 = 35667L;

    /** The constant {@code INT_VEC3}. */
    @JsxConstant
    public static final long INT_VEC3 = 35668L;

    /** The constant {@code INT_VEC4}. */
    @JsxConstant
    public static final long INT_VEC4 = 35669L;

    /** The constant {@code INVALID_ENUM}. */
    @JsxConstant
    public static final long INVALID_ENUM = 1280L;

    /** The constant {@code INVALID_FRAMEBUFFER_OPERATION}. */
    @JsxConstant
    public static final long INVALID_FRAMEBUFFER_OPERATION = 1286L;

    /** The constant {@code INVALID_INDEX}. */
    @JsxConstant
    public static final long INVALID_INDEX = 4294967295L;

    /** The constant {@code INVALID_OPERATION}. */
    @JsxConstant
    public static final long INVALID_OPERATION = 1282L;

    /** The constant {@code INVALID_VALUE}. */
    @JsxConstant
    public static final long INVALID_VALUE = 1281L;

    /** The constant {@code INVERT}. */
    @JsxConstant
    public static final long INVERT = 5386L;

    /** The constant {@code KEEP}. */
    @JsxConstant
    public static final long KEEP = 7680L;

    /** The constant {@code LEQUAL}. */
    @JsxConstant
    public static final long LEQUAL = 515L;

    /** The constant {@code LESS}. */
    @JsxConstant
    public static final long LESS = 513L;

    /** The constant {@code LINEAR}. */
    @JsxConstant
    public static final long LINEAR = 9729L;

    /** The constant {@code LINEAR_MIPMAP_LINEAR}. */
    @JsxConstant
    public static final long LINEAR_MIPMAP_LINEAR = 9987L;

    /** The constant {@code LINEAR_MIPMAP_NEAREST}. */
    @JsxConstant
    public static final long LINEAR_MIPMAP_NEAREST = 9985L;

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
    public static final long LINE_WIDTH = 2849L;

    /** The constant {@code LINK_STATUS}. */
    @JsxConstant
    public static final long LINK_STATUS = 35714L;

    /** The constant {@code LOW_FLOAT}. */
    @JsxConstant
    public static final long LOW_FLOAT = 36336L;

    /** The constant {@code LOW_INT}. */
    @JsxConstant
    public static final long LOW_INT = 36339L;

    /** The constant {@code LUMINANCE}. */
    @JsxConstant
    public static final long LUMINANCE = 6409L;

    /** The constant {@code LUMINANCE_ALPHA}. */
    @JsxConstant
    public static final long LUMINANCE_ALPHA = 6410L;

    /** The constant {@code MAX}. */
    @JsxConstant
    public static final long MAX = 32776L;

    /** The constant {@code MAX_3D_TEXTURE_SIZE}. */
    @JsxConstant
    public static final long MAX_3D_TEXTURE_SIZE = 32883L;

    /** The constant {@code MAX_ARRAY_TEXTURE_LAYERS}. */
    @JsxConstant
    public static final long MAX_ARRAY_TEXTURE_LAYERS = 35071L;

    /** The constant {@code MAX_CLIENT_WAIT_TIMEOUT_WEBGL}. */
    @JsxConstant
    public static final long MAX_CLIENT_WAIT_TIMEOUT_WEBGL = 37447L;

    /** The constant {@code MAX_COLOR_ATTACHMENTS}. */
    @JsxConstant
    public static final long MAX_COLOR_ATTACHMENTS = 36063L;

    /** The constant {@code MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 35379L;

    /** The constant {@code MAX_COMBINED_TEXTURE_IMAGE_UNITS}. */
    @JsxConstant
    public static final long MAX_COMBINED_TEXTURE_IMAGE_UNITS = 35661L;

    /** The constant {@code MAX_COMBINED_UNIFORM_BLOCKS}. */
    @JsxConstant
    public static final long MAX_COMBINED_UNIFORM_BLOCKS = 35374L;

    /** The constant {@code MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 35377L;

    /** The constant {@code MAX_CUBE_MAP_TEXTURE_SIZE}. */
    @JsxConstant
    public static final long MAX_CUBE_MAP_TEXTURE_SIZE = 34076L;

    /** The constant {@code MAX_DRAW_BUFFERS}. */
    @JsxConstant
    public static final long MAX_DRAW_BUFFERS = 34852L;

    /** The constant {@code MAX_ELEMENTS_INDICES}. */
    @JsxConstant
    public static final long MAX_ELEMENTS_INDICES = 33001L;

    /** The constant {@code MAX_ELEMENTS_VERTICES}. */
    @JsxConstant
    public static final long MAX_ELEMENTS_VERTICES = 33000L;

    /** The constant {@code MAX_ELEMENT_INDEX}. */
    @JsxConstant
    public static final long MAX_ELEMENT_INDEX = 36203L;

    /** The constant {@code MAX_FRAGMENT_INPUT_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_FRAGMENT_INPUT_COMPONENTS = 37157L;

    /** The constant {@code MAX_FRAGMENT_UNIFORM_BLOCKS}. */
    @JsxConstant
    public static final long MAX_FRAGMENT_UNIFORM_BLOCKS = 35373L;

    /** The constant {@code MAX_FRAGMENT_UNIFORM_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_FRAGMENT_UNIFORM_COMPONENTS = 35657L;

    /** The constant {@code MAX_FRAGMENT_UNIFORM_VECTORS}. */
    @JsxConstant
    public static final long MAX_FRAGMENT_UNIFORM_VECTORS = 36349L;

    /** The constant {@code MAX_PROGRAM_TEXEL_OFFSET}. */
    @JsxConstant
    public static final long MAX_PROGRAM_TEXEL_OFFSET = 35077L;

    /** The constant {@code MAX_RENDERBUFFER_SIZE}. */
    @JsxConstant
    public static final long MAX_RENDERBUFFER_SIZE = 34024L;

    /** The constant {@code MAX_SAMPLES}. */
    @JsxConstant
    public static final long MAX_SAMPLES = 36183L;

    /** The constant {@code MAX_SERVER_WAIT_TIMEOUT}. */
    @JsxConstant
    public static final long MAX_SERVER_WAIT_TIMEOUT = 37137L;

    /** The constant {@code MAX_TEXTURE_IMAGE_UNITS}. */
    @JsxConstant
    public static final long MAX_TEXTURE_IMAGE_UNITS = 34930L;

    /** The constant {@code MAX_TEXTURE_LOD_BIAS}. */
    @JsxConstant
    public static final long MAX_TEXTURE_LOD_BIAS = 34045L;

    /** The constant {@code MAX_TEXTURE_SIZE}. */
    @JsxConstant
    public static final long MAX_TEXTURE_SIZE = 3379L;

    /** The constant {@code MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 35978L;

    /** The constant {@code MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS}. */
    @JsxConstant
    public static final long MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 35979L;

    /** The constant {@code MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 35968L;

    /** The constant {@code MAX_UNIFORM_BLOCK_SIZE}. */
    @JsxConstant
    public static final long MAX_UNIFORM_BLOCK_SIZE = 35376L;

    /** The constant {@code MAX_UNIFORM_BUFFER_BINDINGS}. */
    @JsxConstant
    public static final long MAX_UNIFORM_BUFFER_BINDINGS = 35375L;

    /** The constant {@code MAX_VARYING_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_VARYING_COMPONENTS = 35659L;

    /** The constant {@code MAX_VARYING_VECTORS}. */
    @JsxConstant
    public static final long MAX_VARYING_VECTORS = 36348L;

    /** The constant {@code MAX_VERTEX_ATTRIBS}. */
    @JsxConstant
    public static final long MAX_VERTEX_ATTRIBS = 34921L;

    /** The constant {@code MAX_VERTEX_OUTPUT_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_VERTEX_OUTPUT_COMPONENTS = 37154L;

    /** The constant {@code MAX_VERTEX_TEXTURE_IMAGE_UNITS}. */
    @JsxConstant
    public static final long MAX_VERTEX_TEXTURE_IMAGE_UNITS = 35660L;

    /** The constant {@code MAX_VERTEX_UNIFORM_BLOCKS}. */
    @JsxConstant
    public static final long MAX_VERTEX_UNIFORM_BLOCKS = 35371L;

    /** The constant {@code MAX_VERTEX_UNIFORM_COMPONENTS}. */
    @JsxConstant
    public static final long MAX_VERTEX_UNIFORM_COMPONENTS = 35658L;

    /** The constant {@code MAX_VERTEX_UNIFORM_VECTORS}. */
    @JsxConstant
    public static final long MAX_VERTEX_UNIFORM_VECTORS = 36347L;

    /** The constant {@code MAX_VIEWPORT_DIMS}. */
    @JsxConstant
    public static final long MAX_VIEWPORT_DIMS = 3386L;

    /** The constant {@code MEDIUM_FLOAT}. */
    @JsxConstant
    public static final long MEDIUM_FLOAT = 36337L;

    /** The constant {@code MEDIUM_INT}. */
    @JsxConstant
    public static final long MEDIUM_INT = 36340L;

    /** The constant {@code MIN}. */
    @JsxConstant
    public static final long MIN = 32775L;

    /** The constant {@code MIN_PROGRAM_TEXEL_OFFSET}. */
    @JsxConstant
    public static final long MIN_PROGRAM_TEXEL_OFFSET = 35076L;

    /** The constant {@code MIRRORED_REPEAT}. */
    @JsxConstant
    public static final long MIRRORED_REPEAT = 33648L;

    /** The constant {@code NEAREST}. */
    @JsxConstant
    public static final long NEAREST = 9728L;

    /** The constant {@code NEAREST_MIPMAP_LINEAR}. */
    @JsxConstant
    public static final long NEAREST_MIPMAP_LINEAR = 9986L;

    /** The constant {@code NEAREST_MIPMAP_NEAREST}. */
    @JsxConstant
    public static final long NEAREST_MIPMAP_NEAREST = 9984L;

    /** The constant {@code NEVER}. */
    @JsxConstant
    public static final long NEVER = 512L;

    /** The constant {@code NICEST}. */
    @JsxConstant
    public static final long NICEST = 4354L;

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
    public static final long OBJECT_TYPE = 37138L;

    /** The constant {@code ONE}. */
    @JsxConstant
    public static final long ONE = 1L;

    /** The constant {@code ONE_MINUS_CONSTANT_ALPHA}. */
    @JsxConstant
    public static final long ONE_MINUS_CONSTANT_ALPHA = 32772L;

    /** The constant {@code ONE_MINUS_CONSTANT_COLOR}. */
    @JsxConstant
    public static final long ONE_MINUS_CONSTANT_COLOR = 32770L;

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
    public static final long OUT_OF_MEMORY = 1285L;

    /** The constant {@code PACK_ALIGNMENT}. */
    @JsxConstant
    public static final long PACK_ALIGNMENT = 3333L;

    /** The constant {@code PACK_ROW_LENGTH}. */
    @JsxConstant
    public static final long PACK_ROW_LENGTH = 3330L;

    /** The constant {@code PACK_SKIP_PIXELS}. */
    @JsxConstant
    public static final long PACK_SKIP_PIXELS = 3332L;

    /** The constant {@code PACK_SKIP_ROWS}. */
    @JsxConstant
    public static final long PACK_SKIP_ROWS = 3331L;

    /** The constant {@code PIXEL_PACK_BUFFER}. */
    @JsxConstant
    public static final long PIXEL_PACK_BUFFER = 35051L;

    /** The constant {@code PIXEL_PACK_BUFFER_BINDING}. */
    @JsxConstant
    public static final long PIXEL_PACK_BUFFER_BINDING = 35053L;

    /** The constant {@code PIXEL_UNPACK_BUFFER}. */
    @JsxConstant
    public static final long PIXEL_UNPACK_BUFFER = 35052L;

    /** The constant {@code PIXEL_UNPACK_BUFFER_BINDING}. */
    @JsxConstant
    public static final long PIXEL_UNPACK_BUFFER_BINDING = 35055L;

    /** The constant {@code POINTS}. */
    @JsxConstant
    public static final long POINTS = 0L;

    /** The constant {@code POLYGON_OFFSET_FACTOR}. */
    @JsxConstant
    public static final long POLYGON_OFFSET_FACTOR = 32824L;

    /** The constant {@code POLYGON_OFFSET_FILL}. */
    @JsxConstant
    public static final long POLYGON_OFFSET_FILL = 32823L;

    /** The constant {@code POLYGON_OFFSET_UNITS}. */
    @JsxConstant
    public static final long POLYGON_OFFSET_UNITS = 10752L;

    /** The constant {@code QUERY_RESULT}. */
    @JsxConstant
    public static final long QUERY_RESULT = 34918L;

    /** The constant {@code QUERY_RESULT_AVAILABLE}. */
    @JsxConstant
    public static final long QUERY_RESULT_AVAILABLE = 34919L;

    /** The constant {@code R11F_G11F_B10F}. */
    @JsxConstant
    public static final long R11F_G11F_B10F = 35898L;

    /** The constant {@code R16F}. */
    @JsxConstant
    public static final long R16F = 33325L;

    /** The constant {@code R16I}. */
    @JsxConstant
    public static final long R16I = 33331L;

    /** The constant {@code R16UI}. */
    @JsxConstant
    public static final long R16UI = 33332L;

    /** The constant {@code R32F}. */
    @JsxConstant
    public static final long R32F = 33326L;

    /** The constant {@code R32I}. */
    @JsxConstant
    public static final long R32I = 33333L;

    /** The constant {@code R32UI}. */
    @JsxConstant
    public static final long R32UI = 33334L;

    /** The constant {@code R8}. */
    @JsxConstant
    public static final long R8 = 33321L;

    /** The constant {@code R8I}. */
    @JsxConstant
    public static final long R8I = 33329L;

    /** The constant {@code R8UI}. */
    @JsxConstant
    public static final long R8UI = 33330L;

    /** The constant {@code R8_SNORM}. */
    @JsxConstant
    public static final long R8_SNORM = 36756L;

    /** The constant {@code RASTERIZER_DISCARD}. */
    @JsxConstant
    public static final long RASTERIZER_DISCARD = 35977L;

    /** The constant {@code READ_BUFFER}. */
    @JsxConstant
    public static final long READ_BUFFER = 3074L;

    /** The constant {@code READ_FRAMEBUFFER}. */
    @JsxConstant
    public static final long READ_FRAMEBUFFER = 36008L;

    /** The constant {@code READ_FRAMEBUFFER_BINDING}. */
    @JsxConstant
    public static final long READ_FRAMEBUFFER_BINDING = 36010L;

    /** The constant {@code RED}. */
    @JsxConstant
    public static final long RED = 6403L;

    /** The constant {@code RED_BITS}. */
    @JsxConstant
    public static final long RED_BITS = 3410L;

    /** The constant {@code RED_INTEGER}. */
    @JsxConstant
    public static final long RED_INTEGER = 36244L;

    /** The constant {@code RENDERBUFFER}. */
    @JsxConstant
    public static final long RENDERBUFFER = 36161L;

    /** The constant {@code RENDERBUFFER_ALPHA_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_ALPHA_SIZE = 36179L;

    /** The constant {@code RENDERBUFFER_BINDING}. */
    @JsxConstant
    public static final long RENDERBUFFER_BINDING = 36007L;

    /** The constant {@code RENDERBUFFER_BLUE_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_BLUE_SIZE = 36178L;

    /** The constant {@code RENDERBUFFER_DEPTH_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_DEPTH_SIZE = 36180L;

    /** The constant {@code RENDERBUFFER_GREEN_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_GREEN_SIZE = 36177L;

    /** The constant {@code RENDERBUFFER_HEIGHT}. */
    @JsxConstant
    public static final long RENDERBUFFER_HEIGHT = 36163L;

    /** The constant {@code RENDERBUFFER_INTERNAL_FORMAT}. */
    @JsxConstant
    public static final long RENDERBUFFER_INTERNAL_FORMAT = 36164L;

    /** The constant {@code RENDERBUFFER_RED_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_RED_SIZE = 36176L;

    /** The constant {@code RENDERBUFFER_SAMPLES}. */
    @JsxConstant
    public static final long RENDERBUFFER_SAMPLES = 36011L;

    /** The constant {@code RENDERBUFFER_STENCIL_SIZE}. */
    @JsxConstant
    public static final long RENDERBUFFER_STENCIL_SIZE = 36181L;

    /** The constant {@code RENDERBUFFER_WIDTH}. */
    @JsxConstant
    public static final long RENDERBUFFER_WIDTH = 36162L;

    /** The constant {@code RENDERER}. */
    @JsxConstant
    public static final long RENDERER = 7937L;

    /** The constant {@code REPEAT}. */
    @JsxConstant
    public static final long REPEAT = 10497L;

    /** The constant {@code REPLACE}. */
    @JsxConstant
    public static final long REPLACE = 7681L;

    /** The constant {@code RG}. */
    @JsxConstant
    public static final long RG = 33319L;

    /** The constant {@code RG16F}. */
    @JsxConstant
    public static final long RG16F = 33327L;

    /** The constant {@code RG16I}. */
    @JsxConstant
    public static final long RG16I = 33337L;

    /** The constant {@code RG16UI}. */
    @JsxConstant
    public static final long RG16UI = 33338L;

    /** The constant {@code RG32F}. */
    @JsxConstant
    public static final long RG32F = 33328L;

    /** The constant {@code RG32I}. */
    @JsxConstant
    public static final long RG32I = 33339L;

    /** The constant {@code RG32UI}. */
    @JsxConstant
    public static final long RG32UI = 33340L;

    /** The constant {@code RG8}. */
    @JsxConstant
    public static final long RG8 = 33323L;

    /** The constant {@code RG8I}. */
    @JsxConstant
    public static final long RG8I = 33335L;

    /** The constant {@code RG8UI}. */
    @JsxConstant
    public static final long RG8UI = 33336L;

    /** The constant {@code RG8_SNORM}. */
    @JsxConstant
    public static final long RG8_SNORM = 36757L;

    /** The constant {@code RGB}. */
    @JsxConstant
    public static final long RGB = 6407L;

    /** The constant {@code RGB10_A2}. */
    @JsxConstant
    public static final long RGB10_A2 = 32857L;

    /** The constant {@code RGB10_A2UI}. */
    @JsxConstant
    public static final long RGB10_A2UI = 36975L;

    /** The constant {@code RGB16F}. */
    @JsxConstant
    public static final long RGB16F = 34843L;

    /** The constant {@code RGB16I}. */
    @JsxConstant
    public static final long RGB16I = 36233L;

    /** The constant {@code RGB16UI}. */
    @JsxConstant
    public static final long RGB16UI = 36215L;

    /** The constant {@code RGB32F}. */
    @JsxConstant
    public static final long RGB32F = 34837L;

    /** The constant {@code RGB32I}. */
    @JsxConstant
    public static final long RGB32I = 36227L;

    /** The constant {@code RGB32UI}. */
    @JsxConstant
    public static final long RGB32UI = 36209L;

    /** The constant {@code RGB565}. */
    @JsxConstant
    public static final long RGB565 = 36194L;

    /** The constant {@code RGB5_A1}. */
    @JsxConstant
    public static final long RGB5_A1 = 32855L;

    /** The constant {@code RGB8}. */
    @JsxConstant
    public static final long RGB8 = 32849L;

    /** The constant {@code RGB8I}. */
    @JsxConstant
    public static final long RGB8I = 36239L;

    /** The constant {@code RGB8UI}. */
    @JsxConstant
    public static final long RGB8UI = 36221L;

    /** The constant {@code RGB8_SNORM}. */
    @JsxConstant
    public static final long RGB8_SNORM = 36758L;

    /** The constant {@code RGB9_E5}. */
    @JsxConstant
    public static final long RGB9_E5 = 35901L;

    /** The constant {@code RGBA}. */
    @JsxConstant
    public static final long RGBA = 6408L;

    /** The constant {@code RGBA16F}. */
    @JsxConstant
    public static final long RGBA16F = 34842L;

    /** The constant {@code RGBA16I}. */
    @JsxConstant
    public static final long RGBA16I = 36232L;

    /** The constant {@code RGBA16UI}. */
    @JsxConstant
    public static final long RGBA16UI = 36214L;

    /** The constant {@code RGBA32F}. */
    @JsxConstant
    public static final long RGBA32F = 34836L;

    /** The constant {@code RGBA32I}. */
    @JsxConstant
    public static final long RGBA32I = 36226L;

    /** The constant {@code RGBA32UI}. */
    @JsxConstant
    public static final long RGBA32UI = 36208L;

    /** The constant {@code RGBA4}. */
    @JsxConstant
    public static final long RGBA4 = 32854L;

    /** The constant {@code RGBA8}. */
    @JsxConstant
    public static final long RGBA8 = 32856L;

    /** The constant {@code RGBA8I}. */
    @JsxConstant
    public static final long RGBA8I = 36238L;

    /** The constant {@code RGBA8UI}. */
    @JsxConstant
    public static final long RGBA8UI = 36220L;

    /** The constant {@code RGBA8_SNORM}. */
    @JsxConstant
    public static final long RGBA8_SNORM = 36759L;

    /** The constant {@code RGBA_INTEGER}. */
    @JsxConstant
    public static final long RGBA_INTEGER = 36249L;

    /** The constant {@code RGB_INTEGER}. */
    @JsxConstant
    public static final long RGB_INTEGER = 36248L;

    /** The constant {@code RG_INTEGER}. */
    @JsxConstant
    public static final long RG_INTEGER = 33320L;

    /** The constant {@code SAMPLER_2D}. */
    @JsxConstant
    public static final long SAMPLER_2D = 35678L;

    /** The constant {@code SAMPLER_2D_ARRAY}. */
    @JsxConstant
    public static final long SAMPLER_2D_ARRAY = 36289L;

    /** The constant {@code SAMPLER_2D_ARRAY_SHADOW}. */
    @JsxConstant
    public static final long SAMPLER_2D_ARRAY_SHADOW = 36292L;

    /** The constant {@code SAMPLER_2D_SHADOW}. */
    @JsxConstant
    public static final long SAMPLER_2D_SHADOW = 35682L;

    /** The constant {@code SAMPLER_3D}. */
    @JsxConstant
    public static final long SAMPLER_3D = 35679L;

    /** The constant {@code SAMPLER_BINDING}. */
    @JsxConstant
    public static final long SAMPLER_BINDING = 35097L;

    /** The constant {@code SAMPLER_CUBE}. */
    @JsxConstant
    public static final long SAMPLER_CUBE = 35680L;

    /** The constant {@code SAMPLER_CUBE_SHADOW}. */
    @JsxConstant
    public static final long SAMPLER_CUBE_SHADOW = 36293L;

    /** The constant {@code SAMPLES}. */
    @JsxConstant
    public static final long SAMPLES = 32937L;

    /** The constant {@code SAMPLE_ALPHA_TO_COVERAGE}. */
    @JsxConstant
    public static final long SAMPLE_ALPHA_TO_COVERAGE = 32926L;

    /** The constant {@code SAMPLE_BUFFERS}. */
    @JsxConstant
    public static final long SAMPLE_BUFFERS = 32936L;

    /** The constant {@code SAMPLE_COVERAGE}. */
    @JsxConstant
    public static final long SAMPLE_COVERAGE = 32928L;

    /** The constant {@code SAMPLE_COVERAGE_INVERT}. */
    @JsxConstant
    public static final long SAMPLE_COVERAGE_INVERT = 32939L;

    /** The constant {@code SAMPLE_COVERAGE_VALUE}. */
    @JsxConstant
    public static final long SAMPLE_COVERAGE_VALUE = 32938L;

    /** The constant {@code SCISSOR_BOX}. */
    @JsxConstant
    public static final long SCISSOR_BOX = 3088L;

    /** The constant {@code SCISSOR_TEST}. */
    @JsxConstant
    public static final long SCISSOR_TEST = 3089L;

    /** The constant {@code SEPARATE_ATTRIBS}. */
    @JsxConstant
    public static final long SEPARATE_ATTRIBS = 35981L;

    /** The constant {@code SHADER_TYPE}. */
    @JsxConstant
    public static final long SHADER_TYPE = 35663L;

    /** The constant {@code SHADING_LANGUAGE_VERSION}. */
    @JsxConstant
    public static final long SHADING_LANGUAGE_VERSION = 35724L;

    /** The constant {@code SHORT}. */
    @JsxConstant
    public static final long SHORT = 5122L;

    /** The constant {@code SIGNALED}. */
    @JsxConstant
    public static final long SIGNALED = 37145L;

    /** The constant {@code SIGNED_NORMALIZED}. */
    @JsxConstant
    public static final long SIGNED_NORMALIZED = 36764L;

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
    public static final long SRGB = 35904L;

    /** The constant {@code SRGB8}. */
    @JsxConstant
    public static final long SRGB8 = 35905L;

    /** The constant {@code SRGB8_ALPHA8}. */
    @JsxConstant
    public static final long SRGB8_ALPHA8 = 35907L;

    /** The constant {@code STATIC_COPY}. */
    @JsxConstant
    public static final long STATIC_COPY = 35046L;

    /** The constant {@code STATIC_DRAW}. */
    @JsxConstant
    public static final long STATIC_DRAW = 35044L;

    /** The constant {@code STATIC_READ}. */
    @JsxConstant
    public static final long STATIC_READ = 35045L;

    /** The constant {@code STENCIL}. */
    @JsxConstant
    public static final long STENCIL = 6146L;

    /** The constant {@code STENCIL_ATTACHMENT}. */
    @JsxConstant
    public static final long STENCIL_ATTACHMENT = 36128L;

    /** The constant {@code STENCIL_BACK_FAIL}. */
    @JsxConstant
    public static final long STENCIL_BACK_FAIL = 34817L;

    /** The constant {@code STENCIL_BACK_FUNC}. */
    @JsxConstant
    public static final long STENCIL_BACK_FUNC = 34816L;

    /** The constant {@code STENCIL_BACK_PASS_DEPTH_FAIL}. */
    @JsxConstant
    public static final long STENCIL_BACK_PASS_DEPTH_FAIL = 34818L;

    /** The constant {@code STENCIL_BACK_PASS_DEPTH_PASS}. */
    @JsxConstant
    public static final long STENCIL_BACK_PASS_DEPTH_PASS = 34819L;

    /** The constant {@code STENCIL_BACK_REF}. */
    @JsxConstant
    public static final long STENCIL_BACK_REF = 36003L;

    /** The constant {@code STENCIL_BACK_VALUE_MASK}. */
    @JsxConstant
    public static final long STENCIL_BACK_VALUE_MASK = 36004L;

    /** The constant {@code STENCIL_BACK_WRITEMASK}. */
    @JsxConstant
    public static final long STENCIL_BACK_WRITEMASK = 36005L;

    /** The constant {@code STENCIL_BITS}. */
    @JsxConstant
    public static final long STENCIL_BITS = 3415L;

    /** The constant {@code STENCIL_BUFFER_BIT}. */
    @JsxConstant
    public static final long STENCIL_BUFFER_BIT = 1024L;

    /** The constant {@code STENCIL_CLEAR_VALUE}. */
    @JsxConstant
    public static final long STENCIL_CLEAR_VALUE = 2961L;

    /** The constant {@code STENCIL_FAIL}. */
    @JsxConstant
    public static final long STENCIL_FAIL = 2964L;

    /** The constant {@code STENCIL_FUNC}. */
    @JsxConstant
    public static final long STENCIL_FUNC = 2962L;

    /** The constant {@code STENCIL_INDEX}. */
    @JsxConstant({FF52, IE})
    public static final long STENCIL_INDEX = 6401L;

    /** The constant {@code STENCIL_INDEX8}. */
    @JsxConstant
    public static final long STENCIL_INDEX8 = 36168L;

    /** The constant {@code STENCIL_PASS_DEPTH_FAIL}. */
    @JsxConstant
    public static final long STENCIL_PASS_DEPTH_FAIL = 2965L;

    /** The constant {@code STENCIL_PASS_DEPTH_PASS}. */
    @JsxConstant
    public static final long STENCIL_PASS_DEPTH_PASS = 2966L;

    /** The constant {@code STENCIL_REF}. */
    @JsxConstant
    public static final long STENCIL_REF = 2967L;

    /** The constant {@code STENCIL_TEST}. */
    @JsxConstant
    public static final long STENCIL_TEST = 2960L;

    /** The constant {@code STENCIL_VALUE_MASK}. */
    @JsxConstant
    public static final long STENCIL_VALUE_MASK = 2963L;

    /** The constant {@code STENCIL_WRITEMASK}. */
    @JsxConstant
    public static final long STENCIL_WRITEMASK = 2968L;

    /** The constant {@code STREAM_COPY}. */
    @JsxConstant
    public static final long STREAM_COPY = 35042L;

    /** The constant {@code STREAM_DRAW}. */
    @JsxConstant
    public static final long STREAM_DRAW = 35040L;

    /** The constant {@code STREAM_READ}. */
    @JsxConstant
    public static final long STREAM_READ = 35041L;

    /** The constant {@code SUBPIXEL_BITS}. */
    @JsxConstant
    public static final long SUBPIXEL_BITS = 3408L;

    /** The constant {@code SYNC_CONDITION}. */
    @JsxConstant
    public static final long SYNC_CONDITION = 37139L;

    /** The constant {@code SYNC_FENCE}. */
    @JsxConstant
    public static final long SYNC_FENCE = 37142L;

    /** The constant {@code SYNC_FLAGS}. */
    @JsxConstant
    public static final long SYNC_FLAGS = 37141L;

    /** The constant {@code SYNC_FLUSH_COMMANDS_BIT}. */
    @JsxConstant
    public static final long SYNC_FLUSH_COMMANDS_BIT = 1L;

    /** The constant {@code SYNC_GPU_COMMANDS_COMPLETE}. */
    @JsxConstant
    public static final long SYNC_GPU_COMMANDS_COMPLETE = 37143L;

    /** The constant {@code SYNC_STATUS}. */
    @JsxConstant
    public static final long SYNC_STATUS = 37140L;

    /** The constant {@code TEXTURE}. */
    @JsxConstant
    public static final long TEXTURE = 5890L;

    /** The constant {@code TEXTURE0}. */
    @JsxConstant
    public static final long TEXTURE0 = 33984L;

    /** The constant {@code TEXTURE1}. */
    @JsxConstant
    public static final long TEXTURE1 = 33985L;

    /** The constant {@code TEXTURE10}. */
    @JsxConstant
    public static final long TEXTURE10 = 33994L;

    /** The constant {@code TEXTURE11}. */
    @JsxConstant
    public static final long TEXTURE11 = 33995L;

    /** The constant {@code TEXTURE12}. */
    @JsxConstant
    public static final long TEXTURE12 = 33996L;

    /** The constant {@code TEXTURE13}. */
    @JsxConstant
    public static final long TEXTURE13 = 33997L;

    /** The constant {@code TEXTURE14}. */
    @JsxConstant
    public static final long TEXTURE14 = 33998L;

    /** The constant {@code TEXTURE15}. */
    @JsxConstant
    public static final long TEXTURE15 = 33999L;

    /** The constant {@code TEXTURE16}. */
    @JsxConstant
    public static final long TEXTURE16 = 34000L;

    /** The constant {@code TEXTURE17}. */
    @JsxConstant
    public static final long TEXTURE17 = 34001L;

    /** The constant {@code TEXTURE18}. */
    @JsxConstant
    public static final long TEXTURE18 = 34002L;

    /** The constant {@code TEXTURE19}. */
    @JsxConstant
    public static final long TEXTURE19 = 34003L;

    /** The constant {@code TEXTURE2}. */
    @JsxConstant
    public static final long TEXTURE2 = 33986L;

    /** The constant {@code TEXTURE20}. */
    @JsxConstant
    public static final long TEXTURE20 = 34004L;

    /** The constant {@code TEXTURE21}. */
    @JsxConstant
    public static final long TEXTURE21 = 34005L;

    /** The constant {@code TEXTURE22}. */
    @JsxConstant
    public static final long TEXTURE22 = 34006L;

    /** The constant {@code TEXTURE23}. */
    @JsxConstant
    public static final long TEXTURE23 = 34007L;

    /** The constant {@code TEXTURE24}. */
    @JsxConstant
    public static final long TEXTURE24 = 34008L;

    /** The constant {@code TEXTURE25}. */
    @JsxConstant
    public static final long TEXTURE25 = 34009L;

    /** The constant {@code TEXTURE26}. */
    @JsxConstant
    public static final long TEXTURE26 = 34010L;

    /** The constant {@code TEXTURE27}. */
    @JsxConstant
    public static final long TEXTURE27 = 34011L;

    /** The constant {@code TEXTURE28}. */
    @JsxConstant
    public static final long TEXTURE28 = 34012L;

    /** The constant {@code TEXTURE29}. */
    @JsxConstant
    public static final long TEXTURE29 = 34013L;

    /** The constant {@code TEXTURE3}. */
    @JsxConstant
    public static final long TEXTURE3 = 33987L;

    /** The constant {@code TEXTURE30}. */
    @JsxConstant
    public static final long TEXTURE30 = 34014L;

    /** The constant {@code TEXTURE31}. */
    @JsxConstant
    public static final long TEXTURE31 = 34015L;

    /** The constant {@code TEXTURE4}. */
    @JsxConstant
    public static final long TEXTURE4 = 33988L;

    /** The constant {@code TEXTURE5}. */
    @JsxConstant
    public static final long TEXTURE5 = 33989L;

    /** The constant {@code TEXTURE6}. */
    @JsxConstant
    public static final long TEXTURE6 = 33990L;

    /** The constant {@code TEXTURE7}. */
    @JsxConstant
    public static final long TEXTURE7 = 33991L;

    /** The constant {@code TEXTURE8}. */
    @JsxConstant
    public static final long TEXTURE8 = 33992L;

    /** The constant {@code TEXTURE9}. */
    @JsxConstant
    public static final long TEXTURE9 = 33993L;

    /** The constant {@code TEXTURE_2D}. */
    @JsxConstant
    public static final long TEXTURE_2D = 3553L;

    /** The constant {@code TEXTURE_2D_ARRAY}. */
    @JsxConstant
    public static final long TEXTURE_2D_ARRAY = 35866L;

    /** The constant {@code TEXTURE_3D}. */
    @JsxConstant
    public static final long TEXTURE_3D = 32879L;

    /** The constant {@code TEXTURE_BASE_LEVEL}. */
    @JsxConstant
    public static final long TEXTURE_BASE_LEVEL = 33084L;

    /** The constant {@code TEXTURE_BINDING_2D}. */
    @JsxConstant
    public static final long TEXTURE_BINDING_2D = 32873L;

    /** The constant {@code TEXTURE_BINDING_2D_ARRAY}. */
    @JsxConstant
    public static final long TEXTURE_BINDING_2D_ARRAY = 35869L;

    /** The constant {@code TEXTURE_BINDING_3D}. */
    @JsxConstant
    public static final long TEXTURE_BINDING_3D = 32874L;

    /** The constant {@code TEXTURE_BINDING_CUBE_MAP}. */
    @JsxConstant
    public static final long TEXTURE_BINDING_CUBE_MAP = 34068L;

    /** The constant {@code TEXTURE_COMPARE_FUNC}. */
    @JsxConstant
    public static final long TEXTURE_COMPARE_FUNC = 34893L;

    /** The constant {@code TEXTURE_COMPARE_MODE}. */
    @JsxConstant
    public static final long TEXTURE_COMPARE_MODE = 34892L;

    /** The constant {@code TEXTURE_CUBE_MAP}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP = 34067L;

    /** The constant {@code TEXTURE_CUBE_MAP_NEGATIVE_X}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_NEGATIVE_X = 34070L;

    /** The constant {@code TEXTURE_CUBE_MAP_NEGATIVE_Y}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072L;

    /** The constant {@code TEXTURE_CUBE_MAP_NEGATIVE_Z}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074L;

    /** The constant {@code TEXTURE_CUBE_MAP_POSITIVE_X}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_POSITIVE_X = 34069L;

    /** The constant {@code TEXTURE_CUBE_MAP_POSITIVE_Y}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_POSITIVE_Y = 34071L;

    /** The constant {@code TEXTURE_CUBE_MAP_POSITIVE_Z}. */
    @JsxConstant
    public static final long TEXTURE_CUBE_MAP_POSITIVE_Z = 34073L;

    /** The constant {@code TEXTURE_IMMUTABLE_FORMAT}. */
    @JsxConstant
    public static final long TEXTURE_IMMUTABLE_FORMAT = 37167L;

    /** The constant {@code TEXTURE_IMMUTABLE_LEVELS}. */
    @JsxConstant
    public static final long TEXTURE_IMMUTABLE_LEVELS = 33503L;

    /** The constant {@code TEXTURE_MAG_FILTER}. */
    @JsxConstant
    public static final long TEXTURE_MAG_FILTER = 10240L;

    /** The constant {@code TEXTURE_MAX_LEVEL}. */
    @JsxConstant
    public static final long TEXTURE_MAX_LEVEL = 33085L;

    /** The constant {@code TEXTURE_MAX_LOD}. */
    @JsxConstant
    public static final long TEXTURE_MAX_LOD = 33083L;

    /** The constant {@code TEXTURE_MIN_FILTER}. */
    @JsxConstant
    public static final long TEXTURE_MIN_FILTER = 10241L;

    /** The constant {@code TEXTURE_MIN_LOD}. */
    @JsxConstant
    public static final long TEXTURE_MIN_LOD = 33082L;

    /** The constant {@code TEXTURE_WRAP_R}. */
    @JsxConstant
    public static final long TEXTURE_WRAP_R = 32882L;

    /** The constant {@code TEXTURE_WRAP_S}. */
    @JsxConstant
    public static final long TEXTURE_WRAP_S = 10242L;

    /** The constant {@code TEXTURE_WRAP_T}. */
    @JsxConstant
    public static final long TEXTURE_WRAP_T = 10243L;

    /** The constant {@code TIMEOUT_EXPIRED}. */
    @JsxConstant
    public static final long TIMEOUT_EXPIRED = 37147L;

    /** The constant {@code TIMEOUT_IGNORED}. */
    @JsxConstant
    public static final long TIMEOUT_IGNORED = -1L;

    /** The constant {@code TRANSFORM_FEEDBACK}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK = 36386L;

    /** The constant {@code TRANSFORM_FEEDBACK_ACTIVE}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_ACTIVE = 36388L;

    /** The constant {@code TRANSFORM_FEEDBACK_BINDING}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BINDING = 36389L;

    /** The constant {@code TRANSFORM_FEEDBACK_BUFFER}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BUFFER = 35982L;

    /** The constant {@code TRANSFORM_FEEDBACK_BUFFER_BINDING}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BUFFER_BINDING = 35983L;

    /** The constant {@code TRANSFORM_FEEDBACK_BUFFER_MODE}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BUFFER_MODE = 35967L;

    /** The constant {@code TRANSFORM_FEEDBACK_BUFFER_SIZE}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BUFFER_SIZE = 35973L;

    /** The constant {@code TRANSFORM_FEEDBACK_BUFFER_START}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_BUFFER_START = 35972L;

    /** The constant {@code TRANSFORM_FEEDBACK_PAUSED}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_PAUSED = 36387L;

    /** The constant {@code TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 35976L;

    /** The constant {@code TRANSFORM_FEEDBACK_VARYINGS}. */
    @JsxConstant
    public static final long TRANSFORM_FEEDBACK_VARYINGS = 35971L;

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
    public static final long UNIFORM_ARRAY_STRIDE = 35388L;

    /** The constant {@code UNIFORM_BLOCK_ACTIVE_UNIFORMS}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_ACTIVE_UNIFORMS = 35394L;

    /** The constant {@code UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 35395L;

    /** The constant {@code UNIFORM_BLOCK_BINDING}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_BINDING = 35391L;

    /** The constant {@code UNIFORM_BLOCK_DATA_SIZE}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_DATA_SIZE = 35392L;

    /** The constant {@code UNIFORM_BLOCK_INDEX}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_INDEX = 35386L;

    /** The constant {@code UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 35398L;

    /** The constant {@code UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER}. */
    @JsxConstant
    public static final long UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 35396L;

    /** The constant {@code UNIFORM_BUFFER}. */
    @JsxConstant
    public static final long UNIFORM_BUFFER = 35345L;

    /** The constant {@code UNIFORM_BUFFER_BINDING}. */
    @JsxConstant
    public static final long UNIFORM_BUFFER_BINDING = 35368L;

    /** The constant {@code UNIFORM_BUFFER_OFFSET_ALIGNMENT}. */
    @JsxConstant
    public static final long UNIFORM_BUFFER_OFFSET_ALIGNMENT = 35380L;

    /** The constant {@code UNIFORM_BUFFER_SIZE}. */
    @JsxConstant
    public static final long UNIFORM_BUFFER_SIZE = 35370L;

    /** The constant {@code UNIFORM_BUFFER_START}. */
    @JsxConstant
    public static final long UNIFORM_BUFFER_START = 35369L;

    /** The constant {@code UNIFORM_IS_ROW_MAJOR}. */
    @JsxConstant
    public static final long UNIFORM_IS_ROW_MAJOR = 35390L;

    /** The constant {@code UNIFORM_MATRIX_STRIDE}. */
    @JsxConstant
    public static final long UNIFORM_MATRIX_STRIDE = 35389L;

    /** The constant {@code UNIFORM_OFFSET}. */
    @JsxConstant
    public static final long UNIFORM_OFFSET = 35387L;

    /** The constant {@code UNIFORM_SIZE}. */
    @JsxConstant
    public static final long UNIFORM_SIZE = 35384L;

    /** The constant {@code UNIFORM_TYPE}. */
    @JsxConstant
    public static final long UNIFORM_TYPE = 35383L;

    /** The constant {@code UNPACK_ALIGNMENT}. */
    @JsxConstant
    public static final long UNPACK_ALIGNMENT = 3317L;

    /** The constant {@code UNPACK_COLORSPACE_CONVERSION_WEBGL}. */
    @JsxConstant
    public static final long UNPACK_COLORSPACE_CONVERSION_WEBGL = 37443L;

    /** The constant {@code UNPACK_FLIP_Y_WEBGL}. */
    @JsxConstant
    public static final long UNPACK_FLIP_Y_WEBGL = 37440L;

    /** The constant {@code UNPACK_IMAGE_HEIGHT}. */
    @JsxConstant
    public static final long UNPACK_IMAGE_HEIGHT = 32878L;

    /** The constant {@code UNPACK_PREMULTIPLY_ALPHA_WEBGL}. */
    @JsxConstant
    public static final long UNPACK_PREMULTIPLY_ALPHA_WEBGL = 37441L;

    /** The constant {@code UNPACK_ROW_LENGTH}. */
    @JsxConstant
    public static final long UNPACK_ROW_LENGTH = 3314L;

    /** The constant {@code UNPACK_SKIP_IMAGES}. */
    @JsxConstant
    public static final long UNPACK_SKIP_IMAGES = 32877L;

    /** The constant {@code UNPACK_SKIP_PIXELS}. */
    @JsxConstant
    public static final long UNPACK_SKIP_PIXELS = 3316L;

    /** The constant {@code UNPACK_SKIP_ROWS}. */
    @JsxConstant
    public static final long UNPACK_SKIP_ROWS = 3315L;

    /** The constant {@code UNSIGNALED}. */
    @JsxConstant
    public static final long UNSIGNALED = 37144L;

    /** The constant {@code UNSIGNED_BYTE}. */
    @JsxConstant
    public static final long UNSIGNED_BYTE = 5121L;

    /** The constant {@code UNSIGNED_INT}. */
    @JsxConstant
    public static final long UNSIGNED_INT = 5125L;

    /** The constant {@code UNSIGNED_INT_10F_11F_11F_REV}. */
    @JsxConstant
    public static final long UNSIGNED_INT_10F_11F_11F_REV = 35899L;

    /** The constant {@code UNSIGNED_INT_24_8}. */
    @JsxConstant
    public static final long UNSIGNED_INT_24_8 = 34042L;

    /** The constant {@code UNSIGNED_INT_2_10_10_10_REV}. */
    @JsxConstant
    public static final long UNSIGNED_INT_2_10_10_10_REV = 33640L;

    /** The constant {@code UNSIGNED_INT_5_9_9_9_REV}. */
    @JsxConstant
    public static final long UNSIGNED_INT_5_9_9_9_REV = 35902L;

    /** The constant {@code UNSIGNED_INT_SAMPLER_2D}. */
    @JsxConstant
    public static final long UNSIGNED_INT_SAMPLER_2D = 36306L;

    /** The constant {@code UNSIGNED_INT_SAMPLER_2D_ARRAY}. */
    @JsxConstant
    public static final long UNSIGNED_INT_SAMPLER_2D_ARRAY = 36311L;

    /** The constant {@code UNSIGNED_INT_SAMPLER_3D}. */
    @JsxConstant
    public static final long UNSIGNED_INT_SAMPLER_3D = 36307L;

    /** The constant {@code UNSIGNED_INT_SAMPLER_CUBE}. */
    @JsxConstant
    public static final long UNSIGNED_INT_SAMPLER_CUBE = 36308L;

    /** The constant {@code UNSIGNED_INT_VEC2}. */
    @JsxConstant
    public static final long UNSIGNED_INT_VEC2 = 36294L;

    /** The constant {@code UNSIGNED_INT_VEC3}. */
    @JsxConstant
    public static final long UNSIGNED_INT_VEC3 = 36295L;

    /** The constant {@code UNSIGNED_INT_VEC4}. */
    @JsxConstant
    public static final long UNSIGNED_INT_VEC4 = 36296L;

    /** The constant {@code UNSIGNED_NORMALIZED}. */
    @JsxConstant
    public static final long UNSIGNED_NORMALIZED = 35863L;

    /** The constant {@code UNSIGNED_SHORT}. */
    @JsxConstant
    public static final long UNSIGNED_SHORT = 5123L;

    /** The constant {@code UNSIGNED_SHORT_4_4_4_4}. */
    @JsxConstant
    public static final long UNSIGNED_SHORT_4_4_4_4 = 32819L;

    /** The constant {@code UNSIGNED_SHORT_5_5_5_1}. */
    @JsxConstant
    public static final long UNSIGNED_SHORT_5_5_5_1 = 32820L;

    /** The constant {@code UNSIGNED_SHORT_5_6_5}. */
    @JsxConstant
    public static final long UNSIGNED_SHORT_5_6_5 = 33635L;

    /** The constant {@code VALIDATE_STATUS}. */
    @JsxConstant
    public static final long VALIDATE_STATUS = 35715L;

    /** The constant {@code VENDOR}. */
    @JsxConstant
    public static final long VENDOR = 7936L;

    /** The constant {@code VERSION}. */
    @JsxConstant
    public static final long VERSION = 7938L;

    /** The constant {@code VERTEX_ARRAY_BINDING}. */
    @JsxConstant
    public static final long VERTEX_ARRAY_BINDING = 34229L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_BUFFER_BINDING}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 34975L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_DIVISOR}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_DIVISOR = 35070L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_ENABLED}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_ENABLED = 34338L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_INTEGER}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_INTEGER = 35069L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_NORMALIZED}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_NORMALIZED = 34922L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_POINTER}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_POINTER = 34373L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_SIZE}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_SIZE = 34339L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_STRIDE}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_STRIDE = 34340L;

    /** The constant {@code VERTEX_ATTRIB_ARRAY_TYPE}. */
    @JsxConstant
    public static final long VERTEX_ATTRIB_ARRAY_TYPE = 34341L;

    /** The constant {@code VERTEX_SHADER}. */
    @JsxConstant
    public static final long VERTEX_SHADER = 35633L;

    /** The constant {@code VIEWPORT}. */
    @JsxConstant
    public static final long VIEWPORT = 2978L;

    /** The constant {@code WAIT_FAILED}. */
    @JsxConstant
    public static final long WAIT_FAILED = 37149L;

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
