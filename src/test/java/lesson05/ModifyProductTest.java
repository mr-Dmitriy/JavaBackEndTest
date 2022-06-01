package lesson05;

import lesson05.api.ProductService;
import lesson05.dto.Product;
import lesson05.utils.RetrofitUtils;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class ModifyProductTest {

    static ProductService productService;
    Product product = null;
    int id = 1;


    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    void setUp(String title, int price, String category) {
        product = new Product()
                .withId(id)
                .withTitle(title)
                .withPrice(price)
                .withCategoryTitle(category);
    }

    @Test
    @Tag("Positive")
    @DisplayName("Change product (Positive)")
    void modifyProductPositiveTest() throws IOException {

        Response<Product> response = productService.getProductById(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assert response.body() != null;

        String titleBeforeChanges = response.body().getTitle();
        int priceBeforeChanges = response.body().getPrice();
        String categoryBeforeChanges = response.body().getCategoryTitle();

        setUp("computer", 5000, "Electronic");

        response = productService.modifyProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(200));
        assert response.body() != null;
        assertThat(response.body().getTitle() != titleBeforeChanges, is (true));
        assertThat(response.body().getPrice() != priceBeforeChanges, is (true));
        assertThat(response.body().getCategoryTitle() != categoryBeforeChanges, is (true));

        tearDown();
    }


    @SneakyThrows
    void tearDown() {
        Response<Product> response = productService.modifyProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }
}
