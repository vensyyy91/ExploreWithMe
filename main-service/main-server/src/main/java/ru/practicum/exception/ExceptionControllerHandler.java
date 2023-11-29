package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ApiError;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class,
            IllegalArgumentException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(Exception ex) {
        log.error("Статус 400 BAD REQUEST: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status("BAD_REQUEST")
                .reason("Incorrectly made request.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlePSQLException(PSQLException ex) {
        log.error("Статус 409 CONFLICT: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status("CONFLICT")
                .reason("Integrity constraint has been violated.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Статус 409 CONFLICT: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status("CONFLICT")
                .reason("Integrity constraint has been violated.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleIllegalOperationException(IllegalOperationException ex) {
        log.error("Статус 409 CONFLICT: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status("CONFLICT")
                .reason("For the requested operation the conditions are not met.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException ex) {
        log.error("Статус 404 NOT FOUND: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status("NOT_FOUND")
                .reason("The required object was not found.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}