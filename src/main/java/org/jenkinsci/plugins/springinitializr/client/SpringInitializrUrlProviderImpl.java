package org.jenkinsci.plugins.springinitializr.client;

import hudson.Extension;

import java.net.URI;
import java.net.URISyntaxException;

public class SpringInitializrUrlProviderImpl implements SpringInitializrUrlProvider {
    @Override
    public URI getDependenciesUri(String springBootVersion) {
        try {
            return new URI(String.format("http://start.spring.io/ui/dependencies.json?version=%s", springBootVersion));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
