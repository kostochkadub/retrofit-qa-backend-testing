package ru.geekbrains.kosto;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.kosto.dto.Product;
import ru.geekbrains.kosto.java4.lesson6.db.dao.ProductsMapper;
import ru.geekbrains.kosto.service.ProductService;
import ru.geekbrains.kosto.util.DbUtils;
import ru.geekbrains.kosto.util.RetrofitUtils;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.kosto.base.enums.CategoryType.ELECTRONICS;

public class ProductElectronicTests {
    static Long productId;
    Faker faker = new Faker();
    static ProductService productService;
    Product product;
    static ProductsMapper productsMapper;

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
        productService = RetrofitUtils
                .getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withCategoryTitle(ELECTRONICS.getTitle())
                .withPrice((int) (Math.random() * 1000 + 1))
                .withTitle(faker.commerce().department());

    }

    @SneakyThrows
    @Test
    void createNewProductTest() {
        Response<Product> response =
                productService.createProduct(product)
                        .execute();

        productId = response.body().getId();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.code()).isEqualTo(HTTP_CREATED);

        assertThat(productsMapper.selectByPrimaryKey(productId).getTitle()).isEqualTo(product.getTitle());
    }


    @SneakyThrows
    @AfterEach
    void tearDown() {
        DbUtils.getProductsMapper().deleteByPrimaryKey(productId);
    }
}
