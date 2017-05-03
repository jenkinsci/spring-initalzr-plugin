package org.jenkinsci.plugins.springinitializr.client;

import hudson.ProxyConfiguration;
import org.jenkinsci.plugins.springinitializr.client.domain.ProjectSetup;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependency;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependencyResponse;
import org.jenkinsci.plugins.springinitializr.rest.LightRestTemplate;
import org.jenkinsci.plugins.springinitializr.util.UnZipService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipInputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SpringInitializrClientImpl.class, ProxyConfiguration.class})
public class SpringInitializrClientImplTest {
    @InjectMocks
    private SpringInitializrClient springInitializrClient = new SpringInitializrClientImpl();
    @Mock
    private LightRestTemplate lightRestTemplate;
    @Mock
    private SpringInitializrUrlProvider springInitializrUrlProvider;
    @Mock
    private UnZipService unZipService;
    private File directory;
    private ProjectSetup projectSetup;
    private InputStream inputStream;
    private ZipInputStream zipInputStream;

    @Before
    public void setUp() throws Exception {
        directory = mock(File.class);
        projectSetup = mock(ProjectSetup.class);
        zipInputStream = mock(ZipInputStream.class);
        inputStream = mock(InputStream.class);
        mockStatic(ProxyConfiguration.class);
        whenNew(ZipInputStream.class).withArguments(inputStream).thenReturn(zipInputStream);
        when(ProxyConfiguration.getInputStream(new URL("http://localhost/xxx"))).thenReturn(inputStream);
    }

    @Test
    public void works() throws Exception {
        final List<SpringDependency> expected = mock(List.class);
        final URI uri = new URI("a:b");
        final SpringDependencyResponse springDependencyResponse = mock(SpringDependencyResponse.class);
        when(springDependencyResponse.getDependencies()).thenReturn(expected);
        when(springInitializrUrlProvider.getDependenciesUri("VERSION")).thenReturn(uri);
        when(lightRestTemplate.get(uri, SpringDependencyResponse.class)).thenReturn(springDependencyResponse);
        Assert.assertEquals(expected, springInitializrClient.getAvailableDependencies("VERSION"));
    }

    @Test
    public void extract() throws Exception {
        when(springInitializrUrlProvider.getStarterZip("starter", "starter", projectSetup)).thenReturn(new URI("http://localhost/xxx"));
        springInitializrClient.extract(directory, projectSetup);
        verify(unZipService).unzip(directory, zipInputStream, "starter");
        verify(zipInputStream).close();
    }
}