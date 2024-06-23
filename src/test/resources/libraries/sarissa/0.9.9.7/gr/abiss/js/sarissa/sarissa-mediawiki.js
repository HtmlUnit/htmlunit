/**
 * ====================================================================
 * About Sarissa: http://dev.abiss.gr/sarissa
 * ====================================================================
 * Sarissa cross browser XML library - IE XPath Emulation 
 * @version ${project.version}
 * @author: Copyright 2004-2008 Emmanouil Batsis, mailto: mbatsis at users full stop sourceforge full stop net
 *
 * This script depends on sarissa.js and provides an API for remote MediaWiki
 * JSON API calls.
 * 
 * @author: Copyright 2003-2008 Emmanouil Batsis, mailto: mbatsis at users full stop sourceforge full stop net
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
 * Class that can be used to perform queries against a MediaWiki instance 
 * @constructor
 * @requires Sarissa
 * @param {String} apiUrl the base API URL, e.g. <a href="http://en.wikipedia.org/w/api.php" title="Link to Wikipedia's MediaWiki API Instance">http://en.wikipedia.org/w/api.php</a>
 * @param {Function} callback the callback function to use
 */ 
function SarissaMediaWikiContext(apiUrl, arrLanguages){
	this.baseUrl = apiUrl;
	this.format = "json";
	this.languages = arrLanguages;
};


/**
 * Asynchronously obtain an article from the Wiki, then pass it to the given 
 * callback function as JSON data. This method does any required URL encoding for you.
 * @param {String} sFor the article name
 * @param {int} iLimit the maximum number of results to retreive
 */ 
SarissaMediaWikiContext.prototype.doArticleGet = function(sFor, callback){
	Sarissa.setRemoteJsonCallback(
		this.baseUrl + 
			//"?action=query&redirects&format=" + 
			"?action=parse&redirects&format=" +
			this.format + 
			"&page" + 
			encodeURIComponent(sFor), 
		callback);
};

/**
 * Asynchronously obtain an article's backlinks from the Wiki, then pass those to the given 
 * callback function as JSON data. This method does any required URL encoding for you.
 * @param {String} sFor the article name
 * @param {int} iLimit the maximum number of results to retreive
 * @param {Function} callback the callback function to use
 */ 
SarissaMediaWikiContext.prototype.doBacklinksGet = function(sFor, iLimit, callback){
	Sarissa.setRemoteJsonCallback(
		this.baseUrl + 
			"?&generator=backlinks&format=" + 
			this.format + 
			"&gbllimit=" + 
			iLimit + 
			"&gbltitle" + 
			encodeURIComponent(sFor), 
		callback);
};

/**
 * Asynchronously perform a Wiki Search, passing the results to the given 
 * callback function as JSON data. This method does any required URL encoding for you.
 * @param {String} sFor the terms to look for
 * @param {int} iLimit the maximum number of results to retreive
 * @param {Function} callback the callback function to use
 */ 
SarissaMediaWikiContext.prototype.doSearch = function(sFor, iLimit, callback){
	Sarissa.setRemoteJsonCallback(
		this.baseUrl + 
			"?action=query&list=search&srsearch=" + 
			encodeURIComponent(sFor) + 
			"&srwhat=text&srnamespace=0&format=" +
			this.format + 
			"&srlimit=" + 
			iLimit, 
		callback);
};

/**
 * Asynchronously obtain the articles belonging to a category from the Wiki, 
 * then pass those to the given callback function as JSON data. This method 
 * does any required URL encoding for you.
 * @param {String} sFor the article name
 * @param {int} iLimit the maximum number of results to retreive
 * @param {Function} callback the callback function to use
 */ 
SarissaMediaWikiContext.prototype.doCategorySearch = function(sFor, iLimit, callback){
	Sarissa.setRemoteJsonCallback(
		this.baseUrl + 
			"?format=" + 
			this.format + 
			"&list=categorymembers&action=query&cmlimit=" + 
			iLimit + 
			"&cmtitle=Category:" + 
			encodeURIComponent(sFor), 
		callback);
};
/**
 * Asynchronously obtain the Wiki categories an article belongs to, 
 * then pass those to the given callback function as JSON data. This method 
 * does any required URL encoding for you.
 * @param {String} sFor the article name
 * @param {int} iLimit the maximum number of results to retreive
 * @param {Function} callback the callback function to use
 */ 
SarissaMediaWikiContext.prototype.doArticleCategoriesGet = function(sFor, iLimit, callback){
	Sarissa.setRemoteJsonCallback(
		this.baseUrl + 
			"?format=" + 
			this.format + 
			"&action=query&prop=categories&titles=" + 
			encodeURIComponent(sFor), 
		callback);
};



