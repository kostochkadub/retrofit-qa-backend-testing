package ru.geekbrains.kosto;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Converter;
import retrofit2.Response;
import ru.geekbrains.kosto.dto.BadRequestBody;
import ru.geekbrains.kosto.dto.Product;
import ru.geekbrains.kosto.service.ProductService;
import ru.geekbrains.kosto.util.RetrofitUtils;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.kosto.base.enums.CategoryType.*;

public class ProductGetTests {

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
        //Получить список всех продуктов
    void getAllProductsPositiveTest() {

        Response<List<Product>> response = productService
                .getAllProduct()
                .execute();

        assertThat(response.code()).isEqualTo(200);
    }

    @SneakyThrows
    @Test
    void getProductElectronicPositiveTest() {
        product = PostProduct.getProduct(ELECTRONICS.getTitle());
        productId = product.getId();

        retrofit2.Response<Product> response = productService
                .getProductWithId(productId)
                .execute();

        assertThat(response.body().getId()).as("Id is not equal").isEqualTo(productId);
        assertThat(response.body().getCategoryTitle()).isEqualTo(product.getCategoryTitle());
        assertThat(response.code()).isEqualTo(200);
    }

    @SneakyThrows
    @Test
    void getProductFoodPositiveTest() {
        product = PostProduct.getProduct(FOOD.getTitle());
        productId = product.getId();

        retrofit2.Response<Product> response = productService
                .getProductWithId(productId)
                .execute();

        assertThat(response.body().getId()).as("Id is not equal").isEqualTo(productId);
        assertThat(response.body().getCategoryTitle()).isEqualTo(product.getCategoryTitle());
        assertThat(response.code()).isEqualTo(200);
    }

    @SneakyThrows
    @Test
    void getNotExistProductNegativeTest() {

        retrofit2.Response<Product> response = productService
                .getProductWithId(DOESNOTEXIST.getId())
                .execute();

        assertThat(response.code()).isEqualTo(404);

        ResponseBody body = response.errorBody();
        Converter<ResponseBody, BadRequestBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(BadRequestBody.class, new Annotation[0]);
        BadRequestBody badRequestBody = converter.convert(body);
        assertThat(badRequestBody.getMessage()).isEqualTo("Unable to find product with id: " + DOESNOTEXIST.getId());
    }

}
