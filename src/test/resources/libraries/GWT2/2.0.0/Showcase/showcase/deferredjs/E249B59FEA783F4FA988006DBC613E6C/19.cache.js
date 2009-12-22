function YX(){}
function rGb(){}
function OGb(){}
function RGb(){}
function WGb(){}
function LGb(){return YO}
function iY(){return BJ}
function QGb(){return VO}
function VGb(){return WO}
function YGb(){return XO}
function MGb(a){DGb(this,a)}
function JGb(a){if(a==uGb){return true}tC();return a==tGb}
function IGb(a){if(a==vGb){return true}tC();return a==yGb}
function NGb(a){var b;b=Xzb(this,a);if(b){a==this.b&&(this.b=null);EGb(this)}return b}
function FGb(a,b){var c;c=a.K;c.c=b.b;!!c.d&&(c.d[hec]=b.b,undefined)}
function GGb(a,b){var c;c=a.K;c.e=b.b;!!c.d&&(c.d.style[ukc]=b.b,undefined)}
function UGb(a,b){a.c=(lJb(),iJb).b;a.e=(wJb(),vJb).b;a.b=b;return a}
function BGb(a){zGb();QAb(a);a.c=(lJb(),iJb);a.d=(wJb(),vJb);a.f[$dc]=0;a.f[Zdc]=0;return a}
function jY(){eY=true;dY=(gY(),new YX);zn((wn(),vn),19);!!$stats&&$stats(eo(_Dc,pbc,null,null));dY.Kb();!!$stats&&$stats(eo(_Dc,wtc,null,null))}
function zGb(){zGb=P9b;sGb=new OGb;vGb=new OGb;uGb=new OGb;tGb=new OGb;wGb=new OGb;xGb=new OGb;yGb=new OGb}
function CGb(a,b,c){var d;if(c==sGb){if(b==a.b){return}else if(a.b){throw A0b(new x0b,lEc)}}a9(b);vVb(a.g,b);c==sGb&&(a.b=b);d=UGb(new RGb,c);b.K=d;FGb(b,a.c);GGb(b,a.d);EGb(a);c9(b,a)}
function mY(){var a,b,c,d;while(bY){a=bY;bY=bY.c;!bY&&(cY=null);Zab(a.b.b,(c=BGb(new rGb),c.M[Ndc]=aEc,c.f[$dc]=4,c.c=(lJb(),hJb),CGb(c,qFb(new fFb,bEc),(zGb(),wGb)),CGb(c,qFb(new fFb,cEc),xGb),CGb(c,qFb(new fFb,dEc),tGb),CGb(c,qFb(new fFb,eEc),yGb),CGb(c,qFb(new fFb,fEc),wGb),CGb(c,qFb(new fFb,gEc),xGb),b=qFb(new fFb,hEc),d=EQb(new BQb,b),d.M.style[Sdc]=iEc,d.M.style[Odc]=jEc,CGb(c,d,sGb),DGb(c,kEc),c))}}
function DGb(a,b){var c,d,e,f,g,h,i;GUb(a.M,dbc,b);h=C7b(new A7b);i=HVb(new EVb,a.g);while(i.b<i.c.d-1){c=JVb(i);g=c.K.b;e=bH(h.ob(g),35);d=!e?1:e.b;f=KGb(g,d);GUb(Cwb(c.M),b,f);h.pb(g,Z0b(d+1))}}
function EGb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;b=a.e;while(Iyb(b)>0){b.removeChild(Hyb(b,0))}n=1;e=1;for(h=HVb(new EVb,a.g);h.b<h.c.d-1;){d=JVb(h);f=d.K.b;f==wGb||f==xGb?++n:(f==tGb||f==yGb||f==vGb||f==uGb)&&++e}o=NG(zS,0,26,n,0);for(g=0;g<n;++g){o[g]=new WGb;o[g].c=(Gp(),$doc).createElement(_jc);b.appendChild(o[g].c)}j=0;k=e-1;l=0;p=n-1;c=null;for(h=HVb(new EVb,a.g);h.b<h.c.d-1;){d=JVb(h);i=d.K;q=(Gp(),$doc).createElement(Zjc);i.d=q;i.d[hec]=i.c;i.d.style[ukc]=i.e;i.d[Sdc]=dbc;i.d[Odc]=dbc;if(i.b==wGb){Lyb(o[l].c,q,o[l].b);q.appendChild(d.M);q[bec]=k-j+1;++l}else if(i.b==xGb){Lyb(o[p].c,q,o[p].b);q.appendChild(d.M);q[bec]=k-j+1;--p}else if(i.b==sGb){c=q}else if(IGb(i.b)){m=o[l];Lyb(m.c,q,m.b++);q.appendChild(d.M);q[mEc]=p-l+1;++j}else if(JGb(i.b)){m=o[l];Lyb(m.c,q,m.b);q.appendChild(d.M);q[mEc]=p-l+1;--k}}if(a.b){m=o[l];Lyb(m.c,c,m.b);c.appendChild(a.b.M)}}
function KGb(a,b){if(a==wGb){return nEc+b}else if(a==xGb){return oEc+b}else if(a==yGb){return pEc+b}else if(a==tGb){return qEc+b}else if(a==vGb){return rEc+b}else if(a==uGb){return sEc+b}else{return vkc}}
var jEc='100px',tEc='AsyncLoader19',wEc='DockPanel',xEc='DockPanel$DockLayoutConstant',yEc='DockPanel$LayoutData',uEc='DockPanel$TmpRow',vEc='DockPanel$TmpRow;',lEc='Only one CENTER widget may be added',hEc="This is a <code>ScrollPanel<\/code> contained at the center of a <code>DockPanel<\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.<br><br>Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!",dEc='This is the east component',bEc='This is the first north component',cEc='This is the first south component',fEc='This is the second north component',gEc='This is the second south component',eEc='This is the west component',aEc='cw-DockPanel',kEc='cwDockPanel',qEc='east',sEc='lineend',rEc='linestart',nEc='north',_Dc='runCallbacks19',oEc='south',pEc='west';_=YX.prototype=new ZX;_.gC=iY;_.Kb=mY;_.tI=0;_=rGb.prototype=new OAb;_.gC=LGb;_.Ob=MGb;_.bc=NGb;_.tI=201;_.b=null;var sGb,tGb,uGb,vGb,wGb,xGb,yGb;_=OGb.prototype=new vl;_.gC=QGb;_.tI=0;_=RGb.prototype=new vl;_.gC=VGb;_.tI=0;_.b=null;_.d=null;_=WGb.prototype=new vl;_.gC=YGb;_.tI=202;_.b=0;_.c=null;var BJ=__b(inc,tEc),XO=__b(npc,uEc),zS=$_b(Rrc,vEc),YO=__b(npc,wEc),VO=__b(npc,xEc),WO=__b(npc,yEc);jY();