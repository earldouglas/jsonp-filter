package com.earldouglas.jsonpfilter.test;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class JsonPFilterTest extends ServerRunner {

    @Test public void testJsonPFilter() throws Exception {
        assertNoJsonP("/0");
        assertNoJsonP("/1");
        assertNoJsonP("/2");
        assertNoJsonP("/3");

        assertNoJsonP("/0?callback=baz");
        assertCallback("/1?callback=baz", "baz");
        assertNoJsonP("/2?callback=baz");
        assertNoJsonP("/3?callback=baz");

        assertNoJsonP("/0?c2theb=baz");
        assertNoJsonP("/1?c2theb=baz");
        assertCallback("/2?c2theb=baz", "baz");
        assertNoJsonP("/3?c2theb=baz");

        assertNoJsonP("/0?thisvaluehaswhitespace=baz");
        assertNoJsonP("/1?thisvaluehaswhitespace=baz");
        assertNoJsonP("/2?thisvaluehaswhitespace=baz");
        assertCallback("/3?thisvaluehaswhitespace=baz", "baz");
    }

    @Test public void testJsonPFilterWithVariable() throws Exception {
        assertNoJsonP("/4");
        assertNoJsonP("/5");
        assertNoJsonP("/6");

        assertVariable("/4?variable=baz", "baz");
        assertNoJsonP("/5?variable=baz");
        assertNoJsonP("/6?variable=baz");

        assertNoJsonP("/4?custom=baz");
        assertVariable("/5?custom=baz", "baz");
        assertNoJsonP("/6?custom=baz");

        assertNoJsonP("/4?thisvaluehaswhitespace=baz");
        assertNoJsonP("/5?thisvaluehaswhitespace=baz");
        assertVariable("/6?thisvaluehaswhitespace=baz", "baz");
    }

    @Test public void testJsonPFilterWithVariableAndCallback() throws Exception {
        assertNoJsonP("/1");
        assertCallback("/1?callback=baz", "baz");
        assertVariable("/1?variable=baz", "baz");
        assertCallback("/1?callback=baz&variable=baz", "baz");
    }

    private void assertNoJsonP(String urlS) throws Exception {
        assertEquals("{ \"foo\": \"bar\" }\n", get(urlS));

    }

    private void assertCallback(String urlS, String callback) throws Exception {
        assertEquals(callback + "({ \"foo\": \"bar\" })\n", get(urlS));
    }

    private void assertVariable(String urlS, String variable) throws Exception {
        assertEquals(variable + " = { \"foo\": \"bar\" };\n", get(urlS));
    }

    private String get(String urlS) throws Exception {
        URL url = new URL("http://localhost:" + PORT + "/jsonp-filter" + urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(5000);
        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
