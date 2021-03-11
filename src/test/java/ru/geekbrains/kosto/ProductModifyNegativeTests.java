package ru.geekbrains.kosto;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Converter;
import retrofit2.Response;
import ru.geekbrains.kosto.dto.BadRequestBody;
import ru.geekbrains.kosto.dto.Product;
import ru.geekbrains.kosto.service.ProductService;
import ru.geekbrains.kosto.util.DbUtils;
import ru.geekbrains.kosto.util.RetrofitUtils;

import java.lang.annotation.Annotation;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.kosto.base.enums.CategoryType.CATEGORY_ID_DOES_NOT_EXIST;
import static ru.geekbrains.kosto.base.enums.CategoryType.FOOD;
import static ru.geekbrains.kosto.base.enums.ProductId.PRODUCT_ID_DOES_NOT_EXIST;

public class ProductModifyNegativeTests extends BaseTests {

    String expectTitle;
    Product productModify;

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
        assertThat(response.code()).isEqualTo(HTTP_CREATED);

        productModify = new Product()
                .withId(PRODUCT_ID_DOES_NOT_EXIST.getId())
                .withPrice(product.getPrice())
                .withTitle(expectTitle)
                .withCategoryTitle(FOOD.getTitle());
    }

    @SneakyThrows
    @Test
    void modifyProductNegative() {
        Response<Product> response =
                productService.putModifyProduct(productModify)
                        .execute();

        assertThat(response.code()).isEqualTo(HTTP_BAD_REQUEST);

        ResponseBody body = response.errorBody();
        Converter<ResponseBody, BadRequestBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(BadRequestBody.class, new Annotation[0]);
        BadRequestBody badRequestBody = converter.convert(body);
        assertThat(badRequestBody.getMessage()).isEqualTo("Product with id: " + CATEGORY_ID_DOES_NOT_EXIST.getId() + " doesn't exist");
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        if (productId != null) {
            DbUtils.getProductsMapper().deleteByPrimaryKey(productId);
        }
    }


}
