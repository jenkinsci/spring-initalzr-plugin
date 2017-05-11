package org.jenkinsci.plugins.springinitializr;

import com.google.common.base.Joiner;
import hudson.Extension;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.*;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrClient;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrClientImpl;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrUrlProviderImpl;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependency;
import org.jenkinsci.plugins.springinitializr.rest.JsonParserImpl;
import org.jenkinsci.plugins.springinitializr.rest.LightRestTemplateImpl;
import org.jenkinsci.plugins.springinitializr.util.UnZipServiceImpl;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;


public class SpringBootLibrariesListParameterDefinition extends StringParameterDefinition {
    public static final Logger LOG = LoggerFactory.getLogger(SpringBootLibrariesListParameterDefinition.class);
    final static MutablePicoContainer picoContainer = new PicoBuilder().withSetterInjection().build();
    private final String springBootVersion;

    @DataBoundConstructor
    public SpringBootLibrariesListParameterDefinition(String springBootVersion) {
        super("selectedSpringDependencies", "","List of spring boot libraries to use in created micro service");
        this.springBootVersion = springBootVersion;
    }

    @Initializer(after = InitMilestone.PLUGINS_STARTED  )
    public static void init() {
        picoContainer.addComponent(SpringInitializrClientImpl.class);
        picoContainer.addComponent(SpringInitializrUrlProviderImpl.class);
        picoContainer.addComponent(JsonParserImpl.class);
        picoContainer.addComponent(LightRestTemplateImpl.class);
        picoContainer.addComponent(UnZipServiceImpl.class);
        picoContainer.start();
    }

    public static SpringInitializrClient getSpringInitializrClient() {
        return picoContainer.getComponent(SpringInitializrClient.class);
    }

    public List<SpringDependency> getLibs() throws Exception {
        return getSpringInitializrClient().getAvailableDependencies(springBootVersion);
    }

    public String getGroups() throws Exception {
        final HashSet<String> groups = new HashSet<>();
        for (SpringDependency springDependency : getSpringInitializrClient().getAvailableDependencies(springBootVersion)) {
            groups.add(springDependency.getGroup());
        }
        return Joiner.on(", ").join(groups);
    }

    @Override
    public StringParameterValue createValue(StaplerRequest staplerRequest, JSONObject jsonObject) {
        final StringParameterValue stringParameterValue = staplerRequest.bindJSON(StringParameterValue.class, jsonObject);
        stringParameterValue.setDescription(getDescription());
        return stringParameterValue;
    }

    @Extension
    public static class SpringBootLibrariesListParameter extends ParameterDescriptor {
        @Override
        public String getDisplayName() {
            return "Spring boot libraries";
        }
    }
}
