package org.jenkinsci.plugins.springinitializr.rest;

import com.google.gson.Gson;
import hudson.Extension;

public class JsonParserImpl implements JsonParser{

    private final Gson gson = new Gson();

    @Override
    public <T> T parse(String json, Class<T> guard) {
        return gson.fromJson(json, guard);
    }
}
