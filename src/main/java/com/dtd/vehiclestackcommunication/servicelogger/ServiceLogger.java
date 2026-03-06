package com.dtd.vehiclestackcommunication.servicelogger;

import com.dtd.vehiclestackcommunication.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceLogger {

    private final Logger logger = LoggerFactory.getLogger(ServiceLogger.class);

    private long startTime;

    public void logRequest(String methodName) {
        startTime = System.currentTimeMillis();
        String currentDateTimes = DateUtil.getCurrentTimeStamp();
        logger.info("Request received - MethodName: {}, DateTime: {}", methodName, currentDateTimes);
    }

    public void logRequest(String methodName, String requestBody) {
        startTime = System.currentTimeMillis();
        String currentDateTimes = DateUtil.getCurrentTimeStamp();
        logger.info("Request received - MethodName: {}, DateTime: {}, RequestBody: {}", methodName, currentDateTimes, requestBody);
    }

    public void logResponse(String methodName) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String currentDateTimes = DateUtil.getCurrentTimeStamp();
        logger.info("Request completed - MethodName: {}, Time Taken: {} ms, DateTime: {}", methodName, duration, currentDateTimes);
    }

    public void logResponse(String methodName, String responseString) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String currentDateTimes = DateUtil.getCurrentTimeStamp();
        logger.info("Request completed - MethodName: {}, Time Taken: {} ms, DateTime: {}, ResponseBody: {}", methodName, duration, currentDateTimes, responseString);
    }

    public void logRequest(String httpRequestType, String methodName, String path, List<String> requestParams) {
        startTime = System.currentTimeMillis();
        String currentDateTimes = DateUtil.getCurrentTimeStamp();
        logger.info("Request received - HttpRequestType: {}, MethodName: {}, Path: {}, DateTime: {}, RequestParams: {}", httpRequestType, methodName, path, currentDateTimes, requestParams);
    }

    public void logRequest(String httpRequestType, String methodName, String path, List<String> requestParams, String requestBody) {
        startTime = System.currentTimeMillis();
        String currentDateTimes = DateUtil.getCurrentTimeStamp();
        logger.info("Request received - HttpRequestType: {}, MethodName: {}, Path: {}, DateTime: {}, RequestParams: {}, RequestBody: {}", httpRequestType, methodName, path, currentDateTimes, requestParams, requestBody);
    }

    public void logResponse(String httpRequestType, String methodName, String path, String responseBody) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String currentDateTimes = DateUtil.getCurrentTimeStamp();
        logger.info("Request completed - HttpRequestType: {}, MethodName: {}, Path: {}, Time Taken: {} ms, DateTime: {}, ResponseBody: {}", httpRequestType, methodName, path, duration, currentDateTimes, responseBody);
    }
}