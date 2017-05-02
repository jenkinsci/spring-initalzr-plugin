package org.jenkinsci.plugins.springinitializr.client.domain;

import java.util.List;

@lombok.Data
public class SpringDependencyResponse {
    private List<SpringDependency> dependencies;
}
