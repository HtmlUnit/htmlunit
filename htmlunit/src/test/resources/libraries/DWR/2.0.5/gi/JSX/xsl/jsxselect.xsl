<?xml version="1.0"?>
<!--
  ~ Copyright (c) 2001-2007, TIBCO Software Inc.
  ~ Use, modification, and distribution subject to terms of license.
  -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:output method="xml" omit-xml-declaration="yes"/>

  <xsl:param name="jsxtabindex">0</xsl:param>
  <xsl:param name="jsxselectedimage">jsx:///images/select/selected.gif</xsl:param>
  <xsl:param name="jsxtransparentimage">jsx:///images/spc.gif</xsl:param>
  <xsl:param name="jsxdragtype">JSX_GENERIC</xsl:param>
  <xsl:param name="jsxselectedid">null</xsl:param>
  <xsl:param name="jsxsortpath"></xsl:param>
  <xsl:param name="jsxsortdirection">ascending</xsl:param>
  <xsl:param name="jsxsorttype">text</xsl:param>
  <xsl:param name="jsxid">_jsx_blah_21</xsl:param>
  <xsl:param name="jsxapppath"></xsl:param>
  <xsl:param name="jsxabspath"></xsl:param>
  <xsl:param name="jsxdisableescape">no</xsl:param>
  <xsl:param name="jsxshallowfrom"></xsl:param>
  <xsl:param name="jsxnocheck">0</xsl:param>
  <xsl:param name="jsxpath"></xsl:param>
  <xsl:param name="jsxpathapps"></xsl:param>
  <xsl:param name="jsxpathprefix"></xsl:param>

  <xsl:template match="/">
  <JSX_FF_WELLFORMED_WRAPPER><xsl:choose>
      <xsl:when test="$jsxshallowfrom">
        <xsl:for-each select="//*[@jsxid=$jsxshallowfrom]/record">
          <xsl:sort select="@*[name()=$jsxsortpath]" data-type="{$jsxsorttype}" order="{$jsxsortdirection}"/>
          <xsl:apply-templates select="."/>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <xsl:for-each select="//record">
          <xsl:sort select="@*[name()=$jsxsortpath]" data-type="{$jsxsorttype}" order="{$jsxsortdirection}"/>
          <xsl:apply-templates select="."/>
        </xsl:for-each>
      </xsl:otherwise>
    </xsl:choose></JSX_FF_WELLFORMED_WRAPPER>
  </xsl:template>

  <xsl:template match="record">
    <xsl:param name="myjsxid" select="@jsxid"/>

    <div id="{$jsxid}_{$myjsxid}" jsxtype="Option" tabindex="{$jsxtabindex}">
      <xsl:attribute name="title"><xsl:value-of select="@jsxtip"/></xsl:attribute>
      <xsl:attribute name="jsxid"><xsl:value-of select="@jsxid"/></xsl:attribute>
      <xsl:attribute name="class">jsx30select_option</xsl:attribute>
      <xsl:attribute name="onmouseover">jsx3.gui.Select.doFocus(this);</xsl:attribute>
      <xsl:attribute name="onmouseout">jsx3.gui.Select.doBlur(this);</xsl:attribute>
      <xsl:attribute name="onblur">jsx3.gui.Select.doBlur(this);</xsl:attribute>
      <xsl:if test="@jsxstyle">
        <xsl:attribute name="style">
          <xsl:value-of select="@jsxstyle"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="$jsxnocheck != '1'">
        <xsl:choose>
          <xsl:when test="$jsxselectedid=@jsxid">
            <xsl:variable name="src1">
              <xsl:apply-templates select="." mode="uri-resolver">
                <xsl:with-param name="uri" select="$jsxselectedimage"/>
              </xsl:apply-templates>
            </xsl:variable>
            <img unselectable="on" class="jsx30select_check" src="{$src1}"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:variable name="src2">
              <xsl:apply-templates select="." mode="uri-resolver">
                <xsl:with-param name="uri" select="$jsxtransparentimage"/>
              </xsl:apply-templates>
            </xsl:variable>
            <img unselectable="on" class="jsx30select_check" src="{$src2}"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:if>
      <xsl:if test="@jsximg and @jsximg != ''">
        <xsl:variable name="src1">
          <xsl:apply-templates select="." mode="uri-resolver">
            <xsl:with-param name="uri" select="@jsximg"/>
          </xsl:apply-templates>
        </xsl:variable>
        <img unselectable="on" class="jsx30select_icon" src="{$src1}"/>
      </xsl:if>
      <span JSXDragType="{$jsxdragtype}">
        <xsl:attribute name="JSXSpyglass">
          <xsl:value-of select="@jsxid"/>
        </xsl:attribute>
        <xsl:attribute name="JSXDragId">
          <xsl:value-of select="@jsxid"/>
        </xsl:attribute>
        <xsl:apply-templates select="." mode="jsxtext"/>
      </span>
    </div>
  </xsl:template>

  <xsl:template match="record" mode="jsxtext">
    <xsl:choose>
      <xsl:when test="$jsxdisableescape='yes'">
        <xsl:apply-templates select="@jsxtext" mode="disable-output-escp"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="@jsxtext"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <!-- import standard library functions (such as image and resource url resolution) -->
  <xsl:template match="* | @*" mode="uri-resolver">
    <xsl:param name="uri" select="."/>
    <xsl:choose>
      <xsl:when test="starts-with($uri,'JSX/')">
        <xsl:value-of select="concat($jsxpath, $uri)"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'JSXAPPS/')">
        <xsl:value-of select="concat($jsxpathapps, $uri)"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'GI_Builder/')">
        <xsl:value-of select="concat($jsxpath, $uri)"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'jsx:///')">
        <xsl:value-of select="concat($jsxpath, 'JSX/', substring($uri,7))"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'jsxapp:///')">
        <xsl:value-of select="concat($jsxpathapps, substring($uri,10))"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'jsxuser:///')">
        <xsl:value-of select="concat($jsxpathapps, substring($uri,11))"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'jsxaddin://')">
        <!-- cannot resolve addin links in XSL -->
        <xsl:value-of select="$uri"/>
        <!---->
      </xsl:when>
      <xsl:when test="starts-with($uri,'/')">
        <xsl:value-of select="$uri"/>
      </xsl:when>
      <xsl:when test="contains($uri,'://')">
        <xsl:value-of select="$uri"/>
      </xsl:when>
      <xsl:when test="not($jsxpathprefix='') and not(starts-with($uri, $jsxpathprefix))">
        <xsl:apply-templates select="." mode="uri-resolver">
          <xsl:with-param name="uri" select="concat($jsxpathprefix, $uri)"/>
        </xsl:apply-templates>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$uri"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="* | @*" mode="disable-output-escp">
    <xsl:call-template name="disable-output-escp">
      <xsl:with-param name="value" select="."/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="disable-output-escp">
    <xsl:param name="value" select="."/>
    <xsl:choose>
      <xsl:when test="function-available('msxsl:node-set')">
        <xsl:value-of disable-output-escaping="yes" select="$value"/>
      </xsl:when>
      <xsl:otherwise>
        <span class="disable-output-escp"><xsl:value-of select="$value"/></span>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="replace-all">
    <xsl:param name="value" select="."/>
    <xsl:param name="replace" select="''"/>
    <xsl:param name="with" select="''"/>

    <xsl:variable name="first" select="substring-before($value, $replace)"/>
    <xsl:variable name="rest" select="substring-after($value, $replace)"/>

    <xsl:value-of select="$first"/>

    <xsl:if test="$rest">
      <xsl:value-of select="$with"/>
      <xsl:call-template name="replace-all">
        <xsl:with-param name="value" select="$rest"/>
        <xsl:with-param name="replace" select="$replace"/>
        <xsl:with-param name="with" select="$with"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>


</xsl:stylesheet>
