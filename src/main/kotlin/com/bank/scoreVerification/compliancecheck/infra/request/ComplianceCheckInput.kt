package com.bank.scoreVerification.compliancecheck.infra.request

import com.bank.creditcardrequest.infra.request.EmploymentDetails
import java.io.File
import java.util.UUID

class ComplianceCheckInput(
    val customerId: UUID,
    val customerName: String,
    val mobileNumber: String,
    val employerDetails: EmploymentDetails,
    val creditCardLimit: Long,
    val bankStatement: File,
)
