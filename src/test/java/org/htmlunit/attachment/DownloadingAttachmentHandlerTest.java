package org.htmlunit.attachment;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.htmlunit.BrowserVersion;
import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

public class DownloadingAttachmentHandlerTest {

	private static final String FILENAME_WITHOUT_EXTENSION = "fileNoDot";
	private static final String FILENAME_WITH_EXTENSION = "file1.txt";
	@Rule
	public File downloadFolder = new TemporaryFolder().getRoot();

	@Test
	public void whenDownloadFolderWriteable_downloadAttachment() throws IOException {
		DownloadingAttachmentHandler downloadingAttachmentHandler = new DownloadingAttachmentHandler(
				downloadFolder.toPath());
//		TODO add html page with attachment response that is using content disposition
//		TODO add html page with attachment response that is using octet stream
//		new HtmlPage().
		downloadingAttachmentHandler.handleAttachment(null, FILENAME_WITH_EXTENSION);
		Assert.assertTrue(new File(downloadFolder, FILENAME_WITH_EXTENSION).exists());
		downloadingAttachmentHandler.handleAttachment(null, FILENAME_WITHOUT_EXTENSION);
		Assert.assertTrue(new File(downloadFolder, FILENAME_WITHOUT_EXTENSION).exists());
	}

	@Test
	public void whenDownloadFolderDoesNotExit_throwException() {
		Assert.assertThrows(IOException.class, () -> {
			new DownloadingAttachmentHandler(downloadFolder.toPath().resolve("nonExistentFolder"));
		});
	}

}
