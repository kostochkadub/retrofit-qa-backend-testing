package ru.geekbrains.kosto.base.enums;

import lombok.Getter;

public enum CategoryType {
    FOOD(1, "Food"),
    ELECTRONICS(2, "Electronic"),
    DOESNOTEXIST(9999999, "DOES NOT EXIST");

    @Getter
    private final Integer id;
    @Getter
    private final String title;

    CategoryType(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
