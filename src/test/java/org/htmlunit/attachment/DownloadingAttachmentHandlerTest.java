package org.htmlunit.attachment;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.htmlunit.BrowserVersion;
import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

//@RunWith(BrowserRunner.class)
public class DownloadingAttachmentHandlerTest {

	@Test
	public void test() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
		System.out.println(System.getProperty("java.io.tmpdir"));
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX);
		webClient.setAttachmentHandler(new DownloadingAttachmentHandler());
//		HtmlPage page = webClient.getPage("https://github.com/HtmlUnit/htmlunit/releases");
//		page.getAnchorByText("htmlunit-4.1.0-javadoc.jar.asc").click();
		HtmlPage page = webClient.getPage("https://www.ghisler.com/download.htm");
		HtmlAnchor anchorByHref = page.getAnchorByHref("https://totalcommander.ch/1103/tcmd1103x32_64.exe");
//		HtmlPage page = webClient.getPage("https://www.ronsplace.ca/products/ronsdataedit/download");
//		HtmlAnchor anchorByHref = page.getAnchorByHref("/contentproduct/data-edit/setup.exe");
		System.out.println(anchorByHref.asXml());
		anchorByHref.click();
		
		Thread.sleep(100000);
		
//		Path.of(System.getProperty("java.io.tmpdir")
		
	}
	
//	TODO test downloading a file that is an octet stream
//	TODO test downloading a file that has content disposition and proper attachment header
//	TODO test downloading a file that does not have an extension (no dot in filename)
//	TODO test constructor failing if the download folder does not exist or user does not have write access


}
