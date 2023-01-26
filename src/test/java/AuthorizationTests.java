import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.BasicResponseHandler;
import org.junit.jupiter.api.Test;
import services.AuthorizationService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationTests {

    @Test
    public void getAuthorizedTokenByValidCredentials() throws IOException {
        // When
        HttpResponse response = AuthorizationService.getAuthToken("admin", "password123");

        // Then
        assertEquals(
                response.getStatusLine().getStatusCode(),
                HttpStatus.SC_OK);

        String responseString = new BasicResponseHandler().handleResponse(response);
        assertFalse(responseString.contains("Bad Credentials"));
    }

    @Test
    public void getAuthorizedTokenByInValidCredentials() throws IOException {
        // When
        HttpResponse response = AuthorizationService.getAuthToken("admin", "invalid");

        // Then
        assertEquals(
                response.getStatusLine().getStatusCode(),
                HttpStatus.SC_OK);

        String responseString = new BasicResponseHandler().handleResponse(response);
        assertTrue(responseString.contains("Bad credentials"));
    }
}
