package com.ahmed.client;

import com.ahmed.dto.MultiplyDto;
import com.ahmed.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class CandidateClient {
    private final WebClient webClient;

    public ResponseDto findSquare(int input) {
        AtomicReference<ResponseDto> dto = new AtomicReference<>(new ResponseDto());

        webClient
                .get()
                .uri("http://localhost:2002/square/{input}", input)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(String.class).flatMap(errorMessage -> {
                            return Mono.error(new Exception(errorMessage));
                        })
                )
                .bodyToMono(ResponseDto.class)
                .subscribe(responseDto -> {
                    dto.set(responseDto);
                    System.out.println("responseDto=" + responseDto);
                }, throwable -> {
                    if (throwable instanceof WebClientResponseException) {
                        System.out.println("Error Status Code: " + ((WebClientResponseException) throwable).getStatusCode());
                    }
                    System.out.println("Error: " + throwable.getMessage());
                    throwable.printStackTrace();
                });


        return dto.get();
    }

    public Mono<List<ResponseDto>> findSquareList(int input) {
        return webClient
                .get()
                .uri("http://localhost:2002/square/list/{input}", input)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(String.class).flatMap(errorMessage -> {
                            return Mono.error(new Exception(errorMessage));
                        })
                )
                .bodyToFlux(ResponseDto.class)
                .collectList()
                .flatMap(Mono::just); // Example of chaining with another async operation
    }

    public ResponseDto postSquare(MultiplyDto dto) {
        AtomicReference<ResponseDto> atomicReference = new AtomicReference<>(new ResponseDto());
        webClient
                .post()
                .uri("http://localhost:2002/")
                .bodyValue(dto)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(String.class).flatMap(errorMessage -> {
                            return Mono.error(new Exception(errorMessage));
                        })
                )
                .bodyToMono(ResponseDto.class)
                .subscribe(responseDto -> {
                    atomicReference.set(responseDto);
                    System.out.println("responseDto=" + responseDto);
                }, throwable -> {
                    if (throwable instanceof WebClientResponseException) {
                        System.out.println("Error Status Code: " + ((WebClientResponseException) throwable).getStatusCode());
                    }
                    System.out.println("Error: " + throwable.getMessage());
                    //throwable.printStackTrace();
                });

        return atomicReference.get();

    }


}
