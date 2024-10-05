package com.bank.creditcardrequestprocessor.infra.response

import com.bank.creditcardrequestprocessor.domain.DispatchStatus
import java.util.UUID

class CreditCardApplicationProcessResponse(
    val applicationId: UUID,
    val dispatchStatus: DispatchStatus,
    val overallScore: Int,
    val message: String? = null,
)
