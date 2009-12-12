function xC(){}
function ED(){}
function i6(){}
function zhb(){}
function Ehb(){}
function Jhb(){}
function GD(){return CI}
function OC(){return zI}
function u6(){return iL}
function Chb(){return dM}
function Hhb(){return eM}
function Mhb(){return fM}
function Dhb(a){uhb(this.a)}
function Ihb(a){uhb(this.a)}
function Nhb(a){thb(this.a)}
function Bhb(a,b){a.a=b;return a}
function Ghb(a,b){a.a=b;return a}
function Lhb(a,b){a.a=b;return a}
function Shb(a){Xab(a.b,rhb(a.a))}
function sC(a){!a.c&&(a.c=new ED)}
function FC(a,b,c,d){CC();EC(a,b,c,d);return a}
function N2b(a,b,c,d){xp(a.a,b,c,d);return a}
function K2b(a,b){vp(a.a,String.fromCharCode(b));return a}
function B0b(a){var b;b=D0b(a);if(isNaN(b)){throw I1b(new G1b,Ylc+a+Zlc)}return b}
function y6(){var a;while(n6){a=n6;n6=n6.b;!n6&&(o6=null);Shb(a.a)}}
function CC(){CC=_9b;sC((pC(),pC(),oC))}
function shb(a,b){if(b==null){J8(a.c.Kb(),fvc,false)}else{(Bp(),a.c.J).innerText=b||pbc;J8(a.c.Kb(),fvc,true)}}
function GC(a,b,c){var d,e,f;up(c.a,Xvc);if(b<0){b=-b;up(c.a,$dc)}d=pbc+b;f=d.length;for(e=f;e<a.h;++e){up(c.a,Xkc)}for(e=0;e<f;++e){K2b(c,d.charCodeAt(e))}}
function sdb(a){var b,c;b=ZG(a.a.lb(iwc),43);if(b==null){c=KG(DS,321,1,[jwc,kwc,lwc,mwc,avc]);a.a.mb(iwc,c);return c}else{return b}}
function thb(b){var a,d,e,f;e=Tr(b.f.J,Ztc);if(T1b(e,pbc)){(Bp(),b.c.J).innerText=gvc}else{try{f=B0b(e);d=HC(b.a,f);(Bp(),b.c.J).innerText=d||pbc;shb(b,null)}catch(a){a=MS(a);if(aH(a,44)){shb(b,hvc)}else throw a}}}
function D0b(a){var b=y0b;!b&&(b=y0b=/^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i);if(b.test(a)){return parseFloat(a)}else{return Number.NaN}}
function HC(a,b){var c,d;d=I2b(new F2b);if(isNaN(b)){up(d.a,Yvc);return zp(d.a)}c=b<0||b==0&&1/b<0;M2b(d,c?a.l:a.o);if(!isFinite(b)){up(d.a,Zvc)}else{c&&(b=-b);b*=a.k;a.q?MC(a,b,d):NC(a,b,d,a.j)}M2b(d,c?a.m:a.p);return zp(d.a)}
function EC(a,b,c,d){CC();if(!c){throw M0b(new J0b,Wvc)}a.n=b;a.a=c[0];a.b=c[1];KC(a,a.n);if(!d&&a.e){a.i=c[2]&7;a.f=a.i}return a}
function MC(a,b,c){var d,e,f;if(b==0){NC(a,b,c,a.j);GC(a,0,c);return}d=kH(s1b(Math.log(b)/Math.log(10)));b/=Math.pow(10,d);f=a.j;if(a.g>1&&a.g>a.j){while(d%a.g!=0){b*=10;--d}f=1}else{if(a.j<1){++d;b/=10}else{for(e=1;e<a.j;++e){--d;b*=10}}}NC(a,b,c,f);GC(a,d,c)}
function v6(){q6=true;p6=(s6(),new i6);zn((wn(),vn),6);!!$stats&&$stats(eo(hwc,Bbc,null,null));p6.Hb();!!$stats&&$stats(eo(hwc,Wtc,null,null))}
function KC(a,b){var c,d;d=0;c=I2b(new F2b);d+=JC(a,b,d,c,false);a.o=zp(c.a);d+=LC(a,b,d,false);d+=JC(a,b,d,c,false);a.p=zp(c.a);if(d<b.length&&b.charCodeAt(d)==59){++d;d+=JC(a,b,d,c,true);a.l=zp(c.a);d+=LC(a,b,d,true);d+=JC(a,b,d,c,true);a.m=zp(c.a)}else{a.l=$dc+a.o;a.m=a.p}}
function JC(a,b,c,d,e){var f,g,h,i;N2b(d,0,zp(d.a).length,pbc);g=false;h=b.length;for(i=c;i<h;++i){f=b.charCodeAt(i);if(f==39){if(i+1<h&&b.charCodeAt(i+1)==39){++i;up(d.a,qvc)}else{g=!g}continue}if(g){vp(d.a,String.fromCharCode(f))}else{switch(f){case 35:case 48:case 44:case 46:case 59:return i-c;case 164:a.e=true;if(i+1<h&&b.charCodeAt(i+1)==164){++i;M2b(d,a.a)}else{M2b(d,a.b)}break;case 37:if(!e){if(a.k!=1){throw M0b(new J0b,$vc+b+Zlc)}a.k=100}up(d.a,_vc);break;case 8240:if(!e){if(a.k!=1){throw M0b(new J0b,$vc+b+Zlc)}a.k=1000}up(d.a,awc);break;case 45:up(d.a,$dc);break;default:vp(d.a,String.fromCharCode(f));}}}return h-c}
function uhb(b){var a,d;switch(b.e.J.selectedIndex){case 0:b.a=(CC(),!zC&&(zC=FC(new xC,owc,[pwc,qwc,2,qwc],false)),zC);MNb(b.d,b.a.n);b.d.J[Rgc]=!false;break;case 1:b.a=(CC(),!yC&&(yC=FC(new xC,rwc,[pwc,qwc,2,qwc],false)),yC);MNb(b.d,b.a.n);b.d.J[Rgc]=!false;break;case 2:b.a=(CC(),!BC&&(BC=FC(new xC,swc,[pwc,qwc,2,qwc],false)),BC);MNb(b.d,b.a.n);b.d.J[Rgc]=!false;break;case 3:b.a=(CC(),!AC&&(AC=FC(new xC,twc,[pwc,qwc,2,qwc],false)),AC);MNb(b.d,b.a.n);b.d.J[Rgc]=!false;break;case 4:b.d.J[Rgc]=!true;d=Tr(b.d.J,Ztc);try{b.a=(CC(),FC(new xC,d,[pwc,qwc,2,qwc],true))}catch(a){a=MS(a);if(aH(a,54)){shb(b,ivc);return}else throw a}}thb(b)}
function NC(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s;s=Math.pow(10,a.f);i=b.toFixed(a.f+3);q=0;l=0;h=i.indexOf(r2b(101));if(h!=-1){q=Math.floor(b)}else{f=i.indexOf(r2b(46));r=i.length;f==-1&&(f=r);f>0&&(q=B0b(i.substr(0,f-0)));if(f<r-1){l=B0b(i.substr(f+1,i.length-(f+1)));l=~~((~~Math.max(Math.min(l,2147483647),-2147483648)+500)/1000);if(l>=s){l-=s;++q}}}m=a.i>0||l>0;p=pbc+q;n=a.e?gwc:gwc;e=a.e?Gec:Gec;g=p.length;if(q>0||d>0){for(o=g;o<d;++o){up(c.a,Xkc)}for(o=0;o<g;++o){K2b(c,p.charCodeAt(o));g-o>1&&a.d>0&&(g-o)%a.d==1&&up(c.a,n)}}else !m&&up(c.a,Xkc);(a.c||m)&&up(c.a,e);k=pbc+Math.floor(l+s+0.5);j=k.length;while(k.charCodeAt(j-1)==48&&j>a.i+1){--j}for(o=1;o<j;++o){K2b(c,k.charCodeAt(o))}}
function rhb(a){var b,c,d,e,f,g,h,i,j,k,l,m;b=wIb(new tIb,4,2);b.l[nec]=5;a.e=yLb(new wLb);a.e.J.style[eec]=_tc;g=sdb(a.b);for(d=g,e=0,f=d.length;e<f;++e){c=d[e];CLb(a.e,c,-1)}R8(a.e,Bhb(new zhb,a),(Dt(),Dt(),Ct));b.Jc(0,0);h=(i=b.i.a.h.rows[0].cells[0],yHb(b,i,false),i);h.innerHTML=bvc;KHb(b,0,1,a.e);a.d=TNb(new INb);a.d.J.style[eec]=_tc;R8(a.d,Ghb(new Ehb,a),(hv(),hv(),gv));KHb(b,1,1,a.d);a.f=TNb(new INb);a.f.J.style[eec]=_tc;a.f.J[Ztc]=nwc;R8(a.f,Lhb(new Jhb,a),gv);b.Jc(2,0);j=(k=b.i.a.h.rows[2].cells[0],yHb(b,k,false),k);j.innerHTML=dvc;KHb(b,2,1,a.f);a.c=nFb(new lFb);a.c.J.style[eec]=_tc;b.Jc(3,0);l=(m=b.i.a.h.rows[3].cells[0],yHb(b,m,false),m);l.innerHTML=evc;KHb(b,3,1,a.c);uhb(a);return b}
function LC(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p;f=-1;g=0;p=0;h=0;j=-1;k=b.length;n=c;l=true;for(;n<k&&l;++n){e=b.charCodeAt(n);switch(e){case 35:p>0?++h:++g;j>=0&&f<0&&++j;break;case 48:if(h>0){throw M0b(new J0b,bwc+b+Zlc)}++p;j>=0&&f<0&&++j;break;case 44:j=0;break;case 46:if(f>=0){throw M0b(new J0b,cwc+b+Zlc)}f=g+p+h;break;case 69:if(!d){if(a.q){throw M0b(new J0b,dwc+b+Zlc)}a.q=true;a.h=0}while(n+1<k&&b.charCodeAt(n+1)==48){++n;!d&&++a.h}if(!d&&g+p<1||a.h<1){throw M0b(new J0b,ewc+b+Zlc)}l=false;break;default:--n;l=false;}}if(p==0&&g>0&&f>=0){m=f;m==0&&++m;h=g-m;g=m-1;p=1}if(f<0&&h>0||f>=0&&(f<g||f>g+p)||j==0){throw M0b(new J0b,fwc+b+Zlc)}if(d){return n-c}o=g+p+h;a.f=f>=0?o-f:0;if(f>=0){a.i=g+p-f;a.i<0&&(a.i=0)}i=f>=0?f:o;a.j=i-g;if(a.q){a.g=g+a.j;a.f==0&&a.j==0&&(a.j=1)}a.d=j>0?j:0;a.c=f==0||f==o;return n-c}
var twc='#,##0%',owc='#,##0.###',_vc='%',gwc=',',swc='0.###E0',nwc='31415926535.897932',xwc='AsyncLoader6',kwc='Currency',ywc='CwNumberFormat$1',zwc='CwNumberFormat$2',Awc='CwNumberFormat$3',jwc='Decimal',Xvc='E',ewc='Malformed exponential pattern "',fwc='Malformed pattern "',cwc='Multiple decimal separators in pattern "',dwc='Multiple exponential symbols in pattern "',vwc='NumberConstantsImpl_',wwc='NumberFormat',mwc='Percent',lwc='Scientific',$vc='Too many percent/per mille characters in pattern "',qwc='US$',pwc='USD',bwc="Unexpected '0' in pattern \"",Wvc='Unknown currency code',iwc='cwNumberFormatPatterns',hwc='runCallbacks6',rwc='\xA4#,##0.00;(\xA4#,##0.00)',Zvc='\u0221',awc='\u2030',Yvc='\uFFFD';_=xC.prototype=new wl;_.gC=OC;_.tI=0;_.a=null;_.b=null;_.c=false;_.d=3;_.e=false;_.f=3;_.g=40;_.h=0;_.i=0;_.j=1;_.k=1;_.l=$dc;_.m=pbc;_.n=null;_.o=pbc;_.p=pbc;_.q=false;var yC=null,zC=null,AC=null,BC=null;_=ED.prototype=new wl;_.gC=GD;_.tI=0;_=i6.prototype=new j6;_.gC=u6;_.Hb=y6;_.tI=0;_=zhb.prototype=new wl;_.gC=Chb;_.Y=Dhb;_.tI=101;_.a=null;_=Ehb.prototype=new wl;_.gC=Hhb;_._=Ihb;_.tI=102;_.a=null;_=Jhb.prototype=new wl;_.gC=Mhb;_._=Nhb;_.tI=103;_.a=null;var y0b=null;var CI=l0b(uwc,vwc),zI=l0b(Cnc,wwc),iL=l0b(Inc,xwc),dM=l0b(Lpc,ywc),eM=l0b(Lpc,zwc),fM=l0b(Lpc,Awc);v6();