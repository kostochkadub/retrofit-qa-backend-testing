package ru.geekbrains.kosto;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import ru.geekbrains.kosto.dto.ErrorBody;
import ru.geekbrains.kosto.dto.Product;
import ru.geekbrains.kosto.java4.lesson6.db.dao.ProductsMapper;
import ru.geekbrains.kosto.service.ProductService;
import ru.geekbrains.kosto.util.RetrofitUtils;

import java.lang.annotation.Annotation;

public class BaseTests {

    static Long productId;
    Faker faker = new Faker();
    Product product;
    static ProductService productService;
    static ProductsMapper productsMapper;

    @SneakyThrows
    boolean checkThatTheProductDoesNotExist(Long productId, ProductService productService) {
        Response<Product> response =
                productService.getProductWithId(productId)
                        .execute();

        if (response != null && !response.isSuccessful() && response.errorBody() != null && response.code() == 404) {
            ResponseBody body = response.errorBody();
            Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);

            String message = errorBody.getMessage();
            String expectMessage = "Unable to find product with id: " + productId;

            if (message.equals(expectMessage)) {
                return true;
            } else return false;
        } else return false;
    }
}
