<?xml version="1.0" ?>
<!--
  ~ Copyright (c) 2001-2007, TIBCO Software Inc.
  ~ Use, modification, and distribution subject to terms of license.
  -->

<!-- Converts a CIF-formatted serialization file (http://xsd.tns.tibco.com/gi/cif/2006)  to the standard serialization format (urn:tibco.com/v3.0) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:id="http://xsd.tns.tibco.com/gi/cif/2006/inlinedata"
                xmlns:jsx="urn:tibco.com/v3.0" xmlns:jsx1="http://xsd.tns.tibco.com/gi/cif/2006" exclude-result-prefixes="jsx jsx1">

<xsl:preserve-space elements="*"/>
<xsl:output method="xml" indent="yes" cdata-section-elements="jsx1:meta id:data"/>

  <!-- Entry Level Template (creates the outer serialization container in 3.0 syntax) -->
  <xsl:template match="/">
    <component xmlns="http://xsd.tns.tibco.com/gi/cif/2006" xmlns:e="http://xsd.tns.tibco.com/gi/cif/2006/events" xmlns:d="http://xsd.tns.tibco.com/gi/cif/2006/dynamics"
               xmlns:p="http://xsd.tns.tibco.com/gi/cif/2006/property" xmlns:pe="http://xsd.tns.tibco.com/gi/cif/2006/property.eval" xmlns:x="http://xsd.tns.tibco.com/gi/cif/2006/xslparameters"
               xmlns:v="http://xsd.tns.tibco.com/gi/cif/2006/view" xmlns:id="http://xsd.tns.tibco.com/gi/cif/2006/inlinedata" xmlns:u="http://xsd.tns.tibco.com/gi/cif/2006/userdefined"
               xmlns:ue="http://xsd.tns.tibco.com/gi/cif/2006/userdefined.eval" classpath="jsx3.gui">

      <xsl:choose><xsl:when test="/jsx:serialization/jsx:name and /jsx:serialization/jsx:name != ''">
      <meta name="name"><xsl:value-of select="/jsx:serialization/jsx:name"/></meta>
      </xsl:when></xsl:choose>
      <xsl:choose><xsl:when test="/jsx:serialization/jsx:icon and /jsx:serialization/jsx:icon != ''">
      <meta name="icon"><xsl:value-of select="/jsx:serialization/jsx:icon"/></meta>
      </xsl:when></xsl:choose>
      <xsl:choose><xsl:when test="/jsx:serialization/jsx:description and /jsx:serialization/jsx:description != ''">
      <meta name="description"><xsl:value-of select="/jsx:serialization/jsx:description"/></meta>
      </xsl:when></xsl:choose>
      <xsl:choose><xsl:when test="/jsx:serialization/jsx:onBeforeDeserialize and /jsx:serialization/jsx:onBeforeDeserialize != ''">
      <meta name="onBeforeDeserialize"><xsl:value-of select="/jsx:serialization/jsx:onBeforeDeserialize"/></meta>
      </xsl:when></xsl:choose>
      <xsl:choose><xsl:when test="/jsx:serialization/jsx:onAfterDeserialize and /jsx:serialization/jsx:onAfterDeserialize != ''">
      <meta name="onAfterDeserialize"><xsl:value-of select="/jsx:serialization/jsx:onAfterDeserialize"/></meta>
      </xsl:when></xsl:choose>

      <xsl:apply-templates select="/jsx:serialization/jsx:objects/jsx:object"/>

    </component>
  </xsl:template>



  <!-- Model Object Template -->
  <xsl:template match="jsx:object">

    <!-- GET THE CLASS NAME AND CLASS PATH (DERIVE FROM THE TYPE) -->
    <xsl:param name="mytype"><xsl:value-of select="@type"/></xsl:param>
    <xsl:param name="stype">mylongname</xsl:param>
    <xsl:param name="marker"><xsl:text>.</xsl:text></xsl:param>

    <xsl:param name="myclassname"><xsl:call-template name="lastIndexOf">
      <xsl:with-param name="string" select="$mytype" />
      <xsl:with-param name="char" select="$marker" />
    </xsl:call-template></xsl:param>

    <xsl:param name="myclasspath"><xsl:value-of select="substring-before($mytype, concat('.', $myclassname) )"/></xsl:param>

    <!-- create the named element -->
    <xsl:element name="{$myclassname}" namespace="http://xsd.tns.tibco.com/gi/cif/2006">

      <!-- choose statement here to add the classpath attribut if myclasspath != $classpath -->

      <!-- DYNAMICS -->
      <xsl:apply-templates select="jsx:dynamics/@*" mode="both">
        <xsl:with-param name="ns" select="'http://xsd.tns.tibco.com/gi/cif/2006/dynamics'" />
        <xsl:with-param name="prefix" select="'d:'" />
      </xsl:apply-templates>

      <!-- STRINGS -->
      <xsl:apply-templates select="jsx:strings/@*" mode="prefixed">
        <xsl:with-param name="ns" select="'http://xsd.tns.tibco.com/gi/cif/2006/property'" />
        <xsl:with-param name="userns" select="'http://xsd.tns.tibco.com/gi/cif/2006/userdefined'" />
        <xsl:with-param name="prefix" select="'p:'" />
        <xsl:with-param name="userprefix" select="'u:'" />
      </xsl:apply-templates>

      <!-- VARIANTS -->
      <xsl:apply-templates select="jsx:variants/@*" mode="prefixed">
        <xsl:with-param name="ns" select="'http://xsd.tns.tibco.com/gi/cif/2006/property.eval'" />
        <xsl:with-param name="userns" select="'http://xsd.tns.tibco.com/gi/cif/2006/userdefined.eval'" />
        <xsl:with-param name="prefix" select="'pe:'" />
        <xsl:with-param name="userprefix" select="'ue:'" />
      </xsl:apply-templates>

      <!-- MODEL EVENTS -->
      <xsl:apply-templates select="jsx:events/@*" mode="prefixed">
        <xsl:with-param name="ns" select="'http://xsd.tns.tibco.com/gi/cif/2006/events'" />
        <xsl:with-param name="prefix" select="'e:'" />
      </xsl:apply-templates>

      <!-- XSL PARAMETERS -->
      <xsl:apply-templates select="jsx:xslparameters/@*" mode="nonprefixed">
        <xsl:with-param name="ns" select="'http://xsd.tns.tibco.com/gi/cif/2006/xslparameters'" />
        <xsl:with-param name="prefix" select="'x:'" />
      </xsl:apply-templates>

      <!-- PROPERTIES (NATIVE VIEW) -->
      <xsl:apply-templates select="jsx:properties/@*" mode="nonprefixed">
        <xsl:with-param name="ns" select="'http://xsd.tns.tibco.com/gi/cif/2006/view'" />
        <xsl:with-param name="prefix" select="'v:'" />
      </xsl:apply-templates>

      <!-- STRINGS -->
      <xsl:apply-templates select="jsx:strings/@*" mode="prefixed_e">
        <xsl:with-param name="ns" select="'http://xsd.tns.tibco.com/gi/cif/2006/property'" />
        <xsl:with-param name="userns" select="'http://xsd.tns.tibco.com/gi/cif/2006/userdefined'" />
        <xsl:with-param name="prefix" select="'p:'" />
        <xsl:with-param name="userprefix" select="'u:'" />
      </xsl:apply-templates>

      <!-- CHILDREN (RECURSE) -->
      <xsl:apply-templates select="jsx:children/jsx:object"/>

    </xsl:element>

  </xsl:template>


    <!-- CONVERT ATTRIBUTE FORMAT-->
    <xsl:template match="@*" mode="prefixed_e">
      <xsl:param name="ns"/>
      <xsl:param name="userns"/>
      <xsl:param name="prefix"/>
      <xsl:param name="userprefix"/>
      <xsl:param name="marker"><xsl:choose><xsl:when test="starts-with(local-name(),'jsx')">jsx</xsl:when><xsl:otherwise></xsl:otherwise></xsl:choose></xsl:param>
      <xsl:param name="input"><xsl:value-of select="local-name()"/></xsl:param>
      <xsl:param name="myprefix"><xsl:choose><xsl:when test="starts-with(local-name(),'jsx')"><xsl:value-of select="$prefix"/></xsl:when><xsl:otherwise><xsl:value-of select="$userprefix"/></xsl:otherwise></xsl:choose></xsl:param>
      <xsl:param name="myns"><xsl:choose><xsl:when test="starts-with(local-name(),'jsx')"><xsl:value-of select="$ns"/></xsl:when><xsl:otherwise><xsl:value-of select="$userns"/></xsl:otherwise></xsl:choose></xsl:param>
      <xsl:param name="mykey" select="generate-id(.)" />
      <xsl:param name="shortname"><xsl:value-of select="substring-after($input,$marker)"/></xsl:param>
      <xsl:param name="lt" select="'&lt;'"/>

      <!-- add an inline data reference -->
      <xsl:choose><xsl:when test="$myprefix = 'p:' and ($shortname = 'xml' or $shortname='xsl' or ($shortname='text' and contains(.,$lt)) or ($shortname='value' and contains(.,$lt))) and not(.='')">

        <xsl:apply-templates select="." mode="inlinedata">
          <xsl:with-param name="ns" select="$myns" />
          <xsl:with-param name="prefix" select="$myprefix" />
          <xsl:with-param name="mykey" select="$shortname" />
        </xsl:apply-templates>
      </xsl:when>

      </xsl:choose>

  </xsl:template>


  <!-- CONVERT ATTRIBUTE FORMAT-->
  <xsl:template match="@*" mode="prefixed">
    <xsl:param name="ns"/>
    <xsl:param name="userns"/>
    <xsl:param name="prefix"/>
    <xsl:param name="userprefix"/>
    <xsl:param name="marker"><xsl:choose><xsl:when test="starts-with(local-name(),'jsx')">jsx</xsl:when><xsl:otherwise></xsl:otherwise></xsl:choose></xsl:param>
    <xsl:param name="input"><xsl:value-of select="local-name()"/></xsl:param>
    <xsl:param name="myprefix"><xsl:choose><xsl:when test="starts-with(local-name(),'jsx')"><xsl:value-of select="$prefix"/></xsl:when><xsl:otherwise><xsl:value-of select="$userprefix"/></xsl:otherwise></xsl:choose></xsl:param>
    <xsl:param name="myns"><xsl:choose><xsl:when test="starts-with(local-name(),'jsx')"><xsl:value-of select="$ns"/></xsl:when><xsl:otherwise><xsl:value-of select="$userns"/></xsl:otherwise></xsl:choose></xsl:param>
    <xsl:param name="mykey" select="generate-id(.)" />
    <xsl:param name="shortname"><xsl:value-of select="substring-after($input,$marker)"/></xsl:param>
    <xsl:param name="lt" select="'&lt;'"/>

    <!-- add an inline data reference -->
    <xsl:choose><xsl:when test="$myprefix = 'p:' and ($shortname = 'xml' or $shortname='xsl' or ($shortname='text' and contains(.,$lt)) or ($shortname='value' and contains(.,$lt))) and not(.='')">
      <xsl:attribute name="id:{$shortname}" namespace="http://xsd.tns.tibco.com/gi/cif/2006/inlinedata"><xsl:value-of select="$shortname"/></xsl:attribute>
    </xsl:when>

    <xsl:when test="$shortname != 'xml' and $shortname != 'xsl'">
      <xsl:attribute name="{$myprefix}{$shortname}" namespace="{$myns}"><xsl:value-of select="."/></xsl:attribute>
    </xsl:when>
    </xsl:choose>

  </xsl:template>


  <!-- CREATE INLINE DATA REFERENCE -->
  <xsl:template match="@*" mode="inlinedata">
    <xsl:param name="ns"/>
    <xsl:param name="prefix"/>
    <xsl:param name="mykey"/>

    <id:data xmlns:id="http://xsd.tns.tibco.com/gi/cif/2006/inlinedata" href="{$mykey}">
      <xsl:text>&#xA;&#xA;&#xA;</xsl:text><xsl:value-of select="string(.)" disable-output-escaping="no" /><xsl:text>&#xA;&#xA;&#xA;</xsl:text>
    </id:data>
  </xsl:template>


  <!-- CONVERT ATTRIBUTE FORMAT-->
  <xsl:template match="@*" mode="both">
    <xsl:param name="ns"/>
    <xsl:param name="prefix"/>
    <xsl:param name="marker"><xsl:choose><xsl:when test="starts-with(local-name(),'jsx')">jsx</xsl:when><xsl:otherwise></xsl:otherwise></xsl:choose></xsl:param>
    <xsl:param name="input"><xsl:value-of select="local-name()"/></xsl:param>

    <xsl:param name="shortname"><xsl:value-of select="substring-after($input,$marker)"/></xsl:param>
    <xsl:attribute name="{$prefix}{$shortname}" namespace="{$ns}"><xsl:value-of select="."/></xsl:attribute>
  </xsl:template>


  <!-- CONVERT ATTRIBUTE FORMAT-->
  <xsl:template match="@*" mode="nonprefixed">
    <xsl:param name="ns"/>
    <xsl:param name="prefix"/>
    <xsl:param name="input"><xsl:value-of select="local-name()"/></xsl:param>
    <xsl:attribute name="{$prefix}{$input}" namespace="{$ns}"><xsl:value-of select="."/></xsl:attribute>
  </xsl:template>


 <!-- "LAST-INDEX-OF" TEMPLATE -->
  <xsl:template name="lastIndexOf">
     <!-- declare that it takes two parameters  -->
     <xsl:param name="string" />
     <xsl:param name="char" />
     <xsl:choose>
        <!-- if the string contains the character... -->
        <xsl:when test="contains($string, $char)">
           <!-- call the template recursively... -->
           <xsl:call-template name="lastIndexOf">
              <!-- with the string being the string after the character -->
              <xsl:with-param name="string"
                              select="substring-after($string, $char)" />
              <!-- and the character being the same as before -->
              <xsl:with-param name="char" select="$char" />
           </xsl:call-template>
        </xsl:when>
        <!-- otherwise, return the value of the string -->
        <xsl:otherwise><xsl:value-of select="$string" />
      </xsl:otherwise>
     </xsl:choose>
  </xsl:template>


</xsl:stylesheet>
