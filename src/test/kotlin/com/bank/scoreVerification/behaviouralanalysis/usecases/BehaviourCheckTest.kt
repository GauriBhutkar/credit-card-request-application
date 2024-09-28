package com.bank.scoreVerification.behaviouralanalysis.usecases

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.scoreVerification.behaviouralanalysis.client.BehaviouralCheckESBClient
import com.bank.scoreVerification.behaviouralanalysis.domain.BehaviourCheckRecord
import com.bank.scoreVerification.behaviouralanalysis.domain.CreditBehavior
import com.bank.scoreVerification.behaviouralanalysis.domain.PaymentHistorySummary
import com.bank.scoreVerification.behaviouralanalysis.domain.SpendingPatternSummary
import com.bank.scoreVerification.behaviouralanalysis.infra.request.BehaviourCheckInput
import com.bank.scoreVerification.behaviouralanalysis.infra.response.BehaviourCheckResult
import com.bank.scoreVerification.behaviouralanalysis.repositories.BehaviourCheckRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.io.File
import java.util.UUID

class BehaviourCheckTest {
    private val behaviouralCheckESBClient: BehaviouralCheckESBClient = mockk()
    private val behaviourCheckRepository: BehaviourCheckRepository = mockk()
    private val behaviourCheck =
        BehaviourCheck(behaviouralCheckESBClient, behaviourCheckRepository)
    private val customerId = UUID.randomUUID()

    private val behaviourCheckResponse = BehaviourCheckRecord(
        customerId = customerId,
        predictedCreditBehavior = CreditBehavior(
            riskScore = 720,
            likelihoodOfDefault = 0.05,
        ),
        spendingPatterns = SpendingPatternSummary(
            totalSpentLastMonth = 1200.00,
            averageMonthlySpending = 1100.00,
        ),
        paymentHistorySummary = PaymentHistorySummary(
            onTimePaymentsPercentage = 95.0,
            totalMissedPayments = 2,
            averagePaymentDelayDays = 5.0,
        ),
        verificationMethod = "API",
        verificationStatus = VerificationStatus.VERIFIED,
        verificationScore = 90,
    )

    @Test
    fun `should verify risk and return BehaviourCheckResult`() {
        val input = BehaviourCheckInput(
            customerId,
            File.createTempFile("bank-statement", ".pdf"),
        )

        val expectedResult = BehaviourCheckResult(
            verificationStatus = VerificationStatus.VERIFIED,
            verificationScore = 90,
        )
        every { behaviouralCheckESBClient.behaviourCheck(input) } returns Mono.just(behaviourCheckResponse)
        every { behaviourCheckRepository.save(behaviourCheckResponse) } returns Mono.just(behaviourCheckResponse)

        val result: Mono<BehaviourCheckResult> = behaviourCheck(input)

        StepVerifier.create(result).consumeNextWith { assertEquals(it, expectedResult) }.verifyComplete()
    }
}
