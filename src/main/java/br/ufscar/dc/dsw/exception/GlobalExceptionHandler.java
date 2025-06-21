package br.ufscar.dc.dsw.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.warn("Recurso não encontrado: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/error-404");
        mav.addObject("statusCode", HttpStatus.NOT_FOUND.value());
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("Tentativa de acesso negado: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/error-403");
        mav.addObject("statusCode", HttpStatus.FORBIDDEN.value());
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleIllegalStateException(IllegalStateException ex) {
        logger.warn("Requisição inválida: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/error-400");
        mav.addObject("statusCode", HttpStatus.BAD_REQUEST.value());
        mav.addObject("message", ex.getMessage());
        return mav;
    }
}