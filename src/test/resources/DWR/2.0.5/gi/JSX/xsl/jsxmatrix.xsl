<?xml version="1.0"?>
<!--
  ~ Copyright (c) 2001-2007, TIBCO Software Inc.
  ~ Use, modification, and distribution subject to terms of license.
  -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:output method="xml" omit-xml-declaration="yes"/>


  <!-- The ID for the Matrix instance to uniquely identify it among all GI GUI controls -->
  <xsl:param name="jsx_id"/>

  <!-- Users can use these named parameters to further parameterize their templates with custom input parameters -->
  <xsl:param name="jsx_1"/>
  <xsl:param name="jsx_2"/>
  <xsl:param name="jsx_3"/>
  <xsl:param name="jsx_4"/>
  <xsl:param name="jsx_5"/>
  <xsl:param name="jsx_6"/>
  <xsl:param name="jsx_7"/>
  <xsl:param name="jsx_8"/>
  <xsl:param name="jsx_9"/>
  <xsl:param name="jsx_10"/>
  <xsl:param name="jsxpath"></xsl:param>
  <xsl:param name="jsxpathapps"></xsl:param>
  <xsl:param name="jsxpathprefix"></xsl:param>


  <!--

    The developer can override this parameter using the XSL Parameters palette.  Passing 1, will tell the stylesheet to treat category records differently.
    Note that when this parameter is set to '1', two scenarios cause a given row to implement a fullrow colspan: either the record has one or more
    descendant records, or the given record implements the CDF attribute, jsxcategory="1".

  -->
  <xsl:param name="jsx_use_categories">0</xsl:param>


  <!-- total number of Columns that the matrix will render (_getDisplayedChildren). This is automatically updated by the system  -->
  <xsl:param name="jsx_column_count">1</xsl:param>


  <!-- default identifier that is bound to any native object as the jsxdragtype attribute to designate it as being compatible with CDF-based drag drop (legacy) -->
  <xsl:param name="jsx_drag_type">JSX_GENERIC</xsl:param>


  <!-- used when rendering a panel, the following are necessary to profile the panel in an manner that synchs it with js class -->
  <xsl:param name="jsx_panel_index"></xsl:param>
  <xsl:param name="jsx_column_widths"></xsl:param>
  <xsl:param name="jsx_panel_css"></xsl:param>


  <!-- controller icons used when in hierarchical mode -->
  <xsl:param name="jsx_icon">jsx:///images/tree/file.gif</xsl:param>
  <xsl:param name="jsx_icon_minus">jsx:///images/tree/minus.gif</xsl:param>
  <xsl:param name="jsx_icon_plus">jsx:///images/tree/plus.gif</xsl:param>
  <xsl:param name="jsx_transparent_image">jsx:///images/spc.gif</xsl:param>


  <!-- the paging model. one of: off (0), 2pass (1), chunked (2), paged (3), stepped (4) -->
  <xsl:param name="jsx_paging_model">0</xsl:param>


  <!-- cellvalue (#TEXT), row (TR), panel (TABLE), and count (int) -->
  <xsl:param name="jsx_mode">panel</xsl:param>
  <xsl:param name="jsx_cell_value_template_id">_jsx_{serverns}_{serial}_value</xsl:param>
  <xsl:param name="jsx_record_context">cdfkey</xsl:param>


  <!-- when the mode is "panel", a panel object will be returned (div/table) containing the rows matching inclusive of the following exclusive range -->
  <xsl:param name="jsx_min_exclusive">-1</xsl:param>
  <xsl:param name="jsx_max_exclusive">1000000</xsl:param>


  <!-- when mode is "cell", the id for the matrix column that owns the cell (this id can be used to resolve the cell template to call -->
  <xsl:param name="jsx_column_index">-1</xsl:param>


  <!-- the sorting parameters -->
  <xsl:param name="jsx_sort_path">jsxid</xsl:param>
  <xsl:param name="jsx_sort_direction">ascending</xsl:param>
  <xsl:param name="jsx_sort_type">text</xsl:param>


  <!-- when selection state needs to be designated, the CSS -->
  <xsl:param name="jsx_selection_bg_url">JSX/images/list/select.gif</xsl:param>

  <!-- the type of selection (jsxselectionmodel). defaults to single-row selection (1). multirow=2, cell=3, none=0 -->
  <xsl:param name="jsx_selection_model">1</xsl:param>

  <!-- when alternating row colors, use the following -->
  <xsl:param name="jsx_rowbg1"></xsl:param>
  <xsl:param name="jsx_rowbg2"></xsl:param>

  <!-- if rendering in hierarchical mode, and the following is specified as a valid bg color property, any record with child records will be styled with the following -->
  <xsl:param name="jsx_treehead_bgcolor"></xsl:param>
  <xsl:param name="jsx_treehead_fontweight"></xsl:param>

  <!-- style to apply to the TR if generating the autorow -->
  <xsl:param name="jsx_autorow_style">background-color:#fbf89f;</xsl:param>

  <!-- one of: shallow, deep, hierarchical -->
  <xsl:param name="jsx_rendering_model">hierarchical</xsl:param>


  <!-- The CDF record with a jsxid attribute matching this value is condidered the 'context node' -->
  <xsl:param name="jsx_rendering_context">jsxroot</xsl:param>
  <xsl:param name="jsx_rendering_context_child"></xsl:param>

  <!-- when rendering in hierarchical mode the amount of left indent for a child structure -->
  <xsl:param name="jsx_indent">16</xsl:param>

  <!-- tracks the ordinal index of an structures heirarchical level. used when rendering indents -->
  <xsl:param name="jsx_context_index">1</xsl:param>

  <!-- if 1, we don't use @jsxtip for the title attribute -->
  <xsl:param name="jsx_no_tip">0</xsl:param>

  <!-- if 1, then branches of all leaves will not be indented by the width of the plus/minus icon -->
  <xsl:param name="jsx_no_empty_indent">0</xsl:param>

  <!--
  ROOT TEMPLATE: Returns one of the following structures:
    1) A panel (TABLE) containing one or more records. (mode = panel)
    2) A TR element representing a specific row (mode = record)
    3) HTML/text to place inside an existing, on-screen td/div combination (mode = cellvalue)
    4) Count of all records that would be rendered on-screen if all records were to be painted. (mode = count)
    5) A TR element used as the auto-append row. This row has no corresponding record in the CDF, but renders none-the-less
  -->
  <xsl:template match="/">
    <JSX_FF_WELLFORMED_WRAPPER>
      <xsl:choose>
        <xsl:when test="$jsx_mode = 'cellvalue'">
          <!-- this is an update to the cell value (the actual content that will go inside the td/div) -->
          <xsl:choose><xsl:when test="0"></xsl:when></xsl:choose>
        </xsl:when>
        <xsl:when test="$jsx_rendering_model = 'shallow'">
          <!-- all records within a positioned range, immediate children of the node with jsxid equal to $jsx_rendering_context -->
          <xsl:choose>
            <xsl:when test="$jsx_mode = 'count'">
              <xsl:value-of select="count(//*[@jsxid=$jsx_rendering_context]/record)"/>
            </xsl:when>
            <xsl:when test="$jsx_mode = 'autorow'">
              <table id="{$jsx_id}jsx_-1" jsxautorow="true" cellspacing="0" cellpadding="0" class="jsx30matrix_rowtable" style="{$jsx_panel_css}width:{$jsx_column_widths}px;">
                <xsl:call-template name="row_template">
                  <xsl:with-param name="jsx_row_number">-1</xsl:with-param>
                </xsl:call-template>
              </table>
            </xsl:when>
            <xsl:when test="$jsx_mode = 'record'">
              <xsl:for-each select="//*[@jsxid=$jsx_rendering_context]/record">
                <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
                <xsl:if test="@jsxid = $jsx_rendering_context_child">
                  <xsl:apply-templates select="." mode="entry">
                    <xsl:with-param name="jsx_row_number" select="position()"/>
                  </xsl:apply-templates>
                </xsl:if>
              </xsl:for-each>
            </xsl:when>
            <xsl:when test="$jsx_mode = 'sort'">
              <ids>
                <xsl:apply-templates select="//*[@jsxid=$jsx_rendering_context]/record" mode="sort">
                  <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
                </xsl:apply-templates>
              </ids>
            </xsl:when>
            <xsl:otherwise>
              <table id="{$jsx_id}jsx_{$jsx_panel_index}" cellspacing="0" cellpadding="0" class="jsx30matrix_rowtable" style="{$jsx_panel_css}width:{$jsx_column_widths}px;">
                <xsl:for-each select="//*[@jsxid=$jsx_rendering_context]/record">
                  <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
                  <xsl:apply-templates select="." mode="entry">
                    <xsl:with-param name="jsx_row_number" select="position()"/>
                  </xsl:apply-templates>
                </xsl:for-each>
              </table>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:when test="$jsx_rendering_model = 'deep'">
          <!-- all records within a positioned range, descendants of the node with jsxid equal to $jsx_rendering_context -->
          <xsl:choose>
            <xsl:when test="$jsx_mode = 'count'">
              <xsl:value-of select="count(//*[@jsxid=$jsx_rendering_context]//record)"/>
            </xsl:when>
            <xsl:when test="$jsx_mode = 'autorow'">
              <table id="{$jsx_id}jsx_-1" jsxautorow="true" cellspacing="0" cellpadding="0" class="jsx30matrix_rowtable" style="{$jsx_panel_css}width:{$jsx_column_widths}px;">
                <xsl:call-template name="row_template">
                  <xsl:with-param name="jsx_row_number">-1</xsl:with-param>
                </xsl:call-template>
              </table>
            </xsl:when>
            <xsl:when test="$jsx_mode = 'record'">
              <xsl:for-each select="//*[@jsxid=$jsx_rendering_context]//record">
                <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
                <xsl:if test="@jsxid = $jsx_rendering_context_child">
                  <xsl:apply-templates select="." mode="entry">
                    <xsl:with-param name="jsx_row_number" select="position()"/>
                  </xsl:apply-templates>
                </xsl:if>
              </xsl:for-each>
            </xsl:when>
            <xsl:when test="$jsx_mode = 'sort'">
              <ids>
                <xsl:apply-templates select="//*[@jsxid=$jsx_rendering_context]//record" mode="sort">
                  <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
                </xsl:apply-templates>
              </ids>
            </xsl:when>
            <xsl:otherwise>
              <table id="{$jsx_id}jsx_{$jsx_panel_index}" cellspacing="0" cellpadding="0" class="jsx30matrix_rowtable" style="{$jsx_panel_css}width:{$jsx_column_widths}px;">
                <xsl:for-each select="//*[@jsxid=$jsx_rendering_context]//record">
                  <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
                  <xsl:apply-templates select="." mode="entry">
                    <xsl:with-param name="jsx_row_number" select="position()"/>
                  </xsl:apply-templates>
                </xsl:for-each>
              </table>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:when test="$jsx_rendering_model = 'hierarchical'">
          <!-- all records within a positioned range, descendants of the node with jsxid equal to $jsx_rendering_context, diplayed hierarchically -->
          <xsl:choose>
            <xsl:when test="$jsx_mode = 'count'">
              <xsl:value-of select="count(//*[@jsxid=$jsx_rendering_context]//record)"/>
            </xsl:when>
            <xsl:when test="$jsx_mode = 'record'">
              <!-- return the structure for a SINGLE child record -->
              <xsl:for-each select="//*[@jsxid=$jsx_rendering_context]/record[@jsxid=$jsx_rendering_context_child]">
                <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
                <xsl:apply-templates select="." mode="hierarchical_entry">
                  <xsl:with-param name="jsx_row_number" select="position()"/>
                  <xsl:with-param name="jsx_adjusted_width" select="$jsx_column_widths"/>
                  <xsl:with-param name="jsx_descendant_index" select="$jsx_context_index"/>
                </xsl:apply-templates>
              </xsl:for-each>
            </xsl:when>
            <xsl:when test="$jsx_mode = 'sort'">
              <ids>
                <xsl:apply-templates select="//*[@jsxid=$jsx_rendering_context]/record" mode="hierarchical_sort">
                  <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
                </xsl:apply-templates>
              </ids>
            </xsl:when>
            <xsl:otherwise>
              <!-- return the structure for a ALL child records -->
              <xsl:for-each select="//*[@jsxid=$jsx_rendering_context]/record">
                <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
                <xsl:apply-templates select="." mode="hierarchical_entry">
                  <xsl:with-param name="jsx_row_number" select="position()"/>
                  <xsl:with-param name="jsx_adjusted_width" select="$jsx_column_widths"/>
                  <xsl:with-param name="jsx_descendant_index" select="$jsx_context_index"/>
                </xsl:apply-templates>
              </xsl:for-each>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
      </xsl:choose>
    </JSX_FF_WELLFORMED_WRAPPER>
  </xsl:template>




  <!--
  FLAT TEMPLATE:  Called by ROOT template. Calls the ROW template. Returns a TR element to
                  be placed within a Panel. Contains logic to return a range of rows (as opposed to all
                  rows) based upon the paging model and the specified rows per panel. Called when employing
                  shallow or deep rendering models
  -->
  <xsl:template match="node()" mode="entry">
    <xsl:param name="jsx_row_number"/>
    <!-- this ensures the paging range.  need to optimize while keeping ordinal position (perhaps use apply template and sum the position and the start index. TO DO TO DO!! -->
    <xsl:choose><xsl:when test="$jsx_row_number = '-1' or ($jsx_row_number &gt; $jsx_min_exclusive and $jsx_row_number &lt; $jsx_max_exclusive)">
      <xsl:call-template name="row_template">
        <xsl:with-param name="jsx_row_number" select="$jsx_row_number"/>
      </xsl:call-template>
    </xsl:when></xsl:choose>
  </xsl:template>


  <!--
  SORT TEMPLATEs:  Called by ROOT template. returns all record ids in the order they will appear on-screen
  -->
  <xsl:template match="node()" mode="sort">
    "<xsl:value-of select="@jsxid" />",
  </xsl:template>


  <xsl:template match="node()" mode="hierarchical_sort">
    "<xsl:value-of select="@jsxid" />",
    <xsl:apply-templates select="record" mode="hierarchical_sort">
      <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
    </xsl:apply-templates>
  </xsl:template>



  <!--
    HIERARCHY TEMPLATE: Called by ROOT template. Calls the ROW template. Called when employing
                        a hierarchical rendering model. Creates a "div[table | div]" structure.
                        Where the child table is the record and the div is the container holding
                        its child structures. Recurses if child content exists
  -->
  <xsl:template match="node()" mode="hierarchical_entry">
    <!-- input parameters -->
    <xsl:param name="jsx_row_number"/>
    <xsl:param name="jsx_adjusted_width"/>
    <xsl:param name="jsx_descendant_index"/>

    <div jsxtype="structure" style="position:relative;" unselectable="on">
      <!-- note that I hard-code the id of the table with a '0'. when in hierarchical mode, the table id is ignored. it is the row id that matters -->
      <table id="{$jsx_id}jsx_0" cellspacing="0" cellpadding="0" class="jsx30matrix_rowtable" style="{$jsx_panel_css}width:{$jsx_adjusted_width}px;">
        <xsl:call-template name="row_template">
          <xsl:with-param name="jsx_row_number" select="$jsx_min_exclusive + 1"/>
          <xsl:with-param name="jsx_descendant_index" select="$jsx_descendant_index"/>
        </xsl:call-template>
      </table>
      <div style="display:none;" jsxcontextindex="{$jsx_descendant_index + 1}" unselectable="on">
        <!-- recurse -->
        <xsl:choose>
          <xsl:when test="record">

            <xsl:choose>
              <xsl:when test="@jsxopen='1'">
                <xsl:attribute name="style">position:relative;display:block;</xsl:attribute>
              </xsl:when>
            </xsl:choose>

            <xsl:choose>
              <xsl:when test="@jsxopen='1' or $jsx_paging_model != 4 ">
                <xsl:for-each select="record">
                  <xsl:sort select="@*[name()=$jsx_sort_path]" data-type="{$jsx_sort_type}" order="{$jsx_sort_direction}"/>
                  <xsl:apply-templates select="." mode="hierarchical_entry">
                    <xsl:with-param name="jsx_row_number" select="position()"/>
                    <xsl:with-param name="jsx_adjusted_width" select="$jsx_adjusted_width"/>
                    <xsl:with-param name="jsx_descendant_index" select="$jsx_descendant_index + 1"/>
                  </xsl:apply-templates>
                </xsl:for-each>
              </xsl:when><xsl:otherwise><xsl:text>&#160;</xsl:text></xsl:otherwise>
            </xsl:choose>

          </xsl:when><xsl:otherwise><xsl:text>&#160;</xsl:text></xsl:otherwise>
        </xsl:choose>
      </div>
    </div>
  </xsl:template>



  <!--
  HIERARCHY CONTROLLER TEMPLATE: Called by the CELL template. Calls the CELL VALUE template.
                                 Adds togglers and icons to the cell template before calling
                                 out to the CELL VALUE template for the actual content
                                 to place in the cell. Creates a "table/tr/[td | td | td]" structure
                                 that is used to hold the toggle icon, visual icon, and content.
  -->
  <xsl:template name="ui_controller">
    <xsl:param name="jsx_descendant_index"/>
    <xsl:param name="jsx_cell_width"/>
    <xsl:param name="jsx_row_number">0</xsl:param>
    <xsl:param name="jsx_style" select="@jsxstyle"/>

    <table cellpadding="0" cellspacing="0" class="jsx30matrix_rowtable">
      <xsl:attribute name="style">position:relative;left:<xsl:value-of select="($jsx_descendant_index -1) * $jsx_indent"/>px;width:<xsl:value-of select="$jsx_cell_width - (($jsx_descendant_index -1) * $jsx_indent)"/>px;height:16px;</xsl:attribute>
      <tr style="{$jsx_style}">
        <!-- this is the toggle image (plus/minus) -->
        <td jsxtype="plusminus">
          <xsl:attribute name="jsxtype"><xsl:choose>
              <xsl:when test="record and $jsx_paging_model = 4 and not(@jsxopen=1)">paged</xsl:when>
              <xsl:otherwise>plusminus</xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>
          <xsl:attribute name="style">vertical-align:top;width:<xsl:choose>
              <xsl:when test="$jsx_no_empty_indent='1' and not(../record/record)">0</xsl:when>
              <xsl:otherwise>16</xsl:otherwise>
            </xsl:choose>px;background-image:url(<xsl:choose>
              <xsl:when test="record and @jsxopen=1">
                <xsl:apply-templates select="." mode="uri-resolver">
                  <xsl:with-param name="uri" select="$jsx_icon_minus"/>
                </xsl:apply-templates>
              </xsl:when>
              <xsl:when test="record and $jsx_paging_model = 4">
                <xsl:apply-templates select="." mode="uri-resolver">
                  <xsl:with-param name="uri" select="$jsx_icon_plus"/>
                </xsl:apply-templates>
              </xsl:when>
              <xsl:when test="record">
                <xsl:apply-templates select="." mode="uri-resolver">
                  <xsl:with-param name="uri" select="$jsx_icon_plus"/>
                </xsl:apply-templates>
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="." mode="uri-resolver">
                  <xsl:with-param name="uri" select="$jsx_transparent_image"/>
                </xsl:apply-templates>
              </xsl:otherwise>
            </xsl:choose>);background-repeat:no-repeat;</xsl:attribute>
          <xsl:text>&#160;</xsl:text>
        </td>
        <!-- this is the icon for the row -->
        <td jsxtype="icon" unselectable="on">
          <xsl:choose>
            <xsl:when test="@jsximg = ''">
              <xsl:attribute name="style">width:1px;</xsl:attribute>
              <span style="display:none;width:1px;height:1px;"></span>
            </xsl:when>
            <xsl:when test="@jsximg">
              <xsl:attribute name="style">width:16px;vertical-align:top;</xsl:attribute>
              <xsl:variable name="src1">
                <xsl:apply-templates select="@jsximg" mode="uri-resolver"/>
              </xsl:variable>
              <img jsxtype="icon" unselectable="on" class="jsx30matrix_plusminus" src="{$src1}"/>
            </xsl:when>
            <xsl:when test="$jsx_icon=''">
              <xsl:attribute name="style">width:1px;</xsl:attribute>
              <span style="display:none;width:1px;height:1px;"><xsl:text>&#160;</xsl:text></span>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="style">width:16px;vertical-align:top;</xsl:attribute>
              <xsl:variable name="src1">
                <xsl:apply-templates select="." mode="uri-resolver">
                  <xsl:with-param name="uri" select="$jsx_icon"/>
                </xsl:apply-templates>
              </xsl:variable>
              <img jsxtype="icon" unselectable="on" class="jsx30matrix_plusminus" src="{$src1}"/>
            </xsl:otherwise>
          </xsl:choose>
          <xsl:text>&#160;</xsl:text>
        </td>
        <td jsxtype="text" style="vertical-align:top;" unselectable="on">
          <xsl:attribute name="jsxtreenode">
            <xsl:choose>
              <xsl:when test="record">branch</xsl:when>
              <xsl:otherwise>leaf</xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>

          <!-- this is the exact same callout as the one that is created via JavaScript, since this template is an itermediary wrapper -->
          <!-- note that this td will have no control over the content height,but that the td that contains the controller set does -->
          <xsl:call-template name="ui_controller">
            <xsl:with-param name="jsx_cell_width" select="$jsx_cell_width"/>
            <xsl:with-param name="jsx_row_number" select="$jsx_row_number"/>
            <xsl:with-param name="jsx_descendant_index" select="$jsx_descendant_index"/>
          </xsl:call-template>
        </td>
      </tr>
    </table>
  </xsl:template>



  <!--
  ROW TEMPLATE: Called by the FLAT and HIERARCHY templates. Creates the TR. Note that this
                template will be dynamically extended at runtime to embed callouts for the
                individual columns. Calls the CELL template
  -->
  <xsl:template name="row_template">

    <!-- descendant level in the overall hierarchy when painted on-screen -->
    <xsl:param name="jsx_descendant_index"/>

    <!-- this is actually 1-based -->
    <xsl:param name="jsx_row_number">0</xsl:param>

    <xsl:param name="jsxdragtype" select="$jsx_drag_type"/>

    <!-- users can add a custom style declaration to the TR (unfortunately, in my opinion) -->
    <xsl:param name="jsx_style">
      <xsl:choose>
        <xsl:when test="$jsx_row_number = -1"><xsl:value-of select="$jsx_autorow_style"/></xsl:when>
        <xsl:otherwise><xsl:value-of select="@jsxstyle"/></xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <!-- jsxid for the cdf record corresponding to the TR about to be created. Note that if an autoappend row is used, jsxautorow is the assumed id -->
    <xsl:param name="jsx_cdfkey">
      <xsl:choose>
        <xsl:when test="$jsx_row_number = -1">jsxautorow</xsl:when>
        <xsl:otherwise><xsl:value-of select="@jsxid"/></xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <!-- support radio groups -->
    <xsl:param name="jsx_groupname">
      <xsl:choose>
        <xsl:when test="@jsxgroupname"><xsl:value-of select="@jsxgroupname"/></xsl:when>
        <xsl:otherwise><xsl:value-of select="$jsx_id"/></xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <!-- this toggles/alternates the row colors. It should probably be enforced on any forced-height template that is not hierarchical to stop IE bleedthrough -->
    <xsl:param name="jsx_rowbg">
      <xsl:choose>
        <xsl:when test="$jsx_rendering_model != 'hierarchical' and ($jsx_rowbg1 or $jsx_rowbg2) and $jsx_row_number != -1">
          <xsl:text>background-color:</xsl:text>
          <xsl:choose>
            <xsl:when test="$jsx_row_number mod 2 = 0"><xsl:value-of select="$jsx_rowbg1"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="$jsx_rowbg2"/></xsl:otherwise>
          </xsl:choose><xsl:text>;</xsl:text>
        </xsl:when>
        <xsl:when test="record and ($jsx_rendering_model = 'hierarchical') and $jsx_treehead_bgcolor">
          <xsl:text>background-color:</xsl:text><xsl:value-of select="$jsx_treehead_bgcolor"/>
          <xsl:text>;font-weight:</xsl:text><xsl:value-of select="$jsx_treehead_fontweight"/>
          <xsl:text>;border-right-color:</xsl:text><xsl:value-of select="$jsx_treehead_bgcolor"/><xsl:text>;</xsl:text>
        </xsl:when>
      </xsl:choose>
    </xsl:param>

    <!-- boolean that says that this row is the first row in the panel (only cells in the first row declare a width) -->
    <xsl:param name="jsx_is_first_panel_row" select="$jsx_row_number - 1 = $jsx_min_exclusive or $jsx_row_number = -1"/>

    <!-- the TR element -->
    <tr id="{$jsx_id}_jsx_{$jsx_cdfkey}" JSXDragId="{$jsx_cdfkey}" JSXDragType="{$jsx_drag_type}" jsxtype="record" jsxid="{$jsx_cdfkey}" jsxrownumber="{$jsx_row_number}" style="{$jsx_rowbg}{$jsx_style}">
      <xsl:if test="@jsxtip and $jsx_no_tip != '1'">
        <xsl:attribute name="title"><xsl:value-of select="@jsxtip"/></xsl:attribute>
      </xsl:if>
      <xsl:choose>
        <xsl:when test="$jsx_use_categories='0' or @jsxcategory='0' or (not(@jsxcategory='1') and not(record))">
        </xsl:when>
      </xsl:choose>
    </tr>
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

  <!--
  CELL TEMPLATE(S): These templates are dynamically generated by the Matrix class. They return a
                    td/div combination. These templates cannot be modified by the user and are
                    rigidly connected to the underlying system box model.
  -->



  <!--
  CELL VALUE TEMPLATE(S): These templates are dynamically generated by the Matrix class or can be
                          created by the developer and bound to a given column. These templates return the
                          text content that will go inside the associated CELL template. The developer
                          can create this template in order to have more fine-grain control over
                          the format and content of the cell.
  -->



</xsl:stylesheet>


