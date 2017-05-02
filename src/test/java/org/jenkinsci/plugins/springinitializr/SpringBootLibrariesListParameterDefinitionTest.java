package org.jenkinsci.plugins.springinitializr;

import hudson.Extension;
import hudson.model.ParameterValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrClient;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrClientImpl;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrUrlProviderImpl;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependency;
import org.jenkinsci.plugins.springinitializr.rest.JsonParserImpl;
import org.jenkinsci.plugins.springinitializr.rest.LightRestTemplateImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.stapler.StaplerRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.jenkinsci.plugins.springinitializr.SpringBootLibrariesListParameterDefinition.picoContainer;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SpringBootLibrariesListParameterDefinition.class, JSONObject.class, JSONArray.class})
public class SpringBootLibrariesListParameterDefinitionTest {
    @InjectMocks
    private SpringBootLibrariesListParameterDefinition springBootLibrariesListParameterDefinition = new SpringBootLibrariesListParameterDefinition();
    @Mock
    private SpringInitializrClient springInitializrClient;


    public void setUp() throws Exception {
        PowerMockito.mockStatic(SpringBootLibrariesListParameterDefinition.class);
        when(SpringBootLibrariesListParameterDefinition.getSpringInitializrClient()).thenReturn(springInitializrClient);
    }

    @Test
    public void annotationsAndDefaultValues() throws Exception {
        setUp();
        assertNotNull(SpringBootLibrariesListParameterDefinition.SpringBootLibrariesListParameter.class.getAnnotation(Extension.class));
        assertEquals("spring-boot-libraries", springBootLibrariesListParameterDefinition.getName());
        assertEquals("List of spring boot libraries to use in created micro service", springBootLibrariesListParameterDefinition.getDescription());
        assertEquals("Spring boot libraries", new SpringBootLibrariesListParameterDefinition.SpringBootLibrariesListParameter().getDisplayName());
    }

    @Test(expected = RuntimeException.class)
    public void notSupported() throws Exception {
        setUp();
        springBootLibrariesListParameterDefinition.createValue(mock(StaplerRequest.class));
    }

    @Test
    public void getLibs() throws Exception {
        setUp();
        final List<SpringDependency> expected = mock(List.class);
        when(springInitializrClient.getAvailableDependencies("1.5.3.RELEASE")).thenReturn(expected);
        assertEquals(expected, springBootLibrariesListParameterDefinition.getLibs());
    }

    @Test
    public void createSelectedSpringBootParameter() throws Exception {
        setUp();
        final StaplerRequest staplerRequest = mock(StaplerRequest.class);
        final JSONObject jsonObject = mock(JSONObject.class);
        final JSONArray jsonArray = mock(JSONArray.class);
        when(jsonObject.getJSONArray("value")).thenReturn(jsonArray);
        when(jsonArray.join(",", true)).thenReturn("lib-ids");
        final ParameterValue value = springBootLibrariesListParameterDefinition.createValue(staplerRequest, jsonObject);
        assertEquals("selectedSpringDependencies", value.getName());
        assertEquals("lib-ids", value.getValue());
    }

    @Test
    public void testPicoContainer() throws Exception {
        setPicoContainer(mock(MutablePicoContainer.class));
        SpringBootLibrariesListParameterDefinition.init();
        verify(picoContainer).addComponent(SpringInitializrClientImpl.class);
        verify(picoContainer).addComponent(SpringInitializrUrlProviderImpl.class);
        verify(picoContainer).addComponent(JsonParserImpl.class);
        verify(picoContainer).addComponent(LightRestTemplateImpl.class);
        verify(picoContainer).start();
    }
    @Test
    public void testGetClient() throws Exception {
        setPicoContainer(mock(MutablePicoContainer.class));
        final SpringInitializrClient expected = mock(SpringInitializrClient.class);
        when(picoContainer.getComponent(SpringInitializrClient.class)).thenReturn(expected);
        final SpringInitializrClient actual = SpringBootLibrariesListParameterDefinition.getSpringInitializrClient();
        assertEquals(expected, actual);
    }

    private static void setPicoContainer(MutablePicoContainer picoContainer) throws Exception {
        Field field = SpringBootLibrariesListParameterDefinition.class.getDeclaredField("picoContainer");
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, picoContainer);
    }

}