function F$(){}
function gJb(){}
function DJb(){}
function GJb(){}
function LJb(){}
function AJb(){return AR}
function R$(){return bM}
function FJb(){return xR}
function KJb(){return yR}
function NJb(){return zR}
function BJb(a){sJb(this,a)}
function yJb(a){if(a==jJb){return true}KE();return a==iJb}
function xJb(a){if(a==kJb){return true}KE();return a==nJb}
function CJb(a){var b;b=PCb(this,a);if(b){a==this.a&&(this.a=null);tJb(this)}return b}
function uJb(a,b){var c;c=a.H;c.b=b.a;!!c.c&&(c.c[rhc]=b.a,undefined)}
function vJb(a,b){var c;c=a.H;c.d=b.a;!!c.c&&(c.c.style[Onc]=b.a,undefined)}
function JJb(a,b){a.b=(ZLb(),WLb).a;a.d=(iMb(),hMb).a;a.a=b;return a}
function qJb(a){oJb();HDb(a);a.b=(ZLb(),WLb);a.c=(iMb(),hMb);a.e[ihc]=0;a.e[hhc]=0;return a}
function S$(){N$=true;M$=(P$(),new F$);Xn((Un(),Tn),19);!!$stats&&$stats(Co(DHc,kec,null,null));M$.Gb();!!$stats&&$stats(Co(DHc,$wc,null,null))}
function oJb(){oJb=Icc;hJb=new DJb;kJb=new DJb;jJb=new DJb;iJb=new DJb;lJb=new DJb;mJb=new DJb;nJb=new DJb}
function rJb(a,b,c){var d;if(c==hJb){if(b==a.a){return}else if(a.a){throw z3b(new w3b,PHc)}}Kbb(b);PYb(a.f,b);c==hJb&&(a.a=b);d=JJb(new GJb,c);b.H=d;uJb(b,a.b);vJb(b,a.c);tJb(a);Mbb(b,a)}
function V$(){var a,b,c,d;while(K$){a=K$;K$=K$.b;!K$&&(L$=null);Hdb(a.a.a,(c=qJb(new gJb),c.J[Wgc]=EHc,c.e[ihc]=4,c.b=(ZLb(),VLb),rJb(c,fIb(new WHb,FHc),(oJb(),lJb)),rJb(c,fIb(new WHb,GHc),mJb),rJb(c,fIb(new WHb,HHc),iJb),rJb(c,fIb(new WHb,IHc),nJb),rJb(c,fIb(new WHb,JHc),lJb),rJb(c,fIb(new WHb,KHc),mJb),b=fIb(new WHb,LHc),d=ITb(new FTb,b),d.J.style[_gc]=MHc,d.J.style[Xgc]=NHc,rJb(c,d,hJb),sJb(c,OHc),c))}}
function sJb(a,b){var c,d,e,f,g,h,i;KXb(a.J,$dc,b);h=wac(new uac);i=_Yb(new YYb,a.f);while(i.a<i.b.c-1){c=bZb(i);g=c.H.a;e=lJ(h.lb(g),40);d=!e?1:e.a;f=zJb(g,d);KXb(hzb(c.J),b,f);h.mb(g,Y3b(d+1))}}
function tJb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;b=a.d;while(b.children.length>0){b.removeChild(b.children[0])}n=1;e=1;for(h=_Yb(new YYb,a.f);h.a<h.b.c-1;){d=bZb(h);f=d.H.a;f==lJb||f==mJb?++n:(f==iJb||f==nJb||f==kJb||f==jJb)&&++e}o=XI(gV,351,31,n,0);for(g=0;g<n;++g){o[g]=new LJb;o[g].b=Bq((Zp(),$doc),tnc);b.appendChild(o[g].b)}j=0;k=e-1;l=0;p=n-1;c=null;for(h=_Yb(new YYb,a.f);h.a<h.b.c-1;){d=bZb(h);i=d.H;q=Bq((Zp(),$doc),rnc);i.c=q;i.c[rhc]=i.b;i.c.style[Onc]=i.d;i.c[_gc]=$dc;i.c[Xgc]=$dc;if(i.a==lJb){qBb(o[l].b,q,o[l].a);q.appendChild(d.J);q[lhc]=k-j+1;++l}else if(i.a==mJb){qBb(o[p].b,q,o[p].a);q.appendChild(d.J);q[lhc]=k-j+1;--p}else if(i.a==hJb){c=q}else if(xJb(i.a)){m=o[l];qBb(m.b,q,m.a++);q.appendChild(d.J);q[QHc]=p-l+1;++j}else if(yJb(i.a)){m=o[l];qBb(m.b,q,m.a);q.appendChild(d.J);q[QHc]=p-l+1;--k}}if(a.a){m=o[l];qBb(m.b,c,m.a);c.appendChild(a.a.J)}}
function zJb(a,b){if(a==lJb){return RHc+b}else if(a==mJb){return SHc+b}else if(a==nJb){return THc+b}else if(a==iJb){return UHc+b}else if(a==kJb){return VHc+b}else if(a==jJb){return WHc+b}else{return Pnc}}
var NHc='100px',XHc='AsyncLoader19',$Hc='DockPanel',_Hc='DockPanel$DockLayoutConstant',aIc='DockPanel$LayoutData',YHc='DockPanel$TmpRow',ZHc='DockPanel$TmpRow;',PHc='Only one CENTER widget may be added',LHc="This is a <code>ScrollPanel<\/code> contained at the center of a <code>DockPanel<\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.<br><br>Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!",HHc='This is the east component',FHc='This is the first north component',GHc='This is the first south component',JHc='This is the second north component',KHc='This is the second south component',IHc='This is the west component',EHc='cw-DockPanel',OHc='cwDockPanel',UHc='east',WHc='lineend',VHc='linestart',RHc='north',DHc='runCallbacks19',SHc='south',THc='west';_=F$.prototype=new G$;_.gC=R$;_.Gb=V$;_.tI=0;_=gJb.prototype=new FDb;_.gC=AJb;_.Kb=BJb;_.Zb=CJb;_.tI=221;_.a=null;var hJb,iJb,jJb,kJb,lJb,mJb,nJb;_=DJb.prototype=new Ul;_.gC=FJb;_.tI=0;_=GJb.prototype=new Ul;_.gC=KJb;_.tI=0;_.a=null;_.c=null;_=LJb.prototype=new Ul;_.gC=NJb;_.tI=222;_.a=0;_.b=null;var bM=$2b(Mqc,XHc),zR=$2b(Rsc,YHc),gV=Z2b(tvc,ZHc),AR=$2b(Rsc,$Hc),xR=$2b(Rsc,_Hc),yR=$2b(Rsc,aIc);S$();