function BC(){}
function ID(){}
function l6(){}
function Ehb(){}
function Jhb(){}
function Ohb(){}
function KD(){return II}
function SC(){return FI}
function x6(){return oL}
function Hhb(){return jM}
function Mhb(){return kM}
function Rhb(){return lM}
function Ihb(a){zhb(this.b)}
function Nhb(a){zhb(this.b)}
function Shb(a){yhb(this.b)}
function Lhb(a,b){a.b=b;return a}
function Ghb(a,b){a.b=b;return a}
function Qhb(a,b){a.b=b;return a}
function Xhb(a){Zab(a.c,whb(a.b))}
function wC(a){!a.d&&(a.d=new ID)}
function JC(a,b,c,d){GC();IC(a,b,c,d);return a}
function B2b(a,b,c,d){Cp(a.b,b,c,d);return a}
function y2b(a,b){a.b.b+=String.fromCharCode(b);return a}
function p0b(a){var b;b=r0b(a);if(isNaN(b)){throw w1b(new u1b,wlc+a+xlc)}return b}
function B6(){var a;while(q6){a=q6;q6=q6.c;!q6&&(r6=null);Xhb(a.b)}}
function GC(){GC=P9b;wC((tC(),tC(),sC))}
function xhb(a,b){if(b==null){L8(a.d.Nb(),Huc,false)}else{Oq((Gp(),a.d.M),b);L8(a.d.Nb(),Huc,true)}}
function y6(){t6=true;s6=(v6(),new l6);zn((wn(),vn),6);!!$stats&&$stats(eo(Jvc,pbc,null,null));s6.Kb();!!$stats&&$stats(eo(Jvc,wtc,null,null))}
function QC(a,b,c){var d,e,f;if(b==0){RC(a,b,c,a.k);KC(a,0,c);return}d=oH(g1b(Math.log(b)/Math.log(10)));b/=Math.pow(10,d);f=a.k;if(a.h>1&&a.h>a.k){while(d%a.h!=0){b*=10;--d}f=1}else{if(a.k<1){++d;b/=10}else{for(e=1;e<a.k;++e){--d;b*=10}}}RC(a,b,c,f);KC(a,d,c)}
function r0b(a){var b=m0b;!b&&(b=m0b=/^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i);if(b.test(a)){return parseFloat(a)}else{return Number.NaN}}
function yhb(b){var a,d,e,f;e=Zr(b.g.M,ztc);if(H1b(e,dbc)){Oq((Gp(),b.d.M),Iuc)}else{try{f=p0b(e);d=LC(b.b,f);Oq((Gp(),b.d.M),d);xhb(b,null)}catch(a){a=PS(a);if(eH(a,44)){xhb(b,Juc)}else throw a}}}
function xdb(a){var b,c;b=bH(a.b.ob(Kvc),43);if(b==null){c=OG(GS,320,1,[Lvc,Mvc,Nvc,Ovc,Cuc]);a.b.pb(Kvc,c);return c}else{return b}}
function KC(a,b,c){var d,e,f;c.b.b+=xvc;if(b<0){b=-b;c.b.b+=Mdc}d=dbc+b;f=d.length;for(e=f;e<a.i;++e){c.b.b+=wkc}for(e=0;e<f;++e){y2b(c,d.charCodeAt(e))}}
function IC(a,b,c,d){GC();if(!c){throw A0b(new x0b,wvc)}a.o=b;a.b=c[0];a.c=c[1];OC(a,a.o);if(!d&&a.f){a.j=c[2]&7;a.g=a.j}return a}
function LC(a,b){var c,d;d=w2b(new t2b);if(isNaN(b)){d.b.b+=yvc;return d.b.b}c=b<0||b==0&&1/b<0;A2b(d,c?a.m:a.p);if(!isFinite(b)){d.b.b+=zvc}else{c&&(b=-b);b*=a.l;a.r?QC(a,b,d):RC(a,b,d,a.k)}A2b(d,c?a.n:a.q);return d.b.b}
function OC(a,b){var c,d;d=0;c=w2b(new t2b);d+=NC(a,b,d,c,false);a.p=c.b.b;d+=PC(a,b,d,false);d+=NC(a,b,d,c,false);a.q=c.b.b;if(d<b.length&&b.charCodeAt(d)==59){++d;d+=NC(a,b,d,c,true);a.m=c.b.b;d+=PC(a,b,d,true);d+=NC(a,b,d,c,true);a.n=c.b.b}else{a.m=Mdc+a.p;a.n=a.q}}
function NC(a,b,c,d,e){var f,g,h,i;B2b(d,0,d.b.b.length,dbc);g=false;h=b.length;for(i=c;i<h;++i){f=b.charCodeAt(i);if(f==39){if(i+1<h&&b.charCodeAt(i+1)==39){++i;d.b.b+=Suc}else{g=!g}continue}if(g){d.b.b+=String.fromCharCode(f)}else{switch(f){case 35:case 48:case 44:case 46:case 59:return i-c;case 164:a.f=true;if(i+1<h&&b.charCodeAt(i+1)==164){++i;A2b(d,a.b)}else{A2b(d,a.c)}break;case 37:if(!e){if(a.l!=1){throw A0b(new x0b,Avc+b+xlc)}a.l=100}d.b.b+=Bvc;break;case 8240:if(!e){if(a.l!=1){throw A0b(new x0b,Avc+b+xlc)}a.l=1000}d.b.b+=Cvc;break;case 45:d.b.b+=Mdc;break;default:d.b.b+=String.fromCharCode(f);}}}return h-c}
function zhb(b){var a,d;switch(b.f.M.selectedIndex){case 0:b.b=(GC(),!DC&&(DC=JC(new BC,Qvc,[Rvc,Svc,2,Svc],false)),DC);xNb(b.e,b.b.o);b.e.M[Dgc]=!false;break;case 1:b.b=(GC(),!CC&&(CC=JC(new BC,Tvc,[Rvc,Svc,2,Svc],false)),CC);xNb(b.e,b.b.o);b.e.M[Dgc]=!false;break;case 2:b.b=(GC(),!FC&&(FC=JC(new BC,Uvc,[Rvc,Svc,2,Svc],false)),FC);xNb(b.e,b.b.o);b.e.M[Dgc]=!false;break;case 3:b.b=(GC(),!EC&&(EC=JC(new BC,Vvc,[Rvc,Svc,2,Svc],false)),EC);xNb(b.e,b.b.o);b.e.M[Dgc]=!false;break;case 4:b.e.M[Dgc]=!true;d=Zr(b.e.M,ztc);try{b.b=(GC(),JC(new BC,d,[Rvc,Svc,2,Svc],true))}catch(a){a=PS(a);if(eH(a,54)){xhb(b,Kuc);return}else throw a}}yhb(b)}
function whb(a){var b,c,d,e,f,g,h,i,j,k,l,m;b=uIb(new rIb,4,2);b.m[$dc]=5;a.f=hLb(new fLb);a.f.M.style[Sdc]=Btc;g=xdb(a.c);for(d=g,e=0,f=d.length;e<f;++e){c=d[e];lLb(a.f,c,-1)}T8(a.f,Ghb(new Ehb,a),(Ht(),Ht(),Gt));b.Mc(0,0);h=(i=b.j.b.i.rows[0].cells[0],tHb(b,i,false),i);h.innerHTML=Duc;FHb(b,0,1,a.f);a.e=ENb(new sNb);a.e.M.style[Sdc]=Btc;T8(a.e,Lhb(new Jhb,a),(lv(),lv(),kv));FHb(b,1,1,a.e);a.g=ENb(new sNb);a.g.M.style[Sdc]=Btc;a.g.M[ztc]=Pvc;T8(a.g,Qhb(new Ohb,a),kv);b.Mc(2,0);j=(k=b.j.b.i.rows[2].cells[0],tHb(b,k,false),k);j.innerHTML=Fuc;FHb(b,2,1,a.g);a.d=iFb(new gFb);a.d.M.style[Sdc]=Btc;b.Mc(3,0);l=(m=b.j.b.i.rows[3].cells[0],tHb(b,m,false),m);l.innerHTML=Guc;FHb(b,3,1,a.d);zhb(a);return b}
function RC(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s;s=Math.pow(10,a.g);i=b.toFixed(a.g+3);q=0;l=0;h=i.indexOf(f2b(101));if(h!=-1){q=Math.floor(b)}else{f=i.indexOf(f2b(46));r=i.length;f==-1&&(f=r);f>0&&(q=p0b(i.substr(0,f-0)));if(f<r-1){l=p0b(i.substr(f+1,i.length-(f+1)));l=~~((~~Math.max(Math.min(l,2147483647),-2147483648)+500)/1000);if(l>=s){l-=s;++q}}}m=a.j>0||l>0;p=dbc+q;n=a.f?Ivc:Ivc;e=a.f?sec:sec;g=p.length;if(q>0||d>0){for(o=g;o<d;++o){c.b.b+=wkc}for(o=0;o<g;++o){y2b(c,p.charCodeAt(o));g-o>1&&a.e>0&&(g-o)%a.e==1&&(c.b.b+=n,undefined)}}else !m&&(c.b.b+=wkc,undefined);(a.d||m)&&(c.b.b+=e,undefined);k=dbc+Math.floor(l+s+0.5);j=k.length;while(k.charCodeAt(j-1)==48&&j>a.j+1){--j}for(o=1;o<j;++o){y2b(c,k.charCodeAt(o))}}
function PC(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p;f=-1;g=0;p=0;h=0;j=-1;k=b.length;n=c;l=true;for(;n<k&&l;++n){e=b.charCodeAt(n);switch(e){case 35:p>0?++h:++g;j>=0&&f<0&&++j;break;case 48:if(h>0){throw A0b(new x0b,Dvc+b+xlc)}++p;j>=0&&f<0&&++j;break;case 44:j=0;break;case 46:if(f>=0){throw A0b(new x0b,Evc+b+xlc)}f=g+p+h;break;case 69:if(!d){if(a.r){throw A0b(new x0b,Fvc+b+xlc)}a.r=true;a.i=0}while(n+1<k&&b.charCodeAt(n+1)==48){++n;!d&&++a.i}if(!d&&g+p<1||a.i<1){throw A0b(new x0b,Gvc+b+xlc)}l=false;break;default:--n;l=false;}}if(p==0&&g>0&&f>=0){m=f;m==0&&++m;h=g-m;g=m-1;p=1}if(f<0&&h>0||f>=0&&(f<g||f>g+p)||j==0){throw A0b(new x0b,Hvc+b+xlc)}if(d){return n-c}o=g+p+h;a.g=f>=0?o-f:0;if(f>=0){a.j=g+p-f;a.j<0&&(a.j=0)}i=f>=0?f:o;a.k=i-g;if(a.r){a.h=g+a.k;a.g==0&&a.k==0&&(a.k=1)}a.e=j>0?j:0;a.d=f==0||f==o;return n-c}
var Vvc='#,##0%',Qvc='#,##0.###',Bvc='%',Ivc=',',Uvc='0.###E0',Pvc='31415926535.897932',Zvc='AsyncLoader6',Mvc='Currency',$vc='CwNumberFormat$1',_vc='CwNumberFormat$2',awc='CwNumberFormat$3',Lvc='Decimal',xvc='E',Gvc='Malformed exponential pattern "',Hvc='Malformed pattern "',Evc='Multiple decimal separators in pattern "',Fvc='Multiple exponential symbols in pattern "',Xvc='NumberConstantsImpl_',Yvc='NumberFormat',Ovc='Percent',Nvc='Scientific',Avc='Too many percent/per mille characters in pattern "',Svc='US$',Rvc='USD',Dvc="Unexpected '0' in pattern \"",wvc='Unknown currency code',Kvc='cwNumberFormatPatterns',Jvc='runCallbacks6',Tvc='\xA4#,##0.00;(\xA4#,##0.00)',zvc='\u0221',Cvc='\u2030',yvc='\uFFFD';_=BC.prototype=new vl;_.gC=SC;_.tI=0;_.b=null;_.c=null;_.d=false;_.e=3;_.f=false;_.g=3;_.h=40;_.i=0;_.j=0;_.k=1;_.l=1;_.m=Mdc;_.n=dbc;_.o=null;_.p=dbc;_.q=dbc;_.r=false;var CC=null,DC=null,EC=null,FC=null;_=ID.prototype=new vl;_.gC=KD;_.tI=0;_=l6.prototype=new m6;_.gC=x6;_.Kb=B6;_.tI=0;_=Ehb.prototype=new vl;_.gC=Hhb;_._=Ihb;_.tI=101;_.b=null;_=Jhb.prototype=new vl;_.gC=Mhb;_.cb=Nhb;_.tI=102;_.b=null;_=Ohb.prototype=new vl;_.gC=Rhb;_.cb=Shb;_.tI=103;_.b=null;var m0b=null;var II=__b(Wvc,Xvc),FI=__b(cnc,Yvc),oL=__b(inc,Zvc),jM=__b(lpc,$vc),kM=__b(lpc,_vc),lM=__b(lpc,awc);y6();