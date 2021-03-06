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
import static ru.geekbrains.kosto.base.enums.CategoryType.FOOD;

public class ProductModifyTests {
    static Integer productId;
    String expectTitle;
    Integer expectPrice;
    Faker faker = new Faker();
    static ProductService productService;
    Product product;
    Product productModify;

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
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
        assertThat(response.code()).isEqualTo(201);

        expectTitle = product.getTitle() + " Modify";
        expectPrice = product.getPrice() + 1;

        productModify = new Product()
                .withId(productId)
                .withCategoryTitle(FOOD.getTitle())
                .withPrice(product.getPrice() + 1)
                .withTitle(expectTitle);
    }

    @SneakyThrows
    @Test
    void modifyProduct() {
        Response<Product> response =
                productService.putModifyProduct(productModify)
                        .execute();

        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.code()).isEqualTo(200);
        assertThat(checkModifyProduct(productId)).isTrue();
    }

    @SneakyThrows
    boolean checkModifyProduct(int productId) {
        Response<Product> response =
                productService.getProductWithId(productId)
                        .execute();

        if (response.body().getTitle().equals(expectTitle)
                && response.body().getCategoryTitle().equals(FOOD.getTitle())
                && response.body().getPrice().equals(expectPrice)) {
            return true;
        } else return false;
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
