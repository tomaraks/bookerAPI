import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AuthorizationTest {
    public static HttpClient client;

    @BeforeAll
    public static void setup() {
        client = new DefaultHttpClient();
    }

    @Test
    public void getAuthorizedToken() throws IOException {
        JSONObject json = new JSONObject();
        json.put("username", "admin");
        json.put("password", "password123");

        // Given
        HttpPost post = new HttpPost("https://restful-booker.herokuapp.com/auth");

        StringEntity requestEntity = new StringEntity(
                json.toString(),
                ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);

        // When
        HttpResponse response = client.execute(post);

        // Then
        assertEquals(
                response.getStatusLine().getStatusCode(),
                HttpStatus.SC_OK);

        String responseString = new BasicResponseHandler().handleResponse(response);
        assertFalse(responseString.contains("Bad Credentials"));
    }
}
