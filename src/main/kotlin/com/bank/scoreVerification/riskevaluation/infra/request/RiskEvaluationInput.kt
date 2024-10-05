package com.bank.scoreVerification.riskevaluation.infra.request

import com.bank.creditcardrequest.infra.request.EmploymentDetails
import java.util.UUID

class RiskEvaluationInput(
    val customerId: UUID,
    val customerName: String,
    val nationality: String,
    val employmentDetails: EmploymentDetails,
    val creditCardLimit: Long,
    val additionalInformation: String?,
)
