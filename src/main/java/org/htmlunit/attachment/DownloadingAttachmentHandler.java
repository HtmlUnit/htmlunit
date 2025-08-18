/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.attachment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.Page;
import org.htmlunit.WebResponse;
import org.htmlunit.util.StringUtils;

/**
 * Implementation of an {@link AttachmentHandler} that mimics how browsers handle attachments, specifically
 * <ul>
 * <li>download file into a default folder when attachment response is detected</li>
 * <li>infer filename from octet stream response and use that when saving file</li>
 * <li>if a file already exists, append number to it.
 * Keep incrementing numbers until you find a slot that is free (thats how Chrome handles duplicate filenames).</li>
 * </ul>
 *
 * @author Marek Andreansky
 * @author Ronald Brill
 */
public class DownloadingAttachmentHandler implements AttachmentHandler {

    private static final Log LOG = LogFactory.getLog(DownloadingAttachmentHandler.class);

    private Path downloadFolder_;

    /**
     * Creates a new DownloadingAttachmentHandler that stores all downloaded files in the
     * provided directory. The directory must exist and be writable.
     *
     * @param downloadFolder the path to the folder for storing all downloaded files
     * @throws IOException if the folder does not exist or the folder is not writable
     */
    public DownloadingAttachmentHandler(final Path downloadFolder) throws IOException {
        downloadFolder_ = downloadFolder;
        if (Files.notExists(downloadFolder)) {
            throw new IOException("The provided download folder '"
                        + downloadFolder + "' does not exist");
        }
        if (!Files.isWritable(downloadFolder)) {
            throw new IOException("Can't write to the download folder '"
                        + downloadFolder + "'");
        }
    }

    /**
     * Creates a new DownloadingAttachmentHandler that stores all downloaded files in the
     * 'temp'-dir (System.getProperty("java.io.tmpdir")).
     *
     * @throws IOException if the folder does not exist or the folder is not writable
     */
    public DownloadingAttachmentHandler() throws IOException {
        this(Paths.get(System.getProperty("java.io.tmpdir")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleAttachment(final Page page, final String attachmentFilename) {
        final Path destination = determineDestionationFile(page, attachmentFilename);

        final WebResponse webResponse = page.getWebResponse();
        try (InputStream contentAsStream = webResponse.getContentAsStream()) {
            FileUtils.copyToFile(contentAsStream, destination.toFile());
        }
        catch (final Exception e) {
            LOG.error("Failed to write attachment response content to '"
                            + destination.toAbsolutePath() + "'");
            return;
        }

        webResponse.cleanUp();
    }

    private Path determineDestionationFile(final Page page, final String attachmentFilename) {
        String fileName = attachmentFilename;

        if (StringUtils.isBlank(fileName)) {
            final String file = page.getWebResponse().getWebRequest().getUrl().getFile();
            fileName = file.substring(file.lastIndexOf('/') + 1);
        }

        if (StringUtils.isBlank(fileName)) {
            fileName = "download";
        }

        Path newPath = downloadFolder_.resolve(fileName);
        int count = 1;
        while (Files.exists(newPath, LinkOption.NOFOLLOW_LINKS)) {
            final String newFileName;
            final int pos = fileName.lastIndexOf('.');
            if (pos == -1) {
                newFileName = fileName + "(" + count + ")";
            }
            else {
                newFileName = fileName.substring(0, pos)
                                        + "(" + count + ")"
                                        + fileName.substring(pos);
            }

            newPath = downloadFolder_.resolve(newFileName);
            count++;
        }

        return newPath.toAbsolutePath();
    }
}
