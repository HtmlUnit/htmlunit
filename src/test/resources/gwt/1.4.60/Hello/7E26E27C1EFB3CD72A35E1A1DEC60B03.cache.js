(function(){var $wnd = window;var $doc = $wnd.document;var $moduleName, $moduleBase;var _,xn='com.google.gwt.core.client.',yn='com.google.gwt.lang.',zn='com.google.gwt.sample.hello.client.',An='com.google.gwt.user.client.',Bn='com.google.gwt.user.client.impl.',Cn='com.google.gwt.user.client.ui.',Dn='com.google.gwt.user.client.ui.impl.',En='java.lang.',Fn='java.util.';function wn(){}
function pi(a){return this===a;}
function qi(){return Di(this);}
function ni(){}
_=ni.prototype={};_.eQ=pi;_.hC=qi;_.tI=1;var o=null;function r(a){return a==null?0:a.$H?a.$H:(a.$H=t());}
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
_=v.prototype=new ni();_.eQ=C;_.hC=D;_.tI=7;function F(c,a,d,b,e){c.a=a;c.b=b;e;c.tI=d;return c;}
function bb(a,b,c){return a[b]=c;}
function cb(b,a){return b[a];}
function db(a){return a.length;}
function fb(e,d,c,b,a){return eb(e,d,c,b,0,db(b),a);}
function eb(j,i,g,c,e,a,b){var d,f,h;if((f=cb(c,e))<0){throw new li();}h=F(new E(),f,cb(i,e),cb(g,e),j);++e;if(e<a){j=wi(j,1);for(d=0;d<f;++d){bb(h,d,eb(j,i,g,c,e,a,b));}}else{for(d=0;d<f;++d){bb(h,d,b);}}return h;}
function gb(a,b,c){if(c!==null&&a.b!=0&& !lb(c,a.b)){throw new Bh();}return bb(a,b,c);}
function E(){}
_=E.prototype=new ni();_.tI=0;function jb(b,a){return !(!(b&&ob[b][a]));}
function kb(b,a){if(b!=null)jb(b.tI,a)||nb();return b;}
function lb(b,a){return b!=null&&jb(b.tI,a);}
function nb(){throw new Eh();}
function mb(a){if(a!==null){throw new Eh();}return a;}
function pb(b,d){_=d.prototype;if(b&& !(b.tI>=_.tI)){var c=b.toString;for(var a in _){b[a]=_[a];}b.toString=c;}return b;}
var ob;function xb(b){var a;a=xe(new qe(),'Click me',new tb());me(dg(),a);}
function sb(){}
_=sb.prototype=new ni();_.tI=0;function vb(a){fd('Hello, AJAX');}
function tb(){}
_=tb.prototype=new ni();_.u=vb;_.tI=8;function zb(){zb=wn;kc=il(new gl());{gc=new rd();wd(gc);}}
function Ab(b,a){zb();Fd(gc,b,a);}
function Bb(a,b){zb();return ud(gc,a,b);}
function Cb(){zb();return be(gc,'button');}
function Db(){zb();return be(gc,'div');}
function ac(b,a,d){zb();var c;c=o;{Fb(b,a,d);}}
function Fb(b,a,c){zb();var d;if(a===jc){if(cc(b)==8192){jc=null;}}d=Eb;Eb=b;try{c.t(b);}finally{Eb=d;}}
function bc(b,a){zb();ce(gc,b,a);}
function cc(a){zb();return de(gc,a);}
function dc(a){zb();Bd(gc,a);}
function ec(a){zb();return ee(gc,a);}
function fc(a){zb();return Cd(gc,a);}
function hc(a){zb();var b,c;c=true;if(kc.b>0){b=mb(ml(kc,kc.b-1));if(!(c=null.D())){bc(a,true);dc(a);}}return c;}
function ic(b,a){zb();fe(gc,b,a);}
function lc(a,b,c){zb();ge(gc,a,b,c);}
function mc(a,b){zb();he(gc,a,b);}
function nc(a,b){zb();ie(gc,a,b);}
function oc(b,a,c){zb();je(gc,b,a,c);}
function pc(a,b){zb();yd(gc,a,b);}
var Eb=null,gc=null,jc=null,kc;function sc(a){if(lb(a,4)){return Bb(this,kb(a,4));}return x(pb(this,qc),a);}
function tc(){return y(pb(this,qc));}
function qc(){}
_=qc.prototype=new v();_.eQ=sc;_.hC=tc;_.tI=9;function xc(a){return x(pb(this,uc),a);}
function yc(){return y(pb(this,uc));}
function uc(){}
_=uc.prototype=new v();_.eQ=xc;_.hC=yc;_.tI=10;function Ec(){Ec=wn;ad=il(new gl());{Fc();}}
function Fc(){Ec();ed(new Ac());}
var ad;function Cc(){while((Ec(),ad).b>0){mb(ml((Ec(),ad),0)).D();}}
function Dc(){return null;}
function Ac(){}
_=Ac.prototype=new ni();_.x=Cc;_.y=Dc;_.tI=11;function dd(){dd=wn;gd=il(new gl());od=il(new gl());{kd();}}
function ed(a){dd();jl(gd,a);}
function fd(a){dd();$wnd.alert(a);}
function hd(){dd();var a,b;for(a=uj(gd);nj(a);){b=kb(oj(a),5);b.x();}}
function id(){dd();var a,b,c,d;d=null;for(a=uj(gd);nj(a);){b=kb(oj(a),5);c=b.y();{d=c;}}return d;}
function jd(){dd();var a,b;for(a=uj(od);nj(a);){b=mb(oj(a));null.D();}}
function kd(){dd();__gwt_initHandlers(function(){nd();},function(){return md();},function(){ld();$wnd.onresize=null;$wnd.onbeforeclose=null;$wnd.onclose=null;});}
function ld(){dd();var a;a=o;{hd();}}
function md(){dd();var a;a=o;{return id();}}
function nd(){dd();var a;a=o;{jd();}}
var gd,od;function Fd(c,b,a){b.appendChild(a);}
function be(b,a){return $doc.createElement(a);}
function ce(c,b,a){b.cancelBubble=a;}
function de(b,a){switch(a.type){case 'blur':return 4096;case 'change':return 1024;case 'click':return 1;case 'dblclick':return 2;case 'focus':return 2048;case 'keydown':return 128;case 'keypress':return 256;case 'keyup':return 512;case 'load':return 32768;case 'losecapture':return 8192;case 'mousedown':return 4;case 'mousemove':return 64;case 'mouseout':return 32;case 'mouseover':return 16;case 'mouseup':return 8;case 'scroll':return 16384;case 'error':return 65536;case 'mousewheel':return 131072;case 'DOMMouseScroll':return 131072;}}
function ee(b,a){return a.__eventBits||0;}
function fe(c,b,a){b.removeChild(a);}
function ge(c,a,b,d){a[b]=d;}
function he(c,a,b){a.__listener=b;}
function ie(c,a,b){if(!b){b='';}a.innerHTML=b;}
function je(c,b,a,d){b.style[a]=d;}
function pd(){}
_=pd.prototype=new ni();_.tI=0;function Bd(b,a){a.preventDefault();}
function Cd(c,a){var b=a.parentNode;if(b==null){return null;}if(b.nodeType!=1)b=null;return b||null;}
function Dd(d){$wnd.__dispatchCapturedMouseEvent=function(b){if($wnd.__dispatchCapturedEvent(b)){var a=$wnd.__captureElem;if(a&&a.__listener){ac(b,a,a.__listener);b.stopPropagation();}}};$wnd.__dispatchCapturedEvent=function(a){if(!hc(a)){a.stopPropagation();a.preventDefault();return false;}return true;};$wnd.addEventListener('click',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('dblclick',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousedown',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mouseup',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousemove',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousewheel',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('keydown',$wnd.__dispatchCapturedEvent,true);$wnd.addEventListener('keyup',$wnd.__dispatchCapturedEvent,true);$wnd.addEventListener('keypress',$wnd.__dispatchCapturedEvent,true);$wnd.__dispatchEvent=function(b){var c,a=this;while(a&& !(c=a.__listener))a=a.parentNode;if(a&&a.nodeType!=1)a=null;if(c)ac(b,a,c);};$wnd.__captureElem=null;}
function Ed(c,b,a){b.__eventBits=a;b.onclick=a&1?$wnd.__dispatchEvent:null;b.ondblclick=a&2?$wnd.__dispatchEvent:null;b.onmousedown=a&4?$wnd.__dispatchEvent:null;b.onmouseup=a&8?$wnd.__dispatchEvent:null;b.onmouseover=a&16?$wnd.__dispatchEvent:null;b.onmouseout=a&32?$wnd.__dispatchEvent:null;b.onmousemove=a&64?$wnd.__dispatchEvent:null;b.onkeydown=a&128?$wnd.__dispatchEvent:null;b.onkeypress=a&256?$wnd.__dispatchEvent:null;b.onkeyup=a&512?$wnd.__dispatchEvent:null;b.onchange=a&1024?$wnd.__dispatchEvent:null;b.onfocus=a&2048?$wnd.__dispatchEvent:null;b.onblur=a&4096?$wnd.__dispatchEvent:null;b.onlosecapture=a&8192?$wnd.__dispatchEvent:null;b.onscroll=a&16384?$wnd.__dispatchEvent:null;b.onload=a&32768?$wnd.__dispatchEvent:null;b.onerror=a&65536?$wnd.__dispatchEvent:null;b.onmousewheel=a&131072?$wnd.__dispatchEvent:null;}
function zd(){}
_=zd.prototype=new pd();_.tI=0;function ud(c,a,b){if(!a&& !b){return true;}else if(!a|| !b){return false;}return a.isSameNode(b);}
function wd(a){Dd(a);vd(a);}
function vd(d){$wnd.addEventListener('mouseout',function(b){var a=$wnd.__captureElem;if(a&& !b.relatedTarget){if('html'==b.target.tagName.toLowerCase()){var c=$doc.createEvent('MouseEvents');c.initMouseEvent('mouseup',true,true,$wnd,0,b.screenX,b.screenY,b.clientX,b.clientY,b.ctrlKey,b.altKey,b.shiftKey,b.metaKey,b.button,null);a.dispatchEvent(c);}}},true);$wnd.addEventListener('DOMMouseScroll',$wnd.__dispatchCapturedMouseEvent,true);}
function yd(c,b,a){Ed(c,b,a);xd(c,b,a);}
function xd(c,b,a){if(a&131072){b.addEventListener('DOMMouseScroll',$wnd.__dispatchEvent,false);}}
function qd(){}
_=qd.prototype=new zd();_.tI=0;function rd(){}
_=rd.prototype=new qd();_.tI=0;function mg(d,b,a){var c=b.parentNode;if(!c){return;}c.insertBefore(a,b);c.removeChild(b);}
function ng(b,a){if(b.d!==null){mg(b,b.d,a);}b.d=a;}
function og(b,a){qg(b.d,a);}
function pg(b,a){pc(b.d,a|ec(b.d));}
function qg(a,b){lc(a,'className',b);}
function kg(){}
_=kg.prototype=new ni();_.tI=0;_.d=null;function dh(a){if(a.b){throw gi(new fi(),"Should only call onAttach when the widget is detached from the browser's document");}a.b=true;mc(a.d,a);a.i();a.v();}
function eh(a){if(!a.b){throw gi(new fi(),"Should only call onDetach when the widget is attached to the browser's document");}try{a.w();}finally{a.j();mc(a.d,null);a.b=false;}}
function fh(a){if(a.c!==null){oe(a.c,a);}else if(a.c!==null){throw gi(new fi(),"This widget's parent does not implement HasWidgets");}}
function gh(b,a){if(b.b){mc(b.d,null);}ng(b,a);if(b.b){mc(a,b);}}
function hh(c,b){var a;a=c.c;if(b===null){if(a!==null&&a.b){eh(c);}c.c=null;}else{if(a!==null){throw gi(new fi(),'Cannot set a new parent without first clearing the old parent');}c.c=b;if(b.b){dh(c);}}}
function ih(){}
function jh(){}
function kh(a){}
function lh(){}
function mh(){}
function nh(a){gh(this,a);}
function rg(){}
_=rg.prototype=new kg();_.i=ih;_.j=jh;_.t=kh;_.v=lh;_.w=mh;_.A=nh;_.tI=12;_.b=false;_.c=null;function uf(b,a){hh(a,b);}
function wf(b,a){hh(a,null);}
function xf(){var a,b;for(b=this.q();wg(b);){a=xg(b);dh(a);}}
function yf(){var a,b;for(b=this.q();wg(b);){a=xg(b);eh(a);}}
function zf(){}
function Af(){}
function tf(){}
_=tf.prototype=new rg();_.i=xf;_.j=yf;_.v=zf;_.w=Af;_.tI=13;function af(a){a.a=Ag(new sg(),a);}
function bf(a){af(a);return a;}
function cf(c,a,b){fh(a);Bg(c.a,a);Ab(b,a.d);uf(c,a);}
function ef(b,c){var a;if(c.c!==b){return false;}wf(b,c);a=c.d;ic(fc(a),a);bh(b.a,c);return true;}
function ff(){return Fg(this.a);}
function Fe(){}
_=Fe.prototype=new tf();_.q=ff;_.tI=14;function le(a){bf(a);a.A(Db());oc(a.d,'position','relative');oc(a.d,'overflow','hidden');return a;}
function me(a,b){cf(a,b,a.d);}
function oe(b,c){var a;a=ef(b,c);if(a){pe(c.d);}return a;}
function pe(a){oc(a,'left','');oc(a,'top','');oc(a,'position','');}
function ke(){}
_=ke.prototype=new Fe();_.tI=15;function kf(){kf=wn;xh(),zh;}
function hf(b,a){xh(),zh;lf(b,a);return b;}
function jf(b,a){if(b.a===null){b.a=Be(new Ae());}jl(b.a,a);}
function lf(b,a){gh(b,a);pg(b,7041);}
function mf(a){switch(cc(a)){case 1:if(this.a!==null){De(this.a,this);}break;case 4096:case 2048:break;case 128:case 512:case 256:break;}}
function nf(a){lf(this,a);}
function gf(){}
_=gf.prototype=new rg();_.t=mf;_.A=nf;_.tI=16;_.a=null;function te(){te=wn;xh(),zh;}
function se(b,a){xh(),zh;hf(b,a);return b;}
function ue(b,a){nc(b.d,a);}
function re(){}
_=re.prototype=new gf();_.tI=17;function ye(){ye=wn;xh(),zh;}
function ve(a){xh(),zh;se(a,Cb());ze(a.d);og(a,'gwt-Button');return a;}
function we(b,a){xh(),zh;ve(b);ue(b,a);return b;}
function xe(c,a,b){xh(),zh;we(c,a);jf(c,b);return c;}
function ze(b){ye();if(b.type=='submit'){try{b.setAttribute('type','button');}catch(a){}}}
function qe(){}
_=qe.prototype=new re();_.tI=18;function fj(d,a,b){var c;while(a.p()){c=a.s();if(b===null?c===null:b.eQ(c)){return a;}}return null;}
function hj(a){throw cj(new bj(),'add');}
function ij(b){var a;a=fj(this,this.q(),b);return a!==null;}
function ej(){}
_=ej.prototype=new ni();_.f=hj;_.h=ij;_.tI=0;function tj(b,a){throw ji(new ii(),'Index: '+a+', Size: '+b.b);}
function uj(a){return lj(new kj(),a);}
function vj(b,a){throw cj(new bj(),'add');}
function wj(a){this.e(this.B(),a);return true;}
function xj(e){var a,b,c,d,f;if(e===this){return true;}if(!lb(e,12)){return false;}f=kb(e,12);if(this.B()!=f.B()){return false;}c=uj(this);d=f.q();while(nj(c)){a=oj(c);b=oj(d);if(!(a===null?b===null:a.eQ(b))){return false;}}return true;}
function yj(){var a,b,c,d;c=1;a=31;b=uj(this);while(nj(b)){d=oj(b);c=31*c+(d===null?0:d.hC());}return c;}
function zj(){return uj(this);}
function Aj(a){throw cj(new bj(),'remove');}
function jj(){}
_=jj.prototype=new ej();_.e=vj;_.f=wj;_.eQ=xj;_.hC=yj;_.q=zj;_.z=Aj;_.tI=19;function hl(a){{kl(a);}}
function il(a){hl(a);return a;}
function jl(b,a){zl(b.a,b.b++,a);return true;}
function kl(a){a.a=z();a.b=0;}
function ml(b,a){if(a<0||a>=b.b){tj(b,a);}return vl(b.a,a);}
function nl(b,a){return ol(b,a,0);}
function ol(c,b,a){if(a<0){tj(c,a);}for(;a<c.b;++a){if(ul(b,vl(c.a,a))){return a;}}return (-1);}
function pl(c,a){var b;b=ml(c,a);xl(c.a,a,1);--c.b;return b;}
function rl(a,b){if(a<0||a>this.b){tj(this,a);}ql(this.a,a,b);++this.b;}
function sl(a){return jl(this,a);}
function ql(a,b,c){a.splice(b,0,c);}
function tl(a){return nl(this,a)!=(-1);}
function ul(a,b){return a===b||a!==null&&a.eQ(b);}
function wl(a){return ml(this,a);}
function vl(a,b){return a[b];}
function yl(a){return pl(this,a);}
function xl(a,c,b){a.splice(c,b);}
function zl(a,b,c){a[b]=c;}
function Al(){return this.b;}
function gl(){}
_=gl.prototype=new jj();_.e=rl;_.f=sl;_.h=tl;_.n=wl;_.z=yl;_.B=Al;_.tI=20;_.a=null;_.b=0;function Be(a){il(a);return a;}
function De(d,c){var a,b;for(a=uj(d);nj(a);){b=kb(oj(a),6);b.u(c);}}
function Ae(){}
_=Ae.prototype=new gl();_.tI=21;function bg(){bg=wn;gg=wm(new Dl());}
function ag(b,a){bg();le(b);if(a===null){a=cg();}b.A(a);dh(b);return b;}
function dg(){bg();return eg(null);}
function eg(c){bg();var a,b;b=kb(Cm(gg,c),7);if(b!==null){return b;}a=null;if(gg.c==0){fg();}Dm(gg,c,b=ag(new Bf(),a));return b;}
function cg(){bg();return $doc.body;}
function fg(){bg();ed(new Cf());}
function Bf(){}
_=Bf.prototype=new ke();_.tI=22;var gg;function Ef(){var a,b;for(b=nk(Bk((bg(),gg)));uk(b);){a=kb(vk(b),7);if(a.b){eh(a);}}}
function Ff(){return null;}
function Cf(){}
_=Cf.prototype=new ni();_.x=Ef;_.y=Ff;_.tI=23;function Ag(b,a){b.a=fb('[Lcom.google.gwt.user.client.ui.Widget;',[0],[9],[4],null);return b;}
function Bg(a,b){Eg(a,b,a.b);}
function Dg(b,c){var a;for(a=0;a<b.b;++a){if(b.a[a]===c){return a;}}return (-1);}
function Eg(d,e,a){var b,c;if(a<0||a>d.b){throw new ii();}if(d.b==d.a.a){c=fb('[Lcom.google.gwt.user.client.ui.Widget;',[0],[9],[d.a.a*2],null);for(b=0;b<d.a.a;++b){gb(c,b,d.a[b]);}d.a=c;}++d.b;for(b=d.b-1;b>a;--b){gb(d.a,b,d.a[b-1]);}gb(d.a,a,e);}
function Fg(a){return ug(new tg(),a);}
function ah(c,b){var a;if(b<0||b>=c.b){throw new ii();}--c.b;for(a=b;a<c.b;++a){gb(c.a,a,c.a[a+1]);}gb(c.a,c.b,null);}
function bh(b,c){var a;a=Dg(b,c);if(a==(-1)){throw new sn();}ah(b,a);}
function sg(){}
_=sg.prototype=new ni();_.tI=0;_.a=null;_.b=0;function ug(b,a){b.b=a;return b;}
function wg(a){return a.a<a.b.b-1;}
function xg(a){if(a.a>=a.b.b){throw new sn();}return a.b.a[++a.a];}
function yg(){return wg(this);}
function zg(){return xg(this);}
function tg(){}
_=tg.prototype=new ni();_.p=yg;_.s=zg;_.tI=0;_.a=(-1);function xh(){xh=wn;yh=rh(new ph());zh=yh!==null?wh(new oh()):yh;}
function wh(a){xh();return a;}
function oh(){}
_=oh.prototype=new ni();_.tI=0;var yh,zh;function sh(){sh=wn;xh();}
function qh(a){th(a);uh(a);vh(a);}
function rh(a){sh();wh(a);qh(a);return a;}
function th(b){return function(a){if(this.parentNode.onblur){this.parentNode.onblur(a);}};}
function uh(b){return function(a){if(this.parentNode.onfocus){this.parentNode.onfocus(a);}};}
function vh(a){return function(){this.firstChild.focus();};}
function ph(){}
_=ph.prototype=new oh();_.tI=0;function Fi(b,a){a;return b;}
function Ei(){}
_=Ei.prototype=new ni();_.tI=3;function di(b,a){Fi(b,a);return b;}
function ci(){}
_=ci.prototype=new Ei();_.tI=4;function si(b,a){di(b,a);return b;}
function ri(){}
_=ri.prototype=new ci();_.tI=5;function Bh(){}
_=Bh.prototype=new ri();_.tI=24;function Eh(){}
_=Eh.prototype=new ri();_.tI=25;function gi(b,a){si(b,a);return b;}
function fi(){}
_=fi.prototype=new ri();_.tI=26;function ji(b,a){si(b,a);return b;}
function ii(){}
_=ii.prototype=new ri();_.tI=27;function li(){}
_=li.prototype=new ri();_.tI=28;function wi(b,a){return b.substr(a,b.length-a);}
function xi(a,b){return String(a)==b;}
function yi(a){if(!lb(a,1))return false;return xi(this,a);}
function Ai(){var a=zi;if(!a){a=zi={};}var e=':'+this;var b=a[e];if(b==null){b=0;var f=this.length;var d=f<64?1:f/32|0;for(var c=0;c<f;c+=d){b<<=1;b+=this.charCodeAt(c);}b|=0;a[e]=b;}return b;}
_=String.prototype;_.eQ=yi;_.hC=Ai;_.tI=2;var zi=null;function Di(a){return s(a);}
function cj(b,a){si(b,a);return b;}
function bj(){}
_=bj.prototype=new ri();_.tI=29;function lj(b,a){b.c=a;return b;}
function nj(a){return a.a<a.c.B();}
function oj(a){if(!nj(a)){throw new sn();}return a.c.n(a.b=a.a++);}
function pj(a){if(a.b<0){throw new fi();}a.c.z(a.b);a.a=a.b;a.b=(-1);}
function qj(){return nj(this);}
function rj(){return oj(this);}
function kj(){}
_=kj.prototype=new ni();_.p=qj;_.s=rj;_.tI=0;_.a=0;_.b=(-1);function zk(f,d,e){var a,b,c;for(b=rm(f.k());km(b);){a=lm(b);c=a.l();if(d===null?c===null:d.eQ(c)){if(e){mm(b);}return a;}}return null;}
function Ak(b){var a;a=b.k();return Dj(new Cj(),b,a);}
function Bk(b){var a;a=Bm(b);return lk(new kk(),b,a);}
function Ck(a){return zk(this,a,false)!==null;}
function Dk(d){var a,b,c,e,f,g,h;if(d===this){return true;}if(!lb(d,13)){return false;}f=kb(d,13);c=Ak(this);e=f.r();if(!dl(c,e)){return false;}for(a=Fj(c);gk(a);){b=hk(a);h=this.o(b);g=f.o(b);if(h===null?g!==null:!h.eQ(g)){return false;}}return true;}
function Ek(b){var a;a=zk(this,b,false);return a===null?null:a.m();}
function Fk(){var a,b,c;b=0;for(c=rm(this.k());km(c);){a=lm(c);b+=a.hC();}return b;}
function al(){return Ak(this);}
function Bj(){}
_=Bj.prototype=new ni();_.g=Ck;_.eQ=Dk;_.o=Ek;_.hC=Fk;_.r=al;_.tI=30;function dl(e,b){var a,c,d;if(b===e){return true;}if(!lb(b,14)){return false;}c=kb(b,14);if(c.B()!=e.B()){return false;}for(a=c.q();a.p();){d=a.s();if(!e.h(d)){return false;}}return true;}
function el(a){return dl(this,a);}
function fl(){var a,b,c;a=0;for(b=this.q();b.p();){c=b.s();if(c!==null){a+=c.hC();}}return a;}
function bl(){}
_=bl.prototype=new ej();_.eQ=el;_.hC=fl;_.tI=31;function Dj(b,a,c){b.a=a;b.b=c;return b;}
function Fj(b){var a;a=rm(b.b);return ek(new dk(),b,a);}
function ak(a){return this.a.g(a);}
function bk(){return Fj(this);}
function ck(){return this.b.a.c;}
function Cj(){}
_=Cj.prototype=new bl();_.h=ak;_.q=bk;_.B=ck;_.tI=32;function ek(b,a,c){b.a=c;return b;}
function gk(a){return a.a.p();}
function hk(b){var a;a=b.a.s();return a.l();}
function ik(){return gk(this);}
function jk(){return hk(this);}
function dk(){}
_=dk.prototype=new ni();_.p=ik;_.s=jk;_.tI=0;function lk(b,a,c){b.a=a;b.b=c;return b;}
function nk(b){var a;a=rm(b.b);return sk(new rk(),b,a);}
function ok(a){return Am(this.a,a);}
function pk(){return nk(this);}
function qk(){return this.b.a.c;}
function kk(){}
_=kk.prototype=new ej();_.h=ok;_.q=pk;_.B=qk;_.tI=0;function sk(b,a,c){b.a=c;return b;}
function uk(a){return a.a.p();}
function vk(a){var b;b=a.a.s().m();return b;}
function wk(){return uk(this);}
function xk(){return vk(this);}
function rk(){}
_=rk.prototype=new ni();_.p=wk;_.s=xk;_.tI=0;function ym(){ym=wn;Fm=fn();}
function vm(a){{xm(a);}}
function wm(a){ym();vm(a);return a;}
function xm(a){a.a=z();a.d=A();a.b=pb(Fm,v);a.c=0;}
function zm(b,a){if(lb(a,1)){return kn(b.d,kb(a,1))!==Fm;}else if(a===null){return b.b!==Fm;}else{return jn(b.a,a,a.hC())!==Fm;}}
function Am(a,b){if(a.b!==Fm&&hn(a.b,b)){return true;}else if(en(a.d,b)){return true;}else if(cn(a.a,b)){return true;}return false;}
function Bm(a){return pm(new gm(),a);}
function Cm(c,a){var b;if(lb(a,1)){b=kn(c.d,kb(a,1));}else if(a===null){b=c.b;}else{b=jn(c.a,a,a.hC());}return b===Fm?null:b;}
function Dm(c,a,d){var b;{b=c.b;c.b=d;}if(b===Fm){++c.c;return null;}else{return b;}}
function Em(c,a){var b;if(lb(a,1)){b=nn(c.d,kb(a,1));}else if(a===null){b=c.b;c.b=pb(Fm,v);}else{b=mn(c.a,a,a.hC());}if(b===Fm){return null;}else{--c.c;return b;}}
function an(e,c){ym();for(var d in e){if(d==parseInt(d)){var a=e[d];for(var f=0,b=a.length;f<b;++f){c.f(a[f]);}}}}
function bn(d,a){ym();for(var c in d){if(c.charCodeAt(0)==58){var e=d[c];var b=bm(c.substring(1),e);a.f(b);}}}
function cn(f,h){ym();for(var e in f){if(e==parseInt(e)){var a=f[e];for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.m();if(hn(h,d)){return true;}}}}return false;}
function dn(a){return zm(this,a);}
function en(c,d){ym();for(var b in c){if(b.charCodeAt(0)==58){var a=c[b];if(hn(d,a)){return true;}}}return false;}
function fn(){ym();}
function gn(){return Bm(this);}
function hn(a,b){ym();if(a===b){return true;}else if(a===null){return false;}else{return a.eQ(b);}}
function ln(a){return Cm(this,a);}
function jn(f,h,e){ym();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.l();if(hn(h,d)){return c.m();}}}}
function kn(b,a){ym();return b[':'+a];}
function mn(f,h,e){ym();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.l();if(hn(h,d)){if(a.length==1){delete f[e];}else{a.splice(g,1);}return c.m();}}}}
function nn(c,a){ym();a=':'+a;var b=c[a];delete c[a];return b;}
function Dl(){}
_=Dl.prototype=new Bj();_.g=dn;_.k=gn;_.o=ln;_.tI=33;_.a=null;_.b=null;_.c=0;_.d=null;var Fm;function Fl(b,a,c){b.a=a;b.b=c;return b;}
function bm(a,b){return Fl(new El(),a,b);}
function cm(b){var a;if(lb(b,15)){a=kb(b,15);if(hn(this.a,a.l())&&hn(this.b,a.m())){return true;}}return false;}
function dm(){return this.a;}
function em(){return this.b;}
function fm(){var a,b;a=0;b=0;if(this.a!==null){a=this.a.hC();}if(this.b!==null){b=this.b.hC();}return a^b;}
function El(){}
_=El.prototype=new ni();_.eQ=cm;_.l=dm;_.m=em;_.hC=fm;_.tI=34;_.a=null;_.b=null;function pm(b,a){b.a=a;return b;}
function rm(a){return im(new hm(),a.a);}
function sm(c){var a,b,d;if(lb(c,15)){a=kb(c,15);b=a.l();if(zm(this.a,b)){d=Cm(this.a,b);return hn(a.m(),d);}}return false;}
function tm(){return rm(this);}
function um(){return this.a.c;}
function gm(){}
_=gm.prototype=new bl();_.h=sm;_.q=tm;_.B=um;_.tI=35;function im(c,b){var a;c.c=b;a=il(new gl());if(c.c.b!==(ym(),Fm)){jl(a,Fl(new El(),null,c.c.b));}bn(c.c.d,a);an(c.c.a,a);c.a=uj(a);return c;}
function km(a){return nj(a.a);}
function lm(a){return a.b=kb(oj(a.a),15);}
function mm(a){if(a.b===null){throw gi(new fi(),'Must call next() before remove().');}else{pj(a.a);Em(a.c,a.b.l());a.b=null;}}
function nm(){return km(this);}
function om(){return lm(this);}
function hm(){}
_=hm.prototype=new ni();_.p=nm;_.s=om;_.tI=0;_.a=null;_.b=null;function sn(){}
_=sn.prototype=new ri();_.tI=36;function Ah(){xb(new sb());}
function gwtOnLoad(b,d,c){$moduleName=d;$moduleBase=c;if(b)try{Ah();}catch(a){b(d);}else{Ah();}}
var ob=[{},{},{1:1},{3:1},{3:1},{3:1},{3:1},{2:1},{6:1},{2:1,4:1},{2:1},{5:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{8:1,9:1,10:1,11:1},{12:1},{12:1},{12:1},{7:1,8:1,9:1,10:1,11:1},{5:1},{3:1},{3:1},{3:1},{3:1},{3:1},{3:1},{13:1},{14:1},{14:1},{13:1},{15:1},{14:1},{3:1}];if (com_google_gwt_sample_hello_Hello) {  var __gwt_initHandlers = com_google_gwt_sample_hello_Hello.__gwt_initHandlers;  com_google_gwt_sample_hello_Hello.onScriptLoad(gwtOnLoad);}})();