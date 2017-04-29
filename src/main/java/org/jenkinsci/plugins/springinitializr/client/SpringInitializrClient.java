package org.jenkinsci.plugins.springinitializr.client;

import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependency;

import java.util.List;

public interface SpringInitializrClient {
    List<SpringDependency> getAvailableDependencies(String springBootVersion);
}
