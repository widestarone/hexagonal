package com.hexagonal.domain.user.adapter.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.hexagonal.common.entity.BaseEntity
import com.hexagonal.domain.common.constant.IndustryType
import com.hexagonal.domain.company.adapter.entity.CompanyEntity
import com.hexagonal.domain.user.application.port.out.UserUpdateCommand
import com.hexagonal.domain.user.constant.GenderType
import com.hexagonal.domain.user.constant.UserStatus
import com.hexagonal.domain.user.constant.UserType
import com.hexagonal.domain.user.domain.dto.CompanyUser
import com.hexagonal.domain.user.domain.dto.StudioSimpleUser
import com.hexagonal.domain.user.domain.dto.StudioUser
import com.hexagonal.domain.user.domain.dto.SubscribePlan
import com.hexagonal.domain.user.domain.dto.SubscribePlanAdminInfo
import com.hexagonal.domain.user.domain.dto.UserAdminInfo
import com.hexagonal.domain.user.domain.dto.UserSubscribeAdminInfo
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long? = null,

    /**
     * 패스워드
     */
    var password: String,

    /**
     * 성
     */
    var userLastName: String? = null,

    /**
     * 이름. 한국의 경우 성, 이름 모두 이름에 들어감.
     */
    var userFirstName: String,

    /**
     * 회원 타입(준회원, 정회원 - 계약된 기업 소속 회원, 기업 소속 관리자, 클레온 관리자)
     */
    @Enumerated(EnumType.STRING)
    val userType: UserType = UserType.ASSOCIATE,

    /**
     * 회원 상태 (활성, 휴면, 탈퇴, 영구 정지)
     */
    @Enumerated(EnumType.STRING)
    var userStatus: UserStatus = UserStatus.ACTIVE,

    /**
     * 최근 접속일
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var lastLoginAt: LocalDateTime? = null,

    /**
     * 전화 번호
     */
    var phoneNumber: String? = null,

    /**
     * 핸드폰 번호
     */
    var mobileNumber: String? = null,

    /**
     * 이메일
     */
    @Column(unique = true)
    val email: String,

    /**
     * 성별
     */
    @Enumerated(EnumType.STRING)
    val gender: GenderType = GenderType.NONE,

    /**
     * 탈퇴 일자
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var deletedAt: LocalDateTime? = null,
) : BaseEntity() {
    /**
     * 회원정보 업데이트
     * 업데이트 항목은 요구사항에 따라 변경
     */
    fun update(request: UserUpdateCommand): UserEntity {
        userLastName = request.userLastName
        takeUnless { request.userFirstName.isNullOrBlank() }?.let { userFirstName = request.userFirstName }
        phoneNumber = request.phoneNumber
        mobileNumber = request.mobileNumber
        return this
    }

    /**
     * 회원 탈퇴
     * 소프트 삭제함
     */
    fun withdrawal() {
        deletedAt = LocalDateTime.now()
    }

    fun isWithdrawal(): Boolean {
        return deletedAt != null
    }

    fun toStudioSimpleUser(): StudioSimpleUser = StudioSimpleUser(
        id = this.id ?: 0,
        userLastName = this.userLastName,
        userFirstName = this.userFirstName,
        phoneNumber = this.phoneNumber,
        email = this.email,
    )

    fun toStudioUser(): StudioUser = StudioUser(
        id = this.id ?: 0,
        userLastName = this.userLastName,
        userFirstName = this.userFirstName,
        phoneNumber = this.phoneNumber,
        email = this.email,
        language = this.language,
    )
}
