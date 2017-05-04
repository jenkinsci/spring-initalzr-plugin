package org.jenkinsci.plugins.springinitializr.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZipServiceImpl implements UnZipService {
    @Override
    public void unzip(File directory, ZipInputStream zipInputStream, String stripPrefix) {
        try {
            ZipEntry nextEntry;
            int read;
            byte[] buffer = new byte[1024];
            while ((nextEntry = zipInputStream.getNextEntry()) != null) {
                if (nextEntry.isDirectory())
                    continue;
                final String entryName = nextEntry.getName().substring(stripPrefix.length() + 1);
                final File file = new File(directory, entryName).getAbsoluteFile();
                final boolean result = file.getParentFile().mkdirs();
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    while ((read = zipInputStream.read(buffer)) > 0) outputStream.write(buffer, 0, read);
                }
                zipInputStream.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
