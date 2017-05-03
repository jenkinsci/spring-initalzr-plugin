package org.jenkinsci.plugins.springinitializr.client;

import hudson.Extension;
import org.jenkinsci.plugins.springinitializr.client.domain.ProjectSetup;
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
    public void starterZip() throws Exception {
        final ProjectSetup projectSetup = getProjectSetup();
        assertEquals(new URI(new StringBuilder("https://start.spring.io/starter.zip?")
                .append(param("name", "name")).append("&")
                .append(param("baseDir", "baseDir")).append("&")
                .append(param("type", "type")).append("&")
                .append(param("bootVersion", "bootVersion")).append("&")
                .append(param("groupId", "groupId")).append("&")
                .append(param("artifactId", "artifactId")).append("&")
                .append(param("description", "description")).append("&")
                .append(param("packageName", "packageName")).append("&")
                .append(param("packaging", "packaging")).append("&")
                .append(param("javaVersion", "java+Version")).append("&")
                .append(param("autocomplete", "autocomplete")).append("&")
                .append(param("style", "style1")).append("&")
                .append(param("style", "style2"))
                .toString()), springInitializrUrlProvider.getStarterZip("name", "baseDir", projectSetup));
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

    @Test
    public void exception2() throws Exception {
        final URISyntaxException exception = Mockito.mock(URISyntaxException.class);
        try {
            PowerMockito.whenNew(URI.class).withArguments(anyString()).thenThrow(exception);
            springInitializrUrlProvider.getStarterZip("starter", "starter", getProjectSetup());
            fail();
        } catch (RuntimeException e) {
            assertEquals(exception, e.getCause());
        }
    }

    private ProjectSetup getProjectSetup() {
        final ProjectSetup projectSetup = new ProjectSetup();
        projectSetup.setType("type");
        projectSetup.setBootVersion("bootVersion");
        projectSetup.setGroupId("groupId");
        projectSetup.setArtifactId("artifactId");
        projectSetup.setDescription("description");
        projectSetup.setPackageName("packageName");
        projectSetup.setPackaging("packaging");
        projectSetup.setJavaVersion("java Version");
        projectSetup.setAutocomplete("autocomplete");
        projectSetup.setSelectedIDs("style1 , style2");
        return projectSetup;
    }

    private String param(String name, String value) {
        return String.format("%s=%s", name, value);
    }
}