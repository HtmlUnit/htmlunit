(function(){var $wnd = window;var $doc = $wnd.document;var $moduleName, $moduleBase;var _,fL='com.google.gwt.core.client.',gL='com.google.gwt.lang.',hL='com.google.gwt.sample.mail.client.',iL='com.google.gwt.user.client.',jL='com.google.gwt.user.client.impl.',kL='com.google.gwt.user.client.ui.',lL='com.google.gwt.user.client.ui.impl.',mL='java.lang.',nL='java.util.';function eL(){}
function pF(a){return this===a;}
function qF(){return gG(this);}
function nF(){}
_=nF.prototype={};_.eQ=pF;_.hC=qF;_.tN=mL+'Object';_.tI=1;function u(){return B();}
function v(a){return a==null?null:a.tN;}
var w=null;function z(a){return a==null?0:a.$H?a.$H:(a.$H=C());}
function A(a){return a==null?0:a.$H?a.$H:(a.$H=C());}
function B(){return $moduleBase;}
function C(){return ++D;}
var D=0;function ab(b,a){if(!yb(a,2)){return false;}return eb(b,xb(a,2));}
function bb(a){return z(a);}
function cb(){return [];}
function db(){return {};}
function fb(a){return ab(this,a);}
function eb(a,b){return a===b;}
function gb(){return bb(this);}
function E(){}
_=E.prototype=new nF();_.eQ=fb;_.hC=gb;_.tN=fL+'JavaScriptObject';_.tI=7;function ib(c,a,d,b,e){c.a=a;c.b=b;c.tN=e;c.tI=d;return c;}
function kb(a,b,c){return a[b]=c;}
function mb(a,b){return lb(a,b);}
function lb(a,b){return ib(new hb(),b,a.tI,a.b,a.tN);}
function nb(b,a){return b[a];}
function pb(b,a){return b[a];}
function ob(a){return a.length;}
function rb(e,d,c,b,a){return qb(e,d,c,b,0,ob(b),a);}
function qb(j,i,g,c,e,a,b){var d,f,h;if((f=nb(c,e))<0){throw new EE();}h=ib(new hb(),f,nb(i,e),nb(g,e),j);++e;if(e<a){j=CF(j,1);for(d=0;d<f;++d){kb(h,d,qb(j,i,g,c,e,a,b));}}else{for(d=0;d<f;++d){kb(h,d,b);}}return h;}
function sb(f,e,c,g){var a,b,d;b=ob(g);d=ib(new hb(),b,e,c,f);for(a=0;a<b;++a){kb(d,a,pb(g,a));}return d;}
function tb(a,b,c){if(c!==null&&a.b!=0&& !yb(c,a.b)){throw new ED();}return kb(a,b,c);}
function hb(){}
_=hb.prototype=new nF();_.tN=gL+'Array';_.tI=8;function wb(b,a){return !(!(b&&Db[b][a]));}
function xb(b,a){if(b!=null)wb(b.tI,a)||Cb();return b;}
function yb(b,a){return b!=null&&wb(b.tI,a);}
function zb(a){return a&65535;}
function Ab(a){return ~(~a);}
function Bb(a){if(a>(vE(),wE))return vE(),wE;if(a<(vE(),xE))return vE(),xE;return a>=0?Math.floor(a):Math.ceil(a);}
function Cb(){throw new eE();}
function Eb(b,d){_=d.prototype;if(b&& !(b.tI>=_.tI)){var c=b.toString;for(var a in _){b[a]=_[a];}b.toString=c;}return b;}
var Db;function kA(b,a){BA(b.cb(),a,true);}
function mA(a){return Eh(a.ab());}
function nA(a){return Fh(a.ab());}
function oA(a){return fi(a.r,'offsetHeight');}
function pA(a){return fi(a.r,'offsetWidth');}
function qA(d,b,a){var c=b.parentNode;if(!c){return;}c.insertBefore(a,b);c.removeChild(b);}
function rA(b,a){if(b.r!==null){qA(b,b.r,a);}b.r=a;}
function sA(b,c,a){b.hc(c);b.dc(a);}
function tA(b,a){AA(b.cb(),a);}
function uA(b,a){Di(b.ab(),a|hi(b.ab()));}
function vA(){return this.r;}
function wA(){return this.r;}
function xA(a){return gi(a,'className');}
function yA(a){rA(this,a);}
function zA(a){Ci(this.r,'height',a);}
function AA(a,b){xi(a,'className',b);}
function BA(c,j,a){var b,d,e,f,g,h,i;if(c===null){throw sF(new rF(),'Null widget handle. If you are creating a composite, ensure that initWidget() has been called.');}j=EF(j);if(BF(j)==0){throw mE(new lE(),'Style names cannot be empty');}i=xA(c);e=zF(i,j);while(e!=(-1)){if(e==0||vF(i,e-1)==32){f=e+BF(j);g=BF(i);if(f==g||f<g&&vF(i,f)==32){break;}}e=AF(i,j,e+1);}if(a){if(e==(-1)){if(BF(i)>0){i+=' ';}xi(c,'className',i+j);}}else{if(e!=(-1)){b=EF(DF(i,0,e));d=EF(CF(i,e+BF(j)));if(BF(b)==0){h=d;}else if(BF(d)==0){h=b;}else{h=b+' '+d;}xi(c,'className',h);}}}
function CA(a,b){a.style.display=b?'':'none';}
function DA(a){CA(this.r,a);}
function EA(a){Ci(this.r,'width',a);}
function jA(){}
_=jA.prototype=new nF();_.ab=vA;_.cb=wA;_.bc=yA;_.dc=zA;_.fc=DA;_.hc=EA;_.tN=kL+'UIObject';_.tI=11;_.r=null;function gC(a){if(!a.hb()){throw pE(new oE(),"Should only call onDetach when the widget is attached to the browser's document");}try{a.yb();}finally{a.B();yi(a.ab(),null);a.o=false;}}
function hC(a){if(yb(a.q,17)){xb(a.q,17).Eb(a);}else if(a.q!==null){throw pE(new oE(),"This widget's parent does not implement HasWidgets");}}
function iC(b,a){if(b.hb()){yi(b.ab(),null);}rA(b,a);if(b.hb()){yi(a,b);}}
function jC(b,a){b.p=a;}
function kC(c,b){var a;a=c.q;if(b===null){if(a!==null&&a.hb()){c.pb();}c.q=null;}else{if(a!==null){throw pE(new oE(),'Cannot set a new parent without first clearing the old parent');}c.q=b;if(b.hb()){c.lb();}}}
function lC(){}
function mC(){}
function nC(){return this.o;}
function oC(){if(this.hb()){throw pE(new oE(),"Should only call onAttach when the widget is detached from the browser's document");}this.o=true;yi(this.ab(),this);this.A();this.sb();}
function pC(a){}
function qC(){gC(this);}
function rC(){}
function sC(){}
function tC(a){iC(this,a);}
function hB(){}
_=hB.prototype=new jA();_.A=lC;_.B=mC;_.hb=nC;_.lb=oC;_.mb=pC;_.pb=qC;_.sb=rC;_.yb=sC;_.bc=tC;_.tN=kL+'Widget';_.tI=12;_.o=false;_.p=null;_.q=null;function qv(b,a){kC(a,b);}
function sv(b,a){kC(a,null);}
function tv(){var a,b;for(b=this.ib();b.gb();){a=xb(b.kb(),12);a.lb();}}
function uv(){var a,b;for(b=this.ib();b.gb();){a=xb(b.kb(),12);a.pb();}}
function vv(){}
function wv(){}
function pv(){}
_=pv.prototype=new hB();_.A=tv;_.B=uv;_.sb=vv;_.yb=wv;_.tN=kL+'Panel';_.tI=13;function mx(a){nx(a,dh());return a;}
function nx(b,a){b.bc(a);return b;}
function ox(a,b){if(a.n!==null){throw pE(new oE(),'SimplePanel can only contain one child widget');}a.gc(b);}
function qx(a,b){if(b===a.n){return;}if(b!==null){hC(b);}if(a.n!==null){a.Eb(a.n);}a.n=b;if(b!==null){ah(a.F(),a.n.ab());qv(a,b);}}
function rx(){return this.ab();}
function sx(){return ix(new gx(),this);}
function tx(a){if(this.n!==a){return false;}sv(this,a);pi(this.F(),a.ab());this.n=null;return true;}
function ux(a){qx(this,a);}
function fx(){}
_=fx.prototype=new pv();_.F=rx;_.ib=sx;_.Eb=tx;_.gc=ux;_.tN=kL+'SimplePanel';_.tI=14;_.n=null;function Dv(){Dv=eL;lw=wD(new rD());}
function yv(a){Dv();nx(a,yD(lw));ew(a,0,0);return a;}
function zv(b,a){Dv();yv(b);b.g=a;return b;}
function Av(c,a,b){Dv();zv(c,a);c.k=b;return c;}
function Bv(b,a){if(a.blur){a.blur();}}
function Cv(c){var a,b,d;a=c.l;if(!a){fw(c,false);iw(c);}b=Bb((mk()-Fv(c))/2);d=Bb((lk()-Ev(c))/2);ew(c,nk()+b,ok()+d);if(!a){fw(c,true);}}
function Ev(a){return oA(a);}
function Fv(a){return pA(a);}
function aw(a){bw(a,false);}
function bw(b,a){if(!b.l){return;}b.l=false;vm(Bw(),b);b.ab();}
function cw(a){var b;b=a.n;if(b!==null){if(a.h!==null){b.dc(a.h);}if(a.i!==null){b.hc(a.i);}}}
function dw(e,b){var a,c,d,f;d=Ah(b);c=mi(e.ab(),d);f=Ch(b);switch(f){case 128:{a=e.rb(zb(xh(b)),Bu(b));return a&&(c|| !e.k);}case 512:{a=(zb(xh(b)),Bu(b),true);return a&&(c|| !e.k);}case 256:{a=(zb(xh(b)),Bu(b),true);return a&&(c|| !e.k);}case 4:case 8:case 64:case 1:case 2:{if((Eg(),ri)!==null){return true;}if(!c&&e.g&&f==4){bw(e,true);return true;}break;}case 2048:{if(e.k&& !c&&d!==null){Bv(e,d);return false;}}}return !e.k||c;}
function ew(c,b,d){var a;if(b<0){b=0;}if(d<0){d=0;}c.j=b;c.m=d;a=c.ab();Ci(a,'left',b+'px');Ci(a,'top',d+'px');}
function fw(a,b){Ci(a.ab(),'visibility',b?'visible':'hidden');a.ab();}
function gw(a,b){qx(a,b);cw(a);}
function hw(a,b){a.i=b;cw(a);if(BF(b)==0){a.i=null;}}
function iw(a){if(a.l){return;}a.l=true;Fg(a);Ci(a.ab(),'position','absolute');if(a.m!=(-1)){ew(a,a.j,a.m);}tm(Bw(),a);a.ab();}
function jw(){return zD(lw,this.ab());}
function kw(){return zD(lw,this.ab());}
function mw(){qi(this);gC(this);}
function nw(a){return dw(this,a);}
function ow(a,b){return true;}
function pw(a){this.h=a;cw(this);if(BF(a)==0){this.h=null;}}
function qw(a){fw(this,a);}
function rw(a){gw(this,a);}
function sw(a){hw(this,a);}
function xv(){}
_=xv.prototype=new fx();_.F=jw;_.cb=kw;_.pb=mw;_.qb=nw;_.rb=ow;_.dc=pw;_.fc=qw;_.gc=rw;_.hc=sw;_.tN=kL+'PopupPanel';_.tI=15;_.g=false;_.h=null;_.i=null;_.j=(-1);_.k=false;_.l=false;_.m=(-1);var lw;function zo(){zo=eL;Dv();}
function vo(a){a.a=at(new uq());a.f=cq(new Dp());}
function wo(a){zo();xo(a,false);return a;}
function xo(b,a){zo();yo(b,a,true);return b;}
function yo(c,a,b){zo();Av(c,a,b);vo(c);Cs(c.f,0,0,c.a);c.f.dc('100%');us(c.f,0);ws(c.f,0);xs(c.f,0);gr(c.f.b,1,0,'100%');jr(c.f.b,1,0,'100%');fr(c.f.b,1,0,(lt(),mt),(ut(),vt));gw(c,c.f);tA(c,'gwt-DialogBox');tA(c.a,'Caption');av(c.a,c);return c;}
function Ao(b,a){cv(b.a,a);}
function Bo(a,b){if(a.b!==null){ts(a.f,a.b);}if(b!==null){Cs(a.f,1,0,b);}a.b=b;}
function Co(a){if(Ch(a)==4){if(mi(this.a.ab(),Ah(a))){Dh(a);}}return dw(this,a);}
function Do(a,b,c){this.e=true;ui(this.a.ab());this.c=b;this.d=c;}
function Eo(a){}
function Fo(a){}
function ap(c,d,e){var a,b;if(this.e){a=d+mA(this);b=e+nA(this);ew(this,a-this.c,b-this.d);}}
function bp(a,b,c){this.e=false;oi(this.a.ab());}
function cp(a){if(this.b!==a){return false;}ts(this.f,a);return true;}
function dp(a){Bo(this,a);}
function ep(a){hw(this,a);this.f.hc('100%');}
function uo(){}
_=uo.prototype=new xv();_.qb=Co;_.tb=Do;_.ub=Eo;_.vb=Fo;_.wb=ap;_.xb=bp;_.Eb=cp;_.gc=dp;_.hc=ep;_.tN=kL+'DialogBox';_.tI=16;_.b=null;_.c=0;_.d=0;_.e=false;function hc(){hc=eL;zo();}
function gc(c){var a,b;hc();wo(c);Ao(c,'About the Mail Sample');a=bB(new FA());b=bt(new uq(),"This sample application demonstrates the construction of a complex user interface using GWT's built-in widgets.  Have a look at the code to see how easy it is to build your own apps!");tA(b,'mail-AboutText');cB(a,b);cB(a,bn(new Am(),'Close',dc(new cc(),c)));Bo(c,a);return c;}
function ic(a,b){switch(a){case 13:case 27:aw(this);break;}return true;}
function bc(){}
_=bc.prototype=new uo();_.rb=ic;_.tN=hL+'AboutDialog';_.tI=17;function dc(b,a){b.a=a;return b;}
function fc(a){aw(this.a);}
function cc(){}
_=cc.prototype=new nF();_.ob=fc;_.tN=hL+'AboutDialog$1';_.tI=18;function oo(a){if(a.h===null){throw pE(new oE(),'initWidget() was never called in '+v(a));}return a.r;}
function po(a,b){if(a.h!==null){throw pE(new oE(),'Composite.initWidget() may only be called once.');}hC(b);a.bc(b.ab());a.h=b;kC(b,a);}
function qo(){return oo(this);}
function ro(){if(this.h!==null){return this.h.hb();}return false;}
function so(){this.h.lb();this.sb();}
function to(){try{this.yb();}finally{this.h.pb();}}
function mo(){}
_=mo.prototype=new hB();_.ab=qo;_.hb=ro;_.lb=so;_.pb=to;_.tN=kL+'Composite';_.tI=19;_.h=null;function vc(a){a.a=sb('[Lcom.google.gwt.sample.mail.client.Contacts$Contact;',128,22,[sc(new oc(),'Benoit Mandelbrot','benoit@example.com',a),sc(new oc(),'Albert Einstein','albert@example.com',a),sc(new oc(),'Rene Descartes','rene@example.com',a),sc(new oc(),'Bob Saget','bob@example.com',a),sc(new oc(),'Ludwig von Beethoven','ludwig@example.com',a),sc(new oc(),'Richard Feynman','richard@example.com',a),sc(new oc(),'Alan Turing','alan@example.com',a),sc(new oc(),'John von Neumann','john@example.com',a)]);a.b=bB(new FA());}
function wc(d,b){var a,c;vc(d);c=mx(new fx());c.gc(d.b);for(a=0;a<d.a.a;++a){xc(d,d.a[a]);}po(d,c);tA(d,'mail-Contacts');return d;}
function xc(c,a){var b;b=bt(new uq(),"<a href='javascript:;'>"+a.b+'<\/a>');cB(c.b,b);Fu(b,lc(new kc(),c,a,b));}
function jc(){}
_=jc.prototype=new mo();_.tN=hL+'Contacts';_.tI=20;function lc(b,a,c,d){b.a=a;b.b=c;b.c=d;return b;}
function nc(c){var a,b,d;b=qc(new pc(),this.b,this.a);a=mA(this.c)+14;d=nA(this.c)+14;ew(b,a,d);iw(b);}
function kc(){}
_=kc.prototype=new nF();_.ob=nc;_.tN=hL+'Contacts$1';_.tI=21;function sc(d,b,a,c){d.b=b;d.a=a;return d;}
function oc(){}
_=oc.prototype=new nF();_.tN=hL+'Contacts$Contact';_.tI=22;_.a=null;_.b=null;function rc(){rc=eL;Dv();}
function qc(g,a,f){var b,c,d,e;rc();zv(g,true);d=bB(new FA());e=Eu(new Cu(),a.b);b=Eu(new Cu(),a.a);cB(d,e);cB(d,b);c=Bt(new zt());ln(c,4);Ct(c,DC((ie(),le)));Ct(c,d);ox(g,c);tA(g,'mail-ContactPopup');tA(e,'mail-ContactPopupName');tA(b,'mail-ContactPopupEmail');return g;}
function pc(){}
_=pc.prototype=new xv();_.tN=hL+'Contacts$ContactPopup';_.tI=23;function ce(){ce=eL;Ae=he(new ge());}
function ae(a){a.e=yf(new vf(),Ae);a.c=bB(new FA());a.a=bd(new Fc());a.d=lf(new df(),Ae);}
function be(a){ce();ae(a);return a;}
function de(b,a){ed(b.a,a);}
function ee(b){var a;Ce=b;b.e.hc('100%');b.b=yd(new wd());b.b.hc('100%');cB(b.c,b.b);cB(b.c,b.a);b.b.hc('100%');b.a.hc('100%');a=op(new fp());pp(a,b.e,(qp(),yp));pp(a,b.d,(qp(),Ap));pp(a,b.c,(qp(),wp));a.hc('100%');ln(a,4);vp(a,b.c,'100%');ek(b);hk(false);uk('0px');tm(Bw(),a);cj(Bc(new Ac(),b));fe(b,mk(),lk());}
function fe(c,d,a){var b;b=a-nA(c.d)-8;if(b<1){b=1;}c.d.dc(''+b);cd(c.a,d,a);}
function Be(b,a){fe(this,b,a);}
function zc(){}
_=zc.prototype=new nF();_.Bb=Be;_.tN=hL+'Mail';_.tI=24;_.b=null;var Ae,Ce=null;function Bc(b,a){b.a=a;return b;}
function Dc(){fe(this.a,mk(),lk());}
function Ac(){}
_=Ac.prototype=new nF();_.D=Dc;_.tN=hL+'Mail$1';_.tI=25;function ad(a){a.c=bB(new FA());a.b=bB(new FA());a.g=at(new uq());a.f=at(new uq());a.d=at(new uq());a.a=at(new uq());a.e=bx(new Fw(),a.a);}
function bd(b){var a;ad(b);dv(b.a,true);cB(b.b,b.g);cB(b.b,b.f);cB(b.b,b.d);b.b.hc('100%');a=op(new fp());pp(a,b.b,(qp(),yp));pp(a,b.e,(qp(),wp));sp(a,b.e,'100%');cB(b.c,a);sA(a,'100%','100%');sA(b.e,'100%','100%');po(b,b.c);tA(b,'mail-Detail');tA(b.b,'mail-DetailHeader');tA(a,'mail-DetailInner');tA(b.g,'mail-DetailSubject');tA(b.f,'mail-DetailSender');tA(b.d,'mail-DetailRecipient');tA(b.a,'mail-DetailBody');return b;}
function cd(c,e,d){var a,b;b=e-mA(c.e)-9;if(b<1){b=1;}a=d-nA(c.e)-9;if(a<1){a=1;}sA(c.e,''+b,''+a);}
function ed(b,a){et(b.g,a.e);et(b.f,'<b>From:<\/b>&nbsp;'+a.d);et(b.d,'<b>To:<\/b>&nbsp;foo@example.com');et(b.a,a.a);}
function Fc(){}
_=Fc.prototype=new mo();_.tN=hL+'MailDetail';_.tI=26;function gd(e,c,b,d,a){e.d=c;e.b=b;e.e=d;e.a=a;return e;}
function fd(){}
_=fd.prototype=new nF();_.tN=hL+'MailItem';_.tI=27;_.a=null;_.b=null;_.c=false;_.d=null;_.e=null;function jd(){jd=eL;var a;td=sb('[Ljava.lang.String;',129,1,['markboland05','Hollie Voss','boticario','Emerson Milton','Healy Colette','Brigitte Cobb','Elba Lockhart','Claudio Engle','Dena Pacheco','Brasil s.p','Parker','derbvktqsr','qetlyxxogg','antenas.sul','Christina Blake','Gail Horton','Orville Daniel','PostMaster','Rae Childers','Buster misjenou','user31065','ftsgeolbx','aqlovikigd','user18411','Mildred Starnes','Candice Carson','Louise Kelchner','Emilio Hutchinson','Geneva Underwood','Residence Oper?','fpnztbwag','tiger','Heriberto Rush','bulrush Bouchard','Abigail Louis','Chad Andrews','bjjycpaa','Terry English','Bell Snedden','huang','hhh','(unknown sender)','Kent','Dirk Newman','Equipe Virtual Cards','wishesundmore','Benito Meeks']);md=sb('[Ljava.lang.String;',129,1,['mark@example.com','hollie@example.com','boticario@example.com','emerson@example.com','healy@example.com','brigitte@example.com','elba@example.com','claudio@example.com','dena@example.com','brasilsp@example.com','parker@example.com','derbvktqsr@example.com','qetlyxxogg@example.com','antenas_sul@example.com','cblake@example.com','gailh@example.com','orville@example.com','post_master@example.com','rchilders@example.com','buster@example.com','user31065@example.com','ftsgeolbx@example.com','aqlovikigd@example.com','user18411@example.com','mildred@example.com','candice@example.com','louise_kelchner@example.com','emilio@example.com','geneva@example.com','residence_oper@example.com','fpnztbwag@example.com','tiger@example.com','heriberto@example.com','bulrush@example.com','abigail_louis@example.com','chada@example.com','bjjycpaa@example.com','terry@example.com','bell@example.com','huang@example.com','hhh@example.com','kent@example.com','newman@example.com','equipe_virtual@example.com','wishesundmore@example.com','benito@example.com']);vd=sb('[Ljava.lang.String;',129,1,['URGENT -[Mon, 24 Apr 2006 02:17:27 +0000]','URGENT TRANSACTION -[Sun, 23 Apr 2006 13:10:03 +0000]','fw: Here it comes','voce ganho um vale presente Boticario','Read this ASAP','Hot Stock Talk','New Breed of Equity Trader','FWD: TopWeeks the wire special pr news release','[fwd] Read this ASAP','Renda Extra R$1.000,00-R$2.000,00/m?s','re: Make sure your special pr news released','Forbidden Knowledge Conference','decodificadores os menores pre?os','re: Our Pick','RE: The hottest pick Watcher','RE: St0kkMarrkett Picks Trade watch special pr news release','St0kkMarrkett Picks Watch special pr news release news','You are a Winner oskoxmshco','Encrypted E-mail System (VIRUS REMOVED)','Fw: Malcolm','Secure Message System (VIRUS REMOVED)','fwd: St0kkMarrkett Picks Watch special pr news releaser','FWD: Financial Market Traderr special pr news release','? s? uma dica r?pida !!!!! leia !!!','re: You have to heard this','fwd: Watcher TopNews','VACANZE alle Mauritius','funny','re: You need to review this','[re:] Our Pick','RE: Before the be11 special pr news release','[re:] Market TradePicks Trade watch news','No prescription needed','Seu novo site','[fwd] Financial Market Trader Picker','FWD: Top Financial Market Specialists Trader interest increases','Os cart?es mais animados da web!!','We will sale 4 you cebtdbwtcv','RE: Best Top Financial Market Specialists Trader Picks']);od=sb('[Ljava.lang.String;',129,1,['Dear Friend,<br><br>I am Mr. Mark Boland the Bank Manager of ABN AMRO BANK 101 Moorgate, London, EC2M 6SB.<br><br>','I have an urgent and very confidential business proposition for you. On July 20, 2001; Mr. Zemenu Gente, a National of France, who used to be a private contractor with the Shell Petroleum Development Company in Saudi Arabia. Mr. Zemenu Gente Made a Numbered time (Fixed deposit) for 36 calendar months, valued at GBP?30, 000,000.00 (Thirty Million Pounds only) in my Branch.','I have all necessary legal documents that can be used to back up any claim we may make. All I require is your honest Co-operation, Confidentiality and A trust to enable us sees this transaction through. I guarantee you that this will be executed under a legitimate arrangement that will protect you from any breach of the law. Please get in touch with me urgently by E-mail and Provide me with the following;<br>','The OIL sector is going crazy. This is our weekly gift to you!<br><br>Get KKPT First Thing, This Is Going To Run!<br><br>Check out Latest NEWS!<br><br>KOKO PETROLEUM (KKPT) - This is our #1 pick for next week!<br>Our last pick gained $2.16 in 4 days of trading.<br>','LAS VEGAS, NEVADA--(MARKET WIRE)--Apr 6, 2006 -- KOKO Petroleum, Inc. (Other OTC:KKPT.PK - News) -<br>KOKO Petroleum, Inc. announced today that its operator for the Corsicana Field, JMT Resources, Ltd. ("JMT") will commence a re-work program on its Pecan Gap wells in the next week. The re-work program will consist of drilling six lateral bore production strings from the existing well bore. This process, known as Radial Jet Enhancement, will utilize high pressure fluids to drill the lateral well bores, which will extend out approximately 350\' each.','JMT has contracted with Well Enhancement Services, LLC (www.wellenhancement.com) to perform the rework on its Pierce nos. 14 and 14a. A small sand frac will follow the drilling of the lateral well bores in order to enhance permeability and create larger access to the Pecan Gap reservoir. Total cost of the re-work per well is estimated to be approximately $50,000 USD.','Parab?ns!<br>Voc? Ganhou Um Vale Presente da Botic?rio no valor de R$50,00<br>Voc? foi contemplado na Promo??o Respeite Minha Natureza - Pulseira Social.<br>Algu?m pode t?-lo inscrito na promo??o! (Amigos(as), Namorado(a) etc.).<br>Para retirar o seu pr?mio em uma das nossas Lojas, fa?a o download do Vale-Presente abaixo.<br>Ap?s o download, com o arquivo previamente salvo, imprima uma folha e salve a c?pia em seu computador para evitar transtornos decorrentes da perda do mesmo. Lembramos que o Vale-Presente ? ?nico e intransfer?vel.','Large Marketing Campaign running this weekend!<br><br>Should you get in today before it explodes?<br><br>This Will Fly Starting Monday!','PREMIER INFORMATION (PIFR)<br>A U.S. based company offers specialized information management serices to both the Insurance and Healthcare Industries. The services we provide are specific to each industry and designed for quick response and maximum security.<br><br>STK- PIFR<br>Current Price: .20<br>This one went to $2.80 during the last marketing Campaign!','These partnerships specifically allow Premier to obtain personal health information, as governed by the Health In-surancee Portability and Accountability Act of 1996 (HIPAA), and other applicable state laws and regulations.<br><br>Global HealthCare Market Undergoing Digital Conversion','>>   Componentes e decodificadores; confira aqui;<br> http://br.geocities.com/listajohn/index.htm<br>','THE GOVERNING AWARD<br>NETHERLANDS HEAD OFFICE<br>AC 76892 HAUITSOP<br>AMSTERDAM, THE NETHERLANDS.<br>FROM: THE DESK OF THE PROMOTIONS MANAGER.<br>INTERNATIONAL PROMOTIONS / PRIZE AWARD DEPARTMENT<br>REF NUMBER: 14235/089.<br>BATCH NUMBER: 304/64780/IFY.<br>RE/AWARD NOTIFICATION<br>',"We are pleased to inform you of the announcement today 13th of April 2006, you among TWO LUCKY WINNERS WON the GOVERNING AWARD draw held on the 28th of March 2006. The THREE Winning Addresses were randomly selected from a batch of 10,000,000 international email addresses. Your email address emerged alongside TWO others as a category B winner in this year's Annual GOVERNING AWARD Draw.<br>",'>> obrigado por me dar esta pequena aten??o !!!<br>CASO GOSTE DE ASSISTIR TV , MAS A SUA ANTENA S? PEGA AQUELES CANAIS LOCAIS  OU O SEU SISTEMA PAGO ? MUITO CARO , SAIBA QUE TENHO CART?ES DE ACESSO PARA SKY DIRECTV , E DECODERS PARA  NET TVA E TECSAT , TUDO GRATIS , SEM ASSINTURA , SEM MENSALIDADE, VC PAGA UMA VEZ S? E ASSISTE A MUITOS CANAIS , FILMES , JOGOS , PORNOS , DESENHOS , DOCUMENT?RIOS ,SHOWS , ETC,<br><br>CART?O SKY E DIRECTV TOTALMENTE HACKEADOS  350,00<br>DECODERS NET TVA DESBLOQUEADOS                       390,00<br>KITS COMPLETOS SKY OU DTV ANTENA DECODER E CART?O  650,00<br>TECSAT FREE   450,00<br>TENHO TB ACESS?RIOS , CABOS, LNB .<br>','********************************************************************<br> Original filename: mail.zip<br> Virus discovered: JS.Feebs.AC<br>********************************************************************<br> A file that was attached to this email contained a virus.<br> It is very likely that the original message was generated<br> by the virus and not a person - treat this message as you would<br> any other junk mail (spam).<br> For more information on why you received this message please visit:<br>','Put a few letters after your name. Let us show you how you can do it in just a few days.<br><br>http://thewrongchoiceforyou.info<br><br>kill future mailing by pressing this : see main website',"We possess scores of pharmaceutical products handy<br>All med's are made in U.S. laboratories<br>For your wellbeing! Very rapid, protected and secure<br>Ordering, No script required. We have the pain aid you require<br>",'"Oh, don\'t speak to me of Austria. Perhaps I don\'t understand things, but Austria never has wished, and does not wish, for war. She is betraying us! Russia alone must save Europe. Our gracious sovereign recognizes his high vocation and will be true to it. That is the one thing I have faith in! Our good and wonderful sovereign has to perform the noblest role on earth, and he is so virtuous and noble that God will not forsake him. He will fulfill his vocation and crush the hydra of revolution, which has become more terrible than ever in the person of this murderer and villain! We alone must avenge the blood of the just one.... Whom, I ask you, can we rely on?... England with her commercial spirit will not and cannot understand the Emperor Alexander\'s loftiness of soul. She has refused to evacuate Malta. She wanted to find, and still seeks, some secret motive in our actions. What answer did Novosiltsev get? None. The English have not understood and cannot understand the self-ab!<br>negation of our Emperor who wants nothing for himself, but only desires the good of mankind. And what have they promised? Nothing! And what little they have promised they will not perform! Prussia has always declared that Buonaparte is invincible, and that all Europe is powerless before him.... And I don\'t believe a word that Hardenburg says, or Haugwitz either. This famous Prussian neutrality is just a trap. I have faith only in God and the lofty destiny of our adored monarch. He will save Europe!"<br>"Those were extremes, no doubt, but they are not what is most important. What is important are the rights of man, emancipation from prejudices, and equality of citizenship, and all these ideas Napoleon has retained in full force."']);rd=sI(new qI());{for(a=0;a<37;++a){tI(rd,kd());}}}
function kd(){jd();var a,b,c,d,e;d=td[sd++];if(sd==td.a){sd=0;}b=md[ld++];if(ld==md.a){ld=0;}e=vd[ud++];if(ud==vd.a){ud=0;}a='';for(c=0;c<10;++c){a+=od[nd++];if(nd==od.a){nd=0;}}return gd(new fd(),d,b,e,a);}
function qd(a){jd();if(a>=rd.b){return null;}return xb(xI(rd,a),4);}
function pd(){jd();return rd.b;}
var ld=0,md,nd=0,od,rd,sd=0,td,ud=0,vd;function xd(a){a.a=at(new uq());a.c=ct(new uq(),"<a href='javascript:;'>&lt; newer<\/a>",true);a.d=ct(new uq(),"<a href='javascript:;'>older &gt;<\/a>",true);a.g=cq(new Dp());a.b=Bt(new zt());}
function yd(b){var a;xd(b);xs(b.g,0);ws(b.g,0);b.g.hc('100%');is(b.g,b);Fu(b.c,b);Fu(b.d,b);a=Bt(new zt());tA(b.b,'mail-ListNavBar');Ct(a,b.c);Ct(a,b.a);Ct(a,b.d);Ft(b.b,(lt(),ot));Ct(b.b,a);b.b.hc('100%');po(b,b.g);tA(b,'mail-List');Ad(b);Dd(b);return b;}
function Ad(b){var a;Bs(b.g,0,0,'Sender');Bs(b.g,0,1,'Email');Bs(b.g,0,2,'Subject');Cs(b.g,0,3,b.b);wr(b.g.d,0,'mail-ListHeader');for(a=0;a<10;++a){Bs(b.g,a+1,0,'');Bs(b.g,a+1,1,'');Bs(b.g,a+1,2,'');kr(b.g.b,a+1,0,false);kr(b.g.b,a+1,1,false);kr(b.g.b,a+1,2,false);bq(b.g.b,a+1,2,2);}}
function Bd(c,b){var a;a=qd(c.f+b);if(a===null){return;}Cd(c,c.e,false);Cd(c,b,true);a.c=true;c.e=b;de((ce(),Ce),a);}
function Cd(c,a,b){if(a!=(-1)){if(b){rr(c.g.d,a+1,'mail-SelectedRow');}else{vr(c.g.d,a+1,'mail-SelectedRow');}}}
function Dd(e){var a,b,c,d;a=pd();d=e.f+10;if(d>a){d=a;}e.c.fc(e.f!=0);e.d.fc(e.f+10<a);cv(e.a,''+(e.f+1)+' - '+d+' of '+a);b=0;for(;b<10;++b){if(e.f+b>=pd()){break;}c=qd(e.f+b);Bs(e.g,b+1,0,c.d);Bs(e.g,b+1,1,c.b);Bs(e.g,b+1,2,c.e);}for(;b<10;++b){zs(e.g,b+1,0,'&nbsp;');zs(e.g,b+1,1,'&nbsp;');zs(e.g,b+1,2,'&nbsp;');}if(e.e==(-1)){Bd(e,0);}}
function Ed(c,b,a){if(b>0){Bd(this,b-1);}}
function Fd(a){if(a===this.d){this.f+=10;if(this.f>=pd()){this.f-=10;}else{Cd(this,this.e,false);this.e=(-1);Dd(this);}}else if(a===this.c){this.f-=10;if(this.f<0){this.f=0;}else{Cd(this,this.e,false);this.e=(-1);Dd(this);}}}
function wd(){}
_=wd.prototype=new mo();_.nb=Ed;_.ob=Fd;_.tN=hL+'MailList';_.tI=28;_.e=(-1);_.f=0;function ie(){ie=eL;je=u()+'B9DA8B0768BAD7283674A8E1D92AD03D.cache.png';ke=AC(new zC(),je,0,0,32,32);le=AC(new zC(),je,32,0,32,32);me=AC(new zC(),je,64,0,16,16);ne=AC(new zC(),je,80,0,16,16);oe=AC(new zC(),je,96,0,16,16);pe=AC(new zC(),je,112,0,4,4);qe=AC(new zC(),je,116,0,140,75);re=AC(new zC(),je,256,0,32,32);se=AC(new zC(),je,288,0,4,4);te=AC(new zC(),je,292,0,16,16);ue=AC(new zC(),je,308,0,32,32);ve=AC(new zC(),je,340,0,16,16);we=AC(new zC(),je,356,0,16,16);xe=AC(new zC(),je,372,0,16,16);ye=AC(new zC(),je,388,0,16,16);ze=AC(new zC(),je,404,0,16,16);}
function he(a){ie();return a;}
function ge(){}
_=ge.prototype=new nF();_.tN=hL+'Mail_Images_generatedBundle';_.tI=29;var je,ke,le,me,ne,oe,pe,qe,re,se,te,ue,ve,we,xe,ye,ze;function Fe(c,a){var b;c.a=sz(new ty(),a);b=Fy(new Cy(),cf(c,(ie(),ne),'foo@example.com'));tz(c.a,b);af(c,b,'Inbox',(ie(),oe));af(c,b,'Drafts',(ie(),me));af(c,b,'Templates',(ie(),ve));af(c,b,'Sent',(ie(),te));af(c,b,'Trash',(ie(),we));jz(b,true);po(c,c.a);return c;}
function af(d,c,e,a){var b;b=Fy(new Cy(),cf(d,a,e));c.s(b);return b;}
function cf(b,a,c){return '<span>'+EC(a)+c+'<\/span>';}
function De(){}
_=De.prototype=new mo();_.tN=hL+'Mailboxes';_.tI=30;_.a=null;function kf(a){a.b=ff(new ef(),a);}
function lf(b,a){kf(b);mf(b,a,Fe(new De(),a),(ie(),re),'Mail');mf(b,a,tf(new sf()),(ie(),ue),'Tasks');mf(b,a,wc(new jc(),a),(ie(),ke),'Contacts');po(b,b.b);return b;}
function mf(d,c,e,b,a){kA(e,'mail-StackContent');cy(d.b,e,pf(d,c,b,a),true);}
function of(b,a){return 'header-'+b.hC()+'-'+a;}
function pf(g,e,d,a){var b,c,f;f=g.a==0;c=of(g,g.a);g.a++;b="<table class='caption' cellpadding='0' cellspacing='0'><tr><td class='lcaption'>"+EC(d)+"<\/td><td class='rcaption'><b style='white-space:nowrap'>"+a+'<\/b><\/td><\/tr><\/table>';return "<table id='"+c+"' align='left' cellpadding='0' cellspacing='0'"+(f?" class='is-top'":'')+'><tbody>'+"<tr><td class='box-00'>"+EC((ie(),pe))+'<\/td>'+"<td class='box-10'>&nbsp;<\/td>"+"<td class='box-20'>"+EC((ie(),se))+'<\/td>'+'<\/tr><tr>'+"<td class='box-01'>&nbsp;<\/td>"+"<td class='box-11'>"+b+'<\/td>'+"<td class='box-21'>&nbsp;<\/td>"+'<\/tr><\/tbody><\/table>';}
function qf(d,c,b){var a;c++;if(c>0&&c<d.b.f.b){a=di(of(d,c));xi(a,'className','');}b++;if(b>0&&b<d.b.f.b){a=di(of(d,b));xi(a,'className','is-beneath-selected');}}
function rf(){ky(this.b,0);qf(this,(-1),0);}
function df(){}
_=df.prototype=new mo();_.sb=rf;_.tN=hL+'Shortcuts';_.tI=31;_.a=0;function Fn(a){a.f=qB(new iB(),a);}
function ao(a){Fn(a);return a;}
function bo(c,a,b){hC(a);rB(c.f,a);ah(b,a.ab());qv(c,a);}
function co(d,b,a){var c;eo(d,a);if(b.q===d){c=go(d,b);if(c<a){a--;}}return a;}
function eo(b,a){if(a<0||a>b.f.b){throw new rE();}}
function ho(b,a){return tB(b.f,a);}
function go(b,a){return uB(b.f,a);}
function io(e,b,c,a,d){a=co(e,b,a);hC(b);vB(e.f,b,a);if(d){li(c,oo(b),a);}else{ah(c,oo(b));}qv(e,b);}
function jo(b,c){var a;if(c.q!==b){return false;}sv(b,c);a=c.ab();pi(ji(a),a);yB(b.f,c);return true;}
function ko(){return wB(this.f);}
function lo(a){return jo(this,a);}
function En(){}
_=En.prototype=new pv();_.ib=ko;_.Eb=lo;_.tN=kL+'ComplexPanel';_.tI=32;function ay(b){var a;ao(b);a=mh();b.bc(a);b.b=jh();ah(a,b.b);wi(a,'cellSpacing',0);wi(a,'cellPadding',0);Di(a,1);tA(b,'gwt-StackPanel');return b;}
function by(a,b){fy(a,b,a.f.b);}
function cy(c,d,b,a){by(c,d);iy(c,c.f.b-1,b,a);}
function ey(d,a){var b,c;while(a!==null&& !bh(a,d.ab())){b=gi(a,'__index');if(b!==null){c=fi(a,'__owner');if(c==d.hC()){return yE(b);}else{return (-1);}}a=ji(a);}return (-1);}
function fy(e,h,a){var b,c,d,f,g;g=lh();d=kh();ah(g,d);f=lh();c=kh();ah(f,c);a=co(e,h,a);b=a*2;li(e.b,f,b);li(e.b,g,b);BA(d,'gwt-StackPanelItem',true);wi(d,'__owner',e.hC());xi(d,'height','1px');xi(c,'height','100%');xi(c,'vAlign','top');io(e,h,c,a,false);ly(e,a);if(e.c==(-1)){ky(e,0);}else{jy(e,a,false);if(e.c>=a){++e.c;}}}
function gy(d,a){var b,c;if(Ch(a)==1){c=Ah(a);b=ey(d,c);if(b!=(-1)){ky(d,b);}}}
function hy(e,a,b){var c,d,f;c=jo(e,a);if(c){d=2*b;f=ci(e.b,d);pi(e.b,f);f=ci(e.b,d);pi(e.b,f);if(e.c==b){e.c=(-1);}else if(e.c>b){--e.c;}ly(e,d);}return c;}
function iy(e,b,d,a){var c;if(b>=e.f.b){return;}c=ci(ci(e.b,b*2),0);if(a){zi(c,d);}else{Ai(c,d);}}
function jy(c,a,e){var b,d;d=ci(c.b,a*2);if(d===null){return;}b=ii(d);BA(b,'gwt-StackPanelItem-selected',e);d=ci(c.b,a*2+1);CA(d,e);ho(c,a).fc(e);}
function ky(b,a){if(a>=b.f.b||a==b.c){return;}if(b.c>=0){jy(b,b.c,false);}b.c=a;jy(b,b.c,true);}
function ly(f,a){var b,c,d,e;for(e=a,b=f.f.b;e<b;++e){d=ci(f.b,e*2);c=ii(d);wi(c,'__index',e);}}
function my(a){gy(this,a);}
function ny(a){return hy(this,a,go(this,a));}
function Fx(){}
_=Fx.prototype=new En();_.mb=my;_.Eb=ny;_.tN=kL+'StackPanel';_.tI=33;_.b=null;_.c=(-1);function ff(b,a){b.a=a;ay(b);return b;}
function hf(a){var b,c;c=this.c;gy(this,a);b=this.c;if(c!=b){qf(this.a,c,b);}}
function ef(){}
_=ef.prototype=new Fx();_.mb=hf;_.tN=hL+'Shortcuts$1';_.tI=34;function tf(c){var a,b;b=mx(new fx());a=bB(new FA());b.gc(a);cB(a,qn(new nn(),'Get groceries'));cB(a,qn(new nn(),'Walk the dog'));cB(a,qn(new nn(),'Start Web 2.0 company'));cB(a,qn(new nn(),'Write cool app in GWT'));cB(a,qn(new nn(),'Get funding'));cB(a,qn(new nn(),'Take a vacation'));po(c,b);tA(c,'mail-Tasks');return c;}
function sf(){}
_=sf.prototype=new mo();_.tN=hL+'Tasks';_.tI=35;function xf(a){a.b=bt(new uq(),"<a href='javascript:;'>Sign Out<\/a>");a.a=bt(new uq(),"<a href='javascript:;'>About<\/a>");}
function yf(f,a){var b,c,d,e;xf(f);e=Bt(new zt());b=bB(new FA());Ft(e,(lt(),ot));fB(b,(lt(),ot));c=Bt(new zt());ln(c,4);Ct(c,f.b);Ct(c,f.a);d=DC((ie(),qe));Ct(e,d);e.ac(d,(lt(),nt));Ct(e,b);cB(b,bt(new uq(),'<b>Welcome back, foo@example.com<\/b>'));cB(b,c);Fu(f.b,f);Fu(f.a,f);po(f,e);tA(f,'mail-TopPanel');tA(c,'mail-TopPanelLinks');return f;}
function Af(b){var a;if(b===this.b){fk('If this were implemented, you would be signed out now.');}else if(b===this.a){a=gc(new bc());iw(a);Cv(a);}}
function vf(){}
_=vf.prototype=new mo();_.ob=Af;_.tN=hL+'TopPanel';_.tI=36;function iG(b,a){a;return b;}
function hG(){}
_=hG.prototype=new nF();_.tN=mL+'Throwable';_.tI=3;function jE(b,a){iG(b,a);return b;}
function iE(){}
_=iE.prototype=new hG();_.tN=mL+'Exception';_.tI=4;function sF(b,a){jE(b,a);return b;}
function rF(){}
_=rF.prototype=new iE();_.tN=mL+'RuntimeException';_.tI=5;function Cf(b,a){return b;}
function Bf(){}
_=Bf.prototype=new rF();_.tN=iL+'CommandCanceledException';_.tI=37;function sg(a){a.a=ag(new Ff(),a);a.b=sI(new qI());a.d=eg(new dg(),a);a.f=ig(new hg(),a);}
function tg(a){sg(a);return a;}
function vg(c){var a,b,d;a=kg(c.f);ng(c.f);b=null;if(yb(a,5)){b=Cf(new Bf(),xb(a,5));}else{}if(b!==null){d=w;}yg(c,false);xg(c);}
function wg(e,d){var a,b,c,f;f=false;try{yg(e,true);og(e.f,e.b.b);yj(e.a,10000);while(lg(e.f)){b=mg(e.f);c=true;try{if(b===null){return;}if(yb(b,5)){a=xb(b,5);a.D();}else{}}finally{f=pg(e.f);if(f){return;}if(c){ng(e.f);}}if(Bg(fG(),d)){return;}}}finally{if(!f){vj(e.a);yg(e,false);xg(e);}}}
function xg(a){if(!AI(a.b)&& !a.e&& !a.c){zg(a,true);yj(a.d,1);}}
function yg(b,a){b.c=a;}
function zg(b,a){b.e=a;}
function Ag(b,a){tI(b.b,a);xg(b);}
function Bg(a,b){return CE(a-b)>=100;}
function Ef(){}
_=Ef.prototype=new nF();_.tN=iL+'CommandExecutor';_.tI=38;_.c=false;_.e=false;function wj(){wj=eL;Ej=sI(new qI());{Dj();}}
function uj(a){wj();return a;}
function vj(a){if(a.b){zj(a.c);}else{Aj(a.c);}CI(Ej,a);}
function xj(a){if(!a.b){CI(Ej,a);}a.Fb();}
function yj(b,a){if(a<=0){throw mE(new lE(),'must be positive');}vj(b);b.b=false;b.c=Bj(b,a);tI(Ej,b);}
function zj(a){wj();$wnd.clearInterval(a);}
function Aj(a){wj();$wnd.clearTimeout(a);}
function Bj(b,a){wj();return $wnd.setTimeout(function(){b.E();},a);}
function Cj(){var a;a=w;{xj(this);}}
function Dj(){wj();dk(new qj());}
function pj(){}
_=pj.prototype=new nF();_.E=Cj;_.tN=iL+'Timer';_.tI=39;_.b=false;_.c=0;var Ej;function bg(){bg=eL;wj();}
function ag(b,a){bg();b.a=a;uj(b);return b;}
function cg(){if(!this.a.c){return;}vg(this.a);}
function Ff(){}
_=Ff.prototype=new pj();_.Fb=cg;_.tN=iL+'CommandExecutor$1';_.tI=40;function fg(){fg=eL;wj();}
function eg(b,a){fg();b.a=a;uj(b);return b;}
function gg(){zg(this.a,false);wg(this.a,fG());}
function dg(){}
_=dg.prototype=new pj();_.Fb=gg;_.tN=iL+'CommandExecutor$2';_.tI=41;function ig(b,a){b.d=a;return b;}
function kg(a){return xI(a.d.b,a.b);}
function lg(a){return a.c<a.a;}
function mg(b){var a;b.b=b.c;a=xI(b.d.b,b.c++);if(b.c>=b.a){b.c=0;}return a;}
function ng(a){BI(a.d.b,a.b);--a.a;if(a.b<=a.c){if(--a.c<0){a.c=0;}}a.b=(-1);}
function og(b,a){b.a=a;}
function pg(a){return a.b==(-1);}
function qg(){return lg(this);}
function rg(){return mg(this);}
function hg(){}
_=hg.prototype=new nF();_.gb=qg;_.kb=rg;_.tN=iL+'CommandExecutor$CircularIterator';_.tI=42;_.a=0;_.b=(-1);_.c=0;function Eg(){Eg=eL;si=sI(new qI());{ki=new xk();Fk(ki);}}
function Fg(a){Eg();tI(si,a);}
function ah(b,a){Eg();sl(ki,b,a);}
function bh(a,b){Eg();return Ck(ki,a,b);}
function ch(){Eg();return ul(ki,'button');}
function dh(){Eg();return ul(ki,'div');}
function eh(a){Eg();return ul(ki,a);}
function fh(){Eg();return ul(ki,'img');}
function gh(){Eg();return vl(ki,'checkbox');}
function hh(){Eg();return ul(ki,'label');}
function ih(){Eg();return ul(ki,'span');}
function jh(){Eg();return ul(ki,'tbody');}
function kh(){Eg();return ul(ki,'td');}
function lh(){Eg();return ul(ki,'tr');}
function mh(){Eg();return ul(ki,'table');}
function ph(b,a,d){Eg();var c;c=w;{oh(b,a,d);}}
function oh(b,a,c){Eg();var d;if(a===ri){if(Ch(b)==8192){ri=null;}}d=nh;nh=b;try{c.mb(b);}finally{nh=d;}}
function qh(b,a){Eg();wl(ki,b,a);}
function rh(a){Eg();return xl(ki,a);}
function sh(a){Eg();return yl(ki,a);}
function th(a){Eg();return zl(ki,a);}
function uh(a){Eg();return Al(ki,a);}
function vh(a){Eg();return Bl(ki,a);}
function wh(a){Eg();return gl(ki,a);}
function xh(a){Eg();return Cl(ki,a);}
function yh(a){Eg();return Dl(ki,a);}
function zh(a){Eg();return El(ki,a);}
function Ah(a){Eg();return hl(ki,a);}
function Bh(a){Eg();return il(ki,a);}
function Ch(a){Eg();return Fl(ki,a);}
function Dh(a){Eg();jl(ki,a);}
function Eh(a){Eg();return zk(ki,a);}
function Fh(a){Eg();return Ak(ki,a);}
function ci(b,a){Eg();return ll(ki,b,a);}
function ai(a){Eg();return kl(ki,a);}
function bi(b,a){Eg();return Dk(ki,b,a);}
function di(a){Eg();return am(ki,a);}
function gi(a,b){Eg();return dm(ki,a,b);}
function ei(a,b){Eg();return bm(ki,a,b);}
function fi(a,b){Eg();return cm(ki,a,b);}
function hi(a){Eg();return em(ki,a);}
function ii(a){Eg();return ml(ki,a);}
function ji(a){Eg();return nl(ki,a);}
function li(c,a,b){Eg();pl(ki,c,a,b);}
function mi(b,a){Eg();return al(ki,b,a);}
function ni(a){Eg();var b,c;c=true;if(si.b>0){b=xb(xI(si,si.b-1),6);if(!(c=b.qb(a))){qh(a,true);Dh(a);}}return c;}
function oi(a){Eg();if(ri!==null&&bh(a,ri)){ri=null;}bl(ki,a);}
function pi(b,a){Eg();fm(ki,b,a);}
function qi(a){Eg();CI(si,a);}
function ti(a){Eg();gm(ki,a);}
function ui(a){Eg();ri=a;ql(ki,a);}
function xi(a,b,c){Eg();jm(ki,a,b,c);}
function vi(a,b,c){Eg();hm(ki,a,b,c);}
function wi(a,b,c){Eg();im(ki,a,b,c);}
function yi(a,b){Eg();km(ki,a,b);}
function zi(a,b){Eg();lm(ki,a,b);}
function Ai(a,b){Eg();mm(ki,a,b);}
function Bi(b,a,c){Eg();nm(ki,b,a,c);}
function Ci(b,a,c){Eg();om(ki,b,a,c);}
function Di(a,b){Eg();dl(ki,a,b);}
function Ei(){Eg();return pm(ki);}
function Fi(){Eg();return qm(ki);}
var nh=null,ki=null,ri=null,si;function bj(){bj=eL;dj=tg(new Ef());}
function cj(a){bj();if(a===null){throw bF(new aF(),'cmd can not be null');}Ag(dj,a);}
var dj;function gj(b,a){if(yb(a,7)){return bh(b,xb(a,7));}return ab(Eb(b,ej),a);}
function hj(a){return gj(this,a);}
function ij(){return bb(Eb(this,ej));}
function ej(){}
_=ej.prototype=new E();_.eQ=hj;_.hC=ij;_.tN=iL+'Element';_.tI=43;function nj(a){return ab(Eb(this,jj),a);}
function oj(){return bb(Eb(this,jj));}
function jj(){}
_=jj.prototype=new E();_.eQ=nj;_.hC=oj;_.tN=iL+'Event';_.tI=44;function sj(){while((wj(),Ej).b>0){vj(xb(xI((wj(),Ej),0),8));}}
function tj(){return null;}
function qj(){}
_=qj.prototype=new nF();_.zb=sj;_.Ab=tj;_.tN=iL+'Timer$1';_.tI=45;function ck(){ck=eL;gk=sI(new qI());tk=sI(new qI());{pk();}}
function dk(a){ck();tI(gk,a);}
function ek(a){ck();tI(tk,a);}
function fk(a){ck();$wnd.alert(a);}
function hk(a){ck();$doc.body.style.overflow=a?'auto':'hidden';}
function ik(){ck();var a,b;for(a=EG(gk);xG(a);){b=xb(yG(a),9);b.zb();}}
function jk(){ck();var a,b,c,d;d=null;for(a=EG(gk);xG(a);){b=xb(yG(a),9);c=b.Ab();{d=c;}}return d;}
function kk(){ck();var a,b;for(a=EG(tk);xG(a);){b=xb(yG(a),10);b.Bb(mk(),lk());}}
function lk(){ck();return Ei();}
function mk(){ck();return Fi();}
function nk(){ck();return $doc.documentElement.scrollLeft||$doc.body.scrollLeft;}
function ok(){ck();return $doc.documentElement.scrollTop||$doc.body.scrollTop;}
function pk(){ck();__gwt_initHandlers(function(){sk();},function(){return rk();},function(){qk();$wnd.onresize=null;$wnd.onbeforeclose=null;$wnd.onclose=null;});}
function qk(){ck();var a;a=w;{ik();}}
function rk(){ck();var a;a=w;{return jk();}}
function sk(){ck();var a;a=w;{kk();}}
function uk(a){ck();$doc.body.style.margin=a;}
var gk,tk;function sl(c,b,a){b.appendChild(a);}
function ul(b,a){return $doc.createElement(a);}
function vl(b,c){var a=$doc.createElement('INPUT');a.type=c;return a;}
function wl(c,b,a){b.cancelBubble=a;}
function xl(b,a){return !(!a.altKey);}
function yl(b,a){return a.clientX|| -1;}
function zl(b,a){return a.clientY|| -1;}
function Al(b,a){return !(!a.ctrlKey);}
function Bl(b,a){return a.currentTarget;}
function Cl(b,a){return a.which||(a.keyCode|| -1);}
function Dl(b,a){return !(!a.metaKey);}
function El(b,a){return !(!a.shiftKey);}
function Fl(b,a){switch(a.type){case 'blur':return 4096;case 'change':return 1024;case 'click':return 1;case 'dblclick':return 2;case 'focus':return 2048;case 'keydown':return 128;case 'keypress':return 256;case 'keyup':return 512;case 'load':return 32768;case 'losecapture':return 8192;case 'mousedown':return 4;case 'mousemove':return 64;case 'mouseout':return 32;case 'mouseover':return 16;case 'mouseup':return 8;case 'scroll':return 16384;case 'error':return 65536;case 'mousewheel':return 131072;case 'DOMMouseScroll':return 131072;}}
function am(c,b){var a=$doc.getElementById(b);return a||null;}
function dm(d,a,b){var c=a[b];return c==null?null:String(c);}
function bm(c,a,b){return !(!a[b]);}
function cm(d,a,c){var b=parseInt(a[c]);if(!b){return 0;}return b;}
function em(b,a){return a.__eventBits||0;}
function fm(c,b,a){b.removeChild(a);}
function gm(g,b){var d=b.offsetLeft,h=b.offsetTop;var i=b.offsetWidth,c=b.offsetHeight;if(b.parentNode!=b.offsetParent){d-=b.parentNode.offsetLeft;h-=b.parentNode.offsetTop;}var a=b.parentNode;while(a&&a.nodeType==1){if(a.style.overflow=='auto'||(a.style.overflow=='scroll'||a.tagName=='BODY')){if(d<a.scrollLeft){a.scrollLeft=d;}if(d+i>a.scrollLeft+a.clientWidth){a.scrollLeft=d+i-a.clientWidth;}if(h<a.scrollTop){a.scrollTop=h;}if(h+c>a.scrollTop+a.clientHeight){a.scrollTop=h+c-a.clientHeight;}}var e=a.offsetLeft,f=a.offsetTop;if(a.parentNode!=a.offsetParent){e-=a.parentNode.offsetLeft;f-=a.parentNode.offsetTop;}d+=e-a.scrollLeft;h+=f-a.scrollTop;a=a.parentNode;}}
function jm(c,a,b,d){a[b]=d;}
function hm(c,a,b,d){a[b]=d;}
function im(c,a,b,d){a[b]=d;}
function km(c,a,b){a.__listener=b;}
function lm(c,a,b){if(!b){b='';}a.innerHTML=b;}
function mm(c,a,b){while(a.firstChild){a.removeChild(a.firstChild);}if(b!=null){a.appendChild($doc.createTextNode(b));}}
function nm(c,b,a,d){b.style[a]=d;}
function om(c,b,a,d){b.style[a]=d;}
function pm(a){return $doc.body.clientHeight;}
function qm(a){return $doc.body.clientWidth;}
function vk(){}
_=vk.prototype=new nF();_.tN=jL+'DOMImpl';_.tI=46;function gl(b,a){return a.relatedTarget?a.relatedTarget:null;}
function hl(b,a){return a.target||null;}
function il(b,a){return a.relatedTarget||null;}
function jl(b,a){a.preventDefault();}
function ll(f,c,d){var b=0,a=c.firstChild;while(a){var e=a.nextSibling;if(a.nodeType==1){if(d==b)return a;++b;}a=e;}return null;}
function kl(d,c){var b=0,a=c.firstChild;while(a){if(a.nodeType==1)++b;a=a.nextSibling;}return b;}
function ml(c,b){var a=b.firstChild;while(a&&a.nodeType!=1)a=a.nextSibling;return a||null;}
function nl(c,a){var b=a.parentNode;if(b==null){return null;}if(b.nodeType!=1)b=null;return b||null;}
function ol(d){$wnd.__dispatchCapturedMouseEvent=function(b){if($wnd.__dispatchCapturedEvent(b)){var a=$wnd.__captureElem;if(a&&a.__listener){ph(b,a,a.__listener);b.stopPropagation();}}};$wnd.__dispatchCapturedEvent=function(a){if(!ni(a)){a.stopPropagation();a.preventDefault();return false;}return true;};$wnd.addEventListener('click',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('dblclick',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousedown',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mouseup',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousemove',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousewheel',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('keydown',$wnd.__dispatchCapturedEvent,true);$wnd.addEventListener('keyup',$wnd.__dispatchCapturedEvent,true);$wnd.addEventListener('keypress',$wnd.__dispatchCapturedEvent,true);$wnd.__dispatchEvent=function(b){var c,a=this;while(a&& !(c=a.__listener))a=a.parentNode;if(a&&a.nodeType!=1)a=null;if(c)ph(b,a,c);};$wnd.__captureElem=null;}
function pl(f,e,g,d){var c=0,b=e.firstChild,a=null;while(b){if(b.nodeType==1){if(c==d){a=b;break;}++c;}b=b.nextSibling;}e.insertBefore(g,a);}
function ql(b,a){$wnd.__captureElem=a;}
function rl(c,b,a){b.__eventBits=a;b.onclick=a&1?$wnd.__dispatchEvent:null;b.ondblclick=a&2?$wnd.__dispatchEvent:null;b.onmousedown=a&4?$wnd.__dispatchEvent:null;b.onmouseup=a&8?$wnd.__dispatchEvent:null;b.onmouseover=a&16?$wnd.__dispatchEvent:null;b.onmouseout=a&32?$wnd.__dispatchEvent:null;b.onmousemove=a&64?$wnd.__dispatchEvent:null;b.onkeydown=a&128?$wnd.__dispatchEvent:null;b.onkeypress=a&256?$wnd.__dispatchEvent:null;b.onkeyup=a&512?$wnd.__dispatchEvent:null;b.onchange=a&1024?$wnd.__dispatchEvent:null;b.onfocus=a&2048?$wnd.__dispatchEvent:null;b.onblur=a&4096?$wnd.__dispatchEvent:null;b.onlosecapture=a&8192?$wnd.__dispatchEvent:null;b.onscroll=a&16384?$wnd.__dispatchEvent:null;b.onload=a&32768?$wnd.__dispatchEvent:null;b.onerror=a&65536?$wnd.__dispatchEvent:null;b.onmousewheel=a&131072?$wnd.__dispatchEvent:null;}
function el(){}
_=el.prototype=new vk();_.tN=jL+'DOMImplStandard';_.tI=47;function Ck(c,a,b){if(!a&& !b){return true;}else if(!a|| !b){return false;}return a.isSameNode(b);}
function Dk(d,c,e){var b=0,a=c.firstChild;while(a){if(a.isSameNode(e)){return b;}if(a.nodeType==1){++b;}a=a.nextSibling;}return -1;}
function Fk(a){ol(a);Ek(a);}
function Ek(d){$wnd.addEventListener('mouseout',function(b){var a=$wnd.__captureElem;if(a&& !b.relatedTarget){if('html'==b.target.tagName.toLowerCase()){var c=$doc.createEvent('MouseEvents');c.initMouseEvent('mouseup',true,true,$wnd,0,b.screenX,b.screenY,b.clientX,b.clientY,b.ctrlKey,b.altKey,b.shiftKey,b.metaKey,b.button,null);a.dispatchEvent(c);}}},true);$wnd.addEventListener('DOMMouseScroll',$wnd.__dispatchCapturedMouseEvent,true);}
function al(d,c,b){while(b){if(c.isSameNode(b)){return true;}try{b=b.parentNode;}catch(a){return false;}if(b&&b.nodeType!=1){b=null;}}return false;}
function bl(b,a){if(a.isSameNode($wnd.__captureElem)){$wnd.__captureElem=null;}}
function dl(c,b,a){rl(c,b,a);cl(c,b,a);}
function cl(c,b,a){if(a&131072){b.addEventListener('DOMMouseScroll',$wnd.__dispatchEvent,false);}}
function wk(){}
_=wk.prototype=new el();_.tN=jL+'DOMImplMozilla';_.tI=48;function zk(e,a){var d=$doc.defaultView.getComputedStyle(a,null);var b=$doc.getBoxObjectFor(a).x-Math.round(d.getPropertyCSSValue('border-left-width').getFloatValue(CSSPrimitiveValue.CSS_PX));var c=a.parentNode;while(c){if(c.scrollLeft>0){b-=c.scrollLeft;}c=c.parentNode;}return b+$doc.body.scrollLeft+$doc.documentElement.scrollLeft;}
function Ak(d,a){var c=$doc.defaultView.getComputedStyle(a,null);var e=$doc.getBoxObjectFor(a).y-Math.round(c.getPropertyCSSValue('border-top-width').getFloatValue(CSSPrimitiveValue.CSS_PX));var b=a.parentNode;while(b){if(b.scrollTop>0){e-=b.scrollTop;}b=b.parentNode;}return e+$doc.body.scrollTop+$doc.documentElement.scrollTop;}
function xk(){}
_=xk.prototype=new wk();_.tN=jL+'DOMImplMozillaOld';_.tI=49;function sm(a){ao(a);a.bc(dh());Ci(a.ab(),'position','relative');Ci(a.ab(),'overflow','hidden');return a;}
function tm(a,b){bo(a,b,a.ab());}
function vm(b,c){var a;a=jo(b,c);if(a){wm(c.ab());}return a;}
function wm(a){Ci(a,'left','');Ci(a,'top','');Ci(a,'position','');}
function xm(a){return vm(this,a);}
function rm(){}
_=rm.prototype=new En();_.Eb=xm;_.tN=kL+'AbsolutePanel';_.tI=50;function ym(){}
_=ym.prototype=new nF();_.tN=kL+'AbstractImagePrototype';_.tI=51;function qq(){qq=eL;nD(),pD;}
function oq(b,a){nD(),pD;rq(b,a);return b;}
function pq(b,a){if(b.c===null){b.c=An(new zn());}tI(b.c,a);}
function rq(b,a){iC(b,a);uA(b,7041);}
function sq(a){switch(Ch(a)){case 1:if(this.c!==null){Cn(this.c,this);}break;case 4096:case 2048:break;case 128:case 512:case 256:break;}}
function tq(a){rq(this,a);}
function nq(){}
_=nq.prototype=new hB();_.mb=sq;_.bc=tq;_.tN=kL+'FocusWidget';_.tI=52;_.c=null;function Dm(){Dm=eL;nD(),pD;}
function Cm(b,a){nD(),pD;oq(b,a);return b;}
function Em(a){zi(this.ab(),a);}
function Bm(){}
_=Bm.prototype=new nq();_.cc=Em;_.tN=kL+'ButtonBase';_.tI=53;function cn(){cn=eL;nD(),pD;}
function Fm(a){nD(),pD;Cm(a,ch());dn(a.ab());tA(a,'gwt-Button');return a;}
function an(b,a){nD(),pD;Fm(b);b.cc(a);return b;}
function bn(c,a,b){nD(),pD;an(c,a);pq(c,b);return c;}
function dn(b){cn();if(b.type=='submit'){try{b.setAttribute('type','button');}catch(a){}}}
function Am(){}
_=Am.prototype=new Bm();_.tN=kL+'Button';_.tI=54;function fn(a){ao(a);a.e=mh();a.d=jh();ah(a.e,a.d);a.bc(a.e);return a;}
function hn(a,b){if(b.q!==a){return null;}return ji(b.ab());}
function jn(c,b,a){xi(b,'align',a.a);}
function kn(c,b,a){Ci(b,'verticalAlign',a.a);}
function ln(b,a){wi(b.e,'cellSpacing',a);}
function mn(c,a){var b;b=hn(this,c);if(b!==null){jn(this,b,a);}}
function en(){}
_=en.prototype=new En();_.ac=mn;_.tN=kL+'CellPanel';_.tI=55;_.d=null;_.e=null;function rn(){rn=eL;nD(),pD;}
function on(a){nD(),pD;pn(a,gh());tA(a,'gwt-CheckBox');return a;}
function qn(b,a){nD(),pD;on(b);un(b,a);return b;}
function pn(b,a){var c;nD(),pD;Cm(b,ih());b.a=a;b.b=hh();Di(b.a,hi(b.ab()));Di(b.ab(),0);ah(b.ab(),b.a);ah(b.ab(),b.b);c='check'+ ++yn;xi(b.a,'id',c);xi(b.b,'htmlFor',c);return b;}
function sn(b){var a;a=b.hb()?'checked':'defaultChecked';return ei(b.a,a);}
function tn(b,a){vi(b.a,'checked',a);vi(b.a,'defaultChecked',a);}
function un(b,a){Ai(b.b,a);}
function vn(){yi(this.a,this);}
function wn(){yi(this.a,null);tn(this,sn(this));}
function xn(a){zi(this.b,a);}
function nn(){}
_=nn.prototype=new Bm();_.sb=vn;_.yb=wn;_.cc=xn;_.tN=kL+'CheckBox';_.tI=56;_.a=null;_.b=null;var yn=0;function oG(d,a,b){var c;while(a.gb()){c=a.kb();if(b===null?c===null:b.eQ(c)){return a;}}return null;}
function qG(a){throw lG(new kG(),'add');}
function rG(b){var a;a=oG(this,this.ib(),b);return a!==null;}
function sG(a){var b,c,d;d=this.ic();if(a.a<d){a=mb(a,d);}b=0;for(c=this.ib();c.gb();){tb(a,b++,c.kb());}if(a.a>d){tb(a,d,null);}return a;}
function nG(){}
_=nG.prototype=new nF();_.u=qG;_.w=rG;_.jc=sG;_.tN=nL+'AbstractCollection';_.tI=57;function DG(b,a){throw sE(new rE(),'Index: '+a+', Size: '+b.b);}
function EG(a){return vG(new uG(),a);}
function FG(b,a){throw lG(new kG(),'add');}
function aH(a){this.t(this.ic(),a);return true;}
function bH(e){var a,b,c,d,f;if(e===this){return true;}if(!yb(e,25)){return false;}f=xb(e,25);if(this.ic()!=f.ic()){return false;}c=EG(this);d=f.ib();while(xG(c)){a=yG(c);b=yG(d);if(!(a===null?b===null:a.eQ(b))){return false;}}return true;}
function cH(){var a,b,c,d;c=1;a=31;b=EG(this);while(xG(b)){d=yG(b);c=31*c+(d===null?0:d.hC());}return c;}
function dH(){return EG(this);}
function eH(a){throw lG(new kG(),'remove');}
function tG(){}
_=tG.prototype=new nG();_.t=FG;_.u=aH;_.eQ=bH;_.hC=cH;_.ib=dH;_.Db=eH;_.tN=nL+'AbstractList';_.tI=58;function rI(a){{uI(a);}}
function sI(a){rI(a);return a;}
function tI(b,a){hJ(b.a,b.b++,a);return true;}
function uI(a){a.a=cb();a.b=0;}
function wI(b,a){return yI(b,a)!=(-1);}
function xI(b,a){if(a<0||a>=b.b){DG(b,a);}return dJ(b.a,a);}
function yI(b,a){return zI(b,a,0);}
function zI(c,b,a){if(a<0){DG(c,a);}for(;a<c.b;++a){if(cJ(b,dJ(c.a,a))){return a;}}return (-1);}
function AI(a){return a.b==0;}
function BI(c,a){var b;b=xI(c,a);fJ(c.a,a,1);--c.b;return b;}
function CI(c,b){var a;a=yI(c,b);if(a==(-1)){return false;}BI(c,a);return true;}
function DI(d,a,b){var c;c=xI(d,a);hJ(d.a,a,b);return c;}
function FI(a,b){if(a<0||a>this.b){DG(this,a);}EI(this.a,a,b);++this.b;}
function aJ(a){return tI(this,a);}
function EI(a,b,c){a.splice(b,0,c);}
function bJ(a){return wI(this,a);}
function cJ(a,b){return a===b||a!==null&&a.eQ(b);}
function eJ(a){return xI(this,a);}
function dJ(a,b){return a[b];}
function gJ(a){return BI(this,a);}
function fJ(a,c,b){a.splice(c,b);}
function hJ(a,b,c){a[b]=c;}
function iJ(){return this.b;}
function jJ(a){var b;if(a.a<this.b){a=mb(a,this.b);}for(b=0;b<this.b;++b){tb(a,b,dJ(this.a,b));}if(a.a>this.b){tb(a,this.b,null);}return a;}
function qI(){}
_=qI.prototype=new tG();_.t=FI;_.u=aJ;_.w=bJ;_.eb=eJ;_.Db=gJ;_.ic=iJ;_.jc=jJ;_.tN=nL+'ArrayList';_.tI=59;_.a=null;_.b=0;function An(a){sI(a);return a;}
function Cn(d,c){var a,b;for(a=EG(d);xG(a);){b=xb(yG(a),11);b.ob(c);}}
function zn(){}
_=zn.prototype=new qI();_.tN=kL+'ClickListenerCollection';_.tI=60;function qp(){qp=eL;wp=new gp();xp=new gp();yp=new gp();zp=new gp();Ap=new gp();}
function np(a){a.b=(lt(),nt);a.c=(ut(),wt);}
function op(a){qp();fn(a);np(a);wi(a.e,'cellSpacing',0);wi(a.e,'cellPadding',0);return a;}
function pp(c,d,a){var b;if(a===wp){if(d===c.a){return;}else if(c.a!==null){throw mE(new lE(),'Only one CENTER widget may be added');}}hC(d);rB(c.f,d);if(a===wp){c.a=d;}b=jp(new ip(),a);jC(d,b);tp(c,d,c.b);up(c,d,c.c);rp(c);qv(c,d);}
function rp(p){var a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,q;a=p.d;while(ai(a)>0){pi(a,ci(a,0));}l=1;d=1;for(h=wB(p.f);mB(h);){c=nB(h);e=c.p.a;if(e===yp||e===zp){++l;}else if(e===xp||e===Ap){++d;}}m=rb('[Lcom.google.gwt.user.client.ui.DockPanel$TmpRow;',[127],[21],[l],null);for(g=0;g<l;++g){m[g]=new lp();m[g].b=lh();ah(a,m[g].b);}q=0;f=d-1;j=0;n=l-1;b=null;for(h=wB(p.f);mB(h);){c=nB(h);i=c.p;o=kh();i.d=o;xi(i.d,'align',i.b);Ci(i.d,'verticalAlign',i.e);xi(i.d,'width',i.f);xi(i.d,'height',i.c);if(i.a===yp){li(m[j].b,o,m[j].a);ah(o,c.ab());wi(o,'colSpan',f-q+1);++j;}else if(i.a===zp){li(m[n].b,o,m[n].a);ah(o,c.ab());wi(o,'colSpan',f-q+1);--n;}else if(i.a===Ap){k=m[j];li(k.b,o,k.a++);ah(o,c.ab());wi(o,'rowSpan',n-j+1);++q;}else if(i.a===xp){k=m[j];li(k.b,o,k.a);ah(o,c.ab());wi(o,'rowSpan',n-j+1);--f;}else if(i.a===wp){b=o;}}if(p.a!==null){k=m[j];li(k.b,b,k.a);ah(b,p.a.ab());}}
function sp(c,d,b){var a;a=d.p;a.c=b;if(a.d!==null){Ci(a.d,'height',a.c);}}
function tp(c,d,a){var b;b=d.p;b.b=a.a;if(b.d!==null){xi(b.d,'align',b.b);}}
function up(c,d,a){var b;b=d.p;b.e=a.a;if(b.d!==null){Ci(b.d,'verticalAlign',b.e);}}
function vp(b,c,d){var a;a=c.p;a.f=d;if(a.d!==null){Ci(a.d,'width',a.f);}}
function Bp(b){var a;a=jo(this,b);if(a){if(b===this.a){this.a=null;}rp(this);}return a;}
function Cp(b,a){tp(this,b,a);}
function fp(){}
_=fp.prototype=new en();_.Eb=Bp;_.ac=Cp;_.tN=kL+'DockPanel';_.tI=61;_.a=null;var wp,xp,yp,zp,Ap;function gp(){}
_=gp.prototype=new nF();_.tN=kL+'DockPanel$DockLayoutConstant';_.tI=62;function jp(b,a){b.a=a;return b;}
function ip(){}
_=ip.prototype=new nF();_.tN=kL+'DockPanel$LayoutData';_.tI=63;_.a=null;_.b='left';_.c='';_.d=null;_.e='top';_.f='';function lp(){}
_=lp.prototype=new nF();_.tN=kL+'DockPanel$TmpRow';_.tI=64;_.a=0;_.b=null;function gs(a){a.g=Cr(new xr());}
function hs(a){gs(a);a.e=mh();a.a=jh();ah(a.e,a.a);a.bc(a.e);uA(a,1);return a;}
function is(b,a){if(b.f===null){b.f=py(new oy());}tI(b.f,a);}
function js(d,c,b){var a;ks(d,c);if(b<0){throw sE(new rE(),'Column '+b+' must be non-negative: '+b);}a=eq(d,c);if(a<=b){throw sE(new rE(),'Column index: '+b+', Column size: '+eq(d,c));}}
function ks(c,a){var b;b=fq(c);if(a>=b||a<0){throw sE(new rE(),'Row index: '+a+', Row size: '+b);}}
function ls(e,c,b,a){var d;d=er(e.b,c,b);ss(e,d,a);return d;}
function ns(c,b,a){return b.rows[a].cells.length;}
function os(a){return ps(a,a.a);}
function ps(b,a){return a.rows.length;}
function qs(d,b){var a,c,e;c=Ah(b);for(;c!==null;c=ji(c)){if(xF(gi(c,'tagName'),'td')){e=ji(c);a=ji(e);if(bh(a,d.a)){return c;}}if(bh(c,d.a)){return null;}}return null;}
function rs(b,a){var c;if(a!=fq(b)){ks(b,a);}c=lh();li(b.a,c,a);return a;}
function ss(d,c,a){var b,e;b=ii(c);e=null;if(b!==null){e=Er(d.g,b);}if(e!==null){ts(d,e);return true;}else{if(a){zi(c,'');}return false;}}
function ts(b,c){var a;if(c.q!==b){return false;}sv(b,c);a=c.ab();pi(ji(a),a);bs(b.g,a);return true;}
function us(a,b){xi(a.e,'border',''+b);}
function vs(b,a){b.b=a;}
function ws(b,a){wi(b.e,'cellPadding',a);}
function xs(b,a){wi(b.e,'cellSpacing',a);}
function ys(b,a){b.c=a;or(b.c);}
function zs(e,c,a,b){var d;hq(e,c,a);d=ls(e,c,a,b===null);if(b!==null){zi(d,b);}}
function As(b,a){b.d=a;}
function Bs(e,b,a,d){var c;hq(e,b,a);c=ls(e,b,a,d===null);if(d!==null){Ai(c,d);}}
function Cs(d,b,a,e){var c;hq(d,b,a);if(e!==null){hC(e);c=ls(d,b,a,true);Fr(d.g,e);ah(c,e.ab());qv(d,e);}}
function Ds(){return cs(this.g);}
function Es(c){var a,b,d,e,f;switch(Ch(c)){case 1:{if(this.f!==null){e=qs(this,c);if(e===null){return;}f=ji(e);a=ji(f);d=bi(a,f);b=bi(f,e);ry(this.f,this,d,b);}break;}default:}}
function Fs(a){return ts(this,a);}
function vq(){}
_=vq.prototype=new pv();_.ib=Ds;_.mb=Es;_.Eb=Fs;_.tN=kL+'HTMLTable';_.tI=65;_.a=null;_.b=null;_.c=null;_.d=null;_.e=null;_.f=null;function cq(a){hs(a);vs(a,Fp(new Ep(),a));As(a,qr(new pr(),a));ys(a,mr(new lr(),a));return a;}
function eq(b,a){ks(b,a);return ns(b,b.a,a);}
function fq(a){return os(a);}
function gq(b,a){return rs(b,a);}
function hq(e,d,b){var a,c;iq(e,d);if(b<0){throw sE(new rE(),'Cannot create a column with a negative index: '+b);}a=eq(e,d);c=b+1-a;if(c>0){jq(e.a,d,c);}}
function iq(d,b){var a,c;if(b<0){throw sE(new rE(),'Cannot create a row with a negative index: '+b);}c=fq(d);for(a=c;a<=b;a++){gq(d,a);}}
function jq(f,d,c){var e=f.rows[d];for(var b=0;b<c;b++){var a=$doc.createElement('td');e.appendChild(a);}}
function Dp(){}
_=Dp.prototype=new vq();_.tN=kL+'FlexTable';_.tI=66;function Fq(b,a){b.a=a;return b;}
function br(c,b,a){hq(c.a,b,a);return cr(c,c.a.a,b,a);}
function cr(e,d,c,a){var b=d.rows[c].cells[a];return b==null?null:b;}
function dr(c,b,a){js(c.a,b,a);return cr(c,c.a.a,b,a);}
function er(c,b,a){return cr(c,c.a.a,b,a);}
function fr(d,c,a,b,e){hr(d,c,a,b);ir(d,c,a,e);}
function gr(e,d,a,c){var b;hq(e.a,d,a);b=cr(e,e.a.a,d,a);xi(b,'height',c);}
function hr(e,d,b,a){var c;hq(e.a,d,b);c=cr(e,e.a.a,d,b);xi(c,'align',a.a);}
function ir(d,c,b,a){hq(d.a,c,b);Ci(cr(d,d.a.a,c,b),'verticalAlign',a.a);}
function jr(c,b,a,d){hq(c.a,b,a);xi(cr(c,c.a.a,b,a),'width',d);}
function kr(c,b,a,d){var e;hq(c.a,b,a);e=d?'':'nowrap';Ci(dr(c,b,a),'whiteSpace',e);}
function Eq(){}
_=Eq.prototype=new nF();_.tN=kL+'HTMLTable$CellFormatter';_.tI=67;function Fp(b,a){Fq(b,a);return b;}
function bq(d,c,b,a){wi(br(d,c,b),'colSpan',a);}
function Ep(){}
_=Ep.prototype=new Eq();_.tN=kL+'FlexTable$FlexCellFormatter';_.tI=68;function lq(){lq=eL;mq=(nD(),oD);}
var mq;function Du(a){a.bc(dh());uA(a,131197);tA(a,'gwt-Label');return a;}
function Eu(b,a){Du(b);cv(b,a);return b;}
function Fu(b,a){if(b.a===null){b.a=An(new zn());}tI(b.a,a);}
function av(b,a){if(b.b===null){b.b=gv(new fv());}tI(b.b,a);}
function cv(b,a){Ai(b.ab(),a);}
function dv(a,b){Ci(a.ab(),'whiteSpace',b?'normal':'nowrap');}
function ev(a){switch(Ch(a)){case 1:if(this.a!==null){Cn(this.a,this);}break;case 4:case 8:case 64:case 16:case 32:if(this.b!==null){kv(this.b,this,a);}break;case 131072:break;}}
function Cu(){}
_=Cu.prototype=new hB();_.mb=ev;_.tN=kL+'Label';_.tI=69;_.a=null;_.b=null;function at(a){Du(a);a.bc(dh());uA(a,125);tA(a,'gwt-HTML');return a;}
function bt(b,a){at(b);et(b,a);return b;}
function ct(b,a,c){bt(b,a);dv(b,c);return b;}
function et(b,a){zi(b.ab(),a);}
function uq(){}
_=uq.prototype=new Cu();_.tN=kL+'HTML';_.tI=70;function xq(a){{Aq(a);}}
function yq(b,a){b.b=a;xq(b);return b;}
function Aq(a){while(++a.a<a.b.b.b){if(xI(a.b.b,a.a)!==null){return;}}}
function Bq(a){return a.a<a.b.b.b;}
function Cq(){return Bq(this);}
function Dq(){var a;if(!Bq(this)){throw new aL();}a=xI(this.b.b,this.a);Aq(this);return a;}
function wq(){}
_=wq.prototype=new nF();_.gb=Cq;_.kb=Dq;_.tN=kL+'HTMLTable$1';_.tI=71;_.a=(-1);function mr(b,a){b.b=a;return b;}
function or(a){if(a.a===null){a.a=eh('colgroup');li(a.b.e,a.a,0);ah(a.a,eh('col'));}}
function lr(){}
_=lr.prototype=new nF();_.tN=kL+'HTMLTable$ColumnFormatter';_.tI=72;_.a=null;function qr(b,a){b.a=a;return b;}
function rr(c,a,b){BA(tr(c,a),b,true);}
function tr(b,a){iq(b.a,a);return ur(b,b.a.a,a);}
function ur(c,a,b){return a.rows[b];}
function vr(c,a,b){BA(tr(c,a),b,false);}
function wr(c,a,b){AA(tr(c,a),b);}
function pr(){}
_=pr.prototype=new nF();_.tN=kL+'HTMLTable$RowFormatter';_.tI=73;function Br(a){a.b=sI(new qI());}
function Cr(a){Br(a);return a;}
function Er(c,a){var b;b=es(a);if(b<0){return null;}return xb(xI(c.b,b),12);}
function Fr(b,c){var a;if(b.a===null){a=b.b.b;tI(b.b,c);}else{a=b.a.a;DI(b.b,a,c);b.a=b.a.b;}fs(c.ab(),a);}
function as(c,a,b){ds(a);DI(c.b,b,null);c.a=zr(new yr(),b,c.a);}
function bs(c,a){var b;b=es(a);as(c,a,b);}
function cs(a){return yq(new wq(),a);}
function ds(a){a['__widgetID']=null;}
function es(a){var b=a['__widgetID'];return b==null?-1:b;}
function fs(a,b){a['__widgetID']=b;}
function xr(){}
_=xr.prototype=new nF();_.tN=kL+'HTMLTable$WidgetMapper';_.tI=74;_.a=null;function zr(c,a,b){c.a=a;c.b=b;return c;}
function yr(){}
_=yr.prototype=new nF();_.tN=kL+'HTMLTable$WidgetMapper$FreeNode';_.tI=75;_.a=0;_.b=null;function lt(){lt=eL;mt=jt(new it(),'center');nt=jt(new it(),'left');ot=jt(new it(),'right');}
var mt,nt,ot;function jt(b,a){b.a=a;return b;}
function it(){}
_=it.prototype=new nF();_.tN=kL+'HasHorizontalAlignment$HorizontalAlignmentConstant';_.tI=76;_.a=null;function ut(){ut=eL;st(new rt(),'bottom');vt=st(new rt(),'middle');wt=st(new rt(),'top');}
var vt,wt;function st(a,b){a.a=b;return a;}
function rt(){}
_=rt.prototype=new nF();_.tN=kL+'HasVerticalAlignment$VerticalAlignmentConstant';_.tI=77;_.a=null;function At(a){a.a=(lt(),nt);a.c=(ut(),wt);}
function Bt(a){fn(a);At(a);a.b=lh();ah(a.d,a.b);xi(a.e,'cellSpacing','0');xi(a.e,'cellPadding','0');return a;}
function Ct(b,c){var a;a=Et(b);ah(b.b,a);bo(b,c,a);}
function Et(b){var a;a=kh();jn(b,a,b.a);kn(b,a,b.c);return a;}
function Ft(b,a){b.a=a;}
function au(c){var a,b;b=ji(c.ab());a=jo(this,c);if(a){pi(this.b,b);}return a;}
function zt(){}
_=zt.prototype=new en();_.Eb=au;_.tN=kL+'HorizontalPanel';_.tI=78;_.b=null;function vu(){vu=eL;fK(new mJ());}
function su(a){vu();uu(a,ou(new nu(),a));tA(a,'gwt-Image');return a;}
function tu(c,e,b,d,f,a){vu();uu(c,gu(new fu(),c,e,b,d,f,a));tA(c,'gwt-Image');return c;}
function uu(b,a){b.a=a;}
function wu(c,e,b,d,f,a){c.a.ec(c,e,b,d,f,a);}
function xu(a){switch(Ch(a)){case 1:{break;}case 4:case 8:case 64:case 16:case 32:{break;}case 131072:break;case 32768:{break;}case 65536:{break;}}}
function bu(){}
_=bu.prototype=new hB();_.mb=xu;_.tN=kL+'Image';_.tI=79;_.a=null;function eu(){}
function cu(){}
_=cu.prototype=new nF();_.D=eu;_.tN=kL+'Image$1';_.tI=80;function lu(){}
_=lu.prototype=new nF();_.tN=kL+'Image$State';_.tI=81;function hu(){hu=eL;ju=new uC();}
function gu(d,b,f,c,e,g,a){hu();d.b=c;d.c=e;d.e=g;d.a=a;d.d=f;b.bc(xC(ju,f,c,e,g,a));uA(b,131197);iu(d,b);return d;}
function iu(b,a){cj(new cu());}
function ku(b,e,c,d,f,a){if(!yF(this.d,e)||this.b!=c||this.c!=d||this.e!=f||this.a!=a){this.d=e;this.b=c;this.c=d;this.e=f;this.a=a;vC(ju,b.ab(),e,c,d,f,a);iu(this,b);}}
function fu(){}
_=fu.prototype=new lu();_.ec=ku;_.tN=kL+'Image$ClippedState';_.tI=82;_.a=0;_.b=0;_.c=0;_.d=null;_.e=0;var ju;function ou(b,a){a.bc(fh());uA(a,229501);return b;}
function qu(b,e,c,d,f,a){uu(b,gu(new fu(),b,e,c,d,f,a));}
function nu(){}
_=nu.prototype=new lu();_.ec=qu;_.tN=kL+'Image$UnclippedState';_.tI=83;function Bu(a){return (zh(a)?1:0)|(yh(a)?8:0)|(uh(a)?2:0)|(rh(a)?4:0);}
function gv(a){sI(a);return a;}
function iv(d,c,e,f){var a,b;for(a=EG(d);xG(a);){b=xb(yG(a),13);b.tb(c,e,f);}}
function jv(d,c){var a,b;for(a=EG(d);xG(a);){b=xb(yG(a),13);b.ub(c);}}
function kv(e,c,a){var b,d,f,g,h;d=c.ab();g=sh(a)-Eh(d)+fi(d,'scrollLeft')+nk();h=th(a)-Fh(d)+fi(d,'scrollTop')+ok();switch(Ch(a)){case 4:iv(e,c,g,h);break;case 8:nv(e,c,g,h);break;case 64:mv(e,c,g,h);break;case 16:b=wh(a);if(!mi(d,b)){jv(e,c);}break;case 32:f=Bh(a);if(!mi(d,f)){lv(e,c);}break;}}
function lv(d,c){var a,b;for(a=EG(d);xG(a);){b=xb(yG(a),13);b.vb(c);}}
function mv(d,c,e,f){var a,b;for(a=EG(d);xG(a);){b=xb(yG(a),13);b.wb(c,e,f);}}
function nv(d,c,e,f){var a,b;for(a=EG(d);xG(a);){b=xb(yG(a),13);b.xb(c,e,f);}}
function fv(){}
_=fv.prototype=new qI();_.tN=kL+'MouseListenerCollection';_.tI=84;function zw(){zw=eL;Ew=fK(new mJ());}
function yw(b,a){zw();sm(b);if(a===null){a=Aw();}b.bc(a);b.lb();return b;}
function Bw(){zw();return Cw(null);}
function Cw(c){zw();var a,b;b=xb(lK(Ew,c),14);if(b!==null){return b;}a=null;if(Ew.c==0){Dw();}mK(Ew,c,b=yw(new tw(),a));return b;}
function Aw(){zw();return $doc.body;}
function Dw(){zw();dk(new uw());}
function tw(){}
_=tw.prototype=new rm();_.tN=kL+'RootPanel';_.tI=85;var Ew;function ww(){var a,b;for(b=xH(fI((zw(),Ew)));EH(b);){a=xb(FH(b),14);if(a.hb()){a.pb();}}}
function xw(){return null;}
function uw(){}
_=uw.prototype=new nF();_.zb=ww;_.Ab=xw;_.tN=kL+'RootPanel$1';_.tI=86;function ax(a){mx(a);dx(a,false);uA(a,16384);return a;}
function bx(b,a){ax(b);b.gc(a);return b;}
function dx(b,a){Ci(b.ab(),'overflow',a?'scroll':'auto');}
function ex(a){Ch(a)==16384;}
function Fw(){}
_=Fw.prototype=new fx();_.mb=ex;_.tN=kL+'ScrollPanel';_.tI=87;function hx(a){a.a=a.b.n!==null;}
function ix(b,a){b.b=a;hx(b);return b;}
function kx(){return this.a;}
function lx(){if(!this.a||this.b.n===null){throw new aL();}this.a=false;return this.b.n;}
function gx(){}
_=gx.prototype=new nF();_.gb=kx;_.kb=lx;_.tN=kL+'SimplePanel$1';_.tI=88;function py(a){sI(a);return a;}
function ry(f,e,d,a){var b,c;for(b=EG(f);xG(b);){c=xb(yG(b),15);c.nb(e,d,a);}}
function oy(){}
_=oy.prototype=new qI();_.tN=kL+'TableListenerCollection';_.tI=89;function rz(a){a.a=fK(new mJ());}
function sz(b,a){rz(b);b.d=a;b.bc(dh());Ci(b.ab(),'position','relative');b.c=iD((lq(),mq));Ci(b.c,'fontSize','0');Ci(b.c,'position','absolute');Bi(b.c,'zIndex',(-1));ah(b.ab(),b.c);uA(b,1021);Di(b.c,6144);b.f=vy(new uy(),b);lz(b.f,b);tA(b,'gwt-Tree');return b;}
function tz(b,a){wy(b.f,a);}
function vz(d,a,c,b){if(b===null||bh(b,c)){return;}vz(d,a,c,ji(b));tI(a,Eb(b,ej));}
function wz(e,d,b){var a,c;a=sI(new qI());vz(e,a,e.ab(),b);c=yz(e,a,0,d);if(c!==null){if(mi(ez(c),b)){kz(c,!c.f,true);return true;}else if(mi(c.ab(),b)){Dz(e,c,true,!cA(e,b));return true;}}return false;}
function xz(b,a){if(!a.f){return a;}return xz(b,cz(a,a.c.b-1));}
function yz(i,a,e,h){var b,c,d,f,g;if(e==a.b){return h;}c=xb(xI(a,e),7);for(d=0,f=h.c.b;d<f;++d){b=cz(h,d);if(bh(b.ab(),c)){g=yz(i,a,e+1,cz(h,d));if(g===null){return b;}return g;}}return yz(i,a,e+1,h);}
function zz(a){var b;b=rb('[Lcom.google.gwt.user.client.ui.Widget;',[130],[12],[a.a.c],null);eI(a.a).jc(b);return eC(a,b);}
function Az(h,g){var a,b,c,d,e,f,i,j;c=dz(g);{f=g.d;a=mA(h);b=nA(h);e=Eh(f)-a;i=Fh(f)-b;j=fi(f,'offsetWidth');d=fi(f,'offsetHeight');Bi(h.c,'left',e);Bi(h.c,'top',i);Bi(h.c,'width',j);Bi(h.c,'height',d);ti(h.c);kD((lq(),mq),h.c);}}
function Bz(e,d,a){var b,c;if(d===e.f){return;}c=d.g;if(c===null){c=e.f;}b=bz(c,d);if(!a|| !d.f){if(b<c.c.b-1){Dz(e,cz(c,b+1),true,true);}else{Bz(e,c,false);}}else if(d.c.b>0){Dz(e,cz(d,0),true,true);}}
function Cz(e,c){var a,b,d;b=c.g;if(b===null){b=e.f;}a=bz(b,c);if(a>0){d=cz(b,a-1);Dz(e,xz(e,d),true,true);}else{Dz(e,b,true,true);}}
function Dz(d,b,a,c){if(b===d.f){return;}if(d.b!==null){iz(d.b,false);}d.b=b;if(c&&d.b!==null){Az(d,d.b);iz(d.b,true);}}
function Ez(b,a){yy(b.f,a);}
function Fz(b,a){if(a){kD((lq(),mq),b.c);}else{eD((lq(),mq),b.c);}}
function aA(b,a){bA(b,a,true);}
function bA(c,b,a){if(b===null){if(c.b===null){return;}iz(c.b,false);c.b=null;return;}Dz(c,b,a,true);}
function cA(c,a){var b=a.nodeName;return b=='SELECT'||(b=='INPUT'||(b=='TEXTAREA'||(b=='OPTION'||(b=='BUTTON'||b=='LABEL'))));}
function dA(){var a,b;for(b=zz(this);FB(b);){a=aC(b);a.lb();}yi(this.c,this);}
function eA(){var a,b;for(b=zz(this);FB(b);){a=aC(b);a.pb();}yi(this.c,null);}
function fA(){return zz(this);}
function gA(c){var a,b,d,e,f;d=Ch(c);switch(d){case 1:{b=Ah(c);if(cA(this,b)){}else{Fz(this,true);}break;}case 4:{if(gj(vh(c),Eb(this.ab(),ej))){wz(this,this.f,Ah(c));}break;}case 8:{break;}case 64:{break;}case 16:{break;}case 32:{break;}case 2048:break;case 4096:{break;}case 128:if(this.b===null){if(this.f.c.b>0){Dz(this,cz(this.f,0),true,true);}return;}if(this.e==128){return;}{switch(xh(c)){case 38:{Cz(this,this.b);Dh(c);break;}case 40:{Bz(this,this.b,true);Dh(c);break;}case 37:{if(this.b.f){jz(this.b,false);}else{f=this.b.g;if(f!==null){aA(this,f);}}Dh(c);break;}case 39:{if(!this.b.f){jz(this.b,true);}else if(this.b.c.b>0){aA(this,cz(this.b,0));}Dh(c);break;}}}case 512:if(d==512){if(xh(c)==9){a=sI(new qI());vz(this,a,this.ab(),Ah(c));e=yz(this,a,0,this.f);if(e!==this.b){bA(this,e,true);}}}case 256:{break;}}this.e=d;}
function hA(){nz(this.f);}
function iA(b){var a;a=xb(lK(this.a,b),16);if(a===null){return false;}mz(a,null);return true;}
function ty(){}
_=ty.prototype=new hB();_.A=dA;_.B=eA;_.ib=fA;_.mb=gA;_.sb=hA;_.Eb=iA;_.tN=kL+'Tree';_.tI=90;_.b=null;_.c=null;_.d=null;_.e=0;_.f=null;function Dy(a){a.c=sI(new qI());a.i=su(new bu());}
function Ey(d){var a,b,c,e;Dy(d);d.bc(dh());d.e=mh();d.d=ih();d.b=ih();a=jh();e=lh();c=kh();b=kh();ah(d.e,a);ah(a,e);ah(e,c);ah(e,b);Ci(c,'verticalAlign','middle');Ci(b,'verticalAlign','middle');ah(d.ab(),d.e);ah(d.ab(),d.b);ah(c,d.i.ab());ah(b,d.d);Ci(d.d,'display','inline');Ci(d.ab(),'whiteSpace','nowrap');Ci(d.b,'whiteSpace','nowrap');BA(d.d,'gwt-TreeItem',true);return d;}
function Fy(b,a){Ey(b);gz(b,a);return b;}
function cz(b,a){if(a<0||a>=b.c.b){return null;}return xb(xI(b.c,a),16);}
function bz(b,a){return yI(b.c,a);}
function dz(a){var b;b=a.k;{return null;}}
function ez(a){return a.i.ab();}
function fz(a){if(a.g!==null){a.g.Cb(a);}else if(a.j!==null){Ez(a.j,a);}}
function gz(b,a){mz(b,null);zi(b.d,a);}
function hz(b,a){b.g=a;}
function iz(b,a){if(b.h==a){return;}b.h=a;BA(b.d,'gwt-TreeItem-selected',a);}
function jz(b,a){kz(b,a,true);}
function kz(c,b,a){if(b&&c.c.b==0){return;}c.f=b;oz(c);}
function lz(d,c){var a,b;if(d.j===c){return;}if(d.j!==null){if(d.j.b===d){aA(d.j,null);}}d.j=c;for(a=0,b=d.c.b;a<b;++a){lz(xb(xI(d.c,a),16),c);}oz(d);}
function mz(b,a){zi(b.d,'');b.k=a;}
function oz(b){var a;if(b.j===null){return;}a=b.j.d;if(b.c.b==0){CA(b.b,false);BC((ie(),ye),b.i);return;}if(b.f){CA(b.b,true);BC((ie(),ze),b.i);}else{CA(b.b,false);BC((ie(),xe),b.i);}}
function nz(c){var a,b;oz(c);for(a=0,b=c.c.b;a<b;++a){nz(xb(xI(c.c,a),16));}}
function pz(a){if(a.g!==null||a.j!==null){fz(a);}hz(a,this);tI(this.c,a);Ci(a.ab(),'marginLeft','16px');ah(this.b,a.ab());lz(a,this.j);if(this.c.b==1){oz(this);}}
function qz(a){if(!wI(this.c,a)){return;}lz(a,null);pi(this.b,a.ab());hz(a,null);CI(this.c,a);if(this.c.b==0){oz(this);}}
function Cy(){}
_=Cy.prototype=new jA();_.s=pz;_.Cb=qz;_.tN=kL+'TreeItem';_.tI=91;_.b=null;_.d=null;_.e=null;_.f=false;_.g=null;_.h=false;_.j=null;_.k=null;function vy(b,a){b.a=a;Ey(b);return b;}
function wy(b,a){if(a.g!==null||a.j!==null){fz(a);}ah(b.a.ab(),a.ab());lz(a,b.j);hz(a,null);tI(b.c,a);Bi(a.ab(),'marginLeft',0);}
function yy(b,a){if(!wI(b.c,a)){return;}lz(a,null);hz(a,null);CI(b.c,a);pi(b.a.ab(),a.ab());}
function zy(a){wy(this,a);}
function Ay(a){yy(this,a);}
function uy(){}
_=uy.prototype=new Cy();_.s=zy;_.Cb=Ay;_.tN=kL+'Tree$1';_.tI=92;function aB(a){a.a=(lt(),nt);a.b=(ut(),wt);}
function bB(a){fn(a);aB(a);xi(a.e,'cellSpacing','0');xi(a.e,'cellPadding','0');return a;}
function cB(b,d){var a,c;c=lh();a=eB(b);ah(c,a);ah(b.d,c);bo(b,d,a);}
function eB(b){var a;a=kh();jn(b,a,b.a);kn(b,a,b.b);return a;}
function fB(b,a){b.a=a;}
function gB(c){var a,b;b=ji(c.ab());a=jo(this,c);if(a){pi(this.d,ji(b));}return a;}
function FA(){}
_=FA.prototype=new en();_.Eb=gB;_.tN=kL+'VerticalPanel';_.tI=93;function qB(b,a){b.a=rb('[Lcom.google.gwt.user.client.ui.Widget;',[130],[12],[4],null);return b;}
function rB(a,b){vB(a,b,a.b);}
function tB(b,a){if(a<0||a>=b.b){throw new rE();}return b.a[a];}
function uB(b,c){var a;for(a=0;a<b.b;++a){if(b.a[a]===c){return a;}}return (-1);}
function vB(d,e,a){var b,c;if(a<0||a>d.b){throw new rE();}if(d.b==d.a.a){c=rb('[Lcom.google.gwt.user.client.ui.Widget;',[130],[12],[d.a.a*2],null);for(b=0;b<d.a.a;++b){tb(c,b,d.a[b]);}d.a=c;}++d.b;for(b=d.b-1;b>a;--b){tb(d.a,b,d.a[b-1]);}tb(d.a,a,e);}
function wB(a){return kB(new jB(),a);}
function xB(c,b){var a;if(b<0||b>=c.b){throw new rE();}--c.b;for(a=b;a<c.b;++a){tb(c.a,a,c.a[a+1]);}tb(c.a,c.b,null);}
function yB(b,c){var a;a=uB(b,c);if(a==(-1)){throw new aL();}xB(b,a);}
function iB(){}
_=iB.prototype=new nF();_.tN=kL+'WidgetCollection';_.tI=94;_.a=null;_.b=0;function kB(b,a){b.b=a;return b;}
function mB(a){return a.a<a.b.b-1;}
function nB(a){if(a.a>=a.b.b){throw new aL();}return a.b.a[++a.a];}
function oB(){return mB(this);}
function pB(){return nB(this);}
function jB(){}
_=jB.prototype=new nF();_.gb=oB;_.kb=pB;_.tN=kL+'WidgetCollection$WidgetIterator';_.tI=95;_.a=(-1);function eC(b,a){return CB(new AB(),a,b);}
function BB(a){{EB(a);}}
function CB(a,b,c){a.b=b;BB(a);return a;}
function EB(a){++a.a;while(a.a<a.b.a){if(a.b[a.a]!==null){return;}++a.a;}}
function FB(a){return a.a<a.b.a;}
function aC(a){var b;if(!FB(a)){throw new aL();}b=a.b[a.a];EB(a);return b;}
function bC(){return FB(this);}
function cC(){return aC(this);}
function AB(){}
_=AB.prototype=new nF();_.gb=bC;_.kb=cC;_.tN=kL+'WidgetIterators$1';_.tI=96;_.a=(-1);function vC(e,b,g,c,f,h,a){var d;d='url('+g+') no-repeat '+(-c+'px ')+(-f+'px');Ci(b,'background',d);Ci(b,'width',h+'px');Ci(b,'height',a+'px');}
function xC(c,f,b,e,g,a){var d;d=ih();zi(d,yC(c,f,b,e,g,a));return ii(d);}
function yC(e,g,c,f,h,b){var a,d;d='width: '+h+'px; height: '+b+'px; background: url('+g+') no-repeat '+(-c+'px ')+(-f+'px');a="<img src='"+u()+"clear.cache.gif' style='"+d+"' border='0'>";return a;}
function uC(){}
_=uC.prototype=new nF();_.tN=lL+'ClippedImageImpl';_.tI=97;function CC(){CC=eL;FC=new uC();}
function AC(c,e,b,d,f,a){CC();c.d=e;c.b=b;c.c=d;c.e=f;c.a=a;return c;}
function BC(b,a){wu(a,b.d,b.b,b.c,b.e,b.a);}
function DC(a){return tu(new bu(),a.d,a.b,a.c,a.e,a.a);}
function EC(a){return yC(FC,a.d,a.b,a.c,a.e,a.a);}
function zC(){}
_=zC.prototype=new ym();_.tN=lL+'ClippedImagePrototype';_.tI=98;_.a=0;_.b=0;_.c=0;_.d=null;_.e=0;var FC;function nD(){nD=eL;oD=dD(new bD());pD=oD!==null?mD(new aD()):oD;}
function mD(a){nD();return a;}
function aD(){}
_=aD.prototype=new nF();_.tN=lL+'FocusImpl';_.tI=99;var oD,pD;function fD(){fD=eL;nD();}
function cD(a){a.a=gD(a);a.b=hD(a);a.c=jD(a);}
function dD(a){fD();mD(a);cD(a);return a;}
function eD(b,a){a.firstChild.blur();}
function gD(b){return function(a){if(this.parentNode.onblur){this.parentNode.onblur(a);}};}
function hD(b){return function(a){if(this.parentNode.onfocus){this.parentNode.onfocus(a);}};}
function iD(c){var a=$doc.createElement('div');var b=c.z();b.addEventListener('blur',c.a,false);b.addEventListener('focus',c.b,false);a.addEventListener('mousedown',c.c,false);a.appendChild(b);return a;}
function jD(a){return function(){this.firstChild.focus();};}
function kD(b,a){a.firstChild.focus();}
function lD(){var a=$doc.createElement('input');a.type='text';a.style.width=a.style.height=0;a.style.zIndex= -1;a.style.position='absolute';return a;}
function bD(){}
_=bD.prototype=new aD();_.z=lD;_.tN=lL+'FocusImplOld';_.tI=100;function qD(){}
_=qD.prototype=new nF();_.tN=lL+'PopupImpl';_.tI=101;function xD(){xD=eL;AD=BD();}
function wD(a){xD();return a;}
function yD(b){var a;a=dh();if(AD){zi(a,'<div><\/div>');cj(tD(new sD(),b,a));}return a;}
function zD(b,a){return AD?ii(a):a;}
function BD(){xD();if(navigator.userAgent.indexOf('Macintosh')!= -1){return true;}return false;}
function rD(){}
_=rD.prototype=new qD();_.tN=lL+'PopupImplMozilla';_.tI=102;var AD;function tD(b,a,c){b.a=c;return b;}
function vD(){Ci(this.a,'overflow','auto');}
function sD(){}
_=sD.prototype=new nF();_.D=vD;_.tN=lL+'PopupImplMozilla$1';_.tI=103;function ED(){}
_=ED.prototype=new rF();_.tN=mL+'ArrayStoreException';_.tI=104;function dE(a,b){if(b<2||b>36){return (-1);}if(a>=48&&a<48+DE(b,10)){return a-48;}if(a>=97&&a<b+97-10){return a-97+10;}if(a>=65&&a<b+65-10){return a-65+10;}return (-1);}
function eE(){}
_=eE.prototype=new rF();_.tN=mL+'ClassCastException';_.tI=105;function mE(b,a){sF(b,a);return b;}
function lE(){}
_=lE.prototype=new rF();_.tN=mL+'IllegalArgumentException';_.tI=106;function pE(b,a){sF(b,a);return b;}
function oE(){}
_=oE.prototype=new rF();_.tN=mL+'IllegalStateException';_.tI=107;function sE(b,a){sF(b,a);return b;}
function rE(){}
_=rE.prototype=new rF();_.tN=mL+'IndexOutOfBoundsException';_.tI=108;function hF(){hF=eL;{mF();}}
function iF(a){hF();return isNaN(a);}
function jF(e,d,c,h){hF();var a,b,f,g;if(e===null){throw fF(new eF(),'Unable to parse null');}b=BF(e);f=b>0&&vF(e,0)==45?1:0;for(a=f;a<b;a++){if(dE(vF(e,a),d)==(-1)){throw fF(new eF(),'Could not parse '+e+' in radix '+d);}}g=kF(e,d);if(iF(g)){throw fF(new eF(),'Unable to parse '+e);}else if(g<c||g>h){throw fF(new eF(),'The string '+e+' exceeds the range for the requested data type');}return g;}
function kF(b,a){hF();return parseInt(b,a);}
function mF(){hF();lF=/^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i;}
var lF=null;function vE(){vE=eL;hF();}
function yE(a){vE();return zE(a,10);}
function zE(b,a){vE();return Ab(jF(b,a,(-2147483648),2147483647));}
var wE=2147483647,xE=(-2147483648);function CE(a){return a<0?-a:a;}
function DE(a,b){return a<b?a:b;}
function EE(){}
_=EE.prototype=new rF();_.tN=mL+'NegativeArraySizeException';_.tI=109;function bF(b,a){sF(b,a);return b;}
function aF(){}
_=aF.prototype=new rF();_.tN=mL+'NullPointerException';_.tI=110;function fF(b,a){mE(b,a);return b;}
function eF(){}
_=eF.prototype=new lE();_.tN=mL+'NumberFormatException';_.tI=111;function vF(b,a){return b.charCodeAt(a);}
function yF(b,a){if(!yb(a,1))return false;return FF(b,a);}
function xF(b,a){if(a==null)return false;return b==a||b.toLowerCase()==a.toLowerCase();}
function zF(b,a){return b.indexOf(a);}
function AF(c,b,a){return c.indexOf(b,a);}
function BF(a){return a.length;}
function CF(b,a){return b.substr(a,b.length-a);}
function DF(c,a,b){return c.substr(a,b-a);}
function EF(c){var a=c.replace(/^(\s*)/,'');var b=a.replace(/\s*$/,'');return b;}
function FF(a,b){return String(a)==b;}
function aG(a){return yF(this,a);}
function cG(){var a=bG;if(!a){a=bG={};}var e=':'+this;var b=a[e];if(b==null){b=0;var f=this.length;var d=f<64?1:f/32|0;for(var c=0;c<f;c+=d){b<<=1;b+=this.charCodeAt(c);}b|=0;a[e]=b;}return b;}
_=String.prototype;_.eQ=aG;_.hC=cG;_.tN=mL+'String';_.tI=2;var bG=null;function fG(){return new Date().getTime();}
function gG(a){return A(a);}
function lG(b,a){sF(b,a);return b;}
function kG(){}
_=kG.prototype=new rF();_.tN=mL+'UnsupportedOperationException';_.tI=113;function vG(b,a){b.c=a;return b;}
function xG(a){return a.a<a.c.ic();}
function yG(a){if(!xG(a)){throw new aL();}return a.c.eb(a.b=a.a++);}
function zG(a){if(a.b<0){throw new oE();}a.c.Db(a.b);a.a=a.b;a.b=(-1);}
function AG(){return xG(this);}
function BG(){return yG(this);}
function uG(){}
_=uG.prototype=new nF();_.gb=AG;_.kb=BG;_.tN=nL+'AbstractList$IteratorImpl';_.tI=114;_.a=0;_.b=(-1);function dI(f,d,e){var a,b,c;for(b=aK(f.C());zJ(b);){a=AJ(b);c=a.bb();if(d===null?c===null:d.eQ(c)){if(e){BJ(b);}return a;}}return null;}
function eI(b){var a;a=b.C();return hH(new gH(),b,a);}
function fI(b){var a;a=kK(b);return vH(new uH(),b,a);}
function gI(a){return dI(this,a,false)!==null;}
function hI(d){var a,b,c,e,f,g,h;if(d===this){return true;}if(!yb(d,26)){return false;}f=xb(d,26);c=eI(this);e=f.jb();if(!nI(c,e)){return false;}for(a=jH(c);qH(a);){b=rH(a);h=this.fb(b);g=f.fb(b);if(h===null?g!==null:!h.eQ(g)){return false;}}return true;}
function iI(b){var a;a=dI(this,b,false);return a===null?null:a.db();}
function jI(){var a,b,c;b=0;for(c=aK(this.C());zJ(c);){a=AJ(c);b+=a.hC();}return b;}
function kI(){return eI(this);}
function fH(){}
_=fH.prototype=new nF();_.v=gI;_.eQ=hI;_.fb=iI;_.hC=jI;_.jb=kI;_.tN=nL+'AbstractMap';_.tI=115;function nI(e,b){var a,c,d;if(b===e){return true;}if(!yb(b,27)){return false;}c=xb(b,27);if(c.ic()!=e.ic()){return false;}for(a=c.ib();a.gb();){d=a.kb();if(!e.w(d)){return false;}}return true;}
function oI(a){return nI(this,a);}
function pI(){var a,b,c;a=0;for(b=this.ib();b.gb();){c=b.kb();if(c!==null){a+=c.hC();}}return a;}
function lI(){}
_=lI.prototype=new nG();_.eQ=oI;_.hC=pI;_.tN=nL+'AbstractSet';_.tI=116;function hH(b,a,c){b.a=a;b.b=c;return b;}
function jH(b){var a;a=aK(b.b);return oH(new nH(),b,a);}
function kH(a){return this.a.v(a);}
function lH(){return jH(this);}
function mH(){return this.b.a.c;}
function gH(){}
_=gH.prototype=new lI();_.w=kH;_.ib=lH;_.ic=mH;_.tN=nL+'AbstractMap$1';_.tI=117;function oH(b,a,c){b.a=c;return b;}
function qH(a){return a.a.gb();}
function rH(b){var a;a=b.a.kb();return a.bb();}
function sH(){return qH(this);}
function tH(){return rH(this);}
function nH(){}
_=nH.prototype=new nF();_.gb=sH;_.kb=tH;_.tN=nL+'AbstractMap$2';_.tI=118;function vH(b,a,c){b.a=a;b.b=c;return b;}
function xH(b){var a;a=aK(b.b);return CH(new BH(),b,a);}
function yH(a){return jK(this.a,a);}
function zH(){return xH(this);}
function AH(){return this.b.a.c;}
function uH(){}
_=uH.prototype=new nG();_.w=yH;_.ib=zH;_.ic=AH;_.tN=nL+'AbstractMap$3';_.tI=119;function CH(b,a,c){b.a=c;return b;}
function EH(a){return a.a.gb();}
function FH(a){var b;b=a.a.kb().db();return b;}
function aI(){return EH(this);}
function bI(){return FH(this);}
function BH(){}
_=BH.prototype=new nF();_.gb=aI;_.kb=bI;_.tN=nL+'AbstractMap$4';_.tI=120;function hK(){hK=eL;oK=uK();}
function eK(a){{gK(a);}}
function fK(a){hK();eK(a);return a;}
function gK(a){a.a=cb();a.d=db();a.b=Eb(oK,E);a.c=0;}
function iK(b,a){if(yb(a,1)){return yK(b.d,xb(a,1))!==oK;}else if(a===null){return b.b!==oK;}else{return xK(b.a,a,a.hC())!==oK;}}
function jK(a,b){if(a.b!==oK&&wK(a.b,b)){return true;}else if(tK(a.d,b)){return true;}else if(rK(a.a,b)){return true;}return false;}
function kK(a){return EJ(new vJ(),a);}
function lK(c,a){var b;if(yb(a,1)){b=yK(c.d,xb(a,1));}else if(a===null){b=c.b;}else{b=xK(c.a,a,a.hC());}return b===oK?null:b;}
function mK(c,a,d){var b;{b=c.b;c.b=d;}if(b===oK){++c.c;return null;}else{return b;}}
function nK(c,a){var b;if(yb(a,1)){b=BK(c.d,xb(a,1));}else if(a===null){b=c.b;c.b=Eb(oK,E);}else{b=AK(c.a,a,a.hC());}if(b===oK){return null;}else{--c.c;return b;}}
function pK(e,c){hK();for(var d in e){if(d==parseInt(d)){var a=e[d];for(var f=0,b=a.length;f<b;++f){c.u(a[f]);}}}}
function qK(d,a){hK();for(var c in d){if(c.charCodeAt(0)==58){var e=d[c];var b=qJ(c.substring(1),e);a.u(b);}}}
function rK(f,h){hK();for(var e in f){if(e==parseInt(e)){var a=f[e];for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.db();if(wK(h,d)){return true;}}}}return false;}
function sK(a){return iK(this,a);}
function tK(c,d){hK();for(var b in c){if(b.charCodeAt(0)==58){var a=c[b];if(wK(d,a)){return true;}}}return false;}
function uK(){hK();}
function vK(){return kK(this);}
function wK(a,b){hK();if(a===b){return true;}else if(a===null){return false;}else{return a.eQ(b);}}
function zK(a){return lK(this,a);}
function xK(f,h,e){hK();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.bb();if(wK(h,d)){return c.db();}}}}
function yK(b,a){hK();return b[':'+a];}
function AK(f,h,e){hK();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.bb();if(wK(h,d)){if(a.length==1){delete f[e];}else{a.splice(g,1);}return c.db();}}}}
function BK(c,a){hK();a=':'+a;var b=c[a];delete c[a];return b;}
function mJ(){}
_=mJ.prototype=new fH();_.v=sK;_.C=vK;_.fb=zK;_.tN=nL+'HashMap';_.tI=121;_.a=null;_.b=null;_.c=0;_.d=null;var oK;function oJ(b,a,c){b.a=a;b.b=c;return b;}
function qJ(a,b){return oJ(new nJ(),a,b);}
function rJ(b){var a;if(yb(b,28)){a=xb(b,28);if(wK(this.a,a.bb())&&wK(this.b,a.db())){return true;}}return false;}
function sJ(){return this.a;}
function tJ(){return this.b;}
function uJ(){var a,b;a=0;b=0;if(this.a!==null){a=this.a.hC();}if(this.b!==null){b=this.b.hC();}return a^b;}
function nJ(){}
_=nJ.prototype=new nF();_.eQ=rJ;_.bb=sJ;_.db=tJ;_.hC=uJ;_.tN=nL+'HashMap$EntryImpl';_.tI=122;_.a=null;_.b=null;function EJ(b,a){b.a=a;return b;}
function aK(a){return xJ(new wJ(),a.a);}
function bK(c){var a,b,d;if(yb(c,28)){a=xb(c,28);b=a.bb();if(iK(this.a,b)){d=lK(this.a,b);return wK(a.db(),d);}}return false;}
function cK(){return aK(this);}
function dK(){return this.a.c;}
function vJ(){}
_=vJ.prototype=new lI();_.w=bK;_.ib=cK;_.ic=dK;_.tN=nL+'HashMap$EntrySet';_.tI=123;function xJ(c,b){var a;c.c=b;a=sI(new qI());if(c.c.b!==(hK(),oK)){tI(a,oJ(new nJ(),null,c.c.b));}qK(c.c.d,a);pK(c.c.a,a);c.a=EG(a);return c;}
function zJ(a){return xG(a.a);}
function AJ(a){return a.b=xb(yG(a.a),28);}
function BJ(a){if(a.b===null){throw pE(new oE(),'Must call next() before remove().');}else{zG(a.a);nK(a.c,a.b.bb());a.b=null;}}
function CJ(){return zJ(this);}
function DJ(){return AJ(this);}
function wJ(){}
_=wJ.prototype=new nF();_.gb=CJ;_.kb=DJ;_.tN=nL+'HashMap$EntrySetIterator';_.tI=124;_.a=null;_.b=null;function aL(){}
_=aL.prototype=new rF();_.tN=nL+'NoSuchElementException';_.tI=125;function DD(){ee(be(new zc()));}
function gwtOnLoad(b,d,c){$moduleName=d;$moduleBase=c;if(b)try{DD();}catch(a){b(d);}else{DD();}}
var Db=[{},{18:1},{1:1,18:1,23:1,24:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{2:1,18:1},{18:1},{18:1},{18:1},{18:1,19:1},{12:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{6:1,12:1,17:1,18:1,19:1,20:1},{6:1,12:1,13:1,17:1,18:1,19:1,20:1},{6:1,12:1,13:1,17:1,18:1,19:1,20:1},{11:1,18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{11:1,18:1},{18:1,22:1},{6:1,12:1,17:1,18:1,19:1,20:1},{10:1,18:1},{5:1,18:1},{12:1,18:1,19:1,20:1},{4:1,18:1},{11:1,12:1,15:1,18:1,19:1,20:1},{18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{11:1,12:1,18:1,19:1,20:1},{3:1,18:1},{18:1},{8:1,18:1},{8:1,18:1},{8:1,18:1},{18:1},{2:1,7:1,18:1},{2:1,18:1},{9:1,18:1},{18:1},{18:1},{18:1},{18:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{18:1},{18:1,25:1},{18:1,25:1},{18:1,25:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1},{18:1,21:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{12:1,17:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{5:1,18:1},{18:1},{18:1},{18:1},{18:1,25:1},{12:1,14:1,17:1,18:1,19:1,20:1},{9:1,18:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1,25:1},{12:1,17:1,18:1,19:1,20:1},{16:1,18:1,19:1},{16:1,18:1,19:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{5:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{18:1,24:1},{3:1,18:1},{18:1},{18:1,26:1},{18:1,27:1},{18:1,27:1},{18:1},{18:1},{18:1},{18:1,26:1},{18:1,28:1},{18:1,27:1},{18:1},{3:1,18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1}];if (com_google_gwt_sample_mail_Mail) {  var __gwt_initHandlers = com_google_gwt_sample_mail_Mail.__gwt_initHandlers;  com_google_gwt_sample_mail_Mail.onScriptLoad(gwtOnLoad);}})();