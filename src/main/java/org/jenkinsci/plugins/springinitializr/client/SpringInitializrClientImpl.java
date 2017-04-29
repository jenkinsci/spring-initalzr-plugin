package org.jenkinsci.plugins.springinitializr.client;

import com.google.inject.Inject;
import org.jenkinsci.plugins.springinitializr.SpringBootLibrariesListParameterDefinition;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependency;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependencyResponse;
import org.jenkinsci.plugins.springinitializr.rest.LightRestTemplate;

import java.net.URI;
import java.util.List;

public class SpringInitializrClientImpl implements SpringInitializrClient {

    private LightRestTemplate lightRestTemplate;
    private SpringInitializrUrlProvider springInitializrUrlProvider;

    public void setLightRestTemplate(LightRestTemplate lightRestTemplate) {
        this.lightRestTemplate = lightRestTemplate;
    }

    public void setSpringInitializrUrlProvider(SpringInitializrUrlProvider springInitializrUrlProvider) {
        this.springInitializrUrlProvider = springInitializrUrlProvider;
    }

    @Override
    public List<SpringDependency> getAvailableDependencies(String springBootVersion) {
        final URI uri = springInitializrUrlProvider.getDependenciesUri(springBootVersion);
        return lightRestTemplate.get(uri, SpringDependencyResponse.class).getDependencies();
    }
}
