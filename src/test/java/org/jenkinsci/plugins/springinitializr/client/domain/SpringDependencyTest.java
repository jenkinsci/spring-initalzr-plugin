package org.jenkinsci.plugins.springinitializr.client.domain;

import org.hamcrest.core.AllOf;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsMatchers;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.junit.Assert.*;
import static org.jvnet.hudson.test.JenkinsMatchers.hasDefaultConstructor;

public class SpringDependencyTest {

    @Test
    public void name() throws Exception {
        assertThat(SpringDependency.class, AllOf.allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters()
        ));
    }
}