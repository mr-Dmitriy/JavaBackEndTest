package lesson04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ComplexSearchTest extends AbstractTest {

    @Test
    @Tag("Positive")
    @DisplayName("GET. Recipe search with number and addRecipeInformation")
    void getRecipeSearchWith2ParamsTest() throws IOException {
        ComplexSearchResponse response = given()
                .spec(requestSpecification)
                .queryParam("number", "2")
                .queryParam("addRecipeInformation", "true")
                .when()
                .get(getUrl() + "/recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ComplexSearchResponse.class);

        assertThat(response.getNumber(), equalTo(2));
        response.getResults().forEach(recipes ->
                assertThat(recipes.getReadyInMinutes(), notNullValue()));

    }

    @Test
    @Tag("Positive")
    @DisplayName("GET. Recipe search with param: number, addRecipeInformation, instructionsRequired," +
                 " sort, sortDirection, maxProtein, limitLicense ")
    void getRecipeSearchWith8ParamsTest() throws IOException {
        ComplexSearchResponse response = given()
                .spec(requestSpecification)
                .queryParam("number", "5")
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
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ComplexSearchResponse.class);

        assertThat(response.getNumber(), equalTo(5));
        response.getResults().toArray();


        for (ComplexSearchResponse.Result res : response.getResults()) {
            for (ComplexSearchResponse.Nutrient nutr : res.nutrition.getNutrients()) {
                if(nutr.getName().equalsIgnoreCase("calories" ))
                assertThat(nutr.getAmount(), lessThanOrEqualTo(100f));
            }
        }

 }


    @Test
    @Tag("Positive")
    @DisplayName("GET. Recipe search with param: number, cuisine, addRecipeInformation," +
                 " excludeIngredients")
    void getRecipeSearchWith4ParamsTestV1() throws IOException {
        ComplexSearchResponse response = given()
                .spec(requestSpecification)
                .queryParam("number", "5")
                .queryParam("cuisine", "European")
                .queryParam("addRecipeInformation", "true")
                .queryParam("excludeIngredients", "cheese")
                .when()
                .get(getUrl() + "/recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ComplexSearchResponse.class);


        assertThat(response.getNumber(), equalTo(5));
        for (ComplexSearchResponse.Result res : response.getResults()) {
               assertThat( new ArrayList<>(res.getCuisines()), hasItem("European"));
        }

    }

        @Test
        @Tag("Positive")
        @DisplayName("GET. Recipe search with param: addRecipeInformation, offset, number," +
                " equipment")
        void getRecipeSearchWith4ParamsTestV2() throws IOException {
            ComplexSearchResponse response = given()
                    .spec(requestSpecification)
                    .queryParam("addRecipeInformation", "true")
                    .queryParam("offset", "2")
                    .queryParam("number", "5")
                    .queryParam("equipment", "pot")
                    .when()
                    .get(getUrl() + "/recipes/complexSearch")
                    .then()
                    .spec(responseSpecification)
                    .extract()
                    .body()
                    .as(ComplexSearchResponse.class);

            assertThat(response.getNumber(), equalTo(5));
            assertThat(response.getOffset(), equalTo(2));


    }

    @Test
    @Tag("Positive")
    @DisplayName("GET. Recipe search with param: addRecipeInformation, offset, number," +
            " maxReadyTime")
    void getRecipeSearchWith4ParamsTestV3() throws IOException {
        ComplexSearchResponse response = given()
                .spec(requestSpecification)
                .queryParam("addRecipeInformation", "true")
                .queryParam("offset", "3")
                .queryParam("number", "10")
                .queryParam("maxReadyTime", "10")
                .when()
                .get(getUrl() + "/recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ComplexSearchResponse.class);

        response.getResults().forEach(recipes ->
                assertThat(recipes.getReadyInMinutes(), lessThanOrEqualTo(10)));

    }

}
