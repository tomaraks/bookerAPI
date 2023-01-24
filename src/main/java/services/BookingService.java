package services;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import utils.PropertiesReader;

import java.io.IOException;

public class BookingService {
    private static final String BOOKING = "booking";

    public static HttpResponse getBookingIds() throws IOException {

        //Given
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(new PropertiesReader().getBaseURI() + BOOKING);

        // When
        return client.execute(get);
    }
}
