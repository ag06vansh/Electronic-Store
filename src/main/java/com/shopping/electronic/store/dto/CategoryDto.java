package com.shopping.electronic.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @NotBlank(message = "Category Title is required !!!")
    @Size(min = 4, message = "Category title must be of min 4 characters !!!")
    private String title;

    @NotBlank(message = "Category description required !!!")
    private String description;

    private String coverImage;
}
