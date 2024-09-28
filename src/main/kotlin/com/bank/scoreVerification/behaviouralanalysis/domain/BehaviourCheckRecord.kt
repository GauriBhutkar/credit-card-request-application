package com.bank.scoreVerification.behaviouralanalysis.domain

import com.bank.creditcardrequest.domain.VerificationStatus
import java.util.UUID

class BehaviourCheckRecord(
    val customerId: UUID,
    val predictedCreditBehavior: CreditBehavior,
    val spendingPatterns: SpendingPatternSummary,
    val paymentHistorySummary: PaymentHistorySummary,
    val verificationMethod: String,
    val verificationStatus: VerificationStatus,
    val verificationScore: Int,
)

data class CreditBehavior(
    val riskScore: Int,
    val likelihoodOfDefault: Double,
)

data class SpendingPatternSummary(
    val totalSpentLastMonth: Double,
    val averageMonthlySpending: Double,
)

data class PaymentHistorySummary(
    val onTimePaymentsPercentage: Double,
    val totalMissedPayments: Int,
    val averagePaymentDelayDays: Double,
)
