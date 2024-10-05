package com.bank.scoreVerification.identityverification.domain

import com.bank.creditcardrequest.domain.VerificationStatus
import java.util.UUID

class IdentityVerificationRecord(
    val id: UUID,
    val customerId: UUID,
    val customerName: String,
    val detailedReport: IdentityVerificationReport,
    val remarks: String?,
    val verificationMethod: String,
    val verificationStatus: VerificationStatus,
    val verificationScore: Int,
)

data class IdentityVerificationReport(
    val name: String,
    val maritalStatus: String,
    val gender: String,
    val defaultCount: Int,
    val salary: Double,
)
