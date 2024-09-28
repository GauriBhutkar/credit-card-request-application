package com.bank.scoreVerification.employmentVerification.domain

import com.bank.creditcardrequest.domain.VerificationStatus
import java.util.UUID

class EmploymentRecord(
    val id: UUID,
    val employerDetails: EmployerDetails,
    val jobTitle: String,
    val position: String,
    val employmentStatus: String,
    val incomeInformation: IncomeInformation,
    val employmentType: EmploymentType,
    val verificationMethod: String,
    val verificationStatus: VerificationStatus,
    val verificationScore: Int,
)

class EmployerDetails(
    val employerName: String,
    val employerContact: String,
    val employerAddress: String,
)

class IncomeInformation(
    val amount: Double,
    val frequency: String,
)

enum class EmploymentType {
    PERMANENT, PROBATIONARY, CONTRACTUAL
}
