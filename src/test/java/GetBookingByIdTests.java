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

public class GetBookingByIdTests {
    public static int bookingId;
    public static String token;
    public static JSONObject body;

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
        body = jsonObject.getJSONObject("booking");
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
    public void getBookingByCorrectId() throws IOException {
        HttpResponse getResponse = BookingService.getBookingById(bookingId);
        assertEquals(getResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        String responseString2 = new BasicResponseHandler().handleResponse(getResponse);
        JSONObject bookingReceived = new JSONObject(responseString2);

        assertEquals(body.toString(), bookingReceived.toString());
    }

    @Test
    public void getBookingByIncorrectCorrectId() throws IOException {
        HttpResponse updateResponse = BookingService.getBookingById(-9);
        assertEquals(updateResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }
}
