package com.bank.creditcardrequestprocessor.client

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.creditcardrequest.infra.request.EmploymentDetails
import com.bank.creditcardrequest.infra.request.EmploymentStatus
import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.mockPost
import com.bank.scoreVerification.employmentVerification.infra.request.EmploymentVerificationInput
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier
import java.util.UUID

@ExtendWith(SpringExtension::class)
@AutoConfigureWireMock(port = 8687)
class verifyEmploymentClientTest {

    private val employmentVerificationClient = EmploymentVerificationClient(
        "http://localhost:8687",
    )

    @Test
    fun `should finish employment verification`() {
        val input = EmploymentVerificationInput(
            UUID.randomUUID(),
            "John",
            "989999900",
            EmploymentDetails(EmploymentStatus.STUDENT),
        )
        val result = StepResult(VerificationStatus.VERIFIED, 30)
        mockPost(
            WireMock.urlMatching("/api/v1/credit-card/employment-verifications"),
            200,
            jacksonObjectMapper().writeValueAsString(result),
        )

        StepVerifier.create(employmentVerificationClient.verifyEmployment(input))
            .consumeNextWith {
                assertEquals(it.verificationScore, result.verificationScore)
                assertEquals(it.verificationStatus, result.verificationStatus)
            }.verifyComplete()
    }
}
