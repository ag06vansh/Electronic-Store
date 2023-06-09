package com.shopping.electronic.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @Size(min = 3, max = 25, message = "Invalid Name!")
    private String name;

    @Email(message = "Invalid User Email!")
    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "Password is required!")
    private String password;

    @Size(min = 4, message = "Invalid Gender")
    private String gender;

    @NotBlank(message = "Write something about yourself!")
    private String about;

    private String imageName;
}
