<?xml version="1.0"?>
<!--
  ~ Copyright (c) 2001-2007, TIBCO Software Inc.
  ~ Use, modification, and distribution subject to terms of license.
  -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" omit-xml-declaration="yes"/>
  <xsl:param name="jsxtabindex">0</xsl:param>
  <xsl:param name="jsxselectionbg">url(JSX/images/list/select.gif)</xsl:param>
  <xsl:param name="jsxtransparentimage">JSX/images/spc.gif</xsl:param>
  <xsl:param name="jsxid">_jsx_JSX1_12</xsl:param>
  <xsl:param name="jsxsortpath"></xsl:param>
  <xsl:param name="jsxsortdirection">ascending</xsl:param>
  <xsl:param name="jsxsorttype">text</xsl:param>
  <xsl:param name="jsxrowclass">jsx30list_r1</xsl:param>
  <xsl:param name="jsxdragtype">JSX_GENERIC</xsl:param>
  <xsl:param name="jsxapppath"></xsl:param>
  <xsl:param name="jsxabspath"></xsl:param>
  <xsl:param name="jsxrowid">jsxnull</xsl:param>
  <xsl:param name="jsxbg1"></xsl:param>
  <xsl:param name="jsxbg2"></xsl:param>
  <xsl:param name="jsxshallowfrom"></xsl:param>
  <xsl:param name="pagingminexclusive">-1</xsl:param>
  <xsl:param name="pagingmaxexclusive"></xsl:param>

  <xsl:template match="/">
    <xsl:choose>
      <xsl:when test="$jsxrowid!='jsxnull'">
        <xsl:for-each select="//record">
          <xsl:sort select="@*[name()=$jsxsortpath]" data-type="{$jsxsorttype}" order="{$jsxsortdirection}"/>
          <xsl:choose><xsl:when test="@jsxid = $jsxrowid">
            <xsl:apply-templates select=".">
              <xsl:with-param name="myjsxposition" select="position()"/>
            </xsl:apply-templates>
          </xsl:when></xsl:choose>
        </xsl:for-each>
      </xsl:when>
      <xsl:when test="$pagingmaxexclusive">
        <xsl:for-each select="//record">
          <xsl:sort select="@*[name()=$jsxsortpath]" data-type="{$jsxsorttype}" order="{$jsxsortdirection}"/>
          <xsl:choose><xsl:when test="position() &gt; $pagingminexclusive and position() &lt; $pagingmaxexclusive">
            <xsl:apply-templates select=".">
              <xsl:with-param name="myjsxposition" select="position()"/>
            </xsl:apply-templates>
          </xsl:when></xsl:choose>
        </xsl:for-each>
      </xsl:when>
      <xsl:when test="$jsxshallowfrom">
        <xsl:for-each select="//*[@jsxid=$jsxshallowfrom]/record">
          <xsl:sort select="@*[name()=$jsxsortpath]" data-type="{$jsxsorttype}" order="{$jsxsortdirection}"/>
          <xsl:apply-templates select=".">
            <xsl:with-param name="myjsxposition" select="position()"/>
          </xsl:apply-templates>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <xsl:for-each select="//record">
          <xsl:sort select="@*[name()=$jsxsortpath]" data-type="{$jsxsorttype}" order="{$jsxsortdirection}"/>
          <xsl:apply-templates select=".">
            <xsl:with-param name="myjsxposition" select="position()"/>
          </xsl:apply-templates>
        </xsl:for-each>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="record">
    <xsl:param name="myjsxid" select="@jsxid"/>
    <xsl:param name="myjsxgroupname">
      <xsl:choose>
        <xsl:when test="@jsxgroupname"><xsl:value-of select="@jsxgroupname"/></xsl:when>
        <xsl:otherwise><xsl:value-of select="$jsxid"/></xsl:otherwise>
      </xsl:choose>
    </xsl:param>
    <xsl:param name="myjsxposition">0</xsl:param>
    <tr id="{$jsxid}_{$myjsxid}" JSXDragId="{$myjsxid}" JSXDragType="{$jsxdragtype}" tabindex="{$jsxtabindex}" class="{$jsxrowclass}" onfocus="jsx3.gui.List.doFocusItem(this);" onblur="jsx3.gui.List.doBlurItem(this);">
      <xsl:attribute name="title"><xsl:value-of select="@jsxtip"/></xsl:attribute>
      <xsl:choose>
        <xsl:when test="@jsxselected">
          <xsl:attribute name="style">background-color:<xsl:choose>
            <xsl:when test="$myjsxposition mod 2 = 0">
              <xsl:value-of select="$jsxbg1"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$jsxbg2"/>
            </xsl:otherwise>
          </xsl:choose>;background-image:<xsl:value-of select="$jsxselectionbg"/>;<xsl:value-of select="@jsxstyle"/>;</xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="style">background-color:<xsl:choose>
            <xsl:when test="$myjsxposition mod 2 = 0"><xsl:value-of select="$jsxbg1"/></xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$jsxbg2"/>
            </xsl:otherwise>
          </xsl:choose>;<xsl:value-of select="@jsxstyle"/>;</xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:comment>JSXUNCONFIGURED</xsl:comment>
    </tr>
  </xsl:template>

</xsl:stylesheet>
