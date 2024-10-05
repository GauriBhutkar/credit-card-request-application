package com.bank.creditcardrequestprocessor.client

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.creditcardrequest.infra.request.EmploymentDetails
import com.bank.creditcardrequest.infra.request.EmploymentStatus
import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.mockPost
import com.bank.scoreVerification.compliancecheck.infra.request.ComplianceCheckInput
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
@AutoConfigureWireMock(port = 8687)
class checkComplianceClientTest {

    private val complianceCheckClient = ComplianceCheckClient(
        "http://localhost:8687",
    )

    @Test
    fun `should finish compliance check`() {
        val input = ComplianceCheckInput(
            UUID.randomUUID(),
            "John",
            "989999900",
            EmploymentDetails(EmploymentStatus.STUDENT),
            20000,
            File.createTempFile("bank-statement", ".pdf"),
        )
        val result = StepResult(VerificationStatus.VERIFIED, 30)
        mockPost(
            WireMock.urlMatching("/api/v1/credit-card/compliance-check"),
            200,
            jacksonObjectMapper().writeValueAsString(result),
        )

        StepVerifier.create(complianceCheckClient.checkCompliance(input))
            .consumeNextWith {
                assertEquals(it.verificationScore, result.verificationScore)
                assertEquals(it.verificationStatus, result.verificationStatus)
            }.verifyComplete()
    }
}
