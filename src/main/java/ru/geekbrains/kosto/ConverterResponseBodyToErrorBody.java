package ru.geekbrains.kosto;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import ru.geekbrains.kosto.dto.BadRequestBody;
import ru.geekbrains.kosto.dto.ErrorBody;
import ru.geekbrains.kosto.util.RetrofitUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;

public class ConverterResponseBodyToErrorBody {


    public static ErrorBody getErrorBody(Response<?> response) {
        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
            ResponseBody body = response.errorBody();
            Converter<ResponseBody, ErrorBody> converter = null;
            try {
                converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                ErrorBody errorBody = converter.convert(body);
                return errorBody;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
