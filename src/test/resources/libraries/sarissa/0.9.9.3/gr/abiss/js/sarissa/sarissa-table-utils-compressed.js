
/* * ====================================================================
 * About: This a a compressed JS file from the Sarissa library. 
 * see http://dev.abiss.gr/sarissa
 * 
 * Copyright: Manos Batsis, http://dev.abiss.gr
 * 
 * Licence:
 * Sarissa is free software distributed under the GNU GPL version 2 
 * or higher, GNU LGPL version 2.1 or higher and Apache Software 
 * License 2.0 or higher. The licenses are available online see: 
 * http://www.gnu.org  
 * http://www.apache.org
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY 
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE 
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * ====================================================================*/

Sarissa.sortHtmlTableData=function(clickedElem,iFunc,bSkipCache,oCallbac){var oTbl=clickedElem.parentNode.parentNode;while(oTbl.nodeName.toLowerCase()!="table"){oTbl=oTbl.parentNode;}
if(!oTbl.id){oTbl.id="SarissaTable"+(Sarissa.tableIdGenCount++);}
var iColIndex=clickedElem.cellIndex;var matrix;if(!bSkipCache&&Sarissa.tableDataCache[oTbl.id]){matrix=Sarissa.tableDataCache[oTbl.id];}
else{matrix=this.getArrayFromTableData(oTbl,null,null,"th");if(!bSkipCache){Sarissa.tableDataCache[oTbl.id]=matrix;}}
if(!Sarissa.tableColumnSortStates[oTbl.id]){Sarissa.tableColumnSortStates[oTbl.id]=[];}
var sortedColumn=[];for(var i=0;i<matrix.length;i++){sortedColumn[i]=Sarissa.stripTags(matrix[i][iColIndex])+"_mbns_"+i;}
if(iFunc){sortedColumn.sort(iFunc);}
else{sortedColumn.sort();}
var sortOrder=Sarissa.tableColumnSortStates[oTbl.id][iColIndex];if(sortOrder!="asc"){Sarissa.tableColumnSortStates[oTbl.id][iColIndex]="asc";}
else{sortedColumn.reverse();Sarissa.tableColumnSortStates[oTbl.id][iColIndex]="desc";}
var sortedMatrix=[];for(var j=0;j<matrix.length;j++){var indexItem=sortedColumn[j];var iRow=indexItem.substring(indexItem.indexOf("_mbns_")+6,indexItem.length);sortedMatrix[j]=[];for(var k=0;k<matrix[j].length;k++){sortedMatrix[j][k]=matrix[iRow][k];}}
this.updateTableData(oTbl,sortedMatrix,null,null,"th");if(oCallbac){oCallbac(oTbl,iColIndex);}};Sarissa.tableIdGenCount=0;Sarissa.tableColumnSortStates=[];Sarissa.tableDataCache=[];Sarissa.tableDataCacheSize=0;Sarissa.tableDataCacheMaxSize=5;Sarissa.tableDataCachePut=function(sTableId,oArr){if(Sarissa.tableDataCacheSize.length>=Sarissa.tableDataCacheMaxSize){Sarissa.tableDataCache.shift();Sarissa.tableDataCacheSize--;}
Sarissa.tableDataCache[sTableId]=oArr;Sarissa.tableDataCacheSize++;};Sarissa.tableDataCacheMoveColumn=function(sTableId,oldColumnIndex,newColumnIndex){var oldMatrix=Sarissa.tableDataCache[sTableId];var newMatrix=[];var oldRow,movedColumn,newRow;for(var i=0;i<oldMatrix.length;i++){oldRow=oldMatrix[i];movedColumn=oldRow.splice(oldColumnIndex,1);newRow=[];for(var j=0;j<oldArr.length;J++){if(j==newColumnIndex){newRow.put(movedColumn);}
newRow.put(oldRow[j]);}
newMatrix[i]=newRow;}
Sarissa.tableDataCache[sTableId]=newMatrix;};Sarissa.SORT_IGNORE_CASE=function(a,b){var strA=a.toLowerCase(),strB=b.toLowerCase();if(strA<strB)return-1;else if(strA>strB)return 1;else return 0;};Sarissa.SORT_DATE_US=function(a,b){var datA=new Date(a.substring(0,a.lastIndexOf("_mbns_"))),datB=new Date(b.substring(0,b.lastIndexOf("_mbns_")));if(datA<datB)return-1;else if(datA>datB)return 1;else return 0;};Sarissa.SORT_DATE_EU=function(a,b){var strA=a.substring(0,a.lastIndexOf("_mbns_")).split("/"),strB=b.substring(0,b.lastIndexOf("_mbns_")).split("/"),datA=new Date(strA[2],strA[1],strA[0]),datB=new Date(strB[2],strB[1],strB[0]);if(datA<datB)return-1;else if(datA>datB)return 1;else return 0;};Sarissa.getArrayFromTableData=function(oElem,sRowName,sCellName,sHeadingName,bStripTags){if(!sRowName){sRowName="tr"}
if(!sCellName){sCellName="td"}
if(!sHeadingName){sHeadingName="th"}
var rows=oElem.getElementsByTagName(sRowName);var matrix=[];for(var i=0,j=0;i<rows.length;i++){var row=rows[i];if((!sHeadingName)||row.getElementsByTagName(sHeadingName).length==0){matrix[j]=[];var cells=row.getElementsByTagName(sCellName);for(var k=0;k<cells.length;k++){matrix[j][k]=bStripTags?Sarissa.stripTags(cells[k].innerHTML):cells[k].innerHTML;}
j++;}}
return matrix;};Sarissa.updateTableData=function(oElem,newData,sRowName,sCellName,sHeadingName){if(!sRowName){sRowName="tr"}
if(!sCellName){sCellName="td"}
var rows=oElem.getElementsByTagName(sRowName);for(var i=0,j=0;i<newData.length&&j<rows.length;j++){var row=rows[j];if((!sHeadingName)||row.getElementsByTagName(sHeadingName).length==0){var cells=row.getElementsByTagName(sCellName);for(var k=0;k<cells.length;k++){cells[k].innerHTML=newData[i][k];}
i++;}}};