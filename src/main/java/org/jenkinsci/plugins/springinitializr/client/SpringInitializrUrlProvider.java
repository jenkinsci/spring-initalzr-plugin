package org.jenkinsci.plugins.springinitializr.client;

import org.jenkinsci.plugins.springinitializr.client.domain.ProjectSetup;

import java.net.URI;

public interface SpringInitializrUrlProvider {
    URI getDependenciesUri(String springBootVersion);

    URI getStarterZip(String name, String directory, ProjectSetup projectSetup);
}
