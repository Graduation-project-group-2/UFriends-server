package com.gachon.ufriendsserver.api.common.controller;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.common.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

public class CommonController {

    public ResponseEntity SuccessReturn(Object data) {
        return ResponseEntity.ok().body(
                ResponseDTO.builder().code(ResponseCode.SUCCESS.getCode()).msg(ResponseCode.SUCCESS.getMsg()).data(data).build());
    }

    public ResponseEntity SuccessReturn() {
        return ResponseEntity.ok().body(
                ResponseDTO.builder().code(ResponseCode.SUCCESS.getCode()).msg(ResponseCode.SUCCESS.getMsg()).build());
    }

    public ResponseEntity ErrorReturn(ResponseCode responseCode) {
        return ResponseEntity.ok().body(
                ResponseDTO.builder().code(responseCode.getCode()).msg(responseCode.getMsg()).build());
    }
}
