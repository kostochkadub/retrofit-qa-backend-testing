package ru.geekbrains.kosto;

import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Converter;
import retrofit2.Response;
import ru.geekbrains.kosto.dto.BadRequestBody;
import ru.geekbrains.kosto.dto.Category;
import ru.geekbrains.kosto.service.CategoryService;
import ru.geekbrains.kosto.util.RetrofitUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.kosto.ConverterResponseBodyToErrorBody.getErrorBody;
import static ru.geekbrains.kosto.base.enums.CategoryType.DOESNOTEXIST;
import static ru.geekbrains.kosto.base.enums.CategoryType.FOOD;

public class CategoryTests {
    static CategoryService categoryService;

    @BeforeAll
    static void beforeAll() throws MalformedURLException {
        categoryService = RetrofitUtils
                .getRetrofit()
                .create(CategoryService.class);
    }

    @Test
    void getFoodCategoryPositiveTest() throws IOException {
        Response<Category> response = categoryService
                .getCategory(FOOD.getId())
                .execute();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.body().getId()).as("Id is not equal to 1!").isEqualTo(1);
        assertThat(response.body().getTitle()).isEqualTo(FOOD.getTitle());
    }

    @Test
    void getFoodCategoryNegative404Test() throws IOException {
        Response<Category> response = categoryService
                .getCategory(DOESNOTEXIST.getId())
                .execute();
        assertThat(response.code()).isEqualTo(404);

        assertThat(getErrorBody(response).getMessage()).isEqualTo("Unable to find category with id: " + DOESNOTEXIST.getId());
    }

    @Test
    void getFoodCategoryNegative400Test() throws IOException {
        Response<Category> response = categoryService
                .getCategory(DOESNOTEXIST.getTitle())
                .execute();
        assertThat(response.code()).isEqualTo(400);


        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
            ResponseBody body = response.errorBody();
            Converter<ResponseBody, BadRequestBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(BadRequestBody.class, new Annotation[0]);
            BadRequestBody badRequestBody = converter.convert(body);
            assertThat(badRequestBody.getError()).isEqualTo("Bad Request");
        }
    }

}
