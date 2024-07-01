package com.ahmed.controller.exceptionHandler;

import com.ahmed.client.CandidateClient;
import com.ahmed.dto.MultiplyDto;
import com.ahmed.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

    private final CandidateClient candidateClient;

    @GetMapping
    public void ada() {
        MultiplyDto dto = new MultiplyDto();
        dto.setFirst(3);
        dto.setSecond(4);

        ResponseDto dto1 = candidateClient.postSquare(dto);
        System.out.println("dto1="+dto1);
    }
}
