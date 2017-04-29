package org.jenkinsci.plugins.springinitializr.rest;

import java.net.URI;

public interface LightRestTemplate {
    <T> T get(URI uri, Class<T> guard);
}
