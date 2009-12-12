function olb(){}
function uLb(){}
function zLb(){}
function N5b(){}
function Vac(){}
function xLb(){return b5}
function Alb(){return V1}
function CLb(){return c5}
function c6b(){return g7}
function Yac(){return S7}
function Zac(){return fhc(this.J)}
function U5b(){return dhc(this.J)}
function V5b(){return ehc(this.J)}
function $ac(){return ghc(this.J)}
function yLb(a){kLb(this.b,this.a)}
function DLb(a){kLb(this.b,this.a)}
function BLb(a,b,c){a.b=b;a.a=c;return a}
function wLb(a,b,c){a.b=b;a.a=c;return a}
function kLb(a,b){(Nq(),b.J).innerText=S8c+a.Td()+sxc+a.Ud()||awc}
function b6b(a){var b;$5b(a,(b=(Nq(),$doc).createElement(EEc),b.type=T8c,b),U8c);return a}
function Xac(a){yvb(a,pr((Nq(),$doc),V8c));a.J[Pyc]=W8c;return a}
function ehc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;return c.text.length}catch(a){return 0}}
function dhc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;return -c.move(X8c,-65535)}catch(a){return 0}}
function ghc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;var d=c.text.length;var e=0;var f=c.duplicate();f.moveEnd(X8c,-1);var g=f.text.length;while(g==d&&f.parentElement()==b&&c.compareEndPoints(Z8c,f)<=0){e+=2;f.moveEnd(X8c,-1);g=f.text.length}return d+e}catch(a){return 0}}
function Blb(){wlb=true;vlb=(ylb(),new olb);Lo((Io(),Ho),30);!!$stats&&$stats(pp(H8c,mwc,null,null));vlb.Fc();!!$stats&&$stats(pp(H8c,$Oc,null,null))}
function iLb(a,b){var c,d,e,g;c=L1b(new I1b);c.e[bzc]=4;e=N1b(c);c.b.appendChild(e);Fsb(a);fec(c.f,a);e.appendChild(a.J);Hsb(a,c);if(b){d=rZb(new oZb,R8c);wsb(a,wLb(new uLb,a,d),(Hw(),Hw(),Gw));wsb(a,BLb(new zLb,a,d),(kv(),kv(),jv));g=N1b(c);c.b.appendChild(g);Fsb(d);fec(c.f,d);g.appendChild(d.J);Hsb(d,c)}return c}
function fhc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;var d=c.duplicate();d.moveToElementText(b);d.setEndPoint(Y8c,c);var e=d.text.length;var f=0;var g=d.duplicate();g.moveEnd(X8c,-1);var h=g.text.length;while(h==e&&g.parentElement()==b){f+=2;g.moveEnd(X8c,-1);h=g.text.length}return e+f}catch(a){return 0}}
function Elb(){var a,b,c,d,e,f,g;while(tlb){a=tlb;tlb=tlb.b;!tlb&&(ulb=null);Cub(a.a.a,(g=ddc(new adc),g.e[bzc]=5,e=Z5b(new O5b),_cc(e.J,awc,I8c),c=Z5b(new O5b),_cc(c.J,awc,J8c),c.J[bPc]=K8c,c.J[DBc]=true,edc(g,yZb(new nZb,L8c)),edc(g,iLb(e,true)),edc(g,iLb(c,false)),d=b6b(new N5b),_cc(d.J,awc,M8c),b=b6b(new N5b),_cc(b.J,awc,N8c),b.J[bPc]=K8c,b.J[DBc]=true,edc(g,yZb(new nZb,O8c)),edc(g,iLb(d,true)),edc(g,iLb(b,false)),f=Xac(new Vac),_cc(f.J,awc,P8c),f.J.rows=5,edc(g,yZb(new nZb,Q8c)),edc(g,iLb(f,true)),g))}}
var L8c='<b>Normal text box:<\/b>',O8c='<br><br><b>Password text box:<\/b>',Q8c='<br><br><b>Text area:<\/b>',$8c='AsyncLoader30',_8c='CwBasicText$2',a9c='CwBasicText$3',Y8c='EndToStart',b9c='PasswordTextBox',S8c='Selected: ',R8c='Selected: 0, 0',Z8c='StartToEnd',c9c='TextArea',X8c='character',M8c='cwBasicText-password',N8c='cwBasicText-password-disabled',P8c='cwBasicText-textarea',I8c='cwBasicText-textbox',J8c='cwBasicText-textbox-disabled',U8c='gwt-PasswordTextBox',W8c='gwt-TextArea',T8c='password',K8c='read only',H8c='runCallbacks30',V8c='textarea';_=olb.prototype=new plb;_.gC=Alb;_.Fc=Elb;_.tI=0;_=uLb.prototype=new Gm;_.gC=xLb;_._=yLb;_.tI=150;_.a=null;_.b=null;_=zLb.prototype=new Gm;_.gC=CLb;_.Z=DLb;_.tI=151;_.a=null;_.b=null;_=P5b.prototype;_.Td=U5b;_.Ud=V5b;_=N5b.prototype=new O5b;_.gC=c6b;_.tI=228;_=Vac.prototype=new P5b;_.gC=Yac;_.Td=Zac;_.Ud=$ac;_.tI=251;var V1=Ykc(LIc,$8c),b5=Ykc(hMc,_8c),c5=Ykc(hMc,a9c),g7=Ykc(QKc,b9c),S7=Ykc(QKc,c9c);Blb();