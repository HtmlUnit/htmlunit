<?xml version="1.0"?>
<!--
  ~ Copyright (c) 2001-2007, TIBCO Software Inc.
  ~ Use, modification, and distribution subject to terms of license.
  -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:output method="xml" omit-xml-declaration="yes"/>

  <xsl:param name="jsxtabindex">0</xsl:param>
  <xsl:param name="jsxselectedimage">jsx:///images/menu/selected.gif</xsl:param>
  <xsl:param name="jsxtransparentimage">jsx:///images/spc.gif</xsl:param>
  <xsl:param name="jsxsubmenuimage">jsx:///images/menu/submenuarrow.gif</xsl:param>
  <xsl:param name="jsxdragtype">JSX_GENERIC</xsl:param>
  <xsl:param name="jsxrootid">jsxroot</xsl:param>
  <xsl:param name="jsxid">jsxroot</xsl:param>
  <xsl:param name="jsxindex">0</xsl:param>
  <xsl:param name="jsxsortpath"></xsl:param>
  <xsl:param name="jsxsortdirection">ascending</xsl:param>
  <xsl:param name="jsxsorttype">text</xsl:param>
  <xsl:param name="jsxapppath"></xsl:param>
  <xsl:param name="jsxabspath"></xsl:param>
  <xsl:param name="jsxdisableescape">no</xsl:param>
  <xsl:param name="jsxmode">0</xsl:param>
  <xsl:param name="jsxkeycodes"></xsl:param>
  <xsl:param name="jsxpath"></xsl:param>
  <xsl:param name="jsxpathapps"></xsl:param>
  <xsl:param name="jsxpathprefix"></xsl:param>

  <xsl:template match="/">
    <JSX_FF_WELLFORMED_WRAPPER>
      <xsl:apply-templates select="//*[@jsxid=$jsxrootid]"/>
    </JSX_FF_WELLFORMED_WRAPPER>
  </xsl:template>

  <xsl:template match="*">
    <xsl:param name="mystyle" select="@jsxstyle"/>

    <!-- Resolve $jsxsubmenuimage to $_jsxsubmenuimage -->
    <xsl:variable name="_jsxsubmenuimage">
      <xsl:apply-templates select="." mode="uri-resolver">
        <xsl:with-param name="uri" select="$jsxsubmenuimage"/>
      </xsl:apply-templates>
    </xsl:variable>

    <xsl:for-each select="record">
      <xsl:sort select="@*[name()=$jsxsortpath]" data-type="{$jsxsorttype}" order="{$jsxsortdirection}"/>
      <xsl:choose>
        <xsl:when test="@jsxdivider[.='1']">
          <div class="jsx30menu_{$jsxmode}_item_divider" tabindex="{$jsxtabindex}" jsxtype="Disabled" jsxdiv="true">
            <xsl:variable name="src1">
              <xsl:apply-templates select="." mode="uri-resolver">
                <xsl:with-param name="uri" select="$jsxtransparentimage"/>
              </xsl:apply-templates>
            </xsl:variable>
            <img style="position:relative;width:1px;height:1px;border-width:0px;" src="{$src1}"/>
          </div>
        </xsl:when>
      </xsl:choose>
      <div id="{$jsxid}_{@jsxid}" tabindex="{$jsxtabindex}"
          onmouseover="jsx3.GO('{$jsxid}').doFocus(jsx3.gui.Event.wrap(event),this,{$jsxindex});"
          onmouseout="jsx3.GO('{$jsxid}').doBlur(jsx3.gui.Event.wrap(event),this);"
          onblur="jsx3.GO('{$jsxid}').doBlur(jsx3.gui.Event.wrap(event),this);"
          onfocus="jsx3.GO('{$jsxid}').doFocus(jsx3.gui.Event.wrap(event),this,{$jsxindex});">
        <xsl:attribute name="jsxid">
          <xsl:value-of select="@jsxid"/>
        </xsl:attribute>
        <xsl:choose>
          <xsl:when test="@jsxdisabled[.='1']">
            <xsl:attribute name="class">jsx30menu_<xsl:value-of select="$jsxmode"/>_item_disabled</xsl:attribute>
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="class">jsx30menu_<xsl:value-of select="$jsxmode"/>_item</xsl:attribute>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
          <xsl:when test="@jsxtip">
            <xsl:attribute name="title">
              <xsl:value-of select="@jsxtip"/>
            </xsl:attribute>
          </xsl:when>
        </xsl:choose>
        <xsl:choose>
          <xsl:when test="@jsxdisabled='1'">
            <xsl:attribute name="jsxtype">Disabled</xsl:attribute>
          </xsl:when>
          <xsl:when test="record">
            <xsl:attribute name="jsxtype">Book</xsl:attribute>
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="jsxtype">Leaf</xsl:attribute>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
          <xsl:when test="@jsximg">
            <xsl:variable name="src1">
              <xsl:apply-templates select="@jsximg" mode="uri-resolver"/>
            </xsl:variable>
            <img style="position:absolute;left:2px;top:2px;width:16px;height:16px;" src="{$src1}"/>
          </xsl:when>
        </xsl:choose>
        <xsl:choose>
          <xsl:when test="@jsxselected = 1">
            <xsl:variable name="src1">
              <xsl:apply-templates select="." mode="uri-resolver">
                <xsl:with-param name="uri" select="$jsxselectedimage"/>
              </xsl:apply-templates>
            </xsl:variable>
            <img class="jsx30menu_{$jsxmode}_selected" src="{$src1}"/>
          </xsl:when>
        </xsl:choose>
        <xsl:choose>
          <xsl:when test="record">

            <div class="jsx30menu_{$jsxmode}_kc" JSXDragType="{$jsxdragtype}" style="{$mystyle}">
              <xsl:attribute name="JSXDragId">
                <xsl:value-of select="@jsxid"/>
              </xsl:attribute>
              <xsl:attribute name="JSXSpyglass">
                <xsl:value-of select="@jsxid"/>
              </xsl:attribute>
              <table class="jsx30menu_{$jsxmode}_kct">
                <tr>
                  <td class="name">
                    <xsl:apply-templates select="." mode="jsxtext"/>
                  </td>
                  <td class="keycode" style="background-image:url({$_jsxsubmenuimage});background-position:right 2px;background-repeat:no-repeat;">&#160;</td>
                </tr>
              </table>
            </div>

          </xsl:when>
          <xsl:otherwise>
            <div class="jsx30menu_{$jsxmode}_kc" JSXDragType="{$jsxdragtype}" style="{$mystyle}">
              <xsl:attribute name="JSXDragId">
                <xsl:value-of select="@jsxid"/>
              </xsl:attribute>
              <xsl:attribute name="JSXSpyglass">
                <xsl:value-of select="@jsxid"/>
              </xsl:attribute>
              <table class="jsx30menu_{$jsxmode}_kct">
                <tr>
                  <td class="name">
                    <xsl:apply-templates select="." mode="jsxtext"/>
                  </td>
                  <xsl:if test="@jsxkeycode">
                    <td class="keycode">
                      <xsl:apply-templates select="." mode="keycode"/>
                    </td>
                  </xsl:if>
                </tr>
              </table>
            </div>
          </xsl:otherwise>
        </xsl:choose>
      </div>
    </xsl:for-each>
  </xsl:template>

  <!-- import standard library functions (such as image and resource url resolution) -->
  <xsl:template match="record" mode="keycode">
    <xsl:variable name="after" select="substring-after($jsxkeycodes, concat(@jsxid,':'))"/>
    <xsl:choose>
      <xsl:when test="$after">
        <xsl:value-of select="substring-before($after, '|')"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="@jsxkeycode"/>
      </xsl:otherwise>
    </xsl:choose>
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
