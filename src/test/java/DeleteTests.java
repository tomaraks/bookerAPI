import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.AuthorizationService;
import services.BookingService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteTests {
    public static int bookingId;
    public static String token;

    @BeforeEach
    public void generateTokenAndCreateBooking() throws IOException {
        HttpResponse response = AuthorizationService.getAuthToken("admin", "password123");
        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

        String re = new BasicResponseHandler().handleResponse(response);
        JSONObject tokenBody = new JSONObject(re);
        token = tokenBody.getString("token");

        JSONObject json = new JSONObject();
        json.put("firstname", "Akshay");
        json.put("lastname", "Tomar");
        json.put("totalprice", 111);
        json.put("depositpaid", true);
        json.put("bookingdates", new JSONObject().put("checkin", "2023-01-25").put("checkout", "2023-01-26"));
        json.put("additionalneeds", "Cover");

        HttpResponse createResponse = BookingService.createBooking(json);
        assertEquals(createResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        String responseString = new BasicResponseHandler().handleResponse(createResponse);
        JSONObject jsonObject = new JSONObject(responseString);
        bookingId = jsonObject.getInt("bookingid");
    }

    @AfterEach
    public void cleanUp() throws IOException {
        if (bookingId > 0) {
            HttpResponse deleteResponse = BookingService.deleteBookingById(bookingId, token);

            assertEquals(deleteResponse.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
            HttpResponse getResponse = BookingService.getBookingById(bookingId);
            assertEquals(getResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
            bookingId = 0;
        }
    }

    @Test
    public void deleteBookingCorrectly() throws IOException {

        HttpResponse deleteResponse = BookingService.deleteBookingById(bookingId, token);
        assertEquals(deleteResponse.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
        bookingId = 0;
    }

    @Test
    public void deleteBookingWithIncorrectId() throws IOException {
        HttpResponse updateResponse = BookingService.deleteBookingById(-9, token);
        assertEquals(updateResponse.getStatusLine().getStatusCode(), HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void deleteBookingWithIncorrectToken() throws IOException {

        HttpResponse updateResponse = BookingService.deleteBookingById(bookingId, "token");
        assertEquals(updateResponse.getStatusLine().getStatusCode(), HttpStatus.SC_FORBIDDEN);
    }
}
