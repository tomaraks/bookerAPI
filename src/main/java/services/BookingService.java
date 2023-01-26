package services;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import utils.PropertiesReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class BookingService {
    private static final String BOOKING = "booking";

    public static HttpResponse getBookingIdByParams(HashMap<String, String> hashMap) throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder(new PropertiesReader().getBaseURI() + BOOKING);
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            builder.setParameter(entry.getKey(), entry.getValue());
        }

        HttpGet get = new HttpGet(builder.build());
        HttpClient httpclient = HttpClientBuilder.create().build();

        return httpclient.execute(get);
    }

    public static HttpResponse getBookingIds() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(new PropertiesReader().getBaseURI() + BOOKING);

        return client.execute(get);
    }

    public static HttpResponse getBookingById(int id) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(new PropertiesReader().getBaseURI() + BOOKING + "/" + id);
        get.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        get.setHeader(HttpHeaders.ACCEPT, "application/json");

        return client.execute(get);
    }

    public static HttpResponse deleteBookingById(int id, String token) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete get = new HttpDelete(new PropertiesReader().getBaseURI() + BOOKING + "/" + id);
        get.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        get.setHeader(HttpHeaders.ACCEPT, "application/json");
        get.setHeader("Cookie", "token=" + token);

        return client.execute(get);
    }

    public static HttpResponse createBooking(JSONObject jsonObject) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(new PropertiesReader().getBaseURI() + BOOKING);
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        post.setHeader(HttpHeaders.ACCEPT, "application/json");

        StringEntity requestEntity = new StringEntity(
                jsonObject.toString(),
                ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);

        return client.execute(post);
    }

    public static HttpResponse updateBooking(int id, String token, JSONObject jsonObject) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut put = new HttpPut(new PropertiesReader().getBaseURI() + BOOKING + "/" + id);
        put.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        put.setHeader(HttpHeaders.ACCEPT, "application/json");
        put.setHeader("Cookie", "token=" + token);

        StringEntity requestEntity = new StringEntity(
                jsonObject.toString(),
                ContentType.APPLICATION_JSON);
        put.setEntity(requestEntity);

        return client.execute(put);
    }

    public static HttpResponse partialUpdateBooking(int id, String token, JSONObject jsonObject) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPatch patch = new HttpPatch(new PropertiesReader().getBaseURI() + BOOKING + "/" + id);
        patch.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        patch.setHeader(HttpHeaders.ACCEPT, "application/json");
        patch.setHeader("Cookie", "token=" + token);

        StringEntity requestEntity = new StringEntity(
                jsonObject.toString(),
                ContentType.APPLICATION_JSON);
        patch.setEntity(requestEntity);

        return client.execute(patch);
    }
}
