package com.dtd.vehiclestackcommunication.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonInclude(value = Include.NON_EMPTY)
public class Response<T> {

    private Integer status;
    private T data;
    private String message;
    private Boolean error;
    @JsonProperty(value = "Vehicle_Parameters")
    private Map<String, Object> vehicleInfo;
    private Map<String, Object> dtc;

    /**
     * Constructor of List Response which can invoke the method directly to activity
     *
     * @param status  get the status of processing
     * @param message get the message in string format
     * @param error   get the error value of status
     */
    public Response(Integer status, String message, Boolean error, Map<String, Object> vehicleInfo) {
        super();
        this.status = status;
        this.vehicleInfo = vehicleInfo;
        this.message = message;
        this.error = error;
    }

    /**
     * Constructor of List Response which can invoke the method directly to activity
     *
     * @param data get data of constructor
     */
    public Response(T data) {
        super();
        this.data = data;
    }

    /**
     * Constructor of List Response which can invoke the method directly to activity
     */
    public Response() {
    }

}
