package org.jenkinsci.plugins.springinitializr.client;

import hudson.Extension;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependency;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependencyResponse;
import org.jenkinsci.plugins.springinitializr.rest.LightRestTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpringInitializrClientImplTest {
    @InjectMocks
    private SpringInitializrClient springInitializrClient = new SpringInitializrClientImpl();
    @Mock
    private LightRestTemplate lightRestTemplate;
    @Mock
    private SpringInitializrUrlProvider springInitializrUrlProvider;

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
}