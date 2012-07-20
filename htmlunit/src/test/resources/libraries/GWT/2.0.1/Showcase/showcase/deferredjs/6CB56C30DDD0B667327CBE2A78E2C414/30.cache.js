function $nb(){}
function gOb(){}
function lOb(){}
function A8b(){}
function Ndc(){}
function jOb(){return J7}
function kob(){return B4}
function oOb(){return K7}
function R8b(){return O9}
function Qdc(){return zab}
function H8b(){return Vjc(this.J)}
function I8b(){return Wjc(this.J)}
function Rdc(){return Xjc(this.J)}
function Sdc(){return Yjc(this.J)}
function kOb(a){YNb(this.b,this.a)}
function pOb(a){YNb(this.b,this.a)}
function nOb(a,b,c){a.b=b;a.a=c;return a}
function iOb(a,b,c){a.b=b;a.a=c;return a}
function YNb(a,b){(jr(),b.J).innerText=kcd+a.Sd()+qAc+a.Td()||Oyc}
function Q8b(a){var b;N8b(a,(b=(jr(),$doc).createElement(DHc),b.type=lcd,b),mcd);return a}
function Pdc(a){iyb(a,Nr((jr(),$doc),ncd));a.J[NBc]=ocd;return a}
function Wjc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;return c.text.length}catch(a){return 0}}
function Vjc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;return -c.move(pcd,-65535)}catch(a){return 0}}
function Yjc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return 0;var d=c.text.length;var e=0;var f=c.duplicate();f.moveEnd(pcd,-1);var g=f.text.length;while(g==d&&f.parentElement()==b&&c.compareEndPoints(rcd,f)<=0){e+=2;f.moveEnd(pcd,-1);g=f.text.length}return d+e}catch(a){return 0}}
function lob(){gob=true;fob=(iob(),new $nb);hp((ep(),dp),30);!!$stats&&$stats(Np(_bd,$yc,null,null));fob.Ec();!!$stats&&$stats(Np(_bd,hSc,null,null))}
function WNb(a,b){var c,d,e,g;c=y4b(new v4b);c.e[_Bc]=4;e=A4b(c);c.b.appendChild(e);pvb(a);Zgc(c.f,a);e.appendChild(a.J);rvb(a,c);if(b){d=e0b(new b0b,jcd);gvb(a,iOb(new gOb,a,d),(cz(),cz(),bz));gvb(a,nOb(new lOb,a,d),(Hx(),Hx(),Gx));g=A4b(c);c.b.appendChild(g);pvb(d);Zgc(c.f,d);g.appendChild(d.J);rvb(d,c)}return c}
function Xjc(b){try{var c=b.document.selection.createRange();if(c.parentElement()!==b)return -1;var d=c.duplicate();d.moveToElementText(b);d.setEndPoint(qcd,c);var e=d.text.length;var f=0;var g=d.duplicate();g.moveEnd(pcd,-1);var h=g.text.length;while(h==e&&g.parentElement()==b){f+=2;g.moveEnd(pcd,-1);h=g.text.length}return e+f}catch(a){return 0}}
function oob(){var a,b,c,d,e,f,g;while(dob){a=dob;dob=dob.b;!dob&&(eob=null);mxb(a.a.a,(g=Xfc(new Ufc),g.e[_Bc]=5,e=M8b(new B8b),Tfc(e.J,Oyc,acd),c=M8b(new B8b),Tfc(c.J,Oyc,bcd),c.J[kSc]=ccd,c.J[BEc]=true,Yfc(g,l0b(new a0b,dcd)),Yfc(g,WNb(e,true)),Yfc(g,WNb(c,false)),d=Q8b(new A8b),Tfc(d.J,Oyc,ecd),b=Q8b(new A8b),Tfc(b.J,Oyc,fcd),b.J[kSc]=ccd,b.J[BEc]=true,Yfc(g,l0b(new a0b,gcd)),Yfc(g,WNb(d,true)),Yfc(g,WNb(b,false)),f=Pdc(new Ndc),Tfc(f.J,Oyc,hcd),f.J.rows=5,Yfc(g,l0b(new a0b,icd)),Yfc(g,WNb(f,true)),g))}}
var dcd='<b>Normal text box:<\/b>',gcd='<br><br><b>Password text box:<\/b>',icd='<br><br><b>Text area:<\/b>',scd='AsyncLoader30',tcd='CwBasicText$2',ucd='CwBasicText$3',qcd='EndToStart',vcd='PasswordTextBox',kcd='Selected: ',jcd='Selected: 0, 0',rcd='StartToEnd',wcd='TextArea',pcd='character',ecd='cwBasicText-password',fcd='cwBasicText-password-disabled',hcd='cwBasicText-textarea',acd='cwBasicText-textbox',bcd='cwBasicText-textbox-disabled',mcd='gwt-PasswordTextBox',ocd='gwt-TextArea',lcd='password',ccd='read only',_bd='runCallbacks30',ncd='textarea';_=$nb.prototype=new _nb;_.gC=kob;_.Ec=oob;_.tI=0;_=gOb.prototype=new cn;_.gC=jOb;_._=kOb;_.tI=170;_.a=null;_.b=null;_=lOb.prototype=new cn;_.gC=oOb;_.Z=pOb;_.tI=171;_.a=null;_.b=null;_=C8b.prototype;_.Sd=H8b;_.Td=I8b;_=A8b.prototype=new B8b;_.gC=R8b;_.tI=248;_=Ndc.prototype=new C8b;_.gC=Qdc;_.Sd=Rdc;_.Td=Sdc;_.tI=272;var B4=Onc(ULc,scd),J7=Onc(qPc,tcd),K7=Onc(qPc,ucd),O9=Onc(ZNc,vcd),zab=Onc(ZNc,wcd);lob();