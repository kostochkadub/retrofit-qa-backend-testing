package ru.geekbrains.kosto;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.kosto.dto.Product;
import ru.geekbrains.kosto.service.ProductService;
import ru.geekbrains.kosto.util.RetrofitUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.kosto.common.ConverterResponseBodyToErrorBody.getErrorBody;
import static ru.geekbrains.kosto.base.enums.CategoryType.DOESNOTEXIST;
import static ru.geekbrains.kosto.base.enums.CategoryType.FOOD;

public class ProductNegativeTests {
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
                .withCategoryTitle(FOOD.getTitle())
                .withPrice((int) (Math.random() * 1000 + 1))
                .withTitle(faker.food().ingredient());
    }

    @SneakyThrows
    @Test
    void createNewFoodProductNegativeTest() {
        Response<Product> response =
                productService.createProduct(product.withId(DOESNOTEXIST.getId()))
                        .execute();

        assertThat(response.code()).isEqualTo(400);
        assertThat(getErrorBody(response).getMessage()).isEqualTo("Id must be null for new entity");
    }

}
