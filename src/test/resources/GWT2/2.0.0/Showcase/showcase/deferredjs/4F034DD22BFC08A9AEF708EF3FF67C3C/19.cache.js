function tY(){}
function sHb(){}
function PHb(){}
function SHb(){}
function XHb(){}
function MHb(){return oP}
function FY(){return QJ}
function RHb(){return lP}
function WHb(){return mP}
function ZHb(){return nP}
function NHb(a){EHb(this,a)}
function KHb(a){if(a==vHb){return true}KC();return a==uHb}
function JHb(a){if(a==wHb){return true}KC();return a==zHb}
function OHb(a){var b;b=$Ab(this,a);if(b){a==this.a&&(this.a=null);FHb(this)}return b}
function GHb(a,b){var c;c=a.H;c.b=b.a;!!c.c&&(c.c[bgc]=b.a,undefined)}
function HHb(a,b){var c;c=a.H;c.d=b.a;!!c.c&&(c.c.style[umc]=b.a,undefined)}
function VHb(a,b){a.b=(jKb(),gKb).a;a.d=(uKb(),tKb).a;a.a=b;return a}
function CHb(a){AHb();SBb(a);a.b=(jKb(),gKb);a.c=(uKb(),tKb);a.e[Ufc]=0;a.e[Tfc]=0;return a}
function GY(){BY=true;AY=(DY(),new tY);Fn((Cn(),Bn),19);!!$stats&&$stats(ko(nGc,fdc,null,null));AY.Hb();!!$stats&&$stats(ko(nGc,Svc,null,null))}
function AHb(){AHb=Fbc;tHb=new PHb;wHb=new PHb;vHb=new PHb;uHb=new PHb;xHb=new PHb;yHb=new PHb;zHb=new PHb}
function DHb(a,b,c){var d;if(c==tHb){if(b==a.a){return}else if(a.a){throw q2b(new n2b,zGc)}}y9(b);$Wb(a.f,b);c==tHb&&(a.a=b);d=VHb(new SHb,c);b.H=d;GHb(b,a.b);HHb(b,a.c);FHb(a);A9(b,a)}
function JY(){var a,b,c,d;while(yY){a=yY;yY=yY.b;!yY&&(zY=null);vbb(a.a.a,(c=CHb(new sHb),c.J[Gfc]=oGc,c.e[Ufc]=4,c.b=(jKb(),fKb),DHb(c,rGb(new gGb,pGc),(AHb(),xHb)),DHb(c,rGb(new gGb,qGc),yHb),DHb(c,rGb(new gGb,rGc),uHb),DHb(c,rGb(new gGb,sGc),zHb),DHb(c,rGb(new gGb,tGc),xHb),DHb(c,rGb(new gGb,uGc),yHb),b=rGb(new gGb,vGc),d=SRb(new PRb,b),d.J.style[Lfc]=wGc,d.J.style[Hfc]=xGc,DHb(c,d,tHb),EHb(c,yGc),c))}}
function EHb(a,b){var c,d,e,f,g,h,i;UVb(a.J,Vcc,b);h=s9b(new q9b);i=kXb(new hXb,a.f);while(i.a<i.b.c-1){c=mXb(i);g=c.H.a;e=sH(h.lb(g),35);d=!e?1:e.a;f=LHb(g,d);UVb(bxb(c.J),b,f);h.mb(g,P2b(d+1))}}
function FHb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;b=a.d;while(b.children.length>0){b.removeChild(b.children[0])}n=1;e=1;for(h=kXb(new hXb,a.f);h.a<h.b.c-1;){d=mXb(h);f=d.H.a;f==xHb||f==yHb?++n:(f==uHb||f==zHb||f==wHb||f==vHb)&&++e}o=cH(WS,0,26,n,0);for(g=0;g<n;++g){o[g]=new XHb;o[g].b=kq((Ip(),$doc),_lc);b.appendChild(o[g].b)}j=0;k=e-1;l=0;p=n-1;c=null;for(h=kXb(new hXb,a.f);h.a<h.b.c-1;){d=mXb(h);i=d.H;q=kq((Ip(),$doc),Zlc);i.c=q;i.c[bgc]=i.b;i.c.style[umc]=i.d;i.c[Lfc]=Vcc;i.c[Hfc]=Vcc;if(i.a==xHb){lzb(o[l].b,q,o[l].a);q.appendChild(d.J);q[Xfc]=k-j+1;++l}else if(i.a==yHb){lzb(o[p].b,q,o[p].a);q.appendChild(d.J);q[Xfc]=k-j+1;--p}else if(i.a==tHb){c=q}else if(JHb(i.a)){m=o[l];lzb(m.b,q,m.a++);q.appendChild(d.J);q[AGc]=p-l+1;++j}else if(KHb(i.a)){m=o[l];lzb(m.b,q,m.a);q.appendChild(d.J);q[AGc]=p-l+1;--k}}if(a.a){m=o[l];lzb(m.b,c,m.a);c.appendChild(a.a.J)}}
function LHb(a,b){if(a==xHb){return BGc+b}else if(a==yHb){return CGc+b}else if(a==zHb){return DGc+b}else if(a==uHb){return EGc+b}else if(a==wHb){return FGc+b}else if(a==vHb){return GGc+b}else{return vmc}}
var xGc='100px',HGc='AsyncLoader19',KGc='DockPanel',LGc='DockPanel$DockLayoutConstant',MGc='DockPanel$LayoutData',IGc='DockPanel$TmpRow',JGc='DockPanel$TmpRow;',zGc='Only one CENTER widget may be added',vGc="This is a <code>ScrollPanel<\/code> contained at the center of a <code>DockPanel<\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.<br><br>Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!",rGc='This is the east component',pGc='This is the first north component',qGc='This is the first south component',tGc='This is the second north component',uGc='This is the second south component',sGc='This is the west component',oGc='cw-DockPanel',yGc='cwDockPanel',EGc='east',GGc='lineend',FGc='linestart',BGc='north',nGc='runCallbacks19',CGc='south',DGc='west';_=tY.prototype=new uY;_.gC=FY;_.Hb=JY;_.tI=0;_=sHb.prototype=new QBb;_.gC=MHb;_.Lb=NHb;_.$b=OHb;_.tI=202;_.a=null;var tHb,uHb,vHb,wHb,xHb,yHb,zHb;_=PHb.prototype=new Bl;_.gC=RHb;_.tI=0;_=SHb.prototype=new Bl;_.gC=WHb;_.tI=0;_.a=null;_.c=null;_=XHb.prototype=new Bl;_.gC=ZHb;_.tI=203;_.a=0;_.b=null;var QJ=R1b(Dpc,HGc),nP=R1b(Irc,IGc),WS=Q1b(luc,JGc),oP=R1b(Irc,KGc),lP=R1b(Irc,LGc),mP=R1b(Irc,MGc);GY();