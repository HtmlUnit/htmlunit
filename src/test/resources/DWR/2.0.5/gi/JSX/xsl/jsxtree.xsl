<?xml version="1.0"?>
<!--
  ~ Copyright (c) 2001-2007, TIBCO Software Inc.
  ~ Use, modification, and distribution subject to terms of license.
  -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:output method="xml" omit-xml-declaration="yes"/>

  <xsl:param name="jsxtabindex">0</xsl:param>
  <xsl:param name="jsxicon">jsx:///images/tree/file.gif</xsl:param>
  <xsl:param name="jsxiconminus">jsx:///images/tree/minus.gif</xsl:param>
  <xsl:param name="jsxiconplus">jsx:///images/tree/plus.gif</xsl:param>
  <xsl:param name="jsxtransparentimage">jsx:///images/spc.gif</xsl:param>
  <xsl:param name="jsxdragtype">JSX_GENERIC</xsl:param>
  <xsl:param name="jsxrootid">jsxnull</xsl:param>
  <xsl:param name="jsxselectedimage">jsx:///images/tree/selected.gif</xsl:param>
  <xsl:param name="jsxbordercolor">#8d9ec1</xsl:param>
  <xsl:param name="jsxid">_jsx_JSX1_12</xsl:param>
  <xsl:param name="jsxuseroot">1</xsl:param>
  <xsl:param name="jsxapppath"></xsl:param>
  <xsl:param name="jsxabspath"></xsl:param>
  <xsl:param name="jsxsortpath"></xsl:param>
  <xsl:param name="jsxsortdirection">ascending</xsl:param>
  <xsl:param name="jsxsorttype">text</xsl:param>
  <xsl:param name="jsxdeepfrom">jsxnull</xsl:param>
  <xsl:param name="jsxfragment">0</xsl:param>
  <xsl:param name="jsxindent">20</xsl:param>
  <xsl:param name="jsx_no_empty_indent">0</xsl:param>
  <xsl:param name="jsx_img_resolve">1</xsl:param>
  <xsl:param name="jsxpath"></xsl:param>
  <xsl:param name="jsxpathapps"></xsl:param>
  <xsl:param name="jsxpathprefix"></xsl:param>

  <xsl:template match="/">
    <JSX_FF_WELLFORMED_WRAPPER>
      <xsl:choose>
        <xsl:when test="$jsxdeepfrom != 'jsxnull' and $jsxfragment != '1'">
          <xsl:apply-templates select="//*[@jsxid=$jsxdeepfrom]"/>
        </xsl:when>
        <xsl:when test="$jsxuseroot=1">
          <xsl:apply-templates select="//*[@jsxid=$jsxrootid]"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:for-each select="//*[@jsxid=$jsxrootid]/record">
            <xsl:sort select="@*[name()=$jsxsortpath]" data-type="{$jsxsorttype}" order="{$jsxsortdirection}"/>
            <xsl:apply-templates select="."/>
          </xsl:for-each>
        </xsl:otherwise>
      </xsl:choose>
    </JSX_FF_WELLFORMED_WRAPPER>
  </xsl:template>

  <xsl:template match="*">
    <xsl:param name="myjsxid" select="@jsxid"/>
    <xsl:param name="mystyle" select="@jsxstyle"/>
    <xsl:param name="myclass" select="@jsxclass"/>

    <!-- TO DO: shouldn't affect performance to resolve all of the following, but look into how very large trees perform when rendered -->
    <xsl:param name="src1">
      <xsl:apply-templates select="." mode="uri-resolver">
        <xsl:with-param name="uri" select="$jsxiconminus"/>
      </xsl:apply-templates>
    </xsl:param>
    <xsl:param name="src2">
      <xsl:apply-templates select="." mode="uri-resolver">
        <xsl:with-param name="uri" select="$jsxiconplus"/>
      </xsl:apply-templates>
    </xsl:param>
    <xsl:param name="src3">
      <xsl:apply-templates select="." mode="uri-resolver">
        <xsl:with-param name="uri" select="$jsxtransparentimage"/>
      </xsl:apply-templates>
    </xsl:param>
    <xsl:param name="src4">
      <xsl:choose>
        <xsl:when test="$jsx_img_resolve='1'"><xsl:apply-templates select="@jsximg" mode="uri-resolver"/></xsl:when>
        <xsl:otherwise><xsl:value-of select="@jsximg"/></xsl:otherwise>
      </xsl:choose>
    </xsl:param>
    <xsl:param name="src5">
      <xsl:apply-templates select="." mode="uri-resolver">
        <xsl:with-param name="uri" select="$jsxicon"/>
      </xsl:apply-templates>
    </xsl:param>

    <xsl:variable name="_jsxstyle">
      <xsl:if test="$jsxselectedimage">background-image:url(<xsl:value-of select="$jsxselectedimage"/>);</xsl:if>
      <xsl:if test="$jsxbordercolor"><xsl:text>border-right:solid 1px </xsl:text><xsl:value-of select="$jsxbordercolor"/>;</xsl:if>
    </xsl:variable>

    <div jsxtype='item' class='jsx30tree_item' id="{$jsxid}_{$myjsxid}" unselectable="on">
      <xsl:attribute name="jsxid">
        <xsl:value-of select="@jsxid"/>
      </xsl:attribute>
      <div jsxtype='caption' class='jsx30tree_caption' unselectable="on">
        <!-- plus/minus icon -->
        <xsl:choose>
          <xsl:when test="(record or (@jsxlazy > 0)) and @jsxopen=1">
            <img jsxtype="plusminus" class="jsx30tree_pm" src="{$src1}"/>
          </xsl:when>
          <xsl:when test="(record or (@jsxlazy > 0))">
            <img jsxtype="plusminus" class="jsx30tree_pm" src="{$src2}"/>
          </xsl:when>
          <xsl:when test="$jsx_no_empty_indent='1' and not(../record/record)">
            <span class="jsx30tree_empty">
              <xsl:text>&#160;</xsl:text>
            </span>
          </xsl:when>
          <xsl:otherwise>
            <img jsxtype="space" class="jsx30tree_pm" src="{$src3}"/>
          </xsl:otherwise>
        </xsl:choose>
        <!-- image icon -->
        <xsl:choose>
          <xsl:when test="@jsximg='' or (not(@jsximg) and $jsxicon='')">
            <span class="jsx30tree_empty">
              <xsl:text>&#160;</xsl:text>
            </span>
          </xsl:when>
          <xsl:when test="@jsximg">
            <img jsxtype="icon" unselectable="on" class="jsx30tree_icon" src="{$src4}"/>
          </xsl:when>
          <xsl:otherwise>
            <img jsxtype="icon" unselectable="on" class="jsx30tree_icon" src="{$src5}"/>
          </xsl:otherwise>
        </xsl:choose>
        <!-- record text -->
        <span jsxtype="text" unselectable="on" class="jsx30tree_text {$myclass}" tabindex="{$jsxtabindex}"
            JSXDragType="{$jsxdragtype}">
          <xsl:attribute name="style">
            <xsl:if test="@jsxselected='1'">
              <xsl:value-of select="$_jsxstyle"/>
            </xsl:if>
            <xsl:value-of select="@jsxstyle"/>
            <xsl:value-of select="$mystyle"/>
          </xsl:attribute>
          <xsl:choose>
            <xsl:when test="@jsxtip">
              <xsl:attribute name="title">
                <xsl:value-of select="@jsxtip"/>
              </xsl:attribute>
            </xsl:when>
          </xsl:choose>
          <xsl:attribute name="JSXSpyglass">
            <xsl:value-of select="@jsxid"/>
          </xsl:attribute>
          <xsl:attribute name="JSXDragId">
            <xsl:value-of select="@jsxid"/>
          </xsl:attribute>
          <xsl:value-of select="@jsxtext"/>
        </span>
      </div>
      <!-- child records -->
      <div jsxtype="content" unselectable="on" class='jsx30tree_content'>
        <xsl:choose>
          <xsl:when test="record">
            <xsl:attribute name="style">
              <xsl:if test="@jsxopen='1'">display:block;</xsl:if>
              <xsl:text>padding-left:</xsl:text>
              <xsl:value-of select="$jsxindent"/>
              <xsl:text>px;</xsl:text>
            </xsl:attribute>
            <!-- recurse here -->
            <xsl:for-each select="record">
              <xsl:sort select="@*[name()=$jsxsortpath]" data-type="{$jsxsorttype}" order="{$jsxsortdirection}"/>
              <xsl:apply-templates select="."/>
            </xsl:for-each>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>&#160;</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </div>
    </div>
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
