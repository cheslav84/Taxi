package com.havryliuk.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class NewBalanceDto {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "2000.0")
    @Digits(integer=3, fraction=2)
    private BigDecimal rechargeValue;

}
