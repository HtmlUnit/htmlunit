function LC(){}
function SD(){}
function y6(){}
function Rhb(){}
function Whb(){}
function _hb(){}
function UD(){return UI}
function aD(){return RI}
function K6(){return AL}
function Uhb(){return vM}
function Zhb(){return wM}
function cib(){return xM}
function Vhb(a){Mhb(this.b)}
function $hb(a){Mhb(this.b)}
function dib(a){Lhb(this.b)}
function Yhb(a,b){a.b=b;return a}
function Thb(a,b){a.b=b;return a}
function bib(a,b){a.b=b;return a}
function iib(a){kbb(a.c,Jhb(a.b))}
function GC(a){!a.d&&(a.d=new SD)}
function TC(a,b,c,d){QC();SC(a,b,c,d);return a}
function O2b(a,b,c,d){Yp(a.b,b,c,d);return a}
function L2b(a,b){a.b.b+=String.fromCharCode(b);return a}
function C0b(a){var b;b=E0b(a);if(isNaN(b)){throw J1b(new H1b,Mlc+a+Nlc)}return b}
function O6(){var a;while(D6){a=D6;D6=D6.c;!D6&&(E6=null);iib(a.b)}}
function QC(){QC=aac;GC((DC(),DC(),CC))}
function Khb(a,b){if(b==null){Y8(a.d.Pb(),$uc,false)}else{yq((aq(),a.d.K),b);Y8(a.d.Pb(),$uc,true)}}
function Kdb(a){var b,c;b=lH(a.b.qb(bwc),43);if(b==null){c=YG(TS,319,1,[cwc,dwc,ewc,fwc,Vuc]);a.b.rb(bwc,c);return c}else{return b}}
function Lhb(b){var a,d,e,f;e=hs(b.g.K,Stc);if(U1b(e,qbc)){yq((aq(),b.d.K),_uc)}else{try{f=C0b(e);d=VC(b.b,f);yq((aq(),b.d.K),d);Khb(b,null)}catch(a){a=aT(a);if(oH(a,44)){Khb(b,avc)}else throw a}}}
function E0b(a){var b=z0b;!b&&(b=z0b=/^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i);if(b.test(a)){return parseFloat(a)}else{return Number.NaN}}
function VC(a,b){var c,d;d=J2b(new G2b);if(isNaN(b)){d.b.b+=Rvc;return d.b.b}c=b<0||b==0&&1/b<0;N2b(d,c?a.m:a.p);if(!isFinite(b)){d.b.b+=Svc}else{c&&(b=-b);b*=a.l;a.r?$C(a,b,d):_C(a,b,d,a.k)}N2b(d,c?a.n:a.q);return d.b.b}
function SC(a,b,c,d){QC();if(!c){throw N0b(new K0b,Pvc)}a.o=b;a.b=c[0];a.c=c[1];YC(a,a.o);if(!d&&a.f){a.j=c[2]&7;a.g=a.j}return a}
function UC(a,b,c){var d,e,f;c.b.b+=Qvc;if(b<0){b=-b;c.b.b+=cec}d=qbc+b;f=d.length;for(e=f;e<a.i;++e){c.b.b+=Lkc}for(e=0;e<f;++e){L2b(c,d.charCodeAt(e))}}
function $C(a,b,c){var d,e,f;if(b==0){_C(a,b,c,a.k);UC(a,0,c);return}d=yH(t1b(Math.log(b)/Math.log(10)));b/=Math.pow(10,d);f=a.k;if(a.h>1&&a.h>a.k){while(d%a.h!=0){b*=10;--d}f=1}else{if(a.k<1){++d;b/=10}else{for(e=1;e<a.k;++e){--d;b*=10}}}_C(a,b,c,f);UC(a,d,c)}
function L6(){G6=true;F6=(I6(),new y6);Cn((zn(),yn),6);!!$stats&&$stats(ho(awc,Cbc,null,null));F6.Mb();!!$stats&&$stats(ho(awc,Ptc,null,null))}
function YC(a,b){var c,d;d=0;c=J2b(new G2b);d+=XC(a,b,d,c,false);a.p=c.b.b;d+=ZC(a,b,d,false);d+=XC(a,b,d,c,false);a.q=c.b.b;if(d<b.length&&b.charCodeAt(d)==59){++d;d+=XC(a,b,d,c,true);a.m=c.b.b;d+=ZC(a,b,d,true);d+=XC(a,b,d,c,true);a.n=c.b.b}else{a.m=cec+a.p;a.n=a.q}}
function XC(a,b,c,d,e){var f,g,h,i;O2b(d,0,d.b.b.length,qbc);g=false;h=b.length;for(i=c;i<h;++i){f=b.charCodeAt(i);if(f==39){if(i+1<h&&b.charCodeAt(i+1)==39){++i;d.b.b+=jvc}else{g=!g}continue}if(g){d.b.b+=String.fromCharCode(f)}else{switch(f){case 35:case 48:case 44:case 46:case 59:return i-c;case 164:a.f=true;if(i+1<h&&b.charCodeAt(i+1)==164){++i;N2b(d,a.b)}else{N2b(d,a.c)}break;case 37:if(!e){if(a.l!=1){throw N0b(new K0b,Tvc+b+Nlc)}a.l=100}d.b.b+=Uvc;break;case 8240:if(!e){if(a.l!=1){throw N0b(new K0b,Tvc+b+Nlc)}a.l=1000}d.b.b+=Vvc;break;case 45:d.b.b+=cec;break;default:d.b.b+=String.fromCharCode(f);}}}return h-c}
function Mhb(b){var a,d;switch(b.f.K.selectedIndex){case 0:b.b=(QC(),!NC&&(NC=TC(new LC,hwc,[iwc,jwc,2,jwc],false)),NC);KNb(b.e,b.b.o);b.e.K[Vgc]=!false;break;case 1:b.b=(QC(),!MC&&(MC=TC(new LC,kwc,[iwc,jwc,2,jwc],false)),MC);KNb(b.e,b.b.o);b.e.K[Vgc]=!false;break;case 2:b.b=(QC(),!PC&&(PC=TC(new LC,lwc,[iwc,jwc,2,jwc],false)),PC);KNb(b.e,b.b.o);b.e.K[Vgc]=!false;break;case 3:b.b=(QC(),!OC&&(OC=TC(new LC,mwc,[iwc,jwc,2,jwc],false)),OC);KNb(b.e,b.b.o);b.e.K[Vgc]=!false;break;case 4:b.e.K[Vgc]=!true;d=hs(b.e.K,Stc);try{b.b=(QC(),TC(new LC,d,[iwc,jwc,2,jwc],true))}catch(a){a=aT(a);if(oH(a,54)){Khb(b,bvc);return}else throw a}}Lhb(b)}
function Jhb(a){var b,c,d,e,f,g,h,i,j,k,l,m;b=DIb(new AIb,4,2);b.m[qec]=5;a.f=uLb(new sLb);a.f.K.style[iec]=Utc;g=Kdb(a.c);for(d=g,e=0,f=d.length;e<f;++e){c=d[e];yLb(a.f,c,-1)}e9(a.f,Thb(new Rhb,a),(Rt(),Rt(),Qt));b.Oc(0,0);h=(i=b.j.b.i.rows[0].cells[0],CHb(b,i,false),i);h.innerHTML=Wuc;OHb(b,0,1,a.f);a.e=RNb(new FNb);a.e.K.style[iec]=Utc;e9(a.e,Yhb(new Whb,a),(vv(),vv(),uv));OHb(b,1,1,a.e);a.g=RNb(new FNb);a.g.K.style[iec]=Utc;a.g.K[Stc]=gwc;e9(a.g,bib(new _hb,a),uv);b.Oc(2,0);j=(k=b.j.b.i.rows[2].cells[0],CHb(b,k,false),k);j.innerHTML=Yuc;OHb(b,2,1,a.g);a.d=rFb(new pFb);a.d.K.style[iec]=Utc;b.Oc(3,0);l=(m=b.j.b.i.rows[3].cells[0],CHb(b,m,false),m);l.innerHTML=Zuc;OHb(b,3,1,a.d);Mhb(a);return b}
function _C(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s;s=Math.pow(10,a.g);i=b.toFixed(a.g+3);q=0;l=0;h=i.indexOf(s2b(101));if(h!=-1){q=Math.floor(b)}else{f=i.indexOf(s2b(46));r=i.length;f==-1&&(f=r);f>0&&(q=C0b(i.substr(0,f-0)));if(f<r-1){l=C0b(i.substr(f+1,i.length-(f+1)));l=~~((~~Math.max(Math.min(l,2147483647),-2147483648)+500)/1000);if(l>=s){l-=s;++q}}}m=a.j>0||l>0;p=qbc+q;n=a.f?_vc:_vc;e=a.f?Kec:Kec;g=p.length;if(q>0||d>0){for(o=g;o<d;++o){c.b.b+=Lkc}for(o=0;o<g;++o){L2b(c,p.charCodeAt(o));g-o>1&&a.e>0&&(g-o)%a.e==1&&(c.b.b+=n,undefined)}}else !m&&(c.b.b+=Lkc,undefined);(a.d||m)&&(c.b.b+=e,undefined);k=qbc+Math.floor(l+s+0.5);j=k.length;while(k.charCodeAt(j-1)==48&&j>a.j+1){--j}for(o=1;o<j;++o){L2b(c,k.charCodeAt(o))}}
function ZC(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p;f=-1;g=0;p=0;h=0;j=-1;k=b.length;n=c;l=true;for(;n<k&&l;++n){e=b.charCodeAt(n);switch(e){case 35:p>0?++h:++g;j>=0&&f<0&&++j;break;case 48:if(h>0){throw N0b(new K0b,Wvc+b+Nlc)}++p;j>=0&&f<0&&++j;break;case 44:j=0;break;case 46:if(f>=0){throw N0b(new K0b,Xvc+b+Nlc)}f=g+p+h;break;case 69:if(!d){if(a.r){throw N0b(new K0b,Yvc+b+Nlc)}a.r=true;a.i=0}while(n+1<k&&b.charCodeAt(n+1)==48){++n;!d&&++a.i}if(!d&&g+p<1||a.i<1){throw N0b(new K0b,Zvc+b+Nlc)}l=false;break;default:--n;l=false;}}if(p==0&&g>0&&f>=0){m=f;m==0&&++m;h=g-m;g=m-1;p=1}if(f<0&&h>0||f>=0&&(f<g||f>g+p)||j==0){throw N0b(new K0b,$vc+b+Nlc)}if(d){return n-c}o=g+p+h;a.g=f>=0?o-f:0;if(f>=0){a.j=g+p-f;a.j<0&&(a.j=0)}i=f>=0?f:o;a.k=i-g;if(a.r){a.h=g+a.k;a.g==0&&a.k==0&&(a.k=1)}a.e=j>0?j:0;a.d=f==0||f==o;return n-c}
var mwc='#,##0%',hwc='#,##0.###',Uvc='%',_vc=',',lwc='0.###E0',gwc='31415926535.897932',qwc='AsyncLoader6',dwc='Currency',rwc='CwNumberFormat$1',swc='CwNumberFormat$2',twc='CwNumberFormat$3',cwc='Decimal',Qvc='E',Zvc='Malformed exponential pattern "',$vc='Malformed pattern "',Xvc='Multiple decimal separators in pattern "',Yvc='Multiple exponential symbols in pattern "',owc='NumberConstantsImpl_',pwc='NumberFormat',fwc='Percent',ewc='Scientific',Tvc='Too many percent/per mille characters in pattern "',jwc='US$',iwc='USD',Wvc="Unexpected '0' in pattern \"",Pvc='Unknown currency code',bwc='cwNumberFormatPatterns',awc='runCallbacks6',kwc='\xA4#,##0.00;(\xA4#,##0.00)',Svc='\u0221',Vvc='\u2030',Rvc='\uFFFD';_=LC.prototype=new yl;_.gC=aD;_.tI=0;_.b=null;_.c=null;_.d=false;_.e=3;_.f=false;_.g=3;_.h=40;_.i=0;_.j=0;_.k=1;_.l=1;_.m=cec;_.n=qbc;_.o=null;_.p=qbc;_.q=qbc;_.r=false;var MC=null,NC=null,OC=null,PC=null;_=SD.prototype=new yl;_.gC=UD;_.tI=0;_=y6.prototype=new z6;_.gC=K6;_.Mb=O6;_.tI=0;_=Rhb.prototype=new yl;_.gC=Uhb;_.bb=Vhb;_.tI=101;_.b=null;_=Whb.prototype=new yl;_.gC=Zhb;_.eb=$hb;_.tI=102;_.b=null;_=_hb.prototype=new yl;_.gC=cib;_.eb=dib;_.tI=103;_.b=null;var z0b=null;var UI=m0b(nwc,owc),RI=m0b(unc,pwc),AL=m0b(Anc,qwc),vM=m0b(Dpc,rwc),wM=m0b(Dpc,swc),xM=m0b(Dpc,twc);L6();