package org.jenkinsci.plugins.springinitializr.client.domain;

@lombok.Data
public class SpringDependency {
    private String name;
    private String description;
    private String weight;
    private String id;
    private String group;
}
