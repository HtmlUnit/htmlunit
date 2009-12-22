function JX(){}
function RFb(){}
function mGb(){}
function pGb(){}
function uGb(){}
function jGb(){return KO}
function VX(){return pJ}
function oGb(){return HO}
function tGb(){return IO}
function wGb(){return JO}
function kGb(a){bGb(this,a)}
function hGb(a){if(a==UFb){return true}iC();return a==TFb}
function gGb(a){if(a==VFb){return true}iC();return a==YFb}
function lGb(a){var b;b=xzb(this,a);if(b){a==this.b&&(this.b=null);cGb(this)}return b}
function dGb(a,b){var c;c=a.I;c.c=b.b;!!c.d&&(c.d[hdc]=b.b,undefined)}
function eGb(a,b){var c;c=a.I;c.e=b.b;!!c.d&&(c.d.style[tjc]=b.b,undefined)}
function sGb(a,b){a.c=(QIb(),NIb).b;a.e=(_Ib(),$Ib).b;a.b=b;return a}
function _Fb(a){ZFb();pAb(a);a.c=(QIb(),NIb);a.d=(_Ib(),$Ib);a.f[$cc]=0;a.f[Zcc]=0;return a}
function WX(){RX=true;QX=(TX(),new JX);vn((sn(),rn),19);!!$stats&&$stats(_n(WCc,qac,null,null));QX.Ib();!!$stats&&$stats(_n(WCc,qsc,null,null))}
function ZFb(){ZFb=Q8b;SFb=new mGb;VFb=new mGb;UFb=new mGb;TFb=new mGb;WFb=new mGb;XFb=new mGb;YFb=new mGb}
function aGb(a,b,c){var d;if(c==SFb){if(b==a.b){return}else if(a.b){throw B_b(new y_b,gDc)}}N8(b);YUb(a.g,b);c==SFb&&(a.b=b);d=sGb(new pGb,c);b.I=d;dGb(b,a.c);eGb(b,a.d);cGb(a);P8(b,a)}
function ZX(){var a,b,c,d;while(OX){a=OX;OX=OX.c;!OX&&(PX=null);Kab(a.b.b,(c=_Fb(new RFb),c.K[Ncc]=XCc,c.f[$cc]=4,c.c=(QIb(),MIb),aGb(c,QEb(new FEb,YCc),(ZFb(),WFb)),aGb(c,QEb(new FEb,ZCc),XFb),aGb(c,QEb(new FEb,$Cc),TFb),aGb(c,QEb(new FEb,_Cc),YFb),aGb(c,QEb(new FEb,aDc),WFb),aGb(c,QEb(new FEb,bDc),XFb),b=QEb(new FEb,cDc),d=fQb(new cQb,b),d.K.style[Scc]=dDc,d.K.style[Occ]=eDc,aGb(c,d,SFb),bGb(c,fDc),c))}}
function bGb(a,b){var c,d,e,f,g,h,i;hUb(a.K,eac,b);h=D6b(new B6b);i=iVb(new fVb,a.g);while(i.b<i.c.d-1){c=kVb(i);g=c.I.b;e=SG(h.mb(g),35);d=!e?1:e.b;f=iGb(g,d);hUb(kwb(c.K),b,f);h.nb(g,$_b(d+1))}}
function cGb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;b=a.e;while(syb(b)>0){b.removeChild(ryb(b,0))}n=1;e=1;for(h=iVb(new fVb,a.g);h.b<h.c.d-1;){d=kVb(h);f=d.I.b;f==WFb||f==XFb?++n:(f==TFb||f==YFb||f==VFb||f==UFb)&&++e}o=CG(kS,0,26,n,0);for(g=0;g<n;++g){o[g]=new uGb;o[g].c=(Gp(),$doc).createElement($ic);b.appendChild(o[g].c)}j=0;k=e-1;l=0;p=n-1;c=null;for(h=iVb(new fVb,a.g);h.b<h.c.d-1;){d=kVb(h);i=d.I;q=(Gp(),$doc).createElement(Yic);i.d=q;i.d[hdc]=i.c;i.d.style[tjc]=i.e;i.d[Scc]=eac;i.d[Occ]=eac;if(i.b==WFb){vyb(o[l].c,q,o[l].b);q.appendChild(d.K);q[bdc]=k-j+1;++l}else if(i.b==XFb){vyb(o[p].c,q,o[p].b);q.appendChild(d.K);q[bdc]=k-j+1;--p}else if(i.b==SFb){c=q}else if(gGb(i.b)){m=o[l];vyb(m.c,q,m.b++);q.appendChild(d.K);q[hDc]=p-l+1;++j}else if(hGb(i.b)){m=o[l];vyb(m.c,q,m.b);q.appendChild(d.K);q[hDc]=p-l+1;--k}}if(a.b){m=o[l];vyb(m.c,c,m.b);c.appendChild(a.b.K)}}
function iGb(a,b){if(a==WFb){return iDc+b}else if(a==XFb){return jDc+b}else if(a==YFb){return kDc+b}else if(a==TFb){return lDc+b}else if(a==VFb){return mDc+b}else if(a==UFb){return nDc+b}else{return ujc}}
var eDc='100px',oDc='AsyncLoader19',rDc='DockPanel',sDc='DockPanel$DockLayoutConstant',tDc='DockPanel$LayoutData',pDc='DockPanel$TmpRow',qDc='DockPanel$TmpRow;',gDc='Only one CENTER widget may be added',cDc="This is a <code>ScrollPanel<\/code> contained at the center of a <code>DockPanel<\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.<br><br>Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!",$Cc='This is the east component',YCc='This is the first north component',ZCc='This is the first south component',aDc='This is the second north component',bDc='This is the second south component',_Cc='This is the west component',XCc='cw-DockPanel',fDc='cwDockPanel',lDc='east',nDc='lineend',mDc='linestart',iDc='north',WCc='runCallbacks19',jDc='south',kDc='west';_=JX.prototype=new KX;_.gC=VX;_.Ib=ZX;_.tI=0;_=RFb.prototype=new nAb;_.gC=jGb;_.Mb=kGb;_._b=lGb;_.tI=199;_.b=null;var SFb,TFb,UFb,VFb,WFb,XFb,YFb;_=mGb.prototype=new rl;_.gC=oGb;_.tI=0;_=pGb.prototype=new rl;_.gC=tGb;_.tI=0;_.b=null;_.d=null;_=uGb.prototype=new rl;_.gC=wGb;_.tI=200;_.b=0;_.c=null;var pJ=a_b(gmc,oDc),JO=a_b(loc,pDc),kS=_$b(Lqc,qDc),KO=a_b(loc,rDc),HO=a_b(loc,sDc),IO=a_b(loc,tDc);WX();