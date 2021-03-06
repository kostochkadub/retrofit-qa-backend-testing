package ru.geekbrains.kosto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BadRequestBody {
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("error")
    private String error;
    @JsonProperty("message")
    private String message;
    @JsonProperty("path")
    private String path;
}
