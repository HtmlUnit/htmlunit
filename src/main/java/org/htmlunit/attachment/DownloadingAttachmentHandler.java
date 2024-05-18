package org.htmlunit.attachment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.htmlunit.Page;

public class DownloadingAttachmentHandler implements AttachmentHandler {

	private Path downloadFolder;

	public DownloadingAttachmentHandler(Path downloadFolder) {
		this.downloadFolder = downloadFolder;
//		TODO throwing unchecked exception seems like a bad idea, wait for feedback from Ronald
		if (!Files.isWritable(downloadFolder)) throw new RuntimeException("Can't write to ".concat(downloadFolder.toString()));
	}

	public DownloadingAttachmentHandler() {
		this(Path.of(System.getProperty("java.io.tmpdir")));
	}
	
	@Override
	public void handleAttachment(Page page, String attachmentFilename) {
		attachmentFilename = getFilenameFromUrlIfEmpty(page, attachmentFilename);
		Path downloadDestinationFilePath = downloadFolder.resolve(attachmentFilename);
		InputStream contentAsStream;
		try {
			contentAsStream = page.getWebResponse().getContentAsStream();
		} catch (IOException e) {
//			TODO throw exception?
			e.printStackTrace();
			return;
		}
		try {
			Files.copy(contentAsStream, downloadDestinationFilePath);
		} catch (FileAlreadyExistsException e) {
			try {
				Files.copy(contentAsStream, findNextAvailableFilename(attachmentFilename));
			} catch (IOException e1) {
//				TODO throw exception?
				e1.printStackTrace();
				return;
			}
		} catch (IOException e) {
//			TODO throw exception?
			System.out.println("something wrong happened");
			e.printStackTrace();
			return;
		}
		page.getWebResponse().cleanUp();

		System.out.println("Handling attahcment");
	}

	private String getFilenameFromUrlIfEmpty(Page page, String attachmentFilename) {
		if (attachmentFilename==null) {
			String file = page.getWebResponse().getWebRequest().getUrl().getFile();
			attachmentFilename = file.substring(file.lastIndexOf('/') + 1);
		}
		return attachmentFilename;
	}

	private Path findNextAvailableFilename(String fileName) {
		Path newPath;
		Integer count = 0;
		do  {
			count++;
		int fileExtensionstartPosition = (fileName.contains(".") ) ? fileName.lastIndexOf('.') : fileName.length() ;
		newPath = downloadFolder.resolve(fileName.substring(0, fileExtensionstartPosition).concat(" (" + count + ")").concat(fileName.substring(fileExtensionstartPosition)));
		} while (Files.exists(newPath, LinkOption.NOFOLLOW_LINKS));
		return newPath;
	}

}
