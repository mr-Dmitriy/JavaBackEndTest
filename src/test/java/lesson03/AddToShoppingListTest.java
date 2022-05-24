package lesson03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class AddToShoppingListTest extends AbstractTest {

    @Test
    @Tag("Positive")
    @DisplayName("POST. Add to Shopping List")
    void addToShoppingListTest() throws IOException {
        String id = given()
                .queryParam("hash", getApiHash())
                .queryParam("apiKey", getApiKey())
                .body("{\n"
                        + " \"item\": \"1 package baking powder\",\n"
                        + " \"aisle\": \"Baking\",\n"
                        + " \"parse\": true\n"
                        + "}")
//                .log()
//                .all()
                .when()
                .post(getUrl() + "/mealplanner/" + getUserName() + "/shopping-list/items")
//                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        tearDown(getUrl() + "/mealplanner/" + getUserName() + "/shopping-list/items/" + id);
    }
}
