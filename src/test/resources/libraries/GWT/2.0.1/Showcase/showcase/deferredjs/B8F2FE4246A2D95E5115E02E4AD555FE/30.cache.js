function iab(){}
function qAb(){}
function vAb(){}
function KWb(){}
function X_b(){}
function tAb(){return TV}
function uab(){return LS}
function yAb(){return UV}
function _Wb(){return YX}
function $_b(){return JY}
function RWb(){return d6b(this.J)}
function SWb(){return e6b(this.J)}
function __b(){return f6b(this.J)}
function a0b(){return g6b(this.J)}
function uAb(a){gAb(this.b,this.a)}
function zAb(a){gAb(this.b,this.a)}
function xAb(a,b,c){a.b=b;a.a=c;return a}
function sAb(a,b,c){a.b=b;a.a=c;return a}
function gAb(a,b){(wq(),b.J).innerText=$$c+a.Jd()+Amc+a.Kd()||Ykc}
function Z_b(a){skb(a,$q((wq(),$doc),b_c));a.J[Xnc]=c_c;return a}
function $Wb(a){var b;XWb(a,(b=(wq(),$doc).createElement(Ntc),b.type=_$c,b),a_c);return a}
function d6b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;return -c.move(d_c,-65535)}catch(a){return 0}}
function e6b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;return c.text.length}catch(a){return 0}}
function g6b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;var d=c.text.length;var e=0;var f=c.duplicate();f.moveEnd(d_c,-1);var g=f.text.length;while(g==d&&f.parentElement()==b&&c.compareEndPoints(f_c,f)<=0){e+=2;f.moveEnd(d_c,-1);g=f.text.length}return d+e}catch(a){return 0}}
function vab(){qab=true;pab=(sab(),new iab);uo((ro(),qo),30);!!$stats&&$stats($o(P$c,ilc,null,null));pab.vc();!!$stats&&$stats($o(P$c,tEc,null,null))}
function eAb(a,b){var c,d,e,g;c=ISb(new FSb);c.e[joc]=4;e=KSb(c);c.b.appendChild(e);zhb(a);h3b(c.f,a);e.appendChild(a.J);Bhb(a,c);if(b){d=oOb(new lOb,Z$c);qhb(a,sAb(new qAb,a,d),(py(),py(),oy));qhb(a,xAb(new vAb,a,d),(Uw(),Uw(),Tw));g=KSb(c);c.b.appendChild(g);zhb(d);h3b(c.f,d);g.appendChild(d.J);Bhb(d,c)}return c}
function f6b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;var d=c.duplicate();d.moveToElementText(b);d.setEndPoint(e_c,c);var e=d.text.length;var f=0;var g=d.duplicate();g.moveEnd(d_c,-1);var h=g.text.length;while(h==e&&g.parentElement()==b){f+=2;g.moveEnd(d_c,-1);h=g.text.length}return e+f}catch(a){return 0}}
function yab(){var a,b,c,d,e,f,g;while(nab){a=nab;nab=nab.b;!nab&&(oab=null);wjb(a.a.a,(g=f2b(new c2b),g.e[joc]=5,e=WWb(new LWb),b2b(e.J,Ykc,Q$c),c=WWb(new LWb),b2b(c.J,Ykc,R$c),c.J[wEc]=S$c,c.J[Lqc]=true,g2b(g,vOb(new kOb,T$c)),g2b(g,eAb(e,true)),g2b(g,eAb(c,false)),d=$Wb(new KWb),b2b(d.J,Ykc,U$c),b=$Wb(new KWb),b2b(b.J,Ykc,V$c),b.J[wEc]=S$c,b.J[Lqc]=true,g2b(g,vOb(new kOb,W$c)),g2b(g,eAb(d,true)),g2b(g,eAb(b,false)),f=Z_b(new X_b),b2b(f.J,Ykc,X$c),f.J.rows=5,g2b(g,vOb(new kOb,Y$c)),g2b(g,eAb(f,true)),g))}}
var T$c='<b>Zone de texte normale:<\/b>',W$c='<br><br><b>Zone de texte &laquo;mot de passe&raquo;:<\/b>',Y$c='<br><br><b>Zone de texte:<\/b>',g_c='AsyncLoader30',h_c='CwBasicText$2',i_c='CwBasicText$3',e_c='EndToStart',j_c='PasswordTextBox',f_c='StartToEnd',$$c='S\xE9lectionn\xE9: ',Z$c='S\xE9lectionn\xE9: 0, 0',k_c='TextArea',d_c='character',U$c='cwBasicText-password',V$c='cwBasicText-password-disabled',X$c='cwBasicText-textarea',Q$c='cwBasicText-textbox',R$c='cwBasicText-textbox-disabled',a_c='gwt-PasswordTextBox',c_c='gwt-TextArea',S$c='lecture seulement',_$c='password',P$c='runCallbacks30',b_c='textarea';_=iab.prototype=new jab;_.gC=uab;_.vc=yab;_.tI=0;_=qAb.prototype=new pm;_.gC=tAb;_._=uAb;_.tI=170;_.a=null;_.b=null;_=vAb.prototype=new pm;_.gC=yAb;_.Z=zAb;_.tI=171;_.a=null;_.b=null;_=MWb.prototype;_.Jd=RWb;_.Kd=SWb;_=KWb.prototype=new LWb;_.gC=_Wb;_.tI=248;_=X_b.prototype=new MWb;_.gC=$_b;_.Jd=__b;_.Kd=a0b;_.tI=272;var LS=Y9b(cyc,g_c),TV=Y9b(ABc,h_c),UV=Y9b(ABc,i_c),YX=Y9b(hAc,j_c),JY=Y9b(hAc,k_c);vab();