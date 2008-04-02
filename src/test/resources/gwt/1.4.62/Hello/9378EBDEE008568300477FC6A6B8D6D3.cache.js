(function(){var $wnd = window;var $doc = $wnd.document;var $moduleName, $moduleBase;var _,on='com.google.gwt.core.client.',pn='com.google.gwt.lang.',qn='com.google.gwt.sample.hello.client.',rn='com.google.gwt.user.client.',sn='com.google.gwt.user.client.impl.',tn='com.google.gwt.user.client.ui.',un='com.google.gwt.user.client.ui.impl.',vn='java.lang.',wn='java.util.';function nn(){}
function gi(a){return this===a;}
function hi(){return ui(this);}
function ei(){}
_=ei.prototype={};_.eQ=gi;_.hC=hi;_.tI=1;var o=null;function r(a){return a==null?0:a.$H?a.$H:(a.$H=t());}
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
_=v.prototype=new ei();_.eQ=C;_.hC=D;_.tI=7;function F(c,a,d,b,e){c.a=a;c.b=b;e;c.tI=d;return c;}
function bb(a,b,c){return a[b]=c;}
function cb(b,a){return b[a];}
function db(a){return a.length;}
function fb(e,d,c,b,a){return eb(e,d,c,b,0,db(b),a);}
function eb(j,i,g,c,e,a,b){var d,f,h;if((f=cb(c,e))<0){throw new ci();}h=F(new E(),f,cb(i,e),cb(g,e),j);++e;if(e<a){j=ni(j,1);for(d=0;d<f;++d){bb(h,d,eb(j,i,g,c,e,a,b));}}else{for(d=0;d<f;++d){bb(h,d,b);}}return h;}
function gb(a,b,c){if(c!==null&&a.b!=0&& !lb(c,a.b)){throw new sh();}return bb(a,b,c);}
function E(){}
_=E.prototype=new ei();_.tI=0;function jb(b,a){return !(!(b&&ob[b][a]));}
function kb(b,a){if(b!=null)jb(b.tI,a)||nb();return b;}
function lb(b,a){return b!=null&&jb(b.tI,a);}
function nb(){throw new vh();}
function mb(a){if(a!==null){throw new vh();}return a;}
function pb(b,d){_=d.prototype;if(b&& !(b.tI>=_.tI)){var c=b.toString;for(var a in _){b[a]=_[a];}b.toString=c;}return b;}
var ob;function xb(b){var a;a=ve(new oe(),'Click me',new tb());ke(bg(),a);}
function sb(){}
_=sb.prototype=new ei();_.tI=0;function vb(a){fd('Hello, AJAX');}
function tb(){}
_=tb.prototype=new ei();_.u=vb;_.tI=8;function zb(){zb=nn;kc=Fk(new Dk());{gc=new qd();ud(gc);}}
function Ab(b,a){zb();Dd(gc,b,a);}
function Bb(a,b){zb();return sd(gc,a,b);}
function Cb(){zb();return Fd(gc,'button');}
function Db(){zb();return Fd(gc,'div');}
function ac(b,a,d){zb();var c;c=o;{Fb(b,a,d);}}
function Fb(b,a,c){zb();var d;if(a===jc){if(cc(b)==8192){jc=null;}}d=Eb;Eb=b;try{c.t(b);}finally{Eb=d;}}
function bc(b,a){zb();ae(gc,b,a);}
function cc(a){zb();return be(gc,a);}
function dc(a){zb();zd(gc,a);}
function ec(a){zb();return ce(gc,a);}
function fc(a){zb();return Ad(gc,a);}
function hc(a){zb();var b,c;c=true;if(kc.b>0){b=mb(dl(kc,kc.b-1));if(!(c=null.D())){bc(a,true);dc(a);}}return c;}
function ic(b,a){zb();de(gc,b,a);}
function lc(a,b,c){zb();ee(gc,a,b,c);}
function mc(a,b){zb();fe(gc,a,b);}
function nc(a,b){zb();ge(gc,a,b);}
function oc(b,a,c){zb();he(gc,b,a,c);}
function pc(a,b){zb();wd(gc,a,b);}
var Eb=null,gc=null,jc=null,kc;function sc(a){if(lb(a,4)){return Bb(this,kb(a,4));}return x(pb(this,qc),a);}
function tc(){return y(pb(this,qc));}
function qc(){}
_=qc.prototype=new v();_.eQ=sc;_.hC=tc;_.tI=9;function xc(a){return x(pb(this,uc),a);}
function yc(){return y(pb(this,uc));}
function uc(){}
_=uc.prototype=new v();_.eQ=xc;_.hC=yc;_.tI=10;function Ec(){Ec=nn;ad=Fk(new Dk());{Fc();}}
function Fc(){Ec();ed(new Ac());}
var ad;function Cc(){while((Ec(),ad).b>0){mb(dl((Ec(),ad),0)).D();}}
function Dc(){return null;}
function Ac(){}
_=Ac.prototype=new ei();_.x=Cc;_.y=Dc;_.tI=11;function dd(){dd=nn;gd=Fk(new Dk());od=Fk(new Dk());{kd();}}
function ed(a){dd();al(gd,a);}
function fd(a){dd();$wnd.alert(a);}
function hd(){dd();var a,b;for(a=lj(gd);ej(a);){b=kb(fj(a),5);b.x();}}
function id(){dd();var a,b,c,d;d=null;for(a=lj(gd);ej(a);){b=kb(fj(a),5);c=b.y();{d=c;}}return d;}
function jd(){dd();var a,b;for(a=lj(od);ej(a);){b=mb(fj(a));null.D();}}
function kd(){dd();__gwt_initHandlers(function(){nd();},function(){return md();},function(){ld();$wnd.onresize=null;$wnd.onbeforeclose=null;$wnd.onclose=null;});}
function ld(){dd();var a;a=o;{hd();}}
function md(){dd();var a;a=o;{return id();}}
function nd(){dd();var a;a=o;{jd();}}
var gd,od;function Dd(c,b,a){b.appendChild(a);}
function Fd(b,a){return $doc.createElement(a);}
function ae(c,b,a){b.cancelBubble=a;}
function be(b,a){switch(a.type){case 'blur':return 4096;case 'change':return 1024;case 'click':return 1;case 'dblclick':return 2;case 'focus':return 2048;case 'keydown':return 128;case 'keypress':return 256;case 'keyup':return 512;case 'load':return 32768;case 'losecapture':return 8192;case 'mousedown':return 4;case 'mousemove':return 64;case 'mouseout':return 32;case 'mouseover':return 16;case 'mouseup':return 8;case 'scroll':return 16384;case 'error':return 65536;case 'mousewheel':return 131072;case 'DOMMouseScroll':return 131072;}}
function ce(b,a){return a.__eventBits||0;}
function de(c,b,a){b.removeChild(a);}
function ee(c,a,b,d){a[b]=d;}
function fe(c,a,b){a.__listener=b;}
function ge(c,a,b){if(!b){b='';}a.innerHTML=b;}
function he(c,b,a,d){b.style[a]=d;}
function pd(){}
_=pd.prototype=new ei();_.tI=0;function zd(b,a){a.preventDefault();}
function Ad(c,a){var b=a.parentNode;if(b==null){return null;}if(b.nodeType!=1)b=null;return b||null;}
function Bd(d){$wnd.__dispatchCapturedMouseEvent=function(b){if($wnd.__dispatchCapturedEvent(b)){var a=$wnd.__captureElem;if(a&&a.__listener){ac(b,a,a.__listener);b.stopPropagation();}}};$wnd.__dispatchCapturedEvent=function(a){if(!hc(a)){a.stopPropagation();a.preventDefault();return false;}return true;};$wnd.addEventListener('click',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('dblclick',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousedown',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mouseup',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousemove',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousewheel',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('keydown',$wnd.__dispatchCapturedEvent,true);$wnd.addEventListener('keyup',$wnd.__dispatchCapturedEvent,true);$wnd.addEventListener('keypress',$wnd.__dispatchCapturedEvent,true);$wnd.__dispatchEvent=function(b){var c,a=this;while(a&& !(c=a.__listener))a=a.parentNode;if(a&&a.nodeType!=1)a=null;if(c)ac(b,a,c);};$wnd.__captureElem=null;}
function Cd(c,b,a){b.__eventBits=a;b.onclick=a&1?$wnd.__dispatchEvent:null;b.ondblclick=a&2?$wnd.__dispatchEvent:null;b.onmousedown=a&4?$wnd.__dispatchEvent:null;b.onmouseup=a&8?$wnd.__dispatchEvent:null;b.onmouseover=a&16?$wnd.__dispatchEvent:null;b.onmouseout=a&32?$wnd.__dispatchEvent:null;b.onmousemove=a&64?$wnd.__dispatchEvent:null;b.onkeydown=a&128?$wnd.__dispatchEvent:null;b.onkeypress=a&256?$wnd.__dispatchEvent:null;b.onkeyup=a&512?$wnd.__dispatchEvent:null;b.onchange=a&1024?$wnd.__dispatchEvent:null;b.onfocus=a&2048?$wnd.__dispatchEvent:null;b.onblur=a&4096?$wnd.__dispatchEvent:null;b.onlosecapture=a&8192?$wnd.__dispatchEvent:null;b.onscroll=a&16384?$wnd.__dispatchEvent:null;b.onload=a&32768?$wnd.__dispatchEvent:null;b.onerror=a&65536?$wnd.__dispatchEvent:null;b.onmousewheel=a&131072?$wnd.__dispatchEvent:null;}
function xd(){}
_=xd.prototype=new pd();_.tI=0;function sd(c,a,b){if(!a&& !b){return true;}else if(!a|| !b){return false;}return a.isSameNode(b);}
function ud(a){Bd(a);td(a);}
function td(d){$wnd.addEventListener('mouseout',function(b){var a=$wnd.__captureElem;if(a&& !b.relatedTarget){if('html'==b.target.tagName.toLowerCase()){var c=$doc.createEvent('MouseEvents');c.initMouseEvent('mouseup',true,true,$wnd,0,b.screenX,b.screenY,b.clientX,b.clientY,b.ctrlKey,b.altKey,b.shiftKey,b.metaKey,b.button,null);a.dispatchEvent(c);}}},true);$wnd.addEventListener('DOMMouseScroll',$wnd.__dispatchCapturedMouseEvent,true);}
function wd(c,b,a){Cd(c,b,a);vd(c,b,a);}
function vd(c,b,a){if(a&131072){b.addEventListener('DOMMouseScroll',$wnd.__dispatchEvent,false);}}
function qd(){}
_=qd.prototype=new xd();_.tI=0;function kg(d,b,a){var c=b.parentNode;if(!c){return;}c.insertBefore(a,b);c.removeChild(b);}
function lg(b,a){if(b.d!==null){kg(b,b.d,a);}b.d=a;}
function mg(b,a){og(b.d,a);}
function ng(b,a){pc(b.d,a|ec(b.d));}
function og(a,b){lc(a,'className',b);}
function ig(){}
_=ig.prototype=new ei();_.tI=0;_.d=null;function bh(a){if(a.b){throw Dh(new Ch(),"Should only call onAttach when the widget is detached from the browser's document");}a.b=true;mc(a.d,a);a.i();a.v();}
function ch(a){if(!a.b){throw Dh(new Ch(),"Should only call onDetach when the widget is attached to the browser's document");}try{a.w();}finally{a.j();mc(a.d,null);a.b=false;}}
function dh(a){if(a.c!==null){me(a.c,a);}else if(a.c!==null){throw Dh(new Ch(),"This widget's parent does not implement HasWidgets");}}
function eh(b,a){if(b.b){mc(b.d,null);}lg(b,a);if(b.b){mc(a,b);}}
function fh(c,b){var a;a=c.c;if(b===null){if(a!==null&&a.b){ch(c);}c.c=null;}else{if(a!==null){throw Dh(new Ch(),'Cannot set a new parent without first clearing the old parent');}c.c=b;if(b.b){bh(c);}}}
function gh(){}
function hh(){}
function ih(a){}
function jh(){}
function kh(){}
function lh(a){eh(this,a);}
function pg(){}
_=pg.prototype=new ig();_.i=gh;_.j=hh;_.t=ih;_.v=jh;_.w=kh;_.A=lh;_.tI=12;_.b=false;_.c=null;function sf(b,a){fh(a,b);}
function uf(b,a){fh(a,null);}
function vf(){var a,b;for(b=this.q();ug(b);){a=vg(b);bh(a);}}
function wf(){var a,b;for(b=this.q();ug(b);){a=vg(b);ch(a);}}
function xf(){}
function yf(){}
function rf(){}
_=rf.prototype=new pg();_.i=vf;_.j=wf;_.v=xf;_.w=yf;_.tI=13;function Ee(a){a.a=yg(new qg(),a);}
function Fe(a){Ee(a);return a;}
function af(c,a,b){dh(a);zg(c.a,a);Ab(b,a.d);sf(c,a);}
function cf(b,c){var a;if(c.c!==b){return false;}uf(b,c);a=c.d;ic(fc(a),a);Fg(b.a,c);return true;}
function df(){return Dg(this.a);}
function De(){}
_=De.prototype=new rf();_.q=df;_.tI=14;function je(a){Fe(a);a.A(Db());oc(a.d,'position','relative');oc(a.d,'overflow','hidden');return a;}
function ke(a,b){af(a,b,a.d);}
function me(b,c){var a;a=cf(b,c);if(a){ne(c.d);}return a;}
function ne(a){oc(a,'left','');oc(a,'top','');oc(a,'position','');}
function ie(){}
_=ie.prototype=new De();_.tI=15;function hf(){hf=nn;oh(),qh;}
function ff(b,a){oh(),qh;jf(b,a);return b;}
function gf(b,a){if(b.a===null){b.a=ze(new ye());}al(b.a,a);}
function jf(b,a){eh(b,a);ng(b,7041);}
function kf(a){switch(cc(a)){case 1:if(this.a!==null){Be(this.a,this);}break;case 4096:case 2048:break;case 128:case 512:case 256:break;}}
function lf(a){jf(this,a);}
function ef(){}
_=ef.prototype=new pg();_.t=kf;_.A=lf;_.tI=16;_.a=null;function re(){re=nn;oh(),qh;}
function qe(b,a){oh(),qh;ff(b,a);return b;}
function se(b,a){nc(b.d,a);}
function pe(){}
_=pe.prototype=new ef();_.tI=17;function we(){we=nn;oh(),qh;}
function te(a){oh(),qh;qe(a,Cb());xe(a.d);mg(a,'gwt-Button');return a;}
function ue(b,a){oh(),qh;te(b);se(b,a);return b;}
function ve(c,a,b){oh(),qh;ue(c,a);gf(c,b);return c;}
function xe(b){we();if(b.type=='submit'){try{b.setAttribute('type','button');}catch(a){}}}
function oe(){}
_=oe.prototype=new pe();_.tI=18;function Ci(d,a,b){var c;while(a.p()){c=a.s();if(b===null?c===null:b.eQ(c)){return a;}}return null;}
function Ei(a){throw zi(new yi(),'add');}
function Fi(b){var a;a=Ci(this,this.q(),b);return a!==null;}
function Bi(){}
_=Bi.prototype=new ei();_.f=Ei;_.h=Fi;_.tI=0;function kj(b,a){throw ai(new Fh(),'Index: '+a+', Size: '+b.b);}
function lj(a){return cj(new bj(),a);}
function mj(b,a){throw zi(new yi(),'add');}
function nj(a){this.e(this.B(),a);return true;}
function oj(e){var a,b,c,d,f;if(e===this){return true;}if(!lb(e,12)){return false;}f=kb(e,12);if(this.B()!=f.B()){return false;}c=lj(this);d=f.q();while(ej(c)){a=fj(c);b=fj(d);if(!(a===null?b===null:a.eQ(b))){return false;}}return true;}
function pj(){var a,b,c,d;c=1;a=31;b=lj(this);while(ej(b)){d=fj(b);c=31*c+(d===null?0:d.hC());}return c;}
function qj(){return lj(this);}
function rj(a){throw zi(new yi(),'remove');}
function aj(){}
_=aj.prototype=new Bi();_.e=mj;_.f=nj;_.eQ=oj;_.hC=pj;_.q=qj;_.z=rj;_.tI=19;function Ek(a){{bl(a);}}
function Fk(a){Ek(a);return a;}
function al(b,a){ql(b.a,b.b++,a);return true;}
function bl(a){a.a=z();a.b=0;}
function dl(b,a){if(a<0||a>=b.b){kj(b,a);}return ml(b.a,a);}
function el(b,a){return fl(b,a,0);}
function fl(c,b,a){if(a<0){kj(c,a);}for(;a<c.b;++a){if(ll(b,ml(c.a,a))){return a;}}return (-1);}
function gl(c,a){var b;b=dl(c,a);ol(c.a,a,1);--c.b;return b;}
function il(a,b){if(a<0||a>this.b){kj(this,a);}hl(this.a,a,b);++this.b;}
function jl(a){return al(this,a);}
function hl(a,b,c){a.splice(b,0,c);}
function kl(a){return el(this,a)!=(-1);}
function ll(a,b){return a===b||a!==null&&a.eQ(b);}
function nl(a){return dl(this,a);}
function ml(a,b){return a[b];}
function pl(a){return gl(this,a);}
function ol(a,c,b){a.splice(c,b);}
function ql(a,b,c){a[b]=c;}
function rl(){return this.b;}
function Dk(){}
_=Dk.prototype=new aj();_.e=il;_.f=jl;_.h=kl;_.n=nl;_.z=pl;_.B=rl;_.tI=20;_.a=null;_.b=0;function ze(a){Fk(a);return a;}
function Be(d,c){var a,b;for(a=lj(d);ej(a);){b=kb(fj(a),6);b.u(c);}}
function ye(){}
_=ye.prototype=new Dk();_.tI=21;function Ff(){Ff=nn;eg=nm(new ul());}
function Ef(b,a){Ff();je(b);if(a===null){a=ag();}b.A(a);bh(b);return b;}
function bg(){Ff();return cg(null);}
function cg(c){Ff();var a,b;b=kb(tm(eg,c),7);if(b!==null){return b;}a=null;if(eg.c==0){dg();}um(eg,c,b=Ef(new zf(),a));return b;}
function ag(){Ff();return $doc.body;}
function dg(){Ff();ed(new Af());}
function zf(){}
_=zf.prototype=new ie();_.tI=22;var eg;function Cf(){var a,b;for(b=ek(sk((Ff(),eg)));lk(b);){a=kb(mk(b),7);if(a.b){ch(a);}}}
function Df(){return null;}
function Af(){}
_=Af.prototype=new ei();_.x=Cf;_.y=Df;_.tI=23;function yg(b,a){b.a=fb('[Lcom.google.gwt.user.client.ui.Widget;',[0],[9],[4],null);return b;}
function zg(a,b){Cg(a,b,a.b);}
function Bg(b,c){var a;for(a=0;a<b.b;++a){if(b.a[a]===c){return a;}}return (-1);}
function Cg(d,e,a){var b,c;if(a<0||a>d.b){throw new Fh();}if(d.b==d.a.a){c=fb('[Lcom.google.gwt.user.client.ui.Widget;',[0],[9],[d.a.a*2],null);for(b=0;b<d.a.a;++b){gb(c,b,d.a[b]);}d.a=c;}++d.b;for(b=d.b-1;b>a;--b){gb(d.a,b,d.a[b-1]);}gb(d.a,a,e);}
function Dg(a){return sg(new rg(),a);}
function Eg(c,b){var a;if(b<0||b>=c.b){throw new Fh();}--c.b;for(a=b;a<c.b;++a){gb(c.a,a,c.a[a+1]);}gb(c.a,c.b,null);}
function Fg(b,c){var a;a=Bg(b,c);if(a==(-1)){throw new jn();}Eg(b,a);}
function qg(){}
_=qg.prototype=new ei();_.tI=0;_.a=null;_.b=0;function sg(b,a){b.b=a;return b;}
function ug(a){return a.a<a.b.b-1;}
function vg(a){if(a.a>=a.b.b){throw new jn();}return a.b.a[++a.a];}
function wg(){return ug(this);}
function xg(){return vg(this);}
function rg(){}
_=rg.prototype=new ei();_.p=wg;_.s=xg;_.tI=0;_.a=(-1);function oh(){oh=nn;ph=nh(new mh());qh=ph;}
function nh(a){oh();return a;}
function mh(){}
_=mh.prototype=new ei();_.tI=0;var ph,qh;function wi(b,a){a;return b;}
function vi(){}
_=vi.prototype=new ei();_.tI=3;function Ah(b,a){wi(b,a);return b;}
function zh(){}
_=zh.prototype=new vi();_.tI=4;function ji(b,a){Ah(b,a);return b;}
function ii(){}
_=ii.prototype=new zh();_.tI=5;function sh(){}
_=sh.prototype=new ii();_.tI=24;function vh(){}
_=vh.prototype=new ii();_.tI=25;function Dh(b,a){ji(b,a);return b;}
function Ch(){}
_=Ch.prototype=new ii();_.tI=26;function ai(b,a){ji(b,a);return b;}
function Fh(){}
_=Fh.prototype=new ii();_.tI=27;function ci(){}
_=ci.prototype=new ii();_.tI=28;function ni(b,a){return b.substr(a,b.length-a);}
function oi(a,b){return String(a)==b;}
function pi(a){if(!lb(a,1))return false;return oi(this,a);}
function ri(){var a=qi;if(!a){a=qi={};}var e=':'+this;var b=a[e];if(b==null){b=0;var f=this.length;var d=f<64?1:f/32|0;for(var c=0;c<f;c+=d){b<<=1;b+=this.charCodeAt(c);}b|=0;a[e]=b;}return b;}
_=String.prototype;_.eQ=pi;_.hC=ri;_.tI=2;var qi=null;function ui(a){return s(a);}
function zi(b,a){ji(b,a);return b;}
function yi(){}
_=yi.prototype=new ii();_.tI=29;function cj(b,a){b.c=a;return b;}
function ej(a){return a.a<a.c.B();}
function fj(a){if(!ej(a)){throw new jn();}return a.c.n(a.b=a.a++);}
function gj(a){if(a.b<0){throw new Ch();}a.c.z(a.b);a.a=a.b;a.b=(-1);}
function hj(){return ej(this);}
function ij(){return fj(this);}
function bj(){}
_=bj.prototype=new ei();_.p=hj;_.s=ij;_.tI=0;_.a=0;_.b=(-1);function qk(f,d,e){var a,b,c;for(b=im(f.k());bm(b);){a=cm(b);c=a.l();if(d===null?c===null:d.eQ(c)){if(e){dm(b);}return a;}}return null;}
function rk(b){var a;a=b.k();return uj(new tj(),b,a);}
function sk(b){var a;a=sm(b);return ck(new bk(),b,a);}
function tk(a){return qk(this,a,false)!==null;}
function uk(d){var a,b,c,e,f,g,h;if(d===this){return true;}if(!lb(d,13)){return false;}f=kb(d,13);c=rk(this);e=f.r();if(!Ak(c,e)){return false;}for(a=wj(c);Dj(a);){b=Ej(a);h=this.o(b);g=f.o(b);if(h===null?g!==null:!h.eQ(g)){return false;}}return true;}
function vk(b){var a;a=qk(this,b,false);return a===null?null:a.m();}
function wk(){var a,b,c;b=0;for(c=im(this.k());bm(c);){a=cm(c);b+=a.hC();}return b;}
function xk(){return rk(this);}
function sj(){}
_=sj.prototype=new ei();_.g=tk;_.eQ=uk;_.o=vk;_.hC=wk;_.r=xk;_.tI=30;function Ak(e,b){var a,c,d;if(b===e){return true;}if(!lb(b,14)){return false;}c=kb(b,14);if(c.B()!=e.B()){return false;}for(a=c.q();a.p();){d=a.s();if(!e.h(d)){return false;}}return true;}
function Bk(a){return Ak(this,a);}
function Ck(){var a,b,c;a=0;for(b=this.q();b.p();){c=b.s();if(c!==null){a+=c.hC();}}return a;}
function yk(){}
_=yk.prototype=new Bi();_.eQ=Bk;_.hC=Ck;_.tI=31;function uj(b,a,c){b.a=a;b.b=c;return b;}
function wj(b){var a;a=im(b.b);return Bj(new Aj(),b,a);}
function xj(a){return this.a.g(a);}
function yj(){return wj(this);}
function zj(){return this.b.a.c;}
function tj(){}
_=tj.prototype=new yk();_.h=xj;_.q=yj;_.B=zj;_.tI=32;function Bj(b,a,c){b.a=c;return b;}
function Dj(a){return a.a.p();}
function Ej(b){var a;a=b.a.s();return a.l();}
function Fj(){return Dj(this);}
function ak(){return Ej(this);}
function Aj(){}
_=Aj.prototype=new ei();_.p=Fj;_.s=ak;_.tI=0;function ck(b,a,c){b.a=a;b.b=c;return b;}
function ek(b){var a;a=im(b.b);return jk(new ik(),b,a);}
function fk(a){return rm(this.a,a);}
function gk(){return ek(this);}
function hk(){return this.b.a.c;}
function bk(){}
_=bk.prototype=new Bi();_.h=fk;_.q=gk;_.B=hk;_.tI=0;function jk(b,a,c){b.a=c;return b;}
function lk(a){return a.a.p();}
function mk(a){var b;b=a.a.s().m();return b;}
function nk(){return lk(this);}
function ok(){return mk(this);}
function ik(){}
_=ik.prototype=new ei();_.p=nk;_.s=ok;_.tI=0;function pm(){pm=nn;wm=Cm();}
function mm(a){{om(a);}}
function nm(a){pm();mm(a);return a;}
function om(a){a.a=z();a.d=A();a.b=pb(wm,v);a.c=0;}
function qm(b,a){if(lb(a,1)){return an(b.d,kb(a,1))!==wm;}else if(a===null){return b.b!==wm;}else{return Fm(b.a,a,a.hC())!==wm;}}
function rm(a,b){if(a.b!==wm&&Em(a.b,b)){return true;}else if(Bm(a.d,b)){return true;}else if(zm(a.a,b)){return true;}return false;}
function sm(a){return gm(new Dl(),a);}
function tm(c,a){var b;if(lb(a,1)){b=an(c.d,kb(a,1));}else if(a===null){b=c.b;}else{b=Fm(c.a,a,a.hC());}return b===wm?null:b;}
function um(c,a,d){var b;{b=c.b;c.b=d;}if(b===wm){++c.c;return null;}else{return b;}}
function vm(c,a){var b;if(lb(a,1)){b=dn(c.d,kb(a,1));}else if(a===null){b=c.b;c.b=pb(wm,v);}else{b=cn(c.a,a,a.hC());}if(b===wm){return null;}else{--c.c;return b;}}
function xm(e,c){pm();for(var d in e){if(d==parseInt(d)){var a=e[d];for(var f=0,b=a.length;f<b;++f){c.f(a[f]);}}}}
function ym(d,a){pm();for(var c in d){if(c.charCodeAt(0)==58){var e=d[c];var b=yl(c.substring(1),e);a.f(b);}}}
function zm(f,h){pm();for(var e in f){if(e==parseInt(e)){var a=f[e];for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.m();if(Em(h,d)){return true;}}}}return false;}
function Am(a){return qm(this,a);}
function Bm(c,d){pm();for(var b in c){if(b.charCodeAt(0)==58){var a=c[b];if(Em(d,a)){return true;}}}return false;}
function Cm(){pm();}
function Dm(){return sm(this);}
function Em(a,b){pm();if(a===b){return true;}else if(a===null){return false;}else{return a.eQ(b);}}
function bn(a){return tm(this,a);}
function Fm(f,h,e){pm();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.l();if(Em(h,d)){return c.m();}}}}
function an(b,a){pm();return b[':'+a];}
function cn(f,h,e){pm();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.l();if(Em(h,d)){if(a.length==1){delete f[e];}else{a.splice(g,1);}return c.m();}}}}
function dn(c,a){pm();a=':'+a;var b=c[a];delete c[a];return b;}
function ul(){}
_=ul.prototype=new sj();_.g=Am;_.k=Dm;_.o=bn;_.tI=33;_.a=null;_.b=null;_.c=0;_.d=null;var wm;function wl(b,a,c){b.a=a;b.b=c;return b;}
function yl(a,b){return wl(new vl(),a,b);}
function zl(b){var a;if(lb(b,15)){a=kb(b,15);if(Em(this.a,a.l())&&Em(this.b,a.m())){return true;}}return false;}
function Al(){return this.a;}
function Bl(){return this.b;}
function Cl(){var a,b;a=0;b=0;if(this.a!==null){a=this.a.hC();}if(this.b!==null){b=this.b.hC();}return a^b;}
function vl(){}
_=vl.prototype=new ei();_.eQ=zl;_.l=Al;_.m=Bl;_.hC=Cl;_.tI=34;_.a=null;_.b=null;function gm(b,a){b.a=a;return b;}
function im(a){return Fl(new El(),a.a);}
function jm(c){var a,b,d;if(lb(c,15)){a=kb(c,15);b=a.l();if(qm(this.a,b)){d=tm(this.a,b);return Em(a.m(),d);}}return false;}
function km(){return im(this);}
function lm(){return this.a.c;}
function Dl(){}
_=Dl.prototype=new yk();_.h=jm;_.q=km;_.B=lm;_.tI=35;function Fl(c,b){var a;c.c=b;a=Fk(new Dk());if(c.c.b!==(pm(),wm)){al(a,wl(new vl(),null,c.c.b));}ym(c.c.d,a);xm(c.c.a,a);c.a=lj(a);return c;}
function bm(a){return ej(a.a);}
function cm(a){return a.b=kb(fj(a.a),15);}
function dm(a){if(a.b===null){throw Dh(new Ch(),'Must call next() before remove().');}else{gj(a.a);vm(a.c,a.b.l());a.b=null;}}
function em(){return bm(this);}
function fm(){return cm(this);}
function El(){}
_=El.prototype=new ei();_.p=em;_.s=fm;_.tI=0;_.a=null;_.b=null;function jn(){}
_=jn.prototype=new ii();_.tI=36;function rh(){xb(new sb());}
function gwtOnLoad(b,d,c){$moduleName=d;$moduleBase=c;if(b)try{rh();}catch(a){b(d);}else{rh();}}
var ob=[{},{},{1:1},{3:1},{3:1},{3:1},{3:1},{2:1},{6:1},{2:1,4:1},{2:1},{5:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{12:1},{12:1},{12:1},{7:1,8:1,9:1,10:1,11:1},{5:1},{3:1},{3:1},{3:1},{3:1},{3:1},{3:1},{13:1},{14:1},{14:1},{13:1},{15:1},{14:1},{3:1}];if (com_google_gwt_sample_hello_Hello) {  var __gwt_initHandlers = com_google_gwt_sample_hello_Hello.__gwt_initHandlers;  com_google_gwt_sample_hello_Hello.onScriptLoad(gwtOnLoad);}})();