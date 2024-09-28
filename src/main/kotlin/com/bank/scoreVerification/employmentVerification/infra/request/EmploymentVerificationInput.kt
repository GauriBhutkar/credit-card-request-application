package com.bank.scoreVerification.employmentVerification.infra.request

import com.bank.creditcardrequest.infra.request.EmploymentDetails
import java.util.UUID

class EmploymentVerificationInput(
    val customerId: UUID,
    val customerName: String,
    val mobileNumber: String,
    val employerDetails: EmploymentDetails,
)
