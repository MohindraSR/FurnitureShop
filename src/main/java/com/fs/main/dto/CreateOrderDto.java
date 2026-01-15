package com.fs.main.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateOrderDto {
    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<OrderItemDto> items;
}
