function qF(){}
function xG(){}
function w9(){}
function Nkb(){}
function Skb(){}
function Xkb(){}
function zG(){return OL}
function HF(){return LL}
function I9(){return uO}
function Qkb(){return pP}
function Vkb(){return qP}
function $kb(){return rP}
function Rkb(a){Ikb(this.b)}
function Wkb(a){Ikb(this.b)}
function _kb(a){Hkb(this.b)}
function Ukb(a,b){a.b=b;return a}
function Pkb(a,b){a.b=b;return a}
function Zkb(a,b){a.b=b;return a}
function elb(a){ieb(a.c,Fkb(a.b))}
function lF(a){!a.d&&(a.d=new xG)}
function yF(a,b,c,d){vF();xF(a,b,c,d);return a}
function D5b(a,b,c,d){dq(a.b,b,c,d);return a}
function vF(){vF=Lcc;lF((iF(),iF(),hF))}
function r3b(a){var b;b=t3b(a);if(isNaN(b)){throw y4b(new w4b,Doc+a+Eoc)}return b}
function xF(a,b,c,d){vF();if(!c){throw C3b(new z3b,Oyc)}a.o=b;a.b=c[0];a.c=c[1];DF(a,a.o);if(!d&&a.f){a.j=c[2]&7;a.g=a.j}return a}
function M9(){var a;while(B9){a=B9;B9=B9.c;!B9&&(C9=null);elb(a.b)}}
function Hgb(a){var b,c;b=LJ(a.b.lb(azc),49);if(b==null){c=wJ(RV,371,1,[bzc,czc,dzc,ezc,Uxc]);a.b.mb(azc,c);return c}else{return b}}
function Gkb(a,b){if(b==null){Wbb(a.d.Jb(),Zxc,false)}else{(gq(),a.d.K).textContent=b||bec;Wbb(a.d.Jb(),Zxc,true)}}
function J9(){E9=true;D9=(G9(),new w9);ao((Zn(),Yn),6);!!$stats&&$stats(Ho(_yc,nec,null,null));D9.Gb();!!$stats&&$stats(Ho(_yc,Owc,null,null))}
function FF(a,b,c){var d,e,f;if(b==0){GF(a,b,c,a.k);zF(a,0,c);return}d=YJ(i4b(Math.log(b)/Math.log(10)));b/=Math.pow(10,d);f=a.k;if(a.h>1&&a.h>a.k){while(d%a.h!=0){b*=10;--d}f=1}else{if(a.k<1){++d;b/=10}else{for(e=1;e<a.k;++e){--d;b*=10}}}GF(a,b,c,f);zF(a,d,c)}
function t3b(a){var b=o3b;!b&&(b=o3b=/^\s*[+-]?\d*\.?\d*([eE][+-]?\d+)?\s*$/i);if(b.test(a)){return parseFloat(a)}else{return Number.NaN}}
function DF(a,b){var c,d;d=0;c=y5b(new v5b);d+=CF(a,b,d,c,false);a.p=c.b.b;d+=EF(a,b,d,false);d+=CF(a,b,d,c,false);a.q=c.b.b;if(d<b.length&&b.charCodeAt(d)==59){++d;d+=CF(a,b,d,c,true);a.m=c.b.b;d+=EF(a,b,d,true);d+=CF(a,b,d,c,true);a.n=c.b.b}else{a.m=Ugc+a.p;a.n=a.q}}
function zF(a,b,c){var d,e,f;c.b.b+=Pyc;if(b<0){b=-b;c.b.b+=Ugc}d=bec+b;f=d.length;for(e=f;e<a.i;++e){c.b.b+=Cnc}for(e=0;e<f;++e){A5b(c,d.charCodeAt(e))}}
function CF(a,b,c,d,e){var f,g,h,i;D5b(d,0,d.b.b.length,bec);g=false;h=b.length;for(i=c;i<h;++i){f=b.charCodeAt(i);if(f==39){if(i+1<h&&b.charCodeAt(i+1)==39){++i;d.b.b+=iyc}else{g=!g}continue}if(g){d.b.b+=String.fromCharCode(f)}else{switch(f){case 35:case 48:case 44:case 46:case 59:return i-c;case 164:a.f=true;if(i+1<h&&b.charCodeAt(i+1)==164){++i;C5b(d,a.b)}else{C5b(d,a.c)}break;case 37:if(!e){if(a.l!=1){throw C3b(new z3b,Syc+b+Eoc)}a.l=100}d.b.b+=Tyc;break;case 8240:if(!e){if(a.l!=1){throw C3b(new z3b,Syc+b+Eoc)}a.l=1000}d.b.b+=Uyc;break;case 45:d.b.b+=Ugc;break;default:d.b.b+=String.fromCharCode(f);}}}return h-c}
function Hkb(b){var a,d,e,f;e=us(b.g.K,Rwc);if(J4b(e,bec)){(gq(),b.d.K).textContent=$xc}else{try{f=r3b(e);d=AF(b.b,f);(gq(),b.d.K).textContent=d||bec;Gkb(b,null)}catch(a){a=$V(a);if(OJ(a,50)){Gkb(b,_xc)}else throw a}}}
function AF(a,b){var c,d;d=y5b(new v5b);if(isNaN(b)){d.b.b+=Qyc;return d.b.b}c=b<0||b==0&&1/b<0;C5b(d,c?a.m:a.p);if(!isFinite(b)){d.b.b+=Ryc}else{c&&(b=-b);b*=a.l;a.r?FF(a,b,d):GF(a,b,d,a.k)}C5b(d,c?a.n:a.q);return d.b.b}
function Ikb(b){var a,d;switch(b.f.K.selectedIndex){case 0:b.b=(vF(),!sF&&(sF=yF(new qF,gzc,[hzc,izc,2,izc],false)),sF);HQb(b.e,b.b.o);b.e.K[Ljc]=!false;break;case 1:b.b=(vF(),!rF&&(rF=yF(new qF,jzc,[hzc,izc,2,izc],false)),rF);HQb(b.e,b.b.o);b.e.K[Ljc]=!false;break;case 2:b.b=(vF(),!uF&&(uF=yF(new qF,kzc,[hzc,izc,2,izc],false)),uF);HQb(b.e,b.b.o);b.e.K[Ljc]=!false;break;case 3:b.b=(vF(),!tF&&(tF=yF(new qF,lzc,[hzc,izc,2,izc],false)),tF);HQb(b.e,b.b.o);b.e.K[Ljc]=!false;break;case 4:b.e.K[Ljc]=!true;d=us(b.e.K,Rwc);try{b.b=(vF(),yF(new qF,d,[hzc,izc,2,izc],true))}catch(a){a=$V(a);if(OJ(a,60)){Gkb(b,ayc);return}else throw a}}Hkb(b)}
function Fkb(a){var b,c,d,e,f,g,h,i,j,k,l,m;b=ELb(new BLb,4,2);b.m[ghc]=5;a.f=rOb(new pOb);a.f.K.style[$gc]=Twc;g=Hgb(a.c);for(d=g,e=0,f=d.length;e<f;++e){c=d[e];vOb(a.f,c,-1)}ccb(a.f,Pkb(new Nkb,a),(yw(),yw(),xw));b.Ic(0,0);h=(i=b.j.b.i.rows[0].cells[0],DKb(b,i,false),i);h.innerHTML=Vxc;PKb(b,0,1,a.f);a.e=OQb(new CQb);a.e.K.style[$gc]=Twc;ccb(a.e,Ukb(new Skb,a),(cy(),cy(),by));PKb(b,1,1,a.e);a.g=OQb(new CQb);a.g.K.style[$gc]=Twc;a.g.K[Rwc]=fzc;ccb(a.g,Zkb(new Xkb,a),by);b.Ic(2,0);j=(k=b.j.b.i.rows[2].cells[0],DKb(b,k,false),k);j.innerHTML=Xxc;PKb(b,2,1,a.g);a.d=sIb(new qIb);a.d.K.style[$gc]=Twc;b.Ic(3,0);l=(m=b.j.b.i.rows[3].cells[0],DKb(b,m,false),m);l.innerHTML=Yxc;PKb(b,3,1,a.d);Ikb(a);return b}
function GF(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s;s=Math.pow(10,a.g);i=b.toFixed(a.g+3);q=0;l=0;h=i.indexOf(h5b(101));if(h!=-1){q=Math.floor(b)}else{f=i.indexOf(h5b(46));r=i.length;f==-1&&(f=r);f>0&&(q=r3b(i.substr(0,f-0)));if(f<r-1){l=r3b(i.substr(f+1,i.length-(f+1)));l=~~((~~Math.max(Math.min(l,2147483647),-2147483648)+500)/1000);if(l>=s){l-=s;++q}}}m=a.j>0||l>0;p=bec+q;n=a.f?$yc:$yc;e=a.f?Ahc:Ahc;g=p.length;if(q>0||d>0){for(o=g;o<d;++o){c.b.b+=Cnc}for(o=0;o<g;++o){A5b(c,p.charCodeAt(o));g-o>1&&a.e>0&&(g-o)%a.e==1&&(c.b.b+=n,undefined)}}else !m&&(c.b.b+=Cnc,undefined);(a.d||m)&&(c.b.b+=e,undefined);k=bec+Math.floor(l+s+0.5);j=k.length;while(k.charCodeAt(j-1)==48&&j>a.j+1){--j}for(o=1;o<j;++o){A5b(c,k.charCodeAt(o))}}
function EF(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p;f=-1;g=0;p=0;h=0;j=-1;k=b.length;n=c;l=true;for(;n<k&&l;++n){e=b.charCodeAt(n);switch(e){case 35:p>0?++h:++g;j>=0&&f<0&&++j;break;case 48:if(h>0){throw C3b(new z3b,Vyc+b+Eoc)}++p;j>=0&&f<0&&++j;break;case 44:j=0;break;case 46:if(f>=0){throw C3b(new z3b,Wyc+b+Eoc)}f=g+p+h;break;case 69:if(!d){if(a.r){throw C3b(new z3b,Xyc+b+Eoc)}a.r=true;a.i=0}while(n+1<k&&b.charCodeAt(n+1)==48){++n;!d&&++a.i}if(!d&&g+p<1||a.i<1){throw C3b(new z3b,Yyc+b+Eoc)}l=false;break;default:--n;l=false;}}if(p==0&&g>0&&f>=0){m=f;m==0&&++m;h=g-m;g=m-1;p=1}if(f<0&&h>0||f>=0&&(f<g||f>g+p)||j==0){throw C3b(new z3b,Zyc+b+Eoc)}if(d){return n-c}o=g+p+h;a.g=f>=0?o-f:0;if(f>=0){a.j=g+p-f;a.j<0&&(a.j=0)}i=f>=0?f:o;a.k=i-g;if(a.r){a.h=g+a.k;a.g==0&&a.k==0&&(a.k=1)}a.e=j>0?j:0;a.d=f==0||f==o;return n-c}
var lzc='#,##0%',gzc='#,##0.###',Tyc='%',$yc=',',kzc='0.###E0',fzc='31415926535.897932',pzc='AsyncLoader6',czc='Currency',qzc='CwNumberFormat$1',rzc='CwNumberFormat$2',szc='CwNumberFormat$3',bzc='Decimal',Pyc='E',Yyc='Malformed exponential pattern "',Zyc='Malformed pattern "',Wyc='Multiple decimal separators in pattern "',Xyc='Multiple exponential symbols in pattern "',nzc='NumberConstantsImpl_',ozc='NumberFormat',ezc='Percent',dzc='Scientific',Syc='Too many percent/per mille characters in pattern "',izc='US$',hzc='USD',Vyc="Unexpected '0' in pattern \"",Oyc='Unknown currency code',azc='cwNumberFormatPatterns',_yc='runCallbacks6',jzc='\xA4#,##0.00;(\xA4#,##0.00)',Ryc='\u0221',Uyc='\u2030',Qyc='\uFFFD';_=qF.prototype=new Yl;_.gC=HF;_.tI=0;_.b=null;_.c=null;_.d=false;_.e=3;_.f=false;_.g=3;_.h=40;_.i=0;_.j=0;_.k=1;_.l=1;_.m=Ugc;_.n=bec;_.o=null;_.p=bec;_.q=bec;_.r=false;var rF=null,sF=null,tF=null,uF=null;_=xG.prototype=new Yl;_.gC=zG;_.tI=0;_=w9.prototype=new x9;_.gC=I9;_.Gb=M9;_.tI=0;_=Nkb.prototype=new Yl;_.gC=Qkb;_.Y=Rkb;_.tI=126;_.b=null;_=Skb.prototype=new Yl;_.gC=Vkb;_._=Wkb;_.tI=127;_.b=null;_=Xkb.prototype=new Yl;_.gC=$kb;_._=_kb;_.tI=128;_.b=null;var o3b=null;var OL=b3b(mzc,nzc),LL=b3b(uqc,ozc),uO=b3b(Aqc,pzc),pP=b3b(Dsc,qzc),qP=b3b(Dsc,rzc),rP=b3b(Dsc,szc);J9();