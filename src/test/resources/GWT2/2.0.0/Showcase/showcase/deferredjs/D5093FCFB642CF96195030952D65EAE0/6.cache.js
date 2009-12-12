function qC(){}
function xD(){}
function Y5(){}
function mhb(){}
function rhb(){}
function whb(){}
function zD(){return wI}
function HC(){return tI}
function i6(){return cL}
function phb(){return ZL}
function uhb(){return $L}
function zhb(){return _L}
function qhb(a){hhb(this.b)}
function vhb(a){hhb(this.b)}
function Ahb(a){ghb(this.b)}
function thb(a,b){a.b=b;return a}
function ohb(a,b){a.b=b;return a}
function yhb(a,b){a.b=b;return a}
function Fhb(a){Kab(a.c,ehb(a.b))}
function lC(a){!a.d&&(a.d=new xD)}
function yC(a,b,c,d){vC();xC(a,b,c,d);return a}
function C1b(a,b,c,d){Cp(a.b,b,c,d);return a}
function z1b(a,b){a.b.b+=String.fromCharCode(b);return a}
function q_b(a){var b;b=s_b(a);if(isNaN(b)){throw x0b(new v0b,vkc+a+wkc)}return b}
function m6(){var a;while(b6){a=b6;b6=b6.c;!b6&&(c6=null);Fhb(a.b)}}
function vC(){vC=Q8b;lC((iC(),iC(),hC))}
function fhb(a,b){if(b==null){w8(a.d.Lb(),Btc,false)}else{jq((Gp(),a.d.K),b);w8(a.d.Lb(),Btc,true)}}
function j6(){e6=true;d6=(g6(),new Y5);vn((sn(),rn),6);!!$stats&&$stats(_n(Duc,qac,null,null));d6.Ib();!!$stats&&$stats(_n(Duc,qsc,null,null))}
function FC(a,b,c){var d,e,f;if(b==0){GC(a,b,c,a.k);zC(a,0,c);return}d=dH(h0b(Math.log(b)/Math.log(10)));b/=Math.pow(10,d);f=a.k;if(a.h>1&&a.h>a.k){while(d%a.h!=0){b*=10;--d}f=1}else{if(a.k<1){++d;b/=10}else{for(e=1;e<a.k;++e){--d;b*=10}}}GC(a,b,c,f);zC(a,d,c)}
function s_b(a){var b=n_b;!b&&(b=n_b=/^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i);if(b.test(a)){return parseFloat(a)}else{return Number.NaN}}
function ghb(b){var a,d,e,f;e=Or(b.g.K,tsc);if(I0b(e,eac)){jq((Gp(),b.d.K),Ctc)}else{try{f=q_b(e);d=AC(b.b,f);jq((Gp(),b.d.K),d);fhb(b,null)}catch(a){a=AS(a);if(VG(a,44)){fhb(b,Dtc)}else throw a}}}
function fdb(a){var b,c;b=SG(a.b.mb(Euc),43);if(b==null){c=DG(rS,317,1,[Fuc,Guc,Huc,Iuc,wtc]);a.b.nb(Euc,c);return c}else{return b}}
function zC(a,b,c){var d,e,f;c.b.b+=ruc;if(b<0){b=-b;c.b.b+=Mcc}d=eac+b;f=d.length;for(e=f;e<a.i;++e){c.b.b+=vjc}for(e=0;e<f;++e){z1b(c,d.charCodeAt(e))}}
function xC(a,b,c,d){vC();if(!c){throw B_b(new y_b,quc)}a.o=b;a.b=c[0];a.c=c[1];DC(a,a.o);if(!d&&a.f){a.j=c[2]&7;a.g=a.j}return a}
function AC(a,b){var c,d;d=x1b(new u1b);if(isNaN(b)){d.b.b+=suc;return d.b.b}c=b<0||b==0&&1/b<0;B1b(d,c?a.m:a.p);if(!isFinite(b)){d.b.b+=tuc}else{c&&(b=-b);b*=a.l;a.r?FC(a,b,d):GC(a,b,d,a.k)}B1b(d,c?a.n:a.q);return d.b.b}
function DC(a,b){var c,d;d=0;c=x1b(new u1b);d+=CC(a,b,d,c,false);a.p=c.b.b;d+=EC(a,b,d,false);d+=CC(a,b,d,c,false);a.q=c.b.b;if(d<b.length&&b.charCodeAt(d)==59){++d;d+=CC(a,b,d,c,true);a.m=c.b.b;d+=EC(a,b,d,true);d+=CC(a,b,d,c,true);a.n=c.b.b}else{a.m=Mcc+a.p;a.n=a.q}}
function CC(a,b,c,d,e){var f,g,h,i;C1b(d,0,d.b.b.length,eac);g=false;h=b.length;for(i=c;i<h;++i){f=b.charCodeAt(i);if(f==39){if(i+1<h&&b.charCodeAt(i+1)==39){++i;d.b.b+=Mtc}else{g=!g}continue}if(g){d.b.b+=String.fromCharCode(f)}else{switch(f){case 35:case 48:case 44:case 46:case 59:return i-c;case 164:a.f=true;if(i+1<h&&b.charCodeAt(i+1)==164){++i;B1b(d,a.b)}else{B1b(d,a.c)}break;case 37:if(!e){if(a.l!=1){throw B_b(new y_b,uuc+b+wkc)}a.l=100}d.b.b+=vuc;break;case 8240:if(!e){if(a.l!=1){throw B_b(new y_b,uuc+b+wkc)}a.l=1000}d.b.b+=wuc;break;case 45:d.b.b+=Mcc;break;default:d.b.b+=String.fromCharCode(f);}}}return h-c}
function hhb(b){var a,d;switch(b.f.K.selectedIndex){case 0:b.b=(vC(),!sC&&(sC=yC(new qC,Kuc,[Luc,Muc,2,Muc],false)),sC);$Mb(b.e,b.b.o);b.e.K[Dfc]=!false;break;case 1:b.b=(vC(),!rC&&(rC=yC(new qC,Nuc,[Luc,Muc,2,Muc],false)),rC);$Mb(b.e,b.b.o);b.e.K[Dfc]=!false;break;case 2:b.b=(vC(),!uC&&(uC=yC(new qC,Ouc,[Luc,Muc,2,Muc],false)),uC);$Mb(b.e,b.b.o);b.e.K[Dfc]=!false;break;case 3:b.b=(vC(),!tC&&(tC=yC(new qC,Puc,[Luc,Muc,2,Muc],false)),tC);$Mb(b.e,b.b.o);b.e.K[Dfc]=!false;break;case 4:b.e.K[Dfc]=!true;d=Or(b.e.K,tsc);try{b.b=(vC(),yC(new qC,d,[Luc,Muc,2,Muc],true))}catch(a){a=AS(a);if(VG(a,54)){fhb(b,Etc);return}else throw a}}ghb(b)}
function ehb(a){var b,c,d,e,f,g,h,i,j,k,l,m;b=ZHb(new WHb,4,2);b.m[$cc]=5;a.f=MKb(new KKb);a.f.K.style[Scc]=vsc;g=fdb(a.c);for(d=g,e=0,f=d.length;e<f;++e){c=d[e];QKb(a.f,c,-1)}E8(a.f,ohb(new mhb,a),(wt(),wt(),vt));b.Kc(0,0);h=(i=b.j.b.i.rows[0].cells[0],_Gb(b,i,false),i);h.innerHTML=xtc;lHb(b,0,1,a.f);a.e=fNb(new WMb);a.e.K.style[Scc]=vsc;E8(a.e,thb(new rhb,a),(av(),av(),_u));lHb(b,1,1,a.e);a.g=fNb(new WMb);a.g.K.style[Scc]=vsc;a.g.K[tsc]=Juc;E8(a.g,yhb(new whb,a),_u);b.Kc(2,0);j=(k=b.j.b.i.rows[2].cells[0],_Gb(b,k,false),k);j.innerHTML=ztc;lHb(b,2,1,a.g);a.d=IEb(new GEb);a.d.K.style[Scc]=vsc;b.Kc(3,0);l=(m=b.j.b.i.rows[3].cells[0],_Gb(b,m,false),m);l.innerHTML=Atc;lHb(b,3,1,a.d);hhb(a);return b}
function GC(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s;s=Math.pow(10,a.g);i=b.toFixed(a.g+3);q=0;l=0;h=i.indexOf(g1b(101));if(h!=-1){q=Math.floor(b)}else{f=i.indexOf(g1b(46));r=i.length;f==-1&&(f=r);f>0&&(q=q_b(i.substr(0,f-0)));if(f<r-1){l=q_b(i.substr(f+1,i.length-(f+1)));l=~~((~~Math.max(Math.min(l,2147483647),-2147483648)+500)/1000);if(l>=s){l-=s;++q}}}m=a.j>0||l>0;p=eac+q;n=a.f?Cuc:Cuc;e=a.f?sdc:sdc;g=p.length;if(q>0||d>0){for(o=g;o<d;++o){c.b.b+=vjc}for(o=0;o<g;++o){z1b(c,p.charCodeAt(o));g-o>1&&a.e>0&&(g-o)%a.e==1&&(c.b.b+=n,undefined)}}else !m&&(c.b.b+=vjc,undefined);(a.d||m)&&(c.b.b+=e,undefined);k=eac+Math.floor(l+s+0.5);j=k.length;while(k.charCodeAt(j-1)==48&&j>a.j+1){--j}for(o=1;o<j;++o){z1b(c,k.charCodeAt(o))}}
function EC(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p;f=-1;g=0;p=0;h=0;j=-1;k=b.length;n=c;l=true;for(;n<k&&l;++n){e=b.charCodeAt(n);switch(e){case 35:p>0?++h:++g;j>=0&&f<0&&++j;break;case 48:if(h>0){throw B_b(new y_b,xuc+b+wkc)}++p;j>=0&&f<0&&++j;break;case 44:j=0;break;case 46:if(f>=0){throw B_b(new y_b,yuc+b+wkc)}f=g+p+h;break;case 69:if(!d){if(a.r){throw B_b(new y_b,zuc+b+wkc)}a.r=true;a.i=0}while(n+1<k&&b.charCodeAt(n+1)==48){++n;!d&&++a.i}if(!d&&g+p<1||a.i<1){throw B_b(new y_b,Auc+b+wkc)}l=false;break;default:--n;l=false;}}if(p==0&&g>0&&f>=0){m=f;m==0&&++m;h=g-m;g=m-1;p=1}if(f<0&&h>0||f>=0&&(f<g||f>g+p)||j==0){throw B_b(new y_b,Buc+b+wkc)}if(d){return n-c}o=g+p+h;a.g=f>=0?o-f:0;if(f>=0){a.j=g+p-f;a.j<0&&(a.j=0)}i=f>=0?f:o;a.k=i-g;if(a.r){a.h=g+a.k;a.g==0&&a.k==0&&(a.k=1)}a.e=j>0?j:0;a.d=f==0||f==o;return n-c}
var Puc='#,##0%',Kuc='#,##0.###',vuc='%',Cuc=',',Ouc='0.###E0',Juc='31415926535.897932',Tuc='AsyncLoader6',Guc='Currency',Uuc='CwNumberFormat$1',Vuc='CwNumberFormat$2',Wuc='CwNumberFormat$3',Fuc='Decimal',ruc='E',Auc='Malformed exponential pattern "',Buc='Malformed pattern "',yuc='Multiple decimal separators in pattern "',zuc='Multiple exponential symbols in pattern "',Ruc='NumberConstantsImpl_',Suc='NumberFormat',Iuc='Percent',Huc='Scientific',uuc='Too many percent/per mille characters in pattern "',Muc='US$',Luc='USD',xuc="Unexpected '0' in pattern \"",quc='Unknown currency code',Euc='cwNumberFormatPatterns',Duc='runCallbacks6',Nuc='\xA4#,##0.00;(\xA4#,##0.00)',tuc='\u0221',wuc='\u2030',suc='\uFFFD';_=qC.prototype=new rl;_.gC=HC;_.tI=0;_.b=null;_.c=null;_.d=false;_.e=3;_.f=false;_.g=3;_.h=40;_.i=0;_.j=0;_.k=1;_.l=1;_.m=Mcc;_.n=eac;_.o=null;_.p=eac;_.q=eac;_.r=false;var rC=null,sC=null,tC=null,uC=null;_=xD.prototype=new rl;_.gC=zD;_.tI=0;_=Y5.prototype=new Z5;_.gC=i6;_.Ib=m6;_.tI=0;_=mhb.prototype=new rl;_.gC=phb;_.Z=qhb;_.tI=101;_.b=null;_=rhb.prototype=new rl;_.gC=uhb;_.ab=vhb;_.tI=102;_.b=null;_=whb.prototype=new rl;_.gC=zhb;_.ab=Ahb;_.tI=103;_.b=null;var n_b=null;var wI=a_b(Quc,Ruc),tI=a_b(amc,Suc),cL=a_b(gmc,Tuc),ZL=a_b(joc,Uuc),$L=a_b(joc,Vuc),_L=a_b(joc,Wuc);j6();