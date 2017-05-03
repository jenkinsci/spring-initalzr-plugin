package org.jenkinsci.plugins.springinitializr.client.domain;

import com.google.code.beanmatchers.BeanMatchers;
import org.hamcrest.core.AllOf;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsMatchers;

import static org.junit.Assert.*;

public class ProjectSetupTest {
    @Test
    public void TEST() throws Exception {
        assertThat(ProjectSetup.class, AllOf.allOf(
                BeanMatchers.hasValidGettersAndSetters(),
                BeanMatchers.hasValidBeanConstructor(),
                JenkinsMatchers.hasImplementedEquals(),
                JenkinsMatchers.hasImplementedHashCode()
        ));
    }
}