package org.jenkinsci.plugins.springinitializr.client.domain;

import java.util.List;

public class SpringDependencyResponse {
    private List<SpringDependency> dependencies;

    public List<SpringDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<SpringDependency> dependencies) {
        this.dependencies = dependencies;
    }
}
