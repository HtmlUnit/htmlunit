<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright (c) 2001-2007, TIBCO Software Inc.
  ~ Use, modification, and distribution subject to terms of license.
  -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output omit-xml-declaration="yes" method="xml"/>
  <xsl:param name="includexml" select="'yes'" />
  <xsl:template match="/">
    <div class="prettyxml">
      <xsl:for-each select="node()">
        <xsl:call-template name="formatxml">
          <xsl:with-param name="selnode" select="."/>
          <xsl:with-param name="indent" select="0"/>
        </xsl:call-template>
      </xsl:for-each>
    </div>
  </xsl:template>
  <xsl:template name="formatxml">
    <xsl:param name="selnode"/>
    <xsl:param name="indent">-1</xsl:param>
    <div>
      <xsl:if test="$indent='0'">
        <xsl:attribute name="class">root</xsl:attribute>
      </xsl:if>
      <xsl:if test="$indent='1' and (self::comment() or (self::processing-instruction() or (not(self::text()))))">
        <xsl:attribute name="class">indent</xsl:attribute>
      </xsl:if>
      <xsl:choose>
        <xsl:when test="self::comment()">
          <span class="ts cs">
            <xsl:text>&lt;!--</xsl:text>
            <xsl:value-of select="$selnode"/>
            <xsl:text>&gt;</xsl:text>
          </span>
        </xsl:when>
        <xsl:when test="self::processing-instruction()">
          <span class="ts pis">
            <xsl:text>&lt;?</xsl:text>
            <xsl:value-of select="name()"/>
            <xsl:text/>
            <xsl:value-of select="."/>
            <xsl:text>?&gt;</xsl:text>
          </span>
        </xsl:when>
        <xsl:when test="self::text()">
          <xsl:attribute name="style"><xsl:text>display:inline;</xsl:text></xsl:attribute>
          <span class="ts tns">
            <xsl:call-template name="html-replace-entities">
              <xsl:with-param name="text" select="$selnode"/>
            </xsl:call-template>
          </span>
        </xsl:when>
        <xsl:otherwise>
          <span class="ts ps">
            <xsl:text>&lt;</xsl:text>
          </span>
          <span class="ts nns">
            <span class="eln"><xsl:value-of select="name($selnode)"/></span>
            <xsl:for-each select="$selnode/@*">
              <xsl:text> </xsl:text>
              <span class="ts ans">
                <xsl:value-of select="name()"/>
              </span>
              <span class="ts ps">
                <xsl:text>=&quot;</xsl:text>
              </span>
              <span class="ts avs">
                <xsl:call-template name="html-replace-entities">
                  <xsl:with-param name="text" select="."/>
                </xsl:call-template>
              </span>
              <span class="ts ps">
                <xsl:text>&quot;</xsl:text>
              </span>
            </xsl:for-each>
          </span>
          <xsl:choose>
            <xsl:when test="count(node()) = 0">
              <span class="ts ps">
                <xsl:text>/&gt;</xsl:text>
              </span>
            </xsl:when>
            <xsl:otherwise>
              <span class="ts ps">
                <xsl:text>&gt;</xsl:text>
              </span>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="$selnode/node()">
        <xsl:call-template name="formatxml">
          <xsl:with-param name="selnode" select="."/>
          <xsl:with-param name="indent">1</xsl:with-param>
        </xsl:call-template>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="self::text()"/>
        <xsl:when test="count(node()) = 0"/>
        <xsl:otherwise>
         <div class="closing">
           <xsl:if test="not(./*)">
             <xsl:attribute name="style"><xsl:text>display:inline;</xsl:text></xsl:attribute>
           </xsl:if>
         <span style="white-space:nowrap;">
          <span class="ts ps" style="white-space:nowrap;">
            <xsl:text>&lt;/</xsl:text>
          </span>
          <span class="ts nns">
            <span class="eln"><xsl:value-of select="name($selnode)"/></span>
          </span>
          <span class="ts ps">
            <xsl:text>&gt;</xsl:text>
          </span>
         </span>
         </div>
        </xsl:otherwise>
      </xsl:choose>
    </div>
  </xsl:template>
  
  <xsl:template name="html-replace-entities">
    <xsl:param name="text"/>
    <xsl:variable name="tmp">
      <xsl:call-template name="replace-substring">
        <xsl:with-param name="from" select="'&gt;'"/>
        <xsl:with-param name="to" select="'&amp;gt;'"/>
        <xsl:with-param name="value">
          <xsl:call-template name="replace-substring">
            <xsl:with-param name="from" select="'&lt;'"/>
            <xsl:with-param name="to" select="'&amp;lt;'"/>
            <xsl:with-param name="value">
              <xsl:call-template name="replace-substring">
                <xsl:with-param name="from" select="'&quot;'"/>
                <xsl:with-param name="to" select="'&amp;quot;'"/>
                <xsl:with-param name="value">
                  <xsl:call-template name="replace-substring">
                    <xsl:with-param name="from" select="concat('&amp;','apos;')"/>
                    <xsl:with-param name="to" select="'&amp;apos;'"/>
                    <xsl:with-param name="value">
                      <xsl:call-template name="replace-substring">
                        <xsl:with-param name="from" select="'&amp;'"/>
                        <xsl:with-param name="to" select="'&amp;amp;'"/>
                        <xsl:with-param name="value" select="$text"/>
                      </xsl:call-template>
                    </xsl:with-param>
                  </xsl:call-template>
                </xsl:with-param>
              </xsl:call-template>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:variable>
    <xsl:value-of select="$tmp"/>
  </xsl:template>
  
  <xsl:template name="replace-substring">
    <xsl:param name="value"/>
    <xsl:param name="from"/>
    <xsl:param name="to"/>
    <xsl:choose>
      <xsl:when test="contains($value,$from)">
        <xsl:value-of select="substring-before($value,$from)"/>
        <xsl:value-of select="$to"/>
        <xsl:call-template name="replace-substring">
          <xsl:with-param name="value" select="substring-after($value,$from)"/>
          <xsl:with-param name="from" select="$from"/>
          <xsl:with-param name="to" select="$to"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$value"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>
