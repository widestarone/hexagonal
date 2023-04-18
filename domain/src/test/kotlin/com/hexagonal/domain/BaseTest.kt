package com.hexagonal.domain

import com.hexagonal.domainTestApplication
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(classes = [DomainTestApplication::class])
@SuppressWarnings("UnnecessaryAbstractClass")
@Suppress("NonAsciiCharacters")
abstract class BaseTest
