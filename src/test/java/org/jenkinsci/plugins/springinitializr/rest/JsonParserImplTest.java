package org.jenkinsci.plugins.springinitializr.rest;

import hudson.Extension;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class JsonParserImplTest {
    @Test
    public void name() throws Exception {
        assertEquals("HELLO", new JsonParserImpl().parse("\"HELLO\"", String.class));
    }
}