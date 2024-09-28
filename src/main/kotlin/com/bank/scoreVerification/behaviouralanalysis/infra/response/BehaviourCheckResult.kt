package com.bank.scoreVerification.behaviouralanalysis.infra.response

import com.bank.creditcardrequest.domain.VerificationStatus

data class BehaviourCheckResult(
    val verificationStatus: VerificationStatus,
    val verificationScore: Int,
)
