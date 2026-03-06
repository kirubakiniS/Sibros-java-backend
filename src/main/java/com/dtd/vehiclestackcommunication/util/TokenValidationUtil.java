package com.dtd.vehiclestackcommunication.util;

import com.dtd.vehiclestackcommunication.exception.InvalidParameterException;

public class TokenValidationUtil {
    private static final String ACCESS_REQUIRED = "Access token is required ";
    private static final String BEARER = "Bearer ";
    private static final String NO_BEARER = "Bearer keyword missing in token";

    private TokenValidationUtil() {
//        created private constructor to not instantiate this util class
    }

    public static void validateAccessToken(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new InvalidParameterException(ACCESS_REQUIRED);
        }
        if (!accessToken.startsWith(BEARER)) {
            throw new InvalidParameterException(NO_BEARER);
        }
    }
}
