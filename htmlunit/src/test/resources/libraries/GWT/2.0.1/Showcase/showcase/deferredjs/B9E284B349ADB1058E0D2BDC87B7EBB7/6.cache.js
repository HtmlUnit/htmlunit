function ME(){}
function TF(){}
function J8(){}
function Yjb(){}
function bkb(){}
function gkb(){}
function VF(){return dL}
function bF(){return aL}
function V8(){return LN}
function _jb(){return GO}
function ekb(){return HO}
function jkb(){return IO}
function akb(a){Tjb(this.b)}
function fkb(a){Tjb(this.b)}
function kkb(a){Sjb(this.b)}
function dkb(a,b){a.b=b;return a}
function $jb(a,b){a.b=b;return a}
function ikb(a,b){a.b=b;return a}
function pkb(a){vdb(a.c,Qjb(a.b))}
function HE(a){!a.d&&(a.d=new TF)}
function UE(a,b,c,d){RE();TE(a,b,c,d);return a}
function p4b(a,b,c,d){$p(a.b,b,c,d);return a}
function RE(){RE=xbc;HE((EE(),EE(),DE))}
function d2b(a){var b;b=f2b(a);if(isNaN(b)){throw k3b(new i3b,nnc+a+onc)}return b}
function TE(a,b,c,d){RE();if(!c){throw o2b(new l2b,uxc)}a.o=b;a.b=c[0];a.c=c[1];ZE(a,a.o);if(!d&&a.f){a.j=c[2]&7;a.g=a.j}return a}
function Rjb(a,b){if(b==null){hbb(a.d.Kb(),Fwc,false)}else{Hq((cq(),a.d.K),b);hbb(a.d.Kb(),Fwc,true)}}
function W8(){R8=true;Q8=(T8(),new J8);Tn((Qn(),Pn),6);!!$stats&&$stats(yo(Hxc,_cc,null,null));Q8.Hb();!!$stats&&$stats(yo(Hxc,uvc,null,null))}
function Z8(){var a;while(O8){a=O8;O8=O8.c;!O8&&(P8=null);pkb(a.b)}}
function f2b(a){var b=a2b;!b&&(b=a2b=/^\s*[+-]?\d*\.?\d*([eE][+-]?\d+)?\s*$/i);if(b.test(a)){return parseFloat(a)}else{return Number.NaN}}
function Sfb(a){var b,c;b=fJ(a.b.mb(Ixc),48);if(b==null){c=SI(cV,362,1,[Jxc,Kxc,Lxc,Mxc,Awc]);a.b.nb(Ixc,c);return c}else{return b}}
function Sjb(b){var a,d,e,f;e=ms(b.g.K,xvc);if(v3b(e,Pcc)){Hq((cq(),b.d.K),Gwc)}else{try{f=d2b(e);d=WE(b.b,f);Hq((cq(),b.d.K),d);Rjb(b,null)}catch(a){a=lV(a);if(iJ(a,49)){Rjb(b,Hwc)}else throw a}}}
function YE(a,b,c,d,e){var f,g,h,i;p4b(d,0,d.b.b.length,Pcc);g=false;h=b.length;for(i=c;i<h;++i){f=b.charCodeAt(i);if(f==39){if(i+1<h&&b.charCodeAt(i+1)==39){++i;d.b.b+=Qwc}else{g=!g}continue}if(g){d.b.b+=String.fromCharCode(f)}else{switch(f){case 35:case 48:case 44:case 46:case 59:return i-c;case 164:a.f=true;if(i+1<h&&b.charCodeAt(i+1)==164){++i;o4b(d,a.b)}else{o4b(d,a.c)}break;case 37:if(!e){if(a.l!=1){throw o2b(new l2b,yxc+b+onc)}a.l=100}d.b.b+=zxc;break;case 8240:if(!e){if(a.l!=1){throw o2b(new l2b,yxc+b+onc)}a.l=1000}d.b.b+=Axc;break;case 45:d.b.b+=Hfc;break;default:d.b.b+=String.fromCharCode(f);}}}return h-c}
function VE(a,b,c){var d,e,f;c.b.b+=vxc;if(b<0){b=-b;c.b.b+=Hfc}d=Pcc+b;f=d.length;for(e=f;e<a.i;++e){c.b.b+=omc}for(e=0;e<f;++e){m4b(c,d.charCodeAt(e))}}
function _E(a,b,c){var d,e,f;if(b==0){aF(a,b,c,a.k);VE(a,0,c);return}d=sJ(W2b(Math.log(b)/Math.log(10)));b/=Math.pow(10,d);f=a.k;if(a.h>1&&a.h>a.k){while(d%a.h!=0){b*=10;--d}f=1}else{if(a.k<1){++d;b/=10}else{for(e=1;e<a.k;++e){--d;b*=10}}}aF(a,b,c,f);VE(a,d,c)}
function WE(a,b){var c,d;d=k4b(new h4b);if(isNaN(b)){d.b.b+=wxc;return d.b.b}c=b<0||b==0&&1/b<0;o4b(d,c?a.m:a.p);if(!isFinite(b)){d.b.b+=xxc}else{c&&(b=-b);b*=a.l;a.r?_E(a,b,d):aF(a,b,d,a.k)}o4b(d,c?a.n:a.q);return d.b.b}
function ZE(a,b){var c,d;d=0;c=k4b(new h4b);d+=YE(a,b,d,c,false);a.p=c.b.b;d+=$E(a,b,d,false);d+=YE(a,b,d,c,false);a.q=c.b.b;if(d<b.length&&b.charCodeAt(d)==59){++d;d+=YE(a,b,d,c,true);a.m=c.b.b;d+=$E(a,b,d,true);d+=YE(a,b,d,c,true);a.n=c.b.b}else{a.m=Hfc+a.p;a.n=a.q}}
function Tjb(b){var a,d;switch(b.f.K.selectedIndex){case 0:b.b=(RE(),!OE&&(OE=UE(new ME,Oxc,[Pxc,Qxc,2,Qxc],false)),OE);LPb(b.e,b.b.o);b.e.K[yic]=!false;break;case 1:b.b=(RE(),!NE&&(NE=UE(new ME,Rxc,[Pxc,Qxc,2,Qxc],false)),NE);LPb(b.e,b.b.o);b.e.K[yic]=!false;break;case 2:b.b=(RE(),!QE&&(QE=UE(new ME,Sxc,[Pxc,Qxc,2,Qxc],false)),QE);LPb(b.e,b.b.o);b.e.K[yic]=!false;break;case 3:b.b=(RE(),!PE&&(PE=UE(new ME,Txc,[Pxc,Qxc,2,Qxc],false)),PE);LPb(b.e,b.b.o);b.e.K[yic]=!false;break;case 4:b.e.K[yic]=!true;d=ms(b.e.K,xvc);try{b.b=(RE(),UE(new ME,d,[Pxc,Qxc,2,Qxc],true))}catch(a){a=lV(a);if(iJ(a,59)){Rjb(b,Iwc);return}else throw a}}Sjb(b)}
function Qjb(a){var b,c,d,e,f,g,h,i,j,k,l,m;b=KKb(new HKb,4,2);b.m[Vfc]=5;a.f=xNb(new vNb);a.f.K.style[Nfc]=zvc;g=Sfb(a.c);for(d=g,e=0,f=d.length;e<f;++e){c=d[e];BNb(a.f,c,-1)}pbb(a.f,$jb(new Yjb,a),(Uv(),Uv(),Tv));b.Jc(0,0);h=(i=b.j.b.i.rows[0].cells[0],MJb(b,i,false),i);h.innerHTML=Bwc;YJb(b,0,1,a.f);a.e=SPb(new HPb);a.e.K.style[Nfc]=zvc;pbb(a.e,dkb(new bkb,a),(yx(),yx(),xx));YJb(b,1,1,a.e);a.g=SPb(new HPb);a.g.K.style[Nfc]=zvc;a.g.K[xvc]=Nxc;pbb(a.g,ikb(new gkb,a),xx);b.Jc(2,0);j=(k=b.j.b.i.rows[2].cells[0],MJb(b,k,false),k);j.innerHTML=Dwc;YJb(b,2,1,a.g);a.d=tHb(new rHb);a.d.K.style[Nfc]=zvc;b.Jc(3,0);l=(m=b.j.b.i.rows[3].cells[0],MJb(b,m,false),m);l.innerHTML=Ewc;YJb(b,3,1,a.d);Tjb(a);return b}
function aF(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s;s=Math.pow(10,a.g);i=b.toFixed(a.g+3);q=0;l=0;h=i.indexOf(V3b(101));if(h!=-1){q=Math.floor(b)}else{f=i.indexOf(V3b(46));r=i.length;f==-1&&(f=r);f>0&&(q=d2b(i.substr(0,f-0)));if(f<r-1){l=d2b(i.substr(f+1,i.length-(f+1)));l=~~((~~Math.max(Math.min(l,2147483647),-2147483648)+500)/1000);if(l>=s){l-=s;++q}}}m=a.j>0||l>0;p=Pcc+q;n=a.f?Gxc:Gxc;e=a.f?ngc:ngc;g=p.length;if(q>0||d>0){for(o=g;o<d;++o){c.b.b+=omc}for(o=0;o<g;++o){m4b(c,p.charCodeAt(o));g-o>1&&a.e>0&&(g-o)%a.e==1&&(c.b.b+=n,undefined)}}else !m&&(c.b.b+=omc,undefined);(a.d||m)&&(c.b.b+=e,undefined);k=Pcc+Math.floor(l+s+0.5);j=k.length;while(k.charCodeAt(j-1)==48&&j>a.j+1){--j}for(o=1;o<j;++o){m4b(c,k.charCodeAt(o))}}
function $E(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p;f=-1;g=0;p=0;h=0;j=-1;k=b.length;n=c;l=true;for(;n<k&&l;++n){e=b.charCodeAt(n);switch(e){case 35:p>0?++h:++g;j>=0&&f<0&&++j;break;case 48:if(h>0){throw o2b(new l2b,Bxc+b+onc)}++p;j>=0&&f<0&&++j;break;case 44:j=0;break;case 46:if(f>=0){throw o2b(new l2b,Cxc+b+onc)}f=g+p+h;break;case 69:if(!d){if(a.r){throw o2b(new l2b,Dxc+b+onc)}a.r=true;a.i=0}while(n+1<k&&b.charCodeAt(n+1)==48){++n;!d&&++a.i}if(!d&&g+p<1||a.i<1){throw o2b(new l2b,Exc+b+onc)}l=false;break;default:--n;l=false;}}if(p==0&&g>0&&f>=0){m=f;m==0&&++m;h=g-m;g=m-1;p=1}if(f<0&&h>0||f>=0&&(f<g||f>g+p)||j==0){throw o2b(new l2b,Fxc+b+onc)}if(d){return n-c}o=g+p+h;a.g=f>=0?o-f:0;if(f>=0){a.j=g+p-f;a.j<0&&(a.j=0)}i=f>=0?f:o;a.k=i-g;if(a.r){a.h=g+a.k;a.g==0&&a.k==0&&(a.k=1)}a.e=j>0?j:0;a.d=f==0||f==o;return n-c}
var Txc='#,##0%',Oxc='#,##0.###',zxc='%',Gxc=',',Sxc='0.###E0',Nxc='31415926535.897932',Xxc='AsyncLoader6',Kxc='Currency',Yxc='CwNumberFormat$1',Zxc='CwNumberFormat$2',$xc='CwNumberFormat$3',Jxc='Decimal',vxc='E',Exc='Malformed exponential pattern "',Fxc='Malformed pattern "',Cxc='Multiple decimal separators in pattern "',Dxc='Multiple exponential symbols in pattern "',Vxc='NumberConstantsImpl_',Wxc='NumberFormat',Mxc='Percent',Lxc='Scientific',yxc='Too many percent/per mille characters in pattern "',Qxc='US$',Pxc='USD',Bxc="Unexpected '0' in pattern \"",uxc='Unknown currency code',Ixc='cwNumberFormatPatterns',Hxc='runCallbacks6',Rxc='\xA4#,##0.00;(\xA4#,##0.00)',xxc='\u0221',Axc='\u2030',wxc='\uFFFD';_=ME.prototype=new Pl;_.gC=bF;_.tI=0;_.b=null;_.c=null;_.d=false;_.e=3;_.f=false;_.g=3;_.h=40;_.i=0;_.j=0;_.k=1;_.l=1;_.m=Hfc;_.n=Pcc;_.o=null;_.p=Pcc;_.q=Pcc;_.r=false;var NE=null,OE=null,PE=null,QE=null;_=TF.prototype=new Pl;_.gC=VF;_.tI=0;_=J8.prototype=new K8;_.gC=V8;_.Hb=Z8;_.tI=0;_=Yjb.prototype=new Pl;_.gC=_jb;_.Z=akb;_.tI=121;_.b=null;_=bkb.prototype=new Pl;_.gC=ekb;_.ab=fkb;_.tI=122;_.b=null;_=gkb.prototype=new Pl;_.gC=jkb;_.ab=kkb;_.tI=123;_.b=null;var a2b=null;var dL=P1b(Uxc,Vxc),aL=P1b(epc,Wxc),LN=P1b(kpc,Xxc),GO=P1b(nrc,Yxc),HO=P1b(nrc,Zxc),IO=P1b(nrc,$xc);W8();