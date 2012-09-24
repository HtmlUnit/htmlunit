function SE(){}
function ZF(){}
function U8(){}
function ikb(){}
function nkb(){}
function skb(){}
function _F(){return iL}
function hF(){return fL}
function e9(){return QN}
function lkb(){return LO}
function qkb(){return MO}
function vkb(){return NO}
function mkb(a){dkb(this.a)}
function rkb(a){dkb(this.a)}
function wkb(a){ckb(this.a)}
function kkb(a,b){a.a=b;return a}
function pkb(a,b){a.a=b;return a}
function ukb(a,b){a.a=b;return a}
function Bkb(a){Hdb(a.b,akb(a.a))}
function NE(a){!a.c&&(a.c=new ZF)}
function $E(a,b,c,d){XE();ZE(a,b,c,d);return a}
function A5b(a,b,c,d){Vp(a.a,b,c,d);return a}
function XE(){XE=Icc;NE((KE(),KE(),JE))}
function o3b(a){var b;b=q3b(a);if(isNaN(b)){throw v4b(new t4b,Qoc+a+Roc)}return b}
function ZE(a,b,c,d){XE();if(!c){throw z3b(new w3b,$yc)}a.n=b;a.a=c[0];a.b=c[1];dF(a,a.n);if(!d&&a.e){a.i=c[2]&7;a.f=a.i}return a}
function i9(){var a;while(Z8){a=Z8;Z8=Z8.b;!Z8&&($8=null);Bkb(a.a)}}
function cgb(a){var b,c;b=lJ(a.a.lb(mzc),48);if(b==null){c=YI(nV,366,1,[nzc,ozc,pzc,qzc,eyc]);a.a.mb(mzc,c);return c}else{return b}}
function bkb(a,b){if(b==null){tbb(a.c.Jb(),jyc,false)}else{(Zp(),a.c.J).innerText=b||$dc;tbb(a.c.Jb(),jyc,true)}}
function f9(){a9=true;_8=(c9(),new U8);Xn((Un(),Tn),6);!!$stats&&$stats(Co(lzc,kec,null,null));_8.Gb();!!$stats&&$stats(Co(lzc,$wc,null,null))}
function fF(a,b,c){var d,e,f;if(b==0){gF(a,b,c,a.j);_E(a,0,c);return}d=yJ(f4b(Math.log(b)/Math.log(10)));b/=Math.pow(10,d);f=a.j;if(a.g>1&&a.g>a.j){while(d%a.g!=0){b*=10;--d}f=1}else{if(a.j<1){++d;b/=10}else{for(e=1;e<a.j;++e){--d;b*=10}}}gF(a,b,c,f);_E(a,d,c)}
function q3b(a){var b=l3b;!b&&(b=l3b=/^\s*[+-]?\d*\.?\d*([eE][+-]?\d+)?\s*$/i);if(b.test(a)){return parseFloat(a)}else{return Number.NaN}}
function dF(a,b){var c,d;d=0;c=v5b(new s5b);d+=cF(a,b,d,c,false);a.o=Xp(c.a);d+=eF(a,b,d,false);d+=cF(a,b,d,c,false);a.p=Xp(c.a);if(d<b.length&&b.charCodeAt(d)==59){++d;d+=cF(a,b,d,c,true);a.l=Xp(c.a);d+=eF(a,b,d,true);d+=cF(a,b,d,c,true);a.m=Xp(c.a)}else{a.l=Vgc+a.o;a.m=a.p}}
function _E(a,b,c){var d,e,f;Sp(c.a,_yc);if(b<0){b=-b;Sp(c.a,Vgc)}d=$dc+b;f=d.length;for(e=f;e<a.h;++e){Sp(c.a,Qnc)}for(e=0;e<f;++e){x5b(c,d.charCodeAt(e))}}
function cF(a,b,c,d,e){var f,g,h,i;A5b(d,0,Xp(d.a).length,$dc);g=false;h=b.length;for(i=c;i<h;++i){f=b.charCodeAt(i);if(f==39){if(i+1<h&&b.charCodeAt(i+1)==39){++i;Sp(d.a,uyc)}else{g=!g}continue}if(g){Tp(d.a,String.fromCharCode(f))}else{switch(f){case 35:case 48:case 44:case 46:case 59:return i-c;case 164:a.e=true;if(i+1<h&&b.charCodeAt(i+1)==164){++i;z5b(d,a.a)}else{z5b(d,a.b)}break;case 37:if(!e){if(a.k!=1){throw z3b(new w3b,czc+b+Roc)}a.k=100}Sp(d.a,dzc);break;case 8240:if(!e){if(a.k!=1){throw z3b(new w3b,czc+b+Roc)}a.k=1000}Sp(d.a,ezc);break;case 45:Sp(d.a,Vgc);break;default:Tp(d.a,String.fromCharCode(f));}}}return h-c}
function ckb(b){var a,d,e,f;e=qs(b.f.J,bxc);if(G4b(e,$dc)){(Zp(),b.c.J).innerText=kyc}else{try{f=o3b(e);d=aF(b.a,f);(Zp(),b.c.J).innerText=d||$dc;bkb(b,null)}catch(a){a=wV(a);if(oJ(a,49)){bkb(b,lyc)}else throw a}}}
function aF(a,b){var c,d;d=v5b(new s5b);if(isNaN(b)){Sp(d.a,azc);return Xp(d.a)}c=b<0||b==0&&1/b<0;z5b(d,c?a.l:a.o);if(!isFinite(b)){Sp(d.a,bzc)}else{c&&(b=-b);b*=a.k;a.q?fF(a,b,d):gF(a,b,d,a.j)}z5b(d,c?a.m:a.p);return Xp(d.a)}
function dkb(b){var a,d;switch(b.e.J.selectedIndex){case 0:b.a=(XE(),!UE&&(UE=$E(new SE,szc,[tzc,uzc,2,uzc],false)),UE);wQb(b.d,b.a.n);b.d.J[Mjc]=!false;break;case 1:b.a=(XE(),!TE&&(TE=$E(new SE,vzc,[tzc,uzc,2,uzc],false)),TE);wQb(b.d,b.a.n);b.d.J[Mjc]=!false;break;case 2:b.a=(XE(),!WE&&(WE=$E(new SE,wzc,[tzc,uzc,2,uzc],false)),WE);wQb(b.d,b.a.n);b.d.J[Mjc]=!false;break;case 3:b.a=(XE(),!VE&&(VE=$E(new SE,xzc,[tzc,uzc,2,uzc],false)),VE);wQb(b.d,b.a.n);b.d.J[Mjc]=!false;break;case 4:b.d.J[Mjc]=!true;d=qs(b.d.J,bxc);try{b.a=(XE(),$E(new SE,d,[tzc,uzc,2,uzc],true))}catch(a){a=wV(a);if(oJ(a,59)){bkb(b,myc);return}else throw a}}ckb(b)}
function gF(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s;s=Math.pow(10,a.f);i=b.toFixed(a.f+3);q=0;l=0;h=i.indexOf(e5b(101));if(h!=-1){q=Math.floor(b)}else{f=i.indexOf(e5b(46));r=i.length;f==-1&&(f=r);f>0&&(q=o3b(i.substr(0,f-0)));if(f<r-1){l=o3b(i.substr(f+1,i.length-(f+1)));l=~~((~~Math.max(Math.min(l,2147483647),-2147483648)+500)/1000);if(l>=s){l-=s;++q}}}m=a.i>0||l>0;p=$dc+q;n=a.e?kzc:kzc;e=a.e?Bhc:Bhc;g=p.length;if(q>0||d>0){for(o=g;o<d;++o){Sp(c.a,Qnc)}for(o=0;o<g;++o){x5b(c,p.charCodeAt(o));g-o>1&&a.d>0&&(g-o)%a.d==1&&Sp(c.a,n)}}else !m&&Sp(c.a,Qnc);(a.c||m)&&Sp(c.a,e);k=$dc+Math.floor(l+s+0.5);j=k.length;while(k.charCodeAt(j-1)==48&&j>a.i+1){--j}for(o=1;o<j;++o){x5b(c,k.charCodeAt(o))}}
function akb(a){var b,c,d,e,f,g,h,i,j,k,l,m;b=gLb(new dLb,4,2);b.l[ihc]=5;a.e=iOb(new gOb);a.e.J.style[_gc]=dxc;g=cgb(a.b);for(d=g,e=0,f=d.length;e<f;++e){c=d[e];mOb(a.e,c,-1)}Bbb(a.e,kkb(new ikb,a),($v(),$v(),Zv));b.Ic(0,0);h=(i=b.i.a.h.rows[0].cells[0],iKb(b,i,false),i);h.innerHTML=fyc;uKb(b,0,1,a.e);a.d=DQb(new sQb);a.d.J.style[_gc]=dxc;Bbb(a.d,pkb(new nkb,a),(Ex(),Ex(),Dx));uKb(b,1,1,a.d);a.f=DQb(new sQb);a.f.J.style[_gc]=dxc;a.f.J[bxc]=rzc;Bbb(a.f,ukb(new skb,a),Dx);b.Ic(2,0);j=(k=b.i.a.h.rows[2].cells[0],iKb(b,k,false),k);j.innerHTML=hyc;uKb(b,2,1,a.f);a.c=ZHb(new XHb);a.c.J.style[_gc]=dxc;b.Ic(3,0);l=(m=b.i.a.h.rows[3].cells[0],iKb(b,m,false),m);l.innerHTML=iyc;uKb(b,3,1,a.c);dkb(a);return b}
function eF(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p;f=-1;g=0;p=0;h=0;j=-1;k=b.length;n=c;l=true;for(;n<k&&l;++n){e=b.charCodeAt(n);switch(e){case 35:p>0?++h:++g;j>=0&&f<0&&++j;break;case 48:if(h>0){throw z3b(new w3b,fzc+b+Roc)}++p;j>=0&&f<0&&++j;break;case 44:j=0;break;case 46:if(f>=0){throw z3b(new w3b,gzc+b+Roc)}f=g+p+h;break;case 69:if(!d){if(a.q){throw z3b(new w3b,hzc+b+Roc)}a.q=true;a.h=0}while(n+1<k&&b.charCodeAt(n+1)==48){++n;!d&&++a.h}if(!d&&g+p<1||a.h<1){throw z3b(new w3b,izc+b+Roc)}l=false;break;default:--n;l=false;}}if(p==0&&g>0&&f>=0){m=f;m==0&&++m;h=g-m;g=m-1;p=1}if(f<0&&h>0||f>=0&&(f<g||f>g+p)||j==0){throw z3b(new w3b,jzc+b+Roc)}if(d){return n-c}o=g+p+h;a.f=f>=0?o-f:0;if(f>=0){a.i=g+p-f;a.i<0&&(a.i=0)}i=f>=0?f:o;a.j=i-g;if(a.q){a.g=g+a.j;a.f==0&&a.j==0&&(a.j=1)}a.d=j>0?j:0;a.c=f==0||f==o;return n-c}
var xzc='#,##0%',szc='#,##0.###',dzc='%',kzc=',',wzc='0.###E0',rzc='31415926535.897932',Bzc='AsyncLoader6',ozc='Currency',Czc='CwNumberFormat$1',Dzc='CwNumberFormat$2',Ezc='CwNumberFormat$3',nzc='Decimal',_yc='E',izc='Malformed exponential pattern "',jzc='Malformed pattern "',gzc='Multiple decimal separators in pattern "',hzc='Multiple exponential symbols in pattern "',zzc='NumberConstantsImpl_',Azc='NumberFormat',qzc='Percent',pzc='Scientific',czc='Too many percent/per mille characters in pattern "',uzc='US$',tzc='USD',fzc="Unexpected '0' in pattern \"",$yc='Unknown currency code',mzc='cwNumberFormatPatterns',lzc='runCallbacks6',vzc='\xA4#,##0.00;(\xA4#,##0.00)',bzc='\u0221',ezc='\u2030',azc='\uFFFD';_=SE.prototype=new Ul;_.gC=hF;_.tI=0;_.a=null;_.b=null;_.c=false;_.d=3;_.e=false;_.f=3;_.g=40;_.h=0;_.i=0;_.j=1;_.k=1;_.l=Vgc;_.m=$dc;_.n=null;_.o=$dc;_.p=$dc;_.q=false;var TE=null,UE=null,VE=null,WE=null;_=ZF.prototype=new Ul;_.gC=_F;_.tI=0;_=U8.prototype=new V8;_.gC=e9;_.Gb=i9;_.tI=0;_=ikb.prototype=new Ul;_.gC=lkb;_.Y=mkb;_.tI=121;_.a=null;_=nkb.prototype=new Ul;_.gC=qkb;_._=rkb;_.tI=122;_.a=null;_=skb.prototype=new Ul;_.gC=vkb;_._=wkb;_.tI=123;_.a=null;var l3b=null;var iL=$2b(yzc,zzc),fL=$2b(Gqc,Azc),QN=$2b(Mqc,Bzc),LO=$2b(Psc,Czc),MO=$2b(Psc,Dzc),NO=$2b(Psc,Ezc);f9();