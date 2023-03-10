import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import services.AuthorizationService;
import services.BookingService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndToEndWorkflowTest {
    public static int bookingId;
    public static String token;

    @BeforeEach
    public void generateToken() throws IOException {
        HttpResponse response = AuthorizationService.getAuthToken("admin", "password123");
        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

        String re = new BasicResponseHandler().handleResponse(response);
        JSONObject tokenBody = new JSONObject(re);
        token = tokenBody.getString("token");
    }

    @Test
    @Order(1)
    public void createBookingAndVerifyByGettingById() throws IOException {
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
        JSONObject body = jsonObject.getJSONObject("booking");

        HttpResponse getResponse = BookingService.getBookingById(bookingId);
        assertEquals(getResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        String responseString2 = new BasicResponseHandler().handleResponse(getResponse);
        JSONObject bookingReceived = new JSONObject(responseString2);

        assertEquals(body.toString(), bookingReceived.toString());
    }

    @Test
    @Order(2)
    public void updateBookingAndVerifyByBody() throws IOException {
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
    @Order(3)
    public void partialUpdateBookingAndVerifyByBody() throws IOException {
        JSONObject jsonObjectReq = new JSONObject();
        jsonObjectReq.put("firstname", "Akshay31");
        jsonObjectReq.put("lastname", "Tomar31");

        HttpResponse partialUpdateResponse = BookingService.partialUpdateBooking(bookingId, token, jsonObjectReq);

        assertEquals(partialUpdateResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        String responseString = new BasicResponseHandler().handleResponse(partialUpdateResponse);
        JSONObject body = new JSONObject(responseString);

        assertEquals(body.getString("firstname"), "Akshay31");
        assertEquals(body.getString("lastname"), "Tomar31");
        assertEquals(body.getInt("totalprice"), 21);
    }

    @Test
    @Order(4)
    public void deleteBookingAndVerify() throws IOException {

        HttpResponse deleteResponse = BookingService.deleteBookingById(bookingId, token);

        assertEquals(deleteResponse.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
        HttpResponse getResponse = BookingService.getBookingById(bookingId);
        assertEquals(getResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

}
