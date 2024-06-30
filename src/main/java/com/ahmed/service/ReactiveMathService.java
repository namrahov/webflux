package com.ahmed.service;

import com.ahmed.dto.MultiplyDto;
import com.ahmed.dto.ResponseDto;
import com.ahmed.util.SleepUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveMathService {

    public Mono<ResponseDto> findSquare(int input) {
        return Mono.fromSupplier(() -> input * input)
                .map(ResponseDto::new);
    }

    public Flux<ResponseDto> multiplicationTable(int input) {
        return Flux.range(1, 10)
                .doOnNext(i -> SleepUtil.sleepSecond(1))
                .doOnNext(i -> System.out.println("processing = " + i))
                .map(i -> new ResponseDto(i * input));
    }

    public Mono<ResponseDto> multiplyPostTable(Mono<MultiplyDto> multiplyDtoMono) {
        return multiplyDtoMono
                .map(dto -> dto.getFirst() * dto.getSecond())
                .map(ResponseDto::new);
    }

}
