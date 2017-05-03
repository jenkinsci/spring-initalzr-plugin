package org.jenkinsci.plugins.springinitializr.util;

import org.codehaus.plexus.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.*;

public class UnZipServiceImplTest {
    private UnZipService unZipService;
    private File tempDir;

    @Before
    public void setUp() throws Exception {
        unZipService = new UnZipServiceImpl();
        tempDir = Files.createTempDirectory(UnZipServiceImplTest.class.getName()).toFile();
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(tempDir);
    }

    @Test
    public void unzip() throws Exception {
        try (InputStream inputStream = UnZipServiceImplTest.class.getResourceAsStream("/test.zip")) {
            unZipService.unzip(tempDir, new ZipInputStream(inputStream), "test");
        }
        final File[] files = tempDir.listFiles();
        assertEquals(2, files.length);
        for (File file : files) {
            if ("1".equals(file.getName())) {
                assertEquals("1", new String(Files.readAllBytes(file.toPath())));
            }
            else if ("2".equals(file.getName())) {
                assertEquals("2", new String(Files.readAllBytes(file.toPath())));
            }
            else {
                fail(file.getAbsolutePath() + " \"" + file.getName() + "\"");
            }
        }
    }
}