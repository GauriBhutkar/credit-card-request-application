package com.bank.scoreVerification.riskevaluation.domain

import com.bank.creditcardrequest.domain.VerificationStatus
import java.util.UUID

class RiskEvaluationRecord(
    val id: UUID,
    val customerId: UUID,
    val creditLimitRecommendation: Double,
    val detailedReport: RiskEvaluationReport,
    val remarks: String?,
    val verificationMethod: String,
    val verificationStatus: VerificationStatus,
    val verificationScore: Int,
)

class BlacklistCheckResult(
    val status: String,
    val details: String?,
)

enum class ComplianceStatus {
    COMPLIANT,
    NON_COMPLIANT,
}

data class RiskEvaluationReport(
    val financialHistorySummary: String,
    val paymentBehavior: String,
    val existingDebt: Double,
    val recentInquiries: Int,
)
