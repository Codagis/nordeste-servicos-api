package com.ordem.servico.api.exception;

import com.ordem.servico.api.exception.error.Erro;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> "Campo '" + fieldError.getField() + "': " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        String mensagemUsuario = "Erro de validação nos dados da requisição. Verifique os campos.";
        String mensagemTecnica = String.join("; ", erros);

        Erro erro = new Erro(
                status.value(),
                LocalDateTime.now(),
                mensagemTecnica,
                mensagemUsuario
        );

        return new ResponseEntity<>(erro, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        Erro erro = new Erro(
                status.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                "Ocorreu um erro durante o processamento da requisição"
        );

        return new ResponseEntity<>(erro, headers, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Erro> handleAccessDeniedException(AccessDeniedException ex) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Erro> handleAuthenticationException(AuthenticationException ex) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Erro> handleResponseStatusException(ResponseStatusException ex) {
        return buildErrorResponse(ex, (HttpStatus) ex.getStatusCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Erro> handleRuntimeException(RuntimeException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Erro> handleAllExceptions(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Erro> buildErrorResponse(Exception ex, HttpStatus status) {
        String mensagemUsuario = ex.getMessage() != null && !ex.getMessage().isEmpty()
                ? ex.getMessage()
                : "Ocorreu um erro durante a operação";

        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            mensagemUsuario = "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.";
        }

        Erro erro = new Erro(
                status.value(),
                LocalDateTime.now(),
                ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                mensagemUsuario
        );

        return new ResponseEntity<>(erro, status);
    }
}