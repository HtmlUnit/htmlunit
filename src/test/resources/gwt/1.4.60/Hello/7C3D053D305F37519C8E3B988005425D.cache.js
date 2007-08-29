(function(){var $wnd = window;var $doc = $wnd.document;var $moduleName, $moduleBase;var _,mn='com.google.gwt.core.client.',nn='com.google.gwt.lang.',on='com.google.gwt.sample.hello.client.',pn='com.google.gwt.user.client.',qn='com.google.gwt.user.client.impl.',rn='com.google.gwt.user.client.ui.',sn='com.google.gwt.user.client.ui.impl.',tn='java.lang.',un='java.util.';function ln(){}
function ei(a){return this===a;}
function fi(){return si(this);}
function ci(){}
_=ci.prototype={};_.eQ=ei;_.hC=fi;_.tI=1;var o=null;function r(a){return a==null?0:a.$H?a.$H:(a.$H=t());}
function s(a){return a==null?0:a.$H?a.$H:(a.$H=t());}
function t(){return ++u;}
var u=0;function x(b,a){if(!lb(a,2)){return false;}return B(b,kb(a,2));}
function y(a){return r(a);}
function z(){return [];}
function A(){return {};}
function C(a){return x(this,a);}
function B(a,b){return a===b;}
function D(){return y(this);}
function v(){}
_=v.prototype=new ci();_.eQ=C;_.hC=D;_.tI=7;function F(c,a,d,b,e){c.a=a;c.b=b;e;c.tI=d;return c;}
function bb(a,b,c){return a[b]=c;}
function cb(b,a){return b[a];}
function db(a){return a.length;}
function fb(e,d,c,b,a){return eb(e,d,c,b,0,db(b),a);}
function eb(j,i,g,c,e,a,b){var d,f,h;if((f=cb(c,e))<0){throw new ai();}h=F(new E(),f,cb(i,e),cb(g,e),j);++e;if(e<a){j=li(j,1);for(d=0;d<f;++d){bb(h,d,eb(j,i,g,c,e,a,b));}}else{for(d=0;d<f;++d){bb(h,d,b);}}return h;}
function gb(a,b,c){if(c!==null&&a.b!=0&& !lb(c,a.b)){throw new qh();}return bb(a,b,c);}
function E(){}
_=E.prototype=new ci();_.tI=0;function jb(b,a){return !(!(b&&ob[b][a]));}
function kb(b,a){if(b!=null)jb(b.tI,a)||nb();return b;}
function lb(b,a){return b!=null&&jb(b.tI,a);}
function nb(){throw new th();}
function mb(a){if(a!==null){throw new th();}return a;}
function pb(b,d){_=d.prototype;if(b&& !(b.tI>=_.tI)){var c=b.toString;for(var a in _){b[a]=_[a];}b.toString=c;}return b;}
var ob;function xb(b){var a;a=qe(new je(),'Click me',new tb());fe(Cf(),a);}
function sb(){}
_=sb.prototype=new ci();_.tI=0;function vb(a){fd('Hello, AJAX');}
function tb(){}
_=tb.prototype=new ci();_.u=vb;_.tI=8;function zb(){zb=ln;kc=Dk(new Bk());{gc=new qd();vd(gc);}}
function Ab(b,a){zb();yd(gc,b,a);}
function Bb(a,b){zb();return sd(gc,a,b);}
function Cb(){zb();return Ad(gc,'button');}
function Db(){zb();return Ad(gc,'div');}
function ac(b,a,d){zb();var c;c=o;{Fb(b,a,d);}}
function Fb(b,a,c){zb();var d;if(a===jc){if(cc(b)==8192){jc=null;}}d=Eb;Eb=b;try{c.t(b);}finally{Eb=d;}}
function bc(b,a){zb();Bd(gc,b,a);}
function cc(a){zb();return Cd(gc,a);}
function dc(a){zb();td(gc,a);}
function ec(a){zb();return Dd(gc,a);}
function fc(a){zb();return ud(gc,a);}
function hc(a){zb();var b,c;c=true;if(kc.b>0){b=mb(bl(kc,kc.b-1));if(!(c=null.D())){bc(a,true);dc(a);}}return c;}
function ic(b,a){zb();Ed(gc,b,a);}
function lc(a,b,c){zb();Fd(gc,a,b,c);}
function mc(a,b){zb();ae(gc,a,b);}
function nc(a,b){zb();be(gc,a,b);}
function oc(b,a,c){zb();ce(gc,b,a,c);}
function pc(a,b){zb();wd(gc,a,b);}
var Eb=null,gc=null,jc=null,kc;function sc(a){if(lb(a,4)){return Bb(this,kb(a,4));}return x(pb(this,qc),a);}
function tc(){return y(pb(this,qc));}
function qc(){}
_=qc.prototype=new v();_.eQ=sc;_.hC=tc;_.tI=9;function xc(a){return x(pb(this,uc),a);}
function yc(){return y(pb(this,uc));}
function uc(){}
_=uc.prototype=new v();_.eQ=xc;_.hC=yc;_.tI=10;function Ec(){Ec=ln;ad=Dk(new Bk());{Fc();}}
function Fc(){Ec();ed(new Ac());}
var ad;function Cc(){while((Ec(),ad).b>0){mb(bl((Ec(),ad),0)).D();}}
function Dc(){return null;}
function Ac(){}
_=Ac.prototype=new ci();_.x=Cc;_.y=Dc;_.tI=11;function dd(){dd=ln;gd=Dk(new Bk());od=Dk(new Bk());{kd();}}
function ed(a){dd();Ek(gd,a);}
function fd(a){dd();$wnd.alert(a);}
function hd(){dd();var a,b;for(a=jj(gd);cj(a);){b=kb(dj(a),5);b.x();}}
function id(){dd();var a,b,c,d;d=null;for(a=jj(gd);cj(a);){b=kb(dj(a),5);c=b.y();{d=c;}}return d;}
function jd(){dd();var a,b;for(a=jj(od);cj(a);){b=mb(dj(a));null.D();}}
function kd(){dd();__gwt_initHandlers(function(){nd();},function(){return md();},function(){ld();$wnd.onresize=null;$wnd.onbeforeclose=null;$wnd.onclose=null;});}
function ld(){dd();var a;a=o;{hd();}}
function md(){dd();var a;a=o;{return id();}}
function nd(){dd();var a;a=o;{jd();}}
var gd,od;function yd(c,b,a){b.appendChild(a);}
function Ad(b,a){return $doc.createElement(a);}
function Bd(c,b,a){b.cancelBubble=a;}
function Cd(b,a){switch(a.type){case 'blur':return 4096;case 'change':return 1024;case 'click':return 1;case 'dblclick':return 2;case 'focus':return 2048;case 'keydown':return 128;case 'keypress':return 256;case 'keyup':return 512;case 'load':return 32768;case 'losecapture':return 8192;case 'mousedown':return 4;case 'mousemove':return 64;case 'mouseout':return 32;case 'mouseover':return 16;case 'mouseup':return 8;case 'scroll':return 16384;case 'error':return 65536;case 'mousewheel':return 131072;case 'DOMMouseScroll':return 131072;}}
function Dd(b,a){return a.__eventBits||0;}
function Ed(c,b,a){b.removeChild(a);}
function Fd(c,a,b,d){a[b]=d;}
function ae(c,a,b){a.__listener=b;}
function be(c,a,b){if(!b){b='';}a.innerHTML=b;}
function ce(c,b,a,d){b.style[a]=d;}
function pd(){}
_=pd.prototype=new ci();_.tI=0;function sd(c,a,b){if(!a&& !b)return true;else if(!a|| !b)return false;return a.uniqueID==b.uniqueID;}
function td(b,a){a.returnValue=false;}
function ud(c,a){var b=a.parentElement;return b||null;}
function vd(d){try{$doc.execCommand('BackgroundImageCache',false,true);}catch(a){}$wnd.__dispatchEvent=function(){var c=xd;xd=this;if($wnd.event.returnValue==null){$wnd.event.returnValue=true;if(!hc($wnd.event)){xd=c;return;}}var b,a=this;while(a&& !(b=a.__listener))a=a.parentElement;if(b)ac($wnd.event,a,b);xd=c;};$wnd.__dispatchDblClickEvent=function(){var a=$doc.createEventObject();this.fireEvent('onclick',a);if(this.__eventBits&2)$wnd.__dispatchEvent.call(this);};$doc.body.onclick=$doc.body.onmousedown=$doc.body.onmouseup=$doc.body.onmousemove=$doc.body.onmousewheel=$doc.body.onkeydown=$doc.body.onkeypress=$doc.body.onkeyup=$doc.body.onfocus=$doc.body.onblur=$doc.body.ondblclick=$wnd.__dispatchEvent;}
function wd(c,b,a){b.__eventBits=a;b.onclick=a&1?$wnd.__dispatchEvent:null;b.ondblclick=a&(1|2)?$wnd.__dispatchDblClickEvent:null;b.onmousedown=a&4?$wnd.__dispatchEvent:null;b.onmouseup=a&8?$wnd.__dispatchEvent:null;b.onmouseover=a&16?$wnd.__dispatchEvent:null;b.onmouseout=a&32?$wnd.__dispatchEvent:null;b.onmousemove=a&64?$wnd.__dispatchEvent:null;b.onkeydown=a&128?$wnd.__dispatchEvent:null;b.onkeypress=a&256?$wnd.__dispatchEvent:null;b.onkeyup=a&512?$wnd.__dispatchEvent:null;b.onchange=a&1024?$wnd.__dispatchEvent:null;b.onfocus=a&2048?$wnd.__dispatchEvent:null;b.onblur=a&4096?$wnd.__dispatchEvent:null;b.onlosecapture=a&8192?$wnd.__dispatchEvent:null;b.onscroll=a&16384?$wnd.__dispatchEvent:null;b.onload=a&32768?$wnd.__dispatchEvent:null;b.onerror=a&65536?$wnd.__dispatchEvent:null;b.onmousewheel=a&131072?$wnd.__dispatchEvent:null;}
function qd(){}
_=qd.prototype=new pd();_.tI=0;var xd=null;function fg(d,b,a){var c=b.parentNode;if(!c){return;}c.insertBefore(a,b);c.removeChild(b);}
function gg(b,a){if(b.d!==null){fg(b,b.d,a);}b.d=a;}
function hg(b,a){jg(b.d,a);}
function ig(b,a){pc(b.d,a|ec(b.d));}
function jg(a,b){lc(a,'className',b);}
function dg(){}
_=dg.prototype=new ci();_.tI=0;_.d=null;function Cg(a){if(a.b){throw Bh(new Ah(),"Should only call onAttach when the widget is detached from the browser's document");}a.b=true;mc(a.d,a);a.i();a.v();}
function Dg(a){if(!a.b){throw Bh(new Ah(),"Should only call onDetach when the widget is attached to the browser's document");}try{a.w();}finally{a.j();mc(a.d,null);a.b=false;}}
function Eg(a){if(a.c!==null){he(a.c,a);}else if(a.c!==null){throw Bh(new Ah(),"This widget's parent does not implement HasWidgets");}}
function Fg(b,a){if(b.b){mc(b.d,null);}gg(b,a);if(b.b){mc(a,b);}}
function ah(c,b){var a;a=c.c;if(b===null){if(a!==null&&a.b){Dg(c);}c.c=null;}else{if(a!==null){throw Bh(new Ah(),'Cannot set a new parent without first clearing the old parent');}c.c=b;if(b.b){Cg(c);}}}
function bh(){}
function ch(){}
function dh(a){}
function eh(){}
function fh(){}
function gh(a){Fg(this,a);}
function kg(){}
_=kg.prototype=new dg();_.i=bh;_.j=ch;_.t=dh;_.v=eh;_.w=fh;_.A=gh;_.tI=12;_.b=false;_.c=null;function nf(b,a){ah(a,b);}
function pf(b,a){ah(a,null);}
function qf(){var a,b;for(b=this.q();pg(b);){a=qg(b);Cg(a);}}
function rf(){var a,b;for(b=this.q();pg(b);){a=qg(b);Dg(a);}}
function sf(){}
function tf(){}
function mf(){}
_=mf.prototype=new kg();_.i=qf;_.j=rf;_.v=sf;_.w=tf;_.tI=13;function ze(a){a.a=tg(new lg(),a);}
function Ae(a){ze(a);return a;}
function Be(c,a,b){Eg(a);ug(c.a,a);Ab(b,a.d);nf(c,a);}
function De(b,c){var a;if(c.c!==b){return false;}pf(b,c);a=c.d;ic(fc(a),a);Ag(b.a,c);return true;}
function Ee(){return yg(this.a);}
function ye(){}
_=ye.prototype=new mf();_.q=Ee;_.tI=14;function ee(a){Ae(a);a.A(Db());oc(a.d,'position','relative');oc(a.d,'overflow','hidden');return a;}
function fe(a,b){Be(a,b,a.d);}
function he(b,c){var a;a=De(b,c);if(a){ie(c.d);}return a;}
function ie(a){oc(a,'left','');oc(a,'top','');oc(a,'position','');}
function de(){}
_=de.prototype=new ye();_.tI=15;function cf(){cf=ln;mh(),oh;}
function af(b,a){mh(),oh;df(b,a);return b;}
function bf(b,a){if(b.a===null){b.a=ue(new te());}Ek(b.a,a);}
function df(b,a){Fg(b,a);ig(b,7041);}
function ef(a){switch(cc(a)){case 1:if(this.a!==null){we(this.a,this);}break;case 4096:case 2048:break;case 128:case 512:case 256:break;}}
function ff(a){df(this,a);}
function Fe(){}
_=Fe.prototype=new kg();_.t=ef;_.A=ff;_.tI=16;_.a=null;function me(){me=ln;mh(),oh;}
function le(b,a){mh(),oh;af(b,a);return b;}
function ne(b,a){nc(b.d,a);}
function ke(){}
_=ke.prototype=new Fe();_.tI=17;function re(){re=ln;mh(),oh;}
function oe(a){mh(),oh;le(a,Cb());se(a.d);hg(a,'gwt-Button');return a;}
function pe(b,a){mh(),oh;oe(b);ne(b,a);return b;}
function qe(c,a,b){mh(),oh;pe(c,a);bf(c,b);return c;}
function se(b){re();if(b.type=='submit'){try{b.setAttribute('type','button');}catch(a){}}}
function je(){}
_=je.prototype=new ke();_.tI=18;function Ai(d,a,b){var c;while(a.p()){c=a.s();if(b===null?c===null:b.eQ(c)){return a;}}return null;}
function Ci(a){throw xi(new wi(),'add');}
function Di(b){var a;a=Ai(this,this.q(),b);return a!==null;}
function zi(){}
_=zi.prototype=new ci();_.f=Ci;_.h=Di;_.tI=0;function ij(b,a){throw Eh(new Dh(),'Index: '+a+', Size: '+b.b);}
function jj(a){return aj(new Fi(),a);}
function kj(b,a){throw xi(new wi(),'add');}
function lj(a){this.e(this.B(),a);return true;}
function mj(e){var a,b,c,d,f;if(e===this){return true;}if(!lb(e,12)){return false;}f=kb(e,12);if(this.B()!=f.B()){return false;}c=jj(this);d=f.q();while(cj(c)){a=dj(c);b=dj(d);if(!(a===null?b===null:a.eQ(b))){return false;}}return true;}
function nj(){var a,b,c,d;c=1;a=31;b=jj(this);while(cj(b)){d=dj(b);c=31*c+(d===null?0:d.hC());}return c;}
function oj(){return jj(this);}
function pj(a){throw xi(new wi(),'remove');}
function Ei(){}
_=Ei.prototype=new zi();_.e=kj;_.f=lj;_.eQ=mj;_.hC=nj;_.q=oj;_.z=pj;_.tI=19;function Ck(a){{Fk(a);}}
function Dk(a){Ck(a);return a;}
function Ek(b,a){ol(b.a,b.b++,a);return true;}
function Fk(a){a.a=z();a.b=0;}
function bl(b,a){if(a<0||a>=b.b){ij(b,a);}return kl(b.a,a);}
function cl(b,a){return dl(b,a,0);}
function dl(c,b,a){if(a<0){ij(c,a);}for(;a<c.b;++a){if(jl(b,kl(c.a,a))){return a;}}return (-1);}
function el(c,a){var b;b=bl(c,a);ml(c.a,a,1);--c.b;return b;}
function gl(a,b){if(a<0||a>this.b){ij(this,a);}fl(this.a,a,b);++this.b;}
function hl(a){return Ek(this,a);}
function fl(a,b,c){a.splice(b,0,c);}
function il(a){return cl(this,a)!=(-1);}
function jl(a,b){return a===b||a!==null&&a.eQ(b);}
function ll(a){return bl(this,a);}
function kl(a,b){return a[b];}
function nl(a){return el(this,a);}
function ml(a,c,b){a.splice(c,b);}
function ol(a,b,c){a[b]=c;}
function pl(){return this.b;}
function Bk(){}
_=Bk.prototype=new Ei();_.e=gl;_.f=hl;_.h=il;_.n=ll;_.z=nl;_.B=pl;_.tI=20;_.a=null;_.b=0;function ue(a){Dk(a);return a;}
function we(d,c){var a,b;for(a=jj(d);cj(a);){b=kb(dj(a),6);b.u(c);}}
function te(){}
_=te.prototype=new Bk();_.tI=21;function Af(){Af=ln;Ff=lm(new sl());}
function zf(b,a){Af();ee(b);if(a===null){a=Bf();}b.A(a);Cg(b);return b;}
function Cf(){Af();return Df(null);}
function Df(c){Af();var a,b;b=kb(rm(Ff,c),7);if(b!==null){return b;}a=null;if(Ff.c==0){Ef();}sm(Ff,c,b=zf(new uf(),a));return b;}
function Bf(){Af();return $doc.body;}
function Ef(){Af();ed(new vf());}
function uf(){}
_=uf.prototype=new de();_.tI=22;var Ff;function xf(){var a,b;for(b=ck(qk((Af(),Ff)));jk(b);){a=kb(kk(b),7);if(a.b){Dg(a);}}}
function yf(){return null;}
function vf(){}
_=vf.prototype=new ci();_.x=xf;_.y=yf;_.tI=23;function tg(b,a){b.a=fb('[Lcom.google.gwt.user.client.ui.Widget;',[0],[9],[4],null);return b;}
function ug(a,b){xg(a,b,a.b);}
function wg(b,c){var a;for(a=0;a<b.b;++a){if(b.a[a]===c){return a;}}return (-1);}
function xg(d,e,a){var b,c;if(a<0||a>d.b){throw new Dh();}if(d.b==d.a.a){c=fb('[Lcom.google.gwt.user.client.ui.Widget;',[0],[9],[d.a.a*2],null);for(b=0;b<d.a.a;++b){gb(c,b,d.a[b]);}d.a=c;}++d.b;for(b=d.b-1;b>a;--b){gb(d.a,b,d.a[b-1]);}gb(d.a,a,e);}
function yg(a){return ng(new mg(),a);}
function zg(c,b){var a;if(b<0||b>=c.b){throw new Dh();}--c.b;for(a=b;a<c.b;++a){gb(c.a,a,c.a[a+1]);}gb(c.a,c.b,null);}
function Ag(b,c){var a;a=wg(b,c);if(a==(-1)){throw new gn();}zg(b,a);}
function lg(){}
_=lg.prototype=new ci();_.tI=0;_.a=null;_.b=0;function ng(b,a){b.b=a;return b;}
function pg(a){return a.a<a.b.b-1;}
function qg(a){if(a.a>=a.b.b){throw new gn();}return a.b.a[++a.a];}
function rg(){return pg(this);}
function sg(){return qg(this);}
function mg(){}
_=mg.prototype=new ci();_.p=rg;_.s=sg;_.tI=0;_.a=(-1);function mh(){mh=ln;nh=jh(new ih());oh=nh;}
function lh(a){mh();return a;}
function hh(){}
_=hh.prototype=new ci();_.tI=0;var nh,oh;function kh(){kh=ln;mh();}
function jh(a){kh();lh(a);return a;}
function ih(){}
_=ih.prototype=new hh();_.tI=0;function ui(b,a){a;return b;}
function ti(){}
_=ti.prototype=new ci();_.tI=3;function yh(b,a){ui(b,a);return b;}
function xh(){}
_=xh.prototype=new ti();_.tI=4;function hi(b,a){yh(b,a);return b;}
function gi(){}
_=gi.prototype=new xh();_.tI=5;function qh(){}
_=qh.prototype=new gi();_.tI=24;function th(){}
_=th.prototype=new gi();_.tI=25;function Bh(b,a){hi(b,a);return b;}
function Ah(){}
_=Ah.prototype=new gi();_.tI=26;function Eh(b,a){hi(b,a);return b;}
function Dh(){}
_=Dh.prototype=new gi();_.tI=27;function ai(){}
_=ai.prototype=new gi();_.tI=28;function li(b,a){return b.substr(a,b.length-a);}
function mi(a,b){return String(a)==b;}
function ni(a){if(!lb(a,1))return false;return mi(this,a);}
function pi(){var a=oi;if(!a){a=oi={};}var e=':'+this;var b=a[e];if(b==null){b=0;var f=this.length;var d=f<64?1:f/32|0;for(var c=0;c<f;c+=d){b<<=1;b+=this.charCodeAt(c);}b|=0;a[e]=b;}return b;}
_=String.prototype;_.eQ=ni;_.hC=pi;_.tI=2;var oi=null;function si(a){return s(a);}
function xi(b,a){hi(b,a);return b;}
function wi(){}
_=wi.prototype=new gi();_.tI=29;function aj(b,a){b.c=a;return b;}
function cj(a){return a.a<a.c.B();}
function dj(a){if(!cj(a)){throw new gn();}return a.c.n(a.b=a.a++);}
function ej(a){if(a.b<0){throw new Ah();}a.c.z(a.b);a.a=a.b;a.b=(-1);}
function fj(){return cj(this);}
function gj(){return dj(this);}
function Fi(){}
_=Fi.prototype=new ci();_.p=fj;_.s=gj;_.tI=0;_.a=0;_.b=(-1);function ok(f,d,e){var a,b,c;for(b=gm(f.k());Fl(b);){a=am(b);c=a.l();if(d===null?c===null:d.eQ(c)){if(e){bm(b);}return a;}}return null;}
function pk(b){var a;a=b.k();return sj(new rj(),b,a);}
function qk(b){var a;a=qm(b);return ak(new Fj(),b,a);}
function rk(a){return ok(this,a,false)!==null;}
function sk(d){var a,b,c,e,f,g,h;if(d===this){return true;}if(!lb(d,13)){return false;}f=kb(d,13);c=pk(this);e=f.r();if(!yk(c,e)){return false;}for(a=uj(c);Bj(a);){b=Cj(a);h=this.o(b);g=f.o(b);if(h===null?g!==null:!h.eQ(g)){return false;}}return true;}
function tk(b){var a;a=ok(this,b,false);return a===null?null:a.m();}
function uk(){var a,b,c;b=0;for(c=gm(this.k());Fl(c);){a=am(c);b+=a.hC();}return b;}
function vk(){return pk(this);}
function qj(){}
_=qj.prototype=new ci();_.g=rk;_.eQ=sk;_.o=tk;_.hC=uk;_.r=vk;_.tI=30;function yk(e,b){var a,c,d;if(b===e){return true;}if(!lb(b,14)){return false;}c=kb(b,14);if(c.B()!=e.B()){return false;}for(a=c.q();a.p();){d=a.s();if(!e.h(d)){return false;}}return true;}
function zk(a){return yk(this,a);}
function Ak(){var a,b,c;a=0;for(b=this.q();b.p();){c=b.s();if(c!==null){a+=c.hC();}}return a;}
function wk(){}
_=wk.prototype=new zi();_.eQ=zk;_.hC=Ak;_.tI=31;function sj(b,a,c){b.a=a;b.b=c;return b;}
function uj(b){var a;a=gm(b.b);return zj(new yj(),b,a);}
function vj(a){return this.a.g(a);}
function wj(){return uj(this);}
function xj(){return this.b.a.c;}
function rj(){}
_=rj.prototype=new wk();_.h=vj;_.q=wj;_.B=xj;_.tI=32;function zj(b,a,c){b.a=c;return b;}
function Bj(a){return a.a.p();}
function Cj(b){var a;a=b.a.s();return a.l();}
function Dj(){return Bj(this);}
function Ej(){return Cj(this);}
function yj(){}
_=yj.prototype=new ci();_.p=Dj;_.s=Ej;_.tI=0;function ak(b,a,c){b.a=a;b.b=c;return b;}
function ck(b){var a;a=gm(b.b);return hk(new gk(),b,a);}
function dk(a){return pm(this.a,a);}
function ek(){return ck(this);}
function fk(){return this.b.a.c;}
function Fj(){}
_=Fj.prototype=new zi();_.h=dk;_.q=ek;_.B=fk;_.tI=0;function hk(b,a,c){b.a=c;return b;}
function jk(a){return a.a.p();}
function kk(a){var b;b=a.a.s().m();return b;}
function lk(){return jk(this);}
function mk(){return kk(this);}
function gk(){}
_=gk.prototype=new ci();_.p=lk;_.s=mk;_.tI=0;function nm(){nm=ln;um=Am();}
function km(a){{mm(a);}}
function lm(a){nm();km(a);return a;}
function mm(a){a.a=z();a.d=A();a.b=pb(um,v);a.c=0;}
function om(b,a){if(lb(a,1)){return Em(b.d,kb(a,1))!==um;}else if(a===null){return b.b!==um;}else{return Dm(b.a,a,a.hC())!==um;}}
function pm(a,b){if(a.b!==um&&Cm(a.b,b)){return true;}else if(zm(a.d,b)){return true;}else if(xm(a.a,b)){return true;}return false;}
function qm(a){return em(new Bl(),a);}
function rm(c,a){var b;if(lb(a,1)){b=Em(c.d,kb(a,1));}else if(a===null){b=c.b;}else{b=Dm(c.a,a,a.hC());}return b===um?null:b;}
function sm(c,a,d){var b;{b=c.b;c.b=d;}if(b===um){++c.c;return null;}else{return b;}}
function tm(c,a){var b;if(lb(a,1)){b=bn(c.d,kb(a,1));}else if(a===null){b=c.b;c.b=pb(um,v);}else{b=an(c.a,a,a.hC());}if(b===um){return null;}else{--c.c;return b;}}
function vm(e,c){nm();for(var d in e){if(d==parseInt(d)){var a=e[d];for(var f=0,b=a.length;f<b;++f){c.f(a[f]);}}}}
function wm(d,a){nm();for(var c in d){if(c.charCodeAt(0)==58){var e=d[c];var b=wl(c.substring(1),e);a.f(b);}}}
function xm(f,h){nm();for(var e in f){if(e==parseInt(e)){var a=f[e];for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.m();if(Cm(h,d)){return true;}}}}return false;}
function ym(a){return om(this,a);}
function zm(c,d){nm();for(var b in c){if(b.charCodeAt(0)==58){var a=c[b];if(Cm(d,a)){return true;}}}return false;}
function Am(){nm();}
function Bm(){return qm(this);}
function Cm(a,b){nm();if(a===b){return true;}else if(a===null){return false;}else{return a.eQ(b);}}
function Fm(a){return rm(this,a);}
function Dm(f,h,e){nm();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.l();if(Cm(h,d)){return c.m();}}}}
function Em(b,a){nm();return b[':'+a];}
function an(f,h,e){nm();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.l();if(Cm(h,d)){if(a.length==1){delete f[e];}else{a.splice(g,1);}return c.m();}}}}
function bn(c,a){nm();a=':'+a;var b=c[a];delete c[a];return b;}
function sl(){}
_=sl.prototype=new qj();_.g=ym;_.k=Bm;_.o=Fm;_.tI=33;_.a=null;_.b=null;_.c=0;_.d=null;var um;function ul(b,a,c){b.a=a;b.b=c;return b;}
function wl(a,b){return ul(new tl(),a,b);}
function xl(b){var a;if(lb(b,15)){a=kb(b,15);if(Cm(this.a,a.l())&&Cm(this.b,a.m())){return true;}}return false;}
function yl(){return this.a;}
function zl(){return this.b;}
function Al(){var a,b;a=0;b=0;if(this.a!==null){a=this.a.hC();}if(this.b!==null){b=this.b.hC();}return a^b;}
function tl(){}
_=tl.prototype=new ci();_.eQ=xl;_.l=yl;_.m=zl;_.hC=Al;_.tI=34;_.a=null;_.b=null;function em(b,a){b.a=a;return b;}
function gm(a){return Dl(new Cl(),a.a);}
function hm(c){var a,b,d;if(lb(c,15)){a=kb(c,15);b=a.l();if(om(this.a,b)){d=rm(this.a,b);return Cm(a.m(),d);}}return false;}
function im(){return gm(this);}
function jm(){return this.a.c;}
function Bl(){}
_=Bl.prototype=new wk();_.h=hm;_.q=im;_.B=jm;_.tI=35;function Dl(c,b){var a;c.c=b;a=Dk(new Bk());if(c.c.b!==(nm(),um)){Ek(a,ul(new tl(),null,c.c.b));}wm(c.c.d,a);vm(c.c.a,a);c.a=jj(a);return c;}
function Fl(a){return cj(a.a);}
function am(a){return a.b=kb(dj(a.a),15);}
function bm(a){if(a.b===null){throw Bh(new Ah(),'Must call next() before remove().');}else{ej(a.a);tm(a.c,a.b.l());a.b=null;}}
function cm(){return Fl(this);}
function dm(){return am(this);}
function Cl(){}
_=Cl.prototype=new ci();_.p=cm;_.s=dm;_.tI=0;_.a=null;_.b=null;function gn(){}
_=gn.prototype=new gi();_.tI=36;function ph(){xb(new sb());}
function gwtOnLoad(b,d,c){$moduleName=d;$moduleBase=c;if(b)try{ph();}catch(a){b(d);}else{ph();}}
var ob=[{},{},{1:1},{3:1},{3:1},{3:1},{3:1},{2:1},{6:1},{2:1,4:1},{2:1},{5:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{12:1},{12:1},{12:1},{7:1,8:1,9:1,10:1,11:1},{5:1},{3:1},{3:1},{3:1},{3:1},{3:1},{3:1},{13:1},{14:1},{14:1},{13:1},{15:1},{14:1},{3:1}];if (com_google_gwt_sample_hello_Hello) {  var __gwt_initHandlers = com_google_gwt_sample_hello_Hello.__gwt_initHandlers;  com_google_gwt_sample_hello_Hello.onScriptLoad(gwtOnLoad);}})();