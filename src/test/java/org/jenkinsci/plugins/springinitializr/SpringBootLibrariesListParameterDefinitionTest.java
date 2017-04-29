package org.jenkinsci.plugins.springinitializr;

import hudson.Extension;
import hudson.model.ParameterValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrClient;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.stapler.StaplerRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SpringBootLibrariesListParameterDefinition.class, JSONObject.class, JSONArray.class})
public class SpringBootLibrariesListParameterDefinitionTest {
    @InjectMocks
    private SpringBootLibrariesListParameterDefinition springBootLibrariesListParameterDefinition = new SpringBootLibrariesListParameterDefinition();
    @Mock
    private SpringInitializrClient springInitializrClient;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(SpringBootLibrariesListParameterDefinition.class);
        when(SpringBootLibrariesListParameterDefinition.getSpringInitializrClient()).thenReturn(springInitializrClient);
    }

    @Test
    public void annotationsAndDefaultValues() throws Exception {
        assertNotNull(SpringBootLibrariesListParameterDefinition.SpringBootLibrariesListParameter.class.getAnnotation(Extension.class));
        assertEquals("spring-boot-libraries", springBootLibrariesListParameterDefinition.getName());
        assertEquals("List of spring boot libraries to use in created micro service", springBootLibrariesListParameterDefinition.getDescription());
        assertEquals("Spring boot libraries", new SpringBootLibrariesListParameterDefinition.SpringBootLibrariesListParameter().getDisplayName());
    }

    @Test(expected = RuntimeException.class)
    public void notSupported() throws Exception {
        springBootLibrariesListParameterDefinition.createValue(mock(StaplerRequest.class));
    }

    @Test
    public void getLibs() throws Exception {
        final List<SpringDependency> expected = mock(List.class);
        when(springInitializrClient.getAvailableDependencies("1.5.3.RELEASE")).thenReturn(expected);
        assertEquals(expected, springBootLibrariesListParameterDefinition.getLibs());
    }

    @Test
    public void createSelectedSpringBootParameter() throws Exception {
        final StaplerRequest staplerRequest = mock(StaplerRequest.class);
        final JSONObject jsonObject = mock(JSONObject.class);
        final JSONArray jsonArray = mock(JSONArray.class);
        when(jsonObject.getJSONArray("value")).thenReturn(jsonArray);
        when(jsonArray.join(",", true)).thenReturn("lib-ids");
        final ParameterValue value = springBootLibrariesListParameterDefinition.createValue(staplerRequest, jsonObject);
        assertEquals("selectedSpringDependencies", value.getName());
        assertEquals("lib-ids", value.getValue());

    }
}