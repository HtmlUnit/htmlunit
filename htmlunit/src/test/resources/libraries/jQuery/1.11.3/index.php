<html>
<?php 
require_once ("java/Java.inc");
$Util = java("php.java.bridge.Util");
$ctx = java_context();
/* get the current instance of the JavaBridge, ServletConfig and Context */
$bridge = $ctx->getAttribute(  "php.java.bridge.JavaBridge",      100);
$config = $ctx->getAttribute ( "php.java.servlet.ServletConfig",  100);
$context = $ctx->getAttribute( "php.java.servlet.ServletContext", 100);
$servlet = $ctx->getAttribute( "php.java.servlet.Servlet", 100);
?>
<head>
   <title>PHP/Java Bridge settings</title>
</head>
<body bgcolor="#FFFFFF">
<H1>PHP/Java Bridge settings</H1>
<p>
The PHP/Java Bridge web application contains two servlets. The <code>PhpJavaServlet</code> handles requests from remote PHP scripts running in Apache/IIS or from the command line. 
The second servlet <code>PhpCGIServlet</code> can handle requests from internet clients directly. 
<p>
The following shows the settings of the <code>PhpJavaServlet</code> and the <code>PhpCGIServlet</code>.
</p>
<H2>PhpJavaServlet</H2>
<p>
The <code>PhpJavaServlet</code> handles requests from PHP clients.
<blockquote>
<code>
Apache/IIS/console::PHP &lt;--&gt; PhpJavaServlet
</code>
</blockquote>

It listens for PHP/Java Bridge protocol requests on the local interface or on all available network interfaces and invokes Java methods or procedures. The following example accesses the bridge listening on the <strong>local</strong> interface:
<blockquote>
<code>
&lt;?php <br>
require_once("http://localhost:8080/JavaBridge/java/Java.inc");<br>
$System = java("java.lang.System");<br>
echo $System->getProperties();<br>
?&gt;
</code>
</blockquote>

</p>
<table BORDER=1 CELLSPACING=5 WIDTH="85%" >
<tr VALIGN=TOP>
<th>Option</th>
<th>Value</th>
<th WIDTH="60%">Description</th>
</tr>
<tr>
<td>servlet_log_level</td>
<td><?php echo java_values($bridge->getlogLevel());?></td>
<td>The request log level.</td>
</tr>
</table>
</p>
<p>
<?php if (java_instanceof ($servlet, java('php.java.servlet.fastcgi.FastCGIServlet'))) { ?>
<H2>PhpCGIServlet</H2>
<p>
The <code>PhpCGIServlet</code> runs PHP scripts within the J2EE/Servlet engine.
</p>
<blockquote>
<code>
internet browser &lt;--&gt; PhpCGIServlet &lt;--&gt; php-cgi &lt;--&gt; PhpJavaServlet
</code>
</blockquote>
<p>
It starts a PHP FastCGI server, if possible and necessary. Requests for PHP scripts are delegated to the FastCGI server. If the PHP code contains Java calls, the PHP/Java Bridge protocol requests are delegated back to the current VM, to an instance of the <code>PhpJavaServlet</code>.
</p>
<table BORDER=1 CELLSPACING=5 WIDTH="85%" >
<tr VALIGN=TOP>
<th>Option</th>
<th>Value</th>
<th WIDTH="60%">Description</th>
</tr>

<tr>
<td>php_exec</td>
<td><?php $val=java_values($context->getInitParameter("php_exec")); echo $val?$val:"php-cgi"?></td>
<td>The name and location of your system_php_exec. For example <code>/opt/PHP/bin/php-cgi</code>. Default is <code>/usr/bin/php-cgi</code> or <code>c:/Program Files/PHP/php-cgi.exe</code> or a <code>php-cgi</code> from the PATH.</td>
</tr>

<tr>
<td>prefer_system_php_exec</td>
<td><?php $val=java_values($context->getInitParameter("prefer_system_php_exec")); echo $val?$val:"Off"?></td>
<td>Ignore a local <code>WEB-INF/cgi/php-cgi-ARCH-OS</code> executable from the web archive and use the specified <code>php_exec</code>. Default is Off.</td>
</tr>

<tr>
<td>php_include_java</td>
<td><?php $val=java_values($context->getInitParameter("php_include_java")); echo $val?$val:"Off"?></td>
<td>Shall the server add <code>&lt?php include_once("java/Java.inc");?&gt;</code> at the top of each PHP script? Default is Off.</td>
</tr>

</table>
</p>

<?php /* current sevlet is PhpCGIServlet */ } ?>

The settings were taken from the <a href="file://<?php 
echo java_values(java('php.java.servlet.ServletUtil')->getRealPath($context, '/WEB-INF/web.xml'))
?>">WEB-INF/web.xml</a>.
</body>
</html>
