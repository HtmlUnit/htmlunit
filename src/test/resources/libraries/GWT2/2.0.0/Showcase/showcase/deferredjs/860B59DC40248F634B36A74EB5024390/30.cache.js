function J1(){}
function Krb(){}
function Prb(){}
function HNb(){}
function PSb(){}
function Nrb(){return BN}
function V1(){return tK}
function Srb(){return CN}
function YNb(){return FP}
function SSb(){return pQ}
function ONb(){return sYb(this.J)}
function PNb(){return tYb(this.J)}
function TSb(){return uYb(this.J)}
function USb(){return vYb(this.J)}
function Orb(a){Arb(this.b,this.a)}
function Trb(a){Arb(this.b,this.a)}
function Rrb(a,b,c){a.b=b;a.a=c;return a}
function Mrb(a,b,c){a.b=b;a.a=c;return a}
function Arb(a,b){(Bp(),b.J).innerText=tHc+a.Rc()+Gcc+a.Sc()||pbc}
function RSb(a){Tbb(a,dq((Bp(),$doc),wHc));a.J[_dc]=xHc;return a}
function XNb(a){var b;UNb(a,(b=(Bp(),$doc).createElement(Xjc),b.type=uHc,b),vHc);return a}
function sYb(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;return -c.move(yHc,-65535)}catch(a){return 0}}
function tYb(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;return c.text.length}catch(a){return 0}}
function yrb(a,b){var c,d,e,g;c=IJb(new FJb);c.e[nec]=4;e=KJb(c);c.b.appendChild(e);$8(a);$Vb(c.f,a);e.appendChild(a.J);a9(a,c);if(b){d=oFb(new lFb,sHc);R8(a,Mrb(new Krb,a,d),(hv(),hv(),gv));R8(a,Rrb(new Prb,a,d),(Mt(),Mt(),Lt));g=KJb(c);c.b.appendChild(g);$8(d);$Vb(c.f,d);g.appendChild(d.J);a9(d,c)}return c}
function W1(){R1=true;Q1=(T1(),new J1);zn((wn(),vn),30);!!$stats&&$stats(eo(iHc,Bbc,null,null));Q1.Hb();!!$stats&&$stats(eo(iHc,Wtc,null,null))}
function vYb(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;var d=c.text.length;var e=0;var f=c.duplicate();f.moveEnd(yHc,-1);var g=f.text.length;while(g==d&&f.parentElement()==b&&c.compareEndPoints(AHc,f)<=0){e+=2;f.moveEnd(yHc,-1);g=f.text.length}return d+e}catch(a){return 0}}
function uYb(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;var d=c.duplicate();d.moveToElementText(b);d.setEndPoint(zHc,c);var e=d.text.length;var f=0;var g=d.duplicate();g.moveEnd(yHc,-1);var h=g.text.length;while(h==e&&g.parentElement()==b){f+=2;g.moveEnd(yHc,-1);h=g.text.length}return e+f}catch(a){return 0}}
function Z1(){var a,b,c,d,e,f,g;while(O1){a=O1;O1=O1.b;!O1&&(P1=null);Xab(a.a.a,(g=ZUb(new WUb),g.e[nec]=5,e=TNb(new INb),VUb(e.J,pbc,jHc),c=TNb(new INb),VUb(c.J,pbc,kHc),c.J[Ztc]=lHc,c.J[Rgc]=true,$Ub(g,vFb(new kFb,mHc)),$Ub(g,yrb(e,true)),$Ub(g,yrb(c,false)),d=XNb(new HNb),VUb(d.J,pbc,nHc),b=XNb(new HNb),VUb(b.J,pbc,oHc),b.J[Ztc]=lHc,b.J[Rgc]=true,$Ub(g,vFb(new kFb,pHc)),$Ub(g,yrb(d,true)),$Ub(g,yrb(b,false)),f=RSb(new PSb),VUb(f.J,pbc,qHc),f.J.rows=5,$Ub(g,vFb(new kFb,rHc)),$Ub(g,yrb(f,true)),g))}}
var mHc='<b>Normal text box:<\/b>',pHc='<br><br><b>Password text box:<\/b>',rHc='<br><br><b>Text area:<\/b>',BHc='AsyncLoader30',CHc='CwBasicText$2',DHc='CwBasicText$3',zHc='EndToStart',EHc='PasswordTextBox',tHc='Selected: ',sHc='Selected: 0, 0',AHc='StartToEnd',FHc='TextArea',yHc='character',nHc='cwBasicText-password',oHc='cwBasicText-password-disabled',qHc='cwBasicText-textarea',jHc='cwBasicText-textbox',kHc='cwBasicText-textbox-disabled',vHc='gwt-PasswordTextBox',xHc='gwt-TextArea',uHc='password',lHc='read only',iHc='runCallbacks30',wHc='textarea';_=J1.prototype=new K1;_.gC=V1;_.Hb=Z1;_.tI=0;_=Krb.prototype=new wl;_.gC=Nrb;_._=Orb;_.tI=150;_.a=null;_.b=null;_=Prb.prototype=new wl;_.gC=Srb;_.Z=Trb;_.tI=151;_.a=null;_.b=null;_=JNb.prototype;_.Rc=ONb;_.Sc=PNb;_=HNb.prototype=new INb;_.gC=YNb;_.tI=227;_=PSb.prototype=new JNb;_.gC=SSb;_.Rc=TSb;_.Sc=USb;_.tI=250;var tK=l0b(Inc,BHc),BN=l0b(erc,CHc),CN=l0b(erc,DHc),FP=l0b(Npc,EHc),pQ=l0b(Npc,FHc);W1();