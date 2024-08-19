package com.shopme.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "user-photos";
        String categoryImageDir = "../category-images";

        Path userPhotosDir = Paths.get(dirName);
        Path categoryImagesDir = Paths.get(categoryImageDir);

        String userPhotosPath = userPhotosDir.toAbsolutePath().toString();
        String categoryImagesPath = categoryImagesDir.toAbsolutePath().toString();

        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");

        registry.addResourceHandler(categoryImageDir.substring(2) + "/**")
                .addResourceLocations("file:/" + categoryImagesPath + "/");
    }
}
