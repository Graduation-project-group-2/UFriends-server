package com.gachon.ufriendsserver.api.common.controller;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.common.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

public class CommonController {
    public ResponseEntity SuccessReturn(Object data) {
        return ResponseEntity.ok().body(
                ResponseDTO.builder().code(ResponseCode.SUCCESS).data(data).build());
    }

    public ResponseEntity SuccessReturn() {
        return ResponseEntity.ok().body(
                ResponseDTO.builder().code(ResponseCode.SUCCESS).build());
    }

    public ResponseEntity ErrorReturn(int responseCode) {
        return ResponseEntity.ok().body(
                ResponseDTO.builder().code(responseCode).build());
    }
}
