package com.ahmed.service;

import com.ahmed.client.CandidateClient;
import com.ahmed.dto.MultiplyDto;
import com.ahmed.dto.ResponseDto;
import com.ahmed.exception.InputValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RequestHandler {

    private final ReactiveMathService reactiveMathService;
    private final CandidateClient candidateClient;


    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));

        if (input < 10 || input > 20) {
            return Mono.error(new InputValidationException(input));
        }

        Mono<ResponseDto> responseDtoMono = reactiveMathService.findSquare(input);

        List<ResponseDto> list = new ArrayList<>();

        candidateClient.findSquareList(input)
                .doOnNext(list::addAll)
                .doOnError(error -> {
                    System.err.println("Error occurred: " + error.getMessage());
                })
                .doFinally(signal -> {
                    // Print size after the processing is done
                    System.out.println("size=" + list.size());
                })
                .subscribe();

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
