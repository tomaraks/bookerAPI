import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.BookingService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GetBookingIdsTest {

    @Test
    public void getAllIds() throws IOException {
        HttpResponse response = BookingService.getBookingIds();

        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

        String responseString = new BasicResponseHandler().handleResponse(response);
        JSONArray json = new JSONArray(responseString);
        for(int i = 0; i<json.length(); i++) {
            JSONObject jsonObject = (JSONObject) json.get(i);
            assertTrue(jsonObject.getInt("bookingid") != 0);
        }
    }
}
