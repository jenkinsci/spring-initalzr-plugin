package org.jenkinsci.plugins.springinitializr;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrClient;
import org.jenkinsci.plugins.springinitializr.client.domain.ProjectSetup;
import org.jenkinsci.remoting.RoleChecker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FilePath.class, SpringBootLibrariesListParameterDefinition.class})
public class SpringBootProjectBuildStepTest {
    @InjectMocks
    private SpringBootProjectBuildStep springBootProjectBuildStep = new SpringBootProjectBuildStep(null);
    @Mock
    private SpringInitializrClient springInitializrClient;

    private Run run;
    private FilePath filePath;
    private Launcher launcher;
    private TaskListener taskListener;

    @Before
    public void setUp() throws Exception {
        run = mock(Run.class);
        filePath = mock(FilePath.class);
        launcher = mock(Launcher.class);
        taskListener = mock(TaskListener.class);
        PowerMockito.mockStatic(SpringBootLibrariesListParameterDefinition.class);
        when(SpringBootLibrariesListParameterDefinition.getSpringInitializrClient()).thenReturn(springInitializrClient);
    }

    @Test
    public void descriptionName() throws Exception {
        assertEquals("Generate spring boot application", new SpringBootProjectBuildStep.DescriptorImpl().getDisplayName());
    }

    @Test
    public void functionName() throws Exception {
        assertArrayEquals(new String[]{"springBoot"}, SpringBootProjectBuildStep.DescriptorImpl.class.getAnnotation(Symbol.class).value());
    }

    @Test
    public void defaultValues() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        doTest(projectSetup);
    }

    @Test
    public void selectedIDs() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setSelectedIDs("IDS");
        springBootProjectBuildStep.setSelectedIDs("IDS");
        doTest(projectSetup);
    }
    @Test
    public void type() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setType("type");
        springBootProjectBuildStep.setType("type");
        doTest(projectSetup);
    }
    @Test
    public void bootVersion() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setBootVersion("bootVersion");
        springBootProjectBuildStep.setBootVersion("bootVersion");
        doTest(projectSetup);
    }
    @Test
    public void groupId() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setGroupId("groupId");
        projectSetup.setPackageName("groupId.demo");
        springBootProjectBuildStep.setGroupId("groupId");
        doTest(projectSetup);
    }
    @Test
    public void artifactId() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setArtifactId("artifactId");
        projectSetup.setPackageName("ru.ts.psb.cloud.artifactId");
        springBootProjectBuildStep.setArtifactId("artifactId");
        doTest(projectSetup);
    }
    @Test
    public void packageName() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setPackageName("packageName");
        springBootProjectBuildStep.setPackageName("packageName");
        doTest(projectSetup);
    }
    @Test
    public void description() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setDescription("description");
        springBootProjectBuildStep.setDescription("description");
        doTest(projectSetup);
    }
    @Test
    public void packaging() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setPackaging("packaging");
        springBootProjectBuildStep.setPackaging("packaging");
        doTest(projectSetup);
    }

    @Test
    public void javaVersion() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setJavaVersion("javaVersion");
        springBootProjectBuildStep.setJavaVersion("javaVersion");
        doTest(projectSetup);
    }
    @Test
    public void language() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setLanguage("language");
        springBootProjectBuildStep.setLanguage("language");
        doTest(projectSetup);
    }
    @Test
    public void autocomplete() throws Exception {
        final ProjectSetup projectSetup = defaultProjectSetup();
        projectSetup.setAutocomplete("autocomplete");
        springBootProjectBuildStep.setAutocomplete("autocomplete");
        doTest(projectSetup);
    }

    private ProjectSetup defaultProjectSetup() {
        final ProjectSetup projectSetup = new ProjectSetup();
        projectSetup.setSelectedIDs("");
        projectSetup.setType("maven-project");
        projectSetup.setBootVersion("1.5.3.RELEASE");
        projectSetup.setGroupId("ru.ts.psb.cloud");
        projectSetup.setArtifactId("demo");
        projectSetup.setPackageName("ru.ts.psb.cloud.demo");
        projectSetup.setDescription("");
        projectSetup.setPackaging("jar");
        projectSetup.setJavaVersion("1.8");
        projectSetup.setLanguage("java");
        projectSetup.setAutocomplete("");
        return projectSetup;
    }

    private void doTest(ProjectSetup projectSetup) throws java.io.IOException, InterruptedException {
        final File file = mock(File.class);
        when(filePath.act(any(FilePath.FileCallable.class))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                final FilePath.FileCallable fileCallable = invocation.getArgumentAt(0, FilePath.FileCallable.class);
                fileCallable.checkRoles(mock(RoleChecker.class ));
                fileCallable.invoke(file, mock(VirtualChannel.class));
                return null;
            }
        });
        springBootProjectBuildStep.perform(run, filePath, launcher, taskListener);
        verify(springInitializrClient).extract(file, projectSetup);
    }
}