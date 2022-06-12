package lesson06;

import lesson06.api.ProductService;
import lesson06.dto.Product;
import lesson06.utils.RetrofitUtils;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.apache.ibatis.io.Resources;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class CreateProductTest {

    static ProductService productService;
    Product product = null;
    int id;
    static SqlSession session;


    @BeforeAll
    static void beforeAll() throws IOException {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);

        session = null;
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
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
    void createAndDeleteProductTest() throws IOException {

        setUp("bread", 9562, "Food");
        Response<Product> response = productService.createProduct(product).execute();

        assertThat(response.code(), equalTo(201));
        assert response.body() != null;
        assertThat(response.body().getId(), notNullValue());
        assertThat(response.body().getCategoryTitle(), equalTo("Food"));
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        id =  response.body().getId();

        db.dao.ProductsMapper productsMapper = session.getMapper(db.dao.ProductsMapper.class);
        db.dao.CategoriesMapper categoriesMapper = session.getMapper(db.dao.CategoriesMapper.class);

        db.model.Products selected = productsMapper.selectByPrimaryKey((long) id);


        db.model.CategoriesExample example = new db.model.CategoriesExample();
        example.createCriteria().andTitleLike(response.body().getCategoryTitle());
        List<db.model.Categories> list = categoriesMapper.selectByExample(example);
        db.model.Categories categories = list.get(0);
        Long category_id = categories.getId();

        assertThat(selected.getTitle(), equalTo(response.body().getTitle()));
        assertThat(selected.getPrice(), equalTo(response.body().getPrice()));
        assertThat(selected.getCategory_id(), equalTo(category_id));

        tearDown();

        // Check product after deleting
        Response<Product> responseForChecking = productService.getProductById(id).execute();
        assertThat(responseForChecking.code(), equalTo(404));

    }

    @SneakyThrows
    void tearDown() {
        db.dao.ProductsMapper productsMapper = session.getMapper(db.dao.ProductsMapper.class);

        db.model.Products selected = productsMapper.selectByPrimaryKey((long) id);

        productsMapper.deleteByPrimaryKey(selected.getId());
        session.commit();
    }

    @AfterAll
    static void afterAll() {
        session.close();
    }
}