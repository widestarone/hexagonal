package com.hexagonal.domain.user.constant

enum class UserType {
    //  준회원 (가입 절차는 밟았으나 소속 기업이 정식 계약을 하지 않은 경우)
    ASSOCIATE,

    // 정회원 (클론 세일즈 서비스 정식 계약한 기업 소속 회원)
    COMPANY_USER,

    // 스튜디오 회원
    STUDIO_USER,

    // 기업별 관리자 회원
    COMPANY_ADMIN,

    // 클레온 소속의 마스터 관리자
    MASTER_ADMIN,

    NONE,
}
