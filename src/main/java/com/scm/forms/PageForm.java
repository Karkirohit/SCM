package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class PageForm {

    @NotBlank(message = "default blank")
    private String name;

    @NotBlank(message = "Enter a email")
    @Email(message = "Enter a email")
    private String email;

    @NotBlank(message = "Pasword is not empty")
    private String password;

    @Size(min = 8, max = 12,message = "Enter valid Number")
    private String phoneNumber;

    @NotBlank(message = "Blank about")
    private String about;

}
