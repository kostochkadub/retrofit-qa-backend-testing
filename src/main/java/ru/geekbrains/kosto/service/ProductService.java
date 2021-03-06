package ru.geekbrains.kosto.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.geekbrains.kosto.dto.Product;

import java.util.List;

public interface ProductService {
    @GET("products")
    Call<List<Product>> getAllProduct();

    @GET("products/{id}")
    Call<Product> getProductWithId(@Path("id") Object id);

    @PUT("products")
    Call<Product> putModifyProduct(@Body Product putProductsRequest);

    @POST("products")
    Call<Product> createProduct(@Body Product createProductRequest);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") Object id);
}
