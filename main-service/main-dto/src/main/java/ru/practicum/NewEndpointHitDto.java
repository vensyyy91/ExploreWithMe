package ru.practicum;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewEndpointHitDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}