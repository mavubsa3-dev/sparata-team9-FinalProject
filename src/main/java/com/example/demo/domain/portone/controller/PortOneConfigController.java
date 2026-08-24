package com.example.demo.domain.portone.controller;

import com.example.demo.common.config.PortOneProperties;
import com.example.demo.domain.portone.dto.response.PortOneConfigResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PortOneConfigController {

    private final PortOneProperties portOneProperties;

    @GetMapping("/api/config/portone")
    public ResponseEntity<PortOneConfigResponse> getConfig() {
        PortOneConfigResponse response = PortOneConfigResponse.of(
                portOneProperties.getStoreId(),
                portOneProperties.getChannelKey()
        );
        return ResponseEntity.ok(response);
    }
}