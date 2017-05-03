package org.jenkinsci.plugins.springinitializr;

import com.google.common.base.Optional;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.ProxyConfiguration;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrClient;
import org.jenkinsci.plugins.springinitializr.client.domain.ProjectSetup;
import org.jenkinsci.remoting.RoleChecker;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SpringBootProjectBuildStep extends Builder implements SimpleBuildStep {
    public static final Logger LOG = LoggerFactory.getLogger(SpringBootProjectBuildStep.class);
    public static final String STARTER = "starter";
    private Optional<String> selectedIDs = Optional.absent();
    private Optional<String> type = Optional.absent();
    private Optional<String> bootVersion = Optional.absent();
    private Optional<String> groupId = Optional.absent();
    private Optional<String> artifactId = Optional.absent();
    private Optional<String> packageName = Optional.absent();
    private Optional<String> description = Optional.absent();
    private Optional<String> packaging = Optional.absent();
    private Optional<String> javaVersion = Optional.absent();
    private Optional<String> language = Optional.absent();
    private Optional<String> autocomplete = Optional.absent();

    @DataBoundConstructor
    public SpringBootProjectBuildStep(String selectedIDs) {
        this.selectedIDs = Optional.fromNullable(selectedIDs);
    }

    public String getSelectedIDs() {
        return selectedIDs.orNull();
    }

    public String getPackageName() {
        return packageName.orNull();
    }

    public void setPackageName(String packageName) {
        this.packageName = Optional.of(packageName);
    }

    @DataBoundSetter
    public void setSelectedIDs(String selectedIDs) {
        this.selectedIDs = Optional.of(selectedIDs);
    }

    public String getType() {
        return type.orNull();
    }

    @DataBoundSetter
    public void setType(String type) {
        this.type = Optional.of(type);
    }

    public String getBootVersion() {
        return bootVersion.orNull();
    }

    @DataBoundSetter
    public void setBootVersion(String bootVersion) {
        this.bootVersion = Optional.of(bootVersion);
    }

    public String getGroupId() {
        return groupId.orNull();
    }

    @DataBoundSetter
    public void setGroupId(String groupId) {
        this.groupId = Optional.of(groupId);
    }

    public String getArtifactId() {
        return artifactId.orNull();
    }

    @DataBoundSetter
    public void setArtifactId(String artifactId) {
        this.artifactId = Optional.of(artifactId);
    }

    public String getDescription() {
        return description.orNull();
    }

    @DataBoundSetter
    public void setDescription(String description) {
        this.description = Optional.of(description);
    }

    public String getPackaging() {
        return packaging.orNull();
    }

    @DataBoundSetter
    public void setPackaging(String packaging) {
        this.packaging = Optional.of(packaging);
    }

    public String getJavaVersion() {
        return javaVersion.orNull();
    }

    @DataBoundSetter
    public void setJavaVersion(String javaVersion) {
        this.javaVersion = Optional.of(javaVersion);
    }

    public String getLanguage() {
        return language.orNull();
    }

    @DataBoundSetter
    public void setLanguage(String language) {
        this.language = Optional.of(language);
    }

    public String getAutocomplete() {
        return autocomplete.orNull();
    }

    @DataBoundSetter
    public void setAutocomplete(String autocomplete) {
        this.autocomplete = Optional.of(autocomplete);
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher, @Nonnull final TaskListener listener) throws InterruptedException, IOException {
        workspace.act(new FilePath.FileCallable<Void>() {
            @Override
            public Void invoke(File baseFile, VirtualChannel virtualChannel) throws IOException, InterruptedException {
                final ProjectSetup projectSetup = new ProjectSetup();
                projectSetup.setSelectedIDs(selectedIDs.or(""));
                projectSetup.setType(type.or("maven-project"));
                projectSetup.setBootVersion(bootVersion.or("1.5.3.RELEASE"));
                projectSetup.setGroupId(groupId());
                projectSetup.setArtifactId(artifactId());
                projectSetup.setDescription(description.or(""));
                projectSetup.setPackageName(packageName.or(groupId() + "." + artifactId()));
                projectSetup.setPackaging(packaging.or("jar"));
                projectSetup.setJavaVersion(javaVersion.or("1.8"));
                projectSetup.setLanguage(language.or("java"));
                projectSetup.setAutocomplete(autocomplete.or(""));
                SpringBootLibrariesListParameterDefinition.getSpringInitializrClient().extract(baseFile, projectSetup);
                return null;
            }

            private String artifactId() {
                return artifactId.or("demo");
            }

            private String groupId() {
                return groupId.or("ru.ts.psb.cloud");
            }

            @Override
            public void checkRoles(RoleChecker roleChecker) throws SecurityException {

            }
        });
    }

    @Symbol("springBoot")
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {
        @Override
        public String getDisplayName() {
            return "Generate spring boot application";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
