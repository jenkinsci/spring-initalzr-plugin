package org.jenkinsci.plugins.springinitializr.client;

import com.google.inject.Inject;
import hudson.ProxyConfiguration;
import org.jenkinsci.plugins.springinitializr.SpringBootLibrariesListParameterDefinition;
import org.jenkinsci.plugins.springinitializr.client.domain.ProjectSetup;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependency;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependencyResponse;
import org.jenkinsci.plugins.springinitializr.rest.LightRestTemplate;
import org.jenkinsci.plugins.springinitializr.util.UnZipService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.zip.ZipInputStream;

public class SpringInitializrClientImpl implements SpringInitializrClient {

    public static final String STARTER = "starter";
    private LightRestTemplate lightRestTemplate;
    private SpringInitializrUrlProvider springInitializrUrlProvider;
    private UnZipService unZipService;

    public void setLightRestTemplate(LightRestTemplate lightRestTemplate) {
        this.lightRestTemplate = lightRestTemplate;
    }

    public void setSpringInitializrUrlProvider(SpringInitializrUrlProvider springInitializrUrlProvider) {
        this.springInitializrUrlProvider = springInitializrUrlProvider;
    }

    public void setUnZipService(UnZipService unZipService) {
        this.unZipService = unZipService;
    }

    @Override
    public List<SpringDependency> getAvailableDependencies(String springBootVersion) {
        final URI uri = springInitializrUrlProvider.getDependenciesUri(springBootVersion);
        return lightRestTemplate.get(uri, SpringDependencyResponse.class).getDependencies();
    }

    @Override
    public void extract(File toDirecttory, ProjectSetup projectSetup) {
        try (ZipInputStream zipInputStream = new ZipInputStream(ProxyConfiguration.getInputStream(springInitializrUrlProvider.getStarterZip(STARTER, STARTER, projectSetup).toURL()))){
            unZipService.unzip(toDirecttory, zipInputStream, STARTER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
