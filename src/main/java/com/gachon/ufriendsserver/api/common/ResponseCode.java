package com.gachon.ufriendsserver.api.common;

import org.springframework.http.ResponseEntity;

public enum ResponseCode {

    SUCCESS(API.CODE.SUCCESS, "SUCCESS"),

    PARAM_ERROR(API.CODE.PARAM_ERROR, "PARAM_ERROR"),

    DB_ERROR(API.CODE.DB_ERROR, "DB_ERROR"),

    NOT_FOUND_DATA(API.CODE.NOT_FOUND_DATA, "NOT_FOUND_DATA"),

    DUPLICATE_DATA(API.CODE.DUPLICATE_DATA, "DUPLICATE_DATA"),

    NAVER_LOGIN_ERROR(API.CODE.NAVER_LOGIN_ERROR, "NAVER_LOGIN_ERROR"),

    UNKNOWN_ERROR(API.CODE.UNKNOWN_ERROR, "UNKNOWN_ERROR");

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static ResponseCode CODE(int intCode) {
        ResponseCode responseCode = null;
        for (ResponseCode code : ResponseCode.values()) {
            if (intCode == code.getCode()) {
                responseCode = code;
                break;
            }
        }

        if (responseCode == null) {
            responseCode = ResponseCode.SUCCESS;
        }

        return responseCode;
    }

}
