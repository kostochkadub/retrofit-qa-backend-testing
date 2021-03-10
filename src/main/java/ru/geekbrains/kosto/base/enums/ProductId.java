package ru.geekbrains.kosto.base.enums;

import lombok.Getter;

public enum ProductId {
    PRODUCT_ID_DOES_NOT_EXIST(9999999L);

    @Getter
    private final Long id;

    ProductId(Long id) {
        this.id = id;
    }
}
