package lesson04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AddToShoppingListTest extends AbstractTest {

    @Test
    @Tag("Positive")
    @DisplayName("POST. Add to Shopping List")
    void addToShoppingListTest() throws IOException {

        AddToShoppingListRequest addToShoppingListRequest = new AddToShoppingListRequest();
        addToShoppingListRequest.setItem("1 package baking powder");
        addToShoppingListRequest.setAisle("Baking");
        addToShoppingListRequest.setParse(true);

        AddToShoppingListResponse response = given()


                .queryParam("hash", getApiHash())
                .spec(requestSpecification)
                .body(addToShoppingListRequest)
                .when()
                .post(getUrl() + "/mealplanner/" + getUserName() + "/shopping-list/items")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(AddToShoppingListResponse.class);

        System.out.println(response.getCost());
        assertThat(response.getCost(), notNullValue());
        assertThat(response.getCost().doubleValue(), greaterThan(0.00));

        tearDown(getUrl() + "/mealplanner/" + getUserName() + "/shopping-list/items/" + response.getId());
    }
}
