 package com.carlosrepo.SistemaEcomers.Config;

import com.carlosrepo.SistemaEcomers.Common.exception.BusinessException;
import com.carlosrepo.SistemaEcomers.Login.Dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

 @RestControllerAdvice
 public class GlobalExceptionHandler {

     /* ============================
        ERRORES DE NEGOCIO (400)
        ============================ */
     @ExceptionHandler(BusinessException.class)
     public ResponseEntity<ErrorResponse> handleBusiness(
             BusinessException ex,
             HttpServletRequest request) {

         ErrorResponse error = new ErrorResponse(
                 HttpStatus.BAD_REQUEST.value(),
                 ex.getMessage(),
                 request.getRequestURI()
         );

         return ResponseEntity.badRequest().body(error);
     }

     /* ============================
        ERRORES DE VALIDACIÓN (400)
        ============================ */
     @ExceptionHandler(MethodArgumentNotValidException.class)
     public ResponseEntity<ErrorResponse> handleValidation(
             MethodArgumentNotValidException ex,
             HttpServletRequest request) {

         String message = ex.getBindingResult()
                 .getFieldErrors()
                 .stream()
                 .map(err -> err.getField() + ": " + err.getDefaultMessage())
                 .findFirst()
                 .orElse("Datos inválidos");

         ErrorResponse error = new ErrorResponse(
                 HttpStatus.BAD_REQUEST.value(),
                 message,
                 request.getRequestURI()
         );

         return ResponseEntity.badRequest().body(error);
     }

     /* ============================
        AUTENTICACIÓN (401)
        ============================ */
     @ExceptionHandler(AuthenticationException.class)
     public ResponseEntity<ErrorResponse> handleAuthentication(
             AuthenticationException ex,
             HttpServletRequest request) {

         ErrorResponse error = new ErrorResponse(
                 HttpStatus.UNAUTHORIZED.value(),
                 "Credenciales inválidas",
                 request.getRequestURI()
         );

         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
     }

     /* ============================
        AUTORIZACIÓN (403)
        ============================ */
     @ExceptionHandler(AccessDeniedException.class)
     public ResponseEntity<ErrorResponse> handleAccessDenied(
             AccessDeniedException ex,
             HttpServletRequest request) {

         ErrorResponse error = new ErrorResponse(
                 HttpStatus.FORBIDDEN.value(),
                 "No tienes permisos para esta acción",
                 request.getRequestURI()
         );

         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
     }

     /* ============================
        ERRORES TÉCNICOS (500)
        ============================ */
     @ExceptionHandler(Exception.class)
     public ResponseEntity<ErrorResponse> handleGeneric(
             Exception ex,
             HttpServletRequest request) {

         ErrorResponse error = new ErrorResponse(
                 HttpStatus.INTERNAL_SERVER_ERROR.value(),
                 "Error interno del servidor",
                 request.getRequestURI()
         );

         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
     }
 }
