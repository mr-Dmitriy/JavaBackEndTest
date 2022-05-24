package lesson03;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ComplexSearchTest extends AbstractTest{

    @Test
    @Tag("Positive")
    @DisplayName("GET. Recipe search with number and addRecipeInformation")
    void getRecipeSearchWith2ParamsTest() throws IOException {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", "2")
                .queryParam("addRecipeInformation", "true")
                .when()
                .get(getUrl() + "/recipes/complexSearch")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

        assertThat(response.get(), hasKey("number"));
        assertThat(response.get("number"), equalTo(2));
    }

    @Test
    @Tag("Positive")
    @DisplayName("GET. Recipe search with param: number, addRecipeInformation, instructionsRequired," +
                 " sort, sortDirection, maxProtein, limitLicense ")
    void getRecipeSearchWith8ParamsTest() throws IOException {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", "2")
                .queryParam("addRecipeInformation", "true")
                .queryParam("instructionsRequired", "true")
                .queryParam("includeIngredients", "tomato")
                .queryParam("sort", "calories")
                .queryParam("sortDirection", "asc")
                .queryParam("maxProtein", "100")
                .queryParam("limitLicense", "true")
                .when()
                .get(getUrl() + "/recipes/complexSearch")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

        assertThat(response.get(), hasKey("number"));
        assertThat(response.get("number"), equalTo(2));
        assertThat(response.getFloat("results.nutrition.nutrients[0].collect{it.amount}.flatten().max()"), lessThanOrEqualTo(100.f));

    }

    @Test
    @Tag("Positive")
    @DisplayName("GET. Recipe search with param: number, cuisine, addRecipeInformation," +
                 " excludeIngredients")
    void getRecipeSearchWith4ParamsTestV1() throws IOException {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", "5")
                .queryParam("cuisine", "European")
                .queryParam("addRecipeInformation", "true")
                .queryParam("excludeIngredients", "cheese")
                .when()
                .get(getUrl() + "/recipes/complexSearch")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

        assertThat(response.get(), hasKey("number"));
        assertThat(response.get("number"), equalTo(5));
        assertThat(response.getString("results.cuisines.collect{it}.flatten()"), containsString("European"));

    }

        @Test
        @Tag("Positive")
        @DisplayName("GET. Recipe search with param: addRecipeInformation, offset, number," +
                " equipment")
        void getRecipeSearchWith4ParamsTestV2() throws IOException {
            JsonPath response = given()
                    .queryParam("apiKey", getApiKey())
                    .queryParam("addRecipeInformation", "true")
                    .queryParam("offset", "2")
                    .queryParam("number", "5")
                    .queryParam("equipment", "pot")
                    .when()
                    .get(getUrl() + "/recipes/complexSearch")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath();

            assertThat(response.get(), hasKey("number"));
            assertThat(response.get("number"), equalTo(5));
            assertThat(response.get("offset"), equalTo(2));

//            System.out.println(response.getList("results.collect{it.analyzedInstructions.steps.equipment
//                                                .collect{it.name}.flatten()}.findAll{it.contains('pot')}"));

            assertThat(response.getList("results.collect{it.analyzedInstructions.steps.equipment.collect{it.name}." +
                    "flatten()}.findAll{it.contains('pot')}").size(), greaterThan(0));

    }

    @Test
    @Tag("Positive")
    @DisplayName("GET. Recipe search with param: addRecipeInformation, offset, number," +
            " maxReadyTime")
    void getRecipeSearchWith4ParamsTestV3() throws IOException {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("addRecipeInformation", "true")
                .queryParam("offset", "3")
                .queryParam("number", "10")
                .queryParam("maxReadyTime", "10")
                .when()
                .get(getUrl() + "/recipes/complexSearch")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

        assertThat(response.get(), hasKey("number"));
        assertThat(response.get("number"), equalTo(10));
        assertThat(response.get("offset"), equalTo(3));
/*        System.out.println(response.getString("results.collect{it.readyInMinutes}.flatten()"));*/
        assertThat(response.get("results.collect{it.readyInMinutes}.flatten().max()"), lessThanOrEqualTo(10));

    }

}
