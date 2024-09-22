package com.bank.creditcardrequest.domain

import com.bank.creditcardrequest.infra.request.CustomerDetailsInput
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
                employmentDetails = EmploymentDetails.from(application.employmentDetails),
                creditCardLimit = application.creditCardLimit,
                bankStatement = application.bankStatement,
            )
        }
    }
}

data class EmploymentDetails(
    val employmentStatus: String,
    val employerName: String?,
) {
    companion object {
        fun from(details: com.bank.creditcardrequest.infra.request.EmploymentDetails): EmploymentDetails {
            return EmploymentDetails(
                details.employmentStatus.name,
                details.employerName,
            )
        }
    }
}
