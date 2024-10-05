package com.bank.creditcardrequest.domain

import com.bank.creditcardrequest.infra.request.CustomerDetailsInput
import com.bank.creditcardrequest.infra.request.EmploymentDetails
import java.io.File
import java.util.UUID

class CreditCardApplication(
    val id: UUID,
    val customerId: UUID,
    val customerName: String,
    val mobileNumber: String,
    val nationality: String,
    val address: String,
    val annualIncome: Long,
    val employmentDetails: EmploymentDetails,
    val creditCardLimit: Long,
    val bankStatement: File,
) {
    companion object {
        fun from(application: CustomerDetailsInput): CreditCardApplication {
            return CreditCardApplication(
                id = UUID.randomUUID(),
                customerId = application.customerId,
                customerName = application.customerName,
                mobileNumber = application.mobileNumber,
                nationality = application.nationality,
                address = application.address,
                annualIncome = application.annualIncome,
                employmentDetails = application.employmentDetails,
                creditCardLimit = application.creditCardLimit,
                bankStatement = application.bankStatement,
            )
        }
    }
}

enum class VerificationStatus {
    VERIFIED, PENDING, IN_PROGRESS, REJECTED
}
