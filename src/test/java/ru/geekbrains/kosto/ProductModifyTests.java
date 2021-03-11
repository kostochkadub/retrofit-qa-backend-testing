package ru.geekbrains.kosto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.kosto.dto.Product;
import ru.geekbrains.kosto.service.ProductService;
import ru.geekbrains.kosto.util.DbUtils;
import ru.geekbrains.kosto.util.RetrofitUtils;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.kosto.base.enums.CategoryType.FOOD;

public class ProductModifyTests extends BaseTests {

    String expectTitle;
    Integer expectPrice;
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
        assertThat(response.code()).isEqualTo(HTTP_OK);
        assertThat(checkModifyProduct(productId)).isTrue();

        assertThat(productsMapper.selectByPrimaryKey(productId).getCategory_id()).isEqualTo(FOOD.getId());
        assertThat(productsMapper.selectByPrimaryKey(productId).getPrice()).isEqualTo(expectPrice);
        assertThat(productsMapper.selectByPrimaryKey(productId).getTitle()).isEqualTo(expectTitle);
    }

    @SneakyThrows
    boolean checkModifyProduct(Long productId) {
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
        if (productId != null) {
            DbUtils.getProductsMapper().deleteByPrimaryKey(productId);
        }
    }
}
