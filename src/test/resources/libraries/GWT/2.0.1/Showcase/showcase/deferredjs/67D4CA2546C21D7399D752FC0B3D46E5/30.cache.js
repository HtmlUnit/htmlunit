function Anb(){}
function ANb(){}
function FNb(){}
function y7b(){}
function Lcc(){}
function DNb(){return o7}
function Mnb(){return g4}
function INb(){return p7}
function P7b(){return s9}
function Occ(){return dab}
function F7b(){return mic(this.J)}
function G7b(){return nic(this.J)}
function Pcc(){return oic(this.J)}
function Qcc(){return pic(this.J)}
function ENb(a){qNb(this.b,this.a)}
function JNb(a){qNb(this.b,this.a)}
function HNb(a,b,c){a.b=b;a.a=c;return a}
function CNb(a,b,c){a.b=b;a.a=c;return a}
function qNb(a,b){(cr(),b.J).innerText=pad+a.Pd()+Iyc+a.Qd()||fxc}
function Ncc(a){Kxb(a,Gr((cr(),$doc),sad));a.J[dAc]=tad;return a}
function O7b(a){var b;L7b(a,(b=(cr(),$doc).createElement(ZFc),b.type=qad,b),rad);return a}
function mic(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;return -c.move(uad,-65535)}catch(a){return 0}}
function nic(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;return c.text.length}catch(a){return 0}}
function pic(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;var d=c.text.length;var e=0;var f=c.duplicate();f.moveEnd(uad,-1);var g=f.text.length;while(g==d&&f.parentElement()==b&&c.compareEndPoints(wad,f)<=0){e+=2;f.moveEnd(uad,-1);g=f.text.length}return d+e}catch(a){return 0}}
function Nnb(){Inb=true;Hnb=(Knb(),new Anb);bp(($o(),Zo),30);!!$stats&&$stats(Hp(ead,rxc,null,null));Hnb.Ec();!!$stats&&$stats(Hp(ead,gQc,null,null))}
function oNb(a,b){var c,d,e,g;c=z3b(new w3b);c.e[rAc]=4;e=B3b(c);c.b.appendChild(e);Rub(a);Wfc(c.f,a);e.appendChild(a.J);Tub(a,c);if(b){d=f_b(new c_b,oad);Iub(a,CNb(new ANb,a,d),(Jy(),Jy(),Iy));Iub(a,HNb(new FNb,a,d),(mx(),mx(),lx));g=B3b(c);c.b.appendChild(g);Rub(d);Wfc(c.f,d);g.appendChild(d.J);Tub(d,c)}return c}
function oic(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;var d=c.duplicate();d.moveToElementText(b);d.setEndPoint(vad,c);var e=d.text.length;var f=0;var g=d.duplicate();g.moveEnd(uad,-1);var h=g.text.length;while(h==e&&g.parentElement()==b){f+=2;g.moveEnd(uad,-1);h=g.text.length}return e+f}catch(a){return 0}}
function Qnb(){var a,b,c,d,e,f,g;while(Fnb){a=Fnb;Fnb=Fnb.b;!Fnb&&(Gnb=null);Owb(a.a.a,(g=Vec(new Sec),g.e[rAc]=5,e=K7b(new z7b),Rec(e.J,fxc,fad),c=K7b(new z7b),Rec(c.J,fxc,gad),c.J[jQc]=had,c.J[UCc]=true,Wec(g,m_b(new b_b,iad)),Wec(g,oNb(e,true)),Wec(g,oNb(c,false)),d=O7b(new y7b),Rec(d.J,fxc,jad),b=O7b(new y7b),Rec(b.J,fxc,kad),b.J[jQc]=had,b.J[UCc]=true,Wec(g,m_b(new b_b,lad)),Wec(g,oNb(d,true)),Wec(g,oNb(b,false)),f=Ncc(new Lcc),Rec(f.J,fxc,mad),f.J.rows=5,Wec(g,m_b(new b_b,nad)),Wec(g,oNb(f,true)),g))}}
var iad='<b>Normal text box:<\/b>',lad='<br><br><b>Password text box:<\/b>',nad='<br><br><b>Text area:<\/b>',xad='AsyncLoader30',yad='CwBasicText$2',zad='CwBasicText$3',vad='EndToStart',Aad='PasswordTextBox',pad='Selected: ',oad='Selected: 0, 0',wad='StartToEnd',Bad='TextArea',uad='character',jad='cwBasicText-password',kad='cwBasicText-password-disabled',mad='cwBasicText-textarea',fad='cwBasicText-textbox',gad='cwBasicText-textbox-disabled',rad='gwt-PasswordTextBox',tad='gwt-TextArea',qad='password',had='read only',ead='runCallbacks30',sad='textarea';_=Anb.prototype=new Bnb;_.gC=Mnb;_.Ec=Qnb;_.tI=0;_=ANb.prototype=new Zm;_.gC=DNb;_._=ENb;_.tI=170;_.a=null;_.b=null;_=FNb.prototype=new Zm;_.gC=INb;_.Z=JNb;_.tI=171;_.a=null;_.b=null;_=A7b.prototype;_.Pd=F7b;_.Qd=G7b;_=y7b.prototype=new z7b;_.gC=P7b;_.tI=247;_=Lcc.prototype=new A7b;_.gC=Occ;_.Pd=Pcc;_.Qd=Qcc;_.tI=271;var g4=fmc(UJc,xad),o7=fmc(qNc,yad),p7=fmc(qNc,zad),s9=fmc(ZLc,Aad),dab=fmc(ZLc,Bad);Nnb();