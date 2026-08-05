package com.example.demo.domain.portone.dto.response;

public record PortOneConfigResponse(
        String storeId,
        String channelKey
) {
    public static PortOneConfigResponse of(String storeId, String channelKey) {
        return new PortOneConfigResponse(storeId, channelKey);
    }
}