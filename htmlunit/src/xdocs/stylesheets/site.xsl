<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="pathToRoot"/>

<xsl:template match="document">
<html><head><title><xsl:value-of select="@title"/></title>
<style>
body {  background-color: #FFFFFF; margin: 10px 10px 10px 10px; font-family: Arial, Helvetica, sans-serif;  font-size : 12px;  }

a:link { color: #0F3660; }
a:visited { color: #009999; }
a:active { color: #000066; }
a:hover { color: #000066; }

.date { font-size: 11px; }

.box-frame-blue { font-size: 12px;border: solid 1px #7099C5; text-align: left; background-color: #f0f0ff; }
.box-title-blue { padding: 0px 0px 0px 2px; background-color: #7099C5; color: #ffffff; }

.box-frame-red { font-size: 12px;border: solid 1px #7099C5; text-align: left; background-color: #fff0f0; }
.box-title-red { padding: 0px 0px 0px 2px; background-color: #D00000; color: #ffffff; }

.box-frame-yellow { font-size: 12px;border: solid 1px #7099C5; text-align: left; background-color: #FAF9C3; }
.box-title-yellow { padding: 0px 0px 0px 2px; background-color: #C6C600; color: #ffffff; }

.box-frame-gray { font-size: 12px; border: solid 1px #CFDCED; text-align: left; background-color: #f0f0f0; }
.box-title-gray { padding: 0px 0px 0px 2px; background-color: #c0c0c0; color: #ffffff; }

.box-content {padding: 5px 5px 5px 10px;}

.colouredTable { font-size: 12px; border: solid 1px #CFDCED; text-align: left; background-color: #f0f0f0; }
thead { padding: 0px 0px 0px 2px; background-color: #c0c0c0; color: #ffffff; text-align: center; }
tbody {padding: 5px 5px 5px 10px;}

</style>
</head><body>
	<table>
		<tr>
			<td valign="top" align="center">
				<a href="{$pathToRoot}index.html">
					<img border="0">
						<xsl:attribute name="src"><xsl:value-of select="$pathToRoot"/>HtmlUnitLogo.jpg</xsl:attribute>
						</img>
				</a>
			</td>
			<td valign="top">
				<h1><xsl:value-of select="@title"/></h1>
				<xsl:for-each select="section">
					<xsl:if test="@title">
						<h2><xsl:value-of select="@title"/></h2>
					</xsl:if>
					<xsl:apply-templates select="."/>
				</xsl:for-each>
			</td>
		</tr>
	</table>
	<p align="right">
		<a href="http://sourceforge.net">
		</a>
		<a href="http://sourceforge.net">
			<img src="http://sourceforge.net/sflogo.php?group_id=47038&amp;type=2"
				width="125" height="37" border="0" alt="SourceForge.net Logo" />
		</a>
	</p>

</body></html>
</xsl:template>


<xsl:template match="node()|@*" priority="-1">
	<xsl:copy>
		<xsl:apply-templates select="@*"/>
		<xsl:apply-templates/>
	</xsl:copy>
</xsl:template>


<xsl:template match="table|@*" priority="-1">
	<xsl:copy>
		<xsl:apply-templates select="@*"/>
		<xsl:attribute name="class">colouredTable</xsl:attribute>
		<xsl:attribute name="cellpadding">0</xsl:attribute>
		<xsl:attribute name="cellspacing">0</xsl:attribute>
		<xsl:apply-templates/>
	</xsl:copy>
</xsl:template>


<xsl:template match="td|@*" priority="-1">
	<xsl:copy>
		<xsl:apply-templates select="@*"/>
		<xsl:attribute name="style">border: solid 1px #CFDCED;padding: 2pt 2pt 2pt 2pt</xsl:attribute>
		<xsl:apply-templates/>
	</xsl:copy>
</xsl:template>


<xsl:template match="note">
	<xsl:call-template name="colouredBox">
		<xsl:with-param name="colour">blue</xsl:with-param>
		<xsl:with-param name="title" select="@title" />
		<xsl:with-param name="defaultTitle">Note</xsl:with-param>
	</xsl:call-template>
</xsl:template>


<xsl:template match="warning">
	<xsl:call-template name="colouredBox">
		<xsl:with-param name="colour">red</xsl:with-param>
		<xsl:with-param name="title" select="@title" />
		<xsl:with-param name="defaultTitle">Warning</xsl:with-param>
	</xsl:call-template>
</xsl:template>


<xsl:template match="todo">
	<xsl:call-template name="colouredBox">
		<xsl:with-param name="colour">yellow</xsl:with-param>
		<xsl:with-param name="title" select="@title" />
		<xsl:with-param name="defaultTitle">Still to do</xsl:with-param>
	</xsl:call-template>
</xsl:template>


<xsl:template match="source">
	<xsl:call-template name="colouredBox">
		<xsl:with-param name="colour">gray</xsl:with-param>
		<xsl:with-param name="title" select="@title" />
		<xsl:with-param name="isFixedFont">true</xsl:with-param>
	</xsl:call-template>
</xsl:template>


<xsl:template name="colouredBox">
	<xsl:param name="colour"/>
	<xsl:param name="title"/>
	<xsl:param name="defaultTitle"/>
	<xsl:param name="isFixedFont"/>

	<div align="center">
		<table cellpadding="0" cellspacing="0" width="95%">
			<xsl:attribute name="class">box-frame-<xsl:value-of select="$colour"/></xsl:attribute>
			<xsl:if test="$title or $defaultTitle">
			<tr>
				<td>
					<xsl:attribute name="class">box-title-<xsl:value-of select="$colour"/></xsl:attribute>
					<xsl:choose>
						<xsl:when test="$title">
							<xsl:value-of select="$title"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$defaultTitle"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			</xsl:if>
			<tr>
				<td class="box-content">
					<xsl:choose>
						<xsl:when test="$isFixedFont">
							<pre><xsl:apply-templates/></pre>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
	</div>
</xsl:template>


<xsl:template match="faq">
	<xsl:for-each select="faq-entry">
		<p>
			<fieldset>
				<xsl:for-each select="question">
					<p><i><xsl:apply-templates/></i></p>
				</xsl:for-each>
				<xsl:for-each select="answer">
					<p><xsl:apply-templates/></p>
				</xsl:for-each>
			</fieldset>
		</p>
	</xsl:for-each>
</xsl:template>


<xsl:template match="change-log">
	<table>
		<xsl:for-each select="change">
			<tr>
				<td valign="top">
					<xsl:choose>
						<xsl:when test="@type = 'fix'">
							<img src="{$pathToRoot}bullet-red.gif" alt="Bug fix" />
						</xsl:when>
						<xsl:when test="@type = 'new'">
							<img src="{$pathToRoot}bullet-green.gif" alt="New feature" />
						</xsl:when>
						<xsl:when test="@type = 'change'">
							<img src="{$pathToRoot}bullet-yellow.gif" alt="Modification" />
						</xsl:when>
						<xsl:when test="@type = 'documentation'">
							<img src="{$pathToRoot}bullet-purple.gif" alt="Documentation change" />
						</xsl:when>
						<xsl:otherwise>
							UNKNOWN TYPE: <xsl:value-of select="@type"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td valign="top">
					<xsl:apply-templates/>
				</td>
			</tr>
		</xsl:for-each>
	</table>
</xsl:template>


<xsl:template match="directory-structure">
	<ul>
		<xsl:apply-templates/>
	</ul>
</xsl:template>


<xsl:template match="directory">
	<li>
		<b><xsl:value-of select="@name"/></b>
		<xsl:if test="*">
			<ul>
				<xsl:apply-templates/>
			</ul>
		</xsl:if>
	</li>
</xsl:template>


<xsl:template match="file">
	<li>
		<b><xsl:value-of select="@name"/></b>
		<xsl:if test="text()">
			<br /><xsl:apply-templates/>
		</xsl:if>
	</li>
</xsl:template>


<xsl:template match="javadoc">
	<xsl:variable name="fullname" select="."/>
	<xsl:choose>
		<xsl:when test="contains($fullname, '#')">
			<a href="api/{translate(substring-before($fullname,'#'),'.','/')}.html#{substring-after($fullname,'#')}">
				<xsl:call-template name="lastElementOf">
					<xsl:with-param name="string">
						<xsl:value-of select="substring-before($fullname,'#')"/>
					</xsl:with-param>
				</xsl:call-template>
				.<xsl:value-of select="substring-before(substring-after($fullname,'#'),'(')"/>()
			</a>
		</xsl:when>
		<xsl:otherwise>
			<a href="api/{translate($fullname,'.','/')}.html">
				<xsl:call-template name="lastElementOf">
					<xsl:with-param name="string">
						<xsl:value-of select="$fullname"/>
					</xsl:with-param>
				</xsl:call-template>
			</a>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>


<xsl:template name="lastElementOf">
	<xsl:param name="string"/>
	<xsl:choose>
		<xsl:when test="contains($string, '.')">
			<xsl:call-template name="lastElementOf">
				<xsl:with-param name="string">
					<xsl:value-of select="substring-after($string,'.')"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$string"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>


<xsl:template match="date">
	<span class="date">(<xsl:apply-templates/>)</span>
</xsl:template>


<xsl:template match="version-string">
	<span class="date">(Version: <xsl:apply-templates/>)</span>
</xsl:template>


</xsl:stylesheet>
