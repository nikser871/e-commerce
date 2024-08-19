package com.shopme.admin.util;

import com.shopme.common.entity.Category;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Util {

    public static Category copyFull(Category category) {
        return Category.builder()
                .id(category.getId())
                .name(category.getName())
                .alias(category.getAlias())
                .image(category.getImage())
                .enabled(category.getEnabled())
                .build();
    }

    public static Category copyFullWithName(Category category, String name) {
        return Category.builder()
                .id(category.getId())
                .name(name)
                .alias(category.getAlias())
                .image(category.getImage())
                .enabled(category.getEnabled())
                .build();
    }
}
