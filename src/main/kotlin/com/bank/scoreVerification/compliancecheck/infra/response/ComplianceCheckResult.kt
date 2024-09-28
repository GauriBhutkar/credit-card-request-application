package com.bank.scoreVerification.compliancecheck.infra.response

import com.bank.creditcardrequest.domain.VerificationStatus

data class ComplianceCheckResult(
    val verificationStatus: VerificationStatus,
    val verificationScore: Int,
)
