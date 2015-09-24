/*
 * ====================================================================
 * About Sarissa: http://dev.abiss.gr/sarissa
 * ====================================================================
 * Sarissa is an ECMAScript library acting as a cross-browser wrapper for native XML APIs.
 * The library supports Gecko based browsers like Mozilla and Firefox,
 * Internet Explorer (5.5+ with MSXML3.0+), Konqueror, Safari and Opera
 * @author: @author: Copyright 2004-2007 Emmanouil Batsis, mailto: mbatsis at users full stop sourceforge full stop net
 * ====================================================================
 * Licence
 * ====================================================================
 * Sarissa is free software distributed under the GNU GPL version 2 (see <a href="gpl.txt">gpl.txt</a>) or higher, 
 * GNU LGPL version 2.1 (see <a href="lgpl.txt">lgpl.txt</a>) or higher and Apache Software License 2.0 or higher 
 * (see <a href="asl.txt">asl.txt</a>). This means you can choose one of the three and use that if you like. If 
 * you make modifications under the ASL, i would appreciate it if you submitted those.
 * In case your copy of Sarissa does not include the license texts, you may find
 * them online in various formats at <a href="http://www.gnu.org">http://www.gnu.org</a> and 
 * <a href="http://www.apache.org">http://www.apache.org</a>.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY 
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE 
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * Sort the table data based on the column corresponding to the given TH element (clickedElem).
 * @memberOf Sarissa
 * @param {Node} clickedElem the table heading (<code>th</code>) initiating the sort.
 * @param {Function} iFunc the custom sort function if needed. Default (null) is case-sensitive sort.
 * You can also use <code>Sarissa.SORT_IGNORE_CASE</code>, <code>Sarissa.SORT_DATE_US</code>, 
 * and <code>Sarissa.SORT_DATE_EU</code>
 * @param {boolean} bSkipCache whether to skip the data cache and read table data all over again. Setting this
 * to <code>true</code> means the cache for the table, if it exists, will not be updated either. Defaul is <code>false</code>
 * @param {Function} oCallbac a callback function to be executed when the table is 
 * sorted and updated. The callback function may be used for effects for example. The parameters 
 * passed to the callback are the table as a DOM node and the sort column index (zero based <code>int</code>)
 * @requires Sarissa sarissa.js
 */
Sarissa.sortHtmlTableData = function(clickedElem, iFunc, bSkipCache, oCallbac){
	// get the table
	var oTbl = clickedElem.parentNode.parentNode;
	while(oTbl.nodeName.toLowerCase() != "table"){
	    oTbl = oTbl.parentNode;
	}
	// we need a table ID for the cache
	if(!oTbl.id){
		oTbl.id = "SarissaTable"+ (Sarissa.tableIdGenCount++);
	}
	// the column to sort on
	var iColIndex = clickedElem.cellIndex;
	var matrix;
	// use the cache if available and permitted
	if(!bSkipCache && Sarissa.tableDataCache[oTbl.id]){
		matrix = Sarissa.tableDataCache[oTbl.id];
	}
	else{
		// read table, skip any rows containing headings, cache if permitted
		matrix = this.getArrayFromTableData(oTbl, null, null, "th");
		if(!bSkipCache){
			Sarissa.tableDataCache[oTbl.id] = matrix;
		}
	}
	// init state persistence as needed
	if(!Sarissa.tableColumnSortStates[oTbl.id]){
		Sarissa.tableColumnSortStates[oTbl.id] = [];
	}
	// build a array to sort from the specific column data, adding 
	// original index info as a suffix
	var sortedColumn = [];
	for(var i=0; i < matrix.length;i++){
		sortedColumn[i] = Sarissa.stripTags(matrix[i][iColIndex]) + "_mbns_" + i;
	}
	// sort the array
	if(iFunc){
		sortedColumn.sort(iFunc);
	}
	else{
		sortedColumn.sort();
	}
	// persist column state
	var sortOrder = Sarissa.tableColumnSortStates[oTbl.id][iColIndex];
	if(sortOrder != "asc"){
		Sarissa.tableColumnSortStates[oTbl.id][iColIndex] = "asc";
	}
	else{
		sortedColumn.reverse();
		Sarissa.tableColumnSortStates[oTbl.id][iColIndex] = "desc";
	}
	// create the sorted matrix based on sortedColumn
	var sortedMatrix = [];
	for(var j=0; j < matrix.length; j++){
		var indexItem = sortedColumn[j];
		var iRow = indexItem.substring(indexItem.indexOf("_mbns_")+6, indexItem.length);
		sortedMatrix[j] = [];
		for(var k=0; k < matrix[j].length; k++){
			sortedMatrix[j][k] = matrix[iRow][k];
		}
	}
	// update table data, skipping rows with headings
	this.updateTableData(oTbl, sortedMatrix, null, null, "th");
	if(oCallbac){
		oCallbac(oTbl, iColIndex);	
	}
};

/**
 * Used for generating table IDs, which are required for the cache and sort state persistance 
 * @memberOf Sarissa
 * @private
 */
Sarissa.tableIdGenCount = 0;

/**
 * Used for persisting sort state per table column
 * @memberOf Sarissa
 * @private
 */
Sarissa.tableColumnSortStates = [];

/**
 * Used for caching table data.
 * @memberOf Sarissa
 */
Sarissa.tableDataCache = [];

/**
 * Keep track of the cache size. The length property is not for associative arrays 
 * and I really dont want to add 50 lines and implement a PseudoHashMap right now :-)
 * @memberOf Sarissa
 * @private
 */
Sarissa.tableDataCacheSize = 0;

/**
 * The table data cache size, used for sorting HTML tables. You can change it, default is 5 (tables). When a  
 * table is cached exceeding the cache size, the oldest entry is disgarded from the cache.
 * @memberOf Sarissa
 */
Sarissa.tableDataCacheMaxSize = 5;

/**
 * Updates the cache, discards oldest entry if cache size is exceeded.
 * @memberOf Sarissa
 * @private
 */
Sarissa.tableDataCachePut = function(sTableId, oArr){
	if(Sarissa.tableDataCacheSize.length >= Sarissa.tableDataCacheMaxSize){
		Sarissa.tableDataCache.shift();
		Sarissa.tableDataCacheSize--;
	}
	Sarissa.tableDataCache[sTableId] = oArr;
	Sarissa.tableDataCacheSize++;
};
/**
 * Updates the cache of a specific table by reposition a column in the cached data.
 * This is usefull if you use DHTML to visually reposition columns and need to 
 * synchronize the cache.
 * @memberOf Sarissa
 * @private
 */
Sarissa.tableDataCacheMoveColumn = function(sTableId, oldColumnIndex, newColumnIndex){	
	var oldMatrix = Sarissa.tableDataCache[sTableId];
	var newMatrix = [];
	// iterate rows
	var oldRow, movedColumn, newRow;
	for(var i=0; i<oldMatrix.length; i++){
		oldRow = oldMatrix[i];
		movedColumn = oldRow.splice(oldColumnIndex, 1);
		newRow = [];
		// reposition column value
		for(var j=0;j<oldArr.length;J++){
			if(j == newColumnIndex){
				newRow.put(movedColumn);
			}
			newRow.put(oldRow[j]);
		}
		newMatrix[i] = newRow;
	}
	Sarissa.tableDataCache[sTableId] = newMatrix;
};

/**
 * Function for case-insensitive sorting or simple comparison. Can be used as 
 * a parameter to <code>Array.sort()</code>.
 * @memberOf Sarissa
 * @param a a string
 * @param b a string
 * @return -1, 0 or 1 depending on whether <code>a</code> is "less than", equal or "greater than" <code>b</code>
 */
Sarissa.SORT_IGNORE_CASE = function(a, b){
  var strA = a.toLowerCase(),
      strB = b.toLowerCase();
  if(strA < strB) return -1;
  else if(strA > strB) return 1;
  else return 0;
};

/**
 * Function for comparing US dates. Can be used as 
 * a parameter to <code>Array.sort()</code>.
 * @memberOf Sarissa
 * @param a a string
 * @param b a string
 * @return -1, 0 or 1 depending on whether <code>a</code> is "less than", equal or "greater than" <code>b</code>
 */
Sarissa.SORT_DATE_US = function(a, b){
	var datA = new Date(a.substring(0, a.lastIndexOf("_mbns_"))),
		datB = new Date(b.substring(0, b.lastIndexOf("_mbns_")));
	if(datA < datB)	return -1;
	else if(datA > datB) return 1;
    else return 0;
    
};

/**
 * Function for comparing EU dates. Can be used as 
 * a parameter to <code>Array.sort()</code>.
 * @memberOf Sarissa
 * @param a a string
 * @param b a string
 * @return -1, 0 or 1 depending on whether <code>a</code> is "less than", equal or "greater than" <code>b</code>
 */
Sarissa.SORT_DATE_EU = function(a, b){
	var strA = a.substring(0, a.lastIndexOf("_mbns_")).split("/"), 
		strB = b.substring(0, b.lastIndexOf("_mbns_")).split("/"),
		datA = new Date(strA[2], strA[1], strA[0]), 
		datB = new Date(strB[2], strB[1], strB[0]);
	if(datA < datB) return -1;
	else if(datA > datB) return 1;
    else return 0;
};

/**
 * Get the data of the given element as a two-dimensional array. The 
 * given XML or HTML Element must match the structure of an HTML table, 
 * although element names may be different.
 * @memberOf Sarissa
 * @param oElem an HTML or XML table. The method works out of the box 
 * for <code>table</code>, <code>tbody</code>, <code>thead</code> 
 * or <code>tfooter</code> elements. For custom XML tables, the 
 * <code>sRowName</code> <code>sCellName</code> must be used.
 * @param sRowName the row element names. Default is <code>tr</code>
 * @param sCellName the row element names. Default is <code>td</code>
 * @param sHeadingName the heading element names. If you use this, rows with 
 * headings will be <strong>skipped</strong>. To skip headings when reading 
 * HTML tables use <code>th</code>
 * @param bStripTags whether to strip markup from cell contents. Default is <code>false</code>
 * @return a two-dimensional array with the data found in the given element's rows
 */
Sarissa.getArrayFromTableData = function(oElem, sRowName, sCellName, sHeadingName, bStripTags){
	if(!sRowName){
		sRowName = "tr"
	}
	if(!sCellName){
		sCellName = "td"
	}
	if(!sHeadingName){
		sHeadingName = "th"
	}
	var rows = oElem.getElementsByTagName(sRowName);
	var matrix = [];
	for(var i=0, j=0; i < rows.length; i++) {
		// skip rows with headings
		var row = rows[i];
		if((!sHeadingName) || row.getElementsByTagName(sHeadingName).length == 0){
			matrix[j] = [];
			var cells = row.getElementsByTagName(sCellName);
			for(var k=0; k < cells.length; k++){
				matrix[j][k] = bStripTags ? Sarissa.stripTags(cells[k].innerHTML) : cells[k].innerHTML;
			}
			j++;
		}
	}
	return matrix;
};

/**
 * Update the data of the given element using the giventwo-dimensional array as a source. The 
 * given XML or HTML Element must match the structure of an HTML table.
 * @memberOf Sarissa
 * @param oElem an HTML or XML table. The method works out of the box 
 * for <code>table</code>, <code>tbody</code>, <code>thead</code> 
 * or <code>tfooter</code> elements. For custom XML tables, the 
 * <code>sRowName</code> <code>sCellName</code> must be used.
 * @param sRowName the row element names. Default is <code>tr</code>
 * @param sCellName the row element names. Default is <code>td</code>
 * @param sHeadingName the heading element names. If you use this, rows with 
 * headings will be <strong>skipped</strong>. To skip headings when reading 
 * HTML tables use <code>th</code>
 */
Sarissa.updateTableData = function(oElem, newData, sRowName, sCellName, sHeadingName){
	if(!sRowName){
		sRowName = "tr"
	}
	if(!sCellName){
		sCellName = "td"
	}
	var rows = oElem.getElementsByTagName(sRowName);
	for(var i=0, j=0; i < newData.length && j < rows.length; j++){
		// skip rows with headings
		var row = rows[j];
		if((!sHeadingName) || row.getElementsByTagName(sHeadingName).length == 0){
			var cells = row.getElementsByTagName(sCellName);
			for(var k=0; k < cells.length; k++){
				cells[k].innerHTML = newData[i][k];
			}
			i++;
		}
	}
};


