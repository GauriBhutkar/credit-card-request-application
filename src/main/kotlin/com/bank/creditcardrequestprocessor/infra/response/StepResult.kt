package com.bank.creditcardrequestprocessor.infra.response

import com.bank.creditcardrequest.domain.VerificationStatus

data class StepResult(
    val verificationStatus: VerificationStatus,
    val verificationScore: Int,
)
