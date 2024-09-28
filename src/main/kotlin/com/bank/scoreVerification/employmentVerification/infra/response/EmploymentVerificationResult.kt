package com.bank.scoreVerification.employmentVerification.infra.response

import com.bank.creditcardrequest.domain.VerificationStatus

data class EmploymentVerificationResult(
    val verificationStatus: VerificationStatus,
    val verificationScore: Int,
)
