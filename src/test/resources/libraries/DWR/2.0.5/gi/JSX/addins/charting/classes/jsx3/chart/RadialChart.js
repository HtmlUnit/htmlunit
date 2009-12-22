/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.Chart");jsx3.Class.defineClass("jsx3.chart.RadialChart",jsx3.chart.Chart,null,function(m,a){a.init=function(c,g,j,d,k){this.jsxsuper(c,g,j,d,k);};a.updateView=function(){this.jsxsuper();var uc=this.M4();var ic=uc.getWidth();var nc=uc.getHeight();var Tb=uc.getPaddingDimensions();var Hb=[Tb[3],Tb[0],ic-(Tb[3]+Tb[1]),nc-(Tb[0]+Tb[2])];var Mb=this.aO();for(var sc=0;sc<Mb.length;sc++){Mb[sc].setDimensions(Hb);}};m.degreesToRadians=function(f){return Math.modpos(2*Math.PI/360*(-1*f+90),2*Math.PI);};m.getVersion=function(){return jsx3.chart.q2;};});
