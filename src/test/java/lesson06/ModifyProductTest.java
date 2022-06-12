package lesson06;

import lesson06.api.ProductService;
import lesson06.dto.Product;
import lesson06.utils.RetrofitUtils;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class ModifyProductTest {

    static ProductService productService;
    Product product = null;
    int id = 1;
    static SqlSession session;
    String title;
    int price;
    String category;
    String titleBeforeChanges;
    int priceBeforeChanges;
    String categoryBeforeChanges;


    @BeforeAll
    static void beforeAll() throws IOException {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);

        session = null;
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
    }

    void setUp() {
        product = new Product()
                .withId(id)
                .withTitle(title)
                .withPrice(price)
                .withCategoryTitle(category);
    }

    @Test
    @Tag("Positive")
    @DisplayName("Change product (Positive)")
    void modifyProductTest() throws IOException {

        Response<Product> response = productService.getProductById(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assert response.body() != null;

        titleBeforeChanges = response.body().getTitle();
        priceBeforeChanges = response.body().getPrice();
        categoryBeforeChanges = response.body().getCategoryTitle();

        title = "computer";
        price = 5000;
        category = "Electronic";

        setUp();

        response = productService.modifyProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(200));
        assert response.body() != null;
        assertThat(response.body().getTitle() != titleBeforeChanges, is (true));
        assertThat(response.body().getPrice() != priceBeforeChanges, is (true));
        assertThat(response.body().getCategoryTitle() != categoryBeforeChanges, is (true));

        db.dao.ProductsMapper productsMapper = session.getMapper(db.dao.ProductsMapper.class);
        db.dao.CategoriesMapper categoriesMapper = session.getMapper(db.dao.CategoriesMapper.class);

        db.model.Products selected = productsMapper.selectByPrimaryKey((long) response.body().getId());

        db.model.CategoriesExample example = new db.model.CategoriesExample();
        example.createCriteria().andTitleLike(response.body().getCategoryTitle());
        List<db.model.Categories> list = categoriesMapper.selectByExample(example);
        db.model.Categories categories = list.get(0);
        Long category_id = categories.getId();

        assertThat(selected.getTitle(), equalTo(title));
        assertThat(selected.getPrice(), equalTo(price));
        assertThat(selected.getCategory_id(), equalTo(category_id));


        tearDown();

    }


    @SneakyThrows
    void tearDown() {
        db.dao.ProductsMapper productsMapper = session.getMapper(db.dao.ProductsMapper.class);
        db.dao.CategoriesMapper categoriesMapper = session.getMapper(db.dao.CategoriesMapper.class);

        db.model.Products selected_p = productsMapper.selectByPrimaryKey((long) id);

         db.model.CategoriesExample example = new db.model.CategoriesExample();
        example.createCriteria().andTitleLike(categoryBeforeChanges);
        List<db.model.Categories> list = categoriesMapper.selectByExample(example);
        db.model.Categories categories = list.get(0);
        Long category_id = categories.getId();

        selected_p.setTitle(titleBeforeChanges);
        selected_p.setPrice(priceBeforeChanges);
        selected_p.setCategory_id(category_id);
        productsMapper.updateByPrimaryKey(selected_p);
        session.commit();

        assertThat(selected_p.getTitle(), equalTo(titleBeforeChanges));
        assertThat(selected_p.getPrice(), equalTo(priceBeforeChanges));
        assertThat(selected_p.getCategory_id(), equalTo(category_id));
    }
    @AfterAll
    static void afterAll() {
        session.close();
    }

}
