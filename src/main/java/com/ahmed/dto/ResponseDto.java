package com.ahmed.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ResponseDto {
    private Date data = new Date();
    private int output;

    public ResponseDto(int output) {
        this.output = output;
    }
}
