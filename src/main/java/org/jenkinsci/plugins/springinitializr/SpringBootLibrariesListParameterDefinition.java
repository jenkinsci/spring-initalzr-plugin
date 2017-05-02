package org.jenkinsci.plugins.springinitializr;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;
import hudson.model.StringParameterValue;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrClient;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrClientImpl;
import org.jenkinsci.plugins.springinitializr.client.SpringInitializrUrlProviderImpl;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependency;
import org.jenkinsci.plugins.springinitializr.rest.JsonParserImpl;
import org.jenkinsci.plugins.springinitializr.rest.LightRestTemplateImpl;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Extension
public class SpringBootLibrariesListParameterDefinition extends ParameterDefinition {
    public static final Logger LOG = LoggerFactory.getLogger(SpringBootLibrariesListParameterDefinition.class);
    protected final static MutablePicoContainer picoContainer = new PicoBuilder().withSetterInjection().build();
    private final String springBootVersion = "1.5.3.RELEASE";

    @DataBoundConstructor
    public SpringBootLibrariesListParameterDefinition() {
        super("spring-boot-libraries", "List of spring boot libraries to use in created micro service");
    }

    @Initializer(after = InitMilestone.PLUGINS_STARTED  )
    public static void init() {
        picoContainer.addComponent(SpringInitializrClientImpl.class);
        picoContainer.addComponent(SpringInitializrUrlProviderImpl.class);
        picoContainer.addComponent(JsonParserImpl.class);
        picoContainer.addComponent(LightRestTemplateImpl.class);
        picoContainer.start();
    }

    public static SpringInitializrClient getSpringInitializrClient() {
        return picoContainer.getComponent(SpringInitializrClient.class);
    }

    public List<SpringDependency> getLibs() throws Exception {
        return getSpringInitializrClient().getAvailableDependencies(springBootVersion);
    }

    @Override
    public ParameterValue createValue(StaplerRequest staplerRequest, JSONObject jsonObject) {
        return new StringParameterValue("selectedSpringDependencies", jsonObject.getJSONArray("value").join(",", true));
    }

    @Extension
    public static class SpringBootLibrariesListParameter extends ParameterDescriptor {

        @Override
        public String getDisplayName() {
            return "Spring boot libraries";
        }
    }

    // Not used
    @Override
    public ParameterValue createValue(StaplerRequest staplerRequest) {
        throw new RuntimeException("You should supply json value");
    }
}
