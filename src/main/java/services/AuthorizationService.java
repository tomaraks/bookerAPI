package services;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import utils.PropertiesReader;

import java.io.IOException;

public class AuthorizationService {

    private static final String AUTH = "auth";

    public static HttpResponse getAuthToken(String username, String password) throws IOException {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);

        //Given
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(new PropertiesReader().getBaseURI() + AUTH);

        StringEntity requestEntity = new StringEntity(
                json.toString(),
                ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);

        // When
        return client.execute(post);
    }
}
