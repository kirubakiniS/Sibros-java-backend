package com.dtd.vehiclestackcommunication.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class LogUtil {
    static ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    private LogUtil() {
        //created private constructor to not instantiate this util class
    }

    public static String getMethodName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[2].getMethodName();
    }

    public static List<String> getRequestParameters() {
        // Get request parameters
        Map<String, String[]> parameterMap = attributes.getRequest().getParameterMap();

        // List to store request parameters
        List<String> requestParametersList = new ArrayList<>();

        // Collect request parameters
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();
            StringBuilder paramValueString = new StringBuilder();
            for (String paramValue : paramValues) {
                if (paramValueString.length() > 0) {
                    paramValueString.append(", ");
                }
                paramValueString.append(paramValue);
            }

            // Add to the list instead of logging
            requestParametersList.add(paramName + " = " + paramValueString.toString());
        }

        return requestParametersList;
    }
}
