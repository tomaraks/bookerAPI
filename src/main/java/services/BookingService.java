package services;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import utils.PropertiesReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BookingService {
    private static final String BOOKING = "booking";

    public static HttpResponse getBookingIdByParams(HashMap<String, String> hashMap) throws IOException {
        HttpGet get = new HttpGet(new PropertiesReader().getBaseURI() + BOOKING);
        HttpParams params = new BasicHttpParams();
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            params.setParameter(entry.getKey(), entry.getValue());
        }
        get.setParams(params);
        HttpClient client = new DefaultHttpClient();

        // When
        return client.execute(get);

    }

    public static HttpResponse getBookingIds() throws IOException {

        //Given
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(new PropertiesReader().getBaseURI() + BOOKING);

        // When
        return client.execute(get);
    }

    public static HttpResponse getBookingById(int id) throws IOException {

        //Given
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(new PropertiesReader().getBaseURI() + BOOKING + "/" + id);
        get.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        get.setHeader(HttpHeaders.ACCEPT, "application/json");

        // When
        return client.execute(get);
    }

    public static HttpResponse deleteBookingById(int id, String token) throws IOException {

        //Given
        HttpClient client = new DefaultHttpClient();
        HttpDelete get = new HttpDelete(new PropertiesReader().getBaseURI() + BOOKING + "/" + id);
        get.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        get.setHeader(HttpHeaders.ACCEPT, "application/json");
        get.setHeader("Cookie", "token=" + token);

        // When
        return client.execute(get);
    }

    public static HttpResponse createBooking(JSONObject jsonObject) throws IOException {
        //Given
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(new PropertiesReader().getBaseURI() + BOOKING);
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        post.setHeader(HttpHeaders.ACCEPT, "application/json");

        StringEntity requestEntity = new StringEntity(
                jsonObject.toString(),
                ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);

        // When
        return client.execute(post);
    }

    public static HttpResponse updateBooking(int id, String token, JSONObject jsonObject) throws IOException {
        //Given
        HttpClient client = new DefaultHttpClient();
        HttpPut put = new HttpPut(new PropertiesReader().getBaseURI() + BOOKING + "/" + id);
        put.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        put.setHeader(HttpHeaders.ACCEPT, "application/json");
        put.setHeader("Cookie", "token=" + token);

        StringEntity requestEntity = new StringEntity(
                jsonObject.toString(),
                ContentType.APPLICATION_JSON);
        put.setEntity(requestEntity);

        // When
        return client.execute(put);
    }

    public static HttpResponse partialUpdateBooking(int id, String token, JSONObject jsonObject) throws IOException {
        //Given
        HttpClient client = new DefaultHttpClient();
        HttpPatch patch = new HttpPatch(new PropertiesReader().getBaseURI() + BOOKING + "/" + id);
        patch.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        patch.setHeader(HttpHeaders.ACCEPT, "application/json");
        patch.setHeader("Cookie", "token=" + token);

        StringEntity requestEntity = new StringEntity(
                jsonObject.toString(),
                ContentType.APPLICATION_JSON);
        patch.setEntity(requestEntity);

        // When
        return client.execute(patch);
    }
}
