package org.jenkinsci.plugins.springinitializr.rest;

import hudson.Extension;
import hudson.ProxyConfiguration;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IOUtils.class, ProxyConfiguration.class})
public class LightRestTemplateImplTest {
    @InjectMocks
    private LightRestTemplate lightRestTemplate = new LightRestTemplateImpl();
    @Mock
    private JsonParser jsonParser;

    @Test
    public void works() throws Exception {
        final URLConnection urlConnection = mock(URLConnection.class);
        final Runnable expected = mock(Runnable.class);

        mockStatic(IOUtils.class, ProxyConfiguration.class);

        when(ProxyConfiguration.open(new URL("http://127.0.0.1"))).thenReturn(urlConnection);
        when(IOUtils.toByteArray(urlConnection)).thenReturn("TEST".getBytes(StandardCharsets.UTF_8));
        when(jsonParser.parse("TEST", Runnable.class)).thenReturn(expected);

        assertEquals(expected, lightRestTemplate.get(new URI("http://127.0.0.1"), Runnable.class));

        verifyStatic();

        IOUtils.close(urlConnection);
    }

    @Test
    public void exception() throws Exception {
        mockStatic(ProxyConfiguration.class);
        final IOException ioException = new IOException();
        when(ProxyConfiguration.open(any(URL.class))).thenThrow(ioException);
        try {
            lightRestTemplate.get(new URI("http://127.0.0.1"), Runnable.class);
            fail();
        } catch (RuntimeException e) {
            assertEquals(ioException, e.getCause());
        }
    }
}