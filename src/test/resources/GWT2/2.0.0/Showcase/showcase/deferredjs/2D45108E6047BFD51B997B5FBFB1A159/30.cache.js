function $6(){}
function _wb(){}
function exb(){}
function YSb(){}
function eYb(){}
function cxb(){return SS}
function k7(){return KP}
function hxb(){return TS}
function nTb(){return WU}
function hYb(){return GV}
function dTb(){return J1b(this.J)}
function eTb(){return K1b(this.J)}
function iYb(){return L1b(this.J)}
function jYb(){return M1b(this.J)}
function dxb(a){Rwb(this.b,this.a)}
function ixb(a){Rwb(this.b,this.a)}
function gxb(a,b,c){a.b=b;a.a=c;return a}
function bxb(a,b,c){a.b=b;a.a=c;return a}
function Rwb(a,b){(Tp(),b.J).innerText=RVc+a.Hd()+Xhc+a.Id()||Ggc}
function gYb(a){ihb(a,vq((Tp(),$doc),UVc));a.J[sjc]=VVc;return a}
function mTb(a){var b;jTb(a,(b=(Tp(),$doc).createElement(npc),b.type=SVc,b),TVc);return a}
function J1b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;return -c.move(WVc,-65535)}catch(a){return 0}}
function K1b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;return c.text.length}catch(a){return 0}}
function M1b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;var d=c.text.length;var e=0;var f=c.duplicate();f.moveEnd(WVc,-1);var g=f.text.length;while(g==d&&f.parentElement()==b&&c.compareEndPoints(YVc,f)<=0){e+=2;f.moveEnd(WVc,-1);g=f.text.length}return d+e}catch(a){return 0}}
function l7(){g7=true;f7=(i7(),new $6);Rn((On(),Nn),30);!!$stats&&$stats(wo(GVc,Sgc,null,null));f7.wc();!!$stats&&$stats(wo(GVc,ozc,null,null))}
function Pwb(a,b){var c,d,e,g;c=ZOb(new WOb);c.e[Gjc]=4;e=_Ob(c);c.b.appendChild(e);peb(a);p_b(c.f,a);e.appendChild(a.J);reb(a,c);if(b){d=FKb(new CKb,QVc);geb(a,bxb(new _wb,a,d),(zv(),zv(),yv));geb(a,gxb(new exb,a,d),(cu(),cu(),bu));g=_Ob(c);c.b.appendChild(g);peb(d);p_b(c.f,d);g.appendChild(d.J);reb(d,c)}return c}
function L1b(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;var d=c.duplicate();d.moveToElementText(b);d.setEndPoint(XVc,c);var e=d.text.length;var f=0;var g=d.duplicate();g.moveEnd(WVc,-1);var h=g.text.length;while(h==e&&g.parentElement()==b){f+=2;g.moveEnd(WVc,-1);h=g.text.length}return e+f}catch(a){return 0}}
function o7(){var a,b,c,d,e,f,g;while(d7){a=d7;d7=d7.b;!d7&&(e7=null);mgb(a.a.a,(g=o$b(new l$b),g.e[Gjc]=5,e=iTb(new ZSb),k$b(e.J,Ggc,HVc),c=iTb(new ZSb),k$b(c.J,Ggc,IVc),c.J[rzc]=JVc,c.J[hmc]=true,p$b(g,MKb(new BKb,KVc)),p$b(g,Pwb(e,true)),p$b(g,Pwb(c,false)),d=mTb(new YSb),k$b(d.J,Ggc,LVc),b=mTb(new YSb),k$b(b.J,Ggc,MVc),b.J[rzc]=JVc,b.J[hmc]=true,p$b(g,MKb(new BKb,NVc)),p$b(g,Pwb(d,true)),p$b(g,Pwb(b,false)),f=gYb(new eYb),k$b(f.J,Ggc,OVc),f.J.rows=5,p$b(g,MKb(new BKb,PVc)),p$b(g,Pwb(f,true)),g))}}
var KVc='<b>Zone de texte normale:<\/b>',NVc='<br><br><b>Zone de texte &laquo;mot de passe&raquo;:<\/b>',PVc='<br><br><b>Zone de texte:<\/b>',ZVc='AsyncLoader30',$Vc='CwBasicText$2',_Vc='CwBasicText$3',XVc='EndToStart',aWc='PasswordTextBox',YVc='StartToEnd',RVc='S\xE9lectionn\xE9: ',QVc='S\xE9lectionn\xE9: 0, 0',bWc='TextArea',WVc='character',LVc='cwBasicText-password',MVc='cwBasicText-password-disabled',OVc='cwBasicText-textarea',HVc='cwBasicText-textbox',IVc='cwBasicText-textbox-disabled',TVc='gwt-PasswordTextBox',VVc='gwt-TextArea',JVc='lecture seulement',SVc='password',GVc='runCallbacks30',UVc='textarea';_=$6.prototype=new _6;_.gC=k7;_.wc=o7;_.tI=0;_=_wb.prototype=new Ol;_.gC=cxb;_._=dxb;_.tI=150;_.a=null;_.b=null;_=exb.prototype=new Ol;_.gC=hxb;_.Z=ixb;_.tI=151;_.a=null;_.b=null;_=$Sb.prototype;_.Hd=dTb;_.Id=eTb;_=YSb.prototype=new ZSb;_.gC=nTb;_.tI=227;_=eYb.prototype=new $Sb;_.gC=hYb;_.Hd=iYb;_.Id=jYb;_.tI=250;var KP=C5b($sc,ZVc),SS=C5b(wwc,$Vc),TS=C5b(wwc,_Vc),WU=C5b(dvc,aWc),GV=C5b(dvc,bWc);l7();