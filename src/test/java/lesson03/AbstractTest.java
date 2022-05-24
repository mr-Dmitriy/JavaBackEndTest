package lesson03;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;




public abstract class AbstractTest {

    final static java.util.Properties prop = new java.util.Properties();

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    static Integer getMaxIndex(JsonPath response) {
        return new ArrayList<>(response.get("results")).size() - 1;
    }

    public static String getUrl() throws IOException {
        loadProperties();
        return prop.getProperty("baseUrl");
    }

    public static String getApiKey() throws IOException {
        loadProperties();
        return prop.getProperty("apiKey");
    }

    public static String getApiHash() throws IOException {
        loadProperties();
        return prop.getProperty("apiHash");
    }

    public static String getUserName() throws IOException {
        loadProperties();
        return prop.getProperty("userName");
    }

    private static void loadProperties() throws IOException {
        try(FileInputStream configFile = new FileInputStream("src/test/java/resources/propForTest.properties")){
            prop.load(configFile);
        }
    }

    static void tearDown(String url) throws IOException {
        given()
                .queryParam("hash", getApiHash())
                .queryParam("apiKey", getApiKey())
                .delete(url)
                .then()
                .statusCode(200);
    }
}
