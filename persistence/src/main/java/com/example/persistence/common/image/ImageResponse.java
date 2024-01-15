package com.example.persistence.common.image;

public record ImageResponse(
        Long id,
        String url,
        int width,
        int height
) {
}
