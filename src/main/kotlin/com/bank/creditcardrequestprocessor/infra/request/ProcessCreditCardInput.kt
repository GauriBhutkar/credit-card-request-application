package com.bank.creditcardrequestprocessor.infra.request

import com.bank.creditcardrequest.infra.request.EmploymentDetails
import java.io.File
import java.util.UUID

class ProcessCreditCardInput(
    val customerId: UUID,
    val customerName: String,
    val mobileNumber: String,
    val nationality: String,
    val annualIncome: Long,
    val employmentDetails: EmploymentDetails,
    val creditCardLimit: Long,
    val bankStatement: File,
    val additionalInput: String?,
)
