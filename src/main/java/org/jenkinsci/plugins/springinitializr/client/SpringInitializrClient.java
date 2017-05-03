package org.jenkinsci.plugins.springinitializr.client;

import org.jenkinsci.plugins.springinitializr.client.domain.ProjectSetup;
import org.jenkinsci.plugins.springinitializr.client.domain.SpringDependency;

import java.io.File;
import java.util.List;

public interface SpringInitializrClient {
    List<SpringDependency> getAvailableDependencies(String springBootVersion);

    void extract(File toDirecttory, ProjectSetup projectSetup);
}
