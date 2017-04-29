package org.jenkinsci.plugins.springinitializr.client;

import java.net.URI;

public interface SpringInitializrUrlProvider {
    URI getDependenciesUri(String springBootVersion);
}
