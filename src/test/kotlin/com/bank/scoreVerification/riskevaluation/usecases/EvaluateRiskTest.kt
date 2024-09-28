package com.bank.scoreVerification.riskevaluation.usecases

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.creditcardrequest.infra.request.EmploymentDetails
import com.bank.creditcardrequest.infra.request.EmploymentStatus
import com.bank.scoreVerification.riskevaluation.client.RiskEvaluationESBClient
import com.bank.scoreVerification.riskevaluation.domain.RiskEvaluationRecord
import com.bank.scoreVerification.riskevaluation.domain.RiskEvaluationReport
import com.bank.scoreVerification.riskevaluation.infra.request.RiskEvaluationInput
import com.bank.scoreVerification.riskevaluation.infra.response.RiskEvaluationResult
import com.bank.scoreVerification.riskevaluation.repositories.RiskEvaluationRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.UUID

class EvaluateRiskTest {

    private val riskEvaluationESBClient: RiskEvaluationESBClient = mockk()
    private val riskEvaluationRepository: RiskEvaluationRepository = mockk()
    private val evaluateRisk: EvaluateRisk =
        EvaluateRisk(riskEvaluationESBClient, riskEvaluationRepository)
    private val customerId = UUID.randomUUID()

    private val riskReport = RiskEvaluationReport(
        financialHistorySummary = "Stable income with minimal debt.",
        paymentBehavior = "On-time payments for the last 24 months.",
        existingDebt = 15000.00,
        recentInquiries = 2,
    )

    private val riskEvaluationResponse = RiskEvaluationRecord(
        id = UUID.randomUUID(),
        customerId = customerId,
        creditLimitRecommendation = 5000.00,
        detailedReport = riskReport,
        remarks = "Applicant shows strong creditworthiness.",
        verificationScore = 90,
        verificationMethod = "Manual",
        verificationStatus = VerificationStatus.VERIFIED,
    )

    @Test
    fun `should verify risk and return RiskEvaluationResult`() {
        val input = RiskEvaluationInput(
            customerId,
            "John Doe",
            "9987654321",
            EmploymentDetails(EmploymentStatus.FULL_TIME, "Tech Solutions"),
            20000.0,
            null,
        )

        val expectedResult = RiskEvaluationResult(
            verificationStatus = VerificationStatus.VERIFIED,
            verificationScore = 90,
        )
        every { riskEvaluationESBClient.evaluateRisk(input) } returns Mono.just(riskEvaluationResponse)
        every { riskEvaluationRepository.save(riskEvaluationResponse) } returns Mono.just(riskEvaluationResponse)

        val result: Mono<RiskEvaluationResult> = evaluateRisk(input)

        StepVerifier.create(result).consumeNextWith { assertEquals(it, expectedResult) }.verifyComplete()
    }
}
