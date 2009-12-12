function SC(){}
function ZD(){}
function I6(){}
function aib(){}
function fib(){}
function kib(){}
function _D(){return XI}
function hD(){return UI}
function U6(){return DL}
function dib(){return yM}
function iib(){return zM}
function nib(){return AM}
function eib(a){Xhb(this.a)}
function jib(a){Xhb(this.a)}
function oib(a){Whb(this.a)}
function cib(a,b){a.a=b;return a}
function hib(a,b){a.a=b;return a}
function mib(a,b){a.a=b;return a}
function tib(a){vbb(a.b,Uhb(a.a))}
function NC(a){!a.c&&(a.c=new ZD)}
function $C(a,b,c,d){XC();ZC(a,b,c,d);return a}
function r4b(a,b,c,d){Ep(a.a,b,c,d);return a}
function o4b(a,b){Cp(a.a,String.fromCharCode(b));return a}
function f2b(a){var b;b=h2b(a);if(isNaN(b)){throw m3b(new k3b,Tnc+a+Unc)}return b}
function Y6(){var a;while(N6){a=N6;N6=N6.b;!N6&&(O6=null);tib(a.a)}}
function XC(){XC=Fbc;NC((KC(),KC(),JC))}
function Vhb(a,b){if(b==null){h9(a.c.Kb(),bxc,false)}else{(Ip(),a.c.J).innerText=b||Vcc;h9(a.c.Kb(),bxc,true)}}
function V6(){Q6=true;P6=(S6(),new I6);Fn((Cn(),Bn),6);!!$stats&&$stats(ko(dyc,fdc,null,null));P6.Hb();!!$stats&&$stats(ko(dyc,Svc,null,null))}
function fD(a,b,c){var d,e,f;if(b==0){gD(a,b,c,a.j);_C(a,0,c);return}d=FH(Y2b(Math.log(b)/Math.log(10)));b/=Math.pow(10,d);f=a.j;if(a.g>1&&a.g>a.j){while(d%a.g!=0){b*=10;--d}f=1}else{if(a.j<1){++d;b/=10}else{for(e=1;e<a.j;++e){--d;b*=10}}}gD(a,b,c,f);_C(a,d,c)}
function h2b(a){var b=c2b;!b&&(b=c2b=/^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i);if(b.test(a)){return parseFloat(a)}else{return Number.NaN}}
function Sdb(a){var b,c;b=sH(a.a.lb(eyc),43);if(b==null){c=dH(bT,323,1,[fyc,gyc,hyc,iyc,Ywc]);a.a.mb(eyc,c);return c}else{return b}}
function Whb(b){var a,d,e,f;e=as(b.f.J,Vvc);if(x3b(e,Vcc)){(Ip(),b.c.J).innerText=cxc}else{try{f=f2b(e);d=aD(b.a,f);(Ip(),b.c.J).innerText=d||Vcc;Vhb(b,null)}catch(a){a=kT(a);if(vH(a,44)){Vhb(b,dxc)}else throw a}}}
function aD(a,b){var c,d;d=m4b(new j4b);if(isNaN(b)){Bp(d.a,Uxc);return Gp(d.a)}c=b<0||b==0&&1/b<0;q4b(d,c?a.l:a.o);if(!isFinite(b)){Bp(d.a,Vxc)}else{c&&(b=-b);b*=a.k;a.q?fD(a,b,d):gD(a,b,d,a.j)}q4b(d,c?a.m:a.p);return Gp(d.a)}
function ZC(a,b,c,d){XC();if(!c){throw q2b(new n2b,Sxc)}a.n=b;a.a=c[0];a.b=c[1];dD(a,a.n);if(!d&&a.e){a.i=c[2]&7;a.f=a.i}return a}
function _C(a,b,c){var d,e,f;Bp(c.a,Txc);if(b<0){b=-b;Bp(c.a,Ffc)}d=Vcc+b;f=d.length;for(e=f;e<a.h;++e){Bp(c.a,wmc)}for(e=0;e<f;++e){o4b(c,d.charCodeAt(e))}}
function dD(a,b){var c,d;d=0;c=m4b(new j4b);d+=cD(a,b,d,c,false);a.o=Gp(c.a);d+=eD(a,b,d,false);d+=cD(a,b,d,c,false);a.p=Gp(c.a);if(d<b.length&&b.charCodeAt(d)==59){++d;d+=cD(a,b,d,c,true);a.l=Gp(c.a);d+=eD(a,b,d,true);d+=cD(a,b,d,c,true);a.m=Gp(c.a)}else{a.l=Ffc+a.o;a.m=a.p}}
function cD(a,b,c,d,e){var f,g,h,i;r4b(d,0,Gp(d.a).length,Vcc);g=false;h=b.length;for(i=c;i<h;++i){f=b.charCodeAt(i);if(f==39){if(i+1<h&&b.charCodeAt(i+1)==39){++i;Bp(d.a,mxc)}else{g=!g}continue}if(g){Cp(d.a,String.fromCharCode(f))}else{switch(f){case 35:case 48:case 44:case 46:case 59:return i-c;case 164:a.e=true;if(i+1<h&&b.charCodeAt(i+1)==164){++i;q4b(d,a.a)}else{q4b(d,a.b)}break;case 37:if(!e){if(a.k!=1){throw q2b(new n2b,Wxc+b+Unc)}a.k=100}Bp(d.a,Xxc);break;case 8240:if(!e){if(a.k!=1){throw q2b(new n2b,Wxc+b+Unc)}a.k=1000}Bp(d.a,Yxc);break;case 45:Bp(d.a,Ffc);break;default:Cp(d.a,String.fromCharCode(f));}}}return h-c}
function Xhb(b){var a,d;switch(b.e.J.selectedIndex){case 0:b.a=(XC(),!UC&&(UC=$C(new SC,kyc,[lyc,myc,2,myc],false)),UC);LOb(b.d,b.a.n);b.d.J[vic]=!false;break;case 1:b.a=(XC(),!TC&&(TC=$C(new SC,nyc,[lyc,myc,2,myc],false)),TC);LOb(b.d,b.a.n);b.d.J[vic]=!false;break;case 2:b.a=(XC(),!WC&&(WC=$C(new SC,oyc,[lyc,myc,2,myc],false)),WC);LOb(b.d,b.a.n);b.d.J[vic]=!false;break;case 3:b.a=(XC(),!VC&&(VC=$C(new SC,pyc,[lyc,myc,2,myc],false)),VC);LOb(b.d,b.a.n);b.d.J[vic]=!false;break;case 4:b.d.J[vic]=!true;d=as(b.d.J,Vvc);try{b.a=(XC(),$C(new SC,d,[lyc,myc,2,myc],true))}catch(a){a=kT(a);if(vH(a,54)){Vhb(b,exc);return}else throw a}}Whb(b)}
function gD(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s;s=Math.pow(10,a.f);i=b.toFixed(a.f+3);q=0;l=0;h=i.indexOf(X3b(101));if(h!=-1){q=Math.floor(b)}else{f=i.indexOf(X3b(46));r=i.length;f==-1&&(f=r);f>0&&(q=f2b(i.substr(0,f-0)));if(f<r-1){l=f2b(i.substr(f+1,i.length-(f+1)));l=~~((~~Math.max(Math.min(l,2147483647),-2147483648)+500)/1000);if(l>=s){l-=s;++q}}}m=a.i>0||l>0;p=Vcc+q;n=a.e?cyc:cyc;e=a.e?lgc:lgc;g=p.length;if(q>0||d>0){for(o=g;o<d;++o){Bp(c.a,wmc)}for(o=0;o<g;++o){o4b(c,p.charCodeAt(o));g-o>1&&a.d>0&&(g-o)%a.d==1&&Bp(c.a,n)}}else !m&&Bp(c.a,wmc);(a.c||m)&&Bp(c.a,e);k=Vcc+Math.floor(l+s+0.5);j=k.length;while(k.charCodeAt(j-1)==48&&j>a.i+1){--j}for(o=1;o<j;++o){o4b(c,k.charCodeAt(o))}}
function Uhb(a){var b,c,d,e,f,g,h,i,j,k,l,m;b=sJb(new pJb,4,2);b.l[Ufc]=5;a.e=vMb(new tMb);a.e.J.style[Lfc]=Xvc;g=Sdb(a.b);for(d=g,e=0,f=d.length;e<f;++e){c=d[e];zMb(a.e,c,-1)}p9(a.e,cib(new aib,a),(Yt(),Yt(),Xt));b.Mc(0,0);h=(i=b.i.a.h.rows[0].cells[0],uIb(b,i,false),i);h.innerHTML=Zwc;GIb(b,0,1,a.e);a.d=SOb(new HOb);a.d.J.style[Lfc]=Xvc;p9(a.d,hib(new fib,a),(Cv(),Cv(),Bv));GIb(b,1,1,a.d);a.f=SOb(new HOb);a.f.J.style[Lfc]=Xvc;a.f.J[Vvc]=jyc;p9(a.f,mib(new kib,a),Bv);b.Mc(2,0);j=(k=b.i.a.h.rows[2].cells[0],uIb(b,k,false),k);j.innerHTML=_wc;GIb(b,2,1,a.f);a.c=jGb(new hGb);a.c.J.style[Lfc]=Xvc;b.Mc(3,0);l=(m=b.i.a.h.rows[3].cells[0],uIb(b,m,false),m);l.innerHTML=axc;GIb(b,3,1,a.c);Xhb(a);return b}
function eD(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p;f=-1;g=0;p=0;h=0;j=-1;k=b.length;n=c;l=true;for(;n<k&&l;++n){e=b.charCodeAt(n);switch(e){case 35:p>0?++h:++g;j>=0&&f<0&&++j;break;case 48:if(h>0){throw q2b(new n2b,Zxc+b+Unc)}++p;j>=0&&f<0&&++j;break;case 44:j=0;break;case 46:if(f>=0){throw q2b(new n2b,$xc+b+Unc)}f=g+p+h;break;case 69:if(!d){if(a.q){throw q2b(new n2b,_xc+b+Unc)}a.q=true;a.h=0}while(n+1<k&&b.charCodeAt(n+1)==48){++n;!d&&++a.h}if(!d&&g+p<1||a.h<1){throw q2b(new n2b,ayc+b+Unc)}l=false;break;default:--n;l=false;}}if(p==0&&g>0&&f>=0){m=f;m==0&&++m;h=g-m;g=m-1;p=1}if(f<0&&h>0||f>=0&&(f<g||f>g+p)||j==0){throw q2b(new n2b,byc+b+Unc)}if(d){return n-c}o=g+p+h;a.f=f>=0?o-f:0;if(f>=0){a.i=g+p-f;a.i<0&&(a.i=0)}i=f>=0?f:o;a.j=i-g;if(a.q){a.g=g+a.j;a.f==0&&a.j==0&&(a.j=1)}a.d=j>0?j:0;a.c=f==0||f==o;return n-c}
var pyc='#,##0%',kyc='#,##0.###',Xxc='%',cyc=',',oyc='0.###E0',jyc='31415926535.897932',tyc='AsyncLoader6',gyc='Currency',uyc='CwNumberFormat$1',vyc='CwNumberFormat$2',wyc='CwNumberFormat$3',fyc='Decimal',Txc='E',ayc='Malformed exponential pattern "',byc='Malformed pattern "',$xc='Multiple decimal separators in pattern "',_xc='Multiple exponential symbols in pattern "',ryc='NumberConstantsImpl_',syc='NumberFormat',iyc='Percent',hyc='Scientific',Wxc='Too many percent/per mille characters in pattern "',myc='US$',lyc='USD',Zxc="Unexpected '0' in pattern \"",Sxc='Unknown currency code',eyc='cwNumberFormatPatterns',dyc='runCallbacks6',nyc='\xA4#,##0.00;(\xA4#,##0.00)',Vxc='\u0221',Yxc='\u2030',Uxc='\uFFFD';_=SC.prototype=new Bl;_.gC=hD;_.tI=0;_.a=null;_.b=null;_.c=false;_.d=3;_.e=false;_.f=3;_.g=40;_.h=0;_.i=0;_.j=1;_.k=1;_.l=Ffc;_.m=Vcc;_.n=null;_.o=Vcc;_.p=Vcc;_.q=false;var TC=null,UC=null,VC=null,WC=null;_=ZD.prototype=new Bl;_.gC=_D;_.tI=0;_=I6.prototype=new J6;_.gC=U6;_.Hb=Y6;_.tI=0;_=aib.prototype=new Bl;_.gC=dib;_.Y=eib;_.tI=101;_.a=null;_=fib.prototype=new Bl;_.gC=iib;_._=jib;_.tI=102;_.a=null;_=kib.prototype=new Bl;_.gC=nib;_._=oib;_.tI=103;_.a=null;var c2b=null;var XI=R1b(qyc,ryc),UI=R1b(xpc,syc),DL=R1b(Dpc,tyc),yM=R1b(Grc,uyc),zM=R1b(Grc,vyc),AM=R1b(Grc,wyc);V6();