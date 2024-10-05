package com.bank.creditcardrequestprocessor.client

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.mockPost
import com.bank.scoreVerification.behaviouralanalysis.infra.request.BehaviourCheckInput
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier
import java.io.File
import java.util.UUID

@ExtendWith(SpringExtension::class)
@AutoConfigureWireMock(port = 8688)
class BehaviourAnalysisClientTest {

    private val behaviourAnalysisClient = BehaviourAnalysisClient(
        "http://localhost:8688",
    )

    @Test
    fun `should finish behaviour check`() {
        val input = BehaviourCheckInput(
            UUID.randomUUID(),
            File.createTempFile("bank-statement", ".pdf"),
        )
        val result = StepResult(VerificationStatus.VERIFIED, 40)
        mockPost(
            WireMock.urlMatching("/api/v1/credit-card/behaviour-check"),
            200,
            jacksonObjectMapper().writeValueAsString(result),
        )

        StepVerifier.create(behaviourAnalysisClient.analyzeBehavior(input))
            .consumeNextWith {
                assertEquals(it.verificationScore, result.verificationScore)
                assertEquals(it.verificationStatus, result.verificationStatus)
            }.verifyComplete()
    }
}
