package com.travelbud.advices;

import java.nio.file.AccessDeniedException;

import javax.persistence.EntityNotFoundException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.travelbud.dto.ApiError;
import com.travelbud.errors.CommonErrorMessages;

import javassist.bytecode.DuplicateMemberException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

   @Override
   protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       String error = "Malformed JSON request";
       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
   }

   private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
       return new ResponseEntity<>(apiError, apiError.getStatus());
   }

   //other exception handlers below
   
   @ExceptionHandler(EntityNotFoundException.class)
   protected ResponseEntity<Object> handleEntityNotFound(
           EntityNotFoundException ex, WebRequest request) {
	   ex.printStackTrace();
       /*ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
       apiError.setMessage(ex.getMessage());*/
       return handleExceptionInternal(ex, getErrorJson(ex.getMessage()), 
    	          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
       //return buildResponseEntity(apiError);
   }
   
   @ExceptionHandler(AccessDeniedException.class)
   protected ResponseEntity<Object> handleAccessDenied(
		   AccessDeniedException ex, WebRequest request) {
	   ex.printStackTrace();
       /*ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
       apiError.setMessage(ex.getMessage());
       return buildResponseEntity(apiError);*/

       return handleExceptionInternal(ex, getErrorJson(ex.getMessage()), 
    	          new HttpHeaders(), HttpStatus.FORBIDDEN, request);
   }
   
   @ExceptionHandler(DuplicateMemberException.class)
   protected ResponseEntity<Object> handleDuplicateMemberException(
		   DuplicateMemberException ex, WebRequest request) {
	   ex.printStackTrace();
       /*ApiError apiError = new ApiError(HttpStatus.CONFLICT);
       apiError.setMessage(ex.getMessage());
       return buildResponseEntity(apiError);*/
       return handleExceptionInternal(ex, getErrorJson(ex.getMessage()), 
 	          new HttpHeaders(), HttpStatus.CONFLICT, request);
   }

   @ExceptionHandler(Exception.class)
   protected ResponseEntity<Object> handleException(
		   Exception ex) {
	   ex.printStackTrace();
       /*ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
       apiError.setMessage(CommonErrorMessages.COMMON_ERROR);
       return buildResponseEntity(apiError);*/
       return handleExceptionInternal(ex, getErrorJson(CommonErrorMessages.COMMON_ERROR), 
 	          new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, null);
   }
   
   private String getErrorJson(String error) {
	   return "{\"error\":\""+error+"\"}";
   }
   
}
