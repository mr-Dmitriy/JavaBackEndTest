package lesson05;

import lesson05.api.ProductService;
import lesson05.dto.Product;
import lesson05.utils.RetrofitUtils;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class CreateProductTest {

    static ProductService productService;
    Product product = null;
    int id;


    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    //    @BeforeEach
    void setUp (String title, int price, String category) {
        product = new Product()
                .withTitle(title)
                .withPrice(price)
                .withCategoryTitle(category);
    }

    @Test
    @Tag("Positive")
    @DisplayName("Product creation")
    void createProductTest() throws IOException {

        setUp("bread", 9562, "Food");
        Response<Product> response = productService.createProduct(product).execute();

        assertThat(response.code(), equalTo(201));
        assert response.body() != null;
        assertThat(response.body().getId(), notNullValue());
        assertThat(response.body().getCategoryTitle(), equalTo("Food"));
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        id =  response.body().getId();
        tearDown();

        // Check product after deleting
        Response<Product> responseForChecking = productService.getProductById(id).execute();
        assertThat(responseForChecking.code(), equalTo(404));

    }

    @SneakyThrows
//    @AfterEach
    void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

    }
}