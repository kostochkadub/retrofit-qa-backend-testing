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

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.kosto.base.enums.ProductId.PRODUCT_ID_DOES_NOT_EXIST;
import static ru.geekbrains.kosto.common.ConverterResponseBodyToErrorBody.getErrorBody;
import static ru.geekbrains.kosto.base.enums.CategoryType.FOOD;

public class ProductNegativeTests extends BaseTests{

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
                productService.createProduct(product.withId(PRODUCT_ID_DOES_NOT_EXIST.getId()))
                        .execute();

        assertThat(response.code()).isEqualTo(HTTP_BAD_REQUEST);
        assertThat(getErrorBody(response).getMessage()).isEqualTo("Id must be null for new entity");
    }

}
