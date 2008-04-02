(function(){var $wnd = window;var $doc = $wnd.document;var $moduleName, $moduleBase;var _,zK='com.google.gwt.core.client.',AK='com.google.gwt.lang.',BK='com.google.gwt.sample.mail.client.',CK='com.google.gwt.user.client.',DK='com.google.gwt.user.client.impl.',EK='com.google.gwt.user.client.ui.',FK='com.google.gwt.user.client.ui.impl.',aL='java.lang.',bL='java.util.';function yK(){}
function dF(a){return this===a;}
function eF(){return AF(this);}
function bF(){}
_=bF.prototype={};_.eQ=dF;_.hC=eF;_.tN=aL+'Object';_.tI=1;function u(){return B();}
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
_=E.prototype=new bF();_.eQ=fb;_.hC=gb;_.tN=zK+'JavaScriptObject';_.tI=7;function ib(c,a,d,b,e){c.a=a;c.b=b;c.tN=e;c.tI=d;return c;}
function kb(a,b,c){return a[b]=c;}
function mb(a,b){return lb(a,b);}
function lb(a,b){return ib(new hb(),b,a.tI,a.b,a.tN);}
function nb(b,a){return b[a];}
function pb(b,a){return b[a];}
function ob(a){return a.length;}
function rb(e,d,c,b,a){return qb(e,d,c,b,0,ob(b),a);}
function qb(j,i,g,c,e,a,b){var d,f,h;if((f=nb(c,e))<0){throw new sE();}h=ib(new hb(),f,nb(i,e),nb(g,e),j);++e;if(e<a){j=qF(j,1);for(d=0;d<f;++d){kb(h,d,qb(j,i,g,c,e,a,b));}}else{for(d=0;d<f;++d){kb(h,d,b);}}return h;}
function sb(f,e,c,g){var a,b,d;b=ob(g);d=ib(new hb(),b,e,c,f);for(a=0;a<b;++a){kb(d,a,pb(g,a));}return d;}
function tb(a,b,c){if(c!==null&&a.b!=0&& !yb(c,a.b)){throw new sD();}return kb(a,b,c);}
function hb(){}
_=hb.prototype=new bF();_.tN=AK+'Array';_.tI=8;function wb(b,a){return !(!(b&&Db[b][a]));}
function xb(b,a){if(b!=null)wb(b.tI,a)||Cb();return b;}
function yb(b,a){return b!=null&&wb(b.tI,a);}
function zb(a){return a&65535;}
function Ab(a){return ~(~a);}
function Bb(a){if(a>(jE(),kE))return jE(),kE;if(a<(jE(),lE))return jE(),lE;return a>=0?Math.floor(a):Math.ceil(a);}
function Cb(){throw new yD();}
function Eb(b,d){_=d.prototype;if(b&& !(b.tI>=_.tI)){var c=b.toString;for(var a in _){b[a]=_[a];}b.toString=c;}return b;}
var Db;function eA(b,a){vA(b.ab(),a,true);}
function gA(a){return Eh(a.E());}
function hA(a){return Fh(a.E());}
function iA(a){return fi(a.r,'offsetHeight');}
function jA(a){return fi(a.r,'offsetWidth');}
function kA(d,b,a){var c=b.parentNode;if(!c){return;}c.insertBefore(a,b);c.removeChild(b);}
function lA(b,a){if(b.r!==null){kA(b,b.r,a);}b.r=a;}
function mA(b,c,a){b.fc(c);b.bc(a);}
function nA(b,a){uA(b.ab(),a);}
function oA(b,a){Di(b.E(),a|hi(b.E()));}
function pA(){return this.r;}
function qA(){return this.r;}
function rA(a){return gi(a,'className');}
function sA(a){lA(this,a);}
function tA(a){Ci(this.r,'height',a);}
function uA(a,b){xi(a,'className',b);}
function vA(c,j,a){var b,d,e,f,g,h,i;if(c===null){throw gF(new fF(),'Null widget handle. If you are creating a composite, ensure that initWidget() has been called.');}j=sF(j);if(pF(j)==0){throw aE(new FD(),'Style names cannot be empty');}i=rA(c);e=nF(i,j);while(e!=(-1)){if(e==0||jF(i,e-1)==32){f=e+pF(j);g=pF(i);if(f==g||f<g&&jF(i,f)==32){break;}}e=oF(i,j,e+1);}if(a){if(e==(-1)){if(pF(i)>0){i+=' ';}xi(c,'className',i+j);}}else{if(e!=(-1)){b=sF(rF(i,0,e));d=sF(qF(i,e+pF(j)));if(pF(b)==0){h=d;}else if(pF(d)==0){h=b;}else{h=b+' '+d;}xi(c,'className',h);}}}
function wA(a,b){a.style.display=b?'':'none';}
function xA(a){wA(this.r,a);}
function yA(a){Ci(this.r,'width',a);}
function dA(){}
_=dA.prototype=new bF();_.E=pA;_.ab=qA;_.Fb=sA;_.bc=tA;_.dc=xA;_.fc=yA;_.tN=EK+'UIObject';_.tI=11;_.r=null;function aC(a){if(!a.fb()){throw dE(new cE(),"Should only call onDetach when the widget is attached to the browser's document");}try{a.wb();}finally{a.z();yi(a.E(),null);a.o=false;}}
function bC(a){if(yb(a.q,17)){xb(a.q,17).Cb(a);}else if(a.q!==null){throw dE(new cE(),"This widget's parent does not implement HasWidgets");}}
function cC(b,a){if(b.fb()){yi(b.E(),null);}lA(b,a);if(b.fb()){yi(a,b);}}
function dC(b,a){b.p=a;}
function eC(c,b){var a;a=c.q;if(b===null){if(a!==null&&a.fb()){c.nb();}c.q=null;}else{if(a!==null){throw dE(new cE(),'Cannot set a new parent without first clearing the old parent');}c.q=b;if(b.fb()){c.jb();}}}
function fC(){}
function gC(){}
function hC(){return this.o;}
function iC(){if(this.fb()){throw dE(new cE(),"Should only call onAttach when the widget is detached from the browser's document");}this.o=true;yi(this.E(),this);this.y();this.qb();}
function jC(a){}
function kC(){aC(this);}
function lC(){}
function mC(){}
function nC(a){cC(this,a);}
function bB(){}
_=bB.prototype=new dA();_.y=fC;_.z=gC;_.fb=hC;_.jb=iC;_.kb=jC;_.nb=kC;_.qb=lC;_.wb=mC;_.Fb=nC;_.tN=EK+'Widget';_.tI=12;_.o=false;_.p=null;_.q=null;function kv(b,a){eC(a,b);}
function mv(b,a){eC(a,null);}
function nv(){var a,b;for(b=this.gb();b.eb();){a=xb(b.ib(),12);a.jb();}}
function ov(){var a,b;for(b=this.gb();b.eb();){a=xb(b.ib(),12);a.nb();}}
function pv(){}
function qv(){}
function jv(){}
_=jv.prototype=new bB();_.y=nv;_.z=ov;_.qb=pv;_.wb=qv;_.tN=EK+'Panel';_.tI=13;function gx(a){hx(a,dh());return a;}
function hx(b,a){b.Fb(a);return b;}
function ix(a,b){if(a.n!==null){throw dE(new cE(),'SimplePanel can only contain one child widget');}a.ec(b);}
function kx(a,b){if(b===a.n){return;}if(b!==null){bC(b);}if(a.n!==null){a.Cb(a.n);}a.n=b;if(b!==null){ah(a.D(),a.n.E());kv(a,b);}}
function lx(){return this.E();}
function mx(){return cx(new ax(),this);}
function nx(a){if(this.n!==a){return false;}mv(this,a);pi(this.D(),a.E());this.n=null;return true;}
function ox(a){kx(this,a);}
function Fw(){}
_=Fw.prototype=new jv();_.D=lx;_.gb=mx;_.Cb=nx;_.ec=ox;_.tN=EK+'SimplePanel';_.tI=14;_.n=null;function xv(){xv=yK;fw=new oD();}
function sv(a){xv();hx(a,qD(fw));Ev(a,0,0);return a;}
function tv(b,a){xv();sv(b);b.g=a;return b;}
function uv(c,a,b){xv();tv(c,a);c.k=b;return c;}
function vv(b,a){if(a.blur){a.blur();}}
function wv(c){var a,b,d;a=c.l;if(!a){Fv(c,false);cw(c);}b=Bb((mk()-zv(c))/2);d=Bb((lk()-yv(c))/2);Ev(c,nk()+b,ok()+d);if(!a){Fv(c,true);}}
function yv(a){return iA(a);}
function zv(a){return jA(a);}
function Av(a){Bv(a,false);}
function Bv(b,a){if(!b.l){return;}b.l=false;pm(vw(),b);b.E();}
function Cv(a){var b;b=a.n;if(b!==null){if(a.h!==null){b.bc(a.h);}if(a.i!==null){b.fc(a.i);}}}
function Dv(e,b){var a,c,d,f;d=Ah(b);c=mi(e.E(),d);f=Ch(b);switch(f){case 128:{a=e.pb(zb(xh(b)),vu(b));return a&&(c|| !e.k);}case 512:{a=(zb(xh(b)),vu(b),true);return a&&(c|| !e.k);}case 256:{a=(zb(xh(b)),vu(b),true);return a&&(c|| !e.k);}case 4:case 8:case 64:case 1:case 2:{if((Eg(),ri)!==null){return true;}if(!c&&e.g&&f==4){Bv(e,true);return true;}break;}case 2048:{if(e.k&& !c&&d!==null){vv(e,d);return false;}}}return !e.k||c;}
function Ev(c,b,d){var a;if(b<0){b=0;}if(d<0){d=0;}c.j=b;c.m=d;a=c.E();Ci(a,'left',b+'px');Ci(a,'top',d+'px');}
function Fv(a,b){Ci(a.E(),'visibility',b?'visible':'hidden');a.E();}
function aw(a,b){kx(a,b);Cv(a);}
function bw(a,b){a.i=b;Cv(a);if(pF(b)==0){a.i=null;}}
function cw(a){if(a.l){return;}a.l=true;Fg(a);Ci(a.E(),'position','absolute');if(a.m!=(-1)){Ev(a,a.j,a.m);}nm(vw(),a);a.E();}
function dw(){return this.E();}
function ew(){return this.E();}
function gw(){qi(this);aC(this);}
function hw(a){return Dv(this,a);}
function iw(a,b){return true;}
function jw(a){this.h=a;Cv(this);if(pF(a)==0){this.h=null;}}
function kw(a){Fv(this,a);}
function lw(a){aw(this,a);}
function mw(a){bw(this,a);}
function rv(){}
_=rv.prototype=new Fw();_.D=dw;_.ab=ew;_.nb=gw;_.ob=hw;_.pb=iw;_.bc=jw;_.dc=kw;_.ec=lw;_.fc=mw;_.tN=EK+'PopupPanel';_.tI=15;_.g=false;_.h=null;_.i=null;_.j=(-1);_.k=false;_.l=false;_.m=(-1);var fw;function to(){to=yK;xv();}
function po(a){a.a=As(new oq());a.f=Cp(new xp());}
function qo(a){to();ro(a,false);return a;}
function ro(b,a){to();so(b,a,true);return b;}
function so(c,a,b){to();uv(c,a,b);po(c);ws(c.f,0,0,c.a);c.f.bc('100%');os(c.f,0);qs(c.f,0);rs(c.f,0);ar(c.f.b,1,0,'100%');dr(c.f.b,1,0,'100%');Fq(c.f.b,1,0,(ft(),gt),(ot(),pt));aw(c,c.f);nA(c,'gwt-DialogBox');nA(c.a,'Caption');Au(c.a,c);return c;}
function uo(b,a){Cu(b.a,a);}
function vo(a,b){if(a.b!==null){ns(a.f,a.b);}if(b!==null){ws(a.f,1,0,b);}a.b=b;}
function wo(a){if(Ch(a)==4){if(mi(this.a.E(),Ah(a))){Dh(a);}}return Dv(this,a);}
function xo(a,b,c){this.e=true;ui(this.a.E());this.c=b;this.d=c;}
function yo(a){}
function zo(a){}
function Ao(c,d,e){var a,b;if(this.e){a=d+gA(this);b=e+hA(this);Ev(this,a-this.c,b-this.d);}}
function Bo(a,b,c){this.e=false;oi(this.a.E());}
function Co(a){if(this.b!==a){return false;}ns(this.f,a);return true;}
function Do(a){vo(this,a);}
function Eo(a){bw(this,a);this.f.fc('100%');}
function oo(){}
_=oo.prototype=new rv();_.ob=wo;_.rb=xo;_.sb=yo;_.tb=zo;_.ub=Ao;_.vb=Bo;_.Cb=Co;_.ec=Do;_.fc=Eo;_.tN=EK+'DialogBox';_.tI=16;_.b=null;_.c=0;_.d=0;_.e=false;function hc(){hc=yK;to();}
function gc(c){var a,b;hc();qo(c);uo(c,'About the Mail Sample');a=BA(new zA());b=Bs(new oq(),"This sample application demonstrates the construction of a complex user interface using GWT's built-in widgets.  Have a look at the code to see how easy it is to build your own apps!");nA(b,'mail-AboutText');CA(a,b);CA(a,Bm(new um(),'Close',dc(new cc(),c)));vo(c,a);return c;}
function ic(a,b){switch(a){case 13:case 27:Av(this);break;}return true;}
function bc(){}
_=bc.prototype=new oo();_.pb=ic;_.tN=BK+'AboutDialog';_.tI=17;function dc(b,a){b.a=a;return b;}
function fc(a){Av(this.a);}
function cc(){}
_=cc.prototype=new bF();_.mb=fc;_.tN=BK+'AboutDialog$1';_.tI=18;function io(a){if(a.h===null){throw dE(new cE(),'initWidget() was never called in '+v(a));}return a.r;}
function jo(a,b){if(a.h!==null){throw dE(new cE(),'Composite.initWidget() may only be called once.');}bC(b);a.Fb(b.E());a.h=b;eC(b,a);}
function ko(){return io(this);}
function lo(){if(this.h!==null){return this.h.fb();}return false;}
function mo(){this.h.jb();this.qb();}
function no(){try{this.wb();}finally{this.h.nb();}}
function go(){}
_=go.prototype=new bB();_.E=ko;_.fb=lo;_.jb=mo;_.nb=no;_.tN=EK+'Composite';_.tI=19;_.h=null;function vc(a){a.a=sb('[Lcom.google.gwt.sample.mail.client.Contacts$Contact;',126,22,[sc(new oc(),'Benoit Mandelbrot','benoit@example.com',a),sc(new oc(),'Albert Einstein','albert@example.com',a),sc(new oc(),'Rene Descartes','rene@example.com',a),sc(new oc(),'Bob Saget','bob@example.com',a),sc(new oc(),'Ludwig von Beethoven','ludwig@example.com',a),sc(new oc(),'Richard Feynman','richard@example.com',a),sc(new oc(),'Alan Turing','alan@example.com',a),sc(new oc(),'John von Neumann','john@example.com',a)]);a.b=BA(new zA());}
function wc(d,b){var a,c;vc(d);c=gx(new Fw());c.ec(d.b);for(a=0;a<d.a.a;++a){xc(d,d.a[a]);}jo(d,c);nA(d,'mail-Contacts');return d;}
function xc(c,a){var b;b=Bs(new oq(),"<a href='javascript:;'>"+a.b+'<\/a>');CA(c.b,b);zu(b,lc(new kc(),c,a,b));}
function jc(){}
_=jc.prototype=new go();_.tN=BK+'Contacts';_.tI=20;function lc(b,a,c,d){b.a=a;b.b=c;b.c=d;return b;}
function nc(c){var a,b,d;b=qc(new pc(),this.b,this.a);a=gA(this.c)+14;d=hA(this.c)+14;Ev(b,a,d);cw(b);}
function kc(){}
_=kc.prototype=new bF();_.mb=nc;_.tN=BK+'Contacts$1';_.tI=21;function sc(d,b,a,c){d.b=b;d.a=a;return d;}
function oc(){}
_=oc.prototype=new bF();_.tN=BK+'Contacts$Contact';_.tI=22;_.a=null;_.b=null;function rc(){rc=yK;xv();}
function qc(g,a,f){var b,c,d,e;rc();tv(g,true);d=BA(new zA());e=yu(new wu(),a.b);b=yu(new wu(),a.a);CA(d,e);CA(d,b);c=vt(new tt());en(c,4);wt(c,xC((ie(),le)));wt(c,d);ix(g,c);nA(g,'mail-ContactPopup');nA(e,'mail-ContactPopupName');nA(b,'mail-ContactPopupEmail');return g;}
function pc(){}
_=pc.prototype=new rv();_.tN=BK+'Contacts$ContactPopup';_.tI=23;function ce(){ce=yK;Ae=he(new ge());}
function ae(a){a.e=yf(new vf(),Ae);a.c=BA(new zA());a.a=bd(new Fc());a.d=lf(new df(),Ae);}
function be(a){ce();ae(a);return a;}
function de(b,a){ed(b.a,a);}
function ee(b){var a;Ce=b;b.e.fc('100%');b.b=yd(new wd());b.b.fc('100%');CA(b.c,b.b);CA(b.c,b.a);b.b.fc('100%');b.a.fc('100%');a=ip(new Fo());jp(a,b.e,(kp(),sp));jp(a,b.d,(kp(),up));jp(a,b.c,(kp(),qp));a.fc('100%');en(a,4);pp(a,b.c,'100%');ek(b);hk(false);uk('0px');nm(vw(),a);cj(Bc(new Ac(),b));fe(b,mk(),lk());}
function fe(c,d,a){var b;b=a-hA(c.d)-8;if(b<1){b=1;}c.d.bc(''+b);cd(c.a,d,a);}
function Be(b,a){fe(this,b,a);}
function zc(){}
_=zc.prototype=new bF();_.zb=Be;_.tN=BK+'Mail';_.tI=24;_.b=null;var Ae,Ce=null;function Bc(b,a){b.a=a;return b;}
function Dc(){fe(this.a,mk(),lk());}
function Ac(){}
_=Ac.prototype=new bF();_.B=Dc;_.tN=BK+'Mail$1';_.tI=25;function ad(a){a.c=BA(new zA());a.b=BA(new zA());a.g=As(new oq());a.f=As(new oq());a.d=As(new oq());a.a=As(new oq());a.e=Bw(new zw(),a.a);}
function bd(b){var a;ad(b);Du(b.a,true);CA(b.b,b.g);CA(b.b,b.f);CA(b.b,b.d);b.b.fc('100%');a=ip(new Fo());jp(a,b.b,(kp(),sp));jp(a,b.e,(kp(),qp));mp(a,b.e,'100%');CA(b.c,a);mA(a,'100%','100%');mA(b.e,'100%','100%');jo(b,b.c);nA(b,'mail-Detail');nA(b.b,'mail-DetailHeader');nA(a,'mail-DetailInner');nA(b.g,'mail-DetailSubject');nA(b.f,'mail-DetailSender');nA(b.d,'mail-DetailRecipient');nA(b.a,'mail-DetailBody');return b;}
function cd(c,e,d){var a,b;b=e-gA(c.e)-9;if(b<1){b=1;}a=d-hA(c.e)-9;if(a<1){a=1;}mA(c.e,''+b,''+a);}
function ed(b,a){Es(b.g,a.e);Es(b.f,'<b>From:<\/b>&nbsp;'+a.d);Es(b.d,'<b>To:<\/b>&nbsp;foo@example.com');Es(b.a,a.a);}
function Fc(){}
_=Fc.prototype=new go();_.tN=BK+'MailDetail';_.tI=26;function gd(e,c,b,d,a){e.d=c;e.b=b;e.e=d;e.a=a;return e;}
function fd(){}
_=fd.prototype=new bF();_.tN=BK+'MailItem';_.tI=27;_.a=null;_.b=null;_.c=false;_.d=null;_.e=null;function jd(){jd=yK;var a;td=sb('[Ljava.lang.String;',127,1,['markboland05','Hollie Voss','boticario','Emerson Milton','Healy Colette','Brigitte Cobb','Elba Lockhart','Claudio Engle','Dena Pacheco','Brasil s.p','Parker','derbvktqsr','qetlyxxogg','antenas.sul','Christina Blake','Gail Horton','Orville Daniel','PostMaster','Rae Childers','Buster misjenou','user31065','ftsgeolbx','aqlovikigd','user18411','Mildred Starnes','Candice Carson','Louise Kelchner','Emilio Hutchinson','Geneva Underwood','Residence Oper?','fpnztbwag','tiger','Heriberto Rush','bulrush Bouchard','Abigail Louis','Chad Andrews','bjjycpaa','Terry English','Bell Snedden','huang','hhh','(unknown sender)','Kent','Dirk Newman','Equipe Virtual Cards','wishesundmore','Benito Meeks']);md=sb('[Ljava.lang.String;',127,1,['mark@example.com','hollie@example.com','boticario@example.com','emerson@example.com','healy@example.com','brigitte@example.com','elba@example.com','claudio@example.com','dena@example.com','brasilsp@example.com','parker@example.com','derbvktqsr@example.com','qetlyxxogg@example.com','antenas_sul@example.com','cblake@example.com','gailh@example.com','orville@example.com','post_master@example.com','rchilders@example.com','buster@example.com','user31065@example.com','ftsgeolbx@example.com','aqlovikigd@example.com','user18411@example.com','mildred@example.com','candice@example.com','louise_kelchner@example.com','emilio@example.com','geneva@example.com','residence_oper@example.com','fpnztbwag@example.com','tiger@example.com','heriberto@example.com','bulrush@example.com','abigail_louis@example.com','chada@example.com','bjjycpaa@example.com','terry@example.com','bell@example.com','huang@example.com','hhh@example.com','kent@example.com','newman@example.com','equipe_virtual@example.com','wishesundmore@example.com','benito@example.com']);vd=sb('[Ljava.lang.String;',127,1,['URGENT -[Mon, 24 Apr 2006 02:17:27 +0000]','URGENT TRANSACTION -[Sun, 23 Apr 2006 13:10:03 +0000]','fw: Here it comes','voce ganho um vale presente Boticario','Read this ASAP','Hot Stock Talk','New Breed of Equity Trader','FWD: TopWeeks the wire special pr news release','[fwd] Read this ASAP','Renda Extra R$1.000,00-R$2.000,00/m?s','re: Make sure your special pr news released','Forbidden Knowledge Conference','decodificadores os menores pre?os','re: Our Pick','RE: The hottest pick Watcher','RE: St0kkMarrkett Picks Trade watch special pr news release','St0kkMarrkett Picks Watch special pr news release news','You are a Winner oskoxmshco','Encrypted E-mail System (VIRUS REMOVED)','Fw: Malcolm','Secure Message System (VIRUS REMOVED)','fwd: St0kkMarrkett Picks Watch special pr news releaser','FWD: Financial Market Traderr special pr news release','? s? uma dica r?pida !!!!! leia !!!','re: You have to heard this','fwd: Watcher TopNews','VACANZE alle Mauritius','funny','re: You need to review this','[re:] Our Pick','RE: Before the be11 special pr news release','[re:] Market TradePicks Trade watch news','No prescription needed','Seu novo site','[fwd] Financial Market Trader Picker','FWD: Top Financial Market Specialists Trader interest increases','Os cart?es mais animados da web!!','We will sale 4 you cebtdbwtcv','RE: Best Top Financial Market Specialists Trader Picks']);od=sb('[Ljava.lang.String;',127,1,['Dear Friend,<br><br>I am Mr. Mark Boland the Bank Manager of ABN AMRO BANK 101 Moorgate, London, EC2M 6SB.<br><br>','I have an urgent and very confidential business proposition for you. On July 20, 2001; Mr. Zemenu Gente, a National of France, who used to be a private contractor with the Shell Petroleum Development Company in Saudi Arabia. Mr. Zemenu Gente Made a Numbered time (Fixed deposit) for 36 calendar months, valued at GBP?30, 000,000.00 (Thirty Million Pounds only) in my Branch.','I have all necessary legal documents that can be used to back up any claim we may make. All I require is your honest Co-operation, Confidentiality and A trust to enable us sees this transaction through. I guarantee you that this will be executed under a legitimate arrangement that will protect you from any breach of the law. Please get in touch with me urgently by E-mail and Provide me with the following;<br>','The OIL sector is going crazy. This is our weekly gift to you!<br><br>Get KKPT First Thing, This Is Going To Run!<br><br>Check out Latest NEWS!<br><br>KOKO PETROLEUM (KKPT) - This is our #1 pick for next week!<br>Our last pick gained $2.16 in 4 days of trading.<br>','LAS VEGAS, NEVADA--(MARKET WIRE)--Apr 6, 2006 -- KOKO Petroleum, Inc. (Other OTC:KKPT.PK - News) -<br>KOKO Petroleum, Inc. announced today that its operator for the Corsicana Field, JMT Resources, Ltd. ("JMT") will commence a re-work program on its Pecan Gap wells in the next week. The re-work program will consist of drilling six lateral bore production strings from the existing well bore. This process, known as Radial Jet Enhancement, will utilize high pressure fluids to drill the lateral well bores, which will extend out approximately 350\' each.','JMT has contracted with Well Enhancement Services, LLC (www.wellenhancement.com) to perform the rework on its Pierce nos. 14 and 14a. A small sand frac will follow the drilling of the lateral well bores in order to enhance permeability and create larger access to the Pecan Gap reservoir. Total cost of the re-work per well is estimated to be approximately $50,000 USD.','Parab?ns!<br>Voc? Ganhou Um Vale Presente da Botic?rio no valor de R$50,00<br>Voc? foi contemplado na Promo??o Respeite Minha Natureza - Pulseira Social.<br>Algu?m pode t?-lo inscrito na promo??o! (Amigos(as), Namorado(a) etc.).<br>Para retirar o seu pr?mio em uma das nossas Lojas, fa?a o download do Vale-Presente abaixo.<br>Ap?s o download, com o arquivo previamente salvo, imprima uma folha e salve a c?pia em seu computador para evitar transtornos decorrentes da perda do mesmo. Lembramos que o Vale-Presente ? ?nico e intransfer?vel.','Large Marketing Campaign running this weekend!<br><br>Should you get in today before it explodes?<br><br>This Will Fly Starting Monday!','PREMIER INFORMATION (PIFR)<br>A U.S. based company offers specialized information management serices to both the Insurance and Healthcare Industries. The services we provide are specific to each industry and designed for quick response and maximum security.<br><br>STK- PIFR<br>Current Price: .20<br>This one went to $2.80 during the last marketing Campaign!','These partnerships specifically allow Premier to obtain personal health information, as governed by the Health In-surancee Portability and Accountability Act of 1996 (HIPAA), and other applicable state laws and regulations.<br><br>Global HealthCare Market Undergoing Digital Conversion','>>   Componentes e decodificadores; confira aqui;<br> http://br.geocities.com/listajohn/index.htm<br>','THE GOVERNING AWARD<br>NETHERLANDS HEAD OFFICE<br>AC 76892 HAUITSOP<br>AMSTERDAM, THE NETHERLANDS.<br>FROM: THE DESK OF THE PROMOTIONS MANAGER.<br>INTERNATIONAL PROMOTIONS / PRIZE AWARD DEPARTMENT<br>REF NUMBER: 14235/089.<br>BATCH NUMBER: 304/64780/IFY.<br>RE/AWARD NOTIFICATION<br>',"We are pleased to inform you of the announcement today 13th of April 2006, you among TWO LUCKY WINNERS WON the GOVERNING AWARD draw held on the 28th of March 2006. The THREE Winning Addresses were randomly selected from a batch of 10,000,000 international email addresses. Your email address emerged alongside TWO others as a category B winner in this year's Annual GOVERNING AWARD Draw.<br>",'>> obrigado por me dar esta pequena aten??o !!!<br>CASO GOSTE DE ASSISTIR TV , MAS A SUA ANTENA S? PEGA AQUELES CANAIS LOCAIS  OU O SEU SISTEMA PAGO ? MUITO CARO , SAIBA QUE TENHO CART?ES DE ACESSO PARA SKY DIRECTV , E DECODERS PARA  NET TVA E TECSAT , TUDO GRATIS , SEM ASSINTURA , SEM MENSALIDADE, VC PAGA UMA VEZ S? E ASSISTE A MUITOS CANAIS , FILMES , JOGOS , PORNOS , DESENHOS , DOCUMENT?RIOS ,SHOWS , ETC,<br><br>CART?O SKY E DIRECTV TOTALMENTE HACKEADOS  350,00<br>DECODERS NET TVA DESBLOQUEADOS                       390,00<br>KITS COMPLETOS SKY OU DTV ANTENA DECODER E CART?O  650,00<br>TECSAT FREE   450,00<br>TENHO TB ACESS?RIOS , CABOS, LNB .<br>','********************************************************************<br> Original filename: mail.zip<br> Virus discovered: JS.Feebs.AC<br>********************************************************************<br> A file that was attached to this email contained a virus.<br> It is very likely that the original message was generated<br> by the virus and not a person - treat this message as you would<br> any other junk mail (spam).<br> For more information on why you received this message please visit:<br>','Put a few letters after your name. Let us show you how you can do it in just a few days.<br><br>http://thewrongchoiceforyou.info<br><br>kill future mailing by pressing this : see main website',"We possess scores of pharmaceutical products handy<br>All med's are made in U.S. laboratories<br>For your wellbeing! Very rapid, protected and secure<br>Ordering, No script required. We have the pain aid you require<br>",'"Oh, don\'t speak to me of Austria. Perhaps I don\'t understand things, but Austria never has wished, and does not wish, for war. She is betraying us! Russia alone must save Europe. Our gracious sovereign recognizes his high vocation and will be true to it. That is the one thing I have faith in! Our good and wonderful sovereign has to perform the noblest role on earth, and he is so virtuous and noble that God will not forsake him. He will fulfill his vocation and crush the hydra of revolution, which has become more terrible than ever in the person of this murderer and villain! We alone must avenge the blood of the just one.... Whom, I ask you, can we rely on?... England with her commercial spirit will not and cannot understand the Emperor Alexander\'s loftiness of soul. She has refused to evacuate Malta. She wanted to find, and still seeks, some secret motive in our actions. What answer did Novosiltsev get? None. The English have not understood and cannot understand the self-ab!<br>negation of our Emperor who wants nothing for himself, but only desires the good of mankind. And what have they promised? Nothing! And what little they have promised they will not perform! Prussia has always declared that Buonaparte is invincible, and that all Europe is powerless before him.... And I don\'t believe a word that Hardenburg says, or Haugwitz either. This famous Prussian neutrality is just a trap. I have faith only in God and the lofty destiny of our adored monarch. He will save Europe!"<br>"Those were extremes, no doubt, but they are not what is most important. What is important are the rights of man, emancipation from prejudices, and equality of citizenship, and all these ideas Napoleon has retained in full force."']);rd=gI(new eI());{for(a=0;a<37;++a){hI(rd,kd());}}}
function kd(){jd();var a,b,c,d,e;d=td[sd++];if(sd==td.a){sd=0;}b=md[ld++];if(ld==md.a){ld=0;}e=vd[ud++];if(ud==vd.a){ud=0;}a='';for(c=0;c<10;++c){a+=od[nd++];if(nd==od.a){nd=0;}}return gd(new fd(),d,b,e,a);}
function qd(a){jd();if(a>=rd.b){return null;}return xb(lI(rd,a),4);}
function pd(){jd();return rd.b;}
var ld=0,md,nd=0,od,rd,sd=0,td,ud=0,vd;function xd(a){a.a=As(new oq());a.c=Cs(new oq(),"<a href='javascript:;'>&lt; newer<\/a>",true);a.d=Cs(new oq(),"<a href='javascript:;'>older &gt;<\/a>",true);a.g=Cp(new xp());a.b=vt(new tt());}
function yd(b){var a;xd(b);rs(b.g,0);qs(b.g,0);b.g.fc('100%');cs(b.g,b);zu(b.c,b);zu(b.d,b);a=vt(new tt());nA(b.b,'mail-ListNavBar');wt(a,b.c);wt(a,b.a);wt(a,b.d);zt(b.b,(ft(),it));wt(b.b,a);b.b.fc('100%');jo(b,b.g);nA(b,'mail-List');Ad(b);Dd(b);return b;}
function Ad(b){var a;vs(b.g,0,0,'Sender');vs(b.g,0,1,'Email');vs(b.g,0,2,'Subject');ws(b.g,0,3,b.b);qr(b.g.d,0,'mail-ListHeader');for(a=0;a<10;++a){vs(b.g,a+1,0,'');vs(b.g,a+1,1,'');vs(b.g,a+1,2,'');er(b.g.b,a+1,0,false);er(b.g.b,a+1,1,false);er(b.g.b,a+1,2,false);Bp(b.g.b,a+1,2,2);}}
function Bd(c,b){var a;a=qd(c.f+b);if(a===null){return;}Cd(c,c.e,false);Cd(c,b,true);a.c=true;c.e=b;de((ce(),Ce),a);}
function Cd(c,a,b){if(a!=(-1)){if(b){lr(c.g.d,a+1,'mail-SelectedRow');}else{pr(c.g.d,a+1,'mail-SelectedRow');}}}
function Dd(e){var a,b,c,d;a=pd();d=e.f+10;if(d>a){d=a;}e.c.dc(e.f!=0);e.d.dc(e.f+10<a);Cu(e.a,''+(e.f+1)+' - '+d+' of '+a);b=0;for(;b<10;++b){if(e.f+b>=pd()){break;}c=qd(e.f+b);vs(e.g,b+1,0,c.d);vs(e.g,b+1,1,c.b);vs(e.g,b+1,2,c.e);}for(;b<10;++b){ts(e.g,b+1,0,'&nbsp;');ts(e.g,b+1,1,'&nbsp;');ts(e.g,b+1,2,'&nbsp;');}if(e.e==(-1)){Bd(e,0);}}
function Ed(c,b,a){if(b>0){Bd(this,b-1);}}
function Fd(a){if(a===this.d){this.f+=10;if(this.f>=pd()){this.f-=10;}else{Cd(this,this.e,false);this.e=(-1);Dd(this);}}else if(a===this.c){this.f-=10;if(this.f<0){this.f=0;}else{Cd(this,this.e,false);this.e=(-1);Dd(this);}}}
function wd(){}
_=wd.prototype=new go();_.lb=Ed;_.mb=Fd;_.tN=BK+'MailList';_.tI=28;_.e=(-1);_.f=0;function ie(){ie=yK;je=u()+'B9DA8B0768BAD7283674A8E1D92AD03D.cache.png';ke=uC(new tC(),je,0,0,32,32);le=uC(new tC(),je,32,0,32,32);me=uC(new tC(),je,64,0,16,16);ne=uC(new tC(),je,80,0,16,16);oe=uC(new tC(),je,96,0,16,16);pe=uC(new tC(),je,112,0,4,4);qe=uC(new tC(),je,116,0,140,75);re=uC(new tC(),je,256,0,32,32);se=uC(new tC(),je,288,0,4,4);te=uC(new tC(),je,292,0,16,16);ue=uC(new tC(),je,308,0,32,32);ve=uC(new tC(),je,340,0,16,16);we=uC(new tC(),je,356,0,16,16);xe=uC(new tC(),je,372,0,16,16);ye=uC(new tC(),je,388,0,16,16);ze=uC(new tC(),je,404,0,16,16);}
function he(a){ie();return a;}
function ge(){}
_=ge.prototype=new bF();_.tN=BK+'Mail_Images_generatedBundle';_.tI=29;var je,ke,le,me,ne,oe,pe,qe,re,se,te,ue,ve,we,xe,ye,ze;function Fe(c,a){var b;c.a=mz(new ny(),a);b=zy(new wy(),cf(c,(ie(),ne),'foo@example.com'));nz(c.a,b);af(c,b,'Inbox',(ie(),oe));af(c,b,'Drafts',(ie(),me));af(c,b,'Templates',(ie(),ve));af(c,b,'Sent',(ie(),te));af(c,b,'Trash',(ie(),we));dz(b,true);jo(c,c.a);return c;}
function af(d,c,e,a){var b;b=zy(new wy(),cf(d,a,e));c.s(b);return b;}
function cf(b,a,c){return '<span>'+yC(a)+c+'<\/span>';}
function De(){}
_=De.prototype=new go();_.tN=BK+'Mailboxes';_.tI=30;_.a=null;function kf(a){a.b=ff(new ef(),a);}
function lf(b,a){kf(b);mf(b,a,Fe(new De(),a),(ie(),re),'Mail');mf(b,a,tf(new sf()),(ie(),ue),'Tasks');mf(b,a,wc(new jc(),a),(ie(),ke),'Contacts');jo(b,b.b);return b;}
function mf(d,c,e,b,a){eA(e,'mail-StackContent');Cx(d.b,e,pf(d,c,b,a),true);}
function of(b,a){return 'header-'+b.hC()+'-'+a;}
function pf(g,e,d,a){var b,c,f;f=g.a==0;c=of(g,g.a);g.a++;b="<table class='caption' cellpadding='0' cellspacing='0'><tr><td class='lcaption'>"+yC(d)+"<\/td><td class='rcaption'><b style='white-space:nowrap'>"+a+'<\/b><\/td><\/tr><\/table>';return "<table id='"+c+"' align='left' cellpadding='0' cellspacing='0'"+(f?" class='is-top'":'')+'><tbody>'+"<tr><td class='box-00'>"+yC((ie(),pe))+'<\/td>'+"<td class='box-10'>&nbsp;<\/td>"+"<td class='box-20'>"+yC((ie(),se))+'<\/td>'+'<\/tr><tr>'+"<td class='box-01'>&nbsp;<\/td>"+"<td class='box-11'>"+b+'<\/td>'+"<td class='box-21'>&nbsp;<\/td>"+'<\/tr><\/tbody><\/table>';}
function qf(d,c,b){var a;c++;if(c>0&&c<d.b.f.b){a=di(of(d,c));xi(a,'className','');}b++;if(b>0&&b<d.b.f.b){a=di(of(d,b));xi(a,'className','is-beneath-selected');}}
function rf(){ey(this.b,0);qf(this,(-1),0);}
function df(){}
_=df.prototype=new go();_.qb=rf;_.tN=BK+'Shortcuts';_.tI=31;_.a=0;function zn(a){a.f=kB(new cB(),a);}
function An(a){zn(a);return a;}
function Bn(c,a,b){bC(a);lB(c.f,a);ah(b,a.E());kv(c,a);}
function Cn(d,b,a){var c;Dn(d,a);if(b.q===d){c=Fn(d,b);if(c<a){a--;}}return a;}
function Dn(b,a){if(a<0||a>b.f.b){throw new fE();}}
function ao(b,a){return nB(b.f,a);}
function Fn(b,a){return oB(b.f,a);}
function bo(e,b,c,a,d){a=Cn(e,b,a);bC(b);pB(e.f,b,a);if(d){li(c,io(b),a);}else{ah(c,io(b));}kv(e,b);}
function co(b,c){var a;if(c.q!==b){return false;}mv(b,c);a=c.E();pi(ji(a),a);sB(b.f,c);return true;}
function eo(){return qB(this.f);}
function fo(a){return co(this,a);}
function yn(){}
_=yn.prototype=new jv();_.gb=eo;_.Cb=fo;_.tN=EK+'ComplexPanel';_.tI=32;function Ax(b){var a;An(b);a=mh();b.Fb(a);b.b=jh();ah(a,b.b);wi(a,'cellSpacing',0);wi(a,'cellPadding',0);Di(a,1);nA(b,'gwt-StackPanel');return b;}
function Bx(a,b){Fx(a,b,a.f.b);}
function Cx(c,d,b,a){Bx(c,d);cy(c,c.f.b-1,b,a);}
function Ex(d,a){var b,c;while(a!==null&& !bh(a,d.E())){b=gi(a,'__index');if(b!==null){c=fi(a,'__owner');if(c==d.hC()){return mE(b);}else{return (-1);}}a=ji(a);}return (-1);}
function Fx(e,h,a){var b,c,d,f,g;g=lh();d=kh();ah(g,d);f=lh();c=kh();ah(f,c);a=Cn(e,h,a);b=a*2;li(e.b,f,b);li(e.b,g,b);vA(d,'gwt-StackPanelItem',true);wi(d,'__owner',e.hC());xi(d,'height','1px');xi(c,'height','100%');xi(c,'vAlign','top');bo(e,h,c,a,false);fy(e,a);if(e.c==(-1)){ey(e,0);}else{dy(e,a,false);if(e.c>=a){++e.c;}}}
function ay(d,a){var b,c;if(Ch(a)==1){c=Ah(a);b=Ex(d,c);if(b!=(-1)){ey(d,b);}}}
function by(e,a,b){var c,d,f;c=co(e,a);if(c){d=2*b;f=ci(e.b,d);pi(e.b,f);f=ci(e.b,d);pi(e.b,f);if(e.c==b){e.c=(-1);}else if(e.c>b){--e.c;}fy(e,d);}return c;}
function cy(e,b,d,a){var c;if(b>=e.f.b){return;}c=ci(ci(e.b,b*2),0);if(a){zi(c,d);}else{Ai(c,d);}}
function dy(c,a,e){var b,d;d=ci(c.b,a*2);if(d===null){return;}b=ii(d);vA(b,'gwt-StackPanelItem-selected',e);d=ci(c.b,a*2+1);wA(d,e);ao(c,a).dc(e);}
function ey(b,a){if(a>=b.f.b||a==b.c){return;}if(b.c>=0){dy(b,b.c,false);}b.c=a;dy(b,b.c,true);}
function fy(f,a){var b,c,d,e;for(e=a,b=f.f.b;e<b;++e){d=ci(f.b,e*2);c=ii(d);wi(c,'__index',e);}}
function gy(a){ay(this,a);}
function hy(a){return by(this,a,Fn(this,a));}
function zx(){}
_=zx.prototype=new yn();_.kb=gy;_.Cb=hy;_.tN=EK+'StackPanel';_.tI=33;_.b=null;_.c=(-1);function ff(b,a){b.a=a;Ax(b);return b;}
function hf(a){var b,c;c=this.c;ay(this,a);b=this.c;if(c!=b){qf(this.a,c,b);}}
function ef(){}
_=ef.prototype=new zx();_.kb=hf;_.tN=BK+'Shortcuts$1';_.tI=34;function tf(c){var a,b;b=gx(new Fw());a=BA(new zA());b.ec(a);CA(a,kn(new gn(),'Get groceries'));CA(a,kn(new gn(),'Walk the dog'));CA(a,kn(new gn(),'Start Web 2.0 company'));CA(a,kn(new gn(),'Write cool app in GWT'));CA(a,kn(new gn(),'Get funding'));CA(a,kn(new gn(),'Take a vacation'));jo(c,b);nA(c,'mail-Tasks');return c;}
function sf(){}
_=sf.prototype=new go();_.tN=BK+'Tasks';_.tI=35;function xf(a){a.b=Bs(new oq(),"<a href='javascript:;'>Sign Out<\/a>");a.a=Bs(new oq(),"<a href='javascript:;'>About<\/a>");}
function yf(f,a){var b,c,d,e;xf(f);e=vt(new tt());b=BA(new zA());zt(e,(ft(),it));FA(b,(ft(),it));c=vt(new tt());en(c,4);wt(c,f.b);wt(c,f.a);d=xC((ie(),qe));wt(e,d);e.Eb(d,(ft(),ht));wt(e,b);CA(b,Bs(new oq(),'<b>Welcome back, foo@example.com<\/b>'));CA(b,c);zu(f.b,f);zu(f.a,f);jo(f,e);nA(f,'mail-TopPanel');nA(c,'mail-TopPanelLinks');return f;}
function Af(b){var a;if(b===this.b){fk('If this were implemented, you would be signed out now.');}else if(b===this.a){a=gc(new bc());cw(a);wv(a);}}
function vf(){}
_=vf.prototype=new go();_.mb=Af;_.tN=BK+'TopPanel';_.tI=36;function CF(b,a){a;return b;}
function BF(){}
_=BF.prototype=new bF();_.tN=aL+'Throwable';_.tI=3;function DD(b,a){CF(b,a);return b;}
function CD(){}
_=CD.prototype=new BF();_.tN=aL+'Exception';_.tI=4;function gF(b,a){DD(b,a);return b;}
function fF(){}
_=fF.prototype=new CD();_.tN=aL+'RuntimeException';_.tI=5;function Cf(b,a){return b;}
function Bf(){}
_=Bf.prototype=new fF();_.tN=CK+'CommandCanceledException';_.tI=37;function sg(a){a.a=ag(new Ff(),a);a.b=gI(new eI());a.d=eg(new dg(),a);a.f=ig(new hg(),a);}
function tg(a){sg(a);return a;}
function vg(c){var a,b,d;a=kg(c.f);ng(c.f);b=null;if(yb(a,5)){b=Cf(new Bf(),xb(a,5));}else{}if(b!==null){d=w;}yg(c,false);xg(c);}
function wg(e,d){var a,b,c,f;f=false;try{yg(e,true);og(e.f,e.b.b);yj(e.a,10000);while(lg(e.f)){b=mg(e.f);c=true;try{if(b===null){return;}if(yb(b,5)){a=xb(b,5);a.B();}else{}}finally{f=pg(e.f);if(f){return;}if(c){ng(e.f);}}if(Bg(zF(),d)){return;}}}finally{if(!f){vj(e.a);yg(e,false);xg(e);}}}
function xg(a){if(!oI(a.b)&& !a.e&& !a.c){zg(a,true);yj(a.d,1);}}
function yg(b,a){b.c=a;}
function zg(b,a){b.e=a;}
function Ag(b,a){hI(b.b,a);xg(b);}
function Bg(a,b){return qE(a-b)>=100;}
function Ef(){}
_=Ef.prototype=new bF();_.tN=CK+'CommandExecutor';_.tI=38;_.c=false;_.e=false;function wj(){wj=yK;Ej=gI(new eI());{Dj();}}
function uj(a){wj();return a;}
function vj(a){if(a.b){zj(a.c);}else{Aj(a.c);}qI(Ej,a);}
function xj(a){if(!a.b){qI(Ej,a);}a.Db();}
function yj(b,a){if(a<=0){throw aE(new FD(),'must be positive');}vj(b);b.b=false;b.c=Bj(b,a);hI(Ej,b);}
function zj(a){wj();$wnd.clearInterval(a);}
function Aj(a){wj();$wnd.clearTimeout(a);}
function Bj(b,a){wj();return $wnd.setTimeout(function(){b.C();},a);}
function Cj(){var a;a=w;{xj(this);}}
function Dj(){wj();dk(new qj());}
function pj(){}
_=pj.prototype=new bF();_.C=Cj;_.tN=CK+'Timer';_.tI=39;_.b=false;_.c=0;var Ej;function bg(){bg=yK;wj();}
function ag(b,a){bg();b.a=a;uj(b);return b;}
function cg(){if(!this.a.c){return;}vg(this.a);}
function Ff(){}
_=Ff.prototype=new pj();_.Db=cg;_.tN=CK+'CommandExecutor$1';_.tI=40;function fg(){fg=yK;wj();}
function eg(b,a){fg();b.a=a;uj(b);return b;}
function gg(){zg(this.a,false);wg(this.a,zF());}
function dg(){}
_=dg.prototype=new pj();_.Db=gg;_.tN=CK+'CommandExecutor$2';_.tI=41;function ig(b,a){b.d=a;return b;}
function kg(a){return lI(a.d.b,a.b);}
function lg(a){return a.c<a.a;}
function mg(b){var a;b.b=b.c;a=lI(b.d.b,b.c++);if(b.c>=b.a){b.c=0;}return a;}
function ng(a){pI(a.d.b,a.b);--a.a;if(a.b<=a.c){if(--a.c<0){a.c=0;}}a.b=(-1);}
function og(b,a){b.a=a;}
function pg(a){return a.b==(-1);}
function qg(){return lg(this);}
function rg(){return mg(this);}
function hg(){}
_=hg.prototype=new bF();_.eb=qg;_.ib=rg;_.tN=CK+'CommandExecutor$CircularIterator';_.tI=42;_.a=0;_.b=(-1);_.c=0;function Eg(){Eg=yK;si=gI(new eI());{ki=new wk();kl(ki);}}
function Fg(a){Eg();hI(si,a);}
function ah(b,a){Eg();ql(ki,b,a);}
function bh(a,b){Eg();return al(ki,a,b);}
function ch(){Eg();return sl(ki,'button');}
function dh(){Eg();return sl(ki,'div');}
function eh(a){Eg();return sl(ki,a);}
function fh(){Eg();return sl(ki,'img');}
function gh(){Eg();return tl(ki,'checkbox');}
function hh(){Eg();return sl(ki,'label');}
function ih(){Eg();return sl(ki,'span');}
function jh(){Eg();return sl(ki,'tbody');}
function kh(){Eg();return sl(ki,'td');}
function lh(){Eg();return sl(ki,'tr');}
function mh(){Eg();return sl(ki,'table');}
function ph(b,a,d){Eg();var c;c=w;{oh(b,a,d);}}
function oh(b,a,c){Eg();var d;if(a===ri){if(Ch(b)==8192){ri=null;}}d=nh;nh=b;try{c.kb(b);}finally{nh=d;}}
function qh(b,a){Eg();ul(ki,b,a);}
function rh(a){Eg();return vl(ki,a);}
function sh(a){Eg();return yk(ki,a);}
function th(a){Eg();return zk(ki,a);}
function uh(a){Eg();return wl(ki,a);}
function vh(a){Eg();return xl(ki,a);}
function wh(a){Eg();return bl(ki,a);}
function xh(a){Eg();return yl(ki,a);}
function yh(a){Eg();return zl(ki,a);}
function zh(a){Eg();return Al(ki,a);}
function Ah(a){Eg();return cl(ki,a);}
function Bh(a){Eg();return dl(ki,a);}
function Ch(a){Eg();return Bl(ki,a);}
function Dh(a){Eg();el(ki,a);}
function Eh(a){Eg();return Ak(ki,a);}
function Fh(a){Eg();return Bk(ki,a);}
function ci(b,a){Eg();return hl(ki,b,a);}
function ai(a){Eg();return fl(ki,a);}
function bi(b,a){Eg();return gl(ki,b,a);}
function di(a){Eg();return Cl(ki,a);}
function gi(a,b){Eg();return Fl(ki,a,b);}
function ei(a,b){Eg();return Dl(ki,a,b);}
function fi(a,b){Eg();return El(ki,a,b);}
function hi(a){Eg();return am(ki,a);}
function ii(a){Eg();return il(ki,a);}
function ji(a){Eg();return jl(ki,a);}
function li(c,a,b){Eg();ll(ki,c,a,b);}
function mi(b,a){Eg();return ml(ki,b,a);}
function ni(a){Eg();var b,c;c=true;if(si.b>0){b=xb(lI(si,si.b-1),6);if(!(c=b.ob(a))){qh(a,true);Dh(a);}}return c;}
function oi(a){Eg();if(ri!==null&&bh(a,ri)){ri=null;}nl(ki,a);}
function pi(b,a){Eg();bm(ki,b,a);}
function qi(a){Eg();qI(si,a);}
function ti(a){Eg();cm(ki,a);}
function ui(a){Eg();ri=a;ol(ki,a);}
function xi(a,b,c){Eg();fm(ki,a,b,c);}
function vi(a,b,c){Eg();dm(ki,a,b,c);}
function wi(a,b,c){Eg();em(ki,a,b,c);}
function yi(a,b){Eg();gm(ki,a,b);}
function zi(a,b){Eg();hm(ki,a,b);}
function Ai(a,b){Eg();im(ki,a,b);}
function Bi(b,a,c){Eg();jm(ki,b,a,c);}
function Ci(b,a,c){Eg();km(ki,b,a,c);}
function Di(a,b){Eg();pl(ki,a,b);}
function Ei(){Eg();return Ck(ki);}
function Fi(){Eg();return Dk(ki);}
var nh=null,ki=null,ri=null,si;function bj(){bj=yK;dj=tg(new Ef());}
function cj(a){bj();if(a===null){throw vE(new uE(),'cmd can not be null');}Ag(dj,a);}
var dj;function gj(b,a){if(yb(a,7)){return bh(b,xb(a,7));}return ab(Eb(b,ej),a);}
function hj(a){return gj(this,a);}
function ij(){return bb(Eb(this,ej));}
function ej(){}
_=ej.prototype=new E();_.eQ=hj;_.hC=ij;_.tN=CK+'Element';_.tI=43;function nj(a){return ab(Eb(this,jj),a);}
function oj(){return bb(Eb(this,jj));}
function jj(){}
_=jj.prototype=new E();_.eQ=nj;_.hC=oj;_.tN=CK+'Event';_.tI=44;function sj(){while((wj(),Ej).b>0){vj(xb(lI((wj(),Ej),0),8));}}
function tj(){return null;}
function qj(){}
_=qj.prototype=new bF();_.xb=sj;_.yb=tj;_.tN=CK+'Timer$1';_.tI=45;function ck(){ck=yK;gk=gI(new eI());tk=gI(new eI());{pk();}}
function dk(a){ck();hI(gk,a);}
function ek(a){ck();hI(tk,a);}
function fk(a){ck();$wnd.alert(a);}
function hk(a){ck();$doc.body.style.overflow=a?'auto':'hidden';}
function ik(){ck();var a,b;for(a=sG(gk);lG(a);){b=xb(mG(a),9);b.xb();}}
function jk(){ck();var a,b,c,d;d=null;for(a=sG(gk);lG(a);){b=xb(mG(a),9);c=b.yb();{d=c;}}return d;}
function kk(){ck();var a,b;for(a=sG(tk);lG(a);){b=xb(mG(a),10);b.zb(mk(),lk());}}
function lk(){ck();return Ei();}
function mk(){ck();return Fi();}
function nk(){ck();return $doc.documentElement.scrollLeft||$doc.body.scrollLeft;}
function ok(){ck();return $doc.documentElement.scrollTop||$doc.body.scrollTop;}
function pk(){ck();__gwt_initHandlers(function(){sk();},function(){return rk();},function(){qk();$wnd.onresize=null;$wnd.onbeforeclose=null;$wnd.onclose=null;});}
function qk(){ck();var a;a=w;{ik();}}
function rk(){ck();var a;a=w;{return jk();}}
function sk(){ck();var a;a=w;{kk();}}
function uk(a){ck();$doc.body.style.margin=a;}
var gk,tk;function ql(c,b,a){b.appendChild(a);}
function sl(b,a){return $doc.createElement(a);}
function tl(b,c){var a=$doc.createElement('INPUT');a.type=c;return a;}
function ul(c,b,a){b.cancelBubble=a;}
function vl(b,a){return !(!a.altKey);}
function wl(b,a){return !(!a.ctrlKey);}
function xl(b,a){return a.currentTarget;}
function yl(b,a){return a.which||(a.keyCode|| -1);}
function zl(b,a){return !(!a.metaKey);}
function Al(b,a){return !(!a.shiftKey);}
function Bl(b,a){switch(a.type){case 'blur':return 4096;case 'change':return 1024;case 'click':return 1;case 'dblclick':return 2;case 'focus':return 2048;case 'keydown':return 128;case 'keypress':return 256;case 'keyup':return 512;case 'load':return 32768;case 'losecapture':return 8192;case 'mousedown':return 4;case 'mousemove':return 64;case 'mouseout':return 32;case 'mouseover':return 16;case 'mouseup':return 8;case 'scroll':return 16384;case 'error':return 65536;case 'mousewheel':return 131072;case 'DOMMouseScroll':return 131072;}}
function Cl(c,b){var a=$doc.getElementById(b);return a||null;}
function Fl(d,a,b){var c=a[b];return c==null?null:String(c);}
function Dl(c,a,b){return !(!a[b]);}
function El(d,a,c){var b=parseInt(a[c]);if(!b){return 0;}return b;}
function am(b,a){return a.__eventBits||0;}
function bm(c,b,a){b.removeChild(a);}
function cm(g,b){var d=b.offsetLeft,h=b.offsetTop;var i=b.offsetWidth,c=b.offsetHeight;if(b.parentNode!=b.offsetParent){d-=b.parentNode.offsetLeft;h-=b.parentNode.offsetTop;}var a=b.parentNode;while(a&&a.nodeType==1){if(a.style.overflow=='auto'||(a.style.overflow=='scroll'||a.tagName=='BODY')){if(d<a.scrollLeft){a.scrollLeft=d;}if(d+i>a.scrollLeft+a.clientWidth){a.scrollLeft=d+i-a.clientWidth;}if(h<a.scrollTop){a.scrollTop=h;}if(h+c>a.scrollTop+a.clientHeight){a.scrollTop=h+c-a.clientHeight;}}var e=a.offsetLeft,f=a.offsetTop;if(a.parentNode!=a.offsetParent){e-=a.parentNode.offsetLeft;f-=a.parentNode.offsetTop;}d+=e-a.scrollLeft;h+=f-a.scrollTop;a=a.parentNode;}}
function fm(c,a,b,d){a[b]=d;}
function dm(c,a,b,d){a[b]=d;}
function em(c,a,b,d){a[b]=d;}
function gm(c,a,b){a.__listener=b;}
function hm(c,a,b){if(!b){b='';}a.innerHTML=b;}
function im(c,a,b){while(a.firstChild){a.removeChild(a.firstChild);}if(b!=null){a.appendChild($doc.createTextNode(b));}}
function jm(c,b,a,d){b.style[a]=d;}
function km(c,b,a,d){b.style[a]=d;}
function vk(){}
_=vk.prototype=new bF();_.tN=DK+'DOMImpl';_.tI=46;function al(c,a,b){return a==b;}
function bl(b,a){return a.relatedTarget?a.relatedTarget:null;}
function cl(b,a){return a.target||null;}
function dl(b,a){return a.relatedTarget||null;}
function el(b,a){a.preventDefault();}
function hl(f,c,d){var b=0,a=c.firstChild;while(a){var e=a.nextSibling;if(a.nodeType==1){if(d==b)return a;++b;}a=e;}return null;}
function fl(d,c){var b=0,a=c.firstChild;while(a){if(a.nodeType==1)++b;a=a.nextSibling;}return b;}
function gl(d,c,e){var b=0,a=c.firstChild;while(a){if(a==e)return b;if(a.nodeType==1)++b;a=a.nextSibling;}return -1;}
function il(c,b){var a=b.firstChild;while(a&&a.nodeType!=1)a=a.nextSibling;return a||null;}
function jl(c,a){var b=a.parentNode;if(b==null){return null;}if(b.nodeType!=1)b=null;return b||null;}
function kl(d){$wnd.__dispatchCapturedMouseEvent=function(b){if($wnd.__dispatchCapturedEvent(b)){var a=$wnd.__captureElem;if(a&&a.__listener){ph(b,a,a.__listener);b.stopPropagation();}}};$wnd.__dispatchCapturedEvent=function(a){if(!ni(a)){a.stopPropagation();a.preventDefault();return false;}return true;};$wnd.addEventListener('click',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('dblclick',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousedown',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mouseup',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousemove',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousewheel',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('keydown',$wnd.__dispatchCapturedEvent,true);$wnd.addEventListener('keyup',$wnd.__dispatchCapturedEvent,true);$wnd.addEventListener('keypress',$wnd.__dispatchCapturedEvent,true);$wnd.__dispatchEvent=function(b){var c,a=this;while(a&& !(c=a.__listener))a=a.parentNode;if(a&&a.nodeType!=1)a=null;if(c)ph(b,a,c);};$wnd.__captureElem=null;}
function ll(f,e,g,d){var c=0,b=e.firstChild,a=null;while(b){if(b.nodeType==1){if(c==d){a=b;break;}++c;}b=b.nextSibling;}e.insertBefore(g,a);}
function ml(c,b,a){while(a){if(b==a){return true;}a=a.parentNode;if(a&&a.nodeType!=1){a=null;}}return false;}
function nl(b,a){if(a==$wnd.__captureElem)$wnd.__captureElem=null;}
function ol(b,a){$wnd.__captureElem=a;}
function pl(c,b,a){b.__eventBits=a;b.onclick=a&1?$wnd.__dispatchEvent:null;b.ondblclick=a&2?$wnd.__dispatchEvent:null;b.onmousedown=a&4?$wnd.__dispatchEvent:null;b.onmouseup=a&8?$wnd.__dispatchEvent:null;b.onmouseover=a&16?$wnd.__dispatchEvent:null;b.onmouseout=a&32?$wnd.__dispatchEvent:null;b.onmousemove=a&64?$wnd.__dispatchEvent:null;b.onkeydown=a&128?$wnd.__dispatchEvent:null;b.onkeypress=a&256?$wnd.__dispatchEvent:null;b.onkeyup=a&512?$wnd.__dispatchEvent:null;b.onchange=a&1024?$wnd.__dispatchEvent:null;b.onfocus=a&2048?$wnd.__dispatchEvent:null;b.onblur=a&4096?$wnd.__dispatchEvent:null;b.onlosecapture=a&8192?$wnd.__dispatchEvent:null;b.onscroll=a&16384?$wnd.__dispatchEvent:null;b.onload=a&32768?$wnd.__dispatchEvent:null;b.onerror=a&65536?$wnd.__dispatchEvent:null;b.onmousewheel=a&131072?$wnd.__dispatchEvent:null;}
function Ek(){}
_=Ek.prototype=new vk();_.tN=DK+'DOMImplStandard';_.tI=47;function yk(b,a){return a.pageX-$doc.body.scrollLeft|| -1;}
function zk(b,a){return a.pageY-$doc.body.scrollTop|| -1;}
function Ak(e,b){if(b.offsetLeft==null){return 0;}var c=0;var a=b.parentNode;if(a){while(a.offsetParent){c-=a.scrollLeft;a=a.parentNode;}}while(b){c+=b.offsetLeft;var d=b.offsetParent;if(d&&(d.tagName=='BODY'&&b.style.position=='absolute')){break;}b=d;}return c;}
function Bk(d,b){if(b.offsetTop==null){return 0;}var e=0;var a=b.parentNode;if(a){while(a.offsetParent){e-=a.scrollTop;a=a.parentNode;}}while(b){e+=b.offsetTop;var c=b.offsetParent;if(c&&(c.tagName=='BODY'&&b.style.position=='absolute')){break;}b=c;}return e;}
function Ck(a){return $wnd.innerHeight;}
function Dk(a){return $wnd.innerWidth;}
function wk(){}
_=wk.prototype=new Ek();_.tN=DK+'DOMImplSafari';_.tI=48;function mm(a){An(a);a.Fb(dh());Ci(a.E(),'position','relative');Ci(a.E(),'overflow','hidden');return a;}
function nm(a,b){Bn(a,b,a.E());}
function pm(b,c){var a;a=co(b,c);if(a){qm(c.E());}return a;}
function qm(a){Ci(a,'left','');Ci(a,'top','');Ci(a,'position','');}
function rm(a){return pm(this,a);}
function lm(){}
_=lm.prototype=new yn();_.Cb=rm;_.tN=EK+'AbsolutePanel';_.tI=49;function sm(){}
_=sm.prototype=new bF();_.tN=EK+'AbstractImagePrototype';_.tI=50;function kq(){kq=yK;lD(),nD;}
function iq(b,a){lD(),nD;lq(b,a);return b;}
function jq(b,a){if(b.c===null){b.c=un(new tn());}hI(b.c,a);}
function lq(b,a){cC(b,a);oA(b,7041);}
function mq(a){switch(Ch(a)){case 1:if(this.c!==null){wn(this.c,this);}break;case 4096:case 2048:break;case 128:case 512:case 256:break;}}
function nq(a){lq(this,a);}
function hq(){}
_=hq.prototype=new bB();_.kb=mq;_.Fb=nq;_.tN=EK+'FocusWidget';_.tI=51;_.c=null;function xm(){xm=yK;lD(),nD;}
function wm(b,a){lD(),nD;iq(b,a);return b;}
function ym(a){zi(this.E(),a);}
function vm(){}
_=vm.prototype=new hq();_.ac=ym;_.tN=EK+'ButtonBase';_.tI=52;function Cm(){Cm=yK;lD(),nD;}
function zm(a){lD(),nD;wm(a,ch());Dm(a.E());nA(a,'gwt-Button');return a;}
function Am(b,a){lD(),nD;zm(b);b.ac(a);return b;}
function Bm(c,a,b){lD(),nD;Am(c,a);jq(c,b);return c;}
function Dm(b){Cm();if(b.type=='submit'){try{b.setAttribute('type','button');}catch(a){}}}
function um(){}
_=um.prototype=new vm();_.tN=EK+'Button';_.tI=53;function Fm(a){An(a);a.e=mh();a.d=jh();ah(a.e,a.d);a.Fb(a.e);return a;}
function bn(a,b){if(b.q!==a){return null;}return ji(b.E());}
function cn(c,b,a){xi(b,'align',a.a);}
function dn(c,b,a){Ci(b,'verticalAlign',a.a);}
function en(b,a){wi(b.e,'cellSpacing',a);}
function fn(c,a){var b;b=bn(this,c);if(b!==null){cn(this,b,a);}}
function Em(){}
_=Em.prototype=new yn();_.Eb=fn;_.tN=EK+'CellPanel';_.tI=54;_.d=null;_.e=null;function ln(){ln=yK;lD(),nD;}
function hn(a){lD(),nD;jn(a,gh());nA(a,'gwt-CheckBox');return a;}
function kn(b,a){lD(),nD;hn(b);on(b,a);return b;}
function jn(b,a){var c;lD(),nD;wm(b,ih());b.a=a;b.b=hh();Di(b.a,hi(b.E()));Di(b.E(),0);ah(b.E(),b.a);ah(b.E(),b.b);c='check'+ ++sn;xi(b.a,'id',c);xi(b.b,'htmlFor',c);return b;}
function mn(b){var a;a=b.fb()?'checked':'defaultChecked';return ei(b.a,a);}
function nn(b,a){vi(b.a,'checked',a);vi(b.a,'defaultChecked',a);}
function on(b,a){Ai(b.b,a);}
function pn(){yi(this.a,this);}
function qn(){yi(this.a,null);nn(this,mn(this));}
function rn(a){zi(this.b,a);}
function gn(){}
_=gn.prototype=new vm();_.qb=pn;_.wb=qn;_.ac=rn;_.tN=EK+'CheckBox';_.tI=55;_.a=null;_.b=null;var sn=0;function cG(d,a,b){var c;while(a.eb()){c=a.ib();if(b===null?c===null:b.eQ(c)){return a;}}return null;}
function eG(a){throw FF(new EF(),'add');}
function fG(b){var a;a=cG(this,this.gb(),b);return a!==null;}
function gG(a){var b,c,d;d=this.gc();if(a.a<d){a=mb(a,d);}b=0;for(c=this.gb();c.eb();){tb(a,b++,c.ib());}if(a.a>d){tb(a,d,null);}return a;}
function bG(){}
_=bG.prototype=new bF();_.u=eG;_.w=fG;_.hc=gG;_.tN=bL+'AbstractCollection';_.tI=56;function rG(b,a){throw gE(new fE(),'Index: '+a+', Size: '+b.b);}
function sG(a){return jG(new iG(),a);}
function tG(b,a){throw FF(new EF(),'add');}
function uG(a){this.t(this.gc(),a);return true;}
function vG(e){var a,b,c,d,f;if(e===this){return true;}if(!yb(e,25)){return false;}f=xb(e,25);if(this.gc()!=f.gc()){return false;}c=sG(this);d=f.gb();while(lG(c)){a=mG(c);b=mG(d);if(!(a===null?b===null:a.eQ(b))){return false;}}return true;}
function wG(){var a,b,c,d;c=1;a=31;b=sG(this);while(lG(b)){d=mG(b);c=31*c+(d===null?0:d.hC());}return c;}
function xG(){return sG(this);}
function yG(a){throw FF(new EF(),'remove');}
function hG(){}
_=hG.prototype=new bG();_.t=tG;_.u=uG;_.eQ=vG;_.hC=wG;_.gb=xG;_.Bb=yG;_.tN=bL+'AbstractList';_.tI=57;function fI(a){{iI(a);}}
function gI(a){fI(a);return a;}
function hI(b,a){BI(b.a,b.b++,a);return true;}
function iI(a){a.a=cb();a.b=0;}
function kI(b,a){return mI(b,a)!=(-1);}
function lI(b,a){if(a<0||a>=b.b){rG(b,a);}return xI(b.a,a);}
function mI(b,a){return nI(b,a,0);}
function nI(c,b,a){if(a<0){rG(c,a);}for(;a<c.b;++a){if(wI(b,xI(c.a,a))){return a;}}return (-1);}
function oI(a){return a.b==0;}
function pI(c,a){var b;b=lI(c,a);zI(c.a,a,1);--c.b;return b;}
function qI(c,b){var a;a=mI(c,b);if(a==(-1)){return false;}pI(c,a);return true;}
function rI(d,a,b){var c;c=lI(d,a);BI(d.a,a,b);return c;}
function tI(a,b){if(a<0||a>this.b){rG(this,a);}sI(this.a,a,b);++this.b;}
function uI(a){return hI(this,a);}
function sI(a,b,c){a.splice(b,0,c);}
function vI(a){return kI(this,a);}
function wI(a,b){return a===b||a!==null&&a.eQ(b);}
function yI(a){return lI(this,a);}
function xI(a,b){return a[b];}
function AI(a){return pI(this,a);}
function zI(a,c,b){a.splice(c,b);}
function BI(a,b,c){a[b]=c;}
function CI(){return this.b;}
function DI(a){var b;if(a.a<this.b){a=mb(a,this.b);}for(b=0;b<this.b;++b){tb(a,b,xI(this.a,b));}if(a.a>this.b){tb(a,this.b,null);}return a;}
function eI(){}
_=eI.prototype=new hG();_.t=tI;_.u=uI;_.w=vI;_.cb=yI;_.Bb=AI;_.gc=CI;_.hc=DI;_.tN=bL+'ArrayList';_.tI=58;_.a=null;_.b=0;function un(a){gI(a);return a;}
function wn(d,c){var a,b;for(a=sG(d);lG(a);){b=xb(mG(a),11);b.mb(c);}}
function tn(){}
_=tn.prototype=new eI();_.tN=EK+'ClickListenerCollection';_.tI=59;function kp(){kp=yK;qp=new ap();rp=new ap();sp=new ap();tp=new ap();up=new ap();}
function hp(a){a.b=(ft(),ht);a.c=(ot(),qt);}
function ip(a){kp();Fm(a);hp(a);wi(a.e,'cellSpacing',0);wi(a.e,'cellPadding',0);return a;}
function jp(c,d,a){var b;if(a===qp){if(d===c.a){return;}else if(c.a!==null){throw aE(new FD(),'Only one CENTER widget may be added');}}bC(d);lB(c.f,d);if(a===qp){c.a=d;}b=dp(new cp(),a);dC(d,b);np(c,d,c.b);op(c,d,c.c);lp(c);kv(c,d);}
function lp(p){var a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,q;a=p.d;while(ai(a)>0){pi(a,ci(a,0));}l=1;d=1;for(h=qB(p.f);gB(h);){c=hB(h);e=c.p.a;if(e===sp||e===tp){++l;}else if(e===rp||e===up){++d;}}m=rb('[Lcom.google.gwt.user.client.ui.DockPanel$TmpRow;',[125],[21],[l],null);for(g=0;g<l;++g){m[g]=new fp();m[g].b=lh();ah(a,m[g].b);}q=0;f=d-1;j=0;n=l-1;b=null;for(h=qB(p.f);gB(h);){c=hB(h);i=c.p;o=kh();i.d=o;xi(i.d,'align',i.b);Ci(i.d,'verticalAlign',i.e);xi(i.d,'width',i.f);xi(i.d,'height',i.c);if(i.a===sp){li(m[j].b,o,m[j].a);ah(o,c.E());wi(o,'colSpan',f-q+1);++j;}else if(i.a===tp){li(m[n].b,o,m[n].a);ah(o,c.E());wi(o,'colSpan',f-q+1);--n;}else if(i.a===up){k=m[j];li(k.b,o,k.a++);ah(o,c.E());wi(o,'rowSpan',n-j+1);++q;}else if(i.a===rp){k=m[j];li(k.b,o,k.a);ah(o,c.E());wi(o,'rowSpan',n-j+1);--f;}else if(i.a===qp){b=o;}}if(p.a!==null){k=m[j];li(k.b,b,k.a);ah(b,p.a.E());}}
function mp(c,d,b){var a;a=d.p;a.c=b;if(a.d!==null){Ci(a.d,'height',a.c);}}
function np(c,d,a){var b;b=d.p;b.b=a.a;if(b.d!==null){xi(b.d,'align',b.b);}}
function op(c,d,a){var b;b=d.p;b.e=a.a;if(b.d!==null){Ci(b.d,'verticalAlign',b.e);}}
function pp(b,c,d){var a;a=c.p;a.f=d;if(a.d!==null){Ci(a.d,'width',a.f);}}
function vp(b){var a;a=co(this,b);if(a){if(b===this.a){this.a=null;}lp(this);}return a;}
function wp(b,a){np(this,b,a);}
function Fo(){}
_=Fo.prototype=new Em();_.Cb=vp;_.Eb=wp;_.tN=EK+'DockPanel';_.tI=60;_.a=null;var qp,rp,sp,tp,up;function ap(){}
_=ap.prototype=new bF();_.tN=EK+'DockPanel$DockLayoutConstant';_.tI=61;function dp(b,a){b.a=a;return b;}
function cp(){}
_=cp.prototype=new bF();_.tN=EK+'DockPanel$LayoutData';_.tI=62;_.a=null;_.b='left';_.c='';_.d=null;_.e='top';_.f='';function fp(){}
_=fp.prototype=new bF();_.tN=EK+'DockPanel$TmpRow';_.tI=63;_.a=0;_.b=null;function as(a){a.g=wr(new rr());}
function bs(a){as(a);a.e=mh();a.a=jh();ah(a.e,a.a);a.Fb(a.e);oA(a,1);return a;}
function cs(b,a){if(b.f===null){b.f=jy(new iy());}hI(b.f,a);}
function ds(d,c,b){var a;es(d,c);if(b<0){throw gE(new fE(),'Column '+b+' must be non-negative: '+b);}a=Ep(d,c);if(a<=b){throw gE(new fE(),'Column index: '+b+', Column size: '+Ep(d,c));}}
function es(c,a){var b;b=Fp(c);if(a>=b||a<0){throw gE(new fE(),'Row index: '+a+', Row size: '+b);}}
function fs(e,c,b,a){var d;d=Eq(e.b,c,b);ms(e,d,a);return d;}
function hs(c,b,a){return b.rows[a].cells.length;}
function is(a){return js(a,a.a);}
function js(b,a){return a.rows.length;}
function ks(d,b){var a,c,e;c=Ah(b);for(;c!==null;c=ji(c)){if(lF(gi(c,'tagName'),'td')){e=ji(c);a=ji(e);if(bh(a,d.a)){return c;}}if(bh(c,d.a)){return null;}}return null;}
function ls(b,a){var c;if(a!=Fp(b)){es(b,a);}c=lh();li(b.a,c,a);return a;}
function ms(d,c,a){var b,e;b=ii(c);e=null;if(b!==null){e=yr(d.g,b);}if(e!==null){ns(d,e);return true;}else{if(a){zi(c,'');}return false;}}
function ns(b,c){var a;if(c.q!==b){return false;}mv(b,c);a=c.E();pi(ji(a),a);Br(b.g,a);return true;}
function os(a,b){xi(a.e,'border',''+b);}
function ps(b,a){b.b=a;}
function qs(b,a){wi(b.e,'cellPadding',a);}
function rs(b,a){wi(b.e,'cellSpacing',a);}
function ss(b,a){b.c=a;ir(b.c);}
function ts(e,c,a,b){var d;bq(e,c,a);d=fs(e,c,a,b===null);if(b!==null){zi(d,b);}}
function us(b,a){b.d=a;}
function vs(e,b,a,d){var c;bq(e,b,a);c=fs(e,b,a,d===null);if(d!==null){Ai(c,d);}}
function ws(d,b,a,e){var c;bq(d,b,a);if(e!==null){bC(e);c=fs(d,b,a,true);zr(d.g,e);ah(c,e.E());kv(d,e);}}
function xs(){return Cr(this.g);}
function ys(c){var a,b,d,e,f;switch(Ch(c)){case 1:{if(this.f!==null){e=ks(this,c);if(e===null){return;}f=ji(e);a=ji(f);d=bi(a,f);b=bi(f,e);ly(this.f,this,d,b);}break;}default:}}
function zs(a){return ns(this,a);}
function pq(){}
_=pq.prototype=new jv();_.gb=xs;_.kb=ys;_.Cb=zs;_.tN=EK+'HTMLTable';_.tI=64;_.a=null;_.b=null;_.c=null;_.d=null;_.e=null;_.f=null;function Cp(a){bs(a);ps(a,zp(new yp(),a));us(a,kr(new jr(),a));ss(a,gr(new fr(),a));return a;}
function Ep(b,a){es(b,a);return hs(b,b.a,a);}
function Fp(a){return is(a);}
function aq(b,a){return ls(b,a);}
function bq(e,d,b){var a,c;cq(e,d);if(b<0){throw gE(new fE(),'Cannot create a column with a negative index: '+b);}a=Ep(e,d);c=b+1-a;if(c>0){dq(e.a,d,c);}}
function cq(d,b){var a,c;if(b<0){throw gE(new fE(),'Cannot create a row with a negative index: '+b);}c=Fp(d);for(a=c;a<=b;a++){aq(d,a);}}
function dq(f,d,c){var e=f.rows[d];for(var b=0;b<c;b++){var a=$doc.createElement('td');e.appendChild(a);}}
function xp(){}
_=xp.prototype=new pq();_.tN=EK+'FlexTable';_.tI=65;function zq(b,a){b.a=a;return b;}
function Bq(c,b,a){bq(c.a,b,a);return Cq(c,c.a.a,b,a);}
function Cq(e,d,c,a){var b=d.rows[c].cells[a];return b==null?null:b;}
function Dq(c,b,a){ds(c.a,b,a);return Cq(c,c.a.a,b,a);}
function Eq(c,b,a){return Cq(c,c.a.a,b,a);}
function Fq(d,c,a,b,e){br(d,c,a,b);cr(d,c,a,e);}
function ar(e,d,a,c){var b;bq(e.a,d,a);b=Cq(e,e.a.a,d,a);xi(b,'height',c);}
function br(e,d,b,a){var c;bq(e.a,d,b);c=Cq(e,e.a.a,d,b);xi(c,'align',a.a);}
function cr(d,c,b,a){bq(d.a,c,b);Ci(Cq(d,d.a.a,c,b),'verticalAlign',a.a);}
function dr(c,b,a,d){bq(c.a,b,a);xi(Cq(c,c.a.a,b,a),'width',d);}
function er(c,b,a,d){var e;bq(c.a,b,a);e=d?'':'nowrap';Ci(Dq(c,b,a),'whiteSpace',e);}
function yq(){}
_=yq.prototype=new bF();_.tN=EK+'HTMLTable$CellFormatter';_.tI=66;function zp(b,a){zq(b,a);return b;}
function Bp(d,c,b,a){wi(Bq(d,c,b),'colSpan',a);}
function yp(){}
_=yp.prototype=new yq();_.tN=EK+'FlexTable$FlexCellFormatter';_.tI=67;function fq(){fq=yK;gq=(lD(),mD);}
var gq;function xu(a){a.Fb(dh());oA(a,131197);nA(a,'gwt-Label');return a;}
function yu(b,a){xu(b);Cu(b,a);return b;}
function zu(b,a){if(b.a===null){b.a=un(new tn());}hI(b.a,a);}
function Au(b,a){if(b.b===null){b.b=av(new Fu());}hI(b.b,a);}
function Cu(b,a){Ai(b.E(),a);}
function Du(a,b){Ci(a.E(),'whiteSpace',b?'normal':'nowrap');}
function Eu(a){switch(Ch(a)){case 1:if(this.a!==null){wn(this.a,this);}break;case 4:case 8:case 64:case 16:case 32:if(this.b!==null){ev(this.b,this,a);}break;case 131072:break;}}
function wu(){}
_=wu.prototype=new bB();_.kb=Eu;_.tN=EK+'Label';_.tI=68;_.a=null;_.b=null;function As(a){xu(a);a.Fb(dh());oA(a,125);nA(a,'gwt-HTML');return a;}
function Bs(b,a){As(b);Es(b,a);return b;}
function Cs(b,a,c){Bs(b,a);Du(b,c);return b;}
function Es(b,a){zi(b.E(),a);}
function oq(){}
_=oq.prototype=new wu();_.tN=EK+'HTML';_.tI=69;function rq(a){{uq(a);}}
function sq(b,a){b.b=a;rq(b);return b;}
function uq(a){while(++a.a<a.b.b.b){if(lI(a.b.b,a.a)!==null){return;}}}
function vq(a){return a.a<a.b.b.b;}
function wq(){return vq(this);}
function xq(){var a;if(!vq(this)){throw new uK();}a=lI(this.b.b,this.a);uq(this);return a;}
function qq(){}
_=qq.prototype=new bF();_.eb=wq;_.ib=xq;_.tN=EK+'HTMLTable$1';_.tI=70;_.a=(-1);function gr(b,a){b.b=a;return b;}
function ir(a){if(a.a===null){a.a=eh('colgroup');li(a.b.e,a.a,0);ah(a.a,eh('col'));}}
function fr(){}
_=fr.prototype=new bF();_.tN=EK+'HTMLTable$ColumnFormatter';_.tI=71;_.a=null;function kr(b,a){b.a=a;return b;}
function lr(c,a,b){vA(nr(c,a),b,true);}
function nr(b,a){cq(b.a,a);return or(b,b.a.a,a);}
function or(c,a,b){return a.rows[b];}
function pr(c,a,b){vA(nr(c,a),b,false);}
function qr(c,a,b){uA(nr(c,a),b);}
function jr(){}
_=jr.prototype=new bF();_.tN=EK+'HTMLTable$RowFormatter';_.tI=72;function vr(a){a.b=gI(new eI());}
function wr(a){vr(a);return a;}
function yr(c,a){var b;b=Er(a);if(b<0){return null;}return xb(lI(c.b,b),12);}
function zr(b,c){var a;if(b.a===null){a=b.b.b;hI(b.b,c);}else{a=b.a.a;rI(b.b,a,c);b.a=b.a.b;}Fr(c.E(),a);}
function Ar(c,a,b){Dr(a);rI(c.b,b,null);c.a=tr(new sr(),b,c.a);}
function Br(c,a){var b;b=Er(a);Ar(c,a,b);}
function Cr(a){return sq(new qq(),a);}
function Dr(a){a['__widgetID']=null;}
function Er(a){var b=a['__widgetID'];return b==null?-1:b;}
function Fr(a,b){a['__widgetID']=b;}
function rr(){}
_=rr.prototype=new bF();_.tN=EK+'HTMLTable$WidgetMapper';_.tI=73;_.a=null;function tr(c,a,b){c.a=a;c.b=b;return c;}
function sr(){}
_=sr.prototype=new bF();_.tN=EK+'HTMLTable$WidgetMapper$FreeNode';_.tI=74;_.a=0;_.b=null;function ft(){ft=yK;gt=dt(new ct(),'center');ht=dt(new ct(),'left');it=dt(new ct(),'right');}
var gt,ht,it;function dt(b,a){b.a=a;return b;}
function ct(){}
_=ct.prototype=new bF();_.tN=EK+'HasHorizontalAlignment$HorizontalAlignmentConstant';_.tI=75;_.a=null;function ot(){ot=yK;mt(new lt(),'bottom');pt=mt(new lt(),'middle');qt=mt(new lt(),'top');}
var pt,qt;function mt(a,b){a.a=b;return a;}
function lt(){}
_=lt.prototype=new bF();_.tN=EK+'HasVerticalAlignment$VerticalAlignmentConstant';_.tI=76;_.a=null;function ut(a){a.a=(ft(),ht);a.c=(ot(),qt);}
function vt(a){Fm(a);ut(a);a.b=lh();ah(a.d,a.b);xi(a.e,'cellSpacing','0');xi(a.e,'cellPadding','0');return a;}
function wt(b,c){var a;a=yt(b);ah(b.b,a);Bn(b,c,a);}
function yt(b){var a;a=kh();cn(b,a,b.a);dn(b,a,b.c);return a;}
function zt(b,a){b.a=a;}
function At(c){var a,b;b=ji(c.E());a=co(this,c);if(a){pi(this.b,b);}return a;}
function tt(){}
_=tt.prototype=new Em();_.Cb=At;_.tN=EK+'HorizontalPanel';_.tI=77;_.b=null;function pu(){pu=yK;zJ(new aJ());}
function mu(a){pu();ou(a,iu(new hu(),a));nA(a,'gwt-Image');return a;}
function nu(c,e,b,d,f,a){pu();ou(c,au(new Ft(),c,e,b,d,f,a));nA(c,'gwt-Image');return c;}
function ou(b,a){b.a=a;}
function qu(c,e,b,d,f,a){c.a.cc(c,e,b,d,f,a);}
function ru(a){switch(Ch(a)){case 1:{break;}case 4:case 8:case 64:case 16:case 32:{break;}case 131072:break;case 32768:{break;}case 65536:{break;}}}
function Bt(){}
_=Bt.prototype=new bB();_.kb=ru;_.tN=EK+'Image';_.tI=78;_.a=null;function Et(){}
function Ct(){}
_=Ct.prototype=new bF();_.B=Et;_.tN=EK+'Image$1';_.tI=79;function fu(){}
_=fu.prototype=new bF();_.tN=EK+'Image$State';_.tI=80;function bu(){bu=yK;du=new oC();}
function au(d,b,f,c,e,g,a){bu();d.b=c;d.c=e;d.e=g;d.a=a;d.d=f;b.Fb(rC(du,f,c,e,g,a));oA(b,131197);cu(d,b);return d;}
function cu(b,a){cj(new Ct());}
function eu(b,e,c,d,f,a){if(!mF(this.d,e)||this.b!=c||this.c!=d||this.e!=f||this.a!=a){this.d=e;this.b=c;this.c=d;this.e=f;this.a=a;pC(du,b.E(),e,c,d,f,a);cu(this,b);}}
function Ft(){}
_=Ft.prototype=new fu();_.cc=eu;_.tN=EK+'Image$ClippedState';_.tI=81;_.a=0;_.b=0;_.c=0;_.d=null;_.e=0;var du;function iu(b,a){a.Fb(fh());oA(a,229501);return b;}
function ku(b,e,c,d,f,a){ou(b,au(new Ft(),b,e,c,d,f,a));}
function hu(){}
_=hu.prototype=new fu();_.cc=ku;_.tN=EK+'Image$UnclippedState';_.tI=82;function vu(a){return (zh(a)?1:0)|(yh(a)?8:0)|(uh(a)?2:0)|(rh(a)?4:0);}
function av(a){gI(a);return a;}
function cv(d,c,e,f){var a,b;for(a=sG(d);lG(a);){b=xb(mG(a),13);b.rb(c,e,f);}}
function dv(d,c){var a,b;for(a=sG(d);lG(a);){b=xb(mG(a),13);b.sb(c);}}
function ev(e,c,a){var b,d,f,g,h;d=c.E();g=sh(a)-Eh(d)+fi(d,'scrollLeft')+nk();h=th(a)-Fh(d)+fi(d,'scrollTop')+ok();switch(Ch(a)){case 4:cv(e,c,g,h);break;case 8:hv(e,c,g,h);break;case 64:gv(e,c,g,h);break;case 16:b=wh(a);if(!mi(d,b)){dv(e,c);}break;case 32:f=Bh(a);if(!mi(d,f)){fv(e,c);}break;}}
function fv(d,c){var a,b;for(a=sG(d);lG(a);){b=xb(mG(a),13);b.tb(c);}}
function gv(d,c,e,f){var a,b;for(a=sG(d);lG(a);){b=xb(mG(a),13);b.ub(c,e,f);}}
function hv(d,c,e,f){var a,b;for(a=sG(d);lG(a);){b=xb(mG(a),13);b.vb(c,e,f);}}
function Fu(){}
_=Fu.prototype=new eI();_.tN=EK+'MouseListenerCollection';_.tI=83;function tw(){tw=yK;yw=zJ(new aJ());}
function sw(b,a){tw();mm(b);if(a===null){a=uw();}b.Fb(a);b.jb();return b;}
function vw(){tw();return ww(null);}
function ww(c){tw();var a,b;b=xb(FJ(yw,c),14);if(b!==null){return b;}a=null;if(yw.c==0){xw();}aK(yw,c,b=sw(new nw(),a));return b;}
function uw(){tw();return $doc.body;}
function xw(){tw();dk(new ow());}
function nw(){}
_=nw.prototype=new lm();_.tN=EK+'RootPanel';_.tI=84;var yw;function qw(){var a,b;for(b=lH(zH((tw(),yw)));sH(b);){a=xb(tH(b),14);if(a.fb()){a.nb();}}}
function rw(){return null;}
function ow(){}
_=ow.prototype=new bF();_.xb=qw;_.yb=rw;_.tN=EK+'RootPanel$1';_.tI=85;function Aw(a){gx(a);Dw(a,false);oA(a,16384);return a;}
function Bw(b,a){Aw(b);b.ec(a);return b;}
function Dw(b,a){Ci(b.E(),'overflow',a?'scroll':'auto');}
function Ew(a){Ch(a)==16384;}
function zw(){}
_=zw.prototype=new Fw();_.kb=Ew;_.tN=EK+'ScrollPanel';_.tI=86;function bx(a){a.a=a.b.n!==null;}
function cx(b,a){b.b=a;bx(b);return b;}
function ex(){return this.a;}
function fx(){if(!this.a||this.b.n===null){throw new uK();}this.a=false;return this.b.n;}
function ax(){}
_=ax.prototype=new bF();_.eb=ex;_.ib=fx;_.tN=EK+'SimplePanel$1';_.tI=87;function jy(a){gI(a);return a;}
function ly(f,e,d,a){var b,c;for(b=sG(f);lG(b);){c=xb(mG(b),15);c.lb(e,d,a);}}
function iy(){}
_=iy.prototype=new eI();_.tN=EK+'TableListenerCollection';_.tI=88;function lz(a){a.a=zJ(new aJ());}
function mz(b,a){lz(b);b.d=a;b.Fb(dh());Ci(b.E(),'position','relative');b.c=bD((fq(),gq));Ci(b.c,'fontSize','0');Ci(b.c,'position','absolute');Bi(b.c,'zIndex',(-1));ah(b.E(),b.c);oA(b,1021);Di(b.c,6144);b.f=py(new oy(),b);fz(b.f,b);nA(b,'gwt-Tree');return b;}
function nz(b,a){qy(b.f,a);}
function pz(d,a,c,b){if(b===null||bh(b,c)){return;}pz(d,a,c,ji(b));hI(a,Eb(b,ej));}
function qz(e,d,b){var a,c;a=gI(new eI());pz(e,a,e.E(),b);c=sz(e,a,0,d);if(c!==null){if(mi(Ey(c),b)){ez(c,!c.f,true);return true;}else if(mi(c.E(),b)){xz(e,c,true,!Cz(e,b));return true;}}return false;}
function rz(b,a){if(!a.f){return a;}return rz(b,Cy(a,a.c.b-1));}
function sz(i,a,e,h){var b,c,d,f,g;if(e==a.b){return h;}c=xb(lI(a,e),7);for(d=0,f=h.c.b;d<f;++d){b=Cy(h,d);if(bh(b.E(),c)){g=sz(i,a,e+1,Cy(h,d));if(g===null){return b;}return g;}}return sz(i,a,e+1,h);}
function tz(a){var b;b=rb('[Lcom.google.gwt.user.client.ui.Widget;',[128],[12],[a.a.c],null);yH(a.a).hc(b);return EB(a,b);}
function uz(h,g){var a,b,c,d,e,f,i,j;c=Dy(g);{f=g.d;a=gA(h);b=hA(h);e=Eh(f)-a;i=Fh(f)-b;j=fi(f,'offsetWidth');d=fi(f,'offsetHeight');Bi(h.c,'left',e);Bi(h.c,'top',i);Bi(h.c,'width',j);Bi(h.c,'height',d);ti(h.c);iD((fq(),gq),h.c);}}
function vz(e,d,a){var b,c;if(d===e.f){return;}c=d.g;if(c===null){c=e.f;}b=By(c,d);if(!a|| !d.f){if(b<c.c.b-1){xz(e,Cy(c,b+1),true,true);}else{vz(e,c,false);}}else if(d.c.b>0){xz(e,Cy(d,0),true,true);}}
function wz(e,c){var a,b,d;b=c.g;if(b===null){b=e.f;}a=By(b,c);if(a>0){d=Cy(b,a-1);xz(e,rz(e,d),true,true);}else{xz(e,b,true,true);}}
function xz(d,b,a,c){if(b===d.f){return;}if(d.b!==null){cz(d.b,false);}d.b=b;if(c&&d.b!==null){uz(d,d.b);cz(d.b,true);}}
function yz(b,a){sy(b.f,a);}
function zz(b,a){if(a){iD((fq(),gq),b.c);}else{fD((fq(),gq),b.c);}}
function Az(b,a){Bz(b,a,true);}
function Bz(c,b,a){if(b===null){if(c.b===null){return;}cz(c.b,false);c.b=null;return;}xz(c,b,a,true);}
function Cz(c,a){var b=a.nodeName;return b=='SELECT'||(b=='INPUT'||(b=='TEXTAREA'||(b=='OPTION'||(b=='BUTTON'||b=='LABEL'))));}
function Dz(){var a,b;for(b=tz(this);zB(b);){a=AB(b);a.jb();}yi(this.c,this);}
function Ez(){var a,b;for(b=tz(this);zB(b);){a=AB(b);a.nb();}yi(this.c,null);}
function Fz(){return tz(this);}
function aA(c){var a,b,d,e,f;d=Ch(c);switch(d){case 1:{b=Ah(c);if(Cz(this,b)){}else{zz(this,true);}break;}case 4:{if(gj(vh(c),Eb(this.E(),ej))){qz(this,this.f,Ah(c));}break;}case 8:{break;}case 64:{break;}case 16:{break;}case 32:{break;}case 2048:break;case 4096:{break;}case 128:if(this.b===null){if(this.f.c.b>0){xz(this,Cy(this.f,0),true,true);}return;}if(this.e==128){return;}{switch(xh(c)){case 38:{wz(this,this.b);Dh(c);break;}case 40:{vz(this,this.b,true);Dh(c);break;}case 37:{if(this.b.f){dz(this.b,false);}else{f=this.b.g;if(f!==null){Az(this,f);}}Dh(c);break;}case 39:{if(!this.b.f){dz(this.b,true);}else if(this.b.c.b>0){Az(this,Cy(this.b,0));}Dh(c);break;}}}case 512:if(d==512){if(xh(c)==9){a=gI(new eI());pz(this,a,this.E(),Ah(c));e=sz(this,a,0,this.f);if(e!==this.b){Bz(this,e,true);}}}case 256:{break;}}this.e=d;}
function bA(){hz(this.f);}
function cA(b){var a;a=xb(FJ(this.a,b),16);if(a===null){return false;}gz(a,null);return true;}
function ny(){}
_=ny.prototype=new bB();_.y=Dz;_.z=Ez;_.gb=Fz;_.kb=aA;_.qb=bA;_.Cb=cA;_.tN=EK+'Tree';_.tI=89;_.b=null;_.c=null;_.d=null;_.e=0;_.f=null;function xy(a){a.c=gI(new eI());a.i=mu(new Bt());}
function yy(d){var a,b,c,e;xy(d);d.Fb(dh());d.e=mh();d.d=ih();d.b=ih();a=jh();e=lh();c=kh();b=kh();ah(d.e,a);ah(a,e);ah(e,c);ah(e,b);Ci(c,'verticalAlign','middle');Ci(b,'verticalAlign','middle');ah(d.E(),d.e);ah(d.E(),d.b);ah(c,d.i.E());ah(b,d.d);Ci(d.d,'display','inline');Ci(d.E(),'whiteSpace','nowrap');Ci(d.b,'whiteSpace','nowrap');vA(d.d,'gwt-TreeItem',true);return d;}
function zy(b,a){yy(b);az(b,a);return b;}
function Cy(b,a){if(a<0||a>=b.c.b){return null;}return xb(lI(b.c,a),16);}
function By(b,a){return mI(b.c,a);}
function Dy(a){var b;b=a.k;{return null;}}
function Ey(a){return a.i.E();}
function Fy(a){if(a.g!==null){a.g.Ab(a);}else if(a.j!==null){yz(a.j,a);}}
function az(b,a){gz(b,null);zi(b.d,a);}
function bz(b,a){b.g=a;}
function cz(b,a){if(b.h==a){return;}b.h=a;vA(b.d,'gwt-TreeItem-selected',a);}
function dz(b,a){ez(b,a,true);}
function ez(c,b,a){if(b&&c.c.b==0){return;}c.f=b;iz(c);}
function fz(d,c){var a,b;if(d.j===c){return;}if(d.j!==null){if(d.j.b===d){Az(d.j,null);}}d.j=c;for(a=0,b=d.c.b;a<b;++a){fz(xb(lI(d.c,a),16),c);}iz(d);}
function gz(b,a){zi(b.d,'');b.k=a;}
function iz(b){var a;if(b.j===null){return;}a=b.j.d;if(b.c.b==0){wA(b.b,false);vC((ie(),ye),b.i);return;}if(b.f){wA(b.b,true);vC((ie(),ze),b.i);}else{wA(b.b,false);vC((ie(),xe),b.i);}}
function hz(c){var a,b;iz(c);for(a=0,b=c.c.b;a<b;++a){hz(xb(lI(c.c,a),16));}}
function jz(a){if(a.g!==null||a.j!==null){Fy(a);}bz(a,this);hI(this.c,a);Ci(a.E(),'marginLeft','16px');ah(this.b,a.E());fz(a,this.j);if(this.c.b==1){iz(this);}}
function kz(a){if(!kI(this.c,a)){return;}fz(a,null);pi(this.b,a.E());bz(a,null);qI(this.c,a);if(this.c.b==0){iz(this);}}
function wy(){}
_=wy.prototype=new dA();_.s=jz;_.Ab=kz;_.tN=EK+'TreeItem';_.tI=90;_.b=null;_.d=null;_.e=null;_.f=false;_.g=null;_.h=false;_.j=null;_.k=null;function py(b,a){b.a=a;yy(b);return b;}
function qy(b,a){if(a.g!==null||a.j!==null){Fy(a);}ah(b.a.E(),a.E());fz(a,b.j);bz(a,null);hI(b.c,a);Bi(a.E(),'marginLeft',0);}
function sy(b,a){if(!kI(b.c,a)){return;}fz(a,null);bz(a,null);qI(b.c,a);pi(b.a.E(),a.E());}
function ty(a){qy(this,a);}
function uy(a){sy(this,a);}
function oy(){}
_=oy.prototype=new wy();_.s=ty;_.Ab=uy;_.tN=EK+'Tree$1';_.tI=91;function AA(a){a.a=(ft(),ht);a.b=(ot(),qt);}
function BA(a){Fm(a);AA(a);xi(a.e,'cellSpacing','0');xi(a.e,'cellPadding','0');return a;}
function CA(b,d){var a,c;c=lh();a=EA(b);ah(c,a);ah(b.d,c);Bn(b,d,a);}
function EA(b){var a;a=kh();cn(b,a,b.a);dn(b,a,b.b);return a;}
function FA(b,a){b.a=a;}
function aB(c){var a,b;b=ji(c.E());a=co(this,c);if(a){pi(this.d,ji(b));}return a;}
function zA(){}
_=zA.prototype=new Em();_.Cb=aB;_.tN=EK+'VerticalPanel';_.tI=92;function kB(b,a){b.a=rb('[Lcom.google.gwt.user.client.ui.Widget;',[128],[12],[4],null);return b;}
function lB(a,b){pB(a,b,a.b);}
function nB(b,a){if(a<0||a>=b.b){throw new fE();}return b.a[a];}
function oB(b,c){var a;for(a=0;a<b.b;++a){if(b.a[a]===c){return a;}}return (-1);}
function pB(d,e,a){var b,c;if(a<0||a>d.b){throw new fE();}if(d.b==d.a.a){c=rb('[Lcom.google.gwt.user.client.ui.Widget;',[128],[12],[d.a.a*2],null);for(b=0;b<d.a.a;++b){tb(c,b,d.a[b]);}d.a=c;}++d.b;for(b=d.b-1;b>a;--b){tb(d.a,b,d.a[b-1]);}tb(d.a,a,e);}
function qB(a){return eB(new dB(),a);}
function rB(c,b){var a;if(b<0||b>=c.b){throw new fE();}--c.b;for(a=b;a<c.b;++a){tb(c.a,a,c.a[a+1]);}tb(c.a,c.b,null);}
function sB(b,c){var a;a=oB(b,c);if(a==(-1)){throw new uK();}rB(b,a);}
function cB(){}
_=cB.prototype=new bF();_.tN=EK+'WidgetCollection';_.tI=93;_.a=null;_.b=0;function eB(b,a){b.b=a;return b;}
function gB(a){return a.a<a.b.b-1;}
function hB(a){if(a.a>=a.b.b){throw new uK();}return a.b.a[++a.a];}
function iB(){return gB(this);}
function jB(){return hB(this);}
function dB(){}
_=dB.prototype=new bF();_.eb=iB;_.ib=jB;_.tN=EK+'WidgetCollection$WidgetIterator';_.tI=94;_.a=(-1);function EB(b,a){return wB(new uB(),a,b);}
function vB(a){{yB(a);}}
function wB(a,b,c){a.b=b;vB(a);return a;}
function yB(a){++a.a;while(a.a<a.b.a){if(a.b[a.a]!==null){return;}++a.a;}}
function zB(a){return a.a<a.b.a;}
function AB(a){var b;if(!zB(a)){throw new uK();}b=a.b[a.a];yB(a);return b;}
function BB(){return zB(this);}
function CB(){return AB(this);}
function uB(){}
_=uB.prototype=new bF();_.eb=BB;_.ib=CB;_.tN=EK+'WidgetIterators$1';_.tI=95;_.a=(-1);function pC(e,b,g,c,f,h,a){var d;d='url('+g+') no-repeat '+(-c+'px ')+(-f+'px');Ci(b,'background',d);Ci(b,'width',h+'px');Ci(b,'height',a+'px');}
function rC(c,f,b,e,g,a){var d;d=ih();zi(d,sC(c,f,b,e,g,a));return ii(d);}
function sC(e,g,c,f,h,b){var a,d;d='width: '+h+'px; height: '+b+'px; background: url('+g+') no-repeat '+(-c+'px ')+(-f+'px');a="<img src='"+u()+"clear.cache.gif' style='"+d+"' border='0'>";return a;}
function oC(){}
_=oC.prototype=new bF();_.tN=FK+'ClippedImageImpl';_.tI=96;function wC(){wC=yK;zC=new oC();}
function uC(c,e,b,d,f,a){wC();c.d=e;c.b=b;c.c=d;c.e=f;c.a=a;return c;}
function vC(b,a){qu(a,b.d,b.b,b.c,b.e,b.a);}
function xC(a){return nu(new Bt(),a.d,a.b,a.c,a.e,a.a);}
function yC(a){return sC(zC,a.d,a.b,a.c,a.e,a.a);}
function tC(){}
_=tC.prototype=new sm();_.tN=FK+'ClippedImagePrototype';_.tI=97;_.a=0;_.b=0;_.c=0;_.d=null;_.e=0;var zC;function lD(){lD=yK;mD=eD(new dD());nD=mD!==null?kD(new AC()):mD;}
function kD(a){lD();return a;}
function AC(){}
_=AC.prototype=new bF();_.tN=FK+'FocusImpl';_.tI=98;var mD,nD;function EC(){EC=yK;lD();}
function CC(a){a.a=FC(a);a.b=aD(a);a.c=hD(a);}
function DC(a){EC();kD(a);CC(a);return a;}
function FC(b){return function(a){if(this.parentNode.onblur){this.parentNode.onblur(a);}};}
function aD(b){return function(a){if(this.parentNode.onfocus){this.parentNode.onfocus(a);}};}
function bD(c){var a=$doc.createElement('div');var b=c.x();b.addEventListener('blur',c.a,false);b.addEventListener('focus',c.b,false);a.addEventListener('mousedown',c.c,false);a.appendChild(b);return a;}
function cD(){var a=$doc.createElement('input');a.type='text';a.style.width=a.style.height=0;a.style.zIndex= -1;a.style.position='absolute';return a;}
function BC(){}
_=BC.prototype=new AC();_.x=cD;_.tN=FK+'FocusImplOld';_.tI=99;function gD(){gD=yK;EC();}
function eD(a){gD();DC(a);return a;}
function fD(b,a){$wnd.setTimeout(function(){a.firstChild.blur();},0);}
function hD(b){return function(){var a=this.firstChild;$wnd.setTimeout(function(){a.focus();},0);};}
function iD(b,a){$wnd.setTimeout(function(){a.firstChild.focus();},0);}
function jD(){var a=$doc.createElement('input');a.type='text';a.style.opacity=0;a.style.zIndex= -1;a.style.height='1px';a.style.width='1px';a.style.overflow='hidden';a.style.position='absolute';return a;}
function dD(){}
_=dD.prototype=new BC();_.x=jD;_.tN=FK+'FocusImplSafari';_.tI=100;function qD(a){return dh();}
function oD(){}
_=oD.prototype=new bF();_.tN=FK+'PopupImpl';_.tI=101;function sD(){}
_=sD.prototype=new fF();_.tN=aL+'ArrayStoreException';_.tI=102;function xD(a,b){if(b<2||b>36){return (-1);}if(a>=48&&a<48+rE(b,10)){return a-48;}if(a>=97&&a<b+97-10){return a-97+10;}if(a>=65&&a<b+65-10){return a-65+10;}return (-1);}
function yD(){}
_=yD.prototype=new fF();_.tN=aL+'ClassCastException';_.tI=103;function aE(b,a){gF(b,a);return b;}
function FD(){}
_=FD.prototype=new fF();_.tN=aL+'IllegalArgumentException';_.tI=104;function dE(b,a){gF(b,a);return b;}
function cE(){}
_=cE.prototype=new fF();_.tN=aL+'IllegalStateException';_.tI=105;function gE(b,a){gF(b,a);return b;}
function fE(){}
_=fE.prototype=new fF();_.tN=aL+'IndexOutOfBoundsException';_.tI=106;function BE(){BE=yK;{aF();}}
function CE(a){BE();return isNaN(a);}
function DE(e,d,c,h){BE();var a,b,f,g;if(e===null){throw zE(new yE(),'Unable to parse null');}b=pF(e);f=b>0&&jF(e,0)==45?1:0;for(a=f;a<b;a++){if(xD(jF(e,a),d)==(-1)){throw zE(new yE(),'Could not parse '+e+' in radix '+d);}}g=EE(e,d);if(CE(g)){throw zE(new yE(),'Unable to parse '+e);}else if(g<c||g>h){throw zE(new yE(),'The string '+e+' exceeds the range for the requested data type');}return g;}
function EE(b,a){BE();return parseInt(b,a);}
function aF(){BE();FE=/^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i;}
var FE=null;function jE(){jE=yK;BE();}
function mE(a){jE();return nE(a,10);}
function nE(b,a){jE();return Ab(DE(b,a,(-2147483648),2147483647));}
var kE=2147483647,lE=(-2147483648);function qE(a){return a<0?-a:a;}
function rE(a,b){return a<b?a:b;}
function sE(){}
_=sE.prototype=new fF();_.tN=aL+'NegativeArraySizeException';_.tI=107;function vE(b,a){gF(b,a);return b;}
function uE(){}
_=uE.prototype=new fF();_.tN=aL+'NullPointerException';_.tI=108;function zE(b,a){aE(b,a);return b;}
function yE(){}
_=yE.prototype=new FD();_.tN=aL+'NumberFormatException';_.tI=109;function jF(b,a){return b.charCodeAt(a);}
function mF(b,a){if(!yb(a,1))return false;return tF(b,a);}
function lF(b,a){if(a==null)return false;return b==a||b.toLowerCase()==a.toLowerCase();}
function nF(b,a){return b.indexOf(a);}
function oF(c,b,a){return c.indexOf(b,a);}
function pF(a){return a.length;}
function qF(b,a){return b.substr(a,b.length-a);}
function rF(c,a,b){return c.substr(a,b-a);}
function sF(c){var a=c.replace(/^(\s*)/,'');var b=a.replace(/\s*$/,'');return b;}
function tF(a,b){return String(a)==b;}
function uF(a){return mF(this,a);}
function wF(){var a=vF;if(!a){a=vF={};}var e=':'+this;var b=a[e];if(b==null){b=0;var f=this.length;var d=f<64?1:f/32|0;for(var c=0;c<f;c+=d){b<<=1;b+=this.charCodeAt(c);}b|=0;a[e]=b;}return b;}
_=String.prototype;_.eQ=uF;_.hC=wF;_.tN=aL+'String';_.tI=2;var vF=null;function zF(){return new Date().getTime();}
function AF(a){return A(a);}
function FF(b,a){gF(b,a);return b;}
function EF(){}
_=EF.prototype=new fF();_.tN=aL+'UnsupportedOperationException';_.tI=111;function jG(b,a){b.c=a;return b;}
function lG(a){return a.a<a.c.gc();}
function mG(a){if(!lG(a)){throw new uK();}return a.c.cb(a.b=a.a++);}
function nG(a){if(a.b<0){throw new cE();}a.c.Bb(a.b);a.a=a.b;a.b=(-1);}
function oG(){return lG(this);}
function pG(){return mG(this);}
function iG(){}
_=iG.prototype=new bF();_.eb=oG;_.ib=pG;_.tN=bL+'AbstractList$IteratorImpl';_.tI=112;_.a=0;_.b=(-1);function xH(f,d,e){var a,b,c;for(b=uJ(f.A());nJ(b);){a=oJ(b);c=a.F();if(d===null?c===null:d.eQ(c)){if(e){pJ(b);}return a;}}return null;}
function yH(b){var a;a=b.A();return BG(new AG(),b,a);}
function zH(b){var a;a=EJ(b);return jH(new iH(),b,a);}
function AH(a){return xH(this,a,false)!==null;}
function BH(d){var a,b,c,e,f,g,h;if(d===this){return true;}if(!yb(d,26)){return false;}f=xb(d,26);c=yH(this);e=f.hb();if(!bI(c,e)){return false;}for(a=DG(c);eH(a);){b=fH(a);h=this.db(b);g=f.db(b);if(h===null?g!==null:!h.eQ(g)){return false;}}return true;}
function CH(b){var a;a=xH(this,b,false);return a===null?null:a.bb();}
function DH(){var a,b,c;b=0;for(c=uJ(this.A());nJ(c);){a=oJ(c);b+=a.hC();}return b;}
function EH(){return yH(this);}
function zG(){}
_=zG.prototype=new bF();_.v=AH;_.eQ=BH;_.db=CH;_.hC=DH;_.hb=EH;_.tN=bL+'AbstractMap';_.tI=113;function bI(e,b){var a,c,d;if(b===e){return true;}if(!yb(b,27)){return false;}c=xb(b,27);if(c.gc()!=e.gc()){return false;}for(a=c.gb();a.eb();){d=a.ib();if(!e.w(d)){return false;}}return true;}
function cI(a){return bI(this,a);}
function dI(){var a,b,c;a=0;for(b=this.gb();b.eb();){c=b.ib();if(c!==null){a+=c.hC();}}return a;}
function FH(){}
_=FH.prototype=new bG();_.eQ=cI;_.hC=dI;_.tN=bL+'AbstractSet';_.tI=114;function BG(b,a,c){b.a=a;b.b=c;return b;}
function DG(b){var a;a=uJ(b.b);return cH(new bH(),b,a);}
function EG(a){return this.a.v(a);}
function FG(){return DG(this);}
function aH(){return this.b.a.c;}
function AG(){}
_=AG.prototype=new FH();_.w=EG;_.gb=FG;_.gc=aH;_.tN=bL+'AbstractMap$1';_.tI=115;function cH(b,a,c){b.a=c;return b;}
function eH(a){return a.a.eb();}
function fH(b){var a;a=b.a.ib();return a.F();}
function gH(){return eH(this);}
function hH(){return fH(this);}
function bH(){}
_=bH.prototype=new bF();_.eb=gH;_.ib=hH;_.tN=bL+'AbstractMap$2';_.tI=116;function jH(b,a,c){b.a=a;b.b=c;return b;}
function lH(b){var a;a=uJ(b.b);return qH(new pH(),b,a);}
function mH(a){return DJ(this.a,a);}
function nH(){return lH(this);}
function oH(){return this.b.a.c;}
function iH(){}
_=iH.prototype=new bG();_.w=mH;_.gb=nH;_.gc=oH;_.tN=bL+'AbstractMap$3';_.tI=117;function qH(b,a,c){b.a=c;return b;}
function sH(a){return a.a.eb();}
function tH(a){var b;b=a.a.ib().bb();return b;}
function uH(){return sH(this);}
function vH(){return tH(this);}
function pH(){}
_=pH.prototype=new bF();_.eb=uH;_.ib=vH;_.tN=bL+'AbstractMap$4';_.tI=118;function BJ(){BJ=yK;cK=iK();}
function yJ(a){{AJ(a);}}
function zJ(a){BJ();yJ(a);return a;}
function AJ(a){a.a=cb();a.d=db();a.b=Eb(cK,E);a.c=0;}
function CJ(b,a){if(yb(a,1)){return mK(b.d,xb(a,1))!==cK;}else if(a===null){return b.b!==cK;}else{return lK(b.a,a,a.hC())!==cK;}}
function DJ(a,b){if(a.b!==cK&&kK(a.b,b)){return true;}else if(hK(a.d,b)){return true;}else if(fK(a.a,b)){return true;}return false;}
function EJ(a){return sJ(new jJ(),a);}
function FJ(c,a){var b;if(yb(a,1)){b=mK(c.d,xb(a,1));}else if(a===null){b=c.b;}else{b=lK(c.a,a,a.hC());}return b===cK?null:b;}
function aK(c,a,d){var b;{b=c.b;c.b=d;}if(b===cK){++c.c;return null;}else{return b;}}
function bK(c,a){var b;if(yb(a,1)){b=pK(c.d,xb(a,1));}else if(a===null){b=c.b;c.b=Eb(cK,E);}else{b=oK(c.a,a,a.hC());}if(b===cK){return null;}else{--c.c;return b;}}
function dK(e,c){BJ();for(var d in e){if(d==parseInt(d)){var a=e[d];for(var f=0,b=a.length;f<b;++f){c.u(a[f]);}}}}
function eK(d,a){BJ();for(var c in d){if(c.charCodeAt(0)==58){var e=d[c];var b=eJ(c.substring(1),e);a.u(b);}}}
function fK(f,h){BJ();for(var e in f){if(e==parseInt(e)){var a=f[e];for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.bb();if(kK(h,d)){return true;}}}}return false;}
function gK(a){return CJ(this,a);}
function hK(c,d){BJ();for(var b in c){if(b.charCodeAt(0)==58){var a=c[b];if(kK(d,a)){return true;}}}return false;}
function iK(){BJ();}
function jK(){return EJ(this);}
function kK(a,b){BJ();if(a===b){return true;}else if(a===null){return false;}else{return a.eQ(b);}}
function nK(a){return FJ(this,a);}
function lK(f,h,e){BJ();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.F();if(kK(h,d)){return c.bb();}}}}
function mK(b,a){BJ();return b[':'+a];}
function oK(f,h,e){BJ();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.F();if(kK(h,d)){if(a.length==1){delete f[e];}else{a.splice(g,1);}return c.bb();}}}}
function pK(c,a){BJ();a=':'+a;var b=c[a];delete c[a];return b;}
function aJ(){}
_=aJ.prototype=new zG();_.v=gK;_.A=jK;_.db=nK;_.tN=bL+'HashMap';_.tI=119;_.a=null;_.b=null;_.c=0;_.d=null;var cK;function cJ(b,a,c){b.a=a;b.b=c;return b;}
function eJ(a,b){return cJ(new bJ(),a,b);}
function fJ(b){var a;if(yb(b,28)){a=xb(b,28);if(kK(this.a,a.F())&&kK(this.b,a.bb())){return true;}}return false;}
function gJ(){return this.a;}
function hJ(){return this.b;}
function iJ(){var a,b;a=0;b=0;if(this.a!==null){a=this.a.hC();}if(this.b!==null){b=this.b.hC();}return a^b;}
function bJ(){}
_=bJ.prototype=new bF();_.eQ=fJ;_.F=gJ;_.bb=hJ;_.hC=iJ;_.tN=bL+'HashMap$EntryImpl';_.tI=120;_.a=null;_.b=null;function sJ(b,a){b.a=a;return b;}
function uJ(a){return lJ(new kJ(),a.a);}
function vJ(c){var a,b,d;if(yb(c,28)){a=xb(c,28);b=a.F();if(CJ(this.a,b)){d=FJ(this.a,b);return kK(a.bb(),d);}}return false;}
function wJ(){return uJ(this);}
function xJ(){return this.a.c;}
function jJ(){}
_=jJ.prototype=new FH();_.w=vJ;_.gb=wJ;_.gc=xJ;_.tN=bL+'HashMap$EntrySet';_.tI=121;function lJ(c,b){var a;c.c=b;a=gI(new eI());if(c.c.b!==(BJ(),cK)){hI(a,cJ(new bJ(),null,c.c.b));}eK(c.c.d,a);dK(c.c.a,a);c.a=sG(a);return c;}
function nJ(a){return lG(a.a);}
function oJ(a){return a.b=xb(mG(a.a),28);}
function pJ(a){if(a.b===null){throw dE(new cE(),'Must call next() before remove().');}else{nG(a.a);bK(a.c,a.b.F());a.b=null;}}
function qJ(){return nJ(this);}
function rJ(){return oJ(this);}
function kJ(){}
_=kJ.prototype=new bF();_.eb=qJ;_.ib=rJ;_.tN=bL+'HashMap$EntrySetIterator';_.tI=122;_.a=null;_.b=null;function uK(){}
_=uK.prototype=new fF();_.tN=bL+'NoSuchElementException';_.tI=123;function rD(){ee(be(new zc()));}
function gwtOnLoad(b,d,c){$moduleName=d;$moduleBase=c;if(b)try{rD();}catch(a){b(d);}else{rD();}}
var Db=[{},{18:1},{1:1,18:1,23:1,24:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{2:1,18:1},{18:1},{18:1},{18:1},{18:1,19:1},{12:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{6:1,12:1,17:1,18:1,19:1,20:1},{6:1,12:1,13:1,17:1,18:1,19:1,20:1},{6:1,12:1,13:1,17:1,18:1,19:1,20:1},{11:1,18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{11:1,18:1},{18:1,22:1},{6:1,12:1,17:1,18:1,19:1,20:1},{10:1,18:1},{5:1,18:1},{12:1,18:1,19:1,20:1},{4:1,18:1},{11:1,12:1,15:1,18:1,19:1,20:1},{18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{11:1,12:1,18:1,19:1,20:1},{3:1,18:1},{18:1},{8:1,18:1},{8:1,18:1},{8:1,18:1},{18:1},{2:1,7:1,18:1},{2:1,18:1},{9:1,18:1},{18:1},{18:1},{18:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{18:1},{18:1,25:1},{18:1,25:1},{18:1,25:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1},{18:1,21:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{12:1,17:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{5:1,18:1},{18:1},{18:1},{18:1},{18:1,25:1},{12:1,14:1,17:1,18:1,19:1,20:1},{9:1,18:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1,25:1},{12:1,17:1,18:1,19:1,20:1},{16:1,18:1,19:1},{16:1,18:1,19:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{18:1,24:1},{3:1,18:1},{18:1},{18:1,26:1},{18:1,27:1},{18:1,27:1},{18:1},{18:1},{18:1},{18:1,26:1},{18:1,28:1},{18:1,27:1},{18:1},{3:1,18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1}];if (com_google_gwt_sample_mail_Mail) {  var __gwt_initHandlers = com_google_gwt_sample_mail_Mail.__gwt_initHandlers;  com_google_gwt_sample_mail_Mail.onScriptLoad(gwtOnLoad);}})();