package com.bank.creditcardrequestprocessor.client

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.creditcardrequest.infra.request.EmploymentDetails
import com.bank.creditcardrequest.infra.request.EmploymentStatus
import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.mockPost
import com.bank.scoreVerification.riskevaluation.infra.request.RiskEvaluationInput
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier
import java.util.*

@ExtendWith(SpringExtension::class)
@AutoConfigureWireMock(port = 8687)
class RiskEvaluationClientTest {

    private val riskEvaluationClient = RiskEvaluationClient(
        "http://localhost:8687",
    )

    @Test
    fun `should finish risk evaluation`() {
        val input = RiskEvaluationInput(
            UUID.randomUUID(),
            "John",
            "INDIA",
            EmploymentDetails(EmploymentStatus.STUDENT),
            30000,
            null,
        )
        val result = StepResult(VerificationStatus.VERIFIED, 30)
        mockPost(
            WireMock.urlMatching("/api/v1/credit-card/risk-evaluation"),
            200,
            jacksonObjectMapper().writeValueAsString(result),
        )

        StepVerifier.create(riskEvaluationClient.evaluateRisk(input))
            .consumeNextWith {
                assertEquals(it.verificationScore, result.verificationScore)
                assertEquals(it.verificationStatus, result.verificationStatus)
            }.verifyComplete()
    }
}
