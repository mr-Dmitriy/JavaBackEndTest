package lesson04;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class CuisineTest extends AbstractTest {

    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine (Middle Eastern)")
    void postClassifyCuisineAmericanTest() throws IOException {
        CuisineResponse response = given()
                .spec(requestSpecification)
                .queryParam("title", "Aladdin's Eatery Hummus")
                .when()
                .post(getUrl() + "/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(CuisineResponse.class);

        assertThat(response.getCuisines(), hasItem("Middle Eastern"));

    }

    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine (European)")
    void postClassifyCuisineMediterraneanTest() throws IOException {
        CuisineResponse response = given()
                .spec(requestSpecification)
                .queryParam("title", "Spanish Tortilla")
                .when()
                .post(getUrl() + "/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(CuisineResponse.class);

        assertThat(response.getCuisine(), containsString("European"));
    }

    @Test
    @Tag("Positive")
    @DisplayName("POST. Check that confidence is not null")
    void postClassifyCuisineConfidenceNotNullTest() throws IOException {
        CuisineResponse response = given()
                .spec(requestSpecification)
                .queryParam("title", "Louisiana Style Gumbo")
                .when()
                .post(getUrl() + "/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(CuisineResponse.class);

        assertThat(response.getCuisine() instanceof String, is(true));
        assertThat(response.getConfidence(), not(equalTo(0f)));
    }

    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine")
    void postClassifyCuisineCuisinesTypeTest() throws IOException {
        CuisineResponse response = given()
                .spec(requestSpecification)
                .queryParam("title", "Chicken Gumbo Luisiana Style")
                .when()
                .post(getUrl() + "/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(CuisineResponse.class);

        assertThat(response.getCuisine(), containsStringIgnoringCase("creole"));
    }

    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine")
    void postClassifyCuisineSchemaIsValidTest() throws IOException {
        CuisineResponse response = given()
                .spec(requestSpecification)
                .queryParam("title", "Slow Cooker Sausage Gumbo")
//                .log()
//                .all()
                .when()
                .post(getUrl() + "/recipes/cuisine")
//                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(CuisineResponse.class);

        assertThat(response.getCuisine(), notNullValue() );
    }
}
