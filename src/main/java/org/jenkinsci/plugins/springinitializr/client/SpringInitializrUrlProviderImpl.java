package org.jenkinsci.plugins.springinitializr.client;

import hudson.Extension;
import org.jenkinsci.plugins.springinitializr.client.domain.ProjectSetup;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SpringInitializrUrlProviderImpl implements SpringInitializrUrlProvider {
    @Override
    public URI getDependenciesUri(String springBootVersion) {
        try {
            return new URI(String.format("http://start.spring.io/ui/dependencies.json?version=%s", springBootVersion));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public URI getStarterZip(String name, String directory, ProjectSetup projectSetup) {
        try {
            return new URI("https://start.spring.io/starter.zip?" +
                    param("name", name) + "&" +
                    param("baseDir", directory) + "&" +
                    param("type", projectSetup.getType()) + "&" +
                    param("bootVersion", projectSetup.getBootVersion()) + "&" +
                    param("groupId", projectSetup.getGroupId()) + "&" +
                    param("artifactId", projectSetup.getArtifactId()) + "&" +
                    param("description", projectSetup.getDescription()) + "&" +
                    param("packageName", projectSetup.getPackageName()) + "&" +
                    param("packaging", projectSetup.getPackaging()) + "&" +
                    param("javaVersion", projectSetup.getJavaVersion()) + "&" +
                    param("autocomplete", projectSetup.getAutocomplete()) + "&" +
                    param("style", projectSetup.getSelectedIDs().split(",")));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private String param(String key, String... value) {
        final StringBuilder builder = new StringBuilder();
        for (String v : value) {
            try {
                builder.append(key).append("=").append(URLEncoder.encode(v.trim(), StandardCharsets.UTF_8.name())).append("&");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

}
