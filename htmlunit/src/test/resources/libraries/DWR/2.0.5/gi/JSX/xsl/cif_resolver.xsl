<?xml version="1.0" ?>
<!--
  ~ Copyright (c) 2001-2007, TIBCO Software Inc.
  ~ Use, modification, and distribution subject to terms of license.
  -->

<!-- Converts a CIF-formatted serialization file (http://xsd.tns.tibco.com/gi/cif/2006)  to the standard serialization format (urn:tibco.com/v3.0) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:j="http://xsd.tns.tibco.com/gi/cif/2006" xmlns:e="http://xsd.tns.tibco.com/gi/cif/2006/events" xmlns:d="http://xsd.tns.tibco.com/gi/cif/2006/dynamics"
                xmlns:p="http://xsd.tns.tibco.com/gi/cif/2006/property" xmlns:pe="http://xsd.tns.tibco.com/gi/cif/2006/property.eval" xmlns:x="http://xsd.tns.tibco.com/gi/cif/2006/xslparameters"
                xmlns:v="http://xsd.tns.tibco.com/gi/cif/2006/view" xmlns:id="http://xsd.tns.tibco.com/gi/cif/2006/inlinedata" xmlns:u="http://xsd.tns.tibco.com/gi/cif/2006/userdefined"
                xmlns:ue="http://xsd.tns.tibco.com/gi/cif/2006/userdefined.eval" exclude-result-prefixes="j p pe v e d x id u ue">


  <!-- Entry Level Template (creates the outer serialization container -->
  <xsl:template match="/">
    <serialization xmlns="urn:tibco.com/v3.0">
      <unicode><xsl:value-of select="/j:component/j:meta[@name='unicode']"/></unicode>
      <name><xsl:value-of select="/j:component/j:meta[@name='name']"/></name>
      <icon><xsl:value-of select="/component/meta[@name='icon']"/></icon>
      <description><xsl:value-of select="/component/meta[@name='description']"/></description>
      <onBeforeDeserialize><xsl:value-of select="/component/meta[@name='onBeforeDeserialize']"/></onBeforeDeserialize>
      <onAfterDeserialize><xsl:value-of select="/j:component/j:meta[@name='onAfterDeserialize']"/></onAfterDeserialize>
      <objects>
        <xsl:apply-templates select="/j:component/child::*[local-name()!='meta']" mode="j"/>
      </objects>
    </serialization>
  </xsl:template>


  <!-- Model Object Template -->
  <xsl:template match="node()" mode="j">
    <xsl:param name="myc" select="."/>


    <!-- RESOLVE THE CLASSPATH FOR THE OBJECT (TO CREATE THE FULLYQUALIFIED CLASSNAME) -->
    <xsl:param name="ns">
      <xsl:choose>
        <xsl:when test="@classpath"><xsl:value-of select="@classpath"/></xsl:when>
        <xsl:otherwise><xsl:value-of select="/j:component/@classpath"/></xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <object xmlns="urn:tibco.com/v3.0">
      <xsl:attribute name="type"><xsl:value-of select="$ns"/>.<xsl:value-of select="name()"/></xsl:attribute>

      <!-- DYNAMICS -->
      <xsl:choose><xsl:when test="attribute::d:*">
        <dynamics><xsl:apply-templates select="attribute::d:*" mode="prefixed"/></dynamics>
      </xsl:when></xsl:choose>

      <!-- VARIANTS -->
      <xsl:choose><xsl:when test="attribute::pe:* or attribute::ue:*">
        <variants>
          <!-- jsx model properties (evaluated), user-defined properties (evalueated) -->
          <xsl:apply-templates select="attribute::pe:*" mode="prefixed"/>
          <xsl:apply-templates select="attribute::ue:*" mode="nonprefixed"/>
        </variants>
      </xsl:when></xsl:choose>

      <!-- STRINGS -->
      <xsl:choose><xsl:when test="attribute::p:* or attribute::u:* or attribute::id:*">
        <strings>
          <!-- jsx model properties, user-defined properties, inline data -->
          <xsl:apply-templates select="attribute::p:*" mode="prefixed"/>
          <xsl:apply-templates select="attribute::u:*" mode="nonprefixed"/>

          <!-- resolve inline data to an attribte -->
          <xsl:for-each select="attribute::id:*">
            <xsl:call-template name="lookup">
              <xsl:with-param name="theparent" select="$myc"/>
              <xsl:with-param name="theid" select="local-name()"/>
            </xsl:call-template>
          </xsl:for-each>

        </strings>
      </xsl:when></xsl:choose>

      <!-- MODEL EVENTS -->
      <xsl:choose><xsl:when test="attribute::e:*">
        <events><xsl:apply-templates select="attribute::e:*" mode="prefixed"/></events>
      </xsl:when></xsl:choose>

      <!-- XSL PARAMETERS (PARAMETERIZED STYLESHEETS) -->
      <xsl:choose><xsl:when test="attribute::x:*">
        <xslparameters>
          <xsl:apply-templates select="attribute::x:*" mode="nonprefixed"/>
        </xslparameters>
      </xsl:when></xsl:choose>

      <!-- PROPERTIES (NATIVE VIEW) -->
      <xsl:choose><xsl:when test="attribute::v:*">
        <properties><xsl:apply-templates select="attribute::v:*" mode="nonprefixed"/></properties>
      </xsl:when></xsl:choose>

      <!-- CHILDREN (RECURSE) -->
      <xsl:choose><xsl:when test="j:*">
        <xsl:apply-templates select="j:*" mode="j"/>
      </xsl:when></xsl:choose>

    </object>
  </xsl:template>

  <!-- CONVERT INLINE DATA -->
  <xsl:template name="lookup">
    <xsl:param name="theparent"/>
    <xsl:param name="theid"/>
    <xsl:attribute name="jsx{$theid}"><xsl:value-of select="$theparent/id:data[@href=$theid]"/></xsl:attribute>
  </xsl:template>

  <!-- CONVERT ATTRIBUTE FORMAT-->
  <xsl:template match="@*" mode="prefixed">
    <xsl:param name="expandedname">jsx<xsl:value-of select="local-name()"/></xsl:param>
    <xsl:attribute name="{$expandedname}"><xsl:value-of select="."/></xsl:attribute>
  </xsl:template>

  <!-- CONVERT ATTRIBUTE FORMAT-->
  <xsl:template match="@*" mode="nonprefixed">
    <xsl:param name="expandedname"><xsl:value-of select="local-name()"/></xsl:param>
    <xsl:attribute name="{$expandedname}"><xsl:value-of select="."/></xsl:attribute>
  </xsl:template>

</xsl:stylesheet>
