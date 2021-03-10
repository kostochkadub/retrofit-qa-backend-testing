package ru.geekbrains.kosto.common;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import ru.geekbrains.kosto.dto.Product;
import ru.geekbrains.kosto.service.ProductService;
import ru.geekbrains.kosto.util.RetrofitUtils;

public class PostProduct {

    private static Product product;
    private static ProductService productService;
    static Faker faker = new Faker();

    @SneakyThrows
    public static Product getProduct(String title) {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        product = new Product()
                .withCategoryTitle(title)
                .withPrice((int) (Math.random() * 1000 + 1))
                .withTitle(faker.food().ingredient());

        retrofit2.Response<Product> response =
                productService.createProduct(product)
                        .execute();
        return response.body();
    }
}
