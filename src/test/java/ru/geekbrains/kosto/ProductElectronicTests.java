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
import ru.geekbrains.kosto.service.ProductService;
import ru.geekbrains.kosto.util.RetrofitUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.kosto.base.enums.CategoryType.ELECTRONICS;

public class ProductElectronicTests {
    static Integer productId;
    Faker faker = new Faker();
    static ProductService productService;
    Product product;

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
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
        assertThat(response.code()).isEqualTo(201);

    }


    @SneakyThrows
    @AfterEach
    void tearDown() {
        Response<ResponseBody> response =
                productService.deleteProduct(productId)
                        .execute();

        assertThat(response.isSuccessful()).isTrue();
    }
}
