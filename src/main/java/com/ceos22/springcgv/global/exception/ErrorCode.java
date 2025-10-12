package com.ceos22.springcgv.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // COMMON ERROR 1xxx
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_1101", "잘못된 요청입니다."),
    JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "COMMON_1102", "요청 본문을 파싱할 수 없습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON_1103", "입력값 검증에 실패했습니다."),
    DUPLICATE_RESOURCE(HttpStatus.BAD_REQUEST, "COMMON_1104", "이미 존재하는 리소스입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_1105", "리소스를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, "COMMON_1106", "리소스 상태 충돌이 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_1007", "서버 내부 오류가 발생했습니다."),


    // AUTH ERROR 2xxx
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_2101", "인증이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH_2102", "접근 권한이 없습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_2103", "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_2104", "유효하지 않은 토큰입니다."),


    // CINEMA ERROR 3xxx
    CINEMA_NO_REGION(HttpStatus.BAD_REQUEST, "CINEMA_3101", "지역 파라미터가 넘겨지지 않았습니다."),

    // SNACK ERROR 4xxx
    SNACK_NO_STOCK(HttpStatus.BAD_REQUEST, "SNACK_4101", "매점 재고가 부족합니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.httpStatus = status;
        this.code = code;
        this.message = message;
    }
}