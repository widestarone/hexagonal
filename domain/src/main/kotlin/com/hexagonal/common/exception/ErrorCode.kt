package com.hexagonal.common.exception

import org.slf4j.event.Level
import org.springframework.http.HttpStatus

/**
 * 공용 기능별 동작 에러 코드
 * 클론세일즈 비즈니스 에러코드와 분리 필요.
 */
enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatusCode: HttpStatus,
    val logLevel: Level = Level.WARN,
) {
    // 공통 (E01000)
    BAD_REQUEST_ERROR("E01000", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ENTITY_ERROR("E01001", "잘못된 요청이 입력되었습니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_ENUM_REQUEST_ERROR("E01002", "데이터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("E01003", "내부 서버 작업 에러", HttpStatus.INTERNAL_SERVER_ERROR),

    // 로그인관련 (E01100)
    BAD_CREDENTIALS_ERROR("E01101", "로그인 실패하였습니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("E01102", "사용자가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_PASSWORD("E01103", "등록할 수 없는 비밀번호입니다..", HttpStatus.BAD_REQUEST),
    USER_EXISTS("E01104", "동일 사용자가 존재 합니다.", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_USER("E01105", "탈퇴한 사용자 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN_ERROR("E01106", "유효하지 않은 토큰 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_REFRESH_TOKEN_ERROR("E01107", "유효하지 않은 refresh token 입니다.", HttpStatus.BAD_REQUEST),
    CREDENTIALS_FORBIDDEN_ERROR("E01108", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    ACCESS_TOKEN_EXPIRED("E01109", "access Token이 만료되었습니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_EXPIRED("E01110", "refresh Token이 만료되었습니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN_USER_ERROR("E01111", "로그인 요청이 불가능한 유저입니다.", HttpStatus.FORBIDDEN),
    PASSWORD_MIS_MATCH("E01112", "틀린 비밀번호입니다.", HttpStatus.BAD_REQUEST),

    // 점유인증 (E01200)
    CERTIFICATION_NOT_FOUND("E01210", "점유인증을 확인할 수 없습니다.", HttpStatus.NOT_FOUND),
    CERTIFICATION_DUPLICATE("E01211", "이미 인증을 완료하였습니다.", HttpStatus.BAD_REQUEST),
    CERTIFICATION_EXPIRED("E01212", "인증번호가 만료되었습니다. 인증번호를 다시 발송해주세요.", HttpStatus.BAD_REQUEST),
    CERTIFICATION_MIS_MATCH("E01213", "인증번호를 다시 확인해주세요.", HttpStatus.BAD_REQUEST),
    CERTIFICATION_EMAIL_SEND_FAIL("E01213", "인증번호 이메일 발송이 실패하였습니다.", HttpStatus.BAD_REQUEST),

    // 유저 (E01300)
    USER_NOT_MATCHED("E01301", "유저정보가 일치하지 않습니다.", HttpStatus.NOT_FOUND),

    END_OF_ERROR("E99999", "오류 입니다.", HttpStatus.BAD_REQUEST),
}
