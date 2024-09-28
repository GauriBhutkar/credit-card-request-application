package com.bank.scoreVerification.riskevaluation.infra.response

import com.bank.creditcardrequest.domain.VerificationStatus

data class RiskEvaluationResult(
    val verificationStatus: VerificationStatus,
    val verificationScore: Int,
)
