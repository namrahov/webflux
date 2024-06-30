package com.ahmed.config;

import com.ahmed.service.RequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private final RequestHandler requestHandler;

    @Bean
    public RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions
                .route()
                .GET("/router/square/{input}", requestHandler::squareHandler)
                .GET("/router/table/{input}", requestHandler::tableHandler)
                .GET("/router/stream-table/{input}", requestHandler::streamTableHandler)
                .POST("/router/multiply", requestHandler::multiplyHandler)
                .build();
    }
}
