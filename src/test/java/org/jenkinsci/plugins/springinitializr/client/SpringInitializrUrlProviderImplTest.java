package org.jenkinsci.plugins.springinitializr.client;

import hudson.Extension;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SpringInitializrUrlProviderImpl.class})
public class SpringInitializrUrlProviderImplTest {
    @InjectMocks
    private SpringInitializrUrlProvider springInitializrUrlProvider = new SpringInitializrUrlProviderImpl();

    @Test
    public void getDependencyURI() throws Exception {
        assertEquals(new URI("http://start.spring.io/ui/dependencies.json?version=VERS"), springInitializrUrlProvider.getDependenciesUri("VERS"));
    }

    @Test
    public void exception() throws Exception {
        final URISyntaxException exception = Mockito.mock(URISyntaxException.class);
        try {
            PowerMockito.whenNew(URI.class).withArguments(anyString()).thenThrow(exception);
            springInitializrUrlProvider.getDependenciesUri("VERSION");
            fail();
        } catch (RuntimeException e) {
            assertEquals(exception, e.getCause());
        }
    }
}