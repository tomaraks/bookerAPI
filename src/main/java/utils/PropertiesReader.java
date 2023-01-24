package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private Properties properties = new Properties();

    public PropertiesReader() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");

        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("Property File Missing!!");
        }
    }

    public String getBaseURI() {
        return properties.getProperty("baseUrl");
    }

}
