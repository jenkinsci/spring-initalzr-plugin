package org.jenkinsci.plugins.springinitializr.rest;

public interface JsonParser {
    <T> T parse(String json, Class<T> guard);
}
