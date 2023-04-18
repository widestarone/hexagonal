package com.hexagonal.domain.util

import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.createType
import kotlin.reflect.jvm.jvmName

class Randomizer {
    class NoUsableConstructor() : Error()

    companion object {

        inline fun <reified T : Any> makeRandomInstance(): T {
            return makeRandomInstance(T::class, getKType<T>()) as T
        }

        private var random: Random = Random

        fun makeRandomInstance(type: KType): Any? {
            return makeRandomInstance(type.classifier as KClass<*>, type)
        }

        fun makeRandomInstance(clazz: KClass<*>, type: KType): Any? {
            if (type.isMarkedNullable && random.nextBoolean()) {
                return null
            }

            val primitive = makeStandardInstanceOrNull(clazz, type)
            if (primitive != null) {
                return primitive
            }

            val constructors = clazz.constructors.shuffled(random)

            for (constructor in constructors) {
                try {
                    val arguments = constructor.parameters
                        .map { makeRandomInstance(it.type) }
                        .toTypedArray()

                    return constructor.call(*arguments)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            throw NoUsableConstructor()
        }

        private fun makeStandardInstanceOrNull(clazz: KClass<*>, type: KType): Any? = when (clazz) {
            Any::class -> "Anything" // We should randomize among some types
            Int::class -> random.nextInt()
            Long::class -> random.nextLong()
            Double::class -> random.nextDouble()
            Float::class -> random.nextFloat()
            Char::class -> makeRandomChar()
            String::class -> makeRandomString()
            Instant::class -> Instant.now()
            List::class, Collection::class -> makeRandomList(type)
            Map::class -> makeRandomMap(type)
            BigDecimal::class -> BigDecimal(random.nextLong())
            Boolean::class -> random.nextBoolean()
            LocalDate::class -> LocalDate.now()
            LocalTime::class -> LocalTime.now()
            LocalDateTime::class -> LocalDateTime.now()
            else -> try {
                val enumClz = Class.forName(clazz.jvmName).enumConstants as Array<*>
                enumClz.random()
            } catch (e: Exception) {
                null
            }
        }

        private fun makeRandomList(type: KType): List<Any?> {
            val numOfElements = random.nextInt(10)
            val elemType = type.arguments[0].type!!
            return (1..numOfElements)
                .map { makeRandomInstance(elemType) }
        }

        private fun makeRandomMap(type: KType): Map<Any?, Any?> {
            val numOfElements = random.nextInt(10)
            val keyType = type.arguments[0].type!!
            val valType = type.arguments[1].type!!
            val keys = (1..numOfElements)
                .map { makeRandomInstance(keyType) }
            val values = (1..numOfElements)
                .map { makeRandomInstance(valType) }
            return keys.zip(values).toMap()
        }

        private fun makeRandomChar() = ('A'..'z').random(random)
        private fun makeRandomString() = (1..random.nextInt(100))
            .map { makeRandomChar() }
            .joinToString(separator = "") { "$it" }
    }
}

// getKType

// --- Interface ---

inline fun <reified T : Any> getKType(): KType =
    object : SuperTypeTokenHolder<T>() {}.getKTypeImpl()

// --- Implementation ---
@Suppress("unused")
open class SuperTypeTokenHolder<T>

fun SuperTypeTokenHolder<*>.getKTypeImpl(): KType =
    javaClass.genericSuperclass.toKType().arguments.single().type!!

fun KClass<*>.toInvariantFlexibleProjection(arguments: List<KTypeProjection> = emptyList()): KTypeProjection {
    // Currently we always produce a non-null type, which is obviously wrong
    val args = when (java.isArray) {
        true -> listOf(java.componentType.kotlin.toInvariantFlexibleProjection())
        else -> arguments
    }
    return KTypeProjection.invariant(createType(args, nullable = false))
}

fun Type.toKTypeProjection(): KTypeProjection = when (this) {
    is Class<*> -> this.kotlin.toInvariantFlexibleProjection()
    is ParameterizedType -> {
        val erasure = (rawType as Class<*>).kotlin
        erasure.toInvariantFlexibleProjection(
            (
                erasure.typeParameters.zip(actualTypeArguments)
                    .map { (parameter, argument) ->
                        val projection = argument.toKTypeProjection()
                        projection.takeIf {
                            parameter.variance == KVariance.INVARIANT || parameter.variance != projection.variance
                        } ?: KTypeProjection.invariant(projection.type!!)
                    }
                ),
        )
    }
    is WildcardType -> when {
        lowerBounds.isNotEmpty() -> KTypeProjection.contravariant(lowerBounds.single().toKType())
        upperBounds.isNotEmpty() -> KTypeProjection.covariant(upperBounds.single().toKType())
        else -> KTypeProjection.STAR
    }
    is GenericArrayType ->
        Array<Any>::class.toInvariantFlexibleProjection(listOf(genericComponentType.toKTypeProjection()))
    is TypeVariable<*> -> TODO() // TODO
    else -> throw IllegalArgumentException("Unsupported type: $this")
}

fun Type.toKType(): KType = toKTypeProjection().type!!
