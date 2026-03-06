package com.dtd.vehiclestackcommunication.exception;

import com.dtd.vehiclestackcommunication.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * This Container class used to catch the Exceptions arise from the code. And to
 * Handle those Exceptions with proper error message and error code.
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Bad data exception response.
     *
     * @param ex the ex
     * @return the response
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadDataException.class)
    public Response<String> badDataException(BadDataException ex) {
        LOGGER.error(ex.getLocalizedMessage());
        return setStatusAndMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * No data found exception response.
     *
     * @param ex the ex
     * @return the response
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NoDataFoundException.class)
    public Response<String> noDataFoundException(NoDataFoundException ex) {
        LOGGER.error(ex.getLocalizedMessage());
        return setStatusAndMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * Common exception response.
     *
     * @param ex the ex
     * @return the response
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = CommonException.class)
    public Response<String> commonException(CommonException ex) {
        LOGGER.error(ex.getLocalizedMessage());
        return setStatusAndMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * Invalid parameter exception response.
     *
     * @param ex the ex
     * @return the response
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InvalidParameterException.class)
    public Response<String> invalidParameterException(InvalidParameterException ex) {
        LOGGER.error(ex.getLocalizedMessage());
        return setStatusAndMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * User un authorized exception response.
     *
     * @param ex the ex
     * @return the response
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = UserUnAuthorizedException.class)
    public Response<String> userUnAuthorizedException(UserUnAuthorizedException ex) {
        LOGGER.error(ex.getLocalizedMessage());
        return setStatusAndMessage(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    private Response<String> setStatusAndMessage(int status, String message) {
        Response<String> responseMap = new Response<>();
        responseMap.setStatus(status);
        responseMap.setData("");
        responseMap.setMessage(message);
        return responseMap;
    }

}
