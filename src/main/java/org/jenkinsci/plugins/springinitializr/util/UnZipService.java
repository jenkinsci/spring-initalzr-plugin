package org.jenkinsci.plugins.springinitializr.util;

import java.io.File;
import java.util.zip.ZipInputStream;

public interface UnZipService {
    void unzip(File directory, ZipInputStream zipInputStream, String stripPrefix);
}
