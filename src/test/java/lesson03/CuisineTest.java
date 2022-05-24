package lesson03;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class CuisineTest extends AbstractTest {

    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine (Middle Eastern)")
    void postClassifyCuisineAmericanTest() throws IOException {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("title", "Aladdin's Eatery Hummus")
                .when()
                .post(getUrl() + "/recipes/cuisine")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

        assertThat(response.get(), hasEntry("cuisine", "Middle Eastern"));
    }

    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine (European)")
    void postClassifyCuisineMediterraneanTest() throws IOException {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("title", "Spanish Tortilla")
                .when()
                .post(getUrl() + "/recipes/cuisine")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

        assertThat(response.get("cuisines"), hasItems("European", "Spanish"));
    }

    @Test
    @Tag("Positive")
    @DisplayName("POST. Check that confidence is not null")
    void postClassifyCuisineConfidenceNotNullTest() throws IOException {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("title", "Louisiana Style Gumbo")
                .when()
                .post(getUrl() + "/recipes/cuisine")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

        assertThat(response.get("cuisine") instanceof String, is(true));
        assertThat(response.get("confidence"), not(equalTo(0f)));
    }

    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine")
    void postClassifyCuisineCuisinesTypeTest() throws IOException {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("title", "Chicken Gumbo Luisiana Style")
                .when()
                .post(getUrl() + "/recipes/cuisine")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

        assertThat(response.get("cuisine"), equalToIgnoringCase("creole"));
    }

    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine")
    void postClassifyCuisineSchemaIsValidTest() throws IOException {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("title", "Slow Cooker Sausage Gumbo")
//                .log()
//                .all()
                .when()
                .post(getUrl() + "/recipes/cuisine")
//                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

        assertThat(response.getList("cuisine.collect{it}.flatten()"), notNullValue() );
    }
}
