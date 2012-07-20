function kib(){}
function m1b(){}
function J1b(){}
function M1b(){}
function R1b(){}
function G1b(){return b9}
function wib(){return D3}
function L1b(){return $8}
function Q1b(){return _8}
function T1b(){return a9}
function H1b(a){y1b(this,a)}
function E1b(a){if(a==p1b){return true}II();return a==o1b}
function D1b(a){if(a==q1b){return true}II();return a==t1b}
function I1b(a){var b;b=VWb(this,a);if(b){a==this.a&&(this.a=null);z1b(this)}return b}
function A1b(a,b){var c;c=a.H;c.b=b.a;!!c.c&&(c.c[iCc]=b.a,undefined)}
function B1b(a,b){var c;c=a.H;c.d=b.a;!!c.c&&(c.c.style[AIc]=b.a,undefined)}
function P1b(a,b){a.b=(d4b(),a4b).a;a.d=(o4b(),n4b).a;a.a=b;return a}
function w1b(a){u1b();NXb(a);a.b=(d4b(),a4b);a.c=(o4b(),n4b);a.e[_Bc]=0;a.e[$Bc]=0;return a}
function xib(){sib=true;rib=(uib(),new kib);hp((ep(),dp),19);!!$stats&&$stats(Np(f9c,$yc,null,null));rib.Ec();!!$stats&&$stats(Np(f9c,hSc,null,null))}
function u1b(){u1b=wxc;n1b=new J1b;q1b=new J1b;p1b=new J1b;o1b=new J1b;r1b=new J1b;s1b=new J1b;t1b=new J1b}
function x1b(a,b,c){var d;if(c==n1b){if(b==a.a){return}else if(a.a){throw noc(new koc,r9c)}}pvb(b);Zgc(a.f,b);c==n1b&&(a.a=b);d=P1b(new M1b,c);b.H=d;A1b(b,a.b);B1b(b,a.c);z1b(a);rvb(b,a)}
function Aib(){var a,b,c,d;while(pib){a=pib;pib=pib.b;!pib&&(qib=null);mxb(a.a.a,(c=w1b(new m1b),c.J[NBc]=g9c,c.e[_Bc]=4,c.b=(d4b(),_3b),x1b(c,l0b(new a0b,h9c),(u1b(),r1b)),x1b(c,l0b(new a0b,i9c),s1b),x1b(c,l0b(new a0b,j9c),o1b),x1b(c,l0b(new a0b,k9c),t1b),x1b(c,l0b(new a0b,l9c),r1b),x1b(c,l0b(new a0b,m9c),s1b),b=l0b(new a0b,n9c),d=Rbc(new Obc,b),d.J.style[SBc]=o9c,d.J.style[OBc]=p9c,x1b(c,d,n1b),y1b(c,q9c),c))}}
function y1b(a,b){var c,d,e,f,g,h,i;Tfc(a.J,Oyc,b);h=kvc(new ivc);i=jhc(new ghc,a.f);while(i.a<i.b.c-1){c=lhc(i);g=c.H.a;e=I_(h.nc(g),40);d=!e?1:e.a;f=F1b(g,d);Tfc(XSb(c.J),b,f);h.oc(g,Moc(d+1))}}
function z1b(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;b=a.d;while(b.children.length>0){b.removeChild(b.children[0])}n=1;e=1;for(h=jhc(new ghc,a.f);h.a<h.b.c-1;){d=lhc(h);f=d.H.a;f==r1b||f==s1b?++n:(f==o1b||f==t1b||f==q1b||f==p1b)&&++e}o=s_(Ncb,353,31,n,0);for(g=0;g<n;++g){o[g]=new R1b;o[g].b=Nr((jr(),$doc),fIc);b.appendChild(o[g].b)}j=0;k=e-1;l=0;p=n-1;c=null;for(h=jhc(new ghc,a.f);h.a<h.b.c-1;){d=lhc(h);i=d.H;q=Nr((jr(),$doc),dIc);i.c=q;i.c[iCc]=i.b;i.c.style[AIc]=i.d;i.c[SBc]=Oyc;i.c[OBc]=Oyc;if(i.a==r1b){gVb(o[l].b,q,o[l].a);q.appendChild(d.J);q[cCc]=k-j+1;++l}else if(i.a==s1b){gVb(o[p].b,q,o[p].a);q.appendChild(d.J);q[cCc]=k-j+1;--p}else if(i.a==n1b){c=q}else if(D1b(i.a)){m=o[l];gVb(m.b,q,m.a++);q.appendChild(d.J);q[s9c]=p-l+1;++j}else if(E1b(i.a)){m=o[l];gVb(m.b,q,m.a);q.appendChild(d.J);q[s9c]=p-l+1;--k}}if(a.a){m=o[l];gVb(m.b,c,m.a);c.appendChild(a.a.J)}}
function F1b(a,b){if(a==r1b){return t9c+b}else if(a==s1b){return u9c+b}else if(a==t1b){return v9c+b}else if(a==o1b){return w9c+b}else if(a==q1b){return x9c+b}else if(a==p1b){return y9c+b}else{return BIc}}
var p9c='100px',z9c='AsyncLoader19',C9c='DockPanel',D9c='DockPanel$DockLayoutConstant',E9c='DockPanel$LayoutData',A9c='DockPanel$TmpRow',B9c='DockPanel$TmpRow;',r9c='Only one CENTER widget may be added',n9c="This is a <code>ScrollPanel<\/code> contained at the center of a <code>DockPanel<\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.<br><br>Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!",j9c='This is the east component',h9c='This is the first north component',i9c='This is the first south component',l9c='This is the second north component',m9c='This is the second south component',k9c='This is the west component',g9c='cw-DockPanel',q9c='cwDockPanel',w9c='east',y9c='lineend',x9c='linestart',t9c='north',f9c='runCallbacks19',u9c='south',v9c='west';_=kib.prototype=new lib;_.gC=wib;_.Ec=Aib;_.tI=0;_=m1b.prototype=new LXb;_.gC=G1b;_.Ic=H1b;_.Xc=I1b;_.tI=222;_.a=null;var n1b,o1b,p1b,q1b,r1b,s1b,t1b;_=J1b.prototype=new cn;_.gC=L1b;_.tI=0;_=M1b.prototype=new cn;_.gC=Q1b;_.tI=0;_.a=null;_.c=null;_=R1b.prototype=new cn;_.gC=T1b;_.tI=223;_.a=0;_.b=null;var D3=Onc(ULc,z9c),a9=Onc(ZNc,A9c),Ncb=Nnc(CQc,B9c),b9=Onc(ZNc,C9c),$8=Onc(ZNc,D9c),_8=Onc(ZNc,E9c);xib();