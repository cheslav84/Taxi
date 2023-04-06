package com.havryliuk.dto;

import com.havryliuk.model.Role;
import com.havryliuk.util.validators.annotations.PasswordMatches;
import com.havryliuk.util.validators.annotations.ValidEmail;
import com.havryliuk.util.validators.annotations.ValidPassword;
import com.havryliuk.util.validators.annotations.ValidPhone;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
@ToString
@PasswordMatches (field = "matchingPassword")
public class UserDto {

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 20)
    private String surname;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    @Min("1923-03-01T08:30")
//    @Max("2010-06-30T16:30")//todo
    private LocalDate birthDate;

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
