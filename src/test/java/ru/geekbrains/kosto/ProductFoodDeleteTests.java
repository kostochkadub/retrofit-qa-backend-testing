package ru.geekbrains.kosto;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.kosto.dto.Product;
import ru.geekbrains.kosto.service.ProductService;
import ru.geekbrains.kosto.util.DbUtils;
import ru.geekbrains.kosto.util.RetrofitUtils;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.kosto.base.enums.CategoryType.FOOD;

public class ProductFoodDeleteTests extends BaseTests {

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
        productService = RetrofitUtils
                .getRetrofit()
                .create(ProductService.class);
    }

    @SneakyThrows
    @BeforeEach
    void setUp() {
        product = new Product()
                .withCategoryTitle(FOOD.getTitle())
                .withPrice((int) (Math.random() * 1000 + 1))
                .withTitle(faker.food().ingredient());

        Response<Product> response =
                productService.createProduct(product)
                        .execute();

        productId = response.body().getId();
        assertThat(response.isSuccessful()).isTrue();
    }

    @SneakyThrows
    @Test
    void deleteProductFoodTest() {
        Response<ResponseBody> response =
                productService.deleteProduct(productId)
                        .execute();

        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.code()).isEqualTo(HTTP_OK);
        assertThat(checkThatTheProductDoesNotExist(productId, productService)).isTrue();

        assertThat(productsMapper.selectByPrimaryKey(product.getId())).isNull();
    }

}
