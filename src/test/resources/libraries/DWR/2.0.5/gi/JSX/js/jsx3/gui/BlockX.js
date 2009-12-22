/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.xml.Cacheable","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.BlockX",jsx3.gui.Block,[jsx3.xml.Cacheable],function(d,j){j.init=function(k,n,i,f,c){this.jsxsuper(k,n,i,f,c);};j.paint=function(){this.applyDynamicProperties();return this.jsxsuper(this.doTransform());};j.onDestroy=function(b){this.jsxsuper(b);this.onDestroyCached(b);};d.getVersion=function(){return "3.00.00";};});jsx3.BlockX=jsx3.gui.BlockX;
