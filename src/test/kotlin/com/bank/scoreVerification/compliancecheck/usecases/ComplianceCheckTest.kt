package com.bank.scoreVerification.compliancecheck.usecases

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.creditcardrequest.infra.request.EmploymentDetails
import com.bank.creditcardrequest.infra.request.EmploymentStatus
import com.bank.scoreVerification.compliancecheck.client.ComplianceCheckESBClient
import com.bank.scoreVerification.compliancecheck.domain.BlacklistCheckResult
import com.bank.scoreVerification.compliancecheck.domain.ComplianceCheckRecord
import com.bank.scoreVerification.compliancecheck.infra.request.ComplianceCheckInput
import com.bank.scoreVerification.compliancecheck.infra.response.ComplianceCheckResult
import com.bank.scoreVerification.compliancecheck.repositories.ComplianceCheckRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.io.File
import java.util.*

class ComplianceCheckTest {

    private val complianceCheckESBClient: ComplianceCheckESBClient = mockk()
    private val complianceCheckRepository: ComplianceCheckRepository = mockk()
    private val complianceCheck: ComplianceCheck =
        ComplianceCheck(complianceCheckESBClient, complianceCheckRepository)

    private val complianceCheckRecord = ComplianceCheckRecord(
        complianceStatus = "COMPLIANT",
        blacklistCheckResult = BlacklistCheckResult(
            status = "CLEAN",
            details = null,
        ),
        kycStatus = "VERIFIED",
        amlStatus = "CLEAR",
        complianceRemarks = "All checks passed successfully.",
        verificationMethod = "Automated checks against databases",
        verificationStatus = VerificationStatus.VERIFIED,
        verificationScore = 30,
    )

    @Test
    fun `should check compliance details and return ComplianceCheckResult`() {
        val customerId = UUID.randomUUID()
        val input = ComplianceCheckInput(
            customerId,
            "John Doe",
            "9987654321",
            EmploymentDetails(EmploymentStatus.FULL_TIME, "Tech Solutions"),
            28000,
            File.createTempFile("bank_statement", ".pdf"),
        )

        val expectedResult = ComplianceCheckResult(
            verificationStatus = VerificationStatus.VERIFIED,
            verificationScore = 30,
        )
        every { complianceCheckESBClient.complianceCheck(input) } returns Mono.just(complianceCheckRecord)
        every { complianceCheckRepository.save(complianceCheckRecord) } returns Mono.just(complianceCheckRecord)

        val result: Mono<ComplianceCheckResult> = complianceCheck(input)

        StepVerifier.create(result).consumeNextWith { assertEquals(it, expectedResult) }.verifyComplete()
    }
}
