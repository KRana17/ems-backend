package com.example.ems.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void testExceptionAnnotation() {
        ResponseStatus annotation = ResourceNotFoundException.class.getAnnotation(ResponseStatus.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testExceptionIsRuntimeException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}