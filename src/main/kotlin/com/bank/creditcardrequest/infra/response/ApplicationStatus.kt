package com.bank.creditcardrequest.infra.response

import java.time.LocalDateTime
import java.util.UUID

class ApplicationStatus(
    val applicationId: UUID,
    val status: Status,
    val submittedAt: LocalDateTime = LocalDateTime.now(),
)

enum class Status {
    UNDER_REVIEW
}
