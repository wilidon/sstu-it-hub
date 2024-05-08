package ru.sstu.studentprofile.web.exp;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sstu.studentprofile.domain.exception.NotFoundException;
import ru.sstu.studentprofile.domain.exception.UnprocessableEntityException;
import ru.sstu.studentprofile.domain.security.JwtAuthentication;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {
    private static final Logger LOGGER = LogManager.getLogger(ExceptionController.class);

    private void logError(final Exception exception,
                          final JwtAuthentication authentication,
                          final ErrorMessage errorMessage) {
        String message = exception.getClass().getSimpleName() + ":" +
                errorMessage.toString();
        if (authentication != null) {
            message += ";\nUserInfo:" + authentication.toString();
        }
        LOGGER.error(message, exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedException(final Exception exception,
                                                              final HttpServletRequest request,
                                                              final JwtAuthentication authentication) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI());
        logError(exception, authentication, message);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(message);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(final Exception exception,
                                                          final HttpServletRequest request,
                                                          final JwtAuthentication authentication) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI());
        logError(exception, authentication, message);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(message);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorMessage> unprocessableEntityException(final Exception exception,
                                                                    final HttpServletRequest request,
                                                                    final JwtAuthentication authentication) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI());
        logError(exception, authentication, message);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(message);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> runtimeException(final Exception exception,
                                                          final HttpServletRequest request,
                                                          final JwtAuthentication authentication) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI());
        logError(exception, authentication, message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }
}

