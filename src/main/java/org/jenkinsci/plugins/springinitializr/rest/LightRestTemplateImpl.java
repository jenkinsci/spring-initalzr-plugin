package org.jenkinsci.plugins.springinitializr.rest;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.ProxyConfiguration;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class LightRestTemplateImpl implements LightRestTemplate {
    private JsonParser jsonParser;

    public void setJsonParser(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    @Override
    public <T> T get(URI uri, Class<T> guard) {
        URLConnection urlConnection = null;
        try {
            urlConnection = ProxyConfiguration.open(uri.toURL());
            final String json = new String(IOUtils.toByteArray(urlConnection), StandardCharsets.UTF_8);
            return jsonParser.parse(json, guard);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (urlConnection != null) {
                IOUtils.close(urlConnection);
            }
        }
    }
}
