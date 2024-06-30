package com.ahmed.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseDto {
    private Date data = new Date();
    private int output;

    public ResponseDto(int output) {
        this.output = output;
    }
}
