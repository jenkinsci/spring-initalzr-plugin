package org.jenkinsci.plugins.springinitializr.client.domain;

import com.google.common.base.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ProjectSetup {
    private String name;
    private String selectedIDs;
    private String type;
    private String bootVersion;
    private String groupId;
    private String artifactId;
    private String packageName;
    private String description;
    private String packaging;
    private String javaVersion;
    private String language;
    private String autocomplete;
}
