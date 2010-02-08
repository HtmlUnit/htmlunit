function u$(){}
function CIb(){}
function ZIb(){}
function aJb(){}
function fJb(){}
function WIb(){return rR}
function G$(){return YL}
function _Ib(){return oR}
function eJb(){return pR}
function hJb(){return qR}
function XIb(a){OIb(this,a)}
function UIb(a){if(a==FIb){return true}EE();return a==EIb}
function TIb(a){if(a==GIb){return true}EE();return a==JIb}
function YIb(a){var b;b=jCb(this,a);if(b){a==this.b&&(this.b=null);PIb(this)}return b}
function QIb(a,b){var c;c=a.I;c.c=b.b;!!c.d&&(c.d[cgc]=b.b,undefined)}
function RIb(a,b){var c;c=a.I;c.e=b.b;!!c.d&&(c.d.style[mmc]=b.b,undefined)}
function dJb(a,b){a.c=(BLb(),yLb).b;a.e=(MLb(),LLb).b;a.b=b;return a}
function MIb(a){KIb();bDb(a);a.c=(BLb(),yLb);a.d=(MLb(),LLb);a.f[Vfc]=0;a.f[Ufc]=0;return a}
function H$(){C$=true;B$=(E$(),new u$);Tn((Qn(),Pn),19);!!$stats&&$stats(yo($Fc,_cc,null,null));B$.Hb();!!$stats&&$stats(yo($Fc,uvc,null,null))}
function KIb(){KIb=xbc;DIb=new ZIb;GIb=new ZIb;FIb=new ZIb;EIb=new ZIb;HIb=new ZIb;IIb=new ZIb;JIb=new ZIb}
function NIb(a,b,c){var d;if(c==DIb){if(b==a.b){return}else if(a.b){throw o2b(new l2b,kGc)}}ybb(b);OXb(a.g,b);c==DIb&&(a.b=b);d=dJb(new aJb,c);b.I=d;QIb(b,a.c);RIb(b,a.d);PIb(a);Abb(b,a)}
function K$(){var a,b,c,d;while(z$){a=z$;z$=z$.c;!z$&&(A$=null);vdb(a.b.b,(c=MIb(new CIb),c.K[Ifc]=_Fc,c.f[Vfc]=4,c.c=(BLb(),xLb),NIb(c,BHb(new qHb,aGc),(KIb(),HIb)),NIb(c,BHb(new qHb,bGc),IIb),NIb(c,BHb(new qHb,cGc),EIb),NIb(c,BHb(new qHb,dGc),JIb),NIb(c,BHb(new qHb,eGc),HIb),NIb(c,BHb(new qHb,fGc),IIb),b=BHb(new qHb,gGc),d=XSb(new USb,b),d.K.style[Nfc]=hGc,d.K.style[Jfc]=iGc,NIb(c,d,DIb),OIb(c,jGc),c))}}
function OIb(a,b){var c,d,e,f,g,h,i;ZWb(a.K,Pcc,b);h=l9b(new j9b);i=$Xb(new XXb,a.g);while(i.b<i.c.d-1){c=aYb(i);g=c.I.b;e=fJ(h.mb(g),40);d=!e?1:e.b;f=VIb(g,d);ZWb(Xyb(c.K),b,f);h.nb(g,N2b(d+1))}}
function PIb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;b=a.e;while(eBb(b)>0){b.removeChild(dBb(b,0))}n=1;e=1;for(h=$Xb(new XXb,a.g);h.b<h.c.d-1;){d=aYb(h);f=d.I.b;f==HIb||f==IIb?++n:(f==EIb||f==JIb||f==GIb||f==FIb)&&++e}o=RI(XU,347,31,n,0);for(g=0;g<n;++g){o[g]=new fJb;o[g].c=(cq(),$doc).createElement(Tlc);b.appendChild(o[g].c)}j=0;k=e-1;l=0;p=n-1;c=null;for(h=$Xb(new XXb,a.g);h.b<h.c.d-1;){d=aYb(h);i=d.I;q=(cq(),$doc).createElement(Rlc);i.d=q;i.d[cgc]=i.c;i.d.style[mmc]=i.e;i.d[Nfc]=Pcc;i.d[Jfc]=Pcc;if(i.b==HIb){hBb(o[l].c,q,o[l].b);q.appendChild(d.K);q[Yfc]=k-j+1;++l}else if(i.b==IIb){hBb(o[p].c,q,o[p].b);q.appendChild(d.K);q[Yfc]=k-j+1;--p}else if(i.b==DIb){c=q}else if(TIb(i.b)){m=o[l];hBb(m.c,q,m.b++);q.appendChild(d.K);q[lGc]=p-l+1;++j}else if(UIb(i.b)){m=o[l];hBb(m.c,q,m.b);q.appendChild(d.K);q[lGc]=p-l+1;--k}}if(a.b){m=o[l];hBb(m.c,c,m.b);c.appendChild(a.b.K)}}
function VIb(a,b){if(a==HIb){return mGc+b}else if(a==IIb){return nGc+b}else if(a==JIb){return oGc+b}else if(a==EIb){return pGc+b}else if(a==GIb){return qGc+b}else if(a==FIb){return rGc+b}else{return nmc}}
var iGc='100px',sGc='AsyncLoader19',vGc='DockPanel',wGc='DockPanel$DockLayoutConstant',xGc='DockPanel$LayoutData',tGc='DockPanel$TmpRow',uGc='DockPanel$TmpRow;',kGc='Only one CENTER widget may be added',gGc="This is a <code>ScrollPanel<\/code> contained at the center of a <code>DockPanel<\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.<br><br>Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!",cGc='This is the east component',aGc='This is the first north component',bGc='This is the first south component',eGc='This is the second north component',fGc='This is the second south component',dGc='This is the west component',_Fc='cw-DockPanel',jGc='cwDockPanel',pGc='east',rGc='lineend',qGc='linestart',mGc='north',$Fc='runCallbacks19',nGc='south',oGc='west';_=u$.prototype=new v$;_.gC=G$;_.Hb=K$;_.tI=0;_=CIb.prototype=new _Cb;_.gC=WIb;_.Lb=XIb;_.$b=YIb;_.tI=219;_.b=null;var DIb,EIb,FIb,GIb,HIb,IIb,JIb;_=ZIb.prototype=new Pl;_.gC=_Ib;_.tI=0;_=aJb.prototype=new Pl;_.gC=eJb;_.tI=0;_.b=null;_.d=null;_=fJb.prototype=new Pl;_.gC=hJb;_.tI=220;_.b=0;_.c=null;var YL=P1b(kpc,sGc),qR=P1b(prc,tGc),XU=O1b(Ptc,uGc),rR=P1b(prc,vGc),oR=P1b(prc,wGc),pR=P1b(prc,xGc);H$();