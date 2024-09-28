package com.bank.scoreVerification.compliancecheck.domain

import com.bank.creditcardrequest.domain.VerificationStatus

class ComplianceCheckRecord(
    val complianceStatus: String,
    val blacklistCheckResult: BlacklistCheckResult,
    val kycStatus: String,
    val amlStatus: String,
    val complianceRemarks: String,
    val verificationMethod: String,
    val verificationStatus: VerificationStatus,
    val verificationScore: Int,
)

class BlacklistCheckResult(
    val status: String,
    val details: String?,
)
