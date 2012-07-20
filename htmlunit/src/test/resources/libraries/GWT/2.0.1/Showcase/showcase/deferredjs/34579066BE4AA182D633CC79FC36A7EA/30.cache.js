function T4(){}
function _ub(){}
function evb(){}
function tRb(){}
function GWb(){}
function cvb(){return CQ}
function d5(){return uN}
function hvb(){return DQ}
function KRb(){return HS}
function JWb(){return sT}
function ARb(){return O0b(this.J)}
function BRb(){return P0b(this.J)}
function KWb(){return Q0b(this.J)}
function LWb(){return R0b(this.J)}
function dvb(a){Rub(this.b,this.a)}
function ivb(a){Rub(this.b,this.a)}
function gvb(a,b,c){a.b=b;a.a=c;return a}
function bvb(a,b,c){a.b=b;a.a=c;return a}
function Rub(a,b){(eq(),b.J).innerText=CMc+a.Tc()+jhc+a.Uc()||Hfc}
function IWb(a){bfb(a,Iq((eq(),$doc),FMc));a.J[Eic]=GMc;return a}
function JRb(a){var b;GRb(a,(b=(eq(),$doc).createElement(voc),b.type=DMc,b),EMc);return a}
function O0b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;return -c.move(HMc,-65535)}catch(a){return 0}}
function P0b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;return c.text.length}catch(a){return 0}}
function R0b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;var d=c.text.length;var e=0;var f=c.duplicate();f.moveEnd(HMc,-1);var g=f.text.length;while(g==d&&f.parentElement()==b&&c.compareEndPoints(JMc,f)<=0){e+=2;f.moveEnd(HMc,-1);g=f.text.length}return d+e}catch(a){return 0}}
function e5(){_4=true;$4=(b5(),new T4);bo(($n(),Zn),30);!!$stats&&$stats(Io(rMc,Tfc,null,null));$4.Gb();!!$stats&&$stats(Io(rMc,_yc,null,null))}
function Pub(a,b){var c,d,e,g;c=rNb(new oNb);c.e[Sic]=4;e=tNb(c);c.b.appendChild(e);icb(a);SZb(c.f,a);e.appendChild(a.J);kcb(a,c);if(b){d=ZIb(new WIb,BMc);_bb(a,bvb(new _ub,a,d),(Zx(),Zx(),Yx));_bb(a,gvb(new evb,a,d),(Cw(),Cw(),Bw));g=tNb(c);c.b.appendChild(g);icb(d);SZb(c.f,d);g.appendChild(d.J);kcb(d,c)}return c}
function Q0b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;var d=c.duplicate();d.moveToElementText(b);d.setEndPoint(IMc,c);var e=d.text.length;var f=0;var g=d.duplicate();g.moveEnd(HMc,-1);var h=g.text.length;while(h==e&&g.parentElement()==b){f+=2;g.moveEnd(HMc,-1);h=g.text.length}return e+f}catch(a){return 0}}
function h5(){var a,b,c,d,e,f,g;while(Y4){a=Y4;Y4=Y4.b;!Y4&&(Z4=null);feb(a.a.a,(g=QYb(new NYb),g.e[Sic]=5,e=FRb(new uRb),MYb(e.J,Hfc,sMc),c=FRb(new uRb),MYb(c.J,Hfc,tMc),c.J[czc]=uMc,c.J[tlc]=true,RYb(g,eJb(new VIb,vMc)),RYb(g,Pub(e,true)),RYb(g,Pub(c,false)),d=JRb(new tRb),MYb(d.J,Hfc,wMc),b=JRb(new tRb),MYb(b.J,Hfc,xMc),b.J[czc]=uMc,b.J[tlc]=true,RYb(g,eJb(new VIb,yMc)),RYb(g,Pub(d,true)),RYb(g,Pub(b,false)),f=IWb(new GWb),MYb(f.J,Hfc,zMc),f.J.rows=5,RYb(g,eJb(new VIb,AMc)),RYb(g,Pub(f,true)),g))}}
var vMc='<b>Normal text box:<\/b>',yMc='<br><br><b>Password text box:<\/b>',AMc='<br><br><b>Text area:<\/b>',KMc='AsyncLoader30',LMc='CwBasicText$2',MMc='CwBasicText$3',IMc='EndToStart',NMc='PasswordTextBox',CMc='Selected: ',BMc='Selected: 0, 0',JMc='StartToEnd',OMc='TextArea',HMc='character',wMc='cwBasicText-password',xMc='cwBasicText-password-disabled',zMc='cwBasicText-textarea',sMc='cwBasicText-textbox',tMc='cwBasicText-textbox-disabled',EMc='gwt-PasswordTextBox',GMc='gwt-TextArea',DMc='password',uMc='read only',rMc='runCallbacks30',FMc='textarea';_=T4.prototype=new U4;_.gC=d5;_.Gb=h5;_.tI=0;_=_ub.prototype=new Zl;_.gC=cvb;_._=dvb;_.tI=170;_.a=null;_.b=null;_=evb.prototype=new Zl;_.gC=hvb;_.Z=ivb;_.tI=171;_.a=null;_.b=null;_=vRb.prototype;_.Tc=ARb;_.Uc=BRb;_=tRb.prototype=new uRb;_.gC=KRb;_.tI=248;_=GWb.prototype=new vRb;_.gC=JWb;_.Tc=KWb;_.Uc=LWb;_.tI=272;var uN=H4b(Msc,KMc),CQ=H4b(iwc,LMc),DQ=H4b(iwc,MMc),HS=H4b(Ruc,NMc),sT=H4b(Ruc,OMc);e5();