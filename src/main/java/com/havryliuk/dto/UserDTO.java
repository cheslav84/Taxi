package com.havryliuk.dto;

import com.havryliuk.persistence.model.Role;
import com.havryliuk.util.validators.annotations.PasswordMatches;
import com.havryliuk.util.validators.annotations.ValidEmail;
import com.havryliuk.util.validators.annotations.ValidPassword;
import com.havryliuk.util.validators.annotations.ValidPhone;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Setter
@Getter
@ToString
@PasswordMatches (field = "matchingPassword")
public class UserDTO {

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 20)
    private String surname;

    @NotNull
    @Min(16)
    @Max(120)
    private int age;

    @NotNull
    @NotEmpty
    @ValidEmail(message = "invalid")
    private String email;

    @NotNull
    @NotEmpty
    @ValidPhone
    private String phone;

    @NotNull
    @NotEmpty
    @ValidPassword
    private String password;

    @NotNull
    @NotEmpty
    private String matchingPassword;

    private Role role;

}
