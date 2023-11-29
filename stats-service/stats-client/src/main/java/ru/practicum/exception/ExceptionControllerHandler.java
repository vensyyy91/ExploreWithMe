package ru.practicum.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleHttpClientErrorException(HttpClientErrorException.BadRequest ex) {
        Response response;
        try {
            response = objectMapper.readValue(ex.getResponseBodyAsString(), Response.class);
        } catch (JsonProcessingException e) {
            response = new Response(ex.getMessage());
        }
        log.error("Статус 400 BAD REQUEST: {}", response.getError(), ex);

        return response;
    }
}