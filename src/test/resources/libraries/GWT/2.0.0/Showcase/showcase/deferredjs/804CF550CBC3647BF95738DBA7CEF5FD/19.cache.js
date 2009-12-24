function Afb(){}
function z$b(){}
function W$b(){}
function Z$b(){}
function c_b(){}
function T$b(){return v6}
function Mfb(){return X0}
function Y$b(){return s6}
function b_b(){return t6}
function e_b(){return u6}
function U$b(a){L$b(this,a)}
function R$b(a){if(a==C$b){return true}PD();return a==B$b}
function Q$b(a){if(a==D$b){return true}PD();return a==G$b}
function V$b(a){var b;b=fUb(this,a);if(b){a==this.a&&(this.a=null);M$b(this)}return b}
function N$b(a,b){var c;c=a.H;c.b=b.a;!!c.c&&(c.c[kzc]=b.a,undefined)}
function O$b(a,b){var c;c=a.H;c.d=b.a;!!c.c&&(c.c.style[CFc]=b.a,undefined)}
function a_b(a,b){a.b=(q1b(),n1b).a;a.d=(B1b(),A1b).a;a.a=b;return a}
function J$b(a){H$b();ZUb(a);a.b=(q1b(),n1b);a.c=(B1b(),A1b);a.e[bzc]=0;a.e[azc]=0;return a}
function Nfb(){Ifb=true;Hfb=(Kfb(),new Afb);Lo((Io(),Ho),19);!!$stats&&$stats(pp(X5c,mwc,null,null));Hfb.Fc();!!$stats&&$stats(pp(X5c,$Oc,null,null))}
function H$b(){H$b=Muc;A$b=new W$b;D$b=new W$b;C$b=new W$b;B$b=new W$b;E$b=new W$b;F$b=new W$b;G$b=new W$b}
function K$b(a,b,c){var d;if(c==A$b){if(b==a.a){return}else if(a.a){throw xlc(new ulc,h6c)}}Fsb(b);fec(a.f,b);c==A$b&&(a.a=b);d=a_b(new Z$b,c);b.H=d;N$b(b,a.b);O$b(b,a.c);M$b(a);Hsb(b,a)}
function Qfb(){var a,b,c,d;while(Ffb){a=Ffb;Ffb=Ffb.b;!Ffb&&(Gfb=null);Cub(a.a.a,(c=J$b(new z$b),c.J[Pyc]=Y5c,c.e[bzc]=4,c.b=(q1b(),m1b),K$b(c,yZb(new nZb,Z5c),(H$b(),E$b)),K$b(c,yZb(new nZb,$5c),F$b),K$b(c,yZb(new nZb,_5c),B$b),K$b(c,yZb(new nZb,a6c),G$b),K$b(c,yZb(new nZb,b6c),E$b),K$b(c,yZb(new nZb,c6c),F$b),b=yZb(new nZb,d6c),d=Z8b(new W8b,b),d.J.style[Uyc]=e6c,d.J.style[Qyc]=f6c,K$b(c,d,A$b),L$b(c,g6c),c))}}
function L$b(a,b){var c,d,e,f,g,h,i;_cc(a.J,awc,b);h=zsc(new xsc);i=rec(new oec,a.f);while(i.a<i.b.c-1){c=tec(i);g=c.H.a;e=uZ(h.Vb(g),35);d=!e?1:e.a;f=S$b(g,d);_cc(iQb(c.J),b,f);h.Wb(g,Wlc(d+1))}}
function M$b(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;b=a.d;while(b.children.length>0){b.removeChild(b.children[0])}n=1;e=1;for(h=rec(new oec,a.f);h.a<h.b.c-1;){d=tec(h);f=d.H.a;f==E$b||f==F$b?++n:(f==B$b||f==G$b||f==D$b||f==C$b)&&++e}o=eZ(bab,0,26,n,0);for(g=0;g<n;++g){o[g]=new c_b;o[g].b=pr((Nq(),$doc),hFc);b.appendChild(o[g].b)}j=0;k=e-1;l=0;p=n-1;c=null;for(h=rec(new oec,a.f);h.a<h.b.c-1;){d=tec(h);i=d.H;q=pr((Nq(),$doc),fFc);i.c=q;i.c[kzc]=i.b;i.c.style[CFc]=i.d;i.c[Uyc]=awc;i.c[Qyc]=awc;if(i.a==E$b){sSb(o[l].b,q,o[l].a);q.appendChild(d.J);q[ezc]=k-j+1;++l}else if(i.a==F$b){sSb(o[p].b,q,o[p].a);q.appendChild(d.J);q[ezc]=k-j+1;--p}else if(i.a==A$b){c=q}else if(Q$b(i.a)){m=o[l];sSb(m.b,q,m.a++);q.appendChild(d.J);q[i6c]=p-l+1;++j}else if(R$b(i.a)){m=o[l];sSb(m.b,q,m.a);q.appendChild(d.J);q[i6c]=p-l+1;--k}}if(a.a){m=o[l];sSb(m.b,c,m.a);c.appendChild(a.a.J)}}
function S$b(a,b){if(a==E$b){return j6c+b}else if(a==F$b){return k6c+b}else if(a==G$b){return l6c+b}else if(a==B$b){return m6c+b}else if(a==D$b){return n6c+b}else if(a==C$b){return o6c+b}else{return DFc}}
var f6c='100px',p6c='AsyncLoader19',s6c='DockPanel',t6c='DockPanel$DockLayoutConstant',u6c='DockPanel$LayoutData',q6c='DockPanel$TmpRow',r6c='DockPanel$TmpRow;',h6c='Only one CENTER widget may be added',d6c="This is a <code>ScrollPanel<\/code> contained at the center of a <code>DockPanel<\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.<br><br>Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!",_5c='This is the east component',Z5c='This is the first north component',$5c='This is the first south component',b6c='This is the second north component',c6c='This is the second south component',a6c='This is the west component',Y5c='cw-DockPanel',g6c='cwDockPanel',m6c='east',o6c='lineend',n6c='linestart',j6c='north',X5c='runCallbacks19',k6c='south',l6c='west';_=Afb.prototype=new Bfb;_.gC=Mfb;_.Fc=Qfb;_.tI=0;_=z$b.prototype=new XUb;_.gC=T$b;_.Jc=U$b;_.Yc=V$b;_.tI=202;_.a=null;var A$b,B$b,C$b,D$b,E$b,F$b,G$b;_=W$b.prototype=new Gm;_.gC=Y$b;_.tI=0;_=Z$b.prototype=new Gm;_.gC=b_b;_.tI=0;_.a=null;_.c=null;_=c_b.prototype=new Gm;_.gC=e_b;_.tI=203;_.a=0;_.b=null;var X0=Ykc(LIc,p6c),u6=Ykc(QKc,q6c),bab=Xkc(tNc,r6c),v6=Ykc(QKc,s6c),s6=Ykc(QKc,t6c),t6=Ykc(QKc,u6c);Nfb();