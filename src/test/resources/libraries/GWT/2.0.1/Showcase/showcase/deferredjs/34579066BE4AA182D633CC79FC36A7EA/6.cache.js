function lF(){}
function sG(){}
function s9(){}
function Nkb(){}
function Skb(){}
function Xkb(){}
function uG(){return DL}
function CF(){return AL}
function E9(){return jO}
function Qkb(){return eP}
function Vkb(){return fP}
function $kb(){return gP}
function Rkb(a){Ikb(this.a)}
function Wkb(a){Ikb(this.a)}
function _kb(a){Hkb(this.a)}
function Pkb(a,b){a.a=b;return a}
function Ukb(a,b){a.a=b;return a}
function Zkb(a,b){a.a=b;return a}
function elb(a){feb(a.b,Fkb(a.a))}
function gF(a){!a.c&&(a.c=new sG)}
function tF(a,b,c,d){qF();sF(a,b,c,d);return a}
function h7b(a,b,c,d){aq(a.a,b,c,d);return a}
function qF(){qF=pec;gF((dF(),dF(),cF))}
function X4b(a){var b;b=Z4b(a);if(isNaN(b)){throw c6b(new a6b,Qqc+a+Rqc)}return b}
function sF(a,b,c,d){qF();if(!c){throw g5b(new d5b,_Ac)}a.n=b;a.a=c[0];a.b=c[1];yF(a,a.n);if(!d&&a.e){a.i=c[2]&7;a.f=a.i}return a}
function I9(){var a;while(x9){a=x9;x9=x9.b;!x9&&(y9=null);elb(a.a)}}
function Cgb(a){var b,c;b=GJ(a.a.lb(nBc),48);if(b==null){c=rJ(NV,368,1,[oBc,pBc,qBc,rBc,fAc]);a.a.mb(nBc,c);return c}else{return b}}
function Gkb(a,b){if(b==null){Tbb(a.c.Jb(),kAc,false)}else{(eq(),a.c.J).innerText=b||Hfc;Tbb(a.c.Jb(),kAc,true)}}
function F9(){A9=true;z9=(C9(),new s9);bo(($n(),Zn),6);!!$stats&&$stats(Io(mBc,Tfc,null,null));z9.Gb();!!$stats&&$stats(Io(mBc,_yc,null,null))}
function AF(a,b,c){var d,e,f;if(b==0){BF(a,b,c,a.j);uF(a,0,c);return}d=TJ(O5b(Math.log(b)/Math.log(10)));b/=Math.pow(10,d);f=a.j;if(a.g>1&&a.g>a.j){while(d%a.g!=0){b*=10;--d}f=1}else{if(a.j<1){++d;b/=10}else{for(e=1;e<a.j;++e){--d;b*=10}}}BF(a,b,c,f);uF(a,d,c)}
function Z4b(a){var b=U4b;!b&&(b=U4b=/^\s*[+-]?\d*\.?\d*([eE][+-]?\d+)?\s*$/i);if(b.test(a)){return parseFloat(a)}else{return Number.NaN}}
function yF(a,b){var c,d;d=0;c=c7b(new _6b);d+=xF(a,b,d,c,false);a.o=cq(c.a);d+=zF(a,b,d,false);d+=xF(a,b,d,c,false);a.p=cq(c.a);if(d<b.length&&b.charCodeAt(d)==59){++d;d+=xF(a,b,d,c,true);a.l=cq(c.a);d+=zF(a,b,d,true);d+=xF(a,b,d,c,true);a.m=cq(c.a)}else{a.l=Dic+a.o;a.m=a.p}}
function uF(a,b,c){var d,e,f;Zp(c.a,aBc);if(b<0){b=-b;Zp(c.a,Dic)}d=Hfc+b;f=d.length;for(e=f;e<a.h;++e){Zp(c.a,upc)}for(e=0;e<f;++e){e7b(c,d.charCodeAt(e))}}
function xF(a,b,c,d,e){var f,g,h,i;h7b(d,0,cq(d.a).length,Hfc);g=false;h=b.length;for(i=c;i<h;++i){f=b.charCodeAt(i);if(f==39){if(i+1<h&&b.charCodeAt(i+1)==39){++i;Zp(d.a,vAc)}else{g=!g}continue}if(g){$p(d.a,String.fromCharCode(f))}else{switch(f){case 35:case 48:case 44:case 46:case 59:return i-c;case 164:a.e=true;if(i+1<h&&b.charCodeAt(i+1)==164){++i;g7b(d,a.a)}else{g7b(d,a.b)}break;case 37:if(!e){if(a.k!=1){throw g5b(new d5b,dBc+b+Rqc)}a.k=100}Zp(d.a,eBc);break;case 8240:if(!e){if(a.k!=1){throw g5b(new d5b,dBc+b+Rqc)}a.k=1000}Zp(d.a,fBc);break;case 45:Zp(d.a,Dic);break;default:$p(d.a,String.fromCharCode(f));}}}return h-c}
function Hkb(b){var a,d,e,f;e=zs(b.f.J,czc);if(n6b(e,Hfc)){(eq(),b.c.J).innerText=lAc}else{try{f=X4b(e);d=vF(b.a,f);(eq(),b.c.J).innerText=d||Hfc;Gkb(b,null)}catch(a){a=WV(a);if(JJ(a,49)){Gkb(b,mAc)}else throw a}}}
function vF(a,b){var c,d;d=c7b(new _6b);if(isNaN(b)){Zp(d.a,bBc);return cq(d.a)}c=b<0||b==0&&1/b<0;g7b(d,c?a.l:a.o);if(!isFinite(b)){Zp(d.a,cBc)}else{c&&(b=-b);b*=a.k;a.q?AF(a,b,d):BF(a,b,d,a.j)}g7b(d,c?a.m:a.p);return cq(d.a)}
function Ikb(b){var a,d;switch(b.e.J.selectedIndex){case 0:b.a=(qF(),!nF&&(nF=tF(new lF,tBc,[uBc,vBc,2,vBc],false)),nF);yRb(b.d,b.a.n);b.d.J[tlc]=!false;break;case 1:b.a=(qF(),!mF&&(mF=tF(new lF,wBc,[uBc,vBc,2,vBc],false)),mF);yRb(b.d,b.a.n);b.d.J[tlc]=!false;break;case 2:b.a=(qF(),!pF&&(pF=tF(new lF,xBc,[uBc,vBc,2,vBc],false)),pF);yRb(b.d,b.a.n);b.d.J[tlc]=!false;break;case 3:b.a=(qF(),!oF&&(oF=tF(new lF,yBc,[uBc,vBc,2,vBc],false)),oF);yRb(b.d,b.a.n);b.d.J[tlc]=!false;break;case 4:b.d.J[tlc]=!true;d=zs(b.d.J,czc);try{b.a=(qF(),tF(new lF,d,[uBc,vBc,2,vBc],true))}catch(a){a=WV(a);if(JJ(a,59)){Gkb(b,nAc);return}else throw a}}Hkb(b)}
function BF(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s;s=Math.pow(10,a.f);i=b.toFixed(a.f+3);q=0;l=0;h=i.indexOf(N6b(101));if(h!=-1){q=Math.floor(b)}else{f=i.indexOf(N6b(46));r=i.length;f==-1&&(f=r);f>0&&(q=X4b(i.substr(0,f-0)));if(f<r-1){l=X4b(i.substr(f+1,i.length-(f+1)));l=~~((~~Math.max(Math.min(l,2147483647),-2147483648)+500)/1000);if(l>=s){l-=s;++q}}}m=a.i>0||l>0;p=Hfc+q;n=a.e?lBc:lBc;e=a.e?jjc:jjc;g=p.length;if(q>0||d>0){for(o=g;o<d;++o){Zp(c.a,upc)}for(o=0;o<g;++o){e7b(c,p.charCodeAt(o));g-o>1&&a.d>0&&(g-o)%a.d==1&&Zp(c.a,n)}}else !m&&Zp(c.a,upc);(a.c||m)&&Zp(c.a,e);k=Hfc+Math.floor(l+s+0.5);j=k.length;while(k.charCodeAt(j-1)==48&&j>a.i+1){--j}for(o=1;o<j;++o){e7b(c,k.charCodeAt(o))}}
function Fkb(a){var b,c,d,e,f,g,h,i,j,k,l,m;b=fMb(new cMb,4,2);b.l[Sic]=5;a.e=iPb(new gPb);a.e.J.style[Jic]=ezc;g=Cgb(a.b);for(d=g,e=0,f=d.length;e<f;++e){c=d[e];mPb(a.e,c,-1)}_bb(a.e,Pkb(new Nkb,a),(tw(),tw(),sw));b.Lc(0,0);h=(i=b.i.a.h.rows[0].cells[0],hLb(b,i,false),i);h.innerHTML=gAc;tLb(b,0,1,a.e);a.d=FRb(new uRb);a.d.J.style[Jic]=ezc;_bb(a.d,Ukb(new Skb,a),(Zx(),Zx(),Yx));tLb(b,1,1,a.d);a.f=FRb(new uRb);a.f.J.style[Jic]=ezc;a.f.J[czc]=sBc;_bb(a.f,Zkb(new Xkb,a),Yx);b.Lc(2,0);j=(k=b.i.a.h.rows[2].cells[0],hLb(b,k,false),k);j.innerHTML=iAc;tLb(b,2,1,a.f);a.c=YIb(new WIb);a.c.J.style[Jic]=ezc;b.Lc(3,0);l=(m=b.i.a.h.rows[3].cells[0],hLb(b,m,false),m);l.innerHTML=jAc;tLb(b,3,1,a.c);Ikb(a);return b}
function zF(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p;f=-1;g=0;p=0;h=0;j=-1;k=b.length;n=c;l=true;for(;n<k&&l;++n){e=b.charCodeAt(n);switch(e){case 35:p>0?++h:++g;j>=0&&f<0&&++j;break;case 48:if(h>0){throw g5b(new d5b,gBc+b+Rqc)}++p;j>=0&&f<0&&++j;break;case 44:j=0;break;case 46:if(f>=0){throw g5b(new d5b,hBc+b+Rqc)}f=g+p+h;break;case 69:if(!d){if(a.q){throw g5b(new d5b,iBc+b+Rqc)}a.q=true;a.h=0}while(n+1<k&&b.charCodeAt(n+1)==48){++n;!d&&++a.h}if(!d&&g+p<1||a.h<1){throw g5b(new d5b,jBc+b+Rqc)}l=false;break;default:--n;l=false;}}if(p==0&&g>0&&f>=0){m=f;m==0&&++m;h=g-m;g=m-1;p=1}if(f<0&&h>0||f>=0&&(f<g||f>g+p)||j==0){throw g5b(new d5b,kBc+b+Rqc)}if(d){return n-c}o=g+p+h;a.f=f>=0?o-f:0;if(f>=0){a.i=g+p-f;a.i<0&&(a.i=0)}i=f>=0?f:o;a.j=i-g;if(a.q){a.g=g+a.j;a.f==0&&a.j==0&&(a.j=1)}a.d=j>0?j:0;a.c=f==0||f==o;return n-c}
var yBc='#,##0%',tBc='#,##0.###',eBc='%',lBc=',',xBc='0.###E0',sBc='31415926535.897932',CBc='AsyncLoader6',pBc='Currency',DBc='CwNumberFormat$1',EBc='CwNumberFormat$2',FBc='CwNumberFormat$3',oBc='Decimal',aBc='E',jBc='Malformed exponential pattern "',kBc='Malformed pattern "',hBc='Multiple decimal separators in pattern "',iBc='Multiple exponential symbols in pattern "',ABc='NumberConstantsImpl_',BBc='NumberFormat',rBc='Percent',qBc='Scientific',dBc='Too many percent/per mille characters in pattern "',vBc='US$',uBc='USD',gBc="Unexpected '0' in pattern \"",_Ac='Unknown currency code',nBc='cwNumberFormatPatterns',mBc='runCallbacks6',wBc='\xA4#,##0.00;(\xA4#,##0.00)',cBc='\u0221',fBc='\u2030',bBc='\uFFFD';_=lF.prototype=new Zl;_.gC=CF;_.tI=0;_.a=null;_.b=null;_.c=false;_.d=3;_.e=false;_.f=3;_.g=40;_.h=0;_.i=0;_.j=1;_.k=1;_.l=Dic;_.m=Hfc;_.n=null;_.o=Hfc;_.p=Hfc;_.q=false;var mF=null,nF=null,oF=null,pF=null;_=sG.prototype=new Zl;_.gC=uG;_.tI=0;_=s9.prototype=new t9;_.gC=E9;_.Gb=I9;_.tI=0;_=Nkb.prototype=new Zl;_.gC=Qkb;_.Y=Rkb;_.tI=121;_.a=null;_=Skb.prototype=new Zl;_.gC=Vkb;_._=Wkb;_.tI=122;_.a=null;_=Xkb.prototype=new Zl;_.gC=$kb;_._=_kb;_.tI=123;_.a=null;var U4b=null;var DL=H4b(zBc,ABc),AL=H4b(Gsc,BBc),jO=H4b(Msc,CBc),eP=H4b(Puc,DBc),fP=H4b(Puc,EBc),gP=H4b(Puc,FBc);F9();