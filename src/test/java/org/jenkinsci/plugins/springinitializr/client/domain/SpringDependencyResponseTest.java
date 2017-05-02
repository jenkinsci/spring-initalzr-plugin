package org.jenkinsci.plugins.springinitializr.client.domain;

import org.hamcrest.core.AllOf;
import org.junit.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.junit.Assert.*;

public class SpringDependencyResponseTest {
    @Test
    public void name() throws Exception {
        assertThat(SpringDependencyResponse.class, AllOf.allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters()
        ));

    }
}