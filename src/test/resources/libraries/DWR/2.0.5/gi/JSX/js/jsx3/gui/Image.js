/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Image",jsx3.gui.Block,[],function(s,h){h.paint=function(){this.applyDynamicProperties();var V=this.RL(true);var Dc=this.getUriResolver().resolveURI(this.jsxsrc);var Nb=this.getWidth()!=null?" width=\""+V.XK()+"\"":"";var F=this.getHeight()!=null?" height=\""+V.P5()+"\"":"";return this.jsxsuper("<img unselectable=\"on\" class=\"jsx30image\" src=\""+Dc+"\""+Nb+F+"/>");};h.onSetChild=function(p){return false;};h.k7=function(p,m,b){this.B_(p,m,b,1);};h.getRenderedWidth=function(){var qc=this.getRendered();return qc&&qc.childNodes[0]?qc.childNodes[0].width:null;};h.getRenderedHeight=function(){var Eb=this.getRendered();return Eb&&Eb.childNodes[0]?Eb.childNodes[0].height:null;};h.getSrc=function(){return this.jsxsrc;};h.setSrc=function(b){this.jsxsrc=b;return this;};});
