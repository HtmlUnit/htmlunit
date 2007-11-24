
 /****************************************************************
  *                                                              *
  *  curvyCorners                                                *
  *  ------------                                                *
  *                                                              *
  *  This script generates rounded corners for your divs.        *
  *                                                              *
  *  Version 1.2.9                                               *
  *  Copyright (c) 2006 Cameron Cooke                            *
  *  By: Cameron Cooke and Tim Hutchison.                        *
  *                                                              *
  *                                                              *
  *  Website: http://www.curvycorners.net                        *
  *  Email:   info@totalinfinity.com                             *
  *  Forum:   http://www.curvycorners.net/forum/                 *
  *                                                              *
  *                                                              *
  *  This library is free software; you can redistribute         *
  *  it and/or modify it under the terms of the GNU              *
  *  Lesser General Public License as published by the           *
  *  Free Software Foundation; either version 2.1 of the         *
  *  License, or (at your option) any later version.             *
  *                                                              *
  *  This library is distributed in the hope that it will        *
  *  be useful, but WITHOUT ANY WARRANTY; without even the       *
  *  implied warranty of MERCHANTABILITY or FITNESS FOR A        *
  *  PARTICULAR PURPOSE. See the GNU Lesser General Public       *
  *  License for more details.                                   *
  *                                                              *
  *  You should have received a copy of the GNU Lesser           *
  *  General Public License along with this library;             *
  *  Inc., 59 Temple Place, Suite 330, Boston,                   *
  *  MA 02111-1307 USA                                           *
  *                                                              *
  ****************************************************************/

  // Browser detection
  var isIE     = navigator.userAgent.toLowerCase().indexOf("msie") > -1;
  var isMoz    = document.implementation && document.implementation.createDocument;
  var isSafari = ((navigator.userAgent.toLowerCase().indexOf('safari')!=-1)&&(navigator.userAgent.toLowerCase().indexOf('mac')!=-1))?true:false;

  /*
  Usage:

  newCornersObj = new curvyCorners(settingsObj, "classNameStr");
  newCornersObj = new curvyCorners(settingsObj, divObj1[, divObj2[, divObj3[, . . . [, divObjN]]]]);
  */
  function curvyCorners()
  {
      // Check parameters
      if(typeof(arguments[0]) != "object") throw newCurvyError("First parameter of curvyCorners() must be an object.");
      if(typeof(arguments[1]) != "object" && typeof(arguments[1]) != "string") throw newCurvyError("Second parameter of curvyCorners() must be an object or a class name.");

      // Get object(s)
      if(typeof(arguments[1]) == "string")
      {
          // Get elements by class name
          var startIndex = 0;
          var boxCol = getElementsByClass(arguments[1]);
      }
      else
      {
          // Get objects
          var startIndex = 1;
          var boxCol = arguments;
      }

      // Create return collection/object
      var curvyCornersCol = new Array();

      // Create array of html elements that can have rounded corners
      if(arguments[0].validTags)
        var validElements = arguments[0].validTags;
      else
        var validElements = ["div"]; // Default

      // Loop through each argument
      for(var i = startIndex, j = boxCol.length; i < j; i++)
      {
          // Current element tag name
          var currentTag = boxCol[i].tagName.toLowerCase();

          if(inArray(validElements, currentTag) !== false)
          {
              curvyCornersCol[curvyCornersCol.length] = new curvyObject(arguments[0], boxCol[i]);
          }
      }

      this.objects = curvyCornersCol;

      // Applys the curvyCorners to all objects
      this.applyCornersToAll = function()
      {
          for(var x = 0, k = this.objects.length; x < k; x++)
          {
              this.objects[x].applyCorners();
          }
      }
  }

  // curvyCorners object (can be called directly)
  function curvyObject()
  {
      // Setup Globals
      this.box              = arguments[1];
      this.settings         = arguments[0];
      this.topContainer     = null;
      this.bottomContainer  = null;
      this.masterCorners    = new Array();
      this.contentDIV       = null;

      // Get box formatting details
      var boxHeight       = get_style(this.box, "height", "height");
      var boxWidth        = get_style(this.box, "width", "width");
      var borderWidth     = get_style(this.box, "borderTopWidth", "border-top-width");
      var borderColour    = get_style(this.box, "borderTopColor", "border-top-color");
      var boxColour       = get_style(this.box, "backgroundColor", "background-color");
      var backgroundImage = get_style(this.box, "backgroundImage", "background-image");
      var boxPosition     = get_style(this.box, "position", "position");
      var boxPadding      = get_style(this.box, "paddingTop", "padding-top");

      // Set formatting propertes
      this.boxHeight       = parseInt(((boxHeight != "" && boxHeight != "auto" && boxHeight.indexOf("%") == -1)? boxHeight.substring(0, boxHeight.indexOf("px")) : this.box.scrollHeight));
      this.boxWidth        = parseInt(((boxWidth != "" && boxWidth != "auto" && boxWidth.indexOf("%") == -1)? boxWidth.substring(0, boxWidth.indexOf("px")) : this.box.scrollWidth));
      this.borderWidth     = parseInt(((borderWidth != "" && borderWidth.indexOf("px") !== -1)? borderWidth.slice(0, borderWidth.indexOf("px")) : 0));
      this.boxColour       = format_colour(boxColour);
      this.boxPadding      = parseInt(((boxPadding != "" && boxPadding.indexOf("px") !== -1)? boxPadding.slice(0, boxPadding.indexOf("px")) : 0));
      this.borderColour    = format_colour(borderColour);
      this.borderString    = this.borderWidth + "px" + " solid " + this.borderColour;
      this.backgroundImage = ((backgroundImage != "none")? backgroundImage : "");
      this.boxContent      = this.box.innerHTML;

      // Make box relative if not already absolute and remove any padding
      if(boxPosition != "absolute") this.box.style.position = "relative";
      this.box.style.padding = "0px";

      // If IE and height and width are not set, we need to set width so that we get positioning
      if(isIE && boxWidth == "auto" && boxHeight == "auto") this.box.style.width = "100%";

      // Resize box so that it stays to the orignal height


      // Remove content if box is using autoPad
      if(this.settings.autoPad == true && this.boxPadding > 0)
        this.box.innerHTML = "";

      /*
      This method creates the corners and
      applies them to the div element.
      */
      this.applyCorners = function()
      {
          /*
          Create top and bottom containers.
          These will be used as a parent for the corners and bars.
          */
          for(var t = 0; t < 2; t++)
          {
              switch(t)
              {
                  // Top
                  case 0:

                      // Only build top bar if a top corner is to be draw
                      if(this.settings.tl || this.settings.tr)
                      {
                          var newMainContainer = document.createElement("DIV");
                          newMainContainer.style.width    = "100%";
                          newMainContainer.style.fontSize = "1px";
                          newMainContainer.style.overflow = "hidden";
                          newMainContainer.style.position = "absolute";
                          newMainContainer.style.paddingLeft  = this.borderWidth + "px";
                          newMainContainer.style.paddingRight = this.borderWidth + "px";
                          var topMaxRadius = Math.max(this.settings.tl ? this.settings.tl.radius : 0, this.settings.tr ? this.settings.tr.radius : 0);
                          newMainContainer.style.height = topMaxRadius + "px";
                          newMainContainer.style.top    = 0 - topMaxRadius + "px";
                          newMainContainer.style.left   = 0 - this.borderWidth + "px";
                          this.topContainer = this.box.appendChild(newMainContainer);
                      }
                      break;

                  // Bottom
                  case 1:

                      // Only build bottom bar if a top corner is to be draw
                      if(this.settings.bl || this.settings.br)
                      {
                          var newMainContainer = document.createElement("DIV");
                          newMainContainer.style.width    = "100%";
                          newMainContainer.style.fontSize = "1px";
                          newMainContainer.style.overflow = "hidden";
                          newMainContainer.style.position = "absolute";
                          newMainContainer.style.paddingLeft  = this.borderWidth + "px";
                          newMainContainer.style.paddingRight = this.borderWidth + "px";
                          var botMaxRadius = Math.max(this.settings.bl ? this.settings.bl.radius : 0, this.settings.br ? this.settings.br.radius : 0);
                          newMainContainer.style.height  = botMaxRadius + "px";
                          newMainContainer.style.bottom  =  0 - botMaxRadius + "px";
                          newMainContainer.style.left    =  0 - this.borderWidth + "px";
                          this.bottomContainer = this.box.appendChild(newMainContainer);
                      }
                      break;
              }
          }

          // Turn off current borders
          if(this.topContainer) this.box.style.borderTopWidth = "0px";
          if(this.bottomContainer) this.box.style.borderBottomWidth = "0px";

          // Create array of available corners
          var corners = ["tr", "tl", "br", "bl"];

          /*
          Loop for each corner
          */
          for(var i in corners)
          {
              // FIX for prototype lib
              if(i > -1 < 4)
              {
                  // Get current corner type from array
                  var cc = corners[i];

                  // Has the user requested the currentCorner be round?
                  if(!this.settings[cc])
                  {
                      // No
                      if(((cc == "tr" || cc == "tl") && this.topContainer != null) || ((cc == "br" || cc == "bl") && this.bottomContainer != null))
                      {
                          // We need to create a filler div to fill the space upto the next horzontal corner.
                          var newCorner = document.createElement("DIV");

                          // Setup corners properties
                          newCorner.style.position = "relative";
                          newCorner.style.fontSize = "1px";
                          newCorner.style.overflow = "hidden";

                          // Add background image?
                          if(this.backgroundImage == "")
                            newCorner.style.backgroundColor = this.boxColour;
                          else
                            newCorner.style.backgroundImage = this.backgroundImage;

                          switch(cc)
                          {
                              case "tl":
                                  newCorner.style.height      = topMaxRadius - this.borderWidth + "px";
                                  newCorner.style.marginRight = this.settings.tr.radius - (this.borderWidth*2) + "px";
                                  newCorner.style.borderLeft  = this.borderString;
                                  newCorner.style.borderTop   = this.borderString;
                                  newCorner.style.left        = -this.borderWidth + "px";
                                  break;

                              case "tr":
                                  newCorner.style.height      = topMaxRadius - this.borderWidth + "px";
                                  newCorner.style.marginLeft  = this.settings.tl.radius - (this.borderWidth*2) + "px";
                                  newCorner.style.borderRight = this.borderString;
                                  newCorner.style.borderTop   = this.borderString;
                                  newCorner.style.backgroundPosition  = "-" + (topMaxRadius + this.borderWidth) + "px 0px";
                                  newCorner.style.left        = this.borderWidth + "px";
                                  break;

                              case "bl":
                                  newCorner.style.height       = botMaxRadius - this.borderWidth + "px";
                                  newCorner.style.marginRight  = this.settings.br.radius - (this.borderWidth*2) + "px";
                                  newCorner.style.borderLeft   = this.borderString;
                                  newCorner.style.borderBottom = this.borderString;
                                  newCorner.style.left         = -this.borderWidth + "px";
                                  newCorner.style.backgroundPosition = "-" + (this.borderWidth) + "px -" + (this.boxHeight + (botMaxRadius + this.borderWidth)) + "px";
                                  break;

                              case "br":
                                  newCorner.style.height       = botMaxRadius - this.borderWidth + "px";
                                  newCorner.style.marginLeft   = this.settings.bl.radius - (this.borderWidth*2) + "px";
                                  newCorner.style.borderRight  = this.borderString;
                                  newCorner.style.borderBottom = this.borderString;
                                  newCorner.style.left         = this.borderWidth + "px"
                                  newCorner.style.backgroundPosition = "-" + (botMaxRadius + this.borderWidth) + "px -" + (this.boxHeight + (botMaxRadius + this.borderWidth)) + "px";
                                  break;
                          }
                      }
                  }
                  else
                  {
                      /*
                      PERFORMANCE NOTE:

                      If more than one corner is requested and a corner has been already
                      created for the same radius then that corner will be used as a master and cloned.
                      The pixel bars will then be repositioned to form the new corner type.
                      All new corners start as a bottom right corner.
                      */
                      if(this.masterCorners[this.settings[cc].radius])
                      {
                          // Create clone of the master corner
                          var newCorner = this.masterCorners[this.settings[cc].radius].cloneNode(true);
                      }
                      else
                      {
                          // Yes, we need to create a new corner
                          var newCorner = document.createElement("DIV");
                          newCorner.style.height = this.settings[cc].radius + "px";
                          newCorner.style.width  = this.settings[cc].radius + "px";
                          newCorner.style.position = "absolute";
                          newCorner.style.fontSize = "1px";
                          newCorner.style.overflow = "hidden";

                          // THE FOLLOWING BLOCK OF CODE CREATES A ROUNDED CORNER
                          // ---------------------------------------------------- TOP

                          // Get border radius
                          var borderRadius = parseInt(this.settings[cc].radius - this.borderWidth);

                          // Cycle the x-axis
                          for(var intx = 0, j = this.settings[cc].radius; intx < j; intx++)
                          {
                              // Calculate the value of y1 which identifies the pixels inside the border
                              if((intx +1) >= borderRadius)
                                var y1 = -1;
                              else
                                var y1 = (Math.floor(Math.sqrt(Math.pow(borderRadius, 2) - Math.pow((intx+1), 2))) - 1);

                              // Only calculate y2 and y3 if there is a border defined
                              if(borderRadius != j)
                              {
                                  if((intx) >= borderRadius)
                                    var y2 = -1;
                                  else
                                    var y2 = Math.ceil(Math.sqrt(Math.pow(borderRadius,2) - Math.pow(intx, 2)));

                                  if((intx+1) >= j)
                                    var y3 = -1;
                                  else
                                    var y3 = (Math.floor(Math.sqrt(Math.pow(j ,2) - Math.pow((intx+1), 2))) - 1);
                              }

                              // Calculate y4
                              if((intx) >= j)
                                var y4 = -1;
                              else
                                var y4 = Math.ceil(Math.sqrt(Math.pow(j ,2) - Math.pow(intx, 2)));

                              // Draw bar on inside of the border with foreground colour
                              if(y1 > -1) this.drawPixel(intx, 0, this.boxColour, 100, (y1+1), newCorner, -1, this.settings[cc].radius);

                              // Only draw border/foreground antialiased pixels and border if there is a border defined
                              if(borderRadius != j)
                              {
                                  // Cycle the y-axis
                                  for(var inty = (y1 + 1); inty < y2; inty++)
                                  {
                                      // Draw anti-alias pixels
                                      if(this.settings.antiAlias)
                                      {
                                          // For each of the pixels that need anti aliasing between the foreground and border colour draw single pixel divs
                                          if(this.backgroundImage != "")
                                          {
                                              var borderFract = (pixelFraction(intx, inty, borderRadius) * 100);

                                              if(borderFract < 30)
                                              {
										                                        this.drawPixel(intx, inty, this.borderColour, 100, 1, newCorner, 0, this.settings[cc].radius);
                                              }
									                                     else
                                              {
									                                         this.drawPixel(intx, inty, this.borderColour, 100, 1, newCorner, -1, this.settings[cc].radius);
                                              }
                                          }
                                          else
                                          {
                                              var pixelcolour = BlendColour(this.boxColour, this.borderColour, pixelFraction(intx, inty, borderRadius));
                                              this.drawPixel(intx, inty, pixelcolour, 100, 1, newCorner, 0, this.settings[cc].radius, cc);
                                          }
                                      }
                                  }

                                  // Draw bar for the border
                                  if(this.settings.antiAlias)
                                  {
                                      if(y3 >= y2)
                                      {
                                         if (y2 == -1) y2 = 0;
                                         this.drawPixel(intx, y2, this.borderColour, 100, (y3 - y2 + 1), newCorner, 0, 0);
                                      }
                                  }
                                  else
                                  {
                                      if(y3 >= y1)
                                      {
                                          this.drawPixel(intx, (y1 + 1), this.borderColour, 100, (y3 - y1), newCorner, 0, 0);
                                      }
                                  }

                                  // Set the colour for the outside curve
                                  var outsideColour = this.borderColour;
                              }
                              else
                              {
                                  // Set the coour for the outside curve
                                  var outsideColour = this.boxColour;
                                  var y3 = y1;
                              }

                              // Draw aa pixels?
                              if(this.settings.antiAlias)
                              {
                                  // Cycle the y-axis and draw the anti aliased pixels on the outside of the curve
                                  for(var inty = (y3 + 1); inty < y4; inty++)
                                  {
                                      // For each of the pixels that need anti aliasing between the foreground/border colour & background draw single pixel divs
                                      this.drawPixel(intx, inty, outsideColour, (pixelFraction(intx, inty , j) * 100), 1, newCorner, ((this.borderWidth > 0)? 0 : -1), this.settings[cc].radius);
                                  }
                              }
                          }

                          // END OF CORNER CREATION
                          // ---------------------------------------------------- END

                          // We now need to store the current corner in the masterConers array
                          this.masterCorners[this.settings[cc].radius] = newCorner.cloneNode(true);
                      }

                      /*
                      Now we have a new corner we need to reposition all the pixels unless
                      the current corner is the bottom right.
                      */
                      if(cc != "br")
                      {
                          // Loop through all children (pixel bars)
                          for(var t = 0, k = newCorner.childNodes.length; t < k; t++)
                          {
                              // Get current pixel bar
                              var pixelBar = newCorner.childNodes[t];

                              // Get current top and left properties
                              var pixelBarTop    = parseInt(pixelBar.style.top.substring(0, pixelBar.style.top.indexOf("px")));
                              var pixelBarLeft   = parseInt(pixelBar.style.left.substring(0, pixelBar.style.left.indexOf("px")));
                              var pixelBarHeight = parseInt(pixelBar.style.height.substring(0, pixelBar.style.height.indexOf("px")));

                              // Reposition pixels
                              if(cc == "tl" || cc == "bl"){
                                  pixelBar.style.left = this.settings[cc].radius -pixelBarLeft -1 + "px"; // Left
                              }
                              if(cc == "tr" || cc == "tl"){
                                  pixelBar.style.top =  this.settings[cc].radius -pixelBarHeight -pixelBarTop + "px"; // Top
                              }

                              switch(cc)
                              {
                                  case "tr":
                                      pixelBar.style.backgroundPosition  = "-" + Math.abs((this.boxWidth - this.settings[cc].radius + this.borderWidth) + pixelBarLeft) + "px -" + Math.abs(this.settings[cc].radius -pixelBarHeight -pixelBarTop - this.borderWidth) + "px";
                                      break;

                                  case "tl":
                                      pixelBar.style.backgroundPosition = "-" + Math.abs((this.settings[cc].radius -pixelBarLeft -1)  - this.borderWidth) + "px -" + Math.abs(this.settings[cc].radius -pixelBarHeight -pixelBarTop - this.borderWidth) + "px";
                                      break;

                                  case "bl":
                                      pixelBar.style.backgroundPosition = "-" + Math.abs((this.settings[cc].radius -pixelBarLeft -1) - this.borderWidth) + "px -" + Math.abs((this.boxHeight + this.settings[cc].radius + pixelBarTop) -this.borderWidth) + "px";
                                      break;
                              }
                          }
                      }
                  }

                  if(newCorner)
                  {
                      // Position the container
                      switch(cc)
                      {
                          case "tl":
                            if(newCorner.style.position == "absolute") newCorner.style.top  = "0px";
                            if(newCorner.style.position == "absolute") newCorner.style.left = "0px";
                            if(this.topContainer) this.topContainer.appendChild(newCorner);
                            break;

                          case "tr":
                            if(newCorner.style.position == "absolute") newCorner.style.top  = "0px";
                            if(newCorner.style.position == "absolute") newCorner.style.right = "0px";
                            if(this.topContainer) this.topContainer.appendChild(newCorner);
                            break;

                          case "bl":
                            if(newCorner.style.position == "absolute") newCorner.style.bottom  = "0px";
                            if(newCorner.style.position == "absolute") newCorner.style.left = "0px";
                            if(this.bottomContainer) this.bottomContainer.appendChild(newCorner);
                            break;

                          case "br":
                            if(newCorner.style.position == "absolute") newCorner.style.bottom   = "0px";
                            if(newCorner.style.position == "absolute") newCorner.style.right = "0px";
                            if(this.bottomContainer) this.bottomContainer.appendChild(newCorner);
                            break;
                      }
                  }
              }
          }

          /*
          The last thing to do is draw the rest of the filler DIVs.
          We only need to create a filler DIVs when two corners have
          diffrent radiuses in either the top or bottom container.
          */

          // Find out which corner has the biiger radius and get the difference amount
          var radiusDiff = new Array();
          radiusDiff["t"] = Math.abs(this.settings.tl.radius - this.settings.tr.radius)
          radiusDiff["b"] = Math.abs(this.settings.bl.radius - this.settings.br.radius);

          for(z in radiusDiff)
          {
              // FIX for prototype lib
              if(z == "t" || z == "b")
              {
                  if(radiusDiff[z])
                  {
                      // Get the type of corner that is the smaller one
                      var smallerCornerType = ((this.settings[z + "l"].radius < this.settings[z + "r"].radius)? z +"l" : z +"r");

                      // First we need to create a DIV for the space under the smaller corner
                      var newFiller = document.createElement("DIV");
                      newFiller.style.height = radiusDiff[z] + "px";
                      newFiller.style.width  =  this.settings[smallerCornerType].radius+ "px"
                      newFiller.style.position = "absolute";
                      newFiller.style.fontSize = "1px";
                      newFiller.style.overflow = "hidden";
                      newFiller.style.backgroundColor = this.boxColour;
                      //newFiller.style.backgroundColor = get_random_color();

                      // Position filler
                      switch(smallerCornerType)
                      {
                          case "tl":
                              newFiller.style.bottom = "0px";
                              newFiller.style.left   = "0px";
                              newFiller.style.borderLeft = this.borderString;
                              this.topContainer.appendChild(newFiller);
                              break;

                          case "tr":
                              newFiller.style.bottom = "0px";
                              newFiller.style.right  = "0px";
                              newFiller.style.borderRight = this.borderString;
                              this.topContainer.appendChild(newFiller);
                              break;

                          case "bl":
                              newFiller.style.top    = "0px";
                              newFiller.style.left   = "0px";
                              newFiller.style.borderLeft = this.borderString;
                              this.bottomContainer.appendChild(newFiller);
                              break;

                          case "br":
                              newFiller.style.top    = "0px";
                              newFiller.style.right  = "0px";
                              newFiller.style.borderRight = this.borderString;
                              this.bottomContainer.appendChild(newFiller);
                              break;
                      }
                  }

                  // Create the bar to fill the gap between each corner horizontally
                  var newFillerBar = document.createElement("DIV");
                  newFillerBar.style.position = "relative";
                  newFillerBar.style.fontSize = "1px";
                  newFillerBar.style.overflow = "hidden";
                  newFillerBar.style.backgroundColor = this.boxColour;
                  newFillerBar.style.backgroundImage = this.backgroundImage;

                  switch(z)
                  {
                      case "t":
                          // Top Bar
                          if(this.topContainer)
                          {
                              // Edit by Asger Hallas: Check if settings.xx.radius is not false
                              if(this.settings.tl.radius && this.settings.tr.radius)
                              {
                                  newFillerBar.style.height      = topMaxRadius - this.borderWidth + "px";
                                  newFillerBar.style.marginLeft  = this.settings.tl.radius - this.borderWidth + "px";
                                  newFillerBar.style.marginRight = this.settings.tr.radius - this.borderWidth + "px";
                                  newFillerBar.style.borderTop   = this.borderString;

                                  if(this.backgroundImage != "")
                                    newFillerBar.style.backgroundPosition  = "-" + (topMaxRadius + this.borderWidth) + "px 0px";

                                  this.topContainer.appendChild(newFillerBar);
                              }

                              // Repos the boxes background image
                              this.box.style.backgroundPosition      = "0px -" + (topMaxRadius - this.borderWidth) + "px";
                          }
                          break;

                      case "b":
                          if(this.bottomContainer)
                          {
                              // Edit by Asger Hallas: Check if settings.xx.radius is not false
                              if(this.settings.bl.radius && this.settings.br.radius)
                              {
                                  // Bottom Bar
                                  newFillerBar.style.height       = botMaxRadius - this.borderWidth + "px";
                                  newFillerBar.style.marginLeft   = this.settings.bl.radius - this.borderWidth + "px";
                                  newFillerBar.style.marginRight  = this.settings.br.radius - this.borderWidth + "px";
                                  newFillerBar.style.borderBottom = this.borderString;

                                  if(this.backgroundImage != "")
                                    newFillerBar.style.backgroundPosition  = "-" + (botMaxRadius + this.borderWidth) + "px -" + (this.boxHeight + (topMaxRadius + this.borderWidth)) + "px";

                                  this.bottomContainer.appendChild(newFillerBar);
                              }
                          }
                          break;
                  }
              }
          }

          /*
          AutoPad! apply padding if set.
          */
          if(this.settings.autoPad == true && this.boxPadding > 0)
          {
              // Create content container
              var contentContainer = document.createElement("DIV");

              // Set contentContainer's properties
              contentContainer.style.position = "relative";
              contentContainer.innerHTML      = this.boxContent;
              contentContainer.className      = "autoPadDiv";

              // Get padding amounts
              var topPadding = Math.abs(topMaxRadius - this.boxPadding);
              var botPadding = Math.abs(botMaxRadius - this.boxPadding);

              // Apply top padding
              if(topMaxRadius < this.boxPadding)
                contentContainer.style.paddingTop = topPadding + "px";

              // Apply Bottom padding
              if(botMaxRadius < this.boxPadding)
                contentContainer.style.paddingBottom = botMaxRadius + "px";

              // Apply left and right padding
              contentContainer.style.paddingLeft = this.boxPadding + "px";
              contentContainer.style.paddingRight = this.boxPadding + "px";

              // Append contentContainer
              this.contentDIV = this.box.appendChild(contentContainer);
          }
      }

      /*
      This function draws the pixles
      */
      this.drawPixel = function(intx, inty, colour, transAmount, height, newCorner, image, cornerRadius)
      {
          // Create pixel
          var pixel = document.createElement("DIV");
          pixel.style.height   = height + "px";
          pixel.style.width    = "1px";
          pixel.style.position = "absolute";
          pixel.style.fontSize = "1px";
          pixel.style.overflow = "hidden";

          // Max Top Radius
          var topMaxRadius = Math.max(this.settings["tr"].radius, this.settings["tl"].radius);

          // Dont apply background image to border pixels
          if(image == -1 && this.backgroundImage != "")
          {
              pixel.style.backgroundImage = this.backgroundImage;
			           pixel.style.backgroundPosition  = "-" + (this.boxWidth - (cornerRadius - intx) + this.borderWidth) + "px -" + ((this.boxHeight + topMaxRadius + inty) -this.borderWidth) + "px";
		        }
          else
          {
              pixel.style.backgroundColor = colour;
          }

          // Set opacity if the transparency is anything other than 100
          if (transAmount != 100)
            setOpacity(pixel, transAmount);

          // Set the pixels position
          pixel.style.top = inty + "px";
          pixel.style.left = intx + "px";

          newCorner.appendChild(pixel);
      }
  }

  // ------------- UTILITY FUNCTIONS

  // Inserts a element after another
  function insertAfter(parent, node, referenceNode)
  {
	     parent.insertBefore(node, referenceNode.nextSibling);
  }

  /*
  Blends the two colours by the fraction
  returns the resulting colour as a string in the format "#FFFFFF"
  */
  function BlendColour(Col1, Col2, Col1Fraction)
  {
      var red1 = parseInt(Col1.substr(1,2),16);
      var green1 = parseInt(Col1.substr(3,2),16);
      var blue1 = parseInt(Col1.substr(5,2),16);
      var red2 = parseInt(Col2.substr(1,2),16);
      var green2 = parseInt(Col2.substr(3,2),16);
      var blue2 = parseInt(Col2.substr(5,2),16);

      if(Col1Fraction > 1 || Col1Fraction < 0) Col1Fraction = 1;

      var endRed = Math.round((red1 * Col1Fraction) + (red2 * (1 - Col1Fraction)));
      if(endRed > 255) endRed = 255;
      if(endRed < 0) endRed = 0;

      var endGreen = Math.round((green1 * Col1Fraction) + (green2 * (1 - Col1Fraction)));
      if(endGreen > 255) endGreen = 255;
      if(endGreen < 0) endGreen = 0;

      var endBlue = Math.round((blue1 * Col1Fraction) + (blue2 * (1 - Col1Fraction)));
      if(endBlue > 255) endBlue = 255;
      if(endBlue < 0) endBlue = 0;

      return "#" + IntToHex(endRed)+ IntToHex(endGreen)+ IntToHex(endBlue);
  }

  /*
  Converts a number to hexadecimal format
  */
  function IntToHex(strNum)
  {
      base = strNum / 16;
      rem = strNum % 16;
      base = base - (rem / 16);
      baseS = MakeHex(base);
      remS = MakeHex(rem);

      return baseS + '' + remS;
  }


  /*
  gets the hex bits of a number
  */
  function MakeHex(x)
  {
      if((x >= 0) && (x <= 9))
      {
          return x;
      }
      else
      {
          switch(x)
          {
              case 10: return "A";
              case 11: return "B";
              case 12: return "C";
              case 13: return "D";
              case 14: return "E";
              case 15: return "F";
          }
      }
  }


  /*
  For a pixel cut by the line determines the fraction of the pixel on the 'inside' of the
  line.  Returns a number between 0 and 1
  */
  function pixelFraction(x, y, r)
  {
      var pixelfraction = 0;

      /*
      determine the co-ordinates of the two points on the perimeter of the pixel that the
      circle crosses
      */
      var xvalues = new Array(1);
      var yvalues = new Array(1);
      var point = 0;
      var whatsides = "";

      // x + 0 = Left
      var intersect = Math.sqrt((Math.pow(r,2) - Math.pow(x,2)));

      if ((intersect >= y) && (intersect < (y+1)))
      {
          whatsides = "Left";
          xvalues[point] = 0;
          yvalues[point] = intersect - y;
          point =  point + 1;
      }
      // y + 1 = Top
      var intersect = Math.sqrt((Math.pow(r,2) - Math.pow(y+1,2)));

      if ((intersect >= x) && (intersect < (x+1)))
      {
          whatsides = whatsides + "Top";
          xvalues[point] = intersect - x;
          yvalues[point] = 1;
          point = point + 1;
      }
      // x + 1 = Right
      var intersect = Math.sqrt((Math.pow(r,2) - Math.pow(x+1,2)));

      if ((intersect >= y) && (intersect < (y+1)))
      {
          whatsides = whatsides + "Right";
          xvalues[point] = 1;
          yvalues[point] = intersect - y;
          point =  point + 1;
      }
      // y + 0 = Bottom
      var intersect = Math.sqrt((Math.pow(r,2) - Math.pow(y,2)));

      if ((intersect >= x) && (intersect < (x+1)))
      {
          whatsides = whatsides + "Bottom";
          xvalues[point] = intersect - x;
          yvalues[point] = 0;
      }

      /*
      depending on which sides of the perimeter of the pixel the circle crosses calculate the
      fraction of the pixel inside the circle
      */
      switch (whatsides)
      {
              case "LeftRight":
              pixelfraction = Math.min(yvalues[0],yvalues[1]) + ((Math.max(yvalues[0],yvalues[1]) - Math.min(yvalues[0],yvalues[1]))/2);
              break;

              case "TopRight":
              pixelfraction = 1-(((1-xvalues[0])*(1-yvalues[1]))/2);
              break;

              case "TopBottom":
              pixelfraction = Math.min(xvalues[0],xvalues[1]) + ((Math.max(xvalues[0],xvalues[1]) - Math.min(xvalues[0],xvalues[1]))/2);
              break;

              case "LeftBottom":
              pixelfraction = (yvalues[0]*xvalues[1])/2;
              break;

              default:
              pixelfraction = 1;
      }

      return pixelfraction;
  }


  // This function converts CSS rgb(x, x, x) to hexadecimal
  function rgb2Hex(rgbColour)
  {
      try{

          // Get array of RGB values
          var rgbArray = rgb2Array(rgbColour);

          // Get RGB values
          var red   = parseInt(rgbArray[0]);
          var green = parseInt(rgbArray[1]);
          var blue  = parseInt(rgbArray[2]);

          // Build hex colour code
          var hexColour = "#" + IntToHex(red) + IntToHex(green) + IntToHex(blue);
      }
      catch(e){

          alert("There was an error converting the RGB value to Hexadecimal in function rgb2Hex");
      }

      return hexColour;
  }

  // Returns an array of rbg values
  function rgb2Array(rgbColour)
  {
      // Remove rgb()
      var rgbValues = rgbColour.substring(4, rgbColour.indexOf(")"));

      // Split RGB into array
      var rgbArray = rgbValues.split(", ");

      return rgbArray;
  }

  /*
  Function by Simon Willison from sitepoint.com
  Modified by Cameron Cooke adding Safari's rgba support
  */
  function setOpacity(obj, opacity)
  {
      opacity = (opacity == 100)?99.999:opacity;

      if(isSafari && obj.tagName != "IFRAME")
      {
          // Get array of RGB values
          var rgbArray = rgb2Array(obj.style.backgroundColor);

          // Get RGB values
          var red   = parseInt(rgbArray[0]);
          var green = parseInt(rgbArray[1]);
          var blue  = parseInt(rgbArray[2]);

          // Safari using RGBA support
          obj.style.backgroundColor = "rgba(" + red + ", " + green + ", " + blue + ", " + opacity/100 + ")";
      }
      else if(typeof(obj.style.opacity) != "undefined")
      {
          // W3C
          obj.style.opacity = opacity/100;
      }
      else if(typeof(obj.style.MozOpacity) != "undefined")
      {
          // Older Mozilla
          obj.style.MozOpacity = opacity/100;
      }
      else if(typeof(obj.style.filter) != "undefined")
      {
          // IE
          obj.style.filter = "alpha(opacity:" + opacity + ")";
      }
      else if(typeof(obj.style.KHTMLOpacity) != "undefined")
      {
          // Older KHTML Based Browsers
          obj.style.KHTMLOpacity = opacity/100;
      }
  }

  /*
  Returns index if the passed value is found in the
  array otherwise returns false.
  */
  function inArray(array, value)
  {
      for(var i = 0; i < array.length; i++){

          // Matches identical (===), not just similar (==).
          if (array[i] === value) return i;
      }

      return false;
  }

  /*
  Returns true if the passed value is found as a key
  in the array otherwise returns false.
  */
  function inArrayKey(array, value)
  {
      for(key in array){

          // Matches identical (===), not just similar (==).
          if(key === value) return true;
      }

      return false;
  }

  // Cross browser add event wrapper
  function addEvent(elm, evType, fn, useCapture) {
	  if (elm.addEventListener) {
		  elm.addEventListener(evType, fn, useCapture);
		  return true;
	  }
	  else if (elm.attachEvent) {
		  var r = elm.attachEvent('on' + evType, fn);
		  return r;
	  }
	  else {
		  elm['on' + evType] = fn;
	  }
  }

  // Cross browser remove event wrapper
  function removeEvent(obj, evType, fn, useCapture){
    if (obj.removeEventListener){
      obj.removeEventListener(evType, fn, useCapture);
      return true;
    } else if (obj.detachEvent){
      var r = obj.detachEvent("on"+evType, fn);
      return r;
    } else {
      alert("Handler could not be removed");
    }
  }

  // Formats colours
  function format_colour(colour)
  {
      var returnColour = "#ffffff";

      // Make sure colour is set and not transparent
      if(colour != "" && colour != "transparent")
      {
          // RGB Value?
          if(colour.substr(0, 3) == "rgb")
          {
              // Get HEX aquiv.
              returnColour = rgb2Hex(colour);
          }
          else if(colour.length == 4)
          {
              // 3 chr colour code add remainder
              returnColour = "#" + colour.substring(1, 2) + colour.substring(1, 2) + colour.substring(2, 3) + colour.substring(2, 3) + colour.substring(3, 4) + colour.substring(3, 4);
          }
          else
          {
              // Normal valid hex colour
              returnColour = colour;
          }
      }

      return returnColour;
  }

  // Returns the style value for the property specfied
  function get_style(obj, property, propertyNS)
  {
      try
      {
          if(obj.currentStyle)
          {
              var returnVal = eval("obj.currentStyle." + property);
          }
          else
          {
              /*
              Safari does not expose any information for the object if display is
              set to none is set so we temporally enable it.
              */
              if(isSafari && obj.style.display == "none")
              {
                obj.style.display = "";
                var wasHidden = true;
              }

              var returnVal = document.defaultView.getComputedStyle(obj, '').getPropertyValue(propertyNS);

              // Rehide the object
              if(isSafari && wasHidden)
              {
                  obj.style.display = "none";
              }
          }
      }
      catch(e)
      {
          // Do nothing
      }

      return returnVal;
  }

  // Get elements by class by Dustin Diaz.
  function getElementsByClass(searchClass, node, tag)
  {
	     var classElements = new Array();

      if(node == null)
		      node = document;
	     if(tag == null)
		      tag = '*';

	     var els = node.getElementsByTagName(tag);
	     var elsLen = els.length;
	     var pattern = new RegExp("(^|\s)"+searchClass+"(\s|$)");

	     for (i = 0, j = 0; i < elsLen; i++)
      {
		        if(pattern.test(els[i].className))
          {
			           classElements[j] = els[i];
			           j++;
		        }
	     }

	     return classElements;
  }

  // Displays error message
  function newCurvyError(errorMessage)
  {
      return new Error("curvyCorners Error:\n" + errorMessage)
  }