function jY(){}
function AGb(){}
function XGb(){}
function $Gb(){}
function dHb(){}
function UGb(){return iP}
function vY(){return NJ}
function ZGb(){return fP}
function cHb(){return gP}
function fHb(){return hP}
function VGb(a){MGb(this,a)}
function SGb(a){if(a==DGb){return true}DC();return a==CGb}
function RGb(a){if(a==EGb){return true}DC();return a==HGb}
function WGb(a){var b;b=eAb(this,a);if(b){a==this.b&&(this.b=null);NGb(this)}return b}
function OGb(a,b){var c;c=a.I;c.c=b.b;!!c.d&&(c.d[zec]=b.b,undefined)}
function PGb(a,b){var c;c=a.I;c.e=b.b;!!c.d&&(c.d.style[Jkc]=b.b,undefined)}
function bHb(a,b){a.c=(uJb(),rJb).b;a.e=(FJb(),EJb).b;a.b=b;return a}
function KGb(a){IGb();ZAb(a);a.c=(uJb(),rJb);a.d=(FJb(),EJb);a.f[qec]=0;a.f[pec]=0;return a}
function wY(){rY=true;qY=(tY(),new jY);Cn((zn(),yn),19);!!$stats&&$stats(ho(rEc,Cbc,null,null));qY.Mb();!!$stats&&$stats(ho(rEc,Ptc,null,null))}
function IGb(){IGb=aac;BGb=new XGb;EGb=new XGb;DGb=new XGb;CGb=new XGb;FGb=new XGb;GGb=new XGb;HGb=new XGb}
function LGb(a,b,c){var d;if(c==BGb){if(b==a.b){return}else if(a.b){throw N0b(new K0b,DEc)}}n9(b);IVb(a.g,b);c==BGb&&(a.b=b);d=bHb(new $Gb,c);b.I=d;OGb(b,a.c);PGb(b,a.d);NGb(a);p9(b,a)}
function zY(){var a,b,c,d;while(oY){a=oY;oY=oY.c;!oY&&(pY=null);kbb(a.b.b,(c=KGb(new AGb),c.K[dec]=sEc,c.f[qec]=4,c.c=(uJb(),qJb),LGb(c,zFb(new oFb,tEc),(IGb(),FGb)),LGb(c,zFb(new oFb,uEc),GGb),LGb(c,zFb(new oFb,vEc),CGb),LGb(c,zFb(new oFb,wEc),HGb),LGb(c,zFb(new oFb,xEc),FGb),LGb(c,zFb(new oFb,yEc),GGb),b=zFb(new oFb,zEc),d=RQb(new OQb,b),d.K.style[iec]=AEc,d.K.style[eec]=BEc,LGb(c,d,BGb),MGb(c,CEc),c))}}
function MGb(a,b){var c,d,e,f,g,h,i;TUb(a.K,qbc,b);h=P7b(new N7b);i=UVb(new RVb,a.g);while(i.b<i.c.d-1){c=WVb(i);g=c.I.b;e=lH(h.qb(g),35);d=!e?1:e.b;f=TGb(g,d);TUb(Pwb(c.K),b,f);h.rb(g,k1b(d+1))}}
function NGb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;b=a.e;while(Wyb(b)>0){b.removeChild(Vyb(b,0))}n=1;e=1;for(h=UVb(new RVb,a.g);h.b<h.c.d-1;){d=WVb(h);f=d.I.b;f==FGb||f==GGb?++n:(f==CGb||f==HGb||f==EGb||f==DGb)&&++e}o=XG(MS,0,26,n,0);for(g=0;g<n;++g){o[g]=new dHb;o[g].c=(aq(),$doc).createElement(okc);b.appendChild(o[g].c)}j=0;k=e-1;l=0;p=n-1;c=null;for(h=UVb(new RVb,a.g);h.b<h.c.d-1;){d=WVb(h);i=d.I;q=(aq(),$doc).createElement(mkc);i.d=q;i.d[zec]=i.c;i.d.style[Jkc]=i.e;i.d[iec]=qbc;i.d[eec]=qbc;if(i.b==FGb){Zyb(o[l].c,q,o[l].b);q.appendChild(d.K);q[tec]=k-j+1;++l}else if(i.b==GGb){Zyb(o[p].c,q,o[p].b);q.appendChild(d.K);q[tec]=k-j+1;--p}else if(i.b==BGb){c=q}else if(RGb(i.b)){m=o[l];Zyb(m.c,q,m.b++);q.appendChild(d.K);q[EEc]=p-l+1;++j}else if(SGb(i.b)){m=o[l];Zyb(m.c,q,m.b);q.appendChild(d.K);q[EEc]=p-l+1;--k}}if(a.b){m=o[l];Zyb(m.c,c,m.b);c.appendChild(a.b.K)}}
function TGb(a,b){if(a==FGb){return FEc+b}else if(a==GGb){return GEc+b}else if(a==HGb){return HEc+b}else if(a==CGb){return IEc+b}else if(a==EGb){return JEc+b}else if(a==DGb){return KEc+b}else{return Kkc}}
var BEc='100px',LEc='AsyncLoader19',OEc='DockPanel',PEc='DockPanel$DockLayoutConstant',QEc='DockPanel$LayoutData',MEc='DockPanel$TmpRow',NEc='DockPanel$TmpRow;',DEc='Only one CENTER widget may be added',zEc="This is a <code>ScrollPanel<\/code> contained at the center of a <code>DockPanel<\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.<br><br>Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!",vEc='This is the east component',tEc='This is the first north component',uEc='This is the first south component',xEc='This is the second north component',yEc='This is the second south component',wEc='This is the west component',sEc='cw-DockPanel',CEc='cwDockPanel',IEc='east',KEc='lineend',JEc='linestart',FEc='north',rEc='runCallbacks19',GEc='south',HEc='west';_=jY.prototype=new kY;_.gC=vY;_.Mb=zY;_.tI=0;_=AGb.prototype=new XAb;_.gC=UGb;_.Qb=VGb;_.dc=WGb;_.tI=201;_.b=null;var BGb,CGb,DGb,EGb,FGb,GGb,HGb;_=XGb.prototype=new yl;_.gC=ZGb;_.tI=0;_=$Gb.prototype=new yl;_.gC=cHb;_.tI=0;_.b=null;_.d=null;_=dHb.prototype=new yl;_.gC=fHb;_.tI=202;_.b=0;_.c=null;var NJ=m0b(Anc,LEc),hP=m0b(Fpc,MEc),MS=l0b(isc,NEc),iP=m0b(Fpc,OEc),fP=m0b(Fpc,PEc),gP=m0b(Fpc,QEc);wY();