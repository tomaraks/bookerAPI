import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import services.BookingService;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetBookingIdsTest {

    @Test
    public void getAllIds() throws IOException {
        HttpResponse response = BookingService.getBookingIds();

        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        assertBookingIdsAreNotNull(response);
    }

    @Test
    public void getBookingIdByFirstName() throws IOException {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("firstname", "Susan");
        HttpResponse response = BookingService.getBookingIdByParams(hashMap);

        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        assertBookingIdsAreNotNull(response);
    }

    @Test
    public void getBookingIdByLastName() throws IOException {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("lastname", "Wilson");
        HttpResponse response = BookingService.getBookingIdByParams(hashMap);

        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        assertBookingIdsAreNotNull(response);
    }

    @Test
    public void getBookingIdByFirstAndLastName() throws IOException {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("firstname", "Susan");
        hashMap.put("lastname", "Wilson");
        HttpResponse response = BookingService.getBookingIdByParams(hashMap);

        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        assertBookingIdsAreNotNull(response);
    }

    @Test
    public void getBookingIdByCheckinAndCheckoutDates() throws IOException {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("checkin", "2019-02-07");
        hashMap.put("checkout", "2019-03-29");
        HttpResponse response = BookingService.getBookingIdByParams(hashMap);

        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        assertBookingIdsAreNotNull(response);
    }

    public void assertBookingIdsAreNotNull(HttpResponse response) throws IOException {
        String responseString = new BasicResponseHandler().handleResponse(response);
        JSONArray json = new JSONArray(responseString);
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = (JSONObject) json.get(i);
            assertTrue(jsonObject.getInt("bookingid") != 0);
        }
    }
}
