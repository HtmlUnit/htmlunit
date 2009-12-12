function Qkb(){}
function RKb(){}
function WKb(){}
function O4b(){}
function W9b(){}
function UKb(){return I4}
function alb(){return A1}
function ZKb(){return J4}
function d5b(){return M6}
function Z9b(){return w7}
function V4b(){return zfc(this.J)}
function W4b(){return Afc(this.J)}
function $9b(){return Bfc(this.J)}
function _9b(){return Cfc(this.J)}
function VKb(a){HKb(this.b,this.a)}
function $Kb(a){HKb(this.b,this.a)}
function YKb(a,b,c){a.b=b;a.a=c;return a}
function TKb(a,b,c){a.b=b;a.a=c;return a}
function HKb(a,b){(Gq(),b.J).innerText=b7c+a.Qd()+Nvc+a.Rd()||wuc}
function Y9b(a){$ub(a,ir((Gq(),$doc),e7c));a.J[ixc]=f7c;return a}
function c5b(a){var b;_4b(a,(b=(Gq(),$doc).createElement(dDc),b.type=c7c,b),d7c);return a}
function zfc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;return -c.move(g7c,-65535)}catch(a){return 0}}
function Afc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;return c.text.length}catch(a){return 0}}
function Cfc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;var d=c.text.length;var e=0;var f=c.duplicate();f.moveEnd(g7c,-1);var g=f.text.length;while(g==d&&f.parentElement()==b&&c.compareEndPoints(i7c,f)<=0){e+=2;f.moveEnd(g7c,-1);g=f.text.length}return d+e}catch(a){return 0}}
function blb(){Ykb=true;Xkb=($kb(),new Qkb);Fo((Co(),Bo),30);!!$stats&&$stats(jp(S6c,Iuc,null,null));Xkb.Fc();!!$stats&&$stats(jp(S6c,cNc,null,null))}
function FKb(a,b){var c,d,e,g;c=P0b(new M0b);c.e[wxc]=4;e=R0b(c);c.b.appendChild(e);fsb(a);fdc(c.f,a);e.appendChild(a.J);hsb(a,c);if(b){d=vYb(new sYb,a7c);Yrb(a,TKb(new RKb,a,d),(mw(),mw(),lw));Yrb(a,YKb(new WKb,a,d),(Ru(),Ru(),Qu));g=R0b(c);c.b.appendChild(g);fsb(d);fdc(c.f,d);g.appendChild(d.J);hsb(d,c)}return c}
function Bfc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;var d=c.duplicate();d.moveToElementText(b);d.setEndPoint(h7c,c);var e=d.text.length;var f=0;var g=d.duplicate();g.moveEnd(g7c,-1);var h=g.text.length;while(h==e&&g.parentElement()==b){f+=2;g.moveEnd(g7c,-1);h=g.text.length}return e+f}catch(a){return 0}}
function elb(){var a,b,c,d,e,f,g;while(Vkb){a=Vkb;Vkb=Vkb.b;!Vkb&&(Wkb=null);cub(a.a.a,(g=ecc(new bcc),g.e[wxc]=5,e=$4b(new P4b),acc(e.J,wuc,T6c),c=$4b(new P4b),acc(c.J,wuc,U6c),c.J[fNc]=V6c,c.J[Zzc]=true,fcc(g,CYb(new rYb,W6c)),fcc(g,FKb(e,true)),fcc(g,FKb(c,false)),d=c5b(new O4b),acc(d.J,wuc,X6c),b=c5b(new O4b),acc(b.J,wuc,Y6c),b.J[fNc]=V6c,b.J[Zzc]=true,fcc(g,CYb(new rYb,Z6c)),fcc(g,FKb(d,true)),fcc(g,FKb(b,false)),f=Y9b(new W9b),acc(f.J,wuc,$6c),f.J.rows=5,fcc(g,CYb(new rYb,_6c)),fcc(g,FKb(f,true)),g))}}
var W6c='<b>Normal text box:<\/b>',Z6c='<br><br><b>Password text box:<\/b>',_6c='<br><br><b>Text area:<\/b>',j7c='AsyncLoader30',k7c='CwBasicText$2',l7c='CwBasicText$3',h7c='EndToStart',m7c='PasswordTextBox',b7c='Selected: ',a7c='Selected: 0, 0',i7c='StartToEnd',n7c='TextArea',g7c='character',X6c='cwBasicText-password',Y6c='cwBasicText-password-disabled',$6c='cwBasicText-textarea',T6c='cwBasicText-textbox',U6c='cwBasicText-textbox-disabled',d7c='gwt-PasswordTextBox',f7c='gwt-TextArea',c7c='password',V6c='read only',S6c='runCallbacks30',e7c='textarea';_=Qkb.prototype=new Rkb;_.gC=alb;_.Fc=elb;_.tI=0;_=RKb.prototype=new Bm;_.gC=UKb;_._=VKb;_.tI=150;_.a=null;_.b=null;_=WKb.prototype=new Bm;_.gC=ZKb;_.Z=$Kb;_.tI=151;_.a=null;_.b=null;_=Q4b.prototype;_.Qd=V4b;_.Rd=W4b;_=O4b.prototype=new P4b;_.gC=d5b;_.tI=227;_=W9b.prototype=new Q4b;_.gC=Z9b;_.Qd=$9b;_.Rd=_9b;_.tI=250;var A1=sjc(QGc,j7c),I4=sjc(mKc,k7c),J4=sjc(mKc,l7c),M6=sjc(VIc,m7c),w7=sjc(VIc,n7c);blb();