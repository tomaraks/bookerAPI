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

public class AllUpdateTest {
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
    public void updateBookingCorrectly() throws IOException {
        JSONObject json = new JSONObject();
        json.put("firstname", "Akshay21");
        json.put("lastname", "Tomar21");
        json.put("totalprice", 21);
        json.put("depositpaid", true);
        json.put("bookingdates", new JSONObject().put("checkin", "2023-01-25").put("checkout", "2023-01-26"));
        json.put("additionalneeds", "Cover");

        HttpResponse updateResponse = BookingService.updateBooking(bookingId, token, json);

        assertEquals(updateResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        String responseString = new BasicResponseHandler().handleResponse(updateResponse);
        JSONObject body = new JSONObject(responseString);

        assertEquals(body.getString("firstname"), "Akshay21");
        assertEquals(body.getString("lastname"), "Tomar21");
        assertEquals(body.getInt("totalprice"), 21);
    }

    @Test
    public void updateBookingWithIncorrectBody() throws IOException {
        JSONObject json = new JSONObject();
        json.put("ff", "Akshay");
        json.put("ff", "Tomar");
        json.put("totalprice", 21);
        json.put("depositpaid", true);
        json.put("bookingdates", new JSONObject().put("checkin", "2023-01-25").put("checkout", "2023-01-26"));
        json.put("additionalneeds", "Cover");

        HttpResponse updateResponse = BookingService.updateBooking(bookingId, token, json);
        assertEquals(updateResponse.getStatusLine().getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void updateBookingWithIncorrectId() throws IOException {
        JSONObject json = new JSONObject();
        json.put("firstname", "Akshay21");
        json.put("lastname", "Tomar21");
        json.put("totalprice", 21);
        json.put("depositpaid", true);
        json.put("bookingdates", new JSONObject().put("checkin", "2023-01-25").put("checkout", "2023-01-26"));
        json.put("additionalneeds", "Cover");

        HttpResponse updateResponse = BookingService.updateBooking(-9, token, json);
        assertEquals(updateResponse.getStatusLine().getStatusCode(), HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void updateBookingWithIncorrectToken() throws IOException {
        JSONObject json = new JSONObject();
        json.put("firstname", "Akshay21");
        json.put("lastname", "Tomar21");
        json.put("totalprice", 21);
        json.put("depositpaid", true);
        json.put("bookingdates", new JSONObject().put("checkin", "2023-01-25").put("checkout", "2023-01-26"));
        json.put("additionalneeds", "Cover");

        HttpResponse updateResponse = BookingService.updateBooking(bookingId, "token", json);
        assertEquals(updateResponse.getStatusLine().getStatusCode(), HttpStatus.SC_FORBIDDEN);
    }
}
