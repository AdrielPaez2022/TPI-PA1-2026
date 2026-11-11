package ar.edu.undec.adapter.handler;

import empleado.exception.DatosEmpleadoInvalidosException;
import empleado.exception.EmpleadoNoEncontradoException;
import empleado.exception.LegajoDuplicadoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DatosEmpleadoInvalidosException.class)
    public ResponseEntity<ApiError> manejarDatosInvalidos(
            DatosEmpleadoInvalidosException exception, HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(LegajoDuplicadoException.class)
    public ResponseEntity<ApiError> manejarLegajoDuplicado(
            LegajoDuplicadoException exception, HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, exception, request);
    }

    @ExceptionHandler(EmpleadoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarEmpleadoNoEncontrado(
            EmpleadoNoEncontradoException exception, HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarSolicitudMalformada(
            HttpMessageNotReadableException exception, HttpServletRequest request) {
        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "El cuerpo de la solicitud no tiene un formato válido",
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }

    private ResponseEntity<ApiError> respuesta(
            HttpStatus status, RuntimeException exception, HttpServletRequest request) {
        ApiError error = new ApiError(
                Instant.now(), status.value(), status.getReasonPhrase(),
                exception.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }
}
