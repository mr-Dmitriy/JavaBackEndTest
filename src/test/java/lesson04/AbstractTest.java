package lesson04;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import static io.restassured.RestAssured.given;




public abstract class AbstractTest {

    ResponseSpecification responseSpecification = null;
    RequestSpecification requestSpecification = null;

    final static Properties prop = new Properties();

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void beforeTest() throws IOException {
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();

//        RestAssured.responseSpecification = responseSpecification;

        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", getApiKey())
//                .setContentType(ContentType.JSON)
//                .log(LogDetail.ALL)
                .build();

//        RestAssured.requestSpecification = requestSpecification;

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
