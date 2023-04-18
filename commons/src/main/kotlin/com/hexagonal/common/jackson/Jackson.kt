package com.hexagonal.common.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.commons.lang3.StringUtils

object Jackson {
    private val MAPPER = jacksonObjectMapper().registerKotlinModule()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(JavaTimeModule())

    private val YAML_MAPPER = ObjectMapper(YAMLFactory()).registerKotlinModule()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(JavaTimeModule())

    private val EMPTY = ByteArray(0)

    fun mapper(): ObjectMapper {
        return MAPPER
    }

    fun yamlMapper(): ObjectMapper {
        return YAML_MAPPER
    }

    fun writeValueAsString(message: Any): String {
        return try {
            mapper().writeValueAsString(message)
        } catch (e: JsonProcessingException) {
            StringUtils.EMPTY
        }
    }

    fun writeValueAsBytes(message: Any): ByteArray {
        return try {
            mapper().writeValueAsBytes(message)
        } catch (e: JsonProcessingException) {
            EMPTY
        }
    }

    fun <T> readValue(message: String, valueType: Class<T>): T? {
        return try {
            return mapper().readValue(message, valueType)
        } catch (e: JsonProcessingException) {
            null
        }
    }
}
