function qC(){}
function xD(){}
function Z5(){}
function nhb(){}
function shb(){}
function xhb(){}
function zD(){return wI}
function HC(){return tI}
function j6(){return cL}
function qhb(){return ZL}
function vhb(){return $L}
function Ahb(){return _L}
function rhb(a){ihb(this.b)}
function whb(a){ihb(this.b)}
function Bhb(a){hhb(this.b)}
function uhb(a,b){a.b=b;return a}
function phb(a,b){a.b=b;return a}
function zhb(a,b){a.b=b;return a}
function Ghb(a){Lab(a.c,fhb(a.b))}
function lC(a){!a.d&&(a.d=new xD)}
function yC(a,b,c,d){vC();xC(a,b,c,d);return a}
function M1b(a,b,c,d){zp(a.b,b,c,d);return a}
function J1b(a,b){a.b.b+=String.fromCharCode(b);return a}
function A_b(a){var b;b=C_b(a);if(isNaN(b)){throw H0b(new F0b,Ekc+a+Fkc)}return b}
function n6(){var a;while(c6){a=c6;c6=c6.c;!c6&&(d6=null);Ghb(a.b)}}
function vC(){vC=$8b;lC((iC(),iC(),hC))}
function ghb(a,b){if(b==null){x8(a.d.Kb(),Mtc,false)}else{(Cp(),a.d.K).textContent=b||oac;x8(a.d.Kb(),Mtc,true)}}
function k6(){f6=true;e6=(h6(),new Z5);wn((tn(),sn),6);!!$stats&&$stats(ao(Ouc,Aac,null,null));e6.Hb();!!$stats&&$stats(ao(Ouc,Bsc,null,null))}
function FC(a,b,c){var d,e,f;if(b==0){GC(a,b,c,a.k);zC(a,0,c);return}d=dH(r0b(Math.log(b)/Math.log(10)));b/=Math.pow(10,d);f=a.k;if(a.h>1&&a.h>a.k){while(d%a.h!=0){b*=10;--d}f=1}else{if(a.k<1){++d;b/=10}else{for(e=1;e<a.k;++e){--d;b*=10}}}GC(a,b,c,f);zC(a,d,c)}
function C_b(a){var b=x_b;!b&&(b=x_b=/^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i);if(b.test(a)){return parseFloat(a)}else{return Number.NaN}}
function hhb(b){var a,d,e,f;e=Or(b.g.K,Esc);if(S0b(e,oac)){(Cp(),b.d.K).textContent=Ntc}else{try{f=A_b(e);d=AC(b.b,f);(Cp(),b.d.K).textContent=d||oac;ghb(b,null)}catch(a){a=BS(a);if(VG(a,44)){ghb(b,Otc)}else throw a}}}
function gdb(a){var b,c;b=SG(a.b.lb(Puc),43);if(b==null){c=DG(sS,320,1,[Quc,Ruc,Suc,Tuc,Htc]);a.b.mb(Puc,c);return c}else{return b}}
function zC(a,b,c){var d,e,f;c.b.b+=Cuc;if(b<0){b=-b;c.b.b+=Vcc}d=oac+b;f=d.length;for(e=f;e<a.i;++e){c.b.b+=Fjc}for(e=0;e<f;++e){J1b(c,d.charCodeAt(e))}}
function xC(a,b,c,d){vC();if(!c){throw L_b(new I_b,Buc)}a.o=b;a.b=c[0];a.c=c[1];DC(a,a.o);if(!d&&a.f){a.j=c[2]&7;a.g=a.j}return a}
function AC(a,b){var c,d;d=H1b(new E1b);if(isNaN(b)){d.b.b+=Duc;return d.b.b}c=b<0||b==0&&1/b<0;L1b(d,c?a.m:a.p);if(!isFinite(b)){d.b.b+=Euc}else{c&&(b=-b);b*=a.l;a.r?FC(a,b,d):GC(a,b,d,a.k)}L1b(d,c?a.n:a.q);return d.b.b}
function DC(a,b){var c,d;d=0;c=H1b(new E1b);d+=CC(a,b,d,c,false);a.p=c.b.b;d+=EC(a,b,d,false);d+=CC(a,b,d,c,false);a.q=c.b.b;if(d<b.length&&b.charCodeAt(d)==59){++d;d+=CC(a,b,d,c,true);a.m=c.b.b;d+=EC(a,b,d,true);d+=CC(a,b,d,c,true);a.n=c.b.b}else{a.m=Vcc+a.p;a.n=a.q}}
function CC(a,b,c,d,e){var f,g,h,i;M1b(d,0,d.b.b.length,oac);g=false;h=b.length;for(i=c;i<h;++i){f=b.charCodeAt(i);if(f==39){if(i+1<h&&b.charCodeAt(i+1)==39){++i;d.b.b+=Xtc}else{g=!g}continue}if(g){d.b.b+=String.fromCharCode(f)}else{switch(f){case 35:case 48:case 44:case 46:case 59:return i-c;case 164:a.f=true;if(i+1<h&&b.charCodeAt(i+1)==164){++i;L1b(d,a.b)}else{L1b(d,a.c)}break;case 37:if(!e){if(a.l!=1){throw L_b(new I_b,Fuc+b+Fkc)}a.l=100}d.b.b+=Guc;break;case 8240:if(!e){if(a.l!=1){throw L_b(new I_b,Fuc+b+Fkc)}a.l=1000}d.b.b+=Huc;break;case 45:d.b.b+=Vcc;break;default:d.b.b+=String.fromCharCode(f);}}}return h-c}
function ihb(b){var a,d;switch(b.f.K.selectedIndex){case 0:b.b=(vC(),!sC&&(sC=yC(new qC,Vuc,[Wuc,Xuc,2,Xuc],false)),sC);_Mb(b.e,b.b.o);b.e.K[Mfc]=!false;break;case 1:b.b=(vC(),!rC&&(rC=yC(new qC,Yuc,[Wuc,Xuc,2,Xuc],false)),rC);_Mb(b.e,b.b.o);b.e.K[Mfc]=!false;break;case 2:b.b=(vC(),!uC&&(uC=yC(new qC,Zuc,[Wuc,Xuc,2,Xuc],false)),uC);_Mb(b.e,b.b.o);b.e.K[Mfc]=!false;break;case 3:b.b=(vC(),!tC&&(tC=yC(new qC,$uc,[Wuc,Xuc,2,Xuc],false)),tC);_Mb(b.e,b.b.o);b.e.K[Mfc]=!false;break;case 4:b.e.K[Mfc]=!true;d=Or(b.e.K,Esc);try{b.b=(vC(),yC(new qC,d,[Wuc,Xuc,2,Xuc],true))}catch(a){a=BS(a);if(VG(a,54)){ghb(b,Ptc);return}else throw a}}hhb(b)}
function fhb(a){var b,c,d,e,f,g,h,i,j,k,l,m;b=$Hb(new XHb,4,2);b.m[hdc]=5;a.f=NKb(new LKb);a.f.K.style[_cc]=Gsc;g=gdb(a.c);for(d=g,e=0,f=d.length;e<f;++e){c=d[e];RKb(a.f,c,-1)}F8(a.f,phb(new nhb,a),(wt(),wt(),vt));b.Jc(0,0);h=(i=b.j.b.i.rows[0].cells[0],aHb(b,i,false),i);h.innerHTML=Itc;mHb(b,0,1,a.f);a.e=gNb(new XMb);a.e.K.style[_cc]=Gsc;F8(a.e,uhb(new shb,a),(av(),av(),_u));mHb(b,1,1,a.e);a.g=gNb(new XMb);a.g.K.style[_cc]=Gsc;a.g.K[Esc]=Uuc;F8(a.g,zhb(new xhb,a),_u);b.Jc(2,0);j=(k=b.j.b.i.rows[2].cells[0],aHb(b,k,false),k);j.innerHTML=Ktc;mHb(b,2,1,a.g);a.d=REb(new PEb);a.d.K.style[_cc]=Gsc;b.Jc(3,0);l=(m=b.j.b.i.rows[3].cells[0],aHb(b,m,false),m);l.innerHTML=Ltc;mHb(b,3,1,a.d);ihb(a);return b}
function GC(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s;s=Math.pow(10,a.g);i=b.toFixed(a.g+3);q=0;l=0;h=i.indexOf(q1b(101));if(h!=-1){q=Math.floor(b)}else{f=i.indexOf(q1b(46));r=i.length;f==-1&&(f=r);f>0&&(q=A_b(i.substr(0,f-0)));if(f<r-1){l=A_b(i.substr(f+1,i.length-(f+1)));l=~~((~~Math.max(Math.min(l,2147483647),-2147483648)+500)/1000);if(l>=s){l-=s;++q}}}m=a.j>0||l>0;p=oac+q;n=a.f?Nuc:Nuc;e=a.f?Bdc:Bdc;g=p.length;if(q>0||d>0){for(o=g;o<d;++o){c.b.b+=Fjc}for(o=0;o<g;++o){J1b(c,p.charCodeAt(o));g-o>1&&a.e>0&&(g-o)%a.e==1&&(c.b.b+=n,undefined)}}else !m&&(c.b.b+=Fjc,undefined);(a.d||m)&&(c.b.b+=e,undefined);k=oac+Math.floor(l+s+0.5);j=k.length;while(k.charCodeAt(j-1)==48&&j>a.j+1){--j}for(o=1;o<j;++o){J1b(c,k.charCodeAt(o))}}
function EC(a,b,c,d){var e,f,g,h,i,j,k,l,m,n,o,p;f=-1;g=0;p=0;h=0;j=-1;k=b.length;n=c;l=true;for(;n<k&&l;++n){e=b.charCodeAt(n);switch(e){case 35:p>0?++h:++g;j>=0&&f<0&&++j;break;case 48:if(h>0){throw L_b(new I_b,Iuc+b+Fkc)}++p;j>=0&&f<0&&++j;break;case 44:j=0;break;case 46:if(f>=0){throw L_b(new I_b,Juc+b+Fkc)}f=g+p+h;break;case 69:if(!d){if(a.r){throw L_b(new I_b,Kuc+b+Fkc)}a.r=true;a.i=0}while(n+1<k&&b.charCodeAt(n+1)==48){++n;!d&&++a.i}if(!d&&g+p<1||a.i<1){throw L_b(new I_b,Luc+b+Fkc)}l=false;break;default:--n;l=false;}}if(p==0&&g>0&&f>=0){m=f;m==0&&++m;h=g-m;g=m-1;p=1}if(f<0&&h>0||f>=0&&(f<g||f>g+p)||j==0){throw L_b(new I_b,Muc+b+Fkc)}if(d){return n-c}o=g+p+h;a.g=f>=0?o-f:0;if(f>=0){a.j=g+p-f;a.j<0&&(a.j=0)}i=f>=0?f:o;a.k=i-g;if(a.r){a.h=g+a.k;a.g==0&&a.k==0&&(a.k=1)}a.e=j>0?j:0;a.d=f==0||f==o;return n-c}
var $uc='#,##0%',Vuc='#,##0.###',Guc='%',Nuc=',',Zuc='0.###E0',Uuc='31415926535.897932',cvc='AsyncLoader6',Ruc='Currency',dvc='CwNumberFormat$1',evc='CwNumberFormat$2',fvc='CwNumberFormat$3',Quc='Decimal',Cuc='E',Luc='Malformed exponential pattern "',Muc='Malformed pattern "',Juc='Multiple decimal separators in pattern "',Kuc='Multiple exponential symbols in pattern "',avc='NumberConstantsImpl_',bvc='NumberFormat',Tuc='Percent',Suc='Scientific',Fuc='Too many percent/per mille characters in pattern "',Xuc='US$',Wuc='USD',Iuc="Unexpected '0' in pattern \"",Buc='Unknown currency code',Puc='cwNumberFormatPatterns',Ouc='runCallbacks6',Yuc='\xA4#,##0.00;(\xA4#,##0.00)',Euc='\u0221',Huc='\u2030',Duc='\uFFFD';_=qC.prototype=new sl;_.gC=HC;_.tI=0;_.b=null;_.c=null;_.d=false;_.e=3;_.f=false;_.g=3;_.h=40;_.i=0;_.j=0;_.k=1;_.l=1;_.m=Vcc;_.n=oac;_.o=null;_.p=oac;_.q=oac;_.r=false;var rC=null,sC=null,tC=null,uC=null;_=xD.prototype=new sl;_.gC=zD;_.tI=0;_=Z5.prototype=new $5;_.gC=j6;_.Hb=n6;_.tI=0;_=nhb.prototype=new sl;_.gC=qhb;_.Y=rhb;_.tI=101;_.b=null;_=shb.prototype=new sl;_.gC=vhb;_._=whb;_.tI=102;_.b=null;_=xhb.prototype=new sl;_.gC=Ahb;_._=Bhb;_.tI=103;_.b=null;var x_b=null;var wI=k_b(_uc,avc),tI=k_b(jmc,bvc),cL=k_b(pmc,cvc),ZL=k_b(soc,dvc),$L=k_b(soc,evc),_L=k_b(soc,fvc);k6();