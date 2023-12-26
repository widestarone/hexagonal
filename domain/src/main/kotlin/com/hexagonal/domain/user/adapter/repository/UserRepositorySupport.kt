package com.hexagonal.domain.user.adapter.repository

import com.hexagonal.domain.user.adapter.entity.UserEntity
import com.hexagonal.domain.user.application.port.out.UserListCommand
import com.hexagonal.domain.user.constant.UserSortType
import com.hexagonal.domain.user.constant.UserStatus
import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.Relation
import com.linecorp.kotlinjdsl.querydsl.where.WhereDsl
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.pageQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.criteria.JoinType

@Repository
class UserRepositorySupport(
    val queryFactory: SpringDataQueryFactory,
) {

    fun findUserList(
        command: UserListCommand,
    ): Page<UserEntity> {
        val sort = when (UserSortType.NONE == command.sortType) {
            true -> Sort.by("id").descending()
            else -> when (command.isDesc) {
                true -> Sort.by(command.sortType.value).descending()
                else -> Sort.by(command.sortType.value).ascending()
            }
        }

        val pageable = PageRequest.of(
            command.pageIndex - 1,
            command.pageSize,
            sort,
        )

        return queryFactory.pageQuery(pageable) {
            selectDistinct(entity(UserEntity::class))
            from(entity(UserEntity::class))
            where(userFirstName(command.userFirstName))
            where(userLastName(command.userLastName))
            where(email(command.email))
            where(userStatus(command.userStatus))
            where(createdAt(command.startCreatedAt, command.endCreatedAt))
            where(lastLoginAt(command.startLastLoginAt, command.endLastLoginAt))
        }
    }

    /**
     * userFirstName 으로 조회
     */
    private fun WhereDsl.userFirstName(userFirstName: String?): PredicateSpec {
        return and(
            takeIf { userFirstName != null }?.let { column(UserEntity::userFirstName).like("%$userFirstName%") },
        )
    }

    /**
     * userLastName 으로 조회
     */
    private fun WhereDsl.userLastName(userLastName: String?): PredicateSpec {
        return and(
            takeIf { userLastName != null }?.let { column(UserEntity::userLastName).like("%$userLastName%") },
        )
    }

    /**
     * email로 조회
     */
    private fun WhereDsl.email(email: String?): PredicateSpec {
        return and(
            takeIf { email != null }?.let { column(UserEntity::email).like("%$email%") },
        )
    }

    /**
     * companyName으로 조회
     */
//    private fun WhereDsl.companyName(companyName: String?): PredicateSpec {
//        return and(
//            takeIf { companyName != null }?.let { column(UserEntity::companyName).like("%$companyName%") },
//        )
//    }

    /**
     * nationCode로 조회
     */
//    private fun WhereDsl.nationCode(nationCode: String?): PredicateSpec {
//        return and(
//            takeIf { nationCode != null }?.let { column(UserEntity::nationCode).equal(nationCode!!) },
//        )
//    }

    /**
     * industryType 으로 조회
     */
//    private fun WhereDsl.industryType(industryType: IndustryType?): PredicateSpec {
//        return and(
//            takeIf { industryType != null }?.let { column(UserEntity::industryType).equal(industryType!!) },
//        )
//    }

    /**
     * userStatus로 조회
     */
    private fun WhereDsl.userStatus(userStatus: UserStatus?): PredicateSpec {
        return and(
            takeIf { userStatus != null }?.let { column(UserEntity::userStatus).equal(userStatus!!) },
        )
    }

    /**
     * 시작, 마지막 생성일로 조회
     */
    private fun WhereDsl.createdAt(startCreatedAt: LocalDateTime?, endCreatedAt: LocalDateTime?): PredicateSpec {
        return and(
            takeIf { startCreatedAt != null && endCreatedAt != null }?.let { column(UserEntity::createdAt).between(startCreatedAt!!, endCreatedAt!!) },
        )
    }

    /**
     * 시작, 마지막 최근 접속일로 조회
     */
    private fun WhereDsl.lastLoginAt(startLastLoginAt: LocalDateTime?, endLastLoginAt: LocalDateTime?): PredicateSpec {
        return and(
            takeIf { startLastLoginAt != null && endLastLoginAt != null }?.let { column(UserEntity::lastLoginAt).between(startLastLoginAt!!, endLastLoginAt!!) },
        )
    }

//    fun findUserSubscribeList(
//        command: UserSubscribeListCommand,
//    ): Page<UserEntity> {
//        val sort = when (UserSortType.NONE == command.sortType) {
//            true -> Sort.by("id").descending()
//            else -> when (command.isDesc) {
//                true -> Sort.by(command.sortType.value).descending()
//                else -> Sort.by(command.sortType.value).ascending()
//            }
//        }
//
//        val pageable = PageRequest.of(
//            command.pageIndex - 1,
//            command.pageSize,
//            sort,
//        )
//
//        return queryFactory.pageQuery(pageable) {
//            selectDistinct(entity(UserEntity::class))
//            from(entity(UserEntity::class))
//            join(
//                entity(UserEntity::class),
//                entity(SubscribePlanEntity::class),
//                Relation<UserEntity, SubscribePlanEntity>("subscribePlanEntity"),
//                JoinType.INNER,
//            )
//            where(userFirstName(command.userFirstName))
//            where(userLastName(command.userLastName))
//            where(email(command.email))
//            where(companyName(command.companyName))
//            where(nationCode(command.nationCode))
//            where(industryType(command.industryType))
//            where(planType(command.planType))
//            where(planModifiedAt(command.startPlanModifiedAt, command.endPlanModifiedAt))
//            where(paidAt(command.startPaidAt, command.endPaidAt))
//        }
//    }

//    private fun WhereDsl.planType(planType: SubscribePlanType?): PredicateSpec {
//        return and(
//            planType?.let { column(SubscribePlanEntity::planType).equal(planType) },
//        )
//    }

//    private fun WhereDsl.planModifiedAt(startPlanModifiedAt: LocalDateTime?, endPlanModifiedAt: LocalDateTime?): PredicateSpec {
//        return and(
//            takeIf { startPlanModifiedAt != null && endPlanModifiedAt != null }?.let { column(SubscribePlanEntity::planModifiedAt).between(startPlanModifiedAt!!, endPlanModifiedAt!!) },
//        )
//    }
//
//    private fun WhereDsl.paidAt(startPaidAt: LocalDateTime?, endPaidAt: LocalDateTime?): PredicateSpec {
//        return and(
//            takeIf { startPaidAt != null && endPaidAt != null }?.let { column(SubscribePlanEntity::paidAt).between(startPaidAt!!, endPaidAt!!) },
//        )
//    }
}
