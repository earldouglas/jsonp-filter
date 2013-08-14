package com.earldouglas.jsonpfilter.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class JsonPFilterTest extends ServerRunner {

    @Test public void testJsonPFilter() throws Exception {
        assertNoCallbk("/0");
        assertNoCallbk("/1");
        assertNoCallbk("/2");
        assertNoCallbk("/3");

        assertNoCallbk("/0?callback=baz");
        assertCallback("/1?callback=baz", "baz");
        assertNoCallbk("/2?callback=baz");
        assertNoCallbk("/3?callback=baz");

        assertNoCallbk("/0?c2theb=baz");
        assertNoCallbk("/1?c2theb=baz");
        assertCallback("/2?c2theb=baz", "baz");
        assertNoCallbk("/3?c2theb=baz");

        assertNoCallbk("/0?thisvaluehaswhitespace=baz");
        assertNoCallbk("/1?thisvaluehaswhitespace=baz");
        assertNoCallbk("/2?thisvaluehaswhitespace=baz");
        assertCallback("/3?thisvaluehaswhitespace=baz", "baz");
    }

    private void assertNoCallbk(String urlS) throws Exception {
        assertEquals("{ \"foo\": \"bar\" }\n", get(urlS));

    }

    private void assertCallback(String urlS, String callback) throws Exception {
        assertEquals(callback + "({ \"foo\": \"bar\" })\n", get(urlS));
    }

    private String get(String urlS) throws Exception {
        URL url = new URL("http://localhost:" + PORT + "/jsonp-filter" + urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(5000);
        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }
}
