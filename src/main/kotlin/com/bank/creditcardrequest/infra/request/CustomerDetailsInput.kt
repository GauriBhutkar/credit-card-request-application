package com.bank.creditcardrequest.infra.request

import java.io.File
import java.util.UUID

class CustomerDetailsInput(
    val customerId: UUID,
    val customerName: String,
    val mobileNumber: String,
    val nationality: String,
    val address: String,
    val annualIncome: Long,
    val employmentDetails: EmploymentDetails,
    val creditCardLimit: Long,
    val bankStatement: File
)

class EmploymentDetails(
    val employmentStatus: EmploymentStatus,
    val employerName: String?
)

enum class EmploymentStatus {
    FULL_TIME, PART_TIME, UNEMPLOYED, STUDENT, RETIRED
}