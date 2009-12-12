function VX(){}
function wGb(){}
function TGb(){}
function WGb(){}
function _Gb(){}
function QGb(){return UO}
function fY(){return vJ}
function VGb(){return RO}
function $Gb(){return SO}
function bHb(){return TO}
function RGb(a){IGb(this,a)}
function OGb(a){if(a==zGb){return true}pC();return a==yGb}
function NGb(a){if(a==AGb){return true}pC();return a==DGb}
function SGb(a){var b;b=cAb(this,a);if(b){a==this.a&&(this.a=null);JGb(this)}return b}
function KGb(a,b){var c;c=a.H;c.b=b.a;!!c.c&&(c.c[wec]=b.a,undefined)}
function LGb(a,b){var c;c=a.H;c.d=b.a;!!c.c&&(c.c.style[Vkc]=b.a,undefined)}
function ZGb(a,b){a.b=(nJb(),kJb).a;a.d=(yJb(),xJb).a;a.a=b;return a}
function GGb(a){EGb();WAb(a);a.b=(nJb(),kJb);a.c=(yJb(),xJb);a.e[nec]=0;a.e[mec]=0;return a}
function gY(){bY=true;aY=(dY(),new VX);zn((wn(),vn),19);!!$stats&&$stats(eo(zEc,Bbc,null,null));aY.Hb();!!$stats&&$stats(eo(zEc,Wtc,null,null))}
function EGb(){EGb=_9b;xGb=new TGb;AGb=new TGb;zGb=new TGb;yGb=new TGb;BGb=new TGb;CGb=new TGb;DGb=new TGb}
function HGb(a,b,c){var d;if(c==xGb){if(b==a.a){return}else if(a.a){throw M0b(new J0b,LEc)}}$8(b);$Vb(a.f,b);c==xGb&&(a.a=b);d=ZGb(new WGb,c);b.H=d;KGb(b,a.b);LGb(b,a.c);JGb(a);a9(b,a)}
function jY(){var a,b,c,d;while($X){a=$X;$X=$X.b;!$X&&(_X=null);Xab(a.a.a,(c=GGb(new wGb),c.J[_dc]=AEc,c.e[nec]=4,c.b=(nJb(),jJb),HGb(c,vFb(new kFb,BEc),(EGb(),BGb)),HGb(c,vFb(new kFb,CEc),CGb),HGb(c,vFb(new kFb,DEc),yGb),HGb(c,vFb(new kFb,EEc),DGb),HGb(c,vFb(new kFb,FEc),BGb),HGb(c,vFb(new kFb,GEc),CGb),b=vFb(new kFb,HEc),d=TQb(new QQb,b),d.J.style[eec]=IEc,d.J.style[aec]=JEc,HGb(c,d,xGb),IGb(c,KEc),c))}}
function IGb(a,b){var c,d,e,f,g,h,i;VUb(a.J,pbc,b);h=O7b(new M7b);i=kWb(new hWb,a.f);while(i.a<i.b.c-1){c=mWb(i);g=c.H.a;e=ZG(h.lb(g),35);d=!e?1:e.a;f=PGb(g,d);VUb(xwb(c.J),b,f);h.mb(g,j1b(d+1))}}
function JGb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;b=a.d;while(b.children.length>0){b.removeChild(b.children[0])}n=1;e=1;for(h=kWb(new hWb,a.f);h.a<h.b.c-1;){d=mWb(h);f=d.H.a;f==BGb||f==CGb?++n:(f==yGb||f==DGb||f==AGb||f==zGb)&&++e}o=JG(wS,0,26,n,0);for(g=0;g<n;++g){o[g]=new _Gb;o[g].b=dq((Bp(),$doc),Akc);b.appendChild(o[g].b)}j=0;k=e-1;l=0;p=n-1;c=null;for(h=kWb(new hWb,a.f);h.a<h.b.c-1;){d=mWb(h);i=d.H;q=dq((Bp(),$doc),ykc);i.c=q;i.c[wec]=i.b;i.c.style[Vkc]=i.d;i.c[eec]=pbc;i.c[aec]=pbc;if(i.a==BGb){Fyb(o[l].b,q,o[l].a);q.appendChild(d.J);q[qec]=k-j+1;++l}else if(i.a==CGb){Fyb(o[p].b,q,o[p].a);q.appendChild(d.J);q[qec]=k-j+1;--p}else if(i.a==xGb){c=q}else if(NGb(i.a)){m=o[l];Fyb(m.b,q,m.a++);q.appendChild(d.J);q[MEc]=p-l+1;++j}else if(OGb(i.a)){m=o[l];Fyb(m.b,q,m.a);q.appendChild(d.J);q[MEc]=p-l+1;--k}}if(a.a){m=o[l];Fyb(m.b,c,m.a);c.appendChild(a.a.J)}}
function PGb(a,b){if(a==BGb){return NEc+b}else if(a==CGb){return OEc+b}else if(a==DGb){return PEc+b}else if(a==yGb){return QEc+b}else if(a==AGb){return REc+b}else if(a==zGb){return SEc+b}else{return Wkc}}
var JEc='100px',TEc='AsyncLoader19',WEc='DockPanel',XEc='DockPanel$DockLayoutConstant',YEc='DockPanel$LayoutData',UEc='DockPanel$TmpRow',VEc='DockPanel$TmpRow;',LEc='Only one CENTER widget may be added',HEc="This is a <code>ScrollPanel<\/code> contained at the center of a <code>DockPanel<\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.<br><br>Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!",DEc='This is the east component',BEc='This is the first north component',CEc='This is the first south component',FEc='This is the second north component',GEc='This is the second south component',EEc='This is the west component',AEc='cw-DockPanel',KEc='cwDockPanel',QEc='east',SEc='lineend',REc='linestart',NEc='north',zEc='runCallbacks19',OEc='south',PEc='west';_=VX.prototype=new WX;_.gC=fY;_.Hb=jY;_.tI=0;_=wGb.prototype=new UAb;_.gC=QGb;_.Lb=RGb;_.$b=SGb;_.tI=201;_.a=null;var xGb,yGb,zGb,AGb,BGb,CGb,DGb;_=TGb.prototype=new wl;_.gC=VGb;_.tI=0;_=WGb.prototype=new wl;_.gC=$Gb;_.tI=0;_.a=null;_.c=null;_=_Gb.prototype=new wl;_.gC=bHb;_.tI=202;_.a=0;_.b=null;var vJ=l0b(Inc,TEc),TO=l0b(Npc,UEc),wS=k0b(psc,VEc),UO=l0b(Npc,WEc),RO=l0b(Npc,XEc),SO=l0b(Npc,YEc);gY();