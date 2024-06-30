package com.ahmed.service;

import com.ahmed.dto.MultiplyDto;
import com.ahmed.dto.ResponseDto;
import com.ahmed.exception.InputValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class RequestHandler {

    private final ReactiveMathService reactiveMathService;
    private final WebClient webClient;

    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));

        if(input < 10 || input > 20) {
            return Mono.error(new InputValidationException(input));
        }

        Mono<ResponseDto> responseDtoMono = reactiveMathService.findSquare(input);

        webClient
                .get()
                .uri("/square/{input}", 10)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(String.class).flatMap(errorMessage -> {
                            return Mono.error(new Exception(errorMessage));
                        })
                )
                .bodyToMono(ResponseDto.class)
                .subscribe(responseDto -> {
                    System.out.println("responseDto=" + responseDto);
                }, throwable -> {
                    if (throwable instanceof WebClientResponseException) {
                        System.out.println("Error Status Code: " + ((WebClientResponseException) throwable).getStatusCode());
                    }
                    System.out.println("Error: " + throwable.getMessage());
                    throwable.printStackTrace();
                });

        return ServerResponse.ok().body(responseDtoMono, ResponseDto.class);
    }

    public Mono<ServerResponse> tableHandler(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));
        Flux<ResponseDto> responseDtoFlux = reactiveMathService.multiplicationTable(input);

        return ServerResponse.ok().body(responseDtoFlux, ResponseDto.class);
    }

    public Mono<ServerResponse> streamTableHandler(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));
        Flux<ResponseDto> responseDtoFlux = reactiveMathService.multiplicationTable(input);

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseDtoFlux, ResponseDto.class);
    }

    public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
        Mono<MultiplyDto> multiplyDtoMono = serverRequest.bodyToMono(MultiplyDto.class);
        Mono<ResponseDto> responseDtoMono = reactiveMathService.multiplyPostTable(multiplyDtoMono);

        return ServerResponse.ok()
                .body(responseDtoMono, ResponseDto.class);
    }
}
