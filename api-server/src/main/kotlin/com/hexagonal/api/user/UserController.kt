package com.hexagonal.api.user

import com.hexagonal.common.auth.application.port.`in`.LoginCommand
import com.hexagonal.common.auth.application.port.`in`.RefreshTokenCommand
import com.hexagonal.common.auth.domain.dto.Token
import com.hexagonal.common.exception.ServiceException
import com.hexagonal.domain.user.application.port.`in`.UserAutoLogInCommand
import com.hexagonal.domain.user.application.port.`in`.UserEmailCommand
import com.hexagonal.domain.user.application.port.`in`.UserPasswordModifyCommand
import com.hexagonal.domain.user.application.port.`in`.UserUseCase
import com.hexagonal.domain.user.application.port.out.UserPasswordUpdateResult
import com.hexagonal.domain.user.domain.dto.UserLoginInfo
import com.hexagonal.domain.user.domain.dto.UserSignUp
import com.hexagonal.common.annotation.LoginUser
import com.hexagonal.common.annotation.SuccessApiResponse
import com.hexagonal.common.response.ErrorResponse
import com.hexagonal.domain.user.domain.dto.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Tag(name = "유저")
@RestController
@RequestMapping("api/v1")
@SuccessApiResponse
@Validated
class UserController(
    private val userUseCase: UserUseCase,
) {
//    @Operation(summary = "회원 가입")
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "200",
//            ),
//            ApiResponse(
//                responseCode = "422",
//                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
//            ),
//            ApiResponse(
//                responseCode = "500",
//                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
//            ),
//        ],
//    )
//    @PostMapping("/user/signup")
//    fun signUp(@Valid @RequestBody request: StudioUserCommand): com.hexagonal.domain.common.dto.ApiResponse<UserSignUp> = try {
//        com.hexagonal.domain.common.dto.ApiResponse(
//            success = true,
//            data = userUseCase.signUpStudioUser(request),
//        )
//    } catch (e: ServiceException) {
//        com.hexagonal.domain.common.dto.ApiResponse(
//            errorCode = e.errorCode.code,
//            message = e.errorCode.message,
//        )
//    } catch (e: Exception) {
//        com.hexagonal.domain.common.dto.ApiResponse()
//    }

//    @Operation(summary = "회원 조회")
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "200",
//            ),
//            ApiResponse(
//                responseCode = "422",
//                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
//            ),
//            ApiResponse(
//                responseCode = "500",
//                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
//            ),
//        ],
//    )
//    @GetMapping("/user/{id}")
//    fun findUser(@PathVariable id: Long, @LoginUser user: User): com.hexagonal.domain.common.dto.ApiResponse<User> {
//        return userUseCase.findStudioUser(id, user)
//    }

//    @Operation(summary = "Simple 회원 조회")
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "200",
//            ),
//            ApiResponse(
//                responseCode = "422",
//                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
//            ),
//            ApiResponse(
//                responseCode = "500",
//                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
//            ),
//        ],
//    )
//    @GetMapping("/user/simple/{id}")
//    fun findSimpleUser(@PathVariable id: Long, @LoginUser user: StudioSimpleUser): com.hexagonal.domain.common.dto.ApiResponse<StudioSimpleUser> {
//        return userUseCase.findStudioSimpleUser(id, user)
//    }

    @Operation(summary = "회원 비밀번호 재설정")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
            ),
            ApiResponse(
                responseCode = "422",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    @PatchMapping("/user/password")
    fun updateUserPassword(@RequestBody request: UserPasswordModifyCommand): UserPasswordUpdateResult {
        return userUseCase.updateUserPassword(request)
    }

//    /**
//     * 회원 탈퇴
//     */
//    @Operation(summary = "회원 탈퇴")
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "200",
//            ),
//            ApiResponse(
//                responseCode = "422",
//                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
//            ),
//            ApiResponse(
//                responseCode = "500",
//                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
//            ),
//        ],
//    )
//    @DeleteMapping("/user/withdrawal")
//    fun withdrawalWithReason(@Valid @RequestBody request: WithdrawalCommand, @LoginUser user: StudioSimpleUser): WithdrawalResult {
//        return WithdrawalResult(
//            result = userUseCase.withdrawalWithReason(request, user.id, user.email),
//        )
//    }

//    @Operation(summary = "회원 수정")
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "200",
//            ),
//            ApiResponse(
//                responseCode = "422",
//                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
//            ),
//            ApiResponse(
//                responseCode = "500",
//                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
//            ),
//        ],
//    )
//    @PutMapping("/user")
//    fun updateUser(@RequestBody request: StudioUserModifyCommand, @LoginUser user: StudioSimpleUser):
//        com.hexagonal.domain.common.dto.ApiResponse<StudioSimpleUser> {
//        return userUseCase.updateStudioUser(request, user)
//    }

    @Operation(summary = "회원 가입 전 이메일 중복 여부 확인")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
            ),
            ApiResponse(
                responseCode = "422",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    @GetMapping("/user/email")
    fun checkUserEmail(@RequestParam(name = "email") email: String): Boolean {
        return userUseCase.checkAvailableUserEmail(UserEmailCommand(email = email))
    }

    @Operation(summary = "회원 로그인")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
            ),
            ApiResponse(
                responseCode = "422",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    @PostMapping("/user/login")
    fun userLogin(@Valid @RequestBody request: LoginCommand): ResponseEntity<UserLoginInfo> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userUseCase.loginUser(request))
    }

    @Operation(summary = "회원 자동 로그인")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
            ),
            ApiResponse(
                responseCode = "422",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    @PostMapping("/user/auto-login")
    fun userAutoLogin(@Valid @RequestBody request: UserAutoLogInCommand) {
        userUseCase.autoLoginUser(request)
    }

    @Operation(summary = "회원 로그인 토큰 갱신")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
            ),
            ApiResponse(
                responseCode = "422",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    @PostMapping("/user/refresh-token")
    fun userRefreshToken(@Valid @RequestBody request: RefreshTokenCommand): ResponseEntity<Token> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userUseCase.refreshToken(request))
    }
}
